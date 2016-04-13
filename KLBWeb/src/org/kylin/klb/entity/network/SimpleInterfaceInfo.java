package org.kylin.klb.entity.network;

public class SimpleInterfaceInfo {
	
	private String name;
	private String description;
	private String state;
	private String mode;
	private String addr;
	private String mtu;
	private String connect;
	private String negotiation;
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
	
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getMtu() {
		return mtu;
	}
	public void setMtu(String mtu) {
		this.mtu = mtu;
	}
	public String getConnect() {
		return connect;
	}
	public void setConnect(String connect) {
		this.connect = connect;
	}
	public String getNegotiation() {
		return negotiation;
	}
	public void setNegotiation(String negotiation) {
		this.negotiation = negotiation;
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
	
	
}
