package org.kylin.klb.xmlRpc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XmlDom4J {
	public void createXMLFile() {
		Document document = DocumentHelper.createDocument();

		Element catalogElement = document.addElement("catalog");

		catalogElement.addComment("An XML Catalog");

		catalogElement.addProcessingInstruction("target", "text");

		Element journal = catalogElement.addElement("journal");

		journal.addAttribute("title", "XML Zone");
		journal.addAttribute("publisher", "IBM Devoloperment");

		Element articleElement = journal.addElement("article");
		articleElement.addAttribute("level", "Intermediate");
		articleElement.addAttribute("date", "December-2008");

		Element titleElement = articleElement.addElement("title");
		titleElement.setText("又是下雨天");

		Element authorElement = articleElement.addElement("author");
		Element firstNameElement = authorElement.addElement("firstname");
		firstNameElement.setText("Marcello");
		Element lastNameElement = authorElement.addElement("lastname");
		lastNameElement.setText("Vitaletti");
		try {
			OutputFormat format = new OutputFormat();
			format.setEncoding("gb2312");
			XMLWriter output = new XMLWriter(new FileWriter(new File(
					"catalog.xml")), format);
			output.write(document);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Document modifyXMLNodeAttributeByName(File inputXml, String XPath,
			String oldAttributeValue, String attributeValue) {
		if (XPath.indexOf("@") < 0) {

			return null;
		}
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(inputXml);
			List list = document.selectNodes(XPath);
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				Attribute attribute = (Attribute) iter.next();
				if (attribute.getValue().equals(oldAttributeValue))
					attribute.setValue(attributeValue);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;
	}

	public Document modifyXMLNodeAttributeByName(File inputXml, String XPath,
			String attributeValue) {
		if (XPath.indexOf("@") < 0) {

			return null;
		}
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(inputXml);
			List list = document.selectNodes(XPath);
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				Attribute attribute = (Attribute) iter.next();

				attribute.setValue(attributeValue);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;
	}

	public String[] getNodeAttributeValue(File inputxml, String XPath) {
		String nodeAttri = "";
		if (XPath.indexOf("@") < 0) {
			return null;
		}
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(inputxml);
			List list = document.selectNodes(XPath);
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Attribute attri = (Attribute) it.next();
				nodeAttri = nodeAttri + attri.getValue() + ",";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (nodeAttri.length() > 0) {
			nodeAttri = nodeAttri.substring(0, nodeAttri.length() - 1);
		}
		return nodeAttri.split(",");
	}

	public Document modifyXMLNodeTextByName(File inputXml, String XPath,
			String newText) {
		if (XPath.indexOf("@") >= 0) {

			return null;
		}
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(inputXml);
			List list = document.selectNodes(XPath);
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				Element elementText = (Element) iter.next();
				elementText.setText(newText);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;
	}

	public Document modifyXMLNodeTextByName(File inputXml, String XPath,
			String oldText, String newText) {
		if (XPath.indexOf("@") >= 0) {

			return null;
		}
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(inputXml);
			List list = document.selectNodes(XPath);
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				Element elementText = (Element) iter.next();
				if (elementText.getText().equals(oldText))
					elementText.setText(newText);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;
	}

	public String[] getNodeTextValue(File inputxml, String XPath) {
		String nodeTextValue = "";
		if (XPath.indexOf("@") >= 0) {
			return null;
		}
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(inputxml);
			List list = document.selectNodes(XPath);
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Element text = (Element) it.next();
				nodeTextValue = nodeTextValue + text.getText() + ",";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (nodeTextValue.length() > 0) {
			nodeTextValue = nodeTextValue.substring(0,
					nodeTextValue.length() - 1);
		}
		return nodeTextValue.split(",");
	}

	public void saveXmlFile(Document document, String filePath, String code) {
		if (document == null) {
			return;
		}
		try {
			OutputFormat format = new OutputFormat();
			format.setEncoding(code);
			XMLWriter output = new XMLWriter(
					new FileWriter(new File(filePath)), format);
			output.write(document);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		XmlDom4J dom4jParser = new XmlDom4J();

		String path = Test.class.getResource("").getPath();
		File file = new File(path + File.separator + "users.xml");

		String xpath = "//User[@id = '1']";
		Document d = dom4jParser.modifyXMLNodeTextByName(file, xpath, "aaaaa");

	}
}
