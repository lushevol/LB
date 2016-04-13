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
	<script src="${ctx}/js/nginx/kylin.klb.nginx.real.serv.js" type="text/javascript"></script>
</head>
<body>
	<div id="mark"></div>
	<!--添加真实服务器组 -->
	<div class="popLayer popDiv" id="addScheduler">
		<div class="tt">
			<h3>真实服务器组配置</h3>
			<div class="operate">
				<a href="javascript:;" class="btnClose" hider="#addScheduler" title="关闭">关闭</a>
			</div>
		</div>
		<div class="ct">
			<div class="main">
				<div class="nrarea" id="setssid">
				<!-- 
					<div class="txtCont">
						<p>真实服务器组说明：ssl连接超时时间，仅在启用ssl卸载时有效。</p>
					</div>
				 -->
					<form id="inputForm" name="inputForm" method="post">
						<input type="hidden" id="update" name="update" value="0" />
						<input type="hidden" id="oldName" name="oldName" value="" />
						<input type="hidden" id="rsgId" name="rsgId" />
						<div class="popTable">
							<div id="show" style="color: red;"></div>
							<table width="100%" cellspacing="0">
								<tr>
									<th width="110">真实服务器组名称：</th>
									<td>
										<input type="text" class="input" disabled="disabled" name="name" id="name" />
									</td>
								</tr>
								<tr>
									<th>负载算法：</th>
									<td>
										<s:select list="#request.methodList" theme="simple" name="method" id="method"
											headerKey="" headerValue="-空-" listKey="display" listValue="value">
										</s:select>
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
		<div class="box3"><h3><span>真实服务器组</span></h3></div>
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
								<span class="tit"><strong>真实服务器组列表</strong></span>
								<div id="message" style="float: left; color: red; padding-left: 10px;"></div>
								<span class="operate"><a href="javascript:;" class="addLink popDivBt" id="add">添加</a></span>
							</caption>
							<thead>
								<tr>
									
									<th width="200">真实服务器组名称</th>
									<th width="200">负载算法</th>											
									<th class="last lb2">真实服务器组操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="realServerGroupList" status="count">
									<tr>
																		
										<td>${name}</td>
										<td>${methodName}</td>																			
										<td>
											<a href="javascript:;" class="popDivBt" onclick="kylin.klb.nginx.real.serv.setss({'rsgId':'${rsgId}', 'name':'${name}'})">设置</a>
											<span><a href="javascript:;" onclick="kylin.klb.nginx.real.serv.delss(this,{'rsgId':'${rsgId}'})">删除</a></span>
											<a href="server-item.action?rsgId=${rsgId}&rsgName=${name}">管理</a>
											
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