/**
 * @fileOverview 麒麟天平链路负载均衡系统
 * 接口配置-链路聚合
 * @author kylin
 */
 
if (!window.interface) {
	window.interface = {};
}
if (!window.interface.bond) {
	window.interface.bond = {};
}

//链路聚合配置
$(document).ready(function(){
	if(!($("#failedMess").val()===""||$("#failedMess").val()===null)){
		alert($("#failedMess").val());
		$("#failedMess").val("");
	}
	/* jQuery.validator.addMethod("isStr", function(value, element) {   
		return this.optional(element) || /^[\w]+$/.test(value);   
	}, "只能包括英文字母、数字和下划线"); */
	jQuery.validator.addMethod("isBond", function(value, element) {   
		return this.optional(element) || /^bond[\d]+$/.test(value);   
	}, "聚合接口名称的格式为bondX（X为数字）");
	
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
     		interface.bond.save();
     	},
		rules: {
			name: { required: true, isBond: true }
		},
		messages: {
			name:{ required: "聚合接口 不能为空", isBond:"聚合接口名称的格式为bondX（X为数字）！" }
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
		interface.bond.formenable();
		interface.bond.formreset();
		interface.bond.popbox();
	});
});

(function(package){
	jQuery.extend(package, {
		save : function () {
			$("#save_bond").attr("disabled",true);
			package.message("正在保存数据...");

			var jsonDate = {
				"bondName":$("#name").val()
			};
			
			$.ajax({
				type: "POST",
				url: "interface-bond!save.action",
				data: jsonDate,
				dataType : 'json',
				timeout : 10000,
				success: function(json){
					if(typeof json != "undefined") {
						$("#save_bond").attr("disabled",false);
						if(json.auth == true) {
							//alert(json.mess);
							location.reload();
						} else {
							package.message(json.mess);
							$("#save_bond").attr("disabled",false);
						}
					} else {
						package.message("保存数据失败");
						$("#save_bond").attr("disabled",false);
					}
				},
				error : function(){
					package.message("保存数据失败");
					$("#save_bond").attr("disabled",false);
				}
			})
		},
		del : function (obj,jsonp){
			if(confirm("确实要删除吗?")) {
				$("#message").html("正在删除...").hide().fadeIn();
				$(obj).parent().append("<a href=\"javascript:;\">正在删除...</a>");
				$(obj).html("");
				
				if(typeof jsonp != "undefined") {
					$.ajax({
						type: "POST",
						url: "interface-bond!del.action",
						data: { "bondName":$.trim(jsonp.bondName) },
						dataType : "json",
						timeout : 10000,
						success: function(json){
							if(typeof json != "undefined") {
								if(json.auth == true) {
									//alert("删除操作成功");
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
			var popDiv = $("#addBond");
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
		message : function(msg) {
			if($.trim(msg) == "") $("#show").html("");
			$("#show").html(msg).fadeIn();
		}				
	});
})(interface.bond);