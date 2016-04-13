package org.kylin.klb.web.security;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Results;
import org.kylin.klb.entity.security.Hostname;
import org.kylin.klb.service.KlbManager;
import org.kylin.klb.web.CrudActionSupport;
import org.springframework.beans.factory.annotation.Autowired;

@Namespace("/")
@Results( { @org.apache.struts2.convention.annotation.Result(name = "reload", location = "klb-load-hostname!input.action", type = "redirect") })
public class KlbLoadHostnameAction extends CrudActionSupport<Hostname> {
	private static final long serialVersionUID = 1L;
	private Hostname entity;

	@Autowired
	private KlbManager klbManager;

	public Hostname getModel() {
		return this.entity;
	}

	protected void prepareModel() throws Exception {
		this.entity = this.klbManager.getHostname();
		if (this.entity == null)
			this.entity = new Hostname();
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
		String hostnameMess =  this.klbManager.setHostname(this.entity.getHostname());
		if ( hostnameMess.equals("true") ) {
			addActionMessage("主机名设置成功");
		} else if ( hostnameMess.equals("false") ) {
			addActionMessage("主机名设置失败");
		} else {
			addActionMessage( hostnameMess );
		}
		return "reload";
	}

	public Hostname getEntity() {
		return this.entity;
	}

	public void setEntity(Hostname entity) {
		this.entity = entity;
	}
}