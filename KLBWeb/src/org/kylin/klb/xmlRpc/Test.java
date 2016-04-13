package org.kylin.klb.xmlRpc;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Test implements XmlDocument {
	public void createXml(String fileName) {
		Document document = DocumentHelper.createDocument();
		Element employees = document.addElement("employees");
		Element employee = employees.addElement("employee");
		Element name = employee.addElement("name");
		name.setText("ddvip");
		Element sex = employee.addElement("sex");
		sex.addAttribute("test", "test1");
		Element age = employee.addElement("age");
		age.setText("29");

	}

	public List<Element> getSubElementByXml(String fileName, String name) {
		List list = new ArrayList();
		File inputXml = new File(fileName);
		SAXReader saxReader = new SAXReader();
		try {
			Document document = saxReader.read(inputXml);
			Element root = document.getRootElement();
			Element ele = root.element(name);
			for (Iterator it = ele.elementIterator(); it.hasNext();) {
				Element subElement = (Element) it.next();
				list.add(subElement);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<String> getSubElementNameByXml(String fileName, String name) {
		List<Element> elements = getSubElementByXml(fileName, name);
		List list = new ArrayList();
		for (Element ele : elements) {
			list.add(ele.getName());
		}
		return list;
	}

	public void parserXml(String fileName) {
		File inputXml = new File(fileName);
		SAXReader saxReader = new SAXReader();
		try {
			Document document = saxReader.read(inputXml);
			Element root = document.getRootElement();
			Element ele = root.element("Users");
			List list = root.selectNodes("//User");
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				Element element = (Element) iter.next();

				Iterator iterator = element.elementIterator("title");
				while (iterator.hasNext()) {
					Element titleElement = (Element) iterator.next();
					if (!titleElement.getText().equals(
							"Java configuration with XML Schema"))
						continue;
					titleElement
							.setText("Create flexible and extensible XML schema");
				}
			}
		} catch (DocumentException e) {

		}
	}

	public static void main(String[] a) throws IOException {
		Test t = new Test();
		String path = Test.class.getResource("").getPath();
		t.parserXml(path + File.separator + "users.xml");
	}
}
