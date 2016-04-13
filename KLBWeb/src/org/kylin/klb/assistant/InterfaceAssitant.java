package org.kylin.klb.assistant;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.xmlrpc.XmlRpcException;
import org.kylin.klb.entity.network.InterfaceInfo;
import org.kylin.klb.service.KlbClient;

public class InterfaceAssitant extends KlbClient {
	//private static String methodName = "Execute";

	/* public List<String> getEthernets() {
		List<String> ethernets = new ArrayList<String>();
		String xmlName = "interfaces.xml";
		Document doc = getDocumentByInputStream(xmlName);
		doc.getRootElement().element("Network").element("Interfaces")
				.addElement("Ethernet").addAttribute("get", "1");
		String result = (String) executeXml(methodName, doc.asXML());
		try {
			Document document = DocumentHelper.parseText(result);
			Element eth = document.getRootElement().element("Network").element(
					"Interfaces").element("Ethernet");
			if (!eth.elements().isEmpty()) {
				for (Iterator i = eth.elementIterator(); i.hasNext();) {
					Element e = (Element) i.next();
					ethernets.add(e.getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ethernets;
	}
	
	public String constructXmlForInterfaceInfo() {
		String xmlName = "interfaces.xml";
		Document doc = getDocumentByInputStream(xmlName);
		Element ethernet = doc.getRootElement().element("Network").element("Interfaces")
				.addElement("Ethernet");
		List<String> ethernets = getEthernets();
		if(ethernets == null){
			return null;
		}
		Iterator<String> i = ethernets.iterator();
		while(i.hasNext()){
			String eth = i.next();
			ethernet.addElement(eth);
			ethernet.element(eth).addElement("Address").addAttribute("get", "1");
			ethernet.element(eth).addElement("ArpState").addAttribute("get", "1");
			ethernet.element(eth).addElement("Description").addAttribute("get", "1");
			ethernet.element(eth).addElement("Dhcp").addElement("Enabled").addAttribute("get", "1");
			ethernet.element(eth).addElement("Duplex").addAttribute("get", "1");
			ethernet.element(eth).addElement("Enabled").addAttribute("get", "1");
			ethernet.element(eth).addElement("IP").addAttribute("get", "1");
			ethernet.element(eth).addElement("MTU").addAttribute("get", "1");
			ethernet.element(eth).addElement("Speed").addAttribute("get", "1");
			ethernet.element(eth).addElement("Status").addElement("Link").addAttribute("get", "1");
			ethernet.element(eth).addElement("Status").addElement("RealAddress").addAttribute("get", "1");
		}
		return doc.asXML();
	} */

	public List<InterfaceInfo> getInterfaceInfoList() {
		List<InterfaceInfo> iil = new ArrayList<InterfaceInfo>();
		InterfaceInfo ii = new InterfaceInfo();
		try {
			Object[] result = (Object[])executeXml("Ethernet.GetAll");			
			if (result != null) {				
				for (int i = 0; i < result.length; i++) {
					Map res = (Map)result[i];
					String name = (String)res.get("Dev");
					String descrip = (String)res.get("Description");
					String adsl = (String)res.get("Adsl");
					String master = (String)res.get("Master");
					String address = (String)res.get("Address");
					String currentAddress = (String)res.get("CurrentAddress");
					String realAddress = (String)res.get("RealAddress");
					Boolean detect = (Boolean)res.get("Detect");
					Boolean fullDuplex = (Boolean)res.get("FullDuplex");
					Integer speed = (Integer)res.get("Speed");
					Integer mtu = (Integer)res.get("MTU");
					Object[] ip = (Object[])res.get("IP");
					Boolean enabled = (Boolean)res.get("Enabled");
					Integer  arp = (Integer)res.get("Arp");
					Boolean dhcp = (Boolean)res.get("Dhcp");
					Boolean carrier = (Boolean)res.get("Carrier");
					
					/* System.out.println(name);
					System.out.println(descrip);
					System.out.println(adsl);
					System.out.println(master);
					System.out.println(address);
					System.out.println(currentAddress);
					System.out.println(realAddress);
					System.out.println(detect);
					System.out.println(fullDuplex);
					System.out.println(speed);
					System.out.println(mtu);
					System.out.println(enabled);
					System.out.println(arp);
					System.out.println(dhcp);
					System.out.println(carrier.toString()); */
					
					if (name != null) {
						ii.setName(name);
					}
					if (descrip != null) {
						ii.setDescription(descrip);					
					}
					
					ii.setState(enabled.toString());
					ii.setDhcp(dhcp.toString());
																			
					String mode = new String();
					String dh = dhcp.toString();
					if( !StringUtils.equals(adsl, "") ){
						mode = "adsl";						
					} else if ( !StringUtils.equals(master, "") ) {
						mode = "slave";
					} else if ( StringUtils.equals(dh, "true") ) {
						mode = "dhcp";
					} else {
						mode = "route";
					}
					ii.setInterfaceMode(mode);
															
					String ipList = new String();
					if(ip.length==0){
						ipList = "";
					}
					else{
						for(int j=0; j<ip.length; j++){
							if (!StringUtils.equals((String)ip[j], "")) {
								ipList += ip[j] + ";";
							}
						}
						ipList = ipList.substring(0, ipList.length()-1);
					}
					ii.setIpList(ipList);
					ii.setMtu(mtu.toString());
					ii.setMac(currentAddress);
					ii.setConnect(carrier.toString());
					if(StringUtils.equals(detect.toString(), "true")){
						ii.setNegotiation("Auto");
					}
					else{
						ii.setNegotiation("Hand");
					}
					ii.setDoubleMode(fullDuplex.toString());
					ii.setSpeed(speed.toString());
					ii.setArpState(arp.toString());
					iil.add(ii);
					ii = new InterfaceInfo();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return iil;
	}
	
	public String editInterfaceInfo(InterfaceInfo ii) throws XmlRpcException {
		
		Hashtable descriTemp = new Hashtable();
		descriTemp.put("Dev", ii.getName());
		descriTemp.put("Description", ii.getDescription());
		Boolean descriResult = (Boolean)executeXml("Ethernet.Description", descriTemp);
		System.out.println(descriResult);
		
		Hashtable enableTemp = new Hashtable();
		enableTemp.put("Dev", ii.getName());
		Boolean bool = null;
		if ( ii.getState().equals("true") ) {
			bool = true;
		} else {
			bool = false;
		}
		enableTemp.put("Enabled", bool);
		Boolean enableResult = (Boolean)executeXml("Ethernet.Enabled", enableTemp);
		System.out.println(enableResult);
		
		Hashtable dhcpTemp = new Hashtable();
		dhcpTemp.put("Dev", ii.getName());		
		dhcpTemp.put("Dhcp", ii.getDhcp().equals("true") ? true : false);
		Boolean dhcpResult = (Boolean)executeXml("Ethernet.Dhcp", dhcpTemp);
		System.out.println(dhcpResult);
		
		Hashtable ipTemp = new Hashtable();
		ipTemp.put("Dev", ii.getName());		
		String ipStr = ii.getIpList();
		String[] ipArray = null;
		if (ipStr.equals("")) {
			ipArray = new String[0];
		} else {
			ipArray = ii.getIpList().split(";");
		}
		ipTemp.put("IP", ipArray);
		Boolean ipResult = true;
		if ( ii.getDhcp().equals("false") ) {
			ipResult = (Boolean)executeXml("Ethernet.IP", ipTemp);
		}
		
		Hashtable mtuTemp = new Hashtable();
		mtuTemp.put("Dev", ii.getName());
		mtuTemp.put("MTU", NumberUtils.toInt(ii.getMtu()));
		Boolean mtuResult = (Boolean)executeXml("Ethernet.MTU",mtuTemp);
		System.out.println(mtuResult);
				
		Hashtable macTemp = new Hashtable();
		macTemp.put("Dev", ii.getName());
		macTemp.put("Address", ii.getMac());
		Boolean macResult = (Boolean)executeXml("Ethernet.Address",macTemp);
		System.out.println(macResult);
										
		//System.out.println(ii.getNegotiation() +"..;");
		if(StringUtils.equals(ii.getNegotiation(), "Auto")){			
			Hashtable autoTemp = new Hashtable();
			autoTemp.put("Dev", ii.getName());
			autoTemp.put("Detect", true);
			Boolean autoResult = (Boolean)executeXml("Ethernet.Link",autoTemp);
			System.out.println(autoResult);			
		}
		else{
			//System.out.println(ii.getDoubleMode() +"..;");
			//System.out.println(ii.getSpeed() +"..;");			
			Hashtable handTemp = new Hashtable();
			handTemp.put("Dev", ii.getName());
			handTemp.put("Detect", false);
			handTemp.put("FullDuplex", ii.getDoubleMode().equals("true") ? true : false);
			handTemp.put("Speed", NumberUtils.toInt(ii.getSpeed()));
			Boolean handResult = (Boolean)executeXml("Ethernet.Link", handTemp);
			System.out.println(handResult);
		}
		
		Hashtable arpTemp = new Hashtable();
		arpTemp.put("Dev", ii.getName());
		arpTemp.put("Arp", NumberUtils.toInt(ii.getArpState()));
		Boolean arpResult = (Boolean)executeXml("Ethernet.Arp",arpTemp);
		System.out.println(arpResult);
																
		String failedMess = getFailedMessage(ii.getName(), ipResult);
		return failedMess;
	}
	
	public String getFailedMessage(String name, Boolean result) {
		String failedMess = new String();
		if(result != null && result == true){
			return "";
		} else {
			try {
				failedMess += "接口" + name + ":";
				failedMess += "IP地址设置失败。";
				return failedMess;
			}catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}
}
