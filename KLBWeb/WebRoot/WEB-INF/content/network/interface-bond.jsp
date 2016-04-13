<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/meta.jsp" %>
<title>interface-bond</title>
<link href="${ctx}/css/skin/default/common.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/module.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/subindex.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/messages_cn.js"></script>
<script type="text/javascript" src="${ctx}/js/network/interface.bond.js"></script>
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
    <div class="box3">
	    <h3><span>链路聚合</span></h3>
	</div>
	<br />
		
	<!--列表开始-->
	<div class="tableWrap bond">
		<!--表格外层左侧边框-->
		<div class="ct">
			<!--表格外层右侧边框-->
			<div class="main">
				<!--列表主要内容开始-->
				<div class="tableList">
					<table>
						<caption>
							<span class="tit"><strong>链路聚合信息列表</strong></span>
							<input type="hidden" name="failedMess" id="failedMess" value="${failedMess}"/>
							<div id="message" style="float:left;color: red;padding-left:10px;"></div>
							<span class="operate"><a href="javascript:;" class="addLink popDivBt" id="add">添加</a></span>
						</caption>
						<thead>
							<tr>
								<th width="55">名称</th>
								<th width="90">负载算法</th>
								<th width="55">监控方式</th>
								<th width="140">物理接口</th>
								<th width="55">接口模式</th>
								<th width="130">地址</th>								
								<th width="40">状态</th>
								<th width="55">聚合设置</th>
								<th width="55">接口属性</th>
								<th class="last lb2">操作</th>
							</tr>
						</thead>
						<tbody>
							<s:iterator value="bondInfoList" id="bondList" status="index">
								<tr>
									<td>${name}</td>
									<td>
										<s:if test="%{algorithm == 0}">轮询模式</s:if>
										<s:if test="%{algorithm == 1}">主备模式</s:if>
										<s:if test="%{algorithm == 2}">IP地址异或</s:if>
										<s:if test="%{algorithm == 3}">广播模式</s:if>
										<s:if test="%{algorithm == 4}">支持802.3ad</s:if>
										<s:if test="%{algorithm == 5}">tlb平衡模式</s:if>
										<s:if test="%{algorithm == 6}">alb平衡模式</s:if>
									</td>
									<td>
										<s:if test="%{monitor == 0}">MII</s:if>
										<s:if test="%{monitor == 1}">ARP</s:if>
									</td>
									<td>${interBonded}</td>
									<td>
										<s:if test="%{interMode == 'route'}">普通</s:if>
										<s:else>DHCP</s:else>
									</td>
									<td>${ipAddr}</td>
									<td>
										<s:if test="%{state == 'true'}">开启</s:if>										
										<s:else>关闭</s:else>
									</td>
									<td>
										<c:if test="${ interMode eq 'route' || interMode eq 'dhcp' }">
											<a href="interface-bond-config!init.action?name=${name}">设置</a>
										</c:if>
									</td>
									<td>
										<a href="interface-bond-attribute!init.action?name=${name}">修改</a>
									</td>
									<td>											
										<span>
											<a href="javascript:;" onclick="interface.bond.del(this,{'bondName':'${name}'})">删除</a>
										</span>
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
	<%-- <h4>链路聚合说明：</h4>
		<p>&emsp;链路聚合。</p> --%>
    
  </div>
  <!--文本内容区结束-->
</div>
<!--主要内容结束-->

<!--弹出层警告开始-->
<!--添加聚合接口 -->
<div id="mark"></div>
<div class="popLayer popDiv" id="addBond">
	<div class="tt">
		<h3>添加聚合接口</h3>
		<div class="operate">
			<a href="javascript:;" class="btnClose" hider="#addBond" title="关闭">关闭</a>
		</div>
	</div>
	
	<div class="ct">
		<div class="main">
			<div class="nrarea">
				<form id="inputForm" name="inputForm" method="post">	       			
	       			<input type="hidden" id="operation" name="operation" value="add"/>
					<div class="popTable">
						<div id="show" style="color: red;"></div>
						<table width="100%" cellspacing="0">							
							<tr>
								<th width="106">聚合接口名称：</th>
								<td>											
									<input class="input" type="text" name="name" id="name"/>
									<span class="gray7">&nbsp;[格式为bondX（X为0,1,2,...）]</span>
								</td>
							</tr>												
						</table>
					</div>
	
					<div class="btnArea btnConfirm">
						<button class="btn" type="submit" id="save_bond"><span>保存</span></button>
	          			<button class="btn btnGray" type="reset" hider="#addBond"><span>取消</span></button>
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
<!--弹出层结束-->

</body>
</html>