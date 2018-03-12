

/************************* 基础对象包******************************/
/**
 * 经纬度封装类
 * @param {double} latitude
 * @param {double} longitude
 */
iscreate.maps.base.LatLng = function(latitude,longitude){
	var lat_=latitude;
	var lng_ = longitude;
	return {
		'getLongitude':function(){return lng_},
		'getLatitude':function(){return lat_},
		'setLongitude':function(lat){lat_=lat;},
		'setLatitude':function(lng){lng_lng}
	}
}
/**
 * 经纬度范围类
 * (一个矩形范围,包含西南方经纬度和东北方经纬度)
 * @param {LatLng} sw
 * @param {LatLng} ne
 */
iscreate.maps.base.LatLngBounds = function(sw,ne){
	var southwest_ = sw;
	var northeast_ = ne;
	return {
		'getSw':function(){return southwest_},
		'getNe':function(){return northeast_},
		'setSw':function(southwest){southwest_=southwest},
		'setNe':function(northeast){northeast_=northeast}
	}
};
/**
 * Size类(以像素为单位)
 * @param {number} height
 * @param {number} width
 */
iscreate.maps.base.Size = function(height,width){
	var height_=height;
	var width_ = width;
	return {
		'getHeight':function(){return height_;},
		'getWidth':function(){return width_},
		'setHeight':function(h){height_=h;},
		'setWidth':function(w){width_=w;}
	};
};

/**
 * 以像素为单位的点对象
 * @param {Object} x
 * @param {Object} y
 * @return {TypeName} 
 */
iscreate.maps.base.Point = function(x,y){
	var x_=x;
	var y_=y;
	return {
		'getX':function(){return x_;},
		'getY':function(){return y_;},
		'setX':function(px){x_=px;},
		'setY':function(py){y_=py;}
	};
};

/*******************************常用功能包**************************************/
/**
 * 计算两个经纬度对象的距离
 * @param {Object} latLng1
 * @param {Object} latLng2
 */
iscreate.maps.comm.distance =function(latLng1,latLng2){
	var me = this;
	var lat1 = latLng1.getLatitude();
	var lng1 = latLng1.getLongitude();
	var lat2 = latLng2.getLatitude();
	var lng2 = latLng2.getLongitude();
	var R = 6371000; // 地球半径单位为米
	var dLat = (lat2 - lat1) * Math.PI / 180;
	var dLon = (lng2 - lng1) * Math.PI / 180;
	var a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
			+ Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180)
			* Math.sin(dLon / 2) * Math.sin(dLon / 2);
	var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	var d = R * c;
	return d;
};
/**
 * 将GPS坐标转换为地图的经纬度对象
 * @param {Object} latLng
 */
iscreate.maps.comm.gpsToMapLatLng=function(latLng){
};
/**
 * 将地图坐标转换成GPS坐标
 * @param {Object} latLng
 */
iscreate.maps.comm.mapToGpsLatLng = function(latLng){};