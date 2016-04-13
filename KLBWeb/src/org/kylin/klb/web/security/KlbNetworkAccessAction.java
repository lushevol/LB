package org.kylin.klb.web.security;

import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Results;
import org.kylin.klb.entity.security.AccessControl;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.service.KlbNetworkAccessService;
import org.kylin.klb.web.CrudActionSupport;
import org.kylin.modules.web.struts2.Struts2Utils;

@Namespace("/")
@ParentPackage("struts-default")
@Results( { @org.apache.struts2.convention.annotation.Result(name = "reload", location = "klb-network-access.action", type = "redirect") })
public class KlbNetworkAccessAction extends CrudActionSupport<AccessControl> {
	private static final long serialVersionUID = 1L;
	//@Autowired
	private KlbNetworkAccessService nas = new KlbNetworkAccessService();	
	private AccessControl entity;
	private String id;
	private List<AccessControl> list = Collections.EMPTY_LIST;

	protected void prepareModel() throws Exception {
		this.entity = new AccessControl();
	}

	public AccessControl getModel() {
		return this.entity;
	}

	public String save() throws Exception {
		HttpServletRequest request = Struts2Utils.getRequest();
		String update = request.getParameter("update");		
		
		String ruleId = this.entity.getId();		
		//if (StringUtils.isNotEmpty(ip)) {
		//if (StringUtils.equalsIgnoreCase(oldIp, ip)) {
		if (StringUtils.equalsIgnoreCase(update, "0")) {						
			String accessMess =  this.nas.addAccessControl(this.entity);
			if ( accessMess.equals("true") ) {
				Struts2Utils.renderJson("{auth:true,\"mess\":\"数据保存成功\"}", new String[0]);
			} else if ( accessMess.equals("false") ) {
				Struts2Utils.renderJson("{auth:false,\"mess\":\"数据保存失败\"}", new String[0]);
			} else {
				Struts2Utils.renderJson("{auth:false,\"mess\":\"" + accessMess + "\"}", new String[0]);
			}
		} else if (StringUtils.equalsIgnoreCase(update, "1")) {			
			String accessMess = this.nas.setAccessControl(ruleId, this.entity);
			if ( accessMess.equals("true") ) {
				Struts2Utils.renderJson("{auth:true,\"mess\":\"数据更新成功\"}", new String[0]);
			} else if ( accessMess.equals("false") ) {
				Struts2Utils.renderJson("{auth:false,\"mess\":\"数据更新失败\"}", new String[0]);
			} else {
				Struts2Utils.renderJson("{auth:false,\"mess\":\"" + accessMess + "\"}", new String[0]);
			}
		}
		/* } else {
				AccessControl ac = this.klbManager.getAccessControlByIp(ip);
				if (ac != null) {
					Struts2Utils.renderJson("{auth:false,\"mess\":\"IP地址 已经存在\"}", new String[0]);
				} else if (StringUtils.equalsIgnoreCase(update, "0")) {
					this.klbManager.setAccessControl(this.entity);
					Struts2Utils.renderJson("{auth:true,\"mess\":\"数据保存成功\"}", new String[0]);
				} else if (StringUtils.equalsIgnoreCase(update, "1")) {
					this.klbManager.updateAccessControl(this.entity);
					Struts2Utils.renderJson("{auth:true,\"mess\":\"数据更新成功\"}", new String[0]);
				}
			}
		} else {
			Struts2Utils.renderJson("{auth:false,\"mess\":\"IP地址 不能为空\"}",
					new String[0]);
		} */
		return null;
	}

	public String delete() throws Exception {
		HttpServletRequest request = Struts2Utils.getRequest();
		String id = request.getParameter("id");
		if (StringUtils.isNotEmpty(id)) {
			boolean flag = false;
			flag = this.nas.delAccessControlById(id);
			if (flag)
				Struts2Utils.renderJson("{auth:true}", new String[0]);
			else
				Struts2Utils.renderJson("{auth:false}", new String[0]);
		} else {
			Struts2Utils.renderJson("{auth:false}", new String[0]);
		}
		return null;
	}

	public String input() throws Exception {
		HttpServletRequest request = Struts2Utils.getRequest();
		String id = request.getParameter("id");
		List<Display> protocolList = this.nas.getProtocolList();
		request.setAttribute("protocolList", protocolList);

		if (StringUtils.isNotEmpty(id)) {
			AccessControl ac = this.nas.getAccessControlById(id);
			Struts2Utils.renderJson("{\"auth\":true,\"obj\":" + JSONObject.fromObject(ac).toString() + "}", new String[0]);
		} else {
			Struts2Utils.renderJson("{\"auth\":false}", new String[0]);
		}
		return null;
	}

	public String list() throws Exception {
		HttpServletRequest request = Struts2Utils.getRequest();		
		List<Display> protocolList = this.nas.getProtocolList();
		request.setAttribute("protocolList", protocolList);
		
		this.list = this.nas.getAccessControlList();
		return "success";
	}

	public List<AccessControl> getList() {
		return this.list;
	}

	public void setList(List<AccessControl> list) {
		this.list = list;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
