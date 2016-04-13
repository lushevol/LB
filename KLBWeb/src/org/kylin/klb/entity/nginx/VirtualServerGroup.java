package org.kylin.klb.entity.nginx;

import java.util.List;


public class VirtualServerGroup{
	private String vsId;
	private String name;	// 虚拟服务名称，ssl证书名字根据此字段生成
	private String status; //虚拟服务状态
	private String	dev;	//绑定虚拟ip的接口
	private	String listenPort;	//监听端口
	private String	virtualIp; //ulong	ip；		\\ 虚拟ip
	private String	sslStatu;//uchar	ssl；	\\ 是否使用ssl卸载，on或者off
	private String sslTimeout; //ushort   ssl_session_timeout;	\\ ssl连接超时时间，仅在启用ssl卸载时有效
	private String haType;
	private String haName;
	private String haStatus;
	private String cookieEnabled;
	private String cookieName;
	private String cookieExpire;
	private String cert;
	private String key;


	List<Location> locationList; //		\\ 多个location链表
	
	public String toString() {
		return "VirtualServerGroup : [ " + 
				"vsId=" + this.vsId +
				", name=" + this.name +
				", status=" + this.status +
				", dev=" + this.dev +
				", listenPort=" + this.listenPort +
				", virtualIp=" + this.virtualIp +
				", sslStatu=" + this.sslStatu +
				", sslTimeout=" + this.sslTimeout +
				", haType=" + this.haType +
				", haName=" + this.haName +
				", haStatus=" + this.haStatus +
				", cookieEnabled=" + this.cookieEnabled +
				", cookieName=" + this.cookieName +
				", cookieExpire=" + this.cookieExpire +
				" ]\n";
	}
		
	public String getVsId() {
		return vsId;
	}
	public void setVsId(String vsId) {
		this.vsId = vsId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDev() {
		return dev;
	}
	public void setDev(String dev) {
		this.dev = dev;
	}
	public String getListenPort() {
		return listenPort;
	}
	public void setListenPort(String listenPort) {
		this.listenPort = listenPort;
	}
	public String getVirtualIp() {
		return virtualIp;
	}
	public void setVirtualIp(String virtualIp) {
		this.virtualIp = virtualIp;
	}
	public String getSslStatu() {
		return sslStatu;
	}
	public void setSslStatu(String sslStatu) {
		this.sslStatu = sslStatu;
	}
	public String getSslTimeout() {
		return sslTimeout;
	}
	public void setSslTimeout(String sslTimeout) {
		this.sslTimeout = sslTimeout;
	}
	public List<Location> getLocationList() {
		return locationList;
	}
	public void setLocationList(List<Location> locationList) {
		this.locationList = locationList;
	}

	public String getHaType() {
		return haType;
	}

	public void setHaType(String haType) {
		this.haType = haType;
	}

	public String getHaStatus() {
		return haStatus;
	}

	public void setHaStatus(String haStatus) {
		this.haStatus = haStatus;
	}

	public String getHaName() {
		return haName;
	}

	public void setHaName(String haName) {
		this.haName = haName;
	}

	public String getCookieEnabled() {
		return cookieEnabled;
	}

	public void setCookieEnabled(String cookieEnabled) {
		this.cookieEnabled = cookieEnabled;
	}

	public String getCookieName() {
		return cookieName;
	}

	public void setCookieName(String cookieName) {
		this.cookieName = cookieName;
	}

	public String getCookieExpire() {
		return cookieExpire;
	}

	public void setCookieExpire(String cookieExpire) {
		this.cookieExpire = cookieExpire;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCert() {
		return cert;
	}

	public void setCert(String cert) {
		this.cert = cert;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	
}
