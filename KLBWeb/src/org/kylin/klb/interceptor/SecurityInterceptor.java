package org.kylin.klb.interceptor;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class SecurityInterceptor extends AbstractInterceptor {
	public static final String USER_SESSION_KEY = "klbSessionUser";
	private static final long serialVersionUID = 1L;

	public String intercept(ActionInvocation invocation) throws Exception {
		ActionContext ctx = invocation.getInvocationContext();
		if (ctx.getSession().get("klbSessionUser") == null) {
			ctx.put("error", "1");
			return "login";
		}
		if (StringUtils.equals((String) ctx.getSession().get("connectStatus"),
				"failed")) {
			ctx.put("error", "2");
			return "login";
		}
		return invocation.invoke();
	}
}
