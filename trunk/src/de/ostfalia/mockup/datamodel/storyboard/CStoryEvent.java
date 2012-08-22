/**
 * 
 */
package de.ostfalia.mockup.datamodel.storyboard;

/**
 * @author O. Laudi
 *
 */
public abstract class CStoryEvent extends CXMLStoryboardEmt{
	protected String	m_strID 	= "";
	protected String	m_strDELAY 	= "";
	/**
	 * @param m_strNR
	 */
	public CStoryEvent(String strDELAY) {
		super();
		this.m_strID = "";
		this.m_strDELAY = strDELAY;
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

}
