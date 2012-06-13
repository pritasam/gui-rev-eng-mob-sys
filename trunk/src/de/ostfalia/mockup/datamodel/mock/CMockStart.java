/**
 * 
 */
package de.ostfalia.mockup.datamodel.mock;

import de.ostfalia.mockup.datamodel.CXMLEmt;

/**
 * @author O. Laudi
 *
 */
public class CMockStart extends CXMLEmt {

	/**
	 * 
	 * @param strID
	 * @param strStartview
	 */
	public CMockStart(String strID, String strStartview) {
		super("state");

		// Add attribs
		this.addAttrib("xsi:type", "mock:Start");
		this.addAttrib("ID", strID);
		this.addAttrib("startview", strStartview);
	}

}
