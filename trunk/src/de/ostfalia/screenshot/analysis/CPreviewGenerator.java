/**
 * 
 */
package de.ostfalia.screenshot.analysis;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import de.ostfalia.mockup.datamodel.diagram.CDiagramConsts;

/**
 * @author O. Laudi
 *
 */
public class CPreviewGenerator {

	// consts
	protected final int nPICCELLWIDTTH	= 200;
	protected final int nPICCELLHEIGHT	= 200;
	/**
	 * 
	 */
	public CPreviewGenerator() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BufferedImage getPreviewGraphic(HashMap<String, List<String>> picMap, String strMockupName) {
		BufferedImage biPreview	= null;
		Point pPicRes			= null;
		if (picMap != null) {
			if (picMap.size() > 0) {
				// get resolution of the first picture
				for (String strMD5 : picMap.keySet()) {
					List<String> lstPics	= picMap.get(strMD5);
					if (lstPics.size() > 0) {
						String strPicName = lstPics.get(0);
						BufferedImage bi;
						try {
							bi = ImageIO.read(new FileInputStream("Screenshots/scr_" + strPicName + ".png"));
							if (bi != null) {
								pPicRes = new Point(bi.getWidth(), bi.getHeight());
								if (pPicRes != null) {
									break;
								}
							}
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				Point pPrevRes	= getPreviewResolution(pPicRes, picMap.size());
				biPreview		= new BufferedImage(pPrevRes.x, pPrevRes.y, BufferedImage.TYPE_INT_RGB);
				Graphics2D g	= biPreview.createGraphics();
				
				for (String strMD5 : picMap.keySet()) {
					List<String> lstPics	= picMap.get(strMD5);
					if (lstPics.size() > 0) {
						String strPicName 	= lstPics.get(0);
//						Point pPosiOfPic 	= getPosiOfPic()
					}
				}
				
				for (int y = 0; y < pPrevRes.y; y++) {
					for (int x = 0; x < pPrevRes.x; x++) {
						drawPic(x, y, strMockupName, g);
					}
				}
				
//				g.setBackground(Color.LIGHT_GRAY);
//				//g.setColor(Color.BLACK);
//				g.setFont(new Font("Arial", Font.BOLD, 32));
//				g.drawString(strMockupName, 15, pPrevRes.y / 2);
			}
		}
		
		return biPreview;
	}
	
	private Point getPreviewResolution(Point pPicRes, int nPicCount) {
		int nRatioSum = CDiagramConsts.ASPECT_RATIO_X + CDiagramConsts.ASPECT_RATIO_Y;
		
		// get max x and max y number of pics
		int nMaxX = (int)((Math.sqrt(Integer.valueOf(nPicCount)) * (2.0 / nRatioSum * CDiagramConsts.ASPECT_RATIO_X)) + 0.5);
		int nMaxY = (int)((Math.sqrt(Integer.valueOf(nPicCount)) * (2.0 / nRatioSum * CDiagramConsts.ASPECT_RATIO_Y)) + 0.5);
		
		return new Point(nMaxX, nMaxY);
	}
	
	private Point getPosiOfPic(int nCurrentPic, int nPicCount) {
		int nRatioSum = CDiagramConsts.ASPECT_RATIO_X + CDiagramConsts.ASPECT_RATIO_Y;
		
		// get max x and max y number of pics
		int nMaxX = (int)((Math.sqrt(Integer.valueOf(nPicCount)) * (2.0 / nRatioSum * CDiagramConsts.ASPECT_RATIO_X)) + 0.5);
	
		// get exact position of nCurrentPic
		int nX = nCurrentPic % nMaxX;
		int nY = (int)(nCurrentPic / nMaxX);
		
		return new Point(nX, nY);
	}
	
	private void drawPic(int x, int y, String strPicName, Graphics2D gOut) {
		if (gOut != null) {
			x = (x * nPICCELLWIDTTH) + (nPICCELLWIDTTH / 2);
			y = (y * nPICCELLHEIGHT) + (nPICCELLHEIGHT / 2);
			BufferedImage bi;
			try {
				bi = ImageIO.read(new FileInputStream("Screenshots/scr_" + strPicName + ".png"));
				if (bi != null) {
					// y1/x1 is for iterating bufferedimage
					// y/x is for iterating gOut Graphic
					for (int y1 = 0; y1 < bi.getHeight(); y1 = y1 + (bi.getHeight() / (nPICCELLHEIGHT / 2))) {
						for (int x1 = 0; x1 < bi.getHeight(); x1 = x1 + (bi.getHeight() / (nPICCELLWIDTTH / 2))) {
							Color px = new Color(bi.getRGB(x, y));
							gOut.setColor(px);
							gOut.fillRect(x, y, 1, 1);
							x++;
						}
						x = (x * nPICCELLWIDTTH) + (nPICCELLWIDTTH / 2);
						y++;
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
