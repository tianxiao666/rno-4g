

function LoadingDiv ( opt ) {
	
	this.id = null;
	this.parentId = null;
	this.loading = null;
	this.loadingMsg = null;
	this.loading_div = null;
	
	{
		var defOpt = {
			"loadingMsg" : "请稍后"
		};
		opt = $.extend(opt,defOpt);
		this.id = opt.id;
		this.parentId = opt.parentId;
		this.loadingMsg = opt.loadingMsg;
		this.loading = $("<div />").addClass("loading_div").appendTo($("body"));
		this.loading_div = $("<div />").addClass("loading_msg_div").appendTo($(this.loading));
		var loading_img = $("<img >").attr({"src":getRootPath()+"/images/loading_img.gif"}).addClass("loading_img").appendTo($(this.loading_div));
		var loading_msg = $("<span />").text(this.loadingMsg).appendTo($(this.loading_div));
		$(this.loading).css({"position":"absolute","display":"none","background-color":"white","opacity":"0.6"});
		$(this.loading_div).css({"position":"absolute"});
	}
	
	this.showLoading = function () {
		//设计div规格
		var pwidth = null;
		var pheight = null;
		var pleft = null;
		var ptop = null;
		if ( !this.parentId ) {
			pwidth = $(window).width();
			pheight = $(window).height(); 
			pleft = $("body").offset().left;
			ptop = $("body").offset().top;
		} else {
			pwidth = $(this.parentId).width();
			pheight = $(this.parentId).height();
			pleft = $(this.parentId).offset().left;
			ptop = $(this.parentId).offset().top;
		}
		
		$(this.loading).width(pwidth);
		$(this.loading).height(pheight);
		$(this.loading).css({"top": ptop + "px" , "left": pleft + "px"});
		var dwidth = $(this.loading_div).width();
		var dheight = $(this.loading_div).height();
		var dtop = pheight / 2 - dheight / 2;
		var dleft = pwidth / 2 - dwidth / 2;
		$(this.loading_div).css({"top": dtop + "px" , "left": dleft + "px"});
		//显示div
		$(this.loading).fadeIn(200);
	}
	
	this.closeLoading = function () {
		$(this.loading).fadeOut(200);
	}
	
	
	this.setParentId = function ( pId ) {
		this.parentId = pId;
	}
	
	/**
	 * 获取当前工程的虚拟目录
	 * @return
	 */    
	function getRootPath(){
        var strFullPath=window.document.location.href;
        var strPath=window.document.location.pathname;
        var pos=strFullPath.indexOf(strPath);
        var prePath=strFullPath.substring(0,pos);
        var postPath=strPath.substring(0,strPath.substr(1).indexOf('/')+1);
        return(prePath+postPath);
    }
	
}