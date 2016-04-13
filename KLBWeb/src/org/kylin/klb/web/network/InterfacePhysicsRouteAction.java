package org.kylin.klb.web.network;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Results;
import org.kylin.klb.entity.network.InterfaceInfo;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.service.network.InterfacePhysicsRouteService;
import org.kylin.klb.util.Utils;
import org.kylin.modules.web.struts2.Struts2Utils;

import com.opensymphony.xwork2.ActionSupport;

@Namespace("/network")
//@ParentPackage("struts-default")
@Results( { @org.apache.struts2.convention.annotation.Result(name = "reload", location = "interface-physics!mess.action", type = "redirect"),
	@org.apache.struts2.convention.annotation.Result(name = "list", location = "interface-physics-route!list.action", type = "redirect") })
public class InterfacePhysicsRouteAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private InterfacePhysicsRouteService iprs = new InterfacePhysicsRouteService();
	private InterfaceInfo ii;
	
	private String name;
	
	public String init() throws Exception {
		iprs.initInterfaceInfoList(name);
		return "list";
	}
	
	public String list() throws Exception {
		HttpServletRequest request = Struts2Utils.getRequest();
		List<Display> stateList = iprs.getStateList();
		request.setAttribute("stateList", stateList);
		List<Display> dhcpList = iprs.getDhcpList();
		request.setAttribute("dhcpList", dhcpList);
		
		List<Display> doubleList = iprs.getDoubleList();
		request.setAttribute("doubleList", doubleList);
		List<Display> speedList = iprs.getSpeedList();
		request.setAttribute("speedList", speedList);
		List<Display> arpList = iprs.getArpList();
		request.setAttribute("arpList", arpList);
		
		ii = iprs.getInterfaceInfo();
		return "success";
	}
	
	public String save() throws Exception {
		String failedMess = null;
		String name = ii.getName();
		String physicsMess =  iprs.editInterfaceInfo(ii);
		if ( physicsMess.equals("true") ) {
			failedMess = "";
		} else if ( physicsMess.equals("false") ) {
			failedMess = "物理接口" + name + "设置失败";
		} else {
			failedMess = name + "配置错误：" + physicsMess;
		}
		Utils.getInstance().setFailedMess(failedMess);
		return "reload";
	}
	public String resumeMac() throws Exception {		
		String mac = iprs.getResumeMac();
		if ( !mac.equals("") ) {
			Struts2Utils.renderJson("{auth:true,\"mac\":\"" + mac + "\"}", new String[0]);
		} else {
			Struts2Utils.renderJson("{auth:false}", new String[0]);
		}
		return null;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public InterfaceInfo getIi() {
		return ii;
	}
	public void setIi(InterfaceInfo ii) {
		this.ii = ii;
	}
}
