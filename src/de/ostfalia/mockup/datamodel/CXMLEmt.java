/**
 * 
 */
package de.ostfalia.mockup.datamodel;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import de.ostfalia.mockup.datamodel.diagram.CDiagramConsts;
import de.ostfalia.mockup.datamodel.mock.CMockDocumentRoot;
import de.ostfalia.mockup.datamodel.mock.CMockView;

/**
 * @author O. Laudi
 *
 */
public class CXMLEmt {
	private final String				m_XMLSPACE = "   ";
	protected String					m_strTagName;
	protected List<CXMLEmt>				m_lstChildren = null;
	protected HashMap<String, String>	m_hmAttribMap = null;
	
	/**
	 * Constructor
	 * @param strTagName
	 */
	public CXMLEmt(String strTagName) {
		m_strTagName = strTagName;
		m_lstChildren = new ArrayList<CXMLEmt>();
		m_hmAttribMap = new HashMap<String, String>();
	}
	
	/**
	 * Adds a Childnode of the type CXMLEmt to the 
	 * current parent node.
	 * @param xmlEmt
	 * @return
	 */
	public boolean addChildNode(CXMLEmt xmlEmt) {
		boolean			isSuccess = true;
		
		try {
			m_lstChildren.add(xmlEmt);
		} catch (Exception e) {
			isSuccess = false;
		}		
		
		return isSuccess;
	}
		
	/**
	 * @return the m_lstChildren
	 */
	public List<CXMLEmt> getChildren() {
		return m_lstChildren;
	}

	/**
	 * Adds an attribut with the name strAttribName and
	 * the value strAttribValue to the current node.
	 * @param strAttribName
	 * @param strAttribValue
	 * * @return
	 */
	public boolean addAttrib(String strAttribName, String strAttribValue) {
		boolean			isSuccess = true;
		
		try {
			m_hmAttribMap.put(strAttribName, strAttribValue);
		} catch (Exception e) {
			isSuccess = false;
		}
		
		return isSuccess;
	}
	
	/**
	 * Saves the two files
	 * strDiagramName.mock and strDiagramName.diagram
	 * @param strDiagramName
	 * @return
	 */
	public boolean saveToFile(String strDiagramName) {
		boolean	isSuccess 	= saveMockFile(new File("DiagMock" + File.separator + strDiagramName + ".mock"));
		if (isSuccess)
			saveDiagramFile(new File("DiagMock" + File.separator + strDiagramName + ".diagram"));
		
		return isSuccess;
	}
	
	/**
	 * Saves the mock-file
	 * @param fMockFile
	 * @return
	 */
	private boolean saveMockFile(File fMockFile) {
		boolean			isSuccess 		= true;
		BufferedWriter	bw;
		String			strWritebuffer 	= "<?xml version=\"1.0\" encoding=\"ASCII\"?>\n";
		
		try {
			fMockFile.createNewFile();
			bw = new BufferedWriter(new FileWriter(fMockFile), 1024);
			strWritebuffer += this.getXMLTree(0);
			
			bw.write(strWritebuffer);
			bw.newLine();
			bw.flush();
		} catch (IOException e) {
			isSuccess = false;
			e.printStackTrace();
		}
		
		return isSuccess;
	}
	
	/**
	 * saves the diagram-file
	 * @param fDiagramFile
	 * @return
	 */
	private boolean saveDiagramFile(File fDiagramFile) {
		boolean			isSuccess 		= true;
		int				nCmtViews		= getCountViews(this);
		BufferedWriter	bw;
		String			strWritebuffer 	= "<?xml version=\"1.0\" encoding=\"ASCII\"?>\n";
		
		try {
			fDiagramFile.createNewFile();
			bw = new BufferedWriter(new FileWriter(fDiagramFile), 1024);
			//strWritebuffer += this.getXMLTree(0);
			
			bw.write(strWritebuffer);
			bw.newLine();
			bw.flush();
		} catch (IOException e) {
			isSuccess = false;
			e.printStackTrace();
		}
		
		return isSuccess;
	}
	
	/**
	 * recursive method to get the stringrepresentation auf the Node 
	 * and its childnodes
	 * @param xmlEmt
	 * @return
	 */
	private String getXMLTree(int nIterationDepth) {
		String strSpace 	= "";
		String strResult 	= "";
		
		for (int i = 0; i < nIterationDepth; i++)
			strSpace += m_XMLSPACE;
		
		if (this.m_lstChildren.size() > 0) {
			// <name atrb1>
			//   <child/>
			// </name>
			// opentag + attribs
			strResult = strSpace + "<" + this.m_strTagName;
			for (String strKey : m_hmAttribMap.keySet()) {
				strResult += " " + strKey + "=\""+ m_hmAttribMap.get(strKey) + "\"";
			}
			strResult += ">\n";
			
			// all childnodes
			nIterationDepth++;
			for(CXMLEmt emt : m_lstChildren) {
				strResult += emt.getXMLTree(nIterationDepth);
			}
			
			// endtag
			strResult += strSpace + "</" + this.m_strTagName + ">\n";
		} else {
			// one tag <name atrb1/>
			strResult = strSpace + "<" + this.m_strTagName;
			for (String strKey : m_hmAttribMap.keySet()) {
				strResult += " " + strKey + "=\""+ m_hmAttribMap.get(strKey) + "\"";
			}
			strResult += "/>\n";
		}
		
		return strResult;
	}
	
	/**
	 * Helper methods
	 */
	
	/**
	 * counts the number of view and overlayviews
	 * @param parentEmt
	 * @return
	 */
	private int getCountViews(CXMLEmt parentEmt) {
		int	nCntViews = 0;
		
		for(CXMLEmt emt : parentEmt.getChildren()) {
			if ((emt instanceof CMockView) || (emt instanceof CMockView))
				nCntViews++;
			nCntViews += getCountViews(emt);
		}
		
		return nCntViews;
	}
	
	
//	private Point getPositionOfView(int nCurrentView, int nViewcount) {
//		
//		return new Point(x, y);
//	}
}
