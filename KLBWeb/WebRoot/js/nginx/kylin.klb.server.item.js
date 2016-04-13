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
	window.kylin.klb.server = {};
}

if (!window.kylin.klb.server.item) {
	window.kylin.klb.server.item = {};
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
     		kylin.klb.server.item.savess();
     		//form.submit();
     	},
		rules: {
			ip: { required:true, isip: true, firstip: true },
			port: { required:true, isPort: true },	
			weight: { required:true, digits:true },
			maxFails: { required:true, digits:true },
			failTimeout: { required:true, digits: true },
			type: { required:true, digits: true  },
			srunId: { isStr: true  }
		},
		messages: {
			ip: { required: "真实服务器地址 不能为空", isip:"真实服务器地址 IP不合法", firstip:"请输入合法的真实服务器IP首位（1~223）" },
			port: { required:"服务器端口 不能为空", isPort: "服务器端口 输入不合法" },	
			weight: {required:"权重 不能为空", digits: "权重 只能是正整数"},
			maxFails: { required: "最大失败次数 不能为空", digits: "最大失败次数 只能是正整数" },
			failTimeout: { required: "失败超时时间 不能为空", digits:"失败超时时间 只能是正整数" },
			type: { required: "类型 不能为空", digits: "类型 只能是正整数"},
			srunId: { isStr: "SrunID 只能包括英文字母、数字和下划线"  }
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
		$("#serverId").val("");		
		kylin.klb.server.item.formenable();
		kylin.klb.server.item.formreset();
		kylin.klb.server.item.popbox();
		
	});
	
	$('#save_scheduler').click(function(){
		return kylin.klb.server.item.checkSave();
	});
	
	
});

(function(package){
	jQuery.extend(package, {
		//设置弹出层
		setss : function (jsonp){
			$("#update").val("1");
			$("#serverId").val(jsonp.serverId);
			package.formdisabled();
			kylin.klb.server.item.popbox();
			
			package.message("正在加载数据....");
			if(typeof jsonp != "undefined") {
				$.ajax({
					type: "POST",
					url: "server-item!input.action",
					data: {"rsgId":jsonp.rsgId, "serverId":jsonp.serverId},
					dataType : 'json',
					timeout : 20000,
					success: function(json){
						if(typeof json != "undefined") {
							var obj = json.obj;
							if(json.auth == true) {
								if(typeof obj != "undefined") {
									package.formenable();
									$("#ip").val(obj.ip);	
									$("#port").val(obj.port);	
									$("#weight").val(obj.weight);	
									$("#maxFails").val(obj.maxFails);	
									$("#failTimeout").val(obj.failTimeout);	
									$("#type").val(obj.type);	
									$("#srunId").val(obj.srunId);																																										
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
				"rsgId":$("#rsgId").val(),
				"serverId":$("#serverId").val(),
				"ip":$("#ip").val(),
				"port":$("#port").val(),
				"weight":$("#weight").val(),
				"maxFails":$("#maxFails").val(),
				"failTimeout":$("#failTimeout").val(),
				"type":$("#type").val(),	
				"srunId":$("#srunId").val(),	
				"update":$("#update").val()			
			};
			
			$.ajax({
				type: "POST",
				url: "server-item!save.action",
				data: jsonDate,
				dataType : 'json',
				timeout : 20000,
				success: function(json){
					if(typeof json != "undefined") {
						$("#save_scheduler").attr("disabled",false);
						if(json.auth == true) {							
							location.reload();
						} else {
						//	alert();
							package.message(json.mess);
							//location.reload();
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
						url: "server-item!delete.action",
						data: {"rsgId":jsonp.rsgId,"serverId":jsonp.serverId},
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
			$("#type").val(0);
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
})(kylin.klb.server.item);
