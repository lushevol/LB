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
	<script src="${ctx}/js/nginx/kylin.klb.nginx.conf.js" type="text/javascript"></script>
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
		<form id="inputForm" name="inputForm" action="nginx-conf!save.action" method="post">
			<div class="box3">
				<div class="txtCont"><p></p></div>
				<div id="show" style="color: red;">&nbsp;<s:actionmessage theme="mytheme"/></div>
				
				<h3><span>七层负载状态</span></h3>
				<table cellpadding="0" class="table">			
					<tr>
						<th width="203">启用状态：</th>
						<td colspan="3" class="stc">						
							<s:if test="%{enabled == 'true'}">
								<s:if test="%{status == 'true'}"><font color="#0070EE">开启</font></s:if>
								<s:else><font color="#FF0000">异常</font></s:else>
							</s:if>
							<s:else><font color="#0070EE">关闭</font></s:else>
							
							<input class="input" type="hidden" name="enabled" id="enabled" value="${enabled}"/>
						</td>
						<td>
							<span><a href="javascript:;" class="btnStyle" id="start" onclick="kylin.klb.nginx.conf.start(this)">
									<span><s:if test="%{enabled == 'true'}">关闭</s:if> <s:else>开启</s:else></span></a></span>						
						</td>
					</tr>
				</table>
				<h3><span>参数配置</span></h3>
				<table cellpadding="0" class="table">
					<tr>
						<th width="203"><label class="openGray">域名限定：</label></th>
						<td colspan="3">
							<s:radio list="#{'true':'启用','false':'关闭'}" name="denyNotMatch" id="denyNotMatch" theme="simple"></s:radio>
						</td>
					</tr>
					<tr>
						<th><label class="openGray">同时开启线程数：</label></th>
						<td colspan="3">
							<input class="input" type="text" name="processor"  id="processor" value="${processor}"/>
							<span class="gray7">&nbsp;&nbsp;</span>
						</td>
					</tr>
					<tr>
						<th><label class="openGray">单个线程最大连接数：</label></th>
						<td colspan="3">
							<input class="input" type="text" name="connections" id="connections" value="${connections}"/>
							<span class="gray7">&nbsp;&nbsp;</span>
						</td>
					</tr>
					<tr>
						<th><label class="openGray">超时时间：</label></th>
						<td colspan="3">
							<input class="input" type="text" name="keepalive" id="keepalive" value="${keepalive}"/>
							<span class="gray7">秒 &nbsp;&nbsp;</span>
						</td>
					</tr>
					<tr>
						<th><label class="openGray">是否启用http压缩功能：</label></th>
						<td colspan="3">
							<s:radio list="#{'true':'是','false':'否'}" name="gzip" id="gzip" theme="simple"></s:radio>
						</td>
					</tr>
			
					<tr>
						<th><label class="openGray">压缩页面最小字节数：</label></th>
						<td colspan="3">
							<input class="input" type="text" name="gzipLength" id="gzipLength" value="${gzipLength}"/>
							<span class="gray7">建议>=1000 &nbsp;&nbsp;</span>
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
			<!--配置文件结束-->  
  		<div class="txtCont">
	   	 <h4>说明：</h4>
	   	 <p>&emsp;启用状态：异常，参数配置可能出现错误。</p>
	   	 <p>&emsp;保存：存储当前的系统配置。</p>
	   	 <p>&emsp;重置：放弃当前修改，恢复至所保存的配置。</p>
		</div>
	</div>
	<!--主要内容结束-->

</body>
</html>
