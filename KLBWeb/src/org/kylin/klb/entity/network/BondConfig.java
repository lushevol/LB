package org.kylin.klb.entity.network;

public class BondConfig {
	
	private String name;
	private String algorithm;
	private String monitor;
	private String interval;
	private String ipList;
	private String interForBond;
	private String interBonded;
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
	public String getIpList() {
		return ipList;
	}
	public void setIpList(String ipList) {
		this.ipList = ipList;
	}
	public String getInterForBond() {
		return interForBond;
	}
	public void setInterForBond(String interForBond) {
		this.interForBond = interForBond;
	}
	public String getInterBonded() {
		return interBonded;
	}
	public void setInterBonded(String interBonded) {
		this.interBonded = interBonded;
	}
	public String getInterval() {
		return interval;
	}
	public void setInterval(String interval) {
		this.interval = interval;
	}
}
