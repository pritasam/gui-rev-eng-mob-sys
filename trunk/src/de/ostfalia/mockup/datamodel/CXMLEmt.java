/**
 * 
 */
package de.ostfalia.mockup.datamodel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.swing.plaf.metal.MetalIconFactory.FolderIcon16;

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
	public boolean saveToMockjarFile(String strDiagramName) {
		// gen Mock-file
		boolean	isSuccess	= new File("DiagMock" + File.separator + 
											strDiagramName + File.separator + 
												"diagrams").mkdirs();
		
		if (isSuccess)
			isSuccess = saveMockFile(new File("DiagMock" + File.separator + 
												strDiagramName + File.separator + 
													"diagrams" + File.separator +
														strDiagramName + ".mock"));
		
		// gen Diagram-File
		if (isSuccess) {
			isSuccess = saveDiagramFile(new File("DiagMock" + File.separator +
													strDiagramName + File.separator + 
														"diagrams" + File.separator +
															strDiagramName + ".diagram"));
		}
			
		
		// gen Info-File
		if (isSuccess) {
			isSuccess	= new File("DiagMock" + File.separator +
										strDiagramName + File.separator + 
											"export").mkdirs();
			
			if (isSuccess)
				isSuccess = saveInfoFile(new File("DiagMock" + File.separator +
													strDiagramName + File.separator + 
														"export" + File.separator +
															"info.txt"), strDiagramName);
		}
		
		// gen preview-file
		if (isSuccess) {
			isSuccess = savePreviewFile(new File("DiagMock" + File.separator +
													strDiagramName + File.separator + 
														"export" + File.separator +
															"preview.jpg"), strDiagramName);
		}
		
		// copy mockup-pictures to folder "images"
		if (isSuccess) {
			isSuccess	= new File("DiagMock" + File.separator +
										strDiagramName + File.separator + 
											"images").mkdirs();
		
			//TODO: create copyImages-method
		}
		
		// create mockjar-file
		if (isSuccess) {
			isSuccess = createMockjarFile("DiagMock" + File.separator + strDiagramName, strDiagramName);
		}
		
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
		int				nCntViews		= getCountViews(this);
		Point			ptViewPosition	= null;
		BufferedWriter	bw;
		String			strWritebuffer 	= "<?xml version=\"1.0\" encoding=\"ASCII\"?>\n";
		
		try {
			fDiagramFile.createNewFile();
			bw = new BufferedWriter(new FileWriter(fDiagramFile), 1024);
			//strWritebuffer += this.getXMLTree(0);
			ptViewPosition	= getPositionOfView(1, nCntViews);
			
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
	 * generates the Info.txt
	 * @param fInfoFile
	 * @param strMockupName
	 * @return
	 */
	private boolean saveInfoFile(File fInfoFile, String strMockupName) {
		boolean			isSuccess 		= true;
		BufferedWriter	bw;
		String			strWritebuffer 	= "Project: " + strMockupName + "\n";
		
		try {
			fInfoFile.createNewFile();
			bw = new BufferedWriter(new FileWriter(fInfoFile), 1024);
			strWritebuffer += "Preview: " + strMockupName + "/export/preview.jpg\n";
			strWritebuffer += "Mock: /" + strMockupName + ".mocks/" + strMockupName + ".mock\n";
			
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
	 * saves a preview.jpg which contains the Mockup-name
	 * @param fPrevFile
	 * @param strMockupName
	 * @return
	 */
	private boolean savePreviewFile(File fPrevFile, String strMockupName) {
		boolean			isSuccess 		= true;
		BufferedImage	biPreview		= new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
		Graphics2D		g				= biPreview.createGraphics();
		
		g.setBackground(Color.LIGHT_GRAY);
		//g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.BOLD, 96));
		g.drawString(strMockupName, 15, 480 / 2);
		
		try {
			isSuccess = ImageIO.write(biPreview, "jpg", fPrevFile);
	    }
	    catch (IOException e) {
	    	e.printStackTrace();
	    }
		
		return isSuccess;
	}
	
	/**
	 * copies all neccessary image-file for the Mockup-diagram 
	 * to the image-folder
	 * @return
	 */
	private boolean copyImages() {
		boolean			isSuccess 	= true;
		
		return isSuccess;
	}
	
	/**
	 * creates a zip-file of all neccessary mockup-files and
	 * renames it to "mockjar"
	 * @param strFolderToFiles
	 * @param strMockUpName
	 * @return
	 */
	private boolean createMockjarFile(String strFolderToFiles, String strMockUpName) {
		boolean			isSuccess 	= true;
		ZipOutputStream	zipOut		= null;
		
		try {
			zipOut = new ZipOutputStream(new FileOutputStream(strFolderToFiles + ".zip"));
		} catch (FileNotFoundException e) {
			isSuccess = false;
			e.printStackTrace();
		}
		finally
		{
			try
			{
				// recursive method to iterate mockup-folder
				iterateMockupFolder(new File(strFolderToFiles), zipOut, 
						strFolderToFiles.substring(0, strFolderToFiles.length() - strMockUpName.length()));
				
				// rename generated zipfile to mockjar
				File zipFile = new File(strFolderToFiles.substring(0, strFolderToFiles.length() - strMockUpName.length() - 1), 
						strMockUpName + ".zip");
				isSuccess = zipFile.renameTo(new File(strFolderToFiles.substring(0, strFolderToFiles.length() - strMockUpName.length() - 1), 
						strMockUpName + ".mockjar"));
				
				if (zipOut != null)
					zipOut.close();
			}
			catch(Exception ex)
			{
				isSuccess = false;
			}
		}
		
		return isSuccess;
	}
	
	/**
	 * recursive methode to iterate over a folderToFiles and create
	 * a zip-file including subfolders
	 * @param folderToFiles
	 * @param zipOut
	 * @param strRootFolder
	 */
	private void iterateMockupFolder(File folderToFiles, ZipOutputStream zipOut, String strRootFolder) {
		File[] files = folderToFiles.listFiles();
		
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					// recursive call with subfolder
					iterateMockupFolder(files[i], zipOut, strRootFolder);
					
					}
				else {
					// add file to archiv
					try {
						String strPath			= files[i].getPath(); //.substring(strRootFolder.length());
						FileInputStream inFile 	= new FileInputStream(strPath);
						zipOut.putNextEntry(new ZipEntry(strPath.substring(strRootFolder.length())));
						byte[] buffer = new byte[4096];
						int nLen;
						
						while ((nLen = inFile.read(buffer)) > 0) {
							zipOut.write(buffer, 0, nLen);
						}
						
						inFile.close();
						
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}
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
	
	/**
	 * gets the gridposition of a view in a set of many views
	 * 
	 * 0 1 2 3 x
	 * 1
	 * 2
	 * y
	 * 
	 * @param nCurrentView
	 * @param nViewcount
	 * @return
	 */
	private Point getPositionOfView(int nCurrentView, int nViewcount) {
		int nRatioSum = CDiagramConsts.ASPECT_RATIO_X + CDiagramConsts.ASPECT_RATIO_Y;
		
		// get max x and max y number of views
		int nMaxX = (int)((Math.sqrt(Integer.valueOf(nViewcount)) * (2.0 / nRatioSum * CDiagramConsts.ASPECT_RATIO_X)) + 0.5);
		//int nMaxY = (int)((Math.sqrt(Integer.valueOf(nViewcount)) * (2.0 / nRatioSum * CDiagramConsts.ASPECT_RATIO_Y)) + 0.5);
	
		// get exact position of nCurrentView
		int nX = nCurrentView % nMaxX;
		int nY = (int)(nCurrentView / nMaxX);
		
		return new Point(nX, nY);
	}
}
