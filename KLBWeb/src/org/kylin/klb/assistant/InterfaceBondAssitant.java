package org.kylin.klb.assistant;

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
import org.kylin.klb.entity.network.Bond;
import org.kylin.klb.entity.network.BondAttribute;
import org.kylin.klb.entity.network.BondConfig;
import org.kylin.klb.entity.network.BondInfo;
import org.kylin.klb.entity.network.InterfaceInfo;
import org.kylin.klb.service.KlbClient;

public class InterfaceBondAssitant extends KlbClient {
	private static String methodName = "Execute";
	private static List<Bond> bondList = new ArrayList<Bond>();

	/* public List<String> getBonds() {
		List<String> bonds = new ArrayList<String>();
		String xmlName = "interfaces.xml";
		Document doc = getDocumentByInputStream(xmlName);
		doc.getRootElement().element("Network").element("Interfaces")
				.addElement("Bonding").addAttribute("get", "1");
		String result = (String) executeXml(methodName, doc.asXML());
		try {
			Document document = DocumentHelper.parseText(result);
			Element bond = document.getRootElement().element("Network")
					.element("Interfaces").element("Bonding");
			if (!bond.elements().isEmpty()) {
				for (Iterator i = bond.elementIterator(); i.hasNext();) {
					Element e = (Element) i.next();
					bonds.add(e.getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return bonds;
	}

	public String constructXmlForBondInfo() {
		String xmlName = "interfaces.xml";
		Document doc = getDocumentByInputStream(xmlName);
		Element Bonding = doc.getRootElement().element("Network").element("Interfaces").addElement("Bonding");
		List<String> bonds = getBonds();
		if (bonds == null) {
			return null;
		}
		Iterator<String> i = bonds.iterator();
		while (i.hasNext()) {
			String bond = i.next();
			Bonding.addElement(bond);
			Bonding.element(bond).addElement("Address")
					.addAttribute("get", "1");
			Bonding.element(bond).addElement("ArpState").addAttribute("get",
					"1");
			Bonding.element(bond).addElement("Description").addAttribute("get",
					"1");
			Bonding.element(bond).addElement("Dhcp").addElement("Enabled")
					.addAttribute("get", "1");
			Bonding.element(bond).addElement("Duplex").addAttribute("get", "1");
			Bonding.element(bond).addElement("Enabled")
					.addAttribute("get", "1");
			Bonding.element(bond).addElement("IP").addAttribute("get", "1");
			Bonding.element(bond).addElement("MTU").addAttribute("get", "1");
			Bonding.element(bond).addElement("Device").addAttribute("get", "1");
			Bonding.element(bond).addElement("Mode").addAttribute("get", "1");
			Bonding.element(bond).addElement("LinkCheck").addAttribute("get", "1");
			Bonding.element(bond).addElement("Status").addElement("Link")
					.addAttribute("get", "1");
			Bonding.element(bond).addElement("Status")
					.addElement("RealAddress").addAttribute("get", "1");
		}
		return doc.asXML();
	} */

	public List<BondInfo> getBondInfoList() {		
		List<BondInfo> bondInfoList = new ArrayList<BondInfo>();
		BondInfo bi = new BondInfo();

		try {
			Object[] result = (Object[])executeXml("Bonding.GetAll");
			
			if (result != null) {				
				for (int i = 0; i < result.length; i++) {
					
					Map res = (Map)result[i];
					String name = (String)res.get("Dev");
					Object[] slaves = (Object[])res.get("Slaves");
					Integer  mode = (Integer )res.get("Mode");
					Integer  checkMode = (Integer )res.get("CheckMode");
					Integer  frequency = (Integer )res.get("Frequency");
					Object[] checkIP = (Object[])res.get("CheckIP");
					String descrip = (String)res.get("Description");
					Object[] ip = (Object[])res.get("IP");
					Boolean enabled = (Boolean)res.get("Enabled");
					Integer  mtu = (Integer )res.get("MTU");
					String address = (String)res.get("Address");
					String currentAddress = (String)res.get("CurrentAddress");
					Boolean dhcp = (Boolean)res.get("Dhcp");
					Integer  arp = (Integer )res.get("Arp");
					Boolean carrier = (Boolean)res.get("Carrier");
					
					/* System.out.println(name);
					System.out.println(slaves);
					System.out.println(mode);
					System.out.println(checkMode);
					System.out.println(frequency);
					System.out.println(checkIP);
					System.out.println(descrip);
					System.out.println(ip);
					System.out.println(enabled);
					System.out.println(mtu);
					System.out.println(address);
					System.out.println(currentAddress);
					System.out.println(dhcp);
					System.out.println(arp);
					System.out.println(carrier); */
					
					if (name != null) {
						bi.setName(name);
					}
					bi.setAlgorithm(mode.toString());
					bi.setMonitor(checkMode.toString());
					
					String interBonded = new String();
					if(slaves.length==0){
						interBonded = "";
					}
					else{
						for(int j=0; j<slaves.length; j++){
							if (!StringUtils.equals((String)slaves[j], "")) {
								interBonded += slaves[j] + ";";
							}
						}
						interBonded = interBonded.substring(0, interBonded.length()-1);
					}
					bi.setInterBonded(interBonded);
					String interMode = new String();
					String dh = dhcp.toString();
					if(StringUtils.equals(dh, "true")){
						interMode = "dhcp";
					} else {
						interMode = "route";
					}					
					bi.setInterMode(interMode);
					
					String ipList = new String();
					if(ip.length==0){
						ipList = "";
					} else {
						for(int j=0; j<ip.length; j++) {
							if (!StringUtils.equals((String)ip[j], "")) {
								ipList += ip[j] + ";";
							}
						}
						ipList = ipList.substring(0, ipList.length()-1);
					}
					String[] addrArray = ipList.split(";");
					if (addrArray.length > 1) {
						bi.setIpAddr(addrArray[0] + " ...");
					} else {
						bi.setIpAddr(addrArray[0]);
					}
					
					bi.setState(enabled.toString());					
					bondInfoList.add(bi);
					bi = new BondInfo();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return bondInfoList;
	}

	public void addBond(BondConfig bc) {
		Bond b = new Bond();
		b.setName(bc.getName());
		b.setAlgorithm(bc.getAlgorithm());
		b.setMonitor(bc.getMonitor());
		b.setInterval(bc.getInterval());
		b.setIpList(bc.getIpList());
		b.setInterBonded(bc.getInterBonded());
		b.setInterMode("route");
		b.setState("false");
		bondList.add(b);
	}

	public boolean editBondAttribute(BondAttribute ba) throws XmlRpcException {
		boolean status = false;
		
		Hashtable descriTemp = new Hashtable();
		descriTemp.put("Dev", ba.getName());
		descriTemp.put("Description", ba.getDescription());
		Boolean descriResult = (Boolean)executeXml("Bonding.Description",descriTemp);
		System.out.println(descriResult);
		
		Hashtable enableTemp = new Hashtable();
		enableTemp.put("Dev", ba.getName());
		Boolean bool = null;
		if ( ba.getState().equals("true") ) {
			bool = true;
		} else {
			bool = false;
		}
		enableTemp.put("Enabled", bool);
		Boolean enableResult = (Boolean)executeXml("Bonding.Enabled",enableTemp);
		System.out.println(enableResult);
		
		Hashtable dhcpTemp = new Hashtable();
		dhcpTemp.put("Dev", ba.getName());		
		dhcpTemp.put("Dhcp", ba.getDhcp().equals("true") ? true : false);
		Boolean dhcpResult = (Boolean)executeXml("Bonding.Dhcp", dhcpTemp);
		System.out.println(dhcpResult);
		
		if( ba.getDhcp().equals("false") ){
			Hashtable ipTemp = new Hashtable();
			ipTemp.put("Dev", ba.getName());
			
			String ipStr = ba.getAddressList();
			String[] ipArray = null;
			if (ipStr.equals("")) {
				ipArray = new String[0];
			} else {
				ipArray = ba.getAddressList().split(";");
			}
			ipTemp.put("IP", ipArray);
			Boolean ipResult = (Boolean)executeXml("Bonding.IP",ipTemp);
			System.out.println(ipResult);
		}
		
		Hashtable mtuTemp = new Hashtable();
		mtuTemp.put("Dev", ba.getName());
		mtuTemp.put("MTU", NumberUtils.toInt(ba.getMtu()));
		Boolean mtuResult = (Boolean)executeXml("Bonding.MTU", mtuTemp);
		System.out.println(mtuResult);
		
		Hashtable macTemp = new Hashtable();
		macTemp.put("Dev", ba.getName());
		macTemp.put("Address", ba.getMac());
		Boolean macResult = (Boolean)executeXml("Bonding.Address", macTemp);
		System.out.println(macResult);
		
		Hashtable arpTemp = new Hashtable();
		arpTemp.put("Dev", ba.getName());
		arpTemp.put("Arp", NumberUtils.toInt(ba.getArpState()));
		Boolean arpResult = (Boolean)executeXml("Bonding.Arp", arpTemp);
		System.out.println(arpResult);
				
		if (enableResult != null && mtuResult != null && macResult != null && arpResult != null) {
			status = true;
		}
		return status;
	}

	public boolean editBondConfig (String oldName, BondConfig bc) throws XmlRpcException {
		boolean status = false;
		
		Hashtable modeTemp = new Hashtable();
		modeTemp.put("Dev", bc.getName());
		modeTemp.put("Mode", NumberUtils.toInt(bc.getAlgorithm()));
		Boolean modeResult = (Boolean)executeXml("Bonding.Mode",modeTemp);
		System.out.println(modeResult);
		
		Hashtable checkTemp = new Hashtable();
		checkTemp.put("Dev", bc.getName());
		checkTemp.put("CheckMode", NumberUtils.toInt(bc.getMonitor()));		
		checkTemp.put("Frequency", NumberUtils.toInt(bc.getInterval()));
		Boolean checkResult = (Boolean)executeXml("Bonding.Check",checkTemp);
		System.out.println(checkResult);
		
		if(StringUtils.equals(bc.getMonitor(), "1") ){
			Hashtable checkIpTemp = new Hashtable();
			checkIpTemp.put("Dev", bc.getName());			
			
			String ipStr = bc.getIpList();
			String[] ipArray = null;
			if (ipStr.equals("")) {
				ipArray = new String[0];
			} else {
				ipArray = bc.getIpList().split(";");
			}			
			checkIpTemp.put("CheckIP", ipArray);
			Boolean checkIpResult = (Boolean)executeXml("Bonding.CheckIP",checkIpTemp);
			System.out.println(checkIpResult);
		}
				
		Hashtable slaveTemp = new Hashtable();
		slaveTemp.put("Dev", bc.getName());
			
		String[] slaveArray = null;
		if (bc.getInterBonded().equals("")) {
			slaveArray = new String[0];
		} else {
			slaveArray = bc.getInterBonded().split(";");
		}			
		slaveTemp.put("Slaves", slaveArray);
		Boolean slaveResult = (Boolean)executeXml("Bonding.SetSlaves",slaveTemp);			
		System.out.println(slaveResult);
		if ( modeResult != null && modeResult == true && (checkResult != null && checkResult == true) ) {
			status = true;
		}
		return status;
	}

	public BondAttribute getBondAttributeByName(String name) {
		BondAttribute ba = new BondAttribute();
		
		try {
			Hashtable ethTemp = new Hashtable();
			ethTemp.put("Dev", name);
			Object result = (Object)executeXml("Bonding.Get",ethTemp);
			
			if (result != null) {
				Map res = (Map)result;
				Object[] slaves = (Object[])res.get("Slaves");
				Integer  mode = (Integer )res.get("Mode");
				Integer  checkMode = (Integer )res.get("CheckMode");
				Integer  frequency = (Integer )res.get("Frequency");
				Object[] checkIP = (Object[])res.get("CheckIP");
				String descrip = (String)res.get("Description");
				Object[] ip = (Object[])res.get("IP");
				Boolean enabled = (Boolean)res.get("Enabled");
				Integer  mtu = (Integer )res.get("MTU");
				String address = (String)res.get("Address");
				String currentAddress = (String)res.get("CurrentAddress");
				Boolean dhcp = (Boolean)res.get("Dhcp");
				Integer  arp = (Integer )res.get("Arp");
				Boolean carrier = (Boolean)res.get("Carrier");
				
				/* System.out.println(name);
				System.out.println(slaves);
				System.out.println(mode);
				System.out.println(checkMode);
				System.out.println(frequency);
				System.out.println(checkIP);
				System.out.println(descrip);
				System.out.println(ip);
				System.out.println(enabled);
				System.out.println(mtu);
				System.out.println(address);
				System.out.println(currentAddress);
				System.out.println(dhcp);
				System.out.println(arp);
				System.out.println(carrier); */
				
				ba.setName(name);
				ba.setDescription(descrip);
				ba.setState(enabled.toString());
				ba.setDhcp(dhcp.toString());
				
				String ipList = new String();
				if(ip.length==0){
					ipList = "";
				}
				else{
					for(int i=0; i<ip.length; i++){
						if (!StringUtils.equals((String)ip[i], "")) {
							ipList += ip[i] + ";";
						}
					}
					ipList = ipList.substring(0, ipList.length()-1);
				}
				ba.setAddressList(ipList);
				ba.setMtu(mtu.toString());
				ba.setMac(currentAddress);
				ba.setArpState(arp.toString());	
			}																											
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ba;
	}

	public BondConfig getBondConfigByName(String name) {
		BondConfig bc = new BondConfig();		
		try {			
			Hashtable ethTemp = new Hashtable();
			ethTemp.put("Dev", name);
			Object result = (Object)executeXml("Bonding.Get",ethTemp);
			
			if (result != null) {
				Map res = (Map)result;
				Object[] slaves = (Object[])res.get("Slaves");
				Integer  mode = (Integer )res.get("Mode");
				Integer  checkMode = (Integer )res.get("CheckMode");
				Integer  frequency = (Integer )res.get("Frequency");
				Object[] checkIP = (Object[])res.get("CheckIP");
				String descrip = (String)res.get("Description");
				Object[] ip = (Object[])res.get("IP");
				Boolean enabled = (Boolean)res.get("Enabled");
				Integer  mtu = (Integer )res.get("MTU");
				String address = (String)res.get("Address");
				String currentAddress = (String)res.get("CurrentAddress");
				Boolean dhcp = (Boolean)res.get("Dhcp");
				Integer  arp = (Integer )res.get("Arp");
				Boolean carrier = (Boolean)res.get("Carrier");
				
				/* System.out.println(name);
				System.out.println(slaves);
				System.out.println(mode);
				System.out.println(checkMode);
				System.out.println(frequency);
				System.out.println(checkIP);
				System.out.println(descrip);
				System.out.println(ip);
				System.out.println(enabled);
				System.out.println(mtu);
				System.out.println(address);
				System.out.println(currentAddress);
				System.out.println(dhcp);
				System.out.println(arp);
				System.out.println(carrier); */
				
				bc.setName(name);
				bc.setMonitor(checkMode.toString());
				
				String ipList = new String();
				if(checkIP.length==0){
					ipList = "";
				}
				else{
					for(int i=0; i<checkIP.length; i++){
						if (!StringUtils.equals((String)checkIP[i], "")) {
							ipList += checkIP[i] + ";";
						}
					}
					//ipList = ipList.substring(0, ipList.length()-1);
				}
				
				bc.setInterval(frequency.toString());
				if(StringUtils.equals(checkMode.toString(), "1")){					
					bc.setIpList(ipList);
				}				
				String interBonded = new String();
				if(slaves.length==0){
					interBonded = "";
				}
				else{
					for(int i=0; i<slaves.length; i++){
						if (!StringUtils.equals((String)slaves[i], "")) {
							interBonded += slaves[i] + ";";
						}
					}
					//interBonded = interBonded.substring(0, interBonded.length()-1);
				}
				bc.setInterBonded(interBonded);
				bc.setAlgorithm(mode.toString());
			}																										
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return bc;
	}

	/* public void delBondByName(String name) {
		Iterator<Bond> i = bondList.iterator();
		int index = 0;
		while (i.hasNext()) {
			Bond temp = i.next();
			if (StringUtils.equals(temp.getName(), name)) {
				bondList.remove(index);
				break;
			}
			index += 1;
		}
	} */
}
