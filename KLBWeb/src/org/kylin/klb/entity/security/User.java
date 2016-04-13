package org.kylin.klb.entity.security;

import org.apache.commons.lang.builder.ToStringBuilder;

public class User {
	private String id;
	private String loginName;
	private String password;
	private String newPassword;
	private String name;
	private String email;
	private String auth;
	private String guide;

	public String getGuide() {
		return this.guide;
	}

	public void setGuide(String guide) {
		this.guide = guide;
	}

	public String getAuth() {
		return this.auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public String getNewPassword() {
		return this.newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
