/**
 * 
 */
package de.ostfalia.viewer.inputrecorder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.glavsoft.drawing.Renderer;
import com.glavsoft.rfb.client.ClientToServerMessage;
import com.glavsoft.rfb.client.KeyEventMessage;
import com.glavsoft.rfb.client.PointerEventMessage;
import com.glavsoft.rfb.protocol.ReceiverTask;

import de.ostfalia.viewer.logger.CLogger;

/**
 * @author O. Laudi
 *
 */
public class CInputRecorder{
	
	private static CInputRecorder	m_instance;
	private CImageComparer			m_imageCmp;
	private byte					m_lastButtonMask;
	private boolean					m_isFirstImgSaved;
	private boolean					m_isEventMessage;
	
	/**
	 * Constructor
	 */
	private CInputRecorder() {
		m_lastButtonMask 	= 0;
		m_imageCmp 			= null;
		m_isFirstImgSaved 	= false;
		m_isEventMessage	= false;
	}
	
	/**
	 * Get Instance from singleton
	 * @return
	 */
	public static CInputRecorder getInst() {
		if ( m_instance == null) {
			m_instance 			= new CInputRecorder();
		}
		return m_instance;
	}
	
	/**
	 * @return the m_imageCmp
	 */
	public CImageComparer getImageCmp() {
		return m_imageCmp;
	}

	/**
	 * @return the m_isFirstImgSaved
	 */
	public boolean isFirstImgSaved() {
		return m_isFirstImgSaved;
	}

	/**
	 * @return the m_isEventMessage
	 */
	public boolean isEventMessage() {
		return m_isEventMessage;
	}

	/**
	 * sets isEventMessage to false
	 */
	public void resetEventMessage() {
		this.m_isEventMessage = false;
	}

	/**
	 * Handels the extended behavior to the SendMessage-method in the Protocol-class
	 * @param message
	 * @param receiverTask
	 */
	public void processMessage(ClientToServerMessage message, ReceiverTask receiverTask) {
		Long lTimeStamp = System.currentTimeMillis();
		
		if (message instanceof PointerEventMessage) {}
		else {
			CLogger.getInst(CLogger.SYS_OUT).writeline("InputRecorder::processMessage: " + message.toString());
		}
		
		if (message instanceof PointerEventMessage) {
			// Click
			if ((1 == ((PointerEventMessage) message).getButtonMask()) && (m_lastButtonMask != ((PointerEventMessage) message).getButtonMask())) {
				CLogger.getInst(CLogger.SYS_OUT).writeline("InputRecorder::processMessage: " + message.toString());
				prepareImageCompare(receiverTask.getRenderer(), lTimeStamp);
//				m_imageCmp.saveMasterAsPicFile();
				this.m_isEventMessage = true;
//				saveScreenshot(receiverTask.getRenderer(), lTimeStamp);
			}
			
			m_lastButtonMask = ((PointerEventMessage) message).getButtonMask();
		}
		else if (message instanceof KeyEventMessage) {
			prepareImageCompare(receiverTask.getRenderer(), lTimeStamp);
//			m_imageCmp.saveMasterAsPicFile();
			this.m_isEventMessage = true;
//			saveScreenshot(receiverTask.getRenderer(), lTimeStamp);
		}
	}
	
	/**
	 * Prepares the CImageCompare-class
	 * @param renderer
	 */
	private void prepareImageCompare(Renderer renderer, long lTimestamp) {
		BufferedImage image = new BufferedImage(renderer.getWidth(), renderer.getHeight(), BufferedImage.TYPE_INT_RGB);
		image.setRGB(0, 0, renderer.getWidth(), renderer.getHeight(), renderer.getPixels(), 0, renderer.getWidth());
		// Save image to CImageComparer
		m_imageCmp = new CImageComparer(image, lTimestamp);
		m_isFirstImgSaved = true;
	}
	
	/**
	 * Saves a screenshot in the path %app%/Screenshots/ from a rendererobject.
	 * The Timestamp is am vlaue in milliseconds to identify the Eventtime
	 * @param renderer
	 * @param lTimestamp
	 */
	public void saveScreenshot(Renderer renderer, long lTimestamp) {
		BufferedImage image = new BufferedImage(renderer.getWidth(), renderer.getHeight(), BufferedImage.TYPE_INT_RGB);
		image.setRGB(0, 0, renderer.getWidth(), renderer.getHeight(), renderer.getPixels(), 0, renderer.getWidth());
		
		CLogger.getInst(CLogger.FILE).writeline("InputRecorder::saveScreenshot: scr_" + lTimestamp + ".png");
		
		try {
		    // retrieve image
		    File outputfile = new File("Screenshots" + File.separator + "scr_" + lTimestamp + ".png");
		    ImageIO.write(image, "png", outputfile);
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
	}
	
	/**
	 * Processes an imagecompare between a set masterimage an the 
	 * current given image in the renderer
	 * @param renderer
	 * @return isDifferent
	 */
	public boolean processImageCompare(Renderer renderer) {
		boolean isDifferent = false;
		if (m_isFirstImgSaved) {
			BufferedImage image = new BufferedImage(renderer.getWidth(), renderer.getHeight(), BufferedImage.TYPE_INT_RGB);
			image.setRGB(0, 0, renderer.getWidth(), renderer.getHeight(), renderer.getPixels(), 0, renderer.getWidth());
			
			isDifferent = m_imageCmp.compareMasterWith(image);
		}
		else
		{
			// Set Masterimage
			prepareImageCompare(renderer, System.currentTimeMillis());
		}
		return isDifferent;
	}
}
