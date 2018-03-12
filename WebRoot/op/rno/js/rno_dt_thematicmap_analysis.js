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
var dtSampleContainer;// 采样点容器
var map;

var currentSelConfigIds = new Array();// 当前选择的用于分析的分析列表
var currentloadtoken = "";// 加载任务的token

var minZoom = 15;// 只有大于 该 缩放级别，才真正
var randomShowCnt = 100;// 在不需要全部显示的时候，最大随机显示的个数

var onLoadingGisCell = false;// 是否正在加载小区信息

var isRuleChanged = false;// 渲染规则是否有变化

var loadingLayerId = null;// 加载层的id
var rendererFactory = new RendererFactory(false);// 缓存的渲染规则
var lastRendererType = "";// 上一次渲染的指标类型
var isRuleChanged = false;// 渲染规则是否有变化
//var dtopts;// 全局变量DT项
// 地图上的鼠标样式
var mapDefaultCursor = "";
var mapPointerCursor = "pointer";

// 当前加载的小区所属的区域id
var curAreaId = null;
/**
 * ======================================================= 变量定义结束
 * =======================================================
 */

$(document).ready(
		function() {
			
			//显示可以切换的地图
			$("#trigger").val(glomapid=='null' || glomapid=='baidu'?"切换谷歌":"切换百度");

			bindNormalEvent();

			initAreaCascade();

			$("#realPostAreaId").val($("#queryCellAreaId").val());// 第一次设置到隐藏域

			gisCellDisplayLib = new GisCellDisplayLib(map, minZoom,
					randomShowCnt, null, {
						'clickFunction' : bindCellClick,
					},null,null);

			var lng = $("#hiddenLng").val();
			var lat = $("#hiddenLat").val();

			gisCellDisplayLib.initMap("map_canvas", lng, lat, {
				'zoomend' : function(e, time) {
					gisCellDisplayLib.handleZoomEnd(e, time);
					dtSampleContainer.handleZoomEnd(e, time);
				},
				'moveend' : function(e, time) {
					gisCellDisplayLib.handleMoveEnd(e, time);
					dtSampleContainer.handleMoveEnd(e, time);
				}
			});

			// 获取默认鼠标样式
			mapDefaultCursor = gisCellDisplayLib.getDefaultCursor();
			//dtSampleContainer = new DtSampleContainer(map);
			dtSampleContainer = new DtSampleContainer(gisCellDisplayLib, null, null,null, {
				'clickFunction' : bindSampleClick,
			});
			//dtSampleContainer.setCellLib(gisCellDisplayLib);
			var wholeX = 0;
			var wholeY = 0;
			var mapCont = document.getElementById("map_canvas");
			var offset = getElementAbsPos(mapCont);
			wholeX = offset.left;
			wholeY = offset.top;
			// console.log("map cont:left=" + wholeX + ",top=" + wholeY);
			dtSampleContainer.setMapContOffset(wholeX, wholeY);
			loadAndShowAnalysisList();

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
						initPageParam();
						currentloadtoken = getLoadToken();
						loadGisCellData(currentloadtoken);
					});

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
		$("#realPostAreaId").val($("#queryCellAreaId").val());// 设置到隐藏域
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
		// 获得匹配选择器的第一个祖先元素：获取最接近$(this)的tr
		var tr = $(this).closest("tr");
		// 同辈元素的第一个元素的值
		var loadedConfigId = $(this).siblings(":first").val();
		if (tr.find("input.forselect").attr("checked")) {
			isSelectedNow = true;
		}
		if (isSelectedNow == false) {
			alert('请先选择需要从分析列表中删除的Dtitem');
			return;
		}
		// removeAnalysisItemFromLoadedListForAjaxAction

		$.ajax({
			url : 'removeDtItemFromAnalysisListForAjaxAction',
			data : {
				'configIds' : loadedConfigId
			},
			dataType : 'text',
			type : 'post',
			success : function(data) {
				tr.remove();
			}
		});

		// if (isSelectedNow) {
		// // 需要重新获取小区
		// changeAnalysisListSelection();
		// }
	});

	// 从加载列表中，选择若干列表进行分析
	$("#confirmSelectionAnalysisBtn").click(function() {
		// console.log("确定进来了");
		var items = $('input[name="fordescid"]:checked');
		var len = items.length;
		// console.log(len);
		if (len == 0) {
			alert("你还没有选择任何内容！");
			return;
		}
		// 触发改变的事件
		//
		var line = $('input[name="fordescid"]:checked');
		var dtDescId = line.val();
		// console.log("dtDescId:"+dtDescId);
		dtSampleContainer.clearData();
		loadSampleData(dtDescId, 1);

		// TODO 如果选择了同步显示小区，则同步显示小区
		var checked = $("#showWithCellCheck").attr("checked");
		var ars = line.siblings(".hiddenareaid");
		if (ars.length > 0) {
			var selAreaId = $(ars[0]).val();
		}
		// console.log("curAreaId=" + curAreaId + ",selAreaId==" + selAreaId);

		if (checked) {
			if (selAreaId != curAreaId) {
				// console.log("确定加载小区。。。");
				curAreaId = selAreaId;
				$("#realPostAreaId").val(curAreaId);// 设置用于提交的区域id
				// 执行加载小区
				gisCellDisplayLib.clearData();
				currentloadtoken = getLoadToken();
				loadGisCellData(currentloadtoken);
			} else {
				gisCellDisplayLib.resetSpecPolygonToDefaultOutlook();
			}
		}
	});

	// 显示渲染图例
	$("#showRenderColorBtn").click(
			function() {
				if (gisCellDisplayLib.getPolygonCnt() > 0
						&& dtSampleContainer.getMapElementsLen() > 0) {
					$("#analyze_Dialog").toggle();
				} else {
					animateInAndOut("operInfo", 500, 500, 1000, "operTip",
							"当前无相关的渲染图例");
				}
			});
	
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
	// getLoadedAnalysisListForAjaxAction
	$("#analysisListTable").empty();
	$
			.ajax({
				url : 'getAllLoadedDtListForAjaxAction',
				dataType : 'json',
				type : 'post',
				success : function(data) {
					// console.log(data);
					if (!data) {
						return;
					}
					// 在面板显示
					var htmlstr = "";
					var one;
					var trClass = "";
					for ( var i = 0; i < data.length; i++) {
						one = data[i];
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
									+ one['title']
									+ "</span>"
									+ "  </td>"
									+ "  <td  width=\"20%\"  class=\"td-standard-date bd-right-white td_nowrap\">"
									+ "  <span >"
									+ one['collectTime']
									+ "</span>"
									+ "  </td>"
									+ "  <td width=\"5%\" class=\"bd-right-white\">"
									+ "  <input type=\"checkbox\" class=\"forselect\" name=\"fordescid\" value=\""
									+ data[i]['configId']
									+ "\" id='"
									+ data[i]['configId']
									+ "' />"
									+ "  <label for=\"checkbox\"></label>"
									+ "<input type=\"hidden\" class=\"hiddenareaid\" value=\""
									+ data[i]['obj']["areaId"]
									+ "\" />"
									+ "  </td>"
									+ "  <td width=\"10%\">"
									+ "  <input type=\"button\" class=\"removebtn\"  value=\"移除\" /><input type=\"hidden\" class=\"hiddenconfigid\" value=\""
									+ data[i]['configId']
									+ "\" />  "
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
 * 根据所选择的分析列表加载小区数据
 */
function loadGisCellData(loadToken) {
	onLoadingGisCell = true;
	$("#conditionForm")
			.ajaxSubmit(
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
								// console.log("返回的数据有问题:" + err);
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
	var len = gisCells.length;

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

var loadflag = true;
/**
 * 从服务器加载指定采样点描述ID的统计情况
 * 
 * @param action
 * @param startIndex
 *            起始下标
 */
function loadSampleData(dtDescId, curpage) {
	// console.log("loadSampleData. startIndex=="+startIndex);
	// 封装page对象
	var senddata = {
		"dtDescId" : dtDescId,
		"page['totalPageCnt']" : 0,
		"page['pageSize']" : 100,
		"page['currentPage']" : curpage,
		"page['totalCnt']" : 0
	};
	$.ajax({
		url : 'querySampleDataInAreaByPageForAjaxAction',
		// data:"{'page':[{'totalPageCnt':0,'pageSize':25,'currentPage'=1,'totalCnt'=0,'forcedStartIndex'=-1}]}",
		data : senddata,
		dataType : 'text',
		type : 'post',
		async : true,
		success : function(data) {
			// console.log("data="+data);
			var obj = eval("(" + data + ")");
			if (!data) {
				return;
			}
			// totalCnt,hasMore,rnoStsResults,startIndex
			try {
				var page = obj['page'];
				curpage = page.currentPage;
				// console.log("curpage="+curpage);
				var samplelist = obj['list'];
				var hasMore = obj['hasMore'];
				// console.log(hasMore);
				// console.log(samplelist);
				// if(loadflag==true){
				if (curpage == 2) {
					// 坐标移动
					gisCellDisplayLib.panTo(samplelist[0].lng,
							samplelist[0].lat);
				}
				// loadflag=false;
				// }
				if (hasMore == true) {
					curpage++;
					loadSampleData(dtDescId, curpage);
				}
				// 画出圆圈
				dtSampleContainer.showOnMap(samplelist);

			} catch (err) {
				// console.error(err);
			}
		},
		error : function(xmh, textstatus, e) {
			alert("出错啦！" + textstatus);
		}
	});
}

var model1 = "小区分析";//
var model2 = "采样点分析";

// 选择小区覆盖范围的状态
var cellCoverMode_IDLE = "idle";
var cellCoverMode_WAIT_F_CELL = "wait";
var cellCoverMode_ANALYING = "doing";
var currentCellCoverMode = cellCoverMode_WAIT_F_CELL;
// idle->wait->doing->idle

// 选择采样点占用小区覆盖
var sampleOccupyMode_IDLE = "idle";
var sampleOccupyMode_WAIT_F_SAMPLE = "wait";
var sampleOccupyMode_ANALYING = "doing";
var currentSampleOccupyMode = sampleOccupyMode_IDLE;

// 绑定小区点击事件[小区覆盖图]
function bindCellClick(polygon,polygonobj) {
	//var polygonobj = polygon._data.getCell();
	if(!polygonobj) {
		var cmk = gisCellDisplayLib.getComposeMarkerByShape(polygon);
		polygonobj = cmk.getCell();
	}
	//var polygonobj = gisCellDisplayLib.getPolygonCell(polygon);

	if (currentCellCoverMode == cellCoverMode_WAIT_F_CELL) {
		hideOperTips();
		gisCellDisplayLib.setDefaultCursor(mapDefaultCursor);// 恢复鼠标为默认
		currentCellCoverMode = cellCoverMode_ANALYING;// 标识正在进行
		dtSampleContainer.staticsSingleServerCellCover(polygonobj,rendererFactory.getRule("servercellcover"));
		currentCellCoverMode = cellCoverMode_IDLE;// 分析完，恢复idle
	} else if (currentCellCoverMode == cellCoverMode_ANALYING) {
		// alert("当前正在进行分析小区的覆盖情况，请稍等。");
		animateOperTips("当前正在进行分析小区的覆盖情况，请稍等。");
		return;
	} else if (currentCellCoverMode == cellCoverMode_IDLE) {
		// 展现小区详情
		return;
	}
}
// 绑定采样点点击事件[采样点小区占用图]
function bindSampleClick(elementShape,event) {
	
	if (currentSampleOccupyMode == sampleOccupyMode_WAIT_F_SAMPLE) {
		hideOperTips();
		gisCellDisplayLib.setDefaultCursor(mapDefaultCursor);// 恢复鼠标为默认
		currentSampleOccupyMode = sampleOccupyMode_ANALYING;
		dtSampleContainer.staticsSingleSampleDetail(elementShape,rendererFactory.getRule('singlesampledetail'));
		currentSampleOccupyMode = sampleOccupyMode_IDLE;
	}
	// alert("点击sample");
	// 展现采样点详情
	var data = elementShape.getData();
	if (data) {
		// alert(data.getDetail());
		try {
			// 采样点详情
			var detail = data.getDetail();
			if (detail instanceof Array && detail.length > 0) {
				detail = detail[0];
			}
			if (detail) {
				var ncells = $.trim(getValidValue(detail['tdNcells']));
				var ncarr = ncells.split(",");
				ncells = "";
				var cnt = 0;
				if (ncarr) {
					for ( var i = 0; i < ncarr.length; i++) {
						if (ncarr[i] != "") {
							ncells += ncarr[i] + "&nbsp;&nbsp;"
							cnt++;
						}
						if ((cnt) % 3 == 0) {
							ncells += "<br/>";
						}
					}
				}
				$("#tdSampleTime").html(
						$.trim(getValidValue(detail['tdSampleTime'])));
				$("#tdServerCell").html(
						$.trim(getValidValue(detail['tdServerCell'])));
				$("#tdRxLev").html($.trim(getValidValue(detail['tdRxLev'])));
				$("#tdRxQual").html($.trim(getValidValue(detail['tdRxQual'])));

				$("#tdNcells").html(ncells);
				$("#tdNcellRxLev").html(
						$.trim(getValidValue(detail['tdNcellRxLev'])));
				$("#tdServerCellToSampleAngle")
						.html(
								$
										.trim(getValidValue(detail['tdServerCellToSampleAngle'])));
				$("#tdServerCellAngle").html(
						$.trim(getValidValue(detail['tdServerCellAngle'])));
			}
		} catch (err) {

		}
	}

}
// [小区覆盖图]
function CellCoverClickEvent() {

	if (gisCellDisplayLib.getPolygonCnt() === 0) {
		animateOperTips("尚未有小区数据，该功能无法进行！");
		return;
	}
	clearPreState();
	if (currentCellCoverMode == cellCoverMode_IDLE) {
		// alert("请在地图中选择可视小区进行分析");
		showOperTips("请在地图中选择可视小区进行分析");
		// console.log("test()==model1=" + model1);
		dtSampleContainer.clearOnlyExtraOverlay();
		gisCellDisplayLib.resetSpecPolygonToDefaultOutlook();
		gisCellDisplayLib.setDefaultCursor(mapPointerCursor);// 设置鼠标为手型
		currentCellCoverMode = cellCoverMode_WAIT_F_CELL;
	} else if (currentCellCoverMode == cellCoverMode_WAIT_F_CELL) {
		showOperTips("请在地图中选择可视小区进行分析");
	} else if (currentCellCoverMode == cellCoverMode_ANALYING) {
		showOperTips("请在地图中选择可视小区进行分析");
	}
}
// [采样点小区占用图]
function SampleCellOccupyClickEvent() {

	clearPreState();
	if (currentSampleOccupyMode == sampleOccupyMode_IDLE) {
		// alert("请在地图中选择可视采样点进行分析");
		showOperTips("请在地图中选择可视采样点进行分析");
		dtSampleContainer.clearOnlyExtraOverlay();
		gisCellDisplayLib.resetSpecPolygonToDefaultOutlook();
		gisCellDisplayLib.setDefaultCursor(mapPointerCursor);// 设置鼠标为手型
		currentSampleOccupyMode = sampleOccupyMode_WAIT_F_SAMPLE;
	} else if (currentSampleOccupyMode == sampleOccupyMode_WAIT_F_SAMPLE) {
		showOperTips("请在地图中选择可视采样点进行分析");
	} else if (currentSampleOccupyMode == sampleOccupyMode_ANALYING) {
		showOperTips("请在地图中选择可视采样点进行分析");
	}
	// if(model=="normal"){bindCellClick(polygon);};
	// dtSampleContainer.staticsSingleServerCellCover();
}
/*function createmark() {

	var marker1 = new BMap.Marker(new BMap.Point(113.83503954105389,
			22.15022213891501)); // 创建标注
	map.addOverlay(marker1); // 将标注添加到地图中

	dtSampleContainer.clearOnlyExtraOverlay();
}*/
/**
 * 统计功能
 * 
 * @param type
 *            指明统计类型：信号强度，
 * @param action
 *            提交到后台的action名称
 * @param name
 *            如果name不为空，说明要获取type类型的name指定的范围的统计值。如获取type=信号与天线方向不符，name=正常覆盖
 */
function commondtstatics(type, action, name, fun, dontanimateout) {
	if (dtSampleContainer.getMapElementsLen() === 0) {
		animateOperTips("尚未有采样数据，无法进行分析！");
		return;
	}
	var areaId = $("#cityId option:selected").val();
	rendererFactory.findRule(type, areaId, function(rule, ruleCode, areaId) {
		/*if (dtopts != null || dtopts.length != 0) {
			dtopts.length = 0;
		}*/
		//var dtopts = rule.getRuleSettings();
		// console.log("dtopts[0]="+dtopts[0].params.distance);
		if (typeof fun == "function") {
			fun(rule);
			if (!dontanimateout) {
				animateOperTips("分析完毕！");
			}
		}
		// 图形化显示
		showRendererRuleColor(rule.rawData, ruleCode, areaId);
		lastRendererType = type;

	}, false);
}
/**
 * 从服务器加载指定DT指标的统计情况
 * 
 * @param action
 * @param type
 *            指标类型
 * @param name
 *            指标类型下的某类型小区
 * @param startIndex
 *            起始下标
 */
function loadDtStaticsInfo(rule, action, type, name, startIndex, callback) {
	console.log("loadDtStaticsInfo:" + action);
	// console.log("loadStaticsInfo.
	// startIndex=="+startIndex+",action="+action+",type="+type);
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
					for ( var i = 0; i < len; i++) {
						cell = stsresult[i]['cell'];
						// console.log("填充cell="+cell+" 的
						// stsresult="+stsresult[i]);
						// oriStsResults = allCellStsResults[cell];
						// if (!oriStsResults) {
						// oriStsResults = new CellStsResult(cell);
						// allCellStsResults[cell] = oriStsResults;
						// }
						// oriStsResults.set(type, stsresult[i]);// 增加某种类型的统计结果

						option = createOptionFromStsResult(stsresult[i], rule);
						gisCellDisplayLib
								.changeCellPolygonOptions(cell, option);
					}
				}
				if (hasMore === true) {
					loadStaticsInfo(rule, action, type, name, startIndex,
							callback);
				} else {
					// console.log("准备调用loadStaticsInfo的callback...");
					callback();
				}
			} catch (err) {
				// console.error(err);
				hideTips("");
			}
		},
		error : function() {
			hideTips("");
			alert("系统出错！请联系管理员！");
		},
		complete : function() {
			hideTips("");
		}
	});
}
