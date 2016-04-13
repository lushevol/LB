package org.kylin.klb.xmlRpc;

import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.kylin.klb.entity.security.Director;
import org.kylin.klb.entity.security.Director.Remote;
import org.kylin.klb.util.Utils;
import org.kylin.modules.utils.StringUtils;

public class Client1 {
	public static void main(String[] args) throws DocumentException {
		try {
			XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();

			config.setServerURL(new URL("http://172.20.19.145:8888"));

			XmlRpcClient client = new XmlRpcClient();
			client.setConfig(config);

			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read("d:/rpc.xml");

			String[] params = { "", document.asXML() };
			Object xml = client.execute("Execute", params);

			Document doc = DocumentHelper.parseText((String) xml);

			Node d = doc
					.selectSingleNode("//Root//System//Status//Director//@value");
			String s = d.getText();
			s = Pattern.compile("['   ']+").matcher(s).replaceAll(" ");

			Director director = null;
			Director.Remote remote = null;
			List directors = new ArrayList();
			List remotes = new ArrayList();

			Map schedulings = Utils.getInstance().getSchedulings();

			String[] lines = s.split("\n");
			for (int i = lines.length - 1; i >= 0; --i) {
				if (i == 1)
					break;
				String line = StringUtils.delSpace(lines[i]);
				if (line.startsWith("->")) {
					String[] str = line.split(" ");
					remote = new Director.Remote();

					remote.setRemoteAddress(str[1]);
					remote.setRemoteForward(Utils.getInstance().getForward(
							str[2]));
					remote.setRemoteWeight(str[3]);
					remote.setRemoteActiveConn(str[4]);
					remote.setRemoteInActConn(str[5]);

					remotes.add(remote);
				}

				if (line.startsWith("FWM")) {
					director = new Director();
					director.setLocalProt("FWM");
					String sd = null;
					Iterator it = schedulings.keySet().iterator();
					while (it.hasNext()) {
						String key = (String) it.next();
						if (lines[i].indexOf(key) != -1) {
							sd = key;
							director.setLocalScheduler(key);
							break;
						}
					}
					if (StringUtils.isNotEmpty(sd)) {
						String[] str = lines[i].split(sd);
						if (str.length > 1) {
							String[] str1 = str[0].split("FWM");
							director.setLocalAddress(str1[1]);

							String[] str2 = str[1].split(" ");
							director.setLocalPersistent(str2[2]);
						} else if (str.length == 1) {
							String[] str1 = str[0].split("FWM");
							director.setLocalAddress(str1[1]);
						}
					}
					if (!remotes.isEmpty()) {
						director.setRemotes(remotes);
						if (i >= 2)
							remotes = new ArrayList();
					}
					directors.add(director);
				}
			}

		} catch (MalformedURLException e) {

		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlRpcException e) {
			e.printStackTrace();
		}
	}
}
