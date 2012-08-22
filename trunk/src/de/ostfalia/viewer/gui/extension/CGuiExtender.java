/**
 * 
 */
package de.ostfalia.viewer.gui.extension;

import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import com.glavsoft.rfb.protocol.Protocol;
import com.glavsoft.rfb.protocol.ProtocolContext;
import com.glavsoft.viewer.swing.Surface;
import com.glavsoft.viewer.swing.Utils;

import de.ostfalia.mockup.generator.CMockupGenerator;
import de.ostfalia.viewer.inputrecorder.CInputRecorder;

/**
 * @author O.Laudi
 *
 */
public abstract class CGuiExtender {
	protected ProtocolContext 			m_Protocol;
	protected Container 				m_container;
	protected Protocol 					m_workingProtocol;
	protected List<JComponent>			m_kbdButtons;
	protected Surface 					m_surface;
	
	/**
	 * Constructor
	 * @param container
	 * @param context
	 */
	public CGuiExtender(Container container,
			final ProtocolContext context, 
			Protocol workingProtocol,
			List<JComponent> kbdButtons,
			Surface surface) {
		m_Protocol 			= context;
		m_container 		= container;
		m_workingProtocol	= workingProtocol;
		m_kbdButtons		= kbdButtons;
	}
	
	
	protected abstract void createIndividualButtons(JPanel buttonBar, Insets buttonsMargin);
	
	public void addOS_Buttons(JPanel buttonBar) {
		Insets buttonsMargin = new Insets(2, 2, 2, 2);

		// Creates individual Buttons based on the current OS and Device
		createIndividualButtons(buttonBar, buttonsMargin);
		
		// Recordbutton
		buttonBar.add(Box.createHorizontalStrut(10));
		
		JToggleButton androidRecord = new JToggleButton(Utils.getButtonIcon("record"));
		androidRecord.setToolTipText("Record Inputsequence");
		androidRecord.setSelectedIcon(Utils.getButtonIcon("record-stop"));
		androidRecord.setMargin(buttonsMargin);
		androidRecord.setAlignmentX(Component.RIGHT_ALIGNMENT);
		buttonBar.add(androidRecord);
		androidRecord.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Start or stop record of inputsequences
				CInputRecorder.getInst().setToggleRecord();
				if (CInputRecorder.getInst().isRecord()) {
					CInputRecorder.getInst().initStoryboard();
				} else {
					// Button released
					String strName = JOptionPane.showInputDialog(null, "Enter the name of the diagram : ", 
							"Mockupgenerator", JOptionPane.INFORMATION_MESSAGE);
					
					// Set Name for Storyboard
					CInputRecorder.getInst().finish(m_workingProtocol.getReceiverTask(), strName);
					
					// Generates the mock-file
					CMockupGenerator mockgen = new CMockupGenerator(strName);
					
					if (mockgen.testMockModel())
						JOptionPane.showMessageDialog(null, "Diagramfiles successfully generated.", "Mockupgenerator", JOptionPane.INFORMATION_MESSAGE); 
					else
						JOptionPane.showMessageDialog(null, "Errors occurred during generation of diagramfiles.", "Mockupgenerator", JOptionPane.ERROR_MESSAGE); 
				}
			}
		});
	}
	
	protected void setSurfaceToHandleKbdFocus() {
		if (m_surface != null && ! m_surface.requestFocusInWindow()) {
			m_surface.requestFocus();
		}
	}
}
