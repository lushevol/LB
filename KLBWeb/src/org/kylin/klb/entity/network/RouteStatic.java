package org.kylin.klb.entity.network;

public class RouteStatic {
	private String id;
	private String ip;
	private String mask;
	private String status;
	private String gate;
	private String gatePolicy;
	private String inter;
	private String metric;
	private String weight;
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getMask() {
		return mask;
	}
	public void setMask(String mask) {
		this.mask = mask;
	}
	public String getGate() {
		return gate;
	}
	public void setGate(String gate) {
		this.gate = gate;
	}
	public String getInter() {
		return inter;
	}
	public void setInter(String inter) {
		this.inter = inter;
	}
	public String getMetric() {
		return metric;
	}
	public void setMetric(String metric) {
		this.metric = metric;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
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
	public String getGatePolicy() {
		return gatePolicy;
	}
	public void setGatePolicy(String gatePolicy) {
		this.gatePolicy = gatePolicy;
	}
}
