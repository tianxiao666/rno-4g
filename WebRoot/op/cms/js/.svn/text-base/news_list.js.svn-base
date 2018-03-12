
var i = 150;
var ni = 0;
var de = 0;
var nei = 0;
var tid = "";
$(function(){
 	//alert($("#news_list_container").css("height"));
 	var mheight = $("#news_list_container").css("height");
 	if(!mheight){
 		return;
 	}
 	ni = parseInt(mheight.substring(0,mheight.indexOf("px")));
 	//alert(ni);
 	nei = parseInt("-"+mheight.substring(0,mheight.indexOf("px")));
 	//i = i + ni;
 	//$(".news_list_ul li").hide();
 	var uheight = $(".news_list_ul li").css("height");
 	de = uheight.substring(0,uheight.indexOf("px"));
	news_list_container();
 	
});
 function news_list_container(){
 	if(ni < 150){
 		return;
 	}
 	$("#news_list_container").css("margin-top",i);
 	i--;
 	if(i == 0 || i < 0){
 		//var liindex = nei/i;
 		//$(".news_list_ul li").each(function(){
 		//	if(liindex >  0){
 		//		$(".news_list_ul li").eq(liindex).show();
 		//	}
 		//	liindex--;
		//});
 	}
 	if(i == nei || i < nei){
 		i = 150;
 		//$(".news_list_ul li").hide();
 	}
 	//var mi = 150 + ni - i;
 	//alert(mi);
 	//if(mi > de && mi != 0){
 	//	var liindex = mi/de;
 	//	$(".news_list_ul li").each(function(){
 	//		if(liindex >  0){
 	//			$(".news_list_ul li").eq(liindex).show();
 	//		}
 	//		liindex--;
	//	});
 	//}
 	tid = setTimeout("news_list_container()",30);
 }

 function pageNumberClick(pageIndex,pageSize,totalPageCount){
 //响应数字页码的点击事件
   if(pageIndex<0){
      return;
   }
   if(pageIndex>=totalPageCount){
      return;
   }
   
   //alert("pageIndex = "+pageIndex + ",pageSize = "+pageSize+" totalPage="+totalPage);
   var totalCount=$("#totalCount").val();
   window.location.href='getRangeAnnouncementAction?pageIndex='+pageIndex+"&pageSize="+pageSize+"&totalCount="+totalCount+"&totalPageCount="+totalPageCount;
  
 }
 
 //点击那个跳转按钮
 function showAnnouncementByPage(pageSize,totalPageCount){
   
    var pageIndex=new Number($("#txtShowPage").val());
    if(isNaN(pageIndex)){
      alert("请输入数字");
      return;
    }
    pageIndex--;//这个pageIndex在程序里是以0开始的，用户的输入是从1开始，所以要减去1
    if(pageIndex>=totalPageCount){
       alert("最大不能超过总页数 ："+totalPageCount);
       return;
    }
    
    pageNumberClick(pageIndex,pageSize,totalPageCount);
 }
 
 //暂停
 function stop_news_list_container(){
 	clearTimeout(tid);
 }
 
 function start_news_list_container(){
 	if(ni < 150){
 		return;
 	}
 	tid = setTimeout("news_list_container()",30);
 }