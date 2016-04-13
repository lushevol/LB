package org.kylin.klb.entity.security;

public class SystemHard {
	private int cpuNum = 0;
	private float cpuSpeed = 0;
	private double cpuUsage = 0.0D;

	private double memTotal = 0.0D;
	private double memUsed = 0.0D;
	private double memUsage = 0.0D;
	private double memFree = 0.0D;
	private double memSwapUsed = 0.0D;

	private double hdTotal = 0.0D;
	private double hdUsed = 0.0D;
	private double hdFree = 0.0D;
	private double hdUsage = 0.0D;

	public double getHdTotal() {
		return this.hdTotal;
	}

	public void setHdTotal(double hdTotal) {
		this.hdTotal = hdTotal;
	}

	public int getCpuNum() {
		return this.cpuNum;
	}

	public void setCpuNum(int cpuNum) {
		this.cpuNum = cpuNum;
	}

	public float getCpuSpeed() {
		return cpuSpeed;
	}

	public void setCpuSpeed(float cpuSpeed) {
		this.cpuSpeed = cpuSpeed;
	}

	public double getMemTotal() {
		return this.memTotal;
	}

	public void setMemTotal(double memTotal) {
		this.memTotal = memTotal;
	}

	public double getMemUsed() {
		return this.memUsed;
	}

	public void setMemUsed(double memUsed) {
		this.memUsed = memUsed;
	}

	public double getHdUsed() {
		return this.hdUsed;
	}

	public void setHdUsed(double hdUsed) {
		this.hdUsed = hdUsed;
	}

	public double getCpuUsage() {
		return this.cpuUsage;
	}

	public void setCpuUsage(double cpuUsage) {
		this.cpuUsage = cpuUsage;
	}

	public double getMemUsage() {
		return this.memUsage;
	}

	public void setMemUsage(double memUsage) {
		this.memUsage = memUsage;
	}

	public double getHdUsage() {
		return this.hdUsage;
	}

	public void setHdUsage(double hdUsage) {
		this.hdUsage = hdUsage;
	}

	public double getMemSwapUsed() {
		return this.memSwapUsed;
	}

	public void setMemSwapUsed(double memSwapUsed) {
		this.memSwapUsed = memSwapUsed;
	}

	public static String toParamXml(String hard) {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<Root>"
				+ "<System>"
				+ "<Status>"
				+ new StringBuilder("<").append(hard).append(" get=\"1\" />")
						.toString() + "</Status>" + "</System>" + "</Root>";
	}

	public double getHdFree() {
		return this.hdFree;
	}

	public void setHdFree(int hdFree) {
		this.hdFree = hdFree;
	}

	public double getMemFree() {
		return this.memFree;
	}

	public void setMemFree(double memFree) {
		this.memFree = memFree;
	}

	public void setHdFree(double hdFree) {
		this.hdFree = hdFree;
	}
}
