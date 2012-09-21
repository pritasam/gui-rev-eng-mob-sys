/**
 * 
 */
package de.ostfalia.viewer.gui.extension;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;

import de.ostfalia.viewer.inputrecorder.CInputRecorder;

/**
 * @author O. Laudi
 *
 */
public class CProgressWnd{

	protected int				m_nStepCount;
	protected int				m_nCurrentStep;
	protected String 			m_strCaption;
	protected boolean			m_isRunning;
	protected final JDialog 	m_dlg;
	protected JProgressBar 		m_dpb;

	
	/**
	 * 
	 * @param nStepCount
	 */
	public CProgressWnd(int nStepCount, String strCaption) {
		m_nStepCount 	= nStepCount;
		m_nCurrentStep	= 0;
		m_strCaption	= strCaption;
		m_isRunning		= false;
		
		m_dlg = new JDialog(CInputRecorder.getInst().getContainerFrame(), "Please wait...", true);
		m_dpb = new JProgressBar(0, m_nStepCount);
		m_dpb.setValue(1);
	    m_dlg.add(BorderLayout.CENTER, m_dpb);
	    m_dlg.add(BorderLayout.NORTH, new JLabel(m_strCaption));
	    m_dlg.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	    m_dlg.setSize(300, 75);
	    m_dlg.setLocationRelativeTo(CInputRecorder.getInst().getContainerFrame());
	}

	/**
	 * 
	 */
	public void nextStep() {
		try {
			if (m_nCurrentStep < m_nStepCount)
				m_nCurrentStep++;
			else
				hide();
			
			SwingUtilities.invokeLater(new Runnable() {
			    public void run() {
			      // Here, we can safely update the GUI
			      // because we'll be called from the
			      // event dispatch thread
			    	m_dpb.setValue(m_nCurrentStep);
			    }
			});
		}
		catch (Exception e) {
			m_dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		}
	}
	
	public void show() {
		try {
			Thread t = new Thread(new Runnable() {
			      public void run() {
			    	  m_dlg.setVisible(true);
			    	  m_dlg.repaint();
			      }
		    });
		    t.start();
		}
		catch (Exception e) {
			m_dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		}
	}
	
	public void hide() {
		m_isRunning = false;
		try {
			m_dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			m_dlg.setVisible(false);
		}
		catch (Exception e) {
			
		}
	}
}
