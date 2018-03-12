var isRecordInfoClick = false;	//标识 档案信息 是否点击
var isTaskInfoClick = false;	//标识 任务信息 是否点击
var isResourceBoxClick = false;	//标识 储物箱   是否点击
var staffId  = "";
//任务查询条件
var queryTaskConditions = {};
queryTaskConditions.toTitle = "";	//任务主题
queryTaskConditions.taskStatus = "";//任务状态：未完成、已完成
queryTaskConditions.taskType = "";	//任务类型：抢修、巡检、车辆调度
queryTaskConditions.beginTime = "";	//开始时间
queryTaskConditions.endTime = "";	//截至时间
queryTaskConditions.accountId = "";

//储物箱查询条件
var queryResourceBoxConditions = {};
queryResourceBoxConditions.resourceType = "";
queryResourceBoxConditions.resourceName = "";
queryResourceBoxConditions.beginTime = "";
queryResourceBoxConditions.endTime = "";
queryResourceBoxConditions.staffId = "";

$(function(){
	staffId = $("#hiddenStaffId").val();
	queryTaskConditions.accountId = staffId;
	queryResourceBoxConditions.staffId = staffId;
	if(!staffId||staffId==""){
		alert("传入人员Id为空");
		return false;
	}
	//获取人员基本信息
	getStaffBaseInfo(staffId);
	//GIS展示人员信息
	$("#staffGisButton").click(function(){
		//TODO GIS展示人员信息
		
	});
	//GIS展示组织驻地信息
	$("#orgGisButton").click(function(){
		var orgId = $("#hiddenOrgId").val();
		//window.open("../gisdispatch/orgGisDisplayPage.jsp?orgId="+orgId);
	});
	/**档案信息 Li 点击事件**/
	$("#recordInfoLi").click(function(){
		if(isRecordInfoClick)return;
		//TODO 获取人员档案信息
		
		isRecordInfoClick = true;
	});
	/**任务信息 Li 点击事件**/
	$("#taskInfoLi").click(function(){
		if(isTaskInfoClick)return;
		//获取人员任务信息
		getStaffTaskInfo();
		isTaskInfoClick = true;
	});
	/**储物箱 Li 点击事件**/
	$("#resourceBoxLi").click(function(){
		if(isResourceBoxClick)return;
		//获取人员储物箱信息
		getStaffResourceBoxInfo();
		isResourceBoxClick = true;
	});
	//任务列表搜索按钮
	$("#taskSearchButton").click(function(){
		var taskIndex = 0;
		var taskStatus = "";
		$(".taskStatus").each(function(){
			if($(this).attr("checked") == "checked"){	
				taskStatus = $(this).val();
				taskIndex+=1;
			}
		});
		if(taskIndex>1){
			taskStatus = "";
		}
		var taskName = $("#taskName").val();
		var beginTime = $("#taskBeginTime").val();
		var endTime = $("#taskEndTime").val();
		var taskType = $("#taskType").val();
		
		if(beginTime==''||endTime==''){
			alert("请选择要搜索的任务创建时间段");
			return false;
		}
		queryTaskConditions.toTitle = taskName;
		queryTaskConditions.taskStatus = taskStatus;
		queryTaskConditions.taskType = taskType;
		queryTaskConditions.beginTime = beginTime;
		queryTaskConditions.endTime = endTime;
		showStaffTaskInfoByConditions(queryTaskConditions);
	});
	/*储物箱搜索事件*/
	$("#meSearchButton").click(function(){
		var meName = $("#meName").val();
		var beginTime = $("#meBeginTime").val();
		var endTime = $("#meEndTime").val();
		//物资json字符串
		var jsonStr = "";
		jsonStr += "[{";
		jsonStr += "'meName':'" + meName + "'";
		jsonStr += ",'meBeginTime':'" + beginTime + "'";
		jsonStr += ",'meEndTime':'" + endTime + "'}]";
		//物资类型
		var meType = $("#meType").val();
		if(meType=='all'){
			//TODO 查询全部
			$.post("showQueryAssetsAjaxAction",{materlsJson:jsonStr},function(data){
				var count = 0;
				if(data){
					count = data.length;
					showAssetsTable(data);
				}
				$("#assetsCount").html("("+count+")");
			},"json");
			$.post("showQueryReplacementPartAjaxAction",{materlsJson:jsonStr},function(data){
				var count = 0;
				if(data){
					count = data.length;
					showPartTable(data);
				}
				$("#partCount").html("("+count+")");
			},"json");
			$.post("showQueryConsumableAjaxAction",{materlsJson:jsonStr},function(data){
				var count = 0;
				if(data){
					count = data.length;
					showConsumableTable(data);
				}
				$("#consumableCount").html("("+count+")");
			},"json");
		}else if(meType=='assets'){
			$.post("showQueryAssetsAjaxAction",{materlsJson:jsonStr},function(data){
				var count = 0;
				if(data){
					count = data.length;
					showAssetsTable(data);
				}
				$("#assetsCount").html("("+count+")");
			},"json");
		}else if(meType=='part'){
			$.post("showQueryReplacementPartAjaxAction",{materlsJson:jsonStr},function(data){
				var count = 0;
				if(data){
					count = data.length;
					showPartTable(data);
				}
				$("#partCount").html("("+count+")");
			},"json");
		}else{
			//易耗品
			$.post("showQueryConsumableAjaxAction",{materlsJson:jsonStr},function(data){
				var count = 0;
				if(data){
					count = data.length;
					showConsumableTable(data);
				}
				$("#consumableCount").html("("+count+")");
			},"json");
		}
	});
});
/*获取人员储物箱信息*/
function getStaffResourceBoxInfo(){
	//showStaffResourceBoxInfoByConditions(queryResourceBoxConditions);
}
//内部方法
//检查非空判断
function checkStrIsNull(str){
	if(str==undefined){
		return "";
	}else{
		return str;
	}
}
/*条件获取人员储物箱信息*/
function showStaffResourceBoxInfoByConditions(queryResourceBoxConditions){
	$.ajax( {
		url : "getStaffResourceBoxInfoActionForAjax",
		cache : false,
		data : queryResourceBoxConditions,
		async : false,
		type : "POST",
		dataType : 'json',
		success : function(res) {
			if(res){
				showStaffResourceBoxInfo(res);
			}else{
				$("#assetsCount").html("(0)");
				$("#consumableCount").html("(0)");
				$("#partCount").html("(0)");
			}
		}
	});
}
/*生成人员储物箱信息*/
function showStaffResourceBoxInfo(data){
	//工具资产
	var assetsList = data.assetsList;
	if(assetsList){
		showAssetsTable(assetsList);
	}
	var assetsListCount = data.assetsListCount;
	$("#assetsCount").html("("+assetsListCount+")");
	//易耗品
	var consumableList = data.consumableList;
	if(consumableList){
		showConsumableTable(consumableList);
	}
	var consumableListCount = data.consumableListCount;
	$("#consumableCount").html("("+consumableListCount+")");
	//设备备件
	var partList = data.partList;
	if(partList){
		showPartTable(partList);
	}
	var partListCount = data.partListCount;
	$("#partCount").html("("+partListCount+")");
}
/*生成数据(工具资产table)*/
function showAssetsTable(data){
	$("#assetsTable").html("");
	var divStr = "";
	divStr += "<tr>";
	divStr += "<th>资产编号</th>";
	divStr += "<th>物资名</th>";
	divStr += "<th>产权属主</th>";
	divStr += "<th>品牌</th>";
	divStr += "<th>型号</th>";
	divStr += "<th>规格</th>";
	divStr += "<th>状态</th>";
	divStr += "</tr>";
	$(data).each(function(index,value){
		divStr += "<tr class='assetsResult'>";
		divStr += "<td><a href='#'>"+checkStrIsNull(value.assetsNumber)+"</a></td>";
		divStr += "<td>"+checkStrIsNull(value.meClassificationName)+"</td>";
		divStr += "<td>"+checkStrIsNull(value.propertyOwner)+"</td>";
		divStr += "<td>"+checkStrIsNull(value.brand)+"</td>";
		divStr += "<td>"+checkStrIsNull(value.type)+"</td>";
		divStr += "<td>"+checkStrIsNull(value.specification)+"</td>";
		divStr += "<td>"+checkStrIsNull(value.status)+"</td>";
		divStr += "</tr>";
	});
	$("#assetsTable").html(divStr);
	var list = $(".assetsResult");
	var pageSize="10";
	pagingColumnByForeground("assetsPagingDiv",list,pageSize);
}
/*生成数据(易耗品table)*/
function showConsumableTable(data){
	$("#consumableTable").html("");
	var divStr = "";
	divStr += "<tr>";
	divStr += "<th>易耗品编码</th>";
	divStr += "<th>物资名</th>";
	divStr += "<th>品牌</th>";
	divStr += "<th>型号</th>";
	divStr += "<th>规格</th>";
	divStr += "<th>数量</th>";
	divStr += "<th>单位</th>";
	divStr += "</tr>";
	$(data).each(function(index,value){
		divStr += "<tr class='consumableResult'>";
		divStr += "<td><a href='#'>"+checkStrIsNull(value.consumableCoding)+"</a></td>";
		divStr += "<td>"+checkStrIsNull(value.meClassificationName)+"</td>";
		divStr += "<td>"+checkStrIsNull(value.brand)+"</td>";
		divStr += "<td>"+checkStrIsNull(value.type)+"</td>";
		divStr += "<td>"+checkStrIsNull(value.specification)+"</td>";
		divStr += "<td>"+checkStrIsNull(value.count)+"</td>";
		divStr += "<td>"+checkStrIsNull(value.theMeasuringUnit)+"</td>";
		divStr += "</tr>";
	});
	$("#consumableTable").html(divStr);
	var list = $(".consumableResult");
	var pageSize="10";
	pagingColumnByForeground("consumablePagingDiv",list,pageSize);
}
/*生成数据(设备备件table)*/
function showPartTable(data){
	$("#partTable").html("");
	var divStr = "";
	divStr += "<tr>";
	divStr += "<th>备件序列号</th>";
	divStr += "<th>物资名</th>";
	divStr += "<th>条码号</th>";
	divStr += "<th>产权属主</th>";
	divStr += "<th>品牌</th>";
	divStr += "<th>型号</th>";
	divStr += "<th>规格</th>";
	divStr += "<th>状态</th>";
	divStr += "</tr>";
	$(data).each(function(index,value){
		divStr += "<tr class='replacementPartResult'>";
		divStr += "<td><a href='#'>"+checkStrIsNull(value.serialNumber)+"</a></td>";
		divStr += "<td>"+checkStrIsNull(value.meClassificationName)+"</td>";
		divStr += "<td>"+checkStrIsNull(value.barCode)+"</td>";
		divStr += "<td>"+checkStrIsNull(value.propertyOwner)+"</td>";
		divStr += "<td>"+checkStrIsNull(value.brand)+"</td>";
		divStr += "<td>"+checkStrIsNull(value.type)+"</td>";
		divStr += "<td>"+checkStrIsNull(value.specification)+"</td>";
		divStr += "<td>"+checkStrIsNull(value.status)+"</td>";
		divStr += "</tr>";
	});
	$("#partTable").html(divStr);
	var list = $(".replacementPartResult");
	var pageSize="10";
	pagingColumnByForeground("partPagingDiv",list,pageSize);
}
/*获取人员任务信息*/
function getStaffTaskInfo(){
	var taskIndex = 0;
	var taskStatus = "";
	$(".taskStatus").each(function(){
		if($(this).attr("checked") == "checked"){	
			taskStatus = $(this).val();
			taskIndex+=1;
		}
	});
	if(taskIndex>1){
		taskStatus = "";
	}
	var taskName = $("#taskName").val();
	var beginTime = $("#taskBeginTime").val();
	var endTime = $("#taskEndTime").val();
	var taskType = $("#taskType").val();
	
	queryTaskConditions.toTitle = taskName;
	queryTaskConditions.taskStatus = taskStatus;
	queryTaskConditions.taskType = taskType;
	queryTaskConditions.beginTime = beginTime;
	queryTaskConditions.endTime = endTime;
	showStaffTaskInfoByConditions(queryTaskConditions);
}
//条件获取人员任务信息
function showStaffTaskInfoByConditions(queryTaskConditions){
	$.ajax( {
		url : "getStaffTaskInfoAction",
		cache : false,
		data : queryTaskConditions,
		async : false,
		type : "POST",
		dataType : 'json',
		success : function(res) {
			if(res){
				showStaffTaskInfo(res);
			}else{
				$("#taskPagingColumnTotalPage").html("1");
				$("#taskAvgSpendTime").html("0");
				$("#taskTotalNumber").html("0");
			}
		}
	});
}
/*显示人员任务信息*/
function showStaffTaskInfo(data){
	var taskList = data;
	var table = $("#taskInfoTable");
	table.html("");
	var thHtml = "<tr><th style='width:80px;'>任务类型</th><th style='width:120px;'>任务编号</th><th>任务主题</th><th style='width:135px;'>创建时间</th><th style='width:135px;'>截止时间</th><th style='width:135px;'>完成时间</th><th style='width:80px;'>历时(小时)</th><th style='width:60px;'>是否超时</th></tr>";
	var th = $(thHtml);
	th.appendTo(table);
	//数据为空，则清空表格
	if(!data){
		return ; //参数过滤
	}
	//循环生成数据
	var totalPage = 0;
	var avgSpendTime = 0;
	var totalTaskAmount = 0;
	var overTaskNumber = 0;
	for(var i=0;i<taskList.length;i++){
		var task = taskList[i];
		if(!task)continue;
		var tr = $("<tr class='pageTr'></tr>");
		var taskType = task.bizProcessName;
		
		var TOID = task.toId?task.toId:task.woId;
		var WOID = task.woId;
		var BIZUNITINSTID = !task.assignerOrgId?task.currentHandlerOrgId:task.assignerOrgId;
		var taskName = !task.woTitle?task.toTitle:task.woTitle;
		var assignedTime = !task.assignTime?task.createTime:task.assignTime;
		var overTime = task.requireCompleteTime;
		var operateTime = task.finalCompleteTime;
		var isOver = task.isOverTime;
		if(isOver == 1){
			overTaskNumber++;
		}
		var locationHref = task.formUrl;
		locationHref = "../../"+locationHref+"?TOID="+TOID+"&WOID="+WOID+"&bizunitInstanceId="+BIZUNITINSTID;
		var spendTime = !task.takeTime?"":task.takeTime;
		if(spendTime != null && spendTime != ""){
			avgSpendTime = avgSpendTime+spendTime;
		}
		totalPage = task.totalPage;
		//avgSpendTime = task.avgSpendTime;
		totalTaskAmount = taskList.length;
		
		if(!assignedTime){assignedTime="";}
		if(!overTime){overTime="";}
		if(!operateTime){operateTime="";}
		
		taskType = $("<td>"+taskType+"</td>");
		TOID = $("<td><a href='"+locationHref+"' target='_blank'>"+TOID+"</a></td>");
		taskName = $("<td class='tl'>"+taskName+"</td>");
		assignedTime = $("<td>"+assignedTime+"</td>");
		overTime = $("<td>"+overTime+"</td>");
		operateTime = $("<td>"+operateTime+"</td>");
		spendTime = $("<td>"+spendTime+"</td>");
		if(isOver == 1){
			isOver = $("<td><em class='red'>是</em></td>");
		}else{
			isOver = $("<td>否</td>");
		}
		
		taskType.appendTo(tr);
		TOID.appendTo(tr);
		taskName.appendTo(tr);
		assignedTime.appendTo(tr);
		overTime.appendTo(tr);
		operateTime.appendTo(tr);
		spendTime.appendTo(tr);
		isOver.appendTo(tr);
		tr.appendTo(table);
	}
	if(avgSpendTime != 0){
		avgSpendTime = avgSpendTime/totalTaskAmount;
		avgSpendTime = parseFloat(avgSpendTime).toFixed(2); 
	}
	$("#taskAvgSpendTime").html(avgSpendTime);
	$("#taskPagingColumnTotalPage").html(totalPage);
	$("#taskTotalNumber").html(totalTaskAmount);
	$("#overTaskNumber").html(overTaskNumber);
	
	//分页
	pagingColumnByForeground("taskPageContent",$(".pageTr"),10);
}

/*获取人员基本信息*/
function getStaffBaseInfo(staffId){
	$.ajax( {
		url : "getStaffBaseInfoAction",
		cache : false,
		data : {accountId:staffId},
		async : false,
		type : "POST",
		dataType : 'json',
		success : function(res) {
			if(res){
				showStaffBaseInfo(res);
			}
		}
	});
}
/*显示人员基本信息(生成数据)*/
function showStaffBaseInfo(data){
	var itAccount = data.account;
	var staffName = data.name;
	var staffSex = data.sex;
	var birthday = data.birthday;
	var email = !data.email?"":data.email;
	var staffType = data.typeName;
	var cellPhoneNumber = data.cellPhoneNumber;
	var address = data.address;
	//人员经纬度
	var curLat = !data.latitude?"":data.latitude;
	var curLng = !data.longitude?"":data.longitude;
	var staffSkill = data.skillStr;
	var idCardNum = data.identityCard;
	var staffLocation = "&nbsp;";
	var staffPicture = !data.picture?"":data.picture;
	var staffRoleList = data.staffRoleList;
	
	//人员所属组织
	var orgId = data.orgId;
	var orgAddress = data.orgAddress;
	var orgName = data.orgName;
	var orgLat = data.orgLat;
	var orgLng = data.orgLng;
	
	$("#belongTo").html(orgName);
	$("#orgAddress").html(orgAddress);
	$("#hiddenOrgId").val(orgId);
	
	if(curLat!=""&&curLng!=""){
		staffLocation = curLat+" , "+curLng;
	}
	if(staffPicture!=""){
		$("#staffPicture").attr("src",staffPicture);
	}
	
	$("#staffName").html(staffName);
	$("#itAccount").html(itAccount);
	$("#staffSex").html(staffSex);
	$("#birthday").html(birthday);
	$("#email").html(email);
	$("#staffType").html(staffType);
	$("#cellPhoneNumber").html(cellPhoneNumber);
	$("#address").html(address);
	$("#staffLocation").html(staffLocation);
	$("#staffSkill").html(staffSkill);
	$("#idCardNum").html(idCardNum);
	//人员角色
	if(staffRoleList){
		var table = $("#staffRoleTable");
		table.html("");
		var thHtml = "<tr><th>角色</th></tr>";
		var th = $(thHtml);
		th.appendTo(table);
		//循环生成数据
		for(var i=0;i<staffRoleList.length;i++){
			var role = staffRoleList[i];
			if(!role)continue;
			var tr = $("<tr></tr>");
			var roleName = role.name;
			
			if(!roleName){roleName="";}
			
			roleName = $("<td>"+roleName+"</td>");
			
			roleName.appendTo(tr);
			tr.appendTo(table);
		}
	}
}

/**----------各种分页--------------**/
//储物箱 首页
function rbFirstPage(){
	var pageIndex = 1;
	var pageSize = $("#rbPagingColunmSelect").val();
	$("#rbPagingColumnCurrentPage").val(pageIndex);
}
//储物箱 尾页
function rbLastPage(){
	var pageIndex = $("#rbPagingColumnTotalPage").html();
	var pageSize = $("#rbPagingColunmSelect").val();
	$("#rbPagingColumnCurrentPage").val(pageIndex);
}
//储物箱 上一页
function rbBackPage(){
	var pageSize = $("#rbPagingColunmSelect").val();
	var pageIndex = $("#rbPagingColumnCurrentPage").val();
	var totalPage = $("#rbPagingColumnTotalPage").html();
	pageIndex = parseInt(pageIndex)-1;
	if(parseInt(pageIndex) < 1){
		pageIndex = 1;
	}
	$("#rbPagingColumnCurrentPage").val(pageIndex);
}
//储物箱 下一页
function rbNextPage(){
	var pageSize = $("#rbPagingColunmSelect").val();
	var pageIndex = $("#rbPagingColumnCurrentPage").val();
	var totalPage = $("#rbPagingColumnTotalPage").html();
	pageIndex = parseInt(pageIndex)+1;
	if(parseInt(pageIndex) > parseInt(totalPage)){
		pageIndex = totalPage;
	}
	$("#rbPagingColumnCurrentPage").val(pageIndex);
}
//储物箱 跳转到指定页
function rbSkipToPage(){
	var pageSize = $("#rbPagingColunmSelect").val();
	var pageIndex = $("#rbPagingColumnCurrentPage").val();
	var totalPage = $("#rbPagingColumnTotalPage").html();
	if(parseInt(pageIndex) > parseInt(totalPage)){
		pageIndex = totalPage;
	}
	$("#rbPagingColumnCurrentPage").val(pageIndex);
}

/**--------张声JS----------**/
$(function(){
	$(".tab_menu ul li").each(function(index){
		$(this).click(function(){
			$(".tab_menu ul li").removeClass("ontab");
			$(this).addClass("ontab");
			$(".tab_content").hide();
			$(".tab_content").eq(index).show();
		})
	})
	
	/*tab里面的tab*/
	$(".tab_inside_ul ul li").each(function(index){
		$(this).click(function(){
			$(".tab_inside_ul ul li").removeClass("tab_inside_ontab");
			$(this).addClass("tab_inside_ontab");
			$(".tab_inside_content").hide();
			$(".tab_inside_content").eq(index).show();
		})
	})
})

