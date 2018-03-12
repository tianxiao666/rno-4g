/**
 * 本模块负责完成
 * 1、获取、展现小区配置 小区干扰分析列表
 * 2、选择、反选分析列表，确定真正需要分析的数据范围
 * 3、根据所选择的分析列表，加载小区到地图展示
 * 4、进行相关统计，如频率复用 
 * 5、频率复用分布标记
 * 6、维持session不超时
 */

/**
 * ======================================================= 变量定义开始
 * =======================================================
 */

var gisCellDisplayLib;// 负责显示小区的对象
var map;

var analysisList = new Array();// 分析列表
var allCellStsResults = new Object();// 所有的小区信息列表,key是小区名，value是CellStsResult对象

var currentSelConfigId = "";// 当前选择的用于分析的分析列表配置id
var currentSelType = "";//当前选择类型 小区配置 小区干扰
var currentSelIsTemp = "";// 当前选择是否临时配置
var currentloadtoken = "";// 加载任务的token

var minZoom = 15;// 只有大于 该 缩放级别，才真正
var randomShowCnt = 50;// 在不需要全部显示的时候，最大随机显示的个数

var onLoadingGisCell=false;//是否正在加载小区信息

var loadingLayerId=null;//加载层的id

var specRenderderLabel = new Array();// 频点搜索出来后用于显示的label


/**
 * ======================================================= 变量定义结束
 * =======================================================
 */

$(document).ready(function() {

	bindNormalEvent();
	
	initAreaCascade();
	
	gisCellDisplayLib = new GisCellDisplayLib(map, minZoom, randomShowCnt);
	var lng = $("#hiddenLng").val();
	var lat = $("#hiddenLat").val();
	gisCellDisplayLib.initMap("map_canvas", lng, lat);
    map = gisCellDisplayLib.getMap();
	
	 
	loadAndShowCellConfigAnalysisList();//小区配置
	//loadAndShowCellInteferenceAnalysisList();//小区干扰
			$("#trigger").click(function(){
	 	 //console.log("glomapid:"+glomapid);
	 	 if(glomapid=='null' || glomapid=='baidu'){
//	 	  console.log("切换前是百度");
		  $(this).val("切换百度");
//		  console.log("切换后是谷歌");
		  sessId="google";
	 	 }else{
//	 	 	console.log("切换前是谷歌");
		 	$(this).val("切换谷歌");
//		 	console.log("切换后是百度");
		 	sessId="baidu";
	 	 }
		 storageMapId(sessId);
		 });
});
/**
 * 通过session存储地图ID
 */
function storageMapId(mapid){
	$(".loading_cover").css("display", "block");
	$.ajax({
		url : 'storageMapIdBySessForAjaxAction',
		data : {
			'mapId' : mapid
		},
		dataType : 'json',
		type : 'post',
		success : function(data) {
			glomapid=data;
			//console.log("data:"+data);
//			var c = data;
			try{
//			initRedirect("initGisCellDisplayAction");
			window.location.href=window.location.href;
			}catch(err){
			
			}
		},
		error : function(xhr, textstatus, e) {

		},
		complete : function() {
			$(".loading_cover").css("display", "none");
		}
	});
}

/**
 * 区域联动事件
 */
function initAreaCascade() {
	// 区域联动事件
	$("#provinceId").change(function() {
		getSubAreas("provinceId", "cityId", "市");
	});
	$("#cityId").change(
			function() {
				getSubAreas("cityId", "queryCellAreaId", "区/县", function(data) {
					$("#hiddenAreaLngLatDiv").html("");
					$("#hiddenLng").val("");
					$("#hiddenLat").val("");
					// console.log("data===" + data.toSource());
					if (data) {
						var html = "";
						for ( var i = 0; i < data.length; i++) {
							var one = data[i];
							html += "<input type=\"hidden\" id=\"areaid_"
									+ one['area_id'] + "\" value=\""
									+ one["longitude"] + "," + one["latitude"]
									+ "\" />";
							// console.log("lng="+one["longitude"]);
							// console.log("lat="+one["latitude"]);
							if (i == 0) {
								$("#hiddenLng").val(one["longitude"]);
								$("#hiddenLat").val(one["latitude"]);
							}
						}
						$("#hiddenAreaLngLatDiv").append(html);
					}

					$("#queryCellAreaId").trigger("change");
				});
			});

	$("#queryCellAreaId").change(function() {
		var lnglat = $("#areaid_" + $("#queryCellAreaId").val()).val();
		//console.log("改变区域:" + lnglat);
		// 清除当前区域的数据
//		clearAllData();

		if (lnglat) {
			// 地图中心点
			var lls = lnglat.split(",");
			if (lls[0] == 0 || lls[1] == 0) {
				//console.warn("未设置该区域的中心点经纬度。");
			} else {
				// 地图移动
				gisCellDisplayLib.panTo(lls[0], lls[1]);
			}
		}
	});
}

/**
 * 清空所有的数据
 */
function clearAllData() {
	// 清空数组
	allCellStsResults=null;
	allCellStsResults=new Object();
	// 清空地图
	clearMarkCellForFreqReuse();//清除之前频点分布标记
	// 清空地图数据
	gisCellDisplayLib.clearData();// 清空地图上的东西
	
}

// 初始化分页参数
function initPageParam() {
	$("#hiddenPageSize").val(100);
	$("#hiddenCurrentPage").val(1);
	$("#hiddenTotalPageCnt").val(0);
	$("#hiddenTotalCnt").val(0);
	$("#hiddenForcedStartIndex").val(-1);
}

/**
 * 绑定普通事件
 */
function bindNormalEvent() {

	// 加载列表中对应的“移除”按钮，事件
	$(".removebtn").live('click', function() {
		// 是否被选中
		var isSelectedNow = false;
		//var tr = $(this).parent().parent();
		var tr=$(this).closest("tr");
		var loadedConfigId = $(this).siblings(":first").val();
		if (tr.find("input.forselect").attr("checked")) {
			isSelectedNow = true;
		}
		var type=$(this).attr("name");
		if(type=="cellconfig"){
			type="CELLDATA";
		}else{
			type="INTERFERENCEDATA"
		}

		$.ajax({
			url : 'removeCellAnalysisItemFromListForAjaxAction',
			data : {
				'loadedConfigId' : loadedConfigId,'type':type
			},
			dataType : 'text',
			type : 'post',
			success : function(data) {
				tr.remove();
			}
		});

		if (isSelectedNow) {
			// 需要重新获取小区
			changeAnalysisListSelection();
		}
	});


	
	// 从小区干扰加载列表中，选择若干列表进行分析
	$("#confirmSelectionAnalysisBtn").click(function() {
		// 触发改变的事件
		changeAnalysisListSelection();
	});
	
	
	//2013-12-9 gmh add
	$("#showCellBtn").click(function(){
		//确定加载某个配置的小区到地图
		var id = $("input[name='cellconfigradio']:checked").val();
		if(!id){
			animateInAndOut("operInfo",500,500,1000,"operTip","请先选择一个小区配置");
			return;
		}
		clearAllData();
		initPageParam();//恢复分页参数
		currentSelConfigId=id;//
		showOperTips("operInfo","operTip","正在加载小区。。。");
		currentloadtoken = getLoadToken();
		loadGisCellData(currentloadtoken,id);
	});

}

/**
 * 获取和展现被加载的小区配置分析列表
 * 
 */
function loadAndShowCellConfigAnalysisList() {
	$("#analysisListTable_cellconfig").empty();
	$.ajax({
		url : "getCellConfigAnalysisListForAjaxAction",
		dataType : 'json',
		type : 'post',
		success : function(data) {
			var htmlstr = "";
			htmlstr = getCellConfigAnaliysisHtml(data);
			$("#analysisListTable_cellconfig").append(htmlstr);
			if(htmlstr!=""){
				$("#showCellBtnDiv").show();
			}
		}
	});
}
/**
 * 获取和展现被加载的小区干扰分析列表
 */
function loadAndShowCellInteferenceAnalysisList() {
	$("#analysisListTable_cellinterference").empty();
	$.ajax({
		url : "getCellInterferenceAnalysisListForAjaxAction",
		dataType : 'json',
		type : 'post',
		success : function(data) {
			var htmlstr = getCellInterferenceAnaliysisHtml(data);
			$("#analysisListTable_cellinterference").append(htmlstr);
			if(htmlstr!=""){
				$("#analysisBtnDiv").show();
			}
		}
	});
}
/**
 * 获取小区配置html
 * @param {} data
 */
function getCellConfigAnaliysisHtml(data){
	if (!data) {
		return;
	}
	var htmlstr = "";
	var trClass="";
	for ( var i = 0; i < data.length; i++) {
		if(i%2==0){
			trClass="tb-tr-bg-coldwhite";
		}else{
			trClass="tb-tr-bg-warmwhite";
		}
		var tempType="";
		htmlstr +="<tr class=\""+trClass+"\">"//table 内容  
					+ "  <td width=\"45%\" class=\"bd-right-white\" >    "
					+ "  <span >"
					+data[i]['title']
					+"</span>"
					+ "  </td>"
					+ "  <td width=\"45%\" class=\"bd-right-white\" >    "
					+ "  <span >"
					+data[i]['name']
					+"</span>"
					+ "  </td>"
					+ "  <td  width=\"20%\"  class=\"td-standard-date bd-right-white td_nowrap\">"
					+ "  <span >"
					+data[i]['collectTime']
					+ "</span>"
					+ "  </td>"
					+ "  <td width=\"5%\" class=\"bd-right-white\">"
					+ "  <input type=\"hidden\"  name=\"isTemp\" value='"
					+ data[i]['isTemp']
					+ "' />"
					+ "  <input type=\"hidden\"  name=\"type\" value='"
					+ data[i]['type']
					+ "' />"
					+ "  <input type=\"radio\" class=\"forselect\" name=\"cellconfigradio\" value='"
					+ data[i]['configId']
					+ "' />"
					+ "  <label for=\"checkbox\"></label>"
					+ "  </td>"
					+ "  <td width=\"10%\">"
					+ "  <input type=\"button\" class=\"removebtn\" name=\"cellconfig\" value=\"移除\" /><input type=\"hidden\" class=\"hiddenconfigid\" value=\""
					+ data[i]['configId'] + "\" />"
					+ "  </td >                                                                                                                     "
					+ "  </tr>"
					+"<tr><td colspan=\"6\" style=\"background-color: #e7e7e7; height:1px; width:100%\"></td> </tr> ";
	}		
	return htmlstr;
}
/**
 * 获取小区干扰列表html
 * @param {} data
 */
function getCellInterferenceAnaliysisHtml(data){
	if (!data) {
		return;
	}
	var htmlstr = "";
	var trClass="";
	for ( var i = 0; i < data.length; i++) {
		if(i%2==0){
			trClass="tb-tr-bg-coldwhite";
		}else{
			trClass="tb-tr-bg-warmwhite";
		}
		htmlstr +="<tr class=\""+trClass+"\">"//table 内容  
						+ "  <td width=\"45%\" class=\"bd-right-white\" >    "
						+ "  <span >"
						+data[i]['title']
						+"</span>"
						+ "  </td>"
						+ "  <td width=\"45%\" class=\"bd-right-white\" >    "
						+ "  <span >"
						+data[i]['name']
						+"</span>"
						+ "  </td>"
						+ "  <td  width=\"20%\"  class=\"td-standard-date bd-right-white td_nowrap\">"
						+ "  <span >"
						+data[i]['collectTime']
						+ "</span>"
						+ "  </td>"
						+ "  <td width=\"5%\" class=\"bd-right-white\">"
						+ "  <input type=\"hidden\"  name=\"isTemp\" value='"
						+ data[i]['isTemp']
						+ "' />"
						+ "  <input type=\"hidden\"  name=\"type\" value='"
						+ data[i]['type']
						+ "' />"
						+ "  <input type=\"radio\" class=\"forselect\" name=\"interconfigradio\" value='"
						+ data[i]['configId']
						+ "' />"
						+ "  <label for=\"checkbox\"></label>"
						+ "  </td>"
						+ "  <td width=\"10%\">"
						+ "  <input type=\"button\" class=\"removebtn\" name=\"celliterference\"  value=\"移除\" /><input type=\"hidden\" class=\"hiddenconfigid\" value=\""
						+ data[i]['configId'] + "\" />"
						+ "  </td >                                                                                                                     "
						+ "  </tr>"
						+"<tr><td colspan=\"6\" style=\"background-color: #e7e7e7; height:1px; width:100%\"></td> </tr> ";
	}
	return htmlstr;
}

/**
 * 改变选择的小区配置或小区干扰分析列表
 */
//function changeAnalysisListSelection(type) {
//	// 比对当前选择的分析列表和之前的分析列表
//	// currentSelConfigId
//	var id = $("input[name='configradio']:checked").val();
//	var type = $("input[name='configradio']:checked").prev().val();
//	var isTemp = $("input[name='configradio']:checked").prev().prev().val();
//	// 默认没有变化
//	var hasChanged = false;
//	if (id != currentSelConfigId) {
//		hasChanged = true;
//	}
//	if (hasChanged) {
//		// 有变化
//		currentSelConfigId=id;
//		currentSelType = type;//当前选择类型 小区配置 小区干扰
//		currentSelIsTemp = isTemp;// 当前选择是否临时配置
//		$("inupt[name='type']").val(currentSelType);
//		clearAllData();
//		initPageParam();// 重置分页参数
//		
//		// 重选
//		$.ajax({
//			url : 'reselectCellAnalysisListForAjaxAction',
//			data : {
//				'selectId' : id,'type':type
//			},
//			type : 'post',
//			dataType : 'text',
//			success : function(data) {
//				currentloadtoken = getLoadToken();
//				loadGisCellData(currentloadtoken);
//			}
//		});
//	}
//}

/**
 * 根据所选择的分析列表加载小区数据
 */
function loadGisCellData(loadToken,configId) {
	onLoadingGisCell=true;
	$("#conditionForm")
			.ajaxSubmit(
					{
						url : 'getFreqReuseCellGisInfoFromSelAnaListForAjaxAction',
						data:{"configId":configId},
						dataType : 'json',
						success : function(data) {
							if (loadToken != currentloadtoken) {
								// 新的加载开始了，这些旧的数据要丢弃
								//console.log("丢弃此次的加载结果。");
								return;
							}
							// console.log(data);
							var obj = data;
							try {
								showGisCellOnMap(obj['gisCells']);
								var page = obj['page'];
								if (page) {
									var pageSize = page['pageSize'] ? page['pageSize']
											: 0;
									$("#hiddenPageSize").val(pageSize);

									var currentPage = new Number(
											page['currentPage'] ? page['currentPage']
													: 1);
									currentPage++;// 向下一页
									$("#hiddenCurrentPage").val(currentPage);

									var totalPageCnt = new Number(
											page['totalPageCnt'] ? page['totalPageCnt']
													: 0);
									$("#hiddenTotalPageCnt").val(totalPageCnt);

									var forcedStartIndex = new Number(
											page['forcedStartIndex'] ? page['forcedStartIndex']
													: -1);
									forcedStartIndex=-1;//禁用
									
									$("#hiddenForcedStartIndex").val(
											forcedStartIndex);

									var totalCnt = page['totalCnt'] ? page['totalCnt']
											: 0;
									totalCellCnt = totalCnt;
									$("#hiddenTotalCnt").val(totalCnt);


									//下一次的起点
									//currentPage从1开始
									var nextStartIndex=(currentPage-1)*pageSize;
									//console.log("nextStartIndex="+nextStartIndex+",totalCnt="+totalCnt+",currentPage="+currentPage+",pageSize="+pageSize);
									if (totalCnt > nextStartIndex) {
										 //console.log("继续获取下一页小区");
										 loadGisCellData(loadToken,configId);
									}else{
										onLoadingGisCell=false;
										hideOperTips("operInfo");
									}
//									try {
//										if (currentPage == 2) {
//											gisCellDisplayLib
//													.panToCell(obj['gisCells'][0]['cell']);
//										}
//									} catch (err) {
//									  console.error(err);
//									}
								}
								// 如果没有获取完成，则继续获取
							} catch (err) {
								// //console.log("返回的数据有问题:" + err);
								if (loadToken == currentloadtoken) {
									onLoadingGisCell=false;//终止
								}
								hideOperTips("operInfo");
							}
						},
						error : function(xmh, textstatus, e) {
							//alert("出错啦！" + textstatus);
							if (loadToken == currentloadtoken) {
								onLoadingGisCell=false;//终止
							}
							hideOperTips("operInfo");
						},
						complete : function() {
							$(".loading_cover").css("display", "none");
						}
					});
}

/**
 * 地图显示小区信息
 */
function showGisCellOnMap(gisCells) {
	//console.log("showGisCellOnMap.  gisCells=" + gisCells);
	if (!gisCells) {
		return;
	}

	//添加到地图
	gisCellDisplayLib.showGisCell(gisCells);
}

/**
 * 缓存统计数据的结构
 * 
 * @param cell
 */
function CellStsResult(cell) {
	this.cell = cell;
}

CellStsResult.prototype.get=function(prop) {
	return this[prop];
}

CellStsResult.prototype.set=function(prop, val) {
	if (prop) {
		this[prop] = val;
	}
}
/**
 * 频率复用统计 div 生成统计图
 */
function reportFreqReuse(){
	$("#reportPageSize").val(20);
	$("#reportCurrentPage").val(0);
	$("#reportPageCount").val(0);
	$("#reportTotalCount").val(0);
	getReportFreqReuseData();
}
/**
 * 频率分布标记 div弹出
 */
function analyzeFreqReuse(){
	$("#report_Dialog").hide();
	
	if(currentSelConfigId=="" || currentSelConfigId==undefined){
		animateInAndOut("operInfo",500,500,1000,"operTip","请先选择一个小区配置再进行分析");
		return false;
	}
	$("#analyzeedit_Dialog").show();
}
/**
 * 获取频点统计数据并生成统计图
 */
function getReportFreqReuseData(){
	$("#analyzeedit_Dialog").hide();
	
	
//	var id = $("input[name='cellconfigradio']:checked").val();
//	var type = $("input[name='cellconfigradio']:checked").prev().val();
	if(currentSelConfigId=="" || currentSelConfigId==undefined){
		animateInAndOut("operInfo",500,500,1000,"operTip","请先选择一个小区配置再进行分析");
		return false;
	}
	$("#reportCoverDiv").show();
	$("#report_Dialog").show();
	$("#reportId").val(currentSelConfigId);
	$("#reportType").val("CELLDATA");
	$("#reportForm")
	.ajaxSubmit({
		url : 'staticsFreqReuseInfoForAjaxAction',
		dataType : 'json',
		data:{'configId':currentSelConfigId},//上次加载的数据
		async:false,
		success : function(data) {
//			console.log("data:"+data);
			var page = data.page;
			if (page) {
				var pageSize = page.pageSize ? page.pageSize : 0;
				$("#reportPageSize").val(pageSize);
		
				var currentPage = page.currentPage && pageSize!=0 ? page.currentPage : 0;
				$("#reportCurrentPage").val(currentPage);
		
				var totalPageCnt = page.totalPageCnt ? page.totalPageCnt : 0;
				$("#reportPageCount").val(totalPageCnt);
		
				var totalCnt = page['totalCnt'] ? page['totalCnt'] : 0;
				$("#reportTotalCount").val(totalCnt);
		
				// 跳转
				$("#emTotalCnt").html(totalCnt);
				$("#showCurrentPage").val(currentPage);
				$("#emTotalPageCnt").html(totalPageCnt);
			}
			createReportChart(data.freqReuseInfos);//生成频点统计图
		},
		error : function(xmh, textstatus, e) {
			//alert("出错啦！" + textstatus);
		},
		complete : function() {
							$("#reportCoverDiv").css("display", "none");
						}
	});
	return true;
}
// 分页统计
function reportFreqByPage(dir) {
	var pageSize =new Number($("#reportPageSize").val());
	var currentPage = new Number($("#reportCurrentPage").val());
	var totalPageCnt =new Number($("#reportPageCount").val());
	var totalCnt = new Number($("#reportTotalCount").val());
	
	if (dir == "first") {
		if (currentPage < 0 && currentPage==1) {
			return;
		} else {
			$("#reportCurrentPage").val("1");
		}
	} else if (dir == "last") {
		if (currentPage >= totalPageCnt) {
			return;
		} else {
			$("#reportCurrentPage").val(totalPageCnt);
		}
	} else if (dir == "back") {
		if (currentPage <= 1) {
			return;
		} else {
			$("#reportCurrentPage").val(currentPage - 1);
		}
	} else if (dir == "next") {
		if (currentPage >= totalPageCnt) {
			return;
		} else {
			$("#reportCurrentPage").val(currentPage + 1);
		}
	} else if (dir == "num") {
		var userinput = $("#showCurrentPage").val();
		if (isNaN(userinput)) {
			alert("请输入数字！")
			return;
		}
		if (userinput > totalPageCnt || userinput < 0) {
			alert("输入页面范围不在范围内！");
			return;
		}
		$("#reportCurrentPage").val(userinput);
	}else if(dir=="all"){
		$("#reportCurrentPage").val(0);
	}else{
		return;
	}
	
	//获取统计数据
	getReportFreqReuseData();
}
/**
 * 生成频点统计图
 */
function createReportChart(data){
 if (!data) {
	return;
}
//组装数据
var keyStr="";
var tchStr = "";
var bcchStr = "";
$.each(data,function(key,value){
//	console.log("key:"+key+" value:"+value+" tchCount:"+value['tchCount']+" bcchCount:"+value['bcchCount']);
	keyStr+=",'"+key+"'";
	tchStr+=","+value['tchCount'];
	bcchStr+=","+value['bcchCount'];	
})
//alert(tchStr)
//alert(bcchStr)
tchStr=tchStr.substring(1,tchStr.length);
bcchStr=bcchStr.substring(1,bcchStr.length);
keyStr=keyStr.substring(1,keyStr.length);
var categories = "["+keyStr+"]";
var series = "[{name: 'TCH',color: 'green',data: ["+tchStr+"]}, {name: 'BCCH',color: 'red',data: ["+bcchStr+"]}]";	
//console.log("categories:"+categories);
//console.log("series:"+series)
categories=eval(categories);
series=eval(series);
//column/bar
 new Highcharts.Chart({
            chart: {
            	renderTo:'report_Div',
                type: 'column',
                zoomType: 'xy'
            },
            title: {
                text: ''
            },
            xAxis: {
                categories: categories
            },
            yAxis: {
                min: 0,
                title: {
                    text: ''
                },
                stackLabels: {
                    enabled: true,
                    style: {
                        fontWeight: 'bold',
                        color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                    }
                }
            },
            legend: {
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'middle',
                borderWidth: 0
            },
            tooltip: {
                formatter: function() {
                    return '<b>'+ this.x +'</b><br/>'+
                        this.series.name +': '+ this.y +'<br/>'+
                        'Total: '+ this.point.stackTotal;
                }
            },
            plotOptions: {
                column: {
                    stacking: 'normal',
                    dataLabels: {
                        enabled: true,
                        color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white'
                    },
                    pointWidth: 30,	//chao.xj
                    cursor:'pointer'//chao.xj 鼠标移到图表上时鼠标的样式  
                }
            },
            series: series
        });
}
/**
 * 频点复用地图标记
 */
function markCellForFreqReuse(){
	gisCellDisplayLib.clearOnlyExtraOverlay();//清除额外覆盖物
	if(onLoadingGisCell==true){
		animateInAndOut("operInfo",500,500,1000,"operTip","正在加载小区数据，请稍后尝试...");
		return;
	}
	var cell=null;
//	for(var i in allCellStsResults){
//		cell=i;
//		break;
//	}
//
//	if(!cell){//无小区可标记
//		return;
//	}
	
	if(gisCellDisplayLib.getPolygonCnt()==0){
		animateInAndOut("operInfo",500,500,1000,"operTip","不存在任何小区...");
		return;
	}
	var freq_value = $("input[name='freq_value']").val();
	if(freq_value==""){
		animateInAndOut("operInfo",500,500,1000,"operTip","请输入频点值！");
		$("input[name='freq_value']").focus();
		return false;
	}

	// 可视区域
	var visiblePolygons=gisCellDisplayLib.get("visiblePolygons");
	var size = visiblePolygons.length;
	var pl;
	var cont;
	//var label;
	var cnt=0;
	clearMarkCellForFreqReuse();//清除之前标记
	for ( var i = 0; i < size; i++) {
		cont = null;
		pl = visiblePolygons[i];
		var cmk=gisCellDisplayLib.getComposeMarkerByShape(pl);
		cont = cmk.getFreqCellLabelContent(pl,freq_value,gisCellDisplayLib);
		if (cont != "") {
			cnt++;
			var pt=gisCellDisplayLib.getLnglatObjByComposeMarker(cmk);
			var	label = new IsTextLabel(pt.lng, pt.lat, cont,null,gisCellDisplayLib);
			if (label) {
				if(map instanceof BMap.Map){
				label.setStyle({ "cursor":"pointer"});
				gisCellDisplayLib.addOverlay(label);
				label.addEventListener("contextmenu",function(e){
//							console.log("contextmenu:"+label+" e:"+e);
							try{
								//console.log("map:"+map+" this:"+this);
								//map.removeOverlay(label);
								gisCellDisplayLib.removeOverlay(this);
//								$(this).attr("display","none");
								animateInAndOut("operInfo", 200, 200, 500, "operTip", $(this).text()+"删除文本标注成功！");
							}catch(e){
								animateInAndOut("operInfo", 200, 200, 500, "operTip", $(this).text()+"删除文本标注失败！"+e);
							}
						});
				}
				specRenderderLabel.push(label);
				
				//console.log(label);
				
			}
		}
	}
	

	
}
//已作为类库的原型方法
/**
 * 获取符合频点条件的lable内容
 */
function getFreqCellLabelContent(pl,freq){
	if (!freq || $.trim(freq) == "") {
		return null;
	}
	if (isNaN(freq)) {
		return null;
	}
	var labelContent1="";
	var labelContent2="";
	var cellArray = pl._data.getCellArray();
	var gisc;
	var freq1 = parseInt(freq)+1;
	var freq2 = parseInt(freq)-1;
	for ( var i = 0; i < pl._data.getCount(); i++) {
		gisc = cellArray[i];
		if (gisc.bcch == freq) {
			labelContent1 =  gisc.cell + " bcch:" + gisc.bcch+",tch:" + gisc.tch;
			break;
		}
		if(gisc.bcch == freq1||gisc.bcch == freq2){
			labelContent2 = gisc.cell +" bcch:" + gisc.bcch+",tch:" + gisc.tch;
			break;
		}
		if (gisc.tch) {
			var tcha = gisc.tch.split(",");
			for ( var ti = 0; ti < tcha.length; ti++) {
				if (tcha[ti] == freq) {
					labelContent1 = gisc.cell + " bcch:" + gisc.bcch+",tch:" + gisc.tch;
					break;
				}
				if(gisc.bcch == freq1||gisc.bcch == freq2){
					labelContent2 = gisc.cell + " bcch:" + gisc.bcch+",tch:" + gisc.tch;
					break;
				}
			}
		}
	}
	var content="";
	if(labelContent1!=""){
		content = "<div style='background-color:red'>"+labelContent1+"</div>";
	}else if(labelContent2!=""){
		content = "<div style='background-color:yellow'>"+labelContent2+"</div>";
	}
	return content;
}
/**
 * 清除标记
 */
function clearMarkCellForFreqReuse(){
	gisCellDisplayLib.resetSpecPolygonToDefaultOutlook();

	// -----label---------//
	size = specRenderderLabel.length;
	if (size > 0) {
		for ( var i = 0; i < size; i++) {
			try {
				gisCellDisplayLib.removeOverlay(specRenderderLabel[i]);
			} catch (er) {
				// ignore
			}
		}
	}
	specRenderderLabel.splice(0, size);
}
/**
 * 验证数字
 * @param {} node
 */
function checkNumber(node){
	  var number  = node.value;
	  number=number.replace(/[^1234567890]/g,'');
	  node.value=number;
}

