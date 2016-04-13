<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/meta.jsp" %>
<title>adsl</title>
<link href="${ctx}/css/skin/default/common.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/module.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/subindex.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/messages_cn.js"></script>
<script type="text/javascript" src="${ctx}/js/network/adsl.js"></script>
</head>
<body>
<!--面板开始-->
<div class="mainPartPanel">
  <div class="circle"></div>&nbsp;
</div>
<!--面板结束-->
<!--主要内容开始-->
<div class="mainPartContNoTop">
    <div class="box3">
	    <h3><span>ADSL配置</span></h3>
	</div>
	<br />
	<!--列表开始-->
	<div class="tableWrap adsl">
		<!--表格外层左侧边框-->
		<div class="ct">
			<!--表格外层右侧边框-->
			<div class="main">
				<!--列表主要内容开始-->
				<div class="tableList">
					<table>
						<caption>
							<span class="tit"><strong>ADSL信息列表</strong></span>
							<div id="message" style="float:left;color: red;padding-left:10px;"></div>
							<span class="operate"><a href="javascript:;" class="addLink popDivBt" id="add">添加</a></span>
						</caption>
						<thead>
							<tr>
								<th width="65">ADSL接口</th>
								<th width="75">描述</th>
								<th width="100">RX（收到报文数）</th>
								<th width="100">TX（发送报文数）</th>
								<th width="110">分配IP地址</th>
								<th width="80">拨号持续时间</th>
								<th width="60">拨号状态</th>
								<th class="last lb2">操作</th>
							</tr>
						</thead>
						<tbody>
							<s:iterator value="aiList" id="serviceList" status="index">
								<tr>
									<td>${inter}</td>
									<td>${describe}</td>
									<td>${rx}</td>
									<td>${tx}</td>
									<td>${ip}</td>
									<td>${persist}</td>
									<td>
										<s:if test="%{status == 0}"><font color="#0066DD">未拨号</font></s:if>
										<s:if test="%{status == 1}">拨号中...</s:if>
										<s:if test="%{status == 2}"><font color="#00AA20">已连接</font></s:if>
										<s:if test="%{status == 3}"><font color="#E01010">无效</font></s:if>
									</td>
									<td>
										<span>
											<s:if test="%{status == 0}">
												<a href="javascript:;" onclick="adsl.edit(this,{'inter':'${inter}','mode':'dial'})">
												拨号</a>
											</s:if>
											<s:if test="%{status == 1}">
												<a href="javascript:;" onclick="adsl.edit(this,{'inter':'${inter}','mode':'stop'})">
												断开连接</a>
											</s:if>
											<s:if test="%{status == 2}">
												<a href="javascript:;" onclick="adsl.edit(this,{'inter':'${inter}','mode':'stop'})">
												断开连接</a>
											</s:if>
										</span>
										<s:if test="%{status != 2}">
											<a href="javascript:;" onclick="adsl.set({'inter':'${inter}'})">设置</a>
										</s:if>
										<span>
											<a href="javascript:;" onclick="adsl.del(this,{'inter':'${inter}'})">删除</a>
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
	</div>
</div>
<!--主要内容结束-->

<!--弹出层警告开始-->
<div id="mark"></div>
<div class="popLayer popDiv" id="addAdsl">
	<div class="tt">
		<h3>ADSL属性</h3>
		<div class="operate"><a href="javascript:;" class="btnClose" hider="#addAdsl" title="关闭">关闭</a></div>
	</div>
	
	<div class="ct">
		<div class="main">
			<div class="nrarea">
				<form id="inputForm" name="inputForm" method="post">
					<input type="hidden" id="operation" name="operation" value="add"/>
					<div class="popTable">
						<div id="show" style="color: red;"></div>
						<table width="100%" cellspacing="0">
							<tr>
								<th width="106">ADSL接口：</th>
								<td>
									<input class="input" type="text" name="inter" id="inter" value="ppp"/>
									<span class="gray7">&nbsp;[格式为pppX（X为数字）]</span>
									<%-- <s:select list="#request.interfaceList" theme="simple" name="interface" id="interface" 
								          headerKey="" headerValue="请选择" listKey="value" listValue="display">
								    </s:select> --%>
								</td>
							</tr>
							<tr>
								<th width="106">物理接口：</th>
								<td>
									<s:select list="#request.ethList" theme="simple" name="eth" id="eth" 
								        headerKey="" headerValue="-选择接口-" listKey="value" listValue="display">
								    </s:select>
								</td>
							</tr>
							<tr>
								<th width="106">描述：</th>
								<td>											
									<input class="input" type="text" name="describe" id="describe"/>																				
								</td>
							</tr>
							<tr>
								<th width="106">用户名：</th>
								<td>											
									<input class="input" type="text" name="user" id="user"/>
									<span><font color="#DD0099">*</font></span>										
								</td>
							</tr>
							<tr>
								<th width="106">密码：</th>
								<td>											
									<input class="input" type="password" name="password" id="password"/>
									<span><font color="#DD0099">*</font></span>										
								</td>
							</tr>
							<tr>
								<th width="106">MTU：</th>
								<td>
									<input class="input" type="text" name="mtu" id="mtu"/>
									<span class="gray7">&nbsp;[68--1492]</span>					
								</td>
							</tr>
							<%-- <tr>
								<th width="106">按需拨号&nbsp;</th>				
								<td>
									 <input type="checkbox" id="dial" name="dial" value="dial"/>
								</td>
							</tr> --%>
							<tr>
								<th width="106">空闲：</th>
								<td>
									<input class="input" type="text" name="timeout" id="timeout"/>
									<span class="gray7">&nbsp;[/秒]</span>					
								</td>
							</tr>
							<%-- <tr>
								<th width="106">本地地址：</th>
								<td>
									<input class="input" type="text" name="addr" id="addr"/>															
								</td>
							</tr>--%>
						</table>
					</div>
	
					<div class="btnArea btnConfirm">
						<button class="btn" type="submit" id="save_adsl"><span>保存</span></button>
	          			<button class="btn btnGray" type="reset" hider="#addAdsl"><span>取消</span></button>
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
<!--弹出层结束-->

</body>
</html>