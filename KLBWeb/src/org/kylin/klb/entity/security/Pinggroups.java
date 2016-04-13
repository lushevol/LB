package org.kylin.klb.entity.security;

import org.kylin.klb.entity.IdEntity;

public class Pinggroups extends IdEntity {
	private String firstadd;
	private String secondadd;

	public String getFirstadd() {
		return this.firstadd;
	}

	public void setFirstadd(String firstadd) {
		this.firstadd = firstadd;
	}
	public String getSecondadd() {
		return this.secondadd;
	}

	public void setSecondadd(String secondadd) {
		this.secondadd = secondadd;
	}
}

