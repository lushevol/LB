package org.kylin.klb.service.nginx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.kylin.klb.entity.nginx.RealServerGroup;
import org.kylin.klb.entity.nginx.ServerItem;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.service.KlbClient;
import org.kylin.klb.util.Utils;

public class NginxRealServService extends KlbClient {

	private static Logger log = Logger.getLogger(NginxRealServService.class);

	private Map<String, String> getMethodMap() {
		Map<String, String> methodMap = new HashMap<String, String>();
		methodMap.put("0", "轮询");
		methodMap.put("1", "加权轮询");
		methodMap.put("2", "源地址Hash");
		methodMap.put("3", "最短响应时间");
		methodMap.put("4", "URL路径Hash");
		methodMap.put("5", "Cookie会话保持");
		methodMap.put("6", "Cookie-Tomcate");
		methodMap.put("7", "Cookie-Resin");
		methodMap.put("8", "代理源地址Hash");
		return methodMap;
	}

	private String getMethodDisplayByMethodId(String methodId) {
		Map<String, String> methodMap = this.getMethodMap();
		if (methodMap.containsKey(methodId)) {
			return methodMap.get(methodId);
		}
		return "未知算法";
	}

	public List<Display> getMethodDisplayList() {
		Map<String, String> methodMap = this.getMethodMap();
		List<Display> list = new ArrayList<Display>();
		for (String string : methodMap.keySet()) {
			Display display = new Display(methodMap.get(string), string);
			list.add(display);
		}
		return list;
	}

	private Map<String, String> getServerTypeMap() {
		Map<String, String> typeMap = new HashMap<String, String>();
		typeMap.put("0", "正常");
		typeMap.put("1", "关闭");
		typeMap.put("2", "备份");
		return typeMap;
	}

	private String getServerTypeByType(String type) {
		Map<String, String> typeMap = this.getServerTypeMap();
		if (typeMap.containsKey(type)) {
			return typeMap.get(type);
		}
		return "未知类型";
	}

	public List<Display> getServerTypeList() {
		Map<String, String> typeMap = this.getServerTypeMap();
		List<Display> list = new ArrayList<Display>();
		for (String string : typeMap.keySet()) {
			Display display = new Display(typeMap.get(string), string);
			list.add(display);
		}
		return list;
	}

	public List<Display> getRealGroupNameDisplayList() {
		List<RealServerGroup> groupList = this.getRealServerGroupList();
		List<Display> displayList = new ArrayList<Display>();
		for (RealServerGroup realServerGroup : groupList) {
			Display display = new Display(realServerGroup.getName(),
					realServerGroup.getName());
			displayList.add(display);
		}
		return displayList;
	}

	@SuppressWarnings("unchecked")
	public List<RealServerGroup> getRealServerGroupList() {
		List<RealServerGroup> groupList = new ArrayList<RealServerGroup>();

		try {
			Hashtable vsTemp = new Hashtable();
			Object[] result = (Object[]) executeXml("Http.Group.List", vsTemp);
			if (result != null) {
				RealServerGroup entity = null;

				for (int i = 0; i < result.length; i++) {
					entity = new RealServerGroup();

					Map res = (Map) result[i];

					String rsgId = res.get("ID").toString();
					entity.setRsgId(rsgId);

					String name = res.get("Name").toString();
					entity.setName(name);

					String method = res.get("Method").toString();
					entity.setMethod(method);

					entity.setMethodName(this
							.getMethodDisplayByMethodId(method));

					// Object[] serverItems= (Object[])res.get("ServerList");
					List<ServerItem> serverItemList = new ArrayList<ServerItem>();
					/*
					 * if ( serverItems != null && serverItems.length !=0 ) {
					 * 
					 * ServerItem serverItem = null;
					 * 
					 * for (int j = 0; j < serverItems.length; j++) { serverItem =
					 * new ServerItem();
					 * 
					 * Map serverMap = (Map)serverItems[j]; String serverId =
					 * serverMap.get("ID").toString(); String ip =
					 * serverMap.get("IP").toString(); String port =
					 * serverMap.get("Port").toString(); String weight =
					 * serverMap.get("Weight").toString(); String maxFails =
					 * serverMap.get("MaxFails").toString(); String failTimeout =
					 * serverMap.get("FailTimeout").toString(); String type =
					 * serverMap.get("Type").toString();
					 * 
					 * serverItem.setRsgId(rsgId);
					 * serverItem.setServerId(serverId); serverItem.setIp(ip);
					 * serverItem.setPort(port); serverItem.setWeight(weight);
					 * serverItem.setMaxFails(maxFails);
					 * serverItem.setFailTimeout(failTimeout);
					 * serverItem.setType(type);
					 * 
					 * serverItemList.add(serverItem); } }
					 */
					entity.setServerList(serverItemList);
					//System.out.println(entity.getName() + " : " + entity);
					groupList.add(entity);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
		return groupList;
	}

	public RealServerGroup getRealServerGroupById(String rsgId) {
		List<RealServerGroup> groupList = this.getRealServerGroupList();
		if (groupList == null) {
			return null;
		}
		for (RealServerGroup realServerGroup : groupList) {
			if (realServerGroup.getRsgId().equals(rsgId)) {
				return realServerGroup;
			}
		}
		return null;
	}

	public RealServerGroup getRealServerGroupByName(String newName) {
		List<RealServerGroup> groupList = this.getRealServerGroupList();
		if (groupList == null) {
			return null;
		}
		for (RealServerGroup realServerGroup : groupList) {
			if (realServerGroup.getName().equals(newName)) {
				return realServerGroup;
			}
		}
		return null;
	}

	public String addRealServerGroup(RealServerGroup entity) {
		return this.setRealServerGroup(false, entity);
	}

	public String updateRealServerGroup(RealServerGroup entity) {
		return this.setRealServerGroup(true, entity);
	}

	@SuppressWarnings("unchecked")
	public String setRealServerGroup(boolean isUpdate, RealServerGroup entity) {
		String flagStr = "false";
		String methName = null;
		Hashtable entityHashtable = new Hashtable();

		//System.out.println(entity);
		if (isUpdate) {
			//System.out.println("update http server group : "	+ entity.getRsgId());
			methName = "Http.Group.Set";
			entityHashtable.put("ID", NumberUtils.toInt(entity.getRsgId()));
		} else {
			//System.out.println("add http server group : " + entity.getRsgId());
			methName = "Http.Group.Add";
		}
		entityHashtable.put("Name", entity.getName());
		entityHashtable.put("Method", NumberUtils.toInt(entity.getMethod()));

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
	public boolean deleteRealServerGroup(String rsgId) {
		boolean flag = false;
		try {
			Hashtable delTemp = new Hashtable();
			delTemp.put("ID", NumberUtils.toInt(rsgId));
			Boolean delResult = (Boolean) executeXml("Http.Group.Del", delTemp);
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
	public List<ServerItem> getServerItemList(String rsgId) {
		List<ServerItem> list = new ArrayList<ServerItem>();
		//System.out.println("rsgId : " + NumberUtils.toInt(rsgId));
		try {
			Hashtable vsTemp = new Hashtable();
			vsTemp.put("ID", NumberUtils.toInt(rsgId));

			Object result = (Object) executeXml("Http.Group.Get", vsTemp);

			if (result != null) {
				Map res = (Map) result;
				Object[] serverItems = (Object[]) res.get("ServerList");
				if (serverItems != null && serverItems.length != 0) {
					ServerItem serverItem = null;

					for (int j = 0; j < serverItems.length; j++) {
						serverItem = new ServerItem();

						Map serverMap = (Map) serverItems[j];
						String serverId = serverMap.get("ID").toString();
						String ip = serverMap.get("IP").toString();
						String port = serverMap.get("Port").toString();
						String weight = serverMap.get("Weight").toString();
						String maxFails = serverMap.get("MaxFails").toString();
						String failTimeout = serverMap.get("FailTimeout")
								.toString();
						String type = serverMap.get("Type").toString();
						String typeName = this.getServerTypeByType(type);
						String srunId = serverMap.get("SrunID").toString();

						serverItem.setRsgId(rsgId);
						serverItem.setServerId(serverId);
						serverItem.setIp(ip);
						serverItem.setPort(port);
						serverItem.setWeight(weight);
						serverItem.setMaxFails(maxFails);
						serverItem.setFailTimeout(failTimeout);
						serverItem.setType(type);
						serverItem.setTypeName(typeName);
						serverItem.setSrunId(srunId);

						list.add(serverItem);
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

	public ServerItem getServerItemById(String rsgId, String serverId) {
		List<ServerItem> list = this.getServerItemList(rsgId);
		if (null == list) {
			return null;
		}
		for (ServerItem serverItem : list) {
			if (serverItem.getServerId().equals(serverId)) {
				return serverItem;
			}
		}
		return null;
	}

	public String addServerItem(ServerItem entity) {
		return this.setServerItem(false, entity);
	}

	public String updateServerItem(ServerItem entity) {
		return this.setServerItem(true, entity);
	}

	@SuppressWarnings("unchecked")
	public String setServerItem(boolean isUpdate, ServerItem entity) {
		String flagStr = "false";
		String methName = null;
		//System.out.println(entity);

		Hashtable entityHashtable = new Hashtable();
		entityHashtable.put("GroupID", NumberUtils.toInt(entity.getRsgId()));
		if (isUpdate) {
			methName = "Http.Group.Server.Set";
			entityHashtable.put("ID", NumberUtils.toInt(entity.getServerId()));
		} else {
			methName = "Http.Group.Server.Add";
		}
		if (StringUtils.isNotEmpty(entity.getIp())) {
			entityHashtable.put("IP", entity.getIp());
		}

		if (StringUtils.isNotEmpty(entity.getPort())) {
			entityHashtable.put("Port", NumberUtils.toInt(entity.getPort()));
		}

		if (StringUtils.isNotEmpty(entity.getWeight())) {
			entityHashtable
					.put("Weight", NumberUtils.toInt(entity.getWeight()));
		}

		if (StringUtils.isNotEmpty(entity.getMaxFails())) {
			entityHashtable.put("MaxFails", NumberUtils.toInt(entity
					.getMaxFails()));
		}

		if (StringUtils.isNotEmpty(entity.getFailTimeout())) {
			entityHashtable.put("FailTimeout", NumberUtils.toInt(entity
					.getFailTimeout()));
		}

		if (StringUtils.isNotEmpty(entity.getType())) {
			entityHashtable.put("Type", NumberUtils.toInt(entity.getType()));
		}

		if (StringUtils.isNotEmpty(entity.getSrunId())) {
			entityHashtable.put("SrunID", entity.getSrunId());
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
	public boolean deleteServerItem(String rsgId, String serverId) {
		boolean flag = false;
		try {
			Hashtable delTemp = new Hashtable();
			delTemp.put("GroupID", NumberUtils.toInt(rsgId));
			delTemp.put("ID", NumberUtils.toInt(serverId));
			Boolean delResult = (Boolean) executeXml("Http.Group.Server.Del",
					delTemp);
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
