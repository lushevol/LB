package org.kylin.klb.sysInfo;

import com.sun.management.OperatingSystemMXBean;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.StringTokenizer;
import java.lang.management.ManagementFactory;

public class MonitorServiceImpl implements IMonitorService {
	private static final int CPUTIME = 30;
	private static final int PERCENT = 100;
	private static final int FAULTLENGTH = 10;
	private static final File versionFile = new File("/proc/version");
	private static String linuxVersion = null;

	public MonitorInfoBean getMonitorInfoBean() throws Exception {
		int kb = 1024;

		long totalMemory = Runtime.getRuntime().totalMemory() / kb;

		long freeMemory = Runtime.getRuntime().freeMemory() / kb;

		long maxMemory = Runtime.getRuntime().maxMemory() / kb;

		OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory
				.getOperatingSystemMXBean();

		String osName = System.getProperty("os.name");

		long totalMemorySize = osmxb.getTotalPhysicalMemorySize() / kb;

		long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize() / kb;

		long usedMemory = (osmxb.getTotalPhysicalMemorySize() - osmxb
				.getFreePhysicalMemorySize())
				/ kb;

		for (ThreadGroup parentThread = Thread.currentThread().getThreadGroup(); parentThread
				.getParent() != null;) {
			parentThread = parentThread.getParent();
		}
		ThreadGroup parentThread = Thread.currentThread().getThreadGroup();
		int totalThread = parentThread.activeCount();

		double cpuRatio = 0.0D;
		if (osName.toLowerCase().startsWith("windows"))
			cpuRatio = getCpuRatioForWindows();
		else {
			cpuRatio = getCpuRateForLinux();
		}

		MonitorInfoBean infoBean = new MonitorInfoBean();
		infoBean.setFreeMemory(freeMemory);
		infoBean.setFreePhysicalMemorySize(freePhysicalMemorySize);
		infoBean.setMaxMemory(maxMemory);
		infoBean.setOsName(osName);
		infoBean.setTotalMemory(totalMemory);
		infoBean.setTotalMemorySize(totalMemorySize);
		infoBean.setTotalThread(totalThread);
		infoBean.setUsedMemory(usedMemory);
		infoBean.setCpuRatio(cpuRatio);
		return infoBean;
	}

	private static double getCpuRateForLinux() {
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader brStat = null;
		StringTokenizer tokenStat = null;
		try {
			System.out.println("Get usage rate of CUP , linux version: "
					+ linuxVersion);

			Process process = Runtime.getRuntime().exec("top -b -n 1");
			is = process.getInputStream();
			isr = new InputStreamReader(is);
			brStat = new BufferedReader(isr);
			double d;
			if (linuxVersion.equals("2.4")) {
				brStat.readLine();
				brStat.readLine();
				brStat.readLine();
				brStat.readLine();

				tokenStat = new StringTokenizer(brStat.readLine());
				tokenStat.nextToken();
				tokenStat.nextToken();
				String user = tokenStat.nextToken();
				tokenStat.nextToken();
				String system = tokenStat.nextToken();
				tokenStat.nextToken();
				String nice = tokenStat.nextToken();

				user = user.substring(0, user.indexOf("%"));
				system = system.substring(0, system.indexOf("%"));
				nice = nice.substring(0, nice.indexOf("%"));

				float userUsage = new Float(user).floatValue();
				float systemUsage = new Float(system).floatValue();
				float niceUsage = new Float(nice).floatValue();

				d = (userUsage + systemUsage + niceUsage) / 100.0F;
				return d;
			}
			brStat.readLine();
			brStat.readLine();

			tokenStat = new StringTokenizer(brStat.readLine());
			tokenStat.nextToken();
			tokenStat.nextToken();
			tokenStat.nextToken();
			tokenStat.nextToken();
			tokenStat.nextToken();
			tokenStat.nextToken();
			tokenStat.nextToken();
			String cpuUsage = tokenStat.nextToken();

			Float usage = new Float(cpuUsage
					.substring(0, cpuUsage.indexOf("%")));

			return 1.0F - usage.floatValue() / 100.0F;
		} catch (IOException ioe) {

			freeResource(is, isr, brStat);
			return 1.0D;
		} finally {
			freeResource(is, isr, brStat);
		}
	}

	private static void freeResource(InputStream is, InputStreamReader isr,
			BufferedReader br) {
		try {
			if (is != null)
				is.close();
			if (isr != null)
				isr.close();
			if (br != null)
				br.close();
		} catch (IOException ioe) {

		}
	}

	private double getCpuRatioForWindows() {
		try {
			String procCmd = System.getenv("windir")
					+ "\\system32\\wbem\\wmic.exe process get Caption,CommandLine,"
					+ "KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";

			long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd));
			Thread.sleep(30L);
			long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd));
			if ((c0 != null) && (c1 != null)) {
				long idletime = c1[0] - c0[0];
				long busytime = c1[1] - c0[1];
				return Double.valueOf(100L * busytime / (busytime + idletime))
						.doubleValue();
			}
			return 0.0D;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0.0D;
	}

	private long[] readCpu(Process proc) {
		long[] retn = new long[2];
		try {
			proc.getOutputStream().close();
			InputStreamReader ir = new InputStreamReader(proc.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			String line = input.readLine();
			if ((line == null) || (line.length() < 10)) {
				return null;
			}
			int capidx = line.indexOf("Caption");
			int cmdidx = line.indexOf("CommandLine");
			int rocidx = line.indexOf("ReadOperationCount");
			int umtidx = line.indexOf("UserModeTime");
			int kmtidx = line.indexOf("KernelModeTime");
			int wocidx = line.indexOf("WriteOperationCount");
			long idletime = 0L;
			long kneltime = 0L;
			long usertime = 0L;
			while ((line = input.readLine()) != null) {
				if (line.length() < wocidx) {
					continue;
				}

				String caption = Bytes.substring(line, capidx, cmdidx - 1)
						.trim();
				String cmd = Bytes.substring(line, cmdidx, kmtidx - 1).trim();
				if (cmd.indexOf("wmic.exe") >= 0) {
					continue;
				}

				if ((caption.equals("System Idle Process"))
						|| (caption.equals("System"))) {
					idletime = idletime
							+ Long.valueOf(
									Bytes.substring(line, kmtidx, rocidx - 1)
											.trim()).longValue();

					idletime = idletime
							+ Long.valueOf(
									Bytes.substring(line, umtidx, wocidx - 1)
											.trim()).longValue();
				} else {
					kneltime = kneltime
							+ Long.valueOf(
									Bytes.substring(line, kmtidx, rocidx - 1)
											.trim()).longValue();

					usertime = usertime
							+ Long.valueOf(
									Bytes.substring(line, umtidx, wocidx - 1)
											.trim()).longValue();
				}
			}
			retn[0] = idletime;
			retn[1] = (kneltime + usertime);
			return retn;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				proc.getInputStream().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		IMonitorService service = new MonitorServiceImpl();
		MonitorInfoBean monitorInfo = service.getMonitorInfoBean();

	}
}
