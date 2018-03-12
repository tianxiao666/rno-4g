window.onload = function(){
	var woId = $("#WOID").val();
	var toId = $("#TOID").val();
	var customerWoType = $("#customerWoType").val();
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
	//获取任务单信息
	taskOrderInfoSection(toId,"showTaskOrderInfoDiv");
	//生成工单流转过程树
	workOrderProcedureTreeSection(woId,"procedureTree");
	//点击获取工单服务跟踪记录
	$("#woTraceRecord").click(function(){
		workOrderTracingRecordSection(woId,"woTraceRecordDiv");
	});
	//点击获取任务单服务跟踪记录
	$("#toTraceRecord").click(function(){
		taskOrderTracingRecordSection(toId,"toTraceRecordDiv");
	});
	//点击任务单流转过程刷新信息
	$("#taskProcedure").click(function(){
		var techToId = $("#TOID").val();
		taskOrderProcedureSection("loadTechSupportTaskOrderProcedureAction",techToId,"techProcedureDiv","SECTION");
	});
	
	//模糊查询转派人员
	$(".search_button").click(function(){
		var orgId = $("#curOrgId").val();
		var value = $("#search_value").val();
		fuzzyQueryToSendTeamerList(orgId,value,"toSendDiv");
	});
	
	showResourceApplyMess();
	
	//设施基础信息
	showBaseFacilityByWoIdTable(woId,"netresourceDiv");
	//任务进展图
	taskFlowChart();
	
}

$(document).ready(function() {
	var woId = $("#WOID").val();
	var toId = $("#TOID").val();
	
	var status = $("#STATUS").val();
	if(status=="12"){
		$("input[name='acceptOrReject']").each(function(){
			if($(this).val() == "reject"){
				$(this).attr("checked",true);
			}
			$(this).attr("disabled",true);
		});
	}
	
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
		if($(this).text() == $(".faultReason").val()){
			$(this).attr("selected",true);
		}
	});
		
	//提交阶段回复专家任务
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
	//提交转派专家任务
	$("#toSendForm").validate({submitHandler: function(form) { 
		$("#toSendForm").ajaxSubmit({
			dataType:'text',
			success:function(result){
				if(result=="success"){
					location.reload();
					alert("转派成功！");
				}else{
					alert("转派失败！");
					}
			}
			
		});
		return false;
	}});	
	//提交最终回复
	$("#replySubmit").validate({submitHandler: function(form) { 
		form.submit();
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
		toSendTechTaskTree();
	});
	//回填选中角色
	$("#trans_commit").click(function(){
		var t_teamer = $("input[name='tosend_recipient']:checked").val().split("-");
		$("#recipient").val(t_teamer[0]);
		$("#transpondTeamerName").val(t_teamer[1]);
	});
	
	//受理或驳回
	$("input[name='acceptOrReject']").click(function(){
		if($(this).val()=="accept"){
			$("#acceptForm").attr("action","acceptUrgentRepairTechSupportTaskOrderAction");
			$("#handleCommentText").attr("name","workmanageTaskorder.acceptComment");
		}else{
			$("#acceptForm").attr("action","rejectUrgentRepairTechSupportTaskOrderAction");
			$("#handleCommentText").attr("name","workmanageTaskorder.rejectComment");
		}
	});

	//提交受理/驳回现场任务表单
	$("#acceptForm").validate({submitHandler: function(form) { 
		var flag = isGoOn();
		if(flag){
		 	form.submit();
		}else{
			return false;
		}
	}});

});

//生成转派专家任务组织树
function toSendTechTaskTree(){
	var account = $("#currentHandler").val();
	if(account==null||account==""||account=="undefined"){
		return;
	}
	var values = {"actor":account}
	var myUrl = "createToSendTechSupportTaskOrgTreeAction";
	$.ajax({
		url : myUrl,
		data : values,
		dataType : 'json',
		type : 'POST',
		success : function(result) {
		if(result!=null||result!=""){
			newCreateOrgTree(result,"toSendTree","personnel_tree","toSendDiv","showToSendSpecialistList");	
		}else{
			alert("组织架构树为空！");
		}
		
		}
	});
}

function fuzzyQueryToSendTeamerList(orgId,value,tableId){
	var values = {
				"ORGID" : orgId,
				"search_value":value
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

/**
* 确认是否继续执行
*/
function isGoOn(){
	var msg = "";
	var flag
	$("input[name='acceptOrReject']").each(function(){
		if($(this).attr("checked") == "checked"){
			flag = $(this).val();
		}
	
	});
	if(flag=="accept"){
		msg = "是否受理？";
	}else if(flag=="reject"){
		msg = "是否驳回？";
	}

	
	return confirm(msg);
	
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
		$("#foreseeSolveTime").attr("class","Wdate required input-text");
		$("#handle-title").html("");
		$("#handle-contant").empty();
		$("#handle-title").html("延期申请原因：");
		str +="<textarea class='required {maxlength:1000}' id='reasonForDelay' name='urgentrepairTechsupporttaskorder.reasonForDelayApply' rows='4' style='width: 90%'>"+delay+"</textarea><span class='red'>*</span>";
		$("#handle-contant").append(str);
	} else {
		//解决
		$("#expectSolveTime").hide();
		$("#expectSolveTime2").hide();
		$("#foreseeSolveTime").attr("class","Wdate input-text");
		$("#handle-title").html("");
		$("#handle-contant").empty();
		$("#handle-title").html("故障处理措施：");
		str +="<textarea class='required {maxlength:1000}' id='faultHandle' name='urgentrepairTechsupporttaskorder.faultHandleDetail' rows='4' style='width: 90%'>"+detail+"</textarea><span class='red'>*</span>";
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

//跳转到车辆调度申请单
function showCarApplyPage() {
	var woId = $("#WOID").val();
	var toId = $("#TOID").val();
	var baseStationType =  $("#baseStationType").val();
	var baseStationId =  $("#baseStationId").val();
	var faultLevel =  $("#faultLevel").val();
	var criticalClass = "";
	if (faultLevel == "一级告警") {
		criticalClass = "normal";
	} else {
		criticalClass = "urgent";
	}
	var workType = "urgentrepair"; //抢修

	window.showModalDialog("../cardispatch/cardispatch_applyworkorder.jsp?baseStationType="+baseStationType+"&WOID="+woId+ "&TOID="
			+ toId + "&workType=" + workType + "&baseStationId=" + baseStationId
			+ "&criticalClass="
			+ criticalClass, this, "dialogHeight:548px;dialogWidth:1000px;center:yes;status:no;resizable:no;dialogLeft:200%;dialogTop:200%"); 
			
			
	var sc = showResourceApplyMess();	
	window.opener.eval(sc);
 
}

//显示资源申请记录
function showResourceApplyMess() {
	var toId = $("#TOID").val();
	$.post("cardispatchWorkorder!findApplyWorkorderByToId",{"TOID":toId},function(data){
		$("#applyResource_div").html("");
		var divStr = "";
		divStr += "<tr>";
	 	divStr += "<th></th>";
        divStr += "<th>申请单主题</th>";
        divStr += "<th>申请单类型</th>";
        divStr += "<th>申请资源数量</th>";
        divStr += "<th>申请时间</th>";
        divStr += "<th>申请单状态</th>";
		divStr += "</tr>";
		$(data).each(function(index,value){
			divStr += "<tr class='resourceList'>";
			divStr += "<td><input type='checkbox' value='' class='m_check'/></td>";
			divStr += "<td ><div style='width:490px;word-wrap:break-word;word-break:break-all;overflow:auto;'><a onclick=showCarDetail('"+value.carWoId+"') style='cursor: pointer; color: #0000ff; font-weight: bold;'>"+value.woTitle+"</a></div></td>";
			divStr += "<td>车辆申请</td>";
			divStr += "<td>1</td>";
			divStr += "<td>"+value.createTime+"</td>";
			divStr += "<td>"+value.statusName+"</td>";
			divStr += "</tr>";
		});
		//alert(divStr);
		$("#applyResource_div").html(divStr);
		//工具资产分页
		var list = $(".resourceList");
		var pageSize="5";
		pagingColumnByForeground("resourcePage",list,pageSize);
	},"json");
}
	
//显示车辆调度信息
function showCarDetail(woId) {
	window.open("../cardispatch/cardispatch_lookupworkorder.jsp?WOID="+woId);
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
	if($("#WOSTATUS").val() == "2") {	
		$("#accept_icon").attr("class","list_pic_li list_pic_li_now");
		$("#scene_icon").attr("class","list_pic_li");
		$("#techsupport_icon").attr("class","list_pic_li");
		$("#reply_icon").attr("class","list_pic_li");
	}else if($("#WOSTATUS").val() == "3") {
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
	}else if($("#WOSTATUS").val() == "6"){
		$("#accept_icon").attr("class","list_pic_li list_pic_li_on");
		if(hasTech=="has"){
			$("#scene_icon").attr("class","list_pic_li list_pic_li_on");
			$("#techsupport_icon").attr("class","list_pic_li list_pic_li_now");
		}else{
			$("#scene_icon").attr("class","list_pic_li list_pic_li_now");
			$("#techsupport_icon").attr("class","list_pic_li");
		}
		$("#reply_icon").attr("class","list_pic_li");
	}else if($("#WOSTATUS").val() == "7"){
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