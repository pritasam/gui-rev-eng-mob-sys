/**
 * 
 */
package de.ostfalia.screenshot.analysis.test;


import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;

import de.ostfalia.screenshot.analysis.CPicAnalyzer;
import junit.framework.*;

/**
 * @author O. Laudi
 *
 */
public class CPicAnalyzerTest extends TestCase {

	// variables
	private CPicAnalyzer 	m_analyze;
	private Rectangle		m_rectButton;
	private Rectangle		m_rectExpect;
	
	// init objects
	public void setUp() {
		m_analyze 		= null;
		m_rectButton	= null;
		m_rectExpect	= null;
	}
	
	// clear objects
	public void tearDown() {
	}
	
	// Einstellen Button
	public void testMenuEinstellenButton() {
		// RegionLink + Region
		m_analyze	= new CPicAnalyzer("src" + File.separator +
				"de" + File.separator + 
				"ostfalia" + File.separator +
				"screenshot" + File.separator +
				"analysis" + File.separator +
				"test" + File.separator +
				   "MenuTestOrig.png");

		m_rectButton = m_analyze.getSurroundedRect(new Point(100, 550));
		m_rectExpect = new Rectangle(0, 500, 480, 100);
		assertEquals(m_rectButton.getLocation(), m_rectExpect.getLocation());
		assertEquals(m_rectButton.getSize(), m_rectExpect.getSize());
	}
	
	// Karte Button
	public void testMenuKarteButton() {
		// RegionLink + Region
		m_analyze	= new CPicAnalyzer("src" + File.separator +
				"de" + File.separator + 
				"ostfalia" + File.separator +
				"screenshot" + File.separator +
				"analysis" + File.separator +
				"test" + File.separator +
				   "MenuTestOrig.png");

		m_rectButton = m_analyze.getSurroundedRect(new Point(50, 750));
		m_rectExpect = new Rectangle(0, 701, 238, 98);
		assertEquals(m_rectButton.getLocation(), m_rectExpect.getLocation());
		assertEquals(m_rectButton.getSize(), m_rectExpect.getSize());
	}
	
	// Details Button
	public void testMenuDetailsButton() {
		// RegionLink + Region
		m_analyze	= new CPicAnalyzer("src" + File.separator +
				"de" + File.separator + 
				"ostfalia" + File.separator +
				"screenshot" + File.separator +
				"analysis" + File.separator +
				"test" + File.separator +
				   "MenuTestOrig.png");

		m_rectButton = m_analyze.getSurroundedRect(new Point(400, 750));
		m_rectExpect = new Rectangle(240, 701, 239, 98);
		assertEquals(m_rectButton.getLocation(), m_rectExpect.getLocation());
		assertEquals(m_rectButton.getSize(), m_rectExpect.getSize());
	}
}
