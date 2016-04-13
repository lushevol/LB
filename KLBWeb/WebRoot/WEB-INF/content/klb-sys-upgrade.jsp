<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link rel="stylesheet" type="text/css" href="${ctx}/css/skin/default/common.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/skin/default/module.css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
<script type="text/javascript" src="${ctx}/js/kylin.object.js"></script>
<script type="text/javascript" src="${ctx}/js/kylin.klb.upgrade.js"></script>
<script type="text/javascript">
var uploadObj = null;
$(document).ready(function() {
	var upload = new kylin.klb.upgrade.Upload({
		fileDom:"#upload",
		iframeDom:"#fileiframe",
		fileFormDom:"#filefrom",
		errorDom:"#error",
		fileType:"pkg,xml"
	});
	uploadObj = upload;
	/***/
	$("#import").bind("click", function(){
			upload.upSubmit();
		}
	);
});
function uploadCbk(obj){
	if(typeof obj != "undefined") {
		uploadObj.cancle();
		$("#error").html(obj.msg);
	}
}

</script>

</head>
<body>
<!--面板开始-->
<div class="mainPartPanel">
  <div class="circle"></div>&nbsp;
</div>
<!--面板结束-->
<!--主要内容开始-->
<iframe style='display:none' id='fileiframe' name='fileiframe'></iframe>
<div class="mainPartContNoTop">
  <div class="box3">
	<h3><span>系统升级</span></h3>
  </div>
  <!--配置文件开始-->
  <div class="fileCont">
	<div id="error" style="color:red;">&nbsp;<s:actionmessage theme="mytheme"/></div>
	    <s:form action="/upgrade.action" method="post" enctype="multipart/form-data" id="filefrom" target="fileiframe">
		    <input type="hidden" name="jsCallBack" value="uploadCbk"/>
		    <tr>
		      <td><label>升级包路径：</label></td>
		      <td><input type="file" id="upload" name="upload" size="40" style="height:22px" /></td>
		      <td><a href="javascript:void(0);" class="btnStyle" id="import"><span>升级</span></a></td>
		    </tr>
		    <%-- <tr>
		      <td><br /></td>
		    </tr>
		    <tr>
		    	<td><label>导出升级包：</label></td>
		    	<td><a href="export.action" class="btnStyle"><span>配置导出</span></a></td>
			</tr> --%>
	    </s:form>
	</div>
  	<!--配置文件结束-->
  	<br />
  	<div class="txtCont">
	    <%-- <h4>系统升级说明：</h4>
	    <p>&emsp;系统升级。</p>
	    <p>&emsp;</p> --%>
	</div>
</div>

<!--主要内容结束-->
</body>
</html>
