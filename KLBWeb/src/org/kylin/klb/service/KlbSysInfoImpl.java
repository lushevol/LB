package org.kylin.klb.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.lang.math.NumberUtils;
import org.kylin.klb.entity.security.Director;
import org.kylin.klb.entity.security.SystemHard;
import org.springframework.stereotype.Service;

@Service
public class KlbSysInfoImpl {
	public SystemHard getDeskUsage() throws Exception {
		String osName = System.getProperty("os.name");
		SystemHard systemHard = new SystemHard();

		if (!osName.toLowerCase().startsWith("windows")) {
			double totalHD = 0.0D;
			double usedHD = 0.0D;
			Runtime rt = Runtime.getRuntime();
			Process p = rt.exec("df -hl");
			BufferedReader in = null;
			try {
				in = new BufferedReader(new InputStreamReader(p
						.getInputStream()));
				String str = null;
				String[] strArray = (String[]) null;
				while ((str = in.readLine()) != null) {
					int m = 0;
					strArray = str.split(" ");
					for (String tmp : strArray) {
						if (tmp.trim().length() == 0)
							continue;
						++m;
						if (tmp.indexOf("G") != -1) {
							if ((m == 2) && (!tmp.equals(""))
									&& (!tmp.equals("0"))) {
								totalHD = totalHD
										+ Double.parseDouble(tmp.substring(0,
												tmp.length() - 1)) * 1024.0D;
							}
							if ((m == 3) && (!tmp.equals("none"))
									&& (!tmp.equals("0"))) {
								usedHD = usedHD
										+ Double.parseDouble(tmp.substring(0,
												tmp.length() - 1)) * 1024.0D;
							}
						}
						if (tmp.indexOf("M") != -1) {
							if ((m == 2) && (!tmp.equals(""))
									&& (!tmp.equals("0"))) {
								totalHD = totalHD
										+ Double.parseDouble(tmp.substring(0,
												tmp.length() - 1));
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
			systemHard.setHdUsage(NumberUtils.toInt(new DecimalFormat("#")
					.format(usedHD / totalHD * 100.0D)));
			systemHard.setHdTotal(NumberUtils.toInt(new DecimalFormat("#")
					.format(totalHD)));
			systemHard.setHdUsed(NumberUtils.toInt(new DecimalFormat("#")
					.format(usedHD)));
		}
		return systemHard;
	}

	public double getCpuUsage() throws Exception {
		String osName = System.getProperty("os.name");
		if (!osName.toLowerCase().startsWith("windows")) {
			double cpuUsed = 0.0D;
			double idleUsed = 0.0D;
			Runtime rt = Runtime.getRuntime();
			Process p = rt.exec("top -i -n 1");
			BufferedReader in = null;
			try {
				in = new BufferedReader(new InputStreamReader(p
						.getInputStream()));
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
		return 0.0D;
	}

	public SystemHard getMemInfo() throws Exception {
		String osName = System.getProperty("os.name");
		SystemHard systemHard = new SystemHard();
		if (!osName.toLowerCase().startsWith("windows")) {
			long memUsed = 0L;
			long memTotal = 0L;
			double memUsage = 0.0D;
			Runtime rt = Runtime.getRuntime();
			Process p = rt.exec("top -b -n 1");
			BufferedReader in = null;
			try {
				in = new BufferedReader(new InputStreamReader(p
						.getInputStream()));
				String str = null;
				StringTokenizer token = null;
				int linecount = 0;
				while ((str = in.readLine()) != null) {
					++linecount;
					if (linecount == 4) {
						String[] s = str.split("k ");
						String memUsedstr = s[1];
						String memTotalstr = s[0];
						String[] memUsedstr1 = memUsedstr.split(" ");
						memUsed = Long
								.parseLong(memUsedstr1[(memUsedstr1.length - 1)]);
						String[] memTotalstr1 = memTotalstr.split(" ");
						memTotal = Long
								.parseLong(memTotalstr1[(memTotalstr1.length - 1)]);
						systemHard.setMemUsed(NumberUtils
								.toInt(new DecimalFormat("#")
										.format(memUsed / 1024L)));
						systemHard.setMemTotal(NumberUtils
								.toInt(new DecimalFormat("#")
										.format(memTotal / 1024L)));
						memUsage = memUsed * 100L / memTotal;
						systemHard.setMemUsage(memUsage);
					}

					if (linecount == 5) {
						String[] s = str.split("k ");
						String memSwapUsed = s[1];
						String[] memSwapUsedstr = memSwapUsed.split(" ");
						memSwapUsed = memSwapUsedstr[(memSwapUsedstr.length - 1)];

						systemHard.setMemSwapUsed(NumberUtils.toDouble(new DecimalFormat("#.##")
									.format(NumberUtils.toDouble(memSwapUsed, 0.0D) / 1024.0D)));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				in.close();
			}
		}
		return systemHard;
	}

	public String[] getCpuInfo() throws IOException, InterruptedException {
		String osName = System.getProperty("os.name");
		String[] cpuInfo = { "0", "0" };
		BufferedReader br = null;
		if (!osName.toLowerCase().startsWith("windows")) {
			try {
				File file = new File("/proc/cpuinfo");
				br = new BufferedReader(new InputStreamReader(
						new FileInputStream(file)));

				String str = null;
				int cpu = 0;
				int cpuSpeed = 0;
				while ((str = br.readLine()) != null) {
					if (str.indexOf("processor") != -1) {
						++cpu;
					}
					if (str.indexOf("cpu MHz") != -1) {
						String[] arr = str.split(":");
						if ((arr != null) && (arr.length > 0)
								&& (cpuSpeed == 0)) {
							cpuInfo[1] = new DecimalFormat("#")
									.format(NumberUtils.toFloat(arr[1], 0.0F));
						}
					}
					cpuInfo[0] = String.valueOf(cpu);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				br.close();
			}
		}
		return cpuInfo;
	}

	public SystemHard getSysInfo() throws Exception {
		KlbManager klb = new KlbManager();
		SystemHard systemHard = new SystemHard();

		SystemHard memHard = klb.getMemInfo();
		systemHard.setMemFree(memHard.getMemFree());
		systemHard.setMemTotal(memHard.getMemTotal());
		systemHard.setMemUsed(memHard.getMemUsed());
		systemHard.setMemUsage(memHard.getMemUsage());
		
		SystemHard cpuHard = klb.getCpuInfo();
		systemHard.setCpuNum(cpuHard.getCpuNum());
		systemHard.setCpuSpeed(cpuHard.getCpuSpeed());		
		systemHard.setCpuUsage(cpuHard.getCpuUsage());

		SystemHard diskHard = klb.getDiskInfo();
		systemHard.setHdUsage(diskHard.getHdUsage());
		systemHard.setHdTotal(diskHard.getHdTotal());
		systemHard.setHdUsed(diskHard.getHdUsed());

		return systemHard;
	}

	public List<Director> getDirectorInfo() throws Exception {
		KlbManager klb = new KlbManager();
		return klb.getDirectorInfo();
	}
}
