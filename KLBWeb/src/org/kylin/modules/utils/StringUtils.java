package org.kylin.modules.utils;

import java.io.StringReader;

public class StringUtils extends org.apache.commons.lang.StringUtils {
	public static boolean isNotEmpty(String s) {
		return (s != null) && (!"".equals(s.trim()));
	}

	public static boolean isNotEmpty(String[] strings) {
		String[] arrayOfString = strings;
		int j = strings.length;
		for (int i = 0; i < j; ++i) {
			String string = arrayOfString[i];
			if (isEmpty(string))
				return false;
		}

		return true;
	}

	public static String delSpace(String str) {
		if (str == null) {
			return null;
		}

		String regStartSpace = "^[　 ]*";
		String regEndSpace = "[　 ]*$";
		String strDelSpace = str.replaceAll(regStartSpace, "").replaceAll(
				regEndSpace, "");

		return strDelSpace;
	}

	public static String formatString(String str) {
		StringReader sr = new StringReader(str);
		StringBuffer sb = new StringBuffer();
		try {
			int temp = sr.read();

			while (temp != -1) {

				if (temp < 32) {
					sb.append(' ');
				}

				else {
					sb.append((char) temp);
				}

				temp = sr.read();
			}

			return sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
