package org.kylin.klb.util;

import org.apache.commons.lang.StringUtils;

import sun.misc.BASE64Encoder;

public class HashToBase64Util {

	public static String hashToBase64Util(String hash) {
		BASE64Encoder encoder = new BASE64Encoder();
		if (StringUtils.equals(hash, "")) {
			return "";
		} else {
			try {
				String base64 = encoder.encode(hash.getBytes("8859_1"));
				return base64;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
