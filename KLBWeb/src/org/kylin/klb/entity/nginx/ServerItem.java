package org.kylin.klb.entity.nginx;

public class ServerItem {
	private String rsgId;
	private String serverId;
	private String ip;
	private String port;
	private String weight;
	private String maxFails;
	private String failTimeout;
	private String type;
	private String typeName;
	private String srunId;
	
	public String toString() {
		return "ServerItem : [ " + 
				"rsgId=" + this.rsgId +
				", serverId=" + this.serverId +
				", ip=" + this.ip +
				", port=" + this.port +
				", weight=" + this.weight +
				", maxFails=" + this.maxFails +
				", failTimeout=" + this.failTimeout +
				", type=" + this.type +
				", typeName=" + this.typeName +
				", srunId=" + this.srunId +
				" ]\n";
	}
	
	public String getRsgId() {
		return rsgId;
	}
	public void setRsgId(String rsgId) {
		this.rsgId = rsgId;
	}
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getMaxFails() {
		return maxFails;
	}
	public void setMaxFails(String maxFails) {
		this.maxFails = maxFails;
	}
	public String getFailTimeout() {
		return failTimeout;
	}
	public void setFailTimeout(String failTimeout) {
		this.failTimeout = failTimeout;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getSrunId() {
		return srunId;
	}

	public void setSrunId(String srunId) {
		this.srunId = srunId;
	}
	
	
}
