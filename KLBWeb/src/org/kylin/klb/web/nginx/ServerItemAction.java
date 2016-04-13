package org.kylin.klb.web.nginx;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Results;
import org.kylin.klb.entity.nginx.ServerItem;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.service.nginx.NginxRealServService;
import org.kylin.klb.web.CrudActionSupport;
import org.kylin.modules.web.struts2.Struts2Utils;

@Namespace("/nginx")
@ParentPackage("struts-default")
@Results(@org.apache.struts2.convention.annotation.Result(name = "reload", location = "server-item.action", type = "redirect"))
public class ServerItemAction extends CrudActionSupport<ServerItem> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	ServerItem entity = null;
	
	private NginxRealServService nginxRealServService = new NginxRealServService();
	
	private List<ServerItem> serverItemList = null;
	
	protected void prepareModel() throws Exception {
		this.entity = new ServerItem();
	}
	
	public ServerItem getModel() {
		return this.entity;
	}
	
	public String list() throws Exception {
		//System.out.println("server item comming!");
		HttpServletRequest request = Struts2Utils.getRequest();
		String rsgId = request.getParameter("rsgId");
		String rsgName = request.getParameter("rsgName");
	//System.out.println("Get server item ===========");
	//System.out.println("rsgId = " + rsgId);
	//System.out.println("rsgName = " + rsgName);
		if(rsgId == null || rsgId.isEmpty()){
			return null;
		}
		request.setAttribute("rsgId", rsgId);
		request.setAttribute("rsgName", rsgName);
		List<Display> typeList = this.nginxRealServService.getServerTypeList();
		request.setAttribute("typeList", typeList);
		this.serverItemList = this.nginxRealServService.getServerItemList(rsgId);
		return "success";
	}

	public String save() throws Exception {
		HttpServletRequest request = Struts2Utils.getRequest();
		String retMes = "";
		String mesTmp = "";
		String rsgId = request.getParameter("rsgId").trim();
		String serverId = request.getParameter("serverId").trim();
		
		this.entity.setRsgId(rsgId);
		this.entity.setServerId(serverId);
		
		String update = request.getParameter("update").trim();
		if (StringUtils.equals(update.trim(), "1")){
			retMes = this.nginxRealServService.updateServerItem(this.entity);	
			mesTmp = "数据更新";
		}else{
			retMes = this.nginxRealServService.addServerItem(this.entity);
			mesTmp = "数据保存";
		}	
	
		if ( retMes.equals("true") ) {
			Struts2Utils.renderJson("{auth:true,\"mess\":\"" + mesTmp + "\"}", new String[0]);
		} else if ( retMes.equals("false") ) {
			Struts2Utils.renderJson("{auth:false,\"mess\":\"" + mesTmp + "\"}", new String[0]);
		} else {
			Struts2Utils.renderJson("{auth:false,\"mess\":\"" + retMes + "\"}", new String[0]);
		}
		return null;
	}
	
	public String input() throws Exception {
		HttpServletRequest request = Struts2Utils.getRequest();
		String rsgId = request.getParameter("rsgId").trim();
		String serverId = request.getParameter("serverId").trim();
	//System.out.println("Query server item ===========");
	//System.out.println("rsgid : " + rsgId);
	//System.out.println("serverId : " + serverId);
		if (StringUtils.isNotEmpty(rsgId) && StringUtils.isNotEmpty(serverId)) {
			ServerItem tmp = this.nginxRealServService.getServerItemById(rsgId,serverId);
		//System.out.println("input : " + tmp);
			Struts2Utils.renderJson("{\"auth\":true,\"obj\":"
						+ JSONObject.fromObject(tmp).toString() + "}", new String[0]);
		} else {
			Struts2Utils.renderJson("{\"auth\":false}", new String[0]);
		}
		return null;
	}
	
	public String delete() throws Exception {
		HttpServletRequest request = Struts2Utils.getRequest();
		String rsgId = request.getParameter("rsgId").trim();
		String serverId = request.getParameter("serverId").trim();
	//System.out.println("Delete server item ===========");
	//System.out.println("rsgid : " + rsgId);
	//System.out.println("serverId : " + serverId);
		if (StringUtils.isNotEmpty(rsgId) && StringUtils.isNotEmpty(serverId)) {
			boolean flag = this.nginxRealServService.deleteServerItem(rsgId,serverId);
			if (flag)
				Struts2Utils.renderJson("{auth:true}", new String[0]);
			else
				Struts2Utils.renderJson("{auth:false}", new String[0]);
		} else {
			Struts2Utils.renderJson("{auth:false}", new String[0]);
		}
		return null;
	}

	public List<ServerItem> getServerItemList() {
		return serverItemList;
	}

	public void setServerItemList(List<ServerItem> serverItemList) {
		this.serverItemList = serverItemList;
	}

}
