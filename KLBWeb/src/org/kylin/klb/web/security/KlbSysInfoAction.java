package org.kylin.klb.web.security;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.kylin.klb.entity.security.Director;
import org.kylin.klb.entity.security.SystemHard;
import org.kylin.klb.service.KlbSysInfoImpl;
import org.kylin.klb.web.CrudActionSupport;
import org.kylin.modules.utils.StringUtils;
import org.kylin.modules.web.struts2.Struts2Utils;
import org.springframework.beans.factory.annotation.Autowired;

@Namespace("/")
@ParentPackage("struts-default")
public class KlbSysInfoAction extends CrudActionSupport<SystemHard> {
	private static final long serialVersionUID = 1L;

	@Autowired
	KlbSysInfoImpl klbSysInfoImpl;
	private SystemHard systemHard;
	private List<Director> directors;

	public List<Director> getDirectors() {
		//System.out.println("KlbSysInfoAction.getDirectors()");
		return this.directors;
	}

	public void setDirectors(List<Director> directors) {
		//System.out.println("KlbSysInfoAction.setDirectors()");
		this.directors = directors;
	}

	public SystemHard getModel() {
		//System.out.println("KlbSysInfoAction.getModel()");
		return this.systemHard;
	}

	public void prepareExecute() throws Exception {
		//System.out.println("KlbSysInfoAction.prepareExecute()");
		prepareModel();
	}

	protected void prepareModel() throws Exception {
		//System.out.println("KlbSysInfoAction.prepareModel()");
	
		HttpServletRequest request = Struts2Utils.getRequest();
		String isframe = request.getParameter("isframe");
		//System.out.println("isframe=["+ isframe + "]");
		if ((StringUtils.isEmpty(isframe)) || ("0".equals(isframe))) {
			this.systemHard = this.klbSysInfoImpl.getSysInfo();
			this.directors = this.klbSysInfoImpl.getDirectorInfo();
		} else {
			this.systemHard = this.klbSysInfoImpl.getSysInfo();
		}
	}

	public String execute() throws Exception {
		// klbClient.Test();
		//System.out.println("how to set the param");
		return "success";
	}

	public String list() throws Exception {
		return null;
	}

	public String input() throws Exception {
		return "include";
	}

	public String save() throws Exception {
		return null;
	}

	public String delete() throws Exception {
		return null;
	}
}
