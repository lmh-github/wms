/*
*Jquery 简单的图片弹出插件
*需要jquery1.4或者以上版本支持
*By 甘强 2011/04/05
*1.0版
*插件官方地址：http://www.skygq.com/2011/04/05/jquery-popimage/
*/
;(function($){
//	计算popImage的根路径
	var root;
	$('script').each(function(a, tag) {
		miuScript = $(tag).get(0).src.match(/(.*)jquery\.popImage(\.mini)?\.js$/);
		if (miuScript !== null) {
			root = miuScript[1];
		}
	});
//	加载css样式
	var css_href = root+'css/popImage.css';
	var styleTag = document.createElement("link");
	styleTag.setAttribute('type', 'text/css');
	styleTag.setAttribute('rel', 'stylesheet');
	styleTag.setAttribute('href', css_href);
	$("head")[0].appendChild(styleTag);
	$.fn.popImage = function(options){
		var s = $.extend({}, $.fn.popImage.defaultSettings, options || {});
		if ($("#popImage_cache").length == 0){
			$("<div id='popImage_cache'></div><div class='popImage_close'></div>").appendTo("body");
		}
		var item_num = $("#popImage_cache img").length;
		return this.each(function(index){
			var $$ = $(this),
			iw = $$.outerWidth(),
			ih = $$.outerHeight(),
			imgUrl = this[s.tagName],
			index = item_num + index,
			this_id = "slide" + index;

			$('<img src="'+imgUrl+'" class="popImage_cached '+this_id+'" title="click to close"/>').appendTo("#popImage_cache").hide();
			$$.click(function(e){
				var animate_image = $('#popImage_cache .'+this_id),
				w_w = $(window).width(),
				w_h = $(window).height(),
				st = $(window).scrollTop();
				$('.popImage_close').hide();
				e.preventDefault();
				position = $$.offset(),
				o_h = animate_image.height(),
				o_w = animate_image.width();

				var t = st + (w_h - o_h)/2,
				l = (w_w - o_w)/2;

				animate_image.css({'left':position.left, 'top':position.top,'height':ih,'width':iw});
				$('.popImage_cached').hide();
				animate_image.show().fadeTo(0,0.9);
				animate_image.animate({'left':l,'top':t,'height':o_h,'width':o_w,'opacity':1},s.timeOut,function(){
					var position2 = animate_image.offset();
					$('.popImage_close').css({'left':position2.left+o_w-6, 'top':position2.top-15}).show();
				});
			});
			$('.popImage_close,.popImage_cached').bind('click',function(a){
				$('.popImage_close').hide();
				$('.popImage_cached').fadeOut(400);
				a.preventDefault();
			});
		});
	};
	$.fn.popImage.defaultSettings = {
		"tagName":"href",
		"timeOut":"600"
	};
})(jQuery);