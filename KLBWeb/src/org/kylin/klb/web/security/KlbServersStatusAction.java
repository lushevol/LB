package org.kylin.klb.web.security;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Results;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.entity.security.Monitor;
import org.kylin.klb.entity.security.Server;
import org.kylin.klb.service.KlbManager;
import org.kylin.klb.service.MonitorService;
import org.kylin.klb.service.ServersStatusService;
import org.kylin.modules.web.struts2.Struts2Utils;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

@Namespace("/")
@ParentPackage("struts-default")
@Results( { @org.apache.struts2.convention.annotation.Result(name = "reload", location = "klb-servers-status.action", type = "redirect") })
public class KlbServersStatusAction extends ActionSupport {
	private static final long serialVersionUID = 1L;

	@Autowired
	private KlbManager klbManager;
	Monitor entity = null;
	String serviceName;
	String vsId;

	private List<Display> typeList;
	private List<Server> servers;
	private List<Monitor> monitors;
	private ServersStatusService serversStatusService = new ServersStatusService();
	private MonitorService monitorService = new MonitorService();

	public String execute() throws Exception {
		// HttpServletRequest request = Struts2Utils.getRequest();
		this.monitors = monitorService.convert();

		this.servers = serversStatusService.getServers();
		this.typeList = monitorService.getTypeList();
		// Map forwards = Utils.getInstance().getForwards();
		// request.setAttribute("forwards", forwards);
		return "success";
	}

	public String save() throws Exception {

		HttpServletRequest request = Struts2Utils.getRequest();
		String update = request.getParameter("update");
		String oldService = request.getParameter("oldServiceName");
		String service = this.entity.getServiceName();

		//if (StringUtils.isNotEmpty(service)) {
		if (StringUtils.equalsIgnoreCase(oldService, service)) {
			if (StringUtils.equalsIgnoreCase(update, "0")) {
				this.monitorService.saveServiceMonitor(this.entity);
				Struts2Utils.renderJson("{auth:true,\"mess\":\"数据保存成功\"}", new String[0]);
			} else if (StringUtils.equalsIgnoreCase(update, "1")) {								
				String serverStatMess = this.monitorService.saveServiceMonitor(this.entity);
				if ( serverStatMess.equals("true") ) {
					Struts2Utils.renderJson("{auth:true,\"mess\":\"数据更新成功\"}", new String[0]);
				} else if ( serverStatMess.equals("false") ) {
					Struts2Utils.renderJson("{auth:false,\"mess\":\"数据更新失败\"}", new String[0]);
				} else {
					Struts2Utils.renderJson("{auth:false,\"mess\":\"" + serverStatMess + "\"}", new String[0]);
				}								
			}
		} else {
			Monitor ls = this.monitorService.getMonitorByName(service);
			if (ls != null) {
				Struts2Utils.renderJson("{auth:false,\"mess\":\"虚拟服务名称已经存在\"}", new String[0]);
			} else if (StringUtils.equalsIgnoreCase(update, "0")) {
				this.monitorService.saveServiceMonitor(this.entity);
				Struts2Utils.renderJson("{auth:true,\"mess\":\"数据保存成功\"}", new String[0]);
			} else if (StringUtils.equalsIgnoreCase(update, "1")) {
				this.monitorService.saveServiceMonitor(this.entity);
				Struts2Utils.renderJson("{auth:true,\"mess\":\"数据更新成功\"}", new String[0]);
			}
		}

		/* } else {
			Struts2Utils.renderJson("{auth:false,\"mess\":\"虚拟服务名称 不能为空\"}", new String[0]);
		} */
		return null;
	}

	public String input() throws Exception {
		String vsId = this.getVsId();
		if (StringUtils.isNotEmpty(vsId)) {
			Monitor mon = this.monitorService.getMonitorById(vsId);
			Struts2Utils.renderJson("{\"auth\":true,\"obj\":" + JSONObject.fromObject(mon).toString() + "}", new String[0]);
		} else {
			Struts2Utils.renderJson("{\"auth\":false}", new String[0]);
		}
		return null;
	}

	public List<Server> getServers() {
		return servers;
	}

	public void setServers(List<Server> servers) {
		this.servers = servers;
	}

	public List<Monitor> getMonitors() {
		return monitors;
	}

	public void setMonitors(List<Monitor> monitors) {
		this.monitors = monitors;
	}

	public Monitor getEntity() {
		return entity;
	}

	public void setEntity(Monitor entity) {
		this.entity = entity;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public List<Display> getTypeList() {
		return typeList;
	}

	public void setTypeList(List<Display> typeList) {
		this.typeList = typeList;
	}

	public String getVsId() {
		return vsId;
	}

	public void setVsId(String vsId) {
		this.vsId = vsId;
	}

}
