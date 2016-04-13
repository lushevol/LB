package org.kylin.klb.web.security;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.List;
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
@Results( { @org.apache.struts2.convention.annotation.Result(name = "reload", location = "klb-statistics-server.action", type = "redirect") })
public class KlbStatisticsServerAction extends CrudActionSupport<Statistics> {
	private static final long serialVersionUID = 1L;
	private String SERVER_DATA_KEY = "SERVER_DATA_KEY";
	private String filename;
	private InputStream downStream;
	private Statistics entity;
	HttpServletRequest request = ServletActionContext.getRequest();
	HttpSession session = Struts2Utils.getSession();

	@Autowired
	private KlbManager klbManager;

	public String execute() throws Exception {
		this.session.removeAttribute(this.SERVER_DATA_KEY);

		List serverce = this.klbManager.getTrueServIps();
		this.request.setAttribute("serverce", serverce);

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
		this.session.removeAttribute(this.SERVER_DATA_KEY);
		Struts2Utils.renderJson("{auth:true}", new String[0]);
		return null;
	}

	protected void prepareModel() throws Exception {
	}

	public String save() throws Exception {
		return null;
	}

	@Action(value = "/klb-st-server", results = { @org.apache.struts2.convention.annotation.Result(name = "success", type = "stream", params = {
			"contentType", "application/vnd.ms-excel", "inputName", "downStream", "contentDisposition",
			"attachment;filename=${filename}", "bufferSize", "4096" })})
	public String exserver() throws Exception {
		this.filename = "真实服务器转发统计";
		LinkedHashMap datas = (LinkedHashMap) this.session.getAttribute(this.SERVER_DATA_KEY);
		try{
		CreateExcel excel = new CreateExcel();
		this.downStream = excel.getExcelInputStream("真实服务器转发统计", datas);
		}catch (Exception e) {
			//e.printStackTrace();
			return "reload";
		}

		return "success";
	}

	public String getFilename() {
		try {
			this.filename = new String(this.filename.getBytes(), "ISO8859-1");
			//this.filename = new String(this.filename.getBytes(), "GB2312");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return this.filename + ".xls";
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public InputStream getDownStream() {
		return this.downStream;
	}

	public void setDownStream(InputStream downStream) {
		this.downStream = downStream;
	}
}
