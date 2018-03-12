$(function(){
	/*显示隐藏gis右边框*/
	$("#gis_content_right_show_img").click(function(){
		$("#gis_content_right_main").toggle();
		if($(this).attr("src") == "image/show_right.png"){
			$(this).attr("src","image/hide_right.png");
			$("#reportMonitorDiv").css("left","330px");
		}else{
			$(this).attr("src","image/show_right.png");
			$("#reportMonitorDiv").css("left","15px");
		}
		var left = $("#city_select").offset().left;
		$(".map_city").css({"left" : left+"px"} );
		left = $("#district_select").offset().left;
		$(".map_district").css({"left" : left+"px"} );
		left = $("#street_select").offset().left;
		$(".map_street").css({"left" : left+"px"} );
	});
	/*全屏*/
	$("#full_screen").click(function(){
		if($("#top_content").css("display")== "block"){
			$("#top_content").hide();
			var height = document.documentElement.clientHeight; 
			height = height - 40;
			$("#gis_map").css("height",height+"px");
			$("#monitorResourceDiv").css("height",height+"px");
			$("#searchResultDiv").css("height",height+"px");
			$(this).html("<h4 class='fullScreen_ico'>退出全屏</h4>"); 
		}else{
			$("#top_content").show();
			var height = document.documentElement.clientHeight; 
			height = height - 205;
			$("#gis_map").css("height",height+"px");
			$("#monitorResourceDiv").css("height",height+"px");
			$("#searchResultDiv").css("height",height+"px");
			$(this).html("<h4 class='fullScreen_ico'>全屏</h4>"); 
		}
	})
	
	$(".top_tab_content").each(function(){
		$(this).find($(".sort_box a")).each(function(index){
			$(this).click(function(){
				$(this).addClass("current");
				$(this).siblings().removeClass("current");
				$(this).parent().parent().find($(".resource_list")).hide().eq(index).show();
			})
		})
	})
	
	//treeview事件
	$("#tree").treeview({
		collapsed: false,
		animated: "fast",
		control:"#sidetreecontrol"
	});
	
	//右边信息框Table切换
	$(".top_tab ul li .tab_title").each(function(index){
		$(this).click(function(){
			$(".top_tab ul li").removeClass("active");
			$(this).parent().addClass("active");
			$(".top_tab_content").hide();
			$(".top_tab_content").eq(index).show();
		})
	});
	
	$(".tab_close").each(function(index){
		$(this).click(function(){
			$(this).parent().hide();
			if(index == 0){
				$("#searchResultDiv").hide();
			}else if(index == 1){
				$("#gis_content_right_data").hide();
			}
			$("#monitorLi").addClass("active");
			$("#monitorResourceDiv").css("display","block");
		})
	})
	
     /**网络设施的弹框_table切换*/
   	$(".dialog-tabmenu li").live("click",function(){
   		$(".dialog-tabmenu li").removeClass("on");
   		$(this).addClass("on");
   		$(".dialog-tabcontent dd").hide();
   		var curIndex = $(".dialog-tabmenu li").index($(this));
   		$(this).parent().parent().find($(".dialog-tabcontent")).find("dd").eq(curIndex).show();
   		
   	});
   	/*顶部搜索*/
    $(".inline_btn h4.search_ico").click(function(){
		$(".search_span_bg").slideToggle("fast");
	});
    /*
	$(".search_span_bg").hover(function(){
		$(".search_span_bg").show();
	},function(){
		$(".search_span_bg").hide();
	});
	* */
	/* 创建工单tool */
	$(".cjgd_choose").hover(function(){
			$(".cjgd_choose_button").show();
		},function(){
			$(".cjgd_choose_button").hide();
	});

	/* 左右选择样式切换 */
	$(".left_on").click(function(){
		$(".left_right_on").css("background","url(image/left_on.png) no-repeat");
	})
	$(".right_on").click(function(){
		$(".left_right_on").css("background","url(image/right_on.png) no-repeat");
	})
	$(".tab_1 ul li").click(function(){
		var biztype = $(this).attr("biztype");
		$("#curBizType").html(biztype);
		$("#pagingColunmSelect").val(10);
		$("#curPageIndex").val(1);
		$(".tab_1 ul li").removeClass("ontab");
		$(this).addClass("ontab");
	})
	
	/*tab中的tab*/
	$(".left_tree_tab").each(function(){
		$(this).find("li").each(function(index){
			$(this).click(function(){
				$(this).parent().find("a").removeClass("selected");
				$(this).parent().find("a").eq(index).addClass("selected");
				$("#pagingColunmSelect").val(10);
			})
		})
	})
	$(".bt_search").click(function(){
		if($(this).attr("checked") == "checked"){
			$(".bt_search_show").slideDown(100);
		}else{
			$(".bt_search_show").slideUp(100);
		}
	})
});

