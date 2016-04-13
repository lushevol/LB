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

<script type="text/javascript">
	function goSet2 () {
		$("#inputForm").submit();
	}
	function cancel () {
		var guide = $("#guide");
		location.href="klb-set-cancel.action?guide="+guide.val();
		return false;
	}
</script>
</head>
<body class="headerQuide">
<!--头部开始-->
<jsp:include page="/common/klb-header.jsp">
	<jsp:param name="type" value="1"/>
</jsp:include>
<!--头部结束-->
<div class="center">
	<!--向导开始-->
	<!--背景-->
  	<div class="quideBg">
    	<div class="quideBgL">
      		<div class="quideBgR">
      			<form id="inputForm" name="inputForm" action="klb-set-2.action" method="post" onsubmit="return false;">
	        		<!--内容-->
	        		<div class="quide">
	          			<h3 class="set1">欢迎使用${sessionScope.productName }</h3>
	          			<div class="content">
	            			<p class="word">&nbsp;&nbsp;&nbsp;&nbsp;欢迎使用${sessionScope.productName }设置向导。设置向导将帮助您方便、快速地完成一次负载均衡基本配置。<br />
	            			&nbsp;&nbsp;&nbsp;&nbsp;单击“下一步”继续，或单击“取消”退出设置向导。
	            			</p>
	          			</div>
	          			<p class="button">
				       		<a href="javascript:;" onclick="goSet2();return false;" class="btnStyle"><span>下一步</span></a>
				          	<a href="javascript:;" onclick="cancel();return false;" class="btnStyle btnGray"><span>取消</span></a>
	          			</p>
	          			<p class="tip">
	          				<s:checkbox id="guide" name="guide" value="%{guide.equals('false')?'false':'true'}" fieldValue="1" />
	          				&nbsp;下次登录不再显示此页面
	          			</p>
	        		</div>
        		</form>
			</div>
		</div>
		<br />
		<br />
		<br />
	</div>
	<!--向导结束-->
</div>
<!--底部版权开始-->
<%@ include file="/common/klb-footer.jsp" %>
<!--底部版权结束-->
</body>
</html>
