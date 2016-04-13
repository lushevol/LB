package org.kylin.klb.web.security;

import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Results;
import org.kylin.klb.entity.security.LoadScheduler;
import org.kylin.klb.service.KlbManager;
import org.kylin.klb.web.CrudActionSupport;
import org.kylin.modules.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Namespace("/")
@Results( { @org.apache.struts2.convention.annotation.Result(name = "reload", location = "klb-load-scheduler.action", type = "redirect") })
public class KlbLoadSchedulerAction extends CrudActionSupport<LoadScheduler> {
	private static final long serialVersionUID = 1L;
	HttpServletRequest request = ServletActionContext.getRequest();
	private String schedulerName;

	@Autowired
	private KlbManager klbManager;
	private LoadScheduler loadScheduler;
	LoadScheduler entity = null;

	private List<LoadScheduler> list = Collections.EMPTY_LIST;

	protected void prepareModel() throws Exception {
		this.entity = new LoadScheduler();
	}

	public LoadScheduler getModel() {
		return this.entity;
	}

	public String save() throws Exception {
		List count = this.klbManager.loadSchedulerGetScheduler();

		if (count.size() >= 2) {
			addActionMessage("不能超过两个");
			return "reload";
		}

		String schedulerName = this.entity.getSchedulerName();
		if (schedulerName.equalsIgnoreCase("")) {
			addActionMessage("请输入调度器名称");
		} 
		else if (schedulerName.matches("^[\\w]+[\\.\\w]*$")) {
			this.klbManager.setScheduler(schedulerName);
			addActionMessage("调度器添加成功");
		}
		else {
			addActionMessage("调度器添加失败");
		}
		return "reload";
	}

	public String delete() throws Exception {

		String temp = this.schedulerName;
		temp = new String(temp.getBytes("8859_1"), "UTF-8");
		//
		if (StringUtils.equals(temp, "本机")) {
			addActionMessage("本机不可以删除");
			return "reload";

		} else {

			if (this.klbManager.removeScheduler(this.schedulerName))
				addActionMessage("调度器删除成功");
			else
				addActionMessage("调度器删除失败");

			return "reload";
		}
	}

	public String input() throws Exception {
		return "input";
	}

	public String list() throws Exception {
		this.list = this.klbManager.loadSchedulerGetScheduler();
		return "success";
	}

	public List<LoadScheduler> getList() {
		return this.list;
	}

	public void setList(List<LoadScheduler> list) {
		this.list = list;
	}

	public LoadScheduler getLoadScheduler() {
		return this.loadScheduler;
	}

	public void setLoadScheduler(LoadScheduler loadScheduler) {
		this.loadScheduler = loadScheduler;
	}

	public String getSchedulerName() {
		return this.schedulerName;
	}

	public void setSchedulerName(String schedulerName) {
		this.schedulerName = schedulerName;
	}
}
