/**
 * @fileOverview 麒麟天平链路负载均衡系统
 * 接口配置-链路聚合-聚合配置
 * @author kylin
 */

if (!window.interface) {
	window.interface = {};
}
if (!window.interface.bond) {
	window.interface.bond = {};
}
if (!window.interface.bond.config) {
	window.interface.bond.config = {};
}

$(document).ready(function(){
	initIps();
	initSelect();
	initMonitor();
	dealMonitor();
	dealcd();
	
	$("#mii").click(function() {
		$("#mii").attr("checked",true);
		dealcd();
		$("#monitor").val("0");
		$("#ips").val("");
		delAllIps();
	});
	
	$("#arp").click(function() {
		$("#arp").attr("checked",true);
		dealcd();
		$("#monitor").val("1");
	});
	function delAllIps(){
		var arpTable = document.getElementById('arpTable');
		rowLen = arpTable.rows.length;
		for(var temp=rowLen-1; temp>0; temp-=1){
			arpTable.deleteRow(temp);
		}
	}
	function initIps(){
		var arpTable = document.getElementById('arpTable');
		var ips = $("#ips").val();
		ipArray = ips.split(";");
		if(ipArray.length == 1){
			return;
		}
		for(var temp=0; temp<ipArray.length-1; temp+=1){
			newRow = arpTable.insertRow(temp+1);
			newCell0 = newRow.insertCell(0);
			newCell0.innerHTML = ipArray[temp];
			newCell1 = newRow.insertCell(1);
			newCell1.innerHTML = "<a href=\"javascript:;\" onclick=\"interface.bond.config.delIp(" + (temp+1) + ")\">删除</a>";
		}
	}
	function initSelect(){
		objSelect = document.getElementById('interToSelect');
		interToSelect = $("#interForBond").val();
		interArray = interToSelect.split(";");
		if(interArray.length == 1){
			return;
		}
		for(var i=0; i<interArray.length-1; i+=1){
			varItem = new Option(interArray[i], interArray[i]);
			objSelect.options.add(varItem);
		}
		
		objSelected = document.getElementById('interSelected');
		interSelected = $("#interBonded").val();
		interArray = interSelected.split(";");
		if(interArray.length == 1){
			return;
		}
		for(var i=0; i<interArray.length-1; i+=1){
			varItem = new Option(interArray[i], interArray[i]);
			objSelected.options.add(varItem);
		}
	}
	function dealcd(){
		if($("#mii").attr("checked")==true){			
			$("#ip").val("");
			$("#ips").val("");
			$("#ip").attr("disabled",true);
			$("#addIp").attr("disabled",true);
			$(".miiGray").addClass("gray9");			
		}
		else{
			$("#ip").attr("disabled",false);
			$("#addIp").attr("disabled",false);			
			$(".miiGray").removeClass("gray9");
		}
	}
	function initMonitor(){
		if ($("#monitor").val()==""){
			$("#monitor").val("0");
		}
	}
	function dealMonitor(){
		if ($("#monitor").val()=="0"){
			$("#mii").attr("checked",true);
		}
		else{
			$("#arp").attr("checked",true);
		}
	}
	
	$('#bondConfigForm').submit(function(){
		return interface.bond.config.checkSave();
	});
});

(function(package){
	jQuery.extend(package, {
		select : function () {
			var select = $("#interToSelect").val();
			if(select==null){
				return;
			}
			objSelected = document.getElementById('interSelected');
			for (var i = 0; i < objSelected.options.length; i+=1) {
				if (objSelected.options[i].value == select) {
					alert(select+"已经存在");
					return;
				}
			}
			varItem = new Option(select, select);
			objSelected.options.add(varItem);
			var interBonded = $("#interBonded").val();
			select = select + ";";
			interBonded = interBonded + select;
			$("#interBonded").val(interBonded);
		},
		unselect : function () {
			objSelect = document.getElementById('interSelected');
			var select = $("#interSelected").val();
			if(select==null){
				return;
			}
			var length = objSelect.options.length - 1;
			for(var i = length; i >= 0; i-=1){
				if(objSelect[i].selected == true){
					objSelect.options[i] = null;
				}
			}
			var interBonded = $("#interBonded").val();
			interArray = interBonded.split(";");
			var interNew = "";
			for(var i=0; i<interArray.length-1; i+=1){
				if(interArray[i] != select){
					interNew = interNew + interArray[i] + ";";
				}
			}
			$("#interBonded").val(interNew);			
		},
		addIp : function () {
			var ip = $("#ip").val();
			var arpTable = document.getElementById('arpTable');
			if ( interface.bond.config.check() ) {
				rowLen = arpTable.rows.length;
				newRow = arpTable.insertRow(rowLen);
				newCell0 = newRow.insertCell(0);
				newCell0.innerHTML = ip;
				newCell1 = newRow.insertCell(1);
				newCell1.innerHTML = "<a href=\"javascript:;\" onclick=\"interface.bond.config.delIp(" + rowLen + ")\">删除</a>";
				var ips = $("#ips").val();
				ip = ip + ";";
				ips = ips + ip;
				$("#ips").val(ips);
				$("#ip").val("");
			}
		},
		
		delIp : function (rowIndex){
			if(confirm("确实要删除吗?")) {
				var arpTable = document.getElementById('arpTable');
				var ip = arpTable.rows[rowIndex].cells[0].innerHTML;
				arpTable.deleteRow(rowIndex);
				rowLen = arpTable.rows.length;
				
				for(var temp=rowIndex; temp<rowLen; temp+=1){
					newRow = arpTable.rows[temp];
					newCell1 = newRow.cells[1];
					newCell1.innerHTML = "<a href=\"javascript:;\" onclick=\"interface.bond.config.delIp(" + temp + ")\">删除</a>";
				}
				var ips = $("#ips").val();
				ipArray = ips.split(";");
				var ipsNew = "";
				for(var i=0; i<ipArray.length-1; i+=1){
					if(ipArray[i] != ip){
						ipsNew = ipsNew + ipArray[i] + ";";
					}
				}
				$("#ips").val(ipsNew);
			}
		},
		
		check : function () {
			var ip = $("#ip");
			var flag = true;
			if($.trim(ip.val()) == "") {
				flag = false;
				package.message("添加地址不能是空");
				ip.focus();
				return false;
			}
			
			if(!package.ipCheck(ip.val())) {
				flag = false;
				package.message("添加地址 IP不合法");
				ip.focus();
				return false;
			}
			if(!package.ipFirst(ip.val())) {
				flag = false;
				package.message("IP地址首位 请输入1~223之间的整数");
				ip.focus();
				return false;
			}
			
			return flag;
		},
		checkSave : function () {						
			var interval = $("#interval");			
			var flag = true;
			if($.trim(interval.val()) == "") {
				flag = false;
				package.message("监控间隔不能是空");
				interval.focus();
				return false;
			}
			if( !package.intervalCheck(interval.val())) {
				flag = false;
				package.message("监控间隔 请输入正整数");
				interval.focus();
				return false;
			}			
			return flag;
		},
		message : function(msg) {
			if($.trim(msg) == "") $("#show").html("");
			$("#show").html(msg).fadeIn();
		},
		ipCheck : function (val) {
			if($.trim(val) == "") return false;
			return (/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/.test(val) && (RegExp.$1 <256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256));
		},
		ipFirst : function (val) {
			if($.trim(val) == "") return true;
			return (/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/.test(val) && (RegExp.$1>0 && RegExp.$1 <224));
		},
		intervalCheck : function (val) {
			if($.trim(val) == "") return true;
			return (/^(\d+)$/.test(val)) && (RegExp.$1 > 0);
		}
	});
})(interface.bond.config);
