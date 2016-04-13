package org.kylin.klb.service;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.kylin.klb.exception.FcException;
import org.kylin.modules.web.struts2.Struts2Utils;

public class RegisterService extends KlbClient {

	XmlRpcClient client = null;
	
	/* public RegisterService() {

		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		try {
			config.setServerURL(new URL(Struts2Utils.getString("klb.client.url")));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		this.client = new XmlRpcClient();
		this.client.setConfig(config);
	} */
	
	public boolean register(String registerCode)throws Exception{
		//String[] params = new String[1];
		//params[0] = registerCode;
		//int status = (Integer)this.client.execute("Licence.Register", params);
		try {
			Boolean result = (Boolean)executeXml("Licence.Import", registerCode);
			System.out.println(result);
			if(result!=null && result==true){
				return true;
			}
			else{
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			/* if (StringUtils.equals(e.getMessage(), "Failed to read server's response: Connection refused: connect")) {
				throw ce;
			}
			if (StringUtils.equals(e.getMessage(), "Failed to read server's response: No route to host: connect")) {
				throw ce;
			} */
			return false;
		}		
	}

	public String getCode()throws Exception{
		//String[] params = new String[0];
		//String code = (String)this.client.execute("Licence.Code", params);
		try {
			String code = (String)executeXml("Licence.Machine");
			//System.out.println(code + ";");
			return code;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	public boolean verify()throws Exception{
		//String[] params = new String[0];
		//FcException ce = new FcException();
		//int status;
		try {
			Boolean status = (Boolean)executeXml("Licence.IsExist");
			//status = (Integer)this.client.execute("Licence.Verify", params);
			if( status!=null && status==true ){
				return true;
			}
			else{
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			/* if (StringUtils.equals(e.getMessage(), "Failed to read server's response: Connection refused: connect")) {
				throw ce;
			}
			if (StringUtils.equals(e.getMessage(), "Failed to read server's response: No route to host: connect")) {
				throw ce;
			} */
			return false;
		}
	}
}
