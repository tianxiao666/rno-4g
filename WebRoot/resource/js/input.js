/*input样式*/
$(function(){
	$(":button,:submit").mousedown(function(){
			$(this).addClass("input_button_down");
		});
		$(":button,:submit").mouseup(function(){
			$(this).removeClass("input_button_down");
		});
		

})
		
		