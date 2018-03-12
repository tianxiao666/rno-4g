//邻区颜色
var option_ncell={
			'fillColor':'#FF0040'
	};
// 主覆盖小区颜色
	var option_serverCell={
			'fillColor':'#FE9A2E'
	};
	// 设定线条的样式
	// #FE9A2E #FF0040
	
	// var option_line_serverCell =
	// {strokeColor:dtopts[0].style.color,strokeWeight:2,strokeOpactity:1};
	// var
	// option_line_ncell={strokeColor:dtopts[1].style.color,strokeWeight:1,strokeOpactity:1};

// ----------采样点 数据结构----------------//
function Sample(sample) {
	this.lng = sample.lng;// 采样点地图经度
	this.lat = sample.lat;// 采样点地图纬度
	this.cellLng = sample.cellLng;// 服务小区gps经度
	this.cellLat = sample.cellLat;// 服务小区gps纬度
	this.cellMapLng = sample.cellMapLng;// 服务小区地图baidu经度
	this.cellMapLat = sample.cellMapLat;// 服务小区地图baidu纬度
	this.cell = sample.cell;// 服务小区英文名称
	this.rxLevSub = sample.rxLevSub;// 采样点得到的服务小区的信号强度
	this.rxQualSub = sample.rxQualSub;// 采样点得到的服务小区的信号质量[1,7]
	this.distance = sample.distance;// 采样点距离服务小区的距离（米）
	this.angle = sample.angle;// 服务小区到采样点的夹角
	this.ncellRxLevSubs = sample.ncellRxLevSubs;// 采样点测量到的邻区的信号强度（以逗号分隔）
	this.ncells = sample.ncells;// 采样点的到的邻区的名称
	this.sampleTimeStr = sample.sampleTimeStr;// 采样点的时间，字符串形式 yyyy-MM-dd
												// HH:mm:ss
	this.avgDistance=sample.avgDistance;// 采样点距离小区的平均距离
	

	this.avgNcellLevSub = sample.avgNcellLevSub;// 采样点得到的邻区的平均信号强度
	this.bearing = sample.bearing;// 服务小区的方位角

	// 2013-12-2
	this.cellIndoor = sample.cellIndoor;
	this.ncellIndoors = sample.ncellIndoors;
	//@author chao.xj 2015-7-21下午2:01:39 4g采样参数
	this.areaType = sample.areaType;//区域类型
	this.cellRsrp = sample.cellRsrp;//小区场强
	this.cellRsSinr = sample.cellRsSinr;//小区干扰
	this.ncellRsrps = sample.ncellRsrps;//邻区场强集合
	this.cellEarfcn = sample.cellEarfcn;//小区频点
	this.ncellRsrp1 = sample.ncellRsrp1;//邻区场强1
	this.ncellRsrp2 = sample.ncellRsrp2;
	this.ncellRsrp3 = sample.ncellRsrp3;
	this.ncellRsrp4 = sample.ncellRsrp4;
	this.ncellRsrp5 = sample.ncellRsrp5;
	this.ncellRsrp6 = sample.ncellRsrp6;
}

Sample.prototype.get = function(prop) {
	return this[prop];
}

function getSampleDetail(sample) {
	if (!sample) {
		return {};
	}
	
	var cont = new Object();
	cont['tdSampleTime']=getValidValue(sample.sampleTimeStr, "未知");
	cont['tdServerCell']=getValidValue(sample.cell, "未知");
	cont['tdRxLev']=getValidValue(sample.rxLevSub, "未知");
	cont['tdRxQual']=getValidValue(sample.rxQualSub, "未知");
	cont['tdNcells']=getValidValue(sample.ncells, "未知");
	cont['tdNcellRxLev']=getValidValue(sample.avgNcellLevSub, "未知");
	cont['tdServerCellToSampleAngle']=getValidValue(sample.angle, "未知");
	cont['tdServerCellAngle']=getValidValue(sample.bearing, "未知");
	//@author chao.xj 2015-7-21下午2:16:59
	cont['tdServerCellToSampleDis']=getValidValue(sample.distance, "未知");
	cont['tdAreaType']=getValidValue(sample.areaType, "未知");
	cont['tdCellRsrp']=getValidValue(sample.cellRsrp, "未知");
	cont['tdCellRsSinr']=getValidValue(sample.cellRsSinr, "未知");
	cont['tdNcellRsrps']=getValidValue(sample.ncellRsrps, "未知");
	cont['tdCellEarfcn']=getValidValue(sample.cellEarfcn, "未知");
	return cont;
}

// -----------采样点图元---------------//

function CommonBase() {
	this.DiffDistance = 5;
	this.singleDomClass = "singleWrapCls";
	this.multiDomClass = "multiWrapCls";
}
/**
 * 用于代表地图上的一个圆圈——采样点
 */
function IsComposeSample() {
	this.sampleArray = new Array();
	this.sampleCnt = 0;
	this.mapElement = null;// 地图图元
	this.lng = 0;
	this.lat = 0;
	this.centerPoint = null;
	this.radius = 10;

}

function IsComposeSample(sample, option) {
	if (!sample) {
		return null;
	}
	this.sampleArray = new Array();
	this.sampleArray.push(sample);

	this.sampleCnt = 1;
	this.mapElement = null;// 地图图元 BMap.Circle

	this.lng = sample.lng;
	this.lat = sample.lat;
	this.centerPoint = new BMap.Point(this.lng, this.lat);

	this.radius = 10;// px

}

// 继承
IsComposeSample.prototype = new CommonBase();

/**
 * 增加一个
 * 
 * @param sample
 */
IsComposeSample.prototype.addSample = function(sample) {
	if (!sample) {
		return;
	}
	this.sampleArray.push(sample);
	this.sampleCnt++;
	// if(this.sampleCnt==2){
	// //改变外观
	// }
}

IsComposeSample.prototype.getSampleArray = function() {
	return this.sampleArray;
}

IsComposeSample.prototype.getSampleCount = function() {
	return this.sampleCnt;
}
IsComposeSample.prototype.getDomClass = function() {
	if (this.samplCnt > 1) {
		return this.multiDomClass;
	} else {
		return this.singleDomClass;
	}
}
/**
 * 两个是不是重叠
 * 
 * @param anotherSample
 */
IsComposeSample.prototype.isOverlay = function(anotherSample) {
	var d = distance(this.lng, this.lat, anotherSample.lng, anotherSample.lat);
	if (d < DiffDistance) {
		return true;
	} else {
		return false;
	}
}

IsComposeSample.prototype.get = function(prop) {
	return this[prop];
}

/**
 * 获取详细信息
 */
IsComposeSample.prototype.getDetail = function() {
	var cont = new Array();
	for ( var i = 0; i < this.sampleArray.length; i++) {
		cont.push(getSampleDetail(this.sampleArray[i]));
	}
	return cont;
}
/**
 * 获取中心点
 * 
 * @returns {BMap.Point}
 */
IsComposeSample.prototype.getCenterPoint = function() {
	return this.centerPoint;
}

/**
 * 获取半径
 * 
 * @returns {Number}
 */
IsComposeSample.prototype.getRadius = function() {
	return this.radius;
}

IsComposeSample.prototype.setMapElement = function(mapElement) {
	this.mapElement = mapElement;
}

// --------图元-------------//
function MapElement() {
	this.elementShape = null;
	this.elementData = null;// IsComposeSample对象
}

function MapElement(elementShape, elementData) {
	this.elementShape = elementShape;
	this.elementData = elementData;

	// 相互关联
	this.elementData.mapElement = this;
	this.elementShape.mapElement = this;

	this.elementData._shape = elementShape;
	this.elementShape._data = elementData;

}
/**
 * 获取中心点
 * 
 * @returns
 */
MapElement.prototype.getCenter = function() {
	if (this.elementShape) {
		return this.elementShape.getCenter();
	}
	return null;
}

/**
 * 获取指定小区的经纬度
 */
MapElement.prototype.getCellPoint = function(cell) {
	if (cell) {
		if (this.elementData) {
			samples = this.elementData.getSampleArray();
			if (samples) {
				for ( var i = 0; i < samples.length; i++) {
					if (samples[i].cell == cell) {
						return new BMap.Point(samples[i].cellMapLng,
								samples[i].cellMapLat);
					}
				}
			}
		}
	}
	return null;
}

/**
 * 修改外观
 * 
 * @param option
 * @returns {Boolean}
 */
MapElement.prototype.applyOption = function(option) {
	if (!option) {
		return false;
	}
	if (!this.elementShape) {
		return false;
	}
	if (option['fillColor']) {
		this.elementShape.setFillColor(option['fillColor']);
	}
	return true;
}
/**
 * 修改透明度
 * 
 * @param option
 * @returns {Boolean}
 */
MapElement.prototype.applyOpacity = function(option) {
	if (!option) {
		return false;
	}
	if (!this.elementShape) {
		return false;
	}
	if (option['fillOpacity']!=null) {
		this.elementShape.setOpacity(option['fillOpacity']);
	}
	return true;
}
MapElement.prototype.getDetail = function() {
	if (!this.elementData) {
		return {};
	}
	return this.elementData.getDetail();
}

MapElement.prototype.setIsShow = function(visible) {
	if (!this.elementShape) {
		return;
	}
	if (visible == true) {
		this.elementShape.show();
	} else {
		this.elementShape.hide();
	}
}

MapElement.prototype.getIsShow = function() {
	return this.elementShape.getIsShow();
}

MapElement.prototype.setElementData = function(elementData) {
	var old = this.elementData;
	old._shape == null;// 将之前的引用清空
	old.mapElement = null;
	old = null;

	this.elementData = elementData;
	this.elementData.mapElement = this;
	// 更新关联关系
	if (this.elementShape) {
		this.elementShape._data = elementData;
		this.elementData._shape = this.elementShape;
	}
}

MapElement.prototype.getElementData = function() {
	return this.elementData;
}

MapElement.prototype.setElementShape = function(elementShape) {
	var old = this.elementShape;
	old._data = null;// 将之前的引用清空
	old.mapElement = null;
	old = null;

	this.elementShape = elementShape;
	this.elementShape.mapElement = this;
	// 更新关联关系
	if (this.elementData) {
		this.elementData._shape = elementShape;
		this.elementShape._data = this.elementData;
	}
}

MapElement.prototype.getElementShape = function() {
	return this.elementShape;
}
// 显示在地图上
MapElement.prototype.show = function(map) {
	if (!map) {
		return;
	}
	 map.addOverlay(this.elementShape);
	//div函数暂时注销
//	this.elementShape.show();
}
// 隐藏
MapElement.prototype.hide = function(map) {
	if (!map) {
		return;
	}
	// map.removeOverlay(this.elementShape);
	this.elementShape.hide();
}

function MapDomWrap(radius, domtag, point, domclass) {
	// console.log("在构造方法内：domtag=" + domtag + ",domclass=" + domclass);
	this._data = null;
	this.mapElement = null;
	this.radius = radius;
	try {
		this.dom = document.createElement(domtag);
		this.dom.setAttribute("class", domclass);
		this.point = point;
		document.body.appendChild(this.dom);
		// console.log("构造方法内，dom=" + this.dom);
	} catch (err) {
		console.error(err);
	}
}

MapDomWrap.prototype.getDom = function() {
	return this.dom;
}

MapDomWrap.prototype.getData=function(){
	return this._data;
}

MapDomWrap.prototype.getCenter = function() {
	return this.point;
}

MapDomWrap.prototype.show = function() {
	if (this.dom) {
		this.dom.style.display = "";
	}
}
MapDomWrap.prototype.hide = function() {
	if (this.dom) {
		this.dom.style.display = "none";
	}
}

MapDomWrap.prototype.setLen=function(len){
	if(typeof len==="number"){
		if(this.dom){
			this.dom.style.width=len+"px";
			this.dom.style.height=len+"px";
		}
	}
}

MapDomWrap.prototype.getLen=function(){
	if(this.dom){
		var w=this.dom.style.width;
		if(w){
			w=w.substring(0,w.indexOf("px"));
			return w;
		}
	}
	return 0;
}

MapDomWrap.prototype.getIsShow = function() {
	if (this.dom.style.display == "none") {
		return false;
	} else {
		return true;
	}
}

MapDomWrap.prototype.setFillColor = function(color) {
	if (this.dom && color != null && color != undefined) {
		this.dom.style.background = color;
		this.dom.style.opacity = 1;
		this.dom.style.filter = "alpha(opacity=1)";
	}
}
MapDomWrap.prototype.setOpacity = function(opacity) {
	if (this.dom && opacity != null && opacity != undefined) {
		this.dom.style.background = 'none';
		this.dom.style.opacity = opacity;
		this.dom.style.filter = "alpha(opacity="+opacity+")";
	}
}
MapDomWrap.prototype.addEventListener = function(eventName, callback) {
	var me = this;
	if (this.dom && typeof callback == "function") {
		$(this.dom).on(eventName, function(event) {
			callback(me, event);
		});
	}
}

// ---------综合类---------------//
function DtSampleContainer(cellLib, minZoom, randomShowCnt, colors,
		responseFunction) {
	this.cellLib = cellLib;
	this.map = this.cellLib.getMap();
	this.minZoom = minZoom ? minZoom : 16;
	this.randomShowCnt = randomShowCnt ? randomShowCnt : 50;
	this.singleColor = colors ? (colors['singleColor'] ? color['singleColor']
			: "#0B4C5F") : '#0B4C5F';
	/*this.multiColor = colors ? (colors['multiColor'] ? color['multiColor']
			: "#FF00FF") : '#FF00FF';*/
	this.multiColor = colors ? (colors['multiColor'] ? color['multiColor']
	: "#0DC90D") : '#0DC90D';
	var defaultClickFunction = function(shape, event) {
		//console.log("click : shape=" + shape);
		//alert(shape._data.getDetail());
		shape._data.getDetail();
	};
	this.clickFunction = responseFunction ? (responseFunction['clickFunction'] ? responseFunction['clickFunction']
			: defaultClickFunction)
			: defaultClickFunction;
	var defaultMouseoverFunction = function() {
	};
	this.mouseoverFunction = responseFunction ? (responseFunction['mouseoverFunction'] ? responseFunction['mouseoverFunction']
			: defaultMouseoverFunction)
			: defaultMouseoverFunction;
	this.mouseoutFunction = responseFunction ? (responseFunction['mouseoutFunction'] ? responseFunction['mouseoutFunction']
			: defaultMouseoverFunction)
			: defaultMouseoverFunction;

	this.preZoom = minZoom;
	this.DiffAzimuth = 1;
	this.DiffDistance = 5;
	this.firstTime = true;
	this.lastOperTime = 0;

	this.isComposeSamples = new Array();// 元素是：IsComposeSample
	this.mapElements = new Array();// 元素是：MapElement
	this.visibles = new Array();
	this.hiddens = new Array();
	this.specials = new Array();// 特殊渲染的外观
	this.extraMapOverlays = new Array();// 额外的覆盖物。与specialPolygons的区别是：specialPolygons指的还是小区，而这个extraMapOverlays是额外生成的，如用于标识小区的label覆盖物

	this.servercellToMapElements = new Object();// 服务小区到图元的映射.key为服务小区名称，value为mapelement列表
	this.lnglatToMapElement = new Object();// 以lng+"_"+lat为key，value为对应的mapElement
	this.cellToMapLnglat = new Object();// key:cellname,value:{cellMapLng,cellMapLat}采集点对应的服务小区名称数组

	this.cellNames=new Array();// cellNames记录的是小区依次出现的顺序，重点体现切换顺序。里面可能会有重复的。如cell1->cell2->cell3->cell2
	
	this.mapContOffsetLeft = 0;
	this.mapContOffsetTop = 0;
}

/**
 * 设置地图容器的offset
 */
DtSampleContainer.prototype.setMapContOffset = function(offsetLeft, offsetTop) {
	this.mapContOffsetLeft = offsetLeft;
	this.mapContOffsetTop = offsetTop;
}

/**
 * 设置维护小区信息的js lib
 * 
 * @param cellLib
 *            GisCellDisplayLib对象
 */
DtSampleContainer.prototype.setCellLib = function(cellLib) {
	this.cellLib = cellLib;
}

DtSampleContainer.prototype.getMapElementsLen=function(){
	return this.mapElements.length;
}
/**
 * 根据数据创建地图图元（圆圈）
 * 
 * @param isComposeSample
 */
DtSampleContainer.prototype.createMapElementFromIsComposeSample = function(
		isComposeSample) {
	if (!isComposeSample) {
		console.error("createMapElementFromIsComposeSample传入的参数是null！");
		return null;
	}
	try {
		var me = this;
		var center = isComposeSample.getCenterPoint();
		var radius = isComposeSample.getRadius();
		var option = new Object();
		if (isComposeSample.get("sampleCnt") > 1) {
			option['fillColor'] = this.singleColor;
		} else {
			option['fillColor'] = this.multiColor;
		}
		//圆圈
		 var elementShape = new BMap.Circle(center, radius, {
		 strokeWeight : 1,fillColor:option['fillColor']
		 });
//		console.log("elementShape圆圈====="+elementShape.getCenter().lng+"---"+elementShape.getFillColor());
		// console.log("创建mapdomwrap：center=(" + center.lng + "," + center.lat
		// + "),radius=" + radius + "px,domclass="
		// + isComposeSample.getDomClass());
		//div函数暂时注销
		/*var elementShape = new MapDomWrap(radius, "div", center,
				isComposeSample.getDomClass());*/
		// console.log("创建结果：" + elementShape);
		var mapElement = new MapElement(elementShape, isComposeSample);
		
		elementShape.addEventListener('click', function(event) {
			me.clickFunction(isComposeSample, event);
		});
		elementShape.addEventListener('mouseover', function(event) {
			me.mouseoverFunction(isComposeSample, event);
		});
		elementShape.addEventListener('mouseout', function(event) {
			me.mouseoutFunction(isComposeSample, event);
		});
		//div函数暂时注销
		/*elementShape.addEventListener('click', function(event) {
			me.clickFunction(elementShape, event);
		});
		elementShape.addEventListener('mouseover', function(event) {
			me.mouseoverFunction(elementShape, event);
		});
		elementShape.addEventListener('mouseout', function(event) {
			me.mouseoutFunction(elementShape, event);
		});*/

//		console.log("生成的mapElement = "+mapElement);
		return mapElement;
	} catch (err) {
		console.error(err);
		return null;
	}
}

/**
 * 根据所属经纬度调整自定义的地图dom元素到地图合适的位置
 * 
 * @param mapDomWrap
 */
DtSampleContainer.prototype.adjustMapDomWrapperPosition = function(mapDomWrap) {
	if (!mapDomWrap || !mapDomWrap instanceof MapDomWrap) {
		return;
	}
	// console.log("--------------begin ");
	// console.log("----b4 move....left:"+dom.style.left+",top="+dom.style.top);
	var position = this.map.pointToPixel(mapDomWrap.point);
	// var length = mapDomWrapper.dom.clientWidth ||
	// mapDomWrapper.dom.offsetWidth;
	var length =mapDomWrap.getLen();// mapDomWrap.radius ? mapDomWrap.radius :
									// 0;
	// console.log("----point=("+residePoint.lng+","+residePoint.lat+"),position=("+position.x+","+position.y+"),length="
	// + length);
	mapDomWrap.dom.style.left = (this.mapContOffsetLeft + position.x - length / 2)
			+ "px";
	mapDomWrap.dom.style.top = (this.mapContOffsetTop + position.y-length / 2)
			+ "px";

	// console.log("----aft move:left="+dom.style.left+",top="+dom.style.top);
	// console.log("--------------end");
}

// 判断图元是否要显示
DtSampleContainer.prototype.shouldDisplay = function(mapElement) {
	// 如果不在可见区域内，肯定不显示
	var visib = false;
	var map = this.map;
	if (!map.getBounds().containsPoint(mapElement.getCenter())) {
		visib = false;
	} else {
		// 结合缩放级别、可见区域
		// if (map.getZoom() < this.minZoom) {
		// // 在不需要全部显示的范围内
		// // 看一下随机显示的数量达到上限没有
		// if (this.visibles.length < this.randomShowCnt) {
		// visib = true;
		// } else {
		// visib = false;
		// // console.log("超过随机显示数量");
		// }
		// } else {
		// // 在可见区域内，且在需要全部显示的缩放级别范围内
		// visib = true;
		// }
		visib = true;
	}
	// console.log("visible == "+visib);
	return visib;

}

/**
 * 修改图元对应的外观
 */
DtSampleContainer.prototype.changeMapElementOutlook = function(mapElement,
		option, putspec) {
	if (!mapElement) {
		return;
	}

	if (!option) {
		return;
	}
	// 应用新外观
	if (mapElement.applyOption(option)) {
		//div函数暂时注销
//		if (mapElement.getIsShow()) {
			/*this.map.removeOverlay(mapElement.getElementShape());
			this.map.addOverlay(mapElement.getElementShape());*/
		mapElement.getElementShape().setFillColor(option['fillColor']);
//		}
		if (putspec) {
			this.specials.push(mapElement);
		}
	}

}

/*
 * 两点之间画线
 */
DtSampleContainer.prototype.drawLines = function(point1, point2, option,handler) {
	if(!point1 || !point2){
		return;
	}
	try {
		// 如果该连线已经存在，则不添加
		var extraMapOverlays = this.extraMapOverlays;
		var oldLine, ps;
		var need = true;
		for ( var i = 0; i < extraMapOverlays.length; i++) {
			oldLine = extraMapOverlays[i];
			if (oldLine) {
				ps = oldLine.getPath();
				if (ps) {
					if (point1.equals(ps[0]) && point2.equals(ps[1])
							|| point1.equals(ps[1]) && point2.equals(ps[0])) {
						// console.log("已经存在该线");
						need = false;// 已经存在
					}
				}
			}
		}

		if (!need) {
			return;
		}

		// 画一条线
		var pline = new BMap.Polyline([ point1, point2 ], option, {
			strokeWeight : 1
		});
		this.map.addOverlay(pline);
		this.extraMapOverlays.push(pline);
		
		if(typeof handler=="function"){
			pline.addEventListener("click",handler);
		}
	} catch (err) {
		console.error(err);
	}
}

DtSampleContainer.prototype.clearData = function() {
	this.isComposeSamples.splice(0, this.isComposeSamples.length);
	var map = this.map;

	var mapElements = this.mapElements;
	for ( var i = 0; i < mapElements.length; i++) {
		// map.removeOverlay(mapElements[i].getElementShape());
		$(mapElements[i].getElementShape().getDom()).remove();
	}
	mapElements.splice(0, mapElements.length);

	// 可见
	this.visibles.splice(0, this.visibles.length);

	// 隐藏
	this.hiddens.splice(0, this.hiddens.length);

	// 特殊、额外图元
	this.specials.splice(0, this.specials.length);

	// 额外
	var extraMapOverlays = this.extraMapOverlays;
	for ( var i = 0; i < extraMapOverlays.length; i++) {
		map.removeOverlay(extraMapOverlays[i]);
	}
	extraMapOverlays.splice(0, extraMapOverlays.length);

	// hash表
	this.servercellToMapElements = null;
	this.servercellToMapElements = new Object();

	this.lnglatToMapElement = null;
	this.lnglatToMapElement = new Object();
	
	
	this.cellToMapLnglat=null;
	this.cellToMapLnglat=new Object();
	// 关闭信息窗
	// this.closeInfoWindow();
	
	this.cellNames.splice(0, this.cellNames.length);
}

/**
 * 只清除额外覆盖物
 */
DtSampleContainer.prototype.clearOnlyExtraOverlay = function() {
	var map = this.map;
	var extraMapOverlays = this.extraMapOverlays;
	for ( var i = 0; i < extraMapOverlays.length; i++) {
		map.removeOverlay(extraMapOverlays[i]);
	}
	extraMapOverlays.splice(0, extraMapOverlays.length);
}

/**
 * 将sample数组显示在地图上
 * 
 * 考虑到是时间有序的
 */
DtSampleContainer.prototype.showOnMap = function(sampleArray) {
	try {
		var isComposeSamples = this.isComposeSamples;
		var mapElements = this.mapElements;
		var visibles = this.visibles;
		var hiddens = this.hiddens;
		var servercellToMapElements = this.servercellToMapElements;
		var map = this.map;
		var multiColor = this.multiColor;
		var singleColor = this.multiColor;
		var cellToMapLnglat = this.cellToMapLnglat;
		// var start = composeMarkers.length;// 新获取的小区对象在数组中的起始位置
		var composeSample;
		var cell = "";
		var lng, lat;
		var sample;
		var mapElement;
		var lnglatkey = "";
		var elementShape = null;
		var tmp = null;
		var len=this.getPixelLen(map.getZoom());
		
		
		var cellNames=this.cellNames;
		// O(n*n)
		for ( var i = 0; i < sampleArray.length; i++) {
			var sample = sampleArray[i];
			if (!sample) {
				console.warn("sample undefined!");
				return;
			}
			cell = sample['cell'];
			// console.log("cell is ="+cell);
			if (!cell) {
				console.warn("cell undefined!");
				//continue;
			}

			// cellNames记录的是小区依次出现的顺序，重点体现切换顺序。里面可能会有重复的。如cell1->cell2->cell3->cell2
			if(cell){
			if(cellNames.length==0){
				cellNames.push(cell);//
			}else{
				if(cellNames[cellNames.length-1]!=cell){
					cellNames.push(cell);
				}
			}
			}
			lng = sample['lng'];
			lat = sample['lat'];
			if (!lng || !lat) {
				console.warn("lng or lat undefined!");
				continue;
			}
			if (typeof (lng) != "number" || typeof (lat) != "number") {
				console.warn("lng or lat should be number!");
				continue;
			}
			lnglatkey = lng + "_" + lat;
			mapElement = this.lnglatToMapElement[lnglatkey];// 是否已经存在同经纬度
			if (!mapElement) {
				// 尚未有同经纬度的
				composeSample = new IsComposeSample(sample);
				// elementShape=new
				// BMap.Circle(composeSample.getCenterPoint(),composeSample.getRadius(),{strokeWeight:
				// 1});
				// mapElement=new MapElement(elementShape,composeSample);
				mapElement = this
						.createMapElementFromIsComposeSample(composeSample);
//				 console.log("create ：" + mapElement);
				if (mapElement) {
					//div函数暂时注销
					/*mapElement
					.getElementShape().setLen(len);*/
					if (this.shouldDisplay(mapElement)) {
//						 console.log("visible");
						//div函数暂时注销
						/*this.adjustMapDomWrapperPosition(mapElement
								.getElementShape());*/
						mapElement.show(this.map);
						visibles.push(mapElement);
					} else {
						hiddens.push(mapElement);
					}
				}
			} else {
				composeSample = mapElement.getElementData();
				if (composeSample) {
					composeSample.addSample(sample);
				} else {
					continue;
				}
				if (composeSample.getSampleCount() == 2) {
					// 由单一的变为2个
					this.changeMapElementOutlook(mapElement, {
						'fillColor' : multiColor
					}, false);
				}
			}

			if(cell){
			// 与服务小区的关系.可以根据服务小区快速找到采样
			tmp = servercellToMapElements[cell];
			if (!tmp) {
				tmp = new Array();
				servercellToMapElements[cell] = tmp;
			}
			tmp.push(mapElement);
			mapElements.push(mapElement);

			if (!cellToMapLnglat[cell]) {
				tmp = {
					'cellMapLng' : sample.cellMapLng,
					'cellMapLat' : sample.cellMapLat,
					'avgDistance':sample.avgDistance
				};
				cellToMapLnglat[cell] = tmp;
				
			}
			}
		}
		// cellNames=this.getCellNamesArrByCellsObj();
	} catch (err) {
		console.error(err);
	}
}

/**
 * 处理缩放结束事件
 * 
 * @param e
 */
DtSampleContainer.prototype.handleZoomEnd = function(e, occurTime) {

	var me = this;
	if (occurTime != null && occurTime != undefined) {
		me.lastOperTime = occurTime;
	}

	

	var map = this.map;
	var visibles = this.visibles;
	var hiddens = this.hiddens;
	var mapElements = this.mapElements;
	var newzoom = map.getZoom();

	// console.log("handlemoveend map="+map+"
	// this.map="+this.map+",visibles="+this.visibles);
	var bounds = map.getBounds();
	visibles.splice(0, visibles.length);
	var len=this.getPixelLen(map.getZoom());
	for ( var i = 0; i < mapElements.length; i++) {
		// console.log("move : i=" + i);
		//div函数暂时注销
		/*mapElements[i].getElementShape().setLen(len);
		this.adjustMapDomWrapperPosition(mapElements[i].getElementShape());*/
		if (!bounds.containsPoint(mapElements[i].getCenter())) {
			mapElements[i].getElementShape().hide();
			hiddens.push(mapElements[i]);
		} else {
			mapElements[i].getElementShape().show();
			visibles.push(mapElements[i]);
		}
	}

	this.preZoom = newzoom;
}

/**
 * 处理移动结束事件
 * 
 * @param e
 */
DtSampleContainer.prototype.handleMoveEnd = function(e, occurTime) {
	var me = this;
	if (occurTime != null && occurTime != undefined) {
		me.lastOperTime = occurTime;
	}
	// var ct = new Date().getTime();
	// if (ct - me.lastOperTime < me.minResponseInterval) {
	// // 开始处理缩放事件
	// window.setTimeout(function() {
	// me.handleMoveEnd(e);
	// }, 500);
	// return;
	// }

	var map = this.map;
	var visibles = this.visibles;
	var hiddens = this.hiddens;
	var mapElements = this.mapElements;

	// console.log("handlemoveend map="+map+"
	// this.map="+this.map+",visibles="+this.visibles);
	var bounds = map.getBounds();
	visibles.splice(0, visibles.length);
	var zoom=map.getZoom();
	var len=this.getPixelLen(zoom);
	for ( var i = 0; i < mapElements.length; i++) {
		// console.log("move : i=" + i);
		//div函数暂时注销
		/*mapElements[i].getElementShape().setLen(len);
		this.adjustMapDomWrapperPosition(mapElements[i].getElementShape());*/
		if (!bounds.containsPoint(mapElements[i].getCenter())) {
			mapElements[i].getElementShape().hide();
			hiddens.push(mapElements[i]);
		} else {
			mapElements[i].getElementShape().show();
			visibles.push(mapElements[i]);
		}
	}
}

// 获取距离-------临时
DtSampleContainer.prototype.getPixelLen=function(zoom){
	// console.log("getPixelLen.zoom="+zoom);
	var len=10;
	if(zoom>=18){
		len=20;
	}else if(zoom==17){
		len=18;
	}else if(zoom==16){
		len=14;
	}
	else {
		len= 10;
	}
	// console.log("zoom="+zoom+"时，返回len="+len)
	return len;
}


/*
 * 尽量获取边上的中心点
 */
DtSampleContainer.prototype.getProperlyCellPoint=function(cell){
	if(!cell){
		return null;
	}
	var pointStr=null;
	var cellPoint=null;
	if(this.cellLib){
		cellPoint=this.cellLib.getCellEdgeCenterPoint(cell);
	}
	if(cellPoint==null){
		pointStr=this.cellToMapLnglat[cell];
	
		if(pointStr!=null && pointStr!=undefined){
			try{
			cellPoint=new BMap.Point(pointStr.cellMapLng,pointStr.cellMapLat);
			}catch(err){
				return null;
			}
		}
	}
	return cellPoint;	
}


/**
 * 统计室分外泄 absoluteStren：绝对信号强度 relativeStren：相对于主服务小区的信号差距
 * 
 * 逻辑： 室分小区做主覆盖 或 室分小区信号强度比主覆盖小区信号强（作为邻区） 或 室分小区信号强度在-80db以上（作为邻区）
 * 
 * 效果： 按信号强度规则渲染采样点 统计出来以后，将室分小区和由于其外泄影响到的采样点进行连线
 */
DtSampleContainer.prototype.staticsIndoorSignalLeakOutside = function(
		absoluteStren, relativeStren,rule) {
	if(this.cellLib){
		this.cellLib.resetSpecPolygonToDefaultOutlook();
	}
	this.clearOnlyExtraOverlay();

	absoluteStren = arguments[0] ? new Number(arguments[0]) : -80;
	relativeStren = arguments[1] ? new Number(arguments[1]) : 12;

 //console.log("absoluteStren=" + absoluteStren + ",relativeStren="
// + relativeStren);
	if (isNaN(absoluteStren) || isNaN(relativeStren)) {
		alert("请传入数字作为参数！");
		return;
	}
	var composes = null;
	var samples = null;
	var sample = null;
	var ok = false;
	
	// var servercellToMapElements = this.servercellToMapElements;
	var mapElements = this.mapElements;
	var cellToMapLnglat=this.cellToMapLnglat;
	var mapElement = null;
	var cellPoint = null;
	var elementData = null;
	var sampleArray = null;
	var sample = null;
	var ncellcolor=dtopts[0].style.color;
	var servercellcolor=dtopts[1].style.color;
	// '#FF0040','#FE9A2E'
	var option_ncell_line_high = {
		'strokeWeight' : 1,
		'strokeColor' : ncellcolor
	};
	var option_serverCell_line = {
		'strokeWeight' : 1,
		'strokeColor' : servercellcolor
	};
	
	var option = null;
	var renderCell=null;
	var cellOption=null;
	for ( var j = 0; j < mapElements.length; j++) {
		try {
			ok = false;
			mapElement = mapElements[j];
			if (!mapElement) {
				continue;
			}
			composes = mapElement.getElementData();
			if (!composes) {
				continue;
			}
			samples = composes.getSampleArray();
			if (!samples) {
				continue;
			}
			for ( var k = 0; k < samples.length; k++) {
				sample = samples[k];
				if (!sample) {
					continue;
				}
// console.log("sample.ncellIndoors=="+sample.ncellIndoors);
				cell=sample['cell'];
// if(cell=="S5AGSY2"){
// console.log("focus on it.");
// }
				
				if (sample['cellIndoor'] == "Y" || sample['cellIndoor'] == 'y') {
					ok = true;
					// console.log("find one!!!!");
					option = option_serverCell_line;
					cellPoint=this.getProperlyCellPoint(cell);
					renderCell=cell;
					cellOption=option_serverCell;
					break;
				}
				
				if (sample.ncellIndoors.indexOf("Y") >= 0
						|| sample.ncellIndoors.indexOf("y") >= 0) {
// console.log("one of the ncell is indoor
// cell。sample.ncells="+sample.ncells+",sample.ncellIndoors="+sample.ncellIndoors)
					var nids = sample.ncellIndoors.split(",");
					var nrxlev = sample.ncellRxLevSubs.split(",");
					for ( var m = 0; m < nids.length; m++) {
						if (nids[m] == "Y" || nids[m] == 'y') {
							try {
								if (nrxlev[m] > absoluteStren
										|| (nrxlev[m] >= sample.rxLevSub
												- relativeStren || nrxlev[m] < sample.rxLevSub
												+ relativeStren)) {
									ok = true;
// console.log("find ncell");
									option = option_ncell_line_high;
									var ncellnames=sample.ncells.split(",");
									renderCell=ncellnames[m];
									cellOption=option_ncell;
									cellPoint=this.getProperlyCellPoint(ncellnames[m]);
									break;
								}
							} catch (err) {
								console.error(err);
							}
						}
					}
				}
			}
			if (ok && cellPoint!=null && mapElement.getCenter()!=null) {
				// 连线
				this.drawLines(cellPoint, mapElement.getCenter(), option);
				if(this.cellLib){
					this.cellLib.changeCellPolygonOptions(renderCell,cellOption,true);
				}
			}
		} catch (err) {
			console.error(err);
		}
	}

}


/**
 * 覆盖过远
 */
DtSampleContainer.prototype.staticsOverCoverage=function(rule){
	if(this.cellLib){
		this.cellLib.resetSpecPolygonToDefaultOutlook();
	}
	var dtopts = rule.getRuleSettings();
	var cells=this.servercellToMapElements;// 封装的小区，每个小区下有N个采集对象元素
	var cellNames=this.cellNames;
// for(var cell in cells){
// cellNames.push(cell);
// }
	var me=this;
	// var cellNames=this.getCellNamesArrByCellsObj();
	// console.log("cellNames: "+ cellNames);
	var cnt=0;// 小区比对总数
	var avgofncelldistance=0;// 其他小区平均值

	var mapElements=null;
	// dtopts.overcoverage[1].style[0].color;// 过覆盖颜色线
	// dtopts.overcoverage[1].style[0].strokeWeight;
	// dtopts.overcoverage[1].multiple;// 倍数
	// 降序
	var color=dtopts[0].style.color;// 过覆盖颜色线
	var weight = dtopts[0].style.strokeWeight;
	var opacity =  dtopts[0].style.strokeOpacity;
	var multiple=dtopts[0].params.multiple;// 倍数
    var option={strokeColor:color,strokeWeight:weight,strokeOpacity:opacity};// 过覆盖颜色填充对象
    // console.log(color);
    
    var cnt=0;
	var sumdis=0;
	var ncellavg=null;
	var serverCell=null;
	var serverCellAvg=0;
	for(var i=0;i<cellNames.length;i++){
// var baseavgDistance=this.setCell(i,cells,cellNames);
// mapElements=cells[cellNames[i]];
// cell1_L=this.setCell(i-2,cells,cellNames);
// cell2_L=this.setCell(i-1,cells,cellNames);
// cell3_L=this.setCell(i+1,cells,cellNames);
// cell4_L=this.setCell(i+2,cells,cellNames);
// if(cell1_L!=null)cnt++,avgofncelldistance+=cell1_L;
// if(cell2_L!=null)cnt++,avgofncelldistance+=cell1_L;
// if(cell3_L!=null)cnt++,avgofncelldistance+=cell1_L;
// if(cell4_L!=null)cnt++,avgofncelldistance+=cell1_L;
//		
// if((avgofncelldistance/cnt)/4*multiple<baseavgDistance){
// for(var j=0;j<mapElements.length;j++){
// var isComposeSample=mapElements[j].getElementData();
// var sampleArray = isComposeSample.getSampleArray();
// // var lng=sampleArray[0]['lng'];//采集点坐标经度
// // var lat=sampleArray[0]['lat'];//采集点坐标纬度
// // var point1=new BMap.Point(sampleArray[0].cellMapLng,
// // sampleArray[0].cellMapLat);
// var point1=this.getProperlyCellPoint(sampleArray[0].cell);
// var point2=isComposeSample.getCenterPoint();
// this.drawLines(point1,point2,option,alertLineDistance);
// if(this.cellLib){
// this.cellLib.changeCellPolygonOptions(sampleArray[0].cell,option_serverCell,true);
// }
// }
// }
		
		cnt=0;
		sumdis=0;
	    ncellavg=null;
		serverCell=cellNames[i];
		serverCellAvg=this.cellToMapLnglat[serverCell];
		if(!serverCellAvg){
			continue;
		}
		serverCellAvg=serverCellAvg.avgDistance;
		if(!serverCellAvg){
			continue;
		}
		ncellavg=this.getNCellAvgDistance(i-2,serverCell,false);
		if(ncellavg!=null){
			cnt++;
			sumdis+=ncellavg;
		}
		ncellavg=this.getNCellAvgDistance(i-1,serverCell,false);
		if(ncellavg!=null){
			cnt++;
			sumdis+=ncellavg;
		}
		ncellavg=this.getNCellAvgDistance(i+1,serverCell,true);
		if(ncellavg!=null){
			cnt++;
			sumdis+=ncellavg;
		}
		ncellavg=this.getNCellAvgDistance(i+2,serverCell,true);
		if(ncellavg!=null){
			cnt++;
			sumdis+=ncellavg;
		}
		if(cnt==0){
			continue;
		}
		ncellavg=sumdis/cnt;
		// console.log("server
		// cell="+serverCell+",平均覆盖距离："+serverCellAvg+",相邻小区的平均覆盖距离："+ncellavg);
		// multiple 2.5
		if(serverCellAvg>ncellavg*multiple){
			// console.warn("server cell="+serverCell+" 过覆盖！");
			 mapElements=cells[serverCell];
			for(var j=0;j<mapElements.length;j++){
		  		var isComposeSample=mapElements[j].getElementData();
		  		var sampleArray = isComposeSample.getSampleArray();
		  		var point1=this.getProperlyCellPoint(sampleArray[0].cell);
				var point2=isComposeSample.getCenterPoint();
				this.drawLines(point1,point2,option,alertLineDistance);	
				if(this.cellLib){
					this.cellLib.changeCellPolygonOptions(sampleArray[0].cell,option_serverCell,true);
				}
			}
		}
	}
}


// 获取第index位置的小区的平均覆盖距离，该小区不能等于serverCell
// forword=true,index++向后搜索否则，index--向前搜索
DtSampleContainer.prototype.getNCellAvgDistance=function(index,serverCell,forword){
	if(index<0){
		return null;
	}
	var cellNames=this.cellNames;
	if(index>=cellNames.length){
		return null;
	}
	var ncell=cellNames[index];
	while(ncell==serverCell){
		if(forword){
			index++;
		}else{
			index--;
		}
		if(index<0 || index>=cellNames.length){
			return null;
		}
		
		ncell=cellNames[index];
	}
	if(!ncell){
		return null;
	}
	ncell=this.cellToMapLnglat[ncell];
	if(!ncell){
		return null;
	}
	return ncell.avgDistance;
}

/**
 * 提醒线的长度
 */
function alertLineDistance(e){
	// console.log("line point length:"+e.target.getPath().length);
	if(!e){
		return;
	}
	var map=e.target.getMap();
	var pt=e.target.getPath();
	var dis=0;
	try{
		dis=map.getDistance(pt[0],pt[1]);
		alert("距离："+getValidValue(dis,0,2)+"米");
	}catch(err){
		// console.error(err);
	}
	return dis;
}

	 
// 信号与天线方向不符：采样点与小区的连线方向与小区方位角方向对比，
// 如果超出45度（即默认覆盖波瓣为45度）则认为是覆盖方向不符，
// 如果超出90度，则认为是明显的“背向覆盖”。
/**
 * 异常覆盖分析：信号与天线方向不符 staticsAbnormalCoverage－>staticsSignalAndAntennaNotMatch
 */
DtSampleContainer.prototype.staticsSignalAndAntennaNotMatch=function(rule){
	var mapElements=this.mapElements;
	var isComposeSample=null;
	var angle;// 服务小区到采样点的夹角
	var bearing;// 服务小区的方位角
	var color;
	var sampleArray;
	
//	RendererRule 
	var setting=null;
	for(var i=0;i<mapElements.length;i++){
		isComposeSample=mapElements[i].getElementData();
		sampleArray=isComposeSample.getSampleArray();
			var angle=sampleArray[0]['angle'];
			var bearing=sampleArray[0]['bearing'];
			var cell=sampleArray[0].cell;
			// 服务区百度经纬度
			var cellbaidulng = sampleArray[0].cellMapLng;
			var cellbaidulat = sampleArray[0].cellMapLat;
			var celloption={fillColor:"#1AD183"};
			
			// console.log("cellbaidu="+cellbaidulng+","+cellbaidulat);
// var cellCenter=new BMap.Point(cellbaidulng,cellbaidulat);
			var cellCenter=this.getProperlyCellPoint(cell);
			var differenceangle=getMinAngle(angle,bearing);
			// Math.abs(angle-bearing)
			setting=rule.findRuleSetting(differenceangle);
//			if(setting==null){
//				setting={'style':''};
//			}
			color=setting.style.color;
			var option={strokeColor:color,strokeWeight:1,strokeOpacity:1};
			if(differenceangle<=45){
			    // 正常覆盖
				// dtopts.abnormalcoverage[0].style[0].color
//				color=setting.style.color;
//				if(color==""){
//					color="#993366";
//				}
//				var option={strokeColor:color,strokeWeight:1,strokeOpacity:1};
				continue;
			}
			gisCellDisplayLib.changeCellPolygonOptions(cell, celloption,null);
			//if(differenceangle>=45 && differenceangle<90){
//			// 覆盖方向不符
//				// dtopts.abnormalcoverage[1].style[0].color
//				color=setting.style.color;
////				if(color==""){
////					color="#FF9900";
////				}
//				var option={strokeColor:color,strokeWeight:1,strokeOpacity:1};
				//console.log("mid color is :"+color);
			//}
//			if(differenceangle>90){
//			// 背向覆盖
//				// dtopts.abnormalcoverage[2].style[0].color
//				color=setting.style.color;
////				if(color==""){
////					color="#FF0000";
////				}
//				var option={strokeColor:color,strokeWeight:1,strokeOpacity:1};
//			}
			// 获取mapElement的中心
			var center = mapElements[i].getCenter();
			// 用直线连接center和cellCenter
			this.drawLines(center,cellCenter,option);
		}
		
	}



/**
 * 小区覆盖图展示
 * 
 */

DtSampleContainer.prototype.staticsSingleServerCellCover=function(cell,rule)
{
	var dtopts = rule.getRuleSettings();
	// console.log("staticsSingleServerCellCover="+cell);
	// return;
	// 通过服务小区名找到对应的采样点集合数据
	var mapElements = this.servercellToMapElements[cell];
	if(!mapElements){
		return;
	}
	// console.log("mapElements="+mapElements);
	// 获取cell的位置,由于每个mapElement都有共同的cell,只需要取第一个即可
	// var cellCenter = mapElements[0].getCellPoint(cell);
	// 服务区百度经纬度
	var cellbaidulng = mapElements[0].getElementData().getSampleArray()[0].cellMapLng;
	var cellbaidulat = mapElements[0].getElementData().getSampleArray()[0].cellMapLat;
// var cellCenter=new BMap.Point(cellbaidulng,cellbaidulat);
	var cellCenter=this.getProperlyCellPoint(cell);
	// console.log("cellCenter="+cellCenter);
	// return;
	// console.log("cellCenter="+cellCenter.lng+"----------------"+cellCenter.lat);
	// 设置线段的样式
	var color=dtopts[0].style.color;
	var option = {strokeColor:color,strokeWeight:1,strokeOpacity:1};
	// 遍历mapElements
	for(var i = 0; i < mapElements.length; i++)
	{
		// 获取mapElement的中心
		var center = mapElements[i].getCenter();
		// console.log("center="+center.lng+"----------------"+center.lat);
		// 用直线连接center和cellCenter
		this.drawLines(center,cellCenter,option);

	}
	// 主覆盖小区染色
	if(this.cellLib){
		this.cellLib.changeCellPolygonOptions(cell,option_serverCell,true);
	}

}
/**
 * 采样点小区占用图展示
 * 
 */
DtSampleContainer.prototype.staticsSingleSampleDetail=function(elementShape,rule)
{
	if(!elementShape){
		return;
	}
	var dtopts = rule.getRuleSettings();
	var isComposeSample = elementShape._data;
	if(!isComposeSample){
		return;
	}
	// 获得isComposeSample中的sample数组
	var sampleArray = isComposeSample.getSampleArray();
	// 获取isCompose的坐标centerPoint
	var centerPoint = elementShape.getCenter();
	var cellArray = [];
	var option_line_serverCell = {strokeColor:dtopts[1].style.color,strokeWeight:2,strokeOpactity:1};
	var option_line_ncell={strokeColor:dtopts[0].style.color,strokeWeight:1,strokeOpactity:1};
	
	for(var i = 0; i < sampleArray.length; i++)
	{
		var cell = sampleArray[i].cell;
		var cellPoint=this.getProperlyCellPoint(cell);
		this.drawLines(cellPoint,centerPoint,option_line_serverCell);
		if(this.cellLib){
			this.cellLib.changeCellPolygonOptions(cell,option_serverCell,true);
		}
		
		// 处理邻区
		var ncellstr=sampleArray[i].ncells;
		if(!ncellstr){
			continue;
		}
		var ncellarr=ncellstr.split(",");
		if(ncellarr.length>0){
			for(var j=0;j<ncellarr.length;j++){
				cellPoint=this.getProperlyCellPoint(ncellarr[j]);
				if(cellPoint){
					this.drawLines(cellPoint,centerPoint,option_line_ncell);
				}
				if(this.cellLib){
					this.cellLib.changeCellPolygonOptions(ncellarr[j],option_ncell,true);
				}
			}
		}
	}
}

DtSampleContainer.prototype.staticsSignalStrength=function(rule)
{
	// console.log("staticsSignalStrength入口方法体");
	// 将局部变量mapElements指向调用该方法的对象的mapElements;
	var dtopts = rule.getRuleSettings();
	var mapElements = this.mapElements;
	// 遍历对象mapElements
	for(var i = 0; i < mapElements.length; i++)
	{
		// 取出每一个mapElement的数据
		var isComposeSample = mapElements[i].getElementData();
		
		// 每个isComposeSample可能由多个smaple组成,需要遍历isComposeSample
		var sampleArray = isComposeSample.getSampleArray();
		var temp = 0;
		for(var j = 0; j < sampleArray.length; j++)
		{
			// 取出每个sample的rxLevSub(主服务小区信号强度)
			temp += sampleArray[j]['rxLevSub'];
		}
		temp /= sampleArray.length;
		// 遍历signalstrength，根据max的值改变相应的color;
		var color="#FFFFFF";// 无效点
		for(var m = 0; m < dtopts.length; m++)
		{
			if(dtopts[m].minValue <= temp && temp < dtopts[m].maxValue)
			   
			{
				color = dtopts[m].style.color;
				// console.log("dtopts[m].style.color:"+color);
				break;
			}
		}
		var option = {fillColor:color};
		this.changeMapElementOutlook(mapElements[i],option);
	}
}

DtSampleContainer.prototype.staticsSignalQuality=function(rule){
	var dtopts = rule.getRuleSettings();
	var mapElements=this.mapElements;
	var isComposeSample=null;
	var sampleArray = null;
	var medianval=0;
	// console.log("mapElements.length="+mapElements.length);
	for(var i=0;i<mapElements.length;i++){
		isComposeSample=mapElements[i].getElementData();
		sampleArray = isComposeSample.getSampleArray();
		var temp = 0;
		for(var j=0;j<sampleArray.length;j++){
			// if(j==0){gisCellDisplayLib.panTo(sampleArray[j].lng,
			// sampleArray[j].lat);}
			temp += sampleArray[j]['rxQualSub'];
			// console.log("sampleArray[j]['rxQualSub']="+sampleArray[j]['rxQualSub']);
		}
		temp = temp/sampleArray.length;
		// console.log("temp="+temp);
		var color="#FFFFFF";// 无效点
		for(var m=0;m<dtopts.length;m++){
			if(dtopts[m].minValue<=temp && temp<dtopts[m].maxValue){
				color=dtopts[m].style.color;
				break;
			}
		}
		var option={fillColor:color};
		this.changeMapElementOutlook(mapElements[i],option);
	}
}
DtSampleContainer.prototype.staticsSignalStructure=function(rule){
	var dtopts = rule.getRuleSettings();
	var mapElements=this.mapElements;
	var isComposeSample=null;
	var medianval=0;
	var rxLevSub;
	var rxLevSubmax;
	var rxLevSubmin;
	var ncellRxLevs=new Array();
	var ncellRxLevSubs;
	for(var i=0;i<mapElements.length;i++){
		isComposeSample=mapElements[i].getElementData();
		// option=getSignaleOption(isComposeSample);
		sampleArray = isComposeSample.getSampleArray();
		for(var j=0;j<sampleArray.length;j++){
			rxLevSub=sampleArray[j]['rxLevSub'];
			rxLevSubmax=rxLevSub+12;
			rxLevSubmin=rxLevSub-12;
			ncellRxLevSubs=sampleArray[j]['ncellRxLevSubs'];
			ncellRxLevs=ncellRxLevSubs.split(",");
			for(var k=0;k<ncellRxLevs.length;k++){
				if(rxLevSubmin<=ncellRxLevs[k] && ncellRxLevs[k]<rxLevSubmax){medianval++;}
			}
		}
		// console.log("medianval="+medianval);
		var color="#FFFFFF";// 无效点
		for(var m=0;m<dtopts.length;m++){
			if(dtopts[m].minValue<=medianval && medianval<dtopts[m].maxValue){
				color=dtopts[m].style.color;break;
			}
		}
		medianval=0;// 重新初始化
		var option={fillColor:color};
		this.changeMapElementOutlook(mapElements[i],option);
	}
}

/**
 * 全路段小区覆盖图
 */
DtSampleContainer.prototype.staticsPrimaryService=function(rule){
	var dtopts = rule.getRuleSettings();
	// 与服务小区的关系.可以根据服务小区快速找到采样点
	// 服务小区到图元的映射.key为服务小区名称，value为mapelement列表
	var cellmapElements=this.servercellToMapElements;
	var cellToMapLnglat=this.cellToMapLnglat;
	// var mapElements=new Array();
// for(var cell in cellmapElements){
// cellnames.push(cell);
// //mapElements.push(cellmapElements[cell]);
// }
	// 小区原点坐标
	// var point1=new BMap.Point(cell.getLng, cell.getLat);
	var isComposeSample=null;
	
　　 var lng;
    var lat;
   
    var strokeColor=dtopts[0].style.color;
    var strokeWeight = dtopts[0].style.strokeWeight;
    var strokeOpacity = dtopts[0].style.strokeOpacity;
    var option_line_serverCell = {strokeColor:strokeColor,strokeWeight:strokeWeight,strokeOpacity:strokeOpacity};
    //var option_line_serverCell={'strokeWeight':1,'strokeColor':color};
    var mapElements;
    var isComposeSample=null;
    // 服务小区
    var cellPoint;
    var samplePoint;
    var point2;
    
    for(var cell in cellToMapLnglat){
    	mapElements=cellmapElements[cell];
    	if(!mapElements){
    		// console.log("cell=="+cell+" 不存在！采样信息");
    		continue;
    	}
    	// 服务小区的坐标点
    	cellPoint=this.getProperlyCellPoint(cell);
    	if(this.cellLib){
    		this.cellLib.changeCellPolygonOptions(cell,option_serverCell,true);
    	}
		for(var m=0;m<mapElements.length;m++){
		
			// 服务小区首个采样点
			isComposeSample=mapElements[0].getElementData();
			
			sampleArray = isComposeSample.getSampleArray();
			samplePoint=mapElements[0].getCenter();
			this.drawLines(cellPoint,samplePoint,option_line_serverCell);
			
			samplePoint=mapElements[mapElements.length-1].getCenter();
			this.drawLines(cellPoint,samplePoint,option_line_serverCell);
			break;
		}
		
	}
}

DtSampleContainer.prototype.staticsNoMainCoverage=function(rule){
	var dtopts = rule.getRuleSettings();
	var mapElements=this.mapElements;
	for(var i=0;i<mapElements.length;i++){
		var isComposeSample=mapElements[i].getElementData();
		var sampleArray = isComposeSample.getSampleArray();
		// option=getSignaleOption(isComposeSample);
		for(var j=0;j<sampleArray.length;j++){
			var rxLevSub=sampleArray[j]['rxLevSub'];
			var ncellRxLevSubs=sampleArray[j]['ncellRxLevSubs'];
			var ncellRxLevs=ncellRxLevSubs.split(",");
			var medianval = 0;
			for(var k=0;k<ncellRxLevs.length;k++){
				// dtopts.nomaincoverage[1].dbcomparaval
				if(dtopts[0].params.dbcomparaval>=Math.abs(rxLevSub-ncellRxLevs[k])){
					medianval++;
					}
			}
		}
		var color;
		var option;
			// dtopts.nomaincoverage[1].cellnum
		if(dtopts[0].params.cellnum<=medianval){
			// dtopts.nomaincoverage[0].style[0].color
			color=dtopts[0].style.color;
			
		}
		else{
			color="#FFFFFF";//正常覆盖点以白色表示
		}
		option={fillColor:color};
		this.changeMapElementOutlook(mapElements[i],option);
		
	}
}
/**
 * 信号快速衰减分析
 * 
 */
DtSampleContainer.prototype.staticsRapidAttenuation=function(rule){
	var dtopts = rule.getRuleSettings();
	var mapElements=this.mapElements;
	var isComposeSample=null;
	var sampleArray;
	var medianval=0;
	var rxLevSub;
	var onesampleobj;
	var sampleTimeStr;
	var accordTimeStr;// 符合条件的
	var allSampleobj=new Array();// 5秒内的Sample对象
	var accordMapElements=new Array();// 5秒内的采集点对象
	var startsampleTimeStr;
	var flag=true;// 是否初始化起始时间
	var isMeet=true;// 是否满足信号强度衰减 15db 以上的要求
	var isNotMeetIndex;// 不满足要求的索引值
	var rxLevSubmax;
	var cur_i;
	var color;
	var option={strokeColor:color,strokeWeight:1,strokeOpactity:1};
	var sampleArray;
	var cell=null;
	var cellPoint=null;
	var centerPoint=null;
		// 设定线条的样式
	// var option = {strokeColor:"red",strokeWeight:1,strokeOpactity:1};
	for(var i=0;i<mapElements.length;i++){
		cur_i=i;
		isComposeSample=mapElements[i].getElementData();
		// option=getSignaleOption(isComposeSample);
		sampleArray=isComposeSample.getSampleArray();
		// console.log("sampleArray="+sampleArray);
		// 一个图元对应一个采样点
			rxLevSub=sampleArray[0]['rxLevSub'];
				sampleTimeStr=sampleArray[0]['sampleTimeStr'];
				onesampleobj=sampleArray[0];
			if(flag==true){startsampleTimeStr=sampleTimeStr;flag=false;}
		if(new Date(sampleTimeStr.replace(/[-]/g, "/"))<=new Date(getNewDateTime(startsampleTimeStr,dtopts[0].params.second))){
			// console.log("满足5秒之内");
			allSampleobj.push(onesampleobj);
		  	accordMapElements.push(mapElements[i]);
		  }else{
		    flag=true;
		  	// console.log("不满足5秒");
		  	// console.log("allSampleobj.length＝"+allSampleobj.length);
		    if(allSampleobj.length>=2){
		  	for(var m=1;m<allSampleobj.length;m++){
			   var dbcomparaval=Math.abs(allSampleobj[m].rxLevSub- allSampleobj[0].rxLevSub);
		  	   // console.log("第一个强度和某个强度之差比较＝"+dbcomparaval);
		  	   // dtopts.rapidattenuation[1].dbattenuationval
		  	   if(dbcomparaval<dtopts[0].params.dbattenuationval){
		  	   		isMeet=false;
		  	   		isNotMeetIndex=m;
		  	   		break;
		  	   }else{
		  	   		isMeet=true;
		  	   }
		  	}
		  	if(isMeet==true){
		  		// console.log("满足要求：");
		  	    for(var b=0;b<accordMapElements.length;b++){
			  	    // console.log("accordMapElements[b]="+accordMapElements[b]);
			  		// this.changeMapElementOutlook(accordMapElements[b],option);
			  		color=dtopts[0].style.color;
				    option={strokeColor:color,strokeWeight:1};
				
					// var cellbaidulng = allSampleobj[b].cellMapLng;
					// var cellbaidulat =allSampleobj[b].cellMapLat;
					// var cellPoint=new BMap.Point(cellbaidulng,cellbaidulat);
				    cell=allSampleobj[b]['cell'];
					cellPoint=this.getProperlyCellPoint(cell);
					// 获取isCompose的坐标centerPoint
					centerPoint = accordMapElements[b].getCenter();
					this.drawLines(cellPoint,centerPoint,option);
					if(this.cellLib){
			    		this.cellLib.changeCellPolygonOptions(cell,option_serverCell,true);
			    	}
		  		}	
		  	}else{
		  		i=cur_i-(allSampleobj.length-1)+m-2;
		  		// continue;
		  	}
		  	allSampleobj.length=0;
			accordMapElements.length=0;
			flag=true;
			}else{
			// console.log("采样点对象数组长度小于2");
			// dtopts.rapidattenuation[0].style[0].color
			color=dtopts[0].style.color;
			option={strokeColor:color,strokeWeight:1};
			for(var m=0;m<allSampleobj.length;m++){
			
				cell=allSampleobj[m]['cell'];
			     cellPoint=this.getProperlyCellPoint(cell);
				// 获取isCompose的坐标centerPoint
				centerPoint = accordMapElements[m].getCenter();
				this.drawLines(cellPoint,centerPoint,option);
				if(this.cellLib){
		    		this.cellLib.changeCellPolygonOptions(cell,option_serverCell,true);
		    	}
			}
			flag=true;
			// 如果当前复合点的最大信号强度>-85db且数组长度<2终止当前循环既当前的那一个循环被终止掉但是以后的循环还是被执行不变色
			allSampleobj.length=0;
			accordMapElements.length=0;
			// continue;
			}
		  }
		  
		
	}
}



/**
 * 统计弱覆盖
 */
DtSampleContainer.prototype.staticsWeakCoverage=function(rule){
	/*
	var mapElements=this.mapElements;
	var weakCoverageSample=new Array();
	var accordMapElements=new Array();
	// var tempSample = null;
	// 遍历mapElements,求出每个mapElement的rxLevSub平局值
	for(var i=0; i<mapElements.length; i++)
	{
		var mapElement = mapElements[i];
		var isComposeSample = mapElement.getElementData();
		var sampleArray = isComposeSample.getSampleArray();
		// 判断每个elements之间的距离
		var temp = 0;
		for(var j=0; j<sampleArray.length; j++)
		{
			temp += sampleArray[j]['rxLevSub'];
			// console.log(sampleArray[j]['sampleTimeStr']);
		}
		temp /= sampleArray.length;
		// 如果temp小于临界值，则保存将sampleArray[0]和mapElements[i]分别保存到weakCoverageSample和accordMapElements之中
		// dtopts.weakcoverage[1].rxlevsub->dtopts[0].params.rxlevsub
		if(temp<dtopts[0].params.rxlevsub)
		{
			weakCoverageSample.push(sampleArray[0]);
			accordMapElements.push(mapElement);
			// 如果weakCoverageSample.length>=2,且首末元素的坐标距离是否大于100米,则对accordMapElements的元素进行渲染
			// dtopts.weakcoverage[1].distance->dtopts[0].params.distance
			if(weakCoverageSample.length>=2 && distance(weakCoverageSample[0].lng,weakCoverageSample[0].lat,weakCoverageSample[weakCoverageSample.length-1].lng,weakCoverageSample[weakCoverageSample.length-1].lat)>dtopts[0].params.distance)
			{
				// dtopts.weakcoverage[0].style[0].color-dtopts[0].style.color
				var color=dtopts[0].style.color;
		  		var option={fillColor:color};
				for(var n=0;n<accordMapElements.length;n++)
				{
					this.changeMapElementOutlook(accordMapElements[n],option);
			   	}
			}
		}
		// 否则，将weakCoverageSample和accordMapElements清空
		else
		{
			weakCoverageSample.length=0;
			accordMapElements.length=0;
		}
		
	}*/
	var dtopts = rule.getRuleSettings();
	var mapElements=this.mapElements;
	var accordMapElements=new Array();
	
	var options = new Array();
	//弱覆盖的颜色
	var option = {fillColor:dtopts[0].style.color,fillOpacity:0.6};
	options.push(option);
	//正常覆盖颜色
	option = {fillColor:"#FFFFFF",fillOpacity:0.6};
	options.push(option);
	
	this.cellLib.initOptions(options);
	
	// var tempSample = null;
	// 遍历mapElements,求出每个mapElement的rxLevSub平局值
	for(var i=0; i<mapElements.length; i++){
		var mapElement = mapElements[i];
		var isComposeSample = mapElement.getElementData();
		var sampleArray = isComposeSample.getSampleArray();
		// 判断每个elements之间的距离
		var temp = 0;
		for(var j=0; j<sampleArray.length; j++){
			temp += sampleArray[j]['rxLevSub'];
			// console.log(sampleArray[j]['sampleTimeStr']);
		}
		temp /= sampleArray.length;
		// 如果temp小于临界值，则保存将sampleArray[0]和mapElements[i]分别保存到weakCoverageSample和accordMapElements之中
		// dtopts.weakcoverage[1].rxlevsub->dtopts[0].params.rxlevsub
		if(temp<dtopts[0].params.rxlevsub){
			accordMapElements.push(mapElement);
			// 如果weakCoverageSample.length>=2,且首末元素的坐标距离是否大于100米,则对accordMapElements的元素进行渲染
			// dtopts.weakcoverage[1].distance->dtopts[0].params.distance
				
		}
		// 否则，将accordMapElements清空
		else{
			changeOptions(accordMapElements,dtopts[0].params.distance,options,this);
			this.changeMapElementOutlook(mapElement,options[1]);
			accordMapElements.length=0;
		}
		
	}
	
	//对accordMapElements中剩余的测量点进行判断
	changeOptions(accordMapElements,dtopts[0].params.distance,options,this);
	
	//内部函数，负责根据弱覆盖的条件改变测量点的样式
	function changeOptions(accordMapElements,definedDis,options,obj){
		if(accordMapElements.length>=2 && distance(accordMapElements[0].getElementData().getSampleArray()[0].lng,accordMapElements[0].getElementData().getSampleArray()[0].lat,accordMapElements[accordMapElements.length-1].getElementData().getSampleArray()[0].lng,accordMapElements[accordMapElements.length-1].getElementData().getSampleArray()[0].lat)>definedDis){
			for(var n=0;n<accordMapElements.length;n++){
				obj.changeMapElementOutlook(accordMapElements[n],options[0]);
		   	}
		}else{
			for(var n=0;n<accordMapElements.length;n++){
				obj.changeMapElementOutlook(accordMapElements[n],options[1]);
		   	}
		}
	}
	
}
/**
 * 统计4G弱覆盖
 */
DtSampleContainer.prototype.statics4GWeakCoverage=function(rule){
	console.log("进入statics4GWeakCoverage 4g弱覆盖方法");
	if(this.cellLib){
		this.cellLib.resetSpecPolygonToDefaultOutlook();
	}
	this.clearOnlyExtraOverlay();
	var dtopts = rule;
	var mapElements=this.mapElements;
	var accordMapElements=new Array();
	
	var options = new Array();
	//弱覆盖的颜色
	var option = {fillColor:dtopts[0].style.color,fillOpacity:0.6};
	options.push(option);
	//正常覆盖颜色
	option = {fillColor:"#FFFFFF",fillOpacity:0.6};
	options.push(option);
	//恢复
	this.resetMapElementOutlook({fillColor:"#0DC90D",fillOpacity:0.6});//FFFFFF---> 0DC90D
	this.cellLib.initOptions(options);
	
	// var tempSample = null;
	// 遍历mapElements,求出每个mapElement的rxLevSub平局值
	var cellRsrp,cellRsSinr,areaType,lng,lat;
	//城区 50米
	//临时数组
	var tempCityArray = new Array();
	//满足10个采样点后的最终数组存储
	var finalCityArray = new Array();
	
	//非城区 120米
	//临时数组
	var tempNonCityArray = new Array();
	//满足10个采样点后的最终数组存储
	var finalNonCityArray = new Array();
	
	//高速 240米
	//临时数组
	var tempFreeWayArray = new Array();
	//满足10个采样点后的最终数组存储
	var finalFreeWayArray = new Array();
//	console.log("mapElements.length 的大小为============="+mapElements.length);
//	alert("mapElements.length 的大小为============="+mapElements.length);
	for(var i=0; i<mapElements.length; i++){
		var mapElement = mapElements[i];
		var isComposeSample = mapElement.getElementData();
//		alert("isComposeSample============="+isComposeSample);
		
		var sampleArray = isComposeSample.getSampleArray();
		
		// 判断每个elements之间的距离
		
		var temp = 0;
		for(var j=0; j<sampleArray.length; j++){
			cellRsrp = sampleArray[j]['cellRsrp'];
			
			cellRsSinr = sampleArray[j]['cellRsSinr'];
			areaType= sampleArray[j]['areaType'];
			lng = sampleArray[j]['lng'];
			lat = sampleArray[j]['lat'];
			
			// console.log(sampleArray[j]['sampleTimeStr']);
		}
		// 如果temp小于临界值，则保存将sampleArray[0]和mapElements[i]分别保存到weakCoverageSample和accordMapElements之中
		// dtopts.weakcoverage[1].rxlevsub->dtopts[0].params.rxlevsub
//		console.log(Number(cellRsrp)+"--"+Number(cellRsSinr)+"--"+areaType);
		if(areaType=="城区"){
			
			if(Number(cellRsrp)<-110 || Number(cellRsSinr)<-3){
//				this.changeMapElementOutlook(mapElement,options[0],true);
//				alert("城区 Number(cellRsrp)<-110 || Number(cellRsSinr)<-3");
				
//				finalCityArray.push(mapElement);
					if(tempCityArray.length>=1 && distance(lng,lat,tempCityArray[tempCityArray.length-1].getElementData().getSampleArray()[0].lng,tempCityArray[tempCityArray.length-1].getElementData().getSampleArray()[0].lat)>50){
						
						tempCityArray.push(mapElement);
					}else{
						
						tempCityArray.push(mapElement);
					}
				}
//			}else {
					
					/*if(tempCityArray.length>=2 && distance(tempCityArray[0].getElementData().getSampleArray()[0].lng,tempCityArray[0].getElementData().getSampleArray()[0].lat,tempCityArray[tempCityArray.length-1].getElementData().getSampleArray()[0].lng,tempCityArray[tempCityArray.length-1].getElementData().getSampleArray()[0].lat)>50){
						//将一数组元素插入到另一数组中
						Array.prototype.push.apply(finalCityArray, tempCityArray);
					}*/
//				tempCityArray = [];
//			}
		}else if(areaType=="非城区"){
			if(Number(cellRsrp)<-110 || Number(cellRsSinr)<-3){
//				alert("非城区 Number(cellRsrp)<-110 || Number(cellRsSinr)<-3");
//				this.changeMapElementOutlook(mapElement,options[0],true);
				tempNonCityArray.push(mapElement);
//				finalNonCityArray.push(mapElement);
					if(tempNonCityArray.length>=1 && distance(lng,lat,tempNonCityArray[tempNonCityArray.length-1].getElementData().getSampleArray()[0].lng,tempNonCityArray[tempNonCityArray.length-1].getElementData().getSampleArray()[0].lat)>120){
						
						tempNonCityArray.push(mapElement);
					}else{
						tempNonCityArray.push(mapElement);	
					}
				}
//			}else {
					
					/*if(tempNonCityArray.length>=2 && distance(tempNonCityArray[0].getElementData().getSampleArray()[0].lng,tempNonCityArray[0].getElementData().getSampleArray()[0].lat,tempNonCityArray[tempNonCityArray.length-1].getElementData().getSampleArray()[0].lng,tempNonCityArray[tempNonCityArray.length-1].getElementData().getSampleArray()[0].lat)>120){
						//将一数组元素插入到另一数组中
						Array.prototype.push.apply(finalNonCityArray, tempNonCityArray);
					}
				tempNonCityArray = [];*/
//			}
		}else if(areaType=="高速"){
			if(Number(cellRsrp)<-110 || Number(cellRsSinr)<-3){
//				alert("高速 Number(cellRsrp)<-110 || Number(cellRsSinr)<-3");
//				this.changeMapElementOutlook(mapElement,options[0],true);
				
//				finalFreeWayArray.push(mapElement);
					if(tempFreeWayArray.length>=1 &&　distance(lng,lat,tempFreeWayArray[tempFreeWayArray.length-1].getElementData().getSampleArray()[0].lng,tempFreeWayArray[tempFreeWayArray.length-1].getElementData().getSampleArray()[0].lat)>50){
						tempFreeWayArray.push(mapElement);
					}else{
						tempFreeWayArray.push(mapElement);
					}
				}
//			}else {
				/*if(tempFreeWayArray>=2 &&　distance(tempFreeWayArray[0].getElementData().getSampleArray()[0].lng,tempFreeWayArray[0].getElementData().getSampleArray()[0].lat,tempFreeWayArray[tempFreeWayArray.length-1].getElementData().getSampleArray()[0].lng,tempFreeWayArray[tempFreeWayArray.length-1].getElementData().getSampleArray()[0].lat)>50){
					//将一数组元素插入到另一数组中
					Array.prototype.push.apply(finalFreeWayArray, tempFreeWayArray);
				}
				tempFreeWayArray = [];*/
//			}
		}
	}
	if(tempCityArray.length>0){
		//将一数组元素插入到另一数组中
		Array.prototype.push.apply(finalCityArray, tempCityArray);
	}
	if(tempNonCityArray.length>0){
	//将一数组元素插入到另一数组中
	Array.prototype.push.apply(finalNonCityArray, tempNonCityArray);
	}
	if(tempFreeWayArray.length>0){
		//将一数组元素插入到另一数组中
		Array.prototype.push.apply(finalFreeWayArray, tempFreeWayArray);
		}
//	alert("finalCityArray ==="+finalCityArray.length+"---------finalNonCityArray==="+finalNonCityArray.length+"---finalFreeWayArray==="+finalFreeWayArray.length);
	for ( var k = 0; k < finalCityArray.length; k++) {
		this.changeMapElementOutlook(finalCityArray[k],options[0],true);
	}
	for ( var k = 0; k < finalNonCityArray.length; k++) {
		this.changeMapElementOutlook(finalNonCityArray[k],options[0],true);
	}
	for ( var k = 0; k < finalFreeWayArray.length; k++) {
		this.changeMapElementOutlook(finalFreeWayArray[k],options[0],true);
	}
}
/**
 * 4g室分外泄
 */
DtSampleContainer.prototype.statics4GIndoorSignalLeakOutside = function(
		rule) {
	if(this.cellLib){
		this.cellLib.resetSpecPolygonToDefaultOutlook();
	}
	this.clearOnlyExtraOverlay();
	
	var dtopts = rule;
	var mapElements=this.mapElements;
	var accordMapElements=new Array();
	
	var options = new Array();
	//e频段数组
	var efreqArr=dtopts[0].efreq;
	//室分外泄的颜色
	var option = {fillColor:dtopts[0].style.color,fillOpacity:0.6};
	options.push(option);
	//正常覆盖颜色
	option = {fillColor:"#FFFFFF",fillOpacity:0.6};
	options.push(option);
	//恢复
	this.resetMapElementOutlook({fillColor:"#0DC90D",fillOpacity:0.6});//FFFFFF---> 0DC90D
	this.cellLib.initOptions(options);
	
	// var tempSample = null;
	// 遍历mapElements,求出每个mapElement的rxLevSub平局值
	var cellEarfcn;
	for(var i=0; i<mapElements.length; i++){
		var mapElement = mapElements[i];
		var isComposeSample = mapElement.getElementData();
		var sampleArray = isComposeSample.getSampleArray();
		// 判断每个elements之间的距离
		var temp = 0;
		for(var j=0; j<sampleArray.length; j++){
			cellEarfcn = sampleArray[j]['cellEarfcn'];
			for ( var k = 0; k < efreqArr.length; k++) {
//				alert("cellEarfcn==="+cellEarfcn+"----------ncellearfcn="+efreqArr[k]);
				if(Number(cellEarfcn)==efreqArr[k]){
					this.changeMapElementOutlook(mapElement,options[0],true);
				}
			}
			// console.log(sampleArray[j]['sampleTimeStr']);
		}
		
		
	}
}
/**
 * 4g小区信号重叠覆盖
 */
DtSampleContainer.prototype.statics4GCellSignalOverlap = function(
		rule) {
	if(this.cellLib){
		this.cellLib.resetSpecPolygonToDefaultOutlook();
	}
	this.clearOnlyExtraOverlay();
	
	var dtopts = rule;
	var mapElements=this.mapElements;
	var accordMapElements=new Array();
	
	var options = new Array();
	
	//临时数组
	var tempArray = new Array();
	//满足10个采样点后的最终数组存储
	var finalArray = new Array();
	//弱覆盖的颜色
	var option = {fillColor:dtopts[0].style.color,fillOpacity:0.6};
	options.push(option);
	//正常覆盖颜色
	option = {fillColor:"#FFFFFF",fillOpacity:0.6};
	options.push(option);
	//恢复
	this.resetMapElementOutlook({fillColor:"#0DC90D",fillOpacity:0.6});//FFFFFF---> 0DC90D
	
	this.cellLib.initOptions(options);
	
	// var tempSample = null;
	// 遍历mapElements,求出每个mapElement的rxLevSub平局值
	var cellRsrp,ncellRsrp1,ncellRsrp2,ncellRsrp3,ncellRsrp4,ncellRsrp5,ncellRsrp6;
	for(var i=0; i<mapElements.length; i++){
		var mapElement = mapElements[i];
		var isComposeSample = mapElement.getElementData();
		var sampleArray = isComposeSample.getSampleArray();
		// 判断每个elements之间的距离
		var temp = 0;
		//ncell场强有几个
		var ncellRsrpCnt=0;
		//符合条件的比值数
		var accordRatioCne=0;
		for(var j=0; j<sampleArray.length; j++){
			
			cellRsrp = sampleArray[j]['cellRsrp'];
			
			ncellRsrp1 = sampleArray[j]['ncellRsrp1'];
			ncellRsrp2 = sampleArray[j]['ncellRsrp2'];
			ncellRsrp3 = sampleArray[j]['ncellRsrp3'];
			ncellRsrp4 = sampleArray[j]['ncellRsrp4'];
			ncellRsrp5 = sampleArray[j]['ncellRsrp5'];
			ncellRsrp6 = sampleArray[j]['ncellRsrp6'];
			if (ncellRsrp1 != null && ncellRsrp1 != undefined) {
				ncellRsrpCnt++;
			}
			if (ncellRsrp2 != null && ncellRsrp2 != undefined) {
				ncellRsrpCnt++;
			}
			if (ncellRsrp3 != null && ncellRsrp3 != undefined) {
				ncellRsrpCnt++;			
						}
			if (ncellRsrp4 != null && ncellRsrp4 != undefined) {
				ncellRsrpCnt++;
			}
			if (ncellRsrp5 != null && ncellRsrp5 != undefined) {
				ncellRsrpCnt++;
			}
			if (ncellRsrp6 != null && ncellRsrp6 != undefined) {
				ncellRsrpCnt++;
			}
			//邻小区有4个及4个以上信号强度比主小区的信号强度小于6db以内，连续10个采样点都是如此，则为重叠覆盖路段。
			if(ncellRsrpCnt>=4){
				//主小区RSRP-邻小区RSRP <= 6db
				if(Number(cellRsrp)-Number(ncellRsrp1)<=6){
					accordRatioCne++;
				}
				if(Number(cellRsrp)-Number(ncellRsrp2)<=6){
					accordRatioCne++;
				}
				if(Number(cellRsrp)-Number(ncellRsrp3)<=6){
					accordRatioCne++;
				}
				if(Number(cellRsrp)-Number(ncellRsrp4)<=6){
					accordRatioCne++;
				}
				if(Number(cellRsrp)-Number(ncellRsrp5)<=6){
					accordRatioCne++;
				}
				if(Number(cellRsrp)-Number(ncellRsrp6)<=6){
					accordRatioCne++;
				}
				if(accordRatioCne>=4){
					//临时存储符合条件的图元
//					tempArray.push(sampleArray[j]);
					tempArray.push(mapElement);
					if(tempArray.length>=10){
						//将一数组元素插入到另一数组中
						Array.prototype.push.apply(finalArray, tempArray);
						tempArray = [];
					}
				}else{
					if(tempArray.length>=10){
						//将一数组元素插入到另一数组中
						Array.prototype.push.apply(finalArray, tempArray);
					}
					tempArray = [];
				}
			}else {
				tempArray = [];
				break;
			}
		}
	}
	for ( var k = 0; k < finalArray.length; k++) {
		this.changeMapElementOutlook(finalArray[k],options[0],true);
	}
}
/**
 * 恢复图元外观
 */
DtSampleContainer.prototype.resetMapElementOutlook = function(
		option) {

	if (!option) {
		return;
	}
	for ( var i = 0; i < this.specials.length; i++) {
		// 恢复外观
		this.specials[i].applyOption(option);
//		this.specials[i].applyOpacity(option);
	}

}