package org.kylin.klb.web.linkload;

import java.util.List;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.kylin.klb.entity.linkload.DnsIsp;
import org.kylin.klb.service.linkload.DnsIspService;
import org.kylin.klb.util.Utils;
import org.kylin.modules.web.struts2.Struts2Utils;

import com.opensymphony.xwork2.ActionSupport;

@Namespace("/linkload")
//@Results(@org.apache.struts2.convention.annotation.Result(name = "reload", location = "mindDns-isp!list.action", type = "redirect"))
@ParentPackage("struts-default")
public class DnsIspAction extends ActionSupport{
	private static final long serialVersionUID = 1L;

	private DnsIspService dis = new DnsIspService();
	private List<DnsIsp> dil;
	private String id;
	private String failedMess;
	//private String status;
	public String list() throws Exception {
		dil = dis.getDnsIspInfoList();
		return "success";
	}
	public String mess() throws Exception {
		failedMess = Utils.getInstance().getFailedMess();
		dil = dis.getDnsIspInfoList();
		return "success";
	}
			
	/* public String edit() throws Exception {
		ris.editRouteStatus(id, status);
		Struts2Utils.renderJson("{auth:true,\"mess\":\"" + "操作成功" + "\"}",
				new String[0]);
		return null;
	} */
	public String del() throws Exception {
		Utils.getInstance().setFailedMess("");
		if ( dis.delDnsIspById(id) ) {
			Struts2Utils.renderJson("{auth:true}", new String[0]);
		} else {
			Struts2Utils.renderJson("{auth:false}", new String[0]);
		}
		return null;
	}
	
	public List<DnsIsp> getDil() {
		return dil;
	}
	public void setDil(List<DnsIsp> dil) {
		this.dil = dil;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFailedMess() {
		return failedMess;
	}

	public void setFailedMess(String failedMess) {
		this.failedMess = failedMess;
	}
	
}

