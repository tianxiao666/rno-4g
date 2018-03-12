// JavaScript Document

	$(function(){
		/*日期选择*/
		$('#datepicker').datepicker({
			inline: true,
			dateFormat:"yy-mm-dd",
			onSelect:function(dateText,inst){
				
				/*点击日期事件*/
				var choose_date = new Date(dateText);
				var choose_day = choose_date.getDate();
				$(".dd_show").html(choose_day + "日");
				/* 有无任务颜色显示不同，有任务为红色，无任务为绿色 */
				$(".dd_show").addClass("green");
			},
			showMonthAfterYear:true
		});
	})
	function test(){
		var str = "[{'title':'title1','begin_time':1,'end_time':15},{'title':'title2','begin_time':10,'end_time':15},{'title':'title3','begin_time':9,'end_time':15},{'title':'title4','begin_time':2,'end_time':9},{'title':'title3','begin_time':12,'end_time':24},{'title':'title3','begin_time':12,'end_time':24},{'title':'title3','begin_time':12,'end_time':24},{'title':'title3','begin_time':12,'end_time':24},{'title':'title3','begin_time':12,'end_time':24},{'title':'title3','begin_time':12,'end_time':24},{'title':'title3','begin_time':12,'end_time':24}]";
		str = eval(str);
		page_set_json(0,4,str);
	}
	/*json对象调用*/
	function mission_json(jsonA,date_time){
		if(date_time!=null){
			var time = date_time.split("-");
			var chineseTime = time[0]+"年"+time[1]+"月"+time[2]+"日";
			$("#chineseTime").html(chineseTime);
			$("#norm_time").val(date_time);
		}else{
			var dateTime = new Date();
			var m = dateTime.getMonth()+1;
			var d = dateTime.getDate();
			var h = dateTime.getHours();
			var miu = dateTime.getMinutes();
			var s = dateTime.getSeconds();
			var time = dateTime.getFullYear()+"-"+m+"-"+d;
			var chineseTime = dateTime.getFullYear()+"年"+m+"月"+d+"日";
			$("#chineseTime").html(chineseTime);
			$("#norm_time").val(time);
		}
		
	    var gantt_content = $(".gantt_content");  //容器
		gantt_content.html("");
		$.each(jsonA, function(index, obj){
			gantt(obj.title,obj.begin_time,obj.end_time,$("#norm_time").val());
		});
	}
	
	/*json对象分页*/
	function page_set_json(begin,end,jsonA){
		for(var begin; begin<=end; begin++){
			var obj = jsonA[begin];
			gantt(obj.title,obj.begin_time,obj.end_time);
		}
	}
	
	

	
	//任务名，起始时间，结束时间  pagetime:甘特图显示的日期
    function gantt(title,begin_time,end_time,page_time){
		var begin_times = begin_time.split(" ");
		var end_times = end_time.split(" ");
		var page_times = page_time.split("-");
		//取日期
		var begin_date = begin_times[0].split("-");
		var end_date = end_times[0].split("-");
		//取时间
		var begin_hour = begin_times[1].split(":");
		var end_hour = end_times[1].split(":");
		var title
		//字符串转数字
		this.title = title;
		var begin_time_hour = parseFloat(begin_hour[0]);
		var end_time_hour = parseFloat(end_hour[0]);
		
		var gantt_content = $(".gantt_content");  //容器
		var gantt_line = document.createElement("div");  //创建div
		gantt_line.className = "gantt_line";
		gantt_line.innerHTML = "<span class='gantt_title'>"+ "</span>";
		gantt_content.append(gantt_line);
		
		//计算好距离 根据起始时间和结束时间设定 左边距 和 长度
		//（月份）当派发时间=截止时间
		if(parseFloat(end_date[1])==parseFloat(begin_date[1])){
			//（日）当派发时间=截止时间
			if(parseFloat(end_date[2])==parseFloat(begin_date[2])){
				gantt_line.style.marginLeft = begin_time_hour*12.5 + "px";
				var width = (parseFloat(end_time_hour) - parseFloat(begin_time_hour))*12.5-1 ;
				gantt_line.style.width = width+"px"
			}else 
			//（日）当截止时间>派发时间
			if(parseFloat(end_date[2])>parseFloat(begin_date[2])){
				//（日）当前页面时间=派发时间
				if(parseFloat(page_times[2])==parseFloat(begin_date[2])){
					gantt_line.style.marginLeft = begin_time_hour*12.5 + "px";
					var width = (24 - parseFloat(begin_time_hour))*12.5-1 ;
					gantt_line.style.width = width+"px"
				}else
				//（日）当前页面时间=截止时间
				if(parseFloat(page_times[2])==parseFloat(end_date[2])){
					gantt_line.style.marginLeft = begin_time_hour*12.5 + "px";
					var width = (parseFloat(end_time_hour) - 0)*12.5-1 ;
					gantt_line.style.width = width+"px"
				}else
				//（日）当前页面时间<截止时间且当前页面时间>派发时间
				if(parseFloat(page_times[2])<parseFloat(end_date[2])&&parseFloat(page_times[2])>parseFloat(begin_date[2])){
					gantt_line.style.marginLeft = 0*12.5 + "px";
					var width = 24*12.5-1 ;
					gantt_line.style.width = width+"px"
				}
			}
		}else
		//（月份）当派发时间>截止时间
		 if(parseFloat(end_date[1])>parseFloat(begin_date[1])){
		 	//（月份）当前时间=派发时间
		 	if(parseFloat(page_times[1])==parseFloat(begin_date[1])){
		 		//（日）当前时间=派发时间
		 		if(parseFloat(page_times[2])==parseFloat(begin_date[2])){

		 			gantt_line.style.marginLeft = begin_time_hour*12.5 + "px";
					var width = (24 - parseFloat(begin_time_hour))*12.5-1 ;
					gantt_line.style.width = width+"px"
		 		}else
		 		//（日）当前时间>派发时间
		 		if(parseFloat(page_times[2])>parseFloat(begin_date[2])){
		 			gantt_line.style.marginLeft = 0*12.5 + "px";
					var width = 24*12.5-1 ;
					gantt_line.style.width = width+"px"
		 		}
		 	}else 
		 	//（月份）当前时间=截止时间
		 	if(parseFloat(page_times[1])==parseFloat(end_date[1])){
		 		//（日）当前时间=截止时间
		 		if(parseFloat(page_times[2])==parseFloat(end_date[2])){
		 			gantt_line.style.marginLeft = begin_time_hour*12.5 + "px";
					var width = (parseFloat(end_time_hour) - 0)*12.5-1 ;
					gantt_line.style.width = width+"px"
		 		}else
		 		//（日）当前时间<截止时间
		 		if(parseFloat(page_times[2])<parseFloat(end_date[2])){
		 			gantt_line.style.marginLeft = 0*12.5 + "px";
					var width = 24*12.5-1 ;
					gantt_line.style.width = width+"px"
		 		}
		 	}
		 }
		
		
		gantt_line.setAttribute("title",title);
	}
	
	/*获取当前时间并显示,精确到15分钟*/
	function show_now_time(){
		var date = new Date();
		var this_hour = date.getHours();
		var this_minute = parseInt(date.getMinutes() / 15);
		$(".now_time").css("marginLeft",this_hour*12.5 +this_minute*3.125 - 7 + "px");
		$(".now_time").attr("title","当前时间：" + date.getHours() + ":" + date.getMinutes());
	}
	
	/*创建甘特图容器*/
	function createGanttContent(targetContent,ganttContentId,datepickerClass,resourceId,resourceType,isOverTime){
		//清空目标容器HTML
		$("#"+targetContent).html("");
		
		var currentTime=new Date();
		var year=currentTime.getFullYear();
		var month=currentTime.getMonth()+1;
		var date=currentTime.getDate();
		currentTime = year+"-"+month+"-"+date;
		
		var gt = "";
		gt+="<div class='gantt' ><div class='gantt_title'>";
		gt+="<div class='date_select'><em class='dd_show red'>"+date+"日</em><input type='hidden' value='"+resourceType+"'/><input type='hidden' value='"+resourceId+"'/><div class='"+datepickerClass+"'></div></div></span></div>";
		gt+="<div class='gantt_main mt10'><div class='gantt_top'><div class='now_time' title='当前时间'></div></div><div id='"+ganttContentId+"' class='gantt_content'> </div></div></div></div>";
		//gt = $(gt);
		
		targetContent = "#"+targetContent;
		//gt.appendTo($(targetContent));
		$(targetContent).html(gt);
		
		ganttContentId = "#"+ganttContentId;
		
		//获取数据
		createResourceGantt(resourceId,resourceType,currentTime,ganttContentId,isOverTime);
		
		/*日期选择*/
		$("."+datepickerClass).datepicker({
			inline: true,
			dateFormat:"yy-mm-dd",
			onSelect:function(dateText,inst){
				/*点击日期事件*/
				var choose_date = new Date(dateText);
				var choose_day = choose_date.getDate();
				$(targetContent+" .dd_show").html(choose_day + "日");
				//获取数据
				createResourceGantt(resourceId,resourceType,dateText,ganttContentId,isOverTime);
	       	},
			showMonthAfterYear:true
		});
		/*隐藏日历*/
		$(targetContent+" .ui-datepicker-calendar").hide();
		$(targetContent+" .dd_show").click(function(){
			$(targetContent+" .ui-datepicker-calendar").toggle();
			if($(targetContent+" .ui-datepicker-calendar").css("display") == "none"){
				$(targetContent+" .dd_show").css("background","#ddd url(../../jslib/gantt/images/dd_down.png) right 0px no-repeat");
			}else{
				$(targetContent+" .dd_show").css("background","#ddd url(../../jslib/gantt/images/dd_up.png) right 0px no-repeat");
			}
		});
	}
	//创建资源甘特图
	function createResourceGantt(resourceId,resourceType,taskDate,content,isOverTime){
		$.ajax({
			url : "getResourceTaskGanttAction",
			data : {"taskDate":taskDate,"resourceId":resourceId,"resourceType":resourceType,"isOverTime":isOverTime},
			dataType : 'json',
			async:false,
			type : 'POST',
			success : function(result) {
				mission_json(result,content,null);
			}
		});
		show_now_time();
	}
	/** 绑定事件 **/
	function bindResourceMonthGanttEvent(targetContent,resourceId,resourceType){
		if(!targetContent){
			return false;
		}
		var year = $(targetContent+" .ui-datepicker-year").html().replace("年","-");
		var month = $(targetContent+" .ui-datepicker-month").html().replace("月","-");
		var taskDate = year+month;
		var endDate = "30";
		$(targetContent+" .ui-datepicker-calendar tbody td a").each(function(){
			endDate = $(this).html();
		});
		$.ajax({
			url : "getResourceMonthTaskAction",
			data : {"taskDate":taskDate,"endDate":endDate,"resourceId":resourceId,"resourceType":resourceType},
			dataType : 'json',
			async:false,
			type : 'POST',
			success : function(result){
				if(result){
					$(targetContent+" .ui-datepicker-calendar tbody td a").each(function(){
						var i = $(this).html();
						var ti = result[i];
						var hasTask = ti.hasTask;
						// 有无任务颜色显示不同，有任务为红色，无任务为绿色
						var cs = "bg_green";
						if(hasTask&&hasTask=='true'){
							cs = "bg_red";
							$(this).attr("class",cs);
						}
					});
				}
			}
		});
	}
	/*json对象调用*/
	function mission_json(jsonA,content,nullParam){
		$(content).html("");
		if(jsonA){
			$.each(jsonA, function(index, obj){
				gantt(obj.title,obj.begin_time,obj.end_time,content,nullParam);
			});
		}
	}
	//任务名，起始时间，结束时间
    function gantt(title,begin_time,end_time,content,nullParam){
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
