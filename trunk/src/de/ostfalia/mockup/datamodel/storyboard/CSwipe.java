/**
 * 
 */
package de.ostfalia.mockup.datamodel.storyboard;

/**
 * @author O. Laudi
 *
 */
public class CSwipe extends CStoryEvent{
	
	// Member
	protected String	m_strbtn = "";
	protected String	m_strX1 = "";
	protected String	m_strY1 = "";
	protected String	m_strX2 = "";
	protected String	m_strY2 = "";
	protected String	m_strDura = "";
		
	/**
	 * @param m_strbtn
	 * @param m_strX1
	 * @param m_strY1
	 * @param m_strX2
	 * @param m_strY2
	 * @param m_strDura
	 * @param m_strDELAY
	 */
	public CSwipe(String strbtn, String strX1,
			String strY1, String strX2, String strY2, String strDura, String strDELAY) {
		super(strDELAY);
		this.m_strbtn = strbtn;
		this.m_strX1 = strX1;
		this.m_strY1 = strY1;
		this.m_strX2 = strX2;
		this.m_strY2 = strY2;
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
	 * @return the m_strX1
	 */
	public String getX1() {
		return m_strX1;
	}


	/**
	 * @param m_strX1 the m_strX1 to set
	 */
	public void setX1(String strX1) {
		this.m_strX1 = strX1;
	}


	/**
	 * @return the m_strY1
	 */
	public String getY1() {
		return m_strY1;
	}


	/**
	 * @param m_strY1 the m_strY1 to set
	 */
	public void setY1(String strY1) {
		this.m_strY1 = strY1;
	}


	/**
	 * @return the m_strX2
	 */
	public String getX2() {
		return m_strX2;
	}


	/**
	 * @param m_strX2 the m_strX2 to set
	 */
	public void setX2(String strX2) {
		this.m_strX2 = strX2;
	}


	/**
	 * @return the m_strY2
	 */
	public String getY2() {
		return m_strY2;
	}


	/**
	 * @param m_strY2 the m_strY2 to set
	 */
	public void setY2(String strY2) {
		this.m_strY2 = strY2;
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
		
		strResult += strSpace + "<Swipe id=\"" + this.m_strID + "\" " +
									  "btn=\"" + this.m_strbtn + "\" " + 
									  "x1=\"" + this.m_strX1 + "\" " + 
									  "y1=\"" + this.m_strY1 + "\" " + 
									  "x2=\"" + this.m_strX2 + "\" " + 
									  "y2=\"" + this.m_strY2 + "\" " + 
									  "dura=\"" + this.m_strDura + "\" " +
									  "delay=\"" + this.m_strDELAY + "\"></Swipe>\n";
		
		return strResult;
	}

}
