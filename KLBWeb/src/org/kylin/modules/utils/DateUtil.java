package org.kylin.modules.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class DateUtil {
	public static final String YEAR = " year ";
	public static final String MONTH = " month ";
	public static final String DAY = " day ";
	public static final String WEEK = " week ";
	public static final String HOUR = " hour ";
	public static final String MINUTE = " minute ";
	public static final String SECOND = " second ";
	public static final String PERIOD_YEAR_FORMAT = "yyyy";
	public static final String PERIOD_DATE_FORMAT = "yyyy-MM";
	public static final String DATE_MONTH = "yyyy-MM";
	public static final String DATE_MONTH_CN = "yyyy年MM月";
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATE_WEEK_FORMAT = "yyyy-MM-dd EEEE";
	public static final String PRODUCT_DATE_FORMAT = "yy/MM/dd";

	public static boolean checkParam(Object param) {
		return (param == null) || ("".equals(param));
	}

	private static long getCurrentTimeMillis() {
		return System.currentTimeMillis();
	}

	public static Date getCurrentDate() {
		return new Date(getCurrentTimeMillis());
	}

	public static String getCurrentFormatDateTime() {
		Date date = getCurrentDate();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(date);
	}

	public static Date getCurrentFormateDateTimeToDate() {
		Date date = getCurrentDate();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String dateStr = dateFormat.format(date);
		return dateFormat.parse(dateStr, new ParsePosition(0));
	}

	public static String getCurrentFormatDate() {
		Date date = new Date(getCurrentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(date);
	}

	public static Date getCurrentFormatDateToDate() {
		Date date = new Date(getCurrentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = dateFormat.format(date);
		return dateFormat.parse(dateStr, new ParsePosition(0));
	}

	public static String getCurrentFormatDateWeek() {
		Date date = new Date(getCurrentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd EEEE");
		return dateFormat.format(date);
	}

	public static String getCurrentCustomizeFormatDate(String pattern) {
		if (checkParam(pattern)) {
			return "";
		}
		Date date = getCurrentDate();
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(date);
	}

	public static String formatDate(Date date, String pattern) {
		if ((checkParam(pattern)) || (checkParam(date))) {
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

		return dateFormat.format(date);
	}

	public static String formatDate(Date date, String pattern, Locale locale) {
		if ((checkParam(pattern)) || (checkParam(date))) {
			return "";
		}
		if (checkParam(locale)) {
			locale = Locale.getDefault();
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, locale);
		return dateFormat.format(date);
	}

	public static Date parseStrToDate(String dateStr) {
		if (checkParam(dateStr)) {
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.parse(dateStr, new ParsePosition(0));
	}

	public static Date parseStrToDateTime(String dateStr) {
		if (checkParam(dateStr)) {
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return dateFormat.parse(dateStr, new ParsePosition(0));
	}

	public static Calendar parseStrToCalendar(String dateStr) {
		if (checkParam(dateStr)) {
			return null;
		}
		Date date = parseStrToDateTime(dateStr);
		Locale locale = Locale.getDefault();
		Calendar cal = new GregorianCalendar(locale);
		cal.setTime(date);
		return cal;
	}

	public static String parseDateStrToDateTimeStr(String dateStr) {
		if (checkParam(dateStr)) {
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = dateFormat.parse(dateStr, new ParsePosition(0));
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static Date parseStrToCustomPatternDate(String dateStr,
			String pattern) {
		if ((checkParam(pattern)) || (checkParam(dateStr))) {
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		Date date = dateFormat.parse(dateStr, new ParsePosition(0));
		return date;
	}

	public static String convertDatePattern(String dateStr, String patternFrom,
			String patternTo) {
		if ((checkParam(patternFrom)) || (checkParam(patternTo))
				|| (checkParam(dateStr))) {
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(patternFrom);
		Date date = dateFormat.parse(dateStr, new ParsePosition(0));
		return formatDate(date, patternTo);
	}

	public static Date addDays(Date date, int days) {
		if (checkParam(date)) {
			return null;
		}
		if (days == 0) {
			return date;
		}
		Locale locale = Locale.getDefault();
		Calendar calendar = new GregorianCalendar(locale);
		calendar.setTime(date);
		calendar.add(5, days);
		return calendar.getTime();
	}

	public static Date minusDays(Date date, int days) {
		return addDays(date, 0 - days);
	}

	public static String addDate(String dateStr, String pattern, String type,
			int count) {
		if ((checkParam(dateStr)) || (checkParam(pattern))
				|| (checkParam(type))) {
			return "";
		}
		if (count == 0) {
			return dateStr;
		}
		Date date = parseStrToCustomPatternDate(dateStr, pattern);
		Locale locale = Locale.getDefault();
		Calendar calendar = new GregorianCalendar(locale);
		calendar.setTime(date);

		if (" year ".equals(type))
			calendar.add(1, count);
		else if (" month ".equals(type))
			calendar.add(2, count);
		else if (" day ".equals(type))
			calendar.add(5, count);
		else if (" week ".equals(type))
			calendar.add(4, count);
		else if (" hour ".equals(type))
			calendar.add(10, count);
		else if (" minute ".equals(type))
			calendar.add(12, count);
		else if (" second ".equals(type))
			calendar.add(13, count);
		else {
			return "";
		}

		return formatDate(calendar.getTime(), pattern);
	}

	public static String minusDate(String dateStr, String pattern, String type,
			int count) {
		return addDate(dateStr, pattern, type, 0 - count);
	}

	public static int compareDate(String dateStr1, String dateStr2,
			String pattern) {
		if ((checkParam(dateStr1)) || (checkParam(dateStr2))
				|| (checkParam(pattern))) {
			return 999;
		}
		Date date1 = parseStrToCustomPatternDate(dateStr1, pattern);
		Date date2 = parseStrToCustomPatternDate(dateStr2, pattern);

		return date1.compareTo(date2);
	}

	public static String getFirstDayInMonth(String dateStr) {
		if (checkParam(dateStr)) {
			return "";
		}
		Date date = parseStrToDate(dateStr);
		Locale locale = Locale.getDefault();
		Calendar calendar = new GregorianCalendar(locale);
		calendar.setTime(date);
		int firstDay = calendar.getActualMinimum(5);
		calendar.set(5, firstDay);
		return formatDate(calendar.getTime(), "yyyy-MM-dd");
	}

	public static String getLastDayInMonth(String dateStr) {
		if (checkParam(dateStr)) {
			return "";
		}
		Date date = parseStrToDate(dateStr);
		Locale locale = Locale.getDefault();
		Calendar calendar = new GregorianCalendar(locale);
		calendar.setTime(date);
		int lastDay = calendar.getActualMaximum(5);
		calendar.set(5, lastDay);
		return formatDate(calendar.getTime(), "yyyy-MM-dd");
	}

	public static String getFirstDayInWeek(String dateStr) {
		if (checkParam(dateStr)) {
			return "";
		}
		Date date = parseStrToDate(dateStr);
		Locale locale = Locale.getDefault();
		Calendar calendar = new GregorianCalendar(locale);
		calendar.setTime(date);
		int firstDay = calendar.getActualMinimum(7);
		calendar.set(7, firstDay);
		return formatDate(calendar.getTime(), "yyyy-MM-dd");
	}

	public static String getNoDayInWeek(String dateStr, int nDate) {
		if (checkParam(dateStr)) {
			return "";
		}
		Date date = parseStrToDate(dateStr);
		Locale locale = Locale.getDefault();
		Calendar calendar = new GregorianCalendar(locale);
		calendar.setTime(date);
		calendar.set(7, nDate);
		return formatDate(calendar.getTime(), "yyyy-MM-dd");
	}

	public static String getLastDayInWeek(String dateStr) {
		if (checkParam(dateStr)) {
			return "";
		}
		Date date = parseStrToDate(dateStr);
		Locale locale = Locale.getDefault();
		Calendar calendar = new GregorianCalendar(locale);
		calendar.setTime(date);
		int lastDay = calendar.getActualMaximum(7);
		calendar.set(7, lastDay);

		return formatDate(calendar.getTime(), "yyyy-MM-dd");
	}

	public static Date addTimeHour(Date date, int minute) {
		if (checkParam(date)) {
			return null;
		}
		if (minute == 0) {
			return date;
		}
		Locale locale = Locale.getDefault();
		Calendar calendar = new GregorianCalendar(locale);
		calendar.setTime(date);
		calendar.add(12, minute);
		return calendar.getTime();
	}

	public static String specifyTimeHour(String specifyTime, String pattern) {
		Locale locale = Locale.getDefault();
		Calendar calendar = new GregorianCalendar(locale);
		Date date = parseStrToDateTime(specifyTime);
		calendar.setTime(date);
		if (calendar.get(12) <= 30) {
			calendar.set(12, 30);
			calendar.set(13, 0);
		} else {
			calendar.add(11, 1);
			calendar.set(12, 0);
			calendar.set(13, 0);
		}
		return formatDate(calendar.getTime(), pattern);
	}

	public static String currentTimeHour(String pattern) {
		Locale locale = Locale.getDefault();
		Calendar calendar = new GregorianCalendar(locale);
		Date date = getCurrentDate();
		calendar.setTime(date);
		if (calendar.get(12) <= 30) {
			calendar.set(12, 30);
		} else {
			calendar.add(11, 1);
			calendar.set(12, 0);
		}
		return formatDate(calendar.getTime(), pattern);
	}

	public static long getTotalTime(String startTime, String endTime) {
		if ((checkParam(startTime)) || (checkParam(endTime))) {
			return 0L;
		}
		Calendar end = parseStrToCalendar(endTime);
		Calendar stard = parseStrToCalendar(startTime);

		return end.getTimeInMillis() - stard.getTimeInMillis();
	}

	public static int getTotalDay(Date startDay, Date endDay) {
		if ((checkParam(startDay)) || (checkParam(endDay))) {
			return 0;
		}
		Locale locale = Locale.getDefault();
		Calendar start = new GregorianCalendar(locale);
		Calendar end = new GregorianCalendar(locale);
		start.setTime(startDay);
		end.setTime(endDay);
		int y = end.get(1) - start.get(1);
		if (y != 0) {
			return end.get(6) - start.get(6) + 365 * y;
		}
		return end.get(6) - start.get(6);
	}

	public static List<String> getDaySlice(Date startDay, Date endDay) {
		int space = getTotalDay(startDay, endDay);
		List spaceList = new ArrayList();
		for (int i = 0; i <= space; ++i) {
			Date dateSpace = addDays(startDay, i);
			spaceList.add(i, formatDate(dateSpace, "yyyy-MM-dd"));
		}
		return spaceList;
	}

	public static String interval(String interval) {
		if (interval.equals("60"))
			return "分钟";
		if (interval.equals("3600"))
			return "小时";
		if (interval.equals("86400"))
			return "天";
		if (interval.equals("2592000")) {
			return "月";
		}
		return null;
	}
}
