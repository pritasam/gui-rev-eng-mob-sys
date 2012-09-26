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
package de.ostfalia.viewer.inputrecorder;

import javax.swing.JOptionPane;

import com.glavsoft.rfb.ClipboardController;
import com.glavsoft.rfb.IRepaintController;
import com.glavsoft.rfb.ISessionController;
import com.glavsoft.rfb.encoding.decoder.DecodersContainer;
import com.glavsoft.rfb.protocol.*;
import com.glavsoft.transport.Reader;

import de.ostfalia.viewer.logger.CLogger;

/**
 * @author O. Laudi
 *
 */
public class CMasterReceiverTask extends ReceiverTask {
	private EReceiverState		m_eState; //0=test 1=Filter 2=normal
	private final double 		m_dEpsilon = 0.0;
	private CVNCServerTester 	m_stTester;
	private CVNCServerFilter 	m_sfFilter;
	
	public CMasterReceiverTask(Reader reader,
			IRepaintController repaintController,
			ClipboardController clipboardController,
			ISessionController sessionManager, DecodersContainer decoders,
			ProtocolContext context) {
		super(reader, repaintController, clipboardController, sessionManager, decoders,
				context);
		m_eState 	= EReceiverState.TEST_RECEIVERTASK;
		m_stTester 	= new CVNCServerTester(reader, repaintController, clipboardController, sessionManager, decoders, context);
	}

	/**
	 * new run()-method
	 */
	@Override
	public void run() {
		if (m_eState == EReceiverState.TEST_RECEIVERTASK) {
			
			// User must show a static image
			JOptionPane.showMessageDialog(null, "Please make sure to show a static screencontent on your device.", "Initialize device", JOptionPane.INFORMATION_MESSAGE);

			
			m_stTester.run();

			CLogger.getInst(CLogger.SYS_OUT).writeline("tester.getEqualRatio() = " + m_stTester.getEqualRatio());
			if(m_stTester.getEqualRatio() > m_dEpsilon){
				m_eState = EReceiverState.FILTERED_RECEIVERTASK;
			} else {
				m_eState = EReceiverState.DEFAULT_RECEIVERTASK;
			}
			JOptionPane.showMessageDialog(null, "State " + m_eState + " was detected.", "Initialization complete!", JOptionPane.INFORMATION_MESSAGE);
			CLogger.getInst(CLogger.SYS_OUT).writeline("State = " + m_eState);

		}
		if (m_eState == EReceiverState.FILTERED_RECEIVERTASK) {
			if (m_sfFilter == null)
				m_sfFilter = new CVNCServerFilter(reader, repaintController, clipboardController, sessionManager, decoders, context);
			m_sfFilter.run();
		} else if (m_eState == EReceiverState.DEFAULT_RECEIVERTASK) {
			///TODO : Warum geht Filter, aber normal nicht???
//			super.run();
			if (m_sfFilter == null)
				m_sfFilter = new CVNCServerFilter(reader, repaintController, clipboardController, sessionManager, decoders, context);
			m_sfFilter.run();
		}
	}

}
