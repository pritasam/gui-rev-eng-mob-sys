//    MockVNC-Client, extends the original Tight-VNC-Client from 
//	  http://www.tightvnc.com/ for GUI-Reverseengineering-features
//    for mobile devices.
//    Copyright (C) 2012  Oliver Laudi
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.
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

import de.ostfalia.viewer.logger.CLogger;

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
		// CannyEdge-Filter
		BufferedImage biEdgeImage = processCannyEdgeDetection();
		saveBufferedImage(biEdgeImage, this.m_strImageFile + "canny.png");
		
//		// HoughTransformation
//		BufferedImage biHoughImage = processHoughTransform(biEdgeImage);
//		saveBufferedImage(biHoughImage, this.m_strImageFile + "Hough.png");
		
		// Detect button
		Rectangle outRect = null;
		BufferedImage biHoughImage = null;
		outRect = processButtonDetector(biEdgeImage, pPoint, biHoughImage, this.m_strImageFile);
		//saveBufferedImage(biHoughImage, this.m_strImageFile + "ButtonDetect.png");
		
		return outRect;
	}
	
	/**
	 * returns an image, which shows only edges of the saved SourceImage
	 * @return
	 */
	private BufferedImage processCannyEdgeDetection() {
		// create the detector 
		CannyEdgeDetector detector = new CannyEdgeDetector(); 
		
		// adjust its parameters as desired 
		detector.setLowThreshold(1f); 
		detector.setHighThreshold(3f); 
//		detector.setLowThreshold(3f); 
//		detector.setHighThreshold(6f);
		
		// apply it to an image 
		detector.setSourceImage(this.m_biSourceImage); 
		detector.process(); 
		return detector.getEdgesImage();
	}
	
//	/**
//	 * HoughTransformation Algo
//	 * @param biSource
//	 * @return
//	 */
//	private BufferedImage processHoughTransform(BufferedImage biSource) {
//		// create HoughTransformator
//		HoughTransform hTrans	= new HoughTransform();
//		
//		return hTrans.process(biSource);
//		
////		HoughLines hTrans	= new HoughLines();
////		return hTrans.process(biSource);
//		
//	}
	
	/**
	 * ButtonDetection for a given Point
	 * @param biSource
	 * @param pPoint
	 * @return
	 */
	private Rectangle processButtonDetector(BufferedImage biSource, Point pPoint, BufferedImage biOut, String strImageFile) {
		// create CButtonDetector
		CButtonDetector buttonDetect	= new CButtonDetector();
		
		return buttonDetect.process(biSource, pPoint, biOut, strImageFile);
	}
	
	/**
	 * Saves the given BufferedImage as a file
	 * @param bi
	 * @param strOutputFile
	 */
	private void saveBufferedImage(BufferedImage bi, String strOutputFile) {
		if (bi != null) {
			try {
			    // retrieve image
			    ImageIO.write(bi, "png", new File(strOutputFile));
			} catch (IOException e) {
			    e.printStackTrace();
			}
		}
		else {
			CLogger.getInst(CLogger.SYS_OUT).writeline("CPicAnalyzer::saveBufferedImage: BufferedImage is null.");
		}
	}
}
