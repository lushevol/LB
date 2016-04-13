package org.kylin.klb.entity.security;

import org.kylin.klb.entity.IdEntity;

public class LoadScheduler extends IdEntity {
	private String schedulerName;

	public String getSchedulerName() {
		return this.schedulerName;
	}

	public void setSchedulerName(String schedulerName) {
		this.schedulerName = schedulerName;
	}
}
