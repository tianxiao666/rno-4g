var cellParams = ['BCCH','BSIC','ATT','ACC','CB','CBQ','CCHPWR','MAXRET','NCCPERM','T3212','QCOMPDL','PTIMTEMP',
			 'QLENSD','TO','TX','QDESDL','MISSNM','SSDESDLAFR','SSDESDLAHR','QEVALSD','QEVALSI','SSEVALSD','PTIMHF','PTIMTA',
			'SSEVALSI','SCHO','SSRAMPSD','SSRAMPSI','RLINKUP','CELLQ','RLINKT','AW','RLINKTAFR','PSSTEMP','PTIMBQ','RLINKTAHR',
		 	 'NECI','DHA','CLS_STATUS','CLSLEVEL','SCLD','SCLDSC','CLSACC','FNOFFSET','BSRXMIN','QLENSI','HOCLSACC','ACTIVEMBCCHNO','IDLEMBCCHNO',
		 	 'DMPR','SSDESULAFR','SSDESULAHR','ACSTATE','QCOMPUL','QDESUL','BSPWR','QLIMDL','SLEVEL','LIMIT1','STIME',
			'XRANGE','LIMIT2','BTSPS','LIMIT3','LIMIT4','BTSPSHYST','BSTXPWR','QLIMUL','AGBLK','BCCHTYPE','ECSC','DTXD','RLINKUPAFR',
			 'RLINKUPAHR','DTXU','C_SYS_TYPE','MBCR','CRO','SSDESDL','IRC','HCSIN','CHCSDL','HCSOUT','LA','CSPSALLOC','CSPSPRIO','GPRSSUP',
			'MPDCH','LAYER','SSDESUL','PSKONBCCH','SCALLOC','BSPWRB','BSPWRT','MSTXPWR','CHAP','DTMSTATE','PRIMPLIM','MSRXMIN',
			'ACCMIN','IHO','MAXIHO','TIHO','TMAXIHO','SSOFFSETUL','SSOFFSETDL','QOFFSETUL','QOFFSETDL','SSOFFSETULAFR','PT','SSLENSD',
		 	'SSOFFSETDLAFR','QOFFSETULAFR','QOFFSETDLAFR','CLSRAMP','MAXTA','LAYERTHR','LAYERHYST','BSRXSUFF','MSRXSUFF','DYNMSPWR_STATE',
			'SSLENSI','QLIMULAFR','QLIMDLAFR','TALIM','FLEXHIGHGPRS','PDCHPREEMPT','TBFDLLIM','TBFULLIM','BSPWRMIN','ISHOLEV','LAC','CRH',
			'FPDCH','DYNBTSPWR_STATE','FASTMSREG','GAMMA','GPRSPRIO','HYSTSEP','LCOMPDL','LCOMPUL','MFRMS','CI','PSSBQ','PSSHF','PSSTA'];
			
var chgParams = ['CHGR_STATE','CHGR_TG','BAND','BCCD','CBCH','CCCH','EACPREF','DCHNO_32','ETCHTN','EXCHGR','HOP','HOPTYPE','HSN','MAIO',
			'NUMREQBPC','NUMREQCS3CS4BPC','NUMREQE2ABPC','NUMREQEGPRSBPC','ODPDCHLIMIT'];

var neightbourParams = ['AWOFFSET','BQOFFSET','BQOFFSETAFR','BQOFFSETAWB','CAND','CS','GPRSVALID','HIHYST','KHYST','KOFFSET',
			'LHYST','OFFSET','LOHYST','PROFFSET','TRHYST','TROFFSET','USERDATA'];

var interval = 2000;// 周期性获取文件导出进度情况的

$(document).ready(function() {
	//加载bsc树形菜单
	getAllBscCell();
	//加载参数树形菜单
	changeParam();
	
	//参数对比点击事件
	$("#compareBtn").click(function(){
		paramCompare();
	});
	
	//数据导出点击事件
	$("#exportBtn").click(function(){
		exportData();
	});
	
	// 区域联动事件
	$("#provinceId").change(function() {
		getSubAreas("provinceId", "cityId", "市");
	});
	$("#cityId").change(function() {
		// 清除bsc列表
		$("#allBsc").html("");
		// 重新加载bsc列表
		getAllBscCell();
	});
	
	//自适应宽度
	var tdWidth = $("#contentTab tr:eq(0) td:nth-child(1)").width(); 
	$("#paramDiffDiv").css({
		'width' : tdWidth+"px"
	});
	$("#paramDiffDetailDiv").css({
		'width' : tdWidth+"px"
	});
});

function getAllBscCell(cityId) {
	var cityId = $("#cityId").val();
	showOperTips("loadingDataDiv", "loadContentId", "正在加载BSC");
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
				html += "<li><input name='bscChk'  type='checkbox'/><span>全选</span><ul>";
				for(var i=0; i<bscList.length; i++) {
					html +="<li><input name='bsc' value='"+bscList[i].BSC_ID+"' type='checkbox'/> <span>"+bscList[i].ENGNAME+"</span></li>";
				}
					html += "</ul></li>";
				$("#allBsc").html(html);
				$("#allBsc").treeview();
				//加入关联选择事件
				$("input[name='bscChk']").click(function(){
					 var checkedValue = this.checked;
					 $(this).parent("li").find("input[name='bsc']").attr("checked",checkedValue);
				 });
			} else {
				$("#allBsc").html("该城市下不存在BSC");
			}
		},
		complete:function(){
			hideOperTips("loadingDataDiv");
		}
	});
}

function exportData() {
	
	$("#progressDesc").html("");
	
	//获取城市id
	var cityId = $("#cityId").val();
	//获取参数类型
	var paramType = $('input[name="paramType"]:checked').val();
	//获取所选择的参数
	var paramStr = "";
	$('input:checkbox[name="params"]:checked').each(function(){  
		paramStr += $(this).val()+",";  
	});
	paramStr = paramStr.substring(0, paramStr.length - 1);
	//获取所选择的BSC
	var bscStr = "";
	$('input:checkbox[name="bsc"]:checked').each(function(){  
		bscStr += $(this).val()+",";  
	});
	bscStr = bscStr.substring(0, bscStr.length - 1);
	//获取第一个日期
	var date1 = $("#date1").val();
	//获取第二个日期
	var date2 = $("#date2").val();
	
	//验证
	if(paramType == undefined || paramType == "") {
		alert("请选择参数类型！");
		return;
	}
	if(paramStr == undefined || paramStr == "") {
		alert("请选择需要对比的参数！");
		return;
	}
	if(bscStr == undefined || bscStr == "") {
		alert("请选择需要对比的BSC！");
		return;
	}
	if(date1 == date2) {
		alert("选择比较的两个日期是同一天！");
		return;
	}
	showOperTips("loadingDataDiv", "loadContentId", "正在执行导出");
	$.ajax({
		url : 'exportEriCellCompareDataAjaxForAction',
		data : {
			'cityId' : cityId,
			'paramType' : paramType,
			'paramStr' : paramStr,
			'bscStr' : bscStr,
			'date1' : date1,
			'date2' : date2
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

/**
 * 参数对比点击事件
 */
function paramCompare() {
	
	//清空	
	$("#paramDiffTable").html("");
	$("#paramDiffDetailTable").html("");
	$("#paramName").html("");
	
	//获取城市id
	var cityId = $("#cityId").val();
	//获取参数类型
	var paramType = $('input[name="paramType"]:checked').val();
	//获取所选择的参数
	var paramStr = "";
	$('input:checkbox[name="params"]:checked').each(function(){  
		paramStr += $(this).val()+",";  
	});
	paramStr = paramStr.substring(0, paramStr.length - 1);
	//获取所选择的BSC
	var bscStr = "";
	$('input:checkbox[name="bsc"]:checked').each(function(){  
		bscStr += $(this).val()+",";  
	});
	bscStr = bscStr.substring(0, bscStr.length - 1);
	//获取第一个日期
	var date1 = $("#date1").val();
	//获取第二个日期
	var date2 = $("#date2").val();
	
	//验证
	if(paramType == undefined || paramType == "") {
		alert("请选择参数类型！");
		return;
	}
	if(paramStr == undefined || paramStr == "") {
		alert("请选择需要对比的参数！");
		return;
	}
	if(bscStr == undefined || bscStr == "") {
		alert("请选择需要对比的BSC！");
		return;
	}
	if(date1 == date2) {
		alert("选择比较的两个日期是同一天！");
		return;
	}
	//console.log("date1="+date1+"   date2="+date2);
	//console.log("cityId:"+cityId+"  paramType："+paramType+"  bsc:"+bscStr+"  param:"+paramStr);
	//禁止重复点击
	$(this).attr("disabled","disabled");
	showOperTips("loadingDataDiv", "loadContentId", "正在加载对比数据");
	$.ajax({
		url : 'eriCellParamsCompareForAjaxAction',
		data : {
			'cityId' : cityId,
			'paramType' : paramType,
			'paramStr' : paramStr,
			'bscStr' : bscStr,
			'date1' : date1,
			'date2' : date2
		},
		type : 'post',
		dataType : 'text',
		success : function(raw) {
			
			var data = eval("("+raw+")");
			var flag = data['flag'];
			var result = data['result'];
			
			if(flag) {
				if(result) {
					var paramTitle = paramStr.split(",");
					var html = "<tr><th>BSC</th>";
					for(var i=0; i<paramTitle.length; i++) {
						html +="<th>" + paramTitle[i] + "</th>";
					}
					html += "</tr>";
					var title;
					var one;
					for(var i=0; i<result.length; i++) {
						html +="<tr>";
						html += "<td>" + result[i]['BSC_ENGNAME'] + "</td>";
					    for(var j=0; j<paramTitle.length; j++) {
					    	title = paramTitle[j];
					    	if(Number(result[i][title]) > 0) {
					    		html += "<td style='background:red; cursor:pointer' " +
					    					" onclick='getParamDiffDetail(\""+result[i]['BSC_ENGNAME']+"\",\""
					    							+paramType+"\",\""+title+"\",\""+cityId+"\",\""+date1+"\",\""+date2+"\")'>" 
					    				+ result[i][title] + "</td>";
					    	} else {
					    		html += "<td>" + result[i][title] + "</td>";
					    	}
					    }
						html += "</tr>";
					}
					$("#paramDiffTable").html(html);
				}
			} else {
				alert(result);	
			}
		},
		complete:function(){
			//解除禁止点击
			$("#compareBtn").removeAttr("disabled");
			hideOperTips("loadingDataDiv");
		}
	});
	
}

/**
 * 获取参数对比详情
 */
function getParamDiffDetail(bsc,paramType,param,cityId,date1,date2) {
	showOperTips("loadingDataDiv", "loadContentId", "正在加载详情");
	
	$.ajax({
		url : 'getEriCellParamsDiffDetailForAjaxAction',
		data : {
			'bsc' : bsc,
			'paramType' : paramType,
			'param' : param,
			'cityId' : cityId,
			'date1' : date1,
			'date2' : date2
		},
		type : 'post',
		dataType : 'text',
		success : function(raw) {
			var data = eval("("+raw+")");
			var flag = data['flag'];
			$("#paramDiffDetailTable").html("");
			$("#paramName").html("");
			if(flag) {
				var result = data['result'];
				if(result) {
					var html = "";
					if(paramType=="cell"){
						html += "<tr><th>BSC</th><th>CELL</th><th>"+date1+"</th><th>"+date2+"</th></tr>";
						for(var i=0; i<result.length; i++) {
							html +="<tr>";
							html += "<td>" + bsc + "</td>";
							html += "<td>" + getValidValue(result[i]['CELL']) + "</td>";
						    html += "<td>" + getValidValue(result[i]['PARAM1']) + "</td>";
						    html += "<td>" + getValidValue(result[i]['PARAM2']) + "</td>";
							html += "</tr>";
						}
					}else if(paramType=="channel"){
						html += "<tr><th>BSC</th><th>CELL</th><th>CHGR</th><th>"+date1+"</th><th>"+date2+"</th></tr>";
						for(var i=0; i<result.length; i++) {
							html +="<tr>";
							html += "<td>" + bsc + "</td>";
							html += "<td>" + getValidValue(result[i]['CELL']) + "</td>";
							html += "<td>" + getValidValue(result[i]['CH_GROUP']) + "</td>";
						    html += "<td>" + getValidValue(result[i]['PARAM1']) + "</td>";
						    html += "<td>" + getValidValue(result[i]['PARAM2']) + "</td>";
							html += "</tr>";
						}						
					}else if(paramType=="neighbour"){
						html += "<tr><th>BSC</th><th>CELL</th><th>NCELL</th><th>"+date1+"</th><th>"+date2+"</th></tr>";
						for(var i=0; i<result.length; i++) {
							html +="<tr>";
							html += "<td>" + bsc + "</td>";
							html += "<td>" + getValidValue(result[i]['CELL']) + "</td>";
							html += "<td>" + getValidValue(result[i]['N_CELL']) + "</td>";
						    html += "<td>" + getValidValue(result[i]['PARAM1']) + "</td>";
						    html += "<td>" + getValidValue(result[i]['PARAM2']) + "</td>";
							html += "</tr>";
						}						
					}
					$("#paramName").html(param);
					$("#paramDiffDetailTable").html(html);
				}
			}
		},
		complete:function(){
			hideOperTips("loadingDataDiv");
		}
	});
}

/*
 * 改变参数类型
 */
function changeParam() {
	$("#paramCheckBox").html("");
	
	var paramType = $('input[name="paramType"]:checked').val();
	var paramHtml;
	if(paramType == "cell") {
		paramHtml = strToHtmlByParams(cellParams);
	} else if(paramType == "channel") {
		paramHtml = strToHtmlByParams(chgParams);
	}else if(paramType == "neighbour") {
		paramHtml = strToHtmlByParams(neightbourParams);
	}
	
	$("#paramCheckBox").html(paramHtml);
	//console.log(paramHtml);
	
	//绑定树形菜单样式
	$("#paramCheckBox").treeview({
		collapsed : true
	});
	
	//加入关联选择事件
	$("input[name='pmChk']").click(function(){
		 var checkedValue = this.checked;
		 $(this).parent("li").find("input[name='pmChk']").attr("checked",checkedValue);
	 });
	$("input[name='pmChk']").click(function(){
		 var checkedValue = this.checked;
		 $(this).parent("li").find("input[name='params']").attr("checked",checkedValue);
	 });
}

/**
 * 将参数类型数组转成ul格式的html字符串
 * 并以首字母为分组，排序
 */
function strToHtmlByParams(params) {
	var html = "";
	//将字符串按字母排序
	params.sort();
	//获取原数组中的元素首字母，作为一个新数组，并去掉重复
	var ul=[];
	var a;
	for(var i=0; i<params.length; i++) {
		a = params[i].substring(0,1);
		ul.push(a);
	} 
	ul = unique(ul);
	//拼接html
	html += "<li><input name='pmChk' checked='checked' type='checkbox'/><span>全选</span><ul>";
	for(var i=0; i<ul.length; i++) {
		html +="<li><input name='pmChk' checked='checked' type='checkbox'/> <span>"+ul[i]+"</span><ul>";
		for(var j=0; j<params.length; j++) {
			if(ul[i] == params[j].substring(0,1)) {
					html +="<li><input name='params' checked='checked' value='"+params[j]+"' type='checkbox'/> <span>"+params[j]+"</span></li>";
			}
		}
		html +="</ul></li>";
	}
	html += "</ul></li>";
	return html;
}

/**
 * 返回元素不重复的数组
 */
function unique(arr) {
 var result = [], hash = {};
    for (var i = 0, elem; (elem = arr[i]) != null; i++) {
        if (!hash[elem]) {
            result.push(elem);
            hash[elem] = true;
        }
    }
    return result;
}