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
	private byte					m_lastButtonMask;
	
	/**
	 * Constructor
	 */
	private CInputRecorder() {
		m_lastButtonMask = 0;
	}
	
	/**
	 * Get Instance from singleton
	 * @return
	 */
	public static CInputRecorder getInst() {
		if ( m_instance == null) {
			m_instance = new CInputRecorder();
		}
		return m_instance;
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
			CLogger.getInst(CLogger.FILE).writeline("InputRecorder::processMessage: " + message.toString());
		}
		
		if (message instanceof PointerEventMessage) {
			// Click
			if (m_lastButtonMask != ((PointerEventMessage) message).getButtonMask()) {
				CLogger.getInst(CLogger.FILE).writeline("InputRecorder::processMessage: " + message.toString());
				saveScreenshot(receiverTask.getRenderer(), lTimeStamp);
			}
			
			m_lastButtonMask = ((PointerEventMessage) message).getButtonMask();
		}
		else if (message instanceof KeyEventMessage) {
			saveScreenshot(receiverTask.getRenderer(), lTimeStamp);
		}
	}
	
	/**
	 * Saves a screenshot in the path %app%/Screenshots/ from a rendererobject.
	 * The Timpestamp is am vlaue in millioseconds to identify the Eventtime
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
}
