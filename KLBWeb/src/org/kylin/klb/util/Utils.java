package org.kylin.klb.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;
import org.kylin.modules.utils.StringUtils;

public class Utils {
	private Map<String, String> forward = new HashMap();
	private Map<String, String> scheduling = new HashMap();
	private Map<String, String> checkResults = new HashMap();
	private String failedMess = new String();
	private static Utils instance = null;

	public static synchronized Utils getInstance() {
		if (instance == null)
			instance = new Utils();
		return instance;
	}
	
	public static String GetExceptionMessage(XmlRpcException error) {
		int code = error.code;
		//String[] params=error.getMessage().split("\n");

		StringReader sr = new StringReader(error.getMessage());
		BufferedReader br = new BufferedReader(sr);
		String param0 = null;
		String param1 = null;
		String param2 = null;
		try {
			param0 = br.readLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			param1 = br.readLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			param2 = br.readLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		}		
		
		if(ExceptionUtils.IsExist(code)) {
			String format = ExceptionUtils.GetFormat(code);
			int count=ExceptionUtils.GetCount(code);
			switch (count) {
				case 3:
					return String.format(format, param0, param1, param2);
				case 2:
					return String.format(format, param0, param1);
				case 1:
					return String.format(format, param0);
				default:
					return String.format(format);
			}
		} else {
			return String.format("未知错误. 错误码(%s)", error.code);
		}
	}

	public Utils() {		
		this.forward.put("0", "直接路由");
		this.forward.put("1", "网络地址转换");
		this.forward.put("2", "IP隧道");

		this.scheduling.put("0", "轮询调度");
		this.scheduling.put("1", "加权轮询调度");
		this.scheduling.put("2", "最少链接调度");
		this.scheduling.put("3", "加权最少链接调度");
		this.scheduling.put("4", "基于局部性的最少链接调度");
		this.scheduling.put("5", "带复制的基于局部性最少链接");
		this.scheduling.put("6", "目标地址散列调度");
		this.scheduling.put("7", "源地址散列调度");
		this.scheduling.put("8", "最短期望延迟");
		this.scheduling.put("9", "无须队列等待");

		this.checkResults.put("0", "warntime, deadtime, initdead之间的关系;错误.");
		this.checkResults.put("1", "高可用设备接口;为空.");
		this.checkResults.put("2", "高可用集群的多播地址;无效.");
		this.checkResults.put("3", "heartbeat使用方式;错误,做高可用时高可用设备的数量不为2.");
		this.checkResults.put("100", "接口配置;错误,包括接口IP地址和网络掩码配置错误.");
		this.checkResults.put("101", "路由表项接口;为空或者主机地址错误,包括网络地址和子网掩码错误.");
		this.checkResults.put("102", "目标地址;错误,包括IP地址和子网掩码错误.");
		this.checkResults.put("200", "虚拟服务接口;为空或者主机地址错误,包括IP地址和子网掩码错误.");
		this.checkResults.put("201", "虚拟服务持久连接;的值大于0时持久连接的网络掩码配置错误.");
		this.checkResults.put("202", "虚拟服务;错误,TCP和UDP端口为空.");
		this.checkResults.put("203", "虚拟服务使用的真实服务器数量;为空.");
		this.checkResults.put("204", "虚拟服务中真实服务器的IP地址;无效.");
	}

	public Map<String, String> getForwards() {
		return this.forward;
	}

	public String getForward(String forward) {
		if ((this.forward != null) && (StringUtils.isNotEmpty(forward))) {
			String key = forward.toLowerCase();
			if (StringUtils.equals(key, "tunnel"))
				key = "Tunnel";
			if (StringUtils.equals(key, "route"))
				key = "Route";
			if (StringUtils.equals(key, "masq"))
				key = "Masq";
			if (this.forward.containsKey(key)) {
				return (String) this.forward.get(key);
			}
		}
		return null;
	}

	public Map<String, String> getSchedulings() {
		return this.scheduling;
	}

	public String getScheduling(String scheduling) {
		if ((this.scheduling != null) && (StringUtils.isNotEmpty(scheduling))) {
			String key = scheduling.toLowerCase();
			if (this.scheduling.containsKey(key)) {
				return (String) this.scheduling.get(key);
			}
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		String str = "　a 　b 　c 　";

	}

	public Map<String, String> getCheckResults() {
		return this.checkResults;
	}

	public String getFailedMess() {
		return failedMess;
	}

	public void setFailedMess(String failedMess) {
		this.failedMess = failedMess;
	}

}
