package org.kylin.klb.web.nginx;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Results;
import org.kylin.klb.entity.nginx.RealServerGroup;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.service.nginx.NginxRealServService;
import org.kylin.klb.web.CrudActionSupport;
import org.kylin.modules.web.struts2.Struts2Utils;

@Namespace("/nginx")
@ParentPackage("struts-default")
@Results(@org.apache.struts2.convention.annotation.Result(name = "reload", location = "nginx-real-serv.action", type = "redirect"))
public class NginxRealServAction extends CrudActionSupport<RealServerGroup> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	RealServerGroup entity = null;
	
	private NginxRealServService nginxRealServService = new NginxRealServService();
	
	private List<RealServerGroup> realServerGroupList = null;
	
	

	protected void prepareModel() throws Exception {
		this.entity = new RealServerGroup();
	}
	
	public RealServerGroup getModel() {
		return this.entity;
	}
	
	public String list() throws Exception {
		HttpServletRequest request = Struts2Utils.getRequest();
		
		List<Display> methodList = this.nginxRealServService.getMethodDisplayList();
		request.setAttribute("methodList", methodList);
		
		this.realServerGroupList = this.nginxRealServService.getRealServerGroupList();
		
		return "success";
	}

	public String save() throws Exception {
		HttpServletRequest request = Struts2Utils.getRequest();
		String update = request.getParameter("update").trim();
		String oldName = request.getParameter("oldName").trim();
		String newName = this.entity.getName();	
		String retMes = "";
		String mesTmp = "";
	//System.out.println("newName = " + newName);
	//System.out.println("oldName = " + oldName);
	//System.out.println("update = " + update);
		if(!StringUtils.equals(oldName, newName)){
			RealServerGroup tmp = this.nginxRealServService.getRealServerGroupByName(newName);
			if(tmp != null){
				Struts2Utils.renderJson("{auth:false,\"mess\":\"真实服务器组名称已经存在\"}", new String[0]);
				return null;
			}
		}
		if (StringUtils.equals(update.trim(), "1")){
			retMes = this.nginxRealServService.updateRealServerGroup(this.entity);	
			mesTmp = "数据更新";
		}else{
			retMes = this.nginxRealServService.addRealServerGroup(this.entity);
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
		String rsgId = this.entity.getRsgId();
		if (StringUtils.isNotEmpty(rsgId)) {
			RealServerGroup tmp = this.nginxRealServService.getRealServerGroupById(rsgId);
			Struts2Utils.renderJson("{\"auth\":true,\"obj\":"
						+ JSONObject.fromObject(tmp).toString() + "}", new String[0]);
		} else {
			Struts2Utils.renderJson("{\"auth\":false}", new String[0]);
		}
		return null;
	}

	
	public String delete() throws Exception {
		HttpServletRequest request = Struts2Utils.getRequest();
		String rsgId = request.getParameter("rsgId");

		if (StringUtils.isNotEmpty(rsgId)) {
			boolean flag = this.nginxRealServService.deleteRealServerGroup(rsgId);
			if (flag)
				Struts2Utils.renderJson("{auth:true}", new String[0]);
			else
				Struts2Utils.renderJson("{auth:false}", new String[0]);
		} else {
			Struts2Utils.renderJson("{auth:false}", new String[0]);
		}
		return null;
	}


	
	public List<RealServerGroup> getRealServerGroupList() {
		return realServerGroupList;
	}

	public void setRealServerGroupList(List<RealServerGroup> realServerGroupList) {
		this.realServerGroupList = realServerGroupList;
	}

	

	

}
