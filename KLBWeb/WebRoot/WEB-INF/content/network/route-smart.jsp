<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/meta.jsp" %>
<title>route-smart</title>
<link href="${ctx}/css/skin/default/common.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/module.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/subindex.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/messages_cn.js"></script>
<script type="text/javascript" src="${ctx}/js/network/route.smart.js"></script>
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
    <div class="box3">
	    <h3><span>智能路由表</span></h3>
	</div>
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
							<span class="tit"><strong>智能路由信息列表</strong></span>
							<input type="hidden" name="failedMess" id="failedMess" value="${failedMess}"/>
							<div id="message" style="float:left;color: red;padding-left:10px;"></div>
							<span class="operate"><a href="route-smart-config!list.action?operation=add" class="addLink popDivBt" id="add">添加</a></span>
						</caption>
						<thead>
							<tr>
								<th width="40">路由ID</th>
								<th width="80">描述</th>
								<th width="80">ISP名称</th>
								<th width="110">网关</th>
								<th width="80">接口</th>
								<th width="70">权重</th>
								<th width="70">状态</th>
								<th class="last lb2">操作</th>
							</tr>
						</thead>
						<tbody>
							<s:iterator value="rsil" id="smartList" status="index">
								<tr>
									<td>${id}</td>
									<td>${describe}</td>
									<td>${isp}</td>
									<td>${gate}</td>
									<td>${inter}</td>
									<td>${weight}</td>									
									<td>
										<s:if test="%{status == 'true'}"><font color="#008000">良好</font></s:if>
										<s:else><blink><font color="#FF0000">失效</font></blink></s:else>
									</td>
									<td>											
										<a href="route-smart-config!list.action?operation=edit&id=${id}">设置</a>
										<span>
											<a href="javascript:;" onclick="route.smart.del(this,{'id':'<s:property value="#index.index" />'})">删除</a>
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
  <!--文本内容区结束-->
</div>
<!--主要内容结束-->

</body>
</html>