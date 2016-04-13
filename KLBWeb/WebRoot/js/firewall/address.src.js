/**
 * @fileOverview 麒麟天平链路负载均衡系统
 * 防火墙管理-地址转换-源地址转换
 * @author kylin
 */
if (!window.address) {
	window.address = {};
}
if (!window.address.src) {
	window.address.src = {};
}
 
 $(document).ready(function(){
 	
 	var dealType = function(){
		var type = $("#masq").val();
		if(type=="MASQ"){
			$("#type").val("MASQ");
		}
	};
	
	var dealRadio = function(){
		var type = $("#masq").val();
		if(type=="MASQ"){
			$("#dynamicIp").attr("checked",true);
			$("#startIP").attr("disabled", true);
			$("#endIP").attr("disabled", true);
			$(".autoGray").addClass("gray9");
		}
		else{
			$("#fixedIp").attr("checked",true);
			
		}
	};
	var dealExcept = function(){
		var type = $("#type").val();
		if(type=="EXPT"){
			$("#except").attr("checked",true);
			$(".exceptGray").addClass("gray9");
			$("#dynamicIp").attr("disabled", true);
			$("#fixedIp").attr("disabled", true);
			$("#startIP").attr("disabled", true);
			$("#endIP").attr("disabled", true);
			$("#startPort").attr("disabled", true);
			$("#endPort").attr("disabled", true);
		}
		else{
			$("#except").attr("checked",false);
		}	
	};
	
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
	dealProtocol();
 	dealHide();
 	dealPort();
 	dealType();
 	dealRadio();
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
			$("#dynamicIp").attr("disabled", true);
			$("#fixedIp").attr("disabled", true);
			$("#startIP").attr("disabled", true);
			$("#endIP").attr("disabled", true);
			$("#startPort").attr("disabled", true);
			$("#endPort").attr("disabled", true);
			$("#type").val("EXPT");
		} else {
			$(".exceptGray").removeClass("gray9");
			$("#dynamicIp").attr("disabled", false);
			$("#fixedIp").attr("disabled", false);
			if ( $(".radio:checked").attr("id") == "fixedIp") {
				$("#fixedIp").click();
			} else {
				$("#dynamicIp").click();
			}
			if( protocols == "tcp" || protocols == "udp" ) {
				$("#startPort").attr("disabled", false);
				$("#endPort").attr("disabled", false);
			} else {
				$(".portGray").addClass("gray9");
				$("#startPort").attr("disabled", true);
				$("#endPort").attr("disabled", true);
			}			
		}
	});
	
	$("#dynamicIp").click(function() {
		$("#startIP").val("");
		$("#endIP").val("");
		$("#startIP").attr("disabled", true);
		$("#endIP").attr("disabled", true);
		$(".autoGray").addClass("gray9");
		$("#type").val("MASQ");
	});
	
	$("#fixedIp").click(function() {
		$("#startIP").attr("disabled", false);
		$("#endIP").attr("disabled", false);
		$(".autoGray").removeClass("gray9");
		$("#type").val("SNAT");
	});
	
	$('#inputForm').submit(function(){
		return address.src.check();
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
			
			/* if( srcNet.val()=="" && destNet.val()=="" ) {
				flag = false;
				package.message("源地址/掩码和目的地址/掩码不能全为空！");
				return false;
			} */
			
			var v = srcNet.val().split("/");
			/* if( srcNet.val()!="" && v.length!=2 ) {
				flag = false;
				package.message("源地址/掩码格式不正确");
				srcNet.focus();
				return false;
			} */
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
			/* if( destNet.val()!="" && destv.length!=2 ) {
				flag = false;
				package.message("目的地址/掩码格式不正确");
				destNet.focus();
				return false;
			} */
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
			
			if( !($("#except").attr("checked")) && $("#fixedIp").attr("checked") && $.trim(startIP.val()) == "" ) {
				flag = false;
				package.message("固定IP转换 起始地址不能为空");
				startIP.focus();
				return false;
			}			
			if(!package.ipCheck(startIP.val())) {
				flag = false;
				package.message("固定IP转换 起始地址不合法");
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
				package.message("固定IP转换 终止地址不合法");
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
				package.message("源端口转换 起始端口不合法");
				startPort.focus();
				return false;
			}
			if(!package.portCheck(endPort.val())) {
				flag = false;
				package.message("源端口转换 终止端口不合法");
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
})(address.src);
