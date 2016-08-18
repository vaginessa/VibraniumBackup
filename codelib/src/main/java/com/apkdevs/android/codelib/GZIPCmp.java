package com.apkdevs.android.codelib;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZIPCmp {
	public static String CmpTxT(String str) throws IOException {
		byte[] blockcopy = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(str.length()).array();
		ByteArrayOutputStream baos = new ByteArrayOutputStream(str.length());
		GZIPOutputStream gzos = new GZIPOutputStream(baos);
		gzos.write(str.getBytes()); gzos.close(); baos.close();
		byte[] cmprsd = new byte[4 + baos.toByteArray().length];
		System.arraycopy(blockcopy, 0, cmprsd, 0, 4);
		System.arraycopy(baos.toByteArray(), 0, cmprsd, 4, baos.toByteArray().length);
		return Base64.encodeToString(cmprsd, Base64.DEFAULT);
	}
	public static String DCmpTxT(String str) throws IOException {
		byte[] compressed = Base64.decode(str, Base64.DEFAULT);
		if (compressed.length > 4) {
			GZIPInputStream gzis = new GZIPInputStream(new ByteArrayInputStream(compressed, 4, compressed.length - 4));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			for (int value = 0; value != -1;) {
				value = gzis.read();
				if (value != -1) baos.write(value);
			}
			gzis.close();
			baos.close();
			return new String(baos.toByteArray(), "UTF-8");
		}	else	{ return ""; }
	}
}
