package org.kylin.klb.web.security;

import java.util.Collections;
import java.util.List;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Results;
import org.kylin.klb.entity.security.Pinggroups;
import org.kylin.klb.service.KlbManager;
import org.kylin.klb.web.CrudActionSupport;
import org.springframework.beans.factory.annotation.Autowired;

@Namespace("/")
@Results( { @org.apache.struts2.convention.annotation.Result(name = "reload", location = "klb-load-pinggroups.action", type = "redirect") })
public class KlbLoadPinggroupsAction extends CrudActionSupport<Pinggroups> {
	private static final long serialVersionUID = 1L;
	private String firstadd;
	private String secondadd;
	private Pinggroups entity;
	private Pinggroups entity1;
	private List<Pinggroups> list = Collections.EMPTY_LIST;
	private List<Pinggroups> list1 = Collections.EMPTY_LIST;
	@Autowired
	private KlbManager klbManager;

	public Pinggroups getModel() {
		return this.entity;
	}

	protected void prepareModel() throws Exception {
		this.entity = new Pinggroups();
	}

	public String delete() throws Exception {
		if (this.klbManager.removefirstadd(this.firstadd))
			addActionMessage("域名地址删除成功");
		else
			addActionMessage("域名地址删除失败");
		return "reload";
	}
	
	/* public String deletesecond() throws Exception {	
		if (this.klbManager.removesecondadd(this.secondadd))
		{
			addActionMessage("域名地址删除成功");
		}
		else
			addActionMessage("域名地址删除失败");
		return "reload";
	} */

	public String input() throws Exception {
		return "success";
	}

	public String list() throws Exception {
		this.list = this.klbManager.loadPinggroupsGetPinggroups();
		//this.list1 = this.klbManager.loadPinggroupsGetPinggroups1();
		return "success";
	}
	
	public String save() throws Exception {
		String firstadd = this.entity.getFirstadd();		
		String pinggroupMess =  this.klbManager.setfirstadd(firstadd);
		if ( pinggroupMess.equals("true") ) {
			addActionMessage("域名地址添加成功");
		} else if ( pinggroupMess.equals("false") ) {
			addActionMessage("域名地址添加失败");
		} else {
			addActionMessage( pinggroupMess );
		}		
		return "reload";
	}
	
	/* public String savesecondadd() throws Exception {
		this.klbManager.setsecondadd(secondadd);
		addActionMessage("域名地址添加成功");
		return "reload";
	} */
		
	public Pinggroups getEntity() {
		return this.entity;
	}

	public void setEntity(Pinggroups entity) {
		this.entity = entity;
	}

	public List<Pinggroups> getList() {
		return list;
	}

	public void setList(List<Pinggroups> list) {
		this.list = list;
	}

	public String getFirstadd() {
		return firstadd;
	}

	public void setFirstadd(String firstadd) {
		this.firstadd = firstadd;
	}

	public List<Pinggroups> getList1() {
		return list1;
	}

	public void setList1(List<Pinggroups> list1) {
		this.list1 = list1;
	}

	public String getSecondadd() {
		return secondadd;
	}

	public void setSecondadd(String secondadd) {
		this.secondadd = secondadd;
	}

	public Pinggroups getEntity1() {
		return entity1;
	}

	public void setEntity1(Pinggroups entity1) {
		this.entity1 = entity1;
	}
}
