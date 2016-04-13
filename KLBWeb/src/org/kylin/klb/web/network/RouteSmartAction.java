package org.kylin.klb.web.network;

import java.util.List;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.kylin.klb.entity.network.RouteSmart;
import org.kylin.klb.service.network.RouteSmartService;
import org.kylin.klb.util.Utils;
import org.kylin.modules.web.struts2.Struts2Utils;

import com.opensymphony.xwork2.ActionSupport;

@Namespace("/network")
@ParentPackage("struts-default")
public class RouteSmartAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	
	private RouteSmartService rss = new RouteSmartService();
	private List<RouteSmart> rsil;
	private String id;
	private String status;
	private String failedMess;
	public String list() throws Exception {
		rsil = rss.getRouteSmartInfoList();
		return "success";
	}
	public String mess() throws Exception {
		failedMess = Utils.getInstance().getFailedMess();
		rsil = rss.getRouteSmartInfoList();
		return "success";
	}
	
	public String del() throws Exception {
		Utils.getInstance().setFailedMess("");
		if ( rss.delRouteById(id) ) {
			Struts2Utils.renderJson("{auth:true}", new String[0]);
		} else {
			Struts2Utils.renderJson("{auth:false}", new String[0]);
		}		
		return null;
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
	public List<RouteSmart> getRsil() {
		return rsil;
	}
	public void setRsil(List<RouteSmart> rsil) {
		this.rsil = rsil;
	}
}