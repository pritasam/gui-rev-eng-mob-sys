/**
 * 
 */
package de.ostfalia.mockup.datamodel.storyboard;

/**
 * @author O. Laudi
 *
 */
public abstract class CStoryEvent {
	protected String	m_strID = "";
	
	/**
	 * @param m_strNR
	 */
	public CStoryEvent() {
		super();
		this.m_strID = "";
	}
}
