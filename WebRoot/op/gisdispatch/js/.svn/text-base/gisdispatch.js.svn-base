var map=null;				//地图对象
var currentInfoWindow=null; //infoWindow对象
var allLayers = new Array();	//保存所有图层数据,保存格式[{key:图层key,visiableList:[],disVisableList:[],allList[]}{}]
var neLatLng=null;
var swLatLng=null;
var layerTreeData = null;			//存放左边图元组织架构树数据
var allTreeValues = new Array();	//保存左边树图层、图元信息 
var allMarkers =new Array();		//所有Marker对象
var hotPoitConstant = {"RESOURCE_QUESTIONSPOT":"_resource_questionspot","RESOURCE_HIDDENTROUBLESPOT":"_resource_hiddentroublespot","RESOURCE_FAULTSPOT":"_resource_falutspot","RESOURCE_WATCHSPOT":"_resource_watchspot"};	//标记热点常量
var searchResultMarkers = new Array();	//搜索结果Marker列表（存放临时数据，按结果显示区的返回键立即清除）
var isTitleShow = false;	//标识图元Title是否显示
$(function(){
	//var height = document.body.clientHeight;
	var height = document.documentElement.clientHeight; 
	height = height - 205;
	$("#gis_map").css("height",height+"px");
	$("#gis_content_right").css("height",height+"px");
	$("#monitorResourceDiv").css("height",height+"px");
	$("#searchResultDiv").css("height",height+"px");
	
	var width = $(window).width()/2;
	width = width - 100;
	$("#please_res_wait_cue_div").css("left",width+"px");
	initMap();		//初始化地图
	//给地图添加点击事件
	IMapEvent.addListener(map,"click",function(event){
    	currentInfoWindow.close();
    	$("#tuceng_div").css("display","none");
    });
	
	/**收起Table*/
	$(".top_tr_img").live("click",function(){
		if($(this).attr("src") == "image/ico_show.gif"){
				$(this).attr("src","image/ico_hide.gif");
				$(this).parent().parent().parent().find("tr").not($(".main_tr")).hide();
			}else{
				$(this).attr("src","image/ico_show.gif");
				$(this).parent().parent().parent().find("tr").not($(".main_tr")).show();
			}
	});
	/**
	 * 顶部图层按钮点击事件							
	 */
	var ifClick = false;	//用于标识显示隐藏
	$("#show_tuceng_div").click(function(){
		$("#tuceng_div").slideToggle("fast",function(){
			
			//che.yd---------------begin----
			$(this).toggleClass("selected");
			if($("#tuceng_div").css("display") == "block"){
				$("#show_tuceng_div").html("<h4 class='layer_ico'>图层 ▲</h4>");
			}else{
				$("#show_tuceng_div").html("<h4 class='layer_ico'>图层 ▼</h4>");
			}
			//che.yd---------------end----
			
			if(!ifClick){
				//显示左边树,若数据不为空，则重新创建左边树
				if(layerTreeData!=null){
					//显示隐藏不需要重新获取左边树，只需要从全局变量中拿
					createLayerTree(layerTreeData,null);
				}
				ifClick = true;
			}else{
				//隐藏左边树，不做操作
				ifClick = false;
			}
		});
	});
	
	/* 标记热点按钮 */
	$("#markPoint").click(function(){
		$("#biaojiredian").slideToggle("fast");
	});
	//定时刷新任务数
	setInterval(function(){
		 refreshMarker();
	},150000);
	//定时刷新监控资源信息
	setInterval(function(){
		$("#mrLoadingDiv").show();
		getMonitorResource();
	},60000);
	//刷新按钮点击事件
	$("#refresh_ico").click(function(){
		$("#mrLoadingDiv").show();
		refreshCacheData();	//先刷新内存数据
		getMonitorResource();//再获取监控资源
	});
	//定时刷新cache的数据
	setInterval(function(){
		refreshCacheData();
	},300000);
	/**
	 * 设置按钮点击事件
	 */
	$("#settingButton").click(function(){
		$("#tuceng_div").slideToggle("fast",function(){
			ifClick = false;
		});
		var userConfigTree = "";
		//循环判断哪些图层是更改过的
	    $(".pel").each(function(index){
	    	var visiale = $(this).attr("checked");
	    	var layerKey = $(this).val();
	    	if(visiale == undefined){
	    		visiable = false;
	    	}else{
	    		visiable = true;
	    	}
	    	//遍历已经保存的树信息
	    	for(var i=0;i<allTreeValues.length;i++){
	    		var tv = allTreeValues[i];
	    		if(tv.key == layerKey){
	    			//判断状态有无更改过,只取更改过状态的图层
	    			if(tv.visiable != visiable){
	    				userConfigTree+=layerKey+":"+visiable+",";
	    				//删除该元素
						allTreeValues.splice(i,1);	    				
	    			}
	    		}
	    	}
	    });
	    //判断哪些图元是更改过的
	    $(".layer").each(function(index){
	    	var visiale = $(this).attr("checked");
	    	var typeKey = $(this).val();
	    	if(visiale == undefined){
	    		visiable = false;
	    	}else{
	    		visiable = true;
	    	}
	    	//遍历已经保存的树信息
	    	for(var i=0;i<allTreeValues.length;i++){
	    		var tv = allTreeValues[i];
	    		if(tv.key == typeKey){
	    			//判断状态有无更改过,只取更改过状态的图层
	    			if(tv.visiable != visiable){
	    				userConfigTree+=typeKey+":"+visiable+",";
	    				//删除该元素
						allTreeValues.splice(i,1);	    				
	    			}
	    		}
	    	}
	    });
	    if(userConfigTree!=""){
	    	userConfigTree = userConfigTree.substring(0,userConfigTree.length-1);
	    }
	    var nowState = false;
	   	if($("#isTitleShow").attr("checked") == "checked"){
	   		nowState = true;
	   	}
	   	if(nowState!=isTitleShow){
	   		var layerKey = $("#isTitleShow").val();
	   		userConfigTree+=layerKey+":"+nowState;
			isTitleShow = nowState;
	   	}
	    var queryUrl = "savePicLayerConfigAction";
	    var zoom = map.getZoom();
	    var currentVisibleKM="";
	    if(zoom!=null){
	    	currentVisibleKM = getVisiableKMByZoom();
	    }
//	   	alert(userConfigTree);
		var param={userConfigTree:userConfigTree,currentVisibleKM:currentVisibleKM};
		$.ajax({ 
		    type : "post", 
		    url : queryUrl, 
		    data : param, 
		    async : true, 
		    success : function($data){
				var res = $data;
				var result = eval("("+res+")");
				
				if(result['hasChanged']){
					var newlyShow = result.newlyShow;
					var newlyHide = result.newlyHide;
					var layerTree = result.treeJson;
					
					
					//清空要隐藏的元素
					if(newlyHide && newlyHide!=""){
						cleanHidedMarker(newlyHide);
					}
					
					//显示新增图元
					if(newlyShow && newlyShow!=""){
						initMarker(newlyShow);
					}
					
					//重新生成左边树
					if(layerTree!=null){
						createLayerTree(layerTree,true);
					}
					layerTreeData = null;
					layerTreeData = layerTree;
					
					setTimeout(function(){
						responseZoomChange();
					},500);
					
					setTimeout(function(){
						refreshMarker();
					},1500);
					//alert(layerTreeData.toSource());
				}else{
					
				}
		    } 
		});
	});
	topTableChange(); //顶部菜单Table切换
	workspaceSelfAdaption();
	
  	//顶部搜索按钮
	$("#topSearchButton").click(function(){
		var searchType="";
		var param = {};
		if($("#gjSearchCb").attr("checked") == "checked"){
			//高级搜索
			$(".topSearchCb").each(function(index){
				if($(this).attr("checked") == "checked"){
					var type = $(this).val();
					searchType+=type+",";
					if(type=="_resource_car"){
						param.searchCarStatu = $("#searchCarStatu").val();
						param.searchCarDuty = $("#searchCarDuty").val();
						param.searchCarTaskCount = $("#searchCarTaskCount").val();
					}else if(type=="_resource_human"){
						param.searchHumanDuty = $("#searchHumanDuty").val();
						param.searchHumanTaskCount = $("#searchHumanTaskCount").val();
					}else if(type=="_resource_station"){
						param.searchBsGrade = $("#searchBsGrade").val();
						param.searchBsWoCount = $("#searchBsWoCount").val();
					}
				}
			});
		}
		if(searchType!=""){
			searchType = searchType.substring(0,searchType.length-1);
		}
		var searchName = $("#topSearchText").val();
		if(searchName==""){
			alert("请输入要搜索的资源名称！");
			return false;
		}
		//清除搜索结果
		cleanSearchResultEvent();
		currentInfoWindow.close();
		$(".search_span_bg").slideToggle("fast");
		param.searchName = searchName;
		param.searchType = searchType;
		$("#tabmenu li").removeClass("active");
		$("#searchResultLi").css("display","block");
		$("#searchResultLi").attr("class","active");
		$("#searchResultDiv").css("display","block");
		$("#monitorResourceDiv").css("display","none");
		$("#gis_content_right_data").css("display","none");
		searchNearResource(param);
	});
	
	
	//ESC事件
	bindKeyBoardEntryEvent();
	//显示或隐藏图元Title
	$("#isTitleShow").click(function(){
		/*if($(this).attr("checked") == "checked"){
			if(allMarkers){
				for(var i=0;i<allMarkers.length;i++){
					var marker = allMarkers[i];
					marker.textOverlay_.setMap(map);
				}
			}
			isTitleShow = true;
		}else{
			if(allMarkers){
				for(var i=0;i<allMarkers.length;i++){
					var marker = allMarkers[i];
					marker.textOverlay_.setMap(null);
				}
			}
			isTitleShow = false;
		}*/
	});
});
function refreshCacheData(){
	var zoom = map.getZoom();
	var centerLatLng = map.getCenter();
	var currentVisiableKM = getVisiableKMByZoom();
	var bounds = map.getBounds();	//当前窗口范围
	//Hard code
	var swlat,swlng,nelat,nelng;
	if(bounds!=null){
		swlat=bounds.getSw().getLatitude();
		swlng=bounds.getSw().getLongitude();
		nelat=bounds.getNe().getLatitude();
		nelng=bounds.getNe().getLongitude();
	}else{
		//Hard code防止获取不到数据
		swlat=23.05426437989033;
		swlng=113.31888037387466;
		nelat=23.10172044456083;
		nelng=113.44685393039322;
	}
	var param = {'swLat':swlat,'swLng':swlng,'neLat':nelat,'neLng':nelng,currentVisibleKM:currentVisiableKM,zoom:zoom,'centerLatLng.latitude':centerLatLng.getLatitude(),'centerLatLng.longitude':centerLatLng.getLongitude()};
	$.ajax({ 
        type : "post", 
        url : "refreshCacheDataAction", 
        data : param, 
        async : false, 
        success : function($data){ 
        	//刷新成功
        }
    });
}
/* 获取监控资源 */
function getMonitorResource(){
	var height = document.documentElement.clientHeight; 
	height = height - 210;
	$.ajax({
		type : "post", 
		url : "getMonitorResourceAction", 
		async : true, 
		success : function(res){ 
			var queryResult = eval("("+res+")");
			var staffList = queryResult.staffList;//人员
			var carList = queryResult.carList;	//车辆
			var stationList = queryResult.stationList;	//站址
			//循环判断获取回来的列表是否为空
			var hul = $("#monitorStaffListUl");
			hul.html("");
			//生成站址列表
			if(staffList && staffList.length>0){
				hul.prev().html("共有 "+staffList.length+" 个结果");
				for(var i=0;i<staffList.length;i++){
					var be = staffList[i];
					//生成左边搜索结果
					var name = be.name;
					var staffId = be.key;
					var sex = be.sex;
					var phone = be.phone;
					var count = 0;
					
					var tempStaffId = staffId.substring(staffId.lastIndexOf("_")+1);
					if(name == undefined || name == null || name=='null'){name="";}
					if(sex == undefined || sex == null || sex=='null'){sex=" ";}
					if(phone == undefined || phone == null || phone=='null'){phone=" ";}
					if(be.taskInfo){
						count = be.taskInfo.totalTaskCount;
					}
					
					var li = "<li onclick=\"openResourceInfoWindow('"+staffId+"','"+be.type+"')\" class='resource_item clearfix mhLi'><div class='r'>";
					li += "<p class='name'><a href='../staffduty/showStaffInfo.jsp?staffId="+tempStaffId+"' target='_blank'>"+name+"</a>，"+sex+"，"+phone+"</p>";
					li += "<p class='name mt5'>当前<i class='red f-bold'>"+count+"个</i>任务</p>";
					//hul.html(hul.html()+li);
					hul.append($(li));
				}
			}else{
				hul.siblings().find($(".no_result hidden")).show();
				hul.prev().html("共有 0 个结果");
			}
			var cul = $("#monitorCarListUl");
			cul.html("");
			//生成搜索结果车辆列表
			if(carList && carList.length>0){
				cul.prev().html("共有 "+carList.length+" 个结果");
				for(var i=0;i<carList.length;i++){
					var be = carList[i];
					//生成左边搜索结果
					var name = be.name;
					var carId = be.key;
					var driverName = be.driverName;
					var driverPhone = be.driverPhone;
					var carState = be.isOnDuty;
					var carType = be.carType;
					
					var tempCarId = carId.substring(carId.lastIndexOf("_")+1);
					if(name == undefined || name == null || name=='null'){name="";}
					if(driverName == undefined || driverName == null || driverName=='null'){driverName=" ";}
					if(driverPhone == undefined || driverPhone == null || driverPhone=='null'){driverPhone=" ";}
					if(carType == undefined || carType == null || carType=='null'){carType=" ";}
					
					var stat = carState;
					if( carState && carState=='0'){
				    	carState = "<em style='color:grey;'>离线</em>";
				    }else if(carState && carState=='1'){
				    	carState = "<em style='color:blue;'>行驶中</em>";
				    }else if(carState && carState=='2'){
				    	carState = "<em style='color:green;'>静止</em>";
				    }else{
				    	carState = "<em style='color:grey;'>待初始化</em>";
				    }
				    var count = 0;
				    if(be.taskInfo){
						count = be.taskInfo.totalTaskCount;
					}
					var li = "<li onclick=\"openResourceInfoWindow('"+carId+"','"+be.type+"')\" class='resource_item clearfix mcLi'><div class='r'>";
					li += "<p class='name'><a href='../cardispatch/cargeneral_index.jsp?carId="+tempCarId+"&type=view' target='_blank'>"+name+"</a>，"+carType+"，"+carState+"</p>";
					li += "<p class='name mt5'>"+driverName+"("+driverPhone+")<em class='name_red'><i class='red f-bold'>"+count+"个</i>任务</em></p></div></li>";
					//cul.html(cul.html()+li);
					cul.append($(li));
				}
			}else{
				cul.siblings().find($(".no_result hidden")).show();
				cul.prev().html("共有 0 个结果");
			}
			var sul = $("#monitorStationListUl");
			sul.html("");
			//生成站址列表
			if(stationList && stationList.length>0){
				sul.prev().html("共有 "+stationList.length+" 个结果");
				var stationUrl = "../../resource/physicalres/getPhysicalresForOperaAction?currentEntityType=Station&modelType=view&showType=showTask&currentEntityId=";
				for(var i=0;i<stationList.length;i++){
					var be = stationList[i];
					//生成左边搜索结果
					var name = be.name;
					var stationId = be.key;
					var address = be.address;
					var count = 0;
					
					var tempStationId = stationId.substring(stationId.lastIndexOf("_")+1);
					if(name == undefined || name == null || name=='null'){name="";}
					if(address == undefined || address == null || address=='null'){address=" ";}
					if(be.taskInfo){
						count = be.taskInfo.totalTaskCount;
					}
					tempStationId = stationUrl+tempStationId;
					
					var li = "<li onclick=\"openResourceInfoWindow('"+stationId+"','"+be.type+"')\" class='resource_item clearfix msLi'><div class='r'>";
					li += "<p class='name'><a href='"+tempStationId+"' target='_blank'>"+name+"</a></p>";
					li += "<p class='name mt5'>抢修任务<i class='red f-bold'>"+count+"个</i>，巡检任务<i class='red f-bold'>0个</i></p>";
					li += "<p class='name'>地址："+address+"</p></div></li>";
					//sul.html(sul.html()+li);
					sul.append($(li));
				}
			}else{
				sul.siblings().find($(".no_result hidden")).show();
				sul.prev().html("共有 0 个结果");
			}
			//分页
			pagingColumnByForeground("mcPageContent",$(".mcLi"),6);
			pagingColumnByForeground("mhPageContent",$(".mhLi"),6);
			pagingColumnByForeground("msPageContent",$(".msLi"),6);
			
			$("#mrLoadingDiv").hide();
		} 
	});
}
/*搜索附近资源*/
function searchNearResource(param){
	var hul = $("#searchStaffListUl");
	hul.html("");
	var cul = $("#searchCarListUl");
	cul.html("");
	var sul = $("#searchStationListUl");
	sul.html("");
	var queryUrl = "getResourceByConditionsAction";
	$.ajax({
		type : "post", 
		url : queryUrl, 
		data : param, 
		async : true, 
		success : function($data){ 
		   	var res = $data;
			var queryResult = eval("("+res+")");
			var staffList = queryResult._resource_human;//人员
			var carList = queryResult._resource_car;	//车辆
			var stationList = queryResult._resource_station;	//站址
			//生成人员列表
			if(staffList && staffList.length>0){
				hul.prev().html("共有 "+staffList.length+" 个结果");
				for(var i=0;i<staffList.length;i++){
					var be = staffList[i];
					var marker = null;
					var isStaffExit = false;
					var index = 0;
					for(var j=0;j<allMarkers.length;j++){
						marker = allMarkers[j];
						if(be.key==marker.entityId){
							isStaffExit = true;
							index = j;
							break;
						}
					}
					if(isStaffExit){
						//更改图标
						var options = {};
						options.icon = "image/staff5.png";
						marker.setOptions(options);
						marker.searchIcon = "image/staff5.png";
						//保存数据
						allMarkers.splice(index,1);
						allMarkers.push(marker);
						searchResultMarkers.push(marker);
					}else{
						//图元不存在，生成图元
						createSingelMarker(be);
					}
					//生成左边搜索结果
					var name = be.name;
					var staffId = be.key;
					var sex = be.sex;
					var phone = be.phone;
					var count = 0;
					
					var tempStaffId = staffId.substring(staffId.lastIndexOf("_")+1);
					if(name == undefined || name == null || name=='null'){name="";}
					if(sex == undefined || sex == null || sex=='null'){sex=" ";}
					if(phone == undefined || phone == null || phone=='null'){phone=" ";}
					if(be.taskInfo){
						count = be.taskInfo.totalTaskCount;
					}
					var li = "<li onclick=\"openResourceInfoWindow('"+staffId+"','"+be.type+"')\" class='resource_item clearfix shLi'><div class='r'>";
					li += "<p class='name'><a href='../staffduty/showStaffInfo.jsp?staffId="+tempStaffId+"' target='_blank'>"+name+"</a>，"+sex+"，"+phone+"</p>";
					li += "<p class='name mt5'>当前<i class='red f-bold'>"+count+"个</i>任务</p>";
					hul.append($(li));
				}
			}else{
				hul.siblings().find($(".no_result hidden")).show();
				hul.prev().html("共有 0 个结果");
			}
			//生成搜索结果车辆列表
			if(carList && carList.length>0){
				cul.prev().html("共有 "+carList.length+" 个结果");
				for(var i=0;i<carList.length;i++){
					var be = carList[i];
					var marker = null;
					var isCarExit = false;
					var index = 0;
					for(var j=0;j<allMarkers.length;j++){
						var marker = allMarkers[j];
						if(be.key==marker.entityId){
							isCarExit = true;
							index = j;
							break;
						}
					}
					if(isCarExit){
						//更改图标
						var options = {};
						options.icon = "image/staff5.png";
						marker.setOptions(options);
						marker.searchIcon = "image/staff5.png";
						//保存数据
						allMarkers.splice(index,1);
						allMarkers.push(marker);
						searchResultMarkers.push(marker);
					}else{
						//图元不存在，生成图元
						createSingelMarker(be);
					}
					//生成左边搜索结果
					var name = be.name;
					var carId = be.key;
					var driverName = be.driverName;
					var driverPhone = be.driverPhone;
					var carState = be.isOnDuty;
					var carType = be.carType;
					
					var tempCarId = carId.substring(carId.lastIndexOf("_")+1);
					if(name == undefined || name == null || name=='null'){name="";}
					if(driverName == undefined || driverName == null || driverName=='null'){driverName=" ";}
					if(driverPhone == undefined || driverPhone == null || driverPhone=='null'){driverPhone=" ";}
					if(carType == undefined || carType == null || carType=='null'){carType=" ";}
					
					if(carState && carState=='0'){
				    	carState = "<em style='color:grey;'>离线</em>";
				    }else if(carState && carState=='1'){
				    	carState = "<em style='color:blue;'>行驶中</em>";
				    }else if(carState && carState=='2'){
				    	carState = "<em style='color:green;'>静止</em>";
				    }else{
				    	carState = "<em style='color:grey;'>待初始化</em>";
				    }
				    var count = 0;
				    if(be.taskInfo){
						count = be.taskInfo.totalTaskCount;
					}
					var li = "<li onclick=\"openResourceInfoWindow('"+carId+"','"+be.type+"')\" class='resource_item clearfix scLi'><div class='r'>";
					li += "<p class='name'><a href='../cardispatch/cargeneral_index.jsp?carId="+tempCarId+"&type=view' target='_blank'>"+name+"</a>，"+carType+"，"+carState+"</p>";
					li += "<p class='name mt5'>"+driverName+"("+driverPhone+")<em class='name_red'><i class='red f-bold'>"+count+"个</i>任务</em></p></div></li>";
					cul.append($(li));
				}
			}else{
				cul.siblings().find($(".no_result hidden")).show();
				cul.prev().html("共有 0 个结果");
			}
			//生成站址列表
			if(stationList && stationList.length>0){
				sul.prev().html("共有 "+stationList.length+" 个结果");
				var stationUrl = "../../resource/physicalres/getPhysicalresForOperaAction?currentEntityType=Station&modelType=view&showType=showTask&currentEntityId=";
				for(var i=0;i<stationList.length;i++){
					var be = stationList[i];
					var marker = null;
					var isStationExit = false;
					var index = 0;
					for(var j=0;j<allMarkers.length;j++){
						marker = allMarkers[j];
						if(be.key==marker.entityId){
							isStationExit = true;
							index = j;
							break;
						}
					}
					if(isStationExit){
						//更改图标
						var options = {};
						options.icon = "image/staff5.png";
						marker.setOptions(options);
						marker.searchIcon = "image/staff5.png";
						//保存数据
						allMarkers.splice(index,1);
						allMarkers.push(marker);
						searchResultMarkers.push(marker);
					}else{
						//图元不存在，生成图元
						createSingelMarker(be);
					}
					//生成左边搜索结果
					var name = be.name;
					var stationId = be.key;
					var address = be.address;
					var count = 0;
					
					var tempStationId = stationId.substring(stationId.lastIndexOf("_")+1);
					if(name == undefined || name == null || name=='null'){name="";}
					if(address == undefined || address == null || address=='null'){address=" ";}
					if(be.taskInfo){
						count = be.taskInfo.totalTaskCount;
					}
					tempStationId = stationUrl+tempStationId;
					var li = "<li onclick=\"openResourceInfoWindow('"+stationId+"','"+be.type+"')\" class='resource_item clearfix ssLi'><div class='r'>";
					li += "<p class='name'><a href='"+tempStationId+"' target='_blank'>"+name+"</a></p>";
					li += "<p class='name mt5'>抢修任务<i class='red f-bold'>"+count+"个</i>，巡检任务<i class='red f-bold'>0个</i></p>";
					li += "<p class='name'>地址："+address+"</p></div></li>";
					sul.append($(li));
				}
			}else{
				sul.siblings().find($(".no_result hidden")).show();
				sul.prev().html("共有 0 个结果");
			}
			//分页
			pagingColumnByForeground("ssPageContent",$(".ssLi"),6);
			pagingColumnByForeground("scPageContent",$(".scLi"),6);
			pagingColumnByForeground("shPageContent",$(".shLi"),6);
			/*var humanList = queryResult._resource_human;//人员
			var carList = queryResult._resource_car;	//车辆
			var storehouseList = queryResult._resource_storehouse;//仓库
			var stationList = queryResult._resource_station;	//站址
			var mgList = queryResult._resource_maintaingroup;	//维护组
			var sdList = queryResult._resource_shiyebuaddress;	//事业部
			var pgdList = queryResult._resource_projectgroupaddress;	//项目组
			var madList = queryResult._resource_manitainareaaddress;	//维护区域
			var hqtList = queryResult._resource_headerquarteraddress;	//总部？
			//生成搜索结果仓库列表
			if(storehouseList!=null){
				for(var i=0;i<storehouseList.length;i++){
					var be = storehouseList[i];
					var marker = null;
					var isStoreHouseExit = false;
					var index = 0;
					for(var j=0;j<allMarkers.length;j++){
						marker = allMarkers[j];
						if(be.key==marker.entityId){
							isStoreHouseExit = true;
							index = j;
							break;
						}
					}
					if(isStoreHouseExit){
						//更改图标
						var options = {};
						options.icon = "image/staff5.png";
						marker.setOptions(options);
						marker.searchIcon = "image/staff5.png";
						//保存数据
						allMarkers.splice(index,1);
						allMarkers.push(marker);
						searchResultMarkers.push(marker);
					}else{
						//图元不存在，生成图元
						createSingelMarker(be);
					}
					//生成左边搜索结果
					var name = be.name;
					var storeHouseId = be.key;
					var layerType = be.type;
					
					if(name == undefined || name == null || name=='null'){name="";}
					var li=$("<li onclick=\"openResourceInfoWindow('"+storeHouseId+"','"+layerType+"')\"><div class=\"markers-icon\"></div><div class=\"search-results\"><h4>"+name+"</h4></li>");
					li.appendTo(ul);
				}
			}
			//生成搜索结果维护组列表
			if(mgList!=null){
				for(var i=0;i<mgList.length;i++){
					var be = mgList[i];
					var marker = null;
					var isMgExit = false;
					var index = 0;
					for(var j=0;j<allMarkers.length;j++){
						marker = allMarkers[j];
						if(be.key==marker.entityId){
							isMgExit = true;
							index = j;
							break;
						}
					}
					if(isMgExit){
						//更改图标
						var options = {};
						options.icon = "image/staff5.png";
						marker.setOptions(options);
						marker.searchIcon = "image/staff5.png";
						//保存数据
						allMarkers.splice(index,1);
						allMarkers.push(marker);
						searchResultMarkers.push(marker);
					}else{
						//图元不存在，生成图元
						createSingelMarker(be);
					}
					//生成右边搜索结果
					var name = be.name;
					var mgId = be.key;
					var layerType = be.type;
					
					if(name == undefined || name == null || name=='null'){name="";}
					var li=$("<li onclick=\"openResourceInfoWindow('"+mgId+"','"+layerType+"')\"><div class=\"markers-icon\"></div><div class=\"search-results\"><h4>"+name+"</h4></li>");
					li.appendTo(ul);
				}
			}
			//生成搜索结果事业部列表
			if(sdList!=null){
				for(var i=0;i<sdList.length;i++){
					var be = sdList[i];
					var marker = null;
					var isSdExit = false;
					var index = 0;
					for(var j=0;j<allMarkers.length;j++){
						marker = allMarkers[j];
						if(be.key==marker.entityId){
							isSdExit = true;
							index = j;
							break;
						}
					}
					if(isSdExit){
						//更改图标
						var options = {};
						options.icon = "image/staff5.png";
						marker.setOptions(options);
						marker.searchIcon = "image/staff5.png";
						//保存数据
						allMarkers.splice(index,1);
						allMarkers.push(marker);
						searchResultMarkers.push(marker);
					}else{
						//图元不存在，生成图元
						createSingelMarker(be);
					}
					//生成右边搜索结果
					var name = be.name;
					var sdId = be.key;
					var layerType = be.type;
					
					if(name == undefined || name == null || name=='null'){name="";}
					var li=$("<li onclick=\"openResourceInfoWindow('"+sdId+"','"+layerType+"')\"><div class=\"markers-icon\"></div><div class=\"search-results\"><h4>"+name+"</h4></li>");
					li.appendTo(ul);
				}
			}
			//搜索结果项目组列表
			if(pgdList!=null){
				for(var i=0;i<pgdList.length;i++){
					var be = pgdList[i];
					var marker = null;
					var isPgdExit = false;
					var index = 0;
					for(var j=0;j<allMarkers.length;j++){
						marker = allMarkers[j];
						if(be.key==marker.entityId){
							isPgdExit = true;
							index = j;
							break;
						}
					}
					if(isPgdExit){
						//更改图标
						var options = {};
						options.icon = "image/staff5.png";
						marker.setOptions(options);
						marker.searchIcon = "image/staff5.png";
						//保存数据
						allMarkers.splice(index,1);
						allMarkers.push(marker);
						searchResultMarkers.push(marker);
					}else{
						//图元不存在，生成图元
						createSingelMarker(be);
					}
					//生成左边搜索结果
					var name = be.name;
					var pgdId = be.key;
					var layerType = be.type;
					
					if(name == undefined || name == null || name=='null'){name="";}
					var li=$("<li onclick=\"openResourceInfoWindow('"+pgdId+"','"+layerType+"')\"><div class=\"markers-icon\"></div><div class=\"search-results\"><h4>"+name+"</h4></li>");
					li.appendTo(ul);
				}
			}
			//搜索结果维护区域列表
			if(madList!=null){
				for(var i=0;i<madList.length;i++){
					var be = madList[i];
					var marker = null;
					var isMadExit = false;
					var index = 0;
					for(var j=0;j<allMarkers.length;j++){
						marker = allMarkers[j];
						if(be.key==marker.entityId){
							isMadExit = true;
							index = j;
							break;
						}
					}
					if(isMadExit){
						//更改图标
						var options = {};
						options.icon = "image/staff5.png";
						marker.setOptions(options);
						marker.searchIcon = "image/staff5.png";
						//保存数据
						allMarkers.splice(index,1);
						allMarkers.push(marker);
						searchResultMarkers.push(marker);
					}else{
						//图元不存在，生成图元
						createSingelMarker(be);
					}
					//生成右边搜索结果
					var name = be.name;
					var madId = be.key;
					var layerType = be.type;
					
					if(name == undefined || name == null || name=='null'){name="";}
					var li=$("<li onclick=\"openResourceInfoWindow('"+madId+"','"+layerType+"')\"><div class=\"markers-icon\"></div><div class=\"search-results\"><h4>"+name+"</h4></li>");
					li.appendTo(ul);
				}
			}
			//搜索结果公司总部列表？
			if(hqtList!=null){
				for(var i=0;i<hqtList.length;i++){
					var be = hqtList[i];
					var marker = null;
					var isHqtExit = false;
					var index = 0;
					for(var j=0;j<allMarkers.length;j++){
						marker = allMarkers[j];
						if(be.key==marker.entityId){
							isHqtExit = true;
							index = j;
							break;
						}
					}
					if(isHqtExit){
						//更改图标
						var options = {};
						options.icon = "image/staff5.png";
						marker.setOptions(options);
						marker.searchIcon = "image/staff5.png";
						//保存数据
						allMarkers.splice(index,1);
						allMarkers.push(marker);
						searchResultMarkers.push(marker);
					}else{
						//图元不存在，生成图元
						createSingelMarker(be);
					}
					//生成右边搜索结果
					var name = be.name;
					var hqtId = be.key;
					var layerType = be.type;
					
					if(name == undefined || name == null || name=='null'){name="";}
					var li=$("<li onclick=\"openResourceInfoWindow('"+hqtId+"','"+layerType+"')\"><div class=\"markers-icon\"></div><div class=\"search-results\"><h4>"+name+"</h4></li>");
					li.appendTo(ul);
				}
			}
			ul.appendTo(searchResultDiv);
			//显示右边信息
			$("#cleanSearchResultButton").css("display","block");
			$("#gis_content_right_main").css("display","block");
			$("#searchResultLi").css("display","block");
			$("#searchResultLi").attr("class","active");
			$("#searchResultDiv").css("display","block");
			$("#gis_content_right_data").css("display","none");
			$("#assignResultLi").removeAttr("class");*/
		} 
	});
}
//首页
function firstPage(){
	var curWO = $("#curBizType").html();
	var ul = "";
	var type = "";
	if(curWO=='urgentrepair'){
		//类型为工单
		ul = "urUl li";
		type = "ur";
	}else if(curWO=='taskorder'){
		//类型为任务单
		ul = "toUl li";
		type = "to";
	}else if(curWO=='cardispatch'){
		//类型为车辆调度单
		ul = "cdUl li";
		type = "cd";
	}else{
		//类型为物资调度单
		ul = "rdUl li";
		type = "rd";
	}
	var pageIndex = 0;
	var pageSize = $("#pageSize").val();
	$("#curPageIndex").val(pageIndex+1);
	$("#"+ul).each(function(){
		var url = $(this).attr("onclick");
		var ck = $(this).children().attr("class");
		var bizTypeCode = $(this).attr("bizTypeCode");
		var workOrderType = $(this).attr("workOrderType");
		if(ck&&ck=='selected'){
			var param = {};
			param.bizTypeCode = bizTypeCode;
			param.taskOrderType = workOrderType;
			param.workOrderType = workOrderType;
			param.pageIndex = pageIndex;
			param.pageSize = pageSize;
			param.orderNumber = order_number;
			param.orderTitle = order_title;
			param.orderState = order_state;			
			getOrderListByConditions(curWO,param);
			return;
		}
	});
}
//尾页
function lastPage(){
	var curWO = $("#curBizType").html();
	var ul = "";
	var type = "";
	if(curWO=='urgentrepair'){
		//类型为工单
		ul = "urUl li";
		type = "ur";
	}else if(curWO=='taskorder'){
		//类型为任务单
		ul = "toUl li";
		type = "to";
	}else if(curWO=='cardispatch'){
		//类型为车辆调度单
		ul = "cdUl li";
		type = "cd";
	}else{
		//类型为物资调度单
		ul = "rdUl li";
		type = "rd";
	}
	var pageIndex = $("#totalPage").html();
	var pageSize = $("#pageSize").val();
	$("#curPageIndex").val(pageIndex);
	$("#"+ul).each(function(){
		var url = $(this).attr("onclick");
		var ck = $(this).children().attr("class");
		var bizTypeCode = $(this).attr("bizTypeCode");
		var workOrderType = $(this).attr("workOrderType");
		if(ck&&ck=='selected'){
			var param = {};
			param.bizTypeCode = bizTypeCode;
			param.taskOrderType = workOrderType;
			param.workOrderType = workOrderType;
			param.pageIndex = pageIndex;
			param.pageSize = pageSize;
			param.orderNumber = order_number;
			param.orderTitle = order_title;
			param.orderState = order_state;			
			getOrderListByConditions(curWO,param);
			return;
		}
	});
}
//上一页
function backPage(){
	var curWO = $("#curBizType").html();
	var ul = "";
	var type = "";
	if(curWO=='urgentrepair'){
		//类型为工单
		ul = "urUl li";
		type = "ur";
	}else if(curWO=='taskorder'){
		//类型为任务单
		ul = "toUl li";
		type = "to";
	}else if(curWO=='cardispatch'){
		//类型为车辆调度单
		ul = "cdUl li";
		type = "cd";
	}else{
		//类型为物资调度单
		ul = "rdUl li";
		type = "rd";
	}
	var pageSize = $("#pageSize").val();
	var pageIndex = $("#curPageIndex").val();
	var totalPage = $("#totalPage").html();
	pageIndex = parseInt(pageIndex)-1;
	if(parseInt(pageIndex) < 2){
		pageIndex = 0;
		$("#curPageIndex").val(1);
	}else{
		$("#curPageIndex").val(pageIndex);
	}
	$("#"+ul).each(function(){
		var url = $(this).attr("onclick");
		var ck = $(this).children().attr("class");
		var bizTypeCode = $(this).attr("bizTypeCode");
		var workOrderType = $(this).attr("workOrderType");
		if(ck&&ck=='selected'){
			var param = {};
			param.bizTypeCode = bizTypeCode;
			param.taskOrderType = workOrderType;
			param.workOrderType = workOrderType;
			param.pageIndex = pageIndex;
			param.pageSize = pageSize;
			param.orderNumber = order_number;
			param.orderTitle = order_title;
			param.orderState = order_state;			
			getOrderListByConditions(curWO,param);
			return;
		}
	});
}
//下一页
function nextPage(){
	var curWO = $("#curBizType").html();
	var ul = "";
	var type = "";
	if(curWO=='urgentrepair'){
		//类型为工单
		ul = "urUl li";
		type = "ur";
	}else if(curWO=='taskorder'){
		//类型为任务单
		ul = "toUl li";
		type = "to";
	}else if(curWO=='cardispatch'){
		//类型为车辆调度单
		ul = "cdUl li";
		type = "cd";
	}else{
		//类型为物资调度单
		ul = "rdUl li";
		type = "rd";
	}
	var pageSize = $("#pageSize").val();
	var pageIndex = $("#curPageIndex").val();
	var totalPage = $("#totalPage").html();
	pageIndex = parseInt(pageIndex)+1;
	if(parseInt(pageIndex) > parseInt(totalPage)){
		pageIndex = totalPage;
	}
	$("#curPageIndex").val(pageIndex);
	$("#"+ul).each(function(){
		var url = $(this).attr("onclick");
		var ck = $(this).children().attr("class");
		var bizTypeCode = $(this).attr("bizTypeCode");
		var workOrderType = $(this).attr("workOrderType");
		if(ck&&ck=='selected'){
			var param = {};
			param.bizTypeCode = bizTypeCode;
			param.taskOrderType = workOrderType;
			param.workOrderType = workOrderType;
			param.pageIndex = pageIndex;
			param.pageSize = pageSize;
			param.orderNumber = order_number;
			param.orderTitle = order_title;
			param.orderState = order_state;			
			getOrderListByConditions(curWO,param);
			return;
		}
	});
}
//跳转到指定页
function skipToPage(){
	var curWO = $("#curBizType").html();
	var ul = "";
	var type = "";
	if(curWO=='urgentrepair'){
		//类型为工单
		ul = "urUl li";
		type = "ur";
	}else if(curWO=='taskorder'){
		//类型为任务单
		ul = "toUl li";
		type = "to";
	}else if(curWO=='cardispatch'){
		//类型为车辆调度单
		ul = "cdUl li";
		type = "cd";
	}else{
		//类型为物资调度单
		ul = "rdUl li";
		type = "rd";
	}
	var pageSize = $("#pageSize").val();
	var pageIndex = $("#curPageIndex").val();
	var totalPage = $("#totalPage").html();
	if(parseInt(pageIndex) > parseInt(totalPage)){
		pageIndex = totalPage;
	}
	$("#curPageIndex").val(pageIndex);
	$("#"+ul).each(function(){
		var url = $(this).attr("onclick");
		var ck = $(this).children().attr("class");
		var bizTypeCode = $(this).attr("bizTypeCode");
		var workOrderType = $(this).attr("workOrderType");
		if(ck&&ck=='selected'){
			var param = {};
			param.bizTypeCode = bizTypeCode;
			param.taskOrderType = workOrderType;
			param.workOrderType = workOrderType;
			param.pageIndex = pageIndex;
			param.pageSize = pageSize;
			param.orderNumber = order_number;
			param.orderTitle = order_title;
			param.orderState = order_state;			
			getOrderListByConditions(curWO,param);
			return;
		}
	});
}
/*退出连线派发任务*/
function bindKeyBoardEntryEvent(){
	document.onkeypress = function(e){
		var curKey=0,e=e||event;
	　　	curKey=e.keyCode||e.which||e.charCode;
	　　	//按了Esc键退出连线
		if(curKey==27){
			if(mousePolyline&&selectedMarkerArray.length>0){
				cleanAssignResultEvent();
				map.map_.draggableCursor="auto";
			}
			if(currentInfoWindow){
				currentInfoWindow.close();
			}
		}
		if(curKey==13){
			$("#topSearchButton").click();
		}
	};
	
}
/**获取重叠图标**/
function getOverlapMarker(marker){
	if(!marker){return false;}
	var overLapMarkers = new Array();	//重叠图标列表
	var latlng = marker.getPosition();
	for(var i=0;i<allMarkers.length;i++){
		var mk = allMarkers[i];
		var mkLatlng = mk.getPosition();
		
	}
}


/*创建单个Marker对象*/
function createSingelMarker(be){
	//图元不存在，生成图元
	var lat = be.latitude;
	var lng = be.longitude;
	lat = stringToFloat(""+lat);
	lng = stringToFloat(""+lng);
	var position = new ILatLng(lat,lng);
	position = IMapComm.gpsToMapLatLng(position);
	var marker = new IMarker(position,be.name,{});
	//更改图标
	var options = {};
	options.icon = "image/staff5.png";
	marker.setOptions(options);
	var taskText = "";
	if(be.type == "_resource_maintaingroup" || be.type=="_resource_headerquarteraddress" || be.type=="_resource_shiyebuaddress" || be.type=="_resource_projectgroupaddress" || be.type=="_resource_manitainareaaddress"){
		taskText=""+be.title+"<span style=\"color:black;\">(</span><span style=\"color:red;\">"+be.taskInfo.totalTaskCount+"</span><span style=\"color:black;\">人)</span>"+"";	
	}else{
		taskText=""+be.title+"<span style=\"color:black;\">(</span><span style=\"color:red;\">"+be.taskInfo.totalTaskCount+"</span><span style=\"color:black;\">单)</span>"+"";	
	}
	var statusText = ""+taskText;
	var statusLabel = "<lable style='text-shadow:1px 1px 10px #000000;color: #222222;font-size: 12px;'>"+statusText+"</lable>";
	marker.textOverlay_ = new ITextOverlay(position,statusLabel,{});
	if(isTitleShow){
		marker.textOverlay_.setMap(map);
		marker.textOverlay_.isShow_ = true;
	}
	marker.setMap(map);
	//保存数据到Marker对象
	marker.entityId = be.key;
	marker.entityType = be.type;
	marker.isVisible = be.isVisible;
	marker.taskTitle = be.title;
	marker.basicIcon = be.icon;
	marker.totalTaskCount = be.taskInfo.totalTaskCount;
	marker.statusLabel_ = statusLabel;
	marker.searchIcon = "image/staff5.png";
	if(be.isOnDuty){
		if(be.isOnDuty!='null'){
			// LineOff(离线) : 0 ,Travel (行驶中 ) : 1 ,Static (静止 ) : 2 ,Init (待初始化 ) : 3 
		    var carState = be.isOnDuty;
		    var carIcon = "image/car-LineOff.png";	//离线或待初始化
		    var carState = be.isOnDuty;
		    if(carState && carState=='1'){
		    	carIcon = "image/car.png";
		    }else if(carState && carState=='2'){
		    	carIcon = "image/car-Static.png";
		    }
			marker.carState = carState;
			marker.stateIcon = carIcon;
		}
	}
	//保存数据
	allMarkers.push(marker);
	searchResultMarkers.push(marker);
	//绑定鼠标单击事件
	bindMarkerClickEvent(marker);
}
/**
 * 刷新Marker对象
 */
function refreshMarker(){
	var queryUrl="refreshGraphElementAction";
	$.ajax({ 
	   type : "post", 
	   url : queryUrl, 
	   async : true, 
	   success : function($data){
		    var res = $data;
			var json = eval("("+res+")");
			//{layerManagerName:'',layerList:[{key:'',name:'',
			// simpleGeListWithTaskInfo:[{key:'',taskInfo:{totalTaskCount:'',taskDetailInfos:[{name:'',count:'',needShowLittleIcon:'',littleIcon:''}]}}]}]}
			//逻辑：循环获取回来的list,拿出 图层Id,循环与已经保存的allLayers比较,获取allLayers的visiableList,再循环visiableList对比,刷新Marker的Title
			//注意：返回来的数据格式： 图层key,simpleGeListWithTaskInfo[图元key,taskInfo[totalTaskCount....]]	要刷新的是图元的title 
			var layerList = json.layerList;
			if(layerList){
				for(var i=0;i<layerList.length;i++){
					//获取每个图元的可见List
					var data = layerList[i];
					var layerKey = data.key;
					var taskInfoList = data.simpleGeListWithTaskInfo;
					for(var m=0;m<taskInfoList.length;m++){
						var task = taskInfoList[m];
						for(var j=0;j<allMarkers.length;j++){
							var marker = allMarkers[j];
							var entityId = marker.entityId;
							//console.log("图元key==="+task.key+"======EntityId==="+entityId+"==="+allMarkers.length);
							if(entityId == task.key){
								//刷新该Marker的Title
								var totalTaskCount = task.taskInfo.totalTaskCount;
								if(totalTaskCount == "" || totalTaskCount == null){
									totalTaskCount = 0;
								}
								var position = marker.getPosition();
								//alert(".."+task.taskInfo.toSource());
								
								//刷新车辆和人的经纬度 
								if(marker.entityType == "_resource_human" || marker.entityType == "_resource_car"){
									var lat = task.latitude;
									var lng = task.longitude;
									var markerLng = position.getLongitude();
									var markerLat = position.getLatitude();
									//console.log(marker.taskTitle+":"markerLat+","+markerLng+";task:"+lat+","+lng);
									if(markerLng!=lng || markerLat!=lat){
										var latlng = new ILatLng(lat,lng);
										latlng = IMapComm.gpsToMapLatLng(latlng);
										marker.setPosition(latlng);
										//重新生成textOverlay
										marker.textOverlay_.setMap(null);
										marker.textOverlay_ = undefined;
										marker.textOverlay_ = new ITextOverlay(latlng,marker.statusLabel_,{});
										if(isTitleShow){
											marker.textOverlay_.setMap(map);
											marker.textOverlay_.isShow_ = true;
										}
									}
								}
								
								var taskDetailList;
								if(task.taskInfo.taskDetailInfos){
									taskDetailList = task.taskInfo.taskDetailInfos;
								
									var iconList = "";
									for(var n=0;n<taskDetailList.length;n++){
										var visiable = taskDetailList[n].needShowLittleIcon;
										var ltCount = taskDetailList[n].count;
										if(visiable == true && ltCount>0){
											var iconSrc = taskDetailList[n].littleIcon;
											iconList+="<img src=\""+iconSrc+"\" />";
										}
									}
									if(marker.imageOverlay_){
										marker.imageOverlay_.setMap(null);
										marker.imageOverlay_ = undefined;
									}
									marker.imageOverlay_ = new IImageOverlay(position,iconList,{});
									marker.imageOverlay_.setMap(map);
								}else{
									if(marker.imageOverlay_){
										marker.imageOverlay_.setMap(null);
										marker.imageOverlay_ = undefined;
									}
								}
								//console.log(marker.taskTitle+"===MT:"+marker.totalTaskCount+",RT:"+totalTaskCount);
								if(marker.totalTaskCount!=totalTaskCount){
									marker.textOverlay_.setMap(null);
									marker.textOverlay_ = undefined;
									var title = marker.taskTitle;
									if(marker.entityType == "_resource_maintaingroup" || marker.entityType=="_resource_headerquarteraddress" || marker.entityType=="_resource_shiyebuaddress" || marker.entityType=="_resource_projectgroupaddress" || marker.entityType=="_resource_manitainareaaddress"){
										title+="<span style=\"color:black;\">(</span><span style=\"color:red;\">"+totalTaskCount+"</span><span style=\"color:black;\">人)</span>"+"";
									}else{
										title+="<span style=\"color:black;\">(</span><span style=\"color:red;\">"+totalTaskCount+"</span><span style=\"color:black;\">单)</span>"+"";
									}
									var statusText = ""+title;
									var statusLabel = "<lable class='icon_shadow'>"+statusText+"</lable>";
									marker.textOverlay_ = new ITextOverlay(position,statusLabel,{});
									if(isTitleShow){
										marker.textOverlay_.setMap(map);
										marker.textOverlay_.isShow_ = true;
									}
									marker.statusLabel_ = statusLabel;
									marker.totalTaskCount = totalTaskCount;
								}
								break;
							}
						}
					}
				}
			}
	     } 
	});
}
//初始化地图
function initMap(){
	var zoom = 16;
	var latText = $("#latText").val();
	var lngText = $("#lngText").val();
	var lat = stringToFloat(latText);
	var lng = stringToFloat(lngText);
	var centerLatLng = new ILatLng(lat,lng);
	map = new IMap("gis_map",centerLatLng,zoom,{});
	
	//TODO 请求数据库，获取当前用户所属区域坐标
	$.ajax({ 
        type : "post", 
        url : "getCurrentUserAreaAction", 
        dataType : "json",
        async : false, 
        success : function($data){ 
        	var result = $data;
        	if(result){
        		lat = result.latitude;
				lng = result.longitude;
        		centerLatLng = new ILatLng(lat,lng);
				centerLatLng = IMapComm.gpsToMapLatLng(centerLatLng);
				map.panTo(centerLatLng);
				var isTaskDispatcher = result.isTaskDispatcher;
				if(isTaskDispatcher){
					$(".cjgd_choose").show();
				}
        	}
        }
    });   
	
	setTimeout(function(){
		//$("#loading_div").css("display","block");
		showWaitCue();
		   	
		//初始数据
		currentInfoWindow = new IInfoWindow("",{"position":centerLatLng});
		
		var currentVisiableKM = getVisiableKMByZoom();
		var bounds = map.getBounds();	//当前窗口范围
		//Hard code
		var swlat,swlng,nelat,nelng;
		if(bounds!=null){
			swlat=bounds.getSw().getLatitude();
			swlng=bounds.getSw().getLongitude();
			nelat=bounds.getNe().getLatitude();
			nelng=bounds.getNe().getLongitude();
		}else{
			//Hard code防止获取不到数据
			swlat=23.05426437989033;
			swlng=113.31888037387466;
			nelat=23.10172044456083;
			nelng=113.44685393039322;
		}
		var param = {'swLat':swlat,'swLng':swlng,'neLat':nelat,'neLng':nelng,currentVisibleKM:currentVisiableKM,zoom:zoom,'centerLatLng.latitude':centerLatLng.getLatitude(),'centerLatLng.longitude':centerLatLng.getLongitude()};
		var queryUrl = "initMapAction";
		$.ajax({ 
	        type : "post", 
	        url : queryUrl, 
	        data : param, 
	        async : true, 
	        timeout: 120000,
	　　　　　error: function (xmlHttpRequest, error) {
				closePleaseWaitTimer();
				//地图可视范围变化事件
				bindBoundsChangedEvent();
	　　　　　 　  alert("出错啦！！");
	　　　　　},
	        success : function($data){ 
	        	var result = $data;
				var res = eval("("+result+")");
				closePleaseWaitTimer();
				initMarker(res.layerList);
				//获取左边树
				showLayerTree();
				//地图可视范围变化事件
				bindBoundsChangedEvent();
				//获取监控资源
				setTimeout(function(){
			    	getMonitorResource();
			    },2000);
				setTimeout(function(){
			    	//手动刷新Marker对象
			    	refreshMarker();
			    },5000);
	        }
	    });
		//$("#loading_div").css("display","none");
	},1500);
	//获取用户工单列表
	getUserWorkOrderListByPage('urgentrepair','pendingWorkOrder',0,10);
}
var loadingTimer = null;
var loadingSecond = 1;
var loadingSmall = false;
//显示稍等提示
function showWaitCue () {
	loadingSecond = 1;
	$("#please_res_wait_cue_div").css("display","block");
	$("#please_res_wait_cue_div").find(".please_wait_second").text(1);
	//clearInterval
	loadingTimer = setInterval( pleaseWaitCount , 1000 ); 
}

//开始计时刷新地图时间
function pleaseWaitCount () {
	if ( loadingSecond == 5 || loadingSmall) {
		loadingSmall = false;
		$(".wait_white").fadeOut(1000);
		var wait_show_div_count = $(".please_wait_cue_div:visible").length;
		var top = 23;
		if ( wait_show_div_count > 1 ) {
			top += 32 * (wait_show_div_count - 1) ;
		}
		$("#please_res_wait_cue_div").animate({"top": top+"px","left":"50%" , "min-width" : "250px" , "border-radius" : "15px" , "font-size" : "12px" , "line-height" : "20px" , "min-height" : "20px" },1200, function(){
			$("#please_res_wait_cue_div").css( {"background-color":"#777"});
		});
	}
	
	$("#please_res_wait_cue_div").find(".please_wait_second").text(loadingSecond);
	loadingSecond++;
}
function showLoading(){
	if($("#please_res_wait_cue_div").css("display") == "block"){
		return ;
	}
	loadingSecond = 1;
	$("#please_res_wait_cue_div").find(".please_wait_second").text(1);
	//clearInterval
	loadingTimer = setInterval( pleaseWaitCount , 1000 ); 
}

//停止计时刷新地图时间
function closePleaseWaitTimer() {
	$("#please_res_wait_cue_div").css("display","none");
	$(".wait_white").css("display","none");
	clearInterval(loadingTimer);
	loadingTimer = null;
	loadingSmall = false;
	//$(opening_wait_div).animate({"top": 23+"px"},1000);
}
//工作区自适应
function workspaceSelfAdaption(){
//    var mainDiv = $("#mainDiv");
    var allWidth = $(window).width();
    var allHeight = $(window).height();

    //垂直
    var v1 = $("#vertical_div1");
    var v2 = $("#vertical_div2");
    var v3 = $("#vertical_div3");
    var vh1 = v1.height();
    var vh2 = v2.height();
    v3.height(allHeight-vh1-vh2);  
    
    //水平
    var h1 = $("#horizontal_div1");
    var h2 = $("#horizontal_div2");
    var h3 = $("#horizontal_div3");
    var hw1 = h1.width();
    var hw2 = h2.width();
    h3.width(allWidth-hw1-hw2);
    
    $(document).resize();
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
//甘特图月份切换
function showResourceMonthGantt(){
	if($(".cwiDatepicker").prev().val()){
		var carId = $(".cwiDatepicker").prev().val();
		showResourceMonthGanttByConditions(".cwiDatepicker",carId,"car");
	}
	if($(".swiDatepicker").prev().val()){
		var staffId = $(".swiDatepicker").prev().val();
		showResourceMonthGanttByConditions(".swiDatepicker",staffId,"people");
	}
	if($(".ssDatepicker").prev().val()){
		var staffId = $(".ssDatepicker").prev().val();
		showResourceMonthGanttByConditions(".ssDatepicker",staffId,"people");
	}
	if($(".csDatepicker").prev().val()){
		var carId = $(".csDatepicker").prev().val();
		showResourceMonthGanttByConditions(".csDatepicker",carId,"car");
	}
	if($(".report_gantt_datepicker").prev().val()){
		var resourceId = $(".report_gantt_datepicker").prev().val();
		var resourceType = $(".report_gantt_datepicker").prev().prev().val();
		showResourceMonthGanttByConditions(".report_gantt_datepicker",resourceId,resourceType);
	}
}
//按条件获取月份任务
function showResourceMonthGanttByConditions(content,resourceId,resourceType){
	var year = $(content+" .ui-datepicker-year").html().replace("年","-");
	var month = $(content+" .ui-datepicker-month").html().replace("月","-");
	var taskDate = year+month;
	var endDate = "30";
	$(content+" .ui-datepicker-calendar tbody td a").each(function(){
		endDate = $(this).html();
	});
	$.ajax({
		url : "getResourceMonthTaskAction",
		data : {"taskDate":taskDate,"endDate":endDate,"resourceId":resourceId,"resourceType":resourceType},
		dataType : 'json',
		async:false,
		type : 'POST',
		success : function(result){
			if(result){
				$(content+" .ui-datepicker-calendar tbody td a").each(function(){
					var i = $(this).html();
					var ti = result[i];
					var hasTask = ti.hasTask;
					// 有无任务颜色显示不同，有任务为红色，无任务为绿色
					var cs = "bg_green";
					if(hasTask&&hasTask=='true'){
						cs = "bg_red";
						$(this).attr("class",cs);
					}
				});
			}
		}
	});
}
/**
 * 给标记绑定鼠标单击事件
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
		},500);
	}); 
};

/**
 * 显示当前标记对象的信息
 * @param {Object} entityId
 */
function showMarkerDetail(marker){
	if(!currentInfoWindow)return;
	//对象Id
	var entityId = marker.entityId;
	
	//对象类型
	var entityType = marker.entityType;
	if(!entityType){
		alert("对象类型为空！");
		return false;
	}
	//保存Marker对象
	selectedMarker = null;
	selectedMarker = marker;	//用于选中事件
	var position = marker.getPosition();
	var param = {entityId:entityId};
	var queryUrl = "";
	var isChecked = false;
	for(var k=0;k<choosedMarkerArray.length;k++){
		var mk = choosedMarkerArray[k];
		if(mk.entityId == marker.entityId){
			isChecked = true;
		}
	}
	//判断不同对象类型，获取不同信息，弹出信息框
	if(entityType == "_resource_station"){
		if(selectedMarkerArray!=null&&selectedMarkerArray.length==1){
			BindLiningDispatchTaskEvent(entityId,entityType);
			return false;
		}
		//获取站址信息
		if(currentInfoWindow.bizValue_  == entityId){
			//currentInfoWindow.open(map);
			//selectedMarker.openInfoWindow(currentInfoWindow);
		}else{
			
		}
		queryUrl = "getTaskDetailInfoOfGraphElementAction";
		var param = {resourceTypeAndId:entityType+","+entityId,infoType:'resourceInfoWindow'};
		$.ajax({
			type : "post", 
		    url : queryUrl, 
		    data : param, 
		    async : false, 
		    success : function($data){ 
		    	var res = $data;
				var queryResult = eval("("+res+")");
				var stationName = queryResult.basicInfo.name;
				var rank = queryResult.basicInfo.rank;
				var address = queryResult.basicInfo.address;
				var key = queryResult.basicInfo.key;
				var stationAccessUrl = queryResult.basicInfo.stationAccessUrl;
				
				if(stationName == null || stationName == 'null'){stationName="";}
				if(rank == null || rank == 'null'){rank="未知";}
				if(address == null || address == 'null'){address="";}
				
				var tempStationId="";
				tempStationId = entityId.substring(entityId.lastIndexOf("_")+1);
				var workList = queryResult.workList;
				//hard code
				//var stationUrl = "http://proxytest.iscreate.com:7777/networkresourcemanage/resource/physicalres/getPhysicalresForOperaAction?currentEntityType=BaseStation&currentEntityLabel="+tempStationId+"&modelType=view&showType=showTask";
				var stationUrl = "../../resource/physicalres/getPhysicalresForOperaAction?currentEntityType=Station&modelType=view&showType=showTask&currentEntityId="+tempStationId;
				
				var content = "<div class=\"dialog-title-bar clearfix\"><div class=\"dialog-bar-m\"><h3><label>站址：</label><span><a class=\"blue\" target=\"_blank\" href=\""+stationUrl+"\">"+stationName+"</a> &nbsp;<em class=\"dialog-ji selectEntity\">"
				if(isChecked){
					content+="<input class=\"infoWindowCheckbox\" type=\"checkbox\" checked=\"checked\"  onclick=\"selecteMarker('"+entityType+"','"+key+"',this)\" />&nbsp;选中站址</em></span></h3><h3><label>地址：</label><span>"+address+"</span></h3></div></div>";
				}else{
					content+="<input class=\"infoWindowCheckbox\" type=\"checkbox\"  onclick=\"selecteMarker('"+entityType+"','"+key+"',this)\" />&nbsp;选中站址</em></span></h3><h3 class=\"clearfix\"><label>地址：</label><span>"+address+"</span></h3></div></div>";
				}
				content+="<div class=\"dialog-content clearfix\"><div class=\"dialog-content-box\"><div class=\"dialog-content-tab\"><ul class=\"dialog-tabmenu clearfix\"><li class=\"on\">当前工单/任务单</li><li>包含逻辑基站</li> <li style=\"border-right:1px solid #ccc;\">周边搜索</li></ul>";
			    content+="<dl class=\"dialog-tabcontent\" class=\"clearfix\">";
			    //循环生成用户能创建的工单类型
			    content +="<dd><h4></h4><div class=\"dialog-task\"><ul>";
				//TODO循环生成任务单、工单  把工单、任务单标识保存在li里面
				var ownWorkOrderList = queryResult.ownWorkOrderList;
				if(ownWorkOrderList!=null){
					for(var i=0;i<ownWorkOrderList.length;i++){
					   	var ow = ownWorkOrderList[i];
					   	var orderNum = ow.orderNum;
					   	var taskName = ow.taskName;
					   	var statuName = ow.statuName;
					   	var locationHref = "../../"+ow.locationHref+"?WOID="+orderNum;
						content+="<li><input class=\"vm stationRadio\" type=\"radio\" name=\"stationRadio\" value=\""+orderNum+"\" />&nbsp;<a href='"+locationHref+"' target=\"_blank\">"+orderNum+"</a>,"+taskName+","+statuName+"</li>";
					}
				}
				var ownTaskOrderList = queryResult.ownTaskOrderList;
				if(ownTaskOrderList!=null){
					for(var i=0;i<ownTaskOrderList.length;i++){
					   	var ot = ownTaskOrderList[i];
					   	var orderNum = ot.orderNum;
					   	var taskName = ot.taskName;
					   	var statuName = ot.statuName;
					   	var woid=ot.parentWoId;
					   	var locationHref = "../../"+ot.locationHref+"?TOID="+orderNum+"&WOID="+woid;
						content+="<li><input class=\"vm stationRadio\" type=\"radio\" name=\"stationRadio\" value=\""+orderNum+"\" />&nbsp;<a href='"+locationHref+"' target=\"_blank\">"+orderNum+"</a>,"+taskName+","+statuName+"</li>";
					}
				}
				var notOwnTaskOrderList = queryResult.notOwnTaskOrderList;
				if(notOwnTaskOrderList!=null){
					for(var i=0;i<notOwnTaskOrderList.length;i++){
					   	var not = notOwnTaskOrderList[i];
					   	var orderNum = not.orderNum;
					   	var taskName = not.taskName;
					   	var statuName = not.statuName;
					   	var woid=ot.parentWoId;
					   	var locationHref = "../../"+not.locationHref+"?TOID="+orderNum+"&WOID="+woid;
						content+="<li><input class=\"vm stationRadio\" disabled=\"disabled\" type=\"radio\" name=\"stationRadio\" value=\""+orderNum+"\" />&nbsp;<a href='"+locationHref+"' target=\"_blank\">"+orderNum+"</a>,"+taskName+","+statuName+"</li>";
					}
				}
				var notOwnWorkOrderList = queryResult.notOwnWorkOrderList;
				if(notOwnWorkOrderList!=null){
					for(var i=0;i<notOwnWorkOrderList.length;i++){
					   	var now = notOwnWorkOrderList[i];
					   	var orderNum = now.orderNum;
					   	var taskName = now.taskName;
					   	var statuName = now.statuName;
					   	var locationHref = "../../"+now.locationHref+"?WOID="+orderNum;
						content+="<li><input class=\"vm stationRadio\" disabled=\"disabled\" type=\"radio\" name=\"stationRadio\" value=\""+orderNum+"\" />&nbsp;<a href='"+locationHref+"' target=\"_blank\">"+orderNum+"</a>,"+taskName+","+statuName+"</li>";
					}
				}
			    content+="</ul></div></dd>";
			    content+="<dd style=\"display:none\">";
			    var baseStationList = queryResult.basicInfo.baseStationList;
			   
			    content+="<ul>";
			    if(baseStationList){
			    	for(var i=0;i<baseStationList.length;i++){
			    		var bs = baseStationList[i];
			    		var baseStationId = bs.baseStationId;
			    		var baseStationName = bs.baseStationName;
			    		var baseStationGrade = bs.baseStationGrade;
			    		var baseStationType = bs.baseStationType;
			    		if(!baseStationName||baseStationName=='null'){baseStationName=" ";}
			    		if(!baseStationType || baseStationType=='null'){baseStationType="";}
			    		if(!baseStationGrade || baseStationGrade=='null'){baseStationGrade=" ";}
			    		
			    		content+="<li>基站：<a href='#'>"+baseStationName+"</a>(等级："+baseStationGrade+") - <a href='#' onclick=\"showMoreMenu(this)\" bsid='"+baseStationId+"' bst='"+baseStationType+"' address='"+address+"'>创建工单</a></li>";
			    	}
			    }
			    content+="</ul></dd><dd style=\"display:none\"><table><tr><td>网络设施：</td><td><input type=\"checkbox\" class=\"netWorkCheckBox\" value=\"_resource_manhole\"/>&nbsp;管井&nbsp;&nbsp;</td></tr><tr><td>生产资源：</td><td><input type=\"checkbox\" value=\"_resource_human\" class=\"productionCheckBox\" checked=\"checked\"/>&nbsp;人员&nbsp;&nbsp;<input type=\"checkbox\" value=\"_resource_car\"  class=\"productionCheckBox\"/>&nbsp;车辆&nbsp;&nbsp;<input type=\"checkbox\"  class=\"productionCheckBox\" value=\"_resource_storehouse\"/>&nbsp;仓库</td></tr></table>";
			    content+="<div class=\"dd_bottom\"><input type=\"text\" id=\"distanceText\" class=\"distanceText\" value=\"500\"/>&nbsp;米范围内&nbsp;&nbsp;<input type=\"text\" value=\"\" id=\"queryNameText\" style='width:100px'/>&nbsp;&nbsp;<input class=\"dialog-but\" type=\"button\" value=\"搜索\" onclick=\"searchByConditionEvent()\" /></div></dd></dl></div></div></div><div class=\"dd_bottom\"><input class=\"dialog-but\" type=\"button\" onclick=\"BindLiningDispatchTaskEvent('"+key+"','"+entityType+"')\" value=\"连线派发任务\" /></div>";
				selectedMarker.workList = workList;
				currentInfoWindow.bizValue_ = key;
				currentInfoWindow.setContent(content);
				currentInfoWindow.setPosition(position);
				//currentInfoWindow.open(map);
				selectedMarker.openInfoWindow(currentInfoWindow);
				//table切换
				tableToggle();
		    } 
		});
	}else if(entityType == "_resource_human"){	//人员弹框信息
		if(selectedMarkerArray!=null&&selectedMarkerArray.length==1){
			BindLiningDispatchTaskEvent(entityId,entityType);
			return false;
		}
		if(currentInfoWindow.bizValue_  == entityId){
			//currentInfoWindow.open(map);
			selectedMarker.openInfoWindow(currentInfoWindow);
		}else{
			queryUrl = "getTaskDetailInfoOfGraphElementAction";
			var param = {resourceTypeAndId:entityType+","+entityId,infoType:'resourceInfoWindow'};
			$.ajax({
				type : "post", 
			    url : queryUrl, 
			    data : param, 
			    async : true, 
			    success : function($data){ 
			    	var res = $data;
					var queryResult = eval("("+res+")");
					var skillList = queryResult.skillList;
					var basicInfo = queryResult.basicInfo;
					var busyOrNot = queryResult.busyOrNot;
					var staffName = basicInfo.staffName;
					var sex = basicInfo.sex;
					var contactPhone = basicInfo.contactPhone;
					var staffId = basicInfo.staffId;
					
					if(staffName == null || staffName == 'null'){staffName="";}
					if(sex == null || sex == 'null'){sex="";}
					if(contactPhone == null || contactPhone == 'null'){contactPhone="";}
					
					//che.yd 添加人员的查看信息链接
					//humanresource/getOneStaffAction?userId=li.sj
					if(staffId!=null){
						staffId = staffId.substring(staffId.lastIndexOf("_")+1);
					}
					
					var content = "<div class=\"dialog-title-bar clearfix\"><div class=\"dialog-bar-m clearfix\"><h3><b>人员：</b><a href=\"../staffduty/showStaffInfo.jsp?staffId="+staffId+"\" target=\"_blank\" class=\"blue\">"+staffName+"</a> &nbsp;</h3><h3><label>性别：</label><span class=\"blue\">"+sex+" &nbsp;</span></h3><h3><label>电话：</label><span>"+contactPhone+" &nbsp;<em class=\"dialog-ji selectEntity\">";
					if(isChecked){
						content+="<input type=\"checkbox\" checked=\"checked\"  onclick=\"selecteMarker('"+entityType+"','"+staffId+"',this)\" />&nbsp;选中人员</em></span></h3></div></div>";
					}else{
						content+="<input type=\"checkbox\"  onclick=\"selecteMarker('"+entityType+"','"+staffId+"',this)\" />&nbsp;选中人员</em></span></h3></div></div>";
					}
					content+="<div class=\"dialog-content clearfix\"><div class=\"dialog-content-box\"><div class=\"dialog-content-tab\"><ul class=\"dialog-tabmenu clearfix\"><li class=\"on\">技能</li><li>闲忙任务数</li><li style=\"border-right:1px solid #ccc;\">他的储物箱</li></ul><dl class=\"dialog-tabcontent\" class=\"clearfix\"><dd><ul class=\"dialog-skill\">";
					if(skillList!=null){
						for(var i=0;i<skillList.length;i++){
							var sk = skillList[i];
							var skType = sk.skillType;
							var skGrade = sk.skillGrade;
							var skEx = sk.experienceYear;
							content+="<li>"+skType+","+skGrade+","+skEx+"年</li>";
						}
					}
					var totalTask = busyOrNot.totalTask;
					content+="</ul></dd><dd style=\"display:none\" id='hdGantt'><span><h4 style='width:200px; margin:0 auto;'>任务甘特图<span style='font-weight:normal; color:blue;' >("+totalTask+"个任务)</span></h4></span>";
					
					content+="</dd><dd style=\"display:none\">";
					//人员储物箱信息
					var materialList = queryResult.materialList;
					//生成人员储物箱
					if(materialList){
						content+="<ul class='dialog-skill'>";
						for(var i=0;i<materialList.length;i++){
							var ma = materialList[i];;
							var materialType = ma.materialType;
							var materialName = ma.materialName;
							var materialCount = ma.materialCount;
							var materialStoreId = ma.materialStoreId;
							content+="<li>"+materialType+"："+materialName+"，<a hef='#' class='blue' >"+materialCount+"</a></li>";
						}
						content+="</ul>";
					}
					content+="</dd></dl>";
				    content+="</div></div></div><div class=\"dd_bottom\"><input type=\"button\" onclick=\"BindLiningDispatchTaskEvent('"+staffId+"','"+entityType+"')\" value=\"连线分配任务\" class=\"dialog-but\" /><a href=\"#\" class=\"dialog-locus\">轨迹回放</a></div>";
				    currentInfoWindow.bizValue_ = staffId;
					currentInfoWindow.setContent(content);
					currentInfoWindow.setPosition(position);
					//currentInfoWindow.open(map);
					selectedMarker.openInfoWindow(currentInfoWindow);
					//获取人员任务甘特图
					
					//table切换
					tableToggle();
					
					createGanttContent("hdGantt","staffWindowInfoGanttContent","swiDatepicker",staffId,"people",null);
				} 
			});
		}
	}else if(entityType == "_resource_car"){
		if(selectedMarkerArray!=null&&selectedMarkerArray.length==1){
			BindLiningDispatchTaskEvent(entityId,entityType);
			return false;
		}
		//获取车辆信息
		if(currentInfoWindow.bizValue_  == entityId){
			//currentInfoWindow.open(map);
			selectedMarker.openInfoWindow(currentInfoWindow);
		}else{
			queryUrl = "getTaskDetailInfoOfGraphElementAction";
			var param = {resourceTypeAndId:entityType+","+entityId,infoType:'resourceInfoWindow'};
			$.ajax({
				type : "post", 
			    url : queryUrl, 
			    data : param, 
			    async : true, 
			    success : function($data){ 
			    	var res = $data;
					var queryResult = eval("("+res+")");
					//{basicInfo:{key:'1',name:'粤A410Q8',carType:'轿车',driverName:'李茂群',driverPhone:'13533390355'}}
					var carNumber = queryResult.basicInfo.name;
					var carType = queryResult.basicInfo.carType;
					var driverName = queryResult.basicInfo.driverName;
					var driverPhone = queryResult.basicInfo.driverPhone;
					var carId = queryResult.basicInfo.key;
					if(carNumber == null || carNumber == 'null'){carNumber="";}
					if(carType == null || carType == 'null'){carType="未知";}
					if(driverName == null || driverName == 'null'){driverName="";}
					if(driverPhone == null || driverPhone == 'null'){driverPhone="";}
					
					//che.yd 添加车辆的查看信息链接
					
					//LineOff(离线) : 0 ,Travel (行驶中 ) : 1 ,Static (静止 ) : 2 ,Init (待初始化 ) : 3 
					var carState = marker.carState;
					var stat = carState;
					if(carState && carState=='0'){
				    	carState = "<em style='color:grey;'>离线</em>";
				    }else if(carState && carState=='1'){
				    	carState = "<em style='color:blue;'>行驶中</em>";
				    }else if(carState && carState=='2'){
				    	carState = "<em style='color:green;'>静止</em>";
				    }else{
				    	carState = "<em style='color:grey;'>待初始化</em>";
				    }
					var content = "<div class=\"dialog-title-bar clearfix\"><div class=\"dialog-bar-m\"><h3><a href=\"../cardispatch/cargeneral_index.jsp?carId="+carId+"&type=view\" target='_blank' >"+carNumber+"</a>，"+carType+"，"+carState+"<span class=\"dialog-ji selectEntity\">";
					if(isChecked){
						content+="<input type=\"checkbox\" checked=\"checked\"  onclick=\"selecteMarker('"+entityType+"','"+carId+"',this)\" />&nbsp;选中车辆</span></h3>";
					}else{
						content+="<input type=\"checkbox\"  onclick=\"selecteMarker('"+entityType+"','"+carId+"',this)\" />&nbsp;选中车辆</span></h3>";
					}
					content+="<h3>"+driverName+"，"+driverPhone+"</h3>";
					var taskList = queryResult.ownTaskOrderList;
					var taskCount = 0;
					if(taskList!=null){
						taskCount = taskList.length;
					}
					content+="</div></div>";
					//hardcode
					//var taskList = [{orderNum:'10086',takeCarAddress:'天天',useCarTime:'2012-4-20 14:58:40'},{orderNum:'100861',takeCarAddress:'天天3',useCarTime:'2012-4-20 14:58:40'},{orderNum:'1008611',takeCarAddress:'天天2',useCarTime:'2012-4-20 14:58:40'}];
					
					content+="<div class=\"dialog-content clearfix\"><div class=\"dialog-content-box\"><div class=\"dialog-content-tab\"><ul class=\"dialog-tabmenu clearfix\"><li class=\"on\">出车任务(<span style=\"color:red\">"+taskCount+"</span>个)</li><li style=\"border-right:1px solid #ccc;\">甘特图</li></ul><dl class=\"dialog-tabcontent\"><dd><div class=\"dialog-task\"><ul>";
					if(taskList!=null){
						for(var i=0;i<taskList.length;i++){
						   	var task = taskList[i];
						   	var orderNum = task.parentWoId;
						   	var takeCarAddress = task.takeCarAddress;
						   	var realTakeCarTime = task.realTakeCarTime;
						   	
						   	if(!realTakeCarTime||realTakeCarTime=='null'){realTakeCarTime="";}
						   	if(!takeCarAddress||takeCarAddress=='null'){takeCarAddress="";}
						   	
						   	var locationHref="../cardispatch/"+task.locationHref+"?WOID="+orderNum;
							content+="<li><a href='"+locationHref+"' target=\"_blank\">"+orderNum+"</a>,"+takeCarAddress+","+realTakeCarTime+"</li>";
						}
					}
					
					content+="</ul></div></dd><dd style=\"display:none\" id='cdGantt'><span><h4 style='width:200px; margin:0 auto;'>任务甘特图<span style='font-weight:normal; color:blue;' >("+taskCount+"个任务)</span></h4>";
					
					content+="</dd></dl></div></div>";
					//转码
					carNumber = encodeURI(encodeURI(carNumber));
					
					content+="</div><div class=\"dd_bottom\"><input type=\"button\" onclick=\"BindLiningDispatchTaskEvent('"+carId+"','"+entityType+"')\" value=\"分配出车任务\" id=\"assignSetOutTaskButton\" class=\"dialog-but\"/><a class=\"dialog-locus\" href=\"../cardispatch/loadCarStateMonitoringPageAction?carNumber="+carNumber+"\" target=\"_blank\">轨迹回放</a></div>";
					
					currentInfoWindow.bizValue_ = carId;
					currentInfoWindow.setContent(content);
					currentInfoWindow.setPosition(position);
					//currentInfoWindow.open(map);
					selectedMarker.openInfoWindow(currentInfoWindow);
					
					//table切换
					tableToggle();
					//甘特图
					createGanttContent("cdGantt","cwiGanttContent","cwiDatepicker",carId,"car",null);
			    } 
			});
		}
	}else if(entityType == "_resource_storehouse"){
		//获取仓库信息
		if(currentInfoWindow.bizValue_  == entityId){
			//currentInfoWindow.open(map);
			selectedMarker.openInfoWindow(currentInfoWindow);
		}else{
			queryUrl = "getTaskDetailInfoOfGraphElementAction";
			var param = {resourceTypeAndId:entityType+","+entityId,infoType:'resourceInfoWindow'};
			$.ajax({
				type : "post", 
			    url : queryUrl, 
			    data : param, 
			    async : true, 
			    success : function($data){ 
			    	var res = $data;
					var queryResult = eval("("+res+")");
					//{basicInfo:{hotspotId:'86',markName:'aaa',markType:'_resource_questionspot',remark:'aaaa'},
					//workList:[{id:'1',workName:'创建抢修工单',workLocation:'createWorkOrderOnPicUnitgAction',workFlag:'urgentrepair'}]}
					//生成信息弹框
					
					var key = queryResult.basicInfo.warehouseId;
					var warehouseName = queryResult.basicInfo.warehouseName;
					var warehousePhone = queryResult.basicInfo.warehousePhone;
					var warehouseAddress = queryResult.basicInfo.warehouseAddress;
					var remark = queryResult.basicInfo.remark;
					var warehouseType = "维护仓库";
					
					if(!warehouseName || warehouseName=='null'){warehouseName="";};
					if(!warehousePhone || warehousePhone=='null'){warehousePhone="";};
					if(!warehouseAddress || warehouseAddress=='null'){warehouseAddress="";};
					if(!remark||remark=='null'){remark="0";};
					
					var content = "<div class=\"dialog-title-bar clearfix\"><div class=\"dialog-bar-m clearfix\">";
					content+="<h3><b>仓库名称：</b><a href=\"#\" target=\"_blank\" class=\"blue\">"+warehouseName+"</a> &nbsp;</h3>";
					content+="<h3><label>仓库类型：</label><span class=\"blue\">"+warehouseType+"&nbsp;</span>&nbsp;<label>仓库电话：</label><span class=\"blue\">"+"&nbsp;</span></h3>";
					content+="<h3><label>仓库地址：</label><span>"+warehouseAddress+"&nbsp;</span></h3></div></div>";
					
					content+="<div class=\"dialog-content clearfix\"><div class=\"dialog-content-box\"><div class=\"dialog-content-tab\">";
					content+="<ul class=\"dialog-tabmenu clearfix\"><li class=\"on\">库存物资统计</li><li>当前出入库单</li><li style=\"border-right:1px solid #ccc;\">物资查询</li></ul><dl class=\"dialog-tabcontent\" class=\"clearfix\"><dd>";
					
					var materialList = queryResult.materialList;
					//生成库存物资信息
					if(materialList){
						content+="<ul class='dialog-skill' id='meUl'>";
						for(var i=0;i<materialList.length;i++){
							var ma = materialList[i];;
							var materialType = ma.materialType;
							var materialName = ma.materialName;
							var materialCount = ma.materialCount;
							var materialStoreId = ma.materialStoreId;
							content+="<li class='meLi'>"+materialType+"："+materialName+"，<a hef='#' class='blue' >"+materialCount+"</a></li>";
						}
						content+="</ul>";
						content+="<div class=\"dd_bottom\" style=\"position:absolute; bottom:5px;right:22px;\">&nbsp;<input type='button' class='page_ctr' value='<'/>&nbsp;<input type='text' style='width:30px;' value='1'/>&nbsp;页/共1页&nbsp;<input type='button' class='page_ctr' value='>'/></div>";
					}
					content+="</dd><dd style=\"display:none\"><ul class='dialog-skill' id='ucTask'>";
					//出入库单信息
					var outboundList = queryResult.outboundList;
					if(outboundList){
						for(var i=0;i<outboundList.length;i++){
							var ma = outboundList[i];;
							var formId = ma.formId;
							var outboundTheApplicant = ma.outboundTheApplicant;
							var status = ma.status;
							if(!formId){formId = "";}
							if(!outboundTheApplicant){outboundTheApplicant = "";}
							if(!status){status = "";}
							content+="<li class='ucLi'>出库单："+formId+"，"+outboundTheApplicant+"，"+status+"</li>";
						}
					}
					//出入库单信息
					var incomingList = queryResult.incomingList;
					if(incomingList){
						for(var i=0;i<incomingList.length;i++){
							var ma = incomingList[i];;
							var formId = ma.formId;
							var outboundTheApplicant = ma.outboundTheApplicant;
							var status = ma.status;
							if(!formId){formId = "";}
							if(!outboundTheApplicant){outboundTheApplicant = "";}
							if(!status){status = "";}
							content+="<li class='ucLi'>入库单："+formId+"，"+outboundTheApplicant+"，"+status+"</li>";
						}
					}
					content+="</ul><div class=\"dd_bottom\" style=\"position:absolute; bottom:5px;right:22px;\">&nbsp;<input type='button' value='<' class='page_ctr' />&nbsp;<input type='text' style='width:30px;' value='1'/>&nbsp;页/共1页&nbsp;<input type='button' value='>' class='page_ctr' /></div>";
					content+="</dd><dd style=\"display:none\">";
					content+="<ul class='dd_ul'><li><em>物资名：</em><input type='text' id='meName' /> </li>";
					content+="<li><em>物资形态：</em><input type='radio' name='meShape' class='vm' />工具资产&nbsp;<input type='radio' class='vm' name='meShape' />备件设备&nbsp;<input type='radio' class='vm' name='meShape' />易耗品</li>";
					content+="<li><em>品牌：</em><input type='text' style='width:60px;' id='meBrand' /> &nbsp;<em>型号：</em><input type='text' style='width:60px;' id='meModel' /></li>";
					content+="<li class='tc'><input type='button' value='查询'/> <a style='position:absolute;right:22px;' href='#'>高级查询</a></li></ul>";
					conten+="</dd></dl>";
				    content+="</div></div></div>";
					currentInfoWindow.bizValue_ = key;
					currentInfoWindow.setContent(content);
					currentInfoWindow.setPosition(position);
					//currentInfoWindow.open(map);
					selectedMarker.openInfoWindow(currentInfoWindow);
					//table切换
					tableToggle();
			    } 
			});		
		}
						
	}else if(entityType == "_resource_questionspot"){
		//获取问题点信息
		if(currentInfoWindow.bizValue_  == entityId){
			//currentInfoWindow.open(map);
			selectedMarker.openInfoWindow(currentInfoWindow);
		}else{
			queryUrl = "getTaskDetailInfoOfGraphElementAction";
			var param = {resourceTypeAndId:entityType+","+entityId,infoType:'resourceInfoWindow'};
			$.ajax({
				type : "post", 
			    url : queryUrl, 
			    data : param, 
			    async : true, 
			    success : function($data){ 
			    	var res = $data;
					var queryResult = eval("("+res+")");
					//{basicInfo:{hotspotId:'86',markName:'aaa',markType:'_resource_questionspot',remark:'aaaa'},
					//workList:[{id:'1',workName:'创建抢修工单',workLocation:'createWorkOrderOnPicUnitgAction',workFlag:'urgentrepair'}]}
					var markName = queryResult.basicInfo.markName;
					var markType = queryResult.basicInfo.markType;
					var remark = queryResult.basicInfo.remark;
					var address = queryResult.basicInfo.address;
					var key = queryResult.basicInfo.hotspotId;
					if(markName == null || markName == 'null'){markName="";}
					if(remark == null || remark == 'null'){remark="";}
						
					var content="<div class=\"question\"><ul><li><label>问题点名称：</label><span>"+markName+"</span></li><li><label>问题点地址：</label><span>"+address+"</span></li><li><label>备注：</label><span>"+remark+"</span></li></ul></div>"
					
					currentInfoWindow.bizValue_ = key;
					currentInfoWindow.setContent(content);
					currentInfoWindow.setPosition(position);
					//currentInfoWindow.open(map);
					selectedMarker.openInfoWindow(currentInfoWindow);
			    } 
			});		
		}
		
	}else if(entityType == "_resource_hiddentroublespot"){
		//获取隐患点信息
		if(currentInfoWindow.bizValue_  == entityId){
			//currentInfoWindow.open(map);
			selectedMarker.openInfoWindow(currentInfoWindow);
		}else{
			queryUrl = "getTaskDetailInfoOfGraphElementAction";
			var param = {resourceTypeAndId:entityType+","+entityId,infoType:'resourceInfoWindow'};
			$.ajax({
				type : "post", 
			    url : queryUrl, 
			    data : param, 
			    async : true, 
			    success : function($data){ 
			    	var res = $data;
					var queryResult = eval("("+res+")");
					//{basicInfo:{hotspotId:'86',markName:'aaa',markType:'_resource_questionspot',remark:'aaaa'},
					//workList:[{id:'1',workName:'创建抢修工单',workLocation:'createWorkOrderOnPicUnitgAction',workFlag:'urgentrepair'}]}
					var markName = queryResult.basicInfo.markName;
					var markType = queryResult.basicInfo.markType;
					var remark = queryResult.basicInfo.remark;
					var address = queryResult.basicInfo.address;
					var key = queryResult.basicInfo.hotspotId;
					if(markName == null || markName == 'null'){markName="";}
					if(remark == null || remark == 'null'){remark="";}
						
					var content="<div>隐患点名称："+markName+"<br/>隐患点地址:"+address+"<br/>备注："+remark+"<br/></div>"
					
					currentInfoWindow.bizValue_ = key;
					currentInfoWindow.setContent(content);
					currentInfoWindow.setPosition(position);
					//currentInfoWindow.open(map);
					selectedMarker.openInfoWindow(currentInfoWindow);
			    } 
			});		
		}
		
	}else if(entityType == "_resource_falutspot"){
		//获取故障点信息
		if(currentInfoWindow.bizValue_  == entityId){
			//currentInfoWindow.open(map);
			selectedMarker.openInfoWindow(currentInfoWindow);
		}else{
			queryUrl = "getTaskDetailInfoOfGraphElementAction";
			var param = {resourceTypeAndId:entityType+","+entityId,infoType:'resourceInfoWindow'};
			$.ajax({
				type : "post", 
			    url : queryUrl, 
			    data : param, 
			    async : true, 
			    success : function($data){ 
			    	var res = $data;
					var queryResult = eval("("+res+")");
					//{basicInfo:{hotspotId:'86',markName:'aaa',markType:'_resource_questionspot',remark:'aaaa'},
					//workList:[{id:'1',workName:'创建抢修工单',workLocation:'createWorkOrderOnPicUnitgAction',workFlag:'urgentrepair'}]}
					var markName = queryResult.basicInfo.markName;
					var markType = queryResult.basicInfo.markType;
					var remark = queryResult.basicInfo.remark;
					var address = queryResult.basicInfo.address;
					var key = queryResult.basicInfo.hotspotId;
					if(markName == null || markName == 'null'){markName="";}
					if(remark == null || remark == 'null'){remark="";}
						
					var content="<div>故障点名称："+markName+"<br/>故障点地址:"+address+"<br/>备注："+remark+"<br/></div>"
					
					currentInfoWindow.bizValue_ = key;
					currentInfoWindow.setContent(content);
					currentInfoWindow.setPosition(position);
					//currentInfoWindow.open(map);
					selectedMarker.openInfoWindow(currentInfoWindow);
			    } 
			});		
		}
		
	}else if(entityType == "_resource_watchspot"){
		//获取盯防点信息
		if(currentInfoWindow.bizValue_  == entityId){
			//currentInfoWindow.open(map);
			selectedMarker.openInfoWindow(currentInfoWindow);
		}else{
			queryUrl = "getTaskDetailInfoOfGraphElementAction";
			var param = {resourceTypeAndId:entityType+","+entityId,infoType:'resourceInfoWindow'};
			$.ajax({
				type : "post", 
			    url : queryUrl, 
			    data : param, 
			    async : true, 
			    success : function($data){ 
			    	var res = $data;
					var queryResult = eval("("+res+")");
					//{basicInfo:{hotspotId:'86',markName:'aaa',markType:'_resource_questionspot',remark:'aaaa'},
					//workList:[{id:'1',workName:'创建抢修工单',workLocation:'createWorkOrderOnPicUnitgAction',workFlag:'urgentrepair'}]}
					var markName = queryResult.basicInfo.markName;
					var markType = queryResult.basicInfo.markType;
					var remark = queryResult.basicInfo.remark;
					var address = queryResult.basicInfo.address;
					var key = queryResult.basicInfo.hotspotId;
					if(markName == null || markName == 'null'){markName="";}
					if(remark == null || remark == 'null'){remark="";}
						
					var content="<div>盯防点名称："+markName+"<br/>盯防点地址:"+address+"<br/>备注："+remark+"<br/></div>"
					
					currentInfoWindow.bizValue_ = key;
					currentInfoWindow.setContent(content);
					currentInfoWindow.setPosition(position);
					//currentInfoWindow.open(map);
					selectedMarker.openInfoWindow(currentInfoWindow);
			    }
			});		
		}
		
	}
	else if(entityType == "_resource_manitainareaaddress"){
		//获取维护片区信息
		queryUrl = "getTaskDetailInfoOfGraphElementAction";
		var param = {resourceTypeAndId:entityType+","+entityId,infoType:'resourceInfoWindow'};
		$.ajax({
			type : "post", 
		    url : queryUrl, 
		    data : param, 
		    async : true, 
		    success : function($data){ 
		    	var res = $data;
				var queryResult = eval("("+res+")");
				//生成信息弹框
				var orgName = queryResult.basicInfo.orgName;
				var address = queryResult.basicInfo.address;
				var contactPhone = queryResult.basicInfo.contactPhone;
				var dutyPerson = queryResult.basicInfo.dutyPerson;
				var key = queryResult.basicInfo.orgId;
				
				if(!orgName||orgName=='null'){orgName="";};
				if(!address||address=='null'){address="";};
				if(!contactPhone||contactPhone=='null'){contactPhone="";};
				if(!dutyPerson||dutyPerson=='null'){dutyPerson="0";};
				
				//che.yd------------begin-----
				var orgStaffCount= queryResult.basicInfo.orgStaffCount;
				if(!orgStaffCount||orgStaffCount=='null'){orgStaffCount="0";};
				//che.yd------------end-----
				
				var content = "<div class=\"architecture\"><ul><li><label>组织名称：</label><span><a href=\"../organization/organizationByOrgIdPage.jsp?orgId="+key+"\" target='_blank'>"+orgName+"</a>&nbsp;</span></li><li><label>办公地点：</label><span>"+address+"&nbsp;</span></li><li><label>联系电话：</label><span>"+contactPhone+"&nbsp;</span></li><li><label>现有人数：</label><span>"+orgStaffCount+"人<em class=\"dialog-ji selectEntity\">";
				if(isChecked){
					content+="<input type=\"checkbox\" checked=\"checked\" onclick=\"selecteMarker('"+entityType+"','"+key+"',this)\" />&nbsp;选中对象</em></span></div>";
				}else{
					content+="<input type=\"checkbox\"  onclick=\"selecteMarker('"+entityType+"','"+key+"',this)\" />&nbsp;选中对象</em></span></div>";
				}
				currentInfoWindow.bizValue_ = key;
				currentInfoWindow.setContent(content);
				currentInfoWindow.setPosition(position);
				//currentInfoWindow.open(map);
				selectedMarker.openInfoWindow(currentInfoWindow);
		    } 
		});
	}else if(entityType == "_resource_maintaingroup"){		//获取维护组信息
		
		queryUrl = "getTaskDetailInfoOfGraphElementAction";
		var param = {resourceTypeAndId:entityType+","+entityId,infoType:'resourceInfoWindow'};
		$.ajax({
			type : "post", 
		    url : queryUrl, 
		    data : param, 
		    async : true, 
		    success : function($data){ 
		    	var res = $data;
				var queryResult = eval("("+res+")");
				//生成信息弹框
				var orgName = queryResult.basicInfo.orgName;
				var address = queryResult.basicInfo.address;
				var contactPhone = queryResult.basicInfo.contactPhone;
				var dutyPerson = queryResult.basicInfo.dutyPerson;
				var key = queryResult.basicInfo.orgId;
				
				if(!orgName||orgName=='null'){orgName="";};
				if(!address||address=='null'){address="";};
				if(!contactPhone||contactPhone=='null'){contactPhone="";};
				if(!dutyPerson||dutyPerson=='null'){dutyPerson="0";};
				
				var content = "<div class=\"architecture\"><ul><li><label>组织名称：</label><a href=\"../organization/showProviderOrgByOrgIdAction?orgId="+key+"\" target='_blank'>"+orgName+"</a>&nbsp;</span></li><li><label>办公地点：</label><span>"+address+"&nbsp;</span></li><li><label>联系电话：</label><span>"+contactPhone+"&nbsp;</span></li><li><label>现有人数：</label><span>"+dutyPerson+"人<em class=\"dialog-ji selectEntity\">";
				if(isChecked){
					content+="<input type=\"checkbox\" checked=\"checked\" onclick=\"selecteMarker('"+entityType+"','"+key+"',this)\" />&nbsp;选中对象</em></span></div>";
				}else{
					content+="<input type=\"checkbox\"  onclick=\"selecteMarker('"+entityType+"','"+key+"',this)\" />&nbsp;选中对象</em></span></div>";
				}
				currentInfoWindow.bizValue_ = key;
				currentInfoWindow.setContent(content);
				currentInfoWindow.setPosition(position);
				//currentInfoWindow.open(map);
				selectedMarker.openInfoWindow(currentInfoWindow);
		    }
		});
	}else if(entityType == "_resource_shiyebuaddress"){		//获取事业部信息
		
		queryUrl = "getTaskDetailInfoOfGraphElementAction";
		var param = {resourceTypeAndId:entityType+","+entityId,infoType:'resourceInfoWindow'};
		$.ajax({
			type : "post", 
		    url : queryUrl, 
		    data : param, 
		    async : true, 
		    success : function($data){ 
		    	var res = $data;
				var queryResult = eval("("+res+")");
				//生成信息弹框
				var orgName = queryResult.basicInfo.orgName;
				var address = queryResult.basicInfo.address;
				var contactPhone = queryResult.basicInfo.contactPhone;
				var dutyPerson = queryResult.basicInfo.dutyPerson;
				var key = queryResult.basicInfo.orgId;
				
				if(!orgName||orgName=='null'){orgName="";};
				if(!address||address=='null'){address="";};
				if(!contactPhone||contactPhone=='null'){contactPhone="";};
				if(!dutyPerson||dutyPerson=='null'){dutyPerson="0";};
				
				//che.yd------------begin-----
				var orgStaffCount= queryResult.basicInfo.orgStaffCount;
				if(!orgStaffCount||orgStaffCount=='null'){orgStaffCount="0";};
				//che.yd------------end-----
				
				//che.yd 添加查看组织信息链接
				//humanresource/getOneOrgAction?orgId=28
				
				var content = "<div class=\"architecture\"><ul><li><label>组织名称：</label><span><a href=\"../organization/showProviderOrgByOrgIdAction?orgId="+key+"\" target='_blank'>"+orgName+"</a>&nbsp;</span></li><li><label>办公地点：</label><span>"+address+"&nbsp;</span></li><li><label>联系电话：</label><span>"+contactPhone+"&nbsp;</span></li><li><label>现有人数：</label><span>"+orgStaffCount+"人<em class=\"dialog-ji selectEntity\">";
				if(isChecked){
					content+="<input type=\"checkbox\" checked=\"checked\"  onclick=\"selecteMarker('"+entityType+"','"+key+"',this)\" />&nbsp;选中对象</em></span></div>";
				}else{
					content+="<input type=\"checkbox\"  onclick=\"selecteMarker('"+entityType+"','"+key+"',this)\" />&nbsp;选中对象</em></span></div>";
				}
				//var content = "<div>组织名称："+orgName+"<br/>办公地点："+address+"<br/>联系电话："+contactPhone+"<br/>现有人数："+dutyPerson+"人&nbsp;&nbsp;&nbsp;&nbsp;<input type=\"checkbox\"  onclick=\"selecteMarker('"+entityType+"','"+key+"')\" />&nbsp;选中对象</div>";
				currentInfoWindow.bizValue_ = key;
				currentInfoWindow.setContent(content);
				currentInfoWindow.setPosition(position);
				//currentInfoWindow.open(map);
				selectedMarker.openInfoWindow(currentInfoWindow);
		    }
		});
	}else if(entityType == "_resource_headerquarteraddress"){	//公司驻地
		queryUrl = "getTaskDetailInfoOfGraphElementAction";
		var param = {resourceTypeAndId:entityType+","+entityId,infoType:'resourceInfoWindow'};
		$.ajax({
			type : "post", 
		    url : queryUrl, 
		    data : param, 
		    async : true, 
		    success : function($data){ 
		    	var res = $data;
				var queryResult = eval("("+res+")");
				//生成信息弹框
				var orgName = queryResult.basicInfo.orgName;
				var address = queryResult.basicInfo.address;
				var contactPhone = queryResult.basicInfo.contactPhone;
				var dutyPerson = queryResult.basicInfo.dutyPerson;
				var key = queryResult.basicInfo.orgId;
				
				if(!orgName||orgName=='null'){orgName="";};
				if(!address||address=='null'){address="";};
				if(!contactPhone||contactPhone=='null'){contactPhone="";};
				if(!dutyPerson||dutyPerson=='null'){dutyPerson="0";};
				
				//che.yd------------begin-----
				var orgStaffCount= queryResult.basicInfo.orgStaffCount;
				if(!orgStaffCount||orgStaffCount=='null'){orgStaffCount="0";};
				//che.yd------------end-----
				
				//che.yd 添加查看组织信息链接
				//humanresource/getOneOrgAction?orgId=28
				
				var content = "<div class=\"architecture\"><ul><li><label>组织名称：</label><span><a href=\"../organization/showProviderOrgByOrgIdAction?orgId="+key+"\" target='_blank'>"+orgName+"</a>&nbsp;</span></li><li><label>办公地点：</label><span>"+address+"&nbsp;</span></li><li><label>联系电话：</label><span>"+contactPhone+"&nbsp;</span></li><li><label>现有人数：</label><span>"+orgStaffCount+"人<em class=\"dialog-ji selectEntity\">";
				if(isChecked){
					content+="<input type=\"checkbox\" checked=\"checked\" onclick=\"selecteMarker('"+entityType+"','"+key+"',this)\" />&nbsp;选中对象</em></span></div>";
				}else{
					content+="<input type=\"checkbox\"  onclick=\"selecteMarker('"+entityType+"','"+key+"',this)\" />&nbsp;选中对象</em></span></div>";
				}
				//var content = "<div>组织名称："+orgName+"<br/>办公地点："+address+"<br/>联系电话："+contactPhone+"<br/>现有人数："+dutyPerson+"人&nbsp;&nbsp;&nbsp;&nbsp;<input type=\"checkbox\"  onclick=\"selecteMarker('"+entityType+"','"+key+"')\" />&nbsp;选中对象</div>";
				currentInfoWindow.bizValue_ = key;
				currentInfoWindow.setContent(content);
				currentInfoWindow.setPosition(position);
				//currentInfoWindow.open(map);
				selectedMarker.openInfoWindow(currentInfoWindow);
		    }
		});
	}else if(entityType == "_resource_projectgroupaddress"){	//项目组
		
		queryUrl = "getTaskDetailInfoOfGraphElementAction";
		var param = {resourceTypeAndId:entityType+","+entityId,infoType:'resourceInfoWindow'};
		$.ajax({
			type : "post", 
		    url : queryUrl, 
		    data : param, 
		    async : true, 
		    success : function($data){ 
		    	var res = $data;
				var queryResult = eval("("+res+")");
				//生成信息弹框
				var orgName = queryResult.basicInfo.orgName;
				var address = queryResult.basicInfo.address;
				var contactPhone = queryResult.basicInfo.contactPhone;
				var dutyPerson = queryResult.basicInfo.dutyPerson;
				var key = queryResult.basicInfo.orgId;
				
				if(!orgName||orgName=='null'){orgName="";};
				if(!address||address=='null'){address="";};
				if(!contactPhone||contactPhone=='null'){contactPhone="";};
				if(!dutyPerson||dutyPerson=='null'){dutyPerson="0";};
				
				//che.yd------------begin-----
				var orgStaffCount= queryResult.basicInfo.orgStaffCount;
				if(!orgStaffCount||orgStaffCount=='null'){orgStaffCount="0";};
				//che.yd------------end-----
				//che.yd 添加查看组织信息链接
				//humanresource/getOneOrgAction?orgId=28
				var content = "<div class=\"architecture\"><ul><li><label>组织名称：</label><span><a href=\"../organization/showProviderOrgByOrgIdAction?orgId="+key+"\" target='_blank'>"+orgName+"</a>&nbsp;</span></li><li><label>办公地点：</label><span>"+address+"&nbsp;</span></li><li><label>联系电话：</label><span>"+contactPhone+"&nbsp;</span></li><li><label>现有人数：</label><span>"+orgStaffCount+"人<em class=\"dialog-ji selectEntity\">";
				if(isChecked){
					content+="<input type=\"checkbox\" checked=\"checked\" onclick=\"selecteMarker('"+entityType+"','"+key+"',this)\" />&nbsp;选中对象</em></span></div>";
				}else{
					content+="<input type=\"checkbox\"  onclick=\"selecteMarker('"+entityType+"','"+key+"',this)\" />&nbsp;选中对象</em></span></div>";
				}
				//var content = "<div>组织名称："+orgName+"<br/>办公地点："+address+"<br/>联系电话："+contactPhone+"<br/>现有人数："+dutyPerson+"人&nbsp;&nbsp;&nbsp;&nbsp;<input type=\"checkbox\"  onclick=\"selecteMarker('"+entityType+"','"+key+"')\" />&nbsp;选中对象</div>";
				currentInfoWindow.bizValue_ = key;
				currentInfoWindow.setContent(content);
				currentInfoWindow.setPosition(position);
				//currentInfoWindow.open(map);
				selectedMarker.openInfoWindow(currentInfoWindow);
		    }
		});
	}
};
/*显示更多菜单(创建工单)*/
function showMoreMenu(be){
	var address = $(be).attr("address");
	var bsid = $(be).attr("bsid");
	var baseStationType = $(be).attr("bst");
	var workList = selectedMarker.workList;
	if(workList){
   		var content="<div id='moreMenuDiv' style='display:none;'>";
    	for(var i=0;i<workList.length;i++){
    		var wk = workList[i];
    		var wkLocation = wk.workLocation;
    		var wkName = wk.workName;
    		var wkFlag = wk.workFlag;
    		content+="<a href=\"#\" onclick=\"createWorkOrderEvent('"+wkFlag+"','"+bsid+"','"+wkLocation+"','"+baseStationType+"')\">"+wkName+"</a><br/>";
    	}
    	content+="</div>";
    }
    content = $(content);
    content.appendTo(be);
	var offset = $(be).offset();
	var top = offset.top - 155;
	var left = $(be).css("left");
	
	$("#moreMenuDiv").show();
	$("#moreMenuDiv").hover(function(){},function(){
		$("#moreMenuDiv").hide();
	});
}
/**
 * 创建各类工单
 */
function createWorkOrderEvent(type,stationId,workLocation,baseStationType){
	if(!selectedMarker){return false;}
	stationId = stationId.substring(stationId.lastIndexOf("_")+1);
	window.open(workLocation+"?workOrderType="+type+"&baseStationId="+stationId+"&baseStationType="+baseStationType);
}

/**
 * 按条件搜索网络设施的周边信息
 */
function searchByConditionEvent(){
	//清除搜索结果
	cleanSearchResultEvent();
	
	var searchType = "";
	$(".productionCheckBox").each(function(index){
		if($(this).attr("checked") == "checked"){
			var type = $(this).val();
			searchType+=type+",";
		}
	});
	$(".netWorkCheckBox").each(function(index){
		if($(this).attr("checked") == "checked"){
			var type = $(this).val();
			searchType+=type+",";
		}
	});
	if(searchType!=""){
		searchType = searchType.substring(0,searchType.length-1);
	}
	var distance = $("#distanceText").val();
	var latlng = selectedMarker.getPosition();
	var searchName = $("#queryNameText").val();
	if(searchName == "输入资源名称检索"){
		searchName = "";	
	}
	$("#tabmenu li").removeClass("active");
	$("#searchResultLi").css("display","block");
	$("#searchResultLi").attr("class","active");
	$("#searchResultDiv").css("display","block");
	$("#monitorResourceDiv").css("display","none");
	$("#gis_content_right_data").css("display","none");
	var param={searchType:searchType,searchName:searchName,distance:distance,'geLatLng.latitude':latlng.getLatitude(),'geLatLng.longitude':latlng.getLongitude()};
	var queryUrl = "getResourceByConditionsAction";
	$.ajax({
		type : "post", 
		url : queryUrl, 
		data : param, 
		async : true, 
		success : function($data){ 
		   	var res = $data;
			var queryResult = eval("("+res+")");
			var staffList = queryResult._resource_human;//人员
			var carList = queryResult._resource_car;	//车辆
			var stationList = queryResult._resource_station;	//站址
			//循环判断获取回来的列表是否为空
			var hul = $("#searchStaffListUl");
			hul.html("");
			//生成人员列表
			if(staffList && staffList.length>0){
				hul.prev().html("共有 "+staffList.length+" 个结果");
				for(var i=0;i<staffList.length;i++){
					var be = staffList[i];
					var marker = null;
					var isStaffExit = false;
					var index = 0;
					for(var j=0;j<allMarkers.length;j++){
						marker = allMarkers[j];
						if(be.key==marker.entityId){
							isStaffExit = true;
							index = j;
							break;
						}
					}
					if(isStaffExit){
						//更改图标
						var options = {};
						options.icon = "image/staff5.png";
						marker.setOptions(options);
						marker.searchIcon = "image/staff5.png";
						//保存数据
						allMarkers.splice(index,1);
						allMarkers.push(marker);
						searchResultMarkers.push(marker);
					}else{
						//图元不存在，生成图元
						createSingelMarker(be);
					}
					//生成左边搜索结果
					var name = be.name;
					var staffId = be.key;
					var sex = be.sex;
					var phone = be.phone;
					var count = 0;
					
					var tempStaffId = staffId.substring(staffId.lastIndexOf("_")+1);
					if(name == undefined || name == null || name=='null'){name="";}
					if(sex == undefined || sex == null || sex=='null'){sex=" ";}
					if(phone == undefined || phone == null || phone=='null'){phone=" ";}
					if(be.taskInfo){
						count = be.taskInfo.totalTaskCount;
					}
					
					var li = "<li onclick=\"openResourceInfoWindow('"+staffId+"','"+be.type+"')\" class='resource_item clearfix shLi'><div class='r'>";
					li += "<p class='name'><a href='../staffduty/showStaffInfo.jsp?staffId="+tempStaffId+"' target='_blank'>"+name+"</a>，"+sex+"，"+phone+"</p>";
					li += "<p class='name mt5'>当前<i class='red f-bold'>"+count+"个</i>任务</p>";
					hul.append($(li));
				}
			}else{
				hul.siblings().find($(".no_result hidden")).show();
				hul.prev().html("共有 0 个结果");
			}
			var cul = $("#searchCarListUl");
			cul.html("");
			//生成搜索结果车辆列表
			if(carList && carList.length>0){
				cul.prev().html("共有 "+carList.length+" 个结果");
				for(var i=0;i<carList.length;i++){
					var be = carList[i];
					var marker = null;
					var isCarExit = false;
					var index = 0;
					for(var j=0;j<allMarkers.length;j++){
						var marker = allMarkers[j];
						if(be.key==marker.entityId){
							isCarExit = true;
							index = j;
							break;
						}
					}
					if(isCarExit){
						//更改图标
						var options = {};
						options.icon = "image/staff5.png";
						marker.setOptions(options);
						marker.searchIcon = "image/staff5.png";
						//保存数据
						allMarkers.splice(index,1);
						allMarkers.push(marker);
						searchResultMarkers.push(marker);
					}else{
						//图元不存在，生成图元
						createSingelMarker(be);
					}
					//生成左边搜索结果
					var name = be.name;
					var carId = be.key;
					var driverName = be.driverName;
					var driverPhone = be.driverPhone;
					var carState = be.isOnDuty;
					var carType = be.carType;
					
					var tempCarId = carId.substring(carId.lastIndexOf("_")+1);
					if(name == undefined || name == null || name=='null'){name="";}
					if(driverName == undefined || driverName == null || driverName=='null'){driverName=" ";}
					if(driverPhone == undefined || driverPhone == null || driverPhone=='null'){driverPhone=" ";}
					if(carType == undefined || carType == null || carType=='null'){carType=" ";}
					
					if(carState && carState=='0'){
				    	carState = "<em style='color:grey;'>离线</em>";
				    }else if(carState && carState=='1'){
				    	carState = "<em style='color:blue;'>行驶中</em>";
				    }else if(carState && carState=='2'){
				    	carState = "<em style='color:green;'>静止</em>";
				    }else{
				    	carState = "<em style='color:grey;'>待初始化</em>";
				    }
				    var count = 0;
				    if(be.taskInfo){
						count = be.taskInfo.totalTaskCount;
					}
					var li = "<li onclick=\"openResourceInfoWindow('"+carId+"','"+be.type+"')\" class='resource_item clearfix scLi'><div class='r'>";
					li += "<p class='name'><a href='../cardispatch/cargeneral_index.jsp?carId="+tempCarId+"&type=view' target='_blank'>"+name+"</a>，"+carType+"，"+carState+"</p>";
					li += "<p class='name mt5'>"+driverName+"("+driverPhone+")<em class='name_red'><i class='red f-bold'>"+count+"个</i>任务</em></p></div></li>";
					cul.append($(li));
				}
			}else{
				cul.siblings().find($(".no_result hidden")).show();
				cul.prev().html("共有 0 个结果");
			}
			var sul = $("#searchStationListUl");
			sul.html("");
			//生成站址列表
			if(stationList && stationList.length>0){
				sul.prev().html("共有 "+stationList.length+" 个结果");
				var stationUrl = "../../resource/physicalres/getPhysicalresForOperaAction?currentEntityType=Station&modelType=view&showType=showTask&currentEntityId=";
				for(var i=0;i<stationList.length;i++){
					var be = stationList[i];
					var marker = null;
					var isStationExit = false;
					var index = 0;
					for(var j=0;j<allMarkers.length;j++){
						marker = allMarkers[j];
						if(be.key==marker.entityId){
							isStationExit = true;
							index = j;
							break;
						}
					}
					if(isStationExit){
						//更改图标
						var options = {};
						options.icon = "image/staff5.png";
						marker.setOptions(options);
						marker.searchIcon = "image/staff5.png";
						//保存数据
						allMarkers.splice(index,1);
						allMarkers.push(marker);
						searchResultMarkers.push(marker);
					}else{
						//图元不存在，生成图元
						createSingelMarker(be);
					}
					//生成左边搜索结果
					var name = be.name;
					var stationId = be.key;
					var address = be.address;
					var count = 0;
					
					var tempStationId = stationId.substring(stationId.lastIndexOf("_")+1);
					if(name == undefined || name == null || name=='null'){name="";}
					if(address == undefined || address == null || address=='null'){address=" ";}
					if(be.taskInfo){
						count = be.taskInfo.totalTaskCount;
					}
					tempStationId = stationUrl+tempStationId;
					var li = "<li onclick=\"openResourceInfoWindow('"+stationId+"','"+be.type+"')\" class='resource_item clearfix ssLi'><div class='r'>";
					li += "<p class='name'><a href='"+tempStationId+"' target='_blank'>"+name+"</a></p>";
					li += "<p class='name mt5'>抢修任务<i class='red f-bold'>"+count+"个</i>，巡检任务<i class='red f-bold'>0个</i></p>";
					li += "<p class='name'>地址："+address+"</p></div></li>";
					sul.append($(li));
				}
			}else{
				sul.siblings().find($(".no_result hidden")).show();
				sul.prev().html("共有 0 个结果");
			}
			//分页
			pagingColumnByForeground("ssPageContent",$(".ssLi"),6);
			pagingColumnByForeground("scPageContent",$(".scLi"),6);
			pagingColumnByForeground("shPageContent",$(".shLi"),6);
			//显示右边信息
			/*$("#cleanSearchResultButton").css("display","block");
			$("#gis_content_right_main").css("display","block");
			$("#searchResultLi").css("display","block");
			$("#searchResultLi").attr("class","active");
			$("#searchResultDiv").css("display","block");
			$("#gis_content_right_data").css("display","none");
			$("#assignResultLi").removeAttr("class");*/
		} 
	});
}

/*打开资源信息弹框*/
function openResourceInfoWindow(resourceId,resourceType){
//	console.log("openResourceInfoWindow:"+resourceId+"========"+resourceType);
	if(!resourceId || !resourceType){return false;}
	var isResourceExit = false;
	for(var i=0;i<allMarkers.length;i++){
		var marker = allMarkers[i];
//		console.log(marker.entityId+"====="+ resourceId);
		if(marker.entityId == resourceId){
			isResourceExit = true;
			showMarkerDetail(marker);
			break;
		}
	}
	//资源不存在，向数据库请求数据，生成资源
	if(!isResourceExit){
		var picUnitTypeCode = resourceType;
		resourceId = resourceId.substring(resourceId.lastIndexOf("_")+1);
		$.ajax({
			type : "post", 
			url : "getSpecifiedResourceInfoAction",
			data : {picUnitTypeCode:picUnitTypeCode,resourceId:resourceId},
			async : true, 
			success : function($data){
				var res = $data;
				var result = eval("("+res+")");
				var marker = createMarker(result);
				showMarkerDetail(marker);
			}
		});
	}
}
/**
 * 清除搜索结果
 */
function cleanSearchResultEvent(){
	for(var i=0;i<searchResultMarkers.length;i++){
		var sr = searchResultMarkers[i];
		for(var j=0;j<allMarkers.length;j++){
			var marker = allMarkers[j];
			if(marker.entityId == sr.entityId){
				var options = {};
				options.icon = marker.basicIcon;
				//车辆状态图标
				if(marker.stateIcon){
					options.icon = marker.stateIcon;
				}
				//判断是否连线
				if(marker.assignIcon){
					options.icon = marker.assignIcon;
				}
				//判断是否已经选中
				if(marker.checkedIcon){
					options.icon = marker.checkedIcon;
				}
				marker.setOptions(options);
				marker.searchIcon = undefined;
				break;
			}
		}
	}
	searchResultMarkers = new Array();
	//$("#searchResultLi").css("display","none");
	//TODO待
	/*
	$("#tabmenu li").removeClass("active");
	$("#monitorLi").addClass("active");
	$("#monitorResourceDiv").css("display","block");
	$("#searchResultDiv").css("display","none");
	$("#gis_content_right_data").css("display","none");
	*/
}
/**********预留 选择模式 中打开选择对象******************/
var choosedMarkerArray = new Array();		//已选Marker对象数组(预留 选择模式)
/**
 * 选中Marker对象
 */
function selecteMarker(type,key,obj){
	var selectStatu = $(obj).attr("checked");
	//判断类型，匹配不同的选中图标
	if(type == "_resource_station"){
		if(selectStatu){
			selectedMarker.type = key;
			//选中当前对象
			//选择该对象
			var options = {};
			options.icon = "image/u89_original2.png";
			selectedMarker.setOptions(options);
			choosedMarkerArray.push(selectedMarker);	//存放到已选Marker对象数组
			selectedMarker.checkedIcon = "image/u89_original2.png";
			return false;
		}else{
			//若值不为空，则判断是否为当前对象，是则取消选中，否则则提示操作出错
			selectedMarker.type = null;
			//显示不选中该对象的图标
			var options = {};
			options.icon = "image/u89_original.png";
			selectedMarker.setOptions(options);
			//从已选数组中删除
			cancelSelected(selectedMarker);
		}
	}else if(type=="_resource_human"){
		if(selectStatu){
			selectedMarker.type = key;
			//控制选中图标
			/*if(selectedMarker.searchIcon == undefined || selectedMarker.searchIcon == null){
				var options = {};
				options.icon = "image/staff2.png";
				selectedMarker.setOptions(options);
			}else{
				//显示水滴
				var options = {};
				options.icon = "image/staff0.png";
				selectedMarker.setOptions(options);
			}*/
			var options = {};
			options.icon = "image/staff2.png";
			selectedMarker.setOptions(options);
			choosedMarkerArray.push(selectedMarker);	//存放到已选Marker对象数组
			selectedMarker.checkedIcon = "image/staff2.png";
			return false;
		}else{
			//若值不为空，则判断是否为当前对象，是则取消选中，否则则提示操作出错
			if(selectedMarker.type == key){
				//取消选中当前对象
				selectedMarker.type = null;
				var options = {};
//				//控制选中图标
				if(selectedMarker.searchIcon == undefined || selectedMarker.searchIcon == null){
					options.icon = "image/staff1.png";
				}else{
					//水滴
					options.icon = "image/staff5.png";
				}
				selectedMarker.setOptions(options);
				//从已选数组中删除
				cancelSelected(selectedMarker);
				selectedMarker.checkedIcon = undefined;
			}else{
				alert("操作错误！");
				return false;
			}
		}
	}else if(type == "_resource_car"){
		if(selectStatu){
			selectedMarker.type = key;
			var carState = selectedMarker.carState;
			var carIcon = "image/car-LineOff2.png";
			if(carState){
				if(carState && carState=='1'){
			    	carIcon = "image/car-red.png";
			    }else if(carState && carState=='2'){
			    	carIcon = "image/car-Static2.png";
			    }
			}else{
				carIcon = "image/car-red.png";
			}
			var options = {};
			options.icon = carIcon;
			selectedMarker.setOptions(options);
			choosedMarkerArray.push(selectedMarker);	//存放到已选Marker对象数组
			selectedMarker.checkedIcon = carIcon;
			return false;
		}else{
			//若值不为空，则判断是否为当前对象，是则取消选中，否则则提示操作出错 
			if(selectedMarker.type == key){
//				alert("同一个对象，取消选中");
				selectedMarker.type = null;
				// 判断是否被搜索，取消选中不同对象效果
				var options = {};
				if(selectedMarker.searchIcon){
					//该车辆被搜索，显示水滴
					options.icon = selectedMarker.searchIcon;
				}else{
					var carState = selectedMarker.carState;
					if(carState){
						var carIcon = "image/car-LineOff.png";
						if(carState && carState=='1'){
					    	carIcon = "image/car.png";
					    }else if(carState && carState=='2'){
					    	carIcon = "image/car-Static.png";
					    }
						options.icon = carIcon;
					}else{
						options.icon = selectedMarker.basicIcon;
					}
				}
				selectedMarker.setOptions(options);
				//从已选数组中删除
				cancelSelected(selectedMarker);
				selectedMarker.checkedIcon = undefined;
			}else{
				alert("操作错误！");
				return false;
			}
		}
	}else if(type == "_resource_manitainareaaddress"){
		//片区、维护区域
		if(selectStatu){
			selectedMarker.type = key;
			//选中当前对象
			//选择该对象
			var options = {};
			options.icon = "image/pianqu2.png";
			selectedMarker.setOptions(options);
			choosedMarkerArray.push(selectedMarker);	//存放到已选Marker对象数组
			selectedMarker.checkedIcon = "image/pianqu2.png";
			return false;
		}else{
			//若值不为空，则判断是否为当前对象，是则取消选中，否则则提示操作出错
			//取消选中当前对象
			selectedMarker.type = null;
			//显示不选中该对象的图标
			var options = {};
			options.icon = selectedMarker.basicIcon;
			selectedMarker.setOptions(options);
			//从已选数组中删除
			cancelSelected(selectedMarker);
			selectedMarker.checkedIcon = undefined;
		}
	}else if(type == "_resource_projectgroupaddress"){
		//项目组
		if(selectStatu){
			selectedMarker.type = key;
			//选中当前对象
			//选择该对象
			var options = {};
			options.icon = "image/xiangmuzu2.png";
			selectedMarker.setOptions(options);
			choosedMarkerArray.push(selectedMarker);	//存放到已选Marker对象数组
			selectedMarker.checkedIcon = "image/xiangmuzu2.png";
			return false;
		}else{
			//若值不为空，则判断是否为当前对象，是则取消选中，否则则提示操作出错
			if(selectedMarker.type == key){
				//取消选中当前对象
				selectedMarker.type = null;
				//显示不选中该对象的图标
				var options = {};
				options.icon = selectedMarker.basicIcon;
				selectedMarker.setOptions(options);
				//从已选数组中删除
				cancelSelected(selectedMarker);
				selectedMarker.checkedIcon = undefined;
			}else{
				alert("操作错误！");
				return false;
			}
		}
	}else if(type == "_resource_headerquarteraddress"){
		//总部
		if(selectStatu){
			selectedMarker.type = key;
			//选中当前对象
			//选择该对象
			var options = {};
			options.icon = "image/zongbu2.png";
			selectedMarker.setOptions(options);
			choosedMarkerArray.push(selectedMarker);	//存放到已选Marker对象数组
			selectedMarker.checkedIcon = "image/zongbu2.png";
			return false;
		}else{
			//若值不为空，则判断是否为当前对象，是则取消选中，否则则提示操作出错
			if(selectedMarker.type == key){
				//取消选中当前对象
				selectedMarker.type = null;
				//显示不选中该对象的图标
				var options = {};
				options.icon = selectedMarker.basicIcon;
				selectedMarker.setOptions(options);
				//从已选数组中删除
				cancelSelected(selectedMarker);
				selectedMarker.checkedIcon = undefined;
			}else{
				alert("操作错误！");
				return false;
			}
		}
	}else if(type == "_resource_shiyebuaddress"){
		//事业部
		if(selectStatu){
			selectedMarker.type = key;
			//选中当前对象
			//选择该对象
			var options = {};
			options.icon = "image/shiyebu2.png";
			selectedMarker.setOptions(options);
			choosedMarkerArray.push(selectedMarker);	//存放到已选Marker对象数组
			selectedMarker.checkedIcon = "image/shiyebu2.png";
			return false;
		}else{
			//若值不为空，则判断是否为当前对象，是则取消选中，否则则提示操作出错
			if(selectedMarker.type == key){
				//取消选中当前对象
				selectedMarker.type = null;
				//显示不选中该对象的图标
				var options = {};
				options.icon = selectedMarker.basicIcon;
				selectedMarker.setOptions(options);
				//从已选数组中删除
				cancelSelected(selectedMarker);
				selectedMarker.checkedIcon = undefined;
			}else{
				alert("操作错误！");
				return false;
			}
		}
	}else if(type == "_resource_maintaingroup"){
		//维护组
		if(selectStatu){
			selectedMarker.type = key;
			//选中当前对象
			//选择该对象
			var options = {};
			options.icon = "image/weihuzu2.png";
			selectedMarker.setOptions(options);
			choosedMarkerArray.push(selectedMarker);	//存放到已选Marker对象数组
			selectedMarker.checkedIcon = "image/weihuzu2.png";
			return false;
		}else{
			//若值不为空，则判断是否为当前对象，是则取消选中，否则则提示操作出错
			if(selectedMarker.type == key){
				//取消选中当前对象
				selectedMarker.type = null;
				//显示不选中该对象的图标
				var options = {};
				options.icon = selectedMarker.basicIcon;
				selectedMarker.setOptions(options);
				//从已选数组中删除
				cancelSelected(selectedMarker);
				selectedMarker.checkedIcon = undefined;
			}else{
				alert("操作错误！");
				return false;
			}
		}
	}
}
/**
 * 将Marker从选中的Marker列表中删除
 */
function cancelSelected(marker){
	for(var j=0;j<choosedMarkerArray.length;j++){
		var st = choosedMarkerArray[j];
		if(st.entityId  == marker.entityId){
			choosedMarkerArray.splice(j,1);
		};
	}
}
/************************************* 鼠标连线变量 ****************************************************************/
var selectedMarkerArray = null;	//已经选中的Marker对象数组（鼠标连线）
var selectedMarker = null;			//已经选中的Marker对象
var mousePath = null;			//折线数组
var mousePolyline = null;		//鼠标折线
var selectedOrderNum = null;		//已选任务单/工单
var mouseListener=null;
/*
 * 鼠标点击连线事件
 */
function BindLiningDispatchTaskEvent(entityId,entityType){
	if(!selectedMarker){
		return false;
	}
	//判断是否已经连线
	if(selectedMarkerArray!=null && selectedMarkerArray.length==2){
		cleanAssignResultEvent();
	}
	var isTheSameMarker = false;
	if(selectedMarkerArray==null){
		//初始化 已选数组
		selectedMarkerArray = new Array();
	}
	if(selectedMarkerArray != null){
		//循环遍历
		for(var i=0;i<selectedMarkerArray.length;i++){
			var mp = selectedMarkerArray[i];
			//判断是否为同一对象点击
			if(mp.entityId == selectedMarker.entityId){
				isTheSameMarker = true;
				cleanAssignResultEvent();	
				map.map_.draggableCursor="auto";
				break;
			}
			//判断是否为同类型对象
			if(mp.entityId != selectedMarker.entityId && mp.entityType == selectedMarker.entityType){
				alert("相同对象不能进行鼠标连线分配任务！");
				return false;
			}
		}
		
	}
	//判断是否为同一对象点击
	if(!isTheSameMarker){
		//保存对象
		selectedMarkerArray.push(selectedMarker);
	}
	//初始化鼠标折线
	if(mousePath == null || mousePolyline == null){
		mousePath = new Array();
		var option={"map":map};
		var strokeWeight=5;
		option.strokeOpacity=0.6;
		mousePolyline = new IPolyline(mousePath,"red",strokeWeight,option);
	}
    var latLng = selectedMarker.getPosition();
    var ifSeleted = false;
    //判断是否已经点击，true则取消连线,false则生成连线
    /*mousePath = mousePolyline.getPath();
    for(var i=0;i<mousePath.length;i++){
    	if(mousePath[i].getLongitude()==latLng.getLongitude() && mousePath[i].getLatitude()==latLng.getLatitude()){
    		ifSeleted = true;
    		break;
    	}
    }*/
     //保存已选工单
   // selectedOrderNum = $("input[name='stationRadio']").val();
 	$(".stationRadio").each(function(index){	
		if($(this).attr("checked") == "checked"){
			selectedOrderNum = $(this).val();
		}
	});
    if(!isTheSameMarker){
	   	//判断 已选标记List ，若等于2，则删除鼠标连线，打开分配任务
	    if(selectedMarkerArray.length==2){
	    	//清空折线数组的元素，取消标记
	    	IMapEvent.removeListener(mouseListener);
	    	mousePath.splice(1);
	    	mousePath.push(latLng);
			mousePolyline.setPath(mousePath);
			//alert(mousePolyline.imageOverlay_);
			if(mousePolyline.imageOverlay_){
				mousePolyline.imageOverlay_.setMap(null);
				mousePolyline.imageOverlay_ = undefined;
			}
		   	//判断类型，选中图标
	    	if(entityType == "_resource_station"){
	    		var options = {};
				options.icon = "image/u89_original2.png";
				selectedMarker.setOptions(options);
				selectedMarker.assignIcon = options.icon;	//派发图标标识
	    	}else if(entityType == "_resource_car"){
	    		var options = {};
	    		options.icon = "image/car-red.png";
				if(selectedMarker.notDutyIcon){
					options.icon = "image/car-LineOff2.png";
				}
				selectedMarker.setOptions(options);
				selectedMarker.assignIcon = options.icon;	//派发图标标识
	    	}else{
	    		var options = {};
				options.icon = "image/staff2.png";
				selectedMarker.setOptions(options);
				selectedMarker.assignIcon = options.icon;	//派发图标标识
	    	}
	    	//打开分配任务
		   	$("#gis_content_right_main").css("display","block");
		   	$("#nextStepButton").css("display","block");
			$("#assignResultLi").css("display","block");
			$("#assignResultLi").attr("class","active");
			$("#gis_content_right_data").css("display","block");
			$("#searchResultLi").removeAttr("class");
			$("#monitorLi").removeAttr("class");
			$("#searchResultDiv").css("display","none");
			$("#monitorResourceDiv").css("display","none");
			$("#loadingDiv").css("display","block");
			//鼠标样式
		   	map.map_.draggableCursor="auto";
	    	for(var i=0;i<selectedMarkerArray.length;i++){
				var marker = selectedMarkerArray[i];
				if(marker.entityType == "_resource_station"){
					entityType = marker.entityType;
			   		entityId = marker.entityId;
			   		var queryUrl = "getTaskDetailInfoOfGraphElementAction";
					var param = {resourceTypeAndId:entityType+","+entityId};
					$.ajax({
						type : "post", 
					    url : queryUrl, 
					    data : param, 
					    async : false, 
					    success : function($data){ 
					    	$("#loadingDiv").css("display","none");
					    	var res = $data;
							var queryResult = eval("("+res+")");
							showNetWorkInfoOnRight(queryResult);
							$("#stationIdHiddenText").val(entityId);
							$("#stationTypeHiddenText").val(entityType);
							//鼠标样式
		   					map.map_.draggableCursor="auto";
					    } 
					});
			   	}else if(marker.entityType == "_resource_car"){
			   		entityType = marker.entityType;
			   		entityId = marker.entityId;
			   		var queryUrl = "getTaskDetailInfoOfGraphElementAction";
			   		var param = {resourceTypeAndId:entityType+","+entityId};
					$.ajax({
						type : "post", 
					    url : queryUrl, 
					    data : param, 
					    async : false,
					    success : function($data){ 
					    	$("#loadingDiv").css("display","none");
					    	var res = $data;
					    	var queryResult = eval("("+res+")");
							//TODO 保存车辆Id，显示右边车辆信息
							showCarInfoOnRight(queryResult);
							$("#carIdHiddenText").val(entityId);
							//鼠标样式
		   					map.map_.draggableCursor="auto";
					    } 
					});
			   	}else{
			   		var isForCar = false;
			   		for(var k=0;k<selectedMarkerArray.length;k++){
			   			var m = selectedMarkerArray[k];
			   			if(m.entityType == "_resource_car"){
			   				isForCar = true;
			   			}
			   		}
					entityType = marker.entityType;
				   	entityId = marker.entityId;
				   	var queryUrl = "getTaskDetailInfoOfGraphElementAction";
					var param = {resourceTypeAndId:entityType+","+entityId};
					$.ajax({
						type : "post", 
					    url : queryUrl, 
					    data : param, 
					    async : false, 
					    success : function($data){ 
					    	$("#loadingDiv").css("display","none");
					    	var res = $data;
							var queryResult = eval("("+res+")");
							showStaffInfoOnRight(queryResult,isForCar);
							$("#staffIdHiddenText").val(entityId);
							//鼠标样式
		   					map.map_.draggableCursor="auto";
					    } 
					});
				}
			}
			$("#loadingDiv").css("display","none");
	    }else{
	    	//判断类型，选中图标
	    	if(entityType == "_resource_station"){
	    		var options = {};
				options.icon = "image/u89_original2.png";
				selectedMarker.setOptions(options);
				selectedMarker.assignIcon = options.icon;	//派发图标标识
	    	}else if(entityType == "_resource_car"){
	    		var options = {};
				options.icon = "image/car-red.png";
				if(selectedMarker.notDutyIcon){
					options.icon = "image/car-LineOff2.png";
				}
				selectedMarker.setOptions(options);
				selectedMarker.assignIcon = options.icon;	//派发图标标识
	    	}else{
	    		var options = {};
				options.icon = "image/staff2.png";
				selectedMarker.setOptions(options);
				selectedMarker.assignIcon = options.icon;	//派发图标标识
	    	}
		   	if(mousePolyline!=null && mousePolyline!='null'){
		   		//生成鼠标连线
		    	mousePath.push(latLng);
			   	mousePolyline.setPath(mousePath);
			   	if(selectedMarkerArray.length==1){
			   		mouseListener = IMapEvent.addListener(map,"mousemove",function(event){
				    	var mlatlng = event.latLng;
				    	if(mousePolyline!=null){
				    		mousePath = mousePolyline.getPath();
				    		if(mousePolyline.imageOverlay_){
								mousePolyline.imageOverlay_.setMap(null);
								mousePolyline.imageOverlay_ = undefined;
						   	}
						   	var tishiDiv = "<div style=\"border:1px solid red;width:100px;height:30px;\">按Esc取消连线</div>";
						   	mousePolyline.imageOverlay_ = new IImageOverlay(mlatlng,tishiDiv,{});
							mousePolyline.imageOverlay_.setMap(map);
				    		//鼠标连线
					   		if(selectedMarkerArray!=null){
					   			if(selectedMarkerArray.length==2){
					   				
						   		}else{
						   			mousePath.splice(1);	
							   		mousePath.push(mlatlng);
							   		mousePolyline.setPath(mousePath);
							   		map.addOverlay(mousePolyline);
						   		}
					   		}else{
					   			IMapEvent.removeListener(mouseListener);
					   		}
				    	}
				    });
			   	}else{
    				IMapEvent.removeListener(mouseListener);
			   	}
		   	}
		   	map.map_.draggableCursor="crosshair";
	    }
    }else{
    	//清空折线数组的元素，取消标记
    	IMapEvent.removeListener(mouseListener);
    	//删除梯度
    	if(mousePolyline.imageOverlay_){
			mousePolyline.imageOverlay_.setMap(null);
			mousePolyline.imageOverlay_ = undefined;
		}
    	//删除折线
	    map.removeOverlay(mousePolyline);
	    
    	mousePath = null;
	   	mousePolyline = null;
	   	if(selectedMarkerArray!=null){
	   		//还原Marker对象
			for(var i=0;i<selectedMarkerArray.length;i++){
			 	var marker = selectedMarkerArray[i];
			  	if(marker.entityType == "_resource_station" && marker.entityId == entityId){
			  		var options = {};
					options.icon = "image/u89_original.png";
					marker.setOptions(options);
					marker.assignIcon = undefined;	//派发图标标识
			  	}else if(marker.entityType == "_resource_station" && marker.entityId == entityId){
			  		var options = {};
					options.icon = marker.basicIcon;
					marker.setOptions(options);
					marker.assignIcon = undefined;	//派发图标标识
			  	}else{
			  		var options = {};
					options.icon = "image/staff1.png";
					marker.setOptions(options);
					marker.assignIcon = undefined;	//派发图标标识
			  	}
			}
		   	//清除 已选Marker 中的数据
		   	for(var i=0;i<selectedMarkerArray.length;i++){
		   		var marker = selectedMarkerArray[i];
		   		if(marker.entityId == entityId){
		   			if(selectedMarkerArray.length > 1){
		   				selectedMarkerArray.splice(i,1);
		   			}else{
		   				selectedMarkerArray = new Array();
		   			}
		   		}
		   	}
	   	}
	   	map.map_.draggableCursor="auto";
    }
    currentInfoWindow.close();
}
/**
 * 清除鼠标连线派发任务结果
 */
function cleanAssignResultEvent(){
	//删除折线
	IMapEvent.removeListener(mouseListener);
	if(mousePath!=null){
		if(mousePolyline.imageOverlay_){
			mousePolyline.imageOverlay_.setMap(null);
			mousePolyline.imageOverlay_ = undefined;
		}
		map.removeOverlay(mousePolyline);
		mousePath = null;
		mousePolyline.setMap(null);
		mousePolyline = null;
	}
	//还原Marker对象
	for(var i=0;i<selectedMarkerArray.length;i++){
		var marker = selectedMarkerArray[i];
		if(marker.entityType == "_resource_station"){
			var options = {};
			options.icon = marker.basicIcon;
			//判断是否被搜索
			if(marker.searchIcon){
				options.icon = marker.searchIcon;
			}
			marker.setOptions(options);
			marker.assignIcon = undefined;	//派发图标标识
	   	}else if(marker.entityType == "_resource_car"){
	   		var options = {};
			options.icon = marker.basicIcon;
			//判断车辆状态
			var carState = marker.carState;
			if(carState){
				var carIcon = "image/car-LineOff.png";
				if(carState && carState=='1'){
			    	carIcon = "image/car.png";
			    }else if(carState && carState=='2'){
			    	carIcon = "image/car-Static.png";
			    }
				options.icon = carIcon;
			}
			//判断是否被搜索
			if(marker.searchIcon){
				options.icon = marker.searchIcon;
			}
			marker.setOptions(options);
			marker.assignIcon = undefined;	//派发图标标识
	   	}else{
	   		var options = {};
			options.icon = marker.basicIcon;
			//判断是否被搜索
			if(marker.searchIcon){
				options.icon = marker.searchIcon;
			}
			marker.setOptions(options);
			marker.assignIcon = undefined;	//派发图标标识
		}
	}
	selectedMarkerArray = null;
	//清空表格
	showNetWorkInfoOnRight(null);
	showStaffInfoOnRight(null,null);
	showCarInfoOnRight(null);
	//清空数据
	$("#staffIdHiddenText").val("");
	$("#stationIdHiddenText").val("");
	$("#stationTypeHiddenText").val("");
	$("#carIdHiddenText").val("");
	selectedOrderNum = null;
	
	$("#cleanAssignResultButton").css("display","none");
	$("#nextStepButton").css("display","none");
	
	$("#assignResultLi").hide();
	$("#tabmenu li").removeClass("active");
	$("#monitorLi").addClass("active");
	$("#monitorResourceDiv").css("display","block");
}
/**
 * 点击下一步，分配任务
 */
function nextToAssignTaskEvent(){
	var locationHref = "";
	$(".assignStationRadio").each(function(index){	
		if($(this).attr("checked") == "checked"){
			selectedOrderNum = $(this).val();
			//locationHref = $(this).next().val();
			locationHref = $(this).attr("bizValue_")
		}
	});
	var assignTaskUserId  = $("#staffIdHiddenText").val();
	var assignTaskStationId = $("#stationIdHiddenText").val();
	var workobjectType = $("#stationTypeHiddenText").val();
	var carId = $("#carIdHiddenText").val();
	//派发出车任务
	if(carId!=null && carId!=""){
		//判断派发对象是人还是站址
		if(assignTaskUserId){
			//派发给人
			assignTaskUserId = assignTaskUserId.substring(assignTaskUserId.lastIndexOf("_")+1);
			carId = carId.substring(carId.lastIndexOf("_")+1);
			var carNumber = $("#carNumberHiddenText").val();
			var driverCarId = $("#driverCarIdHiddenText").val();
			var driverAccount = $("#driverAccountHiddenText").val();
			var WOID = $(":radio[name='cdRadio']:checked").val();
			if(!WOID || WOID==""){
				alert("请先选择一张车辆调度单！");
				return false;
			}
			var url = "../cardispatch/cardispatchWorkorder!enterCardispatchWorkorderAction.action?WOID="+WOID+"&carNumber="+carNumber+"&driverCarId="+driverCarId;
			window.open(url);
			$("#nextStepButton").css("display","none");
			$("#cleanAssignResultButton").css("display","none");
			cleanAssignResultEvent();
			return false;
		}else{
			//派发给站址
			assignTaskStationId=assignTaskStationId.substring(assignTaskStationId.lastIndexOf("_")+1);
			carId = carId.substring(carId.lastIndexOf("_")+1);
			var url = "../cardispatch/cardispatch_applyworkorder.jsp?stationId="+assignTaskStationId;
			window.open(url);
			$("#nextStepButton").css("display","none");
			$("#cleanAssignResultButton").css("display","none");
			cleanAssignResultEvent();
			return false;
		}
	}
	if(selectedOrderNum == null){
		alert("无工单/任务单可派发！");
		return false;
	}
	if(assignTaskUserId  != '' && assignTaskUserId != null){
		assignTaskUserId = assignTaskUserId.substring(assignTaskUserId.lastIndexOf("_")+1);
		workobjectType = workobjectType.substring(workobjectType.lastIndexOf("_")+1);
		assignTaskStationId=assignTaskStationId.substring(assignTaskStationId.lastIndexOf("_")+1);
		var queryUrl = "assignTaskOnPicUnitAction";
		var param = {assignTaskLocationAction:locationHref,orderNum:selectedOrderNum,assignTaskUserId:assignTaskUserId,workobjectType:workobjectType,stationId:assignTaskStationId};
		$.ajax({ 
		   type : "post", 
		   url : queryUrl, 
		   data : param,
		   async : true, 
		   dataType : "json",
		   success : function($data){ 
			    var res = $data;
			    if(res.assignRes=='success'){
			    	window.open("../"+res.assignUrl);
			    	$("#nextStepButton").css("display","none");
					$("#cleanAssignResultButton").css("display","none");
					cleanAssignResultEvent();
			    }else if(res.assignRes=='fail'){
			    	if(confirm(res.assignInfo)){
			    		window.open("../"+res.assignUrl);
			    		$("#nextStepButton").css("display","none");
						$("#cleanAssignResultButton").css("display","none");
						cleanAssignResultEvent();
			    	}
			    }else if(res.assignRes=='error'){
			    	alert(res.assignInfo);
			    }else{
					alert("该人员不能与该任务单/工单分配任务！");
				}
		     } 
		}); 
	}
}
/**
 * 清除地图上可见标记
 */
function cleanVisiableMarkers(){
	for(var i=0;i<allLayers.length;i++){
		var visiableList = allLayers[i].visiableList;
		for(var i=0;i<visiableList.length;i++){
			var marker = visiableList[i];
			marker.textOverlay_.setMap(null);
			marker.textOverlay_ = undefined;
			if(marker.imageOverlay_){
				marker.imageOverlay_.setMap(null);
				marker.imageOverlay_ = undefined;
			}
			marker.setMap(null);
		}
		allLayers[i].visiableList = new Array();
	}
}

/**
 * 创建Marker对象
 * @return marker
 */
function createMarker(data){
	var nwMarker = null;
	if(data){
		var layerKey = data.key;
		var layerName = data.layerName;
		var geList = data.geList;
		//分类保存数据
		var d1 = {};
		d1.key = layerKey;
		d1.allList = new Array();
		d1.visiableList = new　Array();
		d1.disVisibleList = new Array();
		if(allLayers.length>0){
			for(var i=0;i<allLayers.length;i++){
				var al = allLayers[i];
				if(al.key == layerKey){
					d1.allList = al.allList;
					d1.visiableList = al.visiableList;
					d1.disVisibleList = al.disVisibleList;
					allLayers.splice(k,1);
				}
			}
		}
		for(var j=0;j<geList.length;j++){
			var layer = geList[j];
			var isVisible = layer.isVisible;
			//判断类型
			var type = layer.type;
			if(checkIfInAllMarkers(layer.key)){
				var lat = layer.latitude;	//纬度
				var lng = layer.longitude;	//经度
				lat = stringToFloat(""+lat);
				lng = stringToFloat(""+lng);
				if(!lat){lat=0.0;}
				if(!lng){lng=0.0;}
				var position = new ILatLng(lat,lng);
				var position = IMapComm.gpsToMapLatLng(position);
				//移动地图中心
				map.setCenter(position);
				//在地图上添加标记
				nwMarker = new IMarker(position,layer.name,{});
				nwMarker.carState = layer.isOnDuty;
				var markerOptions = {};
				//判断是否被搜索，显示图标
				if(searchResultMarkers!=null){
					for(var f1=0;f1<searchResultMarkers.length;f1++){
						var srMarker = searchResultMarkers[f1];
						if(srMarker.entityId == layer.key){
							markerOptions.icon = srMarker.searchIcon;
							break;
						}
					}
				}
				if(!markerOptions.icon){
					markerOptions.icon = layer.icon;
				}
				nwMarker.setOptions(markerOptions);
				var totalTaskCount = layer.taskInfo.totalTaskCount;
				if(totalTaskCount == null){
					totalTaskCount = 0;
				}
				var taskText = "";
/*taskDetailInfos:[{name:'抢修工单指示器',count:0,needShowLittleIcon:true,littleIcon:'image/qiangxiuLittleIcon.png'},
{name:'巡检工单指示器',count:0,needShowLittleIcon:true,littleIcon:'image/xunjianLittleIcon.png'},
{name:'修缮工单指示器',count:0,needShowLittleIcon:true,littleIcon:'image/xiushanLittleIcon.png'}]}*/
				var taskDetailList = layer.taskInfo.taskDetailInfos;
				if(taskDetailList){
					var iconList = "";
					for(var k=0;k<taskDetailList.length;k++){
						var visiable = taskDetailList[k].needShowLittleIcon;
						var ltCount = taskDetailList[k].count;
						if(visiable == true && ltCount>0){
							var iconSrc = taskDetailList[k].littleIcon;
							iconList+="<img src=\""+iconSrc+"\" />";
						}
					}
					if(iconList!=""){
						nwMarker.imageOverlay_ = new IImageOverlay(position,iconList,{});
						nwMarker.imageOverlay_.setMap(map);
					}
				}
				if(layer.title){
					if(type == "_resource_maintaingroup" || type=="_resource_headerquarteraddress" || type=="_resource_shiyebuaddress" || type=="_resource_projectgroupaddress" || type=="_resource_manitainareaaddress"){
						taskText=""+layer.title+"<span style=\"color:black;\">(</span><span style=\"color:red;\">"+totalTaskCount+"</span><span style=\"color:black;\">人)</span>"+"";
					}else{
						taskText=""+layer.title+"<span style=\"color:black;\">(</span><span style=\"color:red;\">"+totalTaskCount+"</span><span style=\"color:black;\">单)</span>"+"";
					}
				}
				var statusText = ""+taskText;
				var statusLabel = "<lable style='text-shadow:1px 1px 10px #000000;color: #222222;font-size: 12px;'>"+statusText+"</lable>";
				nwMarker.textOverlay_ = new ITextOverlay(position,statusLabel,{});
				//保存数据到Marker对象
				nwMarker.entityId = layer.key;
				nwMarker.entityType = layer.type;
				nwMarker.parentId = layerKey;
				nwMarker.isVisible = isVisible;
				nwMarker.taskTitle = layer.title;
				nwMarker.basicIcon = layer.icon;
				nwMarker.totalTaskCount = totalTaskCount;
				nwMarker.statusLabel_ = statusLabel;
				
				//显示Marker
				if(isTitleShow){
					nwMarker.textOverlay_.setMap(map);
					nwMarker.textOverlay_.isShow_ = true;
				}
				nwMarker.setMap(map);
				//判断是否可见
				if(isVisible == true){
					//保存到Marker数组
					d1.visiableList.push(nwMarker);
				}else{
					d1.disVisibleList.push(nwMarker);
				}
				//绑定鼠标单击事件
				bindMarkerClickEvent(nwMarker);
				//保存数据
				d1.allList.push(nwMarker);
				//保存Marker
				allMarkers.push(nwMarker);
			}
		}
		allLayers.push(d1);
	}
	return nwMarker;
}
/**
 * 工单列表的全选事件
 */
function WOCheckAll(){
	//查询结果的全选事件
	$("#WOCheckButton").click(function(){
		if($("#WOCheckButton").attr("checked") == "checked"){
			$(".WOCheckBox").each(function(index){
				$(this).attr("checked","checked");
			});
		}else{
			$(".WOCheckBox").each(function(index){
				$(this).removeAttr("checked");
			});
		}
	});
}
/**
 * 任务单列表的全选事件
 */
function TOCheckAll(){
	//查询结果的全选事件
	$("#TOCheckButton").click(function(){
		if($("#TOCheckButton").attr("checked") == "checked"){
			$(".TOCheckBox").each(function(index){
				$(this).attr("checked","checked");
			});
		}else{
			$(".TOCheckBox").each(function(index){
				$(this).removeAttr("checked");
			});
		}
	});
}
/**
 * 在右边显示人员信息
 */
function showStaffInfoOnRight(data,isForCar){
	var staff = data;
	var table = $("#staffInfoTable");
	table.html("");
	//数据为空，则清空表格
	if(!data||data.length==0){
		table.html("");
		return ; //参数过滤
	}
	var basicInfo = staff.basicInfo;
	var skillList = staff.skillList;
	var busyOrNot = staff.busyOrNot;
	
	var staffName = basicInfo.staffName;
	var contactPhone = basicInfo.contactPhone;
	var sex = basicInfo.sex;
	if(staffName == null || staffName == undefined || staffName=='null'){staffName="  ";}
	if(sex == null || sex == undefined || sex=='null'){sex="  ";}
	if(contactPhone == null || contactPhone == undefined || contactPhone == 'null'){contactPhone="  ";}
	
	
	//姓名
	//Hard code
	var layerType = "_resource_human";
	
    var tr = $("<tr class='main_tr'><th colspan='2' style='text-align:left;'><img  class='top_tr_img' src='image/ico_show.gif' />&nbsp;人员信息：<a href='#' onclick=\"openResourceInfoWindow('"+basicInfo.staffId+"','"+layerType+"')\">"+staffName+"</a>,"+sex+","+contactPhone+"</th></tr>");
    tr.appendTo(table);
    
    if(isForCar&&isForCar!=null){
    	var tr4 = "<tr><td colspan='2'><div>车辆调度单：</div>";
		var orderTreeList = staff.orderTreeList;
		if(orderTreeList){
			tr4+="<ul style='height:100px;overflow-y:scroll;'>";
			for(var i=0;i<orderTreeList.length;i++){
				var wo = orderTreeList[i];
				var taskName = wo.taskName;
				var WOID = wo.orderNum;
				var statuName = wo.statuName;
				var locationHref = "../../cardispatch/cardispatchWorkorder!enterCardispatchWorkorderAction.action?WOID="+WOID;
				
				if(!taskName||taskName=='null'){taskName="";}
				if(!WOID||WOID=='null'){WOID="";}
				if(!statuName||statuName=='null'){statuName="";}
				
				tr4+="<li>";
				if(statuName=='待派车'){
					tr4+="<input type='radio' class='cdRadio vm' name='cdRadio' value='"+WOID+"' /><a href='"+locationHref+"' target='_blank' >"+WOID+"</a>,"+taskName+","+statuName;
				}else{
					tr4+="<input type='radio' disabled='disabled' class='cdRadio vm' name='cdRadio' value='"+WOID+"' /><a href='"+locationHref+"' target='_blank' >"+WOID+"</a>,"+taskName+","+statuName;
				}
				tr4+="</li>";
			}
			tr4+="</ul>";
		}
		tr4+="</td></tr>";
		tr4 = $(tr4);
		tr4.appendTo(table);
		return false;
	}
	
	
    var tr2 = "<tr><td colspan='2'><ul class='td_tab_ul'><li class='tab_on'>技能</li><li>当前任务数<span class='red_word'>（"+busyOrNot.totalTask+"）</span></li></ul>";
    tr2+="<div class='td_tab_container'><div class='td_tab_content'>";
    if(skillList){
    	tr2+="<ul class='style_ul'>";
		for(var i=0;i<skillList.length;i++){
			var sk = skillList[i];
			var skType = !sk.skillType?'':sk.skillType;
			var skYear = !sk.experienceYear?'1':sk.experienceYear;
			var skGrade = !sk.skillGrade?'':sk.skillGrade;
			tr2+="<li>"+skType+","+skGrade+","+skYear+"年</li>";
		}
		tr2+="</ul>";
	}
    tr2+="</div>";
	
	var staffId = basicInfo.staffId;
	staffId = staffId.substring(staffId.lastIndexOf("_")+1);
	
    tr2+="<div class='td_tab_content' style='display:none;' id='hrGantt'>";
    tr2+="</div></td><input type='hidden' class='smallHiddenCarId'/><input type='hidden' class='smallHiddenStaffId'/></tr>";
    tr2 = $(tr2);
    tr2.appendTo(table);
    
	//甘特图
	createGanttContent("hrGantt","staffSmallGanttContent","ssDatepicker",staffId,"people",null);
	
	$(".td_tab_ul li").each(function(index){
		$(this).click(function(){
			$(this).parent().find($("li")).removeClass("tab_on");
			$(this).addClass("tab_on");
			$(this).parent().parent().find($(".td_tab_container .td_tab_content")).hide();
			$(this).parent().parent().find($(".td_tab_container .td_tab_content")).eq(index).show();
		})
	})
}
/**
 * 在右边显示车辆信息
 */
function showCarInfoOnRight(data){
	//鼠标样式
	map.map_.draggableCursor="auto";
//{basicInfo:{key:'1',name:'粤A410Q8',carType:'小轿车',driverName:'陈志全',driverPhone:'13025196621'},
	var car = data;
	var table = $("#carInfoTable");
	table.html("");
	//数据为空，则清空表格
	if(!data||data.length==0){
		table.html("");
		return ; //参数过滤
	}
	var basicInfo = car.basicInfo;
	
	var carNumber = basicInfo.name;
	var carName = basicInfo.name;
	var carType = basicInfo.carType;
	var driverName = basicInfo.driverName;
	var driverPhone = basicInfo.driverPhone;
	var carId = basicInfo.key;
	var driverCarId = basicInfo.driverCarId;
	var driverAccount = basicInfo.driverAccount;
	var carPic = basicInfo.carPic;
	var passengerNumber = basicInfo.passengerNumber;
	
	//Hard code
	carId = "_resource_car_"+carId;
	if(carName == null || carName == undefined || carName=='null'){carName="  ";}
	if(carType == null || carType == undefined || carType=='null'){carType="  ";}
	if(driverName == null || driverName == undefined || driverName == 'null'){driverName="  ";}
	if(driverPhone == null || driverPhone == undefined || driverPhone=='null'){driverPhone="  ";}
	if(carPic == null || carPic == undefined || carPic=='null'){carPic="";}
	if(!passengerNumber || passengerNumber=='null'){passengerNumber=" ";}
	
	var orderTreeList = car.orderTreeList;
	var totalTask = 0;
	if(orderTreeList){
		totalTask = orderTreeList.length;
	}
	carPic = "../../cardispatch/"+carPic;
	//车辆信息
	//Hard code
	var layerType = "_resource_car";
	
	var tr = $("<tr class='main_tr'><input type='hidden' id='driverAccountHiddenText' value='"+driverAccount+"'/><input type='hidden' id='carNumberHiddenText' value='"+carName+"'/><input type='hidden' id='driverCarIdHiddenText' value='"+driverCarId+"'/><th colspan='2' style='text-align:left;'><img  class='top_tr_img' src='image/ico_show.gif' />&nbsp;车辆信息：<a href=\"#\" onclick=\"openResourceInfoWindow('"+carId+"','"+layerType+"')\">"+carName+"</a></th></tr>");
    tr.appendTo(table);
    
    var tr2 = "<tr><td colspan='2'><ul class='td_tab_ul'><li class='tab_on'>基本信息</li><li>当前任务数<span class='red_word'>（"+totalTask+"）</span></li></ul>";
    tr2+="<div class='td_tab_container'><div class='td_tab_content'>";
    tr2+="<ul class='style_ul'>";
	tr2+="<li>司机姓名："+driverName+"</li>";
	tr2+="<li>司机电话："+driverPhone+"</li>";
	tr2+="<li>车辆座位数："+passengerNumber+"</li>";
	tr2+="</ul>";
    tr2+="<div class='driverPic'><img style='height:65px;width:65px;'src='"+carPic+"'/></div></div>";
    
	
    tr2+="<div class='td_tab_content' style='display:none;' id='crGantt'>";
    tr2+="</div></td><input type='hidden' class='smallHiddenCarId'/><input type='hidden' class='smallHiddenStaffId'/></tr>";
    tr2 = $(tr2);
    tr2.appendTo(table);
    
	//甘特图
	createGanttContent("crGantt","carSmallGanttContent","csDatepicker",basicInfo.key,"car",null);
	
	$(".td_tab_ul li").each(function(index){
		$(this).click(function(){
			$(this).parent().find($("li")).removeClass("tab_on");
			$(this).addClass("tab_on");
			$(this).parent().parent().find($(".td_tab_container .td_tab_content")).hide();
			$(this).parent().parent().find($(".td_tab_container .td_tab_content")).eq(index).show();
		})
	})
}
/**
 * 在右边显示网络设施信息
 */
function showNetWorkInfoOnRight(data){
	var table = $("#netWorkTable");
	table.html("");
	//数据为空，则清空表格
	if(!data||data.length==0){
		table.html("");
		return ; //参数过滤
	}
	
	var basicInfo = data.basicInfo;
	var rank = basicInfo.rank;
	if(rank == null || 'null'==rank){rank="未知";}
	//网络设施名称
	var stationAccessUrl = basicInfo.stationAccessUrl;
	
	var tempStationId=basicInfo.key;
	//Hard code
	var layerType = "_resource_station";
	
    var th = $("<tr class='main_tr'><th colspan='2' style='text-align:left;'><img class='top_tr_img' src='image/ico_show.gif' />站址：<a href='#'  onclick=\"openResourceInfoWindow('"+tempStationId+"','"+layerType+"')\">"+basicInfo.name+"</a><span class='fr mr10'><a href='#'>历史维护记录</a></span></th></tr>");
    th.appendTo(table);
    var tr = $("<tr><td class='menuTd'>地址：</td><td>"+basicInfo.address+"</td></tr>");
    tr.appendTo(table);
    
    var tr2 =$("<tr></tr>");
	var td = $("<td colspan='2'><div>活动工单：<span class='fr mr10'><a href='#'>历史工单</a></span></div></td>");
	var ul = createWorkOrderTree(data.orderTreeList);
	ul.appendTo(td);
	td.appendTo(tr2);
	tr2.appendTo(table);
    //触发treeView事件
	$("#tree2").treeview({
		collapsed: false,
		animated: "fast",
		control:"#sidetreecontrol"
	});
	//再卡一下，防止出现异常情况
	map.map_.draggableCursor="auto";
}
/**
 * 顶部菜单Table切换
 */
function topTableChange(){
	$(".tab_1 ul li").each(function(index){
		$(this).click(function(){
			$(".tab_1 ul li").removeClass("ontab");
			$(this).addClass("ontab");
			$(".tab_content").hide();
			$(".tab_content").eq(index).show();
		})
	});
}
/**
 * 初始化marker
 */
function initMarker(data){
	var layerlist = data;
	if(layerlist){
		for(var i=0;i<layerlist.length;i++){
			lazyCreateMarkers(layerlist[i]);
		}
	}
}
var dIndex = 0;
function lazyCreateMarkers(data){
	if(!data){return false;}
	var geList = data.geList;
	var layerName = data.layerName;
	var layerKey = data.key;
	if(!geList || geList.length<1){return false;}
	//分类保存数据
	var d1 = {};
	d1.key = layerKey;
	d1.allList = new Array();
	d1.visiableList = new　Array();
	d1.disVisibleList = new Array();
	
	if(allLayers.length>0){
		for(var k=0;k<allLayers.length;k++){
			var al = allLayers[k];
			if(al.key == layerKey){
				d1.allList = al.allList;
				d1.visiableList = al.visiableList;
				d1.disVisibleList = al.disVisibleList;
				allLayers.splice(k,1);
			}
		}
	}
	lazyForeachCreateMarkers(geList,d1);
	//循环调用
}
function lazyForeachCreateMarkers(data,d1){
	var dCount = data.length;
	if(dIndex == 0){
		dCount = dCount%2==0?dCount/2:dCount/2-0.5;
	}else{
		dCount = data.length;
	}
	if(dIndex==data.length || (dIndex==data.length-1)){
		allLayers.push(d1);
		dIndex = 0;
		return false;
	}
	if(data && data.length>0){
		for(;dIndex<dCount;dIndex++){
			var layer = data[dIndex];
			var isVisible = layer.isVisible;
			//判断类型
			var type = layer.type;
			if(checkIfInAllMarkers(layer.key)){
				var marker = buildMarker(layer);
				//显示Marker
				if(isTitleShow){
					marker.textOverlay_.setMap(map);
					marker.textOverlay_.isShow_ = true;
				}
				marker.setMap(map);
				//判断是否可见
				if(isVisible == true){
					//保存到Marker数组
					d1.visiableList.push(marker);
				}else{
					d1.disVisibleList.push(marker);
				}
				//绑定鼠标单击事件
				bindMarkerClickEvent(marker);
				//保存数据
				d1.allList.push(marker);
				//保存Marker
				allMarkers.push(marker);
			}
		}
		lazyForeachCreateMarkers(data,d1);
	}
}
function buildMarker(layer){
	var isVisible = layer.isVisible;
	var type = layer.type;
	var lat = layer.latitude;	//纬度
	var lng = layer.longitude;	//经度
	lat = stringToFloat(""+lat);
	lng = stringToFloat(""+lng);
	if(!lat){lat=0.0;}
	if(!lng){lng=0.0;}
	var position = new ILatLng(lat,lng);
	var position = IMapComm.gpsToMapLatLng(position);
	//在地图上添加标记
	var marker = new IMarker(position,layer.name,{});
	var markerOptions = {};
	//判断车辆是否离线（针对车辆）
	if(layer.isOnDuty){
		if(layer.isOnDuty!='null'){
			// LineOff(离线) : 0 ,Travel (行驶中 ) : 1 ,Static (静止 ) : 2 ,Init (待初始化 ) : 3 
		    var carIcon = "image/car-LineOff.png";	//离线或待初始化
		    var carState = layer.isOnDuty;
		    if(carState && carState=='1'){
		    	carIcon = "image/car.png";
		    }else if(carState && carState=='2'){
		    	carIcon = "image/car-Static.png";
		    }
			markerOptions.icon = carIcon;
			marker.carState = carState;
			marker.stateIcon = carIcon;
		}
	}
	//判断是否被搜索，显示图标
	if(searchResultMarkers!=null){
		for(var f1=0;f1<searchResultMarkers.length;f1++){
			var srMarker = searchResultMarkers[f1];
			if(srMarker.entityId == layer.key){
				markerOptions.icon = srMarker.searchIcon;
				break;
			}
		}
	}
	//判断是否已经被选中
	var isChecked = false;
	for(var k=0;k<choosedMarkerArray.length;k++){
		var mk = choosedMarkerArray[k];
		if(mk.entityId == layer.key){
			markerOptions.icon = mk.checkedIcon;
		}
	}
	//不被搜索，则显示默认图标
	if(!markerOptions.icon){
		markerOptions.icon = layer.icon;
	}
	marker.setOptions(markerOptions);
	var totalTaskCount = layer.taskInfo.totalTaskCount;
	if(totalTaskCount == null){
		totalTaskCount = 0;
	}
	var taskText = "";
	//生成脚标指示器（针对站址）
	var taskDetailList = layer.taskInfo.taskDetailInfos;
	if(taskDetailList){
		//taskDetailInfos:[{name:'抢修工单指示器',count:0,needShowLittleIcon:false,littleIcon:'image/1.png'},{name:'巡检工单指示器',count:0,needShowLittleIcon:false,littleIcon:'image/2.png'},{name:'修缮工单指示器',count:0,needShowLittleIcon:false,littleIcon:'image/3.png'}]
		var iconList = "";
		for(var k=0;k<taskDetailList.length;k++){
			var visiable = taskDetailList[k].needShowLittleIcon;
			var ltCount = taskDetailList[k].count;
			if(visiable == true && ltCount>0){
				var iconSrc = taskDetailList[k].littleIcon;
				iconList+="<img src=\""+iconSrc+"\" />";
			}
		}
		if(iconList!=""){
			marker.imageOverlay_ = new IImageOverlay(position,iconList,{});
			marker.imageOverlay_.setMap(map);
		}
	}
	//针对维护组等
	if(layer.title){
		if(type == "_resource_maintaingroup" || type=="_resource_headerquarteraddress" || type=="_resource_shiyebuaddress" || type=="_resource_projectgroupaddress" || type=="_resource_manitainareaaddress"){
			taskText=""+layer.title+"<span style=\"color:black;\">(</span><span style=\"color:red;\">"+totalTaskCount+"</span><span style=\"color:black;\">人)</span>"+"";
		}else{
			taskText=""+layer.title+"<span style=\"color:black;\">(</span><span style=\"color:red;\">"+totalTaskCount+"</span><span style=\"color:black;\">单)</span>"+"";
		}
	}
	var statusText = ""+taskText;
	var statusLabel = "<lable class='icon_shadow'>"+statusText+"</lable>";
	marker.textOverlay_ = new ITextOverlay(position,statusLabel,{});
	//保存数据到Marker对象
	marker.entityId = layer.key;
	marker.entityType = layer.type;
	marker.isVisible = isVisible;
	marker.taskTitle = layer.title;
	marker.basicIcon = layer.icon;
	marker.totalTaskCount = totalTaskCount;
	marker.statusLabel_ = statusLabel;
	return marker;
}
/**
 * 验证是否存在该marker对象
 */
function checkIfInAllMarkers(key){
	var isExit = true;
	for(var i=0;i<allMarkers.length;i++){
		var marker = allMarkers[i];
		if(marker.entityId == key){
			isExit = false;
			break;
		}
	}	
	//console.log("key:"+key+",isExit:"+isExit);
	return isExit;
}
/**
 * 给标记绑定鼠标双击事件
 * @param {Object} marker
 */
function bindMarkerDoubleClickEvent(marker){
    var clickType = undefined;
	IMapEvent.addListener(marker, "dblclick", function(){
		clickType = "dblclick";
		setTimeout(function(){
			if(clickType=="dblclick")
				clickType=undefined;
		},300);
	}); 
};



/**
 * 获取左边图层架构树
 */
function showLayerTree(){
	//获取默认组织架构树
    var queryUrl="getLayerTreeConfigAction";
    $.ajax({ 
	   type : "post", 
	   url : queryUrl, 
	   async : true, 
	   success : function($data){ 
		    var res=$data;
			var queryResult=eval("("+res+")");
			if(queryResult){
				//拼装字符串，生成左边树
				//alert(queryResult);
				createLayerTree(queryResult,true);
				//保存左边树数据
				layerTreeData = null;
				layerTreeData = queryResult;
			}
	     } 
	}); 
}
/**
 * 创建左边图层架构树
 * param ()
 */
function createLayerTree(data,isCreate_){
	if(!data){
		aler("传入图层架构数据为空！");
		return false;
	}
	var json = data;
	var listViewDom = $("#treeDiv");
	var tableHtml = "<ul id=\"tree\"></ul>";
	var table = $(tableHtml);
		
	//数据为空，则清空表格
	if(!data || data.length == 0){
		listViewDom.html("");
		table.appendTo(listViewDom);
		return ; //参数过滤
	}
	//循环生成数据
	var layerName = json.name;
	var li = $("<li><input type=\"checkbox\" class=\"mr5 vm\" onclick=\"allSelect(this)\"  checked='checked'/><span class=\"icon-layers icon\"></span>图元</li>");
	li.appendTo(table);
	var layerList = json.layerList;
	
	
	var ul = $("<ul></ul>");
	ul.appendTo(li);
	//循环生成数据
	allTreeValues = new Array();
	for(var i=0;i<layerList.length;i++){
		//循环生成资源图元类
		var layer = layerList[i];
		if(layer){
			var layerName = layer.name;
			var layerKey = layer.key;
			var selected = layer.selected;
			var geTypes = layer.geTypes;
			var t1 = {};
			t1.key = layerKey;
			t1.visiable = selected;
			allTreeValues.push(t1);
			/*显示图层名称按钮*/
			if(layerName=='_layerName'){
				$("#isTitleShow").val(layerKey);
				if(selected == true){
					$("#isTitleShow").attr("checked","checked");
					if(isCreate_){
						if(!isTitleShow || selected){
							if(allMarkers){
								for(var a=0;a<allMarkers.length;a++){
									var marker = allMarkers[a];
									if(marker.textOverlay_){
										marker.textOverlay_.setMap(map);
									}
								}
							}
						}
						isTitleShow = true;
					}
				}else{
					$("#isTitleShow").removeAttr("checked");
					if(isCreate_==true){
						if(isTitleShow || !selected){
							if(allMarkers){
								for(var a=0;a<allMarkers.length;a++){
									var marker = allMarkers[a];
									if(marker.textOverlay_){
										marker.textOverlay_.setMap(null);
									}
								}
							}
						}
						isTitleShow = false;
					}
				}
				continue;
			}
			//判断是否选中
			if(selected == true){
				var li2 = $("<li><input type=\"checkbox\" checked=\"checked\" class=\"pel vm\" value=\""+layerKey+"\" onclick=\"selectChildLayer(this)\"/><span class=\"icon-layers icon\"></span>"+layerName+"</li>");
				li2.appendTo(ul);
			}else{
				var li2 = $("<li><input type=\"checkbox\" class=\"pel vm\" value=\""+layerKey+"\" onclick=\"selectChildLayer(this)\"/><span class=\"icon-layers icon\"></span>"+layerName+"</li>");
				li2.appendTo(ul);
			}
			//循环生成资源图元下的子图元
			if(geTypes.length > 0){
				var ul2 = $("<ul></ul>");
				ul2.appendTo(li2);
				for(var j=0;j<geTypes.length;j++){
					var type = geTypes[j];
					var typeName = type.name;
					var selected = type.selected;
					var typeKey = type.key;
					typeKey = layerKey+"-"+typeKey;
					var t2 = {};
					t2.key = typeKey;
					t2.visiable = selected;
					allTreeValues.push(t2);
					var code = type.code;
					var geClass="";
					geClass = judgeStyleClass(code);
					var littleIcons = type.littleIcons;
					var maxMile = type.maxMile;
					//判断是否选中
					if(selected == true){
						var li3 = $("<li><input type=\"checkbox\" checked=\"checked\" class=\"layer vm\" value=\""+typeKey+"\" onclick=\"selectParentLayer(this);selectChildLayer(this);selectLittleIcon(this)\"/><span class=\"icon "+geClass+"\"></span>"+typeName+"<span style=\"color:blue;\">("+maxMile+"公里内)</span></li>");
						li3.appendTo(ul2);
					}else{
						var li3 = $("<li><input type=\"checkbox\" class=\"layer vm\" value=\""+typeKey+"\" onclick=\"selectParentLayer(this);selectChildLayer(this);selectLittleIcon(this)\" /><span class=\"icon "+geClass+"\"></span>"+typeName+"<span style=\"color:blue;\">("+maxMile+"公里内)</span></li>");
						li3.appendTo(ul2);
					}
					//循环生成资源图元下的子图元
					if(littleIcons.length > 0){
						var ul3 = $("<ul></ul>");
						ul3.appendTo(li3);
						for(var k=0;k<littleIcons.length;k++){
							var lt = littleIcons[k];
							var ltName = lt.name;
							var selected = lt.selected;
							var ltKey = lt.key;
							ltKey = typeKey+"-"+ltKey;
							var t3 = {};
							t3.key = ltKey;
							t3.visiable = selected;
							allTreeValues.push(t3);
							var geClass = "";
							geClass = judgeStyleClass(ltName);
							//判断是否选中
							if(selected == true){
								var li4 = $("<li><input type=\"checkbox\" checked=\"checked\" class=\"layer vm\" value=\""+ltKey+"\" onclick=\"selectIconParent(this)\"/><span class=\"icon-LittleIcon "+geClass+"\"></span>"+ltName+"</li>");
								li4.appendTo(ul3);
							}else{
								var li4 = $("<li><input type=\"checkbox\" class=\"layer vm\" value=\""+ltKey+"\" onclick=\"selectIconParent(this)\" /><span class=\"icon-LittleIcon "+geClass+"\"></span>"+ltName+"</li>");
								li4.appendTo(ul3);
							}	
						}
					}
				}
			}
		}
	}
	listViewDom.html("");
	table.appendTo(listViewDom);
	//触发treeView事件
	$("#tree").treeview({
		collapsed: false,
		animated: "fast",
		control:"#sidetreecontrol"
	});
}
//判断不同样式
function judgeStyleClass(typeCode){
	if(!typeCode){return false};
	if(typeCode == "_resource_station"){
		return "icon-station";
	}else if(typeCode == "_resource_manhole"){
		return "icon-manhole";
	}else if(typeCode == "_resource_human"){
		return "icon-staff";
	}else if(typeCode == "_resource_maintaingroup"){
		return "icon-weihuzu";
	}else if(typeCode == "_resource_car"){
		return "icon-car";
	}else if(typeCode == "_resource_storehouse"){
		return "icon-storehouse";
	}else if(typeCode == "_resource_headerquarteraddress"){
		return "icon-zongbu";
	}else if(typeCode == "_resource_shiyebuaddress"){
		return "icon-shiyebu";
	}else if(typeCode == "_resource_projectgroupaddress"){
		return "icon-xiangmuzu";
	}else if(typeCode == "_resource_manitainareaaddress"){
		return "icon-pianqu";
	}if(typeCode=="_resource_falutspot"||typeCode=="_resource_watchspot"||typeCode=="_resource_questionspot"||typeCode=="_resource_hiddentroublespot"){
		return "icon-hotPoint";
	}else if(typeCode=="抢修工单指示器"){
		return "icon-qiangxiuLittleIcon";
	}else if(typeCode=="修缮工单指示器"){
		return "icon-xiushanLittleIcon";
	}else if(typeCode=="巡检工单指示器"){
		return "icon-xunjianLittleIcon";
	}
}
/**
 * 选中全部
 */
function allSelect(data){
	if($(data).attr("checked") == "checked"){
		$(".layer").each(function(index){
			$(this).attr("checked","checked");
		});
		$(".pel").each(function(index){
			$(this).attr("checked","checked");
		});
	}else{
		$(".layer").each(function(index){
			$(this).removeAttr("checked");
		});
		$(".pel").each(function(index){
			$(this).removeAttr("checked");
		});
	}
}
/**
 * 选中父级菜单
 */
function selectParentLayer(data){
	if($(data).attr("checked") == "checked"){
		$(data).parent().parent().prev().prev().attr("checked","checked");
	}else{
		var isSelected = false;
		$(data).parent().parent().children().each(function(index){
			if($(this).children().attr("checked") == "checked"){
				isSelected = true;
			}
		});
		if(isSelected){
			$(data).parent().parent().prev().prev().attr("checked","checked");
		}else{
			$(data).parent().parent().prev().prev().removeAttr("checked");
		}
	}
}
/**
 * 选中下级子菜单
 */
function selectChildLayer(data){
	if($(data).attr("checked") == "checked"){
		$(data).next().next().children().each(function(index){
			$(this).children().attr("checked","checked");
		});
	}else{
		$(data).next().next().children().each(function(index){
			$(this).children().removeAttr("checked");
		});
	}
}
/**
 * 选中下级脚标
 */
function selectLittleIcon(data){
	if($(data).attr("checked") == "checked"){
		$(data).next().next().next().children().each(function(index){
			$(this).children().attr("checked","checked");
		});
	}else{
		$(data).next().next().next().children().each(function(index){
			$(this).children().removeAttr("checked");
		});
	}
}
/**
 * 选中脚标上级父菜单
 */
function selectIconParent(data){
	if($(data).attr("checked") == "checked"){
		$(data).parent().parent().prev().prev().prev().attr("checked","checked");
		$(data).parent().parent().prev().prev().prev().parent().parent().prev().prev().attr("checked","checked");
	}else{
		var isSelected = false;
		$(data).parent().parent().children().each(function(index){
			if($(this).children().attr("checked") == "checked"){
				isSelected = true;
			}
		});
		if(isSelected){
			//选中父级菜单
			$(data).parent().parent().prev().prev().prev().attr("checked","checked");
			//选中父级菜单的父级菜单
			$(data).parent().parent().prev().prev().prev().parent().parent().prev().prev().attr("checked","checked");
		}else{
			$(data).parent().parent().prev().prev().prev().removeAttr("checked");
			$(data).parent().parent().prev().prev().prev().parent().parent().prev().prev().removeAttr("checked");
		}
	}
}
/**
 * 判断是否在当前背景窗口之内
 */
function ifInBGWindow(){
	//背景窗口对象
	var BGBounds = iscreate.maps.base.LatLngBounds(swLatLng,neLatLng);
	BGBounds = iscreate.maps.google.tools.toGoogleBounds(BGBounds);
	//当前窗口对象
	var bounds = map.getBounds();
	bounds = iscreate.maps.google.tools.toGoogleBounds(bounds);
	var sw = bounds.getSouthWest();
	var ne = bounds.getNorthEast();
	//判断某范围是否包含指定经纬度，返回boolean
	var res1 = BGBounds.contains(ne);
	var res2 = BGBounds.contains(sw);
	if(res1 && res2){
		//包含
		return true;
	}else{
		//不包含
		return false;
	}
}
/**
 * 清空要隐藏的Marker，数据一并清空
 */
function cleanHidedMarker(data){
//{hasChange:true,newlyShow:[],
//newlyHide:[{key:'1',layerName:'网络资源设施',geList:[{key:'_resource_station_f36e85f143844c8b9052f96b827339ea'},{key:'_resource_station_510b62d468464bd982c25d2bcafc7633'},{key:'_resource_station_ceba3a5e27b64a609a7a9b1068fe0840'},{key:'_resource_station_554dac6fd0f947f1be51f1f09ad4aa8a'},{key:'_resource_station_0b71c80c511f443e83f6b002a60cd5b1'},{key:'_resource_station_84031e4a2f644f53ae91c065f2d9bea4'},{key:'_resource_station_eb0d91dab970483f9f9f104007da4bb5'},{key:'_resource_station_986bdc23e4464bb1a92ebeadf66388ee'},{key:'_resource_station_329c61798eda4c6a88606b2e1c1e44e5'},{key:'_resource_station_a3a56ddf26834f7baec60e0f6d785548'},{key:'_resource_station_0bf4db85446349f38572c1b757c7cc5f'},{key:'_resource_station_0b8c740fc7104ff6b599a2749a9f0cd4'},{key:'_resource_station_fc8561409e63428d941454acf93879ca'},{key:'_resource_station_353a7f849b884012bb5fa86215f6f036'},{key:'_resource_station_279b313ee43e4a32a44b971d6821aa8f'},{key:'_resource_station_be838c50f18f45458238976f9202a1e9'},{key:'_resource_station_ffcf4e14963941f69c942fbd22def459'},{key:'_resource_station_59fd87ee841f49feab86a4d509ec0096'},{key:'_resource_station_108ee55aa49d4e10ae219a1d585588f0'},{key:'_resource_station_ebd9b3cd570f4ee599cba7af27d50fa4'},{key:'_resource_station_50b7f71c3a1c4b8fb0baf49bf8761501'},{key:'_resource_station_722b943c219d40d3aea41f5f617eae21'},{key:'_resource_station_f39e7952827042869303a786ec4158a2'},{key:'_resource_station_3c6dc81b8a47429fbf4cf9d24e622e9c'},{key:'_resource_station_2f38623b7a62499392598b0a3637c789'},{key:'_resource_station_d9c9e8c057cc43f4b628225171bfd41c'},{key:'_resource_station_a8f73cb19b9143439d5b6dc35f01cbb6'},{key:'_resource_station_d136bae6136e4636b888c679695281a0'},{key:'_resource_station_72b0bdbadebe466b888a58668a0db8ed'},{key:'_resource_station_dfe36834b6a748d29c809addde10ac4a'},{key:'_resource_station_672f0093150345f3a783274238f8f916'},{key:'_resource_station_beb1a3130eb54eaba9e1b6f033e4f59a'}]}]}
	var layerList = data;
	if(layerList){
		for(var i=0;i<layerList.length;i++){
			var layer = layerList[i];
			if(layer){
				var layerKey = layer.key;
				var geList = layer.geList;
				//循环清空allLayers中的元素
				for(var j=0;j<allLayers.length;j++){
					var al = allLayers[j];
					if(al.key == layerKey){
						var allList = al.allList;
						var visiableList=al.visiableList;
						if(geList){
							for(var n=0;n<geList.length;n++){
								var ge = geList[n];
								//循环allLayers所有列表，删除该元素(数据)
								for(var m=allList.length-1;m>=0;m--){
									var marker = allList[m];
									if(ge.key == marker.entityId){
										allList.splice(m,1);
										break;
									}
								}
								//循环allMarkers,删除该元素(setMap(null))
								for(var d=allMarkers.length-1;d>=0;d--){
									var marker = allMarkers[d];
									if(marker.entityId == ge.key){
										allMarkers.splice(d,1);
										marker.textOverlay_.setMap(null);
										marker.textOverlay_ = undefined;
										if(marker.imageOverlay_){
											marker.imageOverlay_.setMap(null);
											marker.imageOverlay_ = undefined;
										}
										marker.setMap(null);
										break;
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
/**
 * 通过zoom获取可见公里数
 */
function getVisiableKMByZoom(){
	var zoom = map.getZoom();
	zoom = parseInt(zoom);
	//TODO 判断mapSetting.apiType，返回不同公里数（不同地图Zoom级别定义可能不同）
	if(zoom == 4){
		return 1000;
	}else if(zoom == 5){
		//500公里可见
		return 500;
	}else if(zoom == 6){
		//200公里可见
		return 200;
	}else if(zoom == 7){
		//100公里可见
		return 100;
	}else if(zoom == 8){
		//50公里可见
		return 50;
	}else if(zoom == 9){
		//25公里可见
		return 25;
	}else if(zoom == 10){
		return 20;
	}else if(zoom == 11){
		return 10;
	}else if(zoom == 12){
		return 5;
	}else if(zoom == 13 || zoom > 13){
		return 2;
	}
}
/**
 * 地图可视范围变化事件
 */
var lastChangedTime = null;
function bindBoundsChangedEvent(){
	IMapEvent.addListener(map,"bounds_changed",function(){
	    lastChangedTime = new Date().getTime();
	    var myTime=lastChangedTime;
	   	setTimeout(function(){
	   	    if(myTime==lastChangedTime){
	   	    	//加载Loading效果
	   	    	showLoading();
	   	    	//触发变化事件
	   	      	responseZoomChange();
	   	    }else{
//	   	     	console.log("do nothing! your time:"+myTime+",lastChangedTime:"+lastChangedTime);
	   	    }
	   	},3000);
	});
}
function responseZoomChange(){
	var zoom = map.getZoom();
	var currentVisiableKM = getVisiableKMByZoom();
	var bounds = map.getBounds();	//当前窗口范围
	var center = map.getCenter();
	var lat = center.getLatitude();
	var lng = center.getLongitude();
	var lat = stringToFloat(lat);
	var lng = stringToFloat(lng);
	var centerLatLng = new ILatLng(lat,lng);
	
	//经纬度GPS校正
	var mapSw = bounds.getSw();
	var mapNe = bounds.getNe();
	var gpsSw = IMapComm.mapToGpsLatLng(mapSw);
	var gpsNe = IMapComm.mapToGpsLatLng(mapNe);
	
	var swlat = gpsSw.getLatitude();
	var swlng = gpsSw.getLongitude();
	var nelat = gpsNe.getLatitude();
	var nelng = gpsNe.getLongitude();
				
	var queryUrl="responseZoomLevelChangeAction";
	var param = {'swLat':swlat,'swLng':swlng,'neLat':nelat,'neLng':nelng,currentVisibleKM:currentVisiableKM,zoom:zoom,'centerLatLng.latitude':centerLatLng.getLatitude(),'centerLatLng.longitude':centerLatLng.getLongitude()};
	$.ajax({ 
	    type : "post", 
	    url : queryUrl, 
	    data : param, 
	    async : true, 
	    success : function($data){ 
          	//清除Loading效果
          	closePleaseWaitTimer();
          	var res = $data;
			var result = eval("("+res+")");
			//获取新增列表信息，创建标记对象；获取隐藏列表信息，清除要隐藏的Marker对象
			var newlyShow = result.newlyShow;
			var newlyHide = result.newlyHide;
			//清空要隐藏的元素
			if(newlyHide!=null && newlyHide!=""){
				cleanHidedMarker(newlyHide);
			}
			//显示新增图元
			if(newlyShow!=null && newlyShow!=""){
				initMarker(newlyShow);
			}
		} 
    });
}
/**
 * 地图打点标记热点事件
 */
var listener = null;
function markHotPointEvent(type){
	//最好封装TODO
	if(!map.map_){
		//TODO 不同地图类型，调用的属性可能不一样
	}else{
		map.map_.draggableCursor="url('image/green.png'),crosshair";
	}
	
	$("#biaojiredian").slideToggle("fast");
	if(type == hotPoitConstant.RESOURCE_QUESTIONSPOT){
		//问题点
		addMarkPointOnMap(type);
	}else if(type == hotPoitConstant.RESOURCE_HIDDENTROUBLESPOT){
		//隐患点
		addMarkPointOnMap(type);
	}else if(type == hotPoitConstant.RESOURCE_FAULTSPOT){
		//故障点
		addMarkPointOnMap(type);
	}else{
		//盯防点
		addMarkPointOnMap(type);
	}
}
/**
 * 在地图上添加热点标记
 */
var hotPointMarkerData = null;
var hotPointInfoWindow = null;
var hotPointMarker = null;
var hotPointListener = null;
function　addMarkPointOnMap(type){
	if(hotPointMarkerData!=null){
		cancelMarkedPointEvent();
		IMapEvent.removeListener(hotPointListener);
	}
	hotPointListener = IMapEvent.addListener(map,"click",function(event){
		if(hotPointMarkerData == null){
			if(hotPointMarker==null){
				var position = event.latLng;
				//在地图上添加标记
				hotPointMarker = new IMarker(position,type,{});
				/*var statusLabel = "<lable style='color: #222222;font-size: 12px;font-weight: bold;font-style: italic;'>"+type+"</lable>";
				hotPointMarker.textOverlay_ = new ITextOverlay(position,statusLabel,{});
				hotPointMarker.textOverlay_.setMap(map);*/
				var markerOptions = {};
				markerOptions.icon = "image/green.png";
				hotPointMarker.setOptions(markerOptions);
				hotPointMarker.bizValues_ = type;
				hotPointMarker.setMap(map);
				//保存当前 热点 Marker对象
				hotPointMarkerData = hotPointMarker;
				//弹出信息框
				var content = "<div>添加标记</div><div>名称：<input type=\"text\" value=\"我的标记\" id=\"markName\"/></div><div>备注：<textarea id=\"remark\">我的备注</textarea></div>";
				content+="<div style=\"margin-left:120px\"><input type=\"button\" value=\"保存\" onclick=\"saveMarkedPointEvent('"+type+"')\"/><input type=\"button\" value=\"删除\" onclick=\"cancelMarkedPointEvent()\"/></div>";
				
				//che.yd-----------begin------------
				if(type=="_resource_questionspot"){	//问题点
					content = "<div class=\"question\"><ul><li><label>问题点名称：</label><span><input type=\"text\" value=\"\" id=\"markName\"/></span></li><li><label>问题点地址：</label><span><input type=\"text\" value=\"bbb\" id=\"address\"/></span></li><li><label>备注：</label><span><textarea id=\"remark\">我的备注</textarea></span></li></ul></div>";
					content+="<div style=\"margin-left:120px\"><input type=\"button\" value=\"保存\" onclick=\"saveMarkedPointEvent('"+type+"')\"/><input type=\"button\" value=\"删除\" onclick=\"cancelMarkedPointEvent()\"/></div>";
					
				}else if(type=="_resource_falutspot"){		//故障点
					content = "<div class=\"question\"><ul><li><label>故障点名称：</label><span><input type=\"text\" value=\"\" id=\"markName\"/></span></li><li><label>故障点地址：</label><span><input type=\"text\" value=\"bbb\" id=\"address\"/></span></li><li><label>备注：</label><span><textarea id=\"remark\">我的备注</textarea></span></li></ul></div>";
					content+="<div style=\"margin-left:120px\"><input type=\"button\" value=\"保存\" onclick=\"saveMarkedPointEvent('"+type+"')\"/><input type=\"button\" value=\"删除\" onclick=\"cancelMarkedPointEvent()\"/></div>";
					
					//content = "<div>：<input type=\"text\" value=\"\" id=\"markName\"/></div><div>：<input type=\"text\" value=\"bbb\" id=\"address\"/></div><div>备注：<textarea id=\"remark\">我的备注</textarea></div>";
					//content+="<div style=\"margin-left:120px\"><input type=\"button\" value=\"保存\" onclick=\"saveMarkedPointEvent('"+type+"')\"/><input type=\"button\" value=\"删除\" onclick=\"cancelMarkedPointEvent()\"/></div>";
				}else if(type=="_resource_hiddentroublespot"){		//隐患点
				
					content = "<div class=\"question\"><ul><li><label>隐患点名称：</label><span><input type=\"text\" value=\"\" id=\"markName\"/></span></li><li><label>隐患点地址：</label><span><input type=\"text\" value=\"bbb\" id=\"address\"/></span></li><li><label>备注：</label><span><textarea id=\"remark\">我的备注</textarea></span></li></ul></div>";
					content+="<div style=\"margin-left:120px\"><input type=\"button\" value=\"保存\" onclick=\"saveMarkedPointEvent('"+type+"')\"/><input type=\"button\" value=\"删除\" onclick=\"cancelMarkedPointEvent()\"/></div>";
					
					//content = "<div>隐患点名称：<input type=\"text\" value=\"\" id=\"markName\"/></div><div>隐患点地址：<input type=\"text\" value=\"bbb\" id=\"address\"/></div><div>备注：<textarea id=\"remark\">我的备注</textarea></div>";
					//content+="<div style=\"margin-left:120px\"><input type=\"button\" value=\"保存\" onclick=\"saveMarkedPointEvent('"+type+"')\"/><input type=\"button\" value=\"删除\" onclick=\"cancelMarkedPointEvent()\"/></div>";
				
				}else if(type=="_resource_watchspot"){		//盯防区域
					
					content = "<div class=\"question\"><ul><li><label>盯防区域名称：</label><span><input type=\"text\" value=\"\" id=\"markName\"/></span></li><li><label>盯防区域地址：</label><span><input type=\"text\" value=\"bbb\" id=\"address\"/></span></li><li><label>备注：</label><span><textarea id=\"remark\">我的备注</textarea></span></li></ul></div>";
					content+="<div style=\"margin-left:120px\"><input type=\"button\" value=\"保存\" onclick=\"saveMarkedPointEvent('"+type+"')\"/><input type=\"button\" value=\"删除\" onclick=\"cancelMarkedPointEvent()\"/></div>";
					
					//content = "<div>盯防区域名称：<input type=\"text\" value=\"\" id=\"markName\"/></div><div>盯防区域地址：<input type=\"text\" value=\"bbb\" id=\"address\"/></div><div>备注：<textarea id=\"remark\">我的备注</textarea></div>";
					//content+="<div style=\"margin-left:120px\"><input type=\"button\" value=\"保存\" onclick=\"saveMarkedPointEvent('"+type+"')\"/><input type=\"button\" value=\"删除\" onclick=\"cancelMarkedPointEvent()\"/></div>";
				
				}
				
				//che.yd-----------end------------
				
				
				hotPointInfoWindow = new IInfoWindow("",{"position":position});
				hotPointInfoWindow.setContent(content);
				hotPointInfoWindow.open(map);
				map.map_.draggableCursor="pointer";
			}else{
				cancelMarkedPointEvent();
				IMapEvent.removeListener(hotPointListener);
			}
		}else{
			cancelMarkedPointEvent();
			IMapEvent.removeListener(hotPointListener);
		}
	});
}
/*
 * 取消、删除热点
 */
function cancelMarkedPointEvent(){
//	hotPointMarker.textOverlay_.setMap(null);
//	hotPointMarker.textOverlay_ = undefined;
	hotPointMarker.setMap(null);
	hotPointInfoWindow.close();
	hotPointMarker = null;
	hotPointMarkerData = null;
	IMapEvent.removeListener(hotPointListener);
}
/**
 * 保存热点
 */
function saveMarkedPointEvent(type){
	var markName = $("#markName").val();
	if(markName == ""){
		alert("请填写热点名称！");
		return false;
	}
	var address = $("#address").val();
	var userMark = "";
	var remark = $("#remark").val();
	var position = hotPointMarker.getPosition();
	var lat = position.getLatitude();
	var lng = position.getLongitude();
	//拼装参数
	userMark+="markName:"+markName+",markType:"+type+",longitude:"+lng+",latitude:"+lat+",remark:"+remark+",address:"+address;
	var queryUrl = "saveUserMarkAction";
	var param={userMark:userMark};
	$.ajax({ 
	   type : "post", 
	   url : queryUrl, 
	   data : param, 
	   async : true, 
	   success : function($data){ 
		    var res = $data;
			var result = eval("("+res+")");
			//创建Marker对象
			cancelMarkedPointEvent();
			var marker = null;
			var gelist = result.gelist;
			for(var i=0;i<gelist.length;i++){
				var ge = gelist[i];
				var lat = ge.latitude;	//纬度
				var lng = ge.longitude;	//经度
				lat = stringToFloat(""+lat);
				lng = stringToFloat(""+lng);
				if(!lat){lat=0.0;}
				if(!lng){lng=0.0;}
				var position = new ILatLng(lat,lng);
				//在地图上添加标记
				marker = new IMarker(position,ge.name,{});
				var markerOptions = {};
				markerOptions.icon = ge.icon;
				marker.setOptions(markerOptions);
				marker.setMap(map);
				var title="<span style=\"color:black;\">(</span><span style=\"color:red;\">"+ge.taskInfo.totalTaskCount+"</span><span style=\"color:black;\">)</span>"+"";
				var statusText = ""+title;
				var statusLabel = "<lable style='text-shadow:1px 1px 10px #000000;color: #222222;font-size: 12px;'>"+ge.name+statusText+"</lable>";
				marker.textOverlay_ = new ITextOverlay(position,statusLabel,{});
				marker.textOverlay_.setMap(map);
				marker.textOverlay_.isShow_ = true;
				//保存数据到Marker对象
				marker.entityId = ge.key;
				marker.entityType = ge.type;
				marker.isVisible = ge.isVisible;
				marker.title = ge.title;
				//绑定事件
				bindMarkerClickEvent(marker);
			}
			
			//保存数据到allLayers
			var layerKey = result.key;
			var flag = result.flag;
			for(var i=0;i<allLayers.length;i++){
				var al = allLayers[i];
				if(al.key == layerKey){
					if(ge.isVisible){
						al.visiableList.push(marker);
					}else{
						al.disVisableList.push(marker);
					}
					al.allList.push(marker);
					break;
				}
			}
			allMarkers.push(marker);
	     } 
	}); 
}
//获取用户工作列表
function getUserWorkOrderList(bizTypeCode,workOrderType){
	getUserWorkOrderListByPage(bizTypeCode,workOrderType,0,10);
}
//分页获取用户工作列表
function getUserWorkOrderListByPage(bizTypeCode,workOrderType,pageIndex,pageSize){
	if(pageIndex==1){
		$("#curPageIndex").val(1);
	}
	var queryUrl = "getWorkOrderListOfUserForGisDispatch";
	$.ajax({ 
	   type : "post", 
	   url : queryUrl, 
	   data :{bizTypeCode:bizTypeCode,workOrderType:workOrderType,pageIndex:pageIndex,pageSize:pageSize},
	   async : true, 
	   success : function($data){ 
		    var res = $data;
			res = eval("("+res+")");
			var totalPage = 0;
			var totalCount = 0;
			if(res){
				var index = res.length-1;
				if(index!=-1){
					totalPage = res[index].totalPage;
					totalCount = res[index].totalCount;
				}
				if(bizTypeCode=='urgentrepair'){
					showUrResultTable(res);
				}else if(bizTypeCode=='cardispatch'){
					showCdResultTable(res);
				}else if(bizTypeCode=='resourcedispatch'){
					showRdResultTable(res);
				}else{
					
				}
			}
			$("#woResult").html("共"+totalCount+"条记录");
			$("#totalPage").html(totalPage);
	     } 
	}); 
}
/**
 * 显示抢修工单列表
 */ 
function showUrResultTable(data){
	var workOrderList = data;
	var table = $("#urTable");
	table.html("");
	var thHtml = "<tr id='b'><th style='width:30px;'></th><th style='width:30px;'><input type='checkbox' id='WOCheckButton' /></th><th>工单号</th><th style='width:60px;'>工单类型</th><th style='width:32%'>工单主题</th><th style='width:45px;'>创建人</th><th>创建时间</th><th>要求完成时间</th><th>当前责任人</th><th>工单状态</th></tr>";
	var th = $(thHtml);
	th.appendTo(table);
                        
   	//数据为空，则清空表格
	if(!data||data.length==0){
		return ; //参数过滤
	}
	for(var i=0;i<workOrderList.length;i++){
		var wo = workOrderList[i];
		if(!wo)continue;
		var WOID = wo.WOID;
		var WOTITLE = wo.WOTITLE;
		var CREATEPERSON = wo.CREATEPERSON;
		var CREATETIME = wo.CREATETIME;
		var BIZ_OVERTIME = wo.BIZ_OVERTIME;
		var OPERATORID = wo.OPERATORID;
		var STATUSNAME = wo.STATUSNAME;
		var resourceId = "_resource_station_"+wo.resourceId;
		var locationHref="../../"+wo.locationHref+"?WOID="+WOID;	//che.yd添加的
		var isRead = wo.ISREADED;	//0:未读 1:已读 2：未读，状态改变 3：已读，状态改变
		var isQuick = wo.isQuick;
		var isOver = wo.isOver;
		var WOTYPE = wo.WOTYPE;
		
		
		if(!WOID)continue;
		if(!WOID){WOID="";}
		if(!WOTITLE || WOTITLE=='null'){WOTITLE="";}
		if(!CREATEPERSON || CREATEPERSON=='null'){CREATEPERSON="";}
		if(!CREATETIME || CREATETIME=='null'){CREATETIME="";}
		if(!BIZ_OVERTIME || BIZ_OVERTIME=='null'){BIZ_OVERTIME="";}
		if(!OPERATORID || OPERATORID=='null'){OPERATORID="";}
		if(!STATUSNAME || STATUSNAME=='null'){STATUSNAME="";}
		if(!resourceId){resourceId="";}
		if(!locationHref){locationHref="";}	//che.yd添加的
		if(!WOTYPE){WOTYPE="";}
		if(!BIZ_OVERTIME||BIZ_OVERTIME=='null'){BIZ_OVERTIME="";}
		
		//Hard code 
		var layerType = "_resource_station";
		var tr = $("<tr class='urTr' onclick=\"openResourceInfoWindow('"+resourceId+"','"+layerType+"')\"></tr>");
		var index = i+1 ;
		var vs = $("<td>"+ index +"</td>");
		var checkBox = $("<input class=\"WOCheckBox\" type=\"checkbox\" />");
		var checkBoxTd = $("<td></td>");
		checkBox.attr("value",wo.WOID);
		checkBox.appendTo(checkBoxTd);
		var WOID = $("<td><a href='"+locationHref+"' target=\"_blank\">"+WOID+"</a></td>");
     	var woTitle = "<td><span class=\"wotitle_span\">"+WOTITLE+"<span class='icon_span'>";
     	if(isRead){
     		if(isRead=='0'){
     			woTitle+="<em class='back_icon icon_new'>新</em>";
     		}else if(isRead=='2' || isRead=='3'){
     			woTitle+="<em class='back_icon icon_change'>变</em>";	
     		}
     	}
     	if(isOver){
     		woTitle+="<em class='back_icon icon_over'>超</em>";
     	}
     	if(isQuick){
     		woTitle+="<em class='back_icon icon_quick'>快</em>";
     	}
     	woTitle+="</span></span></td>";
		var WOTITLE = $(woTitle);	//che.yd添加
		var CREATEPERSON = $("<td>"+CREATEPERSON+"</td>");
		var CREATETIME = $("<td>"+CREATETIME+"</td>");
		var BIZ_OVERTIME = $("<td>"+BIZ_OVERTIME+"</td>");
		var OPERATORID = $("<td>"+OPERATORID+"</td>");
		var STATUSNAME = $("<td>"+STATUSNAME+"</td>");
		var WOTYPE = $("<td>"+WOTYPE+"</td>");
		
		vs.appendTo(tr);
		checkBoxTd.appendTo(tr);
		WOID.appendTo(tr);
		WOTYPE.appendTo(tr);
		WOTITLE.appendTo(tr);
		CREATEPERSON.appendTo(tr);
		CREATETIME.appendTo(tr);
		BIZ_OVERTIME.appendTo(tr);
		OPERATORID.appendTo(tr);
		STATUSNAME.appendTo(tr);
		tr.appendTo(table);
	}
	//全选事件
	WOCheckAll();
}
/**
 * 显示车辆调度工单列表
 */ 
function showCdResultTable(data){
	var workOrderList = data;
	var table = $("#cdTable");
	table.html("");
	var thHtml = "<tr id=\"b\"><th style=\"width:30px;\"></th><th style=\"width:30px;\"><input type=\"checkbox\" id=\"WOCheckButton\" /></th><th>工单号</th><th style=\"width:32%\">工单主题</th><th>创建人</th><th>创建时间</th><th>要求完成时间</th><th>当前责任人</th><th>工单状态</th></tr>";
	var th = $(thHtml);
	th.appendTo(table);
                        
   	//数据为空，则清空表格
	if(!data||data.length==0){
		return ; //参数过滤
	}
	for(var i=0;i<workOrderList.length;i++){
		var wo = workOrderList[i];
		if(!wo)continue;
		var WOID = wo.WOID;
		var WOTITLE = wo.WOTITLE;
		var CREATEPERSON = wo.CREATEPERSON;
		var CREATETIME = wo.CREATETIME;
		var BIZ_OVERTIME = wo.BIZ_OVERTIME;
		var OPERATORID = wo.OPERATORID;
		var STATUSNAME = wo.STATUSNAME;
		var resourceId = "_resource_human_"+wo.resourceId;
		
		var locationHref="../../"+wo.locationHref+"?WOID="+WOID;	
		var isRead = wo.ISREADED;	//0:未读 1:已读 2：未读，状态改变 3：已读，状态改变
		var isQuick = wo.isQuick;
		var isOver = wo.isOver;
		if(!WOID){WOID=""};
		if(!WOTITLE||WOID=='null'){WOTITLE=""};
		if(!CREATEPERSON || CREATEPERSON=='null'){CREATEPERSON=""};
		if(!CREATETIME||CREATETIME=='null'){CREATETIME=""};
		if(!BIZ_OVERTIME||BIZ_OVERTIME=='null'){BIZ_OVERTIME=""};
		if(!OPERATORID || OPERATORID=='null'){OPERATORID=""};
		if(!STATUSNAME||STATUSNAME=='null'){STATUSNAME=""};
		if(!locationHref){locationHref=""}; 
		if(!BIZ_OVERTIME||BIZ_OVERTIME=='null'){BIZ_OVERTIME="";}
		if(!WOID)continue;
		
		var tr = $("<tr class='cdTr' onclick=\"openResourceInfoWindow('"+resourceId+"','_resource_human')\"></tr>");
		var index = i+1 ;
		var vs = $("<td>"+ index +"</td>");
		var checkBox = $("<input class=\"WOCheckBox\" type=\"checkbox\" />");
		var checkBoxTd = $("<td></td>");
		checkBox.attr("value",wo.WOID);
		checkBox.appendTo(checkBoxTd);
		var WOID = $("<td><a href='"+locationHref+"' target=\"_blank\">"+WOID+"</a></td>");
     	var woTitle = "<td><span class=\"wotitle_span\">"+WOTITLE+"<span class='icon_span'>";
     	if(isRead){
     		if(isRead=='0'){
     			woTitle+="<em class='back_icon icon_new'>新</em>";
     		}else if(isRead=='2' || isRead=='3'){
     			woTitle+="<em class='back_icon icon_change'>变</em>";	
     		}
     	}
     	if(isOver){
     		woTitle+="<em class='back_icon icon_over'>超</em>";
     	}
     	if(isQuick){
     		woTitle+="<em class='back_icon icon_quick'>快</em>";
     	}
     	woTitle+="</span></span></td>";
		var WOTITLE = $(woTitle);	//che.yd添加
		var CREATEPERSON = $("<td>"+CREATEPERSON+"</td>");
		var CREATETIME = $("<td>"+CREATETIME+"</td>");
		var BIZ_OVERTIME = $("<td>"+BIZ_OVERTIME+"</td>");
		var OPERATORID = $("<td>"+OPERATORID+"</td>");
		var STATUSNAME = $("<td>"+STATUSNAME+"</td>");
		
		vs.appendTo(tr);
		checkBoxTd.appendTo(tr);
		WOID.appendTo(tr);
		WOTITLE.appendTo(tr);
		CREATEPERSON.appendTo(tr);
		CREATETIME.appendTo(tr);
		BIZ_OVERTIME.appendTo(tr);
		OPERATORID.appendTo(tr);
		STATUSNAME.appendTo(tr);
		tr.appendTo(table);
	}
	//全选事件
	WOCheckAll();
}
/**
 * 显示物资调度单列表
 */ 
function showRdResultTable(data){
	var workOrderList = data;
	var table = $("#rdTable");
	table.html("");
	var thHtml = "<tr id=\"b\"><th style=\"width:30px;\"></th><th style=\"width:30px;\"><input type=\"checkbox\" id=\"WOCheckButton\" /></th><th>工单号</th><th style=\"width:32%\">工单主题</th><th>创建人</th><th>创建时间</th><th>要求完成时间</th><th>当前责任人</th><th>工单状态</th></tr>";
	var th = $(thHtml);
	th.appendTo(table);
                        
   	//数据为空，则清空表格
	if(!data||data.length==0){
		return ; //参数过滤
	}
	for(var i=0;i<workOrderList.length;i++){
		var wo = workOrderList[i];
		if(!wo)continue;
		var WOID = wo.WOID;
		var WOTITLE = wo.WOTITLE;
		var CREATEPERSON = wo.CREATEPERSON;
		var CREATETIME = wo.CREATETIME;
		var BIZ_OVERTIME = wo.BIZ_OVERTIME;
		var OPERATORID = wo.OPERATORID;
		var STATUSNAME = wo.STATUSNAME;
		var locationHref="../../"+wo.locationHref+"?WOID="+WOID;	
		var isRead = wo.ISREADED;	//0:未读 1:已读 2：未读，状态改变 3：已读，状态改变
		var isQuick = wo.isQuick;
		var isOver = wo.isOver;
		if(!WOID){WOID=""};
		if(!WOTITLE||WOID=='null'){WOTITLE=""};
		if(!CREATEPERSON || CREATEPERSON=='null'){CREATEPERSON=""};
		if(!CREATETIME||CREATETIME=='null'){CREATETIME=""};
		if(!BIZ_OVERTIME||BIZ_OVERTIME=='null'){BIZ_OVERTIME=""};
		if(!OPERATORID || OPERATORID=='null'){OPERATORID=""};
		if(!STATUSNAME||STATUSNAME=='null'){STATUSNAME=""};
		if(!BIZ_OVERTIME||BIZ_OVERTIME=='null'){BIZ_OVERTIME="";}
		if(!WOID)continue;
		
		var tr = $("<tr class='rdTr'></tr>");
		var index = i+1 ;
		var vs = $("<td>"+ index +"</td>");
		var checkBox = $("<input class=\"WOCheckBox\" type=\"checkbox\" />");
		var checkBoxTd = $("<td></td>");
		checkBox.attr("value",wo.WOID);
		checkBox.appendTo(checkBoxTd);
		var WOID = $("<td><a href='"+locationHref+"' target=\"_blank\">"+WOID+"</a></td>");
     	var woTitle = "<td><span class=\"wotitle_span\">"+WOTITLE+"<span class='icon_span'>";
     	if(isRead){
     		if(isRead=='0'){
     			woTitle+="<em class='back_icon icon_new'>新</em>";
     		}else if(isRead=='2' || isRead=='3'){
     			woTitle+="<em class='back_icon icon_change'>变</em>";	
     		}
     	}
     	if(isOver){
     		woTitle+="<em class='back_icon icon_over'>超</em>";
     	}
     	if(isQuick){
     		woTitle+="<em class='back_icon icon_quick'>快</em>";
     	}
     	woTitle+="</span></span></td>";
		var WOTITLE = $(woTitle);	//che.yd添加
		var CREATEPERSON = $("<td>"+CREATEPERSON+"</td>");
		var CREATETIME = $("<td>"+CREATETIME+"</td>");
		var BIZ_OVERTIME = $("<td>"+BIZ_OVERTIME+"</td>");
		var OPERATORID = $("<td>"+OPERATORID+"</td>");
		var STATUSNAME = $("<td>"+STATUSNAME+"</td>");
		
		vs.appendTo(tr);
		checkBoxTd.appendTo(tr);
		WOID.appendTo(tr);
		WOTITLE.appendTo(tr);
		CREATEPERSON.appendTo(tr);
		CREATETIME.appendTo(tr);
		BIZ_OVERTIME.appendTo(tr);
		OPERATORID.appendTo(tr);
		STATUSNAME.appendTo(tr);
		tr.appendTo(table);
	}
	//全选事件
	WOCheckAll();
}
/**
 * 获取用户任务单列表信息
 */
function getTaskOrderList(bizTypeCode,taskOrderType){
	getTaskOrderListByPage(bizTypeCode,taskOrderType,0,10);
}
/**
 * 分页获取用户任务单列表信息
 */
function getTaskOrderListByPage(bizTypeCode,taskOrderType,pageIndex,pageSize){
	if(pageIndex==1){
		$("#curPageIndex").val(1);
	}
	var queryUrl = "getTaskOrderListOfUserForGisDispatch";
	$.ajax({ 
	   type : "post", 
	   url : queryUrl, 
	   data : {bizTypeCode:bizTypeCode,taskOrderType:taskOrderType,pageIndex:pageIndex,pageSize:pageSize},
	   async : true, 
	   success : function($data){ 
		    var res = $data;
			res = eval("("+res+")");
			var totalCount = 0;
			var totalPage = 0;
			if(res){
				var index = res.length-1;
				if(index!=-1){
					totalPage = res[index].totalPage;
					totalCount = res[index].totalCount;
				}
				if(!totalPage){
					totalPage = 0;
				}
				$("#totalPage").html(totalPage);
				showTaskOrderList(res);
			}
			$("#woResult").html("共"+totalCount+"条记录");
			$("#totalPage").html(totalPage);
	     } 
	}); 
}
/**
 * 组装字符串，显示用户任务单列表
 */ 
function showTaskOrderList(data){
	var taskOrderList = data;
	var table = $("#toTable");
	table.html("");
	var thHtml = "<tr id='b2'><th style='width:30px'></th><th style='width:30px;'><input type='checkbox' id='TOCheckButton'/></th><th>任务单号</th><th>任务单类型</th><th style='width:32%'>任务单主题</th><th>创建人</th><th>创建时间</th><th>要求完成时间</th><th>当前责任人</th><th>工单状态</th></tr>";
	var th = $(thHtml);
	th.appendTo(table);
	//数据为空，则清空表格
	if(!data||data.length==0){
		return ; //参数过滤
	}
	for(var i=0;i<taskOrderList.length;i++){
		var to = taskOrderList[i];
		if(!to)continue;
		var TOID = to.TOID;
		var WOID = to.WOID;
		var TASKNAME = to.TASKNAME;
		var CREATEPERSON = to.ASSIGNEDPERSONID;	//che.yd添加的
		var CREATETIME = to.CREATETIME;
		var LASTEDITTIME = to.LASTEDITTIME;
		var OPERATORID = to.OPERATORID;
		var STATUS = to.STATUSNAME;	//che.yd添加的
		var OVERTIME=to.OVERTIME;	//che.yd添加的
		var resourceId = "_resource_station_"+to.resourceId;
		var locationHref="../../"+to.locationHref+"?WOID="+WOID+"&TOID="+TOID;;	//che.yd添加的
		var isRead = to.ISREADED;	//0:未读 1:已读 2：未读，状态改变 3：已读，状态改变
		var isQuick = to.isQuick;
		var isOver = to.isOver;
		var TOTYPE = to.TOTYPE;
		
		if(!TOID)continue;
		
		if(!TOID){TOID=""};
		if(!TASKNAME||TASKNAME=='null'){TASKNAME=""};
		if(!CREATEPERSON||CREATEPERSON=='null'){CREATEPERSON=""};
		if(!CREATETIME||CREATETIME=='null'){CREATETIME=""};
		if(!LASTEDITTIME||LASTEDITTIME=='null'){LASTEDITTIME=""};
		if(!OPERATORID || OPERATORID=="null"){OPERATORID=""};
		if(!STATUS||STATUS=='null'){STATUS=""};
		if(!locationHref){locationHref=""};	//che.yd添加的
		if(!TOTYPE){TOTYPE="";}
		if(!OVERTIME||OVERTIME=='null'){OVERTIME="";}
		
		//Hard code
		var layerType = "_resource_station";
		var tr = $("<tr class='toTr' onclick=\"openResourceInfoWindow('"+resourceId+"','"+layerType+"')\"></tr>");
		var index = i+1 ;
		var vs = $("<td>"+ index +"</td>");
		var checkBox = $("<input class=\"TOCheckBox\" type=\"checkbox\" />");
		var checkBoxTd = $("<td></td>");
		checkBox.attr("value",to.TOID);
		checkBox.appendTo(checkBoxTd);
		var TOID = $("<td><a href='"+locationHref+"' target=\"_blank\">"+TOID+"</a></td>");
		var woTitle = "<td><span class=\"wotitle_span\">"+TASKNAME+"<span class='icon_span'>";
     	if(isRead){
     		if(isRead=='0'){
     			woTitle+="<em class='back_icon icon_new'>新</em>";
     		}else if(isRead=='2' || isRead=='3'){
     			woTitle+="<em class='back_icon icon_change'>变</em>";	
     		}
     	}
     	if(isOver){
     		woTitle+="<em class='back_icon icon_over'>超</em>";
     	}
     	if(isQuick){
     		woTitle+="<em class='back_icon icon_quick'>快</em>";
     	}
     	woTitle+="</span></span></td>";
		var TASKNAME = $(woTitle);	//che.yd添加
		//var TASKNAME = $("<td>"+"<span class=\"wotitle_span\">"+TASKNAME+"</span>"+"</td>");
		var CREATEPERSON = $("<td>"+CREATEPERSON+"</td>");
		var CREATETIME = $("<td>"+CREATETIME+"</td>");
		var OVERTIME = $("<td>"+OVERTIME+"</td>");
		var OPERATORID = $("<td>"+OPERATORID+"</td>");
		var STATUS = $("<td>"+STATUS+"</td>");
		var TOTYPE = $("<td>"+TOTYPE+"</td>");
		
		vs.appendTo(tr);
		checkBoxTd.appendTo(tr);
		TOID.appendTo(tr);
		TOTYPE.appendTo(tr);
		TASKNAME.appendTo(tr);
		CREATEPERSON.appendTo(tr);
		CREATETIME.appendTo(tr);
		OVERTIME.appendTo(tr);
		OPERATORID.appendTo(tr);
		STATUS.appendTo(tr);
		tr.appendTo(table);
	}
	//全选事件
	TOCheckAll();
}
var order_number;
var order_title;
var order_state;
/*按条件获取工单列表*/
function getWorkOrderListByCondition(be){
	//获取要查询的属性值
	order_number = $(be).parent().find($(".queryOrderNumber")).val();
	order_title = $(be).parent().find($(".queryOrderTitle")).val();
	order_state = $(be).parent().find($(".queryOrderState")).val();
	//判断当前Tab
	var biztype = $("#curBizType").html();
	var ul = "";
	if(biztype=='urgentrepair'){
		//类型为工单
		ul = "urUl li";
	}else if(biztype=='taskorder'){
		//类型为任务单
		ul = "toUl li";
	}else if(biztype=='cardispatch'){
		//类型为车辆调度单
		ul = "cdUl li";
	}else{
		//类型为物资调度单
		ul = "rdUl li";
	}
	$("#"+ul).each(function(){
		var url = $(this).attr("onclick");
		var ck = $(this).children().attr("class");
		var bizTypeCode = $(this).attr("bizTypeCode");
		var workOrderType = $(this).attr("workOrderType");
		if(ck&&ck=='selected'){
			var param = {};
			param.bizTypeCode = bizTypeCode;
			param.taskOrderType = workOrderType;
			param.workOrderType = workOrderType;
			param.pageIndex = 0;
			param.pageSize = 10;
			param.orderNumber = order_number;
			param.orderTitle = order_title;
			param.orderState = order_state;			
			getOrderListByConditions(biztype,param);
			return;
		}
	});
}
function getOrderListByConditions(bizType,param){
	if(bizType=='taskorder'){
		$.ajax({ 
		   type : "post", 
		   url : "getTaskOrderListOfUserForGisDispatch", 
		   data : param,
		   async : true, 
		   success : function($data){ 
			    var res = $data;
				res = eval("("+res+")");
				var totalCount = 0;
				var totalPage = 0;
				if(res){
					var index = res.length-1;
					if(index!=-1){
						totalPage = res[index].totalPage;
						totalCount = res[index].totalCount;
					}
					if(!totalPage){
						totalPage = 0;
					}
					$("#totalPage").html(totalPage);
					showTaskOrderList(res);
				}
				$("#woResult").html("共"+totalCount+"条记录");
				$("#totalPage").html(totalPage);
		     } 
		}); 
	}else{
		$.ajax({ 
		   type : "post", 
		   url : "getWorkOrderListOfUserForGisDispatch", 
		   data : param,
		   async : true, 
		   success : function($data){ 
			    var res = $data;
				res = eval("("+res+")");
				var totalPage = 0;
				var totalCount = 0;
				if(res){
					var index = res.length-1;
					if(index!=-1){
						totalPage = res[index].totalPage;
						totalCount = res[index].totalCount;
					}
					if(bizType=='urgentrepair'){
						showUrResultTable(res);
					}else if(bizType=='cardispatch'){
						showCdResultTable(res);
					}else if(bizType=='resourcedispatch'){
						showRdResultTable(res);
					}
				}
				$("#woResult").html("共"+totalCount+"条记录");
				$("#totalPage").html(totalPage);
		     } 
		}); 
	}
}

/**
 * 生成工单树
 */
function createWorkOrderTree(data){
	//再卡一下，防止出现异常情况
	map.map_.draggableCursor="auto";
	//var data=[{orderNum:'01-20120418-002',taskName:'2012-4-18 集中调度测试',statuName:'已派发',orderFlag:'wo',	locationHref:'urgentrepair/displayUrgentRepairWorkOrderAction.action',childTaskList:[{orderNum:'02-20120611-001-002',parentWoId:'02-20120611-001',taskName:'第一事业部东部片区2012年第二季度2G基站巡检计划（东二区维护一组：2012-06-11 至 2012-06-11 11:11:00 ）',statuName:'处理中',locationHref:'routineinspection/loadExecuteStationRoutineInspectionAction.action',orderFlag:'to'},{orderNum:'02-20120604-002-006',parentWoId:'02-20120604-002',taskName:'第一事业部东部片区2012年6月2G基站巡检计划（东二区维护一组：2012-06-04 至 2012-06-05 00:00:00）',statuName:'处理中',locationHref:'routineinspection/loadExecuteStationRoutineInspectionAction.action',orderFlag:'to'}]},{orderNum:'01-20120726-011',taskName:'024',statuName:'已派发',orderFlag:'wo',locationHref:'urgentrepair/displayUrgentRepairWorkOrderAction.action'}];
	var workOrderList = data;
	var ul = $("<ul id=\"tree2\" style='height:100px;overflow-y:scroll;'></ul>");
	//数据为空，则清空表格
	if(!data || data.length == 0){
		return ul; //参数过滤
	}
	//循环生成数据
	for(var i=0;i<workOrderList.length;i++){
		var wo = workOrderList[i];
		if(wo){
		   	var orderNum = wo.orderNum;
		   	var taskName = wo.taskName;
		   	var statuName = wo.statuName;
		   	var locationHref = "";
		   	var parentWoId = wo.parentWoId;
		   	if(parentWoId&&parentWoId!=""){
		   		locationHref = "../../"+wo.locationHref+"?TOID="+orderNum+"&WOID="+parentWoId;
		   	}else{
		   		locationHref = "../../"+wo.locationHref+"?WOID="+orderNum;
		   	}
		   	var li = "";
			if(selectedOrderNum!=null && selectedOrderNum==orderNum){
				li = $("<li><input type=\"radio\" class=\"assignStationRadio vm\" bizValue_=\""+wo.locationHref+"\" checked=\"checked\" name=\"stationRadio\" value=\""+orderNum+"\"/><a href='"+locationHref+"' target=\"_blank\">"+orderNum+"</a>,"+taskName+","+statuName+"</li>");
				isOk = true;
			}else{
				if(selectedOrderNum==null && i==workOrderList.length-1){
					li = $("<li><input type=\"radio\" class=\"assignStationRadio vm\" bizValue_=\""+wo.locationHref+"\" checked=\"checked\" name=\"stationRadio\" value=\""+orderNum+"\"/><a href='"+locationHref+"' target=\"_blank\">"+orderNum+"</a>,"+taskName+","+statuName+"</li>");
				}else{
					li = $("<li><input type=\"radio\" class=\"assignStationRadio vm\" bizValue_=\""+wo.locationHref+"\" name=\"stationRadio\" value=\""+orderNum+"\"/><a href='"+locationHref+"' target=\"_blank\"\">"+orderNum+"</a>,"+taskName+","+statuName+"</li>");
				}
			}
			var childTaskList = wo.childTaskList;
			//循环生成子任务单
			if(childTaskList&&childTaskList.length>0){
				var ul2 = $("<ul></ul>");
				ul2.appendTo(li);
				for(var j=0;j<childTaskList.length;j++){
					var to = childTaskList[j];
					var orderNum = to.orderNum;
				   	var taskName = to.taskName;
				   	var statuName = to.statuName;
				   	var parentWoId = to.parentWoId;
				   	var locationHref = locationHref = "../../"+wo.locationHref+"?TOID="+orderNum+"&WOID="+parentWoId;
				   	var li2 = "";
					if(selectedOrderNum!=null && selectedOrderNum==orderNum){
						li2 = $("<li><input type=\"radio\" class=\"assignStationRadio vm\" bizValue_=\""+to.locationHref+"\" checked=\"checked\" name=\"stationRadio\" value=\""+orderNum+"\"/><a href='"+locationHref+"' target=\"_blank\">"+orderNum+"</a>,"+taskName+","+statuName+"</li>");
						isOk = true;
					}else{
						if(selectedOrderNum==null && j==childTaskList.length-1){
							li2 = $("<li><input type=\"radio\" class=\"assignStationRadio vm\" bizValue_=\""+to.locationHref+"\" checked=\"checked\" name=\"stationRadio\" value=\""+orderNum+"\"/><a href='"+locationHref+"' target=\"_blank\">"+orderNum+"</a>,"+taskName+","+statuName+"</li>");
						}else{
							li2 = $("<li><input type=\"radio\" class=\"assignStationRadio vm\" bizValue_=\""+to.locationHref+"\" name=\"stationRadio\" value=\""+orderNum+"\"/><a href=\""+locationHref+" target=\"_blank\"\">"+orderNum+"</a>,"+taskName+","+statuName+"</li>");
						}
					}
					li2.appendTo(ul2);
				}
			}
			li.appendTo(ul);
		}
	}
	return ul;
}
/*table切换*/
function tableToggle(){
	//右边信息框Table切换
	$(".dialog-tabmenu li").each(function(index){
		$(this).click(function(){
			$(".dialog-tabmenu li").removeClass("on");
	   		$(this).addClass("on");
	   		$(".dialog-tabcontent dd").hide();
	   		var curIndex = $(".dialog-tabmenu li").index($(this));
	   		$(this).parent().parent().find($(".dialog-tabcontent")).find("dd").eq(curIndex).show();
		})
	});
}
