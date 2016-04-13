package org.kylin.klb.web.security;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Results;
import org.kylin.klb.entity.security.Display;
import org.kylin.klb.service.TimeService;
import org.kylin.modules.web.struts2.Struts2Utils;

import com.opensymphony.xwork2.ActionSupport;
@Namespace("/")
@Results( { @org.apache.struts2.convention.annotation.Result(name = "reload", location = "klb-sys-time!input.action", type = "redirect") })
public class KlbSysTimeAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	
	private String time;
	private String zone;	
	private String setDate;
	private String setTime;
	
	private TimeService timeService = new TimeService();
	
	public String input() throws Exception {
		
		HttpServletRequest request = Struts2Utils.getRequest();
		
		List<Display> zoneList = this.timeService.getZoneList();
		request.setAttribute("zoneList", zoneList);
		
		timeService.getTimeInfo();
		time = timeService.getTime();
		zone = timeService.getZone();		
		return "success";
	}
	
	public String save() throws Exception {		
		String timeMess =  timeService.setKlbTime(setDate, setTime, zone);		
		if ( timeMess.equals("true") ) {
			addActionMessage("系统时间设置成功");
		} else if ( timeMess.equals("false") ) {
			addActionMessage("系统时间设置失败");
		} else {
			addActionMessage( timeMess );
		}
		return "reload";
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getSetDate() {
		return setDate;
	}

	public void setSetDate(String setDate) {
		this.setDate = setDate;
	}

	public String getSetTime() {
		return setTime;
	}

	public void setSetTime(String setTime) {
		this.setTime = setTime;
	}

}
