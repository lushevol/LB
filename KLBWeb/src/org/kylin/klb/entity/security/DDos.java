package org.kylin.klb.entity.security;

import org.kylin.klb.entity.IdEntity;

public class DDos extends IdEntity {
	private String ddosset;
	private String status;

	public String getDdosset() {
		return this.ddosset;
	}

	public void setDdosset(String ddosset) {
		this.ddosset = ddosset;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
