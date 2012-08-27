/**
 * 
 */
package de.ostfalia.mockup.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.ostfalia.mockup.datamodel.CXMLEmt;
import de.ostfalia.mockup.datamodel.EnumKey;
import de.ostfalia.mockup.datamodel.mock.CMockApplication;
import de.ostfalia.mockup.datamodel.mock.CMockDocumentRoot;
import de.ostfalia.mockup.datamodel.mock.CMockEnd;
import de.ostfalia.mockup.datamodel.mock.CMockKeyLink;
import de.ostfalia.mockup.datamodel.mock.CMockOverlayView;
import de.ostfalia.mockup.datamodel.mock.CMockStart;
import de.ostfalia.mockup.datamodel.mock.CMockStartLink;
import de.ostfalia.mockup.datamodel.mock.CMockView;
import de.ostfalia.mockup.datamodel.storyboard.CSequence;
import de.ostfalia.mockup.datamodel.storyboard.CStoryboard;

/**
 * @author O. Laudi
 *
 */
public class CMockupGenerator {
	private String							m_strDiagramName;
	private CXMLEmt							m_mockTree;
	private CXMLEmt							m_diagramTree;
	private CStoryboard						m_storyboard;
	private HashMap<String, List<String>>	m_picMap;
	
	public CMockupGenerator(String strDiagramName, CStoryboard	storyboard) {
		m_strDiagramName	= strDiagramName;
		m_mockTree 			= null;
		m_diagramTree		= null;
		m_storyboard		= storyboard;
	}
	
	/**
	 * Testmethod to test the mock-model-generator
	 */
	public boolean testMockModel() {
		m_mockTree = new CMockDocumentRoot();
		CMockApplication mockApp = new CMockApplication(m_strDiagramName, 3);
		CMockStart mockStart = new CMockStart("_Start", "Intro");
		mockStart.addChildNode(new CMockStartLink("Start123", "Intro"));
		mockApp.addChildNode(mockStart);
		CMockView mockView1 = new CMockView("Intro", "images/Master1.png", false);
		mockView1.addChildNode(new CMockKeyLink("KeyID123", "_End", EnumKey.HOME));
		mockApp.addChildNode(mockView1);
		mockApp.addChildNode(new CMockView("Targetview", "images/Master2.png", false));
		mockApp.addChildNode(new CMockOverlayView("OverLayView", "images/Master3.png", "Targetview", true, false));
		mockApp.addChildNode(new CMockEnd("_End"));
		m_mockTree.addChildNode(mockApp);
		//TODO: m_picMap mit Master1 und Master2.png initialisieren
		return m_mockTree.saveToMockjarFile(m_strDiagramName, m_picMap);
	}
	
	/**
	 * Creates the mockfile based on the storyboard.
	 * in intermediate steps, there will be done optimizations
	 * @return
	 */
	public boolean createMockModel() {
		boolean isSuccess 	= true;
		
		// create picMap with crc: Map<crc, id>
		createPicMap();
		
		// create directed graph
		isSuccess = buildMockTree();
		
		return isSuccess;
	}
	
	/**
	 * returns the md5-sum ofa file
	 * @param strFilename
	 * @return
	 */
	public String getMD5(String strFilename) {
		MessageDigest md;
		FileInputStream fis;
		StringBuffer sb = new StringBuffer();
		
		try {
			md 	= MessageDigest.getInstance("MD5");
			fis = new FileInputStream(strFilename);
			
			byte[] dataBytes = new byte[1024];
			 
		    int nread = 0;
				while ((nread = fis.read(dataBytes)) != -1) {
				    md.update(dataBytes, 0, nread);
				}
		    byte[] mdbytes = md.digest();
		    
		    for (int i = 0; i < mdbytes.length; i++) {
		        sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
		    }
		    
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	
	/**
	 * creates a HashMap <String, List<String>> with crc as Key and IDs for Screenshots as Values
	 */
	private void createPicMap() {
		String strCRC		= "";
		CSequence sequence	= null;
		List<String> lstTmp	= null;
		m_picMap = new HashMap<String, List<String>>();
		
		for (int nSeq = 1; nSeq <= m_storyboard.getSequences().size(); nSeq++) {
			sequence = m_storyboard.getSequences().get(String.valueOf(nSeq));
			
			// get startID
			strCRC = getMD5("Screenshots" + File.separator + 
					"scr_" + sequence.getSTARTID() + ".png");
			
			// does crc already exists in hashmap?
			if (m_picMap.containsKey(strCRC)) {
				// add value
				lstTmp = m_picMap.get(strCRC);
				if (!lstTmp.contains(sequence.getSTARTID())) {
					lstTmp.add(sequence.getSTARTID());
					m_picMap.put(strCRC, lstTmp);
				}
			}
			else {
				// add key and value
				lstTmp = new ArrayList<String>();
				lstTmp.add(sequence.getSTARTID());
				m_picMap.put(strCRC, lstTmp);
			}
			
			// get targetID
			strCRC = getMD5("Screenshots" + File.separator + 
					"scr_" + sequence.getTARGETID() + ".png");
			
			// does crc already exists in hashmap?
			if (m_picMap.containsKey(strCRC)) {
				// add value
				lstTmp = m_picMap.get(strCRC);
				if (!lstTmp.contains(sequence.getTARGETID())) {
					lstTmp.add(sequence.getTARGETID());
					m_picMap.put(strCRC, lstTmp);
				}
			}
			else {
				// add key and value
				lstTmp = new ArrayList<String>();
				lstTmp.add(sequence.getTARGETID());
				m_picMap.put(strCRC, lstTmp);
			}
		}
	}
	
	/**
	 * builds the mocktree based on the storyboard and the picMap
	 * @return
	 */
	private boolean buildMockTree() {
		CSequence sequence	= null;
		
		m_mockTree = new CMockDocumentRoot();
		CMockApplication mockApp = new CMockApplication(m_strDiagramName, 3);
		
		// iterate all sequences of storyboard
		for (int nSeq = 1; nSeq <= m_storyboard.getSequences().size(); nSeq++) {
			sequence = m_storyboard.getSequences().get(String.valueOf(nSeq));
			
			if (nSeq == 1) {
				// Start
				CMockStart mockStart = new CMockStart("_Start", findCRCforID(sequence.getSTARTID()));
				mockStart.addChildNode(new CMockStartLink("Start123", findCRCforID(sequence.getSTARTID())));
				mockApp.addChildNode(mockStart);
			}
			else if (nSeq == m_storyboard.getSequences().size()) {
				// End
				CMockView mockView1 = new CMockView(findCRCforID(sequence.getTARGETID()), "images/" + sequence.getTARGETID() + ".png", false);
				mockView1.addChildNode(new CMockKeyLink("KeyID_End", "_End", EnumKey.HOME));
				mockApp.addChildNode(mockView1);
				mockApp.addChildNode(new CMockEnd("_End"));
			}
			else {
				CMockView mockView1 = new CMockView(findCRCforID(sequence.getTARGETID()), "images/" + sequence.getTARGETID() + ".png", false);
				mockView1.addChildNode(new CMockKeyLink("KeyID123", "_End", EnumKey.HOME));
				mockApp.addChildNode(mockView1);
			}
		}
		
		m_mockTree.addChildNode(mockApp);
		return m_mockTree.saveToMockjarFile(m_strDiagramName, m_picMap);
	}
	
	/**
	 * find the CRC to a saved Screenshot-ID
	 * @param strID
	 * @return
	 */
	private String findCRCforID(String strID) {
		String strReturn 		= "";
		List<String> lstValue	= null;
		
		if (m_picMap != null) {
			for (String strCRC : m_picMap.keySet()) {
				lstValue = m_picMap.get(strCRC);
				
				if (lstValue.contains(strID)) {
					strReturn = strCRC;
				}
			}
		}
		
		return strReturn;
	}
}
