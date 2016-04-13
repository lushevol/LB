package org.kylin.klb.service.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.xmlrpc.XmlRpcException;
import org.kylin.klb.entity.network.RouteSmartConfig;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.service.KlbClient;
import org.kylin.klb.util.Utils;

public class RouteSmartConfigService extends KlbClient {
	
	public List<Display> getIspNameList(){		
		List<Display> ret = new ArrayList<Display>();		
		try {			
			Object[] result = (Object[])executeXml("ISP.Get.List.Name");			
			if (result != null) {				
				for (int i = 0; i < result.length; i++) {
					Map res = (Map)result[i];
					Integer id = (Integer)res.get("ID");
					String name = (String)res.get("Name");
										
					Display ispName = new Display(id.toString(), name);
					ret.add(ispName);
				}
			}									
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	
	private Map<String, String> getModeMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("0", "disable");
		map.put("1", "ping");
		map.put("2", "tcp");
		map.put("3", "udp");
		return map;
	}

	public String getModeById(String type) {
		Map<String, String> map = this.getModeMap();
		if (map.containsKey(type)) {
			return map.get(type);
		}
		return "未知检测方式";
	}

	public List<Display> getModeList() {
		Map<String, String> map = this.getModeMap();
		List<Display> list = new ArrayList<Display>();
		for (String string : map.keySet()) {
			Display display = new Display(map.get(string), string);
			list.add(display);
		}
		return list;
	}
	
	public String editRouteSmartConfig(RouteSmartConfig rsc) {
		Integer id = NumberUtils.toInt(rsc.getId());								
		Integer isp = NumberUtils.toInt(rsc.getIspName());
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
		editTemp.put("ISP", isp);
		editTemp.put("GatePolicy", gatePolicy);
		editTemp.put("Gates", gatesObj);
		
		String flagStr = "false";
		try {
			Boolean editResult = (Boolean)executeXml("SmartRoute.Set", editTemp);
			//System.out.println(editResult);
			
			Hashtable descriTemp = new Hashtable();
			descriTemp.put("ID", id);
			descriTemp.put("Description", rsc.getDescribe());
			Boolean descriResult = (Boolean)executeXml("SmartRoute.Description", descriTemp);
			
			Hashtable checkTemp = new Hashtable();
			checkTemp.put("ID", id);
			if(StringUtils.isNotEmpty(rsc.getMode())){
				checkTemp.put("Mode", NumberUtils.toInt(rsc.getMode()));
			}			
			if(StringUtils.isNotEmpty(rsc.getIp())){
				checkTemp.put("Ip", rsc.getIp());
			}			
			if(StringUtils.isNotEmpty(rsc.getPort())){
				checkTemp.put("Port", NumberUtils.toInt(rsc.getPort()));
			}
			if(StringUtils.isNotEmpty(rsc.getFrequcency())){
				checkTemp.put("Frequcency", NumberUtils.toInt(rsc.getFrequcency()));
			}					
			if(StringUtils.isNotEmpty(rsc.getTimeout())){
				checkTemp.put("Timeout", NumberUtils.toInt(rsc.getTimeout()));
			}			
			Boolean checkResult = (Boolean)executeXml("SmartRoute.Check", checkTemp);
			
			if ( editResult != null && editResult == true  && checkResult == true) {
				flagStr = "true";
			}
		}catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}
	
	public String addRouteSmartConfig(RouteSmartConfig rsc) {
		Integer isp = NumberUtils.toInt(rsc.getIspName());
		Integer gatePolicy = NumberUtils.toInt(rsc.getGatePolicy());
		
		String gatesString = rsc.getGates();
		String[] gatesArray = gatesString.split(";");
		Object[] gatesObj = new Object[gatesArray.length];		
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
			}
		}						
		Hashtable addTemp = new Hashtable();
		addTemp.put("Description", rsc.getDescribe());
		addTemp.put("ISP", isp);
		addTemp.put("GatePolicy", gatePolicy);
		addTemp.put("Gates", gatesObj);	
		
		if(StringUtils.isNotEmpty(rsc.getMode())){
			addTemp.put("Mode", NumberUtils.toInt(rsc.getMode()));
		}
		
		if(StringUtils.isNotEmpty(rsc.getIp())){
			addTemp.put("Ip", rsc.getIp());
		}
		
		if(StringUtils.isNotEmpty(rsc.getPort())){
			addTemp.put("Port", NumberUtils.toInt(rsc.getPort()));
		}
		
		if(StringUtils.isNotEmpty(rsc.getFrequcency())){
			addTemp.put("Frequcency", NumberUtils.toInt(rsc.getFrequcency()));
		}
		
		
		if(StringUtils.isNotEmpty(rsc.getTimeout())){
			addTemp.put("Timeout", NumberUtils.toInt(rsc.getTimeout()));
		}
		
		String flagStr = "false";
		try {
			Boolean addResult = (Boolean)executeXml("SmartRoute.Add", addTemp);
			System.out.println(addResult + "..");						
			
			if ( addResult != null && addResult == true ) {
				flagStr = "true";
			}			
		}catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}
	
	public RouteSmartConfig getRouteSmartConfigById(String id) {
		
		RouteSmartConfig ret = new RouteSmartConfig();							
		try {
			Hashtable routeTemp = new Hashtable();
			routeTemp.put("Start", NumberUtils.toInt(id));
			routeTemp.put("Count", 1);
			Object[] result = (Object[])executeXml("SmartRoute.Get",routeTemp);
			
			if (result != null) {
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
				
				ret.setDescribe(descrip);
				ret.setIspName(isp.toString());
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
				
				/*add by yjp 2012/02/28*/
				String mode = res.get("Mode").toString();
				ret.setMode(mode);
				
				String modeName = getModeById(mode);
				ret.setModeName(modeName);
				
				String ip = res.get("Ip").toString();
				ret.setIp(ip);
				
				String port = res.get("Port").toString();
				ret.setPort(port);
				
				String frequcency = res.get("Frequcency").toString();
				ret.setFrequcency(frequcency);
				
				
				String timeout = res.get("Timeout").toString();
				ret.setTimeout(timeout);
				
			}																		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ret;
	}
}
