/**
 * 
 */
package de.ostfalia.mockup.datamodel.mock;

/**
 * @author O. Laudi
 *
 */
public class CMockEnd extends CMockState {

	/**
	 * 
	 * @param strID
	 */
	public CMockEnd(String strID) {
		super(strID);

		// Add attribs
		this.addAttrib("xsi:type", "mock:End");
	}

}
