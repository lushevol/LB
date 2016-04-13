package org.kylin.klb.xmlRpc;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.kylin.klb.entity.security.Director;

public class Client {
	public static void main(String[] args) throws DocumentException {
		try {
			XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();

			config.setServerURL(new URL("http://172.20.19.145:8888"));

			XmlRpcClient client = new XmlRpcClient();
			client.setConfig(config);

			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read("d:/tt.xml");

			Director director = new Director();
			Director.Remote remote = null;
			List remotes = new ArrayList();

			Node d = document
					.selectSingleNode("//Root//System//Status//Director//@value");
			String s = d.getText();
			s = Pattern.compile("['   ']+").matcher(s).replaceAll(" ");

			String[] lines = s.split("\n");
			for (String line : lines) {

			}

			for (int i = 0; i < lines.length; ++i) {
				String[] local = lines[i].split(" ");
				if (i == 2) {
					if (("FWM".startsWith(local[0])) && (local.length >= 5)) {
						director.setLocalFlags(local[3] + local[4]);
					}
					director.setLocalProt(local[0]);
					director.setLocalAddress(local[1]);
					director.setLocalScheduler(local[2]);
				}
				if (i >= 3) {
					remote = new Director.Remote();
					remote.setRemoteAddress(local[2]);
					remote.setRemoteForward(local[3]);
					remote.setRemoteWeight(local[4]);
					remote.setRemoteActiveConn(local[5]);
					remote.setRemoteInActConn(local[6]);

					remotes.add(remote);
				}
			}
			director.setRemotes(remotes);
		} catch (MalformedURLException e) {

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
