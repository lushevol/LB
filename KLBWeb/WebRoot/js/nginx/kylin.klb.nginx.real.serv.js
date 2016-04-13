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

if (!window.kylin.klb.nginx) {
	window.kylin.klb.nginx = {};
}

if (!window.kylin.klb.nginx.real) {
	window.kylin.klb.nginx.real = {};
}

if (!window.kylin.klb.nginx.real.serv) {
	window.kylin.klb.nginx.real.serv = {};
}


//虚拟服务配置
$(document).ready(function(){
	
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
		return this.optional(element) || ( /^(\d+(-\d+)?,)*(\d+(-\d+)?)$/.test(value) );   
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
     		kylin.klb.nginx.real.serv.savess();
     		//form.submit();
     	},
		rules: {
			name: { required:true, isStr:true },
			//dev: { required:true },
			//interfaces: { required:true },
			method: { required:true},
			//listenPort: { required:true, isPort: true },			
		},
		messages: {
			name:{ required: "真实服务器组名称 不能为空", isStr:"真实服务器组名称 只能包括英文字母、数字和下划线" },
			//dev:{ required: "高可用类型 不能为空" },
			//interfaces:{ required: "接 口 不能为空" },
			method:{ required: "负载算法 不能为空"},
			//listenPort: { required:"监听端口不能为空", isPort: "监听端口输入不合法" },					
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
		$("#rsgId").val("0");	
		$("#update").val("0");		
		$("#oldName").val("");	
		kylin.klb.nginx.real.serv.formenable();
		kylin.klb.nginx.real.serv.formreset();
		kylin.klb.nginx.real.serv.popbox();
		
	});
	
	$('#save_scheduler').click(function(){
		return kylin.klb.nginx.real.serv.checkSave();
	});
	
	
});

(function(package){
	jQuery.extend(package, {
		//设置弹出层
		setss : function (jsonp){
			$("#update").val("1");
			$("#rsgId").val(jsonp.rsgId);	
			$("#oldName").val(jsonp.name);
			
			package.formdisabled();
			kylin.klb.nginx.real.serv.popbox();
			//alert($("#oldName").val());
			//alert($("#update").val());
			package.message("正在加载数据....");
			if(typeof jsonp != "undefined") {
				$.ajax({
					type: "POST",
					url: "nginx-real-serv!input.action",
					data: {"rsgId":jsonp.rsgId},
					dataType : 'json',
					timeout : 20000,
					success: function(json){
						if(typeof json != "undefined") {
							var obj = json.obj;
							if(json.auth == true) {
								if(typeof obj != "undefined") {
									package.formenable();
									//package.formreset();												
									$("#rsgId").val(obj.rsgId);
									$("#name").val(obj.name);																										
									$("#method").val(obj.method);																											
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
			
			
			//alert($("#oldName").val());
			//alert($("#update").val());
			var jsonDate = {
				"rsgId":$("#rsgId").val(),
				"name":$("#name").val(),
				"method":$("#method").val(),
				"update":$("#update").val(),
				"oldName":$("#oldName").val()			
			};
			
			$.ajax({
				type: "POST",
				url: "nginx-real-serv!save.action",
				data: jsonDate,
				dataType : 'json',
				timeout : 20000,
				success: function(json){
					if(typeof json != "undefined") {
						$("#save_scheduler").attr("disabled",false);
						if(json.auth == true) {							
							location.reload();
						} else {
							//alert(json.mess);
							//location.reload();
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
						url: "nginx-real-serv!delete.action",
						data: {"rsgId":jsonp.rsgId},
						dataType : 'json',
						timeout : 20000,
						success: function(json){
							if(typeof json != "undefined") {
								if(json.auth == true) {									
									location.reload();
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
		}
	});
})(kylin.klb.nginx.real.serv);
