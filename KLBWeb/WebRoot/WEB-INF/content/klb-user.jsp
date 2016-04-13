<%@ page contentType="text/html;charset=UTF-8"
%><%@ include file="/common/taglibs.jsp"
%><%@ page import="org.kylin.klb.entity.security.User"
%><%@ page import="org.kylin.klb.service.SecurityUtils"
%><%User user = SecurityUtils.getSecurityUser();%>
<c:set var="user" value="<%=user%>"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>无标题文档</title>
<%@ include file="/common/meta.jsp" %>
<link href="${ctx}/css/skin/default/common.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/module.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
<script type="text/javascript" src="${ctx}/js/kylin.klb.common.js"></script>
<script type="text/javascript" src="${ctx}/js/kylin.klb.user.js"></script>
</head>
<body>
<!--面板开始-->
<div class="mainPartPanel">
  <div class="circle"></div>
  &nbsp; </div>
<!--面板结束-->
<!--主要内容开始-->
<div class="mainPartContNoTop">
  <!--列表开始-->
  <div class="tableWrap">
    <!--表格外层左侧边框-->
    <div class="ct">
      <!--表格外层右侧边框-->
      <div class="main">
        <!--列表主要内容开始-->
        <form id="mainForm" action="klb-user.action" method="get">
        <input type="hidden" name="page.pageNo" id="pageNo" value="${page.pageNo}"/>
        <div class="tableList">
          <table>
            <caption>
            <span class="tit"><strong>用户列表</strong></span><div align="left" id="message" style="float:left;color: red;padding-left:10px;"><s:actionmessage theme="mytheme"/></div>
		  	<span class="operate"></span>
		  	<%-- <c:if test="${'admin' eq user.loginName}">			  	
			  		<a pop="#addUser" class="addLink popDivBt" href="klb-user!input.action">添加</a>			  	
		  	</c:if> --%>
            </caption>
			<thead>
              	<tr>
	                <th width="180">管理员名称</th>
	                <%-- <th width="100">用户名称</th> --%>
	                <th width="220">E-MAIL</th>
	                <th class="last lb2">操作</th>
              	</tr>
            </thead>
            <tbody>
            	<s:iterator value="user" status="count">
		          	<tr>
		        		<td>${loginName}&nbsp;</td>
		                <%-- <td>${name}&nbsp;</td> --%>
		                <td>${email}&nbsp;</td>
		                <td>
							<c:if test="${'admin' eq user.auth}">
				                <%--&nbsp;
								<c:if test="${user.loginName eq loginName || loginName eq 'admin'}">
									<a id="userDelId${count.count}" onclick="kylin.klb.user.del();return;" href="javascript:void(0);">删除</a>
								</c:if>
								--%>
								<c:if test="${user.loginName eq 'admin'}">
								<%--	<a href="klb-user!input.action?id=${id}">修改</a> --%>
									<a href="klb-user!changeInput.action?id=${id}">修改密码</a>&nbsp;
								<%-- <c:if test="${loginName ne 'admin'}">
										<a id="userDelId${count.count}" onclick="kylin.klb.user.del('${id}');return;" href="javascript:void(0);">删除</a>
									</c:if> --%>
								</c:if>
								
								<c:if test="${user.loginName ne 'admin'}">
									<c:if test="${loginName ne 'admin'}">
									<%--	<a href="klb-user!input.action?id=${id}">修改</a> --%>
										<a href="klb-user!changeInput.action?id=${id}">修改密码</a>&nbsp;
									<%-- <c:if test="${user.loginName ne loginName}">
											<a id="userDelId${count.count}" onclick="kylin.klb.user.del('${id}');return;" href="javascript:void(0);">删除</a>
										</c:if> --%>
									</c:if>
								</c:if>
							</c:if>
		                </td>
		         	</tr>
	         	</s:iterator>
            </tbody>
          </table>
        </div>
        </form>
        <!--列表主要内容结束-->
      </div>
    </div>
    <!--表格外层底部开始-->
    <div class="bt">
      <p></p>
      <div></div>
    </div>
    <!--表格外层底部结束-->
  </div>
  <div class="txtCont">
    <h4>说明：</h4>
    <p>&emsp;管理员登陆后可以对密码进行修改。操作过程中如遇到任何疑问，请发送邮件至zxm@kylinos.com.cn，我们会热诚地为您服务。</p>
  </div>
</div>
<!--主要内容结束-->
</body>
</html>
