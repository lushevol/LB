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
<script type="text/javascript" src="${ctx}/js/kylin.klb.set2.js"></script>

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
      			<form id="inputForm" action="klb-set-3.action" name="inputForm" method="post" onsubmit="return false;">
      				<input id="guide" type="hidden" name="guide" value="${guide}"/>
	        		<!--内容-->
	        		<div class="quide quide2">
	          			<h3 class="set2">负载均衡虚拟服务设置</h3>
	          			<div class="content">
	            			<p class="word">&nbsp;&nbsp;&nbsp;&nbsp;添加新的负载均衡服务。若添加的是FTP类长时间保持会话的服务，需要设置连接保持时间，而不能启用默认参数0。</p>
	            			<div id="show" style="color: red;">&nbsp;<s:actionmessage theme="mytheme"/></div>
	            			<table>
	              				<tr>
					                <td class="col01" width="25%">虚拟服务名称：</td>
					                <td>
										<input class="input" type="text" name="service" id="service" value="${service}"/>
					                </td>
					          	</tr>
	              				<tr>
					                <td class="col01" width="25%">虚拟服务地址：</td>
					                <td>
										<input class="input" type="text" name="vipMark" id="vipMark" value="${vipMark}"/>
					                </td>
					          	</tr>
		              			<tr>
					                <td class="col01">虚拟服务掩码：</td>
					                <td><input class="input" type="text" name="persistentNetmask" id="persistentNetmask" value="${persistentNetmask}"/></td>
		              			</tr>
		              			<tr>
					          		<th></th>
					          		<c:if test="${empty tcpPorts || tcpPorts eq '80'}">
					          			<td class="disabled" id="tcptd">
						              		<input type="checkbox" id="tcp" name="tcp" value="tcpPorts"/>
							             	<label>TCP端口</label>
							                <input name="tcpPorts" disabled="disabled" type="text" class="input fno w44" id="tcpPorts" onblur="javascript:this.value=this.value.replace(/，/ig,',');" />
						            	</td>
					          		</c:if>
					          		<c:if test="${not empty tcpPorts && tcpPorts ne '80'}">
					          			<td class="enable" id="tcptd">
						              		<input type="checkbox" id="tcp" checked="checked" name="tcp" value="tcpPorts"/>
							             	<label>TCP端口</label>
							                <input name="tcpPorts" value="${tcpPorts}" type="text" class="input fno w44" id="tcpPorts" onblur="javascript:this.value=this.value.replace(/，/ig,',');" />
						            	</td>
					          		</c:if>
					            </tr>
					            <tr>
					 				<th></th>
					 				<c:if test="${empty udpPorts}">
					 					<td class="disabled" id="udptd">
						              		<input type="checkbox" name="udp" value="udpPorts" id="udp"/>
						           			<label>UDP端口</label>
						                	<input type="text" class="input fno w44" disabled="disabled" name="udpPorts" id="udpPorts" onblur="javascript:this.value=this.value.replace(/，/ig,',');"/>
						                </td>
					 				</c:if>
					 				<c:if test="${not empty udpPorts}">
						 				<td class="enable" id="udptd">
						              		<input type="checkbox" checked="checked" name="udp" value="udpPorts" id="udp"/>
						           			<label>UDP端口</label>
						                	<input type="text" class="input fno w44" value="${udpPorts}" name="udpPorts" id="udpPorts" onblur="javascript:this.value=this.value.replace(/，/ig,',');"/>
						                </td>
					 				</c:if>
					            </tr>
					            <tr>
		                			<td class="col01">连接保持：</td>
			              			<td>
			              				<input type="text" class="input w44" name="persistent" id="persistent" value="${persistent}"/>秒 
			                		</td>
		              			</tr>
	            			</table>
	          			</div>
	          			<p class="button">
	          				<button class="btn btnGray" onclick="goSet1();return false;" id="save_scheduler"><span>上一步</span></button>
	          				<button class="btn" onclick="goSet3();return false;" hider="#addScheduler"><span>下一步</span></button>
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
