package org.kylin.klb.service;

import java.util.Hashtable;
import java.util.Map;
import org.apache.xmlrpc.XmlRpcException;
import org.kylin.klb.entity.security.Syslog;
import org.kylin.klb.service.KlbClient;
import org.kylin.klb.util.Utils;
public class SyslogService extends KlbClient {
	
	public String setSyslog(Syslog sl) {		
		Hashtable syslogTemp = new Hashtable();
		syslogTemp.put("Enabled", sl.getEnabled().equals("true") ? true : false);
		syslogTemp.put("Domain", sl.getDomain());
		
		String flagStr = "false";
		try {			
			Boolean syslogResult = (Boolean)executeXml("Logger.Set", syslogTemp);
			if ( syslogResult != null && syslogResult == true ) {
				flagStr = "true";
			}
		} catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}

	public Syslog getSyslog() {		
		Syslog syslog = new Syslog();
		try {
			Map result = (Map)executeXml("Logger.Get");			
			if (result != null) {
				Boolean enabled = (Boolean)result.get("Enabled");
				String domain = (String)result.get("Domain");
				syslog.setEnabled(enabled.toString());
				syslog.setDomain(domain);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return syslog;		
	}

}
