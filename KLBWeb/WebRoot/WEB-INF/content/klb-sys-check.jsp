<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/meta.jsp" %>
<title>无标题文档</title>
<link href="${ctx}/css/skin/default/common.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/module.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
<script type="text/javascript" src="${ctx}/js/kylin.klb.common.js"></script>
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
    <h4>系统自检说明：</h4>
    <p>&nbsp;&nbsp;&nbsp;&nbsp;系统自检针对用户配置内容进行检测，检测逻辑配置是否存在问题。主要包括：配套参数是否存在遗漏、关系是否正确；各项地址、掩码格式是否正确，地址与服务名是否有效等问题。</p>
    <p class="vLine"></p>
    <p class="txtCenter"> <a href="klb-sys-check!doCheck.action" class="bigBtn">系统自检</a></p>
  </div>
  <c:if test="${error == 'true'}">
	  <!--系统及配置文件状态开始 -->
	  <div class="box2">
	    <!--子模块标题区开始-->
	    <div class="tt">
	      <h3></h3>
	      <div></div>
	    </div>
	    <!--子模块标题区结束-->
	    <!--子模块内容区开始-->
	    <div class="ct">
	      <div class="main">
	        <div class="nrarea nrarea2">
	          <p>系统配置成功</p>
	          <ul>
	          	<li>系统程序及配置文件均正常</li>
	          </ul>
	        </div>
	      </div>
	    </div>
	    <!--子模块内容区结束-->
	    <!--子模块底部开始-->
	    <div class="bt">
	      <p></p>
	      <div></div>
	    </div>
	    <!--子模块底部结束-->
	  </div>
	  <!--系统及配置文件状态结束 -->
  </c:if>
  
  <c:if test="${error == 'false'}">
  	  <!--系统及配置文件error状态开始 -->
	  <div class="box2">
	    <!--子模块标题区开始-->
	    <div class="tt">
	      <h3></h3>
	      <div></div>
	    </div>
	    <!--子模块标题区结束-->
	    <!--子模块内容区开始-->
	    <div class="ct">
	      <div class="main">
	        <div class="nrarea nrarea2">
	          <p class="error">系统配置有错误</p>
	          <ul>
	          <s:iterator value="checkResults" status="count">
	           <li><span>${redString}</span>${blackString}</li>
	           </s:iterator>
	          </ul>	          	
	        </div>
	      </div>
	    </div>
	    <!--子模块内容区结束-->
	    <!--子模块底部开始-->
	    <div class="bt">
	      <p></p>
	      <div></div>
	    </div>
	    <!--子模块底部结束-->
	  </div>
	  <!--系统及配置文件error状态结束 -->
  </c:if>
  <!--文本内容区结束-->
</div>
<!--主要内容结束-->
</body>
</html>
