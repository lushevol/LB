<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/meta.jsp" %>
<title>dns-policy</title>
<link href="${ctx}/css/skin/default/common.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/module.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/subindex.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/messages_cn.js"></script>
<script type="text/javascript" src="${ctx}/js/linkload/dns.policy.js"></script>
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
    	<span><a href="${ctx}/linkload/dns-policy!list.action"><font color="#DD0099">DNS策略</font></a></span> |    	
		<span><a href="${ctx}/linkload/dns-server!input.action">服务器设置</a></span>		
    </h4>
    <p class="vLine"></p>
    <div class="box3">
	    <h3><span>DNS策略</span></h3>
	    <br />	    
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
								<span class="tit"><strong>DNS策略列表</strong></span>
								<div id="message" style="float:left;color:red;padding-left:10px;"></div>
								<span class="operate"><a href="dns-policy-config!list.action?id=add" class="addLink popDivBt" id="add">添加</a></span>
							</caption>
							<thead>
								<tr>
									<th width="160">域名</th>
									<th width="180">别名</th>					
									<th width="200">ISP服务器</th>
									<th class="last lb2">操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="dpl" id="serviceList" status="index">
									<tr>
										<td>${name}</td>
										<td>${alise}</td>
										<td>${servers}</td>
										<%-- <td>
											<s:if test="%{status == 'true'}">开启</s:if>
											<s:else>关闭</s:else>
										</td> --%>
										<td>
											<%-- <span>
												<s:if test="%{status == 'true'}">
													<a href="javascript:;" onclick="route.isp.edit(this,{'sale':'${sale}','status':'false'})">
													关闭</a>
												</s:if>
												<s:else>
													<a href="javascript:;" onclick="route.isp.edit(this,{'sale':'${sale}','status':'true'})">
													开启</a>
												</s:else>
											</span> --%>
											
											<a href="dns-policy-config!list.action?id=${id}">设置</a>
											<span>
												<a href="javascript:;" onclick="dns.policy.del(this,{'id':'${id}'})">删除</a>
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