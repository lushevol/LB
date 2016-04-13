<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/meta.jsp" %>
<title>interface-bond-config</title>
<link href="${ctx}/css/skin/default/common.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/module.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/subindex.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/messages_cn.js"></script>
<script type="text/javascript" src="${ctx}/js/network/interface.bond.config.js"></script>
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
    	<a href="${ctx}/network/interface-physics!list.action">物理接口</a> | 
		<%-- <a href="${ctx}/network/interface-next.action">子接口</a> | --%>
		<a href="${ctx}/network/interface-bond!list.action"><font color="#DD0099">链路聚合</font></a>
    </h4>
    <p class="vLine"></p>
    <form id="bondConfigForm" name="bondConfigForm" action="interface-bond-config!save.action" method="post">
    <div class="box3">
    	<div id="show" style="color: red;">&nbsp;<s:actionmessage theme="mytheme"/></div>
    	<h3><span>链路聚合配置</span></h3>
		   <%-- <input type="hidden" id="status" name="status" value=${status}/> --%>
    	<table cellpadding="0" class="table">
			<tr>
				<th width="135">名称：</th>
				<td>
					<span class="gray7">${bc.name}<input class="input" type="hidden" name="bc.name" id="name" value="${bc.name}"/></span>
				</td>
			</tr>
			<tr>
				<th width="135">负载算法：</th>
				<td>
					<s:select list="#request.algorithmList" theme="simple" name="bc.algorithm" id="algorithm" 
							headerKey="" headerValue="-选择算法-" listKey="value" listValue="display">
					</s:select>
					<span>&nbsp;<font color="#DD0099">*</font></span>
				</td>
			</tr>
			<tr>
				<th>监控间隔：</th>
				<td>
					<input class="input ipInput" type="text" name="bc.interval" id="interval" value="${bc.interval}"/>
					<%-- <input class="input ipInput" type="text" name="bc.mii" id="miiInterval" value="${bc.mii}"/> --%>
					<span class="gray7">&nbsp;毫秒</span>
				</td>
			</tr>
		</table>
		<table cellpadding="0" class="table">
			<tr>
				<th width="135">监控方式：<input type="hidden" name="bc.monitor" id="monitor" value="${bc.monitor}"/></th>									
				<td>
					<input type="radio" name="monitor" value="" checked="checked" id="mii"/>&nbsp;MII&emsp;
				</td>				
			</tr>
			<tr>
				<th></th>
				<td>
					<input type="radio" name="monitor" value="" id="arp"/>&nbsp;ARP&ensp;
				</td>
				<td>&emsp;<label class="miiGray">IP地址：</label></td>
				<td>
					<input class="input ipInput" type="text" name="ip" id="ip"/>
					<input type="hidden" name="bc.ipList" id="ips" value="${bc.ipList}"/>
					&emsp;&emsp;
					<%-- <input class="btn" type="button" name="addIp" id="addIp" onclick="interface.bond.config.addIp()" value="添加"/> --%>
					<a href="javascript:;" class="btnStyle" name="addIp" id="addIp" onclick="interface.bond.config.addIp()"><span>添加</span></a>
				</td>
			</tr>
		</table>
		<br/>
		<!--列表开始-->
		<div class="tableWrap">
			<!--表格外层左侧边框-->
			<div class="ct">
				<!--表格外层右侧边框-->
				<div class="main">
					<!--列表主要内容开始-->
					<div class="tableList">
						<table id=arpTable>
							<caption>
								<span class="tit"><strong>ARP地址列表</strong></span>
								<div id="message" style="float:left;color: red;padding-left:10px;"></div>
								<span class="operate"></span>
							</caption>
							<thead>
								<tr>
									<th width="220">IP地址</th>
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
		<br/>
		<h3><span>物理接口绑定</span></h3>		
		<table cellpadding="0" class="table">
			<tr>	
				<td width="180">选择物理接口：<br/>
					<input type="hidden" name="interForBond" id="interForBond" value="${interForBond}"/>
					<select name="interToSelect" id="interToSelect" size="5" style="width:180px">
					</select>
				</td>
				<td>
					<input class="button" type="button" name="select" id="select" value="->" onclick="interface.bond.config.select()"/><br/>
					<input class="button" type="button" name="unselect" id="unselect" value="×" onclick="interface.bond.config.unselect()"/>&emsp;
				</td>
				<td>被选择物理接口：<br/>
					<input type="hidden" name="bc.interBonded" id="interBonded" value="${bc.interBonded}"/>
					<select name="interSelected" id="interSelected" size="5" style="width:170px">
					</select>
				</td>
			</tr>
		</table>		
				
	</div>		
	    <div class="btnArea pL137">
			<button class="btn" type="submit" id="button"><span>确定</span></button>
			<a href="${ctx}/network/interface-bond!list.action" class="btnStyle btnGray"><span>&ensp;取消</span></a>
		</div>
    </form>
	      
  </div>
  <!--文本内容区结束-->
</div>
<!--主要内容结束-->

</body>
</html>