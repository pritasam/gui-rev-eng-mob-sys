/**
 * 
 */
package de.ostfalia.mockup.datamodel.storyboard;

import java.util.HashMap;

/**
 * @author O. Laudi
 *
 */
public class CSequence extends CXMLStoryboardEmt{
	
	// Member
	protected String						m_strID = "";
	protected String						m_strSTARTID = "";
	protected String						m_strTARGETID = "";
	protected String						m_strDELAY = "";
	
	protected HashMap<String, CStoryEvent>	m_mapASYNC = null;
	protected HashMap<String, CStoryEvent>	m_mapSYNC = null;
	
	/**
	 * @param m_strSTARTID
	 * @param m_strTARGETID
	 * @param m_strDELAY
	 */
	public CSequence(String strSTARTID,
			String strTARGETID, String strDELAY) {
		super();
		this.m_strID 		= "1";
		this.m_strSTARTID 	= strSTARTID;
		this.m_strTARGETID 	= strTARGETID;
		this.m_strDELAY 	= strDELAY;
		this.m_mapASYNC 	= new HashMap<String, CStoryEvent>();
		this.m_mapSYNC 		= new HashMap<String, CStoryEvent>();
	}
	
	public CSequence() {
		super();
		this.m_strID 		= "1";
		this.m_strSTARTID 	= "";
		this.m_strTARGETID 	= "";
		this.m_strDELAY 	= "0";
		this.m_mapASYNC 	= new HashMap<String, CStoryEvent>();
		this.m_mapSYNC 		= new HashMap<String, CStoryEvent>();
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
	 * @return the m_strDELAY
	 */
	public String getDELAY() {
		return m_strDELAY;
	}

	/**
	 * @param m_strDELAY the m_strDELAY to set
	 */
	public void setDELAY(String strDELAY) {
		this.m_strDELAY = strDELAY;
	}

	/**
	 * adds an Async-event
	 * @param stEvent
	 */
	public void addAsyncEvent(CStoryEvent stEvent) {
		// get next String-ID
		int nID = this.m_mapASYNC.size() + 1;
		stEvent.setID(String.valueOf(nID));
		this.m_mapASYNC.put(String.valueOf(nID), stEvent);
	}
	
	/**
	 * adds an Sync-event
	 * @param stEvent
	 */
	public void addSyncEvent(CStoryEvent stEvent) {
		// get next String-ID
		int nID = this.m_mapSYNC.size() + 1;
		stEvent.setID(String.valueOf(nID));
		this.m_mapSYNC.put(String.valueOf(nID), stEvent);
	}

	@Override
	public String getXMLString(int nIterationDepth) {
		String strSpace 	= "";
		String strResult 	= "";
		
		for (int i = 0; i < nIterationDepth; i++)
			strSpace += m_XMLSPACE;
		
		strResult += strSpace + "<Sequence id=\"" + this.m_strID + "\" " +
										   "startID=\"" + this.m_strSTARTID + "\" " +
										   "targetID=\"" + this.m_strTARGETID + "\" " +
										   "delay=\"" + this.m_strDELAY + "\">\n";
		
		nIterationDepth++;
		strSpace	= "";
		for (int i = 0; i < nIterationDepth; i++)
			strSpace += m_XMLSPACE;
		
		nIterationDepth++;
		// add all async events
		strResult += strSpace + "<Async>\n";
		for (int nSyncNr = 0; nSyncNr < m_mapASYNC.size(); nSyncNr++) {
			strResult += m_mapASYNC.get(String.valueOf(nSyncNr + 1)).getXMLString(nIterationDepth);
		}
		strResult += strSpace + "</Async>\n";
		
		// add all sync events
		strResult += strSpace + "<Sync>\n";
		for (int nSyncNr = 0; nSyncNr < m_mapSYNC.size(); nSyncNr++) {
			strResult += m_mapSYNC.get(String.valueOf(nSyncNr + 1)).getXMLString(nIterationDepth);
		}
		strResult += strSpace + "</Sync>\n";
		
		strResult += strSpace + "</Sequence>\n";
		
		return strResult;
	}
}
