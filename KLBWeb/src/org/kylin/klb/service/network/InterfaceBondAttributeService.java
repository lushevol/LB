package org.kylin.klb.service.network;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.xmlrpc.XmlRpcException;
import org.kylin.klb.assistant.InterfaceBondAssitant;
import org.kylin.klb.entity.network.BondAttribute;
import org.kylin.klb.entity.network.BondConfig;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.util.Utils;

public class InterfaceBondAttributeService{
	
	private InterfaceBondAssitant iba = new InterfaceBondAssitant();
	private static BondAttribute ba;
	
	public void initBondAttributeByName(String name){
		ba = iba.getBondAttributeByName(name);
	}
	
	public String editBondAttribute(BondAttribute ba) {				
		String flagStr = "false";
		try {
			boolean editBool = iba.editBondAttribute(ba);
			if ( editBool == true ) {
				flagStr = "true";
			}
		} catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;		
	}
	
	public BondAttribute getBondAttribute(){
		return ba;
	}
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
}
