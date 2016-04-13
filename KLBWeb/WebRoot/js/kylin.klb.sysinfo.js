/**
 * @fileOverview 麒麟天平负载均衡系统
 * 系统信息部分
 * @author kylin
 */
if (!window.kylin) {
	window.kylin = {};
}
if (!window.kylin.klb) {
	window.kylin.klb = {};
}
if (!window.kylin.klb.sysinfo) {
	window.kylin.klb.sysinfo = {};
}

(function(package){
	jQuery.extend(package, {
		process : function(dom,count) {
			if(!$(dom)) return;
			if(typeof count == "undefined") count = 0;
			var i =1.7*count/100;
			var c = 0;
			
			$(dom+"_text").text(count);
			
			/**
			 * if(count < 1) {
					$(dom+"_text").text(count);
				} else {
					var t = setInterval(
					    function() {
							$(dom+"_text").text(c);
							c+=1;
					        if(c>count) {
								clearInterval(t);
					        }
					    },
					20);
				}
			 */
			if(count>1) {
				var t1 = setInterval(
				//var t1 = setTimeout(
				    function() {
				        $(dom).css("width",i+"px");
				        i+=2;
				        if(i>=170*count/100) {
							clearInterval(t1);
				        }
				    },
				20)
			}
		},
		
	});
})(kylin.klb.sysinfo);
