// JavaScript Document
$(function(){
	/*tab 可以重复使用*/
	$(".tab_content").hide().eq(0).show();
	$(".tab_menu").each(function(){
		$(this).find($("ul li")).each(function(index){
			$(this).click(function(){
				$(this).parent().parent().parent().find($(".tab_container .tab_content")).hide();
				$(this).parent().parent().parent().find($(".tab_container .tab_content")).eq(index).show();
				$(this).parent().find("li").removeClass("ontab");
				$(this).addClass("ontab");
			})
		})
	})
	$(".title_overflow").each(function(){
		$(this).attr("title",$(this).text());
	})
	// 组织树
	$("#tree_div").treeview({
		collapsed: false,
		animated: "fast"
	});
	$("#tree_div li span").each(function(){
		$(this).click(function(){
			$("#tree_div li span").removeClass("on_selected");
			$(this).addClass("on_selected");
		})
	});
})