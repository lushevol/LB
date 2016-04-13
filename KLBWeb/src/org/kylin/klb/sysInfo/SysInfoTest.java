package org.kylin.klb.sysInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.StringTokenizer;

public class SysInfoTest {
	public static void main(String[] a) throws IOException {
		String path = "D:\\\\springside\\\\klb\\src\\test\\java\\org\\ceopen\\klb\\sysInfo\\";
		File file = new File(path + "mem.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(file)));
		String str = null;
		StringTokenizer token = null;
		while ((str = br.readLine()) != null) {
			if (str.indexOf("Mem:") != -1) {
				String[] arr = str.split(" ");
				token = new StringTokenizer(str);
				token.nextToken();

				token.nextToken();

				token.nextToken();

			}

			if (str.indexOf("Swap:") != -1) {
				String[] arr = str.split(" ");
				token = new StringTokenizer(str);
				token.nextToken();

				token.nextToken();

			}
		}
	}
}
