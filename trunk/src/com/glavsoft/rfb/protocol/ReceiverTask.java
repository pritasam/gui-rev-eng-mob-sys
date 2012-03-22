// Copyright (C) 2010, 2011 GlavSoft LLC.
// All rights reserved.
//
//-------------------------------------------------------------------------
// This file is part of the TightVNC software.  Please visit our Web site:
//
//                       http://www.tightvnc.com/
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License along
// with this program; if not, write to the Free Software Foundation, Inc.,
// 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
//-------------------------------------------------------------------------
//

package com.glavsoft.rfb.protocol;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.glavsoft.drawing.Renderer;
import com.glavsoft.exceptions.CommonException;
import com.glavsoft.exceptions.ProtocolException;
import com.glavsoft.exceptions.TransportException;
import com.glavsoft.rfb.ClipboardController;
import com.glavsoft.rfb.IRepaintController;
import com.glavsoft.rfb.ISessionController;
import com.glavsoft.rfb.client.FramebufferUpdateRequestMessage;
import com.glavsoft.rfb.client.SetPixelFormatMessage;
import com.glavsoft.rfb.encoding.EncodingType;
import com.glavsoft.rfb.encoding.PixelFormat;
import com.glavsoft.rfb.encoding.decoder.Decoder;
import com.glavsoft.rfb.encoding.decoder.DecodersContainer;
import com.glavsoft.rfb.encoding.decoder.FramebufferUpdateRectangle;
import com.glavsoft.rfb.encoding.decoder.RichCursorDecoder;
import com.glavsoft.transport.Reader;

public class ReceiverTask implements Runnable {
	private static final byte FRAMEBUFFER_UPDATE = 0;
	private static final byte SET_COLOR_MAP_ENTRIES = 1;
	private static final byte BELL = 2;
	private static final byte SERVER_CUT_TEXT = 3;


	private static Logger logger = Logger.getLogger("com.glavsoft.rfb.protocol.ReceiverTask");
	private final Reader reader;
	private volatile boolean isRunning = false;
	private Renderer renderer;
	private final IRepaintController repaintController;
	private final ClipboardController clipboardController;
	private final DecodersContainer decoders;
	private final ISessionController sessionManager;
	private FramebufferUpdateRequestMessage fullscreenFbUpdateIncrementalRequest;
	private final ProtocolContext context;
	private PixelFormat pixelFormat;
	private boolean needSendPixelFormat;

	public ReceiverTask(Reader reader,
			IRepaintController repaintController, ClipboardController clipboardController,
			ISessionController sessionManager, DecodersContainer decoders,
			ProtocolContext context) {
		this.reader = reader;
		this.repaintController = repaintController;
		this.clipboardController = clipboardController;
		this.context = context;
		this.sessionManager = sessionManager;
		this.decoders = decoders;
		renderer = repaintController.createRenderer(reader, context.getFbWidth(), context.getFbHeight(),
				context.getPixelFormat());
		fullscreenFbUpdateIncrementalRequest =
			new FramebufferUpdateRequestMessage(0, 0, context.getFbWidth(), context.getFbHeight(), true);
	}

	@Override
	public void run() {
		isRunning = true;
		while (isRunning) {
			try {
				byte messageId = reader.readByte();
				switch (messageId) {
				case FRAMEBUFFER_UPDATE:
//					logger.fine("Server message: FramebufferUpdate (0)");
					framebufferUpdateMessage();
					break;
				case SET_COLOR_MAP_ENTRIES:
					logger.severe("Server message SetColorMapEntries is not implemented.");
					break;
				case BELL:
					logger.fine("Server message: Bell");
					System.out.print("\0007");
				    System.out.flush();
					break;
				case SERVER_CUT_TEXT:
					logger.fine("Server message: CutText (3)");
					serverCutText();
					break;
				default:
					logger.severe("Unsupported server message. Id = " + messageId);
				}
			} catch (TransportException e) {
				logger.severe("Close session: " + e.getMessage());
				if (isRunning) {
					sessionManager.stopTasksAndRunNewSession("Connection closed.");
				}
				stopTask();
			} catch (ProtocolException e) {
				logger.severe(e.getMessage());
				if (isRunning) {
					sessionManager.stopTasksAndRunNewSession(e.getMessage() + "\nConnection closed.");
				}
				stopTask();
			} catch (CommonException e) {
				logger.severe(e.getMessage());
				if (isRunning) {
					sessionManager.stopTasksAndRunNewSession("Connection closed.");
				}
				stopTask();
			} catch (Throwable te) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				te.printStackTrace(pw);
				if (isRunning) {
					sessionManager.stopTasksAndRunNewSession(te.getMessage() + "\n" + sw.toString());
				}
				stopTask();
			}
		}
	}

	private void serverCutText() throws TransportException {
		reader.readByte();  // padding
		reader.readInt16(); // padding
		clipboardController.updateSystemClipboard(reader.readString());
	}

	public void framebufferUpdateMessage() throws CommonException {
		reader.readByte(); // padding
		int numberOfRectangles = reader.readUInt16();
		while (numberOfRectangles-- > 0) {
			FramebufferUpdateRectangle rect = new FramebufferUpdateRectangle();
			rect.fill(reader);

			Decoder decoder = decoders.getDecoderByType(rect.getEncodingType());
//			logger.info(rect.toString());
			if (decoder != null) {
				decoder.decode(reader, renderer, rect);
				repaintController.repaintBitmap(rect);
			} else if (rect.getEncodingType() == EncodingType.RICH_CURSOR) {
				RichCursorDecoder.getInstance().decode(reader, renderer, rect);
				repaintController.repaintCursor();
			} else if (rect.getEncodingType() == EncodingType.CURSOR_POS) {
				renderer.decodeCursorPosition(rect);
				repaintController.repaintCursor();
			} else if (rect.getEncodingType() == EncodingType.DESKTOP_SIZE) {
				if (rect.width <= 0 || rect.height <= 0)
					throw new ProtocolException("Server sent wrong Desctop Size: one of new desctop size dimensions is less or equals to zero (" +
							rect.width + "x" + rect.height + ").");
				fullscreenFbUpdateIncrementalRequest =
					new FramebufferUpdateRequestMessage(0, 0, rect.width, rect.height, true);
				synchronized (renderer) {
					renderer = repaintController.createRenderer(reader, rect.width, rect.height,
							context.getPixelFormat());
				}
				context.sendMessage(new FramebufferUpdateRequestMessage(0, 0, rect.width, rect.height, false));
//				repaintController.repaintCursor();
			} else
				throw new CommonException("Unprocessed encoding: " + rect.toString());
		}
		synchronized (this) {
			if (needSendPixelFormat) {
				needSendPixelFormat = false;
				context.setPixelFormat(pixelFormat);
				context.sendMessage(new SetPixelFormatMessage(pixelFormat));
				logger.fine("sent: "+pixelFormat);
				context.sendRefreshMessage();
				logger.fine("sent: nonincremental fb update");
			} else {
				context.sendMessage(fullscreenFbUpdateIncrementalRequest);
			}
		}
	}

	public synchronized void queueUpdatePixelFormat(PixelFormat pf) {
		pixelFormat = pf;
		needSendPixelFormat = true;
//		context.sendMessage(new FramebufferUpdateRequestMessage(0, 0, 1, 1, false));
	}

	public void stopTask() {
		isRunning = false;
	}
	
	/**
	 * O. laudi
	 * 
	 */
	public void saveScreenshot() {
		BufferedImage image = new BufferedImage(renderer.getWidth(), renderer.getHeight(), BufferedImage.TYPE_INT_RGB);
		image.setRGB(0, 0, renderer.getWidth(), renderer.getHeight(), renderer.getPixels(), 0, renderer.getWidth());
		
		try {
		    // retrieve image
		    File outputfile = new File("D:\\saved_" + System.currentTimeMillis() + ".png");
		    ImageIO.write(image, "png", outputfile);
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
	}
	
	/**
	 * O. Laudi
	 */

}
