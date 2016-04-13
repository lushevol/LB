/**
 * @fileOverview 麒麟天平负载均衡系统
 * 虚拟服务管理
 * @author kylin
 */
 
if (!window.kylin) {
	window.kylin = {};
}
if (!window.kylin.klb) {
	window.kylin.klb = {};
}
if (!window.kylin.klb.sirtualserv) {
	window.kylin.klb.sirtualserv = {};
}

//虚拟服务配置
$(document).ready(function(){

	

	//虚拟服务设置
	$("#tcp").click(function(){
		if(this.checked){
			$("#tcptd").addClass("enable1").removeClass("disabled");
			$("#tcpPorts").attr("disabled",false);
		} else {
			$("#tcptd").addClass("disabled").removeClass("enable1");
			$("#tcpPorts").attr("disabled",true);
		}
	});
	
	//虚拟服务设置
	$("#udp").click(function(){
		if(this.checked){
			$("#udptd").addClass("enable1").removeClass("disabled");
			$("#udpPorts").attr("disabled",false);
		} else {
			$("#udptd").addClass("disabled").removeClass("enable1");
			$("#udpPorts").attr("disabled",true);
		}
	});
	
	jQuery.validator.addMethod("isip", function(value, element) {   
		return this.optional(element) || (/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/.test(value) && (RegExp.$1 <256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256));   
	}, "请输入合法的IP信息");
	jQuery.validator.addMethod("firstip", function(value, element) {   
		return this.optional(element) || (/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/.test(value) && (RegExp.$1 > 0 && RegExp.$1 < 224));   
	}, "请输入合法的IP首位信息");
	jQuery.validator.addMethod("isStr", function(value, element) {
		return this.optional(element) || /^[\w]+$/.test(value);
	}, "只能包括英文字母、数字和下划线");
	jQuery.validator.addMethod("isPort", function(value, element) {
		return this.optional(element) || ( /^(\d+(-\d+)?,)*(\d+(-\d+)?)$/.test(value) );   
	}, "端口输入不合法");
	jQuery.validator.addMethod("traffic", function(value,element){
		return this.optional(element) || (/^(\d+)$/.test(value)) && ( RegExp.$1 < 2147483648 );
	}, "流量控制只能输入数字，最大值是2147483647");
	jQuery.validator.addMethod("timeout", function(value,element){
		return this.optional(element) || (/^(\d+)$/.test(value)) && ( (RegExp.$1 <= 86400) && (RegExp.$1 >= 60) || ( RegExp.$1 == 0 ));
	}, "连接保持只能输入数字，范围是60-86400");
	
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
     		kylin.klb.sirtualserv.savess();
     		//form.submit();
     	},
		rules: {
			service: { required:true, isStr:true },
			haType: { required:true },
			//interfaces: { required:true },
			vipMark: { required:true, isip: true, firstip: true },
			tcpPorts: { required:true, isPort: true },
			udpPorts: { required:true, isPort: true },
			trafficUp: { digits: true, traffic: true },
			trafficDown: { digits: true, traffic: true },
			scheduling: { required:true },
			persistentTimeout: { required:true, timeout: true },
			persistentNetmask: { required:true, isip: true }
		},
		messages: {
			service:{ required: "虚拟服务名称 不能为空", isStr:"虚拟服务名称 只能包括英文字母、数字和下划线" },
			haType:{ required: "高可用类型 不能为空" },
			//interfaces:{ required: "接 口 不能为空" },
			vipMark:{ required: "虚拟服务地址 不能为空", isip:"虚拟服务地址 IP不合法", firstip:"请输入合法的虚拟服务IP首位（1~223）" },
			tcpPorts: { required:"TCP端口不能为空", isPort: "TCP端口输入不合法" },
			udpPorts: { required:"UDP端口不能为空", isPort: "UDP端口输入不合法" },
			trafficUp: { digits: "最大上行流量 只能是正整数，输入0或不填表示不限制上行流量", traffic: "最大上行流量 允许输入的最大数值为2147483647" },
			trafficDown: { digits: "最大下行流量 只能是正整数，输入0或不填表示不限制下行流量", traffic: "最大下行流量 允许输入的最大数值为2147483647" },
			scheduling:{ required: "调度算法 不能为空" },
			persistentTimeout:{ required: "连接保持 不能为空", timeout:"连接保持只能输入数字，范围是60-86400，输入0表示关闭连接保持" },
			persistentNetmask:{ required: "连接保持网络掩码 不能为空", isip:"连接保持网络掩码 IP地址不合法" }
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
		$("#update").val("0");
		$("#oldService").val("");				
		kylin.klb.sirtualserv.formenable();
		kylin.klb.sirtualserv.formreset();
		kylin.klb.sirtualserv.popbox();
		$("#haType").val("0");
		$("#scheduling").val("0");
		$("#persistentTimeout").val("0");
		$("#persistentNetmask").val("255.255.255.255");
	});
	
	$('#save_scheduler').click(function(){
		return kylin.klb.sirtualserv.checkSave();
	});
	
});

(function(package){
	jQuery.extend(package, {
		//设置弹出层
		setss : function (jsonp){
			$("#update").val("1");
			package.formdisabled();
			kylin.klb.sirtualserv.popbox();
			
			package.message("正在加载数据....");
			if(typeof jsonp != "undefined") {
				$.ajax({
					type: "POST",
					url: "klb-load-sirtual-serv!input.action",
					data: {"vsId":jsonp.vsId},
					dataType : 'json',
					timeout : 20000,
					success: function(json){
						if(typeof json != "undefined") {
							var obj = json.obj;
							if(json.auth == true) {
								if(typeof obj != "undefined") {
									package.formenable();
									package.formreset();
									var serviceName = obj.service;
									/* if(serviceName.match("^[_][\\w]*$")){
										serviceName = serviceName.substr(1,serviceName.length-1);
									} */
									$("#service").val(serviceName);
									$("#vsId").val(obj.vsId);
									$("#haType").val(obj.haType);
									$("#interfaces").val(obj.interfaces);
									$("#vipMark").val(obj.vipMark);
									
									if(obj.tcpPorts != "" /*&& obj.tcpPorts != "80"*/) {
										$("#tcpPorts").attr("disabled",false);
										$("#tcptd").addClass("enable1").removeClass("disabled");
										$("#tcpPorts").val(obj.tcpPorts);
										$("#tcp").attr("checked",true);
									}
									if(obj.udpPorts != "") {
										$("#udpPorts").attr("disabled",false);
										$("#udptd").addClass("enable1").removeClass("disabled");
										$("#udpPorts").val(obj.udpPorts);
										$("#udp").attr("checked",true);
									}
									$("#trafficUp").val(obj.trafficUp);
									$("#trafficDown").val(obj.trafficDown);
									$("#scheduling").val(obj.scheduling);
									$("#persistentTimeout").val(obj.persistentTimeout);
									$("#persistentNetmask").val(obj.persistentNetmask);
									$("#oldService").val(jsonp.service);
									package.message("");
								}
							} else {
								package.message("加载数据时出现错误");
							}
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
			
			var tcp1 = "";
			var udp1 = "";
			if($("#tcp")[0].checked) {
				tcp1 = $("#tcpPorts").val();
			} /* else {
				tcp1 = "80";
			} */
			if($("#udp")[0].checked) {
				udp1 = $("#udpPorts").val();
			}
			var jsonDate = {
				"vsId":$("#vsId").val(),
				"service":$("#service").val(),
				"haType":$("#haType").val(),
				"interfaces":$("#interfaces").val(),
				"vipMark":$("#vipMark").val(),
				"tcpPorts":tcp1,
				"udpPorts":udp1,
				"trafficUp":$("#trafficUp").val(),
				"trafficDown":$("#trafficDown").val(),
				"scheduling":$("#scheduling").val(),
				"persistentTimeout":$("#persistentTimeout").val(),
				"persistentNetmask":$("#persistentNetmask").val(),
				"oldService":$("#oldService").val(),
				"update":$("#update").val()
			};
			
			$.ajax({
				type: "POST",
				url: "klb-load-sirtual-serv!save.action",
				data: jsonDate,
				dataType : 'json',
				timeout : 20000,
				success: function(json){
					if(typeof json != "undefined") {
						$("#save_scheduler").attr("disabled",false);
						if(json.auth == true) {							
							location.reload();
						} else {
							alert(json.mess);
							location.reload();
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
		startss : function (obj,jsonp){
			if(typeof jsonp != "undefined") {
				if(confirm("要改变当前状态吗?")) {
					var val = $.trim($(obj).text());
					var statusVal = "关闭";
					var status = "false";
					$(obj).parent().append("<a href=\"javascript:;\">正在"+val+"...</a>");
					$(obj).html("");
					$("#message").html("正在"+val+"...").hide().fadeIn();
					
					if(val == "关闭") {
						statusVal = "开启";
						status = "false";
					} else {
						statusVal = "关闭";
						status = "true";
					}
					$.ajax({
						type: "POST",
						url: "klb-load-sirtual-serv!start.action",
						data: {"vsId":jsonp.vsId,"status":status},
						dataType : 'json',
						timeout : 20000,
						success: function(json){
							if(typeof json != "undefined") {
								if(json.auth == true) {
									$(obj).html(statusVal);
									$(obj).next().remove();
									$("#message").html(val+" 操作成功").show().fadeOut(3000);
									if (val == "关闭") {
										$(obj).parent().parent().siblings(".stc").html(val).css("color","red");
									} else {
										$(obj).parent().parent().siblings(".stc").html(val).css("color","green");
									}
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
		},
		delss : function (obj,jsonp){
			if(confirm("确实要删除吗?")) {
				$("#message").html("正在删除...").hide().fadeIn();
				$(obj).parent().append("<a href=\"javascript:;\">正在删除...</a>");
				$(obj).html("");
				
				if(typeof jsonp != "undefined") {
					$.ajax({
						type: "POST",
						url: "klb-load-sirtual-serv!delete.action",
						data: {"vsId":jsonp.vsId},
						dataType : 'json',
						timeout : 20000,
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
		
		checkSave : function () {
			var tcpPorts = $("#tcpPorts");
			var udpPorts = $("#udpPorts");
			var flag = true;
			if( $.trim(tcpPorts.val()) == "" && $.trim(udpPorts.val()) == "" ) {
				flag = false;
				package.message("TCP端口和UDP端口不能全为空");
				return false;
			}
			return flag;
		},
		formreset : function () {
			$("#inputForm")[0].reset();
			package.message("");
			$("#udpPorts").attr("disabled",true);
			$("#udptd").addClass("disabled").removeClass("enable1");
			$("#tcpPorts").attr("disabled",true);
			$("#tcptd").addClass("disabled").removeClass("enable1");
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
})(kylin.klb.sirtualserv);
