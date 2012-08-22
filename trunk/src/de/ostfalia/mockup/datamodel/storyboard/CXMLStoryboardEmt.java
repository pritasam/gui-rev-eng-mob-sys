/**
 * 
 */
package de.ostfalia.mockup.datamodel.storyboard;

/**
 * @author O. Laudi
 *
 */
public abstract class CXMLStoryboardEmt {
	protected final String		m_XMLSPACE = "   ";
	
	/**
	 * gets the XML-String of this element and containing subelements
	 */
	public abstract String getXMLString(int nIterationDepth);
}
