package org.kylin.klb.web.nginx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Results;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;
import org.kylin.klb.entity.nginx.Location;
import org.kylin.klb.entity.nginx.VirtualServerGroup;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.service.nginx.NginxRealServService;
import org.kylin.klb.service.nginx.NginxVirtualServService;
import org.kylin.klb.web.CrudActionSupport;
import org.kylin.modules.web.struts2.Struts2Utils;

@Namespace("/nginx")
@ParentPackage("struts-default")
@Results(@org.apache.struts2.convention.annotation.Result(name = "reload", location = "service-location.action", type = "redirect"))
public class ServiceLocationAction extends CrudActionSupport<Location> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Location entity = null;
	
	private NginxVirtualServService nginxVirtualServService = new NginxVirtualServService();
	private NginxRealServService nginxRealServService = new NginxRealServService();
	
	private List<Location> locationList = null;
	
	private String vsId;
	private String vsName;
	private File certFile;
	private File keyFile;
	private String uploadMsg;
	
	private VirtualServerGroup virtualService;
	
	
	protected void prepareModel() throws Exception {
		this.entity = new Location();
	}
	public Location getModel() {
		return this.entity;
	}

	public String list() throws Exception {
	//System.out.println("location comming!");
		HttpServletRequest request = Struts2Utils.getRequest();

	//System.out.println("vsId = " + vsId);
	//System.out.println("vsName = " + vsName);
		if(vsId == null || vsId.isEmpty()){
			return null;
		}
		this.virtualService = this.nginxVirtualServService.getVirtualServerById(vsId);
		List<Display> realGroupNameList = this.nginxRealServService.getRealGroupNameDisplayList();
		request.setAttribute("realGroupNameList", realGroupNameList);
		this.locationList = this.nginxVirtualServService.getLocatioList(vsId);
		List<Display> insertList = this.nginxVirtualServService.getLocationInsertDisplayList(this.locationList);
		request.setAttribute("insertList", insertList);
		return "success";
	}
	
	public String save() throws Exception {
		HttpServletRequest request = Struts2Utils.getRequest();
		String retMes = "";
		String mesTmp = "";
		String vsId = request.getParameter("vsId").trim();
		String locationId = request.getParameter("locationId").trim();
//System.out.println("Save location =========");	
//System.out.println("vsId = " + vsId);
//System.out.println("locationId = " + locationId);
		this.entity.setServiceId(vsId);
		this.entity.setLocationId(locationId);
		
		String update = request.getParameter("update").trim();
		if (StringUtils.equals(update.trim(), "1")){
			retMes = this.nginxVirtualServService.updateServiceLocation(this.entity);	
			mesTmp = "数据更新";
		}else{
			retMes = this.nginxVirtualServService.addServiceLocation(this.entity);
			mesTmp = "数据保存";
		}	
	
		if ( retMes.equals("true") ) {
			Struts2Utils.renderJson("{auth:true,\"mess\":\"" + mesTmp + "\"}", new String[0]);
		} else if ( retMes.equals("false") ) {
			Struts2Utils.renderJson("{auth:false,\"mess\":\"" + mesTmp + "\"}", new String[0]);
		} else {
			Struts2Utils.renderJson("{auth:false,\"mess\":\"" + retMes + "\"}", new String[0]);
		}
		return null;
	}

	public String delete() throws Exception {
		HttpServletRequest request = Struts2Utils.getRequest();
		String vsId = request.getParameter("vsId").trim();
		String locationId = request.getParameter("locationId").trim();

		if (StringUtils.isNotEmpty(vsId) && StringUtils.isNotEmpty(locationId)) {
			boolean flag = this.nginxVirtualServService.deleteServiceLocation(vsId,locationId);
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
		String vsId = request.getParameter("vsId").trim();
		String locationId = request.getParameter("locationId").trim();
		if (StringUtils.isNotEmpty(vsId) && StringUtils.isNotEmpty(locationId)) {
			Location tmp = this.nginxVirtualServService.getServiceLocationById(vsId,locationId);
			Struts2Utils.renderJson("{\"auth\":true,\"obj\":"
						+ JSONObject.fromObject(tmp).toString() + "}", new String[0]);
		} else {
			Struts2Utils.renderJson("{\"auth\":false}", new String[0]);
		}
		return null;
	}
	
	public String upload() throws Exception{
		String retMes= "false";
		uploadMsg = "";
	//System.out.println("virtualService = " +this.virtualService);
		this.virtualService.setVsId(this.vsId);
		try {
			uploadMsg = checkCertAndKeyFile();
			if(!"true".equals(uploadMsg)){
				return list();
			}
			StringBuffer certSb = new StringBuffer();
			if(!getCertContext(certSb)){
				uploadMsg = certSb.toString();
				return list();
			}else{
				this.virtualService.setCert(certSb.toString());
			}
			StringBuffer keySb = new StringBuffer();
			if(!getKeytContext(keySb)){
				uploadMsg = keySb.toString();
				return list();
			}else{
				this.virtualService.setKey(keySb.toString());
			}
		
			retMes = this.nginxVirtualServService.updateVirtualServer(this.virtualService);
			if ( retMes.equals("true") ){
				uploadMsg = "数据更新成功";
			}else{
				uploadMsg = retMes;
			}
			
		} catch (Exception e) {
			uploadMsg = "数据更新出现异常";
		}

		return list();
	}
	

	private  X509Certificate getCertFromFile(File file) {
		try {
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			FileInputStream fis = new FileInputStream(file);
			Certificate certificate = cf.generateCertificate(fis);
			fis.close();
			return (X509Certificate) certificate;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		}
		return null;
	}

	private  String checkCert(X509Certificate cert) {
		String retMsg = "true";
		try {
			cert.checkValidity();
		} catch (CertificateExpiredException e) { // 过期
			retMsg = "证书已过期";
		} catch (CertificateNotYetValidException e) { // 尚未生效
			retMsg = "证书还未生效";
		}
		return retMsg;
	}


	public  boolean checkKeyMatch(PublicKey publicKey,PrivateKey privateKey) {
		try {

			// 用私钥对信息生成数字签名
			java.security.Signature signet = java.security.Signature
					.getInstance("MD5withRSA");
			signet.initSign(privateKey);
			signet.update("test".getBytes());
			byte[] retBuf = signet.sign();
			java.security.Signature signetcheck = java.security.Signature
					.getInstance("MD5withRSA");
			signetcheck.initVerify(publicKey);
			signetcheck.update("test".getBytes());
			if (signetcheck.verify(retBuf)) {
				return true;
			} else {
				return false;
			}
		} catch (java.lang.Exception e) {
			return false;
		}
	}

	private  String checkCertAndKeyFile() {
		String retMsg = "true";
		if(this.certFile == null || this.keyFile == null){
			return retMsg;
		}
		Security.addProvider(new BouncyCastleProvider());
		
		// 读入客户端私钥
		X509Certificate cert = getCertFromFile(certFile);
		if (cert == null) {
			retMsg = "不是有效的证书文件";
			return retMsg;
		}
		retMsg = checkCert(cert);
		if (!retMsg.equals("true")) {
			return retMsg;
		}

		try {
			PublicKey publicKey = cert.getPublicKey();
			PEMReader kr = new PEMReader(new FileReader(keyFile));
			KeyPair keyPair = (KeyPair) kr.readObject();
			kr.close();
			if (keyPair == null) {
				retMsg = "不是合法的证书密钥";
				return retMsg;
			}
			PrivateKey privateKey = keyPair.getPrivate();
			if(!checkKeyMatch(publicKey, privateKey)){
				retMsg = "证书与密钥不匹配";
				return retMsg;
			}
			
		} catch (Exception e) {
			//e.printStackTrace();
			String eMsg = e.getMessage();
			if (eMsg.contains("password is required")) {
				retMsg = "证书密钥有密码保护，请选择其它证书";
			}

		}
		return retMsg;
	}
	
	private boolean getCertContext(StringBuffer certSb){
		if(this.certFile == null){
			return true;
		}
		String retMsg = "";
		try {
			InputStream crtIn = new FileInputStream(this.certFile);	
			int certSize = crtIn.available();	
			if (certSize < 1024 || certSize > 1024 * 50) {
				certSb.setLength(0);
				certSb.append("不是合法的证书文件");
				crtIn.close();
				return false;
			}
			crtIn.close();
			
			FileReader fr = new FileReader(this.certFile);
			BufferedReader br = new BufferedReader(fr);
			String temp = null;
			while( (temp = br.readLine()) != null ){
				retMsg += temp + "\n";
			}
			br.close();
			fr.close();
			certSb.setLength(0);
			certSb.append(retMsg);
			return true;
			
		} catch (Exception e) {
			certSb.setLength(0);
			certSb.append("导入证书出现异常");
			e.printStackTrace();	
			return false;
		}

	}
	
	private boolean getKeytContext(StringBuffer sb){
		if(this.keyFile == null){
			return true;
		}
		String retMsg = "";
		try {
			InputStream crtIn = new FileInputStream(this.keyFile);	
			int certSize = crtIn.available();	
			if (certSize < 500 || certSize > 1024*10 ) {
				sb.setLength(0);
				sb.append("不是合法的证书密钥");
				crtIn.close();
				return false;
			}
			crtIn.close();
			
			FileReader fr = new FileReader(this.keyFile);
			BufferedReader br = new BufferedReader(fr);
			String temp = null;
			while( (temp = br.readLine()) != null ){
				retMsg += temp + "\n";
			}
			br.close();
			fr.close();
			sb.setLength(0);
			sb.append(retMsg);
			return true;
		} catch (Exception e) {
			sb.setLength(0);
			sb.append("导入证书密钥异常");
			e.printStackTrace();		
			return false;
		}
	}
	
	

	
	public List<Location> getLocationList() {
		return locationList;
	}
	public void setLocationList(List<Location> locationList) {
		this.locationList = locationList;
	}

	
	public File getCertFile() {
		return certFile;
	}

	public void setCertFile(File certFile) {
		this.certFile = certFile;
	}

	public File getKeyFile() {
		return keyFile;
	}

	public void setKeyFile(File keyFile) {
		this.keyFile = keyFile;
	}
	public String getVsId() {
		return vsId;
	}
	public void setVsId(String vsId) {
		this.vsId = vsId;
	}
	public String getVsName() {
		return vsName;
	}
	public void setVsName(String vsName) {
		this.vsName = vsName;
	}
	public String getUploadMsg() {
		return uploadMsg;
	}
	public void setUploadMsg(String uploadMsg) {
		this.uploadMsg = uploadMsg;
	}
	public VirtualServerGroup getVirtualService() {
		return virtualService;
	}
	public void setVirtualService(VirtualServerGroup virtualService) {
		this.virtualService = virtualService;
	}



}
