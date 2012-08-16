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
 * @author Hans
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
