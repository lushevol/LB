package org.kylin.klb.web.security;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Results;
import org.kylin.klb.entity.security.SmtpMail;
import org.kylin.klb.service.SysMailService;
import org.kylin.klb.web.CrudActionSupport;

@Namespace("/")
@Results( { @org.apache.struts2.convention.annotation.Result(name = "reload", location = "klb-sys-mail!input.action", type = "redirect") })
public class KlbSysMailAction extends CrudActionSupport<SmtpMail> {
	private static final long serialVersionUID = 1L;
	private SmtpMail entity;

	private SysMailService service = new SysMailService();
	
	public SmtpMail getModel() {
		return this.entity;
	}
   
	protected void prepareModel() throws Exception {
		this.entity = this.service.getSysMail();
		if (this.entity == null)
			this.entity = new SmtpMail();
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
		String result =  this.service.setSysMail(this.entity);
		if ( result.equals("true") ) {
			addActionMessage("邮件配置设置成功");
		} else if ( result.equals("false") ) {
			addActionMessage("邮件配置设置失败");
		} else {
			addActionMessage( result );
		}
		return "reload";
	}

	public SmtpMail getEntity() {
		return this.entity;
	}

	public void setEntity(SmtpMail entity) {
		this.entity = entity;
	}
}
