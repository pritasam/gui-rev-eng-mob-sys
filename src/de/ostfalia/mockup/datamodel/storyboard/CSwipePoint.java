/**
 * 
 */
package de.ostfalia.mockup.datamodel.storyboard;

/**
 * @author O. Laudi
 *
 */
public class CSwipePoint extends CXMLStoryboardEmt {

	// Member
	protected String			m_strID = "";
	protected String			m_strX = "";
	protected String			m_strY = "";
	
	public CSwipePoint(String strX, String strY) {
		super();
		this.m_strX	= strX;
		this.m_strY	= strY;
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
	 * @return the m_strX
	 */
	public String getX() {
		return m_strX;
	}

	/**
	 * @param m_strX the m_strX to set
	 */
	public void setX(String strX) {
		this.m_strX = strX;
	}

	/**
	 * @return the m_strY
	 */
	public String getY() {
		return m_strY;
	}

	/**
	 * @param m_strY the m_strY to set
	 */
	public void setY(String strY) {
		this.m_strY = strY;
	}

	/* (non-Javadoc)
	 * @see de.ostfalia.mockup.datamodel.storyboard.CXMLStoryboardEmt#getXMLString(int)
	 */
	@Override
	public String getXMLString(int nIterationDepth) {
		String strSpace 	= "";
		String strResult 	= "";
		
		for (int i = 0; i < nIterationDepth; i++)
			strSpace += m_XMLSPACE;
		
		strResult += strSpace + "<SwipePoint id=\"" + this.m_strID + "\" " +
									  "x=\"" + this.m_strX + "\" " + 
									  "y=\"" + this.m_strY + "\"></SwipePoint>\n";
		
		return strResult;
	}

}
