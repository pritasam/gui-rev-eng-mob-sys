/**
 * 
 */
package de.ostfalia.viewer.gui.extension;

import java.awt.Container;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.glavsoft.rfb.client.KeyEventMessage;
import com.glavsoft.rfb.protocol.Protocol;
import com.glavsoft.rfb.protocol.ProtocolContext;
import com.glavsoft.utils.Keymap;
import com.glavsoft.viewer.swing.Surface;
import com.glavsoft.viewer.swing.Utils;

/**
 * @author O. Laudi
 *
 */
public class CGuiExtenderAndroid extends CGuiExtender {

	public CGuiExtenderAndroid(Container container, ProtocolContext context,
			Protocol workingProtocol, List<JComponent> kbdButtons,
			Surface surface) {
		super(container, context, workingProtocol, kbdButtons, surface);
	}

	@Override
	protected void createIndividualButtons(JPanel buttonBar, Insets buttonsMargin) {
		buttonBar.add(Box.createHorizontalStrut(10));
		
		JButton androidHomeButton = new JButton(Utils.getButtonIcon("android_home"));
		androidHomeButton.setToolTipText("Home");
		androidHomeButton.setMargin(buttonsMargin);
		androidHomeButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
		buttonBar.add(androidHomeButton);
		androidHomeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendAndroidHomeKey(m_Protocol);
				setSurfaceToHandleKbdFocus();
			}
		});
		
		JButton androidMenuButton = new JButton(Utils.getButtonIcon("android_menu"));
		androidMenuButton.setToolTipText("Menu");
		androidMenuButton.setMargin(buttonsMargin);
		androidMenuButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
		buttonBar.add(androidMenuButton);
		androidMenuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendAndroidMenuKey(m_Protocol);
				setSurfaceToHandleKbdFocus();
			}
		});
		
		JButton androidBackButton = new JButton(Utils.getButtonIcon("android_back"));
		androidBackButton.setToolTipText("Back");
		androidBackButton.setMargin(buttonsMargin);
		androidBackButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
		buttonBar.add(androidBackButton);
		androidBackButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendAndroidBackKey(m_Protocol);
				setSurfaceToHandleKbdFocus();
			}
		});
		
		JButton androidSearchButton = new JButton(Utils.getButtonIcon("android_search"));
		androidSearchButton.setToolTipText("Search");
		androidSearchButton.setMargin(buttonsMargin);
		androidSearchButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
		buttonBar.add(androidSearchButton);
		androidSearchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendAndroidSearchKey(m_Protocol);
				setSurfaceToHandleKbdFocus();
			}
		});
	}

	private void sendAndroidHomeKey(ProtocolContext context) {
		context.sendMessage(new KeyEventMessage(Keymap.K_HOME, true));
		context.sendMessage(new KeyEventMessage(Keymap.K_HOME, false));
	}
	
	private void sendAndroidMenuKey(ProtocolContext context) {
		context.sendMessage(new KeyEventMessage(Keymap.K_PAGE_UP, true));
		context.sendMessage(new KeyEventMessage(Keymap.K_PAGE_UP, false));
	}
	
	private void sendAndroidBackKey(ProtocolContext context) {
		context.sendMessage(new KeyEventMessage(Keymap.K_DELETE, true));
		context.sendMessage(new KeyEventMessage(Keymap.K_DELETE, false));
	}
	
	private void sendAndroidSearchKey(ProtocolContext context) {
		context.sendMessage(new KeyEventMessage(Keymap.K_CTRL_LEFT, true));
		context.sendMessage(new KeyEventMessage(Keymap.K_CTRL_LEFT, false));
	}
	
	// Not in use now
//	private void sendAndroidRotateKey(ProtocolContext context) {
//		context.sendMessage(new KeyEventMessage(Keymap.K_F4, true));
//		context.sendMessage(new KeyEventMessage(Keymap.K_F4, false));
//	}
}
