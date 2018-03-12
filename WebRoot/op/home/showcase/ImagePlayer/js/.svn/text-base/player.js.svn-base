/*
 * images player
 * author:mr·zhong
 * date:2010-04-19
 */
 
//当前导航页码数字
var currentPage = 1;
//图片间隔时间
var playerTime = 3000;

$(function(){	
	SetPlayerNavPosition();		   	
	OnLoadingImages();	
	OnLoadingLinks();
	setInterval("Player()",playerTime);
});

/*
 * 图片播放方法
 */
function Player(){
	PageClick($("#page_" + currentPage));
	
	if(currentPage == $("#playerNav a").length)
		currentPage = 1;
	else
		currentPage++;
}

/*
 * 创建图片页码·并绑定页码的click事件
 * count:需要创建页面的个数
 */
function CreatePageNberObj(count){
	var pages = '';

	for(var i = 1; i <= (count - 1); i++){
		pages += '<a id="page_' + i + '" idx="' + i + '" onfocus="blur()">' + i + '</a>';		
	}
	
	$("#playerNav").html(pages);
	
	for(var i = 1; i <= count; i++){
		BindPageClick("page_" + i);
	}
}

/*
 * 加载图片集·并为图片设定唯一ID，最后显示一张隐藏其它
 */
function OnLoadingImages(){
	var li_id = 1;
	
	$("#playerImage li").each(function(){
		$(this).attr("id","play_img_" + li_id);				
		
		if(li_id > 1){
			SetImageShowOrHide($(this),false);
		}
		
		li_id++;
	});
}

/*
 * 加载图片相对应链接集·并为链接设定唯一ID，最后显示对相应的链接隐藏其它
 */
function OnLoadingLinks(){
	var a_id = 1;
	
	$("#playerTitle a").each(function(){
		$(this).attr("id","link_" + a_id);				

		if(a_id > 1){
			SetImageShowOrHide($(this),false);
		}
						
		a_id++;
	});
	
	CreatePageNberObj(a_id);
}

/*
 * 绑定图片页码的click事件底层方法
 */
function BindPageClick(id){
	$("#" + id).click(function(){
		PageClick($(this));
	});
}

/*
 * 图片页码Click处理方法
 * obj:当前页码元素对象
 */
function PageClick(obj){
	var id = obj.attr("idx");	
	
	//当页码在自动播放的时候点击了某个页码数字必须再重新赋值给当前的currentPage记录器
	currentPage = id;
	//设置所有页码样式为默认
	$("#playerNav a").removeClass("hover");
	//设置当前选中的页码数字为指定的颜色
	SetPageColor(obj,true);				
	
	//显示或隐藏图片
	$("#playerImage li").each(function(){										   
		if($(this).attr("id") == ("play_img_" + id)){
			SetImageShowOrHide($(this),true);
		}else{
			SetImageShowOrHide($(this),false);			
		}									
	});
	
	//显示或隐藏文字链
	$("#playerTitle a").each(function(){										   
		if($(this).attr("id") == ("link_" + id)){
			SetImageShowOrHide($(this),true);
		}else{
			SetImageShowOrHide($(this),false);			
		}									
	});	
}

/*
 * 设置指定的元素或图片显示或不隐藏
 * obj:需要隐藏的元素
 * isShow:显示or隐藏
 */
function SetImageShowOrHide(obj,isShow){
	if(!isShow){
		obj.fadeOut(1000);
	}else{
		obj.fadeIn(2000);
	}
}

/*
 * 设置指定的图片页码样式
 * obj:需要设置的元素
 * isSelect:是否选中
 */
function SetPageColor(obj,isSelect){
	if(!isSelect){
		obj.removeClass("hover");
	}else{
		obj.addClass("hover");
	}
}

/*
 * 设置图片数字链接和图片标题DIV位置
 */
function SetPlayerNavPosition(){
	var offset = $("#playerBox").offset();
	var boxHeight = $("#playerBox").height();
	var navHeight = $("#playerNavAndTitle").height();
	var navHeight2 = $("#playerNav").height();
	var titleHeight = $("#playerTitle").height();
	
	//$("#playerNavAndTitle").css({ top:(offset.top + boxHeight - navHeight) + 1 + "px", left:offset.left + 1 + "px" });
	$("#playerNavAndTitle").css({ top:(offset.top + boxHeight - navHeight2) + 1 + "px", left:offset.left + 1 + "px" });
	//$("#playerTitle").css({ top:(offboxHeight )  + "px"});
}