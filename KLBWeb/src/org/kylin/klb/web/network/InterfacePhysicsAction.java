package org.kylin.klb.web.network;

import java.util.List;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.kylin.klb.entity.network.SimpleInterfaceInfo;
import org.kylin.klb.service.network.InterfacePhysicsService;
import org.kylin.klb.util.Utils;

import com.opensymphony.xwork2.ActionSupport;

@Namespace("/network")
@ParentPackage("struts-default")

public class InterfacePhysicsAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private InterfacePhysicsService ips = new InterfacePhysicsService();
	private List<SimpleInterfaceInfo> iil;
	private String failedMess;
	
	public String list() throws Exception {
		iil = ips.getInterfaceInfoList();
		return "success";
	}
	public String mess() throws Exception {
		failedMess = Utils.getInstance().getFailedMess();
		iil = ips.getInterfaceInfoList();
		return "success";
	}
	
	public List<SimpleInterfaceInfo> getIil() {
		return iil;
	}
	public void setIil(List<SimpleInterfaceInfo> iil) {
		this.iil = iil;
	}

	public String getFailedMess() {
		return failedMess;
	}

	public void setFailedMess(String failedMess) {
		this.failedMess = failedMess;
	}
}
