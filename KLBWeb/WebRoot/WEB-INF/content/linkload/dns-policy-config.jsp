<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/meta.jsp" %>
<title>dns-policy-config</title>
<link href="${ctx}/css/skin/default/common.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/module.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/subindex.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/js/validate/messages_cn.js"></script>
<script type="text/javascript" src="${ctx}/js/linkload/dns.policy.config.js"></script>
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
    <h4>   	
    	<span><a href="${ctx}/linkload/dns-policy!list.action"><font color="#DD0099">DNS策略</font></a></span> |    	
		<span><a href="${ctx}/linkload/dns-server!input.action">服务器设置</a></span>
    </h4>
    <p class="vLine"></p>
    <form id="policyConfigForm" name="policyConfigForm" action="dns-policy-config!save.action" method="post">
    <div class="box3">
    	<h3><span>策略属性</span></h3>
    	<div id="show" style="color: red;">&nbsp;<s:actionmessage theme="mytheme"/></div>
    	<input type="hidden" id="id" name="dpc.id" value=${dpc.id} />
    	<table cellpadding="0" class="table">
			<tr>
				<th width="150">域名：</th>
				<td>
					<input class="input" type="text" name="dpc.name" id="name" value="${dpc.name}"/>
				</td>
			</tr>
			<tr>
				<th>状态：</th>
				<td colspan="3">
					<s:select list="#request.stateList" theme="simple" name="dpc.state" id="state"
							listKey="value" listValue="display">
					</s:select>
				</td>
			</tr>
		</table>
			
		<table cellpadding="0" class="table">
			<tr>
				<th width="150">别名：<input type="hidden" name="dpc.aliseList" id="alises" value="${dpc.aliseList}"/></th>
				<td>
					<input type="hidden" name="dpc.allAliasList" id="allAlias" value="${dpc.allAliasList}"/>
				 	<input type="text" class="input" id="alise" name="alise" value="" />&emsp;
				</td>
				<td>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;
					<a href="javascript:;" class="btnStyle" id="add" onclick="dns.policy.config.addAlise()"><span>添加</span></a>
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
						<table id="aliseTable">
							<caption>
								<span class="tit"><strong>DNS别名列表</strong></span>
								<div id="message" style="float:left;color: red;padding-left:10px;"></div>
								<span class="operate"></span>
							</caption>
							<thead>
								<tr>
									<th width="200">别名</th>														
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
			
		<h3><span>策略配置</span></h3>
		<table cellpadding="0" class="table">
			<tr>
				<th>ISP名称：</th>
				<td>
					<s:select list="#request.ispNameList" theme="simple" name="ispName" id="ispName" 
							headerKey="" headerValue="-默认-" listKey="value" listValue="display">
					</s:select>
					<input type="hidden" name="dpc.servers" id="servers" value="${dpc.servers}"/>
					<input type="hidden" name="dpc.displayServers" id="displayServers" value="${dpc.displayServers}"/>
				</td>
			</tr>
			<tr>
				<th width="150">IP地址：</th>				
				<td>
				 	<input type="text" class="input" id="ip" name="ip" value="" />&emsp;
				</td>
				<td>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;
					<a href="javascript:;" class="btnStyle" id="add" onclick="dns.policy.config.addServer()"><span>添加</span></a>
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
						<table id="serverTable">
							<caption>
								<span class="tit"><strong>ISP服务器列表</strong></span>
								<div id="message" style="float:left;color: red;padding-left:10px;"></div>
								<span class="operate"></span>
							</caption>
							<thead>
								<tr>
									<th width="180">ISP名称</th>
									<th width="180">IP地址</th>									
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
		
		<table cellpadding="0" class="table">
			<tr>
				<th width="150">DNS回应：<input class="input" type="hidden" name="dpc.echo" id="echo" value="${dpc.echo}"/></th>
				<td colspan="3">
					<input type="radio" name="echo" value="" id="returnAll"/>&nbsp;返回所有IP
				</td>
			</tr>
			<tr>
				<th></th>
				<td colspan="3">
					<input type="radio" name="echo" value="" checked="checked" id="linkAll"/>&nbsp;按策略返回链路的所有IP				
				</td>
			</tr>
			<tr>
				<th>记录生存时间（TTL）：</th>
				<td>
					<input class="input" type="text" name="dpc.ttl" id="ttl" value="${dpc.ttl}"/>
					<span class="gray7">&nbsp;秒</span>
				</td>
			</tr>
		</table>
				
	</div>		
	    <div class="btnArea pL137">
			<button class="btn" type="submit" id="button"><span>确定</span></button>
			<a href="${ctx}/linkload/dns-policy!list.action" class="btnStyle btnGray"><span>&ensp;取消</span></a>
		</div>
    </form>
	      
  </div>
  <!--文本内容区结束-->
</div>
<!--主要内容结束-->

</body>
</html>