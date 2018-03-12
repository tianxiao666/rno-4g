/**
 * 本模块负责完成
 * 1、获取、展现分析列表
 * 2、选择、反选分析列表，确定真正需要分析的数据范围
 * 3、根据所选择的分析列表，加载小区到地图展示
 * 4、进行相关统计，如无线资源利用率
 * 5、按照渲染规则，展现统计结果
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
var rendererFactory = null;

var currentSelConfigIds = new Array();// 当前选择的用于分析的分析列表
var currentloadtoken = "";// 加载任务的token

var minZoom = 15;// 只有大于 该 缩放级别，才真正
var randomShowCnt = 50;// 在不需要全部显示的时候，最大随机显示的个数

var onLoadingGisCell = false;// 是否正在加载小区信息

var lastRendererType = "";// 上一次渲染的指标类型
var isRuleChanged = false;// 渲染规则是否有变化

var loadingLayerId = null;// 加载层的id
/**
 * ======================================================= 变量定义结束
 * =======================================================
 */

$(document).ready(function() {
	//显示可以切换的地图
	$("#trigger").val(glomapid=='null' || glomapid=='baidu'?"切换谷歌":"切换百度");
	
	bindNormalEvent();

	initAreaCascade();

	gisCellDisplayLib = new GisCellDisplayLib(map, minZoom, randomShowCnt);
	var lng = $("#hiddenLng").val();
	var lat = $("#hiddenLat").val();
	map = gisCellDisplayLib.initMap("map_canvas", lng, lat);
	
	rendererFactory=new RendererFactory(false);// 缓存的渲染规则

	loadAndShowAnalysisList();
});

// function loadevent(){
// getSubArea("#conditionForm #provinceId","#conditionForm
// #cityId","#conditionForm #queryCellAreaId");
// getSubArea("#formImportCell #provinceId","#formImportCell
// #cityId","#formImportCell #areaId")
//	
// }
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
		// console.log("改变区域:" + lnglat);
		// 清除当前区域的数据
		//clearAllData();

		if (lnglat) {
			// 地图中心点
			var lls = lnglat.split(",");
			if (lls[0] == 0 || lls[1] == 0) {
				// console.warn("未设置该区域的中心点经纬度。");
			} else {
				// 地图移动
				gisCellDisplayLib.panTo(lls[0], lls[1]);
			}
		}
	});
}

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
 * 清空所有的数据
 */
function clearAllData() {
	// 清空数组
	allCellStsResults = null;
	allCellStsResults = new Object();
	// 清空地图

	// 清空地图数据
	gisCellDisplayLib.clearData();// 清空地图上的东西

	// 清空缓存的上一次渲染的指标类型
	lastRendererType = "";

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
		// var tr = $(this).parent().parent();
		var tr = $(this).closest("tr");
		var loadedConfigId = $(this).siblings(":first").val();
		if (tr.find("input.forselect").attr("checked")) {
			isSelectedNow = true;
		}

		$.ajax({
			url : 'removeAnalysisItemFromLoadedListForAjaxAction',
			data : {
				'loadedConfigId' : loadedConfigId
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

	// 从加载列表中，选择若干列表进行分析
	$("#confirmSelectionAnalysisBtn").click(function() {
		// 触发改变的事件
		if($("#analysisListTable").find("input.forselect:checked").length>0)
		{
			changeAnalysisListSelection();
		}
		else
		{
			alert("请选择至少一条小区性能指标记录！");
		}
	});

	// // 改变区域
	// $("#queryCellAreaId").change(function() {
	// var lnglat = $("#areaid_" + $("#queryCellAreaId").val()).val();
	// // console.log("改变区域:" + lnglat);
	// // 清除当前区域的数据
	//
	// var lls = lnglat.split(",");
	// map.panTo(new BMap.Point(lls[0], lls[1]));// 这个偏移可以不理会
	// });
	
	//百度地图和谷歌地图切换按钮的事件绑定
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
		/* var temp = $(this).val();
		 var sessId;
		 if(temp=="切换百度"){
		 	console.log("切换前是谷歌");
		 	$(this).val("切换谷歌");
		 	console.log("切换后是百度");
		 	sessId="baidu";
		 	
		 	//重新刷新本页面
		 }else{
		 console.log("切换前是百度");
		  $(this).val("切换百度");
		  console.log("切换后是谷歌");
		  sessId="google";
		  //重新刷新本页面
		 }*/
		 storageMapId(sessId);
		 });
}

/**
 * 获取和展现被加载的分析列表
 */
function loadAndShowAnalysisList() {
	$("#analysisListTable").empty();
	$
			.ajax({
				url : 'getLoadedAnalysisListForAjaxAction',
				dataType : 'json',
				type : 'post',
				success : function(data) {
					if (!data) {
						return;
					}
					// 在面板显示
					var htmlstr = "";
					var one;
					var trClass = "";
					for ( var i = 0; i < data.length; i++) {
						one = data[i]['stsAnaItemDetail'];
						if (i % 2 == 0) {
							trClass = "tb-tr-bg-coldwhite";
						} else {
							trClass = "tb-tr-bg-warmwhite";
						}
						if (one) {
							htmlstr += "<tr class=\""
									+ trClass
									+ "\">"// table 内容 yuan.yw 修改 2013-10-28
									+ "  <td width=\"45%\" class=\"bd-right-white\" >    "
									+ "  <span >"
									+ one['areaName']
									+ "</span>"
									+ "  </td>"
									+ "  <td  width=\"20%\"  class=\"bd-right-white td_nowrap\">"
									+ "  <span >"
									+ getValidValue(one['stsType'],"未知")
									+ "</span>"
									+ "  </td>"
									+ "  <td  width=\"20%\"  class=\"td-standard-date bd-right-white td_nowrap\">"
									+ "  <span >"
									+ getValidValue(one['stsDate'],"未知")
									+ "</span>"
									+ "  </td>"
									+ "  <td width=\"20%\" class=\"bd-right-white td_nowrap\">"
									+ "  <span>"
									+ getValidValue(one['periodType'],"不规则指标")
									+ "</span>"
									+ "  </td>"
									+ "  <td width=\"5%\" class=\"bd-right-white\">"
									+ "  <input type=\"checkbox\" class=\"forselect\" name=\"checkbox\" id='"
									+ data[i]['configId']
									+ "' />"
									+ "  <label for=\"checkbox\"></label>"
									+ "  </td>"
									+ "  <td width=\"10%\">"
									+ "  <input type=\"button\" class=\"removebtn\"  value=\"移除\" /><input type=\"hidden\" class=\"hiddenconfigid\" value=\""
									+ data[i]['configId']
									+ "\" />"
									+ "  </td >                                                                                                                     "
									+ "  </tr>"
									+ "<tr><td colspan=\"6\" style=\"background-color: #e7e7e7; height:1px; width:100%\"></td> </tr> ";
						}
					}
					$("#analysisListTable").append(htmlstr);
					if (htmlstr != "") {
						$("#analysisBtnDiv").show();
					}

				}
			});
}

/**
 * 改变选择的分析列表
 */
function changeAnalysisListSelection() {
	// 比对当前选择的分析列表和之前的分析列表
	// currentSelConfigIds
	var ids = new Array();
	$("#analysisListTable").find("input.forselect:checked").each(
			function(i, ele) {
				ids.push($(ele).attr("id"));
			});

	// 默认没有变化
	var hasChanged = false;
	if (ids.length == currentSelConfigIds.length) {
		for ( var i = 0; i < ids.length; i++) {
			for ( var j = 0; j < currentSelConfigIds.length; j++) {
				if (ids[i] == currentSelConfigIds[j]) {
					break;
				}
			}
			if (j == currentSelConfigIds.length) {
				hasChanged = true;
				break;
			}
		}
	} else {
		hasChanged = true;
	}

	if (hasChanged) {
		// 有变化
		currentSelConfigIds.splice(0, currentSelConfigIds.length);
		for ( var i = 0; i < ids.length; i++) {
			currentSelConfigIds.push(ids[i]);
		}

		clearAllData();
		initPageParam();// 重置分页参数
		// 重选
		$.ajax({
			url : 'reselectAnalysisListForAjaxAction',
			data : {
				'selIds' : currentSelConfigIds.join(",")
			},
			type : 'post',
			dataType : 'text',
			success : function(data) {
				currentloadtoken = getLoadToken();
				loadGisCellData(currentloadtoken);
			}
		});
	}
}

/**
 * 根据所选择的分析列表加载小区数据
 */
function loadGisCellData(loadToken) {
	// console.log("loadGisCellData....");
	// if(loadingLayerId){
	// layer.close(loadingLayerId);
	// loadingLayerId=layer.load(0,1,true);
	// }
	onLoadingGisCell = true;
	$("#conditionForm")
			.ajaxSubmit(
					{
						url : 'getToBeAnaCellGisInfoFromSelAnaListForAjaxAction',
						dataType : 'json',
						success : function(data) {
							if (loadToken != currentloadtoken) {
								// 新的加载开始了，这些旧的数据要丢弃
								// console.log("丢弃此次的加载结果。");
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
									$("#hiddenForcedStartIndex").val(
											forcedStartIndex);

									var totalCnt = page['totalCnt'] ? page['totalCnt']
											: 0;
									totalCellCnt = totalCnt;
									$("#hiddenTotalCnt").val(totalCnt);

									if (forcedStartIndex != -1) {
										// 有设置强制起点
										// 扩大pagesize，加速获取过程
										forcedStartIndex = forcedStartIndex
												+ pageSize;
										pageSize = pageSize << 1;
										$("#hiddenPageSize").val(pageSize);
									}

									// 下一次的起点
									// currentPage从1开始
									var nextStartIndex = forcedStartIndex == -1 ? ((currentPage - 1) * pageSize)
											: forcedStartIndex;
									// console.log("nextStartIndex="+nextStartIndex+",totalCnt="+totalCnt+",currentPage="+currentPage+",pageSize="+pageSize);
									if (totalCnt > nextStartIndex) {
										// console.log("继续获取下一页小区");
										loadGisCellData(loadToken);
									} else {
										onLoadingGisCell = false;
										// if(loadingLayerId){
										// layer.close(loadingLayerId);
										// }
									}
								}
								// 如果没有获取完成，则继续获取
							} catch (err) {
								// //console.log("返回的数据有问题:" + err);
								if (loadToken == currentloadtoken) {
									onLoadingGisCell = false;// 终止
								}
								// if(loadingLayerId){
								// layer.close(loadingLayerId);
								// }
							}
						},
						error : function(xmh, textstatus, e) {
//							alert("出错啦！" + textstatus);
							if (loadToken == currentloadtoken) {
								onLoadingGisCell = false;// 终止
							}
							// if(loadingLayerId){
							// layer.close(loadingLayerId);
							// }
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
	// console.log("showGisCellOnMap. gisCells=" + gisCells);
	if (!gisCells) {
		return;
	}
	// 增加到allCellStsResults
//	var len = gisCells.length;
//	var csr = null;
//	for ( var i = 0; i < len; i++) {
//		csr = new CellStsResult(gisCells[i]['cell']);
//		allCellStsResults[gisCells[i]['cell']] = csr;
//	}

	// 添加到地图
	gisCellDisplayLib.showGisCell(gisCells);
}

function showTips(tip) {
	$(".loading_cover").css("display", "block");
	$(".loading_fb").html(tip);
}

function hideTips(tip) {
	$(".loading_cover").css("display", "none");
}

/**
 * 统计功能
 * 
 * @param type
 *            指明统计类型：无线资源利用率，
 * @param action
 *            提交到后台的action名称
 * @param name
 *            如果name不为空，说明要获取type类型的name指定的范围的统计值。如获取type=无线资源利用率，name=超忙小区
 */
function commonstatics(type, action, name) {
	
	var areaId = $("#cityId").val();
	rendererFactory.findRule(type,areaId,function(rule,ruleCode,areaId){
		//console.log("在callback中，rule.rawData="+rule.rawData);
		//图形化显示
		showRendererRuleColor(rule.rawData,ruleCode,areaId,"%");
		
		//console.log("rule is:");
		//rule.print();
		
		
		// 连续两次渲染一样的指标
		if (type === lastRendererType && !isRuleChanged) {
			return;
		}

		isRuleChanged = false;

		if (onLoadingGisCell == true) {
			alert("正在加载小区数据，请稍后尝试...");
			return;
		}

		// if(loadingLayerId){
		// layer.close(loadingLayerId);
		// loadingLayerId=layer.load(0,1,true);
		// }
//		var cell = null;
//		for ( var i in allCellStsResults) {
//			cell = i;
//			break;
//		}
//		if (!cell) {
//			//console.log("未选中任何分析列表。当前无可分析的小区。");
//			// layer.close(loadingLayerId);
//			return;
//		}

		//console.log("打开loading。。。");
		showTips("统计");
		// 清除上一次的渲染效果
		gisCellDisplayLib.resetPolygonToDefaultOutlook();

		// console.log("使用者中："+(rule instanceof RendererRule));
		if (rule == null) {
			alert("不存在当前的指标的渲染规则！")
			hideTips();
			return;
		}

		// 获取数据进行渲染
		// 是否缓存有数据
		//var cellStsResult = allCellStsResults[cell];
		//if (!cellStsResult || !cellStsResult.get(type)) {
			// 缓存没有该类型的指标统计结果，需要从服务器获取
			//console.log("get from server:");
			loadStaticsInfo(rule,action, type, name, 0, function() {
//				var option = null;
//				for ( var eachcell in allCellStsResults) {
//					// console.log("eachcell==="+eachcell+",value=="+allCellStsResults[eachcell]);
//					// 根据渲染规则和该小区的统计值，得到样式
//					option = createOptionFromStsResult(
//							allCellStsResults[eachcell].get(type), rule);
//					 //console.log("新获取的数据，createOptionFromStsResult	 得到的options="+option['fillColor']);
//					// 应用到地图显示上
//					gisCellDisplayLib
//							.changeCellPolygonOptions(eachcell, option);
//					// layer.close(loadingLayerId);
//				}
				//不缓存
				allCellStsResults=null;
				allCellStsResults=new Object();
				//console.log("loadStaticsInfo的回调里关闭loading。。。");
				hideTips();
			});
//		} else {
//			// 缓存中存在
//			changeOutlookAccordingToStsResult(type, rule);
//			//console.log("缓存中存在时关闭loading。。。");
//			hideTips();
//		}

		lastRendererType = type;

	},false);
	


}

/**
 * 根据统计值改变地图上的图元的外观 图元的统计值已经存放在
 */
function changeOutlookAccordingToStsResult(type, rule) {
	// console.log("changeOutlookAccordingToStsResult.... type="+type);
	var option = null;
	// var rule = rendererFactory.findRule(type);
	for ( var eachcell in allCellStsResults) {
		// console.log("eachcell==="+eachcell+",value=="+allCellStsResults[eachcell]);
		// 根据渲染规则和该小区的统计值，得到样式
		option = createOptionFromStsResult(allCellStsResults[eachcell]
				.get(type), rule);
		 //console.log("createOptionFromStsResult 得到的options="+option.fillColor);
		// 应用到地图显示上
		gisCellDisplayLib.changeCellPolygonOptions(eachcell, option);
	}
}

/**
 * 从服务器加载指定指标的统计情况
 * 
 * @param action
 * @param type
 *            指标类型
 * @param name
 *            指标类型下的某类型小区
 * @param startIndex
 *            起始下标
 */
function loadStaticsInfo(rule,action, type, name, startIndex, callback) {
	 //console.log("loadStaticsInfo. startIndex=="+startIndex+",action="+action+",type="+type);
	if (!type) {
		return;
	}

	$.ajax({
		url : action,
		data : {
			'stsCode' : type,
			'startIndex' : startIndex
		},
		dataType : 'json',
		type : 'post',
		async : false,
		success : function(data) {
			if (!data) {
				return;
			}
			// totalCnt,hasMore,rnoStsResults,startIndex
			try {
				var stsresult = data['rnoStsResults'];
				startIndex = data['startIndex'];
				var hasMore = data['hasMore'];
				if (stsresult) {
					var len = stsresult.length;
					var cell = null;
					var oriStsResults = null;
					gisCellDisplayLib.releaseOptions();
					for ( var i = 0; i < len; i++) {
						cell = stsresult[i]['cell'];
						//console.log("填充cell="+cell+" 的 stsresult="+stsresult[i]);
//						oriStsResults = allCellStsResults[cell];
//						if (!oriStsResults) {
//							oriStsResults = new CellStsResult(cell);
//							allCellStsResults[cell] = oriStsResults;
//						}
//						oriStsResults.set(type, stsresult[i]);// 增加某种类型的统计结果
						
						option = createOptionFromStsResult(stsresult[i], rule);
			         	gisCellDisplayLib.changeCellPolygonOptions(cell, option);
					}
				}
				if (hasMore === true) {
					loadStaticsInfo(rule,action, type, name, startIndex, callback);
				} else {
					//console.log("准备调用loadStaticsInfo的callback...");
					callback();
				}
			} catch (err) {
				 //console.error(err);
				 hideTips("");
			}
		},
		error:function(){
			hideTips("");
			alert("系统出错！请联系管理员！");
		},
		complete:function(){
			hideTips("");
		}
	});
}

/**
 * 根据统计数值和渲染规则，得到用于地图显示的外观选项
 * 
 * @param stsResult
 * @param rule
 */
//var xxxxx=1;
function createOptionFromStsResult(stsResult, rule) {

	//console.log("createOptionFromStsResult。stsResult="+stsResult+",rule="+rule);
	if (!stsResult || !rule) {
		return null;
	}

	// console.log("-----------begin-----------");
	// for(var i in stsResult){
	// console.log("key="+i+",value="+stsResult[i]);
	// }
	// console.log("-----------end-----------");
	// console.log("stsResult="+stsResult.toSource());
	//console.log("maxvalue="+stsResult['maxValue']);
	var setting = rule.findRuleSetting(stsResult['avgValue']);
//	if(xxxxx==1){
//	//console.log(" in create option rule =" ) ;
//	rule.print();
//	xxxxx++;
//	}
	//console.log("setting ?"+setting instanceof RuleSetting);
	if (setting==null) {
		return null;
	}
	//console.log(setting)
	
	var style = setting.getStyle();
	//console.log("style="+style['color']);
	if (!style) {
		//console.log("style 无效！")
		return null;
	}
	var option = new Object();
	option.fillColor = style.color;
	option.fillOpacity = 1;

	//console.log("返回前，color="+option['fillColor']);
	return option;
}

/**
 * 小区的所有信息： 1、gis信息 2、统计信息
 */
/*
 * function CellTotalInfo(giscell){ this.cell=giscell.cell?giscell.cell:null;
 * this.cellstsresult=new CellStsResult(this.cell); }
 * 
 * CellTotalInfo.prototype.addCellStsResult=function(prop,val){
 * if(!this.cellstsresult){ this.cellstsresult=new CellStsResult(this.cell); }
 * this.cellstsresult.set(prop,val); } //获取指定类型的指标统计情况
 * CellTotalInfo.prototype.getCellStsResult=function(type){
 * if(!this.cellstsresult){ return null; } return this.cellstsresult.get(type); }
 */

/**
 * 缓存统计数据的结构
 * 
 * @param cell
 */
function CellStsResult(cell) {
	this.cell = cell;
}

CellStsResult.prototype.get = function(prop) {
	return this[prop];
}

CellStsResult.prototype.set = function(prop, val) {
	if (prop) {
		this[prop] = val;
	}
}
