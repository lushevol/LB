/**
 * @fileOverview 麒麟天平负载均衡系统
 * 用户部分
 * @author kylin
 */
if (!window.kylin) {
	window.kylin = {};
}
if (!window.kylin.klb) {
	window.kylin.klb = {};
}
if (!window.kylin.klb.user) {
	window.kylin.klb.user = {};
}

$(document).ready(function() {
	/*$("#loginForm").submit(
		function(){
			if(kylin.klb.user.check()) {
				kylin.klb.user.fclogin();
				return false;
			} else {
				return false;
			}
		}
	)*/
});

(function(package){
	jQuery.extend(package, {
		/*user*/
		fclogin : function() {
			$.ajax({
				type: "POST",
				url: "login.action",
				data: {"user.loginName":$("#loginName").val(),"user.password":$("#password").val()},
				dataType : 'json',
				timeout : 10000,
				success: function(json){
					if(typeof json != "undefined") {
						if(json.auth == true) {
							if(json.error == 1) {
								window.location.replace("klb-login.action?error=1");
							} 
							if(json.error == 2) {
								window.location.replace("klb-login.action?error=2");
							} 
							else {
								window.location.replace("klb-index.action");
							}
						} else {
							
						}
					}
				},
				error : function(){
					alert(提交失败);
				}
			});
		},
		check : function() {
			var un = $("#loginName").val();
			// var pw = $("#password").val();
			if(jQuery.trim(un)=="") {
				$("#errorsId").html("请输入用户名");
				$("#loginName").focus();
				return false;
			}
			/* if(jQuery.trim(pw)=="") {
				$("#errorsId").html("请输入密码");
				$("#password").focus();
				return false;
			} */
			return true;
		},
		//分页跳转
		jumpPage : function (pageNo) {
			$("#pageNo").val(pageNo);
			$("#mainForm").submit();
		},
		del : function (id) {
			if(typeof id != "undefined" && id != "") {
				if(confirm('确定删除吗？删除操作不可恢复！')) {
					location.href="klb-user!delete.action?id="+id;
				}
			} else {
				alert("不能删除当前用户");
			}
			return;
		},
		jumpFrame : function (src) {
			if(typeof src != "undefined" && src != "") {
				$("#mainMidFrameId")[0].src=src;
			}
		},
		configCommit : function(obj) {
			if(confirm("确实要配置保存生效吗?")) {
				package.loadingshow();
				$.ajax({
					type: "POST",
	          		url: "configure!configure.action",
					dataType : 'json',
					timeout : 15000,
					success: function(obj){
						if(typeof obj.auth!="undefined"){
							package.loadinghide();
							alert(obj.msg);
	             		} else {
	             			package.loadinghide();
							alert(obj.msg);
	             		}
	           		},
					error : function(){
						package.loadinghide();
						alert("配置保存失败！");
					}
	        	});
			}
		},
		reloadCommit : function(obj) {
			if(confirm("确实要配置重载生效吗?")) {
				package.loadingshow();
				$.ajax({
					type: "POST",
	          		url: "configure!reload.action",
					dataType : 'json',
					timeout : 25000,
					success: function(obj){
						if(typeof obj.auth!="undefined"){
							package.loadinghide();
							alert(obj.msg);
	             		} else {
	             			package.loadinghide();
							alert(obj.msg);
	             		}
	           		},
					error : function(){
						package.loadinghide();
						alert("配置重载失败！");
					}
	        	});
			}
		},
		loadingshow : function (){
			var popDiv = $("#loading");
			if(!popDiv) return;
			
			var markLay = $("#mark")[0];
			if(typeof popDiv == "undefined" || typeof markLay == "undefined") {
				return;
			}
			markLay.style.display="block";
			popDiv.html("<img src=\"css/skin/images/common/loading3.gif\"/>");
			
			popDiv.show();
			popDiv.css({
				"position":"absolute",
				"top":"50%",
				"left":"50%",
				"margin-bottom":"0",
				"z-index":"9999"
			})
			return false;
		},
		loadinghide : function (){
			$('#mark').hide();
			$("#loading").hide();
			return false;
		}
	});
})(kylin.klb.user);
