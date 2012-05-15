/**
 * 
 */
package de.ostfalia.viewer.inputrecorder;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.glavsoft.exceptions.CommonException;
import com.glavsoft.exceptions.ProtocolException;
import com.glavsoft.exceptions.TransportException;
import com.glavsoft.rfb.ClipboardController;
import com.glavsoft.rfb.IRepaintController;
import com.glavsoft.rfb.ISessionController;
import com.glavsoft.rfb.client.FramebufferUpdateRequestMessage;
import com.glavsoft.rfb.client.SetPixelFormatMessage;
import com.glavsoft.rfb.encoding.EncodingType;
import com.glavsoft.rfb.encoding.decoder.Decoder;
import com.glavsoft.rfb.encoding.decoder.DecodersContainer;
import com.glavsoft.rfb.encoding.decoder.FramebufferUpdateRectangle;
import com.glavsoft.rfb.encoding.decoder.RichCursorDecoder;
import com.glavsoft.rfb.protocol.ProtocolContext;
import com.glavsoft.rfb.protocol.ReceiverTask;
import com.glavsoft.transport.Reader;

import de.ostfalia.viewer.logger.CLogger;

/**
 * @author O. Laudi
 *
 */
public class CVNCServerTester extends ReceiverTask {

	protected long	m_lFramecount;
	protected long	m_lEqualCount;
	private boolean	m_isDifferent;
	
	
	
	/**
	 * Constructor
	 * @param reader
	 * @param repaintController
	 * @param clipboardController
	 * @param sessionManager
	 * @param decoders
	 * @param context
	 */
	public CVNCServerTester(Reader reader,
			IRepaintController repaintController, ClipboardController clipboardController,
			ISessionController sessionManager, DecodersContainer decoders,
			ProtocolContext context) {
		super(reader, repaintController, clipboardController, sessionManager, decoders, context);
		m_isDifferent = false;
	}

	/**
	 * returns the equalrate of all frames since the thread starts.
	 * 0.0 = all frames are different
	 * 1.0 = all frames are equal
	 * @return equalRatio
	 */
	public double getEqualRatio() {
		double dER = 0.0;
		
		if (m_lFramecount == 0)
			dER = 0.0;
		else
			dER = (double)m_lEqualCount / (double)m_lFramecount;
		
		return dER;
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.glavsoft.rfb.protocol.ReceiverTask#run()
	 */
	@Override
	public void run() {
		long time = System.currentTimeMillis();
		isRunning = true;
		while (isRunning) {
			if((System.currentTimeMillis()-time)>5000){
				isRunning = false;
			}
			try {
				byte messageId = reader.readByte();
				CLogger.getInst(CLogger.SYS_OUT).writeline("ReceiverTask::run(): messageId " + messageId);
				switch (messageId) {
				case FRAMEBUFFER_UPDATE:
//					logger.fine("Server message: FramebufferUpdate (0)");
					framebufferUpdateMessage();
					break;
				case SET_COLOR_MAP_ENTRIES:
					logger.severe("Server message SetColorMapEntries is not implemented.");
					break;
				case BELL:
					logger.fine("Server message: Bell");
					System.out.print("\0007");
				    System.out.flush();
					break;
				case SERVER_CUT_TEXT:
					logger.fine("Server message: CutText (3)");
					serverCutText();
					break;
				default:
					logger.severe("Unsupported server message. Id = " + messageId);
				}
			} catch (TransportException e) {
				logger.severe("Close session: " + e.getMessage());
				if (isRunning) {
					sessionManager.stopTasksAndRunNewSession("Connection closed.");
				}
				stopTask();
			} catch (ProtocolException e) {
				logger.severe(e.getMessage());
				if (isRunning) {
					sessionManager.stopTasksAndRunNewSession(e.getMessage() + "\nConnection closed.");
				}
				stopTask();
			} catch (CommonException e) {
				logger.severe(e.getMessage());
				if (isRunning) {
					sessionManager.stopTasksAndRunNewSession("Connection closed.");
				}
				stopTask();
			} catch (Throwable te) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				te.printStackTrace(pw);
				if (isRunning) {
					sessionManager.stopTasksAndRunNewSession(te.getMessage() + "\n" + sw.toString());
				}
				stopTask();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.glavsoft.rfb.protocol.ReceiverTask#framebufferUpdateMessage()
	 */
	@Override
	public void framebufferUpdateMessage() throws CommonException {
		reader.readByte(); // padding
		int numberOfRectangles = reader.readUInt16();
		CLogger.getInst(CLogger.SYS_OUT).writeline("CVNCServerTester::framebufferUpdateMessage(): numberOfRectangles " + numberOfRectangles );
		while (numberOfRectangles-- > 0) {
			FramebufferUpdateRectangle rect = new FramebufferUpdateRectangle();
			rect.fill(reader);

			Decoder decoder = decoders.getDecoderByType(rect.getEncodingType());
			if (decoder != null) {
				reader.mark();
				decoder.decode(reader, renderer, rect);
				CLogger.getInst(CLogger.SYS_OUT).writeline("CVNCServerTester::framebufferUpdateMessage(): rect " + rect.toString()+" len "+reader.countFromMark());
				repaintController.repaintBitmap(rect);
			} else if (rect.getEncodingType() == EncodingType.RICH_CURSOR) {
				RichCursorDecoder.getInstance().decode(reader, renderer, rect);
				repaintController.repaintCursor();
			} else if (rect.getEncodingType() == EncodingType.CURSOR_POS) {
				renderer.decodeCursorPosition(rect);
				repaintController.repaintCursor();
			} else if (rect.getEncodingType() == EncodingType.DESKTOP_SIZE) {
				if (rect.width <= 0 || rect.height <= 0)
					throw new ProtocolException("Server sent wrong Desctop Size: one of new desctop size dimensions is less or equals to zero (" +
							rect.width + "x" + rect.height + ").");
				fullscreenFbUpdateIncrementalRequest =
					new FramebufferUpdateRequestMessage(0, 0, rect.width, rect.height, true);
				synchronized (renderer) {
					renderer = repaintController.createRenderer(reader, rect.width, rect.height,
							context.getPixelFormat());
				}
				context.sendMessage(new FramebufferUpdateRequestMessage(0, 0, rect.width, rect.height, false));
//				repaintController.repaintCursor();
			} else
				throw new CommonException("Unprocessed encoding: " + rect.toString());
		}
		synchronized (this) {
			if (needSendPixelFormat) {
				needSendPixelFormat = false;
				context.setPixelFormat(pixelFormat);
				context.sendMessage(new SetPixelFormatMessage(pixelFormat));
				logger.fine("sent: "+pixelFormat);
				context.sendRefreshMessage();
				logger.fine("sent: nonincremental fb update");
			} else {
				/**
				 * O. laudi
				 * after full bufferrefresh check, if compare is needed and check for differences
				 */
				m_lFramecount++;
				
				if (CInputRecorder.getInst().isFirstImgSaved()) {
					m_isDifferent = CInputRecorder.getInst().processImageCompare(renderer);
					if (!m_isDifferent)
						m_lEqualCount++;
				}
				else {
					CInputRecorder.getInst().processImageCompare(renderer);
				}
					
				
				CLogger.getInst(CLogger.SYS_OUT).writeline("m_isDifferent = " + m_isDifferent);
				if (m_isDifferent) {
					// Successful comparison --> Check for Differences
					if (CInputRecorder.getInst().getImageCmp() != null) {
						if (CInputRecorder.getInst().getImageCmp().getDeltaRegion() != null) {
							CInputRecorder.getInst().getImageCmp().saveAsPicFiles();
							CLogger.getInst(CLogger.SYS_OUT).writeline(CInputRecorder.getInst().getImageCmp().getDeltaRegion().toString());
							CInputRecorder.getInst().getImageCmp().getDeltaRegion().saveAsPicFile(System.currentTimeMillis());
						}
						
					}
							
//					CLogger.getInst(CLogger.SYS_OUT).writeline(CInputRecorder.getInst().getImageCmp().getDeltaRegion().toString());
				}
				
				context.sendMessage(fullscreenFbUpdateIncrementalRequest);
				CLogger.getInst(CLogger.SYS_OUT).writeline("CVNCServerTester::framebufferUpdateMessage(): sendMessage " + fullscreenFbUpdateIncrementalRequest.toString());
			}
		}
	}

}
