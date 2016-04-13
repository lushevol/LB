package org.kylin.klb.sysInfo;

public class MonitorInfoBean {
	private long totalMemory;
	private long freeMemory;
	private long maxMemory;
	private String osName;
	private long totalMemorySize;
	private long freePhysicalMemorySize;
	private long usedMemory;
	private int totalThread;
	private double cpuRatio;

	public long getFreeMemory() {
		return this.freeMemory;
	}

	public void setFreeMemory(long freeMemory) {
		this.freeMemory = freeMemory;
	}

	public long getFreePhysicalMemorySize() {
		return this.freePhysicalMemorySize;
	}

	public void setFreePhysicalMemorySize(long freePhysicalMemorySize) {
		this.freePhysicalMemorySize = freePhysicalMemorySize;
	}

	public long getMaxMemory() {
		return this.maxMemory;
	}

	public void setMaxMemory(long maxMemory) {
		this.maxMemory = maxMemory;
	}

	public String getOsName() {
		return this.osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public long getTotalMemory() {
		return this.totalMemory;
	}

	public void setTotalMemory(long totalMemory) {
		this.totalMemory = totalMemory;
	}

	public long getTotalMemorySize() {
		return this.totalMemorySize;
	}

	public void setTotalMemorySize(long totalMemorySize) {
		this.totalMemorySize = totalMemorySize;
	}

	public int getTotalThread() {
		return this.totalThread;
	}

	public void setTotalThread(int totalThread) {
		this.totalThread = totalThread;
	}

	public long getUsedMemory() {
		return this.usedMemory;
	}

	public void setUsedMemory(long usedMemory) {
		this.usedMemory = usedMemory;
	}

	public double getCpuRatio() {
		return this.cpuRatio;
	}

	public void setCpuRatio(double cpuRatio) {
		this.cpuRatio = cpuRatio;
	}
}
