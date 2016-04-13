<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/meta.jsp" %>
<title>route-smart-config</title>
<link href="${ctx}/css/skin/default/common.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/module.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/subindex.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/messages_cn.js"></script>
<script type="text/javascript" src="${ctx}/js/network/route.smart.config.js"></script>
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
    <form id="smartConfigForm" name="smartConfigForm" action="route-smart-config!save.action" method="post">
    <div class="box3">
    <input class="input" type="hidden" name="operation" id="operation" value="${operation}" />
    <input class="input" type="hidden" name="id" id="id" value="${id}" />
    	<h3><span>智能路由配置</span></h3>
    	<div id="show" style="color: red;">&nbsp;<s:actionmessage theme="mytheme"/></div>
		<table cellpadding="0" class="table">
			<tr>
				<th width="180">描述：</th>
				<td>
					<input class="input" type="text" name="rsc.describe" id="describe" value="${rsc.describe}"/>
				</td>
			</tr>
			
			<tr>
				<th>网关策略：</th>
				<td>
					<s:select list="#request.gatePolicyList" theme="simple" name="rsc.gatePolicy" id="gatePolicy" 
							headerKey="" headerValue="-请选择-" listKey="value" listValue="display">
					</s:select>
				</td>
			</tr>
			
			<tr>
				<th width="180">ISP名称：</th>
				<td>
					<s:select list="#request.ispNameList" theme="simple" name="rsc.ispName" id="ispName" 
							headerKey="" headerValue="-默认-" listKey="value" listValue="display">
					</s:select>
				</td>
			</tr>
	<!-- -->
			</table>
		
    	
		<br/>
		<h3><span>路由网关配置</span></h3>
		<table cellpadding="0" class="table">
	 		
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
				<%-- <td><input type="radio" name="gateSet" value="" id="interSet"/>&nbsp;</td>--%>
				<td>
					<s:select list="#request.interfaceList" theme="simple" name="interface" id="interface" 
							headerKey="" headerValue="-选择接口-" listKey="value" listValue="display">
					</s:select>
				</td>
			</tr>
		</table>
		<!-- -->
		<table cellpadding="0" class="table">
			<tr>
				<th width="180">权重：</th>
				<td>
					<input class="input" type="text" name="weight" id="weight" value="1"/>
					<span class="gray7">&nbsp;[1--256]</span>
				</td>
				  
				<td>
					&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;
					<a href="javascript:;" class="btnStyle" name="addGate" id="addGate" onclick="route.smart.config.addGates()"><span>添加</span></a>
				</td>
				
			</tr>
		
		</table>
		
		<%-- --%>
		<!--列表开始-->
		<div class="tableWrap">
			<!--表格外层左侧边框-->
			<div class="ct">
				<!--表格外层右侧边框-->
				<div class="main">
					<!--列表主要内容开始-->
					<div class="tableList">
						<table id="gatesTable">
							<caption>
								<span class="tit"><strong>路由网关列表</strong></span>
								<div id="message" style="float:left;color: red;padding-left:10px;"></div>
								<span class="operate"></span>
							</caption>
							<thead>
								<tr>
									<th width="170">网关</th>
									<th width="130">接口</th>
									<th width="130">权重</th>
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
			
		<h3><span>链路检测</span></h3>
		<table cellpadding="0" class="table">
			<tr>
				<th width="180">检测方式：</th>
				<td>
				<!--  modify 2012/03/25  default value = disable
					<s:select list="#request.modeList" theme="simple" name="rsc.mode" id="mode" 
							headerKey="" headerValue="-默认-" listKey="display" listValue="value">
					</s:select>
					-->
					<s:select list="#request.modeList" theme="simple" name="rsc.mode" id="mode" 
							listKey="display" listValue="value">
					</s:select>
				</td>
			</tr>
			<tr>
				<th width="180">目标IP：</th>
				<td>
					<input class="input" type="text" name="rsc.ip" id="ip" value="${rsc.ip}"/>
				</td>
			</tr>
			<tr>
				<th width="180">目标端口：</th>
				<td>
					<input class="input" type="text" name="rsc.port" id="port" value="${rsc.port}"/>
				</td>
			</tr>
			<tr>
				<th width="180">检测频率：</th>
				<td>
					<input class="input" type="text" name="rsc.frequcency" id="frequcency" value="${rsc.frequcency}"/>
				</td>
			</tr>
			<tr>
				<th width="180">超时时间：</th>
				<td>
					<input class="input" type="text" name="rsc.timeout" id="timeout" value="${rsc.timeout}"/>
				</td>
			</tr>
		</table>
				
	</div>
	    <div class="btnArea pL137">
			<button class="btn" type="submit" id="button"><span>确定</span></button>
			<a href="${ctx}/network/route-smart!list.action" class="btnStyle btnGray"><span>&ensp;取消</span></a>
		</div>
    </form>
    
  </div>
  <!--文本内容区结束-->
</div>
<!--主要内容结束-->

</body>
</html>