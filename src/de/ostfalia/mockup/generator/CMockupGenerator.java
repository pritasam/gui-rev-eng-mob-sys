/**
 * 
 */
package de.ostfalia.mockup.generator;

import java.io.File;

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

/**
 * @author O. Laudi
 *
 */
public class CMockupGenerator {
	private String		m_strDiagramName;
	private CXMLEmt		m_mockTree;
	private CXMLEmt		m_diagramTree;
	
	public CMockupGenerator(String strDiagramName) {
		m_strDiagramName	= strDiagramName;
		m_mockTree 			= null;
		m_diagramTree		= null;
	}
	
	/**
	 * Testmethod to test the mock-model-generator
	 */
	public void testMockModel() {
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
		m_mockTree.saveToFile(new File("DiagMock" + File.separator + m_strDiagramName + ".mock"));
	}
}
