$(document).ready(
		function() {
		
	//切换区域
	initAreaCascade();
	//检查是否存在这周计算的干扰矩阵
	//isCalculateInterMartixThisWeek();
	
	//加载ncs数据
	$("#showNcsData").click(function() {
		initFormPage('interferMartixAddNcsForm');
		loadNcsData();
	});
	
	//新增计算ncs干扰矩阵
	$("#calculateNcsInterMartix").click(function() {
		add2GDirectionAngleTask();
	});
});

/**
* 新增2g小区方向角计算
*/
function add2GDirectionAngleTask() {
	$("span#isDateRightTip").html("");
	showOperTips("loadingDataDiv", "loadContentId", "正在提交计算任务");

	$("#interferMartixAddNcsForm").ajaxSubmit({
		url : 'add2GDirectionAngleTaskForAjaxAction',
		dataType : 'text',
		success : function(raw) {
			var data = eval("("+raw+")");
			var isNcsExist = data['isNcsExist'];
			
			if(isNcsExist) {
				//提示计算任务已经提交到系统
				alert("计算任务已经提交到系统");
				//页面跳转到干扰矩阵显示页
				var cityId=$("#cityId2").val();
				location.href = 'init2GCellDirectionAngleTaskManageAction?cityId='+cityId;

			} else {
				$("span#isDateRightTip").html("该日期范围不存在NCS数据记录");
				$('#calculateNcsInterMartix').removeAttr("disabled"); 
			}
		},
		complete : function() {
			hideOperTips("loadingDataDiv");
		}
	});
}

///**
//* 检查是否存在这周计算的干扰矩阵
//*/
//function isCalculateInterMartixThisWeek() {
//	$("span#isCalculateTip").html("");
//	showOperTips("loadingDataDiv", "loadContentId", "正在检查这周是否计算干扰矩阵");
//	
//	$("#interferMartixAddNcsForm").ajaxSubmit({
//		url : 'isExistedInterMartixThisWeekAction',
//		dataType : 'text',
//		success : function(raw) {
//			var data = eval("("+raw+")");
//			var flag = data['flag'];
//			var desc = data['desc'];
//			if(flag) {
//				$("span#isCalculateTip").html(desc);
//			} else {
//			}
//		},
//		complete : function() {
//			hideOperTips("loadingDataDiv");
//		}
//	});
//}

/**
* 加载对应的NCS数据
*/
function loadNcsData() {

	showOperTips("loadingDataDiv", "loadContentId", "正在加载NCS数据");
	
	$("#interferMartixAddNcsForm").ajaxSubmit({
		url : 'queryNcsDataByPageForAjaxAction',
		dataType : 'text',
		success : function(raw) {
			showNcsDataList(raw);
		},
		complete : function() {
			hideOperTips("loadingDataDiv");
		}
	});
}

/**
* 分页显示NCS数据
*/
function showNcsDataList(raw) {
	
 	var data = eval("("+raw+")");
	var table = $("#ncsDataTable");
	
	//清空干扰矩阵详情列表
	$("#ncsDataTable tr:gt(0)").remove();
	
	if(data == null || data == undefined){
		return;
	}
	
	var list=data['data'];
	var html="";
	var tr="";
	var one="";
	
	for(var i=0;i<list.length;i++){
		one = list[i];
		tr = "<tr>";
		tr += "<td>"+getValidValue(one['NAME'],'')+"</td>";
		tr += "<td>"+getValidValue(one['BSC'],'')+"</td>";
		tr += "<td>"+getValidValue(one['FILE_NAME'],'')+"</td>";
		tr += "<td>"+getValidValue(one['MEA_TIME'],'')+"</td>";
		tr += "</tr>";
		html += tr;
	}
	table.append(html);
	
	//设置隐藏的page信息
	setFormPageInfo("interferMartixAddNcsForm",data['page']);
	
	//设置分页面板
	setPageView(data['page'],"ncsDataPageDiv");
}

//设置隐藏的page信息
function setFormPageInfo(formId, page) {
	if(formId==null || formId==undefined || page==null || page==undefined){
		return;
	}
	
	var form=$("#"+formId);
	if(!form){
		return;
	}
	
	form.find("#hiddenPageSize").val(page.pageSize);
	form.find("#hiddenCurrentPage").val(new Number(page.currentPage));
	form.find("#hiddenTotalPageCnt").val(page.totalPageCnt);
	form.find("#hiddenTotalCnt").val(page.totalCnt);
}

/**
* 设置分页面板
* @param page
* 分页信息
* @param divId
* 分页面板id
*/
function setPageView(page,divId){
	if(page==null || page==undefined  ){
		return;
	}
	
	var div=$("#"+divId);
	if(!div){
		return;
	}

	var pageSize = page['pageSize'] ? page['pageSize'] : 0;
	var currentPage = page['currentPage'] ? page['currentPage'] : 1;
	var totalPageCnt = page['totalPageCnt'] ? page['totalPageCnt'] : 0;
	var totalCnt = page['totalCnt'] ? page['totalCnt'] : 0;

	//设置到面板上
	$(div).find("#emTotalCnt").html(totalCnt);
	$(div).find("#showCurrentPage").val(currentPage);
	$(div).find("#emTotalPageCnt").html(totalPageCnt);
}

//初始化form下的page信息
function initFormPage(formId){
	var form=$("#"+formId);
	if(!form){
		return;
	}
	form.find("#hiddenPageSize").val(25);
	form.find("#hiddenCurrentPage").val(1);
	form.find("#hiddenTotalPageCnt").val(-1);
	form.find("#hiddenTotalCnt").val(-1);
}

/**
* 分页跳转的响应
* @param dir
* @param action（方法名）
* @param formId
* @param divId
*/
function showListViewByPage(dir,action,formId,divId) {
	
	var form=$("#"+formId);
	var div=$("#"+divId);
	//alert(form.find("#hiddenPageSize").val());
	var pageSize =new Number(form.find("#hiddenPageSize").val());
	var currentPage = new Number(form.find("#hiddenCurrentPage").val());
	var totalPageCnt =new Number(form.find("#hiddenTotalPageCnt").val());
	var totalCnt = new Number(form.find("#hiddenTotalCnt").val());

	//console.log("pagesize="+pageSize+",currentPage="+currentPage+",totalPageCnt="+totalPageCnt+",totalCnt="+totalCnt);
	if (dir === "first") {
		if (currentPage <= 1) {
			return;
		} else {
			$(form).find("#hiddenCurrentPage").val("1");
		}
	} else if (dir === "last") {
		if (currentPage >= totalPageCnt) {
			return;
		} else {
			$(form).find("#hiddenCurrentPage").val(totalPageCnt);
		}
	} else if (dir === "back") {
		if (currentPage <= 1) {
			return;
		} else {
			$(form).find("#hiddenCurrentPage").val(currentPage - 1);
		}
	} else if (dir === "next") {
		if (currentPage >= totalPageCnt) {
			return;
		} else {
			$(form).find("#hiddenCurrentPage").val(currentPage + 1);
		}
	} else if (dir === "num") {
		var userinput = $(div).find("#showCurrentPage").val();
		if (isNaN(userinput)) {
			alert("请输入数字！")
			return;
		}
		if (userinput > totalPageCnt || userinput < 1) {
			alert("输入页面范围不在范围内！");
			return;
		}
		$(form).find("#hiddenCurrentPage").val(userinput);
	}else{
		return;
	}
	//获取资源
	if(typeof action =="function"){
		action();
	}
}

//区域切换触发
function initAreaCascade() {
	
	$("#provinceId2").change(function() {
		getSubAreas("provinceId2", "cityId2", "市");
	});

	$("#cityId2").change(function() {	
		//检查是否存在这周计算的干扰矩阵
		//isCalculateInterMartixThisWeek();
		/*getSubAreas("cityId2", "areaId2", "区/县",function(){
			$("#areaId2").append("<option selected='true' value=''>全部</option>");
		});*/
	});
}