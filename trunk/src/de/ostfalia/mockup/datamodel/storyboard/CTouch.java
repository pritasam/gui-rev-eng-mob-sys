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
	 * @param strID
	 * @param m_strbtn
	 * @param m_strX
	 * @param m_strY
	 * @param m_strDura
	 */
	public CTouch(String strbtn, String strX, String strY,
			String strDura) {
		super();
		this.m_strbtn = strbtn;
		this.m_strX = strX;
		this.m_strY = strY;
		this.m_strDura = strDura;
	}
}
