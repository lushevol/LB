package org.kylin.klb.web.security;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.kylin.klb.entity.security.User;
import org.kylin.klb.service.KlbClient;
import org.kylin.klb.service.KlbManager;
import org.kylin.klb.service.RegisterService;
import org.kylin.klb.service.SecurityUtils;
import org.kylin.klb.util.HashToBase64Util;
import org.kylin.modules.web.struts2.Struts2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@Namespace("/")
@ParentPackage("struts-default")
public class SystemAction extends ActionSupport implements ModelDriven<User> {
	private static final long serialVersionUID = 1L;

	// private static String PASSWORD;
	private String connectStatus;
	
	private String code;
	private RegisterService rs = new RegisterService();
	@Autowired
	private KlbManager klbmanager;
	@Autowired
	private KlbClient klbClient;
	private static Logger logger = LoggerFactory.getLogger(SystemAction.class);

	private User user = new User();	
	private String error;

	public User getModel() {
		return this.user;
	}
	
	@Action(value = "/init", results = { @org.apache.struts2.convention.annotation.Result(name = "success", location = "klb-login.action", type = "redirect") })
	public String init() throws Exception {
		HttpSession session = Struts2Utils.getSession();
		String isSoftVersion = "false";
		String isHaEnable = "false";
		String productName = Struts2Utils.PRODUCT_NAME;
		String companyName = Struts2Utils.COMPANY_NAME;
		String loginImage = "";
		String logoImage = "";
		try{
			isSoftVersion = Struts2Utils.getString("IsSoftVersion");
			isHaEnable = Struts2Utils.getString("IsHaEnable");
			productName = Struts2Utils.getString("ProductName");
			companyName = Struts2Utils.getString("CompanyName");
			loginImage = Struts2Utils.getString("LoginImage");
			logoImage = Struts2Utils.getString("LogoImage");
		}catch (Exception e) {
		
		}
	//System.out.println("IsSoftVersion : " + isSoftVersion);
		Struts2Utils.IS_SOFT_VERSION = "true".equals(isSoftVersion) ? true : false;
		Struts2Utils.IS_HA_ENABLE = "true".equals(isHaEnable) ? true : false;
		Struts2Utils.PRODUCT_NAME = productName;
		session.setAttribute("isSoftVersion", isSoftVersion);
		session.setAttribute("isHaEnable", isHaEnable);
		session.setAttribute("productName", productName);
		session.setAttribute("companyName", companyName);
		session.setAttribute("loginImage", loginImage);
		session.setAttribute("logoImage", logoImage);
	//System.out.println("productName : " + productName);
	//System.out.println("companyName : " + companyName);
		return "success";
	}

	@Action(value = "/login", results = { @org.apache.struts2.convention.annotation.Result(name = "success", location = "redirect.action", type = "redirect"),
			@org.apache.struts2.convention.annotation.Result(name = "register", location = "Register.jsp", type = "dispatcher") })
	public String login() throws Exception {

		SecurityUtils.removeSecurityUser();
		SecurityUtils.removeConnectStatus();
		
		String loginName = this.user.getLoginName();
		String password = this.user.getPassword();

		klbClient.checkPassword(password);
		//System.out.println(klbClient.isConnectSuccess());
		if (StringUtils.isNotEmpty(loginName)) {
			if (!klbClient.isConnectSuccess()) {
				SecurityUtils.updateSecurityUser(this.user);
				// SecurityUtils.setConnectStatus();
				//Struts2Utils.renderJson("{auth:true,\"error\":\"2\"}", new String[0]);
				//return null;
				return "success";
			}

			if (!klbClient.checkPassword(password)) {
				//
				//Struts2Utils.renderJson("{auth:true,\"error\":\"1\"}", new String[0]);
				// SecurityUtils.updateSecurityUser(this.user);
				//return null;
				return "success";
			} else {
				//
				KlbClient.setHashPassword(klbClient.encrypt(password));

				String storedHashPassword = KlbClient.getHashPassword();
				String storedBase64Password = HashToBase64Util.hashToBase64Util(storedHashPassword);
				this.klbmanager.initUsersXml();
				User tempUser = new User();
				tempUser = this.klbmanager.getUser("1");
				tempUser.setPassword(storedBase64Password);
				Boolean updateSuccess = this.klbmanager.updateUserInfo("1", tempUser);
				//
				this.user = this.klbmanager.getUser("LoginName", loginName);
				//
				if (this.user != null) {
					SecurityUtils.updateSecurityUser(this.user);
				}
				
				boolean registered = false;
				registered = rs.verify();
								
				if (registered){
					//System.out.println(registered + ";;;;;");
					return "success";
				}
				else{
					code = rs.getCode();
					return "register";
				}
			}

			// SecurityUtils.updateSecurityUser(this.user);
			/*
			 * if (this.user != null) {
			 * 
			 * SecurityUtils.updateSecurityUser(this.user); String
			 * storedHashPassword = KlbClient.getHashPassword(); String
			 * storedBase64Password =
			 * HashToBase64Util.hashToBase64Util(storedHashPassword);
			 * 
			 * if (StringUtils.equals(storedBase64Password,
			 * this.user.getPassword())) { //
			 * SecurityUtils.updateSecurityUser(this.user);
			 *  } }
			 */

			/*
			 * if (this.user != null) { ShaPasswordEncoder sha = new
			 * ShaPasswordEncoder(); sha.setEncodeHashAsBase64(true); if
			 * (StringUtils.equals(sha.encodePassword(password, null),
			 * this.user.getPassword())) {
			 * SecurityUtils.updateSecurityUser(this.user); } }
			 */
		}
		// this.setConnectStatus("true");
		//Struts2Utils.renderJson("{auth:true,\"error\":\"0\"}", new String[0]);
		//return null;
		return "success";
	}
	@Action(value = "/logout", results = { @org.apache.struts2.convention.annotation.Result(name = "success", location = "init.action", type = "redirect") })
	public String logout() throws Exception {
		SecurityUtils.removeSecurityUser();
		return "success";
	}
	
	@Action(value = "/reboot", results = { @org.apache.struts2.convention.annotation.Result(name = "success", location = "init.action", type = "redirect") })
	public String reboot() throws Exception {
		String result =  this.klbmanager.rebootServer();
		if ( result.equals("true") ) {
			addActionMessage("重启服务器成功");
		} else if ( result.equals("false") ) {
			addActionMessage("重启服务器失败");
		} else {
			addActionMessage( result );
		}
		SecurityUtils.removeSecurityUser();
		return "success";
	}
	
	@Action(value = "/shutdown", results = { @org.apache.struts2.convention.annotation.Result(name = "success", location = "init.action", type = "redirect") })
	public String shutdown() throws Exception {
		String result =  this.klbmanager.shutdownServer();
		if ( result.equals("true") ) {
			addActionMessage("关闭服务器成功");
		} else if ( result.equals("false") ) {
			addActionMessage("关闭服务器失败");
		} else {
			addActionMessage( result );
		}
		SecurityUtils.removeSecurityUser();
		return "success";
	}
	
	@Action(value = "/fc", results = { @org.apache.struts2.convention.annotation.Result(name = "success", location = "fc.jsp", type = "dispatcher") })
	public String fc() throws Exception {
		return "success";
	}

	public String getConnectStatus() {
		return connectStatus;
	}

	public void setConnectStatus(String connectStatus) {
		this.connectStatus = connectStatus;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
