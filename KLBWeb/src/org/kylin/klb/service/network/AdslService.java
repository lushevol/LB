package org.kylin.klb.service.network;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.xmlrpc.XmlRpcException;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.kylin.klb.entity.network.AdslAttribute;
import org.kylin.klb.entity.network.AdslInfo;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.service.KlbClient;
import org.kylin.klb.util.Utils;

public class AdslService extends KlbClient {
	
	/* public List<Display> getInterfaceList(){
		List<Display> ret = new ArrayList<Display>();
		Display display = new Display("ppp0", "ppp0");
		ret.add(display);
		display = new Display("ppp1", "ppp1");
		ret.add(display);
		display = new Display("ppp2", "ppp2");
		ret.add(display);
		return ret;
	} */
	
	public List<Display> getEthList(){
		List<Display> ret = new ArrayList<Display>();		
		try {			
			Object[] result = (Object[])executeXml("Bonding.GetAll");
			String interBonded = new String();
			if ( result != null && result.length != 0 ) {				
				for (int i = 0; i < result.length; i++) {					
					Map res = (Map)result[i];
					//String bondName = (String)res.get("Dev");
					Object[] slaves = (Object[])res.get("Slaves");
										
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
					//String adsl = (String)res.get("Adsl");
					if ( !interBonded.contains(ethName) ) {
						Display interEth = new Display(ethName, ethName);
						ret.add(interEth);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/* public List<String> getEthernets() {
		List<String> ethernets = new ArrayList<String>();
		Document doc = getDocumentByInputStream(xmlName);
		doc.getRootElement().element("Network").element("Interfaces").addElement("Ethernet").addAttribute("get", "1");
		String result = (String) executeXml(methodName, doc.asXML());
		try {
			Document document = DocumentHelper.parseText(result);
			Element eth = document.getRootElement().element("Network").element("Interfaces").element("Ethernet");
			if (!eth.elements().isEmpty()) {
				for (Iterator i = eth.elementIterator(); i.hasNext();) {
					Element e = (Element) i.next();
					ethernets.add(e.getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ethernets;
	}
	
	public String constructXmlForAdslInfo() {
		Document doc = getDocumentByInputStream(xmlName);
		Element ethernet = doc.getRootElement().element("Network").element("Interfaces").addElement("Ethernet");
		List<String> ethernets = getEthernets();
		if(ethernets == null){
			return null;
		}
		Iterator<String> i = ethernets.iterator();
		while(i.hasNext()){
			String eth = i.next();
			Element adsl = ethernet.addElement(eth).addElement("Adsl");
			adsl.addElement("Enabled").addAttribute("get", "1");
			adsl.addElement("Device").addAttribute("get", "1");
			adsl.addElement("Dns").addAttribute("get", "1");
			adsl.addElement("Gate").addAttribute("get", "1");
			adsl.addElement("IP").addAttribute("get", "1");
			adsl.addElement("MTU").addAttribute("get", "1");
			adsl.addElement("Mode").addAttribute("get", "1");
			adsl.addElement("Password").addAttribute("get", "1");
			adsl.addElement("Status").addAttribute("get", "1");
			adsl.addElement("Timeout").addAttribute("get", "1");
			adsl.addElement("User").addAttribute("get", "1");
		}
		return doc.asXML();
	} */
	
	public AdslAttribute getAdslAttribute(String inter){
		AdslAttribute aa = new AdslAttribute();															
		try {			
			Hashtable adslTemp = new Hashtable();
			adslTemp.put("Dev", inter);			
			Object result = (Object)executeXml("Adsl.Get",adslTemp);
			
			if (result != null) {
				Map res = (Map)result;
				Integer id = (Integer)res.get("ID");
				String descrip = (String)res.get("Description");
				String dev = (String)res.get("Dev");
				String ethernet = (String)res.get("Ethernet");
				String user = (String)res.get("User");
				String password = (String)res.get("Password");
				Integer timeout = (Integer)res.get("Timeout");
				String ip = (String)res.get("IP");
				String dns = (String)res.get("Dns");
				String gate = (String)res.get("Gate");
				Integer mtu = (Integer)res.get("MTU");
				Integer status = (Integer)res.get("Status");
				Integer rx = (Integer)res.get("RX");
				Integer tx = (Integer)res.get("TX");
				Integer time = (Integer)res.get("Time");
				
				System.out.println(id);
				System.out.println(descrip);
				System.out.println(dev);
				System.out.println(ethernet);
				System.out.println(user);
				System.out.println(password);
				System.out.println(timeout);
				System.out.println(ip);
				System.out.println(dns);
				System.out.println(gate);
				System.out.println(mtu);
				System.out.println(status);
				System.out.println(rx);
				System.out.println(tx);
				System.out.println(time);
				
				aa.setInter(inter);
				aa.setEth(ethernet);
				aa.setDescribe(descrip);
				aa.setUser(user);								
				aa.setPassword(password);
				aa.setMtu(mtu.toString());
				aa.setTimeout(timeout.toString());
								
			}
									
			return aa;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<AdslInfo> getAdslInfoList(){
		List<AdslInfo> ret = new ArrayList<AdslInfo>();
		
		try {
			Object[] result = (Object[])executeXml("Adsl.GetAll");			
			if (result != null) {				
				for (int i = 0; i < result.length; i++) {
					Map res = (Map)result[i];
					Integer id = (Integer)res.get("ID");
					String descrip = (String)res.get("Description");
					String dev = (String)res.get("Dev");
					String ethernet = (String)res.get("Ethernet");
					String user = (String)res.get("User");
					String password = (String)res.get("Password");
					Integer timeout = (Integer)res.get("Timeout");
					String ip = (String)res.get("IP");
					String dns = (String)res.get("Dns");
					String gate = (String)res.get("Gate");
					Integer mtu = (Integer)res.get("MTU");
					Integer status = (Integer)res.get("Status");
					Integer rx = (Integer)res.get("RX");
					Integer tx = (Integer)res.get("TX");
					Integer time = (Integer)res.get("Time");
					
					/* System.out.println(id);
					System.out.println(descrip);
					System.out.println(dev);
					System.out.println(ethernet);
					System.out.println(user);
					System.out.println(password);
					System.out.println(timeout);
					System.out.println(ip);
					System.out.println(dns);
					System.out.println(gate);
					System.out.println(mtu);
					System.out.println(status);
					System.out.println(rx);
					System.out.println(tx);
					System.out.println(time); */
					
					AdslInfo temp = new AdslInfo();
					temp.setInter(dev);
					temp.setDescribe(descrip);
					temp.setIp(ip);
					temp.setRx(rx.toString());
					temp.setTx(tx.toString());
					temp.setPersist(time.toString());
					temp.setStatus(status.toString());
					
					ret.add(temp);
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ret;
	}
	public String addAdsl(AdslAttribute aa) {
		Hashtable addTemp = new Hashtable();
		addTemp.put("Description", aa.getDescribe());
		addTemp.put("Dev", aa.getInter());
		addTemp.put("Ethernet", aa.getEth());
		addTemp.put("User", aa.getUser());
		addTemp.put("Password", aa.getPassword());
		addTemp.put("Timeout", NumberUtils.toInt(aa.getTimeout()));
		addTemp.put("MTU", NumberUtils.toInt(aa.getMtu()));
		
		String flagStr = "false";
		try {
			Boolean addResult = (Boolean)executeXml("Adsl.Add",addTemp);
			System.out.println(addResult);
			flagStr = "true";
		} catch (XmlRpcException e) {	
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}
	
	public Boolean delAdslInfo(String inter) {		
		boolean flag = false;
		try {
			Hashtable delTemp = new Hashtable();
			delTemp.put("Dev", inter);
			Boolean delResult = (Boolean)executeXml("Adsl.Delete",delTemp);
			System.out.println(delResult);
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;								
	}
	
	public String editAdslAttribute(AdslAttribute aa) {								
		Hashtable setTemp = new Hashtable();
		setTemp.put("Dev", aa.getInter());
		setTemp.put("User", aa.getUser());
		setTemp.put("Password", aa.getPassword());
		setTemp.put("Timeout", NumberUtils.toInt(aa.getTimeout()));
		setTemp.put("MTU", NumberUtils.toInt(aa.getMtu()));
			
		Hashtable descriTemp = new Hashtable();
		descriTemp.put("Dev", aa.getInter());
		descriTemp.put("Description", aa.getDescribe());
		
		String flagStr = "false";	
		try {
			Boolean setResult = (Boolean)executeXml("Adsl.Set",setTemp);
			System.out.println(setResult);
						
			Boolean descriResult = (Boolean)executeXml("Adsl.Description",descriTemp);
			flagStr = "true";
		} catch (XmlRpcException e) {	
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}
	
	public String editAdslState (String inter, String mode) {
		String flagStr = "false";	
		try {
			if (StringUtils.equals(mode, "dial")){
				Hashtable dialTemp = new Hashtable();
				dialTemp.put("Dev", inter);
				Boolean dialResult = (Boolean)executeXml("Adsl.Dial",dialTemp);
			} else {
				Hashtable stopTemp = new Hashtable();
				stopTemp.put("Dev", inter);
				Boolean stopResult = (Boolean)executeXml("Adsl.Stop",stopTemp);
			}
			flagStr = "true";
		} catch (XmlRpcException e) {	
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}	
}
