<%@ page language="java" pageEncoding="UTF-8"
%><%@ include file="/common/taglibs.jsp"
%><%@ page import="org.kylin.klb.entity.security.User"
%><%@ page import="org.kylin.klb.service.SecurityUtils"
%><%
	User user = SecurityUtils.getSecurityUser();
	String type = request.getParameter("type");
	request.setAttribute("type",type);
%>
<script type="text/javascript" src="${ctx}/js/fc.js"></script>
<script type="text/javascript">
	function rebootServer(){
		if(confirm("确定重启服务器？")){
			parent.location.replace("reboot.action");
			return true;
		}
		return false;
	}
	
	function stopServer(){
		if(confirm("确定关闭服务器？")){
			parent.location.replace("shutdown.action");
			return true;
		}
		return false;
	}
</script>
<c:set var="user" value="<%=user%>"/>
<div class="header">
  <div class="tt">
    <h1><a title=""></a></h1>
    <div class="userArea">
      <ul>
      	<c:if test="${not empty user.id}">
			<li>您好，管理员</li>
        	<%-- <li><a href="javascript:;" onclick="fc.iframeProxy({'action':'klb-set-1.action'})">设置向导</a></li> --%>
        </c:if>
        <!--  
        <li><a href="javascript:;" onclick="fc.iframeProxy({'action':'klb-help.action'})">帮助</a></li>
        -->
        <c:if test="${not empty user.id}">
			<c:if test="${user.auth == 'admin'}">
				<c:if test="${empty type || type != '1'}">
					<li><a onclick="kylin.klb.user.jumpFrame('klb-user!changeInput.action?id=${user.id}');return;" href="javascript:void(0);">修改密码</a></li>
				</c:if>
			</c:if>
			<li><a onclick="rebootServer()" href="javascript:void(0);">重启服务器</a></li>
			<li><a onclick="stopServer()" href="javascript:void(0);">关闭服务器</a></li>
        	<li class="exit"><a href="javascript:;" onclick="fc.parentProxy({'action':'logout.action'})">退出</a></li>
        </c:if>
      </ul>
    </div>
  </div>
	<c:if test="${empty type || type != '1'}">
	  	<script type="text/javascript">
			$(document).ready(function() {
				//显示时间
				$('#epiClock').epiclock({ format : ' Y年F月j日　G:i:s　D' });  //绑定
		    	$.epiclock(); //开始运行
			});
		</script>
		<div class="ct">
			<!--快捷菜单区开始-->
    		<div class="quickMenu">
			<ul>
			<%-- <li><a onclick="kylin.klb.user.configCommit(this);" href="javascript:void(0);"><span>调度器设置生效</span></a></li> --%>
			</ul>
			</div>
	    	<!--快捷菜单区结束-->
		<div class="time" id="epiClock"></div>
		</div>
	</c:if>
</div>
