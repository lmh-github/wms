/**
 * weetips.js
 *
 * @category   javascript
 * @package    jquery
 * @author     Jack <xiejinci@gmail.com>
 * @version    
 */
var weetips = function() {
	var self = this;
	$('<style id="weetipsstyle" type="text/css">'	+ 
			'#weetips{'	+ 
				'border:1px solid #FFBA43;' +
				'z-index:65535;'	+ 
				'position:absolute;' + 
				'background:#FDFFCE;' + 
				'color:#ff6600;' + 
				'padding:5px 10px;'	+
			'}'	+ 
			'#weetips-content{' +
				'text-align:left;'+
				'word-break:break-all;'+
			'}'+
		  '</style>').appendTo('body');
	$('<div id="weetips"><div id="weetips-content"></div><div style="both:clear"></div></div>').appendTo('body').hide();
	this.init = function() {		
		$(".weetips").each(function(){
			if ($(this).attr("title")) {
				$(this).attr("msg", $(this).attr("title")).removeAttr('title');
			}
			var msg = $(this).attr('msg');
			if ($.trim(msg) == '') {
				$(this).removeClass("weetips");
			}	
		});
		$(".weetips").mouseover(function(event) {
			var msg = $(this).attr('msg');
			if ($.trim(msg) == '') {return false;}
			$('#weetips-content').html(msg);
			var width = parseInt("0"+$(this).attr('bwidth'), 10);
			if (width) {
				$('#weetips-content').css("width", width).css("overflow", "hidden").css("white-space", "normal");
			} else {
				$('#weetips-content').css("width", "").css("overflow", "").css("white-space", "nowrap");
			}
			self.setPosition(event);
		}).mousemove(function(event){
			self.setPosition(event);
		}).mouseout(function() {
			$('#weetips').hide();
		});	
	}
	
	this.setPosition = function(event) {
		var left = self.getLeft(event);
		var top = self.getTop(event);			
		$('#weetips').css({left:left, top:top}).show();
	}
	
	this.getLeft = function(event) {
		var docWidth = document.documentElement.clientWidth || document.body.clientWidth;
		var docLeft = document.documentElement.scrollLeft|| document.body.scrollLeft;
		var docRight = docLeft + docWidth;
		var left = docLeft+event.clientX;
		if (left+$('#weetips').width()+38 >= docRight) {
			left = Math.min(left - 10 - $('#weetips').width(), docRight-$('#weetips').width()-38);
		} else {
			left += 10;
		}
		return left;
	}
	
	this.getTop = function(event) {			
		var docHeight = document.documentElement.clientHeight|| document.body.clientHeight;
		var docTop = document.documentElement.scrollTop|| document.body.scrollTop;
		var docBottom = docTop + docHeight;
		var top = docTop+event.clientY;
		if (top+$('#weetips').height()+10 >= docBottom) {
			top = top - 10 - $('#weetips').height();
		} else {
			top = top + 10;
		}
		return top;
	}
	this.init();
};
var $weetips = null;
$(document).ready(function(){
	$weetips = new weetips();
	$weetips.init();
});