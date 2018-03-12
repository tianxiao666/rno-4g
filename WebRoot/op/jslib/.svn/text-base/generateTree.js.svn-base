/**
 * 生成全部区域树(CHECKBOX)
 */
function createAllAreaTreeWithCheckBox(data,divId,treeId,tableId,functionName,checkBoxName) {
	var listViewDom = $("#"+divId);
	var ul = $("<ul id=\""+treeId+"\" class=\"filetree architecture_tree\"></ul>");
	if(!data){
		//清空数据
		listViewDom.html("");
		ul.appendTo(listViewDom);
		return false;
	}
	foreachCreateAllAreaTreeWithCheckBox(data,ul,tableId,functionName,checkBoxName);
	//清空数据
	listViewDom.html("");
	ul.appendTo(listViewDom);
	//treeview事件
	$("#"+treeId+"").treeview({
		collapsed: false,
		animated: "fast",
		control:"#sidetreecontrol"
	});
}
/**
 * 生成区域树
 */
function foreachCreateAllAreaTreeWithCheckBox(data,ul,tableId,functionName,checkBoxName) {
	var reg=new RegExp(" ","g"); //创建正则RegExp对象     yuan.yw add 
	for(var i=0;i<data.length;i++){
		//循环生成
		var data2 = data[i];
		var dataStr = ObjectToStr(data2);
		dataStr=dataStr.replace(reg,"&nbsp;");
		//判断有无子级菜单
		var childTree = data2.children;
		if(childTree == undefined || childTree.length==0){
			var li = $("<li><input type='checkbox' style='width:20px' name='"+checkBoxName+"' value='"+data2.areaId+"' /><span class='folder' onclick="+functionName+"('"+dataStr+"','"+tableId+"',this);changeColor(this)>"+data2.name+"</span></li>");
			li.appendTo(ul);
		}else{
			var li = $("<li class='closed'><input style='width:20px' type='checkbox' name='"+checkBoxName+"' value='"+data2.areaId+"' /><span class='folder' onclick="+functionName+"('"+dataStr+"','"+tableId+"',this);changeColor(this)>"+data2.name+"</span></li>");
			li.appendTo(ul);
			var ul2 = $("<ul></ul>");
			foreachCreateAllAreaTreeWithCheckBox(childTree,ul2,tableId,functionName,checkBoxName);
			ul2.appendTo(li);
		}
	}
}

/**
 * 根据账号生成账号所属最高组织为根节点的树 yuan.yw
 */
function newCreateOrgTree(data,divId,treeId,tableId,functionName) {
	var listViewDom = $("#"+divId);
	var ul = $("<ul id=\""+treeId+"\" class=\"filetree architecture_tree\"></ul>");
	if(!data){
		//清空数据
		listViewDom.html("");
		ul.appendTo(listViewDom);
		return false;
	}
	newForeachCreateOrgTree(data,ul,tableId,functionName);
	//清空数据
	listViewDom.html("");
	ul.appendTo(listViewDom);
	//treeview事件
	$("#"+treeId+"").treeview({
		collapsed: false,
		animated: "fast",
		control:"#sidetreecontrol"
	});
}


/**
 * 递归生成组织树  yuan.yw
 */
function newForeachCreateOrgTree(data,ul,tableId,functionName) {
	var reg=new RegExp(" ","g"); //创建正则RegExp对象     yuan.yw add 
	for(var i=0;i<data.length;i++){
		//循环生成
		var data2 = data[i];
		var dataStr = ObjectToStr(data2);
		dataStr=dataStr.replace(reg,"&nbsp;");
		//判断有无子级菜单
		var childTree = data2.children;
		if(childTree == undefined || childTree.length==0){
			var li = $("<li><span class='folder' onclick="+functionName+"('"+dataStr+"','"+tableId+"');changeColor(this)>"+data2.name+"</span></li>");
			li.appendTo(ul);
		}else{
			var li = $("<li class='closed'><span class='folder' onclick="+functionName+"('"+dataStr+"','"+tableId+"');changeColor(this)>"+data2.name+"</span></li>");
			li.appendTo(ul);
			var ul2 = $("<ul></ul>");
			newForeachCreateOrgTree(childTree,ul2,tableId,functionName);
			ul2.appendTo(li);
		}
	}
}


/**
 * 根据账号生成账号所属最高组织为根节点的树
 */
/*function createOrgTree(data,divId,treeId,tableId,functionName) {
	var listViewDom = $("#"+divId);
	var ul = $("<ul id=\""+treeId+"\" class=\"filetree architecture_tree\"></ul>");
	if(!data){
		//清空数据
		listViewDom.html("");
		ul.appendTo(listViewDom);
		return false;
	}
	foreachCreateOrgTree(data,ul,tableId,functionName);
	//清空数据
	listViewDom.html("");
	ul.appendTo(listViewDom);
	//treeview事件
	$("#"+treeId+"").treeview({
		collapsed: false,
		animated: "fast",
		control:"#sidetreecontrol"
	});
}*/
/**
 * 生成全部区域树
 */
function createAllAreaTree(data,divId,treeId,tableId,functionName) {
	var listViewDom = $("#"+divId);
	var ul = $("<ul id=\""+treeId+"\" class=\"filetree architecture_tree\"></ul>");
	if(!data){
		//清空数据
		listViewDom.html("");
		ul.appendTo(listViewDom);
		return false;
	}
	foreachCreateAllAreaTree(data,ul,tableId,functionName);
	//清空数据
	listViewDom.html("");
	ul.appendTo(listViewDom);
	//treeview事件
	$("#"+treeId+"").treeview({
		collapsed: false,
		animated: "fast",
		control:"#sidetreecontrol"
	});
}
/**
 * 递归生成区域树
 */
function foreachCreateAllAreaTree(data,ul,tableId,functionName) {
	var reg=new RegExp(" ","g"); //创建正则RegExp对象     yuan.yw add 
	for(var i=0;i<data.length;i++){
		//循环生成
		var data2 = data[i];
		var dataStr = ObjectToStr(data2);
		dataStr=dataStr.replace(reg,"&nbsp;");
		//判断有无子级菜单
		var childTree = data2.children;
		if(childTree == undefined || childTree.length==0){
			var li = $("<li><span class='folder' onclick="+functionName+"('"+dataStr+"','"+tableId+"');changeColor(this)>"+data2.name+"</span></li>");
			li.appendTo(ul);
		}else{
			var li = $("<li class='closed'><span class='folder' onclick="+functionName+"('"+dataStr+"','"+tableId+"');changeColor(this)>"+data2.name+"</span></li>");
			li.appendTo(ul);
			var ul2 = $("<ul></ul>");
			foreachCreateAllAreaTree(childTree,ul2,tableId,functionName);
			ul2.appendTo(li);
		}
	}
}
/**
 * 递归生成组织树
 */
/*function foreachCreateOrgTree(data,ul,tableId,functionName) {
	var reg=new RegExp(" ","g"); //创建正则RegExp对象     yuan.yw add 
	for(var i=0;i<data.length;i++){
		//循环生成
		var data2 = data[i];
		var dataStr = ObjectToStr(data2);
		dataStr=dataStr.replace(reg,"&nbsp;");
		//判断有无子级菜单
		var childTree = data2.childTree;
		if(childTree == undefined || childTree.length==0){
			var li = $("<li><span class='folder' onclick="+functionName+"('"+dataStr+"','"+tableId+"');changeColor(this)>"+data2.orgName+"</span></li>");
			li.appendTo(ul);
		}else{
			var li = $("<li class='closed'><span class='folder' onclick="+functionName+"('"+dataStr+"','"+tableId+"');changeColor(this)>"+data2.orgName+"</span></li>");
			li.appendTo(ul);
			var ul2 = $("<ul></ul>");
			foreachCreateOrgTree(childTree,ul2,tableId,functionName);
			ul2.appendTo(li);
		}
	}
}*/

/**
 * 根据账号生成账号所属最高组织为根节点的树(默认打开第一层树)
 */
function createOrgTreeOpenFirstNode(data,divId,treeId,tableId,functionName) {
	newCreateOrgTree(data,divId,treeId,tableId,functionName);
	$("#"+treeId+">li>div").click();
}

/**
 * 递归生成组织树(默认打开第一层树)
 */
/*function foreachCreateOrgTreeOpenFirstNode(data,ul,tableId,functionName) {
	var reg=new RegExp(" ","g"); //创建正则RegExp对象     yuan.yw add
	for(var i=0;i<data.length;i++){
		//循环生成
		var data2 = data[i];
		var dataStr = ObjectToStr(data2);
		dataStr=dataStr.replace(reg,"&nbsp;");
		//判断有无子级菜单
		var childTree = data2.childTree;
		if(childTree == undefined || childTree.length==0){
			var li = $("<li><span class='folder' onclick="+functionName+"('"+dataStr+"','"+tableId+"');changeColor(this)>"+data2.orgName+"</span></li>");
			li.appendTo(ul);
		}else{
			var li = "";
			if(i==0){
				li = $("<li><span class='folder' onclick="+functionName+"('"+dataStr+"','"+tableId+"');changeColor(this)>"+data2.orgName+"</span></li>");
			}else{
				li = $("<li class='closed'><span class='folder' onclick="+functionName+"('"+dataStr+"','"+tableId+"');changeColor(this)>"+data2.orgName+"</span></li>");
			}
			li.appendTo(ul);
			var ul2 = $("<ul></ul>");
			foreachCreateOrgTree(childTree,ul2,tableId,functionName);
			ul2.appendTo(li);
		}
	}
}*/

/**
 * 生成带复选框的树
 */
function createCheckBoxTree(data,divId,treeId,tableId,functionName,checkBoxName,checkBoxFunctionName) {
	var listViewDom = $("#"+divId);
	var ul = $("<ul id=\""+treeId+"\" class=\"filetree architecture_tree\"></ul>");
	if(!data){
		//清空数据
		listViewDom.html("");
		ul.appendTo(listViewDom);
		return false;
	}
	foreachCreateCheckBoxTree(data,ul,tableId,functionName,checkBoxName,checkBoxFunctionName);
	//清空数据
	listViewDom.html("");
	ul.appendTo(listViewDom);
	//treeview事件
	$("#"+treeId+"").treeview({
		collapsed: false,
		animated: "fast",
		control:"#sidetreecontrol"
	});
}


/**
 * 生成带复选框的树(项目信息管理)
 */
function createCheckBoxTreeInformationmanage(data,divId,treeId,tableId,functionName,checkBoxName,checkBoxFunctionName) {
	var listViewDom = $("#"+divId);
	var ul = $("<ul id=\""+treeId+"\" class=\"filetree architecture_tree\"></ul>");
	if(!data){
		//清空数据
		listViewDom.html("");
		ul.appendTo(listViewDom);
		return false;
	}
	foreachCreateCheckBoxTreeInformationmanage(data,ul,tableId,functionName,checkBoxName,checkBoxFunctionName);
	//清空数据
	listViewDom.html("");
	ul.appendTo(listViewDom);
	//treeview事件
	$("#"+treeId+"").treeview({
		collapsed: false,
		animated: "fast",
		control:"#sidetreecontrol"
	});
}

/**
 * 递归生成带复选框的树
 */
function foreachCreateCheckBoxTree(data,ul,tableId,functionName,checkBoxName,checkBoxFunctionName){
	var reg=new RegExp(" ","g"); //创建正则RegExp对象     yuan.yw add
	for(var i=0;i<data.length;i++){
		//循环生成
		var data2 = data[i];
		var dataStr = ObjectToStr(data2);
		dataStr=dataStr.replace(reg,"&nbsp;");
		//判断有无子级菜单
		if(data2.childTree==undefined && data2.orgId==undefined ){//区域
			var childTree = data2.children;
			if(childTree == undefined || childTree.length==0){
				var li = $("<li id='"+data2.areaId+"'><input  name=\"ckOrg\" type=\"checkbox\" value=\""+data2.areaId+"\" onclick=\"checkBoxTreeCheckClickEvent(this,'"+checkBoxName+data2.areaId+"','"+checkBoxName+"')\"  /><span class='folder' onclick="+functionName+"('"+dataStr+"','"+tableId+"');changeColor(this)>"+data2.name+"</span></li>");
				li.appendTo(ul);
			}else{
				var li = $("<li id='"+data2.areaId+"' ><input  name=\"ckOrg\"  type=\"checkbox\" value=\""+data2.areaId+"\" onclick=\"checkBoxTreeCheckClickEvent(this,'"+checkBoxName+data2.areaId+"','"+checkBoxName+"')\"  /><span class='folder' onclick="+functionName+"('"+dataStr+"','"+tableId+"');changeColor(this)>"+data2.name+"</span></li>");
				li.appendTo(ul);
				var ul2 = $("<ul></ul>");
				foreachCreateCheckBoxTree(childTree,ul2,tableId,functionName,checkBoxName,checkBoxFunctionName);
				ul2.appendTo(li);
			}
			
		}else{//组织
			var childTree = data2.children;
			if(childTree == undefined || childTree.length==0){
				var li = $("<li id='"+checkBoxName+data2.orgId+"'><input  name=\"ckOrg\" checked type=\"checkbox\" value=\""+data2.orgId+"\" onclick=\"checkBoxTreeCheckClickEvent(this,'"+checkBoxName+data2.orgId+"','"+checkBoxName+"')\"  /><span class='folder' onclick="+functionName+"('"+dataStr+"','"+tableId+"');changeColor(this)>"+data2.name+"</span></li>");
				li.appendTo(ul);
			}else{
				var li = $("<li id='"+checkBoxName+data2.orgId+"' class='closed'><input  name=\"ckOrg\" checked type=\"checkbox\" value=\""+data2.orgId+"\" onclick=\"checkBoxTreeCheckClickEvent(this,'"+checkBoxName+data2.orgId+"','"+checkBoxName+"')\"  /><span class='folder' onclick="+functionName+"('"+dataStr+"','"+tableId+"');changeColor(this)>"+data2.name+"</span></li>");
				li.appendTo(ul);
				var ul2 = $("<ul></ul>");
				foreachCreateCheckBoxTree(childTree,ul2,tableId,functionName,checkBoxName,checkBoxFunctionName);
				ul2.appendTo(li);
			}
		}
		
		
		
		
	
	}
}


/**
 * 递归生成带复选框的树(项目信息管理)
 */
function foreachCreateCheckBoxTreeInformationmanage(data,ul,tableId,functionName,checkBoxName,checkBoxFunctionName){
	var reg=new RegExp(" ","g"); //创建正则RegExp对象     yuan.yw add
	for(var i=0;i<data.length;i++){
		//循环生成
		var data2 = data[i];
		var dataStr = ObjectToStr(data2);
		dataStr=dataStr.replace(reg,"&nbsp;");
		//判断有无子级菜单
		if(data2.childTree==undefined && data2.orgId==undefined ){//区域
			var childTree = data2.children;
			var isChecked = "";
			if(data2.checked == true){
				isChecked = "checked=\"checked\" disabled=\"disabled\" stat=\"cd\" ";
			}else{
				isChecked = "";
			}
			if(childTree == undefined || childTree.length==0){
				var li = $("<li id='"+data2.areaId+"'><input  name=\"ckOrg\" "+isChecked+" type=\"checkbox\" value=\""+data2.areaId+"\" onclick=\"checkBoxTreeCheckClickEvent(this,'"+checkBoxName+data2.areaId+"','"+checkBoxName+"')\"  /><span class='folder' onclick="+functionName+"('"+dataStr+"','"+tableId+"');changeColor(this)>"+data2.name+"</span></li>");
				li.appendTo(ul);
			}else{
				var li = $("<li id='"+data2.areaId+"' ><input  name=\"ckOrg\" "+isChecked+" type=\"checkbox\" value=\""+data2.areaId+"\" onclick=\"checkBoxTreeCheckClickEvent(this,'"+checkBoxName+data2.areaId+"','"+checkBoxName+"')\"  /><span class='folder' onclick="+functionName+"('"+dataStr+"','"+tableId+"');changeColor(this)>"+data2.name+"</span></li>");
				li.appendTo(ul);
				var ul2 = $("<ul></ul>");
				foreachCreateCheckBoxTreeInformationmanage(childTree,ul2,tableId,functionName,checkBoxName,checkBoxFunctionName);
				ul2.appendTo(li);
			}
			
		}else{//组织
			var childTree = data2.children;
			if(childTree == undefined || childTree.length==0){
				var li = $("<li id='"+checkBoxName+data2.orgId+"'><input  name=\"ckOrg\" checked type=\"checkbox\" value=\""+data2.orgId+"\" onclick=\"checkBoxTreeCheckClickEvent(this,'"+checkBoxName+data2.orgId+"','"+checkBoxName+"')\"  /><span class='folder' onclick="+functionName+"('"+dataStr+"','"+tableId+"');changeColor(this)>"+data2.name+"</span></li>");
				li.appendTo(ul);
			}else{
				var li = $("<li id='"+checkBoxName+data2.orgId+"' class='closed'><input  name=\"ckOrg\" checked type=\"checkbox\" value=\""+data2.orgId+"\" onclick=\"checkBoxTreeCheckClickEvent(this,'"+checkBoxName+data2.orgId+"','"+checkBoxName+"')\"  /><span class='folder' onclick="+functionName+"('"+dataStr+"','"+tableId+"');changeColor(this)>"+data2.name+"</span></li>");
				li.appendTo(ul);
				var ul2 = $("<ul></ul>");
				foreachCreateCheckBoxTreeInformationmanage(childTree,ul2,tableId,functionName,checkBoxName,checkBoxFunctionName);
				ul2.appendTo(li);
			}
		}
		
		
		
		
	
	}
}

/**
 * 复选框的控制
 */
function checkBoxTreeCheckClickEvent(ckObj,id,ckName){
	if(ckObj.checked){
		$("#"+id+" input[type='checkbox']").attr("checked","checked");
	}else{
		$("#"+id+" input[type='checkbox']").removeAttr("checked");
	}
}

/**
 * 根据工单号生成工单流转过程树
 */
function createWorkOrderProcedureTree(data,divId,treeId,tableId,functionName) {
	var listViewDom = $("#"+divId);
	var ul = $("<ul id=\""+treeId+"\" class=\"filetree architecture_tree\"></ul>");
	if(!data){
		//清空数据
		listViewDom.html("");
		ul.appendTo(listViewDom);
		return false;
	}
	foreachCreateWorkOrderProcedureTree(data,ul,tableId,functionName);
	//清空数据
	listViewDom.html("");
	ul.appendTo(listViewDom);
	//treeview事件
	$("#"+treeId+"").treeview({
		collapsed: false,
		animated: "fast"
	});
}

/**
 * 递归生成工单流转过程树
 */
function foreachCreateWorkOrderProcedureTree(data,ul,tableId,functionName) {
	for(var i=0;i<data.length;i++){
		//循环生成
		var data2 = data[i];
		var dataStr = ObjectToStr(data2);
		//判断有无子级菜单
		var childTree = data2.childTree;
		if(data2.type=="抢修工单"){
			if(childTree == undefined || childTree.length==0){
				var li = $("<li style='line-height:14px'><span class='folder' onclick="+functionName+"('"+dataStr+"','"+tableId+"');changeColor(this)>受理工单</span></li>");
				li.appendTo(ul);
			}else{
				var li = $("<li class='closed' style='line-height:14px'><span class='folder' onclick="+functionName+"('"+dataStr+"','"+tableId+"');changeColor(this)>受理工单</span></li>");
				li.appendTo(ul);
				var ul2 = $("<ul></ul>");
				foreachCreateWorkOrderProcedureTree(childTree,ul2,tableId,functionName);
				ul2.appendTo(li);
			}
			
		}else{
			if(childTree == undefined || childTree.length==0){
				var li = $("<li style='line-height:14px'><span class='folder' onclick="+functionName+"('"+dataStr+"','"+tableId+"');changeColor(this)>"+data2.type+":"+data2.orderId+"</span></li>");
				li.appendTo(ul);
			}else{
				var li = $("<li class='closed' style='line-height:14px'><span class='folder' onclick="+functionName+"('"+dataStr+"','"+tableId+"');changeColor(this)>"+data2.type+":"+data2.orderId+"</span></li>");
				li.appendTo(ul);
				var ul2 = $("<ul></ul>");
				foreachCreateWorkOrderProcedureTree(childTree,ul2,tableId,functionName);
				ul2.appendTo(li);
			}
		}
		
	}
}

/**
 * 权限树
 */
function createPermissionTree(data,divId,treeId,tableId,functionName) {
	var listViewDom = $("#"+divId);
	var ul = $("<ul id=\""+treeId+"\" class=\"filetree architecture_tree\"></ul>");
	if(!data){
		//清空数据
		listViewDom.html("");
		ul.appendTo(listViewDom);
		return false;
	}
	foreachInitPermissionTree(data,ul,tableId,functionName);
	//清空数据
	listViewDom.html("");
	ul.appendTo(listViewDom);
	//treeview事件
	$("#"+treeId+"").treeview({
		collapsed: true,
		animated: "fast",
		control:"#sidetreecontrol"
	});
}

/**
 * 递归生成权限树
 */
function foreachInitPermissionTree(data,ul,tableId,functionName) {
	//var reg=new RegExp(" ","g"); //创建正则RegExp对象     yuan.yw add 
	for(var i=0;i<data.length;i++){
		//循环生成
		var data2 = data[i];
		var dataStr = ObjectToStr(data2.permission);
		//dataStr=dataStr.replace(reg,"&nbsp;");
		//判断有无子级菜单
		var childTree = data2.subPermission;
		if(childTree == undefined || childTree.length==0){
			var li = $("<li><span class='folder' onclick="+functionName+"('"+dataStr+"','"+tableId+"');changeColor(this)>"+data2.permission.TYPENAME+":"+data2.permission.NAME+"</span></li>");
			li.appendTo(ul);
		}else{
			var li = $("<li class='closed'><span class='folder' onclick="+functionName+"('"+dataStr+"','"+tableId+"');changeColor(this)>"+data2.permission.TYPENAME+":"+data2.permission.NAME+"</span></li>");
			li.appendTo(ul);
			var ul2 = $("<ul></ul>");
			foreachInitPermissionTree(childTree,ul2,tableId,functionName);
			ul2.appendTo(li);
		}
	}
}

/**
 * 权限树
 */
function createPermissionTreeByRole(data,divId,treeId,tableId,functionName) {
	var listViewDom = $("#"+divId);
	var ul = $("<ul id=\""+treeId+"\" class=\"filetree architecture_tree\"></ul>");
	if(!data){
		//清空数据
		listViewDom.html("");
		ul.appendTo(listViewDom);
		return false;
	}
	foreachInitPermissionByRoleTree(data,ul,tableId,functionName);
	//清空数据
	listViewDom.html("");
	ul.appendTo(listViewDom);
	//treeview事件
	$("#"+treeId+"").treeview({
		collapsed: false,
		animated: "fast",
		control:"#sidetreecontrol"
	});
}

/**
 * 递归生成权限树
 */
function foreachInitPermissionByRoleTree(data,ul,tableId,functionName) {
	//var reg=new RegExp(" ","g"); //创建正则RegExp对象     yuan.yw add 
	for(var i=0;i<data.length;i++){
		//循环生成
		var data2 = data[i];
		var dataStr = ObjectToStr(data2.permission);
		//dataStr=dataStr.replace(reg,"&nbsp;");
		//判断有无子级菜单
		var childTree = data2.subPermission;
		var auth;
		var perAccessText = "";
		if(data2.permission.PER_ACCESS != null && data2.permission.PER_ACCESS.indexOf('read') >= 0){
			perAccessText = perAccessText + "查看/";
		}
		if(data2.permission.PER_ACCESS != null && data2.permission.PER_ACCESS.indexOf('write') >= 0){
			perAccessText = perAccessText + "编辑/";
		}
		if(perAccessText != null && perAccessText != ""){
			perAccessText = perAccessText.substring(0,perAccessText.length-1);
			auth="(<em style='color:red'>"+perAccessText+"</em>)";
		}else{
			auth="";
		}
		if(childTree == undefined || childTree.length==0){
			var li = $("<li><span class='folder' per='"+data2.permission.PER_ACCESS+"' onclick="+functionName+"('"+dataStr+"','"+tableId+"',this);changeColor(this)>"+data2.permission.TYPENAME+":"+data2.permission.NAME+auth+"</span></li>");
			li.appendTo(ul);
		}else{
			var li = $("<li class='closed'><span class='folder' per='"+data2.permission.PER_ACCESS+"' onclick="+functionName+"('"+dataStr+"','"+tableId+"',this);changeColor(this)>"+data2.permission.TYPENAME+":"+data2.permission.NAME+auth+"</span></li>");
			li.appendTo(ul);
			var ul2 = $("<ul></ul>");
			foreachInitPermissionByRoleTree(childTree,ul2,tableId,functionName);
			ul2.appendTo(li);
		}
	}
}

//改变颜色
function changeColor(me){
	$(".filetree li span").removeClass("on_selected");
	$(me).addClass("on_selected");
}

//************************************** 节点触发方法 *****************************************
//获取队长
function showTeamLeader(dataStr,tableId){
var data = eval("(" + dataStr + ")");
var values = {
			"ORGID" : data.orgId
		}
var myUrl = "loadTeamLeadersInfoAction";	
$.ajax({
		url : myUrl,
		data : values,
		async:false,
		dataType : 'json',
		type : 'POST',
		success : function(result) {
		$("#"+tableId).html("");
		var dateTime = new Date();
		var m = dateTime.getMonth()+1;
		var d = dateTime.getDate();
		var h = dateTime.getHours();
		var miu = dateTime.getMinutes();
		var s = dateTime.getSeconds();
		var time = dateTime.getFullYear()+"-"+m+"-"+d+"&nbsp;"+h+":"+miu+":"+s;
		var htmlString = "<tr><th>维护队</th><th>队长</th><th>今天是否值班</th><th>忙闲情况(任务数)</th></tr>";
		if (result != null && result != "") {
			for ( var i = 0; i < result.length; i++) {
				if(result[i].dutyStatus=="是"){
					if(result[i].sum == 0){
						var htmlString =htmlString+ "<tr><td ><input type='radio' name='recipient' value='"+result[i].teamId+"-"+result[i].accountId+"'/>"+result[i].teamName+"</td><td class='tl' style='text-indent:4em'>"+result[i].teamleader+" "+result[i].phone+"</td><td class='tc'><em class='is_sb' onclick=getStaffTaskInfoForGantt('"+result[i].accountId+"',this)>"+result[i].dutyStatus+"</em></td><td ><em class='is_sb' ><a id='g"+i+"' class='showGantt_a' >"+result[i].sum+"</a></em></td></tr>";
					}else{
						var htmlString =htmlString+ "<tr><td ><input type='radio' name='recipient' value='"+result[i].teamId+"-"+result[i].accountId+"'/>"+result[i].teamName+"</td><td class='tl' style='text-indent:4em'>"+result[i].teamleader+" "+result[i].phone+"</td><td class='tc'><em class='is_sb' onclick=getStaffTaskInfoForGantt('"+result[i].accountId+"',this)>"+result[i].dutyStatus+"</em></td><td ><em class='not_sb'><a id='g"+i+"' class='showGantt_a' >"+result[i].sum+"</a></em></td></tr>";
					}
				}else{
					if(result[i].sum == 0){
						var htmlString =htmlString+ "<tr><td ><input type='radio' name='recipient' value='"+result[i].teamId+"-"+result[i].accountId+"'/>"+result[i].teamName+"</td><td class='tl' style='text-indent:4em'>"+result[i].teamleader+" "+result[i].phone+"</td> <td class='tc'><em class='not_sb' onclick=getStaffTaskInfoForGantt('"+result[i].accountId+"',this)>"+result[i].dutyStatus+"</em></td><td ><em class='is_sb'><a id='g"+i+"' class='showGantt_a' >"+result[i].sum+"</a></em></td></tr>";
					}else{
						var htmlString =htmlString+ "<tr><td ><input type='radio' name='recipient' value='"+result[i].teamId+"-"+result[i].accountId+"'/>"+result[i].teamName+"</td><td class='tl' style='text-indent:4em'>"+result[i].teamleader+" "+result[i].phone+"</td> <td class='tc'><em class='not_sb' onclick=getStaffTaskInfoForGantt('"+result[i].accountId+"',this)>"+result[i].dutyStatus+"</em></td><td ><em class='not_sb'><a id='g"+i+"' class='showGantt_a'>"+result[i].sum+"</a></em></td></tr>";
					}
				}
	
			}
			$("#"+tableId).append(htmlString);
		}else{
			$("#"+tableId).append(htmlString);
		} 
		}
		
	});
}

//获取转派维护人员
function showToSendSenceTeamerList(dataStr,tableId){
	var data = eval("(" + dataStr + ")");
	var values = {
				"ORGID" : data.orgId
			}
	var myUrl = "loadToSendTeamerInfoListAction";	
	$.ajax({
			url : myUrl,
			data : values,
			async:false,
			dataType : 'json',
			type : 'POST',
			success : function(result) {
			$("#"+tableId).html("");
			$("#curOrgId").val(data.orgId);
			var dateTime = new Date();
			var m = dateTime.getMonth()+1;
			var d = dateTime.getDate();
			var h = dateTime.getHours();
			var miu = dateTime.getMinutes();
			var s = dateTime.getSeconds();
			var time = dateTime.getFullYear()+"-"+m+"-"+d+"&nbsp;"+h+":"+miu+":"+s;
			var htmlString = "<table class='alert_table'><tr><th>人员姓名</th><th>任务数</th></tr> </table><div class='alert_table_scroll'><table class='alert_table'>";
			if (result != null && result != "") {
				for ( var i = 0; i < result.length; i++) {
					if(result[i].dutyStatus=="是"){
						var htmlString =htmlString+ "<tr><td class='tl' style='text-indent:2em'><input type='radio' name='tosend_recipient' value='"+result[i].accountId+"-"+result[i].teamer+"'/>"+result[i].teamer+" "+result[i].phone+"</td> <td class='tc'><a id='g"+i+"' class='showGantt_a' onclick=getStaffTaskInfoForGantt('"+result[i].accountId+"',this)>"+result[i].sum+"</a></td></tr>";
					}else{
						var htmlString =htmlString+ "<tr><td class='tl' style='text-indent:2em'><input type='radio' name='tosend_recipient' value='"+result[i].accountId+"-"+result[i].teamer+"'/>"+result[i].teamer+" "+result[i].phone+"</td> <td class='tc'><a id='g"+i+"' class='showGantt_a' onclick=getStaffTaskInfoForGantt('"+result[i].accountId+"',this)>"+result[i].sum+"</a></td></tr>";
					}
		
				}
				$("#"+tableId).append(htmlString);
			}else{
				$("#"+tableId).append(htmlString);
			} 
			}
			
	});
}

//获取转派调度员
function showToSendDispatcherList(dataStr,tableId){
	var data = eval("(" + dataStr + ")");
	var values = {
				"ORGID" : data.orgId
			}
	var myUrl = "loadToSendDispatcherInfoListAction";	
	$.ajax({
			url : myUrl,
			data : values,
			async:false,
			dataType : 'json',
			type : 'POST',
			success : function(result) {
			$("#"+tableId).html("");
			var dateTime = new Date();
			var m = dateTime.getMonth()+1;
			var d = dateTime.getDate();
			var h = dateTime.getHours();
			var miu = dateTime.getMinutes();
			var s = dateTime.getSeconds();
			var time = dateTime.getFullYear()+"-"+m+"-"+d+"&nbsp;"+h+":"+miu+":"+s;
			var htmlString = "<table class='alert_table'><tr><th>人员姓名</th><th>任务数</th></tr> </table><div class='alert_table_scroll'><table class='alert_table'>";
			if (result != null && result != "") {
				for ( var i = 0; i < result.length; i++) {
					if(result[i].dutyStatus=="是"){
						var htmlString =htmlString+ "<tr><td class='tl' style='text-indent:2em'><input type='radio' name='tosend_recipient' value='"+result[i].accountId+"-"+result[i].teamer+"'/>"+result[i].teamer+" "+result[i].phone+"</td> <td class='tc'><a id='g"+i+"' class='showGantt_a' onclick=getStaffTaskInfoForGantt('"+result[i].accountId+"',this)>"+result[i].sum+"</a></td></tr>";
					}else{
						var htmlString =htmlString+ "<tr><td class='tl' style='text-indent:2em'><input type='radio' name='tosend_recipient' value='"+result[i].accountId+"-"+result[i].teamer+"'/>"+result[i].teamer+" "+result[i].phone+"</td> <td class='tc'><a id='g"+i+"' class='showGantt_a' onclick=getStaffTaskInfoForGantt('"+result[i].accountId+"',this)>"+result[i].sum+"</a></td></tr>";
					}
		
				}
				$("#"+tableId).append(htmlString);
			}else{
				$("#"+tableId).append(htmlString);
			} 
			}
			
	});
}

//获取转派专家
function showToSendSpecialistList(dataStr,tableId){
	var data = eval("(" + dataStr + ")");
	var values = {
				"ORGID" : data.orgId
			}
	var myUrl = "loadToSendSpecialistInfoListAction";	
	$.ajax({
			url : myUrl,
			data : values,
			async:false,
			dataType : 'json',
			type : 'POST',
			success : function(result) {
			$("#"+tableId).html("");
			$("#curOrgId").val(data.orgId);
			var dateTime = new Date();
			var m = dateTime.getMonth()+1;
			var d = dateTime.getDate();
			var h = dateTime.getHours();
			var miu = dateTime.getMinutes();
			var s = dateTime.getSeconds();
			var time = dateTime.getFullYear()+"-"+m+"-"+d+"&nbsp;"+h+":"+miu+":"+s;
			var htmlString = "<table class='alert_table'><tr><th>人员姓名</th><th>任务数</th></tr> </table><div class='alert_table_scroll'><table class='alert_table'>";
			if (result != null && result != "") {
				for ( var i = 0; i < result.length; i++) {
					if(result[i].dutyStatus=="是"){
						var htmlString =htmlString+ "<tr><td class='tl' style='text-indent:2em'><input type='radio' name='tosend_recipient' value='"+result[i].accountId+"-"+result[i].teamer+"'/>"+result[i].teamer+" "+result[i].phone+"</td> <td class='tc'><a id='g"+i+"' class='showGantt_a' onclick=getStaffTaskInfoForGantt('"+result[i].accountId+"',this)>"+result[i].sum+"</a></td></tr>";
					}else{
						var htmlString =htmlString+ "<tr><td class='tl' style='text-indent:2em'><input type='radio' name='tosend_recipient' value='"+result[i].accountId+"-"+result[i].teamer+"'/>"+result[i].teamer+" "+result[i].phone+"</td> <td class='tc'><a id='g"+i+"' class='showGantt_a' onclick=getStaffTaskInfoForGantt('"+result[i].accountId+"',this)>"+result[i].sum+"</a></td></tr>";
					}
		
				}
				$("#"+tableId).append(htmlString);
			}else{
				$("#"+tableId).append(htmlString);
			} 
			}
			
	});
}

//展现任务流转过程
function showWorkOrderProcedure(dataStr,tableId){
	var data = eval("(" + dataStr + ")");
	if(data.type == "抢修工单"){
		workOrderProcedureSection("loadWorkOrderProcedureAction",data.orderId,tableId);
	}else if(data.type == "现场任务单"){
		taskOrderProcedureSection("loadSenceTaskOrderProcedureAction",data.orderId,tableId,"WO_SECTION");
	}else if(data.type == "专家任务单"){
		taskOrderProcedureSection("loadTechSupportTaskOrderProcedureAction",data.orderId,tableId,"WO_SECTION");
	}	
}

