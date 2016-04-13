package org.kylin.klb.entity.security;

import org.kylin.klb.entity.IdEntity;

public class Hostname extends IdEntity {
	
	private String hostname;
	
	public String getHostname() {
		return hostname;
	}
	
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

}