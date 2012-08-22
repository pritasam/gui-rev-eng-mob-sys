/**
 * 
 */
package de.ostfalia.mockup.datamodel.storyboard;

/**
 * @author O. Laudi
 *
 */
public class CKey extends CStoryEvent{

	// Member
	protected String	m_strPressed = "";
	protected String	m_strKeycode = "";
	protected String	m_strDura = "";
	
	/**
	 * @param m_strPressed
	 * @param m_strKeycode
	 * @param m_strDura
	 * @param m_strDELAY
	 */
	public CKey(String strPressed, String strKeycode,
			String strDura, String strDELAY) {
		super(strDELAY);
		this.m_strPressed = strPressed;
		this.m_strKeycode = strKeycode;
		this.m_strDura = strDura;
	}

	@Override
	public String getXMLString(int nIterationDepth) {
		String strSpace 	= "";
		String strResult 	= "";
		
		for (int i = 0; i < nIterationDepth; i++)
			strSpace += m_XMLSPACE;
		
		strResult += strSpace + "<Key id=\"" + this.m_strID + "\" " +
									  "pressed=\"" + this.m_strPressed + "\" " + 
									  "keycode=\"" + this.m_strKeycode + "\" " + 
									  "dura=\"" + this.m_strDura + "\" " +
									  "delay=\"" + this.m_strDELAY + "\">\n";
		
		return strResult;
	}
}
