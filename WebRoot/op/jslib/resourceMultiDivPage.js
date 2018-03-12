var vdivId = "";
//打开人员列表
function openStaffMultiDivByAccount(divId){
	vdivId = divId;
	var enterpriseId = $("#enterpriseId").val();
	$(".org_p_choiceAccount").eq(0).attr("checked",true);
	var choiceAccountType = $('input:radio[name="org_p_choiceAccount"]:checked').val();
	if(!choiceAccountType){
		choiceAccountType = "one";
	}
	var orgId = $("#orgId").val();
	$.ajax({
			url : "../system/getProviderSystemStaffAction",
			data : {"enterpriseId":enterpriseId,"choiceAccountType":choiceAccountType},
			async:false,
			type : 'POST',
			success : function(data) {
				$("#"+divId).html(data);
				$("#"+divId).show();
				$("#black").show();
				var showList = $(".org_p_select_staff_tr");
				var pageDivId = "org_p_select_staff_page";
				var pageSize = "10";
				pagingColumnByForeground(pageDivId,showList,pageSize);
			}
	});
}

//根据人员选择进行搜索
function changeSearchStaffMulti(divId){
	var enterpriseId = $("#enterpriseId").val();
	var choiceAccountType = $('input:radio[name="org_p_choiceAccount"]:checked').val();
	var orgId = $("#orgId").val();
	$.ajax({
			url : "../system/getProviderSystemStaffAction",
			data : {"enterpriseId":enterpriseId,"choiceAccountType":choiceAccountType},
			async:false,
			type : 'POST',
			success : function(data) {
				$("#"+divId).html(data);
				$("#"+divId).show();
				$("#black").show();
				var showList = $(".org_p_select_staff_tr");
				var pageDivId = "org_p_select_staff_page";
				var pageSize = "10";
				pagingColumnByForeground(pageDivId,showList,pageSize);
				if(choiceAccountType=="all"){
					$(".org_p_choiceAccount").eq(1).attr("checked",true);
				}
			}
	});
}

//模糊查询
function staffMultiFuzzy(){
	$(".org_p_select_staff_tr").hide();
	var fuzzy = $("#rex").val();
	var enterpriseId = $("#enterpriseId").val();
	var choiceAccountType = $('input:radio[name="org_p_choiceAccount"]:checked').val();
	var orgId = $("#orgId").val();
	fuzzy = encodeURIComponent(fuzzy);
	$.ajax({
			url : "../system/getProviderSystemStaffAction",
			data : {"enterpriseId":enterpriseId,"conditions":fuzzy,"choiceAccountType":choiceAccountType},
			async:false,
			type : 'POST',
			success : function(data) {
				$("#"+vdivId).html(data);
				$("#"+vdivId).show();
				$("#black").show();
				var showList = $(".org_p_select_staff_tr");
				var pageDivId = "org_p_select_staff_page";
				var pageSize = "10";
				pagingColumnByForeground(pageDivId,showList,pageSize);
			}
	});
}

//拼音查询
function staffMultiPinyinQuery(pinyin){
	$(".org_p_select_staff_tr").hide();
	var enterpriseId = $("#enterpriseId").val();
	var choiceAccountType = $('input:radio[name="org_p_choiceAccount"]:checked').val();
	var orgId = $("#orgId").val();
	$.ajax({
			url : "../system/getProviderSystemStaffAction",
			data : {"enterpriseId":enterpriseId,"pinyin":pinyin,"choiceAccountType":choiceAccountType},
			async:false,
			type : 'POST',
			success : function(data) {
				$("#"+vdivId).html(data);
				$("#"+vdivId).show();
				$("#black").show();
				var showList = $(".org_p_select_staff_tr");
				var pageDivId = "org_p_select_staff_page";
				var pageSize = "10";
				pagingColumnByForeground(pageDivId,showList,pageSize);
			}
	});
}

