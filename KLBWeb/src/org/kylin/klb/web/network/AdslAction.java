package org.kylin.klb.web.network;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.kylin.klb.entity.network.AdslAttribute;
import org.kylin.klb.entity.network.AdslInfo;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.service.network.AdslService;
import org.kylin.klb.util.Utils;
import org.kylin.modules.web.struts2.Struts2Utils;

import com.opensymphony.xwork2.ActionSupport;

@Namespace("/network")
@ParentPackage("struts-default")
public class AdslAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	
	private AdslService as = new AdslService();
	private List<AdslInfo> aiList;
	private AdslAttribute aa;
	private String operation;
	private String inter;
	private String mode;
	
	public String list() throws Exception {
		HttpServletRequest request = Struts2Utils.getRequest();
		/* List<Display> interfaceList = as.getInterfaceList();
		request.setAttribute("interfaceList", interfaceList); */
		List<Display> ethList = as.getEthList();
		request.setAttribute("ethList", ethList);
		aiList = as.getAdslInfoList();
		return "success";
	}
	public String input() throws Exception {
		aa = as.getAdslAttribute(inter);
		Struts2Utils.renderJson("{\"auth\":true,\"obj\":"
				+ JSONObject.fromObject(aa).toString() + "}", new String[0]);
		return null;
	}
	public String save() throws Exception {
		if (StringUtils.equals(operation, "add")){			
			String adslMess =  as.addAdsl(aa);
			if ( adslMess.equals("true") ) {
				Struts2Utils.renderJson("{auth:true,\"mess\":\"ADSL接口添加成功\"}", new String[0]);
			} else if ( adslMess.equals("false") ) {
				Struts2Utils.renderJson("{auth:false,\"mess\":\"ADSL接口添加失败\"}", new String[0]);
			} else {
				Struts2Utils.renderJson("{auth:false,\"mess\":\"" + adslMess + "\"}", new String[0]);
			}			
		} else {
			String adslMess =  as.editAdslAttribute(aa);
			if ( adslMess.equals("true") ) {
				Struts2Utils.renderJson("{auth:true,\"mess\":\"" + "ADSL接口设置成功" + "\"}", new String[0]);
			} else if ( adslMess.equals("false") ) {
				Struts2Utils.renderJson("{auth:false,\"mess\":\"" + "ADSL接口设置失败" + "\"}", new String[0]);
			} else {
				Struts2Utils.renderJson("{auth:false,\"mess\":\"" + adslMess + "\"}", new String[0]);
			}			
		}
		return null;
	}
	public String del() throws Exception {
		Utils.getInstance().setFailedMess("");		
		if ( as.delAdslInfo(inter) ) {
			Struts2Utils.renderJson("{auth:true}", new String[0]);
		} else {
			Struts2Utils.renderJson("{auth:false}", new String[0]);
		}
		return null;
	}
	
	public String edit() throws Exception {		
		String editMess =  as.editAdslState(inter, mode);
		if ( editMess.equals("true") ) {
			Struts2Utils.renderJson("{auth:true}", new String[0]);
		} else if ( editMess.equals("false") ) {
			Struts2Utils.renderJson("{auth:false,\"mess\":\"\"}", new String[0]);
		} else {
			Struts2Utils.renderJson("{auth:false,\"mess\":\"" + editMess + "\"}", new String[0]);
		}				
		return null;
	}
	public List<AdslInfo> getAiList() {
		return aiList;
	}
	public void setAiList(List<AdslInfo> aiList) {
		this.aiList = aiList;
	}
	public AdslAttribute getAa() {
		return aa;
	}
	public void setAa(AdslAttribute aa) {
		this.aa = aa;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getInter() {
		return inter;
	}
	public void setInter(String inter) {
		this.inter = inter;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
}
