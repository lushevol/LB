package org.kylin.klb.web.linkload;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Results;
import org.kylin.klb.entity.linkload.DnsServer;
import org.kylin.klb.entity.security.Display;

import org.kylin.klb.service.linkload.DnsServerService;
import org.kylin.modules.web.struts2.Struts2Utils;
import com.opensymphony.xwork2.ActionSupport;


@Namespace("/linkload")
@Results( {@org.apache.struts2.convention.annotation.Result(name = "list", location = "dns-server!input.action", type = "redirect") })
public class DnsServerAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DnsServerService dss = new DnsServerService();	
	private DnsServer ds;
	
	public String list() throws Exception {
		HttpServletRequest request = Struts2Utils.getRequest();
		List<Display> stateList = dss.getStateList();
		request.setAttribute("stateList", stateList);
		List<Display> reverseList = dss.getReverseList();
		request.setAttribute("reverseList", reverseList);
		
		/* ds = dss.getDnsServer();
		if (ds == null)
			ds = new DnsServer(); */
		
		return "success";
	}
	
	public String input() throws Exception {
		HttpServletRequest request = Struts2Utils.getRequest();
		List<Display> stateList = dss.getStateList();
		request.setAttribute("stateList", stateList);
		List<Display> reverseList = dss.getReverseList();
		request.setAttribute("reverseList", reverseList);
		ds = dss.getDnsServer();
		if (ds == null)
			ds = new DnsServer();
		
		return "success";
	}
	
	public String save() throws Exception {								
		String dnsServerMess = dss.setDnsServer(ds);
		if ( dnsServerMess.equals("true") ) {
			addActionMessage("服务器设置成功");
		} else if ( dnsServerMess.equals("false") ) {
			addActionMessage("服务器设置失败");
		} else {
			addActionMessage( dnsServerMess );
		}
		return "list";
	}
	
	public DnsServerService getDss() {
		return dss;
	}

	public void setDss(DnsServerService dss) {
		this.dss = dss;
	}

	public DnsServer getDs() {
		return ds;
	}

	public void setDs(DnsServer ds) {
		this.ds = ds;
	}
		
}
