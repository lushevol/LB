<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/meta.jsp" %>
<title>dns-isp-config</title>
<link href="${ctx}/css/skin/default/common.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/module.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/subindex.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/messages_cn.js"></script>
<script type="text/javascript" src="${ctx}/js/linkload/dns.isp.config.js"></script>
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
    <form id="ispConfigForm" name="ispConfigForm" action="dns-isp-config!save.action" method="post">
    <div class="box3">
    	<h3><span>ISP名称</span></h3>
    	<div id="show" style="color: red;">&nbsp;<s:actionmessage theme="mytheme"/></div>
    	<table cellpadding="0" class="table">
			<tr>
				<th width="150">运营商：</th>
				<td>
					<input type="hidden" name="dic.id" id="id" value="${dic.id}"/>
					<input type="text" class="input" name="dic.name" id="name" value="${dic.name}"/>					
				</td>
			</tr>
		</table>
		<h3><span>ISP配置</span></h3>
		<table cellpadding="0" class="table">
			<tr>
				<th width="150">地址(/掩码)：<input type="hidden" name="dic.ipList" id="ips" value="${dic.ipList}"/></th>
				<td>
					<input type="text" class="input" id="ip" name="ip" value="" maxlength="18"/>&emsp;&emsp;
				</td>
				<td>&emsp;&emsp;
					<a href="javascript:;" class="btnStyle" id="add" onclick="dns.isp.config.addIp()"><span>添加</span></a>
				</td>
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
						<table id="routeTable">
							<caption>
								<span class="tit"><strong>ISP地址列表</strong></span>
								<div id="message" style="float:left;color: red;padding-left:10px;"></div>
								<span class="operate"></span>
							</caption>
							<thead>
								<tr>
									<th width="380">地址/掩码</th>
									<th class="last lb2">操作</th>
								</tr>
							</thead>
							<tbody>
								
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
		
		<%-- <table cellpadding="0" class="table">
			<tr>
				<th width="150">导入文件：</th>
				<td><input type="file" id="upload" name="upload" size="40" style="height:22px"/></td>
		      	<td><a href="javascript:void(0);" class="btnStyle" id="import"><span>导入</span></a></td>				
			</tr>
		</table> --%>
				
	</div>		
	    <div class="btnArea pL137">
			<button class="btn" type="submit" id="button"><span>确定</span></button>
			<a href="${ctx}/linkload/dns-isp!list.action" class="btnStyle btnGray"><span>&ensp;取消</span></a>
		</div>
    </form>
	      
  </div>
  <!--文本内容区结束-->
</div>
<!--主要内容结束-->

</body>
</html>