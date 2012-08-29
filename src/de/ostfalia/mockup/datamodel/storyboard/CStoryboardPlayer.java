/**
 * 
 */
package de.ostfalia.mockup.datamodel.storyboard;

import java.util.HashMap;

import javax.swing.JOptionPane;

import com.glavsoft.rfb.client.KeyEventMessage;
import com.glavsoft.rfb.client.PointerEventMessage;
import com.glavsoft.rfb.protocol.Protocol;

import de.ostfalia.viewer.logger.CLogger;

/**
 * @author O. Laudi
 *
 */
public class CStoryboardPlayer {
	// Constant
	private final int				MIN_EVENT_DELAY = 25;
	// Member
	private CStoryboard				m_storyboard;
	private Protocol				m_workingProtocol;
	private boolean					m_isStopped;
	private long					m_lngTimestamp;
	
	/**
	 * @param m_storyboard
	 */
	public CStoryboardPlayer(CStoryboard m_storyboard, Protocol workingProtocol) {
		super();
		this.m_storyboard 		= m_storyboard;
		this.m_workingProtocol	= workingProtocol;
		this.m_isStopped		= true;
	}

	/**
	 * @return the m_storyboard
	 */
	public CStoryboard getStoryboard() {
		return m_storyboard;
	}

	/**
	 * @param m_storyboard the m_storyboard to set
	 */
	public void setStoryboard(CStoryboard storyboard) {
		this.m_storyboard = storyboard;
	}

	/**
	 * starts playing the saved storyboard
	 */
	public void play() {
		m_isStopped = false;
		playStoryboard();
	}
	
	/**
	 * stops playing the current running storyboard
	 */
	public void stop() {
		m_isStopped = true;
		JOptionPane.showMessageDialog(null, "Playing Storyboard stopped!", "StoryboardPlayer", JOptionPane.WARNING_MESSAGE);
	}
	
	/**
	 * Plays the current saved storyboard
	 */
	private void playStoryboard() {
		CSequence sequence						= null;
		CSwipePoint swipePoint					= null;
		HashMap<String, CSequence> mapSequences = m_storyboard.getSequences();
		HashMap<String, CStoryEvent> mapAsync	= null;
		HashMap<String, CStoryEvent> mapSync	= null;
		int nSeqCount 							= mapSequences.size();
		int nASyncCount							= 0;
		int nSyncCount							= 0;
		int nSwipePointIndex1					= 0;
		int nSwipePointIndex2					= 0;
		long lngLastEvent						= 0;
		
		try {
			// Iterate all sequences
			m_lngTimestamp = System.currentTimeMillis();
			for (int nSeq = 1; nSeq <= nSeqCount; nSeq++) {
				if (!m_isStopped) {
					sequence 	= mapSequences.get(String.valueOf(nSeq));
					mapAsync 	= sequence.getMapASYNC();
					mapSync 	= sequence.getMapSYNC();
					nASyncCount	= mapAsync.size();
					nSyncCount	= mapSync.size();
					
					// Delay für Sequence
					CLogger.getInst(CLogger.SYS_OUT).writeline("Storyboardplayer::playStoryboard(): Sequence " + nSeq + " waiting delay " + sequence.getDELAY());
					while ((System.currentTimeMillis() - m_lngTimestamp) < Integer.valueOf(sequence.getDELAY())) {}
					m_lngTimestamp = System.currentTimeMillis();
					
					// start all Asyncs
					for (int nAsync = 1; nAsync <= nASyncCount; nAsync++) {
						// in asynchronen Threads starten
						//TODO ctsmsg
					}
					
					// start syncs with delay
					for (int nSync = 1; nSync <= nSyncCount; nSync++) {
						// nacheinander aufrufen
						if (mapSync.get(String.valueOf(nSync)) instanceof CKey) {
							CKey keyEvent = (CKey) mapSync.get(String.valueOf(nSync));
							
							// wait delay
							CLogger.getInst(CLogger.SYS_OUT).writeline("Storyboardplayer::playStoryboard(): keyEvent " + nSync + " waiting delay " + keyEvent.getDELAY());
							while ((System.currentTimeMillis() - m_lngTimestamp) < Integer.valueOf(keyEvent.getDura())) {}
							m_lngTimestamp = System.currentTimeMillis();
							
							// send message for duration
							m_workingProtocol.sendMessage(new KeyEventMessage(Integer.valueOf(keyEvent.getKeycode()), 
																			  Boolean.valueOf(keyEvent.getPressed())));
							
							// wait duration
							CLogger.getInst(CLogger.SYS_OUT).writeline("Storyboardplayer::playStoryboard(): keyEvent " + nSync + " waiting duration " + keyEvent.getDura());
							while ((System.currentTimeMillis() - m_lngTimestamp) < Integer.valueOf(keyEvent.getDura())) {}
							m_lngTimestamp = System.currentTimeMillis();
							
							// send button released
							m_workingProtocol.sendMessage(new KeyEventMessage(Integer.valueOf(keyEvent.getKeycode()), false));
						}
						else if (mapSync.get(String.valueOf(nSync)) instanceof CTouch) {
							CTouch touchEvent = (CTouch) mapSync.get(String.valueOf(nSync));

							// wait delay
							CLogger.getInst(CLogger.SYS_OUT).writeline("Storyboardplayer::playStoryboard(): touchEvent " + nSync + " waiting delay " + touchEvent.getDELAY());
							while ((System.currentTimeMillis() - m_lngTimestamp) < Integer.valueOf(touchEvent.getDura())) {}
							m_lngTimestamp = System.currentTimeMillis();
							
							// send message for duration
							m_workingProtocol.sendMessage(new PointerEventMessage(Byte.valueOf(touchEvent.getBtn()), 
																	  			  Short.valueOf(touchEvent.getX()),
																	  			  Short.valueOf(touchEvent.getY())));
							
							// wait duration
							CLogger.getInst(CLogger.SYS_OUT).writeline("Storyboardplayer::playStoryboard(): touchEvent " + nSync + " waiting delay " + touchEvent.getDura());
							while ((System.currentTimeMillis() - m_lngTimestamp) < Integer.valueOf(touchEvent.getDura())) {}
							m_lngTimestamp = System.currentTimeMillis();
							
							// send button released
							m_workingProtocol.sendMessage(new PointerEventMessage((byte) 0, 
																	  			  Short.valueOf(touchEvent.getX()),
																	  			  Short.valueOf(touchEvent.getY())));
						}
						else if (mapSync.get(String.valueOf(nSync)) instanceof CSwipe) {
							CSwipe swipeEvent 	= (CSwipe) mapSync.get(String.valueOf(nSync));
							int nPointCount		= swipeEvent.getMapPoints().size();
							
							// At least 2 CSqipePoints for start and end
							if (nPointCount >= 2) {
								// wait delay
								CLogger.getInst(CLogger.SYS_OUT).writeline("Storyboardplayer::playStoryboard(): swipeEvent " + nSync + " waiting delay " + swipeEvent.getDELAY());
								while ((System.currentTimeMillis() - m_lngTimestamp) < Integer.valueOf(swipeEvent.getDura())) {}
								m_lngTimestamp = System.currentTimeMillis();
								
								// get first SwipePoint
								swipePoint = swipeEvent.getMapPoints().get("1");
								
								// send message for duration
								m_workingProtocol.sendMessage(new PointerEventMessage(Byte.valueOf(swipeEvent.getBtn()), 
																		  			  Short.valueOf(swipePoint.getX()),
																		  			  Short.valueOf(swipePoint.getY())));
								
								// send interpolation during duration
								float fAlpha 	= 0.0f;
								float fx 		= 0.0f;
								float fy 		= 0.0f;
								int	nCount		= 0;
								lngLastEvent = m_lngTimestamp;
								while ((System.currentTimeMillis() - m_lngTimestamp) < Integer.valueOf(swipeEvent.getDura())) {
									nCount++;
									
									if ((nCount < 2) || (System.currentTimeMillis() - lngLastEvent) > MIN_EVENT_DELAY) {
										// send every <MIN_EVENT_DELAY> (25ms) an interpolated message (50 and 100 are too much!!!)
										
										// get swipepoint (=(Delta/Dura) * (Count-1))
										if (Integer.valueOf(swipeEvent.getDura()) > 0) {
											nSwipePointIndex1 = Integer.valueOf(((int)(System.currentTimeMillis() - m_lngTimestamp) / Integer.valueOf(swipeEvent.getDura())) *
																(nPointCount - 1));
											nSwipePointIndex2 = nSwipePointIndex1 + 1;
											
											if ((nSwipePointIndex1 < nPointCount) &&
												(nSwipePointIndex2 < nPointCount)) {
												nSwipePointIndex1++;
												nSwipePointIndex2++;
											}
										}
										else {
											// div/0
											nSwipePointIndex1 = 1;
											nSwipePointIndex2 = 2;
										}
										
										fAlpha = (float)(System.currentTimeMillis() - m_lngTimestamp) / Float.valueOf(swipeEvent.getDura());
										swipePoint = swipeEvent.getMapPoints().get("1");
										fx = ((1.0f - fAlpha) * (float)Short.valueOf(swipeEvent.getMapPoints().get(String.valueOf(nSwipePointIndex1)).getX()) + 
												fAlpha * (float)Short.valueOf(swipeEvent.getMapPoints().get(String.valueOf(nSwipePointIndex2)).getX()));
										fy = ((1.0f - fAlpha) * (float)Short.valueOf(swipeEvent.getMapPoints().get(String.valueOf(nSwipePointIndex1)).getY()) + 
												fAlpha * (float)Short.valueOf(swipeEvent.getMapPoints().get(String.valueOf(nSwipePointIndex2)).getY()));
										
										CLogger.getInst(CLogger.SYS_OUT).writeline("Storyboardplayer::playStoryboard(): swipeEvent " + nSync + " interpolation x:" + fx + " y:" + fy);
										
										m_workingProtocol.sendMessage(new PointerEventMessage(Byte.valueOf(swipeEvent.getBtn()), 
																				  			  (short)fx,
																				  			  (short)fy));
										lngLastEvent = System.currentTimeMillis();
									}
								}
								
								// get last SwipePoint
								swipePoint = swipeEvent.getMapPoints().get(String.valueOf(nPointCount));
								
								m_workingProtocol.sendMessage(new PointerEventMessage(Byte.valueOf(swipeEvent.getBtn()), 
																					  Short.valueOf(swipePoint.getX()),
																					  Short.valueOf(swipePoint.getY())));
								m_lngTimestamp = System.currentTimeMillis();
								
								// send button released
								m_workingProtocol.sendMessage(new PointerEventMessage((byte) 0, 
																					  Short.valueOf(swipePoint.getX()),
																					  Short.valueOf(swipePoint.getY())));
							}
							else {
								// if not enough SwipePoints --> stop playing storyboard
								CLogger.getInst(CLogger.SYS_OUT).writeline("Storyboardplayer::playStoryboard(): swipeEvent.nPointCount = " + nPointCount + "; not enough SwipePoints");
								this.stop();
							}
						}
					}
				}
			}
			if (!m_isStopped)
				JOptionPane.showMessageDialog(null, "Playing Storyboard \"" + m_storyboard.getID() + "\" finished!", "StoryboardPlayer", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
