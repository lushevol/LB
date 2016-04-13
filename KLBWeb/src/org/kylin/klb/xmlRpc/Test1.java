package org.kylin.klb.xmlRpc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class Test1 {
	private Document document;
	private List nodeList;
	private Node root;

	public Test1(File inputXml) {
		try {
			SAXReader saxReader = new SAXReader();
			this.document = saxReader.read(inputXml);
			this.root = this.document.selectSingleNode("//Root");
		} catch (DocumentException e) {
			e.getMessage();
		} catch (Exception e) {
			e.getMessage();
		}
	}

	private void getDataOfPid(String pid) throws IOException {
		this.nodeList = this.root.selectNodes("//User[id='1']");
		if ((this.nodeList != null) && (this.nodeList.size() > 0)) {
			for (Iterator it = this.nodeList.iterator(); it.hasNext();) {
				Element e = (Element) it.next();
				String user = e.elementText("LoginName");
				Element dd = e.element("LoginName");
				dd.setText("ddddd");
				String UserName = e.elementText("UserName");
				String PassWord = e.elementText("PassWord");

			}
		}
		store("testd.xml");
	}

	public void store(String filename) throws IOException {
		Writer out = new OutputStreamWriter(new FileOutputStream(filename),
				"utf-8");
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter writer = new XMLWriter(out, format);
		writer.write(this.document);
		out.close();
	}

	public static void main(String[] a) throws IOException {
		String path = Test.class.getResource("").getPath();
		File file = new File(path + File.separator + "users.xml");
		Test1 test = new Test1(file);
		test.getDataOfPid("1");
	}
}
