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
