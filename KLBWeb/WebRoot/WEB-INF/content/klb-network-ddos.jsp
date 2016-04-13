<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/common/meta.jsp"%>
	<title>无标题文档</title>
	<link href="${ctx}/css/skin/default/common.css" rel="stylesheet" type="text/css" />
	<link href="${ctx}/css/skin/default/module.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<!--面板开始-->
	<div class="mainPartPanel">
		<div class="circle"></div>
		&nbsp;
	</div>
	<!--面板结束-->
	<!--主要内容开始-->
	<div class="mainPartContNoTop">
		<form id="inputForm" name="inputForm" action="klb-network-ddos!save.action" method="post">
			<div class="box3">
				<h3><span>DDOS攻击防护</span></h3>
				<table cellpadding="0" class="table">
					<tr>
						<td colspan="2"><div id="show" style="color: red;">&nbsp;<s:actionmessage theme="mytheme"/></div></td>
					</tr>
					<tr>
						<th width="210">DDOS攻击防护状态：</th>
						<td>${status}</td>
					</tr>
					<tr>
						<th>DDOS攻击防护设置：</th>
						<td>
							<s:select list="#{'off':'关闭','on':'开启'}" theme="simple" name="ddosset"></s:select>
						</td>
					</tr>
				</table>
				<div class="btnArea pL129">
					<button class="btn" type="submit" id="button"><span>保存</span></button>
					<button type="reset" class="btn btnGray" onclick="location.reload();"><span>重置</span></button>
				</div>
			</div>
		</form><br/>
	<div class="txtCont">	   
    </div>
	</div>
	<!--主要内容结束-->
</body>
</html>
