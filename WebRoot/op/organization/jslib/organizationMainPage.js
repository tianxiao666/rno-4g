$(document).ready(function(){
	//显示-隐藏区域树 yuan.yw
	$("#area_server_choice_btn").click(function(){
		$("#area_server_Div").slideToggle("fast");
	}); 
	//显示-隐藏区域树 yuan.yw
	$("#org_c_area_server_choice_btn").click(function(){
		$("#org_c_area_server_Div").slideToggle("fast");
	}); 
	//显示-隐藏区域树 yuan.yw
	$("#org_p_area_server_choice_btn").click(function(){
		$("#org_p_area_server_Div").slideToggle("fast");
	}); 
	$(".add_root_architecture").hide();
	var isLagal = "false";
	var enterpriseId = "";
	var isCoo = "";
	$.ajax({
		url: "getLoginIdBelongEnterpriseTypeAjaxAction",
		async:false,
		type:"POST",
		dataType : 'json',
		success : function(result) {
			if(result){
				isLagal = "true";
				enterpriseId = result.id;
				isCoo = result.isCoo;
			}
		}
	});
	//isCoo = "false";
	//enterpriseId = 2;
	if(isLagal=="true"){
		$("#enterpriseId").val(enterpriseId);
		var orgTypeObj = $("#architecture_structure").children().children();
		if(isCoo=="true"){
			//显示服务商组织
			orgTypeObj.eq(0).show();
			orgTypeObj.eq(0).attr("class","selected");
			orgTypeObj.eq(1).hide();
			orgTypeObj.eq(1).attr("class","");
			$("#architecture_structure_0").show();
			$("#architecture_structure_1").hide();
			$("#isCoo").val(isCoo);
			$("#org_p_enterpriseId").val(enterpriseId);
			//生成服务商组织树
			providerOrgTree();
			$("#add_staff_p_type").live("change",function(){
			    loadProviderOrgRoleInfo("add_staff_p_type");
			    $("#show_add_org_p_org_role").html("");
		    })
		    
		    $("#update_staff_p_type").live("change",function(){
			    loadProviderOrgRoleInfo("update_staff_p_type");
			    $("#show_update_org_p_org_role").html("");
		    })
		}else if(isCoo=="false"){
			//显示客户商组织
			orgTypeObj.eq(0).hide();
			orgTypeObj.eq(0).attr("class","");
			orgTypeObj.eq(1).show();
			orgTypeObj.eq(1).attr("class","selected");
			$("#org_c_enterpriseId").val(enterpriseId);
			$("#architecture_structure_0").hide();
			$("#architecture_structure_1").show();
			$("#isCoo").val(isCoo);
			//生成客户组织树
			getCustomerOrgTree();
		}else if(isCoo == "systemManager"){
			//系统管理员
			$(".add_root_architecture").show();
			$("#isCoo").val(isCoo);
			
			$.ajax({
				url: "getAllTheTopProviderOrgAction",//yuan.yw
				data:{enterpriseType:"CARRIEROPERATOR"},
				async:false,
				type:"POST",
				dataType : 'json',
				success : function(result) {
					if(result){
						var enterpriseIds = "";
						$.each(result,function(key,value){
							enterpriseIds += value.enterpriseId+",";
						});
						if(enterpriseIds!=""){
							enterpriseIds = enterpriseIds.substring(0,enterpriseIds.length-1);
						}
						$("#org_c_enterpriseIds").val(enterpriseIds);
					}
				}
			});
			$.ajax({
				url: "getAllTheTopProviderOrgAction",
				async:false,
				type:"POST",
				dataType : 'json',
				success : function(result) {
					if(result){
						var enterpriseIds = "";
						$.each(result,function(key,value){
							enterpriseIds += value.enterpriseId+",";
						});
						if(enterpriseIds!=""){
							enterpriseIds = enterpriseIds.substring(0,enterpriseIds.length-1);
						}
						$("#org_p_enterpriseIds").val(enterpriseIds);
					}
				}
			});
			providerOrgTree1();
			getCustomerOrgTree();
			$("#add_staff_p_type").live("change",function(){
			    loadProviderOrgRoleInfo("add_staff_p_type");
			    $("#show_add_org_p_org_role").html("");
		    })
		    
		    $("#update_staff_p_type").live("change",function(){
			    loadProviderOrgRoleInfo("update_staff_p_type");
			    $("#show_update_org_p_org_role").html("");
		    })
		}
	}
});

//生成服务商的组织架构树
function providerOrgTree(){
	var orgId = "";
	$.ajax({
		url: "getTheTopProviderOrgAjaxAction",
		async:false,
		type:"POST",
		dataType : 'json',
		success : function(result) {
			orgId = result.referenceValue;
		}
	});
	if(orgId==null||orgId==""){
		return;
	}
	var values = {"orgId":orgId};
	var myUrl = "getProviderOrgTreeByOrgIdAction";
	$.post(myUrl,values,function(data){
		createOrgTreeOpenFirstNode(data,"treeDiv","personnel_tree","providerOrgDiv","showProviderOrgInfo");
		//生成上级组织树
		createOrgTreeOpenFirstNode(data,"highOrgTree","personnel_tree1","","addOrgName");
	},"json");
}

//生成服务商的组织架构树
function providerOrgTree1(){
	var enterpriseIds = $("#org_p_enterpriseIds").val();
	var values = {"enterpriseIds":enterpriseIds};
	var myUrl = "getAllProviderOrgTreeAction";
	$.post(myUrl,values,function(data){
		createOrgTreeOpenFirstNode(data,"treeDiv","personnel_tree","providerOrgDiv","showProviderOrgInfo");
		//生成上级组织树
		createOrgTreeOpenFirstNode(data,"highOrgTree","personnel_tree1","","addOrgName");
	},"json");
}

//显示服务商的组织信息
function showProviderOrgInfo(dataStr,tableId){
	var data = eval("(" + dataStr + ")");
	var orgName = data.name;
	var orgId = data.orgId;
	$("#"+tableId).show();
	$("#mainOrgName").html("组织："+orgName);
	$("#add_org_p_parentOrgName").html(orgName);
	$("#orgId").val(orgId);
	$("#p_orgId").val(orgId);
	//显示组织架构的基本信息
	showProviderOrgBaseInfo(orgId);
	$("#orgName").val(orgName);
	
	//显示管辖人员的信息
	showProviderAccountInfo(orgId);
	$("#roleIdList").val("");
	$("#roleNameList").val("");
	showProjectByOrgId();
	
	var enterpriseIds = $("#show_org_p_enterpriseId").val();
	var values = {"enterpriseIds":enterpriseIds};
	var myUrl = "getAllProviderOrgTreeAction";
	$.post(myUrl,values,function(data){
		createOrgTreeOpenFirstNode(data,"highOrgTree","personnel_tree1","","addOrgName");
	},"json");
	//showOrgNetWorkByPorjectId();
}

//关闭添加最高级组织的弹出框
function closeAddTopOrgDiv(){
	$("#add_top_org").hide();
	$(".black").hide();
}

//显示添加最高级组织的弹出框
function showAddTopOrgDiv(){
	$(".top_org_error").html("");
	$(".add_top_org").val("");
	var title = $("#architecture_structure .selected").html();
	$("#add_top_type").val(title);
	if(title=="服务商组织"){
		title = "服务商";
	}else{
		title = "运营商";
	}
	$("#add_top_org_title").html(title);
	$("#add_top_org").show();
	$(".black").show();
	
	//添加组织类型的数据字典
	addDictionary("getDictionaryByTypeAction","add_top_org_orgType_em","add_top_org_orgType","orgType");//yuan.yw
	//添加组织属性的数据字典
	addDictionary("getDictionaryByTypeAction","add_top_org_orgAttribute_em","add_top_org_orgAttribute","businessType");
	//添加企业的数据字典
	//addDictionary("getAllNoChoiceEnterpriseAjaxAction","add_top_org_enterpriseId_em","add_top_org_enterpriseId");
	areaServerTree("area_server_treeDiv","areaTree","add_top_org","areaServerTreeClick");
	var orgType = encodeURIComponent(title);
	$.post("getAllNoChoiceEnterpriseAjaxAction",{"orgType":orgType},function(data){
		var str = "";
		str += "<select id='add_top_org_enterpriseId'>";
		str += "<option value=''>请选择</option>";
		$.each(data,function(index,value){
			str += "<option value='"+value.typeCode+"'>"+value.name+"</option>";
		});
		str += "</select>";
		$("#add_top_org_enterpriseId_em").html(str);
	},"json");
}

//添加最高级组织
function addTopOrg(){
	var flag = "true";
	var errorStr = "";
	var orgName = $("#add_top_org_name").val();
	if(orgName==""){
		errorStr = "必填字段";
		flag = "false";
	}
	if(orgName!="" && orgName.length > 50){
		errorStr = "超过50字符";
		flag = "false";
	}
	getErrorPrompt("add_top_org_name",errorStr);
	errorStr = "";
	
	var orgAttribute = $("#add_top_org_orgAttribute_em").children().eq(0).val();
	if(orgAttribute==""){
		errorStr = "必填字段";
		flag = "false";
	}
	getErrorPrompt("add_top_org_orgAttribute_em",errorStr);
	errorStr = "";
	
	var orgType = $("#add_top_org_orgType_em").children().eq(0).val();
	if(orgType==""){
		errorStr = "必填字段";
		flag = "false";
	}
	getErrorPrompt("add_top_org_orgType_em",errorStr);
	errorStr = "";
	
	var enterpriseId = $("#add_top_org_enterpriseId_em").children().eq(0).val();
	if(enterpriseId==""){
		errorStr = "必填字段";
		flag = "false";
	}
	getErrorPrompt("add_top_org_enterpriseId_em",errorStr);
	errorStr = "";
	
	//驻点位置
	var address = $.trim($("#add_top_org_address").val());
	if(address.length > 100){
		errorStr = "超过100字符";
		flag = "false";
	}
	getErrorPrompt("add_top_org_address",errorStr);
	errorStr = "";
	
	//组织职责
	var orgDuty = $.trim($("#add_top_org_orgDuty").val());
	if(orgDuty.length > 100){
		errorStr = "超过100字符";
		flag = "false";
	}
	getErrorPrompt("add_top_org_orgDuty",errorStr);
	errorStr = "";
	
	//驻点经度
	var longitude = $.trim($("#add_top_org_longitude").val());
	if(longitude!=""){
		if(longitude.length>20){
			errorStr = "超过100字符";
			flag = "false";
		}
		if(longitude!=0){
			if(!(/^d+$/).test(longitude)){
				if(!(/^-?\d+\.?\d+$/).test(longitude)){
					errorStr = "输入格式错误";
					flag = "false";
				}else if(longitude < -180 || longitude > 180){
					errorStr = "经度超过范围";
					flag = "false";
				}
			}
		}
	}
	getErrorPrompt("add_top_org_longitude",errorStr);
	errorStr = "";
	
	//驻点纬度
	var latitude = $.trim($("#add_top_org_latitude").val());
	if(latitude!=""){
		if(latitude.length>20){
			errorStr = "超过20字符";
			flag = "false";
		}
		if(latitude!=0){
			if(!(/^d+$/).test(latitude)){
				if(!(/^-?\d+\.?\d+$/).test(latitude)){
					errorStr = "输入格式错误";
					flag = "false";
				}else if(latitude < -90 || latitude > 90){
					errorStr = "纬度超过范围";
					flag = "false";
				}
			}
		}
	}
	getErrorPrompt("add_top_org_latitude",errorStr);
	errorStr = "";
	
	//驻点电话
	var contactPhone = $.trim($("#add_top_org_contactPhone").val());
	if(contactPhone!=""){
		if(contactPhone.length>20){
			errorStr = "超过20字符";
			flag = "false";
		}
		if(!(/^(\(\d{3,4}\)|\d{3,4}-)?\d*$/).test(contactPhone)){
			errorStr = "输入格式错误";
			flag = "false";
		}
	}
	getErrorPrompt("add_top_org_contactPhone",errorStr);
	errorStr = "";
	
	//负责人电话
	var dutyPersonPhone = $.trim($("#add_top_org_dutyPersonPhone").val());
	if(dutyPersonPhone!=""){
		if(dutyPersonPhone.length>20){
			errorStr = "超过20字符";
			flag = "false";
		}
		if(!(/^(\(\d{3,4}\)|\d{3,4}-)?\d*$/).test(dutyPersonPhone)){
			errorStr = "输入格式错误";
			flag = "false";
		}
	}
	getErrorPrompt("add_top_org_dutyPersonPhone",errorStr);
	errorStr = "";
	
	if(flag=="true"){
		var jsonStr = getTopOrgJsonStr();
		jsonStr = encodeURIComponent(jsonStr);
		$.post("saveProviderOrgInfoAjaxAction",{"orgJsonStr":jsonStr},function(data){
			if(data=="success"){
				alert("添加根组织成功");
				closeAddTopOrgDiv();
				var orgAttr = $("#add_top_type").val();
				if(orgAttr=="服务商组织"){
					var org_p_enterpriseIds = $("#org_p_enterpriseIds").val();
					if(org_p_enterpriseIds!=""){
						org_p_enterpriseIds += ","+enterpriseId;
					}else{
						org_p_enterpriseIds = enterpriseId;
					}
					$("#org_p_enterpriseIds").val(org_p_enterpriseIds);
					providerOrgTree1();
				}else{
					var org_c_enterpriseIds = $("#org_c_enterpriseIds").val();
					if(org_c_enterpriseIds!=""){
						org_c_enterpriseIds += ","+enterpriseId;
					}else{
						org_c_enterpriseIds = enterpriseId;
					}
					$("#org_c_enterpriseIds").val(org_c_enterpriseIds);
					getCustomerOrgTree();
				}
			}else{
				alert("添加根组织失败");
			}
		},"json");
	}
}

//填写组织名
function addOrgName(dataStr,tableId){
	var data = eval("(" + dataStr + ")");
	var orgName = data.name;
	var orgId = data.orgId;
	$("#update_org_p_parentOrgId").val(orgId);
	$("#update_org_p_parentOrgName").val(orgName);
	$("#highOrgTree").slideToggle("fast");
}

//服务商的基本信息=============================================================================================

//显示服务商组织的基本信息
function showProviderOrgBaseInfo(orgId){
	$.post("getProviderOrgByOrgIdAction",{"orgId":orgId,pageType:"server"},function(data){//yuan.yw
		$("#service_tab_0").html(data);
		var parentOrgName = $("#parentOrgName").val();
		$("#update_org_p_parentOrgName").val(parentOrgName);
		$("#enterpriseId").val($("#show_org_p_enterpriseId").val());
		$("#add_org_p_parentOrgName").html($("#orgName").val());
		//权限控制
		authorityControl(orgId);
		//添加组织类型的数据字典
		addDictionary("getDictionaryByTypeAction","orgType","org_p_type","orgType");//yuan.yw
		//添加组织属性的数据字典
		addDictionary("getDictionaryByTypeAction","orgAttribute","org_p_orgAttribute","businessType");
		//添加企业的数据字典
		addDictionary("getDictionaryByTypeAction","select_enterpriseId","org_p_enterpriseId","");
		//添加企业url的数据字典
		//addProviderEnterpriseUrlDictionary("add_account_p_enterpriseUrl");
		//添加人员类型的数据字典
		addProviderStaffTypeDictionary("add_staff_p_type");
		//添加人员类型的数据字典
		addProviderStaffTypeDictionary("update_staff_p_type");
	});
}

//权限控制
function authorityControl(orgId){
	var isCoo = $("#isCoo").val();
	if(isCoo =="systemManager"){
		return;
	}
	$.post("getAuthorityByAccountAjaxAction",function(data){
			var orgIds = "";
			if(data){
				$.each(data,function(index,value){
					orgIds += value + ",";
				});
				if(orgIds!=""){
					orgIds = orgIds.substring(0,orgIds.length-1);
				}
			}
			$(".authorityControlClass").show();
			authorityControlButton(orgIds);
			$("#account_downOrgId").val(orgIds);
		},"json");
}

//权限控制按钮
function authorityControlButton(orgIds){
	var orgId = $("#orgId").val();
	var flag = "false";
	if(orgIds!=""){
		var split = orgIds.split(",");
		$.each(split,function(i,v){
			if(orgId==v){
				flag = "true";
				return false;
			}
		});
		if(flag=="false"){
			$(".authorityControlClass").hide();
		}
	}
}

//添加负责人的下拉列表
function addDutyPersonDictionary(divId,emId){
	var orgId = $("#orgId").val();
	var title = $("#architecture_structure .selected").html();
	if(title=="服务商组织"){
		orgId = $("#p_orgId").val();
	}else{
		orgId = $("#c_orgId").val();
	}
	$.ajax({
		url: "getAccountDictionaryByOrgIdAjaxAction",
		async:false,
		type:"POST",
		dataType : 'json',
		data: {"orgId":orgId} ,
		success : function(result) {
			var str = "";
			str += "<select id='"+divId+"'>";
			str += "<option value=''>请选择</option>";
			$.each(result,function(index,value){
				str += "<option value='"+value.account+"'>"+value.name+"</option>";
			});
			str += "</select>";
			$("#"+emId).html(str);
		}
	});
}

//添加数据字典
function addDictionary(actionName,className,idName,dictionaryType){//yuan.yw
	
	$.post(actionName,{dictionaryType:dictionaryType},function(data){
		var str = "";
		str += "<select id='"+idName+"'>";
		str += "<option value=''>请选择</option>";
		$.each(data,function(index,value){
			if(""==dictionaryType||"orgType"==dictionaryType){
				str += "<option value='"+value.typeCode+"'>"+value.name+"</option>";
			}else{
				str += "<option value='"+value.id+"'>"+value.name+"</option>";
			}
			
		});
		str += "</select>";
		$("#"+className).html(str);
	},"json");
}

//弹出添加服务商组织的基本信息
function addProviderOrgBaseInfo(){
	//错误提示清空
	$(".org_p_error").html("");
	areaServerTree("org_p_area_server_treeDiv","org_p_areaTree","org_p","areaServerTreeClick");
	changeValueEmpty("org_p_orgInfo_div");
	$("#org_p_dutyPerson").html("<option value=''>请选择</option>");
	$(".add_p_baseInfo").show();
	$(".update_p_baseInfo").hide();
	$("#highOrgTree").hide();
	var enterpriseId = $("#show_org_p_enterpriseId").val();
	$.each($("#select_enterpriseId").children().children(),function(){
		if($(this).val()==enterpriseId){
			$(this).attr("selected","selected");
			return false;
		}
	});
}

//确认添加服务商组织的基本信息
function confirmAddProviderOrgBaseInfo(){
	var flag = "true";
	var errorStr = "";
	var orgName = $.trim($("#org_p_name").val());
	if(orgName==""){
		errorStr = "必填字段";
		flag = "false";
	}
	if(orgName!="" && orgName.length > 50){
		errorStr = "超过50字符";
		flag = "false";
	}
	getErrorPrompt("org_p_name",errorStr);
	errorStr = "";
	
	var orgAttribute = $("#orgAttribute").children().eq(0).val();
	if(orgAttribute==""){
		errorStr = "必填字段";
		flag = "false";
	}
	getErrorPrompt("orgAttribute",errorStr);
	errorStr = "";
	
	var orgType = $("#orgType").children().eq(0).val();
	if(orgType==""){
		errorStr = "必填字段";
		flag = "false";
	}
	getErrorPrompt("orgType",errorStr);
	errorStr = "";
	
	var enterprise = $("#select_enterpriseId").children().eq(0).val();
	if(enterprise==""){
		errorStr = "必填字段";
		flag = "false";
	}
	getErrorPrompt("select_enterpriseId",errorStr);
	errorStr = "";
	
	//驻点位置
	var address = $.trim($("#org_p_address").val());
	if(address.length > 100){
		errorStr = "超过100字符";
		flag = "false";
	}
	getErrorPrompt("org_p_address",errorStr);
	errorStr = "";
	
	//组织职责
	var orgDuty = $.trim($("#org_p_orgDuty").val());
	if(orgDuty.length > 100){
		errorStr = "超过100字符";
		flag = "false";
	}
	getErrorPrompt("org_p_orgDuty",errorStr);
	errorStr = "";
	
	//驻点经度
	var longitude = $.trim($("#org_p_longitude").val());
	if(longitude!=""){
		if(longitude.length > 20){
			errorStr = "超过20字符";
			flag = "false";
		}
		if(longitude!=0){
			if(!(/^d+$/).test(longitude)){
				if(!(/^-?\d+\.?\d+$/).test(longitude)){
					errorStr = "输入格式错误";
					flag = "false";
				}else if(longitude < -180 || longitude > 180){
					errorStr = "经度超过范围";
					flag = "false";
				}
			}
		}
	}
	getErrorPrompt("org_p_longitude",errorStr);
	errorStr = "";
	
	//驻点纬度
	var latitude = $.trim($("#org_p_latitude").val());
	if(latitude!=""){
		if(latitude.length > 20){
			errorStr = "超过20字符";
			flag = "false";
		}
		if(latitude!=0){
			if(!(/^d+$/).test(latitude)){
				if(!(/^-?\d+\.?\d+$/).test(latitude)){
					errorStr = "输入格式错误";
					flag = "false";
				}else if(latitude < -90 || latitude > 90){
					errorStr = "纬度超过范围";
					flag = "false";
				}
			}
		}
	}
	getErrorPrompt("org_p_latitude",errorStr);
	errorStr = "";
	
	//驻点电话
	var contactPhone = $.trim($("#org_p_contactPhone").val());
	if(contactPhone!=""){
		if(contactPhone.length > 20){
			errorStr = "超过20字符";
			flag = "false";
		}
		if(!(/^(\(\d{3,4}\)|\d{3,4}-)?\d*$/).test(contactPhone)){
			errorStr = "输入格式错误";
			flag = "false";
		}
	}
	getErrorPrompt("org_p_contactPhone",errorStr);
	errorStr = "";
	
	//负责人电话
	var dutyPersonPhone = $.trim($("#org_p_dutyPersonPhone").val());
	if(dutyPersonPhone!=""){
		if(dutyPersonPhone.length > 20){
			errorStr = "超过20字符";
			flag = "false";
		}
		if(!(/^(\(\d{3,4}\)|\d{3,4}-)?\d*$/).test(dutyPersonPhone)){
			errorStr = "输入格式错误";
			flag = "false";
		}
	}
	getErrorPrompt("org_p_dutyPersonPhone",errorStr);
	errorStr = "";
	
	if(flag=="true"){
		var jsonStr = getPOrgJsonStr();
		jsonStr = encodeURIComponent(jsonStr);
		$.post("saveProviderOrgInfoAjaxAction",{"orgJsonStr":jsonStr},function(data){
			$("#serviceInfo_Dialog").hide();
			$(".black").hide();
			var isCoo = $("#isCoo").val();
			if(isCoo=="systemManager"){
				providerOrgTree1();
			}else{
				providerOrgTree();
			}
		},"json");
	}
}

//弹出修改服务商组织的基本信息
function updateProviderOrgBaseInfo(){
	areaServerTree("org_p_area_server_treeDiv","org_p_areaTree","org_p","areaServerTreeClick");
	if($("#p_areaIds").val()!=""){
		var areaIds = $("#p_areaIds").val();
		var areaArray = areaIds.split(",");
		for(var i=0;i<areaArray.length;i++){
			$("#org_p_area_server_treeDiv input[name='areaCheckBox'][value='"+areaArray[i]+"']").attr("checked","checked");
		}
	}
	//添加负责人的下拉列表
	addDutyPersonDictionary("org_p_dutyPerson","dutyPerson");
	//错误提示清空
	$(".org_p_error").html("");
	
	$("#org_p_name").val($("#show_org_p_name").html());
	var type = $("#show_org_p_type").html();
	$.each($("#orgType").children().children(),function(){
		if($(this).text()==type){
			$(this).attr("selected","selected");
			return false;
		}
	});
	var orgAttribute = $("#show_org_p_orgAttribute").html();
	$.each($("#orgAttribute").children().children(),function(){
		if($(this).text()==orgAttribute){
			$(this).attr("selected","selected");
			return false;
		}
	});
	var dutyPerson = $("#show_org_p_dutyPerson").html();
	$.each($("#dutyPerson").children().children(),function(){
		if($(this).val()==dutyPerson){
			$(this).attr("selected","selected");
			return false;
		}
	});
	
	$("#org_p_orgDuty").val($("#show_org_p_orgDuty").html());
	$("#org_p_address").val($("#show_org_p_address").html());
	$("#org_p_longitude").val(changeUnkownValue($("#show_org_p_longitude").val()));
	$("#org_p_latitude").val(changeUnkownValue($("#show_org_p_latitude").val()));
	$("#org_p_contactPhone").val($("#show_org_p_contactPhone").html());
	$("#org_p_dutyPerson").val($("#show_org_p_dutyPerson").html());
	$("#update_org_p_parentOrgName").val($("#parentOrgName").val());
	$("#update_org_p_parentOrgId").val($("#show_org_p_parentOrgId").val());
	$("#update_org_p_enterpriseId").html($("#show_org_p_enterpriseName").html());
	$("#org_p_areaId").val($("#show_org_p_areaId").val());
	$("#org_p_areaname").val($("#show_org_p_areaName").val());
	$(".add_p_baseInfo").hide();
	$(".update_p_baseInfo").show();
	$("#highOrgTree").hide();
}

//确认修改服务商组织的基本信息
function confirmUpdateProviderOrgBaseInfo(){
	var orgId = $("#orgId").val();
	var flag = "true";
	var errorStr = "";
	var orgName = $("#org_p_name").val();
	if(orgName==""){
		errorStr = "必填字段";
		flag = "false";
	}
	if(orgName!="" && orgName.length > 50){
		errorStr = "超过50字符";
		flag = "false";
	}
	getErrorPrompt("org_p_name",errorStr);
	errorStr = "";
	
	var orgAttribute = $("#orgAttribute").children().eq(0).val();
	if(orgAttribute==""){
		errorStr = "必填字段";
		flag = "false";
	}
	getErrorPrompt("orgAttribute",errorStr);
	errorStr = "";
	
	var orgType = $("#orgType").children().eq(0).val();
	if(orgType==""){
		errorStr = "必填字段";
		flag = "false";
	}
	getErrorPrompt("orgType",errorStr);
	errorStr = "";
	
	var parentOrgId = $("#update_org_p_parentOrgId").val();
	if(parentOrgId==orgId){
		errorStr = "上级组织与当前组织重复";
		flag = "false";
	}
	getErrorPrompt("update_org_p_parentOrgName",errorStr);
	errorStr = "";
	
		//驻点位置
	var address = $.trim($("#org_p_address").val());
	if(address.length > 100){
		errorStr = "超过100字符";
		flag = "false";
	}
	getErrorPrompt("org_p_address",errorStr);
	errorStr = "";
	
	//组织职责
	var orgDuty = $.trim($("#org_p_orgDuty").val());
	if(orgDuty.length > 100){
		errorStr = "超过100字符";
		flag = "false";
	}
	getErrorPrompt("org_p_orgDuty",errorStr);
	errorStr = "";
	
	//驻点经度
	var longitude = $.trim($("#org_p_longitude").val());
	if(longitude!=""){
		if(longitude.length > 20){
			errorStr = "超过20字符";
			flag = "false";
		}
		if(longitude!=0){
			if(!(/^d+$/).test(longitude)){
				if(!(/^-?\d+\.?\d+$/).test(longitude)){
					errorStr = "输入格式错误";
					flag = "false";
				}else if(longitude < -180 || longitude > 180){
					errorStr = "经度超过范围";
					flag = "false";
				}
			}
		}
	}
	getErrorPrompt("org_p_longitude",errorStr);
	errorStr = "";
	
	//驻点纬度
	var latitude = $.trim($("#org_p_latitude").val());
	if(latitude!=""){
		if(latitude.length > 20){
			errorStr = "超过20字符";
			flag = "false";
		}
		if(latitude!=0){
			if(!(/^d+$/).test(latitude)){
				if(!(/^-?\d+\.?\d+$/).test(latitude)){
					errorStr = "输入格式错误";
					flag = "false";
				}else if(latitude < -90 || latitude > 90){
					errorStr = "纬度超过范围";
					flag = "false";
				}
			}
		}
	}
	getErrorPrompt("org_p_latitude",errorStr);
	errorStr = "";
	
	//驻点电话
	var contactPhone = $.trim($("#org_p_contactPhone").val());
	if(contactPhone!=""){
		if(contactPhone.length > 20){
			errorStr = "超过20字符";
			flag = "false";
		}
		if(!(/^(\(\d{3,4}\)|\d{3,4}-)?\d*$/).test(contactPhone)){
			errorStr = "输入格式错误";
			flag = "false";
		}
	}
	getErrorPrompt("org_p_contactPhone",errorStr);
	errorStr = "";
	
	//负责人电话
	var dutyPersonPhone = $.trim($("#org_p_dutyPersonPhone").val());
	if(dutyPersonPhone!=""){
		if(dutyPersonPhone.length > 20){
			errorStr = "超过20字符";
			flag = "false";
		}
		if(!(/^(\(\d{3,4}\)|\d{3,4}-)?\d*$/).test(dutyPersonPhone)){
			errorStr = "输入格式错误";
			flag = "false";
		}
	}
	getErrorPrompt("org_p_dutyPersonPhone",errorStr);
	errorStr = "";
	
	if(flag=="true"){
		var jsonStr = getPOrgJsonStr();
		jsonStr = encodeURIComponent(jsonStr);
		$.post("updateProviderOrgInfoAjaxAction",{"orgJsonStr":jsonStr},function(data){
			$("#serviceInfo_Dialog").hide();
			$(".black").hide();
			showProviderOrgBaseInfo($("#orgId").val());
			var isCoo = $("#isCoo").val();
			if(isCoo=="systemManager"){
				providerOrgTree1();
			}else{
				providerOrgTree();
			}
		},"json");
	}
	
}

//点击服务商组织的基本信息的删除按钮
function deleteProviderOrgBaseInfo(){
	$("#delete_p_baseInfo").html($("#orgName").val());
}

//点击服务商组织的基本信息的确认删除按钮
function confirmDeleteProviderOrgBaseInfo(){
	var isCoo = $("#isCoo").val();
	if(isCoo=="true"){
		var orgId = $("#orgId").val();
		$.post("deleteProviderOrgInfoAjaxAction",{"orgId":orgId},function(data){
			var isCoo = $("#isCoo").val();
			if(isCoo=="systemManager"){
				providerOrgTree1();
			}else{
				providerOrgTree();
			}
			$("#providerOrgDiv").hide();
		},"json");
	}else if(isCoo=="false"){
		var orgId = $("#c_orgId").val();
		$.post("deleteProviderOrgInfoAjaxAction",{"orgId":orgId},function(data){
			getCustomerOrgTree();
			$("#customerOrgDiv").hide();
		},"json");
	}else if(isCoo=="systemManager"){
		var title = $("#architecture_structure .selected").html();
		if(title=="服务商组织"){
			var orgId = $("#p_orgId").val();
			$.post("deleteProviderOrgInfoAjaxAction",{"orgId":orgId},function(data){
				var isCoo = $("#isCoo").val();
				providerOrgTree1();
				$("#providerOrgDiv").hide();
			},"json");
		}else if(title=="运营商组织"){
			var orgId = $("#c_orgId").val();
			$.post("deleteProviderOrgInfoAjaxAction",{"orgId":orgId},function(data){
				getCustomerOrgTree();
				$("#customerOrgDiv").hide();
			},"json");
		}
	}
}

//服务商的管辖人员=========================================================================

//显示服务商组织的管辖人员信息
function showProviderAccountInfo(orgId){
	var pageDivId = "org_p_staff_page";
	var showDivId = "org_p_staff_table";
	var actionName = "../system/getProviderSystemStaffByOrgIdAction";
	var pageSize = "10";
	var param={
		orgId:orgId,
		currentPage:"1",
		pageSize:pageSize
	};
	pagingColumnByBackgroundJsp(pageDivId,showDivId,actionName,param);
	/*
	$.post("getProviderStaffByOrgIdAction",{"orgId":orgId},function(data){
		$("#service_tab_1").html(data);
	});*/
}

//显示保存服务商的账号信息
function showSaveProviderAccountInfo(){
	var enterpriseId = $("#enterpriseId").val();
	var enterpriseUrl = "";
	$.post("../system/getSysEnterpriseUrlDictionaryAjaxAction",function(data){
		if(data){
			$.each(data,function(index,value){
				if(value.typeCode==enterpriseId){
					enterpriseUrl = value.name;
					return false;
				}
			});
		}
		$("#add_account_p_enterpriseUrl").html(enterpriseUrl);
	},"json");
	loadProviderOrgRole("org_p_org_role");
	$(".add_account_p_error").html("");
	$("#add_account_p_account").val("");
	$("#add_account_p_password").val("");
	$("#add_account_p_comfigPwd").val("");
	$("#add_account_p_name").val("");
	$("#add_account_p_cellPhoneNumber").val("");
	$("#add_account_p_email").val("");
	$("#add_account_p_email").val("");
	$("#add_account_p_mobileEmailAddress").val("");
	$("#add_account_p_backUpEmailAddress").val("");
	var myDate = new Date();
	var year = myDate.getFullYear();
	var month = myDate.getMonth();
	var date = myDate.getDate();
	$("#add_account_p_time_range_begin").val(year+"-"+(month+1)+"-"+date);
	$("#add_account_p_time_range_end").val((year+1)+"-"+(month+1)+"-"+date);
	$(".add_account_p_status").eq(0).attr("checked","checked");
	
	$("#account_title").attr("class","selected");
	$("#staff_title").attr("class","");
	$("#skill_title").attr("class","");
	$("#staff_title").hide();
	$("#skill_title").hide();
	$("#staffadd_tab_0").show();
	$("#staffadd_tab_1").hide();
	$("#staffadd_tab_2").hide();
	$(".staffadd_info_account .staffInfo_value").hide();
	$(".staffadd_account_but").show();
	$(".staffadd_info_account .staffInfo_input").show();
	
	$(".staffInfo_input").show();
	$(".staffInfo_value").hide();
	
	$("#show_add_org_p_org_role").html("");
	$("#show_add_org_p_biz_role").html("");
	$("ul[name='org_p_biz_role']").each(function(){
		$(this).html("");
	});
	$("#orgRole_tableId").val("show_add_org_p_org_role");
	$("#bizRole_tableId").val("show_add_org_p_biz_role");
	$(".add_account_p_isEnterprise").removeAttr("checked");
	$("#add_staff_p_type").val("");
}

//检测添加账号
function checkAddProviderAccount(div){
	var account = $("#"+div+"_account").val();
	var urls = $("#"+div+"_enterpriseUrl").html();
	var enterpriseUrl = urls;
	/*
	$.each(urls,function(index,value){
		if($(this).attr("selected")=="selected"){
			enterpriseUrl = $(this).text();
			return false;
		}
	});
	*/
	if(account==""){
		alert("必填字段");
		return;
	}
	if(!(/^[-\.\w#%&:?=\/]+$/i).test(account)){
		alert("输入格式错误");
		return;
	}
	if($.trim(account)!=""){
		if($.trim(account).length > 20){
			alert("超过20字符");
			return;
		}
	}
	$.post("../system/checkAccountAjaxAction",{"account":account+enterpriseUrl},function(data){
		if(data=="success"){
			alert("该账号可用");
		}else{
			alert("该账号已经被使用");
		}
	});
}

//保存服务商的账号信息
function saveProviderAccountInfo(){
	//var isEnterprise = $('input:radio[name="add_account_p_isEnterprise"]:checked').val();
	var enterpriseId = $("#enterpriseId").val();
	var orgId = $("#orgId").val();
	var account = $.trim($("#add_account_p_account").val());
	var password = $.trim($("#add_account_p_password").val());
	var comfigPwd = $.trim($("#add_account_p_comfigPwd").val());
	var name = $.trim($("#add_account_p_name").val());
	var sex = $("input[name='add_account_p_sex']:checked").val();
	var cellPhoneNumber = $.trim($("#add_account_p_cellPhoneNumber").val());
	var mobileEmailAddress = $.trim($("#add_account_p_mobileEmailAddress").val());
	var backUpEmailAddress = $.trim($("#add_account_p_backUpEmailAddress").val());
	var time_range_begin = $("#add_account_p_time_range_begin").val();
	var time_range_end = $("#add_account_p_time_range_end").val();
	
	
	var state = "0";
	$(".show_add_account_p_status").removeAttr("checked");
	if($(".add_account_p_status").eq(0).attr("checked")=="checked"){
		state = "1";
		$(".show_add_account_p_status").attr("checked","checked");
	}
	var email = $.trim($("#add_account_p_email").val());
	//var type = $("#add_staff_p_type").val();
	var roleIdList = $("#roleIdList").val();
	var bizRoleIdList = $("#bizRoleIdList").val();
	if(roleIdList==""){
		roleIdList = bizRoleIdList;
	}else{
		if(bizRoleIdList!=""){
			roleIdList += ","+ bizRoleIdList;
		}
	}
	var enterpriseUrl = $("#add_account_p_enterpriseUrl").html();
	var flag = "true";
	var errorStr = "";
	$.ajax({
		url: "../system/checkAccountAjaxAction",
		async:false,
		type:"POST",
		dataType : 'json',
		data: {"account":account+enterpriseUrl} ,
		success : function(result) {
			if(result!="success"){
				errorStr = "该账号已经被使用";
				flag = "false";
			}
		}
	});
	//账号提示
	if($.trim(account)==""){
		errorStr = "必填字段";
		flag = "false";
	}
	if($.trim(account)!=""){
		if(!(/^[-\.\w#%&:?=\/]+$/i).test(account)){
			errorStr = "输入格式错误";
			flag = "false";
		}
	}
	if($.trim(account)!=""){
		if($.trim(account).length > 20){
			errorStr = "超过20字符";
			flag = "false";
		}
	}
	getErrorPrompt("add_account_p_account",errorStr);
	errorStr = "";
	//密码提示
	if(password==""){
		errorStr = "必填字段";
		flag = "false";
	}else if(password.length<6){
		errorStr = "密码不能小于6位数";
		flag = "false";
	}else if(!(/[0-9]+/).test(password) || !(/[A-Za-z]+/).test(password)){
		errorStr = "密码要包含英文和数字";
		flag = "false";
	}else if(password.length > 40){
		errorStr = "超过40字符";
		flag = "false";
	}
	getErrorPrompt("add_account_p_password",errorStr);
	errorStr = "";
	//确认密码提示
	if(password!=comfigPwd){
		errorStr = "两次密码不一致";
		flag = "false";
	}
	getErrorPrompt("add_account_p_comfigPwd",errorStr);
	errorStr = "";
	//名字提示
	if($.trim(name)==""){
		errorStr = "必填字段";
		flag = "false";
	}else if(name.length > 20){
		errorStr = "超过20字符";
		flag = "false";
	}
	getErrorPrompt("add_account_p_name",errorStr);
	errorStr = "";
	//手机号码提示
	if(cellPhoneNumber==""){
		errorStr = "必填字段";
		flag = "false";
	}else if(!(/^(\(\d{3,4}\)|\d{3,4}-)?\d*$/).test(cellPhoneNumber)){
		errorStr = "输入格式错误";
		flag = "false";
	}else if(cellPhoneNumber.length > 11){
		errorStr = "超过11字符";
		flag = "false";
	}
	getErrorPrompt("add_account_p_cellPhoneNumber",errorStr);
	errorStr = "";
	//邮箱提示
	if(email==""){
		errorStr = "必填字段";
		flag = "false";
	}else if(!(/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/).test(email)){
		errorStr = "输入格式错误";
		flag = "false";
	}else if(email.length > 50){
		errorStr = "超过50字符";
		flag = "false";
	}
	getErrorPrompt("add_account_p_email",errorStr);
	errorStr = "";
	//手机邮箱提示
	/*if($.trim(mobileEmailAddress)==""){
		errorStr = "必填字段";
		flag = "false";
	}else*/
	 if(!(/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/).test(mobileEmailAddress)){
		errorStr = "输入格式错误";
		flag = "false";
	}else if($.trim(email)==$.trim(mobileEmailAddress)){
		errorStr = "手机邮箱与其他邮箱重复";
		flag = "false";
	}else if(mobileEmailAddress.length > 50){
		errorStr = "超过50字符";
		flag = "false";
	}
	getErrorPrompt("add_account_p_mobileEmailAddress",errorStr);
	errorStr = "";
	//备用邮箱提示
	if($.trim(backUpEmailAddress)!=""){
		if(!(/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/).test(backUpEmailAddress)){
			errorStr = "输入格式错误";
			flag = "false";
		}else if($.trim(backUpEmailAddress)==$.trim(mobileEmailAddress) || $.trim(backUpEmailAddress)==$.trim(email)){
			errorStr = "备用邮箱与其他邮箱重复";
			flag = "false";
		}else if(backUpEmailAddress.length > 50){
			errorStr = "超过50字符";
			flag = "false";
		}
	}
	getErrorPrompt("add_account_p_backUpEmailAddress",errorStr);
	errorStr = "";
	/*
	//是否属于该企业提示
	if(!isEnterprise){
		errorStr = "请选择";
		flag = "false";
	}
	getErrorPrompt("add_account_p_isEnterprise",errorStr);
	errorStr = "";
	*/
	if(flag=="true"){
		account = account + enterpriseUrl;
		var jsonStr = "{";
		jsonStr += "'orgId':'"+orgId+"',";
		//jsonStr += "'type':'"+type+"',";
		jsonStr += "'roleIdList':'"+roleIdList+"',";
		jsonStr += "'account':'"+account+"',";
		jsonStr += "'password':'"+password+"',";
		jsonStr += "'name':'"+name+"',";
		jsonStr += "'gender':'"+sex+"',";
		jsonStr += "'mobile':'"+cellPhoneNumber+"',";
		//jsonStr += "'isEnterprise':'"+isEnterprise+"',";
		jsonStr += "'enterpriseId':'"+enterpriseId+"',";
		jsonStr += "'mobileemail':'"+mobileEmailAddress+"',";
		jsonStr += "'backupemail':'"+backUpEmailAddress+"',";
		jsonStr += "'status':'"+state+"',";
		jsonStr += "'email':'"+email+"'";
		jsonStr += "}";
		jsonStr = encodeURIComponent(jsonStr);
		$.ajax({
		url: "../system/createStaffAndUserRoleAction",
		async:false,
		type:"POST",
		data: {"orgJsonStr":jsonStr} ,
		success : function($result) {
				if($result != 0){
					$("#serviceAddStaff_Dialog").hide();
					var de  = $result;
					showUpdateProviderAccountInfo(de);
					$("#serviceStaff_Dialog").show();
				}else{
					alert("人员信息添加失败");
				}
			}
		});
	}
	
}

//加载组织角色
function loadProviderOrgRoleInfo(groupTypeId){
	var orgId = $("#orgId").val();
	var groupType = $("#"+groupTypeId).val();
	$.ajax({
           url:"../system/getAllSysRoleAction",
           async:false,
           type:"POST",
           dataType:"json",
           success : function(result) {
			var str = "";
			if(result){
				$.each(result,function(index,value){
					str +="<li><input class='org_role_class' type='checkbox' value='"+value.roleId+"' /><em>"+value.name+"</em></li>";
				});
				$("#org_p_org_role").html(str);
			}
		}
	});
}

//加载组织角色
function loadProviderOrgRole(divId){
	var orgId = $("#orgId").val();
	$.ajax({
           url:"../system/getAllSysRoleAction",
           async:false,
           type:"POST",
           dataType:"json",
           success : function(result) {
			var str = "";
			if(result){
				$.each(result,function(index,value){
					str +="<li><input class='org_role_class' type='checkbox' value='"+value.roleId+"' /><em>"+value.name+"</em></li>";
				});
				$("#"+divId).html(str);
			}
		}
	});
}

//显示组织角色
function showProviderOrgRole(state){
	var flag = "true";
	var org_role_checkbox = $.trim($("#org_p_org_role").html());
	var staffType = "";
	if(state==0){
		staffType = "add_staff_p_type";
	}else{
		staffType = "update_staff_p_type";
	}
	var groupType = $("#"+staffType).val();
	
	var errorStr = "";
	getErrorPrompt(staffType,errorStr);
	errorStr = "";
	if(flag=="true"){
		//loadProviderOrgRoleInfo("add_staff_p_type");
		//showProviderBizRole(state);
		$("#serviceRole_Dialog").show();
		$(".role_black").show();
	}
}


//获取组织角色
function getProviderOrgRole(){
	var tableId = $("#orgRole_tableId").val();
	var str = "";
	var nameStr = "";
	var tableStr = "";
	$(".org_role_class").each(function(){
		if($(this).attr("checked")=="checked"){
			str += $(this).val()+",";
			nameStr += $(this).next().html()+",";
			//tableStr += "<tr><td class='role_table_tr'>"+$("#orgName").val()+"</td><td>"+$(this).next().html()+"</td></tr>";
			tableStr += "<tr><td class='role_table_tr'>"+$(this).next().html()+"</td></tr>";
		}
	});
	if(str!=""){
		str = str.substring(0,str.length-1);
		nameStr = nameStr.substring(0,nameStr.length-1);
	}
	$("#roleIdList").val(str);
	$("#roleNameList").val(nameStr);
	$("#"+tableId).html(tableStr);
}

//获取业务角色
function getProviderBizRole(){
	var tableId = $("#bizRole_tableId").val();
	$("#"+tableId).html("");
	var str = "";
	var tableStr = "";
	var index = $("#business_role").children().eq(0).children().length;
	for(var i = 0; i < index; i++){
		var array = new Array();
		array = getProviderBizRoleModule(i);
		str += array[0];
		tableStr += array[1];
	}
	
	/*
	//抢修
	$("#business_role_0 .biz_role_class").each(function(){
		var arr = new Array();
		var bizStr ="";
		bizStr += "<tr><td class='role_table_tr'>"+$("#business_role_tag_0").html()+"</td><td>";
		if($(this).attr("checked")=="checked"){
			str += $(this).val()+",";
			bizStr += "<p>"+$(this).next().html()+"</p>";
			index = 1;
		}
		bizStr += "</td></tr>";
		if(index==0){
			bizStr = "";
		}
		tableStr += bizStr;
		arr[0] = str;
		arr[1] = tableStr;
		index = 0;
	});
	*/
	
	if(str!=""){
		str = str.substring(0,str.length-1);
	}
	$("#bizRoleIdList").val(str);
	$("#"+tableId).html(tableStr);
	
}

//生成业务角色的模板
function getProviderBizRoleModule(index){
	var str = "";
	var tableStr = "";
	var arr = new Array();
	var falg = 0;
	var bizStr ="";
	bizStr += "<tr><td class='role_table_tr'>"+$("#business_role_tag_"+index).html()+"</td><td>";
	$("#business_role_"+index+" .biz_role_class").each(function(){
		if($(this).attr("checked")=="checked"){
			str += $(this).val()+",";
			bizStr += "<p>"+$(this).next().html()+"</p>";
			falg = 1;
		}
	});
	
	bizStr += "</td></tr>";
	if(falg==0){
		bizStr = "";
	}
	tableStr += bizStr;
	arr[0] = str;
	arr[1] = tableStr;
	return arr;
}

//关闭授予角色
function closeProviderAwardRole(){
	$("#serviceRole_Dialog").hide();
	$(".role_black").hide();
}

//保存人员详细信息
function saveProviderStaffInfo(){
	var sex = $("#add_staff_p_sex").val();
	
	var account = $("#add_account_p_account").val()+$("#add_account_p_enterpriseUrl").html();
	var birthday = $("#add_staff_p_birthday").val();
	var graduateDate = $("#add_staff_p_graduateDate").val();
	var degree = $("#add_staff_p_degree").val();
	var identityCard = $("#add_staff_p_identityCard").val();

	var jsonStr = "{";
	jsonStr += "'sex':'"+sex+"',";
	
	jsonStr += "'account':'"+account+"',";
	jsonStr += "'birthday':'"+birthday+"',";
	jsonStr += "'graduateDate':'"+graduateDate+"',";
	jsonStr += "'degree':'"+degree+"',";
	jsonStr += "'identityCard':'"+identityCard+"'";
	jsonStr += "}";
	jsonStr = encodeURIComponent(jsonStr);
	$.post("saveProiderStaffAjaxAction",{"orgJsonStr":jsonStr},function(data){
		if("success"==data){
			$("#show_add_staff_p_sex").html(sex+"&nbsp;");
			
			$("#show_add_staff_p_account").html(account+"&nbsp;");
			$("#show_add_staff_p_birthday").html(birthday+"&nbsp;");
			$("#show_add_staff_p_graduateDate").html(graduateDate+"&nbsp;");
			$("#show_add_staff_p_degree").html(degree+"&nbsp;");
			$("#show_add_staff_p_identityCard").html(identityCard+"&nbsp;");
			
			$("#show_add_account_p_sex").html(sex+"&nbsp;");
			
			$(".staffInfo_input").hide();
			$(".staffInfo_value").show();
			var orgId = $("#orgId").val();
			showProviderAccountInfo(orgId);
		}
	},"json");
}

//添加人员类型的数据字典
function addProviderStaffTypeDictionary(divId){
	$.post("../system/getSysRoleByUserGroupAjaxAction",function(data){
		var str = "";
		str += "<option value='0' selected='selected'>请选择</option>";
		if(data){
			$.each(data,function(index,value){
				str += "<option value='"+value.roleTypeId+"'>"+value.name+"</option>";
			});
		}
		$("#"+divId).html(str);
	},"json");
}

//添加人员类型的数据字典
function addProviderEnterpriseUrlDictionary(divId){
	$.post("../system/getSysEnterpriseUrlDictionaryAjaxAction",function(data){
		var str = "";
		//str += "<option value='0'>请选择</option>";
		if(data){
			$.each(data,function(index,value){
				str += "<option value='"+value.typeCode+"'>"+value.name+"</option>";
			});
		}
		$("#"+divId).html(str);
	},"json");
}

//显示账号修改tag
function showUpdateProviderAccountInfo(account){
	$(".authorityControlClass").show();
	$("#staff_tab_0").show();
	$("#staff_tab_1").hide();
	$("#staff_tab_2").hide();
	$(".staffInfo_value").show();
	$(".staffInfo_input").hide();
	$(".staffInfo_modify").show();
	$(".staffInfo_save").hide();
	var orgIds = $("#account_downOrgId").val();
	authorityControlButton(orgIds);
	
	$(".update_account_p_error").html("");
	$("#orgRole_tableId").val("show_update_org_p_org_role");
	$("#bizRole_tableId").val("show_update_org_p_biz_role");
	$.post("../system/getProviderSystemAccountAjaxAction",{"orgUserId":account},function(data){
		$("#staff_tab").children().eq(0).children().eq(1).hide();
		if(data.isCard=="false"){
			$("#staff_tab").children().eq(0).children().eq(1).show();
			$("#show_update_account_p_isEnterprise").html("是");
		}else{
			$("#show_update_account_p_isEnterprise").html("否");
		}
		var changePassword = "";
		if(data.password){
			var pwdLength = data.password.length;
			for(var i = 0; i < pwdLength; i++){
				changePassword += "*";
			}
		}
		$("#show_update_account_p_account").html(changeUnkownValue(data.account)+"&nbsp;");
		$("#show_update_account_p_password").html(changePassword+"&nbsp;");
		$("#show_update_account_p_comfigPwd").html(changePassword+"&nbsp;");
		$("#show_update_account_p_name").html(changeUnkownValue(data.name)+"&nbsp;");
		var sexChiese = "";
		if(data.gender == "male"){
			sexChiese = "男";
		}else if(data.gender == "female"){
			sexChiese = "女";
		}
		$("#show_update_account_p_sex").html(changeUnkownValue(sexChiese)+"&nbsp;");
		$("#show_update_account_p_cellPhoneNumber").html(changeUnkownValue(data.mobile)+"&nbsp;");
		$("#show_update_account_p_email").html(changeUnkownValue(data.email)+"&nbsp;");
		$("#show_update_account_p_backUpEmailAddress").html(changeUnkownValue(data.backupemail)+"&nbsp;");
		$("#show_update_account_p_mobileEmailAddress").html(changeUnkownValue(data.mobileemail)+"&nbsp;");
		//$("#show_update_account_p_time_range_begin").html(changeUnkownValue(data.time_range_begin)+"&nbsp;");
		//$("#show_update_account_p_time_range_end").html(changeUnkownValue(data.time_range_end)+"&nbsp;");
		var status = data.status;
		if(status==0){
			$(".show_update_account_p_status").eq(0).removeAttr("checked");
		}
		var account = changeUnkownValue(data.account);
		var enterpriseUrl = "";
		if(account){
			var s = account.indexOf("@");
			enterpriseUrl = account.substring(s,account.length);
			account = account.substring(0,s);
		}
		//人员ID
		$("#update_orgUserId").val(data.org_user_id);
		$("#update_account_p_account").val(changeUnkownValue(account));
		$("#update_account_p_enterpriseUrl").html(changeUnkownValue(enterpriseUrl));
		$("#update_account_p_password").val(changeUnkownValue(data.password));
		$("#update_account_p_comfigPwd").val(changeUnkownValue(data.password));
		$("#update_account_p_name").val(changeUnkownValue(data.name));
		$("input[name='update_account_p_sex']").each(function(){
			if($(this).val() == changeUnkownValue(data.gender)){
				$(this).attr("checked",true);
			}
		});
		$("#update_account_p_cellPhoneNumber").val(changeUnkownValue(data.mobile));
		$("#update_account_p_email").val(changeUnkownValue(data.email));
		$("#update_account_p_backUpEmailAddress").val(changeUnkownValue(data.backUpEmail));
		$("#update_account_p_mobileEmailAddress").val(changeUnkownValue(data.mobileemail));
		//$("#update_account_p_time_range_begin").val(changeUnkownValue(data.time_range_begin));
		//$("#update_account_p_time_range_end").val(changeUnkownValue(data.time_range_end));
		if(status==0){
			$(".update_account_p_status").eq(0).removeAttr("checked");
		}
		
		$("#show_update_staff_p_sex").html(changeUnkownValue(sexChiese)+"&nbsp;");
		$("#show_update_staff_p_type").html(changeUnkownValue(data.roleName)+"&nbsp;");
		$("#show_update_staff_p_birthday").html(changeUnkownValue(data.birthday)+"&nbsp;");
		//$("#show_update_staff_p_graduateDate").html(changeUnkownValue(data.graduateDate)+"&nbsp;");
		//$("#show_update_staff_p_degree").html(changeUnkownValue(data.degree)+"&nbsp;");
		$("#show_update_staff_p_identityCard").html(changeUnkownValue(data.idcard)+"&nbsp;");
		
		$("#update_staff_p_sex").children().each(function(){
			if($(this).val()==data.gender){
				$(this).attr("selected","selected");
			}
		});
		
		$("#update_staff_p_type").children().each(function(){
			if($(this).val()==data.role_type_id){
				$(this).attr("selected","selected");
			}
		});
		
		$("#update_staff_p_birthday").val(data.birthdayStr);
		//$("#update_staff_p_graduateDate").val(data.graduateDateStr);
		//$("#update_staff_p_degree").val(data.degree);
		$("#update_staff_p_identityCard").val(data.idcard);
		
		$("#staff_tab").children().children().each(function(index){
			if(index==0){
				$(this).attr("class","selected");
			}else{
				$(this).attr("class","");
			}
		});
		loadProviderOrgRoleInfo("update_staff_p_type");
		var roleIdList = data.roleIdList;
		if(roleIdList){
			$.each(roleIdList,function(index2,value2){
				$.each($(".org_role_class"),function(){
					if($(this).val()==value2.roleId){
						$(this).attr("checked","checked");
						return false;
					}
				});
			});
		}
		getProviderOrgRole();
		/*
		var orgId = $("#orgId").val();
		$.post("getProviderOrgRoleAction",{"orgId":orgId},function(data1){
			var str = "";
			$.each(data1,function(index1,value1){
				str +="<li><input class='org_role_class' type='checkbox' value='"+value1.roleId+"' /><em>"+value1.name+"</em></li>";
			});
			
			$("#org_p_org_role").html(str);
			
			getProviderOrgRole();
		},"json");
		
		$.post("getProviderBizRoleAction",{"orgId":orgId},function(data1){
			var urgentrepair = "";
			if(data1){
				$.each(data1.urgentrepair,function(index1,value1){
					urgentrepair +="<li><input class='biz_role_class' type='checkbox' value='"+value1.roleId+"' /><em>"+value1.name+"</em></li>";
				});
			}
			$("#business_role_0").html(urgentrepair);
			
			getProviderBizRole();
		},"json");
		*/
		
	},'json');
}

//检测账号（修改）
function checkUpdateProviderAccount(divId){
	var enterpriseUrl = $("#"+divId+"_enterpriseUrl").html();
	var showAccount = $("#show_"+divId+"_account").html();
	var account = $("#"+divId+"_account").val();
	var account1 = account + enterpriseUrl +"&nbsp;";
	if(showAccount==account1){
		alert("该账号可用");
		return;
	}
	if(account==""){
		alert("必填字段");
		return;
	}
	if(!(/^[-\.\w#%&:?=\/]+$/i).test(account)){
		alert("输入格式错误");
		return;
	}
	if($.trim(account)!=""){
		if($.trim(account).length > 20){
			alert("超过20字符");
			return;
		}
	}
	account = account + enterpriseUrl;
	$.post("../system/checkAccountAjaxAction",{"account":account},function(data){
		if(data=="success"){
			alert("该账号可用");
		}else{
			alert("该账号已经被使用");
		}
	});
}

//修改账号信息
function updateProviderAccountInfo(){
	var orgId = $("#orgId").val();
	var account = $.trim($("#update_account_p_account").val());
	var enterpriseUrl = $.trim($("#update_account_p_enterpriseUrl").html());
	var type= $.trim($("#update_staff_p_type").val());
	var password = $.trim($("#update_account_p_password").val());
	var comfigPwd = $.trim($("#update_account_p_comfigPwd").val());
	var name = $.trim($("#update_account_p_name").val());
	var sex = changeUnkownValue($("input[name='update_account_p_sex']:checked").val());
	var cellPhoneNumber = $.trim($("#update_account_p_cellPhoneNumber").val());
	var mobileEmailAddress = $.trim($("#update_account_p_mobileEmailAddress").val());
	var backUpEmailAddress = $.trim($("#update_account_p_backUpEmailAddress").val());
	//var time_range_begin = $("#update_account_p_time_range_begin").val();
	//var time_range_end = $("#update_account_p_time_range_end").val();
	//var isEnterprise = $('input:radio[name="update_account_p_isEnterprise"]:checked').val();
	
	var state = "0";
	$(".show_update_account_p_status").removeAttr("checked");
	if($(".update_account_p_status").eq(0).attr("checked")=="checked"){
		state = "1";
		$(".show_update_account_p_status").attr("checked","checked");
	}
	
	var email = $.trim($("#update_account_p_email").val());
	var roleIdList = $("#roleIdList").val();
	var bizRoleIdList = $("#bizRoleIdList").val();
	
	if(roleIdList==""){
		roleIdList = bizRoleIdList;
	}else{
		if(bizRoleIdList!=""){
			roleIdList += ","+ bizRoleIdList;
		}
	}
	var flag = "true";
	var errorStr = "";
	var showAccount = $("#show_update_account_p_account").html();
	var account1 = account+enterpriseUrl+"&nbsp;";
	if(showAccount!=account1){
		$.ajax({
			url: "../system/checkAccountIsExistAjaxAction",
			async:false,
			type:"POST",
			dataType : 'json',
			data: {"account":account+enterpriseUrl} ,
			success : function(result) {
				if(result == "found"){
					errorStr = "该账号已经被使用";
					flag = "false";
				}
			}
		});
	}
	//账号提示
	if($.trim(account)==""){
		errorStr = "必填字段";
		flag = "false";
	}
	//账号提示
	if($.trim(account)!=""){
		if(!(/^[-\.\w#%&:?=\/]+$/i).test(account)){
			errorStr = "输入格式错误";
			flag = "false";
		}
	}
	if($.trim(account)!=""){
		if($.trim(account).length > 20){
			errorStr = "超过20字符";
			flag = "false";
		}
	}
	getErrorPrompt("update_account_p_account",errorStr);
	errorStr = "";
	//用户群提示
	if($.trim(type)=="0" || type==null){
		errorStr = "必填字段";
		flag = "false";
	}
	getErrorPrompt("update_staff_p_type",errorStr);
	errorStr = "";
	//密码提示
	if(password==""){
		errorStr = "必填字段";
		flag = "false";
	}else if(password.length<6){
		errorStr = "密码不能小于6位数";
		flag = "false";
	}else if(!(/[0-9]+/).test(password) || !(/[A-Za-z]+/).test(password)){
		errorStr = "密码要包含英文和数字";
		flag = "false";
	}else if(password.length > 40){
		errorStr = "超过40字符";
		flag = "false";
	}
	getErrorPrompt("update_account_p_password",errorStr);
	errorStr = "";
	//确认密码提示
	if(password!=comfigPwd){
		errorStr = "两次密码不一致";
		flag = "false";
	}
	getErrorPrompt("update_account_p_comfigPwd",errorStr);
	errorStr = "";
	//名字提示
	if($.trim(name)==""){
		errorStr = "必填字段";
		flag = "false";
	}else if(name.length > 20){
		errorStr = "超过20字符";
		flag = "false";
	}
	//手机号码提示
	if(cellPhoneNumber==""){
		errorStr = "必填字段";
		flag = "false";
	}else if(!(/^(0|[1-9]\d*)$/).test(cellPhoneNumber)){
		errorStr = "输入格式错误";
		flag = "false";
	}else if(cellPhoneNumber.length > 11){
		errorStr = "超过11字符";
		flag = "false";
	}
	getErrorPrompt("update_account_p_cellPhoneNumber",errorStr);
	errorStr = "";
	//邮箱提示
	if(email==""){
		errorStr = "必填字段";
		flag = "false";
	}else if(!(/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/).test(email)){
		errorStr = "输入格式错误";
		flag = "false";
	}else if(email.length > 50){
		errorStr = "超过50字符";
		flag = "false";
	}
	getErrorPrompt("update_account_p_email",errorStr);
	errorStr = "";
	//手机邮箱提示
	/*if(mobileEmailAddress==""){
		errorStr = "必填字段";
		flag = "false";
	}else*/
	 if(!(/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/).test(mobileEmailAddress)){
		errorStr = "输入格式错误";
		flag = "false";
	}else if($.trim(email)==$.trim(mobileEmailAddress)){
		errorStr = "手机邮箱与其他邮箱重复";
		flag = "false";
	}else if(mobileEmailAddress.length > 50){
		errorStr = "超过50字符";
		flag = "false";
	}
	getErrorPrompt("update_account_p_mobileEmailAddress",errorStr);
	errorStr = "";
	//备用邮箱提示
	if($.trim(backUpEmailAddress)!=""){
		if(!(/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/).test(backUpEmailAddress)){
			errorStr = "请输入正确的e-mail格式";
			flag = "false";
		}else if($.trim(backUpEmailAddress)==$.trim(mobileEmailAddress) || $.trim(backUpEmailAddress)==$.trim(email)){
			errorStr = "备用邮箱与其他邮箱重复";
			flag = "false";
		}else if(backUpEmailAddress.length > 50){
			errorStr = "超过50字符";
			flag = "false";
		}
	}
	getErrorPrompt("update_account_p_backUpEmailAddress",errorStr);
	errorStr = "";
	/*
	//是否属于该企业提示
	if(!isEnterprise){
		errorStr = "请选择";
		flag = "false";
	}
	getErrorPrompt("update_account_p_isEnterprise",errorStr);
	errorStr = "";
	*/
	
	if(flag == "true"){
		account = account + enterpriseUrl;
		//showAccount = showAccount.substring(0,showAccount.length-6);
		var jsonStr = "{";
		jsonStr += "'orgId':'"+orgId+"',";
		jsonStr += "'orgUserId':'"+$("#update_orgUserId").val()+"',";
		//jsonStr += "'type':'"+type+"',";
		jsonStr += "'roleIdList':'"+roleIdList+"',";
		jsonStr += "'account':'"+account+"',";
		jsonStr += "'password':'"+password+"',";
		jsonStr += "'name':'"+name+"',";
		jsonStr += "'gender':'"+sex+"',";
		jsonStr += "'mobile':'"+cellPhoneNumber+"',";
		jsonStr += "'mobileemail':'"+mobileEmailAddress+"',";
		jsonStr += "'backupemail':'"+backUpEmailAddress+"',";
		//jsonStr += "'time_range_begin':'"+time_range_begin+"',";
		//jsonStr += "'time_range_end':'"+time_range_end+"',";
		jsonStr += "'status':'"+state+"',";
		//jsonStr += "'showAccount':'"+showAccount+"',";
		//jsonStr += "'isEnterprise':'"+isEnterprise+"',";
		jsonStr += "'email':'"+email+"'";
		jsonStr += "}";
		jsonStr = encodeURIComponent(jsonStr);
		$.ajax({
			url: "../system/updateStaffAndUserRoleAction",
			async:false,
			type:"POST",
			data: {"orgJsonStr":jsonStr},
			success : function(result) {
				if(result=="failed"){
				alert("修改失败！");
				return;
				}
				showUpdateProviderAccountInfo($("#update_orgUserId").val());
			}
		});
	}
}

//显示人员修改的tag
function showUpdateProviderStaffInfo(){
	$(".staff_info_other .staffInfo_value").hide();
	$(".staff_info_other .staffInfo_input").show();
	$(".staff_other_but .staffInfo_modify").hide();
	$(".staff_other_but .staffInfo_save").show();
}

//修改人员信息
function updateProviderStaffInfo(){
	var sex = $("#update_staff_p_sex").val();
	
	var birthday = $("#update_staff_p_birthday").val();
	var graduateDate = $("#update_staff_p_graduateDate").val();
	var degree = $("#update_staff_p_degree").val();
	var identityCard = $("#update_staff_p_identityCard").val();
	var account = $("#show_update_account_p_account").html();
	if(account!=""){
		account = account.substring(0,account.length-6);
	}
	
	var jsonStr = "{";
	jsonStr += "'sex':'"+sex+"',";
	
	jsonStr += "'account':'"+account+"',";
	jsonStr += "'birthday':'"+birthday+"',";
	jsonStr += "'graduateDate':'"+graduateDate+"',";
	jsonStr += "'degree':'"+degree+"',";
	jsonStr += "'identityCard':'"+identityCard+"'";
	jsonStr += "}";
	jsonStr = encodeURIComponent(jsonStr);
	$.post("updateProiderStaffAjaxAction",{"orgJsonStr":jsonStr},function(data){
		if("success"==data){
			$("#show_update_staff_p_sex").html(sex+"&nbsp;");
			var typeName = "";
			$("#update_staff_p_type").children().each(function(){
				if($(this).attr("selected")=="selected"){
					typeName = $(this).text();
				}
			});
			
			var orgId = $("#orgId").val();
			showProviderAccountInfo(orgId);
			
			$("#show_update_staff_p_type").html(typeName+"&nbsp;");
			$("#show_update_staff_p_account").html(account+"&nbsp;");
			$("#show_update_staff_p_birthday").html(birthday+"&nbsp;");
			$("#show_update_staff_p_graduateDate").html(graduateDate+"&nbsp;");
			$("#show_update_staff_p_degree").html(degree+"&nbsp;");
			$("#show_update_staff_p_identityCard").html(identityCard+"&nbsp;");
			
			$("#show_update_account_p_sex").html(sex+"&nbsp;");
			$("input[name='update_account_p_sex']").each(function(){
				if($(this).val() == changeUnkownValue(sex)){
					$(this).attr("checked",true);
				}
			});
			
			$(".staff_info_other .staffInfo_value").show();
			$(".staff_info_other .staffInfo_input").hide();
			$(".staff_other_but .staffInfo_modify").show();
			$(".staff_other_but .staffInfo_save").hide();
		}
	},"json");
}

//删除人员信息
function deleteProviderStaffInfo(){
	var orgUserId = "";
	var single = 0;
	$(".org_p_staff_list").each(function(){
		if($(this).attr("checked")){
			single = 1;
			orgUserId = $(this).val();
			return false;
		}
	});
	if(single==0){
		alert("请选择要删除的人员");
		return;
	}else{
		if(confirm("确定要删除吗？")){	
			var orgId = $("#orgId").val();
			$.post("deleteProiderAccountAndOrgAjaxAction",{"orgUserId":orgUserId,"orgId":orgId},function(data){
				showProviderAccountInfo(orgId);
			},"json");
		}
	}
}

function setNormalAccount(account){
	var orgId = $("#orgId").val();
	$.post("../system/setNormalAccountAction",{"account":account},function(data){
		if(data == true){
			alert("账号解锁成功");
			showProviderAccountInfo(orgId);
		}else{
			alert("账号解锁失败");
		}
	},"json");
}

//模糊查询
function orgShowStaffFuzzy(){
	var orgId = $("#orgId").val();
	var fuzzy = $("#org_p_staff_fuzzy").val();
	fuzzy = encodeURIComponent(fuzzy);
	var pageDivId = "org_p_staff_page";
	var showDivId = "org_p_staff_table";
	var actionName = "../system/getProviderSystemStaffByOrgIdAction";
	var pageSize = "10";
	var param={
		orgId : orgId,
		conditions : fuzzy,
		currentPage : "1",
		pageSize : pageSize
	};
	pagingColumnByBackgroundJsp(pageDivId,showDivId,actionName,param);
	
	
	/*
	$.post("getProviderStaffByOrgIdAndFuzzyAction",{"orgId":orgId,"fuzzy":fuzzy},function(data){
		if(data){
			var array = new Array();
			$.each(data,function(index,value){
				$.each($(".org_p_select_staff_list"),function(){
					if($(this).val()==value){
						$(this).parent().parent().show();
						array.push($(this).parent().parent());
					}
				});
			});
			var showList = array;
			var pageDivId = "org_p_staff_page";
			var pageSize = "5";
			pagingColumnByForeground(pageDivId,showList,pageSize);
		}
	},"json");
	*/
}

/**
 *客户组织
 */

//客户商组织的准备
function customerOrgReady(){
	//添加组织类型的数据字典
	addDictionary("getDictionaryByTypeAction","em_org_c_orgType","org_c_type","orgType");
	//添加组织属性的数据字典
	addDictionary("getDictionaryByTypeAction","em_org_c_orgAttribute","org_c_orgAttribute","businessType");
	//添加企业的数据字典
	addDictionary("getDictionaryByTypeAction","org_c_select_enterpriseId","org_c_enterpriseId","");
}

//填写组织名
function addCOrgName(dataStr,tableId){
	var data = eval("(" + dataStr + ")");
	var orgName = data.name;
	var orgId = data.orgId;
	$("#update_org_c_parentOrgId").val(orgId);
	$("#update_org_c_parentOrgName").val(orgName);
	$("#org_c_highOrgTree").slideToggle("fast");
}
 
//生成客户组织树
function getCustomerOrgTree(){
	var enterpriseId = $("#enterpriseId").val();
	var isCoo = $("#isCoo").val();
	if(isCoo=="systemManager"){
		enterpriseId = $("#org_c_enterpriseIds").val();
	}
	var values = {"enterpriseIds":enterpriseId,"enterpriseType":"CARRIEROPERATOR"};//yuan.yw
	var myUrl = "getAllProviderOrgTreeAction";
	$.post(myUrl,values,function(data){
		createOrgTreeOpenFirstNode(data,"customerTreeDiv","c_personnel_tree","customerOrgDiv","showCustomerOrgInfo");
		//生成上级组织树
		createOrgTreeOpenFirstNode(data,"org_c_highOrgTree","c_personnel_tree1","","addCOrgName");
	},"json");
	customerOrgReady();
}

function showCustomerOrgInfo(dataStr,tableId){
	var data = eval("(" + dataStr + ")");
	var orgName = data.name;
	var orgId = data.orgId;
	$("#"+tableId).show();
	$("#c_orgMainName").html(orgName);
	
	//显示组织架构的基本信息
	showCustomerOrgBaseInfo(orgId);
	
	//显示服务商组织的管辖人员信息
	showCustomerAccountInfo(orgId);
}

//显示客户商组织的基本信息
function showCustomerOrgBaseInfo(orgId){
	$.post("getProviderOrgByOrgIdAction",{"orgId":orgId,pageType:"operator"},function(data){//yuan.yw
		$("#client_tab_0").html(data);
		$("#c_orgId").val(orgId);
		$("#orgId").val(orgId);
		$("#enterpriseId").val($("#show_org_c_enterpriseId").val());
		$("#add_org_c_parentOrgName").html($("#show_org_c_parentOrgName").val());
		
		var enterpriseId = $("#show_org_c_enterpriseId").val();
		var values = {"enterpriseIds":enterpriseId,"enterpriseType":"CARRIEROPERATOR"};//yuan.yw
		var myUrl = "getAllProviderOrgTreeAction";
		$.post(myUrl,values,function(data){
			//生成上级组织树
			createOrgTreeOpenFirstNode(data,"org_c_highOrgTree","c_personnel_tree1","","addCOrgName");
		},"json");
	});
}

//关闭客户窗体
function closeCustomerOrgBaseInfoWin(){
	$("#customerInfo_Dialog").hide();
	$(".black").hide();
}

//显示添加客户商组织
function addCustomerOrgBaseInfo(){
	$("#customerInfo_Dialog").show();
	$(".black").show();
	//错误提示清空
	areaServerTree("org_c_area_server_treeDiv","org_c_areaTree","org_c","areaServerTreeClick");
	$(".org_c_error").html("");
	changeValueEmpty("org_c_orgInfo_div");
	$("#org_c_dutyPerson").html("<option value=''>请选择</option>");
	$(".add_c_baseInfo").show();
	$(".update_c_baseInfo").hide();
	$("#org_c_highOrgTree").hide();
	var enterpriseId = $("#enterpriseId").val();
	$.each($("#org_c_select_enterpriseId").children().children(),function(){
		if($(this).val()==enterpriseId){
			$(this).attr("selected","selected");
			return false;
		}
	});
	$("#add_org_c_parentOrgName").html($("#show_org_c_name").html());
}

//保存添加客户商组织
function confirmAddCustomerOrgBaseInfo(){
	var flag = "true";
	var errorStr = "";
	var orgName = $("#org_c_name").val();
	if(orgName==""){
		errorStr = "必填字段";
		flag = "false";
	}
	if(orgName!="" && orgName.length > 50){
		errorStr = "超过50字符";
		flag = "false";
	}
	getErrorPrompt("org_c_name",errorStr);
	errorStr = "";
	
	var orgAttribute = $("#em_org_c_orgAttribute").children().eq(0).val();
	if(orgAttribute==""){
		errorStr = "必填字段";
		flag = "false";
	}
	getErrorPrompt("em_org_c_orgAttribute",errorStr);
	errorStr = "";
	/*
	var orgType = $("#em_org_c_orgType").children().eq(0).val();
	if(orgType==""){
		errorStr = "必选字段";
		flag = "false";
	}
	getErrorPrompt("em_org_c_orgType",errorStr);
	errorStr = "";
	*/
	
	var enterprise = $("#org_c_select_enterpriseId").children().eq(0).val();
	if(enterprise==""){
		errorStr = "必填字段";
		flag = "false";
	}
	getErrorPrompt("org_c_select_enterpriseId",errorStr);
	errorStr = "";
	
	//驻点位置
	var address = $.trim($("#org_c_address").val());
	if(address.length > 100){
		errorStr = "超过100字符";
		flag = "false";
	}
	getErrorPrompt("org_c_address",errorStr);
	errorStr = "";
	
	//组织职责
	var orgDuty = $.trim($("#org_c_orgDuty").val());
	if(orgDuty.length > 100){
		errorStr = "超过100字符";
		flag = "false";
	}
	getErrorPrompt("org_c_orgDuty",errorStr);
	errorStr = "";
	
	//驻点经度
	var longitude = $.trim($("#org_c_longitude").val());
	if(longitude!=""){
		if(longitude.length > 20){
			errorStr = "超过20字符";
			flag = "false";
		}
		if(longitude!=0){
			if(!(/^d+$/).test(longitude)){
				if(!(/^-?\d+\.?\d+$/).test(longitude)){
					errorStr = "输入格式错误";
					flag = "false";
				}else if(longitude < -180 || longitude > 180){
					errorStr = "经度超过范围";
					flag = "false";
				}
			}
		}
	}
	getErrorPrompt("org_c_longitude",errorStr);
	errorStr = "";
	
	//驻点纬度
	var latitude = $.trim($("#org_c_latitude").val());
	if(latitude!=""){
		if(latitude.length > 20){
			errorStr = "超过20字符";
			flag = "false";
		}
		if(longitude!=0){
			if(!(/^d+$/).test(latitude)){
				if(!(/^-?\d+\.?\d+$/).test(latitude)){
					errorStr = "输入格式错误";
					flag = "false";
				}else if(latitude < -90 || latitude > 90){
					errorStr = "纬度超过范围";
					flag = "false";
				}
			}
		}
	}
	getErrorPrompt("org_c_latitude",errorStr);
	errorStr = "";
	
	//驻点电话
	var contactPhone = $.trim($("#org_c_contactPhone").val());
	if(contactPhone!=""){
		if(contactPhone.length > 20){
			errorStr = "超过20字符";
			flag = "false";
		}
		if(!(/^(\(\d{3,4}\)|\d{3,4}-)?\d*$/).test(contactPhone)){
			errorStr = "输入格式错误";
			flag = "false";
		}
	}
	getErrorPrompt("org_c_contactPhone",errorStr);
	errorStr = "";
	
	//负责人电话
	var dutyPersonPhone = $.trim($("#org_c_dutyPersonPhone").val());
	if(dutyPersonPhone!=""){
		if(dutyPersonPhone.length > 20){
			errorStr = "超过20字符";
			flag = "false";
		}
		if(!(/^(\(\d{3,4}\)|\d{3,4}-)?\d*$/).test(dutyPersonPhone)){
			errorStr = "输入格式错误";
			flag = "false";
		}
	}
	getErrorPrompt("org_c_dutyPersonPhone",errorStr);
	errorStr = "";
	
	if(flag=="true"){
		var jsonStr = getCOrgJsonStr();
		jsonStr = encodeURIComponent(jsonStr);
		$.post("saveProviderOrgInfoAjaxAction",{"orgJsonStr":jsonStr},function(data){
			$("#customerInfo_Dialog").hide();
			$(".black").hide();
			getCustomerOrgTree();
		},"json");
	}
}

//弹出修改客户商组织的基本信息
function updateCustomerOrgBaseInfo(){
	$("#customerInfo_Dialog").show();
	$(".black").show();
	areaServerTree("org_c_area_server_treeDiv","org_c_areaTree","org_c","areaServerTreeClick");
	if($("#c_areaIds").val()!=""){
		var areaIds = $("#c_areaIds").val();
		var areaArray = areaIds.split(",");
		for(var i=0;i<areaArray.length;i++){
			$("#org_c_area_server_treeDiv input[name='areaCheckBox'][value='"+areaArray[i]+"']").attr("checked","checked");
		}
	}
	//添加负责人的下拉列表
	addDutyPersonDictionary("org_c_dutyPerson","em_org_c_dutyPerson");
	//错误提示清空
	$(".org_c_error").html("");
	
	$("#org_c_name").val($("#show_org_c_name").html());
	/*
	var type = $("#show_org_c_type").html();
	$.each($("#em_org_c_orgType").children().children(),function(){
		if($(this).text()==type){
			$(this).attr("selected","selected");
			return false;
		}
	});
	*/
	var orgAttribute = $("#show_org_c_orgAttribute").html();
	$.each($("#em_org_c_orgAttribute").children().children(),function(){
		if($(this).text()==orgAttribute){
			$(this).attr("selected","selected");
			return false;
		}
	});
	
	var dutyPerson = $("#show_org_c_dutyPerson").html();
	$.each($("#em_org_c_dutyPerson").children().children(),function(){
		if($(this).val()==dutyPerson){
			$(this).attr("selected","selected");
			return false;
		}
	});
	
	$("#org_c_orgDuty").val($("#show_org_c_orgDuty").html());
	$("#org_c_address").val($("#show_org_c_address").html());
	$("#org_c_longitude").val($("#show_org_c_longitude").val());
	$("#org_c_latitude").val($("#show_org_c_latitude").val());
	$("#org_c_contactPhone").val($("#show_org_c_contactPhone").html());
	$("#org_c_dutyPersonPhone").val($("#show_org_c_dutyPersonPhone").html());
	$("#org_c_dutyPerson").val($("#show_org_c_dutyPerson").html());
	$("#update_org_c_parentOrgName").val($("#show_org_c_parentOrgName").val());
	$("#update_org_c_parentOrgId").val($("#show_org_c_parentOrgId").val());
	$("#update_org_c_enterpriseId").html($("#show_org_c_enterpriseName").html());
	$("#org_c_areaId").val($("#show_org_c_areaId").val());
	$("#org_c_areaname").val($("#show_org_c_areaName").val());
	$(".add_c_baseInfo").hide();
	$(".update_c_baseInfo").show();
	$("#org_c_highOrgTree").hide();
}

//确认修改服务商组织的基本信息
function confirmUpdateCustomerOrgBaseInfo(){
	var orgId = $("#c_orgId").val();
	var flag = "true";
	var errorStr = "";
	var orgName = $("#org_c_name").val();
	if(orgName==""){
		errorStr = "必填字段";
		flag = "false";
	}
	if(orgName!="" && orgName.length > 50){
		errorStr = "超过50字符";
		flag = "false";
	}
	getErrorPrompt("org_c_name",errorStr);
	errorStr = "";
	
	var orgAttribute = $("#em_org_c_orgAttribute").children().eq(0).val();
	if(orgAttribute==""){
		errorStr = "必填字段";
		flag = "false";
	}
	getErrorPrompt("em_org_c_orgAttribute",errorStr);
	errorStr = "";
	
	var orgType = $("#em_org_c_orgType").children().eq(0).val();
	if(orgType==""){
		errorStr = "必选字段";
		flag = "false";
	}
	getErrorPrompt("em_org_c_orgType",errorStr);
	errorStr = "";
	
	var parentOrgId = $("#update_org_c_parentOrgId").val();
	if(parentOrgId==orgId){
		errorStr = "上级组织与当前组织重复";
		flag = "false";
	}
	getErrorPrompt("update_org_c_parentOrgName",errorStr);
	errorStr = "";
	
		//驻点位置
	var address = $.trim($("#org_c_address").val());
	if(address.length > 100){
		errorStr = "超过100字符";
		flag = "false";
	}
	getErrorPrompt("org_c_address",errorStr);
	errorStr = "";
	
	//组织职责
	var orgDuty = $.trim($("#org_c_orgDuty").val());
	if(orgDuty.length > 100){
		errorStr = "超过100字符";
		flag = "false";
	}
	getErrorPrompt("org_c_orgDuty",errorStr);
	errorStr = "";
	
	//驻点经度
	var longitude = $.trim($("#org_c_longitude").val());
	if(longitude!=""){
		if(longitude.length > 20){
			errorStr = "超过20字符";
			flag = "false";
		}
		if(longitude!=0){
			if(!(/^d+$/).test(longitude)){
				if(!(/^-?\d+\.?\d+$/).test(longitude)){
					errorStr = "输入格式错误";
					flag = "false";
				}else if(longitude < -180 || longitude > 180){
					errorStr = "经度超过范围";
					flag = "false";
				}
			}
		}
	}
	getErrorPrompt("org_c_longitude",errorStr);
	errorStr = "";
	
	//驻点纬度
	var latitude = $.trim($("#org_c_latitude").val());
	if(latitude!=""){
		if(latitude.length > 20){
			errorStr = "超过20字符";
			flag = "false";
		}
		if(latitude!=0){
			if(!(/^d+$/).test(latitude)){
				if(!(/^-?\d+\.?\d+$/).test(latitude)){
					errorStr = "输入格式错误";
					flag = "false";
				}else if(latitude < -90 || latitude > 90){
					errorStr = "纬度超过范围";
					flag = "false";
				}
			}
		}
	}
	getErrorPrompt("org_c_latitude",errorStr);
	errorStr = "";
	
	//驻点电话
	var contactPhone = $.trim($("#org_c_contactPhone").val());
	if(contactPhone!=""){
		if(contactPhone.length > 20){
			errorStr = "超过20字符";
			flag = "false";
		}
		if(!(/^(\(\d{3,4}\)|\d{3,4}-)?\d*$/).test(contactPhone)){
			errorStr = "输入格式错误";
			flag = "false";
		}
	}
	getErrorPrompt("org_c_contactPhone",errorStr);
	errorStr = "";
	
	//负责人电话
	var dutyPersonPhone = $.trim($("#org_c_dutyPersonPhone").val());
	if(dutyPersonPhone!=""){
		if(dutyPersonPhone.length > 20){
			errorStr = "超过20字符";
			flag = "false";
		}
		if(!(/^(\(\d{3,4}\)|\d{3,4}-)?\d*$/).test(dutyPersonPhone)){
			errorStr = "输入格式错误";
			flag = "false";
		}
	}
	getErrorPrompt("org_c_dutyPersonPhone",errorStr);
	errorStr = "";
	
	if(flag=="true"){
		var jsonStr = getCOrgJsonStr();
		jsonStr = encodeURIComponent(jsonStr);
		$.post("updateProviderOrgInfoAjaxAction",{"orgJsonStr":jsonStr},function(data){
			$("#customerInfo_Dialog").hide();
			$(".black").hide();
			showCustomerOrgBaseInfo($("#c_orgId").val());
			getCustomerOrgTree();
		},"json");
	}
	
}

//点击服务商组织的基本信息的删除按钮
function deleteCustomerOrgBaseInfo(){
	$("#personnelDelete_Dialog").show();
	$(".black").show();
	$("#delete_p_baseInfo").html($("#show_org_c_name").html());
}


//运营商管辖人员=======================================================================
//显示服务商组织的管辖人员信息
function showCustomerAccountInfo(orgId){
	var pageDivId = "org_c_staff_page";
	var showDivId = "org_c_staff_table";
	var actionName = "../system/getProviderSystemStaffByOrgIdAction";
	var pageSize = "10";
	var param={
		orgId:orgId,
		currentPage:"1",
		pageSize:pageSize
	};
	pagingColumnByBackgroundJsp(pageDivId,showDivId,actionName,param);
	/*
	$.post("getProviderStaffByOrgIdAction",{"orgId":orgId},function(data){
		$("#service_tab_1").html(data);
	});*/
}

//显示添加运营商
function showAddCustomerAccount(){
	$("#serviceAddStaff_c_Dialog").show();
	$(".black").show();
	var enterpriseId = $("#enterpriseId").val();
	var enterpriseUrl = "";
	$.post("../system/getSysEnterpriseUrlDictionaryAjaxAction",function(data){
		if(data){
			$.each(data,function(index,value){
				if(value.typeCode==enterpriseId){
					enterpriseUrl = value.name;
					return false;
				}
			});
		}
		$("#add_account_c_enterpriseUrl").html(enterpriseUrl);
	},"json");
	$(".add_account_c_error").html("");
	$("#add_account_c_account").val("");
	$("#add_account_c_password").val("");
	$("#add_account_c_comfigPwd").val("");
	$("#add_account_c_name").val("");
	$("#add_account_c_cellPhoneNumber").val("");
	$("#add_account_c_email").val("");
	$("#add_account_c_email").val("");
	$("#add_account_c_mobileEmailAddress").val("");
	$("#add_account_c_backUpEmailAddress").val("");
	loadProviderOrgRole("org_p_org_role");
	var myDate = new Date();
	var year = myDate.getFullYear();
	var month = myDate.getMonth();
	var date = myDate.getDate();
	$("#add_account_c_time_range_begin").val(year+"-"+(month+1)+"-"+date);
	$("#add_account_c_time_range_end").val((year+1)+"-"+(month+1)+"-"+date);
	$(".add_account_c_status").eq(0).attr("checked","checked");
	
	
	$("#account_title").attr("class","selected");
	$("#staff_title").attr("class","");
	$("#skill_title").attr("class","");
	$("#staff_title").hide();
	$("#skill_title").hide();
	$("#staffadd_tab_0").show();
	$("#staffadd_tab_1").hide();
	$("#staffadd_tab_2").hide();
	$(".staffadd_info_account .staffInfo_value").hide();
	$(".staffadd_account_but").show();
	$(".staffadd_info_account .staffInfo_input").show();
	
	$(".staffInfo_input").show();
	$(".staffInfo_value").hide();
	
	$("#show_add_org_p_org_role").html("");
	$("#show_add_org_p_biz_role").html("");
	$("ul[name='org_p_biz_role']").each(function(){
		$(this).html("");
	});
	$("#orgRole_tableId").val("show_add_org_p_org_role_1");
	$("#bizRole_tableId").val("show_add_org_p_biz_role");
	$(".add_account_p_isEnterprise").removeAttr("checked");
	$("#add_staff_p_type").val("");
}

//关闭添加运营商
function closeAddCustomerAccount(){
	$("#serviceAddStaff_c_Dialog").hide();
	$(".black").hide();
}

//保存服务商的账号信息
function saveCustomerAccountInfo(){
	//var isEnterprise = $('input:radio[name="add_account_p_isEnterprise"]:checked').val();
	var enterpriseId = $("#enterpriseId").val();
	var orgId = $("#orgId").val();
	var account = $.trim($("#add_account_c_account").val());
	var password = $.trim($("#add_account_c_password").val());
	var comfigPwd = $.trim($("#add_account_c_comfigPwd").val());
	var name = $.trim($("#add_account_c_name").val());
	var sex = $("input[name='add_account_c_sex']:checked").val();
	var cellPhoneNumber = $.trim($("#add_account_c_cellPhoneNumber").val());
	var mobileEmailAddress = $.trim($("#add_account_c_mobileEmailAddress").val());
	var backUpEmailAddress = $.trim($("#add_account_c_backUpEmailAddress").val());
	var time_range_begin = $("#add_account_c_time_range_begin").val();
	var time_range_end = $("#add_account_c_time_range_end").val();
	var state = "0";
	$(".show_add_account_c_status").removeAttr("checked");
	if($(".add_account_c_status").eq(0).attr("checked")=="checked"){
		state = "1";
		$(".show_add_account_c_status").attr("checked","checked");
	}
	var email = $.trim($("#add_account_c_email").val());
	//var type = $("#add_staff_c_type").val();
	var type = "OperationCustomer";
	var roleIdList = $("#roleIdList").val();
	var bizRoleIdList = $("#bizRoleIdList").val();
	if(roleIdList==""){
		roleIdList = bizRoleIdList;
	}else{
		if(bizRoleIdList!=""){
			roleIdList += ","+ bizRoleIdList;
		}
	}
	var enterpriseUrl = $.trim($("#add_account_c_enterpriseUrl").html());
	var flag = "true";
	var errorStr = "";
	$.ajax({
		url: "../system/checkAccountAjaxAction",
		async:false,
		type:"POST",
		dataType : 'json',
		data: {"account":account+enterpriseUrl} ,
		success : function(result) {
			if(result!="success"){
				errorStr = "该账号已经被使用";
				flag = "false";
			}
		}
	});
	//账号提示
	if($.trim(account)==""){
		errorStr = "必填字段";
		flag = "false";
	}
	//账号提示
	if($.trim(account)!=""){
		if(!(/^[-\.\w#%&:?=\/]+$/i).test(account)){
			errorStr = "输入格式错误";
			flag = "false";
		}
	}
	if($.trim(account)!=""){
		if($.trim(account).length > 20){
			errorStr = "超过20字符";
			flag = "false";
		}
	}
	getErrorPrompt("add_account_c_account",errorStr);
	errorStr = "";
	/*
	//用户群提示
	if($.trim(type)=="0"){
		errorStr = "请选择用户群";
		flag = "false";
	}
	getErrorPrompt("add_staff_p_type",errorStr);
	errorStr = "";
	*/
	//密码提示
	if(password==""){
		errorStr = "必填字段";
		flag = "false";
	}else if(password.length<6){
		errorStr = "密码不能小于6位数";
		flag = "false";
	}else if(!(/[0-9]+/).test(password) || !(/[A-Za-z]+/).test(password)){
		errorStr = "密码要包含英文和数字";
		flag = "false";
	}else if(password.length > 40){
		errorStr = "超过40字符";
		flag = "false";
	}
	getErrorPrompt("add_account_c_password",errorStr);
	errorStr = "";
	//确认密码提示
	if(password!=comfigPwd){
		errorStr = "两次密码不一致";
		flag = "false";
	}
	getErrorPrompt("add_account_c_comfigPwd",errorStr);
	errorStr = "";
	//名字提示
	if($.trim(name)==""){
		errorStr = "必填字段";
		flag = "false";
	}else if(name.length > 20){
		errorStr = "超过20字符";
		flag = "false";
	}
	getErrorPrompt("add_account_c_name",errorStr);
	errorStr = "";
	//手机号码提示
	if(cellPhoneNumber==""){
		errorStr = "必填字段";
		flag = "false";
	}else if(!(/^(0|[1-9]\d*)$/).test(cellPhoneNumber)){
		errorStr = "输入格式错误";
		flag = "false";
	}else if(cellPhoneNumber.length > 11){
		errorStr = "超过11字符";
		flag = "false";
	}
	getErrorPrompt("add_account_c_cellPhoneNumber",errorStr);
	errorStr = "";
	//邮箱提示
	if(email==""){
		errorStr = "必填字段";
		flag = "false";
	}else if(!(/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/).test(email)){
		errorStr = "输入格式错误";
		flag = "false";
	}else if(email.length > 50){
		errorStr = "超过50字符";
		flag = "false";
	}
	getErrorPrompt("add_account_c_email",errorStr);
	errorStr = "";
	//手机邮箱提示
	/*if(mobileEmailAddress==""){
		errorStr = "必填字段";
		flag = "false";
	}else*/
	 if(!(/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/).test(mobileEmailAddress)){
		errorStr = "输入格式错误";
		flag = "false";
	}else if($.trim(email)==$.trim(mobileEmailAddress)){
		errorStr = "手机邮箱与其他邮箱重复";
		flag = "false";
	}else if(mobileEmailAddress.length > 50){
		errorStr = "超过50字符";
		flag = "false";
	}
	getErrorPrompt("add_account_c_mobileEmailAddress",errorStr);
	errorStr = "";
	//备用邮箱提示
	if($.trim(backUpEmailAddress)!=""){
		if(!(/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/).test(backUpEmailAddress)){
			errorStr = "输入格式错误";
			flag = "false";
		}else if($.trim(backUpEmailAddress)==$.trim(mobileEmailAddress) || $.trim(backUpEmailAddress)==$.trim(email)){
			errorStr = "备用邮箱与其他邮箱重复";
			flag = "false";
		}else if(backUpEmailAddress.length > 50){
			errorStr = "超过50字符";
			flag = "false";
		}
	}
	getErrorPrompt("add_account_c_backUpEmailAddress",errorStr);
	errorStr = "";
	
	/*
	//是否属于该企业提示
	if(!isEnterprise){
		errorStr = "请选择";
		flag = "false";
	}
	getErrorPrompt("add_account_c_isEnterprise",errorStr);
	errorStr = "";
	*/
	if(flag=="true"){
		account = account + enterpriseUrl;
		var jsonStr = "{";
		jsonStr += "'orgId':'"+orgId+"',";
		//jsonStr += "'type':'"+type+"',";
		jsonStr += "'roleIdList':'"+roleIdList+"',";
		jsonStr += "'account':'"+account+"',";
		jsonStr += "'password':'"+password+"',";
		jsonStr += "'name':'"+name+"',";
		jsonStr += "'gender':'"+sex+"',";
		jsonStr += "'mobile':'"+cellPhoneNumber+"',";
		//jsonStr += "'isEnterprise':'"+isEnterprise+"',";
		jsonStr += "'enterpriseId':'"+enterpriseId+"',";
		jsonStr += "'mobileemail':'"+mobileEmailAddress+"',";
		jsonStr += "'backupemail':'"+backUpEmailAddress+"',";
		jsonStr += "'status':'"+state+"',";
		jsonStr += "'email':'"+email+"'";
		jsonStr += "}";
		jsonStr = encodeURIComponent(jsonStr);
		$.ajax({
		url: "../system/createStaffAndUserRoleAction",
		async:false,
		type:"POST",
		data: {"orgJsonStr":jsonStr} ,
		success : function($result) {
				if($result != 0){
					$("#serviceAddStaff_c_Dialog").hide();
					var de  = $result;
					showUpdateProviderAccountInfo(de);
					$("#serviceStaff_Dialog").show();
					
				}else{
					alert("人员信息添加失败");
				}
			}
		});
	}
	
}

//关闭修改账号
function closeUpdateCustomerAccount(){
	$("#serviceUpdateStaff_c_Dialog").hide();
	$(".black").hide();
}

function changeUpdateCustomerAccountInfo(){
	$("#update_update_account_button").hide();
	$("#update_save_account_button").show();
	$(".staffInfo_value").hide();
	$(".staffInfo_input").show();
}

//显示账号修改tag
function showUpdateCustomerAccountInfo(account){
	$("#serviceUpdateStaff_c_Dialog").show();
	$(".black").show();
	$("#update_update_account_button").show();
	$("#update_save_account_button").hide();
	//$(".authorityControlClass").show();
	$("#staff_tab_0").hide();
	$("#staff_tab_1").hide();
	$("#staff_tab_2").hide();
	$(".staffInfo_value").show();
	$(".staffInfo_input").hide();
	//$(".staffInfo_modify").show();
	//$(".staffInfo_save").hide();
	//var orgIds = $("#account_downOrgId").val();
	//authorityControlButton(orgIds);
	
	$(".update_account_p_error").html("");
	$("#orgRole_tableId").val("show_update_org_c_org_role");
	$("#bizRole_tableId").val("show_update_org_c_biz_role");
	$.post("getProviderAccountAjaxAction",{"account":account},function(data){
		$("#staff_tab").children().eq(0).children().eq(1).hide();
		if(data.isCard=="false"){
			$("#staff_tab").children().eq(0).children().eq(1).show();
			$("#show_update_account_c_isEnterprise").html("是");
		}else{
			$("#show_update_account_c_isEnterprise").html("否");
		}
		var changePassword = "";
		if(data.password){
			var pwdLength = data.password.length;
			for(var i = 0; i < pwdLength; i++){
				changePassword += "*";
			}
		}
		$("#show_update_account_c_account").html(changeUnkownValue(data.account)+"&nbsp;");
		$("#show_update_account_c_password").html(changePassword+"&nbsp;");
		$("#show_update_account_c_comfigPwd").html(changePassword+"&nbsp;");
		$("#show_update_account_c_name").html(changeUnkownValue(data.name)+"&nbsp;");
		$("#show_update_account_c_sex").html(changeUnkownValue(data.gender)+"&nbsp;");
		$("#show_update_account_c_cellPhoneNumber").html(changeUnkownValue(data.cellPhoneNumber)+"&nbsp;");
		$("#show_update_account_c_email").html(changeUnkownValue(data.email)+"&nbsp;");
		$("#show_update_account_c_backUpEmailAddress").html(changeUnkownValue(data.backUpEmailAddress)+"&nbsp;");
		$("#show_update_account_c_mobileEmailAddress").html(changeUnkownValue(data.mobileEmailAddress)+"&nbsp;");
		$("#show_update_account_c_time_range_begin").html(changeUnkownValue(data.time_range_begin)+"&nbsp;");
		$("#show_update_account_c_time_range_end").html(changeUnkownValue(data.time_range_end)+"&nbsp;");
		var status = data.status;
		if(status==0){
			$(".show_update_account_c_status").eq(0).removeAttr("checked");
		}
		var account = changeUnkownValue(data.account);
		var enterpriseUrl = "";
		if(account){
			var s = account.indexOf("@");
			enterpriseUrl = account.substring(s,account.length);
			account = account.substring(0,s);
		}
		$("#update_account_c_account").val(changeUnkownValue(account));
		$("#update_account_c_enterpriseUrl").html(changeUnkownValue(enterpriseUrl));
		$("#update_account_c_password").val(changeUnkownValue(data.password));
		$("#update_account_c_comfigPwd").val(changeUnkownValue(data.password));
		$("#update_account_c_name").val(changeUnkownValue(data.name));
		$("input[name='update_account_c_sex']").each(function(){
			if($(this).val() == changeUnkownValue(data.gender)){
				$(this).attr("checked",true);
			}
		});
		$("#update_account_c_cellPhoneNumber").val(changeUnkownValue(data.cellPhoneNumber));
		$("#update_account_c_email").val(changeUnkownValue(data.email));
		$("#update_account_c_backUpEmailAddress").val(changeUnkownValue(data.backUpEmailAddress));
		$("#update_account_c_mobileEmailAddress").val(changeUnkownValue(data.mobileEmailAddress));
		$("#update_account_c_time_range_begin").val(changeUnkownValue(data.time_range_begin));
		$("#update_account_c_time_range_end").val(changeUnkownValue(data.time_range_end));
		if(status==0){
			$(".update_account_c_status").eq(0).removeAttr("checked");
		}
		
		$("#show_update_staff_c_sex").html(changeUnkownValue(data.gender)+"&nbsp;");
		$("#show_update_staff_c_type").html(changeUnkownValue(data.type)+"&nbsp;");
		$("#show_update_staff_c_birthday").html(changeUnkownValue(data.birthday)+"&nbsp;");
		$("#show_update_staff_c_graduateDate").html(changeUnkownValue(data.graduateDate)+"&nbsp;");
		$("#show_update_staff_c_degree").html(changeUnkownValue(data.degree)+"&nbsp;");
		$("#show_update_staff_c_identityCard").html(changeUnkownValue(data.identityCard)+"&nbsp;");
		
		$("#update_staff_c_sex").children().each(function(){
			if($(this).val()==data.gender){
				$(this).attr("selected","selected");
			}
		});
		
		$("#update_staff_c_type").children().each(function(){
			if($(this).val()==data.typeId){
				$(this).attr("selected","selected");
			}
		});
		
		$("#update_staff_c_birthday").val(data.birthdayStr);
		$("#update_staff_c_graduateDate").val(data.graduateDateStr);
		$("#update_staff_c_degree").val(data.degree);
		$("#update_staff_c_identityCard").val(data.identityCard);
		
		$("#staff_tab").children().children().each(function(index){
			if(index==0){
				$(this).attr("class","selected");
			}else{
				$(this).attr("class","");
			}
		});
		
		/*
		loadProviderOrgRoleInfo("update_staff_p_type");
		showProviderBizRole(1);
		var roleIdList = data.roleIdList;
		if(roleIdList){
			$.each(roleIdList,function(index2,value2){
				$.each($(".org_role_class"),function(){
					if($(this).val()==value2){
						$(this).attr("checked","checked");
						return false;
					}
				});
			});
		}
		var bizRoleIdList = data.bizRoleIdList;
		if(bizRoleIdList){
			$.each(bizRoleIdList,function(index2,value2){
				$.each($(".biz_role_class"),function(){
					if($(this).val()==value2){
						$(this).attr("checked","checked");
						return false;
					}
				});
			});
		}
		getProviderOrgRole();
		getProviderBizRole();
		*/
		/*
		var orgId = $("#orgId").val();
		$.post("getProviderOrgRoleAction",{"orgId":orgId},function(data1){
			var str = "";
			$.each(data1,function(index1,value1){
				str +="<li><input class='org_role_class' type='checkbox' value='"+value1.roleId+"' /><em>"+value1.name+"</em></li>";
			});
			
			$("#org_p_org_role").html(str);
			
			getProviderOrgRole();
		},"json");
		
		$.post("getProviderBizRoleAction",{"orgId":orgId},function(data1){
			var urgentrepair = "";
			if(data1){
				$.each(data1.urgentrepair,function(index1,value1){
					urgentrepair +="<li><input class='biz_role_class' type='checkbox' value='"+value1.roleId+"' /><em>"+value1.name+"</em></li>";
				});
			}
			$("#business_role_0").html(urgentrepair);
			
			getProviderBizRole();
		},"json");
		*/
		
	},"json");
}

//修改账号信息
function updateCustomerAccountInfo(){
	var enterpriseId = $("#enterpriseId").val();
	var orgId = $("#orgId").val();
	var account = $.trim($("#update_account_c_account").val());
	var enterpriseUrl = $.trim($("#update_account_c_enterpriseUrl").html());
	//var type= $("#update_staff_c_type").val();
	var type= "OperationCustomer";
	var password = $.trim($("#update_account_c_password").val());
	var comfigPwd = $("#update_account_c_comfigPwd").val();
	var name = $.trim($("#update_account_c_name").val());
	var sex = changeUnkownValue($("input[name='update_account_c_sex']:checked").val());
	var cellPhoneNumber = $.trim($("#update_account_c_cellPhoneNumber").val());
	var mobileEmailAddress = $.trim($("#update_account_c_mobileEmailAddress").val());
	var backUpEmailAddress = $.trim($("#update_account_c_backUpEmailAddress").val());
	var time_range_begin = $("#update_account_c_time_range_begin").val();
	var time_range_end = $("#update_account_c_time_range_end").val();
	//var isEnterprise = $('input:radio[name="update_account_p_isEnterprise"]:checked').val();
	
	var state = "0";
	$(".show_update_account_c_status").removeAttr("checked");
	if($(".update_account_c_status").eq(0).attr("checked")=="checked"){
		state = "1";
		$(".show_update_account_c_status").attr("checked","checked");
	}
	
	var email = $.trim($("#update_account_c_email").val());
	var roleIdList = $("#roleIdList").val();
	//var bizRoleIdList = $("#bizRoleIdList").val();
	var flag = "true";
	var errorStr = "";
	var showAccount = $("#show_update_account_c_account").html();
	var account1 = account+enterpriseUrl+"&nbsp;";
	if(showAccount!=account1){
		$.ajax({
			url: "../system/checkAccountIsExistAjaxAction",
			async:false,
			type:"POST",
			dataType : 'json',
			data: {"account":account+enterpriseUrl} ,
			success : function(result) {
				if(result == "found"){
					errorStr = "该账号已经被使用";
					flag = "false";
				}
			}
		});
	}
	//账号提示
	if($.trim(account)==""){
		errorStr = "必填字段";
		flag = "false";
	}
	//账号提示
	if($.trim(account)!=""){
		if(!(/^[-\.\w#%&:?=\/]+$/i).test(account)){
			errorStr = "输入格式错误";
			flag = "false";
		}
	}
	if($.trim(account)!=""){
		if($.trim(account).length > 20){
			errorStr = "超过20字符";
			flag = "false";
		}
	}
	getErrorPrompt("update_account_c_account",errorStr);
	errorStr = "";
	//用户群提示
	/*
	if($.trim(type)=="0"){
		errorStr = "请选择用户群";
		flag = "false";
	}
	getErrorPrompt("update_staff_c_type",errorStr);
	errorStr = "";
	*/
	//密码提示
	if(password==""){
		errorStr = "必填字段";
		flag = "false";
	}else if(password.length<6){
		errorStr = "密码不能小于6位数";
		flag = "false";
	}else if(!(/[0-9]+/).test(password) || !(/[A-Za-z]+/).test(password)){
		errorStr = "密码要包含英文和数字";
		flag = "false";
	}else if(password.length > 40){
		errorStr = "超过40字符";
		flag = "false";
	}
	getErrorPrompt("update_account_c_password",errorStr);
	errorStr = "";
	//确认密码提示
	if(password!=comfigPwd){
		errorStr = "密码和确认密码不一致";
		flag = "false";
	}
	getErrorPrompt("update_account_c_comfigPwd",errorStr);
	errorStr = "";
	//名字提示
	if($.trim(name)==""){
		errorStr = "必填字段";
		flag = "false";
	}else if(name.length > 20){
		errorStr = "超过20字符";
		flag = "false";
	}
	//手机号码提示
	if(cellPhoneNumber==""){
		errorStr = "必填字段";
		flag = "false";
	}else if(!(/^(0|[1-9]\d*)$/).test(cellPhoneNumber)){
		errorStr = "输入格式错误";
		flag = "false";
	}else if(cellPhoneNumber.length > 11){
		errorStr = "超过11字符";
		flag = "false";
	}
	getErrorPrompt("update_account_c_cellPhoneNumber",errorStr);
	errorStr = "";
	//邮箱提示
	if(email==""){
		errorStr = "必填字段";
		flag = "false";
	}else if(!(/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/).test(email)){
		errorStr = "输入格式错误";
		flag = "false";
	}else if(email.length > 50){
		errorStr = "超过50字符";
		flag = "false";
	}
	getErrorPrompt("update_account_c_email",errorStr);
	errorStr = "";
	//手机邮箱提示
	/*if(mobileEmailAddress==""){
		errorStr = "必填字段";
		flag = "false";
	}else*/
	 if(!(/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/).test(mobileEmailAddress)){
		errorStr = "输入格式错误";
		flag = "false";
	}else if($.trim(email)==$.trim(mobileEmailAddress)){
		errorStr = "手机邮箱与其他邮箱重复";
		flag = "false";
	}else if(mobileEmailAddress.length > 50){
		errorStr = "超过50字符";
		flag = "false";
	}
	getErrorPrompt("update_account_c_mobileEmailAddress",errorStr);
	errorStr = "";
	//备用邮箱提示
	if($.trim(backUpEmailAddress)!=""){
		if(!(/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/).test(backUpEmailAddress)){
			errorStr = "输入格式错误";
			flag = "false";
		}else if($.trim(backUpEmailAddress)==$.trim(mobileEmailAddress) || $.trim(backUpEmailAddress)==$.trim(email)){
			errorStr = "备用邮箱与其他邮箱重复";
			flag = "false";
		}else if(backUpEmailAddress.length > 50){
			errorStr = "超过50字符";
			flag = "false";
		}
	}
	getErrorPrompt("update_account_c_backUpEmailAddress",errorStr);
	errorStr = "";
	
	/*
	//是否属于该企业提示
	if(!isEnterprise){
		errorStr = "请选择";
		flag = "false";
	}
	getErrorPrompt("update_account_p_isEnterprise",errorStr);
	errorStr = "";
	*/
	
	if(flag == "true"){
		account = account + enterpriseUrl;
		//showAccount = showAccount.substring(0,showAccount.length-6);
		var jsonStr = "{";
		jsonStr += "'orgId':'"+orgId+"',";
		jsonStr += "'orgUserId':'"+$("#update_orgUserId").val()+"',";
		//jsonStr += "'type':'"+type+"',";
		jsonStr += "'roleIdList':'"+roleIdList+"',";
		jsonStr += "'account':'"+account+"',";
		jsonStr += "'password':'"+password+"',";
		jsonStr += "'name':'"+name+"',";
		jsonStr += "'gender':'"+sex+"',";
		jsonStr += "'mobile':'"+cellPhoneNumber+"',";
		jsonStr += "'mobileemail':'"+mobileEmailAddress+"',";
		jsonStr += "'backupemail':'"+backUpEmailAddress+"',";
		jsonStr += "'enterpriseId':'"+enterpriseId+"',";
		//jsonStr += "'time_range_begin':'"+time_range_begin+"',";
		//jsonStr += "'time_range_end':'"+time_range_end+"',";
		jsonStr += "'status':'"+state+"',";
		//jsonStr += "'showAccount':'"+showAccount+"',";
		//jsonStr += "'isEnterprise':'"+isEnterprise+"',";
		jsonStr += "'email':'"+email+"'";
		jsonStr += "}";
		jsonStr = encodeURIComponent(jsonStr);
		$.ajax({
			url: "../system/updateStaffAndUserRoleAction",
			async:false,
			type:"POST",
			data: {"orgJsonStr":jsonStr},
			success : function(result) {
				if(result=="failed"){
				alert("修改失败！");
				return;
				}
				showUpdateProviderAccountInfo($("#update_orgUserId").val());
			}
		});
	}
}

//删除运营商人员与组织关系
function deleteCustomerStaffInfo(){
	var orgUserId = "";
	var single = 0;
	$(".org_p_staff_list").each(function(){
		if($(this).attr("checked")){
			single = 1;
			orgUserId = $(this).val();
			return false;
		}
	});
	if(single==0){
		alert("请选择要删除的人员");
		return;
	}else{
		if(confirm("确定要删除吗？")){	
			var orgId = $("#orgId").val();
			$.post("deleteProiderAccountAndOrgAjaxAction",{"orgUserId":orgUserId,"orgId":orgId},function(data){
				showCustomerAccountInfo(orgId);
			},"json");
		}
	}
}

//模糊查询
function orgShowStaffFuzzy(divId){
	var orgId = $("#orgId").val();
	var fuzzy = $("#"+divId+"_fuzzy").val();
	fuzzy = encodeURIComponent(fuzzy);
	var pageDivId = divId+"_page";
	var showDivId = divId+"_table";
	var actionName = "../system/getProviderSystemStaffByOrgIdAction";
	var pageSize = "10";
	var param={
		orgId : orgId,
		conditions : fuzzy,
		currentPage : "1",
		pageSize : pageSize
	};
	pagingColumnByBackgroundJsp(pageDivId,showDivId,actionName,param);
	
	
	/*
	$.post("getProviderStaffByOrgIdAndFuzzyAction",{"orgId":orgId,"fuzzy":fuzzy},function(data){
		if(data){
			var array = new Array();
			$.each(data,function(index,value){
				$.each($(".org_p_select_staff_list"),function(){
					if($(this).val()==value){
						$(this).parent().parent().show();
						array.push($(this).parent().parent());
					}
				});
			});
			var showList = array;
			var pageDivId = "org_p_staff_page";
			var pageSize = "5";
			pagingColumnByForeground(pageDivId,showList,pageSize);
		}
	},"json");
	*/
}

//切换类型
function markOrgType(orgType){
	$("#add_top_type").val(orgType);
}

//维护网络资源(王景安)====================================================================
//显示当前组织的所有项目
function showProjectByOrgId(){
	var orgId = $("#orgId").val();
	var str = "";
	$("#p_projectId").val("");
	$("#p_network_top").html("");
	$("#p_network_main").html("");
	$.post("networkresourcemanage_ajax!getOrgProjectByOrgIdAjaxAction",{"orgId":orgId},function(data){
		if(data){
			$.each(data,function(key,value){
				if (value) {
					str +="<input type='radio' class='p_network_project top3' name='p_network_project' value='"+value.id+"' onclick='showOrgNetWorkByPorjectId("+value.id+","+value.cityId+");' />";
					str +="<input type='hidden' value='"+value.cityId+"'>";
					str +="<em>"+value.NAME+"</em>";
				}
			});
			$("#p_network_top").html(str);
			var p = $(".p_network_project");
			if(p.length>=1){
				p.eq(0).attr("checked","checked");
				var projectId = p.eq(0).val();
				showOrgNetWorkByPorjectId(projectId,p.eq(0).next().val());
			}
		}
	},"json");
}

//显示组织-项目-区域
function showOrgNetWorkByPorjectId(projectId,cityId){
	$.post("org_networkresource_info.jsp",{"project":projectId},function(data){
		$("#p_network_main").html(data);
		$("#p_projectId").val(projectId);
		areaTree (projectId,cityId);
	});
}

//跳转到项目管理页面
function editOrgNetWork(){
	var projectId = $("#p_projectId").val();
	window.open("/ops/op/informationmanage/show_networkresource_info.jsp?projectId="+projectId);
}
//维护网络资源(王景安)====================================================================

//内部方法==================================================================
function getTopOrgJsonStr(){
	var orgId = $("#orgId").val();
	var name = $("#add_top_org_name").val();
	var enterpriseId = $("#add_top_org_enterpriseId").val();
	var type = $("#add_top_org_orgType").val();
	var orgAttribute = $("#add_top_org_orgAttribute").val();
	var orgDuty = $("#add_top_org_orgDuty").val();
	var address = $("#add_top_org_address").val();
	var longitude = $("#add_top_org_longitude").val();
	var latitude = $("#add_top_org_latitude").val();
	var contactPhone = $("#add_top_org_contactPhone").val();
	var dutyPerson = $("#add_top_org_dutyPerson").val();
	var dutyPersonPhone = $("#add_top_org_dutyPersonPhone").val();
	var orgAttr = $("#architecture_structure .selected").html();
	var areaId = $("#add_top_org_areaId").val();
	var areaName = $("#add_top_org_areaname").val();//yuan.yw
	var parentOrgId = 0;
	
	var jsonStr = "{";
	jsonStr += "'orgAttr':'"+orgAttr+"',";
	jsonStr += "'orgId':'"+orgId+"',";
	jsonStr += "'type':'"+type+"',";
	jsonStr += "'name':'"+name+"',";
	jsonStr += "'enterpriseId':'"+enterpriseId+"',";
	jsonStr += "'orgAttribute':'"+orgAttribute+"',";
	jsonStr += "'orgDuty':'"+orgDuty+"',";
	jsonStr += "'address':'"+address+"',";
	jsonStr += "'longitude':'"+longitude+"',";
	jsonStr += "'latitude':'"+latitude+"',";
	jsonStr += "'contactPhone':'"+contactPhone+"',";
	jsonStr += "'dutyPerson':'"+dutyPerson+"',";
	jsonStr += "'parentOrgId':'"+parentOrgId+"',";
	jsonStr += "'dutyPersonPhone':'"+dutyPersonPhone+"',";
	jsonStr += "'areaId':'"+areaId+"',";
	jsonStr += "'areaName':'"+areaName+"'";
	jsonStr += "}";
	return jsonStr;
}

function getPOrgJsonStr(){
	var orgId = $("#orgId").val();
	var name = $("#org_p_name").val();
	var enterpriseId = $("#show_org_p_enterpriseId").val();
	var type = $("#org_p_type").val();
	var orgAttribute = $("#org_p_orgAttribute").val();
	var orgDuty = $("#org_p_orgDuty").val();
	var address = $("#org_p_address").val();
	var longitude = $("#org_p_longitude").val();
	var latitude = $("#org_p_latitude").val();
	var contactPhone = $("#org_p_contactPhone").val();
	var dutyPerson = $("#org_p_dutyPerson").val();
	var dutyPersonPhone = $("#org_p_dutyPersonPhone").val();
	var parentOrgId = $("#update_org_p_parentOrgId").val();
	var areaId = $("#org_p_areaId").val();//yuan.yw
	var areaName = $("#org_p_areaname").val();//yuan.yw
	var jsonStr = "{";
	jsonStr += "'orgAttr':'serviceProvider',";
	jsonStr += "'orgId':'"+orgId+"',";
	jsonStr += "'type':'"+type+"',";
	jsonStr += "'name':'"+name+"',";
	jsonStr += "'enterpriseId':'"+enterpriseId+"',";
	jsonStr += "'orgAttribute':'"+orgAttribute+"',";
	jsonStr += "'orgDuty':'"+orgDuty+"',";
	jsonStr += "'address':'"+address+"',";
	jsonStr += "'longitude':'"+longitude+"',";
	jsonStr += "'latitude':'"+latitude+"',";
	jsonStr += "'contactPhone':'"+contactPhone+"',";
	jsonStr += "'dutyPerson':'"+dutyPerson+"',";
	jsonStr += "'parentOrgId':'"+parentOrgId+"',";
	jsonStr += "'dutyPersonPhone':'"+dutyPersonPhone+"',";
	jsonStr += "'areaId':'"+areaId+"',";
	jsonStr += "'areaName':'"+areaName+"'";
	jsonStr += "}";
	return jsonStr;
}

function getCOrgJsonStr(){
	var orgId = $("#c_orgId").val();
	var name = $("#org_c_name").val();
	var enterpriseId = $("#enterpriseId").val();
	var type = $("#org_c_type").val();
	var orgAttribute = $("#org_c_orgAttribute").val();
	var orgDuty = $("#org_c_orgDuty").val();
	var address = $("#org_c_address").val();
	var longitude = $("#org_c_longitude").val();
	var latitude = $("#org_c_latitude").val();
	var contactPhone = $("#org_c_contactPhone").val();
	var dutyPerson = $("#org_c_dutyPerson").val();
	var dutyPersonPhone = $("#org_c_dutyPersonPhone").val();
	var parentOrgId = $("#update_org_c_parentOrgId").val();
	var areaId = $("#org_c_areaId").val();//yuan.yw
	var areaName = $("#org_c_areaname").val();//yuan.yw
	var jsonStr = "{";
	jsonStr += "'orgAttr':'customer',";
	jsonStr += "'orgId':'"+orgId+"',";
	jsonStr += "'type':'"+type+"',";
	jsonStr += "'name':'"+name+"',";
	jsonStr += "'enterpriseId':'"+enterpriseId+"',";
	jsonStr += "'orgAttribute':'"+orgAttribute+"',";
	jsonStr += "'orgDuty':'"+orgDuty+"',";
	jsonStr += "'address':'"+address+"',";
	jsonStr += "'longitude':'"+longitude+"',";
	jsonStr += "'latitude':'"+latitude+"',";
	jsonStr += "'contactPhone':'"+contactPhone+"',";
	jsonStr += "'dutyPerson':'"+dutyPerson+"',";
	jsonStr += "'parentOrgId':'"+parentOrgId+"',";
	jsonStr += "'dutyPersonPhone':'"+dutyPersonPhone+"',";
	jsonStr += "'areaId':'"+areaId+"',";
	jsonStr += "'areaName':'"+areaName+"'";
	jsonStr += "}";
	return jsonStr;
}

//清空input和select
function changeValueEmpty(divId){
	$("#"+divId+" input[type='text']").val("");
	$("#"+divId+" input[type='hidden']").val("");
	$("#"+divId+" select").val("");
}

//错误提示
function getErrorPrompt(id,str){
	var size = $("#"+id).parent().children().length;
	$("#"+id).parent().children().eq(size-1).html(str);
}

//生成区域树 //yuan.yw
function areaServerTree(divId,treeId,tableId,event){
	if($("#"+divId).children().size()==0){
		var myUrl = "getAreaTreeAjaxAction";
		/*$.post(myUrl,'',function(data){
			createAllAreaTreeWithCheckBox(data,divId,treeId,tableId,event,'areaCheckBox');
		},"json");*/
		$.ajax({
			url : myUrl,
			data : '',
			dataType : 'json',
			async:false,
			type : 'POST',
			success : function(data) {
				createAllAreaTreeWithCheckBox(data,divId,treeId,tableId,event,'areaCheckBox');
				$("#"+treeId+">li>div").click();
			}
		});
	}else{
		$("#"+divId+" input[type='checkbox']").removeAttr("checked");
		$("#"+divId).parent().hide();
	}

	
}

//点击选择区域
function areaServerTreeClick(dataStr,tableId,me){
	if($(me).prev().attr("checked")=="checked"){
		$(me).prev().removeAttr("checked");
	}else{
		$(me).prev().attr("checked","checked");
	}
	//var data = eval("(" + dataStr + ")");
	//var areaName = data.name;
	//var areaId = data.areaId;
	//$("#"+tableId+"_areaId").val(areaId);
	//$("#"+tableId+"_areaname").val(areaName);
	//$("div[name='"+tableId+"']").hide();	
}

//确认选择区域
function confirmChooseArea(treeId,tableId,me){
	var areaIds="";
	var areaNames="";
	$("#"+treeId+" input[type='checkbox']:checked").each(function(index){
		if(index==0){
			areaIds=$(this).val();
			areaNames=$(this).next().text();
		}else{
			areaIds +=","+$(this).val();
			areaNames +=","+$(this).next().text();
		}
	})
	$("#"+tableId+"_areaId").val(areaIds);
	$("#"+tableId+"_areaname").val(areaNames);
	$(me).parent().parent().slideToggle("fast");	
}
//取消选择区域
function cancelChooseArea(me){
	$(me).parent().parent().slideToggle("fast");
	
}



