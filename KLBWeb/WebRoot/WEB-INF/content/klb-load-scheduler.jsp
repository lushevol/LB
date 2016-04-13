<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>无标题文档</title>
<%@ include file="/common/meta.jsp" %>
<link rel="stylesheet" type="text/css" href="${ctx}/css/skin/default/common.css"  />
<link rel="stylesheet" type="text/css" href="${ctx}/css/skin/default/module.css"  />
<script type="text/javascript" src="${ctx}/js/jquery.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
<script type="text/javascript" src="${ctx}/js/kylin.klb.common.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$('#inputForm').submit(function(){
		return check();
	})
	
	//调度器添加限制
	$("#add").click(function(){
		if ($("tbody tr.schedulerNum").length >= 2) {
			$('#addScheduler').hide();
			$('#mark').hide();
			alert("为保证Heartbeat高可用，不能添加超过两个调度器");
		}
	});
});

function reset1(){
	$("#name").val("");
	$("#show").html("");
}
function check(){
	var name =$("#name").val();
	if($.trim(name)=="") {
		$("#show").html("请输入调度器名称！");
		$("#name").focus();
		return false;
	}
	if( !(/^[\w]+[\.\w]*$/.test(name)) ) {
		$("#show").html("您输入的调度器名称不合法，请重新输入");
		$("#name").focus();
		return false;
	}
	$("#show").html("");
}
</script>
</head>
<body>
<!--面板开始-->
<div class="mainPartPanel">
  <div class="circle"></div>
  &nbsp; </div>
<!--面板结束-->
<!--主要内容开始-->
<div class="mainPartContNoTop">
  <!--文本内容区开始-->
  <div class="txtCont">
	
	<div class="box3">
		<h3><span>调度器管理</span></h3>
	</div>
	<br/>

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
            <span class="tit"><strong>调度器列表</strong></span><div align="left" id="message" style="float:left;color: red;padding-left:10px;"><s:actionmessage theme="mytheme"/></div>
            <span class="operate"><a href="####" class="addLink popDivBt" onclick="popbox('#addScheduler')" pop="#addScheduler" id="add">添加</a></span>
            </caption>
            <thead>
              <tr>
                <th width="180">调度器名称</th>
                <th class="last lb2">操作</th>
              </tr>
            </thead>
            <tbody>
            <s:iterator value="list" id="scheduler" status="index">
              <tr class="schedulerNum">
                <td><s:property value="%{schedulerName.startsWith('_')?schedulerName.substring(1,schedulerName.length()):schedulerName}"/></td>
                <td><a href="klb-load-scheduler!delete.action?schedulerName=${schedulerName}" 
                onclick="if(confirm('你确定要删除吗？')==false) return false;">删除</a></td>
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
  
    <h4>调度器管理页面说明：</h4>
    <p>&emsp;调度器管理页面与高可用和虚拟服务配置存在密切关系。在配置虚拟服务之前需要在此界面添加一台调度器，确保虚拟服务能够运行在一台调度器上。
    	同样，在开启高可用之前需要在此界面添加两台调度器，确保可完成高可用功能。用户填写的调度器名称需要和主机名一致，linux下查看主机名方法是：uname -n。</p>
  </div>
  <!--文本内容区结束-->
</div>
<!--主要内容结束-->

<!--弹出层警告开始-->
<!--调度器添加 -->
<div id="mark"></div>
<div class="popLayer popDiv" id="addScheduler">
  <div class="tt">
    <h3>调度器添加</h3>
    <div class="operate"><a href="####" class="btnClose" hider="#addScheduler" title="关闭">关闭</a></div>
  </div>
  
  <div class="ct">
    <div class="main">
      <div class="nrarea">
      	<form id="inputForm" name="inputForm" action="klb-load-scheduler!save.action" method="post">
        <div class="popTable">
		    <div id="show" style="color: red;"></div>
		    	<table width="100%" cellspacing="0">
		            <tr>
		              <th width="106">调度器名称：</th>
		              <td><input id="name" type="text" class="input" name="schedulerName"/></td>
		            </tr>
				</table>
		    </div>
	        
	        <div class="btnArea btnConfirm">
	          <button class="btn" type="submit" id="submitId"><span>保存</span></button>
	          <button type="reset" class="btn btnGray" onclick="reset1();"><span>重置</span></button>
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
<!--弹出层警告结束-->
</body>
</html>

