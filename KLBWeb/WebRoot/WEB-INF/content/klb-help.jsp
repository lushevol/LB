<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<%@ include file="/common/meta.jsp"%>
		<title>帮助</title>
		<%-- <link rel="stylesheet" type="text/css"
			href="${ctx}/css/skin/default/common.css" />
		<link rel="stylesheet" type="text/css"
			href="${ctx}/css/skin/default/module.css" />
		<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
		<script type="text/javascript" src="${ctx}/js/kylin.klb.common.js"></script> --%>
		<%-- <style type="text/css">
			html,body {
				height: 100%;
				overflow: hidden;
			}
		</style> --%>
		<style type="text/css">
			A:link      { color: #5050ff; text-decoration: underline; }
			A:visited   { color: #5050ff; text-decoration: underline; }
			A:active    { color: #5050ff; text-decoration: underline; }
			.normalCell	{
				color: #000000;
				font-family: verdana,arial,sans-serif;
				text-align: left;
				font-size: 12px;
			}
			.italicCell	{
				color: #00AACC;
				font-family: verdana,arial,sans-serif;
				font-style:italic;
				text-align: left;
				font-size: 12px;
			}
			li { 
				font-family: verdana,arial,sans-serif;
				font-size: 12px;
			}
			ol { 
				font-family: verdana,arial,sans-serif;
				font-size: 12px;
			}
			ul { 
				font-family: verdana,arial,sans-serif;
				font-size: 12px;
			}
			p { 
				font-family: verdana,arial,sans-serif;
				font-size: 12px;
			}
			td { 
				font-family: verdana,arial,sans-serif;
				font-size: 14px;
			}
			body { 
				font-family: verdana,arial,sans-serif;
				font-size: 11px;
			}
			h2 { 
				color: #0000CC;
				font-family: verdana,arial,sans-serif;
				font-size: 20px;
				font-family: verdana,arial,sans-serif;
				font-weight: bold;
			}

		</style>
	</head>
	<body>
		<!--面板开始-->
		<%-- <div class="mainPartPanel">
			<div class="circle"></div>
			&nbsp;
		</div> --%>
		<!--面板结束-->
		<!--主要内容开始-->
		<%-- <div class="mainPartContNoTop">
			<div class="box3">
				<div class="txtCont">
					<li>
						${sessionScope.productName }管理帮助
					</li>
					<p>
						这里显示说明文字，文字的宽度是固定的，高度可以自适应。这里显示说明文字，文字的宽度是固定的，高度可以自适应。这里显示说明文字，文字的宽度是固定的，高度可以自适应。这里显示说明文字，文字的宽度是固定的，高度可以自适应。这里显示说明文字，文字的宽度是固定的，高度可以自适应。这里显示说明文字，文字的宽度是固定的，高度可以自适应。
					</p>
				</div>
				<h3>
					&nbsp;
				</h3>
			</div>
		</div> --%>
		<!--主要内容结束-->
		<center>
			<a name="kvsHelp"></a><h2>${sessionScope.productName }管理帮助</h2>
		</center>
		<hr/>
		<p><b><a name="login">Web用户登录</a></b>：</p>
		<p>该页面是Web网管的入口，用户必须登录后才能配置管理负载均衡系统。</p>
		<table cellspacing="0" cellpadding="4" border="0" width="100%">
		<tr>
			<td width="80" valign="top" class="italicCell">用户名</td>
			<td class="normalCell">调度器中存在的web用户。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">密码</td>
			<td class="normalCell">登录用户所必须的密码。</td>
		</tr>
		</table>
		<hr/>
		<p><b><a name="userChange">修改密码</a></b>：</p>
		<p>该页面可以修改登录的用户密码。</p>
		<table cellspacing="0" cellpadding="4" border="0" width="100%">
		<tr>
			<td width="80" valign="top" class="italicCell">登录名</td>
			<td class="normalCell">默认登录名为admin。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">密码</td>
			<td class="normalCell">用户原来的密码。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">新密码</td>
			<td class="normalCell">用户需要设置的新密码。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">确认密码</td>
			<td class="normalCell">重复输入一遍密码，防止第一次输入有误。</td>
		</tr>
		</table>
		<hr/>
		<%-- <p><b><a name="set">设置向导</a></b>：</p>
		<p>该页面用于准确、快速地配置一次虚拟服务。</p>
		<p><b><a name="set1">设置向导 — 欢迎页面</a></b>：</p>
		<p>对设置向导进行简介，用户可以选择下次使用是否出现此页面。</p>
		<p><b><a name="set2">设置向导 — 负载均衡虚拟服务页面</a></b>：</p>
		<table cellspacing="0" cellpadding="4" border="0" width="100%">
		<tr>
			<td width="80" valign="top" class="italicCell">虚拟服务名称</td>
			<td class="normalCell">设定简洁明了的虚拟服务名称，只能包括英文字母、数字和下划线。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">虚拟服务地址</td>
			<td class="normalCell">对外提供虚拟服务的IP地址。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">虚拟服务掩码</td>
			<td class="normalCell">与虚拟服务IP地址相配套的网络掩码。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">端口</td>
			<td class="normalCell">虚拟服务需要启用的端口。多个端口之间用“，”分隔，端口段之间用“—”分隔。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">连接保持时间</td>
			<td class="normalCell">连接保持时间用于保证某些服务的不间断性，类似“FTP”等长时间数据交换的服务需要开启连接保持，而“HTTP”则不需要。默认设置"0"表示关闭，需要开启时，可以设置60—86400之间任意一个数值。</td>
		</tr>
		</table>
		<p><b><a name="set3">设置向导 — 负载均衡真实服务器页面</a></b>：</p>
		<table cellspacing="0" cellpadding="4" border="0" width="100%">
		<tr>
			<td width="80" valign="top" class="italicCell">真实服务器名称</td>
			<td class="normalCell">设定简洁明了的真实服务器名称，只能包括英文字母、数字和下划线。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">真实服务器地址</td>
			<td class="normalCell">真实服务器的IP地址。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">转发方式</td>
			<td class="normalCell">从调度器转发数据到真实服务器有三种方式，分别是：网络地址转换、IP隧道、直接路由。</td>
		</tr>
		</table>
		<hr/> --%>
		<p><b><a name="sysInfo"><font color="#CC0088">系统管理</font></a></b></p>
		<hr size="1" color="#0099DD"/>
		<p><b><a name="sysInfo">系统信息</a></b>：</p>
		<p>该页面主要用于显示调度器的一些系统状态。</p>
		<table cellspacing="0" cellpadding="4" border="0" width="100%">
		<tr>
			<td width="80" valign="top" class="italicCell">调度器系统状态</td>
			<td class="normalCell">显示调度器CPU、内存、硬盘的参数以及使用情况。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">调度器转发状态</td>
			<td class="normalCell">显示配置好的虚拟服务转发状态。包括服务地址、端口、转发方式、转发目标、权重、活动连接数以及非活动连接数等。</td>
		</tr>
		</table>
		<hr/>
		<%-- <p><b><a name="sysCheck">系统自检</a></b>：</p>
		<p>该页面可以对配置文件内容进行逻辑检测，检测用户配置是否正确。</p>
		<table cellspacing="0" cellpadding="4" border="0" width="100%">
		<tr>
			<td width="80" valign="top" class="italicCell">系统自检</td>
			<td class="normalCell">检测之后，若未发现问题，则提示正常；若发现问题，则会逐一提示，主要包括参数是否完整、参数填写是否正确、功能项配置是否完整等逻辑问题。</td>
		</tr>
		</table>
		<hr/> --%>
		<p><b><a name="sysFiles">配置管理</a></b>：</p>
		<p>该页面可以对系统配置进行保存和重载，还可以对配置文件进行导入与导出。</p>
		<table cellspacing="0" cellpadding="4" border="0" width="100%">
		<tr>
			<td width="80" valign="top" class="italicCell">保存</td>
			<td class="normalCell">保存系统当前的所有配置。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">重载</td>
			<td class="normalCell">放弃当前修改，恢复至所保存的配置。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">浏览</td>
			<td class="normalCell">在本地找到需要导入的配置文件。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">导入</td>
			<td class="normalCell">将选择好的配置文件导入调度器。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">导出</td>
			<td class="normalCell">从调度器上下载配置文件至本地。</td>
		</tr>
		</table>
		<hr/>
		<p><b><a name="sysFiles">系统升级</a></b>：</p>
		<p>该页面可以导入升级包进行系统升级。</p>
		<table cellspacing="0" cellpadding="4" border="0" width="100%">
		<tr>
			<td width="80" valign="top" class="italicCell">浏览</td>
			<td class="normalCell">在本地找到需要导入的系统升级包。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">升级</td>
			<td class="normalCell">选定升级包后点击升级按钮即可进行系统升级。</td>
		</tr>
		</table>
		<hr/>
		<p><b><a name="sysFiles">系统时间</a></b>：</p>
		<p>该页面可以对服务器的时间进行设置，高可用方案需要两台负载均衡服务器时间保持一致。</p>
		<table cellspacing="0" cellpadding="4" border="0" width="100%">
		<tr>
			<td width="80" valign="top" class="italicCell">当前时间</td>
			<td class="normalCell">显示负载均衡服务器的当前时间。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">日期</td>
			<td class="normalCell">为服务器时间选择一个日期。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">时间</td>
			<td class="normalCell">为服务器时间填写一个具体时间点。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">时区</td>
			<td class="normalCell">为服务器时间选择一个时区。</td>
		</tr>
		</table>
		<hr/>
		<p><b><a name="sysFiles">系统日志</a></b>：</p>
		<p>该页面可以设置远程日志服务器地址，并启用日志功能。</p>
		<table cellspacing="0" cellpadding="4" border="0" width="100%">
		<tr>
			<td width="80" valign="top" class="italicCell">地址</td>
			<td class="normalCell">设置远程日志服务器地址。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">启用</td>
			<td class="normalCell">选择启用或禁用日志功能。</td>
		</tr>
		</table>
		<hr/>
		<p><b><a name="netInterface"><font color="#CC0088">网络管理</font></a></b></p>
		<hr size="1" color="#0099DD"/>
		<p><b><a name="netInterface">接口配置-物理接口</a></b>：</p>
		<p>该页面用于配置本机的物理接口。</p>
		<table cellspacing="0" cellpadding="4" border="0" width="100%">
		<tr>
			<td width="80" valign="top" class="italicCell">描述</td>
			<td class="normalCell">可自定义一个物理接口描述，如可填写为内网口，外网口等。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">状态</td>
			<td class="normalCell">开启和关闭相应的物理接口。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">DHCP</td>
			<td class="normalCell">可以开启或者关闭该接口的DHCP功能。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">IP地址</td>
			<td class="normalCell">可以添加和删除该物理接口绑定的IP地址及掩码列表。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">高级属性</td>
			<td class="normalCell">可对该物理接口的MTU，协商模式和ARP状态等高级属性进行设置，其中“协商模式”包括“双工模式”和“速率”，用户可选择手工设置也可以设置为自动协商。“ARP状态”包括开启、关闭、ARP应答和ARP代理四种状态。</td>
		</tr>
		</table>
		<hr/>
		<p><b><a name="netInterface">接口配置-链路聚合</a></b>：</p>
		<p>用于配置本机的聚合接口，是接口的高级功能配置页面，可以提高负载均衡服务器的吞吐能力。</p>
		<table cellspacing="0" cellpadding="4" border="0" width="100%">
		<tr>
			<td width="80" valign="top" class="italicCell">添加</td>
			<td class="normalCell">添加聚合接口，保存后该聚合接口将存在一些默认属性。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">聚合设置</td>
			<td class="normalCell">可对该聚合接口的负载算法、监控方式及绑定的物理接口进行设置。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">属性修改</td>
			<td class="normalCell">可对该聚合接口的开启状态、地址/掩码及其高级属性进行修改。</td>
		</tr>
		</table>
		<hr/>
		<p><b><a name="netRouting">路由配置-静态路由</a></b>：</p>
		<p>该页面可以对调度器的静态路由进行管理，可以添加、修改或者删除一条静态路由。</p>
		<table cellspacing="0" cellpadding="4" border="0" width="100%">
		<tr>
			<td width="80" valign="top" class="italicCell">目的地址</td>
			<td class="normalCell">由IP地址和掩码组成。正确填写实例：172.19.0.0/16。“/”前填写的是目标IP地址，“/”后填写的是网络掩码，换算的基本方式为255.0.0.0=8，255.255.0.0=16。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">Metric</td>
			<td class="normalCell">设定该静态路由的度量值。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">描述</td>
			<td class="normalCell">可自定义该静态路由的描述信息。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">网关策略</td>
			<td class="normalCell">设置该路由条目的网关策略，仅对有多出口网关时生效。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">路由网关</td>
			<td class="normalCell">设置该静态路由条目的网关、接口及权重，权重的设置仅在多出口网关时生效。</td>
		</tr>
		</table>
		<hr/>
		<p><b><a name="netRouting">路由配置-策略路由</a></b>：</p>
		<p>该页面可以对调度器的策略路由进行管理，可以添加、修改、删除或者插入一条策略路由。</p>
		<table cellspacing="0" cellpadding="4" border="0" width="100%">		
		<tr>
			<td width="80" valign="top" class="italicCell">描述</td>
			<td class="normalCell">可自定义该策略路由的描述信息。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">网关策略</td>
			<td class="normalCell">设置该策略路由条目的网关策略，仅对有多出口网关时生效。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">路由规则</td>
			<td class="normalCell">可以自定义该策略路由条目的路由规则，包括源网络、目标网络、协议、源端口及目的端口。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">路由网关</td>
			<td class="normalCell">设置该策略路由条目的网关、接口及权重，权重的设置仅在多出口网关时生效。</td>
		</tr>
		</table>
		<hr/>
		<p><b><a name="netDdos">域名服务器</a></b>：</p>
		<p>高可用外网检测页面中，允许用户填写域名地址，因此首先要填写正确的域名服务器地址。</p>
		<table cellspacing="0" cellpadding="4" border="0" width="100%">
		<tr>
			<td width="80" valign="top" class="italicCell">域名服务器</td>
			<td class="normalCell">可设置首选和备选域名服务器地址。</td>
		</tr>		
		</table>
		<hr/>
		<p><b><a name="netDdos">ADSL配置</a></b>：</p>
		<p>可以添加，修改或者删除一个ADSL配置条目。</p>
		<table cellspacing="0" cellpadding="4" border="0" width="100%">
		<tr>
			<td width="80" valign="top" class="italicCell">ADSL接口</td>
			<td class="normalCell">添加的ADSL接口名称。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">物理接口</td>
			<td class="normalCell">设置该ADSL接口绑定的物理接口。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">描述</td>
			<td class="normalCell">可自定义该ADSL接口的描述信息。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">用户名</td>
			<td class="normalCell">设置该ADSL接口相关的用户名。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">密码</td>
			<td class="normalCell">设置该ADSL接口相关的密码。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">MTU</td>
			<td class="normalCell">设置该ADSL接口相关的MTU值，MTU值如无特殊需求请选择默认值1492。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">空闲</td>
			<td class="normalCell">空闲时间X秒，表示拨号无数据传输X秒后自动断开连接，如果需要一直保持连接，则该字段为空。</td>
		</tr>		
		</table>
		<hr/>
		<p><b><a name="netDdos">ARP配置</a></b>：</p>
		<p>可以添加，修改或者删除一条静态ARP配置条目。ARP信息列表还会显示系统中的动态ARP条目，用户可对该动态ARP条目进行设置，将该动态ARP条目保存为静态ARP条目。</p>
		<table cellspacing="0" cellpadding="4" border="0" width="100%">
		<tr>
			<td width="80" valign="top" class="italicCell">选择接口</td>
			<td class="normalCell">设置该静态ARP条目对应的接口。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">IP地址</td>
			<td class="normalCell">设置该静态ARP条目对应的IP地址。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">MAC地址</td>
			<td class="normalCell">设置该静态ARP条目对应的MAC地址。</td>
		</tr>		
		</table>
		<hr/>
		<p><b><a name="netRouting">地址转换-源地址转换</a></b>：</p>
		<p>可以添加，修改，删除或者插入一条源地址转换规则。</p>
		<table cellspacing="0" cellpadding="4" border="0" width="100%">
		<tr>
			<td width="80" valign="top" class="italicCell">规则描述</td>
			<td class="normalCell">设置该源地址转换规则的描述信息。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">源地址</td>
			<td class="normalCell">设定该源地址转换规则的源地址及其掩码。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">目的地址</td>
			<td class="normalCell">设定该源地址转换规则的目的地址及其掩码。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">协议</td>
			<td class="normalCell">设置该源地址转换规则使用的协议。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">源端口</td>
			<td class="normalCell">设定该源地址转换规则使用的源端口。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">目的端口</td>
			<td class="normalCell">设定该源地址转换规则使用的目的端口。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">输出接口</td>
			<td class="normalCell">设置该源地址转换规则使用的输出接口。</td>
		</tr>
		</table>
		<hr/>
		<p><b><a name="netRouting">地址转换-目的地址转换</a></b>：</p>
		<p>可以添加，修改，删除或者插入一条目的地址转换规则。配置方式类似于源地址转换的配置</p>
		<table cellspacing="0" cellpadding="4" border="0" width="100%">
		</table>
		<hr/>
		<p><b><a name="sirtualServ"><font color="#CC0088">应用负载</font></a></b></p>
		<hr size="1" color="#0099DD"/>
		<p><b><a name="sirtualServ">虚拟服务</a></b>：</p>
		<p>该页面用于配置一个虚拟服务。</p>
		<table cellspacing="0" cellpadding="4" border="0" width="100%">
		<tr>
			<td width="80" valign="top" class="italicCell">虚拟服务名称</td>
			<td class="normalCell">设定简洁明了的虚拟服务名称，只能包括英文字母、数字和下划线。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">高可用类型</td>
			<td class="normalCell">设定该虚拟服务的高可用类型，若不启用高可用请选择“本地”。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">接口</td>
			<td class="normalCell">虚拟服务IP地址运行的接口。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">虚拟服务地址</td>
			<td class="normalCell">对外提供虚拟服务的IP地址。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">协议/端口</td>
			<td class="normalCell">虚拟服务需要启用的协议及端口。多个端口之间用“，”分隔，端口段之间用“—”分隔。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">流量控制</td>
			<td class="normalCell">可设置该虚拟服务的最大上行流量和最大下行流量，实现流量控制的功能。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">调度算法</td>
			<td class="normalCell">为虚拟服务选择一个调度算法。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">连接保持</td>
			<td class="normalCell">连接保持时间用于保证某些服务的不间断性，类似“FTP”等长时间数据交换的服务需要开启连接保持，而“HTTP”则不需要。默认设置"0"表示关闭，需要开启时，可以设置60—86400之间任意一个数值。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">连接保持掩码</td>
			<td class="normalCell">与连接保持相配套的网络掩码。</td>
		</tr>
		</table>
		<hr/>
		<p><b><a name="trueServ">真实服务器</a></b>：</p>
		<p>该页面用于配置一个真实服务器。</p>
		<table cellspacing="0" cellpadding="4" border="0" width="100%">
		<tr>
			<td width="80" valign="top" class="italicCell">选择虚拟服务</td>
			<td class="normalCell">为添加的真实服务器选择一个虚拟服务。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">真实服务器名称</td>
			<td class="normalCell">设定简洁明了的真实服务器名称，只能包括英文字母、数字和下划线。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">真实服务器地址</td>
			<td class="normalCell">真实服务器的IP地址。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">转发方式</td>
			<td class="normalCell">从调度器转发数据到真实服务器有三种方式，分别是：网络地址转换、IP隧道、直接路由。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">权重</td>
			<td class="normalCell">当选择了包含权重的调度算法，则权重值生效。权值最大设置为65535。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">真实服务器端口</td>
			<td class="normalCell">仅当转发方式为网络地址转换时，才允许设置真实服务器端口，此时设置的端口起到端口转发的作用。</td>
		</tr>
		</table>
		<hr/>
		<p><b><a name="trueServ">服务器状态</a></b>：</p>
		<p>该页面用于检测真实服务器并显示其状态</p>
		<table cellspacing="0" cellpadding="4" border="0" width="100%">
		<tr>
			<td width="80" valign="top" class="italicCell">检测间隔</td>
			<td class="normalCell">相邻两次检测后端真实服务器的间隔时间。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">超时时间</td>
			<td class="normalCell">检测信号发出后等待响应的时间。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">重试次数</td>
			<td class="normalCell">服务器未响应检测信号后，重复检测的次数。</td>
		</tr>		
		<tr>
			<td width="80" valign="top" class="italicCell">检测方式</td>
			<td class="normalCell">ping检测方式、端口检测方式。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">检测端口</td>
			<td class="normalCell">使用端口检测方式，则需要填写响应的端口号。</td>
		</tr>
		</table>
		<hr/>
		<p><b><a name="sirtualServ"><font color="#CC0088">链路负载</font></a></b></p>
		<hr size="1" color="#0099DD"/>
		<p><b><a name="sirtualServ">ISP地址段</a></b>：</p>
		<p>该页面用于配置一个ISP地址段，ISP地址段在智能DNS和智能路由中使用。</p>
		<table cellspacing="0" cellpadding="4" border="0" width="100%">
		<tr>
			<td width="80" valign="top" class="italicCell">ISP导入</td>
			<td class="normalCell">可以通过导入功能，从本地主机导入txt格式ISP地址段。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">运营商</td>
			<td class="normalCell">设定该ISP地址段的运营商名称。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">ISP配置</td>
			<td class="normalCell">配置该ISP的地址列表。</td>
		</tr>
		</table>
		<hr/>
		<p><b><a name="trueServ">智能DNS-DNS策略</a></b>：</p>
		<p>智能DNS主要应用在链路入站负载均衡，是指从Internet发起向内网服务器访问的负载均衡。负载均衡系统根据用户访问来源记录用户所属的ISP运营商，由KLB系统返回相应的解析地址，供用户访问。</p>
		<table cellspacing="0" cellpadding="4" border="0" width="100%">
		<tr>
			<td width="80" valign="top" class="italicCell">域名</td>
			<td class="normalCell">用于配置DNS策略的域名。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">状态</td>
			<td class="normalCell">用于配置DNS策略的开启或关闭状态。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">别名</td>
			<td class="normalCell">用于配置DNS策略的域名对应的别名。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">ISP名称</td>
			<td class="normalCell">ISP名称是ISP地址段中配置的ISP列表名称，配置了ISP地址段后，在该下拉菜单中会自动出现对应的名称。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">DNS回应</td>
			<td class="normalCell">用于配置DNS策略的返回IP，包括返回所有IP和按策略返回链路的所有IP。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">记录生存时间</td>
			<td class="normalCell">用于配置DNS策略的域名记录的缓存时间，单位为秒。</td>
		</tr>
		</table>
		<hr/>
		<p><b><a name="trueServ">智能DNS-服务器设置</a></b>：</p>
		<p>该页面用于设置DNS服务器的相关属性。</p>
		<table cellspacing="0" cellpadding="4" border="0" width="100%">
		<tr>
			<td width="80" valign="top" class="italicCell">服务器状态</td>
			<td class="normalCell">用于配置DNS服务器的开启或关闭状态。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">DNS端口</td>
			<td class="normalCell">用于配置DNS服务器提供服务的TCP和UDP端口，默认为53。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">支持反向查询</td>
			<td class="normalCell">用于启用或禁用通过IP查询域名的反向查询。</td>
		</tr>
		</table>
		<hr/>
		<p><b><a name="netDdos">智能路由</a></b>：</p>
		<p>可以实现基于目标IP的智能路由，查询目标IP所属的ISP网段，走对应的接口转发数据包。</p>
		<table cellspacing="0" cellpadding="4" border="0" width="100%">
		<tr>
			<td width="80" valign="top" class="italicCell">描述</td>
			<td class="normalCell">可自定义该智能路由的描述信息。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">网关策略</td>
			<td class="normalCell">设置该智能路由条目的网关策略，仅在配置了多出口网关时生效。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">ISP名称</td>
			<td class="normalCell">ISP名称是ISP地址段中配置的ISP列表名称，配置了ISP地址段后，在该下拉菜单中会自动出现对应名字。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">路由网关</td>
			<td class="normalCell">设置该智能路由条目的网关、接口及权重，权重的设置仅在配置了多出口网关时生效。</td>
		</tr>
		</table>
		<hr/>
		<p><b><a name="hostname"><font color="#CC0088">高可用管理</font></a></b></p>
		<hr size="1" color="#0099DD"/>
		<p><b><a name="hostname">主机名配置</a></b>：</p>
		<p>该页面用于对本调度器的主机名进行配置。</p>
		<table cellspacing="0" cellpadding="4" border="0" width="100%">
		</table>
		<hr/>
		<p><b><a name="sedundant">高可用配置</a></b>：</p>
		<p>该页面用于设置高可用参数，通过设置能够实现高可用功能。</p>
		<table cellspacing="0" cellpadding="4" border="0" width="100%">
		<tr>
			<td width="80" valign="top" class="italicCell">高可用状态</td>
			<td class="normalCell">用于开启或关闭高可用状态。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">心跳检测接口</td>
			<td class="normalCell">心跳检测需要启用的接口。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">心跳通讯端口</td>
			<td class="normalCell">心跳检测需要启用的端口。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">对方IP地址</td>
			<td class="normalCell">用于配置高可用时，另一台调度器的IP地址。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">对方主机名</td>
			<td class="normalCell">用于配置高可用时，另一台调度器的主机名。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">主备连接同步ID</td>
			<td class="normalCell">当使用主备模式高可用时，配置相同的ID号，可以启动连接同步，保证数据同步。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">心跳间隔时间</td>
			<td class="normalCell">每隔设定时间发送一次心跳。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">发出警告时间</td>
			<td class="normalCell">当设定时间过后，仍未收到心跳响应则发出警告。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">死亡判定时间</td>
			<td class="normalCell">当设定时间过后，仍未收到心跳响应则判定死亡。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">启动等待时间</td>
			<td class="normalCell">当启用高可用集群时，需要等待设定时间后才能完全生效，保证网络启动的正确性。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">资源是否迁回</td>
			<td class="normalCell">当某台死亡的高可用调度器再次启动时，是否从备份调度器抢夺回资源。</td>
		</tr>
		</table>
		<hr/>
		<p><b><a name="scheduler">高可用检测</a></b>：</p>
		<p>可以添加或删除用于高可用检测的域名地址。</p>
		<table cellspacing="0" cellpadding="4" border="0" width="100%">
		</table>
		<hr/>
		<p><b><a name="netDdos"><font color="#CC0088">安全管理</font></a></b></p>
		<hr size="1" color="#0099DD"/>
		<p><b><a name="netDdos">DDOS攻击防护</a></b>：</p>
		<p>防止恶意用户使用DDOS攻击调度器。</p>
		<table cellspacing="0" cellpadding="4" border="0" width="100%">
		<tr>
			<td width="80" valign="top" class="italicCell">开启</td>
			<td class="normalCell">开启DDOS攻击防护功能。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">关闭</td>
			<td class="normalCell">关闭DDOS攻击防护功能。</td>
		</tr>
		</table>
		<hr/>
		<p><b><a name="netAccess">访问控制</a></b>：</p>
		<p>通过设置可以实现黑名单功能，禁止某些地址、协议或端口对本调度器进行访问。</p>
		<table cellspacing="0" cellpadding="4" border="0" width="100%">
		<tr>
			<td width="80" valign="top" class="italicCell">源地址</td>
			<td class="normalCell">设定一个需要屏蔽的源IP地址及其掩码。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">目的地址</td>
			<td class="normalCell">设定一个需要屏蔽的目的IP地址及其掩码。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">协议</td>
			<td class="normalCell">设定一个需要屏蔽的协议。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">源端口</td>
			<td class="normalCell">设定一个需要屏蔽的源端口。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">目的端口</td>
			<td class="normalCell">设定一个需要屏蔽的目的端口。</td>
		</tr>
		</table>
		<hr/>
		<p><b><a name="serviceStatistics"><font color="#CC0088">统计日志</font></a></b></p>
		<hr size="1" color="#0099DD"/>
		<p><b><a name="serviceStatistics">服务转发统计</a></b>：</p>
		<p>该页面用于统计虚拟服务转发数据。</p>
		<table cellspacing="0" cellpadding="4" border="0" width="100%">
		<tr>
			<td width="80" valign="top" class="italicCell">虚拟服务</td>
			<td class="normalCell">选择一个需要统计的虚拟服务。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">起始日期</td>
			<td class="normalCell">选择一个起始日期。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">起始时间</td>
			<td class="normalCell">选择一个具体的起始时间。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">结束日期</td>
			<td class="normalCell">选择一个结束日期。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">结束时间</td>
			<td class="normalCell">选择一个具体的结束时间。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">时间间隔</td>
			<td class="normalCell">数据统计单位。包括：分、小时、天、月。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">导出日志</td>
			<td class="normalCell">将统计数据保存在本地。</td>
		</tr>
		</table>
		<hr/>
		<p><b><a name="serverStatistics">服务器转发统计</a></b>：</p>
		<p>该页面用于统计真实服务器转发数据。</p>
		<table cellspacing="0" cellpadding="4" border="0" width="100%">
		<tr>
			<td width="80" valign="top" class="italicCell">真实服务器</td>
			<td class="normalCell">选择一个需要统计的真实服务器。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">起始日期</td>
			<td class="normalCell">选择一个起始日期。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">起始时间</td>
			<td class="normalCell">选择一个具体的起始时间。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">结束日期</td>
			<td class="normalCell">选择一个结束日期。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">结束时间</td>
			<td class="normalCell">选择一个具体的结束时间。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">时间间隔</td>
			<td class="normalCell">数据统计单位。包括：分、小时、天、月。</td>
		</tr>
		<tr>
			<td width="80" valign="top" class="italicCell">导出日志</td>
			<td class="normalCell">将统计数据保存在本地。</td>
		</tr>
		</table>
		<hr/>
	</body>
</html>
