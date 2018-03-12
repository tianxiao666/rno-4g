
var dtopts={

"weakcoverage": [
 { 'areatype':'城区','distance':50,'rsrp':-110,'rssina':-3,"style":{"color": "#FF0000"}}
,{ 'areatype':'非城区','distance':120,'rsrp':-110,'rssina':-3,"style":{"color": "#FF0000"}}
,{ 'areatype':'高速','distance':240,'rsrp':-110,'rssina':-3,"style":{"color": "#FF0000"}}
],
"indoorsignalleakoutside": [
 { 'efreq':[38950,39000,39050,39150,39250],"style":{"color": "#FF0000"}}
 
],
"cellsignaloverlap": [
 { "style":{"color": "#FF0000"}}
]
};
var dtDetailData = new Array();//全局變量數組

//合并
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
$(document).ready(function() {

	//显示可以切换的地图
	$("#trigger").val(glomapid=='null' || glomapid=='baidu'?"切换谷歌":"切换百度");

	bindNormalEvent();

	initAreaCascade();
	initAreaCascade2();
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
	//以上合并
	$(".switch").click(function() {
		$(this).hide();
		$(".switch_hidden").show();
		$(".resource_list_icon").animate({
			right : '0px'
		}, 'fast');
		$(".resource_list300_box").hide("fast");
	})
	$(".switch_hidden").click(function() {
		$(this).hide();
		$(".switch").show();
		$(".resource_list_icon").animate({
			right : '380px'
		}, 'fast');
		$(".resource_list300_box").show("fast");
	})
	$(".zy_show").click(function() {
		$(".search_box_alert").slideToggle("fast");
	})
	//以上合并
	// 设置jquery ui
	jqueryUiSet();
	$(".fileCls").live("mouseover", function() {
//		var str=$(this).text();
		$(this).text($(this).attr("all"));
	});
	$(".fileCls").live("mouseout", function() {
//		var str=$(this).text();
		$(this).text($(this).attr("part"));
	});
	$("#queryDtBtn").click(function(){
		queryDtDescDataForMap();
	});
	$("#loadDtData").click(function(){
		
		queryDtDataDetailForMap(1);
	});
	
	//触发dt分析按钮  弱覆盖
	$("#weakcoveragebtn").click(function(){
		if (dtSampleContainer.getMapElementsLen() === 0) {
			animateOperTips("尚未有采样数据，无法进行分析！");
			return;
		}
		dtSampleContainer.statics4GWeakCoverage(dtopts.weakcoverage);
	});
	//触发dt分析按钮  室分外泄
	$("#radioresourceutilizationbtn").click(function(){
		if (dtSampleContainer.getMapElementsLen() === 0) {
			animateOperTips("尚未有采样数据，无法进行分析！");
			return;
		}
		dtSampleContainer.statics4GIndoorSignalLeakOutside(dtopts.indoorsignalleakoutside);
	});
	//触发dt分析按钮  重叠覆盖
	$("#cellsignaloverlapbtn").click(function(){
		if (dtSampleContainer.getMapElementsLen() === 0) {
			animateOperTips("尚未有采样数据，无法进行分析！");
			return;
		}
		dtSampleContainer.statics4GCellSignalOverlap(dtopts.cellsignaloverlap);
	});
});
function jqueryUiSet(){
	
	$("#datepicker").datepicker({
		"dateFormat" : "yy-mm-dd"
	});
	
	$("#dtMeaBegDate").datepicker(
			{
				dateFormat : "yy-mm-dd",
				defaultDate : "+1w",
				changeMonth : true,
				numberOfMonths : 1,
				onClose : function(selectedDate) {
					
				}
			});
}
/**
 * 查询DT描述数据
 */
function queryDtDescDataForMap(){
	$("#searchDtForm").ajaxSubmit({
		url:'queryG4DtDescForMapAjaxAction',
		type:'post',
		dataType:'text',
		success:function(raw){
			var data={};
			try{
			data=eval("("+raw+")");
			}catch(err){
				console.log(err);
			}
			displayDtDescData(data);
		}
	});
}
/**
 * 显示返回的MR描述信息
 * @param data
 */
function displayDtDescData(data){
	if(data==null||data==undefined){
		return;
	}
	//
	$("#dtListTab tr:not(:first)").each(function(i, ele) {
		$(ele).remove();
	});
	var html="";
	var city=$("#citymenu2").find("option:selected").text();
	var dataType="";
	for ( var i = 0; i < data.length; i++) {
		one = data[i];
		dataType=getValidValue(one['DATA_TYPE'],'');
		if(dataType=="DataService"){
			dataType="数据业务";
		}else if(dataType=="SweepFreq"){
			dataType="扫频业务";
		}
		var all=getValidValue(one['FILE_NAME'],'');
		var part = all.length>12?all.substring(0,12)+"...":all;
		var spanStr="<span class='fileCls' all='"+all+"' part='"+part+"'>" 
					+ part +"</span>";
		html += "<tr>";
		//html+="<td>"+city+"</td>";
//		html+="<td>"+getValidValue(one['MEA_TIME'],'')+"</td>";
		html+="<td>"+dataType+"</td>";
		html+="<td>"+getValidValue(one['AREA_TYPE'],'')+"</td>";
		html+="<td>"+spanStr+"</td>";
//		html+="<td>"+getValidValue(one['RECORD_COUNT'],'')+"</td>";
		html+="<td> <input id='fileList' name='fileList' type='checkbox' value='"+getValidValue(one['DT_SAMPLING_DESC_ID'],'')+"' /></td>";
//		html+="<td>"+getValidValue(one['CREATE_TIME'],'')+"</td>";
		html+="</tr>";
	}
	$("#dtListTab").append(html);
}
/**
 * 查询DT详情数据通过描述ID
 */
function queryDtDataDetailForMap(curpage){
		/*$("#dtListTab #fileList").each(function(i,ele){
		console.log("A----->>>"+i+"-------"+$(ele).val());
		});*/
	   
		var descId="";
		$('input[name="fileList"]:checked').each(function(){
			descId+=$(this).val()+",";
		//	console.log("B----->>>"+$(this).val());
		});
		if(descId==""){
			animateOperTips("未选择加载哪些数据，请选择。");
			return;
		}
		// showTips("采样点数据展现");
		 
		var cityId=$("#citymenu").find("option:selected").val();
		descId=descId.substring(0, descId.length-1);
//		console.log(descId);
		var hasMore;
	$.ajax({
		url:'queryG4DtDetailDataInDescIdByPageAjaxAction',
		type:'post',
		data : {
			'cityId':cityId,
			'descIdStr' : descId,
			'page.pageSize':300,
			'page.totalCnt':-1,
			'page.currentPage':curpage
		},
		dataType:'text',
		success:function(raw){
			var data={};
			try{
			data=eval("("+raw+")");
			}catch(err){
				console.log(err);
			}
			var page = data['page'];
			curpage = page.currentPage;
//			 console.log("curpage="+curpage);
			var samplelist = data['data'];
			hasMore = data['hasMore'];
			if (curpage == 2) {
				// 坐标移动
				gisCellDisplayLib.panTo(samplelist[0].lng,
						samplelist[0].lat);
			}
			if (hasMore == true) {
				curpage++;
				queryDtDataDetailForMap(curpage);
			}
			// 画出圆圈
			dtSampleContainer.showOnMap(samplelist);
		},
		error : function(xmh, textstatus, e) {
			hideTips("");
			alert("出错啦！" + textstatus);
		},
		complete : function() {
			if(!hasMore){
				hideTips("");
			}
		}
	});
	//hideTips("");
}
function clearPreState(){
	dtSampleContainer.clearOnlyExtraOverlay();
	gisCellDisplayLib.resetSpecPolygonToDefaultOutlook();
	gisCellDisplayLib.releaseOptions();
	
	currentSampleOccupyMode=sampleOccupyMode_IDLE;
	currentCellCoverMode = cellCoverMode_WAIT_F_CELL;
}

//显示操作提示信息
function showOperTips(tip){
	$("#operInfo").css("display","");
	$("#operInfo").find("#operTip").html(tip);
}

function animateOperTips(tip){
	$("#operInfo").find("#operTip").html(tip);
	animateInAndOut("operInfo", 500, 500, 2000);
}

function hideOperTips(){
	$("#operInfo").css("display","none");
}

/**
 * 统计信号强度
 *  
 */
function staticsSignalStrength(rule)
{
	clearPreState();
	dtSampleContainer.staticsSignalStrength(rule);
}

/**
 * 统计信号质量
 *  
 */
function staticsSignalQuality(rule)
{
	clearPreState();
	dtSampleContainer.staticsSignalQuality(rule);
}

/**
 * 统计信号结构图
 *  
 */
function staticsSignalStructure(rule)
{
	clearPreState();
	dtSampleContainer.staticsSignalStructure(rule);
}

/**
 * 全路段主服务覆盖图
 *  
 */
function staticsPrimaryService(rule)
{
	clearPreState();
	dtSampleContainer.staticsPrimaryService(rule);
}


/**
 * 弱覆盖分析图
 *  
 */
function staticsWeakCoverage(rule)
{
	clearPreState();
	dtSampleContainer.staticsWeakCoverage(rule);
}

/**
 * 无主覆盖分析
 *  
 */
function staticsNoMainCoverage(rule)
{
	clearPreState();
	dtSampleContainer.staticsNoMainCoverage(rule);
}

/**
 * 过覆盖分析
 *  
 */
function staticsOverCoverage(rule)
{
	clearPreState();
	dtSampleContainer.staticsOverCoverage(rule);
}

/**
 * 室分外泄
 */
function analysisIndoorSignalLeakOutside(rule){
	clearPreState();
	dtSampleContainer.staticsIndoorSignalLeakOutside(-80,12,rule);
}

/**
 * 天线方向与覆盖方向不符
 */
function staticsSignalAndAntennaNotMatch(rule){
	clearPreState();
	dtSampleContainer.staticsSignalAndAntennaNotMatch(rule);
}
/**
 * 信号快速衰减分析
 *  
 */
function staticsRapidAttenuation(rule){
　clearPreState();
  dtSampleContainer.staticsRapidAttenuation(rule);
}
function find(value)
{
	for(var i = 0; i < this.length; i++)
	{
		if(value == this[i])
			return true;
	}
	return false;
}

 /**
* 是否为快速衰减：是返回true,不是返回false
* */
/*
 function isNotRapidAttenuation(samplearray){

  var sampleobj=new Array();
  var startsampleTimeStr;
  var sampleTimeStr;
  var startrxLevSub;
  var rxLevSub;
  var initstart=true;
  var flag=true;
	for(var i=0;i<sampleArray.length;i++){
			rxLevSub=sampleArray[i]['rxLevSub'];

				sampleTimeStr=sampleArray[i]['sampleTimeStr'];
				sampleobj.push(sampleArray[i]);

			if(initstart==true){startsampleTimeStr=sampleTimeStr;startrxLevSub=rxLevSub;initstart=false;}
			if(sampleTimeStr>(startsampleTimeStr+dtopts.rapidattenuation[1].second)){
				for(var j=1;j<sampleobj.length;j++){
					if(Math.abs(startrxLevSub-sampleobj[j]['rxLevSub'])<15){
						flag=false;
						break;
					}
				}
				if(flag==true){return true;}
				if(flag==false){initstart=true;}
			}
			
		}
		return flag;

}
*/	
/**
*传入时间字串，和递增秒数获取新的时间
*/
function getNewDateTime(timestr,seconds){

		//var dd="2007-08-31 23:00:00";
		//替换所有-,否则转换时会出错
		var trantimestr=timestr.replace(/[-]/g, "/");
		//console.log(aa);
		var d1=new Date(trantimestr);
		var a=d1.valueOf();//毫秒为单位
		a=a+(seconds*1000);  //由秒转至毫秒可加可減
		a=new Date(a);
		var str=a.getFullYear()+'/'+(a.getMonth()+1)+'/'+a.getDate()+' '
		str+=a.getHours()+':'+a.getMinutes()+':'+a.getSeconds();
		return str;
}
//以下合并
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
function initAreaCascade2() {
	// 区域联动事件
	$("#provincemenu").change(function() {
		getSubAreas("provincemenu", "citymenu", "市");
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
	$(".loading_cover").css("display", "block").css("z-index","10000");
//	$(".loading_fb").html(tip);
	$(".loading_fb").text(tip);
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
	showTips("采样点数据展现");
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
			hideTips("");
			alert("出错啦！" + textstatus);
		},
		complete : function() {
			hideTips("");
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
//	console.log("elementShape===="+elementShape);
//	alert("elementShape===="+elementShape.getSampleArray()[0]['cellRsrp']);
	$('#sampleDetailLi').trigger("click");
	if (currentSampleOccupyMode == sampleOccupyMode_WAIT_F_SAMPLE) {
		hideOperTips();
		gisCellDisplayLib.setDefaultCursor(mapDefaultCursor);// 恢复鼠标为默认
		currentSampleOccupyMode = sampleOccupyMode_ANALYING;
		dtSampleContainer.staticsSingleSampleDetail(elementShape,rendererFactory.getRule('singlesampledetail'));
		currentSampleOccupyMode = sampleOccupyMode_IDLE;
	}
	// alert("点击sample");
	// 展现采样点详情
//	var data = elementShape.getData();
	var data = elementShape;
	if (data) {
		// alert(data.getDetail());
		try {
			// 采样点详情
			var detail = data.getDetail();
//			var detail = data.getSampleArray();
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
				$("#tdCellRsrp").html($.trim(getValidValue(detail['tdCellRsrp'])));
				$("#tdCellRsSinr").html($.trim(getValidValue(detail['tdCellRsSinr'])));

				$("#tdNcells").html(ncells);
				$("#tdNcellRsrps").html(
						$.trim(getValidValue(detail['tdNcellRsrps'])));
				$("#tdServerCellToSampleDis")
						.html(
								$
										.trim(getValidValue(detail['tdServerCellToSampleDis']))+"米");
				$("#tdAreaType").html(
						$.trim(getValidValue(detail['tdAreaType'])));
				$("#tdCellEarfcn").html(
						$.trim(getValidValue(detail['tdCellEarfcn'])));
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
//以下合并
/**
 * 输入的数值必须为正数，如果是小数，最多只能输入两位小数 node是元素 引用方法，在需要验证的input中加入
 * onkeyup="checkNumber(this)"onafterpaste="checkNumber(this)"
 */
function checkNumber(node){
	  var money  = node.value;
	  money=money.replace(/[^.\-1234567890]/g,'');
	  if(money.indexOf('0')==0 &&　money.indexOf('.') != 1 && money.length >= 2){// 小数点前0太多
	       money = money.replace('0','');
	  }
	  if(money.indexOf('0')==0 &&　money.indexOf('.') == -1 && money.length >= 2){// 小数点前0太多
	       money = money.replace('0','');
	  }
	  if(money.indexOf('.') != -1){// 包含小数点
	       var index = money.indexOf('.');
	      // money = money.substring(0,index+3);
	       var backString = money.substring(index+1);
	       // alert(backString);
	       if(backString.indexOf('.') != -1){// 字符串中有第二个小数点
	          money = money.substring(0,index+1)+money.substring(index+1,index+1+backString.indexOf('.'));
	       }else{
	          money = money.substring(0,index+3);
	       }
	  }
	  // ou.jh
	  if(money.indexOf('.') != -1){// 包含小数点
	  		money = money.substring(0,15);
	  }else{
	  		money = money.substring(0,12);
	  }
	  
	  
	  node.value=money;
}


/**
 * 图形化显示某个渲染规则的颜色示例
 * 
 * @param data
 */
function showRendererRuleColor(data,trafficCode,areaId){
   //console.log("in showRendererruleColor .. data=="+data+",areaId="+areaId);
	if(data){
		//console.log("data is valid");
		var context = "";
		if(data.trafficMap){
			// console.log("data has trafficmap");
			//console.log("showRendererRuleColor-data.trafficMap:"+data.trafficMap);
			context = context + "<tr class=\"tb-tr-bg\">"
			                +"    <td style=\"width:15px\">"
			                +"    </td>"
			                +"	<td colspan=\"3\" style=\"width:300px\">"
			                +"    	<span class=\"sp_title\">"+data.trafficMap.NAME+"  图例 </span>"
			                +"    </td>"
			                +"    <td style=\"width:15px\">"
			                +"    </td>"
			                +"</tr>";
			
			  context = context + "<tr class=\"tb-tr-bg-coldwhite\">"
				            +"    <td style=\"width:15px\">"
				            +"    </td>"
				            +"    <td style=\"width:30px\" align=\"center\"><span>图例</span>"
				            +"    </td>"
				            +"    <td align=\"center\"><span>范围</span>"
				            +"    </td>"
				            +"    <td align=\"center\"><span>指标描述</span>"
				            +"    </td>"
				            +"     <td style=\"width:15px\">"
				            +"    </td>"                         
				            +"</tr>";
		}
		if(data.returnList){
			//console.log("data has returnList");
			//console.log("showRendererRuleColor-data.returnList:"+data.returnList); 
			
			try{
			$.each(data.returnList,function(k,v){
					var backgroundcolor = "";
					if(v.STYLE != null && v.STYLE != ""){
						//console.log("style="+v.STYLE);
						var da = eval("("+v.STYLE+")");
						backgroundcolor = da.color;
					}
					context = context + "<tr class=\"tb-tr-bg\"> "
				                        +" 	<td style=\"width:15px\"> "
				                        +"     </td> "
				                        +" 	<td align=\"center\"> "
				                        +"     	<table width=\"20\" height=\"20\"  style=\"background-color:"+backgroundcolor+"\"> "
				                        +"         	<tr > "
				                        +"             	<td style=\" border:1px solid #CCC; \"> "
				                        +"                 </td> "
				                        +"             </tr> "
				                        +"         </table> "
				                        +"     </td> "
				                        +"     <td align=\"center\"> ";
				  if((v.MIN_VALUE==null ||v.MIN_VALUE==undefined) && (v.MAX_VALUE==null || v.MAX_VALUE==undefined)){
					  context = context + "     	<span></span> ";
				  }
				  else if(v.MIN_VALUE == null){
				  		context = context + "     	<span><"+v.MAX_VALUE+"</span> ";
				  }else if(v.MAX_VALUE == null){
				  		context = context + "     	<span>>="+v.MIN_VALUE+"</span> ";
				  }else{
					  	context = context + "     	<span>"+v.MIN_VALUE+"-"+v.MAX_VALUE+"</span> ";
				  }
				  context = context + "     </td> "
				                        +"     <td align=\"center\"> "
				                        +"     	<span><xmp>"+v.RENDERERNAME+"</xmp></span> "
				                        +"     </td> "
						                +"    <td style=\"width:15px\">"
						                +"    </td>"
				                        +" </tr>  ";
			});
			}catch(err){
				//console.error(err);
			}
		}
		 //console.log("1--context==="+context);
		
		var td="<input id='wire_edit' name='' type='button'  value='修改' onclick='showUpdateRnoTrafficRendererPanel(\""+areaId+"\",\""+trafficCode+"\")'  style='width:60px' />";
		//console.log("td=="+td);
		context += " <tr class=\"tb-tr-bg\"> "
                    +"	<td style=\"width:15px\"> "
                    +"    </td> "
                    +"    <td align=\"right\" colspan=\"3\"> "
                    + td
                    +"    </td> "
	                +"    <td style=\"width:15px\">"
	                +"    </td>"
                    +" </tr>    ";

        try{
		fillUpdatePanelWithData(areaId,trafficCode,data);
        }catch(err){
        	//console.log(err);
        }
		$("#trafficTable").html(context);
		$("#analyze_Dialog").css({
		"top" :(40) + "px",
		"right" :(400) + "px",
		"width":(300)+"px",
		"z-index": (30)
		});		
		$("#analyze_Dialog").show();
		
		// 填充修改面板，但暂不显示
		
  }else{
	  //console.warn("data invalid!");
  }

}

/**
 * 填充用于修改的面板
 * 
 * @param areaId
 * @param tafficCode
 * @param data
 */
function fillUpdatePanelWithData(areaId,trafficCode,data){
   //console.log("fillUpdatePanelWithData. areaId="+areaId+",trafficCode="+trafficCode+",data="+data);
	if(data){
		var context = "";
		var divContext = "";
		if(data.trafficMap){
			context = context + "<tr class=\"tb-tr-bg\">"
                           	  +"	<td style=\"width:20px\">"
                              +"    </td>"
                              +"	<td colspan=\"5\" style=\"width:350px\">"
                              +"      <input type=\"hidden\" value=\""+areaId+"\" name=\"areaId\"/>"
                              +"      <input type=\"hidden\" value=\""+trafficCode+"\" id=\"trafficCode\" name=\"trafficCode\"/>"                                                 
                              +"      <span class=\"sp_title\">"+data.trafficMap.NAME+"</span>"
                              +"    </td>"
                              +"</tr>";
		}
		  context = context + "<tr class=\"tb-tr-bg-coldwhite\">"
				            +"    <td style=\"width:15px\">"
				            +"    </td>"
				            +"    <td style=\"width:30px\" align=\"center\"><span>图例</span>"
				            +"    </td>"
				            +"    <td align=\"center\"><span>最小值</span>"
				            +"    </td>"
				            +"    <td align=\"center\"><span>最大值</span>"
				            +"    </td>"
				            +"    <td style=\"width:140px\" align=\"center\"><span>指标描述</span>"
				            +"    </td>"
				            +"     <td style=\"width:15px\">"
				            +"    </td>"                             
				            +"</tr>";
		//保存选项的disableFields选项
		//var disabledFieldsArray = [];
		var disabledFieldsArray = "[";
		if(data.returnList){
			$.each(data.returnList,function(k,v){
					var backgroundcolor = "";
					var style = "";
					if(v.STYLE != null && v.STYLE != ""){
						//var da = eval("("+v.STYLE+")");
						style = v.STYLE;
						backgroundcolor = eval("("+style+")").color;
					}
					var aId = 0;
					if(v.AREA_ID != null && v.AREA_ID != 0 && v.AREA_ID != ""){
						aId = v.AREA_ID;
					}else{
						aId = areaId;
					}
					var minValue = "";
					if(v.MIN_VALUE != null){
						minValue = v.MIN_VALUE;
					}
					var maxValue = "";
					if(v.MAX_VALUE != null){
						maxValue = v.MAX_VALUE;
					}
					var configOrder ="";
					if(v.CONFIG_ORDER != null){
						configOrder = v.CONFIG_ORDER;
					}
					var disabledFields="";
					
					v.DISABLED_FIELDS=getValidValue(v.DISABLED_FIELDS,{});
					if(v.DISABLED_FIELDS!=null && v.DISABLED_FIELDS!=undefined && v.DISABLED_FIELDS!="undefined" && v.DISABLED_FIELDS!="null"){
						try{
							disabledFields=eval("("+v.DISABLED_FIELDS+")");
						}catch(err){
							//console.error(err);
							disabledFields=eval("({'MIN_VALUE':false,'MAX_VALUE':false})");
						}
					}
					var colorDisabled=(true == disabledFields['color']);
					var minvalueDisabled=(true == disabledFields['MIN_VALUE']);
					var maxvalueDisabled=(true == disabledFields['MAX_VALUE']);
					var disabledFieldStr = "{'MIN_VALUE':"+disabledFields['MIN_VALUE']+",'MAX_VALUE':"+disabledFields['MAX_VALUE']+"}";
					//disabledFieldsArray.push(disabledFieldStr);
					if(k<data.returnList.length-1)
					{
						disabledFieldsArray = disabledFieldsArray + disabledFieldStr + ",";
					}
					else
					{
						disabledFieldsArray += disabledFieldStr;
					}
					//params
					var params = "";
					if(v.PARAMS != null && v.PARAMS != ""){
						params = v.PARAMS
					}
					context = context +"<tr class=\"tb-tr-bg\">"
                               	+"<td>"
                                   +"</td>"
                                    +"<td align=\"center\">"
                                    +"      <input type=\"hidden\" style=\"width:80%;\" name=\"rnoTrafficRendererList["+k+"].style\" class=\"colorstyle color"+k+"\" value=\""+style+"\" />"
                                    +"      <input type=\"hidden\" name=\"rnoTrafficRendererList["+k+"].params\" value=\""+params+"\" />"
                                    +"    	<table id=\"colorTable"+k+"\" width=\"20\" height=\"20\" class=\"color"+k+"\""+(colorDisabled==true?"":" onclick=\"farbtasticColor('colorpickerDiv"+k+"','colorpicker"+k+"','colorTable"+k+"');\"")+" id=\"value"+k+"\" style=\"background-color:"+backgroundcolor+"\">"
                                    +"        	<tr >"
                                    +"            	<td style=\" border:1px solid #CCC; \">"
                                    +"              </td>"
                                    +"          </tr>"
                                    +"      </table>"
                                    +"      <input type=\"hidden\" style=\"width:30%;\" name=\"rnoTrafficRendererList["+k+"].trafficRenId\" value=\""+v.TRAFFIC_REN_ID+"\" />"
                                    +"      <input type=\"hidden\" style=\"width:80%;\" name=\"rnoTrafficRendererList["+k+"].trafficType\" value=\""+v.TRAFFIC_TYPE+"\" />"
                                    +"      <input type=\"hidden\" style=\"width:30%;\" name=\"rnoTrafficRendererList["+k+"].areaId\" value=\""+areaId+"\" />"
                                    +"      <input type=\"hidden\" style=\"width:30%;\" name=\"rnoTrafficRendererList["+k+"].configOrder\" value=\""+configOrder+"\" />"
                                    +"      <input type=\"hidden\" style=\"width:30%;\" name=\"rnoTrafficRendererList["+k+"].disabledFields\" value=\""+disabledFieldStr+"\" />"
                                    +"    </td>"
                                    +"    <td align=\"center\">"
                                    +"    	<input type="+(minvalueDisabled==true?"'hidden'":"\"text\"")+" class=\"min_value\" style=\"width:80%;\" name=\"rnoTrafficRendererList["+k+"].minValue\"  onblur=\"checkNumber(this)\" value=\""+minValue+"\" />"
                                    +"    </td>"
                                    +"    <td align=\"center\">"
                                    +"    <input type="+(maxvalueDisabled==true?"'hidden'":"\"text\"")+" class=\"max_value\" style=\"width:80%;\" name=\"rnoTrafficRendererList["+k+"].maxValue\"  onblur=\"checkNumber(this)\" value=\""+maxValue+"\" />"
                                    +"    </td>"
                                    +"    <td align=\"center\">"
                                    //+"    	<span><xmp>"+v.RENDERERNAME+"</xmp></span>"
                                    +"    <input type=\"text\" style=\"width:80%;\" name=\"rnoTrafficRendererList["+k+"].name\" value=\""+v.RENDERERNAME+"\" />"
                                    +"    </td>"
                                    +"    <td>"
                                    +"    </td>"                                  
                                    +"</tr> ";
                 divContext = divContext+"<div id=\"colorpickerDiv"+k+"\" class=\"dialog2 colordialog2\" style=\"display:none; width:410px;\" > "
									+"<div class=\"dialog_header\">"
				                    +"	<div class=\"dialog_title\">图例颜色选择</div>"
				                    +"    <div class=\"dialog_tool\">"
				        	        +"    	<div class=\"dialog_tool_close dialog_closeBtn2\" onclick=\"$('#colorpickerDiv"+k+"').hide();\"></div>"
				    	            +"    </div>"
									+"</div>"
				                    +"        <div class=\"dialog_content\" style=\"width:200px; height:200px; background:#f9f9f9\">"
		                            +"		      <div id=\"colorpicker"+k+"\" style=\"position:absolute;\"></div>"
		                            +"        </div>        "                              
		            				+"</div>";
			});
		}
		disabledFieldsArray +="]";
		  	context = context +"<tr class=\"tb-tr-bg\">"
                              +"  	<td align=\"center\" colspan=\"5\">"
							  +"		<input id=\"save\" name=\"\" type=\"button\"  value=\"保存\" onclick=\"updateRendererRule('rendererForm',"+disabledFieldsArray+");\" style=\"width:60px\" />	" 
							  +"        &nbsp;&nbsp;&nbsp;"
							  +"		<input id=\"close\" name=\"\" type=\"button\"  value=\"取消\"  onclick=\"$('#analyzeedit_Dialog').hide();$('.black').hide();\" style=\"width:60px\" />"	
                              +"    </td>"
                              +"</tr>";
		$("#analyzeedit_trafficTable").html(context);
		$("#divColorDiv").html(divContext);
		
		
	}
}

/**
 * 显示更新面板，面板内容已经在前一步填充
 * 
 * @param areaId
 * @param trafficCode
 */
function showUpdateRnoTrafficRendererPanel(areaId,trafficCode){
	$("#analyzeedit_Dialog").css({
		"top" :(40) + "px",
		"right" :(700) + "px",
		"width":(410)+"px",
		"z-index": (40)
	});		
	$("#analyzeedit_Dialog").show();
	$(".black").show();
	
//	
//var url = "getRnoTrafficRendererAction";
//var params = {areaId:areaId,trafficCode:trafficCode};
//$.ajax({
//type: "POST", // ajax的方式为post(get方式对传送数据长度有限制)
//url: url, // 一般处理程序页面AddUser.ashx(在2中会写出该页面内容)
//cache : false,
//dataType: "json",
//data: params, //
//要传送的数据键值对adduserName为键（方便2中的文件用此名称接受数据）txtuserName为值（要传递的变量，例如用户名）
//success: function (data) {
//if(data){
//var context = "";
//var divContext = "";
//if(data.trafficMap){
//context = context + "<tr class=\"tb-tr-bg\">"
//+" <td style=\"width:20px\">"
//+" </td>"
//+" "
//+" <td colspan=\"5\" style=\"width:350px\">"
//+" <input type=\"hidden\" value=\""+areaId+"\" name=\"areaId\"/>"
//+" <input type=\"hidden\" value=\""+trafficCode+"\" id=\"trafficCode\"
//name=\"trafficCode\"/>"
//+" <span class=\"sp_title\">"+data.trafficMap.NAME+"</span>"
//+" </td>"
//+" </tr>";
//}
//context = context + "<tr class=\"tb-tr-bg-coldwhite\">"
//+" <td style=\"width:20px\">"
//+" </td>"
//+" <td style=\"width:30px\"><span>图例</span>"
//+" </td>"
//+" <td><span>最小值</span>"
//+" </td>"
//+" <td><span>最大值</span>"
//+" </td>"
//+" <td style=\"width:50px\"><span>小区描述</span>"
//+" </td>"
//+" <td style=\"width:10px\">"
//+" </td> "
//+""
//+" </tr>";
//if(data.returnList){
//$.each(data.returnList,function(k,v){
//var backgroundcolor = "";
//if(v.STYLE != null && v.STYLE != ""){
//var da = eval("("+v.STYLE+")");
//backgroundcolor = da.color;
//}
//var aId = 0;
//if(v.AREA_ID != null && v.AREA_ID != 0 && v.AREA_ID != ""){
//aId = v.AREA_ID;
//}else{
//aId = areaId;
//}
//var minValue = "";
//if(v.MIN_VALUE != null){
//minValue = v.MIN_VALUE;
//}
//var maxValue = "";
//if(v.MAX_VALUE != null){
//maxValue = v.MAX_VALUE;
//}
//var configOrder ="";
//if(v.CONFIG_ORDER != null){
//configOrder = v.CONFIG_ORDER;
//}
//var disabledFields={};
//if(v.DISABLED_FIELDS!=null && v.DISABLED_FIELDS!=undefined){
//try{
//disabledFields=eval("("+v.DISABLED_FIELDS+")");
//}catch(err){
//disabledFields={};
//}
//}
//var colorDisabled=(disabledFields['color']==true);
//var minvalueDisabled=(disabledFields['MIN_VALUE']==true);
//var maxvalueDisabled=(disabledFields['MAX_VALUE']==true);
//context = context +"<tr class=\"tb-tr-bg\">"
//+"<td style=\"width:20px\">"
//+"</td>"
//+"<td>"
//+" <input type=\"hidden\" style=\"width:80%;\"
//name=\"rnoTrafficRendererList["+k+"].style\" class=\"colorstyle color"+k+"\"
//value=\""+backgroundcolor+"\" />"
//+" <table width=\"20\" height=\"20\"
//class=\"color"+k+"\""+(colorDisabled==true?"":"
//onclick=\"farbtasticColor('colorpickerDiv"+k+"','colorpicker"+k+"','color"+k+"');\"")+"
//id=\"value"+k+"\" style=\"background-color:"+backgroundcolor+"\">"
//+" <tr >"
//+" <td style=\" border:1px solid #CCC; \">"
//+" </td>"
//+" </tr>"
//+" </table>"
//+" <input type=\"hidden\" style=\"width:30%;\"
//name=\"rnoTrafficRendererList["+k+"].trafficRenId\"
//value=\""+v.TRAFFIC_REN_ID+"\" />"
//+" <input type=\"hidden\" style=\"width:80%;\"
//name=\"rnoTrafficRendererList["+k+"].trafficType\"
//value=\""+v.TRAFFIC_TYPE+"\" />"
//+" <input type=\"hidden\" style=\"width:30%;\"
//name=\"rnoTrafficRendererList["+k+"].areaId\" value=\""+areaId+"\" />"
//+" <input type=\"hidden\" style=\"width:30%;\"
//name=\"rnoTrafficRendererList["+k+"].configOrder\" value=\""+configOrder+"\"
///>"
//+" <input type=\"hidden\" style=\"width:30%;\"
//name=\"rnoTrafficRendererList["+k+"].disabledFields\"
//value=\""+v.DISABLED_FIELDS+"\" />"
//+" </td>"
//+" <td>"
//+" <input type="+(minvalueDisabled==true?"'hidden'":"\"text\"")+"
//class=\"min_value\" style=\"width:80%;\"
//name=\"rnoTrafficRendererList["+k+"].minValue\" onblur=\"checkNumber(this)\"
//value=\""+minValue+"\" />"
//+" </td>"
//+" <td>"
//+" <input type="+(maxvalueDisabled==true?"'hidden'":"\"text\"")+"
//class=\"max_value\" style=\"width:80%;\"
//name=\"rnoTrafficRendererList["+k+"].maxValue\" onblur=\"checkNumber(this)\"
//value=\""+maxValue+"\" />"
//+" </td>"
//+" <td>"
//+" <span>"+v.RENDERERNAME+"</span>"
//+" <input type=\"hidden\" style=\"width:80%;\"
//name=\"rnoTrafficRendererList["+k+"].name\" value=\""+v.RENDERERNAME+"\" />"
//+" </td>"
//+" <td style=\"width:10px\">"
//+" </td>"
//+"</tr> ";
//divContext = divContext+"<div id=\"colorpickerDiv"+k+"\" class=\"dialog2
//colordialog2\" style=\"display:none; width:410px;\" > "
//+"<div class=\"dialog_header\">"
//+" <div class=\"dialog_title\">图例颜色选择</div>"
//+" <div class=\"dialog_tool\">"
//+" <div class=\"dialog_tool_close dialog_closeBtn2\"
//onclick=\"$('#colorpickerDiv"+k+"').hide();\"></div>"
//+" </div>"
//+"</div>"
//+" <div class=\"dialog_content\" style=\"width:200px; height:200px;
//background:#f9f9f9\">"
//+" <div id=\"colorpicker"+k+"\" style=\"position:absolute;\"></div>"
//+" </div> "
//+"</div>";
//});
//}
//context = context + "<tr class=\"tb-tr-bg\">"
//+" <td style=\"width:20px\">"
//+" </td>"
//+" <td>"
//+" </td>"
//+" <td style=\" text-align:center\">"
//+" <input id=\"save\" name=\"\" type=\"button\" value=\"保存\"
//onclick=\"updateRendererRule('rendererForm');\" style=\"width:60px\" /> "
//+" </td>"
//+" <td style=\" text-align:center\">"
//+" <input id=\"close\" name=\"\" type=\"button\" value=\"取消\"
//onclick=\"$('#analyzeedit_Dialog').hide();$('.black').hide();\"
//style=\"width:60px\" />"
//+" </td>"
//+" <td>"
//+" "
//+" </td>"
//+" </tr>";
//$("#analyzeedit_trafficTable").html(context);
//$("#divColorDiv").html(divContext);
// 		
//$("#analyzeedit_Dialog").css({
//"top" :(40) + "px",
//"right" :(700) + "px",
//"width":(410)+"px",
//"z-index": (40)
//});
//$("#analyzeedit_Dialog").show();
//$(".black").show();
//}
//}
//});
	
}

function farbtasticColor(dialogDivId,farbtasticDivId,colorTable){
	$("#"+dialogDivId).css({
		"top" :(40) + "px",
		"right" :(486) + "px",
		"width":(200)+"px",
		"height":(242)+"px",
		"z-index": (40)
		});		
		$(".colordialog2").hide();
		$("#"+dialogDivId).show();
	 $('#'+farbtasticDivId).farbtastic('#'+colorTable);
}

function updateRendererRule(formId,disabledFieldsArray){

//var minNULLCount = 0;
//var maxNULLCount = 0;
var isTrue = 0;

$(".min_value").each(function(k,v)
{
	var minFlag = $(this).val() == null || $(this).val() == "";
	var maxFlag = $(".max_value").eq(k).val() == null || $(".max_value").eq(k).val() == "";
	var minFlagDB = disabledFieldsArray[k]['MIN_VALUE'];
	var maxFlagDB = disabledFieldsArray[k]['MAX_VALUE'];
	//minFlagDB和maxFlagDB同时为false
	if((!minFlagDB)&&(!maxFlagDB))
	{
		//如果只有一行，则最大值和最小值不能同时为空
		if(disabledFieldsArray.length == 1)
		{
			if(minFlag && maxFlag)
			{
				isTrue++;
				alert("最大值和最小值不能同时为空");
				return false;
			}
		}
		else
		{
			//前一项的最小值不能小于后一行的最大值
			if(k < $(".min_value").length-1 && (parseFloat($(this).val()) < parseFloat($(".max_value").eq(k+1).val())))
			{
				isTrue++;
				alert("前一项的最小值不能小于后一行的最大值");
				$(this).focus();
				return false;
			}
			//只能允许整个数组中的最小值为null或""
			if(minFlag && !(k==($(".min_value").length-1)))
			{
				isTrue++;
				alert("中间值不能为空");
				return false;
			}
			//只能允许整个数组中的最大值为null或""
			if(maxFlag && !(k==0))
			{
				isTrue++;
				alert("中间值不能为空");
				return false;
			}
		}
		//如果同一行中最大值和最小值都不为空，且最小值>最大值，则不允许提交
		if((!minFlag) && (!maxFlag) && (parseFloat($(this).val())>parseFloat($(".max_value").eq(k).val())))
		{
			isTrue++;
			alert("最小值不能大于最大值");
			$(this).focus();
			return false;
		}
		
		
	}
});

if(isTrue)
{
	return false;
}

$(".colorstyle").each(function(k,v){
	var text = $(this).val();
	var color = $("#colorTable"+k).css("background-color");
	color = rgbToHex(color);
	//console.log("text: "+text);
	text = eval("(" + text + ")");
	text.color = color;
	text = objectToStyleStr(text);
	$(this).val(text);
});
	$("#"+formId).ajaxSubmit({
		url : 'updateOrAddrnoTrafficRendererAction',
		dataType : 'json',
		success : function(data) {
		// console.log(data);
			//alert(data.message);
			if(data.flag == true){
				$("#analyzeedit_Dialog").hide();
				$(".black").hide();
				$(".colordialog2").hide();
				var trafficCode = $("#trafficCode").val();
				var areaId=$("#"+formId).find("input[name=areaId]").val();
				//console.log("areaid is ="+areaId);
				// 更新颜色图例面板
				rendererFactory.findRule(trafficCode, areaId, showRendererRuleColor,true);
			}
			// 清空所有的缓存规则
			rendererFactory.clearAllRules();
			isRuleChanged=true;
		},
		complete : function() {
		$(".loading_cover").css("display", "none");
		var trafficCode = $("#trafficCode").val();
		var areaId=$("#"+formId).find("input[name=areaId]").val();
		// 更新颜色图例面板
		rendererFactory.findRule(trafficCode, areaId, showRendererRuleColor,true);
		}
	});
}


/**
 * @author Liang YJ
 * @param obj
 * @returns {String}
 * @date 2013.12.26 15:06
 * 描述：将输入的对象转换成json字符串
 */
function objectToStyleStr(obj)
{
	var json = "{";
	for(var key in obj)
	{
			if(key=="color")
			{
				json = json + key + ":'" + obj[key] + "',";
			}
			else
			{					
				json = json + key + ":" + obj[key] + ",";
			}
	}
	json = json.slice(0,json.length-1);
	json += "}";
	return json;
}