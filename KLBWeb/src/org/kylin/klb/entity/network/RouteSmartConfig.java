package org.kylin.klb.entity.network;

public class RouteSmartConfig  {
	private String id;
	private String status;
	private String describe;
	private String ispName;
	private String gates;
	private String gatePolicy;
	private String displayGates;
	
	/*add by yjp 2012/02/28*/
	private String mode;
	private String modeName;
	private String ip;
	private String port;
	private String frequcency;
	private String timeout;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public String getGates() {
		return gates;
	}
	public void setGates(String gates) {
		this.gates = gates;
	}
	public String getGatePolicy() {
		return gatePolicy;
	}
	public void setGatePolicy(String gatePolicy) {
		this.gatePolicy = gatePolicy;
	}
	public String getDisplayGates() {
		return displayGates;
	}
	public void setDisplayGates(String displayGates) {
		this.displayGates = displayGates;
	}
	public String getIspName() {
		return ispName;
	}
	public void setIspName(String ispName) {
		this.ispName = ispName;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getFrequcency() {
		return frequcency;
	}
	public void setFrequcency(String frequcency) {
		this.frequcency = frequcency;
	}
	public String getTimeout() {
		return timeout;
	}
	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}
	public String getModeName() {
		return modeName;
	}
	public void setModeName(String modeName) {
		this.modeName = modeName;
	}	
	
}
