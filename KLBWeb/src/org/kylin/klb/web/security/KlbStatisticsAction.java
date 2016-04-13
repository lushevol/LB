package org.kylin.klb.web.security;

import com.opensymphony.xwork2.ActionSupport;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpSession;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Results;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.ui.RectangleInsets;
import org.kylin.klb.entity.security.Charts;
import org.kylin.klb.entity.security.Statistics;
import org.kylin.klb.service.KlbClient;
import org.kylin.modules.utils.DateUtil;
import org.kylin.modules.utils.StringUtils;
import org.kylin.modules.web.struts2.Struts2Utils;

@Namespace("/")
@ParentPackage("jfreechart-default")
@Results( { @org.apache.struts2.convention.annotation.Result(name = "success", location = "", type = "chart", params = {
		"height", "390", "width", "763", "mode", "stand", "value", "chart" }) })
public class KlbStatisticsAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private String SERVICE_DATA_KEY = "SERVICE_DATA_KEY";
	private String SERVER_DATA_KEY = "SERVER_DATA_KEY";
	private String status = "true";
	private KlbClient klbClient = new KlbClient();

	public Statistics statistics = new Statistics();
	private JFreeChart chart;
	HttpSession session = Struts2Utils.getSession();

	public LinkedHashMap<String, String> getServerDateSet(String name,
			Long startTimeToLong, Long endTimeToLong, Long time_long) {

		int xNo;
		/* Element root = DocumentHelper.createElement("Root");
		root.addElement("Statistics").addElement("Servers").addAttribute("get", "1")
		.addAttribute("from", Long.toString(startTimeToLong)).addAttribute("to", Long.toString(endTimeToLong))
		.addAttribute("interval", Long.toString(time_long)).addAttribute("target", name);
				
		String statiscResult = (String) klbClient.executeXml("Execute", root.asXML());	*/	
		
		try {
			Hashtable statisticTemp = new Hashtable();
			statisticTemp.put("IP", name);
			statisticTemp.put("From", NumberUtils.toInt(startTimeToLong.toString()));
			statisticTemp.put("To", NumberUtils.toInt(endTimeToLong.toString()));
			statisticTemp.put("Interval", NumberUtils.toInt(time_long.toString()));
			String statisticResult = (String)klbClient.executeXml("Statistic.Server",statisticTemp);
						
			/* Document document = DocumentHelper.parseText(statiscResult);
			Element Servers = document.getRootElement().element("Statistics").element("Servers"); */
			LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
			if( statisticResult==null || statisticResult.isEmpty() ){
				xNo = (int)((endTimeToLong-startTimeToLong)/time_long);
				xNo += 1;
				for (int i = 0; i < xNo; i++) {
					int ii = 0;
					data.put(String.valueOf(i), String.valueOf(ii));
				}
				return data;
			}
			String value = statisticResult;
			String[] values = value.split(";");
			xNo = values.length;
			for (int i = 0; i < xNo; i++) {
				int ii = Integer.parseInt(values[i]);
				data.put(String.valueOf(i), String.valueOf(ii));
			}
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public LinkedHashMap<String, String> getServiceDateSet(String name,
			Long startTimeToLong, Long endTimeToLong, Long time_long) {

		int xNo;
		/* Element root = DocumentHelper.createElement("Root");
		root.addElement("Statistics").addElement("Services").addAttribute("get", "1")
		.addAttribute("from", Long.toString(startTimeToLong)).addAttribute("to", Long.toString(endTimeToLong))
		.addAttribute("interval", Long.toString(time_long)).addAttribute("target", name);		
		String statiscResult = (String) klbClient.executeXml("Execute", root.asXML()); */
		
		try {
			Hashtable nameToIdTemp = new Hashtable();
			nameToIdTemp.put("Name", name);
			Object nameToIdResult = (Object)klbClient.executeXml("VirtualService.NameToID",nameToIdTemp);
			Integer id = null;
			if (nameToIdResult != null) {
				Map res = (Map)nameToIdResult;
				id = (Integer)res.get("ID");
			}
			
			Hashtable statisticTemp = new Hashtable();
			statisticTemp.put("ID", id);
			statisticTemp.put("From", NumberUtils.toInt(startTimeToLong.toString()));
			statisticTemp.put("To", NumberUtils.toInt(endTimeToLong.toString()));
			statisticTemp.put("Interval", NumberUtils.toInt(time_long.toString()));
			String statisticResult = (String)klbClient.executeXml("Statistic.Service",statisticTemp);
						
			/* Document document = DocumentHelper.parseText(statiscResult);
			Element Servers = document.getRootElement().element("Statistics").element("Services"); */
			
			LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
			if( statisticResult==null || statisticResult.isEmpty() ){
				xNo = (int)((endTimeToLong-startTimeToLong)/time_long);
				xNo += 1;
				for (int i = 0; i < xNo; i++) {
					int ii = 0;
					data.put(String.valueOf(i), String.valueOf(ii));
				}
				return data;
			}
			String value = statisticResult;
			String[] values = value.split(";");
			xNo = values.length;
			for (int i = 0; i < xNo; i++) {
				int ii = Integer.parseInt(values[i]);
				data.put(String.valueOf(i), String.valueOf(ii));
			}
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String service() throws Exception {

		String server = this.statistics.getServer();
		String startDate = this.statistics.getStartDate();
		String endDate = this.statistics.getEndDate();
		String startTime = this.statistics.getStartTime();
		String endTime = this.statistics.getEndTime();
		
		startTime = StringUtils.defaultIfEmpty(startTime, "00:00:00");
		endTime = StringUtils.defaultIfEmpty(endTime, "00:00:00");

		String time = StringUtils.defaultIfEmpty(this.statistics.getTime(), "3600");

		Date date = new Date();

		date.setHours(date.getHours() - 48);
		startDate = StringUtils.defaultIfEmpty(startDate, DateUtil.formatDate(date, "yyyy-MM-dd"));
		endDate = StringUtils.defaultIfEmpty(endDate, DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
		
		String startDateTime = startDate + " " + startTime;
		String endDateTime = endDate + " " + endTime;
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		df.setTimeZone(TimeZone.getTimeZone("GMT-0"));
		
		Long startTimeToLong = df.parse(startDateTime).getTime() / 1000L;
		Long endTimeToLong = df.parse(endDateTime).getTime() / 1000L;
		
		//startTimeToLong = startTimeToLong - Integer.parseInt(zone) * 60 * 60;
		//endTimeToLong = endTimeToLong - Integer.parseInt(zone) * 60 * 60;
		
		Charts charts = new Charts();
		charts.setYAxisLabel("转发次数");
		String[] xKeys = new String[0];
		String[] yKeys = { "间隔周期" };
		double[][] yKeysVale = { new double[0] };

		if (StringUtils.isNotEmpty(new String[] { server, startDate, endDate, time })) {
			charts.setTitle(server + "报文转发次数");
			charts.setXAxisLabel("间隔周期(" + DateUtil.interval(time) + ")");
			yKeys[0] = ("次/" + DateUtil.interval(time));
			LinkedHashMap<String, String> map = getServiceDateSet(server, startTimeToLong, endTimeToLong, Long.decode(time));
			if(map == null){
				status = "false";
			}
			LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
			data.put("服务名称", server);
			data.put("间隔周期", DateUtil.interval(time));
			data.put("开始日期", startDate);
			data.put("结束日期", endDate);

			double[] d = new double[map.size()];
			int count = 0;
			xKeys = new String[map.size()];
			Iterator it = map.keySet().iterator();
			while (it.hasNext()) {
				String s = (String) it.next();
				d[count] = Double.parseDouble((String) map.get(s));
				xKeys[count] = s;
				++count;

				data.put(s + DateUtil.interval(time), (String) map.get(s));
			}

			LinkedHashMap datas = (LinkedHashMap) this.session.getAttribute(this.SERVICE_DATA_KEY);
			if (datas != null) {
				datas.put(server, data);
			} else {
				datas = new LinkedHashMap();
				datas.put(server, data);
			}
			this.session.setAttribute(this.SERVICE_DATA_KEY, datas);
			yKeysVale = new double[][] { d };
		}

		CategoryDataset dataset = DatasetUtilities.createCategoryDataset(yKeys, xKeys, yKeysVale);
		chart(charts, dataset);

		return "success";
	}

	public String server() throws Exception {
		String server = this.statistics.getServer();
		String startDate = this.statistics.getStartDate();
		String endDate = this.statistics.getEndDate();
		String startTime = this.statistics.getStartTime();
		String endTime = this.statistics.getEndTime();
		
		startTime = StringUtils.defaultIfEmpty(startTime, "00:00:00");
		endTime = StringUtils.defaultIfEmpty(endTime, "00:00:00");

		String time = StringUtils.defaultIfEmpty(this.statistics.getTime(), "3600");

		Date date = new Date();
		date.setHours(date.getHours() - 48);
		
		startDate = StringUtils.defaultIfEmpty(startDate, DateUtil.formatDate(date, "yyyy-MM-dd"));
		endDate = StringUtils.defaultIfEmpty(endDate, DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
		
		String startDateTime = startDate + " " + startTime;
		String endDateTime = endDate + " " + endTime;
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		df.setTimeZone(TimeZone.getTimeZone("GMT-0"));

		Long startTimeToLong = df.parse(startDateTime).getTime() / 1000L;
		Long endTimeToLong = df.parse(endDateTime).getTime() / 1000L;

		//startTimeToLong = startTimeToLong - Integer.parseInt(zone) * 60 * 60;
		//endTimeToLong = endTimeToLong - Integer.parseInt(zone) * 60 * 60;

		Charts charts = new Charts();
		charts.setYAxisLabel("转发次数");
		String[] xKeys = new String[0];
		String[] yKeys = { "间隔周期" };
		double[][] yKeysVale = { new double[0] };

		if (StringUtils.isNotEmpty(new String[] { server, startDate, endDate, time })) {
			charts.setTitle(server + "报文转发次数");
			charts.setXAxisLabel("间隔周期(" + DateUtil.interval(time) + ")");
			yKeys[0] = ("次/" + DateUtil.interval(time));
			LinkedHashMap map = getServerDateSet(server, startTimeToLong, endTimeToLong, Long.decode(time));
			if(map==null){
				return "success";
			}
			LinkedHashMap data = new LinkedHashMap();

			data.put("服务名称", server);
			data.put("间隔周期", DateUtil.interval(time));
			data.put("开始日期", startDate);
			data.put("结束日期", endDate);

			int x = map.size();
			double[] d = new double[x];
			int count = 0;
			xKeys = new String[x];
			Iterator it = map.keySet().iterator();
			while (it.hasNext()) {
				String s = (String) it.next();
				d[count] = Double.parseDouble((String) map.get(s));
				xKeys[count] = s;
				++count;

				data.put(s + DateUtil.interval(time), (String) map.get(s));
			}

			LinkedHashMap datas = (LinkedHashMap) this.session.getAttribute(this.SERVER_DATA_KEY);
			if (datas != null) {
				datas.put(server, data);
			} else {
				datas = new LinkedHashMap();
				datas.put(server, data);
			}
			this.session.setAttribute(this.SERVER_DATA_KEY, datas);
			yKeysVale = new double[][] { d };
		}

		CategoryDataset dataset = DatasetUtilities.createCategoryDataset(yKeys, xKeys, yKeysVale);
		chart(charts, dataset);
		return "success";
	}

	private void chart(Charts charts, CategoryDataset dataset) {
		//System.out.println("before execute chart");
		this.chart = ChartFactory.createLineChart(charts.getTitle(), charts.getXAxisLabel(), charts.getYAxisLabel(), dataset,
				PlotOrientation.VERTICAL, true, true, false);
		//System.out.println("after execute chart");
		this.chart.setBackgroundPaint(Color.WHITE);
		CategoryPlot categoryplot = (CategoryPlot) this.chart.getPlot();

		categoryplot.setDomainGridlinesVisible(true);

		categoryplot.setRangeGridlinesVisible(true);
		categoryplot.setRangeGridlinePaint(Color.lightGray);
		categoryplot.setDomainGridlinePaint(Color.lightGray);
		categoryplot.setBackgroundPaint(Color.WHITE);

		categoryplot.setAxisOffset(new RectangleInsets(0.0D, 0.0D, 0.0D, 0.0D));

		LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer) categoryplot.getRenderer();
		lineandshaperenderer.setBaseShapesVisible(true);

		lineandshaperenderer.setBaseLinesVisible(true);

		lineandshaperenderer.setBaseItemURLGenerator(new StandardCategoryURLGenerator("", "series", "hits"));

		lineandshaperenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		lineandshaperenderer.setBaseItemLabelsVisible(true);

		CategoryAxis domainAxis = categoryplot.getDomainAxis();

		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		numberaxis.setAutoRangeIncludesZero(false);
	}

	public JFreeChart getChart() {
		return this.chart;
	}

	public Statistics getStatistics() {
		return this.statistics;
	}

	public void setStatistics(Statistics statistics) {
		this.statistics = statistics;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
