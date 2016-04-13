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
	<script src="${ctx}/js/nginx/kylin.klb.server.item.js" type="text/javascript"></script>
</head>
<body>
<input type="hidden" id="vsId" name="${vsId}" />
<input type="hidden" id="vsName" name="${vsName }" />
	<div id="mark"></div>
	<!--真实服务器 -->
	<div class="popLayer popDiv" id="addScheduler">
		<div class="tt">
			<h3>真实服务器 配置</h3>
			<div class="operate">
				<a href="javascript:;" class="btnClose" hider="#addScheduler" title="关闭">关闭</a>
			</div>
		</div>
		<div class="ct">
			<div class="main">
				<div class="nrarea" id="setssid">
				 
					<div class="txtCont">
						<p>真实服务器 配置说明：最大失败次数，尝试检测服务器地址有效性的最大次数。</p>
					</div>
				 
					<form id="inputForm" name="inputForm" method="post">
						<input type="hidden" id="update" name="update" value="0" />
						<input type="hidden" id="rsgId" name="rsgId" value="${rsgId}" />	
						<input type="hidden" id="serverId" name="serverId" value="" />						
						<div class="popTable">
							<div id="show" style="color: red;"></div>
							<table width="100%" cellspacing="0">
								<tr>
									<th width="110">真实服务器地址：</th>
									<td>
										<input type="text" class="input" name="ip" id="ip" />
									</td>
								</tr>
								<tr>
									<th width="110">端口：</th>
									<td>
										<input type="text" class="input" name="port" id="port" />
									</td>
								</tr>
								<tr>
									<th width="110">权重：</th>
									<td>
										<input type="text" class="input" name="weight" id="weight" value="1" />
										<span class="gray7">范围[1-255]&nbsp;&nbsp;</span>
									</td>
								</tr>
								<tr>
									<th width="110">最大失败次数：</th>
									<td>
										<input type="text" class="input" name="maxFails" id="maxFails" value="1"/>
										<span class="gray7">范围[1-255]&nbsp;&nbsp;</span>
									</td>
								</tr>
								<tr>
									<th width="110">检测间隔：</th>
									<td>
										<input type="text" class="input" name="failTimeout" id="failTimeout" value="10" />
										<span class="gray7">秒[1-255]&nbsp;&nbsp;</span>
									</td>
								</tr>
								<tr>
									<th width="110">类型：</th>
									<td>
										<s:select list="#request.typeList" theme="simple" name="type" id="type"
											listKey="display" listValue="value">
										</s:select>
									</td>
								</tr>	
								<tr>
									<th width="110">服务器标识：</th>
									<td>
										<input type="text" class="input" name="srunId" id="srunId" />
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
		
		<div class="box3"><h3><span>真实服务器组&nbsp;${rsgName}&nbsp;配置</span></h3></div>
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
								<span class="tit"><strong>真实服务器列表</strong></span>
								<div id="message" style="float: left; color: red; padding-left: 10px;"></div>
								<span class="operate"><a href="javascript:;" class="addLink popDivBt" id="add">添加</a></span>
							</caption>
							<thead>
								<tr>									
									
									<th width="110">服务器IP地址</th>																									
									<th width="80">服务器端口</th>	
									<th width="80">权重</th>		
									<th width="100">最大失败次数</th>							
									<th width="100">检测间隔</th>
									<th width="80">类型</th>
									<th width="80">服务器标识</th>
									<th class="last lb2">虚拟服务操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="serverItemList" status="count">
									<tr>
																		
										<td>${ip}</td>
										<td>${port}</td>	
										<td>${weight}</td>
										<td>${maxFails}</td>	
										<td>${failTimeout}&nbsp;(秒)</td>
										<td>${typeName}</td>	
										<td>${srunId}</td>																
										<td>
											<a href="javascript:;" class="popDivBt" onclick="kylin.klb.server.item.setss({'rsgId':'${rsgId}', 'serverId':'${serverId}'})">设置</a>
											<span><a href="javascript:;" onclick="kylin.klb.server.item.delss(this,{'rsgId':'${rsgId}', 'serverId':'${serverId}'})">删除</a></span>
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
		<div class="btnArea pL137">			
			<a href="${ctx}/nginx/nginx-real-serv.action" class="btn"><span>返回</span></a>
		</div>
		<div class="txtCont">			
		</div>
	</div>
	<!--主要内容结束-->
</body>
</html>