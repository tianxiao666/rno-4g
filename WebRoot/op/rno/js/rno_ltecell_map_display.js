var chineseToCode = [ {
	'code' : 'SITE_STYLE',
	'name' : '基站类型'
}, 
/*{
	'code' : 'LOCAL_CELLID',
	'name' : '本地小区标识'
}, */
{
	'code' : 'PCI',
	'name' : 'PCI'
}, {
	'code' : 'LONGITUDE',
	'name' : '小区经度'
}, {
	'code' : 'LATITUDE',
	'name' : '小区纬度'
}, {
	'code' : 'GROUND_HEIGHT',
	'name' : '天线挂高'
}, {
	'code' : 'AZIMUTH',
	'name' : '方向角'
}, {
	'code' : 'STATION_CFG',
	'name' : '开站配比'
}, {
	'code' : 'BAND_TYPE',
	'name' : '频带类型'
}, {
	'code' : 'DOWNTILT',
	'name' : '下倾角'
}, {
	'code' : 'FRAME_CFG',
	'name' : '子帧分配'
}, {
	'code' : 'COVER_TYPE',
	'name' : '覆盖类型'
}, {
	'code' : 'M_DOWNTILT',
	'name' : '机械下倾角'
}, {
	'code' : 'SPECIAL_FRAME_CFG',
	'name' : '特殊子帧'
}, {
	'code' : 'RSPOWER',
	'name' : '参考信号功率'
}, {
	'code' : 'E_DOWNTILT',
	'name' : '电子下倾角'
}, {
	'code' : 'IS_VIP',
	'name' : '是否VIP站点'
}, {
	'code' : 'TAC',
	'name' : 'TAC'
}, {
	'code' : 'RRUNUM',
	'name' : 'RRU数量'
}, {
	'code' : 'TAL',
	'name' : 'TAL'
}, {
	'code' : 'RRUVER',
	'name' : 'RRU型号'
}, {
	'code' : 'STATE',
	'name' : '开通情况'
}, {
	'code' : 'CELL_RADIUS',
	'name' : '小区覆盖半径'
}, {
	'code' : 'ANTENNA_TYPE',
	'name' : '天线型号'
}, {
	'code' : 'OPERATION_TIME',
	'name' : '开通日期'
}, {
	'code' : 'BAND',
	'name' : '带宽'
}, {
	'code' : 'INTEGRATED',
	'name' : '天线是否合路'
}, {
	'code' : 'BUILD_TYPE',
	'name' : '建设类型'
}, {
	'code' : 'EARFCN',
	'name' : '下行频点'
}, {
	'code' : 'PDCCH',
	'name' : 'PDCCH'
}, {
	'code' : 'PA',
	'name' : 'PA'
}, {
	'code' : 'PB',
	'name' : 'PB'
} ];
//显示同一站的小区
var currentCellArray = null;
var currentIndex = 0;


var submitOK = true;//判断lte小区编辑信息是否通过验证
//var isNext = true;//用于lte信息编辑页面中，判断是否对下一个输入框进行验证

var gisCellDisplayLib;// 负责显示小区的对象

// 当前加载的小区所属的区域id
var curAreaId = null;

var onLoadingGisCell = false;// 是否正在加载小区信息

var gisCellGeDisplayLib;// 负责显示小区的对象

var ge;
var map;

var cellDetails = new Array();// 小区详情

var minZoom = 15;// 只有大于 该 缩放级别，才真正

var randomShowCnt = 50;// 在不需要全部显示的时候，最大随机显示的个数

var currentloadtoken = "";// 加载的token，每次分页加载都要比对。

var currentAreaCenter;// 当前所选区域的中心点

var totalCellCnt = 0;// 小区总数

var cacheCells = new Array(); //缓存的小区，包含部分详情

var collPairsList = new Array(); //缓存的冲突小区
var confPairsList = new Array(); //缓存的混淆小区

//定义地图上的polygon右键弹出菜单
var pciContextMenu=[{
						text:'同PCI的其他小区',
						callback:function(){
							var polygon;
							if (pciContextMenu.length!=0) {
								polygon = pciContextMenu[pciContextMenu.length-1].polygon;
								pciContextMenu.splice(pciContextMenu.length-1,1);
								//console.log("callback:pciContextMenu回调函数："+pciContextMenu);
							}
								//console.log("同PCI的其他小区");
								showLteCellsWithSamePciClick(polygon);
							}
					}
]

$(document)
		.ready(
				function() {
					// 隐藏详情面板
					hideDetailDiv();
					
					//显示可以切换的地图
					//$("#trigger").val(glomapid=='null' || glomapid=='baidu'?"切换谷歌":"切换百度");			
					//bindNormalEvent();
					
					initAreaCascadeLteCell();
					
					// 第一次设置cityId到隐藏域
					$("#realPostAreaId").val($("#cityId").val());
					
					//初始化GE地图
					initGeMap();
					
					//编辑lte小区详情
					$("#editLteCell").click(function() {
						var lcid = $("#lteCellId").val();
						getLteCellDetailsById(lcid);
						$("#editLteCellMessage").css({
							"top" : (100) + "px",
							"right" : (450) + "px",
							"width" : (668) + "px",
							"z-index" : (30)
						});
						$("#editLteCellMessage").show();
					});
					//提交更改的lte小区详情
					$("#btnUpdate").click(function() {
					    
					     //提交前再做一次全局检验
					     var length = chineseToCode.length;
						 
						 for ( var i = 0; i < length; i++) {
						    var oneKey = chineseToCode[i];
						    var code = oneKey['code'];      //元素id值
						    var value = $("#"+code).val(); //元素value值
						    
						    //如果submitOK为true，继续验证下一个元素，反之则不再循环
						 	if(submitOK==true){
								checkDateType(code,value);
						 	} else {
						 		break;
						 	}
						 }
						 if(submitOK) {
						 	 $("#lteCellDetailForm")
								.ajaxSubmit({
										url : "updateLteCellDetailsForAjaxAction",
										dataType : 'text',
										success : function(raw) {
									
												var data=null;
												try{
													data=eval("("+raw+")");
													if(!data){
														alert("未知错误！请联系管理员！");
													}else{
														if(data['flag']==true){
															//
															alert("更新成功！");
															$("#editLteCellMessage").hide();
														}else{
															alert("更新失败！");
														}
													}
												}catch(err){
												    alert("未知错误！请联系管理员！");
												}							
										},
										complete : function() {
											//alert("更新完成");
											$(".loading_cover").css("display", "none");
										}
								});
						 } else {
						 	
						 }
					});
					//重置当前编辑lte小区详情
					$("#btnReset").click(function() {
						 //获取缓存的目标小区，即currentCellArray的第一个小区
						 var oneLteCellDetail = currentCellArray[0];
						 //加载到页面重置数据
						 showOneLteCellDetail(oneLteCellDetail);
					});
			});
			
/**
*初始化GE地图
*/
function initGeMap() {
			//根据登录用户加载地图
			gisCellDisplayLib = new GisCellDisplayLib(map, minZoom,
						randomShowCnt, null,  {
				'clickFunction' : showLteCellDetailClick,
				'mouseoverFunction' : null,
				'mouseoutFunction' : null,
				'rightclickFunction':null
			}, null, pciContextMenu);
			var lng = $("#hiddenLng").val();
			var lat = $("#hiddenLat").val();
			map = gisCellDisplayLib.initMap("map_canvas", lng, lat);


			$(document).ajaxStart(function() {
				$(".loading_cover").css("display", "block");
			});

			$(document).ajaxComplete(function() {
				$(".loading_cover").css("display", "none");
			});
			
			//打开地图后不加载该区域的小区数据，点击显示小区才加载
			//加载小区
			//currentloadtoken = getLoadToken();
			//getGisCell(currentloadtoken);
			
			//点击显示LTE小区信息
			$("#displaycell").click(
					function() {
						
						var newAreaId = $("#realPostAreaId").val();
						if (curAreaId == newAreaId) {
							animateInAndOut("operInfo", 500, 500, 1000,
									"operTip", "该区域小区已经加载");
							return;// 和上一次的一样，不需要再加载
						}
						curAreaId = newAreaId;// 记录当前加载的小区所属的区域id
						
						gisCellDisplayLib.clearData();
						resetQueryCondition(); //重置查询条件
						currentloadtoken = getLoadToken();
						loadLteCellData(currentloadtoken);
					});
}

/****************** 地图视图的点击事件 start******************/

//在地图上展示lte小区以及相同pci的其他小区
function showLteCellsWithSamePciClick(polygon) {

			var lcid = polygon._data.getCell();
			// 主覆盖小区颜色
			var option_serverCell={
				//绿色
				'fillColor':'#00FF00'
			};
			//复用PCI的其他小区颜色
			var option_pciReuseCell={
				//黄色
				'fillColor':'#FFFF00'
			};
			//主小区添加样式
			gisCellDisplayLib.changeCellPolygonOptions(lcid,option_serverCell,true);
			
			$(".loading_cover").css("display", "block");
			
			$.ajax({
				url : 'getCellsWithSamePciDetailsForAjaxAction',
				dataType : 'json',
				data: {'reuseLcid' : lcid},
				type : 'post',
				success : function(data) {
					//console.log(data);
					var mainCell = new Object();
					var ortherCells = new Array();
					
					for(var i=0; i<data.length; i++) {
						//console.log(data[i]);
						if(data[i].LTE_CELL_ID == lcid) {
							mainCell = data[i];
						} else {
							ortherCells.push(data[i]);
						}
				   }
				  //console.log(mainCell);
				  //console.log(ortherCells);
				   if(ortherCells.length==0){
					   animateInAndOut("operInfo", 500, 500, 1000,
										"operTip", "对不起,该小区没有复用其PCI的其他小区!");
				   }
				   for(var i=0;i<ortherCells.length;i++){
				   		//为复用pci的小区添加样式
				   		gisCellDisplayLib.changeCellPolygonOptions(ortherCells[i]['LTE_CELL_ID'],option_pciReuseCell,true);
				   		//主小区与其他复用其pci小区连线
				   		if(ortherCells[i].reuse < ortherCells[i].CELL_RADIUS ||
				   			ortherCells[i].reuse < mainCell.CELL_RADIUS) {
				   			gisCellDisplayLib.drawLineBetweenCells(mainCell['LTE_CELL_ID']+"",ortherCells[i]['LTE_CELL_ID']+"",{'color':'red',"weight":1});
				   		} else {
				   			gisCellDisplayLib.drawLineBetweenCells(mainCell['LTE_CELL_ID']+"",ortherCells[i]['LTE_CELL_ID']+"",{'color':'blue',"weight":1});
				   		}
				   }
				   
				   //在table上显示相同PCI的小区信息
				   showLteCellsWithSamePciInTable(mainCell,ortherCells);
				},
				error : function(err, status) {
					console.error(status);
				},
				complete : function() {
					$(".loading_cover").css("display", "none");
				}
			});
}

//显示lte小区详情
function showLteCellDetailClick(polygon, event) {
	console.log("polygon 是百度地图吗=========>>>>>>>>>>>>"+(polygon instanceof BMap.Polygon));
	console.log("polygon 是leaflet地图吗=========>>>>>>>>>>>>"+(polygon instanceof BMap.Polygon));
	console.log("点击对象小区的名称是=========>>>>>>>>>>>>"+(polygon));
	if(polygon instanceof BMap.Polygon){
		var cmk = polygon._data;
		if (cmk.getCount() > 1) {
			scatterAllBMap(polygon);
		} else {
			var label = polygon._data.getCell();
			console.log("百度显示"+label+"详情");
			showCellDetail(label);
		}
	}else if(polygon instanceof L.Polygon){
		var cmk = polygon._data;
		if (cmk.getCount() > 1) {
			scatterAllOSM(polygon);
		} else {
			var label = polygon._data.getCell();
			// alert("显示"+label+"详情");
			showCellDetail(label);
		}
	}else{
		showCellDetail(polygon.getId());
	}
}

/****************** 地图视图的点击事件 end******************/

/**
 * 展开重叠的小区,BMap
 * 
 * @param {}  polygon
 */
function scatterAllBMap(polygon) {

	var html = formOverlapHtmlContent(polygon._data);
	html = "<h4 style='margin:0 0 5px 0;padding:0.2em 0'>重叠小区（"
			+ polygon._data.getCount() + "个）</h4>" + html;

	gisCellDisplayLib.showInfoWindow(html, new BMap.Point(new Number(
			polygon._data.getLng()), new Number(polygon._data.getLat())));

}
/**
 * 展开重叠的小区,OSM
 * 
 * @param {}  polygon
 */
function scatterAllOSM(polygon) {
	var html = formOverlapHtmlContent(polygon._data);
	html = "<h4 style='margin:0 0 5px 0;padding:0.2em 0'>重叠小区（"
			+ polygon._data.getCount() + "个）</h4>" + html;

	gisCellDisplayLib.showInfoWindow(html, new L.Point(new Number(
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


/**
 * 显示所关联的lte小区的部分信息，显示在侧边栏
 * @param {}   polygon
 */
function showCellDetail(lcid) {
	// 看本地缓存有没有
	var cell = null;
	var find = false;
	for ( var i = 0; i < cellDetails.length; i++) {
		cell = cellDetails[i];
		if (cell['lcid'] === lcid) {
			// console.log("从缓存找到详情。")
			find = true;
			break;
		}
	}
	if (find) {
		displayCellDetail(cell);
		return;
	}
    //从后台获取
	$(".loading_cover").css("display", "block");
	$.ajax({
		url : 'getLteCellDetailForAjaxAction',
		data : {
			'lcid' : lcid
		},
		dataType : 'json',
		type : 'post',
		success : function(data) {
			var c = data;
			if (c) {
				cellDetails.push(c); //存入缓存
				displayCellDetail(c); //展示出来
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
function initAreaCascadeLteCell() {
	//$("#areaId").append("<option value='-1'>全部</option>");
	// 区域联动事件
	$("#provinceId").change(function() {
		getSubAreas("provinceId", "cityId", "市",  function(data) {
						$("#hiddenAreaLngLatDiv").html("");
						$("#hiddenLng").val("");
						$("#hiddenLat").val("");
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
							//$("#areaId").append("<option value='-1'>全部</option>");
						}
						//$("#cityId").trigger("change");
				});
	});
	
	$("#cityId").change(function() {
				//改变区域更改当前id
				$('#realPostAreaId').val($("#cityId").val());
				//清空当前加载小区id
				curAreaId = null;
				
				var lnglat = $("#areaid_" + $("#cityId").val()).val();
				//console.log("改变区域:" + lnglat);
				// 清除当前区域的数据
				clearAllData();
		
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
				// 重置查询表单的数据
				resetQueryCondition();
	});
	
	/*
	$("#provinceId").change(function() {
		getSubAreas("provinceId", "cityId", "市");
	});
	
	$("#cityId").change(
			function() {
			
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
							//console.log(html);
							$("#hiddenAreaLngLatDiv").append(html);
							$("#areaId").append("<option value='-1'>全部</option>");
						}
	
						$("#areaId").trigger("change");
						
				});
	});

	$("#areaId").change(function() {
				
				var lnglat = $("#areaid_" + $("#areaId").val()).val();
				//console.log("改变区域:" + lnglat);
				// 清除当前区域的数据
				clearAllData();
		
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
				// 重置查询表单的数据
				resetQueryCondition();
				
				//区域切换后不加载该区域的小区数据，点击显示小区才加载
				// 重新获取新区域的数据
				//currentloadtoken = getLoadToken();
				//getGisCell(currentloadtoken);
	});*/
}


/**
 * 清空所有的数据
 */
function clearAllData() {

	try {
		gisCellDisplayLib.clearOverlays();
	} catch (e) {

	}
	cacheCells.splice(0, cacheCells.length);//删除缓存的小区
	cellDetails.splice(0, cellDetails.length);//删除缓存的详情
	gisCellDisplayLib.clearData();// 清空地图上的东西
}
/**
 * 重置查询表单的数据
 */
function resetQueryCondition() {
	$("#hiddenCurrentPage").val(1);
	$("#hiddenTotalPageCnt").val(0);
	$("#hiddenTotalCnt").val(0);
	$("#hiddenForcedStartIndex").val(-1);
	$("#hiddenPageSize").val(100);
}

/**
 * 根据所选择的区域加载LTE小区数据
 */
function loadLteCellData(loadToken) {
	onLoadingLteCell = true;
	$("#conditionForm")
			.ajaxSubmit(
					{
						url : 'getLteCellByPageForAjaxAction',
						dataType : 'json',
						success : function(data) {
							if (loadToken != currentloadtoken) {
								// 新的加载开始了，这些旧的数据要丢弃
								// console.log("丢弃此次的加载结果。");
								return;
							}
							var obj = data;
							//缓存cell数据
							//console.log(obj['lteCells'][0]);
							var allCells = obj['lteCells'];
							for(var i=0; i<obj['lteCells'].length; i++) {
								var one = allCells[i];
								cacheCells.push(one);
							}
							try {
								// obj = eval("(" + data + ")");
								// alert(obj['celllist']);
								// showGisCell(obj['gisCells']);
								showLteCellOnMap(obj['lteCells'],'lcid');
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
										loadLteCellData(loadToken);
									}
								}
								// 如果没有获取完成，则继续获取
							} catch (err) {
								// console.log("返回的数据有问题:" + err);
							}
						},
						error : function(xmh, textstatus, e) {
							//alert("出错啦！" + textstatus);
						},
						complete : function() {
							$(".loading_cover").css("display", "none");
						}
			});
}

/**
 * 地图显示Lte小区信息
 * key:'lcid' 表示以lcid为图形主键，加载的是lte小区
 */
function showLteCellOnMap(lteCells,key) {
	if (!lteCells) {
		return;
	}
	// 调用showGisCell方法添加到地图
	gisCellDisplayLib.showGisCell(lteCells,key);
}

/***************** 侧边Lte信息框 start********************/

// 隐藏详情面板
function hideDetailDiv() {

	$("a.siwtch").hide();
	$(".switch_hidden").show();
	$(".resource_list_icon").animate({
		right : '0px'
	}, 'fast');
	$(".resource_list_box").hide("fast");
}

/**
 * 显示lte小区详情面板
 * 
 * @param {}
 *   lteCell
 */
function displayCellDetail(lteCell) {
    //console.log(lteCell);
	$(".switch_hidden").trigger("click");

	$("#lteCellId").val(getValidValue(lteCell.lcid));
	$("#lteCellIdForEdit").val(getValidValue(lteCell.lcid));
	$("#showLteCellNameId").html(getValidValue(lteCell.chineseName));
	$("#showLteCellCoverTypeId").html(getValidValue(lteCell.coverType));
	$("#showLteCellCoverRangeId").html(getValidValue(lteCell.coverRange));
	$("#showLteCellBandId").html(getValidValue(lteCell.band));
	$("#showLteCellEarfcnId").html(getValidValue(lteCell.earfcn));
	$("#showLteCellBandTypeId").html(getValidValue(lteCell.bandType));

	$("#showLteCellGroundHeightId").html(getValidValue(lteCell.groundHeight));
	$("#showLteCellRrunumId").html(getValidValue(lteCell.rrunum));
	$("#showLteCellRruverId").html(getValidValue(lteCell.rruver));

	$("#showLteCellAntennaTypeId").html(getValidValue(lteCell.antennaType));
	$("#showLteCellIntegratedId").html(getValidValue(lteCell.integrated));
	$("#showLteCellRspowerId").html(getValidValue(lteCell.rspower));

}
/***************** 侧边Lte信息框 end********************/

/***************** Lte信息编辑框 start********************/
/**
 * 通过id获取LTE小区所有详情
 */
function getLteCellDetailsById(lcid) {
	//console.log("lcid"+lcid);
	$.ajax({
			url :'getLteCellAndCositeCellsDetailForAjaxAction',
			data : {
				'lcid' : lcid
			},
			dataType :'text',
			success : function(data) {
				showLteCellDetails(data);
			},
			complete : function() {
			
			}
		});
}
/**
 * 显示LTE小区详情,获取到相同基站的所有小区的详情
 */
function showLteCellDetails(data) {
	//console.log(data);
	var arr = eval("(" + data + ")");
	//保存基站的所有小区
	currentCellArray = arr;
	//只取目标小区，即第一个小区
	var oneLteCellDetail = currentCellArray[0];
	if (!oneLteCellDetail) {
		return;
	}
	showOneLteCellDetail(oneLteCellDetail);
}


/**
 * 显示LTE小区的详情，加载到页面
 */
function showOneLteCellDetail(oneLteCellDetail){
	//onekey['name'] 为id
	//getValidValue(oneLteCellDetail[onekey['code']] 为值
	// 循环设置
	var html = '';
	var size = chineseToCode.length;
	var onekey;
	for ( var i = 0; i < size; i++) {
		if (i % 3 == 0) {
			// 换行
			if (i > 0) {
				html += "</tr>";
			} 
			html += "<tr>";
		}
		onekey = chineseToCode[i];
		//加载select标签
		if(onekey['code']==='IS_VIP' || onekey['code']==='STATE' || onekey['code']==='INTEGRATED') {
			html += "<td class='menuTd'>" + onekey['name']+" : " +
					"<select id='"+onekey['code']+"' value='"+oneLteCellDetail[onekey['code']]+"'" +
							" name='lteCellUpdateResult."+onekey['code']+"'> ";
					if(oneLteCellDetail[onekey['code']]==="是") {
						html += "<option value='是' selected='selected'>是</option> " +
								"<option value='否'>否</option> ";
					} else {
						html += "<option value='是'>是</option> " +
								"<option value='否' selected='selected'>否</option> ";
					}
		    html += "</select> "+
					"</td> ";
		} //加载日期标签
		else if(onekey['code']==='OPERATION_TIME') {
			html += "<td class='menuTd'>" + onekey['name']+" : " +
					"<input class='Wdate input-text' type='text' style='width: 132px;' " +
							"readonly='' onfocus='WdatePicker({dateFmt:\"yyyy-MM-dd\"})' " +
							"value='"+oneLteCellDetail[onekey['code']]+"' " +
							"name='lteCellUpdateResult."+onekey['code']+"' id='"+onekey['code']+"' > " +
					"</td>";
		} //加载小区经度，纬度标签
		else if(onekey['code']==='LONGITUDE' || onekey['code']==='LATITUDE') {
			html += "<td class='menuTd'>" + onekey['name']+" : " +
						"<input id='"+onekey['code']+"' "+
							 "name='lteCellUpdateResult."+onekey['code']+"' "+
							 "value='"+oneLteCellDetail[onekey['code']]+"' " +
							 "onkeyup='checkOnkeyupDateType(this)'/>" +
							 "</br><span id='span"+onekey['code']+"'  style='font-family:华文中宋; color:red;width:100px;display:block;'></span>" +
					"</td>";
		} //加载其他input标签
		else {
			html += "<td class='menuTd'>" + onekey['name']+" : " +
						"<input id='"+onekey['code']+"' "+
							 "name='lteCellUpdateResult."+onekey['code']+"' "+
							 "value='"+getValidValue(oneLteCellDetail[onekey['code']])+"' " +
							 "onkeyup='checkOnkeyupDateType(this)'/>" +
							 "</br><span id='span"+onekey['code']+"'  style='font-family:华文中宋; color:red;width:100px;display:block;'></span>" +
					"</td>";
		}
	}
	if (size % 3 > 0) {
		for ( var i = 0; i < 3 - size % 3; i++) {
			html += "<td class='menuTd'></td>";
		}
	}
	html+="</tr>";

	$("span#VIEW_ENODEB_NAME").html(getValidValue(oneLteCellDetail['ENODEB_NAME']));
	$("span#VIEW_CELL_NAME").html(getValidValue(oneLteCellDetail['CELL_NAME']));
	$("#viewCellDetailTable").html(html);
}
/*
 *输入框输入完成后触发验证
 */
function checkOnkeyupDateType(e) {
	var id = e.id;  //获取元素的id
	var value = e.value;  //获取元素的值	
	checkDateType(id,value);
}
/*
 * 对单个输入框输入完成后的数据验证
 * @param 元素id值
 * @param 元素value值
 */
function checkDateType(id,value) {

	//验证需要的正则表达式
	var vali1 = /[^A-Za-z0-9]/g;   //输入数字和英文字母
	var vali2 = /[^\d.]/g; 		  //输入数字和小数点
	var vali3 = /[^\d:：]/g;		   //输入数字和：
	var vali4 = /[^\d-]/g;    /*/^[-+]?[0-9]*\.?[0-9]+$/;*/	//输入正负浮点数
	var vali5 = /[^\d-.]/g;    //输入数字,负号，小数点
	
	//console.log("选择的值是:"+value);
	
	//----------需要验证的参数 start-------------//
	//基站类型的验证
	if(id==='SITE_STYLE') {
		if(value==null || value=="") {
			$("span#span"+id).html("值不能为空");
			submitOK = false;
		}else {
			$("span#span"+id).html("");
			submitOK =true;
		}
	}
	//频段类型的验证
	if(id==='BAND_TYPE') {
		if(value==null || value=="") {
			$("span#span"+id).html("值不能为空");
			submitOK = false;
		}else {
			$("span#span"+id).html("");
			submitOK =true;
		}
	}
	//经度的验证
	if(id==='LONGITUDE') {
		if(135.04166666667<value || value<73.666666666667){
			$("span#span"+id).html("中国经度范围东经73.666666666667-<br>135.04166666667");
			submitOK = false;
		}else if(vali2.test(value)) {
			$("span#span"+id).html("请输入数字或者小数点");
			submitOK = false;
		} else if(value==null || value=="") {
			$("span#span"+id).html("值不能为空");
			submitOK = false;
		} else {
			$("span#span"+id).html("");
			submitOK =true;
		}
	}
	//纬度的验证
	if(id==='LATITUDE') {
		if(53.55<value || value<3.8666666666667){
			$("span#span"+id).html("中国纬度范围北纬3.8666666666667-<br>53.55");
			submitOK = false;
		}else if(vali2.test(value)) {
			$("span#span"+id).html("请输入数字或者小数点");
			submitOK = false;
		} else if(value==null || value=="") {
			$("span#span"+id).html("值不能为空");
			submitOK = false;
		} else {
			$("span#span"+id).html("");
			submitOK =true;
		}
	}
	//开站配比，子帧分配，特殊子帧的验证
	if(id==='STATION_CFG' || id==='FRAME_CFG' || id==='SPECIAL_FRAME_CFG') {
		if(vali3.test(value)) {
			$("span#span"+id).html("请输入数字或者：符");
			submitOK = false;
		} else if(value==null || value=="") {
			$("span#span"+id).html("值不能为空");
			submitOK = false;
		} else {
			$("span#span"+id).html("");
			submitOK =true;
		}
	}
	//PA，PB的验证
	if(id==='PA' || id==='PB') {
		if(vali4.test(value)) {
			$("span#span"+id).html("请输入正确的格式");
			submitOK = false;
		} else if(value==null || value=="") {
			$("span#span"+id).html("值不能为空");
			submitOK = false;
		} else {
			$("span#span"+id).html("");
			submitOK =true;
		}
	}
	//覆盖类型的验证
	if(id==='COVER_TYPE'){
		if(value=="indoor" || value=="outdoor"){
			$("span#span"+id).html("");
			submitOK = true;
		}else if(vali2.test(value)) {
			$("span#span"+id).html("类型是“indoor”或者“outdoor”");
			submitOK = false;
		}else if(value==null || value=="") {
			$("span#span"+id).html("值不能为空");
			submitOK = false;
		} 
	}
	//带宽的验证
	if(id==='BAND') {
		if(value==null || value=="") {
			$("span#span"+id).html("值不能为空");
			submitOK = false;
		}else {
			$("span#span"+id).html("");
			submitOK =true;
		}
	}
	//方向角的验证
	if(id==='AZIMUTH') {
		if(vali5.test(value)) {
			$("span#span"+id).html("请输入正确的格式");
			submitOK = false;
		} else if(value==null || value=="") {
			$("span#span"+id).html("值不能为空");
			submitOK = false;
		} else if(value < 0 || value > 360) {
			$("span#span"+id).html("必须处于[0,360]角度范围内");
			submitOK = false;
		} else {
			$("span#span"+id).html("");
			submitOK =true;
		}
	}
	//数字输入的验证
	if(id==='PCI' || id==='GROUND_HEIGHT'
		|| id==='M_DOWNTILT' || id==='RSPOWER' || id==='E_DOWNTILT'
		|| id==='TAC'|| id==='RRUNUM'|| id==='TAL'|| id==='CELL_RADIUS'
		|| id==='EARFCN' || id==='PDCCH' || id==='DOWNTILT') {
		if(vali5.test(value)) {
			$("span#span"+id).html("请输入正确的格式");
			submitOK = false;
		} else if(value==null || value=="") {
			$("span#span"+id).html("值不能为空");
			submitOK = false;
		} else {
			$("span#span"+id).html("");
			submitOK =true;
		}
	}
	
	//----------需要验证的参数 end--------------//
/*	
	//全局验证，只有验证页面的span全为空，submitOK才为true
	var length = chineseToCode.length;
	var oneKey;
	for ( var i = 0; i < length; i++) {
	
		oneKey = chineseToCode[i];
		var code = oneKey['code'];
		
		//IS_VIP,STATE,OPERATION_TIME,INTEGRATED不检查，因为没有生成span
		if(!(code==="IS_VIP") && !(code==="STATE") && !(code==="OPERATION_TIME") && !(code==="INTEGRATED")) {
			var spanText = $("span#span"+code).text();
			//console.log(code+" span:"+spanText);
	 		if(spanText===null || ""===spanText) {
				submitOK = true;
				isNext = true;
			} else {
				submitOK = false;
				isNext = false;
				//console.log("不为空的span："+code);
				break;
			}
		}
	}
	//console.log("全部span为空"+submitOK);
*/
}
/***************** Lte信息编辑框 end********************/


/*************** PCI复用分析框 start*************/

/**
 * 打开PCI复用分析
 */
function showPciReuseAnalysis(){
    if(cacheCells.length > 0) {
	    $("#pciPageSize").val(5);
		$("#pciCurrentPage").val(1);
		$("#pciTotalPageCnt").val(-1);
		$("#pciTotalCnt").val(-1);
		//console.log($("#realPostAreaId").val());
		$("#areaIdForPciAnalysis").val($("#realPostAreaId").val());//获取当前区域id
		showPciReuseAnalysisDetails();
    }else{
    	alert("地图没有加载到任何小区数据！");
    }
	
}
/**
 * 显示PCI复用分析数据
 */
function showPciReuseAnalysisDetails(){
	//隐藏pci冲突与混淆检测窗口
	$("#pciCollAndConf_Dialog").hide();
	
	$("#pciReuseCoverDiv").show();
	$("#pciReuse_Dialog").show();
	
	$("#pciReuseForm").ajaxSubmit({
		url : 'getPciReuseAnalysisByPageForAjaxAction',
		dataType : 'text',
		success : function(raw) {
			//console.log(raw);
			data=eval("("+raw+")");
			//console.log("data:"+data);
			var pciList = data['pciList'];
			var reuseCellList = data['reuseCellList'];
			var cellPairsList = data['cellPairsList'];

			
			var reuseCell = null;
			var cellPairs = null;
			var oneReuseCell = null;
			var oneCellPairs = null;
			
			var table = $("#viewPciReuseAnalysis");
			// 只保留表头
			$("#viewPciReuseAnalysis tr:not(:first)").each(function(i, ele) {
				$(ele).remove();
			});
			var tr = "";
			for(var i=0; i<pciList.length; i++) {
				tr += "<tr class='greystyle-standard-whitetr'><td>" + pciList[i] + "</td><td>";
				
				oneReuseCell = reuseCellList[i];
				for(var j=0; j<oneReuseCell.length; j++) {
					if(j%4==0){
						tr += "</br>";
					}	
					oneCell = oneReuseCell[j];
					tr += "<a onclick='oneCellPciReuseDisplay("+oneCell.lcid+")' title='"+oneCell.chineseName+"'>" + oneCell.chineseName.substring(0,4)+"..." + "</a>|";
				}

				tr +="</td><td>";
				
				cellPairs = cellPairsList[i];
				for(var j=0; j<cellPairs.length; j++) {
					if(j%3==0){
						tr += "</br>";
					}
					oneCellPairs = cellPairs[j];
					tr += "<"+oneCellPairs.cellOne.chineseName.substring(0,5)+"..." + ">-<" + oneCellPairs.cellTwo.chineseName.substring(0,5)+"..." + ">||";
				}
				tr += "</td></tr>";
			}
			//tr += "<tr><td>"+cacheCells.length+"</td></tr>";
			//console.log(tr);
			table.append($(tr));

			// 设置隐藏的page信息
			setFormPageInfo("pciReuseForm", data['page']);
			// 设置分页面板
			setPageView(data['page'], "pciPageDiv");
		},
		error : function(xmh, textstatus, e) {
			//alert("出错啦！" + textstatus);
		},
		complete : function() {
			$("#pciReuseCoverDiv").css("display", "none");
		}
	});
	return false;
}


//显示一个主小区的复用分析情况
//lcid：小区id
//pci：小区pci
function oneCellPciReuseDisplay(lcid) {
	//console.log(lcid);
	//console.log(pci);
	//var reuseAreaId = $("#areaIdForPciAnalysis").val();//获取当前区域id
	$.ajax({
		url : 'getCellsWithSamePciDetailsForAjaxAction',
		data : {
			'reuseLcid' :lcid
		},
		dataType : 'json',
		type : 'post',
		success : function(data) {
			
			var mainCell = new Object();
			var ortherCells = new Array();
			//取参考小区
			for(var i=0; i<data.length; i++) {
				if(data[i].LTE_CELL_ID ==lcid) {
					mainCell = data[i];
				} else {
					ortherCells.push(data[i]);
				}
			}

			//地图移动到指定小区
			if (mainCell.LONGITUDE == 0 || mainCell.LATITUDE == 0) {
				// console.warn("未设置该区域的中心点经纬度。");
			} else {
				// 地图移动
				gisCellDisplayLib.panTo(mainCell.LONGITUDE, mainCell.LATITUDE);
			}
					
			//在table上显示相同PCI的小区信息
			showLteCellsWithSamePciInTable(mainCell,ortherCells);
			
		},
		error : function(xmh, textstatus, e) {
			//alert("出错啦！" + textstatus);
		},
		complete : function() {
			$("#pciReuseCoverDiv").css("display", "none");
		}
	});
}

/**
 * 在table上显示相同PCI的小区信息
 */
function showLteCellsWithSamePciInTable(mainCell,ortherCells) {
	var table = $("#viewPciReuseCell");
	// 只保留表头
	$("#viewPciReuseCell tr:not(:first)").each(function(i, ele) {
		$(ele).remove();
	});
	//追加标题
	document.getElementById("pciReuseCellTitle").innerHTML = "与小区<"+mainCell['CELL_NAME']+">同PCI("+mainCell['PCI']+")的小区，</br><"+
							mainCell['CELL_NAME']+">的覆盖距离："+mainCell['CELL_RADIUS']+"M";	
	//追加tr
	var tr = "<tr><th colspan='1' style='width: 10%'>小区</th><th colspan='1' style='width: 10%'>小区覆盖距离</th>"+
				"<th colspan='1' style='width: 10%'>复用距离</th></tr>";
	for(var i=0; i < ortherCells.length; i++) {
		tr += "<tr class='greystyle-standard-whitetr'><td> <a onclick='oneCellPciReuseDisplay("+ortherCells[i]['LTE_CELL_ID']+")' title='"+ortherCells[i]['CELL_NAME']+"'>" + ortherCells[i]['CELL_NAME'].substring(0,4)+"..." + "</a></td><td>" 
				+ ortherCells[i]['CELL_RADIUS'] +"M</td><td>" + getValidValue(ortherCells[i]['reuse']) + "M</td></tr>";
	}
	table.append($(tr));
	
	/****在地图上加载样式与连线****/
	
	//先做clear
	gisCellDisplayLib.clearOnlyExtraOverlay();//清除额外覆盖物
	gisCellDisplayLib.resetPolygonToDefaultOutlook();//恢复默认polygon颜色
	
	//主覆盖小区颜色
	var option_serverCell={
		//绿色
		'fillColor':'#00FF00'
	};
	//复用PCI的其他小区颜色
	var option_pciReuseCell={
		//黄色
		'fillColor':'#FFFF00'
	};
	//主小区添加样式
	gisCellDisplayLib.changeCellPolygonOptions(mainCell['LTE_CELL_ID'],option_serverCell,true);
	
    for(var i=0;i<ortherCells.length;i++){
  		//为复用pci的小区添加样式
  		gisCellDisplayLib.changeCellPolygonOptions(ortherCells[i]['LTE_CELL_ID'],option_pciReuseCell,true);
  		//主小区与其他复用其pci小区连线
  		if(ortherCells[i].reuse < ortherCells[i].CELL_RADIUS ||
  			ortherCells[i].reuse < mainCell.CELL_RADIUS) {
  			gisCellDisplayLib.drawLineBetweenCells(mainCell['LTE_CELL_ID']+"",ortherCells[i]['LTE_CELL_ID']+"",{'color':'red',"weight":1});
  		} else {
  			gisCellDisplayLib.drawLineBetweenCells(mainCell['LTE_CELL_ID']+"",ortherCells[i]['LTE_CELL_ID']+"",{'color':'blue',"weight":1});
  		}
   }
}

// 跳转
function showListViewByPage(dir) {
	var pageSize = new Number($("#pciPageSize").val());
	var currentPage = new Number($("#pciCurrentPage").val());
	var totalPageCnt = new Number($("#pciTotalPageCnt").val());
	var totalCnt = new Number($("#pciTotalCnt").val());

	if (dir === "first") {
		if (currentPage <= 1) {
			return;
		} else {
			$("#pciCurrentPage").val("1");
		}
	} else if (dir === "last") {
		if (currentPage >= totalPageCnt) {
			return;
		} else {
			$("#pciCurrentPage").val(totalPageCnt);
		}
	} else if (dir === "back") {
		if (currentPage <= 1) {
			return;
		} else {
			$("#pciCurrentPage").val(currentPage - 1);
		}
	} else if (dir === "next") {
		if (currentPage >= totalPageCnt) {
			return;
		} else {
			$("#pciCurrentPage").val(currentPage + 1);
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
		$("#pciCurrentPage").val(userinput);
	} else {
		return;
	}
	//显示PCI复用分析数据
	showPciReuseAnalysisDetails();
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
	form.find("#pciPageSize").val(page.pageSize);
	form.find("#pciCurrentPage").val(new Number(page.currentPage));// /
	form.find("#pciTotalPageCnt").val(page.totalPageCnt);
	form.find("#pciTotalCnt").val(page.totalCnt);

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
/*************** PCI复用分析框 end*************/


/*************** PCI冲突与混淆框 start*************/

/**
 * 打开PCI冲突与混淆分析
 */
function showPciCollisionAndConfusion(){
    if(cacheCells.length > 0) {
	    $("#collAndConfPageSize").val(5);
		$("#collAndConfCurrentPage").val(1);
		$("#collAndConfTotalPageCnt").val(-1);
		$("#collAndConfTotalCnt").val(-1);
		//console.log($("#realPostAreaId").val());
		$("#areaIdForCollAndConfAnalysis").val($("#realPostAreaId").val());//获取当前区域id
		showCollAndConfAnalysisDetails();
    }else{
    	alert("地图没有加载到任何小区数据！");
    }
	
}

/**
 * 显示PCI冲突与混淆分析
 */
function showCollAndConfAnalysisDetails() {
	//隐藏pci复用分析窗口
	$("#pciReuse_Dialog").hide();
	
	$("#pciCollAndConfCoverDiv").show();
	$("#pciCollAndConf_Dialog").show();
	
	$("#pciCollAndConfForm").ajaxSubmit({
		url : 'getCollAndConfCellWithSamePciByPageForAjaxAction',
		dataType : 'text',
		success : function(raw) {
			//console.log(raw);
			data=eval("("+raw+")");
			//console.log("data:"+data);
			var pciList = data['pciList'];
			var collisionList = data['collisionList'];
			var confusionList = data['confusionList'];
			collPairsList = data['collPairsList'];
			confPairsList = data['confPairsList'];

			var oneCollisionList = null;
			var oneConfusionList = null;
			
			var oneCollision = null;
			var oneConfusion = null;
			
			var table = $("#viewCollAndConfAnalysis");
			// 只保留表头
			$("#viewCollAndConfAnalysis tr:not(:first)").each(function(i, ele) {
				$(ele).remove();
			});
			var tr = "";
			for(var i=0; i<pciList.length; i++) {

				tr += "<tr class='greystyle-standard-whitetr'><td>" +
						"<a onclick='showPciCollAndConfInMap("+pciList[i]+")'>" + pciList[i] + "</a></td><td>";
				oneCollisionList = collisionList[i];
				for(var j=0; j<oneCollisionList.length; j++) {
					if(j%6==0){
						tr += "</br>";
					}	
					oneCollision = oneCollisionList[j];
					tr += oneCollision['chineseName'] + "; ";
				}

				tr +="</td><td>";
				
				oneConfusionList = confusionList[i];
				for(var j=0; j<oneConfusionList.length; j++) {
					if(j%2==0){
						tr += "</br>";
					}
					oneConfusion = oneConfusionList[j];
					tr += "<" + oneConfusion.cellOne['chineseName'] + ">-<" + 
							oneConfusion.cellTwo['chineseName'] + ">, 与<" + 
							oneConfusion.mainCell['chineseName'] + ">为相邻小区; ";
				}
				tr += "</td></tr>";
			}
			//tr += "<tr><td>"+cacheCells.length+"</td></tr>";
			//console.log(tr);
			table.append($(tr));

			// 设置隐藏的page信息
			setCollAndConfFormPageInfo("pciCollAndConfForm", data['page']);
			// 设置分页面板
			setPciCollAndConfPageView(data['page'], "collAndConfPageDiv");
			
		},
		error : function(xmh, textstatus, e) {
			//alert("出错啦！" + textstatus);
		},
		complete : function() {
			$("#pciReuseCoverDiv").css("display", "none");
		}
	});
	return false;
}

/**
 * 在地图显示pci冲突与混淆的小区
 * @param pci
 */
function showPciCollAndConfInMap(pci) {

	//先做clear
	gisCellDisplayLib.clearOnlyExtraOverlay();//清除额外覆盖物
	gisCellDisplayLib.resetPolygonToDefaultOutlook();//恢复默认polygon颜色
	
	// 地图定位,大概定位到第一个cell
	if(collPairsList[0].length > 0) {
		var cell = collPairsList[0][0]['cellOne'];
		if (cell['lng'] == 0 || cell['lat'] == 0) {
			// console.warn("未设置该区域的中心点经纬度。");
		} else {
			// 地图移动
			gisCellDisplayLib.panTo(cell['lng'], cell['lat']);
		}
	}
	
	//console.log(collPairsList);
	//找出当前加载的pci的小区，将其连线和改变样式
	for(var i=0; i < collPairsList.length; i++) {
	
		//从缓存中拿出来
		var oneCollPairsList = collPairsList[i]; 
		var oneConfPairsList = confPairsList[i];
		
		
		//pci冲突
		for(var j=0; j < oneCollPairsList.length; j++) {
			//console.log(oneCollPairsList[j]);
			if(pci == oneCollPairsList[j]['cellOne']['pci']) {
				
				//冲突小区颜色
				var option_collision={
					//黄色
					'fillColor':'#FFFF00'
				};
				//console.log("冲突："+oneCollPairsList[j]['cellOne']['chineseName']+"  "+oneCollPairsList[j]['cellTwo']['chineseName']);
				//冲突小区添加样式
				gisCellDisplayLib.changeCellPolygonOptions(oneCollPairsList[j]['cellOne']['lcid']+"",option_collision,true);
				gisCellDisplayLib.changeCellPolygonOptions(oneCollPairsList[j]['cellTwo']['lcid']+"",option_collision,true);
	  			//冲突小区之间连线
	  			gisCellDisplayLib.drawLineBetweenCells(
	  								oneCollPairsList[j]['cellOne']['lcid']+"",
						  			oneCollPairsList[j]['cellTwo']['lcid']+"",
						  			{'color':'red',"weight":1});
 
			}
		}
		//pci混淆
		for(var j=0; j < oneConfPairsList.length; j++) {
			if(pci == oneConfPairsList[j]['cellOne']['pci']) {
			
				//混淆小区颜色
				var option_confusion={
					//橙色
					'fillColor':'#FF5C00'
				};
				//console.log("混淆："+oneConfPairsList[j]['cellOne']['chineseName']+"  "+oneConfPairsList[j]['cellTwo']['chineseName']);
				//混淆小区添加样式
				gisCellDisplayLib.changeCellPolygonOptions(oneConfPairsList[j]['cellOne']['lcid']+"",option_confusion,true);
				gisCellDisplayLib.changeCellPolygonOptions(oneConfPairsList[j]['cellTwo']['lcid']+"",option_confusion,true);
	  			//混淆小区之间连线
	  			gisCellDisplayLib.drawLineBetweenCells(
	  								oneConfPairsList[j]['cellOne']['lcid']+"",
						  			oneConfPairsList[j]['cellTwo']['lcid']+"",
						  			{'color':'blue',"weight":1});

			}
		}
		
		//同时是混淆，又是冲突,样式改为紫色
		var id = '';
		var option = {
			//紫色
			'fillColor':'#7109AA'
		};
		for(var j=0; j < oneCollPairsList.length; j++) {
			for(var k=0; k < oneConfPairsList.length; k++) {
				var idColl1 = oneCollPairsList[j]['cellOne']['lcid'];
				var idColl2 = oneCollPairsList[j]['cellTwo']['lcid'];
				var idConf1 = oneConfPairsList[k]['cellOne']['lcid'];
				var idConf2 = oneConfPairsList[k]['cellTwo']['lcid'];
				if(idColl1 == idConf1 || idColl1 == idConf2) {
					id = idColl1;
				}
				if(idColl2 == idConf1 || idColl2 == idConf2) {
					id = idColl2;
				}
			}option_confusion
			gisCellDisplayLib.changeCellPolygonOptions(id+"",option,true);
		}
	}
	
}

// 设置formid下的page信息
// 其中，当前页会加一
function setCollAndConfFormPageInfo(formId, page) {
	if (formId == null || formId == undefined || page == null
			|| page == undefined) {
		return;
	}

	var form = $("#" + formId);
	if (!form) {
		return;
	}
	// console.log("setCollAndConfFormPageInfo.
	// pageSize="+page.pageSize+",currentPage="+page.currentPage+",totalPageCnt="+page.totalPageCnt+",totalCnt="+page.totalCnt);
	form.find("#collAndConfPageSize").val(page.pageSize);
	form.find("#collAndConfCurrentPage").val(new Number(page.currentPage));// /
	form.find("#collAndConfTotalPageCnt").val(page.totalPageCnt);
	form.find("#collAndConfTotalCnt").val(page.totalCnt);
}

/**
 * 设置分页面板
 * @param page 分页信息
 * @param divId 分页面板id
 */
function setPciCollAndConfPageView(page, divId) {
	if (page == null || page == undefined) {
		return;
	}

	var div = $("#" + divId);
	if (!div) {
		return;
	}

	var pageSize = page['pageSize'] ? page['pageSize'] : 0;

	var currentPage = page['currentPage'] ? page['currentPage'] : 1;

	var totalPageCnt = page['totalPageCnt'] ? page['totalPageCnt'] : 0;

	var totalCnt = page['totalCnt'] ? page['totalCnt'] : 0;

	// 设置到面板上
	$(div).find("#pciCollAndConfShowTotalCnt").html(totalCnt);
	$(div).find("#pciCollAndConfShowCurrentPage").val(currentPage);
	$(div).find("#pciCollAndConfShowTotalPageCnt").html(totalPageCnt);
}
// 跳转
function showPciCollAndconfListViewByPage(dir) {
	var pageSize = new Number($("#collAndConfPageSize").val());
	var currentPage = new Number($("#collAndConfCurrentPage").val());
	var totalPageCnt = new Number($("#collAndConfTotalPageCnt").val());
	var totalCnt = new Number($("#collAndConfTotalCnt").val());

	if (dir === "first") {
		if (currentPage <= 1) {
			return;
		} else {
			$("#collAndConfCurrentPage").val("1");
		}
	} else if (dir === "last") {
		if (currentPage >= totalPageCnt) {
			return;
		} else {
			$("#collAndConfCurrentPage").val(totalPageCnt);
		}
	} else if (dir === "back") {
		if (currentPage <= 1) {
			return;
		} else {
			$("#collAndConfCurrentPage").val(currentPage - 1);
		}
	} else if (dir === "next") {
		if (currentPage >= totalPageCnt) {
			return;
		} else {
			$("#collAndConfCurrentPage").val(currentPage + 1);
		}
	} else if (dir === "num") {
		var userinput = $("#pciCollAndConfShowCurrentPage").val();
		if (isNaN(userinput)) {
			alert("请输入数字！")
			return;
		}
		if (userinput > totalPageCnt || userinput < 1) {
			alert("输入页面范围不在范围内！");
			return;
		}
		$("#collAndConfCurrentPage").val(userinput);
	} else {
		return;
	}
	
	//显示PCI冲突与混淆数据
	showCollAndConfAnalysisDetails();
}
/*************** PCI冲突与混淆框 end*************/


/*************** 菜单栏  start****************/

function $getId(id) {
	return document.getElementById(id);
}

/** 隐藏 **/
function displayHidden(whichID) {
	$getId(whichID).style.display = "none";
}
/** 显示 **/
function displayBlock(whichID) {
	$getId(whichID).style.display = "block";
}

/** 跳转功能 **/
function setIntro(id) {
	//高亮当前的链接
	$("#menu a.selected").removeClass("selected");
	$("#menu a[id='" + id + "']").addClass("selected");
}

function subTitle1On1() {
	$getId("subTitle1").style.background = "#eaeaea url(img/tab-a.png) no-repeat 0 3px";
	$getId("subTitle1a").style.background = "#eaeaea url(img/tab-b.png) no-repeat 100% 3px";
	$getId("subTitle2").style.background = "none";
	$getId("subTitle2a").style.background = "#eaeaea url(img/bg-menuli.gif) no-repeat 100% center";
	$getId("subTitle3").style.background = "none";
	$getId("subTitle3a").style.background = "#eaeaea url(img/bg-menuli.gif) no-repeat 100% center";
}
function subTitle1On2() {
	$getId("subTitle2").style.background = "#eaeaea url(img/tab-a.png) no-repeat 0 3px";
	$getId("subTitle2a").style.background = "#eaeaea url(img/tab-b.png) no-repeat 100% 3px";
	$getId("subTitle1").style.background = "none";
	$getId("subTitle1a").style.background = "#eaeaea url(img/bg-menuli.gif) no-repeat 100% center";
	$getId("subTitle3").style.background = "none";
	$getId("subTitle3a").style.background = "#eaeaea url(img/bg-menuli.gif) no-repeat 100% center";
}
function subTitle1On3() {
	$getId("subTitle3").style.background = "#eaeaea url(img/tab-a.png) no-repeat 0 3px";
	$getId("subTitle3a").style.background = "#eaeaea url(img/tab-b.png) no-repeat 100% 3px";
	$getId("subTitle2").style.background = "none";
	$getId("subTitle2a").style.background = "#eaeaea url(img/bg-menuli.gif) no-repeat 100% center";
	$getId("subTitle1").style.background = "none";
	$getId("subTitle1a").style.background = "#eaeaea url(img/bg-menuli.gif) no-repeat 100% center";
} 
/************** 菜单栏  end****************/
