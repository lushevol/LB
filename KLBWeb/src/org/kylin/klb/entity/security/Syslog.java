package org.kylin.klb.entity.security;

import org.kylin.klb.entity.IdEntity;

public class Syslog extends IdEntity {
	
	private String enabled;
	private String domain;
	
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
		
}
