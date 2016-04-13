package org.kylin.klb.web.linkload;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Results;
import org.kylin.klb.entity.linkload.DnsIspConfig;
import org.kylin.klb.service.linkload.DnsIspConfigService;
import org.kylin.klb.util.Utils;

import com.opensymphony.xwork2.ActionSupport;

@Namespace("/linkload")
@ParentPackage("struts-default")
@Results(@org.apache.struts2.convention.annotation.Result(name = "reload", location = "dns-isp!mess.action", type = "redirect"))
public class DnsIspConfigAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DnsIspConfigService dics = new DnsIspConfigService();
	private String id;
	private DnsIspConfig dic;
	
	public String list() throws Exception {		
		//ric.setSale(sale);
		if(StringUtils.equals(id, "add")){
			dic = new DnsIspConfig();
			dic.setId(id);
		}
		else{
			dic = dics.getDnsIspConfigById(id);
			dic.setId(id);
		}		
		return "success";
	}
	public String save() throws Exception {
		String dnsIspId = dic.getId();
		if( StringUtils.equals(dnsIspId, "add") ) {			
			String failedMess = null;
			String dnsIspMess =  dics.addDnsIspConfig(dic);
			if ( dnsIspMess.equals("true") ) {			
				failedMess = "";
			} else if ( dnsIspMess.equals("false") ) {				
				failedMess = "ISP地址段添加失败";
			} else {
				failedMess = "您添加的ISP地址段配置错误：" + dnsIspMess;
			}
			Utils.getInstance().setFailedMess(failedMess);			
		} else {			
			String failedMess = null;
			String routeStaticMess =  dics.editDnsIspConfig(dic);
			if ( routeStaticMess.equals("true") ) {
				failedMess = "";
			} else if ( routeStaticMess.equals("false") ) {				
				failedMess = "ISP地址段" + dnsIspId + "配置失败";
			} else {
				failedMess = "ISP地址段" + dnsIspId + "配置错误：" + routeStaticMess;
			}
			Utils.getInstance().setFailedMess(failedMess);
		}
		return "reload";
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public DnsIspConfig getDic() {
		return dic;
	}
	public void setDic(DnsIspConfig dic) {
		this.dic = dic;
	}
	
}
