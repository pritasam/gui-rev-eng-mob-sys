/**
 * 
 */
package de.ostfalia.viewer.inputrecorder;

import java.awt.TrayIcon.MessageType;

import javax.swing.JOptionPane;

import com.glavsoft.rfb.ClipboardController;
import com.glavsoft.rfb.IRepaintController;
import com.glavsoft.rfb.ISessionController;
import com.glavsoft.rfb.encoding.decoder.DecodersContainer;
import com.glavsoft.rfb.protocol.ProtocolContext;
import com.glavsoft.rfb.protocol.ReceiverTask;
import com.glavsoft.transport.Reader;

/**
 * @author O.Laudi
 * This class checks, if the vnc-server broadcasts the imagedata
 * or only sends deltaimages. For every case will be instanciated 
 * a specific class
 * - Server sends only delta: ReceiverTast.java (com.glavsoft.rfb.protocol)
 * - Server broadcasts images: CVNCServerFilter.java (de.ostfalia.viewer.inputrecorder)
 *
 */
public class CReceiverTaskProvider {
	private static CReceiverTaskProvider	m_instance;
	private ReceiverTask 					m_receiverTask;
	
	/**
	 * Constructor
	 */
	private CReceiverTaskProvider() {
		m_receiverTask = null;
	}
	
	/**
	 * Singleton-accessor-method
	 * @return
	 */
	public static CReceiverTaskProvider getInst() {
		if ( m_instance == null) {
			m_instance 	= new CReceiverTaskProvider();
		}
		return m_instance;	
	}
	
	/**
	 * returns a choosen instance of the ReceiverTask, depends on the
	 * servers behavior
	 * @param reader
	 * @param repaintController
	 * @param clipboardController
	 * @param sessionManager
	 * @param decoders
	 * @param context
	 * @return
	 */
	public ReceiverTask getReceiverTask(Reader reader,
			IRepaintController repaintController, ClipboardController clipboardController,
			ISessionController sessionManager, DecodersContainer decoders,
			ProtocolContext context) {
		if (m_receiverTask == null) {
			initReceiverTaskProvider(reader, repaintController, clipboardController, sessionManager, decoders, context);
		}
		return m_receiverTask;
	}
	
	/**
	 * Checks the data received by the vnc-server and decides either to use
	 * the default ReceiverTask.java for correct sending servers or the modified
	 * CVNCServerFilter.java for broadcasting servers.
	 */
	private void initReceiverTaskProvider(Reader reader,
			IRepaintController repaintController, ClipboardController clipboardController,
			ISessionController sessionManager, DecodersContainer decoders,
			ProtocolContext context) {
		
		m_receiverTask = new CVNCServerTester(
			reader, repaintController,
			clipboardController, sessionManager,
			decoders, context);
		
		// User must show a static image
		JOptionPane.showMessageDialog(null, "Please make sure to show a static screencontent on your device.", "Initialize device", 1);
		
		// get images from server for 2 seconds and compare
		new Thread(m_receiverTask).start();
		long lCurTime = System.currentTimeMillis();
		while ((System.currentTimeMillis() - lCurTime) < 2000) {
			
		}
		synchronized (this) {
			m_receiverTask.stopTask();
		}
		
		if (((CVNCServerTester)m_receiverTask).getEqualRatio() > 0.0) {
			// images equal --> return CVNCServerFilter
			//m_receiverTask = null;
			m_receiverTask = new CVNCServerFilter(m_receiverTask);
//			m_receiverTask = new CVNCServerFilter(
//					reader, repaintController,
//					clipboardController, sessionManager,
//					decoders, context);
		}
		else
		{
			// images different --> return ReceiverTask
			//m_receiverTask = null;
			m_receiverTask = new ReceiverTask(m_receiverTask);
//			m_receiverTask = new ReceiverTask(
//					reader, repaintController,
//					clipboardController, sessionManager,
//					decoders, context);
		}
	}
}
