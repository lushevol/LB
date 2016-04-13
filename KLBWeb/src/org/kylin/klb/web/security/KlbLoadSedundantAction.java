package org.kylin.klb.web.security;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Results;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.entity.security.LoadSedundant;
import org.kylin.klb.service.KlbManager;
import org.kylin.klb.service.SedundantService;
import org.kylin.klb.web.CrudActionSupport;
import org.kylin.modules.web.struts2.Struts2Utils;
import org.springframework.beans.factory.annotation.Autowired;

@Namespace("/")
@Results( { @org.apache.struts2.convention.annotation.Result(name = "reload", location = "klb-load-sedundant!input.action", type = "redirect") })
public class KlbLoadSedundantAction extends CrudActionSupport<LoadSedundant> {
	private static final long serialVersionUID = 1L;
	private LoadSedundant entity;
	private SedundantService sedundantService = new SedundantService();
	// private Set interfaces = this.klbManager.getInterfaces();
	@Autowired
	private KlbManager klbManager;

	public LoadSedundant getModel() {
		return this.entity;
	}

	protected void prepareModel() throws Exception {

		this.entity = this.klbManager.getSedundant();

		if (this.entity != null)
			return;
		this.entity = new LoadSedundant();
	}

	public String delete() throws Exception {
		return null;
	}

	public String input() throws Exception {
		HttpServletRequest request = Struts2Utils.getRequest();

		//Map interfacesMap = this.sedundantService.getinterfacesMap();
		//request.setAttribute("interfacesMap", interfacesMap);		
		List<Display> interfacesList = this.sedundantService.getinterfacesList();
		request.setAttribute("interfacesList", interfacesList);

		//List scheduler = this.klbManager.getSchedulerForSedundant();
		//request.setAttribute("schedulerList", scheduler);
		return "success";
	}

	public String list() throws Exception {
		HttpServletRequest request = Struts2Utils.getRequest();

		//List scheduler = this.klbManager.getSchedulerForSedundant();
		//request.setAttribute("schedulerList", scheduler);
		
		return "success";
	}

	public String save() throws Exception {						
		String sedundantMess =  this.klbManager.setSedundant(this.entity);
		if ( sedundantMess.equals("true") ) {
			addActionMessage("高可用参数设置成功");
		} else if ( sedundantMess.equals("false") ) {
			addActionMessage("高可用参数设置失败");
		} else {
			addActionMessage( sedundantMess );
		}
		return "reload";
	}
	
	public String start() throws Exception {
		HttpServletRequest request = Struts2Utils.getRequest();		
		String enabled = request.getParameter("enabled");
		if ( StringUtils.isNotEmpty(enabled) ) {
			String enabledMess =  this.klbManager.setSedundantStatus(enabled);
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

	public SedundantService getSedundantService() {
		return sedundantService;
	}

	public void setSedundantService(SedundantService sedundantService) {
		this.sedundantService = sedundantService;
	}

}
