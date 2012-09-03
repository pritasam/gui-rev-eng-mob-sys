/**
 * 
 */
package de.ostfalia.mockup.datamodel.storyboard.test;

import junit.framework.*;

/**
 * @author O. Laudi
 *
 */
public class CStoryboardTest extends TestCase {

	// variables
	private boolean	m_isSuccess;
	
	// init objects
	public void setUp() {
		m_isSuccess = true;
	}
	
	// clear objects
	public void tearDown() {
	}
	
	public void testGetXMLString() {
		assertEquals(true, m_isSuccess);
	}
}
