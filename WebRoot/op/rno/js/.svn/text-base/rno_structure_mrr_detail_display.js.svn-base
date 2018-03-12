$(document).ready(
		function() {
		//tab选项卡
		tab("div_tab1", "li", "onclick");//项目服务范围类别切换
			initAreaCascade();

			//ncs记录查询
			$("#searchMrrDescBtn").click(function(){
				initFormPage("mrrDescDataForm");
				doQueryMrrDesc();
			});
			
			
			
			//点击某个详情按钮，显示相应的详情按钮
			tab("mrr_detail_tab", "li", "onclick");//
		});



//执行查询mrr
function doQueryMrrDesc(){
	$("#mrrDescDataForm").ajaxSubmit({
		url : 'queryMrrDescriptorByPageForAjaxAction',
		dataType : 'text',
		success : function(data) {
			showMrrQueryResult(data);
		},
		complete : function() {
			$(".loading_cover").css("display", "none");
		}
	})
}


function initAreaCascade() {
	
//	$("#areaId2").append("<option selected='true' value=''>全部</option>");
	
	$("#provinceId2").change(function() {
		getSubAreas("provinceId2", "cityId2", "市");
	});

	$("#cityId2").change(function() {
		getSubAreas("cityId2", "areaId2", "区/县",function(){
			$("#areaId2").append("<option selected='true' value=''>全部</option>");
		});
		
	});
}


//初始化form下的page信息
function initFormPage(formId){
	var form=$("#"+formId);
	if(!form){
		return;
	}
	form.find("#hiddenPageSize").val(25);
	form.find("#hiddenCurrentPage").val(1);
	form.find("#hiddenTotalPageCnt").val(-1);
	form.find("#hiddenTotalCnt").val(-1);
}

//设置formid下的page信息
//其中，当前页会加一
function setFormPageInfo(formId,page){
	if(formId==null || formId==undefined || page==null || page==undefined){
		return;
	}
	
	var form=$("#"+formId);
	if(!form){
		return;
	}
	
	form.find("#hiddenPageSize").val(page.pageSize);
	form.find("#hiddenCurrentPage").val(new Number(page.currentPage));///
	form.find("#hiddenTotalPageCnt").val(page.totalPageCnt);
	form.find("#hiddenTotalCnt").val(page.totalCnt);
}

/*
* 显示查询的mrr记录信息
* data:包含page信息和查询回来的数据信息
*/
function showMrrQueryResult(rawdata){
	
	var data=eval("("+rawdata+")");
	
	var table=$("#queryMrrResultTab");
	
	if(data==null || data==undefined){
		return;
	}
	
	var list=data['data'];
	var th="<tr><th>选择</th><th>记录日期</th><th>文件名称</th><th>BSC</th><th>区域</th><th>操作</th></tr>" ;
	var html="";
	html+=th;
	
	var tr="";
	var one="";
	for(var i=0;i<list.length;i++){
		tr="<tr>";
		one=list[i];
		tr+="<td><input type='checkbox' class='forcheck' id='"+one['RNO_NCS_DESC_ID']+"' onclick=\"loadAndShowNcsCellInteferenceAnalysisList(this)\" /></td>";
		tr+="<td>"+getValidValue(one['MEA_DATE'],'未知日期')+"</td>";
		tr+="<td>"+getValidValue(one['FILE_NAME'],'未知文件名')+"</td>";
		tr+="<td>"+getValidValue(one['BSC'],'未知BSC')+"</td>";
		tr+="<td>"+getValidValue(one['CITY_ID'],'未知区域')+"</td>";
		tr+="<td><input type='button' onclick=\"viewMrrDetail('"+one['ERI_MRR_DESC_ID']+"') \" value='查看详情'/></td>";
		
		tr+="</tr>";
		html+=tr;
		
	}
	
	table.html(html);
	
	//设置隐藏的page信息
	setFormPageInfo("mrrDescDataForm",data['page']);
	
	//设置分页面板
	setPageView(data['page'],"mrrResultPageDiv");
}

/**
* 查看mrr的详情
* @param ncsId
*/
function viewMrrDetail(mrrId){
	resetVisitFlat();
	//将这个ncsid赋予所有的隐藏域
	$(".hiddenMrrIdCls").val(mrrId);
	
	//查管理信息
	initFormPage('MrrAdmDataForm');
	getMrrAdmData();
	
	//查信号强度
	initFormPage('MrrStrengthDataForm');
	getMrrStrengthData();
	
	//查信号质量
	initFormPage('MrrQualityDataForm');
	getMrrQualityData();

	//查传输功率
	initFormPage('MrrPowerDataForm');
	getMrrPowerData();
	
	//查实时预警
	initFormPage('MrrTaDataForm');
	getMrrTaData();
	
	//查路径损耗
	initFormPage('MrrPlDataForm');
	getMrrPlData();
	
	//查路径损耗差异
	initFormPage('MrrPldDataForm');
	getMrrPldData();
	
	//查测量结果
	initFormPage('MrrMeaDataForm');
	getMrrMeaData();
	
	//查上下行FER
	initFormPage('MrrFerDataForm');
	getMrrFerData();
	
	$("#mrrDetailLi").trigger("click");
}


/**
 * 获取mrr管理信息
 */
function getMrrAdmData(){
	//alert("查小区2！");
	getMrrRelativeData(
			"queryMrrAdmDataByPageForAjaxAction",
			'MrrAdmDataForm', 'queryAdmDataTab',
			'MrrAdmDataPageDiv','mrrAdm');
}
/**
 * 获取mrr信号强度信息
 */
function getMrrStrengthData(){
	getMrrRelativeData("queryMrrStrenDataByPageForAjaxAction",
			'MrrStrengthDataForm', 'queryStrengthDataTab',
			'MrrStrengthDataPageDiv','mrrStrength');
}
/**
 * 获取mrr信号质量信息
 */
function getMrrQualityData(){
	getMrrRelativeData("queryMrrQualiDataByPageForAjaxAction",
			'MrrQualityDataForm', 'queryQualityDataTab',
			'MrrQualityDataPageDiv','mrrQuality');
}
/**
 * 获取mrr传输功率信息
 */
function getMrrPowerData(){
	getMrrRelativeData("queryMrrPowerDataByPageForAjaxAction",
			'MrrPowerDataForm', 'queryPowerDataTab',
			'MrrPowerDataPageDiv','mrrPower');
}
/**
 * 获取mrr实时预警信息
 */
function getMrrTaData(){
	getMrrRelativeData("queryMrrTaDataByPageForAjaxAction",
			'MrrTaDataForm', 'queryTaDataTab',
			'MrrTaDataPageDiv','mrrTa');
}
/**
 * 获取mrr路径损耗信息
 */
function getMrrPlData(){
	getMrrRelativeData("queryMrrPlDataByPageForAjaxAction",
			'MrrPlDataForm', 'queryPlDataTab',
			'MrrPlDataPageDiv','mrrPl');
}
/**
 * 获取mrr路径损耗差异信息
 */
function getMrrPldData(){
	getMrrRelativeData("queryMrrPldDataByPageForAjaxAction",
			'MrrPldDataForm', 'queryPldDataTab',
			'MrrPldDataPageDiv','mrrPld');
}
/**
 * 获取mrr测量结果信息
 */
function getMrrMeaData(){
	getMrrRelativeData("queryMrrMeaDataByPageForAjaxAction",
			'MrrMeaDataForm', 'queryMeaDataTab',
			'MrrMeaDataPageDiv','mrrMea');
}
/**
 * 获取mrr的上下行FER信息
 */
function getMrrFerData(){
	getMrrRelativeData("queryMrrFerDataByPageForAjaxAction",
			'MrrFerDataForm', 'queryFerDataTab',
			'MrrFerDataPageDiv','mrrFer');
}

/**
 * 统一获取数据的处理方法
 * @param url
 * 后台action
 * @param formId
 * 提交的form id
 * @param tabId
 * 展示结果的table id
 * @param pageDivId
 * 分页id
 */
function getMrrRelativeData(url, formId, tabId, pageDivId,type) {
	$(".loading_cover").css("display", "block");
	$("#" + formId).ajaxSubmit({
		url : url,
		dataType : 'text',
		success : function(data) {
			showMrrItemDetailQueryResult(data, tabId, formId, pageDivId,type);
		},
		complete : function() {
			$(".loading_cover").css("display", "none");
		}
	});

}


function showMrrItemDetailQueryResult(data, tabId, formId, pageDivId,type) {


	data = eval("(" + data + ")");

	var page = data['page'];
	var mrrItemlist = data['data'];

	// 设置form的分页信息
	setFormPageInfo(formId, page);

	// 设置分页控件
	setPageView(page, pageDivId);

	// 用于显示结果的table
	var table = $("#" + tabId);
	// 只保留表头
	// table.append(getHead());
	$("#" + tabId + " tr:not(:first)").each(function(i, ele) {
		$(ele).remove();
	});
	// console.log("celllist====" + celllist);
	if (mrrItemlist) {
		var one;
		var tr;
		for ( var i = 0; i < mrrItemlist.length; i++) {
			one = mrrItemlist[i];
			if (!one) {
				//console.log("one is null!");
				continue;
			}

			if (type == "mrrAdm") {

				tr = "<tr class=\"greystyle-standard-whitetr\"  align=\"center\">";
				tr += "<td>" + getValidValue(one['FILE_FORMAT']) + "</td>";
				tr += "<td>" + getValidValue(one['START_DATE']) + "</td>";
				tr += "<td>" + getValidValue(one['RECORD_INFO']) + "</td>";
				tr += "<td>" + getValidValue(one['RID']) + "</td>";
				tr += "<td>" + getValidValue(one['TOTAL_TIME']) + "</td>";
				tr += "<td>" + getValidValue(one['MEASURE_LIMIT']) + "</td>";
				tr += "<td>" + getValidValue(one['MEASURE_SIGN']) + "</td>";
				tr += "<td>" + getValidValue(one['MEASURE_INTERVAL']) + "</td>";
				tr += "<td>" + getValidValue(one['MEASURE_TYPE']) + "</td>";
				tr += "<td>" + getValidValue(one['MEASURING_LINK']) + "</td>";
				tr += "<td>" + getValidValue(one['MEASURE_LIMIT2']) + "</td>";
				tr += "<td>" + getValidValue(one['MEASURE_LIMIT3']) + "</td>";
				tr += "<td>" + getValidValue(one['MEASURE_LIMIT4']) + "</td>";
				tr += "<td>" + getValidValue(one['CONNECTION_TYPE']) + "</td>";
				tr += "<td>" + getValidValue(one['DTM_FILTER']) + "</td>";
				/*
				 * tr += "<td> <input type='checkbox' id='" +
				 * getValidValue(one['ID']) + "' class='forcheck' /></td>";
				 */
				tr += "</tr>";
			}
			else if (type == "mrrStrength") {
				tr = "<tr class=\"greystyle-standard-whitetr\"  align=\"center\">";
				tr += "<td>" + getValidValue(one['CELL_NAME']) + "</td>";
				tr += "<td>" + getValidValue(one['SUBCELL']) + "</td>";
				tr += "<td>" + getValidValue(one['CHANNEL_GROUP_NUM']) + "</td>";

				var ulValStr = "";
				var dlValStr = "";
				var totUl = 0;
				var totDl = 0;
				var ul,dl;
				for(var j = 0; j <= 63; j++) {
					ul = one['RXLEVUL'+j];
					dl = one['RXLEVDL'+j];
					totUl += ul;
					totDl += dl;
					ulValStr += "RXLEVUL" + j + "=" + one['RXLEVUL'+j] + ", ";
					dlValStr += "RXLEVDL" + j + "=" + one['RXLEVDL'+j] + ", ";
				}
				var avgUl = totUl/64;
				var avgDl = totDl/64;

				tr += "<td id='sUl"+ one['SIGNAL_STRENGTH_ID'] 
						+ "' onclick='viewVals(\"sUl"+one['SIGNAL_STRENGTH_ID']+"\")'>" + parseFloat(getValidValue(avgUl, '', 5)) + "</td>";
				tr += "<td id='sDl"+ one['SIGNAL_STRENGTH_ID'] 
						+ "' onclick='viewVals(\"sDl"+one['SIGNAL_STRENGTH_ID']+"\")'>" + parseFloat(getValidValue(avgDl, '', 5)) + "</td>";
				tr += "</tr>";
				tr += "<tr id='sUl"+ one['SIGNAL_STRENGTH_ID']
						+ "' class='greystyle-standard-whitetr' align='center' style='display:none;'><td colspan='5'>"
						+ ulValStr + "</td></tr>";
				tr += "<tr id='sDl"+ one['SIGNAL_STRENGTH_ID']
						+ "' class='greystyle-standard-whitetr' align='center' style='display:none;'><td colspan='5'>"
						+ dlValStr + "</td></tr>";
			}
			else if (type == "mrrQuality") {
				tr = "<tr class=\"greystyle-standard-whitetr\"  align=\"center\">";
				tr += "<td>" + getValidValue(one['CELL_NAME']) + "</td>";
				tr += "<td>" + getValidValue(one['SUBCELL']) + "</td>";
				tr += "<td>" + getValidValue(one['CHANNEL_GROUP_NUM']) + "</td>";

				var ulValStr = "";
				var dlValStr = "";
				var totUl = 0;
				var totDl = 0;
				var ul,dl;
				for(var j = 0; j <= 7; j++) {
					ul = one['RXQUALUL'+j];
					dl = one['RXQUALDL'+j];
					totUl += ul;
					totDl += dl;
					ulValStr += "RXQUALUL" + j + "=" + one['RXQUALUL'+j] + ", ";
					dlValStr += "RXQUALDL" + j + "=" + one['RXQUALDL'+j] + ", ";
				}
				var avgUl = totUl/8;
				var avgDl = totDl/8;

				tr += "<td id='qUl"+ one['SIGNAL_QUALITY_ID'] 
						+ "' onclick='viewVals(\"qUl"+one['SIGNAL_QUALITY_ID']+"\")'>" + parseFloat(getValidValue(avgUl, '', 5)) + "</td>";
				tr += "<td id='qDl"+ one['SIGNAL_QUALITY_ID'] 
						+ "' onclick='viewVals(\"qDl"+one['SIGNAL_QUALITY_ID']+"\")'>" + parseFloat(getValidValue(avgDl, '', 5)) + "</td>";
				tr += "</tr>";
				tr += "<tr id='qUl"+ one['SIGNAL_QUALITY_ID']
						+ "' class='greystyle-standard-whitetr' align='center' style='display:none;'><td colspan='5'>"
						+ ulValStr + "</td></tr>";
				tr += "<tr id='qDl"+ one['SIGNAL_QUALITY_ID']
						+ "' class='greystyle-standard-whitetr' align='center' style='display:none;'><td colspan='5'>"
						+ dlValStr + "</td></tr>";
			}
			else if (type == "mrrPower") {
				tr = "<tr class=\"greystyle-standard-whitetr\"  align=\"center\">";
				tr += "<td>" + getValidValue(one['CELL_NAME']) + "</td>";
				tr += "<td>" + getValidValue(one['SUBCELL']) + "</td>";
				tr += "<td>" + getValidValue(one['CHANNEL_GROUP_NUM']) + "</td>";

				var msValStr = "";
				var btsValStr = "";
				var totMs = 0;
				var totBts = 0;
				var ms,bts;
				for(var j = 0; j <= 31; j++) {
					ms = one['MSPOWER'+j];
					totMs += ms;
					msValStr += "MSPOWER" + j + "=" + one['MSPOWER'+j] + ", ";
				}
				for(var j = 0; j <= 15; j++) {
					bts = one['BSPOWER'+j];
					totBts += bts;
					btsValStr += "BSPOWER" + j + "=" + one['BSPOWER'+j] + ", ";
				}
				var avgMs = totMs/32;
				var avgBts = totBts/16;

				tr += "<td id='ms"+ one['TRANSMIT_POWER_ID'] 
						+ "' onclick='viewVals(\"ms"+one['TRANSMIT_POWER_ID']+"\")'>" + parseFloat(getValidValue(avgMs, '', 5)) + "</td>";
				tr += "<td id='bts"+ one['TRANSMIT_POWER_ID'] 
						+ "' onclick='viewVals(\"bts"+one['TRANSMIT_POWER_ID']+"\")'>" + parseFloat(getValidValue(avgBts, '', 5)) + "</td>";
				tr += "</tr>";
				tr += "<tr id='ms"+ one['TRANSMIT_POWER_ID']
						+ "' class='greystyle-standard-whitetr' align='center' style='display:none;'><td colspan='5'>"
						+ msValStr + "</td></tr>";
				tr += "<tr id='bts"+ one['TRANSMIT_POWER_ID']
						+ "' class='greystyle-standard-whitetr' align='center' style='display:none;'><td colspan='5'>"
						+ btsValStr + "</td></tr>";
			}
			else if (type == "mrrTa") {
				tr = "<tr class=\"greystyle-standard-whitetr\"  align=\"center\">";
				tr += "<td>" + getValidValue(one['CELL_NAME']) + "</td>";
				tr += "<td>" + getValidValue(one['SUBCELL']) + "</td>";
				tr += "<td>" + getValidValue(one['CHANNEL_GROUP_NUM']) + "</td>";

				var taValStr = "";
				var totTa = 0;
				var ta;
				for(var j = 0; j <= 75; j++) {
					ta = one['TAVAL'+j];
					totTa += ta;
					taValStr += "TAVAL" + j + "=" + one['TAVAL'+j] + ", ";
				}
				var avgTa = totTa/76;

				tr += "<td id='ta"+ one['TA_ID'] 
						+ "' onclick='viewVals(\"ta"+one['TA_ID']+"\")'>" + parseFloat(getValidValue(avgTa, '', 5)) + "</td>";
				tr += "<tr id='ta"+ one['TA_ID']
						+ "' class='greystyle-standard-whitetr' align='center' style='display:none;'><td colspan='5'>"
						+ taValStr + "</td></tr>";
			}
			else if (type == "mrrPl") {
				tr = "<tr class=\"greystyle-standard-whitetr\"  align=\"center\">";
				tr += "<td>" + getValidValue(one['CELL_NAME']) + "</td>";
				tr += "<td>" + getValidValue(one['SUBCELL']) + "</td>";
				tr += "<td>" + getValidValue(one['CHANNEL_GROUP_NUM']) + "</td>";

				var ulValStr = "";
				var dlValStr = "";
				var totUl = 0;
				var totDl = 0;
				var ul,dl;
				for(var j = 0; j <= 59; j++) {
					ul = one['PLOSSUL'+j];
					totUl += ul;
					ulValStr += "PLOSSUL" + j + "=" + one['PLOSSUL'+j] + ", ";
				}
				for(var j = 0; j <= 64; j++) {
					dl = one['PLOSSDL'+j];
					totDl += dl;
					dlValStr += "PLOSSDL" + j + "=" + one['PLOSSDL'+j] + ", ";
				}
				var avgUl = totUl/60;
				var avgDl = totDl/65;

				tr += "<td id='plUl"+ one['PATH_LOSS_ID'] 
						+ "' onclick='viewVals(\"plUl"+one['PATH_LOSS_ID']+"\")'>" + parseFloat(getValidValue(avgUl, '', 5)) + "</td>";
				tr += "<td id='plDl"+ one['PATH_LOSS_ID'] 
						+ "' onclick='viewVals(\"plDl"+one['PATH_LOSS_ID']+"\")'>" + parseFloat(getValidValue(avgDl, '', 5)) + "</td>";
				tr += "</tr>";
				tr += "<tr id='plUl"+ one['PATH_LOSS_ID']
						+ "' class='greystyle-standard-whitetr' align='center' style='display:none;'><td colspan='5'>"
						+ ulValStr + "</td></tr>";
				tr += "<tr id='plDl"+ one['PATH_LOSS_ID']
						+ "' class='greystyle-standard-whitetr' align='center' style='display:none;'><td colspan='5'>"
						+ dlValStr + "</td></tr>";
			}
			else if (type == "mrrPld") {
				tr = "<tr class=\"greystyle-standard-whitetr\"  align=\"center\">";
				tr += "<td>" + getValidValue(one['CELL_NAME']) + "</td>";
				tr += "<td>" + getValidValue(one['SUBCELL']) + "</td>";
				tr += "<td>" + getValidValue(one['CHANNEL_GROUP_NUM']) + "</td>";

				var pldValStr = "";
				var totPld = 0;
				var pld;
				for(var j = 0; j <= 50; j++) {
					pld = one['PLDIFF'+j];
					totPld += pld;
					pldValStr += "PLDIFF" + j + "=" + one['PLDIFF'+j] + ", ";
				}
				var avgPld = totPld/51;

				tr += "<td id='pld"+ one['PATH_LOSS_DIFF_ID'] 
						+ "' onclick='viewVals(\"pld"+one['PATH_LOSS_DIFF_ID']+"\")'>" + parseFloat(getValidValue(avgPld, '', 5)) + "</td>";
				tr += "<tr id='pld"+ one['PATH_LOSS_DIFF_ID']
						+ "' class='greystyle-standard-whitetr' align='center' style='display:none;'><td colspan='5'>"
						+ pldValStr + "</td></tr>";
			}
			else if (type == "mrrMea") {
				
				tr = "<tr class=\"greystyle-standard-whitetr\"  align=\"center\">";
				tr += "<td>" + getValidValue(one['CELL_NAME']) + "</td>";
				tr += "<td>" + getValidValue(one['SUBCELL']) + "</td>";
				tr += "<td>" + getValidValue(one['CHANNEL_GROUP_NUM']) + "</td>";
				tr += "<td>" + getValidValue(one['REP']) + "</td>";
				tr += "<td>" + getValidValue(one['REP_FER_UPLINK']) + "</td>";
				tr += "<td>" + getValidValue(one['REP_FER_DOWNLINK']) + "</td>";
				tr += "<td>" + getValidValue(one['REP_FER_BL']) + "</td>";
				tr += "<td>" + getValidValue(one['REP_FER_THL']) + "</td>";

				tr += "</tr>";
			}
			else if (type == "mrrFer") {
				tr = "<tr class=\"greystyle-standard-whitetr\"  align=\"center\">";
				tr += "<td>" + getValidValue(one['CELL_NAME']) + "</td>";
				tr += "<td>" + getValidValue(one['SUBCELL']) + "</td>";
				tr += "<td>" + getValidValue(one['CHANNEL_GROUP_NUM']) + "</td>";
				var ulValStr = "";
				var dlValStr = "";
				var totUl = 0;
				var totDl = 0;
				var ul,dl;
				for(var j = 0; j <= 96; j++) {
					ul = one['FER_UPLINK'+j];
					totUl += ul;
					ulValStr += "FER_UPLINK" + j + "=" + one['FER_UPLINK'+j] + ", ";
				}
				for(var j = 0; j <= 96; j++) {
					dl = one['FER_DOWNLINK'+j];
					totDl += dl;
					dlValStr += "FER_DOWNLINK" + j + "=" + one['FER_DOWNLINK'+j] + ", ";
				}
				var avgUl = totUl/97;
				var avgDl = totDl/97;

				tr += "<td id='ferUl"+ one['FER_ID'] 
						+ "' onclick='viewVals(\"ferUl"+one['FER_ID']+"\")'>" + parseFloat(getValidValue(avgUl, '', 5)) + "</td>";
				tr += "<td id='ferDl"+ one['FER_ID'] 
						+ "' onclick='viewVals(\"ferDl"+one['FER_ID']+"\")'>" + parseFloat(getValidValue(avgDl, '', 5)) + "</td>";
				tr += "</tr>";
				tr += "<tr id='ferUl"+ one['FER_ID']
						+ "' class='greystyle-standard-whitetr' align='center' style='display:none;'><td colspan='5'>"
						+ ulValStr + "</td></tr>";
				tr += "<tr id='ferDl"+ one['FER_ID']
						+ "' class='greystyle-standard-whitetr' align='center' style='display:none;'><td colspan='5'>"
						+ dlValStr + "</td></tr>";
			}
			// console.log("tr===" + tr);
			table.append($(tr));// 增加
		}
	}
}

/**
 * 统一显示隐藏值的方法
 * @param id
 * 要显示的tr对应id
 */
function viewVals(id) {
	var disVal = $("tr#"+id).css("display");
	if(disVal=="none") {
		$("tr#"+id).css({
			"display" : ""
		});
		$("td#"+id).css({
			"background" : "#819A9C"
		});
	} else {
		$("tr#"+id).css({
			"display" : "none"
		});
		$("td#"+id).css({
			"background" : ""
		});
	}
}

//根据符号和值组合值
function getRelssval(sign,val){
	if(sign==1){
		return "-"+val;
	}
	else{
		return val;
	}
}

/**
* 设置分页面板
* @param page
* 分页信息
* @param divId
* 分页面板id
*/
function setPageView(page,divId){
	if(page==null || page==undefined  ){
		return;
	}
	
	var div=$("#"+divId);
	if(!div){
		return;
	}
	
	//
	var pageSize = page['pageSize'] ? page['pageSize'] : 0;
	//$("#hiddenPageSize").val(pageSize);

	var currentPage = page['currentPage'] ? page['currentPage'] : 1;
	//$("#hiddenCurrentPage").val(currentPage);

	var totalPageCnt = page['totalPageCnt'] ? page['totalPageCnt'] : 0;
	//$("#hiddenTotalPageCnt").val(totalPageCnt);

	var totalCnt = page['totalCnt'] ? page['totalCnt'] : 0;
	//$("#hiddenTotalCnt").val(totalCnt);
	
	//设置到面板上
	$(div).find("#emTotalCnt").html(totalCnt);
	$(div).find("#showCurrentPage").val(currentPage);
	$(div).find("#emTotalPageCnt").html(totalPageCnt);
}


/**
* 分页跳转的响应
* @param dir
* @param action
* @param formId
* @param divId
*/
function showListViewByPage(dir,action,formId,divId) {
	
	var form=$("#"+formId);
	var div=$("#"+divId);
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
		var userinput = $(div).find("#showCurrentPage").val();
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


//清空各访问标志
function resetVisitFlat(){
	
}