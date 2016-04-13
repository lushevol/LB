<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>无标题文档</title>
	<%@ include file="/common/meta.jsp"%>
	<link rel="stylesheet" type="text/css" href="${ctx}/css/skin/default/common.css" />
	<link rel="stylesheet" type="text/css" href="${ctx}/css/skin/default/module.css" />
	<link rel="stylesheet" type="text/css" href="${ctx}/css/skin/default/subindex.css" />
	<script src="${ctx}/js/jquery-1.3.2.pack.js" type="text/javascript"></script>
	<script src="${ctx}/js/validate/jquery.validate.js" type="text/javascript"></script>
	<script src="${ctx}/js/validate/messages_cn.js" type="text/javascript"></script>
	<script src="${ctx}/js/kylin.klb.sirtualserv.js" type="text/javascript"></script>
</head>
<body>
	<div id="mark"></div>
	<!--添加虚拟服务 -->
	<div class="popLayer popDiv" id="addScheduler">
		<div class="tt">
			<h3>虚拟服务配置</h3>
			<div class="operate">
				<a href="javascript:;" class="btnClose" hider="#addScheduler" title="关闭">关闭</a>
			</div>
		</div>
		<div class="ct">
			<div class="main">
				<div class="nrarea" id="setssid">
					<div class="txtCont">
						<p>虚拟服务说明：tcp和udp有多个端口时，用半角逗号分隔。</p>
					</div>
					<form id="inputForm" name="inputForm" method="post">
						<input type="hidden" id="update" name="update" value="0" />
						<input type="hidden" id="oldService" name="oldService" value="" />
						<input type="hidden" id="vsId" name="vsId" />
						<div class="popTable">
							<div id="show" style="color: red;"></div>
							<table width="100%" cellspacing="0">
								<tr>
									<th width="110">虚拟服务名称：</th>
									<td>
										<input type="text" class="input" disabled="disabled" name="service" id="service" />
									</td>
								</tr>
 <s:if test="#session.isHaEnable.equals('true')">
								<tr>
									<th>高可用类型：</th>
									<td>
										<s:select list="#request.haTypeList" theme="simple" name="haType" id="haType" headerKey=""
											headerValue="-空-" listKey="value" listValue="display">
										</s:select>
										<%-- <s:select list="#request.schedulerList" theme="simple" name="scheduler" id="scheduler" headerKey=""
											headerValue="请选择" listKey="schedulerName" listValue="schedulerName.startsWith('_')?
			              					schedulerName.substring(1,schedulerName.length()):schedulerName">
										</s:select> --%>
									</td> 
								</tr>
 </s:if>
 <s:else>
 	<input type="hidden" name="haType" id="haType" value="0"/>
 </s:else>
								<tr>
									<th>接&nbsp;&nbsp;&nbsp;&nbsp;口：</th>

									<td>
 <s:if test="#session.isSoftVersion.equals('false')">
										<s:select list="#request.interfacesList" theme="simple" name="interfaces" id="interfaces" headerKey=""
											headerValue="-空-" listKey="value" listValue="display"></s:select>
	 </s:if>
 <s:else>
 	<input type="text" name="interfaces" id="interfaces" class="input fno"/>
 </s:else>
									</td>
								</tr>
								<tr>
									<th>虚拟服务地址：</th>
									<td>
										<input type="text" class="input fno" name="vipMark" id="vipMark" />
									</td>
								</tr>
								<tr>
									<th>协议/端口：</th>
									<td class="disabled" id="tcptd">
										<input type="checkbox" id="tcp" name="tcp" value="tcpPorts" />
										<label>TCP端口</label>
										<input name="tcpPorts" disabled="disabled" type="text" class="input fno w90" id="tcpPorts"
											onblur="javascript:this.value=this.value.replace(/，/ig,',');" />
									</td>
								</tr>
								<tr>
									<th></th>
									<td class="disabled" id="udptd">
										<input type="checkbox" name="udp" value="udpPorts" id="udp" />
										<label>UDP端口</label>
										<input type="text" class="input fno w90" disabled="disabled" name="udpPorts" id="udpPorts"
											onblur="javascript:this.value=this.value.replace(/，/ig,',');" />
									</td>
								</tr>
 <s:if test="#session.isSoftVersion.equals('false')">								
								<tr>
									<th>流量控制：</th>
									<td><label class="gray6">最大上行流量</label>
										<input type="text" class="input fno w80" name="trafficUp" id="trafficUp" />
										<label class="gray6">kbps</label>
									</td>
								</tr>
								<tr>
									<th></th>
									<td><label class="gray6">最大下行流量</label>
										<input type="text" class="input fno w80" name="trafficDown" id="trafficDown" />
										<label class="gray6">kbps</label>
									</td>
								</tr>
</s:if>
 <s:else>
 	<input type="hidden" name="trafficUp" id="trafficUp" value="0"/>
 	<input type="hidden" name="trafficDown" id="trafficDown" value="0"/>
 </s:else>								
								<tr>
								<th>调度算法：</th>
									<td>
										<s:select list="#request.schedulings" theme="simple" name="scheduling" id="scheduling"
											headerKey="" headerValue="-空-" listKey="key" listValue="value">
										</s:select>
									</td>
								</tr>
								<tr>
									<th>连接保持：</th>
									<td>
										<input type="text" class="input w44" name="persistentTimeout"
											id="persistentTimeout" value="${persistentTimeout}" />秒										
										<span class="gray7">&nbsp;[0或60~86400]</span>
										<span class="note"></span>
									</td>
								</tr>
								<tr>
									<th>连接保持网络掩码：</th>
									<td>
										<input type="text" class="input" name="persistentNetmask"
											id="persistentNetmask" value="${persistentNetmask}" />
									</td>
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
		<div class="box3"><h3><span>虚拟服务</span></h3></div>
		<br/>
		<!--列表开始-->
		<div class="tableWrap sirtualServ">
			<!--表格外层左侧边框-->
			<div class="ct">
				<!--表格外层右侧边框-->
				<div class="main">
					<!--列表主要内容开始-->
					<div class="tableList vTopTd">
						<table>
							<caption>
								<span class="tit"><strong>虚拟服务列表</strong></span>
								<div id="message" style="float: left; color: red; padding-left: 10px;"></div>
								<span class="operate"><a href="javascript:;" class="addLink popDivBt" id="add">添加</a></span>
							</caption>
							<thead>
								<tr>
<s:if test="#session.isHaEnable.equals('true')">
									<th width="70">高可用类型</th>
</s:if>									
									<th width="80">虚拟服务名称</th>
									<th width="110">虚拟服务地址</th>									
									<th width="170">虚拟服务协议 / 端口</th>	
<s:if test="#session.isSoftVersion.equals('false')">																	
									<th width="100">流量控制</th>
 </s:if>									
									<th width="110">真实服务器地址</th>
									<th width="30">状态</th>
									<th class="last lb2">虚拟服务操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="sirtualServ" status="count">
									<tr>
 <s:if test="#session.isHaEnable.equals('true')">								
										<td>
											<s:if test="%{haType == 0}">本地</s:if>
											<s:if test="%{haType == 1}">本机服务</s:if>
											<s:if test="%{haType == 2}">另外一台</s:if>
										</td>
 </s:if>																				
										<td>${service}
											<%-- <s:property value="%{service.startsWith('_')?service.substring(1, service.length()):service}" />--%>
										</td>
										<td>${vipMark}</td>
										<td>${protocolAndPort}</td>	
<s:if test="#session.isSoftVersion.equals('false')">																			
										<td>${trafficUp}/${trafficDown}
											<label class="gray6">kbps</label>
										</td>
 </s:if>										
										<td>
											<c:forEach var="lz" items="${realIp}">
												${lz}<br /></c:forEach>
										</td>
										<td class="stc">
											<s:if test="%{status == 'true'}"><font color="#008000">开启</font></s:if>
											<s:else><font color="#FF0000">关闭</font></s:else>
										</td>
										<td>
											<span><a href="javascript:;" onclick="kylin.klb.sirtualserv.startss(this,{'vsId':'${vsId}'})">
												<s:if test="%{status == 'true'}">关闭</s:if> <s:else>开启</s:else></a>
											</span>
											<a href="javascript:;" class="popDivBt" onclick="kylin.klb.sirtualserv.setss({'vsId':'${vsId}', 'service':'${service}'})">设置</a>
											<span><a href="javascript:;" onclick="kylin.klb.sirtualserv.delss(this,{'vsId':'${vsId}'})">删除</a></span>
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
		<!--列表结束-->
		<div class="txtCont">			
		</div>
	</div>
	<!--主要内容结束-->
</body>
</html>