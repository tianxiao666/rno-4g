
/***************************地图对象包****************************/
/**
 * 地图类
 * @param {Object} canvas_id
 * @param {Object} latitude
 * @param {Object} longitude
 * @param {Object} zoom
 * @param {Object} mapOptions
 */
iscreate.maps.google.Map = function(canvas_id, center, zoom,mapOptions){
	var me = this;
	me.map_ = null;
	var myOptions = iscreate.maps.google.tools.toGoogleMapOptions(mapOptions)
	var googleCenter = iscreate.maps.google.tools.toGoogleLatLng(center);
	myOptions.center = googleCenter;
	myOptions.zoom = zoom;
	myOptions.mapTypeId = google.maps.MapTypeId.ROADMAP ;
	myOptions.scaleControl = true;
	me.map_ = new google.maps.Map(document.getElementById(canvas_id), myOptions);
	//生成地图控件
	canvas_id = "#"+canvas_id;
	var div="<div class='map_choose' style='box-shadow: 2px 2px 4px #333333;cursor: pointer;margin-top: 10px;position: absolute;right: 130px;top: -3px;z-index: 1;'>";
	div+="<a class='map_choose_a' style=' display:inline-block;border:1px solid #aaa; background:#eee; padding:4px 6px 4px 20px; line-height:18px;' href='#'>地图选择 ▼</a>";
	div+="<ul class='map_choose_ul' style='display:none;'>";
	if(mapSetting.apiType == 'BaiduMap'){
		div+="<li style=' background:#fff; border:1px solid #ccc; border-top:none; padding:4px; line-height:24px; text-align:center; cursor:pointer;'><input type='radio' value='BaiduMap' name='mapType' checked='checked' style='position:relative; top:3px; left:-3px;'/><img src='"+rootPath_+"/images/baidu_map_ico.png' /></li>";
		div+="<li style=' background:#fff; border:1px solid #ccc; border-top:none; padding:4px; line-height:24px; text-align:center; cursor:pointer;'><input type='radio' value='GoogleMap' name='mapType' style='position:relative; top:3px; left:-3px;'/><img src='"+rootPath_+"/images/google_map_ico.png' /></li>";
	}else if(mapSetting.apiType == 'GoogleMap'){
		div+="<li style=' background:#fff; border:1px solid #ccc; border-top:none; padding:4px; line-height:24px; text-align:center; cursor:pointer;'><input type='radio' name='mapType' value='BaiduMap' style='position:relative; top:3px; left:-3px;'/><img src='"+rootPath_+"/images/baidu_map_ico.png' /></li>";
		div+="<li style=' background:#fff; border:1px solid #ccc; border-top:none; padding:4px; line-height:24px; text-align:center; cursor:pointer;'><input type='radio' name='mapType' value='GoogleMap' checked='checked' style='position:relative; top:3px; left:-3px;'/><img src='"+rootPath_+"/images/google_map_ico.png' /></li>";
	}else{
		div+="<li style=' background:#fff; border:1px solid #ccc; border-top:none; padding:4px; line-height:24px; text-align:center; cursor:pointer;'><input type='radio' name='mapType' value='BaiduMap' style='position:relative; top:3px; left:-3px;'/><img src='"+rootPath_+"/images/baidu_map_ico.png' /></li>";
		div+="<li style=' background:#fff; border:1px solid #ccc; border-top:none; padding:4px; line-height:24px; text-align:center; cursor:pointer;'><input type='radio' name='mapType' value='GoogleMap' style='position:relative; top:3px; left:-3px;'/><img src='"+rootPath_+"/images/google_map_ico.png' /></li>";
	}
	div+="</ul>";
	div+="</div>";
	try{
		div = $(div);
		div.appendTo($(canvas_id));
		$(".map_choose").hover(function(){
				$(".map_choose_ul").slideToggle("fast");
			},function(){
				$(".map_choose_ul").hide();
		});
		$(".map_choose_ul li").each(function(){
			$(this).click(function(){
				$(".map_choose_ul").slideUp("fast");
				$(this).children().attr("checked","checked");
				var mapType = $(this).children().val();
				selectMap(mapType);
			})
		})
	}catch(e){
		alert("无法生成地图切换控件！请检查是否导入JQuery的包！ "+e);
	}
};

/**
 * 修改地图属性
 * @param {MapOptions} mapOptions
 */
iscreate.maps.google.Map.prototype.setOptions=function(mapOptions){
	var me = this;
	var myOptions = iscreate.maps.google.tools.toGoogleMapOptions(mapOptions);
	me.map_.setOptions(myOptions);
};

/**
 * 返回当前视口的纬度/经度范围
 */
iscreate.maps.google.Map.prototype.getBounds = function(){
	var me = this;
	var gBounds = me.map_.getBounds();
	if(!gBounds)return null;
	var iSw = iscreate.maps.google.tools.toIscreateLatLng(gBounds.getSouthWest());
	var iNe = iscreate.maps.google.tools.toIscreateLatLng(gBounds.getNorthEast());
	var bounds = new iscreate.maps.base.LatLngBounds(iSw,iNe);
	return bounds;
};

/**
 * 获取地图中心坐标
 */
iscreate.maps.google.Map.prototype.getCenter = function(){
	var me = this;
	var googleCenter = me.map_.getCenter();
	var iscreateCenter = iscreate.maps.google.tools.toIscreateLatLng(googleCenter);
	return iscreateCenter;
};

/**
 * 获取地图当前的缩放级别
 */
iscreate.maps.google.Map.prototype.getZoom = function(){
	var me = this;
	var zoom = me.map_.getZoom();
	return zoom;
};

/**
 * 将地图中心移动一段指定的距离（以像素为单位）
 * @param {Object} x
 * @param {Object} y
 */
iscreate.maps.google.Map.prototype.panBy = function(x, y){
	var me = this;
	me.map_.panBy(x,y);
};

/**
 * 将地图中心更改为指定的LatLng
 * @param {Object} latLng
 */
iscreate.maps.google.Map.prototype.panTo = function(latLng){
	var me = this;
	var googleLatLng = iscreate.maps.google.tools.toGoogleLatLng(latLng);
	me.map_.panTo(googleLatLng);
};

/**
 * 设置地图的缩放级别
 * @param {Object} zoom
 */
iscreate.maps.google.Map.prototype.setZoom = function(zoom){
	var me = this;
	me.map_.setZoom(zoom);
};

/**
 * 设置地图中心点位置
 * @param {Object} latLng
 */
iscreate.maps.google.Map.prototype.setCenter = function(latLng){
	var me = this;
	var googleLatLng = iscreate.maps.google.tools.toGoogleLatLng(latLng);
	me.map_.setCenter(googleLatLng);
};
/**
 * 添加叠加层
 * @param {Object} overlay
 */
iscreate.maps.google.Map.prototype.addOverlay = function(overlay){
	var me = this;
	if(overlay){
		overlay.setMap(me);
	}
};
/**
 * 删除叠加层
 * @param {Object} overlay
 */
iscreate.maps.google.Map.prototype.removeOverlay = function(overlay){
	var me = this;
	if(overlay){
		overlay.setMap(null);
	}
};

/**
 * 地图的div大小改变的时候调用该方法更新地图
 */
iscreate.maps.google.Map.prototype.resize = function(){
	var me = this;
    google.maps.event.trigger(me.map_, 'resize');
};

/**
 * 标记类
 * @param {Object} latitude 纬度
 * @param {Object} longitude 经度
 * @param {Object} title 鼠标悬停的时候显示的标题
 * @param {Object} markerOptions 标记配置项信息
 */
iscreate.maps.google.Marker = function(latLng, title, markerOptions){
	var me = this;
	var myOptions = iscreate.maps.google.tools.toGoogleMarkerOptions(markerOptions);
	var googleLatLng = iscreate.maps.google.tools.toGoogleLatLng(latLng);
	myOptions.position = googleLatLng;
	myOptions.title = title;
	me.marker_ = new google.maps.Marker(myOptions);
	me.overlay_obj_ = me.marker_; //事件响应的时候用到
};

/**
 * 设置标记的属性值
 * @param {Object} MarkerOptions
 */
iscreate.maps.google.Marker.prototype.setOptions = function(markerOptions){
	var me = this;
	var myOptions = iscreate.maps.google.tools.toGoogleMarkerOptions(markerOptions);
	me.marker_.setOptions(myOptions);
}

/**
 * 获取标记所在的经纬度信息
 */
iscreate.maps.google.Marker.prototype.getPosition = function(){
	var me = this;
	var googlePosition = me.marker_.getPosition();
	var iscreatePositon = iscreate.maps.google.tools.toIscreateLatLng(googlePosition);
	return iscreatePositon;
}
/**
 * 设置标记所在的经纬度信息
 * @param {Object} latLng
 */
iscreate.maps.google.Marker.prototype.setPosition = function(latLng){
	var me = this;
	var googleLatLng = iscreate.maps.google.tools.toGoogleLatLng(latLng);
	me.marker_.setPosition(googleLatLng);
}
/**
 * 设置标记是否可见
 * @param {Object} visible
 */
iscreate.maps.google.Marker.prototype.setVisible = function(visible){
	var me = this;
	me.marker_.setVisible(visible);
}
/**
 * 获取标记是否可见
 */
iscreate.maps.google.Marker.prototype.getVisible = function(){
	var me = this;
	return me.marker_.getVisible();
}

/**
 * 设置地图对象
 * @param {Object} latLng
 */
iscreate.maps.google.Marker.prototype.setMap = function(map){
	var me = this;
	me.belong_map_ = map;
	if(map==null){
		me.marker_.setMap(null);
	}else{
	    me.marker_.setMap(map.map_);
	}
}

/**
 * 获取地图对象
 * @param {Object} latLng
 */
iscreate.maps.google.Marker.prototype.getMap = function(){
	var me = this;
	return me.belong_map_ ;
}
/**
 * 打开信息窗口
 * @param {Object} infoWindow
 */
iscreate.maps.google.Marker.prototype.openInfoWindow = function(infoWindow){
	var me = this;
	infoWindow.infoWindow_.open(me.marker_.map);
}

/**
 * 折线类
 * @param {Object} latlngList
 * @param {Object} strokeColor
 * @param {Object} strokeWeight
 * @param {Object} polylineOptions
 */
iscreate.maps.google.Polyline = function(latLngList,strokeColor,strokeWeight,polylineOptions){
	var me = this;
	var myOptions = iscreate.maps.google.tools.toGooglePolylineOptions(polylineOptions);
	var path = new Array();
	for (var i=0;i<latLngList.length;i++) {
		var lat = latLngList[i].getLatitude();
		var lng = latLngList[i].getLongitude();
		var latLng = new google.maps.LatLng(lat, lng);
		path.push(latLng);
	}
	myOptions.strokeColor = strokeColor;
	myOptions.strokeWeight = strokeWeight;
	myOptions.path = path;
	myOptions.strokeOpacity=polylineOptions.strokeOpacity
	me.polyline_ = new google.maps.Polyline(myOptions);	
	me.overlay_obj_ = me.polyline_; //事件响应的时候用到
};
/**
 * 设置折线的属性值
 * @param {Object} polylineOptions
 */
iscreate.maps.google.Polyline.prototype.setOptions = function(polylineOptions){
	var me = this;
	var myOptions = iscreate.maps.google.tools.toGooglePolylineOptions(polylineOptions);
	me.polyline_.setOptions(myOptions);
};

/**
 * 获取当前折线的路径(有序的经纬度对象序列)
 */
iscreate.maps.google.Polyline.prototype.getPath = function(){
	var me = this;
	var googlePath = me.polyline_.getPath();
	var resultArray = new Array();  //存放经纬度对象的有序序列
	if(googlePath!=undefined&&googlePath!=null&&googlePath!=""){
		for(var i=0;i<googlePath.getLength();i++){
			var googleLatLng =googlePath.getAt(i);
			var iscreateLatLng = iscreate.maps.google.tools.toIscreateLatLng(googleLatLng);
			resultArray.push(iscreateLatLng);
		}
	}
	return resultArray;
};
/**
 * 设置当前折线的路径
 * @param {Object} path
 */
iscreate.maps.google.Polyline.prototype.setPath = function(path){
	var me = this;
	if(path==undefined||path==null||path==""){
		return;
	}
	var googlePath = new Array();
	for(var i=0;i<path.length;i++){
		var iscreateLatLng = path[i];
		var googleLatLng = iscreate.maps.google.tools.toGoogleLatLng(iscreateLatLng);
		googlePath.push(googleLatLng);
	}
	me.polyline_.setPath(googlePath);
};

/**
 * 设置折线是否可见
 * @param {Object} visible
 */
iscreate.maps.google.Polyline.prototype.setVisible = function(visible){
	var me = this;
	me.polyline_.setVisible(visible);
}
/**
 * 获取折线是否可见
 */
iscreate.maps.google.Polyline.prototype.getVisible = function(){
	var me = this;
	return me.polyline_.getVisible();
}

/**
 * 给折线设置地图对象
 * @param {Object} latLng
 */
iscreate.maps.google.Polyline.prototype.setMap = function(map){
	var me = this;
	me.belong_map_ = map;
	if(map==null){
		me.polyline_.setMap(null);
	}else{
	    me.polyline_.setMap(map.map_);
	}
}

/**
 * 获取地图对象
 * @param {Object} latLng
 */
iscreate.maps.google.Polyline.prototype.getMap = function(){
	var me = this;
	return me.belong_map_ ;
}
/**
 * 打开信息窗口
 * @param {Object} infoWindow
 */
iscreate.maps.google.Polyline.prototype.openInfoWindow = function(infoWindow){
	var me = this;
	infoWindow.infoWindow_.open(me.polyline_.map);
}

/**
 * 多边形类
 * @param {Object} polygonPath
 * @param {Object} polygonOptions
 */
iscreate.maps.google.Polygon = function(polygonPath, polygonOptions){
	var me = this;
	var myOptions = iscreate.maps.google.tools.toGooglePolygonOptions(polygonOptions);

	var path = new Array();
	for (var i=0;i<polygonPath.length;i++) {
		var lat = polygonPath[i].getLatitude();
		var lng = polygonPath[i].getLongitude();
		var latLng = new google.maps.LatLng(lat, lng);
		path.push(latLng);
	}
	myOptions.path = path;
	me.polygon_ = new google.maps.Polygon(myOptions);	
    me.overlay_obj_ = me.polygon_; //事件响应时用到
};

/**
 * 设置多边形的属性值
 * @param {Object} polygonOptions
 */
iscreate.maps.google.Polygon.prototype.setOptions = function(polygonOptions){
	var me = this;
	var myOptions = iscreate.maps.google.tools.toGooglePolygonOptions(polygonOptions);
	me.polygon_.setOptions(myOptions);
};

/**
 * 获取当前多边形的路径(有序的经纬度对象序列)
 */
iscreate.maps.google.Polygon.prototype.getPath = function(){
	var me = this;
	var googlePath = me.polygon_.getPath();
	var resultArray = new Array();  //存放经纬度对象的有序序列
	if(googlePath!=undefined&&googlePath!=null&&googlePath!=""){
		for(var i=0;i<googlePath.getLength();i++){
			var googleLatLng =googlePath.getAt(i);
			var iscreateLatLng = iscreate.maps.google.tools.toIscreateLatLng(googleLatLng);
			resultArray.push(iscreateLatLng);
		}
	}
	return resultArray;
};

/**
 * 设置当前多边形的路径(有序的经纬度对象序列)
 * @param {Object} path
 */
iscreate.maps.google.Polygon.prototype.setPath = function(path){
	var me = this;
	if(path==undefined||path==null||path==""){
		return;
	}
	var googlePath = new Array();
	for(var i=0;i<path.length;i++){
		var iscreateLatLng = path[i];
		var googleLatLng = iscreate.maps.google.tools.toGoogleLatLng(iscreateLatLng);
		googlePath.push(googleLatLng);
	}
	me.polygon_.setPath(googlePath);
};

/**
 * 设置多边形是否可见
 * @param {Object} visible
 */
iscreate.maps.google.Polygon.prototype.setVisible = function(visible){
	var me = this;
	me.polygon_.setVisible(visible);
}
/**
 * 获取多边形是否可见
 */
iscreate.maps.google.Polygon.prototype.getVisible = function(){
	var me = this;
	return me.polygon_.getVisible();
}

/**
 * 设置地图对象
 * @param {Object} latLng
 */
iscreate.maps.google.Polygon.prototype.setMap = function(map){
	var me = this;
	me.belong_map_ = map;
	if(map==null){
		me.polygon_.setMap(null);
	}else{
	    me.polygon_.setMap(map.map_);
	}
}

/**
 * 获取地图对象
 * @param {Object} latLng
 */
iscreate.maps.google.Polygon.prototype.getMap = function(){
	var me = this;
	return me.belong_map_ ;
}


/**
 * 弹出信息窗口
 * @param {Object} content
 * @param {Object} iInfoWindowOptions
 */
iscreate.maps.google.InfoWindow = function(content,iInfoWindowOptions){
	var me = this;
	var myOptions = {};
	myOptions.content = content;
	var opts_ = iInfoWindowOptions;
	if(opts_){
		if(opts_.content){
			myOptions.content = opts_.content;
		}
		if(opts_.position){
			var iscreateLatLng =opts_.position;
			var googleLatLng = iscreate.maps.google.tools.toGoogleLatLng(iscreateLatLng);
			myOptions.position = googleLatLng;
		}
	}
	me.infoWindow_ = new google.maps.InfoWindow(myOptions);
	me.overlay_obj_ = me.infoWindow_;	
};
/**
 * 打开信息窗口
 * @param {Object} map
 * @memberOf {TypeName} 
 */
iscreate.maps.google.InfoWindow.prototype.open = function(map){
	var me = this;
	me.infoWindow_.open(map.map_);
};
/**
 * 关闭信息窗口
 * @param {Object} map
 * @memberOf {TypeName} 
 */
iscreate.maps.google.InfoWindow.prototype.close = function(){
	var me = this;
	me.infoWindow_.close();
};

/**
 * 修改信息窗口的位置
 * @param {Object} latLng
 * @memberOf {TypeName} 
 */
iscreate.maps.google.InfoWindow.prototype.setPosition = function(latLng){
	var me = this;
	var googleLatLng = iscreate.maps.google.tools.toGoogleLatLng(latLng);
	me.infoWindow_.setPosition(googleLatLng);
};

/**
 * 获取信息窗口的位置
 * @memberOf {TypeName} 
 * @return {TypeName} 
 */
iscreate.maps.google.InfoWindow.prototype.getPosition = function(){
	var me = this;
	var googleLatLng =  me.infoWindow_.getPosition();
	var iLatLng = iscreate.maps.google.tools.toIscreateLatLng(googleLatLng);
	return iLatLng;
};

/**
 * 修改信息窗口的内容
 * @param {Object} latLng
 * @memberOf {TypeName} 
 */
iscreate.maps.google.InfoWindow.prototype.setContent = function(content){
	var me = this;
	me.infoWindow_.setContent(content);
};



/**
 * 矩形类
 * @param {Object} sw
 * @param {Object} ne
 * @param {Object} rectangleOptions
 */
iscreate.maps.google.Rectangle = function(latLngBounds ,rectangleOptions){
	var me = this;
	var googleBounds = iscreate.maps.google.tools.toGoogleBounds(latLngBounds);
	var myOptions = iscreate.maps.google.tools.toGoogleRectangleOptions(rectangleOptions);
    myOptions.bounds = googleBounds;
	me.rectangle_ = new google.maps.Rectangle(myOptions);	
    me.overlay_obj_ = me.rectangle_; //事件响应时用到
};

/**
 * 设置矩形的属性值
 * @param {Object} rectangleOptions
 */
iscreate.maps.google.Rectangle.prototype.setOptions = function(rectangleOptions){
    var me = this;
    var myOptinos = iscreate.maps.google.tools.toGoogleRectangleOptions(rectangleOptions);
    me.rectangle_.setOptions(myOptinos);
};

/**
 * 设置矩形的经纬度范围
 * @param {Object} latLngBounds
 */
iscreate.maps.google.Rectangle.prototype.setBounds = function(latLngBounds){
	var me = this;
	var googleBounds = iscreate.maps.google.tools.toGoogleBounds(latLngBounds);
	me.rectangle_.setBounds(googleBounds);
};

/**
 * 获取当前矩形的经纬度范围
 */
iscreate.maps.google.Rectangle.prototype.getBounds = function(){
	var me = this;
	var googleBounds = me.rectangle_.getBounds();
    var iscreateBounds = iscreate.maps.google.tools.toIscreateBounds(googleBounds);
    return iscreateBounds;
};

/**
 * 设置矩形是否可见
 * @param {Object} visible
 */
iscreate.maps.google.Rectangle.prototype.setVisible = function(visible){
	var me = this;
	me.rectangle_.setVisible(visible);
}
/**
 * 获取矩形是否可见
 */
iscreate.maps.google.Rectangle.prototype.getVisible = function(){
	var me = this;
	return me.rectangle_.getVisible();
}

/**
 * 设置地图对象
 * @param {Object} latLng
 */
iscreate.maps.google.Rectangle.prototype.setMap = function(map){
	var me = this;
	me.belong_map_ = map;
	if(map==null){
		me.rectangle_.setMap(null);
	}else{
	    me.rectangle_.setMap(map.map_);
	}
}

/**
 * 获取地图对象
 * @param {Object} latLng
 */
iscreate.maps.google.Rectangle.prototype.getMap = function(){
	var me = this;
	return me.belong_map_ ;
}

/**
 * 圆类
 * @param {LatLng} center
 * @param {double} radius
 * @param {circleOptions} circleOptions
 */
iscreate.maps.google.Circle = function(center, radius, circleOptions){
	var me = this;
	var myOptions = iscreate.maps.google.tools.toGoogleCircleOptions(circleOptions);
	var googleCenter = iscreate.maps.google.tools.toGoogleLatLng(center);
	myOptions.center = googleCenter;
	myOptions.radius = radius;
	me.circle_ = new google.maps.Circle(myOptions);
	me.overlay_obj_ = me.circle_; //事件响应时用到
};
/**
 * 设置圆的属性值
 * @param {Object} circleOptions
 */
iscreate.maps.google.Circle.prototype.setOptions = function(circleOptions){
	var me = this;
	var myOptions = iscreate.maps.google.tools.toGoogleCircleOptions(circleOptions);
	me.circle_.setOptions(myOptions);
};

/**
 * 获取圆形中心的经纬度对象
 */
iscreate.maps.google.Circle.prototype.getCenter = function(){
	var me = this;
    var googleCenter = me.circle_.getCenter();
    var iscreateCenter = iscreate.maps.google.tools.toIscreateLatLng(googleCenter);
    return iscreateCenter;
};

/**
 * 获取圆的半径(单位为米)
 */
iscreate.maps.google.Circle.prototype.getRadius = function(){
	var me = this;
	var radius = me.circle_.getRadius();
	return radius;
};

/**
 * 设置地图的中心位置
 * @param {Object} center
 */
iscreate.maps.google.Circle.prototype.setCenter = function(center){
	var me = this;
	var googleCenter = iscreate.maps.google.tools.toGoogleLatLng(center);
	me.circle_.setCenter(googleCenter);
};

/**
 * 设置圆的半径
 * @param {Object} radius
 */
iscreate.maps.google.Circle.prototype.setRadius = function(radius){
	var me = this;
	me.circle_.setRadius(radius);
};

/**
 * 设置圆是否可见
 * @param {Object} visible
 */
iscreate.maps.google.Circle.prototype.setVisible = function(visible){
	var me = this;
	me.circle_.setVisible(visible);
}
/**
 * 获取圆是否可见
 */
iscreate.maps.google.Circle.prototype.getVisible = function(){
	var me = this;
	return me.circle_.getVisible();
}

/**
 * 设置地图对象
 * @param {Object} latLng
 */
iscreate.maps.google.Circle.prototype.setMap = function(map){
	var me = this;
	me.belong_map_ = map;
	if(map==null){
		me.circle_.setMap(null);
	}else{
	    me.circle_.setMap(map.map_);
	}
}

/**
 * 获取地图对象
 * @param {Object} latLng
 */
iscreate.maps.google.Circle.prototype.getMap = function(){
	var me = this;
	return me.belong_map_ ;
}

/******************************事件包*********************************/

/**
 * 添加监听器
 * @param {Object} obj
 * @param {Object} enentName
 * @param {Object} callback
 */
iscreate.maps.google.event.addListener = function(obj, eventName, callback) {
	var me = this;
	me.handle_ = null; //事件句柄，用于删除当前绑定的事件
	//地图事件的处理
	if (obj instanceof iscreate.maps.google.Map) {
		if (eventName == "bounds_changed" || eventName == "center_changed"
				|| eventName == "zoom_changed") {
			me.handle_ = google.maps.event.addListener(obj.map_, eventName,
					callback);
		} else if (eventName == "click" || eventName == "dblclick"
				|| eventName == "mousemove" || eventName == "mouseout"
				|| eventName == "mouseover" || eventName == "rightclick") {
			me.handle_ = google.maps.event.addListener(obj.map_, eventName,
					function(mouseEvent) {
						var googleLatLng = mouseEvent.latLng;
						var iscreateLatLng = iscreate.maps.google.tools.toIscreateLatLng(googleLatLng);
						callback( {
							"latLng" : iscreateLatLng
						});
					});
		}
	} else if (obj instanceof iscreate.maps.google.Marker) { //标记事件的处理
		if (eventName == "click" || eventName == "dblclick"
				|| eventName == "mousedown" || eventName == "mouseout"
				|| eventName == "mouseover" || eventName == "mouseup"
				|| eventName == "rightclick" || eventName == "drag"
				|| eventName == "dragend" || eventName == "dragstart") {
			me.handle_ = google.maps.event.addListener(obj.overlay_obj_,
					eventName, function(mouseEvent) {
						var googleLatLng = mouseEvent.latLng;
						var iscreateLatLng = iscreate.maps.google.tools
								.toIscreateLatLng(googleLatLng);
						callback( {
							"latLng" : iscreateLatLng
						});
					});
		}
	} else if (obj instanceof iscreate.maps.google.Polyline //折线事件的处理
			|| obj instanceof iscreate.maps.google.Polygon //多边形事件的处理
			|| obj instanceof iscreate.maps.google.Rectangle //矩形事件的处理
			|| obj instanceof iscreate.maps.google.Circle //矩形事件的处理
	) {
		if (eventName == "click" || eventName == "dblclick"
				|| eventName == "mousedown" || eventName == "mousemove"
				|| eventName == "mouseout" || eventName == "mouseover"
				|| eventName == "mouseup" || eventName == "rightclick") {
			me.handle_ = google.maps.event.addListener(obj.overlay_obj_, eventName,
					function(mouseEvent) {
						var googleLatLng = mouseEvent.latLng;
						var iscreateLatLng = iscreate.maps.google.tools
								.toIscreateLatLng(googleLatLng);
						callback({
							"latLng" : iscreateLatLng
						});
					});
		}
	}
	return me;
};

/**
 * 删除监听器
 * @param {Object} listener
 */
iscreate.maps.google.event.removeListener = function(listener){
	var me = this;
	google.maps.event.removeListener(me.handle_);
};

/**
 * 触发一个监听器
 * @param {Object} obj
 * @param {Object} enentName
 * @param {Object} opts
 */
iscreate.maps.google.event.trigger= function(obj,eventName,opts){
	var me = this;
	var googleObj = null;
	if (obj instanceof iscreate.maps.google.Map) {
		googleObj= obj.map_;
	}else{
		googleObj=obj.overlay_obj_;
	}
	if(opts&&opts.latLng){   //这里需要加入判断latLng变量类型是不是经纬度对象
		var googleLatLng = iscreate.maps.google.tools.toGoogleLatLng(opts.latLng)
		opts.latLng = googleLatLng;
		google.maps.event.trigger(googleObj, eventName, opts);
	}else{
		google.maps.event.trigger(googleObj, eventName, opts);
	}
};

/********************************将iscreate定义的对象设计为转换为**************************************/
/**
 * 将iscreate定义的LatLng转换为google api的LatLng对象
 * @param {Object} iscreateLatLng
 */
iscreate.maps.google.tools.toGoogleLatLng = function(iscreateLatLng){
    var lat = iscreateLatLng.getLatitude();
    var lng = iscreateLatLng.getLongitude();
	var googleLatLng = new google.maps.LatLng(lat,lng);
	return googleLatLng;
}
/**
 * google的经纬度对象转换为iscreate的经纬度对象
 * @param {Object} googleLatLng
 * @return {TypeName} 
 */
iscreate.maps.google.tools.toIscreateLatLng = function(googleLatLng){
	var lat = googleLatLng.lat();
    var lng = googleLatLng.lng();
	var iscreateLatLng = new iscreate.maps.base.LatLng(lat,lng);
	return iscreateLatLng;
}
/**
 * 将iscreate的LatLngBounds对象转换为google的LatLngBounds对象
 * @param {Object} iscreateBounds
 * @return {TypeName} 
 */
iscreate.maps.google.tools.toGoogleBounds = function(iscreateBounds){
	var iscreateSw = iscreateBounds.getSw();
	var iscreateNe = iscreateBounds.getNe();
	var googleSw = iscreate.maps.google.tools.toGoogleLatLng(iscreateSw);
	var googleNe = iscreate.maps.google.tools.toGoogleLatLng(iscreateNe);
	var googleBounds = new google.maps.LatLngBounds(googleSw,googleNe);
	return googleBounds;
}
/**
 * 将google的LatLngBounds对象转换为iscreate的LatLngBounds对象
 * @param {Object} googleBounds
 * @return {TypeName} 
 */
iscreate.maps.google.tools.toIscreateBounds = function(googleBounds){
	var googleSw = googleBounds.getSouthWest();
	var googleNe = googleBounds.getNorthEast();
	var iscreateSw = iscreate.maps.google.tools.toIscreateLatLng(googleSw);
    var iscreateNe = iscreate.maps.google.tools.toIscreateLatLng(googleNe);
	var iscreateBounds = new iscreate.maps.base.LatLngBounds(iscreateSw,iscreateNe);
	return iscreateBounds;
}

/**
 * 将iscreate的Point对象转换为google的Point对象
 * @param {Object} iscreatePoint
 * @return {TypeName} 
 */
iscreate.maps.google.tools.toGooglePoint = function(iscreatePoint){
	var iscreateX = iscreatePoint.getX();
	var iscreateY = iscreatePoint.getY();
	var googlePoint = new google.maps.Point(iscreateX,iscreateY);
	return googlePoint;
}
/**
 * 将google的Point对象转换为iscreate的Point对象
 * @param {Object} googlePoint
 * @return {TypeName} 
 */
iscreate.maps.google.tools.toIscreatePoint = function(googlePoint){
	var googleX = googlePoint.x;
	var googleY = googlePoint.y;
	var iscreatePoint = new iscreate.maps.base.Point(googleX,googleY);
	return iscreatePoint;
}
/**
 * 将google的Size对象转换为iscreate的Size对象
 * @param {Object} googleSize
 * @return {TypeName} 
 */
iscreate.maps.google.tools.toGoogleSize = function(googleSize){
	var iscreateW = iscreatePoint.getWidth();
	var iscreateH = iscreatePoint.getHeight();
	var googleSize = new google.maps.Size(iscreateW,iscreateH);
	return googleSize;
}
/**
 * 将iscreate的Size对象转换为google的Size对象
 * @param {Object} iscreateSize
 * @return {TypeName} 
 */
iscreate.maps.google.tools.toIscreateSize = function(iscreateSize){
	var iscreateW = iscreateSize.width;
	var iscreateH = iscreateSize.height;
	var iscreateSize = new iscreate.maps.base.Size(iscreateW,iscreateH);
	return iscreateSize;
}


/**
 * 将iscreate的Map对象参数转换为google的Map对象参数
 * @param {Object} iscreateMapOptions
 */
iscreate.maps.google.tools.toGoogleMapOptions = function(iscreateMapOptions){
	var opts_ = iscreateMapOptions;
	var myOptions = {};

	if(opts_){
		if(("zoom" in opts_)&&opts_.zoom){
			myOptions.zoom = opts_.zoom;
		}
		if(("center" in opts_)&&opts_.center){
			myOptions.center = new google.maps.LatLng(opts_.center.getLatitude(),opts_.center.getLongitude());
		}
	}
	return myOptions;
}

/**
 * 将iscreate的Marker对象参数转换为google的Marker对象参数
 * @param {Object} iscreateMarkerOptions
 */
iscreate.maps.google.tools.toGoogleMarkerOptions = function(iscreateMarkerOptions){
	var opts_ = iscreateMarkerOptions;
	var myOptions = {};
	if(opts_){
		if(("map" in opts_)){
			if(opts_.map){
			    myOptions.map = opts_.map.map_;
			}else if(opts_.map==null){
				myOptions.map = null;
			}
		}
		if(("draggable" in opts_)){ //这个是布尔型
			myOptions.draggable = opts_.draggable;
		}
		if(("icon" in opts_)&&opts_.icon){
			myOptions.icon = opts_.icon;
		};
		if(("position" in opts_)&&opts_.position){
			myOptions.position = iscreate.maps.google.tools.toGoogleLatLng(opts_.position);
		}
		if(("shadow" in opts_)&&opts_.shadow){
			myOptions.shadow = opts_.shadow;
		};
		if(("title" in opts_)&&opts_.title){
			myOptions.title = opts_.title;
		}
		if("visible" in opts_){
			myOptions.visible = opts_.visible;
		}
	}
	return myOptions;
}

/**
 * 将iscreate的Polyline对象参数转换为google的Polyline对象参数
 * @param {Object} iscreatePolylineOptions
 */
iscreate.maps.google.tools.toGooglePolylineOptions = function(iscreatePolylineOptions){
	var opts_ = iscreatePolylineOptions;
	var myOptions = {};
	if(opts_){
		if(("map" in opts_)){
			if(opts_.map){
			    myOptions.map = opts_.map.map_;
			}else if(opts_.map==null){
				myOptions.map = null;
			}
		}
		if(("path" in opts_) && opts_.path){
			var googlePath = new Array();
			var iscreatePath = opts_.path;
			for(var i=0;i<iscreatePath.length;i++){
				var iLatLng = iscreatePath[i];
				var gLatLng = iscreate.maps.google.tools.toGoogleLatLng(iLatLng);
				googlePath.push(gLatLng);
			}
			myOptions.path = googlePath;
		}
		if(("strokeColor" in opts_)&&opts_.strokeColor){
			myOptions.strokeColor = opts_.strokeColor;
		}
		if(("strokeWeight" in opts_)&&opts_.strokeWeight){
			myOptions.strokeWeight = opts_.strokeWeight;
		}
	}
	return myOptions;
}

/**
 * 将iscreate的Polygon对象参数转换为google的Polygon对象参数
 * @param {Object} iscreatePolygonOptions
 */
iscreate.maps.google.tools.toGooglePolygonOptions = function(iscreatePolygonOptions){
	var opts_ = iscreatePolygonOptions;
	var myOptions = {};
	if(opts_){
		if(("map" in opts_)){
			if(opts_.map){
			    myOptions.map = opts_.map.map_;
			}else if(opts_.map==null){
				myOptions.map = null;
			}
		}
		if(("fillColor" in opts_) && opts_.fillColor){
			myOptions.fillColor = opts_.fillColor;
		}
		if(("path" in opts_) && opts_.path){
			var googlePath = new Array();
			var iscreatePath = opts_.path;
			for(var i=0;i<iscreatePath.length;i++){
				var iLatLng = iscreatePath[i];
				var gLatLng = iscreate.maps.google.tools.toGoogleLatLng(iLatLng);
				googlePath.push(gLatLng);
			}
			myOptions.paths = googlePath;
		}
		if(("strokeColor" in opts_)&&opts_.strokeColor){
			myOptions.strokeColor = opts_.strokeColor;
		}
		if(("strokeWeight" in opts_)&&opts_.strokeWeight){
			myOptions.strokeWeight = opts_.strokeWeight;
		}
	}
	return myOptions;
}

/**
 * 将iscreate的Rectangle对象参数转换为google的Rectangle对象参数
 * @param {Object} iscreateRectangleOptions
 */
iscreate.maps.google.tools.toGoogleRectangleOptions = function(iscreateRectangleOptions){
	var opts_ = iscreateRectangleOptions;
	var myOptions = {};
	if(opts_){
		if(("map" in opts_)){
			if(opts_.map){
			    myOptions.map = opts_.map.map_;
			}else if(opts_.map==null){
				myOptions.map = null;
			}
		}
		if(("bounds" in opts_) && opts_.bounds){
            var iscreateBounds = opts_.bounds;
            var googleBounds = iscreate.maps.google.tools.toGoogleBounds(iscreateBounds);
            myOptions.bounds = googleBounds;
		}
		if(("fillColor" in opts_) && opts_.fillColor){
			myOptions.fillColor = opts_.fillColor;
		}
		if(("strokeColor" in opts_)&&opts_.strokeColor){
			myOptions.strokeColor = opts_.strokeColor;
		}
		if(("strokeWeight" in opts_)&&opts_.strokeWeight){
			myOptions.strokeWeight = opts_.strokeWeight;
		}
	}
	return myOptions;
}

/**
 * 将iscreate的Circle对象参数转换为google的Circle对象参数
 * @param {Object} iscreateCircleOptions
 */
iscreate.maps.google.tools.toGoogleCircleOptions = function(iscreateCircleOptions){
	var opts_ = iscreateCircleOptions;
	var myOptions = {};
	if(opts_){
		if(("map" in opts_)){
			if(opts_.map){
			    myOptions.map = opts_.map.map_;
			}else if(opts_.map==null){
				myOptions.map = null;
			}
		}
		if(("center" in opts_) && opts_.center){
            var iscreateLatLng = opts_.center;
            var googleLatLng = iscreate.maps.google.tools.toGoogleLatLng(iscreateLatLng);
            myOptions.center = googleLatLng;
		}
		if(("radius" in opts_)&&opts_.radius){
			myOptions.radius = opts_.radius;
		}
		if(("fillColor" in opts_) && opts_.fillColor){
			myOptions.fillColor = opts_.fillColor;
		}
		if(("strokeColor" in opts_)&&opts_.strokeColor){
			myOptions.strokeColor = opts_.strokeColor;
		}
		if(("strokeWeight" in opts_)&&opts_.strokeWeight){
			myOptions.strokeWeight = opts_.strokeWeight;
		}
	}
	return myOptions;	
}

/**
 * 将GPS坐标转换成地图上对应的坐标
 * @param {Object} latLng
 */
iscreate.maps.comm.gpsToMapLatLng=function(latLng){
	var cList = null;
	var lat = latLng.getLatitude();
	var lng = latLng.getLongitude();
	var obj = {};

	if (iscreate.maps.comm.correctionList_ != null) {
		cList = iscreate.maps.comm.correctionList_;
		var n1 = cList[0].accurateLat - lat;
		var n2 = cList[0].accurateLng - lng;
		var s1 = Math.pow(n1, 2) + Math.pow(n2, 2);
		var fitIndex = 0; //最近的校正点索引，初始值为0 
		var minSum = s1; //最小坐标距离 
		for (var i in cList) {
			var num1 = cList[i].accurateLat - lat;
			var num2 = cList[i].accurateLng - lng;
			var sum = Math.pow(num1, 2) + Math.pow(num2, 2);
			if (minSum > sum) {
				fitIndex = i;
				minSum = sum;
			}
		}
		obj.latitude = lat + parseFloat(cList[fitIndex].offsetLat);
		obj.longitude = lng + parseFloat(cList[fitIndex].offsetLng);
	} else {
		obj.latitude = parseFloat(lat);
		obj.longitude = parseFloat(lng);
	}
	return new iscreate.maps.base.LatLng(obj.latitude,obj.longitude);
}
/**
 * 将地图上的坐标转换成GPS坐标(有误差)
 * @param {Object} latLng
 */
iscreate.maps.comm.mapToGpsLatLng = function(latLng){
	var me = this;
	var cList = null;
	var lat = latLng.getLatitude();
	var lng = latLng.getLongitude();
	var obj = {};
	if (iscreate.maps.comm.correctionList_ != null) {
		cList = iscreate.maps.comm.correctionList_;
		var n1 = cList[0].accurateLat - lat;
		var n2 = cList[0].accurateLng - lng;
		var s1 = Math.pow(n1, 2) + Math.pow(n2, 2);
		var fitIndex = 0; //最近的校正点索引，初始值为0 
		var minSum = s1; //最小坐标距离 
		for (var i in cList) {
			var num1 = cList[i].accurateLat - lat;
			var num2 = cList[i].accurateLng - lng;
			var sum = Math.pow(num1, 2) + Math.pow(num2, 2);
			if (minSum > sum) {
				fitIndex = i;
				minSum = sum;
			}
		}
		obj.latitude = lat - parseFloat(cList[fitIndex].offsetLat);
		obj.longitude = lng - parseFloat(cList[fitIndex].offsetLng);
	} else {
		obj.latitude = parseFloat(latitude);
		obj.longitude = parseFloat(longitude);
	}
	return new iscreate.maps.base.LatLng(obj.latitude,obj.longitude);
}


/*********************添加文字覆盖层的类和方法,以后可能会有改动************************/
/**
 * 将iscreate的TextOverlay对象参数转换为google的TextOverlay对象参数
 * @param {Object} iscreateMarkerOptions
 */
iscreate.maps.google.tools.toGoogleTextOverlayOptions = function(
		iscreateTextOverlayOptions) {
	var opts_ = iscreateTextOverlayOptions;
	var myOptions = {};
	if (opts_) {
		if (("map" in opts_)) {
			if (opts_.map) {
				myOptions.map = opts_.map.map_;
			} else if (opts_.map == null) {
				myOptions.map = null;
			}
		}
		if (("position" in opts_) && opts_.position) {
			myOptions.position = iscreate.maps.google.tools
					.toGoogleLatLng(opts_.position);
		}
		if (("text" in opts_) && opts_.text) {
			myOptions.text = opts_.text;
		}
	}
	return myOptions;
};
var isFirst = true;
/**
 * 文字覆盖层
 * @param {Object} position
 * @param {Object} title
 * @param {Object} textOverlayOptions
 * @memberOf {TypeName} 
 */
iscreate.maps.google.TextOverlay = function(position, text, textOverlayOptions) {
	if(isFirst){
		initTextOverlay();
		isFirst = false;
	}
	var me = this;
	var myOptions = iscreate.maps.google.tools.toGoogleTextOverlayOptions(textOverlayOptions);
			
	var googleLatLng = iscreate.maps.google.tools.toGoogleLatLng(position);
	myOptions.position = googleLatLng;
	myOptions.text = text;
	me.textoverlay_ = new ClassForGoogle.TextOverlay(myOptions.position,
			myOptions.text, myOptions);
	me.overlay_obj_ = me.marker_; //事件响应的时候用到
};

/**
 * 设置文字覆盖层的属性值
 * @param {Object} textOverlayOptions
 * @memberOf {TypeName} 
 */
iscreate.maps.google.TextOverlay.prototype.setOptions = function(
		textOverlayOptions) {
	var me = this;
	var myOptions = iscreate.maps.google.tools
			.toGoogleTextOverlayOptions(textOverlayOptions);
	me.textoverlay_.setOptions(myOptions);
}

/**
 * 设置地图对象
 * @param {Object} map
 */
iscreate.maps.google.TextOverlay.prototype.setMap = function(map) {
	var me = this;
	me.belong_map_ = map;
	if (map == null) {
		me.textoverlay_.setMap(null);
		//me.textoverlay_.onRemove();
	} else {
		//me.textoverlay_.onAdd();
		me.textoverlay_.setMap(map.map_);
	}
}
/**
 * 获取地图对象
 * @param {Object} latLng
 */
iscreate.maps.google.TextOverlay.prototype.getMap = function() {
	var me = this;
	return me.belong_map_;
}

var ClassForGoogle = {};
/**
 * google自定义文字图层
 * @param {Object} point
 * @param {Object} text
 * @param {Object} map
 * @memberOf {TypeName} 
 */
ClassForGoogle.TextOverlay = function(point, text, textOverlayOptions) {
	var me = this;
//	me.prototype =  new google.maps.OverlayView();
	
	// 初始化参数：坐标、文字、地图
	this.point_ = point;
	this.text_ = text;
	// 到onAdd时才需要创建div
	this.div_ = null;
	var opts_ = textOverlayOptions;
	if (opts_) {
		if ("map" in opts_) {
			if (opts_.map) {
				//this.setMap(opts_.map);
			}
		}
	}
//	alert(ClassForGoogle.TextOverlay.prototype.toSource());  
}
function initTextOverlay(){
	ClassForGoogle.TextOverlay.prototype = new google.maps.OverlayView(); //将自定义对象的 prototype 设置为 google.maps.OverlayView() 的新实例
	ClassForGoogle.TextOverlay.prototype.onAdd = function() {
		// 创建一个div，其中包含了当前文字
		var div = document.createElement('DIV');
		div.name = "TextOverlay"; //样式
		div.style.borderStyle = "none"; //
		div.style.borderWidth = "0px"; //
		div.style.position = "absolute"; //绝对定位
		div.style.font_size = "9px";
		div.style.color = "blue";
	//	div.align = "left";
	//	var span = document.createElement("span");
	//	span.style.align = "left";
	//	span.style.font_size = "9px";
	//	span.style.color = "blue";
	//	var text = document.createTextNode(this.text_);
	//	span.appendChild(text);
	//	div.appendChild(span);
		
		div.innerHTML = this.text_;
		this.div_ = div;
		var panes = this.getPanes();
		//var panes = google.maps.MapPanes;
		panes.overlayImage.appendChild(div);
		//重画
		this.draw();
	}
	//
	ClassForGoogle.TextOverlay.prototype.draw = function() {
		// 利用projection获得当前视图的坐标
		var overlayProjection = this.getProjection();
		//var overlayProjection = google.maps.MapCanvasProjection;
		var center = overlayProjection.fromLatLngToDivPixel(this.point_);
		// 为简单，长宽是固定的，实际应该根据文字改变
		var div = this.div_;
		div.style.left = center.x - div.offsetWidth / 2 + 'px';
		div.style.top = (center.y-60) + 'px';
		
		//默认为10个字的宽度
		div.style.width = '10em';
		div.style.width = '200px';
		//div.style.height = '10px';
		div.align = "center";
	}
	//
	ClassForGoogle.TextOverlay.prototype.onRemove = function() {
		this.div_.parentNode.removeChild(this.div_);
		this.div_ = null;
	}
}

/*ClassForGoogle.TextOverlay.prototype.setMap = function(map){
	if(map==null){
		ClassForGoogle.TextOverlay.prototype.onRemove();
	}else{
		ClassForGoogle.TextOverlay.prototype.onAdd();
	}
}*/

/*************************以上是文字覆盖层的内容****************************/

/*********************添加文字覆盖层的类和方法,以后可能会有改动************************/
/**
 * 将iscreate的ImageOverlay对象参数转换为google的ImageOverlay对象参数
 * @param {Object} iscreateMarkerOptions
 */
iscreate.maps.google.tools.toGoogleImageOverlayOptions = function(
		iscreateImageOverlayOptions) {
	var opts_ = iscreateImageOverlayOptions;
	var myOptions = {};
	if (opts_) {
		if (("map" in opts_)) {
			if (opts_.map) {
				myOptions.map = opts_.map.map_;
			} else if (opts_.map == null) {
				myOptions.map = null;
			}
		}
		if (("position" in opts_) && opts_.position) {
			myOptions.position = iscreate.maps.google.tools
					.toGoogleLatLng(opts_.position);
		}
		if (("image" in opts_) && opts_.image) {
			myOptions.image = opts_.image;
		}
	}
	return myOptions;
};
var isImageFirst = true;
/**
 * 图片覆盖层
 * @param {Object} position
 * @param {Object} title
 * @param {Object} imageOverlayOptions
 * @memberOf {TypeName} 
 */
iscreate.maps.google.ImageOverlay = function(position, image, imageOverlayOptions) {
	if(isImageFirst){
		initImageOverlay();
		isImageFirst = false;
	}
	var me = this;
	var myOptions = iscreate.maps.google.tools.toGoogleImageOverlayOptions(imageOverlayOptions);
			
	var googleLatLng = iscreate.maps.google.tools.toGoogleLatLng(position);
	myOptions.position = googleLatLng;
	myOptions.image = image;
	me.imageoverlay_ = new ClassForGoogle2.ImageOverlay(myOptions.position,
			myOptions.image, myOptions);
	me.overlay_obj_ = me.marker_; //事件响应的时候用到
};

/**
 * 设置文字覆盖层的属性值
 * @param {Object} imageOverlayOptions
 * @memberOf {TypeName} 
 */
iscreate.maps.google.ImageOverlay.prototype.setOptions = function(
		imageOverlayOptions) {
	var me = this;
	var myOptions = iscreate.maps.google.tools
			.toGoogleImageOverlayOptions(imageOverlayOptions);
	me.imageoverlay_.setOptions(myOptions);
}

/**
 * 设置地图对象
 * @param {Object} map
 */
iscreate.maps.google.ImageOverlay.prototype.setMap = function(map) {
	var me = this;
	me.belong_map_ = map;
	if (map == null) {
		me.imageoverlay_.setMap(null);
	} else {
		me.imageoverlay_.setMap(map.map_);
	}
}
/**
 * 获取地图对象
 * @param {Object} latLng
 */
iscreate.maps.google.ImageOverlay.prototype.getMap = function() {
	var me = this;
	return me.belong_map_;
}

var ClassForGoogle2 = {};
/**
 * google自定义文字图层
 * @param {Object} point
 * @param {Object} image
 * @param {Object} map
 * @memberOf {TypeName} 
 */
ClassForGoogle2.ImageOverlay = function(point, image, imageOverlayOptions) {
	var me = this;
//	me.prototype =  new google.maps.OverlayView();
	
	// 初始化参数：坐标、文字、地图
	this.point_ = point;
	this.image_ = image;
	// 到onAdd时才需要创建div
	this.div_ = null;
	var opts_ = imageOverlayOptions;
	if (opts_) {
		if ("map" in opts_) {
			if (opts_.map) {
				this.setMap(opts_.map);
			}
		}
	}
}
function initImageOverlay(){
	ClassForGoogle2.ImageOverlay.prototype = new google.maps.OverlayView(); //将自定义对象的 prototype 设置为 google.maps.OverlayView() 的新实例
	ClassForGoogle2.ImageOverlay.prototype.onAdd = function() {
		// 创建一个div，其中包含了当前文字
		var div = document.createElement('DIV');
		div.name = "ImageOverlay"; //样式
		div.style.borderStyle = "none"; //
		div.style.borderWidth = "0px"; //
		div.style.position = "absolute"; //绝对定位
		div.style.font_size = "9px";
		div.style.color = "blue";
	//	div.align = "left";
	//	var span = document.createElement("span");
	//	span.style.align = "left";
	//	span.style.font_size = "9px";
	//	span.style.color = "blue";
	//	var text = document.createTextNode(this.text_);
	//	span.appendChild(text);
	//	div.appendChild(span);
		
		div.innerHTML = this.image_;
		this.div_ = div;
		var panes = this.getPanes();
		panes.overlayImage.appendChild(div);
		//重画
		this.draw();
	}
	//
	ClassForGoogle2.ImageOverlay.prototype.draw = function() {
		// 利用projection获得当前视图的坐标
		var overlayProjection = this.getProjection();
		var center = overlayProjection.fromLatLngToDivPixel(this.point_);
		// 为简单，长宽是固定的，实际应该根据文字改变
		var div = this.div_;
		div.style.left = center.x - div.offsetWidth / 2 + 'px';
		div.style.top = (center.y+5) + 'px';
		
		//默认为10个字的宽度
		div.style.width = '10em';
		div.style.width = '200px';
		//div.style.height = '10px';
		div.align = "center";
	}
	//
	ClassForGoogle2.ImageOverlay.prototype.onRemove = function() {
		this.div_.parentNode.removeChild(this.div_);
		this.div_ = null;
	}
}

/*************************以上是文字覆盖层的内容****************************/




/******************对象名称引用***********************/
var ILatLng = iscreate.maps.base.LatLng;
var ILatLngBounds = iscreate.maps.base.LatLngBounds;
var ISize =iscreate.maps.base.Size;
var IPoint = iscreate.maps.base.Point;
var IMap = iscreate.maps.google.Map;
var IMarker = iscreate.maps.google.Marker;
var ITextOverlay = iscreate.maps.google.TextOverlay;
var IPolyline = iscreate.maps.google.Polyline;
var IPolygon = iscreate.maps.google.Polygon;
var IInfoWindow = iscreate.maps.google.InfoWindow;
var ICircle = iscreate.maps.google.Circle;
var IRectangle = iscreate.maps.google.Rectangle;
var IMapEvent=iscreate.maps.google.event;
var IMapComm = iscreate.maps.comm;
var IImageOverlay = iscreate.maps.google.ImageOverlay;