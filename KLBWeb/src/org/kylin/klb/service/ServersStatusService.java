package org.kylin.klb.service;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.kylin.klb.entity.security.Server;
import org.kylin.modules.utils.StringUtils;

public class ServersStatusService extends KlbClient {

	public boolean contains(List<Server> servers, String ip) {
		Iterator<Server> it = servers.iterator();
		while (it.hasNext()) {
			Server temp = it.next();
			if (StringUtils.equals(temp.getIp(), ip)) {
				return true;
			}
		}
		return false;
	}

	public List<Server> updateServers(List<Server> servers, String ip, Server.VirtualService virtualService) {

		Iterator<Server> it = servers.iterator();
		List<Server> ret = new ArrayList<Server>();
		while (it.hasNext()) {
			Server temp = it.next();
			if (StringUtils.equals(temp.getIp(), ip)) {
				temp.getVirtualServices().add(virtualService);
			}
			ret.add(temp);
		}
		return ret;
	}

	public List<Server> getServers() {
		
		List<Server> servers = new ArrayList<Server>();
		
		try {			
			Hashtable vsTemp = new Hashtable();
			vsTemp.put("All", true);
			Object[] result = (Object[])executeXml("VirtualService.Get",vsTemp);
			
			if (result == null || result.length ==0) {
				return null;
			} else {
				for (int i = 0; i < result.length; i++) {										
					Map res = (Map)result[i];
					Integer id = (Integer)res.get("ID");
					String name = (String)res.get("Name");
					Object[] ips = (Object[])res.get("IP");
					Object[] serversObj = (Object[])res.get("Servers");
																								
					if (serversObj == null || serversObj.length == 0) {
						continue;
					} else {
						
						for (int j = 0; j < serversObj.length; j++) {
							Map server = (Map)serversObj[j];
							Integer tsId = (Integer)server.get("ID");
							String ip = (String)server.get("IP");
							String tsName = (String)server.get("Name");
							Boolean enabled = (Boolean)server.get("Enabled");
							Boolean status = (Boolean)server.get("Status");														
														
							Server.VirtualService virtualService = new Server.VirtualService();
							Server tempS = new Server();
							tempS.setIp(ip);
							if (enabled == true) {
								if (status != null && status == true) {
									virtualService.setStatus("up");									
								} else {
									virtualService.setStatus("down");
								}
							} else {
								virtualService.setStatus("closed");
							}
							
							virtualService.setRealServerName(tsName);
							
							if ( ips != null && ips.length !=0 ) {
								Map ipMap = (Map)ips[0];
								String vip = (String)ipMap.get("IP");
								
								virtualService.setVirtualServiceIpName(name + " : " + vip);
							}
							//
							List<Server.VirtualService> list = new ArrayList<Server.VirtualService>();
							list.add(virtualService);
							tempS.setVirtualServices(list);
							servers.add(tempS);
						}
						
					}																														
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return servers;
	}

	public void test(List<Server> servers) {
		Iterator<Server> it = servers.iterator();
		while (it.hasNext()) {
			Server server = it.next();

			List<Server.VirtualService> services = server.getVirtualServices();
			Iterator<Server.VirtualService> it2 = services.iterator();
			while (it2.hasNext()) {
				Server.VirtualService test = it2.next();

			}
		}
	}

	public void test1(Server server) {
		Server s = server;
		List<Server.VirtualService> services = s.getVirtualServices();
		Iterator<Server.VirtualService> it = services.iterator();
		while (it.hasNext()) {
			Server.VirtualService test = it.next();

		}
	}
}
