var $ = jQuery.noConflict();
var map;
var gisCellDisplayLib;// 负责显示小区的对象



var cellDetails = new Array();// 小区详情
var composeMarkers = new Array();// 复合点
var allPolygons = new Array();// 与composeMarkers对应
var visiblePolygons = new Array();// 可见的多边形

// var onScatterling = false;// 正在展开重叠小区
var scatterlingIndex = -1;// 展开显示的重叠mark下标
var overlaymks = new Array();// 临时用来散开展示重叠小区的marker对象
var scatterWindow;// 展开的信息窗口

// 相似度判断阈值
var DiffAzimuth = 1;// 角度
var DiffDistance = 5;// 距离（米）

//
var minZoom = 17;// 只有大于 该 缩放级别，才真正
var preZoom = 15;// 当前的缩放级别
var minResponseInterval = 1000;// 对事件做响应的最小的间隔
var lastOperTime = 0;// 最后一次操作的时间
var randomShowCnt = 150;// 在不需要全部显示的时候，最大随机显示的个数

var mapGridLib //用于将地图分成网格加载小区的对象

// 渲染颜色
var singleColor = '#0B4C5F';
var multiColor = '#FE2E64';

//
var specRenderderPolygons = new Array();// 搜索到以后，特殊渲染的
var specRenderderLabel = new Array();// 频点搜索出来后用于显示的label

var cellToNcell = new Array();
var targetCellColor = "#B40431";// 暗红色
var targetNcellColor = "#2EFE2E";// 耀眼的绿色

var maxCacheNcellsCnt = 100;// 最大缓存数量
var maxCacheNcellsTime = 1000 * 60 * 2;// 邻区数据缓存时间
var ncellsOfCell = new Array();// 缓存获取到的小区的邻区，存储的是CellToNcell对象

var currentloadtoken = "";// 加载的token，每次分页加载都要比对。

var firstTime=true;//第一次加载地图

var isShowCellName = false; //地图是否显示小区名字
//
function getLoadToken() {
	return new Date().getTime() + Math.random() * 100 + "";
}

$(document).ready(function() {

	// 隐藏详情面板
	hideDetailDiv();
	// 初始化地图
	initMap();
	//初始化事件响应函数
	initEvent();
	
	// 加载数据
	//currentloadtoken = getLoadToken();
	//getGisCell(currentloadtoken);
	
	/******* peng.jm 2014-9-29 网格形式加载小区  start ********/	
	$("#loadgiscell").click(function(){
			clearAll(); //加载前清除地图上覆盖物
			//getGisCellByFreqType(currentloadtoken);

			//初始化网格状态
			mapGridLib.initMapGrids();
			//设置地图加载小区状态为true
			mapGridLib.setIsLoading(true);
			//设置当前屏幕经纬度范围
			var winMinLng = map.getBounds().getSouthWest().lng;
			var winMinLat = map.getBounds().getSouthWest().lat;
			var winMaxLng = map.getBounds().getNorthEast().lng;
			var winMaxLat = map.getBounds().getNorthEast().lat;
			mapGridLib.setWinLngLatRange(winMinLng,winMinLat,winMaxLng,winMaxLat);
			//设置当前加载标识
			currentloadtoken = getLoadToken();
			mapGridLib.setCurrentloadtoken(currentloadtoken);
			//加载小区
			var t = new Date().getTime();
			/*var gsmtype = $("input[name='freqType']:checked").val();
			var params = {
				'freqType' : gsmtype
			};*/
			//t-minResponseInterval保证不延迟加载
			mapGridLib.loadGisCell(t-minResponseInterval, currentloadtoken, minResponseInterval);
	});
	/******* peng.jm 2014-9-29 网格形式加载小区  end ********/		
		
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
 * 初始化地图
 */
function initMap() {
	gisCellDisplayLib = new GisCellDisplayLib(map, minZoom, randomShowCnt,
			null, {
				'clickFunction' : handleClick,
				'mouseoverFunction' : null,
				'mouseoutFunction' : null
			},{'showCellLabel' : false},"", preZoom);
	var lng = $("#hiddenLng").val();
	var lat = $("#hiddenLat").val();
	gisCellDisplayLib.initMap("map_canvas", lng, lat, {
										'movestart' : handleMovestartAndZoomstart,
										'zoomstart' : handleMovestartAndZoomstart,
										'moveend' : handleMoveendAndZoomend,
										'zoomend' : handleMoveendAndZoomend });
	map = gisCellDisplayLib.getMap();

	/******* peng.jm 2014-9-29 网格形式加载小区  start ********/	
	//以网格形式在地图加载小区
	mapGridLib = new MapGridLib(gisCellDisplayLib,"conditionForm","loadingCellTip"); 
	//以城市为单位创建区域网格
	var cityName = $("#cityId").find("option:selected").text().trim();
	mapGridLib.createMapGrids(cityName);
	/******* peng.jm 2014-9-29 网格形式加载小区  end ********/
	
	// 创建信息窗口
	//gisCellDisplayLib.createInfoWindow("");
}


// 隐藏详情面板
function hideDetailDiv() {

	$("a.siwtch").hide();
	$(".switch_hidden").show();
	$(".resource_list_icon").animate({
		right : '0px'
	}, 'fast');
	$(".resource_list_box").hide("fast");
}

// 绑定事件
function initEvent() {
	// 根据条件搜索小区
	$("#searchCellBtn").click(function() {
		searchCell();
	});
	// 搜索邻区
	$("#searchNcellBtn").click(function() {
		searchNcell();
	});
	// 搜频点
	$("#searchFreqBtn").click(function() {
		searchFreq();
	});

	// 让搜索面板可见
	//$("#searchDiv").css("display", "block");

	// 清除搜索结果
	$("#clearSearchResultBtn").click(function() {
		clearSpecRendPolygons();
	});

	//控制地图小区名称是否显示
	$("#showCellName").click(function() {
		if(isShowCellName) {
			$("#showCellName").val("显示小区名字");
			isShowCellName = false;
			gisCellDisplayLib.hidePolygonsLabel();
		} else {
			$("#showCellName").val("关闭小区名字");
			isShowCellName = true;
			gisCellDisplayLib.showPolygonsLabel();
		}
	});
	
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
							
							if (i == 0) {
								$("#hiddenLng").val(one["longitude"]);
								$("#hiddenLat").val(one["latitude"]);
							}
						}
						$("#hiddenAreaLngLatDiv").append(html);
						$("#queryCellAreaId").append("<option value='-1'>全部</option>");
					}

					//$("#queryCellAreaId").trigger("change");
				});
			});

	// 区域选择改变
	$("#queryCellAreaId").change(function() {
		var lnglat = $("#areaid_" + $("#queryCellAreaId").val()).val();
		 //console.log("改变区域:" + lnglat);
		// 清除当前区域的数据
		clearAll();
		//是否显示全市的小区，如果是，则无不要移动地图
		if($("#queryCellAreaId").val()>-1){
			
			// 地图中心点
			var lls = lnglat.split(",");
			//map.panTo(new BMap.Point(lls[0], lls[1]));// 这个偏移可以不理会
			gisCellDisplayLib.panTo(lls[0], lls[1]);
		}
		// 重置查询表单的数据
		resetQueryCondition();
		// 重新获取新区域的数据
		currentloadtoken = getLoadToken();
		//getGisCell(currentloadtoken);
		
		/******* peng.jm 2014-9-29 网格形式加载小区  start ********/	
		//以城市为单位创建区域网格
		var cityName = $("#cityId").find("option:selected").text().trim();
		mapGridLib.createMapGrids(cityName);
		/******* peng.jm 2014-9-29 网格形式加载小区  end ********/	
	});
}

// 清除全部的数据
function clearAll() {

	// var pl;
	// for ( var i = 0; i < allPolygons.length; i++) {
	// pl = allPolygons[i];
	// if (pl._isShow == true) {
	// pl._isShow = false;
	// map.removeOverlay(pl);
	// }
	// }

	try {
		gisCellDisplayLib.closeInfoWindow();
	} catch (e) {

	}
	try {
		gisCellDisplayLib.clearOverlays();
	} catch (e) {

	}

	cellDetails.splice(0, cellDetails.length);
	composeMarkers.splice(0, composeMarkers.length);
	for(var i=0;i<allPolygons.length;i++){
		allPolygons[i]._isShow=false;
	}
	allPolygons.splice(0, allPolygons.length);
	visiblePolygons.splice(0, visiblePolygons.length);

	specRenderderPolygons.splice(0, specRenderderPolygons.length);
	specRenderderLabel.splice(0, specRenderderLabel.length);
	cellToNcell.splice(0, cellToNcell.length);
	ncellsOfCell.splice(0, ncellsOfCell.length);
	
	gisCellDisplayLib.clearData();

}

// 重置查询表单的值
function resetQueryCondition() {
	$("#hiddenCurrentPage").val(1);
	$("#hiddenTotalPageCnt").val(0);
	$("#hiddenTotalCnt").val(0);
}

/**
 * 获取gis小区
 */
function getGisCell(loadToken) {
	$("#conditionForm").ajaxSubmit(
			{
				url : 'getGisCellByPageForAjaxAction',
				dataType : 'json',
				success : function(data) {
					// console.log(data);
					if (loadToken != currentloadtoken) {
						// 新的加载开始了，这些旧的数据要丢弃
						//console.log("丢弃此次的加载结果。");
						return;
					}
					var obj = data;
					try {
						// obj = eval("(" + data + ")");
						// alert(obj['celllist']);
//						showGisCell(obj['gisCells']);
						gisCellDisplayLib.showGisCell(obj['gisCells']);
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

							var totalCnt = page['totalCnt'] ? page['totalCnt']
									: 0;
							$("#hiddenTotalCnt").val(totalCnt);

							if (totalPageCnt >= currentPage) {
								// console.log("继续获取下一页小区");
								// if(currentPage<4)
								getGisCell(loadToken);
							}
						}
						// 如果没有获取完成，则继续获取
					} catch (err) {
						// console.log("返回的数据有问题:" + err);
					}
				},
				error : function(xmh, textstatus, e) {
//					alert("出错啦！" + textstatus);
				},
				complete : function() {
					$(".loading_cover").css("display", "none");
				}
			});
}

//这里需要改造
/**
 * 处理polygon的点击事件
 * 
 * @param {}
 *            polygon
 */
function handleClick(polygon, cell) {
	if(!cell) {
		var cmk = gisCellDisplayLib.getComposeMarkerByShape(polygon);
		cell = cmk.getCell();
	}
	showCellDetail(cell);
}

///**
// * 处理鼠标移动事件
// * 
// * @param {}
// *            polygon
// * @param {}
// *            event
// */
//function handleMouseover(polygon, event) {
//	var cmk = polygon._data;
//	if (cmk.getCount() > 1) {
//		//scatterAll(polygon);
//	} else {
//		// showCellTitle(polygon);
//	}
//}
//
//// 显示小区标题
//function showCellTitle(polygon) {
//	// console.log("show cell title");
//}
//
////这里需要改造
///**
// * 展开重叠的小区
// * 
// * @param {}
// *            polygon
// */
//function scatterAll(polygon) {
//	var html = formOverlapHtmlContent(polygon._data);
//	html = "<h4 style='margin:0 0 5px 0;padding:0.2em 0'>重叠小区（"
//			+ polygon._data.getCount() + "个）</h4>" + html;
//	gisCellDisplayLib.showInfoWindow(html, gisCellDisplayLib.getOriginPointByShape(polygon));
//}
//
///**
// * 把重叠区域的内容形成html
// * 
// * @param {}
// *            composeMark
// */
//function formOverlapHtmlContent(composeMark) {
//	if (!composeMark) {
//		return "";
//	}
//	var cellArray = composeMark.getCellArray();
//	var html = "";
//	var cell;
//	for ( var i = 0; i < cellArray.length; i++) {
//		cell = cellArray[i];
//		html += "<a onclick='javascript:showCellDetail(\"" + cell.cell
//				+ "\")' target='_blank'>"
//				+ (cell.chineseName ? cell.chineseName : cell.cell)
//				+ "</a><br/><br/>";
//	}
//	return html;
//}
//
//function showCellDetailFromPolygon(polygon) {
//	var label = polygon._data.getCell();
//	// alert("显示"+label+"详情");
//	showCellDetail(label);
//}

/**
 * 显示所关联的小区的详情
 * 
 * @param {}
 *            polygon
 */
function showCellDetail(label) {
	// alert("关联的小区为：" + polygon._data.getCell());

	// 看本地缓存有没有
	var cell = null;
	var find = false;
	for ( var i = 0; i < cellDetails.length; i++) {
		cell = cellDetails[i];
		if (cell['label'] === label) {
			// console.log("从缓存找到详情。")
			find = true;
			break;
		}
	}

	if (find) {
		displayCellDetail(cell);
		return;
	}

	$(".loading_cover").css("display", "block");
	$.ajax({
		url : 'getCellDetailForAjaxAction',
		data : {
			'label' : label
		},
		dataType : 'json',
		type : 'post',
		success : function(data) {
			var c = data;
			// try {
			// //c = eval("(" + data + ")");
			// } catch (e) {
			// alert("获取小区详情失败！");
			// return;
			// }
			if (c) {
				cellDetails.push(c);
				displayCellDetail(c);
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
 * 显示小区详情
 * 
 * @param {}
 *            cell
 */
function displayCellDetail(cell) {

	$(".switch_hidden").trigger("click");

	$("#showCellLabelId").html(getValidValue(cell.label));
	$("#showCellNameId").html(getValidValue(cell.name));
	$("#showCellLacId").html(getValidValue(cell.lac));
	$("#showCellCiId").html(getValidValue(cell.ci));
	$("#showCellBcchId").html(getValidValue(cell.bcch));

	$("#showCellTchId").html(getValidValue(cell.tch));
	$("#showCellBsicId").html(getValidValue(cell.bsic));
	$("#showCellAzimuthId").html(getValidValue(cell.bearing));

	$("#showCellDownId").html(getValidValue(cell.downtilt));
	$("#showCellBtsTypeId").html(getValidValue(cell.btstype));
	$("#showCellAntHeightId").html(getValidValue(cell.antHeigh));
	$("#showCellAntTypeId").html(getValidValue(cell.antType));

	$("#showCellLngId").html(getValidValue(cell.longitude));
	$("#showCellLatId").html(getValidValue(cell.latitude));

	$("#showCellCoverareaId").html(getValidValue(cell.coverarea));
	$("#showCellFreqSectionId").html(getValidValue(cell.gsmfrequencesection));
}


function showLoadTip(tip){
	$(".loading_fb").html(tip);
	$(".loading_cover").css("display","block");
}

function hideLoadTip(){
	$(".loading_cover").css("display","none");
}
// 按条件搜索小区
function searchCell() {
gisCellDisplayLib.clearOnlyExtraOverlay();//清除额外覆盖物
	showLoadTip("小区");
	
	
	// 获取输入的值
	var inputValue = $.trim($("#conditionValue").val());
	var conditionType = $("#conditionType").val();

	//console.log("conditionType="+conditionType+",inputValue="+inputValue);
	if ($.trim(inputValue) == "") {
		alert("请输入搜索条件");
		hideLoadTip();
		return;
	}

	// 将特别渲染的polygon恢复默认
	clearSpecRendPolygons();

//	var composeMarkers=gisCellDisplayLib.get("composeMarkers");
//	// 开始遍历
//	size = composeMarkers.length;
//	var cmk;
//	var findcmk;
//	var pl;
//	var find=false;
//	for ( var i = 0; i < size; i++) {
//		cmk = composeMarkers[i];
//		// console.log("cmk.get("+conditionType+")="+cmk.get(conditionType));
//		if (cmk.hasLikeCell(conditionType, inputValue)) {
//			find=true;
//			findcmk=cmk;
//			pl=gisCellDisplayLib.getShapeObjByComposeMarker(findcmk);
//			/*if(map instanceof BMap.Map){
//				pl=cmk.getPolygon();
//			}else{
//				pl=gisCellDisplayLib.ge.getElementById(cmk._cell);
//			}*/
//			// console.log("找到一个:"+cmk.getCell());
//			//specRenderderPolygons.push(cmk.getPolygon());
//			gisCellDisplayLib.changeCellPolygonOptions(findcmk.getCell(), {'fillColor':targetCellColor,'fillOpacity':1}, true);
//		}
//	}
//
//	if (find===false) {
//		alert("未找到符合条件的小区");
//	}else{
//		// 移动地图
//		var cmk=gisCellDisplayLib.getComposeMarkerByShape(pl);
//		gisCellDisplayLib.panTo(cmk.getLng(), cmk.getLat());
//		/*if(map instanceof BMap.Map){
//		gisCellDisplayLib.panTo(pl._data.getLng(), pl._data.getLat());
//		}else{
//			var lnglat=findcmk.getPointArray()[0].split(',');
//		gisCellDisplayLib.panTo(lnglat[0], lnglat[1]);
//		}*/
//	}
//
//	hideLoadTip();
	
	//对所需的小区数据进行预加载，再渲染展示
	var cityId = $("#cityId").val();
	var areaId = $("#queryCellAreaId").val();
	mapGridLib.loadRelaCellByCellParamAndCityIdOnMap(inputValue,conditionType,cityId,areaId,function(){
			var composeMarkers=gisCellDisplayLib.get("composeMarkers");
			// 开始遍历
			size = composeMarkers.length;
			var cmk;
			var findcmk;
			var pl;
			var find=false;
			for ( var i = 0; i < size; i++) {
				cmk = composeMarkers[i];
				// console.log("cmk.get("+conditionType+")="+cmk.get(conditionType));
				if (cmk.hasLikeCell(conditionType, inputValue)) {
					find=true;
					findcmk=cmk;
					pl=gisCellDisplayLib.getShapeObjByComposeMarker(findcmk);
					/*if(map instanceof BMap.Map){
						pl=cmk.getPolygon();
					}else{
						pl=gisCellDisplayLib.ge.getElementById(cmk._cell);
					}*/
					// console.log("找到一个:"+cmk.getCell());
					//specRenderderPolygons.push(cmk.getPolygon());
					gisCellDisplayLib.changeCellPolygonOptions(findcmk.getCell(), {'fillColor':targetCellColor,'fillOpacity':1}, true);
				}
			}
		
			if (find===false) {
				alert("未找到符合条件的小区");
			}else{
				// 移动地图
				var cmk=gisCellDisplayLib.getComposeMarkerByShape(pl);
				gisCellDisplayLib.panTo(cmk.getLng(), cmk.getLat());
				/*if(map instanceof BMap.Map){
				gisCellDisplayLib.panTo(pl._data.getLng(), pl._data.getLat());
				}else{
					var lnglat=findcmk.getPointArray()[0].split(',');
				gisCellDisplayLib.panTo(lnglat[0], lnglat[1]);
				}*/
			}
		
			hideLoadTip();
	});
}

/**
 * 清理特殊渲染的图形
 */
function clearSpecRendPolygons() {
	
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

// 按条件搜索邻区
function searchNcell() {
	gisCellDisplayLib.clearOnlyExtraOverlay();//清除额外覆盖物
	showLoadTip("邻区");
	var inputcell = $.trim($("#cellForNcell").val());
	if (inputcell === "") {
		hideLoadTip();
		alert("请输入小区名！");
		hideLoadTip();
		return;
	}
	var size = ncellsOfCell.length;
	var one;
	var ct = new Date().getTime();
	for ( var i = 0; i < size; i++) {
		one = ncellsOfCell[i];
		if (one.isSame(inputcell)) {
			if (one.getCreateTime() - ct > maxCacheNcellsTime) {
				// 超过缓存时间
				ncellsOfCell.splice(i, 1);
				one = null;
			}
			break;
		}
		one = null;
	}
	if (!one) {
		// 从后台获取
		/*$.ajax({
			url : 'searchNcellByCellForAjaxAction',
			data : {
				'label' : inputcell,
				"areaId" : $("#onearea").val()
			},
			dataType : 'json',
			success : function(data) {
				var size = data.length;
				if (size == 0) {
					hideLoadTip();
					alert("未找到指定小区的邻区！");
					hideLoadTip();
					return;
				} else {
					var ownc = data[0]['cell'];
					var cellToNcell = new CellToNcell(ownc,gisCellDisplayLib.get("composeMarkers"));
					if (!cellToNcell) {
						alert("指定的小区尚不存在！");
						hideLoadTip();
						return;
					} else {
						for ( var i = 0; i < size; i++) {
							cellToNcell.addNcell(data[i]['ncell']);
						}
					}
					// 展现
					showNcell(cellToNcell);
					hideLoadTip();
				}
			},
			error : function(xhr, text, e) {
				alert("邻区查询出错！");
			},complete:function(){
				hideLoadTip();
			}
		});*/
		
		//对所需的邻区数据进行预加载，再渲染展示
		var cityId = $("#cityId").val();
		var areaId = $("#areaId").val();
		mapGridLib.loadNcellDetailsByCellOnMap(inputcell,cityId,areaId,function(ncells){
			if (ncells.length == 0) {
				alert("未找到指定小区的邻区！");
				hideLoadTip();
				return;
			} else {
				var cellToNcell = new CellToNcell(inputcell,gisCellDisplayLib.get("composeMarkers"));
				if (!cellToNcell) {
					alert("指定的小区尚不存在！");
					hideLoadTip();
					return;
				} else {
					for ( var i = 0; i < ncells.length; i++) {
						cellToNcell.addNcell(ncells[i]);
					}
				}
				// 展现
				showNcell(cellToNcell);
				hideLoadTip();
			}
		});
	}
}

/**
 * 渲染邻区
 * 
 * @param cellToNcell
 */
function showNcell(cellToNcell) {
	if (!cellToNcell) {
		return;
	}
	// 先清除之前的搜索结果
	clearSpecRendPolygons();
	// 渲染
	var pl = cellToNcell.getPolygon();
	if (pl) {
		// 先移动。移动会触发事件
		var cmk=gisCellDisplayLib.getComposeMarkerByShape(pl);
		gisCellDisplayLib.panTo(cmk.getLng(),cmk.getLat());
//		gisCellDisplayLib.panTo(pl._data.getLng(),pl._data.getLat());
		gisCellDisplayLib.changeCellPolygonOptions(cellToNcell.getCell(), {'fillColor':targetCellColor,'fillOpacity':1}, true);
	}
	var npls = cellToNcell.getNcells();
	var size=0;
	if(npls instanceof Array){
		size = npls.length;
	}
	for ( var i = 0; i < size; i++) {
		gisCellDisplayLib.changeCellPolygonOptions(npls[i], {'fillColor':targetNcellColor,'fillOpacity':1}, true);
	}

	hideLoadTip();
}

// 按条件搜索频点
function searchFreq() {
gisCellDisplayLib.clearOnlyExtraOverlay();//清除额外覆盖物
	showLoadTip("频点");
	
	var freq = $.trim($("#freqValue").val());
	if (freq == "") {
		hideLoadTip();
		alert("请输入需要查找的频点");
		return;
	}
	if (isNaN(freq)) {
		hideLoadTip();
		alert("请输入数字频点！");
		return;
	}

	clearSpecRendPolygons();

	// 可视区域
	var visiblePolygons=gisCellDisplayLib.get("visiblePolygons");
	var size = visiblePolygons.length;
	var pl;
	var cont;
	var label;
	var cnt=0;
	for ( var i = 0; i < size; i++) {
		cont = null;
		pl = visiblePolygons[i];
		if(!pl){continue;}
		var cmk=gisCellDisplayLib.getComposeMarkerByShape(pl);
		cont = cmk.getContainsFreqCell(freq);
		if (cont != null) {
			cnt++;
			//var pt=gisCellDisplayLib.getCellEdgeCenterPoint(cmk.getCell());
			var pt=gisCellDisplayLib.getLnglatObjByComposeMarker(cmk);
			if(i==1){
				gisCellDisplayLib.panTo(pt.lng, pt.lat);
			}
			if(pt!=null){
				label = new IsTextLabel(pt.lng, pt.lat, cont,null,gisCellDisplayLib);
			}else{
			    label = new IsTextLabel(cmk.getLng(), cmk.getLat(), cont,null,gisCellDisplayLib);
			}
			if (label) {
				specRenderderLabel.push(label);
				gisCellDisplayLib.addOverlay(label);
			}
		}
	}
	
	hideLoadTip();
	if(cnt==0){
		alert("未搜索到指定的频点的信息!");
	}
}



/***************** 地图小区加载改造 start ********************/
function handleMoveendAndZoomend(e, lastOperTime) {
	var winMinLng = map.getBounds().getSouthWest().lng;
	var winMinLat = map.getBounds().getSouthWest().lat;
	var winMaxLng = map.getBounds().getNorthEast().lng;
	var winMaxLat = map.getBounds().getNorthEast().lat;
	//设置当前屏幕经纬度范围
	mapGridLib.setWinLngLatRange(winMinLng,winMinLat,winMaxLng,winMaxLat);
	//每一次移动，缩放结束的独立标识
	currentloadtoken = getLoadToken();
	mapGridLib.setCurrentloadtoken(currentloadtoken);
	/*var gsmtype = $("input[name='freqType']:checked").val();
	var params = {
		'freqType' : gsmtype
	};*/
	mapGridLib.loadGisCell(lastOperTime, currentloadtoken, minResponseInterval);
}
function handleMovestartAndZoomstart(e, lastOperTime) {
	//每一次移动，缩放的独立标识
	currentloadtoken = getLoadToken();
	mapGridLib.setCurrentloadtoken(currentloadtoken);
}
/***************** 地图小区加载改造 end ********************/

