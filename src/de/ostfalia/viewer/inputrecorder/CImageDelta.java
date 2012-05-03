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
 * Datastructure
 */
public class CImageDelta {
	protected int			m_nPos_X;
	protected int			m_nPos_Y;
	protected int			m_nWidth;
	protected int			m_nHeight;
	protected BufferedImage m_biDeltaImage;
	
	public CImageDelta(int nPos_X, int nPos_Y, int nWidth, int nHeight) {
		m_nPos_X 		= nPos_X;
		m_nPos_Y 		= nPos_Y;
		m_nWidth 		= nWidth;
		m_nHeight 		= nHeight;
		m_biDeltaImage 	= null;
	}
	
	/**
	 * @return the m_nPos_X
	 */
	public int getPos_X() {
		return m_nPos_X;
	}

	/**
	 * @param m_nPos_X the m_nPos_X to set
	 */
	public void setPos_X(int nPos_X) {
		this.m_nPos_X = nPos_X;
	}

	/**
	 * @return the m_nPos_Y
	 */
	public int getPos_Y() {
		return m_nPos_Y;
	}

	/**
	 * @param m_nPos_Y the m_nPos_Y to set
	 */
	public void setPos_Y(int nPos_Y) {
		this.m_nPos_Y = nPos_Y;
	}

	/**
	 * @return the m_nWidth
	 */
	public int getWidth() {
		return m_nWidth;
	}

	/**
	 * @param m_nWidth the m_nWidth to set
	 */
	public void setWidth(int nWidth) {
		this.m_nWidth = nWidth;
	}

	/**
	 * @return the m_nHeight
	 */
	public int getHeight() {
		return m_nHeight;
	}

	/**
	 * @param m_nHeight the m_nHeight to set
	 */
	public void setHeight(int nHeight) {
		this.m_nHeight = nHeight;
	}

	/**
	 * @return the m_biDeltaImage
	 */
	public BufferedImage getDeltaImage() {
		return m_biDeltaImage;
	}

	/**
	 * @param m_biDeltaImage the m_biDeltaImage to set
	 */
	public void setDeltaImage(BufferedImage biDeltaImage) {
		this.m_biDeltaImage = biDeltaImage;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CImageDelta: posX = " + getPos_X() + "; " + 
				"posY = " + getPos_Y() + "; " +
				"width = " + getWidth() + "; " +
				"heigth = " + getHeight();
	}
	
	public void saveAsPicFile(long lTimestamp) {
		try {
		    // retrieve image
		    File outputfile = new File("Screenshots" + File.separator + "scr_" + lTimestamp + "_Delta.png");
		    ImageIO.write(this.m_biDeltaImage, "png", outputfile);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
}
