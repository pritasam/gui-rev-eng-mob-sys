/**
 * 
 */
package de.ostfalia.mockup.datamodel.storyboard;

/**
 * @author O. Laudi
 *
 */
public class CTouch extends CStoryEvent{

	// Member
	protected String	m_strbtn = "";
	protected String	m_strX = "";
	protected String	m_strY = "";
	protected String	m_strDura = "";

	/**
	 * @param m_strbtn
	 * @param m_strX
	 * @param m_strY
	 * @param m_strDura
	 * @param m_strDELAY
	 */
	public CTouch(String strbtn, String strX, String strY,
			String strDura, String strDELAY) {
		super(strDELAY);
		this.m_strbtn = strbtn;
		this.m_strX = strX;
		this.m_strY = strY;
		this.m_strDura = strDura;
	}

	/**
	 * @return the m_strbtn
	 */
	public String getBtn() {
		return m_strbtn;
	}


	/**
	 * @param m_strbtn the m_strbtn to set
	 */
	public void setBtn(String strbtn) {
		this.m_strbtn = strbtn;
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


	/**
	 * @return the m_strDura
	 */
	public String getDura() {
		return m_strDura;
	}


	/**
	 * @param m_strDura the m_strDura to set
	 */
	public void setDura(String strDura) {
		this.m_strDura = strDura;
	}


	@Override
	public String getXMLString(int nIterationDepth) {
		String strSpace 	= "";
		String strResult 	= "";
		
		for (int i = 0; i < nIterationDepth; i++)
			strSpace += m_XMLSPACE;
		
		strResult += strSpace + "<Touch id=\"" + this.m_strID + "\" " +
									  "btn=\"" + this.m_strbtn + "\" " + 
									  "x=\"" + this.m_strX + "\" " + 
									  "y=\"" + this.m_strY + "\" " + 
									  "dura=\"" + this.m_strDura + "\" " +
									  "delay=\"" + this.m_strDELAY + "\"></Touch>\n";
		
		return strResult;
	}
}
