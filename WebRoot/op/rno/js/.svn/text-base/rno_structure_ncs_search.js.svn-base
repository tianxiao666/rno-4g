var clusterDataVisit_1 = false;// 是否获取过“所有连通簇列表暑假”数据
var clusterDataVisit_2 = false;// 是否获取过“最大连通簇列表”数据
var clusterDataVisit_3 = false;// 是否获取过“簇相关小区列表”数据
var clusterDataVisit_4 = false;// 是否获取过“自动优化建议列表”数据

var isSingleNcs = false;// 是否分析单个ncs
var token = null;// 任务token，如果是单个ncs的话，则是ncsId

var cityId, cityName, areaId, areaName;

$(document).ready(function() {

	initAreaCascadeNcs();

	// ncs记录查询
	$("#searchNcsDescBtn").click(function() {
		initFormPage("ncsDescDataForm");
		doQueryNcsDesc();
	});

	// ncs汇总任务查询
	$("#searchTaskBtn").click(function() {
		initFormPage("ncsTaskDataForm");
		doQueryNcsTask();
	});
	$("#selAllNcsCb").change(function() {
		// alert($("#selAllNcsCb").attr("checked"));
		if ($("#selAllNcsCb").attr("checked") == "checked") {
			$(".forcheck").attr("checked", "checked");
		} else {
			$(".forcheck").removeAttr("checked");
		}
	});

	// 点击“所有连通簇列表”li的响应
	$("#allClusterLi").click(function() {
		if (clusterDataVisit_1 == true) {
			return;
		}
		clusterDataVisit_1 = true;
		if (isSingleNcs == true) {
			getSingleClusterAllData(token)
		} else {
			getAndShowReport(token, 'cluster', 'ncsClusterDataTab1');
		}
	});
	// 点击“最大连通簇列表”li的响应
	$("#maxClusterLi").click(function() {
		if (clusterDataVisit_2 == true) {
			return;
		}
		clusterDataVisit_2 = true;
		if (isSingleNcs == true) {
			getSingleMaxClusterCellAllData(token);
		} else {
			getAndShowReport(token, 'clustercell', 'ncsClusterDataTab2');
		}
	});
	// 点击“簇相关小区列表”li的响应
	$("#clusterCellLi").click(function() {
		if (clusterDataVisit_3 == true) {
			return;
		}
		clusterDataVisit_3 = true;
		if (isSingleNcs == true) {
			getSingleMaxClusterCellInterAllData(token);
		} else {
			getAndShowReport(token, 'clustercellrela', 'ncsClusterDataTab3');
		}
	});

	// 簇指标分析结果
	$("#clusterDataLi").click(function() {
		$("#cluster_tab").find("li").each(function(i, ele) {
			if ($(ele).hasClass("selected")) {
				$(ele).trigger("click");
			}
		});
	});
	
	//自动优化建议
	$("#optSuggestionLi").click(function() {
		if (clusterDataVisit_4 == true) {
			return;
		}
		clusterDataVisit_4 = true;
		getAndShowReport(token, 'optsuggestion', 'ncsOptSuggestionTab');
		
	});
	
	$("#rmvSelectedItems").click(function() {
		if($("#queryNcsResultTab td").length==0){
				animateInAndOut("operInfo", 500, 500, 1000, "operTip",
								"请先进行查询操作,再选择复选框，然后点击该删除按钮。");
		}else{
		//$(".forcheck")
			var ncsIds='';
			$("#queryNcsResultTab input:not(:first)").each(function(i,e){
				if($(e).attr("checked")=='checked'){
					var id=$(e).attr("id");
					//console.log("id:"+id);
					if(!isNaN(id)){
					ncsIds+=','+id;
					}
				}
			});
			if(''==ncsIds){
				animateInAndOut("operInfo", 500, 500, 1000, "operTip",
								"请先选择复选框，然后点击该删除按钮。");
			}else{
				ncsIds=ncsIds.substring(1);
				if(confirm("你确定要删除选中项吗?")){
					//删除选择项操作
					deleteNcsSelectedRec(ncsIds);
				}else{
					animateInAndOut("operInfo", 500, 500, 1000, "operTip",
								"取消删除操作。");
				}
			}
		}
		
	});
	$(document).bind("click",function(e){
	
		var target  = $(e.target);
		if(target.attr('id')=='fadelayer'){
			$("#fadelayer").remove();
			$("#light").hide( "slow", function(){
			$("#light").remove();
			//$("#ncsprompt").attr("disabled",null);
			})
		}
		
	});
});

// 执行查询ncs
function doQueryNcsDesc() {
	// 记录查询的区域级别
	cityId = $("#ncs_cityId2").val();
	cityName = $.trim($("#ncs_cityId2").find("option:selected").text());

	areaId = $("#ncs_areaId2").val();
	areaName = $.trim($("#ncs_areaId2").find("option:selected").text());

	$("#ncsDescDataForm").ajaxSubmit({
		url : 'queryNcsDescpByPageForAjaxAction',
		dataType : 'text',
		success : function(data) {
			showNcsQueryResult(data);
		},
		complete : function() {
			$(".loading_cover").css("display", "none");
		}
	})
}

/**
 * 执行查询ncs汇总任务
 */
function doQueryNcsTask() {
	$("#ncsTaskDataForm").ajaxSubmit({
		url : 'queryNcsAnalysisTaskForAjaxAction',
		dataType : 'text',
		success : function(data) {
			showNcsTaskQueryResult(data);
		},
		complete : function() {
			$(".loading_cover").css("display", "none");
		}
	})
}

/**
 * 显示分页查询回来的ncs汇总任务
 * 
 * @param data
 */
function showNcsTaskQueryResult(rawdata) {

	var table = $("#queryTaskResultTab");

	$("#queryTaskResultTab tr:not(:first)").each(function(i, ele) {
		$(ele).remove();
	});

	var data = eval("(" + rawdata + ")");
	var html = "";
	var list = data['data'];
	var one = null;
	for ( var i = 0; i < list.length; i++) {
		one = list[i];
		if (!one) {
			continue;
		}
		html += "<tr>";
		html += "<td>" + getValidValue(one['DATA_LEVEL'], '') + "</td>";
		html += "<td>" + getValidValue(one['LEVEL_NAME'], '') + "</td>";
		html += "<td>" + getValidValue(one['CNT'], '') + "</td>";
		html += "<td>" + getValidValue(one['START_TIME'], '') + "</td>";
		html += "<td>" + getValidValue(one['COMPLETE_TIME'], '') + "</td>";
		html += "<td>" + getValidValue(one['TASK_GOING_STATUS'], '') + "</td>";
		if (one['TASK_GOING_STATUS'] == '成功完成') {
			html += "<td><input class='exportNcsTaskBtnCls' onclick='downloadreport(\""
					+ one['TASK_ID']
					+ "\")' type='button' value='导出excel分析报告' data='"
					+ one['TASK_ID']
					+ "'/>"
					+ " <input type='button' class='viewNcsTaskBtnCls' onclick='viewreport(\""
					+ one['TASK_ID'] + "\")' value='查看报告'/></td>";
		} else if (one['TASK_GOING_STATUS'] == '失败') {
			html += "<td>" + one['RESULT'] + "</td>";
		} else {
			html += "<td></td>";
		}
		html += "</tr>";
	}

	table.append($(html));

	// 设置隐藏的page信息
	setFormPageInfo("ncsTaskDataForm", data['page']);

	// 设置分页面板
	setPageView(data['page'], "ncsTaskResultPageDiv");
}

/**
 * 下载指定的分析任务的报告
 * 
 * @param taskId
 */
function downloadreport(taskId) {
	var form = $("#getReportForm");
	form.find("input#taskId").val(taskId);
	form.submit();
}

/**
 * 获取报告的某部分内容，渲染到指定的table上
 */
function getAndShowReport(taskId, rptDataType, tableId, callback) {
	showOperTips("loadingDiv", "tipcontentId", "正在获取报告信息....");
	var sg=0;
	if(isSingleNcs==true){
		sg=1;
	}
	$.ajax({
		url : 'getNcsReportDataForAjaxAction',
		data : {
			'taskId' : taskId,
			'dataType' : rptDataType,
			'singleNcs':sg
		},
		dataType : 'text',
		type : 'post',
		success : function(raw) {
			var data = null;
			var html = "";
			try {
				data = eval("(" + raw + ")");
				// var one=data[0];
				// html+="<tr><th>编号</th>";
				// for(var i=0;i<one.length;i++){
				// html+="<th>"+one[i]+"</th>";
				// }
				// html+="</tr>";//标题
				// for(var i=1;i<data.length;i++){
				// one=data[i];
				// html+="<tr><td>"+(i)+"</td>";
				// for(var j=0;j<one.length;j++){
				// html+="<td>"+getValidValue(one[j],'',5)+"</td>";
				// }
				// html+="</tr>";
				// }
				// $("#"+tableId).html(html);
				if (rptDataType == "cellres") {
					showStructData(data);// 显示结构指标
				} else if (rptDataType == "cluster") {
					showClusterData(data);// 显示结构指标
				} else if (rptDataType == "clustercell") {
					showClusterCell(data);// 显示结构指标
				} else if (rptDataType == "clustercellrela") {
					showClusterCellRela(data);// 显示结构指标
				}else if(rptDataType=='optsuggestion'){
					showOptSuggestion(data);
				}
			} catch (err) {

			}

		},
		complete : function() {
			if (typeof callback == 'function') {
				callback();
			}
			hideOperTips("loadingDiv");
		}
	});
}

/**
 * 显示自动优化结果
 * @param data
 */
function showOptSuggestion(data){
	
	$("#ncsOptSuggestionTab tr:not(:first)").each(function(i, ele) {
		$(ele).remove();
	});
	
	var html = "";
	if (!data || data.length == 0) {
		return;
	}
	
	var one;
	for ( var i = 0; i < data.length; i++) {
		one = data[i];
		html += "<tr><td>" + (i + 1) + "</td>";
		html+="<td>"+getValidValue(one['CELL'],'')+"</td>";
		html+="<td>"+getValidValue(one['CELLNAME'],'')+"</td>";
		html+="<td>"+getValidValue(one['EXPECTED_COVER_DIS'],'',5)+"</td>";
		html+="<td>"+getValidValue(one['OVERSHOOTING_FACT'],'',5)+"</td>";
		html+="<td>"+getValidValue(one['INTERFER_NCELL_CNT'],'')+"</td>";
		html+="<td>"+getValidValue(one['INTERFER_NCE_CNT_EXINDR'],'')+"</td>";
		html+="<td>"+getValidValue(one['CELL_COVER'],'',5)+"</td>";
		html+="<td>"+getValidValue(one['CAPACITY_DESTROY'],'',5)+"</td>";
		html+="<td>"+getValidValue(one['LONGITUDE'],'',5)+"</td>";
		html+="<td>"+getValidValue(one['LATITUDE'],'',5)+"</td>";
		html+="<td>"+getValidValue(one['ANT_HEIGHT'],'',5)+"</td>";
		html+="<td>"+getValidValue(one['DOWNTILT'],'',5)+"</td>";
		html+="<td>"+getValidValue(one['IDEAL_DOWNTILT'],'',5)+"</td>";
		html+="<td>"+getValidValue(one['INDOOR_CELL_TYPE'],'',5)+"</td>";
		html+="<td>"+getValidValue(one['DECLARE_CHANNEL_NUMBER'],'')+"</td>";
		html+="<td>"+getValidValue(one['AVAILABLE_CHANNEL_NUMBER'],'')+"</td>";
		html+="<td>"+getValidValue(one['CARRIER_NUMBER'],'')+"</td>";
		html+="<td>"+getValidValue(one['RESOURCE_USE_RATE'],'',5)+"</td>";
		html+="<td>"+getValidValue(one['TRAFFIC'],'',5)+"</td>";
		html+="<td>"+getValidValue(one['PROBLEM_DESC'],'')+"</td>";
		html+="<td>"+getValidValue(one['MESSAGE'],'')+"</td>";
		html+="<td>"+getValidValue(one['STS_TIME'],'')+"</td>";

		html += "</tr>";
	}
	$("#ncsOptSuggestionTab").append(html);
}

/**
 * 页面上显示任务的报告数据
 * 
 * @param taskId
 */
function viewreport(taskId) {
	// 结构指标分析结果
	clearOldData();
	token = taskId;
	isSingleNcs = false;
	getAndShowReport(taskId, 'cellres', 'ncsStructDataTab', function() {
		// 触发获取连通簇的方法
		$("#allClusterLi").trigger("click");
	});

	// 触发选中“小区结构指标”
	$("#structDataLi").trigger("click");
}

function initAreaCascadeNcs() {

	$("#ncs_areaId2").append("<option selected='true' value=''>全部</option>");

	$("#ncs_provinceId2").change(function() {
		getSubAreas("ncs_provinceId2", "ncs_cityId2", "市");
	});

	$("#ncs_cityId2").change(
			function() {
				getSubAreas("ncs_cityId2", "ncs_areaId2", "区/县", function() {
					$("#ncs_areaId2").append(
							"<option selected='true' value=''>全部</option>");
				});

			});
}

// 初始化form下的page信息
function initFormPage(formId) {
	var form = $("#" + formId);
	if (!form) {
		return;
	}
	// form.find("#hiddenPageSize").val(25);
	form.find("#hiddenCurrentPage").val(1);
	form.find("#hiddenTotalPageCnt").val(-1);
	form.find("#hiddenTotalCnt").val(-1);
}

/*
 * 显示查询的ncs记录信息 data:包含page信息和查询回来的数据信息
 */
function showNcsQueryResult(rawdata) {
	// table id ：queryNcsResultTab

	$("#selAllNcsCb").removeAttr("checked");
	var data = eval("(" + rawdata + ")");

	var table = $("#queryNcsResultTab");

	// id
	var id = 'RNO_NCS_DESC_ID';

	// 忽略的子段
	var obmit = new Array();
	obmit.push("INTERFER_MATRIX_ID");
	obmit.push("AREA_ID");
	obmit.push("RECORD_COUNT");
	obmit.push("NET_TYPE");
	obmit.push("CREATE_TIME");
	obmit.push("MOD_TIME");
	obmit.push("STATUS");

	// alias
	var alias = new Object();
	alias['START_TIME'] = '开始测量时间';
	alias['RECORD_COUNT'] = '';

	if (data == null || data == undefined) {

		return;
	}

	var list = data['data'];
	// var th="<tr><th><input type='checkbox'
	// id='selAllNcsCb'/>全选</th><th>文件名称</th><th>BSC</th><th>频段</th><th>SEGTIME</th><th>ABSS</th><th>NUMFREQ</th><TH>RECTIME</TH>"
	// +
	// "<TH>RID</TH><TH>ECNOABSS</TH> <TH>RELSS</TH>" +
	// " <TH>RELSS2</TH> <TH>RELSS3</TH> <TH>RELSS4</TH>" +
	// " <TH>RELSS5</TH><th>操作</th></tr>" ;

	// 情况表格显示
	$("#queryNcsResultTab tr:not(:first)").each(function(i, ele) {
		$(ele).remove();
	});

	var html = "";
	var tr = "";
	var one = "";
	for ( var i = 0; i < list.length; i++) {
		tr = "<tr>";
		one = list[i];
		tr += "<td><input type='checkbox' class='forcheck' id='ncs_"
				+ one['RNO_NCS_DESC_ID'] + "' onclick='refreshNcsAnalysisList(this,\""+"ncsAnalysisListTable"+"\")'/></td>";
		tr += "<td><span>" + getValidValue(one['NAME'], '未知文件名')+"</span><br/>";
		//--是否可以查看结果
		if(one['STATUS']=='H'){
			tr+="<a onclick='getSingleAnaRes(\""+ one['RNO_NCS_DESC_ID'] +"\",this)'>查看分析结果</a>";
		}else{
			tr+="<a onclick='getSingleAnaRes(\""+ one['RNO_NCS_DESC_ID'] +"\",this)'>分析该NCS</a>";
		}
		tr+="</td>";
		tr += "<td>" + getValidValue(one['BSC'], '未知BSC') + "</td>";
		tr += "<td>" + getValidValue(one['FREQ_SECTION'], '未知频段') + "</td>";
		tr += "<td>" + getValidValue(one['START_TIME'], '') + "</td>";
		tr += "<td>" + getValidValue(one['SEGTIME'], '') + "</td>";
		tr += "<td>" + getValidValue(one['ABSS'], '') + "</td>";
		tr += "<td>" + getValidValue(one['NUMFREQ'], '') + "</td>";
		tr += "<td>" + getValidValue(one['RECTIME'], '') + "</td>";
/*		tr += "<td>" + getValidValue(one['RID'], '') + "</td>";
		tr += "<td>" + getValidValue(one['ECNOABSS'], '') + "</td>";*/
		tr += "<td>"
				+ getValidValue(getRelssval(one['RELSS_SIGN'], one['RELSS']),
						'') + "</td>";
		tr += "<td>"
				+ getValidValue(getRelssval(one['RELSS2_SIGN'], one['RELSS2']),
						'') + "</td>";
		tr += "<td>"
				+ getValidValue(getRelssval(one['RELSS3_SIGN'], one['RELSS3']),
						'') + "</td>";
		tr += "<td>"
				+ getValidValue(getRelssval(one['RELSS4_SIGN'], one['RELSS4']),
						'') + "</td>";
		tr += "<td>"
				+ getValidValue(getRelssval(one['RELSS5_SIGN'], one['RELSS5']),
						'') + "</td>";
		/*tr+="<td><input type='button' value='删除' onclick='return deleteNcsRec(\""+one['RNO_NCS_DESC_ID']+"\")'/></td>";*/

		tr += "</tr>";
		html += tr;

	}

	table.append($(html));

	// 设置隐藏的page信息
	setFormPageInfo("ncsDescDataForm", data['page']);

	// 设置分页面板
	setPageView(data['page'], "ncsResultPageDiv");
}

//根据ncsid删除相关的记录
function deleteNcsRec(ncsId){
	if(confirm("确定要删除该项吗？")!=true){
          return false;
         }
	showOperTips("loadingDiv", "tipcontentId", "正在删除....");
	var td=$(this).parent("td");
	var tr=td.parent("tr");
	var table=$("#ncsDescDataForm");
	$.ajax({
		url:'deleteNcsRecByIdForAjaxAction',
		data:{'ncsId':ncsId},
		type:'post',
		dataType:'text',
		success:function(raw){
			hideOperTips("loadingDiv");
			//重新执行查询
			table.find("#hiddenTotalCnt").val('-1');//表示在后台要重新计算总数量
			//当前有多少条记录
			var rowcnt=document.getElementById('queryNcsResultTab').rows.length;
			if(rowcnt>2){
				//此时重新查询就可以了。
			}else{
				var curpage=new Number(table.find("#hiddenCurrentPage").val());
				//该页只有一行，如果当前不是第一页的话，就将页数减一，重新查询
				if(curpage>1){
					table.find("#hiddenCurrentPage").val(curpage-1);//减一页
				}
			}
			
			//执行查询
			$("#ncs_cityId2").val(cityId);
//			cityName = $.trim($("#cityId2").find("option:selected").text());

			$("#ncs_areaId2").val(areaId);
//			areaName = $.trim($("#areaId2").find("option:selected").text());
			doQueryNcsDesc();
			animateInAndOut("operInfo", 500, 500, 1000, "operTip",
								"删除成功。");
		},fail:function(raw){
			hideOperTips("loadingDiv");
			animateInAndOut("operInfo", 500, 500, 1000, "operTip",
								"删除失败。");
		}
	});
}
//根据ncsid集合删除相关的记录(722,720)
function deleteNcsSelectedRec(ncsIds){
	showOperTips("loadingDiv", "tipcontentId", "正在删除....");
	//var td=$(this).parent("td");
	//var tr=td.parent("tr");
	var table=$("#ncsDescDataForm");
	$.ajax({
		url:'deleteNcsSelectedRecByIdForAjaxAction',
		data:{'ncsIds':ncsIds},
		type:'post',
		dataType:'text',
		success:function(raw){
			hideOperTips("loadingDiv");
			//重新执行查询
			table.find("#hiddenTotalCnt").val('-1');//表示在后台要重新计算总数量
			//当前有多少条记录
			var rowcnt=document.getElementById('queryNcsResultTab').rows.length;
			if(rowcnt>2){
				//此时重新查询就可以了。
			}else{
				var curpage=new Number(table.find("#hiddenCurrentPage").val());
				//该页只有一行，如果当前不是第一页的话，就将页数减一，重新查询
				if(curpage>1){
					table.find("#hiddenCurrentPage").val(curpage-1);//减一页
				}
			}
			
			//执行查询
			$("#ncs_cityId2").val(cityId);
//			cityName = $.trim($("#cityId2").find("option:selected").text());

			$("#ncs_areaId2").val(areaId);
//			areaName = $.trim($("#areaId2").find("option:selected").text());
			doQueryNcsDesc();
			animateInAndOut("operInfo", 500, 500, 1000, "operTip",
								"删除成功。");
		},fail:function(raw){
			hideOperTips("loadingDiv");
			animateInAndOut("operInfo", 500, 500, 1000, "operTip",
								"删除失败。");
		}
	});
}
// 设置formid下的page信息
// 其中，当前页会加一
function setFormPageInfo(formId, page) {
	if (formId == null || formId == undefined || page == null
			|| page == undefined) {
		return;
	}

	var form = $("#" + formId);
	if (!form) {
		return;
	}

	// console.log("setFormPageInfo .
	// pageSize="+page.pageSize+",currentPage="+page.currentPage+",totalPageCnt="+page.totalPageCnt+",totalCnt="+page.totalCnt);
	form.find("#hiddenPageSize").val(page.pageSize);
	form.find("#hiddenCurrentPage").val(new Number(page.currentPage));// /
	form.find("#hiddenTotalPageCnt").val(page.totalPageCnt);
	form.find("#hiddenTotalCnt").val(page.totalCnt);

	// alert("after set currentpage in form is
	// :"+form.find("#hiddenCurrentPage").val());
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
	if (page == null || page == undefined) {
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
	$(div).find("#emTotalCnt").html(totalCnt);
	$(div).find("#showCurrentPage").val(currentPage);
	$(div).find("#emTotalPageCnt").html(totalPageCnt);
}

// 根据符号和值组合值
function getRelssval(sign, val) {
	if (sign == 1) {
		return "-" + val;
	} else {
		return val;
	}
}

/**
 * 提交分析任务
 */
function analysisNcsStructure() {
	var ids = new Array();
	var trs = new Array();
	$("#queryNcsResultTab").find("input.forcheck:checked").each(
			function(i, ele) {
				ids.push($(ele).attr("id"));
				// trs.push($(ele).closest("tr"));
			});

	if (ids.length == 0) {
		// layer.msg("请先选择需要删除的邻区关系",2,2);
		alert('请先选择ncs');
		return;
	}

	clearOldData();// 清除旧的展现数据和变量

	if (ids.length == 1) {
		alert("请选择多个NCS");
		return;
		// 单个ncs的
		isSingleNcs = true;
		token = ids[0];
		getSingleAnaRes(ids[0]);
	} else {
		// 多个ncs的
		var ncsids = ids.join(",");
		isSingleNcs = false;
		var level = '';
		var id = '';
		var name = '';
		if (areaName == '全部') {
			level = '市';
			id = cityId;
			name = cityName;
		} else {
			level = '区/县';
			id = areaId;
			name = areaName;
		}
		// alert("暂时不支持联合分析。");
		$.ajax({
			url : 'submitNcsAnalysisTaskAction',
			data : {
				'ncsIds' : ncsids,
				'level' : level,
				'areaId' : id,
				'areaName' : name,
				'cityId':cityId
			},
			type : 'post',
			dataType : 'text',
			success : function(raw) {
				var data = null;

				try {
					data = eval("(" + raw + ")");
					if (data['flag'] == false) {
						alert("任务提交失败！原因：" + data['msg']);
					} else {
						animateInAndOut("operInfo", 500, 500, 1000, "operTip",
								"计算任务提交成功，请稍后查看结果。");
					}

				} catch (err) {

				}

			}
		});
	}

}

/**
 * 清除旧数据
 */
function clearOldData() {
	clusterDataVisit_1 = false;// 是否获取过“所有连通簇列表暑假”数据
	clusterDataVisit_2 = false;// 是否获取过“最大连通簇列表”数据
	clusterDataVisit_3 = false;// 是否获取过“簇相关小区列表”数据
	clusterDataVisit_4 = false;// 是否获取过“自动优化建议”数据

	var isSingleNcs = false;// 是否分析单个ncs
	var token = null;// 任务token，如果是单个ncs的话，则是ncsId

	// 清除表格数据

	// ---结构指标数据-----//
	$("#ncsStructDataTab tr:not(:first)").each(function(i, ele) {
		$(ele).remove();
	});

	// ---cluster 数据-----//
	$("#ncsClusterDataTab1 tr:not(:first)").each(function(i, ele) {
		$(ele).remove();
	});
	$("#ncsClusterDataTab2 tr:not(:first)").each(function(i, ele) {
		$(ele).remove();
	});
	$("#ncsClusterDataTab3 tr:not(:first)").each(function(i, ele) {
		$(ele).remove();
	});
	$("#ncsOptSuggestionTab tr:not(:first)").each(function(i, ele) {
		$(ele).remove();
	});

	// 清除地图渲染相关的数据
	hasCenter = false;
	gisCellDisplayLib.clearData();
	gisCellList.splice(0, gisCellList.length);
	ncsDetailList.splice(0, ncsDetailList.length);
}

/**
 * 显示单个ncs的分析结果 1、结构指标 2、簇
 * 
 * @param ncsId
 * @param obj:html元素
 */
function getSingleAnaRes(ncsId,obj) {
	showOperTips("loadingDiv", "tipcontentId", "正在获取报告信息....");
	isSingleNcs=true;
	token=ncsId;
	// 结构指标
	$.ajax({
		url : 'getNcsReportDataForAjaxAction',
		data : {
			'taskId' : ncsId,
			'dataType' : 'cellres',// cellres cluster clustercell
			// clustercellrela
			'singleNcs' : 1
		},
		dataType : 'text',
		type : 'post',
		success : function(raw) {

			if (raw == "doing") {
				animateInAndOut("operInfo", 500, 500, 1000, "operTip",
						"该ncs的结果正在分析中，请稍后再试");
			} else {
				animateInAndOut("operInfo", 500, 500, 1000, "operTip",
						"分析完毕，请查看结果！");
				$(obj).text("查看分析结果");
				var data = null;
				try {
					data = eval("(" + raw + ")");
					showStructData(data);// 显示结构指标
					$("#structDataLi").trigger("click");
				} catch (err) {
					alert("获取分析结果出错！"+err);
				}
			}
		},
		complete : function() {
			hideOperTips("loadingDiv");
		}
	})
}

/**
 * 显示结构指标数据
 * 
 * @param data
 */
function showStructData(data) {
	$("#ncsStructDataTab tr:not(:first)").each(function(i, ele) {
		$(ele).remove();
	});

	if (!data || data.length == 0) {
		return;
	}

	var one = null;
	var html = "";
	// 小区 小区频点数 被干扰系数 网络结构指数 冗余覆盖指数 重叠覆盖度 干扰源系数 过覆盖系数 小区检测次数 理想覆盖距离 关联邻小区数
	// 小区覆盖分量 小区容量分量

	for ( var i = 0; i < data.length; i++) {
		one = data[i];
		html += "<tr>";
		html += "<td>" + (i + 1) + "</td>";
		html += "<td>" + one['cell'] + "</TD>";
		html += "<td>" + getValidValue(one['FREQ_CNT'], '') + "</TD>";
		html += "<td>" + getValidValue(one['BE_INTERFER'], '', 5) + "</TD>";
		html += "<td>" + getValidValue(one['NET_STRUCT_FACTOR'], '', 5)
				+ "</TD>";
		html += "<td>" + getValidValue(one['REDUNT_COVER_FACT'], '', 5)
				+ "</TD>";
		html += "<td>" + getValidValue(one['OVERLAP_COVER'], '', 5) + "</TD>";
		html += "<td>" + getValidValue(one['SRC_INTERFER'], '', 5) + "</TD>";
		html += "<td>" + getValidValue(one['OVERSHOOTING_FACT'], '', 5)
				+ "</TD>";
		html += "<td>" + getValidValue(one['DETECT_CNT'], '') + "</TD>";
		html += "<td>" + getValidValue(one['DETECT_CNT_EXINDR'], '') + "</TD>";
		html += "<td>" + getValidValue(one['EXPECTED_COVER_DIS'], '', 5)
				+ "</TD>";
		html += "<td>" + getValidValue(one['INTERFER_NCELL_CNT'], '') + "</TD>";
		html += "<td>" + getValidValue(one['INTERFER_NCE_CNT_EXINDR'], '') + "</TD>";
		html += "<td>" + getValidValue(one['CELL_COVER'], '', 5) + "</TD>";
		html += "<td>" + getValidValue(one['CAPACITY_DESTROY'], '', 5)
				+ "</TD>";
		html += "</tr>";
	}

	$("#ncsStructDataTab").append($(html));

	// 显示在地图上
	showStuctDataOnMap(data, function() {
		// 修改颜色
		showRenderer($("#ncsRendererType").val());
	});

}

/**
 * 获取单个ncs的所有连通簇列表
 */
function getSingleClusterAllData(ncsId) {
	showOperTips("loadingDiv", "tipcontentId", "正在获取报告信息....");
	$.ajax({
		url : 'getNcsReportDataForAjaxAction',
		data : {
			'taskId' : ncsId,
			'dataType' : 'cluster',// cellres cluster clustercell
			// clustercellrela
			'singleNcs' : 1
		},
		type : 'post',
		dataType : 'text',
		success : function(raw) {
			if (raw == "doing") {
				animateInAndOut("operInfo", 500, 500, 1000, "operTip",
						"该ncs的结果正在分析中，请稍后再试");
			} else {
				var data = null;
				try {
					data = eval("(" + raw + ")");
					showClusterData(data);
				} catch (err) {

				}
			}
		},
		complete : function() {
			hideOperTips("loadingDiv");
		}
	});
}

/**
 * 显示连通簇信息
 * 
 * @param data
 */
function showClusterData(data) {
	var html = "";
	if (!data || data.length == 0) {
		return;
	}
	var one;
	for ( var i = 0; i < data.length; i++) {
		one = data[i];
		html += "<tr><td>" + (i + 1) + "</td>";
		html += "<td>" + one['CLUSTER_ID'] + "</td>";
		html += "<td>" + getValidValue(one['CELLCNT'], '') + "</td>";
		html += "<td>" + getValidValue(one['TOTAL_FREQ_CNT'], '') + "</td>";
		html += "<td>" + getValidValue(one['CONTROL_FACTOR'], '', 6) + "</td>";
		html += "<td>" + getValidValue(one['WEIGHT'], '', 6) + "</td>";
		html += "<td>" + getValidValue(one['SECTORS'], '').replace(/,/g, '/')
				+ "</td>";
		html += "</tr>";
	}
	$("#ncsClusterDataTab1").append(html);
}

/**
 * 获取单个ncs的最大连通簇小区列表
 */
function getSingleMaxClusterCellAllData(ncsId) {
	showOperTips("loadingDiv", "tipcontentId", "正在获取报告信息....");
	$.ajax({
		url : 'getNcsReportDataForAjaxAction',
		data : {
			'taskId' : ncsId,
			'dataType' : 'clustercell',// cellres cluster clustercell
			// clustercellrela
			'singleNcs' : 1
		},
		type : 'post',
		dataType : 'text',
		success : function(raw) {
			var data = null;
			if (raw == "doing") {
				animateInAndOut("operInfo", 500, 500, 1000, "operTip",
						"该ncs的结果正在分析中，请稍后再试");
			} else {
				try {
					data = eval("(" + raw + ")");
					showClusterCell(data);
				} catch (err) {

				}
			}
		},
		complete : function() {
			hideOperTips("loadingDiv");
		}

	});
}

/**
 * 显示连通簇内小区
 * 
 * @param data
 */
function showClusterCell(data) {
	var html = "";
	if (!data || data.length == 0) {
		return;
	}
	var one;
	for ( var i = 0; i < data.length; i++) {
		one = data[i];
		html += "<tr><td>" + (i + 1) + "</td>";
		html += "<td>" + one['CLUSTER_ID'] + "</td>";
		html += "<td>" + one['CELL'] + "</td>";
		html += "<td>" + getValidValue(one['NAME'], '') + "</td>";
		html += "<td>" + getValidValue(one['FREQ_CNT'], '') + "</td>";
		html += "<td>" + getValidValue(one['TOTAL_FREQ_CNT'], '') + "</td>";
		html += "</tr>";
	}
	$("#ncsClusterDataTab2").append(html);
}

/**
 * 获取单个ncs的簇内小区干扰关系列表
 */
function getSingleMaxClusterCellInterAllData(ncsId) {
	showOperTips("loadingDiv", "tipcontentId", "正在获取报告信息....");
	$.ajax({
		url : 'getNcsReportDataForAjaxAction',
		data : {
			'taskId' : ncsId,
			'dataType' : 'clustercellrela',// cellres cluster clustercell
			// clustercellrela
			'singleNcs' : 1
		},
		type : 'post',
		dataType : 'text',
		success : function(raw) {
			if (raw == "doing") {
				animateInAndOut("operInfo", 500, 500, 1000, "operTip",
						"该ncs的结果正在分析中，请稍后再试");
			} else {
				var data = null;
				try {
					data = eval("(" + raw + ")");
					showClusterCellRela(data);
				} catch (err) {

				}
			}
		},
		complete : function() {
			hideOperTips("loadingDiv");
		}

	});
}

/**
 * 显示簇内小区的干扰关系
 * 
 * @param data
 */
function showClusterCellRela(data) {
	var html = "";
	if (!data || data.length == 0) {
		return;
	}
	var one;
	for ( var i = 0; i < data.length; i++) {
		one = data[i];
		html += "<tr><td>" + (i + 1) + "</td>";
		html += "<td>" + one['CELL'] + "</td>";
		html += "<td>" + one['CLUSTER_ID'] + "</td>";
		html += "<td>" + one['TOTAL_FREQ_CNT'] + "</td>";
		html += "<td>" + one['NCELL'] + "</td>";
		html += "</tr>";
	}
	$("#ncsClusterDataTab3").append(html);
}

/**
 * 分页跳转的响应
 * 
 * @param dir
 * @param action
 * @param formId
 */
function showListViewByPage(dir, action, formId) {

	var form = $("#" + formId);

	// alert(form.find("#hiddenPageSize").val());
	var pageSize = new Number(form.find("#hiddenPageSize").val());
	var currentPage = new Number(form.find("#hiddenCurrentPage").val());
	var totalPageCnt = new Number(form.find("#hiddenTotalPageCnt").val());
	var totalCnt = new Number(form.find("#hiddenTotalCnt").val());

	// console.log("pagesize="+pageSize+",currentPage="+currentPage+",totalPageCnt="+totalPageCnt+",totalCnt="+totalCnt);
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
	} else {
		return;
	}
	// 获取资源
	if (typeof action == "function") {
		action();
	}
}
/**
 * 点击ncs获取提示信息
 */
function clickNcsPrompt(){
	var black_overlay={ 
            'display': 'none',
            'position': 'absolute',
            'top': '0%',
            'left': '0%',
            'width': '100%',
            'height': '100%',
            'background-color': 'white',
            'background': 'white',
            'z-index':'1000001', 
           '-moz-opacity': '0.1', 
            'opacity':'.01',
            'filter': 'alpha(opacity=1)' 
        } 
	var white_content ={ 
            'display': 'none',
            'position': 'absolute',
            'top': '25%',
            'left': '25%',
            'width': '55%',
            'height': '55%',
            'padding': '20px',
            'border': '10px solid orange',
            'background-color': 'white',//#0099FF
            'background':'white',
            'z-index':'1000002',
            'overflow': 'auto',
            'opacity':'1'
            /*'-moz-opacity': '0.8', 
            'opacity':'.80',
            'filter': 'alpha(opacity=88)' */
            }
        var lightdiv=document.createElement("div");
        var tipstr="<table width=\"100%\" border=\"1\" cellpadding=\"0\" cellspacing=\"0\" bordercolor=\"#FF0099\" style=\"line-height:30px; border-color:#FF0099\">"
			  +"<tr>"
			  +"<th colspan=\"2\" style=\"font-weight:bold\">注意事项</th>"
			  +"</tr>"
			  +"<tr>"
			  +"<td width=\"100px\">相关数据准备：</td>"
			  +"<td></td>"
			  +"</tr>"
			  +"<tr>"
			    +"<td>1、小区数据</td>"
			    +"<td style=\"line-height:normal\">确定小区配置数据为最新数据。<br/>操作指引：在[资源配置]项的[小区信息管理]页面小区信息导入最新的小区数据信息</td>"
			  +"</tr>"
			  +"<tr>"
			    +"<td>2、话统数据</td>"
			    +"<td style=\"line-height:normal\">确定有最新的话务统计数据(用于自动优化功能分析)。<br/>操作指引：[话统分析]项的[加载话务性能]页面话统指标导入NCS相关的小区的最新话统指标</td>"
			  +"</tr>"
			+"</table>";
        lightdiv.innerHTML=tipstr;
       
        lightdiv.id='light';
        lightdiv.class='white_content';
        setStyle(lightdiv,white_content);
        //$("#ncsprompt").attr("disabled","disabled");
        
        var fadediv=document.createElement("div");
        fadediv.id='fadelayer';
        setStyle(fadediv,black_overlay);
        document.body.appendChild(fadediv);
        document.body.appendChild(lightdiv);   
        $(fadediv).show("slow");
        $(lightdiv).show("slow");
}
/**
 * 对html元素对象设置样式
 * @param {} obj
 * @param {} css
 */
function setStyle(obj,css){
	for(var atr in css){
	obj.style[atr] = css[atr];
	}
} 

/**
 * @author Liang YJ
 * @param obj
 * @date 2014-4-22 17:28
 * @description 在ncs数据选择窗口选择数据时，刷新ncs分析列表
 */
function refreshNcsAnalysisList(obj,tabName){
	var analysisList = $("#"+tabName);
	var item = $(obj);
	var id = item.attr("id");
	if(item.attr("checked")){
		//选择
		//如果ncsAnalysisList已经有该选项
		analysisList.find("tr").each(function (){
			if($(this).children(":first").children("input").val()===id){
				return;
			}
		});
		//如果ncsAnalysisList上未有该选项
		var length = analysisList.find("tr").length;//firefox下table的直接子标签是tbody,不是tr
		var areaName = item.parent().next("td").children("span").text();
		var html = "";
		if(length%2==0){
			html += "<tr class='tb-tr-bg-warmwhite'>";
		}else{
			html += "<tr class='tb-tr-bg-coldwhite'>";
		}
		html += "<td><input type='hidden' value='"+id+"'/></td>";
		html += "<td><span>"+areaName+"</span></td>";
		html +="<td><input type='button' class='delete_selected_item' value='删除' onclick='deleteSelectedItem(this);'/></td></tr>";
		analysisList.append(html);
		
	}else{
		//取消选择
		analysisList.find("tr").each(function (index){
			if($(this).children(":first").children("input").val()===id){
				analysisList.find("tr").eq(index).remove();
			}
		});
		
	}
}
