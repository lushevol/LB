package org.kylin.klb.web.security;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Results;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.entity.security.LoadSirtualServ;
import org.kylin.klb.service.KlbManager;
import org.kylin.klb.service.SirtualServService;
import org.kylin.klb.util.Utils;
import org.kylin.klb.web.CrudActionSupport;
import org.kylin.modules.web.struts2.Struts2Utils;
import org.springframework.beans.factory.annotation.Autowired;

@Namespace("/")
@Results( { @org.apache.struts2.convention.annotation.Result(name = "reload", location = "klb-load-sirtual-serv.action", type = "redirect") })
public class KlbLoadSirtualServAction extends CrudActionSupport<LoadSirtualServ> {
	private static final long serialVersionUID = 1L;

	@Autowired
	private KlbManager klbManager;
	LoadSirtualServ entity = null;

	private LoadSirtualServ loadsirtualserv = new LoadSirtualServ();
	
	private SirtualServService sirtualservservice = new SirtualServService();
	
	private List<LoadSirtualServ> sirtualServ = Collections.EMPTY_LIST;

	protected void prepareModel() throws Exception {
		this.entity = new LoadSirtualServ();
	}

	public void prepareDelete() throws Exception {
		prepareModel();
	}

	public LoadSirtualServ getModel() {
		return this.entity;
	}

	public String save() throws Exception {
		
		HttpServletRequest request = Struts2Utils.getRequest();
		String update = request.getParameter("update");

		String oldService = request.getParameter("oldService");
		String service = this.entity.getService();
		if (StringUtils.equals(oldService.trim(), "")){
			update = "0";
		}
		else{
			update = "1";
		}		
		//if (StringUtils.isNotEmpty(id)) {
		if (StringUtils.equalsIgnoreCase(oldService, service)) {
			if (StringUtils.equalsIgnoreCase(update, "0")) {
				String vsMess =  this.klbManager.setSirtualServ(this.entity);
				if ( vsMess.equals("true") ) {
					Struts2Utils.renderJson("{auth:true,\"mess\":\"数据保存成功\"}", new String[0]);
				} else if ( vsMess.equals("false") ) {
					Struts2Utils.renderJson("{auth:false,\"mess\":\"数据保存失败\"}", new String[0]);
				} else {
					Struts2Utils.renderJson("{auth:false,\"mess\":\"" + vsMess + "\"}", new String[0]);
				}
			} else if (StringUtils.equalsIgnoreCase(update, "1")) {								
				String vsMess =  this.klbManager.updateSirtualServ(this.entity);
				System.out.println(vsMess);
				if ( vsMess.equals("true") ) {
					Struts2Utils.renderJson("{auth:true,\"mess\":\"数据更新成功\"}", new String[0]);
				} else if ( vsMess.equals("false") ) {
					Struts2Utils.renderJson("{auth:false,\"mess\":\"数据更新失败\"}", new String[0]);
				} else {
					Struts2Utils.renderJson("{auth:false,\"mess\":\"" + vsMess + "\"}", new String[0]);
				}
			}
		} else {
			LoadSirtualServ ls = this.klbManager.getSirtualServByName(service);
			if (ls != null) {
				Struts2Utils.renderJson("{auth:false,\"mess\":\"虚拟服务名称已经存在\"}", new String[0]);
			} else if (StringUtils.equalsIgnoreCase(update, "0")) {								
				String vsMess =  this.klbManager.setSirtualServ(this.entity);
				if ( vsMess.equals("true") ) {
					Struts2Utils.renderJson("{auth:true,\"mess\":\"数据保存成功\"}", new String[0]);
				} else if ( vsMess.equals("false") ) {
					Struts2Utils.renderJson("{auth:false,\"mess\":\"数据保存失败\"}", new String[0]);
				} else {
					Struts2Utils.renderJson("{auth:false,\"mess\":\"" + vsMess + "\"}", new String[0]);
				}
			} else if (StringUtils.equalsIgnoreCase(update, "1")) {								
				String vsMess =  this.klbManager.updateSirtualServ(this.entity);
				if ( vsMess.equals("true") ) {
					Struts2Utils.renderJson("{auth:true,\"mess\":\"数据更新成功\"}", new String[0]);
				} else if ( vsMess.equals("false") ) {
					Struts2Utils.renderJson("{auth:false,\"mess\":\"数据更新失败\"}", new String[0]);
				} else {
					Struts2Utils.renderJson("{auth:false,\"mess\":\"" + vsMess + "\"}", new String[0]);
				}				
			}
		}
		/* } else {
			Struts2Utils.renderJson("{auth:false,\"mess\":\"虚拟服务名称 不能为空\"}",
					new String[0]);
		} */
		return null;
	}

	public String list() throws Exception {
		//
		HttpServletRequest request = Struts2Utils.getRequest();

		//List scheduler = this.klbManager.getScheduler();
		//request.setAttribute("schedulerList", scheduler);
		List<Display> haTypeList = this.sirtualservservice.getHaTypeList();
		request.setAttribute("haTypeList", haTypeList);

		//Map interfaces = this.sirtualservservice.getinterfacesMap1();
		//request.setAttribute("interfacesMap1", interfaces);
		
		List<Display> interfacesList = this.sirtualservservice.getinterfacesList();
		request.setAttribute("interfacesList", interfacesList);

		// Set interfaces = this.klbManager.getInterfaces();
		// request.setAttribute("interfacesList", interfaces);

		Map schedulings = Utils.getInstance().getSchedulings();
		request.setAttribute("schedulings", schedulings);

		this.sirtualServ = this.klbManager.getSirtualServInfo();
		return "success";
	}

	public String delete() throws Exception {
		HttpServletRequest request = Struts2Utils.getRequest();
		String id = request.getParameter("vsId");

		if (StringUtils.isNotEmpty(id)) {
			boolean flag = this.klbManager.removeSirtualServ(id);
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
		String id = this.entity.getVsId();
		if (StringUtils.isNotEmpty(id)) {
			LoadSirtualServ ls = this.klbManager.getSirtualServById(id);
			Struts2Utils.renderJson("{\"auth\":true,\"obj\":"
						+ JSONObject.fromObject(ls).toString() + "}", new String[0]);
		} else {
			Struts2Utils.renderJson("{\"auth\":false}", new String[0]);
		}
		return null;
	}

	public String start() throws Exception {
		HttpServletRequest request = Struts2Utils.getRequest();
		boolean flag = false;

		String id = request.getParameter("vsId");
		String status = request.getParameter("status");
		if ((StringUtils.isNotEmpty(id)) && (StringUtils.isNotEmpty(status))) {
			flag = this.klbManager.setSirtualServStatus(id, status);
		}
		if (flag)
			Struts2Utils.renderJson("{auth:true}", new String[0]);
		else {
			Struts2Utils.renderJson("{auth:false}", new String[0]);
		}
		return null;
	}

	public List<LoadSirtualServ> getSirtualServ() {
		return this.sirtualServ;
	}

	public void setSirtualServ(List<LoadSirtualServ> sirtualServ) {
		this.sirtualServ = sirtualServ;
	}
}
