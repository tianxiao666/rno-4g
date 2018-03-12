// JavaScript Document
	$(document).ready(function(){
		/* checkbox点击隐藏 */
		$("#fieldset-1 .container-tab2 ul li:last").css("border-right","1px solid #ccc");//为tab的最后一个li加上左边框
		$("#fieldset-2 .container-tab2 ul li:last").css("border-right","1px solid #ccc");
		$("#fieldset-3 .container-tab2 ul li:last").css("border-right","1px solid #ccc");

		$(".container-tab1 fieldset legend :checkbox").each(function(){
			if($(this).attr("checked")=="checked"){
					$(this).parent().parent().removeClass("fieldset-hide");
					$(this).parent().parent().find(".container-main").show();
					}
					else{
						$(this).parent().parent().addClass("fieldset-hide");
						$(this).parent().parent().find(".container-main").hide();
						}
			$(this).click(function(){
				if($(this).attr("checked")=="checked"){
					$(this).parent().parent().removeClass("fieldset-hide");
					$(this).parent().parent().find(".container-main").show();
					}
					else{
						$(this).parent().parent().addClass("fieldset-hide");
						$(this).parent().parent().find(".container-main").hide();
						}
				})
			})
			
			$(".hide_show_checkbox").click(function(){
				if($(this).attr("checked")=="checked"){
					$(this).parent().parent().parent().find("tr").not($(".main-table1-tr")).show();
					}else{
						$(this).parent().parent().parent().find("tr").not($(".main-table1-tr")).hide();
						}
				})
			
			
		/* tab门 */
		$("#fieldset-1 .container-tab2 ul li").each(function(){
			$(this).click(function(){
				$("#fieldset-1 .container-tab2 ul li").removeClass("tab2-li-show");
				$(this).addClass("tab2-li-show");
				})
			})
		$("#fieldset-1 .container-tab2 ul li").each(function(index){
			$(this).click(function(){
					$("#fieldset-1 .container-main-table1-tab").hide();
					$("#fieldset-1 .container-main-table1-tab").eq(index).show();
				})
			})
			
		$("#fieldset-2 .container-tab2 ul li").each(function(){
			$(this).click(function(){
				$("#fieldset-2 .container-tab2 ul li").removeClass("tab2-li-show");
				$(this).addClass("tab2-li-show");
				})
			})
		$("#fieldset-2 .container-tab2 ul li").each(function(index){
			$(this).click(function(){
					$("#fieldset-2 .container-main-table1-tab").hide();
					$("#fieldset-2 .container-main-table1-tab").eq(index).show();
				})
			})
			
		$("#fieldset-3 .container-tab2 ul li").each(function(){
			$(this).click(function(){
				$("#fieldset-3 .container-tab2 ul li").removeClass("tab2-li-show");
				$(this).addClass("tab2-li-show");
				})
			})
		$("#fieldset-3 .container-tab2 ul li").each(function(index){
			$(this).click(function(){
					$("#fieldset-3 .container-main-table1-tab").hide();
					$("#fieldset-3 .container-main-table1-tab").eq(index).show();
				})
			})

		/* 点击加减按钮收起 */
		$(".main-table1 .hide-show-img").each(function(){
			if($(this).attr("src") == "images/ico_show.gif"){
				$(this).parent().parent().parent().find("tr").not($(".main-table1-tr")).show();
			}else{
				$(this).parent().parent().parent().find("tr").not($(".main-table1-tr")).hide();
				}
		});	
			
		$(".main-table1 .hide-show-img").live('click',function(){
			if($(this).attr("src") == "images/ico_show.gif"){
				$(this).parent().parent().parent().find("tr").not($(".main-table1-tr")).hide();
				$(this).attr("src","images/ico_hide.gif");
			}else{
				$(this).parent().parent().parent().find("tr").not($(".main-table1-tr")).show();
				$(this).attr("src","images/ico_show.gif");
				}
			})
		})
		
		$(document).ready(function(){
			$(".search-button").click(function(){
				$("#search-table").hide();
				$("#black").hide();
				})
			$(".search-no").click(function(){
			$("#search-table").hide();
			$("#black").hide();
			})
		})
		
		
		$(function(){
			$(".main-table1 #gaojisousuo").click(function(){
				$("#black").show();
				$("#search-table").show();
			})
		})
			if($(".isWork").html()=="是"){
				$(".isWork").css("background","Chartreuse");
			}
			

		$(document).ready(function(){
			$(".main-table2 tr th>input").click(function(){
				$(".main-table2 tr td>input").attr("checked",$('.main-table2 tr th>input').attr('checked'));

			})
		$("#tree>li>input").click(function(){
				$("#tree>li>ul>li>input").attr("checked",true);
			})
			$(".a_tabs a").each(function(index){
				$(this).click(function(){
					$(".a_tabs a").removeClass("a_tabs_on");
					$(this).addClass("a_tabs_on");
					$(".a_tabs_table").hide();
					$(".a_tabs_table").eq(index).show();
				})
	})
		})
			
$(function(){
	/*隐藏日历*/
	$(".ui-datepicker-calendar").hide();
	$(".dd_show").click(function(){
		$(".ui-datepicker-calendar").toggle();
		if($(".ui-datepicker-calendar").css("display") == "none"){
			$(".dd_show").css("background","#ddd url(../../images/dd_down.png) right 0px no-repeat");
		}else{
			$(".dd_show").css("background","#ddd url(../../images/dd_up.png) right 0px no-repeat");
		}
	})
	$(".ui-datepicker-prev").live('click',function(){
		$(".dd_show").css("background","#ddd url(../../images/dd_up.png) right 0px no-repeat");
	});
	
	$(".ui-datepicker-next").live('click',function(){
		$(".dd_show").css("background","#ddd url(../../images/dd_up.png) right 0px no-repeat");
	})
});
$(function(){
	/* 催办、撤销点开 */
	$(".button_tabs input").each(function(index){
		$(this).click(function(){
			$(".handle_content").hide();
			$(".button_tabs_content").hide();
			$(".button_tabs_content").eq(index).show(100);
		})
	})
	/* 派发任务 点开 */
	$(".show_handle_content").click(function(){
		$(".button_tabs_content").hide();
		$(".paifa_handle_content").toggle();
		$("html, body").animate({
			scrollTop: $("#container960").height()
		}, 500);
		if($(".paifa_handle_content").css("display")=="block"){
			$(".show_handle_content").val("派发任务 ▲");
		}else{
			$(".show_handle_content").val("派发任务 ▼");
		}
	})
	/* 选择人员 点开关闭 */
	$(".select_peo").click(function(){
		$(".alert_select").show();
	})
	$(".alert_select_hide").click(function(){
		$(".alert_select").hide();
	})
	
	/* 关闭handle_content */
	$(".close_handle_content").click(function(){
		$(".handle_content").hide();
	})
})	
		