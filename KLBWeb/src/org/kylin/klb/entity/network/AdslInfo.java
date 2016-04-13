package org.kylin.klb.entity.network;

public class AdslInfo {
	private String inter;
	private String describe;
	private String status;
	private String rx;
	private String tx;
	private String ip;
	private String oppIp;
	private String persist;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRx() {
		return rx;
	}
	public void setRx(String rx) {
		this.rx = rx;
	}
	public String getTx() {
		return tx;
	}
	public void setTx(String tx) {
		this.tx = tx;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getOppIp() {
		return oppIp;
	}
	public void setOppIp(String oppIp) {
		this.oppIp = oppIp;
	}
	public String getPersist() {
		return persist;
	}
	public void setPersist(String persist) {
		this.persist = persist;
	}
	public String getInter() {
		return inter;
	}
	public void setInter(String inter) {
		this.inter = inter;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
}
