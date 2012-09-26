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
package de.ostfalia.xml.reader;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * @author O. Laudi
 *
 */
public abstract class CXMLReader {
	/**
	 * Speichert die zugrundeliegende XML-Datei
	 */
	protected File			m_xmlFile 	= null;
	
	/**
	 * Speichert die Referenz auf die XML-Struktur
	 */
	protected Document		m_xmlDoc 	= null;
	
	/**
	 * Speichert das Rootelement
	 */
	protected Element		m_emtRoot	= null;
	
	/**
	 * Errorhandler für die DTD-Validierung
	 */
	ErrorHandler myErrorHandler = new ErrorHandler()
	{
	    public void fatalError(SAXParseException exception)
	    throws SAXException
	    {
	        System.err.println(this.getClass().toString() + ": FatalError: " + exception);
	    }
	    
	    public void error(SAXParseException exception)
	    throws SAXException
	    {
	        System.err.println(this.getClass().toString() + ": Error: " + exception);
	    }

	    public void warning(SAXParseException exception)
	    throws SAXException
	    {
	        System.err.println(this.getClass().toString() + ": Warning: " + exception);
	    }
	};
	
	/**
	 * Konstruktor
	 * @param xmlFile
	 */
	public CXMLReader(File xmlFile) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(true);
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
			db.setErrorHandler(myErrorHandler);
			try {
				m_xmlDoc = db.parse(xmlFile);
				m_xmlDoc.getDocumentElement().normalize();
				m_emtRoot = m_xmlDoc.getDocumentElement();
			} catch (SAXException e) {
				JOptionPane.showMessageDialog(null, "An Error occured while checking XML-file. Please check the Stacktrace\n\n!" +
						e.getMessage(), "Storyboard.dtd", JOptionPane.WARNING_MESSAGE);
				e.printStackTrace();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "The Storyboard.dtd-File was not found!", 
						"Storyboard.dtd", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * reads the content of the XML File
	 */
	protected abstract void readXMLFile();
}
