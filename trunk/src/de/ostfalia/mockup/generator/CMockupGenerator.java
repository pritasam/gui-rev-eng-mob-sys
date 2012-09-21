/**
 * 
 */
package de.ostfalia.mockup.generator;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import de.ostfalia.mockup.datamodel.CXMLEmt;
import de.ostfalia.mockup.datamodel.EnumKey;
import de.ostfalia.mockup.datamodel.EnumLinkCategory;
import de.ostfalia.mockup.datamodel.mock.CMockApplication;
import de.ostfalia.mockup.datamodel.mock.CMockDocumentRoot;
import de.ostfalia.mockup.datamodel.mock.CMockEnd;
import de.ostfalia.mockup.datamodel.mock.CMockKeyLink;
import de.ostfalia.mockup.datamodel.mock.CMockOverlayView;
import de.ostfalia.mockup.datamodel.mock.CMockRectangleWithPosition;
import de.ostfalia.mockup.datamodel.mock.CMockRegionLink;
import de.ostfalia.mockup.datamodel.mock.CMockStart;
import de.ostfalia.mockup.datamodel.mock.CMockStartLink;
import de.ostfalia.mockup.datamodel.mock.CMockState;
import de.ostfalia.mockup.datamodel.mock.CMockTimedLink;
import de.ostfalia.mockup.datamodel.mock.CMockView;
import de.ostfalia.mockup.datamodel.storyboard.CKey;
import de.ostfalia.mockup.datamodel.storyboard.CSequence;
import de.ostfalia.mockup.datamodel.storyboard.CStoryEvent;
import de.ostfalia.mockup.datamodel.storyboard.CStoryboard;
import de.ostfalia.mockup.datamodel.storyboard.CSwipe;
import de.ostfalia.mockup.datamodel.storyboard.CSwipePoint;
import de.ostfalia.mockup.datamodel.storyboard.CTouch;
import de.ostfalia.screenshot.analysis.CPicAnalyzer;
import de.ostfalia.viewer.gui.extension.CProgressWnd;

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
		
		// buttondetection
		//detectButtons();
		
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
	 * detect buttons in images stored in PicMap
	 * @return
	 */
	private boolean detectButtons() {
		// iterate picMap
		if (this.m_picMap != null) {
			for (String strMD5 : this.m_picMap.keySet()) {
				List<String> lstEmt		= this.m_picMap.get(strMD5);
				
				// get first Value in List
				if (lstEmt != null) {
					// iterate through valueList
					for (String strPicID : lstEmt) {
						// check sequences with strPicId as startID
						if (m_storyboard != null) {
							for (int nSeq = 0; nSeq < m_storyboard.getSequences().size(); nSeq++) {
								CSequence seq = m_storyboard.getSequences().get(String.valueOf(nSeq + 1));
								// only if startID of Sequence fits strPicID
								if (seq.getSTARTID().equals(strPicID)) {
									//TODO auch für ASYNCs
									// Check for each Screenshot, if there is a click or swipe event
									for (int nSync = 0; nSync < seq.getMapSYNC().size(); nSync++) {
										CStoryEvent storyEvent = seq.getMapSYNC().get(String.valueOf(nSync + 1));
										
										if (storyEvent instanceof CTouch) {
											if (lstEmt.size() > 0) {
												CPicAnalyzer analyze	= new CPicAnalyzer("Screenshots" + File.separator + 
																						   "scr_" + strPicID + ".png");
												
												// testdata
												analyze.getSurroundedRect(new Point(Integer.valueOf(((CTouch)storyEvent).getX()), 
																					Integer.valueOf(((CTouch)storyEvent).getY())));
											}
										}
										//TODO Swipe?
									}
								}
							}
						}
					}
				}
			}
		}
		
		
		
		return true;
	}
	
	/**
	 * builds the mocktree based on the storyboard and the picMap
	 * @return
	 */
	private boolean buildMockTree() {
		CSequence sequence			= null;
		boolean	isPicLandscape		= false;
		boolean isStoryLandscape	= false;
		boolean	isStateAvailable	= false;
		int		nNextRegionID		= 0;
		
		m_mockTree = new CMockDocumentRoot();
		CMockApplication mockApp 	= new CMockApplication(m_strDiagramName, 0);
		List<CXMLEmt> seqInMockApp	= mockApp.getChildren();
		CMockState	md5State		= null;
		CProgressWnd progress		= new CProgressWnd(m_storyboard.getSequences().size(), "Creating *.mockjar file");
		progress.show();
		
		// iterate all sequences of storyboard
		for (int nSeq = 1; nSeq <= m_storyboard.getSequences().size(); nSeq++) {
			sequence 	= m_storyboard.getSequences().get(String.valueOf(nSeq));
			md5State	= null;
			
			// search for md5-state in seqInMockApp to refresh content
			isStateAvailable	= false;
			for (CXMLEmt cxmlEmt : seqInMockApp) {
				if (cxmlEmt instanceof CMockState)  {
					if (((CMockState)cxmlEmt).getAttrib("ID").equals(findMD5forID(sequence.getSTARTID()))) {
						isStateAvailable	= true;
						md5State = (CMockState)cxmlEmt;
					}
				}
			}			
			
			// if no md5, create new Emt
			if (md5State == null) {
				// get imageinformations
				try {
					BufferedImage bufferedImage = ImageIO.read(new FileInputStream("Screenshots/scr_" + sequence.getSTARTID() + ".png"));
					
					// if pic width > height, then landscape
					if (bufferedImage.getWidth() > bufferedImage.getHeight())
						isPicLandscape = true;
					else
						isPicLandscape = false;
					
					// if storybaord width > height, then landscape
					if (Integer.valueOf(m_storyboard.getWidth()) > Integer.valueOf(m_storyboard.getHeight()))
						isStoryLandscape = true;
					else
						isStoryLandscape = false;
					
					if (isPicLandscape != isStoryLandscape) {
						// change sizes
						// check dimensions
						if ((Integer.valueOf(m_storyboard.getHeight()) != bufferedImage.getWidth()) ||
								(Integer.valueOf(m_storyboard.getWidth()) != bufferedImage.getHeight())) {
							// overlayView
							md5State = new CMockOverlayView(findMD5forID(sequence.getSTARTID()), 
									"images/scr_" + sequence.getSTARTID() + ".png", 
									"", true, isPicLandscape);
						}
						else {
							// View
							md5State = new CMockView(findMD5forID(sequence.getSTARTID()), 
									"images/scr_" + sequence.getSTARTID() + ".png", 
									isPicLandscape);
						}
					}
					else {
						// same sizes
						// check dimensions
						if ((Integer.valueOf(m_storyboard.getWidth()) != bufferedImage.getWidth()) ||
								(Integer.valueOf(m_storyboard.getHeight()) != bufferedImage.getHeight())) {
							// overlayView
							md5State = new CMockOverlayView(findMD5forID(sequence.getSTARTID()), 
									"images/scr_" + sequence.getSTARTID() + ".png", 
									"", true, isPicLandscape);
						}
						else {
							// View
							md5State = new CMockView(findMD5forID(sequence.getSTARTID()), 
									"images/scr_" + sequence.getSTARTID() + ".png", 
									isPicLandscape);
						}
					}
					// TODO "over" füllen in overlayView
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			// create Transition
			if (sequence.getMapSYNC().size() > 0) {
				// SyncTransitions
				for (String strEmt : sequence.getMapSYNC().keySet()) {
					if (sequence.getMapSYNC().get(strEmt) instanceof CKey) {
						// KeyLink
						md5State.addChildNode(new CMockKeyLink("", 
								findMD5forID(sequence.getTARGETID()), 
								EnumKey.getEnumByValue(Integer.valueOf(((CKey)sequence.getMapSYNC().get(strEmt)).getKeycode()))));
					}
					else {
						// Touch or Swipe
						if (sequence.getMapSYNC().get(strEmt) instanceof CSwipe) {
							// ViewLink
							//TODO
							
							// RegionLink + Region
							CPicAnalyzer analyze	= new CPicAnalyzer("Screenshots" + File.separator + 
									   "scr_" + sequence.getSTARTID() + ".png");

							// Get Rectangle
							Rectangle rectButton = null;
							if (((CSwipe)sequence.getMapSYNC().get(strEmt)).getMapPoints().size() > 0) {
								// Get first SwipePoint
								CSwipePoint swpPnt = ((CSwipe)sequence.getMapSYNC().get(strEmt)).getMapPoints().get("1");
								rectButton = analyze.getSurroundedRect(
										new Point(Integer.valueOf(swpPnt.getX()), 
												  Integer.valueOf(swpPnt.getY())));
							}
							else {
								rectButton = new Rectangle(1, 1, 
										Integer.valueOf(m_storyboard.getWidth()), Integer.valueOf(m_storyboard.getHeight()));
							}
							
							// Create RegionLink
							md5State.addChildNode(new CMockRegionLink("", 
									findMD5forID(sequence.getTARGETID()), 
									EnumLinkCategory.Swipe, 
									"Region_" + String.valueOf(nNextRegionID)));
							
							// Create Region
							md5State.addChildNode(new CMockRectangleWithPosition("Region_" + nNextRegionID,
									rectButton.height, rectButton.width,
									rectButton.x, rectButton.y));
							
							nNextRegionID++;
						}
						else if (sequence.getMapSYNC().get(strEmt) instanceof CTouch) {
							// ViewLink
							//TODO
							
							// RegionLink + Region
							CPicAnalyzer analyze	= new CPicAnalyzer("Screenshots" + File.separator + 
									   "scr_" + sequence.getSTARTID() + ".png");

							Rectangle rectButton = analyze.getSurroundedRect(
									new Point(Integer.valueOf(((CTouch)sequence.getMapSYNC().get(strEmt)).getX()), 
											  Integer.valueOf(((CTouch)sequence.getMapSYNC().get(strEmt)).getY())));
							
							// Create RegionLink
							md5State.addChildNode(new CMockRegionLink("", 
									findMD5forID(sequence.getTARGETID()), 
									EnumLinkCategory.Touch, 
									"Region_" + String.valueOf(nNextRegionID)));
							
							// Create Region
							md5State.addChildNode(new CMockRectangleWithPosition("Region_" + nNextRegionID,
									rectButton.height, rectButton.width,
									rectButton.x, rectButton.y));
							
							nNextRegionID++;
						}
					}
					
				}
			}
			else if (sequence.getMapASYNC().size() > 0 ) {
				// ASyncTransitions
			}
			else {
				// no Events -> TimeLink
				md5State.addChildNode(new CMockTimedLink("", 
						findMD5forID(sequence.getTARGETID()), sequence.getDELAY()));
			}
			
			// add state to List
			if (!isStateAvailable) {
				seqInMockApp.add(md5State);
			}
			
			if (nSeq == 1) {
				// if first seq, then add start
				CMockStart mockStart = new CMockStart("_Start", findMD5forID(sequence.getSTARTID()));
				mockStart.addChildNode(new CMockStartLink("Start123", findMD5forID(sequence.getSTARTID())));
				seqInMockApp.add(mockStart);
			}
			else if (nSeq == m_storyboard.getSequences().size()) {
				// if last seq, then add end
				md5State	= null;
				
				// search for md5-state in seqInMockApp to refresh content
				for (CXMLEmt cxmlEmt : seqInMockApp) {
					if (cxmlEmt instanceof CMockState)  {
						if (((CMockState)cxmlEmt).getAttrib("ID").equals(findMD5forID(sequence.getTARGETID()))) {
							md5State = (CMockState)cxmlEmt;
						}
					}
				}
				
				// create new View, if not existing
				if (md5State == null) {
					md5State = new CMockView(findMD5forID(sequence.getTARGETID()), "images/scr_" + sequence.getTARGETID() + ".png", false);
					md5State.addChildNode(new CMockKeyLink("KeyID_End", "_End", EnumKey.HOME));
					seqInMockApp.add(md5State);
				}
				else {
					md5State.addChildNode(new CMockKeyLink("KeyID_End", "_End", EnumKey.HOME));
				}
				
				mockApp.addChildNode(new CMockEnd("_End"));
				
			}
			
			// Update Progressbar
			progress.nextStep();
		}
		
		progress.hide();
		
		//mockApp.addChildNode(seqInMockApp);
		mockApp.setNextRegionID(nNextRegionID);
		m_mockTree.addChildNode(mockApp);
		return m_mockTree.saveToMockjarFile(m_strDiagramName, m_picMap);
	}
	
	/**
	 * find the MD5 to a saved Screenshot-ID
	 * @param strID
	 * @return
	 */
	private String findMD5forID(String strID) {
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
