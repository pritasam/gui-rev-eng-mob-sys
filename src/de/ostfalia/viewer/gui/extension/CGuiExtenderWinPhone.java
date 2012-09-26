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
public class CGuiExtenderWinPhone extends CGuiExtender {

	public CGuiExtenderWinPhone(Container container, ProtocolContext context,
			Protocol workingProtocol, List<JComponent> kbdButtons,
			Surface surface) {
		super(container, context, workingProtocol, kbdButtons, surface);
	}

	@Override
	protected void createIndividualButtons(JPanel buttonBar, Insets buttonsMargin) {
buttonBar.add(Box.createHorizontalStrut(10));
		
		JButton androidHomeButton = new JButton(Utils.getButtonIcon("winphone_windows"));
		androidHomeButton.setToolTipText("Windows");
		androidHomeButton.setMargin(buttonsMargin);
		androidHomeButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
		buttonBar.add(androidHomeButton);
		androidHomeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendWinPhoneHomeKey(m_Protocol);
				setSurfaceToHandleKbdFocus();
			}
		});
		
		JButton androidBackButton = new JButton(Utils.getButtonIcon("winphone_back"));
		androidBackButton.setToolTipText("Back");
		androidBackButton.setMargin(buttonsMargin);
		androidBackButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
		buttonBar.add(androidBackButton);
		androidBackButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendWinPhoneBackKey(m_Protocol);
				setSurfaceToHandleKbdFocus();
			}
		});
		
		JButton androidSearchButton = new JButton(Utils.getButtonIcon("winphone_search"));
		androidSearchButton.setToolTipText("Search");
		androidSearchButton.setMargin(buttonsMargin);
		androidSearchButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
		buttonBar.add(androidSearchButton);
		androidSearchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendWinPhoneSearchKey(m_Protocol);
				setSurfaceToHandleKbdFocus();
			}
		});
	}
	
	private void sendWinPhoneHomeKey(ProtocolContext context) {
		context.sendMessage(new KeyEventMessage(Keymap.K_HOME, true));
		context.sendMessage(new KeyEventMessage(Keymap.K_HOME, false));
	}
		
	private void sendWinPhoneBackKey(ProtocolContext context) {
		context.sendMessage(new KeyEventMessage(Keymap.K_DELETE, true));
		context.sendMessage(new KeyEventMessage(Keymap.K_DELETE, false));
	}
	
	private void sendWinPhoneSearchKey(ProtocolContext context) {
		context.sendMessage(new KeyEventMessage(Keymap.K_CTRL_LEFT, true));
		context.sendMessage(new KeyEventMessage(Keymap.K_CTRL_LEFT, false));
	}

}
