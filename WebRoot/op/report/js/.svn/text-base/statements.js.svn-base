$(function(){

	   /*tab切换*/
	    $(".tab_menu ul li").each(function(index){
			$(this).click(function(){
				$(".tab_menu ul li").removeClass("ontab");
				$(this).addClass("ontab");
				$(".tab_content").hide();
				$(".tab_content").eq(index).show();
				})
		});
		/*input样式*/
		$(":button,:submit").mousedown(function(){
			$(this).addClass("input_button_down");
		});
		$(":button,:submit").mouseup(function(){
			$(this).removeClass("input_button_down");
		});
		
		/* checkbox点击隐藏 */
		$(".statements_right fieldset legend :checkbox").each(function(){
			if($(this).attr("checked")=="checked"){
				$(this).parent().parent().removeClass("fieldset-hide");
				$(this).parent().parent().find(".output_table").show();
			}
			else{
				$(this).parent().parent().addClass("fieldset-hide");
				$(this).parent().parent().find(".output_table").hide();
			}
			$(this).click(function(){
				if($(this).attr("checked")=="checked"){
					$(this).parent().parent().removeClass("fieldset-hide");
					$(this).parent().parent().find(".output_table").show();
				}
				else{
					$(this).parent().parent().addClass("fieldset-hide");
					$(this).parent().parent().find(".output_table").hide();
				}
			})
		});
		
		/*显示/隐藏报表设置*/
	    $(".show_report_settings").click(function(){
			$(".report_settings").slideToggle("fast");
		});
		
		
		
})