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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;


/**
 * @author O. Laudi
 *
 */
public class CPreviewGenerator {

	// consts
	protected final int nPICCELLWIDTTH	= 1280;
	protected final int nPICCELLHEIGHT	= 800;
	
	/**
	 * Constructor
	 */
	public CPreviewGenerator() {
		super();
	}

	public BufferedImage getPreviewGraphic(HashMap<String, List<String>> picMap, String strMockupName, String strStartID) {
		BufferedImage biPreview	= null;
		
		BufferedImage bi;
		try {
			bi = ImageIO.read(new FileInputStream("Screenshots/scr_" + strStartID + ".png"));
		
			// Generate preview out of first picture in Map
			biPreview		= new BufferedImage(nPICCELLWIDTTH, nPICCELLHEIGHT, BufferedImage.TYPE_INT_RGB);
			Graphics2D g	= biPreview.createGraphics();
			
			// draw picture
			g.setBackground(Color.LIGHT_GRAY);
			
			if (bi != null) {
				drawPic(bi, g);
			}
			
			// draw MockApp-name
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD, 48));
			g.drawString("MockApp: " + strMockupName, 16, nPICCELLHEIGHT - 64);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return biPreview;
	}
	
//	private Point getPreviewResolution(Point pPicRes, int nPicCount) {
//		int nRatioSum = CDiagramConsts.ASPECT_RATIO_X + CDiagramConsts.ASPECT_RATIO_Y;
//		
//		// get max x and max y number of pics
//		int nMaxX = (int)((Math.sqrt(Integer.valueOf(nPicCount)) * (2.0 / nRatioSum * CDiagramConsts.ASPECT_RATIO_X)) + 0.5);
//		int nMaxY = (int)((Math.sqrt(Integer.valueOf(nPicCount)) * (2.0 / nRatioSum * CDiagramConsts.ASPECT_RATIO_Y)) + 0.5);
//		
//		return new Point(nMaxX, nMaxY);
//	}
//	
//	private Point getPosiOfPic(int nCurrentPic, int nPicCount) {
//		int nRatioSum = CDiagramConsts.ASPECT_RATIO_X + CDiagramConsts.ASPECT_RATIO_Y;
//		
//		// get max x and max y number of pics
//		int nMaxX = (int)((Math.sqrt(Integer.valueOf(nPicCount)) * (2.0 / nRatioSum * CDiagramConsts.ASPECT_RATIO_X)) + 0.5);
//	
//		// get exact position of nCurrentPic
//		int nX = nCurrentPic % nMaxX;
//		int nY = (int)(nCurrentPic / nMaxX);
//		
//		return new Point(nX, nY);
//	}
	
	private void drawPic(BufferedImage biIn, Graphics2D gOut) {
		if (gOut != null) {
			int x = (nPICCELLWIDTTH / 2) - (biIn.getWidth() / 2);
			int y = 0;
			if (biIn != null) {
				// y1/x1 is for iterating biIn
				// y/x is for iterating gOut Graphic
				for (int y1 = 0; y1 < biIn.getHeight(); y1++) {
					for (int x1 = 0; x1 < biIn.getWidth(); x1++) {
						Color px = new Color(biIn.getRGB(x1, y1));
						gOut.setColor(px);
						gOut.fillRect(x, y, 1, 1);
						x++;
					}
					x = (nPICCELLWIDTTH / 2) - (biIn.getWidth() / 2);
					y++;
				}
			}
		}
	}
}
