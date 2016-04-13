package org.kylin.klb.web.network;

import java.util.List;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.kylin.klb.entity.network.RoutePolicy;
import org.kylin.klb.service.network.RoutePolicyService;
import org.kylin.klb.util.Utils;
import org.kylin.modules.web.struts2.Struts2Utils;

import com.opensymphony.xwork2.ActionSupport;

@Namespace("/network")
@ParentPackage("struts-default")
public class RoutePolicyAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	
	private RoutePolicyService rps = new RoutePolicyService();
	private RoutePolicy rpi;
	private List<RoutePolicy> rpil;
	private String oldId;
	private String id;
	private String status;
	private String failedMess;
	public String list() throws Exception {
		rpil = rps.getRoutePolicyInfoList();
		return "success";
	}
	public String mess() throws Exception {
		failedMess = Utils.getInstance().getFailedMess();
		rpil = rps.getRoutePolicyInfoList();
		return "success";
	}
	public String move() throws Exception {
		Utils.getInstance().setFailedMess("");
		boolean status = rps.move(oldId, id);
		if(status == true){
			Struts2Utils.renderJson("{auth:true,\"mess\":\"移动成功\"}", new String[0]);
		}
		else{
			Struts2Utils.renderJson("{auth:true,\"mess\":\"移动失败\"}", new String[0]);
		}
		return null;
	}
	/* public String edit() throws Exception {
		Utils.getInstance().setFailedMess("");
		rps.editRouteStatus(id, status);
		Struts2Utils.renderJson("{auth:true,\"mess\":\"" + "操作成功" + "\"}", new String[0]);
		return null;
	} */
	public String del() throws Exception {
		Utils.getInstance().setFailedMess("");		
		if ( rps.delRouteById(id) ) {
			Struts2Utils.renderJson("{auth:true}", new String[0]);
		} else {
			Struts2Utils.renderJson("{auth:false}", new String[0]);
		}
		return null;
	}
	public RoutePolicy getRpi() {
		return rpi;
	}
	public void setRpi(RoutePolicy rpi) {
		this.rpi = rpi;
	}
	public List<RoutePolicy> getRpil() {
		return rpil;
	}
	public void setRpil(List<RoutePolicy> rpil) {
		this.rpil = rpil;
	}
	public String getOldId() {
		return oldId;
	}
	public void setOldId(String oldId) {
		this.oldId = oldId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFailedMess() {
		return failedMess;
	}
	public void setFailedMess(String failedMess) {
		this.failedMess = failedMess;
	}
}
