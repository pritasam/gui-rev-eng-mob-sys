/**
 * 
 */
package de.ostfalia.mockup.datamodel.mock;

import de.ostfalia.mockup.datamodel.CXMLEmt;

/**
 * @author O. Laudi
 *
 */
public class CMockOverlayView extends CXMLEmt {

	/**
	 * 
	 * @param strID
	 * @param strImage
	 * @param strOver
	 */
	public CMockOverlayView(String strID, String strImage, String strOver) {
		super("state");

		// Add attribs
		this.addAttrib("xsi:type", "mock:OverlayView");
		this.addAttrib("ID", strID);
		this.addAttrib("image", strImage);
		this.addAttrib("over", strOver);
	}

}
