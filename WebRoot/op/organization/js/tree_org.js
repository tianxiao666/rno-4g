$(document).ready(function(){
	// 服务商组织树
	$("#serviceTree").treeview({
		collapsed: false,
		animated: "fast"
	});
	$("#serviceTree li span").each(function(){
		$(this).click(function(){
			$("#serviceTree li span").removeClass("on_selected");
			$(this).addClass("on_selected");
		})
	});
	
	// 客户组织树
	$("#clientTree").treeview({
		collapsed: true,
		animated: "fast"
	});
	$("#clientTree li span").each(function(){
		$(this).click(function(){
			$("#clientTree li span").removeClass("on_selected");
			$(this).addClass("on_selected");
		})
	});
	// 物资调度树
	$("#materialsTree").treeview({
		collapsed: true,
		animated: "fast"
	});
	$("#materialsTree li span").each(function(){
		$(this).click(function(){
			$("#materialsTree li span").removeClass("on_selected");
			$(this).addClass("on_selected");
		})
	});
	
	// 服务商网络资源树
	$("#serviceNetworkTree").treeview({
		collapsed: true,
		animated: "fast"
	});
	$("#serviceNetworkTree li span").each(function(){
		$(this).click(function(){
			$("#serviceNetworkTree li span").removeClass("on_selected");
			$(this).addClass("on_selected");
		})
	});
	
	// 客户网络资源树
	$("#clientNetworkTree").treeview({
		collapsed: true,
		animated: "fast"
	});
	$("#clientNetworkTree li span").each(function(){
		$(this).click(function(){
			$("#clientNetworkTree li span").removeClass("on_selected");
			$(this).addClass("on_selected");
		})
	});
});