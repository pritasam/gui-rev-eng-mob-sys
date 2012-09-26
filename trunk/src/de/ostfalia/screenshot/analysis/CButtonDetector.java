/**
 * 
 */
package de.ostfalia.screenshot.analysis;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * @author O. Laudi
 *
 */
public class CButtonDetector {
	
	// statics
	private final static int	MIN_BUTTON_SIZE		= 35;	// Min. Buttonsize = MIN_BUTTON_SIZE x MIN_BUTTON_SIZE
	private final static int	MAX_PIXEL_DEVIATION	= 1;	// Pixel to look right and left from rootline
	private final static int	ORIENTATION_LEFT	= 1;
	private final static int	ORIENTATION_RIGHT	= 2;
	private final static int	ORIENTATION_TOP		= 3;
	private final static int	ORIENTATION_BOTTOM	= 4;
	
	// Member
	private CButtonLine	m_leftBorder	= null;
	private CButtonLine	m_rightBorder	= null;
	private CButtonLine	m_topBorder		= null;
	private CButtonLine	m_bottomBorder	= null;
	

	/**
	 * Default constructor
	 */
	public CButtonDetector() {
		
	}
	
	/**
	 * Input: 	- biSource: binary BufferedImage with detected Edges in black and white (created with CannyEdgeDetector)
	 * 			- pPoint: position from which the algo starts to find a button rectangle
	 * 
	 * Output:	- biOutput: Inputimage with red line 
	 * 
	 * @param biSource
	 * @return
	 */
	public Rectangle process(BufferedImage biSource, Point pPoint, BufferedImage biOutput) {
		// load the file using Java's imageIO library 
        biOutput	= new BufferedImage(biSource.getColorModel(),
        								biSource.copyData(biSource.getRaster()),
						        		biSource.getColorModel().isAlphaPremultiplied(), 
						        		null);
        
        // Left
        m_leftBorder 	= getLine(ORIENTATION_LEFT, pPoint, biOutput);
        
        // Right
        m_rightBorder 	= getLine(ORIENTATION_RIGHT, pPoint, biOutput);
        
        // Top
        m_topBorder 	= getLine(ORIENTATION_TOP, pPoint, biOutput);
        
        // Bottom
        m_bottomBorder 	= getLine(ORIENTATION_BOTTOM, pPoint, biOutput);
        
        // calc rectangle
        Rectangle outRect = getProcessedRectangle(biOutput);
        // draw rectangle on picture
        drawRectOnBI(biOutput, outRect);
        
        return outRect;
	}
	
	/**
	 * 
	 * @param nORIENTATION
	 * @param pPoint
	 * @param biOutput
	 * @return
	 */
	private CButtonLine getLine(int nORIENTATION, Point pPoint, BufferedImage biOutput) {
		/**             A
		 *              | nLeftMulti
		 *              |
		 * O----------->O
		 * nRootMulti   |
		 *              | nRightMulti
		 *              V
		 */
		int nRootMulti	= 1;
		int nLeftMulti	= -1;
		int nRightMulti	= 1;
		
		switch (nORIENTATION) {
		case ORIENTATION_LEFT:
			nRootMulti	= -1;
			nLeftMulti	= 1;
			nRightMulti	= -1;
			break;
		case ORIENTATION_RIGHT:
			nRootMulti	= 1;
			nLeftMulti	= -1;
			nRightMulti	= 1;
			break;
		case ORIENTATION_TOP:
			nRootMulti	= -1;
			nLeftMulti	= -1;
			nRightMulti	= 1;
			break;
		case ORIENTATION_BOTTOM:
			nRootMulti	= 1;
			nLeftMulti	= 1;
			nRightMulti	= -1;
			break;
		default:
			break;
		}
		
		return detectLine(nORIENTATION, nRootMulti, nLeftMulti, nRightMulti, pPoint, biOutput);
	}
	
	/**
	 * processes the algo to find a line
	 * @param nORIENTATION
	 * @param nRootMulti
	 * @param nLeftMulti
	 * @param nRightMulti
	 * @param pPoint
	 * @param biOutput
	 * @return
	 */
	private CButtonLine detectLine(int nORIENTATION, 
								   int nRootMulti, 
								   int nLeftMulti, 
								   int nRightMulti,
								   Point pPoint, 
								   BufferedImage biOutput) {
		CButtonLine line = new CButtonLine();
		int nRootIterator 	= 0;
		int nLeftIterator	= 0;
		int nRightIterator	= 0;
		
		/**             A
		 *              | nLeftMulti
		 *              |
		 * O----------->O
		 * nRootMulti   |
		 *              | nRightMulti
		 *              V
		 */

		switch (nORIENTATION) {
		case ORIENTATION_LEFT:
		case ORIENTATION_RIGHT:
			nRootIterator = pPoint.x;
			// iterate aslong no line is found or the imageborders are not reached
			while ((!isLineFound(line)) && ((nRootIterator > (0 + MAX_PIXEL_DEVIATION)) && (nRootIterator < (biOutput.getWidth() - MAX_PIXEL_DEVIATION)))) {
				// iterate from root til next white pixel
				nRootIterator = nRootIterator + (1 * nRootMulti);
				
				if (biOutput.getRGB(nRootIterator, pPoint.y) == Color.WHITE.getRGB()) {
					// iterate left way til end
					nLeftIterator = pPoint.y;
					do {
						nLeftIterator = nLeftIterator + (1 * nLeftMulti);
					} while ((isWhitePixelDeviation(nORIENTATION, new Point(nRootIterator, nLeftIterator), biOutput)));
					
					// iterate right way til end
					nRightIterator = pPoint.y;
					do {
						nRightIterator = nRightIterator + (1 * nRightMulti);
					} while ((isWhitePixelDeviation(nORIENTATION, new Point(nRootIterator, nRightIterator), biOutput)));
					
					// save in line
					line.setLine(nRootIterator, nLeftIterator, nRootIterator, nRightIterator);
				}
			}
			break;
		case ORIENTATION_TOP:
		case ORIENTATION_BOTTOM:
			nRootIterator = pPoint.y;
			// iterate aslong no line is found or the imageborders are not reached
			while ((!isLineFound(line)) && ((nRootIterator > (0 + MAX_PIXEL_DEVIATION)) && (nRootIterator < (biOutput.getHeight() - MAX_PIXEL_DEVIATION)))) {
				// iterate from root til next white pixel
				nRootIterator = nRootIterator + (1 * nRootMulti);
				
				if (biOutput.getRGB(pPoint.x, nRootIterator) == Color.WHITE.getRGB()) {
					// iterate left way til end
					nLeftIterator = pPoint.x;
					do {
						nLeftIterator = nLeftIterator + (1 * nLeftMulti);
					} while ((isWhitePixelDeviation(nORIENTATION, new Point(nLeftIterator, nRootIterator), biOutput)));
					
					// iterate right way til end
					nRightIterator = pPoint.x;
					do {
						nRightIterator = nRightIterator + (1 * nRightMulti);
					} while ((isWhitePixelDeviation(nORIENTATION, new Point(nRightIterator, nRootIterator), biOutput)));
					
					// save in line
					line.setLine(nLeftIterator, nRootIterator, nRightIterator, nRootIterator);
				}
			}		
		default:
			break;
		}
		
		if (!isLineFound(line)) {
			line = new CButtonLine();
		}
		
		return line;
	}
	
	/**
	 * returns true, if the linelenght >= MAX_PIXEL_DEVIATION
	 * @param bLine
	 * @return
	 */
	private boolean isLineFound(CButtonLine bLine) {
		boolean isFound = false;
		
		if (bLine.getX1() == bLine.getX2()) {
    		// vertical line
    		if (Math.abs(bLine.getY1() - bLine.getY2()) >= MIN_BUTTON_SIZE) {
    			isFound = true;
    		}
    		else {
    			isFound = false;
    		}
    	}
    	else
    	{
    		// horizontal line
    		if (Math.abs(bLine.getX1() - bLine.getX2()) >= MIN_BUTTON_SIZE) {
    			isFound = true;
    		}
    		else {
    			isFound = false;
    		}
    	}
		
		return isFound;
	}
	
	/**
	 * checks if a white pixel exists in the MAX_Deviation at the left and right way
	 * @param nORIENTATION
	 * @param pPoint
	 * @param biOutput
	 * @return
	 */
	private boolean isWhitePixelDeviation(int nORIENTATION, 
									      Point pPoint, 
									      BufferedImage biOutput) {
		boolean	isFound = false;
		
		switch (nORIENTATION) {
		case ORIENTATION_LEFT:
		case ORIENTATION_RIGHT:
			// horizontal check
			for (int nCnt = 0; nCnt <= MAX_PIXEL_DEVIATION; nCnt++) {
				if ((biOutput.getRGB(pPoint.x + nCnt, pPoint.y) == Color.WHITE.getRGB()) ||
					(biOutput.getRGB(pPoint.x - nCnt, pPoint.y) == Color.WHITE.getRGB())) {
					isFound = true;
					nCnt = MAX_PIXEL_DEVIATION + 1;
				}
			}
			break;
		case ORIENTATION_TOP:
		case ORIENTATION_BOTTOM:
			// vertical check
			for (int nCnt = 0; nCnt <= MAX_PIXEL_DEVIATION; nCnt++) {
				if ((biOutput.getRGB(pPoint.x, pPoint.y + nCnt) == Color.WHITE.getRGB()) ||
					(biOutput.getRGB(pPoint.x, pPoint.y - nCnt) == Color.WHITE.getRGB())) {
					isFound = true;
					nCnt = MAX_PIXEL_DEVIATION + 1;
				}
			}
		default:
			break;
		}
		
		return isFound;
	}
	
	/**
	 * gets the inner rect from the 4 processed Line2Ds
	 * @return
	 */
	public Rectangle getProcessedRectangle(BufferedImage biOutput) {
		
		// Check for Point(0, 0) and set them to imageboundaries
		if ((m_leftBorder.getX1() == 0) && (m_leftBorder.getX2() == 0) &&
			(m_leftBorder.getY1() == 0) && (m_leftBorder.getY2() == 0)) {
			m_leftBorder.setLine(0, 0, 0, biOutput.getHeight() - 1);
		}
		if ((m_rightBorder.getX1() == 0) && (m_rightBorder.getX2() == 0) &&
			(m_rightBorder.getY1() == 0) && (m_rightBorder.getY2() == 0)) {
			m_rightBorder.setLine(biOutput.getWidth() - 1, 0, biOutput.getWidth() - 1, biOutput.getHeight() - 1);
		}
		if ((m_topBorder.getX1() == 0) && (m_topBorder.getX2() == 0) &&
			(m_topBorder.getY1() == 0) && (m_topBorder.getY2() == 0)) {
			m_topBorder.setLine(0, 0, biOutput.getWidth() - 1, 0);
		}
		if ((m_bottomBorder.getX1() == 0) && (m_bottomBorder.getX2() == 0) &&
			(m_bottomBorder.getY1() == 0) && (m_bottomBorder.getY2() == 0)) {
			m_bottomBorder.setLine(0, biOutput.getHeight() - 1, biOutput.getWidth() - 1, biOutput.getHeight() - 1);
		}
		
		return new Rectangle(m_leftBorder.getX1(), 
							 m_topBorder.getY1(), 
							 Math.abs(m_leftBorder.getX1() - m_rightBorder.getX1()), 
							 Math.abs(m_topBorder.getY1() - m_bottomBorder.getY1()));
	}
	
	/**
	 * draws a rectangle on the image given in the parameterlist
	 * @param biOutput
	 * @param rect
	 */
	private void drawRectOnBI(BufferedImage biOutput, Rectangle rect) {
		// left + right
		for (int y = rect.y; y < (rect.y + rect.height); y++) {
			// left
			biOutput.setRGB(rect.x, y, Color.RED.getRGB());
			// right
			biOutput.setRGB(rect.x + rect.width, y, Color.RED.getRGB());
		}
		
		// top + bot
		for (int x = rect.x; x < (rect.x + rect.width); x++) {
			// top
			biOutput.setRGB(x, rect.y, Color.RED.getRGB());
			// bot
			biOutput.setRGB(x, rect.y + rect.height, Color.RED.getRGB());
		}
		
//		m_leftBorder.draw(biOutput, Color.RED.getRGB());
//		m_rightBorder.draw(biOutput, Color.RED.getRGB());
//		m_topBorder.draw(biOutput, Color.RED.getRGB());
//		m_bottomBorder.draw(biOutput, Color.RED.getRGB());
		
	}
}
