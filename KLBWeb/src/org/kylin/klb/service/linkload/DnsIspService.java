package org.kylin.klb.service.linkload;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.xmlrpc.XmlRpcException;
import org.kylin.klb.entity.linkload.DnsIsp;
import org.kylin.klb.service.KlbClient;

public class DnsIspService extends KlbClient {
	public List<DnsIsp> getDnsIspInfoList(){
		List<DnsIsp> dil = new ArrayList<DnsIsp>();
								
		try {
			Object[] result = (Object[])executeXml("ISP.Get.List.All");			
			if (result != null) {				
				for (int i = 0; i < result.length; i++) {
					Map res = (Map)result[i];
					Integer id = (Integer)res.get("ID");
					String name = (String)res.get("Name");
					Object[] nets = (Object[])res.get("Net");
					
					/* System.out.println(id);
					System.out.println(name);
					System.out.println(nets); */
					
					DnsIsp di = new DnsIsp();
					di.setId(id.toString());
					di.setName(name);
					
					String netStr = "";
					if( nets != null && nets.length != 0 ){
						if ( nets.length <= 5 ) {
							for(int j=0; j<nets.length; j++){
								String net = (String)nets[j];								
								netStr += net + "<br>";
							}
						} else {
							for(int j=0; j < 5; j++){
								String net = (String)nets[j];								
								netStr += net + "<br>";
							}
							netStr += ". . ." + "&emsp;. . .";
						}					
						di.setAddr(netStr);						
					}
					dil.add(di);										
				}
			}						
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return dil;
	}
	
	public Boolean delDnsIspById(String id) {
		boolean flag = false;
		try {
			Hashtable delTemp = new Hashtable();
			delTemp.put("ID", NumberUtils.toInt(id));
			Boolean delResult = (Boolean)executeXml("ISP.Delete",delTemp);
			System.out.println(delResult);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;		
	}	
}
