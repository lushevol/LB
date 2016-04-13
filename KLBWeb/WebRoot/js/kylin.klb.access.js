/**
 * @fileOverview 麒麟天平负载均衡系统
 * 访问控制管理
 * @author kylin
 */
 
if (!window.kylin) {
	window.kylin = {};
}
if (!window.kylin.klb) {
	window.kylin.klb = {};
}
if (!window.kylin.klb.access) {
	window.kylin.klb.access = {};
}

//访问控制配置
$(document).ready(function(){
	jQuery.validator.addMethod("isAddr", function(value, element) {   
		return this.optional(element) || (/^(\d+)\.(\d+)\.(\d+)\.(\d+)\/?(\d+)?$/.test(value) && 
			(RegExp.$1 <256 && RegExp.$2<256 && RegExp.$3<256 && RegExp.$4<256) && (RegExp.$5 <= 32))&&((RegExp.$5 >= 0));   
	}, "请输入合法的IP信息");
	jQuery.validator.addMethod("firstip", function(value, element) {
		return this.optional(element) || (/^(\d+)\.(\d+)\.(\d+)\.(\d+)\/?(\d+)?$/.test(value) && 
			(RegExp.$1 > 0 && RegExp.$1 < 224));
	}, "请输入合法的IP首位信息");
		
	$.validator.setDefaults({
		invalidHandler: function(form, validator) {
	    	$.each(validator.invalid,function(key,value){
	            tmpkey = key;
	            tmpval = value;
	            validator.invalid = {};
	            validator.invalid[tmpkey] = value;
	            $("#show").html(value).hide().fadeIn();
	            $("#"+key).focus();
	            return false;
	    	});
		},
		errorPlacement:function(error, element) {},
	    onkeyup: false,
	    onfocusout:false,
	    focusInvalid: true
	});
	//为inputForm注册validate函数
	$("#inputForm").validate({
		submitHandler: function(form) {
     		kylin.klb.access.savess();
     		//form.submit();
     	},
		rules: {
			srcNet: { isAddr: true, firstip: true },
			destNet: { isAddr: true, firstip: true }
		},
		messages: {
			srcNet:{ isAddr:"请输入合法的源地址(/掩码)", firstip:"请输入合法的源地址IP首位（1~223）" },
			destNet:{ isAddr:"请输入合法的目的地址(/掩码)", firstip:"请输入合法的目的地址IP首位（1~223）" }
		}
	});
		
	//弹出层关闭按钮
	$("input[type=button],a,button").live("click", function(){
		if($(this).attr("hider")){
			var closeWath = $(this).attr("hider");
			$(closeWath).hide();
			$('#mark').hide();
		}
	});
	
	//添加弹出层
	$("#add").click(function(){
		$("#update").val("0");
		//$("#oldIp").val("");
		//初始化协议隐藏效果
		var protocols = $("#protocols").val();
		if ( protocols == "other" ){		
			$(".autoHide").show();
		}
		else{
			$(".autoHide").hide();
		}
		//初始化端口设置效果
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
		kylin.klb.access.formenable();
		kylin.klb.access.formreset();
		kylin.klb.access.popbox();		
	});
	
	/* var dealProtocol = function(){
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
		}
		else{
			$("#srcPort").val("");
			$("#destPort").val("");			
			$(".portGray").addClass("gray9");
			$("#srcPort").attr("disabled", true);
			$("#destPort").attr("disabled", true);			
		}
	};		
 	dealPort();	 */
 	
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
	
	$('#inputForm').submit(function(){
		return kylin.klb.access.checkRule();
	});
		
});

(function(package){
	jQuery.extend(package, {
		//设置弹出层
		setss : function (jsonp){
			$("#update").val("1");
						
			package.formdisabled();
			kylin.klb.access.popbox();
									
			package.message("正在加载数据....");
			if(typeof jsonp != "undefined") {
				$.ajax({
					type: "POST",
					url: "klb-network-access!input.action",
					data: {"id":$.trim(jsonp.id)},
					dataType : 'json',
					timeout : 10000,
					success: function(json){
						if(typeof json != "undefined") {
							if(json.auth == true) {
								var obj = json.obj;
								if(typeof obj != "undefined") {
									package.formenable();
									$("#srcNet").val(obj.srcNet);
									$("#destNet").val(obj.destNet);
									$("#protocol").val(obj.protocol);
									$("#protocols").val(obj.protocols);
									$("#srcPort").val(obj.srcPort);
									$("#destPort").val(obj.destPort);									
									$("#id").val(obj.id);
									package.message("");
									//初始化协议隐藏效果
									var protocols = $("#protocols").val();
									if ( protocols == "other" ){
										$(".autoHide").show();
									}
									else{
										$(".autoHide").hide();
									}
									//初始化端口设置效果
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
								}
							} else {
								package.message("加载数据时出现错误");
							}
						} else {
							package.message("加载数据时出现错误");
						}
					},
					error : function(e){
						package.message("加载数据时出现错误");
					}
				});	
			} else {
				package.message("加载数据时出现错误");
			}
		},
		savess : function () {			
			if( package.checkEmpty() ) {
				$("#save_scheduler").attr("disabled",true);
				package.message("正在保存数据...");
	
				var jsonDate = {
					"srcNet":$("#srcNet").val(),
					"destNet":$("#destNet").val(),
					"protocol":$("#protocol").val(),
					"srcPort":$("#srcPort").val(),
					"destPort":$("#destPort").val(),
					"update":$("#update").val(),
					"id":$("#id").val()
				};
				
				$.ajax({
					type: "POST",
					url: "klb-network-access!save.action",
					data: jsonDate,
					dataType : 'json',
					timeout : 15000,
					success: function(json){
						if(typeof json != "undefined") {
							$("#save_scheduler").attr("disabled",false);
							if(json.auth == true) {								
								location.reload();
							} else {
								package.message(json.mess);
								$("#save_scheduler").attr("disabled",false);
							}
						} else {
							package.message("保存数据失败");
							$("#save_scheduler").attr("disabled",false);
						}
					},
					error : function(){
						package.message("保存数据超时");
						$("#save_scheduler").attr("disabled",false);
					}
				})
			}
		},
		delss : function (obj,jsonp){
			if(confirm("确实要删除吗?")) {
				$("#message").html("正在删除...").hide().fadeIn();
				$(obj).parent().append("<a href=\"javascript:;\">正在删除...</a>");
				$(obj).html("");
				
				if(typeof jsonp != "undefined") {
					$.ajax({
						type: "POST",
						url: "klb-network-access!delete.action",
						data: {
							"id":$.trim(jsonp.id)
						},
						dataType : "json",
						timeout : 10000,
						success: function(json){
							if(typeof json != "undefined") {
								if(json.auth == true) {									
									location.reload();
								} else {
									$(obj).html("删除");
									$(obj).next().remove();
									$("#message").html("删除操作失败").hide().fadeIn().fadeOut(5000);
								}
							}
						}, error : function(){
							$(obj).html("删除");
							$(obj).next().remove();
							$("#message").html("删除操作失败").hide().fadeIn().fadeOut(5000);
						}
					});
				} else {
					$(obj).html("删除");
					$(obj).next().remove();
					$("#message").html("删除操作失败").hide().fadeIn().fadeOut(5000);
				}
				
			}
		},
		formreset : function () {
			$("#inputForm")[0].reset();
			package.message("");
		},
		formenable : function() {
			$("input").attr("disabled",false);
			$("button").attr("disabled",false);
			$("select").attr("disabled",false);
		},
		formdisabled : function() {
			$("input").attr("disabled",true);
			$("button").attr("disabled",true);
			$("select").attr("disabled",true);
		},
		popbox : function (){
			var popDiv = $("#addScheduler");
			if(!popDiv) return;
			
			var markLay = $("#mark")[0];
			markLay.style.display="block";
			if(typeof popDiv == "undefined" || typeof markLay == "undefined") {
				return;
			}
			markLay.style.display="block";
			popDiv.show();
			popDiv.css({
				"position":"absolute",
				"top":"50%",
				"left":"50%",
				"margin-left": -popDiv.width()/2,
				"margin-top": -popDiv.height()/2,
				"margin-bottom":"0",
				"z-index":"9999"
			})
			return false;
		},
		checkEmpty : function () {
			var srcNet = $("#srcNet");
			var destNet = $("#destNet");
			var protocol = $("#protocol");
			if( srcNet.val()=="" && destNet.val()=="" && protocol.val()=="" ) {
				return confirm("保存空规则将屏蔽所有网络 确定要屏蔽所有网络吗？");
			} else {
				return true;
			}			
		},
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
				package.message("源地址、目的地址和协议全为空 将屏蔽所有网络");
				return false;
			}
			
			var v = srcNet.val().split("/");
			/* if( srcNet.val()!="" && v.length!=2 ) {
				flag = false;
				package.message("源地址/掩码 格式不正确");
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
				package.message("目的地址/掩码 格式不正确");
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
		portCheck : function (val) {
			if($.trim(val) == "") return true;
			return (/^(\d+)$/.test(val)) && ((RegExp.$1 < 65536))&&((RegExp.$1 >= 0));
		}
	});
})(kylin.klb.access);
