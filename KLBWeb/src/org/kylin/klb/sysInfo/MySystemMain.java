package org.kylin.klb.sysInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class MySystemMain {
	public static double getCpuUsage() throws Exception {
		double cpuUsed = 0.0D;
		double idleUsed = 0.0D;
		Runtime rt = Runtime.getRuntime();
		Process p = rt.exec("top -b -n 1");
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String str = null;
			int linecount = 0;

			while ((str = in.readLine()) != null) {
				++linecount;
				if (linecount == 3) {
					String[] s = str.split("%");
					String idlestr = s[3];
					String[] idlestr1 = idlestr.split(" ");
					idleUsed = Double
							.parseDouble(idlestr1[(idlestr1.length - 1)]);
					cpuUsed = 100.0D - idleUsed;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			in.close();
		}
		return cpuUsed;
	}

	public static double getMemUsage() throws Exception {
		long memUsed = 0L;
		long memTotal = 0L;
		double memUsage = 0.0D;
		Runtime rt = Runtime.getRuntime();
		Process p = rt.exec("top -b -n 1");
		BufferedReader in = null;

		in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String str = null;
		int linecount = 0;

		while ((str = in.readLine()) != null) {
			++linecount;
			if (linecount == 4) {
				String[] s = str.split("k ");
				String memUsedstr = s[1];
				String memTotalstr = s[0];
				String[] memUsedstr1 = memUsedstr.split(" ");
				memUsed = Long.parseLong(memUsedstr1[(memUsedstr1.length - 1)]);
				String[] memTotalstr1 = memTotalstr.split(" ");
				memTotal = Long
						.parseLong(memTotalstr1[(memTotalstr1.length - 1)]);
				memUsage = memUsed * 100L / memTotal;
				break;
			}

		}

		return memUsage;
	}

	public static void main(String[] args) {
		try {

		} catch (Exception localException) {
		}
	}
}
