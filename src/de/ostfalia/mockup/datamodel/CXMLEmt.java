/**
 * 
 */
package de.ostfalia.mockup.datamodel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

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
	
	public boolean saveToFile(File outputFile) {
		BufferedWriter	bw;
		String			strWritebuffer = "<?xml version=\"1.0\" encoding=\"ASCII\"?>\n";
		boolean			isSuccess = true;
		try {
			outputFile.createNewFile();
			bw = new BufferedWriter(new FileWriter(outputFile), 1024);
			strWritebuffer += getXMLTree(this, 0);
			
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
	private String getXMLTree(CXMLEmt xmlEmt, int nIterationDepth) {
		String strSpace = "";
		String strResult = "";
		
		for (int i = 0; i < nIterationDepth; i++)
			strSpace += m_XMLSPACE;
		
		if (xmlEmt.m_lstChildren.size() > 0) {
			// <name atrb1>
			//   <child/>
			// </name>
			// opentag + attribs
			strResult = strSpace + "<" + xmlEmt.m_strTagName;
			for (String strKey : m_hmAttribMap.keySet()) {
				strResult += " " + strKey + "=\""+ m_hmAttribMap.get(strKey) + "\"";
			}
			strResult += ">\n";
			
			// all childnodes
			for(CXMLEmt emt : m_lstChildren) {
				strResult += getXMLTree(emt, ++nIterationDepth);
			}
			
			// endtag
			strResult = strSpace + "</" + xmlEmt.m_strTagName + ">\n";
		} else {
			// one tag <name atrb1/>
			strResult = strSpace + "<" + xmlEmt.m_strTagName;
			for (String strKey : m_hmAttribMap.keySet()) {
				strResult += " " + strKey + "=\""+ m_hmAttribMap.get(strKey) + "\"";
			}
			strResult += "/>\n";
		}
		
		return strResult;
	}
}
