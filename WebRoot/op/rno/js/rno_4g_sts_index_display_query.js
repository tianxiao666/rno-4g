var optionsLength;//获取指标列的总长度
var columnObj = new Object(); //保存指标列
var columnArr = new Array(); //指标列数组
var defColumnArr = new Array(); //初始指标列数组
var firstTime = true;
var defIndexLabelStr="rrc_ConnEstabSucc,erab_EstabSucc,wireConn,erab_Drop_CellLevel,switchSucc";
var defIndexNameStr="RRC连接建立成功率,E-RAB建立成功率,无线接通率,E-RAB掉线率(小区级),切换成功率";
//var downloadData;

$(function() {
		//tab选项卡
	//tab("div_tab", "li", "onclick");//项目服务范围类别切换
	loadevent();
	});


/**
 *触发省市区域联动事件
 */
function loadevent() {
	//区域联动
	initAreaCascade();
	initFormSubmitEvent()
	getRno4GStsIndexDesc();
	getAllCellByAreaId($("#cityId").val());
	initDisplayTab();
	initBindEvent();
}

function initBindEvent(){
}
/**
 * 初始化表格
 */
function initDisplayTab(){
/*	var labelArr=defIndexLabelStr.split(",");
	var nameArr=defIndexNameStr.split(",");
	var html="";
	for(var i=0;i<nameArr.length;i++){
		html+="<th id='"+labelArr[i].toUpperCase()+"' >"+nameArr[i]+"</th>";
		columnArr.push(labelArr[i].toUpperCase());
	}
	$("#tab_0_queryResultTab thead tr").append(html);*/
	initIndexDiv();
}
/**
 * 初始化区域联动
 */
function initAreaCascade() {
	
	$("#provinceId").change(function() {
		getSubAreas("provinceId", "cityId", "市");	
	});
	$("#cityId").change(function() {
		getAllCellByAreaId($("#cityId").val()) ;
/*				var cityId2 = $("#cityId").val();
				$("#cityId2").val(cityId2);*/
			});
/*	$("#provinceId2").change(function() {
		getSubAreas("provinceId2", "cityId2", "市");		
	});

	$("#cityId2").change(function() {
		
		 * getSubAreas("cityId2", "areaId2", "区/县",function(){
		 * $("#areaId2").append("<option selected='true' value=''>全部</option>");
		 * });
		 
		// 获取cityId赋值到表单隐藏域
		var cityId = $("#cityId2").val();
		$("#cityId").val(cityId);
		getAllCellByAreaId($("#cityId2").val()) ;
	});*/
}
/**
 * 初始化表单提交事件
 */
function initFormSubmitEvent() {
	$("#form_tab_0").submit(	function() {
		//重新初始化分页参数
		if ($("#form_tab_0 input[name='queryCondition.stsDate']").val() == "") {
			alert("请输入日期!")
			return false;
			}
/*		$("#tab_0_hiddenPageSize").val("25");
		$("#tab_0_hiddenCurrentPage").val("1");
		$("#tab_0_hiddenTotalPageCnt").val("0");
		$("#tab_0_hiddenTotalCnt").val("0");*/
		initFormPage("form_tab_0");
		var indexStr = $("#indexHiddenNameStr").val();
		if(indexStr.length==0){
			$("#indexHiddenNameStr").val(defIndexNameStr);
			$("#indexHiddenStr").val(defIndexLabelStr.toUpperCase());
		}
		var cells = $("#cellHiddenNameStr").val();
		if(cells==null||cells.length==0){
			alert("请选择要查看的小区");
			return false;
		}
/*		var cellValue = $("#form_tab_0 .encell").val();
		var cellChValue = $("#form_tab_0 .chcell").val();
		if (cellValue != "小区英文名") {
			$("#form_tab_0 input[name='queryCondition.cell']").val(cellValue);
			} else {
				$("#form_tab_0 input[name='queryCondition.cell']").val("");
				}
		if (cellChValue != "小区中文名") {
			$("#form_tab_0 input[name='queryCondition.cellChineseName']")
			.val(cellChValue);
			} else {
				$("#form_tab_0 input[name='queryCondition.cellChineseName']").val("");
				}*/
		getRno4GStsIndexData("form_tab_0", "tab_0_queryResultTab","page_div_0");
		//getFormDataToExportForm("form_tab_0");//获取查询form表单里的数据 添加到导出form表单里
		return false;
		});
}

/**
 * 初始化页面配置信息
 * @param formId
 */
function initFormPage(formId) {
	var form = $("#" + formId);
	if (!form) {
		return;
	}
	form.find("#tab_0_hiddenPageSize").val(25);
	form.find("#tab_0_hiddenCurrentPage").val(1);
	form.find("#tab_0_hiddenTotalPageCnt").val(-1);
	form.find("#tab_0_hiddenTotalCnt").val(-1);
}
/**
 * 获取话统指标数据
 * @param formId
 * @param tabFlag
 */
function getRno4GStsIndexData(formId,tableId,pageDivId) {
	showOperTips("loadingDataDiv", "loadContentId", "正在加载");
	$("#form_tab_0").ajaxSubmit({
		dataType : 'text',
		success : function(raw) {
			var data = eval("(" + raw + ")");
			showRno4GStsIndexData(data['data'],tableId);
			setPage(data['page'],formId,pageDivId);
/*			setFormPageInfo(data['page'],formId);
			setPageView(data['page'],pageDivId);
			downloadData = data;*/
		},
		complete : function() {
			firstTime = false;
			$(".loading_cover").css("display", "none");
		}
	});
}
/**
 * 显示话统指标数据
 * @param data
 * @param formId
 * @param tabFlag
 */
function showRno4GStsIndexData(data,tableId){
	//先保存下载用参数
	initDownloadAttachParams();
	if(data==null||data==undefined){
		return;
	}
	var table = $("#"+tableId);
/*	$("#"+tableId+" tr:not(:first)").each(function(i, ele) {
		$(ele).remove();
	});*/
	$("#"+tableId+" tbody").remove();
	var html = "";
	var len = data.length;
	html+="<tbody>";
	for(var i=0;i<len;i++){
		line = data[i];
		html += "<tr>";
		html += "<td>"+line.MEABEGTIME+"</td>";
		html += "<td>"+line.MEAENDTIME+"</td>";
		html += "<td>"+line.CELLNAME+"</td>";
		for(var j=0;j<columnArr.length;j++){
			//console.log(columnArr[j]);
			var columnStr = "line['"+columnArr[j]+"']";
			html +=	"<td>"+eval("(" + columnStr + ")")+"</td>";
		}
		html +="</tr>";
	}
	html+="</tbody>";
	//console.log(html);
	table.append(html);
}
/**
 * 设置分页属性
 * @param page
 * @param formId
 * @param divId
 */
function setPage(page,formId,divId){
	if (divId == null || divId == undefined || formId == null || formId == undefined || page == null || page == undefined) {
		return;
	}
	setFormPageInfo(page,formId);
	setPageView(page, divId);
}
/**
 * 改变Form的page值
 * @param formId
 * @param page
 */
function setFormPageInfo(page,formId) {
	if (formId == null || formId == undefined || page == null || page == undefined) {
		return;
	}

	var form = $("#" + formId);
	if (!form) {
		return;
	}

	// console.log("setFormPageInfo .
	// pageSize="+page.pageSize+",currentPage="+page.currentPage+",totalPageCnt="+page.totalPageCnt+",totalCnt="+page.totalCnt);
	form.find("#tab_0_hiddenPageSize").val(page.pageSize);
	form.find("#tab_0_hiddenCurrentPage").val(new Number(page.currentPage));// /
	form.find("#tab_0_hiddenTotalPageCnt").val(page.totalPageCnt);
	form.find("#tab_0_hiddenTotalCnt").val(page.totalCnt);

}
/**
 * 设置分页面板
 * 
 * @param page
 *            分页信息
 * @param divId
 *            分页面板id
 */
function setPageView(page, divId) {
	if (divId == null || divId == undefined || page == null || page == undefined) {
		return;
	}

	var div = $("#" + divId);
	if (!div) {
		return;
	}

	//
	var pageSize = page['pageSize'] ? page['pageSize'] : 0;
	// $("#hiddenPageSize").val(pageSize);

	var currentPage = page['currentPage'] ? page['currentPage'] : 1;
	// $("#hiddenCurrentPage").val(currentPage);

	var totalPageCnt = page['totalPageCnt'] ? page['totalPageCnt'] : 0;
	// $("#hiddenTotalPageCnt").val(totalPageCnt);

	var totalCnt = page['totalCnt'] ? page['totalCnt'] : 0;
	// $("#hiddenTotalCnt").val(totalCnt);

	// 设置到面板上
	$(div).find("#tab_0_emTotalCnt").html(totalCnt);
	$(div).find("#tab_0_showCurrentPage").val(currentPage);
	$(div).find("#tab_0_emTotalPageCnt").html(totalPageCnt);
}

/**
* 分页跳转的响应
* @param dir
* @param action（方法名）
* @param formId
* @param divId
*/
function showListViewByPage(dir,action,formId,pageDivId,tabFlag) {
	
	var form=$("#"+formId);
	var div=$("#"+pageDivId);
	//alert(form.find("#hiddenPageSize").val());
	var pageSize =new Number(form.find("#"+tabFlag+"hiddenPageSize").val());
	var currentPage = new Number(form.find("#"+tabFlag+"hiddenCurrentPage").val());
	var totalPageCnt =new Number(form.find("#"+tabFlag+"hiddenTotalPageCnt").val());
	var totalCnt = new Number(form.find("#"+tabFlag+"hiddenTotalCnt").val());

	//console.log("pagesize="+pageSize+",currentPage="+currentPage+",totalPageCnt="+totalPageCnt+",totalCnt="+totalCnt);
	if (dir === "first") {
		if (currentPage <= 1) {
			return;
		} else {
			form.find("#"+tabFlag+"hiddenCurrentPage").val("1");
		}
	} else if (dir === "last") {
		if (currentPage >= totalPageCnt) {
			return;
		} else {
			form.find("#"+tabFlag+"hiddenCurrentPage").val(totalPageCnt);
		}
	} else if (dir === "back") {
		if (currentPage <= 1) {
			return;
		} else {
			form.find("#"+tabFlag+"hiddenCurrentPage").val(currentPage - 1);
		}
	} else if (dir === "next") {
		if (currentPage >= totalPageCnt) {
			return;
		} else {
			form.find("#"+tabFlag+"hiddenCurrentPage").val(currentPage + 1);
		}
	} else if (dir === "num") {
		var userinput = $(div).find("#"+tabFlag+"showCurrentPage").val();
		if (isNaN(userinput)) {
			alert("请输入数字！")
			return;
		}
		if (userinput > totalPageCnt || userinput < 1) {
			alert("输入页面范围不在范围内！");
			return;
		}
		$(form).find("#"+tabFlag+"hiddenCurrentPage").val(userinput);
	}else{
		return;
	}
	var tableId = tabFlag + "queryResultTab";
	//获取资源
	if(typeof action =="function"){
		action(formId,tableId,pageDivId);
	}else{
		getRno4GStsIndexData(formId,tableId,pageDivId);
	}
}

/**
 * 获取话统指标项
 */
function getRno4GStsIndexDesc(){
	showOperTips("loadingDataDiv", "loadContentId", "正在加载");
	$.ajax({
		url : 'getRno4GStsIndexDescForAjaxAction',
		data : {},
		dataType : 'text',
		type : 'post',
		success : function(raw) {
			var data;
			try {
				data = eval("(" + raw + ")");
				// console.log("data.state:"+data.state);
				var html;
				optionsLength = data.length;
				for(var i=0;i<optionsLength;i++){
					html +="<option id ='index_option_"+i+"' value='"+data[i].INDEX_REAL_NAME.trim()+"'>"+data[i].INDEX_DISPLAY_NAME.trim() + "</option>";
				}
				$("#defaultIndex").html(html);
			} catch (err) {
			}
		},
		complete : function() {
			hideOperTips("loadingDataDiv");
		}
	});
}
/**
 * 加载Cell
 */
function getAllCellByAreaId(areaId) {
	showOperTips("loadingDataDiv", "loadContentId", "正在加载");
	$.ajax({
		//url : 'getAllCellByAreaIdForAjaxAction',
		url:'getAllLteCellsByCityIdInMapForAjaxAction',
		data : {
			//'areaId' : areaId
			'cityId' : areaId
		},
		type : 'post',
		dataType : 'text',
		success : function(raw) {
			var data = eval("(" + raw + ")");
			var html = "";
			//console.log(data);
			for ( var i in data) {				
				var cells = data[i];
				var datalen=cells.length;
				for ( var j = 0; j < datalen; j++) {
					html += "<option value='" + cells[j].LABEL + "'>" + cells[j].NAME + "(" +cells[j].LABEL + ")" + "</option>";
				}
				//console.log(html);
			}
			$("#selectedCell").html("");
			$("#defaultCell").html(html);
		},
		complete:function(){
			hideOperTips("loadingDataDiv");
		}
	});
}
function initDownloadAttachParams(){
	//清空并保存
	$("#cellNameStrForDownload").val("");
	$("#cellNameStrForDownload").val($("#cellHiddenNameStr").val());
	$("#indexStrForDownload").val("");
	$("#indexStrForDownload").val($("#indexHiddenStr").val());
	$("#indexNameStrForDownload").val("");
	$("#indexNameStrForDownload").val($("#indexHiddenNameStr").val());
	$("#cityIdForDownload").val("");
	$("#cityIdForDownload").val($("#cityId").val());
	$("#begTimeForDownload").val("");
	$("#begTimeForDownload").val($("#beginTime").val());
	$("#endTimeForDownload").val("");
	$("#endTimeForDownload").val($("#latestAllowedTime").val());
}
function downloadIndexDataFile(){
	
	var form = $("#downloadFileForm");
	form.submit();
	
	
	
	/*
	var sendData = {
			'attachParams.cellNameStrForDownload' : $("#cellNameStrForDownload").val(),
			'attachParams.indexStrForDownload' : $("#indexStrForDownload").val(),
			'attachParams.indexNameStrForDownload' : $("#indexNameStrForDownload").val(),
			'attachParams.cityIdForDownload' : $("#cityIdForDownload").val(),
			'attachParams.begTimeForDownload' : $("#begTimeForDownload").val(),
			'attachParams.endTimeForDownload' : $("#endTimeForDownload").val()
		}
	showOperTips("loadingDataDiv", "loadContentId", "正在执行导出");
	$.ajax({
		url:'downloadLteStsCellIndexFileAction',
		data :sendData,
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
				$("#progressDesc").html("");
			}
		},
		complete:function(){
			//hideOperTips("loadingDataDiv");
		}
	});
*/
	
}
//BSC选择器
function PutRightOneClk(defaultDomId,selectId) {
    if(document.getElementById(defaultDomId).options.selectedIndex == -1){return false;}
    while(document.getElementById(defaultDomId).options.selectedIndex > -1){
        var id = document.getElementById(defaultDomId).options.selectedIndex
        var varitem = new Option(document.getElementById(defaultDomId).options[id].text,document.getElementById(defaultDomId).options[id].value);        
        var domId = document.getElementById(defaultDomId).options[id].id;
        if(domId!=null){
        	varitem.setAttribute("id",domId);
        }
        var index = 0;
        if(document.getElementById(selectId).options.length>0){
        	index=document.getElementById(selectId).options.length;
        	for(var i=0;i<document.getElementById(selectId).options.length;i++){
        		if(Number(domId.split("_")[2])<Number(document.getElementById(selectId).options[i].id.split("_")[2])){
        			index = i;
        			break;
        		}
        	}
        }
        document.getElementById(selectId).options.add(varitem,index);
        document.getElementById(defaultDomId).options.remove(id);
        //document.getElementById(defaultDomId).options[id]==null;
    }
}
function PutRightAllClk(defaultDomId,selectId) {
    if(document.getElementById(defaultDomId).options.length == 0){return false;}
    for(var i=0;i<document.getElementById(defaultDomId).options.length;i++){
        var varitem = new Option(document.getElementById(defaultDomId).options[i].text,document.getElementById(defaultDomId).options[i].value);
        var domId = document.getElementById(defaultDomId).options[i].id;
        if(domId!=null){
        	varitem.setAttribute("id",domId);
        }
        var index = 0;
        if(document.getElementById(selectId).options.length>0){
        	index=document.getElementById(selectId).options.length;
        	for(var j=0;j<document.getElementById(selectId).options.length;j++){
        		if(Number(domId.split("_")[2])<Number(document.getElementById(selectId).options[j].id.split("_")[2])){
        			index = j;
        			break;
        		}
        	}
        }
        document.getElementById(selectId).options.add(varitem,index);
        //document.getElementById(selectId).options.add(varitem);
    }
    document.getElementById(defaultDomId).options.length = 0;
}
function PutLeftOneClk(defaultDomId,selectId) {
    if(document.getElementById(selectId).options.selectedIndex == -1){return false;}
    while(document.getElementById(selectId).options.selectedIndex > -1){
        var id = document.getElementById(selectId).options.selectedIndex
        var varitem = new Option(document.getElementById(selectId).options[id].text,document.getElementById(selectId).options[id].value);
        var domId = document.getElementById(selectId).options[id].id;
        if(domId!=null){
        	varitem.setAttribute("id",domId);
        }
        var index = 0;
        if(document.getElementById(defaultDomId).options.length>0){
        	index=document.getElementById(defaultDomId).options.length;
        	for(var i=0;i<document.getElementById(defaultDomId).options.length;i++){
        		if(Number(domId.split("_")[2])<Number(document.getElementById(defaultDomId).options[i].id.split("_")[2])){
        			index = i;
        			break;
        		}
        	}
        }
        document.getElementById(defaultDomId).options.add(varitem,index);
        document.getElementById(selectId).options.remove(id);
    }
}
function PutLeftAllClk(defaultDomId,selectId) {
    if(document.getElementById(selectId).options.length == 0){return false;}
    for(var i=0;i<document.getElementById(selectId).options.length;i++){
        var varitem = new Option(document.getElementById(selectId).options[i].text,document.getElementById(selectId).options[i].value);
        var domId = document.getElementById(selectId).options[i].id;
        if(domId!=null){
        	varitem.setAttribute("id",domId);
        }
        var index = 0;
        if(document.getElementById(defaultDomId).options.length>0){
        	index=document.getElementById(defaultDomId).options.length;
        	for(var j=0;j<document.getElementById(defaultDomId).options.length;j++){
        		if(Number(domId.split("_")[2])<Number(document.getElementById(defaultDomId).options[j].id.split("_")[2])){
        			index = j;
        			break;
        		}
        	}
        }
        document.getElementById(defaultDomId).options.add(varitem,index);
        //document.getElementById(defaultDomId).options.add(varitem);
    }
    document.getElementById(selectId).options.length = 0;
}
function initIndexDiv(){
	var defaultDomId="defaultIndex";
	var selectId="selectedIndex";
	var arrT = defIndexLabelStr.split(",");
	if(document.getElementById(selectId).options.length == 0){
		for(var j=0;j<arrT.length;j++){
			for(var i=0;i<document.getElementById(defaultDomId).options.length;i++){
				if(document.getElementById(defaultDomId).options[i].value.trim()==arrT[j].trim()){
					var varitem = new Option(document.getElementById(defaultDomId).options[i].text,document.getElementById(defaultDomId).options[i].value);
					var domId = document.getElementById(defaultDomId).options[i].id;
			        if(domId!=null){
			        	varitem.setAttribute("id",domId);
			        }
			        var index = 0;
			        if(document.getElementById(selectId).options.length>0){
			        	index=document.getElementById(selectId).options.length;
			        	for(var k=0;k<document.getElementById(selectId).options.length;k++){
			        		if(Number(domId.split("_")[2])<Number(document.getElementById(selectId).options[k].id.split("_")[2])){
			        			index = k;
			        			break;
			        		}
			        	}
			        }
					document.getElementById(selectId).options.add(varitem);
					document.getElementById(defaultDomId).options.remove(i);
					break;
				}
			}
	    }
	}
}
/**
 * 确认选择的BSC
 *//*
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
}*/

/**
 * 确认选择的Index
 */
function sumIndex() {
	if(!firstTime){
		if(!confirm("改变指标项将清空表格中数据？")){
			return;
		}
	}
	
	var tableStr = "";
	var indexLabelStr = "";
	var indexNameStr = "";
	columnArr.length=0;
	for(var i=0;i<document.getElementById("selectedIndex").options.length;i++){
		tableStr += "<th id='"+document.getElementById("selectedIndex").options[i].value+"'>"+document.getElementById("selectedIndex").options[i].text+"</th> ";
		columnArr.push(document.getElementById("selectedIndex").options[i].value.toUpperCase());
		indexLabelStr += document.getElementById("selectedIndex").options[i].value.toUpperCase() + ","; 
		indexNameStr +=document.getElementById("selectedIndex").options[i].text + ",";
    }
	indexLabelStr = indexLabelStr.substring(0,indexLabelStr.length - 1);
	indexNameStr = indexNameStr.substring(0,indexNameStr.length - 1);
	$("#tab_0_queryResultTab tbody").remove();
	$("#tab_0_queryResultTab thead tr th:gt(2)").remove();
    $("#tab_0_queryResultTab thead tr").append(tableStr);
    $("#indexHiddenStr").val("");
    $("#indexHiddenStr").val(indexLabelStr);
    $("#indexHiddenNameStr").val("");
    $("#indexHiddenNameStr").val(indexNameStr);
    $("#selectIndexDiv").hide();
}

/**
 * 确认选择的Index
 */
function sumCell() {
	var cellStr = "";
	var cellNameStr = "";
	var cellLabelStr = "";
	var len = document.getElementById("selectedCell").options.length;
	if(len>1000){
		alert("选择的小区不能超过1000个！");
		return;
	}
	for(var i=0;i<len;i++){
		var op = document.getElementById("selectedCell").options[i];
		var name = op.text.split("(")[0].trim()+"";
		cellStr += op.text + ",";
		cellNameStr += name + ",";
		cellLabelStr += op.value+ ","; 
    }
	cellStr = cellStr.substring(0,cellStr.length - 1);
	cellNameStr = cellNameStr.substring(0,cellNameStr.length - 1);
	cellLabelStr = cellLabelStr.substring(0,cellLabelStr.length - 1);
    $("#cellStr").val("");
    $("#cellStr").val(cellStr);
    $("#cellHiddenNameStr").val("");
    $("#cellHiddenNameStr").val(cellNameStr);
    $("#cellHiddenLabelStr").val("");
    $("#cellHiddenLabelStr").val(cellLabelStr);
    $("#selectCellDiv").hide();
}