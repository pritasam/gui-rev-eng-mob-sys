/**
 * 
 */
package de.ostfalia.mockup.datamodel.mock;

import de.ostfalia.mockup.datamodel.EnumLinkCategory;

/**
 * @author O. Laudi
 *
 */
public abstract class CMockRVLink extends CMockLink {

	/**
	 * 
	 * @param strID
	 * @param strTarget
	 * @param eLinkCat
	 */
	public CMockRVLink(String strID, String strTarget, EnumLinkCategory eLinkCat) {
		super(strID, strTarget);

		// Add attribs
		this.addAttrib("type", eLinkCat.name());
	}

}
