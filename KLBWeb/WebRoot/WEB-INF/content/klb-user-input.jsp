<%@ page contentType="text/html;charset=UTF-8"
%><%@ include file="/common/taglibs.jsp"
%><%@ page import="org.kylin.klb.entity.security.User"
%><%@ page import="org.kylin.klb.service.SecurityUtils"
%><%User user = SecurityUtils.getSecurityUser();%>
<c:set var="nowUser" value="<%=user%>"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/meta.jsp" %>
<title>Mini-Web 帐号管理</title>
<link href="${ctx}/css/skin/default/common.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/module.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/js/validate/jquery.validate.css" type="text/css" rel="stylesheet"/>
<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
<script src="${ctx}/js/validate/jquery.validate.js" type="text/javascript"></script>
<script src="${ctx}/js/validate/messages_cn.js" type="text/javascript"></script>
<script>
	$(document).ready(function() {
		//聚焦第一个输入框
		$("#loginName").focus();
		//为inputForm注册validate函数
		$("#inputForm").validate({
			rules: {
				loginName: {
					required: true,
					remote: "klb-user!checkLoginName.action?oldLoginName=" + encodeURIComponent('${loginName}')
				},
				name: "required",
				<s:if test="id == null">
				/* password: {
					required: true,
					minlength:3
				}, */
				passwordConfirm: {
					equalTo:"#password"
				},
				</s:if>
				email:"email"//,
				//auth:"required"
			},
			messages: {
				loginName: {
					remote: "用户登录名已存在"
				}
				<s:if test="id == null">
				//,
				passwordConfirm: {
					equalTo: "输入与上面相同的密码"
				}
				</s:if>
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
    <h3><span><s:if test="id == null">创建</s:if><s:else>修改</s:else>用户</span></h3>
    <form id="inputForm" action="klb-user!save.action" method="post">
		<input type="hidden" name="id" value="${id}"/>
		<table class="noborder">
			<tr>
				<td>登录名:</td>
				<td><input class="input" type="text" name="loginName" size="40" id="loginName" value="${loginName}"/></td>
			</tr>
			<tr>
				<td>用户名:</td>
				<td><input class="input" type="text" id="name" name="name" size="40" value="${name}"/></td>
			</tr>
			<s:if test="id == null">
				<tr>
					<td>密码:</td>
					<td><input class="input" type="password" id="password" name="password" size="40" value="${password}"/></td>
				</tr>
				<tr>
					<td>确认密码:</td>
					<td><input class="input" type="password" id="passwordConfirm" name="passwordConfirm" size="40" value="${password}"/>
					</td>
				</tr>
			</s:if>
			
			<tr>
				<td>邮箱:</td>
				<td><input class="input" type="text" id="email" name="email" size="40" value="${email}"/></td>
			</tr>
			<tr>
				<td>角色:</td>
				<td>
					<div style="word-break:break-all;width:250px; overflow:auto; ">
						管理员
						<s:if test="id == null">
							<s:checkbox name="auth" fieldValue="admin" value="1" theme="simple"></s:checkbox>
						</s:if>
						<s:else>
							<s:if test="auth == 'admin'">
								<s:checkbox name="auth" fieldValue="admin" value="1" theme="simple"></s:checkbox>
							</s:if>
							<s:else>
								<s:checkbox name="auth" fieldValue="admin" value="0" theme="simple"></s:checkbox>
							</s:else>
						</s:else>
					</div>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<c:if test="${nowUser.auth == 'admin'}">
						<button hider="#addUser" class="btn" type="submit"><span>保存</span></button>
					</c:if>
					<button onclick="history.back()" hider="#addUser" class="btn" type="button"><span>取消</span></button>
				</td>
			</tr>
		</table>
	</form>
  </div>
</div>
<!--主要内容结束-->
</body>
</html>
