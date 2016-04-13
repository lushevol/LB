package org.kylin.klb.web.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.kylin.klb.service.UpgradeService;
import org.kylin.modules.web.struts2.Struts2Utils;

import com.opensymphony.xwork2.ActionSupport;

@Namespace("/")
@ParentPackage("struts-default")
public class KlbSysUpgradeAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	
	private UpgradeService us = new UpgradeService();
	private File[] upload;
	private String[] uploadContentType;
	private String[] uploadFileName;
	private String[] fileName;
	String[] type = { "upgrade" };

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

	@Action(value = "/upgrade", results = { @org.apache.struts2.convention.annotation.Result(name = "success", location = "klb-sys-upgrade.action", type = "redirect") })
	public String upgrade() throws Exception {
		String result = judge();
		if (StringUtils.isEmpty(result)) {
			String[] target = new String[this.upload.length];
			//
			for (int i = 0; i < this.upload.length; ++i) {				
				/*
				 * if (!this.klbmanager .saveConfig("configure.xml",
				 * this.upload[i])) { result = "上传配置文件验证有误,请检查配置文件"; break; }
				 */
				String upgradeMess = this.us.upgrade(this.upload[i]);
				/* if (!StringUtils.equals(temp, "success")) {
					result = temp;
					break;
				} */
				if ( upgradeMess.equals("true") ) {
					result = "";
				} else if ( upgradeMess.equals("false") ) {
					result = "系统升级失败,请检查升级文件";
					break;
				} else {
					result = upgradeMess;
					break;
				}
			}
		}
		
		StringBuffer script = new StringBuffer();
		script.append("<script type=\"text/javascript\">");
		String auth = "false";
		if (StringUtils.isEmpty(result)) {
			auth = "true";
			result = "系统升级成功";
		} else {
			auth = "false";
		}
		String json = "{\"auth\":\"" + auth + "\",\"msg\":\"" + result
				+ "\",\"filePath\":\"filePath\"}";
		if (StringUtils.isEmpty(this.jsCallBack))
			script.append("parent.uploadCbk(" + json + ");");
		else {
			script.append("parent." + this.jsCallBack + "(" + json + ");");
		}
		script.append("</script>");
		Struts2Utils.renderHtml(script.toString(), new String[0]);
		return null;
	}

	public String[] getFileName() {
		return this.fileName;
	}

	public void setFileName(String[] fileName) {
		this.fileName = fileName;
	}
}
