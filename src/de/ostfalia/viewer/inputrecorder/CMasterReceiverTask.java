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
	private int state; //0=test 1=Filter 2=normal
	private long time;
	private CVNCServerTester tester;
	private CVNCServerFilter filter;
	
	public CMasterReceiverTask(Reader reader,
			IRepaintController repaintController,
			ClipboardController clipboardController,
			ISessionController sessionManager, DecodersContainer decoders,
			ProtocolContext context) {
		super(reader, repaintController, clipboardController, sessionManager, decoders,
				context);
		state = 0;
		time=0;
		tester = new CVNCServerTester(reader, repaintController, clipboardController, sessionManager, decoders, context);
	}

	/* (non-Javadoc)
	 * @see com.glavsoft.rfb.protocol.ReceiverTask#run()
	 */
	@Override
	public void run() {
		if(state==0){
			
			// User must show a static image
			JOptionPane.showMessageDialog(null, "Please make sure to show a static screencontent on your device.", "Initialize device", 1);

			tester.run();

			CLogger.getInst(CLogger.SYS_OUT).writeline("tester.getEqualRatio() = " + tester.getEqualRatio());
			if(tester.getEqualRatio()>0.0){
				state=1;
			}else{
				state=2;
			}
			CLogger.getInst(CLogger.SYS_OUT).writeline("State = " + state);

		}
		if(state==1){
			if (filter == null)
				filter = new CVNCServerFilter(reader, repaintController, clipboardController, sessionManager, decoders, context);
			filter.run();
		}else if(state==2){
			super.run();
		}
	}
	
	

}
