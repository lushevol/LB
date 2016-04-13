package org.kylin.klb.web.network;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Results;
import org.kylin.klb.entity.network.BondAttribute;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.service.network.InterfaceBondAttributeService;
import org.kylin.klb.util.Utils;
import org.kylin.modules.web.struts2.Struts2Utils;

import com.opensymphony.xwork2.ActionSupport;

@Namespace("/network")
@Results( { @org.apache.struts2.convention.annotation.Result(name = "reload", location = "interface-bond!mess.action", type = "redirect"),
	@org.apache.struts2.convention.annotation.Result(name = "list", location = "interface-bond-attribute!list.action", type = "redirect") })
public class InterfaceBondAttributeAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private InterfaceBondAttributeService ibas = new InterfaceBondAttributeService();
	private BondAttribute ba;
	private String name;
	
	public String init() throws Exception {
		ibas.initBondAttributeByName(name);
		return "list";
	}
	public String list() throws Exception {
		HttpServletRequest request = Struts2Utils.getRequest();
		List<Display> stateList = ibas.getStateList();
		request.setAttribute("stateList", stateList);
		List<Display> dhcpList = ibas.getDhcpList();
		request.setAttribute("dhcpList", dhcpList);
		List<Display> arpList = ibas.getArpList();
		request.setAttribute("arpList", arpList);
		ba = ibas.getBondAttribute();
		return "success";
	}
	
	public String save() throws Exception {
		String failedMess = null;
		String name = ba.getName();
		String bondAttrMess =  ibas.editBondAttribute(ba);
		if ( bondAttrMess.equals("true") ) {
			failedMess = "";
		} else if ( bondAttrMess.equals("false") ) {
			failedMess = "聚合接口" + name + "修改失败";
		} else {
			failedMess = name + "配置错误：" + bondAttrMess;
		}
		Utils.getInstance().setFailedMess(failedMess);
		return "reload";
	}
	
	public BondAttribute getBa() {
		return ba;
	}
	public void setBa(BondAttribute ba) {
		this.ba = ba;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
