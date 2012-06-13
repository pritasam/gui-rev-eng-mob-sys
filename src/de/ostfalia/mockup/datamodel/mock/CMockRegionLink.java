/**
 * 
 */
package de.ostfalia.mockup.datamodel.mock;

import de.ostfalia.mockup.datamodel.EnumLinkCategory;

/**
 * @author O. Laudi
 *
 */
public class CMockRegionLink extends CMockRVLink {

	/**
	 * 
	 * @param strID
	 * @param strTarget
	 * @param eLinkCat
	 * @param strRegion
	 */
	public CMockRegionLink(String strID, String strTarget,
			EnumLinkCategory eLinkCat, String strRegion) {
		super(strID, strTarget, eLinkCat);

		// Add attribs
		this.addAttrib("xsi:type", "mock:RegionLink");
		this.addAttrib("region", "#" + strRegion);
	}

}
