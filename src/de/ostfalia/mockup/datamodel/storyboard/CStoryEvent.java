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
package de.ostfalia.mockup.datamodel.storyboard;

/**
 * @author O. Laudi
 *
 */
public abstract class CStoryEvent extends CXMLStoryboardEmt{
	protected String	m_strID 	= "";
	protected String	m_strDELAY 	= "";
	/**
	 * @param m_strNR
	 */
	public CStoryEvent(String strDELAY) {
		super();
		this.m_strID = "";
		this.m_strDELAY = strDELAY;
	}
	
	/**
	 * @return the m_strID
	 */
	public String getID() {
		return m_strID;
	}

	/**
	 * @param m_strID the m_strID to set
	 */
	public void setID(String strID) {
		this.m_strID = strID;
	}

	/**
	 * @return the m_strDELAY
	 */
	public String getDELAY() {
		return m_strDELAY;
	}

	/**
	 * @param m_strDELAY the m_strDELAY to set
	 */
	public void setDELAY(String strDELAY) {
		this.m_strDELAY = strDELAY;
	}

}
