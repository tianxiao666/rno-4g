$(document).ready(
		function() {
		//tab选项卡
		tab("div_tab1", "li", "onclick");//项目服务范围类别切换
			initAreaCascade();

			//ncs记录查询
			$("#searchNcsDescBtn").click(function(){
				initFormPage("ncsDescDataForm");
				doQueryNcsDesc();
			});
			
			
			
			//点击某个详情按钮，显示相应的详情按钮
			tab("ncs_detail_tab", "li", "onclick");//
		});



//执行查询ncs
function doQueryNcsDesc(){
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


/**
 * 获取ncs小区测量信息
 */
function getNcsCellData(){
	//alert("查小区2！");
	getNcsRelativeData(
			"queryNcsCellDataByPageForAjaxAction",
			'NcsCellDataForm', 'queryCellDataTab',
			'NcsCellDataPageDiv','cell');
}
/**
 * 获取ncs ncell测量信息
 */
function getNcsNcellData(){
	getNcsRelativeData("queryNcsNcellDataByPageForAjaxAction",'NcsNCellDataForm','queryNCellResultTab','NcsNCellDataPageDiv','ncell');
}

/**
 * 获取干扰矩阵信息
 */
function getNcsInterferMatrixData(){
	getNcsRelativeData("queryNcsInterferMatrixByPageForAjaxAction",'NcsInterferMatrixDataForm','queryInterferMatrixResultTab','NcsInterferMatrixDataPageDiv','matrix');
}

/**
 * 获取簇
 * 
 */
function getNcsClusterData(){
	getNcsRelativeData("queryNcsClusterByPageForAjaxAction",'NcsClusterDataForm','queryClusterDataTab','NcsClusterDataPageDiv','cluster');
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
function getNcsRelativeData(url, formId, tabId, pageDivId,type) {
	$(".loading_cover").css("display", "block");
	$("#" + formId).ajaxSubmit({
		url : url,
		dataType : 'text',
		success : function(data) {
			// console.log(data);
			///alert("查回来了！");
			showNcsItemDetailQueryResult(data, tabId, formId, pageDivId,type);
		},
		complete : function() {
			$(".loading_cover").css("display", "none");
		}
	});

}


function showNcsItemDetailQueryResult(data, tabId, formId, pageDivId,type) {

	//alert("显示查回来的结果！");
	//var btntype = $("#btnFlag").val();
	//console.log("类别：" + type);
	data = eval("(" + data + ")");
	// console.log("data===" + data);
	// 准备填充ncs
	var page = data['page'];
	var ncslist = data['data'];

	// console.log("page===" + page);
	// if (page) {
	// var pageSize = page['pageSize'] ? page['pageSize'] : 0;
	// $("#hiddenPageSize").val(pageSize);
	//
	// var currentPage = page['currentPage'] ? page['currentPage'] : 1;
	// $("#hiddenCurrentPage").val(currentPage);
	//
	// var totalPageCnt = page['totalPageCnt'] ? page['totalPageCnt'] : 0;
	// $("#hiddenTotalPageCnt").val(totalPageCnt);
	//
	// var totalCnt = page['totalCnt'] ? page['totalCnt'] : 0;
	// $("#hiddenTotalCnt").val(totalCnt);
	//
	// // 跳转
	// $("#emTotalCnt").html(totalCnt);
	// $("#showCurrentPage").val(currentPage);
	// $("#emTotalPageCnt").html(totalPageCnt);
	//
	// }

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
	if (ncslist) {
		var one;
		var tr;
		for ( var i = 0; i < ncslist.length; i++) {
			// console.log("i==" + i);
			// CELL 小区中文名 LAC CI ARFCN BSIC TCH 操作
			one = ncslist[i];
			if (!one) {
				//console.log("one is null!");
				continue;
			}

			if (type == "cell") {

				tr = "<tr class=\"greystyle-standard-whitetr\"  align=\"center\">";
				tr += "<td>" + getValidValue(one['CELL']) + "</td>";
				tr += "<td>" + getValidValue(one['CHGR']) + "</td>";
				tr += "<td>" + getValidValue(one['REP']) + "</td>";
				tr += "<td>" + getValidValue(one['REPHR']) + "</td>";
				tr += "<td>" + getValidValue(one['REPUNDEFGSM']) + "</td>";
				tr += "<td>" + getValidValue(one['AVSS']) + "</td>";

				/*
				 * tr += "<td> <input type='checkbox' id='" +
				 * getValidValue(one['ID']) + "' class='forcheck' /></td>";
				 */

				tr += "</tr>";
			}
			else if (type == "ncell") {
				tr = "<tr class=\"greystyle-standard-whitetr\"  align=\"center\">";
				tr += "<td>" + getValidValue(one['CELL']) + "</td>";
				tr += "<td>" + getValidValue(one['NCELL']) + "</td>";
				tr += "<td>" + getValidValue(one['CHGR']) + "</td>";
				tr += "<td>" + getValidValue(one['BSIC']) + "</td>";
				tr += "<td>" + getValidValue(one['ARFCN']) + "</td>";
				tr += "<td>" + (one['DEFINED_NEIGHBOUR']==1?"是":"否")
						+ "</td>";
				tr += "<td>" + getValidValue(one['RECTIMEARFCN']) + "</td>";
				tr += "<td>" + getValidValue(one['REPARFCN']) + "</td>";
				tr += "<td>" + getValidValue(one['TIMES']) + "</td>";
				tr += "<td>" + getValidValue(one['NAVSS']) + "</td>";
				tr += "<td>" + getValidValue(one['TIMES1']) + "</td>";
				tr += "<td>" + getValidValue(one['NAVSS1']) + "</td>";
				tr += "<td>" + getValidValue(one['TIMES2']) + "</td>";
				tr += "<td>" + getValidValue(one['NAVSS2']) + "</td>";
				tr += "<td>" + getValidValue(one['TIMES3']) + "</td>";
				tr += "<td>" + getValidValue(one['NAVSS3']) + "</td>";
				tr += "<td>" + getValidValue(one['TIMES4']) + "</td>";
				tr += "<td>" + getValidValue(one['NAVSS4']) + "</td>";
				tr += "<td>" + getValidValue(one['TIMES5']) + "</td>";
				tr += "<td>" + getValidValue(one['NAVSS5']) + "</td>";
				tr += "<td>" + getValidValue(one['TIMES6']) + "</td>";
				tr += "<td>" + getValidValue(one['NAVSS6']) + "</td>";
				tr += "<td>" + getValidValue(one['TIMESRELSS']) + "</td>";
				tr += "<td>" + getValidValue(one['TIMESRELSS2']) + "</td>";
				tr += "<td>" + getValidValue(one['TIMESRELSS3']) + "</td>";
				tr += "<td>" + getValidValue(one['TIMESRELSS4']) + "</td>";
				tr += "<td>" + getValidValue(one['TIMESRELSS5']) + "</td>";
				tr += "<td>" + getValidValue(one['TIMESABSS']) + "</td>";
				tr += "<td>" + getValidValue(one['TIMESALONE']) + "</td>";
				tr += "<td>" + (one['DISTANCE']==10000000000?"":getValidValue(one['DISTANCE'],'',3)) + "</td>";
				tr += "<td>" + getValidValue(one['INTERFER'],'',6) + "</td>";

				/*
				 * tr += "<td> <input type='checkbox' id='" +
				 * getValidValue(one['RNO_NCS_ID']) + "' class='forcheck' /></td>";
				 */

				tr += "</tr>";
			}
			else if (type == "matrix") {
				tr = "<tr class=\"greystyle-standard-whitetr\"  align=\"center\">";
				tr += "<td>" + getValidValue(one['CELL']) + "</td>";
				tr += "<td>" + getValidValue(one['INTERFERENCE_CELL'])
						+ "</td>";
				tr += "<td>" + getValidValue(one['CI'],'',6) + "</td>";
				tr += "<td>" + getValidValue(one['CA'],'',6) + "</td>";
				tr += "<td>" + (one['IS_NEIGHBOUR']==1?'是':'否') + "</td>";
				tr += "<td>" +(one['DISTANCE']==10000000000?"":getValidValue(one['DISTANCE'],'',3)) + "</td>";

				/*
				 * tr += "<td> <input type='checkbox' id='" +
				 * getValidValue(one['INTER_ID']) + "' class='forcheck' /></td>";
				 */

				tr += "</tr>";

			}else if (type == "cluster") {
				//alert("cluster");
				tr = "<tr class=\"greystyle-standard-whitetr clusterCls\"  align=\"center\">";
				tr += "<td>" + getValidValue(one['ID']) + "</td>";
				tr += "<td>" + getValidValue(one['CONTROL_FACTOR'],'',5)
						+ "</td>";
				tr += "<td>" + getValidValue(one['WEIGHT'],'',5) + "</td>";
				tr += "<td><input type='button' value='查看簇内小区' onclick='viewClusterCell(this,\""+one['ID']+"\")'/></td>";
				
				/*
				 * tr += "<td> <input type='checkbox' id='" +
				 * getValidValue(one['INTER_ID']) + "' class='forcheck' /></td>";
				 */

				tr += "</tr>";
			}
			
			// console.log("tr===" + tr);
			table.append($(tr));// 增加
		}
	}

}

/**
 * 查看簇内小区详情
 */
/*function viewClusterCell(obj,clusterId){
	$(".loading_cover").css("display", "block");
	var td=$(obj).closest("td");
	//旧：queryNcsClusterCellForAjaxAction
	$.ajax ({
		url : 'queryNcsClusterCellForAjaxAction',
		data:{'clusterId':clusterId},
		type:'post',
		dataType : 'text',
		success : function(rawdata) {
			// console.log(data);
			///alert("查回来了！");
			//
//			console.log("详情："+rawdata);
			var cells="";
			var cellstr="";
			try{
				var data=eval(rawdata);
				var one=null;
				for(var i=0;i<data.length;i++){
					one=data[i];
					if(!one){
						continue;
					}
					cells+=getValidValue(one['CELL'])+"  ";
				}
				cells +="<input type='button' id='"+clusterId+"' name='interferQueryBtn' value='查询干扰度'/ onclick='queryInterfer(this)'>";
				td.html(cells);//替换掉按钮
			}catch(err){
				
			}
			
		},
		complete : function() {
			$(".loading_cover").css("display", "none");
		}
	});
}*/
/**
 * 查看簇内小区详情
 * @param {} obj
 * @param {} clusterId
 */
function viewClusterCell(obj,clusterId){
	$(".loading_cover").css("display", "block");
	var td=$(obj).closest("td");
		$("#clusterresTab").html("");
    var tr=$(obj).closest("tr");
    $(".clusterCls").css("background-color","");
    $(tr).css("background-color","#3399FF");
	//旧：queryNcsClusterCellForAjaxAction
	$.ajax ({
		url : 'queryNcsClusterCellAndOutputEachOtherInterValueForAjaxAction',
		data:{'clusterId':clusterId},
		type:'post',
		dataType : 'text',
		success : function(rawdata) {
		
//			console.log("详情："+rawdata);
			var cells="";
			var cellstr="";
			var cellinterstr="";
			var data="";
			var tr="";
			try{
			    data=eval("("+rawdata+")");
//			    data=eval(rawdata);
//				console.log(data);
			var clustercells=	data['clustercells'];
			
//				console.log(clustercells);
			var ncscellinter=data['ncscellinter'];
//			 console.log(ncscellinter);
				var one=null;
				for(var i=0;i<clustercells.length;i++){
					one=clustercells[i];
					if(!one){
						continue;
					}
					cells+=getValidValue(one['CELL'])+"  ";
				}
//				console.log("cells:"+cells);
				tr+="<tr><td colspan=\"3\" align=\"left\">簇内小区："+cells+"</td> </tr>";
				  tr+="<tr align=\"center\">"
										   +" <td>小区</td>"
										   +" <td>邻区</td>"
										   +"<td>干扰度</td>" 
										  +"</tr>";
				
//				cells +="<input type='button' id='"+clusterId+"' name='interferQueryBtn' value='查询干扰度'/ onclick='queryInterfer(this)'>";
//				td.html(cells);//替换掉按钮
				for(var i=0;i<ncscellinter.length;i++){
					one=ncscellinter[i];
					if(!one){
						continue;
					}
					
					cellinterstr+=getValidValue(one['CELL'])+getValidValue(one['NCELL'])+getValidValue(one['INTERFER'])+"  ";
					tr+="<tr align=\"center\">"
										   +"<td>"+getValidValue(one['CELL'])+"</td>" 
										    +"<td>"+getValidValue(one['NCELL'])+"</td>"
										    +"<td>"+getValidValue(one['INTERFER'],'',8)+"</td>"
										  +"</tr>";
					}
//			console.log("cellinterstr:"+cellinterstr);						  
				$("#clusterresTab").html(tr);
			}catch(err){
				//console.log(err);
			}
			
		},
		complete : function() {
			$(".loading_cover").css("display", "none");
		}
	});
}
/*function queryInterfer(obj){
	//dom对象－>jquery对象
	var clusterId=$(obj).attr("id");
//	console.log(clusterId);
//	console.log($("input[name='interferQueryBtn']").attr("id"));
	var url="queryNcsClusterCellAndOutputEachOtherInterValueForAjaxAction";
	var sendData={'clusterId':clusterId};
		$(".loading_cover").css("display", "block");
	$.ajax({
		url : url,
		data:sendData,
		dataType : "json",
		type : 'post',
		success : function(data) {
//			bindLoadedNcsList(data);
			console.log(data);
		},
		complete : function() {
			$(".loading_cover").css("display", "none");
		}
	});
}*/


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
	
	//console.log("setFormPageInfo . pageSize="+page.pageSize+",currentPage="+page.currentPage+",totalPageCnt="+page.totalPageCnt+",totalCnt="+page.totalCnt);
	form.find("#hiddenPageSize").val(page.pageSize);
	form.find("#hiddenCurrentPage").val(new Number(page.currentPage));///
	form.find("#hiddenTotalPageCnt").val(page.totalPageCnt);
	form.find("#hiddenTotalCnt").val(page.totalCnt);
	
	//alert("after set currentpage in form is :"+form.find("#hiddenCurrentPage").val());
}

/*
* 显示查询的ncs记录信息
* data:包含page信息和查询回来的数据信息
*/
function showNcsQueryResult(rawdata){
	//table id ：queryNcsResultTab
	
	
	var data=eval("("+rawdata+")");
	
	var table=$("#queryNcsResultTab");
	
	//id
	var id='RNO_NCS_DESC_ID';
	
	//忽略的子段
	var obmit=new Array();
	obmit.push("INTERFER_MATRIX_ID");
	obmit.push("AREA_ID");
	obmit.push("RECORD_COUNT");
	obmit.push("NET_TYPE");
	obmit.push("CREATE_TIME");
	obmit.push("MOD_TIME");
	obmit.push("STATUS");
	
	//alias
	var alias=new Object();
	alias['START_TIME']='开始测量时间';
	alias['RECORD_COUNT']='';
	
	if(data==null || data==undefined){
	
		return;
	}
	
	var list=data['data'];
	var th="<tr><th>选择</th><th>文件名称</th><th>BSC</th><th>频段</th><th>SEGTIME</th><th>ABSS</th><th>NUMFREQ</th><TH>RECTIME</TH>" +
			"<TH>FILE_FORMAT</TH><TH>RID</TH><TH>TERM_REASON</TH><TH>ECNOABSS</TH> <TH>RELSS</TH>" +
			" <TH>RELSS2</TH> <TH>RELSS3</TH> <TH>RELSS4</TH>" +
			" <TH>RELSS5</TH><th>操作</th></tr>" ;
	var html="";
	html+=th;
	
	var tr="";
	var one="";
	for(var i=0;i<list.length;i++){
		tr="<tr>";
		one=list[i];
		//tr+="<td><input type='checkbox' class='forcheck' id='"+one['RNO_NCS_DESC_ID']+"' /></td>";
		tr+="<td><input type='checkbox' class='forcheck' id='"+one['RNO_NCS_DESC_ID']+"' onclick=\"loadAndShowNcsCellInteferenceAnalysisList(this)\" /></td>";
		tr+="<td>"+getValidValue(one['NAME'],'未知文件名')+"</td>";
		tr+="<td>"+getValidValue(one['BSC'],'未知BSC')+"</td>";
		tr+="<td>"+getValidValue(one['FREQ_SECTION'],'未知频段')+"</td>";
		tr+="<td>"+getValidValue(one['SEGTIME'],'')+"</td>";
		tr+="<td>"+getValidValue(one['ABSS'],'')+"</td>";
		tr+="<td>"+getValidValue(one['NUMFREQ'],'')+"</td>";
		tr+="<td>"+getValidValue(one['RECTIME'],'')+"</td>";
		tr+="<td>"+getValidValue(one['FILE_FORMAT'],'')+"</td>";
		tr+="<td>"+getValidValue(one['RID'],'')+"</td>";
		tr+="<td>"+getValidValue(one['TERM_REASON'],'')+"</td>";
		tr+="<td>"+getValidValue(one['ECNOABSS'],'')+"</td>";
		tr+="<td>"+getValidValue(getRelssval(one['RELSS_SIGN'],one['RELSS']),'')+"</td>";
		tr+="<td>"+getValidValue(getRelssval(one['RELSS2_SIGN'],one['RELSS2']),'')+"</td>";
		tr+="<td>"+getValidValue(getRelssval(one['RELSS3_SIGN'],one['RELSS3']),'')+"</td>";
		tr+="<td>"+getValidValue(getRelssval(one['RELSS4_SIGN'],one['RELSS4']),'')+"</td>";
		tr+="<td>"+getValidValue(getRelssval(one['RELSS5_SIGN'],one['RELSS5']),'')+"</td>";
		tr+="<td><input type='button' onclick=\"viewNcsDetail('"+one['RNO_NCS_DESC_ID']+"') \" value='查看详情'/></td>";
		
		tr+="</tr>";
		html+=tr;
		
	}
	
	table.html(html);
	
	//设置隐藏的page信息
	setFormPageInfo("ncsDescDataForm",data['page']);
	
	//设置分页面板
	setPageView(data['page'],"ncsResultPageDiv");
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
* 将选择的ncs作为数据范围，计算相应的区域结构信息
*/
function analysisAreaStructure(){
	var ids = new Array();
	var trs = new Array();
	$("#queryNcsResultTab").find("input.forcheck:checked").each(function(i, ele) {
		ids.push($(ele).attr("id"));
		// trs.push($(ele).closest("tr"));
	});

	if (ids.length == 0) {
		// layer.msg("请先选择需要删除的邻区关系",2,2);
		alert('请先选择ncs');
		return;
	}

	var ncsids = ids.join(",");
	$.ajax({
		url:'queryAreaDamageDataForAjaxAction',
		data:{'ncsIds':ncsids},
		type:'post',
		dataType:'text',
		success:function(rawdata){
			try{
				data=eval("("+rawdata+")");
				$("#areaStructDiv").css("display","block");
				var tab=$("#queryAreaStructureResultTab");
				
				var html="";
				var head="<tr><th>区域名称</th><th>区域破坏系数</th><th>归一化干扰水平</th></tr>";
				var tr;
				html+=head;
				
				var one=null;
				for(var i=0;i<data.length;i++){
					one=data[i];
					if(!one){
						continue;
					}
					tr="";
					tr+="<tr>";
					tr+="<td>"+one['AREA_NAME']+"</TD>";
					tr+="<td>"+one['DAMAGE']+"</td>";
					tr+="<td>"+one['NORMALIZE']+"</td>";
					tr+="</tr>";
					
					html+=tr;
				}
				
				tab.html(html);
			}catch(err){
				
			}
		}
	})
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
* 查看ncs的详情
* @param ncsId
*/
function viewNcsDetail(ncsId){
	resetVisitFlat();
	//将这个ncsid赋予所有的隐藏域
	$(".hiddenNcsIdCls").val(ncsId);
	
	//查小区
	initFormPage('NcsCellDataForm');
	getNcsCellData();
	
	//查邻区
	initFormPage('NcsNCellDataForm');
	getNcsNcellData();
	
	//查干扰矩阵
	initFormPage('NcsInterferMatrixDataForm');
	getNcsInterferMatrixData();
	
	//查看簇
	initFormPage('NcsClusterDataForm');
	getNcsClusterData();
	$("#div_tab ul li").eq(1).click();
}

//清空各访问标志
function resetVisitFlat(){
	
}