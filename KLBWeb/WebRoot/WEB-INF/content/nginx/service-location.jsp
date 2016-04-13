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
	<script src="${ctx}/js/nginx/kylin.klb.service.location.js" type="text/javascript"></script>
</head>
<body>
	<div id="mark"></div>
	<!--添加虚拟服务 -->
	<div class="popLayer popDiv" id="addScheduler">
		<div class="tt">
			<h3>规则配置</h3>
			<div class="operate">
				<a href="javascript:;" class="btnClose" hider="#addScheduler" title="关闭">关闭</a>
			</div>
		</div>
		<div class="ct">
			<div class="main">
				<div class="nrarea" id="setssid">
				<!--<div class="txtCont">
						<p>location配置说明：ssl连接超时时间，仅在启用ssl卸载时有效。</p>
					</div>
				 -->
					<form id="inputForm" name="inputForm" method="post">
						<input type="hidden" id="update" name="update" value="0" />
							
						<input type="hidden" id="locationId" name="locationId" value="" />						
						<div class="popTable">
							<div id="show" style="color: red;"></div>
							<table width="100%" cellspacing="0">
								<tr>
									<th width="110">匹配规则：</th>
									<td>
										<input type="text" class="input" name="match" id="match" />
									</td>
								</tr>
								
								<tr>
									<th>真实服务器组名：</th>
									<td>
										<s:select list="#request.realGroupNameList" theme="simple" name="groupName" id="groupName" headerKey=""
											headerValue="-空-" listKey="value" listValue="display"></s:select>
									</td>							
								</tr>					
								<tr id="insertTr">
									<th>插入位置：</th>
									<td>
										<s:select list="#request.insertList" theme="simple" name="insertId" id="insertId" headerKey=""
											headerValue="序列末尾" listKey="display" listValue="value"></s:select>
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
	<div class="box3"><h3><span>虚拟服务状态</span></h3>
			<table cellpadding="0" class="table">
				<tr>
					<th width="150">域&nbsp;&nbsp;&nbsp;&nbsp;名：</th>
					<td>
						${virtualService.name }
					</td>
				</tr>
			</table>
			<table cellpadding="0" class="table">
				<tr>
					<th width="150">服务状态：</th>
					<td>
						<s:if test="%{virtualService.status == 'true'}"><font color="#0070EE">正常</font></s:if>
						<s:else><font color="#FF0000">异常</font></s:else>
					</td>
				</tr>
			</table>
<s:if test="#session.isSoftVersion.equals('false')">		
			<table cellpadding="0" class="table">
				<tr>
					<th width="150">高可用状态：</th>
					<td>
						<s:if test="%{virtualService.haStatus == 'true'}"><font color="#0070EE">正常</font></s:if>
						<s:else><font color="#FF0000">异常</font></s:else>
					</td>
				</tr>
			</table>
</s:if>				
		</div>
		
		<div class="box3"><h3><span>规则配置</span></h3></div>
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
								<span class="tit"><strong>规则列表</strong></span>
								<div id="message" style="float: left; color: red; padding-left: 10px;"></div>
								<span class="operate"><a href="javascript:;" class="addLink popDivBt" id="add">添加</a></span>
							</caption>
							<thead>
								<tr>		
									<th width="80">序列</th>							
									<th width="250">匹配规则</th>
									<th width="250">真实服务器组名</th>																		
									<th class="last lb2">操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="locationList" status="count">
									<tr>
										<td>${locationId}</td>								
										<td>${match}</td>
										<td>${groupName}</td>																
										<td>
											<a href="javascript:;" class="popDivBt" onclick="kylin.klb.service.location.setss({'vsId':'${vsId}', 'locationId':'${locationId}'})">设置</a>
											<span><a href="javascript:;" onclick="kylin.klb.service.location.delss(this,{'vsId':'${vsId}', 'locationId':'${locationId}'})">删除</a></span>
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
		<form action="service-location!upload.action" id="uploadForm"  name="uploadForm" method="post" enctype="multipart/form-data" method="post">
	<input type="hidden" id="vsId" name="vsId" value="${vsId}" />	
	<input type="hidden" id="vsName" name="vsName" value="${vsName}" />	
		
		<div class="box3"><h3><span>高级管理</span></h3>
	    	<div id="uploadShow" style="color: red;">&nbsp;${uploadMsg }</div>
	    	<table cellpadding="0" class="table">
				<tr>
					<th width="150">启用ssl卸载：</th>
					<td>
						<s:radio list="#{'true':'是','false':'否'}" name="virtualService.sslStatu" id="sslStatu" 
								theme="simple" onclick="window.kylin.klb.service.location.sslStatuChange()"></s:radio>				
					</td>
				</tr>
			</table>
			<table cellpadding="0" class="table">
				<tr>
					<th width="150">ssl超时时间：</th>
					<td>
						<input type="text" class="input" name="virtualService.sslTimeout" id="sslTimeout" value="${virtualService.sslTimeout}" />
						<span class="gray7">秒&nbsp;&nbsp;</span>			
					</td>
				</tr>
			</table>
	    	
	    	<table cellpadding="0" class="table">
				<tr>
					<th width="150">ssl证书：</th>
					<td>
						<input type="file" id="certFile" name="certFile" size="30" style="height:20px"/>
						
					</td>
				</tr>
			</table>
			
			<table cellpadding="0" class="table">
				<tr>
					<th width="150">ssl证书密钥：</th>
					<td>
						<input type="file" id="keyFile" name="keyFile" size="30" style="height:20px"/>
					</td>
				   
				</tr>
			</table>
			<table cellpadding="0" class="table">
				<tr>
					<th width="150">启用cookie：</th>
					<td>
						<s:radio list="#{'true':'是','false':'否'}" name="virtualService.cookieEnabled" id="cookieEnabled" 
							theme="simple"  onclick="window.kylin.klb.service.location.cookieEnabledChange()"></s:radio>				
					</td>
				</tr>
			</table>
			<table cellpadding="0" class="table">
				<tr>
					<th width="150">cookie名称：</th>
					<td>
						<input type="text" class="input" name="virtualService.cookieName" id="cookieName" value="${virtualService.cookieName}" />
						<span class="gray7">&nbsp;&nbsp;</span>			
					</td>
				</tr>
			</table>
			<table cellpadding="0" class="table">
				<tr>
					<th width="150">cookie有效时间：</th>
					<td>
						<input type="text" class="input" name="virtualService.cookieExpire" id="cookieExpire" value="${virtualService.cookieExpire}" />	
						<span class="gray7">天&nbsp;&nbsp;</span>		
					</td>
				</tr>
			</table>
		</div>
		<!--列表结束-->
		<div class="btnArea pL137">			
			<button class="btn" type="button" id="upload"><span>保存</span></button>
			<a href="${ctx}/nginx/service-location.action?vsId=${vsId}&vsName=${vsName}" class="btn btnGray"><span>重置</span></a>
			<a href="${ctx}/nginx/nginx-virtual-serv.action" class="btn"><span>返回</span></a>
		</div>
		<div class="txtCont">			
		</div>
	</form>
		
	</div>
	<!--主要内容结束-->
</body>
</html>