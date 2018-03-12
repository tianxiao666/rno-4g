// JavaScript Document
//获取class方法
var ganttPageSize = 5;
var ganttData = null;
var ganttCurrentIndex = 0;
var ganttTotalPage = 0;
function getElementsByClassName (className) {
	   var all = document.all ? document.all : document.getElementsByTagName('*');
	   var elements = new Array();
	   for (var e = 0; e < all.length; e++) {
		 if (all[e].className == className) {
			elements[elements.length] = all[e];
		   break;
		  }
		}
	   return elements;
}

	/*json对象调用*/
	function mission_json(jsonA,content){
		$(content).html("");
		if(jsonA){
			$.each(jsonA, function(index, obj){
				gantt(obj.title,obj.begin_time,obj.end_time,content);
			});
		}
	}
	
	/*json对象分页*/
	function page_set_json(begin,end,jsonA){
		$(".gantt_content").html("");
		var jsonALength = jsonA.length;
//		console.log("jsonALength:"+jsonALength+",begin:"+begin+",end:"+end);
		if(end>jsonALength-1){
			end = jsonALength-1;
		}
		for(var begin; begin<=end; begin++){
			var obj = jsonA[begin];
			gantt(obj.title,obj.begin_time,obj.end_time);
		}
	}
	/*获取页面元素位置*/
	$(document).ready(function(){
		$("table td a").each(function(){
			$(this).click(function(){
				var offset = $(this).offset();
				$("#gantt").css("top",offset.top + 20 + "px");
				$("#gantt").css("left",offset.left - 50 + "px");
				$("#gantt").show();
			})
		})
		//甘特图显示 和 隐藏
/*		$("#gantt").mouseover(function(){
			$("#gantt").show();
		})	
		$("#gantt").mouseout(function(){
			$("#gantt").hide();
		})
		
		$("#close_gantt").click(function(){
			$("#gantt").hide();
			})                          */
	})

	
	//任务名，起始时间，结束时间
    function gantt(title,begin_time,end_time){
		var title,begin_time,title;
//		console.log(title+","+begin_time+","+end_time);
		//字符串转数字
		this.title = title;
		this.begin_time = parseFloat(begin_time);
		this.end_time = parseFloat(end_time);
		
		//计算好距离 根据起始时间和结束时间设定 左边距 和 长度
		var marginLeft = begin_time*12.5 + "px";
		var width = (end_time - begin_time)*12.5 - 1 + "px";
		
		$(".gantt_content").html($(".gantt_content").html()+"<div class='gantt_line' title='"+title+"' style='width:"+width+"; margin-left:"+marginLeft+";'><span class='gantt_title'>"+ "</span></div>");
//		alert($(".gantt_content").html());
	}
	
	//任务名，起始时间，结束时间
    function gantt(title,begin_time,end_time,content){
		var title,begin_time,title;
//		console.log(title+","+begin_time+","+end_time);
		//字符串转数字
		this.title = title;
		this.begin_time = parseFloat(begin_time);
		this.end_time = parseFloat(end_time);
		
		//计算好距离 根据起始时间和结束时间设定 左边距 和 长度
		var marginLeft = begin_time*12.5 + "px";
		var width = (end_time - begin_time)*12.5 - 1 + "px";
		
		$(content).html($(content).html()+"<div class='gantt_line' title='"+title+"' style='width:"+width+"; margin-left:"+marginLeft+";'><span class='gantt_title'>"+ "</span></div>");
	}
	
	/*获取当前时间并显示,精确到15分钟*/
	function show_now_time(){
		var date = new Date();
		var this_hour = date.getHours();
		var this_minute = parseInt(date.getMinutes() / 15);
		$(".now_time").css("margin-left",this_hour*12.5 +this_minute*3.125 - 7 + "px");
		$(".now_time").attr("title","当前时间：" + date.getHours() + ":" + date.getMinutes());
	}
