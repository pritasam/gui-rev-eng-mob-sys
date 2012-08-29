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

import de.ostfalia.mockup.datamodel.storyboard.CKey;
import de.ostfalia.mockup.datamodel.storyboard.CSequence;
import de.ostfalia.mockup.datamodel.storyboard.CStoryboard;
import de.ostfalia.mockup.datamodel.storyboard.CSwipe;
import de.ostfalia.mockup.datamodel.storyboard.CSwipePoint;
import de.ostfalia.mockup.datamodel.storyboard.CTouch;
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
	private boolean					m_isRecord;
	private boolean					m_isStoryPlaying;
	private CStoryboard				m_storyboard;
	private CSequence				m_sequence;
	private long					m_lTimeStamp;
	private long					m_lngLastSeqBegin;
	private long					m_lngLastEventEnd;
	private byte 					m_buttonMask;
	private short 					m_x;
	private short 					m_y;
	
	/**
	 * Constructor
	 */
	private CInputRecorder() {
		m_lastButtonMask 		= 0;
		m_imageCmp 				= null;
		m_isFirstImgSaved 		= false;
		m_isEventMessage		= false;
		m_isRecord				= false;
		m_isStoryPlaying		= false;
		initStoryboard();
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
	 * @return the m_isRecord
	 */
	public boolean isRecord() {
		return m_isRecord;
	}

	/**
	 * @param toggle the value of isRecord 
	 */
	public void setToggleRecord() {
		this.m_isRecord = !this.m_isRecord;
	}
	
	/**
	 * @return the m_isStroybPlaying
	 */
	public boolean isStoryPlaying() {
		return m_isStoryPlaying;
	}

	/**
	 * @param m_isStroybPlaying the m_isStroybPlaying to set
	 */
	public void setToggleStoryPlaying() {
		this.m_isStoryPlaying = !this.m_isStoryPlaying;
	}

	/**
	 * Recordbegin
	 * Creates a new Instance of CStoryboard
	 */
	public void initStoryboard() {
		m_sequence			= null;
		m_storyboard 		= new CStoryboard();
		m_lTimeStamp		= System.currentTimeMillis();
		m_lngLastSeqBegin	= 0;
		m_lngLastEventEnd	= 0;
	}
	
	/**
	 * 
	 * @return m_storyboard
	 */
	public CStoryboard getStoryboard() {
		return this.m_storyboard;
	}

	/**
	 * Handels the extended behavior to the SendMessage-method in the Protocol-class
	 * @param message
	 * @param receiverTask
	 */
	public void processMessage(ClientToServerMessage message, ReceiverTask receiverTask) {
		if (this.m_isRecord) {
						
			if (message instanceof PointerEventMessage) {
				// Click
				//CLogger.getInst(CLogger.SYS_OUT).writeline("CInputRecorder::processMessage(): PointerEventMessage: " + ((PointerEventMessage)message).toString());
				
				if ((1 == ((PointerEventMessage) message).getButtonMask()) && 
						(m_lastButtonMask != ((PointerEventMessage) message).getButtonMask())) {
					
					m_lngLastSeqBegin 	= m_lTimeStamp;
					m_lTimeStamp		= System.currentTimeMillis();
					m_lngLastEventEnd 	= 0;
					
					saveScreenshot(receiverTask.getRenderer(), m_lTimeStamp);
					// add last sequence to storyboard if exits and set targetID
					if (m_sequence != null) {
						m_sequence.setTARGETID(String.valueOf(m_lTimeStamp));
						m_storyboard.addSequence(m_sequence);
					}
						
					// create new sequence
					this.m_sequence = new CSequence(String.valueOf(m_lTimeStamp), "", String.valueOf(m_lTimeStamp - m_lngLastSeqBegin));
					
					// save messagedata for decision of Eventtype
					m_buttonMask = ((PointerEventMessage)message).getButtonMask();
					m_x = ((PointerEventMessage)message).getX();
					m_y = ((PointerEventMessage)message).getY();
					
//					CLogger.getInst(CLogger.SYS_OUT).writeline("InputRecorder::processMessage: " + message.toString());
//					prepareImageCompare(receiverTask.getRenderer(), lTimeStamp);
////					m_imageCmp.saveMasterAsPicFile();
//					this.m_isEventMessage = true;
////					saveScreenshot(receiverTask.getRenderer(), lTimeStamp);
				}
				else if ((0 == ((PointerEventMessage) message).getButtonMask()) && 
						(m_lastButtonMask != ((PointerEventMessage) message).getButtonMask())){
					

					// unterscheiden sich Koordinaten, dann swipe, ansonsten touch
					if ((m_x != ((PointerEventMessage)message).getX()) || 
						(m_y != ((PointerEventMessage)message).getY())) {
						// swipe
						CSwipe swp = new CSwipe(String.valueOf(m_buttonMask), 
												String.valueOf(System.currentTimeMillis() - m_lTimeStamp),
												String.valueOf(m_lngLastEventEnd));
						swp.addtMapPoint(new CSwipePoint(String.valueOf(m_x), String.valueOf(m_y)));
						swp.addtMapPoint(new CSwipePoint(String.valueOf(((PointerEventMessage)message).getX()), 
														 String.valueOf(((PointerEventMessage)message).getY())));
						
						m_sequence.addSyncEvent(swp);
					}
					else {
						// touch
						m_sequence.addSyncEvent(new CTouch(String.valueOf(m_buttonMask), 
								String.valueOf(m_x), 
								String.valueOf(m_y),
								String.valueOf(System.currentTimeMillis() - m_lTimeStamp),
								String.valueOf(m_lngLastEventEnd)));
					}
					
					m_lngLastEventEnd = System.currentTimeMillis();
				}
				
				m_lastButtonMask = ((PointerEventMessage) message).getButtonMask();
			}
			else if (message instanceof KeyEventMessage) {
				if (((KeyEventMessage)message).isDownFlag()) {
					m_lngLastSeqBegin 	= m_lTimeStamp;
					m_lTimeStamp		= System.currentTimeMillis();
					m_lngLastEventEnd 	= 0;
					
					saveScreenshot(receiverTask.getRenderer(), m_lTimeStamp);
					// add last sequence to storyboard if exits and set targetID
					if (m_sequence != null) {
						m_sequence.setTARGETID(String.valueOf(m_lTimeStamp));
						m_storyboard.addSequence(m_sequence);
					}
						
					// create new sequence
					this.m_sequence = new CSequence(String.valueOf(m_lTimeStamp), "", String.valueOf(m_lTimeStamp - m_lngLastSeqBegin));
					
//					// pressed
//					CLogger.getInst(CLogger.SYS_OUT).writeline("InputRecorder::processMessage: " + message.toString());
//					
//					m_lngKeyEventTimer = System.currentTimeMillis();
//					prepareImageCompare(receiverTask.getRenderer(), lTimeStamp);
////					m_imageCmp.saveMasterAsPicFile();
//					this.m_isEventMessage = true;
//					saveScreenshot(receiverTask.getRenderer(), m_lTimeStamp);
				}
				else {
					// released
					// Add KeyEvent to Storyboard
					m_sequence.addSyncEvent(new CKey("true", 
													 String.valueOf(((KeyEventMessage)message).getKey()), 
													 String.valueOf(System.currentTimeMillis() - m_lTimeStamp), 
													 String.valueOf(m_lngLastEventEnd)));
					
					m_lngLastEventEnd = System.currentTimeMillis();
				}
			}
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
	
	/**
	 * Sets the name
	 * saves the last screenshot
	 * adds the last screenshot as target to the last sequence
	 * add last sequence to the storyboard
	 * generate storyboard
	 * @param strID
	 * @return
	 */
	public boolean finish(ReceiverTask receiverTask, String strID) {
		boolean isSuccess = true;
		Long lTimeStamp = System.currentTimeMillis();
		
		saveScreenshot(receiverTask.getRenderer(), lTimeStamp);
		if (m_sequence != null) {
			m_sequence.setTARGETID(String.valueOf(lTimeStamp));
			if (m_storyboard != null) {
				m_storyboard.addSequence(m_sequence);
			}
		}
		
		this.m_storyboard.finish(strID);
		
		return isSuccess;
	}
}
