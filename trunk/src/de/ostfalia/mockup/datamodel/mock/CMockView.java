/**
 * 
 */
package de.ostfalia.mockup.datamodel.mock;

import de.ostfalia.mockup.datamodel.CXMLEmt;

/**
 * @author O. Laudi
 *
 */
public class CMockView extends CMockState {

	/**
	 * @param strTagName
	 */
	public CMockView(String strID, String strImage, boolean isLandscape) {
		super(strID);

		// Add attribs
		this.addAttrib("xsi:type", "mock:FullView");
		this.addAttrib("image", strImage);
		this.addAttrib("Landscape", isLandscape?"true":"false");
	}

}
