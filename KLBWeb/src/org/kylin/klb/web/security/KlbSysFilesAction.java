package org.kylin.klb.web.security;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Results;
import org.kylin.klb.entity.security.KlbSysFiles;
import org.kylin.klb.web.CrudActionSupport;

@Namespace("/")
@Results( { @org.apache.struts2.convention.annotation.Result(name = "reload", location = "klb-sys-files.action", type = "redirect") })
public class KlbSysFilesAction extends CrudActionSupport<KlbSysFiles> {
	private static final long serialVersionUID = 1L;
	private KlbSysFiles entity;

	public String execute() throws Exception {
		return "success";
	}

	public KlbSysFiles getModel() {
		return this.entity;
	}

	public String delete() throws Exception {
		return null;
	}

	public String input() throws Exception {
		return null;
	}

	public String list() throws Exception {
		return null;
	}

	protected void prepareModel() throws Exception {
	}

	public String save() throws Exception {
		return null;
	}
}
