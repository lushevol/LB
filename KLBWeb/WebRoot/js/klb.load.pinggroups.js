/**
 * @fileOverview 麒麟天平链路负载均衡系统
 * 接口配置-高可用检测
 * @author kylin
 */
 
if (!window.klb) {
	window.klb = {};
}
if (!window.klb.load) {
	window.klb.load = {};
}
if (!window.klb.load.pinggroups) {
	window.klb.load.pinggroups = {};
}

//高可用检测
$(document).ready(function(){
	/* jQuery.validator.addMethod("isDns", function(value, element) {   
		return this.optional(element) || (/^([a-z0-9-]{1,}.)?[a-z0-9-]{2,}.([a-z0-9-]{1,}.)?[a-z0-9]{2,}$/i).test(value);   
	}, "你输入的域名地址不合法"); */
	
	$.validator.setDefaults({
		invalidHandler: function(form, validator) {
	    	$.each(validator.invalid,function(key,value){
	            tmpkey = key;
	            tmpval = value;
	            validator.invalid = {};
	            validator.invalid[tmpkey] = value;
	            $("#show1").html(value).hide().fadeIn();
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
		rules: {
			firstadd: { required: true }			
		},
		messages: {
			firstadd:{ required: "域名地址 不能为空" }
		}
	});		
	
});

(function(package){
	jQuery.extend(package, {
		
		message : function(msg) {
			if($.trim(msg) == "") $("#show").html("");
			$("#show").html(msg).fadeIn();
		}				
	});
})(klb.load.pinggroups);