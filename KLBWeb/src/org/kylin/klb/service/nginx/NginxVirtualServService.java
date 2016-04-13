package org.kylin.klb.service.nginx;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.kylin.klb.entity.nginx.Location;
import org.kylin.klb.entity.nginx.VirtualServerGroup;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.service.KlbClient;
import org.kylin.klb.service.SirtualServService;
import org.kylin.klb.util.Utils;
import org.kylin.modules.web.struts2.Struts2Utils;

public class NginxVirtualServService extends KlbClient {

	private static Logger log = Logger.getLogger(NginxVirtualServService.class);

	private SirtualServService sirtualservservice = new SirtualServService();

	public List<Display> getDevList() {
		return sirtualservservice.getinterfacesList();

	}

	public List<Display> getHaTypeList() {
		return sirtualservservice.getHaTypeList();
	}

	public String getHaNameByType(String haType) {
		return sirtualservservice.getHaNameByType(haType);
	}

	public List<Display> getLocationInsertDisplayList(
			List<Location> locationList) {
		List<Display> list = new ArrayList<Display>();
		if (locationList != null) {
			for (Location location : locationList) {
				String locate = "序列" + location.getLocationId() + "前面";
				Display display = new Display(locate, location.getLocationId());
				list.add(display);
			}
		}	
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<VirtualServerGroup> getVirtualServerGroupList() {

		List<VirtualServerGroup> groupList = new ArrayList<VirtualServerGroup>();

		try {
			Hashtable vsTemp = new Hashtable();
			Object[] result = (Object[]) executeXml("Http.Service.List", vsTemp);
			if (result != null) {
				VirtualServerGroup virtualServerGroup = null;

				for (int i = 0; i < result.length; i++) {
					virtualServerGroup = new VirtualServerGroup();

					Map res = (Map) result[i];

					String vsId = res.get("ID").toString();
					virtualServerGroup.setVsId(vsId);

					String name = res.get("Name").toString();
					virtualServerGroup.setName(name);

					String status = res.get("Status").toString();
					virtualServerGroup.setStatus(status);

					String dev = res.get("Dev").toString();
					virtualServerGroup.setDev(dev);

					String listen = res.get("Port").toString();
					virtualServerGroup.setListenPort(listen);

					String ip = res.get("IP").toString();
					virtualServerGroup.setVirtualIp(ip);

					String sslStatu = (String) res.get("Ssl").toString();
					virtualServerGroup.setSslStatu(sslStatu);

					String sslTimeout = res.get("SslTimeout").toString();
					virtualServerGroup.setSslTimeout(sslTimeout);
if(Struts2Utils.IS_HA_ENABLE == true){
					String haType = res.get("HA").toString();
					virtualServerGroup.setHaType(haType);
//System.out.println("get ha : " + haType);
					String haName = getHaNameByType(haType);
					virtualServerGroup.setHaName(haName);
//System.out.println("get ha : " + haName);
					String haStatus = res.get("HAStatus").toString();
					virtualServerGroup.setHaStatus(haStatus);
}else{
	virtualServerGroup.setHaType("0");
	virtualServerGroup.setHaName("本地");
	virtualServerGroup.setHaStatus("false");
}
					String cookieEnabled = res.get("CookieEnabled").toString();
					virtualServerGroup.setCookieEnabled(cookieEnabled);

					String cookieName = res.get("CookieName").toString();
					virtualServerGroup.setCookieName(cookieName);

					String cookieExpire = res.get("CookieExpire").toString();
					virtualServerGroup.setCookieExpire(cookieExpire);

					// Object[] locationes= (Object[])res.get("LocationList");
					List<Location> locationList = new ArrayList<Location>();
					/*
					 * if ( locationes != null && locationes.length !=0 ) {
					 * 
					 * Location location = null;
					 * 
					 * for (int j = 0; j < locationes.length; j++) { location =
					 * new Location();
					 * 
					 * Map locationMap = (Map)locationes[j]; String locationId =
					 * locationMap.get("ID").toString(); String match =
					 * locationMap.get("Match").toString(); String groupName =
					 * locationMap.get("Group").toString();
					 * location.setServiceId(vsId);
					 * location.setLocationId(locationId);
					 * location.setMatch(match);
					 * location.setGroupName(groupName);
					 * 
					 * locationList.add(location); } }
					 */
					virtualServerGroup.setLocationList(locationList);
					//System.out.println(virtualServerGroup.getName() + " : "							+ virtualServerGroup);
					groupList.add(virtualServerGroup);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
		return groupList;
	}

	public VirtualServerGroup getVirtualServerById(String vsId) {
		List<VirtualServerGroup> groupList = this.getVirtualServerGroupList();
		if (groupList == null) {
			return null;
		}
		for (VirtualServerGroup virtualServerGroup : groupList) {
			if (virtualServerGroup.getVsId().equals(vsId)) {
				return virtualServerGroup;
			}
		}
		return null;
	}

	public VirtualServerGroup getVirtualServerByName(String newName) {
		List<VirtualServerGroup> groupList = this.getVirtualServerGroupList();
		if (groupList == null) {
			return null;
		}
		for (VirtualServerGroup virtualServerGroup : groupList) {
			if (virtualServerGroup.getName().equals(newName)) {
				return virtualServerGroup;
			}
		}
		return null;
	}

	public String addVirtualServer(VirtualServerGroup entity) {
		return this.setVirtualServer(false, entity);
	}

	public String updateVirtualServer(VirtualServerGroup entity) {
		return this.setVirtualServer(true, entity);
	}

	@SuppressWarnings("unchecked")
	public String setVirtualServer(boolean isUpdate, VirtualServerGroup entity) {
		String flagStr = "false";
		String methName = null;
		Hashtable entityHashtable = new Hashtable();
		//System.out.println(entity);
		if (isUpdate) {
			//System.out.println("update http service : " + entity.getVsId());
			methName = "Http.Service.Set";
			entityHashtable.put("ID", NumberUtils.toInt(entity.getVsId()));
		} else {
			//System.out.println("add http service : " + entity.getVsId());
			methName = "Http.Service.Add";
		}
		if (StringUtils.isNotEmpty(entity.getName())) {
			entityHashtable.put("Name", entity.getName());
		}
		if (StringUtils.isNotEmpty(entity.getDev())) {
			entityHashtable.put("Dev", entity.getDev());
		}
		if (StringUtils.isNotEmpty(entity.getVirtualIp())) {
			entityHashtable.put("IP", entity.getVirtualIp());
		}
		if (StringUtils.isNotEmpty(entity.getListenPort())) {
			entityHashtable.put("Port", NumberUtils.toInt(entity
					.getListenPort()));
		}
		if (StringUtils.isNotEmpty(entity.getSslStatu())) {
			boolean sslStatu = false;
			if ("true".equals(entity.getSslStatu())) {
				sslStatu = true;
			}
			entityHashtable.put("Ssl", sslStatu);
		}

		if (StringUtils.isNotEmpty(entity.getSslTimeout())) {
			entityHashtable.put("SslTimeout", NumberUtils.toInt(entity
					.getSslTimeout()));
		}

		if (StringUtils.isNotEmpty(entity.getCert())) {
			byte[] certBuf = entity.getCert().getBytes();
			entityHashtable.put("Cert", certBuf);
		}

		if (StringUtils.isNotEmpty(entity.getKey())) {
			byte[] keyBuf = entity.getKey().getBytes();
			entityHashtable.put("Key", keyBuf);
		}
if(Struts2Utils.IS_HA_ENABLE == true){
		if (StringUtils.isNotEmpty(entity.getHaType())) {
//System.out.println("set ha : " + NumberUtils.toInt(entity.getHaType()));
			entityHashtable.put("HA", NumberUtils.toInt(entity.getHaType()));
		}
}
		if (StringUtils.isNotEmpty(entity.getCookieEnabled())) {
			boolean statu = false;
			if ("true".equals(entity.getCookieEnabled())) {
				statu = true;
			}
			entityHashtable.put("CookieEnabled", statu);
		}

		if (StringUtils.isNotEmpty(entity.getCookieName())) {
			entityHashtable.put("CookieName", entity.getCookieName());
		}

		if (StringUtils.isNotEmpty(entity.getCookieExpire())) {
			entityHashtable.put("CookieExpire", NumberUtils.toInt(entity
					.getCookieExpire()));
		}

		try {
			Boolean result = (Boolean) executeXml(methName, entityHashtable);
			if (result != null && result == true) {
				flagStr = "true";
			}
		} catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}

	@SuppressWarnings("unchecked")
	public boolean deleteVirtualServer(String vsId) {
		boolean flag = false;
		try {
			Hashtable delTemp = new Hashtable();
			delTemp.put("ID", NumberUtils.toInt(vsId));
			Boolean delResult = (Boolean) executeXml("Http.Service.Del",
					delTemp);
			if (delResult != null && delResult == true) {
				flag = true;
			}
		} catch (Exception e) {
			flag = false;
			// e.printStackTrace();
		}
		return flag;
	}

	@SuppressWarnings("unchecked")
	public String setVirtualServerSslCert(String vsId, String certContext,
			String keyContext) {
		String flagStr = "false";
		String methName = "Http.Service.Set";
		Hashtable entityHashtable = new Hashtable();
		byte[] certBuf = certContext.getBytes();
		byte[] keyBuf = keyContext.getBytes();
		entityHashtable.put("ID", NumberUtils.toInt(vsId));
		entityHashtable.put("Cert", certBuf);
		entityHashtable.put("Key", keyBuf);

		try {
			Boolean result = (Boolean) executeXml(methName, entityHashtable);
			if (result != null && result == true) {
				flagStr = "true";
			}
		} catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;

	}

	@SuppressWarnings("unchecked")
	public List<Location> getLocatioList(String vsId) {

		List<Location> list = new ArrayList<Location>();

		try {
			Hashtable vsTemp = new Hashtable();
			vsTemp.put("ID", NumberUtils.toInt(vsId));

			Object result = (Object) executeXml("Http.Service.Get", vsTemp);

			if (result != null) {
				Map res = (Map) result;
				Object[] locationes = (Object[]) res.get("LocationList");
				if (locationes != null && locationes.length != 0) {
					Location location = null;

					for (int j = 0; j < locationes.length; j++) {
						location = new Location();

						Map locationMap = (Map) locationes[j];
						String locationId = locationMap.get("ID").toString();
						String match = locationMap.get("Match").toString();
						String groupName = locationMap.get("Group").toString();
						location.setServiceId(vsId);
						location.setLocationId(locationId);
						location.setMatch(match);
						location.setGroupName(groupName);

						list.add(location);
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
		return list;
	}

	public Location getServiceLocationById(String vsId, String locationId) {
		List<Location> list = this.getLocatioList(vsId);
		if (null == list) {
			return null;
		}
		for (Location location : list) {
			if (location.getLocationId().equals(locationId)) {
				return location;
			}
		}
		return null;
	}

	public String addServiceLocation(Location entity) {
		return this.setSericeLocation(false, entity);
	}

	public String updateServiceLocation(Location entity) {
		return this.setSericeLocation(true, entity);
	}

	@SuppressWarnings("unchecked")
	public String setSericeLocation(boolean isUpdate, Location entity) {
		String flagStr = "false";
		String methName = null;
		//System.out.println("Location set ==============");
		//System.out.println("isupdate : " + isUpdate);
		//System.out.println("entity : " + entity);
		Hashtable entityHashtable = new Hashtable();
		entityHashtable.put("ServiceID", NumberUtils.toInt(entity
				.getServiceId()));
		if (isUpdate) {
			methName = "Http.Service.Location.Set";
			entityHashtable
					.put("ID", NumberUtils.toInt(entity.getLocationId()));
		} else {
			methName = "Http.Service.Location.Add";
			if(StringUtils.isNotEmpty(entity.getInsertId())){
				entityHashtable
				.put("ID", NumberUtils.toInt(entity.getInsertId()));
			}
		}
		entityHashtable.put("Match", entity.getMatch());
		entityHashtable.put("Group", entity.getGroupName());

		try {
			Boolean result = (Boolean) executeXml(methName, entityHashtable);
			if (result != null && result == true) {
				flagStr = "true";
			}
		} catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}

	@SuppressWarnings("unchecked")
	public boolean deleteServiceLocation(String vsId, String locationId) {
		boolean flag = false;
		try {
			Hashtable delTemp = new Hashtable();
			delTemp.put("ServiceID", NumberUtils.toInt(vsId));
			delTemp.put("ID", NumberUtils.toInt(locationId));
			Boolean delResult = (Boolean) executeXml(
					"Http.Service.Location.Del", delTemp);
			if (delResult != null && delResult == true) {
				flag = true;
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

}
