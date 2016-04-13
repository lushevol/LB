/**
 * @fileOverview 麒麟天平链路负载均衡系统
 * 智能DNS-ISP地址段
 * @author kylin
 */
 
if (!window.dns) {
	window.dns = {};
}
if (!window.dns.isp) {
	window.dns.isp = {};
}

//ISP地址段配置
$(document).ready(function(){
	if( !($("#failedMess").val()==="" || $("#failedMess").val()===null) ) {
		alert($("#failedMess").val());
		$("#failedMess").val("");
	}

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
	
});

(function(package){
	jQuery.extend(package, {
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
						url: "dns-isp!edit.action",
						data: {"sale":jsonp.sale,"status":jsonp.status},
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
						url: "dns-isp!del.action",
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
		}
	});
})(dns.isp);