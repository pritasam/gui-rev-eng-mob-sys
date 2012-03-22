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

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import com.glavsoft.rfb.CapabilityContainer;
import com.glavsoft.rfb.IChangeSettingsListener;
import com.glavsoft.rfb.RfbCapabilityInfo;
import com.glavsoft.rfb.encoding.EncodingType;
import com.glavsoft.rfb.protocol.auth.SecurityType;

/**
 * Protocol Settings class
 */
public class ProtocolSettings {
    private static final EncodingType DEFAULT_PREFERRED_ENCODING = EncodingType.TIGHT;
	private static final int DEFAULT_JPEG_QUALITY = -6;
	private static final int DEFAULT_COMPRESSION_LEVEL = -6;

	// bits per pixel constants
	public static final int BPP_32 = 32;
	public static final int BPP_16 = 16;
	public static final int BPP_8 = 8;
	public static final int BPP_6 = 6;
	public static final int BPP_3 = 3;

	public static final int BPP_SERVER_SETTINGS = 0;

	private static final int DEFAULT_BITS_PER_PIXEL = BPP_32;

//	private static final int RECONNECT_INTERVAL_5_SEC = 5 * 1000;

    private boolean sharedFlag;
    private double scaling;
    private boolean viewOnly;
    private EncodingType preferredEncoding;
    private boolean allowCopyRect;
    private boolean showRemoteCursor;
    private LocalPointer mouseCursorTrack;
    private int compressionLevel;
    private int jpegQuality;
    private String protocolVersion;
    private boolean isTight;
	private boolean allowClipboardTransfer;
	private boolean convertToAscii;
	private int bitsPerPixel;

	private final List<IChangeSettingsListener> listeners = new LinkedList<IChangeSettingsListener>();

    public CapabilityContainer
		tunnelingCapabilities,
		authCapabilities,
		serverMessagesCapabilities,
		clientMessagesCapabilities,
		encodingTypesCapabilities;
    public LinkedHashSet<EncodingType> encodings;
	private boolean prefEncodingChanged;

    public static ProtocolSettings getDefaultSettings() {
    	ProtocolSettings settings = new ProtocolSettings();
        settings.sharedFlag = true;

        settings.scaling = 1.0;
        settings.viewOnly = false;

//        settings.cursor = (int)WindowsRfbViewerCore.Cursors.Dot;
        settings.showRemoteCursor = true;
        settings.mouseCursorTrack = LocalPointer.ON;

        settings.tunnelingCapabilities = new CapabilityContainer();
//        settings.initKnownTunnelingCapabilities(settings.tunnelingCapabilities);
        settings.authCapabilities = new CapabilityContainer();
        settings.initKnownAuthCapabilities(settings.authCapabilities);
        settings.serverMessagesCapabilities = new CapabilityContainer();
//        settings.initKnownServerMessagesCapabilities(settings.serverMessagesCapatibilities);
        settings.clientMessagesCapabilities = new CapabilityContainer();
//        settings.initKnownClientMessagesCapabilities(settings.clientMessagesCapatibilities);
        settings.encodingTypesCapabilities = new CapabilityContainer();
        settings.initKnownEncodingTypesCapabilities(settings.encodingTypesCapabilities);
        settings.preferredEncoding = DEFAULT_PREFERRED_ENCODING;
        settings.allowCopyRect = true;
        settings.compressionLevel = DEFAULT_COMPRESSION_LEVEL;
        settings.jpegQuality = DEFAULT_JPEG_QUALITY;
        settings.prefEncodingChanged = true;
        settings.convertToAscii = false;
        settings.allowClipboardTransfer = true;
        settings.bitsPerPixel = 0;//DEFAULT_BITS_PER_PIXEL;
        settings.refine();
        return settings;
    }

	private void initKnownAuthCapabilities(CapabilityContainer cc) {
		cc.addEnabled(SecurityType.NONE_AUTHENTICATION.getId(),
				RfbCapabilityInfo.VENDOR_STANDARD, RfbCapabilityInfo.AUTHENTICATION_NO_AUTH);
		cc.addEnabled(SecurityType.VNC_AUTHENTICATION.getId(),
				RfbCapabilityInfo.VENDOR_STANDARD, RfbCapabilityInfo.AUTHENTICATION_VNC_AUTH);
	    //cc.addEnabled( 19, "VENC", "VENCRYPT");
	    //cc.addEnabled( 20, "GTKV", "SASL____");
	    //cc.addEnabled(129, RfbCapabilityInfo.TIGHT_VNC_VENDOR, "ULGNAUTH");
	    //cc.addEnabled(130, RfbCapabilityInfo.TIGHT_VNC_VENDOR, "XTRNAUTH");
	}

	private void initKnownEncodingTypesCapabilities(CapabilityContainer cc) {
		cc.add(EncodingType.COPY_RECT.getId(),
				RfbCapabilityInfo.VENDOR_STANDARD, RfbCapabilityInfo.ENCODING_COPYRECT);
		cc.add(EncodingType.HEXTILE.getId(),
				RfbCapabilityInfo.VENDOR_STANDARD, RfbCapabilityInfo.ENCODING_HEXTILE);
		cc.add(EncodingType.ZLIB.getId(),
				RfbCapabilityInfo.VENDOR_TRIADA, RfbCapabilityInfo.ENCODING_ZLIB);
		cc.add(EncodingType.ZRLE.getId(),
				RfbCapabilityInfo.VENDOR_TRIADA, RfbCapabilityInfo.ENCODING_ZRLE);
		cc.add(EncodingType.RRE.getId(),
				RfbCapabilityInfo.VENDOR_STANDARD, RfbCapabilityInfo.ENCODING_RRE);
		cc.add(EncodingType.TIGHT.getId(),
				RfbCapabilityInfo.VENDOR_TIGHT, RfbCapabilityInfo.ENCODING_TIGHT);

		cc.add(EncodingType.RICH_CURSOR.getId(),
				RfbCapabilityInfo.VENDOR_TIGHT, RfbCapabilityInfo.ENCODING_RICH_CURSOR);
		cc.add(EncodingType.CURSOR_POS.getId(),
				RfbCapabilityInfo.VENDOR_TIGHT, RfbCapabilityInfo.ENCODING_CURSOR_POS);
		cc.add(EncodingType.DESKTOP_SIZE.getId(),
				RfbCapabilityInfo.VENDOR_TIGHT, RfbCapabilityInfo.ENCODING_DESKTOP_SIZE);
	}

	public void addListener(IChangeSettingsListener listener) {
		listeners.add(listener);
	}

	public byte getSharedFlag() {
		return (byte) (sharedFlag ? 1 : 0);
	}

	public boolean isShared() {
		return sharedFlag;
	}

	public void setSharedFlag(boolean sharedFlag) {
		this.sharedFlag = sharedFlag;
	}

	public double getScaling() {
		return scaling;
	}

	public void setScaling(double scaling) {
		this.scaling = scaling;
	}

	public boolean isViewOnly() {
		return viewOnly;
	}

	public void setViewOnly(boolean viewOnly) {
		if (this.viewOnly != viewOnly) {
			this.viewOnly = viewOnly;
			fireListeners();
		}
	}

	public void enableAllEncodingCaps() {
		encodingTypesCapabilities.setAllEnable(true);

	}

	public int getBitsPerPixel() {
		return bitsPerPixel;
	}

	/**
	 * Set bpp only in 3, 6, 8, 16, 32. When bpp is wrong, it resets to {@link #DEFAULT_BITS_PER_PIXEL}
	 */
	public void setBitsPerPixel(int bpp) {
		if (bitsPerPixel != bpp) {
			prefEncodingChanged = true;
			switch (bpp) {
			case BPP_32:
			case BPP_16:
			case BPP_8:
			case BPP_6:
			case BPP_3:
			case BPP_SERVER_SETTINGS:
				bitsPerPixel = bpp;
				break;
			default:
				bitsPerPixel = DEFAULT_BITS_PER_PIXEL;
			}
			fireListeners();
		}
	}

	private void refine() {
		encodings = new LinkedHashSet<EncodingType>();
		if (EncodingType.RAW_ENCODING == preferredEncoding) {
			// when RAW selected send no ordinary encodings so only default RAW encoding will be enabled
		} else {
			encodings.add(preferredEncoding); // preferred first
			encodings.addAll(EncodingType.ordinaryEncodings);
			if (compressionLevel > 0 && compressionLevel < 10) {
				encodings.add(EncodingType.byId(
						EncodingType.COMPRESS_LEVEL_0.getId() + compressionLevel));
			}
			if (jpegQuality > 0 && jpegQuality < 10 && bitsPerPixel == BPP_32) {
				encodings.add(EncodingType.byId(
						EncodingType.JPEG_QUALITY_LEVEL_0.getId() + jpegQuality));
			}
			if (allowCopyRect) {
				encodings.add(EncodingType.COPY_RECT);
			}
		}
		switch(mouseCursorTrack) {
		case OFF:
			showRemoteCursor = false;
			break;
		case HIDE:
			showRemoteCursor = false;
			encodings.add(EncodingType.RICH_CURSOR);
			encodings.add(EncodingType.CURSOR_POS);
			break;
		case ON:
		default:
			showRemoteCursor = true;
			encodings.add(EncodingType.RICH_CURSOR);
			encodings.add(EncodingType.CURSOR_POS);
		}
		encodings.add(EncodingType.DESKTOP_SIZE);
		fireListeners();
	}

	public void fireListeners() {
		for (IChangeSettingsListener listener : listeners) {
			listener.fireChangeSettings(this);
		}
	}

	public void setPreferredEncoding(EncodingType preferredEncoding) {
		if (this.preferredEncoding != preferredEncoding) {
			this.preferredEncoding = preferredEncoding;
			prefEncodingChanged = true;
			refine();
		}
	}

	public EncodingType getPreferredEncoding() {
		return preferredEncoding;
	}

	public boolean checkAndResetPreferredEncondingChangedFlag() {
		if (prefEncodingChanged) {
			prefEncodingChanged = false;
			return true;
		}
		return false;
	}

	public void setAllowCopyRect(boolean allowCopyRect) {
		if (this.allowCopyRect != allowCopyRect) {
			this.allowCopyRect = allowCopyRect;
			refine();
		}
	}

	public boolean isAllowCopyRect() {
		return allowCopyRect;
	}


	public boolean isShowRemoteCursor() {
		return showRemoteCursor;
	}

	public void setMouseCursorTrack(LocalPointer mouseCursorTrack) {
		if (this.mouseCursorTrack != mouseCursorTrack) {
			this.mouseCursorTrack = mouseCursorTrack;
			refine();
		}
	}

	public LocalPointer getMouseCursorTrack() {
		return mouseCursorTrack;
	}

	public void setCompressionLevel(int compressionLevel) {
		if (this.compressionLevel != compressionLevel) {
			this.compressionLevel = compressionLevel;
			refine();
		}
	}

	public int getCompressionLevel() {
		return compressionLevel;
	}

	public void setJpegQuality(int jpegQuality) {
		if (this.jpegQuality != jpegQuality) {
			this.jpegQuality = jpegQuality;
			refine();
		}
	}

	public int getJpegQuality() {
		return jpegQuality;
	}

	public void setAllowClipboardTransfer(boolean allowClipboardTransfer) {
		if (this.allowClipboardTransfer != allowClipboardTransfer) {
			this.allowClipboardTransfer = allowClipboardTransfer;
			fireListeners();
		}
	}

	public boolean isAllowClipboardTransfer() {
		return allowClipboardTransfer;
	}

	public void setTight(boolean isTight) {
		this.isTight = isTight;
	}

	public boolean isTight() {
		return isTight;
	}

	public void setProtocolVersion(String protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	public String getProtocolVersion() {
		return protocolVersion;
	}

	public boolean isConvertToAscii() {
		return convertToAscii;
	}

	public void setConvertToAscii(boolean convertToAscii) {
		if (this.convertToAscii != convertToAscii) {
			this.convertToAscii = convertToAscii;
			refine();
		}
	}

}
