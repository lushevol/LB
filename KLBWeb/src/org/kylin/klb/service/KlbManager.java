package org.kylin.klb.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.xmlrpc.XmlRpcException;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.kylin.klb.entity.security.DDos;
import org.kylin.klb.entity.security.Director;
import org.kylin.klb.entity.security.Hostname;
import org.kylin.klb.entity.security.LoadInterfaces;
import org.kylin.klb.entity.security.LoadScheduler;
import org.kylin.klb.entity.security.LoadSedundant;
import org.kylin.klb.entity.security.LoadSirtualServ;
import org.kylin.klb.entity.security.LoadTrueServ;
import org.kylin.klb.entity.security.Pinggroups;
import org.kylin.klb.entity.security.SmtpMail;
import org.kylin.klb.entity.security.Statistics;
import org.kylin.klb.entity.security.SystemHard;
import org.kylin.klb.entity.security.User;
import org.kylin.klb.util.Utils;
import org.kylin.modules.utils.StringUtils;
import org.kylin.modules.utils.UUID;
import org.kylin.modules.web.struts2.Struts2Utils;
import org.springframework.stereotype.Service;

@Service
public class KlbManager extends KlbClient {
	private static String methodName = "Execute";

	public DDos getDDOS() {		
		DDos d = new DDos();		
		try {
			Boolean result = (Boolean)executeXml("AntiDos.Get");
			if (result != null) {				
				if (result.equals(false)) {
					d.setDdosset("off");
					d.setStatus("关闭");
				} else {
					d.setDdosset("on");
					d.setStatus("开启");
				}				
			}									
			return d;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String setDDOS(String status) {		
		Boolean b = null;
		if (status.equals("off")) {
			b = false;
		} else {
			b = true;
		}
							
		String flagStr = "false";
		try {			
			Boolean result = (Boolean)executeXml("AntiDos.Set", b);							
			if ( result != null && result == true ) {
				flagStr = "true";
			}
		} catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}
	
	public String setHostname(String hostname) {
		String flagStr = "false";
		try {
			Boolean result = (Boolean)executeXml("Hostname.Set", hostname);
			if (result != null && result == true) {
				flagStr = "true";
			}
		} catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}

	public Hostname getHostname() {		
		Hostname hostname = new Hostname();
		try {			
			String result = (String)executeXml("Hostname.Get");
			if (result != null) {
				hostname.setHostname(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hostname;		
	}

	public List<LoadScheduler> loadSchedulerGetScheduler() throws XmlRpcException{
		List<String> list = new ArrayList<String>();
		list = getSchedulerName();

		List<LoadScheduler> schedulers = new ArrayList<LoadScheduler>();
		if ((list != null) && (!list.isEmpty())) {
			for (String scheName : list) {
				LoadScheduler sch = new LoadScheduler();
				sch.setSchedulerName(scheName);
				schedulers.add(sch);
			}
		}
		return schedulers;
	}

	public List<LoadScheduler> getScheduler()throws XmlRpcException {
		List<String> schedulerNameListwithLocalMachine = new ArrayList();
		List<String> schedulerNameListwithoutLocalMachine = getSchedulerName();
		schedulerNameListwithLocalMachine.add("本地");
		schedulerNameListwithLocalMachine.addAll(schedulerNameListwithoutLocalMachine);

		List schedulers = new ArrayList();
		if ((schedulerNameListwithLocalMachine != null)
				&& (!schedulerNameListwithLocalMachine.isEmpty())) {
			for (String scheName : schedulerNameListwithLocalMachine) {
				LoadScheduler sch = new LoadScheduler();
				sch.setSchedulerName(scheName);
				schedulers.add(sch);
			}
		}
		return schedulers;
	}

	public List<String> getSchedulerName()throws XmlRpcException {
		
		String localName = (String)executeXml("Hostname.Get");
		List list = new ArrayList();
		if (localName == null) {
			return null;
		} else {
						
			list.add(localName);
		}
						
		return list;
	}

	public String setScheduler(String name) throws XmlRpcException {
		String xmlName = "scheduler.xml";
		Document doc = getDocumentByInputStream(xmlName);
		Element devices = doc.getRootElement().element("Devices");
		devices.addAttribute("add", "1");
		devices.addElement((name.matches("^[\\d]+[\\.\\w]*$")) ? "_" + name : name);

		Object obj = executeXml(methodName, doc.asXML());
		return (String) obj;
	}

	public boolean removeScheduler(String schedulerName) {
		boolean flag = false;
		try {
			String xmlName = "scheduler.xml";
			Document doc = getDocumentByInputStream(xmlName);
			Element devices = doc.getRootElement().element("Devices");
			devices.addAttribute("remove", "1");
			devices.addElement(schedulerName);
			executeXml(methodName, doc.asXML());
			flag = true;
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
	
	public List<Pinggroups> loadPinggroupsGetPinggroups() {
		List<Pinggroups> pinggroups = new ArrayList<Pinggroups>();
		try {
			Object result = (Object)executeXml("HA.Get");
			
			if (result != null) {				
				Map res = (Map)result;
				Object[] pings = (Object[])res.get("Ping");
				
				if ((pings != null) && (pings.length != 0)) {
					Object[] ping = (Object[])pings[0];
					
					if ((ping != null) && (ping.length != 0)) {
						for (int j = 0; j < ping.length; j++) {
							Pinggroups sch = new Pinggroups();
							sch.setFirstadd(ping[j].toString());
							pinggroups.add(sch);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}				
		return pinggroups;
	}
	
	public String setfirstadd(String firstadd) {
		Hashtable setTemp = new Hashtable();													
		Object[] pingsObj = new Object[1];
		
		String flagStr = "false";
		try {
			Object result = (Object)executeXml("HA.Get");			
			if (result != null) {				
				Map res = (Map)result;
				Object[] pings = (Object[])res.get("Ping");
				
				if ((pings != null) && (pings.length != 0)) {
					Object[] ping = (Object[])pings[0];
					
					Object[] pingObjs = new Object[ping.length + 1];
					if ((ping != null) && (ping.length != 0)) {
						for (int j = 0; j < ping.length; j++) {
							pingObjs[j] = ping[j];
							if (StringUtils.isNotEmpty(firstadd)) {
								pingObjs[j+1] = firstadd;																			
							}
						}
					}
					pingsObj[0] = pingObjs;
					/* String[] pingStr = new String[ping.length + 1];
					if ((ping != null) && (ping.length != 0)) {
						for (int j = 0; j < ping.length; j++) {
							pingStr[j] = ping[j].toString();
							if (StringUtils.isNotEmpty(firstadd)) {
								pingStr[j+1] = firstadd;																			
							}
						}
					}
					pingsObj[0] = (Object[])pingStr; */
				} else {
					Object[] pingObjs = new Object[1];
					if (StringUtils.isNotEmpty(firstadd)) {
						pingObjs[0] = firstadd;																			
					}
					pingsObj[0] = pingObjs;
				}
			}																
			setTemp.put("Ping", pingsObj);
			Boolean setResult = (Boolean)executeXml("HA.Set",setTemp);
			//System.out.println(setResult);																						
			flagStr = "true";
		} catch (XmlRpcException e) {			
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}
	
	public boolean removefirstadd(String firstadd) {
		boolean flag = false;
		try {
			Hashtable setTemp = new Hashtable();
													
			Object[] pingsObj = new Object[1];									
			Object result = (Object)executeXml("HA.Get");
			
			if (result != null) {
				Map res = (Map)result;
				Object[] pings = (Object[])res.get("Ping");
				
				if ((pings != null) && (pings.length != 0)) {
					Object[] ping = (Object[])pings[0];
					
					String[] pingStr = new String[ping.length - 1];
					String pingStrTemp = "";					
					if ((ping != null) && (ping.length != 0)) {
						for (int j = 0; j < ping.length; j++) {
							if ( !ping[j].toString().equals(firstadd) ){
								pingStrTemp += ping[j].toString() + ";";
							}
							pingStr = pingStrTemp.split(";");
														
							/* pingStr[j] = ping[j].toString();
							if (StringUtils.isNotEmpty(firstadd)) {
								
								pingStr[j+1] = firstadd;
							} */
						}
					}
					pingsObj[0] = (Object[])pingStr;
				}
			}																
			setTemp.put("Ping", pingsObj);
			Boolean setResult = (Boolean)executeXml("HA.Set",setTemp);
			//System.out.println(setResult);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;		
	}

	public boolean removeSirtualServ(String id) {
		boolean flag = false;
		try {
			Hashtable delTemp = new Hashtable();
			delTemp.put("ID", NumberUtils.toInt(id));
			Boolean delResult = (Boolean)executeXml("VirtualService.Delete",delTemp);
			//System.out.println(delResult);						
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	public boolean removeTrueServ(String vsId, String tsId) {
		boolean flag = false;
		try {
			
			Hashtable delTemp = new Hashtable();
			delTemp.put("ID", NumberUtils.toInt(vsId));
			
			Object[] vsIdObj = new Object[1];
			Hashtable tsIdTemp = new Hashtable();
			tsIdTemp.put("ID", NumberUtils.toInt(tsId));
			vsIdObj[0] = tsIdTemp;
			
			delTemp.put("Servers", vsIdObj);
			Boolean delResult = (Boolean)executeXml("VirtualService.Server.Delete",delTemp);
			//System.out.println(delResult);
						
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	public String configureCommit() {
		String flagStr = "false";
		try {						
			Boolean result = (Boolean)executeXml("Configure.Save");			
			flagStr = "true";
		} catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}
	public String reloadCommit() {
		String flagStr = "false";
		try {
			Boolean result = (Boolean)executeXml("Configure.Reload");
			flagStr = "true";
		} catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}

	public Set<LoadInterfaces> getInterfacesForSedundant() throws XmlRpcException {
		String xmlName = "interfaces.xml";
		Document doc = getDocumentByInputStream(xmlName);
		doc.getRootElement().element("Network").element("Interfaces").addAttribute("get", "1");
		Object obj = executeXml(methodName, doc.asXML());
		if (obj == null) {
			return null;
		}
		Set set = new HashSet();
		try {
			Document document = DocumentHelper.parseText((String) obj);
			Element inter = document.getRootElement().element("Network").element("Interfaces");
			if (!inter.elements().isEmpty())
				for (Iterator i = inter.elementIterator(); i.hasNext();) {
					Element e = (Element) i.next();
					// 
					LoadInterfaces lf = new LoadInterfaces();
					lf.setInterfacesForSedundant(e.getName());
					set.add(lf);
				}
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		return set;
	}

	public String setSedundant(LoadSedundant sed) {
		
		Hashtable setTemp = new Hashtable();						
		if (StringUtils.isNotEmpty(sed.getDeadtime())) {
			setTemp.put("Deadtime", NumberUtils.toInt(sed.getDeadtime()));
		}
		if (StringUtils.isNotEmpty(sed.getInittime())) {
			setTemp.put("Initdead", NumberUtils.toInt(sed.getInittime()));
		}
		if (StringUtils.isNotEmpty(sed.getInterfaces())) {
			setTemp.put("Dev", sed.getInterfaces());
		}
		if (StringUtils.isNotEmpty(sed.getKeepalive())) {
			setTemp.put("Keepalive", NumberUtils.toInt(sed.getKeepalive()));
		}
		if (StringUtils.isNotEmpty(sed.getIndirect())) {
			setTemp.put("Autoback", sed.getIndirect().equals("true") ? true : false);			
		}			
		if (StringUtils.isNotEmpty(sed.getUcast())) {
			setTemp.put("IP", sed.getUcast());				
		}
		if (StringUtils.isNotEmpty(sed.getUdpport())) {
			setTemp.put("Port", NumberUtils.toInt(sed.getUdpport()));				
		}
		if (StringUtils.isNotEmpty(sed.getWarntime())) {
			setTemp.put("Warntime", NumberUtils.toInt(sed.getWarntime()));				
		}
		if (StringUtils.isNotEmpty(sed.getHostname())) {
			setTemp.put("Hostname", sed.getHostname());				
		}
		if (StringUtils.isNotEmpty(sed.getMasterDevice())) {
			setTemp.put("Sync", NumberUtils.toInt(sed.getMasterDevice()));				
		}
		
		if (StringUtils.isNotEmpty(sed.getSend())) {
			setTemp.put("Send", sed.getSend().equals("true") ? true : false);				
		}
		
		if (StringUtils.isNotEmpty(sed.getAddress())) {
			setTemp.put("Address", sed.getAddress());				
		}
		
		
		String flagStr = "false";
		try {
			Boolean setResult = (Boolean)executeXml("HA.Set",setTemp);
			flagStr = "true";
		} catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;		
	}

	public LoadSedundant getSedundant() {
		LoadSedundant sed = null;
		try {
			Object result = (Object)executeXml("HA.Get");
			
			if (result != null) {				
				Map res = (Map)result;
				Integer keepalive = (Integer)res.get("Keepalive");
				Integer deadtime = (Integer)res.get("Deadtime");
				Integer warntime = (Integer)res.get("Warntime");
				Integer initdead = (Integer)res.get("Initdead");
				Boolean autoback = (Boolean)res.get("Autoback");
				Integer port = (Integer)res.get("Port");
				String ip = (String)res.get("IP");
				String dev = (String)res.get("Dev");
				Boolean devStatus = (Boolean)res.get("DevStatus");
				String hostname = (String)res.get("Hostname");
				Boolean enabled = (Boolean)res.get("Enabled");
				Boolean self = (Boolean)res.get("Self");
				Boolean other = (Boolean)res.get("Other");
				Integer sync = (Integer)res.get("Sync");
				
				Boolean send = (Boolean)res.get("Send");
				String address = (String)res.get("Address");
				
				/* //System.out.println(keepalive);
				//System.out.println(deadtime);
				//System.out.println(warntime);
				//System.out.println(initdead);
				//System.out.println(autoback);
				//System.out.println(port);
				//System.out.println(ip);
				//System.out.println(dev);
				//System.out.println(devStatus);
				//System.out.println(hostname);
				//System.out.println(enabled);
				//System.out.println(self);
				//System.out.println(other);
				//System.out.println(sync); */
				
				sed = new LoadSedundant();
				
				sed.setIndirect(autoback.toString());
				sed.setEnabled(enabled.toString());
				sed.setSelf(self.toString());
				sed.setOther(other.toString());
				sed.setDeadtime(deadtime.toString());
				sed.setInittime(initdead.toString());
				sed.setInterfaces(dev);
				sed.setKeepalive(keepalive.toString());
				if (sync != null) {
					sed.setMasterDevice(sync.toString());
				}
				sed.setUcast(ip);
				sed.setHostname(hostname);
				sed.setUdpport(port.toString());
				sed.setWarntime(warntime.toString());
				sed.setSend(send.toString());
				sed.setAddress(address);
			}									
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sed;
	}

	public List<String> getSirtualServNames() {
		List list = new ArrayList();
		
		try {			
			Hashtable vsNameTemp = new Hashtable();
			vsNameTemp.put("All", true);
			Object[] vsNameResult = (Object[])executeXml("VirtualService.Get",vsNameTemp);
			
			String existNameList = "";
			if (vsNameResult == null || vsNameResult.length == 0) {
				return null;
			} else {
				for (int i = 0; i < vsNameResult.length; i++) {
					Map res = (Map)vsNameResult[i];
					String existName = (String)res.get("Name");
					if ( !existName.equals("") ) {
						list.add(existName);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<String> getSirtualServIPs() throws XmlRpcException {
		List list = new ArrayList();
		Document doc = getServiceXmlDocument();
		doc.getRootElement().element("Services").addAttribute("get", "1");
		String xml = (String) executeXml(methodName, doc.asXML());
		if (xml == null)
			return null;
		try {
			Document doc1 = DocumentHelper.parseText(xml);
			Element ser1 = doc1.getRootElement().element("Services");
			if (ser1.elements().isEmpty()) {
				return null;
			}
			for (Iterator it = ser1.elementIterator(); it.hasNext();) {
				Element ele = (Element) it.next();
				String temp = "Enabled,Device,IP,Interface,Mark,Netmask,Persistent,PersistentNetmask," +
						"Schedule,Servers,TCPPorts,UDPPorts";
				if (temp.contains(ele.getName()))
					ser1.remove(ele);
				else {
					ele.addAttribute("get", "1");
				}
			}
			String xml1 = (String) executeXml(methodName, doc1.asXML());
			Document doc2 = DocumentHelper.parseText(xml1);
			Element ser2 = doc2.getRootElement().element("Services");
			for (Iterator it1 = ser2.elementIterator(); it1.hasNext();) {
				Element ele1 = (Element) it1.next();
				if (!ele1.elements().isEmpty()) {
					for (Iterator it2 = ele1.elementIterator(); it2.hasNext();) {
						Element ele2 = (Element) it2.next();
						ele2.addAttribute("get", "1");
					}
				}
			}
			String xml2 = (String) executeXml(methodName, doc2.asXML());
			Document doc3 = DocumentHelper.parseText(xml2);
			Element ser3 = doc3.getRootElement().element("Services");
			for (Iterator it1 = ser3.elementIterator(); it1.hasNext();) {
				Element ele1 = (Element) it1.next();
				if (!ele1.elements().isEmpty()) {
					for (Iterator it2 = ele1.elementIterator(); it2.hasNext();) {
						Element ele2 = (Element) it2.next();
						String name = ele2.getName();
						if ((!"IP".equals(name))
								|| (ele2.attributes().isEmpty()))
							continue;
						Attribute attr = ele2.attribute("value");
						if ((attr != null) && (!"".equals(attr.getValue()))) {
							list.add(attr.getValue());
						}
					}
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return list;
	}

	private String getSirtualServDocumentByName(String name) throws XmlRpcException {
		name = (name.matches("^[\\d]+[\\w]*$")) ? "_" + name : name;

		Document doc = getServiceXmlDocument();
		Element services = doc.getRootElement().element("Services");
		Element http1 = services.addElement(name);
		http1.addAttribute("get", "1");
		String xml = (String) executeXml(methodName, doc.asXML());
		// 
		try {
			Document doc1 = DocumentHelper.parseText(xml);
			Element ele1 = doc1.getRootElement().element("Services").element(
					name);
			if (ele1.elements().isEmpty()) {
				return null;
			}
			for (Iterator it = ele1.elementIterator(); it.hasNext();) {
				Element ele = (Element) it.next();
				ele.addAttribute("get", "1");
			}

			// 
			String xml1 = (String) executeXml(methodName, doc1.asXML());
			// 
			Document doc2 = DocumentHelper.parseText(xml1);
			for (Iterator it = doc2.getRootElement().element("Services")
					.element(name).elementIterator(); it.hasNext();) {
				Element ele = (Element) it.next();
				// 
				if ("Servers".equals(ele.getName())) {
					// 
					if (!ele.elements().isEmpty())
						for (Iterator it2 = ele.elementIterator(); it2
								.hasNext();) {
							Element ele2 = (Element) it2.next();
							ele2.addAttribute("get", "1");
						}
					else {
						return xml1;
					}
				}
			}

			String xml2 = (String) executeXml(methodName, doc2.asXML());
			// 
			// 
			Document doc3 = DocumentHelper.parseText(xml2);
			for (Iterator it = doc3.getRootElement().element("Services")
					.element(name).elementIterator(); it.hasNext();) {
				Element ele = (Element) it.next();
				if ((!"Servers".equals(ele.getName()))
						|| (ele.elements().isEmpty()))
					continue;
				for (Iterator it2 = ele.elementIterator(); it2.hasNext();) {
					Element ele2 = (Element) it2.next();
					for (Iterator it3 = ele2.elementIterator(); it3.hasNext();) {
						((Element) it3.next()).addAttribute("get", "1");
					}
				}
			}

			String xml3 = (String) executeXml(methodName, doc3.asXML());
			return xml3;
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getSirtualServDocument() throws XmlRpcException {
		Document doc = getServiceXmlDocument();
		doc.getRootElement().element("Services").addAttribute("get", "1");
		String xml = (String) executeXml(methodName, doc.asXML());
		if (xml == null)
			return null;
		try {
			Document doc1 = DocumentHelper.parseText(xml);
			Element ser1 = doc1.getRootElement().element("Services");
			if (ser1.elements().isEmpty()) {
				return xml;
			}
			for (Iterator it = ser1.elementIterator(); it.hasNext();) {
				Element ele = (Element) it.next();
				String temp = "Enabled,Device,IP,Interface,Mark,Netmask,Persistent,PersistentNetmask," +
						"Schedule,Servers,TCPPorts,UDPPorts";

				if (temp.contains(ele.getName()))
					ser1.remove(ele);
				else {
					ele.addAttribute("get", "1");
				}
			}
			String xml1 = (String) executeXml(methodName, doc1.asXML());
			Document doc2 = DocumentHelper.parseText(xml1);
			Element ser2 = doc2.getRootElement().element("Services");
			for (Iterator it1 = ser2.elementIterator(); it1.hasNext();) {
				Element ele1 = (Element) it1.next();
				if (!ele1.elements().isEmpty()) {
					for (Iterator it2 = ele1.elementIterator(); it2.hasNext();) {
						Element ele2 = (Element) it2.next();
						ele2.addAttribute("get", "1");
					}
				}
			}
			String xml2 = (String) executeXml(methodName, doc2.asXML());
			Document doc3 = DocumentHelper.parseText(xml2);
			for (Iterator it1 = doc3.getRootElement().element("Services").elementIterator(); it1.hasNext();) {
				Element ele1 = (Element) it1.next();
				for (Iterator it2 = ele1.elementIterator(); it2.hasNext();) {
					Element ele2 = (Element) it2.next();
					if ((!ele2.getName().equals("Servers"))
							|| (ele2.elements().isEmpty()))
						continue;
					for (Iterator it3 = ele2.elementIterator(); it3.hasNext();) {
						Element ele3 = (Element) it3.next();
						ele3.addAttribute("get", "1");
					}
				}
			}

			String xml3 = (String) executeXml(methodName, doc3.asXML());
			Document doc4 = DocumentHelper.parseText(xml3);
			for (Iterator it1 = doc4.getRootElement().element("Services").elementIterator(); it1.hasNext();) {
				Element ele1 = (Element) it1.next();
				for (Iterator it2 = ele1.elementIterator(); it2.hasNext();) {
					Element ele2 = (Element) it2.next();
					if ((!ele2.getName().equals("Servers")) || (ele2.elements().isEmpty()))
						continue;
					for (Iterator it3 = ele2.elementIterator(); it3.hasNext();) {
						Element ele3 = (Element) it3.next();
						if (!ele3.elements().isEmpty()) {
							for (Iterator it4 = ele3.elementIterator(); it4.hasNext();) {
								Element ele4 = (Element) it4.next();
								ele4.addAttribute("get", "1");
							}
						}
					}
				}
			}
			String xml4 = (String) executeXml(methodName, doc4.asXML());
			return xml4;
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<LoadSirtualServ> getSirtualServInfo() {
		List sirtualServList = new ArrayList();
		
		LoadSirtualServ ser = new LoadSirtualServ();				
		try {			
			Hashtable vsTemp = new Hashtable();
			vsTemp.put("All", true);
			Object[] result = (Object[])executeXml("VirtualService.Get",vsTemp);
			
			if (result != null) {
				for (int i = 0; i < result.length; i++) {
					Map res = (Map)result[i];
					Integer id = (Integer)res.get("ID");
					Integer mark = (Integer)res.get("Mark");
					String descrip = (String)res.get("Description");
					String name = (String)res.get("Name");
					Boolean enabled = (Boolean)res.get("Enabled");
					Object[] ips = (Object[])res.get("IP");
					Object[] tcpPorts = (Object[])res.get("TcpPorts");
					Object[] udpPorts = (Object[])res.get("UdpPorts");
					Integer schedule = (Integer)res.get("Schedule");
					Boolean persistent = (Boolean)res.get("Persistent");
					Integer persistentTimeout = (Integer)res.get("PersistentTimeout");
					String persistentNetmask = (String)res.get("PersistentNetmask");
					Object monitorObj = (Object)res.get("Monitor");
if(Struts2Utils.IS_HA_ENABLE == true){					
					Integer ha = (Integer)res.get("HA");
					ser.setHaType(ha.toString());	
					Boolean haStatus = (Boolean)res.get("HAStatus");
}else{
					ser.setHaType("0");
}
					Object[] servers = (Object[])res.get("Servers");
					
					
					/* //System.out.println(id);
					//System.out.println(mark);
					//System.out.println(descrip);
					//System.out.println(name);
					//System.out.println(enabled);
					//System.out.println(ips);
					//System.out.println(tcpPorts);
					//System.out.println(udpPorts);
					//System.out.println(schedule);
					//System.out.println(persistent);
					//System.out.println(persistentTimeout);
					//System.out.println(persistentNetmask);
					//System.out.println(monitorObj);
					//System.out.println(ha);
					//System.out.println(haStatus);
					//System.out.println(servers); */
									
					ser.setVsId(id.toString());
					ser.setService(name);	
					
					ser.setStatus(enabled.toString());
					
					if ( ips != null && ips.length !=0 ) {
						Map ipMap = (Map)ips[0];
						String ip = (String)ipMap.get("IP");
						String dev = (String)ipMap.get("Dev");
						Boolean status = (Boolean)ipMap.get("Status");
						
						/* //System.out.println(ips[0]);
						//System.out.println(ip);
						//System.out.println(dev);
						//System.out.println(status); */
						
						ser.setVipMark(ip);
						ser.setInterfaces(dev);					
					}
					
					ser.setIdMark(mark.toString());
					if ( persistent == false ) {
						ser.setPersistentTimeout("0");
					} else {
						ser.setPersistentTimeout(persistentTimeout.toString());	
					}
					ser.setPersistentNetmask(persistentNetmask);	
					ser.setScheduling(schedule.toString());
	
					Integer tcpMin = null;
					StringBuffer tcpbuf = new StringBuffer();
					StringBuffer tcpbufLimit = new StringBuffer();
					if ( tcpPorts != null && tcpPorts.length !=0 ) {
						for (int j = 0; j < tcpPorts.length; j++) {
							Map tcpPort = (Map)tcpPorts[j];
							tcpMin = (Integer)tcpPort.get("Min");
							Integer max = (Integer)tcpPort.get("Max");
							
							/* //System.out.println(tcpPorts[j]);
							//System.out.println(tcpMin);
							//System.out.println(max); */
							
							if ( tcpMin != null ) {
								if ( tcpMin.equals(max) ) {
									tcpbuf.append(tcpMin.toString());
									tcpbuf.append(",");
								} else {
									tcpbuf.append(tcpMin.toString());
									tcpbuf.append("-");
									tcpbuf.append(max.toString());
									tcpbuf.append(",");
								}
							}											
						}
					}
					if ( tcpPorts != null && tcpPorts.length >=3 ) {
						for (int j = 0; j < 2; j++) {
							Map tcpPort = (Map)tcpPorts[j];
							tcpMin = (Integer)tcpPort.get("Min");
							Integer max = (Integer)tcpPort.get("Max");														
							
							if ( tcpMin != null ) {
								if ( tcpMin.equals(max) ) {
									tcpbufLimit.append(tcpMin.toString());
									tcpbufLimit.append(",");
								} else {
									tcpbufLimit.append(tcpMin.toString());
									tcpbufLimit.append("-");
									tcpbufLimit.append(max.toString());
									tcpbufLimit.append(",");
								}
							}											
						}
					}
					
					Integer udpMin = null;
					StringBuffer udpbuf = new StringBuffer();
					StringBuffer udpbufLimit = new StringBuffer();
					if ( udpPorts != null && udpPorts.length !=0 ) {
						for (int j = 0; j < udpPorts.length; j++) {
							Map udpPort = (Map)udpPorts[j];
							udpMin = (Integer)udpPort.get("Min");
							Integer max = (Integer)udpPort.get("Max");
							
							/* //System.out.println(udpPorts[j]);
							//System.out.println(udpMin);
							//System.out.println(max); */
							
							if ( udpMin != null ) {
								if ( udpMin.equals(max) ) {
									udpbuf.append(udpMin.toString());
									udpbuf.append(",");
								} else {
									udpbuf.append(udpMin.toString());
									udpbuf.append("-");
									udpbuf.append(max.toString());
									udpbuf.append(",");
								}
							}							
						}
					}					
					if ( udpPorts != null && udpPorts.length >= 3 ) {
						for (int j = 0; j < 2; j++) {
							Map udpPort = (Map)udpPorts[j];
							udpMin = (Integer)udpPort.get("Min");
							Integer max = (Integer)udpPort.get("Max");														
							
							if ( udpMin != null ) {
								if ( udpMin.equals(max) ) {
									udpbufLimit.append(udpMin.toString());
									udpbufLimit.append(",");
								} else {
									udpbufLimit.append(udpMin.toString());
									udpbufLimit.append("-");
									udpbufLimit.append(max.toString());
									udpbufLimit.append(",");
								}
							}							
						}
					}
					
					String tcpAndPort = new String();
					String udpAndPort = new String();
					if ( tcpPorts != null && tcpPorts.length !=0 ) {
						if (tcpPorts.length < 3) {
							tcpAndPort = "TCP: " + tcpbuf.substring(0, tcpbuf.length() - 1);
						} else {
							tcpAndPort = "TCP: " + tcpbufLimit.substring(0, tcpbufLimit.length() - 1) + " ..." ;
						}
					}
					if ( udpPorts != null && udpPorts.length !=0 ) {
						if (udpPorts.length < 3) {
							udpAndPort = "UDP: " + udpbuf.substring(0, udpbuf.length() - 1);
						} else {
							udpAndPort = "UDP: " + udpbufLimit.substring(0, udpbufLimit.length() - 1) + " ...";
						}
					}
					
					if ( tcpMin == null) {
						if (udpMin != null) {							
							ser.setProtocolAndPort(udpAndPort);
							//ser.setUdpPorts(udpbuf.substring(0, udpbuf.length() - 1));
						}
					} else if (udpMin == null) {						
						ser.setProtocolAndPort(tcpAndPort);
						//ser.setTcpPorts(tcpbuf.substring(0, tcpbuf.length() - 1));
					} else {						
						ser.setProtocolAndPort( tcpAndPort + " " + udpAndPort );
						//ser.setTcpPorts(tcpbuf.substring(0, tcpbuf.length() - 1));
						//ser.setUdpPorts(udpbuf.substring(0, udpbuf.length() - 1));
					}
if(Struts2Utils.IS_SOFT_VERSION == false){	
					Object trafficObj = (Object)res.get("Traffic");
					Map trafficMap = (Map)trafficObj;
					Integer up = (Integer)trafficMap.get("Up");
					Integer down = (Integer)trafficMap.get("Down");
					ser.setTrafficUp(up.toString());
					ser.setTrafficDown(down.toString());
}else{
	ser.setTrafficUp("100");
	ser.setTrafficDown("200");
}					
	
					List realIp = new ArrayList();					
					if (servers != null) {
						for (int j = 0; j < servers.length; j++) {
							Map server = (Map)servers[j];
							String ip = (String)server.get("IP");
							
							if ( ip == "" )
								continue;
							realIp.add(ip);
						}
					}
					ser.setRealIp(realIp);
					sirtualServList.add(ser);
					ser = new LoadSirtualServ();
				}
			}						
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return sirtualServList;

		/* List<String> names = getSirtualServNames();
		if ((names != null) && (names.size() > 0)) {
			for (String name : names) {
				LoadSirtualServ ser = getSirtualServByName(name);
				sirtualServList.add(ser);
			}
		}
		return sirtualServList; */
	}
	
	public LoadSirtualServ getSirtualServById(String id) {
		LoadSirtualServ ser = new LoadSirtualServ();				
		try {
			
			Hashtable vsTemp = new Hashtable();
			vsTemp.put("Start", NumberUtils.toInt(id));
			vsTemp.put("Count", 1);
			Object[] result = (Object[])executeXml("VirtualService.Get",vsTemp);
			
			if (result != null) {
				Map res = (Map)result[0];
				Integer mark = (Integer)res.get("Mark");
				String descrip = (String)res.get("Description");
				String name = (String)res.get("Name");
				Boolean enabled = (Boolean)res.get("Enabled");
				Object[] ips = (Object[])res.get("IP");
				Object[] tcpPorts = (Object[])res.get("TcpPorts");
				Object[] udpPorts = (Object[])res.get("UdpPorts");
				Integer schedule = (Integer)res.get("Schedule");
				Boolean persistent = (Boolean)res.get("Persistent");
				Integer persistentTimeout = (Integer)res.get("PersistentTimeout");
				String persistentNetmask = (String)res.get("PersistentNetmask");
				Object monitorObj = (Object)res.get("Monitor");		
				Object[] servers = (Object[])res.get("Servers");
				
if(Struts2Utils.IS_HA_ENABLE == true){				
				Integer ha = (Integer)res.get("HA");
				Boolean haStatus = (Boolean)res.get("HAStatus");
				ser.setHaType(ha.toString());
}else{
	ser.setHaType("0");
}
				/* //System.out.println(id);
				//System.out.println(mark);
				//System.out.println(descrip);
				//System.out.println(name);
				//System.out.println(enabled);
				//System.out.println(ips);
				//System.out.println(tcpPorts);
				//System.out.println(udpPorts);
				//System.out.println(schedule);
				//System.out.println(persistent);
				//System.out.println(persistentTimeout);
				//System.out.println(persistentNetmask);
				//System.out.println(monitorObj);
				//System.out.println(ha);
				//System.out.println(haStatus);
				//System.out.println(servers); */
								
				ser.setVsId(id);
				ser.setService(name);
				
				ser.setStatus(enabled.toString());
				
				if ( ips != null && ips.length !=0 ) {
					Map ipMap = (Map)ips[0];
					String ip = (String)ipMap.get("IP");
					String dev = (String)ipMap.get("Dev");
					Boolean status = (Boolean)ipMap.get("Status");
					
					/* //System.out.println(ip);
					//System.out.println(dev);
					//System.out.println(status); */
					
					ser.setVipMark(ip);
					ser.setInterfaces(dev);					
				}
				
				ser.setIdMark(mark.toString());				
				if ( persistent == false ) {
					ser.setPersistentTimeout("0");
				} else {
					ser.setPersistentTimeout(persistentTimeout.toString());	
				}
				ser.setPersistentNetmask(persistentNetmask);
				ser.setScheduling(schedule.toString());

				Integer tcpMin = null;
				StringBuffer tcpbuf = new StringBuffer();
				if ( tcpPorts != null && tcpPorts.length !=0 ) {
					for (int j = 0; j < tcpPorts.length; j++) {
						Map tcpPort = (Map)tcpPorts[j];
						tcpMin = (Integer)tcpPort.get("Min");
						Integer max = (Integer)tcpPort.get("Max");
						
						/* //System.out.println(tcpPorts[j]);
						//System.out.println(tcpMin);
						//System.out.println(max); */
						
						if ( tcpMin != null ) {
							if ( tcpMin.equals(max) ) {
								tcpbuf.append(tcpMin.toString());
								tcpbuf.append(",");
							} else {
								tcpbuf.append(tcpMin.toString());
								tcpbuf.append("-");
								tcpbuf.append(max.toString());
								tcpbuf.append(",");
							}
						}											
					}
				}
				
				Integer udpMin = null;
				StringBuffer udpbuf = new StringBuffer();
				if ( udpPorts != null && udpPorts.length !=0 ) {
					for (int j = 0; j < udpPorts.length; j++) {
						Map udpPort = (Map)udpPorts[j];
						udpMin = (Integer)udpPort.get("Min");
						Integer max = (Integer)udpPort.get("Max");
						
						/* //System.out.println(udpPorts[j]);
						//System.out.println(udpMin);
						//System.out.println(max); */
						
						if ( udpMin != null ) {
							if ( udpMin.equals(max) ) {
								udpbuf.append(udpMin.toString());
								udpbuf.append(",");
							} else {
								udpbuf.append(udpMin.toString());
								udpbuf.append("-");
								udpbuf.append(max.toString());
								udpbuf.append(",");
							}
						}
					}
				}
																
				if ( tcpMin == null) {
					if (udpMin != null) {						
						//ser.setProtocolAndPort( "UDP:" + udpbuf.substring(0, udpbuf.length() - 1) );
						ser.setUdpPorts(udpbuf.substring(0, udpbuf.length() - 1));
					}
				} else if (udpMin == null) {					
					ser.setTcpPorts(tcpbuf.substring(0, tcpbuf.length() - 1));
				} else {					
					ser.setTcpPorts(tcpbuf.substring(0, tcpbuf.length() - 1));
					ser.setUdpPorts(udpbuf.substring(0, udpbuf.length() - 1));
				}
				
if(Struts2Utils.IS_SOFT_VERSION == false){	
				Object trafficObj = (Object)res.get("Traffic");
	
				Map trafficMap = (Map)trafficObj;
				Integer up = (Integer)trafficMap.get("Up");
				Integer down = (Integer)trafficMap.get("Down");
				ser.setTrafficUp(up.toString());
				ser.setTrafficDown(down.toString());
}else{
	ser.setTrafficUp("100");
	ser.setTrafficDown("200");
}
				List realIp = new ArrayList();				
				if (servers != null) {
					for (int i = 0; i < servers.length; i++) {
						Map server = (Map)servers[i];
						String ip = (String)server.get("IP");
						
						if ( ip == "" )
							continue;
						realIp.add(ip);
					}
				}
				ser.setRealIp(realIp);								
			}			
			return ser;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public LoadSirtualServ getSirtualServByName(String name) {
		LoadSirtualServ ser = new LoadSirtualServ();
		//String xml = getSirtualServDocumentByName(name);
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
					//String name = (String)res.get("Name");
					Boolean enabled = (Boolean)res.get("Enabled");
					Object[] ips = (Object[])res.get("IP");
					Object[] tcpPorts = (Object[])res.get("TcpPorts");
					Object[] udpPorts = (Object[])res.get("UdpPorts");
					Integer schedule = (Integer)res.get("Schedule");
					Boolean persistent = (Boolean)res.get("Persistent");
					Integer persistentTimeout = (Integer)res.get("PersistentTimeout");
					String persistentNetmask = (String)res.get("PersistentNetmask");
					Object monitorObj = (Object)res.get("Monitor");
				
					Object[] servers = (Object[])res.get("Servers");
if(Struts2Utils.IS_HA_ENABLE == true){						
					Integer ha = (Integer)res.get("HA");
					Boolean haStatus = (Boolean)res.get("HAStatus");
					ser.setHaType(ha.toString());
}else{
	ser.setHaType("0");
}
					/* //System.out.println(id);
					//System.out.println(mark);
					//System.out.println(descrip);
					//System.out.println(name);
					//System.out.println(enabled);
					//System.out.println(ips);
					//System.out.println(tcpPorts);
					//System.out.println(udpPorts);
					//System.out.println(schedule);
					//System.out.println(persistent);
					//System.out.println(persistentTimeout);
					//System.out.println(persistentNetmask);
					//System.out.println(monitorObj);
					//System.out.println(ha);
					//System.out.println(haStatus);
					//System.out.println(servers); */
									
					ser.setService(name);

					

					ser.setStatus(enabled.toString());
					
					if ( ips != null && ips.length !=0 ) {
						Map ipMap = (Map)ips[0];
						String ip = (String)ipMap.get("IP");
						String dev = (String)ipMap.get("Dev");
						Boolean status = (Boolean)ipMap.get("Status");
						
						/* //System.out.println(ip);
						//System.out.println(dev);
						//System.out.println(status); */
						
						ser.setVipMark(ip);
						ser.setInterfaces(dev);					
					}
					
					ser.setIdMark(mark.toString());

					ser.setPersistentTimeout(persistentTimeout.toString());

					ser.setPersistentNetmask(persistentNetmask);

					ser.setScheduling(schedule.toString());

					Integer tcpMin = null;
					StringBuffer tcpbuf = new StringBuffer();
					if ( tcpPorts != null && tcpPorts.length !=0 ) {
						for (int j = 0; j < tcpPorts.length; j++) {
							Map tcpPort = (Map)tcpPorts[j];
							tcpMin = (Integer)tcpPort.get("min");
							Integer max = (Integer)tcpPort.get("max");
							
							/* //System.out.println(tcpPorts[j]);
							//System.out.println(tcpMin);
							//System.out.println(max); */
							
							if ( tcpMin != null ) {
								if ( tcpMin.equals(max) ) {
									tcpbuf.append(tcpMin.toString());
									tcpbuf.append(",");
								} else {
									tcpbuf.append(tcpMin.toString());
									tcpbuf.append("-");
									tcpbuf.append(max.toString());
									tcpbuf.append(",");
								}
							}											
						}
					}
					
					Integer udpMin = null;
					StringBuffer udpbuf = new StringBuffer();
					if ( udpPorts != null && udpPorts.length !=0 ) {
						for (int j = 0; j < udpPorts.length; j++) {
							Map udpPort = (Map)udpPorts[j];
							udpMin = (Integer)udpPort.get("min");
							Integer max = (Integer)udpPort.get("max");
							
							/* //System.out.println(udpPorts[j]);
							//System.out.println(udpMin);
							//System.out.println(max); */
							
							if ( udpMin != null ) {
								if ( udpMin.equals(max) ) {
									udpbuf.append(udpMin.toString());
									udpbuf.append(",");
								} else {
									udpbuf.append(udpMin.toString());
									udpbuf.append("-");
									udpbuf.append(max.toString());
									udpbuf.append(",");
								}
							}
						}
					}
																	
					if ( tcpMin == null) {
						if (udpMin != null) {							
							ser.setProtocolAndPort( "UDP:" + udpbuf.substring(0, udpbuf.length() - 1) );
							ser.setUdpPorts(udpbuf.substring(0, udpbuf.length() - 1));
						}
					} else if (udpMin == null) {						
						ser.setProtocolAndPort( "TCP:" + tcpbuf.substring(0, tcpbuf.length() - 1) );
						ser.setTcpPorts(tcpbuf.substring(0, tcpbuf.length() - 1));
					} else {						
						ser.setProtocolAndPort( "TCP:" + tcpbuf.substring(0, tcpbuf.length() - 1) + " "
								+ "UDP:" + udpbuf.substring(0, udpbuf.length() - 1) );
						ser.setTcpPorts(tcpbuf.substring(0, tcpbuf.length() - 1));
						ser.setUdpPorts(udpbuf.substring(0, udpbuf.length() - 1));
					}

					List realIp = new ArrayList();
					
					if (servers != null) {
						for (int i = 0; i < servers.length; i++) {
							Map server = (Map)servers[i];
							String ip = (String)server.get("IP");
							
							if ( ip == "" )
								continue;
							realIp.add(ip);
						}
					}
					ser.setRealIp(realIp);								
				}
			}												
			return ser;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean setSirtualServStatus(String id, String status) {
		boolean flag = false;
		try {
			Hashtable enableTemp = new Hashtable();
			enableTemp.put("ID", NumberUtils.toInt(id));
			enableTemp.put("Enabled", status.equals("true") ? true : false);
			Boolean enableResult = (Boolean)executeXml("VirtualService.Enabled",enableTemp);
			
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}
	
	public String setSedundantStatus(String enabled) {		
		Hashtable enableTemp = new Hashtable();
		enableTemp.put("Enabled", enabled.equals("true") ? true : false);
		
		String flagStr = "false";	
		try {
			Boolean enableResult = (Boolean)executeXml("HA.Enabled",enableTemp);
			//System.out.println(enableResult);
			if ( enableResult != null && enableResult == true ) {
				flagStr = "true";
			}
		} catch (XmlRpcException e) {			
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}

	public String updateSirtualServ(LoadSirtualServ ser) {

		String flagStr = "false";
		
		String id = ser.getVsId();
		String service = ser.getService();
		//String service = (s.matches("^[\\d]+[\\w]*$")) ? "_" + s : s;			
		Hashtable descripTemp = new Hashtable();
		descripTemp.put("ID", NumberUtils.toInt(id));
		descripTemp.put("Name", service);
				
		//String hostname = (String)executeXml("Hostname.Get");
		Hashtable haTemp = new Hashtable();
		haTemp.put("ID", NumberUtils.toInt(id));	
if(Struts2Utils.IS_HA_ENABLE == true){
			
		haTemp.put("HA", NumberUtils.toInt(ser.getHaType()));
}					
		Hashtable addressTemp = new Hashtable();
		addressTemp.put("ID", NumberUtils.toInt(id));
		Object[] ipObj = new Object[1];
		if ( StringUtils.isNotEmpty(ser.getVipMark()) ) {			
			Hashtable ipTemp = new Hashtable();		
			ipTemp.put("IP", ser.getVipMark());
			if (StringUtils.isNotEmpty(ser.getInterfaces())) {
				ipTemp.put("Dev", ser.getInterfaces());
			}
			ipObj[0] = ipTemp;
		}
		addressTemp.put("IP", ipObj);			
			
		String[] tcpports = new String[0];
		String tcp = ser.getTcpPorts();
		if (StringUtils.contains(tcp, ",")) {
			tcpports = tcp.split(",");
		}
		if (StringUtils.contains(tcp, "，")) {
			tcpports = tcp.split("，");
		}			
		Hashtable tcpPortTemp = new Hashtable();
		if ((tcpports != null) && (tcpports.length > 0)) {
			Object[] tcpPortsObj = new Object[tcpports.length];
			int index = 0;
			for (String p : tcpports) {
				if (StringUtils.contains(p, "-")) {
					String[] minAndMax = new String[2];
					minAndMax = p.split("-");
					tcpPortTemp.put( "Min", NumberUtils.toInt(minAndMax[0]) );
					tcpPortTemp.put( "Max", NumberUtils.toInt(minAndMax[1]) );
				} else {
					tcpPortTemp.put( "Min", NumberUtils.toInt(p) );
					tcpPortTemp.put( "Max", NumberUtils.toInt(p) );
				}
				tcpPortsObj[index] = tcpPortTemp;
				tcpPortTemp = new Hashtable();
				index++;
			}
			addressTemp.put("TcpPorts", tcpPortsObj);
		} else if (!tcp.equals("")) {
			Object[] tcpPortsObj = new Object[1];
			if (StringUtils.contains(tcp, "-")) {
				String[] minAndMax = new String[2];
				minAndMax = tcp.split("-");
				tcpPortTemp.put( "Min", NumberUtils.toInt(minAndMax[0]) );
				tcpPortTemp.put( "Max", NumberUtils.toInt(minAndMax[1]) );
			} else {
				tcpPortTemp.put( "Min", NumberUtils.toInt(tcp) );
				tcpPortTemp.put( "Max", NumberUtils.toInt(tcp) );							
			}
			tcpPortsObj[0] = tcpPortTemp;
			addressTemp.put("TcpPorts", tcpPortsObj);
		}

		String udp = ser.getUdpPorts();			
		String[] udpports = new String[0];
		if (StringUtils.contains(udp, ",")) {
			udpports = udp.split(",");
		}
		if (StringUtils.contains(udp, "，")) {
			udpports = udp.split("，");
		}
		Hashtable udpPortTemp = new Hashtable();
		if ((udpports != null) && (udpports.length > 0)) {
			Object[] udpPortsObj = new Object[udpports.length];
			int index = 0;
			for (String p : udpports) {
				if (StringUtils.contains(p, "-")) {
					String[] minAndMax = new String[2];
					minAndMax = p.split("-");
					udpPortTemp.put( "Min", NumberUtils.toInt(minAndMax[0]) );
					udpPortTemp.put( "Max", NumberUtils.toInt(minAndMax[1]) );
				} else {
					udpPortTemp.put( "Min", NumberUtils.toInt(p) );
					udpPortTemp.put( "Max", NumberUtils.toInt(p) );												
				}
				udpPortsObj[index] = udpPortTemp;
				udpPortTemp = new Hashtable();
				index++;
			}
			addressTemp.put("UdpPorts", udpPortsObj);
		} else if ( !udp.equals("") ) {
			Object[] udpPortsObj = new Object[1];
			if (StringUtils.contains(udp, "-")) {
				String[] minAndMax = new String[2];
				minAndMax = udp.split("-");
				udpPortTemp.put( "Min", NumberUtils.toInt(minAndMax[0]) );
				udpPortTemp.put( "Max", NumberUtils.toInt(minAndMax[1]) );
			} else {
				udpPortTemp.put( "Min", NumberUtils.toInt(udp) );
				udpPortTemp.put( "Max", NumberUtils.toInt(udp) );					
			}
			udpPortsObj[0] = udpPortTemp;
			addressTemp.put("UdpPorts", udpPortsObj);
		}
		
		Hashtable trafTemp = new Hashtable();
		trafTemp.put("ID", NumberUtils.toInt(id));
if(Struts2Utils.IS_SOFT_VERSION == false){						
	
		Object trafficObj = new Object();			
		Hashtable trafficTemp = new Hashtable();
		if ( StringUtils.isNotEmpty(ser.getTrafficUp()) ) {
			trafficTemp.put( "Up", NumberUtils.toInt(ser.getTrafficUp()) );
		}
		if ( StringUtils.isNotEmpty(ser.getTrafficDown()) ) {
			trafficTemp.put( "Down", NumberUtils.toInt(ser.getTrafficDown()) );
		}
		trafficObj = trafficTemp;
		trafTemp.put("Traffic", trafficObj);
}						
		Hashtable servTemp = new Hashtable();
		servTemp.put("ID", NumberUtils.toInt(id));		
		if ( StringUtils.isNotEmpty(ser.getPersistentTimeout()) && !ser.getPersistentTimeout().equals("0") ) {
			servTemp.put( "Persistent", true );
			servTemp.put( "PersistentTimeout", NumberUtils.toInt(ser.getPersistentTimeout()) );
		} else {
			servTemp.put( "Persistent", false );
		}	
		if (StringUtils.isNotEmpty(ser.getPersistentNetmask())) {
			servTemp.put( "PersistentNetmask", ser.getPersistentNetmask() );
		}	
		if (StringUtils.isNotEmpty(ser.getScheduling())) {
			servTemp.put( "Schedule", NumberUtils.toInt(ser.getScheduling()) );			
		}
								
		try {
			Boolean descripResult = (Boolean)executeXml("VirtualService.Description",descripTemp);
if(Struts2Utils.IS_HA_ENABLE == true){
			Boolean haResult = (Boolean)executeXml("VirtualService.HA",haTemp);
}
			Boolean addressResult = (Boolean)executeXml("VirtualService.Address", addressTemp);
if(Struts2Utils.IS_SOFT_VERSION == false){		
			Boolean monitorResult = (Boolean)executeXml("VirtualService.Traffic", trafTemp);
}
			Boolean servResult = (Boolean)executeXml("VirtualService.Service", servTemp);
			
			flagStr = "true";
		} catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}

	public String updateTrueServ(LoadTrueServ ts) {						
		String service = ts.getService();		
		Hashtable setTemp = new Hashtable();			
		if (StringUtils.isNotEmpty(ts.getVsId())) {
			setTemp.put("ID", NumberUtils.toInt(ts.getVsId()));
		}
		if (StringUtils.isNotEmpty(service)) {
			setTemp.put("Name", service);
		}
			
		Object[] serverObj = new Object[1];
		Hashtable serverTemp = new Hashtable();
		if (StringUtils.isNotEmpty(ts.getTsId())) {
			serverTemp.put( "ID", NumberUtils.toInt(ts.getTsId()) );
		}			
		String serviceName = ts.getServiceName();		
		if (StringUtils.isNotEmpty(serviceName)) {
			serverTemp.put("Name", serviceName);
		}
		if (StringUtils.isNotEmpty(ts.getForward())) {
			serverTemp.put("Action", NumberUtils.toInt(ts.getForward()));
		}
		if (StringUtils.isNotEmpty(ts.getIp())) {
			serverTemp.put("IP", ts.getIp());
		}
		if (StringUtils.isNotEmpty(ts.getWeight())) {
			serverTemp.put("Weight", NumberUtils.toInt(ts.getWeight()));
		}
		if (StringUtils.isNotEmpty(ts.getStatus())) {
			serverTemp.put("Enabled", ts.getStatus().equals("true") ? true : false);				
		}			
		if (StringUtils.isNotEmpty(ts.getMapport())) {
			serverTemp.put("MapPort", NumberUtils.toInt(ts.getMapport()));
		}
		serverObj[0] = serverTemp;
		setTemp.put("Servers", serverObj);
		
		String flagStr = "false";	
		try {
			Boolean setResult = (Boolean)executeXml("VirtualService.Server.Set",setTemp);
			flagStr = "true";
		} catch (XmlRpcException e) {			
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}
	
	public LoadTrueServ getTrueServByName(String service, String name) {
		List<LoadTrueServ> list = getTrueServInfo();
		if (name.equalsIgnoreCase(""))
			return null;
		
		if ((list != null) && (!list.isEmpty())) {
			for (LoadTrueServ ts : list) {
				if ((ts.getService().equals(service)) && (ts.getServiceName().equals(name))) {
					return ts;
				}
			}
		}
		return null;
	}

	public LoadTrueServ getTrueServById(String vsId, String tsId) {
		List<LoadTrueServ> list = getTrueServInfo();
		if ((list != null) && (!list.isEmpty())) {
			for (LoadTrueServ ts : list) {
				if ((ts.getVsId().equals(vsId)) && (ts.getTsId().equals(tsId))) {
					return ts;
				}
			}
		}
		return null;
	}

	public List<LoadTrueServ> getTrueServInfo() {
		List trueServList = new ArrayList();
		
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
					Integer mark = (Integer)res.get("Mark");
					String descrip = (String)res.get("Description");
					String name = (String)res.get("Name");					
					Object[] ips = (Object[])res.get("IP");
					Object[] tcpPorts = (Object[])res.get("TcpPorts");
					Object[] udpPorts = (Object[])res.get("UdpPorts");
					Integer schedule = (Integer)res.get("Schedule");
					Object monitorObj = (Object)res.get("Monitor");
					
					Object[] servers = (Object[])res.get("Servers");
if(Struts2Utils.IS_HA_ENABLE == true){				
					Integer ha = (Integer)res.get("HA");
					Boolean haStatus = (Boolean)res.get("HAStatus");
}
					/* //System.out.println(id);
					//System.out.println(mark);
					//System.out.println(descrip);
					//System.out.println(name);					
					//System.out.println(ips);
					//System.out.println(tcpPorts);
					//System.out.println(udpPorts);
					//System.out.println(schedule);
					//System.out.println(persistent);
					//System.out.println(persistentTimeout);
					//System.out.println(persistentNetmask);
					//System.out.println(monitorObj);
					//System.out.println(ha);
					//System.out.println(haStatus);
					//System.out.println(servers); */
					
					LoadTrueServ ls = new LoadTrueServ();
										
					if (servers == null || servers.length == 0) {
						continue;
					} else {
						for (int j = 0; j < servers.length; j++) {
							Map server = (Map)servers[j];
							Integer tsId = (Integer)server.get("ID");
							String ip = (String)server.get("IP");
							String tsName = (String)server.get("Name");
							Integer action = (Integer)server.get("Action");
							Integer weight = (Integer)server.get("Weight");
							Boolean enabled = (Boolean)server.get("Enabled");
							Integer mapport = (Integer)server.get("MapPort");
														
							////System.out.println(mapport + ",,,");
							
							ls.setTsId(tsId.toString());
							ls.setServiceName(tsName);														
							ls.setForward(action.toString());
							ls.setStatus(enabled.toString());
							
							if ( ip != null && (!"".equals(ip)) ) {								
								ls.setIp(ip);
							}
							ls.setWeight(weight.toString());
							if ( mapport != null ) {
								ls.setMapport(mapport.toString());
							}
							ls.setVsId(id.toString());
							ls.setService(name);
							
							if ( ips != null && ips.length !=0 ) {
								Map ipMap = (Map)ips[0];
								String vip = (String)ipMap.get("IP");
								//String dev = (String)ipMap.get("Dev");
								//Boolean status = (Boolean)ipMap.get("Status");
								ls.setVipMark(vip);
							}
							
							Integer tcpMin = null;
							StringBuffer tcpbuf = new StringBuffer();							
							if ( tcpPorts != null && tcpPorts.length == 1 ) {								
								Map tcpPort = (Map)tcpPorts[0];
								tcpMin = (Integer)tcpPort.get("Min");
								Integer max = (Integer)tcpPort.get("Max");
									
								/* //System.out.println(tcpPorts[j]);
								//System.out.println(tcpMin);
								//System.out.println(max); */
									
								if ( tcpMin != null ) {
									if ( tcpMin.equals(max) ) {
										tcpbuf.append(tcpMin.toString());										
									} else {
										tcpbuf.append("");										
									}
								}																			
							}							
							
							Integer udpMin = null;
							StringBuffer udpbuf = new StringBuffer();							
							if ( udpPorts != null && udpPorts.length == 1 ) {								
								Map udpPort = (Map)udpPorts[0];
								udpMin = (Integer)udpPort.get("Min");
								Integer max = (Integer)udpPort.get("Max");
									
								/* //System.out.println(udpPorts[j]);
								//System.out.println(udpMin);
								//System.out.println(max); */
									
								if ( udpMin != null ) {
									if ( udpMin.equals(max) ) {
										udpbuf.append(udpMin.toString());											
									} else {
										udpbuf.append("");											
									}
								}
							}														
							
							if ( tcpMin == null) {
								if (udpMin != null) {
									ls.setVsPort(udpbuf.toString());
								}
							} else if (udpMin == null) {	
								ls.setVsPort(tcpbuf.toString());								
							} else {
								ls.setVsPort("");														
							}
							
							trueServList.add(ls);
							ls = new LoadTrueServ();																				
						}
					}																																								
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return trueServList;
	}
	
	public boolean setTrueServStatus(String vsId, String tsId, String status) {
		boolean flag = false;
		try {			
			Hashtable enableTemp = new Hashtable();
			enableTemp.put("ID", NumberUtils.toInt(vsId));
			
			Object[] vsIdObj = new Object[1];
			Hashtable tsIdTemp = new Hashtable();
			tsIdTemp.put("ID", NumberUtils.toInt(tsId));
			tsIdTemp.put("Enabled", status.equals("true") ? true : false);
			vsIdObj[0] = tsIdTemp;
			
			enableTemp.put("Servers", vsIdObj);
			Boolean enableResult = (Boolean)executeXml("VirtualService.Server.Enabled",enableTemp);									
						
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	public List<String> getTrueServIps() {
		List list = new ArrayList();
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
					Object[] ips = (Object[])res.get("IP");					
					Object[] servers = (Object[])res.get("Servers");
										
					if (servers == null || servers.length == 0) {
						continue;
					} else {
						for (int j = 0; j < servers.length; j++) {
							Map server = (Map)servers[j];
							Integer tsId = (Integer)server.get("ID");
							String ip = (String)server.get("IP");
							String tsName = (String)server.get("Name");
							
							if (ip==null || ip.equals(""))
								continue;							
							boolean equal = false;
							Iterator it3 = list.iterator();
							while (it3.hasNext()) {
								String temp = (String)it3.next();
								if(StringUtils.equals(ip, temp)){
									equal = true;
								}
							}
							if(equal == false){
								list.add(ip);
							}
						}
					}
				}
			}						
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String setSirtualServ(LoadSirtualServ ser) {
		String flagStr = "false";
		String service = ser.getService();
		//String service = (s.matches("^[\\d]+[\\w]*$")) ? "_" + s : s;	
		Hashtable addTemp = new Hashtable();
		if (StringUtils.isNotEmpty(ser.getService())) {
			addTemp.put("Name", service);
		}
			
		try {
			Boolean addResult = (Boolean)executeXml("VirtualService.Add",addTemp);
			//System.out.println(addResult);
			
			Integer id = (Integer)executeXml("VirtualService.GetCount");
			//String hostname = (String)executeXml("Hostname.Get");
if(Struts2Utils.IS_HA_ENABLE == true){
			Hashtable haTemp = new Hashtable();
			haTemp.put("ID",  id-1);
		
				haTemp.put("HA", NumberUtils.toInt(ser.getHaType()));

			Boolean haResult = (Boolean)executeXml("VirtualService.HA",haTemp);
			////System.out.println(haResult);
}		
			Hashtable addressTemp = new Hashtable();
			addressTemp.put("ID",  id-1);
			Object[] ipObj = new Object[1];
			if ( StringUtils.isNotEmpty(ser.getVipMark()) ) {			
				Hashtable ipTemp = new Hashtable();		
				ipTemp.put("IP", ser.getVipMark());
				if (StringUtils.isNotEmpty(ser.getInterfaces())) {
					ipTemp.put("Dev", ser.getInterfaces());
				}
				ipObj[0] = ipTemp;
			}
			addressTemp.put("IP", ipObj);
					
			if (StringUtils.isNotEmpty(ser.getTcpPorts())) {
				String tcpPorts = ser.getTcpPorts();
				
				Hashtable tcpPortTemp = new Hashtable();
				if ((StringUtils.contains(tcpPorts, ",")) || (StringUtils.contains(tcpPorts, "，"))) {
					String[] ports = null;
					if (StringUtils.contains(tcpPorts, ","))
						ports = tcpPorts.split(",");
					else if (StringUtils.contains(tcpPorts, "，")) {
						ports = tcpPorts.split("，");
					}
					////System.out.println(tcpPorts + "..");
					////System.out.println(ports + "..");
					
					Object[] tcpPortsObj = new Object[ports.length];
					int index = 0;
					for (String str : ports){
						if (StringUtils.contains(str, "-")) {
							String[] minAndMax = new String[2];
							minAndMax = str.split("-");
							tcpPortTemp.put( "Min", NumberUtils.toInt(minAndMax[0]) );
							tcpPortTemp.put( "Max", NumberUtils.toInt(minAndMax[1]) );
							
						} else {
							tcpPortTemp.put( "Min", NumberUtils.toInt(str) );
							tcpPortTemp.put( "Max", NumberUtils.toInt(str) );						
						}
						tcpPortsObj[index] = tcpPortTemp;
						tcpPortTemp = new Hashtable();
						index++;
					}
					addressTemp.put("TcpPorts", tcpPortsObj);
				}
				else {
					Object[] tcpPortsObj = new Object[1];
					if (StringUtils.contains(tcpPorts, "-")) {
						String[] minAndMax = new String[2];
						minAndMax = tcpPorts.split("-");
						tcpPortTemp.put( "Min", NumberUtils.toInt(minAndMax[0]) );
						tcpPortTemp.put( "Max", NumberUtils.toInt(minAndMax[1]) );
					} else {
						tcpPortTemp.put( "Min", NumberUtils.toInt(tcpPorts) );
						tcpPortTemp.put( "Max", NumberUtils.toInt(tcpPorts) );
					}
					tcpPortsObj[0] = tcpPortTemp;
					addressTemp.put("TcpPorts", tcpPortsObj);
				}
			}
			
			if (StringUtils.isNotEmpty(ser.getUdpPorts())) {			
				String udpPorts = ser.getUdpPorts();
				
				Hashtable udpPortTemp = new Hashtable();
				if ((StringUtils.contains(udpPorts, ",")) || (StringUtils.contains(udpPorts, "，"))) {
					String[] ports = new String[0];
					if (StringUtils.contains(udpPorts, ","))
						ports = udpPorts.split(",");
					else if (StringUtils.contains(udpPorts, "，")) {
						ports = udpPorts.split("，");
					}
					
					Object[] udpPortsObj = new Object[ports.length];
					int index = 0;
					for (String str : ports){
						if (StringUtils.contains(str, "-")) {
							String[] minAndMax = new String[2];
							minAndMax = str.split("-");
							udpPortTemp.put( "Min", NumberUtils.toInt(minAndMax[0]) );
							udpPortTemp.put( "Max", NumberUtils.toInt(minAndMax[1]) );						
						} else {
							udpPortTemp.put( "Min", NumberUtils.toInt(str) );
							udpPortTemp.put( "Max", NumberUtils.toInt(str) );				
						}
						udpPortsObj[index] = udpPortTemp;
						udpPortTemp = new Hashtable();
						index++;
					}
					addressTemp.put("UdpPorts", udpPortsObj);
				}
				else {
					Object[] udpPortsObj = new Object[1];
					if (StringUtils.contains(udpPorts, "-")) {
						String[] minAndMax = new String[2];
						minAndMax = udpPorts.split("-");
						udpPortTemp.put( "Min", NumberUtils.toInt(minAndMax[0]) );
						udpPortTemp.put( "Max", NumberUtils.toInt(minAndMax[1]) );					
					} else {
						udpPortTemp.put( "Min", NumberUtils.toInt(udpPorts) );
						udpPortTemp.put( "Max", NumberUtils.toInt(udpPorts) );
					}
					udpPortsObj[0] = udpPortTemp;
					addressTemp.put("UdpPorts", udpPortsObj);
				}
			}
			Boolean addressResult = (Boolean)executeXml("VirtualService.Address", addressTemp);
			////System.out.println(addressResult);
			
if(Struts2Utils.IS_SOFT_VERSION == false){		
			Hashtable trafTemp = new Hashtable();
			trafTemp.put("ID", id-1);
			Object trafficObj = new Object();
			
			Hashtable trafficTemp = new Hashtable();
			if ( StringUtils.isNotEmpty(ser.getTrafficUp()) ) {
				trafficTemp.put( "Up", NumberUtils.toInt(ser.getTrafficUp()) );
			}
			if ( StringUtils.isNotEmpty(ser.getTrafficDown()) ) {
				trafficTemp.put( "Down", NumberUtils.toInt(ser.getTrafficDown()) );
			}
			trafficObj = trafficTemp;
			trafTemp.put("Traffic", trafficObj);
			Boolean monitorResult = (Boolean)executeXml("VirtualService.Traffic", trafTemp);
}			
			Hashtable servTemp = new Hashtable();
			servTemp.put("ID",  id-1);
			if ( StringUtils.isNotEmpty(ser.getPersistentTimeout()) && !ser.getPersistentTimeout().equals("0") ) {
				servTemp.put( "Persistent", true );
				servTemp.put( "PersistentTimeout", NumberUtils.toInt(ser.getPersistentTimeout()) );
			} else {
				servTemp.put( "Persistent", false );
			}
	
			if (StringUtils.isNotEmpty(ser.getPersistentNetmask())) {
				servTemp.put( "PersistentNetmask", ser.getPersistentNetmask() );
			}
	
			if (StringUtils.isNotEmpty(ser.getScheduling())) {
				servTemp.put( "Schedule", NumberUtils.toInt(ser.getScheduling()) );			
			}
			Boolean servResult = (Boolean)executeXml("VirtualService.Service", servTemp);
			////System.out.println(servResult);
			flagStr = "true";
		} catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}

	public String setTrueServ(LoadTrueServ ser) {
		String serviceName = ser.getServiceName();		
		Hashtable addTemp = new Hashtable();			
		if (StringUtils.isNotEmpty(ser.getVsId())) {
			addTemp.put("ID", NumberUtils.toInt(ser.getVsId()));
		}
		if (StringUtils.isNotEmpty(ser.getService())) {
			addTemp.put("Name", ser.getService());
		}
			
		Object[] serverObj = new Object[1];
		Hashtable serverTemp = new Hashtable();			
		if (StringUtils.isNotEmpty(serviceName)) {
			serverTemp.put("Name", serviceName);
		}
		if (StringUtils.isNotEmpty(ser.getForward())) {
			serverTemp.put("Action", NumberUtils.toInt(ser.getForward()));
		}
		if (StringUtils.isNotEmpty(ser.getIp())) {
			serverTemp.put("IP", ser.getIp());			
		}
		if (StringUtils.isNotEmpty(ser.getWeight())) {
			serverTemp.put("Weight", NumberUtils.toInt(ser.getWeight()));
		}
		if (StringUtils.isNotEmpty(ser.getStatus())) {
			serverTemp.put("Enabled", ser.getStatus().equals("true") ? true : false);				
		}
		if (StringUtils.isNotEmpty(ser.getMapport())) {
			serverTemp.put("MapPort", NumberUtils.toInt(ser.getMapport()));
		}
		serverObj[0] = serverTemp;
		addTemp.put("Servers", serverObj);
		
		String flagStr = "false";
		try {
			Boolean addResult = (Boolean)executeXml("VirtualService.Server.Add",addTemp);
			//System.out.println(addResult);
			flagStr = "true";
		} catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}

	private Document getServiceXmlDocument() {
		String xmlName = "service.xml";
		Document doc = getDocumentByInputStream(xmlName);
		return doc;
	}

	public List<User> getUserList() {
		List ulist = new ArrayList();
		try {
			String xmlName = "users.xml";
			Document doc = getDocumentByReader(xmlName);
			Node root = doc.selectSingleNode("//Root");
			List nodeList = null;

			User user = null;

			nodeList = root.selectNodes("//User");

			if ((nodeList != null) && (nodeList.size() > 0)) {
				for (Iterator it = nodeList.iterator(); it.hasNext();) {
					Element e = (Element) it.next();
					user = new User();
					user.setId(e.elementText("Id"));
					user.setLoginName(e.elementText("LoginName"));
					user.setName(e.elementText("UserName"));
					user.setEmail(e.elementText("Email"));
					user.setPassword(e.elementText("PassWord"));
					user.setAuth(e.elementText("Auth"));
					user.setGuide(e.elementText("Guide"));
					ulist.add(user);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return ulist;
	}

	public User getUser(String Id) {
		if (StringUtils.isEmpty(Id)) {
			return null;
		}
		User user = null;
		try {
			String xmlName = "users.xml";
			Document doc = getDocumentByReader(xmlName);
			Node root = doc.selectSingleNode("//Root");
			List nodeList = null;

			nodeList = root.selectNodes("//User[Id='" + Id + "']");

			if ((nodeList != null) && (nodeList.size() > 0)) {
				Element e = (Element) nodeList.get(0);
				user = new User();
				user.setId(e.elementText("Id"));
				user.setLoginName(e.elementText("LoginName"));
				user.setName(e.elementText("UserName"));
				user.setEmail(e.elementText("Email"));
				user.setPassword(e.elementText("PassWord"));
				user.setAuth(e.elementText("Auth"));
				user.setGuide(e.elementText("Guide"));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return user;
	}

	public User getUser(String param, String val) {
		if ((StringUtils.isEmpty(param)) || (StringUtils.isEmpty(val))) {
			return null;
		}
		User user = null;
		try {
			String xmlName = "users.xml";
			Document doc = getDocumentByReader(xmlName);
			Node root = doc.selectSingleNode("//Root");
			List nodeList = null;

			nodeList = root.selectNodes("//User[" + param + "='" + val + "']");

			if ((nodeList != null) && (nodeList.size() > 0)) {
				Element e = (Element) nodeList.get(0);
				user = new User();
				user.setId(e.elementText("Id"));
				user.setLoginName(e.elementText("LoginName"));
				user.setName(e.elementText("UserName"));
				user.setEmail(e.elementText("Email"));
				user.setPassword(e.elementText("PassWord"));
				user.setAuth(e.elementText("Auth"));
				user.setGuide(e.elementText("Guide"));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return user;
	}

	public boolean updateUserInfo(String Id, User user) throws IOException {
		if ((user == null) || (StringUtils.isEmpty(Id))) {
			// 
			return false;
		}
		boolean flag = false;
		try {
			String xmlName = "users.xml";

			Document doc = getDocumentByReader(xmlName);

			Node root = doc.selectSingleNode("//Root");
			List nodeList = null;

			nodeList = root.selectNodes("//User[Id='" + Id + "']");

			if ((nodeList != null) && (nodeList.size() > 0)) {
				Element e = (Element) nodeList.get(0);
				if (StringUtils.isNotEmpty(user.getLoginName())) {
					Element ln = e.element("LoginName");
					if (ln != null)
						ln.setText(user.getLoginName());
				}
				if (StringUtils.isNotEmpty(user.getName())) {
					Element ln = e.element("UserName");
					if (ln != null)
						ln.setText(user.getName());
				}
				if (StringUtils.isNotEmpty(user.getEmail())) {
					Element ln = e.element("Email");
					if (ln != null)
						ln.setText(user.getEmail());
				}
				if (StringUtils.isNotEmpty(user.getPassword())) {
					Element ln = e.element("PassWord");
					if (ln != null)
						// 
						ln.setText(user.getPassword());
				}
				if (StringUtils.isNotEmpty(user.getAuth())) {
					Element ln = e.element("Auth");
					if (ln != null)
						ln.setText(user.getAuth());
				}
				if (StringUtils.isNotEmpty(user.getGuide())) {
					Element ln = e.element("Guide");
					// 
					if (ln != null)
						ln.setText(user.getGuide());
					// 
				}
				flag = saveXml(xmlName, doc);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return flag;
	}

	public boolean deleteUser(String Id) throws IOException {
		if (StringUtils.isEmpty(Id)) {
			return false;
		}
		boolean flag = false;
		try{
			String xmlName = "users.xml";
			Document doc = getDocumentByReader(xmlName);
			Element elment = doc.getRootElement();
			
			Node userNode = elment.selectSingleNode("//User[Id='" + Id + "']");
	
			if ((userNode != null) && (elment.remove(userNode))) {
				flag = saveXml(xmlName, doc);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}

		return flag;
	}

	public boolean saveUser(User user) throws IOException {
		if (user == null) {
			return false;
		}
		boolean flag = false;
		
		try {
			String xmlName = "users.xml";
			Document doc = getDocumentByReader(xmlName);
			Element elment = doc.getRootElement();

			if (doc != null) {
				Element euser = elment.addElement("User");
				euser.addElement("Id").setText(UUID.create());
				euser.addElement("LoginName").setText(
						StringUtils.defaultIfEmpty(user.getLoginName(), ""));
				euser.addElement("UserName").setText(
						StringUtils.defaultIfEmpty(user.getName(), ""));
				euser.addElement("PassWord").setText(
						StringUtils.defaultIfEmpty(user.getPassword(), ""));
				euser.addElement("Email").setText(
						StringUtils.defaultIfEmpty(user.getEmail(), ""));
				euser.addElement("Auth").setText(
						StringUtils.defaultIfEmpty(user.getAuth(), ""));
				euser.addElement("Guide").setText(
						StringUtils.defaultIfEmpty(user.getGuide(), ""));
				flag = saveXml(xmlName, doc);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return flag;
	}
	
	private boolean createUsersXml(String xmlName){
		String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +

							"<Root>" + 
							  "<User>" + 
							    "<Id>1</Id>" +  
							    "<LoginName>admin</LoginName>" +  
							    "<UserName>admin</UserName>" +  
							    "<PassWord>+KCYgLDAqICAgICAgICAgA==</PassWord>" +  
							    "<Email>zxm@kylinos.com.cn</Email>" +  
							    "<Guide>1</Guide>" +  
							    "<Auth>admin</Auth>" + 
							  "</User>" + 
							"</Root>";
		File file = new File(xmlName);
		try{
			if(!file.exists()){
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(xmlStr);
			bw.flush();
			bw.close();
			fw.close();
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		return true;
		
	}
	
	public boolean initUsersXml(){
		String xmlName = XML_REALPATH + XML_PATH + "users.xml";
		File file = new File(xmlName);
		if(!file.exists()){
			return createUsersXml(xmlName);
		}else{
			Document doc = getDocumentByReader("users.xml");
			if(doc == null || !doc.hasContent()){
				return createUsersXml(xmlName);
			}
		}
		return true;
		
	}

	public String configExport() throws XmlRpcException, DocumentException, IOException {
		String result = (String)executeXml("Configure.Export");
		return formatXml(DocumentHelper.parseText(result));
	}

	public boolean saveConfig(String filename, File file) throws IOException {
		try {
			SAXReader saxReader = new SAXReader();
			Document document = null;
			document = saxReader.read(file);
			return saveXml(filename, document);
		} catch (Exception localException) {
		}
		return false;
	}

	public String configImport(File xml) throws IOException, DocumentException {
		if (xml == null) {
			return "false";
		}
		Document newXml = getDocumentByInputStream(xml);
		String newXmlString = formatXml(DocumentHelper.parseText(newXml.asXML()));		
		
		String flagStr = "false";
		try {
			Boolean importResult = (Boolean)executeXml("Configure.Import", newXmlString);
			if ( importResult != null && importResult == true ) {
				flagStr = "true";
			}
		} catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}

	public String statService(Statistics ser) throws XmlRpcException {
		Object obj = executeXml(methodName, ser.serviceToXml());
		return (String) obj;
	}

	public String statServer(Statistics ser) throws XmlRpcException {
		Object obj = executeXml(methodName, ser.serverToXml());
		return (String) obj;
	}

	private String findServiceByIp(String ip) throws XmlRpcException {
		String xml = getSirtualServDocument();
		try {
			Document doc = DocumentHelper.parseText(xml);
			Element ele = doc.getRootElement().element("Services");
			if (!ele.elements().isEmpty())
				for (Iterator it = ele.elementIterator(); it.hasNext();) {
					Element ele1 = (Element) it.next();
					if (!ele1.elements().isEmpty()) {
						Element IP = ele1.element("IP");
						if (!IP.attributes().isEmpty()) {
							String vip = IP.attribute("value").getValue();
							if (vip.equalsIgnoreCase(ip))
								return ele1.getName();
						}
					}
				}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	public LoadSirtualServ getSirServByIp(String ip) throws XmlRpcException {
		String xml = getSirtualServDocument();
		try {
			Document doc = DocumentHelper.parseText(xml);
			Element ele = doc.getRootElement().element("Services");
			if (!ele.elements().isEmpty())
				for (Iterator it = ele.elementIterator(); it.hasNext();) {
					Element ele1 = (Element) it.next();
					if (!ele1.elements().isEmpty()) {
						Element IP = ele1.element("IP");
						if (!IP.attributes().isEmpty()) {
							String vip = IP.attribute("value").getValue();
							if (vip.equalsIgnoreCase(ip))
								return getSirtualServByName(ele1.getName());
						}
					}
				}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String findServiceServerByIp(String ip) throws XmlRpcException {
		String xml = getSirtualServDocument();
		try {
			Document doc = DocumentHelper.parseText(xml);
			Element ele = doc.getRootElement().element("Services");
			if (!ele.elements().isEmpty()) {
				for (Iterator it = ele.elementIterator(); it.hasNext();) {
					Element ele1 = (Element) it.next();
					if (!ele1.elements().isEmpty()) {
						Element Servers = ele1.element("Servers");
						if (!Servers.elements().isEmpty()) {
							for (Iterator it1 = Servers.elementIterator(); it1
									.hasNext();) {
								Element server = (Element) it1.next();
								if (!server.elements().isEmpty()) {
									Element IP = server.element("IP");
									if ((!IP.attributes().isEmpty())
											&& (ip.equalsIgnoreCase(IP
													.attribute("value")
													.getValue()))) {
										return ele1.getName() + " "
												+ server.getName();
									}
								}
							}
						}
					}
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	private LoadTrueServ getServiceServerByIp(String ip) throws XmlRpcException {
		String xml = getSirtualServDocument();
		try {
			Document doc = DocumentHelper.parseText(xml);
			Element ele = doc.getRootElement().element("Services");
			if (!ele.elements().isEmpty()) {
				for (Iterator it = ele.elementIterator(); it.hasNext();) {
					Element ele1 = (Element) it.next();
					if (!ele1.elements().isEmpty()) {
						Element Servers = ele1.element("Servers");
						if (!Servers.elements().isEmpty()) {
							for (Iterator it1 = Servers.elementIterator(); it1
									.hasNext();) {
								Element server = (Element) it1.next();
								if (!server.elements().isEmpty()) {
									Element IP = server.element("IP");
									if ((!IP.attributes().isEmpty())
											&& (ip.equalsIgnoreCase(IP
													.attribute("value")
													.getValue())))
										return getTrueServByName(
												ele1.getName(), server
														.getName());
								}
							}
						}
					}
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean serviceServerSet(LoadSirtualServ sir, LoadTrueServ ser) {
		try {
			if (sir.getService() != null) {
				ser.setService(sir.getService());
				executeXml(methodName, sir.toXml());
				executeXml(methodName, ser.toXml());
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	public SystemHard getCpuInfo() {
		SystemHard systemHard = new SystemHard();
		try {			
			Map result = (Map)executeXml("Status.Core");						
			if (result != null) {														
				Object[] core = (Object[])result.get("Core");
				String usage = (String)result.get("Usage");
				usage = usage.replaceAll("%", "").trim();				
				
				systemHard.setCpuNum(core.length);
				systemHard.setCpuSpeed(NumberUtils.toFloat((String)core[0]));
				systemHard.setCpuUsage(NumberUtils.toDouble(usage));
								
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return systemHard;
	}

	public SystemHard getDiskInfo() {
		SystemHard systemHard = new SystemHard();
		try {			
			Map result = (Map)executeXml("Status.Disk");						
			if (result != null) {				
				Integer total = (Integer)result.get("Total");
				Integer free = (Integer)result.get("Free");
				Integer used = (Integer)result.get("Used");
				
				systemHard.setHdTotal(total);
				systemHard.setHdFree(free);
				systemHard.setHdUsed(used);
				systemHard.setHdUsage(NumberUtils.toInt(new DecimalFormat("#")
						.format(systemHard.getHdUsed()
								/ systemHard.getHdTotal() * 100.0D)));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return systemHard;
	}

	public SystemHard getMemInfo() {
		SystemHard systemHard = new SystemHard();
		try {
			Map result = (Map)executeXml("Status.Memory");
			if (result != null) {
				Integer total = (Integer)result.get("Total");
				Integer free = (Integer)result.get("Free");
				systemHard.setMemTotal(total / 1024);
				systemHard.setMemFree(free / 1024);
				systemHard.setMemUsed(systemHard.getMemTotal()
						- systemHard.getMemFree());
				systemHard.setMemUsage(NumberUtils.toInt(new DecimalFormat("#")
						.format(systemHard.getMemUsed()
								/ systemHard.getMemTotal() * 100.0D)));	
			}
		} catch (Exception localException) {			
		}
		return systemHard;
	}

	public List<Director> getDirectorInfo() {
		Director director = null;
		Director.Remote remote = null;
		List directors = new ArrayList();
		List remotes = new ArrayList();
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
					Integer mark = (Integer)res.get("Mark");
					String descrip = (String)res.get("Description");
					String name = (String)res.get("Name");
					Boolean vsEnabled = (Boolean)res.get("Enabled");
					Object[] ips = (Object[])res.get("IP");
					Object[] tcpPorts = (Object[])res.get("TcpPorts");
					Object[] udpPorts = (Object[])res.get("UdpPorts");
					Integer schedule = (Integer)res.get("Schedule");
					Boolean persistent = (Boolean)res.get("Persistent");
					Integer persistentTimeout = (Integer)res.get("PersistentTimeout");
if(Struts2Utils.IS_HA_ENABLE == true){
					Integer ha = (Integer)res.get("HA");
					Boolean haStatus = (Boolean)res.get("HAStatus");
}
					Object[] servers = (Object[])res.get("Servers");
					
					director = new Director();
					if (vsEnabled == null || vsEnabled != true) {
						continue;
					} else {						
						Map schedulings = Utils.getInstance().getSchedulings();
						director.setLocalScheduler( (String) schedulings.get(schedule.toString()) );
						
						String localAddress = "";
						if ( name != null && !name.equals("")) {
							localAddress += name + ":";
						}
						if ( ips != null && ips.length !=0 ) {
							Map ipMap = (Map)ips[0];
							String ip = (String)ipMap.get("IP");
							//String dev = (String)ipMap.get("Dev");
							Boolean status = (Boolean)ipMap.get("Status");												
							
							localAddress += ip + "&nbsp;&nbsp;";
						}
						
						StringBuffer tcpbuf = new StringBuffer();
						StringBuffer udpbuf = new StringBuffer();
						StringBuffer tcpbufLimit = new StringBuffer();
						StringBuffer udpbufLimit = new StringBuffer();
						Integer tcpMin = null;
						if ( tcpPorts != null && tcpPorts.length !=0 ) {
							for (int j = 0; j < tcpPorts.length; j++) {							
								
								Map tcpPort = (Map)tcpPorts[j];
								tcpMin = (Integer)tcpPort.get("Min");
								Integer max = (Integer)tcpPort.get("Max");														
								
								if ( tcpMin != null ) {
									if ( tcpMin.equals(max) ) {
										tcpbuf.append(tcpMin.toString());
										tcpbuf.append(",");
									} else {
										tcpbuf.append(tcpMin.toString());
										tcpbuf.append("-");
										tcpbuf.append(max.toString());
										tcpbuf.append(",");
									}
								}																																	
							}
						}
						if ( tcpPorts != null && tcpPorts.length >=3 ) {
							for (int j = 0; j < 2; j++) {								
								Map tcpPort = (Map)tcpPorts[j];
								tcpMin = (Integer)tcpPort.get("Min");
								Integer max = (Integer)tcpPort.get("Max");
								
								if ( tcpMin != null ) {
									if ( tcpMin.equals(max) ) {
										tcpbufLimit.append(tcpMin.toString());
										tcpbufLimit.append(",");
									} else {
										tcpbufLimit.append(tcpMin.toString());
										tcpbufLimit.append("-");
										tcpbufLimit.append(max.toString());
										tcpbufLimit.append(",");
									}
								}																																	
							}
						}
						
						Integer udpMin = null;
						if ( udpPorts != null && udpPorts.length !=0 ) {
							for (int j = 0; j < udpPorts.length; j++) {
								
								Map udpPort = (Map)udpPorts[j];
								udpMin = (Integer)udpPort.get("Min");
								Integer max = (Integer)udpPort.get("Max");														
								
								if ( udpMin != null ) {
									if ( udpMin.equals(max) ) {
										udpbuf.append(udpMin.toString());
										udpbuf.append(",");
									} else {
										udpbuf.append(udpMin.toString());
										udpbuf.append("-");
										udpbuf.append(max.toString());
										udpbuf.append(",");
									}
								}																																					
							}
						}
						if ( udpPorts != null && udpPorts.length >=3 ) {
							for (int j = 0; j < 2; j++) {								
								Map udpPort = (Map)udpPorts[j];
								udpMin = (Integer)udpPort.get("Min");
								Integer max = (Integer)udpPort.get("Max");														
								
								if ( udpMin != null ) {
									if ( udpMin.equals(max) ) {
										udpbufLimit.append(udpMin.toString());
										udpbufLimit.append(",");
									} else {
										udpbufLimit.append(udpMin.toString());
										udpbufLimit.append("-");
										udpbufLimit.append(max.toString());
										udpbufLimit.append(",");
									}
								}																																					
							}
						}
						
						String tcpAndPort = new String();
						String udpAndPort = new String();
						if ( tcpPorts != null && tcpPorts.length !=0 ) {
							if (tcpPorts.length < 3) {
								tcpAndPort = "TCP:" + tcpbuf.substring(0, tcpbuf.length() - 1);
							} else {
								tcpAndPort = "TCP:" + tcpbufLimit.substring(0, tcpbufLimit.length() - 1) + " ..." ;
							}
						}
						if ( udpPorts != null && udpPorts.length !=0 ) {
							if (udpPorts.length < 3) {
								udpAndPort = "UDP:" + udpbuf.substring(0, udpbuf.length() - 1);
							} else {
								udpAndPort = "UDP:" + udpbufLimit.substring(0, udpbufLimit.length() - 1) + " ...";
							}
						}
						if ( tcpMin == null) {
							if (udpMin != null) {
								localAddress += udpAndPort;								
							}
						} else if (udpMin == null) {
							localAddress += tcpAndPort;							
						} else {
							localAddress += tcpAndPort + " " + udpAndPort;							
						}
						director.setLocalAddress(localAddress);
						//持久连接开启时，持久连接时间才有效
						if ( persistent == true ) {
							director.setLocalPersistent(persistentTimeout.toString());
						}
					}
					
					if (servers == null || servers.length == 0) {
						continue;
					} else {
						for (int j = 0; j < servers.length; j++) {
							Map server = (Map)servers[j];
							Integer tsId = (Integer)server.get("ID");
							String ip = (String)server.get("IP");
							String tsName = (String)server.get("Name");
							Integer action = (Integer)server.get("Action");
							Integer weight = (Integer)server.get("Weight");
							Boolean tsEnabled = (Boolean)server.get("Enabled");
							Boolean status = (Boolean)server.get("Status");
							Integer active = (Integer)server.get("Active");
							Integer inActive = (Integer)server.get("InActive");
							
							remote = new Director.Remote();
							if (tsEnabled == null || tsEnabled != true) {
								continue;
							} else {
								remote.setRemoteForward( Utils.getInstance().getForward(action.toString()) );
								remote.setRemoteWeight(weight.toString());
								remote.setRemoteActiveConn(active.toString());
								remote.setRemoteInActConn(inActive.toString());
								
								if ( ip != null && (!"".equals(ip)) ) {
									remote.setRemoteAddress(ip);
								}
							}																					
							remotes.add(remote);
						}
					}													
					director.setRemotes(remotes);
					directors.add(director);
					remotes = new ArrayList();								
				}
			}
		} catch (Exception localException) {
		}
		return directors;
	}
	public String rebootServer() {		
		Hashtable tableTmp = new Hashtable();
		
		
		String flagStr = "false";
		try {			
			Boolean result = (Boolean)executeXml("System.Reboot", tableTmp);
			if ( result != null && result == true ) {
				flagStr = "true";
			}
		} catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}
	
	public String shutdownServer() {		
		Hashtable tableTmp = new Hashtable();
		
		
		String flagStr = "false";
		try {			
			Boolean result = (Boolean)executeXml("System.Shutdown", tableTmp);
			if ( result != null && result == true ) {
				flagStr = "true";
			}
		} catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}

	public static void main(String[] args) throws XmlRpcException {
		KlbManager t = new KlbManager();
		Statistics ser = new Statistics();
		ser.setServer("web-test1");
		ser.setStartDate("2010-03-03 00:00:00");
		ser.setEndDate("2010-03-09 00:00:00");
		ser.setTime("60");
		String s = t.statService(ser);

	}
}
