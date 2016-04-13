package org.kylin.klb.entity.nginx;

import java.util.List;

public class RealServerGroup {
	
	private String rsgId;
	private String name;
	private String method;
	private String methodName;
	
	private List<ServerItem> serverList = null;
	
	public String toString() {
		return "RealServerGroup : [ " + 
				"rsgId=" + this.rsgId +
				", name=" + this.name +
				", method=" + this.method +
				", methodName=" + this.methodName +
				" ]\n";
	}
	

	public String getRsgId() {
		return rsgId;
	}

	public void setRsgId(String rsgId) {
		this.rsgId = rsgId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public List<ServerItem> getServerList() {
		return serverList;
	}

	public void setServerList(List<ServerItem> serverList) {
		this.serverList = serverList;
	}


	public String getMethodName() {
		return methodName;
	}


	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	
	

}
