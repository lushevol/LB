<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/meta.jsp" %>
<title>route-static-config</title>
<link href="${ctx}/css/skin/default/common.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/module.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/subindex.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/messages_cn.js"></script>
<script type="text/javascript" src="${ctx}/js/network/route.static.config.js"></script>
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
    	<span><a href="${ctx}/network/route-static!list.action"><font color="#DD0099">静态路由</font></a></span> |    	
		<span><a href="${ctx}/network/route-policy!list.action">策略路由</a></span>
		<%-- <a href="${ctx}/network/route-multi.action">多播路由</a> --%>
    </h4>
    <p class="vLine"></p>
    <form id="staticConfigForm" name="staticConfigForm" action="route-static-config!save.action" method="post">
    <div class="box3">
    	<h3><span>静态路由配置</span></h3>
    	<div id="show" style="color: red;">&nbsp;<s:actionmessage theme="mytheme"/></div>
		<input type="hidden" id="id" name="rsc.id" value=${rsc.id} />
    	<table cellpadding="0" class="table">
			<tr>
				<th width="180"><label class="setGray">目的地址(/掩码)：</label></th>
				<td>
					<input class="input autoHide" type="text" name="rsc.ip" id="ip" value="${rsc.ip}" maxlength="18"/>
					<input type="hidden" name="rsc.ipTemp" id="ipTemp" value="${rsc.ipTemp}"/>
					<label class="setGray" id="ipLabel"></label>					
				</td>
			</tr>
			<tr>
				<th width="180"><label class="setGray">Metric：</label></th>
				<td>
					<input class="input autoHide" type="text" name="rsc.metric" id="metric" value="${rsc.metric}"/>
					<input type="hidden" name="rsc.metricTemp" id="metricTemp" value="${rsc.metricTemp}"/>
					<label class="setGray" id="metricLabel"></label>
					<span class="gray7 autoHide">&nbsp;[0--65535]</span>
				</td>
			</tr>
			<tr>
				<th>描述：</th>
				<td>
					<input class="input" type="text" name="rsc.describe" id="describe" value="${rsc.describe}"/>
				</td>
			</tr>
			<tr>
				<th>网关策略：</th>
				<td>
					<s:select list="#request.gatePolicyList" theme="simple" name="rsc.gatePolicy" id="gatePolicy" 
							headerKey="" headerValue="-选择算法-" listKey="value" listValue="display">
					</s:select>
					<span class="gray7">&ensp;[仅在配置多网关时起作用]</span>
				</td>
			</tr>
		</table>
		<h3><span>路由网关配置</span></h3>
		<table cellpadding="0" class="table">
			<tr>
				<th width="180"></th>				
				<td></td>
			</tr>
			<tr>
				<th width="180"><label class="interGray">网关：</label>
				<%-- <input type="hidden" name="gateOrInter" id="gateOrInter" value="gate"/>--%></th>				
				<%-- <td><input type="radio" name="gateSet" value="" checked="checked" id="gateSet"/>&nbsp;</td> --%>
				<td>
					<input class="input" type="text" name="gate" id="gate"/>
					<input type="hidden" name="rsc.gates" id="gates" value="${rsc.gates}"/>
					<input type="hidden" name="rsc.displayGates" id="displayGates" value="${rsc.displayGates}"/>
					&emsp;
					<input type="checkbox" id="auto" name="auto" value=""/>&nbsp;自动获取网关
				</td>
			</tr>
			<tr>
				<th><label class="gateGray">接口：</label></th>
				<%-- <td><input type="radio" name="gateSet" value="" id="interSet"/>&nbsp;</td> --%>
				<td>
					<s:select list="#request.interfaceList" theme="simple" name="interface" id="interface" 
							headerKey="" headerValue="-选择接口-" listKey="value" listValue="display">
					</s:select>
				</td>
			</tr>
		</table>
		<table cellpadding="0" class="table">
			<tr>
				<th width="180">权重：</th>
				<td>
					<input class="input" type="text" name="weight" id="weight" value="1"/>
					<span class="gray7">&nbsp;[1--256]</span>&emsp;&emsp;&emsp;&emsp;
				</td>
				<td>
					<a href="javascript:;" class="btnStyle" name="addGate" id="addGate" onclick="route.static.config.addGates()"><span>添加</span></a>
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
						<table id=gatesTable>
							<caption>
								<span class="tit"><strong>路由网关列表</strong></span>
								<div id="message" style="float:left;color: red;padding-left:10px;"></div>
								<span class="operate"></span>
							</caption>
							<thead>
								<tr>
									<th width="160">网关</th>
									<th width="120">接口</th>
									<th width="120">权重</th>
									<th class="last lb2">操作</th>
								</tr>
							</thead>
							
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
				
	</div>		
	    <div class="btnArea pL137">
			<button class="btn" type="submit" id="button"><span>确定</span></button>
			<a href="${ctx}/network/route-static!list.action" class="btnStyle btnGray"><span>&ensp;取消</span></a>
		</div>
    </form>
	      
  </div>
  <!--文本内容区结束-->
</div>
<!--主要内容结束-->

</body>
</html>