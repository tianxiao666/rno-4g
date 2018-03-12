$(document).ready(function(){
	
	$("#formTemplate").validate({
		messages:{
			"reportTemplate.reportName":"请填写模板名称"
		},
		submitHandler : function(form) {
			savereporttemplate();
		}
	});
	
	$(".dispNameCls").on("click", function(e) {
		var inp = $(this).siblings("input:first");
		//alert(inp.val());
		//赋值 
		$("#curFieldName").html($(this).html());
		$("#hiddenForDisplayName").val($(this).html());
		//
		$("#hiddenDbField").val(inp.val());
	});
	$("#searchRptTempBtn").click(function(){
		//ReportTemplateDataForm
		//重新查询的时候要删除详情信息并保留标题头
		$("#RptTemplateDetailTab tr:not(:first)").each(function(i, e) {
			e.remove();
		});
		getReportTemplateData("queryReportTemplateListByPageForAjaxAction","queryReportTemplateDataTab", "ReportTemplateDataForm", "ReportTemplateDataPageDiv");
	});
	/*$("#formTemplate").submit(function(){
		
	});*/
	$("#div_tab ul li").eq(1).click(function() {
				$("#searchRptTempBtn").click();
			});
});

//增加一个表头
function addOneField() {
	var dbField = $("#hiddenDbField").val();
	var disName = $("#hiddenForDisplayName").val();

	if ($.trim(dbField) == "") {
		alert("请先选择一个表头");
		return;
	}

	if ($.trim(disName) == "") {
		alert("请输入表头在报表中显示的名称");
		return;
	}

	var cnt = reculcateSeq();
	cnt++;
	var field=$("#curFieldName").html();
	var tr = "<tr class='contentTr'><td class='seqTd'>"
			+ cnt
			+ "</td><td>"
			+ field
			+ "</td><td>"
			+ disName
			+ "</td><td>"
			+" <input type='hidden' class='orderCls' name='rptDetail["+(cnt-1)+"].displayOrder'  value='"+cnt+"' />"
			+" <input type='hidden' class='dbfieldCls' name='rptDetail["+(cnt-1)+"].tableFields'  value='"+dbField+"' />"
			+"<input type='hidden' class='dispCls' name='rptDetail["+(cnt-1)+"].displayName' value='"+disName+"' />"
			+" <input type='button' value='up' onclick='moveTitle(this,\"up\")'/>"
			+" <input type='button' value='down' onclick='moveTitle(this,\"down\")'/>"
			+"<input type='button' value='删除' onclick='deleteOneTitle(this);'/>"
			+"</td></tr>";
	$(tr).insertBefore($("#btnTr"));
}

//重新编号
function reculcateSeq() {
	var trs = $("#repTempTab").find("tr.contentTr");
	var cnt = trs.length;
	for ( var i = 0; i < cnt; i++) {
		$(trs[i]).find("td.seqTd").html(i + 1);
		//
		$(trs[i]).find("input.orderCls").attr("name","rptDetail["+(i)+"].displayOrder");
		$(trs[i]).find("input.orderCls").val(i);
		$(trs[i]).find("input.dbfieldCls").attr("name","rptDetail["+(i)+"].tableFields");
		$(trs[i]).find("input.dispCls").attr("name","rptDetail["+(i)+"].displayName");
	}
	return cnt;
}

//删除某表头
function deleteOneTitle(obj) {
	var tr = $(obj).closest("tr");
	if (tr) {
		tr.remove();
		reculcateSeq();
	}
}

//向上调整
function moveTitle(obj,dir){
	if(dir!='up' && dir!='down'){
		return;
	}
	var tr = $(obj).closest("tr");
	if(!tr){
		return;
	}
	var seq=tr.find("td.seqTd").html();
	//alert("seq="+seq);
	if(dir=='up' && seq=="1"){
		return;
	}
	
	if(dir=='down' && seq==cnt){
		return;
	}
	seq=new Number(seq);
	//寻找上一个tr
	var trs = $("#repTempTab").find("tr.contentTr");
	var cnt = trs.length;
	var upTr=null;
	for ( var i = 0; i < cnt; i++) {
		var needSeq=new Number($(trs[i]).find("td.seqTd").html());
		//alert("needSeq="+needSeq);
		if(dir=='up' && needSeq==seq-1
			|| dir=='down' && needSeq==seq+1){
			//alert("find uptr");
			upTr=$(trs[i]);
			break;
		}
	}
	if(!upTr){
		return;
	}
	
	//开始调整
	tr.remove();
	if(dir=='up'){
	tr.insertBefore(upTr);
	}else{
		tr.insertAfter(upTr);
	}
	//调整编号
	reculcateSeq();
}
/**
 * 获取报表模板数据
 * @param {} url
 * @param {} tabId
 * @param {} formId
 * @param {} pageDivId
 */
function getReportTemplateData(url,tabId, formId, pageDivId){
	
	$(".loading_cover").css("display", "block");
	var reportName=$("#reportName").val();
	var displayName=$("#displayName").val();
	/*console.log("reportName:"+reportName);
	console.log("displayName:"+displayName);
	console.log("(reportName.trim():"+reportName.trim());
	console.log("displayName.trim():"+displayName.trim());*/
	$("#hiddenRptName").val(reportName.trim());
	$("#hiddenDispName").val(displayName.trim());
	$("#" + formId).ajaxSubmit({
		url : url,
		dataType : 'text',
		success : function(data) {
//			console.log(data);
			///alert("查回来了！");
			showReportTempResult(data, tabId, formId, pageDivId);
		},
		complete : function() {
			$(".loading_cover").css("display", "none");
		}
	});

}
/**
 * 显示报表模板数据结果
 * @param {} data
 * @param {} tabId
 * @param {} formId
 * @param {} pageDivId
 */
function showReportTempResult(data, tabId, formId, pageDivId) {

	//alert("显示查回来的结果！");
	//var btntype = $("#btnFlag").val();
	data = eval("(" + data + ")");
//	 console.log("data===" + data);
	// 准备填充ncs
	var page = data['page'];
	var rpttemplist = data['data'];
//	console.log("rpttemplist:"+rpttemplist);
		 if (page) {
	 var pageSize = page['pageSize'] ? page['pageSize'] : 0;
	 $("#hiddenPageSize").val(pageSize);
	
	 var currentPage = page['currentPage'] ? page['currentPage'] : 1;
	 $("#hiddenCurrentPage").val(currentPage);
	
	 var totalPageCnt = page['totalPageCnt'] ? page['totalPageCnt'] : 0;
	 $("#hiddenTotalPageCnt").val(totalPageCnt);
	
	 var totalCnt = page['totalCnt'] ? page['totalCnt'] : 0;
	 $("#hiddenTotalCnt").val(totalCnt);
	
	 // 跳转
	 $("#emTotalCnt").html(totalCnt);
	 $("#showCurrentPage").val(currentPage);
	 $("#emTotalPageCnt").html(totalPageCnt);
	
	 }
	// 设置form的分页信息
//	setFormPageInfo(formId, page);

	// 设置分页控件
//	setPageView(page, pageDivId);

	// 用于显示结果的table
	var table = $("#" + tabId);
	// 只保留表头
	// table.append(getHead());
	$("#" + tabId + " tr:not(:first)").each(function(i, ele) {
		$(ele).remove();
	});
	// console.log("celllist====" + celllist);
	if (rpttemplist) {
		var one;
		var tr;
		for ( var i = 0; i < rpttemplist.length; i++) {
//			 console.log("i==" + i);
			// CELL 小区中文名 LAC CI ARFCN BSIC TCH 操作
			one = rpttemplist[i];
			if (!one) {
				//console.log("one is null!");
				continue;
			}
				tr += "<tr class=\"greystyle-standard-whitetr\"  align=\"center\">";
				tr += "<td>" + getValidValue(one['reportName']) + "</td>";
				tr += "<td>" + getValidValue(one['tableName']) + "</td>";
				tr += "<td>" + getValidValue(one['creator']) + "</td>";
				tr += "<td>" + getValidValue(one['createTime']) + "</td>";
				tr += "<td>" + getValidValue(one['modTime']) + "</td>";
				/*tr += "<td>" + getValidValue(one['status']) + "</td>";
				tr += "<td>" + getValidValue(one['applyScope']) + "</td>";
				tr += "<td>" + getValidValue(one['scopeValue']) + "</td>";*/
				
				  tr += "<td><span style='display:block;white-space:nowrap'> <input type='button' id='" +
				  getValidValue(one['id']) + "' class='forcheck' value='查看' onclick='viewRptTemplateDetail(this,"+getValidValue(one['id'])+")'/>" +
				  		" <input type='button' id='" +
				  getValidValue(one['id']) + "' class='forcheck' value='删除' onclick='return rmvRptTemplateAndDetail(this,"+getValidValue(one['id'])+")'/></span></td>";
				 
				tr += "</tr>";
			}
			
			
//			 console.log("tr===" + tr);
			table.append(tr);// 增加
		}
	}
/**
 * 查看报表模板下的详情信息
 * @param {} obj
 * @param {} tempId
 */
function viewRptTemplateDetail(obj,tempId){
	//queryRptTemplateDetailListByRptTempIdForAjaxAction
		$(".loading_cover").css("display", "block");
//	var td=$(obj).closest("td");
		$("#RptTemplateDetailTab tr:not(:first)").each(function(i, ele) {
		$(ele).remove();
	});
	$.ajax ({
		url : 'queryRptTemplateDetailListByRptTempIdForAjaxAction',
		data:{'rptTemplateId':tempId},
		type:'post',
		dataType : 'json',
		success : function(rawdata) {
		
//			console.log("详情："+rawdata);
			var data="";
			var tr="";
			try{
//			    data=eval("("+rawdata+")");
			    data=rawdata;
//				console.log(data);
			for(var i=0;i<data.length;i++){
					one=data[i];
					if(!one){
						continue;
					}
					
					tr+="<tr align=\"center\">"
										   +"<td>"+getValidValue(one['tableFields'])+"</td>" 
										    +"<td>"+getValidValue(one['displayName'])+"</td>"
										    +"<td>"+getValidValue(one['displayOrder'])+"</td>"
										  +"</tr>";
					}
//			console.log("cellinterstr:"+cellinterstr);						  
				$("#RptTemplateDetailTab").append(tr);
			}catch(err){
				//console.log(err);
			}
			
		},
		complete : function() {
			$(".loading_cover").css("display", "none");
		}
	});
}
/**
 * 删除某报表模板及所归属的详情信息
 * @param {} obj
 * @param {} tempId
 */
function rmvRptTemplateAndDetail(obj,tempId){
	var flag=confirm("确定将此记录删除："+tempId);
//	console.log(flag);
	if(!flag){
		return false;
	}
		$(".loading_cover").css("display", "block");
//	var td=$(obj).closest("td");
		//console.log("tempId:"+tempId);
	$.ajax ({
		url : 'rmvRptTemplateAndDetailListByRptTempIdForAjaxAction',
		data:{'rptTemplateId':tempId},
		type:'post',
		dataType : 'json',
		success : function(data) {
//		 	console.log("data:"+data);
			if(data){
				animateInAndOut("operInfo", 500, 500, 1000, "operTip",
							"删除成功："+tempId);
			}else{
				animateInAndOut("operInfo", 500, 500, 1000, "operTip",
							"删除失败："+tempId);
			}
			
			
		},
		complete : function() {
			$("#searchRptTempBtn").click();
			$(".loading_cover").css("display", "none");
		}
	});
}
/**
 * 调用报表模板记录数据函数
 */
function getReportTemplateRecordData(){
	getReportTemplateData("queryReportTemplateListByPageForAjaxAction","queryReportTemplateDataTab", "ReportTemplateDataForm", "ReportTemplateDataPageDiv");
}
/**
 * 分页跳转的响应
 * @param dir
 * @param action
 * @param formId
 */
function showListViewByPage(dir,action,formId) {
	
	var form=$("#"+formId);
	
	//alert(form.find("#hiddenPageSize").val());
	var pageSize =new Number(form.find("#hiddenPageSize").val());
	var currentPage = new Number(form.find("#hiddenCurrentPage").val());
	var totalPageCnt =new Number(form.find("#hiddenTotalPageCnt").val());
	var totalCnt = new Number(form.find("#hiddenTotalCnt").val());

	//console.log("pagesize="+pageSize+",currentPage="+currentPage+",totalPageCnt="+totalPageCnt+",totalCnt="+totalCnt);
	if (dir === "first") {
		if (currentPage <= 1) {
			return;
		} else {
			$(form).find("#hiddenCurrentPage").val("1");
		}
	} else if (dir === "last") {
		if (currentPage >= totalPageCnt) {
			return;
		} else {
			$(form).find("#hiddenCurrentPage").val(totalPageCnt);
		}
	} else if (dir === "back") {
		if (currentPage <= 1) {
			return;
		} else {
			$(form).find("#hiddenCurrentPage").val(currentPage - 1);
		}
	} else if (dir === "next") {
		if (currentPage >= totalPageCnt) {
			return;
		} else {
			$(form).find("#hiddenCurrentPage").val(currentPage + 1);
		}
	} else if (dir === "num") {
		var userinput = $("#showCurrentPage").val();
		if (isNaN(userinput)) {
			alert("请输入数字！")
			return;
		}
		if (userinput > totalPageCnt || userinput < 1) {
			alert("输入页面范围不在范围内！");
			return;
		}
		$(form).find("#hiddenCurrentPage").val(userinput);
	}else{
		return;
	}
	//获取资源
	if(typeof action =="function"){
		action();
	}
}
/**
 * 保存报告模板
 */
function savereporttemplate(){
	$(".loading_cover").css("display", "block");
	$("#formTemplate").ajaxSubmit(
			{
				url : 'saveReportTemplateForAjaxAction',
				dataType : 'json',
				success : function(data) {
					var flag=data.flag;
					var msg=data.msg;
					if(flag){
						animateInAndOut("operInfo", 500, 500, 1000, "operTip",
							msg);
					}else{
						animateInAndOut("operInfo", 500, 500, 1000, "operTip",
							msg);
					}
					
				},
				error : function(xmh, textstatus, e) {
					alert("出错啦！" + textstatus+e);
				},
				complete : function() {
					$(".loading_cover").css("display", "none");
				}
			});
}
/**
 * 删除左右两端的空格:向string类中注入trim方法
 * @return {}
 */
String.prototype.trim=function(){
    return this.replace(/(^\s*)|(\s*$)/g, "");
 }
