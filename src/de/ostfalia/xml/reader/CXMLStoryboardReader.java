/**
 * 
 */
package de.ostfalia.xml.reader;

import java.io.File;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.ostfalia.mockup.datamodel.storyboard.CKey;
import de.ostfalia.mockup.datamodel.storyboard.CSequence;
import de.ostfalia.mockup.datamodel.storyboard.CStoryboard;
import de.ostfalia.mockup.datamodel.storyboard.CSwipe;
import de.ostfalia.mockup.datamodel.storyboard.CSwipePoint;
import de.ostfalia.mockup.datamodel.storyboard.CTouch;

/**
 * @author O. Laudi
 *
 */
public class CXMLStoryboardReader extends CXMLReader {

	// Saves the storyboard from the XML file
	private CStoryboard				m_storyboard;
	private CSequence				m_sequence;
	
	public CXMLStoryboardReader(File xmlFile) {
		super(xmlFile);
		this.m_storyboard = null;
		this.m_sequence = null;
		readXMLFile();
	}

	/**
	 * @return the m_storyboard
	 */
	public CStoryboard getStoryboard() {
		return m_storyboard;
	}

	@Override
	protected void readXMLFile() {
		this.m_storyboard 			= null;
		this.m_sequence 			= null;
		NamedNodeMap attrMap 		= null;
		NodeList nodeLstRoot 		= null;
		NodeList nodeLstSequences 	= null;
		NodeList nodeLstASyncs		= null;
		NodeList nodeLstASyncsChild	= null;
		NodeList nodeSwipePoints	= null;
		
		// Storyboard
		nodeLstRoot = m_xmlDoc.getElementsByTagName(m_emtRoot.getNodeName());
		for (int nEmtRoot = 0; nEmtRoot < nodeLstRoot.getLength(); nEmtRoot++) {
			Node nodeEmtRoot = nodeLstRoot.item(nEmtRoot);
			// get attributes
			attrMap = nodeEmtRoot.getAttributes();
			this.m_storyboard = new CStoryboard(0, 0);
			this.m_storyboard.setID(attrMap.getNamedItem("id").getNodeValue());
			this.m_storyboard.setWidth(attrMap.getNamedItem("width").getNodeValue());
			this.m_storyboard.setHeight(attrMap.getNamedItem("height").getNodeValue());
			
			// List of Sequences
			nodeLstSequences = m_xmlDoc.getElementsByTagName("Sequence");
			for (int nSeq = 0; nSeq < nodeLstSequences.getLength(); nSeq++) {
				Node nodeEmtSeq = nodeLstSequences.item(nSeq);
				// get attributes
				attrMap = nodeEmtSeq.getAttributes();
				this.m_sequence = new CSequence(attrMap.getNamedItem("startID").getNodeValue(), 
												attrMap.getNamedItem("targetID").getNodeValue(), 
												attrMap.getNamedItem("delay").getNodeValue());
				
				nodeLstASyncs = nodeEmtSeq.getChildNodes();
				for (int nASyncs = 0; nASyncs < nodeLstASyncs.getLength(); nASyncs++) {
					Node nodeEmtASyncs = nodeLstASyncs.item(nASyncs);
					
					// only level1 Childs (<...>)
					if (nodeEmtASyncs.getNodeType() == 1) {
						if (nodeEmtASyncs.getNodeName() == "Async") {
							// read async events
							nodeLstASyncsChild = nodeEmtASyncs.getChildNodes();
							for (int nASyncChilds = 0; nASyncChilds < nodeLstASyncsChild.getLength(); nASyncChilds++) {
								Node nodeEmtAsync = nodeLstASyncsChild.item(nASyncChilds);
								
								// only level1 Childs (<...>)
								if (nodeEmtAsync.getNodeType() == 1) {
									// get attributes
									attrMap = nodeEmtAsync.getAttributes();
									if (nodeEmtAsync.getNodeName() == "Key") {
										this.m_sequence.addAsyncEvent(new CKey(attrMap.getNamedItem("pressed").getNodeValue(),
																			   attrMap.getNamedItem("keycode").getNodeValue(), 
																			   attrMap.getNamedItem("dura").getNodeValue(), 
																			   attrMap.getNamedItem("delay").getNodeValue()));
									}
									else if (nodeEmtAsync.getNodeName() == "Touch") {
										this.m_sequence.addAsyncEvent(new CTouch(attrMap.getNamedItem("btn").getNodeValue(),
																			   attrMap.getNamedItem("x").getNodeValue(), 
																			   attrMap.getNamedItem("y").getNodeValue(), 
																			   attrMap.getNamedItem("dura").getNodeValue(),
																			   attrMap.getNamedItem("delay").getNodeValue()));
									}
									else if (nodeEmtAsync.getNodeName() == "Swipe") {
										CSwipe swp = new CSwipe(attrMap.getNamedItem("btn").getNodeValue(),
											     attrMap.getNamedItem("dura").getNodeValue(),
											     attrMap.getNamedItem("delay").getNodeValue());
										
										// get CSwipePoints
										nodeSwipePoints = nodeEmtAsync.getChildNodes();
										for (int nSwipePoints = 0; nSwipePoints < nodeSwipePoints.getLength(); nSwipePoints++) {
											Node nodeEmtSwipePoint = nodeSwipePoints.item(nSwipePoints);
											
											// only level1 Childs (<...>)
											if (nodeEmtSwipePoint.getNodeType() == 1) {
												// get attributes
												attrMap = nodeEmtSwipePoint.getAttributes();
												swp.addtMapPoint(new CSwipePoint(attrMap.getNamedItem("x").getNodeValue(), 
																			     attrMap.getNamedItem("y").getNodeValue()));
											}
										}
										
										this.m_sequence.addAsyncEvent(swp);
									}
								}
							}
						}
						else if (nodeEmtASyncs.getNodeName() == "Sync") {
							// read sync events
							nodeLstASyncsChild = nodeEmtASyncs.getChildNodes();
							for (int nSyncChilds = 0; nSyncChilds < nodeLstASyncsChild.getLength(); nSyncChilds++) {
								Node nodeEmtSync = nodeLstASyncsChild.item(nSyncChilds);
								
								// only level1 Childs (<...>)
								if (nodeEmtSync.getNodeType() == 1) {
									// get attributes
									attrMap = nodeEmtSync.getAttributes();
									if (nodeEmtSync.getNodeName() == "Key") {
										this.m_sequence.addSyncEvent(new CKey(attrMap.getNamedItem("pressed").getNodeValue(),
																			  attrMap.getNamedItem("keycode").getNodeValue(), 
																			  attrMap.getNamedItem("dura").getNodeValue(), 
																			  attrMap.getNamedItem("delay").getNodeValue()));
									}
									else if (nodeEmtSync.getNodeName() == "Touch") {
										this.m_sequence.addSyncEvent(new CTouch(attrMap.getNamedItem("btn").getNodeValue(),
																			    attrMap.getNamedItem("x").getNodeValue(), 
																			    attrMap.getNamedItem("y").getNodeValue(), 
																			    attrMap.getNamedItem("dura").getNodeValue(),
																			    attrMap.getNamedItem("delay").getNodeValue()));
									}
									else if (nodeEmtSync.getNodeName() == "Swipe") {
										CSwipe swp = new CSwipe(attrMap.getNamedItem("btn").getNodeValue(),
											     attrMap.getNamedItem("dura").getNodeValue(),
											     attrMap.getNamedItem("delay").getNodeValue());
										
										// get CSwipePoints
										nodeSwipePoints = nodeEmtSync.getChildNodes();
										for (int nSwipePoints = 0; nSwipePoints < nodeSwipePoints.getLength(); nSwipePoints++) {
											Node nodeEmtSwipePoint = nodeSwipePoints.item(nSwipePoints);
											
											// only level1 Childs (<...>)
											if (nodeEmtSwipePoint.getNodeType() == 1) {
												// get attributes
												attrMap = nodeEmtSwipePoint.getAttributes();
												swp.addtMapPoint(new CSwipePoint(attrMap.getNamedItem("x").getNodeValue(), 
																			     attrMap.getNamedItem("y").getNodeValue()));
											}
										}
										
										this.m_sequence.addSyncEvent(swp);
									}
								}
							}
						}
					}					
				}
				
				// add sequence to storyboard
				this.m_storyboard.addSequence(this.m_sequence);
			}
		}
	}
	
	

}
