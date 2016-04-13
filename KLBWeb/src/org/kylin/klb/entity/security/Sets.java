package org.kylin.klb.entity.security;

public class Sets {
	private String service;
	private String guide;
	private String vipMark;
	private String tcpPorts;
	private String udpPorts;
	private String persistentNetmask;
	private String persistent = "0";
	private String serviceName;
	private String ip;
	private String forward;

	public String getPersistent() {
		return this.persistent;
	}

	public void setPersistent(String persistent) {
		this.persistent = persistent;
	}

	public String getGuide() {
		return this.guide;
	}

	public void setGuide(String guide) {
		this.guide = guide;
	}

	public String getVipMark() {
		return this.vipMark;
	}

	public void setVipMark(String vipMark) {
		this.vipMark = vipMark;
	}

	public String getTcpPorts() {
		return this.tcpPorts;
	}

	public void setTcpPorts(String tcpPorts) {
		this.tcpPorts = tcpPorts;
	}

	public String getPersistentNetmask() {
		return this.persistentNetmask;
	}

	public void setPersistentNetmask(String persistentNetmask) {
		this.persistentNetmask = persistentNetmask;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getForward() {
		return this.forward;
	}

	public void setForward(String forward) {
		this.forward = forward;
	}

	public String getUdpPorts() {
		return this.udpPorts;
	}

	public void setUdpPorts(String udpPorts) {
		this.udpPorts = udpPorts;
	}

	public String getService() {
		return this.service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getServiceName() {
		return this.serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
}
