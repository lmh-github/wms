// JavaScript Document
jQuery(function($){
 $(".order_box").eq(0).css({"z-index": 9});
	
	 $(".tit_order ul li").click(function(){
		var index1 = $(".tit_order ul li").index(this);	
		$(".tit_order ul li").eq(index1).addClass("accu").siblings("li").removeClass("accu");
		$(".order_box").eq(index1).show().siblings(".order_box").hide();
	 });
	 

$(".mbox a.zd").toggle(function(){
						var index = $(".mbox a.zd").index(this);
						$(".mbox .databox").eq(index).hide();
					},function(){
						var index = $(".mbox a.zd").index(this);
						$(".mbox .databox").eq(index).show();
					});
	 
	 
	 	 
});			
				
