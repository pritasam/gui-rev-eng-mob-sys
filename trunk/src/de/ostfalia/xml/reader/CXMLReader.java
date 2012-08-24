/**
 * 
 */
package de.ostfalia.xml.reader;

import java.io.File;
import java.io.IOException;

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
				e.printStackTrace();
			} catch (IOException e) {
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
