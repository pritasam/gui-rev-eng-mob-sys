/**
 * 
 */
package de.ostfalia.mockup.datamodel.storyboard;

import java.util.HashMap;

/**
 * @author O. Laudi
 *
 */
public class CSequence {
	
	// Member
	protected String						m_strID = "";
	protected String						m_strSTARTID = "";
	protected String						m_strTARGETID = "";
	
	protected HashMap<String, CStoryEvent>	m_mapASYNC = null;
	protected HashMap<String, CStoryEvent>	m_mapSYNC = null;
	
	/**
	 * @param m_strID
	 * @param m_strSTARTID
	 * @param m_strTARGETID
	 */
	public CSequence(String strID, String strSTARTID,
			String strTARGETID) {
		super();
		this.m_strID = strID;
		this.m_strSTARTID = strSTARTID;
		this.m_strTARGETID = strTARGETID;
		this.m_mapASYNC = new HashMap<String, CStoryEvent>();
		this.m_mapSYNC = new HashMap<String, CStoryEvent>();
	}
	
	public CSequence() {
		super();
		this.m_strID = "1";
		this.m_strSTARTID = "";
		this.m_strTARGETID = "";
		this.m_mapASYNC = new HashMap<String, CStoryEvent>();
		this.m_mapSYNC = new HashMap<String, CStoryEvent>();
	}
	
	/**
	 * @return the m_strID
	 */
	public String getID() {
		return m_strID;
	}

	/**
	 * @param m_strID the m_strID to set
	 */
	public void setID(String strID) {
		this.m_strID = strID;
	}

	/**
	 * @return the m_strSTARTID
	 */
	public String getSTARTID() {
		return m_strSTARTID;
	}

	/**
	 * @param m_strSTARTID the m_strSTARTID to set
	 */
	public void setSTARTID(String strSTARTID) {
		this.m_strSTARTID = strSTARTID;
	}

	/**
	 * @return the m_strTARGETID
	 */
	public String getTARGETID() {
		return m_strTARGETID;
	}

	/**
	 * @param m_strTARGETID the m_strTARGETID to set
	 */
	public void setTARGETID(String strTARGETID) {
		this.m_strTARGETID = strTARGETID;
	}

	/**
	 * adds an Async-event
	 * @param stEvent
	 */
	public void addAsyncEvent(CStoryEvent stEvent) {
		// get next String-ID
		int nID = this.m_mapASYNC.size() + 1;
		this.m_mapASYNC.put(String.valueOf(nID), stEvent);
	}
	
	/**
	 * adds an Sync-event
	 * @param stEvent
	 */
	public void addSyncEvent(CStoryEvent stEvent) {
		// get next String-ID
		int nID = this.m_mapSYNC.size() + 1;
		this.m_mapSYNC.put(String.valueOf(nID), stEvent);
	}
}
