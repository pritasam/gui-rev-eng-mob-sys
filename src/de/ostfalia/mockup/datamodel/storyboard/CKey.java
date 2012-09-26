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
public class CKey extends CStoryEvent{

	// Member
	protected String	m_strPressed = "";
	protected String	m_strKeycode = "";
	protected String	m_strDura = "";
	
	/**
	 * @param m_strPressed
	 * @param m_strKeycode
	 * @param m_strDura
	 * @param m_strDELAY
	 */
	public CKey(String strPressed, String strKeycode,
			String strDura, String strDELAY) {
		super(strDELAY);
		this.m_strPressed = strPressed;
		this.m_strKeycode = strKeycode;
		this.m_strDura = strDura;
	}

	
	/**
	 * @return the m_strPressed
	 */
	public String getPressed() {
		return m_strPressed;
	}


	/**
	 * @param m_strPressed the m_strPressed to set
	 */
	public void setPressed(String strPressed) {
		this.m_strPressed = strPressed;
	}


	/**
	 * @return the m_strKeycode
	 */
	public String getKeycode() {
		return m_strKeycode;
	}


	/**
	 * @param m_strKeycode the m_strKeycode to set
	 */
	public void setKeycode(String strKeycode) {
		this.m_strKeycode = strKeycode;
	}


	/**
	 * @return the m_strDura
	 */
	public String getDura() {
		return m_strDura;
	}


	/**
	 * @param m_strDura the m_strDura to set
	 */
	public void setDura(String strDura) {
		this.m_strDura = strDura;
	}


	@Override
	public String getXMLString(int nIterationDepth) {
		String strSpace 	= "";
		String strResult 	= "";
		
		for (int i = 0; i < nIterationDepth; i++)
			strSpace += m_XMLSPACE;
		
		strResult += strSpace + "<Key id=\"" + this.m_strID + "\" " +
									  "pressed=\"" + this.m_strPressed + "\" " + 
									  "keycode=\"" + this.m_strKeycode + "\" " + 
									  "dura=\"" + this.m_strDura + "\" " +
									  "delay=\"" + this.m_strDELAY + "\"></Key>\n";
		
		return strResult;
	}
}
