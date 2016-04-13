package org.kylin.klb.service.network;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.xmlrpc.XmlRpcException;
import org.kylin.klb.assistant.InterfaceBondAssitant;
import org.kylin.klb.entity.network.BondConfig;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.service.KlbClient;
import org.kylin.klb.util.Utils;

public class InterfaceBondConfigService extends KlbClient {
	
	private InterfaceBondAssitant iba = new InterfaceBondAssitant();
	
	private static BondConfig bc = new BondConfig();
	
	public void initBondConfigByName(){
		bc = new BondConfig();
	}
	public void initBondConfigByName(String name){
		bc = iba.getBondConfigByName(name);
	}
	public BondConfig getBondConfig(){
		return bc;
	}
	
	public String editBondConfig(String oldName, BondConfig bc) {				
		String flagStr = "false";
		try {
			boolean editBool = iba.editBondConfig(oldName, bc);
			if ( editBool == true ) {
				flagStr = "true";
			}
		} catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}
	
	public String getInterForBond(){
		String ethStr = "";		
		try {
			Object[] ethResult = (Object[])executeXml("Ethernet.GetAll");
			if ( ethResult != null && ethResult.length != 0 ) {				
				for (int i = 0; i < ethResult.length; i++) {
					Map res = (Map)ethResult[i];
					String ethName = (String)res.get("Dev");
					String adsl = (String)res.get("Adsl");
					String master = (String)res.get("Master");
					if ( StringUtils.equals(adsl, "") && StringUtils.equals(master, "") ) {
						ethStr += ethName + ";";
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ethStr;
	}
	/* public List<Display> getInterfaceList(){
		List<Display> ret = new ArrayList<Display>();
		Display display = new Display("eth0", "eth0");
		ret.add(display);
		display = new Display("eth1", "eth1");
		ret.add(display);
		display = new Display("eth2", "eth2");
		ret.add(display);
		display = new Display("eth3", "eth3");
		ret.add(display);
		display = new Display("eth4", "eth4");
		ret.add(display);
		display = new Display("eth5", "eth5");
		ret.add(display);
		return ret;
	}
	public List<Display> getBondNameList(){
		List<Display> ret = new ArrayList<Display>();
		Display display = new Display("bond0", "bond0");
		ret.add(display);
		display = new Display("bond1", "bond1");
		ret.add(display);
		display = new Display("bond2", "bond2");
		ret.add(display);
		return ret;
	} */
	public List<Display> getAlgorithmList(){
		List<Display> ret = new ArrayList<Display>();
		Display display = new Display("0", "轮询");
		ret.add(display);
		display = new Display("1", "主备");
		ret.add(display);
		display = new Display("2", "IP地址异或");
		ret.add(display);
		display = new Display("3", "广播");
		ret.add(display);
		display = new Display("4", "支持802.3ad");
		ret.add(display);
		display = new Display("5", "tlb平衡");
		ret.add(display);
		display = new Display("6", "alb平衡");
		ret.add(display);
		return ret;
	}
	public void addBond (BondConfig bc){
		iba.addBond(bc);
	}
	
}
