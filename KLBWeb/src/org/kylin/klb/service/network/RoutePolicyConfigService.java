package org.kylin.klb.service.network;

import java.util.ArrayList;
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
import org.kylin.klb.entity.network.RoutePolicyConfig;
import org.kylin.klb.entity.network.RouteStaticConfig;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.service.KlbClient;
import org.kylin.klb.util.Utils;

public class RoutePolicyConfigService extends KlbClient {
	private static String methodName = "Execute";
	private static String xmlName = "routepolicy.xml";
	private static String failedMessage;
	
	public List<Display> getProtocolList() {
		List<Display> ret = new ArrayList<Display>();
		Display display = new Display("", "-空-");
		ret.add(display);
		display = new Display("tcp", "TCP");
		ret.add(display);
		display = new Display("udp", "UDP");
		ret.add(display);
		display = new Display("other", "其他");
		ret.add(display);
		return ret;
	}
	
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
	/* public String delGatesNodeForEdit(String id) {
		Document doc = getDocumentByInputStream(xmlName);
		doc.getRootElement().element("Network").element("Route").
		element("Policy").addElement(id).addElement("Gates").addAttribute("get", "1");
		doc.getRootElement().element("Network").element("Route").
		element("Policy").element(id).addElement("Rule").addAttribute("get", "1");
		String result = (String) executeXml(methodName, doc.asXML());
		try {
			doc = DocumentHelper.parseText(result);
			//System.out.println(doc.asXML());
			doc.getRootElement().element("Network").element("Route").
			element("Policy").element(id).element("Gates").addAttribute("remove", "1");
			doc.getRootElement().element("Network").element("Route").
			element("Policy").element(id).element("Rule").addAttribute("remove", "1");
			//System.out.println(doc.asXML());
			result = (String) executeXml(methodName, doc.asXML());
			//System.out.println(result);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public String insertGatesNodeForEdit(RoutePolicyConfig rpc) {
		String id = "_" + rpc.getId() + "_";
		Document doc = getDocumentByInputStream(xmlName);
		Element gates = doc.getRootElement().element("Network").element("Route").element("Policy").
		addElement(id).addElement("Gates");
		gates.addAttribute("insert", "1");
		String gatesString = rpc.getGates();
		if(!StringUtils.equals(gatesString, "")){
			String[] gatesArray = gatesString.split(";");
			for (int i = 0; i<gatesArray.length; i++) {
				gates.addElement("_" + i + "_");
			}
		}
		
		Element rules = doc.getRootElement().element("Network").element("Route").element("Policy").
		element(id).addElement("Rule");
		rules.addAttribute("insert", "1");
		String rulesString = rpc.getRules();
		if(!StringUtils.equals(rulesString, "")){
			String[] rulesArray = rulesString.split(";");
			for (int i = 0; i<rulesArray.length; i++) {
				rules.addElement("_" + i + "_");
			}
		}
		//System.out.println(doc.asXML());
		String result = (String) executeXml(methodName, doc.asXML());
		//System.out.println(result);
		return result;
	} */
	
	public String editRoutePolicyConfig(RoutePolicyConfig rpc) {
		Integer id = NumberUtils.toInt(rpc.getId());								
		
		Integer gatePolicy = NumberUtils.toInt(rpc.getGatePolicy());
		String gatesString = rpc.getGates();
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
		editTemp.put("GatePolicy", gatePolicy);
		editTemp.put("Gates", gatesObj);
		
		String flagStr = "false";
		try {
			Boolean editResult = (Boolean)executeXml("PolicyRoute.Set", editTemp);
			System.out.println(editResult);
			
			Hashtable descriTemp = new Hashtable();
			descriTemp.put("ID", id);
			descriTemp.put("Description", rpc.getDescribe());
			Boolean descriResult = (Boolean)executeXml("PolicyRoute.Description", descriTemp);

			Hashtable routeTemp = new Hashtable();
			routeTemp.put("Start", id);
			routeTemp.put("Count", 1);
			Object[] result = (Object[])executeXml("PolicyRoute.Get",routeTemp);
						
			if ( result != null && result.length != 0 ) {
				Map res = (Map)result[0];
				Object[] rules = (Object[])res.get("Rules");
				Object[] rulesId = new Object[rules.length];
				if (rules != null) {
					for (int i = 0; i < rules.length; i++) {
						Hashtable ruleIdTemp = new Hashtable();						
						ruleIdTemp.put("ID", i);
						rulesId[i] = ruleIdTemp;
					}
				}
				if ( rulesId != null && rulesId.length != 0 ) {
					Hashtable delTemp = new Hashtable();
					delTemp.put("ID", id);
					delTemp.put("Rules", rulesId);
					Boolean delResult = (Boolean)executeXml("PolicyRoute.DeleteRule",delTemp);
					System.out.println(delResult);
				}								
			}
									
			Hashtable addRuleTemp = new Hashtable();
			addRuleTemp.put("ID", id);
			Boolean addRuleResult = null;
			String rulesString = rpc.getRules();
			String[] rulesArray = rulesString.split(";");
			Object[] rulesObj = new Object[rulesArray.length];
			int indexR = 0;
			if ( rulesString != null && !rulesString.equals("") ) {
				for (int i = 0; i < rulesArray.length; i++) {
					String[] temp = rulesArray[indexR].split(",");
					String[] netArray = {"","","","",""};
					if(temp.length != 0){
						for(int j=0; j<temp.length; j++){
							netArray[j]=temp[j];
						}
					}					
					Hashtable ruleTemp = new Hashtable();					
					ruleTemp.put("SrcNet", netArray[0]);
					ruleTemp.put("DestNet", netArray[1]);
					ruleTemp.put("ProtocolStr", netArray[2]);
					ruleTemp.put("SrcPortStr", netArray[3]);
					ruleTemp.put("DestPortStr", netArray[4]);
					rulesObj[i] = ruleTemp;
					
					indexR++;
				}
				addRuleTemp.put("Rules", rulesObj);
				addRuleResult = (Boolean)executeXml("PolicyRoute.AddRule",addRuleTemp);
			}
			
			if ( editResult != null && editResult == true ) {
				flagStr = "true";
			}
		}catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}
	
	public String getFailedMessage(String id, Boolean result) {
		String failedMess = new String();
		
		//String xml = result;
		/* if(!xml.contains("failed")){
			return "";
		} */
		if(result != null && result == true){
			return "";
		}
		else{
			try {
				/* Document doc = DocumentHelper.parseText(xml);
				Element policyNode = doc.getRootElement().element("Network").element("Route").element("Policy").element(id);
				Element rule = policyNode.element("Rule");
				if (!rule.elements().isEmpty()) {
					for (Iterator i = rule.elementIterator(); i.hasNext();) {
						Element e = (Element) i.next();
						if(e.asXML().contains("failed")){
							failedMess += "规则" + e.getName().substring(1, e.getName().length()-1);
							if(e.element("SrcNet").attributeValue("failed")!=null){
								failedMess += "源地址设置失败，";
							}
							if(e.element("DestNet").attributeValue("failed")!=null){
								failedMess += "目的地址设置失败，";
							}
							if(e.element("Protocol").attributeValue("failed")!=null){
								failedMess += "协议设置失败，";
							}
							if(e.element("SrcPort").attributeValue("failed")!=null){
								failedMess += "源端口设置失败，";
							}
							if(e.element("DestPort").attributeValue("failed")!=null){
								failedMess += "目的端口设置失败，";
							}
							if(!StringUtils.equals(failedMess, "")){
								failedMess = failedMess.substring(0, failedMess.length()-1) + "。";
							}
							failedMess += "\n";
						}
					}
				} */
				failedMess += "路由" + id + ":";								
				failedMess += "网关设置失败。";
				//failedMess = failedMess.substring(0, failedMess.length()-1) + "。";
				
				/* Element gates = policyNode.element("Gates");
				if (!gates.elements().isEmpty()) {
					for (Iterator i = gates.elementIterator(); i.hasNext();) {
						Element e = (Element) i.next();
						if(e.asXML().contains("failed")){
							failedMess += "网关" + e.getName().substring(1, e.getName().length()-1);
							if(e.element("Gate").attributeValue("failed")!=null){
								failedMess += "网关地址设置失败，";
							}
							if(e.element("Interface").attributeValue("failed")!=null){
								failedMess += "接口设置失败，";
							}
							if(e.element("Weight").attributeValue("failed")!=null){
								failedMess += "权重设置失败，";
							}
							if(!StringUtils.equals(failedMess, "")){
								failedMess = failedMess.substring(0, failedMess.length()-1) + "。";
							}
							failedMess += "\n";
						}
					}
				} */
				return failedMess;
			}catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	public String getFailedMessage1(String id, Boolean result) {
		String failedMess = new String();		
		if(result != null && result == true){
			return "";
		}
		else{
			try {
				failedMess += "路由" + id + ":";								
				failedMess += "路由规则设置失败（可能是地址IP和掩码不匹配，或协议和端口不匹配等），";
				failedMess = failedMess.substring(0, failedMess.length()-1) + "。";
								
				return failedMess;
			}catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	/* public String addPolicyNode() {
		Document doc = getDocumentByInputStream(xmlName);
		doc.getRootElement().element("Network").element("Route").element("Policy").
		addAttribute("insert", "1").addElement("Node");
		String result = (String) executeXml(methodName, doc.asXML());
		//System.out.println(result);
		return result;
	}
	public String addPolicyNode(String id) {
		Document doc = getDocumentByInputStream(xmlName);
		doc.getRootElement().element("Network").element("Route").element("Policy").
		addAttribute("insert", "1").addAttribute("index", id).addElement("Node");
		String result = (String) executeXml(methodName, doc.asXML());
		//System.out.println(result);
		return result;
	}
	public String addGatesAndRulesNode(RoutePolicyConfig rpc) {
		String xml = addPolicyNode();
		try {
			Document doc = DocumentHelper.parseText(xml);
			Element policyElement = doc.getRootElement().element("Network").element("Route").
			element("Policy");
			Iterator si = policyElement.elementIterator();
			Element policyNode = (Element)si.next();
			Element rules = policyNode.addElement("Rule");
			rules.addAttribute("insert", "1");
			String rulesString = rpc.getRules();
			
			if(!StringUtils.equals(rulesString, "")){
				String[] rulesArray = rulesString.split(";");
				for (int i = 0; i<rulesArray.length; i++) {
					rules.addElement("_" + i + "_");
				}
			}
			
			Element gates = policyNode.addElement("Gates");
			gates.addAttribute("insert", "1");
			String gatesString = rpc.getGates();
			if(!StringUtils.equals(gatesString, "")){
				String[] gatesArray = gatesString.split(";");
				for (int i = 0; i<gatesArray.length; i++) {
					gates.addElement("_" + i + "_");
				}
			}
			
			String result = (String) executeXml(methodName, doc.asXML());
			return result;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public String addGatesAndRulesNode(String id, RoutePolicyConfig rpc) {
		String xml = addPolicyNode(id);
		try {
			Document doc = DocumentHelper.parseText(xml);
			Element policyElement = doc.getRootElement().element("Network").element("Route").
			element("Policy");
			Iterator si = policyElement.elementIterator();
			Element policyNode = (Element)si.next();
			Element rules = policyNode.addElement("Rule");
			rules.addAttribute("insert", "1");
			String rulesString = rpc.getRules();
			if(!StringUtils.equals(rulesString, "")){
				String[] rulesArray = rulesString.split(";");
				for (int i = 0; i<rulesArray.length; i++) {
					rules.addElement("_" + i + "_");
				}
			}			
			Element gates = policyNode.addElement("Gates");
			gates.addAttribute("insert", "1");
			String gatesString = rpc.getGates();
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
	
	public String addRoutePolicyConfig(RoutePolicyConfig rpc) {						
		//String xml = addGatesAndRulesNode(rpc);		
		Integer gatePolicy = NumberUtils.toInt(rpc.getGatePolicy());			
		String gatesString = rpc.getGates();
		String[] gatesArray = gatesString.split(";");
		Object[] gatesObj = new Object[gatesArray.length];
		//int index = 0;
		if (gatesString != null) {
			for (int i = 0; i < gatesArray.length; i++) {
				String[] temp = gatesArray[i].split(",");
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
				//index++;
			}
		}						
		Hashtable addTemp = new Hashtable();
		addTemp.put("Description", rpc.getDescribe());
		addTemp.put("GatePolicy", gatePolicy);
		addTemp.put("Gates", gatesObj);	
		
		String flagStr = "false";
		try {
			Boolean addResult = (Boolean)executeXml("PolicyRoute.Add", addTemp);
			System.out.println(addResult);
						
			Integer id = (Integer)executeXml("PolicyRoute.GetCount");
			Hashtable addRuleTemp = new Hashtable();
			addRuleTemp.put("ID", id-1);
			Boolean addRuleResult = null;
			String rulesString = rpc.getRules();
			//System.out.println(rulesString + ",,,");
			String[] rulesArray = rulesString.split(";");
			Object[] rulesObj = new Object[rulesArray.length];
			//int indexR = 0;
			if ( rulesString != null && !rulesString.equals("") ) {
				for (int i = 0; i < rulesArray.length; i++) {
					String[] temp = rulesArray[i].split(",");
					String[] netArray = {"","","","",""};
					if(temp.length != 0){
						for(int j=0; j<temp.length; j++){
							netArray[j]=temp[j];
						}
					}
					//System.out.println(netArray[0] + ",,,");
					Hashtable ruleTemp = new Hashtable();
					
					ruleTemp.put("SrcNet", netArray[0]);
					ruleTemp.put("DestNet", netArray[1]);
					ruleTemp.put("ProtocolStr", netArray[2]);
					ruleTemp.put("SrcPortStr", netArray[3]);
					ruleTemp.put("DestPortStr", netArray[4]);
					rulesObj[i] = ruleTemp;					
					//indexR++;
				}
				addRuleTemp.put("Rules", rulesObj);	
				addRuleResult = (Boolean)executeXml("PolicyRoute.AddRule",addRuleTemp);
			}
			
			if ( addResult != null && addResult == true ) {
				flagStr = "true";
			}
			//System.out.println(addRuleResult + ",,");									
			/* String idStr = id.toString();
			String failedMess = getFailedMessage(idStr, addResult) + getFailedMessage1(idStr, addRuleResult);
			return failedMess; */
		}catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}
	public String insertRoutePolicyConfig(String id, RoutePolicyConfig rpc) {
		//String xml = addGatesAndRulesNode(id, rpc);		
		Integer gatePolicy = NumberUtils.toInt(rpc.getGatePolicy());
		String gatesString = rpc.getGates();
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
						
		Hashtable insertTemp = new Hashtable();
		insertTemp.put("ID", NumberUtils.toInt(id));
		insertTemp.put("GatePolicy", gatePolicy);
		insertTemp.put("Gates", gatesObj);
		
		String flagStr = "false";
		try {
			Boolean insertResult = (Boolean)executeXml("PolicyRoute.Insert",insertTemp);
			System.out.println(insertResult);
			
			Hashtable descriTemp = new Hashtable();
			descriTemp.put("ID", NumberUtils.toInt(id));
			descriTemp.put("Description", rpc.getDescribe());
			Boolean descriResult = (Boolean)executeXml("PolicyRoute.Description", descriTemp);
						
			Hashtable addRuleTemp = new Hashtable();
			addRuleTemp.put("ID", NumberUtils.toInt(id));
			Boolean addRuleResult = null;
			String rulesString = rpc.getRules();
			String[] rulesArray = rulesString.split(";");
			Object[] rulesObj = new Object[rulesArray.length];
			int indexR = 0;
			if ( rulesString != null && !rulesString.equals("") ) {
				for (int i = 0; i < rulesArray.length; i++) {
					String[] temp = rulesArray[indexR].split(",");
					String[] netArray = {"","","","",""};
					if(temp.length != 0){
						for(int j=0; j<temp.length; j++){
							netArray[j]=temp[j];
						}
					}					
					Hashtable ruleTemp = new Hashtable();
					
					ruleTemp.put("SrcNet", netArray[0]);
					ruleTemp.put("DestNet", netArray[1]);
					ruleTemp.put("ProtocolStr", netArray[2]);
					ruleTemp.put("SrcPortStr", netArray[3]);
					ruleTemp.put("DestPortStr", netArray[4]);
					rulesObj[i] = ruleTemp;
					
					indexR++;
				}
				addRuleTemp.put("Rules", rulesObj);
				addRuleResult = (Boolean)executeXml("PolicyRoute.AddRule",addRuleTemp);
			}
			
			if ( insertResult != null && insertResult == true ) {
				flagStr = "true";
			}			
		}catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}
	public RoutePolicyConfig getRoutePolicyConfigById(String id) {
		
		RoutePolicyConfig ret = new RoutePolicyConfig();
							
		try {
			
			Hashtable routeTemp = new Hashtable();
			routeTemp.put("Start", NumberUtils.toInt(id));
			routeTemp.put("Count", 1);
			Object[] result = (Object[])executeXml("PolicyRoute.Get",routeTemp);
			
			if (result != null) {
				Map res = (Map)result[0];				
				String descrip = (String)res.get("Description");
				Integer gatePolicy = (Integer)res.get("GatePolicy");
				Boolean status = (Boolean)res.get("Status");
				Object[] gates = (Object[])res.get("Gates");
				Object[] rules = (Object[])res.get("Rules");
				
				/* System.out.println(id);				
				System.out.println(descrip);
				System.out.println(gatePolicy);
				System.out.println(status);
				System.out.println(gates);
				System.out.println(rules); */
				
				ret.setDescribe(descrip);
				ret.setGatePolicy(gatePolicy.toString());
				
				String rulesString = "";
				if (rules != null) {
					for (int i = 0; i < rules.length; i++) {						
						Map rule = (Map)rules[i];
						Integer ruleId = (Integer)rule.get("ID");
						String srcNet = (String)rule.get("SrcNet");
						String destNet = (String)rule.get("DestNet");
						Integer protocol = (Integer)rule.get("Protocol");
						String protocolStr = (String)rule.get("ProtocolStr");
						Integer srcPort = (Integer)rule.get("SrcPort");
						String srcPortStr = (String)rule.get("SrcPortStr");
						Integer destPort = (Integer)rule.get("DestPort");
						String destPortStr = (String)rule.get("DestPortStr");
						
						/* System.out.println(ruleId);
						System.out.println(srcNet);
						System.out.println(destNet);
						System.out.println(protocol);
						System.out.println(protocolStr);
						System.out.println(srcPort);
						System.out.println(srcPortStr);
						System.out.println(destPort);
						System.out.println(destPortStr); */
						
						rulesString += srcNet + ",";
						rulesString += destNet + ",";
						rulesString += protocolStr + ",";
						rulesString += srcPortStr + ",";
						rulesString += destPortStr + ";";
						
					}
				}
				ret.setRules(rulesString);
				
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
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ret;
	}
}
