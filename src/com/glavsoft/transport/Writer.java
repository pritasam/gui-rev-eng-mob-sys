package com.glavsoft.transport;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.glavsoft.exceptions.TransportException;

public class Writer {
	private final DataOutputStream os;

	public Writer(OutputStream os) {
		this.os = new DataOutputStream(os);
	}

	public void flush() throws TransportException {
		try {
			os.flush();
		} catch (IOException e) {
			throw new TransportException("Cannot flush output stream", e);
		}
	}

	public void writeByte(int b) throws TransportException {
		write((byte) (b & 0xff));
	}

	public void write(byte b) throws TransportException {
		try {
			os.writeByte(b);
		} catch (IOException e) {
			throw new TransportException("Cannot write byte", e);
		}
	}

	public void writeInt16(int sh) throws TransportException {
		write((short) (sh & 0xffff));
	}

	public void write(short sh) throws TransportException {
		try {
			os.writeShort(sh);
		} catch (IOException e) {
			throw new TransportException("Cannot write short", e);
		}
	}

	public void writeInt32(int i) throws TransportException {
		write(i);
	}

	public void write(int i) throws TransportException {
		try {
			os.writeInt(i);
		} catch (IOException e) {
			throw new TransportException("Cannot write int", e);
		}
	}

	public void write(byte[] b) throws TransportException {
		write(b, 0, b.length);
	}

	/** Writes string as byte array (ISO_8859_1 encoded)
	 * (No any length prefix, string characters only!)
	 */
	public void write(String s) throws TransportException {
		write(s.getBytes(Reader.ISO_8859_1));
	}

	public void write(byte[] b, int length) throws TransportException {
		write(b, 0, length);
	}

	public void write(byte[] b, int offset, int length)
			throws TransportException {
		try {
			os.write(b, offset, length <= b.length ? length : b.length);
		} catch (IOException e) {
			throw new TransportException("Cannot write " + length + " bytes", e);
		}
	}
}