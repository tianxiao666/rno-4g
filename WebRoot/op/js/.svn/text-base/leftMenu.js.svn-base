$(function(){
	/*显示隐藏左边框*/
	$(".menu_title_tool").click(function(){
		$(".leftMenu").toggle();
		if($(this).attr("class") == "menu_title_tool_selected"){
				$(this).attr("class","menu_title_tool");
			}else{
				$(this).attr("class","menu_title_tool_selected");
				}
	});
	$(".module_up_down").toggle(function(){
			var $self = $(this);
			$self.prev().slideToggle(100,function(){
				$("img",$self).attr("src","../../images/down.gif");
				//$(".global_module h3").addClass("selected_h3");
			});
	 },function(){
			var $self = $(this);
			$self.prev().slideToggle(100,function(){
				$("img",$self).attr("src","../../images/up.gif");
				//$(".global_module h3").removeClass("selected_h3");
			});
	});
	
})