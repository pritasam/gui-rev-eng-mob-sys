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
