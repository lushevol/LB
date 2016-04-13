package org.kylin.klb.web.nginx;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Results;
import org.kylin.klb.entity.nginx.NginxGlobalConf;
import org.kylin.klb.service.nginx.NginxConfService;
import org.kylin.klb.web.CrudActionSupport;
import org.kylin.modules.web.struts2.Struts2Utils;

@Namespace("/nginx")
@ParentPackage("struts-default")
@Results(@org.apache.struts2.convention.annotation.Result(name = "reload", location = "nginx-conf.action", type = "redirect"))
public class NginxConfAction extends CrudActionSupport<NginxGlobalConf> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	NginxGlobalConf entity = null;
	
	private NginxConfService nginxConfService = new NginxConfService();

	public void prepareExecute() throws Exception {		
		prepareModel();
	}
	
	protected void prepareModel() throws Exception {
		this.entity = this.nginxConfService.getNginxGlobalConf();	
	}
	
	public NginxGlobalConf getModel() {
		return this.entity;
	}
	
	public String list() throws Exception {
		return "success";
	}
	
	public String save() throws Exception {
		String retMes =  this.nginxConfService.setNginxGlobalConf(this.entity);
		if ( retMes.equals("true") ) {
			addActionMessage("参数设置成功");
		} else if ( retMes.equals("false") ) {
			addActionMessage("参数设置失败");
		} else {
			addActionMessage( retMes );
		}
		return "reload";
	}
	
	public String start() throws Exception {
		HttpServletRequest request = Struts2Utils.getRequest();		
		String enabled = request.getParameter("enabled");
		if ( StringUtils.isNotEmpty(enabled) ) {
			String enabledMess =  this.nginxConfService.changeNginxServerStatu(enabled);
			if ( enabledMess.equals("true") ) {
				Struts2Utils.renderJson("{auth:true}", new String[0]);
			} else if ( enabledMess.equals("false") ) {
				Struts2Utils.renderJson("{auth:false,\"mess\":\"操作失败\"}", new String[0]);
			} else {
				Struts2Utils.renderJson("{auth:false,\"mess\":\"" + enabledMess + "\"}", new String[0]);
			}
		}
		return null;
	}


	public String delete() throws Exception {
		
		return null;
	}


	public String input() throws Exception {
		
		return null;
	}

	
	

}
