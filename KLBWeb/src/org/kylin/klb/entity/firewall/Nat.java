package org.kylin.klb.entity.firewall;

public class Nat {	
	private String id;
	private String type;
	private String srcIP;	
	private String destIP;	
	private String protocol;
	private String srcPort;
	private String destPort;	
	private String interfaces;
	private String describe;
	private String startIP;
	private String endIP;
	private String startPort;
	private String endPort;
	private String status;
	private String enabled;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSrcIP() {
		return srcIP;
	}
	public void setSrcIP(String srcIP) {
		this.srcIP = srcIP;
	}
	public String getDestIP() {
		return destIP;
	}
	public void setDestIP(String destIP) {
		this.destIP = destIP;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getSrcPort() {
		return srcPort;
	}
	public void setSrcPort(String srcPort) {
		this.srcPort = srcPort;
	}
	public String getDestPort() {
		return destPort;
	}
	public void setDestPort(String destPort) {
		this.destPort = destPort;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public String getStartIP() {
		return startIP;
	}
	public void setStartIP(String startIP) {
		this.startIP = startIP;
	}
	public String getEndIP() {
		return endIP;
	}
	public void setEndIP(String endIP) {
		this.endIP = endIP;
	}
	public String getStartPort() {
		return startPort;
	}
	public void setStartPort(String startPort) {
		this.startPort = startPort;
	}
	public String getEndPort() {
		return endPort;
	}
	public void setEndPort(String endPort) {
		this.endPort = endPort;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	public String getInterfaces() {
		return interfaces;
	}
	public void setInterfaces(String interfaces) {
		this.interfaces = interfaces;
	}
		
}
