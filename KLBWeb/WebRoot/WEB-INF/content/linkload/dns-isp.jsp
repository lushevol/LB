<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/meta.jsp" %>
<title>dns-isp</title>
<link href="${ctx}/css/skin/default/common.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/module.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/subindex.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/messages_cn.js"></script>
<script type="text/javascript" src="${ctx}/js/linkload/dns.isp.js"></script>
</head>
<body>
<!--面板开始-->
<div class="mainPartPanel">
  <div class="circle"></div>&nbsp;
</div>
<!--面板结束-->
<!--主要内容开始-->
<div class="mainPartContNoTop">
  <!--文本内容区开始-->
  <div class="txtCont">  
    <div class="box3">
	    <h3><span>ISP地址段</span></h3>
	    <table cellpadding="0" class="table">
			<tr>
				<th width="720">
					<a href="dns-isp-import!list.action" class="btnStyle btnGray"><span>ISP导入</span></a>
				</th>
			</tr>
		</table>
		
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
								<span class="tit"><strong>ISP地址段列表</strong></span>
								<input type="hidden" name="failedMess" id="failedMess" value="${failedMess}"/>
								<div id="message" style="float:left;color:red;padding-left:10px;"></div>
								<span class="operate"><a href="dns-isp-config!list.action?id=add" class="addLink popDivBt" id="add">添加</a></span>
							</caption>
							<thead>
								<tr>
									<th width="180">运营商名称</th>
									<th width="200">ISP地址段</th>					
									<%-- <th width="90">接口</th>
									<th width="70">权重</th>									
									<th width="45">状态</th> --%>
									<th class="last lb2">操作</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="dil" id="serviceList" status="index">
									<tr>
										<td>											
											${name}
											<%--<s:if test="%{sale == 'Tele'}">电信</s:if>
											<s:if test="%{sale == 'CNC'}">网通</s:if> --%>
										</td>
										<td>${addr}</td>
										<%-- <td>${inter}</td>
										<td>${weight}</td>
										<td>
											<s:if test="%{status == 'true'}">开启</s:if>
											<s:else>关闭</s:else>
										</td> --%>
										<td>
											<%-- <span>
												<s:if test="%{status == 'true'}">
													<a href="javascript:;" onclick="route.isp.edit(this,{'sale':'${sale}','status':'false'})">
													关闭</a>
												</s:if>
												<s:else>
													<a href="javascript:;" onclick="route.isp.edit(this,{'sale':'${sale}','status':'true'})">
													开启</a>
												</s:else>
											</span> --%>
											
											<a href="dns-isp-config!list.action?id=${id}">设置</a>
											<span>
												<a href="javascript:;" onclick="dns.isp.del(this,{'id':'${id}'})">删除</a>
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
  </div>
  <!--文本内容区结束-->
</div>
<!--主要内容结束-->

</body>
</html>