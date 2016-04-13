/**
 * @fileOverview 麒麟天平链路负载均衡系统
 * 路由配置-静态路由
 * @author kylin
 */
 
if (!window.arp) {
	window.arp = {};
}

//arp配置
$(document).ready(function(){
	jQuery.validator.addMethod("isip", function(value, element) {   
		return this.optional(element) || (/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/.test(value) && (RegExp.$1 <256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256));   
	}, "请输入合法的IP信息");
	jQuery.validator.addMethod("firstip", function(value, element) {   
		return this.optional(element) || (/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/.test(value) && (RegExp.$1 > 0 && RegExp.$1 < 224));   
	}, "请输入合法的IP首位信息");
	jQuery.validator.addMethod("isMac", function(value,element){
		return this.optional(element) || (/^([0-9a-fA-F]{2})((:[0-9a-fA-F]{2}){5})$/.test(value));
	}, "MAC地址 输入不合法");
	
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
     		arp.save();
     	},
		rules: {
			interface: { required:true },
			ip: { required:true, isip: true, firstip: true },
			mac: { required:true, isMac: true }
		},
		messages: {
			interface:{ required: "接口 不能为空" },
			ip:{ required: "IP地址 不能为空", isip:"IP地址不合法", firstip:"请输入合法的IP首位（1~223）" },
			mac:{ required: "MAC地址 不能为空", isMac:"MAC地址 输入不合法" }
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
		arp.formenable();
		arp.formreset();
		arp.popbox();
	});
});

(function(package){
	jQuery.extend(package, {
		//设置弹出层
		set : function (jsonp){
			package.message("");
			arp.popbox();			
			$("#id").val(jsonp.id);
			$("#ip").val(jsonp.ip);
			$("#mac").val(jsonp.mac);
			$("#interface").val(jsonp.inter);
			if(jsonp.type==0 || jsonp.type==2){
				$("#operation").val("edit");
			}
		},
		
		save : function () {
			$("#save_scheduler").attr("disabled",true);
			package.message("正在保存数据...");

			var jsonDate = {
				"arp.inter":$("#interface").val(),
				"arp.ip":$("#ip").val(),
				"arp.mac":$("#mac").val(),
				"operation":$("#operation").val(),
				"id":$("#id").val(),
			};
			
			$.ajax({
				type: "POST",
				url: "arp!save.action",
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
		del : function (obj, jsonp){
			if(confirm("确实要删除吗?")) {
				$("#message").html("正在删除...").hide().fadeIn();
				$(obj).parent().append("<a href=\"javascript:;\">正在删除...</a>");
				$(obj).html("");
				
				if(typeof jsonp != "undefined") {
					$.ajax({
						type: "POST",
						url: "arp!del.action",
						data: { "id":jsonp.id, "ip":jsonp.ip, "inter":jsonp.inter },
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
})(arp);