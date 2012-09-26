//    MockVNC-Client, extends the original Tight-VNC-Client from 
//	  http://www.tightvnc.com/ for GUI-Reverseengineering-features
//    for mobile devices.
//    Copyright (C) 2012  Oliver Laudi
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
