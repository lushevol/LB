/**
 * @fileOverview 麒麟天平负载均衡系统 统计
 * @author kylin
 */

if (!window.kylin) {
	window.kylin = {};
}
if (!window.kylin.klb) {
	window.kylin.klb = {};
}
if (!window.kylin.klb.statistics) {
	window.kylin.klb.statistics = {};
}
if (!window.kylin.klb.statistics.server) {
	window.kylin.klb.statistics.server = {};
}


$(document).ready(function() {
	var cal = Calendar.setup( {
		onSelect : function(cal) {
			cal.hide();
		}
	});
	cal.manageFields("f_btn1", "f_date1", "%Y-%m-%d");
	cal.manageFields("f_btn2", "f_date2", "%Y-%m-%d");

	kylin.klb.statistics.server.showChart();

	$("#statisSub").click(function() {
		kylin.klb.statistics.server.showChart();
		return;
	});
	$("#statisExport").click(function(e) {
		e.stopPropagation();
		kylin.klb.statistics.server.excel();
		return false;
	});
});

(function(package) {
	jQuery.extend(package, {
		showChart : function() {
			var img = [];
			var server = $("#server").val();
			var startDate = $("#f_date1").val();
			var endDate = $("#f_date2").val();
			var startTime = $("#f_time1").val();
			var endTime = $("#f_time2").val();
			var time = $("#time").val();
			if (!package.checkfrom()) {
				return;
			}
			$.ajax({
				type: "POST",
				url: "klb-statistics-server!initServer.action",
				dataType : "json",
				timeout : 10000,
				success: function(json){
					if(typeof json != "undefined") {
						var obj = json.obj;
						if(json.auth == true) {
							if ($.trim(server) == "") {
								var max = $("#server option:last").attr("index");
								for (var i = 0;i <= max; i++) {
									var val = $("#server option[index=" + i + "]").val();
									if ($.trim(val) != "") {
										var param = "statistics.server=" + encodeURIComponent(val)
												+ "&statistics.startDate=" + startDate + "&statistics.startTime=" + startTime 
												+ "&statistics.endDate=" + endDate + "&statistics.endTime=" + endTime 
												+ "&statistics.time=" + time;
										var chartSrc = "klb-statistics!server.action?" + param
												+ "&r=" + Math.random();
										img.push("<img id=\"statisticsid\" src=\"" + chartSrc
												+ "\" alt=\"\" width=\"763\" /></br>");
									}
								}
							} else {
								var param = "statistics.server=" + server + "&statistics.startDate=" + startDate
										+ "&statistics.startTime=" + startTime + "&statistics.endDate=" + endDate
										+ "&statistics.endTime=" + endTime + "&statistics.time=" + time;
								var chartSrc = "klb-statistics!server.action?" + param + "&r="
										+ Math.random();
								img.push("<img id=\"statisticsid\" src=\"" + chartSrc
										+ "\" alt=\"\" width=\"763\" />");
							}
				
							$("#graphChart").html(img.join(""));
						}
					}
				},
				error : function(){
					
				}
			});
		},
		excel: function(){
			var server = $("#server").val();
			var startDate = $("#f_date1").val();
			var endDate = $("#f_date2").val();
			var startTime = $("#f_time1").val();
			var endTime = $("#f_time2").val();
			var time = $("#time").val();
			if (!package.checkfrom()) {
				return;
			}
			if ($.trim(server) != "") {
				var chartSrc = "klb-st-server.action?r="
						+ Math.random();
				location.href=chartSrc;
			} else {
				var chartSrc = "klb-st-server.action?r="
						+ Math.random();
				location.href=chartSrc;
			}
		},
		checkfrom : function() {
			var startDate = $("#f_date1").val();
			var endDate = $("#f_date2").val();
			var startTime = $("#f_time1").val();
			var endTime = $("#f_time2").val();
			var time = $("#time").val();
			
			var startDateArray = startDate.split("-");
			var startTimeArray = startTime.split(":");
			
			var endDateArray = endDate.split("-");
			var endTimeArray = endTime.split(":");
			
			var yearcha = endDateArray[0]-startDateArray[0];
			var monthcha = endDateArray[1]-startDateArray[1];
			var datecha = endDateArray[2]-startDateArray[2];
			
			var hourcha = endTimeArray[0]-startTimeArray[0];
			var minutecha = endTimeArray[1]-startTimeArray[1];
			var secondcha = endTimeArray[2]-startTimeArray[2];
			
			var interval = yearcha*365*24*60*60+
			monthcha*30*24*60*60+
			datecha*24*60*60+
			hourcha*60*60+
			minutecha*60+
			secondcha
			
			if ($.trim(startDate) == "" && $.trim(endDate) != "") {
				alert("请选择起始日期");
				return false;
			}

			if ($.trim(startDate) != "" && $.trim(endDate) == "") {
				alert("请选择结束日期");
				return false;
			}

			if (startDate > endDate) {
				alert("起始日期不能大于结束日期");
				return false;
			}
			
			if (time > interval) {
				alert("您所选择的时间间隔小于间隔周期");
				return false;
			}
			
			return true;
		}
	});
})(kylin.klb.statistics.server);
