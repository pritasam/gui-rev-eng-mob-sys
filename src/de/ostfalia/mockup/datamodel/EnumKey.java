/**
 * 
 */
package de.ostfalia.mockup.datamodel;

/**
 * @author O. Laudi
 *
 */
public enum EnumKey {
	BACK (65535),
	HOME (65360),
	MENU (65365),
	SEARCH (65507);
	
	private int value;
	
	private EnumKey(int value) {
		this.value = value;
	}
	
	// gets the int-value of the enum
	public int getValue() {
		return value;
	}
	
	// returns the enum of the enumvalue
	public static EnumKey getEnumByValue(int value) {
		for(EnumKey test : EnumKey.values()) {
			if(test.getValue() == value) {
				return test;
			}
		}
		return null;
	}
	
	// returns the Name of the enumvalue
	public static String getEnumNameByValue(int value) {
		for(EnumKey test : EnumKey.values()) {
			if(test.getValue() == value) {
				return test.name();
			}
		}
		return null;
	}
}
