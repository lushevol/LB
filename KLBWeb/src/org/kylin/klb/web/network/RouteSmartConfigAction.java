package org.kylin.klb.web.network;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Results;
import org.kylin.klb.entity.network.RouteSmartConfig;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.service.network.RouteSmartConfigService;
import org.kylin.klb.util.Utils;
import org.kylin.modules.web.struts2.Struts2Utils;

import com.opensymphony.xwork2.ActionSupport;

@Namespace("/network")
@ParentPackage("struts-default")
@Results(@org.apache.struts2.convention.annotation.Result(name = "reload", location = "route-smart!mess.action", type = "redirect"))
public class RouteSmartConfigAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	
	private RouteSmartConfigService rscs = new RouteSmartConfigService();
	private RouteSmartConfig rsc;
	private String id;
	private String operation;
	private String gi;
	
	public String list() throws Exception {
		HttpServletRequest request = Struts2Utils.getRequest();
		List<Display> interfaceList = rscs.getInterfaceList();
		request.setAttribute("interfaceList", interfaceList);
		List<Display> gatePolicyList = rscs.getGatePolicyList();
		request.setAttribute("gatePolicyList", gatePolicyList);
		List<Display> ispNameList = rscs.getIspNameList();
		request.setAttribute("ispNameList", ispNameList);
		List<Display> modeList = rscs.getModeList();
		request.setAttribute("modeList", modeList);
		
		if( StringUtils.equals(operation, "add") ){
			rsc = new RouteSmartConfig();
		} else {
			rsc = rscs.getRouteSmartConfigById(id);
		}
		return "success";
	}
	public String save() throws Exception {
		
		if(StringUtils.equals(operation, "add")){
			String failedMess = null;			
			String routeSmartMess = rscs.addRouteSmartConfig(rsc);
			if ( routeSmartMess.equals("true") ) {
				failedMess = "";
			} else if ( routeSmartMess.equals("false") ) {				
				failedMess = "智能路由添加失败";
			} else {
				failedMess = "您添加的路由配置错误：" + routeSmartMess;
			}
			Utils.getInstance().setFailedMess(failedMess);
		}
		if(StringUtils.equals(operation, "edit")){
			rsc.setId(id);
			String failedMess = null;
			String routeSmartMess = rscs.editRouteSmartConfig(rsc);
			if ( routeSmartMess.equals("true") ) {
				failedMess = "";
			} else if ( routeSmartMess.equals("false") ) {				
				failedMess = "智能路由" + id + "配置失败";
			} else {
				failedMess = "智能路由" + id + "配置错误：" + routeSmartMess;
			}
			Utils.getInstance().setFailedMess(failedMess);
		}
		return "reload";
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getGi() {
		return gi;
	}
	public void setGi(String gi) {
		this.gi = gi;
	}
	public RouteSmartConfig getRsc() {
		return rsc;
	}
	public void setRsc(RouteSmartConfig rsc) {
		this.rsc = rsc;
	}
}