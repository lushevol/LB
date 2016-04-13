package org.kylin.klb.service.network;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.xmlrpc.XmlRpcException;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.kylin.klb.entity.network.Arp;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.service.KlbClient;
import org.kylin.klb.util.Utils;

public class ArpService extends KlbClient {
	private static String methodName = "Execute";
	
	public List<Display> getInterfaceList(){
		List<Display> ret = new ArrayList<Display>();		
		try {			
			Object[] result = (Object[])executeXml("Bonding.GetAll");
			String interBonded = new String();
			if ( result != null && result.length != 0 ) {				
				for (int i = 0; i < result.length; i++) {					
					Map res = (Map)result[i];
					String bondName = (String)res.get("Dev");
					Object[] slaves = (Object[])res.get("Slaves");
					
					Display interBond = new Display(bondName, bondName);
					ret.add(interBond);					
					if( slaves.length != 0 ){
						for(int j=0; j<slaves.length; j++){
							if (!StringUtils.equals((String)slaves[j], "")) {
								interBonded += slaves[j] + ";";
							}
						}
						interBonded = interBonded.substring(0, interBonded.length()-1);
					}					
				}
			}
			
			Object[] ethResult = (Object[])executeXml("Ethernet.GetAll");
			if ( ethResult != null && ethResult.length != 0 ) {				
				for (int i = 0; i < ethResult.length; i++) {
					Map res = (Map)ethResult[i];
					String ethName = (String)res.get("Dev");
					String adsl = (String)res.get("Adsl");
					Display interEth = new Display(ethName, ethName);
					if ( !StringUtils.equals(adsl, "") ) {
						interEth = new Display(adsl, adsl);
					}
					if ( interBonded.contains(ethName) ) {
						interEth = new Display(ethName, ethName + "(无效)");												
					}
					ret.add(interEth);										
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	/* public void delArpById(String id){
		id = "_" + id + "_";
		String xmlName = "arp.xml";
		Document doc = getDocumentByInputStream(xmlName);
		doc.getRootElement().element("Network").element("Arp").addElement("Static").
		addAttribute("remove", "1").addElement(id);
		
		String result = (String) executeXml(methodName, doc.asXML());
	} */
	
	public Boolean delArp(String ip, String inter) {
		boolean flag = false;
		try {
			Hashtable delTemp = new Hashtable();
			delTemp.put("IP", ip);
			delTemp.put("Dev", inter);
			Boolean delResult = (Boolean)executeXml("Arp.Delete",delTemp);
			System.out.println(delResult);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}
	
	public String editArp(String id, Arp arp){
		Hashtable setTemp = new Hashtable();
		setTemp.put("IP", arp.getIp());
		setTemp.put("MAC", arp.getMac());
		setTemp.put("Dev", arp.getInter());
		
		String flagStr = "false";
		try {
			Boolean setResult = (Boolean)executeXml("Arp.Set",setTemp);
			flagStr = "true";
		} catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}
	
	public String addArp(Arp arp){				
		Hashtable addTemp = new Hashtable();
		addTemp.put("IP", arp.getIp());
		addTemp.put("MAC", arp.getMac());
		addTemp.put("Dev", arp.getInter());
		
		String flagStr = "false";
		try {
			Boolean addResult = (Boolean)executeXml("Arp.Set",addTemp);
			System.out.println(addResult);
			
			if ( addResult != null && addResult == true ) {
				flagStr = "true";
			}
		} catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}
	
	public List<Arp> getArpDynamicList(){
		List<Arp> arpInfoList = new ArrayList<Arp>();
		
		try {
			Object[] result = (Object[])executeXml("Arp.Get.Dynamic");			
			if (result != null) {				
				for (int i = 0; i < result.length; i++) {
					Map res = (Map)result[i];
					String ip = (String)res.get("IP");
					String mac = (String)res.get("MAC");
					String dev = (String)res.get("Dev");
					Integer status = (Integer)res.get("Status");
					
					/* System.out.println(ip);
					System.out.println(mac);
					System.out.println(dev);
					System.out.println(status); */
					
					Arp arp = new Arp();
					arp.setIp(ip);
					arp.setMac(mac);
					arp.setInter(dev);
					arp.setType(status.toString());
					arpInfoList.add(arp);
				}
			}						
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return arpInfoList;
	}
	public List<Arp> getArpInfoList(){
		List<Arp> arpInfoList = getArpStaticList();
		List<Arp> arpInfoDynamicList = getArpDynamicList();
		
		Iterator<Arp> i = arpInfoDynamicList.iterator();
		while(i.hasNext()){
			Arp arp = i.next();
			arpInfoList.add(arp);
		}
		return arpInfoList;
	}
	/* public List<String> getArpStaticIdList(){
		List<String> arpStaticIdList = new ArrayList<String>();
		String xmlName = "arp.xml";
		Document doc = getDocumentByInputStream(xmlName);
		doc.getRootElement().element("Network").element("Arp").addElement("Static").addAttribute("get", "1");
		String result = (String) executeXml(methodName, doc.asXML());
		try {
			Document document = DocumentHelper.parseText(result);
			Element staticElement = document.getRootElement().element("Network").element("Arp").element("Static");
			for (Iterator i=staticElement.elementIterator(); i.hasNext();) {
				Element e = (Element)i.next();
				arpStaticIdList.add(e.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return arpStaticIdList;
	}
	public String constructXmlForStaticInfo() {
		String xmlName = "arp.xml";
		Document doc = getDocumentByInputStream(xmlName);
		Element temp = doc.getRootElement().element("Network").element("Arp").addElement("Static");
		List<String> arpStaticIdList = getArpStaticIdList();
		if (arpStaticIdList == null) {
			return null;
		}
		Iterator<String> i = arpStaticIdList.iterator();
		while (i.hasNext()) {
			String arpStaticId = i.next();
			temp.addElement(arpStaticId);
			temp.element(arpStaticId).addElement("IP").addAttribute("get", "1");
			temp.element(arpStaticId).addElement("MAC").addAttribute("get", "1");
		}
		return doc.asXML();
	} */
	public List<Arp> getArpStaticList(){
		List<Arp> arpInfoList = new ArrayList<Arp>();
		
		try {
			Object[] result = (Object[])executeXml("Arp.Get.Static");			
			if (result != null) {				
				for (int i = 0; i < result.length; i++) {
					Map res = (Map)result[i];
					String ip = (String)res.get("IP");
					String mac = (String)res.get("MAC");
					String dev = (String)res.get("Dev");
					Integer status = (Integer)res.get("Status");
					
					/* System.out.println(ip);
					System.out.println(mac);
					System.out.println(dev);
					System.out.println(status); */
					
					Arp arp = new Arp();
					arp.setIp(ip);
					arp.setMac(mac);
					arp.setInter(dev);
					arp.setType(status.toString());
					arpInfoList.add(arp);
										
				}
			}						
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return arpInfoList;
	}
}
