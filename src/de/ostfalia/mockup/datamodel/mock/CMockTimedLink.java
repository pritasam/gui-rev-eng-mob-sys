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
	public CMockTimedLink(String strID, String strTarget, String strAfterSeconds) {
		super(strID, strTarget);

		// Add attribs
		this.addAttrib("xsi:type", "mock:TimedLink");
		this.addAttrib("afterSeconds", strAfterSeconds);
	}

}
