/**
 * 
 */
package de.ostfalia.mockup.datamodel.mock;

import de.ostfalia.mockup.datamodel.CXMLEmt;

/**
 * @author O. Laudi
 *
 */
public class CMockApplication extends CXMLEmt {

	/**
	 * 
	 * @param strApplicationName
	 * @param nNextRegionID
	 */
	public CMockApplication(String strApplicationName, Integer nNextRegionID) {
		super("mock:Application");

		// Add attribs
		this.addAttrib("name", strApplicationName);
		this.addAttrib("nextRegionID", nNextRegionID.toString());
	}
	
	/**
	 * Gets the number of regions and add 1 to calculate the 
	 * nextRegionID and refresh it.
	 */
	public void refreshNextRegionID() {
		int nNextRegionID = getNumberOfRegions(this) + 1;
		setNextRegionID(nNextRegionID);
	}
	
	/**
	 * sets the next Region id to a fix value
	 * @param nNextRegionID
	 */
	public void setNextRegionID(int nNextRegionID) {
		this.addAttrib("nextRegionID", String.valueOf(nNextRegionID));
	}
}
