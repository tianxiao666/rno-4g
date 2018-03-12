$(function(){

        /*左边树*/
		$(".tree_menu_info").tree({
			expanded: "false"
		});
		$(".module_up_down").toggle(function(){
					var $self = $(this);
					$self.prev().slideToggle(100,function(){
						$("img",$self).attr("src","images/up.gif");
						//$(".global_module h3").addClass("selected_h3");
					});
			 },function(){
					var $self = $(this);
					$self.prev().slideToggle(100,function(){
						$("img",$self).attr("src","images/down.gif");
						//$(".global_module h3").removeClass("selected_h3");
					});
		});
		
	   /*tab切换*/
	    $(".tab_menu ul li").each(function(index){
			$(this).click(function(){
				$(".tab_menu ul li").removeClass("ontab");
				$(this).addClass("ontab");
				$(".tab_content").hide();
				$(".tab_content").eq(index).show();
				})
		});
		
		/*显示隐藏左边框*/
		$(".menu_title_tool").click(function(){
			$(".statements_left").toggle();
			if($(this).attr("class") == "menu_title_tool_selected"){
					$(this).attr("class","menu_title_tool");
					}else{
						$(this).attr("class","menu_title_tool_selected");
						}
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
		
		/*显示/隐藏区域*/
		$(".statements_tree_button").click(function(){
			$(".statements_tree").slideToggle("fast");
		});
		$("#tree").treeview({
				collapsed: false,
				animated: "medium",
				control:"#sidetreecontrol",
				persist: "location"
		});
		$("#tree2").treeview({
				collapsed: false,
				animated: "medium",
				control:"#sidetreecontrol",
				persist: "location"
		});
		
    })