/**
 * @fileOverview 麒麟天平链路负载均衡系统
 * 路由配置-策略路由
 * @author kylin
 */

if (!window.route) {
	window.route = {};
}
if (!window.route.policy) {
	window.route.policy = {};
}

//策略路由配置
$(document).ready(function(){
	if(!($("#failedMess").val()===""||$("#failedMess").val()===null)){
		alert($("#failedMess").val());
		$("#failedMess").val("");
	}
	jQuery.validator.addMethod("isip", function(value, element) {   
		return this.optional(element) || (/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/.test(value) && (RegExp.$1 <256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256));   
	}, "请输入合法的IP信息");
	
	jQuery.validator.addMethod("metrics", function(value,element){
		return this.optional(element) || (/^(\d+)$/.test(value)) && ((RegExp.$1 < 65536))&&((RegExp.$1 > 0));
	}, "metric只能输入数字，范围是1-65535");
	
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
     		route.policy.move();
     	},
		rules: {
			row: { required:true, digits:true }			
		},
		messages: {
			row:{ required: "输入行 不能为空", digits:"输入行 只能是整数"}		
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
	$(".move").click(function(){
		route.policy.formenable();
		route.policy.formreset();
		route.policy.popbox();
	});
	
});

(function(package){
	jQuery.extend(package, {
		recordOldId : function (jsonp) {
			id = jsonp.id;
			$("#oldId").val(id);
		},
		move : function () {
			$("#save_scheduler").attr("disabled",true);
			package.message("正在保存数据...");

			var jsonDate = {
				"id":$("#row").val(),
				"oldId":$("#oldId").val(),
			};
			
			$.ajax({
				type: "POST",
				url: "route-policy!move.action",
				data: jsonDate,
				dataType : 'json',
				timeout : 10000,
				success: function(json){
					if(typeof json != "undefined") {
						$("#save_scheduler").attr("disabled",false);
						if(json.auth == true) {
							alert(json.mess);
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
	
		/* edit : function (obj,jsonp){
			if(typeof jsonp != "undefined") {
				if(confirm("要改变当前状态吗?")) {
					var val = $.trim($(obj).text());
					var enabledVal = "关闭";
					var enabled = "false";
					$(obj).parent().append("<a href=\"javascript:;\">正在"+val+"...</a>");
					$(obj).html("");
					$("#message").html("正在"+val+"...").hide().fadeIn();
					
					if(val == "关闭") {
						enabledVal = "开启";
						enabled = "false";
					} else {
						enabledVal = "关闭";
						enabled = "true";
					}
					$.ajax({
						type: "POST",
						url: "route-policy!edit.action",
						data: {"id":jsonp.id,"status":jsonp.status},
						dataType : 'json',
						timeout : 20000,
						success: function(json){
							if(typeof json != "undefined") {
								if(json.auth == true) {
									$(obj).html(enabledVal);
									$(obj).next().remove();
									$("#message").html(val+" 操作成功").show().fadeOut(3000);
									$(obj).parent().parent().siblings(".stc").html(val);
									location.reload();
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
		}, */
		del : function (obj, jsonp){
			if(confirm("确实要删除吗?")) {
				$("#message").html("正在删除...").hide().fadeIn();
				$(obj).parent().append("<a href=\"javascript:;\">正在删除...</a>");
				$(obj).html("");
				
				if(typeof jsonp != "undefined") {
					$.ajax({
						type: "POST",
						url: "route-policy!del.action",
						data: { "id":jsonp.id },
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
})(route.policy);