/**
 * @fileOverview 麒麟天平负载均衡系统
 * 真实服务器状态
 * @author kylin
 */

if (!window.kylin) {
	window.kylin = {};
}
if (!window.kylin.klb) {
	window.kylin.klb = {};
}
if (!window.kylin.klb.servers) {
	window.kylin.klb.servers = {};
}
if (!window.kylin.klb.servers.status) {
	window.kylin.klb.servers.status = {};
}

$(document).ready(function(){
	jQuery.validator.addMethod("intervals", function(value,element){
			return this.optional(element) || (/^(\d+)$/.test(value)) && ((RegExp.$1 <= 360))&&((RegExp.$1 >= 1));
		}, "检测间隔只能输入数字，范围是1-360");
	jQuery.validator.addMethod("timeouts", function(value,element){
			return this.optional(element) || (/^(\d+)$/.test(value)) && ((RegExp.$1 <= 360))&&((RegExp.$1 >= 1));
		}, "超时时间只能输入数字，范围是1-360");
	jQuery.validator.addMethod("retrytimes", function(value,element){
			return this.optional(element) || (/^(\d+)$/.test(value)) && ((RegExp.$1 <= 16))&&((RegExp.$1 >= 1));
		}, "重试次数只能输入数字，范围是1-16");
	jQuery.validator.addMethod("isPort", function(value,element){
			return this.optional(element) || (/^(\d+)$/.test(value)) && ((RegExp.$1 <= 65535))&&((RegExp.$1 >= 0));
		}, "检测端口只能输入数字，范围是0-65535");
	jQuery.validator.addMethod("isDate", function(value,element){
			return this.optional(element) || (/^(\d+)$/.test(value)) && ((RegExp.$1 <= 36000))&&((RegExp.$1 >= 1));
		}, "发送间隔只能输入数字，范围是1-36000");
	jQuery.validator.addMethod("isMail", function(value,element){
			return this.optional(element) || (/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.([a-zA-Z0-9_-]+))+)$/.test(value));
		}, "不是合法的邮箱地址");
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
     		kylin.klb.servers.status.savess();
     		//form.submit();
     	},
		rules: {
			interval: { required:true, intervals: true },
			timeout: { required:true, timeouts: true },
			retry: { required:true, retrytimes: true },
			port: { required:true, isPort: true },
			type: { required:true },
			mail: { required:true, isMail: true },
			date: { required:true, isDate: true }
		},
		messages: {
			interval:{ required: "检测间隔 不能为空", intervals: "检测间隔只能输入数字，范围是1-360"},
			timeout:{ required: "超时时间 不能为空", timeouts: "超时时间只能输入数字，范围是1-360"},
			retry:{ required: "重试次数 不能为空", retrytimes: "重试次数只能输入数字，范围是1-16"},
			port:{ required: "检测端口 不能为空", isPort: "检测端口只能输入数字，范围是0-65535"}, 
			type:{ required: "检测方式 不能为空" },
			mail:{ required: "接收人地址 不能为空", isMail: "接收人必须是合法的邮箱地址" },
			date:{ required: "发送间隔 不能为空", isDate: "发送间隔只能输入数字，范围是1-36000"}
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
	
	$('#save_scheduler').click(function(){
		return kylin.klb.servers.status.checkSave();
	});
	
});

(function(package){
	jQuery.extend(package, {
		//设置弹出层
		setss : function (jsonp){
			$("#update").val("1");
			//$("#sirtualService").hide();
			package.formdisabled();
			kylin.klb.servers.status.popbox();
			
			package.message("正在加载数据....");
			if(typeof jsonp != "undefined") {
				$.ajax({
					type: "POST",
					url: "klb-servers-status!input.action",					
					data: {
						"vsId":$.trim(jsonp.vsId)						
					},
					dataType : 'json',
					timeout : 20000,
					success: function(json){
						if(typeof json != "undefined") {
							if(json.auth == true) {
								var obj = json.obj;
								if(typeof obj != "undefined") {
									package.formenable();
									$("#vsId").val(obj.vsId);
									//$("#serviceName").val(obj.serviceName);
									$("#serviceName").text(obj.serviceName);
									$("#serviceName").addClass("gray7");
									$("#interval").val(obj.interval);
									$("#timeout").val(obj.timeout);
									$("#retry").val(obj.retry);
									$("#port").val(obj.port);
									$("#type").val(obj.type);
									if(obj.enabled == 'true'){
										$("#set1").attr('checked',true);
										$("#set2").attr('checked',false);
									}else{
										$("#set1").attr('checked',false);
										$("#set2").attr('checked',true);
									}
									$("#mail").val(obj.mail);
									$("#date").val(obj.date);
									$("#oldServiceName").val(jsonp.serviceName);
									package.message("");
								}
							} else {
								package.message("加载数据时出现错误");
							}
						} else {
							package.message("加载数据时出现错误");
						}
					},
					error : function(){
						package.message("加载数据时出现错误");
					}
				});	
			} else {
				package.message("加载数据时出现错误");
			}
		},
		savess : function () {
			$("#save_scheduler").attr("disabled",true);
			package.message("正在保存数据...");

			var enable =$("#set1").attr('checked') + "";
			
			var jsonDate = {
				"entity.vsId":$("#vsId").val(),
				//"entity.serviceName":$("#serviceName").val(),
				"entity.serviceName":$("#serviceName").text(),
				"entity.interval":$("#interval").val(),
				"entity.timeout":$("#timeout").val(),
				"entity.retry":$("#retry").val(),
				"entity.port":$("#port").val(),
				"entity.type":$("#type").val(),
				"entity.enabled":enable,
				"entity.mail":$("#mail").val(),
				"entity.date":$("#date").val(),
				"oldServiceName":$("#oldServiceName").val(),
				"update":$("#update").val()
			};
			
			$.ajax({
				type: "POST",
				url: "klb-servers-status!save.action",
				data: jsonDate,
				dataType : 'json',
				timeout : 20000,
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
		checkSave : function () {
			var port = $("#port");
			var type = $("#type");
			var flag = true;
			if( $.trim(port.val()) == "0" && type.val() == "2" ) {
				flag = false;
				package.message("检测方式为端口检测时，您输入的端口号不能为0");
				port.focus();
				return false;
			}
			return flag;
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
})(kylin.klb.servers.status);
