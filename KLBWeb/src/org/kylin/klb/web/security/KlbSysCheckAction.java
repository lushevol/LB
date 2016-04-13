package org.kylin.klb.web.security;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.convention.annotation.Namespace;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.kylin.klb.entity.security.CheckResult;
import org.kylin.klb.entity.security.SystemHard;
import org.kylin.klb.service.KlbClient;
import org.kylin.klb.util.Utils;
import org.kylin.klb.web.CrudActionSupport;
import org.kylin.modules.utils.StringUtils;
import org.kylin.modules.web.struts2.Struts2Utils;
import org.springframework.beans.factory.annotation.Autowired;

@Namespace("/")
public class KlbSysCheckAction extends CrudActionSupport {
	private static final long serialVersionUID = 1L;

	// private String test = "123";
	private List<CheckResult> checkResults = new ArrayList<CheckResult>();
	// private String[] checkResults;

	@Autowired
	private KlbClient klbClient = new KlbClient();
	private static String methodName = "Execute";

	public SystemHard getModel() {
		return null;
	}

	public void prepareExecute() throws Exception {
		prepareModel();
	}

	protected void prepareModel() throws Exception {
	}

	public String execute() throws Exception {
		return "success";
	}

	public String doCheck() throws Exception {
		String xmlName = "sysCheck.xml";
		Document doc = klbClient.getDocumentByInputStream(xmlName);
		Element ddos = doc.getRootElement().element("System").element("Health");
		ddos.addAttribute("get", "1");
		String checkResult = (String) klbClient.executeXml(methodName, doc.asXML());

		Document document = DocumentHelper.parseText(checkResult);
		Element Servers = document.getRootElement().element("System").element("Health");
		String value = Servers.attributeValue("value");

		StringReader sr = new StringReader(value);
		// StringReader sr = new StringReader("1;\n2;\n3;\n");
		BufferedReader br = new BufferedReader(sr);
		String line = br.readLine();

		if (line == null) {
			Struts2Utils.getRequest().setAttribute("error", "true");
		} else {
			while (line != null) {
				//
				String[] totalError = line.split(";");
				//
				//
				Map results = Utils.getInstance().getCheckResults();
				Iterator it = results.keySet().iterator();
				while (it.hasNext()) {
					String key = (String) it.next();
					//
					if (StringUtils.equals(key, totalError[0])) {
						//
						String tempResult = new String();
						if (totalError.length == 2) {
							tempResult = totalError[1] + (String) results.get(key);
						} else {
							tempResult = (String) results.get(key);
						}
						//

						CheckResult checkResultRedAndBlack = new CheckResult();
						String[] ResultRedAndBlack = tempResult.split(";");

						checkResultRedAndBlack.setRedString(ResultRedAndBlack[0]);
						checkResultRedAndBlack.setBlackString(ResultRedAndBlack[1]);

						this.checkResults.add(checkResultRedAndBlack);
						break;
					}
				}
				line = br.readLine();
			}

			//
			/*
			 * Iterator it = checkResults.iterator(); while (it.hasNext()) {
			 * String key = (String) it.next();
			 * 
			 *  }
			 */
			Struts2Utils.getRequest().setAttribute("error", "false");
		}
		return "success";
	}

	public String list() throws Exception {
		return "success";
	}

	public String input() throws Exception {
		return null;
	}

	public String save() throws Exception {
		return null;
	}

	public String delete() throws Exception {
		return null;
	}

	public List<CheckResult> getCheckResults() {
		return checkResults;
	}

	public void setCheckResults(List<CheckResult> checkResults) {
		this.checkResults = checkResults;
	}

	/*
	 * public String getTest() { return test; }
	 * 
	 * public void setTest(String test) { this.test = test; }
	 */

}
