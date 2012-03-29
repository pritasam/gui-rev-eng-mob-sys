/**
 * 
 */
package de.ostfalia.viewer.gui.extension;

import java.awt.Container;

import com.glavsoft.rfb.protocol.Protocol;
import com.glavsoft.rfb.protocol.ProtocolContext;

/**
 * @author O.Laudi
 *
 */
public class CGuiExtender {
	private ProtocolContext 	m_Protocol;
	private Container 			m_container;
	
	/**
	 * Constructor
	 * @param container
	 * @param context
	 */
	public CGuiExtender(Container container,
			final ProtocolContext context) {
		m_Protocol = context;
		m_container = container;
	}
	
	
}
