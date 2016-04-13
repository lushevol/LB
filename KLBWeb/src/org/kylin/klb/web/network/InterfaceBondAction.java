package org.kylin.klb.web.network;

import java.util.List;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.kylin.klb.entity.network.BondInfo;
import org.kylin.klb.service.network.InterfaceBondService;
import org.kylin.klb.util.Utils;
import org.kylin.modules.web.struts2.Struts2Utils;

import com.opensymphony.xwork2.ActionSupport;

@Namespace("/network")
@ParentPackage("struts-default")

public class InterfaceBondAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private InterfaceBondService ibs = new InterfaceBondService();
	private List<BondInfo> bondInfoList;
	private String bondName;
	private String failedMess;
	
	public String list() throws Exception {
		bondInfoList = ibs.getBondInfoList();
		return "success";
	}
	public String mess() throws Exception {
		failedMess = Utils.getInstance().getFailedMess();
		bondInfoList = ibs.getBondInfoList();
		return "success";
	}
	
	public String save() throws Exception {		
		String bondMess =  ibs.addBondName(bondName);
		if ( bondMess.equals("true") ) {
			Struts2Utils.renderJson("{auth:true,\"mess\":\"数据保存成功\"}", new String[0]);
		} else if ( bondMess.equals("false") ) {
			Struts2Utils.renderJson("{auth:false,\"mess\":\"数据保存失败\"}", new String[0]);
		} else {
			Struts2Utils.renderJson("{auth:false,\"mess\":\"" + bondMess + "\"}", new String[0]);
		}
		return null;		
	}
	public String del() throws Exception {
		Utils.getInstance().setFailedMess("");
		if (ibs.delBondByName(bondName)) {
			Struts2Utils.renderJson("{auth:true}", new String[0]);
		} else {
			Struts2Utils.renderJson("{auth:false}", new String[0]);
		}
		return null;
	}
	public List<BondInfo> getBondInfoList() {
		return bondInfoList;
	}
	public void setBondInfoList(List<BondInfo> bondInfoList) {
		this.bondInfoList = bondInfoList;
	}

	public String getBondName() {
		return bondName;
	}

	public void setBondName(String bondName) {
		this.bondName = bondName;
	}

	public String getFailedMess() {
		return failedMess;
	}

	public void setFailedMess(String failedMess) {
		this.failedMess = failedMess;
	}
	
}
