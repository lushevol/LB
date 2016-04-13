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
	<script src="${ctx}/js/nginx/kylin.klb.nginx.virtual.serv.js" type="text/javascript"></script>
</head>
<body>
	<div id="mark"></div>
	<!--添加虚拟服务 -->
	<div class="popLayer popDiv" id="addScheduler">
		<div class="tt">
			<h3>虚拟服务配置</h3>
			<div class="operate">
				<a href="javascript:;" class="btnClose" hider="#addScheduler" title="关闭">关闭</a>
			</div>
		</div>
		<div class="ct">
			<div class="main">
				<div class="nrarea" id="setssid">
				<!-- 	<div class="txtCont">
						<p>虚拟服务说明：ssl连接超时时间，仅在启用ssl卸载时有效。</p>
					</div>
				 -->
					<form id="inputForm" name="inputForm" method="post">
						<input type="hidden" id="update" name="update" value="0" />
						<input type="hidden" id="oldName" name="oldName" value="" />
						<input type="hidden" id="vsId" name="vsId" />
						<div class="popTable">
							<div id="show" style="color: red;"></div>
							<table width="100%" cellspacing="0">
								<tr>
									<th width="110">域&nbsp;&nbsp;&nbsp;&nbsp;名：</th>
									<td>
										<input type="text" class="input" disabled="disabled" name="name" id="name" />
									</td>
								</tr>
 <s:if test="#session.isHaEnable.equals('true')">								
								<tr>
									<th>高可用类型：</th>
									<td>
										<s:select list="#request.haTypeList" theme="simple" name="haType" id="haType" headerKey=""
											headerValue="-空-" listKey="value" listValue="display">
										</s:select>						
									</td>
								</tr>
 </s:if>
 <s:else>
 	<input type="hidden" name="haType" id="haType" value="0"/>
 </s:else>								
								<tr>
									<th>接&nbsp;&nbsp;&nbsp;&nbsp;口：</th>
									<td>
<s:if test="#session.isSoftVersion.equals('false')">								
										<s:select list="#request.devList" theme="simple" name="dev" id="dev" headerKey=""
											headerValue="-空-" listKey="value" listValue="display"></s:select>
																				
</s:if>
<s:else>
 	<input type="text" name="dev" id="dev" class="input fno"/>
</s:else>
									</td>
								</tr>
								<tr>
									<th>虚拟服务地址：</th>
									<td>
										<input type="text" class="input fno" name="virtualIp" id="virtualIp" />
									</td>
								</tr>
								<tr>
									<th>监听端口：</th>
									<td>
										<input type="text" class="input fno" name="listenPort" id="listenPort" />
									</td>
								</tr>
								<!--  
								<tr>
									<th>使用ssl卸载：</th>
									<td>
										<input type="radio" id="onSslStatu" name="sslStatu" checked="checked"  />&nbsp;是&nbsp;&nbsp;
										<input type="radio" id="offSslStatu" name="sslStatu" />&nbsp;否
									</td>
								</tr>
								<tr>
									<th>ssl连接超时：</th>
									<td>
										<input type="text" class="input fno" name="sslTimeout" id="sslTimeout" />
									</td>
								</tr>
								-->
								<!--
								<tr>
									<th>ssl证书：</th>
									<td>
										<input type="file" id="certName" name="certName" size="20" style="height:20px"/>
									</td>
								</tr>	
								<tr>
									<th>ssl证书密钥：</th>
									<td>
										<input type="file" id="keyName" name="keyName" size="20" style="height:20px"/>
									</td>
								</tr>	
							  -->							
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
	  
	<!-- 隐藏iframe 上传cert和key 
	 <iframe src="" id="importIframe" frameborder="0" name="importIframe" height="1" width="1"  scrolling="yes"  >
	 	<form action="nginx-virtual-serv!upload.action" id="importForm"  name="importForm" method="post" enctype="multipart/form-data" method="post">
	 		<input type="file" id="certFile" name="certFile" size="15" style="height:20px"/>
	 		<input type="file" id="keyFile" name="keyFile" size="15" style="height:20px"/>
	 		<input type="text" id="kao" name="kao" value="kao" />
	 	</form>
	 </iframe>
	 -->
	<!--面板开始-->
	<div class="mainPartPanel">
		<div class="circle"></div>
		&nbsp;
	</div>
	<!--面板结束-->
	<!--主要内容开始-->
	<div class="mainPartContNoTop">
		<div class="box3"><h3><span>虚拟服务</span></h3></div>
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
								<span class="tit"><strong>虚拟服务列表</strong></span>
								<div id="message" style="float: left; color: red; padding-left: 10px;"></div>
								<span class="operate"><a href="javascript:;" class="addLink popDivBt" id="add">添加</a></span>
							</caption>
							<thead>
								<tr>
<s:if test="#session.isHaEnable.equals('true')">								
									<th width="100">高可用类型</th>
</s:if>										
									<th width="100">域&nbsp;&nbsp;&nbsp;&nbsp;名</th>	
									<th width="100">虚拟IP接口</th>		
									<th width="110">虚拟IP地址</th>																
									<th width="100">监听端口</th>			
									<!-- 			
									<th width="80">SSL超时时间</th>
									<th width="100">COOKIE超时时间</th>	
								 -->										
									<th class="last lb2">虚拟服务操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="virtualServerGroupList" status="count">
									<tr>
<s:if test="#session.isHaEnable.equals('true')">									
										<td>${haName}</td>	
</s:if>																		
										<td>${name}</td>
										<td>${dev}</td>
										<td>${virtualIp}</td>										
										<td>${listenPort}</td>
									<!--
										<td class="stc">
											<s:if test="%{sslStatu == 'true'}">${sslTimeout}</s:if>
											<s:else>未启用</s:else>
										</td>
										<td class="stc">
											<s:if test="%{cookieEnabled == 'true'}">${cookieExpire}</s:if>
											<s:else>未启用</s:else>
										</td>
									 -->		
										<td>
											<a href="javascript:;" class="popDivBt" onclick="kylin.klb.nginx.virtual.serv.setss({'vsId':'${vsId}', 'name':'${name}'})">设置</a>
											<span><a href="javascript:;" onclick="kylin.klb.nginx.virtual.serv.delss(this,{'vsId':'${vsId}'})">删除</a></span>
											<a href="service-location.action?vsId=${vsId}&vsName=${name}">高级管理</a>
											
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