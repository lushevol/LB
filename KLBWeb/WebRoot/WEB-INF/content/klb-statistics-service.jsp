<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>无标题文档</title>
<%@ include file="/common/meta.jsp"%>
<link rel="stylesheet" type="text/css" href="${ctx}/css/skin/default/common.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/skin/default/module.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/calendar/jscal2.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/calendar/border-radius.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/calendar/steel/steel.css" />

<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
<script src="${ctx}/js/calendar/jscal2.js" charset="utf-8"></script>
<script src="${ctx}/js/calendar/lang/cn.js" charset="utf-8"></script>
<script src="${ctx}/js/kylin.klb.statistics.service.js" charset="utf-8"></script>
<script src="${ctx}/js/kylin.statistics.initService.js" charset="utf-8"></script>
<script type="text/javascript">
	$(document).ready(function() {
		//var reDate = /^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/;
		var reDate = /^((((1[6-9]|[2-9]\d)\d{2})-(0[13578]|1[02])-(0[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0[13456789]|1[012])-(0[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-02-29-))$/;
		var reTime = /^(20|21|22|23|[0-1]\d):[0-5]\d:[0-5]\d$/;
		//日期验证
		$("#f_date1").blur(function(){
			$this = $(this);
			if ( !reDate.test($this.val()) ) {
				alert("您输入的起始日期不合法，请重新输入或点击右侧的日期按钮进行选择");	
			}
		});
		$("#f_date2").blur(function(){
			$this = $(this);
			if ( !reDate.test($this.val()) ) {
				alert("您输入的结束日期不合法，请重新输入或点击右侧的日期按钮进行选择");	
			}
		});
		//时间验证
		$("#f_time1").blur(function(){
			$this = $(this);
			if ( !reTime.test($this.val()) ) {
				alert("您输入的起始时间不合法，请输入HH:mm:ss格式的合法时间");	
			}
		});
		$("#f_time2").blur(function(){
			$this = $(this);
			if ( !reTime.test($this.val()) ) {
				alert("您输入的结束时间不合法，请输入HH:mm:ss格式的合法时间");	
			}
		});
	});
</script>
</head>
<body>
	<form name="ServiceForm" id="ServiceForm" action="klb-statistics-service!export.action" onsubmit="return false">
		<!--面板开始-->
		<div class="mainPartPanel">
			<div class="circle"></div>
			&nbsp;
		</div>
		<!--面板结束-->
		<!--主要内容开始-->
		<div class="mainPartContNoTop">
			<!--快速搜索开始 -->
			<div id="show" style="color: red;"></div>
			<div class="quickSearch">
				<table cellspacing="0">
					<tr>
						<th width="40">虚拟服务：</th>
						<td>
							<s:select list="#request.serverce" theme="simple" name="server" id="server" 
			              		headerKey="" headerValue="全部" listKey="key" listValue="value">
			                </s:select>
						</td>
						<th>起始日期：</th>
						<td>
							<div class="timearea">
								<input type="text" class="input" name="startDate" id="f_date1" />
								<a class="icocom btnTime" href="javascript:;" id="f_btn1">时间</a>
							</div>
						</td>
						<th>结束日期：</th>
						<td>
							<div class="timearea">
								<input type="text" class="input" name="endDate" id="f_date2" />
								<a class="icocom btnTime" href="javascript:;" id="f_btn2">时间</a>
							</div>
						</td>
						<th width="90">间隔周期：</th>
						<td>
							<select name="time" id="time" style="width: 156">
								<option value="60">分</option>
								<option value="3600" selected="selected">小时</option>
								<option value="86400">天</option>
								<option value="2592000">月</option>
							</select>
						</td>
						<td width="72">
							<button id="statisSub" class="btn btnGray"><span>统计</span></button>
						</td>
					</tr>
					<tr>
						<th width="73"></th>
						<td></td>
						<th>起始时间：</th>
						<td>
							<div class="timearea2">
								<input type="text" class="input" name="startTime" id="f_time1" value="${startTime}"/>
							</div>
						</td>
						<th>结束时间：</th>
						<td>
							<div class="timearea2">
								<input type="text" class="input" name="endTime" id="f_time2" value="${endTime}"/>
							</div>
						</td>
						<th width="90"></th>
						<td></td>
					</tr>
				</table>
			</div>

			<!--快速搜索结束 -->
			<!--曲线图开始 -->
			<div class="graph">
				<h3 class="graphTit">服务转发统计曲线图</h3>
				<p class="graphBtn">
					<a id="statisExport" href="javascript:;" class="btnStyle"><span>导出统计日志</span></a>
				</p>
				<div class="graphImg" id="graphChart"></div>
			</div>
			<!--曲线图结束 -->
		</div>
		</from>
		<!--主要内容结束-->
</body>
</html>
