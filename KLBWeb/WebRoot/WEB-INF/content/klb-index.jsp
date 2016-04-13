<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${sessionScope.productName }</title>
<%@ include file="/common/meta.jsp" %>
<link href="${ctx}/css/skin/default/common.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/skin/default/module.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
<script type="text/javascript" src="${ctx}/js/kylin.klb.common.js"></script>
<script type="text/javascript" src="${ctx}/js/kylin.klb.user.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.epiclock.js"></script>
<script type="text/javascript" src="${ctx}/js/fc.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		setTimeout(function(){
			fc.iframeProxy({'action':'klb-sys-info.action'});
		}, 5);
	});
</script>

<style type="text/css">
html, body {s
	height:100%;
	overflow:hidden;
}
</style>
</head>
<body class="index">
<div id="mark"></div>
<div class="popLayer popDiv" id="loading" style="display:none;"></div>
<!--弹出层警告结束-->
<!--头部开始-->
<%@ include file="/common/klb-header.jsp" %>
<!--头部结束-->
<div class="center">
  <!--左侧菜单开始-->
  <div class="sidePart">
    <div class="navWrap">
      <!--一级菜单开始-->
      <ul class="navFirst">
        <!--系统管理-->
        <li>
          <h3 class="first"><a href="javascript:;" onclick="fc.iframeProxy({'action':'klb-sys-info.action'})"><img src="css/skin/images/common/ico2.gif" alt="" />系统管理</a></h3>
          <!--二级菜单开始-->
          <ul class="navSec">
            <li><a href="javascript:;" onclick="fc.iframeProxy({'action':'klb-sys-info.action'})">系统信息</a></li>
            <%-- <li><a href="javascript:;" onclick="fc.iframeProxy({'action':'klb-sys-check.action'})">系统自检</a></li>--%>
            <li><a href="javascript:;" onclick="fc.iframeProxy({'action':'klb-sys-files.action'})">配置管理</a></li>
 <s:if test="#session.isSoftVersion.equals('false')">
            <li><a href="javascript:;" onclick="fc.iframeProxy({'action':'klb-sys-upgrade.action'})">系统升级</a></li>
 </s:if>
            <li><a href="javascript:;" onclick="fc.iframeProxy({'action':'klb-sys-time!input.action'})">系统时间</a></li>
            <li><a href="javascript:;" onclick="fc.iframeProxy({'action':'klb-sys-log!input.action'})">系统日志</a></li>
            <li><a href="javascript:;" onclick="fc.iframeProxy({'action':'klb-sys-mail!input.action'})">邮件管理</a></li>
          </ul>
          <!--二级菜单结束-->
        </li>
 <s:if test="#session.isSoftVersion.equals('false')">
        <!--网络管理-->
        <li>
          <h3><a href="javascript:;" onclick="fc.iframeProxy({'action':'network/interface-physics!list.action'})"><img src="css/skin/images/common/ico1.gif" alt="" />网络管理</a></h3>
          <!--二级菜单开始-->
          <ul class="navSec">
          	<li><a href="javascript:;" onclick="fc.iframeProxy({'action':'network/interface-physics!list.action'})">接口配置</a></li>
            <li><a href="javascript:;" onclick="fc.iframeProxy({'action':'network/route-static!list.action'})">路由配置</a></li>
            <li><a href="javascript:;" onclick="fc.iframeProxy({'action':'network/dns!input.action'})">域名服务器</a></li>
            <%-- <li><a href="javascript:;" onclick="fc.iframeProxy({'action':'network/dhcp!list.action'})">DHCP配置</a></li> --%>
            <li><a href="javascript:;" onclick="fc.iframeProxy({'action':'network/adsl!list.action'})">ADSL配置</a></li>
            <li><a href="javascript:;" onclick="fc.iframeProxy({'action':'network/arp!list.action'})">ARP配置</a></li>
            <li><a href="javascript:;" onclick="fc.iframeProxy({'action':'firewall/address!list.action'})">地址转换</a></li>
          </ul>
          <!--二级菜单结束-->
        </li>
 </s:if>
        <!--应用负载管理-->
        <li>
          <h3><a href="javascript:;" onclick="fc.iframeProxy({'action':'klb-load-sirtual-serv.action'})"><img src="css/skin/images/common/ico3.gif" alt="" />应用负载</a></h3>
          <!--二级菜单开始-->
          <ul class="navSec">
            <li><a href="javascript:;" onclick="fc.iframeProxy({'action':'klb-load-sirtual-serv.action'})">虚拟服务</a></li>
            <li><a href="javascript:;" onclick="fc.iframeProxy({'action':'klb-load-true-serv.action'})">真实服务器</a></li>
            <li><a href="javascript:;" onclick="fc.iframeProxy({'action':'klb-servers-status.action'})">服务器状态</a></li>        
          </ul>
          <!--二级菜单结束-->
        </li>                 
        <!--七层负载管理-->
        <li>
          <h3><a href="javascript:;" onclick="fc.iframeProxy({'action':'nginx/nginx-virtual-serv.action'})"><img src="css/skin/images/common/ico3.gif" alt="" />七层负载</a></h3>
          <!--二级菜单开始-->
          <ul class="navSec">
            <li><a href="javascript:;" onclick="fc.iframeProxy({'action':'nginx/nginx-virtual-serv.action'})">虚拟服务</a></li>
            <li><a href="javascript:;" onclick="fc.iframeProxy({'action':'nginx/nginx-real-serv.action'})">真实服务器</a></li>
            <li><a href="javascript:;" onclick="fc.iframeProxy({'action':'nginx/nginx-conf.action'})">服务器状态</a></li>        
          </ul>
          <!--二级菜单结束-->
        </li>        
 <s:if test="#session.isSoftVersion.equals('false')">      
        <!--链路负载管理-->
        <li>
          <h3><a href="javascript:;" onclick="fc.iframeProxy({'action':'linkload/dns-isp!list.action'})"><img src="css/skin/images/common/ico3.gif" alt="" />链路负载</a></h3>
          <!--二级菜单开始-->
          <ul class="navSec">
            <li><a href="javascript:;" onclick="fc.iframeProxy({'action':'linkload/dns-isp!list.action'})">ISP地址段</a></li>
            <li><a href="javascript:;" onclick="fc.iframeProxy({'action':'linkload/dns-policy!list.action'})">智能DNS</a></li>
            <li><a href="javascript:;" onclick="fc.iframeProxy({'action':'network/route-smart!list.action'})">智能路由</a></li>
          </ul>
          <!--二级菜单结束-->
        </li>
 </s:if>       
        
 <s:if test="#session.isHaEnable.equals('true')">        
        <!--高可用管理-->
        <li>
	<s:if test="#session.isSoftVersion.equals('false')">
          <h3><a href="javascript:;" onclick="fc.iframeProxy({'action':'klb-load-hostname!input.action'})"><img src="css/skin/images/common/ico6.gif" alt="" />高可用管理</a></h3>
          <!--二级菜单开始-->
          <ul class="navSec">
 
            <li><a href="javascript:;" onclick="fc.iframeProxy({'action':'klb-load-hostname!input.action'})">主机名配置</a></li>

            <%-- <li><a href="javascript:;" onclick="fc.iframeProxy({'action':'klb-load-scheduler.action'})">调度器管理</a></li> --%>
          	<li><a href="javascript:;" onclick="fc.iframeProxy({'action':'klb-load-sedundant!input.action'})">高可用配置</a></li>
          	<li><a href="javascript:;" onclick="fc.iframeProxy({'action':'klb-load-pinggroups.action'})">高可用检测</a></li>          
          </ul>
           <!--二级菜单结束-->
    </s:if>
    <s:else> 
    	 <h3><a href="javascript:;" onclick="fc.iframeProxy({'action':'klb-load-sedundant!input.action'})"><img src="css/skin/images/common/ico6.gif" alt="" />高可用管理</a></h3>
          <!--二级菜单开始-->
          <ul class="navSec">
          	<li><a href="javascript:;" onclick="fc.iframeProxy({'action':'klb-load-sedundant!input.action'})">高可用配置</a></li>
          	<li><a href="javascript:;" onclick="fc.iframeProxy({'action':'klb-load-pinggroups.action'})">高可用检测</a></li>          
          </ul>
           <!--二级菜单结束-->
 	</s:else>      
        </li>
</s:if>

<s:if test="#session.isSoftVersion.equals('false')">  
        <!--安全管理-->
        <li>
          <h3><a href="javascript:;" onclick="fc.iframeProxy({'action':'klb-network-ddos!input.action'})"><img src="css/skin/images/common/ico7.gif" alt="" />安全管理</a></h3>
          <!--二级菜单开始-->
          <ul class="navSec">
            <li><a href="javascript:;" onclick="fc.iframeProxy({'action':'klb-network-ddos!input.action'})">DDOS攻击防护</a></li>
            <li><a href="javascript:;" onclick="fc.iframeProxy({'action':'klb-network-access.action'})">访问控制</a></li>
          </ul>
          <!--二级菜单结束-->
        </li>
        <!--统计日志-->
        <li>
          <h3><a href="javascript:;" onclick="fc.iframeProxy({'action':'klb-statistics-service.action'})"><img src="css/skin/images/common/ico4.gif" alt="" />统计日志</a></h3>
          <!--二级菜单开始-->
          <ul class="navSec">
          	<li><a href="javascript:;" onclick="fc.iframeProxy({'action':'klb-statistics-service.action'})">服务转发统计</a></li>
            <li><a href="javascript:;" onclick="fc.iframeProxy({'action':'klb-statistics-server.action'})">服务器转发统计</a></li>
          </ul>
          <!--二级菜单结束-->
        </li>
        <!--统计日志-->
        <%-- <li>
          <h3><a href="javascript:;" onclick="fc.iframeProxy({'action':'klb-user.action'})"><img src="css/skin/images/common/ico5.gif" alt="" />用户管理</a></h3>
          <!--二级菜单开始-->
          <ul class="navSec">
            <li><a href="javascript:;" onclick="fc.iframeProxy({'action':'klb-user.action'})">修改密码</a></li>
          </ul>
          <!--二级菜单结束-->
        </li> --%>
  </s:if>
      </ul>
      <!--一级菜单结束-->
    </div>
  </div>
  <!--左侧菜单结束-->
  <!--中间内容开始-->
  <div class="mainPart">
    <iframe src="" id="mainMidFrameId" frameborder="0" name="mainMidFrame" height="400" width="900" class="all" scrolling="yes"></iframe>
    <!--底部开始-->
    <div class="mainPartBot">
      <p></p>
    </div>
    <!--底部结束-->
  </div>
  <!--中间内容结束-->
</div>
<!--底部版权开始-->
<%@ include file="/common/klb-footer.jsp" %>
<!--底部版权结束-->
</body>
</html>
