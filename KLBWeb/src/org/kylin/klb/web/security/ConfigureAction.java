package org.kylin.klb.web.security;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.kylin.klb.service.KlbManager;
import org.kylin.modules.web.struts2.Struts2Utils;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

@Namespace("/")
public class ConfigureAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private File[] upload;
	// private File uploadFile;
	private String[] uploadContentType;
	private String[] uploadFileName;
	private String[] fileName;
	String[] type = { "text/xml" };

	@Autowired
	private KlbManager klbmanager;
	private String jsCallBack;

	public String getJsCallBack() {
		return this.jsCallBack;
	}

	public void setJsCallBack(String jsCallBack) {
		this.jsCallBack = jsCallBack;
	}

	public String[] getUploadContentType() {
		return this.uploadContentType;
	}

	public void setUploadContentType(String[] uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public File[] getUpload() {
		return this.upload;
	}

	public void setUpload(File[] upload) {
		this.upload = upload;
	}

	public String[] getUploadFileName() {
		return this.uploadFileName;
	}

	public void setUploadFileName(String[] uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	private boolean filetype(String current) {
		for (String now : this.type) {
			if (now.equals(current)) {
				return true;
			}
		}
		return false;
	}

	private String judge() {
		String msg = "";
		try {
			for (int i = 0; i < this.upload.length; ++i) {
				if (!filetype(this.uploadContentType[i])) {
					String types = null;
					for (String t : this.type) {
						if (types != null)
							types = types + "," + t;
						else
							types = t;
					}
					msg = this.uploadFileName[i] + " 上传文件的类型错误，只能上传 " + types
							+ " 类型的文件！";
				}
				InputStream in = new FileInputStream(this.upload[i]);
				int size = in.available();

				if (size > 1048576) {
					msg = this.uploadFileName[i]
							+ "上传文件的大小错误， 单个文件大小小于1M！</li>";
				}
				in.close();
			}
		} catch (Exception e) {
			msg = "上传出现异常";
			e.printStackTrace();
		}
		return msg;
	}

	@Action(value = "/import", results = { @org.apache.struts2.convention.annotation.Result(name = "success", location = "klb-sys-files.action", type = "redirect") })
	public String importFile() throws Exception {
		//
		String result = judge();
		if (StringUtils.isEmpty(result)) {
			String[] target = new String[this.upload.length];
			//
			for (int i = 0; i < this.upload.length; ++i) {
				/*
				 * if (!this.klbmanager .saveConfig("configure.xml",
				 * this.upload[i])) { result = "上传配置文件验证有误,请检查配置文件"; break; }
				 */
				
				String importMess =  this.klbmanager.configImport(this.upload[i]);				
				if ( importMess.equals("true") ) {
					result = "";
				} else if ( importMess.equals("false") ) {
					result = "上传配置文件验证有误,请检查配置文件";
					break;
				} else {
					result = importMess;
					break;
				}
				//return "reload";				
			}
		}
		/*
		 * File configFile = upload[0]; FileReader fr = new
		 * FileReader(configFile); BufferedReader br = new BufferedReader(fr);
		 * StringBuffer sb = new StringBuffer(); String temp = br.readLine();
		 * while(temp != null){ sb.append(temp); temp = br.readLine(); }
		 */
		// String fileContent = sb.toString();
		//
		StringBuffer script = new StringBuffer();
		script.append("<script type=\"text/javascript\">");
		String auth = "false";
		if (StringUtils.isEmpty(result)) {
			auth = "true";
			result = "文件上传成功";
		} else {
			auth = "false";
		}
		String json = "{\"auth\":\"" + auth + "\",\"msg\":\"" + result + "\",\"filePath\":\"filePath\"}";
		if (StringUtils.isEmpty(this.jsCallBack))
			script.append("parent.uploadCbk(" + json + ");");
		else {
			script.append("parent." + this.jsCallBack + "(" + json + ");");
		}
		script.append("</script>");
		Struts2Utils.renderHtml(script.toString(), new String[0]);
		return null;
	}

	@Action("/configure")
	public void configure() throws Exception {
		//		
		String configSaveMess =  this.klbmanager.configureCommit();
		if ( configSaveMess.equals("true") ) {
			Struts2Utils.renderJson("{\"auth\":\"true\",\"msg\":\"配置保存生效!\"}", new String[0]);
		} else if ( configSaveMess.equals("false") ) {
			Struts2Utils.renderJson("{\"auth\":\"false\",\"msg\":\"配置保存未能生效,请检查配置文件内容!\"}", new String[0]);
		} else {
			Struts2Utils.renderJson("{auth:false,\"mess\":\"" + configSaveMess + "\"}", new String[0]);
		}						
	}
	public void reload() throws Exception {
		//System.out.println("调用reload函数");		
		String configReloadMess =  this.klbmanager.reloadCommit();
		//System.out.println(configReloadMess);
		if ( configReloadMess.equals("true") ) {
			Struts2Utils.renderJson("{\"auth\":\"true\",\"msg\":\"配置重载生效!\"}", new String[0]);
		} else if ( configReloadMess.equals("false") ) {
			Struts2Utils.renderJson("{\"auth\":\"false\",\"msg\":\"配置重载未能生效,请检查配置文件内容!\"}", new String[0]);
		} else {
			Struts2Utils.renderJson("{auth:false,\"mess\":\"" + configReloadMess + "\"}", new String[0]);
		}			
	}

	@Action(value = "/export", results = { @org.apache.struts2.convention.annotation.Result(name = "success", type = "stream", params = {
			"contentType", "text/xml", "inputName", "inputStream", "contentDisposition", "attachment;filename=config.xml",
			"bufferSize", "4096" }) })
	public String export() {
		//
		return "success";
	}

	public InputStream getInputStream() throws Exception {
		String doc = this.klbmanager.configExport();
		if (doc != null) {
			return new ByteArrayInputStream(doc.getBytes());
		}
		return new ByteArrayInputStream("".getBytes());
	}

	public String[] getFileName() {
		return this.fileName;
	}

	public void setFileName(String[] fileName) {
		this.fileName = fileName;
	}

	/*
	 * public File getUploadFile() { return uploadFile; }
	 * 
	 * public void setUploadFile(File uploadFile) { this.uploadFile =
	 * uploadFile; }
	 */
}
