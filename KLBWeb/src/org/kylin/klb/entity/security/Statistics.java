package org.kylin.klb.entity.security;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Statistics {
	private String server;
	private String startDate;
	private String endDate;
	private String time;
	private String startTime = "00:00:00";
	private String endTime = "00:00:00";

	public String serverToXml() {
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String start = "";
		String end = "";
		try {
			Date s = df.parse(getStartDate());
			Date e = df.parse(getEndDate());
			start = Long.toString(s.getTime() / 1000L);
			end = Long.toString(e.getTime() / 1000L);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(
				"<Root><Statistics>").append("<Server get=\"1\" ").append(
				"from=\"").append(start).append("\" ").append("to=\"").append(
				end).append("\" ").append("interval=\"").append(getTime())
				.append("\" ").append("target=\"").append(getServer()).append(
						"\" />").append("</Statistics></Root>");
		return sb.toString();
	}

	public String serviceToXml() {
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String start = "";
		String end = "";
		try {
			Date s = df.parse(getStartDate());
			Date e = df.parse(getEndDate());

			start = Long.toString(s.getTime() / 1000L);
			// 
			end = Long.toString(e.getTime() / 1000L);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(
				"<Root><Statistics>").append("<Services get=\"1\" ").append(
				"from=\"").append(start).append("\" ").append("to=\"").append(
				end).append("\" ").append("interval=\"").append(getTime())
				.append("\" ").append("target=\"").append(getServer()).append(
						"\" />").append("</Statistics></Root>");
		return sb.toString();
	}

	public String getServer() {
		return this.server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getStartDate() {
		return this.startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return this.endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getTime() {
		return this.time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
