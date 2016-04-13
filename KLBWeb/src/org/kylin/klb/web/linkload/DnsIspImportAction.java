package org.kylin.klb.web.linkload;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Results;
import org.kylin.klb.entity.linkload.DnsIspConfig;
import org.kylin.klb.service.linkload.DnsIspConfigService;
import org.kylin.klb.util.Utils;

import com.opensymphony.xwork2.ActionSupport;

@Namespace("/linkload")
//@ParentPackage("struts-default")
@Results(@org.apache.struts2.convention.annotation.Result(name = "reload", location = "dns-isp!mess.action", type = "redirect"))
public class DnsIspImportAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DnsIspConfigService dics = new DnsIspConfigService();	
	private DnsIspConfig dic;
	private File upload;
	
	public String list() throws Exception {		
		dic = new DnsIspConfig();
		return "success";
	}
	
	private String judge() {
		String msg = "";
		try {
			InputStream in = new FileInputStream(upload);
			int size = in.available();
			if (size > 1048576) {
				msg = "导入文件的大小错误， 单个文件大小小于1M！</li>";
			}
			in.close();
		} catch (Exception e) {
			msg = "导入出现异常";
			e.printStackTrace();			
		}
		return msg;
	}
	
	public String getImportList() {
		String result = judge();
		String ipTemp = "";
		if (StringUtils.isEmpty(result)) {
			try {
				FileReader fr = new FileReader(upload);
				BufferedReader br = new BufferedReader(fr);
				String temp = null;
				while( (temp = br.readLine()) != null ){
					ipTemp += temp + ";";
				}
				br.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}						
		}
		return ipTemp;
	}
		
	public String save() {		
		String failedMess = null;		
		String importList = getImportList();		
		dic.setIpList(importList);
		String dnsIspMess =  dics.addDnsIspConfig(dic);
		if ( dnsIspMess.equals("true") ) {			
			failedMess = "";
		} else if ( dnsIspMess.equals("false") ) {
			failedMess = "ISP地址段导入失败";
		} else {
			failedMess = "您导入的ISP地址段配置错误：" + dnsIspMess;
		}
		Utils.getInstance().setFailedMess(failedMess);
		return "reload";
	}
	
	public DnsIspConfig getDic() {
		return dic;
	}
	public void setDic(DnsIspConfig dic) {
		this.dic = dic;
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}
	
}
