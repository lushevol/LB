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
<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
<script src="${ctx}/js/validate/jquery.validate.js" type="text/javascript"></script>
<script src="${ctx}/js/validate/messages_cn.js" type="text/javascript"></script>
<script src="${ctx}/js/kylin.klb.access.js" type="text/javascript"></script>
</head>
<body>
	<!--弹出层警告开始-->
	<!--访问控制添加 -->
	<div id="mark"></div>
	<div class="popLayer popDiv" id="addScheduler">
		<div class="tt">
			<h3>访问控制规则</h3>
			<div class="operate">
				<a href="javascript:;" class="btnClose" hider="#addScheduler" title="关闭">关闭</a>
			</div>
		</div>

		<div class="ct">
			<div class="main">
				<div class="nrarea">
					<form id="inputForm" name="inputForm" method="post">
						<input type="hidden" id="update" name="update" value="0"/>      					
       					<input type="hidden" name="id" id="id" value=""/>
       					
						<div class="popTable">
							<div id="show" style="color: red;"></div>
							<table width="100%" cellspacing="0">
								<tr>
									<th width="106">源地址(/掩码)：</th>
									<td>
										<input type="text" class="input" id="srcNet" name="srcNet" value=""/>					
									</td>
								</tr>
								<tr>
									<th>目的地址(/掩码)：</th>
									<td>
										<input type="text" class="input" id="destNet" name="destNet" value=""/>
									</td>
								</tr>
								<tr>
									<th>协议：</th>
									<td>
										<s:select list="#request.protocolList" theme="simple" name="protocols" id="protocols"
													listKey="value" listValue="display">
										</s:select>
										<label class="autoHide">&emsp;请输入其他协议：</label>
									</td>
									<td>
										<input class="input autoHide" type="text" name="protocol" id="protocol" value="${protocol}"/>
									</td>
								</tr>
								<tr>
									<th><label class="portGray">源端口：</label></th>
									<td>
										<input class="input" type="text" name="srcPort" id="srcPort" value=""/>
									</td>
								</tr>
								<tr>
									<th><label class="portGray">目的端口：</label></th>
									<td>
										<input class="input" type="text" name="destPort" id="destPort" value=""/>								
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
	<!--弹出层警告结束-->
	
	<!--面板开始-->
	<div class="mainPartPanel">
		<div class="circle"></div>
		&nbsp;
	</div>
	<!--面板结束-->
	<!--主要内容开始-->
	<div class="mainPartContNoTop">
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
								<span class="tit"><strong>网络屏蔽列表</strong>
								</span>
								<div id="message" style="float:left;color: red;padding-left:10px;"></div>
								<span class="operate">
									<a href="javascript:;" class="addLink popDivBt" id="add">添加</a>
								</span>
							</caption>
							<thead>
								<tr>
									<th width="145">源地址/掩码</th>
									<th width="145">目的地址/掩码</th>
									<th width="80">协议</th>
									<th width="75">源端口</th>
									<th width="75">目的端口</th>
									<th class="last lb2">操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="list" id="access" status="index">
									<tr>
										<td>${srcNet}</td>										
										<td>${destNet}</td>
										<td>${protocol}</td>
										<td>
											<s:if test="%{srcPort != null && srcPort != 0}">${srcPort}</s:if>
										</td>
										<td>
											<s:if test="%{destPort != null && destPort != 0}">${destPort}</s:if>
										</td>
										<td>
											<a href="javascript:;" onclick="kylin.klb.access.setss({'id':'${id}'})">设置</a>
											<span>
												<a href="javascript:;" onclick="kylin.klb.access.delss(this,{'id':'${id}'})">删除</a>
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
		<div class="txtCont">
			<%-- <h4>说明：</h4>
			<p>
			&emsp;访问控制包括IP地址和网络掩码两项，共同组成访问控制目标。可屏蔽单一地址与网段。<br />
			</p> --%>
		</div>
	</div>
	<!--主要内容结束-->
</body>
</html>
