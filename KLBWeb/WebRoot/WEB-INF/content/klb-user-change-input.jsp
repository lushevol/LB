<%@ page contentType="text/html;charset=UTF-8"
%><%@ include file="/common/taglibs.jsp"
%><%@ page import="org.kylin.klb.entity.security.User"
%><%@ page import="org.kylin.klb.service.SecurityUtils"
%><%User user = SecurityUtils.getSecurityUser();%>
<c:set var="nowUser" value="<%=user%>"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>Mini-Web 帐号管理</title>
	<%@ include file="/common/meta.jsp" %>
	<link href="${ctx}/css/skin/default/common.css" rel="stylesheet" type="text/css" />
	<link href="${ctx}/css/skin/default/module.css" rel="stylesheet" type="text/css" />
	<link href="${ctx}/js/validate/jquery.validate.css" type="text/css" rel="stylesheet"/>
	<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
	<script type="text/javascript" src="${ctx}/js/validate/jquery.validate.js"></script>
	<script type="text/javascript" src="${ctx}/js/validate/messages_cn.js"></script>
	<script>
		$(document).ready(function() {
			//聚焦第一个输入框
			$("#loginName").focus();
			//为inputForm注册validate函数
			$("#inputForm").validate({
				rules: {
					/* password: {
						required: true,
						minlength:3
					},
					newPassword: {
						required: true,
						minlength:3
					}, */
					passwordConfirm: {
						equalTo:"#newPassword"
					}
				},
				messages: {
					passwordConfirm: {
						equalTo: "输入与上面相同的密码"
					}
				}
			});
		});
	</script>
</head>

<body>

<!--面板开始-->
<div class="mainPartPanel">
  <div class="circle"></div>&nbsp;
</div>
<!--面板结束-->
<!--主要内容开始-->
<div class="mainPartContNoTop">
  <div class="box3">
    <h3><span>修改密码</span></h3>
    <div id="message" style="color: red;"><s:actionmessage theme="mytheme"/></div>
    <form id="inputForm" action="klb-user!changeUpdate.action" method="post">
		<input type="hidden" name="id" value="${id}"/>
		<input type="hidden" name="loginName" value="${loginName}"/>
		<table cellpadding="0" class="table">
			<tr>
				<th width="120">登录名：</th>
				<td>${loginName}</td>
			</tr>
			<tr>
				<th>原密码：</th>
				<td><input class="input" type="password" id="password" name="password" size="30"/></td>
			</tr>
			<tr>
				<th>新密码：</th>
				<td><input class="input" type="password" id="newPassword" name="newPassword" size="30"/></td>
			</tr>
			<tr>
				<th>确认密码：</th>
				<td><input class="input" type="password" id="passwordConfirm" name="passwordConfirm" size="30"/></td>
			</tr>
		</table>
		<div class="btnArea pL109">
			<c:if test="${nowUser.auth == 'admin'}">
				<button hider="#addUser" class="btn" type="submit"><span>保存</span></button>
			</c:if>
			<button onclick="history.back()" hider="#addUser" class="btn" type="button"><span>取消</span></button>
		</div>
	</form>
  </div>
</div>
<!--主要内容结束-->
</body>
</html>
