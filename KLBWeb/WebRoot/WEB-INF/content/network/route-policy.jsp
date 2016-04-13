<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/meta.jsp" %>
<title>route-policy</title>
<link href="${ctx}/css/skin/default/common.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/module.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/subindex.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/messages_cn.js"></script>
<script type="text/javascript" src="${ctx}/js/network/route.policy.js"></script>
</head>
<body>
<!--移动弹出层开始-->
<div id="mark"></div>
<div class="popLayer popDiv" id="addScheduler">
	<div class="tt">
		<h3>移动</h3>
		<div class="operate">
			<a href="javascript:;" class="btnClose" hider="#addScheduler" title="关闭">关闭</a>
		</div>
	</div>

	<div class="ct">
		<div class="main">
			<div class="nrarea">
				<form id="inputForm" name="inputForm" method="post">
       				<input type="hidden" id="oldId" name="oldId" value=""/>
       					
					<div class="popTable">
						<div id="show" style="color: red;"></div>
						<table width="100%" cellspacing="0">
							<tr>
								<th width="106">移动至第&nbsp;</th>
								<td><input type="text" class="input" name="row" id="row" />行</td>
							</tr>
						</table>
					</div>

					<div class="btnArea btnConfirm">
						<button class="btn" type="submit" id="save_scheduler"><span>确定</span></button>
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
<!--移动弹出层结束-->

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
    	<span><a href="${ctx}/network/route-static!list.action">静态路由</a></span> |    	
		<span><a href="${ctx}/network/route-policy!list.action"><font color="#DD0099">策略路由</font></a></span>
		<%-- <a href="${ctx}/network/route-multi.action">多播路由</a> --%>
    </h4>
    <p class="vLine"></p>
    <div class="box3">
	    <h3><span>策略路由表</span></h3>
	</div>
	    <br />
	<div class="policy">    
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
								<span class="tit"><strong>策略路由信息列表</strong></span>
								<input type="hidden" name="failedMess" id="failedMess" value="${failedMess}"/>
								<div id="message" style="float:left;color: red;padding-left:10px;"></div>
								<span class="operate"><a href="route-policy-config!list.action?operation=add" class="addLink popDivBt" id="add">添加</a></span>
							</caption>
							<thead>
								<tr>
									<th width="37">路由ID</th>
									<th width="110">源地址/掩码</th>
									<th width="110">目的地址/掩码</th>
									<th width="50">协议</th>
									<th width="45">源端口</th>
									<th width="50">目的端口</th>									
									<th width="100">网关</th>
									<th width="50">接口</th>
									<th width="40">权重</th>
									<%-- <th width="35">移动</th> --%>
									<th width="35">插入</th>
									<th width="35">状态</th>
									<th class="last lb2">操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="rpil" id="serviceList" status="index">
									<tr>
										<td>${id}</td>
										<td>${srcNet}</td>										
										<td>${destNet}</td>
										<td>${protocol}</td>
										<td>${srcPort}</td>
										<td>${destPort}</td>																	
										<td>${gate}</td>
										<td>${inter}</td>
										<td>${weight}</td>
										<%-- <td><a href="javascript:;" class="move" onclick="route.policy.recordOldId({'id':'${id}'})">
											<img src="${ctx}/css/skin/images/common/ico_move.gif" alt="移动" title="移动" /></a></td>--%>
										<td><a href="route-policy-config!list.action?operation=insert&id=${id}">
											<img src="${ctx}/css/skin/images/common/ico_insert.gif" alt="插入" title="插入" /></a></td>
										<td>
											<s:if test="%{status == 'true'}"><font color="#008000">良好</font></s:if>
											<s:else><blink><font color="#FF0000">失效</font></blink></s:else>
										</td>
										<td>
											<%-- <span>
												<s:if test="%{status == 'true'}">
													<a href="javascript:;" onclick="route.policy.edit(this,{'id':'<s:property value="#index.index" />','status':'false'})">
													关闭</a>
												</s:if>
												<s:else>
													<a href="javascript:;" onclick="route.policy.edit(this,{'id':'<s:property value="#index.index" />','status':'true'})">
													开启</a>
												</s:else>
											</span> --%>
											
											<a href="route-policy-config!list.action?operation=edit&id=${id}">设置</a>
											<span>
												<a href="javascript:;" onclick="route.policy.del(this,{'id':'<s:property value="#index.index" />'})">删除</a>
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