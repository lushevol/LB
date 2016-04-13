<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/meta.jsp" %>
<title>address-src</title>
<link href="${ctx}/css/skin/default/common.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/module.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/subindex.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
<script type="text/javascript" src="${ctx}/js/firewall/address.src.js"></script>
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
    <h4>
    	<a href="${ctx}/firewall/address!list.action"><font color="#DD0099">源地址转换</font></a> | 
		<a href="${ctx}/firewall/addressd!list.action">目的地址转换</a>
    </h4>
    <p class="vLine"></p>
   
    <div class="box3">
		<h3><span>源地址转换配置</span></h3>
	</div>
	  <form id="inputForm" name="inputForm" action="address-src!save.action" method="post">
  		<input class="input" type="hidden" name="operation" id="operation" value="${operation}"/>
  		<input class="input" type="hidden" name="id" id="id" value="${id}"/>
		
		<div id="show" style="color: red;">&nbsp;<s:actionmessage theme="mytheme"/></div>
		<table cellpadding="0" class="table">
			<tr>
				<th width="160">规则描述：</th>
				<td>
					<input class="input" type="text" name="nat.describe" id="describe" value="${nat.describe}"/>
				</td>
			</tr>
			<tr>
				<th width="160">源地址(/掩码)：<input class="input" type="hidden" name="nat.type" id="type" value="${nat.type}"/></th>
				<td>
					<input class="input" type="text" name="nat.srcIP" id="srcIP" value="${nat.srcIP}"/>	
				</td>
			</tr>
			<tr>
				<th>目的地址(/掩码)：</th>
				<td>
					<input class="input" type="text" name="nat.destIP" id="destIP" value="${nat.destIP}"/>	
				</td>
			</tr>
			<tr>
				<th>协议：</th>
				<td>
					<s:select list="#request.protocolList" theme="simple" name="protocols" id="protocols"
								listKey="value" listValue="display">
					</s:select>
					<label class="autoHide">&emsp;请输入其他协议：</label>
				</td>
				<td>
					<input class="input autoHide" type="text" name="nat.protocol" id="protocol" value="${nat.protocol}"/>
				</td>
			</tr>
			<tr>
				<th><label class="portGray">源端口：</label></th>
				<td>
					<input class="input" type="text" name="nat.srcPort" id="srcPort" value="${nat.srcPort}"/>
				</td>
			</tr>
			<tr>
				<th><label class="portGray">目的端口：</label></th>
				<td>
					<input class="input" type="text" name="nat.destPort" id="destPort" value="${nat.destPort}"/>								
				</td>
			</tr>
			<tr>
				<th>输出接口：</th>
				<td>
					<%-- <input class="input" type="text" name="nat.outFace" id="outFace" value="${nat.outFace}"/> --%>
					<s:select list="#request.interfaceList" theme="simple" name="nat.interfaces" id="interfaces" 
							headerKey="" headerValue="-选择接口-" listKey="value" listValue="display">
					</s:select>
				</td>
			</tr>			
		</table>
		<table cellpadding="0" class="table">
			<tr>
				<th width="160">不作转换：</th>				
				<td>
				 	<input type="checkbox" id="except" name="except" value=""/>
				 	<span>（<font color="#DD0099">默认启用转换规则，选中为不作转换</font>）</span>
				</td>
			</tr>
		</table>
		<table cellpadding="0" class="table">
			<tr>
				<th width="160"><label class="exceptGray">源地址转换为：</label></th>
				<td colspan="3">
					<input type="radio" class="radio" name="snat" value="" id="dynamicIp"/>&nbsp;<label class="exceptGray">动态IP</label>
					<input type="hidden" name="masq" id="masq" value="${nat.type}"/>
				</td>
			</tr>
			<tr>
				<th></th>
				<td colspan="3">
					<input type="radio" class="radio" name="snat" value="" checked="checked" id="fixedIp"/>&nbsp;
					<label class="exceptGray">固定IP</label>&emsp;
				</td>
				<td>
					<label class="autoGray exceptGray">IP地址&nbsp;(段)：</label>
				</td>
				<td>
					<input class="input ipInput" type="text" name="nat.startIP" id="startIP" value="${nat.startIP}"/>
					<label class="autoGray exceptGray">-</label>
				</td>
				<td>
					<input class="input ipInput" type="text" name="nat.endIP" id="endIP" value="${nat.endIP}"/>
				</td>
			</tr>
		</table>
		<table cellpadding="0" class="table">
			<tr>
				<th width="160"><label class="exceptGray portGray">源端口转换为：</label></th>				
				<td colspan="3">
					<input class="input ipInput" type="text" name="nat.startPort" id="startPort" value="${nat.startPort}"/>
					<label class="exceptGray portGray">-</label>
				</td>
				<td>
					<input class="input ipInput" type="text" name="nat.endPort" id="endPort" value="${nat.endPort}"/>
				</td>
			</tr>
		</table>
						
		<div class="btnArea pL160">
			<button class="btn" type="submit" id="button"><span>确定</span></button>
			<a href="${ctx}/firewall/address!list.action" class="btnStyle btnGray"><span>&ensp;取消</span></a>
		</div>
	  </form>
		<br />		
  </div>
  <!--文本内容区结束-->
</div>
<!--主要内容结束-->

</body>
</html>