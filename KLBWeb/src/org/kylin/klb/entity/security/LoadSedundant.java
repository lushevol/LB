package org.kylin.klb.entity.security;

import org.kylin.klb.entity.IdEntity;

public class LoadSedundant extends IdEntity {
	private String self;
	private String other;
	private String interfaces;
	private String udpport;
	private String ucast;
	private String hostname;
	private String keepalive;
	private String warntime;
	private String deadtime;
	private String inittime;
	private String indirect;
	private String enabled;
	private String masterDevice;
	private String send;
	private String address;

	/* public String toXml() {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
				.append("<Root>").append("<Availability>");
		// if (StringUtils.isNotEmpty(getClustername())) {
			sb.append("<ClusterName value=\"").append(getClustername()).append(
					"\"").append(" set=\"1\" />");
		// }
		if (StringUtils.isNotEmpty(getDeadtime())) {
			sb.append("<Deadtime value=\"").append(getDeadtime()).append("\"")
					.append(" set=\"1\" />");
		}
		if (StringUtils.isNotEmpty(getInittime())) {
			sb.append("<Initdead value=\"").append(getInittime()).append("\"")
					.append(" set=\"1\" />");
		}
		if (StringUtils.isNotEmpty(getInterfaces())) {
			sb.append("<Interface value=\"").append(getInterfaces()).append(
					"\"").append(" set=\"1\" />");
		}
		if (StringUtils.isNotEmpty(getKeepalive())) {
			sb.append("<Keepalive value=\"").append(getKeepalive())
					.append("\"").append(" set=\"1\" />");
		}
		if (StringUtils.isNotEmpty(getIndirect())) {
			sb.append("<Autoback value=\"").append(getIndirect()).append("\"")
					.append(" set=\"1\" />");
		}
		if (StringUtils.isNotEmpty(getAvailable())) {
			sb.append("<Manager value=\"").append(getAvailable()).append("\"")
					.append(" set=\"1\" />");
		}
		if (StringUtils.isNotEmpty(getUcast())) {
			sb.append("<MulticastIP  value=\"").append(getUcast()).append("\"")
					.append(" set=\"1\" />");
		}
		if (StringUtils.isNotEmpty(getUdpport())) {
			sb.append("<Port  value=\"").append(getUdpport()).append("\"")
					.append(" set=\"1\" />");
		}
		if (StringUtils.isNotEmpty(getWarntime())) {
			sb.append("<Warntime  value=\"").append(getWarntime()).append("\"")
					.append(" set=\"1\" />");
		}
		// if (StringUtils.isNotEmpty(getMasterDevice())) {
			sb.append("<MasterDevice value=\"").append(getMasterDevice())
					.append("\"").append(" set=\"1\" />");
		// }
		sb.append("</Availability></Root>");
		return sb.toString();
	} */

	public String getSend() {
		return send;
	}

	public void setSend(String send) {
		this.send = send;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getInterfaces() {
		return this.interfaces;
	}

	public void setInterfaces(String interfaces) {
		// 
		this.interfaces = interfaces;
	}

	public String getIndirect() {
		return this.indirect;
	}

	public void setIndirect(String indirect) {
		this.indirect = indirect;
	}

	public String getMasterDevice() {
		return this.masterDevice;
	}

	public void setMasterDevice(String masterDevice) {
		this.masterDevice = masterDevice;
	}	

	public String getSelf() {
		return self;
	}

	public void setSelf(String self) {
		this.self = self;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
}
