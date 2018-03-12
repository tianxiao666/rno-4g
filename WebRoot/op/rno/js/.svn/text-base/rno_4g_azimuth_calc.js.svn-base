
//点击td转换可编辑
var editHTML;
var editText;

$(document).ready(
		function() {

	//切换区域
	initAreaCascade();
	initAreaCascade2();
	//默认加载结构分析
	//getStructureTask();

	$("#searchStructureTask").click(function(){
		$("span#nameErrorText").html("");
		var taskName = $.trim($("#taskName").val());
		if(ifHasSpecChar(taskName)){
			$("span#nameErrorText").html("含有特殊字符");
			return;
		}
		getStructureTask();
	});

	/**
	 * 提交任务分析
	 */
	$("#submitTask").click(function(){
		//eriAndHwStructureAnalysis();
		pciPlanAnalysis();
	});
	/**
	 * 取消任务
	 */
	$("#cancleTask").click(function(){
		cancleNcsAndMrrTask();
	});

	$("#addTask").click(function() {
		$("#azimuth_calcWinDiv").toggle();
			/*var cityId=$("#citymenu").find("option:selected").val();
			queryDateInfo(cityId, 3,waitforsel,selected);*/
		var dateStr=getBeforeDate(5);
		dateStr=dateStr+" 00:00:00";
		$("#azimuth_calcWinDiv #_beginTime").val(dateStr);
		var input = $(this);
        var offset = input.offset();
        //先后设置div的内容、位置，最后显示出来（渐进效果）
        //$('#'+divId).css('left',offset.left + input.width() + 2 + 'px').css('top',offset.top + 'px');
        $("#azimuth_calcWinDiv").css('position','absolute').css('left',document.body.clientWidth/2-document.getElementById("azimuth_calcWinDiv").offsetWidth/2).css('top',((document.body.clientHeight/2-document.getElementById("azimuth_calcWinDiv").offsetHeight/2)<0)?0:(document.body.clientHeight/2-document.getElementById("azimuth_calcWinDiv").offsetHeight/2));
	});
	$("#calc_azimuth").click(function(){
		submitCalcTask();
	});
});



//提交PCI分析任务
function pciPlanAnalysis() {

	var lteCells = $("#pciCell").val().trim();

	//验证数据
	if(ifHasSpecChar(lteCells)) {
		animateInAndOut("operInfo", 500, 500, 1000, "operTip",
		"包含有以下特殊字符:~'!@#$%^&*()-+_=:");
		$('#submitTask').removeAttr("disabled");
		return;
	}

	showOperTips("loadingDataDiv", "loadContentId", "正在提交结构分析计算任务");
	$.ajax({
		url : 'submitPciPlanAnalysisTaskAction',
		data : {
			'lteCells' : lteCells
		},
		type : 'post',
		dataType : 'text',
		success : function(raw) {
			var data = null;
			try {
				data = eval("(" + raw + ")");
				if (data['flag'] == false) {
					alert(data['result']);
				} else {
					alert("任务提交成功，请等待计算完成！");
					//animateInAndOut("operInfo", 500, 500, 1000, "operTip","计算任务提交成功，请稍后查看结果。");
					//location.href="initNcsAnalysisPageAction";
					$("#initNcsAnalysisPageForm").submit();
				}
			} catch (err) {
			}
		},
		complete : function() {
			hideOperTips("loadingDataDiv");
		}
	});
}

//取消结构分析任务
function cancleNcsAndMrrTask() {
	location.href="initLteInterferCalcPageAction";
}

/**
 *
 * @title 存储任务消息向session
 * @author chao.xj
 * @date 2014-7-15下午3:33:14
 * @company 怡创科技
 * @version 1.2
 */
function storageTaskInfoForSession(){

	var isMeet = checkTaskInfoSubmit();
	if(!isMeet){
		return;
	}
	$("span#dataTypeErrorText").html("");
	//存储数据
	var taskName = $("#taskName").val();
	var taskDescription = $("#taskDescription").val();
	var provinceId = $("#provinceId2").val();
	var cityId = $("#cityId2").val();
	var provinceName = $("#provinceId2  option:selected").text();
	var cityName = $("#cityId2  option:selected").text();
	//console.log(provinceName+" " +cityName);
	var meaStartTime = $("#beginTime").val();
	var meaEndTime = $("#latestAllowedTime").val();

	var planTypeOne = $("#planType1").is(":checked");
	var planTypeTwo = $("#planType2").is(":checked");

	var planOne=$("#useEriData").is(":checked");
	var planTwo=$("#useHwData").is(":checked");
	var cosi = $("#cosi").val();
//	var calConCluster=$("#calConCluster").is(":checked");
//	var calClusterConstrain=$("#calClusterConstrain").is(":checked");
//	var calClusterWeight=$("#calClusterWeight").is(":checked");
//	var calCellRes=$("#calCellRes").is(":checked");
//	var calIdealDis=$("#calIdealDis").is(":checked");
	var i=0;
	if(!planOne){
		 i++;
		}
	if(!planTwo){
		i++;
		}
	if(i==2){
		$("span#dataTypeErrorText").html("不能均不选择，至少选择一类!");
		return;
	}
	var converType;
	if(planOne){
		converType = "ONE";
	}else {
		converType = "TWO";
	}

	i = 0;
	if (!planTypeOne) {
		i++;
	}
	if (!planTypeTwo) {
		i++;
	}
	if (i == 2) {
		$("span#dataTypeErrorText").html("不能均不选择，至少选择一类!");
		return;
	}
	var planType;
	if (planTypeOne) {
		planType = "ONE";
	} else {
		planType = "TWO";
	}

	showOperTips("loadingDataDiv", "loadContentId", "正在跳转页面");

	$.ajax({
		url : 'storageLteInterferCalcTaskObjInfoForAjaxAction',
		data : {
			'taskInfoType' : 'taskInfoForward',
			'taskName':taskName,
			'taskDescription':taskDescription,
			'provinceId' : provinceId,
			'cityId' : cityId,
			'provinceName' :  provinceName,
			'cityName' :  cityName,
			'meaStartTime' : meaStartTime,
			'meaEndTime' : meaEndTime,
			'planType' : planType,
			'converType' : converType,
			'cosi' : cosi
//			'calProcedure.CALCONCLUSTER':calConCluster,
//			'calProcedure.CALCLUSTERCONSTRAIN':calClusterConstrain,
//			'calProcedure.CALCLUSTERWEIGHT':calClusterWeight,
//			'calProcedure.CALCELLRES':calCellRes,
//			'calProcedure.CALIDEALDIS':calIdealDis
		},
		dataType : 'text',
		type : 'post',
		success : function(raw) {
			var data;
			try {
				data = eval("(" + raw + ")");
//				console.log("data.state:"+data.state);
				var state = data['state'];
			} catch (err) {
			}
		},
		complete : function() {
			//hideOperTips("loadingDataDiv");
			//跳转新的页面
			location.href="stepByStepOperateLteInterCalcTaskInfoPageForAjaxAction?taskInfoType=taskInfoForward";
		}
	});
}

/**
 *
 * @title 对提交分析任务的信息作验证:不涉及ncs及mrr
 * @returns
 * @author chao.xj
 * @date 2014-7-15上午11:58:25
 * @company 怡创科技
 * @version 1.2
 */
function checkTaskInfoSubmit() {
	var n=0;
	var m=0;
	var flagName = true;
	var flagDesc = true;
	var flagDate = true;

	var taskName = $.trim($("#_taskName").val());
	var taskDescription = $.trim($("#_taskDescription").val());
	var startTime = $.trim($("#_beginTime").val());
 	var endTime = $.trim($("#_latestAllowedTime").val());
	$("span#_nameErrorText").html("");
	$("span#_descErrorText").html("");
	$("span#dateErrorText").html("");
	$("span#nameError").html("");
	$("span#descError").html("");
	$("span#dateError").html("");

 	for(var i=0; i<taskName.length; i++){     //应用for循环语句,获取表单提交用户名字符串的长度
	     var leg = taskName.charCodeAt(i);     //获取字符的ASCII码值
	     if(leg>255){        //判断如果长度大于255
	    	n+=2;           //则表示是汉字为2个字节
	     }else {
	   		n+=1;           //否则表示是英文字符,为1个字节
	     }
    }
    for(var i=0; i<taskDescription.length; i++){
	     var leg=taskDescription.charCodeAt(i);
	     if(leg>255){
	    	m+=2;
	     }else {
	   		m+=1;
	     }
    }
	//验证任务名称
    if (ifHasSpecChar(taskName)){
	     $("span#_nameErrorText").html("（包含有以下特殊字符:~'!@#$%^&*()-+_=:）");
	     $("span#nameError").html("※");
	     flagName = false;
	}
	if (n>25){
	     $("span#_nameErrorText").html("（不超过25个汉字）");
	     $("span#nameError").html("※");
	     flagName = false;
	} else if(n==0) {
		 $("span#_nameErrorText").html("（请输入任务名称）");
	     $("span#nameError").html("※");
	     flagName = false;
	}
	//验证任务描述
	 if (ifHasSpecChar(taskDescription)){
	     $("span#_nameErrorText").html("（包含有以下特殊字符:~'!@#$%^&*()-+_=:）");
	     $("span#nameError").html("※");
	     flagName = false;
	}
	if (m>255){
	     $("span#descErrorText").html("（不超过255个汉字）");
	     $("span#descError").html("※");
	     flagDesc = false;
	}

	if(endTime=="" || startTime==""){
		$("span#dateErrorText").html("请填写需要使用的测量数据的时间！");
	    $("span#dateError").html("※");
		flagDate = false;
	}
	else if(exDateRange(endTime,startTime) > 10) {
		//验证测试日期是否大于十天
		$("span#dateErrorText").html("（时间跨度请不要超过10天！）");
	    $("span#dateError").html("※");
		flagDate = false;
	}

	if(flagName && flagDesc && flagDate) {
		result = true;
	} else {
		result = false;
	}
	return result;
}

/**
* 按条件查询结构分析任务
*/
function getStructureTask() {
	//重置分页条件
	initFormPage('structureTaskForm');
	//提交表单
	sumbitStructureForm();
}

/**
* 提交表单
*/
function sumbitStructureForm() {
	showOperTips("loadingDataDiv", "loadContentId", "正在查询");

	$("#structureTaskForm").ajaxSubmit({
		url : 'query4GAzimuthCalcTaskByPageForAjaxAction',
		dataType : 'text',
		success : function(raw) {
			showStructureTask(raw);
		},
		complete : function() {
			hideOperTips("loadingDataDiv");
		}
	});
}

/**
* 列表显示结构分析任务
*/
function showStructureTask(raw) {

 	if(raw) {

		var table = $("#structureTaskTable");
		//清空结构分析任务详情列表
		$("#structureTaskTable tr:gt(0)").remove();

		var data = eval("("+raw+")");
		if(data == null || data == undefined){
			return;
		}
		var list=data['data'];
		var tr="";
		var one="";

		for(var i=0; i<list.length; i++){
			one = list[i];
			tr += "<tr>";
			tr += "<td>"+getValidValue(one['JOB_NAME'],'')+"</td>";
			if(one['JOB_RUNNING_STATUS'] == "Initiate") {
				tr += "<td>排队中</td>";
			} else if(one['JOB_RUNNING_STATUS'] == "Launched"
						|| one['JOB_RUNNING_STATUS'] == "Running") {
				tr += "<td>运行中</td>";
			} else if(one['JOB_RUNNING_STATUS'] == "Stopping") {
				tr += "<td>停止中</td>";
			} else if(one['JOB_RUNNING_STATUS'] == "Stopped") {
				tr += "<td>已停止</td>";
			} else if(one['JOB_RUNNING_STATUS'] == "Fail") {
				tr += "<td style='color:red;'>异常终止</td>";
			} else if(one['JOB_RUNNING_STATUS'] == "Succeded") {
				tr += "<td>正常完成</td>";
			} else {
				tr += "<td></td>";
			}
			tr += "<td>"+getValidValue(one['CITY_NAME'],'')+"</td>";
			tr += "<td>--</td>";
			tr += "<td>"+getValidValue(one['BEG_MEA_TIME'].substr(0,10),'')+"<br/>至<br/>"
								+getValidValue(one['END_MEA_TIME'].substr(0,10),'')+"</td>";
			tr += "<td>"+getValidValue(one['LAUNCH_TIME'],'')+"</td>";
			tr += "<td>"+getValidValue(one['COMPLETE_TIME'],'')+"</td>";
			//排队中
			if(one['JOB_RUNNING_STATUS'] == "Initiate") {
				tr += "<td><input type='button' value='停止' onclick='stopPciTask(\""+one['JOB_ID']+"\",\""+one['MR_JOB_ID']+"\")'/></td>";
			}
			//运行中
			else if(one['JOB_RUNNING_STATUS'] == "Launched"
						|| one['JOB_RUNNING_STATUS'] == "Running") {
				tr += "<td><input type='button' value='停止' onclick='stopPciTask(\""+one['JOB_ID']+"\",\""+one['MR_JOB_ID']+"\")'/> "
					+ "<input type='button' value='查看运行报告' onclick='checkStructureTaskReport(\""+one['JOB_ID']+"\")'/></td>";
			}
			//异常终止
			else if(one['JOB_RUNNING_STATUS'] == "Fail") {
				tr += "<td><input type='button' value='查看运行报告' onclick='checkStructureTaskReport(\""+one['JOB_ID']+"\")'/></td>";
			}
			//正常完成
			else if(one['JOB_RUNNING_STATUS'] == "Succeded") {
				tr += "<td><input type='button' value='下载结果文件' onclick='downloadPciFile(\""+one['JOB_ID']+"\",\""+one['MR_JOB_ID']+"\")'/> "
						+ "<input type='button' value='查看运行报告' onclick='checkStructureTaskReport(\""+one['JOB_ID']+"\")'/></td>";
			}
			//停止中
			else if(one['JOB_RUNNING_STATUS'] == "Stopping") {
				tr += "<td><input type='button' value='查看运行报告' onclick='checkStructureTaskReport(\""+one['JOB_ID']+"\")'/></td>";
			}
			//其他（，已停止）
			else {
				tr += "<td><input type='button' value='下载结果文件' onclick='downloadPciFile(\""+one['JOB_ID']+"\",\""+one['MR_JOB_ID']+"\")'/>"
						 +"<input type='button' value='查看运行报告' onclick='checkStructureTaskReport(\""+one['JOB_ID']+"\")'/></td>";
			}
			tr += "</tr>";
		}
		table.append(tr);

		//设置隐藏的page信息
		setFormPageInfo("structureTaskForm",data['page']);

		//设置分页面板
		setPageView(data['page'],"structureTaskPageDiv");
 	}
}

//查看运行报告
function checkStructureTaskReport(jobId) {
	$("#viewReportForm").find("input#hiddenJobId").val(jobId);
	initFormPage("viewReportForm");
	$("#reportDiv").css("display","block");
	$("#structureTaskDiv").css("display","none");
	$("#renderImgDiv").css("display","none");
	queryReportData();
}
//停止任务
function stopPciTask(jobId,mrJobId) {
	var flag = confirm("是否停止该任务计算？");
	if (flag == false){
	  return;
	}
	showOperTips("loadingDataDiv", "loadContentId", "正在停止任务");
	$.ajax({
		url : 'stop4GAzimuthJobByJobIdForAjaxAction',
		data : {
			'jobId' : jobId,
			'mrJobId' : mrJobId
		},
		dataType : 'text',
		type : 'post',
		success : function(raw) {
			var data;
			try {
				data = eval("(" + raw + ")");
				var flag = data['flag'];
				if(flag) {
					alert("停止操作已提交，请稍后查看停止结果");
					//刷新列表
					sumbitStructureForm();
				} else {
					alert("任务停止失败！");
				}
			} catch (err) {
			}
		},
		complete : function() {
			hideOperTips("loadingDataDiv");
		}
	});
}
//下载结果文件
function downloadPciFile(jobId,mrJobId) {
	var form = $("#downloadPciFileForm");
	form.find("input#jobId").val(jobId);
	form.find("input#mrJobId").val(mrJobId);
	form.submit();
}
//查看渲染图
function viewRenderImg(jobId) {
	//保存jobId用于获取对应的渲染图
	$("#reportNcsTaskId").val(jobId);
	//加载渲染图
	var flag = loadRenderImage();
	if(!flag) {
		return;
	}
	//加载默认渲染规则
	showRendererRuleColor();

	$("#renderImgDiv").css("display","block");
	$("#structureTaskDiv").css("display","none");
	$("#reportDiv").css("display","none");
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
		if(one['STATE'].indexOf("Fail") == -1) {
			html+="<td>"+getValidValue(one['STATE'],'')+"</td>";
		} else {
			html+="<td style='color: red;'>"+getValidValue(one['STATE'],'')+"</td>";
		}
		html+="<td>"+getValidValue(one['ATT_MSG'],'')+"</td>";
		html+="</tr>";
	}
	$("#reportListTab").append(html);
}

/**
 * 从报告的详情返回列表页面
 */
function returnToTaskList(){
	$("#reportDiv").css("display","none");
	$("#structureTaskDiv").css("display","block");
	$("#renderImgDiv").css("display","none");
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

//初始化区域1
function initAreaCascade() {

	$("#provinceId2").change(function() {
		getSubAreas("provinceId2", "cityId2", "市");
	});

	$("#cityId2").change(function() {
		/*getSubAreas("cityId2", "areaId2", "区/县",function(){
			$("#areaId2").append("<option selected='true' value=''>全部</option>");
		});*/
		//以城市为单位创建区域网格
		var cityName = $("#cityId2").find("option:selected").text().trim();
		//获取cityId赋值到表单隐藏域
		var cityId = $("#cityId2").val();
		$("#cityId").val(cityId);
	});
}
/**
 *
 * @title 初始化区域2
 * @author chao.xj
 * @date 2015-4-29下午2:02:27
 * @company 怡创科技
 * @version 1.2
 */
function initAreaCascade2() {

	$("#provinceId3").change(function() {
		getSubAreas("provinceId3", "cityId3", "市");
	});

	$("#cityId3").change(function() {
		/*getSubAreas("cityId2", "areaId2", "区/县",function(){
			$("#areaId2").append("<option selected='true' value=''>全部</option>");
		});*/
		//以城市为单位创建区域网格
		var cityName = $("#cityId3").find("option:selected").text().trim();
		//获取cityId赋值到表单隐藏域
		var cityId = $("#cityId3").val();
		$("#cityId").val(cityId);
	});
}

//计算两个日期差值
function exDateRange(sDate1,sDate2){
  var iDateRange;
  if(sDate1!=""&&sDate2!=""){
    var startDate=sDate1.replace(/-/g,"/");
    var endDate=sDate2.replace(/-/g,"/");
    var S_Date=new Date(Date.parse(startDate));
    var E_Date=new Date(Date.parse(endDate));
    iDateRange=(S_Date-E_Date)/86400000;
    //alert(iDateRange);
  }
  return iDateRange;
}

/**
 *
 * @title js获取当前指定的前几天的日期
 * @param n
 * @returns
 * @author chao.xj
 * @date 2015-4-29下午2:19:21
 * @company 怡创科技
 * @version 1.2
 */
function getBeforeDate(n){
    var n = n;
    var d = new Date();
    var year = d.getFullYear();
    var mon=d.getMonth()+1;
    var day=d.getDate();
    if(day <= n){
            if(mon>1) {
               mon=mon-1;
            }
           else {
             year = year-1;
             mon = 12;
             }
           }
          d.setDate(d.getDate()-n);
          year = d.getFullYear();
          mon=d.getMonth()+1;
          day=d.getDate();
     s = year+"-"+(mon<10?('0'+mon):mon)+"-"+(day<10?('0'+day):day);
     return s;
}
/**
 *
 * @title 提交计算任务
 * @author chao.xj
 * @date 2015-4-29下午2:18:36
 * @company 怡创科技
 * @version 1.2
 */
function submitCalcTask(){
	var isMeet = checkTaskInfoSubmit();
	if(!isMeet){
		return;
	}
	//隐藏窗口
	$('#azimuth_calcWinDiv').hide();
	var cityId = $("#cityId3").val();
	var taskName=$("#_taskName").val();
	var taskDesc=$("#_taskDescription").val();

//	$("#cityIdParam").val($("#cityId3").val());
	//获取窗口城市
	$("#cityId2").val($("#cityId3").val());
	var cityIdParam=$("#cityIdParam").val();
	//获取日期范围
	var sDate = $("#_beginTime").val();
	var eDate = $("#_latestAllowedTime").val();

//	console.log("cityId:"+cityId+"--taskName:"+taskName+"---taskDesc:"+taskDesc+"--sDate:"+sDate+"--eDate:"+eDate);
	showOperTips("loadingDataDiv", "loadContentId", "正在提交LTE网络覆盖分析任务");
	$.ajax({
		url : 'submit4GAzimuthCalcTaskAction',
		data : {
			'taskInfo.CITYID' : cityId,
			'taskInfo.TASKNAME':taskName,
			'taskInfo.TASKDESC':taskDesc,
			'taskInfo.STARTTIME':sDate,
			'taskInfo.ENDTIME':eDate
		},
		type : 'post',
		dataType : 'json',
		success : function(raw) {
			var data = null;
			try {
//				console.log("raw:"+raw);
//				data = eval("(" + raw + ")");
				data=raw;
//				console.log("data:"+data);
				if (data['flag'] == false) {
					alert(data['result']);
				} else {
					alert("任务提交成功，请等待计算完成！");
					//animateInAndOut("operInfo", 500, 500, 1000, "operTip","计算任务提交成功，请稍后查看结果。");
					//location.href="initNcsAnalysisPageAction";
//					$("#init4GAzimuthCalcPageForm").submit();
					getStructureTask();
				}
			} catch (err) {
				console.log("submitCalcTask   err:"+err);
			}
		},
		complete : function() {
			hideOperTips("loadingDataDiv");
		}
	});
}