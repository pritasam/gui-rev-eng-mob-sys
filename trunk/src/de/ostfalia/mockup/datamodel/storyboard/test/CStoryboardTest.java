/**
 * 
 */
package de.ostfalia.mockup.datamodel.storyboard.test;

import de.ostfalia.mockup.datamodel.storyboard.CKey;
import de.ostfalia.mockup.datamodel.storyboard.CSequence;
import de.ostfalia.mockup.datamodel.storyboard.CStoryEvent;
import de.ostfalia.mockup.datamodel.storyboard.CStoryboard;
import de.ostfalia.mockup.datamodel.storyboard.CSwipe;
import de.ostfalia.mockup.datamodel.storyboard.CSwipePoint;
import de.ostfalia.mockup.datamodel.storyboard.CTouch;
import junit.framework.*;

/**
 * @author O. Laudi
 *
 */
public class CStoryboardTest extends TestCase {

	// variables
	private CStoryboard	m_story;
	
	// init objects
	public void setUp() {
		m_story = new CStoryboard(480, 800);
	}
	
	// clear objects
	public void tearDown() {
	}
	
	// create storyboard, create storyfile and compare output
	// seq 1 : start 10, target 20, delay 0, Key
	// seq 2 : start 20, target 30, delay 500, 2x Touch
	// seq 3 : start 30, target 40, delay 1000 (ASYNC), Swipe
	public void testCreateStory() {
		CSequence seq 		= null;
		CStoryEvent event	= null;
		CSwipePoint swpPnt	= null;
		
		// m_story testen
		assertEquals(m_story.getID(), "1");
		assertEquals(m_story.getWidth(), "480");
		assertEquals(m_story.getHeight(), "800");
		assertEquals(m_story.getSequences().size(), 0);
		// Current Sequence
		seq = m_story.getCurrentSequence();
		assertNull(seq);
		
		// create seq 1
		seq = new CSequence("10", "20", "0");
		assertEquals(seq.getSTARTID(), "10");
		assertEquals(seq.getTARGETID(), "20");
		assertEquals(seq.getDELAY(), "0");
		assertEquals(seq.getMapSYNC().size(), 0);
		assertEquals(seq.getMapASYNC().size(), 0);
		
		// Keyevent
		event = new CKey("true", "65535", "210", "0");
		assertEquals(event.getXMLString(0), "<Key id=\"\" pressed=\"true\" keycode=\"65535\" dura=\"210\" delay=\"0\"></Key>\n");
		
		seq.addSyncEvent(event);
		assertEquals(seq.getMapASYNC().size(), 0);
		assertEquals(seq.getMapSYNC().size(), 1);
		assertEquals(event.getXMLString(0), "<Key id=\"1\" pressed=\"true\" keycode=\"65535\" dura=\"210\" delay=\"0\"></Key>\n");
		
		m_story.addSequence(seq);
		assertEquals(m_story.getSequences().size(), 1);
		
		// create seq 2
		seq = new CSequence();
		seq.setSTARTID("20");
		seq.setTARGETID("30");
		seq.setDELAY("500");
		assertEquals(seq.getSTARTID(), "20");
		assertEquals(seq.getTARGETID(), "30");
		assertEquals(seq.getDELAY(), "500");
		assertEquals(seq.getMapSYNC().size(), 0);
		assertEquals(seq.getMapASYNC().size(), 0);
		
		// Touch
		event = new CTouch("1", "50", "100", "50", "100");
		assertEquals(event.getXMLString(0), "<Touch id=\"\" btn=\"1\" x=\"50\" y=\"100\" dura=\"50\" delay=\"100\"></Touch>\n");
		
		seq.addSyncEvent(event);
		assertEquals(seq.getMapSYNC().size(), 1);
		event = new CTouch("2", "150", "200", "250", "200");
		seq.addSyncEvent(event);
		assertEquals(seq.getMapSYNC().size(), 2);
		assertEquals(event.getXMLString(0), "<Touch id=\"2\" btn=\"2\" x=\"150\" y=\"200\" dura=\"250\" delay=\"200\"></Touch>\n");
		
		m_story.addSequence(seq);
		assertEquals(m_story.getSequences().size(), 2);
		
		// create seq 3
		seq = new CSequence("30", "40", "1000");
		
		// SwipeEvent
		event = new CSwipe("3", "1000", "0");
		swpPnt = new CSwipePoint("50", "50");
		assertEquals(swpPnt.getX(), "50");
		assertEquals(swpPnt.getY(), "50");
		((CSwipe)event).addtMapPoint(swpPnt);
		assertEquals(((CSwipe)event).getMapPoints().size(), 1);
		((CSwipe)event).addtMapPoint(new CSwipePoint("60", "40"));
		((CSwipe)event).addtMapPoint(new CSwipePoint("70", "30"));
		assertEquals(((CSwipe)event).getMapPoints().size(), 3);
		seq.addAsyncEvent(event);
		assertEquals(seq.getMapASYNC().size(), 1);
		assertEquals(seq.getMapSYNC().size(), 0);
		assertEquals(event.getXMLString(0), "<Swipe id=\"1\" btn=\"3\" dura=\"1000\" delay=\"0\">\n" +
		   "   <SwipePoint id=\"1\" x=\"50\" y=\"50\"></SwipePoint>\n" +
		   "   <SwipePoint id=\"2\" x=\"60\" y=\"40\"></SwipePoint>\n" +
		   "   <SwipePoint id=\"3\" x=\"70\" y=\"30\"></SwipePoint>\n" +
		   "</Swipe>\n");
		
		m_story.addSequence(seq);
		assertEquals(m_story.getSequences().size(), 3);
		
		// Current Sequence
		seq = m_story.getCurrentSequence();
		assertEquals(seq.getID(), "3");
	}
	
	public void testCreateXMLString() {
		assertNotNull(m_story);
		testCreateStory();
		m_story.finish("test1", 480, 800);
		assertEquals(m_story.getID(), "test1");
	}
}
