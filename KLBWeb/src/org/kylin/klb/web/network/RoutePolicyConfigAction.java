package org.kylin.klb.web.network;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Results;
import org.kylin.klb.entity.firewall.Nat;
import org.kylin.klb.entity.network.RoutePolicy;
import org.kylin.klb.entity.network.RoutePolicyConfig;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.service.firewall.NatService;
import org.kylin.klb.service.network.RoutePolicyConfigService;
import org.kylin.klb.util.Utils;
import org.kylin.modules.web.struts2.Struts2Utils;

import com.opensymphony.xwork2.ActionSupport;

@Namespace("/network")
@ParentPackage("struts-default")
@Results(@org.apache.struts2.convention.annotation.Result(name = "reload", location = "route-policy!mess.action", type = "redirect"))
public class RoutePolicyConfigAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	
	private RoutePolicyConfigService rpcs = new RoutePolicyConfigService();
	private RoutePolicyConfig rpc;
	private String id;
	private String operation;
	private String gi;
	
	/* public String getGate() throws Exception {
		String gate = rpcs.getGate(gi);
		if(StringUtils.equals(gate, "")){
			Struts2Utils.renderJson("{auth:false,\"mess\":\"" + "添加失败" + "\"}", new String[0]);
		}
		else{
			Struts2Utils.renderJson("{auth:true,\"gate\":\"" + gate + "\"}", new String[0]);
		}
		return null;
	}
	public String getInter() throws Exception {
		String inter = rpcs.getInter(gi);
		if(StringUtils.equals(inter, "")){
			Struts2Utils.renderJson("{auth:false,\"mess\":\"" + "添加失败" + "\"}", new String[0]);
		}
		else{
			Struts2Utils.renderJson("{auth:true,\"inter\":\"" + inter + "\"}", new String[0]);
		}
		return null;
	} */
	public String list() throws Exception {
		HttpServletRequest request = Struts2Utils.getRequest();
		List<Display> interfaceList = rpcs.getInterfaceList();
		request.setAttribute("interfaceList", interfaceList);
		List<Display> protocolList = rpcs.getProtocolList();
		request.setAttribute("protocolList", protocolList);
		List<Display> gatePolicyList = rpcs.getGatePolicyList();
		request.setAttribute("gatePolicyList", gatePolicyList);
		
		if(StringUtils.equals(operation, "add")||StringUtils.equals(operation, "insert")){
			rpc = new RoutePolicyConfig();
		}
		else{
			rpc = rpcs.getRoutePolicyConfigById(id);
		}
		return "success";
	}
	public String save() throws Exception {
		
		if(StringUtils.equals(operation, "add")){
			String failedMess = null;			
			String routePolicyMess = rpcs.addRoutePolicyConfig(rpc);
			if ( routePolicyMess.equals("true") ) {
				failedMess = "";
			} else if ( routePolicyMess.equals("false") ) {				
				failedMess = "策略路由添加失败";
			} else {
				failedMess = "您添加的路由配置错误：" + routePolicyMess;
			}
			Utils.getInstance().setFailedMess(failedMess);
		}
		if(StringUtils.equals(operation, "edit")){
			rpc.setId(id);
			String failedMess = null;
			String routePolicyMess = rpcs.editRoutePolicyConfig(rpc);
			if ( routePolicyMess.equals("true") ) {
				failedMess = "";
			} else if ( routePolicyMess.equals("false") ) {				
				failedMess = "策略路由" + id + "配置失败";
			} else {
				failedMess = "策略路由" + id + "配置错误：" + routePolicyMess;
			}
			Utils.getInstance().setFailedMess(failedMess);
		}
		if(StringUtils.equals(operation, "insert")){
			String failedMess = null;
			String routePolicyMess = rpcs.insertRoutePolicyConfig(id, rpc);
			if ( routePolicyMess.equals("true") ) {
				failedMess = "";
			} else if ( routePolicyMess.equals("false") ) {				
				failedMess = "策略路由" + id + "插入失败";
			} else {
				failedMess = "您插入的策略路由" + id + "配置错误：" + routePolicyMess;
			}
			Utils.getInstance().setFailedMess(failedMess);
		}
		return "reload";
	}
	public RoutePolicyConfig getRpc() {
		return rpc;
	}
	public void setRpc(RoutePolicyConfig rpc) {
		this.rpc = rpc;
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
}