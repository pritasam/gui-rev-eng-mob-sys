/**
 * 
 */
package de.ostfalia.mockup.datamodel.mock;

/**
 * @author O. Laudi
 *
 */
public class CMockStartLink extends CMockLink {

	/**
	 * @param strID
	 * @param strTarget
	 */
	public CMockStartLink(String strID, String strTarget) {
		super(strID, strTarget);

		// Add attribs
		this.addAttrib("xsi:type", "mock:StartLink");
	}

}
