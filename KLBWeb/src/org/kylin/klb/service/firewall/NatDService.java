package org.kylin.klb.service.firewall;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.xmlrpc.XmlRpcException;
import org.dom4j.Document;
import org.dom4j.Element;
import org.kylin.klb.entity.firewall.Nat;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.service.KlbClient;
import org.kylin.klb.util.Utils;

public class NatDService extends KlbClient {
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

	public List<Nat> getNatList() {
		List<Nat> natList = new ArrayList<Nat>();
		Nat nat = new Nat();
		
		try {			
			Hashtable natTemp = new Hashtable();
			natTemp.put("All", true);
			Object[] result = (Object[])executeXml("Nat.Dest.Get",natTemp);
						
			if (result != null) {
				for (int i = 0; i < result.length; i++) {
						
					Map res = (Map)result[i];
					Integer id = (Integer)res.get("ID");
					Boolean status = (Boolean)res.get("Status");
					Boolean enabled = (Boolean)res.get("Enabled");					
					String descrip = (String)res.get("Description");																				
					Object matchObj = (Object)res.get("Match");
					Object actionObj = (Object)res.get("Action");
					
					nat.setId(id.toString());
					nat.setEnabled(enabled.toString());
					nat.setDescribe(descrip);
					
					if (matchObj != null) {
						Map match = (Map)matchObj;
						String srcNet = (String)match.get("SrcNet");
						String destNet = (String)match.get("DestNet");
						Integer protocol = (Integer)match.get("Protocol");
						String protocolStr = (String)match.get("ProtocolStr");
						Integer srcPort = (Integer)match.get("SrcPort");
						String srcPortStr = (String)match.get("SrcPortStr");
						Integer destPort = (Integer)match.get("DestPort");
						String destPortStr = (String)match.get("DestPortStr");
						String dev = (String)match.get("Dev");
						
						/* System.out.println(srcNet);
						System.out.println(destNet);
						System.out.println(protocol);
						System.out.println(protocolStr);
						System.out.println(srcPort);
						System.out.println(srcPortStr);
						System.out.println(destPort);
						System.out.println(destPortStr);
						System.out.println(dev); */
						
						nat.setDestIP(destNet);
						nat.setDestPort(destPortStr);
						nat.setInterfaces(dev);						
						nat.setProtocol(protocolStr);
						nat.setSrcIP(srcNet);
						nat.setSrcPort(srcPortStr);
																																										
					}
					
					if (actionObj != null) {
						Map action = (Map)actionObj;
						Boolean except = (Boolean)action.get("Except");
						String startIP = (String)action.get("StartIP");
						String endIP = (String)action.get("EndIP");
						Integer startPort = (Integer)action.get("StartPort");
						Integer endPort = (Integer)action.get("EndPort");
						
						/* System.out.println(except);						
						System.out.println(startIP);
						System.out.println(endIP);
						System.out.println(startPort);
						System.out.println(endPort); */
						
						if(except == true){
							nat.setType("EXPT");
							nat.setStartIP("");
							nat.setEndIP("");
							nat.setStartPort("");
							nat.setEndPort("");
						}
						else{							
							nat.setType("DNAT");
							nat.setStartIP(startIP);
							nat.setEndIP(endIP);
							nat.setStartPort(startPort.toString());
							nat.setEndPort(endPort.toString());
						}																		
					}
					natList.add(nat);
					nat = new Nat();																				
				}
			}						
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return natList;
	}

	public boolean move(String oldId, String newId) throws XmlRpcException {
		boolean status = false;
		String xmlName = "nat.xml";
		Document doc = getDocumentByInputStream(xmlName);
		Element nat = doc.getRootElement().element("Network").element("Nat");

		nat.addAttribute("move", "1").addAttribute("to", newId).addElement("_" + oldId + "_");
		// System.out.println(doc.asXML());
		String result = (String) executeXml(methodName, doc.asXML());
		// System.out.println(result);
		if (result != null && !result.contains("failed")) {
			status = true;
		}
		return status;
	}

	public Nat getNatById(String id) {								
		Nat ret = new Nat();		
		try {			
			Hashtable natTemp = new Hashtable();
			natTemp.put("Start", NumberUtils.toInt(id));
			natTemp.put("Count", 1);
			Object[] result = (Object[])executeXml("Nat.Dest.Get",natTemp);
			
			if (result != null) {
				Map res = (Map)result[0];
				Boolean status = (Boolean)res.get("Status");
				Boolean enabled = (Boolean)res.get("Enabled");					
				String descrip = (String)res.get("Description");																				
				Object matchObj = (Object)res.get("Match");
				Object actionObj = (Object)res.get("Action");
				
				ret.setId(id);
				ret.setDescribe(descrip);
				
				if (matchObj != null) {
					Map match = (Map)matchObj;
					String srcNet = (String)match.get("SrcNet");
					String destNet = (String)match.get("DestNet");
					Integer protocol = (Integer)match.get("Protocol");
					String protocolStr = (String)match.get("ProtocolStr");
					Integer srcPort = (Integer)match.get("SrcPort");
					String srcPortStr = (String)match.get("SrcPortStr");
					Integer destPort = (Integer)match.get("DestPort");
					String destPortStr = (String)match.get("DestPortStr");
					String dev = (String)match.get("Dev");
					
					/* System.out.println(srcNet);
					System.out.println(destNet);
					System.out.println(protocol);
					System.out.println(protocolStr);
					System.out.println(srcPort);
					System.out.println(srcPortStr);
					System.out.println(destPort);
					System.out.println(destPortStr);
					System.out.println(dev); */
					
					ret.setDestIP(destNet);
					ret.setDestPort(destPortStr);
					ret.setInterfaces(dev);					
					ret.setProtocol(protocolStr);
					ret.setSrcIP(srcNet);
					ret.setSrcPort(srcPortStr);
																																									
				}
				
				if (actionObj != null) {
					Map action = (Map)actionObj;
					Boolean except = (Boolean)action.get("Except");					
					String startIP = (String)action.get("StartIP");
					String endIP = (String)action.get("EndIP");
					Integer startPort = (Integer)action.get("StartPort");
					Integer endPort = (Integer)action.get("EndPort");
					
					/* System.out.println(except);					
					System.out.println(startIP);
					System.out.println(endIP);
					System.out.println(startPort);
					System.out.println(endPort); */
					
					if(except == true){
						ret.setType("EXPT");
						ret.setStartIP("");
						ret.setEndIP("");
						ret.setStartPort("");
						ret.setEndPort("");
					}
					else{						
						ret.setType("DNAT");
						ret.setStartIP(startIP);
						ret.setEndIP(endIP);
						if (startPort == 0) {
							ret.setStartPort("");
						} else {
							ret.setStartPort(startPort.toString());
						}
						if (endPort == 0) {
							ret.setEndPort("");
						} else {
							ret.setEndPort(endPort.toString());
						}
					}																	
				}																				
			}									
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ret;
	}
	
	public String getFailedMessage(String id, Boolean result) {
		String failedMess = new String();
		if(result != null && result == true){
			return "";
		}
		else{
			try {
				failedMess += "目的地址转换" + id + ": ";
				failedMess += "配置失败，可能是您配置的IP地址和掩码不匹配。";
				return failedMess;
			}catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	public String addNat(Nat nat) {							
		Object matchObj = new Object();
		Object actionObj = new Object();
			
		Hashtable addTemp = new Hashtable();
		Hashtable matchTemp = new Hashtable();
		Hashtable actionTemp = new Hashtable();
			
		if (nat.getSrcIP() != null) {
			matchTemp.put("SrcNet", nat.getSrcIP());
		}
		if (nat.getSrcPort() != null) {
			matchTemp.put("SrcPortStr", nat.getSrcPort());
		}
		if (nat.getDestIP() != null) {
			matchTemp.put("DestNet", nat.getDestIP());
		}
		if (nat.getDestPort() != null) {
			matchTemp.put("DestPortStr", nat.getDestPort());
		}			
		if (nat.getInterfaces() != null) {
			matchTemp.put("Dev", nat.getInterfaces());
		}
		if (nat.getProtocol() != null) {
			matchTemp.put("ProtocolStr", nat.getProtocol());
		}
		matchObj = matchTemp;			
			
		if (nat.getType() != null) {
			if ( nat.getType().equalsIgnoreCase("EXPT") ) {
				actionTemp.put("Except", true);
			} else {
				actionTemp.put("Except", false);
			}				
		}						
		if (nat.getStartIP() != null) {					
			actionTemp.put("StartIP", nat.getStartIP());
		}			
		if (nat.getEndIP() != null) {					
			actionTemp.put("EndIP", nat.getEndIP());
		}
		if (nat.getStartPort() != null) {
			actionTemp.put( "StartPort", NumberUtils.toInt(nat.getStartPort()) );
		}
		if (nat.getEndPort() != null) {
			actionTemp.put( "EndPort", NumberUtils.toInt(nat.getEndPort()) );
		}
		actionObj = actionTemp;
			
		addTemp.put("Description", nat.getDescribe());
		addTemp.put("Match", matchObj);
		addTemp.put("Action", actionObj);
		
		String flagStr = "false";
		try {
			Boolean addResult = (Boolean)executeXml("Nat.Dest.Add",addTemp);
			System.out.println(addResult);
			flagStr = "true";			
		} catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}

	public String editNat(String id, Nat nat) {					
		Object matchObj = new Object();
		Object actionObj = new Object();
			
		Hashtable editTemp = new Hashtable();
		Hashtable matchTemp = new Hashtable();
		Hashtable actionTemp = new Hashtable();
			
		if (nat.getSrcIP() != null) {
			matchTemp.put("SrcNet", nat.getSrcIP());
		}
		if (nat.getSrcPort() != null) {
			matchTemp.put("SrcPortStr", nat.getSrcPort());
		}
		if (nat.getDestIP() != null) {
			matchTemp.put("DestNet", nat.getDestIP());
		}
		if (nat.getDestPort() != null) {
			matchTemp.put("DestPortStr", nat.getDestPort());
		}			
		if (nat.getInterfaces() != null) {
			matchTemp.put("Dev", nat.getInterfaces());
		}
		if (nat.getProtocol() != null) {
			matchTemp.put("ProtocolStr", nat.getProtocol());
		}
		matchObj = matchTemp;
			
		if (nat.getType() != null) {
			if ( nat.getType().equalsIgnoreCase("EXPT") ) {
				actionTemp.put("Except", true);
			} else {
				actionTemp.put("Except", false);
			}				
		}						
		if (nat.getStartIP() != null) {					
			actionTemp.put("StartIP", nat.getStartIP());
		}
		if (nat.getEndIP() != null) {		
			actionTemp.put("EndIP", nat.getEndIP());
		}
		if (nat.getStartPort() != null) {
			actionTemp.put( "StartPort", NumberUtils.toInt(nat.getStartPort()) );
		}
		if (nat.getEndPort() != null) {
			actionTemp.put( "EndPort", NumberUtils.toInt(nat.getEndPort()) );
		}
		actionObj = actionTemp;
			
		editTemp.put("ID", NumberUtils.toInt(id));
		editTemp.put("Description", nat.getDescribe());
		editTemp.put("Match", matchObj);
		editTemp.put("Action", actionObj);
		
		String flagStr = "false";
		try {
			Boolean editResult = (Boolean)executeXml("Nat.Dest.Replace",  editTemp);
			System.out.println(editResult);
			flagStr = "true";
		} catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}

	public String insertNat(String id, Nat nat) {							
		Object matchObj = new Object();
		Object actionObj = new Object();
			
		Hashtable insertTemp = new Hashtable();
		Hashtable matchTemp = new Hashtable();
		Hashtable actionTemp = new Hashtable();
			
		if (nat.getSrcIP() != null) {
			matchTemp.put("SrcNet", nat.getSrcIP());
		}
		if (nat.getSrcPort() != null) {
			matchTemp.put("SrcPortStr", nat.getSrcPort());
		}
		if (nat.getDestIP() != null) {
			matchTemp.put("DestNet", nat.getDestIP());
		}
		if (nat.getDestPort() != null) {
			matchTemp.put("DestPortStr", nat.getDestPort());
		}			
		if (nat.getInterfaces() != null) {
			matchTemp.put("Dev", nat.getInterfaces());
		}
		if (nat.getProtocol() != null) {
			matchTemp.put("ProtocolStr", nat.getProtocol());
		}
		matchObj = matchTemp;
			
		if (nat.getType() != null) {
			if ( nat.getType().equalsIgnoreCase("EXPT") ) {
				actionTemp.put("Except", true);
			} else {
				actionTemp.put("Except", false);
			}				
		}
						
		if (nat.getStartIP() != null) {					
			actionTemp.put("StartIP", nat.getStartIP());
		}
		if (nat.getEndIP() != null) {		
			actionTemp.put("EndIP", nat.getEndIP());
		}
		if (nat.getStartPort() != null) {
			actionTemp.put( "StartPort", NumberUtils.toInt(nat.getStartPort()) );
		}
		if (nat.getEndPort() != null) {
			actionTemp.put( "EndPort", NumberUtils.toInt(nat.getEndPort()) );
		}
		actionObj = actionTemp;
			
		insertTemp.put("ID", NumberUtils.toInt(id));
		insertTemp.put("Description", nat.getDescribe());
		insertTemp.put("Match", matchObj);
		insertTemp.put("Action", actionObj);
		
		String flagStr = "false";	
		try {
			Boolean insertResult = (Boolean)executeXml("Nat.Dest.Insert",insertTemp);
			System.out.println(insertResult);
			flagStr = "true";
		} catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}
	
	public String editNatEnabled(String id, String enabled) {
		String flagStr = "false";	
		try {
			Hashtable enableTemp = new Hashtable();
			enableTemp.put("ID", NumberUtils.toInt(id));
			enableTemp.put("Enabled", enabled.equals("true") ? true : false);
			Boolean enableResult = (Boolean)executeXml("Nat.Dest.Enabled",enableTemp);
			flagStr = "true";
		} catch (XmlRpcException e) {	
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}

	public boolean delNatById(String id) {
		boolean status = false;	
		try {
			Hashtable delTemp = new Hashtable();
			delTemp.put("ID", NumberUtils.toInt(id));
			Boolean delResult = (Boolean)executeXml("Nat.Dest.Delete",delTemp);
			System.out.println(delResult);
			status = true;
		} catch (Exception e) {
			status = false;
			e.printStackTrace();
		}				
		return status;
	}
}
