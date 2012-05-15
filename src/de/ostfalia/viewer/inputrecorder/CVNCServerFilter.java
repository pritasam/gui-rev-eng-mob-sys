/**
 * 
 */
package de.ostfalia.viewer.inputrecorder;

import com.glavsoft.exceptions.CommonException;
import com.glavsoft.exceptions.ProtocolException;
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
 * @author O.Laudi
 * This class filters the incoming images from the
 * Readerclass and manipulates the DataInputStream
 *
 */
public class CVNCServerFilter extends ReceiverTask {

	/* (non-Javadoc)
	 * @see com.glavsoft.rfb.protocol.ReceiverTask#framebufferUpdateMessage()
	 */
	@Override
	public void framebufferUpdateMessage() throws CommonException {
		reader.readByte(); // padding
		int numberOfRectangles = reader.readUInt16();
		CLogger.getInst(CLogger.SYS_OUT).writeline("CVNCServerFilter::framebufferUpdateMessage(): numberOfRectangles " + numberOfRectangles );
		while (numberOfRectangles-- > 0) {
			FramebufferUpdateRectangle rect = new FramebufferUpdateRectangle();
			rect.fill(reader);

			Decoder decoder = decoders.getDecoderByType(rect.getEncodingType());
			
			/**
			 * O. Laudi
			 */
			
//			logger.info(rect.toString());
			if (decoder != null) {
				reader.mark();
				decoder.decode(reader, renderer, rect);
				CLogger.getInst(CLogger.SYS_OUT).writeline("CVNCServerFilter::framebufferUpdateMessage(): rect " + rect.toString()+" len "+reader.countFromMark());
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
				if (CInputRecorder.getInst().processImageCompare(renderer)) {
					// Successful comparison --> Check for Differences
					if (CInputRecorder.getInst().getImageCmp() != null) {
						if (CInputRecorder.getInst().getImageCmp().getDeltaRegion() != null) {
							CInputRecorder.getInst().getImageCmp().saveAsPicFiles();
							CLogger.getInst(CLogger.SYS_OUT).writeline(CInputRecorder.getInst().getImageCmp().getDeltaRegion().toString());
							CInputRecorder.getInst().getImageCmp().getDeltaRegion().saveAsPicFile(System.currentTimeMillis());
						}
					}
							
//							CLogger.getInst(CLogger.SYS_OUT).writeline(CInputRecorder.getInst().getImageCmp().getDeltaRegion().toString());
				}
				context.sendMessage(fullscreenFbUpdateIncrementalRequest);
				CLogger.getInst(CLogger.SYS_OUT).writeline("CVNCServerFilter::framebufferUpdateMessage(): sendMessage " + fullscreenFbUpdateIncrementalRequest.toString());
			}
		}
	}

	/**
	 * Copyconstructor
	 * @param m_receiverTask
	 */
	public CVNCServerFilter (ReceiverTask m_receiverTask) {
		super(m_receiverTask);
	}
	
	public CVNCServerFilter(Reader reader,
			IRepaintController repaintController,
			ClipboardController clipboardController,
			ISessionController sessionManager, DecodersContainer decoders,
			ProtocolContext context) {
		super(reader, repaintController, clipboardController, sessionManager, decoders,
				context);
		// TODO Auto-generated constructor stub
	}

}
