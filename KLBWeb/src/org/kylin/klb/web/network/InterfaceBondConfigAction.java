package org.kylin.klb.web.network;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Results;
import org.kylin.klb.entity.network.BondConfig;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.service.network.InterfaceBondConfigService;
import org.kylin.modules.web.struts2.Struts2Utils;

import com.opensymphony.xwork2.ActionSupport;

@Namespace("/network")
//@ParentPackage("struts-default")
@Results( { @org.apache.struts2.convention.annotation.Result(name = "reload", location = "interface-bond!list.action", type = "redirect"),
	@org.apache.struts2.convention.annotation.Result(name = "list", location = "interface-bond-config!list.action", type = "redirect") })
public class InterfaceBondConfigAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	
	private InterfaceBondConfigService ibcs = new InterfaceBondConfigService();
	private static String name;
	private BondConfig bc;
	private String interForBond;
	
	public String init() throws Exception {
		if(StringUtils.equals(name, "add")){
			ibcs.initBondConfigByName();
		}
		else{
			ibcs.initBondConfigByName(name);
		}
		return "list";
	}
	public String list() throws Exception {
		HttpServletRequest request = Struts2Utils.getRequest();
		/* List<Display> bondNameList = ibcs.getBondNameList();
		request.setAttribute("bondNameList", bondNameList);
		List<Display> interfaceList = ibcs.getInterfaceList();
		request.setAttribute("interfaceList", interfaceList); */
		List<Display> algorithmList = ibcs.getAlgorithmList();
		request.setAttribute("algorithmList", algorithmList);
		interForBond = ibcs.getInterForBond();
		bc = ibcs.getBondConfig();
		//System.out.println(bc.getIpList());
		return "success";
	}
	public String save() throws Exception {
		/* if(StringUtils.equals(name, "add")){
			ibcs.addBond(bc);
		}
		else{
			ibcs.editBondConfig(name, bc);
		}
		return "reload"; */
		String bondConfigMess =  ibcs.editBondConfig(name, bc);		
		if ( bondConfigMess.equals("true") ) {			
			return "reload";
		} else if ( bondConfigMess.equals("false") ) {
			addActionMessage("链路聚合配置失败");
			return "list";
		} else {
			addActionMessage( bondConfigMess );
			return "list";
		}
	}
	
	public BondConfig getBc() {
		return bc;
	}
	public void setBc(BondConfig bc) {
		this.bc = bc;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInterForBond() {
		return interForBond;
	}
	public void setInterForBond(String interForBond) {
		this.interForBond = interForBond;
	}
}
