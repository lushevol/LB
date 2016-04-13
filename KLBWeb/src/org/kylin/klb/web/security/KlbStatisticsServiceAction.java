package org.kylin.klb.web.security;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Results;
import org.kylin.klb.entity.security.Statistics;
import org.kylin.klb.service.KlbManager;
import org.kylin.klb.service.myexcel.CreateExcel;
import org.kylin.klb.web.CrudActionSupport;
import org.kylin.modules.web.struts2.Struts2Utils;
import org.springframework.beans.factory.annotation.Autowired;

@Namespace("/")
@Results( { @org.apache.struts2.convention.annotation.Result(name = "reload", location = "klb-statistics-service.action", type = "redirect") })
public class KlbStatisticsServiceAction extends CrudActionSupport<Statistics> {
	private static final long serialVersionUID = 1L;
	private String SERVICE_DATA_KEY = "SERVICE_DATA_KEY";
	private String filename;
	private InputStream downStream;
	private Statistics entity;
	HttpServletRequest request = ServletActionContext.getRequest();
	HttpSession session = Struts2Utils.getSession();

	@Autowired
	private KlbManager klbManager;

	public String execute() throws Exception {

		this.session.removeAttribute(this.SERVICE_DATA_KEY);

		List<String> serverce = this.klbManager.getSirtualServNames();
		Map map = new HashMap();
		if ((serverce != null) && (!serverce.isEmpty())) {
			for (String s : serverce) {
				map.put(s, (s.startsWith("_")) ? s.substring(1, s.length()) : s);
			}
		}
		this.request.setAttribute("serverce", map);

		return "success";
	}

	public Statistics getModel() {
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

	public String initServer() throws Exception {
		//
		this.session.removeAttribute(this.SERVICE_DATA_KEY);
		Struts2Utils.renderJson("{auth:true}", new String[0]);
		return null;
	}

	protected void prepareModel() throws Exception {
	}

	public String save() throws Exception {
		return null;
	}

	@Action(value = "/klb-st-service", results = { @org.apache.struts2.convention.annotation.Result(name = "success", type = "stream", params = {
			"contentType", "application/vnd.ms-excel", "inputName", "downStream", "contentDisposition",
			"attachment;filename=${filename}", "bufferSize", "4096" }) })
	public String exservice() throws Exception {
		// 
		this.filename = "服务转发统计";
		LinkedHashMap datas = (LinkedHashMap) this.session.getAttribute(this.SERVICE_DATA_KEY);
		try{
		CreateExcel excel = new CreateExcel();
		this.downStream = excel.getExcelInputStream("服务转发统计", datas);
		}catch (Exception e) {
			return "reload";
		}

		return "success";
	}

	public String getFilename() {
		try {
			this.filename = new String(this.filename.getBytes(), "ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return this.filename + ".xls";
	}

	public InputStream getDownStream() {
		return this.downStream;
	}

	public void setDownStream(InputStream downStream) {
		this.downStream = downStream;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
}
