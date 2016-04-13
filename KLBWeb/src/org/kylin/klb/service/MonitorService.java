package org.kylin.klb.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.entity.security.LoadType;
import org.kylin.klb.entity.security.Monitor;
import org.kylin.klb.util.Utils;

public class MonitorService extends KlbClient {

	private static String methodName = "Execute";

	private Map<String, String> types = new HashMap();

	public MonitorService() {
		this.types.put("0", "关闭");
		this.types.put("1", "ping检测");
		this.types.put("2", "端口检测");
	}

	public List<Display> getTypeList(){
		List<Display> typeList = new ArrayList<Display>();
		Display type1 = new Display("0", "关闭");
		Display type2 = new Display("1", "ping检测");
		Display type3 = new Display("2", "端口检测");
		typeList.add(type1);
		typeList.add(type2);
		typeList.add(type3);
		return typeList;
	}
	
	public Monitor getMonitorByName(String name) {

		Monitor ret = new Monitor();
		if (name.equalsIgnoreCase(""))
			return null;
		try {
			
			Hashtable vsNameTemp = new Hashtable();
			vsNameTemp.put("All", true);
			Object[] vsNameResult = (Object[])executeXml("VirtualService.Get",vsNameTemp);
			
			String existNameList = "";
			if (vsNameResult != null) {
				for (int i = 0; i < vsNameResult.length; i++) {
					Map res = (Map)vsNameResult[i];
					String existName = (String)res.get("Name");
					if ( !existName.equals("") ) {
						existNameList += existName + ";";
					}
				}
			}
			
			if ( !existNameList.contains(name)) {
				return null;
			} else {
				Hashtable nameToIdTemp = new Hashtable();
				nameToIdTemp.put("Name", name);
				Object nameToIdResult = (Object)executeXml("VirtualService.NameToID",nameToIdTemp);
				Integer id = null;
				if (nameToIdResult == null) {
					return null;
				} else {
					Map res = (Map)nameToIdResult;
					id = (Integer)res.get("ID");
				}
				
				Hashtable vsTemp = new Hashtable();
				vsTemp.put("Start", id);
				vsTemp.put("Count", 1);
				Object[] result = (Object[])executeXml("VirtualService.Get",vsTemp);
				
				if (result != null) {
					Map res = (Map)result[0];
					Integer mark = (Integer)res.get("Mark");
					String descrip = (String)res.get("Description");					
					Object monitorObj = (Object)res.get("Monitor");
					
					ret.setServiceName(name);
										
					if ( monitorObj != null ) {
						Map monitorMap = (Map)monitorObj;
						Integer interval = (Integer)monitorMap.get("Interval");
						Integer timeout = (Integer)monitorMap.get("Timeout");
						Integer retry = (Integer)monitorMap.get("Retry");
						Integer type = (Integer)monitorMap.get("Type");
						Integer port = (Integer)monitorMap.get("Port");
												
						System.out.println(interval);
						System.out.println(timeout);
						System.out.println(retry);
						System.out.println(type);
						System.out.println(port);
						
						ret.setInterval(interval.toString());
						ret.setPort(port.toString());
						ret.setRetry(retry.toString());
						ret.setTimeout(timeout.toString());
						ret.setType(type.toString());
					}																
				}
			}			
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Monitor getMonitorById(String id) {
		
		Monitor ret = new Monitor();
		try {
			
			Hashtable vsTemp = new Hashtable();
			vsTemp.put("Start", NumberUtils.toInt(id));
			vsTemp.put("Count", 1);
			Object[] result = (Object[])executeXml("VirtualService.Get",vsTemp);
			
			if (result != null) {
				Map res = (Map)result[0];				
				String name = (String)res.get("Name");
				Object monitorObj = (Object)res.get("Monitor");
				Object[] servers = (Object[])res.get("Servers");
				
				Map monitorMap = (Map)monitorObj;
				
				Integer interval = (Integer)monitorMap.get("Interval");
				Integer timeout = (Integer)monitorMap.get("Timeout");
				Integer retry = (Integer)monitorMap.get("Retry");
				Integer type = (Integer)monitorMap.get("Type");
				Integer port = (Integer)monitorMap.get("Port");
				
				Integer date = (Integer)monitorMap.get("Date");
				String mail = (String)monitorMap.get("Mail");
				Boolean enabled = (Boolean)monitorMap.get("Enabled");
				
				ret.setVsId(id);
				ret.setServiceName(name);
				ret.setInterval(interval.toString());
				ret.setPort(port.toString());
				ret.setRetry(retry.toString());
				ret.setTimeout(timeout.toString());
				ret.setType(type.toString());		
				
				ret.setEnabled(enabled.toString());
				ret.setMail(mail);
				ret.setDate(date.toString());
			}
			
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String saveServiceMonitor(Monitor monitor) {							
		String id = monitor.getVsId();
		//System.out.println(id);
		Hashtable servTemp = new Hashtable();
		servTemp.put("ID", NumberUtils.toInt(id));
		Object monitorObj = new Object();

		Hashtable monitorTemp = new Hashtable();		
		monitorTemp.put( "Interval", NumberUtils.toInt(monitor.getInterval()) );
		monitorTemp.put( "Timeout", NumberUtils.toInt(monitor.getTimeout()) );
		monitorTemp.put( "Retry", NumberUtils.toInt(monitor.getRetry()) );
		monitorTemp.put( "Type", NumberUtils.toInt(monitor.getType()) );
		monitorTemp.put( "Port", NumberUtils.toInt(monitor.getPort()) );
		
		monitorTemp.put("Enabled", monitor.getEnabled().equals("true") ? true : false);
		monitorTemp.put("Mail", monitor.getMail());
		monitorTemp.put( "Date", NumberUtils.toInt(monitor.getDate()) );
		
				
		monitorObj = monitorTemp;			
		servTemp.put("Monitor", monitorObj);
		
		String flagStr = "false";
		try {
			Boolean monitorResult = (Boolean)executeXml("VirtualService.Service", servTemp);
			flagStr = "true";			
		} catch (XmlRpcException e) {			
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}

	/* public Element getMonitorForSet(Monitor moni) {
		String xmlName = "monitor.xml";
		Document doc = getDocumentByInputStream(xmlName);

		Element interval = doc.getRootElement().element("Interval");
		Element port = doc.getRootElement().element("Port");
		Element retry = doc.getRootElement().element("Retry");
		Element timeout = doc.getRootElement().element("Timeout");
		Element type = doc.getRootElement().element("Type");

		interval.addAttribute("value", moni.getInterval());
		interval.addAttribute("set", "1");

		port.addAttribute("value", moni.getPort());
		port.addAttribute("set", "1");

		retry.addAttribute("value", moni.getRetry());
		retry.addAttribute("set", "1");

		timeout.addAttribute("value", moni.getTimeout());
		timeout.addAttribute("set", "1");

		type.addAttribute("value", moni.getType());
		type.addAttribute("set", "1");

		Element monitor = doc.getRootElement();
		return monitor;
	} */

	/* public Element getMonitorForGet() {
		String xmlName = "monitor.xml";
		Document doc = getDocumentByInputStream(xmlName);

		Element interval = doc.getRootElement().element("Interval");
		Element port = doc.getRootElement().element("Port");
		Element retry = doc.getRootElement().element("Retry");
		Element timeout = doc.getRootElement().element("Timeout");
		Element type = doc.getRootElement().element("Type");

		interval.addAttribute("get", "1");
		port.addAttribute("get", "1");
		retry.addAttribute("get", "1");
		timeout.addAttribute("get", "1");
		type.addAttribute("get", "1");

		Element monitor = doc.getRootElement();
		return monitor;
	} */

	/* public String getData() {
		String xmlName = "service.xml";
		Document doc = getDocumentByInputStream(xmlName);
		Element services = doc.getRootElement().element("Services");
		services.addAttribute("get", "1");

		String servicesResult = (String) executeXml(methodName, doc.asXML());
		//
		try {
			Document newDoc = DocumentHelper.parseText(servicesResult);
			Element newServices = newDoc.getRootElement().element("Services");
			for (Iterator i = newServices.elementIterator(); i.hasNext();) {
				Element service = (Element) i.next();
				Element monitor = getMonitorForGet();
				service.add(monitor);
			}
			String monitorResult = (String) executeXml(methodName, newDoc.asXML());

			return monitorResult;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	} */

	public List<Monitor> getMonitors() {
		List<Monitor> monitors = new ArrayList<Monitor>();

		try {
			
			Hashtable vsTemp = new Hashtable();
			vsTemp.put("All", true);
			Object[] result = (Object[])executeXml("VirtualService.Get",vsTemp);
			
			if (result == null || result.length ==0) {
				return null;
			} else {
				for (int i = 0; i < result.length; i++) {
					Map res = (Map)result[i];
					Integer id = (Integer)res.get("ID");					
					String name = (String)res.get("Name");										
					Object monitorObj = (Object)res.get("Monitor");
					
					Map monitorMap = (Map)monitorObj;
					Integer interval = (Integer)monitorMap.get("Interval");
					Integer timeout = (Integer)monitorMap.get("Timeout");
					Integer retry = (Integer)monitorMap.get("Retry");
					Integer type = (Integer)monitorMap.get("Type");
					Integer port = (Integer)monitorMap.get("Port");
					Integer date = (Integer)monitorMap.get("Date");
					String mail = (String)monitorMap.get("Mail");
					Boolean enabled = (Boolean)monitorMap.get("Enabled");
					
					Monitor monitor = new Monitor();
					
					monitor.setVsId(id.toString());
					monitor.setServiceName(name);

					monitor.setInterval(interval.toString());
					monitor.setPort(port.toString());
					monitor.setRetry(retry.toString());
					monitor.setTimeout(timeout.toString());
					monitor.setType(type.toString());
					monitor.setEnabled(enabled.toString());
					monitor.setMail(mail);
					monitor.setDate(date.toString());
					
					monitors.add(monitor);		

				
				}
			}												
			return monitors;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Monitor> convert() {
		List<Monitor> monitors = getMonitors();
		List<Monitor> ret = new ArrayList<Monitor>();
		if ( monitors != null && monitors.size() != 0 ) {
			Iterator it = monitors.iterator();
			while (it.hasNext()) {
				Monitor monitor = (Monitor) it.next();
				String type = monitor.getType();
				monitor.setType(types.get(type));
				ret.add(monitor);
			}
		}		
		return ret;
	}

}
