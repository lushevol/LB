package org.kylin.klb.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.commons.lang.math.NumberUtils;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.util.Utils;
import org.kylin.modules.utils.StringUtils;

public class TimeService extends KlbClient {
	//private static String methodName = "Execute";
	private String time;
	private String zone;
	
	public Map<String, String> getZoneMap(){
		Map<String, String> zoneMap = new HashMap<String, String>();

		zoneMap.put("-1", "东一区");
		zoneMap.put("-2", "东二区");
		zoneMap.put("-3", "东三区");
		zoneMap.put("-4", "东四区");
		zoneMap.put("-5", "东五区");
		zoneMap.put("-6", "东六区");
		zoneMap.put("-7", "东七区");
		zoneMap.put("-8", "东八区");
		zoneMap.put("-9", "东九区");
		zoneMap.put("-10", "东十区");
		zoneMap.put("-11", "东十一区");
		zoneMap.put("-12", "东十二区");
		zoneMap.put("0", "零时区");
		zoneMap.put("1", "西一区");
		zoneMap.put("2", "西二区");
		zoneMap.put("3", "西三区");
		zoneMap.put("4", "西四区");
		zoneMap.put("5", "西五区");
		zoneMap.put("6", "西六区");
		zoneMap.put("7", "西七区");
		zoneMap.put("8", "西八区");
		zoneMap.put("9", "西九区");
		zoneMap.put("10", "西十区");
		zoneMap.put("11", "西十一区");
		
		return zoneMap;
	}
	
	public List<Display> getZoneList(){
		List<Display> zoneList = new ArrayList<Display>();

		Display zone1 = new Display("-1", "东一区");
		Display zone2 = new Display("-2", "东二区");
		Display zone3 = new Display("-3", "东三区");
		Display zone4 = new Display("-4", "东四区");
		Display zone5 = new Display("-5", "东五区");
		Display zone6 = new Display("-6", "东六区");
		Display zone7 = new Display("-7", "东七区");
		Display zone8 = new Display("-8", "东八区");
		Display zone9 = new Display("-9", "东九区");
		Display zone10 = new Display("-10", "东十区");
		Display zone11 = new Display("-11", "东十一区");
		Display zone12 = new Display("-12", "东十二区");
		Display zone13 = new Display("-13", "东十三区");
		Display zone14 = new Display("-14", "东十四区");
		
		Display zone15 = new Display("0", "零时区");
		
		Display zone16 = new Display("1", "西一区");
		Display zone17 = new Display("2", "西二区");
		Display zone18 = new Display("3", "西三区");
		Display zone19 = new Display("4", "西四区");
		Display zone20 = new Display("5", "西五区");
		Display zone21 = new Display("6", "西六区");
		Display zone22 = new Display("7", "西七区");
		Display zone23 = new Display("8", "西八区");
		Display zone24 = new Display("9", "西九区");
		Display zone25 = new Display("10", "西十区");
		Display zone26 = new Display("11", "西十一区");
		Display zone27 = new Display("12", "西十二区");
		
		zoneList.add(zone1);
		zoneList.add(zone2);
		zoneList.add(zone3);
		zoneList.add(zone4);
		zoneList.add(zone5);
		zoneList.add(zone6);
		zoneList.add(zone7);
		zoneList.add(zone8);
		zoneList.add(zone9);
		zoneList.add(zone10);
		zoneList.add(zone11);
		zoneList.add(zone12);
		zoneList.add(zone13);
		zoneList.add(zone14);
		zoneList.add(zone15);
		zoneList.add(zone16);
		zoneList.add(zone17);
		zoneList.add(zone18);
		zoneList.add(zone19);
		zoneList.add(zone20);
		zoneList.add(zone21);
		zoneList.add(zone22);
		zoneList.add(zone23);
		zoneList.add(zone24);
		zoneList.add(zone25);
		zoneList.add(zone26);
		zoneList.add(zone27);
		return zoneList;
	}
		
	public void getTimeInfo() {

		try {
			Object result = (Object)executeXml("Time.Get");
			
			if (result != null) {				
				Map res = (Map)result;
				Integer tick = (Integer)res.get("Tick");
				Integer zone = (Integer)res.get("Zone");								
				
				this.time = tick.toString();
				this.zone = zone.toString();
								
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public String setKlbTime(String date, String time, String zone) throws Exception {		
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		df.setTimeZone(TimeZone.getTimeZone("GMT-0"));
		
		Date time_date = df.parse(date.trim() + " " + time.trim());		
		String setTime = Long.toString((time_date.getTime())/1000L);
		Hashtable setTemp = new Hashtable();
		if (StringUtils.isNotEmpty(setTime)) {
			setTemp.put("Tick", NumberUtils.toInt(setTime));
		}
		if (StringUtils.isNotEmpty(zone)) {
			setTemp.put("Zone", NumberUtils.toInt(zone));
		}
		
		String flagStr = "false";
		try {
			Boolean setResult = (Boolean)executeXml("Time.Set", setTemp);
			if ( setResult != null && setResult == true ) {
				flagStr = "true";
			}
		} catch (XmlRpcException e) {
			e.printStackTrace();
			return Utils.GetExceptionMessage(e);
		}
		return flagStr;
	}

	public String getTime(){
		return this.time;
	}
	
	public String getZone(){
		return this.zone;
	}

}
