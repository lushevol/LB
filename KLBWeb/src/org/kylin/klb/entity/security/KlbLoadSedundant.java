package org.kylin.klb.entity.security;

import org.kylin.klb.entity.IdEntity;

public class KlbLoadSedundant extends IdEntity {
	private String clustername;
	private String udpport;
	private String ucast;
	private String keepalive;
	private String warntime;
	private String deadtime;
	private String inittime;
	private String radio2;

	public String getClustername() {
		return this.clustername;
	}

	public void setClustername(String clustername) {
		this.clustername = clustername;
	}

	public String getUdpport() {
		return this.udpport;
	}

	public void setUdpport(String udpport) {
		this.udpport = udpport;
	}

	public String getUcast() {
		return this.ucast;
	}

	public void setUcast(String ucast) {
		this.ucast = ucast;
	}

	public String getKeepalive() {
		return this.keepalive;
	}

	public void setKeepalive(String keepalive) {
		this.keepalive = keepalive;
	}

	public String getWarntime() {
		return this.warntime;
	}

	public void setWarntime(String warntime) {
		this.warntime = warntime;
	}

	public String getDeadtime() {
		return this.deadtime;
	}

	public void setDeadtime(String deadtime) {
		this.deadtime = deadtime;
	}

	public String getInittime() {
		return this.inittime;
	}

	public void setInittime(String inittime) {
		this.inittime = inittime;
	}

	public String getRadio2() {
		return this.radio2;
	}

	public void setRadio2(String radio2) {
		this.radio2 = radio2;
	}
}
