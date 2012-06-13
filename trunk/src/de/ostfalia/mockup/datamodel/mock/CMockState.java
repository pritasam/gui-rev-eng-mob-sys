/**
 * 
 */
package de.ostfalia.mockup.datamodel.mock;

import de.ostfalia.mockup.datamodel.CXMLEmt;

/**
 * @author O. Laudi
 *
 */
public abstract class CMockState extends CXMLEmt {

	/**
	 * 
	 * @param strID
	 */
	public CMockState(String strID) {
		super("state");

		// Add attribs
		this.addAttrib("ID", strID);
	}

}
