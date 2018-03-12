//内部数据检查
var internalCheck = [ {
	'code' : 'powerCheck',
	'name' : '功率检查'
},{
	'code' : 'freqHopCheck',
	'name' : '跳频检查'
},{
	'code' : 'nccperm',
	'name' : 'NCCPERM检查'
},{
	'code' : 'meaFreqMultidefined',
	'name' : '测量频点多定义'
},{
	'code' : 'meaFreqMomit',
	'name' : '测量频点漏定义'
},{
	'code' : 'baNumCheck',
	'name' : 'BA表个数检查'
},{
	'code' : 'sameFreqBsicCheck',
	'name' : '同频同bsic检查'
},{
	'code' : 'talimMaxTa',
	'name' : 'TALIM_MAXTA检查'
}];
//邻区检查
var ncellCheck = [ {
	'code' : 'ncellMomit',
	'name' : '本站邻区漏定义'
},{
	'code' : 'unidirNcell',
	'name' : '单向邻区'
},{
	'code' : 'ncellNumCheck',
	'name' : '邻区过多过少检查'
},{
	'code' : 'sameNcellFreqCheck',
	'name' : '同邻频检查'
}
/*,{
	'code' : 'ncellDataCheck',
	'name' : '邻区数据检查'
}*/
];

var interval = 2000;// 周期性获取文件导出进度情况的

//缓存数据
var powerCheckResult = new Array();
var freqHopCheckResult = new Array();
var nccpermResult = new Array();
var meaFreqMultidefinedResult = new Array();
var meaFreqMomitResult = new Array();
var baNumCheckResult = new Array();
var sameFreqBsicCheckResult = new Array();
var talimMaxTaResult = new Array();

var ncellMomitResult = new Array();
var unidirNcellResult = new Array();
var ncellNumCheckResult = new Array();
var sameNcellFreqCheckResult = new Array();
var ncellDataCheckResult = new Array();

$(document).ready(function() {
	//加载参数树形菜单
	loadParamCheckMenu();
	//加载BSC
	getAllBsc();
	
	//参数对比点击事件
	$("#checkBtn").click(function(){
		//显示需要检查的tab
		showTabs();
	});
	
	//数据导出点击事件
	$("#exportBtn").click(function(){
		exportData();
	});

	//查询设置按钮点击事件
	$("#settingsBtn").click(function(){
		$("#settingsDiv").show();
	});
	
	//控制tab移动
	$(".btnRight").click(function(){
	  	var i = $("#frame").scrollLeft();
	  	$("#frame").scrollLeft(i+50);
	});
	$(".btnLeft").click(function(){
	  	var i = $("#frame").scrollLeft();
	  	$("#frame").scrollLeft(i-50);
	});
	
	// 区域联动事件
	$("#provinceId").change(function() {
		getSubAreas("provinceId", "cityId", "市");
	});
	$("#cityId").change(function() {
		// 清除bsc列表
		$("#defaultBsc").html("");
		$("#selectedBsc").html("");
		// 重新加载bsc列表
		getAllBsc();
	});
	
	var tdWidth = $("#contentTab tr:eq(0) td:nth-child(2)").width(); 
	//console.log(tdWidth);
	$("#frame").css({
		'width' : (tdWidth-1151)+"px"
	});
});

/**
 * 导出爱立信参数一致性结果数据
 */
function exportData() {
	
	$("#progressDesc").html("");
	//获取所选择的菜单项
	var items = "";
	$('input:checkbox[name="twoChk"]:checked').each(function(){  
		items += $(this).val() + ",";
	});
	items = items.substr(0, items.length - 1);
	if(items.length == 0) {
		alert("请选择要查询的项目！");
		return;
	}
	
	//获取bsc
	var bscStr = $("#bscStr").val();
	if(bscStr == "") {
		alert("请选择BSC！");
		return;
	}
	//获取日期
	var date1 = $("#date1").val();
	if(date1 == "") {
		alert("请选择日期！");
		return;
	}
	//获取cityId
	var cityId = $("#cityId").val();
	//获取设置
	var numTest = /^([0-9]+)$/; //检查数字
	
	var isCheckMaxChgr = false;
	if($("#isCheckMaxChgr").is(':checked')) { 
		isCheckMaxChgr = true;
	}
	
	var isCheckBaNum = false;
	if($("#isCheckBaNum").is(':checked')) { 
		isCheckBaNum = true;
	}
	var maxNum = $("#MAXNUM").val();
	var minNum = $("#MINNUM").val();
	if(isCheckBaNum) {
		if(maxNum == null || minNum == null || !numTest.test(maxNum) 
			|| !numTest.test(minNum) || Number(minNum)>Number(maxNum)) {
			alert("BA表个数检查的查询设置不符合要求,请重新设置");
			return;
		}
	}
	
	var isCheckCoBsic = false;
	if($("#isCheckCoBsic").is(':checked')) { 
		isCheckCoBsic = true;
	}
	var distance = $("#DISTANCE").val();
	if(isCheckCoBsic) {
		if(distance == null || !numTest.test(distance)) {
			alert("同频同bsic检查的查询设置不符合要求,请重新设置");
			return;
		}
	}
	
	var isCheckNcellNum = false;
	if($("#isCheckNcellNum").is(':checked')) { 
		isCheckNcellNum = true;
	}
	var ncell_maxNum = $("#NCELL_MAXNUM").val();
	var ncell_minNum = $("#NCELL_MINNUM").val();
	if(isCheckNcellNum) {
		if(ncell_minNum == null || ncell_minNum == null || !numTest.test(ncell_maxNum) 
			|| !numTest.test(ncell_maxNum)) {
			alert("邻区过多过少检查的查询设置不符合要求,请重新设置");
			return;
		}
	}
	
	showOperTips("loadingDataDiv", "loadContentId", "正在执行导出");
	$.ajax({
		url : 'exportEriCellCheckDataAjaxForAction',
		data : {
			'bscStr' : bscStr,
			'date1' : date1,
			'items' : items,
			'cityId' : cityId,
			'settings.isCheckMaxChgr' : isCheckMaxChgr,
			'settings.isCheckBaNum' : isCheckBaNum,
			'settings.MAXNUM' : maxNum,
			'settings.MINNUM' : minNum,
			'settings.isCheckCoBsic' : isCheckCoBsic,
			'settings.DISTANCE' : distance,
			'settings.isCheckNcellNum' : isCheckNcellNum,
			'settings.NCELL_MAXNUM' : ncell_maxNum,
			'settings.NCELL_MINNUM' : ncell_minNum
		},
		type : 'post',
		dataType : 'text',
		success : function(raw) {
			var data = eval("("+raw+")");
			var msg = data["msg"];
			var token = data["token"];
			//console.log(token);
			if(token != "-1") {
				//轮询进度
				queryExportProgress(token);
			} else {
				alert(msg);	
				hideOperTips("loadingDataDiv");
				$("#progressDesc").html("");
			}
		},
		complete:function(){
			
		}
	})
}

/**
 * 查询导出进度
 */
function queryExportProgress(token) {
	$.ajax({
		url : 'queryExportProgressAjaxForAction',
		data : {
			'token' : token
		},
		type : 'post',
		dataType : 'text',
		success : function(raw) {
			var obj = eval("(" + raw + ")");
			if(obj['fail']) {
				//导出失败
				alert(obj['msg']);
				hideOperTips("loadingDataDiv");
				$("#progressDesc").html("");
				return;
			}
			if (obj['finished']) {
				//完成
				$("#progressDesc").html("生成文件完成");
				downloadDataFile(token);
				hideOperTips("loadingDataDiv");
				$("#progressDesc").html("");
			} else {
				//未完成
				//console.log(obj['msg']);
				$("#progressDesc").html(obj['msg']);
				window.setTimeout(function() {
					queryExportProgress(token);
				}, interval);
			}
		},
		complete:function(){
		}
	});
}

/**
 * 下载爱立信小区参数对比结果文件
 */
function downloadDataFile(token) {
	var form = $("#downloadEriCellAnalysisDataFileForm");
	form.find("input#token").val(token);
	form.submit();
}

/*
 * 加载参数树形菜单
 */
function loadParamCheckMenu() {
	$("#paramCheckMenu").html("");
	var html = "";
	//内部数据检查
	html += "<li><input type='checkbox' name='oneChk'><span>内部数据检查</span><ul>";
	for(var i=0; i<internalCheck.length; i++) {
		html += "<li><input type='checkbox' name='twoChk' value='"+internalCheck[i]['code']
								+"'><span id='"+internalCheck[i]['code']+"'>"+internalCheck[i]['name']+"</span></li>";
	}
	html += "</ul></li>";
	//邻区检查
	html += "<li><input type='checkbox' name='oneChk'><span>邻区检查</span><ul>";
	for(var i=0; i<ncellCheck.length; i++) {
		html += "<li><input type='checkbox' name='twoChk' value='"+ncellCheck[i]['code']
								+"'><span id='"+ncellCheck[i]['code']+"'>"+ncellCheck[i]['name']+"</span></li>";
	}
	html += "</ul></li>";
	//加载到页面
	$("#paramCheckMenu").html(html);
	//加入树形菜单样式
	$("#paramCheckMenu").treeview();
	//加入关联选择事件
	$("input[name='oneChk']").click(function(){
		 var checkedValue = this.checked;
		 $(this).parent("li").find("input[name='twoChk']").attr("checked",checkedValue);
	});
}

/**
 * 显示需要检查的tab
 */
function showTabs() {
	$("#paramCheckTab").html("");
	$("#paramCheckContent").html("");
	//获取所选择的菜单项
	var items = [];
	var one;
	$('input:checkbox[name="twoChk"]:checked').each(function(){  
		one = {
			'code' : $(this).val(),
	    	'name' : $("span#"+$(this).val()).text()
	    };
		items.push(one);  
	});
	//console.log(items);
	if(items.length == 0) {
		alert("请选择要查询的项目！");
		$("#paramCheckTab").html("");
		$("#paramCheckContent").html("");
		return;
	}
	//组建tab的html脚本
	var tabTitle = "<li id='"+items[0]['code']+"' class='selected'>"+items[0]['name']+"</li>";
	var tabContent = "<div id='div_tab_0' style='display:block; overflow:auto;height:600px;'>"
						+"<table id='"+items[0]['code']+"Table' style='width: 100%'></table></div>";
	for(var i=1; i<items.length; i++) {
		tabTitle += "<li id='"+items[i]['code']+"' >"+items[i]['name']+"</li>";
		tabContent += "<div id='div_tab_"+i+"' style='display:none; overflow:auto;height:600px;'>"
						 +"<table id='"+items[i]['code']+"Table' style='width: 100%'></table></div>";
	}
	$("#paramCheckTab").html(tabTitle);
	$("#paramCheckContent").html(tabContent);
	//加入点击切换tab事件
	tab("div_tab", "li", "onclick");
	//为所有li项加入加载数据事件
	$("#paramCheckTab li").click(function() {
		loadResultForTab($(this).attr("id"));
	});

	//为第一个tab的table加载数据
	loadResultForTab(items[0]['code']);
}

/**
 * tab加载数据事件
 */
function loadResultForTab(checkType) {
	if($("#"+checkType+"Table:has(tr)").length > 0)  {
		//console.log(checkType+"Table已加载数据");
		return;
	}
	//获取bsc
	var bscStr = $("#bscStr").val();
	if(bscStr == "") {
		alert("请选择BSC！");
		$("#paramCheckTab").html("");
		$("#paramCheckContent").html("");
		return;
	}
	//获取日期
	var date1 = $("#date1").val();
	if(date1 == "") {
		alert("请选择日期！");
		$("#paramCheckTab").html("");
	 	$("#paramCheckContent").html("");
		return;
	}
	//获取cityId
	var cityId = $("#cityId").val();
	//获取设置
	var numTest = /^([0-9]+)$/; //检查数字
	
	var isCheckMaxChgr = false;
	if($("#isCheckMaxChgr").is(':checked')) { 
		isCheckMaxChgr = true;
	}
	
	var isCheckBaNum = false;
	if($("#isCheckBaNum").is(':checked')) { 
		isCheckBaNum = true;
	}
	var maxNum = $("#MAXNUM").val();
	var minNum = $("#MINNUM").val();
	if(isCheckBaNum) {
		if(maxNum == null || minNum == null || !numTest.test(maxNum) 
			|| !numTest.test(minNum) || Number(minNum)>Number(maxNum)) {
			alert("BA表个数检查的查询设置不符合要求,请重新设置");
			$("#paramCheckTab").html("");
			$("#paramCheckContent").html("");
			return;
		}
	}
	
	var isCheckCoBsic = false;
	if($("#isCheckCoBsic").is(':checked')) { 
		isCheckCoBsic = true;
	}
	var distance = $("#DISTANCE").val();
	if(isCheckCoBsic) {
		if(distance == null || !numTest.test(distance)) {
			alert("同频同bsic检查的查询设置不符合要求,请重新设置");
			$("#paramCheckTab").html("");
			$("#paramCheckContent").html("");
			return;
		}
	}
	
	var isCheckNcellNum = false;
	if($("#isCheckNcellNum").is(':checked')) { 
		isCheckNcellNum = true;
	}
	var ncell_maxNum = $("#NCELL_MAXNUM").val();
	var ncell_minNum = $("#NCELL_MINNUM").val();
	if(isCheckNcellNum) {
		if(ncell_minNum == null || ncell_minNum == null || !numTest.test(ncell_maxNum) 
			|| !numTest.test(ncell_maxNum)) {
			alert("邻区过多过少检查的查询设置不符合要求,请重新设置");
			$("#paramCheckTab").html("");
			$("#paramCheckContent").html("");
			return;
		}
	}
	
	showOperTips("loadingDataDiv", "loadContentId", "正在加载");
	$.ajax({
		url : 'getEriCellParamCheckByTypeForAjaxAction',
		data : {
			'bscStr' : bscStr,
			'date1' : date1,
			'checkType' : checkType,
			'cityId' : cityId,
			'settings.isCheckMaxChgr' : isCheckMaxChgr,
			'settings.isCheckBaNum' : isCheckBaNum,
			'settings.MAXNUM' : maxNum,
			'settings.MINNUM' : minNum,
			'settings.isCheckCoBsic' : isCheckCoBsic,
			'settings.DISTANCE' : distance,
			'settings.isCheckNcellNum' : isCheckNcellNum,
			'settings.NCELL_MAXNUM' : ncell_maxNum,
			'settings.NCELL_MINNUM' : ncell_minNum
		},
		type : 'post',
		dataType : 'text',
		success : function(raw) {
			var data = eval("("+raw+")");
			var flag = data['flag'];
			var result = data['result'];
			var thHtml = "";
			var trHtml = "";
			if(flag) {
				//标题
				thHtml = buildTh(checkType);
				$("#"+checkType+"Table").append(thHtml);
				//缓存数据
				saveCache(checkType,result);
				//tr，初始加载200条信息
				if(result.length > 0) {
					appendTr(checkType,0,200);
				}
			} else {
				$("#"+checkType+"Table").html("");
			}
		},
		complete:function(){
			hideOperTips("loadingDataDiv");
		}
	});
		
}

/**
 * 缓存数据
 */
function saveCache(checkType,data) {
	//功率检查
	if(checkType == 'powerCheck') {
		powerCheckResult = data;
	} 
	//跳频检查
	else if(checkType == 'freqHopCheck') {
		freqHopCheckResult = data;
	}
	//NCCPERM检查
	else if(checkType == 'nccperm') {
		nccpermResult = data;
	}
	//测量频点多定义
	else if(checkType == 'meaFreqMultidefined') {
		meaFreqMultidefinedResult = data;
	}	
	//测量频点漏定义
	else if(checkType == 'meaFreqMomit') {
		meaFreqMomitResult = data;
	}
	//BA表个数检查
	else if(checkType == 'baNumCheck') {
		baNumCheckResult = data;
	}		
	//同频同bsic检查
	else if(checkType == 'sameFreqBsicCheck') {
		//clear
		sameFreqBsicCheckResult.splice(0,sameFreqBsicCheckResult.length);  ;

		var bcch;
		var bsic;
		var combinedCells;
		var combinedCell;
		var cc;
		var meaDis;
		var mml;
		var pos1;
		var pos2;
		var bsc1,cell1,cell1Name,bsc2,cell2,cell2Name;
		var obj;

		for(var key in data[0]) {
			obj = data[0][key];
			//console.log(data[0]);
			
			bcch = obj["bcch"];
			bsic = obj["bsic"];
			//console.log(bcch);
			combinedCells = obj["combinedCells"][0];
			//console.log(combinedCells);
			combinedCell = combinedCells["combinedCell"];
			meaDis = combinedCells["meaDis"];
			//console.log(meaDis);
			mml = combinedCells["mml"];
			//console.log(combinedCell);
			cc = combinedCell.split(",");
			
			pos1 = cc[0].indexOf("-");
			pos2 = cc[0].indexOf("-",pos1+1);
			bsc1 = cc[0].substr(0,pos1);
			//console.log(pos2-pos1);
			cell1 = cc[0].substr(pos1+1,pos2-pos1-1);
			cell1Name = cc[0].substr(pos2+1,cc[0].length);
			//console.log(bsc1+"   "+cell1+"   "+cell1Name);
			pos1 = cc[1].indexOf("-");
			pos2 = cc[1].indexOf("-",pos1+1);
			bsc2 = cc[1].substr(0,pos1);
			cell2 = cc[1].substr(pos1+1,pos2-pos1-1);
			cell2Name = cc[1].substr(pos2+1,cc[1].length);
			
			var one = {
				'BSIC' : bsic,'BCCH' : bcch,
				'BSC1' : bsc1,'CELL1' : cell1,'CELL1_NAME':cell1Name,
				'BSC2' : bsc2,'CELL2' : cell2,'CELL2_NAME':cell2Name,
				'DISTANCE' : meaDis,'MML' : mml
			};
			sameFreqBsicCheckResult.push(one);
		}
		//sameFreqBsicCheckResult = data;
	}
	//TALIM_MAXTA检查
	else if(checkType == 'talimMaxTa') {
		talimMaxTaResult = data;
	}	

	//本站邻区漏定义
	else if(checkType == 'ncellMomit') {
		ncellMomitResult = data;
	}
	//单向邻区检查
	else if(checkType == 'unidirNcell') {
		unidirNcellResult = data;
	}
	//邻区过多过少检查
	else if(checkType == 'ncellNumCheck') {
		ncellNumCheckResult = data;
	}
	//同邻频检查
	else if(checkType == 'sameNcellFreqCheck') {
		sameNcellFreqCheckResult = data;
	}
	//邻区数据检查
	else if(checkType == 'ncellDataCheck') {
		ncellDataCheckResult = data;
	}
}

/**
 * 建立table的标题
 */
function buildTh(checkType) {
	var th = "";
	//功率检查
	if(checkType == 'powerCheck') {
		th += "<tr><th></th><th>BSC</th><th>CELL</th><th>BSPWRB</th><th>BSPWRT</th><th>指令</th></tr>";
	}	
	//跳频检查
	else if(checkType == 'freqHopCheck') {
		th += "<tr><th></th><th>BSC</th><th>CELL</th><th>CHGR</th><th>HOP</th><th>频点数</th><th>频点列表</th><th>指令</th></tr>";
	}
	//NCCPERM检查
	else if(checkType == 'nccperm') {
		th += "<tr><th></th><th>BSC</th><th>CELL</th><th>NCCPERM</th><th>缺失的NCC</th><th>指令</th></tr>";
	}
	//测量频点多定义
	else if(checkType == 'meaFreqMultidefined') {
		th += "<tr><th></th><th>BSC</th><th>CELL</th><th>LISTTYPE</th><th>多定义的频点</th><th>指令</th></tr>";
	}
	//测量频点漏定义
	else if(checkType == 'meaFreqMomit') {
		th += "<tr><th></th><th>BSC</th><th>CELL</th><th>LISTTYPE</th><th>漏定义的频点</th><th>指令</th></tr>";
	}
	//BA表个数检查
	else if(checkType == 'baNumCheck') {
		th += "<tr><th></th><th>BSC</th><th>CELL</th><th>LISTTYPE</th><th>NUM</th></tr>";
	}	
	//同频同bsic检查
	else if(checkType == 'sameFreqBsicCheck') {
		th += "<tr><th></th><th>BSIC</th><th>BCCHNO</th><th>BSC1</th><th>CELL1</th><th>CELL1_NAME</th>" 
					+"<th>BSC2</th><th>CELL2</th><th>CELL2_NAME</th><th>DISTANCE(M)</th><th>指令</th></tr>";
	}
	//TALIM_MAXTA检查
	else if(checkType == 'talimMaxTa') {
		th += "<tr><th></th><th>CREATE_TIME</th><th>BSC</th><th>CELL</th><th>TALIM</th><th>MAXTA</th></tr>";
	}

	//本站邻区漏定义
	else if(checkType == 'ncellMomit') {
		th += "<tr><th></th><th>BSC</th><th>CELL</th><th>CELLR</th><th>CELLR_BSC</th><th>CS</th><th>指令</th></tr>";
	}
	//单向邻区检查
	else if(checkType == 'unidirNcell') {
		th += "<tr><th></th><th>BSC</th><th>CELL</th><th>CELLR</th><th>DIR</th><th>CELLR_BSC</th><th>同BSC</th><th>指令</th></tr>";
	}
	//邻区过多过少检查
	else if(checkType == 'ncellNumCheck') {
		th += "<tr><th></th><th>BSC</th><th>CELL</th><th>邻区数量</th><th>邻区信息</th></tr>";
	}	
	//同邻频检查
	else if(checkType == 'sameNcellFreqCheck') {
		th += "<tr><th></th><th>BSC</th><th>CELL</th><th>CELLR</th><th>cell_bcch</th><th>cellr_bcch</th><th>cell_问题频点</th><th>cellr_问题频点</th>" 
					+"<th>DIR</th><th>CS</th><th>DISTANCE</th><th>备注</th></tr>";
	}	
	//邻区数据检查
	else if(checkType == 'ncellDataCheck') {
		th += "<tr><th></th><th>NAME</th><th>BSC</th><th>CELL</th><th>VALUE</th><th>BSCR</th><th>CELLR</th><th>VALUER</th></tr>";
	}	

	return th;
}

/**
 * 加载table的tr
 * @param checkType 检查类型
 * @param isAll 是否加载全部
 * @param start 开始数
 * @param end 结束数
 */
function appendTr(checkType,start,end) {
	var tr = "";
	//功率检查
	if(checkType == "powerCheck") {
		if(end >= powerCheckResult.length) {
			for(var i=start; i<powerCheckResult.length; i++) {
				tr += "<tr><td>"+(i+1)+"</td><td>"+getValidValue(powerCheckResult[i]['ENGNAME'],'')+"</td><td>"
						+getValidValue(powerCheckResult[i]['CELL'],'')+"</td><td>"
						+getValidValue(powerCheckResult[i]['BSPWRB'],'')+"</td><td>"
						+getValidValue(powerCheckResult[i]['BSPWRT'],'')+"</td><td>"
						+getValidValue(powerCheckResult[i]['COMMAND'],'')+"</td></tr>";
			}
		} else {
			for(var i=start; i<end; i++) {
				tr += "<tr><td>"+(i+1)+"</td><td>"+getValidValue(powerCheckResult[i]['ENGNAME'],'')+"</td><td>"
						+getValidValue(powerCheckResult[i]['CELL'],'')+"</td><td>"
						+getValidValue(powerCheckResult[i]['BSPWRB'],'')+"</td><td>"
						+getValidValue(powerCheckResult[i]['BSPWRT'],'')+"</td><td>"
						+getValidValue(powerCheckResult[i]['COMMAND'],'')+"</td></tr>";
			}
			tr += "<tr id='"+checkType+"LoadMore'>" +
					"<td colspan='3' style='text-align: center;cursor:pointer;background: #bdd3ef;border:2px solid #eee ' " +
							" onclick='appendTr(\""+checkType+"\","+end+","+powerCheckResult.length+")'>显示全部</td>" +
					"<td colspan='3' style='text-align: center;cursor:pointer;background: #bdd3ef;border:2px solid #eee ' " +
							" onclick='appendTr(\""+checkType+"\","+end+","+(end+200)+")'>加载更多</td></tr>";
		}
	}
	//跳频检查
	else if(checkType == 'freqHopCheck') {
		if(end >= freqHopCheckResult.length) {
			for(var i=start; i<freqHopCheckResult.length; i++) {
				tr += "<tr><td>"+(i+1)+"</td><td>"+getValidValue(freqHopCheckResult[i]['BSC'],'')+"</td><td>"
						+getValidValue(freqHopCheckResult[i]['CELL'],'')+"</td><td>"
						+getValidValue(freqHopCheckResult[i]['CH_GROUP'],'')+"</td><td>"
						+getValidValue(freqHopCheckResult[i]['HOP'],'')+"</td><td>"
						+getValidValue(freqHopCheckResult[i]['DCHNO'],'')+"</td><td>"
						+getValidValue(freqHopCheckResult[i]['DCH'],'')+"</td><td>"
						+getValidValue(freqHopCheckResult[i]['COMMAND'],'')+"</td></tr>";
			}
		} else {
			for(var i=start; i<end; i++) {
				tr += "<tr><td>"+(i+1)+"</td><td>"+getValidValue(freqHopCheckResult[i]['BSC'],'')+"</td><td>"
						+getValidValue(freqHopCheckResult[i]['CELL'],'')+"</td><td>"
						+getValidValue(freqHopCheckResult[i]['CH_GROUP'],'')+"</td><td>"
						+getValidValue(freqHopCheckResult[i]['HOP'],'')+"</td><td>"
						+getValidValue(freqHopCheckResult[i]['DCHNO'],'')+"</td><td>"
						+getValidValue(freqHopCheckResult[i]['DCH'],'')+"</td><td>"
						+getValidValue(freqHopCheckResult[i]['COMMAND'],'')+"</td></tr>";
			}
			tr += "<tr id='"+checkType+"LoadMore'>" +
					"<td colspan='4' style='text-align: center;cursor:pointer;background: #bdd3ef;border:2px solid #eee ' " +
							" onclick='appendTr(\""+checkType+"\","+end+","+freqHopCheckResult.length+")'>显示全部</td>" +
					"<td colspan='4' style='text-align: center;cursor:pointer;background: #bdd3ef;border:2px solid #eee ' " +
							" onclick='appendTr(\""+checkType+"\","+end+","+(end+200)+")'>加载更多</td></tr>";
		}
	}
	//NCCPERM检查
	else if(checkType == 'nccperm') {
		if(end >=nccpermResult.length) {
			for(var i=start; i<nccpermResult.length; i++) {
				tr += "<tr><td>"+(i+1)+"</td><td>"+getValidValue(nccpermResult[i]['BSC'],'')+"</td><td>"
						+getValidValue(nccpermResult[i]['CELL'],'')+"</td><td>"
						+getValidValue(nccpermResult[i]['NCCPERM'],'')+"</td><td>"
						+getValidValue(nccpermResult[i]['LEAK_NCC'],'')+"</td><td>"
						+getValidValue(nccpermResult[i]['COMMAND'],'')+"</td></tr>";
			}
		} else {
			for(var i=start; i<end; i++) {
				tr += "<tr><td>"+(i+1)+"</td><td>"+getValidValue(nccpermResult[i]['BSC'],'')+"</td><td>"
						+getValidValue(nccpermResult[i]['CELL'],'')+"</td><td>"
						+getValidValue(nccpermResult[i]['NCCPERM'],'')+"</td><td>"
						+getValidValue(nccpermResult[i]['LEAK_NCC'],'')+"</td><td>"
						+getValidValue(nccpermResult[i]['COMMAND'],'')+"</td></tr>";
			}
			tr += "<tr id='"+checkType+"LoadMore'>" +
					"<td colspan='3' style='text-align: center;cursor:pointer;background: #bdd3ef;border:2px solid #eee ' " +
							" onclick='appendTr(\""+checkType+"\","+end+","+nccpermResult.length+")'>显示全部</td>" +
					"<td colspan='3' style='text-align: center;cursor:pointer;background: #bdd3ef;border:2px solid #eee ' " +
							" onclick='appendTr(\""+checkType+"\","+end+","+(end+200)+")'>加载更多</td></tr>";
		}
	}
	//测量频点多定义
	else if(checkType == 'meaFreqMultidefined') {
		if(end >= meaFreqMultidefinedResult.length) {
			for(var i=start; i<meaFreqMultidefinedResult.length; i++) {
				tr += "<tr><td>"+(i+1)+"</td><td>"+getValidValue(meaFreqMultidefinedResult[i]['BSC'],'')+"</td><td>"
						+getValidValue(meaFreqMultidefinedResult[i]['CELL'],'')+"</td><td>"
						+getValidValue(meaFreqMultidefinedResult[i]['LISTTYPE'],'')+"</td><td>"
						+getValidValue(meaFreqMultidefinedResult[i]['OVER_DEFINE'],'')+"</td><td>"
						+getValidValue(meaFreqMultidefinedResult[i]['COMMAND'],'')+"</td></tr>";
			}
		} else {
			for(var i=start; i<end; i++) {
				tr += "<tr><td>"+(i+1)+"</td><td>"+getValidValue(meaFreqMultidefinedResult[i]['BSC'],'')+"</td><td>"
						+getValidValue(meaFreqMultidefinedResult[i]['CELL'],'')+"</td><td>"
						+getValidValue(meaFreqMultidefinedResult[i]['LISTTYPE'],'')+"</td><td>"
						+getValidValue(meaFreqMultidefinedResult[i]['OVER_DEFINE'],'')+"</td><td>"
						+getValidValue(meaFreqMultidefinedResult[i]['COMMAND'],'')+"</td></tr>";
			}
			tr += "<tr id='"+checkType+"LoadMore'>" +
					"<td colspan='3' style='text-align: center;cursor:pointer;background: #bdd3ef;border:2px solid #eee ' " +
							" onclick='appendTr(\""+checkType+"\","+end+","+meaFreqMultidefinedResult.length+")'>显示全部</td>" +
					"<td colspan='3' style='text-align: center;cursor:pointer;background: #bdd3ef;border:2px solid #eee ' " +
							" onclick='appendTr(\""+checkType+"\","+end+","+(end+200)+")'>加载更多</td></tr>";
		}
	}	
	//测量频点漏定义
	else if(checkType == 'meaFreqMomit') {
		if(end >= meaFreqMomitResult.length) {
			for(var i=start; i<meaFreqMomitResult.length; i++) {
				tr += "<tr><td>"+(i+1)+"</td><td>"+getValidValue(meaFreqMomitResult[i]['BSC'],'')+"</td><td>"
						+getValidValue(meaFreqMomitResult[i]['CELL'],'')+"</td><td>"
						+getValidValue(meaFreqMomitResult[i]['LISTTYPE'],'')+"</td><td>"
						+getValidValue(meaFreqMomitResult[i]['LEAK_DEFINE'],'')+"</td><td>"
						+getValidValue(meaFreqMomitResult[i]['COMMAND'],'')+"</td></tr>";
			}
		} else {
			for(var i=start; i<end; i++) {
				tr += "<tr><td>"+(i+1)+"</td><td>"+getValidValue(meaFreqMomitResult[i]['BSC'],'')+"</td><td>"
						+getValidValue(meaFreqMomitResult[i]['CELL'],'')+"</td><td>"
						+getValidValue(meaFreqMomitResult[i]['LISTTYPE'],'')+"</td><td>"
						+getValidValue(meaFreqMomitResult[i]['LEAK_DEFINE'],'')+"</td><td>"
						+getValidValue(meaFreqMomitResult[i]['COMMAND'],'')+"</td></tr>";
			}
			tr += "<tr id='"+checkType+"LoadMore'>" +
					"<td colspan='3' style='text-align: center;cursor:pointer;background: #bdd3ef;border:2px solid #eee ' " +
							" onclick='appendTr(\""+checkType+"\","+end+","+meaFreqMomitResult.length+")'>显示全部</td>" +
					"<td colspan='3' style='text-align: center;cursor:pointer;background: #bdd3ef;border:2px solid #eee ' " +
							" onclick='appendTr(\""+checkType+"\","+end+","+(end+200)+")'>加载更多</td></tr>";
		}
	}
	//BA表个数检查
	else if(checkType == 'baNumCheck') {
		if(end >= baNumCheckResult.length) {
			for(var i=start; i<baNumCheckResult.length; i++) {
				tr += "<tr><td>"+(i+1)+"</td><td>"+getValidValue(baNumCheckResult[i]['BSC'],'')+"</td><td>"
						+getValidValue(baNumCheckResult[i]['CELL'],'')+"</td><td>"
						+getValidValue(baNumCheckResult[i]['LISTTYPE'],'')+"</td><td>"
						+getValidValue(baNumCheckResult[i]['NUM'],'')+"</td></tr>";
			}
		} else {
			for(var i=start; i<end; i++) {
				tr += "<tr><td>"+(i+1)+"</td><td>"+getValidValue(baNumCheckResult[i]['BSC'],'')+"</td><td>"
						+getValidValue(baNumCheckResult[i]['CELL'],'')+"</td><td>"
						+getValidValue(baNumCheckResult[i]['LISTTYPE'],'')+"</td><td>"
						+getValidValue(baNumCheckResult[i]['NUM'],'')+"</td></tr>";
			}
			tr += "<tr id='"+checkType+"LoadMore'>" +
					"<td colspan='3' style='text-align: center;cursor:pointer;background: #bdd3ef;border:2px solid #eee ' " +
							" onclick='appendTr(\""+checkType+"\","+end+","+baNumCheckResult.length+")'>显示全部</td>" +
					"<td colspan='2' style='text-align: center;cursor:pointer;background: #bdd3ef;border:2px solid #eee ' " +
							" onclick='appendTr(\""+checkType+"\","+end+","+(end+200)+")'>加载更多</td></tr>";
		}
	}
	//同频同bsic检查
	else if(checkType == 'sameFreqBsicCheck') {
		if(end >= sameFreqBsicCheckResult.length) {
			for(var i=start; i<sameFreqBsicCheckResult.length; i++) {
				tr += "<tr><td>"+(i+1)+"</td><td>"+getValidValue(sameFreqBsicCheckResult[i]['BSIC'],'')+"</td><td>"
						+getValidValue(sameFreqBsicCheckResult[i]['BCCH'],'')+"</td><td>"
						+getValidValue(sameFreqBsicCheckResult[i]['BSC1'],'')+"</td><td>"
						+getValidValue(sameFreqBsicCheckResult[i]['CELL1'],'')+"</td><td>"
						+getValidValue(sameFreqBsicCheckResult[i]['CELL1_NAME'],'')+"</td><td>"
						+getValidValue(sameFreqBsicCheckResult[i]['BSC2'],'')+"</td><td>"
						+getValidValue(sameFreqBsicCheckResult[i]['CELL2'],'')+"</td><td>"
						+getValidValue(sameFreqBsicCheckResult[i]['CELL2_NAME'],'')+"</td><td>"
						+getValidValue(sameFreqBsicCheckResult[i]['DISTANCE'],'')+"</td><td>"
						+getValidValue(sameFreqBsicCheckResult[i]['MML'],'')+"</td></tr>";
			}
		} else {
			for(var i=start; i<end; i++) {
				tr += "<tr><td>"+(i+1)+"</td><td>"+getValidValue(sameFreqBsicCheckResult[i]['BSIC'],'')+"</td><td>"
						+getValidValue(sameFreqBsicCheckResult[i]['BCCH'],'')+"</td><td>"
						+getValidValue(sameFreqBsicCheckResult[i]['BSC1'],'')+"</td><td>"
						+getValidValue(sameFreqBsicCheckResult[i]['CELL1'],'')+"</td><td>"
						+getValidValue(sameFreqBsicCheckResult[i]['CELL1_NAME'],'')+"</td><td>"
						+getValidValue(sameFreqBsicCheckResult[i]['BSC2'],'')+"</td><td>"
						+getValidValue(sameFreqBsicCheckResult[i]['CELL2'],'')+"</td><td>"
						+getValidValue(sameFreqBsicCheckResult[i]['CELL2_NAME'],'')+"</td><td>"
						+getValidValue(sameFreqBsicCheckResult[i]['DISTANCE'],'')+"</td><td>"
						+getValidValue(sameFreqBsicCheckResult[i]['MML'],'')+"</td></tr>";
			}
			tr += "<tr id='"+checkType+"LoadMore'>" +
					"<td colspan='6' style='text-align: center;cursor:pointer;background: #bdd3ef;border:2px solid #eee ' " +
							" onclick='appendTr(\""+checkType+"\","+end+","+sameFreqBsicCheckResult.length+")'>显示全部</td>" +
					"<td colspan='5' style='text-align: center;cursor:pointer;background: #bdd3ef;border:2px solid #eee ' " +
							" onclick='appendTr(\""+checkType+"\","+end+","+(end+200)+")'>加载更多</td></tr>";
		}
	}
	//TALIM_MAXTA检查
	else if(checkType == 'talimMaxTa') {
		if(end >= talimMaxTaResult.length) {
			for(var i=start; i<baNumCheckResult.length; i++) {
				tr += "<tr><td>"+(i+1)+"</td><td>"+getValidValue(talimMaxTaResult[i]['CREATE_TIME'],'')+"</td><td>"
						+getValidValue(talimMaxTaResult[i]['BSC'],'')+"</td><td>"
						+getValidValue(talimMaxTaResult[i]['CELL'],'')+"</td><td>"
						+getValidValue(talimMaxTaResult[i]['TALIM'],'')+"</td><td>"
						+getValidValue(talimMaxTaResult[i]['MAXTA'],'')+"</td></tr>";
			}
		} else {
			for(var i=start; i<end; i++) {
				tr += "<tr><td>"+(i+1)+"</td><td>"+getValidValue(talimMaxTaResult[i]['CREATE_TIME'],'')+"</td><td>"
						+getValidValue(talimMaxTaResult[i]['BSC'],'')+"</td><td>"
						+getValidValue(talimMaxTaResult[i]['CELL'],'')+"</td><td>"
						+getValidValue(talimMaxTaResult[i]['TALIM'],'')+"</td><td>"
						+getValidValue(talimMaxTaResult[i]['MAXTA'],'')+"</td></tr>";
			}
			tr += "<tr id='"+checkType+"LoadMore'>" +
					"<td colspan='3' style='text-align: center;cursor:pointer;background: #bdd3ef;border:2px solid #eee ' " +
							" onclick='appendTr(\""+checkType+"\","+end+","+talimMaxTaResult.length+")'>显示全部</td>" +
					"<td colspan='3' style='text-align: center;cursor:pointer;background: #bdd3ef;border:2px solid #eee ' " +
							" onclick='appendTr(\""+checkType+"\","+end+","+(end+200)+")'>加载更多</td></tr>";
		}
	}
	
	//本站邻区漏定义
	else if(checkType == 'ncellMomit') {
		if(end >= ncellMomitResult.length) {
			for(var i=start; i<ncellMomitResult.length; i++) {
				tr += "<tr><td>"+(i+1)+"</td><td>"+getValidValue(ncellMomitResult[i]['BSC'],'')+"</td><td>"
						+getValidValue(ncellMomitResult[i]['CELL'],'')+"</td><td>"
						+getValidValue(ncellMomitResult[i]['CELLR'],'')+"</td><td>"
						+getValidValue(ncellMomitResult[i]['CELLR_BSC'],'')+"</td><td>"
						+getValidValue(ncellMomitResult[i]['CS'],'')+"</td><td>"
						+getValidValue(ncellMomitResult[i]['MML'],'')+"</td></tr>";
			}
		} else {
			for(var i=start; i<end; i++) {
				tr += "<tr><td>"+(i+1)+"</td><td>"+getValidValue(ncellMomitResult[i]['BSC'],'')+"</td><td>"
						+getValidValue(ncellMomitResult[i]['CELL'],'')+"</td><td>"
						+getValidValue(ncellMomitResult[i]['CELLR'],'')+"</td><td>"
						+getValidValue(ncellMomitResult[i]['CELLR_BSC'],'')+"</td><td>"
						+getValidValue(ncellMomitResult[i]['CS'],'')+"</td><td>"
						+getValidValue(ncellMomitResult[i]['MML'],'')+"</td></tr>";
			}
			tr += "<tr id='"+checkType+"LoadMore'>" +
					"<td colspan='4' style='text-align: center;cursor:pointer;background: #bdd3ef;border:2px solid #eee ' " +
							" onclick='appendTr(\""+checkType+"\","+end+","+ncellMomitResult.length+")'>显示全部</td>" +
					"<td colspan='3' style='text-align: center;cursor:pointer;background: #bdd3ef;border:2px solid #eee ' " +
							" onclick='appendTr(\""+checkType+"\","+end+","+(end+200)+")'>加载更多</td></tr>";
		}
	}
	//单向邻区检查
	else if(checkType == 'unidirNcell') {
		if(end >= unidirNcellResult.length) {
			for(var i=start; i<unidirNcellResult.length; i++) {
				tr += "<tr><td>"+(i+1)+"</td><td>"+getValidValue(unidirNcellResult[i]['BSC'],'')+"</td><td>"
						+getValidValue(unidirNcellResult[i]['CELL'],'')+"</td><td>"
						+getValidValue(unidirNcellResult[i]['CELLR'],'')+"</td><td>"
						+getValidValue(unidirNcellResult[i]['DIR'],'')+"</td><td>"
						+getValidValue(unidirNcellResult[i]['CELLR_BSC'],'')+"</td><td>"
						+getValidValue(unidirNcellResult[i]['IS_SAME_BSC'],'')+"</td><td>"
						+getValidValue(unidirNcellResult[i]['COMMAND'],'')+"</td></tr>";
			}
		} else {
			for(var i=start; i<end; i++) {
				tr += "<tr><td>"+(i+1)+"</td><td>"+getValidValue(unidirNcellResult[i]['BSC'],'')+"</td><td>"
						+getValidValue(unidirNcellResult[i]['CELL'],'')+"</td><td>"
						+getValidValue(unidirNcellResult[i]['CELLR'],'')+"</td><td>"
						+getValidValue(unidirNcellResult[i]['DIR'],'')+"</td><td>"
						+getValidValue(unidirNcellResult[i]['CELLR_BSC'],'')+"</td><td>"
						+getValidValue(unidirNcellResult[i]['IS_SAME_BSC'],'')+"</td><td>"
						+getValidValue(unidirNcellResult[i]['COMMAND'],'')+"</td></tr>";
			}
			tr += "<tr id='"+checkType+"LoadMore'>" +
					"<td colspan='4' style='text-align: center;cursor:pointer;background: #bdd3ef;border:2px solid #eee ' " +
							" onclick='appendTr(\""+checkType+"\","+end+","+unidirNcellResult.length+")'>显示全部</td>" +
					"<td colspan='4' style='text-align: center;cursor:pointer;background: #bdd3ef;border:2px solid #eee ' " +
							" onclick='appendTr(\""+checkType+"\","+end+","+(end+200)+")'>加载更多</td></tr>";
		}
	}
	//邻区过多过少检查
	else if(checkType == 'ncellNumCheck') {
		if(end >= ncellNumCheckResult.length) {
			for(var i=start; i<ncellNumCheckResult.length; i++) {
				tr += "<tr><td>"+(i+1)+"</td><td>"+getValidValue(ncellNumCheckResult[i]['BSC'],'')+"</td><td>"
						+getValidValue(ncellNumCheckResult[i]['CELL'],'')+"</td><td>"
						+getValidValue(ncellNumCheckResult[i]['N_CELL_NUM'],'')+"</td><td>"
						+getValidValue(ncellNumCheckResult[i]['N_CELLS'],'')+"</td></tr>";
			}
		} else {
			for(var i=start; i<end; i++) {
				tr += "<tr><td>"+(i+1)+"</td><td>"+getValidValue(ncellNumCheckResult[i]['BSC'],'')+"</td><td>"
						+getValidValue(ncellNumCheckResult[i]['CELL'],'')+"</td><td>"
						+getValidValue(ncellNumCheckResult[i]['N_CELL_NUM'],'')+"</td><td>"
						+getValidValue(ncellNumCheckResult[i]['N_CELLS'],'')+"</td></tr>";
			}
			tr += "<tr id='"+checkType+"LoadMore'>" +
					"<td colspan='3' style='text-align: center;cursor:pointer;background: #bdd3ef;border:2px solid #eee ' " +
							" onclick='appendTr(\""+checkType+"\","+end+","+ncellNumCheckResult.length+")'>显示全部</td>" +
					"<td colspan='2' style='text-align: center;cursor:pointer;background: #bdd3ef;border:2px solid #eee ' " +
							" onclick='appendTr(\""+checkType+"\","+end+","+(end+200)+")'>加载更多</td></tr>";
		}
	}
	//同邻频检查
	else if(checkType == 'sameNcellFreqCheck') {
		if(end >= sameNcellFreqCheckResult.length) {
			for(var i=start; i<sameNcellFreqCheckResult.length; i++) {
				tr += "<tr><td>"+(i+1)+"</td><td>"+getValidValue(sameNcellFreqCheckResult[i]['BSC'],'')+"</td><td>"
						+getValidValue(sameNcellFreqCheckResult[i]['CELL'],'')+"</td><td>"
						+getValidValue(sameNcellFreqCheckResult[i]['CELLR'],'')+"</td><td>"
						+getValidValue(sameNcellFreqCheckResult[i]['CELL_BCCH'],'')+"</td><td>"
						+getValidValue(sameNcellFreqCheckResult[i]['CELLR_BCCH'],'')+"</td><td>"
						+getValidValue(sameNcellFreqCheckResult[i]['CELL_FREQ'],'')+"</td><td>"
						+getValidValue(sameNcellFreqCheckResult[i]['CELLR_FREQ'],'')+"</td><td>"
						+getValidValue(sameNcellFreqCheckResult[i]['DIR'],'')+"</td><td>"
						+getValidValue(sameNcellFreqCheckResult[i]['CS'],'')+"</td><td>"
						+getValidValue(sameNcellFreqCheckResult[i]['DISTANCE'],'')+"</td><td>"
						+getValidValue(sameNcellFreqCheckResult[i]['COMMENT'],'')+"</td></tr>";
			}
		} else {
			for(var i=start; i<end; i++) {
				tr += "<tr><td>"+(i+1)+"</td><td>"+getValidValue(sameNcellFreqCheckResult[i]['BSC'],'')+"</td><td>"
						+getValidValue(sameNcellFreqCheckResult[i]['CELL'],'')+"</td><td>"
						+getValidValue(sameNcellFreqCheckResult[i]['CELLR'],'')+"</td><td>"
						+getValidValue(sameNcellFreqCheckResult[i]['CELL_BCCH'],'')+"</td><td>"
						+getValidValue(sameNcellFreqCheckResult[i]['CELLR_BCCH'],'')+"</td><td>"
						+getValidValue(sameNcellFreqCheckResult[i]['CELL_FREQ'],'')+"</td><td>"
						+getValidValue(sameNcellFreqCheckResult[i]['CELLR_FREQ'],'')+"</td><td>"
						+getValidValue(sameNcellFreqCheckResult[i]['DIR'],'')+"</td><td>"
						+getValidValue(sameNcellFreqCheckResult[i]['CS'],'')+"</td><td>"
						+getValidValue(sameNcellFreqCheckResult[i]['DISTANCE'],'')+"</td><td>"
						+getValidValue(sameNcellFreqCheckResult[i]['COMMENT'],'')+"</td></tr>";
			}
			tr += "<tr id='"+checkType+"LoadMore'>" +
					"<td colspan='6' style='text-align: center;cursor:pointer;background: #bdd3ef;border:2px solid #eee ' " +
							" onclick='appendTr(\""+checkType+"\","+end+","+sameNcellFreqCheckResult.length+")'>显示全部</td>" +
					"<td colspan='6' style='text-align: center;cursor:pointer;background: #bdd3ef;border:2px solid #eee ' " +
							" onclick='appendTr(\""+checkType+"\","+end+","+(end+200)+")'>加载更多</td></tr>";
		}
	}
	//邻区数据检查
	else if(checkType == 'ncellDataCheck') {
		if(end >= ncellDataCheckResult.length) {
			for(var i=start; i<ncellDataCheckResult.length; i++) {
				tr += "<tr><td>"+(i+1)+"</td><td>"+getValidValue(ncellDataCheckResult[i]['NAME'],'')+"</td><td>"
						+getValidValue(ncellDataCheckResult[i]['BSC'],'')+"</td><td>"
						+getValidValue(ncellDataCheckResult[i]['CELL'],'')+"</td><td>"
						+getValidValue(ncellDataCheckResult[i]['VALUE'],'')+"</td><td>"
						+getValidValue(ncellDataCheckResult[i]['BSCR'],'')+"</td><td>"
						+getValidValue(ncellDataCheckResult[i]['CELLR'],'')+"</td><td>"
						+getValidValue(ncellDataCheckResult[i]['VALUER'],'')+"</td></tr>";
			}
		} else {
			for(var i=start; i<end; i++) {
				tr += "<tr><td>"+(i+1)+"</td><td>"+getValidValue(ncellDataCheckResult[i]['NAME'],'')+"</td><td>"
						+getValidValue(ncellDataCheckResult[i]['BSC'],'')+"</td><td>"
						+getValidValue(ncellDataCheckResult[i]['CELL'],'')+"</td><td>"
						+getValidValue(ncellDataCheckResult[i]['VALUE'],'')+"</td><td>"
						+getValidValue(ncellDataCheckResult[i]['BSCR'],'')+"</td><td>"
						+getValidValue(ncellDataCheckResult[i]['CELLR'],'')+"</td><td>"
						+getValidValue(ncellDataCheckResult[i]['VALUER'],'')+"</td></tr>";
			}
			tr += "<tr id='"+checkType+"LoadMore'>" +
					"<td colspan='4' style='text-align: center;cursor:pointer;background: #bdd3ef;border:2px solid #eee ' " +
							" onclick='appendTr(\""+checkType+"\","+end+","+ncellDataCheckResult.length+")'>显示全部</td>" +
					"<td colspan='4' style='text-align: center;cursor:pointer;background: #bdd3ef;border:2px solid #eee ' " +
							" onclick='appendTr(\""+checkType+"\","+end+","+(end+200)+")'>加载更多</td></tr>";
		}
	}

	//移除旧的加载按钮tr
	$("#"+checkType+"LoadMore").remove();
	//加载更多数据到table
	$("#"+checkType+"Table").append(tr);
}





/**
 * 加载BSC
 */
function getAllBsc(cityId) {
	var cityId = $("#cityId").val();
	showOperTips("loadingDataDiv", "loadContentId", "正在加载");
	$.ajax({
		url : 'getAllBscByCityIdForAjaxAction',
		data : {
			'cityId' : cityId
		},
		type : 'post',
		dataType : 'text',
		success : function(raw) {
			var data = eval("("+raw+")");
			var flag = data["flag"];
			var bscList = data["bscList"];
			if(flag){
				var html = "";
				for(var i=0; i<bscList.length; i++) {
					html +="<option value='"+bscList[i].BSC_ID+"'>"+bscList[i].ENGNAME+"</option>";
				}
				$("#defaultBsc").html(html);
			} else {
				$("#defaultBsc").html("");
			}
		},
		complete:function(){
			hideOperTips("loadingDataDiv");
		}
	})
}

/**
 * 确认选择的BSC
 */
function sumBsc() {
	var bscStr = "";
	for(var i=0;i<document.getElementById("selectedBsc").options.length;i++){
       bscStr += document.getElementById("selectedBsc").options[i].text + ","; 
    }
    bscStr = bscStr.substring(0,bscStr.length - 1);
    //console.log(bscStr);
    $("#bscStr").val("");
    $("#bscStr").val(bscStr);
    $("#selectBscDiv").hide();
}


//BSC选择器
function PutRightOneClk() {
    if(document.getElementById("defaultBsc").options.selectedIndex == -1){return false;}
    while(document.getElementById("defaultBsc").options.selectedIndex > -1){
        var id = document.getElementById("defaultBsc").options.selectedIndex
        var varitem = new Option(document.getElementById("defaultBsc").options[id].text,document.getElementById("defaultBsc").options[id].value);
        document.getElementById("selectedBsc").options.add(varitem);
        document.getElementById("defaultBsc").options.remove(id);
    }
}
function PutRightAllClk() {
    if(document.getElementById("defaultBsc").options.length == 0){return false;}
    for(var i=0;i<document.getElementById("defaultBsc").options.length;i++){
        var varitem = new Option(document.getElementById("defaultBsc").options[i].text,document.getElementById("defaultBsc").options[i].value);
        document.getElementById("selectedBsc").options.add(varitem);
    }
    document.getElementById("defaultBsc").options.length = 0;
}
function PutLeftOneClk() {
    if(document.getElementById("selectedBsc").options.selectedIndex == -1){return false;}
    while(document.getElementById("selectedBsc").options.selectedIndex > -1){
        var id = document.getElementById("selectedBsc").options.selectedIndex
        var varitem = new Option(document.getElementById("selectedBsc").options[id].text,document.getElementById("selectedBsc").options[id].value);
        document.getElementById("defaultBsc").options.add(varitem);
        document.getElementById("selectedBsc").options.remove(id);
    }
}
function PutLeftAllClk() {
    if(document.getElementById("selectedBsc").options.length == 0){return false;}
    for(var i=0;i<document.getElementById("selectedBsc").options.length;i++){
        var varitem = new Option(document.getElementById("selectedBsc").options[i].text,document.getElementById("selectedBsc").options[i].value);
        document.getElementById("defaultBsc").options.add(varitem);
    }
    document.getElementById("selectedBsc").options.length = 0;
}
