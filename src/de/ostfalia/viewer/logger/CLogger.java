/**
 * 
 */
package de.ostfalia.viewer.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author O.Laudi
 *
 */
public class CLogger {
	private File				m_file;
	private BufferedWriter		m_bw;
	private static CLogger		m_instance;
	public static final int		SYS_OUT = 0;
	public static final int		FILE = 1;
	private int					m_nOutputMode;
	private String				m_strServerName;
	
	private CLogger() {
		m_nOutputMode = SYS_OUT;
	}
	
	public static CLogger getInst(int nOutputMode) {
		if ( m_instance == null) {
			m_instance = new CLogger();
		}
		m_instance.m_nOutputMode = nOutputMode;
		return m_instance;
	}
		
	public void writeline(String strLine) {
		// Check for Server-Name in ServerInitMessage
		if (strLine.startsWith("ServerInitMessage")) {
			int nStartPos = strLine.indexOf("name: ");
			int nLen = new String("name: ").length();
			nStartPos += nLen;
			int nEndPos = strLine.indexOf(", framebuffer-width:");
			setServerName(strLine.substring(nStartPos, nEndPos));
		}
		
		// Concat timestamp to message
		SimpleDateFormat formatter = new SimpleDateFormat ("yy.MM.dd '-' HH:mm:ss':' ");
		Date currentTime = new Date();
		strLine = formatter.format(currentTime) + strLine;
		
		switch(m_nOutputMode) {
			case SYS_OUT:
				System.out.println(strLine);
				break;
			case FILE:
				if (m_file == null) {
					m_file = new File("VNC_Viewer.log");
					try {
						m_file.createNewFile();
						m_bw = new BufferedWriter(new FileWriter(m_file), 1024);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				try {
					m_bw.write(strLine);
					m_bw.newLine();
					m_bw.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				break;
			default:
				
		}
	}
	
	/**
	 * @return the ServerName
	 */
	public String getServerName() {
		return m_strServerName;
	}

	/**
	 * @param strServerName the ServerName to set
	 */
	private void setServerName(String strServerName) {
		writeline("Set Servername to '" + strServerName + "'");
		this.m_strServerName = strServerName;
	}
}
