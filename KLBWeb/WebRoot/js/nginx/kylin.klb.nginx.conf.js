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
if (!window.kylin.klb.nginx) {
	window.kylin.klb.nginx = {};
}
if (!window.kylin.klb.nginx.conf) {
	window.kylin.klb.nginx.conf = {};
}

$(document).ready(function(){
	//聚焦第一个输入框
	
	//为inputForm注册validate函数
	$("#inputForm").validate({
		errorPlacement: function(error, element) {
			if(element.attr("name")=="processor" || element.attr("name")=="connections"
				|| element.attr("name")=="keepalive" || element.attr("name")=="gzipLength") {
				 error.css("color","red").insertAfter(element.next("span"));
			} else {
				error.css("color","red").insertAfter(element);
			}
       	},
		rules: {
			processor: { required:true, digits:true },
			connections: { required:true, digits:true },
			keepalive: { required:true, digits:true },
			gzipLength: { required:true, digits:true }
		},
		messages: {	
			processor:{ required: "同时开启的线程数 不能为空", digits:"同时开启的线程数 必须为数字" },
			connections:{ required: "每个线程的最大连接数 不能为空", digits:"每个线程的最大连接数 必须为数字" },
			keepalive:{ required: "超时时间 不能为空", digits:"超时时间 必须为数字" },
			gzipLength:{ required: "压缩页面最小字节数 不能为空", digits:"压缩页面最小字节数 必须为数字" }	
		}
	});
	
	/*
	var dealOpen = function(){
		var enabled = $("#enabled").val();
		if(enabled=="false"){			
			$(".openGray").addClass("gray9");
			$("#processor").attr("disabled", true);
			$("#connections").attr("disabled", true);
			$("#keepalive").attr("disabled", true);
			$("#gzip").attr("disabled", true);
			$("#gzipLength").attr("disabled", true);			
			$("#button").attr("disabled", true);
			$(":radio").attr("disabled", true);
		}
		else{			
			$(".openGray").removeClass("gray9");
			$("#processor").attr("disabled", false);
			$("#connections").attr("disabled", false);
			$("#keepalive").attr("disabled", false);
			$("#gzip").attr("disabled", false);
			$("#gzipLength").attr("disabled", false);		
			$("#button").attr("disabled", false);
			$(":radio").attr("disabled", false);
		}
	};
	
	dealOpen();
	*/
	
	$('#inputForm').submit(function(){
		return kylin.klb.nginx.conf.check();
	});
});

(function(package){
	jQuery.extend(package, {
		start : function (obj){			
			if(confirm("要改变当前状态吗?")) {
				var val = $.trim($(obj).text());
				var enabledVal = "";
				var enabled = "";
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
					url: "nginx-conf!start.action",
					data: {"enabled":enabled},
					dataType : 'json',
					timeout : 20000,
					success: function(json){
						if(typeof json != "undefined") {
							if(json.auth == true) {
								//$(obj).html(enabledVal);
								//$(obj).next().remove();
								$("#show").html(val+" 操作成功").show().fadeOut(3000);									
								//$(obj).parent().parent().siblings(".stc").html(val);
								location.reload();
							} else {
								//$(obj).html(val);
								//$(obj).next().remove();
								alert(json.mess);
								location.reload();
								//$("#show").html(val+" 操作失败").show().fadeOut(3000);								
							}
						}
					}, error : function(){
						//$(obj).html(val);
						//$(obj).next().remove();
						$("#show").html(val+" 操作失败").show().fadeOut(2000);
					}
				});
			}			
		},
		check : function () {					
			return true;
		},
		message : function(msg) {
			if($.trim(msg) == "") return;
			$("#show").html(msg).hide().fadeIn();
		}
	});
})(kylin.klb.nginx.conf);
