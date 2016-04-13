<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/meta.jsp" %>
<title>无标题文档</title>
<link href="${ctx}/css/skin/default/common.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/module.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
<script type="text/javascript" src="${ctx}/js/kylin.klb.sysinfo.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	//进度条
	kylin.klb.sysinfo.process("#process_cpu",${cpuUsage});
	kylin.klb.sysinfo.process("#process_mem",${memUsage});
	kylin.klb.sysinfo.process("#process_hd",${hdUsage});
	//刷新
	/*$("#refresh").click(function() {
		location.reload();
	});*/
});
</script>
</head>
	<body>
		<!--面板开始-->
		<div class="mainPartPanel">
			<div class="circle"></div>
			&nbsp;
		</div>
		<!--面板结束-->
		<!--主要内容开始-->
		<div class="mainPartContNoTop">
			<!--圆角模块开始-->
			<div class="box1">
				<!--圆角模块头部开始-->
				<div class="tt">
					<h3>本调度器系统状态</h3>
					<div class="operate"><a href="klb-sys-info.action" class="refresh" id="refresh">刷新</a></div>
				</div>
				<!--圆角模块头部结束-->
				<!--圆角模块中间内容左侧边框-->
				<div class="ct">
					<!--圆角模块中间内容右侧边框-->
					<div class="main" id="sysinfoid">
						<ul class="hardwareList">
							<li>
								<div class="cpu">
									<h4>CPU</h4>
									<p class="capacity">
										<span id="process_cpu"></span>
									</p>
									<p>CPU核数：${cpuNum}</p>
									<p>频率：${cpuSpeed}Mhz</p>
									<p>
										利用率：
										<em id="process_cpu_text"></em>%
									</p>
								</div>
							</li>
							<li>
								<div class="memory">
									<h4>内存</h4>
									<p class="capacity">
										<span id="process_mem"></span>
									</p>
									<p>总量：${memTotal}M</p>
									<p>空闲：${memFree}M</p>
									<p>已使用：${memUsed}M</p>
									<p>
										利用率：
										<em id="process_mem_text"></em>%
									</p>
								</div>
							</li>
							<li class="last">
								<div class="hardDisk">
									<h4>硬盘</h4>
									<p class="capacity">
										<span id="process_hd"></span>
									</p>
									<p>总量：${hdTotal}M</p>
									<p>已使用：${hdUsed}M</p>
									<p>
										利用率：
										<em id="process_hd_text"></em>%
									<p>
								</div>
							</li>
						</ul>
					</div>
				</div>
				<!--圆角模块底部开始-->
				<div class="bt">
					<p></p>
					<div></div>
				</div>
				<!--圆角模块底部结束-->
			</div>
			<!--圆角模块结束-->
			
			<c:if test="${not empty director}"></c:if>
				<!--圆角模块开始-->
				<div class="box1">
					<!--圆角模块头部开始-->
					<div class="tt">
						<h3>调度器转发状态</h3>
						<div class="operate"></div>
					</div>
					<!--圆角模块头部结束-->
					
					<s:iterator value="directors">
						<!--圆角模块中间内容左侧边框-->
						<div class="ct">
							<!--圆角模块中间内容右侧边框-->
							<div class="main" style="padding:0 3px 0 1px">
								<!--列表主要内容开始-->
								<div class="tableList">
									<table>
										<thead>
											<tr>
												<%-- <th width="70">协议：${localProt}</th> --%>
												<th width="425">服务：${localAddress}</th>
												<th width="245">调度策略：${localScheduler}</th>
												<th width="130">连接保持：
													<c:if test="${not empty localPersistent}">${localPersistent}秒</c:if>
													<c:if test="${empty localPersistent}">无</c:if>
												</th>
											</tr>
										</thead>
									</table>
								</div>
								<div class="tableList">
									<table>
										<thead>
											<tr>
												<th width="120">真实服务器地址</th>
												<th width="120">转发方式</th>
												<th width="120">权重</th>
												<th width="120">活动连接数</th>
												<th width="120">非活动连接数</th>
											</tr>
										</thead>
										<tbody>
											<s:iterator value="remotes">
												<tr>
													<td>${remoteAddress}</td>
													<td>${remoteForward}</td>
													<td>${remoteWeight}</td>
													<td>${remoteActiveConn}</td>
													<td>${remoteInActConn}</td>
												</tr>
											</s:iterator>
										</tbody>
									</table>
								</div>
								<!--列表主要内容结束-->
							</div>
						</div>
						<div class="ct">
							<div class="main" style="padding:0 3px 0 1px">
								<div class="tableList">
									<br/>
									<hr size="1" color="#87CEFA" noshade />
								</div>
							</div>
						</div>
					</s:iterator>
					
					<!--圆角模块底部开始-->
					<div class="bt">
						<p></p>
						<div></div>
					</div>
					<!--圆角模块底部结束-->
				</div>
				<!--圆角模块结束-->
						
			<%--<div class="box1">
				<iframe src="klb-sys-info!input.action?isframe=1" name="iframe" 
					width="763" height="200" frameborder="0" scrolling="no" 
					marginheight="0" marginwidth="0" style="border-style: none #ffffff solid"></iframe>
			</div>
		--%></div>
		<!--主要内容结束-->
	</body>
</html>
