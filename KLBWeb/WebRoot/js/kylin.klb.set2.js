/**
 * @fileOverview 麒麟天平负载均衡系统
 * 设置向导
 * @author kylin
 */
 
if (!window.kylin) {
	window.kylin = {};
}
if (!window.kylin.klb) {
	window.kylin.klb = {};
}
if (!window.kylin.klb.set2) {
	window.kylin.klb.set2 = {};
}

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
});

function goSet3 () {
	if(kylin.klb.set2.formCheck()) {
		kylin.klb.set2.nextStep();
	}
}
function goSet1 () {
	$("#inputForm").attr("action","klb-set-1.action");
	$("#inputForm").submit();
	return false;
}
function cancel () {
	var guide = $("#guide");
	location.href="klb-set-cancel.action?guide="+guide.val();
	return false;
}

(function(package){
	jQuery.extend(package, {
		isIp : function (val){
			return (/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/.test(val) && (RegExp.$1 <256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256));
		},
		digits : function(val){
			return (/^(\d+)$/.test(val)) && ((RegExp.$1 < 86400 && RegExp.$1>=60) || (RegExp.$1==0));
		},
		
		isPort : function(val){
			return (/^(\d+(-\d+)?,)*(\d+(-\d+)?)$/.test(val));
		},
		
		isStr : function(val){
			return (/^[\w]+$/.test(val));
		},
		
		formCheck : function (){
			var service = $("#service");
			if($.trim(service.val()) == "") {
				package.message("虚拟服务名称不能为空");
				service.focus();
				return false;
			}
			
			if(!package.isStr(service.val())) {
				package.message("虚拟服务名称只能包括英文字母、数字和下划线");
				service.focus();
				return false;
			}
			
			var vipMark = $("#vipMark");
			if($.trim(vipMark.val()) == "") {
				package.message("虚拟服务IP地址不能为空");
				vipMark.focus();
				return false;
			}
			
			if(!package.isIp(vipMark.val())) {
				package.message("虚拟服务IP地址不合法");
				vipMark.focus();
				return false;
			}
			var pn = $("#persistentNetmask");
			if($.trim(pn.val()) == "") {
				package.message("虚拟服务掩码不能为空");
				pn.focus();
				return false;
			}
			
			if(!package.isIp(pn.val())) {
				package.message("虚拟服务掩码IP地址不合法");
				pn.focus();
				return false;
			}
			var tcpPorts = $("#tcpPorts");
			if($("#tcp").attr("checked")) {
				if($.trim(tcpPorts.val()) == "") {
					package.message("TCP端口不能为空");
					tcpPorts.focus();
					return false;
				}
				if(!package.isPort(tcpPorts.val())) {
					package.message("TCP端口输入不合法");
					tcpPorts.focus();
					return false;
				}
			}
			
			if($("#udp").attr("checked")) {
				var udpPorts = $("#udpPorts");
				if($.trim(udpPorts.val()) == "") {
					package.message("UDP端口不能为空");
					udpPorts.focus();
					return false;
				}
				if(!package.isPort(udpPorts.val())) {
					package.message("UDP端口输入不合法");
					udpPorts.focus();
					return false;
				}
			}
			var persistent = $("#persistent");
			if($.trim(persistent.val()) == "") {
				package.message("连接保持不能为空");
				persistent.focus();
				return false;
			}
			if(!package.digits(persistent.val())) {
				package.message("连接保持只能是0或者60-86400之间的数值");
				persistent.focus();
				return false;
			}
			return true;
		},
		nextStep : function () {
			//$("input").attr("disabled",true);
			$("button").attr("disabled",true);
			package.message("正在处理...","1");
			
			var service = $("#service");
			var udpPorts = $("#udpPorts");
			var tcpPorts = $("#tcpPorts");
			var pn = $("#persistentNetmask");
			var vipMark = $("#vipMark");
			var persistent = $("#persistent");
			
			var jsonDate = {
				"service":service.val(),
				"guide":$("#guide").val(),
				"udpPorts":udpPorts.val(),
				"tcpPorts":tcpPorts.val(),
				"persistentNetmask":pn.val(),
				"vipMark":vipMark.val(),
				"persistent":persistent.val()
			};
			
			$.ajax({
				type: "POST",
				url: "klb-set-2-check.action",
				data: jsonDate,
				dataType : 'json',
				timeout : 20000,
				success: function(json){
					if(typeof json != "undefined") {
						var obj = json.obj;
						if(json.auth == true) {
							$("#inputForm").submit();
						} else {
							package.message(json.mess);
						}
					}
					$("button").attr("disabled",false);
				},
				error : function(){
					package.message("提交数据时出现错误");
					$("button").attr("disabled",false);
				}
			});
		},
		message : function (msg,isloading) {
			if($.trim(msg) == "") $("#show").html("");
			if(typeof isloading != "undefined" && $.trim(isloading) != "") {
				$("#show").html("<img src=\"css/skin/images/common/loading3.gif\" width=\"15\" height=\"15\"/>"+msg);
			} else $("#show").html(msg);
		}
	});
})(kylin.klb.set2);
