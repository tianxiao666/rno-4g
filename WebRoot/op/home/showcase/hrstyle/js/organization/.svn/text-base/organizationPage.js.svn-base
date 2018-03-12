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
			+ "',this.value) style=' width:70px;text-align: center;'>";
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
	} else if (actionName == "queryOrganizationAction") {
		showStation(page);
	} else {
		getStaffsByOrgId(actionName, pageSize);
	}
}

function prePage(actionName, page) {
	if (actionName == "noAction") {
		return;
	} else if (actionName == "queryOrganizationAction") {
		showStation(page);
	} else {
		getStaffsByOrgId(actionName, pageSize);
	}
}

function nextPage(actionName, page) {
	if (actionName == "noAction") {
		return;
	} else if (actionName == "queryOrganizationAction") {
		showStation(page);
	} else {
		getStaffsByOrgId(actionName, pageSize);
	}
}

function lastPage(actionName, page) {
	if (actionName == "noAction") {
		return;
	} else if (actionName == "queryOrganizationAction") {
		showStation(page);
	} else {
		getStaffsByOrgId(actionName, pageSize);
	}
}
function selectPage(actionName, page) {
	if (actionName == "noAction") {
		return;
	} else if (actionName == "queryOrganizationAction") {
		showStation(page);
	} else {
		getStaffsByOrgId(actionName, pageSize);
	}
}