package org.kylin.klb.service.network;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.xmlrpc.XmlRpcException;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.kylin.klb.entity.network.Dns;
import org.kylin.klb.service.KlbClient;
import org.kylin.klb.util.Utils;

public class DnsService extends KlbClient {
	//private static String methodName = "Execute";
	//private static String xmlName = "dns.xml";
	
	public String setdns(String firstadd, String secondadd) {
		System.out.println(firstadd);
		System.out.println(secondadd);
		Object[] dnsObj = null;		
		if (firstadd.equals("")) {
			if (secondadd.equals("")) {
				String[] dnsArray = {};
				dnsObj = dnsArray;
			} else {
				String[] dnsArray = {secondadd};
				dnsObj = dnsArray;
			}
		} else {
			if (secondadd.equals("")) {
				String[] dnsArray = {firstadd};
				dnsObj = dnsArray;
			} else {
				String[] dnsArray = {firstadd, secondadd};
				dnsObj = dnsArray;
			}
		}
				
		String flagStr = "false";		
		try {			
			Boolean setResult = (Boolean)executeXml("Dns.Server.Set", dnsObj);
			System.out.println(setResult);
			if ( setResult != null && setResult == true ) {
				flagStr = "true";
			}
		} catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;		
	}
	
	public Dns getdns()
	{
		Dns dns = null;
		try {
			Object[] result = (Object[])executeXml("Dns.Server.Get");			
			if (result != null) {
				
				String primary = new String();
				String secondary = new String();
				if(result.length==0){
					primary = "";
					secondary = "";
				} else if (result.length==1) {					
					if (!StringUtils.equals((String)result[0], "")) {
						primary = (String)result[0];
					}
					secondary = "";
				} else {
					if (!StringUtils.equals((String)result[0], "")) {
						primary = (String)result[0];
					}
					if (!StringUtils.equals((String)result[1], "")) {
						secondary = (String)result[1];
					}
				}
				dns = new Dns();
				dns.setFirstadd(primary);
				dns.setSecondadd(secondary);				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return dns;		
	}

}
