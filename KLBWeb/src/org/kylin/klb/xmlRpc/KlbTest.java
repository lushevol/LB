package org.kylin.klb.xmlRpc;

import java.io.PrintStream;
import java.net.URL;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class KlbTest {
	public static void main(String[] args) {
		String password = "";
		String methodName = "Execute";

		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><Root><Statistics><Services get=\"1\" from=\"1267700000\" to=\"1268332134\" interval=\"86400\" target=\"httptest\"></Services></Statistics></Root>";
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		try {
			config.setServerURL(new URL("http://172.19.80.71:8888"));

			XmlRpcClient client = new XmlRpcClient();
			client.setConfig(config);
			String[] params = { password, xml };
			Object obj = client.execute(methodName, params);
			String s = (String) obj;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
