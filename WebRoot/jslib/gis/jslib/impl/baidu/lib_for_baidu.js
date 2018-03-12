/***************************地图对象包****************************/
/**
 * 地图类
 * @param {Object} canvas_id
 * @param {Object} latitude
 * @param {Object} longitude
 * @param {Object} zoom
 * @param {Object} mapOptions
 */
iscreate.maps.baidu.Map = function(canvas_id, center, zoom,mapOptions){
	var me = this;
	me.map_ = null;
	var myOptions = iscreate.maps.baidu.tools.toBaiduMapOptions(mapOptions)
	var baiduCenter = iscreate.maps.baidu.tools.toBaiduLatLng(center);
	myOptions.center = baiduCenter;
	myOptions.zoom = zoom;
	me.map_ = new BMap.Map(canvas_id);
	me.map_.centerAndZoom(baiduCenter,zoom);
	me.map_.addControl(new BMap.NavigationControl());  //缩放级别控件
	me.map_.addControl(new BMap.ScaleControl());  	   //地图比例尺控件
	me.map_.addControl(new BMap.MapTypeControl());     //地图类型控件
	me.map_.enableScrollWheelZoom();				   //启用滚轮缩放地图大小
	//生成地图控件
	canvas_id = "#"+canvas_id;
	var div="<div class='map_choose' style='box-shadow: 2px 2px 4px #333333;cursor: pointer;margin-top: 10px;position: absolute;right: 130px;top: -3px;z-index: 1;'>";
	div+="<a class='map_choose_a' style=' display:inline-block;border:1px solid #aaa; background:#eee; padding:4px 6px 4px 20px; line-height:18px;' href='#'>地图选择 ▼</a>";
	div+="<ul class='map_choose_ul' style='display:none;'>";
	if(mapSetting.apiType == 'BaiduMap'){
		div+="<li style=' background:#fff; border:1px solid #ccc; border-top:none; padding:4px; line-height:24px; text-align:center; cursor:pointer;'><input type='radio' value='BaiduMap' name='mapType' checked='checked' style='position:relative; top:3px; left:-3px;'/><img src='"+rootPath_+"/images/baidu_map_ico.png' /></li>";
//		div+="<li style=' background:#fff; border:1px solid #ccc; border-top:none; padding:4px; line-height:24px; text-align:center; cursor:pointer;'><input type='radio' value='GoogleMap' name='mapType' style='position:relative; top:3px; left:-3px;'/><img src='"+rootPath_+"/images/google_map_ico.png' /></li>";
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
 * 
 */


/**
 * 修改地图属性
 * @param {MapOptions} mapOptions
 */
iscreate.maps.baidu.Map.prototype.setOptions=function(mapOptions){
	var me = this;
	var myOptions = iscreate.maps.baidu.tools.toBaiduMapOptions(mapOptions);
	//---暂时只能修改地图中心和缩放级别，如果想能够修改其他属性，请在此添加
	me.map_.setCenter(myOptions.center);
	me.map_.setZoom(myOptions.zoom);
};

/**
 * 增加地图弹窗
 */

var infowindows;

iscreate.maps.baidu.Map.prototype.openWindow = function(content,opts,point)
{
	var me = this;
	infowindows = new BMap.InfoWindow(content,opts);
	
	me.map_.openInfoWindow(infowindows,point);
}


iscreate.maps.baidu.Map.prototype.setContent = function(content)
{
	var me = this;
	infowindows.setContent(content);
}


/**
 * 关闭地图弹窗
 */

iscreate.maps.baidu.Map.prototype.closeInfoWindow = function()
{
	var me = this;
	
	me.map_.closeInfoWindow();
}


iscreate.maps.baidu.Map.prototype.clickClose = function(isstop,status)
{
	var me = this;
	infowindows.addEventListener('clickclose', function() 
	{
		if(isstop==true && status==true)
		{
			start();
		}
	});
}



/**
 * 鼠标右键增加菜单
 */

iscreate.maps.baidu.Map.prototype.addContextMenu = function()
{
	var me = this;
	var menu = new BMap.ContextMenu();
	var txtMenuItem = [
	  {
	   text:'放大',
	   callback:function(){me.map_.zoomIn()}
	  },
	  {
	   text:'缩小',
	   callback:function(){me.map_.zoomOut()}
	  },
	  {
	   text:'放置到最大级',
	   callback:function(){me.map_.setZoom(18)}
	  },
	  {
	   text:'查看全国',
	   callback:function(){me.map_.setZoom(4)}
	  }
	 ];
	
	
	 for(var i=0; i < txtMenuItem.length; i++){
	  menu.addItem(new BMap.MenuItem(txtMenuItem[i].text,txtMenuItem[i].callback,100));
	  if(i==1){
	   menu.addSeparator();
	  }
	 }
	 me.map_.addContextMenu(menu);
}



/**
 * 返回当前视口的纬度/经度范围
 */
iscreate.maps.baidu.Map.prototype.getBounds = function(){
	var me = this;
	var gBounds = me.map_.getBounds();
	if(!gBounds)return null;
	var iSw = iscreate.maps.baidu.tools.toIscreateLatLng(gBounds.getSouthWest());
	var iNe = iscreate.maps.baidu.tools.toIscreateLatLng(gBounds.getNorthEast());
	var bounds = new iscreate.maps.base.LatLngBounds(iSw,iNe);
	return bounds;
};

/**
 * 获取地图中心坐标
 */
iscreate.maps.baidu.Map.prototype.getCenter = function(){
	var me = this;
	var baiduCenter = me.map_.getCenter();
	var iscreateCenter = iscreate.maps.baidu.tools.toIscreateLatLng(baiduCenter);
	return iscreateCenter;
};

/**
 * 获取地图当前的缩放级别
 */
iscreate.maps.baidu.Map.prototype.getZoom = function(){
	var me = this;
	var zoom = me.map_.getZoom();
	return zoom;
};

/**
 * 将地图中心移动一段指定的距离（以像素为单位）
 * @param {Object} x
 * @param {Object} y
 */
iscreate.maps.baidu.Map.prototype.panBy = function(x, y){
	var me = this;
	me.map_.panBy(x,y);
};

/**
 * 将地图中心更改为指定的LatLng
 * @param {Object} latLng
 */
iscreate.maps.baidu.Map.prototype.panTo = function(latLng){
	var me = this;
	var baiduLatLng = iscreate.maps.baidu.tools.toBaiduLatLng(latLng);
	me.map_.panTo(baiduLatLng);
};

/**
 * 设置地图的缩放级别
 * @param {Object} zoom
 */
iscreate.maps.baidu.Map.prototype.setZoom = function(zoom){
	var me = this;
	me.map_.setZoom(zoom);
};

/**
 * 设置地图中心点位置
 * @param {Object} latLng
 */
iscreate.maps.baidu.Map.prototype.setCenter = function(latLng){
	var me = this;
	var baiduLatLng = iscreate.maps.baidu.tools.toBaiduLatLng(latLng);
	me.map_.setCenter(baiduLatLng);
};
/**
 * 添加叠加层
 * @param {Object} overlay
 */
iscreate.maps.baidu.Map.prototype.addOverlay = function(overlay){
	var me = this;
	if(overlay){
		overlay.setMap(me);
	}
};
/**
 * 删除叠加层
 * @param {Object} overlay
 */
iscreate.maps.baidu.Map.prototype.removeOverlay = function(overlay){
	var me = this;
	if(overlay){
		overlay.setMap(null);
	}
};

/**
 * 地图的div大小改变的时候调用该方法更新地图
 */
iscreate.maps.baidu.Map.prototype.resize = function(){
	var me = this;
    //baidu.maps.event.trigger(me.map_, 'resize');
};

/**
 * 标记类
 * @param {Object} latitude 纬度
 * @param {Object} longitude 经度
 * @param {Object} title 鼠标悬停的时候显示的标题
 * @param {Object} markerOptions 标记配置项信息
 */
iscreate.maps.baidu.Marker = function(latLng, title, markerOptions){
	var me = this;
	var myOptions = {};
	var baiduLatLng = iscreate.maps.baidu.tools.toBaiduLatLng(latLng);
	myOptions.position = baiduLatLng;
	myOptions.title = title;
	if(markerOptions.icon){
		var img = document.createElement('img');
		img.src = markerOptions.icon;
		var myIcon = null;
        var height = img.height;
        var width = img.width;
		if(parseInt(height)==0 || parseInt(width)==0){
	       	img.onload = function () {
	            if (img.complete == true){
					width = img.width;
					height = img.height;
					myIcon = new BMap.Icon(markerOptions.icon, new BMap.Size(width,height));
					myOptions.icon = myIcon;
	            }
	    	}
		}else{
			myIcon = new BMap.Icon(markerOptions.icon, new BMap.Size(width,height));
			myOptions.icon = myIcon;
		}
	}
	myOptions.shadow = null;
	me.marker_ = new BMap.Marker(baiduLatLng,myOptions);
	me.marker_.tempIcon_ = markerOptions.icon;
	var res = typeof markerOptions.map;
	if(res=='object'){
		if(markerOptions.map==null){
			if(me.marker_.map){
				me.marker_.map.removeOverlay(me.marker_);
			}
		}else{
		    markerOptions.map.map_.addOverlay(me.marker_);              
		}
	}

	me.overlay_obj_ = me.marker_; //事件响应的时候用到
};

/**
 * 标记类调用跳动的动画
 */
iscreate.maps.baidu.Marker.prototype.setAnimation = function(type){
	var me = this;
	me.marker_.setAnimation(type); //跳动的动画
}

/**
 * 设置标记标题
 * @param {Object} x X轴位置
 * @param {Object} y Y轴位置
 * @param {Object} labelText 标题
 * @param {Object} labelStyleData label样式
 */
iscreate.maps.baidu.Marker.prototype.setLabel = function(x,y,labelText,labelStyleData){
	var me = this;
	var label = new BMap.Label(labelText,{offset:new BMap.Size(x,y)});
	label.setStyle(labelStyleData);
	me.marker_.setLabel(label);
}


/**
 * 设置标记的属性值
 * @param {Object} MarkerOptions
 */
iscreate.maps.baidu.Marker.prototype.setOptions = function(markerOptions){
	var me = this;
	var icon = markerOptions.icon;
	var title = markerOptions.title;
	var position = markerOptions.position;
	var draggable = markerOptions.draggable;
	if(icon){
		var img = document.createElement('img');
		img.src = icon;
		var myIcon = null;
        var height = img.height;
        var width = img.width;
		if(parseInt(height)==0 || parseInt(width)==0){
	       	//console.log("===开始进入onload..");
	       	img.onload = function () {
	            //console.log("===loading...");
	            if (img.complete == true){
					width = img.width;
					height = img.height;
					//console.log("===complete..."+height+","+width);
					myIcon = new BMap.Icon(markerOptions.icon, new BMap.Size(width,height));
					me.marker_.setIcon(myIcon);
			        me.marker_.tempIcon_ = icon;
	            }
	    	}
	    	//console.log("===执行完毕...");
		}else{
			myIcon = new BMap.Icon(markerOptions.icon, new BMap.Size(width,height));
			me.marker_.setIcon(myIcon);
	        me.marker_.tempIcon_ = icon;
		}
	}
	if(title){
		var label = new BMap.Label(title,{offset:new BMap.Size(-5,-20)});
		me.marker_.setLabel(label);
	}
	if(position){
		me.marker_.setPosition(position);
	}
	var res = typeof markerOptions.map;
	if(res=='object'){
		if(markerOptions.map==null){
			if(me.marker_.map){
				me.marker_.map.removeOverlay(me.marker_);
				me.marker_._visible = false;
			}
		}else{
		    markerOptions.map.map_.addOverlay(me.marker_);   
		    me.marker_._visible = true;
		}
	}
	if(draggable){
		me.marker_.enableDragging();
	}else{
		me.marker_.disableDragging();
	}
	setTimeout(function(){},500);
}

/**
 * 获取标记所在的经纬度信息
 */
iscreate.maps.baidu.Marker.prototype.getPosition = function(){
	var me = this;
	var baiduPosition = me.marker_.getPosition();
	var iscreatePositon = iscreate.maps.baidu.tools.toIscreateLatLng(baiduPosition);
	return iscreatePositon;
}
/**
 * 设置标记所在的经纬度信息
 * @param {Object} latLng
 */
iscreate.maps.baidu.Marker.prototype.setPosition = function(latLng){
	var me = this;
	var baiduLatLng = iscreate.maps.baidu.tools.toBaiduLatLng(latLng);
	me.marker_.setPosition(baiduLatLng);
}
/**
 * 设置标记是否可见
 * @param {Object} visible
 */
iscreate.maps.baidu.Marker.prototype.setVisible = function(visible){
	var me = this;
	if(visible){
		if(me.marker_.map){
			me.marker_.map.addOverlay(me.marker_);
		}
		me.marker_._visible = true;
	}else{
		if(me.marker_.map){
			me.marker_.map.removeOverlay(me.marker_);
		}
		me.marker_._visible = false;
	}
}
/**
 * 获取标记是否可见
 */
iscreate.maps.baidu.Marker.prototype.getVisible = function(){
	var me = this;
	return me.marker_._visible;
}

/**
 * 设置地图对象
 * @param {Object} latLng
 */
iscreate.maps.baidu.Marker.prototype.setMap = function(map){
	var me = this;
	me.belong_map_ = map;
	if(map==null){
		if(me.marker_.map){
			me.marker_.map.removeOverlay(me.marker_);
		}
	}else{
		map.map_.addOverlay(me.marker_);
		//var myIcon = new BMap.Icon(tempIcon_, new BMap.Size(20,20));
		//me.marker_.setIcon(myIcon);
	}
	/*var icon = me.marker_.tempIcon_;
	if(icon){
		var img = document.createElement('img');
		img.src = icon;
		var myIcon = null;
		var height = 70;
		var width = 70;
		myIcon = new BMap.Icon(icon, new BMap.Size(width,height));
		me.marker_.setIcon(myIcon);
		img.onload = function () {
            if (img.complete == true){
                height = img.height;
                width = img.width;
                if(me.marker_.iconwidth_){
					if((height!=0 && height!=me.marker_.iconheight_) || (width!=0 && width!=me.marker_.iconwidth_)){
						me.marker_.iconheight_ = height;
						me.marker_.iconwidth_ = width;
					}
					myIcon = new BMap.Icon(icon, new BMap.Size(me.marker_.iconwidth_,me.marker_.iconheight_));
				}else{
					myIcon = new BMap.Icon(icon, new BMap.Size(width,height));
					me.marker_.iconwidth_ = width;
					me.marker_.iconheight_ = height;
				}
				me.marker_.setIcon(myIcon);
            }
        };
	}*/
}

/**
 * 获取地图对象
 * @param {Object} latLng
 */
iscreate.maps.baidu.Marker.prototype.getMap = function(){
	var me = this;
	return me.belong_map_ ;
}
/**
 * 打开信息窗口
 * @param {Object} infoWindow
 */
iscreate.maps.baidu.Marker.prototype.openInfoWindow = function(infoWindow){
	var me = this;
	me.marker_.openInfoWindow(infoWindow.infoWindow_);
}


/**
 * 折线类
 * @param {Object} latlngList
 * @param {Object} strokeColor
 * @param {Object} strokeWeight
 * @param {Object} polylineOptions
 */
iscreate.maps.baidu.Polyline = function(latLngList,strokeColor,strokeWeight,polylineOptions){
	var me = this;
	var myOptions = iscreate.maps.baidu.tools.toBaiduPolylineOptions(polylineOptions);
	var path = new Array();
	for (var i=0;i<latLngList.length;i++) {
		var lat = latLngList[i].getLatitude();
		var lng = latLngList[i].getLongitude();
		var latLng = new BMap.Point(lng, lat);
		path.push(latLng);
	}
	myOptions.strokeColor = strokeColor;
	myOptions.strokeWeight = strokeWeight;
	myOptions.path = path;
	myOptions.strokeOpacity=polylineOptions.strokeOpacity;
	myOptions.strokeStyle = polylineOptions.strokeStyle;
	me.polyline_ = new BMap.Polyline(path,myOptions);	
	me.overlay_obj_ = me.polyline_; //事件响应的时候用到
	var res = typeof myOptions.map;
	if(res=='object'){
		if(myOptions.map==null){
			me.polyline_.map.removeOverlay(me.polyline_);
			me.polyline_.visible_ = false;
		}else{
			myOptions.map.addOverlay(me.polyline_);
			me.polyline_.visible_ = true;
		}
	}
};
/**
 * 设置折线的属性值
 * @param {Object} polylineOptions
 */
iscreate.maps.baidu.Polyline.prototype.setOptions = function(polylineOptions){
	var me = this;
	var myOptions = iscreate.maps.baidu.tools.toBaiduPolylineOptions(polylineOptions);
	if(myOptions.path){
		me.polyline_.setPath(myOptions.path);
	}
	if(myOptions.strokeColor){
		me.polyline_.setStrokeColor(myOptions.strokeColor);
	}
	if(myOptions.strokeOpacity){
		me.polyline_.setStrokeOpacity(myOptions.strokeOpacity);
	}
	if(myOptions.strokeWeight){
		me.polyline_.setStrokeWeight(myOptions.strokeWeight);
	}
	if(myOptions.strokeStyle){
		me.polyline_.setStrokeStyle(myOptions.strokeStyle);
	}
	var res = typeof myOptions.map;
	if(res=='object'){
		if(myOptions.map==null){
			me.polyline_.map.removeOverlay(me.polyline_);
			me.polyline_.visible_ = false;
		}else{
			myOptions.map.addOverlay(me.polyline_);
			me.polyline_.visible_ = true;
		}
	}
};

/**
 * 获取当前折线的路径(有序的经纬度对象序列)
 */
iscreate.maps.baidu.Polyline.prototype.getPath = function(){
	var me = this;
	var baiduPath = me.polyline_.getPath();
	var resultArray = new Array();  //存放经纬度对象的有序序列
	if(baiduPath!=undefined&&baiduPath!=null&&baiduPath!=""){
		for(var i=0;i<baiduPath.length;i++){
			var baiduLatLng =baiduPath[i];
			var iscreateLatLng = iscreate.maps.baidu.tools.toIscreateLatLng(baiduLatLng);
			resultArray.push(iscreateLatLng);
		}
	}
	return resultArray;
};
/**
 * 设置当前折线的路径
 * @param {Object} path
 */
iscreate.maps.baidu.Polyline.prototype.setPath = function(path){
	var me = this;
	if(path==undefined||path==null||path==""){
		return;
	}
	var baiduPath = new Array();
	for(var i=0;i<path.length;i++){
		var iscreateLatLng = path[i];
		var baiduLatLng = iscreate.maps.baidu.tools.toBaiduLatLng(iscreateLatLng);
		baiduPath.push(baiduLatLng);
	}
	me.polyline_.setPath(baiduPath);
};

/**
 * 设置折线是否可见
 * @param {Object} visible
 */
iscreate.maps.baidu.Polyline.prototype.setVisible = function(visible){
	var me = this;
	if(visible){
		if(me.polyline_._drawer._map){
			me.polyline_._drawer._map.addOverlay(me.polyline_);
		}else if(me.polyline_.map){
			me.polyline_.map.addOverlay(me.polyline_);
		}
		me.polyline_.visible_ = true;
	}else{
		me.polyline_.map.removeOverlay(me.polyline_);
		me.polyline_.visible_ = false;
	}
}
/**
 * 获取折线是否可见
 */
iscreate.maps.baidu.Polyline.prototype.getVisible = function(){
	var me = this;
	var visible_ = me.polyline_.visible_;
	return visible_;
}

/**
 * 给折线设置地图对象
 * @param {Object} latLng
 */
iscreate.maps.baidu.Polyline.prototype.setMap = function(map){
	var me = this;
	me.belong_map_ = map;
	if(map==null){
		if(me.polyline_.map){
			me.polyline_.map.removeOverlay(me.polyline_);
		}
		me.polyline_.visible_ = false;
	}else{
	    map.map_.addOverlay(me.polyline_);
	    me.polyline_.visible_ = true;
	}
	
}

/**
 * 获取地图对象
 * @param {Object} latLng
 */
iscreate.maps.baidu.Polyline.prototype.getMap = function(){
	var me = this;
	return me.polyline_.getMap();
}
/**
 * 打开信息窗口
 * @param {Object} infoWindow
 */
iscreate.maps.baidu.Polyline.prototype.openInfoWindow = function(infoWindow){
	var me = this;
	me.polyline_.openInfoWindow(infoWindow.infoWindow_);
}



/**
 * 信息窗口

 * @param {Object} iInfoWindowOptions
 */
iscreate.maps.baidu.InfoWindow = function(content,iInfoWindowOptions){
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
			var baiduLatLng = iscreate.maps.baidu.tools.toBaiduLatLng(iscreateLatLng);
			myOptions.position = baiduLatLng;
		}
	}
	me.infoWindow_ = new BMap.InfoWindow(content,{});
	me.overlay_obj_ = me.infoWindow_;
};
/**
 * 打开信息窗口
 * @param {Object} map
 * @memberOf {TypeName} 
 */
iscreate.maps.baidu.InfoWindow.prototype.open = function(map){
	var me = this;
	//map.map_.openInfoWindow(me.infoWindow_);
	//map.map_.openInfoWindow(me);
//	console.log("===暂时不封装百度地图的InfoWindow.open()");
};
/**
 * 关闭信息窗口
 * @param {Object} map
 * @memberOf {TypeName} 
 */
iscreate.maps.baidu.InfoWindow.prototype.close = function(){
	var me = this;
	me.infoWindow_.close();
};

/**
 * 修改信息窗口的位置
 * @param {Object} latLng
 * @memberOf {TypeName} 
 */
iscreate.maps.baidu.InfoWindow.prototype.setPosition = function(latLng){
	var me = this;
	var baiduLatLng = iscreate.maps.baidu.tools.toBaiduLatLng(latLng);
	//me.infoWindow_.setPosition(baiduLatLng);
//	console.log("===暂时不封装百度地图的InfoWindow.setPosition()");
};

/**
 * 获取信息窗口的位置
 * @memberOf {TypeName} 
 * @return {TypeName} 
 */
iscreate.maps.baidu.InfoWindow.prototype.getPosition = function(){
	var me = this;
	var baiduLatLng =  me.infoWindow_.getPosition();
	var iLatLng = iscreate.maps.baidu.tools.toIscreateLatLng(baiduLatLng);
	return iLatLng;
};

/**
 * 修改信息窗口的内容
 * @param {Object} latLng
 * @memberOf {TypeName} 
 */
iscreate.maps.baidu.InfoWindow.prototype.setContent = function(content){
	var me = this;
	me.infoWindow_.redraw();
	me.infoWindow_.setContent(content);
};


iscreate.maps.baidu.event.addClickEventListener = function(obj,eventName,fun)
{
	var me = this;
	
	if(eventName=="click")
	{
		obj.map_.addEventListener(eventName, fun);
	}
};



/**
 * 添加监听器
 * @param {Object} obj
 * @param {Object} enentName
 * @param {Object} callback
 */
iscreate.maps.baidu.event.addListener = function(obj, eventName, callback) {
	var me = this;
	me.handle_ = null; //事件句柄，用于删除当前绑定的事件
	//地图事件的处理
	if (obj instanceof iscreate.maps.baidu.Map) {
		if (eventName == "bounds_changed" || eventName == "center_changed" || eventName == "zoom_changed") {
			me.callbackName_ = function() {
				callback({});
			};
			me.callbackName2_ = function() {
				var latlng = obj.map_.getCenter();
				obj.map_.panTo(latlng);
				callback({});
			};
			if(eventName == "bounds_changed"){
				me.handle_ = obj.map_.addEventListener("moveend",me.callbackName_);
				me.handle2_ = obj.map_.addEventListener("zoomend",me.callbackName2_);
			}else{
				if(eventName=="zoom_changed"){
					eventName = "zoomend";
				}else if(eventName=="center_changed"){
					eventName = "moveend";
				}
				obj.map_.addEventListener(eventName,me.callbackName_);
			}
			me.eventName_ = eventName;
			me.obj_ = obj;
		} else if (eventName == "click" || eventName == "dblclick" || eventName == "mousemove" || eventName == "mouseout"
				|| eventName == "mouseover" || eventName == "rightclick") {
			me.callbackName_ = function(mouseEvent) {
						var baiduLatLng = mouseEvent.point;
						var iscreateLatLng = iscreate.maps.baidu.tools.toIscreateLatLng(baiduLatLng);
						callback( {
							"latLng" : iscreateLatLng
						});
					};
			me.handle_ = obj.map_.addEventListener(eventName,me.callbackName_);
			me.eventName_ = eventName;
			me.obj_ = obj;
		}
	} else if (obj instanceof iscreate.maps.baidu.Marker) { //标记事件的处理
		if (eventName == "click" || eventName == "dblclick"
				|| eventName == "mousedown" || eventName == "mouseout"
				|| eventName == "mouseover" || eventName == "mouseup"
				|| eventName == "rightclick" || eventName == "drag"
				|| eventName == "dragend" || eventName == "dragstart") {
			me.callbackName_ =  function(mouseEvent) {
						var baiduLatLng = mouseEvent.point;
						var iscreateLatLng = iscreate.maps.baidu.tools
								.toIscreateLatLng(baiduLatLng);
						callback( {
							"latLng" : iscreateLatLng
						});
					};
			me.handle_ = obj.marker_.addEventListener(eventName,me.callbackName_);
			me.eventName_ = eventName;
			me.obj_ = obj;
		}
	} else if (obj instanceof iscreate.maps.baidu.Polyline) {
		//折线事件的处理
		if (eventName == "click" || eventName == "dblclick"
				|| eventName == "mousedown" || eventName == "mousemove"
				|| eventName == "mouseout" || eventName == "mouseover"
				|| eventName == "mouseup" || eventName == "rightclick") {
			me.callbackName_ = function(mouseEvent) {
						var baiduLatLng = mouseEvent.point;
						var iscreateLatLng = iscreate.maps.baidu.tools
								.toIscreateLatLng(baiduLatLng);
						callback({
							"latLng" : iscreateLatLng
						});
					};
			me.handle_ = obj.polyline_.addEventListener(eventName,me.callbackName_);
			me.eventName_ = eventName;
			me.obj_ = obj;
			me.callback_ = callback;
		}
	}else if (obj instanceof iscreate.maps.baidu.Polygon) {
		 //多边形事件的处理
		if (eventName == "click" || eventName == "dblclick"
				|| eventName == "mousedown" || eventName == "mousemove"
				|| eventName == "mouseout" || eventName == "mouseover"
				|| eventName == "mouseup" || eventName == "rightclick") {
			me.callbackName_ = function(mouseEvent) {
						var baiduLatLng = mouseEvent.point;
						var iscreateLatLng = iscreate.maps.baidu.tools
								.toIscreateLatLng(baiduLatLng);
						callback({
							"latLng" : iscreateLatLng
						});
					};
			me.handle_ = obj.polygon_.addEventListener(eventName,me.callbackName_);
			me.eventName_ = eventName;
			me.obj_ = obj;
			me.callback_ = callback;
		}
	}else if (obj instanceof iscreate.maps.baidu.Rectangle) {
		//矩形事件的处理
		if (eventName == "click" || eventName == "dblclick"
				|| eventName == "mousedown" || eventName == "mousemove"
				|| eventName == "mouseout" || eventName == "mouseover"
				|| eventName == "mouseup" || eventName == "rightclick") {
			me.callbackName_ = function(mouseEvent) {
						var baiduLatLng = mouseEvent.point;
						var iscreateLatLng = iscreate.maps.baidu.tools
								.toIscreateLatLng(baiduLatLng);
						callback({
							"latLng" : iscreateLatLng
						});
					};
			me.handle_ = obj.rectangle_.addEventListener(eventName,me.callbackName_);
			me.eventName_ = eventName;
			me.obj_ = obj;
			me.callback_ = callback;
		}
	}else if (obj instanceof iscreate.maps.baidu.Circle) {
		//矩形事件的处理
		if (eventName == "click" || eventName == "dblclick"
				|| eventName == "mousedown" || eventName == "mousemove"
				|| eventName == "mouseout" || eventName == "mouseover"
				|| eventName == "mouseup" || eventName == "rightclick") {
			me.callbackName_ = function(mouseEvent) {
						var baiduLatLng = mouseEvent.point;
						var iscreateLatLng = iscreate.maps.baidu.tools
								.toIscreateLatLng(baiduLatLng);
						callback({
							"latLng" : iscreateLatLng
						});
					};
			me.handle_ = obj.circle_.addEventListener(eventName,me.callbackName_);
			me.eventName_ = eventName;
			me.obj_ = obj;
			me.callback_ = callback;
		}
	}
	return me;
};

/**
 * 删除监听器
 * @param {Object} listener
 */
iscreate.maps.baidu.event.removeListener = function(listener){
	var me = listener;
	if(me.obj_ instanceof iscreate.maps.baidu.Map){
		//map对象移除事件
		if(me.eventName_ == "bounds_changed"){
			me.obj_.map_.removeEventListener("moveend",me.callbackName_);
			me.obj_.map_.removeEventListener("zoomend",me.callbackName2_);
		}else{
			me.obj_.map_.removeEventListener(me.eventName_,me.callbackName_);
		}
	}else if(me.obj_ instanceof iscreate.maps.baidu.Marker){
		//Marker对象移除事件
		me.obj_.marker_.removeEventListener(me.eventName_,me.callbackName_);
	}else if(me.obj_ instanceof iscreate.maps.baidu.Polyline){
		//Polyline对象移除事件
		me.obj_.polyline_.removeEventListener(me.eventName_,me.callbackName_);
	}else if(me.obj_ instanceof iscreate.maps.baidu.Polygon){
		//Polygon对象移除事件
		me.obj_.polygon_.removeEventListener(me.eventName_,me.callbackName_);
	}else if(me.obj_ instanceof iscreate.maps.baidu.Rectangle){
		//Rectangle对象移除事件
		me.obj_.rectangle_.removeEventListener(me.eventName_,me.callbackName_);
	}else if(me.obj_ instanceof iscreate.maps.baidu.Circle){
		//Circle对象移除事件
		me.obj_.circle_.removeEventListener(me.eventName_,me.callbackName_);
	}
	
};

/**
 * 触发一个监听器
 * @param {Object} obj
 * @param {Object} enentName
 * @param {Object} opts
 */
iscreate.maps.baidu.event.trigger= function(obj,eventName,opts){
	var me = this;
	var baiduObj = null;
//	console.log("===baidu.event.trigger()还没实现哦");
	return false;
	if (obj instanceof iscreate.maps.baidu.Map) {
		baiduObj= obj.map_;
	}else{
		baiduObj=obj.overlay_obj_;
	}
	if(opts&&opts.latLng){   //这里需要加入判断latLng变量类型是不是经纬度对象
		var baiduLatLng = iscreate.maps.baidu.tools.toBaiduLatLng(opts.latLng)
		opts.latLng = baiduLatLng;
		baidu.maps.event.trigger(baiduObj, eventName, opts);
	}else{
		baidu.maps.event.trigger(baiduObj, eventName, opts);
	}
};

/********************************将iscreate定义的对象设计为转换为**************************************/
/**
 * 将iscreate定义的LatLng转换为baidu api的LatLng对象
 * @param {Object} iscreateLatLng
 */
iscreate.maps.baidu.tools.toBaiduLatLng = function(iscreateLatLng){
    var lat = iscreateLatLng.getLatitude();
    var lng = iscreateLatLng.getLongitude();
	var baiduLatLng = new BMap.Point(lng,lat);
	return baiduLatLng;
}
/**
 * baidu的经纬度对象转换为iscreate的经纬度对象
 * @param {Object} baiduLatLng
 * @return {TypeName} 
 */
iscreate.maps.baidu.tools.toIscreateLatLng = function(baiduLatLng){
	var lat = baiduLatLng.lat;
    var lng = baiduLatLng.lng;
	var iscreateLatLng = new iscreate.maps.base.LatLng(lat,lng);
	return iscreateLatLng;
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
/**
 * 将iscreate的Map对象参数转换为baidu的Map对象参数
 * @param {Object} iscreateMapOptions
 */
iscreate.maps.baidu.tools.toBaiduMapOptions = function(iscreateMapOptions){
	var opts_ = iscreateMapOptions;
	var myOptions = {};

	if(opts_){
		if(opts_.zoom){
			myOptions.zoom = opts_.zoom;
		}
		if(opts_.center){
			myOptions.center = new BMap.Point(opts_.center.getLongitude(),opts_.center.getLatitude());
		}
	}
	return myOptions;
}

/**
 * 将iscreate的Polyline对象参数转换为baidu的Polyline对象参数
 * @param {Object} iscreatePolylineOptions
 */
iscreate.maps.baidu.tools.toBaiduPolylineOptions = function(iscreatePolylineOptions){
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
			var baiduPath = new Array();
			var iscreatePath = opts_.path;
			for(var i=0;i<iscreatePath.length;i++){
				var iLatLng = iscreatePath[i];
				var gLatLng = iscreate.maps.baidu.tools.toBaiduLatLng(iLatLng);
				baiduPath.push(gLatLng);
			}
			myOptions.path = baiduPath;
		}
		if(("strokeColor" in opts_)&&opts_.strokeColor){
			myOptions.strokeColor = opts_.strokeColor;
		}
		if(("strokeWeight" in opts_)&&opts_.strokeWeight){
			myOptions.strokeWeight = opts_.strokeWeight;
		}
		if(("strokeOpacity" in opts_)&&opts_.strokeOpacity){
			myOptions.strokeOpacity = opts_.strokeOpacity;
		}
		if(("strokeStyle" in opts_)&&opts_.strokeStyle){
			myOptions.strokeStyle = opts_.strokeStyle;
		}
	}
	return myOptions;
}


/**
 * 将iscreate的TextOverlay对象参数转换为baidu的TextOverlay对象参数
 * @param {Object} iscreateMarkerOptions
 */
iscreate.maps.baidu.tools.toBaiduTextOverlayOptions = function(
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
			myOptions.position = iscreate.maps.baidu.tools
					.toBaiduLatLng(opts_.position);
		}
		if (("text" in opts_) && opts_.text) {
			myOptions.text = opts_.text;
		}
	}
	return myOptions;
};
/**
 * 将iscreate的ImageOverlay对象参数转换为baidu的Overlay对象参数
 * @param {Object} iscreateMarkerOptions
 */
iscreate.maps.baidu.tools.toBaiduImageOverlayOptions = function(iscreateImageOverlayOptions) {
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

/********************** TextOverlay 对象*************************************************/
/**
 * 文字覆盖层
 * @param {Object} position
 * @param {Object} title
 * @param {Object} textOverlayOptions
 * @memberOf {TypeName} 
 */
iscreate.maps.baidu.TextOverlay = function(position, text, textOverlayOptions) {
	var me = this;
	var myOptions = iscreate.maps.baidu.tools.toBaiduTextOverlayOptions(textOverlayOptions);
			
	var baiduLatLng = iscreate.maps.baidu.tools.toBaiduLatLng(position);
	myOptions.position = baiduLatLng;
	myOptions.text = text;
	me.textoverlay_ = new ClassForBaidu.TextOverlay(myOptions.position,myOptions.text, myOptions);
	me.overlay_obj_ = me.marker_; //事件响应的时候用到
};

/**
 * 设置文字覆盖层的属性值
 * @param {Object} textOverlayOptions
 * @memberOf {TypeName} 
 */
iscreate.maps.baidu.TextOverlay.prototype.setOptions = function(
		textOverlayOptions) {
	var me = this;
	var myOptions = iscreate.maps.baidu.tools.toBaiduTextOverlayOptions(textOverlayOptions);
	var res = typeof myOptions.map;
	if(res=='object'){
		if(myOptions.map==null){
			me.textoverlay_.div_.style.display = "none";
			me.textoverlay_.div_.innerHTML = "";
			if(me.textoverlay_.map_){
				me.textoverlay_.map_.removeOverlay(me.textoverlay_);
			}
		}else{
		    myOptions.map.addOverlay(me.textoverlay_);              
		}
	}
	if(myOptions.position){
		
	}
	if(myOptions.text){
		
	}
}

/**
 * 设置地图对象
 * @param {Object} map
 */
iscreate.maps.baidu.TextOverlay.prototype.setMap = function(map) {
	var me = this;
	if (map == null) {
		//me.textoverlay_.map_.removeOverlay(me.textoverlay_);
		if(me.textoverlay_.div_){
			me.textoverlay_.div_.style.display = "none";
			me.textoverlay_.div_.innerHTML = "";
		}
	} else {	
		me.belong_map_ = map;
		me.belong_map_.map_.addOverlay(me.textoverlay_);
	}
}
/**
 * 获取地图对象
 * @param {Object} latLng
 */
iscreate.maps.baidu.TextOverlay.prototype.getMap = function() {
	var me = this;
	return me.belong_map_;
}

var ClassForBaidu = {};
/**
 * Baidu自定义文字图层
 * @param {Object} point
 * @param {Object} text
 * @param {Object} map
 * @memberOf {TypeName} 
 */
ClassForBaidu.TextOverlay = function(point, text, textOverlayOptions) {
	var me = this;
	
	// 初始化参数：坐标、文字、地图
	this.point_ = point;
	this.text_ = text;
	// 到onAdd时才需要创建div
	this.div_ = null;
	var opts_ = textOverlayOptions;
	if (opts_) {
		if ("map" in opts_) {
			if (opts_.map) {
				//暂不实现此方法
//				console.log("===暂不实现TextOverlay直接new的时候setMap");
			}
		}
	}
	ClassForBaidu.TextOverlay.prototype = new BMap.Overlay();
	ClassForBaidu.TextOverlay.prototype.initialize = function(map) {
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
		var me = this;
		div.innerHTML = this.text_;
		this.div_ = div;
		this.map_ = map;
		this.map_.getPanes().labelPane.appendChild(div);
		//重画
		this.draw();
	}
	//
	ClassForBaidu.TextOverlay.prototype.draw = function() {
		var div = this.div_;
	    var pixel = this.map_.pointToOverlayPixel(this.point_);
	    div.style.left = pixel.x - div.offsetWidth / 2 + "px";
	    div.style.top  = pixel.y - 35 + "px";
	    //默认为10个字的宽度
		div.style.width = '10em';
		div.style.width = '200px';
		//div.style.height = '10px';
		div.align = "center";
	}
	//
	ClassForBaidu.TextOverlay.prototype.onRemove = function() {
		this.div_.parentNode.removeChild(this.div_);
		this.div_ = null;
	}
}


/**
 * 图片覆盖层
 * @param {Object} position
 * @param {Object} image
 * @param {Object} imageOverlayOptions
 * @memberOf {TypeName} 
 */
iscreate.maps.baidu.ImageOverlay = function(position, image, imageOverlayOptions) {
	var me = this;
	var myOptions = iscreate.maps.baidu.tools.toBaiduImageOverlayOptions(imageOverlayOptions);
	var baiduLatLng = iscreate.maps.baidu.tools.toBaiduLatLng(position);
	myOptions.position = baiduLatLng;
	myOptions.image = image;
	me.textoverlay_ = new ClassForBaidu2.ImageOverlay(myOptions.position,myOptions.image, myOptions);
	me.overlay_obj_ = me.marker_; //事件响应的时候用到
};

/**
 * 设置图片覆盖层的属性值
 * @param {Object} textOverlayOptions
 * @memberOf {TypeName} 
 */
iscreate.maps.baidu.ImageOverlay.prototype.setOptions = function(imageOverlayOptions) {
	var me = this;
	var myOptions = iscreate.maps.baidu.tools.toBaiduImageOverlayOptions(imageOverlayOptions);
	var res = typeof myOptions.map;
	if(res=='object'){
		if(myOptions.map==null){
			if(me.textoverlay_.div_){
				me.textoverlay_.div_.style.display = "none";
				me.textoverlay_.div_.innerHTML = "";
			}
			if(me.textoverlay_.map_){
				me.textoverlay_.map_.removeOverlay(me.textoverlay_);
			}
		}else{
		    myOptions.map.addOverlay(me.textoverlay_);              
		}
	}
}

/**
 * 设置地图对象
 * @param {Object} map
 */
iscreate.maps.baidu.ImageOverlay.prototype.setMap = function(map) {
	var me = this;
	if (map == null) {
		if(me.textoverlay_.div_){
			me.textoverlay_.div_.style.display = "none";
			me.textoverlay_.div_.innerHTML = "";
		}
		if(me.textoverlay_.map_){
			me.textoverlay_.map_.removeOverlay(me.textoverlay_);
		}
	} else {	
		me.belong_map_ = map;
		me.belong_map_.map_.addOverlay(me.textoverlay_);
	}
}
/**
 * 获取地图对象
 * @param {Object} latLng
 */
iscreate.maps.baidu.TextOverlay.prototype.getMap = function() {
	var me = this;
	return me.belong_map_;
}

var ClassForBaidu2 = {};
/**
 * Baidu自定义图片图层
 * @param {Object} point
 * @param {Object} text
 * @param {Object} map
 * @memberOf {TypeName} 
 */
ClassForBaidu2.ImageOverlay = function(point, image, imageOverlayOptions) {
	var me = this;
	
	// 初始化参数：坐标、文字、地图
	this.point_ = point;
	this.image_ = image;
	// 到onAdd时才需要创建div
	this.div_ = null;
	var opts_ = imageOverlayOptions;
	if (opts_) {
		if ("map" in opts_) {
			if (opts_.map) {
				//暂不实现此方法
//				console.log("===暂不实现ImageOverlay直接new的时候setMap");
			}
		}
	}
	ClassForBaidu2.ImageOverlay.prototype =  new BMap.Overlay();//将自定义对象的 prototype 设置为 BMap.Overlay() 的新实例
	ClassForBaidu2.ImageOverlay.prototype.initialize = function(map) {
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
		var me = this;
		div.innerHTML = this.image_;
		this.div_ = div;
		this.map_ = map;
		this.map_.getPanes().labelPane.appendChild(div);
		//重画
		this.draw();
	}
	//
	ClassForBaidu2.ImageOverlay.prototype.draw = function() {
		var div = this.div_;
	    var pixel = this.map_.pointToOverlayPixel(this.point_);
	    div.style.left = pixel.x - div.offsetWidth / 2 + "px";
	    div.style.top  = (pixel.y+15) + "px";
	    //默认为10个字的宽度
		div.style.width = '10em';
		div.style.width = '200px';
		//div.style.height = '10px';
		div.align = "center";
	}
	//
	ClassForBaidu2.ImageOverlay.prototype.onRemove = function() {
		this.div_.parentNode.removeChild(this.div_);
		this.div_ = null;
	}
}


/******************对象名称引用***********************/
var ILatLng = iscreate.maps.base.LatLng;
var IMap = iscreate.maps.baidu.Map;
var IMarker = iscreate.maps.baidu.Marker;
var IInfoWindow = iscreate.maps.baidu.InfoWindow;
var IPolyline = iscreate.maps.baidu.Polyline;
var ITextOverlay = iscreate.maps.baidu.TextOverlay;
var IImageOverlay = iscreate.maps.baidu.ImageOverlay;
var IMapComm = iscreate.maps.comm;
var IMapEvent = iscreate.maps.baidu.event;
