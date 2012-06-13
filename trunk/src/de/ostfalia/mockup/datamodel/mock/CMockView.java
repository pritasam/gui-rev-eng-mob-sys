/**
 * 
 */
package de.ostfalia.mockup.datamodel.mock;

import de.ostfalia.mockup.datamodel.CXMLEmt;

/**
 * @author O. Laudi
 *
 */
public class CMockView extends CXMLEmt {

	/**
	 * @param strTagName
	 */
	public CMockView(String strID, String strImage) {
		super("state");

		// Add attribs
		this.addAttrib("xsi:type", "mock:Start");
		this.addAttrib("ID", strID);
		this.addAttrib("image", strImage);
	}

}
