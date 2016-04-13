package org.kylin.modules.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class UUID {
	private String prefx = "ID";
	private static long serial = 0L;

	public static String create() {
		return new UUID().toString();
	}

	public static String create(String prefx) {
		return new UUID(prefx).toString();
	}

	public static boolean isID(String sid) {
		return (sid != null) && (sid.length() >= 1)
				&& (!"null".equalsIgnoreCase(sid));
	}

	public static boolean isID(int id) {
		return id != 0;
	}

	public UUID() {
	}

	public UUID(String pre) {
		this.prefx = pre;
	}

	public String toString() {
		SimpleDateFormat formatter = new SimpleDateFormat("MMddHHmmssSSS");
		String time = formatter.format(new Date());
		serial += 1L;
		if (serial > 999L)
			serial = 0L;
		int random = (int) (Math.random() * 10000.0D);
		return formatString(this.prefx, 2, '0')
				+ time
				+ formatString(new StringBuilder(String.valueOf(serial))
						.toString(), 3, '0')
				+ formatString(new StringBuilder(String.valueOf(random))
						.toString(), 4, '0');
	}

	private static String formatString(String a, int length, char b) {
		if (a == null) {
			return "";
		}

		if (a.length() > length) {
			return a.substring(0, length);
		}
		if (a.length() == length) {
			return a;
		}
		StringBuffer buf = new StringBuffer(length);
		buf.append(a);
		for (int i = 0; i < length - a.length(); ++i) {
			buf.append(b);
		}
		return buf.toString();
	}
}
