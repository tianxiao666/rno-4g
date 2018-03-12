var $ = jQuery.noConflict();
var gisCellGeDisplayLib;// 负责显示小区的对象
var ge;
var map;
var cellDetails = new Array();// 小区详情

//
var minZoom = 15;// 只有大于 该 缩放级别，才真正
var randomShowCnt = 50;// 在不需要全部显示的时候，最大随机显示的个数

var currentloadtoken = "";// 加载的token，每次分页加载都要比对。

var currentAreaCenter;// 当前所选区域的中心点

var totalCellCnt = 0;// 小区总数

$(document).ready(function() {
	// 隐藏详情面板
	hideDetailDiv();

	// loadScript("initMap");
	// 初始化地图
	//initMap();
	// 初始化GE地图
	initGeMap();
});
/**
 * 初始化GE地图
 */
function initGeMap() {
	
	gisCellGeDisplayLib = new GisCellGeDisplayLib(ge,null,{
				'clickFunction' : handleClickGe});
	//116.3902750,39.9914944 北京 113.591766357422003000,24.791721545708778500 武江
//	var lng = "113.591766357422003000";//
//	var lat = "24.791721545708778500";//
	var lng = $("#hiddenLng").val();
	var lat = $("#hiddenLat").val();
//	map = new google.maps.Map(document.getElementById("map3d"));
//	map = new GMap2(document.getElementById("map3d"));
	gisCellGeDisplayLib.initGeMap("map_canvas", lng, lat);
	setInterval(function(){ge=gisCellGeDisplayLib.ge;},500);
	bindEvent();
	// 加载数据
	currentloadtoken = getLoadToken();
	getGisCell(currentloadtoken);
}
/**
 * 初始化地图
 */
function initMap() {
	 
	gisCellDisplayLib = new GisCellDisplayLib(map, minZoom, randomShowCnt,
			null, {
				'clickFunction' : handleClick,
				'mouseoverFunction' : null,
				'mouseoutFunction' : null,
				'rightclickFunction':null
			});
	var lng = $("#hiddenLng").val();
	var lat = $("#hiddenLat").val();
	map = gisCellDisplayLib.initMap("map_canvas", lng, lat, {
		"tilesloaded" : function() {
			bindEvent();
			// 加载数据
			currentloadtoken = getLoadToken();
			getGisCell(currentloadtoken);
		}
	});

	// 创建信息窗口
	gisCellDisplayLib.createInfoWindow("");
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

function bindEvent() {

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

	// 区域选择改变
	$("#queryCellAreaId").change(function() {
		var lnglat = $("#areaid_" + $("#queryCellAreaId").val()).val();
		//console.log("改变区域:" + lnglat);
		// 清除当前区域的数据
		clearAll();

		// 地图中心点
		var lls = lnglat.split(",");
		//map.panTo(new BMap.Point(lls[0], lls[1]));// 这个偏移可以不理会
		gisCellGeDisplayLib.panTo(lls[0], lls[1]);
		// 重置查询表单的数据
		resetQueryCondition();
		// 重新获取新区域的数据
		// 重新获取新区域的数据
		currentloadtoken = getLoadToken();
		getGisCell(currentloadtoken);

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
		map.closeInfoWindow();
	} catch (e) {

	}
	try {
		map.clearOverlays();
	} catch (e) {

	}

	cellDetails.splice(0, cellDetails.length);
	
	//gisCellDisplayLib.clearData();

}

function resetQueryCondition() {
	$("#hiddenCurrentPage").val(1);
	$("#hiddenTotalPageCnt").val(0);
	$("#hiddenTotalCnt").val(0);
	$("#hiddenForcedStartIndex").val(-1);
	$("#hiddenPageSize").val(100);

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
					if (loadToken != currentloadtoken) {
						// 新的加载开始了，这些旧的数据要丢弃
						// console.log("丢弃此次的加载结果。");
						return;
					}
					// console.log(data);
					var obj = data;
					try {
						// obj = eval("(" + data + ")");
						// alert(obj['celllist']);
						// showGisCell(obj['gisCells']);
//						gisCellDisplayLib.showGisCell(obj['gisCells']);
						gisCellGeDisplayLib.showGisCell(obj['gisCells']);
//						gisCellGeDisplayLib.showGisCells(obj.gisCells);
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
							totalCellCnt = totalCnt;
							$("#hiddenTotalCnt").val(totalCnt);

							if (totalPageCnt >= currentPage) {
								// console.log("继续获取下一页小区");
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



/**
 * 处理polygon的点击事件
 * 
 * @param {}
 *            polygon
 */
function handleClick(polygon, event) {
	var cmk = polygon._data;
	if (cmk.getCount() > 1) {
		scatterAll(polygon);
	} else {
		showCellDetailFromPolygon(polygon);
	}
}
function handleClickGe(ge,polymark, event) {
	showCellDetail(polymark.getId());
}

/**
 * 处理鼠标移动事件
 * 
 * @param {}
 *            polygon
 * @param {}
 *            event
 */
function handleMouseover(polygon, event) {
	var cmk = polygon._data;
	if (cmk.getCount() > 1) {
		// scatterAll(polygon);
	} else {
		// showCellTitle(polygon);
	}
}

// 显示小区标题
function showCellTitle(polygon) {
	// console.log("show cell title");
}

/**
 * 展开重叠的小区
 * 
 * @param {}
 *            polygon
 */
function scatterAll(polygon) {

	var html = formOverlapHtmlContent(polygon._data);
	html = "<h4 style='margin:0 0 5px 0;padding:0.2em 0'>重叠小区（"
			+ polygon._data.getCount() + "个）</h4>" + html;

	gisCellDisplayLib.showInfoWindow(html, new BMap.Point(new Number(
			polygon._data.getLng()), new Number(polygon._data.getLat())));

}

/**
 * 把重叠区域的内容形成html
 * 
 * @param {}
 *            composeMark
 */
function formOverlapHtmlContent(composeMark) {
	if (!composeMark) {
		return "";
	}
	var cellArray = composeMark.getCellArray();
	var html = "";
	var cell;
	for ( var i = 0; i < cellArray.length; i++) {
		cell = cellArray[i];
		html += "<a onclick='javascript:showCellDetail(\"" + cell.cell
				+ "\")' target='_blank'>"
				+ (cell.chineseName ? cell.chineseName : cell.cell)
				+ "</a><br/><br/>";
	}
	return html;
}

function showCellDetailFromPolygon(polygon) {
	var label = polygon._data.getCell();
	// alert("显示"+label+"详情");
	showCellDetail(label);
}

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
