$(document).ready(
		function() {
			
	//切换区域
	initAreaCascade();
	//默认加载干扰矩阵
	getInterferMartix();
	
	$("#searchInterMartix").click(function(){
		getInterferMartix();
	});

	//重定向至新增ncs干扰矩阵页面
	$("#addInterMartix").click(function(){
		location.href = 'initInterferMartixAddForAjaxAction';
	});
});

/**
* 按条件查询干扰矩阵
*/
function getInterferMartix() {
	//重置分页条件
	initFormPage('interferMartixForm');
	//提交表单
	sumbitInterferMartixForm();
}

/**
* 提交表单
*/
function sumbitInterferMartixForm() {
	showOperTips("loadingDataDiv", "loadContentId", "正在加载");
	
	$("#interferMartixForm").ajaxSubmit({
		url : 'queryInterferMartixByPageForAjaxAction',
		dataType : 'text',
		success : function(raw) {
			showInterMartix(raw);
		},
		complete : function() {
			hideOperTips("loadingDataDiv");
		}
	});
}

/**
* 列表显示干扰矩阵
*/
function showInterMartix(raw) {
 	
 	var data = eval("("+raw+")");
	var table = $("#interferMartixTable");
	
	//清空干扰矩阵详情列表
	$("#interferMartixTable tr:gt(0)").remove();
	
	if(data == null || data == undefined){
		return;
	}
	
	var list=data['data'];
	var html="";
	var tr="";
	var one="";
	
	var startStr;
	var endStr;
	var startDate;
	var endDate;
	var sy,sm,sd;//年月日
	var ey,em,ed;
	
	for(var i=0;i<list.length;i++){
		one = list[i];
		tr = "<tr>";
		tr += "<td>"+getValidValue(one['AREA_NAME'],'')+"</td>";
		tr += "<td>"+getValidValue(one['CREATE_DATE'],'')+"</td>";
		//获取开始结束日期转为页面描述信息
		startStr = getValidValue(one['START_MEA_DATE'],'');
		endStr = getValidValue(one['END_MEA_DATE'],'');
		startStr = startStr.replace(/-/g,"/");
		endStr = endStr.replace(/-/g,"/");
		startDate = new Date(startStr);
		sy = startDate.getYear()+1900;
		sm = startDate.getMonth()+1;
		sd = startDate.getDate();
		endDate = new Date(endStr);
		ey = endDate.getYear()+1900;
		em = endDate.getMonth()+1;
		ed = endDate.getDate();
		
		tr += "<td>"+sy+"年"+sm+"月"+getMonthWeek(sy, sm, sd)+"周<br/>"+sy+"-"+sm+"-"+sd+" ~ "+ey+"-"+em+"-"+ed+"</td>";
		tr += "<td>"+getValidValue(one['RECORD_NUM'],'')+"</td>";
		tr += "<td>"+getValidValue(one['TYPE'],'')+"</td>";
		tr += "<td>"+getValidValue(one['WORK_STATUS'],'')+"</td>";
		tr += "</tr>";
		html += tr;
	}
	table.append(html);
	
	//设置隐藏的page信息
	setFormPageInfo("interferMartixForm",data['page']);
	
	//设置分页面板
	setPageView(data['page'],"interMartixPageDiv");
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

function initAreaCascade() {
	
	$("#provinceId2").change(function() {
		getSubAreas("provinceId2", "cityId2", "市");
	});

	$("#cityId2").change(function() {	
		/*getSubAreas("cityId2", "areaId2", "区/县",function(){
			$("#areaId2").append("<option selected='true' value=''>全部</option>");
		});*/
	});
	
	//判断服务器是否指定了默认的cityId
	var cityIdFromRes = $("#cityIdFromRes").val();
	if(cityIdFromRes != "" && cityIdFromRes != "0" && cityIdFromRes != null) {
		$("#cityId2").val(cityIdFromRes);
		//console.log(cityIdFromRes);
	}
}

var getMonthWeek = function (a, b, c) { 
	/* 
	a = d = 当前日期 
	b = 6 - w = 当前周的还有几天过完（不算今天） 
	a + b 的和在除以7 就是当天是当前月份的第几周 
	*/ 
	var date = new Date(a, parseInt(b) - 1, c), w = date.getDay(), d = date.getDate(); 
		return Math.ceil( 
		(d + 6 - w) / 7 
		); 
};
