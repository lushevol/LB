/**
 * @fileOverview 麒麟天平链路负载均衡系统
 * 智能DNS-ISP地址段-导入配置
 * @author kylin
 */

if (!window.dns) {
	window.dns = {};
}
if (!window.dns.isp) {
	window.dns.isp = {};
}
if (!window.dns.isp.impt) {
	window.dns.isp.impt = {};
}

$(document).ready(function() {
	var uploadObj = null;
	
	var upload = new dns.isp.impt.Upload({
		fileDom:"#upload",
		fileFormDom:"#ispImportForm",
		errorDom:"#show",
		fileType:"txt,xml"
	});
	uploadObj = upload;
	/***/
	$("#import").bind("click", function(){
			if ( dns.isp.impt.checkSave() ) {
				upload.upSubmit();
			}
		}
	);
	function uploadCbk(obj){
		if(typeof obj != "undefined") {
			uploadObj.cancle();
			$("#show").html(obj.msg);
		}
	}
});

	/* $('#import').click(function(){
		return dns.isp.impt.checkSave();
	}); */		

(function(package){
	package.conf = {
			FILETYPE : "txt,xml",
			ERROR : "#show"
	};
	var Upload = kylin.object.createClass(null, {
		flag : false,
		fileDom : null,		
		fileFormDom : null,
		errorDom : null,
		fileType : null,
		//初始化
		init : function(arg0) {
			var _this = this;
			if(!arg0.fileDom) throw new Error("must designated parameter : fileDom");			
			if(!arg0.fileFormDom) throw new Error("must designated parameter : fileFormDom");
			if(!arg0.fileType) arg0.fileType = package.conf.FILETYPE;
			else this.fileType = arg0.fileType;
			if(arg0.errorDom) this.errorDom = arg0.errorDom;
			
			this.fileDom = arg0.fileDom;			
			this.fileFormDom = arg0.fileFormDom
			_this.initBind();
		},
		//重新初始化事件绑定
		initBind : function() {
			$(this.fileFormDom).unbind();
		    $(this.fileDom).unbind();
		    
			var _this = this;
			//表单提交时格式验证
			$(this.fileFormDom).submit(function(){
				return _this.filter();
			});
			//表单修改时格式验证
			$(this.fileDom).change(function(){
				_this.message("");
		      	return _this.filter();
		    }); 
		},
		//文件过滤
		filter : function() {
			var _this = this;
			var flag = false;
		   	var fileter = new Array();
		   	var f = this.fileType.split(",");
		   	for(var j=0;j<f.length;j++) {
		   		fileter.push(f[j]);
		   	}
		  	fileAddress = $(this.fileDom).val();
		  	if(fileAddress != null && fileAddress.length > 0) {
				for(var i = 0; i < fileter.length; i++) {
					var index = fileAddress.lastIndexOf(fileter[i]);
			  		if(index != -1) {
				   		if ((fileAddress.length - index - fileter[i].length) == 0) {
				   			flag = true;
				   			break;
				   		}
			   		}
			   	}
		   		if(!flag) {
		   			_this.message("文件格式不正确");
		   			_this.cancle();
		   			return false;
		   		} 
		   	}else{
		   		_this.message("请选择需要导入的文件");
		   		$("#upload").focus();
		   		_this.cancle();
		   		return false;
		   	}
			return flag;
		},
		//取消文件文本框中的内容
		cancle : function () {
			var _this = this;
			var fileInput = document.createElement("input");
			var dom = $(this.fileDom);
			fileInput.type="file";
			fileInput.size=40;
			dom.parent().append(fileInput);
			dom.remove();
			$(fileInput).attr("id",dom.attr("id"));
			$(fileInput).attr("name",dom.attr("id"));
			
			_this.initBind();
		},
		message : function(str) {
			if(typeof str != "undefined") {
				if(this.errorDom != null) {
					$(this.errorDom).html(str);
				} else {
					alert(str);
				}
			}
		},
		upSubmit : function() {
			$(this.fileFormDom).submit();
		}
	})

	jQuery.extend(package, {		
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
		Upload : Upload		
	});
})(dns.isp.impt);
