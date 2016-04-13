package org.kylin.klb.service.linkload;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.kylin.klb.entity.linkload.DnsPolicy;
import org.kylin.klb.entity.linkload.DnsPolicyConfig;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.service.KlbClient;
import org.kylin.klb.util.Utils;

public class DnsPolicyConfigService extends KlbClient {
	
	public List<Display> getStateList() {
		List<Display> stateList = new ArrayList<Display>();
		Display state = new Display("true", "开启");
		stateList.add(state);
		state = new Display("false", "关闭");
		stateList.add(state);
		return stateList;
	}
	
	public List<Display> getIspNameList(){
		
		List<Display> ret = new ArrayList<Display>();		
		try {			
			Object[] result = (Object[])executeXml("ISP.Get.List.Name");			
			if (result != null) {				
				for (int i = 0; i < result.length; i++) {
					Map res = (Map)result[i];
					Integer id = (Integer)res.get("ID");
					String name = (String)res.get("Name");
					
					//Display ispName = new Display(id.toString(), name);
					Display ispName = new Display(name, name);
					ret.add(ispName);
				}
			}									
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public String getAllAliasList(){
		String allAliasStr = "";
		try {
			Object result = (Object)executeXml("Bind.Get.All");			
			if (result != null) {								
				Map res = (Map)result;				
				Object[] aRecord = (Object[])res.get("A");
																						
				if( aRecord != null && aRecord.length != 0 ){
					for(int i=0; i<aRecord.length; i++){
						Map aReco = (Map)aRecord[i];
						Object[] alise = (Object[])aReco.get("Alias");
																		
						String aliseStr = "";
						if( alise != null && alise.length != 0 ){
							for(int j=0; j<alise.length; j++){
								String alis = (String)alise[j];								
								aliseStr += alis + ";";															
							}
							allAliasStr += aliseStr;
						}
					}					
				}																	
			}						
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return allAliasStr;
	}
	
	public String getOtherAliasListById(String id){
		String otherAliasStr = "";
		try {
			Object result = (Object)executeXml("Bind.Get.All");			
			if (result != null) {								
				Map res = (Map)result;
				Object[] aRecord = (Object[])res.get("A");
																						
				if( aRecord != null && aRecord.length != 0 ){
					for(int i=0; i<aRecord.length; i++){
						Map aReco = (Map)aRecord[i];
						Integer aid = (Integer)aReco.get("ID");
						Object[] alise = (Object[])aReco.get("Alias");
																		
						String aliseStr = "";
						if( alise != null && alise.length != 0 ){
							for(int j=0; j<alise.length; j++){
								String alis = (String)alise[j];								
								aliseStr += alis + ";";															
							}							
						}
						if ( !aid.toString().equals(id) ) {
							otherAliasStr += aliseStr;
						}						
					}
				}																	
			}						
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return otherAliasStr;
	}
	
	public DnsPolicyConfig getDnsPolicyConfigById(String id) {
		//id = "_" + id + "_";
		DnsPolicyConfig ret = new DnsPolicyConfig();
		
		try {			
			Hashtable DnsPolicyTemp = new Hashtable();
			DnsPolicyTemp.put("ID", NumberUtils.toInt(id));			
			Object result = (Object)executeXml("Bind.Get.ARecord", DnsPolicyTemp);
			
			if (result != null) {
				Map res = (Map)result;
				String name = (String)res.get("Name");
				Boolean enabled = (Boolean)res.get("Enabled");
				Boolean returnAll = (Boolean)res.get("ReturnAll");
				Integer ttl = (Integer)res.get("TTL");												
				Object[] alise = (Object[])res.get("Alias");
				Object[] servers = (Object[])res.get("Servers");
				
				/* System.out.println(name);
				System.out.println(enabled);
				System.out.println(returnAll);
				System.out.println(ttl);
				System.out.println(alise);
				System.out.println(servers); */
				
				ret.setName(name);
				ret.setState(enabled.toString());
				ret.setEcho(returnAll.toString());
				ret.setTtl(ttl.toString());
				
				String aliseList = new String();
				if( alise==null || alise.length==0 ){
					aliseList = "";
				}
				else{
					for(int j=0; j<alise.length; j++){
						if (!StringUtils.equals((String)alise[j], "")) {
							aliseList += alise[j] + ";";
						}
					}
					//aliseList = aliseList.substring(0, aliseList.length()-1);
				}
				ret.setAliseList(aliseList);
				
				String serversString = "";
				String serversForDisplay = "";
				if (servers != null) {
					for (int i = 0; i < servers.length; i++) {
						Map server = (Map)servers[i];
						Integer isp = (Integer)server.get("ISP");
						String ip = (String)server.get("IP");
						
						/* System.out.println(isp);
						System.out.println(ip); */
						
						if ( isp != 0 ) {
							Hashtable DnsIspTemp = new Hashtable();
							DnsIspTemp.put("ID", isp);
							Object ispNameResult = (Object)executeXml("ISP.Get.Item.Name",DnsIspTemp);
							
							if (ispNameResult != null) {
								Map ispRes = (Map)ispNameResult;
								String ispName = (String)ispRes.get("Name");
								
								serversForDisplay += ispName + "," + ip + ";";
								serversString += ispName + "," + ip + ";";
							}
						} else {
							serversForDisplay += "默认," + ip + ";";
							serversString += "默认," + ip + ";";
						}																																																											
					}
				}
				ret.setServers(serversString);
				ret.setDisplayServers(serversForDisplay);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ret;		
	}
	
	public String addDnsPolicyConfig(DnsPolicyConfig dpc) {										
		//String id = dpc.getId();		
		String[] aliseArray = dpc.getAliseList().split(";");
		//System.out.println(dpc.getAliseList());
		
		Hashtable addDnsPolicyTemp = new Hashtable();
		//addDnsPolicyTemp.put("ID", NumberUtils.toInt(id));
		addDnsPolicyTemp.put("Name", dpc.getName());
		addDnsPolicyTemp.put("Enabled", dpc.getState().equals("true") ? true : false);
		addDnsPolicyTemp.put("ReturnAll", dpc.getEcho().equals("true") ? true : false);
		if(!StringUtils.equals(dpc.getTtl(), "")) {				
			addDnsPolicyTemp.put("TTL", NumberUtils.toInt(dpc.getTtl()));
		}			
		if(!StringUtils.equals(dpc.getAliseList(), "")) {				
			addDnsPolicyTemp.put("Alias", aliseArray);
		}
		
		String flagStr = "false";	
		try {
			Object[] result = (Object[])executeXml("ISP.Get.List.Name");
			
			String[] idArray = new String[result.length];
			String[] nameArray = new String[result.length];
			if (result != null) {				
				for (int i = 0; i < result.length; i++) {
					Map res = (Map)result[i];
					Integer ispId = (Integer)res.get("ID");
					String name = (String)res.get("Name");
					
					idArray[i] = ispId.toString();
					nameArray[i] = name;					
				}
			}
			String serversString = dpc.getServers();
			//System.out.println(serversString);
			String[] serversArray = serversString.split(";");
			Object[] serversObj = new Object[serversArray.length];
			int index = 0;
			if (serversString != null && !serversString.equals("")) {
				for (int i = 0; i < serversArray.length; i++) {
					String[] temp = serversArray[index].split(",");
					String[] niArray = {"",""};
					if(temp.length != 0){
						for(int j=0; j<temp.length; j++){
							niArray[j]=temp[j];
						}
					}
					
					Hashtable serverTemp = new Hashtable();
					for (int j = 0; j < result.length; j++) {
						if ( nameArray[j].equals(niArray[0]) ) {
							//System.out.println(idArray[j]);
							serverTemp.put("ISP", NumberUtils.toInt(idArray[j]));
						}
					}															
					serverTemp.put("IP", niArray[1]);
					serversObj[i] = serverTemp;
										
					index++;
				}
				addDnsPolicyTemp.put("Servers", serversObj);
			}			
			if(!StringUtils.equals(dpc.getName(), "")) {				
				Boolean addDnsPolicyResult = (Boolean)executeXml("Bind.ARecord.Add",addDnsPolicyTemp);
				//System.out.println(addDnsPolicyResult);
			}
						
			flagStr = "true";
		}catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}
	
	public String editDnsPolicyConfig(DnsPolicyConfig dpc) {				
		String id = dpc.getId();		
		String aliseTemp=dpc.getAliseList();
		String[] aliseArray = null;
		if (aliseTemp.equals("")) {
			aliseArray = new String[0];
		} else {
			aliseArray = dpc.getAliseList().split(";");
		}			 						
			
		Hashtable editDnsPolicyTemp = new Hashtable();
		editDnsPolicyTemp.put("ID", NumberUtils.toInt(id));
		editDnsPolicyTemp.put("Name", dpc.getName());
		editDnsPolicyTemp.put("Enabled", dpc.getState().equals("true") ? true : false);
		editDnsPolicyTemp.put("ReturnAll", dpc.getEcho().equals("true") ? true : false);
		editDnsPolicyTemp.put("TTL", NumberUtils.toInt(dpc.getTtl()));			
		editDnsPolicyTemp.put("Alias", aliseArray);
		
		String flagStr = "false";
		try {	
			Object[] result = (Object[])executeXml("ISP.Get.List.Name");
			String[] idArray = new String[result.length];
			String[] nameArray = new String[result.length];
			if (result != null) {				
				for (int j = 0; j < result.length; j++) {
					Map res = (Map)result[j];
					Integer ispId = (Integer)res.get("ID");
					String name = (String)res.get("Name");
					
					idArray[j] = ispId.toString();
					nameArray[j] = name;					
				}
			}								
						
			String serversString = dpc.getServers();
			String[] serversArray = null;
			if (serversString.equals("")) {
				serversArray = new String[0];
			} else {
				serversArray = serversString.split(";");
			}
			Object[] serversObj = new Object[serversArray.length];
			
			int index = 0;
			if ( serversString != null ) {
				for (int i = 0; i < serversArray.length; i++) {
					String[] temp = serversArray[index].split(",");
					String[] niArray = {"",""};
					if(temp.length != 0){
						for(int j=0; j<temp.length; j++){
							niArray[j]=temp[j];
						}
					}
															
					Hashtable serverTemp = new Hashtable();
					for (int j = 0; j < result.length; j++) {
						if ( nameArray[j].equals(niArray[0]) ) {
							serverTemp.put("ISP", NumberUtils.toInt(idArray[j]));
						}
					}															
					serverTemp.put("IP", niArray[1]);
					serversObj[i] = serverTemp;
										
					index++;
				}
				editDnsPolicyTemp.put("Servers", serversObj);
			}	
			if(!StringUtils.equals(dpc.getName(), "")) {				
				Boolean editDnsPolicyResult = (Boolean)executeXml("Bind.ARecord.Set",editDnsPolicyTemp);
				//System.out.println(editDnsPolicyResult);
			}						
			flagStr = "true";
		}catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}
	
}
