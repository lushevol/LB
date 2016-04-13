/**
 * @fileOverview 麒麟天平链路负载均衡系统
 * 接口配置-链路聚合-属性修改
 * @author kylin
 */

if (!window.interface) {
	window.interface = {};
}
if (!window.interface.bond) {
	window.interface.bond = {};
}
if (!window.interface.bond.attribute) {
	window.interface.bond.attribute = {};
}

$(document).ready(function(){
	var advan = 0;
	dealAdvance();
	initIps();
	$("#advanced").click(function() {
		if($("#advanced").attr("checked")){
			advan = 1;
		} else {
			advan = 0;
		}
		dealAdvance();
	});
	function initIps(){
		var routeTable = document.getElementById('routeTable');
		var ips = $("#ips").val();
		var dhcp = $("#dhcp").val();
		ipArray = ips.split(";");
		if(ipArray == "") {
			return;
		}
		for( var temp = 0; temp < ipArray.length; temp += 1 ) {
			newRow = routeTable.insertRow(temp+1);
			newCell0 = newRow.insertCell(0);
			newCell0.innerHTML = ipArray[temp];
			newCell1 = newRow.insertCell(1);
			if ( dhcp == "false" ) {
				newCell1.innerHTML = "<a href=\"javascript:;\" onclick=\"interface.bond.attribute.delIp(" + (temp+1) + ")\">删除</a>";
			}			
		}
	}
	function dealAdvance() {
		var advance = document.getElementById('advanceAttr');
		if (advan==0) {
			advance.style.display = "none";
		} else {
			advance.style.display = "";
		}
	}
	
	$('#routeModeForm').submit(function() {
		return interface.bond.attribute.checkSave();
	});
	
	var dealDhcp = function() {
		var dhcp = $("#dhcp").val();
		if ( dhcp == "false" ) {
			$(".dhcpGray").removeClass("gray9");
			$("#ip").attr("disabled", false);
			$("#add").attr("disabled", false);			
		} else {
			$("#ip").val("");
			$(".dhcpGray").addClass("gray9");
			$("#ip").attr("disabled", true);
			$("#add").attr("disabled", true);
		}
	};
	dealDhcp();
	
	$("#dhcp").change( function() {
 		var dhcp = $("#dhcp").val();
		if ( dhcp == "false" ) {
			$(".dhcpGray").removeClass("gray9");
			$("#ip").attr("disabled", false);
			$("#add").attr("disabled", false);
			
			var routeTable = document.getElementById('routeTable');
			rowLen = routeTable.rows.length - 1;			
			for(var delRow = rowLen; delRow > 0; delRow -= 1) {
				routeTable.deleteRow(delRow);
			}
			$("#ips").val("");
						
		} else {
			$("#ip").val("");			
			$(".dhcpGray").addClass("gray9");
			$("#ip").attr("disabled", true);
			$("#add").attr("disabled", true);
		}
	});
	
});

(function(package){
	jQuery.extend(package, {
		
		addIp : function () {
			var ip = $("#ip").val();
			var routeTable = document.getElementById('routeTable');
			if ( interface.bond.attribute.check() ) {
				rowLen = routeTable.rows.length;
				newRow = routeTable.insertRow(rowLen);
				newCell0 = newRow.insertCell(0);
				newCell0.innerHTML = ip;
				newCell1 = newRow.insertCell(1);
				newCell1.innerHTML = "<a href=\"javascript:;\" onclick=\"interface.bond.attribute.delIp(" + rowLen + ")\">删除</a>";
				var ips = $("#ips").val();
				if ( ips != "" ) {
					ip = ";" + $("#ip").val();
				} else {
					ip = $("#ip").val();
				}
				ips = ips + ip;
				$("#ips").val(ips);
				$("#ip").val("");
			}		
		},
		
		delIp : function (rowIndex){
			if(confirm("确实要删除吗?")) {
				var routeTable = document.getElementById('routeTable');
				var ip = routeTable.rows[rowIndex].cells[0].innerHTML;				
				routeTable.deleteRow(rowIndex);
				rowLen = routeTable.rows.length;
				
				for(var temp=rowIndex; temp<rowLen; temp+=1) {
					newRow = routeTable.rows[temp];
					newCell1 = newRow.cells[1];
					newCell1.innerHTML = "<a href=\"javascript:;\" onclick=\"interface.bond.attribute.delIp(" + temp + ")\">删除</a>";
				}
				var ips = $("#ips").val();
				ipArray = ips.split(";");
				var ipsNew = "";
				for(var i=0; i<ipArray.length-1; i+=1) {
					if(ipArray[i] != ip && ipArray[i] != "") {
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
			if(!package.ipFirst(v[0])) {
				flag = false;
				package.message("IP地址首位 请输入1~223之间的整数");
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
			return flag;
		},
		checkSave : function () {			
			var describe = $("#description");
			var mtu = $("#mtu");
			var mac = $("#mac");
			var flag = true;
									
			/* if( !package.describeCheck(describe.val())) {
				flag = false;
				package.message("描述 请输入英文字母、数字或下划线");
				describe.focus();
				return false;
			} */
			if( !package.mtuCheck(mtu.val())) {
				flag = false;
				package.message("MTU值 输入不合法");
				mtu.focus();
				return false;
			}
			if( !package.macCheck(mac.val())) {
				flag = false;
				package.message("MAC地址 输入不合法");
				mac.focus();
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
		describeCheck : function (val) {
			if($.trim(val) == "") return true;
			return /^[\w]+$/.test(val);
		},
		mtuCheck : function (val) {
			if($.trim(val) == "") return true;
			return (/^(\d+)$/.test(val)) && ((RegExp.$1 <= 1500))&&((RegExp.$1 >= 68));
		},
		macCheck : function (val) {
			if($.trim(val) == "") return true;
			return /^([0-9a-fA-F]{2})((:[0-9a-fA-F]{2}){5})$/.test(val);
		}
	});
})(interface.bond.attribute);
