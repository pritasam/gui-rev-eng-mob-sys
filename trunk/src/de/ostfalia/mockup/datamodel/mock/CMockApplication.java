/**
 * 
 */
package de.ostfalia.mockup.datamodel.mock;

import de.ostfalia.mockup.datamodel.CXMLEmt;

/**
 * @author O. Laudi
 *
 */
public class CMockApplication extends CXMLEmt {

	/**
	 * 
	 * @param strApplicationName
	 * @param nNextRegionID
	 */
	public CMockApplication(String strApplicationName, Integer nNextRegionID) {
		super("mock:Application");

		// Add attribs
		this.addAttrib("name", strApplicationName);
		this.addAttrib("nextRegionID", nNextRegionID.toString());
	}

}
