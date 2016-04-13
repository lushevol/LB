package org.kylin.klb.entity.security;

public class Monitor {

	private String vsId;
	private String serviceName;
	private String interval;
	private String port;
	private String retry;
	private String timeout;
	private String type;
	
	private String mail;
	private String date;
	private String enabled;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getRetry() {
		return retry;
	}
	
	

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public void setRetry(String retry) {
		this.retry = retry;
	}

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVsId() {
		return vsId;
	}

	public void setVsId(String vsId) {
		this.vsId = vsId;
	}

}
