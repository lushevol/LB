<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${sessionScope.productName }</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
<link href="${ctx}/css/skin/default/common.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/module.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
<script type="text/javascript" src="${ctx}/js/kylin.klb.set3.js"></script>

</head>
<body class="headerQuide">
<!--头部开始-->
<jsp:include page="/common/klb-header.jsp">
	<jsp:param name="type" value="1"/>
</jsp:include>
<!--头部结束-->
<div class="center" style="height:500px;">
	<!--向导开始-->
  	<!--背景-->
  	<div class="quideBg">
    	<div class="quideBgL2">
      		<div class="quideBgR">
	      		<form id="inputForm" name="inputForm" action="klb-set!save.action" method="post" onsubmit="return false;">
	      			<input id="service" type="hidden" name="service" value="${service}"/>
	      			<input id="guide" type="hidden" name="guide" value="${guide}"/>
	      			<input id="persistentNetmask" type="hidden" name="persistentNetmask" value="${persistentNetmask}"/>
	      			<input id="vipMark" type="hidden" name="vipMark" value="${vipMark}"/>
	      			<input id="tcpPorts" type="hidden" name="tcpPorts" value="${tcpPorts}"/>
	      			<input id="udpPorts" type="hidden" name="udpPorts" value="${udpPorts}"/>
	      			<input id="persistent" type="hidden" name="persistent" value="${persistent}"/>
	      			
	       			<!--内容-->
	        		<div class="quide quide2">
		          		<h3 class="set3">负载均衡真实服务器设置</h3>
		          		<div class="content">
		            		<p class="word">添加新的负载均衡服务完成后，为此服务添加真实服务器。</p>
		            		<div id="show" style="color: red;">&nbsp;</div>
		            		<table>
		            			<tr>
				                	<td class="col01" width="25%">真实服务器名称：</td>
				                	<td>
										<input class="input" type="text" name="serviceName" id="serviceName" value="${serviceName}"/>
				                	</td>
				              	</tr>
				              	<tr>
				                	<td class="col01" width="25%">真实服务器地址：</td>
				                	<td>
										<input class="input" type="text" name="ip" id="ip" value="${ip}"/>
				                	</td>
				              	</tr>
		              			<tr>
		                			<td class="col01">转发方式：</td>
		                			<td>
		                				<s:select list="#request.forwards" theme="simple"
							              	name="forward" id="forward" 
							              	headerKey="" headerValue="请选择" listKey="key" 
							              	listValue="value" ></s:select>
									</td>
		              			</tr>
		            		</table>
		          		</div>
	          			<p class="button">
	          				<button class="btn btnGray" onclick="goSet2();return false;" id="save_scheduler"><span>上一步</span></button>
	          				<button class="btn" onclick="sub();return false;" hider="#addScheduler"><span>完成</span></button>
		          			<a href="javascript:;" onclick="cancel();return false;" class="btnStyle btnGray"><span>取消</span></a>
	          			</p>
	        		</div>
	        	</form>
      		</div>
    	</div>
  	</div>
  	<!--向导结束-->
</div>
<!--底部版权开始-->
<%@ include file="/common/klb-footer.jsp" %>
<!--底部版权结束-->
</body>
</html>
