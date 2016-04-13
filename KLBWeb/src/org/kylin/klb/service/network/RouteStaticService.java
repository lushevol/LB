package org.kylin.klb.service.network;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.xmlrpc.XmlRpcException;
import org.kylin.klb.entity.network.RouteStatic;
import org.kylin.klb.service.KlbClient;

public class RouteStaticService extends KlbClient {
	//private static String methodName = "Execute";
	
	/* public List<Display> getInterfaceList(){
		List<Display> ret = new ArrayList<Display>();
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
		return ret;
	} */
	
	/* public List<String> getRouteStaticIds() {
		List<String> routeIds = new ArrayList<String>();
		String xmlName = "routestatic.xml";
		Document doc = getDocumentByInputStream(xmlName);
		doc.getRootElement().element("Network").element("Route").element("Static").addAttribute("get", "1");
		String result = (String) executeXml(methodName, doc.asXML());
		try {
			Document document = DocumentHelper.parseText(result);
			Element route = document.getRootElement().element("Network").element("Route").element("Static");
			if (!route.elements().isEmpty()) {
				for (Iterator i = route.elementIterator(); i.hasNext();) {
					Element e = (Element) i.next();
					routeIds.add(e.getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return routeIds;
	}

	public String constructXmlForRouteInfo() {
		String xmlName = "routestatic.xml";
		Document doc = getDocumentByInputStream(xmlName);
		Element route = doc.getRootElement().element("Network").element("Route").element("Static");
		List<String> routeIds = getRouteStaticIds();
		if (routeIds == null) {
			return null;
		}
		Iterator<String> i = routeIds.iterator();
		while (i.hasNext()) {
			String routeId = i.next();
			route.addElement(routeId);
			route.element(routeId).addElement("Gates").addAttribute("get", "1");
		}
		String result = (String) executeXml(methodName, doc.asXML());
		try {
			Document document = DocumentHelper.parseText(result);
			route = document.getRootElement().element("Network").element("Route").element("Static");
			if (!route.elements().isEmpty()) {
				for (Iterator ii = route.elementIterator(); ii.hasNext();) {
					Element e = (Element) ii.next();
					e.addElement("Enabled").addAttribute("get", "1");
					e.addElement("Net").addAttribute("get", "1");
					if (!e.element("Gates").elements().isEmpty()) {
						for (Iterator iii = e.element("Gates").elementIterator(); iii.hasNext();) {
							Element ee = (Element) iii.next();
							ee.addElement("Gate").addAttribute("get", "1");
							ee.addElement("Interface").addAttribute("get", "1");
							ee.addElement("RealGate").addAttribute("get", "1");
							ee.addElement("RealInterface").addAttribute("get", "1");
							ee.addElement("Weight").addAttribute("get", "1");
						}
					}
				}
			}
			return document.asXML();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	} */
	
	public List<RouteStatic> getRouteStaticInfoList(){
		List<RouteStatic> rsl = new ArrayList<RouteStatic>();
		RouteStatic rs = new RouteStatic();
				
		try {			
			Hashtable routeTemp = new Hashtable();
			routeTemp.put("All", true);
			Object[] result = (Object[])executeXml("StaticRoute.Get",routeTemp);
						
			if (result != null) {									
				for (int i = 0; i < result.length; i++) {
						
					Map res = (Map)result[i];
					Integer id = (Integer)res.get("ID");
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
					System.out.println(gates);	*/
										
					rs.setId(id.toString());					
					rs.setStatus(status.toString());
					
					if(StringUtils.equals(net, "")){
						rs.setIp("");
						rs.setMask("");
						//rs.setMetric("");
					}
					else{
						String[] imArray = net.split("/");
						//String[] mmArray = imArray[1].split("-");
						rs.setIp(imArray[0]);
						rs.setMask(imArray[1]);						
					}
					rs.setMetric(metric.toString());
					
					//单线路网关时静态路由表的策略显示为空
					if ( gates.length == 0 || gates.length == 1 ) {
						rs.setGatePolicy("无");
					} else {
						rs.setGatePolicy(gatePolicy.toString());
					}
					
					String gat = "";
					String inter = "";
					String weigh = "";
					if (gates != null && gates.length != 0) {
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
													
						}
						rs.setGate(gat);
						rs.setInter(inter);
						rs.setWeight(weigh);
																		
						/* for (Iterator iii = e.element("Gates").elementIterator(); iii.hasNext();) {
							Element ee = (Element) iii.next();
							if(!StringUtils.equals(ee.element("RealGate").attributeValue("value"), "")){
								gat += ee.element("RealGate").attributeValue("value");
								inter += ee.element("Interface").attributeValue("value");
							}
							if(!StringUtils.equals(ee.element("RealInterface").attributeValue("value"), "")){
								inter += ee.element("RealInterface").attributeValue("value");
								gat += ee.element("Gate").attributeValue("value");
							}
							weigh += ee.element("Weight").attributeValue("value") + "<br>";
							gat += "<br>";
							inter += "<br>";
						} */
					}
					
					rsl.add(rs);
					rs = new RouteStatic();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		//System.out.println(result);
		return rsl;
	}
	
	/* public void editRouteStatus(String id, String status){
		id = "_" + id + "_";
		String xmlName = "routestatic.xml";
		Document doc = getDocumentByInputStream(xmlName);
		Element route = doc.getRootElement().element("Network").element("Route").element("Static");
		route.addElement(id).addElement("Enabled").addAttribute("set", "1").addAttribute("value", status);
		String result = (String) executeXml(methodName, doc.asXML());
	} */
	
	public Boolean delRouteById(String id) {		
		boolean flag = false;
		try {
			Hashtable delTemp = new Hashtable();
			delTemp.put("ID", NumberUtils.toInt(id));
			Boolean delResult = (Boolean)executeXml("StaticRoute.Delete",delTemp);
			System.out.println(delResult);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;						
	}
}
