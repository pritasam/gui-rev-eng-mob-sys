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
		HashMap<String, CSequence> mapSequences = m_storyboard.getSequences();
		HashMap<String, CStoryEvent> mapAsync	= null;
		HashMap<String, CStoryEvent> mapSync	= null;
		int nSeqCount 							= mapSequences.size();
		int nASyncCount							= 0;
		int nSyncCount							= 0;
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
							CSwipe switchEvent = (CSwipe) mapSync.get(String.valueOf(nSync));
							
							// wait delay
							CLogger.getInst(CLogger.SYS_OUT).writeline("Storyboardplayer::playStoryboard(): swipeEvent " + nSync + " waiting delay " + switchEvent.getDELAY());
							while ((System.currentTimeMillis() - m_lngTimestamp) < Integer.valueOf(switchEvent.getDura())) {}
							m_lngTimestamp = System.currentTimeMillis();
							
							// send message for duration
							m_workingProtocol.sendMessage(new PointerEventMessage(Byte.valueOf(switchEvent.getBtn()), 
																	  			  Short.valueOf(switchEvent.getX1()),
																	  			  Short.valueOf(switchEvent.getY1())));
							
							// send interpolation during duration
							float fAlpha 	= 0.0f;
							float fx 		= 0.0f;
							float fy 		= 0.0f;
							lngLastEvent = m_lngTimestamp;
							while ((System.currentTimeMillis() - m_lngTimestamp) < Integer.valueOf(switchEvent.getDura())) {
								// send every 100ms an interpolated message
								if ((System.currentTimeMillis() - lngLastEvent) > 100) {
									fAlpha = (float)(System.currentTimeMillis() - m_lngTimestamp) / Float.valueOf(switchEvent.getDura());
									fx = ((1.0f - fAlpha) * (float)Short.valueOf(switchEvent.getX1()) + 
											fAlpha * (float)Short.valueOf(switchEvent.getX2()));
									fy = ((1.0f - fAlpha) * (float)Short.valueOf(switchEvent.getY1()) + 
											fAlpha * (float)Short.valueOf(switchEvent.getY2()));
									
									CLogger.getInst(CLogger.SYS_OUT).writeline("Storyboardplayer::playStoryboard(): swipeEvent " + nSync + " interpolation x:" + fx + " y:" + fy);
									
									m_workingProtocol.sendMessage(new PointerEventMessage(Byte.valueOf(switchEvent.getBtn()), 
																			  			  (short)fx,
																			  			  (short)fy));
									lngLastEvent = System.currentTimeMillis();
								}
							}
							m_workingProtocol.sendMessage(new PointerEventMessage(Byte.valueOf(switchEvent.getBtn()), 
																	  			  Short.valueOf(switchEvent.getX2()),
																	  			  Short.valueOf(switchEvent.getY2())));
							m_lngTimestamp = System.currentTimeMillis();
							
							// send button released
							m_workingProtocol.sendMessage(new PointerEventMessage((byte) 0, 
																	  			  Short.valueOf(switchEvent.getX2()),
																	  			  Short.valueOf(switchEvent.getY2())));
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
