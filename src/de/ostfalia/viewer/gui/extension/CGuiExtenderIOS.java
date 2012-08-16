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
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.glavsoft.rfb.client.PointerEventMessage;
import com.glavsoft.rfb.protocol.Protocol;
import com.glavsoft.rfb.protocol.ProtocolContext;
import com.glavsoft.viewer.swing.Surface;
import com.glavsoft.viewer.swing.Utils;

/**
 * @author O. Laudi
 *
 */
public class CGuiExtenderIOS extends CGuiExtender {


	public CGuiExtenderIOS(Container container, ProtocolContext context,
			Protocol workingProtocol, List<JComponent> kbdButtons,
			Surface surface) {
		super(container, context, workingProtocol, kbdButtons, surface);
	}

	@Override
	protected void createIndividualButtons(JPanel buttonBar, Insets buttonsMargin) {
		buttonBar.add(Box.createHorizontalStrut(10));
		
		JButton androidHomeButton = new JButton(Utils.getButtonIcon("ios_home"));
		androidHomeButton.setToolTipText("Home");
		androidHomeButton.setMargin(buttonsMargin);
		androidHomeButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
		buttonBar.add(androidHomeButton);
		androidHomeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendIOSHomeKey(m_Protocol);
				setSurfaceToHandleKbdFocus();
			}
		});
		
		JButton androidMenuButton = new JButton(Utils.getButtonIcon("ios_power"));
		androidMenuButton.setToolTipText("Power");
		androidMenuButton.setMargin(buttonsMargin);
		androidMenuButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
		buttonBar.add(androidMenuButton);
		androidMenuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendIOSPowerKey(m_Protocol);
				setSurfaceToHandleKbdFocus();
			}
		});
	}

	private void sendIOSHomeKey(ProtocolContext context) {
		// Simulate rightclick
		context.sendMessage(new PointerEventMessage((byte) 4, (short) 1, (short) 1));
		context.sendMessage(new PointerEventMessage((byte) 0, (short) 1, (short) 1));
	}
	
	private void sendIOSPowerKey(ProtocolContext context) {
		// simulate middle mousebutton
		context.sendMessage(new PointerEventMessage((byte) 2, (short) 1, (short) 1));
		context.sendMessage(new PointerEventMessage((byte) 0, (short) 1, (short) 1));
	}
}
