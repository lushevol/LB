<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>无标题文档</title>
<%@ include file="/common/meta.jsp" %>
<link rel="stylesheet" type="text/css" href="${ctx}/css/skin/default/common.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/skin/default/module.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/skin/default/subindex.css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.pack.js"></script>
<script src="${ctx}/js/validate/jquery.validate.js" type="text/javascript"></script>
<script src="${ctx}/js/validate/messages_cn.js" type="text/javascript"></script>
<script src="${ctx}/js/kylin.klb.trueserv.js" type="text/javascript"></script>
</head>

<body>
<div id="mark"></div>
<!--真实服务器配置 -->
<div class="popLayer popDiv" id="addScheduler">
	<div class="tt">
    	<h3>真实服务器</h3>
    	<div class="operate"><a href="javascript:;" class="btnClose" hider="#addScheduler" title="关闭">关闭</a></div>
  	</div>
  	<div class="ct">
	    <div class="main">
			<div class="nrarea">
				<form id="inputForm" name="inputForm" method="post">
		        	<input type="hidden" id="update" name="update" value="0"/>
        			<input type="hidden" id="oldServiceName" name="oldServiceName" value=""/>
        			<input type="hidden" id="vsId" name="vsId" />
        			<input type="hidden" id="tsId" name="tsId" />
		        	<div class="popTable">
			        	<div id="show" style="color: red;"></div>
			          	<table width="100%" cellspacing="0">
				            <tr id="servicetr">
								<th>选择虚拟服务：</th>
			              		<td>
			              		 <s:select list="#request.serviceList" theme="simple" name="service" id="service" 
			              			headerKey="" headerValue="-空-" listKey="key" listValue="value">
			              		 </s:select>
<%--			              			<select name="service" id="service">--%>
<%--			                  			<option value="">请选择</option>--%>
<%--			                  			<c:forEach var="ser" items="${serviceList}">--%>
<%--			                     			<option value="${ser}">--%>
<%--			                     			<s:property value="%{#ser.startsWith('_')?#ser.substring(1,#ser.length()):#ser}"/>--%>
<%--			                     			</option>--%>
<%--			                  			</c:forEach>--%>
<%--			                		</select>--%>
			                	</td>
			            	</tr>
				            <tr>
				              <th>真实服务器名称：</th>
				              <td><input type="text" class="input" name="serviceName" id="serviceName"/></td>
				            </tr>
				            <tr>
				              <th>真实服务器地址：</th>
				              <td><input type="text" class="input" name="ip" id="ip"/></td>
				            </tr>
				            <tr>
				              <th>转发方式：</th>
				              <td>
				              	<s:select list="#request.forwards" theme="simple" name="forward" id="forward" 
							    	headerKey="" headerValue="-空-" listKey="key" listValue="value" >
							    </s:select>
				              </td>
				            </tr>
				            <tr>
				              <th>权重：</th>
				              <td><input type="text" class="input" name="weight" id="weight"/></td>
				            </tr>
				            <tr>
				              <th><label class="portGray">真实服务器端口：</label></th>
				              <td><input type="text" class="input" name="mapport" id="mapport"/></td>
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
	<div class="circle"></div>&nbsp;
</div>
<!--面板结束-->
<!--主要内容开始-->
<div class="mainPartContNoTop">
	<div class="box3"><h3><span>真实服务器</span></h3></div>
	<br/>
	<!--列表开始-->
  	<div class="tableWrap trueServ">
    	<!--表格外层左侧边框-->
		<div class="ct">
      	<!--表格外层右侧边框-->
      		<div class="main">
        		<!--列表主要内容开始-->
        		<div class="tableList vTopTd">
          			<table>
            			<caption>
            				<span class="tit"><strong>真实服务器列表</strong></span>
            				<div id="message" style="float:left;color: red;padding-left:10px;"></div>
							<span class="operate">
								<a href="javascript:;" class="addLink popDivBt" id="add">添加</a>
							</span>
            			</caption>
			            <thead>
				        	<tr>				       			
				                <th width="80">虚拟服务名称</th>
				                <th width="160">虚拟服务地址 / 端口</th>				                
				                <th width="90">真实服务器名称</th>				                
				                <th width="160">真实服务器地址 / 端口</th>
				                <th width="90">转发方式</th>
				                <th width="50">权重</th>
				                <th width="30">状态</th>
				                <th class="last lb2">操作</th>
				 			</tr>
			            </thead>
            			<tbody>
				            <s:iterator value="loadTrueServ" status="count">
								<tr>					                
					                <td>${service}</td>
					                <td>${vipMark}
					                	<s:if test="%{ vsPort != null && vsPort != '' }">: ${vsPort}</s:if>
					                </td>
					                <td>${serviceName}</td>					                
					                <td>${ip}
					                	<s:if test="%{ mapport != null && mapport != '' && forward == 1 }">: ${mapport}</s:if>
					                	<s:if test="%{ vsPort != null && vsPort != '' && forward != 1 }">: ${vsPort}</s:if>
					                </td>
					                <td>
					                	<s:if test="%{forward == 0}">直接路由</s:if>
					                	<s:if test="%{forward == 1}">网络地址转换</s:if>
					                	<s:if test="%{forward == 2}">IP隧道</s:if>					                
					                </td>
					                <td>${weight}</td>
					                <td class="stc">
										<s:if test="%{status == 'true'}"><font color="#008000">开启</font></s:if>
										<s:else><font color="#FF0000">关闭</font></s:else>
									</td>
					                <td>
					                	<span><a href="javascript:;" onclick="kylin.klb.trueserv.startss(this,{'vsId':'${vsId}', 'tsId':'${tsId}'})">
											<s:if test="%{status == 'true'}">关闭</s:if> <s:else>开启</s:else></a>
										</span>
					                	<a href="javascript:;" class="popDivBt" onclick="kylin.klb.trueserv.setss({'serviceName':'${serviceName}','vsId':'${vsId}', 'tsId':'${tsId}'})">设置</a>
						                <span>
						                	<a href="javascript:;" onclick="kylin.klb.trueserv.delss(this,{'vsId':'${vsId}','tsId':'${tsId}'})">删除</a>
						                </span>
						                <br />
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
    <%-- <h4>说明：</h4>
    <p>&emsp;真实服务器是真实服务的载体，用户访问虚拟服务，最后都会被转发到真实服务器的服务上。</p> --%>
  </div>
</div>
<!--主要内容结束-->
</body>
</html>
