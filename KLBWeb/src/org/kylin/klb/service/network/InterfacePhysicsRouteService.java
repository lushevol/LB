package org.kylin.klb.service.network;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.xmlrpc.XmlRpcException;
import org.kylin.klb.assistant.InterfaceAssitant;
import org.kylin.klb.entity.network.InterfaceInfo;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.service.KlbClient;
import org.kylin.klb.util.Utils;

public class InterfacePhysicsRouteService  extends KlbClient {
	
	private InterfaceAssitant ia = new InterfaceAssitant();
	private static List<InterfaceInfo> iil;
	private static InterfaceInfo ii;
	
	public List<Display> getArpList() {
		List<Display> ret = new ArrayList<Display>();
		Display display = new Display("0", "开启");
		ret.add(display);
		display = new Display("1", "关闭");
		ret.add(display);
		display = new Display("2", "ARP应答");
		ret.add(display);
		display = new Display("3", "ARP代理");
		ret.add(display);
		return ret;
	}
	public List<Display> getDoubleList() {
		List<Display> ret = new ArrayList<Display>();
		/* Display display = new Display("Auto", "请选择");
		ret.add(display); */
		Display display = new Display("true", "全双工");
		ret.add(display);
		display = new Display("false", "半双工");
		ret.add(display);
		return ret;
	}
	public List<Display> getSpeedList() {
		List<Display> ret = new ArrayList<Display>();
		/* Display display = new Display("Auto", "请选择");
		ret.add(display); */
		Display display = new Display("10", "10Mbps");
		ret.add(display);
		display = new Display("100", "100Mbps");
		ret.add(display);
		display = new Display("1000", "1000Mbps");
		ret.add(display);
		display = new Display("10000", "10000Mbps");
		ret.add(display);
		return ret;
	}
	public List<Display> getStateList() {
		List<Display> stateList = new ArrayList<Display>();
		Display state = new Display("true", "开启");
		stateList.add(state);
		state = new Display("false", "关闭");
		stateList.add(state);
		return stateList;
	}
	public List<Display> getDhcpList() {
		List<Display> dhcpList = new ArrayList<Display>();
		Display state = new Display("true", "开启");
		dhcpList.add(state);
		state = new Display("false", "关闭");
		dhcpList.add(state);
		return dhcpList;
	}
	
	public void initInterfaceInfoList(String name){
		iil = ia.getInterfaceInfoList();
		ii = getInterfaceInfoByName(name);
	}
	
	private InterfaceInfo getInterfaceInfoByName(String name) {
		InterfaceInfo ii = new InterfaceInfo();
		Iterator<InterfaceInfo> i = iil.iterator();
		while(i.hasNext()){
			InterfaceInfo iitemp = i.next();
			if (StringUtils.equals(name, iitemp.getName())){
				ii = iitemp;
			}
		}
		return ii;
	}

	public InterfaceInfo getInterfaceInfo() {
		return ii;
	}
	public String editInterfaceInfo(InterfaceInfo ii) {
		
		String flagStr = "false";		
		try {
			String editMess = ia.editInterfaceInfo(ii);
			if ( editMess.equals("") ) {
				flagStr = "true";
			}
		} catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;		
	}
	
	public String getResumeMac() {
		String realMac = "";
		try {			
			Hashtable ethTemp = new Hashtable();
			ethTemp.put("Dev", ii.getName());			
			Object result = (Object)executeXml("Ethernet.Get",ethTemp);
			
			if (result != null) {
				Map res = (Map)result;
				String address = (String)res.get("Address");
				String currentAddress = (String)res.get("CurrentAddress");
				String realAddress = (String)res.get("RealAddress");
																
				if (realAddress != null) {
					realMac = realAddress;
				}
			}
			return realMac;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
