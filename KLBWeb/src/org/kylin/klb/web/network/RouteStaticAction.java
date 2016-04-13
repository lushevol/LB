package org.kylin.klb.web.network;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.kylin.klb.entity.network.RouteStatic;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.service.network.RouteStaticService;
import org.kylin.klb.util.Utils;
import org.kylin.modules.web.struts2.Struts2Utils;

import com.opensymphony.xwork2.ActionSupport;

@Namespace("/network")
@ParentPackage("struts-default")
public class RouteStaticAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	
	private RouteStaticService rss = new RouteStaticService();
	private RouteStatic rsi;
	private List<RouteStatic> rsil;
	private String id;
	private String status;
	private String failedMess;
	
	public String list() throws Exception {		
		rsil = rss.getRouteStaticInfoList();
		return "success";
	}
	public String mess() throws Exception {
		failedMess = Utils.getInstance().getFailedMess();
		rsil = rss.getRouteStaticInfoList();
		return "success";
	}
	/* public String edit() throws Exception {
		rss.editRouteStatus(id, status);
		Struts2Utils.renderJson("{auth:true,\"mess\":\"" + "操作成功" + "\"}", new String[0]);
		return null;
	} */
	public String del() throws Exception {
		Utils.getInstance().setFailedMess("");		
		if ( rss.delRouteById(id) ) {
			Struts2Utils.renderJson("{auth:true}", new String[0]);
		} else {
			Struts2Utils.renderJson("{auth:false}", new String[0]);
		}
		return null;
	}
	public List<RouteStatic> getRsil() {
		return rsil;
	}
	public void setRsil(List<RouteStatic> rsil) {
		this.rsil = rsil;
	}
	public RouteStatic getRsi() {
		return rsi;
	}
	public void setRsi(RouteStatic rsi) {
		this.rsi = rsi;
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
