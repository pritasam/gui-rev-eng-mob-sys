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
	 * @param strID
	 * @param m_strPressed
	 * @param m_strKeycode
	 * @param m_strDura
	 */
	public CKey(String strPressed, String strKeycode,
			String strDura) {
		super();
		this.m_strPressed = strPressed;
		this.m_strKeycode = strKeycode;
		this.m_strDura = strDura;
	}
}
