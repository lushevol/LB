<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/meta.jsp" %>
<title>dhcp</title>
<link href="${ctx}/css/skin/default/common.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/module.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/subindex.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/messages_cn.js"></script>
<script type="text/javascript" src="${ctx}/js/network/dhcp.js"></script>
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
	    <h3><span>DHCP配置</span></h3>
	    <br />
	    <!--弹出层警告开始-->
		<!--开放服务添加 -->
		<%-- <div id="mark"></div>
		<div class="popLayer popDiv" id="addScheduler">
			<div class="tt">
				<h3>DHCP配置</h3>
				<div class="operate">
					<a href="javascript:;" class="btnClose" hider="#addScheduler" title="关闭">关闭</a>
				</div>
			</div>
	
			<div class="ct">
				<div class="main">
					<div class="nrarea">
						<form id="inputForm" name="inputForm" method="post">
							<input type="hidden" id="update" name="update" value="0"/>
	       					<input type="hidden" id="row" name="row" value=""/>
	       					
							<div class="popTable">
								<div id="show" style="color: red;"></div>
								<table width="100%" cellspacing="0">
									<tr>
										<th width="106">选择接口：</th>
										<td>
											<select name="interface" id="interface">
												<option value="">-请选择-</option>
												<option>eth0</option>
												<option>eth1</option>
												<option>bond0</option>
											</select>
											<s:select list="#request.interfaceList" theme="simple" name="interface" id="interface" 
								              	headerKey="" headerValue="-请选择-" listKey="value" listValue="display">
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
		</div>--%>
		<!--弹出层结束-->
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
								<span class="tit"><strong>DHCP信息列表</strong></span>
								<div id="message" style="float:left;color: red;padding-left:10px;"></div>
								<span class="operate"></span>
							</caption>
							<thead>
								<tr>
									<th width="200">指定接口</th>
									<th width="120">运行状态</th>
									<th class="last lb2">操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="dl" id="dhcpList" status="index">
									<tr>
										<td>${inter}</td>										
										<td>
											<s:if test="%{status == 'true'}">运行</s:if>
											<s:else>停止</s:else>
										</td>
										<td>
											<span> 
												<s:if test="%{status == 'true'}">
													<a href="javascript:;" onclick="dhcp.edit(this,{'inter':'${inter}','status':'false'})">停止</a>
												</s:if> <s:else>
													<a href="javascript:;" onclick="dhcp.edit(this,{'inter':'${inter}','status':'true'})">运行</a>
												</s:else>
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
		<h4>DHCP配置说明：</h4>
		<p>
		&emsp;DHCP配置。
		</p>
	</div>
    
</div>
<!--主要内容结束-->

</body>
</html>