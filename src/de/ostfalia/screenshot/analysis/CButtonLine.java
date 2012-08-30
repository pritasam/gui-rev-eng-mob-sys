/**
 * 
 */
package de.ostfalia.screenshot.analysis;

import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * @author O. Laudi
 *
 */
public class CButtonLine {

	// Member
	private Point	m_point1 = null;
	private Point	m_point2 = null;
	
	/**
	 * Constructor
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public CButtonLine(int x1, int y1, int x2, int y2) {
		setLine(x1, y1, x2, y2);
	}
	
	/**
	 * Defaultconstructor
	 */
	public CButtonLine() {
		this.m_point1	= new Point(0, 0);
		this.m_point2	= new Point(0, 0);
	}
	
	/**
	 * 
	 * @return
	 */
	public Point getP1() {
		return this.m_point1;
	}

	/**
	 * 
	 * @return
	 */
	public Point getP2() {
		return this.m_point2;
	}

	/**
	 * 
	 * @return
	 */
	public int getX1() {
		return this.m_point1.x;
	}
	
	/**
	 * 
	 * @param nX1
	 */
	public void setX1(int nX1) {
		this.m_point1.x = nX1;
	}

	/**
	 * 
	 * @return
	 */
	public int getX2() {
		return this.m_point2.x;
	}
	
	/**
	 * 
	 * @param nX2
	 */
	public void setX2(int nX2) {
		this.m_point2.x = nX2;
	}

	/**
	 * 
	 * @return
	 */
	public int getY1() {
		return this.m_point1.y;
	}
	
	/**
	 * 
	 * @param nY1
	 */
	public void setY1(int nY1) {
		this.m_point1.y = nY1;
	}

	/**
	 * 
	 * @return
	 */
	public int getY2() {
		return this.m_point2.y;
	}
	
	/**
	 * 
	 * @param nY2
	 */
	public void setY2(int nY2) {
		this.m_point1.y = nY2;
	}

	/**
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public void setLine(int x1, int y1, int x2, int y2) {
		this.m_point1	= new Point(x1, y1);
		this.m_point2	= new Point(x2, y2);
	}
	
	/** 
     * Draws the line on the image of your choice with the RGB colour of your choice. 
     */ 
    public void draw(BufferedImage image, int color) { 
    	
    	if (this.m_point1.x == this.m_point2.x) {
    		// vertical line
    		if (this.m_point1.y > this.m_point2.y) {
    			// y1-- -> y2
    			for (int y = this.m_point1.y; y >= this.m_point2.y; y--) {
    				image.setRGB(this.m_point1.x, y, color);
    			}
    		}
    		else {
    			// y1++ -> y2
				for (int y = this.m_point1.y; y <= this.m_point2.y; y++) {
					image.setRGB(this.m_point1.x, y, color);
    			}
    		}
    	}
    	else
    	{
    		// Horizontal line
    		if (this.m_point1.x > this.m_point2.x) {
    			// x1-- -> x2
    			for (int x = this.m_point1.x; x >= this.m_point2.x; x--) {
    				image.setRGB(x, this.m_point1.y, color);
    			}
    		}
    		else {
    			// x1++ -> x2
    			for (int x = this.m_point1.x; x <= this.m_point2.x; x++) {
    				image.setRGB(x, this.m_point1.y, color);
    			}
    		}
    	}
    } 

}
