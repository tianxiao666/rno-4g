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
	$("#userGroups_functionTree").treeview({
		collapsed: true,  
		animated: "fast", 
	});
	$("#userGroups_functionTree li span").each(function(){
		$(this).click(function(){
			$("#userGroups_functionTree li span").removeClass("on_selected");
			$(this).addClass("on_selected");
		})
	});
	
	//组织角色-角色授权树
	//功能类资源树
	$("#orgRole_functionTree").treeview({
		collapsed: true,  
		animated: "fast", 
	});
	$("#orgRole_functionTree li span").each(function(){
		$(this).click(function(){
			$("#orgRole_functionTree li span").removeClass("on_selected");
			$(this).addClass("on_selected");
		})
	});
	//页面类资源树
	$("#orgRole_pageTree").treeview({
		collapsed: true,  
		animated: "fast", 
	});
	$("#orgRole_pageTree li span").each(function(){
		$(this).click(function(){
			$("#orgRole_pageTree li span").removeClass("on_selected");
			$(this).addClass("on_selected");
		})
	});
	//流程类资源树
	$("#orgRole_processTree").treeview({
		collapsed: true,  
		animated: "fast", 
	});
	$("#orgRole_processTree li span").each(function(){
		$(this).click(function(){
			$("#orgRole_processTree li span").removeClass("on_selected");
			$(this).addClass("on_selected");
		})
	});
	
	//业务角色-角色授权树
	//功能类资源树
	$("#businessRole_functionTree").treeview({
		collapsed: true,  
		animated: "fast", 
	});
	$("#businessRole_functionTree li span").each(function(){
		$(this).click(function(){
			$("#businessRole_functionTree li span").removeClass("on_selected");
			$(this).addClass("on_selected");
		})
	});
	//页面类资源树
	$("#businessRole_pageTree").treeview({
		collapsed: true,  
		animated: "fast", 
	});
	$("#businessRole_pageTree li span").each(function(){
		$(this).click(function(){
			$("#businessRole_pageTree li span").removeClass("on_selected");
			$(this).addClass("on_selected");
		})
	});
	//流程类资源树
	$("#businessRole_processTree").treeview({
		collapsed: true,  
		animated: "fast", 
	});
	$("#businessRole_processTree li span").each(function(){
		$(this).click(function(){
			$("#businessRole_processTree li span").removeClass("on_selected");
			$(this).addClass("on_selected");
		})
	});
	
	//组织架构树
	$("#OrgStructureTree").treeview({
		collapsed: true,  
		animated: "fast", 
	});
	$("#OrgStructureTree li span").each(function(){
		$(this).click(function(){
			$("#OrgStructureTree li span").removeClass("on_selected");
			$(this).addClass("on_selected");
		})
	});
});