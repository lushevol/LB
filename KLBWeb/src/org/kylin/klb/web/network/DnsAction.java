package org.kylin.klb.web.network;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Results;
import org.kylin.klb.entity.network.Dns;
import org.kylin.klb.service.network.DnsService;
import org.kylin.klb.web.CrudActionSupport;

@Namespace("/network")
@Results(@org.apache.struts2.convention.annotation.Result(name = "reload", location = "dns!input.action", type = "redirect"))

public class DnsAction extends CrudActionSupport<Dns> {
	private static final long serialVersionUID = 1L;
	private Dns entity;
	private DnsService ds = new DnsService();
	
	public Dns getModel() {
		return this.entity;
	}

	protected void prepareModel() throws Exception {
		this.entity = this.ds.getdns();
		if (this.entity == null)
			this.entity = new Dns();
	}

	public String delete() throws Exception {
		return null;
	}

	public String input() throws Exception {
		return "success";
	}

	public String list() throws Exception {
		return "success";
	}

	public String save() throws Exception {		
		String dnsMess =  this.ds.setdns(this.entity.getFirstadd(), this.entity.getSecondadd());		
		if ( dnsMess.equals("true") ) {
			addActionMessage("域名服务器设置成功");
		} else if ( dnsMess.equals("false") ) {
			addActionMessage("域名服务器设置失败");
		} else {
			addActionMessage(dnsMess);
		}
		return "reload";
	}

	public Dns getEntity() {
		return this.entity;
	}

	public void setEntity(Dns entity) {
		this.entity = entity;
	}
}
