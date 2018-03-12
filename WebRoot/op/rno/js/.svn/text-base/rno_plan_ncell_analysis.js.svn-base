var selectConfig = new Array();// 选择的某种配置
var gisCellDisplayLib;
var map;
var minZoom = 15;// 只有大于 该 缩放级别，才真正
var randomShowCnt = 50;// 在不需要全部显示的时候，最大随机显示的个数
var currentloadtoken = "";// 加载任务的token
var redundantNcellsArray = new Array();// 冗余邻区
var omitNcellsArray = new Array();// 漏定邻区
var multiCellsArray = new Array();//属于主小区、漏定邻区、冗余邻区的其中两种或三种
var serverCellsArray = new Array();//主小区
var selfOption = {'fillColor' : '#66FFFF'};
var normalOption = {'fillColor' : '#00CC00'};
var redundantOption = {'fillColor' : '#A1A1A1'};
var omitOption = {'fillColor' : '#FFFF00'};
var multiOption = {'fillColor':'#FF8C00'};

//Liang YJ 2014-4-24 添加
var currentloadtoken = "";// 加载的token，每次分页加载都要比对。

var redundantLineOpt = {
	'strokeWeight' : 2,
	'strokeColor' : '#A1A1A1',
	'strokeOpacity' : 1
};
var omitLineOpt = {
	'strokeWeight' : 2,
	'strokeColor' : '#FFFF00',
	'strokeOpacity' : 1
};
var normalLineOpt = {
		'strokeWeight' : 2,
		'strokeColor' : '#00CC00',
		'strokeOpacity' : 1	
};

$(document)
		.ready(
				function() {
					
					//显示可以切换的地图
					$("#trigger").val(glomapid=='null' || glomapid=='baidu'?"切换谷歌":"切换百度");
					
					bindNormalEvent();
					
					initAreaCascadeNcellAnalysis();
					
					gisCellDisplayLib = new GisCellDisplayLib(map, minZoom,
							randomShowCnt, null, {
								'clickFunction' : function(py, event) {
									try {
										gisCellDisplayLib.showInfoWindow(
												gisCellDisplayLib
														.getTitleContent(py),
												py.getPath()[0]);
									} catch (err) {
										//console.error(err);
									}
								}
							});
					var lng = $("#hiddenLng").val();
					var lat = $("#hiddenLat").val();
					map = gisCellDisplayLib.initMap("map_canvas", lng, lat);

					// 移除按钮事件
					$(".removebtn").live("click", function() {
						var type = $(this).closest("td").attr("data");
						var id = $(this).closest("td").attr("id");
						// console.log("type="+type+",id="+id);
						removeFromAnalysis(this, type, id);
					});

					// 确定选择按钮事件
					$("#cellConfigConfirmSelectionAnalysisBtn").live("click", function() {
//						var type = $(this).attr("data");
						type="cellConfig";
						selectOneItemFromAnalysis(type);
					});

					// 检查指定小区的邻区情况
					$("#checkNcellForm")
							.validate(
									{
										submitHandler : function(form) {
											if ($("#checkNcellForm").attr(
													"action") == "analysisNcellOfCellForAjaxAction") {
												checkOneCell();
											} else if ($("#checkNcellForm")
													.attr("action") == "analysisAllNcellInWholeNetForAjaxAction") {
												checkWholeNetNcell();
											}
										},
									});
					// 检查某个小区的按钮事件
					$("#checkCellBtn").click(
							function() {
								clearNcellInfo();
								$("#checkNcellForm").attr("action",
										"analysisNcellOfCellForAjaxAction");
								$("#checkNcellForm").submit();
							});

					/**
					 * 全网检查
					 */
					$("#checkWholeNetBtn")
							.click(
									function() {
										clearNcellInfo();
										$("#checkNcellForm")
												.attr("action",
														"analysisAllNcellInWholeNetForAjaxAction");
										$("#checkNcellForm").submit();
									});
					// 小区输入框的获取焦点事件，清空错误提示，如果有错误的话
					$("#serverCell").focus(function() {
						try {
							$("#errorLabelForCell").remove();
						} catch (err) {

						}
					});

					// 清除连线
					$("#clearLinesBtn").click(function() {
						gisCellDisplayLib.clearOnlyExtraOverlay();
					});

					// 清除分析结果
					$("#clearAnalysisResultBtn").click(function() {
						clearNcellInfo();
					});

					$(document).ajaxStart(function() {
						$(".loading_cover").css("display", "block");
					});

					$(document).ajaxComplete(function() {
						$(".loading_cover").css("display", "none");
					});
					//显示数据搜索窗口
					$("#ncs_search").click(function (){
						$("#ncs_Dialog").show();
					});
					$("#handover_search").click(function (){
						$("#handover_Dialog").show();
					});
					$("#structure_search").click(function (){
						$("#structure_Dialog").show();
					});
					//将ncs数据窗口所选记录和ncs分析列表同步
					/*$(".forcheck").each(function (){
						console.log("sb");
						$(this).toggle(function (){
							//选择
							var id = $(this).attr("id");
							var areaName = $(this).parent().children("[name='fileName']").children("span").text();
							var ncsAnalysisList = $("#ncsAnalysisListTable");
							//判断在ncs搜索窗口的所选的选项是否已经在ncs分析列表上
							ncsAnalysisList.children("tr").each(function (i,ele){
								if($(ele).children(":first").children("input").attr("id")===id){
									return;
								}
							});
							var length = ncsAnalysisList.length;
							var html = "";
							if(length%2==0){
								html += "<tr class='tb-tr-bg-warmwhite'>"
							}else{
								html += "<tr class='tb-tr-bg-coldwhite'>"
							}
							html += "<td><input type='hidden' value='"+id+"'/></td>";
							html += "<td><span>"+areaName+"</span></td>";
							html +="<td><input type='button' class='delete_selected_item' value='删除'/></td></tr>";
							ncsAnalysisList.append(html);
						}, function (){
							//取消选择
							var id = $(this).attr("id");
							//判断在ncs搜索窗口的所选的选项是否已经在ncs分析列表上
							ncsAnalysisList.children("tr").each(function (i,ele){
								if($(ele).children(":first").children("input").attr("id")===id){
									$(ele).remove;
								}
							});
						});
					});*/

					//显示或隐藏分析列表
					$(".analysis_list_title").each(function (){
						$(this).click(function (){
							var table = $(this).parent().next("div").children("table");
							if(table.is(":hidden")){
								table.css("display","block");
							}else{
								table.css("display","none");
							}
							
						});
					});
					
					
					//加载小区
					currentloadtoken = getLoadToken();
					getGisCell(currentloadtoken);
				});

/**
 * 区域联动事件
 */
function initAreaCascadeNcellAnalysis() {
	$("#areaId").append("<option value='-1'>全部</option>");
	// 区域联动事件
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
						$("#hiddenAreaLngLatDiv").append(html);
						$("#areaId").append("<option value='-1'>全部</option>");
					}

					$("#areaId").trigger("change");
				});
			});

	$("#areaId").change(function() {
		var lnglat = $("#areaid_" + $("#areaId").val()).val();
		// console.log("改变区域:" + lnglat);
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
		// 重新获取新区域的数据
		currentloadtoken = getLoadToken();
		getGisCell(currentloadtoken);
	});
}

/**
 * 绑定普通事件
 */
function bindNormalEvent() {
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
	// allCellStsResults=null;
	// allCellStsResults=new Object();
	// 清空地图
	// clearMarkCellForFreqReuse();//清除之前频点分布标记
	// 清空地图数据
	// gisCellDisplayLib.clearData();// 清空地图上的东西

}

/**
 * 将选定的一项从分析列表中移除
 * 
 * @obj 触发事件的对象
 * @param type
 *            类型：cellStruct,cellConfig,handover,ncs
 * @param id
 *            id
 */
function removeFromAnalysis(obj, type, id) {
	$.ajax({
		url : 'removeItemFromLoadedListForAjaxAction',
		data : {
			'type' : type,
			"configId" : id
		},
		type : 'post',
		dataType : 'text',
		success : function(data) {

			if (!data) {
				alert("系统问题！请联系管理员。");
				return;
			}

			result = eval("(" + data + ")");
			if (result['flag'] == true) {
				animateInAndOut("operSuc", 500, 500, 2000);
				// alert("删除成功。");
				// alert("tr=="+$(obj).closest("tr").html());
				$(obj).closest("tr").remove();
			} else {
				alert("删除失败！")
			}
		},
		error : function(xhr, status, e) {
			alert("系统出错，请联系管理员！");
		},
		complete : function() {
			// console.log("complete");
		}
	});
	// console.log("quit");
}

/**
 * 选择分析列表里的一个项
 * 
 * @param type
 */
function selectOneItemFromAnalysis(type) {
	// alert("type="+type);
	// alert($("input[type=radio][name="+type+"Radio]").val());
	var rs = $("input[type=radio][name=" + type + "Radio]");
	var id = -1;
	for ( var i = 0; i < rs.length; i++) {
		// alert("i="+i+", checked="+$(rs[i]).attr("checked"));
		if ($(rs[i]).attr("checked") == "checked") {
			id = $(rs[i]).val();
		}
	}
	//alert("id=="+id);
	if (id == -1) {
		alert("请先选择列表项再操作！");
		return;
	}
//	if (selectConfig[type] === id) {
//		// 无变化。
//		// console.log("无变化。");
//		return;
//	}
	$.ajax({
		url : 'selectCheckedAnalysisItemForAjaxAction',
		data : {
			'type' : type,
			"configId" : id
		},
		dataType : 'text',
		type : 'post',
		success : function(data) {
			if (data == null || data == undefined) {
				alert("系统出错！请联系管理员。");
				return;
			}
			try {
				var result = eval("(" + data + ")");
			} catch (err) {
				alert("操作失败！");
				return;
			}
			if (result['flag'] == true) {
				selectConfig[type] = id;
				animateInAndOut("operSuc", 500, 500, 2000);
				// alert("操作成功！");
				if (type === "cellConfig") {
					loadGisCell(id);// 加载小区信息
				}
			} else {
				alert("操作失败！");
			}
		},
		error : function(xhr, status, e) {
			alert("系统出错，请联系管理员！");
		},
	});
}

/**
 * 清除邻区信息
 */
function clearNcellInfo() {
	redundantNcellsArray.splice(0, redundantNcellsArray.length);
	omitNcellsArray.splice(0, omitNcellsArray.length);
	multiCellsArray.length=0;
	serverCellsArray.length=0;

	gisCellDisplayLib.resetSpecPolygonToDefaultOutlook();
	// 连线的清除
	gisCellDisplayLib.clearOnlyExtraOverlay();

	// 邻区面板
	$("#redundantNcellTable tr:not(:first)").each(function(i, ele) {
		$(ele).remove();
	});
	$("#omitNcellTable tr:not(:first)").each(function(i, ele) {
		$(ele).remove();
	});
}

/**
 * 检查指定小区的邻区情况
 */
function checkOneCell() {
	//
	clearNcellInfo();

	// 将特殊渲染的图元颜色改为默认
	gisCellDisplayLib.resetSpecPolygonToDefaultOutlook();

	var cell = $("#serverCell").val();
	cell = $.trim(cell);

	if (cell == "") {
		var label = $("<label class='error' id='errorLabelForCell'>请输入小区名</>");
		$("#serverCell").parent().append(label);
		return;
	}

	var ncsDescIds = new Object();
	var handoverDescIds = new Object();
	var taskId = getAnalysisListMsg(ncsDescIds,handoverDescIds);


	$("#checkNcellForm").ajaxSubmit({
		url : 'analysisNcellOfCellForAjaxAction',
		dataType : 'text',
		type : 'post',
		data : {
			"cell":cell,
			"taskId" : taskId,
			"handoverDescIds" : handoverDescIds,
			"ncsDescIds" : ncsDescIds,
			"cityId" : $("#cityId").val(),
			"areaId": $("#areaId").val()
		},
		success : function(data) {
			var res = null;
			try {
				res = eval("(" + data + ")");
				if (data['flag'] == false) {
					alert("分析失败！" + data['msg'] ? data['msg'] : '');
				} else {
					showKindsAllNcell(res['data'], cell);
				}
			} catch (err) {
				// console.log(err);
				alert("分析失败，请联系管理员。");
			}

		},
		complete : function() {
			// console.log("complete");
		}

	});
}

function getAnalysisListMsg(ncsDescIds,handoverDescIds,taskIds){
	// 获取选择的各分析列表项
	var connector = "_";
	var ncsAnalysisList = $("#ncsAnalysisListTable");
	ncsAnalysisList.find("tr").each(function (index){
		var value = $(this).children("td").eq(0).children("input").val();
		value = value.substring(value.lastIndexOf(connector)+1);
		ncsDescIds[index] = value;
	});

	var handoverAnalysisList = $("#handoverAnalysisListTable");
	handoverAnalysisList.find("tr").each(function (index){
		var value = $(this).children("td").eq(0).children("input").val();
		value = value.substring(value.lastIndexOf(connector)+1);
		handoverDescIds[index] = value;
	});
	
	var taskId = -1;
	var cellStructAnalysisList = $("#cellStructAnalysisListTable");
	if(cellStructAnalysisList.find("tr").length>0){
		taskId = cellStructAnalysisList.find("tr").eq(0).children("td").eq(0).children("input").val();
		taskId = taskId.substring(taskId.lastIndexOf(connector)+1);
	}
	return taskId;
}

/**
 * 检查全网的邻区情况
 */
function checkWholeNetNcell() {
	clearNcellInfo();
	// 将特殊渲染的图元颜色改为默认
	gisCellDisplayLib.resetSpecPolygonToDefaultOutlook();
	
	var ncsDescIds = new Object();
	var handoverDescIds = new Object();
	var taskId = getAnalysisListMsg(ncsDescIds,handoverDescIds);

	$("#checkNcellForm").ajaxSubmit({
		url : 'analysisAllNcellInWholeNetForAjaxAction',
		dataType : 'text',
		type : 'post',
		data : {
			"taskId" : taskId,
			"handoverDescIds" : handoverDescIds,
			"ncsDescIds" : ncsDescIds,
			"cityId" : $("#cityId").val(),
			"areaId": $("#areaId").val()
		},
		success : function(data) {
			var res = null;
			try {
				res = eval("(" + data + ")");
				if (data['flag'] == false) {
					alert("分析失败！" + data['msg'] ? data['msg'] : '');
				} else {
					showKindsAllNcell(res['data']);
				}
			} catch (err) {

			}

		},
		complete : function() {
			// console.log("complete");
		}

	});
}

/**
 * 显示各类型的邻区
 * 
 * @param data
 */
function showKindsAllNcell(data, cell, htmlFlag) {
	// console.log("showKindsAllNcell . data=" + data + ",cell=" + cell);
	if (!data) {
		return;
	}
	var cellFlag = cell != null && cell != undefined;
	if (cellFlag) {
		// 地图移动中心点
		gisCellDisplayLib.panToCell(cell);
		// 开始渲染各类型邻区
		gisCellDisplayLib.changeCellPolygonOptions(cell, selfOption, true);
		// 修改正常邻区的颜色
		var allNcells = data['allNcells'];
		if (allNcells && isArray(allNcells) && allNcells.length > 0) {
		for ( var i = 0; i < allNcells.length; i++) {
				// console.log("正常邻区渲染。。。。。");
				gisCellDisplayLib.changeCellPolygonOptions(allNcells[i]['ncell'],
						normalOption, true);
			}
		}
	}



	// 修改冗余邻区的颜色
	var redundantNcells = data['redundantNcells'];

	// console.log("redundantNcells=="+redundantNcells+",isArray(redundantNcells)="+isArray(redundantNcells)+",redundantNcells.length="+redundantNcells.length);
	if (redundantNcells && isArray(redundantNcells)
			&& redundantNcells.length > 0) {
		// console.log("有冗余小区：")
		for ( var i = 0; i < redundantNcells.length; i++) {
			gisCellDisplayLib.changeCellPolygonOptions(
					redundantNcells[i]['ncell'], redundantOption, true);
			redundantNcellsArray.push(redundantNcells[i]);
			if(cellFlag){
				gisCellDisplayLib.drawLineBetweenCells(cell, redundantNcells[i]['ncell'], redundantLineOpt);
			}

		}
		//fillColor(redundantNcellsArray,redundantNcells,redundantOption,true);
		// 将wholeHtml赋给table
		if(!htmlFlag){	
			var wholeHtml = getNcellResultTableContent(redundantNcells, "redundant");
			$("#redundantNcellTable tr:not(:first)").each(function(i, ele) {
				$(ele).remove();
			});
			$("#redundantNcellTable").append(wholeHtml);
		}
	}
	
	//合理邻区画线
	if (cellFlag) {
		var allNcells = data['allNcells'];
		if (allNcells && isArray(allNcells) && allNcells.length > 0) {
		for ( var i = 0; i < allNcells.length; i++) {
			gisCellDisplayLib.drawLineBetweenCells(cell, allNcells[i]['ncell'], normalLineOpt);
			}
		}
	}
	// 修改漏定邻区的颜色
	var omitNcells = data['omitNcells'];
	if (omitNcells && isArray(omitNcells) && omitNcells.length > 0) {
		for ( var i = 0; i < omitNcells.length; i++) {
			gisCellDisplayLib.changeCellPolygonOptions(omitNcells[i]['ncell'],
					omitOption, true);
			omitNcellsArray.push(omitNcells[i]);
			if(cellFlag){
				gisCellDisplayLib.drawLineBetweenCells(cell, omitNcells[i]['ncell'], omitLineOpt);
			}
		}
		//fillColor(omitNcellsArray,omitNcells,omitOption,true);
		var wholeHtml = getNcellResultTableContent(omitNcells, "omit");
		if(!htmlFlag){
			$("#omitNcellTable tr:not(:first)").each(function(i, ele) {
				$(ele).remove();
			});
			$("#omitNcellTable").append(wholeHtml);
		}
		
	}
	
	//全网分析需要将既属于漏定、冗余、主小区中的两种或三种的小区用橙色区分
	if(!cellFlag){
		//从漏定邻区数据中找出所有主小区
		for(var i=0; i<omitNcells.length; i++){
			var flag = true;
			for(var j=0; j<serverCellsArray.length; j++){
				if(serverCellsArray[j]===omitNcells[i]['cell']){
					flag = false;
					break;
				}
			}
			if(flag){
				serverCellsArray.push(omitNcells[i]['cell']);
			}
		}
		//从冗余邻区数据中找出所有主小区
		for(var i=0; i<redundantNcells.length; i++){
			var flag = true;
			for(var j=0; j<serverCellsArray.length; j++){
				if(serverCellsArray[j]===redundantNcells[i]['cell']){
					flag = false;
					break;
				}
			}
			if(flag){
				serverCellsArray.push(redundantNcells[i]['cell']);
			}
		}
		
		//找出主小区和漏定邻区的交集
		for(var i=0; i<omitNcells.length; i++){
			for(var j=0; j<serverCellsArray.length; j++){
				if(serverCellsArray[j]===omitNcells[i]['ncell']){
					var flag = true
					for(var k=0; k<multiCellsArray.length; k++){
						if(multiCellsArray[k]===serverCellsArray[j]){
							flag = false;
							break;
						}
					}
					if(flag){
						multiCellsArray.push(serverCellsArray[j]);
					}
				}
			}
		}
		//找出主小区和冗余邻区的交集
		for(var i=0; i<redundantNcells.length; i++){
			for(var j=0; j<serverCellsArray.length; j++){
				if(serverCellsArray[j]===redundantNcells[i]['ncell']){
					var flag = true
					for(var k=0; k<multiCellsArray.length; k++){
						if(multiCellsArray[k]===serverCellsArray[j]){
							flag = false;
							break;
						}
					}
					if(flag){
						multiCellsArray.push(serverCellsArray[j]);
					}
				}
			}
		}
		//找出漏定邻区和冗余邻区的交集
		for(var i=0; i<redundantNcells.length; i++){
			for(var j=0; j<omitNcells.length; j++){
				if(omitNcells[j]['ncell']===redundantNcells[i]['ncell']){
					var flag = true
					for(var k=0; k<multiCellsArray.length; k++){
						if(multiCellsArray[k]===omitNcells[j]['ncell']){
							flag = false;
							break;
						}
					}
					if(flag){
						multiCellsArray.push(omitNcells[j]['ncell']);
					}
				}
			}
		}
		//将multi小区渲染成橙色
		for ( var i = 0; i < multiCellsArray.length; i++) {
			gisCellDisplayLib.changeCellPolygonOptions(multiCellsArray[i],multiOption, true);
		}
	}
	
	//内部方法
/*	function fillColor(arr,ncells,option,flag){
			for ( var i = 0; i < ncells.length; i++) {
				// console.log("正常邻区渲染。。。。。");
				gisCellDisplayLib.changeCellPolygonOptions(ncells[i]['ncell'],
						option, flag);
			}
			if(null != arr && undefined != arr && isArray(arr)){
				arr.push(ncells[i])
			}
	}*/

}


function initPageParam(){
	$("#hiddenPageSize").val(100);
	$("#hiddenCurrentPage").val(1);
	$("#hiddenTotalPageCnt").val(0);
	$("#hiddenForcedStartIndex").val(-1);
	$("#hiddenTotalCnt").val(0);
}

/**
 * 加载指定配置下的小区
 */
function loadGisCell(configId) {
	gisCellDisplayLib.clearData();
	initPageParam();
	currentloadtoken = getLoadToken();
	loadGisCellData(currentloadtoken,configId);
}

/**
 * 根据所选择的分析列表加载小区数据
 */
function loadGisCellData(loadToken,configId) {
	onLoadingGisCell = true;
	$("#conditionForm")
			.ajaxSubmit(
					{
						url : 'getFreqReuseCellGisInfoFromSelAnaListForAjaxAction',
						dataType : 'json',
						data : {
							'type' : "CELLDATA",
							'configId':configId
						},
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

									// 下一次的起点
									// currentPage从1开始
									var nextStartIndex = (currentPage - 1)
											* pageSize;
									// console.log("nextStartIndex="+nextStartIndex+",totalCnt="+totalCnt+",currentPage="+currentPage+",pageSize="+pageSize);
									if (totalCnt > nextStartIndex) {
										// console.log("继续获取下一页小区");
										loadGisCellData(loadToken,configId);
									} else {
										onLoadingGisCell = false;
									}
								}
								// 如果没有获取完成，则继续获取
							} catch (err) {
								// console.log("返回的数据有问题:" + err);
								if (loadToken == currentloadtoken) {
									onLoadingGisCell = false;// 终止
								}
							}
						},
						error : function(xmh, textstatus, e) {
							alert("出错啦！" + textstatus);
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
	// 添加到地图
	gisCellDisplayLib.showGisCell(gisCells);
}

/**
 * 获取指定类型的分析列表中选定的项的id
 * 
 * @param type
 */
function getAnalysisItemId(type) {
	var rs = $("input[type=radio][name=" + type + "Radio]");
	var id = -1;
	for ( var i = 0; i < rs.length; i++) {
		if ($(rs[i]).attr("checked") == "checked") {
			id = $(rs[i]).val();
		}
	}
	return id;
}

/**
 * 获取展现需要的表格
 * 
 * @param ncells
 * @returns {String}
 */
function getNcellResultTableContent(ncells, type) {

	var wholeHtml = "", oneHtml = "", currentCell = "", cellTd = "", cellTr = "";
	var ncellTd = new Array();
	var ncellCnt = 0;
	for ( var i = 0; i < ncells.length; i++) {
		// 准备输出到邻区分析结果中
		if (currentCell != ncells[i]['cell']) {
			if (currentCell != "") {
				// console.log("不同主小区时，currentCell=" + currentCell);
				cellTd += " rowspan='" + ncellCnt
						+ "'><a href='javascript:connectNCellByCell(\""
						+ currentCell + "\");' >" + currentCell + "</a></td>";
				cellTr = "<tr>" + cellTd + ncellTd[0] + "</tr>";
				oneHtml += cellTr;
				for ( var jj = 1; jj < ncellTd.length; jj++) {
					oneHtml += "<tr>" + ncellTd[jj] + "</tr>"
				}
				wholeHtml += oneHtml;
			}
			// 新的初始值
			currentCell = ncells[i]['cell'];
			oneHtml = "";
			ncellTd.splice(0, ncellTd.length);

			cellTd = "<td";
			ncellCnt = 1;
			// ncellTd[ncellCnt - 1] = "<td>" + ncells[i]['ncell'] + "</td>";
		} else {
			// console.log("相同时，currentCell=" + currentCell);
			ncellCnt++;
			// console.log("ncellCnt==" + ncellCnt);
		}
		
			ncellTd[ncellCnt - 1] = "<td><a href='javascript:connectCellByNcell(\""
				+ currentCell + "\",\"" + ncells[i]['ncell'] + "\",\"" + type
				+ "\");'>" + ncells[i]['ncell'] + "</a></td>"
				+"<td>"+ncells[i]["detectRatio"].toFixed(5)+"</td>";
			if("redundant"==type){
				ncellTd[ncellCnt - 1] += "<td>"+ncells[i]["hovercnt"].toFixed(1)+"</td>";
			}
			ncellTd[ncellCnt - 1] += "<td>"+ncells[i]["expectedCoverDis"].toFixed(4)+"</td>"
				+"<td>"+ncells[i]["distance"].toFixed(4)+"</td>"
				+"<td>"+ncells[i]["navss"].toFixed(1)+"</td>";
		

	}

	// 最后一个小区的情况
	cellTd += " rowspan='" + ncellCnt
			+ "'><a href='javascript:connectNCellByCell(\"" + currentCell
			+ "\");' >" + currentCell + "</a></td>";
	cellTr = "<tr>" + cellTd + ncellTd[0] + "</tr>";
	oneHtml += cellTr;
	for ( var jj = 1; jj < ncellTd.length; jj++) {
		oneHtml += "<tr>" + ncellTd[jj] + "</tr>"
	}
	wholeHtml += oneHtml;

	return wholeHtml;
}

/**
 * 连接指定的小区另一个小区
 * 
 * @param cell
 * @param ncell
 */
function connectCellByNcell(cell, ncell, type) {
	if (!cell || !ncell) {
		return;
	}
	//gisCellDisplayLib.changeCellPolygonOptions(cell,selfOption,true);
	if (type == "redundant") {
		gisCellDisplayLib.drawLineBetweenCells(cell, ncell, redundantLineOpt);
	} else {
		gisCellDisplayLib.drawLineBetweenCells(cell, ncell, omitLineOpt);
	}
	gisCellDisplayLib.panToCell(cell);
}

/**
 * 连接小区以及其所有的相关小区
 * 
 * @param cell
 */
function connectNCellByCell(cell) {
	if (!cell) {
		return;
	}
/*	gisCellDisplayLib.panToCell(cell);
	//gisCellDisplayLib.changeCellPolygonOptions(cell,selfOption,true);
	for ( var i = 0; i < redundantNcellsArray.length; i++) {
		if (cell == redundantNcellsArray[i]['cell']) {
			gisCellDisplayLib.drawLineBetweenCells(cell,
					redundantNcellsArray[i]['ncell'], redundantLineOpt);
		}
	}
	for ( var i = 0; i < omitNcellsArray.length; i++) {
		if (cell == omitNcellsArray[i]['cell']) {
			gisCellDisplayLib.drawLineBetweenCells(cell,
					omitNcellsArray[i]['ncell'], omitLineOpt);
		}
	}*/
	var ncsDescIds = new Object();
	var handoverDescIds = new Object();
	var taskId = getAnalysisListMsg(ncsDescIds,handoverDescIds);
	//小区恢复原来的颜色
	gisCellDisplayLib.resetSpecPolygonToDefaultOutlook();
	// 连线的清除
	gisCellDisplayLib.clearOnlyExtraOverlay();

	$("#checkNcellForm").ajaxSubmit({
		url : 'analysisNcellOfCellForAjaxAction',
		dataType : 'text',
		type : 'post',
		data : {
			"cell":cell,
			"taskId" : taskId,
			"handoverDescIds" : handoverDescIds,
			"ncsDescIds" : ncsDescIds,
			"cityId" : $("#cityId").val(),
			"areaId": $("#areaId").val()
		},
		success : function(data) {
			var res = null;
			try {
				res = eval("(" + data + ")");
				if (data['flag'] == false) {
					alert("分析失败！" + data['msg'] ? data['msg'] : '');
				} else {
					showKindsAllNcell(res['data'], cell, true);
				}
			} catch (err) {
				// console.log(err);
				alert("分析失败，请联系管理员。");
			}

		},
		complete : function() {
			// console.log("complete");
		}

	});
	
}


/**
 * @author Liang YJ
 * @date 2014-4-23 11:12
 * @param obj
 * @description 点击删除按钮，在分析列表中删除按钮对应的记录
 */
function deleteSelectedItem(obj){
	var deleteBtn = $(obj);
	//判断查询列表是否有改选
	var id = deleteBtn.parent().parent().children("td").eq(0).find("input").val();
	$("#"+id).attr("checked",false);
    deleteBtn.parent().parent().remove();
}


/**
 * 获取gis小区
 * 邻区分析整改，通过地图导航栏加载小区
 * 2014-4-24 18:41
 * Liang YJ
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


//重置查询表单的值
/**
 * 邻区分析整改，通过地图导航栏加载小区
 * 2014-4-24 18:41
 * Liang YJ
 */
function resetQueryCondition() {
	$("#hiddenCurrentPage").val(1);
	$("#hiddenTotalPageCnt").val(0);
	$("#hiddenTotalCnt").val(0);
}

//取消div拖动事件冒泡
function stopParentDrag(e){
	console.log("事件："+e);
	e.stopPropagation();
}



