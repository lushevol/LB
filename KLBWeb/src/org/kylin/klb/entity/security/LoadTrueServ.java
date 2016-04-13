package org.kylin.klb.entity.security;

import java.util.List;
import org.kylin.klb.entity.IdEntity;
import org.kylin.modules.utils.StringUtils;

public class LoadTrueServ extends IdEntity {
	private String vipMark;
	private String vsPort;
	private String description;
	private List realIp;
	private String vsId;
	private String tsId;
	private String service;
	private String serviceName;
	private String status;
	private String forward;
	private String weight;
	private String mapport;
	private String ip;

	public String toXml() {
		StringBuffer sb = new StringBuffer();
		String s1 = getServiceName();
		String s2 = (s1.matches("^[\\d]+[\\w]*$")) ? "_" + s1 : s1;

		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
				.append("<Root>").append("<Services>");
		if (StringUtils.isNotEmpty(getService())) {
			sb.append("<").append(getService()).append(">");
		}
		sb.append("<Servers  add=\"1\">");
		if (StringUtils.isNotEmpty(s1)) {
			sb.append("<").append(s2).append(">");
		}
		if (StringUtils.isNotEmpty(getForward())) {
			sb.append("<Action value=\"").append(getForward()).append(
					"\"  set=\"1\" />");
		}
		if (StringUtils.isNotEmpty(getIp())) {
			sb.append("<IP value=\"").append(getIp())
					.append("\"  set=\"1\" />");
		}
		if (StringUtils.isNotEmpty(getWeight())) {
			sb.append("<Weight value=\"").append(getWeight()).append(
					"\" set=\"1\" />");
		}

		sb.append("</").append(s2).append(">");
		sb.append("</Servers>").append("</").append(getService()).append(">")
				.append("</Services></Root>");
		return sb.toString();
	}

	public String getVipMark() {
		return this.vipMark;
	}

	public void setVipMark(String vipMark) {
		this.vipMark = vipMark;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getWeight() {
		return this.weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getServiceName() {
		return this.serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getForward() {
		return this.forward;
	}

	public void setForward(String forward) {
		this.forward = forward;
	}

	public String getVsId() {
		return vsId;
	}

	public void setVsId(String vsId) {
		this.vsId = vsId;
	}

	public String getTsId() {
		return tsId;
	}

	public void setTsId(String tsId) {
		this.tsId = tsId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMapport() {
		return mapport;
	}

	public void setMapport(String mapport) {
		this.mapport = mapport;
	}

	public String getVsPort() {
		return vsPort;
	}

	public void setVsPort(String vsPort) {
		this.vsPort = vsPort;
	}
}
