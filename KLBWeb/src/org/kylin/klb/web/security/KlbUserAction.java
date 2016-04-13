package org.kylin.klb.web.security;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.xwork.StringUtils;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Results;
import org.apache.xmlrpc.client.XmlRpcClientException;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.kylin.klb.entity.security.User;
import org.kylin.klb.service.KlbClient;
import org.kylin.klb.service.KlbManager;
import org.kylin.klb.service.SecurityUtils;
import org.kylin.klb.service.ServiceException;
import org.kylin.klb.util.HashToBase64Util;
import org.kylin.klb.web.CrudActionSupport;
import org.kylin.modules.web.struts2.Struts2Utils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.providers.encoding.ShaPasswordEncoder;

import sun.misc.BASE64Encoder;

@Namespace("/")
@Results( { @org.apache.struts2.convention.annotation.Result(name = "reload", location = "klb-user.action", type = "redirect") })
public class KlbUserAction extends CrudActionSupport<User> {
	private static final long serialVersionUID = 1L;

	@Autowired
	private KlbManager klbmanager;
	@Autowired
	private KlbClient klbClient;
	private String id;
	private User entity;
	private List<User> user = new ArrayList();

	public void setId(String id) {
		this.id = id;
	}

	public User getModel() {
		return this.entity;
	}

	protected void prepareModel() throws Exception {
		if (StringUtils.isNotEmpty(this.id))
			this.entity = this.klbmanager.getUser(this.id);
		else
			this.entity = new User();
	}

	public String list() throws Exception {
		this.user = this.klbmanager.getUserList();
		return "success";
	}

	public String input() throws Exception {
		return "input";
	}

	public String save() throws Exception {
		boolean flag = false;
		ShaPasswordEncoder sha = new ShaPasswordEncoder();
		sha.setEncodeHashAsBase64(true);

		if (StringUtils.isNotEmpty(this.id)) {
			flag = this.klbmanager.updateUserInfo(this.id, this.entity);
		} else {
			if (StringUtils.isNotEmpty(this.entity.getPassword())) {
				this.entity.setPassword(sha.encodePassword(this.entity
						.getPassword(), null));
			}
			flag = this.klbmanager.saveUser(this.entity);
		}
		if (flag)
			addActionMessage("保存用户成功");
		else
			addActionMessage("保存用户失败");
		return "reload";
	}

	public String delete() throws Exception {
		boolean flag = false;
		clearMessages();
		try {
			if (StringUtils.isNotEmpty(this.id)) {
				flag = this.klbmanager.deleteUser(this.id);
			}
			if (flag)
				addActionMessage("删除用户成功");
			else
				addActionMessage("删除用户失败");
		} catch (ServiceException e) {
			this.logger.error(e.getMessage(), e);
			addActionMessage("删除用户失败");
		}
		return "reload";
	}

	public String changeInput() throws Exception {
		return "change-input";
	}

	/*
	 * public String changeUpdate() throws Exception { ShaPasswordEncoder sha =
	 * new ShaPasswordEncoder(); sha.setEncodeHashAsBase64(true);
	 * 
	 * clearMessages(); boolean flag = false;
	 * 
	 * User u = this.klbmanager.getUser("LoginName", this.entity
	 * .getLoginName()); if ((u != null) && (StringUtils.equals(u.getPassword(),
	 * sha.encodePassword( this.entity.getPassword(), null)))) { if
	 * (StringUtils.isNotEmpty(this.entity.getNewPassword())) {
	 * this.entity.setPassword(sha.encodePassword(this.entity .getNewPassword(),
	 * null)); } flag = this.klbmanager.updateUserInfo(this.id, this.entity); }
	 * 
	 * if (flag) addActionMessage("密码修改成功"); else
	 * addActionMessage("输入的原始密码不正确"); return "change-input"; }
	 */
	public String changeUpdate() throws Exception {

		clearMessages();
		boolean flag = false;

		String newPassword = this.entity.getNewPassword();
		//
		String password = this.entity.getPassword();
		//
		if (!klbClient.checkPassword(password)) {
			addActionMessage("输入的原始密码不正确");
			return "change-input";
		}

		else {
			KlbClient.setHashPassword(klbClient.encrypt(password));
			boolean updateStatus = klbClient.updatePassword(newPassword);
			if (updateStatus) {
				String newHashBase64Password = HashToBase64Util
						.hashToBase64Util(klbClient.encrypt(newPassword));
				this.entity.setPassword(newHashBase64Password);
				flag = this.klbmanager.updateUserInfo(this.id, this.entity);
			}
		}

		/*
		 * 
		 * User u = this.klbmanager.getUser("LoginName", this.entity
		 * .getLoginName()); // // // // // // // //
		 * 
		 * 
		 * String hashPassword = klbClient.encrypt(this.entity.getPassword());
		 * BASE64Encoder encoder = new BASE64Encoder(); String
		 * hashBase64Password = encoder.encode(hashPassword.getBytes("8859_1"));
		 *  //
		 * 
		 * if ((u != null)&& (StringUtils.equals(u.getPassword(),
		 * hashBase64Password))) {
		 * 
		 * 
		 * 
		 * String newHashPassword =
		 * klbClient.encrypt(this.entity.getNewPassword()); String
		 * newHashBase64Password =
		 * encoder.encode(newHashPassword.getBytes("8859_1"));
		 * 
		 * String updatePasswordResult =
		 * klbClient.updatePassword(this.entity.getNewPassword());
		 * KlbClient.setNonHashPassword(this.entity.getNewPassword());
		 * klbClient.setHashPassword();
		 * 
		 * if(StringUtils.equals(updatePasswordResult, "Failed to parse server's
		 * response: Invalid byte 1 of 1-byte UTF-8 sequence.")){
		 * 
		 * this.entity.setPassword(newHashBase64Password); flag =
		 * this.klbmanager.updateUserInfo(this.id, this.entity); }
		 *  }
		 */
		if (flag)
			addActionMessage("密码修改成功");
		else
			addActionMessage("更新失败");
		return "change-input";
	}

	public String checkLoginName() {
		HttpServletRequest request = Struts2Utils.getRequest();
		String newLoginName = request.getParameter("loginName");
		String oldLoginName = request.getParameter("oldLoginName");

		if (!StringUtils.equalsIgnoreCase(newLoginName, oldLoginName)) {
			User u = this.klbmanager.getUser("LoginName", newLoginName);
			if (u == null)
				Struts2Utils.renderText("true", new String[0]);
			else
				Struts2Utils.renderText("false", new String[0]);
		} else {
			Struts2Utils.renderText("true", new String[0]);
		}

		return null;
	}

	public List<User> getUser() {
		return this.user;
	}

	public void setUser(List<User> user) {
		this.user = user;
	}
}
