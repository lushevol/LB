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
<script type="text/javascript" src="${ctx}/js/kylin.klb.upload.js"></script>
<script type="text/javascript" src="${ctx}/js/kylin.klb.user.js"></script>
<script type="text/javascript">
var uploadObj = null;
$(document).ready(function() {
	var upload = new kylin.klb.upload.Upload({
		fileDom:"#upload",
		iframeDom:"#fileiframe",
		fileFormDom:"#filefrom",
		errorDom:"#error",
		fileType:"xml"
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
<div id="mark"></div>
<div class="popLayer popDiv" id="loading" style="display:none;"></div>
<!--面板开始-->
<div class="mainPartPanel">
  <div class="circle"></div>&nbsp;
</div>
<!--面板结束-->
<!--主要内容开始-->
<iframe style='display:none' id='fileiframe' name='fileiframe'></iframe>
<div class="mainPartContNoTop">
  <div class="box3"><h3><span>配置保存/重载</span></h3></div>
  	<br/>
	<table cellpadding="0" class="table">
		<tr>
			<th width="260">
				<a href="javascript:void(0);" class="btnStyle" onclick="kylin.klb.user.configCommit(this);"><span>保&emsp;存</span></a>				
			</th>
			<td>&emsp;&emsp;&emsp;&emsp;
				<a href="javascript:void(0);" class="btnStyle btnGray" onclick="kylin.klb.user.reloadCommit(this);"><span>重&emsp;载</span></a>
			</td>
		</tr>
	</table>
					
  <div class="box3"><h3><span>配置文件</span></h3></div>
	<!--配置文件开始-->
	<div class="fileCont">
	<div id="error" style="color:red;">&nbsp;<s:actionmessage theme="mytheme"/></div>
	    <s:form action="/import.action" method="post" enctype="multipart/form-data" id="filefrom" target="fileiframe">
		    <input type="hidden" name="jsCallBack" value="uploadCbk"/>
		    <tr>
		      <td><label>导入路径：</label></td>
		      <td><input type="file" id="upload" name="upload" size="40" style="height:22px"/></td>
		      <td><a href="javascript:void(0);" class="btnStyle" id="import"><span>导入</span></a></td>
		    </tr>
		    <tr>
		      <td><br /></td>
		    </tr>
		    <tr>
		    	<td><label>配置导出：</label></td>
		    	<td><a href="export.action" class="btnStyle"><span>下载至本地</span></a></td>
			</tr>
	    </s:form>
	</div>
	<br/>
  	<!--配置文件结束-->  
  	<div class="txtCont">
	    <h4>说明：</h4>
	    <p>&emsp;保存：存储当前的系统配置。</p>
	    <p>&emsp;重载：放弃当前修改，恢复至所保存的配置。</p>
	    <p>&emsp;导入：放弃当前修改，导入备份的配置。</p>	    
	    <p>&emsp;导出：备份当前的系统配置，保存至本地。</p>
	</div>
</div>

<!--主要内容结束-->
</body>
</html>
