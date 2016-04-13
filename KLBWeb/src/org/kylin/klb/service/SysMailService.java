package org.kylin.klb.service;

import java.util.Hashtable;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;
import org.kylin.klb.entity.security.SmtpMail;
import org.kylin.klb.util.Utils;
public class SysMailService extends KlbClient {
	
	@SuppressWarnings("unchecked")
	public String setSysMail(SmtpMail entity) {		
		Hashtable tableTmp = new Hashtable();
		tableTmp.put("Enabled", entity.getEnabled().equals("true") ? true : false);
		tableTmp.put("Smtp", entity.getSmtp());
		tableTmp.put("User", entity.getUser());
		tableTmp.put("Passwd", entity.getPasswd());
		tableTmp.put("Address", entity.getAddress());
		
		String flagStr = "false";
		try {			
			Boolean result = (Boolean)executeXml("Mail.Set", tableTmp);
			if ( result != null && result == true ) {
				flagStr = "true";
			}
		} catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}

	@SuppressWarnings("unchecked")
	public SmtpMail getSysMail() {		
		SmtpMail entity = new SmtpMail();
		try {
			Map result = (Map)executeXml("Mail.Get");			
			if (result != null) {
				Boolean enabled = (Boolean)result.get("Enabled");
				String smtp = (String)result.get("Smtp");
				String user = (String)result.get("User");
				String passwd = (String)result.get("Passwd");
				String address = (String)result.get("Address");
				entity.setEnabled(enabled.toString());
				entity.setSmtp(smtp);
				entity.setUser(user);
				entity.setPasswd(passwd);
				entity.setAddress(address);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity;		
	}

}
