package org.kylin.klb.service.linkload;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.kylin.klb.entity.linkload.DnsServer;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.service.KlbClient;
import org.kylin.klb.util.Utils;

public class DnsServerService extends KlbClient {
	
	public List<Display> getStateList() {
		List<Display> stateList = new ArrayList<Display>();
		Display state = new Display("true", "开启");
		stateList.add(state);
		state = new Display("false", "关闭");
		stateList.add(state);
		return stateList;
	}
	
	public List<Display> getReverseList() {
		List<Display> reverseList = new ArrayList<Display>();
		Display reverse = new Display("true", "启用");
		reverseList.add(reverse);
		reverse = new Display("false", "禁用");
		reverseList.add(reverse);
		return reverseList;
	}
	
	public String setDnsServer(DnsServer ds) {		
		Hashtable dnsServerTemp = new Hashtable();
		dnsServerTemp.put("Reverse", ds.getReverse().equals("true") ? true : false);
		dnsServerTemp.put("Port", NumberUtils.toInt(ds.getPort()));
						
		Hashtable enabledTemp = new Hashtable();
		enabledTemp.put("Enabled", ds.getState().equals("true") ? true : false);
		
		String flagStr = "false";
		try {
			Boolean dnsServerResult = (Boolean)executeXml("Bind.Service", dnsServerTemp);
			Boolean enabledResult = (Boolean)executeXml("Bind.Enabled", enabledTemp);
			flagStr = "true";
		} catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}						
		return flagStr;
	}

	public DnsServer getDnsServer() {		
		DnsServer dnsServer = new DnsServer();
		try {
			Map result = (Map)executeXml("Bind.Get.Service");			
			if (result != null) {
				Integer port = (Integer)result.get("Port");
				Boolean reverse = (Boolean)result.get("Reverse");
				Boolean enabled = (Boolean)result.get("Enabled");
				dnsServer.setPort(port.toString());
				dnsServer.setReverse(reverse.toString());
				dnsServer.setState(enabled.toString());				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dnsServer;		
	}
	
}
