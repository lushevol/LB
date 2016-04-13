/**
 * @fileOverview 麒麟天平链路负载均衡系统
 * 路由配置-静态路由-静态路由配置
 * @author kylin
 */

if (!window.route) {
	window.route = {};
}
if (!window.route.static) {
	window.route.static = {};
}
if (!window.route.static.config) {
	window.route.static.config = {};
}

$(document).ready(function(){
	//dealGateOrInter();
	
	var dealNetMetric = function(){
		var id = $("#id").val();
		var ip = $("#ip").val();
		var metric = $("#metric").val();
		if ( id != "add" ){
			$("#ipTemp").val(ip);
			$("#metricTemp").val(metric);
			$(".setGray").addClass("gray7");
			$("#ipLabel").text(ip);
			$("#metricLabel").text(metric);
			$(".autoHide").hide();
		}
	};
	dealNetMetric();
	
	route.static.config.dealGate();
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
	
	$('#staticConfigForm').submit(function(){
		return route.static.config.checkSave();
	});
	
});

(function(package){
	jQuery.extend(package, {
	
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
				newCell3.innerHTML = "<a href=\"javascript:;\" onclick=\"route.static.config.delGate(" + (temp+1) + ")\">删除</a>";
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
					newCell.innerHTML = "<a href=\"javascript:;\" onclick=\"route.static.config.delGate(" + temp + ")\">删除</a>";
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
			if ( route.static.config.check() ) {
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
				newCell3.innerHTML = "<a href=\"javascript:;\" onclick=\"route.static.config.delGate(" + rowLen + ")\">删除</a>";
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
			if ( route.static.config.check() ) {
				rowLen = gatesTable.rows.length;
				newRow = gatesTable.insertRow(rowLen);
				newCell0 = newRow.insertCell(0);
				newCell0.innerHTML = gate;
				newCell1 = newRow.insertCell(1);
				newCell1.innerHTML = inter;
				newCell2 = newRow.insertCell(2);
				newCell2.innerHTML = weight;
				newCell3 = newRow.insertCell(3);
				newCell3.innerHTML = "<a href=\"javascript:;\" onclick=\"route.static.config.delGate(" + rowLen + ")\">删除</a>";
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
			if ( route.static.config.check() ) {
				rowLen = gatesTable.rows.length;
				newRow = gatesTable.insertRow(rowLen);
				newCell0 = newRow.insertCell(0);
				newCell0.innerHTML = gate;
				newCell1 = newRow.insertCell(1);
				newCell1.innerHTML = inter;
				newCell2 = newRow.insertCell(2);
				newCell2.innerHTML = weight;
				newCell3 = newRow.insertCell(3);
				newCell3.innerHTML = "<a href=\"javascript:;\" onclick=\"route.static.config.delGate(" + rowLen + ")\">删除</a>";
				gates = gates + "," + inter + "," + weight + ";";
				
				$("#gates").val(gates);
				$("#interface").val("");
				$("#weight").val("");
			}
		},
			
		addGates : function () {
			if($("#gateOrInter").val()=="gate"){
				gate = $("#gate").val();
				route.static.config.addByGate(gate);
			}
			else{
				inter = $("#interface").val();
				route.static.config.addByInter(inter);
			}
		},
		addByInter : function (inter){
			$.ajax({
				type: "POST",
				url: "route-static-config!getGate.action",
				data: {"gi":inter},
				dataType : 'json',
				timeout : 20000,
				success: function(json){
					if(typeof json != "undefined") {
						if(json.auth == true) {
							gate = json.gate;
							route.static.config.addInter(gate, inter);
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
				url: "route-static-config!getInter.action",
				data: {"gi":gate},
				dataType : 'json',
				timeout : 20000,
				success: function(json){
					if(typeof json != "undefined") {
						if(json.auth == true) {
							inter = json.inter;
							route.static.config.addGate(gate, inter);
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
		
		checkSave : function () {
			var ip = $("#ip");			
			var metric = $("#metric");
			var describe = $("#describe");
			var inter = $("#interface");
			var flag = true;
			
			/* if($.trim(ip.val()) == "") {
				flag = false;
				package.message("目的地址不能是空");
				ip.focus();
				return false;
			} */
			
			var v = ip.val().split("/");			
			if( ip.val()!="" && v.length==2 && $.trim(v[0]) == "") {
				flag = false;
				package.message("“/”前的IP地址不能是空");
				ip.focus();
				return false;
			}
			if(!package.ipCheck(v[0])) {
				flag = false;
				package.message("目的地址 IP不合法");
				ip.focus();
				return false;
			}
			if( ip.val()!="" && v.length==2 && $.trim(v[1]) == "") {
				flag = false;
				package.message("“/”后的掩码不能是空");
				ip.focus();
				return false;
			}
			if(!package.maskCheck(v[1])) {
				flag = false;
				package.message("掩码 请输入0~32之间的整数");
				ip.focus();
				return false;
			}
			if( !package.metricCheck(metric.val())) {
				flag = false;
				package.message("metric 请输入0~65535之间的整数");
				metric.focus();
				return false;
			}
						
			/* if( !package.describeCheck(describe.val())) {
				flag = false;
				package.message("描述信息 请输入英文字母、数字或下划线");
				describe.focus();
				return false;
			} */
			
			if ($("#gatesTable tr").length < 2) {
				flag = false;
				package.message("配置静态路由 请至少添加一条路由网关");
				inter.focus();
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
		metricCheck : function (val) {
			if($.trim(val) == "") return true;
			return (/^(\d+)$/.test(val)) && ((RegExp.$1 < 65536))&&((RegExp.$1 >= 0));
		},
		describeCheck : function (val) {
			if($.trim(val) == "") return true;
			return /^[\w]+$/.test(val);
		},
		weightCheck : function (val) {
			if($.trim(val) == "") return true;
			return (/^(\d+)$/.test(val)) && ((RegExp.$1 < 257))&&((RegExp.$1 > 0));
		}
	});
})(route.static.config);
