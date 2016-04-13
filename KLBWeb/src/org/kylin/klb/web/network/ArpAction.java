package org.kylin.klb.web.network;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.kylin.klb.entity.network.Arp;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.service.network.ArpService;
import org.kylin.klb.util.Utils;
import org.kylin.modules.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionSupport;

@Namespace("/network")
@ParentPackage("struts-default")
public class ArpAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	
	private ArpService as = new ArpService();
	private String id;
	private String ip;
	private String inter;
	private String operation;
	private List<Arp> arpInfoList;
	private Arp arp;
	public String list() throws Exception {
		HttpServletRequest request = Struts2Utils.getRequest();
		List<Display> interfaceList = as.getInterfaceList();
		request.setAttribute("interfaceList", interfaceList);
		arpInfoList = as.getArpInfoList();
		return "success";
	}
	public String save() throws Exception {
		if(StringUtils.equals(operation, "add")){			
			String arpMess =  as.addArp(arp);
			if ( arpMess.equals("true") ) {
				Struts2Utils.renderJson("{auth:true,\"mess\":\"数据保存成功\"}", new String[0]);
			} else if ( arpMess.equals("false") ) {
				Struts2Utils.renderJson("{auth:false,\"mess\":\"数据保存失败\"}", new String[0]);
			} else {
				Struts2Utils.renderJson("{auth:false,\"mess\":\"" + arpMess + "\"}", new String[0]);
			}
		}
		if(StringUtils.equals(operation, "edit")){			
			String arpMess =  as.editArp(id, arp);
			if ( arpMess.equals("true") ) {
				Struts2Utils.renderJson("{auth:true,\"mess\":\"数据更新成功\"}", new String[0]);
			} else if ( arpMess.equals("false") ) {
				Struts2Utils.renderJson("{auth:false,\"mess\":\"数据更新失败\"}", new String[0]);
			} else {
				Struts2Utils.renderJson("{auth:false,\"mess\":\"" + arpMess + "\"}", new String[0]);
			}
		}
		return null;
	}
	public String del() throws Exception {		
		Utils.getInstance().setFailedMess("");		
		if ( as.delArp(ip, inter) ) {
			Struts2Utils.renderJson("{auth:true}", new String[0]);
		} else {
			Struts2Utils.renderJson("{auth:false}", new String[0]);
		}		
		return null;
	}
	public List<Arp> getArpInfoList() {
		return arpInfoList;
	}
	public void setArpInfoList(List<Arp> arpInfoList) {
		this.arpInfoList = arpInfoList;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Arp getArp() {
		return arp;
	}
	public void setArp(Arp arp) {
		this.arp = arp;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getInter() {
		return inter;
	}
	public void setInter(String inter) {
		this.inter = inter;
	}
}
