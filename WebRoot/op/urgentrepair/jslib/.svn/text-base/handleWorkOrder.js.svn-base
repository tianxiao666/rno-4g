window.onload=function(){
	var woId = $("#WOID").val();
	var orgId = $("#ORGID").val();
	var customerWoType = $("#customerWoType").val();
	var accountForGIS = $("#accountForGIS").val();
	//获取工单信息
	workOrderInfoSection(woId,"showWorkOrderInfoDiv");	
	//获取客户工单信息
	if(customerWoType=="home2g"){
		device2gWorkOrderInfoSection(woId,"showCustomerWorkOrderInfoDiv");
	}else if(customerWoType=="homeTd"){
		devicetdWorkOrderInfoSection(woId,"showCustomerWorkOrderInfoDiv");
	}else{
		customerWorkOrderInfoSection(woId,"showCustomerWorkOrderInfoDiv");
	}
	
	//点击获取工单服务跟踪记录
	$("#woTraceRecord").click(function(){
		workOrderTracingRecordSection(woId,"woTraceRecordDiv");
	});
	//设施基础信息
	showBaseFacilityByWoIdTable(woId,"netresourceDiv");
	//点击生成工单流转过程树
	$("#woProcedure").click(function(){
		workOrderProcedureTreeSection(woId,"procedureTree");
	});
	
	//生成派发任务组织树
	tree();
	//获取队长列表
	if(orgId!=null&&orgId!=""&&orgId!="undefined"){
		showTeamLeaderForGIS(orgId);
	}
	//GIS入口，默认选中派发对象
	if(accountForGIS!=null&&accountForGIS!=""&&accountForGIS!="undefined"){
		$("input[name='recipient']").each(function(){
			var str = $(this).val().split("-");
			if(str[1]==accountForGIS){
				$(this).attr("checked","checked");
			}
		});
	}
	
	//获取工单下级任务单	
	showChildrenTaskOrder();
	
	//任务进展图
	taskFlowChart();
	
	$("input[name='subTask']:checked").live("click",function(){
		var statusName = $(this).parent().next().next().next().next().next().html();
		$("#cb").removeAttr("disabled");
		$("#cx").removeAttr("disabled");
		$("#cp").removeAttr("disabled");
		if(statusName == "待受理"||statusName == "升级中"){
			$("#cx").attr("disabled",true);
			$("#cp").attr("disabled",true);
		}else if(statusName == "待撤销"){
			$("#cb").attr("disabled",true);
		}else if(statusName == "处理中"||statusName == "已派发"){
			$("#cp").attr("disabled",true);
		}else if(statusName == "已结束"||statusName == "已撤销"){
			$("#cb").attr("disabled",true);
			$("#cx").attr("disabled",true);
			$("#cp").attr("disabled",true);
		}
		
	});
	
	
	
}


$(document).ready(function() {
	var woId = $("#WOID").val();
	//*******************************计算截止时限**************************
		var area = $("#stationOfArea").val().split("-");
		if(area[1]!=null&&area[1]!=""){
			//获取数据字典-受理专业
			getAcceptProfessinalDictionary(area[1]);
			//获取数据字典-故障类型
			getFaultTypeDictionary(area[1]);
		}
		//获取数据字典-故障等级
		getFaultLevelDictionary();
		
		$("#timePlug").click(function(){
			$("#faultOccuredTime").focus();
		});
		$("#ct").click(function(){
			$("#faultOccuredTime").focus();
		});
		
		$("#baseStationLevel").change(function(){
			$("#latestAllowedTime").val("");
			timeArithmetic();
		});
		$("#faultLevel").change(function(){
			$("#latestAllowedTime").val("");
			timeArithmetic();
		});
		
		$("#faultOccuredTime").blur(function(){
			$("#latestAllowedTime").val("");
			timeArithmetic();
		});
		//*******************************计算截止时限**************************	
	//修改工单
	//返回按钮隐藏
		$("#back_button").hide();
		//保存按钮隐藏
		$("#save_button").hide();
		//点击修改
		$("#modify_button").click(function(){
			$("#back_button").show();
			$("#save_button").show();
			$("#showUpdateOrder").show();
			$("#showWorkOrderInfoDiv").hide();
			var acceptProfessional =$("#acceptProfessional_value").val()
			var faultType = $("#faultType_value").val();
			var faultLevel =$("#faultLevel_value").val();
			$("#acceptProfessional").children().each(function(){
				if($(this).text() == acceptProfessional){
					$(this).attr("selected",true);
				}
			});
			$("#faultType").children().each(function(){
				if($(this).text() == faultType){
					$(this).attr("selected",true);
				}
			});
			$("#faultLevel").children().each(function(){
				if($(this).text() == faultLevel){
					$(this).attr("selected",true);
				}
			});
			
				$("#showOrderInfo").hide();
				$(this).hide();
		});
		
		//点击返回
		$("#back_button").click(function(){
			$("#modify_button").show();
			$("#save_button").hide();
			$("#showUpdateOrder").hide();
			$("#showWorkOrderInfoDiv").show();
			$(this).hide();
		});
	
	//是否影响业务
	var reply_sideeffectService = $("#reply_sideeffectService").val();
	if (reply_sideeffectService == "1") {
		$("#sideeffectY").attr("checked", true);
		$("#sideeffectN").removeAttr("checked");
	} else if (reply_sideeffectService == "0") {
		$("#sideeffectN").attr("checked", true);
		$("#sideeffectY").removeAttr("checked");
	}
	affectBiz();

	//告警是否清除
	var reply_isAlarmClear = $("#reply_isAlarmClear").val();
	if (reply_isAlarmClear == "1") {
		$("#isAlarmClearY").attr("checked", true);
		$("#isAlarmClearN").removeAttr("checked");
	} else if (reply_isAlarmClear == "0") {
		$("#isAlarmClearN").attr("checked", true);
		$("#isAlarmClearY").removeAttr("checked");
	} 
	isClear();

	//故障处理结果
	var reply_faultDealResult = $("#reply_faultDealResult").val();
	if (reply_faultDealResult == "1") {
		$("#done").attr("checked", true);
		$("#reasonForDelayApply").removeAttr("checked");
	} else if (reply_faultDealResult == "0") {
		$("#reasonForDelayApply").attr("checked", true);
		$("#done").removeAttr("checked");
	}
	delayApply();
	
	
	var acceptProfessional = $("#acceptProfessional_value").val();
	var customerWoType = $("#customerWoType").val();
	if(customerWoType == "homeTd"){
		if(acceptProfessional!=null&&acceptProfessional!=""){
			//获取故障大类
			getFaultGeneraDectionarySHENZHE("stepReply_faultType","TD",acceptProfessional);
			getFaultGeneraDectionarySHENZHE("reply_faultType","TD",acceptProfessional);
		}
		
	}else if(customerWoType == "home2g"){
		if(acceptProfessional!=null&&acceptProfessional!=""){
			//获取故障大类
			getFaultGeneraDectionarySHENZHE("stepReply_faultType","2G",acceptProfessional);
			getFaultGeneraDectionarySHENZHE("reply_faultType","2G",acceptProfessional);
		}
	}else{
		//非深圳网优之家
		var city = $("#city").val();
		//获取故障大类
		getFaultGeneraDectionary("stepReply_faultType",city);
		//获取故障大类
		getFaultGeneraDectionary("reply_faultType",city);
	}
	
	
	
	//阶段回复，故障大类级联故障原因
	$("#stepReply_faultType").change(function(){
		var faultType = $(this).find("option:selected").val();
		if(customerWoType == "homeTd"){
			if(acceptProfessional!=null&&acceptProfessional!=""){
				getFaultReasonDictionaryByFaultGeneraSHENZHE("stepReply_faultType",faultType);
			}
			
		}else if(customerWoType == "home2g"){
			if(acceptProfessional!=null&&acceptProfessional!=""){
				getFaultReasonDictionaryByFaultGeneraSHENZHE("stepReply_faultResult",faultType);
			}
		}else{
			getFaultReasonDictionaryByFaultGenera("stepReply_faultResult",faultType);
		}
		
	});
	
	//最终回复，故障大类级联故障原因
	$("#reply_faultType").change(function(){
		var faultType = $(this).find("option:selected").val();
		if(customerWoType == "homeTd"){
			if(acceptProfessional!=null&&acceptProfessional!=""){
				getFaultReasonDictionaryByFaultGeneraSHENZHE("reply_faultResult",faultType);
			}
			
		}else if(customerWoType == "home2g"){
			if(acceptProfessional!=null&&acceptProfessional!=""){
				getFaultReasonDictionaryByFaultGeneraSHENZHE("reply_faultResult",faultType);
			}
		}else{
			getFaultReasonDictionaryByFaultGenera("reply_faultResult",faultType);
		}
		
	});
	//故障大类 默认选中
	$("#reply_faultType").children().each(function(){
		var faultType = $(this).text();
		if($(this).text() == $(".faultGenera").val()){
			$(this).attr("selected",true);
			getFaultReasonDictionaryByFaultGenera("reply_faultResult",$(this).val());
		}
	});
	
	//故障原因 默认选中
	$("#reply_faultResult").children().each(function(){
		if($(this).text() == $(".faultCause").val()){
			$(this).attr("selected",true);
		}
	});
	
	//提交派发现场任务表单
	$("#assignForm").validate({submitHandler: function(form) { 
		var flag=judgeCheckboxIsSelected();
		if(flag){
			$("#assignForm").ajaxSubmit({
				dataType:'text',
				success:function(result){
					if(result=="success"){
						//showChildrenTaskOrder();
						//$(".paifa_handle_content").hide();
						alert("派发成功！");
						location.reload();
					}else{
						alert("派发失败！");
						}
				}
				
			});
			return false;
		}else{
			alert("请选择派发对象！");
			return false;
		}
	}});
	
	//提交修改工单
	$("#updateWorkOrdeForm").validate({submitHandler: function(form) { 
		$("#black").show();
		$("#updateWorkOrdeForm").ajaxSubmit({
				dataType:'text',
				success:function(result){
					if(result=="success"){
						window.location.href="loadUrgentRepairWorkOrderPageAction?WOID="+woId;
					}else{
						alert("修改工单失败！");
						}
				}
				
			});
		return false;	
	}});
	
	//提交阶段回复工单
	$("#stepReplyForm").validate({submitHandler: function(form) { 
		$("#stepReplyForm").ajaxSubmit({
			dataType:'text',
			success:function(result){
				if(result=="success"){
					alert("回复成功！");
				}else if(result=="fail"){
					alert("回复失败！");
				}else if(result=="VPNFail"){
					alert("网优之家接口调用失败，请手工登录【网优之家】进行工单阶段回复");
				}	
			}
			
		});
		return false;
	}});
	
	//受理工单
	$("#acceptForm").validate({submitHandler: function(form) {
		$("#black").show();
		$("#acceptForm").ajaxSubmit({
				dataType:'json',
				success:function(obj){
					var host = window.location.host;
					if(obj.GIS=="GIS"){
						if(obj.result=="success"){
							window.location.href = "http://"+host+"ops/op/urgentrepair/loadUrgentRepairWorkOrderPageAction?WOID="+obj.woId+"&accountForGIS="+obj.accountForGIS+"&entryType="+obj.entryType;
						}else if(obj.result=="fail"){
							alert("受理失败");
							$("#black").hide();
						}else if(obj.result=="VPNFail"){
							alert("网优之家接口调用失败，请手工登录【网优之家】进行工单签收");
							window.location.href = "http://"+host+"ops/op/urgentrepair/loadUrgentRepairWorkOrderPageAction?WOID="+obj.woId+"&accountForGIS="+obj.accountForGIS+"&entryType="+obj.entryType;
						}	
					}else{
						if(obj.result=="success"){
							location.reload();
						}else if(obj.result=="fail"){
							alert("受理失败");
						}else if(obj.result=="VPNFail"){
							alert("网优之家接口调用失败，请手工登录【网优之家】进行工单签收");
							location.reload();
						}
					}
				}
				
			});
			return false;
		}});
	
	//提交转派工单
	$("#toSendForm").validate({submitHandler: function(form) { 
		$("#toSendForm").ajaxSubmit({
			dataType:'text',
			success:function(result){
				if(result=="success"){
					alert("转派成功！");
				}else{
					alert("转派失败！");
					}
			}
			
		});
		return false;
	}});
	//转派
	//选人
	$(".select_peo").click(function(){
		$(".alert_select").show();
	});
	$(".alert_select_hide").click(function(){
		$(".alert_select").hide();
	});
	//转派，选择派发对象
	$(".select_peo").click(function(){
		toSendWorkOrderOrgTree();
	});
	//回填选中角色
	$("#trans_commit").click(function(){
		var t_teamer = $("input[name='tosend_recipient']:checked").val().split("-");
		$("#recipient").val(t_teamer[0]);
		$("#transpondTeamerName").val(t_teamer[1]);
	});
	
	//提交撤销任务单
	$("#cancelForm").validate({submitHandler: function(form) { 
		var flag = selectSubTask();
		if(flag){
			$("#cancelForm").ajaxSubmit({
				dataType:'text',
				success:function(result){
					if(result=="success"){
						location.reload();
						alert("撤销成功！");
					}else{
						alert("撤销失败！");
						}
				}
				
			});
		}
		return false;
	}});
	
	//提交重派任务单
	$("#reAssignForm").validate({submitHandler: function(form) { 
		var flag = selectSubTask();
		if(flag){
			$("#reAssignForm").ajaxSubmit({
				dataType:'text',
				success:function(result){
					if(result=="success"){
						showChildrenTaskOrder();
						alert("重派成功！");
					}else{
						alert("重派失败！");
						}
				}
				
			});
		}
		return false;
	}});
	
	//提交催办任务单
	$("#hastenForm").validate({submitHandler: function(form) { 
		var flag = selectSubTask();
		if(flag){
			$("#hastenForm").ajaxSubmit({
				dataType:'text',
				success:function(result){
					if(result=="success"){
						alert("催办成功！");
						$("#hastenDiv").hide();
					}else{
						alert("催办失败！");
						}
				}
				
			});
		}
		return false;
	}});
	
	//提交最终回复工单
	$("#replyForm").validate({submitHandler: function(form) { 
		var flag = checkAllSubTaskIsFinishedForWorkOrder();
		if(flag == "success"){
			$("#replyForm").ajaxSubmit({
				dataType:'text',
				success:function(result){
					if(result=="success"){
						location.reload();
					}else if(result=="fail"){
						alert("回复失败");
					}else if(result=="VPNFail"){
						alert("网优之家接口调用失败，请手工登录【网优之家】进行工单最终回复");
						location.reload();
					}
				}
				
			});
		}else{
			if(confirm("你还有下级任务没有结束，是否确定现在回复工单？<br>如若回复工单，系统将自动回复并关闭该工单下的所有下级任务！")){
				$("#replyForm").ajaxSubmit({
				dataType:'text',
				success:function(result){
					if(result=="success"){
						location.reload();
					}else if(result=="fail"){
						alert("回复失败");
					}else if(result=="VPNFail"){
						alert("网优之家接口调用失败，请手工登录【网优之家】进行工单最终回复");
						location.reload();
					}
				}
				
			});
			}
		}
		return false;
	}});
		
});


//截止时间算法
function timeArithmetic(){
		//获取行政区域
		var area = $("#stationOfArea").val().split("-");
		var city = "";
		if(area[1]!=null&&area[1]!=""){
			city = area[1];
		}
		var faultOccuredTime = $("#faultOccuredTime").val();
		var oldFaultOccuredTime = $("#oldFaultOccuredTime").val();
		var baseStationLevel = $("#baseStationLevel").val();
		var faultLevel = $("#faultLevel").val();
		if(faultOccuredTime !=""&&baseStationLevel!="请选择"&&faultLevel!="请选择"&&baseStationLevel!=""&&faultLevel!=""){
			$("#oldFaultOccuredTime").val(faultOccuredTime);
			var val=0
			if(city=="长沙市"){
				if(baseStationLevel=="普通"){
					if(faultLevel=="A1"){
						val=5;
					}else if(faultLevel=="A2"){
						val=8;
					}else if(faultLevel=="A3"){
						val=24;
					}
				}else if(baseStationLevel=="VIP"){
					if(faultLevel=="A1"){
						val=2;
					}else if(faultLevel=="A2"){
						val=8;
					}else if(faultLevel=="A3"){
						val=24;
					}
				}
			}else{
				if(baseStationLevel=="普通"){
					if(faultLevel=="A1"){
						val=6;
					}else if(faultLevel=="A2"){
						val=12;
					}else if(faultLevel=="A3"){
						val=24;
					}
				}else if(baseStationLevel=="VIP"){
					if(faultLevel=="A1"){
						val=4;
					}else if(faultLevel=="A2"){
						val=8;
					}else if(faultLevel=="A3"){
						val=16;
					}
				}
			}
			
			var faultOccuredTime = faultOccuredTime.replace(/\-/g,"/");

			var dateTime = new Date(faultOccuredTime);
			dateTime.setHours(dateTime.getHours()+val);

			var m = dateTime.getMonth()+1;
			var d = dateTime.getDate();
			var h = dateTime.getHours();
			var miu = dateTime.getMinutes();
			var s = dateTime.getSeconds();
			if(m<10){
				m = "0"+m;
			}
			if(d<10){
				d = "0"+d;
			}
			if(h<10){
				h = "0"+h;
			}
			if(miu<10){
				miu = "0"+miu;
			}
			if(s<10){
				s = "0"+s;
			}
			var allowTime = dateTime.getFullYear()+"-"+m+"-"+d+" "+h+":"+miu+":"+s;
			
			$("#latestAllowedTime").val(allowTime);
			
		}
}	

//生成派发任务组织树
function tree(){
	var account = $("#currentHandler").val();
	if(account==null||account==""||account=="undefined"){
		return;
	}
	var values = {"account":account}
	var myUrl = "../organization/getOrgTreeDownwardByAccountAction";
	$.ajax({
		url : myUrl,
		data : values,
		dataType : 'json',
		type : 'POST',
		success : function(result) {
		if(result!=null||result!=""){//yuan.yw  xiugai
			newCreateOrgTree(result,"treeDiv","personnel_tree","peopleList","showTeamLeader");	
		}else{
			alert("组织架构树为空！");
		}
		
		}
	});
}

//生成转派工单组织树
function toSendWorkOrderOrgTree(){
	var account = $("#currentHandler").val();
	if(account==null||account==""||account=="undefined"){
		return;
	}
	var values = {"actor":account}
	var myUrl = "createToSendWorkOrderOrgTreeAction";
	$.ajax({
		url : myUrl,
		data : values,
		dataType : 'json',
		type : 'POST',
		success : function(result) {
		if(result!=null||result!=""){
			newCreateOrgTree(result,"toSendTree","toSend_tree","toSendDiv","showToSendDispatcherList");	
		}else{
			alert("组织架构树为空！");
		}
		
		}
	});
}

//获取工单下级任务单	
function showChildrenTaskOrder(){
	var woId = $("#WOID").val();
	if(woId==null||woId==""){
		return;
	}
	var values = {"WOID":woId}
	var myUrl = "getWorkOrderChildrenTaskOrderAction";
	$.ajax({
		url : myUrl,
		data : values,
		dataType : 'json',
		type : 'POST',
		success : function(objs) {
			if (objs != null) {
				$("#subtasktable").html("");
				var htmlString = "<tr><th></th><th>下级任务名</th><th>派发时间</th><th>处理人</th><th>任务处理截止时间</th><th>状态</th></tr>";
				if (objs.length >= 1) {
					for ( var i = 0; i < objs.length; i++) {
						
						var obj = objs[i];
						var overtime = "";
						if (obj['requireCompleteTime'] != undefined) {
							overtime = obj['requireCompleteTime'];
						}
						if (obj['toId'] != undefined) {
							var toId = obj['toId'];
							var toTitle = obj['toTitle'];
							var statusName = obj['statusName'];
							var assignTime = obj['assignTime'];
							var orderType = obj['bizProcessName'];
							//if(statusName == "待撤销"){
								htmlString += "<tr><td><input type='radio' name='subTask' value='"+toId+"&"+obj['currentHandler']+"' id='cboxid"
							//}else{
							//	htmlString += "<tr><td><input type='checkbox' disabled name='subTask' value='"+toId+"&"+obj['currentHandler']+"' id='cboxid"
							//}
							htmlString += i
									+ "'/></td><td><div style='width:490px;word-wrap:break-word;word-break:break-all;overflow:auto;'><a onclick=showTaskOrderPage('"+toId+"') target='_blank' style='cursor: pointer; color: #0000ff; font-weight: bold'>"
									+ toTitle
									+ "</a></div></td><td>"
									+ assignTime
									+ "</td><td>"
									+ obj['currentHandlerName']
									+ "</td><td>"
									+ overtime
									+ "</td><td>"
									+ statusName + "</td></tr>";
						}
					}
				}
				$("#subtasktable").append(htmlString);
			}
		
		}
	});
}	
	
/**
* 判断是否已经选择派发人员
*/
function judgeCheckboxIsSelected(){
	var flag = false;
	$("input[@type=checkbox][name='recipient']").each( 
	function(){ 	
	if($(this).attr("checked")){
	flag = true;
	return ;
	} 
	});
	return flag;
}

//展现任务单信息
function showTaskOrderPage(toId) {
	window.open("loadSenceTaskOrderProcedureAction?TOID="+toId+"&pageType=PAGE");	
}

//任务管理检查选择下级任务
function selectSubTask(){
	//检查下级任务是否有选中
		var flag = false;
		var subToId = "";
		$("input[name='subTask']").each(function(){
			if($(this).attr("checked")=="checked"){
				flag = true;
				var temp = $(this).val().split("&");
				if(subToId == ""){
					subToId += temp[0];
				}else{
					subToId += "&"+temp[0];
				}
			}
		});
		
		if(flag){
			$(".subToIds").val(subToId);
		}else{
			alert("请选择需要处理的下级任务！");
		}
		return flag;
}

//检查工单的下级任务是否全部结束
function checkAllSubTaskIsFinishedForWorkOrder(){
	var woId = $("#WOID").val();
	if(woId==null||woId==""){
		return;
	}
	var flag ="";
	var values = {"WOID":woId}
	var myUrl = "checkAllSubTaskIsFinishedForWorkOrderAction";
	$.ajax({
		url : myUrl,
		data : values,
		async: false,
		dataType : 'text',
		type : 'POST',
		success : function(result) {
			if(result == "success" ){
				flag =  "success";
			}else{
				flag =  "error";
			}
		}	
	});
	return flag;
}

//控制是否显示网管告警清除时间文本框
function isClear(){
	if($("#isAlarmClearY").attr("checked")=="checked"){
			$("#clearTime").show();
			$("#alarmClearTime").attr("lessThanNowTime","#nowTime");
	}else if($("#isAlarmClearN").attr("checked")=="checked"){
		$("#clearTime").hide();
		$("#alarmClearTime").removeAttr("lessThanNowTime");
	}
}

//控制是否显示 申请延期原因文本框
function delayApply() {
	var str="";
	var detail = $("#handle-detail").val();
	var delay = $("#handle-delay").val();
		//申请延期
	if (document.getElementById("reasonForDelayApply").checked == true) {
		$("#expectSolveTime").show();
		$("#expectSolveTime2").show();
		$("#foreseeResolveTime").attr("class","Wdate required input-text");
		$("#handle-title").html("");
		$("#handle-contant").empty();
		$("#handle-title").html("延期申请原因：");
		str +="<textarea class='required input-text {maxlength:1000}' id='reasonForDelay' name='urgentrepairWorkorder.resonForDelayApply' rows='4' style='width: 90%'>"+delay+"</textarea><span class='red'>*</span>";
		$("#handle-contant").append(str);
	} else {
		//解决
		$("#expectSolveTime").hide();
		$("#expectSolveTime2").hide();
		$("#foreseeResolveTime").attr("class","Wdate input-text");
		$("#handle-title").html("");
		$("#handle-contant").empty();
		$("#handle-title").html("故障处理措施：");
		str +="<textarea class='required input-text {maxlength:1000}' id='howToDeal' name='urgentrepairWorkorder.howToDeal' rows='4' style='width: 90%'>"+detail+"</textarea><span class='red'>*</span>";
		$("#handle-contant").append(str);
	}

}

//控制受影响业务的显示
function affectBiz() {
	if ($("#sideeffectY").attr("checked") =="checked"){
		$("#affectedServiceName").show();
	}else{
		$("#affectedServiceName").hide();
	}
}

//获取队长
function showTeamLeaderForGIS(orgId){
var values = {
			"ORGID" : orgId
		}
var myUrl = "loadTeamLeadersInfoAction";	
$.ajax({
		url : myUrl,
		data : values,
		async:false,
		dataType : 'json',
		type : 'POST',
		success : function(result) {
		$("#peopleList").html("");
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
						var htmlString =htmlString+ "<tr><td ><input type='radio' name='recipient' value='"+result[i].teamId+"-"+result[i].accountId+"'/>"+result[i].teamName+"</td><td class='tl' style='text-indent:4em'>"+result[i].teamleader+" "+result[i].phone+"</td><td class='tc'><em class='is_sb' >"+result[i].dutyStatus+"</em></td><td><em class='is_sb' onclick=getStaffTaskInfoForGantt('"+result[i].accountId+"',this)><a id='g"+i+"' class='showGantt_a'>"+result[i].sum+"</a></em></td></tr>";
					}else{
						var htmlString =htmlString+ "<tr><td ><input type='radio' name='recipient' value='"+result[i].teamId+"-"+result[i].accountId+"'/>"+result[i].teamName+"</td><td class='tl' style='text-indent:4em'>"+result[i].teamleader+" "+result[i].phone+"</td><td class='tc'><em class='is_sb'>"+result[i].dutyStatus+"</em></td><td><em class='not_sb' onclick=getStaffTaskInfoForGantt('"+result[i].accountId+"',this)><a id='g"+i+"' class='showGantt_a'>"+result[i].sum+"</a></em></td></tr>";
					}
					
				}else{
					if(result[i].sum == 0){
						var htmlString =htmlString+ "<tr><td ><input type='radio' name='recipient' value='"+result[i].teamId+"-"+result[i].accountId+"'/>"+result[i].teamName+"</td><td class='tl' style='text-indent:4em'>"+result[i].teamleader+" "+result[i].phone+"</td> <td class='tc'><em class='not_sb'>"+result[i].dutyStatus+"</em></td><td><em class='is_sb' onclick=getStaffTaskInfoForGantt('"+result[i].accountId+"',this)><a id='g"+i+"' class='showGantt_a'>"+result[i].sum+"</a></em></td></tr>";
					}else{
						var htmlString =htmlString+ "<tr><td ><input type='radio' name='recipient' value='"+result[i].teamId+"-"+result[i].accountId+"'/>"+result[i].teamName+"</td><td class='tl' style='text-indent:4em'>"+result[i].teamleader+" "+result[i].phone+"</td> <td class='tc'><em class='not_sb'>"+result[i].dutyStatus+"</em></td><td><em class='not_sb' onclick=getStaffTaskInfoForGantt('"+result[i].accountId+"',this)><a id='g"+i+"' class='showGantt_a'>"+result[i].sum+"</a></em></td></tr>";
					}
					
				}
				
			}
			$("#peopleList").append(htmlString);
		}else{
			$("#peopleList").append(htmlString);
		} 
		}
		
	});
}
/*甘特图*/
function showResourceMonthGantt(){
	if($(".urDatepicker").prev().val()){
		var staffId = $(".urDatepicker").prev().val();
		showResourceMonthGanttByConditions(".urDatepicker",staffId,"people");
	}
}
//按条件获取月份任务
function showResourceMonthGanttByConditions(content,resourceId,resourceType){
	var year = $(content+" .ui-datepicker-year").html().replace("年","-");
	var month = $(content+" .ui-datepicker-month").html().replace("月","-");
	var taskDate = year+month;
	var endDate = "30";
	$(content+" .ui-datepicker-calendar tbody td a").each(function(){
		endDate = $(this).html();
	});
	$.ajax({
		url : "getResourceMonthTaskAction",
		data : {"taskDate":taskDate,"endDate":endDate,"resourceId":resourceId,"resourceType":resourceType},
		dataType : 'json',
		async:false,
		type : 'POST',
		success : function(result){
			if(result){
				$(content+" .ui-datepicker-calendar tbody td a").each(function(){
					var i = $(this).html();
					var ti = result[i];
					var hasTask = ti.hasTask;
					// 有无任务颜色显示不同，有任务为红色，无任务为绿色
					var cs = "bg_green";
					if(hasTask&&hasTask=='true'){
						cs = "bg_red";
						$(this).attr("class",cs);
					}
				});
			}
		}
	});
}

/**
 * 人员任务数
 */
function getStaffTaskInfoForGantt(staffId,be){
	createGanttContent("ctGantt","ctGanttContent","urDatepicker",staffId,"people",null);
	var offset = $(be).offset();
	$("#ctGantt").css("top",offset.top + 20 + "px");
	$("#ctGantt").css("left",offset.left - 50 + "px");
	$("#ctGantt").show();
	$("#ctGantt").hover(function(){},function(){
		$(this).hide();
	});
}

//任务流转图
function taskFlowChart(){
	var hasScene = $("#hasSceneTask").val();
	var hasTech = $("#hasTechTask").val();
	$("#accept_icon").removeAttr("class");
	$("#scene_icon").removeAttr("class");
	$("#techsupport_icon").removeAttr("class");
	$("#reply_icon").removeAttr("class");
	//	根据工单状态控制
	if($("#STATUS").val() == "2") {	
		$("#accept_icon").attr("class","list_pic_li list_pic_li_now");
		$("#scene_icon").attr("class","list_pic_li");
		$("#techsupport_icon").attr("class","list_pic_li");
		$("#reply_icon").attr("class","list_pic_li");
	}else if($("#STATUS").val() == "3") {
		$("#accept_icon").attr("class","list_pic_li list_pic_li_on");
		if(hasScene=="has"){
			$("#scene_icon").attr("class","list_pic_li list_pic_li_on");
		}else{
			$("#scene_icon").attr("class","list_pic_li");
		}
		if(hasTech=="has"){
			$("#techsupport_icon").attr("class","list_pic_li list_pic_li_on");
		}else{
			$("#techsupport_icon").attr("class","list_pic_li");
		}
		$("#reply_icon").attr("class","list_pic_li list_pic_li_now");
	}else if($("#STATUS").val() == "6"){
		$("#accept_icon").attr("class","list_pic_li list_pic_li_on");
		if(hasTech=="has"){
			$("#scene_icon").attr("class","list_pic_li list_pic_li_on");
			$("#techsupport_icon").attr("class","list_pic_li list_pic_li_now");
		}else{
			$("#scene_icon").attr("class","list_pic_li list_pic_li_now");
			$("#techsupport_icon").attr("class","list_pic_li");
		}
		$("#reply_icon").attr("class","list_pic_li");
	}else if($("#STATUS").val() == "7"){
		$("#accept_icon").attr("class","list_pic_li list_pic_li_on");
		if(hasScene=="has"){
			$("#scene_icon").attr("class","list_pic_li list_pic_li_on");
		}else{
			$("#scene_icon").attr("class","list_pic_li");
		}
		if(hasTech=="has"){
			$("#techsupport_icon").attr("class","list_pic_li list_pic_li_on");
		}else{
			$("#techsupport_icon").attr("class","list_pic_li");
		}
		$("#reply_icon").attr("class","list_pic_li list_pic_li_on");
	}
}
	