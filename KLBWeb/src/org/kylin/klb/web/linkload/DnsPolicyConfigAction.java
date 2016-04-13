package org.kylin.klb.web.linkload;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Results;
import org.kylin.klb.entity.linkload.DnsPolicyConfig;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.service.linkload.DnsPolicyConfigService;
import org.kylin.klb.util.Utils;
import org.kylin.modules.web.struts2.Struts2Utils;

import com.opensymphony.xwork2.ActionSupport;

@Namespace("/linkload")
@ParentPackage("struts-default")
@Results(@org.apache.struts2.convention.annotation.Result(name = "reload", location = "dns-policy!list.action", type = "redirect"))
public class DnsPolicyConfigAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DnsPolicyConfigService dpcs = new DnsPolicyConfigService();
	private String id;
	private DnsPolicyConfig dpc;
	
	public String list() throws Exception {
		HttpServletRequest request = Struts2Utils.getRequest();
		List<Display> stateList = dpcs.getStateList();
		request.setAttribute("stateList", stateList);
		List<Display> ispNameList = dpcs.getIspNameList();
		request.setAttribute("ispNameList", ispNameList);
		//ric.setSale(sale);
		if(StringUtils.equals(id, "add")){
			dpc = new DnsPolicyConfig();
			dpc.setAllAliasList(dpcs.getAllAliasList());
			dpc.setId(id);
		}
		else{
			dpc = dpcs.getDnsPolicyConfigById(id);
			dpc.setAllAliasList(dpcs.getOtherAliasListById(id));
			dpc.setId(id);
		}		
		return "success";
	}
	public String save() throws Exception {		
		//System.out.println(dpc.getId());
		String dnsPolicyId = dpc.getId();
		if(StringUtils.equals(dnsPolicyId, "add")) {			
			String failedMess = null;
			String dnsPolicyMess = dpcs.addDnsPolicyConfig(dpc);
			if ( dnsPolicyMess.equals("true") ) {			
				failedMess = "";
			} else if ( dnsPolicyMess.equals("false") ) {				
				failedMess = "DNS策略添加失败";
			} else {
				failedMess = "您添加的DNS策略配置错误：" + dnsPolicyMess;
			}			
			Utils.getInstance().setFailedMess(failedMess);			
		} else {
			String failedMess = null;
			String dnsPolicyMess =  dpcs.editDnsPolicyConfig(dpc);
			if ( dnsPolicyMess.equals("true") ) {
				failedMess = "";
			} else if ( dnsPolicyMess.equals("false") ) {				
				failedMess = "DNS策略" + dnsPolicyId + "配置失败";
			} else {
				failedMess = "DNS策略" + dnsPolicyId + "配置错误：" + dnsPolicyMess;
			}			
			Utils.getInstance().setFailedMess(failedMess);
		}
		return "reload";
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public DnsPolicyConfig getDpc() {
		return dpc;
	}
	public void setDpc(DnsPolicyConfig dpc) {
		this.dpc = dpc;
	}
		
}
