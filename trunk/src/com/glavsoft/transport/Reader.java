package com.glavsoft.transport;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import com.glavsoft.exceptions.ClosedConnectionException;
import com.glavsoft.exceptions.TransportException;

public class Reader {
	final static Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
	private final DataInputStream is;

	public Reader(InputStream is) {
		this.is = new DataInputStream(new BufferedInputStream(is));
	}

	public byte readByte() throws TransportException {
		try {
			byte readByte = is.readByte();
			return readByte;
		} catch (EOFException e) {
			throw new ClosedConnectionException(e);
		} catch (IOException e) {
			throw new TransportException("Cannot read byte", e);
		}

	}

	public int readUInt8() throws TransportException {
		return readByte() & 0x0ff;
	}

	public int readUInt16() throws TransportException {
		return readInt16() & 0x0ffff;
	}

	public short readInt16() throws TransportException {
		try {
			short readShort = is.readShort();
			return readShort;
		} catch (EOFException e) {
			throw new ClosedConnectionException(e);
		} catch (IOException e) {
			throw new TransportException("Cannot read int16", e);
		}
	}

	public long readUInt32() throws TransportException {
		return readInt32() & 0xffffffffL;
	}

	public int readInt32() throws TransportException {
		try {
			int readInt = is.readInt();
			return readInt;
		} catch (EOFException e) {
			throw new ClosedConnectionException(e);
		} catch (IOException e) {
			throw new TransportException("Cannot read int16", e);
		}
	}

	/**
	 * Read string by it length
	 *
	 * @return String read
	 */
	public String readString(int length) throws TransportException {
		String stringReceived = new String(readBytes(length), ISO_8859_1);
		return stringReceived;
	}

	/**
	 * Read 32-bit string length and then string themself by it length
	 *
	 * @return String read
	 * @throws TransportException
	 */
	public String readString() throws TransportException {
		// unset most sighificant (sign) bit 'cause InputStream#readFully
		// reads
		// [int] length bytes from stream. Change when realy need read sthing more
		// than
		// 2147483647 bytes lenght
		int length = readInt32() & Integer.MAX_VALUE;
		return readString(length);
	}

	public byte[] readBytes(int length) throws TransportException {
		byte b[] = new byte[length];
		return readBytes(b, 0, length);
	}

	public byte[] readBytes(byte[] b, int offset, int length) throws TransportException {
		try {
			is.readFully(b, offset, length);
			return b;
		} catch (EOFException e) {
			throw new ClosedConnectionException(e);
		} catch (IOException e) {
			throw new TransportException("Cannot read " + length + " bytes array", e);
		}
	}
}