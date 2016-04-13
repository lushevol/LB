package org.kylin.klb.service.nginx;

import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.xmlrpc.XmlRpcException;
import org.kylin.klb.entity.nginx.NginxGlobalConf;
import org.kylin.klb.service.KlbClient;
import org.kylin.klb.util.Utils;
import org.kylin.modules.utils.StringUtils;

public class NginxConfService extends KlbClient {

	@SuppressWarnings("unchecked")
	public NginxGlobalConf getNginxGlobalConf() {
		NginxGlobalConf entity = new NginxGlobalConf();
		try {
			Object result = (Object)executeXml("Http.Get");
			
			if (result != null) {				
				Map res = (Map)result;
				
				String status = res.get("Status").toString();
				entity.setStatus(status);
				
				String enabled = res.get("Enabled").toString();
				entity.setEnabled(enabled);
				
				String denyNotMatch = res.get("DenyNotMatch").toString();
				entity.setDenyNotMatch(denyNotMatch);
		
				String processor = res.get("Processor").toString();
				entity.setProcessor(processor);
				
				String connections = res.get("Connections").toString();
				entity.setConnections(connections);
				
				String keepalive = res.get("Keepalive").toString();
				entity.setKeepalive(keepalive);
				
				String gzip = res.get("Gzip").toString();
				entity.setGzip(gzip);
				
				String gzipLength = res.get("GzipLength").toString();
				entity.setGzipLength(gzipLength);
				
	//System.out.println(entity);
			}									
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	public String setNginxGlobalConf(NginxGlobalConf entity){
		String flagStr = "false";
		
		Hashtable setTemp = new Hashtable();	
		
		if (StringUtils.isNotEmpty(entity.getEnabled())) {
			setTemp.put("Enabled", entity.getEnabled().equals("true") ? true : false);			
		}	
		
		if (StringUtils.isNotEmpty(entity.getDenyNotMatch())) {
			setTemp.put("DenyNotMatch", entity.getDenyNotMatch().equals("true") ? true : false);			
		}	
		
		if (StringUtils.isNotEmpty(entity.getProcessor())) {
			setTemp.put("Processor", NumberUtils.toInt(entity.getProcessor()));
		}
		if (StringUtils.isNotEmpty(entity.getConnections())) {
			setTemp.put("Connections", NumberUtils.toInt(entity.getConnections()));
		}
		
		if (StringUtils.isNotEmpty(entity.getKeepalive())) {
			setTemp.put("Keepalive", NumberUtils.toInt(entity.getKeepalive()));
		}
			
		if (StringUtils.isNotEmpty(entity.getGzip())) {
			setTemp.put("Gzip", entity.getGzip().equals("true") ? true : false);			
		}	
		
		if (StringUtils.isNotEmpty(entity.getGzipLength())) {
			setTemp.put("GzipLength", NumberUtils.toInt(entity.getGzipLength()));				
		}
		
		
		try {
			Boolean setResult = (Boolean)executeXml("Http.Set",setTemp);
			if ( setResult != null && setResult == true ) {
				flagStr = "true";
			}
		} catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;		
	}

	@SuppressWarnings("unchecked")
	public String changeNginxServerStatu(String enabled) {
		Hashtable enableTemp = new Hashtable();
		enableTemp.put("Enabled", enabled.equals("true") ? true : false);
		
		String flagStr = "false";	
		try {
			Boolean enableResult = (Boolean)executeXml("Http.Set",enableTemp);
			if ( enableResult != null && enableResult == true ) {
				flagStr = "true";
			}
		} catch (XmlRpcException e) {			
			e.printStackTrace();		
		}
		return flagStr;
	}

}
