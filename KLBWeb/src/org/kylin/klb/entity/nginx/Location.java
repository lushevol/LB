package org.kylin.klb.entity.nginx;

public class Location {
	private String serviceId;
	private String locationId;
	private String  match;//string	match； 配置文件中location后对应的匹配字符串
	private String groupName;//	string	group_name;真实服务器组名，或者使用real_server_group	*指针，指向已添加的真实服务器组结构;
	private String insertId;
	
	public String toString() {
		return "Location : [ " + 
				"serviceId=" + this.serviceId +
				", locationId=" + this.locationId +
				", match=" + this.match +
				", groupName=" + this.groupName +
				", insertId=" + this.insertId +
				" ]\n";
	}
	
	public String getMatch() {
		return match;
	}
	
	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public void setMatch(String match) {
		this.match = match;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getInsertId() {
		return insertId;
	}

	public void setInsertId(String insertId) {
		this.insertId = insertId;
	}
	
	
}
