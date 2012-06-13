/**
 * 
 */
package de.ostfalia.mockup.datamodel.mock;

/**
 * @author O. Laudi
 *
 */
public class CMockStart extends CMockState {

	/**
	 * 
	 * @param strID
	 * @param strStartview
	 */
	public CMockStart(String strID, String strStartview) {
		super(strID);

		// Add attribs
		this.addAttrib("xsi:type", "mock:Start");
		this.addAttrib("startview", "#" + strStartview);
	}

}
