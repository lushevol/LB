package org.kylin.klb.web.nginx;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Results;
import org.kylin.klb.entity.nginx.VirtualServerGroup;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.service.nginx.NginxVirtualServService;
import org.kylin.klb.web.CrudActionSupport;
import org.kylin.modules.web.struts2.Struts2Utils;

@Namespace("/nginx")
@ParentPackage("struts-default")
@Results(@org.apache.struts2.convention.annotation.Result(name = "reload", location = "nginx-virtual-serv.action", type = "redirect"))
public class NginxVirtualServAction extends CrudActionSupport<VirtualServerGroup> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	
	VirtualServerGroup entity = null;
	
	private NginxVirtualServService nginxVirtualServService = new NginxVirtualServService();
	
	private List<VirtualServerGroup> virtualServerGroupList = null;
	

	

	protected void prepareModel() throws Exception {
		this.entity = new VirtualServerGroup();
	}
	
	public VirtualServerGroup getModel() {
		return this.entity;
	}

	public String list() throws Exception {
		HttpServletRequest request = Struts2Utils.getRequest();
		
		List<Display> devList = this.nginxVirtualServService.getDevList();
		request.setAttribute("devList", devList);
		
		List<Display> haTypeList = this.nginxVirtualServService.getHaTypeList();
		request.setAttribute("haTypeList", haTypeList);
		
		this.virtualServerGroupList = this.nginxVirtualServService.getVirtualServerGroupList();
		
		return "success";
	}
	

	public String delete() throws Exception {
		HttpServletRequest request = Struts2Utils.getRequest();
		String vsId = request.getParameter("vsId");

		if (StringUtils.isNotEmpty(vsId)) {
			boolean flag = this.nginxVirtualServService.deleteVirtualServer(vsId);
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
		String vsId = this.entity.getVsId();
		if (StringUtils.isNotEmpty(vsId)) {
			VirtualServerGroup tmp = this.nginxVirtualServService.getVirtualServerById(vsId);
			Struts2Utils.renderJson("{\"auth\":true,\"obj\":"
						+ JSONObject.fromObject(tmp).toString() + "}", new String[0]);
		} else {
			Struts2Utils.renderJson("{\"auth\":false}", new String[0]);
		}
		return null;
	}
	
	

	public String start() throws Exception {
		
		return null;
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
			VirtualServerGroup tmp = this.nginxVirtualServService.getVirtualServerByName(newName);
			if(tmp != null){
				Struts2Utils.renderJson("{auth:false,\"mess\":\"虚拟服务名称已经存在\"}", new String[0]);
				return null;
			}
		}
		if (StringUtils.equals(update.trim(), "1")){
			retMes = this.nginxVirtualServService.updateVirtualServer(this.entity);	
			mesTmp = "数据更新";
		}else{
			retMes = this.nginxVirtualServService.addVirtualServer(this.entity);
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

	public List<VirtualServerGroup> getVirtualServerGroupList() {
		return virtualServerGroupList;
	}

	public void setVirtualServerGroupList(
			List<VirtualServerGroup> virtualServerGroupList) {
		this.virtualServerGroupList = virtualServerGroupList;
	}

	
	
}
