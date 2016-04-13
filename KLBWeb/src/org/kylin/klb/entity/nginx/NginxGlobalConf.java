package org.kylin.klb.entity.nginx;

public class NginxGlobalConf {

	private String status;
	private String enabled;
	private String denyNotMatch;
	private String processor;
	private String connections;
	private String keepalive;
	private String gzip;
	private String gzipLength;
	
	
	public String toString() {
		return "NginxGlobalConf : [ " + 
				"status=" + this.status +
				", enabled=" + this.enabled +
				", denyNotMatch=" + this.denyNotMatch +
				", processor=" + this.processor +
				", connections=" + this.connections +
				", keepalive=" + this.keepalive +
				", gzip=" + this.gzip +
				", gzipLength=" + this.gzipLength +
				" ]\n";
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getProcessor() {
		return processor;
	}

	public void setProcessor(String processor) {
		this.processor = processor;
	}

	public String getConnections() {
		return connections;
	}

	public void setConnections(String connections) {
		this.connections = connections;
	}

	public String getKeepalive() {
		return keepalive;
	}

	public void setKeepalive(String keepalive) {
		this.keepalive = keepalive;
	}

	public String getGzip() {
		return gzip;
	}

	public void setGzip(String gzip) {
		this.gzip = gzip;
	}

	public String getGzipLength() {
		return gzipLength;
	}

	public void setGzipLength(String gzipLength) {
		this.gzipLength = gzipLength;
	}

	public String getDenyNotMatch() {
		return denyNotMatch;
	}

	public void setDenyNotMatch(String denyNotMatch) {
		this.denyNotMatch = denyNotMatch;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

	
	
	
}
