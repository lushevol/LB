/**
 * @fileOverview 麒麟天平链路负载均衡系统
 * 路由配置-智能路由-添加
 * @author kylin
 */
 
 if (!window.route) {
	window.route = {};
}
if (!window.route.smart) {
	window.route.smart = {};
}
if (!window.route.smart.config) {
	window.route.smart.config = {};
}

$(document).ready(function(){
 	
	route.smart.config.dealGate();		
	
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
	
	$('#smartConfigForm').submit(function(){
		return route.smart.config.checkSave();
	});
	
	$('#mode').change(function(){
		return route.smart.config.modeChange();
	});
	var op = $("#operation").val();
	var mode = "0";
	if(op == "add"){
		$('#mode').val(0);
	}else{
		mode = $("#mode").val()+ "";
	}
	route.smart.config.changeViewByMode(mode);
	
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
				newCell3.innerHTML = "<a href=\"javascript:;\" onclick=\"route.smart.config.delGate(" + (temp+1) + ")\">删除</a>";
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
					newCell.innerHTML = "<a href=\"javascript:;\" onclick=\"route.smart.config.delGate(" + temp + ")\">删除</a>";
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
			if ( route.smart.config.check() ) {
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
				newCell3.innerHTML = "<a href=\"javascript:;\" onclick=\"route.smart.config.delGate(" + rowLen + ")\">删除</a>";
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
		
		check : function () {
			var gate = $("#gate");
			var inter = $("#interface");
			var weight = $("#weight");
			var flag = true;			
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
		//return true;		
			var describe = $("#describe").val();
			var mode =  $("#mode").val();
			var ip = $("#ip").val();
			var port = $("#port").val();
			var frequcency = $("#frequcency").val();
			var timeout = $("#timeout").val();
			
			var inter = $("#interface");	
			if ($("#gatesTable tr").length < 2) {
				package.message("配置智能路由 请至少添加一条路由网关");
				inter.focus();
				return false;				
			}		
			/*
			if(describe == null || describe.length <= 0){
				package.message("描述 不能为空");
				$("describe").focus();
				return false;	
			}
			
			if(describe != null || describe.length > 0){
				if(!package.describeCheck(describe)){
					package.message("描述 只能为数字、字母和下划线组成");
					$("describe").focus();
					return false;	
				}
			}
			if(mode == null || mode.length <= 0){			
				package.message("检查方式不能为空");
				$("mode").focus();
				return false;			
			}*/
			/*add by yjp 2012/03/25 检查方式为disbale 直接返回成功*/
			if(mode == "0" || mode == 0){
				return true;
			}
			if(ip == null || ip.length <= 0){			
				package.message("目标IP不能为空");
				$("ip").focus();
				return false;			
			}
			if(!package.ipCheck(ip)){
				package.message("目标IP不合法");
				$("ip").focus();
				return false;	
			}
			if(!package.ipFirst(ip)){
				package.message("目标IP地址首位 请输入1~223之间的整数");
				$("ip").focus();
				return false;	
			}
			/*add by yjp 2012/03/25 检查方式为ping 不需要端口*/
			if(mode != "1"){
				if(port == null || port.length <= 0){			
					package.message("目标端口不能为空");
					$("port").focus();
					return false;			
				}
			}
			if(!package.portCheck(port)){
				package.message("不是合法的端口地址");
				$("port").focus();
				return false;	
			}
			
			if(frequcency == null || frequcency.length <= 0){			
				package.message("检测频率不能为空");
				$("frequcency").focus();
				return false;			
			}
			if(!package.numValidate(frequcency)){
				package.message("检测频率为整数");
				$("frequcency").focus();
				return false;	
			}
			if(timeout == null || timeout.length <= 0){			
				package.message("超时时间不能为空");
				$("timeout").focus();
				return false;			
			}
			if(!package.numValidate(timeout)){
				package.message("超时时间为整数");
				$("timeout").focus();
				return false;	
			}
			
				
			return true;
		},
		message : function(msg) {
			if($.trim(msg) == "") $("#show").html("");
			$("#show").html(msg).fadeIn();
		},
		numValidate: function (value) {
			return (/^([0-9]\d*)$/.test(value));
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
			//if($.trim(val) == "") return true;
			return /^[\w]+$/.test(val);
		},
		portCheck : function (val) {
			if($.trim(val) == "") return true;
			return (/^(\d+)$/.test(val)) && ((RegExp.$1 < 65536))&&((RegExp.$1 > 0));
		},
		/*add by yjp 2012/03/26*/
		modeChange :  function(){

			var mode =  $("#mode").val();
			return package.changeViewByMode(mode);
		},
		changeViewByMode :  function(mode){
			if(mode == "3" || mode == "2"){	
				//$("#ip").val("");
				//$("#port").val("");
				//$("#frequcency").val("");
				//$("#timeout").val("");
				$("#ip").attr("disabled",false);
				$("#port").attr("disabled",false);
				$("#frequcency").attr("disabled",false);
				$("#timeout").attr("disabled",false);		
			}else if(mode == "1"){			
				//$("#ip").val("");
				//$("#port").val("");
				//$("#frequcency").val("");
				//$("#timeout").val("");
				$("#ip").attr("disabled",false);
				$("#port").attr("disabled",true);
				$("#frequcency").attr("disabled",false);
				$("#timeout").attr("disabled",false);
			}else {			
				//$("#ip").val("");
				//$("#port").val("");
				//$("#frequcency").val("");
				//$("#timeout").val("");
				$("#ip").attr("disabled",true);
				$("#port").attr("disabled",true);
				$("#frequcency").attr("disabled",true);
				$("#timeout").attr("disabled",true);
			}
		},
	});
})(route.smart.config);