package org.kylin.klb.entity.network;

public class BondInfo {
	
	private String name;
	private String algorithm;
	private String monitor;
	private String interBonded;
	private String interMode;
	private String ipAddr;
	private String state;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAlgorithm() {
		return algorithm;
	}
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
	public String getMonitor() {
		return monitor;
	}
	public void setMonitor(String monitor) {
		this.monitor = monitor;
	}
	public String getInterBonded() {
		return interBonded;
	}
	public void setInterBonded(String interBonded) {
		this.interBonded = interBonded;
	}
	public String getInterMode() {
		return interMode;
	}
	public void setInterMode(String interMode) {
		this.interMode = interMode;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getIpAddr() {
		return ipAddr;
	}
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
}
