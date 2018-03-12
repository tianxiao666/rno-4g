$(document).ready(function() {
	var orgSubstanceId = request("orgId");
	var orgStatus = request("orgStatus");
	if (orgStatus == "yes") {
		getStaffsByOrgId(orgSubstanceId, 1);
	}
	$("#queryButton").click(function() {
		showStaff(1);
	});
});

function getStaffsByOrgId(orgSubstanceId, page) {
	var values = {
		"orgSubstanceId" : orgSubstanceId,
		"currentPage" : page
	};
	var myurl = "getStaffByOrganizationIdAction";
	$.ajax( {
		url : myurl,
		type : "POST",
		data : values,
		dataType : "json",
		success : function(result) {
		
//			var page = result.page;
			var staffList = result;
			//删除除表头外的行
		var eventTrs = $("#tableQuery tr");
		var length = eventTrs.length;
		var foot = ""
		for ( var i = length - 1; i > 0; i--) {
			eventTrs.eq(i).remove();
		}
		var tr = "";
		$.each(staffList,
				function(key, object) {
					var staffEnrollDay = object.staff.staffEnrollDay.toString()
							.split(" ", 1);
					tr = tr + "<tr>";
					tr = tr + "<td style='text-align: left'>"
							+ object.organizationName + "</td>";
			        tr = tr + "<td style='text-align: left'>" +object.staff.name+ "</td>";
					tr = tr + "<td style='text-align: left'>"
							+ object.staff.staffCode + "</td>";
					tr = tr + "<td style='text-align: left'>" + staffEnrollDay
							+ "</td>";
					tr = tr + "<td style='text-align: left'>"
							+ object.staff.staffGender + "</td>";
					tr = tr + "<td style='text-align: left'>"
							+ object.staff.staffBirthPalce + "</td>";
					tr = tr + "<td style='text-align: left'>"
							+ object.staff.staffStatus + "</td>";
					tr = tr + "<td style='text-align: left' class='tdr'>"
							+ "<a href='updateStaff.html?id="
							+ object.staff.substanceId + "'>修改</a></td>";
					tr = tr + "<tr>";

				});
		//orgSubstanceId设置为actionName值，以便不用处理数据
//		tr = tr
//				+ foots(orgSubstanceId, page.currentPage, page.totalPage,
//						page.prePage, page.nextPage, page.pageSize);
		$(tr).appendTo("#tableQuery");
	}
	});
}

/**
 * 获取页面的数据通过ajax提交
 */
function showStaff(currentPage) {
	var values = {
		"currentPage" : currentPage,
		"substanceId" : $("#substanceId").val(),
		"substanceIdOfParent" : $("#substanceIdOfParent").val(),
		"staffName" : $("#staffName").val(),
		"staffIdentity" : $("#staffIdentity").val(),
		"staffCode" : $("#staffCode").val(),
		"staffGender" : $("#staffGender").val(),
		"staffPhone" : $("#staffPhone").val(),
		"staffEnrollDay" : $("#staffEnrollDay").val(),
		"staffStatus" : $("#staffStatus").val(),
		"staffPoliticalStatus" : $("#staffPoliticalStatus").val(),
		"organizationId" : $("#organizationId").val(),
		"staffBirthPalce" : $("#staffBirthPalce").val(),
		"staffAddress" : $("#staffAddress").val(),
		"staffEmail" : $("#staffEmail").val()
		
	};
	$.ajax( {
		url : "queryStaffAction",
		type : "POST",
		data : values,
		dataType : "json",
		success : function(result) {
		
		    var page = result.page;
			var staffList = result.staffList;
			var eventTrs = $("#tableQuery tr");
			var length = eventTrs.length;
		
		
			//删除除表头外的行
		var eventTrs = $("#tableQuery tr");
		var length = eventTrs.length;
		for ( var i = length - 1; i > 0; i--) {
			eventTrs.eq(i).remove();
		}
				var tr = "";
		$.each(staffList,
				function(key, object) {
					var staffEnrollDay = object.staff.staffEnrollDay.toString()
							.split(" ", 1);
					
					tr =tr+ "<tr>";
					tr = tr + "<td style='text-align: left'>"
							+ object.organizationName + "</td>";
			        tr = tr + "<td style='text-align: left'>" +object.staff.name+ "</td>";
					tr = tr + "<td style='text-align: left'>"
							+ object.staff.staffCode + "</td>";
					tr = tr + "<td style='text-align: left'>" + staffEnrollDay
							+ "</td>";
					tr = tr + "<td style='text-align: left'>"
							+ object.staff.staffGender + "</td>";
					tr = tr + "<td style='text-align: left'>"
							+ object.staff.staffBirthPalce + "</td>";
					tr = tr + "<td style='text-align: left'>"
							+ object.staff.staffStatus + "</td>";
					tr = tr + "<td style='text-align: left' class='tdr'>"
							+ "<a href='updateStaff.html?id="
							+ object.staff.substanceId + "'>修改</a>" + "</td>";
					tr = tr + "</tr>";

				
				});
			tr = tr
							+ foots("queryStaffAction", page.currentPage,
									page.totalPage, page.prePage,
									page.nextPage, page.pageSize);
			$(tr).appendTo("#tableQuery");
	}
	});
}

function foots(actionName, currentPage, totalPage, prePage, nextPage, pageSize) {
if(totalPage>0){
	var foot="";
	 foot = foot
			+ "	<tr name='page' id='pages'><td colspan='8' class='tdb tdr'>"
	foot = foot
			+ "<table id='page' class='f-r' width='100%'><tr><td style='text-align: right;'>"
	foot = foot + "<a href=JavaScript:firstPage('" + actionName
			+ "',1)>首页</a>&nbsp;&nbsp;";
	foot = foot + "<a id='prePage' href=JavaScript:prePage('" + actionName
			+ "'," + prePage + ")>上一页 </a>&nbsp;&nbsp;";
	foot = foot + "<select id='selectPage'   onchange=JavaScript:selectPage('"
			+ actionName
			+ "',this.value) style=' width:70px;text-align: center;'>";
	for ( var i = 1; i <= totalPage; i++) {
		if (currentPage == i) {
			foot = foot + "	<option value=" + i + " selected>";
			foot = foot + "	第" + i + "页";
			foot = foot + "	</option>";
		} else {
			foot = foot + "	<option value=" + i + ">";
			foot = foot + "	第" + i + "页";
			foot = foot + "	</option>";
		}
	}
	foot = foot + "</select>";
	foot = foot + "&nbsp;&nbsp;";
	foot = foot + "<a id='nextPage' href=JavaScript:nextPage('" + actionName
			+ "'," + nextPage + ")>下一页 </a>&nbsp;&nbsp;";
	foot = foot + "<a href=JavaScript:lastPage('" + actionName + "',"
			+ totalPage + ")>末页</a>&nbsp;&nbsp;";
	return foot;
	}else{
		actionName="noAction";
	var foot="";
	foot = foot
			+ "	<tr name='page' id='pages'><td colspan='8' class='tdb tdr'>"
	foot = foot
			+ "<table id='page' class='f-r' width='100%'><tr><td style='text-align: right;'>"
	foot = foot + "<a href=JavaScript:firstPage('" + actionName
			+ "',0)>首页</a>&nbsp;&nbsp;";
	foot = foot + "<a id='prePage' href=JavaScript:prePage('" + actionName
			+ "',0)>上一页 </a>&nbsp;&nbsp;";
	foot = foot + "<select id='selectPage'   onchange=JavaScript:selectPage('"
			+ actionName
			+ "',this.value) style='70px;text-align: center;'>";
			foot = foot + "	<option value=0 selected>";
			foot = foot + "	第0页";
			foot = foot + "	</option>";
	foot = foot + "</select>";
	foot = foot + "&nbsp;&nbsp;";
	foot = foot + "<a id='nextPage' href=JavaScript:nextPage('" + actionName
			+ "',0)>下一页 </a>&nbsp;&nbsp;";
	foot = foot + "<a href=JavaScript:lastPage('" + actionName + "',0)>末页</a>&nbsp;&nbsp;";
	return foot;
	}
	
}
function firstPage(actionName, page) {
	if (actionName == "noAction") {
		return;
	} else if (actionName == "queryStaffAction") {
		showStaff(page);
	} else {
		getStaffsByOrgId(actionName, pageSize);
	}
}

function prePage(actionName, page) {
	if (actionName == "noAction") {
		return;
	} else if (actionName == "queryStaffAction") {
		showStaff(page);
	} else {
		getStaffsByOrgId(actionName, pageSize);
	}
}

function nextPage(actionName, page) {
	if (actionName == "noAction") {
		return;
	} else if (actionName == "queryStaffAction") {
		showStaff(page);
	} else {
		getStaffsByOrgId(actionName, pageSize);
	}
}

function lastPage(actionName, page) {
	if (actionName == "noAction") {
		return;
	} else if (actionName == "queryStaffAction") {
		showStaff(page);
	} else {
		getStaffsByOrgId(actionName, pageSize);
	}
}
function selectPage(actionName, page) {
	if (actionName == "noAction") {
		return;
	} else if (actionName == "queryStaffAction") {
		showStaff(page);
	} else {
		getStaffsByOrgId(actionName, pageSize);
	}
}