function getLoadToken() {
	return new Date().getTime() + Math.random() * 100 + "";
}

// --------普通工具---------//

function azimuthDiff(azimuth1, azimuth2) {
	var larg = azimuth1 > azimuth2 ? azimuth1 : azimuth2;
	var small = azimuth1 < azimuth2 ? azimuth1 : azimuth2;
	var diff = larg - small;
	if (larg > 270 && small < 90) {
		diff = small + 360 - larg;
	}
	return diff;
}

function distance(lng1, lat1, lng2, lat2) {
	// var lat1 = latLng1.getLatitude();
	// var lng1 = latLng1.getLongitude();
	// var lat2 = latLng2.getLatitude();
	// var lng2 = latLng2.getLongitude();
	if (!lng1 || !lat1 || !lng2 || !lat2) {
		// 任何一个没有定义，认为很远
		return 100000;
	}
	var R = 6371000; // 地球半径单位为米
	var dLat = (lat2 - lat1) * Math.PI / 180;
	var dLon = (lng2 - lng1) * Math.PI / 180;
	var a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
			+ Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180)
			* Math.sin(dLon / 2) * Math.sin(dLon / 2);
	var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	var d = R * c;
	return d;
}

Array.prototype.del = function(index) {
	if (isNaN(index) || index >= this.length) {
		return false;
	}
	this.splice(index, 1);
};

// ---------------------//
function getValidValue(v, defaultValue, precision) {
	if (v == null || v == undefined || v == "null" || v == "NULL"
			|| v == "undefined" || v == "UNDEFINED") {
		if (defaultValue != null && defaultValue != undefined)
			return defaultValue;
		else
			return "";
	}

	if (typeof v == "number") {
		try {
			v = new Number(v).toFixed(precision);
		} catch (err) {
			// console.error("v=" + v + "," + err);
		}
	}
	return v;
}

// 判断是array
var isArray = function(obj) {
	return Object.prototype.toString.call(obj) === '[object Array]';
}

/**
 * 使一个元素渐渐展现然后又渐渐隐去
 * 
 * @param objId
 * @param timeIn
 * @param timeOut
 * @param stayTime
 * 
 */
function animateInAndOut(objId, timeIn, timeOut, stayTime, tipId, tips) {
	if (objId == null || objId == undefined) {
		return;
	}
	if (tipId && tips) {
		try {
			$("#" + tipId).html(tips);
		} catch (err) {

		}
	}
	try {
		if (typeof timeIn == "number" && typeof timeOut == "number") {
			$("#" + objId).fadeIn(timeIn, function() {
				window.setTimeout(function() {
					$("#" + objId).fadeOut(timeOut);
				}, stayTime);
			});
		}
	} catch (err) {

	}
}

function showOperTips(outerId, tipId, tips) {
	try {
		$("#" + outerId).css("display", "");
		$("#" + outerId).find("#" + tipId).html(tips);
	} catch (err) {
	}
}

function hideOperTips(outerId) {
	try {
		$("#" + outerId).css("display", "none");
	} catch (err) {
	}
}

function print(obj) {
	if (!obj) {
		// console.log(obj);
		return;
	}
	if (typeof obj == "string") {
		// console.log(obj);
	}
	if (isArray(obj)) {
		for ( var i = 0; i < obj.length; i++) {
			_printObj(obj[i]);
		}
		return;
	}
	_printObj(obj);
}

function _printObj(obj) {
	if (!obj) {
		// console.log(obj);
		return;
	}
	for ( var i in obj) {
		// console.log(i + "=" + obj[i]);
	}
}

/**
 * 获取指定元素到页面左上角的绝对距离
 * 
 * @param e
 * @returns {___anonymous2664_2677}
 */
function getElementAbsPos(e) {
	var t = e.offsetTop;
	var l = e.offsetLeft;
	while (e = e.offsetParent) {
		t += e.offsetTop;
		l += e.offsetLeft;
	}

	return {
		left : l,
		top : t
	};
}

/**
 * 已知起始角度(a0)和终止角度(a1)获得他们之间的0 到 360度的夹角
 */
function getMinAngle(a0, a1) {
	var angle = 0;
	while (a0 < 0) {
		a0 += 360;
	}
	while (a1 < 0) {
		a1 += 360;
	}

	while (a0 > 360) {
		a0 -= 360;
	}
	while (a1 > 360) {
		a1 -= 360;
	}

	var min = a0 > a1 ? a1 : a0;
	var max = a0 > a1 ? a0 : a1;

	angle = max - min;

	if (max >= 270 && min < 90) {
		angle = (360 - max + min);
	}
	return angle;
}

/**
 * @author Liang YJ
 * @param string
 * @returns string
 * @date 2013.12.26 15:20 描述：将样式值从'rgb(0,0,0)'转换成'#000000'
 */
function rgbToHex(rgb) {
	// ie8颜色样式以#000000的形式保存
	if ("#" == rgb[0]) {
		return rgb;
	}
	// firefox和chrome的颜色样以#000000和rgb(0,0,0)这两种形式保存
	rgb = rgb.slice(4, rgb.length - 1);
	var rgbArr = rgb.split(",");
	hex = "#";
	for ( var i = 0; i < rgbArr.length; i++) {
		rgbArr[i] = parseInt(rgbArr[i]).toString(16);
		if (rgbArr[i].length < 2) {
			rgbArr[i] = "0" + rgbArr[i];
		}
		hex = hex + rgbArr[i];
	}
	return hex;
}

/**
 * @author Liang YJ
 * @date 2014-3-21 11:39
 * @param point1
 *            point : {"lng":lng,"lat":lat};
 * @param point2
 * @description 比较两个点是不是同一个点
 */
function comparePoint(point1, point2) {
	return (point1.lng === point2.lng && point1.lat === point2.lat);

}

/**
 * @author Liang YJ
 * @date 2014-3-21 11:00
 * @param line1
 *            line : json对象 {"point1":point1,"point2":point2}; point :
 *            {"lng":lng,"lat":lat};
 * @param line2
 *            json对象 {"point1":point1,"point2":point2};
 * @description 通过比较两条线段的端点是否一致来比较两条线段是不是同一条线段
 */
function compareLine(line1, line2) {
	var flag1 = comparePoint(line1.point1, line2.point1)
			&& comparePoint(line1.point2, line2.point2);
	var flag2 = comparePoint(line1.point1, line2.point2)
			&& comparePoint(line1.point2, line2.point1);
	return (flag1 || flag2);
}

/**
 * 根据参数中的字节大小，返回带单位的合适的大小表达
 * 
 * @param val
 */
function getPropValueExpress(fileSize) {
	if(isNaN(fileSize)){
		return "";
	}
	var power = new Array(10, 20, 30, 40, 50, 60);
	var unit = new Array("字节", "k", "M", "G", "T", "P");
	var i = 0;
	for (i = 0; i < power.length; i++) {
		if (fileSize < Math.pow(2, power[i])) {
			return new Number((fileSize / (Math.pow(2, power[i] - 10))))
					.toFixed(2)
					+ unit[i];
		}
	}

	return "无法衡量";
}

/**
 * 测试是否包含有以下特殊字符
 * ~'!@#$%^&*()-+_=:
 * @param str
 * @returns
 */ 
function ifHasSpecChar(str){
	var pattern = new RegExp("[~'!@#$%^&*()-+_=:]");
	return pattern.test(str);
}

/**
 * 是否以逗号分割的数字，允许逗号结尾
 * @param lteCells
 * @returns {Boolean}
 */
function isNumCutByComma(lteCells){
	var flag = true;
	var reg = /^(\d{6,9})(,\d{6,9})*,?$/;
	if(!reg.test(lteCells)){
		flag =false;
	}
	return flag;
}