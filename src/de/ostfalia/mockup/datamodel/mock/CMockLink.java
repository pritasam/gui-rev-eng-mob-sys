/**
 * 
 */
package de.ostfalia.mockup.datamodel.mock;

import de.ostfalia.mockup.datamodel.CXMLEmt;

/**
 * @author O. Laudi
 *
 */
public abstract class CMockLink extends CXMLEmt {

	/**
	 * @param strID
	 */
	public CMockLink(String strID, String strTarget) {
		super("transition");

		// Add attribs
		this.addAttrib("ID", strID);
		this.addAttrib("target", "#" + strTarget);
	}

}
