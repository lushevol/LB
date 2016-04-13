package org.kylin.klb.web.network;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Results;
import org.kylin.klb.entity.network.RouteStaticConfig;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.service.network.RouteStaticConfigService;
import org.kylin.klb.util.Utils;
import org.kylin.modules.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionSupport;

@Namespace("/network")
//@ParentPackage("struts-default")
@Results( {@org.apache.struts2.convention.annotation.Result(name = "reload", location = "route-static!mess.action", type = "redirect") })
public class RouteStaticConfigAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private RouteStaticConfigService rscs = new RouteStaticConfigService();
	private String id;
	private String gi;
	private RouteStaticConfig rsc;
	
	public String list() throws Exception {
		//System.out.println(id);
		HttpServletRequest request = Struts2Utils.getRequest();
		List<Display> interfaceList = rscs.getInterfaceList();
		request.setAttribute("interfaceList", interfaceList);
		List<Display> gatePolicyList = rscs.getGatePolicyList();
		request.setAttribute("gatePolicyList", gatePolicyList);
		
		if(StringUtils.equals(id, "add")) {
			rsc = new RouteStaticConfig();
			rsc.setId(id);
			//rsc.setGatePolicy("0");
		} else {
			rsc = rscs.getRouteStaticConfigById(id);
			rsc.setId(id);
		}
		return "success";
	}
		
	public String save() throws Exception {
		String staticId = rsc.getId();
		if(StringUtils.equals(staticId, "add")) {
			String failedMess = null;
			String routeStaticMess =  rscs.addRouteStaticConfig(rsc);			
			if ( routeStaticMess.equals("true") ) {			
				failedMess = "";
			} else if ( routeStaticMess.equals("false") ) {				
				failedMess = "静态路由添加失败";
			} else {
				failedMess = "您添加的路由配置错误：" + routeStaticMess;
			}
			Utils.getInstance().setFailedMess(failedMess);
		} else {
			String failedMess = null;
			String routeStaticMess =  rscs.editRouteStaticConfig(rsc);			
			if ( routeStaticMess.equals("true") ) {
				failedMess = "";
			} else if ( routeStaticMess.equals("false") ) {				
				failedMess = "静态路由" + staticId + "配置失败";
			} else {
				failedMess = "静态路由" + staticId + "配置错误：" + routeStaticMess;
			}
			Utils.getInstance().setFailedMess(failedMess);
		}
		return "reload";
	}
	
	/* public String getGate() throws Exception {
		String gate = rscs.getGate(gi);
		if(StringUtils.equals(gate, "")){
			Struts2Utils.renderJson("{auth:false,\"mess\":\"" + "添加失败" + "\"}", new String[0]);
		}
		else{
			Struts2Utils.renderJson("{auth:true,\"gate\":\"" + gate + "\"}", new String[0]);
		}
		return null;
	}
	public String getInter() throws Exception {
		String inter = rscs.getInter(gi);
		if(StringUtils.equals(inter, "")){
			Struts2Utils.renderJson("{auth:false,\"mess\":\"" + "添加失败" + "\"}", new String[0]);
		}
		else{
			Struts2Utils.renderJson("{auth:true,\"inter\":\"" + inter + "\"}", new String[0]);
		}
		return null;
	} */
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public RouteStaticConfig getRsc() {
		return rsc;
	}

	public void setRsc(RouteStaticConfig rsc) {
		this.rsc = rsc;
	}

	public String getGi() {
		return gi;
	}

	public void setGi(String gi) {
		this.gi = gi;
	}
	
}
