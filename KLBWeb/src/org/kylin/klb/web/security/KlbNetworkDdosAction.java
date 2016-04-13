package org.kylin.klb.web.security;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Results;
import org.kylin.klb.entity.security.DDos;
import org.kylin.klb.service.KlbManager;
import org.kylin.klb.web.CrudActionSupport;
import org.springframework.beans.factory.annotation.Autowired;

@Namespace("/")
@Results( { @org.apache.struts2.convention.annotation.Result(name = "reload", location = "klb-network-ddos!input.action", type = "redirect") })
public class KlbNetworkDdosAction extends CrudActionSupport<DDos> {
	private static final long serialVersionUID = 1L;
	private DDos entity;

	@Autowired
	private KlbManager klbManager;

	public DDos getModel() {
		return this.entity;
	}

	protected void prepareModel() throws Exception {
		this.entity = this.klbManager.getDDOS();
		if (this.entity == null)
			this.entity = new DDos();
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
		String ddosMess =  this.klbManager.setDDOS( this.entity.getDdosset() );
		if ( ddosMess.equals("true") ) {
			addActionMessage("DDOS攻击防护状态设置成功");
		} else if ( ddosMess.equals("false") ) {
			addActionMessage("DDOS攻击防护状态设置失败");
		} else {
			addActionMessage( ddosMess );
		}
		return "reload";
	}

	public DDos getEntity() {
		return this.entity;
	}

	public void setEntity(DDos entity) {
		this.entity = entity;
	}
}
