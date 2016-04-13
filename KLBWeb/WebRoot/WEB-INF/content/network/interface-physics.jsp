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
<script type="text/javascript" src="${ctx}/js/network/interface.physics.js"></script>
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
    <div class="box3">
	    <h3><span>物理接口</span></h3>
	</div>
	<br />
	<!--列表开始-->
	<div class="tableWrap physics">
		<!--表格外层左侧边框-->
		<div class="ct">
			<!--表格外层右侧边框-->
			<div class="main">
				<!--列表主要内容开始-->
				<div class="tableList">
					<table>
						<caption>
							<span class="tit"><strong>物理接口信息列表</strong></span>
							<input type="hidden" name="failedMess" id="failedMess" value="${failedMess}"/>
							<div id="message" style="float:left;color: red;padding-left:10px;"></div>
							<span class="operate"></span>
						</caption>
						<thead>
							<tr>
								<th width="65">接口名称</th>
								<th width="70">描述</th>
								<th width="80">接口模式</th>
								<th width="150">地址</th>
								<th width="50">MTU</th>
								<th width="50">状态</th>
								<th width="50">链接</th>
								<th width="70">协商</th>
								<th width="70">速率</th>
								<th class="last lb2">操作</th>
							</tr>
						</thead>
						<tbody>
							<s:iterator value="iil" id="interfaceList" status="count">
								<tr>
									<td>${name}</td>
									<td>${description}</td>
									<td>
										<s:if test="%{ mode == 'route' }">普通</s:if>
										<s:elseif test="%{ mode == 'dhcp' }">DHCP</s:elseif>
										<s:elseif test="%{ mode == 'adsl' }">ADSL</s:elseif>
										<s:elseif test="%{ mode == 'slave' }">从接口</s:elseif>
									</td>
									<td><s:if test="%{ mode != 'slave' }">${addr}</s:if></td>
									<td>${mtu}</td>
									<td>
										<s:if test="%{ mode != 'slave' && state == 'true' }">开启</s:if>
										<s:if test="%{ mode != 'slave' && state == 'false' }">关闭</s:if>										
									</td>
									<td>
										<s:if test="%{ state == 'true' && connect == 'false' }"><font color="#FF0000">断开</font></s:if>
										<s:if test="%{ state == 'true' && connect == 'true' }"><font color="#008000">连接</font></s:if>										
									</td>
									<td>
										<s:if test="%{ state == 'true' && negotiation == 'true' }">
											<font color="#0099DD">全双工</font></s:if>
										<s:elseif test="%{ state == 'true' && negotiation == 'false' }">半双工</s:elseif>
									</td>
									<td><s:if test="%{ state == 'true' }">${speed}
										<label class="gray6">Mbps</label></s:if>
									</td>
									<td>
										<c:if test="${ mode eq 'route' || mode eq 'dhcp' }">
											<a href="interface-physics-route!init.action?name=${name}">设置</a>
										</c:if>
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
	<%-- <h4>说明：</h4>
		<p>&emsp;物理接口。</p>--%>	       
  </div>
  <!--文本内容区结束-->
</div>
<!--主要内容结束-->

</body>
</html>
