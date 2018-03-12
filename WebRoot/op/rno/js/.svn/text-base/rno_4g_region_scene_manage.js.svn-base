//点击td转换可编辑
var editHTML;
var editText;
var oldData;
var submitStatus=true;
//var SceneType="region";
var sceneNames; //保存场景名列表


$(document).ready(function() {
	// 设置jquery ui
//	jqueryUiSet();
	//绑定事件
	bindEvent();
	//获取场景名列表
	//getSceneNameListTask(sceneType);
	clearAll();
	getSceneNameListTask();
	
	
});

//绑定事件
function bindEvent(){
	
	$("#flashtable").click(function() {
		var flag = confirm("确定刷新页面？你将放弃所有未提交的修改。");
		if (flag == false){
		  return false;
		}
		$("#sceneNameTip").html("");
		//添加记录
		$("#addScene").bind("click",function(){
			initSceneTable();
		});

		submitStatus=true;
		clearOldDataTip();
		clearErrTip();
		clearAll();
		getSceneNameListTask();
	});

/*	$("#altertable").click(function() {
		var flag = confirm("允许修改？");
		if (flag == false){
			  return false;
			}
		addEdit();
	});*/
	//绑定单元格点击事件
//	$(".editBox").each(function() { // 取得所有class为editbox的对像
		//alert("单机");
		//$(this).bind("dbclick", function() { // 给其绑定单击事件
/*	$(".editBox").delegate("click", function() { 
			var objId = $(this).attr("id");
		//	$("span#" + objId).html("");
			editText = $(this).html().trim(); // 取得表格单元格的文本
			 console.log(editText);
			setEditHTML(editText); // 初始化控件
			$(this).data("oldtxt", editText) // 将单元格原文本保存在其缓存中，便修改失败或取消时用
			$(this).html(editHTML) // 改变单元格内容为编辑状态
			$(this).undelegate("click"); // 删除单元格单击事件，避免多次单击
			$("#editTd").focus();*/
			/*
			 * $("#editTd").focus(function(){
			 * if(objId=="SAMEFREQCELLCOEFWEIGHT"){ var
			 * samefreq=editText;
			 * console.log("samefreq"+samefreq);
			 * $("#SWITCHRATIOWEIGHT").text(1-Number(samefreq)); }
			 * if(objId=="SWITCHRATIOWEIGHT"){ var
			 * switchrat=editText;
			 * console.log("switchrat"+switchrat);
			 * $("#SAMEFREQCELLCOEFWEIGHT").text(1-Number(switchrat)); }
			 * });
			 */

//		});
//	});
	
	
	//更新记录
	$("#submitalter").bind("click",function(){
		//console.log(submitStatus);
		chooseTask();
	});
/*	//场景类型选择事件
	$("#sceneTypeList").bind("change",function(){
		sceneType=$("#sceneTypeList").val();
		getSceneNameListTask(sceneType);
	});*/
	//场景名选择事件
	$("#sceneNameList").live("change",function(){
		var sceneId=$("#sceneNameList").val();
		getSceneInfoTask(sceneId);
	});
	//删除记录
	$("#deleteScene").click(function(){
		var sceneId=$("#sceneNameList").val();
		if(submitStatus){
			var flag=confirm("确定要删除《"+$("#sceneNameList").find("option:selected").text()+"》的场景信息吗？");
			if(flag==false) return false;
		deleteSceneInfoTask(sceneId);
		}
	});
				
	//添加记录
	$("#addScene").bind("click",function(){
		initSceneTable();
	});

/*	$("#provincemenu").change(function() {
		getSubAreas("provincemenu", "citymenu", "市");
	});
	//切换区域时，赋值给uploadCityId
	$("#citymenu").change(function() {	
		$("#uploadCityId").val($("#citymenu").val());
	}); */
}

function chooseTask(){
	//console.log(submitStatus);
	if(submitStatus){
		submitUpdataData();
	}else{
	//	console.log(submitStatus);
		submitInsertData();
	}
}



/*//jquery ui 效果
function jqueryUiSet() {
	$("#progressbar").progressbar({
		value : 0
	});
	$("#tabs").tabs();
	
	$( "#accordion" ).accordion();
	
	$("#searchImportDiv").css("height","46px");
	$("#importDiv").css("height","180px");
	
	
	//--ncs上传记录---//
	//$("#provincemenu").selectmenu();
	//$("#citymenu").selectmenu();
	$("#importstatusmenu").selectmenu();
	$("#datepicker").datepicker({
		"dateFormat" : "yy-mm-dd"
	});
	$("#uploadQueryBegDate").datepicker(
			{
				dateFormat : "yy-mm-dd",
				defaultDate : "-2",
				changeMonth : true,
				numberOfMonths : 1,
				onClose : function(selectedDate) {
					$("#uploadQueryEndDate").datepicker("option", "minDate",
							selectedDate);
				}
			});
	$("#uploadQueryBegDate").datepicker("setDate", -2);// 减去2天

	$("#uploadQueryEndDate").datepicker(
			{
				dateFormat : "yy-mm-dd",
				defaultDate : "+1w",
				changeMonth : true,
				numberOfMonths : 1,
				onClose : function(selectedDate) {
					$("#uploadQueryBegDate").datepicker("option", "maxDate",
							selectedDate);
				}
			});
	$("#uploadQueryEndDate").datepicker("setDate",(new Date()));
	
	//---ncs记录查询----//
	$("#citymenu2").selectmenu();
	$("#provincemenu2").selectmenu({
		change: function( event, ui ) {
			getSubAreas("provincemenu2", "citymenu2", "市");
			//需要先初始化，再绑定
			$("#citymenu2").selectmenu("destroy");
			$("#citymenu2").selectmenu();
		}
	});
	$("#ncsMeaBegDate").datetimepicker(
			{
				dateFormat : "yy-mm-dd",
				timeFormat: "HH:mm:ss",
				defaultDate : "-2",
				changeMonth : true,
				numberOfMonths : 1,
				onClose : function(selectedDate) {
					$("#ncsMeaEndDate").datetimepicker("option", "minDate",
							selectedDate);
				}
			});
	$("#ncsMeaBegDate").datetimepicker("setDate",addDays(new Date(),-2));// 减去2天
	$("#ncsMeaEndDate").datetimepicker(
			{
				dateFormat : "yy-mm-dd",
				timeFormat: "HH:mm:ss",
				defaultDate :"+1w",
				changeMonth : true,
				numberOfMonths : 1,
				onClose : function(selectedDate) {
					$("#ncsMeaBegDate").datetimepicker("option", "maxDate",
							selectedDate);
				}
			});
	$("#ncsMeaEndDate").datetimepicker("setDate",(new Date()));
	//$("#searchImportBtn").button();
}*/

/*function allowAlterTable(){
	$(".sceneInfoInput").removeAttr('readOnly');
}*/
/**
 * 初始化场景名称列表
 */
function getSceneNameListTask(){
	$("#addScene").bind("click",function(){
		initSceneTable();
	});
	showOperTips("loadingDataDiv", "loadContentId", "正在查询");
    $.ajax({
		type:'post',
		url : 'getLteRegionSceneNameListForAjaxAction',
		dataType : 'text',
		success : function(raw) {
		//console.log(raw);
		fillSelectList("sceneNameTd","sceneNameList",raw);
		},
		complete : function() {
		var sceneId=$("#sceneNameList").val();	
		getSceneInfoTask(sceneId);
		hideOperTips("loadingDataDiv");
		}
    });
}
/**
 * 填充下拉框
 * @param selectParentId
 * @param selectId
 * @param raw
 */
function fillSelectList(selectParentId,selectId,raw){
	var selectParent=$("#"+selectParentId);
	//清空下拉框
	selectParent.empty();
	if(raw){
		var data = eval("(" + raw + ")");
			//console.log(data);
			if (data == null || data == undefined) {
				return;
			}
			sceneNames = data;
			var selectStr="<select 	name='cond[sceneNameList]' id='"+selectId+"'></select>";
			selectParent.append(selectStr);
			var select=$("#"+selectId);
			for ( var i = 0; i < data.length; i++) {
				//console.log(data.length);
				var str = "";
				var one = "";
				one = data[i];
				str="<option value='"+getValidValue(one['ID'], '')+"'>"+getValidValue(one['NAME'], '')+"</option>";
				select.append(str);
	}
}
}
/**
 * 根据场景ID取得场景详情
 * @param sceneId
 */
function getSceneInfoTask(sceneId){
	$("#scenceInfoTable caption font").html($("#sceneNameList").find("option:selected").text());
	clearAll();
	//console.log(sceneId);
	showOperTips("loadingDataDiv", "loadContentId", "正在查询");
	var data={
			'sceneId':sceneId
	};
    $.ajax({
		type:'post',
		url : 'getLteRegionSceneInfoListForAjaxAction',
		data:data,
		dataType : 'text',
		success : function(raw) {
		//	console.log(raw);
			showSceneInfo(raw);
		},
		complete : function() {
/*			doTableLock();*/
			addEdit();
			hideOperTips("loadingDataDiv");
		}
    });
}

/**
 * 显示场景信息
 * @param raw
 */
function showSceneInfo(raw){
	//console.log(raw);
	if (raw) {
		var data = eval("(" + raw + ")");
		//	console.log(data);
		if (data == null || data == undefined) {
			return;
			}
		//oldData=data;
		displaySceneinfo(data);
		}
}

/**
 * 呈现场景信息
 * @param tableId
 * @param raw
 */
function displaySceneinfo(data){
	//var table = $("#scenceInfoTable");
	//console.log(raw);
/*	var table = $("#"+tableId);*/
	// 只保留表头
/*	$("#"+tableId+" tr:not(:first)").each(function(i, ele) {
		$(ele).remove();
	});*/
	// 清空详情列表
/*	$("#scenceInfoTable tr:gt(0)").remove();*/
	//	var list = data['data'];
	
		$("span.oldDataTip").css({"display":"none"});
		$(".editbox").css({"width":"100%"});
			var one = data;
			$("#INTERRATHOA2THDRSRP").html(getValidValue(one['INTERRATHOA2THDRSRP']));
			$("#INTERRATHOA1THDRSRP").html(getValidValue(one['INTERRATHOA1THDRSRP']));
			$("#INTERRATHOUTRANB1HYST").html(getValidValue(one['INTERRATHOUTRANB1HYST']));
			$("#INTERRATHOUTRANB1THDRSCP").html(getValidValue(one['INTERRATHOUTRANB1THDRSCP']));
			$("#INTERRATHOA1A2TIMETOTRIG").html(getValidValue(one['INTERRATHOA1A2TIMETOTRIG']));
			$("#INTERRATHOA1A2HYST").html(getValidValue(one['INTERRATHOA1A2HYST']));
			$("#BLINDHOA1A2THDRSRP").html(getValidValue(one['BLINDHOA1A2THDRSRP']));
			$("#INTERFREQHOA1A2TIMETOTRIG").html(getValidValue(one['INTERFREQHOA1A2TIMETOTRIG']));
			$("#A3INTERFREQHOA1THDRSRP").html(getValidValue(one['A3INTERFREQHOA1THDRSRP']));
			$("#A3INTERFREQHOA2THDRSRP").html(getValidValue(one['A3INTERFREQHOA2THDRSRP']));
			$("#INTERFREQHOA3OFFSET").html(getValidValue(one['INTERFREQHOA3OFFSET']));
			$("#INTERFREQHOA1A2HYST").html(getValidValue(one['INTERFREQHOA1A2HYST']));
			$("#QRXLEVMIN").html(getValidValue(one['QRXLEVMIN']));
			$("#SNONINTRASEARCH").html(getValidValue(one['SNONINTRASEARCH']));
			$("#THRSHSERVLOW").html(getValidValue(one['THRSHSERVLOW']));			
			$("#TRESELEUTRAN").html(getValidValue(one['TRESELEUTRAN']));
			$("#CELLRESELPRIORITY").html(getValidValue(one['CELLRESELPRIORITY']));
			
			$("#INTERRATHOA2THDRSRP_old").html(getValidValue(one['INTERRATHOA2THDRSRP']));
			$("#INTERRATHOA1THDRSRP_old").html(getValidValue(one['INTERRATHOA1THDRSRP']));
			$("#INTERRATHOUTRANB1HYST_old").html(getValidValue(one['INTERRATHOUTRANB1HYST']));
			$("#INTERRATHOUTRANB1THDRSCP_old").html(getValidValue(one['INTERRATHOUTRANB1THDRSCP']));
			$("#INTERRATHOA1A2TIMETOTRIG_old").html(getValidValue(one['INTERRATHOA1A2TIMETOTRIG']));
			$("#INTERRATHOA1A2HYST_old").html(getValidValue(one['INTERRATHOA1A2HYST']));
			$("#BLINDHOA1A2THDRSRP_old").html(getValidValue(one['BLINDHOA1A2THDRSRP']));
			$("#INTERFREQHOA1A2TIMETOTRIG_old").html(getValidValue(one['INTERFREQHOA1A2TIMETOTRIG']));
			$("#A3INTERFREQHOA1THDRSRP_old").html(getValidValue(one['A3INTERFREQHOA1THDRSRP']));
			$("#A3INTERFREQHOA2THDRSRP_old").html(getValidValue(one['A3INTERFREQHOA2THDRSRP']));
			$("#INTERFREQHOA3OFFSET_old").html(getValidValue(one['INTERFREQHOA3OFFSET']));
			$("#INTERFREQHOA1A2HYST_old").html(getValidValue(one['INTERFREQHOA1A2HYST']));
			$("#QRXLEVMIN_old").html(getValidValue(one['QRXLEVMIN']));
			$("#SNONINTRASEARCH_old").html(getValidValue(one['SNONINTRASEARCH']));
			$("#THRSHSERVLOW_old").html(getValidValue(one['THRSHSERVLOW']));			
			$("#TRESELEUTRAN_old").html(getValidValue(one['TRESELEUTRAN']));
			$("#CELLRESELPRIORITY_old").html(getValidValue(one['CELLRESELPRIORITY']));
			
	}
//点击td转换可编辑
function setEditHTML(value) {
	editHTML = '<input id="editTd" type="text" maxlength="10" onBlur="ok(this)" value="'
			+ value + '" />';
	// editHTML += '<input type="button" onmousemove="ok(this)" value="修改" />';
	// editHTML += '<input type="button" onmousemove="cancel(this)" value="取消"
	// />';
}
/*
 * //取消 function cancel(cbtn){
 * 
 * var $obj = $(cbtn).parent(); //'取消'按钮的上一级，即单元格td
 * //console.log($obj.data("oldtxt")); $obj.html($obj.data("oldtxt"));
 * //将单元格内容设为原始数据，取消修改 $obj.bind("click",function(){ //重新绑定单元格双击事件 editText =
 * $(this).html().trim(); setEditHTML(editText);
 * $(this).data("oldtxt",editText).html(editHTML).unbind("click");
 * $("#editTd").focus(); }); }
 */
// 修改
function ok(obtn) {
	var $obj = $(obtn).parent(); //div
	//var objId = $obj.attr("id");  //div id 为场景ID
//	var tdClass = $(obtn).parent().parent().attr("class"); //TD class为场景的列名
	var spanvalue=$(obtn).parent().parent().find("span").html();
	//console.log("objId="+objId+",spanvalue="+spanvalue);
	var value = $obj.find("input:text")[0].value; // 取得文本框的值，即新数据
	if (value === "") {
		value = "   ";
	}
	if(value!=spanvalue){
		$(obtn).parent().parent().find("span.oldDataTip").css({"display":"inline-block","width":"49%"});
		$(obtn).parent().css({"width":"49%"});
	}else{
		$(obtn).parent().parent().find("span.oldDataTip").css({"display":"none"});
		$(obtn).parent().css({"width":"100%"});
		}
	// alert("success");
	$obj.data("oldtxt", value); // 设置此单元格缓存为新数据
	$obj.html($obj.data("oldtxt"));
/*	$obj.bind("click", function() { // 重新绑定单元格单击事件
		editText = $(this).html().trim();
		setEditHTML(editText);
		$(this).data("oldtxt", editText).html(editHTML).unbind("click");
		$("#editTd").focus();

	});*/
	$obj.attr("onclick","editThis(this)");
}
function editThis(obtn){
			//var objId = $(obtn).attr("id");
		//	$("span#" + objId).html("");
			editText = $(obtn).html().trim(); // 取得表格单元格的文本
			// console.log(editText);
			setEditHTML(editText); // 初始化控件
			$(obtn).data("oldtxt", editText);  // 将单元格原文本保存在其缓存中，便修改失败或取消时用
			$(obtn).html(editHTML); // 改变单元格内容为编辑状态
			$(obtn).removeAttr("onclick"); // 删除单元格单击事件，避免多次单击
			$("#editTd").focus();
}

function addEdit(){
	$("#scenceInfoTable div.editbox").attr("onclick","editThis(this)");
}

/*function doTableLock(){
	  $(function(){
	      $.fn.TableLock({table:'scenceInfoTable',lockRow:1,lockColumn:1,width:'100%',height:'300px'});
	  });
}
*/

function submitUpdataData(){
/*	var dataMap={};
	var flag=false;
	var trId="";
	var rows=$("#scenceInfoTable").find("tr");
//	console.log(rows.length);
	for(var r=1;r<rows.length;r++){
		var rowmap={};
		var tdClass="";
	var	tr =$("#scenceInfoTable").find("tr:eq("+r+")");
	var tds=tr.find("td");
//	console.log(tds.length);
	for(var d=1;d<tds.length;d++){
		var td=tr.find("td:eq("+d+")");
	//	console.log(td.find("span").attr("display"));
		if(td.find("span").css("display")=="inline-block"){
			flag=true;
	//		console.log(flag);
	//		console.log(td.find("span").css("display"));
			trId=td.find("div").attr("id");
	//		console.log(trId);
		}
		tdClass=td.attr("class");
//		console.log(tdClass);
		rowmap[tdClass]=td.find("div").html();
	//	console.log(td.find("div").html());
//		console.log("需要："+rowmap[tdClass]);
		}
	if(flag){
		dataMap[trId]=rowmap;
		flag=false;
	}
	}
	if(trId==""){
		console.log("diu");
	}else{
		updateSceneInfo(dataMap); */
		
//检查参数是否可以上传
	if(!checkSceneParam()){
		return
	}
	
	var INTERRATHOA2THDRSRP=$("#INTERRATHOA2THDRSRP").html().trim();
	var INTERRATHOA1THDRSRP=$("#INTERRATHOA1THDRSRP").html().trim();
	var INTERRATHOUTRANB1HYST=$("#INTERRATHOUTRANB1HYST").html().trim();
	var INTERRATHOUTRANB1THDRSCP=$("#INTERRATHOUTRANB1THDRSCP").html().trim();
	var INTERRATHOA1A2TIMETOTRIG=$("#INTERRATHOA1A2TIMETOTRIG").html().trim();
	var INTERRATHOA1A2HYST=$("#INTERRATHOA1A2HYST").html().trim();
	var BLINDHOA1A2THDRSRP=$("#BLINDHOA1A2THDRSRP").html().trim();
	var INTERFREQHOA1A2TIMETOTRIG=$("#INTERFREQHOA1A2TIMETOTRIG").html().trim();
	var A3INTERFREQHOA1THDRSRP=$("#A3INTERFREQHOA1THDRSRP").html().trim();
	var A3INTERFREQHOA2THDRSRP=$("#A3INTERFREQHOA2THDRSRP").html().trim();
	var INTERFREQHOA3OFFSET=$("#INTERFREQHOA3OFFSET").html().trim();
	var INTERFREQHOA1A2HYST=$("#INTERFREQHOA1A2HYST").html().trim();
	var QRXLEVMIN=$("#QRXLEVMIN").html().trim();
	var SNONINTRASEARCH=$("#SNONINTRASEARCH").html().trim();
	var THRSHSERVLOW=$("#THRSHSERVLOW").html().trim();
	var TRESELEUTRAN=$("#TRESELEUTRAN").html().trim();
	var CELLRESELPRIORITY=$("#CELLRESELPRIORITY").html().trim();
	
	var sceneDataMap={
			'sceneParam.sceneId':$("#sceneNameList").val(),
			'sceneParam.INTERRATHOA2THDRSRP':INTERRATHOA2THDRSRP,
			'sceneParam.INTERRATHOA1THDRSRP':INTERRATHOA1THDRSRP,
			'sceneParam.INTERRATHOUTRANB1HYST':INTERRATHOUTRANB1HYST,
			'sceneParam.INTERRATHOUTRANB1THDRSCP':INTERRATHOUTRANB1THDRSCP,
			'sceneParam.INTERRATHOA1A2TIMETOTRIG':INTERRATHOA1A2TIMETOTRIG,
			'sceneParam.INTERRATHOA1A2HYST':INTERRATHOA1A2HYST,
			'sceneParam.BLINDHOA1A2THDRSRP':BLINDHOA1A2THDRSRP,
			'sceneParam.INTERFREQHOA1A2TIMETOTRIG':INTERFREQHOA1A2TIMETOTRIG,
			'sceneParam.A3INTERFREQHOA1THDRSRP':A3INTERFREQHOA1THDRSRP,
			'sceneParam.A3INTERFREQHOA2THDRSRP':A3INTERFREQHOA2THDRSRP,
			'sceneParam.INTERFREQHOA3OFFSET':INTERFREQHOA3OFFSET,
			'sceneParam.INTERFREQHOA1A2HYST':INTERFREQHOA1A2HYST,
			'sceneParam.QRXLEVMIN':QRXLEVMIN,
			'sceneParam.SNONINTRASEARCH':SNONINTRASEARCH,
			'sceneParam.THRSHSERVLOW':THRSHSERVLOW,
			'sceneParam.TRESELEUTRAN':TRESELEUTRAN,
			'sceneParam.CELLRESELPRIORITY':CELLRESELPRIORITY,
			};
	updateSceneInfo(sceneDataMap);
	}
/**
 * 更新场景
 * @param sceneDataMap
 */
function updateSceneInfo(sceneDataMap){
	showOperTips("loadingDataDiv", "loadContentId", "正在更新");
    $.ajax({
		type:'post',
		url : 'updateLteRegionSceneDataForAjaxAction',
		data:sceneDataMap,
		dataType : 'text',
		success : function(raw) {
			if(raw){
				var status = raw;
				if(status=="success"){
				//var sceneType=$("#sceneTypeList").val();
				var sceneId=$("#sceneNameList").val();
				getSceneInfoTask(sceneId);
				}
				}
		},
		error:function(XMLResponse){
			alert(XMLResponse.responseText);
			},
		complete : function() {
			hideOperTips("loadingDataDiv");
		}
    });
}
/**
 * 删除场景记录
 */
function deleteSceneInfoTask(sceneId){
	deleteSceneInfo(sceneId);	
}
function deleteSceneInfo(sceneId){
	showOperTips("loadingDataDiv", "loadContentId", "正在删除");
	$.ajax({
		type:'post',
		url : 'deleteLteRegionSceneDataForAjaxAction',
		data:{
			'sceneId':sceneId,
		},
		dataType : 'text',
		success : function(raw) {
			if(raw){
				var status = raw;
				if(status=="success"){
				//var sceneType=$("#sceneTypeList").val();
				//var sceneId=$("#sceneNameList").val();
				//getSceneInfoTask(sceneId);
				getSceneNameListTask();
				}
				}
		},
		complete : function() {
/*			doTableLock();*/
			hideOperTips("loadingDataDiv");
		}
    });
}
/**
 * 初始化新增界面
 */
function initSceneTable(){
/*	var table=$("#sceneSelect");
	var str="";
	str="<tr>"+"<td colspan='4'><font style='color: red;font-weight: bold;'>请输入场景名<font></td>"+"</tr>";
	table.append(str);*/
	
	$(".oldDataTip").html("");
	$(".errTip").html("");
	$(".editbox").html("");
	$("#sceneNameTip").html("");
	$("#scenceInfoTable caption font").html("");

	var select=$("#sceneNameTd");
	select.empty();
	var input="";
	input="<input id='sceneNameInput' type='text' value='请输入场景名' onfocus='inputFocus(this)' onBlur='inputBlur(this)' />";
	select.append(input);
	$(".editbox").parent().find("span.oldDataTip").css({"display":"none"});
	$(".editbox").removeAttr("onclick"); 
	$(".editbox").attr("onclick","insertEdit(this)"); 
	$(".editbox").html(" ");
	submitStatus=false;
	//$("#sceneTypeList").unbind("change");
	$("#addScene").unbind("click");
}
function insertEdit(obtn){
	//var objId = $(obtn).attr("id");
//	$("span#" + objId).html("");
	editText = $(obtn).html().trim(); // 取得表格单元格的文本
	 //console.log(editText);
	setInsertHTML(editText); // 初始化控件
	$(obtn).data("oldtxt", editText);  // 将单元格原文本保存在其缓存中，便修改失败或取消时用
	$(obtn).html(editHTML); // 改变单元格内容为编辑状态
	$(obtn).removeAttr("onclick"); // 删除单元格单击事件，避免多次单击
	$("#inserteditTd").focus();
}
function setInsertHTML(value){
	editHTML = '<input id="inserteditTd" type="text" maxlength="10" onBlur="insertok(this)" value="'
		+ value + '" />';
}

function insertok(obtn) {
	var $obj = $(obtn).parent(); //div
	//var objId = $obj.attr("id");  //div id 为场景ID
	var value = $obj.find("input:text")[0].value; // 取得文本框的值，即新数据
	if (value === "") {
		value = "   ";
	}
	$obj.data("oldtxt", value); // 设置此单元格缓存为新数据
	$obj.html($obj.data("oldtxt"));
	$obj.attr("onclick","insertEdit(this)");
}
function submitInsertData(){
	
	//检查参数是否可以上传
	if(!checkSceneParam()){
		return
	} 
	
	var INTERRATHOA2THDRSRP=$("#INTERRATHOA2THDRSRP").html().trim();
	var INTERRATHOA1THDRSRP=$("#INTERRATHOA1THDRSRP").html().trim();
	var INTERRATHOUTRANB1HYST=$("#INTERRATHOUTRANB1HYST").html().trim();
	var INTERRATHOUTRANB1THDRSCP=$("#INTERRATHOUTRANB1THDRSCP").html().trim();
	var INTERRATHOA1A2TIMETOTRIG=$("#INTERRATHOA1A2TIMETOTRIG").html().trim();
	var INTERRATHOA1A2HYST=$("#INTERRATHOA1A2HYST").html().trim();
	var BLINDHOA1A2THDRSRP=$("#BLINDHOA1A2THDRSRP").html().trim();
	var INTERFREQHOA1A2TIMETOTRIG=$("#INTERFREQHOA1A2TIMETOTRIG").html().trim();
	var A3INTERFREQHOA1THDRSRP=$("#A3INTERFREQHOA1THDRSRP").html().trim();
	var A3INTERFREQHOA2THDRSRP=$("#A3INTERFREQHOA2THDRSRP").html().trim();
	var INTERFREQHOA3OFFSET=$("#INTERFREQHOA3OFFSET").html().trim();
	var INTERFREQHOA1A2HYST=$("#INTERFREQHOA1A2HYST").html().trim();
	var QRXLEVMIN=$("#QRXLEVMIN").html().trim();
	var SNONINTRASEARCH=$("#SNONINTRASEARCH").html().trim();
	var THRSHSERVLOW=$("#THRSHSERVLOW").html().trim();
	var TRESELEUTRAN=$("#TRESELEUTRAN").html().trim();
	var CELLRESELPRIORITY=$("#CELLRESELPRIORITY").html().trim();
	
	var sceneDataMap={
			'sceneParam.sceneName':$("#sceneNameInput").val().trim(),
			//'sceneParam.sceneType':$("#sceneTypeList").val(),
			'sceneParam.INTERRATHOA2THDRSRP':INTERRATHOA2THDRSRP,
			'sceneParam.INTERRATHOA1THDRSRP':INTERRATHOA1THDRSRP,
			'sceneParam.INTERRATHOUTRANB1HYST':INTERRATHOUTRANB1HYST,
			'sceneParam.INTERRATHOUTRANB1THDRSCP':INTERRATHOUTRANB1THDRSCP,
			'sceneParam.INTERRATHOA1A2TIMETOTRIG':INTERRATHOA1A2TIMETOTRIG,
			'sceneParam.INTERRATHOA1A2HYST':INTERRATHOA1A2HYST,
			'sceneParam.BLINDHOA1A2THDRSRP':BLINDHOA1A2THDRSRP,
			'sceneParam.INTERFREQHOA1A2TIMETOTRIG':INTERFREQHOA1A2TIMETOTRIG,
			'sceneParam.A3INTERFREQHOA1THDRSRP':A3INTERFREQHOA1THDRSRP,
			'sceneParam.A3INTERFREQHOA2THDRSRP':A3INTERFREQHOA2THDRSRP,
			'sceneParam.INTERFREQHOA3OFFSET':INTERFREQHOA3OFFSET,
			'sceneParam.INTERFREQHOA1A2HYST':INTERFREQHOA1A2HYST,
			'sceneParam.QRXLEVMIN':QRXLEVMIN,
			'sceneParam.SNONINTRASEARCH':SNONINTRASEARCH,
			'sceneParam.THRSHSERVLOW':THRSHSERVLOW,
			'sceneParam.TRESELEUTRAN':TRESELEUTRAN,
			'sceneParam.CELLRESELPRIORITY':CELLRESELPRIORITY,
			};
	insertSceneInfo(sceneDataMap);
}
function insertSceneInfo(sceneDataMap){
	showOperTips("loadingDataDiv", "loadContentId", "正在插入");
    $.ajax({
		type:'post',
		url : 'insertLteRegionSceneDataForAjaxAction',
		data:sceneDataMap,
		dataType : 'text',
		success : function(raw) {
			if(raw){
				var status = raw;
				if(status=="success"){
				//var sceneType=$("#sceneTypeList").val();
				//var sceneId=$("#sceneNameList").val();
				//getSceneInfoTask(sceneId);
				getSceneNameListTask();
				}else if(status=="success"){
					
				}
				}
		},
		complete : function() {
/*			doTableLock();*/
			hideOperTips("loadingDataDiv");
		}
    });
}

function checkSceneParam(){

	$("span.errTip").css({"display":"inline-block","width":"49%"});
	$(".editbox").css({"display":"inline-block","width":"49%"});
	clearErrTip();
	var INTERRATHOA2THDRSRP=$("#INTERRATHOA2THDRSRP").html().trim();
	var INTERRATHOA1THDRSRP=$("#INTERRATHOA1THDRSRP").html().trim();
	var INTERRATHOUTRANB1HYST=$("#INTERRATHOUTRANB1HYST").html().trim();
	var INTERRATHOUTRANB1THDRSCP=$("#INTERRATHOUTRANB1THDRSCP").html().trim();
	var INTERRATHOA1A2TIMETOTRIG=$("#INTERRATHOA1A2TIMETOTRIG").html().trim();
	var INTERRATHOA1A2HYST=$("#INTERRATHOA1A2HYST").html().trim();
	var BLINDHOA1A2THDRSRP=$("#BLINDHOA1A2THDRSRP").html().trim();
	var INTERFREQHOA1A2TIMETOTRIG=$("#INTERFREQHOA1A2TIMETOTRIG").html().trim();
	var A3INTERFREQHOA1THDRSRP=$("#A3INTERFREQHOA1THDRSRP").html().trim();
	var A3INTERFREQHOA2THDRSRP=$("#A3INTERFREQHOA2THDRSRP").html().trim();
	var INTERFREQHOA3OFFSET=$("#INTERFREQHOA3OFFSET").html().trim();
	var INTERFREQHOA1A2HYST=$("#INTERFREQHOA1A2HYST").html().trim();
	var QRXLEVMIN=$("#QRXLEVMIN").html().trim();
	var SNONINTRASEARCH=$("#SNONINTRASEARCH").html().trim();
	var THRSHSERVLOW=$("#THRSHSERVLOW").html().trim();
	var TRESELEUTRAN=$("#TRESELEUTRAN").html().trim();
	var CELLRESELPRIORITY=$("#CELLRESELPRIORITY").html().trim();

	var reg = /^[-+]?[0-9]+(\.[0-9]+)?$/; // 验证数字
	//var reg= /^\d+$/;
	//var reg1 = /^[0-9]*[1-9][0-9]*$/; // 正整数
	//var reg2 = /^(?:[1-9]?\d|100)$/; // 0-100的整数，适用于验证百分数
	//var reg3=/^([1-9]\d*|0)$/; //非负整数
	var flag = true;
	if(!submitStatus){
		var sceneName = $("#sceneNameInput").val().trim();
		$("#sceneNameTip").html("");
		if(sceneName=="请输入场景名"){
			$("#sceneNameTip").html("<font style='color: red;font-weight: bold;'>请输入场景名！<font>");
			flag=false;
		}else{
			for(var i = 0; i<sceneNames.length;i++){
				if(sceneNames[i].NAME==sceneName){
					$("#sceneNameTip").html("<font style='color: red;font-weight: bold;'>场景名已存在！<font>");
					flag=false;
				}
			}
		}
	}
	// console.log(SAMEFREQINTERTHRESHOLD + " "+ OVERSHOOTINGIDEALDISMULTIPLE);
	if (!reg.test(INTERRATHOA2THDRSRP)) {
		$("#INTERRATHOA2THDRSRP_err").html("※请输入数字※");
		flag = false;
/*	} else if (INTERRATHOA2THDRSRP > 1) {
		$("span#INTERRATHOA2THDRSRP_err").html("※值需要小于等于1※");
		flag = false;*/
	}else{
		$("#INTERRATHOA2THDRSRP_err").html("");
		$("#INTERRATHOA2THDRSRP_err").css({"display":"none"});
		$("#INTERRATHOA2THDRSRP").css({"width":"100%"});
	}
	if (!reg.test(INTERRATHOA1THDRSRP)) {
		$("#INTERRATHOA1THDRSRP_err").html("※请输入数字※");
		flag = false;
	}else{
		$("span#INTERRATHOA1THDRSRP_err").html("");
		$("#INTERRATHOA1THDRSRP_err").css({"display":"none"});
		$("#INTERRATHOA1THDRSRP").css({"width":"100%"});
	}
	if (!reg.test(INTERRATHOUTRANB1HYST)) {
		$("span#INTERRATHOUTRANB1HYST_err").html("※请输入数字※");
		flag = false;
	}else{
		$("span#INTERRATHOUTRANB1HYST_err").html("");
		$("#INTERRATHOUTRANB1HYST_err").css({"display":"none"});
		$("#INTERRATHOUTRANB1HYST").css({"width":"100%"});
	}
	if (!reg.test(INTERRATHOUTRANB1THDRSCP)) {
		$("span#INTERRATHOUTRANB1THDRSCP_err").html("※请输入数字※");
		flag = false;
	}else{
		$("span#INTERRATHOUTRANB1THDRSCP_err").html("");
		$("#INTERRATHOUTRANB1THDRSCP_err").css({"display":"none"});
		$("#INTERRATHOUTRANB1THDRSCP").css({"width":"100%"});
	}
	if (!reg.test(INTERRATHOA1A2TIMETOTRIG)) {
		$("span#INTERRATHOA1A2TIMETOTRIG_err").html("※请输入数字※");
		flag = false;
	}else{
		$("span#INTERRATHOA1A2TIMETOTRIG_err").html("");
		$("#INTERRATHOA1A2TIMETOTRIG_err").css({"display":"none"});
		$("#INTERRATHOA1A2TIMETOTRIG").css({"width":"100%"});
	}
	if (!reg.test(INTERRATHOA1A2HYST)) {
		$("span#INTERRATHOA1A2HYST_err").html("※请输入数字※");
		flag = false;
	}else{
		$("span#INTERRATHOA1A2HYST_err").html("");
		$("#INTERRATHOA1A2HYST_err").css({"display":"none"});
		$("#INTERRATHOA1A2HYST").css({"width":"100%"});
	}
	if (!reg.test(BLINDHOA1A2THDRSRP)) {
		$("span#BLINDHOA1A2THDRSRP_err").html("※请输入数字※");
		flag = false;
	}else{
		$("span#BLINDHOA1A2THDRSRP_err").html("");
		$("#BLINDHOA1A2THDRSRP_err").css({"display":"none"});
		$("#BLINDHOA1A2THDRSRP").css({"width":"100%"});
	}
	if (!reg.test(INTERFREQHOA1A2TIMETOTRIG)) {
		$("span#INTERFREQHOA1A2TIMETOTRIG_err").html("※请输入数字※");
		flag = false;
	}else{
		$("span#INTERFREQHOA1A2TIMETOTRIG_err").html("");
		$("#INTERFREQHOA1A2TIMETOTRIG_err").css({"display":"none"});
		$("#INTERFREQHOA1A2TIMETOTRIG").css({"width":"100%"});
	}
	if (!reg.test(A3INTERFREQHOA1THDRSRP)) {
		$("span#A3INTERFREQHOA1THDRSRP_err").html("※请输入数字※");
		flag = false;
	}else{
		$("span#A3INTERFREQHOA1THDRSRP_err").html("");
		$("#A3INTERFREQHOA1THDRSRP_err").css({"display":"none"});
		$("#A3INTERFREQHOA1THDRSRP").css({"width":"100%"});
	}
	if (!reg.test(A3INTERFREQHOA2THDRSRP)) {
		$("span#A3INTERFREQHOA2THDRSRP_err").html("※请输入数字※");
		flag = false;
	}else{
		$("span#A3INTERFREQHOA2THDRSRP_err").html("");
		$("#A3INTERFREQHOA2THDRSRP_err").css({"display":"none"});
		$("#A3INTERFREQHOA2THDRSRP").css({"width":"100%"});
	}
	if (!reg.test(INTERFREQHOA3OFFSET)) {
		$("span#INTERFREQHOA3OFFSET_err").html("※请输入数字※");
		flag = false;
	}else{
		$("span#INTERFREQHOA3OFFSET_err").html("");
		$("#INTERFREQHOA3OFFSET_err").css({"display":"none"});
		$("#INTERFREQHOA3OFFSET").css({"width":"100%"});
	}
	if (!reg.test(INTERFREQHOA1A2HYST)) {
		$("span#INTERFREQHOA1A2HYST_err").html("※请输入数字※");
		flag = false;
	}else{
		$("span#INTERFREQHOA1A2HYST_err").html("");
		$("#INTERFREQHOA1A2HYST_err").css({"display":"none"});
		$("#INTERFREQHOA1A2HYST").css({"width":"100%"});
	}
	if (!reg.test(QRXLEVMIN)) {
		$("span#QRXLEVMIN_err").html("※请输入数字※");
		flag = false;
	}else{
		$("span#QRXLEVMIN_err").html("");
		$("#QRXLEVMIN_err").css({"display":"none"});
		$("#QRXLEVMIN").css({"width":"100%"});
	}
	if (!reg.test(SNONINTRASEARCH)) {
		$("span#SNONINTRASEARCH_err").html("※请输入数字※");
		flag = false;
	}else{
		$("span#SNONINTRASEARCH_err").html("");
		$("#SNONINTRASEARCH_err").css({"display":"none"});
		$("#SNONINTRASEARCH").css({"width":"100%"});
	}
	if (!reg.test(THRSHSERVLOW)) {
		$("span#THRSHSERVLOW_err").html("※请输入数字※");
		flag = false;
	}else{
		$("span#THRSHSERVLOW_err").html("");
		$("#THRSHSERVLOW_err").css({"display":"none"});
		$("#THRSHSERVLOW").css({"width":"100%"});
	}
	if (!reg.test(TRESELEUTRAN)) {
		$("span#TRESELEUTRAN_err").html("※请输入数字※");
		flag = false;
	}else{
		$("span#TRESELEUTRAN_err").html("");
		$("#TRESELEUTRAN_err").css({"display":"none"});
		$("#TRESELEUTRAN").css({"width":"100%"});
	}
	if (!reg.test(CELLRESELPRIORITY)) {
		$("span#CELLRESELPRIORITY_err").html("※请输入数字※");
		flag = false;
	}else{
		$("span#CELLRESELPRIORITY_err").html("");
		$("#CELLRESELPRIORITY_err").css({"display":"none"});
		$("#CELLRESELPRIORITY").css({"width":"100%"});
	}
	if(!flag)alert("请按提示输入");
	return flag;
	}
function clearErrTip(){
	$(".errTip").html("");
}
function clearOldDataTip(){
	$(".oldDataTip").html("");
}
function inputFocus(obtn){
	if($(obtn).val()=="请输入场景名"){
		$(obtn).val("");		
	}
}
function inputBlur(obtn){
	if($(obtn).val().trim()==""){
		$(obtn).val("请输入场景名");
	}
}
function clearAll(){
	submitStatus=true;
	$(".oldDataTip").html("");
	$(".errTip").html("");
	$(".editbox").html("");
	$("#sceneNameTip").html("");
}