package org.kylin.klb.entity.linkload;

public class DnsServer {
	private String state;	
	private String port;
	private String reverse;
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getReverse() {
		return reverse;
	}
	public void setReverse(String reverse) {
		this.reverse = reverse;
	}
	
}
