$(document).ready(
		function() {

	//切换区域
	initAreaCascade();
	//检查是否存在这周计算的干扰矩阵
	isCalculateInterMartixThisWeek();

	//加载MR数据
	$("#showMrData").click(function() {
		$("#isDateRightTip").text("");
		initFormPage('interferMartixAddMrForm');
		loadMrData();
	});

	//新增计算mr干扰矩阵
	$("#calculateMrInterMartix").click(function() {
		var taskname=$("#taskName").val();
		if(taskname.trim()==""){
			animateInAndOut("operInfo", 500, 500, 1000, "operTip",
			"请输入任务名....");
			$('#calculateMrInterMartix').removeAttr("disabled");
		}else if(ifHasSpecChar(taskname)){
			animateInAndOut("operInfo", 500, 500, 1000, "operTip",
			"包含有以下特殊字符:~'!@#$%^&*()-+_=:");
			$('#calculateMrInterMartix').removeAttr("disabled");
		}else{
			addMrInterMartix();
		}

	});
});

/**
* 新增计算MR干扰矩阵
*/
function addMrInterMartix() {
	$("span#isDateRightTip").html("");
	showOperTips("loadingDataDiv", "loadContentId", "正在提交干扰矩阵计算任务");
	if($("#beginTime").val()=="" || $("#latestAllowedTime").val()==""){
		hideOperTips("loadingDataDiv");
		$('#calculateMrInterMartix').removeAttr("disabled");
		alert("时间不能为空！");
		return ;
	}
	$("#interferMartixAddMrForm").ajaxSubmit({
		url : 'add4GInterMartixByMrForAjaxAction',
		dataType : 'text',
		success : function(raw) {
			var data = eval("("+raw+")");
			var isDateRight = data['isDateRight'];
			var isMrExist = data['isMrExist'];
			//var isCalculating = data['isCalculating'];

			if(isDateRight && isMrExist /*&& !isCalculating*/) {
				//提示计算任务已经提交到系统
				alert("计算任务已经提交到系统");
				//页面跳转到干扰矩阵显示页
				var cityId=$("#cityId2").val();
				location.href = 'init4GInterferMartixManageAction?cityId='+cityId;

			} else {
			/*	if(isCalculating) {
					$("span#isCalculateTip").html("存在正在计算或者等待中的干扰矩阵任务..请稍后");
				}*/
				if(!isDateRight) {
					$("span#isDateRightTip").html("日期范围不符合要求（最迟不会超过本周一的0点，最早不会早于上周的周一0点）");
					$('#calculateMrInterMartix').removeAttr("disabled");
				} else {
					if(!isMrExist) {
						$("span#isDateRightTip").html("该日期范围不存在MR数据记录");
						$('#calculateMrInterMartix').removeAttr("disabled");
					}
				}
			}
		},
		complete : function() {
			hideOperTips("loadingDataDiv");
		}
	});
}

/**
* 检查是否存在这周计算的干扰矩阵
*/
function isCalculateInterMartixThisWeek() {
	$("span#isCalculateTip").html("");
	showOperTips("loadingDataDiv", "loadContentId", "正在检查这周是否计算干扰矩阵");

	$("#interferMartixAddMrForm").ajaxSubmit({
		url : 'isExisted4GInterMartixThisWeekAction',
		dataType : 'text',
		success : function(raw) {
			var data = eval("("+raw+")");
			var flag = data['flag'];
			var desc = data['desc'];
			if(flag) {
				$("span#isCalculateTip").html(desc);
			} else {
			}
		},
		complete : function() {
			hideOperTips("loadingDataDiv");
		}
	});
}

/**
* 加载对应的MR数据
*/
function loadMrData() {

	showOperTips("loadingDataDiv", "loadContentId", "正在加载MR数据");
	if($("#beginTime").val()=="" || $("#latestAllowedTime").val()==""){
		hideOperTips("loadingDataDiv");
		alert("日期不能为空!");
		return;
	};
	$("#interferMartixAddMrForm").ajaxSubmit({
		url : 'queryMrDataByPageForAjaxAction',
		dataType : 'text',
		success : function(raw) {
			showMrDataList(raw);
		},
		complete : function() {
			hideOperTips("loadingDataDiv");
		}
	});
}

/**
* 分页显示mr数据
*/
function showMrDataList(raw) {

 	var data = eval("("+raw+")");
	var table = $("#mrDataTable");
	var city=$("#cityId2").find("option:selected").text();
	//清空干扰矩阵详情列表
	$("#mrDataTable tr:gt(0)").remove();

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
		tr += "<td>"+getValidValue(city,'')+"</td>";
		tr += "<td>"+getValidValue(one['MEA_TIME'],'')+"</td>";
		tr += "<td>"+getValidValue(one['FACTORY'],'')+"</td>";
		tr += "<td>"+getValidValue(one['RECORD_COUNT'],'')+"</td>";
		tr += "<td>"+getValidValue(one['CREATE_TIME'],'')+"</td>";
		tr += "</tr>";
		html += tr;
	}
	table.append(html);

	//设置隐藏的page信息
	setFormPageInfo("interferMartixAddMrForm",data['page']);

	//设置分页面板
	setPageView(data['page'],"mrDataPageDiv");
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
		isCalculateInterMartixThisWeek();
		/*getSubAreas("cityId2", "areaId2", "区/县",function(){
			$("#areaId2").append("<option selected='true' value=''>全部</option>");
		});*/
	});
}