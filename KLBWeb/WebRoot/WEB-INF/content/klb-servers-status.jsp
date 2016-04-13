<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>无标题文档</title>
	<%@ include file="/common/meta.jsp"%>
	<link href="${ctx}/css/skin/default/common.css" rel="stylesheet" type="text/css" />
	<link href="${ctx}/css/skin/default/module.css" rel="stylesheet" type="text/css" />
	<link href="${ctx}/css/skin/default/subindex.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
	<script type="text/javascript" src="${ctx}/js/validate/jquery.validate.js"></script>
	<script type="text/javascript" src="${ctx}/js/validate/messages_cn.js"></script>
	<script type="text/javascript" src="${ctx}/js/kylin.klb.servers.status.js" charset="utf-8"></script>
</head>
<body>
	<div id="mark"></div>
	<!--设置检测方式 -->
	<div class="popLayer popDiv" id="addScheduler">
		<div class="tt">
			<h3>设置检测方式</h3>
			<div class="operate">
				<a href="javascript:;" class="btnClose" hider="#addScheduler" title="关闭">关闭</a>
			</div>
		</div>
		<div class="ct">
			<div class="main">
				<div class="nrarea">
					<div class="txtCont">
						<p>检测方式说明：只有选择了端口检测方式后，端口参数才会生效。</p>
					</div>
					<form id="inputForm" name="inputForm" method="post">
						<input type="hidden" id="update" name="update" value="0" />
						<input type="hidden" id="oldServiceName" name="oldServiceName" value="" />
						<input type="hidden" id="vsId" name="vsId" />

						<div class="popTable">
							<div id="show" style="color: red;"></div>
							<table width="100%" cellspacing="0">
								<tr id="sirtualService">
									<th><font color="#777">虚拟服务：</font></th>
									<td><label id="serviceName"></label>
									<%-- <input type="text" class="input" name="serviceName" id="serviceName" /> --%>
									</td>
								</tr>
								<tr>
									<th>检测间隔：</th>
									<td><input type="text" class="input" name="interval" id="interval" />
										<span class="gray7">&nbsp;[1~360]</span>
									</td>
								</tr>
								<tr>
									<th>超时时间：</th>
									<td><input type="text" class="input" name="timeout" id="timeout" />
										<span class="gray7">&nbsp;[1~360]</span>
									</td>
								</tr>
								<tr>
									<th>重试次数：</th>
									<td><input type="text" class="input" name="retry" id="retry" />
										<span class="gray7">&nbsp;[1~16]</span>
									</td>
								</tr>								
								<tr>
									<th>检测方式：</th>
									<td>
										<s:select list="typeList" theme="simple" name="type" id="type"
											listKey="value" listValue="display">
										</s:select>
									</td>
								</tr>
								<tr>
									<th>检测端口：</th>
									<td><input type="text" class="input" name="port" id="port" /></td>
								</tr>
								<tr>
									<th>邮件报警：</th>
									<td>&nbsp;		
									   <input type="radio" name="enabled" value="true" checked="checked" id="set1"/>&nbsp;启用&emsp;<input type="radio" name="enabled" value="false" id="set2"/>&nbsp;关闭
									</td>
								</tr>
								<tr>
									<th>接收人地址：</th>
									<td><input type="text" class="input" name="mail" id="mail" /></td>
								</tr>
								<tr>
									<th>发送间隔：</th>
									<td><input type="text" class="input" name="date" id="date" /></td>
								</tr>
							</table>
						</div>
						<div class="btnArea btnConfirm">
							<button class="btn" type="submit" id="save_scheduler"><span>保存</span></button>
							<button class="btn btnGray" type="reset" hider="#addScheduler"><span>取消</span></button>
						</div>
					</form>
				</div>
			</div>
		</div>
		<div class="bt">
			<p></p>
			<div></div>
		</div>
	</div>
	<!--面板开始-->
	<div class="mainPartPanel">
		<div class="circle"></div>
		&nbsp;
	</div>
	<!--面板结束-->
	<!--主要内容开始-->
	<div class="mainPartContNoTop">
		<div class="box3"><h3><span>检测方式配置</span></h3></div>
		<br/>
		<!--列表开始-->
		<div class="tableWrap">
			<!--表格外层左侧边框-->
			<div class="ct">
				<!--表格外层右侧边框-->
				<div class="main">
					<!--列表主要内容开始-->
					<div class="tableList vTopTd">
						<table>
							<caption>
								<span class="tit"><strong>检测方式列表</strong> </span>
								<div id="message" style="float: left; color: red; padding-left: 10px;"></div>
								<span class="operate"></span>
							</caption>
							<thead>
								<tr>
									<th width="90">虚拟服务</th>
									<th width="90">检测间隔</th>
									<th width="90">超时时间</th>
									<th width="90">重试次数</th>
									<th width="90">检测方式</th>
									<th width="90">检测端口</th>
									<th width="90">邮件警报</th>
									<th class="last lb2">检测操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="monitors" status="count">
									<tr>
										<td>${serviceName}</td>
										<td>${interval}</td>
										<td>${timeout}</td>
										<td>${retry}</td>										
										<td>${type}</td>									
										<td><s:if test="%{type != 'ping检测'}">${port}</s:if></td>
										<td><s:if test="%{enabled == 'true'}">开启</s:if><s:else>关闭</s:else></td>
										<td>
											<a href="javascript:;" class="popDivBt"
												onclick="kylin.klb.servers.status.setss({'vsId':'${vsId}', 'serviceName':'${serviceName}'})">设置</a>
										</td>
									</tr>
								</s:iterator>
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
	
		<div class="box3"><h3><span>真实服务器状态</span></h3></div>
		<br/>
		<!--列表开始-->
		<div class="tableWrap">
			<!--表格外层左侧边框-->
			<div class="ct">
				<!--表格外层右侧边框-->
				<div class="main">
					<!--列表主要内容开始-->
					<div class="tableList vTopTd">
						<table>
							<caption>
								<span class="tit"><strong>服务器状态列表</strong> </span>
								<div id="message" style="float: left; color: red; padding-left: 10px;"></div>
								<span class="operate"><a href="klb-servers-status.action" class="refresh popDivBt" id="add">刷新</a> </span>
							</caption>
							<thead>
								<tr>
									<th width="180">真实服务器名称</th>
									<th width="180">真实服务器地址</th>
									<th width="200">虚拟服务名称 / 地址</th>
									<th class="last lb2">真实服务器状态</th>
								</tr>
							</thead>
						</table>
					</div>
					<div class="tableList">
						<table>
							<tbody>
								<s:iterator value="servers" status="count">
									<s:iterator value="virtualServices" status="count">
										<tr>
											<td width="180">${realServerName}</td>
											<td width="180">${ip}</td>											
											<td width="200">${virtualServiceIpName}</td>
											<td class="stc">
												<s:if test="%{status == 'down'}"><blink><font color="#FF0000">失效</font></blink></s:if>
												<s:elseif test="%{status == 'up'}"><font color="#008000">良好</font></s:elseif>
												<s:else>关闭</s:else>
											</td>
										</tr>
									</s:iterator>
								</s:iterator>
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
		<div class="txtCont">
    	<h4>说明：</h4>
    	<p>&emsp;当对应的虚拟服务开启，且检测方式选择为关闭后，系统认为所有服务器都是良好状态。</p>
  		</div>
	</div>
	<!--主要内容结束-->
</body>
</html>
