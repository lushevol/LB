<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/meta.jsp" %>
<title>klb-load-hostname</title>
<link href="${ctx}/css/skin/default/common.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/module.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/subindex.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>

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
		<form id="inputForm" name="inputForm" action="klb-load-hostname!save.action" method="post">
		<div class="box3">
			<h3><span>主机名配置</span></h3>
			<table cellpadding="0" class="table">
				<tr>
					<td colspan="2"><div id="show" style="color: red;">&nbsp;<s:actionmessage theme="mytheme"/></div></td>
				</tr>
				<%-- <tr>
					<th width="210">当前主机名：</th>
					<td>${hostname}</td>
				</tr> --%>
				<tr>
					<th width="170">主机名设置：</th>
					<td>
						<input id="hostname" type="text" class="input" name="hostname" value="${hostname}"/>
					</td>
				</tr>
			</table>
			<div class="btnArea pL129">
				<button class="btn" type="submit" id="button"><span>保存</span></button>
				<button type="reset" class="btn btnGray" onclick="location.reload();"><span>取消</span></button>
			</div>
		</div>
		</form>    
  </div>
  <!--文本内容区结束-->
</div>
<!--主要内容结束-->

</body>
</html>