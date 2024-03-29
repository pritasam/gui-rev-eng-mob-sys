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

import java.util.HashMap;

/**
 * @author O. Laudi
 *
 */
public class CSwipe extends CStoryEvent{
	
	// Member
	protected String						m_strbtn = "";
	protected String						m_strDura = "";
	protected HashMap<String, CSwipePoint>	m_mapPoints = null;
		
	/**
	 * @param m_strbtn
	 * @param m_strX1
	 * @param m_strY1
	 * @param m_strX2
	 * @param m_strY2
	 * @param m_strDura
	 * @param m_strDELAY
	 */
	public CSwipe(String strbtn, String strDura, String strDELAY) {
		super(strDELAY);
		this.m_strbtn = strbtn;
		this.m_strDura = strDura;
		this.m_mapPoints = new HashMap<String, CSwipePoint>();
	}

	/**
	 * @return the m_strbtn
	 */
	public String getBtn() {
		return m_strbtn;
	}

	/**
	 * @param m_strbtn the m_strbtn to set
	 */
	public void setBtn(String strbtn) {
		this.m_strbtn = strbtn;
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

	/**
	 * @return the m_mapPoints
	 */
	public HashMap<String, CSwipePoint> getMapPoints() {
		return m_mapPoints;
	}

	/**
	 * adds a CSwiprPoint to the CSwipe-Event
	 * @param swipePoint
	 */
	public void addtMapPoint(CSwipePoint swipePoint) {
		// get next String-ID
		int nID = this.m_mapPoints.size() + 1;
		swipePoint.setID(String.valueOf(nID));
		this.m_mapPoints.put(String.valueOf(nID), swipePoint);
	}
	

	@Override
	public String getXMLString(int nIterationDepth) {
		String strSpace 	= "";
		String strResult 	= "";
		
		for (int i = 0; i < nIterationDepth; i++)
			strSpace += m_XMLSPACE;
		
		strResult += strSpace + "<Swipe id=\"" + this.m_strID + "\" " +
									  "btn=\"" + this.m_strbtn + "\" " + 
									  "dura=\"" + this.m_strDura + "\" " +
									  "delay=\"" + this.m_strDELAY + "\">\n";
		// insert CSwipePoint 
		nIterationDepth++;
		for (int nPoint = 0; nPoint < m_mapPoints.size(); nPoint++) {
			strResult += m_mapPoints.get(String.valueOf(nPoint + 1)).getXMLString(nIterationDepth);
		}
		
		nIterationDepth--;
		strSpace 	= "";
		for (int i = 0; i < nIterationDepth; i++)
			strSpace += m_XMLSPACE;
		
		strResult += strSpace + "</Swipe>\n";
		
		return strResult;
	}

}
