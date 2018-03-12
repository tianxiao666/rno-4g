var $ = jQuery.noConflict();

var gisCellList = new Array();
var ncsDetailList = new Array();

var rendererFactory = new RendererFactory(false);// 缓存的渲染规则
var ncsOpts; //保存渲染规则
var centerLng = '113.381580000000000000';
var centerLat = '22.922940000000000500';
var hasCenter=false;

var currentloadtoken = "";// 加载的token，每次分页加载都要比对。

var currentAreaCenter;// 当前所选区域的中心点

var renderProgressHandle = null;   //渲染进度查询控制

var gisCellDisplayLib;
var map;
var minZoom = 17;// 只有大于 该 缩放级别，才真正
var randomShowCnt = 150;// 在不需要全部显示的时候，最大随机显示的个数
var preZoom = 15;// 当前的缩放级别
var minResponseInterval = 1000;// 对事件做响应的最小的间隔
var lastOperTime = 0;// 最后一次操作的时间

$(document).ready(function() {	
		
		// 隐藏详情面板
		hideDetailDiv();
	
		//初始化地图
		gisCellDisplayLib = new GisCellDisplayLib(map, minZoom, randomShowCnt,
				null, {
					//'clickFunction' : null,
					'mouseoverFunction' : mouseoverFunction,
					'mouseoutFunction' : mouseoutFunction
					//'rightclickFunction':null
				}, {'showCellLabel' : false},"", preZoom);
		map = gisCellDisplayLib.initMap("map_canvas",0,0,{
										'movestart' : handleMovestartAndZoomstart,
										'zoomstart' : handleMovestartAndZoomstart,
										'moveend' : handleMoveendAndZoomend,
										'zoomend' : handleMoveendAndZoomend });		
					
		//以网格形式在地图加载小区
		mapGridLib = new MapGridLib(gisCellDisplayLib,"conditionForm","loadingCellTip"); 
		
		//以城市为单位创建区域网格
		var cityName = $("#cityId2").find("option:selected").text().trim();
		mapGridLib.createMapGrids(cityName);	
		//获取cityId赋值到表单隐藏域
		var cityId = $("#cityId2").val();
		$("#cityId").val(cityId);
		
		// 获取gis数据数据
		currentloadtoken = getLoadToken();
			
		//创建自定义图层
		gisCellDisplayLib.createGroudOverLay();	
		
		//加载小区事件
		$("#loadgiscell").click(function(){
		 	
		 	//getGisCell(currentloadtoken);
		 	
		 	/******* peng.jm 2014-9-29 网格形式加载小区  start ********/	
		 	//清除缓存数据
		 	clearAll();
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
			//t-minResponseInterval保证不延迟加载
			mapGridLib.loadGisCell(t-minResponseInterval, currentloadtoken, minResponseInterval);
		 	/******* peng.jm 2014-9-29 网格形式加载小区  end ********/	
		 });
	
		//渲染进度窗
		$("#renderProgressDiv").css({
			"top" :(58) + "%",
			"width":(300)+"px",
			"left":(39)+"%",
			"position" : "absolute",
			"z-index": (31)
			});	
			
		//分析窗
		$("#analyze_Dialog").css({
			"top" :(267) + "px",
			"right" :(400) + "px",
			"width":(300)+"px",
			"left":(40)+"px",
			"z-index": (30)
		});	
});

// 隐藏详情面板
function hideDetailDiv() {

	$("a.siwtch").hide();
	$(".switch_hidden").show();
	$(".resource_list_icon").animate({
		right : '0px'
	}, 'fast');
	$(".resource_list_box").hide("fast");
}

//显示专题图事件
function displayRenderImg() {
	var value = $("#ncsRendererType").val();
	//console.log("选择了："+value);
	//加载渲染规则
	//showRenderer(value);
	showRendererRuleColor();
	//加载渲染图
	loadRenderImage();
}

/**
 * 加载区域渲染图片
 */
function loadRenderImage(){

		var flag = confirm("是否加载渲染图？");
		if (flag == false){
		  return false;
		}
		
		var taskId = $("#reportNcsTaskId").val();
		var ncsRendererType=$("#ncsRendererType").val();
		
		showOperTips("loadingRenderDiv", "loadContentId", "正在加载渲染图");
		
		$.ajax({
			url : 'getRenderImgByParamAndTaskIdForAjaxAction',
			data : {
				'ncsRendererType' : ncsRendererType,
				'taskId' : taskId
			},
			dataType : 'text',
			type : 'post',
			success : function(raw) {
				data = eval("(" + raw + ")");
				var flag = data['flag'];
				if(flag == "imgExisted") {
					//console.log("对应的渲染图存在");
					showRenderImage();
				} 
				if(flag == "imgTaskExisted" || flag == "imgNotExisted") {

					//轮询渲染进度
					checkRenderTask();//保证第一次显示就有值
					renderProgressHandle = setInterval(function(){checkRenderTask();},5000);
					$("#renderProgressDiv").show();
					showOperTips("createRenderDiv", "createContentId", "正在生成渲染图...");
				}

				if(flag == "error") {
					console.log("没有对应的任务");
				}
			},
			complete : function() {
				//$("#renderProgressDiv").hide();
				hideOperTips("loadingRenderDiv");
			}
		});	
		return true;
}

//查询渲染进度
function checkRenderTask() {

	$("span#progressDesc").html("");
	$("span#threadProgress").html("");
	var taskId = $("#reportNcsTaskId").val();
	var ncsRendererType=$("#ncsRendererType").val();
	
	$.ajax({
		url : 'queryRenderProgressAjaxForAction',
		data : {
			'ncsRendererType' : ncsRendererType,
			'taskId' : taskId
		},
		dataType : 'text',
		type : 'post',
		success : function(raw) {
		
			//console.log(raw);	
			var data = eval("("+raw+")");
			var flag = data['flag'];
			if(flag) {
				var status = data['status'];
				var progressDesc = data['progressDesc'];
				var spendTime = data['spendTime']
				//渲染任务处理中的操作
				if(status == "working") {
					if(progressDesc == "图形数据处理中....") {
						$("span#progressDesc").html(progressDesc + "  估计约需：" + spendTime + "min");
						var progress = parseInt(data['threadProgress1']) + parseInt(data['threadProgress2']);
						$("span#threadProgress").html("完成进度：" + progress + "%");
					} else {
						$("span#progressDesc").html(progressDesc + "  估计约需：" + spendTime + "min");
					}
				}
				//渲染任务处于完成阶段的操作
				if(status == "finish") {
					showRenderImage();
					$("#renderProgressDiv").hide();
					hideOperTips("createRenderDiv");
				}
			} else {
				//渲染任务被清除了的操作
				showRenderImage();
				$("#renderProgressDiv").hide();
				hideOperTips("createRenderDiv");
				window.clearInterval(renderProgressHandle);
			}
		}
	});
}

/**
 * 在前台显示渲染图
 */
function showRenderImage() {
		var map1 = gisCellDisplayLib.getMap();
		var jobId = $("#reportNcsTaskId").val();
		var ncsRendererType = $("#ncsRendererType").val();
		
		$.ajax({
			url : 'getRenderImgDetailByjobIdAjaxForAction',
			data : {
				'jobId' : jobId,
				'ncsRendererType' : ncsRendererType
			},
			dataType : 'text',
			type : 'post',
			success : function(raw) {
				//console.log(raw);	
				var data = eval("("+raw+")");
				var flag = data['flag'];
				if(flag){
					var filePath = data['filePath'];//全路径
					filePath=getRootPath()+filePath;
//					var relaPath = filePath.substr(filePath.indexOf("ops")+3,filePath.length);
//					//console.log(relaPath);
//					var imgPath = getRootPath() + relaPath + "image/" + jobId + "_" + ncsRendererType + ".png";
//					imgPath = imgPath.replace(/\\/g, "/"); //替换\为/
					//console.log(imgPath);

					var swLng = data['minLng'];
					var swLat = data['minLat'];
					var neLng = data['maxLng'];
					var neLat = data['maxLat'];
					//console.log("swLng="+swLng+",swLat="+swLat+",neLng="+neLng+",neLat="+neLat);
					//设置图层大小，图片
					gisCellDisplayLib.setGroudOverLayOpt(swLng,swLat,neLng,neLat,filePath);	

				} else {
					alert("渲染图显示出错！");
				}
			}
		});
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
 * 图形化显示某个渲染规则的颜色示例
 * 
 * @param data
 */
function showRendererRuleColor(){
   
    var rendererTypeName = $("#ncsRendererType").find("option:selected").text();
   	var taskId = $("#reportNcsTaskId").val();
	var ncsRendererType=$("#ncsRendererType").val();
    //console.log(name);
    var context = "";

    $.ajax({
		url : 'getRenderColorRuleAjaxForAction',
		data : {
			'ncsRendererType' : ncsRendererType,
			'taskId' : taskId,
		},
		dataType : 'text',
		type : 'post',
		success : function(raw) {
		
			if(raw){
			
				var data = eval("("+raw+")");
				if(data.length > 0) {
					context += "<tr class=\"tb-tr-bg\">"
						            +"    <td style=\"width:15px\">"
						            +"    </td>"
						            +"	<td colspan=\"3\" style=\"width:300px\">"
						            +"    	<span class=\"sp_title\">"+rendererTypeName+"  图例 </span>"
						            +"    </td>"
						            +"    <td style=\"width:15px\">"
						            +"    </td>"
						            +"</tr>";	
					context += "<tr class=\"tb-tr-bg-coldwhite\">"
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
							
					var one = null;
					for(var i = data.length - 1; i >= 0; i--) {
			
						one = data[i];
						
						context += "<tr class=\"tb-tr-bg\"> "
					         		        +" 	<td style=\"width:15px\"> "
					                        +"     </td> "
					                        +" 	<td align=\"center\"> "
					                        +"     	<table width=\"20\" height=\"20\"  style=\"background-color:"+one['startColor']+"\"> "
					                        +"         	<tr > "
					                        +"             	<td style=\" border:1px solid #CCC; \"> "
					                        +"                 </td> "
					                        +"             </tr> "
					                        +"         </table> "
					                        +"     </td> "
					                        +"     <td align=\"center\"> ";
					  	if((one['minVal']==null || one['minVal']==undefined) 
					  			&& (one['maxVal']==null || one['maxVal']==undefined)){
						  	context +=  "     	<span></span> ";
					 	}
					  	else if(i == (data.length - 1)){
					  		context += "     	<span>>="+one['minVal']+"</span> ";
					  	}else{
						  	context += "     	<span>"+one['minVal']+"-"+one['maxVal']+"</span> ";
					  	}
					  	context += "     </td> "
				                      +"     <td align=\"center\"> "
				                      +"     	<span><xmp>"+one['minVal']+"</xmp></span> "
				                      +"     </td> "
				                +"    <td style=\"width:15px\">"
				                +"    </td>"
				                      +" </tr>  ";
					}
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
							    
					$("#trafficTable").html(context);
					$("#analyze_Dialog").show();
				}
				
			}
		
		}
	});
	// 填充修改面板，但暂不显示
}

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

	mapGridLib.loadGisCell(lastOperTime, currentloadtoken, minResponseInterval);
}

function handleMovestartAndZoomstart(e, lastOperTime) {
	//每一次移动，缩放的独立标识
	currentloadtoken = getLoadToken();
	mapGridLib.setCurrentloadtoken(currentloadtoken);
}


// 清除全部的数据
function clearAll() {
	try {
		gisCellDisplayLib.clearOverlays();
	} catch (e) {

	}
	gisCellDisplayLib.clearData();

}

var titleLabels = new Array();
//var offsetSize = new BMap.Size(3, 3);
var offsetSize;
if(typeof gisCellDisplayLib!="undefined")
offsetSize = gisCellDisplayLib.getOffsetSize(3, 3);
// 鼠标移动事件的响应函数
function mouseoverFunction(polygon, event) {
	var cmk=gisCellDisplayLib.getComposeMarkerByShape(polygon);
	var point = gisCellDisplayLib.createPoint(cmk.getLng(),cmk.getLat());
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

/*************************** 以下是地图覆盖渲染图方式 ，不使用**************************************/
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
	}
	
}


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

/*************************** 地图覆盖渲染图方式 ，不使用**********************************/
