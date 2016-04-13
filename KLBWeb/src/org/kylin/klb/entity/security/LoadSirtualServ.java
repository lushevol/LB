package org.kylin.klb.entity.security;

import java.util.List;
import org.kylin.klb.entity.IdEntity;
import org.kylin.modules.utils.StringUtils;

public class LoadSirtualServ extends IdEntity {
	private String protocolAndPort;
	private List realIp;
	private String vsId;
	private String service;
	private String haType;
	private String vipMark;
	private String tcpPorts;
	private String udpPorts;
	private String trafficUp;
	private String trafficDown;
	private String idMark;
	private String scheduling;
	private String persistent;
	private String persistentTimeout;
	private String persistentNetmask;
	private String status;
	private String interfaces;
		
	public static void main(String[] args) {

	}

	public String toXml() {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
				.append("<Root>").append("<Services add=\"1\">");
		String s = getService();
		String service = (s.matches("^[\\d]+[\\w]*$")) ? "_" + s : s;

		if (StringUtils.isNotEmpty(getService())) {
			sb.append("<").append(service).append(">");
		}

		/* if (StringUtils.isNotEmpty(getScheduler())) {
			sb.append("<Device value=\"").append(getScheduler()).append("\"")
					.append(" set=\"1\" />");
		} */

		sb.append("<Enabled value=\"false\" set=\"1\" />");

		if (StringUtils.isNotEmpty(getVipMark())) {
			sb.append("<IP value=\"").append(getVipMark()).append("\"").append(
					" set=\"1\" />");
		}

		if (StringUtils.isNotEmpty(getInterfaces())) {
			sb.append("<Interface value=\"").append(getInterfaces()).append(
					"\"").append(" set=\"1\" />");
		}

		if (StringUtils.isNotEmpty(getIdMark())) {
			sb.append("<Mark  value=\"").append(getIdMark()).append("\"")
					.append(" set=\"1\" />");
		}

		if (StringUtils.isNotEmpty(getPersistent())) {
			sb.append("<Persistent   value=\"").append(getPersistent()).append(
					"\"").append(" set=\"1\" />");
		}

		if (StringUtils.isNotEmpty(getPersistentNetmask())) {
			sb.append("<PersistentNetmask   value=\"").append(
					getPersistentNetmask()).append("\"")
					.append(" set=\"1\" />");
		}

		if (StringUtils.isNotEmpty(getScheduling())) {
			sb.append("<Schedule  value=\"").append(getScheduling()).append(
					"\"").append(" set=\"1\" />");
		}

		if (StringUtils.isNotEmpty(getTcpPorts())) {
			sb.append("<TCPPorts set=\"1\" >");
			String tcpPorts = getTcpPorts();
			if ((StringUtils.contains(tcpPorts, ","))
					|| (StringUtils.contains(tcpPorts, "，"))) {
				String[] ports = new String[0];
				if (StringUtils.contains(tcpPorts, ","))
					ports = tcpPorts.split(",");
				else if (StringUtils.contains(tcpPorts, "，")) {
					ports = tcpPorts.split("，");
				}
				for (String str : ports){
					if (StringUtils.contains(str, "-")) {
						String[] minAndMax = new String[2];
						minAndMax = str.split("-");
						sb.append("<Port min=\"").append(minAndMax[0]).append("\"").append(
						" max=\"").append(minAndMax[1]).append("\" />");
					} else {
						sb.append("<Port min=\"").append(str).append("\"").append(
						" max=\"").append(str).append("\" />");
					}
				}					
			}
			else {				
				if (StringUtils.contains(tcpPorts, "-")) {
					String[] minAndMax = new String[2];
					minAndMax = tcpPorts.split("-");
					sb.append("<Port min=\"").append(minAndMax[0]).append("\"").append(
					" max=\"").append(minAndMax[1]).append("\" />");
				} else {
					sb.append("<Port min=\"").append(tcpPorts).append("\"").append(
					" max=\"").append(tcpPorts).append("\" />");
				}
			}
			sb.append("</TCPPorts>");
		}

		if (StringUtils.isNotEmpty(getUdpPorts())) {
			sb.append("<UDPPorts set=\"1\" >");
			String udpPorts = getUdpPorts();
			if ((StringUtils.contains(udpPorts, ","))
					|| (StringUtils.contains(udpPorts, "，"))) {
				String[] ports = new String[0];
				if (StringUtils.contains(udpPorts, ","))
					ports = udpPorts.split(",");
				else if (StringUtils.contains(udpPorts, "，")) {
					ports = udpPorts.split("，");
				}
				for (String str : ports){
					if (StringUtils.contains(str, "-")) {
						String[] minAndMax = new String[2];
						minAndMax = str.split("-");
						sb.append("<Port min=\"").append(minAndMax[0]).append("\"").append(
						" max=\"").append(minAndMax[1]).append("\" />");
					} else {
						sb.append("<Port min=\"").append(str).append("\"").append(
						" max=\"").append(str).append("\" />");
					}
				}
			}
			else {				
				if (StringUtils.contains(udpPorts, "-")) {
					String[] minAndMax = new String[2];
					minAndMax = udpPorts.split("-");
					sb.append("<Port min=\"").append(minAndMax[0]).append("\"").append(
					" max=\"").append(minAndMax[1]).append("\" />");
				} else {
					sb.append("<Port min=\"").append(udpPorts).append("\"").append(
					" max=\"").append(udpPorts).append("\" />");
				}				
			}
			sb.append("</UDPPorts>");
		}
		sb.append("</").append(service).append(">").append("</Services></Root>");
		return sb.toString();
	}

	public List getRealIp() {
		return this.realIp;
	}

	public void setRealIp(List realIp) {
		this.realIp = realIp;
	}

	public String getService() {
		return this.service;
	}

	public void setService(String service) {
		this.service = service;
	}

	/* public String getScheduler() {
		return this.scheduler;
	}

	public void setScheduler(String scheduler) {
		//
		// if (scheduler == null || StringUtils.equals(scheduler.trim(), "")){
		//
		// this.scheduler = "本机";
		// }else{
		this.scheduler = scheduler;
		// }
		//
	} */

	public String getVipMark() {
		return this.vipMark;
	}

	public void setVipMark(String vipMark) {
		this.vipMark = vipMark;
	}

	public String getPersistent() {
		return this.persistent;
	}

	public void setPersistent(String persistent) {
		this.persistent = persistent;
	}

	public String getPersistentNetmask() {
		return this.persistentNetmask;
	}

	public void setPersistentNetmask(String persistentNetmask) {
		this.persistentNetmask = persistentNetmask;
	}

	public String getInterfaces() {
		return this.interfaces;
	}

	public void setInterfaces(String interfaces) {
		this.interfaces = interfaces;
	}

	/*
	 * public String getNetMask() { return this.netMask; }
	 * 
	 * public void setNetMask(String netMask) { this.netMask = netMask; }
	 */

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	

	public String getTcpPorts() {
		return this.tcpPorts;
	}

	public void setTcpPorts(String tcpPorts) {
		this.tcpPorts = tcpPorts;
	}

	public String getUdpPorts() {
		return this.udpPorts;
	}

	public void setUdpPorts(String udpPorts) {
		this.udpPorts = udpPorts;
	}

	public String getIdMark() {
		return this.idMark;
	}

	public void setIdMark(String idMark) {
		this.idMark = idMark;
	}

	public String getScheduling() {
		return this.scheduling;
	}

	public void setScheduling(String scheduling) {
		this.scheduling = scheduling;
	}

	public String getPersistentTimeout() {
		return persistentTimeout;
	}

	public void setPersistentTimeout(String persistentTimeout) {
		this.persistentTimeout = persistentTimeout;
	}

	public String getVsId() {
		return vsId;
	}

	public void setVsId(String vsId) {
		this.vsId = vsId;
	}

	public String getTrafficUp() {
		return trafficUp;
	}

	public void setTrafficUp(String trafficUp) {
		this.trafficUp = trafficUp;
	}

	public String getTrafficDown() {
		return trafficDown;
	}

	public void setTrafficDown(String trafficDown) {
		this.trafficDown = trafficDown;
	}

	public String getHaType() {
		return haType;
	}

	public void setHaType(String haType) {
		this.haType = haType;
	}

	public String getProtocolAndPort() {
		return protocolAndPort;
	}

	public void setProtocolAndPort(String protocolAndPort) {
		this.protocolAndPort = protocolAndPort;
	}
}
