/**
 * @fileOverview 麒麟天平负载均衡系统
 * 高可用配置
 * @author kylin
 */
if (!window.kylin) {
	window.kylin = {};
}
if (!window.kylin.klb) {
	window.kylin.klb = {};
}
if (!window.kylin.klb.loadsedundant) {
	window.kylin.klb.loadsedundant = {};
}

$(document).ready(function(){
	//聚焦第一个输入框
	//$("#clustername").focus();
	jQuery.validator.addMethod("isip", function(value, element) {   
		return this.optional(element) || (/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/.test(value) && (RegExp.$1 <256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256));   
	}, "请输入合法的IP信息");
	jQuery.validator.addMethod("firstip", function(value, element) {   
		return this.optional(element) || (/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/.test(value) && (RegExp.$1 > 0 && RegExp.$1 < 224));   
	}, "请输入合法的IP首位信息");
	jQuery.validator.addMethod("udpports", function(value,element){
		return this.optional(element) || (/^(\d+)$/.test(value)) && ((RegExp.$1 < 65536))&&((RegExp.$1 > 0));
	}, "端口只能输入数字，范围是1-65535");
	jQuery.validator.addMethod("sync", function(value,element){
		return this.optional(element) || (/^(\d+)$/.test(value)) && ((RegExp.$1 < 256))&&((RegExp.$1 >= 0));
	}, "连接同步只能输入数字，范围是0-255");
	jQuery.validator.addMethod("keepalives", function(value,element){
		return this.optional(element) || (/^(\d+)$/.test(value)) && ((RegExp.$1 < 360001))&&((RegExp.$1 > 9));
	}, "心跳间隔时间只能输入数字，范围是10-360000");
	jQuery.validator.addMethod("warmtimes", function(value,element){
		return this.optional(element) || (/^(\d+)$/.test(value)) && ((RegExp.$1 < 360001))&&((RegExp.$1 > 9));
	}, "发出警告时间只能输入数字，范围是10-360000");
	jQuery.validator.addMethod("deadtimes", function(value,element){
		return this.optional(element) || (/^(\d+)$/.test(value)) && ((RegExp.$1 < 360001))&&((RegExp.$1 > 9));
	}, "死亡判定时间只能输入数字，范围是10-360000");
	jQuery.validator.addMethod("initdeads", function(value,element){
		return this.optional(element) || (/^(\d+)$/.test(value)) && ((RegExp.$1 < 360001))&&((RegExp.$1 > 9));
	}, "启动等待时间只能输入数字，范围是10-360000");
	jQuery.validator.addMethod("isMail", function(value,element){
			return this.optional(element) || (/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.([a-zA-Z0-9_-]+))+)$/.test(value));
	}, "不是合法的邮箱地址");
	//为inputForm注册validate函数
	$("#inputForm").validate({
		errorPlacement: function(error, element) {
			if(element.attr("name")=="masterDevice" || element.attr("name")=="keepalive" || element.attr("name")=="warntime" 
				|| element.attr("name")=="deadtime" || element.attr("name")=="inittime") {
				 error.css("color","red").insertAfter(element.next("span"));
			} else {
				error.css("color","red").insertAfter(element);
			}
       	},
		//errorLabelContainer: "#show",
		//wrapper: "li",
		rules: {
			interfaces: { required:true },
			udpport: { required:true, udpports: true },
			ucast: { required:true, isip:true, firstip: true },
			hostname: { required: true },
			masterDevice: { sync: true },
			keepalive: { required:true, keepalives: true },
			warntime: { required:true, warmtimes: true },
			deadtime: { required:true, deadtimes: true },
			inittime: { required:true, initdeads: true },
			address: { required:true, isMail: true }
		},
		messages: {
			interfaces:{ required: "请选择心跳检测接口" },
			udpport:{ required: "心跳通讯端口 不能为空", digits:"心跳通讯端口 必须为数字" },
			ucast:{ required: "对方IP地址 不能为空", isip:"您输入的IP地址不合法", firstip:"请输入合法的IP地址首位（1~223）" },
			hostname:{ required: "对方主机名 不能为空" },
			masterDevice:{ sync: "连接同步的ID范围[0-255]" },
			keepalive:{ required: "心跳间隔时间 不能为空", digits:"心跳间隔时间 必须为数字" },
			warntime:{ required: "发出警告时间 不能为空", digits:"发出警告时间 必须为数字" },
			deadtime:{ required: "死亡判定时间 不能为空", digits:"死亡判定时间 必须为数字" },
			inittime:{ required: "启动等待时间 不能为空", digits:"启动等待时间 必须为数字"},
			address:{ required: "接收人邮箱地址 不能为空", isMail: "接收人必须是合法的邮箱地址" }
		}
	});
	
	var dealOpen = function(){
		var enabled = $("#enabled").val();
		if(enabled=="true"){			
			$(".openGray").addClass("gray9");
			$("#interfaces").attr("disabled", true);
			$("#udpport").attr("disabled", true);
			$("#ucast").attr("disabled", true);
			$("#hostname").attr("disabled", true);
			$("#masterDevice").attr("disabled", true);
			$("#keepalive").attr("disabled", true);
			$("#warntime").attr("disabled", true);
			$("#deadtime").attr("disabled", true);
			$("#inittime").attr("disabled", true);
			$("#indirect").attr("disabled", true);
			$("#send").attr("disabled", true);
			$("#address").attr("disabled", true);
			//$("#indirect").text().addClass("gray9");
			$("#button").attr("disabled", true);
			$(":radio").attr("disabled", true);
		}
		else{			
			$(".openGray").removeClass("gray9");
			$("#interfaces").attr("disabled", false);
			$("#udpport").attr("disabled", false);
			$("#ucast").attr("disabled", false);
			$("#hostname").attr("disabled", false);
			$("#masterDevice").attr("disabled", false);
			$("#keepalive").attr("disabled", false);
			$("#warntime").attr("disabled", false);
			$("#deadtime").attr("disabled", false);
			$("#inittime").attr("disabled", false);
			$("#indirect").attr("disabled", false);
			$("#send").attr("disabled", false);
			$("#address").attr("disabled", false);
			//$("#indirect").text().removeClass("gray9");
			$(":radio").attr("disabled", false);
		}
	};
	
	dealOpen();
	
	$('#inputForm').submit(function(){
		return kylin.klb.loadsedundant.timeCheck();
	});
});

(function(package){
	jQuery.extend(package, {
		start : function (obj){			
			if(confirm("要改变当前状态吗?")) {
				var val = $.trim($(obj).text());
				var enabledVal = "关闭";
				var enabled = "false";
				//$(obj).parent().append("<a href=\"javascript:;\"  class=\"btnStyle\"><span>"+val+"</span></a>");
				//$(obj).html("");
				$("#show").html("正在"+val+"...").hide().fadeIn();
					
				if(val == "关闭") {
					enabledVal = "开启";
					enabled = "false";
				} else {
					enabledVal = "关闭";
					enabled = "true";
				}
				$.ajax({
					type: "POST",
					url: "klb-load-sedundant!start.action",
					data: {"enabled":enabled},
					dataType : 'json',
					timeout : 20000,
					success: function(json){
						if(typeof json != "undefined") {
							if(json.auth == true) {
								$(obj).html(enabledVal);
								$(obj).next().remove();
								$("#show").html(val+" 操作成功").show().fadeOut(3000);									
								//$(obj).parent().parent().siblings(".stc").html(val);
								location.reload();
							} else {
								$(obj).html(val);
								$(obj).next().remove();
								alert(json.mess);
								location.reload();
								//$("#show").html(val+" 操作失败").show().fadeOut(3000);								
							}
						}
					}, error : function(){
						$(obj).html(val);
						$(obj).next().remove();
						$("#show").html(val+" 操作失败").show().fadeOut(2000);
					}
				});
			}			
		},
		timeCheck : function () {			
			var keepalive = $("#keepalive");
			var warntime = $("#warntime");
			var deadtime = $("#deadtime");
			var inittime = $("#inittime");
			
			if( Number(keepalive.val()) >= Number(warntime.val()) ) {
				package.message("您设置的发出警告时间必须大于心跳间隔时间");
				warntime.focus();
				return false;
			}
			if( Number(warntime.val()) >= Number(deadtime.val()) ) {
				package.message("您设置的死亡判定时间必须大于发出警告时间");
				deadtime.focus();
				return false;
			}
			if( Number(deadtime.val()) >= Number(inittime.val()) ) {
				package.message("您设置的启动等待时间必须大于死亡判定时间");
				inittime.focus();
				return false;
			}
			return true;
		},
		message : function(msg) {
			if($.trim(msg) == "") return;
			$("#show").html(msg).hide().fadeIn();
		}
	});
})(kylin.klb.loadsedundant);
