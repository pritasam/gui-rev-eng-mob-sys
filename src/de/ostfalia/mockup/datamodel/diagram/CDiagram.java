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
package de.ostfalia.mockup.datamodel.diagram;

import java.util.ArrayList;
import java.util.List;


/**
 * @author O. Laudi
 *
 */
public class CDiagram extends CXMLDiagramEmt {

	// Member
	protected List<String>			m_lstPictogramLinks = null;
	protected List<CXMLDiagramEmt>	m_lstChildren 		= null;
	protected List<CXMLDiagramEmt>	m_lstConnections	= null;
	
	/**
	 * @param m_lstPictogramLinks
	 */
	public CDiagram(String strMockName) {
		super(strMockName);
		this.m_lstPictogramLinks 	= new ArrayList<String>();
		this.m_lstChildren			= new ArrayList<CXMLDiagramEmt>();
		this.m_lstConnections		= new ArrayList<CXMLDiagramEmt>();
	}
	
	/* (non-Javadoc)
	 * @see de.ostfalia.mockup.datamodel.diagram.CXMLDiagramEmt#getXMLString(int)
	 */
	@Override
	public String getXMLString(int nIterationDepth) {
		String strSpace 	= "";
		String strResult 	= "";
		
		for (int i = 0; i < nIterationDepth; i++)
			strSpace += m_XMLSPACE;
		
		strResult += strSpace + "<pi:Diagram xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" " +
				"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:al=\"http://eclipse.org/graphiti/mm/algorithms\" " +
				"xmlns:pi=\"http://eclipse.org/graphiti/mm/pictograms\" visible=\"true\" gridUnit=\"10\" diagramTypeId=\"mockdia\" " +
				"name=\"" + m_strMockName + "\" snapToGrid=\"true\" showGuides=\"true\" pictogramLinks=\"";
						
		// add all pictogramLinks
		//"//@children.0/@link //@children.0/@children.0/@link //@children.0/@children.8/@link //@children.1/@link //@connections.0/@link //@children.2/@link //@connections.1/@link //@connections.1/@connectionDecorators.0/@link //@connections.2/@link //@connections.2/@connectionDecorators.0/@link //@children.3/@link //@children.3/@children.0/@link //@children.3/@children.8/@link //@children.0/@children.0/@children.1/@link //@connections.3/@link //@connections.3/@connectionDecorators.0/@link //@connections.4/@link //@connections.4/@connectionDecorators.0/@link //@children.4/@link //@children.4/@children.0/@link //@children.4/@children.8/@link //@connections.5/@link //@connections.5/@connectionDecorators.0/@link //@connections.6/@link //@connections.6/@connectionDecorators.0/@link //@children.5/@link //@connections.7/@link //@connections.7/@connectionDecorators.0/@link //@children.6/@link //@children.6/@children.0/@link //@children.6/@children.8/@link //@connections.9/@link //@connections.9/@connectionDecorators.0/@link //@connections.10/@link //@connections.10/@connectionDecorators.0/@link //@connections.11/@link //@connections.11/@connectionDecorators.0/@link //@connections.12/@link //@connections.12/@connectionDecorators.0/@link //@children.6/@children.0/@children.1/@link //@children.6/@children.0/@children.2/@link //@connections.13/@link //@connections.13/@connectionDecorators.0/@link //@connections.14/@link //@connections.14/@connectionDecorators.0/@link //@children.7/@link //@children.7/@children.0/@link //@children.7/@children.8/@link //@children.7/@children.0/@children.1/@link //@connections.16/@link //@connections.16/@connectionDecorators.0/@link //@children.7/@children.0/@children.2/@link //@children.8/@link //@children.8/@children.0/@link //@children.8/@children.8/@link //@connections.17/@link //@connections.17/@connectionDecorators.0/@link //@children.8/@children.0/@children.1/@link //@children.8/@children.0/@children.2/@link //@connections.18/@link //@connections.18/@connectionDecorators.0/@link //@connections.19/@link //@connections.19/@connectionDecorators.0/@link //@connections.21/@link //@connections.21/@connectionDecorators.0/@link //@connections.22/@link //@connections.22/@connectionDecorators.0/@link //@connections.23/@link //@connections.23/@connectionDecorators.0/@link //@connections.24/@link //@connections.24/@connectionDecorators.0/@link">
		strResult += "\">\n";
		
		nIterationDepth++;
		strSpace 	= "";
		for (int i = 0; i < nIterationDepth; i++)
			strSpace += m_XMLSPACE;
		
		strResult += strSpace + "<graphicsAlgorithm xsi:type=\"al:Rectangle\" background=\"//@colors.1\" foreground=\"//@colors.0\" " +
					"lineWidth=\"" + CDiagramConsts.LINE_WIDTH + "\" transparency=\"0.0\" width=\"" + CDiagramConsts.DIAGRAM_WIDTH + "\" "+
					"height=\"" + CDiagramConsts.DIAGRAM_HEIGHT + "\"/>\n";	  
		
		// add all Children
		// add all Connections
		// add Colors
		strResult += addColors(strSpace);
		  
		// add Fonts
		nIterationDepth--;
		strSpace 	= "";
		for (int i = 0; i < nIterationDepth; i++)
			strSpace += m_XMLSPACE;
		
		strResult += strSpace + "</pi:Diagram>\n";
		
		return strResult;
	}

	/**
	 * adds thr colors.0 to colors.13
	 * @param strResult
	 * @param strSpace
	 * @return
	 */
	private String addColors(String strSpace) {
		String strResult = "";
		strResult += strSpace + "<colors red=\"227\" green=\"238\" blue=\"249\"/>\n";
		strResult += strSpace + "<colors red=\"255\" green=\"255\" blue=\"255\"/>\n";
		strResult += strSpace + "<colors red=\"50\" green=\"50\" blue=\"50\"/>\n";
		strResult += strSpace + "<colors red=\"200\" green=\"200\" blue=\"200\"/>\n";
		strResult += strSpace + "<colors red=\"180\" green=\"180\" blue=\"150\"/>\n";
		strResult += strSpace + "<colors red=\"200\" green=\"200\" blue=\"255\"/>\n";
		strResult += strSpace + "<colors red=\"51\" green=\"51\" blue=\"153\"/>\n";
		strResult += strSpace + "<colors/>\n";
		strResult += strSpace + "<colors blue=\"255\"/>\n";
		strResult += strSpace + "<colors red=\"255\" green=\"50\" blue=\"50\"/>\n";
		strResult += strSpace + "<colors red=\"187\" green=\"102\"/>\n";
		strResult += strSpace + "<colors red=\"150\" green=\"200\" blue=\"200\"/>\n";
		strResult += strSpace + "<colors red=\"200\" green=\"220\" blue=\"220\"/>\n";
		strResult += strSpace + "<colors green=\"255\"/>\n";
				  
		return strResult;
	}
}
