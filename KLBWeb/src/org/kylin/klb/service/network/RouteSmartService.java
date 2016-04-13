package org.kylin.klb.service.network;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.kylin.klb.entity.network.RouteSmart;
import org.kylin.klb.entity.network.RouteSmartConfig;
import org.kylin.klb.service.KlbClient;

public class RouteSmartService extends KlbClient {	
	
	private RouteSmartConfigService routeSmartConfigService = new RouteSmartConfigService();
	
	public List<RouteSmart> getRouteSmartInfoList(){
		List<RouteSmart> ret = new ArrayList<RouteSmart>();
		RouteSmart rs = new RouteSmart();
		
		Hashtable routeTemp = new Hashtable();
		routeTemp.put("All", true);				
		try {
			Object[] result = (Object[])executeXml("SmartRoute.Get",routeTemp);
						
			if (result != null) {
				for (int i = 0; i < result.length; i++) {
						
					Map res = (Map)result[i];
					Integer id = (Integer)res.get("ID");
					String descrip = (String)res.get("Description");
					Integer gatePolicy = (Integer)res.get("GatePolicy");
					Boolean status = (Boolean)res.get("Status");
					Integer isp = (Integer)res.get("ISP");
					Object[] gates = (Object[])res.get("Gates");					
					
					/* System.out.println(id);
					System.out.println(descrip);
					System.out.println(gatePolicy);
					System.out.println(status);
					System.out.println(isp);
					System.out.println(gates); */
					
					String ispName = "";
					if ( isp != 0 ) {
						Hashtable DnsIspTemp = new Hashtable();
						DnsIspTemp.put("ID", isp);			
						Object ispResult = (Object)executeXml("ISP.Get.Item.Name", DnsIspTemp);			
						if (ispResult != null) {
							Map ispRes = (Map)ispResult;
							ispName = (String)ispRes.get("Name");
						}																		
					} else {
						ispName = "默认";
					}
					rs.setIsp(ispName);					
					rs.setId(id.toString());
					rs.setDescribe(descrip);
					rs.setStatus(status.toString());					
										
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
						}
					}
					rs.setGate(gat);
					rs.setInter(inter);
					rs.setWeight(weigh);
					ret.add(rs);
					rs = new RouteSmart();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		//System.out.println(result);
		return ret;
	}
	
	public List<RouteSmartConfig> getRouteSmartList(){
		List<RouteSmartConfig> ret = new ArrayList<RouteSmartConfig>();
		RouteSmartConfig rs = new RouteSmartConfig();
		
		Hashtable routeTemp = new Hashtable();
		routeTemp.put("All", true);				
		try {
			Object[] result = (Object[])executeXml("SmartRoute.Get",routeTemp);
						
			if (result != null) {
				for (int i = 0; i < result.length; i++) {
						
					Map res = (Map)result[0];				
					String descrip = (String)res.get("Description");
					Integer gatePolicy = (Integer)res.get("GatePolicy");
					Boolean status = (Boolean)res.get("Status");
					Integer isp = (Integer)res.get("ISP");
					Object[] gates = (Object[])res.get("Gates");				
					
					/* System.out.println(id);				
					System.out.println(descrip);
					System.out.println(gatePolicy);
					System.out.println(status);
					System.out.println(gates);
					System.out.println(rules); */
					
					rs.setDescribe(descrip);
					rs.setIspName(isp.toString());
					rs.setGatePolicy(gatePolicy.toString());				
					
					String gatesString = "";
					String gatesForDisplay = "";
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
					rs.setGates(gatesString);
					rs.setDisplayGates(gatesForDisplay);		
					
					/*add by yjp 2012/02/28*/
					String mode = res.get("Mode").toString();
					rs.setMode(mode);
					
					String modeName = routeSmartConfigService.getModeById(mode);
					rs.setModeName(modeName);
					
					String ip = res.get("Ip").toString();
					rs.setIp(ip);
					
					String port = res.get("Port").toString();
					rs.setPort(port);
					
					String frequcency = res.get("Frequcency").toString();
					rs.setFrequcency(frequcency);
					
					
					String timeout = res.get("Timeout").toString();
					rs.setTimeout(timeout);
					
					ret.add(rs);
					rs = new RouteSmartConfig();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		//System.out.println(result);
		return ret;
	}
	
	
	public Boolean delRouteById (String id) {		
		boolean flag = false;
		try {
			Hashtable delTemp = new Hashtable();
			delTemp.put("ID", NumberUtils.toInt(id));
			Boolean delResult = (Boolean)executeXml("SmartRoute.Delete",delTemp);
			System.out.println(delResult);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;				
	}
}