package org.kylin.klb.service;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.xmlrpc.XmlRpcException;
import org.kylin.klb.entity.security.AccessControl;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.util.Utils;

public class KlbNetworkAccessService extends KlbClient {	

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

	public List<AccessControl> getAccessControlList() {
		List<AccessControl> acList = new ArrayList<AccessControl>();
		AccessControl ret = new AccessControl();
		
		try {			
			Hashtable acTemp = new Hashtable();
			acTemp.put("All", true);
			Object[] result = (Object[])executeXml("BlackList.Get", acTemp);
						
			if (result != null) {
				for (int i = 0; i < result.length; i++) {						
					Map res = (Map)result[i];
					Integer id = (Integer)res.get("ID");					
					String srcNet = (String)res.get("SrcNet");
					String destNet = (String)res.get("DestNet");
					String protocolStr = (String)res.get("ProtocolStr");
					Integer srcPort = (Integer)res.get("SrcPort");
					Integer destPort = (Integer)res.get("DestPort");										
					
					ret.setId(id.toString());
					//ret.setDescribe(descrip);
					ret.setSrcNet(srcNet);
					ret.setDestNet(destNet);
					ret.setProtocol(protocolStr);
					if ( protocolStr.equalsIgnoreCase("tcp") || protocolStr.equalsIgnoreCase("udp") || protocolStr.equalsIgnoreCase("")) {
						ret.setProtocols(protocolStr);
					} else {
						ret.setProtocols("other");
					}
					ret.setSrcPort(srcPort.toString());	
					ret.setDestPort(destPort.toString());
					acList.add(ret);
					ret = new AccessControl();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return acList;
	}

	public AccessControl getAccessControlById(String id) {
		AccessControl ret = new AccessControl();		
		try {			
			Hashtable acTemp = new Hashtable();
			acTemp.put("Start", NumberUtils.toInt(id));
			acTemp.put("Count", 1);
			Object[] result = (Object[])executeXml("BlackList.Get", acTemp);
			
			if (result != null) {
				Map res = (Map)result[0];
				//Integer id = (Integer)res.get("ID");				
				String srcNet = (String)res.get("SrcNet");
				String destNet = (String)res.get("DestNet");
				String protocolStr = (String)res.get("ProtocolStr");
				Integer srcPort = (Integer)res.get("SrcPort");
				Integer destPort = (Integer)res.get("DestPort");
				
				ret.setId(id);
				//ret.setDescribe(descrip);
				ret.setSrcNet(srcNet);
				ret.setDestNet(destNet);
				ret.setProtocol(protocolStr);
				if ( protocolStr.equalsIgnoreCase("tcp") || protocolStr.equalsIgnoreCase("udp") || protocolStr.equalsIgnoreCase("")) {
					ret.setProtocols(protocolStr);
				} else {
					ret.setProtocols("other");
				}				
				if (srcPort == 0) {
					ret.setSrcPort("");
				} else {
					ret.setSrcPort(srcPort.toString());	
				}
				if (destPort == 0) {
					ret.setDestPort("");
				} else {
					ret.setDestPort(destPort.toString());
				}								
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ret;
	}

	public String addAccessControl(AccessControl ac) {		
		Hashtable addTemp = new Hashtable();									
		if (ac.getSrcNet() != null) {
			addTemp.put("SrcNet", ac.getSrcNet());
		}
		if (ac.getSrcPort() != null) {
			addTemp.put("SrcPort", NumberUtils.toInt(ac.getSrcPort()));
		}
		if (ac.getDestNet() != null) {
			addTemp.put("DestNet", ac.getDestNet());
		}
		if (ac.getDestPort() != null) {
			addTemp.put("DestPort", NumberUtils.toInt(ac.getDestPort()));
		}						
		if (ac.getProtocol() != null) {
			addTemp.put("ProtocolStr", ac.getProtocol());
		}
		
		String flagStr = "false";
		try {	
			Boolean addResult = (Boolean)executeXml("BlackList.Add", addTemp);
			System.out.println(addResult);
			flagStr = "true";
		} catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}
	
	public String setAccessControl(String id, AccessControl ac) {													
		Hashtable setTemp = new Hashtable();			
		if (id != null) {
			setTemp.put("ID", NumberUtils.toInt(id));
		}			
		if (ac.getSrcNet() != null) {
			setTemp.put("SrcNet", ac.getSrcNet());
		}
		if (ac.getSrcPort() != null) {
			setTemp.put("SrcPort", NumberUtils.toInt(ac.getSrcPort()));
		}
		if (ac.getDestNet() != null) {
			setTemp.put("DestNet", ac.getDestNet());
		}
		if (ac.getDestPort() != null) {
			setTemp.put("DestPort", NumberUtils.toInt(ac.getDestPort()));
		}						
		if (ac.getProtocol() != null) {
			setTemp.put("ProtocolStr", ac.getProtocol());
		}
		
		String flagStr = "false";
		try {
			Boolean setResult = (Boolean)executeXml("BlackList.Set",setTemp);
			System.out.println(setResult);
			if (setResult != null && setResult == true) {
				flagStr = "true";
			}
		} catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}

	public boolean delAccessControlById(String id) {
		boolean status = false;		
		try {
			Hashtable delTemp = new Hashtable();
			delTemp.put("ID", NumberUtils.toInt(id));
			Boolean delResult = (Boolean)executeXml("BlackList.Delete",delTemp);			
			status = true;
		} catch (Exception e) {
			status = false;
			e.printStackTrace();
		}
		return status;
	}
}
