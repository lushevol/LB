/**
 * @fileOverview 麒麟天平链路负载均衡系统
 * 接口配置-物理接口
 * @author kylin
 */
 
if (!window.interface) {
	window.interface = {};
}
if (!window.interface.physics) {
	window.interface.physics = {};
}

//接口配置-物理接口
$(document).ready(function(){
	if(!($("#failedMess").val()===""||$("#failedMess").val()===null)){
		alert($("#failedMess").val());
		$("#failedMess").val("");
	}
	
	/* $.validator.setDefaults({
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
	}); */
	
});

(function(package){
	jQuery.extend(package, {
		
		message : function(msg) {
			if($.trim(msg) == "") $("#show").html("");
			$("#show").html(msg).fadeIn();
		}
	});
})(interface.physics);
