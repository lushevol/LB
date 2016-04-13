<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/meta.jsp"%>
<title>系统时间</title>
<link href="${ctx}/css/skin/default/common.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/module.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/calendar/jscal2.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/calendar/border-radius.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/calendar/steel/steel.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
<script type="text/javascript" src="${ctx}/js/calendar/jscal2.js" charset="utf-8"></script>
<script type="text/javascript" src="${ctx}/js/calendar/lang/cn.js" charset="utf-8"></script>
<script type="text/javascript" src="${ctx}/js/jquery.epiclock.js"></script>
<script type="text/javascript" src="${ctx}/js/kylin.klb.systime.js" charset="utf-8"></script>
<script type="text/javascript">
$(document).ready(function() {
	var cal = Calendar.setup( {
		onSelect : function(cal) {
			cal.hide();
		}
	});
	cal.manageFields("f_btn", "f_date", "%Y-%m-%d");
		
	//var reDate = /^((((1[6-9]|[2-9]\d)\d{2})-(0[13578]|1[02])-(0[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0[13456789]|1[012])-(0[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-02-29-))$/;
	var reDate = /^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/;
	var reTime = /^(20|21|22|23|[0-1]\d):[0-5]\d:[0-5]\d$/;
	//日期验证
	$("#f_date").blur(function(){
		$this = $(this);
		if ( !reDate.test($this.val()) ) {
			alert("您输入的日期不合法，请重新输入或点击右侧的日期按钮进行选择");	
		}
	});
	//时间验证
	$("#f_time").blur(function(){
		$this = $(this);
		if ( !reTime.test($this.val()) ) {
			alert("您输入的时间不合法，请输入HH:mm:ss格式的合法时间");
		}
	});
	$('#inputForm').submit(function(){
		if ( !reTime.test($("#f_time").val()) ) {
			alert("您输入的时间不合法，请输入HH:mm:ss格式的合法时间");
			return false;
		}
	});
});
</script>
</head>
<body>
	<!--面板开始-->
	<div class="mainPartPanel">
		<div class="circle"></div>
		&nbsp;
	</div>
	<!--面板结束-->
	<!--主要内容开始-->
	<div class="mainPartContNoTop">
		<form id="inputForm" name="inputForm" action="klb-sys-time!save.action" method="post">
		<div><input type="hidden" name="time" id="time" value="${time}"/></div>
		
		<script type="text/javascript">
	
			var time = $("#time").val();			
			time = time * 1000;
			var now = new Date();
			now.setTime(time);
											
			function CurentTime(){
				var ret = "";
				
				var year = (now.getUTCFullYear()).toString();
				ret = ret + year + "年";
				
				var month = (now.getUTCMonth()+1).toString();
				ret = ret + month + "月";
				
				var date = (now.getUTCDate()).toString();
				ret = ret + date + "日" + " &nbsp &nbsp ";
				
				var hour = (now.getUTCHours()).toString();
				if (hour.length==1) {
					hour = "0" + hour;
				}
				ret = ret + hour + ":";
				
				var minute = (now.getUTCMinutes()).toString();
				if (minute.length==1) {
					minute = "0" + minute;
				}
				ret = ret + minute + ":";
				
				var second = (now.getUTCSeconds()).toString();
				if (second.length==1) {
					second = "0" + second;
				}
				ret = ret + second + " &nbsp &nbsp ";
				
				//var day = (now.getDay()).toString();
				//ret = ret + day;
				
				return ret;
			}  
			function refresh(){
			 	document.getElementById("clock").innerHTML = CurentTime();
			 	now.setSeconds(now.getSeconds()+1);
			}
			setInterval('refresh()',1000);
		</script>
			<div class="box3">
				<div id="show" style="color: red;">&nbsp;<s:actionmessage theme="mytheme"/></div>
				<h3><span>当前时间</span></h3>
				
				  <table cellpadding="0" class="table">
					<tr>
						<th width="193"></th>
						<td><div class="time" id="clock"></div></td>
					</tr>																			
				  </table>
				
				<h3><span>时间设置</span></h3>				
				<table cellpadding="0" class="table">
					<tr>
						<th width="248">日期：</th>
						<td>
							<div class="timearea">
								<input type="text" class="input" name="setDate" id="f_date" />
								<a class="icocom btnTime" href="javascript:;" id="f_btn">时间</a>
							</div>
						</td>
					</tr>
					<tr>
						<th>时间：</th>
						<td>
							<div class="timearea2">
								<input type="text" class="input" name="setTime" id="f_time"/>
							</div>
						</td>
					</tr>
					<tr>
						<th>时区：</th>
						<td>
							<s:select list="#request.zoneList" theme="simple" name="zone" id="zone" 
				              listKey="value" listValue="display" style="width:81px">
				        	</s:select>
						</td>
					</tr>
				</table>				
				<div class="btnArea pL201">
					<button class="btn" type="submit" id="saveBtn"><span>保存</span></button>
					<button type="reset" class="btn btnGray" onclick="location.reload();"><span>重置</span></button>
				</div>
			</div>
		</form>
		<br />
		<div class="txtCont">
			<h4>说明：</h4>
			<p>
			&emsp;设置负载均衡器的本地时间。
			</p>
		</div>	
	</div>
	<!--主要内容结束-->

</body>
</html>
