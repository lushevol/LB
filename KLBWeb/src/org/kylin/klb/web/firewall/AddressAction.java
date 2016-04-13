package org.kylin.klb.web.firewall;

import java.util.List;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.kylin.klb.entity.firewall.Nat;
import org.kylin.klb.service.firewall.NatService;
import org.kylin.klb.util.Utils;
import org.kylin.modules.web.struts2.Struts2Utils;

import com.opensymphony.xwork2.ActionSupport;

@Namespace("/firewall")
@ParentPackage("struts-default")
public class AddressAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private NatService ns = new NatService();
	private List<Nat> natList;
	private String id;
	private String oldId;
	private String status;
	private String enabled;
	private String failedMess;
	
	public String list() throws Exception {
		natList = ns.getNatList();
		return "success";
	}
	public String mess() throws Exception {
		failedMess = Utils.getInstance().getFailedMess();
		natList = ns.getNatList();
		return "success";
	}
	
	public String move() throws Exception {
		boolean status = ns.move(oldId, id);
		if(status == true){
			Struts2Utils.renderJson("{auth:true,\"mess\":\"移动成功\"}", new String[0]);
		}
		else{
			Struts2Utils.renderJson("{auth:true,\"mess\":\"移动失败\"}", new String[0]);
		}
		return null;
	}
	
	public String start() throws Exception {
		String editMess =  ns.editNatEnabled(id, enabled);
		if ( editMess.equals("true") ) {
			Struts2Utils.renderJson("{auth:true}", new String[0]);
		} else if ( editMess.equals("false") ) {
			Struts2Utils.renderJson("{auth:false,\"mess\":\"\"}", new String[0]);
		} else {
			Struts2Utils.renderJson("{auth:false,\"mess\":\"" + editMess + "\"}", new String[0]);
		}
		return null;
	}
	
	public String del() throws Exception {
		Utils.getInstance().setFailedMess("");
		if ( ns.delNatById(id) ) {
			Struts2Utils.renderJson("{auth:true}", new String[0]);
		} else {
			Struts2Utils.renderJson("{auth:false}", new String[0]);
		}		
		return null;
	}
	public List<Nat> getNatList() {
		return natList;
	}
	public void setNatList(List<Nat> natList) {
		this.natList = natList;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOldId() {
		return oldId;
	}
	public void setOldId(String oldId) {
		this.oldId = oldId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	public String getFailedMess() {
		return failedMess;
	}
	public void setFailedMess(String failedMess) {
		this.failedMess = failedMess;
	}
	
}
