<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/meta.jsp" %>
<title>addressD</title>
<link href="${ctx}/css/skin/default/common.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/module.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/subindex.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/messages_cn.js"></script>
<script type="text/javascript" src="${ctx}/js/firewall/addressd.js"></script>
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
    	<a href="${ctx}/firewall/address!list.action">源地址转换</a> | 
		<a href="${ctx}/firewall/addressd!list.action"><font color="#DD0099">目的地址转换</font></a>
    </h4>
    <p class="vLine"></p>
    <div class="box3"><h3><span>目的地址转换</span></h3></div>
	<br />	
	<!--列表开始-->
	<div class="tableWrap address">
		<!--表格外层左侧边框-->
		<div class="ct">
			<!--表格外层右侧边框-->
			<div class="main">
				<!--列表主要内容开始-->
				<div class="tableList">
					<table>
						<caption>
							<span class="tit"><strong>目的地址转换信息列表</strong></span>
							<input type="hidden" name="failedMess" id="failedMess" value="${failedMess}"/>
							<div id="message" style="float:left;color: red;padding-left:10px;"></div>
							<span class="operate">
								<a href="address-dst!list.action?operation=add&id=-1" class="addLink popDivBt" id="add">添加</a>
							</span>
						</caption>
						<thead>
							<tr>
								<th width="25">ID</th>
								<th width="75">描述</th>
								<th width="120">源网络</th>
								<th width="120">目标网络</th>
								<th width="55">协议</th>
								<th width="40">入接口</th>
								<th width="115">转换</th>
								<%-- <th width="45">移动</th>--%>
								<th width="35">插入</th>
								<th width="35">状态</th>
								<th class="last lb2">操作</th>
							</tr>
						</thead>
						<tbody>
							<s:iterator value="natList" id="natList" status="index">
								<tr>
									<td>${id}</td>
									<td>${describe}</td>
									<td><s:if test="%{srcIP == ''}">any</s:if>
										<s:elseif test="%{srcIP != null && srcIP != ''}">${srcIP}</s:elseif>
										<s:if test="%{srcPort != '' && srcPort != null && srcPort != 0}">:${srcPort}</s:if>
									</td>
									<td><s:if test="%{destIP == ''}">any</s:if>
										<s:elseif test="%{destIP != null && destIP != ''}">${destIP}</s:elseif>
										<s:if test="%{destPort != '' && destPort != null && destPort != 0}">:${destPort}</s:if>
									</td>
									<td>${protocol}</td>
									<td>${interfaces}</td>
									<td><s:if test="%{type == 'EXPT'}">不作转换</s:if>
										${startIP}
										<s:if test="%{endIP != '' && endIP != null}">-...</s:if>
										<s:if test="%{startPort != '' && startPort != null && startPort != 0}">&nbsp;:${startPort}</s:if>
										<s:if test="%{endPort != '' && endPort != null && endPort != 0}">-...</s:if>
									</td>
									<%-- <td><a href="javascript:;" class="move" onclick="addressd.recordOldId({'id':'${id}'})">
										<img src="${ctx}/css/skin/images/common/ico_move.gif" alt="移动" title="移动" /></a></td>--%>
									<td><a href="address-dst!list.action?operation=insert&id=${id}">
										<img src="${ctx}/css/skin/images/common/ico_insert.gif" alt="插入" title="插入" /></a></td>
									<td class="stc">
										<s:if test="%{enabled == 'true'}">开启</s:if>
										<s:else>关闭</s:else>
									</td>
									<td>
										<span>										
											<s:if test="%{enabled == 'true'}">
												<a href="javascript:;" onclick="addressd.start(this,{'id':'<s:property value="#index.index" />','enabled':'false'})">
												关闭</a>
											</s:if>
											<s:else>
												<a href="javascript:;" onclick="addressd.start(this,{'id':'<s:property value="#index.index" />','enabled':'true'})">
												开启</a>
											</s:else>
										</span>																						
										<a href="address-dst!list.action?operation=edit&id=${id}">设置</a>										
										<span><a href="javascript:;" onclick="addressd.del(this,{'id':'${id}'})">删除</a></span>
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
  <!--文本内容区结束-->
    
</div>
<!--主要内容结束-->

</body>
</html>