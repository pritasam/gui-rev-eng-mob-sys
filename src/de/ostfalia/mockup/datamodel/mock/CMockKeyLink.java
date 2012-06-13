/**
 * 
 */
package de.ostfalia.mockup.datamodel.mock;

import de.ostfalia.mockup.datamodel.EnumKey;

/**
 * @author O. Laudi
 *
 */
public class CMockKeyLink extends CMockLink {

	/**
	 * 
	 * @param strID
	 * @param strTarget
	 * @param eKey
	 */
	public CMockKeyLink(String strID, String strTarget, EnumKey eKey) {
		super(strID, strTarget);

		// Add attribs
		this.addAttrib("xsi:type", "mock:KeyLink");
		this.addAttrib("key", eKey.name());
	}

}
