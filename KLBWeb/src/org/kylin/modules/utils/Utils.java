package org.kylin.modules.utils;

import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	public static boolean isDouble(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?\\d+(\\.\\d*)?|\\.\\d+$");

		return pattern.matcher(str).matches();
	}
}
