package org.kylin.klb.service;

import javax.servlet.http.HttpSession;
import org.kylin.klb.entity.security.User;
import org.kylin.modules.web.struts2.Struts2Utils;

public class SecurityUtils {
	public static User getSecurityUser() {
		HttpSession session = Struts2Utils.getSession();
		return (User) session.getAttribute("klbSessionUser");
	}

	public static void updateSecurityUser(User user) {
		HttpSession session = Struts2Utils.getSession();
		session.setAttribute("klbSessionUser", user);
	}

	public static void removeSecurityUser() {
		HttpSession session = Struts2Utils.getSession();
		session.removeAttribute("klbSessionUser");
	}

	public static void setConnectStatus() {
		HttpSession session = Struts2Utils.getSession();
		session.setAttribute("connectStatus", "failed");
	}

	public static void removeConnectStatus() {
		HttpSession session = Struts2Utils.getSession();
		session.removeAttribute("connectStatus");
	}
}
