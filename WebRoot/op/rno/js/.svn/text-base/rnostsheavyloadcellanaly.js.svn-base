/**
 * 忙小区邻区分析
 */

var gisCellDisplayLib;// 负责显示小区的对象
var map;
var firstTime = true;

var analysisList = new Array();// 分析列表
var allallCellStsResultss = new Object();// 所有的小区信息列表,key是小区名，value是CellStsResult对象
var rendererFactory = new RendererFactory();// 缓存的渲染规则

var currentSelConfigIds = new Array();// 当前选择的用于分析的分析列表
var currentloadtoken = "";// 加载任务的token

var minZoom = 15;// 只有大于 该 缩放级别，才真正
var preZoom = 15;// 当前的缩放级别
var minResponseInterval = 1000;// 对事件做响应的最小的间隔
var lastOperTime = 0;// 最后一次操作的时间
var randomShowCnt = 50;// 在不需要全部显示的时候，最大随机显示的个数

var currentAreaCenter;// 当前所选区域的中心点

var onLoadingGisCell = false;// 是否正在加载小区信息

var lastRendererType = "";// 上一次渲染的指标类型
var lastLoadAreaId=-1;//当前加载了小区的区域id

var heavyLoadCell = new Array();// 忙小区,key为忙小区的cell名称，value为该忙小区的统计情况
var heavyLoadCellNcell = new Array();// 忙小区的邻区，key为忙小区cell名称，value为其邻区的统计情况（列表）
//var txtMenuItem=[];//右键菜单
$(document).ready(
		function() {
			var txtMenuItem=[
 					
						{
						text:'搜索邻小区了',
						callback:function(){
							var polygon;
						if (txtMenuItem.length!=0) {
							polygon=txtMenuItem[txtMenuItem.length-1].polygon;
							txtMenuItem.splice(txtMenuItem.length-1,1);
//							console.log("callback:txtMenuItem回调函数："+txtMenuItem);
							}
//							console.log(polygon._data.getCell());
							responseRightClickForPolygon(polygon,txtMenuItem);
							}
						}
				  ]
			gisCellDisplayLib = new GisCellDisplayLib(map, minZoom,
					randomShowCnt, null, {
						'clickFunction' : clickFunction,
						'mouseoverFunction' : mouseoverFunction,
						'mouseoutFunction' : mouseoutFunction,
						'rightclickFunction':rightclickFunction
					},null);
			var lng = $("#hiddenLng").val();
			var lat = $("#hiddenLat").val();
			map = gisCellDisplayLib.initMap("map_canvas", lng, lat);

			// 区域联动
			initAreaCascade();
			
			//显示小区
			$("#showCellOnMapBtn").on("click",function(){
				
				if($("#areaId").val()==lastLoadAreaId){
					animateInAndOut("operInfo", 500, 500, 1000,"operTip","该区域小区已经加载");
					return;
				}
				
				// 清除旧区域的数据
				clearAll();
				// 获取gis数据数据
				lastLoadAreaId=$("#areaId").val();
				currentloadtoken = getLoadToken();
				getGisCell(currentloadtoken);
			});
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

					$("#areaId").trigger("change");
				});
			});

	$("#areaId").change(function() {
		var lnglat = $("#areaid_" + $("#areaId").val()).val();
		// console.log("改变区域:" + lnglat);
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

function clearAll() {
	// 查询form
	resetQueryCondition();
	// 忙小区相关
	heavyLoadCell.splice(0, heavyLoadCell.length);
	heavyLoadCellNcell.splice(0, heavyLoadCellNcell.length);

	// 地图显示相关
	gisCellDisplayLib.clearData();
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
 * 获取gis小区
 */
function getGisCell(loadToken) {
	if ($("#hiddenCurrentPage").val() == 1) {
		showLoading("正在加载小区...");
	}
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
								// obj = eval("(" + data + ")");
								// alert(obj['celllist']);
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
									} else {
										// 加载忙小区
										hideLoding();
										loadBusyCell(loadToken);
									}
								}
								// 如果没有获取完成，则继续获取
							} catch (err) {
								// console.log("返回的数据有问题:" + err);
								hideLoding();
							}
						},
						error : function(xmh, textstatus, e) {
//							alert("出错啦！" + textstatus);
						},
						complete : function() {
							$(".loading_cover").css("display", "none");
							// hideLoding();
						}
					});
}


function showLoading(tips) {
	$("#operTip").html(tips);
	$("#operInfo").css("display", "block");
}

function hideLoding() {
	$("#operInfo").css("display", "none");
}

/**
 * 获取忙小区
 */
function loadBusyCell(loadToken) {
	// console.log("加载忙小区数据。");
	var areaId = $("#areaId").val();
	if (!areaId) {
		return;
	}

	showLoading("忙小区");

	gisCellDisplayLib.resetPolygonToDefaultOutlook();

	$.ajax({
		url : 'getHeavyLoadCellWithAreaForAjaxAction',
		data : {
			'areaId' : areaId
		},
		dataType : 'json',
		type : 'post',
		success : function(data) {
			showHeavyLoadCell(data);
		},
		complete : function() {
			hideLoding();
		}
	});
}

// 显示忙小区
function showHeavyLoadCell(data) {
	if (!data) {
		return;
	}
	rendererFactory.findRule("overloadcell",$("#areaId").val(),function(rule){
		var opt = null;
		var one = null;
		if (data.length == 0) {
			$("#resource_list_id").html("未发现忙小区。");
			//var html="<a onclick='javascript:loadNcellStsOfCell(\"p1xdk1\");' >paiiyl</a><br/>";
			//html+="<a onclick='javascript:loadNcellStsOfCell(\"p1xdk1\");' >ddddd</a><br/>";
			//$("#resource_list_id").html(html);
		} else {
			var html="";
			for ( var i = 0; i < data.length; i++) {
				opt = createOptionFromStsResult(data[i], rule);
				gisCellDisplayLib.changeCellPolygonOptions(data[i]['cell'], opt);

				// 将忙小区记录起来
				heavyLoadCell[data[i]['cell']] = data[i];

				// 在面板显示
				html+="<a onclick='javascript:loadNcellStsOfCell(\""+data[i]['cell']+"\",this);' >"+getValidValue(data[i]['chineseName'],getValidValue(data[i]['cell'],"未命名"))+"</a><br/>";
			}
			$("#resource_list_id").html(html);
		}
	},false);
	
}

/**
 * 根据统计数值和渲染规则，得到用于地图显示的外观选项
 * 
 * @param stsResult
 * @param rule
 */
function createOptionFromStsResult(stsResult, rule) {

	if (!stsResult || !rule) {
		return null;
	}
	var setting = rule.findRuleSetting(stsResult['maxValue']);
	if (!setting) {
		return null;
	}
	var style = setting.getStyle();
	if (!style) {
		return null;
	}
	var option = new Object();
	option.fillColor = style.color;
	option.fillOpacity = 0;

	return option;
}

/**
 * 获取忙小区的邻区的话统情况
 * 
 * @param cell
 */
function loadNcellStsOfCell(cell,obj) {
	var ncellinfos = heavyLoadCellNcell[cell];
	if (ncellinfos == undefined) {
		var areaId = $("#areaId").val();
		// console.log("loadNcellStsofCell from server....");
		$.ajax({
			url : 'getNcellInfoOfHeavyLoadCellForAjaxAction',
			data : {
				'cell' : cell,
				'areaId' : areaId
			},
			dataType : 'json',
			type : 'post',
			async : false,
			success : function(data) {
				heavyLoadCellNcell[cell] = data;
			}
		});
		ncellinfos = heavyLoadCellNcell[cell];
	}

	showHeavyCellNcell(cell,ncellinfos,obj);
}

// 显示忙小区的邻区情况
//obj：cell所在的a标签
function showHeavyCellNcell(cell,data,obj) {
//	$div=$(obj).parent("div");
	$(".ncellListCls").remove();
	// console.log("showHeavyCellNcell.....");
	//显示主小区的颜色
	gisCellDisplayLib.changeCellPolygonOptions(cell, {'fillColor':'#0B0B61','fillOpacity':0}, true);
	rendererFactory.findRule("radioresourcerate",$("#areaId").val(),function(rule){
		var opt = null;
		var one = null;
		var ncellDivStr="<div class='ncellListCls' style='border:1px dashed;margin:3px'>邻区(数量："+data.length+" 个)<br/>";
		for ( var i = 0; i < data.length; i++) {
			opt = createOptionFromStsResult(data[i], rule);
			gisCellDisplayLib.changeCellPolygonOptions(data[i]['cell'], opt);
			ncellDivStr+="<span style='margin-right:5px'>"+data[i]['cell']+"</span>";
			if((i+1)%3==0){
				ncellDivStr+="<br/>";
			}
		}
		ncellDivStr+="</div>";
		$(ncellDivStr).insertAfter($(obj));
		gisCellDisplayLib.panToCell(cell);
	},false);
	
}

// --------------//
// 点击事件的响应函数
function clickFunction(polygon, cell) {
	// alert("click 。 "+polygon._data.getCell());
	if (!polygon) {
		return;
	}
	if(!cell) {
		var cmk=gisCellDisplayLib.getComposeMarkerByShape(polygon);
		cell = cmk.getCell();
	}

	if (heavyLoadCell[cell]) {
		// 是忙小区
		loadNcellStsOfCell(cell);
	}
}
/**
 * 右键响应函数
 * @param {} polygon
 * @param {} event
 */
function rightclickFunction(polygon,aa) {
	// alert("click 。 "+polygon._data.getCell());
	if (!polygon) {
		return;
	}
	/*console.log("进入rightclickFunction: polygon:"+polygon+" aa:"+aa);
	gisCellDisplayLib.rightClickMenuItemForPolygon(polygon,aa);*/
	
/*	var cell = polygon._data.getCell();
	console.log(cell);*/

				
}
/**
 * 右键菜单项服务小区与邻区间画线及渲染颜色
 * @param {} polygon
 * @param {} txtMenuItem
 */
function responseRightClickForPolygon(polygon,txtMenuItem){

	var cell=polygon._data.getCell();
			// 主覆盖小区颜色
			var option_serverCell={
					'fillColor':'#FCD208'
			};
			//邻区颜色
			var option_ncell={
						'fillColor':'#4CB848'
				};
			gisCellDisplayLib.changeCellPolygonOptions(cell,option_serverCell,true);
			var ncellarr=new Array();
			sendDate={'cell':cell};
//			console.log(cell);
			$(".loading_cover").css("display", "block");
			$.ajax({
				url : 'getNcellforNcellAnalysisOfBusyCellByCellForAjaxAction',
				dataType : 'text',
				data:sendDate,
				type : 'post',
//				async:	false,
				success : function(data) {
				   var mes_obj=eval("("+data+")");
//				    console.log("进入responseRightClickForPolygon:"+mes_obj);
				   for(var key in mes_obj){
//				   	ncellarr.push(mes_obj[key].NCELL);
				   	gisCellDisplayLib.changeCellPolygonOptions(mes_obj[key].NCELL,option_ncell,true);
				   	gisCellDisplayLib.drawLineBetweenCells(cell,mes_obj[key].NCELL,{'strokeColor':'red',"strokeWeight":1});
				   }
				},
				error : function(err, status) {
					//console.error(status);
				},
				complete : function() {
//					console.log("完成responseRightClickForPolygon:");
					$(".loading_cover").css("display", "none");
				}
			});
			 /*var contextMenu = new BMap.ContextMenu();//创建右键菜单实例 
			 if(txtMenuItem!=null){
				 for(var i=0; i < txtMenuItem.length; i++){  
				  contextMenu.addItem(new BMap.MenuItem(txtMenuItem[i].text,txtMenuItem[i].callback,100));  
				 } 
				 if (txtMenuItem.length>0) {
				 	polygon.addContextMenu(contextMenu);
				 }
			 }*/
}
var titleLabels = new Array();
//var offsetSize = new BMap.Size(3, 3);
var offsetSize;
if(typeof gisCellDisplayLib!="undefined")
  offsetSize = gisCellDisplayLib.getOffsetSize(3,3);
// 鼠标移动事件的响应函数
function mouseoverFunction(polygon, event) {
//	var point = new BMap.Point(polygon._data.getLng(), polygon._data.getLat());
//	var label = new BMap.Label(gisCellDisplayLib.getTitleContent(polygon), {
//		'position' : point,
//		'offset' : offsetSize
//	});
	
	var label=new IsTextLabel(polygon._data.getLng(), polygon._data.getLat(),gisCellDisplayLib.getTitleContent(polygon));
	label.setStyle({
		'backgroundColor' : '#A9F5D0',
		'border':'1px solid'
	});
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
