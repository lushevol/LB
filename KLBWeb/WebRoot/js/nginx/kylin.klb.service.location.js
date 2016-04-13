/**
 * @fileOverview 麒麟天平负载均衡系统
 * 虚拟服务管理
 * @author kylin
 */
 
if (!window.kylin) {
	window.kylin = {};
}
if (!window.kylin.klb) {
	window.kylin.klb = {};
}

if (!window.kylin.klb.service) {
	window.kylin.klb.service = {};
}

if (!window.kylin.klb.service.location) {
	window.kylin.klb.service.location = {};
}




//虚拟服务配置
$(document).ready(function(){

	var statu = $("input[name='virtualService.sslStatu']:checked").val() + "";
	window.kylin.klb.service.location.setSslInputEnabled(statu);	
	
	var statu = $("input[name='virtualService.cookieEnabled']:checked").val() + "";
	window.kylin.klb.service.location.setCookieInputEnabled(statu);

	
	jQuery.validator.addMethod("isip", function(value, element) {   
		return this.optional(element) || (/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/.test(value) && (RegExp.$1 <256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256));   
	}, "请输入合法的IP信息");
	jQuery.validator.addMethod("firstip", function(value, element) {   
		return this.optional(element) || (/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/.test(value) && (RegExp.$1 > 0 && RegExp.$1 < 224));   
	}, "请输入合法的IP首位信息");
	jQuery.validator.addMethod("isStr", function(value, element) {
		return this.optional(element) || /^[\w]+$/.test(value);
	}, "只能包括英文字母、数字和下划线");
	jQuery.validator.addMethod("isPort", function(value, element) {
		return this.optional(element) || (/^(\d+)$/.test(value)) && ( RegExp.$1 <= 65535 );   
	}, "端口输入不合法");
	
	$.validator.setDefaults({
		invalidHandler: function(form, validator) {
	    	$.each(validator.invalid,function(key,value){
	            tmpkey = key;
	            tmpval = value;
	            validator.invalid = {};
	            validator.invalid[tmpkey] = value;
	            $("#show").html(value).hide().fadeIn();
	            $("#"+key).focus();
	            return false;
	    	});
		},
		errorPlacement:function(error, element) {},
	    onkeyup: false,
	    onfocusout:false,
	    focusInvalid: true
	});
	//为inputForm注册validate函数
	$("#inputForm").validate({
		submitHandler: function(form) {
     		kylin.klb.service.location.savess();
     		//form.submit();
     	},
		rules: {
			macth: { required:true, isStr:true },			
			groupName: { required:true, isStr:true },	
		},
		messages: {
			macth:{ required: "macth 不能为空", isStr:"macth 只能包括英文字母、数字和下划线" },
			groupName:{ required: "真实服务器组名 不能为空", isStr:"真实服务器组名 只能包括英文字母、数字和下划线" },				
		}
	});
	
	
		
	//弹出层关闭按钮
	$("input[type=button],a,button").live("click", function(){
		if($(this).attr("hider")){
			var closeWath = $(this).attr("hider");
			$(closeWath).hide();
			$('#mark').hide();
		}
	});
	
	//添加弹出层
	$("#add").click(function(){		
		$("#update").val("0");		
		$("#locationId").val("");	
		$("#insertTr").show();
		kylin.klb.service.location.formenable();
		kylin.klb.service.location.formreset();
		kylin.klb.service.location.popbox();
		
	});
	
	$('#save_scheduler').click(function(){
		return kylin.klb.service.location.checkSave();
	});
	
	$('#upload').click(function(){
		if (kylin.klb.service.location.perpImportFile()) {
				$("#uploadForm").submit();
		}
	});
		
});

(function(package){
	jQuery.extend(package, {
		//设置弹出层
		setss : function (jsonp){
			$("#update").val("1");
			$("#locationId").val(jsonp.locationId);
			package.formdisabled();
			kylin.klb.service.location.popbox();
			$("#insertTr").hide();
			
			package.message("正在加载数据....");
			if(typeof jsonp != "undefined") {
				$.ajax({
					type: "POST",
					url: "service-location!input.action",
					data: {"vsId":jsonp.vsId, "locationId":jsonp.locationId},
					dataType : 'json',
					timeout : 20000,
					success: function(json){
						if(typeof json != "undefined") {
							var obj = json.obj;
							if(json.auth == true) {
								if(typeof obj != "undefined") {
									package.formenable();												
									$("#match").val(obj.match);
									$("#groupName").val(obj.groupName);																																																				
									package.message("");
								}
							} else {
								package.message("加载数据时出现错误");
							}
						}
					},
					error : function(){
						package.message("加载数据时出现错误");
					}
				});
			} else {
				package.message("加载数据时出现错误");
			}
		},
		savess : function () {
			$("#save_scheduler").attr("disabled",true);
			package.message("正在保存数据...");
								
			var jsonDate = {
				"vsId":$("#vsId").val(),
				"locationId":$("#locationId").val(),
				"match":$("#match").val(),
				"groupName":$("#groupName").val(),	
				"insertId":$("#insertId").val(),			
				"update":$("#update").val()			
			};
			
			$.ajax({
				type: "POST",
				url: "service-location!save.action",
				data: jsonDate,
				dataType : 'json',
				timeout : 20000,
				success: function(json){
					if(typeof json != "undefined") {
						$("#save_scheduler").attr("disabled",false);
						if(json.auth == true) {							
							//location.reload();
							//location.href = location.href;
							package.reflashPage();
						} else {
							//alert(json.mess);
							//location.reload();
							//location.href = location.href;
							//package.reflashPage();
							package.message(json.mess);
							$("#save_scheduler").attr("disabled",false);
						}
					} else {
						package.message("保存数据失败");
						$("#save_scheduler").attr("disabled",false);
					}
				},
				error : function(){
					package.message("保存数据失败");
					$("#save_scheduler").attr("disabled",false);
				}
			})
		},
		
		delss : function (obj,jsonp){
			if(confirm("确实要删除吗?")) {
				$("#message").html("正在删除...").hide().fadeIn();
				$(obj).parent().append("<a href=\"javascript:;\">正在删除...</a>");
				$(obj).html("");
				
				if(typeof jsonp != "undefined") {
					$.ajax({
						type: "POST",
						url: "service-location!delete.action",
						data: {"vsId":jsonp.vsId,"locationId":jsonp.locationId},
						dataType : 'json',
						timeout : 20000,
						success: function(json){
							if(typeof json != "undefined") {
								if(json.auth == true) {									
									//location.reload();
									//location.href = location.href;
									package.reflashPage();
								} else {
									$(obj).html("删除");
									$(obj).next().remove();
									$("#message").html("删除操作失败").hide().fadeIn().fadeOut(5000);
								}
							}
						}, error : function(){
							$(obj).html("删除");
							$(obj).next().remove();
							$("#message").html("删除操作失败").hide().fadeIn().fadeOut(5000);
						}
					});
				} else {
					$(obj).html("删除");
					$(obj).next().remove();
					$("#message").html("删除操作失败").hide().fadeIn().fadeOut(5000);
				}
			}
		},
		
		checkSave : function () {			
			var flag = true;			
			return flag;
		},
		formreset : function () {
			$("#inputForm")[0].reset();
			package.message("");			
		},
		formenable : function() {
			$("input").attr("disabled",false);
			$("button").attr("disabled",false);
			$("select").attr("disabled",false);
		},
		formdisabled : function() {
			$("input").attr("disabled",true);
			$("button").attr("disabled",true);
			$("select").attr("disabled",true);
		},
		popbox : function (){
			var popDiv = $("#addScheduler");
			if(!popDiv) return;
			
			var markLay = $("#mark")[0];
			if(typeof popDiv == "undefined" || typeof markLay == "undefined") {
				return;
			}
			markLay.style.display="block";
			popDiv.show();
			popDiv.css({
				"position":"absolute",
				"top":"50%",
				"left":"50%",
				"margin-left": -popDiv.width()/2,
				"margin-top": -popDiv.height()/2,
				"margin-bottom":"0",
				"z-index":"9999"
			})
			return false;
		},
		message : function(msg) {
			if($.trim(msg) == "") $("#show").html("");
			$("#show").html(msg).fadeIn();
		},
		
		uploadMessage : function(msg) {
			if($.trim(msg) == "") $("#uploadShow").html("");
			$("#uploadShow").html(msg).fadeIn();
		},
		
		sslStatuChange: function () {
			var statu = $("input[name='virtualService.sslStatu']:checked").val() + "";
			package.setSslInputEnabled(statu);	
			return true;
		},
		
		setSslInputEnabled: function(statu) {
			if(statu == "true"){
				$("#sslTimeout").attr('disabled',false);
				$("#certFile").attr('disabled',false);
				$("#keyFile").attr('disabled',false);
			}else{
				// $("#sslTimeout").val("0");
				
				$("#sslStatu").val(false);
				$("#sslTimeout").attr('disabled',true);
				$("#certFile").attr('disabled',true);
				$("#keyFile").attr('disabled',true);
			}
		},
		
		
		cookieEnabledChange: function () {
			var statu = $("input[name='virtualService.cookieEnabled']:checked").val() + "";
			package.setCookieInputEnabled(statu);
			return true;
		},
		
		setCookieInputEnabled: function(statu) {
			if(statu == "true"){
				$("#cookieName").attr('disabled',false);	
				$("#cookieExpire").attr('disabled',false);
			}else{
				//$("#cookieName").val("");
				//$("#cookieExpire").val("0");
				$("#cookieEnabled").val(false);
				$("#cookieName").attr('disabled',true);
				$("#cookieExpire").attr('disabled',true);
				
			}
		},
		
		sslTimeoutValidate: function (value) {
			return (/^(\d+)$/.test(value)) && ( RegExp.$1 <= 65535 );
		},
		
		cookieTimeoutValidate: function (value) {
			return (/^(\d+)$/.test(value)) && ( RegExp.$1 <= 65535 );
		},
	
		
		perpImportFile : function () {
			//alert($("#sslStatu").val());
			var sslTimeout = $("#sslTimeout").val();
			var cookieName = $("#cookieName").val();
			var cookieExpire = $("#cookieExpire").val();
			var certName = $("#certFile").val();
			var keyName = $("#keyFile").val();
			var importCert = false;
			var importKey = false;	
			if(sslTimeout != null && sslTimeout.length > 0) {
				if(!package.sslTimeoutValidate(sslTimeout)){
					package.uploadMessage("ssl超时时间只能输入数字, 最大值为65530");	
					return false;
				}
			}
			
			if(certName != null && certName.length > 0) {
				importCert = true;
				flag = package.checkCertName(certName);
				if(!flag){
					package.uploadMessage("ssl证书格式不正确");	
					return false;
				}	
			}		
			if(keyName != null && keyName.length > 0) {
				importKey = true;
				flag = package.checkKeyName(keyName);
				if(!flag){
					package.uploadMessage("ssl证书密钥格式不正确");	
					return false;
				}
			}	
			if(!importCert && importKey){
				package.uploadMessage("请选择ssl证书");	
				return false;
			}	
			if(importCert && !importKey){
				package.uploadMessage("请选择ssl证书密钥");	
				return false;
			}	
			
			if(cookieName != null && cookieName.length > 0) {
				if(!(/^[\w]+$/.test(cookieName))){
					package.uploadMessage("cookie名 只能包括英文字母、数字和下划线");	
					return false;
				}
			}
			
			if(cookieExpire != null && cookieExpire.length > 0) {
				if(!package.cookieTimeoutValidate(cookieExpire)){
					package.uploadMessage("cookie超时时间只能输入数字, 最大值为65530");	
					return false;
				}
			}
		
			return true;
		},
		
		checkCertName : function (certName) {		
			var fileter = new Array();
		   	fileter.push("text");
		   	fileter.push("cert");
		   	fileter.push("crt");   	
			for(var i = 0; i < fileter.length; i++) {
				var index = certName.lastIndexOf(fileter[i]);
		  		if(index != -1) {
			   		if ((certName.length - index - fileter[i].length) == 0) {   			
			   			return true;
			   		}
		   		}
		   	}	
			return false;
		},
		
		checkKeyName : function (keyName) {	
			var fileter = new Array();
		   	fileter.push("text");
		   	fileter.push("pem");
		   	fileter.push("key");
			for(var i = 0; i < fileter.length; i++) {
				var index = keyName.lastIndexOf(fileter[i]);
		  		if(index != -1) {
			   		if ((keyName.length - index - fileter[i].length) == 0) {
			   			
			   			return true;
			   		}
		   		}
		   	}
			return false;
		},
		//因为页面有表单 locatio.realod()或location.href会重新提交表单。改用此方法刷新页面
		reflashPage : function(){
			var vsId = $("#vsId").val();
			var vsName = $("#vsName").val(); 
			var tarPage = "service-location.action?vsId="+vsId+"&vsName="+vsName;
			$("#uploadForm").attr('disabled',true);
			window.location.href = tarPage;
			//location.href = location.href;
			//location.reload();
		}
		
	});
})(kylin.klb.service.location);
