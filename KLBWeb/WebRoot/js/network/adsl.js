/**
 * @fileOverview 麒麟天平链路负载均衡系统
 * ADSL配置
 * @author kylin
 */
 
if (!window.adsl) {
	window.adsl = {};
}

//ADSL配置
$(document).ready(function(){
	jQuery.validator.addMethod("isAdsl", function(value, element) {   
		return this.optional(element) || /^ppp[\d]+$/.test(value);   
	}, "聚合接口名称的格式为pppX（X为数字）");
	jQuery.validator.addMethod("isMtu", function(value,element){
		return this.optional(element) || (/^(\d+)$/.test(value)) && ((RegExp.$1 < 1493))&&((RegExp.$1 > 67));
	}, "MTU值只能输入数字，范围是68-1492");

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
     		adsl.save();
     	},
		rules: {
			//user: { required:true },
			//password: { required:true }
			inter: { required: true, isAdsl: true },
			eth: { required: true },
			mtu: { required: true, isMtu: true }
		},
		messages: {
			//user:{required: "用户名 不能为空"},
			//password:{required: "密码 不能为空"}
			inter:{ required: "ADSL接口 不能为空", isAdsl:"ADSL接口名称的格式为pppX（X为数字）！" },
			eth:{ required: "物理接口 不能为空" },
			mtu:{ required: "MTU值 不能为空", isMtu:"MTU值只能输入数字，范围是68-1492" }
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
		adsl.formenable();
		adsl.formreset();
		adsl.popbox();
		$("#operation").val("add");
		$("#mtu").val("1492");
	});
});

(function(package){
	jQuery.extend(package, {
		edit : function (obj,jsonp){
			if(typeof jsonp != "undefined") {
				if(confirm("要改变当前状态吗?")) {
					var val = $.trim($(obj).text());
					var enabledVal = "断开连接";
					var enabled = "stop";
					$(obj).parent().append("<a href=\"javascript:;\">正在"+val+"...</a>");
					$(obj).html("");
					$("#message").html("正在"+val+"...").hide().fadeIn();
					
					if(val == "断开连接") {
						enabledVal = "拨号";
						enabled = "stop";
					} else {
						enabledVal = "断开连接";
						enabled = "dial";
					}
					$.ajax({
						type: "POST",
						url: "adsl!edit.action",
						data: {"inter":jsonp.inter, "mode":jsonp.mode},
						dataType : 'json',
						timeout : 20000,
						success: function(json){
							if(typeof json != "undefined") {
								if(json.auth == true) {
									$(obj).html(enabledVal);
									$(obj).next().remove();
									$("#message").html(val+" 操作成功").show().fadeOut(3000);
									//$(obj).parent().parent().siblings(".stc").html(val);
									location.reload();
								} else {
									$(obj).html(val);
									$(obj).next().remove();
									$("#message").html(val +" 操作失败 "+ json.mess ).show().fadeOut(3000);
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
	
		//设置弹出层
		set : function (jsonp){
			package.formdisabled();
			adsl.popbox();
			
			package.message("正在加载数据....");
			if(typeof jsonp != "undefined") {
				$.ajax({
					type: "POST",
					url: "adsl!input.action",
					data: {"inter":jsonp.inter},
					dataType : 'json',
					timeout : 10000,
					success: function(json){
						if(typeof json != "undefined") {
							if(json.auth == true) {
								var obj = json.obj;
								if(typeof obj != "undefined") {
									package.formenable();
									package.formreset();
									$("#inter").val(obj.inter);
									$("#inter").attr("disabled",true);
									$("#eth").val(obj.eth);
									$("#eth").attr("disabled",true);
									$("#describe").val(obj.describe);
									$("#user").val(obj.user);
									$("#password").val(obj.password);
									$("#mtu").val(obj.mtu);
									$("#timeout").val(obj.timeout);
									$("#operation").val("edit");
									package.message("");
								}
							} else {
								package.message("加载数据时出现错误");
							}
						} else {
							package.message("加载数据时出现错误");
						}
					},
					error : function(e){
						package.message("加载数据时出现错误");
					}
				});	
			} else {
				package.message("加载数据时出现错误");
			}
		},
		save : function () {
			$("#save_scheduler").attr("disabled",true);
			package.message("正在保存数据...");

			var jsonDate = {
				"aa.inter":$("#inter").val(),
				"aa.eth":$("#eth").val(),
				"aa.describe":$("#describe").val(),
				"aa.user":$("#user").val(),
				"aa.password":$("#password").val(),
				"aa.mtu":$("#mtu").val(),
				"aa.timeout":$("#timeout").val(),
				"operation":$("#operation").val(),
			};
			
			$.ajax({
				type: "POST",
				url: "adsl!save.action",
				data: jsonDate,
				dataType : 'json',
				timeout : 10000,
				success: function(json){
					if(typeof json != "undefined") {
						$("#save_scheduler").attr("disabled",false);
						if(json.auth == true) {							
							location.reload();
						} else {
							package.message(json.mess);
							$("#save_adsl").attr("disabled",false);
						}
					} else {
						package.message("保存数据失败");
						$("#save_adsl").attr("disabled",false);
					}
				},
				error : function(){
					package.message("保存数据失败");
					$("#save_adsl").attr("disabled",false);
				}
			})
		},
		del : function (obj,jsonp){
			if(confirm("确实要删除吗?")) {
				$("#message").html("正在删除...").hide().fadeIn();
				$(obj).parent().append("<a href=\"javascript:;\">正在删除...</a>");
				$(obj).html("");
				
				if(typeof jsonp != "undefined") {
					$.ajax({
						type: "POST",
						url: "adsl!del.action",
						data: { "inter":$.trim(jsonp.inter) },
						dataType : "json",
						timeout : 10000,
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
			var popDiv = $("#addAdsl");
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
})(adsl);