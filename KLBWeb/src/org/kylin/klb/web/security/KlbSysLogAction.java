package org.kylin.klb.web.security;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Results;
import org.kylin.klb.entity.security.Syslog;
import org.kylin.klb.service.SyslogService;
import org.kylin.klb.web.CrudActionSupport;

@Namespace("/")
@Results( { @org.apache.struts2.convention.annotation.Result(name = "reload", location = "klb-sys-log!input.action", type = "redirect") })
public class KlbSysLogAction extends CrudActionSupport<Syslog> {
	private static final long serialVersionUID = 1L;
	private Syslog entity;
	private SyslogService sls = new SyslogService();

	public Syslog getModel() {
		return this.entity;
	}
   
	protected void prepareModel() throws Exception {
		this.entity = this.sls.getSyslog();
		if (this.entity == null)
			this.entity = new Syslog();
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
		String syslogMess =  this.sls.setSyslog(this.entity);
		if ( syslogMess.equals("true") ) {
			addActionMessage("远程日志服务器设置成功");
		} else if ( syslogMess.equals("false") ) {
			addActionMessage("远程日志服务器设置失败");
		} else {
			addActionMessage( syslogMess );
		}
		return "reload";
	}

	public Syslog getEntity() {
		return this.entity;
	}

	public void setEntity(Syslog entity) {
		this.entity = entity;
	}
}
