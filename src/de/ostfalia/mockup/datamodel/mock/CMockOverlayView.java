/**
 * 
 */
package de.ostfalia.mockup.datamodel.mock;

/**
 * @author O. Laudi
 *
 */
public class CMockOverlayView extends CMockView {

	/**
	 * 
	 * @param strID
	 * @param strImage
	 * @param strOver
	 * @param isImageFullSize
	 * @param isLandscape
	 */
	public CMockOverlayView(String strID, String strImage, String strOver, boolean isImageFullSize, boolean isLandscape) {
		super(strID, strImage, isLandscape);

		// Add attribs
		this.addAttrib("xsi:type", "mock:OverlayView");
		this.addAttrib("imageIsFullSize", isImageFullSize?"true":"false");
		this.addAttrib("over", "#" + strOver);
	}

}
