/**
 * 
 */
package de.ostfalia.viewer.gui.extension;

import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import com.glavsoft.rfb.protocol.Protocol;
import com.glavsoft.rfb.protocol.ProtocolContext;
import com.glavsoft.viewer.swing.Surface;
import com.glavsoft.viewer.swing.Utils;

import de.ostfalia.mockup.datamodel.storyboard.CStoryboardPlayer;
import de.ostfalia.mockup.generator.CMockupGenerator;
import de.ostfalia.viewer.inputrecorder.CInputRecorder;
import de.ostfalia.xml.reader.CXMLStoryboardReader;

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
	protected CStoryboardPlayer			m_storyPlayer;
	
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
		m_storyPlayer		= null;
	}
	
	
	protected abstract void createIndividualButtons(JPanel buttonBar, Insets buttonsMargin);
	
	public void addOS_Buttons(JPanel buttonBar) {
		Insets buttonsMargin = new Insets(2, 2, 2, 2);

		// Creates individual Buttons based on the current OS and Device
		createIndividualButtons(buttonBar, buttonsMargin);
		
		// Recordbutton
		buttonBar.add(Box.createHorizontalStrut(10));
		
		JToggleButton btnRecord = new JToggleButton(Utils.getButtonIcon("record"));
		btnRecord.setToolTipText("Record Inputsequence");
		btnRecord.setSelectedIcon(Utils.getButtonIcon("record-stop"));
		btnRecord.setMargin(buttonsMargin);
		btnRecord.setAlignmentX(Component.RIGHT_ALIGNMENT);
		buttonBar.add(btnRecord);
		btnRecord.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Start or stop record of inputsequences
				CInputRecorder.getInst().setToggleRecord();
				if (CInputRecorder.getInst().isRecord()) {
					CInputRecorder.getInst().initStoryboard();
				} else {
					// Button released
					String strName = JOptionPane.showInputDialog(null, "Enter the name of the exported MockUp-files : ", 
							"MockUp-files Generator", JOptionPane.INFORMATION_MESSAGE);
					
					// Set Name for Storyboard
					CInputRecorder.getInst().finish(m_workingProtocol.getReceiverTask(), strName);
					
					// Generates the mock-file
					CMockupGenerator mockgen = new CMockupGenerator(strName, CInputRecorder.getInst().getStoryboard());
					
					if (mockgen.createMockModel())
						JOptionPane.showMessageDialog(null, "Diagramfiles successfully generated.", "Mockupgenerator", JOptionPane.INFORMATION_MESSAGE); 
					else
						JOptionPane.showMessageDialog(null, "Errors occurred during generation of diagramfiles.", "Mockupgenerator", JOptionPane.ERROR_MESSAGE); 
					
//					if (mockgen.testMockModel())
//						JOptionPane.showMessageDialog(null, "Diagramfiles successfully generated.", "Mockupgenerator", JOptionPane.INFORMATION_MESSAGE); 
//					else
//						JOptionPane.showMessageDialog(null, "Errors occurred during generation of diagramfiles.", "Mockupgenerator", JOptionPane.ERROR_MESSAGE); 
				}
			}
		});
		
		// Storyboardbutton
		buttonBar.add(Box.createHorizontalStrut(10));
		
		final JToggleButton btnStoryboard = new JToggleButton(Utils.getButtonIcon("storyboard"));
		btnStoryboard.setToolTipText("Play Storyboard");
		btnStoryboard.setSelectedIcon(Utils.getButtonIcon("storyboard"));
		btnStoryboard.setMargin(buttonsMargin);
		btnStoryboard.setAlignmentX(Component.RIGHT_ALIGNMENT);
		btnStoryboard.setName("StoryboardPlay");
		buttonBar.add(btnStoryboard);
		btnStoryboard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (CInputRecorder.getInst().isStoryPlaying()) {
					// Stop playing story
					if (m_storyPlayer != null) {
						m_storyPlayer.stop();
					}
				} else {
					// get path to storyboard
					final JFileChooser fc = new JFileChooser();
					fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
						
						@Override
						public String getDescription() {
							return "Storyboard (*.story)";
						}
						
						@Override
						public boolean accept(File f) {
							String name = f.getName();
					        if (name.endsWith(".story") || f.isDirectory())
					            return true;
							return false;
						}
					});
					int nRetVal = fc.showOpenDialog(m_container);
					
					// play storyboard or abort?
					if (nRetVal == JFileChooser.APPROVE_OPTION) {
						// check if storyboard is valid (dtd) and read it
						CXMLStoryboardReader storyReader = new CXMLStoryboardReader(fc.getSelectedFile());
						m_storyPlayer = new CStoryboardPlayer(storyReader.getStoryboard(), m_workingProtocol);
						
						// Check, if displayresolution fits to the saved
						int nWSaved	= Integer.valueOf(m_storyPlayer.getStoryboard().getWidth());
						int nWExpect = m_workingProtocol.getReceiverTask().getRenderer().getWidth();
						int nHSaved = Integer.valueOf(m_storyPlayer.getStoryboard().getHeight());
						int nHExpect = m_workingProtocol.getReceiverTask().getRenderer().getHeight();
						
						if ((nWSaved != nWExpect) || (nHSaved != nHExpect)) {
							// displayresolution doesn't fit
							JOptionPane.showMessageDialog(null, "The storyboard expects the displayresolution " + nWSaved + "x" + nHSaved + ".\n\n" +
																"Your current Resolution ist " + nWExpect + "x" + nHExpect + ".", 
																"Incorrect displayresolution", JOptionPane.ERROR_MESSAGE);
						}
						else {
							// start playing story
							m_storyPlayer.play();
						}
						
						CInputRecorder.getInst().setToggleStoryPlaying();
						btnStoryboard.setSelected(false);
					}
				}
				CInputRecorder.getInst().setToggleStoryPlaying();
			}
		});
	}
	
	protected void setSurfaceToHandleKbdFocus() {
		if (m_surface != null && ! m_surface.requestFocusInWindow()) {
			m_surface.requestFocus();
		}
	}
}
