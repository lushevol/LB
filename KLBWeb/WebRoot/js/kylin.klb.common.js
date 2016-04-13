/**********************************************************
声明：UE研究与设计中心所提供的所有js代码均为交互效果展示使用，不建议
在实际开发中使用。我们对任何使用在线上项目的js代码不承担责任，亦不
提供技术支持。
**********************************************************/

$(function(){

	var productName = $("#productName").val();
	var contextPath =  $("#contextPath").val();
	var loginImage = $("#loginImage").val();
	var logoImage = $("#logoImage").val();
	var welcome_system = $(".system");
	var log_region = $(".header .tt h1 a");
	
	if(loginImage != ""){
		var welcome_url= "" + contextPath + loginImage;
		welcome_system.css({
			"background":'url(' + welcome_url +') 0 40px no-repeat',
			"float":"left",
			"width":"430px",
			"height":"470px"
			
		})
	}
	
	if(logoImage != ""){
		var logo_url= "" + contextPath + logoImage;
		log_region.css({
			"background":'url(' + logo_url +') 0 0px no-repeat',
			"float":"left",
			"display":"block",
			"text-indent":"-9999px",
			"width":"250px",
			"height":"46px",
			"overflow":"hidden"
		})
	}
	
	//窗口高度
	doResize();
 	$(window).resize(function() {
		doResize();
	})
	
	//头部弹出层
	$(".setList").hide();
	$(".swich").click(function(){
		$(".setList").hide();
		$(this).next("ul.setList").toggle();
		return false;
	})
	$("ul.setList li a").click(function(){
		$(this).parents('.setList').hide();
	})

	//弹出层关闭按钮
	$("input[type=button],a,button").live("click", function(){
		if($(this).attr("hider")){
			var closeWath = $(this).attr("hider");
			$(closeWath).hide();
			$('#mark').hide();
		}
	})
	
	//弹出层添加框
	$('#mark').bgiframe();
	$('.popDiv').hide();
	
	/* 左侧 展开收缩 状态*/
	$('.navFirst h3.first').next('ul').show();
	$('.navFirst h3.first a').addClass('active');
	$('.navFirst h3').next().find('li:first').find('a').addClass('active');
	
	$('.navFirst h3 a').click(function(){
		$('.navFirst h3 a.active , ul.navSec li a.active').removeClass("active");
		$(this).addClass('active');		
		
		$('ul.navSec').slideUp('fast');
		$(this).parent().next('li ul.navSec').slideDown('fast');
		$(this).parent().next().find('li:first').find('a').addClass('active');
	})
	
	$('.navSec a').click(function(){
		$('.navSec a.active').removeClass('active');
		$(this).addClass('active');
	})

	//空白处关闭层	
    $(".popList").live("click",function(){$(".popList").hide();})
	$("html").live("click",function(){$(".popList").hide();}) 

	//模拟select下拉
	$('.selectSim .btnSelect').click(function(){
		$('.selectSimLayer').hide();
		$('.selectSimLayer').css({"display":"block"})
		$(this).next().css({"left":"-1px"}).show();
		var height = $(this).next().height();
		var width = $(this).next().width();
		//ie6下 遮挡select
		$(this).next().bgiframe({width: width, height: height});
		return false;
	})
	$('.selectSim .selectSimLayer li a').click(function(){
		$(this).parents('.selectSimLayer:visible').hide();
		//选择除删除外的文字
		var txt = $(this)[0].firstChild.nodeValue;
		$(this).parents('.selectSim').find('input').val(txt);
	})
	$('.selectSimLayer span.delete').click(function(){
		$(this).parents('li').remove();
	})
	
	$('html').click(function(){ $('ul.selectSimLayer:visible').hide(); })

	//按钮点击跳转(需要跳转的按钮请添加title属性并赋值为需跳转页面的URL)
	$(":button").click(function(){								
		if ($(this).attr("title")){
			window.location = $(this).attr("title");
		}
	})
	
	//表格
	$('.tableList tbody tr:last').addClass("last");
	
	//向导2页面端口切换
	$("input[type=radio][name=dk]").click(function(){
		$("input[type=radio]").removeAttr("checked").next("input").hide();
		$(this).attr("checked",true);
		$(this).next("input").show();
	})
	
	/**
	 * //路由配置 添加及删除
		$('.addImg').click(function(){
			var addTr = $(this).parents('tr').clone(true);
			$(this).parents('tr').after(addTr);
		})
		$('.delImg').click(function(){
			$(this).parents('tr').remove();
		})
	 */
	
	//虚拟服务设置
	$('input.enable').click(function(){
		 if(this.checked){
			$("td.disabled").addClass("enable");
			$("td.disabled input").removeAttr("disabled");
		}
	})
	$('input.disabled').click(function(){
		 if(this.checked){
			$("td.disabled").removeClass("enable");
			$("td.disabled input").attr({"disabled":true,"checked":false})
		}
	})
})
//添加、设置弹出层
function popbox(dom){
	var popDiv = $(dom);
	if(!popDiv) return;
	
	var markLay = $("#mark")[0];
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
}

//窗口高度
function doResize() {
	var totalheight = document.documentElement.clientHeight;
	var totalwidth = document.documentElement.clientWidth;

	//alert(totalheight +"|"+ totalwidth);
	if ($.browser.msie){
		$("iframe.all").css({height:totalheight-114,width:totalwidth-162}); 
		$(".sidePart").css({height:totalheight-110});
		
		$('.mainPart').css({height:$(".sidePart").height(),width:$("iframe.all").width()}); 
		var a =totalwidth-179;
		//ie6多出了18px,原本是210原因好像是ie6的滚动条多出的宽度。	
		$('iframe').contents().find("body").css("width",a);
	}
	else{
		$("iframe.all").css({height:totalheight-114,width:totalwidth-166}); 
		$(".sidePart").css({height:totalheight-110}); 
		$('iframe.all').attr("scrolling","auto");
	}
	
	//
	$('.headerQuide .center').height( totalheight - $('.headerQuide .header').height() - 30 );
	
}

// 解决 ie6 下 select 遮住 div 的方法
(function($){
$.fn.bgIframe = $.fn.bgiframe = function(s) {
	// This is only for IE6
	if ( $.browser.msie && /6.0/.test(navigator.userAgent) ) {
		s = $.extend({
			top     : 'auto', // auto == .currentStyle.borderTopWidth
			left    : 'auto', // auto == .currentStyle.borderLeftWidth
			width   : 'auto', // auto == offsetWidth
			height  : 'auto', // auto == offsetHeight
			opacity : true,
			src     : 'javascript:false;'
		}, s || {});
		var prop = function(n){return n&&n.constructor==Number?n+'px':n;},
		    html = '<iframe class="bgiframe" frameborder="0" tabindex="-1" src="'+s.src+'"'+
		               'style="display:block;position:absolute;z-index:-1;'+
			               (s.opacity !== false?'filter:Alpha(Opacity=\'0\');':'')+
					       'top:'+(s.top=='auto'?'expression(((parseInt(this.parentNode.currentStyle.borderTopWidth)||0)*-1)+\'px\')':prop(s.top))+';'+
					       'left:'+(s.left=='auto'?'expression(((parseInt(this.parentNode.currentStyle.borderLeftWidth)||0)*-1)+\'px\')':prop(s.left))+';'+
					       'width:'+(s.width=='auto'?'expression(this.parentNode.offsetWidth+\'px\')':prop(s.width))+';'+
					       'height:'+(s.height=='auto'?'expression(this.parentNode.offsetHeight+\'px\')':prop(s.height))+';'+
					'"/>';
		return this.each(function() {
			if ( $('> iframe.bgiframe', this).length == 0 )
				this.insertBefore( document.createElement(html), this.firstChild );
		});
	}
	return this;
};
})(jQuery);
