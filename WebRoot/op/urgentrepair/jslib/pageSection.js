
//工单信息页面
function workOrderInfoSection(woId,divId){
	$("#"+divId).html("");
	$.post("getUrgentRepairWorkOrderInfoAction",{"WOID":woId},function($data){
		$("#"+divId).append($data);
	});
}

//客户工单信息页面
function customerWorkOrderInfoSection(woId,divId){
	$("#"+divId).html("");
	$.post("getUrgentRepairCustomerWorkOrderInfoAction",{"WOID":woId},function($data){
		$("#"+divId).append($data);
	});
}

//2g工单信息页面
function device2gWorkOrderInfoSection(woId,divId){
	$("#"+divId).html("");
	$.post("getUrgentRepairDevice2gWorkOrderInfoAction",{"WOID":woId},function($data){
		$("#"+divId).append($data);
	});
}

//td工单信息页面
function devicetdWorkOrderInfoSection(woId,divId){
	$("#"+divId).html("");
	$.post("getUrgentRepairDevicetdWorkOrderInfoAction",{"WOID":woId},function($data){
		$("#"+divId).append($data);
	});
}

//任务单信息页面
function taskOrderInfoSection(toId,divId){
	$("#"+divId).html("");
	$.post("getUrgentRepairTaskOrderInfoAction",{"TOID":toId},function($data){
		$("#"+divId).append($data);
	});
}	


//工单流转过程树
function workOrderProcedureTreeSection(woId,divId){
	var myUrl = "createWorkOrderProcedureTreeAction";
		$.ajax({
			url : myUrl,
			data:{"WOID":woId},
			dataType : 'json',
			type : 'POST',
			success : function(result) {
				if(result!=null||result!=""){
					createWorkOrderProcedureTree(result,divId,"procedure_Tree","taskPocedureDiv","showWorkOrderProcedure");	
				}
			}
		});
}

//工单流转过程页面
function workOrderProcedureSection(url,woId,divId){
	$("#"+divId).html("");
	$.post(url,{"WOID":woId},function($data){
		$("#"+divId).append($data);
	});
}

//任务单流转过程页面
function taskOrderProcedureSection(url,toId,divId,pageType){
	$("#"+divId).html("");
	$.post(url,{"TOID":toId,"pageType":pageType},function($data){
		$("#"+divId).append($data);
	});
}

//获取数据字典-故障大类
function getFaultGeneraDectionary(id,value){
	var myUrl = "getFaultGeneraDictionaryAction";
	$.ajax({
		url : myUrl,
		data:{"value1":value},
		async: false,
		dataType : 'json',
		type : 'POST',
		success : function(result) {
			$("#"+id).empty();
			var optionStr = "<option value='请选择'>请选择</option>";
			if(result != null){
				$.each(result,function(i,obj){
					optionStr += "<option value='"+obj.id+"'>"+obj.value+"</option>";
				});
				$("#"+id).append(optionStr);
			}
		}	
	});
}

//获取数据字典-故障原因
function getFaultReasonDictionaryByFaultGenera(id,value){
	var myUrl = "getFaultReasonDictionaryByFaultGeneraAction";
	value = encodeURIComponent(value);
	$.ajax({
		url : myUrl,
		data:{"id":value},
		async: false,
		dataType : 'json',
		type : 'POST',
		success : function(result) {
			$("#"+id).empty();
			var optionStr = "<option value='请选择'>请选择</option>";
			if(result != null){
				$.each(result,function(i,obj){
					optionStr += "<option value='"+obj.id+"'>"+obj.value+"</option>";
				});
				$("#"+id).append(optionStr);
			}
		}	
	});
}

//获取数据字典-基站等级
function getBaseStationLevelDictionary(){
	var myUrl = "getBaseStationLevelDictionaryAction";
	$.ajax({
		url : myUrl,
		async: false,
		dataType : 'json',
		type : 'POST',
		success : function(result) {
			$("#baseStationLevel").empty();
			var optionStr = "<option value='请选择'>请选择</option>";
			if(result != null){
				$.each(result,function(i,obj){
					optionStr += "<option value='"+obj+"'>"+obj+"</option>";
				});
				$("#baseStationLevel").append(optionStr);
			}
		}	
	});
}

//获取数据字典-故障类型
function getFaultTypeDictionary(value){
	var myUrl = "getFaultTypeDictionaryAction";
	$.ajax({
		url : myUrl,
		data:{"value1":value},
		async: false,
		dataType : 'json',
		type : 'POST',
		success : function(result) {
			$("#faultType").empty();
			var optionStr = "<option value='请选择'>请选择</option>";
			if(result != null){
				$.each(result,function(i,obj){
					optionStr += "<option value='"+obj+"'>"+obj+"</option>";
				});
				$("#faultType").append(optionStr);
			}
		}	
	});
}

//获取数据字典-故障等级
function getFaultLevelDictionary(){
	var myUrl = "getFaultLevelDictionaryAction";
	$.ajax({
		url : myUrl,
		async: false,
		dataType : 'json',
		type : 'POST',
		success : function(result) {
			$("#faultLevel").empty();
			var optionStr = "<option value='请选择'>请选择</option>";
			if(result != null){
				$.each(result,function(i,obj){
					optionStr += "<option value='"+obj+"'>"+obj+"</option>";
				});
				$("#faultLevel").append(optionStr);
			}
		}	
	});
}


//获取数据字典-受理专业
function getAcceptProfessinalDictionary(value){
	var myUrl = "getAcceptProfessinalDictionaryAction";
	$.ajax({
		url : myUrl,
		data:{"value1":value},
		async: false,
		dataType : 'json',
		type : 'POST',
		success : function(result) {
			$("#acceptProfessional").empty();
			var optionStr = "<option value='请选择'>请选择</option>";
			if(result != null){
				$.each(result,function(i,obj){
					optionStr += "<option value='"+obj+"'>"+obj+"</option>";
				});
				$("#acceptProfessional").append(optionStr);
			}
		}	
	});
}

//获取数据字典-告警逻辑分类
function getAlarmLogicalClassDictionary(){
	var myUrl = "getAlarmLogicalClassDictionaryAction";
	$.ajax({
		url : myUrl,
		async: false,
		dataType : 'json',
		type : 'POST',
		success : function(result) {
			$("#alarmLogicalClass").empty();
			var optionStr = "<option value=''>请选择</option>";
			if(result != null){
				$.each(result,function(i,obj){
					optionStr += "<option value='"+obj+"'>"+obj+"</option>";
				});
				$("#alarmLogicalClass").append(optionStr);
			}
		}	
	});
}

//获取数据字典-告警网管来源
function getAlarmNetManageSourceDictionary(){
	var myUrl = "getAlarmNetManageSourceDictionaryAction";
	$.ajax({
		url : myUrl,
		async: false,
		dataType : 'json',
		type : 'POST',
		success : function(result) {
			$("#alarmNetManageSource").empty();
			var optionStr = "<option value=''>请选择</option>";
			if(result != null){
				$.each(result,function(i,obj){
					optionStr += "<option value='"+obj+"'>"+obj+"</option>";
				});
				$("#alarmNetManageSource").append(optionStr);
			}
		}	
	});
}

//获取数据字典-告警逻辑子类
function getAlarmLogicalSubClassDictionaryByAlarmLogicalClass(value){
	var myUrl = "getAlarmLogicalSubClassDictionaryByAlarmLogicalClassAction";
	value = encodeURIComponent(value);
	$.ajax({
		url : myUrl,
		data:{"referenceValue":value},
		async: false,
		dataType : 'json',
		type : 'POST',
		success : function(result) {
			$("#alarmLogicalSubClass").empty();
			var optionStr = "<option value=''>请选择</option>";
			if(result != null){
				$.each(result,function(i,obj){
					optionStr += "<option value='"+obj+"'>"+obj+"</option>";
				});
				$("#alarmLogicalSubClass").append(optionStr);
			}
		}	
	});
}

//获取数据字典-故障大类
function getFaultGeneraDectionarySHENZHE(selectId,value1,value2){
	var myUrl = "getFaultGeneralDictionarySHENZHEAction";
	$.ajax({
		url : myUrl,
		data:{"value1":value1,"value2":value2},
		async: false,
		dataType : 'json',
		type : 'POST',
		success : function(result) {
			$("#"+selectId).empty();
			var optionStr = "<option value='请选择'>请选择</option>";
			if(result != null){
				$.each(result,function(i,obj){
					optionStr += "<option value='"+obj.id+"'>"+obj.value+"</option>";
				});
				$("#"+selectId).append(optionStr);
			}
		}	
	});
}

//获取数据字典-故障原因
function getFaultReasonDictionaryByFaultGeneraSHENZHE(id,value){
	var myUrl = "getNextTreeNodeDictionarySHENZHENAction";
	$.ajax({
		url : myUrl,
		data:{"id":value},
		async: false,
		dataType : 'json',
		type : 'POST',
		success : function(result) {
			$("#"+id).empty();
			var optionStr = "<option value='请选择'>请选择</option>";
			if(result != null){
				$.each(result,function(i,obj){
					optionStr += "<option value='"+obj.id+"'>"+obj.value+"</option>";
				});
				$("#"+id).append(optionStr);
			}
		}	
	});
}
