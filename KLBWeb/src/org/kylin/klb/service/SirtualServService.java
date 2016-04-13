package org.kylin.klb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.kylin.klb.entity.security.Display;
import org.kylin.modules.web.struts2.Struts2Utils;

public class SirtualServService extends KlbClient {
	
	public List<Display> getHaTypeList(){
		List<Display> typeList = new ArrayList<Display>();
		Display type1 = new Display("0", "本地");
		Display type2 = new Display("1", "本机服务");
		Display type3 = new Display("2", "另外一台");
		typeList.add(type1);
		typeList.add(type2);
		typeList.add(type3);
		return typeList;
	}
	
	public String getHaNameByType(String haType){
		int haT = NumberUtils.toInt(haType);
		String haName = "未知类型";
		switch (haT) {
		case 0:
			haName = "本地";
			break;
		case 1:
			haName = "本机服务";
			break;
		case 2:
			haName = "另外一台";
			break;
		default:		
			break;
		}
		return haName;
		
	}
	
	public List<Display> getinterfacesList() {

		List<Display> ret = new ArrayList<Display>();		
if(Struts2Utils.IS_SOFT_VERSION == true){
	return ret;
}
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
					//ret.add(bondName);
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
	
	public List<String> getInterfacesList(){

		List<String> ret = new ArrayList<String>();
if(Struts2Utils.IS_SOFT_VERSION == true){
	return ret;
}
		try {
			
			Object[] result = (Object[])executeXml("Bonding.GetAll");
			String interBonded = new String();
			if ( result != null && result.length != 0 ) {				
				for (int i = 0; i < result.length; i++) {					
					Map res = (Map)result[i];
					String bondName = (String)res.get("Dev");
					Object[] slaves = (Object[])res.get("Slaves");
					
					ret.add(bondName);										
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
					if ( !interBonded.contains(ethName) ) {
						ret.add(ethName);
					}
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

}
