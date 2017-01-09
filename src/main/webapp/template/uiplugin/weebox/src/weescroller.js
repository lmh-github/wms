var weescroller = function(idUl, options) {
    var self = this;  	
  	this.pausetimer = null;
  	this.steptimer = null;
    this.oUl = $(idUl);
    this.oOUl = this.oUl.clone();
    this.CTL = 0;
    //设置默认属性
   	this.options = {	//默认值
   		row: 5,			//显示行数
  		time: 15,		//速度(越大越慢)
      	pause: 2000,	//停顿时间
      	direct: 0		//0向上,1向下,2向左,3向右
   	};
   	this.options = $.extend(self.options, options || {});   	
    //向下
    this.Next = function() {
    	if (!self.canscroll) return false;
    	clearTimeout(self.pausetimer);
    	clearInterval(self.steptimer);
    	var height = self.oUl.find("li:first").height();
    	self.steptimer = setInterval(function(){
    		if (self.CTL >= height) {
    			self.CTL = 0;
    			clearInterval(self.steptimer);
    			self.oUl.find("li:first").appendTo(self.oUl);
    			self.oUl.get(0).scrollTop = 0;
    			self.pausetimer = setTimeout(self.Next, self.options.pause);
    		} else {
    			self.CTL++;
    			self.oUl.get(0).scrollTop++;
    		}
    	}, self.options.time);
    }
    //向左
    this.Left = function() {
    	if (!self.canscroll) return false;
    	clearTimeout(self.pausetimer);
    	clearInterval(self.steptimer);
    	var width = self.oUl.find("li:first").width();
    	self.steptimer = setInterval(function(){
    		if (self.CTL >= width) {
    			self.CTL = 0;
    			clearInterval(self.steptimer);
    			self.oUl.find("li:first").appendTo(self.oUl);
    			self.oUl.scrollLeft = 0;
    			self.pausetimer = setTimeout(self.Left, self.options.pause);
    		} else {
    			self.CTL++;
    			self.oUl.scrollLeft--;
    		}
    	}, self.options.time);
    }
    //上一个
    this.GoPrev = function() {
    	if (!self.canscroll) return false;
    	if (self.CTL > 0) {
    		self.CTL = 0;
    		self.oUl.get(0).scrollTop = 0;
    	} else {
    		self.oUl.find("li:last").prependTo(self.oUl);
    	}
    }
    //跳到第一个
    this.GoFirst = function() {
    	if (!self.canscroll) return false;
    	while(!self.oUl.find("li:first").hasClass("first")) {
    		this.GoPrev();
    	}
    }
  	//停止
  	this.Stop = function() {
    	clearTimeout(self.pausetimer);
    	clearInterval(self.steptimer);
  	}
    //增加一个LI
    this.Append = function(li, limit, type) {
    	limit = limit - 1;
    	self.oOUl.find(".first").removeClass("first").before(li);
    	if (type == 'new') {
    		self.oOUl.find(".speaker_new").remove();
    		self.oOUl.find("li.mine:gt("+(limit)+")").remove();
    	} else {
    		self.oOUl.find(".speaker_user").remove();
    		self.oOUl.find(".first").nextAll("li:gt("+(limit-1)+")").find(".speaker_new").remove();
    	}
    	self.oOUl.find("li.nohas").remove();
    	self.Stop();
    	self.Start();
    }
    this.Start = function() {
	    //数据是否够滚动
	    self.canscroll = (self.oOUl.find('li').length > self.options.row);    
	  	if (self.canscroll) {
	   		self.oUl.html(self.oOUl.html()+self.oOUl.html());
	   	} else {
	   		self.oUl.html(self.oOUl.html());
	   	}
	  	switch (self.options.direct) {
	  		case 0 : 
	  		   	self.pausetimer = setTimeout(self.Next, self.options.pause);
	  		   	break;
	  		case 1 :
	  			self.pausetimer = setTimeout(self.Prev, self.options.pause);
	  		   	break;
	  		case 2 :
	  			self.pausetimer = setTimeout(self.Left, self.options.pause);
	  		   	break;
	  		case 3 :
	  			self.pausetimer = setTimeout(self.Right, self.options.pause);
	  		   	break;
	  	}
   	}
   	this.oUl.mouseover(function(){self.Stop();});
   	this.oUl.mouseout(function(){self.Next();});
   	this.Start();   	
}