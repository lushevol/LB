/**
 * @fileOverview 麒麟天平负载均衡系统
 * 文件上传
 * @author kylin
 */
if (!window.kylin) {
	window.kylin = {};
}
if (!window.kylin.klb) {
	window.kylin.klb = {};
}
if (!window.kylin.klb.upload) {
	window.kylin.klb.upload = {};
}

$(document).ready(function() {
	
});

(function(package){
	package.conf = {
			FILETYPE : "jpeg,jpg,xml,txt,gif,png",
			ERROR : "#error"
	};
	var Upload = kylin.object.createClass(null, {
		flag : false,
		fileDom : null,
		iframeDom : null,
		fileFormDom : null,
		errorDom : null,
		fileType : null,
		//初始化
		init : function(arg0) {
			var _this = this;
			if(!arg0.fileDom) throw new Error("must designated parameter : fileDom");
			if(!arg0.iframeDom) throw new Error("must designated parameter : iframeDom");
			if(!arg0.fileFormDom) throw new Error("must designated parameter : fileFormDom");
			if(!arg0.fileType) arg0.fileType = package.conf.FILETYPE;
			else this.fileType = arg0.fileType;
			if(arg0.errorDom) this.errorDom = arg0.errorDom;
			
			this.fileDom = arg0.fileDom;
			this.iframeDom = arg0.iframeDom;
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
		   		_this.message("请选择需要上传的文件");
		   		_this.cancle();
		   		return false;
		   	}
			return flag;
		},
		//取消文件文本框中的内容
		cancle:function () {
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
		Upload : Upload
	});
})(kylin.klb.upload);
