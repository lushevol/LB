<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/common/meta.jsp"%>
	<title>无标题文档</title>
	<link href="${ctx}/css/skin/default/common.css" rel="stylesheet" type="text/css" />
	<link href="${ctx}/css/skin/default/module.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
	<script type="text/javascript" src="${ctx}/js/validate/jquery.validate.js"></script>
	<script type="text/javascript" src="${ctx}/js/validate/messages_cn.js"></script>
	<script type="text/javascript" src="${ctx}/js/network/dns.js"></script>
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
	<form id="inputForm" name="inputForm" action="dns!save.action" method="post">
		<div class="box3">
		<h3><span>域名服务器</span></h3>
			<div id="show" style="color: red;">&nbsp;<s:actionmessage theme="mytheme" /></div>
			<table cellpadding="0" class="table">
				<tr>
					<th width="193">首选域名服务器：</th>
					<td colspan="3">
						<input class="input" type="text" name="firstadd" id="firstadd" value="${firstadd}" />
					</td>
				</tr>
				<tr>
					<th width="193">备选域名服务器：</th>
					<td colspan="3">
						<input class="input" type="text" name="secondadd" id="secondadd" value="${secondadd}" />
					</td>
				</tr>
			</table>
			<div class="btnArea pL129">
				<button class="btn" type="submit" id="button"><span>保存</span></button>
				<button type="reset" class="btn btnGray" onclick="location.reload();"><span>取消</span></button>
			</div>
		</div>
	</form>
	<br />
	<div class="txtCont">
		
	</div>
</div>
<!--主要内容结束-->

</body>
</html>
