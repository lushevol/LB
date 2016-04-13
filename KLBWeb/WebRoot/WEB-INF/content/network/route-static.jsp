<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/meta.jsp" %>
<title>route-static</title>
<link href="${ctx}/css/skin/default/common.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/module.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/subindex.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/messages_cn.js"></script>
<script type="text/javascript" src="${ctx}/js/network/route.static.js"></script>
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
    <div class="box3">
	    <h3><span>静态路由表</span></h3>
	    <br />	    
		<%-- <div><b>标&nbsp;记：</b>&nbsp;U&nbsp;(Up)，G&nbsp;(Gateway specified)，L&nbsp;(Local)，C&nbsp;(Connected)，
		S&nbsp;(Static)，0&nbsp;(0spf)，R&nbsp;(Rip)，B&nbsp;(Bgp)，D&nbsp;(Dhcp)，I&nbsp;(Ipsec)，i&nbsp;(Interface specified)
    	</div> --%>
		<!--列表开始-->
		<div class="tableWrap">
			<!--表格外层左侧边框-->
			<div class="ct">
				<!--表格外层右侧边框-->
				<div class="main">
					<!--列表主要内容开始-->
					<div class="tableList">
						<table>
							<caption>
								<span class="tit"><strong>静态路由信息列表</strong></span>
								<input type="hidden" name="failedMess" id="failedMess" value="${failedMess}"/>
								<div id="message" style="float:left;color: red;padding-left:10px;"></div>
								<span class="operate"><a href="route-static-config!list.action?id=add" class="addLink popDivBt" id="add">添加</a></span>
							</caption>
							<thead>
								<tr>
									<th width="130">目的地址/掩码</th>
									<th width="90">策略</th>
									<th width="110">网关</th>
									<%-- <th width="100">标记</th> --%>
									<th width="80">接口</th>
									<th width="50">权重</th>
									<th width="50">度量值</th>
									<th width="50">状态</th>
									<th class="last lb2">操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="rsil" id="serviceList" status="index">
									<tr>
										<td>
											<s:if test="%{ip == '' && mask == ''}">未设置</s:if>
											<s:else>${ip}/${mask}</s:else>
										</td>
										<td><s:if test="%{gatePolicy == 0}">rr算法</s:if>
											<s:if test="%{gatePolicy == 1}">drr算法</s:if>
											<s:if test="%{gatePolicy == 2}">random算法</s:if>
											<s:if test="%{gatePolicy == 3}">wrandom算法</s:if>										
										</td>
										<td>${gate}</td>
										<td>${inter}</td>
										<td>${weight}</td>
										<td>${metric}</td>
										<td>
											<s:if test="%{status == 'true'}"><font color="#008000">良好</font></s:if>
											<s:else><blink><font color="#FF0000">失效</font></blink></s:else>
										</td>
										<td>
											<%-- <span>
												<s:if test="%{status == 'true'}">
													<a href="javascript:;" onclick="route.static.edit(this,{'id':'<s:property value="#index.index" />','status':'false'})">
													关闭</a>
												</s:if>
												<s:else>
													<a href="javascript:;" onclick="route.static.edit(this,{'id':'<s:property value="#index.index" />','status':'true'})">
													开启</a>
												</s:else>
											</span> --%>
											
											<a href="route-static-config!list.action?id=<s:property value="#index.index" />">设置</a>											
											<span>
												<a href="javascript:;" onclick="route.static.del(this,{'id':'<s:property value="#index.index" />'})">删除</a>
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
    </div>   
  </div>
  <!--文本内容区结束-->
</div>
<!--主要内容结束-->

</body>
</html>