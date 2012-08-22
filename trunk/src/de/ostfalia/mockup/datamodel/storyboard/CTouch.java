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
									  "delay=\"" + this.m_strDELAY + "\">\n";
		
		return strResult;
	}
}
