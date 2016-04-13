package org.kylin.klb.entity.network;

public class BondAttribute {
	
	private String name;
	private String description;
	private String state;
	private String dhcp;
	private String interMode;
	private String addressList;
	private String mtu;
	private String mac;
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
	public String getInterMode() {
		return interMode;
	}
	public void setInterMode(String interMode) {
		this.interMode = interMode;
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
	public String getDhcp() {
		return dhcp;
	}
	public void setDhcp(String dhcp) {
		this.dhcp = dhcp;
	}
}
