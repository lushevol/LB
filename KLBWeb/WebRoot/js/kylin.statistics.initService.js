window.onload = initTime;

function initTime(){
	var nowdate = new Date();
	var endYear = (nowdate.getFullYear()).toString();
	var endMonth = (nowdate.getMonth()+1).toString();
	if (endMonth.length==1) {
		endMonth = "0" + endMonth;
	}
	var endDate = (nowdate.getDate()).toString();
	if (endDate.length==1) {
		endDate = "0" + endDate;
	}
	
	var endTime = "00:00:00";
	
	document.getElementById("f_date2").value = endYear+"-"+endMonth+"-"+endDate;
	document.getElementById("f_time2").value = endTime;
	
	var twoDatesAgo = getTwoDatesAgo();
	var startYear = (twoDatesAgo.getFullYear()).toString();
	var startMonth = (twoDatesAgo.getMonth()+1).toString();
	if (startMonth.length==1) {
		startMonth = "0" + startMonth;
	}
	var startDate = (twoDatesAgo.getDate()).toString();
	if (startDate.length==1) {
		startDate = "0" + startDate;
	}
	
	var startTime = "00:00:00";
	document.getElementById("f_date1").value = startYear+"-"+startMonth+"-"+startDate;
	document.getElementById("f_time1").value = startTime;	
}

function getTwoDatesAgo() {
    var newdate=new Date();
    var newtimems=newdate.getTime()-(2*24*60*60*1000);
    newdate.setTime(newtimems);
    return newdate;    
}
