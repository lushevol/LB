<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 

"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/meta.jsp" %>
<!--  
<title>麒麟天平负载均衡系统 - 登录</title>
-->
<title>${sessionScope.productName } - 登录</title>
<link href="${ctx}/css/skin/default/login.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
<script type="text/javascript" src="${ctx}/js/kylin.klb.common.js"></script>
<script type="text/javascript" src="${ctx}/js/kylin.klb.user.js"></script>
</head>
<body>
<div class="login">
	<div class="system">
		<h1>${sessionScope.productName }</h1>
	</div>
	<form id="loginForm" action="${ctx}/login.action" method="post">
		<div class="form">
			<h2>登录系统:
			</h2>	
			<div class="form_errors" id="errorsId">
				<%if ("2".equals(request.getParameter("error"))) {%>         
					与KLBManager连接失败.
				<%}if ("1".equals(request.getParameter("error"))) {%>
					用户名密码错误,请重试.
				<%}if ("3".equals(request.getParameter("error"))) {%>
					此帐号已从别处登录.
				<%}%>
			</div>
			<div>
				<label>用户名：</label>
				<p><span class="input">
					<input type='text' id='loginName' name='loginName' value='admin'/>
					</span></p>
				<label for="">密&nbsp;&nbsp;&nbsp;&nbsp;码：</label>
				<p><span class="input">
					<input type='password' id='password' name='password'/>
					</span></p>
				<p class="buttonWrapper">
					<input class="buttonLogin" type="submit" value="登录" title="index.html" />
				</p>
			</div>
		</div>
	</form>
</div>
</body>
</html>
