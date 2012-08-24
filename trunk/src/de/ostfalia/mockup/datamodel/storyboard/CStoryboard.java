/**
 * 
 */
package de.ostfalia.mockup.datamodel.storyboard;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author O. Laudi
 * 1. Create Sequence and Storyboard with
 * 		m_sequence;
 * 		m_storyboard = new CStoryboard();
 * 2. Add Sequence
 * 		m_storyboard.addSequence(seq);
 * 3. If Name is known, refresh ID
 * 		m_storyboard.finish(strName);
 *
 */
public class CStoryboard extends CXMLStoryboardEmt{
	
	protected String						m_strID = "";

	protected HashMap<String, CSequence>	m_mapSequences = null;
	
	/**
	 * @param m_strID
	 */
	public CStoryboard() {
		super();
		this.m_strID = "1";
		this.m_mapSequences = new HashMap<String, CSequence>();
	}
	
	/**
	 * 
	 * @param seq
	 */
	public void addSequence(CSequence seq) {
		// get next String-ID
		if (m_mapSequences != null) {
			int nID = m_mapSequences.size() + 1;
			seq.setID(String.valueOf(nID));
			this.m_mapSequences.put(String.valueOf(nID), seq);
		}
	}
	
	/**
	 * @return the m_mapSequences
	 */
	public HashMap<String, CSequence> getSequences() {
		return m_mapSequences;
	}
	
	/**
	 * 
	 * @return
	 */
	public CSequence getCurrentSequence() {
		if (m_mapSequences != null)
			return m_mapSequences.get(String.valueOf(m_mapSequences.size()));
		else
			return null;
	}
	
	/**
	 * @return the m_strID
	 */
	public String getID() {
		return m_strID;
	}
	
	/**
	 * sets the Storyboardname
	 * @param strID
	 */
	public void setID(String strID) {
		this.m_strID = strID;
	}
	
	/**
	 * sets the Storyboardname
	 * @param strID
	 */
	public void finish(String strID) {
		this.m_strID = strID;
		// save as xml-file
		saveAsXML();
	}
	
	private boolean saveAsXML() {
		boolean			isSuccess 		= true;
		BufferedWriter	bw;
		String			strWritebuffer 	= "<?xml version=\"1.0\" encoding=\"ASCII\"?>\n" + 
										  "<!DOCTYPE  Storyboard SYSTEM \"Storyboard.dtd\">\n";
		
		isSuccess	= new File("Storyboard" + File.separator + 
				this.m_strID).mkdirs();
		
		if (isSuccess) {
			// create storyfile
			File			fStoryFile		= new File("Storyboard" + File.separator + 
					this.m_strID + File.separator + 
						this.m_strID + ".story");
			// copy dtd-file
			copyDTD(new File("Storyboard" + File.separator + 
						this.m_strID + File.separator + 
						"Storyboard.dtd"));
			
			try {
				fStoryFile.createNewFile();
				bw = new BufferedWriter(new FileWriter(fStoryFile), 1024);
				strWritebuffer += this.getXMLString(0);
				
				bw.write(strWritebuffer);
				bw.newLine();
				bw.flush();
			} catch (IOException e) {
				isSuccess = false;
				e.printStackTrace();
			}
		}		
		
		return isSuccess;
	}
	
	/**
	 * copys the dtd-file to a destinationfolder
	 * @param strDestFile
	 */
	private void copyDTD(File fDestFile) {
		File fInputDTD = new File("src" + File.separator + 
								  "de" + File.separator + 
								  "ostfalia" + File.separator + 
								  "xml" + File.separator + 
								  "reader" + File.separator + 
								  "Storyboard.dtd");
		FileReader in;
		FileWriter out;
		try {
			in = new FileReader(fInputDTD);
			out = new FileWriter(fDestFile);
			
			int c;

		    while ((c = in.read()) != -1)
		      out.write(c);

		    in.close();
		    out.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getXMLString(int nIterationDepth) {
		String strSpace 	= "";
		String strResult 	= "";
		
		for (int i = 0; i < nIterationDepth; i++)
			strSpace += m_XMLSPACE;
		
		strResult += strSpace + "<Storyboard id=\"" + this.m_strID + "\">\n";
		
		// add all sequences
		nIterationDepth++;
		for (int nSeqNr = 0; nSeqNr < m_mapSequences.size(); nSeqNr++) {
			strResult += m_mapSequences.get(String.valueOf(nSeqNr + 1)).getXMLString(nIterationDepth);
		}
		
		strResult += strSpace + "</Storyboard>\n";
		
		return strResult;
	}
}
