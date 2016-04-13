<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/meta.jsp" %>
<title>arp</title>
<link href="${ctx}/css/skin/default/common.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/module.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/subindex.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/messages_cn.js"></script>
<script type="text/javascript" src="${ctx}/js/network/arp.js"></script>
</head>
<body>
	<!--弹出层警告开始-->
	<!--添加ARP配置 -->
	<div id="mark"></div>
	<div class="popLayer popDiv" id="addScheduler">
		<div class="tt">
			<h3>配置ARP</h3>
			<div class="operate">
				<a href="javascript:;" class="btnClose" hider="#addScheduler" title="关闭">关闭</a>
			</div>
		</div>
	
		<div class="ct">
			<div class="main">
				<div class="nrarea">
					<form id="inputForm" name="inputForm" method="post">
	       				<input type="hidden" id="id" name="id" value="-1"/>
	       				<input type="hidden" id="operation" name="operation" value="add"/>
						<div class="popTable">
							<div id="show" style="color: red;"></div>
							<table width="100%" cellspacing="0">
								<tr>
									<th width="106">选择接口：</th>
									<td>
										<s:select list="#request.interfaceList" theme="simple" name="interface" id="interface" 
								              headerKey="" headerValue="-空-" listKey="value" listValue="display">
								        </s:select>&nbsp;
								        <span><font color="#DD0099">*</font></span>
									</td>
								</tr>
								<tr>
									<th width="106">IP地址：</th>
									<td>											
										<input class="input" type="text" name="ip" id="ip"/>
										<span><font color="#DD0099">*</font></span>										
									</td>
								</tr>
								<tr>
									<th width="106">MAC地址：</th>
									<td>
										<input class="input" type="text" name="mac" id="mac"/>
										<span><font color="#DD0099">*</font></span>
										<span class="gray7">&nbsp;[格式如 AA:BB:CC:DD:EE:FF]</span>					
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
	<!--弹出层结束-->
<!--面板开始-->
<div class="mainPartPanel">
  <div class="circle"></div>&nbsp;
</div>
<!--面板结束-->
<!--主要内容开始-->
<div class="mainPartContNoTop">  
    <div class="box3">       
	    <h3><span>ARP配置</span></h3>
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
								<span class="tit"><strong>ARP信息列表</strong></span>
								<div id="message" style="float:left;color: red;padding-left:10px;"></div>
								<span class="operate"><a href="javascript:;" class="addLink popDivBt" id="add">添加</a></span>
							</caption>
							<thead>
								<tr>
									<th width="150">IP地址</th>
									<th width="170">MAC地址</th>
									<th width="100">状态</th>									
									<th width="110">接口</th>
									<th class="last lb2">操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="arpInfoList" id="arpInfoList" status="index">
									<tr>
										<td>${ip}</td>
										<td>${mac}</td>
										<td>
											<s:if test="%{type == 0}"><font color="#E01010">无效</font></s:if>										
											<s:if test="%{type == 1}"><font color="#0066DD">动态</font></s:if>
											<s:if test="%{type == 2}"><font color="#00AA20">静态</font></s:if>											
										</td>										
										<td>${inter}</td>
										<td>
											<a href="javascript:;" class="popDivBt" onclick="arp.set({'id':'<s:property value="#index.index" />',
											'ip':'${ip}','mac':'${mac}','inter':'${inter}','type':'${type}'})">设置</a>
											<span>											
											<a href="javascript:;" onclick="arp.del(this,{'id':'<s:property value="#index.index" />',
												'ip':'${ip}','inter':'${inter}'})">删除</a>										
											<%-- <s:if test="%{type == 2}">
												<a href="javascript:;" onclick="arp.del(this,{'id':'<s:property value="#index.index" />',
												'ip':'${ip}','inter':'${inter}'})">删除</a>
											</s:if> --%>
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
    <div class="txtCont">		
	</div>
    
</div>
<!--主要内容结束-->

</body>
</html>