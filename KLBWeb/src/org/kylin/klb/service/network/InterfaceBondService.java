package org.kylin.klb.service.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.apache.xmlrpc.XmlRpcException;
import org.kylin.klb.assistant.InterfaceBondAssitant;
import org.kylin.klb.entity.network.BondInfo;
import org.kylin.klb.service.KlbClient;
import org.kylin.klb.util.Utils;

public class InterfaceBondService extends KlbClient {
	
	private Map<String, String> types = new HashMap();
	
	private InterfaceBondAssitant ia = new InterfaceBondAssitant();
	public List<BondInfo> getBondInfoList(){
		List<BondInfo> bil = new ArrayList<BondInfo>();
		bil = ia.getBondInfoList();
		return bil;
	}
	public String addBondName(String name){
		String flagStr = "false";
		try {			
			Hashtable addTemp = new Hashtable();
			addTemp.put("Dev", name);			
			Boolean addResult = (Boolean)executeXml("Bonding.Add",addTemp);
			System.out.println(addResult);
			if ( addResult != null && addResult == true ) {
				flagStr = "true";
			}						
		} catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}
	public Boolean delBondByName(String name) {
		//ia.delBondByName(name);		
		boolean flag = false;
		try {
			Hashtable delTemp = new Hashtable();
			delTemp.put("Dev", name);
			Boolean delResult = (Boolean)executeXml("Bonding.Delete",delTemp);
			System.out.println(delResult);								
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}
}
