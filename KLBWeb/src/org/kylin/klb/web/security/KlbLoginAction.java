package org.kylin.klb.web.security;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.kylin.klb.service.RegisterService;
import org.kylin.modules.web.struts2.Struts2Utils;

import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("struts-default")
public class KlbLoginAction extends ActionSupport{
	private static final long serialVersionUID = 1L;

	private String code;
	private String registerCode;
	private RegisterService rs = new RegisterService();
	private String error;
	
	@Action(value = "/klb-login", results = { @org.apache.struts2.convention.annotation.Result(name = "register", location = "Register.jsp", type = "dispatcher"),
			@org.apache.struts2.convention.annotation.Result(name = "success", location = "klb-login.jsp", type = "dispatcher") })
	public String klblogin() throws Exception {		
		/* boolean registered = false;
		registered = rs.verify();
		
		if (registered){
			return "success";
		}
		else{
			code = rs.getCode();
			return "register";
		} */
		return "success";
	}
	/* public String klbRegister() throws Exception {
		boolean registered = false;
		registered = rs.verify();
		
		if (registered){
			return "redirect";
		}
		else{
			code = rs.getCode();
			return "register";
		}		
	} */
	@Action(value = "/fc", results = { @org.apache.struts2.convention.annotation.Result(name = "success", location = "fc.jsp", type = "dispatcher") })
	public String fc() throws Exception {
		return "success";
	}
	
	@Action(value = "/register", results = { @org.apache.struts2.convention.annotation.Result(name = "false", location = "Register.jsp", type = "dispatcher"), 
			@org.apache.struts2.convention.annotation.Result(name = "success", location = "klb-index.jsp", type = "dispatcher")})
	public String register()throws Exception {
		
		HttpServletRequest request = Struts2Utils.getRequest();
		boolean status = false;
		status = rs.register(registerCode);
		if(status){
			return "success";
		}
		else{
			request.setAttribute("register", "false");
			code = rs.getCode();
			return "false";
		}
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public String getRegisterCode() {
		return registerCode;
	}

	public void setRegisterCode(String registerCode) {
		this.registerCode = registerCode;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
}
