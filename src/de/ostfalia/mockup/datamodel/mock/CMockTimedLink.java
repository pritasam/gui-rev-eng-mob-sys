/**
 * 
 */
package de.ostfalia.mockup.datamodel.mock;

/**
 * @author O. Laudi
 *
 */
public class CMockTimedLink extends CMockLink {

	/**
	 * 
	 * @param strID
	 * @param strTarget
	 * @param dAfterSeconds
	 */
	public CMockTimedLink(String strID, String strTarget, Double dAfterSeconds) {
		super(strID, strTarget);

		// Add attribs
		this.addAttrib("xsi:type", "mock:TimedLink");
		this.addAttrib("afterSeconds", dAfterSeconds.toString());
	}

}
