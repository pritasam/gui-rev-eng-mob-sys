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
									  "x2=\"" + this.m_strX1 + "\" " + 
									  "y2=\"" + this.m_strY1 + "\" " + 
									  "dura=\"" + this.m_strDura + "\" " +
									  "delay=\"" + this.m_strDELAY + "\">\n";
		
		return strResult;
	}

}
