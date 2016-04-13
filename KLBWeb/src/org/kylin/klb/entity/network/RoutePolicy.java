package org.kylin.klb.entity.network;

public class RoutePolicy {
	private String id;
	private String srcNet;
	private String destNet;
	private String protocol;
	private String srcPort;
	private String destPort;
	private String status;
	private String gate;
	private String inter;
	private String weight;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSrcNet() {
		return srcNet;
	}
	public void setSrcNet(String srcNet) {
		this.srcNet = srcNet;
	}
	public String getDestNet() {
		return destNet;
	}
	public void setDestNet(String destNet) {
		this.destNet = destNet;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	
}
