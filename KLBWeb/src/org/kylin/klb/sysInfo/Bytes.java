package org.kylin.klb.sysInfo;

public class Bytes {
	public static String substring(String src, int start_idx, int end_idx) {
		byte[] b = src.getBytes();
		String tgt = "";
		for (int i = start_idx; i <= end_idx; ++i) {
			tgt = tgt + (char) b[i];
		}
		return tgt;
	}
}
