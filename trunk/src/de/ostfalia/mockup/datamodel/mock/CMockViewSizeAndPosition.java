/**
 * 
 */
package de.ostfalia.mockup.datamodel.mock;

/**
 * @author O. Laudi
 *
 */
public class CMockViewSizeAndPosition extends CMockRectangle {

	/**
	 * @param strID
	 * @param nHeight
	 * @param nWidth
	 */
	public CMockViewSizeAndPosition(String strID, Integer nHeight,
			Integer nWidth, Integer x, Integer y) {
		super(strID, nHeight, nWidth);

		// Add attribs
		this.addAttrib("x", x.toString());
		this.addAttrib("y", y.toString());
	}

}
