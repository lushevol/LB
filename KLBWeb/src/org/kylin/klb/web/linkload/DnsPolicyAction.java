package org.kylin.klb.web.linkload;

import java.util.List;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.kylin.klb.entity.linkload.DnsPolicy;
import org.kylin.klb.service.linkload.DnsPolicyService;
import org.kylin.klb.util.Utils;
import org.kylin.modules.web.struts2.Struts2Utils;

import com.opensymphony.xwork2.ActionSupport;

@Namespace("/linkload")
@ParentPackage("struts-default")
public class DnsPolicyAction extends ActionSupport {
	private static final long serialVersionUID = 1L;

	private DnsPolicyService dps = new DnsPolicyService();
	private List<DnsPolicy> dpl;
	private String id;
	//private String status;
	public String list() throws Exception {
		dpl = dps.getDnsPolicyInfoList();
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
		if ( dps.delDnsPolicyById(id) ) {
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

	public List<DnsPolicy> getDpl() {
		return dpl;
	}

	public void setDpl(List<DnsPolicy> dpl) {
		this.dpl = dpl;
	}
}
