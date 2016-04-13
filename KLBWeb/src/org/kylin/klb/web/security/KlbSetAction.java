package org.kylin.klb.web.security;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.kylin.klb.entity.security.LoadSirtualServ;
import org.kylin.klb.entity.security.LoadTrueServ;
import org.kylin.klb.entity.security.Sets;
import org.kylin.klb.entity.security.User;
import org.kylin.klb.service.KlbManager;
import org.kylin.klb.service.SecurityUtils;
import org.kylin.klb.util.Utils;
import org.kylin.klb.web.CrudActionSupport;
import org.kylin.modules.utils.StringUtils;
import org.kylin.modules.web.struts2.Struts2Utils;
import org.springframework.beans.factory.annotation.Autowired;

@Namespace("/")
public class KlbSetAction extends CrudActionSupport<Sets> {
	private static final long serialVersionUID = 1L;
	HttpServletRequest request = ServletActionContext.getRequest();

	@Autowired
	private KlbManager klbManager;
	private Sets entity = null;

	protected void prepareModel() throws Exception {
		if (this.entity != null) {
			return;
		}
		this.entity = new Sets();
	}

	public void prepareCancel() throws Exception {
		prepareModel();
	}

	public void prepareSet1() throws Exception {
		prepareModel();
	}

	public void prepareSet2() throws Exception {
		prepareModel();
	}

	public void prepareSet2Check() throws Exception {
		prepareModel();
	}

	public void prepareSet3() throws Exception {
		prepareModel();
	}

	@Action("/klb-set-1")
	public String set1() throws Exception {
		User user = SecurityUtils.getSecurityUser();
		if ((StringUtils.isNotEmpty(user.getGuide()))
				&& (!"1".equals(this.entity.getGuide()))) {
			this.entity.setGuide(user.getGuide());
		}
		return "success";
	}

	@Action("/klb-set-2")
	public String set2() throws Exception {
		return "success";
	}

	@Action("/klb-set-2-check")
	public String set2Check() throws Exception {
		String service = this.entity.getService();
		if (StringUtils.isNotEmpty(service)) {
			LoadSirtualServ ls = this.klbManager.getSirtualServByName(service);
			if (ls != null) {
				Struts2Utils.renderJson("{auth:false,\"mess\":\"虚拟服务名称已经存在\"}", new String[0]);
				return null;
			}
		}
		LoadSirtualServ ls1 = this.klbManager.getSirServByIp(this.entity.getVipMark());
		if ((ls1 != null) && (StringUtils.equals(this.entity.getTcpPorts(), ls1.getTcpPorts()))
				&& (StringUtils.equals(this.entity.getUdpPorts(), ls1.getUdpPorts()))) {
			Struts2Utils.renderJson("{auth:false,\"mess\":\"虚拟服务IP不能使用相同的TCP和UDP端口\"}", new String[0]);
			return null;
		}

		Struts2Utils.renderJson("{auth:true,\"mess\":\"\"}", new String[0]);
		return null;
	}

	@Action(value = "/klb-set-3", results = { @org.apache.struts2.convention.annotation.Result(name = "error", location = "/klb-set-2.action", type = "redirect") })
	public String set3() throws Exception {
		if ((StringUtils.isNotEmpty(this.entity.getService()))
				&& (StringUtils.isNotEmpty(this.entity.getVipMark()))
				&& (StringUtils.isNotEmpty(this.entity.getPersistentNetmask()))) {
			HttpServletRequest request = Struts2Utils.getRequest();
			Map forwards = Utils.getInstance().getForwards();
			request.setAttribute("forwards", forwards);
			return "success";
		}
		return "error";
	}

	public Sets getModel() {
		return this.entity;
	}

	@Action(results = { @org.apache.struts2.convention.annotation.Result(name = "reload", location = "/klb-set-3.action", type = "redirect") })
	public String save() throws Exception {
		LoadSirtualServ ser = new LoadSirtualServ();
		ser.setService(this.entity.getService());
		ser.setVipMark(this.entity.getVipMark());
		ser.setTcpPorts(this.entity.getTcpPorts());
		ser.setUdpPorts(this.entity.getUdpPorts());
		ser.setPersistentNetmask(this.entity.getPersistentNetmask());
		ser.setPersistent(this.entity.getPersistent());

		LoadTrueServ tser = new LoadTrueServ();
		tser.setServiceName(this.entity.getServiceName());
		tser.setIp(this.entity.getIp());
		tser.setForward(this.entity.getForward());

		boolean flag = this.klbManager.serviceServerSet(ser, tser);

		if (flag) {
			User user = SecurityUtils.getSecurityUser();
			user.setGuide(this.entity.getGuide());
			this.klbManager.updateUserInfo(user.getId(), user);
			SecurityUtils.updateSecurityUser(user);
			Struts2Utils.renderJson("{auth:true}", new String[0]);
		} else {
			Struts2Utils.renderJson("{auth:false}", new String[0]);
		}
		return null;
	}

	@Action(value = "/klb-set-cancel", results = { @org.apache.struts2.convention.annotation.Result(name = "success", location = "klb-index.action", type = "redirect") })
	public String cancel() throws Exception {
		User user = SecurityUtils.getSecurityUser();
		user.setGuide(this.entity.getGuide());
		this.klbManager.updateUserInfo(user.getId(), user);
		SecurityUtils.updateSecurityUser(user);
		return "success";
	}

	public String delete() throws Exception {
		return "reload";
	}

	public String input() throws Exception {
		return "input";
	}

	public String list() throws Exception {
		return "success";
	}
}
