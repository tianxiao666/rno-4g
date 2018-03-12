$(document).ready(function(){
	
	//页面类受控资源树
	$("#pageTree").treeview({
		collapsed: false,   // true为默认折叠 false为默认展开
		animated: "fast",  //动画---快
	});
	$("#pageTree li span").each(function(){
		$(this).click(function(){
			$("#pageTree li span").removeClass("on_selected");
			$(this).addClass("on_selected");
		})
	});
	
	//功能类受控资源树
	$("#functionTree").treeview({
		collapsed: true, 
		animated: "fast", 
	});
	$("#functionTree li span").each(function(){
		$(this).click(function(){
			$("#functionTree li span").removeClass("on_selected");
			$(this).addClass("on_selected");
		})
	});
	
	//流程类受控资源树
	$("#processTree").treeview({
		collapsed: true,  
		animated: "fast", 
	});
	$("#processTree li span").each(function(){
		$(this).click(function(){
			$("#processTree li span").removeClass("on_selected");
			$(this).addClass("on_selected");
		})
	});
	
	
	//角色群-角色授权树
	//功能类资源树
	$("#usergrouprole_function_tree").treeview({
		collapsed: true,  
		animated: "fast", 
	});
	$("#usergrouprole_function_tree li span").each(function(){
		$(this).click(function(){
			$("#usergrouprole_function_tree li span").removeClass("on_selected");
			$(this).addClass("on_selected");
		})
	});
	
	//组织角色-角色授权树
	//功能类资源树
	$("#orgrole_function_tree").treeview({
		collapsed: true,  
		animated: "fast", 
	});
	$("#orgrole_function_tree li span").each(function(){
		$(this).click(function(){
			$("#orgrole_function_tree li span").removeClass("on_selected");
			$(this).addClass("on_selected");
		})
	});
	//页面类资源树
	$("#orgrole_page_tree").treeview({
		collapsed: true,  
		animated: "fast", 
	});
	$("#orgrole_page_tree li span").each(function(){
		$(this).click(function(){
			$("#orgrole_page_tree li span").removeClass("on_selected");
			$(this).addClass("on_selected");
		})
	});
	//流程类资源树
	$("#orgrole_process_tree").treeview({
		collapsed: true,  
		animated: "fast", 
	});
	$("#orgrole_process_tree li span").each(function(){
		$(this).click(function(){
			$("#orgrole_process_tree li span").removeClass("on_selected");
			$(this).addClass("on_selected");
		})
	});
	
	//业务角色-角色授权树
	//功能类资源树
	$("#businessrole_function_tree").treeview({
		collapsed: true,  
		animated: "fast", 
	});
	$("#businessrole_function_tree li span").each(function(){
		$(this).click(function(){
			$("#businessrole_function_tree li span").removeClass("on_selected");
			$(this).addClass("on_selected");
		})
	});
	//页面类资源树
	$("#businessrole_page_tree").treeview({
		collapsed: true,  
		animated: "fast", 
	});
	$("#businessrole_page_tree li span").each(function(){
		$(this).click(function(){
			$("#businessrole_page_tree li span").removeClass("on_selected");
			$(this).addClass("on_selected");
		})
	});
	//流程类资源树
	$("#businessrole_process_tree").treeview({
		collapsed: true,  
		animated: "fast", 
	});
	$("#businessrole_process_tree li span").each(function(){
		$(this).click(function(){
			$("#businessrole_process_tree li span").removeClass("on_selected");
			$(this).addClass("on_selected");
		})
	});
});