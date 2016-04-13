<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/meta.jsp" %>
<title>${sessionScope.productName }- 使用授权</title>
<link href="${ctx}/css/skin/default/login.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/register.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
<script type="text/javascript" src="${ctx}/js/kylin.klb.common.js"></script>
</head>
<body>
<div class="login">
	<div class="system">
		<h1>${sessionScope.productName }</h1>
	</div>
	<form id="loginForm" action="${ctx}/register.action" method="post">
		<div class="form">
			<h2>使用授权</h2>	
			<div class="form_errors" id="errorsId">
				<%if ("false".equals(request.getAttribute("register"))) {%>
					注册码无效，请重新输入.
				<%}%>
			</div>
			<div>
				<p>机器码：</p><br/>
				<h4>&nbsp;&nbsp;&nbsp;&nbsp;${code}</h4>
				<p>注册码：</p>
				<p><span class="input1">
					<input type='text' id='registerCode' name='registerCode'/>
					</span></p>
				<p class="buttonWrapper1">
					<input class="buttonLogin" type="submit" value="" title="index.html" />
				</p>
			</div>
		</div>
	</form>
</div>
</body>
</html>
