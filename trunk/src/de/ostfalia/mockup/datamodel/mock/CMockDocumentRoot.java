/**
 * 
 */
package de.ostfalia.mockup.datamodel.mock;

import de.ostfalia.mockup.datamodel.CXMLEmt;

/**
 * @author O.Laudi
 *
 */
public class CMockDocumentRoot extends CXMLEmt {

	/**
	 * @param strTagName
	 */
	public CMockDocumentRoot() {
		super("mock:DocumentRoot");
		
		// Add attribs
		this.addAttrib("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		this.addAttrib("xmlns:mock", "http://de.jweimar.mockup/Mock");
	}

}
