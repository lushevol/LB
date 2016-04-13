package org.kylin.klb.web.network;

import java.util.List;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.kylin.klb.entity.network.Dhcp;
import org.kylin.klb.service.network.DhcpService;
import org.kylin.modules.web.struts2.Struts2Utils;

import com.opensymphony.xwork2.ActionSupport;

@Namespace("/network")
@ParentPackage("struts-default")
public class DhcpAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	
	private DhcpService ds = new DhcpService();
	private List<Dhcp> dl;
	private String inter;
	private String status;
	public String list() throws Exception {
		dl = ds.getDhcpList();
		return "success";
	}
	public String edit() throws Exception {
		ds.editDhcpStatus(inter, status);
		Struts2Utils.renderJson("{auth:true,\"mess\":\"" + "操作成功" + "\"}",
				new String[0]);
		return null;
	}
	public List<Dhcp> getDl() {
		return dl;
	}
	public void setDl(List<Dhcp> dl) {
		this.dl = dl;
	}
	public String getInter() {
		return inter;
	}
	public void setInter(String inter) {
		this.inter = inter;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
