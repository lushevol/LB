package org.kylin.klb.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.kylin.modules.utils.StringUtils;
import org.kylin.modules.web.struts2.Struts2Utils;

public class KlbClient {
	XmlRpcClient client = null;

	public static final String XML_PATH = File.separator + "WEB-INF"
			+ File.separator + "xml" + File.separator;
	public static final String XML_REALPATH = Struts2Utils.getRealPath("");
	private static final String XML_TEMP = "_temp";

	private static String hashPassword = null;

	public KlbClient() {

		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		try {
			config.setServerURL(new URL(Struts2Utils
					.getString("klb.client.url")));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		this.client = new XmlRpcClient();
		this.client.setConfig(config);
	}

	public boolean checkPassword(String password) throws XmlRpcException {

		String cacheHashPassword = hashPassword;
		hashPassword = encrypt(password);
		boolean flag = checkPassword();
		hashPassword = cacheHashPassword;
		return flag;

	}

	private boolean checkPassword() throws XmlRpcException {

		/*
		 * String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<Root></Root>";
		 */

		executeXml("Server.Check");
		if (passwordSuccess) {
			return true;
		} else {
			return false;
		}
	}

	public boolean updatePassword(String newPassword) throws XmlRpcException {

		if (StringUtils.equals(newPassword, "")) {
			Boolean temp = (Boolean) executeXml("Password.Set", newPassword);
			if (temp) {
				hashPassword = "";
				return true;
			}
		} else {
			Boolean temp = (Boolean) executeXml("Password.Set",
					encrypt(newPassword));
			if (temp) {
				hashPassword = encrypt(newPassword);
				return true;
			}
		}
		return false;
	}

	public String encrypt(String password) {

		if (password == null || password.isEmpty())
			return "";
		int passwordLength = password.length();
		char[] passwordCharArray = new char[passwordLength];
		passwordCharArray = password.toCharArray();

		byte[] passwordByteArray = new byte[passwordLength];

		for (int i = 0; i < passwordLength; i++) {
			passwordByteArray[i] = (byte) passwordCharArray[i];
		}

		long value = 0;
		for (int i = 0; i < passwordLength; ++i) {
			value = value * 31 + passwordByteArray[i];
		}

		int bufSize = Long.SIZE / 8 * 2;

		byte[] buf = new byte[bufSize];
		for (short i = 0; i < bufSize; ++i) {
			buf[i] = (byte) (((value & 0xF) << 3) | 0x80);
			value >>= 4;
			// System.out.print(Integer.toHexString((buf[i])) + ",");
		}
		// 
		try {
			// BASE64Encoder encoder = new BASE64Encoder();
			// String result = encoder.encode(buf);
			String result = new String(buf, "8859_1");
			// 
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/*
	 * public void SetPassword(String password) { if (password != null &&
	 * password != "") { hashPassword = Encrypt(password); } else hashPassword =
	 * null; }
	 */

	/*
	 * public String getTime() { String xml = "<?xml version=\"1.0\"
	 * encoding=\"UTF-8\"?>" + "<Root>" + "<System>" + "<Time get=\"1\" />" + "</System></Root>";
	 * String time = (String) executeXml("Execute", xml); return time; }
	 */

	private InputStream getInputStreamByPath(String xmlName) {
		InputStream is = Struts2Utils.getSession().getServletContext()
				.getResourceAsStream(XML_PATH + xmlName);
		return is;
	}

	private boolean connectSuccess = true;
	private boolean passwordSuccess = true;
	private boolean updatePasswordSuccess = false;

	public Object executeXml(String methodName, String xml)
			throws XmlRpcException {
		//
		connectSuccess = true;
		passwordSuccess = true;
		updatePasswordSuccess = false;

		/*
		 * try { byte[] resultByte = hashPassword.getBytes("8859_1"); int
		 * resultByteLength = resultByte.length; for (int i = 0; i <
		 * resultByteLength; i++) { //
		 * System.out.print(Integer.toHexString(resultByte[i]) + ","); } } catch
		 * (Exception e) { e.printStackTrace(); }
		 */

		String[] params = { hashPassword, xml };
		Object returnValue = null;
		try {
			
			returnValue = this.client.execute(methodName, params);
			//
		} catch (XmlRpcException e) {
			e.printStackTrace();

			if (StringUtils.equals(e.getMessage(), "Invalid password.")) {
				passwordSuccess = false;
			} else {
				if (StringUtils
						.equals(e.getMessage(),
								"Failed to parse server's response: Invalid byte 1 of 1-byte UTF-8 sequence.")) {
					updatePasswordSuccess = true;
				} else if (StringUtils
						.equals(e.getMessage(),
								"Failed to read server's response: Connection refused: connect")) {
					connectSuccess = false;
					// SecurityUtils.removeSecurityUser();
					SecurityUtils.setConnectStatus();
				} else {
					throw e;
				}
			}
		}
		return returnValue;
	}

	public Object executeXml(String methodName, Object parameter)
			throws XmlRpcException {
		//
		connectSuccess = true;
		passwordSuccess = true;
		updatePasswordSuccess = false;

		/*
		 * try { byte[] resultByte = hashPassword.getBytes("8859_1"); int
		 * resultByteLength = resultByte.length; for (int i = 0; i <
		 * resultByteLength; i++) { //
		 * System.out.print(Integer.toHexString(resultByte[i]) + ","); } } catch
		 * (Exception e) { e.printStackTrace(); }
		 */

		Vector params = new Vector();
		params.add(hashPassword);
		params.add(parameter);
		Object returnValue = null;
		try {
			returnValue = this.client.execute(methodName, params);
			//
		} catch (XmlRpcException e) {
			e.printStackTrace();

			if (StringUtils.equals(e.getMessage(), "Invalid password.")) {
				passwordSuccess = false;
			} else {
				if (StringUtils
						.equals(e.getMessage(),
								"Failed to parse server's response: Invalid byte 1 of 1-byte UTF-8 sequence.")) {
					updatePasswordSuccess = true;
				} else if (StringUtils
						.equals(e.getMessage(),
								"Failed to read server's response: Connection refused: connect")) {
					connectSuccess = false;
					// SecurityUtils.removeSecurityUser();
					SecurityUtils.setConnectStatus();
				} else {
					throw e;
				}
			}
		}
		// 
		return returnValue;
	}
//methodName=Licence.Machine
	public Object executeXml(String methodName) throws XmlRpcException {
		//
		connectSuccess = true;
		passwordSuccess = true;
		updatePasswordSuccess = false;

		/*
		 * try { byte[] resultByte = hashPassword.getBytes("8859_1"); int
		 * resultByteLength = resultByte.length; for (int i = 0; i <
		 * resultByteLength; i++) { //
		 * System.out.print(Integer.toHexString(resultByte[i]) + ","); } } catch
		 * (Exception e) { e.printStackTrace(); }
		 */
		// 
		Object[] params = { hashPassword };
		Object returnValue = null;
		try {
			returnValue = this.client.execute(methodName, params);
			//
		} catch (XmlRpcException e) {
			e.printStackTrace();
			System.out.println(e.code);
			if (e.code == 500) {
				passwordSuccess = false;
				return null;
			}
			if (StringUtils.equals(e.getMessage(), "Invalid password.")) {
				passwordSuccess = false;
				return null;
			}
			if (StringUtils
					.equals(e.getMessage(),
							"Failed to parse server's response: Invalid byte 1 of 1-byte UTF-8 sequence.")) {
				updatePasswordSuccess = true;
				return null;
			}
			if (StringUtils
					.equals(e.getMessage(), "Cannot unpack the package.")) {
				return "failed";
			} else if (StringUtils
					.equals(e.getMessage(),
							"Failed to read server's response: Connection refused: connect")
					|| StringUtils
							.equals(e.getMessage(),
									"Failed to read server's response: Connection timed out: connect")) {
				connectSuccess = false;
				//
				// SecurityUtils.removeSecurityUser();
				SecurityUtils.setConnectStatus();
			} else {
				throw e;
			}
		}
		// 
		return returnValue;
	}

	public Object executePath(String methodName, String path)
			throws XmlRpcException {
		return executeXml(methodName, getDocumentByPath(path).asXML());
	}

	public Object executeInputStreamByXml(String methodName, Document doc)
			throws XmlRpcException {
		return executeXml(methodName, doc.asXML());
	}

	public Document getDocumentByPath(String xmlPath) {
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(xmlPath);
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return document;
	}

	private Document getDocumentByInputStream(InputStream is) {
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(is);
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return document;
	}

	public Document getDocumentByInputStream(File file) {
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(file);
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return document;
	}

	public Document getDocumentByInputStream(String xmlName) {
		InputStream is = getInputStreamByPath(xmlName);
		return getDocumentByInputStream(is);
	}

	public Document getDocumentByReader(String xmlName) {
		Document doc = null;
		try {
			SAXReader saxReader = new SAXReader();
			doc = saxReader.read(XML_REALPATH + XML_PATH + xmlName);
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return doc;
	}

	public List<Element> getSubElementByXml(String xml, String name) {
		List list = new ArrayList();
		try {
			Document document = DocumentHelper.parseText(xml);
			Element root = document.getRootElement();
			Element ele = root.element(name);
			for (Iterator it = ele.elementIterator(); it.hasNext();) {
				Element subElement = (Element) it.next();
				list.add(subElement);
			}
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return list;
	}

	public List<String> getSubElementNameByXml(String xml, String name) {
		List list = new ArrayList();

		try {
			List<Element> elements = getSubElementByXml(xml, name);

			for (Element ele : elements) {
				list.add(ele.getName());
			}
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return list;
	}

	public List<Element> getSubElementByXml1(String xml, String name,
			String name1, String name2) {
		List list = new ArrayList();
		try {
			Document document = DocumentHelper.parseText(xml);
			Element root = document.getRootElement();
			Element ele = root.element(name).element(name1).element(name2);
			for (Iterator it = ele.elementIterator(); it.hasNext();) {
				Element subElement = (Element) it.next();
				list.add(subElement);
			}
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return list;
	}

	public List<String> getSubElementNameByXml1(String xml, String name,
			String name1, String name2) {
		List list = new ArrayList();
		try {
			List<Element> elements = getSubElementByXml1(xml, name, name1,
					name2);

			for (Element ele : elements) {
				list.add(ele.attributeValue("value"));
			}
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return list;
	}

	public boolean saveXml(String filename, Document document)
			throws IOException {

		boolean c = false;
		try {
			if (document != null) {
				String realPath = Struts2Utils.getRealPath("");
				// 
				String path = realPath + XML_PATH + filename;
				// 
				Writer out = new OutputStreamWriter(new FileOutputStream(path
						+ "_temp"), "utf-8");
				OutputFormat format = OutputFormat.createPrettyPrint();
				XMLWriter writer = new XMLWriter(out, format);
				writer.write(document);
				// 
				out.close();
				c = renameXml(path);
				// c = true;
			}
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return c;
	}

	public boolean renameXml(String filename) {
		boolean c = false;
		try {
			File file = new File(filename + "_temp");
			File newfile = new File(filename);

			if (newfile.exists()) {
				newfile.delete();
			}
			if (file.exists())
				c = file.renameTo(newfile);
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return c;
	}

	public String formatXml(Document doc) throws IOException {
		if (doc == null)
			return null;

		StringWriter stringOut = null;
		XMLWriter xmlOut = null;
		try {
			OutputFormat formater = OutputFormat.createPrettyPrint();
			formater.setEncoding("utf-8");

			stringOut = new StringWriter();
			xmlOut = new XMLWriter(stringOut, formater);
			xmlOut.write(doc);
			return stringOut.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			stringOut.close();
			xmlOut.close();
		}
		return null;
	}

	public boolean isConnectSuccess() {
		return connectSuccess;
	}

	public void setConnectSuccess(boolean connectSuccess) {
		this.connectSuccess = connectSuccess;
	}

	public boolean isPasswordSuccess() {
		return passwordSuccess;
	}

	public void setPasswordSuccess(boolean passwordSuccess) {
		this.passwordSuccess = passwordSuccess;
	}

	public static String getHashPassword() {
		return hashPassword;
	}

	public static void setHashPassword(String hashPassword) {
		KlbClient.hashPassword = hashPassword;
	}

	public boolean isUpdatePasswordSuccess() {
		return updatePasswordSuccess;
	}

	public void setUpdatePasswordSuccess(boolean updatePasswordSuccess) {
		this.updatePasswordSuccess = updatePasswordSuccess;
	}

}
