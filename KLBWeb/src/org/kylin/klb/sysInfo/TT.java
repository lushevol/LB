package org.kylin.klb.sysInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class TT {
	public double getCpuUsage() throws Exception {
		double cpuUsed = 0.0D;
		Runtime rt = Runtime.getRuntime();
		Process p = rt.exec("top -b -n 1");
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String str = null;
			String[] strArray = (String[]) null;
			while ((str = in.readLine()) != null) {
				int m = 0;
				if (str.indexOf(" R ") != -1) {
					strArray = str.split(" ");
					for (String tmp : strArray) {
						if (tmp.trim().length() == 0)
							continue;
						if (++m != 9)
							continue;
						cpuUsed += Double.parseDouble(tmp);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			in.close();
		}
		return cpuUsed;
	}

	public double getMemUsage() throws Exception {
		double menUsed = 0.0D;
		Runtime rt = Runtime.getRuntime();
		Process p = rt.exec("top -b -n 1");
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String str = null;
			String[] strArray = (String[]) null;

			while ((str = in.readLine()) != null) {
				int m = 0;

				if (str.indexOf(" R ") == -1) {
					continue;
				}
				strArray = str.split(" ");
				for (String tmp : strArray) {
					if (tmp.trim().length() == 0)
						continue;
					if (++m != 10)
						continue;
					menUsed += Double.parseDouble(tmp);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			in.close();
		}
		return menUsed;
	}

	public double getDeskUsage() throws Exception {
		double totalHD = 0.0D;
		double usedHD = 0.0D;
		Runtime rt = Runtime.getRuntime();
		Process p = rt.exec("df -hl");

		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String str = null;
			String[] strArray = (String[]) null;
			int flag = 0;
			while ((str = in.readLine()) != null) {
				int m = 0;

				strArray = str.split(" ");
				for (String tmp : strArray) {
					if (tmp.trim().length() == 0)
						continue;
					++m;

					if (tmp.indexOf("G") != -1) {
						if ((m == 2) && (!tmp.equals("")) && (!tmp.equals("0"))) {
							totalHD = totalHD
									+ Double.parseDouble(tmp.substring(0, tmp
											.length() - 1)) * 1024.0D;
						}
						if ((m == 3) && (!tmp.equals("none"))
								&& (!tmp.equals("0"))) {
							usedHD = usedHD
									+ Double.parseDouble(tmp.substring(0, tmp
											.length() - 1)) * 1024.0D;
						}
					}
					if (tmp.indexOf("M") != -1) {
						if ((m == 2) && (!tmp.equals("")) && (!tmp.equals("0"))) {
							totalHD = totalHD
									+ Double.parseDouble(tmp.substring(0, tmp
											.length() - 1));
						}
						if ((m != 3) || (tmp.equals("none"))
								|| (tmp.equals("0")))
							continue;
						usedHD = usedHD
								+ Double.parseDouble(tmp.substring(0, tmp
										.length() - 1));
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			in.close();
		}
		return usedHD / totalHD * 100.0D;
	}

	public static void main(String[] args) throws Exception {
		TT cpu = new TT();
		System.out
				.println("---------------cpu used:" + cpu.getCpuUsage() + "%");
		System.out
				.println("---------------mem used:" + cpu.getMemUsage() + "%");
		System.out
				.println("---------------HD used:" + cpu.getDeskUsage() + "%");

		Runtime lRuntime = Runtime.getRuntime();
		System.out.println("--------------Free Momery:" + lRuntime.freeMemory()
				+ "K");
		System.out.println("--------------Max Momery:" + lRuntime.maxMemory()
				+ "K");
		System.out.println("--------------Total Momery:"
				+ lRuntime.totalMemory() + "K");
		System.out.println("---------------Available Processors :"
				+ lRuntime.availableProcessors());
	}
}
