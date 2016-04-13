package org.kylin.klb.service.network;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.xmlrpc.XmlRpcException;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.kylin.klb.entity.network.Dhcp;
import org.kylin.klb.entity.network.InterfaceInfo;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.service.KlbClient;

public class DhcpService extends KlbClient {
	private static String methodName = "Execute";
	private static String xmlName = "interfaces.xml";
	
	public List<String> getEthernets() throws XmlRpcException {
		List<String> ethernets = new ArrayList<String>();
		Document doc = getDocumentByInputStream(xmlName);
		doc.getRootElement().element("Network").element("Interfaces")
				.addElement("Ethernet").addAttribute("get", "1");
		String result = (String) executeXml(methodName, doc.asXML());
		try {
			Document document = DocumentHelper.parseText(result);
			Element eth = document.getRootElement().element("Network").element(
					"Interfaces").element("Ethernet");
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
	
	public String constructXmlForDhcpInfo() throws XmlRpcException {
		Document doc = getDocumentByInputStream(xmlName);
		Element ethernet = doc.getRootElement().element("Network").element("Interfaces")
				.addElement("Ethernet");
		List<String> ethernets = getEthernets();
		if(ethernets == null){
			return null;
		}
		Iterator<String> i = ethernets.iterator();
		while(i.hasNext()){
			String eth = i.next();
			ethernet.addElement(eth);
			ethernet.element(eth).addElement("Dhcp").addElement("Enabled").addAttribute("get", "1");
		}
		return doc.asXML();
	}
	public List<Dhcp> getDhcpList() throws XmlRpcException {
		List<Dhcp> ret = new ArrayList<Dhcp>();
		String xml = constructXmlForDhcpInfo();
		String result = (String) executeXml(methodName, xml);
		try {
			Document document = DocumentHelper.parseText(result);
			Element eth = document.getRootElement().element("Network").element(
					"Interfaces").element("Ethernet");
			if (!eth.elements().isEmpty()) {
				for (Iterator i = eth.elementIterator(); i.hasNext();) {
					Element e = (Element) i.next();
					Dhcp temp = new Dhcp();
					temp.setInter(e.getName());
					String dhcp = e.element("Dhcp").element("Enabled").attributeValue("value");
					temp.setStatus(dhcp);
					
					ret.add(temp);
					temp = new Dhcp();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ret;
	}
	public void editDhcpStatus(String inter, String status) throws XmlRpcException {
		Document doc = getDocumentByInputStream(xmlName);
		Element eth = doc.getRootElement().element("Network").element("Interfaces").addElement("Ethernet");
		eth.addElement(inter).addElement("Dhcp").addElement("Enabled").addAttribute("set", "1").addAttribute("value", status);
		String result = (String) executeXml(methodName, doc.asXML());
	}
}
