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
package de.ostfalia.viewer.inputrecorder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author O.Laudi
 * implements a compare of two images and returns a datastructure
 * which contains the x- and y-position  and also the dimension
 * CImageDelta
 */
public class CImageComparer {
	protected CImageDelta	m_deltaRegion;
	protected BufferedImage	m_biMasterImage;
//	protected BufferedImage	m_biMasterImageDouble;
	protected BufferedImage	m_biCurrent;
	protected boolean		m_isSaved;
	protected boolean		m_isLocked;
	protected boolean		m_isDifferentFromMaster;
	protected long			m_lTimestamp;
	
	/**
	 * Constructor
	 * @param biMasterImage
	 */
	public CImageComparer(BufferedImage	biMasterImage, long lTimestamp) {
		m_deltaRegion 			= null;
		m_biMasterImage			= copyBufferedImage(biMasterImage);
//		m_biMasterImageDouble	= copyBufferedImage(biMasterImage);
		m_isSaved				= false;
		m_isLocked				= false;
		m_isDifferentFromMaster	= false;
		m_lTimestamp			= lTimestamp;
	}
	
	/**
	 * @return the m_lstDeltaRegion
	 */
	public CImageDelta getDeltaRegion() {
		return m_deltaRegion;
	}
	
	/**
	 * @return the m_biMasterImage
	 */
	public BufferedImage getMasterImage() {
		return m_biMasterImage;
	}

	/**
	 * @param m_biMasterImage the m_biMasterImage to set
	 */
	public void setMasterImage(BufferedImage biMasterImage) {
		m_isSaved 					= false;
		m_isLocked					= false;
		m_isDifferentFromMaster		= false;
		this.m_biMasterImage 		= copyBufferedImage(biMasterImage);
//		this.m_biMasterImageDouble	= copyBufferedImage(biMasterImage);
	}
	
	/**
	 * Compares the saved Masterimage with the image which is given in the 
	 * parameterset and stores it in the variable deltaRegion.
	 * isDifferent indicates if the comparison is different or not.
	 * @param biCurrentImage
	 * @return isDifferent
	 */
	public boolean compareMasterWith(BufferedImage biCurrentImage) {
		int nMax_x 			= -1;
		int nMax_y 			= -1;
		int nMin_x 			= Integer.MAX_VALUE;
		int nMin_y 			= Integer.MAX_VALUE;
		boolean isDifferent	= false;
		//m_deltaRegion		= null;
		m_biCurrent = biCurrentImage;
		
		// Check, if image-dimensions are the same
		if ((this.m_biMasterImage.getHeight() == m_biCurrent.getHeight()) &&
				(this.m_biMasterImage.getWidth() == m_biCurrent.getWidth())) {
			for (int j = 0; j < this.m_biMasterImage.getHeight(); j++) {
				for (int i = 0; i < this.m_biMasterImage.getWidth(); i++) {			       	
					try {
						if (this.m_biMasterImage.getRGB(i, j) != m_biCurrent.getRGB(i, j))
						{
							isDifferent = true;
							if (i > nMax_x)
								nMax_x = i;
							if (i < nMin_x)
								nMin_x = i;
	  
							if (j > nMax_y)
								nMax_y = j;
							if (j < nMin_y)
								nMin_y = j;
						}
					}
					catch (Exception e) {
						System.out.println("CImageComparer::compareMasterWith Exception: i = " + i + "; j = " + j);
						e.printStackTrace();
					}
				}
			}
			// save the result in CImageDelta structure
			if (isDifferent) {
				m_isDifferentFromMaster = true;
				nMax_x++;
				nMax_y++;
				m_deltaRegion = new CImageDelta(nMin_x, nMin_y, nMax_x - nMin_x, nMax_y - nMin_y);
				m_deltaRegion.setDeltaImage(m_biCurrent.getSubimage(nMin_x, nMin_y, nMax_x - nMin_x, nMax_y - nMin_y));
//				m_biMasterImageDouble = copyBufferedImage(biCurrentImage);
			}
			
			// if last frame repeat and differs from master then save
			if (!isDifferent && m_isDifferentFromMaster)
				m_isSaved = false;
				
		}
		return isDifferent;
	}
	
	/**
	 * Saves the Master- and deltapic as file
	 */
	public void saveAsPicFiles() {
		if (!m_isSaved && !m_isLocked) {
//			saveMasterAsPicFile();
			saveCurrentAsPicFile();
			m_isSaved = true;
			m_isLocked = true;
			//this.m_biMasterImage = copyBufferedImage(this.m_biMasterImageDouble);
			m_isDifferentFromMaster = false;
		}
	}
	
	/**
	 * Saves the masterpic as file
	 */
	public void saveMasterAsPicFile() {
		try {
		    // retrieve image
		    File outputfile = new File("Screenshots" + File.separator + "scr_" + m_lTimestamp + "_Master.png");
		    ImageIO.write(this.m_biMasterImage, "png", outputfile);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	/**
	 * Saves the currentpic as file
	 */
	public void saveCurrentAsPicFile() {
		try {
		    // retrieve image
		    File outputfile = new File("Screenshots" + File.separator + "scr_" + m_lTimestamp + "_Current.png");
		    ImageIO.write(m_biCurrent, "png", outputfile);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	/**
	 * Copies a BufferedImage
	 * @param source
	 * @return
	 */
	public BufferedImage copyBufferedImage(BufferedImage source) {
		if(source!=null) {
			BufferedImage copy = new BufferedImage(source.getWidth(),
			source.getHeight(),
			source.getType());
			copy.setData(source.getData());
			return copy;
		}
		return null;
	}
}
