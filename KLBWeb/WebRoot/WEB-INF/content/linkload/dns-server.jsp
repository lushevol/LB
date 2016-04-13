<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/meta.jsp" %>
<title>dns-server</title>
<link href="${ctx}/css/skin/default/common.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/module.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/subindex.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/messages_cn.js"></script>
<script type="text/javascript" src="${ctx}/js/linkload/dns.server.js"></script>
</head>
<body>
<!--面板开始-->
<div class="mainPartPanel">
  <div class="circle"></div>&nbsp;
</div>
<!--面板结束-->
<!--主要内容开始-->
<div class="mainPartContNoTop">
  <!--文本内容区开始-->
  <div class="txtCont">
    <h4>    	
    	<span><a href="${ctx}/linkload/dns-policy!list.action">DNS策略</a></span> |    	
		<span><a href="${ctx}/linkload/dns-server!input.action"><font color="#DD0099">服务器设置</font></a></span>
    </h4>
    <p class="vLine"></p>
    <form id="serverForm" name="serverForm" action="dns-server!save.action" method="post">
    <div class="box3">
    	<h3><span>服务器设置</span></h3>
    	<div id="show" style="color: red;">&nbsp;<s:actionmessage theme="mytheme"/></div>
    	<table cellpadding="0" class="table">
			<tr>
				<th width="170">服务器状态：</th>
				<td colspan="3">
					<s:select list="#request.stateList" theme="simple" name="ds.state" id="state"
							listKey="value" listValue="display">
					</s:select>
				</td>
			</tr>
			<tr>
				<th width="170">DNS端口：</th>
				<td>
					<input class="input w44" type="text" name="ds.port" id="port" value="${ds.port}"/>
				</td>
			</tr>
			<tr>
				<th>支持反向查询：</th>
				<td colspan="3">
					<s:select list="#request.reverseList" theme="simple" name="ds.reverse" id="reverse"
							listKey="value" listValue="display">
					</s:select>
				</td>
			</tr>
		</table>									
	</div>
	    <div class="btnArea pL109">
			<button class="btn" type="submit" id="button"><span>确定</span></button>
			<button type="button" class="btn btnGray" onclick="location.reload();"><span>取消</span></button>
		</div>
    </form>
  </div>
  <!--文本内容区结束-->
</div>
<!--主要内容结束-->

</body>
</html>