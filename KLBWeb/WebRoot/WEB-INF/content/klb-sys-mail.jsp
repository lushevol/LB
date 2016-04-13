<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/meta.jsp" %>
<title>邮件管理</title>
<link href="${ctx}/css/skin/default/common.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/module.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/subindex.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$('#inputForm').submit(function(){
		var checked = $("#set1").attr('checked');
		
		if(checked == false){
			return true;
		}
		var tmp = $("#smtp").val() + "";
		tmp = $.trim(tmp);
		if ( tmp == "" ) {
			alert("smtp服务器不能为空");
			return false;
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
  <!--文本内容区开始-->
  <div class="txtCont">	
		<form id="inputForm" name="inputForm" action="klb-sys-mail!save.action" method="post">
		<div class="box3">
			<h3><span>邮件警报配置</span></h3>
			<table cellpadding="0" class="table">
				<tr>
					<td colspan="2"><div id="show" style="color: red;">&nbsp;<s:actionmessage theme="mytheme"/></div></td>
				</tr>
				<tr>
					<th width="190">smtp服务器：</th>
					<td>
						<input id="smtp" type="text" class="input" name="smtp" value="${smtp}"/>
					</td>
				</tr>
				
				
				<tr>
					<th width="190">smtp认证用户名：</th>
					<td>
						<input id="user" type="text" class="input" name="user" value="${user}"/>
					</td>
				</tr>
				
				<tr>
					<th width="190">smtp认证密码：</th>
					<td>
						<input id="passwd" type="password" class="input" name="passwd" value="${passwd}"/>
					</td>
				</tr>
				<tr>
					<th width="190">发件人名称：</th>
					<td>
						<input id="address" type="text" class="input" name="address" value="${address}"/>
					</td>
				</tr>
				<tr>
					<th width="190">启&ensp;用：</th>
					<td>&nbsp;		
						<s:if test="%{enabled == 'true'}">
					    	<input type="radio" name="enabled" value="true" checked="checked" id="set1"/>&nbsp;是&emsp;<input type="radio" name="enabled" value="false" id="set2"/>&nbsp;否</s:if>
					    <s:else><input type="radio" name="enabled" value="true"  id="set1"/>&nbsp;是&emsp;<input type="radio" name="enabled" value="false" checked="checked" id="set2"/>&nbsp;否</s:else>
					</td>
				</tr>
			</table>
			<div class="btnArea pL129">
				<button class="btn" type="submit" id="button"><span>保存</span></button>
				<button type="reset" class="btn btnGray" onclick="location.reload();"><span>取消</span></button>
			</div>
		</div>
		</form>
  </div>
  <!--文本内容区结束-->
</div>
<!--主要内容结束-->

</body>
</html>