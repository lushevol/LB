package org.kylin.klb.entity.network;

public class Bond {
	
	private String name;
	private String algorithm;
	private String monitor;
	private String interMode;
	private String interval;
	private String ipList;
	private String interForBond;
	private String interBonded;
	private String state;
	private String description;
	private String addressList;
	private String mtu;
	private String mac;
	private String arpState;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAddressList() {
		return addressList;
	}
	public void setAddressList(String addressList) {
		this.addressList = addressList;
	}
	public String getMtu() {
		return mtu;
	}
	public void setMtu(String mtu) {
		this.mtu = mtu;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getArpState() {
		return arpState;
	}
	public void setArpState(String arpState) {
		this.arpState = arpState;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
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
	public String getInterMode() {
		return interMode;
	}
	public void setInterMode(String interMode) {
		this.interMode = interMode;
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
