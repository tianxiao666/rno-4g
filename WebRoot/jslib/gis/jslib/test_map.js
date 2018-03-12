var map = null;  //用来存放地图对象
//初始化函数
function init(){
	//在id为"map_canvas"的DOM元素(一般用div)上显示地图
	var center = new ILatLng(23.149395,113.353282);
	//var center = new ILatLng(23,113);
    map = new IMap("map_canvas",center,18,{});
  
//    //测试map.setOptions方法
//    setTimeout(function(){
//    	map.setOptions({"center": new ILatLng(24,114)});
//    	map.setOptions({"zoom":10});
//    },3000);
/****************************地图基本对象部分的测试*********************************/  
	
	var position = new ILatLng(23.149395,113.353282);
	//position = IMapComm.mapToGpsLatLng(position);
	//在地图上添加标记
	var marker = new IMarker(position,"我擦了",{});
	var markerOptions = {};
	markerOptions.icon = "../../../images/baidu_map_ico.png";
	marker.setOptions(markerOptions);
	var statusLabel = "<lable class='icon_shadow'>上课发呆</lable>";
	marker.textOverlay_ = new ITextOverlay(position,statusLabel,{});
	//保存数据到Marker对象
	marker.textOverlay_.setMap(map);
	marker.setMap(map);
	
	var aa = false;
	setInterval(function(){
		if(aa){
			marker.setMap(map);
			aa = false;
		}else{
			marker.setMap(null);
			aa = true;
		}
	},3000);
	
	/*var markerOptions = {};
	markerOptions.icon = "http://dev.baidu.com/wiki/static/map/API/img/fox.gif";
	marker.setOptions(markerOptions); */
	//var infoWindow = new IInfoWindow("World", {height:100,width:200});  // 创建信息窗口对象
	//infoWindow.setPosition(markerLatLng);
	/*测试打开信息窗口*/
   	/*IMapEvent.addListener(marker,"click",function(){
   		infoWindow.open(map);
   	});*/
	//infoWindow.open(map);
	
	/*IMapEvent.addListener(map,"bounds_changed",function(){
		console.log("我擦");
	});*/
	/*var listener2 = IMapEvent.addListener(marker,"click",function(){
		console.log("点击了");
   	});*/
	//marker.openInfoWindow(infoWindow);
   	//infoWindow.close();
   	
	
	//IMapEvent.removeListener(listener2);
	
	
	var polylinePath =  new Array();
	var latLng1 = new ILatLng(23,113);  //创建经纬度对象
    var latLng2 = new ILatLng(23.001,113.001);
    var latLng3 = new ILatLng(23.001,113.002);
    polylinePath.push(latLng1);  //设置折线的的路径
    polylinePath.push(latLng2);
    polylinePath.push(latLng3);
    var polyline = new IPolyline(polylinePath,"red",8,{strokeColor:"red", strokeWeight:8, strokeOpacity:0.5,strokeStyle:"dashed"});
	polyline.setMap(map);
//    	map.panTo(new ILatLng(23.001,113.001));  //测试map.panTo方法
//    	map.setZoom(17);
//    	map.setCenter(new ILatLng(23.001,113.001));
//    setTimeout(function(){
//    	var bounds = map.getBounds();//测试 map.getBounds()方法
//    	alert( bounds.getSw().getLatitude()+"||"+bounds.getSw().getLongitude()
//    		+"||"+bounds.getNe().getLatitude()+"||"+bounds.getNe().getLongitude());
//    	
//    	var center = map.getCenter();//测试map.getCenter()方法
//    	alert("地图中心："+ center.getLatitude()+"||"+center.getLongitude());
//    	var zoom = map.getZoom();  //测试map.getZoom() 方法
//    	alert(zoom);
//    	map.panBy(200,200); //测试map.panBy方法
//    	map.panTo(new ILatLng(23.001,113.001));  //测试map.panTo方法
//    	map.setZoom(17);
//    	map.setCenter(new ILatLng(23.001,113.001));
//    	//添加和删除标记的方法
//    	var marker1 = new IMarker(23.001,113.001,"测试标记",{});
//    	map.addOverlay(marker1);
//    	setTimeout(function(){
//    		map.removeOverlay(marker1);
//    	},3000);
//    },3000);
    
//    alert(gLatLng.getLatitude()+"||"+gLatLng.getLongitude());
    
    //在地图上添加标记
   /* var marker = new IMarker(mLatLng.getLatitude(),mLatLng.getLongitude(),"测试标记",{"map":map});

    //在地图上添加折线
    var polylinePath = new Array();
    var latLng1 = new ILatLng(23,113);  //创建经纬度对象
    var latLng2 = new ILatLng(23.001,113.001);
    var latLng3 = new ILatLng(23.001,113.002);
    polylinePath.push(latLng1);  //设置折线的的路径
    polylinePath.push(latLng2);
    polylinePath.push(latLng3);
    var polyline = new IPolyline(polylinePath,"#AAEEFF",3,{"map":map});

    //在地图上添加多边形
    var polygonPath = new Array();
    var latLng4 = new ILatLng(23,113);  //创建经纬度对象
    var latLng5 = new ILatLng(22.999,113.001);
    var latLng6 = new ILatLng(22.999,113.002);
    polygonPath.push(latLng4);  //设置折线的的路径
    polygonPath.push(latLng5);
    polygonPath.push(latLng6);
    var polygon = new IPolygon(polygonPath,{"map":map});

    //在地图上添加矩形
    var sw = new ILatLng(22.999,112.999);
    var ne = new ILatLng(23,113);
    var bounds = new ILatLngBounds(sw,ne);
    var rectangle = new IRectangle(bounds,{"map":map});

    //在地图上添加圆
    var center = new ILatLng(23.001,112.999);
    var radius = 50;
    var circle = new ICircle(center,radius,{"map":map});*/

//    //标记的测试
//    setTimeout(function(){
//    	marker.setOptions({"map":null});
//    	marker.setOptions({"draggable":true});
//    	marker.setOptions({"icon":"http://img.baidu.com/hi/img/ihome/logo.gif"});
//    	marker.setOptions({"position":new ILatLng(22.999,112.999)});
//    	marker.setOptions({"shadow":"http://img.baidu.com/hi/img/ihome/logo.gif"});
//    	marker.setOptions({"title":"修改后的标题!"});
//    	marker.setOptions({"visible":false});
//    	setTimeout(function(){
//    		marker.setOptions({"visible":true})
//    	},3000);
//    	var position = marker.getPosition();
//    	alert(position.getLatitude()+"||"+position.getLongitude());
//    	marker.setPosition(new ILatLng(22.999,112.999));
//    	marker.setVisible(false);
//    	setTimeout(function(){
//    		marker.getVisible();
//    	},3000);
//    	marker.setMap(null);
//    	setTimeout(function(){
//    		marker.setMap(map);
//    	},3000);
//    },3000);

    //测试polyline    
/*    setTimeout(function(){
    	var path = new Array();
    	path.push(new ILatLng(23,113));
    	path.push(new ILatLng(23.0011,113.002));
    	path.push(new ILatLng(23.0011,113));
    	polyline.setOptions({"path":path});
    	polyline.setOptions({"strokeColor":"#AAAAAA"});
    	polyline.setOptions({"strokeWeight":5});
    	var path0 = polyline.getPath();  
    	alert(path0[0].getLatitude()+"||"+path0[0].getLongitude());
    	var path = new Array();
    	path.push(new ILatLng(23,113));
    	path.push(new ILatLng(23.0011,113.002));
    	path.push(new ILatLng(23.0011,113));
    	polyline.setPath(path);
    },3000);*/
    
//    //测试polygon
//    setTimeout(function(){
//    	polygon.setOptions({"fillColor":"#00AA00"});
//    	polygon.setOptions({"strokeColor":"#AA0000"});
//    	polygon.setOptions({"strokeWeight":7});
//    	var path = new Array();
//    	path.push(new ILatLng(23,113));
//    	path.push(new ILatLng(23.0011,113.002));
//    	path.push(new ILatLng(23.0011,113));
//    	polygon.setOptions({"path":path});
//    	polygon.setOptions({"map":null});
//    	var path = new Array();
//    	path.push(new ILatLng(23,113));
//    	path.push(new ILatLng(23.0011,113.002));
//    	path.push(new ILatLng(23.0011,113));
//    	polygon.setPath(path);
//        var cPath = polygon.getPath();
//        alert(cPath[1].getLatitude()+"||"+cPath[1].getLongitude());
//    	polygon.setMap(null);
//    	alert(polygon.getMap())
//    	setTimeout(function(){
//    		polygon.setMap(map);
//    		alert(polygon.getMap())
//    	},3000)
//    },3000);

//  //infoWindow测试
//    var infoWindow = new IInfoWindow("<div>这是一个测试窗口!</div>",{"position":new ILatLng(23,113)});
//    infoWindow.open(map);
//    setTimeout(function(){
//    	infoWindow.close();
//    	setTimeout(function(){
//    		infoWindow.setPosition(new ILatLng(23.0005,113));
//    		var p = infoWindow.getPosition();
//    		alert(p.getLatitude()+"||"+p.getLongitude());
//            infoWindow.open(map);
//    	},3000);
//    },3000);
    
//    //rectangle测试
//    setTimeout(function(){
//    	var sw = new ILatLng(23.0001,113.0001);
//    	var ne = new ILatLng(23.0011,113.0011);
//    	var bounds = new ILatLngBounds(sw,ne);
//    	rectangle.setOptions({"bounds":bounds});
//    	rectangle.setOptions({"fillColor":"#00AA00"});
//    	rectangle.setOptions({"map":null});
//    	setTimeout(function(){
//    		rectangle.setOptions({"map":map});
//    	},3000);
//    	
//    	rectangle.setOptions({"strokeColor":"#AA0000"});
//    	rectangle.setOptions({"strokeWeight":7});
    	
//    	var bounds = rectangle.getBounds();
//    	var sw = bounds.getSw();
//        var ne = bounds.getNe();
//        alert(sw.getLatitude()+"||"+sw.getLongitude()+"    "+ne.getLatitude()+"||"+ne.getLongitude());

//    	var ssw = new ILatLng(23.0001,113.0001);
//    	var sne = new ILatLng(23.0011,113.0011);
//    	var sbounds = new ILatLngBounds(ssw,sne);
//    	rectangle.setBounds(sbounds);
    	
//    	rectangle.setMap(null);
//    	setTimeout(function(){
//    		rectangle.setMap(map);
//    	},3000);
    	
//    	//circle测试
//    	circle.setOptions({"center":new ILatLng(23.0001,113.0001)});
//    	circle.setOptions({"fillColor":"#FF00FF"});
//    	circle.setOptions({"strokeColor":"#00FFFF"});
//    	circle.setOptions({"strokeWeight":8});
//    	circle.setOptions({"radius":60});    	
//    },3000);

    
//    //事件测试
//    	//地图对象事件测试
//    	IMapEvent.addListener(map,"bounds_changed",function(){
//    		alert("你改变了经纬度的可视范围!");
//    	});
//    	IMapEvent.addListener(map,"center_changed",function(){
//    		alert("你改变了地图的中心位置!");
//    	});
//    	IMapEvent.addListener(map,"click",function(event){
//    		var latLng = event.latLng;
//    		alert("你点击了地图上的坐标点："+latLng.getLatitude()+","+latLng.getLongitude());
//    	});
//    	IMapEvent.addListener(map,"dblclick",function(event){
//    		var latLng = event.latLng;
//    		alert("你双击了地图上的坐标点："+latLng.getLatitude()+","+latLng.getLongitude());
//    	});
//    	IMapEvent.addListener(map,"mousemove",function(event){
//    		var latLng = event.latLng;
//    		alert("当前坐标："+latLng.getLatitude()+","+latLng.getLongitude());
//    	});
//    	IMapEvent.addListener(map,"mouseout",function(event){
//    		var latLng = event.latLng;
//    		alert("鼠标移出了地图："+latLng.getLatitude()+","+latLng.getLongitude());
//    	});
//    	IMapEvent.addListener(map,"mouseover",function(event){
//    		var latLng = event.latLng;
//    		alert("鼠标移上地图："+latLng.getLatitude()+","+latLng.getLongitude());
//    	});
//    	IMapEvent.addListener(map,"rightclick",function(event){
//    		var latLng = event.latLng;
//    		alert("右键点击地图地图："+latLng.getLatitude()+","+latLng.getLongitude());
//    	});
//    	IMapEvent.addListener(map,"zoom_changed",function(){
//    		alert("地图缩放级别改变!");
//    	});

//    //标记对象事件测试    
//       var clickListener = IMapEvent.addListener(marker,"click",function(event){
//        	var latLng = event.latLng;
//        	alert("标记被点击!"+latLng.getLatitude()+","+latLng.getLongitude());
//        	IMapEvent.removeListener(clickListener);
//        });
//       IMapEvent.trigger(marker,"click",{"latLng":new ILatLng(22,112)});
//        IMapEvent.addListener(marker,"dblclick",function(event){
//        	var latLng = event.latLng;
//        	alert("标记被双击!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
//        IMapEvent.addListener(marker,"mousedown",function(event){
//        	var latLng = event.latLng;
//        	alert("标记处鼠标按下!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
//        IMapEvent.addListener(marker,"mouseout",function(event){
//        	var latLng = event.latLng;
//        	alert("标记处鼠标移出!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
//        IMapEvent.addListener(marker,"mouseover",function(event){
//        	var latLng = event.latLng;
//        	alert("标记处鼠标移入!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
//        IMapEvent.addListener(marker,"mouseup",function(event){
//        	var latLng = event.latLng;
//        	alert("标记处鼠标释放!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
//        IMapEvent.addListener(marker,"rightclick",function(event){
//        	var latLng = event.latLng;
//        	alert("右键点击标记!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
    
//    marker.setOptions({"draggable":true});
//        IMapEvent.addListener(marker,"drag",function(event){
//        	var latLng = event.latLng;
//        	alert("拖动标记!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
//        IMapEvent.addListener(marker,"dragstart",function(event){
//        	var latLng = event.latLng;
//        	alert("开始拖动标记!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
//        IMapEvent.addListener(marker,"dragend",function(event){
//        	var latLng = event.latLng;
//        	alert("拖动标记结束!!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });

//    //折线对象事件测试    
//     IMapEvent.addListener(polyline,"click",function(event){
//        	var latLng = event.latLng;
//        	alert("折线被点击!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
//        IMapEvent.addListener(polyline,"dblclick",function(event){
//        	var latLng = event.latLng;
//        	alert("折线被双击!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
//        IMapEvent.addListener(polyline,"mousedown",function(event){
//        	var latLng = event.latLng;
//        	alert("折线处鼠标按下!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
//        IMapEvent.addListener(polyline,"mousemove",function(event){
//        	var latLng = event.latLng;
//        	alert("折线处鼠标移动!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
//        IMapEvent.addListener(polyline,"mouseout",function(event){
//        	var latLng = event.latLng;
//        	alert("折线处鼠标移出!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
//        IMapEvent.addListener(polyline,"mouseover",function(event){
//        	var latLng = event.latLng;
//        	alert("标记处鼠标移入!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
//        IMapEvent.addListener(polyline,"mouseup",function(event){
//        	var latLng = event.latLng;
//        	alert("折线处鼠标释放!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
//        IMapEvent.addListener(polyline,"rightclick",function(event){
//        	var latLng = event.latLng;
//        	alert("右键点击折线!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
    
//    //多边形对象事件测试    
//     IMapEvent.addListener(polygon,"click",function(event){
//        	var latLng = event.latLng;
//        	alert("多边形被点击!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
//        IMapEvent.addListener(polygon,"dblclick",function(event){
//        	var latLng = event.latLng;
//        	alert("多边形被双击!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
//        IMapEvent.addListener(polygon,"mousedown",function(event){
//        	var latLng = event.latLng;
//        	alert("多边形处鼠标按下!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
//        IMapEvent.addListener(polygon,"mousemove",function(event){
//        	var latLng = event.latLng;
//        	alert("多边形处鼠标移动!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
//        IMapEvent.addListener(polygon,"mouseout",function(event){
//        	var latLng = event.latLng;
//        	alert("多边形处鼠标移出!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
//        IMapEvent.addListener(polygon,"mouseover",function(event){
//        	var latLng = event.latLng;
//        	alert("多边形处鼠标移入!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
//        IMapEvent.addListener(polygon,"mouseup",function(event){
//        	var latLng = event.latLng;
//        	alert("多边形处鼠标释放!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
//        IMapEvent.addListener(polygon,"rightclick",function(event){
//        	var latLng = event.latLng;
//        	alert("右键点击多边形!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
    
    
//    //矩形对象事件测试    
//     IMapEvent.addListener(rectangle,"click",function(event){
//        	var latLng = event.latLng;
//        	alert("矩形被点击!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
//        IMapEvent.addListener(rectangle,"dblclick",function(event){
//        	var latLng = event.latLng;
//        	alert("矩形被双击!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
//        IMapEvent.addListener(rectangle,"mousedown",function(event){
//        	var latLng = event.latLng;
//        	alert("矩形处鼠标按下!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
//        IMapEvent.addListener(rectangle,"mousemove",function(event){
//        	var latLng = event.latLng;
//        	alert("矩形处鼠标移动!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
//        IMapEvent.addListener(rectangle,"mouseout",function(event){
//        	var latLng = event.latLng;
//        	alert("矩形处鼠标移出!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
//        IMapEvent.addListener(rectangle,"mouseover",function(event){
//        	var latLng = event.latLng;
//        	alert("矩形处鼠标移入!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
//        IMapEvent.addListener(rectangle,"mouseup",function(event){
//        	var latLng = event.latLng;
//        	alert("矩形处鼠标释放!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
//        IMapEvent.addListener(rectangle,"rightclick",function(event){
//        	var latLng = event.latLng;
//        	alert("右键点击矩形!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
    
    
//    //圆形对象事件测试    
//     IMapEvent.addListener(circle,"click",function(event){
//        	var latLng = event.latLng;
//        	alert("圆形被点击!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
//        IMapEvent.addListener(circle,"dblclick",function(event){
//        	var latLng = event.latLng;
//        	alert("圆形被双击!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
//        IMapEvent.addListener(circle,"mousedown",function(event){
//        	var latLng = event.latLng;
//        	alert("圆形处鼠标按下!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
//        IMapEvent.addListener(circle,"mousemove",function(event){
//        	var latLng = event.latLng;
//        	alert("圆形处鼠标移动!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
//        IMapEvent.addListener(circle,"mouseout",function(event){
//        	var latLng = event.latLng;
//        	alert("圆形处鼠标移出!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
//        IMapEvent.addListener(circle,"mouseover",function(event){
//        	var latLng = event.latLng;
//        	alert("圆形处鼠标移入!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
//        IMapEvent.addListener(circle,"mouseup",function(event){
//        	var latLng = event.latLng;
//        	alert("圆形处鼠标释放!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
//        IMapEvent.addListener(circle,"rightclick",function(event){
//        	var latLng = event.latLng;
//        	alert("右键点击圆形!"+latLng.getLatitude()+","+latLng.getLongitude());
//        });
    
    
    
}
//窗口加载完毕的时候运行init函数
window.onload = init