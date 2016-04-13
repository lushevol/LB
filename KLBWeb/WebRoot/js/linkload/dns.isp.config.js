/**
 * @fileOverview 麒麟天平链路负载均衡系统
 * 智能DNS-ISP地址段-配置
 * @author kylin
 */

if (!window.dns) {
	window.dns = {};
}
if (!window.dns.isp) {
	window.dns.isp = {};
}
if (!window.dns.isp.config) {
	window.dns.isp.config = {};
}

$(document).ready(function(){
	
	initIps();
	
	function initIps(){
		var routeTable = document.getElementById('routeTable');
		var ips = $("#ips").val();
		ipArray = ips.split(";");
		if(ipArray == ""){
			return;
		}
		for(var temp=0; temp<ipArray.length; temp+=1){			
			newRow = routeTable.insertRow(temp+1);
			newCell0 = newRow.insertCell(0);
			newCell0.innerHTML = ipArray[temp];
			newCell1 = newRow.insertCell(1);
			newCell1.innerHTML = "<a href=\"javascript:;\" onclick=\"dns.isp.config.delIp(" + (temp+1) + ")\">删除</a>";
		}		
	}
	
	$('#ispConfigForm').submit(function(){
		return dns.isp.config.checkSave();
	});
		
});

(function(package){
	jQuery.extend(package, {
		
		addIp : function () {
			var ip = $("#ip").val();			
			var routeTable = document.getElementById('routeTable');
			if ( dns.isp.config.check() ) {
				rowLen = routeTable.rows.length;
				newRow = routeTable.insertRow(rowLen);
				newCell0 = newRow.insertCell(0);
				newCell0.innerHTML = ip;				
				newCell1 = newRow.insertCell(1);
				newCell1.innerHTML = "<a href=\"javascript:;\" onclick=\"dns.isp.config.delIp(" + rowLen + ")\">删除</a>";
				var ips = $("#ips").val();
				if ( ips != "" ) {
					ip = ";" + $("#ip").val();
				} else {
					ip = $("#ip").val();
				}
				ips = ips + ip;
				$("#ips").val(ips);
				//alert($("#ips").val());
				$("#ip").val("");				
			}
		},
		
		delIp : function (rowIndex){
			if(confirm("确实要删除吗?")) {
				var routeTable = document.getElementById('routeTable');
				var ip = routeTable.rows[rowIndex].cells[0].innerHTML;				
				routeTable.deleteRow(rowIndex);
				rowLen = routeTable.rows.length;
				
				for(var temp=rowIndex; temp<rowLen; temp+=1){
					newRow = routeTable.rows[temp];
					newCell1 = newRow.cells[1];
					newCell1.innerHTML = "<a href=\"javascript:;\" onclick=\"dns.isp.config.delIp(" + temp + ")\">删除</a>";
				}
				var ips = $("#ips").val();
				ipArray = ips.split(";");
				var ipsNew = "";
				for(var i=0; i<ipArray.length; i+=1){
					if(ipArray[i] != ip && ipArray[i] != ""){
						ipsNew = ipsNew + ipArray[i] + ";";
					}
				}
				$("#ips").val(ipsNew);
				//alert($("#ips").val());
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
			
			var v = ip.val().split("/");			
			if( ip.val()!="" && v.length==2 && $.trim(v[0]) == "") {
				flag = false;
				package.message("“/”前的IP地址不能是空");
				ip.focus();
				return false;
			}
			if(!package.ipCheck(v[0])) {
				flag = false;
				package.message("添加地址 IP不合法");
				ip.focus();
				return false;
			}
			if( ip.val()!="" && v.length==2 && $.trim(v[1]) == "") {
				flag = false;
				package.message("“/”后的掩码不能是空");
				ip.focus();
				return false;
			}
			if(v.length==2 &&!package.maskCheck(v[1])) {
				flag = false;
				package.message("掩码 请输入0~32之间的整数");
				ip.focus();
				return false;
			}
			return flag;
		},
		checkSave : function () {
			var name = $("#name");			
			var flag = true;
			if( $.trim(name.val())=="" ) {
				flag = false;
				package.message("运营商名称不能是空");
				name.focus();
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
			return (/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/.test(val) && (RegExp.$1>0 && RegExp.$1 <224 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256));
		},
		maskCheck : function (val) {
			if($.trim(val) == "") return false;
			return (/^(\d+)$/.test(val)) && ((RegExp.$1 <= 32))&&((RegExp.$1 >= 0));
		}
	});
})(dns.isp.config);
