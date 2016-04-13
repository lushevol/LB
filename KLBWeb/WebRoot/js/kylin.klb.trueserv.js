/**
 * @fileOverview 麒麟天平负载均衡系统
 * 真实服务管理
 * @author kylin
 */

if (!window.kylin) {
	window.kylin = {};
}
if (!window.kylin.klb) {
	window.kylin.klb = {};
}
if (!window.kylin.klb.trueserv) {
	window.kylin.klb.trueserv = {};
}

//真实服务管理
$(document).ready(function(){
	jQuery.validator.addMethod("isip", function(value, element) {   
		return this.optional(element) || (/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/.test(value) && (RegExp.$1 <256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256));   
	}, "请输入合法的IP信息");
	jQuery.validator.addMethod("firstip", function(value, element) {   
		return this.optional(element) || (/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/.test(value) && (RegExp.$1 > 0 && RegExp.$1 < 224));   
	}, "请输入合法的IP首位信息");
	jQuery.validator.addMethod("isStr", function(value, element) {   
		return this.optional(element) || (/^[\w]+$/.test(value));   
	}, "只能包括英文字母、数字和下划线");
	jQuery.validator.addMethod("weights", function(value,element){
		return this.optional(element) || (/^(\d+)$/.test(value)) && ((RegExp.$1 < 65536))&&((RegExp.$1 > 0));
	}, "权重只能输入数字，范围是1-65535");
	jQuery.validator.addMethod("isPort", function(value, element) {   
		return this.optional(element) || (/^(\d+)$/.test(value)) && ((RegExp.$1 < 65536))&&((RegExp.$1 >= 0));
	}, "端口只能输入数字，范围是0-65535");
		
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
     		kylin.klb.trueserv.savess();
     		//form.submit();
     	},
		rules: {
			service: { required:true },
			serviceName: { required:true, isStr:true },
			ip: { required:true, isip:true, firstip: true },
			forward: { required:true },
			weight: { required:true, weights: true },
			mapport: { isPort: true }
		},
		messages: {
			service:{ required: "请选择选择虚拟服务" },
			serviceName:{ required: "真实服务器名称 不能为空", isStr:"真实服务器名称 只能包括英文字母、数字和下划线" },
			ip:{ required: "真实服务器地址 不能为空", isip:"真实服务器地址 请输入合法的IP地址", firstip:"请输入合法的真实服务器IP首位（1~223）" },
			forward:{ required: "请选择转发方式" },
			weight:{ required: "权重 不能为空" },
			mapport: { isPort: "端口输入不合法" }
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
		$("#oldServiceName").val("");
		$("#servicetr").show();
		
		//初始化端口设置效果
		$("#mapport").val("");
		$(".portGray").addClass("gray9");
		$("#mapport").attr("disabled", true);
		
		kylin.klb.trueserv.formenable();
		kylin.klb.trueserv.formreset();
		kylin.klb.trueserv.popbox();
	});
	
	$("#forward").change( function() {
 		var forward = $("#forward").val();
		if ( forward == "1" ){
			$(".portGray").removeClass("gray9");
			$("#mapport").attr("disabled", false);			
		} else{
			$("#mapport").val("");
			$(".portGray").addClass("gray9");
			$("#mapport").attr("disabled", true);
		}
	});
	
});

(function(package){
	jQuery.extend(package, {
		//设置弹出层
		setss : function (jsonp){
			$("#update").val("1");
			$("#servicetr").hide();
			package.formdisabled();
			kylin.klb.trueserv.popbox();
			
			package.message("正在加载数据....");
			
			if(typeof jsonp != "undefined") {
				$.ajax({
					type: "POST",
					url: "klb-load-true-serv!input.action",
					
					data: {
						"vsId":$.trim(jsonp.vsId),
						"tsId":$.trim(jsonp.tsId)
					},
					dataType : 'json',
					timeout : 20000,
					success: function(json){
						if(typeof json != "undefined") {
							if(json.auth == true) {
								var obj = json.obj;
								if(typeof obj != "undefined") {
									package.formenable();
									$("#vsId").val(obj.vsId);
									$("#tsId").val(obj.tsId);
									$("#service").val(obj.service);
									$("#serviceName").val(obj.serviceName);
									$("#ip").val(obj.ip);
									$("#forward").val(obj.forward);
									$("#mapport").val(obj.mapport);
									/* if(obj.forward == "Route") 
										$("#forward").val("gate");
									else if(obj.forward == "Tunnel") 
										$("#forward").val("ipip");
									else $("#forward").val(obj.forward); */
									
									//初始化端口设置效果
									var forward = $("#forward").val();
									if ( forward == "1" ){
										$(".portGray").removeClass("gray9");
										$("#mapport").attr("disabled", false);			
									} else{
										$("#mapport").val("");
										$(".portGray").addClass("gray9");
										$("#mapport").attr("disabled", true);
									}
									
									$("#weight").val(obj.weight);									
									$("#oldServiceName").val(jsonp.serviceName);
									package.message("");
								}
							} else {
								package.message("加载数据时出现错误");
							}
						} else {
							package.message("加载数据时出现错误");
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
				"tsId":$("#tsId").val(),
				"service":$("#service").val(),
				"serviceName":$("#serviceName").val(),
				"ip":$("#ip").val(),
				"forward":$("#forward").val(),
				"weight":$("#weight").val(),
				"mapport":$("#mapport").val(),
				"oldServiceName":$("#oldServiceName").val(),
				"update":$("#update").val()
			};
			
			$.ajax({
				type: "POST",
				url: "klb-load-true-serv!save.action",
				data: jsonDate,
				dataType : 'json',
				timeout : 20000,
				success: function(json){
					if(typeof json != "undefined") {
						$("#save_scheduler").attr("disabled",false);
						if(json.auth == true) {							
							location.reload();
						} else {
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
		startss : function (obj,jsonp){
			if(typeof jsonp != "undefined") {
				if(confirm("要改变当前状态吗?")) {
					var val = $.trim($(obj).text());
					var statusVal = "关闭";
					var status = "false";
					$(obj).parent().append("<a href=\"javascript:;\">正在"+val+"...</a>");
					$(obj).html("");
					$("#message").html("正在"+val+"...").hide().fadeIn();
					
					if(val == "关闭") {
						statusVal = "开启";
						status = "false";
					} else {
						statusVal = "关闭";
						status = "true";
					}
					$.ajax({
						type: "POST",
						url: "klb-load-true-serv!start.action",
						data: {
							"vsId":jsonp.vsId,
							"tsId":jsonp.tsId,
							"status":status
						},
						dataType : 'json',
						timeout : 20000,
						success: function(json){
							if(typeof json != "undefined") {
								if(json.auth == true) {
									$(obj).html(statusVal);
									$(obj).next().remove();
									$("#message").html(val+" 操作成功").show().fadeOut(3000);
									if (val == "关闭") {
										$(obj).parent().parent().siblings(".stc").html(val).css("color","red");
									} else {
										$(obj).parent().parent().siblings(".stc").html(val).css("color","green");
									}									
								} else {
									$(obj).html(val);
									$(obj).next().remove();
									$("#message").html(val+" 操作失败").show().fadeOut(3000);
								}
							}
						}, error : function(){
							$(obj).html(val);
							$(obj).next().remove();
							$("#message").html(val+" 操作失败").show().fadeOut(2000);
						}
					});
				}
			}
		},
		delss : function (obj,jsonp){
			if(confirm("确实要删除吗?")) {
				$("#message").html("正在删除...").hide().fadeIn();
				$(obj).parent().append("<a href=\"javascript:;\">正在删除...</a>");
				$(obj).html("");
				
				if(typeof jsonp != "undefined") {
					$.ajax({
						type: "POST",
						url: "klb-load-true-serv!delete.action",
						data: {
							"vsId":$.trim(jsonp.vsId),
							"tsId":$.trim(jsonp.tsId)
						},
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
			markLay.style.display="block";
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
})(kylin.klb.trueserv);
