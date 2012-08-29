/**
 * 
 */
package de.ostfalia.screenshot.analysis;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import com.glavsoft.drawing.Renderer;

/**
 * @author O. Laudi
 *
 */
public class CPicAnalyzer {
	
	// Member
	protected String			m_strImageFile 	= "";
	protected BufferedImage		m_biSourceImage	= null;
	
	/**
	 * Constructor
	 * @param strImageFile
	 */
	public CPicAnalyzer(String strImageFile) {
		setImageFile(strImageFile);
	}

	/**
	 * @return the m_renderer
	 */
	public BufferedImage getSourceImage() {
		return m_biSourceImage;
	}
	
	/**
	 * sets the m_biSourceImage with the fileparameter
	 * @param strImageFile
	 */
	public void setImageFile(String strImageFile) {
		try {
			this.m_biSourceImage 	= ImageIO.read(new File(strImageFile));
			this.m_strImageFile		= strImageFile;
		} catch (IOException e) {
			this.m_biSourceImage 	= null;
			this.m_strImageFile		= "";
			e.printStackTrace();
		}
	}

	/**
	 * gets the best-fit and nearest rectangle surrounding a point
	 * @param pPoint
	 * @return
	 */
	public Rectangle getSurroundedRect(Point pPoint) {
		
		BufferedImage biEdgeImage = processCannyEdgeDetection();
		
		saveBufferedImage(biEdgeImage, this.m_strImageFile + "canny.png");
		
		return null;
	}
	
	/**
	 * returns an image, which shows only edges of the saved SourceImage
	 * @return
	 */
	private BufferedImage processCannyEdgeDetection() {
		//create the detector 
		CannyEdgeDetector detector = new CannyEdgeDetector(); 
		
		//adjust its parameters as desired 
//		detector.setLowThreshold(0.5f); 
//		detector.setHighThreshold(1f); 
		detector.setLowThreshold(4f); 
		detector.setHighThreshold(8f);
		
		//apply it to an image 
		detector.setSourceImage(this.m_biSourceImage); 
		detector.process(); 
		return detector.getEdgesImage();
	}
	
	/**
	 * Saves the given BufferedImage as a file
	 * @param bi
	 * @param strOutputFile
	 */
	private void saveBufferedImage(BufferedImage bi, String strOutputFile) {
		try {
		    // retrieve image
		    ImageIO.write(bi, "png", new File(strOutputFile));
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
}
