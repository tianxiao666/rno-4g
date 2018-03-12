var fileSize = 0;
var stopQueryProgress=false;///停止查询进度

$(document).ready(function() {

	// 设置jquery ui
	jqueryUiSet();
	//绑定事件
	bindEvent();
	
	// 查询导入记录
	queryImportDataRec();
	
	//初始化uploadCityId
	$("#uploadCityId").val($("#citymenu").val());
});

//绑定事件
function bindEvent(){
	$("#importBtn").click(function() {
		var filename = fileid.value; 
		if(!(filename.toUpperCase().endsWith(".CSV")||filename.toUpperCase().endsWith(".XLS")
				||filename.toUpperCase().endsWith(".XLSX")||filename.toUpperCase().endsWith(".XML")
				||filename.toUpperCase().endsWith(".TXT")||filename.toUpperCase().endsWith(".ZIP"))){
			alert("不支持该类型文件！");
			return false;
		}
		var flag = confirm("是否导入文件？");
		if (flag == false){
		  return false;
		}
		$("#err").remove();
		if ($("#fileid").val() == "") {
			$("#fileid").parent().append("<font id='err' color='red'>请选择MR文件</font>");
			return;
		}
		if ($.trim($("#meatime").val()) == "") {
			$("#meatime").parent().append("<font id='err' color='red'>请选择测试日期</font>");
			return;
		}
		$("#uploadMsgDiv").css("display","none");
		stopQueryProgress=false;
		doUpload();
	});
	
	//搜索导入记录
	$("#searchImportBtn").click(function(){
		initFormPage("searchImportForm");
		var factory=$("#formImportMr #factory").find("option:selected").val();
		$("#searchImportDiv #fileCode").val(factory);
		queryImportDataRec();
	});
	
	//查询MR描述数据
	$("#searchMrBtn").click(function(){
		initFormPage("searchMrForm");
		queryMrDescData();
	});
				
	//显示隐藏导入窗口
	$("#importTitleDiv").click(function(){
		var flag = $("#importDiv").is(":hidden");//是否隐藏
		if(flag) {
			$(".importContent").show("fast");
		} else {
			$(".importContent").hide("fast");
		}
	});

	$("#provincemenu").change(function() {
		getSubAreas("provincemenu", "citymenu", "市");
	});
	//切换区域时，赋值给uploadCityId
	$("#citymenu").change(function() {	
		$("#uploadCityId").val($("#citymenu").val());
	});
}


/**
 * 在某个基准日期的基础上，对时间进行加减
 * 正数为加，负数为减去
 * @param baseDate
 * @param day
 * @returns {Date}
 */
function addDays(baseDate,day){
    var tinoneday=24*60*60*1000;
    var nt=baseDate.getTime();
    var cht=day*tinoneday;
    var newD=new Date();
    newD.setTime(nt+cht);
    return newD;
}


//jquery ui 效果
function jqueryUiSet() {
	$("#progressbar").progressbar({
		value : 0
	});
	$("#tabs").tabs();
	
	$( "#accordion" ).accordion();
	
	$("#searchImportDiv").css("height","46px");
	$("#importDiv").css("height","250px");
	
	
	//--MR上传记录---//
	//$("#provincemenu").selectmenu();
	//$("#citymenu").selectmenu();
	$("#importstatusmenu").selectmenu();
	$("#datepicker").datepicker({
		"dateFormat" : "yy-mm-dd"
	});
	$("#uploadQueryBegDate").datepicker(
			{
				dateFormat : "yy-mm-dd",
				defaultDate : "-2",
				changeMonth : true,
				numberOfMonths : 1,
				onClose : function(selectedDate) {
					$("#uploadQueryEndDate").datepicker("option", "minDate",
							selectedDate);
				}
			});
	$("#uploadQueryBegDate").datepicker("setDate", -2);// 减去2天

	$("#uploadQueryEndDate").datepicker(
			{
				dateFormat : "yy-mm-dd",
				defaultDate : "+1w",
				changeMonth : true,
				numberOfMonths : 1,
				onClose : function(selectedDate) {
					$("#uploadQueryBegDate").datepicker("option", "maxDate",
							selectedDate);
				}
			});
	$("#uploadQueryEndDate").datepicker("setDate",(new Date()));
	
	//---mr记录查询----//
	$("#citymenu2").selectmenu();
	$("#provincemenu2").selectmenu({
		change: function( event, ui ) {
			getSubAreas("provincemenu2", "citymenu2", "市");
			//需要先初始化，再绑定
			$("#citymenu2").selectmenu("destroy");
			$("#citymenu2").selectmenu();
		}
	});
	$("#mrMeaBegDate").datetimepicker(
			{
				dateFormat : "yy-mm-dd",
				timeFormat: "HH:mm:ss",
				defaultDate : "-2",
				changeMonth : true,
				numberOfMonths : 1,
				onClose : function(selectedDate) {
					$("#mrMeaEndDate").datetimepicker("option", "minDate",
							selectedDate);
				}
			});
	$("#mrMeaBegDate").datetimepicker("setDate",addDays(new Date(),-2));// 减去2天
	$("#mrMeaEndDate").datetimepicker(
			{
				dateFormat : "yy-mm-dd",
				timeFormat: "HH:mm:ss",
				defaultDate :"+1w",
				changeMonth : true,
				numberOfMonths : 1,
				onClose : function(selectedDate) {
					$("#mrMeaBegDate").datetimepicker("option", "maxDate",
							selectedDate);
				}
			});
	$("#mrMeaEndDate").datetimepicker("setDate",(new Date()));
	//$("#searchImportBtn").button();
}



/**
 * 查询导入记录
 */
function queryImportDataRec(){
	$("#searchImportForm").ajaxSubmit({
		url:'queryUploadRecAjaxAction',
		type:'post',
		dataType:'text',
		success:function(raw){
			var data={};
			try{
			data=eval("("+raw+")");
			}catch(err){
				console.log(err);
			}
			displayImportRec(data['data']);
			setFormPageInfo("searchImportForm",data['page']);
			setPageView(data['page'],"mrImportRecPageDiv");
		}
	});
}


/**
 * 显示上传记录
 * @param data
 */
function displayImportRec(data){
	if(data==null||data==undefined){
		return;
	}
	//
	$("#importListTab tr:not(:first)").each(function(i, ele) {
		$(ele).remove();
	});
	var html="";
	var city=$("#citymenu").find("option:selected").text();
	for ( var i = 0; i < data.length; i++) {
		one = data[i];
		html += "<tr>";
		html+="<td>"+getValidValue(city,'')+"</td>";
		html+="<td>"+getValidValue(one['uploadTime'],'')+"</td>";
		html+="<td>"+getValidValue(one['oriFileName'],'')+"</td>";
		html+="<td>"+getPropValueExpress(one['fileSize'])+"</td>";
		html+="<td>"+getValidValue(one['launchTime'],'')+"</td>";
		html+="<td>"+getValidValue(one['completeTime'],'')+"</td>";
		html+="<td>"+getValidValue(one['account'],'')+"</td>";
		if(one['fileStatus'].indexOf("失败") == -1) {
			html+="<td><a style='text-decoration:underline;' onclick='viewDataUploadReport("+one['jobId']+")'>"+one['fileStatus']+"</a></td>";
		} else {
			html+="<td><a style='text-decoration:underline;color:red;' onclick='viewDataUploadReport("+one['jobId']+")'>"+one['fileStatus']+"</a></td>";
		}
		html+="</tr>";
		
		
	}
	$("#importListTab").append(html);
}





//设置formid下的page信息
//其中，当前页会加一
function setFormPageInfo(formId, page) {
	if (formId == null || formId == undefined || page == null
			|| page == undefined) {
		return;
	}

	var form = $("#" + formId);
	if (!form) {
		return;
	}

	// console.log("setFormPageInfo .
	// pageSize="+page.pageSize+",currentPage="+page.currentPage+",totalPageCnt="+page.totalPageCnt+",totalCnt="+page.totalCnt);
	form.find("#hiddenPageSize").val(page.pageSize);
	form.find("#hiddenCurrentPage").val(new Number(page.currentPage));// /
	form.find("#hiddenTotalPageCnt").val(page.totalPageCnt);
	form.find("#hiddenTotalCnt").val(page.totalCnt);

}

/**
 * 设置分页面板
 * 
 * @param page
 *            分页信息
 * @param divId
 *            分页面板id
 */
function setPageView(page, divId) {
	if (page == null || page == undefined) {
		return;
	}

	var div = $("#" + divId);
	if (!div) {
		return;
	}

	//
	var pageSize = page['pageSize'] ? page['pageSize'] : 0;
	// $("#hiddenPageSize").val(pageSize);

	var currentPage = page['currentPage'] ? page['currentPage'] : 1;
	// $("#hiddenCurrentPage").val(currentPage);

	var totalPageCnt = page['totalPageCnt'] ? page['totalPageCnt'] : 0;
	// $("#hiddenTotalPageCnt").val(totalPageCnt);

	var totalCnt = page['totalCnt'] ? page['totalCnt'] : 0;
	// $("#hiddenTotalCnt").val(totalCnt);

	// 设置到面板上
	$(div).find("#emTotalCnt").html(totalCnt);
	$(div).find("#showCurrentPage").val(currentPage);
	$(div).find("#emTotalPageCnt").html(totalPageCnt);
}

//初始化form下的page信息
function initFormPage(formId) {
	var form = $("#" + formId);
	if (!form) {
		return;
	}
	// form.find("#hiddenPageSize").val(25);
	form.find("#hiddenCurrentPage").val(1);
	form.find("#hiddenTotalPageCnt").val(-1);
	form.find("#hiddenTotalCnt").val(-1);
}




//function toggleSearchImport() {
//	if ($("#importDiv").css("display") != "none") {
//		$("#importDiv").css("display", "none");
//	}
//	$("#searchImportDiv").toggle({
//		"duration" : 500
//	});
//}

function toggleImport() {
//	if ($("#searchImportDiv").css("display") != "none") {
//		$("#searchImportDiv").css("display", "none");
//	}
	$("#importDiv").toggle({
		"duration" : 500
	});
}

// 上传
function doUpload() {
	
	//ERI4GMRFILE ZTE4GMRFILE 
	var factory=$("#formImportMr #factory").find("option:selected").val();
	$("#formImportMr #fileCode").val(factory);
	$("#progressNum").text('0%');
	$("#progressbar").progressbar({
		value : 0
	});
	$("#progressInfoDiv").fadeIn();

	fileSize = 1;
	var fileCode = $("#formImportMr #fileCode").val();
	var attachParams = {};
	attachParams['cityId'] = $("#cityId1").val();

	// 询问是否可以上传
	$.ajax({
		url : 'ifFileAcceptableAjaxAction',
		data : {
			'fileSize' : fileSize,
			'fileCode' : fileCode,
			'attachParams' : attachParams
		},
		type : 'post',
		dataType : 'text',
		success : function(raw2) {
			var accp = {};
			try {
				accp = eval("(" + raw2 + ")");
			} catch (err) {

			}
			if (accp['flag'] == true) {
				// 可以上传，获取token
				var token = accp['token'];

				// 启动进度查询方法
				window.setTimeout(function() {
					queryProgress(token);
				}, 1000);

				$("#token").val(token);
				// 执行上传
				$("#formImportMr").ajaxSubmit({
					type : 'post',
					url : "batchUploadFileAjaxAction",
					dataType : 'text',
					success : function(raw) {
						if(raw){
							try{
								var data=eval("("+raw+")");
								if(data['flag']==true){
									$("#progressNum").text('100%');
									$("#progressbar").progressbar({
										value : 100
									});
									$("#uploadMsgDiv").html("上传成功");
									$("#uploadMsgDiv").css("display","block");
								}else{
									$("#uploadMsgDiv").html("上传失败！"+getValidValue(data['msg']));
									$("#uploadMsgDiv").css("display","block");
									stopQueryProgress=true;
								}
							}catch(err){
								$("#uploadMsgDiv").html("上传失败");
								$("#uploadMsgDiv").css("display","block");
								stopQueryProgress=true;
							}
						}else{
							$("#uploadMsgDiv").html("上传失败");
							$("#uploadMsgDiv").css("display","block");
							stopQueryProgress=true;
						}
					}
				});
			}
		}
	});

}

function queryProgress(token) {

	$.ajax({
		url : "queryUploadFileProgressAction",
		type : 'post',
		data : {
			'token' : token
		},
		success : function(raw) {
			// console.log("progress data:" + raw)
			if (raw == null) {
				clearInterval(i);
				// location.reload(true);
				return;
			}
			var data = {};
			try {
				data = eval('(' + raw + ")");
			} catch (err) {
				console.log(err);
			}
			var percentage=0;
			if(data.totalBytes>0){
				percentage= Math.floor(100 * parseFloat(data.readedBytes)
					/ parseFloat(data.totalBytes));
			}
			$("#progressbar").progressbar({
				value : percentage
			});
			$("#progressNum").text(percentage + '%');
			if (percentage >= 1) {
				return;
			}
			if(stopQueryProgress===false){
				window.setTimeout(function() {
					queryProgress(token);
				}, 5000);
			}
		}
	});
}

/**
 * 查询MR描述数据
 */
function queryMrDescData(){
	$("#searchMrForm").ajaxSubmit({
		url:'queryMrDescAjaxAction',
		type:'post',
		dataType:'text',
		success:function(raw){
			var data={};
			try{
			data=eval("("+raw+")");
			}catch(err){
				console.log(err);
			}
			displayMrDescData(data['data']);
			setFormPageInfo("searchMrForm",data['page']);
			setPageView(data['page'],"mrListPageDiv");
		}
	});
}

/**
 * 显示返回的MR描述信息
 * @param data
 */
function displayMrDescData(data){
	if(data==null||data==undefined){
		return;
	}
	//
	$("#mrListTab tr:not(:first)").each(function(i, ele) {
		$(ele).remove();
	});
	var html="";
	var city=$("#citymenu2").find("option:selected").text();
	var factory="";
	for ( var i = 0; i < data.length; i++) {
		one = data[i];
		factory=getValidValue(one['FACTORY'],'');
		if(factory=="ERI"){
			factory="爱立信";
		}else if(factory=="ZTE"){
			factory="中兴";
		}
		html += "<tr>";
		html+="<td>"+city+"</td>";
		html+="<td>"+getValidValue(one['MEA_TIME'],'')+"</td>";
		html+="<td>"+factory+"</td>";
		html+="<td>"+getValidValue(one['RECORD_COUNT'],'')+"</td>";
		html+="<td>"+getValidValue(one['CREATE_TIME'],'')+"</td>";
		html+="</tr>";
	}
	$("#mrListTab").append(html);
}

/**
* 分页跳转的响应
* @param dir
* @param action
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


/**
 * 查看具体的报告
 */
function viewDataUploadReport(jobId){
	$("#viewReportForm").find("input#hiddenJobId").val(jobId);
	initFormPage("viewReportForm");
	$("#reportDiv").css("display","block");
	$("#listinfoDiv").css("display","none");
	queryReportData();
}

/**
 * 查询指定job的报告
 */
function queryReportData(){
	$("#viewReportForm").ajaxSubmit({
		url:'queryJobReportAjaxAction',
		type:'post',
		dataType:'text',
		success:function(raw){
			var data={};
			try{
			data=eval("("+raw+")");
			}catch(err){
				console.log(err);
			}
			displayReportRec(data['data']);
			setFormPageInfo("viewReportForm",data['page']);
			setPageView(data['page'],"reportListPageDiv");
		}
	});
}

/**
 * 显示报告
 */
function displayReportRec(data){
	if(data==null||data==undefined){
		return;
	}
	//
	$("#reportListTab tr:not(:first)").each(function(i, ele) {
		$(ele).remove();
	});
	var html="";

	for ( var i = 0; i < data.length; i++) {
		one = data[i];
		html += "<tr>";
		html+="<td>"+getValidValue(one['STAGE'],'')+"</td>";
		html+="<td>"+getValidValue(one['BEG_TIME'],'')+"</td>";
		html+="<td>"+getValidValue(one['END_TIME'],'')+"</td>";
		if(one['STATE'].indexOf("失败") == -1) {
			html+="<td>"+getValidValue(one['STATE'],'')+"</td>";
		} else {
			html+="<td style='color: red;'>"+getValidValue(one['STATE'],'')+"</td>";
		}
		html+="<td style='word-break: break-all;'>"+getValidValue(one['ATT_MSG'],'')+"</td>";
		html+="</tr>";
	}
	$("#reportListTab").append(html);
}

/**
 * 从报告的详情返回列表页面
 */
function returnToImportList(){
	$("#reportDiv").css("display","none");
	$("#listinfoDiv").css("display","block");
}