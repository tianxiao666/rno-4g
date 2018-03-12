var $ = jQuery.noConflict();
var gisCellDisplayLib;// 负责显示小区的对象

var map;
var cellDetails = new Array();// 小区详情

var gisCellList = new Array();
var ncsDetailList = new Array();

var rendererFactory = new RendererFactory(false);// 缓存的渲染规则
var ncsOpts; //保存渲染规则
var centerLng = '113.381580000000000000';
var centerLat = '22.922940000000000500';
var hasCenter=false;
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
	initMap();
// 	$("#ncsRendererType").change(function(){
// 		//console.log("进入ncsRendererType");
// 		//清空覆盖物
// 		gisCellDisplayLib.clearOverlays();
// 		//加载图片
//// 		loadAreaRenderImage();
// 	});
});
/**
 * 加载区域渲染图片
 */
function loadAreaRenderImage(){
	  	 //console.log(document.readyState);
		 //if(document.readyState=="complete"){  
		var ncsRendererType=$("#ncsRendererType").val();
		var imagename="/op/rno/ana_result/image/"+ncsRendererType+"_"+ncsTaskId+".png";
		//console.log(getRootPath()+imagename);
		var imagepath=getRootPath()+imagename;
		imageLayerCover(imagepath,baiduBoundary);
    		//}  
}
/**
 * 初始化地图
 */
function initMap() {
    //console.log("进入初始化地图函数  initmap");
    hasLoadMap=true;
	gisCellDisplayLib = new GisCellDisplayLib(map, minZoom, randomShowCnt,
			{'singleColor':'#ffffff','multiColor':'#ffffff'}, {
				'clickFunction' : getCellStructAndDetail,
				'mouseoverFunction' : null,
				'mouseoutFunction' : null
			});
	/*var lng = $("#hiddenLng").val();
	var lat = $("#hiddenLat").val();*/
	lng = centerLng;
	lat = centerLat;
	gisCellDisplayLib.initMap("map_canvas", lng, lat, {
		"tilesloaded" : function() {
			bindEvent();
			// 加载数据
			//currentloadtoken = getLoadToken();
			//getNcsCell(currentloadtoken);
			//console.log("baiduBoundary.length:"+baiduBoundary.length);
//			 if(baiduBoundary.length==4){
//			 	loadAreaRenderImage();
//			 }
		}
	});

	// 创建信息窗口
	gisCellDisplayLib.createInfoWindow("");
	
	//container = DtSampleContainer(map, minZoom, null, null, null);
}

/**
 * 鼠标点击扇形的响应函数
 * @param polygon
 * @param event
 */
function getCellStructAndDetail(polygon,event){
	//获取缓存的结构指标数据
	if(polygon instanceof BMap.Polygon){
		var cmk = polygon._data;
		//console.log(cmk);
		if (cmk.getCount() > 1) {
			scatterAll(polygon);
		} else {
			showCellDetailFromPolygon(polygon);
		}
	}else{ 
		showCellDetail(polygon.getId());
	}
	//var cellres=analysisResult['cellres'];
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
		gisCellDisplayLib.panTo(lls[0], lls[1]);
		// 重置查询表单的数据
		resetQueryCondition();
		// 重新获取新区域的数据
		// 重新获取新区域的数据
		/*currentloadtoken = getLoadToken();
		getNcsCell(currentloadtoken);*/

	});
	
	//根据ncs_id查询小区的信息
	$("#queryNcsCell").click(function()
	{
		//console.log("点击了："+$(this).val());
		gisCellDisplayLib.clearData();
		currentloadtoken = getLoadToken();
		getNcsCell(currentloadtoken);
	});
	
	//根据选择的参数返回渲染规则
	$("#ncsRendererType").change(function()
	{
		var value = $("#ncsRendererType").val();
		//console.log("选择了："+value);
		showRenderer(value);
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
	
	gisCellDisplayLib.clearData();

}

function resetQueryCondition() {
	$("#hiddenCurrentPage").val(1);
	$("#hiddenTotalPageCnt").val(0);
	$("#hiddenTotalCnt").val(0);
	$("#hiddenForcedStartIndex").val(-1);
	$("#hiddenPageSize").val(100);

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
	var cmk=gisCellDisplayLib.getComposeMarkerByShape(polygon);
	var point=gisCellDisplayLib.createPoint(cmk.getLng(),cmk.getLat());
	gisCellDisplayLib.showInfoWindow(html, point);
	/*gisCellDisplayLib.showInfoWindow(html, new BMap.Point(new Number(
			polygon._data.getLng()), new Number(polygon._data.getLat())));*/

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
	var ncsDetail = null;
	var gisCell = null;
	var find = false;
	for(var i=0; i<ncsDetailList.length; i++)
	{
		ncsDetail = ncsDetailList[i];
		if(ncsDetail['cell'] == label)
		{
			break;
		}
	}

	
	
	for ( var i = 0; i < cellDetails.length; i++) {
		gisCell = cellDetails[i];
		if (gisCell['label'] === label) {
			// console.log("从缓存找到详情。")
			find = true;
			break;
		}
	}

	if (find) {
		displayCellDetail(ncsDetail,gisCell);
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
			gisCell = data;
			// try {
			// //c = eval("(" + data + ")");
			// } catch (e) {
			// alert("获取小区详情失败！");
			// return;
			// }
			if (gisCell) {
				cellDetails.push(gisCell);
				displayCellDetail(ncsDetail,gisCell);
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
function displayCellDetail(ncsDetail,gisCell) {

	$(".switch_hidden").trigger("click");

	//结构指标信息显示
	$("#showCellId").html(getValidValue(ncsDetail.cell));
	$("#showFreqCntId").html(getValidValue(ncsDetail.FREQ_CNT,"",0));

	$("#showBeInterferId").html(getValidValue(ncsDetail.BE_INTERFER,"",6));
	$("#showSrcInterferId").html(getValidValue(ncsDetail.SRC_INTERFER,"",6));
	$("#showNetStructFactorId").html(getValidValue(ncsDetail.NET_STRUCT_FACTOR,"",6));

	$("#showReduntCoverFactId").html(getValidValue(ncsDetail.REDUNT_COVER_FACT,"",6));
	$("#showOverlapCoverId").html(getValidValue(ncsDetail.OVERLAP_COVER,"",6));
	$("#showExpectedCoverDisId").html(getValidValue(ncsDetail.EXPECTED_COVER_DIS,"",6));
	$("#showOvershootingFactId").html(getValidValue(ncsDetail.OVERSHOOTING_FACT,"",6));

	$("#showDetectCntId").html(getValidValue(ncsDetail.DETECT_CNT,"",0));
	$("#showCellCoverId").html(getValidValue(ncsDetail.CELL_COVER,"",6));
	$("#showCapacityDestroyId").html(getValidValue(ncsDetail.CAPACITY_DESTROY,"",6));
	$("#showInterferNcellCntId").html(getValidValue(ncsDetail.CAPACITY_DESTROY,"",6));
	
	
	//小区信息显示
	$("#showCellLabelId").html(getValidValue(gisCell.label));
	$("#showCellNameId").html(getValidValue(gisCell.name));
	$("#showCellLacId").html(getValidValue(gisCell.lac));
	$("#showCellCiId").html(getValidValue(gisCell.ci));
	$("#showCellBcchId").html(getValidValue(gisCell.bcch));

	$("#showCellTchId").html(getValidValue(gisCell.tch));
	$("#showCellBsicId").html(getValidValue(gisCell.bsic));
	$("#showCellAzimuthId").html(getValidValue(gisCell.bearing));

	$("#showCellDownId").html(getValidValue(gisCell.downtilt));
	$("#showCellBtsTypeId").html(getValidValue(gisCell.btstype));
	$("#showCellAntHeightId").html(getValidValue(gisCell.antHeigh));
	$("#showCellAntTypeId").html(getValidValue(gisCell.antType));

	$("#showCellLngId").html(getValidValue(gisCell.longitude));
	$("#showCellLatId").html(getValidValue(gisCell.latitude));

	$("#showCellCoverareaId").html(getValidValue(gisCell.coverarea));
	$("#showCellFreqSectionId").html(getValidValue(gisCell.gsmfrequencesection));

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
				
				//var td="<input id='wire_edit' name='' type='button'  value='修改' onclick='showUpdateRnoTrafficRendererPanel(\""+areaId+"\",\""+trafficCode+"\")'  style='width:60px' />";
				var td = "";
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
       /* try{
		fillUpdatePanelWithData(areaId,trafficCode,data);
        }catch(err){
        	//console.log(err);
        }*/
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
 * 在地图上显示渲染效果
 * @param data
 */
function showStuctDataOnMap(data,callback){
	var obj = data;
	for(var i=0; i<obj.length; i++)
	{
		var gisCell = {cell:obj[i]['cell'],chineseName:obj[i]['chineseName'],lng:obj[i]['lng'],lat:obj[i]['lat'],allLngLats:obj[i]['allLngLats']};
		//console.log(gisCell.toString());
		gisCellList.push(gisCell);
		var ncsDetail = obj[i];//{cell:obj[i]['cell'],ncsId:obj[i]['NCS_ID'],BE_INTERFER:obj[i]['BE_INTERFER'],NET_STRUCT_FACTOR:obj[i]['NET_STRUCT_FACTOR']};
		ncsDetailList.push(ncsDetail);
	}
	
	//移动地图
	if(hasCenter==false){
		hasCenter = true;
		centerLng = gisCellList[0].lng;
		centerLat = gisCellList[0].lat;
		var i=1;
		while((centerLng=='' || centerLat=='') && i<gisCellList.length){
			centerLng = gisCellList[i++].lng;
			centerLat = gisCellList[i++].lat;
		}
		if(centerLng!='' && centerLat!=''){
			gisCellDisplayLib.panTo(centerLng, centerLat);
		}
	}
	
	//console.log("------begin showCell on map :"+gisCellList.toString());
	gisCellDisplayLib.showGisCell(gisCellList);
	//console.log("------end show map");
	
	if(typeof callback ==="function"){
		callback();
	}
	
}


/**
 * 获取NCS小区信息
 * @author Liang YJ
 * @date 2014-2-20 17:31
 */
function getNcsCell(loadToken) {
	$("#conditionForm").ajaxSubmit(
			{
				url : 'getNcsCellByPageForAjaxAction',
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
						//gisCellDisplayLib.showGisCell(obj['gisCells']);
						//分离gis小区和ncs信息
						//select rncar.NCS_ID as ncsId, c.LABEL as cell, c.NAME as chineseName, c.LONGITUDE as lng, c.LATITUDE as lat, c.LNGLATS as allLngLats, rncar.FREQ_CNT as FreqCnt, rncar.BE_INTERFER as beInterfer, rncar.NET_STRUCT_FACTOR as netStructFactor,rncar.SRC_INTERFER as srcInterfer, rncar.REDUNT_COVER_FACT as reduntCoverFact, rncar.OVERLAP_COVER as overlapCover, rncar.EXPECTED_COVER_DIS as expectedCoverDis, rncar.OVERSHOOTING_FACT as overshootingFact, rncar.DETECT_CNT as detectCnt, rncar.CELL_COVER as cellCover, rncar.EXPECTED_CAPACITY as expectedCapacity, rncar.CAPACITY_DESTROY_FACT as capacityDestroyFact,rncar.CAPACITY_DESTROY as capacityDestroy from RNO_NCS_CELL_ANA_RESULT rncar inner join CELL c on c.label = rncar.CELL where rncar.NCS_ID = ?";
						for(var i=0; i<obj.ncsCellList.length; i++)
						{
							var gisCell = {cell:obj.ncsCellList[i]['cell'],chineseName:obj.ncsCellList[i]['chineseName'],lng:obj.ncsCellList[i]['lng'],lat:obj.ncsCellList[i]['lat'],allLngLats:obj.ncsCellList[i]['allLngLats']};
							gisCellList.push(gisCell);
							var ncsDetail = {cell:obj.ncsCellList[i]['cell'],ncsId:obj.ncsCellList[i]['ncsDescId'],beInterfer:obj.ncsCellList[i]['beInterfer'],netStructFactor:obj.ncsCellList[i]['netStructFactor']};
							ncsDetailList.push(ncsDetail);
						}
						gisCellDisplayLib.showGisCell(gisCellList);
						
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
								getNcsCell(loadToken);
							}
						}
						// 如果没有获取完成，则继续获取
//						alert(currentPage);
						if(currentPage==2 && 0 != gisCellList.length)
						{
							centerLng = gisCellList[0].lng;
							centerLat = gisCellList[0].lat;
							gisCellDisplayLib.panTo(centerLng,centerLat);
						}
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
 * 显示渲染结果
 * @author Liang YJ
 * @date 2014-2-20 17:31
 */
function showRenderer(type)
{
	
	areaId = null;
	rendererFactory.findRule(type, areaId, function(rule, ruleCode, areaId) {
		ncsOpts = rule.getRuleSettings();
		// 图形化显示
		showRendererRuleColor(rule.rawData, ruleCode, areaId);
		lastRendererType = type;
		
		ncsFactorStatistics(type);

	}, false);
	
	
}

/**
 * 根据统计字段设置渲染方式
 * @author Liang YJ
 * @date 2014-2-20 17:31
 */
function ncsFactorStatistics(code)
{
	//ncsDetailList
	//gisCellList
	//ncsOpts
	//alert("code="+code+",ncsDetailList.length="+ncsDetailList.length+",ncsOpts.length="+ncsOpts.length);
	for(var i=0; i < ncsDetailList.length; i++)
	{
		var ncsDetail = ncsDetailList[i];
		var color="#FFFFFF";// 无效点
		var value=parseFloat(ncsDetail[code]);
//		console.log("yuanshi="+ncsDetail[code]+",value="+value);
		for(var j=0; j<ncsOpts.length; j++)
		{
			if(value>parseFloat(ncsOpts[j].minValue) && value <= parseFloat(ncsOpts[j].maxValue))
			{
				color = ncsOpts[j].style.color;
				//console.log("找到颜色：j="+j+","+ncsOpts[j].style.color);
				break;
			}
			
		}
//		if(color=='#FFFFFF'){
//			console.log("未找到合适颜色!");
//		}
		var option = {fillColor:color};
//		console.log("第"+(i+1)+"个数据["+ncsDetail[code]+"]：option="+color);
		//container.changeMapElementOutlook(gisCellList[i],option);
		gisCellDisplayLib.changeCellPolygonOptions(ncsDetail.cell,option,false);
	}
	
}
    /**
     * js获取项目根路径，如： http://localhost:8083/uimcardprj  
     * @return {}
     */
    function getRootPath(){  
        //获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp  
        var curWwwPath=window.document.location.href;  
        //获取主机地址之后的目录，如： uimcardprj/share/meun.jsp  
        var pathName=window.document.location.pathname;  
        var pos=curWwwPath.indexOf(pathName);  
        //获取主机地址，如： http://localhost:8083  
        var localhostPaht=curWwwPath.substring(0,pos);  
        //获取带"/"的项目名，如：/uimcardprj  
        var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);  
        return(localhostPaht+projectName);  
    }  
    /**
     * 图片图层覆盖地图区域
     * @param {} url 图片地址
     * @param {} boundary 左上右下区域
     */
    function imageLayerCover(url,boundary){
    	
    	try{
    		// 创建 GroundOverlay
    	var ge=gisCellDisplayLib.ge;
		var groundOverlay = ge.createGroundOverlay('');
		var highlightStyle = ge.createStyle('');
		
		// 指定图片路径，并将其分配至GroundOverlay
		var icon = ge.createIcon('');
		//icon.setHref("http://www.google.com/logos/earthday08.gif");
		//广州
		//icon.setHref("http://localhost:8080/JSMapAPI/img/image.png");
		//珠海
		//icon.setHref("http://localhost:8080/ops/op/rno/ana_result/image/BE_INTERFER_119.png");
		console.log("url:"+url);
		icon.setHref(url);
		highlightStyle.getIconStyle().setIcon(icon);
		highlightStyle.getIconStyle().getColor().setA(0);
		//icon.setRefreshMode(ge.REFRESH_ON_CHANGE);

		groundOverlay.setIcon(icon);
		groundOverlay.setOpacity(0.8);
		// 指定地理位置
		var latLonBox = ge.createLatLonBox('');
		//N,S,W,E
		//latLonBox.setBox(22.239389, 22.089389, 113.236365, 113.436365, 0);
		//广州
		//latLonBox.setBox(23.935576, 22.521906, 112.968521, 114.065814, 0);
		//珠海
		//latLonBox.setBox(22.460216, 21.785928, 113.067677, 114.417799, 0);
		latLonBox.setBox(Number(boundary[1]), Number(boundary[3]),Number(boundary[0]), Number(boundary[2]), 0);
		groundOverlay.setLatLonBox(latLonBox);
		// 向Google地球添加GroundOverlay
		ge.getFeatures().appendChild(groundOverlay);
		gisCellDisplayLib.panTo(boundary[0],boundary[1]);
    	}catch(e){
    		console.log(e);
    	}
		
	}
