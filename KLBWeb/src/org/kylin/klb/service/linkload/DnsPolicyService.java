package org.kylin.klb.service.linkload;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.kylin.klb.entity.linkload.DnsPolicy;
import org.kylin.klb.service.KlbClient;

public class DnsPolicyService extends KlbClient {
	public List<DnsPolicy> getDnsPolicyInfoList(){
		List<DnsPolicy> dpl = new ArrayList<DnsPolicy>();
								
		try {
			Object result = (Object)executeXml("Bind.Get.All");			
			if (result != null) {								
				Map res = (Map)result;
				Integer port = (Integer)res.get("Port");				
				Object[] aRecord = (Object[])res.get("A");
					
				/* System.out.println(port);
				System.out.println(aRecord); */
																						
				if( aRecord != null && aRecord.length != 0 ){
					for(int i=0; i<aRecord.length; i++){
						Map aReco = (Map)aRecord[i];
						Integer id = (Integer)aReco.get("ID");
						String name = (String)aReco.get("Name");
						Boolean Enabled = (Boolean)aReco.get("Enabled");
						Boolean returnAll = (Boolean)aReco.get("ReturnAll");
						Integer ttl = (Integer)aReco.get("TTL");
						Object[] alise = (Object[])aReco.get("Alias");
						Object[] servers = (Object[])aReco.get("Servers");
						
						DnsPolicy dp = new DnsPolicy();
						dp.setId(id.toString());
						dp.setName(name);
						
						String aliseStr = "";
						if( alise != null && alise.length != 0 ){
							for(int j=0; j<alise.length; j++){
								String alis = (String)alise[j];								
								aliseStr += alis + "<br>";															
							}
							dp.setAlise(aliseStr);												
						}
						
						String serversStr = "";
						if( servers != null && servers.length != 0 ){
							for(int j=0; j<servers.length; j++){
								Map server = (Map)servers[j];
								Integer isp = (Integer)server.get("ISP");
								String ip = (String)server.get("IP");
								/* System.out.println(isp);
								System.out.println(ip); */
								
								if ( isp != 0 ) {
									Hashtable getIspNameTemp = new Hashtable();
									getIspNameTemp.put("ID", isp);
									Object getIspNameResult = (Object)executeXml("ISP.Get.Item.Name", getIspNameTemp);
									//System.out.println(getIspNameResult);
									
									Map ispNameMap = (Map)getIspNameResult;
									String ispName = (String)ispNameMap.get("Name");
																	
									serversStr += ispName + ":" + ip + "<br>";
								} else {
									serversStr += "默认:" + ip + "<br>";
								}
								
															
							}
							dp.setServers(serversStr);
						}					
						dpl.add(dp);								
					}					
				}																	
			}						
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return dpl;
	}
	
	public Boolean delDnsPolicyById(String id) {
		boolean flag = false;
		try {
			Hashtable delTemp = new Hashtable();
			delTemp.put("ID", NumberUtils.toInt(id));
			Boolean delResult = (Boolean)executeXml("Bind.ARecord.Delete",delTemp);
			System.out.println(delResult);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;		
	}
}
