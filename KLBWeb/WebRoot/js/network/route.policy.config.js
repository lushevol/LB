/**
 * @fileOverview 麒麟天平链路负载均衡系统
 * 路由配置-策略路由-添加
 * @author kylin
 */
 
 if (!window.route) {
	window.route = {};
}
if (!window.route.policy) {
	window.route.policy = {};
}
if (!window.route.policy.config) {
	window.route.policy.config = {};
}

$(document).ready(function(){
 	//dealGateOrInter();
	route.policy.config.dealGate();
	route.policy.config.dealRule();
	dealProtocol();
 	dealHide();	
	/* function dealGateOrInter(){
		if($("#gateOrInter").val()=="gate"){
			$("#interface").val("");
			$(".interGray").removeClass("gray7");
			$(".gateGray").addClass("gray7");
			$("#interface").attr("disabled",true);
			$("#gate").attr("disabled",false);
			
		}
		else{			
			$("#gate").val("");
			$(".gateGray").removeClass("gray7");
			$(".interGray").addClass("gray7");
			$("#interface").attr("disabled",false);
			$("#gate").attr("disabled",true);
			
		}
	}
	$("#gateSet").click(function() {
		$(".interGray").removeClass("gray7");
		$(".gateGray").addClass("gray7");
		$("#gateOrInter").val("gate");
		dealGateOrInter();
	});
	
	$("#interSet").click(function() {
		$(".gateGray").removeClass("gray7");
		$(".interGray").addClass("gray7");
		$("#gateOrInter").val("inter");
		dealGateOrInter();
	});	*/
	
	$("#auto").click(function() {
		if($("#auto").attr("checked")){
			$("#gate").val("");
			$(".interGray").addClass("gray7");
			$("#gate").attr("disabled",true);
		} else {
			$(".interGray").removeClass("gray7");
			$("#gate").attr("disabled",false);
		}
	});
 
 	function dealProtocol(){
 		$("#protocol").val("");
		var protocol = $("#protocol").val();
		$("#protocols").val(protocol);
		/* if ( protocol == "tcp" || protocol == "udp" || protocol === ""){
			$("#protocols").val(protocol);
		}
		else{
			$("#protocols").val("other");
		} */
	};
 	function dealHide(){
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
		}
		else{
			$("#srcPort").val("");
			$("#destPort").val("");			
			$(".portGray").addClass("gray9");
			$("#srcPort").attr("disabled", true);
			$("#destPort").attr("disabled", true);
		}
	};
	dealPort();
	
 	$("#protocols").change( function() {
 		var protocols = $("#protocols").val();
		if ( protocols=="other" ){
			$("#protocol").val("");
			$(".autoHide").show();
			$("#srcPort").val("");
			$("#destPort").val("");			
			$(".portGray").addClass("gray9");
			$("#srcPort").attr("disabled", true);
			$("#destPort").attr("disabled", true);			
		} else if( protocols == "tcp" || protocols == "udp" ) {
			$("#protocol").val($("#protocols").val());
			$(".autoHide").hide();
			$(".portGray").removeClass("gray9");
			$("#srcPort").attr("disabled", false);
			$("#destPort").attr("disabled", false);									
		} else {
			$("#protocol").val($("#protocols").val());
			$(".autoHide").hide();
			$("#srcPort").val("");
			$("#destPort").val("");			
			$(".portGray").addClass("gray9");
			$("#srcPort").attr("disabled", true);
			$("#destPort").attr("disabled", true);			
		}
	});
	
	$('#policyConfigForm').submit(function(){
		return route.policy.config.checkSave();
	});
	
});

(function(package){
	jQuery.extend(package, {
		
		dealRule : function () {
			var rules = $("#rules").val();
			var rulesTable = document.getElementById('ruleTable');
			ruleArray = rules.split(";");
			for(var temp=0; temp<ruleArray.length-1; temp+=1){
				newRow = rulesTable.insertRow(temp+1);
				netArray = ruleArray[temp].split(",");
				for(var i=0; i<netArray.length; i+=1){
					newCell = newRow.insertCell(i);
					newCell.innerHTML = netArray[i];
				}
				
				newCell5 = newRow.insertCell(5);
				newCell5.innerHTML = "<a href=\"javascript:;\" onclick=\"route.policy.config.delRule(" + (temp+1) + ")\">删除</a>";
			}
		},
		delRule : function (rowIndex){
			if(confirm("确实要删除吗?")) {
				var rulesTable = document.getElementById('ruleTable');
				
				rulesTable.deleteRow(rowIndex);
				rowLen = rulesTable.rows.length;
				
				for(var temp=rowIndex; temp<rowLen; temp+=1){
					newRow = rulesTable.rows[temp];
					newCell = newRow.cells[5];
					newCell.innerHTML = "<a href=\"javascript:;\" onclick=\"route.policy.config.delRule(" + temp + ")\">删除</a>";
				}
				var rules = $("#rules").val();
				ruleArray = rules.split(";");
				var rulesNew = "";
				for(var i=0; i<ruleArray.length-1; i+=1){
					if((rowIndex-1) != i){
						rulesNew = rulesNew + ruleArray[i] + ";";
					}
				}
				$("#rules").val(rulesNew);
			}
		},
		addRule : function () {
			var srcNet = $("#srcNet").val();
			var destNet = $("#destNet").val();
			var protocol = $("#protocol").val();
			var srcPort = $("#srcPort").val();
			var destPort = $("#destPort").val();
			
			var rules = $("#rules").val();
			var rulesTable = document.getElementById('ruleTable');
			if ( route.policy.config.checkRule() ) {
				rowLen = rulesTable.rows.length;
				newRow = rulesTable.insertRow(rowLen);
				newCell0 = newRow.insertCell(0);
				newCell0.innerHTML = srcNet;
				newCell1 = newRow.insertCell(1);
				newCell1.innerHTML = destNet;
				newCell2 = newRow.insertCell(2);
				newCell2.innerHTML = protocol;
				newCell3 = newRow.insertCell(3);
				newCell3.innerHTML = srcPort;
				newCell4 = newRow.insertCell(4);
				newCell4.innerHTML = destPort;
				newCell5 = newRow.insertCell(5);
				newCell5.innerHTML = "<a href=\"javascript:;\" onclick=\"route.policy.config.delRule(" + rowLen + ")\">删除</a>";
				rules = rules + srcNet + ","+ destNet + ","+ protocol + ","+ srcPort + ","+ destPort + ";";
				
				$("#rules").val(rules);
				$("#srcNet").val("");
				$("#destNet").val("");
				$("#protocols").val("");
				$("#protocol").val("");
				$("#srcPort").val("");
				$("#destPort").val("");
			}
		},
		
		dealGate : function () {
			var gates = $("#displayGates").val();
			var gatesTable = document.getElementById('gatesTable');
			gateArray = gates.split(";");
			for(var temp=0; temp<gateArray.length-1; temp+=1){
				newRow = gatesTable.insertRow(temp+1);
				giArray = gateArray[temp].split(",");
				for(var i=0; i<giArray.length; i+=1){
					newCell = newRow.insertCell(i);
					newCell.innerHTML = giArray[i];
				}				
				newCell3 = newRow.insertCell(3);
				newCell3.innerHTML = "<a href=\"javascript:;\" onclick=\"route.policy.config.delGate(" + (temp+1) + ")\">删除</a>";
			}
		},
		
		delGate : function (rowIndex){
			if(confirm("确实要删除吗?")) {
				var gatesTable = document.getElementById('gatesTable');
				var inter = gatesTable.rows[rowIndex].cells[0].innerHTML;
				var gate = gatesTable.rows[rowIndex].cells[1].innerHTML;
				var weight = gatesTable.rows[rowIndex].cells[2].innerHTML;
				gatesTable.deleteRow(rowIndex);
				rowLen = gatesTable.rows.length;
				
				for(var temp=rowIndex; temp<rowLen; temp+=1){
					newRow = gatesTable.rows[temp];
					newCell = newRow.cells[3];
					newCell.innerHTML = "<a href=\"javascript:;\" onclick=\"route.policy.config.delGate(" + temp + ")\">删除</a>";
				}
				var gates = $("#gates").val();
				giArray = gates.split(";");
				var gisNew = "";
				for(var i=0; i<giArray.length-1; i+=1){
					if((rowIndex-1) != i){
						gisNew = gisNew + giArray[i] + ";";
					}
				}
				$("#gates").val(gisNew);
			}
		},
		addGates : function () {			
			var gate = $("#gate").val();										
			var inter = $("#interface").val();
			var gates = $("#gates").val();
			var weight = $("#weight").val();
			var gatesTable = document.getElementById('gatesTable');
			if ( route.policy.config.check() ) {
				rowLen = gatesTable.rows.length;
				newRow = gatesTable.insertRow(rowLen);
				newCell0 = newRow.insertCell(0);
				if($("#auto").attr("checked")){
					newCell0.innerHTML = "自动";
				} else {
					newCell0.innerHTML = gate;
				}
				newCell1 = newRow.insertCell(1);
				newCell1.innerHTML = inter;
				newCell2 = newRow.insertCell(2);
				newCell2.innerHTML = weight;
				newCell3 = newRow.insertCell(3);
				newCell3.innerHTML = "<a href=\"javascript:;\" onclick=\"route.policy.config.delGate(" + rowLen + ")\">删除</a>";
				if($("#auto").attr("checked")){
					gates = gates + "auto" + "," + inter + "," + weight + ";";
				} else {
					gates = gates + gate + "," + inter + "," + weight + ";";
				}
								
				$("#gates").val(gates);
				if($("#auto").attr("checked")){
					$("#auto").removeAttr("checked");
					$(".interGray").removeClass("gray7");
					$("#gate").attr("disabled",false);
				}
				$("#gate").val("");
				$("#interface").val("");
				$("#weight").val("");
			}
		},
						
		/* addGate : function (gate, inter) {
			var gates = $("#gates").val();
			var weight = $("#weight").val();
			var gatesTable = document.getElementById('gatesTable');
			if ( route.policy.config.check() ) {
				rowLen = gatesTable.rows.length;
				newRow = gatesTable.insertRow(rowLen);
				newCell0 = newRow.insertCell(0);
				newCell0.innerHTML = gate;
				newCell1 = newRow.insertCell(1);
				newCell1.innerHTML = inter;
				newCell2 = newRow.insertCell(2);
				newCell2.innerHTML = weight;
				newCell3 = newRow.insertCell(3);
				newCell3.innerHTML = "<a href=\"javascript:;\" onclick=\"route.policy.config.delGate(" + rowLen + ")\">删除</a>";
				gates = gates + gate + "," + "," + weight + ";";
				
				$("#gates").val(gates);
				$("#gate").val("");
				$("#weight").val("");
			}
		},
		addInter : function (gate, inter) {
			var gates = $("#gates").val();
			var weight = $("#weight").val();
			var gatesTable = document.getElementById('gatesTable');
			if ( route.policy.config.check() ) {
				rowLen = gatesTable.rows.length;
				newRow = gatesTable.insertRow(rowLen);
				newCell0 = newRow.insertCell(0);
				newCell0.innerHTML = gate;
				newCell1 = newRow.insertCell(1);
				newCell1.innerHTML = inter;
				newCell2 = newRow.insertCell(2);
				newCell2.innerHTML = weight;
				newCell3 = newRow.insertCell(3);
				newCell3.innerHTML = "<a href=\"javascript:;\" onclick=\"route.policy.config.delGate(" + rowLen + ")\">删除</a>";
				gates = gates + "," + inter + "," + weight + ";";
				
				$("#gates").val(gates);
				$("#interface").val("");
				$("#weight").val("");
			}
		},
			
		addGates : function () {
			if($("#gateOrInter").val()=="gate"){
				gate = $("#gate").val();
				route.policy.config.addByGate(gate);
			}
			else{
				inter = $("#interface").val();
				route.policy.config.addByInter(inter);
			}
		},
		addByInter : function (inter){
			$.ajax({
				type: "POST",
				url: "route-policy-config!getGate.action",
				data: {"gi":inter},
				dataType : 'json',
				timeout : 20000,
				success: function(json){
					if(typeof json != "undefined") {
						if(json.auth == true) {
							gate = json.gate;
							route.policy.config.addInter(gate, inter);
						}
						else {
							alert(json.mess);
						}
					}
				}, error : function(){
					$(obj).html(val);
					$(obj).next().remove();
					$("#message").html(val+" 操作失败").show().fadeOut(2000);
				}
			});
		},
		addByGate : function (gate){			
			$.ajax({
				type: "POST",
				url: "route-policy-config!getInter.action",
				data: {"gi":gate},
				dataType : 'json',
				timeout : 20000,
				success: function(json){
					if(typeof json != "undefined") {
						if(json.auth == true) {
							inter = json.inter;
							route.policy.config.addGate(gate, inter);
						} else {
							alert(json.mess);
						}
					}
				}, error : function(){
					$(obj).html(val);
					$(obj).next().remove();
					$("#message").html(val+" 操作失败").show().fadeOut(2000);
				}
			});
		}, */
		
		checkRule : function () {
			var srcNet = $("#srcNet");
			var destNet = $("#destNet");
			var protocols = $("#protocols");
			var protocol = $("#protocol");
			var srcPort = $("#srcPort");
			var destPort = $("#destPort");
			var flag = true;
			
			if( srcNet.val()=="" && destNet.val()=="" && protocol.val()=="" ) {
				flag = false;
				package.message("添加地址和协议不能全为空！");
				return false;
			}
			if( srcPort.val()!="" && protocols.val()=="" ) {
				flag = false;
				package.message("添加源端口 则协议不能为空");
				protocols.focus();
				return false;
			}
			if( destPort.val()!="" && protocols.val()=="" ) {
				flag = false;
				package.message("添加目的端口 则协议不能为空");
				protocols.focus();
				return false;
			}
			
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
			if(!package.ipFirst(v[0])) {
				flag = false;
				package.message("源地址IP首位 请输入1~223之间的整数");
				srcNet.focus();
				return false;
			}
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
			if(!package.ipFirst(destv[0])) {
				flag = false;
				package.message("目的地址IP首位 请输入1~223之间的整数");
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
			return flag;
		},
		
		check : function () {
			var gate = $("#gate");
			var inter = $("#interface");
			var weight = $("#weight");
			var flag = true;
			/* if( gate.attr("disabled")==false && $.trim(gate.val())=="" ) {
					flag = false;
					package.message("网关不能是空");
					gate.focus();
					return false;
			} */
			if( gate.attr("disabled")==false && !package.ipCheck(gate.val())) {
				flag = false;
				package.message("网关 IP不合法");
				gate.focus();
				return false;
			}
			if( gate.attr("disabled")==false && !package.ipFirst(gate.val())) {
				flag = false;
				package.message("IP地址首位 请输入1~223之间的整数");
				gate.focus();
				return false;
			}
			if( inter.attr("disabled")==false && $.trim(inter.val())=="" ) {
				flag = false;
				package.message("接口不能是空");
				inter.focus();
				return false;
			}
			
			if( $.trim(weight.val())=="" ) {
				flag = false;
				package.message("权重不能是空");
				weight.focus();
				return false;
			}
			if( !package.weightCheck(weight.val())) {
				flag = false;
				package.message("权重 请输入1~256之间的整数");
				weight.focus();
				return false;
			}
			return flag;
		},
		
		checkSave : function () {			
			var describe = $("#describe");
			var inter = $("#interface");
			var flag = true;			
						
			/* if( !package.describeCheck(describe.val()) ) {
				flag = false;
				package.message("描述信息 请输入英文字母、数字或下划线");
				describe.focus();
				return false;
			} */
			
			if ($("#gatesTable tr").length < 2) {
				flag = false;
				package.message("配置策略路由 请至少添加一条路由网关");
				inter.focus();
				return false;				
			}			
			return flag;
		},
		message : function(msg) {
			if($.trim(msg) == "") $("#show").html("");
			$("#show").html(msg).fadeIn();
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
		weightCheck : function (val) {
			if($.trim(val) == "") return true;
			return (/^(\d+)$/.test(val)) && ((RegExp.$1 < 257))&&((RegExp.$1 > 0));
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
})(route.policy.config);