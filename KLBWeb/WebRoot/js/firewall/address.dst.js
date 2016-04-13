/**
 * @fileOverview 麒麟天平链路负载均衡系统
 * 防火墙管理-地址转换-目的地址转换
 * @author kylin
 */
if (!window.address) {
	window.address = {};
}
if (!window.address.dst) {
	window.address.dst = {};
}
 
 $(document).ready(function(){
 	
 	var dealProtocol = function(){
		var protocol = $("#protocol").val();
		if ( protocol == "tcp" || protocol == "udp" || protocol === ""){
			$("#protocols").val(protocol);
		}
		else{
			$("#protocols").val("other");
		}
	};
 	var dealHide = function(){
		var protocols = $("#protocols").val();
		if ( protocols == "other" ){		
			$(".autoHide").show();
		}
		else{
			$(".autoHide").hide();
		}
	};
	var dealPort = function(){
		var protocols = $("#protocols").val();
		if ( protocols == "tcp" || protocols == "udp" ){
			$(".portGray").removeClass("gray9");
			$("#srcPort").attr("disabled", false);
			$("#destPort").attr("disabled", false);
			if($("#except").attr("checked")) {
				$(".exceptGray").addClass("gray9");
				$("#startPort").attr("disabled", true);
				$("#endPort").attr("disabled", true);
			} else {
				$("#startPort").attr("disabled", false);
				$("#endPort").attr("disabled", false);
			}
		}
		else{
			$("#srcPort").val("");
			$("#destPort").val("");
			$("#startPort").val("");
			$("#endPort").val("");
			$(".portGray").addClass("gray9");
			$("#srcPort").attr("disabled", true);
			$("#destPort").attr("disabled", true);
			$("#startPort").attr("disabled", true);
			$("#endPort").attr("disabled", true);
		}
	};
	var dealExcept = function(){
		var type = $("#type").val();
		if(type=="EXPT"){
			$("#except").attr("checked",true);
			$(".exceptGray").addClass("gray9");
			$("#startIP").attr("disabled", true);
			$("#endIP").attr("disabled", true);
			$("#startPort").attr("disabled", true);
			$("#endPort").attr("disabled", true);
		}
		else{
			$("#except").attr("checked",false);
		}	
	};
	dealProtocol();
 	dealHide();
 	dealPort();
 	dealExcept();
 	
	$("#protocols").change( function() {
 		var protocols = $("#protocols").val();
		if ( protocols=="other" ){
			$("#protocol").val("");
			$(".autoHide").show();
			$("#srcPort").val("");
			$("#destPort").val("");
			$("#startPort").val("");
			$("#endPort").val("");
			$(".portGray").addClass("gray9");
			$("#srcPort").attr("disabled", true);
			$("#destPort").attr("disabled", true);
			$("#startPort").attr("disabled", true);
			$("#endPort").attr("disabled", true);
		} else if( protocols == "tcp" || protocols == "udp" ) {
			$("#protocol").val($("#protocols").val());
			$(".autoHide").hide();
			$(".portGray").removeClass("gray9");
			$("#srcPort").attr("disabled", false);
			$("#destPort").attr("disabled", false);
			if($("#except").attr("checked")) {
				$(".exceptGray").addClass("gray9");
				$("#startPort").attr("disabled", true);
				$("#endPort").attr("disabled", true);
			} else {
				$("#startPort").attr("disabled", false);
				$("#endPort").attr("disabled", false);
			}						
		} else {
			$("#protocol").val($("#protocols").val());
			$(".autoHide").hide();
			$("#srcPort").val("");
			$("#destPort").val("");
			$("#startPort").val("");
			$("#endPort").val("");
			$(".portGray").addClass("gray9");
			$("#srcPort").attr("disabled", true);
			$("#destPort").attr("disabled", true);
			$("#startPort").attr("disabled", true);
			$("#endPort").attr("disabled", true);
		}
	});
	
	$("#except").click(function() {
		var protocols = $("#protocols").val();
		if($("#except").attr("checked")){
			$("#startIP").val("");
			$("#endIP").val("");
			$("#startPort").val("");
			$("#endPort").val("");
			$(".exceptGray").addClass("gray9");
			$("#startIP").attr("disabled", true);
			$("#endIP").attr("disabled", true);
			$("#startPort").attr("disabled", true);
			$("#endPort").attr("disabled", true);
			$("#type").val("EXPT");			
		} else {
			$(".exceptGray").removeClass("gray9");
			$("#startIP").attr("disabled", false);
			$("#endIP").attr("disabled", false);
			if( protocols == "tcp" || protocols == "udp" ) {
				$("#startPort").attr("disabled", false);
				$("#endPort").attr("disabled", false);
			} else {
				$(".portGray").addClass("gray9");
				$("#startPort").attr("disabled", true);
				$("#endPort").attr("disabled", true);
			}
			$("#type").val("DNET");
		}
	});
	
	$('#inputForm').submit(function(){
		return address.dst.check();
	});
	
});

(function(package){
	jQuery.extend(package, {		
		check : function () {
			var srcNet = $("#srcIP");
			var destNet = $("#destIP");
			var protocols = $("#protocols");
			var protocol = $("#protocol");
			var srcPort = $("#srcPort");
			var destPort = $("#destPort");
			var describe = $("#describe");			
			var startIP = $("#startIP");
			var endIP = $("#endIP");
			var startPort = $("#startPort");
			var endPort = $("#endPort");
			var flag = true;
			
			var v = srcNet.val().split("/");			
			if( srcNet.val()!="" && v.length==2 && $.trim(v[0]) == "") {
				flag = false;
				package.message("“/”前的源地址不能是空");
				srcNet.focus();
				return false;
			}
			if(!package.ipCheck(v[0])) {
				flag = false;
				package.message("源地址 IP不合法");
				srcNet.focus();
				return false;
			}
			/* if(!package.ipFirst(v[0])) {
				flag = false;
				package.message("源地址IP首位 请输入1~223之间的整数");
				srcNet.focus();
				return false;
			} */
			if( srcNet.val()!="" && v.length==2 && $.trim(v[1]) == "") {
				flag = false;
				package.message("“/”后的源掩码不能是空");
				srcNet.focus();
				return false;
			}
			if(!package.maskCheck(v[1])) {
				flag = false;
				package.message("源掩码 请输入0~32之间的整数");
				srcNet.focus();
				return false;
			}
						
			var destv = destNet.val().split("/");			
			if( destNet.val()!="" && destv.length==2 && $.trim(destv[0]) == "") {
				flag = false;
				package.message("“/”前的目的地址不能是空");
				destNet.focus();
				return false;
			}
			if(!package.ipCheck(destv[0])) {
				flag = false;
				package.message("目的地址 IP不合法");
				destNet.focus();
				return false;
			}			
			if( destNet.val()!="" && destv.length==2 && $.trim(destv[1]) == "") {
				flag = false;
				package.message("“/”后的目的掩码不能是空");
				destNet.focus();
				return false;
			}
			if(!package.maskCheck(destv[1])) {
				flag = false;
				package.message("目的掩码 请输入0~32之间的整数");
				destNet.focus();
				return false;
			}
			
			if(!package.portCheck(srcPort.val())) {
				flag = false;
				package.message("源端口 请输入0~65535之间的整数");
				srcPort.focus();
				return false;
			}
			if(!package.portCheck(destPort.val())) {
				flag = false;
				package.message("目的端口 请输入0~65535之间的整数");
				destPort.focus();
				return false;
			}
			/* if(!package.describeCheck(describe.val())) {
				flag = false;
				package.message("规则描述 请输入英文字母、数字或下划线");
				describe.focus();
				return false;
			} */
			
			if( !($("#except").attr("checked")) && $.trim(startIP.val()) == "" ) {
				flag = false;
				package.message("目的地址转换 起始地址不能为空");
				startIP.focus();
				return false;
			}
			
			if(!package.ipCheck(startIP.val())) {
				flag = false;
				package.message("转换后的起始地址不合法");
				startIP.focus();
				return false;
			}
			if(!package.ipFirst(startIP.val())) {
				flag = false;
				package.message("起始IP首位 请输入1~223之间的整数");
				startIP.focus();
				return false;
			}
			if(!package.ipCheck(endIP.val())) {
				flag = false;
				package.message("转换后的终止地址不合法");
				endIP.focus();
				return false;
			}
			if(!package.ipFirst(endIP.val())) {
				flag = false;
				package.message("终止IP首位 请输入1~223之间的整数");
				endIP.focus();
				return false;
			}
			if(!package.portCheck(startPort.val())) {
				flag = false;
				package.message("转换后的起始端口不合法");
				startPort.focus();
				return false;
			}
			if(!package.portCheck(endPort.val())) {
				flag = false;
				package.message("转换后的终止端口不合法");
				endPort.focus();
				return false;
			}
			
			return flag;
		},
		message : function(msg) {
			if($.trim(msg) == "") return;
			$("#show").html(msg).hide().fadeIn();
		},
		ipCheck : function (val) {
			if($.trim(val) == "") return true;
			return (/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/.test(val) && (RegExp.$1 <256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256));
		},
		ipFirst : function (val) {
			if($.trim(val) == "") return true;
			return (/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/.test(val) && (RegExp.$1>0 && RegExp.$1 <224));
		},
		maskCheck : function (val) {
			if($.trim(val) == "") return true;
			return (/^(\d+)$/.test(val)) && ((RegExp.$1 <= 32))&&((RegExp.$1 >= 0));
		},
		describeCheck : function (val) {
			if($.trim(val) == "") return true;
			return /^[\w]+$/.test(val);
		},
		portCheck : function (val) {
			if($.trim(val) == "") return true;
			return (/^(\d+)$/.test(val)) && ((RegExp.$1 < 65536))&&((RegExp.$1 >= 0));
		}
	});
})(address.dst);
