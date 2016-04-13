<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/common/meta.jsp"%>
	<title>无标题文档</title>
	<link href="${ctx}/css/skin/default/common.css" rel="stylesheet" type="text/css" />
	<link href="${ctx}/css/skin/default/module.css" rel="stylesheet" type="text/css" />
	<link href="${ctx}/css/skin/default/subindex.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
	<script src="${ctx}/js/validate/jquery.validate.js" type="text/javascript"></script>
	<script src="${ctx}/js/validate/messages_cn.js" type="text/javascript"></script>
	<script src="${ctx}/js/kylin.klb.loadsedundant.js" type="text/javascript"></script>
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
		<form id="inputForm" name="inputForm" action="klb-load-sedundant!save.action" method="post">
			<div class="box3">
				<div class="txtCont"><p></p></div>
				<div id="show" style="color: red;">&nbsp;<s:actionmessage theme="mytheme"/></div>
				<h3><span>高可用资源状态</span></h3>
				<table cellpadding="0" class="table">
					<tr>
						<th width="203"><label>本机资源状态：</label></th>
						<td>
							<s:if test="%{self == 'true'}"><font color="#008000">运行</font></s:if>
							<s:else><font color="#FF0000">待机</font></s:else>
						</td>
					</tr>
					<tr>
						<th><label>对方资源状态：</label></th>
						<td>
							<s:if test="%{other == 'true'}"><font color="#008000">运行</font></s:if>
							<s:else><font color="#FF0000">待机</font></s:else>
						</td>
					</tr>
				</table>
				<h3><span>高可用集群状态</span></h3>
				<table cellpadding="0" class="table">
					<tr>
						<th width="203">高可用状态：</th>
						<td colspan="3" class="stc">
							<font color="#0070EE"><s:if test="%{enabled == 'true'}">开启</s:if><s:else>关闭</s:else></font>&emsp;
							<input class="input" type="hidden" name="enabled" id="enabled" value="${enabled}"/>
						</td>
						<td>
							<span><a href="javascript:;" class="btnStyle" id="start" onclick="kylin.klb.loadsedundant.start(this)">
									<span><s:if test="%{enabled == 'true'}">关闭</s:if> <s:else>开启</s:else></span></a></span>
							<%-- <s:radio list="#{'true':'开','false':'关'}" name="available" theme="simple"></s:radio> --%>
						</td>
					</tr>
				</table>
				<h3><span>高可用集群参数</span></h3>
				<table cellpadding="0" class="table">
					<tr>
						<th width="203"><label class="openGray">心跳检测接口：</label></th>
						<td>
							<s:select list="#request.interfacesList" theme="simple" name="interfaces" id="interfaces"
								headerKey="" headerValue="-空-" listKey="value" listValue="display"></s:select>
						</td>
					</tr>
					<tr>
						<th><label class="openGray">心跳通讯端口：</label></th>
						<td colspan="3">
							<input class="input" type="text" name="udpport" size="5" maxlength="5" id="udpport" value="${udpport}"/>
						</td>
					</tr>
					<tr>
						<th><label class="openGray">对方IP地址：</label></th>
						<td colspan="3">
							<input class="input" type="text" name="ucast" id="ucast" value="${ucast}"/>
						</td>
					</tr>
					<tr>
						<th><label class="openGray">对方主机名：</label></th>
						<td colspan="3">
							<input class="input" type="text" name="hostname" id="hostname" value="${hostname}"/>
						</td>
					</tr>
					<tr>
						<th><label class="openGray">主备连接同步ID：</label></th>
						<td colspan="3">
							<input class="input" type="text" name="masterDevice" size="5"
								maxlength="5" id="masterDevice" value="${masterDevice}"/>
							<span class="gray7">&nbsp;范围[0~255]</span>
							<%-- <s:select list="#request.schedulerList" theme="simple" name="masterDevice" id="masterDevice" 
				              	headerKey="" headerValue="请选择" listKey="schedulerName" listValue="schedulerName.startsWith('_')?
				              	schedulerName.substring(1,schedulerName.length()):schedulerName"></s:select> --%>
						</td>
					</tr>
					<tr>
						<th><label class="openGray">心跳间隔时间：</label></th>
						<td colspan="3">
							<input class="input" type="text" name="keepalive" id="keepalive" value="${keepalive}"/>
							<span class="gray7">毫秒</span>
						</td>
					</tr>
					<tr>
						<th><label class="openGray">发出警告时间：</label></th>
						<td colspan="3">
							<input class="input" type="text" name="warntime" id="warntime" value="${warntime}"/>
							<span class="gray7">毫秒</span>
						</td>
					</tr>
					<tr>
						<th><label class="openGray">死亡判定时间：</label></th>
						<td colspan="3">
							<input class="input" type="text" name="deadtime" id="deadtime" value="${deadtime}" />
							<span class="gray7">毫秒</span>
						</td>
					</tr>
					<tr>
						<th><label class="openGray">启动等待时间：</label></th>
						<td colspan="3">
							<input class="input" type="text" name="inittime" id="inittime" value="${inittime}" />
							<span class="gray7">毫秒</span>
						</td>
					</tr>
					<tr>
						<th><label class="openGray">主机故障恢复，资源是否选迁回：</label></th>
						<td colspan="3">
							<s:radio list="#{'true':'是','false':'否'}" name="indirect" id="indirect" theme="simple"></s:radio>
						</td>
					</tr>
					<tr>
						<th><label class="openGray">启用邮件警报：</label></th>
						<td colspan="3">
							<s:radio list="#{'true':'是','false':'否'}" name="send" id="send" theme="simple"></s:radio>
						</td>
					</tr>
					<tr>
						<th><label class="openGray">接收人邮件地址：</label></th>
						<td colspan="3">
							<input class="input" type="text" name="address" id="address" value="${address}" />
						</td>
					</tr>
				</table>
				<div class="btnArea pL201">
					<button class="btn" type="submit" id="button"><span><label class="openGray">保存</label></span></button>
					<button type="reset" class="btn btnGray" onclick="location.reload();"><span>重置</span></button>
				</div>
			</div>
		</form>
		<br />
		<div class="txtCont">
			
		</div>	
	</div>
	<!--主要内容结束-->

</body>
</html>
