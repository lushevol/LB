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
if (!window.kylin.klb.set3) {
	window.kylin.klb.set3 = {};
}

$(document).ready(function(){
	
});

function goSet2 () {
	$("#inputForm").attr("action","klb-set-2.action");
	$("#inputForm").submit();
}

function sub () {
	if(kylin.klb.set3.formCheck()) {
		kylin.klb.set3.complet();
	}
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
			return (/^\d+$/.test(val));
		},
		formCheck : function (){
			var sname = $("#serviceName");
			if($.trim(sname.val()) == "") {
				package.message("真实服务器名称不能为空");
				sname.focus();
				return false;
			}
			
			var ip = $("#ip");
			if($.trim(ip.val()) == "") {
				package.message("真实服务器IP地址不能为空");
				ip.focus();
				return false;
			}
			
			if(!package.isIp(ip.val())) {
				package.message("真实服务器IP地址地址不合法");
				ip.focus();
				return false;
			}
			var forward = $("#forward");
			if($.trim(forward.val()) == "") {
				package.message("转发方式不能为空");
				forward.focus();
				return false;
			}
			return true;
		},
		complet : function() {
			package.message("正在提交数据...","1");
			$("button").attr("disabled",true);
			var jsonDate = {
				"guide":$("#guide").val(),
				"udpPorts":$("#udpPorts").val(),
				"tcpPorts":$("#tcpPorts").val(),
				"vipMark":$("#vipMark").val(),
				"persistent":$("#persistent").val(),
				"ip":$("#ip").val(),
				"forward":$("#forward").val(),
				"service":$("#service").val(),
				"serviceName":$("#serviceName").val()
			};
			
			$.ajax({
				type: "POST",
				url: "klb-set!save.action",
				data: jsonDate,
				dataType : 'json',
				timeout : 20000,
				success: function(json){
					if(typeof json != "undefined") {
						var obj = json.obj;
						if(json.auth == true) {
							alert("设置成功");
							location.href="klb-index.action";
						} else {
							package.message("提交数据时出现错误");
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
})(kylin.klb.set3);
