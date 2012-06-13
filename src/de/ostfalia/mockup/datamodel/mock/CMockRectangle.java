/**
 * 
 */
package de.ostfalia.mockup.datamodel.mock;

import de.ostfalia.mockup.datamodel.CXMLEmt;

/**
 * @author O. Laudi
 *
 */
public class CMockRectangle extends CXMLEmt {

	/**
	 * 
	 * @param strID
	 * @param nHeight
	 * @param nWidth
	 */
	public CMockRectangle(String strID, Integer nHeight, Integer nWidth) {
		super("region");

		// Add attribs
		this.addAttrib("ID", strID);
		this.addAttrib("height", nHeight.toString());
		this.addAttrib("width", nWidth.toString());
	}

}
