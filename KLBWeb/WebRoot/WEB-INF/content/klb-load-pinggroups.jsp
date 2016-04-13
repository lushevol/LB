<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>无标题文档</title>
<%@ include file="/common/meta.jsp"%>
<link rel="stylesheet" type="text/css" href="${ctx}/css/skin/default/common.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/skin/default/module.css" />
<script type="text/javascript" src="${ctx}/js/jquery.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/messages_cn.js"></script>
<script type="text/javascript" src="${ctx}/js/kylin.klb.common.js"></script>
<script type="text/javascript" src="${ctx}/js/klb.load.pinggroups.js"></script>
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
	<!--文本内容区开始-->
	<div class="txtCont">
		<div id="show" style="color: red;">&nbsp;<s:actionmessage theme="mytheme"/></div>				
		<div class="box3">
			<h3><span>高可用检测</span></h3>
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
								<span class="tit"><strong>域名信息列表</strong></span>
								<div align="left" id="message" style="float: left; color: red; padding-left: 10px;"></div>
								<span class="operate"><a href="####" class="addLink popDivBt" onclick="popbox('#firstadd')"
									pop="#firstadd" id="add">添加</a></span>
							</caption>
							<thead>
								<tr>
									<th width="300">域名地址</th>
									<th class="last lb2">操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="list" id="scheduler" status="index">
									<tr class="schedulerNum">
										<td>
											<s:property value="%{firstadd.startsWith('_')?firstadd.substring(1,firstadd.length()):firstadd}" />
										</td>
										<td>
											<a href="klb-load-pinggroups!delete.action?firstadd=${firstadd}"
												onclick="if(confirm('确实要删除吗？')==false) return false;">删除</a>
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

		<%-- 		
		<div class="box3">
			<h3><span>检测组二</span></h3>
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
								<span class="tit"><strong>域名信息列表</strong></span>
								<div align="left" id="message" style="float: left; color: red; padding-left: 10px;"></div>
								<span class="operate"><a href="####" class="addLink popDivBt" onclick="popbox('#secondadd')"
									pop="#secondadd" id="add">添加</a></span>
							</caption>
							<thead>
								<tr>
									<th width="220">域名地址</th>
									<th class="last lb2">操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="list1" id="scheduler" status="index">
									<tr class="schedulerNum">
										<td>
											<s:property value="%{secondadd.startsWith('_')?secondadd.substring(1,secondadd.length()):secondadd}" />
										</td>
										<td>
											<a href="klb-load-pinggroups!deletesecond.action?secondadd=${secondadd}"
												onclick="if(confirm('你确定要删除吗？')==false) return false;">删除</a>
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
		--%>
		<div class="txtCont">
			<h4>说明：</h4>
			<p>
				&emsp;负载均衡服务器通过与一个或多个其他域名保持连接，来检测自身是否正常运行。一般会选择填写国内比较稳定的域名，如：www.163.com。
				当所有域名都无法访问时，负载均衡服务器会进行高可用切换。
			</p>
		</div>
	</div>
	<!--文本内容区结束-->
</div>
<!--主要内容结束-->

	<!--弹出层警告开始-->
	<!--调度器添加 -->
	<div id="mark"></div>
	<div class="popLayer popDiv" id="firstadd">
		<div class="tt">
			<h3>域名添加</h3>
			<div class="operate"><a href="####" class="btnClose" hider="#firstadd" title="关闭">关闭</a></div>
		</div>

		<div class="ct">
			<div class="main">
				<div class="nrarea">
					<form id="inputForm" name="inputForm" action="klb-load-pinggroups!save.action" method="post">
						<div class="popTable">
							<div id="show1" style="color: red;"></div>
							<table width="100%" cellspacing="0">
								<tr>
									<th width="106">域名地址：</th>
									<td>
										<input id="firstadd" type="text" class="input" name="firstadd" />
									</td>
								</tr>
							</table>
						</div>

						<div class="btnArea btnConfirm">
							<button class="btn" type="submit" id="submitId"><span>保存</span></button>
							<button type="reset" class="btn btnGray" hider="#firstadd"><span>取消</span></button>
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
	<!--弹出层警告结束-->
			
	<%-- 
	<!--弹出层警告开始-->
	<!--调度器添加 -->
	<div id="mark"></div>
	<div class="popLayer popDiv" id="secondadd">
		<div class="tt">
			<h3>域名添加</h3>
			<div class="operate"><a href="####" class="btnClose" hider="#secondadd" title="关闭">关闭</a></div>
		</div>

		<div class="ct">
			<div class="main">
				<div class="nrarea">
					<form id="inputForm" name="inputForm" action="klb-load-pinggroups!savesecondadd.action" method="post">
						<div class="popTable">
							<div id="show" style="color: red;"></div>
							<table width="100%" cellspacing="0">
								<tr>
									<th width="106">域名地址：</th>
									<td>
										<input id="secondadd" type="text" class="input" name="secondadd" />
									</td>
								</tr>
							</table>
						</div>

						<div class="btnArea btnConfirm">
							<button class="btn" type="submit" id="submitId"><span>保存</span></button>
							<button type="reset" class="btn btnGray" onclick="reset1();"><span>重置</span></button>
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
	<!--弹出层警告结束-->
	--%>		
</body>
</html>

