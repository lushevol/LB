package org.kylin.klb.service.linkload;

import java.util.Hashtable;
import java.util.Map;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.kylin.klb.entity.linkload.DnsIspConfig;
import org.kylin.klb.entity.network.RouteStaticConfig;
import org.kylin.klb.service.KlbClient;
import org.kylin.klb.util.Utils;

public class DnsIspConfigService extends KlbClient {
	
	public DnsIspConfig getDnsIspConfigById(String id) {
		//id = "_" + id + "_";
		DnsIspConfig ret = new DnsIspConfig();
		
		try {			
			Hashtable DnsIspTemp = new Hashtable();
			DnsIspTemp.put("ID", NumberUtils.toInt(id));			
			Object result = (Object)executeXml("ISP.Get.Item.All", DnsIspTemp);			
			if (result != null) {
				Map res = (Map)result;
				String name = (String)res.get("Name");				
				Object[] nets = (Object[])res.get("Net");
				
				/* System.out.println(id);
				System.out.println(name);
				System.out.println(nets); */
				
				ret.setName(name);
				
				String ipList = new String();
				if(nets.length==0){
					ipList = "";
				}
				else{
					for(int j=0; j<nets.length; j++){
						if (!StringUtils.equals((String)nets[j], "")) {
							ipList += nets[j] + ";";
						}
					}
					ipList = ipList.substring(0, ipList.length()-1);
				}
				ret.setIpList(ipList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ret;
	}
	
	public String addDnsIspConfig(DnsIspConfig dic) {										
		String id = dic.getId();
		Boolean addIspResult = null;
			
		Hashtable addIspTemp = new Hashtable();
		addIspTemp.put("ID", NumberUtils.toInt(id));
		addIspTemp.put("Name", dic.getName());			
		
		String ipStr = dic.getIpList();
		String[] ipArray = null;
		if (ipStr.equals("")) {
			ipArray = new String[0];
		} else {
			ipArray = dic.getIpList().split(";");
		}				
		addIspTemp.put("Net", ipArray);		
		
		String flagStr = "false";
		try {
			addIspResult = (Boolean)executeXml("ISP.Add", addIspTemp);
			System.out.println(addIspResult);
			flagStr = "true";
		}catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}
	
	public String editDnsIspConfig(DnsIspConfig dic) {				
		String id = dic.getId();
		Boolean editIspResult = null;
			
		Hashtable editIspTemp = new Hashtable();
		editIspTemp.put("ID", NumberUtils.toInt(id));
		editIspTemp.put("Name", dic.getName());					
		
		String ipStr = dic.getIpList();
		String[] ipArray = null;
		if (ipStr.equals("")) {
			ipArray = new String[0];
		} else {
			ipArray = dic.getIpList().split(";");
		}						
		editIspTemp.put("Net", ipArray);
		
		String flagStr = "false";
		try {
			editIspResult = (Boolean)executeXml("ISP.Set",editIspTemp);
			System.out.println(editIspResult);						
			flagStr = "true";
		}catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}	
	
}
