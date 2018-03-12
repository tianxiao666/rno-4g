var infoWindow = null;
var taskPolylineArray = new Array();
var polylineMarkers = new Array();
var locusBeginTime = null;
var locusEndTime = null;
var selectInfoWindow = null;
var infowindow = null;			//marker信息窗
var clickLng;
var clickLat;
var clickPoint;
var progressX;
var progressY;
var status;
var statusArray = new Array();
var pResult = {};
var oTime = 0;
var setInter;
var timeG = 80000;
var timeGC = 80;
var ogpsList = new Array();
var isDown = false;
$(function(){
	var latlng = new ILatLng(23.14651977,113.34106554);
	infoWindow=new IInfoWindow("",{"position":latlng});
	$("#search").click(function(){
		clearInterval(setInter);
		isSearch=true;
		if(infoWindow!=null){
			infoWindow.close();
		}
		$("#hiddenWoId").val("");//清空隐藏域WOID的值
		getCarLocus();
	});
	preLocusTime=$("#locusBeginTime").val();
	
	$("#start").click(start);
	$("#pause").click(stop);
	$("#finish").click(finish);
	$("#back").click(moveBack);
	$("#forward").click(moveForward);
	getGPSLoactionByCarNumber();
	setInterval("updateGPSLoactionByCarNumber()", 10000); 
	
	
	$('.jpp').mousedown(function(e) 
	{
		isDown = true;
		clearInterval(setInter);
		$('.jpp').mousemove(function(e) 
		{
		if(isDown){
			progressX = $('#progressSpan').offset().left;
			startX = progressX;
			
			var clickX = e.pageX;
			
			progress = clickX - startX;
			
			if(progress<0)
			{
				progress = -4;
			}else if(progress > 425){
				progress = 425;
			}
			$('#tcspan').css("margin-left",progress+"px");
			var wi =  parseFloat($("#progressSpan").css("width").substr(0,$("#progressSpan").css("width").length - 2));
			indextime = startTime + parseInt(progress/wi*sumtime);
			oTime = 0;
			getindext(indextime);
		}
//		//计算要回放的点
//		play = Math.ceil(clickPointArray.length * (progress/100));
//		//点击进度条后车子在地图显示位置
//		playCar.setPosition(pointArray[play]);
//		
//		//时间遮罩层随车子移动
//		timeTextOverlay(pointArray[play]);
		});
	});
	
	$('.jpp').mouseup(function(e) 
	{
		isDown = false;
		progressX = $('#progressSpan').offset().left;
		getindextime(indextime);
	});
});

var marker = null;
function getGPSLoactionByCarNumber(){
	var queryCarNumber = $("#queryCarNumber").val();
	var param = {};
	param.carNumber = queryCarNumber;
	if(marker != null){
		marker.setMap(null);
		//marker.textOverlay_.setMap(null);
	}
	$.ajax( {
		url : "cardispatchManage!getGPSLoactionByCarNumber.action",
		type: "post",
		data: param,
		dataType : "json",
		async : true,
		success : function(result) {
		if(result){
			//alert(result.jingdu + "   "  + result.weidu);
			var lng = parseFloat(result.jingdu);
			var lat = parseFloat(result.weidu);
			var latlng = new ILatLng(lat, lng);
			var mapLatLng = IMapComm.gpsToMapLatLng(latlng);
			marker = new IMarker(mapLatLng, result.carNumber, {"map":map});
			//var markerOptions = {};
			//markerOptions.icon = "icon-for-map/nowCarGPS.png";
			//marker.setOptions(markerOptions);
			var pos = marker.getPosition();
			//var statusLabel = "<lable style='color: #000000;font-size: 12px;font-weight: bold;'>车辆当前位置</lable>";
   			//marker.textOverlay_ = new ITextOverlay(pos,statusLabel,{});
			marker.bizValues_ = result;
			marker.setAnimation(BMAP_ANIMATION_BOUNCE);
			var labelStyleData = {
									 color : "#000000",
									 fontSize : "12px",
									 height : "20px",
									 lineHeight : "20px",
									 fontWeight:"900",
									 fontFamily:"微软雅黑",
									 border:"0px",
									 background:"transparent"
								 };
			marker.setLabel(-27,-35,"车辆当前位置",labelStyleData);
			marker.setMap(map);
			//marker.textOverlay_.setMap(map);
			//bindMarker(marker);
			//marker.setMap(map);
		}
		}
	});
}

function updateGPSLoactionByCarNumber(){
	var queryCarNumber = $("#queryCarNumber").val();
	var param = {};
	param.carNumber = queryCarNumber;
	$.ajax( {
		url : "cardispatchManage!getGPSLoactionByCarNumber.action",
		type: "post",
		data: param,
		dataType : "json",
		async : true,
		success : function(result) {
			if(result){
				//alert(result.jingdu + "   "  + result.weidu);
				var lng = parseFloat(result.jingdu);
				var lat = parseFloat(result.weidu);
				var latlng = new ILatLng(lat, lng);
				var mapLatLng = IMapComm.gpsToMapLatLng(latlng);
				marker.setPosition(mapLatLng);
				//marker.setMap(map);
			}
		}
	});
}



var isStop=false;
var isSearch=false;
var totalTime=new Date();
var currentTxt;




function getCarNumber(){
	//自动补全
	var values={
		"carNumber":$("#queryCarNumber").val()
	}
	return false;
}
var map;
var polylineArray=new Array();
var polylineDashedArray=new Array();
var markerArray=new Array();


//获取时间区间内出车任务列表
function getTaskList(){
	$("#loading_div").css("display","block");
	var beginTime=$("#locusDate").val()+" 00:00:00";
	var	endTime=$("#locusDate").val()+" 23:59:59";	
	var hiddenWoId = $("#hiddenWoId").val();
	var queryCarNumber = $("#queryCarNumber").val();
	if(hiddenWoId && hiddenWoId!=""){
		beginTime = locusBeginTime;
		endTime = locusEndTime;
	}
	var values={
		"workorder#startTime":beginTime,
		"workorder#endTime":endTime,
		"workorder#carNumber":queryCarNumber,
		"workorder#state":"returnedCar"
	}
	$.ajax({
		url:"cardispatchWorkorder!findCardispatchWordorderByState.action",
		type:"post",
		data:values,
		dataType:"json",
		async:false,
		success:function(result){
		   	var htmlString="";
		   	if(result!=""&&result){
			  	 $("#taskDiv").css("display","block");
			   	 var data = result;
			   	 var carTotalMileage = 0;
			   	 var carTaskMileage = 0;
			   	 var carGpsMileage = 0;
		   		 for(var i=0;i<data.length;i++){
		   		 	var beginLat = data[i].beginLat;
		   		 	var beginLng = data[i].beginLng;
		   		 	var endLat = data[i].endLat;
		   		 	var endLng = data[i].endLng;	 
		   		 	var woId = data[i].woId;
		   		 	var totalMileage = data[i].totalMileage;
					var gpsMileage = data[i].totalgpsMileage;
					
					if(!totalMileage){totalMileage=0;}
					if(!gpsMileage){gpsMileage=0;}
					
					gpsMileage = stringToFloat(gpsMileage);
					totalMileage = stringToFloat(totalMileage);
					
					carTotalMileage+=totalMileage;
					carTaskMileage+=totalMileage;
				   	carGpsMileage+=gpsMileage;
				   	
					htmlString += "<tr><td><input checked='checked' class='taskCheckBox' type='checkbox' value=\""+woId+"\" onclick=\"showRwCarLocus('"+woId+"',this)\"/></td>";
					htmlString += "<td><a href='#'>"+woId+"</a></td><td>"+totalMileage+"KM</td><td>"+gpsMileage+"</td></tr>";
				 }
				 htmlString += "<tr><td>总计</td><td></td><td>"+carTotalMileage+"</td><td>"+carGpsMileage+"</td></tr>";
			}else{
				//$("#taskDiv").css("display","none");
			}
			$("#taskTable").find("tr:gt(0)").remove();
		   	$(htmlString).appendTo($("#taskTable"));
		   	$("#loading_div").css("display","none");
		   	//显示全部任务轨迹信息
		   	showAllTaskLocus();
		}
	})
	$("#loading_div").css("display","none");
}
function showAllTaskLocus(){
	$(".taskCheckBox").each(function(index){
		var woId = $(this).val();
		showRwCarLocus(woId,this);
	});
}
/*分时段显示轨迹*/
function showRwCarLocus(woId,be){
	var carNumber = $("#queryCarNumber").val();
	var bTime = $(be).attr("btime");
	var eTime = $(be).attr("etime");
	var lbTime = bTime;
	var leTime = eTime;
	var locusDate = $("#locusDate").val();
	bTime = locusDate+" "+bTime;
	eTime = locusDate+" "+eTime;
	$.ajax( {
		url : "getGpsInfoForCarLocusAction",
		type: "post",
		data: {woId:woId,carNumber:carNumber,beginTime:bTime,endTime:eTime},
		dataType : "json",
		async : false,
		success : function(res) {
			if(res){		
				bTime = res.bPickTime;
				eTime = res.ePickTime;
			}
			else{
				
			}
		}
	});
	if($(be).attr("checked") == "checked"){

		
		if(taskPolylineArray){
			for(var i=0;i<taskPolylineArray.length;i++){
				var polyline = taskPolylineArray[i];
				if(polyline.bTime_==bTime && polyline.eTime_==eTime){
					polyline.setMap(map);
					return false;
				}
			}
		}
		var taskPath = new Array();
		if(pointArray){
			var isBegin = false;
			for(var i=0;i<pointArray.length;i++){
				var latlng = pointArray[i];
				if(bTime == latlng.pickTime_){
					isBegin = true;
				}
				if(isBegin){
					taskPath.push(latlng);
				}
				if(eTime == latlng.pickTime_){
					if(leTime != "23:59:59"){
						break;
					}
				}
			}
		}
		//判断是否有数据
		if(taskPath.length>1){
			var option={"map":map};
			var strokeWeight=4;
			option.strokeOpacity=0.6;
			var polyline = new IPolyline(taskPath,"red",strokeWeight,option);
			polyline.woId_ = woId;
			polyline.bTime_ = bTime;
			polyline.eTime_ = eTime;
			taskPolylineArray.push(polyline);
		}
	}else{
		//消除轨迹
		if(taskPolylineArray){
			for(var i=0;i<taskPolylineArray.length;i++){
				var polyline = taskPolylineArray[i];
				if(polyline.bTime_==bTime && polyline.eTime_==eTime){
					polyline.setMap(null);
					break;
				}
			}
		}
	}
}
/**
 * 清除任务单车辆轨迹
 */
function cleanTaskPolylineArray()
{
	for(var i=0;i<taskPolylineArray.length;i++){
		var polyline = taskPolylineArray[i];
		if(polyline){
			polyline.setMap(null);
		}
	}
	taskPolylineArray = new Array();
}


/**
 * 
 * 切换时清除属性值
 * 
 */
function cleanValue()
{
	$('#zehEm').html(0);
	$('#nehEm').html(0);
	$('#tthEm').html(0);
	$('#fehEm').html(0);
	$('#nthEm').html(0);
	
	$("#progressTime").html("0:00:00");
	
//	$('#progressDiv').css("width","0%");
	$("#replayDiv_locusDate").text("");
	$('.start_time').html("00:00:00");
	
	$('.end_time').html("00:00:00");
}


/**
 * 获取车辆位置信息,并描绘轨迹在地图上
 * 
 */
function getCarLocus() {
	if(autoRefresh!=null){
		//清除定时器
		clearInterval(autoRefresh);
		autoRefresh = null;
	}
	$("#loading_div").css("display","block");
	//先清除车辆查询结果
	cleanQueryResult();
	//清除任务单车辆轨迹
	cleanTaskPolylineArray();
	cleanMarkersAndPolyline();
	//切换时清除属性值
	cleanValue();
	
	//播放器移动
	dragAndDrop.Register(replayDiv,replay_title);
	
//	dragAndDrop.Register(haha,tcspan);
	
	
	
	$("#start").show();
	$("#pause").hide();
	//获取车辆当前位置
	getGPSLoactionByCarNumber();
	var beginTime=$("#locusDate").val()+" 00:00:00";
	var endTime=$("#locusDate").val()+" 23:59:59";	
	var queryCarNumber = $("#queryCarNumber").val();
	
	var orgId = $("#gisBizId").val();
	var param = {};
	param.carNumber = queryCarNumber;
	var hiddenWoId = $("#hiddenWoId").val();
	if(hiddenWoId && hiddenWoId!=""){
		$.ajax( {
			url : "getGpsInfoForCarLocusAction",
			type:"post",
			data:{carNumber:queryCarNumber,woId:hiddenWoId},
			dataType : "json",
			async : false,
			success : function(res) {
				if(res){
					beginTime = res.lbTime;
					endTime = res.leTime;
					locusBeginTime = beginTime;
					locusEndTime = endTime;
				}
			}
		});
	}
	
	$(".title_content em:eq(1)").text(queryCarNumber);
	$(".title_content em:eq(3)").text($("#locusDate").val());
	
	param.beginTime = beginTime;
	param.endTime = endTime;
	param.orgId = orgId;
	$.ajax( {
		url : "cardispatchManage!getCarGpsInTime.action",
		type: "post",
		data: param,
		dataType : "json",
		async : false,
		success : function(result) {
			$("#loading_div").css("display","none");
			if(result)
			{
				var car = result.mostNewGPSLocation;
				var gpsCount = result.gpsList;
				
				if(typeof(car)=="undefined" || gpsCount == 0 || gpsCount=="undefined")
				{
					$("#replayDiv").css("display","none");
					$.dialog({
						time: 3300,
						content:'所查时间段或该组织内没有本车的轨迹数据，请修改查询时段或组织',
						title:'error'
					});
					$(".gis_content_right_main:last ul:last").empty();
					$(".gis_content_right_main:last ul:last").append("暂无数据");
					$("#ssPageContent").empty();
					map.closeInfoWindow();
				}
				else
				{
					//车辆轨迹生成
					creatMarkers(result);	
					//车辆状态记录数据获取
					getCarStatusRecord(result);
					//修改播放器开始结束时间
    				modStartAndEndTime();
				}
			}else{
				$("#replayDiv").css("display","none");
				$.dialog({
					time:3300,
					content:'所查时间段或该组织内没有本车的轨迹数据，请修改查询时段或组织',
					title:'error'
					});
					
				$(".gis_content_right_main:last ul:last").empty();
				$(".gis_content_right_main:last ul:last").append("暂无数据");
				$("#ssPageContent").empty();
				map.closeInfoWindow();
				
			}
		}
	});
	
	
	
	
	if($("#queryCarNumber").val()!=null && $("#queryCarNumber").val()!=""){
		getTaskList();
	}
	$("#loading_div").css("display","none");
	isSearch=false;
}
var carLocusList = null;
var clickPointArray = new Array();

var gpsList = null;


/**
 * 
 * 获取车辆状态记录
 * 
 * @author Li.hb
 * @date 2013-08-16
 * 
 * @param gisResult 数据包集
 * 
 */
function getCarStatusRecord(gisResult)
{
	
	if(gpsList!=gisResult.gpsList)
	{
		gpsList = gisResult.gpsList;
		
		var oldPickTime = null;
		var gpslatlng = null;
		statusArray = new Array();
		var queryCarNumber = $("#queryCarNumber").val();
		
		var time = $("#locusDate").val();
		
		var beginTime=time+" 00:00:00";
		var endTime=time+" 23:59:59";	
		
		var oldlat =null;
		var oldlng = null;
		
		var isZeroTime = false;
		
		var num = 0;
		
		if(gpsList)
		{
			for(var i=0;i<=gpsList.length;i++)
			{
				var gps = null;
				var lat = null;
				var lng = null;
				var pickTime = null;
				

				if(i==gpsList.length)
				{
					lat = oldlat;
					lng = oldlng;
					
					//判断最早的一条数据是否是0时0分
					if(oldPickTime.getHours() == 0 && oldPickTime.getMinutes() == 0)
					{
						pickTime = oldPickTime;
						isZeroTime = true;
					}
					else
					{
						pickTime =  new Date(Date.parse(beginTime.replace(/-/g,"/")));
					}
					
				}
				else
				{
					gps = gpsList[gpsList.length-1-i];
					lat = gps.weidu;
					lng = gps.jingdu;
					pickTime = new Date(Date.parse(gps.pickTime.replace(/-/g,"/")));
				}
				
				
				lat = stringToFloat(""+lat);
				lng = stringToFloat(""+lng);
				
				
				if(oldPickTime==null)
				{
					if(new Date(Date.parse(endTime.replace(/-/g,"/"))).getTime() > new Date().getTime())
					{
						oldPickTime = new Date();
					}
					else
					{
						oldPickTime = new Date(Date.parse(endTime.replace(/-/g,"/")));
					}
					
					
				}
				
				if(oldPickTime!=null)
				{
					var minutes = (oldPickTime.getTime() - pickTime.getTime())/(60*1000);
				
					var latLng = {};
					
					
					if(latlng2==null)
					{
						var latlng2 = {};
					}
					
					
					if(minutes>3 || i==gpsList.length)
					{
						
						if(num > 0)
						{
							latlng2.lat_ = oldlat;
							latlng2.lng_ = oldlng;
							latlng2.epicktime_ = latlng2.picktime_;
							latlng2.picktime_ = oldPickTime;
							latlng2.minutes_ = (latlng2.epicktime_.getTime() - latlng2.picktime_.getTime())/(60*1000);
							statusArray.push(latlng2);
							latlng2 = null;
							num = 0;
						}
						
						if(isZeroTime)
						{
							continue;
						}
						
						latLng.lat_ = lat;
						latLng.lng_ = lng;
						latLng.picktime_ = pickTime;
						latLng.epicktime_ = oldPickTime;
						latLng.carNumber = queryCarNumber;	
						latLng.minutes_ = minutes;
						latLng.status = 0;
						
						statusArray.push(latLng);
					}
					else
					{
						if(num==0)
						{
							latlng2.elat_ = lat;
							latlng2.elng_ = lng;
							latlng2.picktime_ = oldPickTime;
							latlng2.carNumber = queryCarNumber;
							latlng2.status = 1;
							num++;
						}
					}
					
				}
				
				oldPickTime = pickTime;
				oldlat = lat;
				oldlng = lng;
				
			}
			calculateCarStatus(statusArray);
		}
		
		
	}
	
}

/**
 * 
 * 根据心跳包计算车辆状态，并填充HTML
 * 
 * @author Li.hb
 * @date 2013-08-16
 * @param statusArray 各种状态数组
 * 
 */
function calculateCarStatus(statusArray)
{
	var queryCarNumber = $("#queryCarNumber").val();
	var beginTime=$("#locusDate").val()+" 00:00:00";
	var endTime=$("#locusDate").val()+" 23:59:59";	
	var param = {};
	
	param.carNumber = queryCarNumber;
	param.beginTime = beginTime;
	param.endTime = endTime;
	
	var hTime = null;
	var status = "";
	var hours = null;
	var minute = null;
	var seconds = null;
	var background = null;
	var latLng = null;
	
	var latlngArray = new Array();
	
	var ul = $(".gis_content_right_main:last ul:last");
			
	ul.empty();
	
	$.ajax( {
		url : "cardispatchManage!getCarHeartbeat.action",
		type: "post",
		data: param,
		dataType : "json",
		async : false,
		success : function(result) {
			
			var heartbeat = result.Heartbeat;
			
			for(var i =0;i<statusArray.length;i++)
			{
				var count = 0;
				
				latLng = statusArray[statusArray.length-1-i];
				
				var bPickTime = latLng.picktime_;
				
				var minutes = latLng.minutes_;
				
				var ePickTime = latLng.epicktime_;
				
				
				if(latLng.status!=1)
				{
					$.each(heartbeat,function(k,v){
				
						hTime = new Date(Date.parse(v.CREATE_TIME.replace(/-/g,"/")));
						
						if(bPickTime.getTime() < hTime.getTime() && ePickTime.getTime() > hTime.getTime() )
						{
							++count;
							if(count>=Math.floor(minutes/5))
							{
								latLng.status = 2;
							}
						}
					})				
				}
				
				
				
				if(latLng.status == 0)
				{
					status = "离线";
					background = "lineoff";
				}
				else if(latLng.status == 2)
				{
					status = "静止";
					background = "static";
				}
				else if(latLng.status == 1)
				{
					status = "行驶";
					background = "travel";
				}
				
				bHours = latLng.picktime_.getHours();
				
				bMinute = latLng.picktime_.getMinutes();
				
				bSeconds = latLng.picktime_.getSeconds();
				
				eHours = ePickTime.getHours();
				
				eMinute = ePickTime.getMinutes();
				
				eSeconds = ePickTime.getSeconds();
				
				if(bHours<10)
				{
					bHours = "0"+bHours;
				}
				if(bMinute<10)
				{
					bMinute = "0"+bMinute;
				}
				if(bSeconds<10)
				{
					bSeconds = "0"+bSeconds;
				}
				
				if(eHours<10)
				{
					eHours = "0"+eHours;
				}
				if(eMinute<10)
				{
					eMinute = "0"+eMinute;
				}
				if(eSeconds<10)
				{
					eSeconds = "0"+eSeconds;
				}
				
				
				var longitude = latLng.lng_;
		
				var latitude = latLng.lat_;
				
				var latlng = new ILatLng(latitude, longitude);
	
				//转换经纬度
				latlng = IMapComm.gpsToMapLatLng(latlng);
				
				latlngArray.push(latlng);
				
				var id = ""+bHours+""+bMinute+""+bSeconds+eHours+""+eMinute+""+eSeconds;
				
				var li = "<li onclick=\"clickCarStatus("+latlng.getLatitude()+","+latlng.getLongitude()+",'"+id+"');\" class=\"resource_item clearfix "+background+"\"style=\"display: list-item;\" title="+status+"><div class=\"r\"><p class=\"name mt5\"><em style=\"color:blue;\">"+bHours+":"+bMinute+":"+bSeconds+"</em><em style=\"color:blue;\"> - "+eHours+":"+eMinute+":"+eSeconds+"</em><em style=\"color:grey;padding-left:7px;\">"+status+"</em><em class=\"name_red\" style=\"padding-left:5px;\"><i class=\"red\">"+Math.round(minutes)+"</i> 分钟</em></p><p class=\"address\" id=\""+id+"\" name="+id+"><img src='images/newLoading.gif'></p></div></li>"
				
				$(ul).append(li);	
				
				var params = {ak:"844f1e1721c48a98b2c8dd39fee35a78",output:"json",pois:"0",location:+latlng.getLatitude()+","+latlng.getLongitude()};

				setAddress(id,params);
			}
			//分页
			pagingColumnByForeground("ssPageContent",$(".gis_content_right_main:last ul:last li"),8);
		}
	});

}	


/**
 * 根据经纬度获取百度地址，并写入
 * 
 * @date 2013-08-17
 * @author li.hb
 * @param {} id  
 * @param {} params
 */
function setAddress(id,params)
{
	var address = null;
	
	$.ajax( {
			url : "http://api.map.baidu.com/geocoder/v2/",
			type: "get",
			async:false,
			data: params,
			dataType : "jsonp",
			success : function(data) 
			{
				address = data.result.formatted_address;
				$("#"+id).text(address);
			}
	});
}


/**
 * 
 * 根据经纬度在地图中弹出提示框
 * 根据ID 获取车辆状态记录的内容
 * 
 * @author Li.hb
 * @date 2013-08-15
 * @param {} lat 纬度
 * @param {} lng 经度
 * @param {} id  车辆状态 ul标签ID
 */
function clickCarStatus(lat,lng,id)
{
	var latlng = new ILatLng(lat, lng);
	
	map.panTo(latlng);
	
	var gpslatlng = new BMap.Point(latlng.getLongitude(),latlng.getLatitude());
	
	var opts ={height: 85,width:230};		     // 信息窗口高度

	var address = $("p[name='"+id+"']").text();
	
	var p = $("p[name='"+id+"']").prev().children();
	
	var btime = p.eq(0).text();
	
	var etime = p.eq(1).text();
	
	var status = p.eq(2).text();
	
	var mi = p.eq(3).find("i").text();
	
	var pl = 0;
	
	var hours = 0;
	
	var minutes = 0;
	
	var seconds = 0;
	
	var clickTime = null;
	
	clickTime = new Date(Date.parse(($("#locusDate").val() +" "+ btime).replace(/-/g,"/"))).getTime();
	
	
	//弹出框内容
	var content="<div><div style=\"text-align:center;margin-top:5px;\"><span style=\"font-size:12px;\"><em style=\"color:blue;\"> "+btime+etime+" </em><em style=\"color:grey;\">"+status+"</em><em style=\"color:red;\">"+mi+" 分钟</em></span></div>";
	
	content+="<div style=\"margin-top:17px;text-align:center;\"><span>"+address+"</span></div><div style=\"padding-left:10px;margin-top:13px;text-align:center;\"><a onclick=\"getindextime("+clickTime+")\" href=\"#\" style=\"font-size:13px;\">回放</a></div>";
	
	//弹出提示框
	map.openWindow(content,opts,gpslatlng);
}





/**
 * 点击播放器进度条进行播放
 * 
 * @param clickPointArray
 */

function clickProgressPlay(clickPointArray)
{
	//拿到当前div层X轴
	progressX = $('#progressSpan').offset().left;
	$('.jpp').click(function(e) 
	{
		startX = progressX;
		
		var clickX = e.pageX;
		
		progress = clickX - startX;
		
		if(progress<0)
		{
			progress = -4;
		}else if(progress > 425){
			progress = 425;
		}
		$('#tcspan').css("margin-left",progress+"px");
		var wi =  parseFloat($("#progressSpan").css("width").substr(0,$("#progressSpan").css("width").length - 2));
		indextime = startTime + parseInt(progress/wi*sumtime);
		oTime = 0;
		getindextime(indextime);
//		//计算要回放的点
//		play = Math.ceil(clickPointArray.length * (progress/100));
//		//点击进度条后车子在地图显示位置
//		playCar.setPosition(pointArray[play]);
//		
//		//时间遮罩层随车子移动
//		timeTextOverlay(pointArray[play]);
	});
}

function getindextime(t){
	indextime = t;
	var idet = t + "";
	idet = parseInt(idet.substr(0,idet.length - 3));
	var kv = null;
	var i = 0;
	$.each(pResult,function(k,v){
			if(idet > k){
				kv = v;
				++i;
			}
	});
	if(i == 0){
		kv = ogpsList[0];
	}
	playCar.setPosition(kv);
	lPosition=kv;
	timeTextOverlay(kv);
	updateProgress(kv);
	oTime = 0;
	map.closeInfoWindow();
	start();
}


function getindext(t){
	indextime = t;
	var idet = t + "";
	idet = parseInt(idet.substr(0,idet.length - 3));
	var kv = null;
	var i = 0;
	$.each(pResult,function(k,v){
			if(idet > k){
				kv = v;
				++i;
			}
	});
	if(i == 0){
		kv = ogpsList[0];
	}
	playCar.setPosition(kv);
	lPosition=kv;
	timeTextOverlay(kv);
	updateProgress(kv);
	oTime = 0;
	map.closeInfoWindow();
}






//生成车辆轨迹
function creatMarkers(data)
{
	if(!data){return false;}
	
	var zehEm = 0;
	var nehEm = 0;
	var tthEm = 0;
	var fehEm = 0;
	var nthEm = 0;
	
	map.closeInfoWindow();
	$("#replayDiv").css("display","block");
	play=0;
	//生成最新经纬度
	var newGPSLocation = data.mostNewGPSLocation;
	if(newGPSLocation){
		var lat = newGPSLocation.weidu;
		var lng = newGPSLocation.jingdu;
		lat = stringToFloat(""+lat);
		lng = stringToFloat(""+lng);
		var latlng = new ILatLng(lat, lng);
		latlng = IMapComm.gpsToMapLatLng(latlng);
		var pickTime_ = newGPSLocation.pickTime;
		var terminalState = newGPSLocation.terminalState;
	    // LineOff(离线) : 3 ,Travel (行驶中 ) : 1 ,Static (静止 ) : 2 ,Init (待初始化 ) : 0 
	    var carIcon = "icon-for-map/car-LineOff.png";	//离线或待初始化
	    if(terminalState && terminalState=='1'){
	    	carIcon = "icon-for-map/car-Travel.png";
	    }else if(terminalState && terminalState=='2'){
	    	carIcon = "icon-for-map/car-Static.png";
	    }
	    var name = newGPSLocation.carNumber;
		playCar=new IMarker(latlng, name, {
			"map" : map
        });		
		var markerOptions = {};
		markerOptions.icon = carIcon;
		playCar.setOptions(markerOptions);
	    /*var statusLabel = "<lable style='color: #0000FF;font-size: 12px;font-weight: bold;font-style: italic;'>"+name+"</lable>";
	    playCar.textOverlay_ = new ITextOverlay(latlng,statusLabel,{});
	    playCar.textOverlay_.setMap(map);*/
		playCar.bizValues_ = newGPSLocation;
        map.panTo(latlng);
	}
	//生成轨迹
	var gpsList = data.gpsList;
	carLocusList = gpsList;
	if(!gpsList){return false;}
	var polylinePath = new Array();
	lastPosition=null;
	for(var i=0;i<gpsList.length;i++){
		var gps = gpsList[i];
		var lat = gps.weidu;
		var lng = gps.jingdu;
		lat = stringToFloat(""+lat);
		lng = stringToFloat(""+lng);
		var marker=null;
		
		var originalLat_ = lat;	//原始纬度
		var originalLng_ = lng;	//原始经度
		
		
		var pickTime = gps.pickTime;
		var gpslatlng = new ILatLng(lat, lng);
		var latLng = IMapComm.gpsToMapLatLng(gpslatlng);
		latLng.pickTime_ = pickTime;
		
		
		
		latLng.time_ = pickTime;
		/*if(lastPosition!=null){
			var speed= Math.round(p2pDistance(lastPosition,latLng)/10);
			for(var k=1;k<speed-1;k++){
				var tempLatLng=replaySpeedCtrl(lastPosition,latLng,speed,k)
		        tempLatLng.pickTime_ = pickTime;
		        pointArray.push(tempLatLng);
		    }
		}*/
		
		clickPointArray.push(latLng);
		
		pointArray.push(latLng);
		ogpsList.push(latLng);
		if(lastPosition==null)
		{
			lastPosition = latLng;
		}
		
		
		var seed = p2pDistance(lastPosition,latLng);
		
		//按时间段统计
		
		
		var periodDate = new Date(Date.parse(latLng.pickTime_.replace(/-/g,"/"))).getHours();
		var key = new Date(Date.parse(latLng.pickTime_.replace(/-/g,"/"))).getTime()+"";
		key = key.substr(0,key.length - 3);
		pResult[key] = latLng;

		if(periodDate >= 0 && periodDate < 9)
		{
			zehEm += seed;
		}
		else if (periodDate >= 9 && periodDate < 12)
		{
			nehEm += seed;
		}
		else if (periodDate >= 12 && periodDate < 14)
		{
			tthEm += seed;
		}
		else if (periodDate >= 14 && periodDate < 19)	
		{
			fehEm += seed;
		}
		else if (periodDate >= 19 && periodDate < 24)
		{
			nthEm += seed;
		}
		
		//增加超过两个坐标点超过1000米后显示虚线
		if(seed < 1000)
		{
			polylinePath.push(latLng);
		}
		else
		{
			var option={"map":map};
			var strokeWeight=5;
			option.strokeOpacity=0.8;
			var polyline = new IPolyline(polylinePath,"#00A80B",strokeWeight,option);
			polylineArray.push(polyline);
			
			
			polylinePath.length = 0;
			
			
			//画虚线的两个点
			polylinePath.push(lastPosition);
			polylinePath.push(latLng);
			
			var option={"map":map};
			var strokeWeight=5;
			option.strokeOpacity=0.8;
			option.strokeStyle="dashed";
			var polyline = new IPolyline(polylinePath,"#FF0000",strokeWeight,option);
			polylineArray.push(polyline);
			polylinePath.length = 0;
		}
		
		lastPosition=latLng;
		
		if(i==0){
			var center = new ILatLng(lat, lng);
			var mapLatLng = IMapComm.gpsToMapLatLng(center);
			map.setCenter(mapLatLng);
			//起点
			marker = new IMarker(latLng,pickTime,{
				"map" : map
		   	});
			marker.setOptions({"icon":"icon-for-map/qidian.png"});
			markerArray.push(marker);
		}
		if(i==gpsList.length-1){
			marker = new IMarker(latLng,pickTime, {
				"map" : map
	    	});
			marker.setOptions({"icon":"icon-for-map/car-red.png"});
			markerArray.push(marker);
		}
		
	}
	$('#zehEm').html(zehEm/1000);
	$('#nehEm').html(nehEm/1000);
	$('#tthEm').html(tthEm/1000);
	$('#fehEm').html(fehEm/1000);
	$('#nthEm').html(nthEm/1000);
	//这里是整条轨迹
	var option={"map":map};
	var strokeWeight=5;
	option.strokeOpacity=0.8;
	var polyline = new IPolyline(polylinePath,"#00A80B",strokeWeight,option);
	polylineArray.push(polyline);
	
	setTimeout(function(){
		//绑定事件	
		bindZoomChangedEvent();
	},3000);
	
//	IMapEvent.addClickEventListener(map,"click");
	
	//地图点击侦听
    bindMapClickEvent(map,clickPointArray);
    
    //播放进度点击回放
    clickProgressPlay(clickPointArray);
    
    //初始化车辆到起点
    playCar.setPosition(clickPointArray[0]);
    
    
    //初始化车辆时间遮罩层
    timeTextOverlay(clickPointArray[0]);
    
}



var minPlay;

/**
 * 鼠标点击获取附近500米内的车辆经纬度
 * @param map
 * @param clickPointArray 经纬度数组
 */
function bindMapClickEvent(map,clickPointArray)
{
	IMapEvent.addClickEventListener(map,"click",function(e)
	{
		//鼠标点击后，暂停车辆回放
		
		if(!isStop)
		{
			stop();
		}
		
		clickPoint = e.point;
		clickLng = e.point.lng;
		clickLat = e.point.lat;
		
		//附近500米
		var distance = 500;
		
		var minPoint = null;
		
		minPlay = null;
		var content = "";
		
		var clicklatlng = new ILatLng(clickLat, clickLng);
		
		var opts ={height: 20}		     // 信息窗口高度

		content = "<div style=\"TEXT-ALIGN: center;\"><img style=\"height:30px;weight:30px;margin-top:12px;\" id=\"loading\" src=\"../../images/loading_img.gif\"></img></div>";
		
		map.openWindow(content,opts,clickPoint);
		
		for(var i = 0;i<pointArray.length;i++)
		{
			point = pointArray[i];
			
			var seed = p2pDistance(clicklatlng,point);
			
			if(seed < distance)
			{			
				distance = seed;
				minPlay = new Date(Date.parse(point.pickTime_.replace(/-/g,"/"))).getTime();
				minPoint =  point;
				
			}
		}
		
		if(minPoint==null)
		{
			content="<div  style=\"margin-top:15px;text-align:center\"><span>Sorry,附近500米内没有该车辆的经纬度</span><div style=\"margin-top:8px;\"><span>请重新选择回放点..</span></div></div>";
			map.setContent(content);
		}
		else
		{
			content="<div><div style=\"text-align:center;margin-top:5px;\"><span style=\"font-size:12px;font-weight:bold\">从以下时间点开始回放</span></div>";
			content+="<div style=\"margin-top:17px;text-align:center;\"><span class=\"map_ico position\"></span><span>"+minPoint.pickTime_.substring(11,16)+"</span><a style=\"padding-left:25px;\"onclick=\"getindextime("+minPlay+")\" href=\"#\">回放</a></div>";
			map.setContent(content);
		}
		
		
	})
	
};


/**
 *  点击回放
 */

function clickPlayBack(minPlay)
{
	play = minPlay;
}


var sumtime = 0;
var width = 0;
var indextime = 0;
var startTime = 0;
var endTime = 0;
/**
 * 时间控件开始时间结束时间显示
 * @param clickPointArray
 */
function modStartAndEndTime()
{
	$("#replayDiv_locusDate").text($("#locusDate").val());
	var locusDate = $("#locusDate").val()+" 23:59:59";
	var startDate = $("#locusDate").val()+" 00:00:00";
//	alert(locusDate);
	var sDate = new Date(Date.parse(startDate.replace(/-/g,"/")));
	var lDate = new Date(Date.parse(locusDate.replace(/-/g,"/")));
	var nowDate =  new Date();
	var endT = "23:59:59";
	var y = nowDate.getFullYear();
	var m = nowDate.getMonth() + 1;
	if(m < 10 ){
		m = "0"+m+"";
	}
	var d = nowDate.getDate();
	startTime = sDate.getTime();
	indextime = sDate.getTime();
	endTime = lDate.getTime();
	if(lDate.getTime() > nowDate.getTime() && $("#locusDate").val() == y+"-"+m+"-"+d){
		var hours =  nowDate.getHours();
		if(hours < 10){
			hours =  "0" + hours + "";
		}
		var minutes =  nowDate.getMinutes();
		if(minutes < 10){
			minutes =  "0" + minutes + "";
		}
		var seconds =  nowDate.getSeconds();
		if(seconds < 10){
			seconds =  "0" + seconds + "";
		}
		endT = hours + ":" + minutes + ":" + seconds;
		endTime = nowDate.getTime();
		sumtime = nowDate.getTime() - sDate.getTime();
	}else{
		sumtime = lDate.getTime() - sDate.getTime();
	}
	var nowDate = new Date();
//	$('.start_time').html(startTime);
	$('.end_time').html(endT);
	var contextprogressSpan = "";
	width =  parseFloat($("#progressSpan").css("width").substr(0,$("#progressSpan").css("width").length - 2));
	for(var i =0;i<statusArray.length;i++){
			var v = statusArray[statusArray.length-1-i];
			var color = "#CCCCCC";
			var wi = 0;
			if(v.status == 0){
				color = "#CCCCCC";
			}else if(v.status == 1){
				color = "#00A80B";
			}else if(v.status == 2){
				color = "#b2c4f7";
			}else{
				color = "#CCCCCC";
			}
				var time = v.epicktime_.getTime() - v.picktime_.getTime(); 
				if(sumtime != 0 && time != 0){
					wi = (time)/1000/(sumtime/1000)*width;
					if(i == statusArray.length - 1){
						wi = parseInt(wi);
					}
				}
				
				contextprogressSpan = contextprogressSpan +	"<div  style=\"width:" + wi + "px;float:left;height:16px;background:none repeat scroll 0 0 " + color + ";\">"
																						+"</div>";
	}
	oTime = 0;
	$("#tcspan").css("margin-left","-4px");
	$("#progressSpan").html("");
	$("#progressSpan").html(contextprogressSpan);
}


function createLocusTip(){
	if(polylineMarkers.length>0){
		return false;
	}
	var gpsList = carLocusList;
	for(var i=0;i<gpsList.length;i++){
		var gps = gpsList[i];
		var lat = gps.weidu;
		var lng = gps.jingdu;
		lat = stringToFloat(""+lat);
		lng = stringToFloat(""+lng);
		var marker=null;
		
		var originalLat_ = lat;	//原始纬度
		var originalLng_ = lng;	//原始经度
		var pickTime = gps.pickTime;
		var gpslatlng = new ILatLng(lat, lng);
		var latLng = IMapComm.gpsToMapLatLng(gpslatlng);
		latLng.pickTime_ = pickTime;
		latLng.time_ = pickTime;
		/*if(lastPosition!=null){
			var speed= Math.round(p2pDistance(lastPosition,latLng)/10);
			for(var k=1;k<speed-1;k++){
				var tempLatLng=replaySpeedCtrl(lastPosition,latLng,speed,k)
		        tempLatLng.pickTime_ = pickTime;
		        pointArray.push(tempLatLng);
		    }
		}*/
		var pMarker = null;
		if(i!=gpsList.length-1){
			//获取下一个坐标点的经纬度，计算箭头方向
		   	var nextLat = gpsList[i+1].weidu;
			var nextLng = gpsList[i+1].jingdu;
			nextLat = stringToFloat(""+nextLat);
			nextLng = stringToFloat(""+nextLng);
			lat = nextLat - originalLat_;
			lng = nextLng - originalLng_;
			//计算大概方向，经度→,纬度 ↑
			//images/blue_z.png
			var icon = "";
			if(lat==0 && lng>0){
				//东方
				icon = "images/arrows_E.png";
			}else if(lat==0 && lng<0){
				//西方
				icon = "images/arrows_W.png";
			}else if(lng==0 && lat>0){
				//北方
				icon = "images/arrows_N.png";
			}else if(lng==0 &&　lat<0){
				//南方
				icon = "images/arrows_S.png";
			}else if(lat<0 && lng>0){
				//东南方向
				icon = "images/arrows_ES.png";
			}else if(lat>0 && lng>0){
				//东北方向
				icon = "images/arrows_EN.png";
			}else if(lat>0 && lng<0){
				//西北方向
				icon = "images/arrows_WN.png";
			}else if(lat<0 && lng<0){
				//西南方向
				icon = "images/arrows_WS.png";
			}
			if(icon!=""){
				//生成Marker对象，指示轨迹方向，显示轨迹采集时间
				pMarker = new IMarker(latLng,pickTime,{"map":map});
				pMarker.setOptions({"icon":icon});
				pMarker.pickTime_ = pickTime;
			   	pMarker.basicIcon_ = icon;
			   	pMarker.originalLat_ = originalLat_;
			   	pMarker.originalLng_ = originalLng_;
			   	pMarker.setMap(map);
			   	polylineMarkers.push(pMarker);
			}
		}else{
			lastPosition=null;
			//终点不显示图标，但是要保存数据
			/*var icon = "images/blue_z.png";
			pMarker = new IMarker(latLng,pickTime,{"icon":icon});
			pMarker.pickTime_ = pickTime;
			//var tishiDiv = "<div>"+pickTime+"</div>";
		   	//pMarker.imageOverlay_ = new IImageOverlay(latLng,tishiDiv,{});
		   	
		   	pMarker.basicIcon_ = icon;
		   	pMarker.originalLat_ = originalLat_;
		   	pMarker.originalLng_ = originalLng_;
		   	pMarker.setMap(map);
		   	polylineMarkers.push(pMarker);*/
		}
	}
}
	
/**
 * 给标记绑定点击事件
 * @param {Object} marker
 */
function bindMarkerClickEvent(marker){
    var clickType = undefined;
	IMapEvent.addListener(marker, "click", function(){
		clickType = "click";
		setTimeout(function(){
			if(clickType=="click")
			showMarkerDetail(marker);
			clickType=undefined;
		},300);
		
	}); 
};


/**
 * 地图可视范围变化事件
 */
var lastChangedTime = null;
function bindZoomChangedEvent(){
	IMapEvent.addListener(map,"zoom_changed",function(){
	    lastChangedTime = new Date().getTime();
	    var myTime=lastChangedTime;
	   	setTimeout(function(){
	   	    if(myTime==lastChangedTime){
	   	      	var zoom = map.getZoom();
				var res = isVisiableZoom(zoom);
				if(res){
					//显示轨迹点
					showCarLocusPoint();
				}else{
					hideCarLocusPoint();
				}
	   	    }else{
//	   	     	console.log("do nothing! your time:"+myTime+",lastChangedTime:"+lastChangedTime);
	   	    }
	   	},500);
	});
}
/**
 * 通过zoom获取可见公里数
 */
function isVisiableZoom(zoom){
	var zoom = parseInt(zoom);
	if(zoom>15){
		//500米可见
		return true;
	}else{
		return false;
	}
}
//显示轨迹点
function showCarLocusPoint(){
	/*if(!polylineMarkers.length>0){
		createLocusTip();
	}*/
	for(var i=0;i<polylineMarkers.length;i++){
		var marker = polylineMarkers[i];
		marker.setMap(map);
	}
}
//隐藏轨迹点
function hideCarLocusPoint(){
	for(var i=0;i<polylineMarkers.length;i++){
		var marker = polylineMarkers[i];
		marker.setMap(null);
		//marker.imageOverlay_.setMap(null);
	}
}
//为标记绑定事件
function bindEvent(marker,info,title){
	IMapEvent.addListener(marker,"click",function(){
			initWindowInfo(info,marker,title);
	});
}

//车辆和司机信息框
function initWindowInfo(info,marker,title){
	var driverName = info.name;
	var phone = info.phone;
	var carUser = info.keyPoint[0].carUser;
	var useCarTime = info.keyPoint[0].realTakeCarTime;
	var useCarAddress = info.keyPoint[0].takeCarAddress;
	var returnCarTime = info.keyPoint[0].realReturnCarTime;
	var returnCarAddress = info.keyPoint[0].realReturnCarAddress;
	var toId = info.keyPoint[0].toId;
	var carUserPhone = info.keyPoint[0].carUserPhone;
	
	if(!driverName){driverName = "";}
	if(!carUser){carUser = "";}
	if(!phone){phone = "";}
	if(!useCarTime){useCarTime = "";}
	if(!useCarAddress){useCarAddress = "";}
	if(!returnCarTime){returnCarTime = "";}
	if(!returnCarAddress){returnCarAddress = "";}
	if(!toId){toId = "";}
	if(!carUserPhone){carUserPhone = "";}
	
	var content="任务单号:"+toId;
	content+="<br/>司机（电话）:"+driverName+"（"+phone+"）<br/>用车人（电话）:"+carUser+"("+carUserPhone+")";
	content+="<br/>用车时间:"+useCarTime+"<br/>还车时间:"+returnCarTime;
	
	var window=new IInfoWindow("",{"position":marker.getPosition()});
	window.setContent(content);
	marker.openInfoWindow(window);
}

//清除所有标记和折线
function cleanMarkersAndPolyline(){
	if(polylineArray!=null){
		for(var l=0;l<polylineArray.length;l++){
			polylineArray[l].setMap(null);
		}
		polylineArray=new Array();
	}
	if(markerArray!=null){
		for(var k=0;k<markerArray.length;k++){
			markerArray[k].setMap(null);
		}
		markerArray=new Array();
	}
	if(pointArray!=null){
		pointArray=new Array();
	}
	if(clickPointArray!=null)
	{
		clickPointArray=new Array();
	}
	
	
	if(polylineMarkers){
		for(var i=0;i<polylineMarkers.length;i++){
			var marker = polylineMarkers[i];
			if(marker.imageOverlay_){
				marker.imageOverlay_.setMap(null);
			}
			marker.setMap(null);
		}
		polylineMarkers = new Array();
	}
	
	if(playCar){
		if(playCar.textOverlay_){
			playCar.textOverlay_.setMap(null);
			playCar.textOverlay_ = null;
		}
		playCar.setMap(null);
	}
	
}

var lastPosition=null;
var playCar=null;
var pointArray=new Array();
var play=0;
var backPlaySpeed = 1;
var forwardPlaySpeed = 1;
var isBack = false;
var lPosition = null;
//回放
function replay(){
	clearInterval(setInter);
	isStop = false;
	status = true;
	/*if(playCar&&playCar.textOverlay_){
		playCar.textOverlay_.setMap(null);
	}*/
	if(markerArray!=""&&markerArray!=null && playCar==null){
		playCar=new IMarker(markerArray[0].getPosition(), markerArray[0].title, {
			"map" : map
        });
        playCar.setOptions({"icon":"icon-for-map/car-green.png"});
	}
	if(playCar==null){
		return;
	}
	if(pointArray!=null){
		setInter = setInterval(function(){
			if(isStop)
				return;
			if(playCar!=null){
				if(pointArray != null){
					//console.log(pResult);
//					$.each(pointArray,function(k,v){
//						var pickTime_ = new Date(Date.parse(v.pickTime_.replace(/-/g,"/")));
//						if(startTime == pickTime_.getTime() || startTime + 10000 > pickTime_.getTime() || startTime + 10000 < pickTime_.getTime()){
//											console.log(pickTime_.getTime());
//											playCar.setPosition(v);
//											lastPosition=v;
//											timeTextOverlay(lastPosition);
//											updateProgress(lastPosition);
//						}else{
//							return false;
//						}
//					});
					var sumi = indextime;
//					console.log(pResult);
//					console.log(sumi);
//					for(var i = 0;i < 10000;i++){
//						if(pResult[sumi]){
//									console.log(pResult[sumi]);
//									playCar.setPosition(pResult[sumi]);
//									lastPosition=pResult[sumi];
//									timeTextOverlay(lastPosition);
//									updateProgress(lastPosition);
//						}else{
//							continue;
//						}
//						sumi = sumi + i;
//					}
				}
//				console.log(indextime);
				indextime = indextime + timeG;
				var idet = indextime + "";
				idet = parseInt(idet.substr(0,idet.length - 3));
				var k = 0;
				for(var i = 0;i <= timeGC;i++){
						idet = idet - i;
//						console.log(pResult[idet]);
						if(pResult[idet] && oTime < idet && timeG > 0){
							playCar.setPosition(pResult[idet]);
							lastPosition=pResult[idet];
							lPosition=pResult[idet];
							timeTextOverlay(lastPosition);
							updateProgress(lastPosition);
							oTime = idet;
							k++;
							break;
						}else if(pResult[idet] && oTime > idet && timeG < 0){
							playCar.setPosition(pResult[idet]);
							lastPosition=pResult[idet];
							lPosition=pResult[idet];
							timeTextOverlay(lastPosition);
							updateProgress(lastPosition);
							oTime = idet;
							k++;
							break;
						}else{
							continue;
						}
				}
				if(k == 0){
					if(lPosition == null){
						lPosition = ogpsList[0];
					}
						var today = new Date();
						today.setTime(indextime);
						var year = today.getFullYear();
						var month = today.getMonth() + 1;
						if(month < 10){
							month = "0" + month + "";
						}
						var date = today.getDate();
						if(date < 10){
							date = "0" + date + "";
						}
						var hours = today.getHours();
						if(hours < 10){
							hours = "0" + hours + "";
						}
						var minutes = today.getMinutes();
						if(minutes < 10){
							minutes = "0" + minutes + "";
						}
						var seconds = today.getSeconds();
						if(seconds < 10){
							seconds = "0" + seconds + "";
						}
						if(lPosition){
							lPosition.pickTime_ = year + "-" + month + "-" + date + " " + hours + ":" + minutes + ":" + seconds;
							timeTextOverlay(lPosition);
							updateProgress(lPosition);
						}
				}
				var wi = (indextime-startTime)/1000*width/((sumtime)/1000);
				var s = -4 + wi;
				$("#tcspan").css("margin-left", s + "px");
				var today = new Date();
				today.setTime(indextime);
//				console.log(indextime+"==="+endTime+"==="+sumtime);
				//console.log(indextime+"=============="+endTime);
				if(indextime > endTime || indextime < startTime){
					map.removeOverlay(playCar.textOverlay_);   //清除车头上的时间
//					playCar.setMap(null);
//					playCar=null;
//					lastPosition=null;
					progressTime="";
					$("#start").show();
					$("#pause").hide();
					clearInterval(setInter);
					indextime=startTime;
					oTime = 0;
				}
//				playCar.setPosition(pointArray[play]);
//				lastPosition=pointArray[play];
//				timeTextOverlay(lastPosition);
//				updateProgress(lastPosition);
//				if(isBack){
//					play=play-backPlaySpeed;
//				}else{
//					play=play+forwardPlaySpeed;
//				}
//				if(play<0){
//					play = 0;
//				}
//				if(play<pointArray.length){
//					replay();		
//				}else{
//					map.removeOverlay(playCar.textOverlay_);   //清除车头上的时间
//					playCar.setMap(null);
//					playCar=null;
//					lastPosition=null;
//					progressTime="";
//					$("#start").show();
//					$("#pause").hide();
//				}
			}
		},100);
	}
}

/**
 * 判断是否在当前背景窗口之内
 */
function ifInBGWindow(latlng){
	//当前窗口对象
	var bounds = map.getBounds();
	bounds = iscreate.maps.google.tools.toGoogleBounds(bounds);
	latlng = iscreate.maps.google.tools.toGoogleLatLng(latlng);
	//判断某范围是否包含指定经纬度，返回boolean
	var res = bounds.contains(latlng);
	if(res){
		//包含
		return true;
	}else{
		//不包含
		return false;
	}
}



var progressTime="";
function updateProgress(position){
	var percent=play/(pointArray.length-1);
	var curTime=position.pickTime_.substring(10);
	$("#progressTime").html(curTime);
//	var per=percent*100;
//	$("#progressDiv").css("width",percent*100+"%");
}

/**
 * 回放时增加车辆顶上时间覆盖层
 * 
 * @param position
 */


function timeTextOverlay(position)
{
	map.removeOverlay(playCar.textOverlay_);
	var statusLabel = "<lable id=\"timeOverlay\" style='color: #FF0000;font-size: 12px;font-weight: bold;'>"+position.pickTime_.substring(11)+"</lable>";
	playCar.textOverlay_ = new ITextOverlay(position,statusLabel,{});
//	console.log(playCar);
	playCar.textOverlay_.setMap(map);
}

//回放速度控制（计算下一个坐标）
function replaySpeedCtrl(lastPosition,nextPosition,speed,j){
	var lastLat=lastPosition.getLatitude();
	var lastLng = lastPosition.getLongitude();
	var nextLat = nextPosition.getLatitude();
	var nextLng = nextPosition.getLongitude();
	var curLat=lastLat+(nextLat-lastLat)*j/speed;
	var curLng=lastLng+(nextLng-lastLng)*j/speed;
	return new ILatLng(curLat,curLng);
}
  
//计算两点之间的距离
function p2pDistance (point,latlng) {
    var lat = [point.getLatitude(), latlng.getLatitude()]
    var lng = [point.getLongitude(), latlng.getLongitude()] 
    var R = 6378137;
    var dLat = (lat[1] - lat[0]) * Math.PI / 180;
    var dLng = (lng[1] - lng[0]) * Math.PI / 180;
    var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(lat[0] * Math.PI / 180) * Math.cos(lat[1] * Math.PI / 180) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    var d = R * c;
    return Math.round(d);
}
  


  
/**
 * 开始回放
 */
function start()
{
//	$("#progressTime").html("0:00:00");
	isStop=false;
	isBack = false;
	backPlaySpeed = 1;
	forwardPlaySpeed = 1;
	$("#start").hide();
	$("#pause").show();
	timeG = 10000;
	timeGC = 10;
	if(indextime == endTime)
	{
		oTime = 0;
		indextime = startTime;
		$("#tcspan").css("margin-left","-4px");
	}
	if(lastPosition==null){
		play=0;
		replay();
		
	}else{
		replay();
	}	
	$(".beishu").css("display","none");
	$("#beishu").html("");
}
  
  //暂停回放
  function stop(){
	  isStop=true;
	  $("#start").show();
	  $("#pause").hide();
  }
  //停止轨迹回放
  function finish(){
	  if(playCar!=null){
		  clearInterval(setInter);
		  play=0;
		  backPlaySpeed = 1;
		  forwardPlaySpeed = 1;
		  isStop=true;
//		  playCar.setMap(null);
//		  playCar = null;
		  lastPosition=null;
		  $("#start").show();
		  $("#pause").hide();
			timeG = 10000;
			timeGC = 10;
		  oTime = 0;
		  indextime = startTime;
//		  alert(indextime);
//		  console.log(oTime);
//		  console.log(startTime+"========="+indextime);
		  $("#tcspan").css("margin-left","-4px");
	  }
//	  $("#rightInformation").css("display","none");
//	  $("#replayDiv").css("display","none");
//	  $("#taskTable").find("tr:gt(0)").remove();
//	  cleanMarkersAndPolyline();
//	  restoreQueryResult();
//  	  carLocusList = null;
	  //初始化车辆到起点
    playCar.setPosition(clickPointArray[0]);
    
    //初始化车辆时间遮罩层
    timeTextOverlay(clickPointArray[0]);
  }
  
  //轨迹后退
  function moveBack(){
	  if(playCar!=null){
	  	if(timeG > 0){
	  		timeG = -10000;
	  	}
	  	if(timeG < 0){
	  		timeG = timeG * 2;
	  	}else{		
	  	  timeG = timeG * -2;
	  	}
	  	  if(timeG<-80000){
	  	  	timeG = -10000;
	  	  }
		  timeGC = timeGC * 2;
	  	  if(timeGC<80){
	  	  	timeGC = 10;
	  	  }
	  	  clearInterval(setInter);
	  	  replay();
		  $(".beishu").show();
	  	  $("#beishu").html("快退"+timeG/-10000+"x");
	  }
  }
  //轨迹快进
  function moveForward(){
	  if(playCar!=null){
	  	if(timeG < 0){
	  		timeG = 10000;
	  	}
	  	if(timeG > 0){
	  	  timeG = timeG * 2;
	  	}else{		
	  	  timeG = timeG * -2;
	  	}
	  	  if(timeG>80000){
	  	  	timeG = 10000;
	  	  }
		  timeGC = timeGC * 2;
	  	  if(timeGC>80){
	  	  	timeGC = 10;
	  	  }
	  	  clearInterval(setInter);
	  	  replay();
		  $(".beishu").show();
		  $("#beishu").html("快进"+timeG/10000+"x");
	  }
  }
 
/**
 * 将string('yyyy-mm-dd hh:mm:ss')和yyyy-mm-dd转换成date
 * @param {Object} parseString
 * @return {TypeName} 
 */
 function parseDate(parseString){
	 var returnDate = new Date(),dateArr=[],timeArr=[0,0,0];
	 var dateString="";
     var timeString="";
     if ( !parseString || parseString ==''){
    	 return null;
     }
     if(parseString.length==10){   	 
    	 dateString=parseString.substring(0,10);
     }else if(parseString.length==21){
    	 dateString=parseString.substring(0,10);
    	 timeString=parseString.substring(11,parseString.length-2);
     }else if(parseString.length==19){
    	 dateString=parseString.substring(0,10);
    	 timeString=parseString.substring(11);
     }
     if(dateString!=""){
    	 dateArr=dateString.split('-');
         returnDate.setFullYear(dateArr[0],dateArr[1]-1,dateArr[2]);
     }
     if(timeString!=""){
    	 timeArr=timeString.split(':');
     }
     returnDate.setHours(timeArr[0],timeArr[1],timeArr[2],0);
     return returnDate;
 }
 /**
 * 将字符串转换成浮点数
 */
function stringToFloat(str){
	var num = null;
	try{
		var re = parseFloat(str);
		num = re;
	}catch(err){
		
	}
	
	return num;
}


//鼠标拖动

var dragAndDrop = function(){
	
	//客户端当前屏幕尺寸(忽略滚动条)
	var _clientWidth;
	var _clientHeight;
		
	//拖拽控制区
	var _controlObj;
	//拖拽对象
	var _dragObj;
	//拖动状态
	var _flag = false;
	
	//拖拽对象的当前位置
	var _dragObjCurrentLocation;
	
	//鼠标最后位置
	var _mouseLastLocation;
	
	//使用异步的Javascript使拖拽效果更为流畅
	//var _timer;
	
	//定时移动，由_timer定时调用
	//var intervalMove = function(){
	//	$(_dragObj).css("left", _dragObjCurrentLocation.x + "px");
	//	$(_dragObj).css("top", _dragObjCurrentLocation.y + "px");
	//};
	
	var getElementDocument = function(element){
		return element.ownerDocument || element.document;
	};
	
	//鼠标按下
	var dragMouseDownHandler = function(evt){

		if(_dragObj){
			
			evt = evt || window.event;
			
			//获取客户端屏幕尺寸
			_clientWidth = document.body.clientWidth;
			_clientHeight = document.documentElement.scrollHeight;
			
			//iframe遮罩
			$("#jd_dialog_m_b_1").css("display", "");
						
			//标记
			_flag = true;
			
			//拖拽对象位置初始化
			_dragObjCurrentLocation = {
				x : $(_dragObj).offset().left,
				y : $(_dragObj).offset().top
			};
	
			//鼠标最后位置初始化
			_mouseLastLocation = {
				x : evt.screenX,
				y : evt.screenY
			};
			
			//注：mousemove与mouseup下件均针对document注册，以解决鼠标离开_controlObj时事件丢失问题
			//注册事件(鼠标移动)			
			$(document).bind("mousemove", dragMouseMoveHandler);
			//注册事件(鼠标松开)
			$(document).bind("mouseup", dragMouseUpHandler);
			
			//取消事件的默认动作
			if(evt.preventDefault)
				evt.preventDefault();
			else
				evt.returnValue = false;
			
			//开启异步移动
			//_timer = setInterval(intervalMove, 10);
		}
	};
	
	//鼠标移动
	var dragMouseMoveHandler = function(evt){
		if(_flag){

			evt = evt || window.event;
			
			//当前鼠标的x,y座标
			var _mouseCurrentLocation = {
				x : evt.screenX,
				y : evt.screenY
			};
			//拖拽对象座标更新(变量)
			_dragObjCurrentLocation.x = _dragObjCurrentLocation.x + (_mouseCurrentLocation.x - _mouseLastLocation.x);
			_dragObjCurrentLocation.y = _dragObjCurrentLocation.y + (_mouseCurrentLocation.y - _mouseLastLocation.y);
			
			//将鼠标最后位置赋值为当前位置
			_mouseLastLocation = _mouseCurrentLocation;
			
			//拖拽对象座标更新(位置)
			$(_dragObj).css("left", _dragObjCurrentLocation.x + "px");
			$(_dragObj).css("top", _dragObjCurrentLocation.y + "px");
			
			//取消事件的默认动作
			if(evt.preventDefault)
				evt.preventDefault();
			else
				evt.returnValue = false;
			
			
		}
	};
	
	//鼠标松开
	var dragMouseUpHandler = function(evt){
		if(_flag){
			evt = evt || window.event;
			
			//取消iframe遮罩
			$("#jd_dialog_m_b_1").css("display", "none");
			
			//注销鼠标事件(mousemove mouseup)
			cleanMouseHandlers();
			
			//标记
			_flag = false;
			
			//清除异步移动
			//if(_timer){
			//	clearInterval(_timer);
			//	_timer = null;
			//}
			progressX = $('#progressSpan').offset().left;
		}
	};
	
	//注销鼠标事件(mousemove mouseup)
	var cleanMouseHandlers = function(){
		if(_controlObj){
			$(_controlObj.document).unbind("mousemove");
			$(_controlObj.document).unbind("mouseup");
		}

		
	};
	
	return {
		//注册拖拽(参数为dom对象)
		Register : function(dragObj, controlObj){
			//赋值
			_dragObj = dragObj;
			_controlObj = controlObj;
			//注册事件(鼠标按下)
			$(_controlObj).bind("mousedown", dragMouseDownHandler);			

		}
	}

}();

