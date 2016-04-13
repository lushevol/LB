package org.kylin.klb.entity.network;

public class InterfaceInfo {
	
	private String name;
	private String description;
	private String state;
	private String dhcp;
	private String interfaceMode;
	private String ipList;
	private String mtu;
	private String mac;
	private String connect;
	private String negotiation;
	private String doubleMode;
	private String speed;
	private String arpState;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getInterfaceMode() {
		return interfaceMode;
	}
	public void setInterfaceMode(String interfaceMode) {
		this.interfaceMode = interfaceMode;
	}
	public String getIpList() {
		return ipList;
	}
	public void setIpList(String ipList) {
		this.ipList = ipList;
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
	public String getConnect() {
		return connect;
	}
	public void setConnect(String connect) {
		this.connect = connect;
	}
	public String getDoubleMode() {
		return doubleMode;
	}
	public void setDoubleMode(String doubleMode) {
		this.doubleMode = doubleMode;
	}
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	public String getArpState() {
		return arpState;
	}
	public void setArpState(String arpState) {
		this.arpState = arpState;
	}
	public String getNegotiation() {
		return negotiation;
	}
	public void setNegotiation(String negotiation) {
		this.negotiation = negotiation;
	}
	public String getDhcp() {
		return dhcp;
	}
	public void setDhcp(String dhcp) {
		this.dhcp = dhcp;
	}
}
