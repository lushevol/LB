/**
 * @fileOverview 麒麟天平负载均衡系统
 * 智能DNS-服务器配置
 * @author kylin
 
if (!window.dns) {
	window.dns = {};
}
if (!window.dns.server) {
	window.dns.server = {};
} */

$(document).ready(function(){
	
	jQuery.validator.addMethod("isPort", function(value,element){
		return this.optional(element) || (/^(\d+)$/.test(value)) && ((RegExp.$1 < 65536))&&((RegExp.$1 > 0));
	}, "端口只能输入数字，范围是1-65535");
	
	//为serverForm注册validate函数
	$("#serverForm").validate({
		errorPlacement: function(error, element) {			
			error.css("color","red").insertAfter(element);			
       	},
		rules: {
			port: { required:true, isPort: true }
		},
		messages: {
			port:{ required: "端口 不能为空", isPort:"端口只能输入数字，范围是1-65535" }			
		}
	});
	
});