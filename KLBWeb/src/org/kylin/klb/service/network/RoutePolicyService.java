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
import org.kylin.klb.entity.network.RoutePolicy;
import org.kylin.klb.entity.network.RouteStatic;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.service.KlbClient;

public class RoutePolicyService extends KlbClient {
	private static String methodName = "Execute";
	
	public boolean move(String oldId, String newId) throws XmlRpcException {
		boolean status = false;
		String xmlName = "routepolicy.xml";
		Document doc = getDocumentByInputStream(xmlName);
		Element policy = doc.getRootElement().element("Network").element("Route").element("Policy");

		policy.addAttribute("move", "1").addAttribute("to", newId).addElement("_" + oldId + "_");
		// System.out.println(doc.asXML());
		String result = (String) executeXml(methodName, doc.asXML());
		// System.out.println(result);
		if (result != null && !result.contains("failed")) {
			status = true;
		}
		return status;
	}
	
	/* public String constructXmlForRouteInfo() {
		String xmlName = "routepolicy.xml";
		Document doc = getDocumentByInputStream(xmlName);
		doc.getRootElement().element("Network").element("Route").element("Policy").addAttribute("get", "1");
		String result = (String) executeXml(methodName, doc.asXML());
		try {
			doc = DocumentHelper.parseText(result);
			Element policy = doc.getRootElement().element("Network").element("Route").element("Policy");
			if (!policy.elements().isEmpty()) {
				for (Iterator i = policy.elementIterator(); i.hasNext();) {
					Element e = (Element) i.next();
					e.addElement("Gates").addAttribute("get", "1");
					e.addElement("Rule").addAttribute("get", "1");
				}
			}
			result = (String) executeXml(methodName, doc.asXML());
			doc = DocumentHelper.parseText(result);
			policy = doc.getRootElement().element("Network").element("Route").element("Policy");
			if (!policy.elements().isEmpty()) {
				for (Iterator i = policy.elementIterator(); i.hasNext();) {
					Element e = (Element) i.next();
					e.addElement("Enabled").addAttribute("get", "1");
					e.addElement("Descrition").addAttribute("get", "1");
					Element gates = e.element("Gates");
					if (!gates.elements().isEmpty()) {
						for (Iterator ii = gates.elementIterator(); ii.hasNext();) {
							Element ee = (Element) ii.next();
							ee.addElement("Gate").addAttribute("get", "1");
							ee.addElement("Interface").addAttribute("get", "1");
							ee.addElement("RealGate").addAttribute("get", "1");
							ee.addElement("RealInterface").addAttribute("get", "1");
							ee.addElement("Weight").addAttribute("get", "1");
						}
					}
					Element rule = e.element("Rule");
					if (!rule.elements().isEmpty()) {
						for (Iterator ii = rule.elementIterator(); ii.hasNext();) {
							Element ee = (Element) ii.next();
							ee.addElement("DestNet").addAttribute("get", "1");
							ee.addElement("DestPort").addAttribute("get", "1");
							ee.addElement("Protocol").addAttribute("get", "1");
							ee.addElement("SrcNet").addAttribute("get", "1");
							ee.addElement("SrcPort").addAttribute("get", "1");
						}
					}
				}
			}
			return doc.asXML();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	} */
	
	public List<RoutePolicy> getRoutePolicyInfoList(){
		List<RoutePolicy> ret = new ArrayList<RoutePolicy>();
		RoutePolicy rp = new RoutePolicy();
						
		try {			
			Hashtable routeTemp = new Hashtable();
			routeTemp.put("All", true);
			Object[] result = (Object[])executeXml("PolicyRoute.Get",routeTemp);
						
			if (result != null) {
				for (int i = 0; i < result.length; i++) {
						
					Map res = (Map)result[i];
					Integer id = (Integer)res.get("ID");
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
					
					rp.setId(id.toString());
					rp.setStatus(status.toString());
					
					String destN = "";
					String destP = "";
					String proto = "";
					String srcN = "";
					String srcP = "";
					if (rules != null) {
						for (int j = 0; j < rules.length; j++) {
							Map rule = (Map)rules[j];
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
																									
							destN += destNet + "<br>";
							destP += destPortStr + "<br>";
							proto += protocolStr + "<br>";
							srcN += srcNet + "<br>";
							srcP += srcPortStr + "<br>";
						}
					}
					
					rp.setDestNet(destN);
					rp.setDestPort(destP);
					rp.setProtocol(proto);
					rp.setSrcNet(srcN);
					rp.setSrcPort(srcP);
					
					String gat = "";
					String inter = "";
					String weigh = "";
					if (gates != null) {
						for (int j = 0; j < gates.length; j++) {
							Map gate = (Map)gates[j];
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
								gat += ip + "自动<br>";
							} else {
								gat += ip + "<br>";
							}							
							inter += eth + "<br>";
							weigh += weight.toString() + "<br>";
																					             
							/* if(!StringUtils.equals(ee.element("RealGate").attributeValue("value"), "")){
								gate += ee.element("RealGate").attributeValue("value");
								inter += ee.element("Interface").attributeValue("value");
							}
							if(!StringUtils.equals(ee.element("RealInterface").attributeValue("value"), "")){
								inter += ee.element("RealInterface").attributeValue("value");
								gate += ee.element("Gate").attributeValue("value");
							}
							weigh += ee.element("Weight").attributeValue("value") + "<br>";
							gat += "<br>";
							inter += "<br>"; */
						}
					}
					rp.setGate(gat);
					rp.setInter(inter);
					rp.setWeight(weigh);
					ret.add(rp);
					rp = new RoutePolicy();
				}
			}
			
			/* if (!policy.elements().isEmpty()) {
				for (Iterator i = policy.elementIterator(); i.hasNext();) {
					Element e = (Element) i.next();
					
					String id = e.getName();
					id = id.substring(1, id.length() - 1);
					//
				}
			} */
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		//System.out.println(result);
		return ret;
	}
	
	/* public void editRouteStatus(String id, String status){
		id = "_" + id + "_";
		String xmlName = "routepolicy.xml";
		Document doc = getDocumentByInputStream(xmlName);
		Element route = doc.getRootElement().element("Network").element("Route").element("Policy");
		route.addElement(id).addElement("Enabled").addAttribute("set", "1").addAttribute("value", status);
		String result = (String) executeXml(methodName, doc.asXML());
	} */
	public Boolean delRouteById(String id) {		
		boolean flag = false;
		try {
			Hashtable delTemp = new Hashtable();
			delTemp.put("ID", NumberUtils.toInt(id));
			Boolean delResult = (Boolean)executeXml("PolicyRoute.Delete",delTemp);
			System.out.println(delResult);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;				
	}
}
