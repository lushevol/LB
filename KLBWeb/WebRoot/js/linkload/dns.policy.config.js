/**
 * @fileOverview 麒麟天平链路负载均衡系统
 * 智能DNS-DNS策略-配置
 * @author kylin
 */

if (!window.dns) {
	window.dns = {};
}
if (!window.dns.policy) {
	window.dns.policy = {};
}
if (!window.dns.policy.config) {
	window.dns.policy.config = {};
}

$(document).ready(function(){

	var dealRadio = function(){
		var returnAll = $("#echo").val();
		if(returnAll=="true"){
			$("#returnAll").attr("checked",true);			
		}
		else{
			$("#linkAll").attr("checked",true);			
		}
	};
	
	dealRadio();
	
	$("#returnAll").click(function() {		
		$("#echo").val("true");
	});
	
	$("#linkAll").click(function() {		
		$("#echo").val("false");
	});
 	
	dns.policy.config.dealAlise();
	dns.policy.config.dealServer();
	
	$('#policyConfigForm').submit(function(){
		return dns.policy.config.saveCheck();
	});
	
});

(function(package){
	jQuery.extend(package, {
		
		dealAlise : function () {
			var alises = $("#alises").val();
			var alisesTable = document.getElementById('aliseTable');
			aliseArray = alises.split(";");
			
			if(aliseArray == ""){
				return;
			}
			for(var temp=0; temp < aliseArray.length-1; temp += 1){
				newRow = alisesTable.insertRow(temp+1);
				newCell0 = newRow.insertCell(0);
				newCell0.innerHTML = aliseArray[temp];
				newCell1 = newRow.insertCell(1);
				newCell1.innerHTML = "<a href=\"javascript:;\" onclick=\"dns.policy.config.delAlise(" + (temp+1) + ")\">删除</a>";				
			}			
		},
		addAlise : function () {
			var alise = $("#alise").val();
			
			var aliseTable = document.getElementById('aliseTable');
			if ( dns.policy.config.checkAlise() ) {
				rowLen = aliseTable.rows.length;
				newRow = aliseTable.insertRow(rowLen);
				newCell0 = newRow.insertCell(0);
				newCell0.innerHTML = alise;
				newCell1 = newRow.insertCell(1);
				newCell1.innerHTML = "<a href=\"javascript:;\" onclick=\"dns.policy.config.delAlise(" + rowLen + ")\">删除</a>";
				
				var alises = $("#alises").val();
				/* if ( alises != "" ) {
					alise = ";" + $("#alise").val();
				} else {
					alise = $("#alise").val();
				} */
				alise = $("#alise").val() + ";";
				alises = alises + alise;
				$("#alises").val(alises);
				//alert($("#alises").val());
				$("#alise").val("");				
			}			
		},
		
		delAlise : function (rowIndex){
			if(confirm("确实要删除吗?")) {
				var aliseTable = document.getElementById('aliseTable');
				var alise = aliseTable.rows[rowIndex].cells[0].innerHTML;
				aliseTable.deleteRow(rowIndex);
				rowLen = aliseTable.rows.length;
				
				for(var temp = rowIndex; temp < rowLen; temp += 1){
					newRow = aliseTable.rows[temp];
					newCell1 = newRow.cells[1];
					newCell1.innerHTML = "<a href=\"javascript:;\" onclick=\"dns.policy.config.delAlise(" + temp + ")\">删除</a>";
				}
				var alises = $("#alises").val();
				aliseArray = alises.split(";");
				var alisesNew = "";
				for(var i=0; i<aliseArray.length; i+=1){
					if(aliseArray[i] != alise && aliseArray[i] != ""){
						alisesNew = alisesNew + aliseArray[i] + ";";
					}
				}
				$("#alises").val(alisesNew);
				//alert($("#alises").val());
			}
		},
		
		dealServer : function () {
			var displayServers = $("#displayServers").val();
			var serverTable = document.getElementById('serverTable');
			serverArray = displayServers.split(";");
			for(var temp=0; temp < serverArray.length-1; temp += 1){
				newRow = serverTable.insertRow(temp+1);
				niArray = serverArray[temp].split(",");
				for(var i = 0; i < niArray.length; i += 1){
					newCell = newRow.insertCell(i);
					newCell.innerHTML = niArray[i];
				}
				newCell2 = newRow.insertCell(2);
				newCell2.innerHTML = "<a href=\"javascript:;\" onclick=\"dns.policy.config.delServer(" + (temp+1) + ")\">删除</a>";
			}
		},
		
		delServer : function (rowIndex){
			if(confirm("确实要删除吗?")) {
				var serverTable = document.getElementById('serverTable');
				var ispName = serverTable.rows[rowIndex].cells[0].innerHTML;
				var ip = serverTable.rows[rowIndex].cells[1].innerHTML;
				
				serverTable.deleteRow(rowIndex);
				rowLen = serverTable.rows.length;
				
				for(var temp = rowIndex; temp < rowLen; temp += 1){
					newRow = serverTable.rows[temp];
					newCell = newRow.cells[2];
					newCell.innerHTML = "<a href=\"javascript:;\" onclick=\"dns.policy.config.delServer(" + temp + ")\">删除</a>";
				}
				var servers = $("#servers").val();
				niArray = servers.split(";");
				var nisNew = "";
				for(var i=0; i<niArray.length-1; i+=1){
					if((rowIndex-1) != i){
						nisNew = nisNew + niArray[i] + ";";
					}
				}
				$("#servers").val(nisNew);
			}
		},
		addServer : function () {			
			var ispName = $("#ispName").val();										
			var ip = $("#ip").val();
			var servers = $("#servers").val();
			
			var serverTable = document.getElementById('serverTable');
			if ( dns.policy.config.checkServer() ) {
				rowLen = serverTable.rows.length;
				newRow = serverTable.insertRow(rowLen);
				newCell0 = newRow.insertCell(0);
				if (ispName == "") {
					newCell0.innerHTML = "其他";
				} else {
					newCell0.innerHTML = ispName;
				}
				newCell1 = newRow.insertCell(1);
				newCell1.innerHTML = ip;
				newCell2 = newRow.insertCell(2);
				newCell2.innerHTML = "<a href=\"javascript:;\" onclick=\"dns.policy.config.delServer(" + rowLen + ")\">删除</a>";
								
				servers = servers + ispName + "," + ip + ";";
												
				$("#servers").val(servers);
				$("#ispName").val("");
				$("#ip").val("");				
			}
		},
												
		checkAlise : function () {
			var alise = $("#alise");
			var flag = true;
									
			if( $.trim(alise.val())=="" ) {
				flag = false;
				package.message("添加的别名不能是空");
				alise.focus();
				return false;
			}			
			/* if(!package.aliseCheck(alise.val())) {
				flag = false;
				package.message("别名不合法");
				alise.focus();
				return false;
			} */
			
			var alises = $("#alises").val();
			alisesArray = alises.split(";");
			for(var i=0; i<alisesArray.length; i+=1){
				if( alisesArray[i] == alise.val() ){
					flag = false;
					package.message("该别名已被使用！");
					alise.focus();
					return false;
				}
			}
			
			var allAlias = $("#allAlias").val();
			allAliasArray = allAlias.split(";");
			for(var i=0; i<allAliasArray.length; i+=1){
				if( allAliasArray[i] == alise.val() ){
					flag = false;
					package.message("该别名已被其他DNS策略使用！");
					alise.focus();
					return false;
				}
			}
			return flag;
		},
		
		checkServer : function () {
			var ip = $("#ip");			
			var flag = true;
			
			if( !package.ipCheck(ip.val())) {
				flag = false;
				package.message("IP不合法");
				ip.focus();
				return false;
			}			
			return flag;
		},
		
		saveCheck : function () {			
			var name = $("#name");
			var ttl = $("#ttl");
			
			if( name.val()== "" ) {
				package.message("域名不能为空！");
				name.focus();
				return false;
			}
			if( !package.ttlCheck(ttl.val())) {
				flag = false;
				package.message("生存时间 请输入正整数");
				ttl.focus();
				return false;
			}
			return true;
		},
		message : function(msg) {
			if($.trim(msg) == "") $("#show").html("");
			$("#show").html(msg).fadeIn();
		},
		ipCheck : function (val) {
			if($.trim(val) == "") return true;
			return (/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/.test(val) && (RegExp.$1 <256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256));
		},
		aliseCheck : function (val) {
			if($.trim(val) == "") return true;
			return /^[\w]+$/.test(val);
		},
		ttlCheck : function (val) {
			if($.trim(val) == "") return true;
			return (/^(\d+)$/.test(val)) && (RegExp.$1 > 0);
		}		
	});
})(dns.policy.config);
