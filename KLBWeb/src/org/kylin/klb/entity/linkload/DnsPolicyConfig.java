package org.kylin.klb.entity.linkload;

public class DnsPolicyConfig {
	private String id;
	private String name;
	private String state;	
	private String aliseList;
	private String allAliasList;
	private String servers;
	private String displayServers;
	private String echo;
	private String ttl;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getAliseList() {
		return aliseList;
	}
	public void setAliseList(String aliseList) {
		this.aliseList = aliseList;
	}
	public String getServers() {
		return servers;
	}
	public void setServers(String servers) {
		this.servers = servers;
	}
	public String getDisplayServers() {
		return displayServers;
	}
	public void setDisplayServers(String displayServers) {
		this.displayServers = displayServers;
	}
	public String getEcho() {
		return echo;
	}
	public void setEcho(String echo) {
		this.echo = echo;
	}
	public String getTtl() {
		return ttl;
	}
	public void setTtl(String ttl) {
		this.ttl = ttl;
	}
	public String getAllAliasList() {
		return allAliasList;
	}
	public void setAllAliasList(String allAliasList) {
		this.allAliasList = allAliasList;
	}
			
}
