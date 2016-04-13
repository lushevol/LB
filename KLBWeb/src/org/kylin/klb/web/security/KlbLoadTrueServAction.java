package org.kylin.klb.web.security;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Results;
import org.kylin.klb.entity.security.LoadTrueServ;
import org.kylin.klb.service.KlbManager;
import org.kylin.klb.util.Utils;
import org.kylin.klb.web.CrudActionSupport;
import org.kylin.modules.web.struts2.Struts2Utils;
import org.springframework.beans.factory.annotation.Autowired;

@Namespace("/")
@Results( { @org.apache.struts2.convention.annotation.Result(name = "reload", location = "klb-load-true-serv.action", type = "redirect") })
public class KlbLoadTrueServAction extends CrudActionSupport<LoadTrueServ> {
	private static final long serialVersionUID = 1L;

	@Autowired
	private KlbManager klbManager;
	LoadTrueServ entity = null;
	private List<LoadTrueServ> loadTrueServ = new ArrayList();

	protected void prepareModel() throws Exception {
		this.entity = new LoadTrueServ();
	}

	public LoadTrueServ getModel() {
		return this.entity;
	}

	public String save() throws Exception {
		HttpServletRequest request = Struts2Utils.getRequest();
		String update = request.getParameter("update");
		String oldServiceName = request.getParameter("oldServiceName");

		String service = this.entity.getService();
		String serviceName = this.entity.getServiceName();

		//if (StringUtils.isNotEmpty(serviceName)) {
		if (StringUtils.equalsIgnoreCase(oldServiceName, serviceName)) {
			if (StringUtils.equalsIgnoreCase(update, "0")) {								
				String tsMess =  this.klbManager.setTrueServ(this.entity);
				if ( tsMess.equals("true") ) {
					Struts2Utils.renderJson("{auth:true,\"mess\":\"数据保存成功\"}", new String[0]);
				} else if ( tsMess.equals("false") ) {
					Struts2Utils.renderJson("{auth:false,\"mess\":\"数据保存失败\"}", new String[0]);
				} else {
					Struts2Utils.renderJson("{auth:false,\"mess\":\"" + tsMess + "\"}", new String[0]);
				}								
			} else if (StringUtils.equalsIgnoreCase(update, "1")) {								
				String tsMess = this.klbManager.updateTrueServ(this.entity);
				if ( tsMess.equals("true") ) {
					Struts2Utils.renderJson("{auth:true,\"mess\":\"数据更新成功\"}", new String[0]);
				} else if ( tsMess.equals("false") ) {
					Struts2Utils.renderJson("{auth:false,\"mess\":\"数据更新失败\"}", new String[0]);
				} else {
					Struts2Utils.renderJson("{auth:false,\"mess\":\"" + tsMess + "\"}", new String[0]);
				}								
			}
		} else {
			//System.out.println(service);
			//System.out.println(serviceName);
			LoadTrueServ ls = this.klbManager.getTrueServByName(service, serviceName);
			//System.out.println(ls);
			if ((ls != null)
					&& (this.entity.getService().equals(ls.getService()))) {
				Struts2Utils.renderJson("{auth:false,\"mess\":\"真实服务器名称 已经存在\"}", new String[0]);
			} else if (StringUtils.equalsIgnoreCase(update, "0")) {								
				String tsMess =  this.klbManager.setTrueServ(this.entity);
				if ( tsMess.equals("true") ) {
					Struts2Utils.renderJson("{auth:true,\"mess\":\"数据保存成功\"}", new String[0]);
				} else if ( tsMess.equals("false") ) {
					Struts2Utils.renderJson("{auth:false,\"mess\":\"数据保存失败\"}", new String[0]);
				} else {
					Struts2Utils.renderJson("{auth:false,\"mess\":\"" + tsMess + "\"}", new String[0]);
				}								
			} else if (StringUtils.equalsIgnoreCase(update, "1")) {
				String tsMess = this.klbManager.updateTrueServ(this.entity);
				if ( tsMess.equals("true") ) {
					Struts2Utils.renderJson("{auth:true,\"mess\":\"数据更新成功\"}", new String[0]);
				} else if ( tsMess.equals("false") ) {
					Struts2Utils.renderJson("{auth:false,\"mess\":\"数据更新失败\"}", new String[0]);
				} else {
					Struts2Utils.renderJson("{auth:false,\"mess\":\"" + tsMess + "\"}", new String[0]);
				}
			}
		}
		/* } else {
			Struts2Utils.renderJson("{auth:false,\"mess\":\"真实服务器名称 不能为空\"}",new String[0]);
		} */
		return null;
	}

	public String delete() throws Exception {
		HttpServletRequest request = Struts2Utils.getRequest();
		String vsId = request.getParameter("vsId");
		String tsId = request.getParameter("tsId");

		if ((StringUtils.isNotEmpty(tsId)) && (StringUtils.isNotEmpty(vsId))) {
			boolean flag = this.klbManager.removeTrueServ(vsId, tsId);
			if (flag)
				Struts2Utils.renderJson("{auth:true}", new String[0]);
			else
				Struts2Utils.renderJson("{auth:false}", new String[0]);
		} else {
			Struts2Utils.renderJson("{auth:false}", new String[0]);
		}
		return null;
	}

	public String input() throws Exception {
		//
		String vsId = this.entity.getVsId();
		String tsId = this.entity.getTsId();
		if (StringUtils.isNotEmpty(tsId)) {
			LoadTrueServ loadTrueServ = this.klbManager.getTrueServById(vsId, tsId);
			Struts2Utils.renderJson("{\"auth\":true,\"obj\":"
					+ JSONObject.fromObject(loadTrueServ).toString() + "}", new String[0]);
		} else {
			Struts2Utils.renderJson("{\"auth\":false}", new String[0]);
		}
		return null;
	}
	
	public String start() throws Exception {
		HttpServletRequest request = Struts2Utils.getRequest();
		boolean flag = false;

		String vsId = request.getParameter("vsId");
		String tsId = request.getParameter("tsId");
		String status = request.getParameter("status");
		if ((StringUtils.isNotEmpty(vsId)) && (StringUtils.isNotEmpty(tsId)) && (StringUtils.isNotEmpty(status))) {
			flag = this.klbManager.setTrueServStatus(vsId, tsId, status);
		}
		if (flag)
			Struts2Utils.renderJson("{auth:true}", new String[0]);
		else {
			Struts2Utils.renderJson("{auth:false}", new String[0]);
		}
		return null;
	}

	public String list() throws Exception {
		//
		HttpServletRequest request = Struts2Utils.getRequest();

		List<String> sirtualServNames = this.klbManager.getSirtualServNames();
		Map map = new HashMap();
		if ((sirtualServNames != null) && (!sirtualServNames.isEmpty())) {
			for (String s : sirtualServNames) {
				map.put(s, s);
			}
		}
		request.setAttribute("serviceList", map);

		Map forwards = Utils.getInstance().getForwards();
		request.setAttribute("forwards", forwards);

		this.loadTrueServ = this.klbManager.getTrueServInfo();
		return "success";
	}

	public List<LoadTrueServ> getLoadTrueServ() {
		return this.loadTrueServ;
	}

	public void setLoadTrueServ(List<LoadTrueServ> loadTrueServ) {
		this.loadTrueServ = loadTrueServ;
	}
}
