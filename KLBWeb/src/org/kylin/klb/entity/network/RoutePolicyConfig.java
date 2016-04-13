package org.kylin.klb.entity.network;

public class RoutePolicyConfig {
	private String id;
	private String status;
	private String describe;
	private String rules;
	private String gates;
	private String gatePolicy;
	private String displayGates;
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
	public String getRules() {
		return rules;
	}
	public void setRules(String rules) {
		this.rules = rules;
	}
	public String getGates() {
		return gates;
	}
	public void setGates(String gates) {
		this.gates = gates;
	}
	public String getDisplayGates() {
		return displayGates;
	}
	public void setDisplayGates(String displayGates) {
		this.displayGates = displayGates;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public String getGatePolicy() {
		return gatePolicy;
	}
	public void setGatePolicy(String gatePolicy) {
		this.gatePolicy = gatePolicy;
	}
	
}
