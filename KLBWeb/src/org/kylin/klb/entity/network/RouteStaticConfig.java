package org.kylin.klb.entity.network;

public class RouteStaticConfig {
	private String id;
	private String ip;
	private String ipTemp;
	private String describe;
	private String status;
	private String gates;
	private String gatePolicy;
	private String metric;
	private String metricTemp;
	private String displayGates;
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}	
	public String getMetric() {
		return metric;
	}
	public void setMetric(String metric) {
		this.metric = metric;
	}
	
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
	public String getGatePolicy() {
		return gatePolicy;
	}
	public void setGatePolicy(String gatePolicy) {
		this.gatePolicy = gatePolicy;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public String getIpTemp() {
		return ipTemp;
	}
	public void setIpTemp(String ipTemp) {
		this.ipTemp = ipTemp;
	}
	public String getMetricTemp() {
		return metricTemp;
	}
	public void setMetricTemp(String metricTemp) {
		this.metricTemp = metricTemp;
	}
}
