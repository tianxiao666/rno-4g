$(document).ready(function(){
	//
	$("#tree").treeview({
		collapsed: false,   // true为默认折叠 false为默认展开
		animated: "fast"  //动画---快
	});
	
	$("#tree li span").each(function(){
		$(this).click(function(){
			$("#tree li span").removeClass("on_selected");
			$(this).addClass("on_selected");
		})
	})
});