/**
 * 
 */
package de.ostfalia.mockup.datamodel.storyboard;

import java.util.HashMap;

/**
 * @author O. Laudi
 * 1. Create Sequence and Storyboard with
 * 		m_sequence;
 * 		m_storyboard = new CStoryboard();
 * 2. Add Sequence
 * 		m_storyboard.addSequence(seq);
 * 3. If Name is known, refresh ID
 * 		m_storyboard.finish(strName);
 *
 */
public class CStoryboard {
	
	protected String						m_strID = "";

	protected HashMap<String, CSequence>	m_mapSequences = null;
	
	/**
	 * @param m_strID
	 */
	public CStoryboard() {
		super();
		this.m_strID = "1";
		this.m_mapSequences = new HashMap<String, CSequence>();
	}
	
	/**
	 * 
	 * @param seq
	 */
	public void addSequence(CSequence seq) {
		// get next String-ID
		if (m_mapSequences != null) {
			int nID = m_mapSequences.size() + 1;
			this.m_mapSequences.put(String.valueOf(nID), seq);
		}
	}
	
	/**
	 * @return the m_mapSequences
	 */
	public HashMap<String, CSequence> getSequences() {
		return m_mapSequences;
	}
	
	/**
	 * 
	 * @return
	 */
	public CSequence getCurrentSequence() {
		if (m_mapSequences != null)
			return m_mapSequences.get(String.valueOf(m_mapSequences.size()));
		else
			return null;
	}
	
	/**
	 * @return the m_strID
	 */
	public String getID() {
		return m_strID;
	}
	
	/**
	 * sets the Storyboardname
	 * @param strID
	 */
	public void finish(String strID) {
		this.m_strID = strID;
		
	}
}
