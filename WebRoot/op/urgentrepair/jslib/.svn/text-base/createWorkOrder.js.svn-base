var woMoudle001 = new Array();
var woMoudle002 = new Array();
var woMoudle003 = new Array();
var woMoudle004 = new Array();
var woMoudle005 = new Array();
//无线和动力
woMoudle001[0] = "工单主题";
woMoudle001[1] = "工单编号";
woMoudle001[2] = "T1阶段处理时限";
woMoudle001[3] = "T2阶段处理时限";
woMoudle001[4] = "网管告警级别";
woMoudle001[5] = "网元级别";
woMoudle001[6] = "网元名称";
woMoudle001[7] = "中文名称:";
woMoudle001[8] = "级别:";
woMoudle001[9] = "故障发生时间";
woMoudle001[10] = "故障发现方式";
woMoudle001[11] = "告警描述";
woMoudle001[12] = "预处理意见";

//无线和动力--客户工单
woMoudle001[13] = "工单状态";
woMoudle001[14] = "是否紧急故障";
woMoudle001[15] = "派单方式";
woMoudle001[16] = "建单时间";
woMoudle001[17] = "事件的业务影响";
woMoudle001[18] = "事件的设备影响";
woMoudle001[19] = "告警类别";
woMoudle001[20] = "网管告警级别";
woMoudle001[21] = "网元级别";
woMoudle001[22] = "告警网管来源";
woMoudle001[23] = "网元别名";
woMoudle001[24] = "告警逻辑分类";
woMoudle001[25] = "告警逻辑子类";
woMoudle001[26] = "告警关联标识";
woMoudle001[27] = "子告警数量";
woMoudle001[28] = "网络分类";

//TD
woMoudle002[0] = "工单主题";
woMoudle002[1] = "工单编号";
woMoudle002[2] = "T1阶段处理时限";
woMoudle002[3] = "T2阶段处理时限";
woMoudle002[4] = "网管告警级别";
woMoudle002[5] = "网元级别";
woMoudle002[6] = "故障发生时间";
woMoudle002[7] = "故障发现方式";
woMoudle002[8] = "告警描述";
woMoudle002[9] = "预处理意见";

//TD--客户
woMoudle002[10] = "工单状态";
woMoudle002[11] = "是否紧急故障";
woMoudle002[12] = "派单方式 ";
woMoudle002[13] = "网元名称";
woMoudle002[14] = "事件的业务影响";
woMoudle002[15] = "事件的设备影响";
woMoudle002[16] = "告警逻辑分类";
woMoudle002[17] = "告警逻辑子类";
woMoudle002[18] = "告警类别";
woMoudle002[19] = "网管告警级别";
woMoudle002[20] = "网元级别";

//WLAM
woMoudle003[0] = "工单主题";
woMoudle003[1] = "工单编号";
woMoudle003[2] = "T1阶段处理时限";
woMoudle003[3] = "T2阶段处理时限";
woMoudle003[4] = "网管告警级别";
woMoudle003[5] = "网元级别";
woMoudle003[6] = "故障发生时间";
woMoudle003[7] = "故障发现方式";
woMoudle003[8] = "告警描述";
woMoudle003[9] = "预处理意见";
woMoudle003[10] = "告警网元：";
woMoudle003[11] = "告警名称";

//WLAN--客户
woMoudle003[12] = "工单状态";
woMoudle003[13] = "是否紧急故障";
woMoudle003[14] = "派单方式";
woMoudle003[15] = "建单时间";
woMoudle003[16] = "事件的业务影响";
woMoudle003[17] = "事件的设备影响";
woMoudle003[18] = "告警类别";
woMoudle003[19] = "网管告警级别";
woMoudle003[20] = "网元级别";
woMoudle003[21] = "告警网管来源";
woMoudle003[22] = "网元别名";
woMoudle003[23] = "告警逻辑分类";
woMoudle003[24] = "告警逻辑子类";
woMoudle003[25] = "告警关联标识";
woMoudle003[26] = "子告警数量";
woMoudle003[27] = "网络分类";

//移动生产支撑
woMoudle004[0] = "主题";
woMoudle004[1] = "派单人";
woMoudle004[2] = "室内（城中村）覆盖名称";
woMoudle004[3] = "区域";
woMoudle004[4] = "派单时间";
woMoudle004[5] = "紧急程度";
woMoudle004[6] = "处理时限";
woMoudle004[7] = "产品信息";
woMoudle004[8] = "故障说明";
woMoudle004[9] = "备注";

//移动生产支撑--客户工单
woMoudle004[10] = "工单编号";

//网优支撑
woMoudle005[0] = "主题";
woMoudle005[1] = "工单号";
woMoudle005[2] = "直放站名称";
woMoudle005[3] = "告警名称";
woMoudle005[4] = "T1处理时限";
woMoudle005[5] = "T2处理时限";
woMoudle005[6] = "告警时间";
woMoudle005[7] = "建单时间";

woMoudle005[8] = "工单号";



$(document).ready(function() {	
	var area = $("#stationOfArea").val().split("-");
	if(area[1]!=null&&area[1]!=""){
		//获取数据字典-受理专业
		getAcceptProfessinalDictionary(area[1]);
		//获取数据字典-故障类型
		getFaultTypeDictionary(area[1]);
	}
	//获取数据字典-故障等级
	getFaultLevelDictionary();
	//获取数据字典-告警网管来源
	getAlarmNetManageSourceDictionary();
	//获取数据字典-告警逻辑分类
	getAlarmLogicalClassDictionary();
	//获取数据字典-告警逻辑子类
	$("#alarmLogicalClass").change(function(){
		var faultType = $(this).find("option:selected").text();
		getAlarmLogicalSubClassDictionaryByAlarmLogicalClass(faultType);
		
	});

	$("#form1").validate({submitHandler: function(form) { 
		showTip()
		$("#form1").ajaxSubmit({
				dataType:'text',
				async : false,
				success:function(result){
					if(result!=null&&result!=""&&result!="error"){
						window.location.href="loadUrgentRepairWorkOrderPageAction?WOID="+result;
					}else{
						alert("创建工单失败！");
						}
				}
				
			});
		return false;	
}});

	$("#sideeffectService").change(function(){
		if($("#sideeffectService").val()=="1"){
			$("#affectedServiceName").attr("disabled",false);
			$("#affectedServiceName").attr("class","required input-text");
			$("#tip").show();
		}else{
			$("#affectedServiceName").attr("disabled",true);
			$("#affectedServiceName").val("");
			$("#affectedServiceName").attr("class","");
			$("#tip").hide();
		}
	});
//*******************************计算截止时限**************************	
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


//*******************************从GIS进来********************************
	var gis = $("#gis").val();
	if(gis == "gis"){
		var stationId = $("#gis_id").val();
		var stationType = $("#gis_type").val();
		$("#selectStationName").val($("#gis_name").val());
		stationByIdAndType(stationId,stationType,"gis");
		$("#stationSelectButton").attr("disabled","disabled");
	}	
//*******************************从GIS进来********************************	

//*******************************从快速创单入口进来************************************

	var moudleId = $("#moudleId").val();
	var content = $("#content").val();
	if(moudleId == "000"){
	    //工单信息
		var woTitle = content.substring(content.search(woMoudle001[0]), content.search(woMoudle001[1])).replace(woMoudle001[0], "");
		var baseStationName = content.substring(content.search(woMoudle001[7]), content.search(woMoudle001[8])).replace(woMoudle001[7], "");
		var netElementName = content.substring(content.search(woMoudle001[6]), content.search(woMoudle001[16])).replace(woMoudle001[6], "");
		var faultLevel = content.substring(content.search(woMoudle001[4]), content.search(woMoudle001[5])).replace(woMoudle001[4], "");
		var faultOccuredTime = content.substring(content.search(woMoudle001[9]), content.search(woMoudle001[10])).replace(woMoudle001[9], "");
		var latestAllowedTime = content.substring(content.search(woMoudle001[2]), content.search(woMoudle001[3])).replace(woMoudle001[2], "");
		var faultDescription = content.substring(content.search(woMoudle001[11]), content.search(woMoudle001[12])).replace(woMoudle001[11], "");
		var acceptProfessional = "主设备";
		faultLevel = $.trim(faultLevel);
		$("#woTitle").val(woTitle);
		$("#netElementName").val(netElementName);
		$("#faultOccuredTime").val(faultOccuredTime);
		$("#latestAllowedTime").val(latestAllowedTime);
		$("#faultDescription").val(faultDescription);
		
		//转换故障等级
		faultLevel = changeFaultLevel(faultLevel);
		$("#faultLevel").children().each(function(){
			if($(this).val() == faultLevel){
				$(this).attr("selected",true);
			}
		});
		
		
		//客户工单信息	
		var customerWoId  = content.substring(content.search(woMoudle001[1]), content.search(woMoudle001[13])).replace(woMoudle001[1], ""); 
		var isEmergencyFault = content.substring(content.search(woMoudle001[14]), content.search(woMoudle001[15])).replace(woMoudle001[14], "");
		var sendWoWay = content.substring(content.search(woMoudle001[15]), content.search(woMoudle001[6])).replace(woMoudle001[15], "");
		var affectedServiceName = content.substring(content.search(woMoudle001[17]), content.search(woMoudle001[18])).replace(woMoudle001[17], "");
		var alarmClass = content.substring(content.search(woMoudle001[19]), content.search(woMoudle001[20])).replace(woMoudle001[19], "");
		var netmanageAlarmLevel = content.substring(content.search(woMoudle001[20]), content.search(woMoudle001[21])).replace(woMoudle001[20], "");
		var alarmNetManageSource = content.substring(content.search(woMoudle001[22]), content.search(woMoudle001[23])).replace(woMoudle001[22], "");
		var alarmLogicalClass = content.substring(content.search(woMoudle001[24]), content.search(woMoudle001[25])).replace(woMoudle001[24], "");
		var alarmLogicalSubClass = content.substring(content.search(woMoudle001[25]), content.search(woMoudle001[19])).replace(woMoudle001[25], "");
		var alarmAssociatedId = content.substring(content.search(woMoudle001[26]), content.search(woMoudle001[27])).replace(woMoudle001[26], "");
		var subAlarmNumber = content.substring(content.search(woMoudle001[27]), content.search(woMoudle001[28])).replace(woMoudle001[27], "");
		isEmergencyFault = $.trim(isEmergencyFault);
		sendWoWay = $.trim(sendWoWay);
		alarmNetManageSource = $.trim(alarmNetManageSource);
		alarmLogicalClass = $.trim(alarmLogicalClass);
		alarmLogicalSubClass = $.trim(alarmLogicalSubClass);
		$("#customerWoId").val(customerWoId);
		$("#customerWoTitle").val(woTitle);
		$("#isEmergencyFault").val(isEmergencyFault);
		$("#sendWoWay").val(sendWoWay);
		$("#affectedServiceName").val(affectedServiceName);
		$("#netmanageAlarmLevel").val(netmanageAlarmLevel);
		$("#alarmNetManageSource").val(alarmNetManageSource);
		$("#alarmLogicalClass").val(alarmLogicalClass);
		getAlarmLogicalSubClassDictionaryByAlarmLogicalClass(alarmLogicalClass);
		$("#alarmLogicalSubClass").val(alarmLogicalSubClass);
		$("#alarmAssociatedId").val(alarmAssociatedId);
		$("#subAlarmNumber").val(subAlarmNumber);
		$("#subAlarmInfo").val(faultDescription);
		
		
		//反填基站信息
		var array = getBaseStationByBaseStationName(baseStationName);
		if(array[0]){
			$("#gis_name").val(baseStationName);
			$("#gis_id").val(array[0]);
			$("#gis_type").val(array[1]);
			stationByIdAndType(array[0],array[1],"gis");
			var ap = setInterval(function(){
				if($("#acceptProfessional").children().size()>1){
					$("#acceptProfessional").children().each(function(){
						if($(this).val() == acceptProfessional){
							$(this).attr("selected",true);
							clearInterval(ap);
						}
					});
				}
			},50)
			
		}
		
	}else if(moudleId == "001"){
		var woTitle = content.substring(content.search(woMoudle001[0]), content.search(woMoudle001[1])).replace(woMoudle001[0], "");
		var baseStationName = content.substring(content.search(woMoudle001[7]), content.search(woMoudle001[8])).replace(woMoudle001[7], "");
		var netElementName = content.substring(content.search(woMoudle001[6]), content.search(woMoudle001[16])).replace(woMoudle001[6], "");
		var faultLevel = content.substring(content.search(woMoudle001[4]), content.search(woMoudle001[5])).replace(woMoudle001[4], "");
		var faultOccuredTime = content.substring(content.search(woMoudle001[9]), content.search(woMoudle001[10])).replace(woMoudle001[9], "");
		var latestAllowedTime = content.substring(content.search(woMoudle001[2]), content.search(woMoudle001[3])).replace(woMoudle001[2], "");
		var faultDescription = content.substring(content.search(woMoudle001[11]), content.search(woMoudle001[12])).replace(woMoudle001[11], "");
		var acceptProfessional = "动力";
		faultLevel = $.trim(faultLevel);
		$("#woTitle").val(woTitle);
		$("#netElementName").val(netElementName);
		$("#faultOccuredTime").val(faultOccuredTime);
		$("#latestAllowedTime").val(latestAllowedTime);
		$("#faultDescription").val(faultDescription);
		
		//转换故障等级
		faultLevel = changeFaultLevel(faultLevel);
		$("#faultLevel").children().each(function(){
			if($(this).val() == faultLevel){
				$(this).attr("selected",true);
			}
		});
		//反填基站信息
		var customerWoId  = content.substring(content.search(woMoudle001[1]), content.search(woMoudle001[13])).replace(woMoudle001[1], ""); 
		var isEmergencyFault = content.substring(content.search(woMoudle001[14]), content.search(woMoudle001[15])).replace(woMoudle001[14], "");
		var sendWoWay = content.substring(content.search(woMoudle001[15]), content.search(woMoudle001[6])).replace(woMoudle001[15], "");
		var affectedServiceName = content.substring(content.search(woMoudle001[17]), content.search(woMoudle001[18])).replace(woMoudle001[17], "");
		var alarmClass = content.substring(content.search(woMoudle001[19]), content.search(woMoudle001[20])).replace(woMoudle001[19], "");
		var netmanageAlarmLevel = content.substring(content.search(woMoudle001[20]), content.search(woMoudle001[21])).replace(woMoudle001[20], "");
		var alarmNetManageSource = content.substring(content.search(woMoudle001[22]), content.search(woMoudle001[23])).replace(woMoudle001[22], "");
		var alarmLogicalClass = content.substring(content.search(woMoudle001[24]), content.search(woMoudle001[25])).replace(woMoudle001[24], "");
		var alarmLogicalSubClass = content.substring(content.search(woMoudle001[25]), content.search(woMoudle001[19])).replace(woMoudle001[25], "");
		var alarmAssociatedId = content.substring(content.search(woMoudle001[26]), content.search(woMoudle001[27])).replace(woMoudle001[26], "");
		var subAlarmNumber = content.substring(content.search(woMoudle001[27]), content.search(woMoudle001[28])).replace(woMoudle001[27], "");
		isEmergencyFault = $.trim(isEmergencyFault);
		sendWoWay = $.trim(sendWoWay);
		alarmNetManageSource = $.trim(alarmNetManageSource);
		alarmLogicalClass = $.trim(alarmLogicalClass);
		alarmLogicalSubClass = $.trim(alarmLogicalSubClass);
		$("#customerWoId").val(customerWoId);
		$("#customerWoTitle").val(woTitle);
		$("#isEmergencyFault").val(isEmergencyFault);
		$("#sendWoWay").val(sendWoWay);
		$("#affectedServiceName").val(affectedServiceName);
		$("#netmanageAlarmLevel").val(netmanageAlarmLevel);
		$("#alarmNetManageSource").val(alarmNetManageSource);
		$("#alarmLogicalClass").val(alarmLogicalClass);
		getAlarmLogicalSubClassDictionaryByAlarmLogicalClass(alarmLogicalClass);
		$("#alarmLogicalSubClass").val(alarmLogicalSubClass);
		$("#alarmAssociatedId").val(alarmAssociatedId);
		$("#subAlarmNumber").val(subAlarmNumber);
		$("#subAlarmInfo").val(faultDescription);
		
		//反填基站信息
		var array = getBaseStationByBaseStationName(baseStationName);
		if(array[0]){
			$("#gis_name").val(baseStationName);
			$("#gis_id").val(array[0]);
			$("#gis_type").val(array[1]);
			stationByIdAndType(array[0],array[1],"gis");
			var ap = setInterval(function(){
				if($("#acceptProfessional").children().size()>1){
					$("#acceptProfessional").children().each(function(){
						if($(this).val() == acceptProfessional){
							$(this).attr("selected",true);
							clearInterval(ap);
						}
					});
				}
			},50)
		}
	}else if(moudleId == "002"){
		//工单信息
		var woTitle = content.substring(content.search(woMoudle002[0]), content.search(woMoudle002[1])).replace(woMoudle002[0], "");
		var faultLevel = content.substring(content.search(woMoudle002[4]), content.search(woMoudle002[5])).replace(woMoudle002[4], "");
		var faultOccuredTime = content.substring(content.search(woMoudle002[6]), content.search(woMoudle002[7])).replace(woMoudle002[6], "");
		var latestAllowedTime = content.substring(content.search(woMoudle002[2]), content.search(woMoudle002[3])).replace(woMoudle002[2], "");
		var faultDescription = content.substring(content.search(woMoudle002[8]), content.search(woMoudle002[9])).replace(woMoudle002[8], "");
		var acceptProfessional = "TD";
		faultLevel = $.trim(faultLevel);
		$("#woTitle").val(woTitle);
		$("#faultOccuredTime").val(faultOccuredTime);
		$("#latestAllowedTime").val(latestAllowedTime);
		$("#faultDescription").val(faultDescription);
		$("#acceptProfessional").children().each(function(){
			if($(this).val() == acceptProfessional){
				$(this).attr("selected",true);
			}
		});
		//转换故障等级
		faultLevel = changeFaultLevel(faultLevel);
		$("#faultLevel").children().each(function(){
			if($(this).val() == faultLevel){
				$(this).attr("selected",true);
			}
		});
		
		//客户工单信息
		var customerWoId  = content.substring(content.search(woMoudle002[1]), content.search(woMoudle002[10])).replace(woMoudle002[1], ""); 
		var isEmergencyFault = content.substring(content.search(woMoudle002[11]), content.search(woMoudle002[12])).replace(woMoudle002[11], "");
		var sendWoWay = content.substring(content.search(woMoudle002[12]), content.search(woMoudle002[13])).replace(woMoudle002[12], "");
		var affectedServiceName = content.substring(content.search(woMoudle002[14]), content.search(woMoudle002[15])).replace(woMoudle002[14], "");
		var alarmClass = content.substring(content.search(woMoudle002[18]), content.search(woMoudle002[19])).replace(woMoudle002[18], "");
		var netmanageAlarmLevel = content.substring(content.search(woMoudle002[19]), content.search(woMoudle002[20])).replace(woMoudle002[19], "");
		var alarmLogicalClass = content.substring(content.search(woMoudle002[16]), content.search(woMoudle002[17])).replace(woMoudle002[16], "");
		var alarmLogicalSubClass = content.substring(content.search(woMoudle002[17]), content.search(woMoudle002[18])).replace(woMoudle002[17], "");
		isEmergencyFault = $.trim(isEmergencyFault);
		sendWoWay = $.trim(sendWoWay);
		alarmLogicalClass = $.trim(alarmLogicalClass);
		alarmLogicalSubClass = $.trim(alarmLogicalSubClass);
		$("#customerWoId").val(customerWoId);
		$("#customerWoTitle").val(woTitle);
		$("#isEmergencyFault").val(isEmergencyFault);
		$("#sendWoWay").val(sendWoWay);
		$("#affectedServiceName").val(affectedServiceName);
		$("#netmanageAlarmLevel").val(netmanageAlarmLevel);
		$("#alarmLogicalClass").val(alarmLogicalClass);
		getAlarmLogicalSubClassDictionaryByAlarmLogicalClass(alarmLogicalClass);
		$("#alarmLogicalSubClass").val(alarmLogicalSubClass);
		$("#subAlarmInfo").val(faultDescription);
		
	}else if(moudleId == "003"){
		var woTitle = content.substring(content.search(woMoudle003[0]), content.search(woMoudle003[1])).replace(woMoudle003[0], "");
		var baseStationName = content.substring(content.search(woMoudle003[10]), content.search(woMoudle003[11])).replace(woMoudle003[10], "");
		var faultLevel = content.substring(content.search(woMoudle003[4]), content.search(woMoudle003[5])).replace(woMoudle003[4], "");
		var faultOccuredTime = content.substring(content.search(woMoudle003[6]), content.search(woMoudle003[7])).replace(woMoudle003[6], "");
		var latestAllowedTime = content.substring(content.search(woMoudle003[2]), content.search(woMoudle003[3])).replace(woMoudle003[2], "");
		var faultDescription = content.substring(content.search(woMoudle003[8]), content.search(woMoudle003[9])).replace(woMoudle003[8], "");
		faultLevel = $.trim(faultLevel);
		$("#woTitle").val(woTitle);
		$("#faultOccuredTime").val(faultOccuredTime);
		$("#latestAllowedTime").val(latestAllowedTime);
		$("#faultDescription").val(faultDescription);
		//转换故障等级
		faultLevel = changeFaultLevel(faultLevel);
		$("#faultLevel").children().each(function(){
			if($(this).val() == faultLevel){
				$(this).attr("selected",true);
			}
		});
		
		//客户工单信息
		var customerWoId  = content.substring(content.search(woMoudle003[1]), content.search(woMoudle003[12])).replace(woMoudle003[1], ""); 
		var isEmergencyFault = content.substring(content.search(woMoudle003[13]), content.search(woMoudle003[14])).replace(woMoudle003[13], "");
		var sendWoWay = content.substring(content.search(woMoudle003[14]), content.search(woMoudle003[6])).replace(woMoudle003[14], "");
		var affectedServiceName = content.substring(content.search(woMoudle003[16]), content.search(woMoudle003[17])).replace(woMoudle003[16], "");
		var alarmClass = content.substring(content.search(woMoudle003[18]), content.search(woMoudle003[19])).replace(woMoudle003[18], "");
		var netmanageAlarmLevel = content.substring(content.search(woMoudle003[19]), content.search(woMoudle003[20])).replace(woMoudle003[19], "");
		var alarmNetManageSource = content.substring(content.search(woMoudle003[21]), content.search(woMoudle003[22])).replace(woMoudle003[21], "");
		var alarmLogicalClass = content.substring(content.search(woMoudle003[23]), content.search(woMoudle003[24])).replace(woMoudle003[23], "");
		var alarmLogicalSubClass = content.substring(content.search(woMoudle003[24]), content.search(woMoudle003[18])).replace(woMoudle003[24], "");
		var alarmAssociatedId = content.substring(content.search(woMoudle003[25]), content.search(woMoudle003[26])).replace(woMoudle003[25], "");
		var subAlarmNumber = content.substring(content.search(woMoudle003[26]), content.search(woMoudle003[27])).replace(woMoudle003[26], "");
		isEmergencyFault = $.trim(isEmergencyFault);
		sendWoWay = $.trim(sendWoWay);
		alarmNetManageSource = $.trim(alarmNetManageSource);
		alarmLogicalClass = $.trim(alarmLogicalClass);
		alarmLogicalSubClass = $.trim(alarmLogicalSubClass);
		$("#customerWoId").val(customerWoId);
		$("#customerWoTitle").val(woTitle);
		$("#isEmergencyFault").val(isEmergencyFault);
		$("#sendWoWay").val(sendWoWay);
		$("#affectedServiceName").val(affectedServiceName);
		$("#netmanageAlarmLevel").val(netmanageAlarmLevel);
		$("#alarmNetManageSource").val(alarmNetManageSource);
		$("#alarmLogicalClass").val(alarmLogicalClass);
		getAlarmLogicalSubClassDictionaryByAlarmLogicalClass(alarmLogicalClass);
		$("#alarmLogicalSubClass").val(alarmLogicalSubClass);
		$("#alarmAssociatedId").val(alarmAssociatedId);
		$("#subAlarmNumber").val(subAlarmNumber);
		$("#subAlarmInfo").val(faultDescription);
		
		//基站信息反填
		var array = getBaseStationByBaseStationName(baseStationName);
		if(array[0]){
			$("#gis_name").val(baseStationName);
			$("#gis_id").val(array[0]);
			$("#gis_type").val(array[1]);
			stationByIdAndType(array[0],array[1],"gis");
		}
	}else if(moudleId == "004"){
		var woTitle = content.substring(content.search(woMoudle004[0]), content.search(woMoudle004[1])).replace(woMoudle004[0], "");
		var baseStationName = content.substring(content.search(woMoudle004[2]), content.search(woMoudle004[3])).replace(woMoudle004[2], "");
		var faultOccuredTime = content.substring(content.search(woMoudle004[4]), content.search(woMoudle004[5])).replace(woMoudle004[4], "");
		var latestAllowedTime = content.substring(content.search(woMoudle004[6]), content.search(woMoudle004[7])).replace(woMoudle004[6], "");
		var faultDescription = content.substring(content.search(woMoudle004[8]), content.search(woMoudle004[9])).replace(woMoudle004[8], "");
		var acceptProfessional = "室内";
		$("#woTitle").val(woTitle);
		$("#faultOccuredTime").val(faultOccuredTime);
		$("#latestAllowedTime").val(latestAllowedTime);
		$("#faultDescription").val(faultDescription);
		
		
		//客户工单信息	
		var customerWoId  = content.substring(content.search(woMoudle004[10]), content.search(woMoudle004[0])).replace(woMoudle004[10], ""); 
		
		$("#customerWoId").val(customerWoId);
		$("#customerWoTitle").val(woTitle);
		$("#subAlarmInfo").val(faultDescription);
		
		//反填基站信息
		var array = getBaseStationByBaseStationName(baseStationName);
		if(array[0]){
			$("#gis_name").val(baseStationName);
			$("#gis_id").val(array[0]);
			$("#gis_type").val(array[1]);
			stationByIdAndType(array[0],array[1],"gis");
			var ap = setInterval(function(){
				if($("#acceptProfessional").children().size()>1){
					$("#acceptProfessional").children().each(function(){
						if($(this).val() == acceptProfessional){
							$(this).attr("selected",true);
							clearInterval(ap);
						}
					});
				}
			},50)
		}
	}else if(moudleId == "005"){
		var woTitle = content.substring(content.search(woMoudle005[0]), content.search(woMoudle005[1])).replace(woMoudle005[0], "");
		var baseStationName = content.substring(content.search(woMoudle005[2]), content.search(woMoudle005[3])).replace(woMoudle005[2], "");
		var faultOccuredTime = content.substring(content.search(woMoudle005[4]), content.search(woMoudle005[5])).replace(woMoudle005[4], "");
		var latestAllowedTime = content.substring(content.search(woMoudle005[6]), content.search(woMoudle005[7])).replace(woMoudle005[6], "");
		var acceptProfessional = "室内";
		$("#woTitle").val(woTitle);
		$("#faultOccuredTime").val(faultOccuredTime);
		$("#latestAllowedTime").val(latestAllowedTime);
		
		
		//客户工单信息	
		var customerWoId  = content.substring(content.search(woMoudle005[8]), content.search(woMoudle005[0])).replace(woMoudle005[8], ""); 
		
		$("#customerWoId").val(customerWoId);
		$("#customerWoTitle").val(woTitle);
		
		//反填基站信息
		var array = getBaseStationByBaseStationName(baseStationName);
		if(array[0]){
			$("#gis_name").val(baseStationName);
			$("#gis_id").val(array[0]);
			$("#gis_type").val(array[1]);
			stationByIdAndType(array[0],array[1],"gis");
			var ap = setInterval(function(){
				if($("#acceptProfessional").children().size()>1){
					$("#acceptProfessional").children().each(function(){
						if($(this).val() == acceptProfessional){
							$(this).attr("selected",true);
							clearInterval(ap);
						}
					});
				}
			},50)
		}
	}

//*******************************从快速创单入口进来************************************


});

function changeFaultLevel(faultLevel){
	var str;
	if(faultLevel=="一级告警"){
		str = "A1";
	}else if(faultLevel=="二级告警"){
		str = "A2";
	}else if(faultLevel=="三级告警"){
		str = "A3";
	}else if(faultLevel=="四级告警"){
		str = "A4";
	}
	
	return str;
}

var theWindow = null;
function winOpen(pageURL) {
	theWindow = window
			.showModalDialog(
					pageURL,
					window,
					'dialogWidth:700px;dialogHeight:300px;dialogLeft:300px;dialogTop:200px;center:yes;location=no;toolbar=no;help:no;resizable:no;status:no');
}

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

function showTip() {
	var obj = document.getElementById("loading_div");
	if(obj==null||obj==""||obj=="null"||obj==undefined||obj=="undefined"){
		return;
	}
	obj.style.display = "block";
}
