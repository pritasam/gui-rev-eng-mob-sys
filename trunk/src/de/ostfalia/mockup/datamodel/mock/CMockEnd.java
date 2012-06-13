/**
 * 
 */
package de.ostfalia.mockup.datamodel.mock;

import de.ostfalia.mockup.datamodel.CXMLEmt;

/**
 * @author O. Laudi
 *
 */
public class CMockEnd extends CXMLEmt {

	/**
	 * 
	 * @param strID
	 */
	public CMockEnd(String strID) {
		super("state");

		// Add attribs
		this.addAttrib("xsi:type", "mock:End");
		this.addAttrib("ID", strID);
	}

}
