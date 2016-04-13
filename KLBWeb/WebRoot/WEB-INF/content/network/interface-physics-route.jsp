<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/meta.jsp" %>
<title>interface-physics</title>
<link href="${ctx}/css/skin/default/common.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/module.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/subindex.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/messages_cn.js"></script>
<script type="text/javascript" src="${ctx}/js/network/interface.physics.route.js"></script>
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
    	<a href="${ctx}/network/interface-physics!list.action"><font color="#DD0099">物理接口</font></a> | 
		<%-- <a href="${ctx}/network/interface-next.action">子接口</a> | --%>
		<a href="${ctx}/network/interface-bond!list.action">链路聚合</a>
    </h4>
    <p class="vLine"></p>
    <form id="routeModeForm" name="routeModeForm" action="interface-physics-route!save.action" method="post">
    <div class="box3">
    	<div id="show" style="color: red;">&nbsp;<s:actionmessage theme="mytheme"/></div>
    	<h3><span>基本信息</span></h3>
    	<table cellpadding="0" class="table">
			<tr>
				<th width="135">名称：</th>
				<td><span class="gray7">${ii.name}<input class="input" type="hidden" name="ii.name" id="name" value="${ii.name}"/></span></td>
			</tr>
			<tr>
				<th>描述：</th>
				<td colspan="3">
					<input class="input" type="text" name="ii.description" id="description" value="${ii.description}"/>
				</td>
			</tr>
			<tr>
				<th>状态：</th>
				<td colspan="3">
					<s:select list="#request.stateList" theme="simple" name="ii.state" id="state"
							listKey="value" listValue="display">
					</s:select>
				</td>
			</tr>
			<tr>
				<th>DHCP：</th>
				<td colspan="3">
					<s:select list="#request.dhcpList" theme="simple" name="ii.dhcp" id="dhcp"
							listKey="value" listValue="display">
					</s:select>
				</td>
			</tr>
		</table>
	
		<br />
		<h3><span>IP地址</span></h3>
    	<table cellpadding="0" class="table">
			<tr>
				<th width="135"><label class="dhcpGray">地址(/掩码)：</label>
					<input type="hidden" name="ii.ipList" id="ips" value="${ii.ipList}"/></th>
				<td>
					<input type="text" class="input" id="ip" name="ip" value="" maxlength="18"/>&emsp;&emsp;
				</td>
				<td>&emsp;&emsp;
					<a href="javascript:;" class="btnStyle" id="add" onclick="interface.physics.route.addIp()">
						<span><label class="dhcpGray">添加</label></span></a>
				</td>
			</tr>
		</table>
		<!--列表开始-->
		<div class="tableWrap">
			<!--表格外层左侧边框-->
			<div class="ct">
				<!--表格外层右侧边框-->
				<div class="main">
					<!--列表主要内容开始-->
					<div class="tableList">
						<table id="routeTable">
							<caption>
								<span class="tit"><strong>IP地址信息列表</strong></span>
								<div id="message" style="float:left;color: red;padding-left:10px;"></div>
								<span class="operate"></span>
							</caption>
							<thead>
								<tr>
									<th width="380">地址/掩码</th>
									<th class="last lb2">操作</th>
								</tr>
							</thead>
							<tbody>
								
							</tbody>
						</table>
					</div>
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
		<!--列表结束-->
		<table cellpadding="0" class="table">
			<tr>
				<th width="135">高级属性&nbsp;</th>				
				<td>
				 	<input type="checkbox" id="advanced" name="advanced" value="advanced"/>
				</td>
			</tr>														
		</table>
	</div>
		
	<div class="box3" id="advanceAttr">
		<h3><span>高级属性</span></h3>
    	<table cellpadding="0" class="table">
			<tr>
				<th width="135">MTU：</th>
				<td>
					<input class="input" type="text" name="ii.mtu" id="mtu" value="${ii.mtu}"/>
					<span class="gray7">&nbsp;[68--1500]</span>
				</td>
			</tr>
			<tr>
				<th>MAC：</th>
				<td colspan="3">
					<input class="input" type="text" name="ii.mac" id="mac" value="${ii.mac}"/>
					<a href="javascript:;" class="btnStyle" id="addMac" onclick="interface.physics.route.resumeMac()"><span>获取缺省MAC</span></a>
					<span class="gray7">[格式如 AA:BB:CC:DD:EE:FF]</span>
				</td>
			</tr>
			<tr>
				<th>协商模式：<input class="input" type="hidden" name="ii.negotiation" id="negotiation" value="${ii.negotiation}"/></th>
				<td colspan="3">
					<input type="radio" name="negotiation" value="" id="auto"/>&nbsp;自动协商
				</td>
			</tr>
			<tr>
				<th></th>
				<td colspan="3">
					<input type="radio" name="negotiation" value="" checked="checked" id="hand"/>&nbsp;手工设置&emsp;
					<label id="autoGray">双工模式：</label>
					<s:select list="#request.doubleList" theme="simple" name="ii.doubleMode" id="doubleMode"
							listKey="value" listValue="display">
					</s:select>
					<label id="autoGray0">&emsp;速&ensp;率：</label>
					<s:select list="#request.speedList" theme="simple" name="ii.speed" id="speed" 
							listKey="value" listValue="display">
					</s:select>
				</td>
			</tr>
			<tr>
				<th></th>
				<td colspan="3"></td>
			</tr>									
			<tr>
				<th>ARP状态：</th>
				<td colspan="3">
					<s:select list="#request.arpList" theme="simple" name="ii.arpState" id="arpState" 
							listKey="value" listValue="display">
					</s:select>
				</td>
			</tr>			
		</table>
    </div>
    	<div class="btnArea pL137">
			<button class="btn" type="submit" id="button"><span>确定</span></button>
		    <%-- <button type="button" class="btn btnGray" onclick="history.back()"><span>取消</span></button> --%>
		    <a href="${ctx}/network/interface-physics!list.action" class="btnStyle btnGray"><span>&ensp;取消</span></a>
		</div>
    </form>
	       
  </div>
  <!--文本内容区结束-->
</div>
<!--主要内容结束-->

</body>
</html>
