/**
 * 忙小区邻区分析
 */

var gisCellDisplayLib;// 负责显示小区的对象
var map;
var firstTime = true;

var analysisList = new Array();// 分析列表
var allallCellStsResultss = new Object();// 所有的小区信息列表,key是小区名，value是CellStsResult对象
var rendererFactory = new RendererFactory(false);// 缓存的渲染规则
var topNLabels = new Array();
var currentSelConfigIds = new Array();// 当前选择的用于分析的分析列表

var currentloadtoken = "";// 加载任务的token

var minZoom = 17;// 只有大于 该 缩放级别，才真正
var preZoom = 15;// 当前的缩放级别
var minResponseInterval = 1000;// 对事件做响应的最小的间隔
var lastOperTime = 0;// 最后一次操作的时间
var randomShowCnt = 150;// 在不需要全部显示的时候，最大随机显示的个数

var currentAreaCenter;// 当前所选区域的中心点

var onLoadingGisCell = false;// 是否正在加载小区信息

var lastRendererType = "";// 上一次渲染的指标类型
var celllabel = "";

var heavyLoadCell = new Array();// 忙小区,key为忙小区的cell名称，value为该忙小区的统计情况
var heavyLoadCellNcell = new Array();// 忙小区的邻区，key为忙小区cell名称，value为其邻区的统计情况（列表）
var stsResult = new Array();

// 当前应该应用的规则
var currentRule = null;
var defaultCursor = "";

var currentSelConfigId = null;// 当前选择的小区配置id
var currentSelInterConfigId = null;// 当前选择的小区干扰数据id

var whetherConfirm=false;//是否已经确定区域
var cellFreqToNcellInter=new Object();//小区频点到邻区的干扰映射
//是否完成全部干扰分析
isCompletedTotalInter=false;
var cellInterAndLnglats;//小区干扰及经纬度信息
var currentActiveRow;//当前选中行
//bcch与tch缓存
var bcchCache = "";
var tchCache = "";

var specRenderderLabel = new Array();// 频点搜索出来后用于显示的label
var targetCellColor = "#B40431";// 暗红色
var targetNcellColor = "#2EFE2E";// 耀眼的绿色
var ncellsOfCell = new Array();// 缓存获取到的小区的邻区，存储的是CellToNcell对象

var mapGridLib //用于将地图分成网格加载小区的对象

var isShowCellName = false; //地图是否显示小区

//地图小区右键菜单
var contextMenu = [
	{
		text:'查看Noise',
		callback:function(){
			var polygon;
			if (contextMenu.length!=0) {
				polygon=contextMenu[contextMenu.length-1].polygon;
				contextMenu.splice(contextMenu.length-1,1);
			}
			//console.log(polygon._data.getCell());
			$("#interPlanLi").trigger("click");
			//加入判断是否是重叠小区
			var cmk = polygon._data;
			if (cmk.getCount() > 1) {
					var html = "";
					if (!cmk) {
						return "";
					}
					var cellArray = cmk.getCellArray();
					var cell;
					for ( var i = 0; i < cellArray.length; i++) {
						cell = cellArray[i];
						html += "<a onclick='showNoiseFunction(this,\""+cell.cell+"\")' "
							 + " target='_blank'>"+ (cell.chineseName ? cell.chineseName : cell.cell)
							 + "</a><br/><br/>";
					}
					html = "<h4 style='margin:0 0 5px 0;padding:0.2em 0'>重叠小区（"
							+ cmk.getCount() + "个）</h4>" + html;
					gisCellDisplayLib.showInfoWindow(html, gisCellDisplayLib.getOriginPointByShape(polygon));
			} else {
				showNoiseFunction(polygon);
			}
		}
	},
	{
		text:'in干扰',
		callback:function(){
			var polygon;
			if (contextMenu.length!=0) {
					polygon=contextMenu[contextMenu.length-1].polygon;
					contextMenu.splice(contextMenu.length-1,1);
				}
				//console.log(polygon._data.getCell());
				//切换tab
				$("#interDetailsLi").trigger("click");
				
				//加入判断是否是重叠小区
				var cmk = polygon._data;
				if (cmk.getCount() > 1) {
						var html = "";
						if (!cmk) {
							return "";
						}
						var cellArray = cmk.getCellArray();
						var cell;
						for ( var i = 0; i < cellArray.length; i++) {
							cell = cellArray[i];
							html += "<a onclick='showInterCellFromIn(this,\""+cell.cell+"\")' "
								 + " target='_blank'>"+ (cell.chineseName ? cell.chineseName : cell.cell)
								 + "</a><br/><br/>";
						}
						html = "<h4 style='margin:0 0 5px 0;padding:0.2em 0'>重叠小区（"
								+ cmk.getCount() + "个）</h4>" + html;
						gisCellDisplayLib.showInfoWindow(html, gisCellDisplayLib.getOriginPointByShape(polygon));
				} else {
					showInterCellFromIn(polygon);
				}
			}
	},
	{
		text:'out干扰',
		callback:function(){
			var polygon;
			if (contextMenu.length!=0) {
					polygon=contextMenu[contextMenu.length-1].polygon;
					contextMenu.splice(contextMenu.length-1,1);
				}
				//console.log(polygon._data.getCell());
				//切换tab
				$("#interDetailsLi").trigger("click");
				//加入判断是否是重叠小区
				var cmk = polygon._data;
				if (cmk.getCount() > 1) {
						var html = "";
						if (!cmk) {
							return "";
						}
						var cellArray = cmk.getCellArray();
						var cell;
						for ( var i = 0; i < cellArray.length; i++) {
							cell = cellArray[i];
							html += "<a onclick='showInterCellFromOut(this,\""+cell.cell+"\")' "
								 + " target='_blank'>"+ (cell.chineseName ? cell.chineseName : cell.cell)
								 + "</a><br/><br/>";
						}
						html = "<h4 style='margin:0 0 5px 0;padding:0.2em 0'>重叠小区（"
								+ cmk.getCount() + "个）</h4>" + html;
						gisCellDisplayLib.showInfoWindow(html, gisCellDisplayLib.getOriginPointByShape(polygon));
				} else {
					showInterCellFromOut(polygon);
				}
			}
	},
	{
		text:'改频点',
		callback:function(){
			var polygon;
			if (contextMenu.length!=0) {
					polygon=contextMenu[contextMenu.length-1].polygon;
					contextMenu.splice(contextMenu.length-1,1);
					//console.log("callback:defaultcontextMenu回调函数："+contextMenu);
				}
				//console.log(polygon._data.getCell());
				//加入判断是否是重叠小区
				var cmk = polygon._data;
				if (cmk.getCount() > 1) {
						var html = "";
						if (!cmk) {
							return "";
						}
						var cellArray = cmk.getCellArray();
						var cell;
						for ( var i = 0; i < cellArray.length; i++) {
							cell = cellArray[i];
							html += "<a onclick='showCellFreqWindow(this,\""+cell.cell+"\")' "
								 + " target='_blank'>"+ (cell.chineseName ? cell.chineseName : cell.cell)
								 + "</a><br/><br/>";
						}
						html = "<h4 style='margin:0 0 5px 0;padding:0.2em 0'>重叠小区（"
								+ cmk.getCount() + "个）</h4>" + html;
						gisCellDisplayLib.showInfoWindow(html, gisCellDisplayLib.getOriginPointByShape(polygon));
				} else {
					showCellFreqWindow(polygon);
				}
			}
	}
];

$(document).ready(
		function() {
			gisCellDisplayLib = new GisCellDisplayLib(map, minZoom,
					randomShowCnt, null, {
					    //'clickFunction' : clickFunction,
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
			defaultCursor = gisCellDisplayLib.getDefaultCursor();
			
			//以网格形式在地图加载小区
			mapGridLib = new MapGridLib(gisCellDisplayLib,"conditionForm","loadingCellTip"); 
			//以城市为单位创建区域网格
			var cityName = $("#cityId").find("option:selected").text().trim();
			mapGridLib.createMapGrids(cityName);
			
			// 区域联动
			//initAreaCascade();
			$.when(initAreaCascade()).then(function() {
				if(!whetherConfirm){
				var areaname = $.trim($("#areaId").find("option:selected").text());
				var areaid = $("#areaId").find("option:selected").val();
				$(".defaultBtn").attr("id", areaid);
				$(".defaultBtn").val("默认选择[" + areaname+"]");
				}
			});
			$("#confirmBtn").click(function(){
				whetherConfirm=true;//已经选择确定区域
				$("#confirmBtn").attr("disabled",true);
				$("#reSelBtn").attr("disabled",false);
			});
			$("#reSelBtn").click(function(){
				whetherConfirm=false;//已经选择确定区域
				$("#reSelBtn").attr("disabled",true);
				$("#confirmBtn").attr("disabled",false);
			});
			// stsResult = getRnoTrafficRenderer('pdinterference');

			var areaId = $("#cityId").val();
//			rendererFactory.findRule("pdinterference", areaId, function(rule,
//					ruleCode, areaId) {
//				// console.log("在callback中，rule.rawData="+rule.rawData);
//				// 图形化显示
//				showRendererRuleColor(rule.rawData, "pdinterference", areaId,
//						"", false);
//				currentRule = rule;
//			});

			// 获取gis数据数据
			currentloadtoken = getLoadToken();
			// getGisCell(currentloadtoken);

			bindNormalEvent();
			
			//loadAndShowCellConfigAnalysisList();// 小区配置
//			loadAndShowCellInteferenceAnalysisList();// 小区干扰
			var mousex = 0, mousey = 0;
	        var divLeft, divTop;
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
				
		
		$("#showNcsDetailBtn").click(function() {
		$("#reSelNcs_Dialog").toggle();
		var display = $("#reSelNcs_Dialog").css("display");
//		console.log("display:"+display);
//		var loadjs=false;
		if ("block" == display) {
			
			$("#ncs_dialog_content").load(
					"initRnoStructNcsDetailDisplayAction #frame", function() {
						//$("#frame").css("width", "600px");

						$.ajaxSetup({
									cache : true
								});
//						if(loadjs){return;}
						//console.log("加载js");
						$.getScript("js/rno_structure_ncs_detail_display.js");//.done(function(){
							//alert("加载完成");
						//});
//						loadjs=true;
						// $("#importAndLoadBtn").remove();
					// console.log($("head").children(":last").attr("src"));
					 var pagecitystr = $
								.trim($("#conditionForm #cityId option:checked")
										.text());
						var pageareastr = $
								.trim($("#conditionForm #areaId option:checked")
										.text());
						var pagecityval = $("#conditionForm #cityId option:checked")
								.val();
						var pageareaval = $("#conditionForm #areaId option:checked")
								.val();

						$("#ncsDescDataForm #cityId2").html("<option value=\""
								+ pagecityval + "\"> " + pagecitystr
								+ " </option>");
						$("#ncsDescDataForm #areaId2").html("<option value=\""
								+ pageareaval + "\"> " + pageareastr
								+ " </option>");
//						console.log($("#ncsDescDataForm #areaId2").find("option:selected").text());
//						console.log($("#ncsDescDataForm #areaId2").text());
//						//ncs记录查询
//						console.log($("#searchNcsDescBtn"));
						//ncs记录查询
						try
						{
						initFormPage("ncsDescDataForm");
						doQueryNcsDesc();
						$("#searchNcsDescBtn").trigger("click");
						}catch(e){
							console.log("e:"+e);
						}
						
				});
			}
		});
			
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
			mapGridLib.loadGisCell(t-minResponseInterval, currentloadtoken, minResponseInterval,params);
		});
		//获取ncs任务最新时间
		getLatelyNcsTaskTime();
		$("#locatecell").click(function(){
			gisCellDisplayLib.panTo(113.36079487718192,23.179828049350228);
			var option_serverCell={
					'fillColor':'#FF0000'
			};
			var cell="GBVDGZ3";
			gisCellDisplayLib.changeCellPolygonOptions(cell,option_serverCell,false);	
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
}, false);

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
$(document).keypress(function(e) {
	// console.log("keydown. e.which="+e.which);
	switch (e.which) {
	case 0:// ESC
		if (curstate == STATE_WAIT) {
			translateToIdle();
		}
	}
});

// 小区分析状态转到空闲
function translateToIdle() {
	curstate = STATE_IDLE;
	hideOperTips("operInfo");
	gisCellDisplayLib.setDefaultCursor(defaultCursor);
}

function bindNormalEvent() {
	// 2013-12-10 gmh add
	$("#showCellBtn").click(
			// 仅仅在地图上显示配置项指定的小区数据
			// getFreqReuseCellGisInfoFromSelAnaListForAjaxAction
			function() {
				var id = $("input[name='cellconfigradio']:checked").val();
				if (!id) {
					animateInAndOut("operInfo", 500, 500, 1000, "operTip",
							"请先选择一个小区配置");
					return false;
				}
				currentSelConfigId=id;//chao.xj
				// 确定加载某个配置的小区到地图
				initPageParam();// 恢复分页参数
				showOperTips("operInfo", "operTip", "正在加载小区。。。");
				clearAll();
				currentloadtoken = getLoadToken();
				loadGisCellByConfig(currentloadtoken, id);
			});

	// 2013-12-11 gmh add
	$("#analyzeAllCellBtn").click(
			function() {
//				console.log("进入analyzeAllCellBtn点击事件");
				// 对小区进行干扰分析，需要的数据：小区配置数据、干扰配置数据
				// 如果已经选择了小区配置，但是还没有在地图上显示小区，则同时在地图上显示小区
				// 如果已经在地图上显示了小区数据，则只是更改小区的颜色
				cellInterAndLnglats=undefined;
				// 检查干扰项
				var id = $("input[name='interconfigradio']:checked").val();
//				console.log(id);
				if (typeof(id)=="undefined") {
					animateInAndOut("operInfo", 500, 500, 1000, "operTip",
							"请先选择一个NCS干扰项!");
					return false;
				}
				currentSelInterConfigId = id;

				// 检查小区配置项
				/*if (currentSelConfigId == null || currentSelConfigId == "") {
					// 允许在未加载小区到地图上之前，进行总体干扰分析，此时会自动加载小区
					id = $("input[name='cellconfigradio']:checked").val();
					if (!id) {
						animateInAndOut("operInfo", 500, 500, 1000, "operTip",
								"请先选择一个小区配置");
						return false;
					} else {
						// 将自动执行加载
						currentSelConfigId = id;
					}
				}*/
				
				// 执行加载
				initPageParam();// 恢复分页参数
//				showOperTips("operInfo", "operTip", "正在加载小区。。。");
//				clearAll();
				currentloadtoken = getLoadToken();
				/*loadGisCellWithInterData(currentloadtoken, currentSelConfigId,
						currentSelInterConfigId);*/
				//835
			    getGisCellTotalInterData(currentloadtoken,currentSelInterConfigId);

			});

	$("#topN").click(
			function() {
//				console.log("进入#topN click");
				var ready = checkIfAllDataReady();
				if(!isCompletedTotalInter){
					animateInAndOut("operInfo", 500, 500, 1000, "operTip",
								"请先完成全部干扰分析然后再进行最大干扰小区标注！");
						return;
				}
				if (ready == true) {
					if (gisCellDisplayLib.getPolygonCnt() == 0) {
						animateInAndOut("operInfo", 500, 500, 1000, "operTip",
								"当前不存在渲染小区！");
						return;
					}
					//getRnoGisCellInAreaTop();
					getRnoGisCellInAreaTop(cellInterAndLnglats);
				} else {
					animateInAndOut("operInfo", 500, 500, 1000, "operTip",
							"请先选择小区配置和干扰数据！");
				}
			});

	// 单个小区的分析
	$("#cellInter").click(
	
			function() {
				
				
				cellFreqToNcellInter = null;// 清空对象
				cellFreqToNcellInter = {};
				//先清空除了前两行表头
				$("#freqintersituation tr:gt(0)").remove();
				//先清空详情除了前两行表头
				$("#detailedintersituation tr:gt(0)").remove();
				var ready = checkIfAllDataReady();
//				var ready=true;
//				console.log("ready:"+ready);
				if (ready == true) {
					if (curstate == STATE_IDLE) {
						curstate = STATE_WAIT;// 状态转换
						gisCellDisplayLib.setDefaultCursor("pointer");
						showOperTips("operInfo", "operTip", "请在地图上选择一个小区进行分析");
					}
				} else {
					animateInAndOut("operInfo", 500, 500, 1000, "operTip",
							"请先选择NCS小区干扰数据！");
				}

			});
	// 打开渲染图例的窗口
	$("#showRenderColorBtn").click(function() {
		$("#analyze_Dialog").toggle();
	});
	// 打开小区干扰的窗口
	$("#showCellInterWinBtn").click(function() {
		$("#interference_dialogId").toggle();
	});
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
}

// 小区干扰分析的状态
var STATE_IDLE = "idle";
var STATE_WAIT = "wait";
var STATE_DOING = "doing";
var curstate = STATE_IDLE;

function initPageParam() {
	$("#hiddenPageSize").val(100);
	$("#hiddenCurrentPage").val(1);
	$("#hiddenTotalPageCnt").val(0);
	$("#hiddenTotalCnt").val(0);
	$("#hiddenForcedStartIndex").val(-1);
}

/**
 * 检查看是否分析需要的数据都具备了
 */
function checkIfAllDataReady() {
	// 小区配置是需要手工点击确定来加载的
//	console.log("进入checkIfAllDataReady");
	/*if (!currentSelConfigId) {
		// animateInAndOut("operInfo", 500, 500, 1000, "operTip", "请先选择一个小区配置");
		return false;
	}*/
//	console.log("currentSelConfigId:"+currentSelConfigId);
	// 干扰配置只需要在页面上选择即可
	var id = $("input[name='interconfigradio']:checked").val();
//	console.log("id:"+id);
	if (typeof(id)=="undefined") {
		 animateInAndOut("operInfo", 500, 500, 1000, "operTip",
		 "请先选择一个NCS小区干扰配置");
		return false;
	}
	currentSelInterConfigId = id;
	return true;
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
			if (htmlstr != "") {
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
			if (htmlstr != "") {
				$("#analysisBtnDiv").show();
			}
		}
	});
}
/**
 * 获取和展现被加载的NCS小区干扰分析列表
 */
function loadAndShowNcsCellInteferenceAnalysisList(inputobj) {
//	$("#analysisListTable_cellinterference").empty();
//	console.log("进入loadAndShowNcsCellInteferenceAnalysisList"+inputobj+":"+$(inputobj).attr("checked"));
	//queryNcsResultTab
	var ncsdescid=$(inputobj).attr("id");
	var pagecity=$.trim($("#cityId option:checked").text());
	var pagearea=$.trim($("#areaId option:checked").text());
	var accordwith=false;
	//如果不符合返回
	$.ajax({
		url : "getNcsAreaInfoByNcsIdForAjaxAction",
		data: {selectId:ncsdescid},
		dataType : 'json',
		type : 'post',
		async: false,
		success : function(data) {
//			console.log(data[0].AREA+":"+data[0].CITY);
			var sqlarea=data[0].AREA;
			var sqlcity=data[0].CITY;
			if(typeof(sqlarea)=="undefined"){
				if(pagecity==sqlcity){
					if(pagearea=="全部"){
//						console.log("pagearea:"+pagearea);
						accordwith=true;
					}else{
						alert("页面区域应该是【"+sqlcity+"】的全部区域,因为ncs为该市所有区域数据!");
						$(inputobj).attr("checked",false);
					}
				}else{
					alert("所选ncs与页面【"+pagecity+"】不在同一区域,所选ncs为【"+sqlcity+"】");
					$(inputobj).attr("checked",false);
				}
			}else{
				if(pagearea==sqlarea){
						accordwith=true;
					}else{
						alert("页面区域为【"+pagearea+"】而ncs区域为【"+sqlarea+"】不一致不符要求!");
						$(inputobj).attr("checked",false);
					}
			}
		}
	});
	if(!accordwith){
		return;
	}
	
	//被选中则
	if($(inputobj).attr("checked")=="checked"){
		
		var seltrobj=$(inputobj).closest("tr");
		var filename=seltrobj.find("td").eq(1).text();
		var bscname=seltrobj.find("td").eq(2).text();
		var freqname=seltrobj.find("td").eq(3).text();
//		console.log(ncsdescid);
		var htmlstr = "<tr class=\"tb-tr-bg-coldwhite\">"
				+ "<td>"
				+ filename
				+ "</td>"
				+ "<td>"
				+ bscname
				+ "</td>"
				//+ "<td>"
				//+ freqname
				//+ "</td>"
				+ "<td>"
				+ "<input type=\"radio\" value=\""+ncsdescid+"\" name=\"interconfigradio\" class=\"forselect\">"
				+ "</td>"
				+ "<td>"
				+ "<input type=\"button\" onclick=\"rmvCellInterferenceAnaliysis(this)\" value=\"移除\" name=\"celliterference\" class=\"removebtn\">"
				//+ "<input type=\"hidden\" value=\""+ncsdescid+"\" class=\"hiddenconfigid\">"
				+ "</td>" + "</tr>";
		$("#analysisListTable_cellinterference").append(htmlstr);
	}else{
		//未选中或取消选中
		//删除已经存在的
//		var ncsdescid=$(inputobj).val();
		$("#analysisListTable_cellinterference tr").each(function(i,e){
//			console.log("e:"+e);
			var valid=$(e).find("input[type='radio']").val();
//			console.log(valid);
			if(ncsdescid==valid){
				$(e).remove();
			}
		});
		
	}
//			var htmlstr = getCellInterferenceAnaliysisHtml(data);
//			$("#analysisListTable_cellinterference").append(htmlstr);
			/*if (htmlstr != "") {
				$("#analysisBtnDiv").show();
			}*/

}
/**
 * 获取小区配置html
 * 
 * @param {}
 *            data
 */
function getCellConfigAnaliysisHtml(data) {
	if (!data) {
		return;
	}
	var htmlstr = "";
	var trClass = "";
	for ( var i = 0; i < data.length; i++) {
		if (i % 2 == 0) {
			trClass = "tb-tr-bg-coldwhite";
		} else {
			trClass = "tb-tr-bg-warmwhite";
		}
		var tempType = "";
		htmlstr += "<tr class=\""
				+ trClass
				+ "\">"// table 内容
				+ "  <td width=\"45%\" class=\"bd-right-white\" >    "
				+ "  <span >"
				+ data[i]['title']
				+ "</span>"
				+ "  </td>"
				+ "  <td width=\"45%\" class=\"bd-right-white\" >    "
				+ "  <span >"
				+ data[i]['name']
				+ "</span>"
				+ "  </td>"
				+ "  <td  width=\"20%\"  class=\"td-standard-date bd-right-white td_nowrap\">"
				+ "  <span >"
				+ data[i]['collectTime']
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
				+ "  <input type=\"button\" class=\"removebtn\" name=\"cellconfig\" value=\"移除\" onclick=\"rmvCellConfigAnaliysis(this)\" /><input type=\"hidden\" class=\"hiddenconfigid\" value=\""
				+ data[i]['configId']
				+ "\" />"
				+ "  </td >                                                                                                                     "
				+ "  </tr>"
				+ "<tr><td colspan=\"6\" style=\"background-color: #e7e7e7; height:1px; width:100%\"></td> </tr> ";
	}
	return htmlstr;
}
/**
 * 移除小区配置列表
 * @param {} obj
 */
function rmvCellConfigAnaliysis(obj){
	var tdobj=$(obj).parent();
	var trobj=tdobj.parent();
	var radiocheck=trobj.find("input[name=cellconfigradio]:checked").size();
	if(radiocheck==0){
		alert("请选择再删除!");
		return;
	}
	var configId=tdobj.find("input:hidden").val();
//	console.log("configId:"+configId);
		$.ajax({
		url : 'removeCellConfigAnalysisItemFromLoadedListForAjaxAction',
		data : {
			'configId' : configId
		},
		dataType : 'text',
		type : 'post',
		success : function(data) {
			var p=$(obj).parent().parent();
			$(p).remove();
		}
	});
}
/**
 * 获取小区干扰列表html
 * 
 * @param {}
 *            data
 */
function getCellInterferenceAnaliysisHtml(data) {
	if (!data) {
		return;
	}
	var htmlstr = "";
	var trClass = "";
	for ( var i = 0; i < data.length; i++) {
		if (i % 2 == 0) {
			trClass = "tb-tr-bg-coldwhite";
		} else {
			trClass = "tb-tr-bg-warmwhite";
		}
		htmlstr += "<tr class=\""
				+ trClass
				+ "\">"// table 内容
				+ "  <td width=\"45%\" class=\"bd-right-white\" >    "
				+ "  <span >"
				+ data[i]['title']
				+ "</span>"
				+ "  </td>"
				+ "  <td width=\"45%\" class=\"bd-right-white\" >    "
				+ "  <span >"
				+ data[i]['name']
				+ "</span>"
				+ "  </td>"
				+ "  <td  width=\"20%\"  class=\"td-standard-date bd-right-white td_nowrap\">"
				+ "  <span >"
				+ data[i]['collectTime']
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
				+ "  <input type=\"button\" class=\"removebtn\" name=\"celliterference\"  value=\"移除\" onclick=\"rmvCellInterferenceAnaliysis(this)\"/><input type=\"hidden\" class=\"hiddenconfigid\" value=\""
				+ data[i]['configId']
				+ "\" />"
				+ "  </td >                                                                                                                     "
				+ "  </tr>"
				+ "<tr><td colspan=\"6\" style=\"background-color: #e7e7e7; height:1px; width:100%\"></td> </tr> ";
	}
	return htmlstr;
}
/**
 * 移除小区干扰列表
 * @param {} obj
 */
/*function rmvCellInterferenceAnaliysis(obj){
	var tdobj=$(obj).parent();
	var trobj=tdobj.parent();
	var radiocheck=trobj.find("input[name=interconfigradio]:checked").size();
	if(radiocheck==0){
		alert("请选择再删除!");
		return;
	}
	var configId=tdobj.find("input:hidden").val();
//	console.log("configId:"+configId);
		$.ajax({
		url : 'removeInterferenceItemsFromLoadedListForAjaxAction',
		data : {
			'configIds' : configId
		},
		dataType : 'text',
		type : 'post',
		success : function(data) {
			if (data == "true") {
					var p=$(obj).parent().parent();
					$(p).remove();
				}
			
		}
	});
}*/
/**
 * 移除NCS小区干扰列表
 * @param {} obj
 */
function rmvCellInterferenceAnaliysis(obj){
	var trobj=$(obj).parent().parent();
	var ncsdescid=trobj.find("input[type='radio']").val();
	trobj.remove();
	$("#queryNcsResultTab tr").each(function(i,e){

			var check_input=$(e).find("input[type='checkbox']");
			var valid=check_input.attr("id");
//			console.log(ncsdescid+":"+valid);
			if(ncsdescid==valid){
				check_input.attr("checked",false);
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
					//ncs任务最近一次的时间
					getLatelyNcsTaskTime();
					//$("#areaId").trigger("change");
					$("#areaId").append("<option value=\"-1\">全部</option>");
				});
			});

	$("#areaId").change(function() {
		 var pagecitystr = $
								.trim($("#conditionForm #cityId option:checked")
										.text());
						var pageareastr = $
								.trim($("#conditionForm #areaId option:checked")
										.text());
						var pagecityval = $("#conditionForm #cityId option:checked")
								.val();
						var pageareaval = $("#conditionForm #areaId option:checked")
								.val();

						$("#ncsDescDataForm #cityId2").html("<option value=\""
								+ pagecityval + "\"> " + pagecitystr
								+ " </option>");
						$("#ncsDescDataForm #areaId2").html("<option value=\""
								+ pageareaval + "\"> " + pageareastr
								+ " </option>");
		if(!whetherConfirm){
			//clearAll();//清除
			currentloadtoken = "";
			var areaname=$.trim($("#areaId").find("option:selected").text());
			var areaid=$("#areaId").find("option:selected").val();
			$(".defaultBtn").attr("id",areaid);
			$(".defaultBtn").val("默认选择[" + areaname+"]");
			isCompletedTotalInter=false;
		}
//		console.log();
		// stsResult = getRnoTrafficRenderer('pdinterference');
		var lnglat = $("#areaid_" + $("#areaId").val()).val();
		// console.log("改变区域:" + lnglat);
		// 清除当前区域的数据
		// clearAll();

		if (lnglat) {
			// 地图中心点
			var lls = lnglat.split(",");
			if (lls[0] == 0 || lls[1] == 0) {
				// console.warn("未设置该区域的中心点经纬度。");
			} else {
				// 地图移动
				gisCellDisplayLib.panTo(lls[0], lls[1]);

				// currentloadtoken = getLoadToken();
				// getGisCell(currentloadtoken);
				// var point = new BMap.Point(113.342974, 23.02308);
				// var tempmk = new BMap.Marker(point);
				// map.addOverlay(tempmk);'
				// map.panTo(point);
			}
		}
		//console.log("areaid改变进入");
		//ncs任务最近一次的时间
		//getLatelyNcsTaskTime();
		
		//以城市为单位创建区域网格
		clearAll(); //清除缓存
		var cityName = $("#cityId").find("option:selected").text().trim();
		mapGridLib.createMapGrids(cityName);
	});
	
}

function clearAll() {
	// 查询form
	resetQueryCondition();
	// 忙小区相关
	heavyLoadCell.splice(0, heavyLoadCell.length);
	heavyLoadCellNcell.splice(0, heavyLoadCellNcell.length);

	// 地图显示相关
	gisCellDisplayLib.clearData();
	for ( var i = 0; i < topNLabels.length; i++) {
		gisCellDisplayLib.removeOverlay(topNLabels[i]);
	}
}

// 重置查询表单的值
function resetQueryCondition() {
	$("#hiddenCurrentPage").val(1);
	$("#hiddenTotalPageCnt").val(0);
	$("#hiddenTotalCnt").val(0);
	$("#hiddenForcedStartIndex").val(-1);
	$("#hiddenPageSize").val(100);

}

/**
 * 
 * @param loadToken
 *            加载token
 * @param url
 *            url
 * @param data
 *            携带数据
 * @param celllistkey
 *            返回的小区列表所在的key
 * @param needRender
 *            是否要渲染
 */
function handleGiscellLoadCommon(loadToken, url, postData, celllistkey,
		needRender) {
	onLoadingGisCell = true;
	$("#conditionForm")
			.ajaxSubmit(
					{
						'url' : url,
						'data' : postData,
						dataType : 'json',
						success : function(data) {
//							console.log("全部干扰分析：data:"+data);
							if (loadToken != currentloadtoken) {
								// 新的加载开始了，这些旧的数据要丢弃
								return;
							}
							// console.log(data);
							var obj = data;
							try {
								showGisCellOnMap(obj[celllistkey]);
								if (needRender == true) {
									for ( var i = 0; i < obj[celllistkey].length; i++) {
										var cellData = obj[celllistkey][i];
										var op = createOptionFromStsResult(cellData);
										op['fillOpacity']=0.2;
										gisCellDisplayLib
												.changeCellPolygonOptions(
														cellData['cell'], op,
														false);
									}
								}
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
									forcedStartIndex = -1;// 禁用

									$("#hiddenForcedStartIndex").val(
											forcedStartIndex);

									var totalCnt = page['totalCnt'] ? page['totalCnt']
											: 0;
									totalCellCnt = totalCnt;
									$("#hiddenTotalCnt").val(totalCnt);

									// 下一次的起点
									// currentPage从1开始
									var nextStartIndex = (currentPage - 1)
											* pageSize;
									// console.log("nextStartIndex="+nextStartIndex+",totalCnt="+totalCnt+",currentPage="+currentPage+",pageSize="+pageSize);
									if (totalCnt > nextStartIndex) {
										// console.log("继续获取下一页小区");
										handleGiscellLoadCommon(loadToken, url,
												postData, celllistkey,
												needRender);
									} else {
										onLoadingGisCell = false;
										hideOperTips("operInfo");
									}

								}
								// 如果没有获取完成，则继续获取
							} catch (err) {
								// //console.log("返回的数据有问题:" + err);
								if (loadToken == currentloadtoken) {
									onLoadingGisCell = false;// 终止
								}
								hideOperTips("operInfo");
							}
						},
						error : function(xmh, textstatus, e) {
							//alert("出错啦！" + textstatus);
							if (loadToken == currentloadtoken) {
								onLoadingGisCell = false;// 终止
							}
							hideOperTips("operInfo");
						},
						complete : function() {
							$(".loading_cover").css("display", "none");
						}
					});
}

// 仅仅加载小区到地图
function loadGisCellByConfig(loadToken, cellConfigId) {
	handleGiscellLoadCommon(loadToken,
			"getFreqReuseCellGisInfoFromSelAnaListForAjaxAction", {
				"configId" : cellConfigId
			}, 'gisCells', false);
}

/**
 * 根据所选择的分析列表加载小区数据并渲染颜色
 */
function loadGisCellWithInterData(loadToken, cellConfigId, interConfigId) {
//	console.log("进入loadGisCellWithInterData cellConfigId："+cellConfigId+" interConfigId:"+interConfigId);
	handleGiscellLoadCommon(loadToken, "getAnalysisGisCellByPageForAjaxAction",
			{
				"cellConfigId" : cellConfigId,
				"interConfigId" : interConfigId
			}, 'rnoAnalysisGisCells', true);
}

/**
 * 地图显示小区信息
 */
function showGisCellOnMap(gisCells) {
	// console.log("showGisCellOnMap. gisCells=" + gisCells);
	if (!gisCells) {
		return;
	}

	// 添加到地图
	gisCellDisplayLib.showGisCell(gisCells);
}

/**
 * 获取gis小区
 */
function getGisCell(loadToken) {
	$("#conditionForm")
			.ajaxSubmit(
					{
						url : 'getAnalysisGisCellByPageForAjaxAction',
						dataType : 'json',
						success : function(data) {
							//console.log("data:"+data);
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
								gisCellDisplayLib
										.showGisCell(obj['rnoAnalysisGisCells']);
								for ( var i = 0; i < obj['rnoAnalysisGisCells'].length; i++) {
									var cellData = obj['rnoAnalysisGisCells'][i];
									var op = createOptionFromStsResult(cellData);
									gisCellDisplayLib.changeCellPolygonOptions(
											cellData['cell'], op, false);
								}
								// changeCellPolygonOptions
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

									var forcedStartIndex = page['forcedStartIndex'] ? page['forcedStartIndex']
											: -1;
									if (forcedStartIndex == -1) {
										// console.log("forcedStartIndex==-1");
										forcedStartIndex = (currentPage - 1)
												* pageSize;
									}
									if (forcedStartIndex != -1) {
										forcedStartIndex = forcedStartIndex
												+ pageSize;
										// 有设置强制起点
										// 扩大pagesize，加速获取过程
										if (pageSize < 400) {
											pageSize = pageSize << 1;
											$("#hiddenPageSize").val(pageSize);
										}

									}
									$("#hiddenForcedStartIndex").val(
											forcedStartIndex);
									// console.log("forcedStartIndex==="
									// + $("#hiddenForcedStartIndex")
									// .val());
									// 下一次的起点
									// currentPage从1开始
									var nextStartIndex = forcedStartIndex == -1 ? ((currentPage - 1) * pageSize)
											: forcedStartIndex;
									// console.log("nextStartIndex="
									// + nextStartIndex + ",totalCnt="
									// + totalCnt + ",currentPage="
									// + currentPage + ",pageSize="
									// + pageSize);
									if (totalCnt > nextStartIndex) {
										// console.log("继续获取下一页小区:currentPage="
										// + currentPage + ",pageSize="
										// + pageSize
										// + ",forcedStartIndex="
										// + forcedStartIndex);
										getGisCell(loadToken);
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
 * 通过ncsid获取gis小区总干扰数据
 */
function getGisCellTotalInterData(loadToken,ncsDescId,curpage) {
//	console.log("进入getGisCellTotalInterData");
	var sendData = {
		"ncsId" : ncsDescId,
		"page['totalPageCnt']" : 0,
		"page['pageSize']" : 100,
		"page['currentPage']" : curpage,
		"page['totalCnt']" : 0,
		"page['forcedStartIndex']":-1
	};
	$.ajax(
					{
						url : 'getTotalAnalysisGisCellByPageWithNcsDescIdForAjaxAction',
						data:sendData,
						dataType : 'json',
						success : function(data) {
//							console.log("成功进来了");
//							console.log("data:"+data);
							if (loadToken != currentloadtoken) {
								// 新的加载开始了，这些旧的数据要丢弃
								// console.log("丢弃此次的加载结果。");
								return;
							}
//							console.log("data:"+data);
							var obj = data;
							try {
//								obj = eval("(" + data + ")");
								obj = data;
								// alert(obj['celllist']);
//								console.log(data);
								if(typeof(cellInterAndLnglats)=="undefined"){
//									console.log("if:"+cellInterAndLnglats);
//									console.log("if:obj['data']"+obj['data'].length);
									cellInterAndLnglats=obj['data'];
//									console.log("if:"+cellInterAndLnglats.length);
								}else{
//									console.log("else:obj['data']"+obj['data'].length);
									//console.log(typeof(cellInterAndLnglats));
									$.each(obj['data'],function(m,n){
										cellInterAndLnglats.push(n);
									});
//									console.log("else:"+cellInterAndLnglats.length);
								}
								for ( var i = 0; i < obj['data'].length; i++) {
									var cellData = obj['data'][i];
//									console.log("cell:"+cellData['CELL']);
									var op = createOptionFromStsResult(cellData);
//									console.log("op:"+op.fillColor);
									gisCellDisplayLib.changeCellPolygonOptions(
											cellData['CELL'], op, false);
									if(i==0){
										//gisCellDisplayLib.panToCell(cellData['CELL']);
									}
								}
								// changeCellPolygonOptions
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

									var forcedStartIndex = page['forcedStartIndex'] ? page['forcedStartIndex']
											: -1;
									if (forcedStartIndex == -1) {
										// console.log("forcedStartIndex==-1");
										forcedStartIndex = (currentPage - 1)
												* pageSize;
									}
//									console.log("if前forcedStartIndex:"+forcedStartIndex);
									if (forcedStartIndex != -1) {
										/*forcedStartIndex = forcedStartIndex
												+ pageSize;*/
											forcedStartIndex = forcedStartIndex
												+ 1;
//										console.log("if后forcedStartIndex:"+forcedStartIndex);
										// 有设置强制起点
										// 扩大pagesize，加速获取过程
//										console.log("前pageSize:"+pageSize);	
										if (pageSize < 400) {
											//pageSize = pageSize << 1;
											$("#hiddenPageSize").val(pageSize);
										}
//										console.log("后pageSize:"+pageSize);		
									}
									$("#hiddenForcedStartIndex").val(
											forcedStartIndex);
//									console.log("forcedStartIndex:"+forcedStartIndex);
									// console.log("forcedStartIndex==="
									// + $("#hiddenForcedStartIndex")
									// .val());
									// 下一次的起点
									// currentPage从1开始
//									console.log("currentPage:"+currentPage);
									var nextStartIndex = forcedStartIndex == -1 ? ((currentPage - 1) * pageSize)
											: forcedStartIndex;
									// console.log("nextStartIndex="
									// + nextStartIndex + ",totalCnt="
									// + totalCnt + ",currentPage="
									// + currentPage + ",pageSize="
									// + pageSize);
//									console.log("totalCnt:"+totalCnt+"-----nextStartIndex:"+nextStartIndex);
									if (totalCnt > nextStartIndex) {
										// console.log("继续获取下一页小区:currentPage="
										// + currentPage + ",pageSize="
										// + pageSize
										// + ",forcedStartIndex="
										// + forcedStartIndex);
//										getGisCell(loadToken);
//										console.log("curpage:"+currentPage);
										getGisCellTotalInterData(loadToken,ncsDescId,currentPage);
									}
								}
								// 如果没有获取完成，则继续获取
							} catch (err) {
								console.log("返回的数据有问题:" + err);
							}
						},
						error : function(xmh, textstatus, e) {
							alert("出错啦！" + textstatus);
						},
						complete : function() {
							//是否完成全部干扰分析
							isCompletedTotalInter=true;
							//console.log("cellInterAndLnglats['CELL']:"+cellInterAndLnglats[0]['CELL']);
							// LNGLATS:baidu googleearth
							var data=cellInterAndLnglats[0]['LNGLATS'];
							var dataobj=eval("("+data+")");
							var lnglats;
							if(map instanceof BMap.Map){
								lnglats=dataobj.baidu;
							}else{
								lnglats=dataobj.googleearth;
							}
							var lnglatsarr=lnglats.split(";");
							var lnglatarr=lnglatsarr[0].split(",");
							gisCellDisplayLib.panTo(Number(lnglatarr[0]),Number(lnglatarr[1]));
							$(".loading_cover").css("display", "none");
						}
					});
}
/**
 * 根据统计数值和渲染规则，得到用于地图显示的外观选项
 * 
 * @param cellData
 */
function createOptionFromStsResult(cellData) {

	if (currentRule != null) {
		var option = null;
		option = new Object();
		option.fillOpacity = 0;
		//sumcica-->CELLINTEREDCOEFFISUM
		if (!cellData['CELLINTEREDCOEFFISUM']) {
			cellData['CELLINTEREDCOEFFISUM'] = 0;
		}
		var ruleSetting = currentRule.findRuleSetting(cellData['CELLINTEREDCOEFFISUM']);
		if (ruleSetting != null) {
			option.fillColor = ruleSetting.style.color;
			return option;
		}
		return null;
	}
}

/**
 * 获取topn 干扰小区
 */
/*function getRnoGisCellInAreaTop() {

	for ( var i = 0; i < topNLabels.length; i++) {
		gisCellDisplayLib.removeOverlay(topNLabels[i]);
	}
	topNLabels.splice(0, topNLabels.length);

	var rank = $("#topNum").val();
	var areaId = $("#areaId").val();
	$.ajax({
		url : 'getRnoGisCellInAreaTopNAction',
		data : {
			rank : rank,
			areaId : areaId,
			"cellConfigId" : currentSelConfigId,
			"interConfigId" : currentSelInterConfigId
		},
		dataType : 'json',
		type : 'post',
		async : false,
		success : function(data) {
			$.each(data, function(k, v) {
				if (v.allLngLats) {
//					var lngLats = v.allLngLats.split(";");
					//改造
					var lnltlists=v.allLngLats;
					var interval;
					if(map instanceof BMap.Map){
					var dt=eval("("+lnltlists+")");
					interval=dt.baidu;
					}else{
					var dt=eval("("+lnltlists+")");
					interval=dt.googleearth;
					}
					var lngLats = interval.split(";");
					if (lngLats) {
						var lnglat = lngLats[2].split(",");
//						var point = new BMap.Point(lnglat[0], lnglat[1]);
						//console.log("lnglat lng:"+lnglat[0]+"---- lat:"+lnglat[1]);
						var point = gisCellDisplayLib.createPoint(lnglat[0], lnglat[1]);
						var label = new BMap.Label(v.rank + "： "
								+ v.chineseName + "(" + v.cell + ")", {
							'position' : point,
							'offset' : new BMap.Size(2, -20)
						});
						//改造label控件
						var con=v.rank + "： "+ v.chineseName + "(" + v.cell + ")";
//						console.log("con:"+con);
						var label =new IsTextLabel(lnglat[0], lnglat[1], con,{
							'position' : point,
							'offset' : gisCellDisplayLib.getOffsetSize(2, -20)},
							{
								'mousedownFunction':mouseDownTextLabel,
								'mouseupFunction':mouseUpTextLabel
							},gisCellDisplayLib);
						if(map instanceof BMap.Map){
						label.setStyle({ "color" : "black", "fontSize" : "12px","backgroundColor":"#FFFFFF" ,"borderColor":"#FF0000","border":  "solid 1px","cursor":"pointer"});
						topNLabels.push(label);
						gisCellDisplayLib.addOverlay(label);
						label.addEventListener("contextmenu",function(e){
//							console.log("contextmenu:"+label+" e:"+e);
							try{
								gisCellDisplayLib.removeOverlay(label);
//								$(this).attr("display","none");
								animateInAndOut("operInfo", 200, 200, 500, "operTip", $(this).text()+"删除文本标注成功！");
							}catch(e){
								animateInAndOut("operInfo", 200, 200, 500, "operTip", $(this).text()+"删除文本标注失败！"+e);
							}
						});
						}
						label.addEventListener("mousedown",function(e){
							    var mousex = 0, mousey = 0;
	       						var divLeft, divTop;
								console.log("mousedown");
								console.log("label:"+this);
								var offset = $(this).offset();
			                    divLeft = parseInt(offset.left,10);
			                    divTop = parseInt(offset.top,10);
			                    mousey = e.pageY;
			                    mousex = e.pageX;
			                    $(this).bind('mousemove',function(event){
			                    	 var left = divLeft + (event.pageX - mousex);
				                    var top = divTop + (event.pageY - mousey);
				                    console.log("left:"+left+" top:"+top);
				                    $(this).css(
				                    {
				                        'top' :  top + 'px',
				                        'left' : left + 'px',
				                        'position' : 'absolute'
				                    });
			                    });
						});
						
						label.addEventListener("mouseup",function(){
								console.log("mouseup");
								console.log("label:"+label);
								$(this).unbind('mousemove');
//								map.addOverlay(this);
						});
					}
				}
			});
		}
	});

}*/
/**
 * 获取topn 干扰小区
 */
function getRnoGisCellInAreaTop(cellInter) {

	for ( var i = 0; i < topNLabels.length; i++) {
		gisCellDisplayLib.removeOverlay(topNLabels[i]);
	}
	topNLabels.splice(0, topNLabels.length);
	var rank = $("#topNum").val();
//	console.log(rank);
			$.each(cellInter, function(k, v) {
				if(k<rank){
				if (v.LNGLATS) {
//					var lngLats = v.allLngLats.split(";");
					//改造
					var lnltlists=v.LNGLATS;
					var interval;
					if(map instanceof BMap.Map){
					var dt=eval("("+lnltlists+")");
					interval=dt.baidu;
					}else{
					var dt=eval("("+lnltlists+")");
					interval=dt.googleearth;
					}
					var lngLats = interval.split(";");
					if (lngLats) {
						var lnglat = lngLats[2].split(",");
//						var point = new BMap.Point(lnglat[0], lnglat[1]);
						//console.log("lnglat lng:"+lnglat[0]+"---- lat:"+lnglat[1]);
						var point = gisCellDisplayLib.createPoint(lnglat[0], lnglat[1]);
						var label = new BMap.Label(v.row_number + "： "
								+ v.CELL+"--"+v.CELLINTEREDCOEFFISUM, {
							'position' : point,
							'offset' : new BMap.Size(2, -20)
						});
						//改造label控件
						var con=v.row_number + "： "+ v.CELL+"--"+v.CELLINTEREDCOEFFISUM ;
//						console.log("con:"+con);
						var label =new IsTextLabel(lnglat[0], lnglat[1], con,{
							'position' : point,
							'offset' : gisCellDisplayLib.getOffsetSize(2, -20)},
							{
								'mousedownFunction':mouseDownTextLabel,
								'mouseupFunction':mouseUpTextLabel
							},gisCellDisplayLib);
						if(map instanceof BMap.Map){
						label.setStyle({ "color" : "black", "fontSize" : "12px","backgroundColor":"#FFFFFF" ,"borderColor":"#FF0000","border":  "solid 1px","cursor":"pointer"});
						topNLabels.push(label);
						gisCellDisplayLib.addOverlay(label);
						label.addEventListener("contextmenu",function(e){
//							console.log("contextmenu:"+label+" e:"+e);
							try{
								gisCellDisplayLib.removeOverlay(label);
//								$(this).attr("display","none");
								animateInAndOut("operInfo", 200, 200, 500, "operTip", $(this).text()+"删除文本标注成功！");
							}catch(e){
								animateInAndOut("operInfo", 200, 200, 500, "operTip", $(this).text()+"删除文本标注失败！"+e);
							}
						});
						}
						
					}
				}
				}
			});
			
	

}
function getCellInterferenceAnalysis(label) {
	if (label == null || label == '') {
		alert("请选择一个小区");
		return;
	}
	var areaId = $("#areaId").val();
	$
			.ajax({
				url : 'getCellInterferenceAnalysisAction',
				data : {
					label : label,
					areaId : areaId,
					'cellConfigId' : currentSelConfigId,
					'interConfigId' : currentSelInterConfigId
				},
				dataType : 'json',
				type : 'post',
				async : false,
				success : function(data) {
					$("#celllabel").text(label);
					$("#interference").text("");
					$("#avg").text("");
					$("#cellRank").text("");
					$("#interference").text("");
					$("#rno_interference").html("");
					$("#rno_interference_cell").html("");
					try{
//						console.log("data['gisCell']:"+data['gisCell']);
					if (data && data['gisCell']) {
//						console.log("data[giscell]=" + data['gisCell']);
						$("#celllabel").text(data['gisCell'].cell);
						$("#interference").text("未知");
						$("#avg").text("未知");
						$("#cellRank").text("未知");
						$("#interference").text(
								getValidValue(data['gisCell'].sumcica, "未知"));
						//console.log("data['gisCell'].sumcica:"+data['gisCell'].sumcica);//undefined
						if (data['gisCell'].sumcica != undefined
								&& data['gisCell'].allsum != undefined) {
							//console.log("allsum=" + data['gisCell'].allsum);
							var avg = parseFloat(data['gisCell'].allsum)
									/ parseFloat(data['gisCell'].tote);
							// if (parseFloat(data['gisCell'].sumcica) > avg) {
							$("#avg")
									.text(
											"高出均值"
													+ ((parseFloat(data['gisCell'].sumcica) - avg)
															/ avg * 100)
															.toFixed(2) + "%");
							// }
							
							if (data['interferenceTCHs']) {
								//console.log("tch==" + data['interferenceTCHs']);
								var context = "";
								$.each(data['interferenceTCHs'],
										function(k, v) {
											context = context + v + ",";
										});
								if (context != null && context != "") {
									context = context.substring(0,
											context.length - 1);
								} else {

								}
								$("#rno_interference").text(context);
							} else {
								$("#rno_interference").text("未知");
							}
							if (data['interferenceCellByLabel']) {
								var context = "";
								var nci = 0;
								$
										.each(
												data['interferenceCellByLabel'],
												function(k, v) {
													nci++;
													context = context
															+ "<a onclick='getCellInterferenceAnalysis(\""
															+ v.cell
															+ "\")'>"
															+ v.cell
															+ "</a> &nbsp;&nbsp;";
													if (nci % 5 == 0) {
														context += "<br/>";
													}
												});
								// if (context != null && context != "") {
								// context = context.substring(0,
								// context.length - 1);
								// }
								$("#rno_interference_cell").html(context);
							} else {
								$("#rno_interference_cell").html("");
							}

							$("#cellRank").text(
									"第" + data['gisCell'].rank + "/"
											+ data['gisCell'].tote);
						}else{
//							console.log("data['interferenceTCHs'].length:"+data['interferenceTCHs'].length);
							if (data['interferenceTCHs'].length==0) {
								$("#rno_interference").text("未知");
							}
							if (data['interferenceCellByLabel'].length==0) {
								$("#rno_interference_cell").text("未知");
							}
						} 
						
					} else {
						// console.log("no data");
						$("#interference").text("无相关干扰数据！");
						
					}
					$("#interference_dialogId").show();
					}catch(err){
						animateInAndOut("operInfo", 200, 200, 500, "operTip", "分析出错！");
					}
				}
			});

}
/**
 * 获取指定小区的干扰分析数据－小区干扰分析
 * @param {} label
 */
function getSpecifyCellInterferenceAnalysis(label,selInterConfigId) {
	if (label == null || label == '') {
		alert("请选择一个小区");
		return;
	}
	var areaId = $("#areaId").val();
	$
			.ajax({
				url : 'getSpecifyCellInterferenceAnalysisAction',
				data : {
					cell : label,
					selectId:selInterConfigId
				},
				dataType : 'json',
				type : 'post',
				//async : false,
				success : function(data) {
					
					try{
//						console.log(data);
						$.each(data,function(k,v){
//							console.log(k+":"+v.adjFreqInterDetaList);
							var adjinter=v.adjFreqInterDetaList;
							
							var sumcellInteredInto=0;
							var sumcellInterOut=0;
							var sumtotalInter=0;
							var cellFreqObj=new Array();
							$.each(adjinter,function(k1,v1){
								
								
								var ncell=v1.ncell;
								var ncellTch=v1.ncellTch;
								var cellInteredInto=Number(v1.cellInteredInto);
								var cellInterOut=Number(v1.cellInterOut);
								var totalInter=Number(v1.totalInter);
								var distance=v1.distance;
//								console.log(k+"-------"+v1.ncell+":"+v1.ncellTch+":"+v1.cellInteredInto+":"+v1.cellInterOut+":"+v1.totalInter+":"+v1.distance);
								//1.首先汇总　全部
//								console.log(cellInteredInto);
								sumcellInteredInto+=cellInteredInto;
								sumcellInterOut+=cellInterOut;
								sumtotalInter+=totalInter;
								//２.其次将单独频点对应的同频与邻频数据储存起来
								var cellFreq=new Object();
//								cellFreq="{\"ncell\":\""+ncell+"\",\"ncellTch\":"+ncellTch+",\"cellInteredInto\":"+cellInteredInto+",\"cellInterOut\":"+cellInterOut+",\"totalInter\":"+totalInter+",\"distance\":"+distance+"}";
								cellFreq={"ncell":ncell,"ncellTch":ncellTch,"cellInteredInto":cellInteredInto,"cellInterOut":cellInterOut,"totalInter":totalInter,"distance":distance};
								
								cellFreqObj.push(cellFreq);
							});
//							var obj = eval("(" + cellFreqObj.substring(0,cellFreqObj.length-1) + ")");
							var obj = cellFreqObj;//.substring(0,cellFreqObj.length-1) ;
							
//							cellFreqToNcellInter={objkey:obj};
//							str = "cellFreqToNcellInter."+k+"='"+obj+"'";
							cellFreqToNcellInter[k]=obj;
//							eval(str);
//							console.log(cellFreqToNcellInter);
//							console.log("sumcellInteredInto:"+Number(sumcellInteredInto));
							//onmouseover=\"this.style.backgroundColor='#e7e7c7'\" onMouseOut=\"this.style.backgroundColor='#f6f6f6'\"
							var tr="<tr onclick=\"getCellFreqInterDetailed(this);changeActiveRow(this);\" >"
									+"<td align=\"center\">"
									+k
									+"</td>"
									+"<td align=\"center\">"
									+sumcellInteredInto.toFixed(4)	
									+"</td>"
									+"<td align=\"center\">"
									+sumcellInterOut.toFixed(4)			
									+"</td>"
									+"<td align=\"center\">"
									+sumtotalInter.toFixed(4)			
									+"</td>"
								+"</tr>";
								$("#freqintersituation").append(tr);
						});
					$("#interference_dialogId").show();
					}catch(err){
						animateInAndOut("operInfo", 200, 200, 500, "operTip", "分析出错！");
					}
				}
			});

}
/**
 * 通过传入行对象获取小区频点为KEY的数据获取其下的集合对象
 * @param {} obj
 */
/*function getCellFreqInterDetailed(obj){
	$("#detailedinterdiv").show();
	
	//先清空除了前两行表头
	$("#detailedintersituation tr:gt(0)").remove();
	var cellFreq=$(obj).find("td").eq(0).text();
	$("#cellfreqlabel").text(cellFreq);
//	console.log(cellFreq);
	for(var key in cellFreqToNcellInter){
//								console.log("进入第一个循环："+key);
//								console.log(cellFreqToNcellInter[key]);
								if(cellFreq==key){
//									console.log("key:"+key);
								var ooo=cellFreqToNcellInter[cellFreq];
								$.each(ooo,function(k,v1){
//									var aa=v.substring(0,v.length-1);
//									var obj=eval("(" + aa + ")");
									//jQuery.parseJSON();
									var ncell=v1.ncell;
									var ncellTch=v1.ncellTch;
									var cellInteredInto=v1.cellInteredInto;
									var cellInterOut=v1.cellInterOut;
									var totalInter=v1.totalInter;
									var distance=v1.distance;
//									$("#detailedintersituation").html();
//									console.log(v.ncell);
									var tr="<tr>"
									+"<td align=\"center\">"
									+ncell
									+"</td>"
									+"<td align=\"center\">"
									+ncellTch	
									+"</td>"
									+"<td align=\"center\">"
									+cellInteredInto		
									+"</td>"
									+"<td align=\"center\">"
									+cellInterOut		
									+"</td>"
									+"<td align=\"center\">"
									+totalInter			
									+"</td>"
									+"<td align=\"center\">"
									+distance		
									+"</td>"
								+"</tr>";
								$("#detailedintersituation").append(tr);
								})
								}
								
	}
}*/
//greystyle-standard-red
//EvenOrOddRow


// --------------//
// 点击事件的响应函数
/*function clickFunction(polygon, event) {
	// alert("click 。 "+polygon._data.getCell());
	currentActiveRow="";
	if (!polygon) {
		return;
	}
	var cmk=gisCellDisplayLib.getComposeMarkerByShape(polygon);
	var cell = cmk.getCell();
	celllabel = cell;
	$("#cellname").text(celllabel);
//	console.log("celllabel:"+celllabel);
//	console.log("curstate:"+curstate);
	if (curstate == STATE_WAIT) {
		showOperTips("operInfo", "operTip", "正在对小区进行分析");
		curstate = STATE_DOING;
		gisCellDisplayLib.setDefaultCursor(defaultCursor);
//		getCellInterferenceAnalysis(celllabel);
		getSpecifyCellInterferenceAnalysis(celllabel,currentSelInterConfigId);
		translateToIdle();
		animateInAndOut("operInfo", 200, 200, 500, "operTip", "分析完成");
	}
}*/

function clearInterDetails() {
	cellFreqToNcellInter = null;// 清空对象
	cellFreqToNcellInter = {};
	//清空频点编辑框的数据
	$("#cellBcch").val("");
	$("#cellTch").val("");
	//隐藏频点编辑框
	$("#freq_dialogId").hide();
	//清空干扰详情列表
	$("#listdistab tr:gt(0)").remove();
	//清除干扰详情列表的标题
	$("span#interDetailTitle").html("小区〖〗的 干扰情况");
	//清空小区频点干扰详情
	$("#freqintersituation tr:gt(0)").remove();
	//清空频点对应的邻区干扰详情
	$("#detailedintersituation tr:gt(0)").remove();
	//隐藏频点干扰详情显示窗
	$("#interference_dialogId").hide();
	//恢复地图默认图元外观
	gisCellDisplayLib.clearOnlyExtraOverlay();//清除额外覆盖物
	gisCellDisplayLib.resetPolygonToDefaultOutlook();//恢复默认polygon颜色
}
/**
 * 查看noise
 */
function showNoiseFunction(polygon, cell) {
	// alert("click 。 "+polygon._data.getCell());
	
	if (!polygon) {
		return;
	}
	//清空界面数据
	clearInterDetails();
	//$("#listdisplay").hide();//隐藏列表展示
	//$("#listdisplaycell").text("");//清空
	//先清空列表展示表的前两行表头
	//$("#listdistab tr:gt(1)").remove();
	
	if(cell) {
		//console.log(cell);
		celllabel = cell;
	} else {
		var cmk = gisCellDisplayLib.getComposeMarkerByShape(polygon);
		celllabel = cmk.getCell();
	}

//	console.log("celllabel:"+celllabel);
	//$("#cellname").text(celllabel);
	getCellInterferMatrixData(celllabel);
}

/**
 * 查看In干扰
 */
function showInterCellFromIn(polygon, cell) {
	if (!polygon) {
		return;
	}
	//清空界面数据
	clearInterDetails();
	//获取cell
	if(cell) {
		//console.log(cell);
		celllabel = cell;
	} else {
		var cmk = gisCellDisplayLib.getComposeMarkerByShape(polygon);
		celllabel = cmk.getCell();
	}
	displayInOrOutInterDetail(celllabel,"IN");
}
/**
 * 查看Out干扰
 */
function showInterCellFromOut(polygon, cell) {
	if (!polygon) {
		return;
	}
	//清空界面数据
	clearInterDetails();
	//获取cell
	if(cell) {
		//console.log(cell);
		celllabel = cell;
	} else {
		var cmk = gisCellDisplayLib.getComposeMarkerByShape(polygon);
		celllabel = cmk.getCell();
	}
	
	displayInOrOutInterDetail(celllabel,"OUT");
}
/**
 * 改频点
 */
function showCellFreqWindow(polygon, cell) {
	if (!polygon) {
		return;
	}

	if(!cell) {
		var cmk = gisCellDisplayLib.getComposeMarkerByShape(polygon);
		cell = cmk.getCell();
	}
	//清空界面数据
	clearInterDetails();
	//页面保存cell，在更新的时候调用
	$("#freqCellName").val(cell);
	
	showOperTips("loadingDataDiv", "loadContentId", "正在处理数据");
	
	$.ajax({
		url : 'getCellFreqByCellNameForAjaxAction',
		data : {
			'cell' : cell
		},
		dataType : 'json',
		type : 'post',
		success : function(data) {
			if(typeof(data[0])==='undefined'){
				alert("找不到小区对应的tch与bcch");
			}else{
				bcchCache = data[0]["BCCH"];
				tchCache = data[0]["TCH"];
				$("#cellBcch").val(bcchCache);
				$("#cellTch").val(tchCache);
			}
		},
		error : function(xhr, textstatus, e) {
			//出错隐藏编辑窗口
			$("#freq_dialogId").hide();
			hideOperTips("loadingDataDiv");
		},
		complete : function() {
			//$(".loading_cover").css("display", "none");
			hideOperTips("loadingDataDiv");
			$("#freq_dialogId").show();
		}
	});
}
/**
 * 更新频点
 */
function updateCellFreq() {
	var bcch = $("#cellBcch").val();
	var tch = $("#cellTch").val();
	var tchArray = tch.split(",");
	//标记
	var flag = true;
	
	//判断是否存在数字与逗号以外的字符
	var regBcch = /^[0-9]+$/;
	var regTch = /^[0-9,]+$/;
	if(!regBcch.test(bcch)) {
		flag = false;
		alert("bcch不能输入数字以外的字符！");
		return;
	}
	if(!regTch.test(tch)) {
		flag = false;
		alert("tch不能输入数字与逗号以外的字符！");
		return;
	}
	
	//判断是否为空
	if(tch == "" || bcch == "") {
		flag = false;
		alert("tch与bcch不能为空！");
		return;
	}
	//判断bcch是否在tch里面
	for(var i = 0; i < tchArray.length; i++) {
		if(tchArray[i] == bcch) {
			flag = false;
			alert("bcch的频点不能出现在tch里面！");
			return;
		}
	}
	//判断tch的频点是否存在同频，邻频
	for(var i = 0; i < tchArray.length; i++) {
		var temp = tchArray[i];
		var tempL = Number(temp) - 1;
		var tempU = Number(temp) + 1;
		for(var j = i + 1; j < tchArray.length; j++) {
			if(tchArray[j] == temp 
				|| tchArray[j] == tempL
				|| tchArray[j] == tempU) {
				flag = false;
				alert("频点{"+temp+"}在tch里面存在同频，邻频关系！");
				return;
			}
		}
	}
	//判断是否符合GSM类型
	var is900;
	if(bcchCache >= 1 && bcchCache <= 94) {
		is900 = true;
	} 
	if(bcchCache >= 512 && bcchCache <= 635) {
		is900 = false;
	}
	if(is900) {
		if(bcch < 1 || bcch >94) {
			flag = false;
			alert("bcch的频点不符合GSM900要求！");
			return;
		}
		for(var i = 0; i < tchArray.length; i++) {
			if(tchArray[i] < 1 || tchArray[i] >94) {
				flag = false;
				alert("tch的频点不符合GSM900要求！");
				return;
			}
		}
	} else {
		if(bcch < 512 || bcch > 635) {
			flag = false;
			alert("bcch的频点不符合GSM1800要求！");
			return;
		}
		for(var i = 0; i < tchArray.length; i++) {
			if(tchArray[i] < 512 || tchArray[i] > 635) {
				flag = false;
				alert("tch的频点不符合GSM1800要求！");
				return;
			}
		}
	}
	
	var cell = $("#freqCellName").val();
	var flagFromServer = false; // 默认更新失败
	
	if(flag) {
		$.ajax({
			url : 'updateCellFreqThroughCellNameForAjaxAction',
			data : {
				'cell' : cell,
				'bcchStr' : bcch,
				'tchStr' : tch
			},
			dataType : 'json',
			type : 'post',
			success : function(data) {
				flagFromServer = data['flag']; 
			},
			error : function(xhr, textstatus, e) {
				//出错隐藏编辑窗口
				$("#freq_dialogId").hide();
				hideOperTips("loadingDataDiv");
			},
			complete : function() {
				//$(".loading_cover").css("display", "none");
				hideOperTips("loadingDataDiv");
				$("#freq_dialogId").show();
				if(flagFromServer){
					alert("频点更新成功！");
				}else{
					alert("频点更新失败！");
				}
			}
		});
	} else {
		alert("bcch或者tch输入有误，请重新输入提交！");
	}
	
}
/**
 * 显示干扰信息
 */
function displayInOrOutInterDetail(cell, inOrOut) {
	var cell1 = cell;
	var flag = true; //用于状态提示
	$("span#loadingStatus").html("  加载小区干扰记录中...");
	showOperTips("loadingDataDiv", "loadContentId", "正在处理数据");
	//var taskId = $("#latelyTaskId").val();
	var MartixDescId = $("#latelyMartixDescId").val();
	$.ajax({
		url : 'getCellInterferMatrixDataDetailForAjaxAction',
		data : {
			'cell' : cell1,
			'MartixDescId' : MartixDescId
		},
		dataType : 'json',
		type : 'post',
		success : function(data) {
			if(data==null){
//				console.log("未找到小区：" + cellname + "的干扰记录");
				flag = false;
				if(cell1) {
					$("span#loadingStatus").html("未找到小区" + cell1 + "的干扰记录！");
					$("span#interDetailTitle").html("小区〖"+cell1+"〗的 "+inOrOut+"干扰情况");
				}
				//animateInAndOut("operInfo", 200, 200, 500, "operTip", "未找到小区：" + cellname + "的干扰记录!");
			}else{
				try{
					    var cellName = data.cellName;
					    $("#cellname").text(cellName);
					    var freqInterInfos = data.freqInterInfos;
					    var cellAbsts = data.cellAbsts;
					    if(freqInterInfos.length <= 0 || cellAbsts.length <= 0) {
					     	flag = false;
							$("span#loadingStatus").html("未找到小区" + cellname + "的干扰记录！");
							$("span#interDetailTitle").html("小区〖"+cellName+"〗的 "+inOrOut+"干扰情况");
					    } else {
						    //以下目的将详情数据存储起来－－－－－－开始
								var cellFreqObj=new Array();
							    //将单独频点对应的同频与邻频数据储存起来
								//cellFreqToNcellInter={"freqInterInfos":freqInterInfos,"cellAbsts":cellAbsts};
								cellFreqToNcellInter={"cellName":cellName,"freqInterInfos":freqInterInfos,"cellAbsts":cellAbsts};
							//以上目的将详情数据存储起来－－－－－－结束

							//额外在地图上加载需要显示的关联cell,以防连线时出现不存在cell
							var cellObj = new Object();
							$.each(freqInterInfos,function(k,v){
									var inter;
									if(inOrOut == "IN") {
										inter = v['in'];
									} else {
										inter = v['out'];
									}
									if(inter.length != 0){
										$.each(inter,function(k,v1){
											var ncellIdx = v1.ncellIdx;
											var ncell = cellAbsts[ncellIdx].cellName;
											//对应邻区
											cellObj[ncell] = 0;
										});
									}
							});
							var cells = "";
							for(var cell in cellObj) {
								cells += "'" + cell + "'" + ",";
							}
							cells = cells.substr(0, cells.length - 1);
							//先补充当前区域地图需要显示但尚未加载的小区
							var cityId = $("#cityId").val();
							mapGridLib.loadRelaCellByLabelsOnMap(cells,cityId,function(){
								//在地图体现干扰关系
								displayInterRelation(cellName,inOrOut,data);	
							});
							
							//displayListInterInfo(cellName,data);	
					    }

				}catch(err){
					//animateInAndOut("operInfo", 200, 200, 500, "operTip", "分析出错！");
				}
			}
		},
		error : function(xhr, textstatus, e) {
		},
		complete : function() {
			//$(".loading_cover").css("display", "none");
			hideOperTips("loadingDataDiv");
			if(flag) {
				$("span#loadingStatus").html("小区干扰记录加载完成");
			}
		}
	});
}
/**
 *  列表展示小区inOrOut干扰情况,将干扰的小区在地图用颜色线连接
 * @param cellName
 *            
 * @param inOrOut
 *            
 * @param obj
 *
 */
function displayInterRelation(cellName,inOrOut,obj) {
	
	$("span#interDetailTitle").html("小区〖"+cellName+"〗的 "+inOrOut+"干扰情况");
	
	var cellName = obj.cellName;
	var freqInterInfos = obj.freqInterInfos;
	var cellAbsts = obj.cellAbsts;
	//console.log(cellName);
	//为小区到列表干扰值存储作好准备
	var sameFreqVal = new Object();
	var AdjFreqVal = new Object();
	//是否邻区
	var isNeighbour = new Object();
	//对应InOrOut的邻区
	var ncells = new Object();
	
	$.each(cellAbsts,function(k,v){
		var cell = v.cellName;
		var isNei = v.isNei;
		//列表展示
		sameFreqVal[cell] = 0; //同频干扰值
		AdjFreqVal[cell] = 0; //邻频干扰值
		//邻区关系
		isNeighbour[cell] = isNei;
		//同频，上邻频，下邻频
		//{"cellName":[{"uca":"uca","lca":lca,"ci":ci}]},创建空数组
		//cellToSameOrAdjFreqInterVal[cell]=[];
	});
	
	$.each(freqInterInfos,function(k,v){
		//console.log(k+":"+v.freq);
			var inter;
			if(inOrOut == "IN") {
				inter = v['in'];
			} else {
				inter = v['out'];
			}
			//console.log("inter:"+inter);
			if(inter.length != 0){
				//console.log("typeof(inter_in):"+typeof(inter_in));
				$.each(inter,function(k,v1){
	
					var ncellIdx = v1.ncellIdx;
					//console.log("ncellIdx:"+ncellIdx);
					var ncell = cellAbsts[ncellIdx].cellName;
					var value = v1.value;
					var ciOrCa = v1.ciOrCa;
					
					//对应邻区
					ncells[ncell] = 0;
					
					//取出上次存储的干扰值进行累加
					if(ciOrCa=="ci") {
						sameFreqVal[ncell] = Number(sameFreqVal[ncell]) + Number(value);
					} else {
						AdjFreqVal[ncell] = Number(AdjFreqVal[ncell]) + Number(value);
					}
				});
			}
	});
	
	var listtr="";	
	for(var key in ncells){
	
			var cell = key;
			var oneSameFreqVal = sameFreqVal[cell];
			var oneAdjFreqVal = AdjFreqVal[cell];
			var oneIsNei = (isNeighbour[cell]==0?"NO":"YES");
			//同频+邻频
			var inter_val = Number(oneSameFreqVal) + Number(oneAdjFreqVal);
				
			//线条样式
			var option_line={};
			if(inter_val < 0.05){
				option_line={'strokeColor':'#0080FF',"strokeWeight":1};
			}else{
				//console.log("同频："+oneSameFreqVal+",邻频："+oneAdjFreqVal+",cell:"+cellName+",ncell:"+cell);
				option_line={'strokeColor':'#AE0000',"strokeWeight":1};
			}

			//连线渲染
			gisCellDisplayLib.drawLineBetweenCells(cellName,cell,option_line);

			//累加tr
			listtr+="<tr>"
					    +"<td style=\"border:1px solid #ccc;\">"+cell+"</td>"
					    +"<td style=\"border:1px solid #ccc;\">"+parseFloat(Number(oneSameFreqVal).toFixed(4))+"</td>"
					    +"<td style=\"border:1px solid #ccc;\">"+parseFloat(Number(oneAdjFreqVal).toFixed(4))+"</td>"
					    +"<td style=\"border:1px solid #ccc;\">"+oneIsNei+"</td>"
					  +"</tr>";
	}	
	$("#listdistab").append(listtr);
	new TableSorter("listdistab"); //为table加上点击排序
}


var titleLabels = new Array();
//var offsetSize = new BMap.Size(3, 3);
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
 * 拖动IsTextLabel标签移动
 * @param {} obj
 * @param {} event
 */
					
function mouseDownTextLabel(obj,event){
//	console.log("obj:"+$(obj).text());
//console.log("进入mouseDownTextLabel"+obj+" event:"+event);
	                $(obj).mousedown(function(e)
	                {
	                	//console.log("进入mousedown"+obj+" event:"+event+" this:"+this);
	                    var offset = $(this).offset();
	                    divLeft = parseInt(offset.left,10);
	                    divTop = parseInt(offset.top,10);
	                    mousey = e.pageY;
	                    mousex = e.pageX;
	                    $(this).bind('mousemove',dragElement);
	                   
	                });
	         //console.log("退出mouseDownTextLabel");        
}
 function dragElement(event)
	                {
	                	//console.log("进入dragElement"+event);
	                    var left = divLeft + (event.pageX - mousex);
	                    var top = divTop + (event.pageY - mousey);
//	                    console.log("left:"+left+" top:"+top);
	                    $(this).css(
	                    {
	                        'top' :  top + 'px',
	                        'left' : left + 'px',
	                        'position' : 'absolute'
	                    });
	                    //console.log("退出dragElement");
	                    return false;
	                }
function mouseUpTextLabel(obj,event){
	//console.log("进入mouseUpTextLabel"+obj+" event:"+event);
	 $(obj).unbind('mousemove');
	 //console.log("退出mouseUpTextLabel"+event);
}
/**
 * 获取gis小区
 */
function getGisCellByFreqType(loadToken) {
//	console.log("loadToken:"+loadToken);
　　　//cobsic干扰查询-chao.xj
	 onLoadingGisCell=true;
	//$(".loading_cover").css("display", "block");
	showOperTips("loadingDataDiv", "loadContentId", "正在加载小区");
	//var gsmtype=$("#freqType option:checked").val();
	var gsmtype=$("input[name='freqType']:checked").val();
	//console.log(gsmtype);
	var sendData={
		"freqType":gsmtype
	};
	$("#conditionForm").ajaxSubmit(
			{
				url : 'getGisCellByPageForAjaxAction',
				data: sendData,
				dataType : 'json',
				success : function(data) {
					if (loadToken != currentloadtoken) {
						// 新的加载开始了，这些旧的数据要丢弃
//						console.log("丢弃此次的加载结果。");
						return;
					}
					// console.log(data);
					var obj = data;
					try {
						// obj = eval("(" + data + ")");
						// alert(obj['celllist']);
						// showGisCell(obj['gisCells']);
						/*for(var i=0;i<obj['gisCells'].length;i++){
							if(obj['gisCells'][i].cell=='test10' || obj['gisCells'][i].cell=='test11'){
								console.log(obj['gisCells'][i].cell);
							}
						}*/
						gisCellDisplayLib.showGisCell(obj['gisCells']);
						var page = obj['page'];
						if (page) {
							var pageSize = page['pageSize'] ? page['pageSize']
									: 0;
							$("#hiddenPageSize").val(pageSize);
							
							var currentPage = new Number(
									page['currentPage'] ? page['currentPage']
											: 1);
//							console.log("currentPage:"+currentPage);
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
//								console.log("继续获取下一页小区");
								getGisCellByFreqType(loadToken);
							}else{
								//cobsic干扰－chao.xj
								onLoadingGisCell=false;
							}
						}
						// 如果没有获取完成，则继续获取
					} catch (err) {
//						console.log("返回的数据有问题:" + err);
						//cobsic干扰－chao.xj
						if (loadToken == currentloadtoken) {
									onLoadingGisCell=false;//终止
						}
					}
				},
				error : function(xmh, textstatus, e) {
					alert("出错啦！" + textstatus);
					//cobsic干扰－chao.xj
					if (loadToken == currentloadtoken) {
								onLoadingGisCell=false;//终止
					}
				},
				complete : function() {
//					whetherLoadCellToMap=true;
					//$(".loading_cover").css("display", "none");
					hideOperTips("loadingDataDiv");
				}
			});
}
/**
 * 清除全部的数据
 */
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

//	cellDetails.splice(0, cellDetails.length);
	
	gisCellDisplayLib.clearData();

}
//执行查询ncs
function doQueryNcsDesc(){
	console.log("进来了doQueryNcsDesc");
	$("#ncsDescDataForm").ajaxSubmit({
		url : 'queryNcsDescpByPageForAjaxAction',
		dataType : 'text',
		success : function(data) {
//			console.log("success:"+data);
			showNcsQueryResult(data);
		},
		complete : function() {
			$(".loading_cover").css("display", "none");
		},
		error:function(x,xx,xxx){
			console.log(x+":"+xx+":"+xxx);
		}
	})
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
//＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝新增项
/**
 * 获取最新一次ncs任务时间
 * @param {} areaid
 * [{"YEARMONTH":"2014/2","COMPLETE_TIME":"2014-02-28 18:34:39","TASK_ID":37}]
 */
function getLatelyNcsTaskTime(){
	//$(".loading_cover").css("display", "block");
	//var areaid=$("#areaId").find("option:selected").val();
	//if(areaid==-1){
	//	 areaid=$("#cityId").find("option:selected").val();
	//}
	$("#latelytasktime").text("正在寻找干扰矩阵...");
	areaid=$("#cityId").find("option:selected").val();
	$.ajax({
		url : 'getLatelyNcsTaskTimeForAjaxAction',
		data : {
			'areaId' : areaid
		},
		dataType : 'json',
		type : 'post',
		success : function(data) {
			if(typeof(data[0])==='undefined'){
				//console.log("无汇总数据!");
				$("#latelytasktime").text("无相应的干扰矩阵,请先进行干扰矩阵计算!");
				//$("#latelyTaskId").val("-1");
				$("#latelyMartixDescId").val("-1");
			}else{
			//console.log(data[0]['COMPLETE_TIME']);
			//$("#latelytasktime").text(data[0]['COMPLETE_TIME']);
			//$("#latelyTaskId").val(data[0]['TASK_ID']);
				$("#latelytasktime").text(data[0]['CREATE_DATE']);
				$("#latelyMartixDescId").val(data[0]['MARTIX_DESC_ID']);
			}
		},
		error : function(xhr, textstatus, e) {

		},
		complete : function() {
			//$(".loading_cover").css("display", "none");
			
		}
	});
}
/**
 * 获取小区干扰矩阵数据详情信息
 * @param {} cellname
 * result=CellInterWithDetailInfo [cellAbsts=[CellAbst [cellName=DQVCBX1, isNei=1], CellAbst [cellName=DQVHJY2, isNei=1], CellAbst [cellName=DQVJTX2, isNei=1], CellAbst [cellName=DQVJTX3, isNei=1], CellAbst [cellName=DQVJXB2, isNei=1], CellAbst [cellName=DQVXYG1, isNei=1], CellAbst [cellName=DQVZRT1, isNei=0], CellAbst [cellName=GQVE1AX, isNei=1], CellAbst [cellName=GQVE2AX, isNei=1], CellAbst [cellName=GQVJSLN, isNei=1], CellAbst [cellName=GQVJXX3, isNei=1], CellAbst [cellName=GQVJXY2, isNei=1], CellAbst [cellName=GQVXYG3, isNei=1]], cellName=GBVDGZ3, freqInterInfos=[FreqInterInfo [freq=21, freqType=, in=[], out=[InterDetail [ciOrCa=uca, value=3.367456896551724E-5, ncellIdx=7, affectFreq=22]], inTotal=0.0, outTotal=6.734913793103448E-5], FreqInterInfo [freq=23, freqType=, in=[], out=[InterDetail [ciOrCa=lca, value=3.367456896551724E-5, ncellIdx=7, affectFreq=22]], inTotal=0.0, outTotal=6.734913793103448E-5], FreqInterInfo [freq=22, freqType=, in=[], out=[InterDetail [ciOrCa=ci, value=5.61242816091954E-5, ncellIdx=7, affectFreq=22]], inTotal=0.0, outTotal=1.122485632183908E-4], FreqInterInfo [freq=34, freqType=, in=[], out=[InterDetail [ciOrCa=ci, value=5.61242816091954E-5, ncellIdx=7, affectFreq=34]], inTotal=0.0, outTotal=1.122485632183908E-4], FreqInterInfo [freq=35, freqType=, in=[], out=[InterDetail [ciOrCa=lca, value=3.367456896551724E-5, ncellIdx=7, affectFreq=34]], inTotal=0.0, outTotal=6.734913793103448E-5], FreqInterInfo [freq=33, freqType=tch, in=[], out=[InterDetail [ciOrCa=uca, value=3.367456896551724E-5, ncellIdx=7, affectFreq=34]], inTotal=0.0, outTotal=6.734913793103448E-5], FreqInterInfo [freq=73, freqType=, in=[], out=[InterDetail [ciOrCa=uca, value=3.367456896551724E-5, ncellIdx=7, affectFreq=74]], inTotal=0.0, outTotal=6.734913793103448E-5], FreqInterInfo [freq=74, freqType=, in=[], out=[InterDetail [ciOrCa=ci, value=5.61242816091954E-5, ncellIdx=7, affectFreq=74]], inTotal=0.0, outTotal=1.122485632183908E-4], FreqInterInfo [freq=75, freqType=, in=[], out=[InterDetail [ciOrCa=lca, value=3.367456896551724E-5, ncellIdx=7, affectFreq=74]], inTotal=0.0, outTotal=6.734913793103448E-5], FreqInterInfo [freq=85, freqType=, in=[], out=[InterDetail [ciOrCa=lca, value=3.367456896551724E-5, ncellIdx=7, affectFreq=84]], inTotal=0.0, outTotal=6.734913793103448E-5], FreqInterInfo [freq=84, freqType=tch, in=[], out=[InterDetail [ciOrCa=ci, value=5.61242816091954E-5, ncellIdx=7, affectFreq=84]], inTotal=0.0, outTotal=1.122485632183908E-4], FreqInterInfo [freq=83, freqType=, in=[], out=[InterDetail [ciOrCa=uca, value=3.367456896551724E-5, ncellIdx=7, affectFreq=84]], inTotal=0.0, outTotal=6.734913793103448E-5], FreqInterInfo [freq=11, freqType=tch, in=[], out=[InterDetail [ciOrCa=ci, value=1.0368854725432726E-4, ncellIdx=8, affectFreq=11]], inTotal=0.0, outTotal=2.0737709450865453E-4], FreqInterInfo [freq=56, freqType=, in=[], out=[InterDetail [ciOrCa=ci, value=1.0368854725432726E-4, ncellIdx=8, affectFreq=56]], inTotal=0.0, outTotal=2.0737709450865453E-4], FreqInterInfo [freq=84, freqType=tch, in=[], out=[InterDetail [ciOrCa=ci, value=1.0368854725432726E-4, ncellIdx=8, affectFreq=84]], inTotal=0.0, outTotal=2.0737709450865453E-4], FreqInterInfo [freq=30, freqType=, in=[], out=[InterDetail [ciOrCa=ci, value=6.590220113351786E-5, ncellIdx=9, affectFreq=30]], inTotal=0.0, outTotal=1.3180440226703572E-4], FreqInterInfo [freq=41, freqType=, in=[], out=[InterDetail [ciOrCa=ci, value=6.590220113351786E-5, ncellIdx=9, affectFreq=41]], inTotal=0.0, outTotal=1.3180440226703572E-4], FreqInterInfo [freq=56, freqType=, in=[], out=[InterDetail [ciOrCa=ci, value=6.590220113351786E-5, ncellIdx=9, affectFreq=56]], inTotal=0.0, outTotal=1.3180440226703572E-4], FreqInterInfo [freq=88, freqType=, in=[], out=[InterDetail [ciOrCa=ci, value=6.590220113351786E-5, ncellIdx=9, affectFreq=88]], inTotal=0.0, outTotal=1.3180440226703572E-4], FreqInterInfo [freq=10, freqType=, in=[], out=[InterDetail [ciOrCa=ci, value=0.004109988191451838, ncellIdx=10, affectFreq=10]], inTotal=0.0, outTotal=0.008219976382903676], FreqInterInfo [freq=23, freqType=, in=[], out=[InterDetail [ciOrCa=ci, value=0.004109988191451838, ncellIdx=10, affectFreq=23]], inTotal=0.0, outTotal=0.008219976382903676], FreqInterInfo [freq=27, freqType=, in=[], out=[InterDetail [ciOrCa=ci, value=0.004109988191451838, ncellIdx=10, affectFreq=27]], inTotal=0.0, outTotal=0.008219976382903676], FreqInterInfo [freq=37, freqType=, in=[], out=[InterDetail [ciOrCa=ci, value=0.004109988191451838, ncellIdx=10, affectFreq=37]], inTotal=0.0, outTotal=0.008219976382903676], FreqInterInfo [freq=56, freqType=, in=[], out=[InterDetail [ciOrCa=ci, value=0.004109988191451838, ncellIdx=10, affectFreq=56]], inTotal=0.0, outTotal=0.008219976382903676], FreqInterInfo [freq=74, freqType=, in=[], out=[InterDetail [ciOrCa=ci, value=0.004109988191451838, ncellIdx=10, affectFreq=74]], inTotal=0.0, outTotal=0.008219976382903676], FreqInterInfo [freq=87, freqType=, in=[], out=[InterDetail [ciOrCa=ci, value=0.004109988191451838, ncellIdx=10, affectFreq=87]], inTotal=0.0, outTotal=0.008219976382903676], FreqInterInfo [freq=92, freqType=tch, in=[], out=[InterDetail [ciOrCa=ci, value=0.004109988191451838, ncellIdx=10, affectFreq=92]], inTotal=0.0, outTotal=0.008219976382903676], FreqInterInfo [freq=11, freqType=tch, in=[], out=[InterDetail [ciOrCa=uca, value=6.390184676337147E-5, ncellIdx=11, affectFreq=12]], inTotal=0.0, outTotal=1.2780369352674293E-4], FreqInterInfo [freq=12, freqType=, in=[], out=[InterDetail [ciOrCa=ci, value=0.007348712377787718, ncellIdx=11, affectFreq=12]], inTotal=0.0, outTotal=0.014697424755575436], FreqInterInfo [freq=13, freqType=, in=[], out=[InterDetail [ciOrCa=lca, value=6.390184676337147E-5, ncellIdx=11, affectFreq=12]], inTotal=0.0, outTotal=1.2780369352674293E-4], FreqInterInfo [freq=64, freqType=, in=[], out=[InterDetail [ciOrCa=uca, value=6.390184676337147E-5, ncellIdx=11, affectFreq=65]], inTotal=0.0, outTotal=1.2780369352674293E-4], FreqInterInfo [freq=65, freqType=, in=[], out=[InterDetail [ciOrCa=ci, value=0.007348712377787718, ncellIdx=11, affectFreq=65]], inTotal=0.0, outTotal=0.014697424755575436], FreqInterInfo [freq=66, freqType=, in=[], out=[InterDetail [ciOrCa=lca, value=6.390184676337147E-5, ncellIdx=11, affectFreq=65]], inTotal=0.0, outTotal=1.2780369352674293E-4], FreqInterInfo [freq=81, freqType=, in=[], out=[InterDetail [ciOrCa=uca, value=6.390184676337147E-5, ncellIdx=11, affectFreq=82]], inTotal=0.0, outTotal=1.2780369352674293E-4], FreqInterInfo [freq=83, freqType=, in=[], out=[InterDetail [ciOrCa=lca, value=6.390184676337147E-5, ncellIdx=11, affectFreq=82]], inTotal=0.0, outTotal=1.2780369352674293E-4], FreqInterInfo [freq=82, freqType=, in=[], out=[InterDetail [ciOrCa=ci, value=0.007348712377787718, ncellIdx=11, affectFreq=82]], inTotal=0.0, outTotal=0.014697424755575436], FreqInterInfo [freq=89, freqType=, in=[], out=[InterDetail [ciOrCa=ci, value=0.007348712377787718, ncellIdx=11, affectFreq=89]], inTotal=0.0, outTotal=0.014697424755575436], FreqInterInfo [freq=88, freqType=, in=[], out=[InterDetail [ciOrCa=uca, value=6.390184676337147E-5, ncellIdx=11, affectFreq=89]], inTotal=0.0, outTotal=1.2780369352674293E-4], FreqInterInfo [freq=90, freqType=, in=[], out=[InterDetail [ciOrCa=lca, value=6.390184676337147E-5, ncellIdx=11, affectFreq=89]], inTotal=0.0, outTotal=1.2780369352674293E-4], FreqInterInfo [freq=3, freqType=, in=[], out=[InterDetail [ciOrCa=uca, value=0.0018689484350371537, ncellIdx=12, affectFreq=4]], inTotal=0.0, outTotal=0.0037378968700743074], FreqInterInfo [freq=4, freqType=, in=[], out=[InterDetail [ciOrCa=ci, value=0.035239810853411394, ncellIdx=12, affectFreq=4]], inTotal=0.0, outTotal=0.07047962170682279], FreqInterInfo [freq=5, freqType=tch, in=[], out=[InterDetail [ciOrCa=lca, value=0.0018689484350371537, ncellIdx=12, affectFreq=4]], inTotal=0.0, outTotal=0.0037378968700743074], FreqInterInfo [freq=15, freqType=tch, in=[], out=[InterDetail [ciOrCa=uca, value=0.0018689484350371537, ncellIdx=12, affectFreq=16]], inTotal=0.0, outTotal=0.0037378968700743074], FreqInterInfo [freq=17, freqType=, in=[], out=[InterDetail [ciOrCa=lca, value=0.0018689484350371537, ncellIdx=12, affectFreq=16]], inTotal=0.0, outTotal=0.0037378968700743074], FreqInterInfo [freq=16, freqType=, in=[], out=[InterDetail [ciOrCa=ci, value=0.035239810853411394, ncellIdx=12, affectFreq=16]], inTotal=0.0, outTotal=0.07047962170682279], FreqInterInfo [freq=25, freqType=tch, in=[], out=[InterDetail [ciOrCa=uca, value=0.0018689484350371537, ncellIdx=12, affectFreq=26]], inTotal=0.0, outTotal=0.0037378968700743074], FreqInterInfo [freq=27, freqType=, in=[], out=[InterDetail [ciOrCa=lca, value=0.0018689484350371537, ncellIdx=12, affectFreq=26]], inTotal=0.0, outTotal=0.0037378968700743074], FreqInterInfo [freq=26, freqType=, in=[], out=[InterDetail [ciOrCa=ci, value=0.035239810853411394, ncellIdx=12, affectFreq=26]], inTotal=0.0, outTotal=0.07047962170682279], FreqInterInfo [freq=69, freqType=, in=[], out=[InterDetail [ciOrCa=uca, value=0.0018689484350371537, ncellIdx=12, affectFreq=70]], inTotal=0.0, outTotal=0.0037378968700743074], FreqInterInfo [freq=70, freqType=, in=[], out=[InterDetail [ciOrCa=ci, value=0.035239810853411394, ncellIdx=12, affectFreq=70]], inTotal=0.0, outTotal=0.07047962170682279], FreqInterInfo [freq=71, freqType=, in=[], out=[InterDetail [ciOrCa=lca, value=0.0018689484350371537, ncellIdx=12, affectFreq=70]], inTotal=0.0, outTotal=0.0037378968700743074], FreqInterInfo [freq=87, freqType=, in=[], out=[InterDetail [ciOrCa=ci, value=0.035239810853411394, ncellIdx=12, affectFreq=87]], inTotal=0.0, outTotal=0.07047962170682279], FreqInterInfo [freq=86, freqType=, in=[], out=[InterDetail [ciOrCa=uca, value=0.0018689484350371537, ncellIdx=12, affectFreq=87]], inTotal=0.0, outTotal=0.0037378968700743074], FreqInterInfo [freq=93, freqType=, in=[], out=[InterDetail [ciOrCa=uca, value=0.0018689484350371537, ncellIdx=12, affectFreq=94]], inTotal=0.0, outTotal=0.0037378968700743074], FreqInterInfo [freq=94, freqType=, in=[], out=[InterDetail [ciOrCa=ci, value=0.035239810853411394, ncellIdx=12, affectFreq=94]], inTotal=0.0, outTotal=0.07047962170682279], FreqInterInfo [freq=88, freqType=, in=[], out=[InterDetail [ciOrCa=lca, value=0.0018689484350371537, ncellIdx=12, affectFreq=87]], inTotal=0.0, outTotal=0.0037378968700743074]]]
 *{"cellName":"GQXHXS3",
 *"freqInterInfos":[{"freq":8,"freqType":"","in":[],"out":[{"ciOrCa":"uca","value":0.008504479278017955,"ncellIdx":1,"affectFreq":9}],"inTotal":0.0,"outTotal":0.01700895855603591},{"freq":9,"freqType":"","in":[],"out":[{"ciOrCa":"ci","value":0.3323841867769064,"ncellIdx":1,"affectFreq":9}],"inTotal":0.0,"outTotal":0.6647683735538128},{"freq":10,"freqType":"","in":[],"out":[{"ciOrCa":"lca","value":0.008504479278017955,"ncellIdx":1,"affectFreq":9}],"inTotal":0.0,"outTotal":0.01700895855603591},{"freq":17,"freqType":"tch","in":[],"out":[{"ciOrCa":"uca","value":0.008504479278017955,"ncellIdx":1,"affectFreq":18}],"inTotal":0.0,"outTotal":0.01700895855603591},{"freq":19,"freqType":"","in":[],"out":[{"ciOrCa":"lca","value":0.008504479278017955,"ncellIdx":1,"affectFreq":18},{"ciOrCa":"uca","value":0.008504479278017955,"ncellIdx":1,"affectFreq":20}],"inTotal":0.0,"outTotal":0.03401791711207182},{"freq":18,"freqType":"","in":[],"out":[{"ciOrCa":"ci","value":0.3323841867769064,"ncellIdx":1,"affectFreq":18}],"inTotal":0.0,"outTotal":0.6647683735538128},{"freq":21,"freqType":"","in":[],"out":[{"ciOrCa":"lca","value":0.008504479278017955,"ncellIdx":1,"affectFreq":20}],"inTotal":0.0,"outTotal":0.01700895855603591},{"freq":20,"freqType":"","in":[],"out":[{"ciOrCa":"ci","value":0.3323841867769064,"ncellIdx":1,"affectFreq":20}],"inTotal":0.0,"outTotal":0.6647683735538128},{"freq":27,"freqType":"","in":[],"out":[{"ciOrCa":"ci","value":0.3323841867769064,"ncellIdx":1,"affectFreq":27}],"inTotal":0.0,"outTotal":0.6647683735538128},{"freq":26,"freqType":"","in":[],"out":[{"ciOrCa":"uca","value":0.008504479278017955,"ncellIdx":1,"affectFreq":27}],"inTotal":0.0,"outTotal":0.01700895855603591},{"freq":29,"freqType":"","in":[],"out":[{"ciOrCa":"ci","value":0.3323841867769064,"ncellIdx":1,"affectFreq":29}],"inTotal":0.0,"outTotal":0.6647683735538128},{"freq":28,"freqType":"","in":[],"out":[{"ciOrCa":"lca","value":0.008504479278017955,"ncellIdx":1,"affectFreq":27},{"ciOrCa":"uca","value":0.008504479278017955,"ncellIdx":1,"affectFreq":29}],"inTotal":0.0,"outTotal":0.03401791711207182},{"freq":30,"freqType":"","in":[],"out":[{"ciOrCa":"lca","value":0.008504479278017955,"ncellIdx":1,"affectFreq":29}],"inTotal":0.0,"outTotal":0.01700895855603591},{"freq":35,"freqType":"","in":[],"out":[{"ciOrCa":"uca","value":0.008504479278017955,"ncellIdx":1,"affectFreq":36}],"inTotal":0.0,"outTotal":0.01700895855603591},{"freq":36,"freqType":"","in":[],"out":[{"ciOrCa":"ci","value":0.3323841867769064,"ncellIdx":1,"affectFreq":36}],"inTotal":0.0,"outTotal":0.6647683735538128},{"freq":37,"freqType":"","in":[],"out":[{"ciOrCa":"lca","value":0.008504479278017955,"ncellIdx":1,"affectFreq":36}],"inTotal":0.0,"outTotal":0.01700895855603591},{"freq":42,"freqType":"","in":[],"out":[{"ciOrCa":"lca","value":0.008504479278017955,"ncellIdx":1,"affectFreq":41}],"inTotal":0.0,"outTotal":0.01700895855603591},{"freq":40,"freqType":"","in":[],"out":[{"ciOrCa":"uca","value":0.008504479278017955,"ncellIdx":1,"affectFreq":41}],"inTotal":0.0,"outTotal":0.01700895855603591},{"freq":41,"freqType":"","in":[],"out":[{"ciOrCa":"ci","value":0.3323841867769064,"ncellIdx":1,"affectFreq":41}],"inTotal":0.0,"outTotal":0.6647683735538128},{"freq":46,"freqType":"","in":[],"out":[{"ciOrCa":"lca","value":0.008504479278017955,"ncellIdx":1,"affectFreq":45}],"inTotal":0.0,"outTotal":0.01700895855603591},{"freq":44,"freqType":"","in":[],"out":[{"ciOrCa":"uca","value":0.008504479278017955,"ncellIdx":1,"affectFreq":45}],"inTotal":0.0,"outTotal":0.01700895855603591},{"freq":45,"freqType":"","in":[],"out":[{"ciOrCa":"ci","value":0.3323841867769064,"ncellIdx":1,"affectFreq":45}],"inTotal":0.0,"outTotal":0.6647683735538128}],
 *"cellAbsts":[{"cellName":"DQVHYY2","isNei":"0"},{"cellName":"GQVHYYM","isNei":"0"}]}
 *
 */
function getCellInterferMatrixData(cellname){
	//$(".loading_cover").css("display", "block");
	//animateInAndOut("operInfo", 200, 200, 500, "operTip", "正在处理数据，请稍候！！");
	var flag = true; //用于状态提示
	$("span#loadingStatus").html("  加载小区干扰记录中...");
	showOperTips("loadingDataDiv", "loadContentId", "正在处理数据");
	//var areaid=$("#areaId").find("option:selected").val();
	//if(areaid==-1){
	//	 areaid=$("#cityId").find("option:selected").val();
	//}
	//var taskId = $("#latelyTaskId").val();
	var MartixDescId = $("#latelyMartixDescId").val();
	$.ajax({
		url : 'getCellInterferMatrixDataDetailForAjaxAction',
		data : {
			'cell' : cellname,
			//'areaId' : areaid
			'MartixDescId' : MartixDescId
		},
		dataType : 'json',
		type : 'post',
		success : function(data) {
			if(data==null){
//				console.log("未找到小区：" + cellname + "的干扰记录");
				flag = false;
				$("span#loadingStatus").html("未找到小区" + cellname + "的干扰记录！");
				//animateInAndOut("operInfo", 200, 200, 500, "operTip", "未找到小区：" + cellname + "的干扰记录!");
			}else{
				try{
					    var cellName = data['cellName'];
					    var cellBcch = data['cellBcch'];
					    var cellFreq = data['cellFreq'];
					    
					    $("#cellname").text(cellName);
					    //显示小区的bcch与tch
					    //console.log(cellBcch);
					    //console.log(cellFreq);
						$("span#cellFreq").html("BCCH : [" + cellBcch + "], TCH : [" + cellFreq + "]");
						
					    var freqInterInfos = data['freqInterInfos'];
					    var cellAbsts = data['cellAbsts'];
					    
					    if(freqInterInfos.length <= 0 || cellAbsts.length <= 0) {
					     	flag = false;
					    	$("span#loadingStatus").html("未找到小区" + cellname + "的干扰记录！");
					    } else {
						    //以下目的将详情数据存储起来－－－－－－开始
								var cellFreqObj=new Array();
							    //将单独频点对应的同频与邻频数据储存起来
								//cellFreqToNcellInter={"freqInterInfos":freqInterInfos,"cellAbsts":cellAbsts};
								cellFreqToNcellInter={"cellName":cellName,"freqInterInfos":freqInterInfos,"cellAbsts":cellAbsts};
							//以上目的将详情数据存储起来－－－－－－结束
						    
							$.each(freqInterInfos,function(k,v){
	//							console.log(k+":"+v.freq);
								var sumcellInteredInto=Number(v['inTotal']).toFixed(4);
								var sumcellInterOut=Number(v['outTotal']).toFixed(4);
								var sumtotalInter=(Number(sumcellInteredInto)+Number(sumcellInterOut)).toFixed(4);
								
								var tr="<tr onDblClick=\"getCellFreqInterDetailedInfoOnTable(this);changeActiveRow(this);\" onclick= \"getCellFreqInterDetailedInfoOnMap(this);changeActiveRow(this);\" >"
										+"<td align=\"center\">"
										+v['freq']
										+"</td>"
										+"<td align=\"center\">"
										+sumcellInteredInto
										+"</td>"
										+"<td align=\"center\">"
										+sumcellInterOut		
										+"</td>"
										+"<td align=\"center\">"
										+sumtotalInter		
										+"</td>"
										+"<td align=\"center\">"
										+v['freqType']			
										+"</td>"
									+"</tr>";
									$("#freqintersituation").append(tr);
							});
							//将该table转成可以做排序的table
							new TableSorter("freqintersituation").OnSorted = function(c, t) {
								$(currentActiveRow).removeClass("greystyle-standard-red-inter");
								currentActiveRow = null;
								$("#detailedinterdiv").hide();
							};
					
							$("#interference_dialogId").show();
							
							//在地图体现干扰关系
							//displayListInterInfo(cellName,data);	
					    }

				}catch(err){
					//animateInAndOut("operInfo", 200, 200, 500, "operTip", "分析出错！");
				}
			}
		},
		error : function(xhr, textstatus, e) {

		},
		complete : function() {
			//$(".loading_cover").css("display", "none");
			hideOperTips("loadingDataDiv");
			if(flag) {
				$("span#loadingStatus").html("小区干扰记录加载完成");
			}
		}
	});
}
/**
 * 通过传入行对象获取小区频点为KEY的数据获取其下的集合对象(频点详情干扰信息)
 * @param {} obj
 */
function getCellFreqInterDetailedInfoOnMap(obj){
	$("#detailedinterdiv").hide();
	

	//先清空除了前两行表头
	$("#detailedintersituation tr:gt(0)").remove();
	var cellFreq=$(obj).find("td").eq(0).text();
	$("#cellfreqlabel").text(cellFreq);
	
	var data = parseCellFreqToInterInfo(cellFreq);

	$("#detailedintersituation").html();
	
	//恢复默认图元外观
	gisCellDisplayLib.clearOnlyExtraOverlay();//清除额外覆盖物
	gisCellDisplayLib.resetPolygonToDefaultOutlook();//恢复默认polygon颜色
	
	var cellName = cellFreqToNcellInter['cellName'];
	//主覆盖小区颜色
	var option_serverCell = {'fillColor':'#10000E','fillOpacity':1};
	gisCellDisplayLib.changeCellPolygonOptions(cellName,option_serverCell,true);
	//邻频小区颜色
	var option_caCell = {'fillColor':'#E9D713','fillOpacity':1};
	//同频小区颜色
	var option_ciCell = {'fillColor':'#CE110F','fillOpacity':1};
	
	for(var key in data){
		var obj = data[key][0];
		
		var ncell = obj['ncell'];
		var ncellDescName = obj['ncellDescName'];
		var cellInterIn = obj['in'];
		var total = obj['total'];
		var cellInterOut = obj['out'];
		var ciOrCa = obj['ciOrCa'];
		var isNei = obj['isNei'];
		var isSameSite = obj['isSameSite'];
		var affectFreq=obj['affectFreq'];
		
		//地图显示邻频，同频小区样式
		if(ciOrCa == "同频") {
			gisCellDisplayLib.changeCellPolygonOptions(ncell,option_ciCell,true);
		} else {
			gisCellDisplayLib.changeCellPolygonOptions(ncell,option_caCell,true);
		}
		
//		console.log(v.ncell);
		var tr="<tr>"
					+"<td align=\"center\">"
					+ncell+"<br/>"
					+"<label title='"+ncellDescName+"'>" + ncellDescName.substring(0,7)+"..." + "</label>"
					+"</td>"
					+"<td align=\"center\">"
					+affectFreq	
					+"</td>"
					+"<td align=\"center\">"
					+cellInterIn.toFixed(4)		
					+"</td>"
					+"<td align=\"center\">"
					+cellInterOut.toFixed(4)		
					+"</td>"
					+"<td align=\"center\">"
					+total.toFixed(4)			
					+"</td>"
					+"<td align=\"center\">"
					+isNei	
					+"</td>"
					+"<td align=\"center\">"
					+isSameSite	
					+"</td>"
					+"<td align=\"center\">"
					+ciOrCa	
					+"</td>"
				+"</tr>";
		$("#detailedintersituation").append(tr);
	}
	//定义为排序table
	new TableSorter("detailedintersituation");
}

/**
 * 通过传入行对象获取小区频点为KEY的数据获取其下的集合对象(频点详情干扰信息)
 * @param {} obj
 */
function getCellFreqInterDetailedInfoOnTable(obj){
	$("#detailedinterdiv").show();
	
	//先清空除了前两行表头
	$("#detailedintersituation tr:gt(0)").remove();
	var cellFreq=$(obj).find("td").eq(0).text();
	$("#cellfreqlabel").text(cellFreq);
	
	var data = parseCellFreqToInterInfo(cellFreq);

	$("#detailedintersituation").html();
	
	//恢复默认图元外观
	gisCellDisplayLib.clearOnlyExtraOverlay();//清除额外覆盖物
	gisCellDisplayLib.resetPolygonToDefaultOutlook();//恢复默认polygon颜色
	
	var cellName = cellFreqToNcellInter['cellName'];
	//主覆盖小区颜色
	var option_serverCell = {'fillColor':'#10000E','fillOpacity':1};
	gisCellDisplayLib.changeCellPolygonOptions(cellName,option_serverCell,true);
	//邻频小区颜色
	var option_caCell = {'fillColor':'#E9D713','fillOpacity':1};
	//同频小区颜色
	var option_ciCell = {'fillColor':'#CE110F','fillOpacity':1};
	
	for(var key in data){
		var obj = data[key][0];
		
		var ncell = obj['ncell'];
		var ncellDescName = obj['ncellDescName'];
		var cellInterIn = obj['in'];
		var total = obj['total'];
		var cellInterOut = obj['out'];
		var ciOrCa = obj['ciOrCa'];
		var isNei = obj['isNei'];
		var isSameSite = obj['isSameSite'];
		var affectFreq=obj['affectFreq'];
		
		//地图显示邻频，同频小区样式
		if(ciOrCa == "同频") {
			gisCellDisplayLib.changeCellPolygonOptions(ncell,option_ciCell,true);
		} else {
			gisCellDisplayLib.changeCellPolygonOptions(ncell,option_caCell,true);
		}
		
//		console.log(v.ncell);
		var tr="<tr>"
					+"<td align=\"center\">"
					+ncell+"<br/>"
					+"<label title='"+ncellDescName+"'>" + ncellDescName.substring(0,7)+"..." + "</label>"
					+"</td>"
					+"<td align=\"center\">"
					+affectFreq	
					+"</td>"
					+"<td align=\"center\">"
					+cellInterIn.toFixed(4)		
					+"</td>"
					+"<td align=\"center\">"
					+cellInterOut.toFixed(4)		
					+"</td>"
					+"<td align=\"center\">"
					+total.toFixed(4)			
					+"</td>"
					+"<td align=\"center\">"
					+isNei	
					+"</td>"
					+"<td align=\"center\">"
					+isSameSite	
					+"</td>"
					+"<td align=\"center\">"
					+ciOrCa	
					+"</td>"
				+"</tr>";
		$("#detailedintersituation").append(tr);
	}
	//定义为排序table
	new TableSorter("detailedintersituation");
}

/**
 *  列表展示小区干扰矩阵信息
 *	将干扰关系在地图上体现
 */
function displayListInterInfo(cellName,obj){

						//$("#listdisplay").show();//显示列表展示
						//$("#listdisplaycell").text(cellName);
						
						var cellName=obj.cellName;
						var freqInterInfos=obj.freqInterInfos;
						var cellAbsts=obj.cellAbsts;
						
						//为小区到列表干扰值存储作好准备
						var cellToInterVal=new Object();
						//同频，上邻频，下邻频
						var cellToSameOrAdjFreqInterVal=new Object();
						var sameOrAdjFreqArr=new Array();//[{"uca":"uca","lca":lca,"ci":ci}]
						
						// 主覆盖小区颜色
						var option_serverCell={};
						option_serverCell={'fillColor':'#10000E','fillOpacity':1};
						gisCellDisplayLib.changeCellPolygonOptions(cellName,option_serverCell,true);
						$.each(cellAbsts,function(k,v){
							var cell=v.cellName;
							//列表展示
							cellToInterVal[cell]=0;
							//同频，上邻频，下邻频
							//{"cellName":[{"uca":"uca","lca":lca,"ci":ci}]},创建空数组
							cellToSameOrAdjFreqInterVal[cell]=[];
						});
						
						
						$.each(freqInterInfos,function(k,v){
							//console.log(k+":"+v.freq);
								var inter_in=v['in'];
								var inter_out=v['out'];
//								console.log("inter_in:"+inter_in+"--------------inter_out:"+inter_out);
								if(inter_in.length!=0){
//									console.log("typeof(inter_in):"+typeof(inter_in));
									$.each(inter_in,function(k,v1){

									var ncellIdx=v1.ncellIdx;
//									console.log("ncellIdx:"+ncellIdx);
									var ncell=cellAbsts[ncellIdx].cellName;
									var cellInterIn=v1.value;
									var ciOrCa=v1.ciOrCa;
									cellToInterVal[ncell]=Number(cellToInterVal[ncell])+Number(cellInterIn);//取出上次存储的干扰值进行累加
									
									//同邻频
									var freqobj={};
									if(cellToSameOrAdjFreqInterVal[ncell].length!=0){
										freqobj=cellToSameOrAdjFreqInterVal[ncell][0];
										freqobj[ciOrCa]=Number(freqobj[ciOrCa])+Number(cellInterIn);
										sameOrAdjFreqArr.push(freqobj);
										cellToSameOrAdjFreqInterVal[ncell]=sameOrAdjFreqArr;
									}else{
										if(ciOrCa=="uca"){
										freqobj={"uca":cellInterIn,"lca":0,"ci":0};
										}
										if(ciOrCa=="lca"){
										freqobj={"uca":0,"lca":cellInterIn,"ci":0};
										}
										if(ciOrCa=="ci"){
										freqobj={"uca":0,"lca":0,"ci":cellInterIn};
										}
										sameOrAdjFreqArr.push(freqobj);
										cellToSameOrAdjFreqInterVal[ncell]=sameOrAdjFreqArr;
									}
									
									
									});
								}
								
								if(inter_out.length!=0){
//									console.log("typeof(inter_out):"+typeof(inter_out));
									$.each(inter_out,function(k,v1){
									var ncellIdx=v1.ncellIdx;
//									console.log("ncellIdx:"+ncellIdx);
									var ncell=cellAbsts[ncellIdx].cellName;
									var cellInterOut=v1.value;
									var ciOrCa=v1.ciOrCa;
									cellToInterVal[ncell]=Number(cellToInterVal[ncell])+Number(cellInterOut);//取出上次存储的干扰值进行累加
									//同邻频
									var freqobj={};
									if(cellToSameOrAdjFreqInterVal[ncell].length!=0){
										freqobj=cellToSameOrAdjFreqInterVal[ncell][0];
										freqobj[ciOrCa]=Number(freqobj[ciOrCa])+Number(cellInterOut);
										sameOrAdjFreqArr.push(freqobj);
										cellToSameOrAdjFreqInterVal[ncell]=sameOrAdjFreqArr;
									}else{
										if(ciOrCa=="uca"){
										freqobj={"uca":cellInterOut,"lca":0,"ci":0};
										}
										if(ciOrCa=="lca"){
										freqobj={"uca":0,"lca":cellInterOut,"ci":0};
										}
										if(ciOrCa=="ci"){
										freqobj={"uca":0,"lca":0,"ci":cellInterOut};
										}
										sameOrAdjFreqArr.push(freqobj);
										cellToSameOrAdjFreqInterVal[ncell]=sameOrAdjFreqArr;
									}
									
									});
								}	
							});
							
						 var listtr="";	
						for(var key in cellToInterVal){
									var cell=key;
									var inter_val=cellToInterVal[key];
									var distance=0;
									
									var option_ncell={};
									//划线选项
									var option_line={};
									if(inter_val<0.05){
										option_line={'strokeColor':'#0080FF',"strokeWeight":1};
									}else{
										option_line={'strokeColor':'#AE0000',"strokeWeight":1};
									}
									//距离计算
									var cellcmk=gisCellDisplayLib.cellToPolygon[cellName]._data;
									var lng1=cellcmk.getLng();
									//console.log("lng1:"+lng1);
									var lat1=cellcmk.getLat();
									
									var ncellcmk=gisCellDisplayLib.cellToPolygon[cell]._data;
									var lng2=ncellcmk.getLng();
									//console.log("lng2:"+lng2);
									var lat2=ncellcmk.getLat();
									distance=getdistance(lng1, lat1, lng2, lat2);	
									
									//console.log(gisCellDisplayLib.cellToPolygon[cellName]._data);
									//连线渲染
									
									gisCellDisplayLib.drawLineBetweenCells(cellName,cell,option_line);
									//小区渲染
									for(var key in cellToSameOrAdjFreqInterVal){
			//							console.log("cellToSameOrAdjFreqInterVal:"+key);
										var freqobj=cellToSameOrAdjFreqInterVal[key][0];
										var lca=freqobj['lca'];
										var uca=freqobj['uca'];
										var ci=freqobj['ci'];
										var option_ncell={};
										if(lca>=uca&&lca>=ci){
			//								console.log("lca"+lca);
											option_ncell={'fillColor':'#EC26D5','fillOpacity':1};
											gisCellDisplayLib.changeCellPolygonOptions(key,option_ncell,true);
										}
										if(uca>=lca&&uca>=ci){
			//								console.log("uca"+uca);
											option_ncell={'fillColor':'#46F0E5','fillOpacity':1};
											gisCellDisplayLib.changeCellPolygonOptions(key,option_ncell,true);
										}
										if(ci>=lca&&ci>=uca){
			//								console.log("ci"+ci);
											option_ncell={'fillColor':'#CE110F','fillOpacity':1};
											gisCellDisplayLib.changeCellPolygonOptions(key,option_ncell,true);
										}else{
			//								console.log("无");
											option_ncell={'fillColor':'#FFFDF8','fillOpacity':1};
											gisCellDisplayLib.changeCellPolygonOptions(key,option_ncell,true);
										}
										
									
									}
									//累加tr
									 listtr+="<tr>"
											    +"<td style=\"border:1px solid #ccc;\">"+cell+"</td>"
											    +"<td style=\"border:1px solid #ccc;\">"+inter_val+"</td>"
											    +"<td style=\"border:1px solid #ccc;\">"+distance+"</td>"
											  +"</tr>";
						}
						
						$("#listdistab").append(listtr);
}
/**
 * 解析小区干扰矩阵到详情对象
 * @param {} freq
 * @return {}
 */
function parseCellFreqToInterInfo(freq){
	var freqInterInfos=cellFreqToNcellInter['freqInterInfos'];
	var cellAbsts=cellFreqToNcellInter['cellAbsts'];
	var cellName=cellFreqToNcellInter['cellName'];
	
	
	//{"freq":[{"ciOrCa":"uca","value":0.008504479278017955,"ncellIdx":1,"affectFreq":9}]}
	//{"freq":[{"ciOrCa":"uca","ncell":"ncell","in":0.008504479278017955,"out":1,"total":0,"isNei":0,"affectFreq":9}]}
	var cellFreqDetailToNcellInter=null;
	
	$.each(freqInterInfos,function(k,v){
					var inter_in=v['in'];
					var inter_out=v['out'];
					var freqe=v['freq'];
			if(freqe==freq){
					if(inter_in.length!=0){
						$.each(inter_in,function(k,v1){
							var ncellIdx = v1.ncellIdx;
							var ncell = cellAbsts[ncellIdx].cellName;
							var ncellDescName = cellAbsts[ncellIdx].cellDescName;
							var isNei = cellAbsts[ncellIdx].isNei;
							var isSameSite = cellAbsts[ncellIdx].isSameSite;
							isNei=(isNei==0?"NO":"YES");
							var cellInterIn=v1.value;
							var affectFreq=v1.affectFreq;
							var ciOrCa=v1.ciOrCa;
							ciOrCa=(ciOrCa=="lca"?"下邻频":ciOrCa=="ci"?"同频":"上邻频");
							if(cellFreqDetailToNcellInter==null){
								cellFreqDetailToNcellInter={};
							}
						    cellFreqDetailToNcellInter[affectFreq+ncell]=[{"ciOrCa":ciOrCa,"ncell":ncell,"ncellDescName":ncellDescName,"in":cellInterIn,"out":0,"total":cellInterIn,"isNei":isNei,"isSameSite":isSameSite,"affectFreq":affectFreq}];
						});
					}
					var flag=cellFreqDetailToNcellInter;
					if(inter_out.length!=0){
						$.each(inter_out,function(k,v1){
							var ncellIdx = v1.ncellIdx;
							var ncell = cellAbsts[ncellIdx].cellName;
							var ncellDescName = cellAbsts[ncellIdx].cellDescName;
							var isNei = cellAbsts[ncellIdx].isNei;
							var isSameSite = cellAbsts[ncellIdx].isSameSite;
							isNei=(isNei==0?"NO":"YES");
							var cellInterOut=v1.value;
							var affectFreq=v1.affectFreq;
							var ciOrCa=v1.ciOrCa;
							ciOrCa=(ciOrCa=="lca"?"下邻频":ciOrCa=="ci"?"同频":"上邻频");
							if(flag==null){
								if(cellFreqDetailToNcellInter==null){
								cellFreqDetailToNcellInter={};
								}
								 cellFreqDetailToNcellInter[affectFreq+ncell]=[{"ciOrCa":ciOrCa,"ncell":ncell,"ncellDescName":ncellDescName,"in":0,"out":cellInterOut,"total":cellInterOut,"isNei":isNei,"isSameSite":isSameSite,"affectFreq":affectFreq}];
							}else{
								 if(typeof(cellFreqDetailToNcellInter[affectFreq])!="undefined"){
								 var obj=cellFreqDetailToNcellInter[affectFreq][0];
								 obj['out']=cellInterOut;
								 obj['total']= Number(obj['in'])+Number(cellInterOut);
								 }else{
 								 cellFreqDetailToNcellInter[affectFreq+ncell]=[{"ciOrCa":ciOrCa,"ncell":ncell,"ncellDescName":ncellDescName,"in":0,"out":cellInterOut,"total":cellInterOut,"isNei":isNei,"isSameSite":isSameSite,"affectFreq":affectFreq}];
								 }
							}
						});
					}
					//break;
			}
									
		});
		return cellFreqDetailToNcellInter;
}
/**
 * 改变行的颜色
 * @param {} obj
 */
function changeActiveRow(obj) {
//	console.log("changeActiveRow:" + obj+":"+currentActiveRow);
	if (!typeof(currentActiveRow)=="undefined" || currentActiveRow!="")
//		console.log("不是undefined");
	   $(currentActiveRow).removeClass("greystyle-standard-red-inter");
	
	if(obj == currentActiveRow) {
		$(currentActiveRow).removeClass("greystyle-standard-red-inter");
		currentActiveRow = null;
		$("#detailedinterdiv").hide();
	} else {
		currentActiveRow = obj;
		$(obj).addClass("greystyle-standard-red-inter");
	}
}   
function getdistance(lng1, lat1, lng2, lat2) {
	// var lat1 = latLng1.getLatitude();
	// var lng1 = latLng1.getLongitude();
	// var lat2 = latLng2.getLatitude();
	// var lng2 = latLng2.getLongitude();
	//console.log("distance进来了");
	if (!lng1 || !lat1 || !lng2 || !lat2) {
		// 任何一个没有定义，认为很远
		return 100000;
	}
	var R = 6371000; // 地球半径单位为米
	var dLat = (lat2 - lat1) * Math.PI / 180;
	var dLon = (lng2 - lng1) * Math.PI / 180;
	var a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
			+ Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180)
			* Math.sin(dLon / 2) * Math.sin(dLon / 2);
	var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	var d = R * c;
	return d;
}



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
					alert("未找到指定小区的邻区！");
					hideOperTips("loadingDataDiv");
					return;
				} else {
					var ownc = data[0]['cell'];
					var cellToNcell = new CellToNcell(ownc,gisCellDisplayLib.get("composeMarkers"));
					if (!cellToNcell) {
						alert("指定的小区尚不存在！");
						hideOperTips("loadingDataDiv");
						return;
					} else {
						for ( var i = 0; i < size; i++) {
							cellToNcell.addNcell(data[i]['ncell']);
						}
					}
					// 展现
					showNcell(cellToNcell);
					hideOperTips("loadingDataDiv");
				}
			},
			error : function(xhr, text, e) {
				alert("邻区查询出错！");
			},complete:function(){
				hideOperTips("loadingDataDiv");
			}
		});*/
		
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
	mapGridLib.loadGisCell(lastOperTime, currentloadtoken, minResponseInterval, params);
}
function handleMovestartAndZoomstart(e, lastOperTime) {
	//每一次移动，缩放的独立标识
	currentloadtoken = getLoadToken();
	mapGridLib.setCurrentloadtoken(currentloadtoken);
}
/***************** 地图小区加载改造 end ********************/