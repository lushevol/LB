/**
 * @fileOverview 麒麟天平负载均衡系统
 * 域名服务器配置
 * @author kylin
 */
if (!window.dns) {
	window.dns = {};
}

$(document).ready(function(){
	//聚焦第一个输入框
	$("#clustername").focus();
	jQuery.validator.addMethod("isip", function(value, element) {   
		return this.optional(element) || (/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/.test(value) && (RegExp.$1 <256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256));   
	}, "请输入合法的IP信息");
	jQuery.validator.addMethod("firstip", function(value, element) {   
		return this.optional(element) || (/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/.test(value) && (RegExp.$1 > 0 && RegExp.$1 < 224));   
	}, "请输入合法的IP首位信息");
	//为inputForm注册validate函数
	$("#inputForm").validate({
		errorPlacement: function(error, element) {
			error.css("color","red").insertAfter(element);
       	},
		rules: {
			firstadd:	{ isip: true, firstip: true },
			secondadd:	{ isip: true, firstip: true },
		},
		messages: {
			firstadd:{ isip:"域名服务器一IP地址错误，请输入合法的IP地址", firstip:"请输入合法的IP首位（1~223）" },
			secondadd:{ isip:"域名服务器二IP地址错误，请输入合法的IP地址", firstip:"请输入合法的IP首位（1~223）" },
		}
	});
	
	$('#inputForm').submit(function(){
		return dns.saveCheck();
	});	
});

(function(package){
	jQuery.extend(package, {
		saveCheck : function () {			
			var firstadd = $("#firstadd");
			var secondadd = $("#secondadd");			
			
			if( secondadd.val()!= "" && firstadd.val()== "" ) {
				package.message("若要设置域名服务器二，则域名服务器一不能为空");
				firstadd.focus();
				return false;
			}
			return true;
		},		
		message : function(msg) {
			if($.trim(msg) == "") return;
			$("#show").html(msg).hide().fadeIn();
		}
	});
})(dns);