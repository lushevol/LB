package org.kylin.klb.service.network;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.kylin.klb.entity.network.RouteStaticConfig;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.service.KlbClient;
import org.kylin.klb.util.Utils;

public class RouteStaticConfigService extends KlbClient {
	//private static String methodName = "Execute";
	//private static String xmlName = "routestatic.xml";
	
	public List<Display> getInterfaceList(){
		/* List<Display> ret = new ArrayList<Display>();
		Display display = new Display("eth0", "eth0");
		ret.add(display);
		display = new Display("eth1", "eth1");
		ret.add(display);
		display = new Display("eth2", "eth2");
		ret.add(display);
		display = new Display("vlan", "vlan");
		ret.add(display);
		display = new Display("ipsec0", "ipsec0");
		ret.add(display);
		return ret; */
		//List<String> ret = new ArrayList<String>();
		
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
					//ret.add(bondName);
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
					/* if ( !interBonded.contains(ethName) ) {
						Display interEth = new Display(ethName, ethName);
						ret.add(interEth);
						//ret.add(ethName);
					} */
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
	
	/* public List<String> getInterList(){
		List<String> ret = new ArrayList<String>();
		try {			
			Object[] result = (Object[])executeXml("Bonding.GetAll");
			String interBonded = new String();
			if ( result != null && result.length != 0 ) {				
				for (int i = 0; i < result.length; i++) {					
					Map res = (Map)result[i];
					String bondName = (String)res.get("Dev");
					Object[] slaves = (Object[])res.get("Slaves");
					
					ret.add(bondName);
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
					if ( !interBonded.contains(ethName) ) {
						ret.add(ethName);
					}
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	} */
	
	public List<Display> getGatePolicyList() {
		List<Display> ret = new ArrayList<Display>();
		Display display = new Display("0", "rr算法");
		ret.add(display);
		display = new Display("1", "drr算法");
		ret.add(display);
		display = new Display("2", "random算法");
		ret.add(display);
		display = new Display("3", "wrandom算法");
		ret.add(display);
		return ret;
	}
	/*public String getInterIp(){
		String xmlName = "interfaces.xml";
		Document doc = getDocumentByInputStream(xmlName);
		doc.getRootElement().element("Network").element("Interfaces")
				.addElement("Ethernet").addAttribute("get", "1");
		String result = (String) executeXml(methodName, doc.asXML());
		try {
			Document document = DocumentHelper.parseText(result);
			Element eth = document.getRootElement().element("Network").element("Interfaces").
			element("Ethernet");
			if (!eth.elements().isEmpty()) {
				for (Iterator i = eth.elementIterator(); i.hasNext();) {
					Element e = (Element) i.next();
					e.addElement("IP").addAttribute("get", "1");
				}
			}
			result = (String) executeXml(methodName, document.asXML());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public boolean judgeInter(String gate, String ip){
		String[] imArray = ip.split("/");
		String[] ipArray = imArray[0].split("[.]");
		String[] gateArray = gate.split("[.]");
		String ipBinary = "";
		String gateBinary = "";
		for(int i=0; i<ipArray.length; i++){
			String temp = Integer.toBinaryString(Integer.parseInt(ipArray[i]));
			int length = temp.length();
			while(length < 8){
				ipBinary += "0";
				length += 1;
			}
			ipBinary += temp;
			temp = Integer.toBinaryString(Integer.parseInt(gateArray[i]));
			length = temp.length();
			while(length < 8){
				gateBinary += "0";
				length += 1;
			}
			gateBinary += temp;
		}
		for(int i=0; i<ipBinary.length(); i++){
			if(ipBinary.charAt(i) != gateBinary.charAt(i)){
				if(32-i<=Integer.parseInt(imArray[1])){
					return true;
				}
				else{
					return false;
				}
			}
		}
		return true;
	}*/
	
	/* public String getGate(String inter){
		String method = "Route.RealGate";
		String result = (String) executeXml(method, inter);
		return result;
	}
	public String getInter(String gate){
		String method = "Route.RealInterface";
		String result = (String) executeXml(method, gate);
		return result;
	} */
	/*public String getInter(String gate){
		String xml = getInterIp();
		try {
			Document document = DocumentHelper.parseText(xml);
			Element eth = document.getRootElement().element("Network").element("Interfaces").
			element("Ethernet");
			if (!eth.elements().isEmpty()) {
				for (Iterator i = eth.elementIterator(); i.hasNext();) {
					Element e = (Element) i.next();
					if (!e.element("IP").elements().isEmpty()) {
						for (Iterator ii = e.element("IP").elementIterator(); ii.hasNext();) {
							Element ee = (Element) ii.next();
							String ip = ee.attributeValue("value");
							if(judgeInter(gate, ip)){
								return e.getName();
							}
						}
					}
				}
			}
			return "none";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}*/
	
	/* public String delGatesNodeForEdit(String id) {
		Document doc = getDocumentByInputStream(xmlName);
		doc.getRootElement().element("Network").element("Route").
		element("Static").addElement(id).addElement("Gates").addAttribute("get", "1");
		String result = (String) executeXml(methodName, doc.asXML());
		try {
			doc = DocumentHelper.parseText(result);			
			doc.getRootElement().element("Network").element("Route").
			element("Static").element(id).element("Gates").addAttribute("remove", "1");			
			result = (String) executeXml(methodName, doc.asXML());			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public String insertGatesNodeForEdit(RouteStaticConfig rsc) {
		String id = "_" + rsc.getId() + "_";
		Document doc = getDocumentByInputStream(xmlName);
		Element gates = doc.getRootElement().element("Network").element("Route").element("Static").
		addElement(id).addElement("Gates");
		gates.addAttribute("insert", "1");
		String gatesString = rsc.getGates();
		if(!StringUtils.equals(gatesString, "")){
			String[] gatesArray = gatesString.split(";");
			for (int i = 0; i<gatesArray.length; i++) {
				gates.addElement("_" + i + "_");
			}
		}
		String result = (String) executeXml(methodName, doc.asXML());
		return result;
	} */
	public String editRouteStaticConfig(RouteStaticConfig rsc) {
		Integer id = NumberUtils.toInt(rsc.getId()) ;
		//delGatesNodeForEdit(id);
		String ip = rsc.getIpTemp();
		Integer metric = NumberUtils.toInt(rsc.getMetricTemp());
		
		Integer gatePolicy = NumberUtils.toInt(rsc.getGatePolicy());
		String gatesString = rsc.getGates();
		String[] gatesArray = gatesString.split(";");
		Object[] gatesObj = new Object[gatesArray.length];
		int index = 0;
		if (gatesString != null) {
			for (int i = 0; i < gatesArray.length; i++) {
				String[] temp = gatesArray[index].split(",");
				String[] igArray = {"","",""};
				if(temp.length != 0){
					for(int j=0; j<temp.length; j++){
						igArray[j]=temp[j];
					}
				}					
				Hashtable gateTemp = new Hashtable();
				if ( igArray[0].equalsIgnoreCase("auto") ) {
					gateTemp.put("Auto", true);
				} else {
					gateTemp.put("IP", igArray[0]);
				}
				gateTemp.put("Dev", igArray[1]);
				gateTemp.put("Weight", NumberUtils.toInt(igArray[2]));
				gatesObj[i] = gateTemp;
					
				index++;
			}
		}									
		Hashtable editTemp = new Hashtable();
		editTemp.put("ID", id);
		editTemp.put("Net", ip);
		editTemp.put("Metric", metric);
		editTemp.put("GatePolicy", gatePolicy);
		editTemp.put("Gates", gatesObj);
		
		String flagStr = "false";
		try {
			Boolean editResult = (Boolean)executeXml("StaticRoute.Set", editTemp);
			
			Hashtable descriTemp = new Hashtable();
			descriTemp.put("ID", id);
			descriTemp.put("Description", rsc.getDescribe());			
			Boolean descriResult = (Boolean)executeXml("StaticRoute.Description", descriTemp);
			if ( editResult == true && descriResult == true ) {
				flagStr = "true";
			}
			/* String failedMess = getFailedMessage(id.toString(), editResult);			
			return failedMess; */			
		}catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}
	/* public String insertStaticNode() {
		Document doc = getDocumentByInputStream(xmlName);
		doc.getRootElement().element("Network").element("Route").element("Static").
		addAttribute("insert", "1").addElement("Node");
		String result = (String) executeXml(methodName, doc.asXML());
		//System.out.println(result);
		return result;
	}
	public String insertGatesNode(RouteStaticConfig rsc) {
		String xml = insertStaticNode();
		try {
			Document doc = DocumentHelper.parseText(xml);
			Element staticElement = doc.getRootElement().element("Network").element("Route").element("Static");
			Iterator si = staticElement.elementIterator();
			Element staticNode = (Element)si.next();
			Element gates = staticNode.addElement("Gates");
			gates.addAttribute("insert", "1");
			String gatesString = rsc.getGates();
			if(!StringUtils.equals(gatesString, "")){
				String[] gatesArray = gatesString.split(";");
				for (int i = 0; i<gatesArray.length; i++) {
					gates.addElement("_" + i + "_");
				}
			}
			String result = (String) executeXml(methodName, doc.asXML());
			//System.out.println(result);
			return result;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	} */
	
	/* public String getFailedMessage(String id, Boolean result) {
		String failedMess = new String();				
		if(result != null && result == true){
			return "";
		}
		else{
			try {
				failedMess += "路由" + id + ":";
				failedMess += "网关设置失败，或目的地址和掩码不匹配，";
				failedMess = failedMess.substring(0, failedMess.length()-1) + "。";								
				return failedMess;
			}catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	} */
	
	public String addRouteStaticConfig(RouteStaticConfig rsc) {										
		String ip = rsc.getIp();
		if (ip.equals("")) {
			ip = "0.0.0.0/0";
		}
		Integer metric = NumberUtils.toInt(rsc.getMetric());
		Integer gatePolicy = NumberUtils.toInt(rsc.getGatePolicy());
		String gatesString = rsc.getGates();
		String[] gatesArray = gatesString.split(";");
		Object[] gatesObj = new Object[gatesArray.length];
		int index = 0;
		if (gatesString != null) {
			for (int i = 0; i < gatesArray.length; i++) {
				String[] temp = gatesArray[index].split(",");
				String[] igArray = {"","",""};
				if(temp.length != 0){
					for(int j=0; j<temp.length; j++){
						igArray[j]=temp[j];
					}
				}
					
				Hashtable gateTemp = new Hashtable();
				if ( igArray[0].equalsIgnoreCase("auto") ) {
					gateTemp.put("Auto", true);
				} else {
					gateTemp.put("IP", igArray[0]);
				}
				gateTemp.put("Dev", igArray[1]);
				gateTemp.put("Weight", NumberUtils.toInt(igArray[2]));
				gatesObj[i] = gateTemp;
					
					/* Element e = (Element) i.next();
					if(!StringUtils.equals(igArray[0], "")){
						e.addElement("Gate").addAttribute("set", "1").addAttribute("value", igArray[0]);
						e.addElement("Weight").addAttribute("set", "1").addAttribute("value", igArray[2]);
					}
					else{
						if(!StringUtils.equals(igArray[1], "")){
							e.addElement("Interface").addAttribute("set", "1").addAttribute("value", igArray[1]);
							e.addElement("Weight").addAttribute("set", "1").addAttribute("value", igArray[2]);
						}
						else{
							e.addElement("Weight").addAttribute("set", "1").addAttribute("value", igArray[2]);
						}
					} */
				index++;
			}
		}
						
		Hashtable addTemp = new Hashtable();
		addTemp.put("Net", ip);
		addTemp.put("Metric", metric);
		addTemp.put("Description", rsc.getDescribe());
		addTemp.put("GatePolicy", gatePolicy);
		addTemp.put("Gates", gatesObj);	
		
		String flagStr = "false";
		try {
			Boolean addResult = (Boolean)executeXml("StaticRoute.Add",addTemp);
			System.out.println(addResult);
			if ( addResult != null && addResult == true ) {
				flagStr = "true";
			}
			/* Integer id = (Integer)executeXml("StaticRoute.GetCount");
			String idStr = id.toString();
			String failedMess = getFailedMessage(idStr, addResult);			
			return failedMess; */
		}catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}
	
	public RouteStaticConfig getRouteStaticConfigById(String id) {
		//id = "_" + id + "_";
		RouteStaticConfig ret = new RouteStaticConfig();
		
		Hashtable routeTemp = new Hashtable();
		routeTemp.put("Start", NumberUtils.toInt(id));
		routeTemp.put("Count", 1);
		try {						
			Object[] result = (Object[])executeXml("StaticRoute.Get",routeTemp);			
			if (result != null) {
				Map res = (Map)result[0];
				String net = (String)res.get("Net");
				Integer metric = (Integer)res.get("Metric");
				String descrip = (String)res.get("Description");
				Integer gatePolicy = (Integer)res.get("GatePolicy");
				Boolean status = (Boolean)res.get("Status");
				Object[] gates = (Object[])res.get("Gates");
				
				/* System.out.println(id);
				System.out.println(net);
				System.out.println(metric);
				System.out.println(descrip);
				System.out.println(gatePolicy);
				System.out.println(status);
				System.out.println(gates); */
				
				ret.setStatus(status.toString());
				ret.setIp(net);								
				ret.setMetric(metric.toString());
				ret.setDescribe(descrip);
				ret.setGatePolicy(gatePolicy.toString());
				
				String gatesString = "";
				String gatesForDisplay = "";
				if (gates != null) {
					for (int i = 0; i < gates.length; i++) {
						Map gate = (Map)gates[i];
						String ip = (String)gate.get("IP");
						String eth = (String)gate.get("Dev");
						Boolean auto = (Boolean)gate.get("Auto");
						Integer weight = (Integer)gate.get("Weight");
						Boolean gateStat = (Boolean)gate.get("Status");
						
						/* System.out.println(ip);
						System.out.println(eth);
						System.out.println(auto);
						System.out.println(weight);
						System.out.println(gateStat); */
						
						if ( auto == true ) {
							gatesForDisplay += ip + "自动,";
						} else {
							gatesForDisplay += ip + ",";
						}
						gatesForDisplay += eth + ",";
						gatesForDisplay += weight.toString() + ";";
						if ( auto == true ) {
							gatesString += "auto," + eth + "," + weight.toString() + ";";
						} else {
							gatesString += ip + "," + eth + "," + weight.toString() + ";";
						}																							
					}
				}
				ret.setGates(gatesString);
				ret.setDisplayGates(gatesForDisplay);					
			}
																						
			/* String gatesString = "";
			String gatesForDisplay = "";
			if (!gates.elements().isEmpty()) {
				for (Iterator i = gates.elementIterator(); i.hasNext();) {
					Element e = (Element) i.next();
					if(!StringUtils.equals(e.element("Interface").attributeValue("value"), "")){
						gatesForDisplay += e.element("RealGate").attributeValue("value") + ",";
						gatesForDisplay += e.element("Interface").attributeValue("value") + ",";
						gatesForDisplay += e.element("Weight").attributeValue("value") + ";";
						
						gatesString += "," + e.element("Interface").attributeValue("value") + 
						"," + e.element("Weight").attributeValue("value") + ";";
					}
					else{
						if(!StringUtils.equals(e.element("Gate").attributeValue("value"), "")){
							gatesForDisplay += e.element("Gate").attributeValue("value") + ",";
							gatesForDisplay += e.element("RealInterface").attributeValue("value") + ",";
							gatesForDisplay += e.element("Weight").attributeValue("value") + ";";
							
							gatesString += e.element("Gate").attributeValue("value") + 
							"," + "," + e.element("Weight").attributeValue("value") + ";";
						}
						else{
							gatesForDisplay += ",";
							gatesForDisplay += ",";
							gatesForDisplay += e.element("Weight").attributeValue("value") + ";";
							
							gatesString += "," + "," + e.element("Weight").attributeValue("value") + ";";
						}
					}
				}
			} */
						
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ret;
	}
}
