package org.kylin.klb.web.security;

import org.apache.struts2.convention.annotation.Namespace;

import com.opensymphony.xwork2.ActionSupport;

@Namespace("/")
public class KlbIndexAction extends ActionSupport {
	private static final long serialVersionUID = 1L;

	public String execute() throws Exception {
		
		return "success";
	}
}
