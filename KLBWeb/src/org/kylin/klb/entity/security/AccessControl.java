package org.kylin.klb.entity.security;

import java.util.Random;

public class AccessControl {
	private String id;
	private String srcNet;
	private String destNet;
	private String protocol;
	private String protocols;
	private String srcPort;
	private String destPort;
	private String describe;

	/* public String toXml() {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(
				"<Root><Network><DeniedIPList add=\"1\" >");

		int num = new Random().nextInt(1000);
		sb.append("<IP").append(num).append(">").append("<IP value=\"").append(
				getIp()).append("\" set=\"1\" />").append("<Netmask value=\"")
				.append(getNetMask()).append("\" set=\"1\" />").append("</IP")
				.append(num).append(">");
		sb.append("</DeniedIPList></Network></Root>");
		return sb.toString();
	}

	public String updateXml() {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(
				"<Root><Network><DeniedIPList>");
		sb.append("<").append(getName()).append(">").append("<IP value=\"")
				.append(getIp()).append("\" set=\"1\" />").append(
						"<Netmask value=\"").append(getNetMask()).append(
						"\" set=\"1\" />").append("</").append(getName())
				.append(">").append("</DeniedIPList></Network></Root>");
		return sb.toString();
	} */

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

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getProtocols() {
		return protocols;
	}

	public void setProtocols(String protocols) {
		this.protocols = protocols;
	}
	
}
