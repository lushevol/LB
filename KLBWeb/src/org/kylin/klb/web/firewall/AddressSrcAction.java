package org.kylin.klb.web.firewall;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Results;
import org.kylin.klb.entity.firewall.Nat;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.service.firewall.NatService;
import org.kylin.klb.util.Utils;
import org.kylin.modules.web.struts2.Struts2Utils;

import com.opensymphony.xwork2.ActionSupport;

@Namespace("/firewall")
@ParentPackage("struts-default")
@Results(@org.apache.struts2.convention.annotation.Result(name = "reload", location = "address!mess.action", type = "redirect"))
public class AddressSrcAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	
	private NatService ns = new NatService();
	private Nat nat;
	private String id;
	private String operation;
	
	public String list() throws Exception {
		HttpServletRequest request = Struts2Utils.getRequest();
		List<Display> interfaceList = ns.getInterfaceList();
		request.setAttribute("interfaceList", interfaceList);
		List<Display> protocolList = ns.getProtocolList();
		request.setAttribute("protocolList", protocolList);
		if(StringUtils.equals(operation, "add")||StringUtils.equals(operation, "insert")){
			nat = new Nat();
		}
		else{
			nat = ns.getNatById(id);
		}
		return "success";
	}
	
	/* public String input() throws Exception {
		HttpServletRequest request = Struts2Utils.getRequest();

		List<Display> interfaceList = ns.getInterfaceList();
		request.setAttribute("interfaceList", interfaceList);
		return "success";
	} */
	
	public String save() throws Exception {
		if(StringUtils.equals(nat.getType(), "MASQ")){
			nat.setStartIP("");
			nat.setEndIP("");
		}
		if(StringUtils.equals(operation, "add")){									
			String failedMess = null;			
			String addressSrcMess = ns.addNat(nat);
			if ( addressSrcMess.equals("true") ) {
				failedMess = "";
			} else if ( addressSrcMess.equals("false") ) {				
				failedMess = "源地址转换添加失败";
			} else {
				failedMess = "您添加的源地址转换配置错误：" + addressSrcMess;
			}
			Utils.getInstance().setFailedMess(failedMess);
		}
		if(StringUtils.equals(operation, "edit")){						
			String failedMess = null;
			String addressSrcMess = ns.editNat(id, nat);
			if ( addressSrcMess.equals("true") ) {
				failedMess = "";
			} else if ( addressSrcMess.equals("false") ) {				
				failedMess = "源地址转换" + id + "配置失败";
			} else {
				failedMess = "源地址转换" + id + "配置错误：" + addressSrcMess;
			}
			Utils.getInstance().setFailedMess(failedMess);
		}
		if(StringUtils.equals(operation, "insert")){									
			String failedMess = null;
			String addressSrcMess = ns.insertNat(id, nat);
			if ( addressSrcMess.equals("true") ) {
				failedMess = "";
			} else if ( addressSrcMess.equals("false") ) {				
				failedMess = "源地址转换" + id + "插入失败";
			} else {
				failedMess = "您插入的源地址转换" + id + "配置错误：" + addressSrcMess;
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
	public Nat getNat() {
		return nat;
	}
	public void setNat(Nat nat) {
		this.nat = nat;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
}
