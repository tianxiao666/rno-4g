var gisCellDisplayLib;// 负责显示小区的对象
var map;

var currentloadtoken = "";// 加载任务的token

var minZoom = 17;// 只有大于 该 缩放级别，才真正
var preZoom = 15;// 当前的缩放级别
var minResponseInterval = 1000;// 对事件做响应的最小的间隔
var lastOperTime = 0;// 最后一次操作的时间
var randomShowCnt = 150;// 在不需要全部显示的时候，最大随机显示的个数

var onLoadingGisCell = false;// 是否正在加载小区信息

var celllabel = "";

var specRenderderLabel = new Array();// 频点搜索出来后用于显示的label

var ncellsOfCell = new Array();// 缓存获取到的小区的邻区，存储的是CellToNcell对象

var specRenderderLabel = new Array();// 频点搜索出来后用于显示的label
var targetCellColor = "#B40431";// 暗红色
var targetNcellColor = "#2EFE2E";// 耀眼的绿色
var ncellsOfCell = new Array();// 缓存获取到的小区的邻区，存储的是CellToNcell对象

var mapGridLib //用于将地图分成网格加载小区的对象

var isShowCellName = false; //地图是否显示小区
var dynaPolygon; //动态覆盖图形
var dynaPolyline; //矢量线
var dynaArrow;  //矢量线的箭头
var dynaCellColor = "#1C1C1C"; //当前动态覆盖图所属小区的颜色
var dynaPolygonColor = "blue"; //蓝色
var dynaPolylineColor = "red"; //红色

var matrixObj;
var cellToInRela = new Object();
var cellToOutRela = new Object();

//地图小区右键菜单
var contextMenu = [
	{
		text:'显示in干扰',
		callback:function(){
			var polygon;
			if (contextMenu.length!=0) {
				polygon=contextMenu[contextMenu.length-1].polygon;
				contextMenu.splice(contextMenu.length-1,1);
			}
			//console.log(polygon._data.getCell());
			//$("#interPlanLi").trigger("click");
			//加入判断是否是重叠小区
			var cmk = polygon._data;
			var cmkLng = cmk._lng;
			var cmkLat = cmk._lat;
			if (cmk.getCount() > 1) {
					var html = "";
					if (!cmk) {
						return "";
					}
					var cellArray = cmk.getCellArray();
					var cell;
					for ( var i = 0; i < cellArray.length; i++) {
						cell = cellArray[i];
						html += "<a onclick='showInMatrix(this,\""+cell.cell+"\",\""+cmkLng+"\",\""+cmkLat+"\")' "
							 + " target='_blank'>"+ (cell.chineseName ? cell.chineseName : cell.cell)
							 + "</a><br/><br/>";
					}
					html = "<h4 style='margin:0 0 5px 0;padding:0.2em 0'>重叠小区（"
							+ cmk.getCount() + "个）</h4>" + html;
					gisCellDisplayLib.showInfoWindow(html, gisCellDisplayLib.getOriginPointByShape(polygon));
			} else {
				showInMatrix(polygon);
			}
		}
	},
	{
		text:'显示out干扰',
		callback:function(){
			var polygon;
			if (contextMenu.length!=0) {
				polygon=contextMenu[contextMenu.length-1].polygon;
				contextMenu.splice(contextMenu.length-1,1);
			}
			//console.log(polygon._data.getCell());
			
			//加入判断是否是重叠小区
			var cmk = polygon._data;
			var cmkLng = cmk._lng;
			var cmkLat = cmk._lat;
			if (cmk.getCount() > 1) {
					var html = "";
					if (!cmk) {
						return "";
					}
					var cellArray = cmk.getCellArray();
					var cell;
					for ( var i = 0; i < cellArray.length; i++) {
						cell = cellArray[i];
						html += "<a onclick='showOutMatrix(this,\""+cell.cell+"\",\""+cmkLng+"\",\""+cmkLat+"\")' "
							 + " target='_blank'>"+ (cell.chineseName ? cell.chineseName : cell.cell)
							 + "</a><br/><br/>";
					}
					html = "<h4 style='margin:0 0 5px 0;padding:0.2em 0'>重叠小区（"
							+ cmk.getCount() + "个）</h4>" + html;
					gisCellDisplayLib.showInfoWindow(html, gisCellDisplayLib.getOriginPointByShape(polygon));
			} else {
				showOutMatrix(polygon);
			}
		}
	},
	{
		text:'PCI智能优化',
		callback:function(){
			var polygon;
			if (contextMenu.length!=0) {
				polygon=contextMenu[contextMenu.length-1].polygon;
				contextMenu.splice(contextMenu.length-1,1);
			}
			//console.log(polygon._data.getCell());
			
			//加入判断是否是重叠小区
			var cmk = polygon._data;
			var cmkLng = cmk._lng;
			var cmkLat = cmk._lat;
			if (cmk.getCount() > 1) {
					var html = "";
					if (!cmk) {
						return "";
					}
					var cellArray = cmk.getCellArray();
					var cell;
					for ( var i = 0; i < cellArray.length; i++) {
						cell = cellArray[i];
						html += "<a onclick='pciAnalysis(this,\""+cell.cell+"\",\""+cmkLng+"\",\""+cmkLat+"\")' "
							 + " target='_blank'>"+ (cell.chineseName ? cell.chineseName : cell.cell)
							 + "</a><br/><br/>";
					}
					html = "<h4 style='margin:0 0 5px 0;padding:0.2em 0'>重叠小区（"
							+ cmk.getCount() + "个）</h4>" + html;
					gisCellDisplayLib.showInfoWindow(html, gisCellDisplayLib.getOriginPointByShape(polygon));
			} else {
				pciAnalysis(polygon);
			}
		}
	}
];

$(document).ready(
		function() {
			gisCellDisplayLib = new GisCellDisplayLib(map, minZoom,
					randomShowCnt, null, {
					    'clickFunction' : clickFunction,
					    'mouseoverFunction' : mouseoverFunction,
						'mouseoutFunction' : mouseoutFunction
					}, {'showCellLabel' : false}, contextMenu, preZoom);
			var lng = $("#hiddenLng").val();
			var lat = $("#hiddenLat").val();
			map = gisCellDisplayLib.initMap("map_canvas", lng, lat,{
										'movestart' : handleMovestartAndZoomstart,
										'zoomstart' : handleMovestartAndZoomstart,
										'moveend' : handleMoveendAndZoomend,
										'zoomend' : handleMoveendAndZoomend });
			
			//以网格形式在地图加载小区
			mapGridLib = new MapGridLib(gisCellDisplayLib,"conditionForm","loadingCellTip"); 
			//以城市为单位创建区域网格
			var cityName = $("#cityId").find("option:selected").text().trim();
			mapGridLib.createMapGrids(cityName);

			var areaId = $("#cityId").val();


			// 获取gis数据数据
			currentloadtoken = getLoadToken();

			bindNormalEvent();
			
			//区域联动
			initAreaCascade()
		
			
		$("#loadCellToMap").click(function(){
			$("#hiddenCurrentPage").val("1");
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
			var gsmtype = $("input[name='freqType']:checked").val();
			var params = {
				'freqType' : gsmtype
			};
			//t-minResponseInterval保证不延迟加载
			mapGridLib.loadLteCell(t-minResponseInterval, currentloadtoken, minResponseInterval,params);
		});

//		$("#locatecell").click(function(){
//			gisCellDisplayLib.panTo(113.36079487718192,23.179828049350228);
//			var option_serverCell={
//					'fillColor':'#FF0000'
//			};
//			var cell="GBVDGZ3";
//			gisCellDisplayLib.changeCellPolygonOptions(cell,option_serverCell,false);	
//		});
	
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
}, false);

function bindNormalEvent() {
	
	//加载干扰矩阵
	getLatelyLteMatrix();
	
	//打开查找小区窗口
	$(".queryButton").click(function() {
		//$("#searchDiv").slideToggle();
		$("#searchDiv").toggle();
	});
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
	// 清除搜索结果
	$("#clearSearchResultBtn").click(function() {
		clearSpecRendPolygons();
	});
	
	//加载干扰矩阵
	$("#loadPciMatrix").click(function(){
		//隐藏pci分析框
		$("#interference_dialogId").hide();
		//恢复默认polygon颜色
		gisCellDisplayLib.resetPolygonToDefaultOutlook();
		
		var jobId=$("#pciMatrix").val();
		loadMatrix(jobId);
	});
}


function initPageParam() {
	$("#hiddenPageSize").val(100);
	$("#hiddenCurrentPage").val(1);
	$("#hiddenTotalPageCnt").val(0);
	$("#hiddenTotalCnt").val(0);
	$("#hiddenForcedStartIndex").val(-1);
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
				
				// stsResult = getRnoTrafficRenderer('pdinterference');
				getSubAreas("cityId", "areaId", "区/县", function(data) {
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
					//$("#areaId").trigger("change");
					$("#areaId").append("<option value=\"-1\">全部</option>");
				});
			});

	$("#areaId").change(function() {
		var lnglat = $("#areaid_" + $("#areaId").val()).val();
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

		//以城市为单位创建区域网格
		clearAll(); //清除缓存
		var cityName = $("#cityId").find("option:selected").text().trim();
		mapGridLib.createMapGrids(cityName);
		
		//加载干扰矩阵列
		getLatelyLteMatrix();
	});

}

// 重置查询表单的值
function resetQueryCondition() {
	$("#hiddenCurrentPage").val(1);
	$("#hiddenTotalPageCnt").val(0);
	$("#hiddenTotalCnt").val(0);
	$("#hiddenForcedStartIndex").val(-1);
	$("#hiddenPageSize").val(100);

}


function loadMatrix(jobId) {
	showOperTips("loadingDataDiv", "loadContentId", "正在加载干扰矩阵");
	$.ajax({
		url : 'getLteMatrixByIdForAjaxAction',
		data : {
			'jobId' : jobId
		},
		dataType : 'json',
		type : 'post',
		success : function(data) {
			//console.log(data['flag']);
			var flag = data['flag'];
			if(flag == 'true'){
				matrixObj = eval("("+data['val']+")");
				$("#matrixTip").text("当前所加载的干扰矩阵为："+$("#pciMatrix").find("option:selected").text());
			} else {
				$("#matrixTip").text("加载干扰矩阵失败！");
			}
		},
		complete : function() {
			hideOperTips("loadingDataDiv");
		}
	});
}

/**
 * 获取最近十次干扰矩阵，显示在页面列表
 */
function getLatelyLteMatrix() {
	
	$("#matrixTip").text("正在加载干扰矩阵列表...");
	//清除缓存干扰矩阵信息
	matrixObj = null;
	
	$("#pciMatrix").html("");
	areaid=$("#cityId").find("option:selected").val();
	
	$.ajax({
		url : 'getLatelyLteMatrixByCityIdForAjaxAction',
		data : {
			'cityId' : areaid
		},
		dataType : 'json',
		type : 'post',
		success : function(data) {
			//console.log(raw);
//			var data = eval("(" + raw + ")");
			if(data.length != 0) {
				var optHtml = "";
				for ( var i = 0; i < data.length; i++) {
					var one = data[i];
					optHtml += "<option value='"+one['JOB_ID']+"'>"+one['TASK_NAME']+"</option>";
				}
				//console.log(optHtml);
				$("#pciMatrix").append(optHtml);
				$("#matrixTip").text("请选择加载的干扰矩阵");
			} else {
				$("#matrixTip").text("无相应的干扰矩阵,请先进行干扰矩阵计算!");
				//$("#latelyTaskId").val("-1");
				//$("#latelyMartixDescId").val("-1");
			}
		},
		complete : function() {
			//$(".loading_cover").css("display", "none");
		}
	});
}

function showInMatrix(polygon, cell, lng, lat) {

	if (!polygon) {
		return;
	}
	//清空界面数据
	//隐藏pci分析框
	$("#interference_dialogId").hide();
	//恢复默认polygon颜色
	gisCellDisplayLib.resetPolygonToDefaultOutlook();
	
	//获取cell名
	if(cell) {
		//console.log(cell);
		celllabel = cell;
	} else {
		var cmk = gisCellDisplayLib.getComposeMarkerByShape(polygon);
		celllabel = cmk.getCell();
	}
	
	if(matrixObj == null) {
		alert("请先选择加载的干扰矩阵！");
		return;
	}
	//alert("小区名："+celllabel+"，获取in干扰信息，添加颜色");
	//获取点击加载干扰矩阵时缓存的matrixObj
	//通过小区名获取干扰矩阵in干扰信息
	//主小区显示黑色，其他小区显示黄色
	showOperTips("loadingDataDiv", "loadContentId", "正在处理");
	var ncells = new Array();
	for ( var i = 0; i < matrixObj.length; i++) {
		if(celllabel == matrixObj[i]['CELL']) {
			ncells.push(matrixObj[i]['NCELL']);
		}
	}

	var cellOpt={};
	cellOpt={'fillColor':'#10000E','fillOpacity':1};
	gisCellDisplayLib.changeCellPolygonOptions(celllabel,cellOpt,true);
	
	var ncellOpt={};
	ncellOpt={'fillColor':'#458B00','fillOpacity':1};
	for ( var i = 0; i < ncells.length; i++) {
		gisCellDisplayLib.changeCellPolygonOptions(ncells[i],ncellOpt,true);
	}
	hideOperTips("loadingDataDiv");
}

function showOutMatrix(polygon, cell, lng, lat) {

	if (!polygon) {
		return;
	}
	//清空界面数据
	//隐藏pci分析框
	$("#interference_dialogId").hide();
	//恢复默认polygon颜色
	gisCellDisplayLib.resetPolygonToDefaultOutlook();
	
	//获取cell名
	if(cell) {
		//console.log(cell);
		celllabel = cell;
	} else {
		var cmk = gisCellDisplayLib.getComposeMarkerByShape(polygon);
		celllabel = cmk.getCell();
	}
	
	if(matrixObj == null) {
		alert("请先选择加载的干扰矩阵！");
		return;
	}
	//alert("小区名："+celllabel+"，获取out干扰信息，添加颜色");
	//获取点击加载干扰矩阵时缓存的matrixObj
	//通过小区名获取干扰矩阵in干扰信息
	//主小区显示黑色，其他小区显示黄色
	
	showOperTips("loadingDataDiv", "loadContentId", "正在处理");
	var ncells = new Array();
	for ( var i = 0; i < matrixObj.length; i++) {
		if(celllabel == matrixObj[i]['NCELL']) {
			ncells.push(matrixObj[i]['CELL']);
		}
	}
	var cellOpt={};
	cellOpt={'fillColor':'#10000E','fillOpacity':1};
	gisCellDisplayLib.changeCellPolygonOptions(celllabel,cellOpt,true);
	
	var ncellOpt={};
	ncellOpt={'fillColor':'#458B00','fillOpacity':1};
	for ( var i = 0; i < ncells.length; i++) {
		gisCellDisplayLib.changeCellPolygonOptions(ncells[i],ncellOpt,true);
	}
	hideOperTips("loadingDataDiv");
}

function pciAnalysis(polygon, cell, lng, lat){
	if (!polygon) {
		return;
	}

	//先清空展示表
	$("#pciAnalysisTable tr:gt(0)").remove();
	//恢复默认polygon颜色
	gisCellDisplayLib.resetPolygonToDefaultOutlook();
	
	//获取cell名
	if(cell) {
		//console.log(cell);
		celllabel = cell;
	} else {
		var cmk = gisCellDisplayLib.getComposeMarkerByShape(polygon);
		celllabel = cmk.getCell();
	}
	
	if(matrixObj == null) {
		alert("请先选择加载的干扰矩阵！");
		return;
	}
	//alert("小区名："+celllabel+"，获取同站小区和pci，计算干扰值，并用窗口显示列表，提供修改");
	showOperTips("loadingDataDiv", "loadContentId", "正在分析计算");
	$.ajax({
		url : 'getSameStationCellsByLteCellIdForAjaxAction',
		data : {
			'lteCell' : celllabel
		},
		dataType : 'json',
		type : 'post',
		success : function(data) {
			if(data[0]['CELL'] != celllabel) {
				alert("加载数据出错！");
				return;
			}
			var cell = data[0]['CELL'];
			var pci = data[0]['PCI'];
			
			$("#cellname").html(cell);
			
			var tableHtml = "<tr><td>原方案</td>";
			var inVal = 0;
			var outVal = 0;
			var totVal = 0;
			for ( var j = 0; j < matrixObj.length; j++) {
				if(cell == matrixObj[j]['CELL']) {
					//关联度*mod值
					mod = getModValByPci(pci,matrixObj[j]['NCELL_PCI'])
					inVal += Number(matrixObj[j]['RELA_VAL'])*mod;
				}
				if(cell == matrixObj[j]['NCELL']) {
					//关联度*mod值
					mod = getModValByPci(pci,matrixObj[j]['CELL_PCI']);
					outVal += Number(matrixObj[j]['RELA_VAL'])*mod;
				}
			}
			inVal = Number(inVal).toFixed(5);
			outVal = Number(outVal).toFixed(5);
			var totVal = Number(inVal) + Number(outVal);
			tableHtml += "<td>"+inVal+"</td><td>"+outVal+"</td><td>"+totVal+"</td>";
			tableHtml += "<td></td></tr>";
			
			for ( var i = 1; i < data.length; i++) {
				tableHtml += "<tr><td>"+pci+"<->"+data[i]['PCI']+"</td>";
				var cellInRelaVal = 0;
				var cellOutRelaVal = 0;
				var mod = 0;
				for ( var j = 0; j < matrixObj.length; j++) {
					if(cell == matrixObj[j]['CELL']) {
						//关联度*mod值
						mod = getModValByPci(data[i]['PCI'],matrixObj[j]['NCELL_PCI'])
						cellInRelaVal += Number(matrixObj[j]['RELA_VAL'])*mod;
					}
					if(cell == matrixObj[j]['NCELL']) {
						//关联度*mod值
						mod = getModValByPci(data[i]['PCI'],matrixObj[j]['CELL_PCI']);
						cellOutRelaVal += Number(matrixObj[j]['RELA_VAL'])*mod;
					}
				}
				cellInRelaVal = Number(cellInRelaVal).toFixed(5);
				cellOutRelaVal = Number(cellOutRelaVal).toFixed(5);
				var cellTotRelaVal = Number(cellInRelaVal) + Number(cellOutRelaVal);
				tableHtml += "<td>"+cellInRelaVal+"</td><td>"+cellOutRelaVal+"</td><td>"+cellTotRelaVal+"</td>";
				tableHtml += "<td><a onclick='changeLteCellPci(\""+cell+"\",\""+data[i]['PCI']+"\",\""+data[i]['CELL']+"\",\""+pci+"\")'>修改</a></td>";
				tableHtml += "</tr>";
			}
			$("#pciAnalysisTable").append(tableHtml);
			//显示pci分析
			$('#interference_dialogId').show();
 		},
		complete : function() {
			hideOperTips("loadingDataDiv");
		}
	});

	
}

/**
 * 更新cell1的pci为pci1，cell2的pci为pci2
 * @param cell
 * @param pci
 * @param cell2
 * @param pci2
 */
function changeLteCellPci(cell1,pci1,cell2,pci2) {
	//console.log("cell1="+cell1+",pci1="+pci1+"; cell2="+cell2+",pci2="+pci2);
	showOperTips("loadingDataDiv", "loadContentId", "正在更新pci");
	$.ajax({
		url : 'changeLteCellPciForAjaxAction',
		data : {
			'cell1' : cell1,
			'pci1' : pci1,
			'cell2' : cell2,
			'pci2' : pci2
		},
		dataType : 'json',
		type : 'post',
		success : function(data) {
			var flag = data['flag'];
			if(flag) {
				alert("更新成功！");
				//显示pci分析
				$('#interference_dialogId').hide();
			} else {
				alert("更新失败！");
				
			}
 		},
		complete : function() {
			hideOperTips("loadingDataDiv");
		}
	});

}

/**
 * 通过pci求mod值
 * @param cellPci
 * @param ncellPci
 * @returns {Number}
 */
function getModValByPci(cellPci,ncellPci) {
	var result = 0;
	cellPci = Number(cellPci);
	ncellPci = Number(ncellPci);
	if(cellPci%3==(ncellPci%3)) {
		result += 1;
	}
	if(cellPci%6==(ncellPci%6)) {
		result += 0.8;
	}
	if(cellPci%30==(ncellPci%30)) {
		result += 0.1;
	}
	return result;
}


function clickFunction(){
	
}

var titleLabels = new Array();
var offsetSize;
if(typeof gisCellDisplayLib!="undefined")
offsetSize = gisCellDisplayLib.getOffsetSize(3, 3);


// 鼠标移动事件的响应函数
function mouseoverFunction(polygon, event) {
	var cmk=gisCellDisplayLib.getComposeMarkerByShape(polygon);
	//将显示信息的坐标lng偏移+0.0005
	var lng = Number(cmk.getLng())+0.0005;
	var lat = Number(cmk.getLat());
	var point = gisCellDisplayLib.createPoint(lng,lat);
//	var point = new BMap.Point(polygon._data.getLng(), polygon._data.getLat());
	if(map instanceof BMap.Map){
	
	var label = new BMap.Label(gisCellDisplayLib.getTitleContent(polygon), {
		'position' : point,
		'offset' : offsetSize
	});
	}
	titleLabels.push(label);
	gisCellDisplayLib.addOverlay(label);
}
// 鼠标移过事件的响应函数
function mouseoutFunction(polygon, event) {
	for ( var i = 0; i < titleLabels.length; i++) {
		gisCellDisplayLib.removeOverlay(titleLabels[i]);
	}
	titleLabels.splice(0, titleLabels.length);
}

/**
 * 清除全部的数据
 */
function clearAll() {
//	//清除动态覆盖图，方向线，箭头
//	map.removeOverlay(dynaPolygon);
//	map.removeOverlay(dynaPolyline);
//	map.removeOverlay(dynaArrow);
	
	//隐藏pci分析框
	$("#interference_dialogId").hide();
	
	//恢复默认polygon颜色
	gisCellDisplayLib.resetPolygonToDefaultOutlook();
	
	//清除查询form
	resetQueryCondition();
	
	// 地图显示相关
	gisCellDisplayLib.clearData();
	
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



/***************** 查找地图小区 start ********************/
// 按条件搜索小区
function searchCell() {
	gisCellDisplayLib.clearOnlyExtraOverlay();//清除额外覆盖物
	showOperTips("loadingDataDiv", "loadContentId", "正在查找小区");
	
	// 获取输入的值
	var inputValue = $.trim($("#conditionValue").val());
	var conditionType = $("#conditionType").val();

	//console.log("conditionType="+conditionType+",inputValue="+inputValue);
	if ($.trim(inputValue) == "") {
		alert("请输入搜索条件");
		hideOperTips("loadingDataDiv");
		return;
	}

	// 将特别渲染的polygon恢复默认
	clearSpecRendPolygons();
	
	//对所需的小区数据进行预加载，再渲染展示
	var cityId = $("#cityId").val();
	var areaId = $("#areaId").val();
	mapGridLib.loadLteRelaCellByCellParamAndCityIdOnMap(inputValue,conditionType,cityId,areaId,function(){
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
				gisCellDisplayLib.changeCellPolygonOptions(findcmk.getCell(), {'fillColor':targetCellColor,'fillOpacity':1}, true);
			}
		}
	
		if (find===false) {
			alert("未找到符合条件的小区");
		}else{
			// 移动地图
			var cmk=gisCellDisplayLib.getComposeMarkerByShape(pl);
			gisCellDisplayLib.panTo(cmk.getLng(), cmk.getLat());
		}
		hideOperTips("loadingDataDiv");
	});
}


// 按条件搜索邻区
function searchNcell() {
	gisCellDisplayLib.clearOnlyExtraOverlay();//清除额外覆盖物
	showOperTips("loadingDataDiv", "loadContentId", "正在查找邻区");
	var inputcell = $.trim($("#cellForNcell").val());
	if (inputcell === "") {
		//hideLoadTip("loadingDataDiv");
		alert("请输入小区名！");
		hideOperTips("loadingDataDiv");
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
		//对所需的邻区数据进行预加载，再渲染展示
		var cityId = $("#cityId").val();
		var areaId = $("#areaId").val();
		mapGridLib.loadNcellDetailsByCellOnMap(inputcell,cityId,areaId,function(ncells){
			if (ncells.length == 0) {
				alert("未找到指定小区的邻区！");
				hideOperTips("loadingDataDiv");
				return;
			} else {
				var cellToNcell = new CellToNcell(inputcell,gisCellDisplayLib.get("composeMarkers"));
				if (!cellToNcell) {
					alert("指定的小区尚不存在！");
					hideOperTips("loadingDataDiv");
					return;
				} else {
					for ( var i = 0; i < ncells.length; i++) {
						cellToNcell.addNcell(ncells[i]);
					}
				}
				// 展现
				showNcell(cellToNcell);
				hideOperTips("loadingDataDiv");
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

	hideOperTips("loadingDataDiv");
}

// 按条件搜索频点
function searchFreq() {
	gisCellDisplayLib.clearOnlyExtraOverlay();//清除额外覆盖物
	showOperTips("loadingDataDiv", "loadContentId", "正在查找频点");
	
	var freq = $.trim($("#freqValue").val());
	if (freq == "") {
		hideOperTips("loadingDataDiv");
		alert("请输入需要查找的频点");
		return;
	}
	if (isNaN(freq)) {
		hideOperTips("loadingDataDiv");
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
	
	hideOperTips("loadingDataDiv");
	if(cnt==0){
		alert("未搜索到指定的频点的信息!");
	}
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
/***************** 查找地图小区 end ********************/


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
	var gsmtype = $("input[name='freqType']:checked").val();
	var params = {
		'freqType' : gsmtype
	};
	mapGridLib.loadLteCell(lastOperTime, currentloadtoken, minResponseInterval, params);
}
function handleMovestartAndZoomstart(e, lastOperTime) {
	//每一次移动，缩放的独立标识
	currentloadtoken = getLoadToken();
	mapGridLib.setCurrentloadtoken(currentloadtoken);
}
/***************** 地图小区加载改造 end ********************/


/**************** 拖动频点干扰详情窗体 start******************/

if(document.getElementById){
    (
      function(){
        if (window.opera){ document.write("<input type='hidden' id='Q' value=' '>"); }
      
        var n = 500;
        var dragok = false;
        var y,x,d,dy,dx;
        
        function move(e)
        {
          if (!e) e = window.event;
          if (dragok){
            d.style.left = dx + e.clientX - x + "px";
            d.style.top  = dy + e.clientY - y + "px";
            return false;
          }
        }
        
        function down(e){
          if (!e) e = window.event;
          var temp = (typeof e.target != "undefined")?e.target:e.srcElement;
          if (temp.tagName != "HTML"|"BODY"  &&  temp.className != "dragclass"){
            temp = (typeof temp.parentNode != "undefined")?temp.parentNode:temp.parentElement;
          }
          if('TR'==temp.tagName){
            temp = (typeof temp.parentNode != "undefined")?temp.parentNode:temp.parentElement;
            temp = (typeof temp.parentNode != "undefined")?temp.parentNode:temp.parentElement;
            temp = (typeof temp.parentNode != "undefined")?temp.parentNode:temp.parentElement;
          }
        
          if (temp.className == "interferDialog"){
            if (window.opera){ document.getElementById("Q").focus(); }
            dragok = true;
            temp.style.zIndex = n++;
            d = temp;
            dx = parseInt(getStyle(temp,"left"))|0;
            dy = parseInt(getStyle(temp,"top"))|0;
            x = e.clientX;
            y = e.clientY;
            document.onmousemove = move;
            return false;
          }
        }
        
        function up(){
          dragok = false;
          document.onmousemove = null;
        }
        
        document.onmousedown = down;
        document.onmouseup = up;
      
      }
    ) ();
  }
function getStyle(d,a){
  if (d.currentStyle){ 
    var curVal=d.currentStyle[a]
  }else{ 
    var curVal=document.defaultView.getComputedStyle(d, null)[a]
  } 
  return curVal;
}
/**************** 拖动频点干扰详情窗体 end******************/