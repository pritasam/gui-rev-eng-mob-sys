/**
 * 
 */
package de.ostfalia.mockup.datamodel.mock;

import de.ostfalia.mockup.datamodel.EnumLinkCategory;

/**
 * @author O. Laudi
 *
 */
public class CMockViewLink extends CMockRVLink {

	/**
	 * @param strID
	 * @param strTarget
	 * @param eLinkCat
	 */
	public CMockViewLink(String strID, String strTarget,
			EnumLinkCategory eLinkCat) {
		super(strID, strTarget, eLinkCat);

		// Add attribs
		this.addAttrib("xsi:type", "mock:ViewLink");
	}

}
