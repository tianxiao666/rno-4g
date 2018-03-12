/**
 * 用于在谷歌地球上控制giscell的显示类
 * @param {} map
 * @param {} minZoom
 * @param {} randomShowCnt
 * @param {} colors
 * @param {} responseFunction
 * @param {} configOption
 * @param {} contextMenu
 */
 
//document.write('<script type="text/javascript">google.load("earth","1");</script>');
google.load("earth","1");
function GisCellDisplayLib(ge, minZoom, randomShowCnt, colors,
		responseFunction, configOption,contextMenu) {
	//ge实例变量
	this.ge=ge;
	this.cells = new Array();
	this.showCellLabel = true;// 是否显示小区名称
	
	this.singleColor = colors ? (colors['singleColor'] ? colors['singleColor']
			: "80D800FF") : '80D800FF';//D8D800FF
	this.multiColor = colors ? (colors['multiColor'] ? colors['multiColor']
			: "80D800AA") : '80D800AA';//D8D800AA
	var singleStyle = null;
	var multiStyle = null;
	var circleSingleStyle = null;
	var circleMultiStyle = null;
	var lineSingleStyle = null;
	var options = new Array();	
	this.setOptions = function (newOptions){
		this.options = newDropts;
	};
	this.getOptions = function (){
		return options;
	};
	
	this.setSingleStyle = function (newStyle){
		singleStyle = newStyle;
	};
	this.getSingleStyle = function (){
		return singleStyle;
	};
	
	this.setMultiStyle = function (newStyle){
		multiStyle = newStyle;
	};
	this.getMultiStyle = function (){
		return multiStyle;
	};
	
	this.setCircleSingleStyle = function (newStyle){
		circleSingleStyle = newStyle;
	};
	this.getCircleSingleStyle = function (){
		return circleSingleStyle;
	};
	
	this.setCircleMultiStyle = function (newStyle){
		circleMultiStyle = newStyle;
	};
	this.getCircleMultiStyle = function (){
		return circleMultiStyle;
	};
	
	this.setLineSingleStyle = function (newStyle){
		lineSingleStyle = newStyle;
	};
	this.getLineSingleStyle = function (){
		return lineSingleStyle;
	};	
/*	var defaultClickFunction = function() {
	};
	this.clickFunction = responseFunction ? responseFunction['clickFunction']: defaultClickFunction;
*/	
	var defaultClickFunction = function() {
	};
	this.clickFunction = responseFunction ? (responseFunction['clickFunction'] ? responseFunction['clickFunction']
			: defaultClickFunction)
			: defaultClickFunction;
	this.composeMarkers = new Array();
	this.allPolygons = new Array();
	this.allPolyMarks = new Array();
	this.visiblePolygons = new Array();
	this.specialPolygons = new Array();// 特殊渲染的外观
	this.cellToPolygon = new Object();// 小区到polygon的映射
	this.extraMapOverlays = new Array();// 额外的覆盖物。与specialPolygons的区别是：specialPolygons指的还是小区，而这个extraMapOverlays是额外生成的，如用于标识小区的label覆盖物
	this.cellToCompose = new Object();// 小区到Compose的映射
	this.sameLnglatPolyArray = new Object;// 相同起点经纬度的poly数组，key为：lng+"_"+lat,value为polygon数组
	var defaultrightclickFunction = function(){
	};
	this.rightclickFunction= responseFunction ? (responseFunction['rightclickFunction'] ? responseFunction['rightclickFunction']
			: defaultrightclickFunction)
			: defaultrightclickFunction;
	var me=this;
	var defaultcontextMenu=[
	{
						text:'搜索邻区',
						callback:function(){
							var polymark;
						if (defaultcontextMenu.length!=0) {
							polymark=defaultcontextMenu[defaultcontextMenu.length-1].polymark;
							//console.log("搜索邻区回调:"+polymark);
							defaultcontextMenu.splice(defaultcontextMenu.length-1,1);
//							console.log("callback:defaultcontextMenu回调函数："+defaultcontextMenu);
							}
//							console.log(polygon._data.getCell());
							me.responseRightClickForPolymark(polymark,defaultcontextMenu);
							}
						}
	];
	this.contextMenu=contextMenu?contextMenu: defaultcontextMenu;
	
	}
/**
 * 初始化GE地图
 * @param {} divId
 * @param {} lng
 * @param {} lat
 */
GisCellDisplayLib.prototype.initMap = function(divId, lng, lat,mapFuncs){
	//google.load("earth","1");
	var ge = this.ge;
	var me = this;
	this.lng=lng;
	this.lat=lat;
	google.earth.createInstance(divId,function(instance){
		ge = instance;
//		console.log("ge:"+ge+" divId:"+divId+" lng:"+me.lng+" lat:"+me.lat)
		ge.getWindow().setVisibility(true);
		ge.getNavigationControl().setVisibility(ge.VISIBILITY_AUTO);
		ge.getLayerRoot().enableLayerById(ge.LAYER_BORDERS, true);
		ge.getLayerRoot().enableLayerById(ge.LAYER_BUILDINGS,true);
		ge.getLayerRoot().enableLayerById(ge.LAYER_ROADS,true);
		ge.getLayerRoot().enableLayerById(ge.LAYER_TERRAIN,true);
		ge.getLayerRoot().enableLayerById(ge.LAYER_BUILDINGS_LOW_RESOLUTION, true);
		var la = ge.getView().copyAsLookAt(ge.ALTITUDE_RELATIVE_TO_GROUND);
		la.setLongitude(Number(me.lng));//113.591766357422003000
		la.setLatitude(Number(me.lat));//24.791721545708778500
		la.setRange(10000);
		ge.getView().setAbstractView(la);
				//初始化小区显示颜色
		me.setSingleStyle(ge.createStyle(""));
		me.getSingleStyle().getPolyStyle().getColor().set(me.singleColor);
		me.setMultiStyle(ge.createStyle(""));
		me.getMultiStyle().getPolyStyle().getColor().set(me.multiColor);
		me.getSingleStyle().getLabelStyle().setScale(0.6);
		me.getMultiStyle().getLabelStyle().setScale(0.6);
		/*var icon = ge.createIcon("");
		icon.setHref("");
		me.getSingleStyle().getIconStyle().setIcon(icon);
		me.getMultiStyle().getIconStyle().setIcon(icon);*/
		me.setCircleSingleStyle(ge.createStyle(""));
		me.getCircleSingleStyle().getPolyStyle().getColor().set("64E1E512");
		me.setCircleMultiStyle(ge.createStyle(""));
		me.getCircleMultiStyle().getPolyStyle().getColor().set("6412DEE5");
		me.setLineSingleStyle(ge.createStyle(""));
		me.getLineSingleStyle().getLineStyle().getColor().set("642AE512");

		//createPlacemark();
//		document.getElementById('installed-plugin-version').innerHTML =ge.getPluginVersion().toString();
//		document.getElementById('installed-plugin-version').innerHTML = ge.getPluginVersion().toString();
		me.ge=ge;
		// 地图所有图块加载完毕触发
			if (mapFuncs && typeof (mapFuncs['tilesloaded']) === "function") {
				mapFuncs['tilesloaded']();
			}
		google.earth.addEventListener(ge.getWindow(), 'click', function(event){
			var keyval=event.getButton ();
			if(keyval==0){
			var my = document.getElementById("searchNcell");
				 if (my != null)
        				my.parentNode.removeChild(my);
			}
		});
	
	},function(err){
		console.log("创建GE实例出错:"+err);
//		console.log(err);
	});
	}
GisCellDisplayLib.prototype.setLng = function(lng) {
	this.lng = lng;
}
GisCellDisplayLib.prototype.setLat = function(lat) {
	this.lat = lat;
}
// 设置图元的点击响应函数
GisCellDisplayLib.prototype.setClickFunction = function(fun) {
	if (fun instanceof Function) {
		this.clickFunction = fun;
	}
}
/**
 * 创建GE地标
 * @param {} lng
 * @param {} lat
 * @param {} markername
 */
GisCellDisplayLib.prototype.createGePlacemark = function(lng,lat,markername) {
var ge=this.ge;
// 创建地标。
var placemark = ge.createPlacemark('');
placemark.setName(markername);
// 设置地标的位置。
var point = ge.createPoint('');
point.setLatitude(Number(lat));
point.setLongitude(Number(lng));
placemark.setGeometry(point);
//向 Google 地球添加地标。
ge.getFeatures().appendChild(placemark);
}
/**
 * 创建GE几何多边形
 * @param {} allLngLats
 */
GisCellDisplayLib.prototype.createGePolygon = function(allLngLats,name) {
var ge=this.ge;
var me=this;

// 创建地标。
var mark = ge.createPlacemark('');
		
		var name=name;
		mark.setName(name);
		
		var outer = ge.createLinearRing('');
		var polygon = ge.createPolygon('');
		
		polygon.setOuterBoundary(outer);
//		var allLngLats="113.58813182080982,24.793842901027013;113.58785283529517,24.794788174191943;113.58841083794492,24.794788174191943";
		var lnglats = allLngLats.split(';');
		var temp = lnglats[0].split(',');
		var lng1 = parseFloat(temp[0]);
		var lat1 = parseFloat(temp[1]);
		temp = lnglats[1].split(',');
		var lng2 = parseFloat(temp[0]);
		var lat2 = parseFloat(temp[1]);
		temp = lnglats[2].split(',');
		var lng3 = parseFloat(temp[0]);
		var lat3 = parseFloat(temp[1]);
		var coords = outer.getCoordinates();
		this.lng1=lng1;
		this.lat1=lat1;
		this.lng2 =lng2;
		this.lat2 =lat2;
		this.lng3 =lng3;
		this.lat3 =lat3;
		this.allLngLats=allLngLats;
		coords.pushLatLngAlt(lat1,lng1,0);
		coords.pushLatLngAlt(lat2,lng2,0);
		coords.pushLatLngAlt(lat3,lng3,0);

		if(!mark.getStyleSelector())
		{
			mark.setStyleSelector(ge.createStyle(''));
		}
		var polyColor = mark.getStyleSelector().getPolyStyle().getColor();
		polyColor.set('D8D800FF');//0E8EC9 C800FF00
		
		mark.setGeometry(polygon);
//		mark.setGeometry(point);
		ge.getFeatures().appendChild(mark);
		this.mark=mark;
		google.earth.addEventListener(mark, 'click', function(event){
			if(me.clickFlag==false){
			console.log("mark:"+mark+" polygon:"+polygon);
			console.log(me.lat1+" "+me.lng1);
			console.log(event.getLatitude()+" "+ge.getFeatures().removeChild(mark));
			ge.getFeatures().removeChild(mark);//单点删除对象
			console.log(Math.random()/1000);
			var ram=Math.random()/1000;
			var LngLats=me.lng1+ram+","+(me.lat1+ram)+";"+(me.lng2+ram)+","+(me.lat2+ram)+";"+(me.lng3+ram)+","+Number(me.lat3+ram);
			console.log(LngLats);
			var startlat=me.lat1;
			var startlng=me.lng1;
			var endlat=Number(me.lat1+ram);
			var endlng=Number(me.lng1+ram);
			me.createGePolygon(LngLats);
			var linemark=me.drawLine(startlat,startlng,endlat,endlng);
			this.linemark=linemark;
			console.log("if:"+me.clickFlag);
			me.clickFlag=true;
			}/*else{
			var lnglats=me.allLngLats;
			ge.getFeatures().removeChild(me.mark);//单点删除对象
			me.createGePolygon(lnglats);
			console.log("else:"+me.clickFlag);
			me.clickFlag=false;
			}*/
		}); //如果需要则添加placemark的鼠标单击响应
		google.earth.addEventListener(mark, 'click', function(event){
			console.log(mark.getName());
			/*if(me.clickFlag==true){
			ge.getFeatures().removeChild(me.mark);
//			ge.getFeatures().removeChild(me.linemark);
			}*/
			
		});
		google.earth.addEventListener(mark, 'dblclick', function(event){
			var lnglats=me.allLngLats;
//			ge.getFeatures().removeChild(mark);//单点删除对象
			me.createGePolygon(lnglats);
		});
}
/**
 * 画线（起始点）
 * @param {} startlat
 * @param {} startlng
 * @param {} endlat
 * @param {} endlng
 */
GisCellDisplayLib.prototype.drawLine = function(startlat,startlng,endlat,endlng,option) {
		var ge=this.ge;
		var kmlcolor
		var flag = null==option || undefined == option;
		if(flag){
			kmlcolor=this.toGePolygonStyle({"fillColor":"#E81F2F","fillOpacity":1});			
		}
		//console.log("kmlcolor:"+kmlcolor);
		// 创建地标
		var lineStringPlacemark = ge.createPlacemark('');
		// 创建LineString
		var lineString = ge.createLineString('');
		if(!lineStringPlacemark.getStyleSelector())
		{
			lineStringPlacemark.setStyleSelector(ge.createStyle(''));
		}
		var linestyle = lineStringPlacemark.getStyleSelector().getLineStyle();
		if(flag){
			linestyle.getColor().set(kmlcolor);
			linestyle.setWidth(1.2);	
		}else{
			//{"color":color,"width":width}
			linestyle.getColor().set(option["color"]);
			linestyle.setWidth(option["width"]);
		}
		
		lineStringPlacemark.setGeometry(lineString);
		// 添加LineString点
		lineString.getCoordinates().pushLatLngAlt(startlat,startlng, 0);
		lineString.getCoordinates().pushLatLngAlt(endlat,endlng, 0);
		// 向Google地球添加地图项
		ge.getFeatures().appendChild(lineStringPlacemark);
		return lineStringPlacemark;
}
/**
 * GE画圆
 * @param {} centerPoint
 * 圆的中心点坐标，包括lng,lat
 * @param {} centerLng
 * @param {} radius
 * @return circlePlacemarkWrap
 * 利用马甲模式封装后的圆形图元
 * Liang YJ 2014-3-13 14:00 修改
 */
//这里需要重新考虑
GisCellDisplayLib.prototype.createCirclePlacemark = function(centerPoint, radius, option, name, id) {
	var ge=this.ge;
	var me = this;
	var circlePlacemarkWrap = null;
	if(!id){
		id = "";
	}
	var circlePlacemark = ge.getElementById(id);
	if(circlePlacemark){
		CirclePlacemarkWrap(newGeLib,newCirclePlacemark,newName,newCenterLng,newCenterLat,newRadius)
		return new CirclePlacemarkWrap(centerPoint,radius,circlePlacemark,me);
	}
	circlePlacemark = ge.createPlacemark(id);
	if(null != name && undefined != name){
		circlePlacemark.setName(name);
	}
  	circlePlacemark.setGeometry(ge.createPolygon(''));
  	var centerLat = centerPoint["lat"];
  	var centerLng = centerPoint["lng"];
  	circlePlacemark.getGeometry().setOuterBoundary(this.makeCircle(centerLat, centerLng, radius));
	var style = ge.createStyle("");
  	var color = null;
  	//使用默认的颜色
  	if(null == option || undefined == option || null ==option['fillColor']){
  		color = this.singleColor;
  	}else{
  		//使用调用者提供的颜色
  		color = toGePolygonStyle(option); 
  	}
  	style.getPolyStyle().getColor().set(color);
  	circlePlacemark.setStyleSelector(style);
  	//circlePlacemarkWrap对象
  	//newCenterPoint,newRadius,newCirclePlacemark,newGeLib
  	circlePlacemarkWrap = new CirclePlacemarkWrap(centerPoint,radius,circlePlacemark,me);
  	ge.getFeatures().appendChild(circlePlacemark);
  	return circlePlacemarkWrap;
}
GisCellDisplayLib.prototype.makeCircle = function(centerLat, centerLng, radius) {
	var ge=this.ge;
	var ring = ge.createLinearRing('');
	var altitude = 0;
    var steps = 10;
    var pi2 = Math.PI * 2;
    for (var i = 0; i < steps; i++) {
      var lat = centerLat + radius * Math.cos(i / steps * pi2);
      var lng = centerLng + radius * Math.sin(i / steps * pi2);
      ring.getCoordinates().pushLatLngAlt(lat, lng, altitude);
    }
    //console.log("ring:"+ring);
    return ring;
}
GisCellDisplayLib.prototype.showGisCells = function(data) {
		this.cells = data;
		var cells=this.cells;
		var ge=this.ge;
		var me=this;
		
//	console.log("共"+cells.length+"个小区");
	try{
			for(var i=0; i < cells.length; i++)
	{
		
		(function(){
					var cellObj=cells[i];
					me.createPolygon(cellObj);
				})();
				
	}
	}catch(err){
		console.log(err);
	}
}
GisCellDisplayLib.prototype.createPolygon = function(cellObj) {
		var ge=this.ge;
		var me=this;
		var allPolygons = this.allPolygons;
		var cellToPolygon = this.cellToPolygon;
		
		var mark = ge.createPlacemark('');
		var polygon = ge.createPolygon('');
		mark.setName(cellObj.chineseName);
		mark.setGeometry(polygon);
		var outer = ge.createLinearRing('');
		polygon.setOuterBoundary(outer);
		var lnglats = cellObj.allLngLats.split(';');
		var temp = lnglats[0].split(',');
		var lng1 = parseFloat(temp[0]);
		var lat1 = parseFloat(temp[1]);
		temp = lnglats[1].split(',');
		var lng2 = parseFloat(temp[0]);
		var lat2 = parseFloat(temp[1]);
		temp = lnglats[2].split(',');
		var lng3 = parseFloat(temp[0]);
		var lat3 = parseFloat(temp[1]);
		//console.log(lng1+","+lat1);
		var coords = outer.getCoordinates();
		coords.pushLatLngAlt(lat1,lng1,0);
		coords.pushLatLngAlt(lat2,lng2,0);
		coords.pushLatLngAlt(lat3,lng3,0);

		if(!mark.getStyleSelector())
		{
			mark.setStyleSelector(ge.createStyle(''));
		}
		var polyColor = mark.getStyleSelector().getPolyStyle().getColor();
		polyColor.set('D8D800FF');
		
		allPolygons.push(polygon);
		this.addCell(cellObj);
		cellToPolygon[this.getCell()] = polygon;// cell to polygon的hash
		cellToPolygon[polygon] = this.getCell();
//		console.log("this.getCell():"+this.getCell()+" polygon:"+polygon);
//		console.log(cellToPolygon[polygon]);
		ge.getFeatures().appendChild(mark);

		//事件监听
		google.earth.addEventListener(mark, 'click', function(event){
//			console.log(i);
//			console.log(me.ge.getFeatures().removeChild(mark));	
//			console.log(cellToPolygon[mark.getGeometry()]);
//			cellToPolygon
			me.clickFunction(me,mark, event);
		});
//		return polygon;
}

GisCellDisplayLib.prototype.showGisCell = function(data,key) {
	// console.log("in GisCellDisplayLib.prototype.showGisCell");
	if (!data) {
		return;
	}
	try {
		var me=this;
		var composeMarkers = this.composeMarkers;
		var allPolyMarks = this.allPolyMarks;
		var visiblePolygons = this.visiblePolygons;
		var cellToPolygon = this.cellToPolygon;
		var cellToCompose = this.cellToCompose;
//		var map = this.map;
		var multiColor = this.multiColor;
		var singleColor = this.multiColor;

		var start = composeMarkers.length;// 新获取的小区对象在数组中的起始位置
		var cmk;
		var j = 0;
		var polymark;
		// O(n*n)
		for ( var i = 0; i < data.length; i++) {
			var gisCell = data[i];
			j = 0;
			for (j = 0; j < composeMarkers.length; j++) {
				cmk = composeMarkers[j];
				if (cmk.similiar(gisCell, this.DiffAzimuth, this.DiffDistance)) {
					cmk.addCell(gisCell,key);
					cellToPolygon[cmk.getCell()] = allPolyMarks[j];// 小区到polygon
					if (cmk.getCount() === 2) {
						// 重新渲染
						polymark = allPolyMarks[j];
						// console.log("旧的polygon" + polygon);
						if (polymark) {
//							polygon.setFillColor(multiColor);
							var polyColor = polymark.getStyleSelector().getPolyStyle().getColor();
//							var option=cmk.getPolygonOptions(me.singleColor, me.multiColor)
							polyColor.set(multiColor);//D8D800FF
						}
					}
					break;
				}
			}
			// console.log("j=" + j);
			if (j >= composeMarkers.length) {
				// console.log("准备添加进单独的marker");
				var onecmk = new ComposeMarker(gisCell,key);
				if(key) {
					cellToCompose[gisCell.lcid]=onecmk;//cell to compose的映射
					//console.log(gisCell.lcid);
				} else {
					cellToCompose[gisCell.cell]=onecmk;//cell to compose的映射
					//console.log(gisCell.cell);
				}	
				if (onecmk) {
					composeMarkers.push(onecmk);// 不与任何点重复，加入
				} else {
					// console.log(gisCell.cell+" 未能正确生成ComposeMarker！");
				}
				// console.log("将marker添加进数组后。");
			}
		}

		// 开始生成polygon
		var newLength = composeMarkers.length;
		// console.log("准备生成polygon...start=="+start+",newLength="+newLength);
		for ( var index = start; index < newLength; index++) {
				cmk = composeMarkers[index];
			try{
			var mark = me.createPolygonFromComposeMark(cmk);
			allPolyMarks.push(mark);
			visiblePolygons.push(mark);//
//			polygon._isShow = false;
			cellToPolygon[cmk.getCell()] = mark;// cell to polygon的hash
			}catch(err){
				console.log(err);
				continue;
			}
			

			// 创建一个marker
			// var tempmk=new BMap.Marker(new

			// 是否要显示？
			/*var visib = this.shouldDisplay(polygon);
			if (visib === true) {
				// console.log("可见，将在地图显示");
				// polygon._isShow = true;
				// map.addOverlay(polygon);
				this.showOnePolygon(polygon);
				visiblePolygons.push(polygon);
			}*/
		}
	} catch (err) {
		console.error(err);
	}
}
/**
 * 从聚合对象创建polygon
 * 
 * @param {}
 *            cmk
 * @return {}
 */
GisCellDisplayLib.prototype.createPolygonFromComposeMark = function(cmk) {
	try {
		if (!cmk) {
			console.log("空的参数");
			return null;
		}
		var me = this;
		var ge = this.ge;
		var pa = cmk.getPointArray();
//		console.log(pa[0].substring(0,pa[0].indexOf(","))+"  "+pa[0].substring(pa[0].indexOf(",")+1));
//		console.log("pa:"+pa[0]);
		/*var polygon = new BMap.Polygon(pa, cmk.getPolygonOptions(
				me.singleColor, me.multiColor));*/
		//Liang YJ 修改 2014-3-12 10:28
		var polymark = null;
		polymark = ge.getElementById(cmk._cell);
		if(null != polymark && undefined != polymark){
			return polymark;
		}
		//try{
			polymark = ge.createPlacemark(cmk._cell);			
		//}catch(err){
			//console.log("无法创建cell: "+err);
			//console.log("无法创建cell的label: "+cmk._cell);
		//}
		var polygon = ge.createPolygon('');
		polymark.setName(cmk._chineseName);
		polymark.setDescription("["+cmk._cell+"]");
//		console.log(mark.getName());
		// 设置地标的位置。
//		var point = ge.createPoint('');
//		point.setLatitude((Number(pa[1].substring(pa[1].indexOf(",")+1)) + Number(pa[2].substring(pa[2].indexOf(",")+1))) / 2);
//		point.setLongitude((Number(pa[1].substring(0,pa[1].indexOf(","))) + Number(pa[2].substring(0,pa[2].indexOf(",")))) / 2);
		var point =this.getCenter(cmk);//获取中心点坐标
		var multiGeometry = ge.createMultiGeometry('');
		multiGeometry.getGeometries().appendChild(point);
		multiGeometry.getGeometries().appendChild(polygon);
		polymark.setGeometry(multiGeometry);
		
//		mark.setGeometry(polygon);
		
		var outer = ge.createLinearRing('');
		polygon.setOuterBoundary(outer);
//		console.log("cmk._allLngLats:"+cmk._allLngLats);
		var lnglats = cmk._allLngLats.split(';');
		var temp = lnglats[0].split(',');
		var lng1 = parseFloat(temp[0]);
		var lat1 = parseFloat(temp[1]);
		temp = lnglats[1].split(',');
		var lng2 = parseFloat(temp[0]);
		var lat2 = parseFloat(temp[1]);
		temp = lnglats[2].split(',');
		var lng3 = parseFloat(temp[0]);
		var lat3 = parseFloat(temp[1]);
		//console.log(lng1+","+lat1);
		var coords = outer.getCoordinates();
		coords.pushLatLngAlt(lat1,lng1,0);
		coords.pushLatLngAlt(lat2,lng2,0);
		coords.pushLatLngAlt(lat3,lng3,0);
		if(!polymark.getStyleSelector())
		{
			polymark.setStyleSelector(ge.createStyle(''));
		}
		var polyColor = polymark.getStyleSelector().getPolyStyle().getColor();
//		console.log("调用前me.singleColor:"+me.singleColor);
		var option=cmk.getPolygonOptions(me.singleColor, me.multiColor);
		
		//{"fillColor":"#E81F2F","fillOpacity":1}
		//option.fillOpacity=1;
		//option.fillColor='';
		//console.log("option.fillColor:"+option.fillColor);
		polyColor.set(option.fillColor);//D8D800FF
		polymark.getStyleSelector().getLabelStyle().setScale(0.6);
//		polymark.getStyleSelector().getPolyStyle().getCursor().set("hand");
		/*var icon = ge.createIcon("");
		icon.setHref("");
		polymark.getStyleSelector().getIconStyle().setIcon(icon);*/
		polymark.getStyleSelector().getIconStyle().getIcon().setW(2);
		
//		console.log("polygon instanceof Object:"+(polygon instanceof Object)+" :polygon instanceof Array:"+(polygon instanceof Array));
//		mark._data = cmk;// 相互引用
//		cmk.setPolygon(mark);

		// 2013-12-13 gmh add label
		// 同一个起点的众多扇形，只显示其中的一个的名称，其他的不显示，免得太拥挤
		var key = cmk.getLng() + "_" + cmk.getLat();
		var plys = this.sameLnglatPolyArray[key];
		if (!plys) {
			plys = new Array();
			this.sameLnglatPolyArray[key]=plys;
		}
		plys.push(polygon);
		if (plys.length == 1) {
			//只有第一个需要配label
			var angle = cmk.getAzimuth();
			var labelPosition = null;
			var edgePosition = null;
			var startPosition = null;
			var cellname = cmk.getFirstCellNameChineseFirst();

			if (pa && pa.length > 2) {
				var p1 = pa[1];
				var p2 = pa[2];
				/*edgePosition = new BMap.Point((p1.lng + p2.lng) / 2,
						(p1.lat + p2.lat) / 2);*/
				startPosition = pa[0];

			} else {
				if (pa) {
					edgePosition = pa;
					startPosition = pa;
				} else {
					edgePosition = null;
					startPosition = null;
				}
			}
			/*if (edgePosition != null) {

				if (angle > 180) {
					angle = angle - 180;
					labelPosition = edgePosition;
				} else {
					labelPosition = startPosition;
				}
				angle = angle - 90;
				var label = new BMap.Label(cellname, {
					'position' : labelPosition,
					'offset' : {
						width : 0,
						height : 0
					}
				});

				label.setStyle({
					'border' : 'none',
					'backgroundColor' : 'transparent',
					'color' : '#2E2EFE',
					'transform' : 'rotate(' + (angle) + 'deg)'
				});

				polygon._label = label;
			}*/
		}
		// /
//		this.mark=mark;均是指向最后一个引用地址
		ge.getFeatures().appendChild(polymark);
		google.earth.addEventListener(polymark, 'click', function(event){
				var my = document.getElementById("searchNcell");
				 if (my != null)
        				my.parentNode.removeChild(my);
			var keyval=event.getButton ();
			if(keyval==0){
				me.clickFunction(polymark,event);
			}else if(keyval==2){
				me.rightClickMenuItemForPolygon(event.getClientX (), event.getClientY(), 100, 18,polymark,typeof me.contextMenu=="undefined"?null:me.contextMenu);
				me.clearOnlyExtraOverlay();//清除额外覆盖物
				me.resetPolygonToDefaultOutlook();//恢复默认polygon颜色
				var aa=typeof me.contextMenu=="undefined"?null:me.contextMenu;
				if (aa.length!=0) {
					//var obj=eval("("+txtMenuItem+")");
					var bb={'polymark':polymark};
					aa.push(bb);
					me.contextMenu=aa;
				}
		  			me.rightclickFunction(polymark,event);
			}
		});
		
		return polymark;
	} catch (err) {
		console.error(err);
		return null;
	}
}
/**
 * 移动视图将地图的中心点更改为给定的点
 * @param {} lng
 * @param {} lat
 */
GisCellDisplayLib.prototype.panTo = function(lng, lat) {
	//this.clearData();
	var ge=this.ge;
	// 获取当前视图。
	var lookAt = ge.getView().copyAsLookAt(ge.ALTITUDE_RELATIVE_TO_GROUND);
	// 设置新的纬度值和经度值。
	lookAt.setLatitude(Number(lat));
	lookAt.setLongitude(Number(lng));
	// 更新 Google 地球中的视图。
	ge.getView().setAbstractView(lookAt);
}
/**
 * 清除地标数据
 */
GisCellDisplayLib.prototype.clearData = function() {
	var ge=this.ge;
/*	var marklist=ge.getFeatures().getChildNodes();
//		console.log(marklist.getLength());
	for(var i=0;i<marklist.getLength();i++){
		ge.getFeatures().removeChild(marklist.item(i));
	}*/
	//Liang YJ 修改 2014-3-12 14:28
	/*var features = ge.getFeatures();
	while (features.getFirstChild()){
		var child = features.getFirstChild();
		features.removeChild(child);
		child.release();
	}*/
	this.composeMarkers.splice(0, this.composeMarkers.length);
	var allPolyMarks = this.allPolyMarks;
	/*for ( var i = 0; i < allPolyMarks.length; i++) {
		// map.removeOverlay(allPolygons[i]);
//		console.log("allPolyMarks:"+allPolyMarks[i].getType());
		//ge.getFeatures().removeChild((allPolyMarks[i]));
		this.hideOnePolygon(allPolygons[i]);
	}*/
	allPolyMarks.splice(0, allPolyMarks.length);
	// 可见
	this.visiblePolygons.splice(0, this.visiblePolygons.length);
	// 特殊、额外图元
	this.specialPolygons.splice(0, this.specialPolygons.length);
	// 额外
	var extraMapOverlays = this.extraMapOverlays;
	for ( var i = 0; i < extraMapOverlays.length; i++) {
		var extraMapOverlay = extraMapOverlays[i];
		console.log("extraMapOverlays:"+extraMapOverlay.getType());
		ge.getFeatures().removeChild(extraMapOverlay);
		extraMapOverlay.release();
	}
	extraMapOverlays.splice(0, extraMapOverlays.length);

	// hash表
	this.cellToPolygon = null;
	this.cellToPolygon = new Object();
	
	this.cellToCompose = null;
	this.cellToCompose = new Object();
	// 关闭信息窗
	this.closeInfoWindow();

}
/**
 * 清除覆盖物
 */
GisCellDisplayLib.prototype.clearOverlays = function() {
	var ge=this.ge;
	/*var marklist=ge.getFeatures().getChildNodes();
//		console.log(marklist.getLength());
	for(var i=0;i<marklist.getLength();i++){
		ge.getFeatures().removeChild(marklist.item(i));
	}*/
	var features = ge.getFeatures();
	while (features.getFirstChild()){
		//Liang YJ 2014-3-18 修改
		var child = features.getFirstChild();
		features.removeChild(child);
		child.release();
	}
}
/**
 * 将全部图元恢复默认外观
 */
GisCellDisplayLib.prototype.resetPolygonToDefaultOutlook = function() {
		var ge=this.ge;
		var marklist=ge.getFeatures().getChildNodes();
		//var style=ge.createStyle('');
		for(var i=0;i<marklist.getLength();i++){
		var mark=marklist.item(i);
		if(!mark.getStyleSelector())
		{
			mark.setStyleSelector(ge.createStyle(''));
		}
		var polyColor = mark.getStyleSelector().getPolyStyle().getColor();
		//polyColor.set('00D800CC');//0E8EC9 C800FF00
		//var cmk=new ComposeMarker();
		//var option=cmk.getPolygonOptions(me.singleColor, me.multiColor)
		polyColor.set(this.singleColor);//D8D800FF option.fillColor
		}
	
}
/**
 * 获取两点中心点
 * @param {} cmk
 * @return {}
 */
GisCellDisplayLib.prototype.getCenter = function(cmk) {
		var ge=this.ge;
		var pa = cmk.getPointArray();
		var point = ge.createPoint('');
		point.setLatitude((Number(pa[1].substring(pa[1].indexOf(",")+1)) + Number(pa[2].substring(pa[2].indexOf(",")+1))) / 2);
		point.setLongitude((Number(pa[1].substring(0,pa[1].indexOf(","))) + Number(pa[2].substring(0,pa[2].indexOf(",")))) / 2);
		return point;
	}
/**
 * 获取小区的底边的中心点
 */
GisCellDisplayLib.prototype.getCellEdgeCenterPoint = function(cell) {
	if (!cell) {
		return null;
	}
	var ge=this.ge;
	//var pl = this.cellToPolygon[cell];
	var polymark = ge.getElementById(cell);
	var polygon=polymark.getGeometry().getGeometries().getLastChild();
	var lnglatarr=polygon.getOuterBoundary().getCoordinates();
	//console.log("getCellEdgeCenterPoint lnglatarr:"+lnglatarr);
	var lng1=lnglatarr.get(1).getLongitude();
	var lat1=lnglatarr.get(1).getLatitude();
	var lng2=lnglatarr.get(2).getLongitude();
	var lat2=lnglatarr.get(2).getLatitude();
	try {
		var point = ge.createPoint('');
		point.setLatitude((Number(lat1) + Number(lat2)) / 2);
		point.setLongitude((Number(lng1) + Number(lng2)) / 2);
		
		return point;
	} catch (err) {
		return null;
	}
	return null;
}
GisCellDisplayLib.prototype.getDefaultCursor=function(){
	return null;
}
/**
 * style类型转换
 * @param {} option
 * @return {}
 */
GisCellDisplayLib.prototype.toGePolygonStyle=function(option){
	if(null === option || undefined === option || null === option.fillColor || undefined === option.fillColor){
		return null;
	}
	//转换透明密度，0-1转换成00-ff
	var opacity = null;
	if(null === option.fillOpacity || undefined === option.fillOpacity){
		opacity = 1;//默认不透明
	}else{
		opacity = option.fillOpacity;
	}
	opacity = Math.floor(opacity*255);
	opacity = opacity.toString(16);
	if(1 === opacity.length){
		opacity = "0"+opacity;
	}
	//转换颜色，#rrggbb转换成
	var rr = option.fillColor.slice(1,3);
	var gg = option.fillColor.slice(3,5);
	var bb = option.fillColor.slice(5);
	
	
	return opacity+bb+gg+rr;
	
}
/**
 * author Liang YJ
 * @param option
 * option包括fillColor("#rrggbb")和opacity(0-1)两个属性，针对polygon
 */
function toGePolygonStyle(option){
	if(null === option || undefined === option || null === option.fillColor || undefined === option.fillColor){
		return null;
	}
	
	//判断是不是百度地图颜色样式，如果不是则直接返回option;
	if("#"!=option.fillColor.charAt(0)){
		return option;
	}
	//转换透明密度，0-1转换成00-ff
	var opacity = null;
	if(null === option.fillOpacity || undefined === option.fillOpacity){
		opacity = 1;//默认不透明
	}else{
		opacity = option.fillOpacity;
	}
	opacity = Math.floor(opacity*255);
	opacity = opacity.toString(16);
	if(1 === opacity.length){
		opacity = "0"+opacity;
	}
	//转换颜色，#rrggbb转换成
	var rr = option.fillColor.slice(1,3);
	var gg = option.fillColor.slice(3,5);
	var bb = option.fillColor.slice(5);
	
	
	return opacity+bb+gg+rr;
	
}
/**
 * author Liang YJ
 * @param option
 * option包括strokColor("#rrggbb"),strokeOpacity(0-1)和strokeWeight针对polygon
 */
function toGeLineStyle(option){
	if(null === option || undefined === option || null === option.strokeColor){
		return null;
	}
	//转换透明密度，0-1转换成00-ff
	var opacity = null;
	if(null === option.strokeOpacity || undefined === option.strokeOpacity){
		opacity = 1;//默认不透明
	}else{
		opacity = option.strokeOpacity;
	}
	opacity = Math.floor(opacity*255);
	opacity = opacity.toString(16);
	if(1 === opacity.length){
		opacity = "0"+opacity;
	}
	//转换颜色，#rrggbb转换成
	var rr = option.strokeColor.slice(1,3);
	var gg = option.strokeColor.slice(3,5);
	var bb = option.strokeColor.slice(5);
	
	var color = opacity+bb+gg+rr;
	
	//线的宽度，百度和谷歌都是以像素为单位，无需转换，默认为1px
	var width = null;
	if(null === option.strokeWeight || undefined === option.strokeWeight){
		width = 1;
	}else{
		width = option.strokeWeight;
	}
	
	//
	
	return {"color":color,"width":width};
	
}
/**
 * 改变小区对应的polygon的外观
 * 
 * @param cell
 * @param option
 * @param puttospec
 *            是否添加到特殊渲染队列（待reset的时候，就只考虑这个队列里的就行了）
 * @returns {Boolean}
 */
GisCellDisplayLib.prototype.changeCellPolygonOptions = function(cell, option,
		puttospec) {
		var me = this;
		var ge = this.ge;
		var placemark = ge.getElementById(cell);
		if(null === placemark)
		{
			return;
		}
		options = this.getOptions();
		var color = null;
		if(options[option["fillColor"]]){
			color = options[option["fillColor"]];
		}else{
			color = me.toGePolygonStyle(option);
			//将新颜色缓存
			options[option["fillColor"]] = color;
		}
		var polyColor = placemark.getStyleSelector().getPolyStyle().getColor();
		polyColor.set(color);
		if (puttospec === true) {
			// 添加到特殊队
			this.specialPolygons.push(placemark);
		}
		return true;
}
/**
 * 将特殊外观的图元恢复默认外观
 */
GisCellDisplayLib.prototype.resetSpecPolygonToDefaultOutlook = function() {
	var ge = this.ge;
	var me = this;
	var specialPolygons = this.specialPolygons;
	var len = specialPolygons.length;
	var pl = null;
	var cmk = null;
	var option = null;
	var sc = this.singleColor;
	var mc = this.multiColor;
	
	for ( var i = 0; i < len; i++) {
		pl = specialPolygons[i];
		if(!pl.getStyleSelector())
		{
			pl.setStyleSelector(ge.createStyle(''));
		}
		var polyColor = pl.getStyleSelector().getPolyStyle().getColor();
		for(var j=0;j<this.composeMarkers.length;j++){
			cmk=this.composeMarkers[j];
//			console.log("cmk._cell:"+cmk._cell+" pl.getId():"+pl.getId());
			if(cmk._cell==pl.getId()){
				var option=cmk.getPolygonOptions(me.singleColor, me.multiColor)
//				console.log("颜色:"+option.fillColor);
				polyColor.set(option.fillColor);//D8D800FF
			}
		}
	}

	// 特殊、额外图元
	this.specialPolygons.splice(0, this.specialPolygons.length);
	//this.specialPolygons.length = 0;
}
/**
 * 删除覆盖物
 * @param {} obj
 */
GisCellDisplayLib.prototype.removeOverlay=function(obj){
	this.ge.getFeatures().removeChild(obj);
	obj.release();
}
GisCellDisplayLib.prototype.get = function(prop) {
	return this[prop];
}
GisCellDisplayLib.prototype.responseRightClickForPolymark=function(polymark,txtMenuItem){

	var cell=polymark.getId();
			// 主覆盖小区颜色
			var option_serverCell={
					'fillColor':'#B40431',"fillOpacity":1
			};
			//邻区颜色
			var option_ncell={
						'fillColor':'#2EFE2E',"fillOpacity":1
				};
			gisCellDisplayLib.changeCellPolygonOptions(cell,option_serverCell,true);
			var ncellarr=new Array();
			sendDate={'cell':cell};
//			console.log(cell);
			$(".loading_cover").css("display", "block");
			$.ajax({
				url : 'getNcellforNcellAnalysisOfBusyCellByCellForAjaxAction',
				dataType : 'text',
				data:sendDate,
				type : 'post',
//				async:	false,
				success : function(data) {
				   var mes_obj=eval("("+data+")");
//				    console.log("进入responseRightClickForPolygon:"+mes_obj);
				   if(mes_obj.length==0){
//				   alert("对不起,没有邻区数据!");
				   animateInAndOut("operInfo", 500, 500, 1000,
									"operTip", "对不起,没有邻区数据!");
				   }
				   var i=0;
				   for(var key in mes_obj){
//				   	ncellarr.push(mes_obj[key].NCELL);
				   	var ncell=mes_obj[key].NCELL;
				   	if(typeof(ncell)=='undefined'){
				   		continue;
				   	}
				   	gisCellDisplayLib.changeCellPolygonOptions(ncell,option_ncell,true);
				   	gisCellDisplayLib.drawLineBetweenCells(cell,ncell,null);
				   	
				   }
				},
				error : function(err, status) {
					console.error(status);
				},
				complete : function() {
					$(".loading_cover").css("display", "none");
				}
			});
			 
}
/**
 * 给指定的两个小区连线
 * 
 * @param cell
 * @param anotherCell
 */
GisCellDisplayLib.prototype.drawLineBetweenCells = function(cell, anotherCell,
		option) {
	if (!cell || !anotherCell) {
		return;
	}
	// 找到cell
	var pl_1 = this.ge.getElementById(cell);//this.cellToPolygon[cell];
	var pl_2 = this.ge.getElementById(anotherCell);//this.cellToPolygon[anotherCell];
	
	if (!pl_1 || !pl_2) {
		return;
	}
	try {
		var point1=this.getCellEdgeCenterPoint(cell);
		var point2=this.getCellEdgeCenterPoint(anotherCell);
		//console.log("point1:"+point1);
		// 画一条线
		//var pline = new BMap.Polyline([ point1, point2 ], option);
		var startlng=point1.getLongitude();
		var startlat=point1.getLatitude ();
		var endlng = point2.getLongitude();
		var endlat = point2.getLatitude ();
		//console.log(startlng+","+startlat+";"+endlng+","+endlat);
		option = toGeLineStyle(option);
		var pline=this.drawLine(startlat,startlng,endlat,endlng,option);
		//this.map.addOverlay(pline);
		this.extraMapOverlays.push(pline);
	} catch (err) {

	}
}
/**
 * 通过传入不同的txtMenuItem对象值获取不同的右键菜单（某个页面个性化定制菜单）
 * @param {} polygon
 * @param {} txtMenuItem
 */
GisCellDisplayLib.prototype.rightClickMenuItemForPolygon=function(left, top, width, height,polymark,txtMenuItem) {  
      // 创建浮动层  
      var button = document.createElement("div"); 
	  //--------------------------
      	//button.style.cssText='padding: 2px 6px; margin: 0px 2px; font-size: 12px; -moz-user-select: none; line-height: 17px; width: 100px; color: rgb(0, 0, 0); cursor: pointer;';
	  	button.style.background='#FFFFFF';//E8AC15
		button.style.color='#000000';
		button.style.border='2px solid #ADBFE4';
		button.style.borderColor='#ADBFE4';//#E8AC15
		button.style.cursor='pointer';
		
		button.id="searchNcell";
		button.onclick=txtMenuItem[0].callback;
		button.innerHTML = txtMenuItem[0].text;

      	button.style.position = 'absolute';  
      	button.style.left =  left + 'px';  
      	button.style.top =   top + 'px';  
      	button.style.width =  width + 'px';  
      	button.style.height =  height + 'px'; 
      	// set up z-orders  
      	button.style.zIndex = 10;  
       
      
      	document.body.appendChild(button);  
      /*点击菜单层中的某一个菜单项，就隐藏菜单*/
     button.addEventListener("click", function(){
     
     	//this.style.display = "none";
     	var my = document.getElementById("searchNcell");
				 if (my != null)
        				my.parentNode.removeChild(my);
     	
     }); 
    } 
/**
 * 只清除额外覆盖物
 */
GisCellDisplayLib.prototype.clearOnlyExtraOverlay = function() {
	var ge=this.ge;
	var extraMapOverlays = this.extraMapOverlays;
	for(var i=0;i<extraMapOverlays.length;i++){
		var extraMapOverlay = extraMapOverlays[i];
		ge.getFeatures().removeChild(extraMapOverlay);
		extraMapOverlay.release();
	}
	extraMapOverlays.splice(0, extraMapOverlays.length);
}
/**
 * 通过图形对象获取cmk数据
 * @param {} shape
 * @return {}
 */
GisCellDisplayLib.prototype.getComposeMarkerByShape = function(shape) {
	
	var ge=this.ge;
	var cellToCompose=this.cellToCompose;
	var cell=shape.getId();
	var cmk=cellToCompose[cell];
	return cmk;
}
/**
 * 添加覆盖物
 * @param {} overlay
 */
GisCellDisplayLib.prototype.addOverlay = function(overlay) {
		return null;
}
/**
 * 通过cmk获取坐标点对象(其实是底边第三个坐标点) 
 * @param {} cmk
 * @return {}
 */
GisCellDisplayLib.prototype.getLnglatObjByComposeMarker = function(cmk) {
	
	var lnglats=new Object();
//	var cell=cmk.getCell();
	var pointArray = cmk.getPointArray();
	var one=pointArray[pointArray.length-1].split(",");
	lnglats['lng']=one[0];
	lnglats['lat']=one[1];
	return lnglats;
}
GisCellDisplayLib.prototype.closeInfoWindow = function(){
	return null;
}
GisCellDisplayLib.prototype.showInfoWindow = function(content, point) {
	
}
/**
 * @author Liang YJ
 * @date 2014-3-12 16:30
 * @description 获取地图对象
 */
GisCellDisplayLib.prototype.getMap = function() {
	return this.ge;
}
GisCellDisplayLib.prototype.getPolygonCnt = function() {
	return this.allPolyMarks.length;
}
/**
 * 通过图形获取原点坐标
 * @param {} pl
 * @return {}
 */
GisCellDisplayLib.prototype.getOriginPointByShape = function(pl) {
		var ge=this.ge;
		var cmk = this.getComposeMarkerByShape(pl);
		var point = ge.createPoint('');
		point.setLatitude(Number(cmk.getLat()));
		point.setLongitude(Number(cmk.getLng()));
		return point;
	}
/**
 * 通过CMK获取图形对象
 * @param {} cmk
 * @return {}
 */
GisCellDisplayLib.prototype.getShapeObjByComposeMarker = function(cmk) {
	
	return this.ge.getElementById(cmk._cell);
}
/**
 * 创建标注
 * @param {} lng
 * @param {} lat
 * @param {} markername
 */
GisCellDisplayLib.prototype.createMarker = function(lng,lat,markername) {
	var ge=this.ge;
	// 创建地标。
	var placemark = ge.createPlacemark('');
	//placemark.setName(name);
	placemark.setDescription(markername);
	var point = ge.createPoint('');
	point.setLatitude(Number(lat));
	point.setLongitude(Number(lng));
	placemark.setGeometry(point);
	//向 Google 地球添加地标。
	ge.getFeatures().appendChild(placemark);
}
/**
 * 通过小区名移动地图
 * @param {} cell
 * @return {Boolean}
 */
GisCellDisplayLib.prototype.panToCell = function(cell) {
	// console.log("panToCell cell="+cell);
	if (!cell) {
		return false;
	}

	// 找到cell
	var pl = this.ge.getElementById(cell);
	if (pl) {
		try {
			var cmk=this.getComposeMarkerByShape(pl);
			this.panTo(cmk.getLng(), cmk.getLat());
			// console.log("succ");
			return true;
		} catch (err) {
			// console.error(err);
		}
	}
	// console.log("pl not found!");
	return false;
}
/**
 * 获取显示的标题内容
 * @param {} polygon
 * @return {String}
 */
GisCellDisplayLib.prototype.getTitleContent = function(polygon) {
	if (!polygon) {
		return "";
	}
	var composeMark = this.getComposeMarkerByShape(polygon);
	if (!composeMark) {
		return "";
	}
	var cellArray = composeMark.getCellArray();
	var html = "";
	var cell;
	for ( var i = 0; i < cellArray.length; i++) {
		cell = cellArray[i];
		html += cell.chineseName ? (cell.chineseName + "" + (cell.cell ? "["
				+ cell.cell + "]" : "")) : cell.cell;
		html += "<br/>";
	}
	return html;
}
/**
 * 获取偏移量
 * @param {} m
 * @param {} n
 * @return {}
 */
GisCellDisplayLib.prototype.getOffsetSize = function(m,n) {
	return null;
}
/**
 * 设置缺省光标
 * @param {} m
 * @param {} n
 */
GisCellDisplayLib.prototype.setDefaultCursor = function(defaultCursor) {
	return null;
}
/**
 * 获取缺省光标
 */
GisCellDisplayLib.prototype.getDefaultCursor=function(){
	return null;
}
/**
 * 创建点坐标
 * @param {} lng
 * @param {} lat
 * @return {}
 */
GisCellDisplayLib.prototype.createPoint = function(lng,lat) {
	var ge=this.ge;
	var point = ge.createPoint('');
	point.setLatitude(Number(lat));
	point.setLongitude(Number(lng));
	return point;
}
/**
 * 创建信息窗口
 */
GisCellDisplayLib.prototype.createInfoWindow = function(content, opts, shownow,
		point) {
	
}
/**
 * @author Liang YJ
 * @date 2014-3-20 10:45
 * @param options
 * option数组
 * 百度地图的颜色格式，fillColor:#rrggbb,fillOpacity:0-1，需要转换成ge的颜色格式aabbggrr
 * @description 设定单个circlePlacemark的显示颜色
 */
GisCellDisplayLib.prototype.initOptions = function (newOptions){
	/*var ge = this.ge;
	var options = this.getOptions();
	if(null === newOptions || 0 === newOptions.length){
		return null;
	}
	for(var i=0; i<newOptions.length; i++){
		var option = newOptions[i];
		var color = toGePolygonStyle(option);
		//options.push({"fillColor":color});
		options[option["fillColor"]] = color;
	}
	//return options;
*/
}
/**
 * @author Liang YJ
 * @date 2014-3-20 11:11
 * @description 清空dropts数组
 */
GisCellDisplayLib.prototype.releaseOptions = function (){
	var options = this.getOptions();
	/*for(var i=0; i<options.length; i++){
		options[i].release();
	}*/
	options.length = 0;
}
/**
 * @author Liang YJ
 * @date 2014-3-13 17:28
 * @param option
 * 百度地图的颜色格式，fillColor:#rrggbb,fillOpacity:0-1，需要转换成ge的颜色格式aabbggrr
 * @description 设定单个circlePlacemark的显示颜色
 */
GisCellDisplayLib.prototype.setCircleSingleStyle = function(option){
	var polyColor = toGePolygonStyle(option);
	this.getCircleSingleStyle.getPolyStyle().getColor().set(polyColor);
}
/**
 * @author Liang YJ
 * @date 2014-3-13 17:28
 * @param color
 * 百度地图的颜色格式，#rrggbb，需要转换成ge的颜色格式aabbggrr
 * @description 设定多个circlePlacemark重叠的显示颜色
 */
GisCellDisplayLib.prototype.setCircleMultiStyle = function(option){
	var polyColor = toGePolygonStyle(option);
	this.getCircleMultiStyle.getPolyStyle().getColor().set(polyColor);
}
/**
 * @author Liang YJ
 * @date 2014-3-13 18:28
 * @param color
 * 百度地图的颜色格式，#rrggbb，需要转换成ge的颜色格式aabbggrr
 * @description 设定单个linePlacemark的显示颜色
 */
GisCellDisplayLib.prototype.setLineSingleStyle = function(option){
	var lineColor = toGePolygonStyle(option);
	this.getLineSingleStyle.getLineStyle().getColor().set(lineColor);
	
}
/**
 * @author Liang YJ
 * @date 2014-3-24 11:58
 * @param polygon
 * @description 根据地图图元返回对应的小区label
 * 
 * 
 */
GisCellDisplayLib.prototype.getPolygonCell = function(placemark){
	return placemark.getId();
}
/**
 * @author Liang YJ
 * @date 2014-3-12 16:30
 * @description 获取地图对象
 */
GisCellDisplayLib.prototype.setMap = function(map) {
	this.ge = map;
}
/**
 * @author Liang YJ
 * @date 2014-3-14 15:06
 * @description  判断某个点的坐标时候在窗口的经纬度区间内
 */
GisCellDisplayLib.prototype.containPoint = function(point) {
	var ge = this.ge;
	//获取窗口边界
	var bounds = ge.getView().getViewportGlobeBounds();
	//窗口右边界,范围[-180,180],负数表示西经，正数表示东经
	var east = bounds.getEast();
	//窗口右边界,范围和右边界一样
	var west = bounds.getWest();
	//窗口上边界,范围[-90,90]，负数表示南纬，正数表示北纬
	var north = bounds.getNorth();
	//窗口下边界,范围和上边界一样
	var south = bounds.getSouth();
	
	var flag = false;
	if(point["lng"] <= east && point["lng"] >= west){
		flag = true;
	}else{
		return flag;
	}
	
	if(point["lat"] <= north && point["lat"] >= south){
		return flag;
	}else{
		flag = false;
		return flag;
	}
	
}


/**
 * @author Liang YJ
 * @param point1
 * 线段的一个端点
 * @param point2
 * 线段的另一个端点
 * @param option
 * 线段的样式，如颜色、宽度等，可选
 * @param name
 * 线段的名称，点击线段可以显示该线段的名称，可选
 * @param id
 * 线段的id(string),可选
 * @reutrn linePlacemark
 * 利用马甲模式封装后的ge的linePlacemark
 * @date 2014-3-13 18:05
 * @description 根据两点的位置和样式在谷歌地图上画线段
 */

GisCellDisplayLib.prototype.createLinePlacemark = function(point1,point2,option,name,id) {
	var me = this;
	var ge = this.ge;
	if(!id){
		id = "";
	}
	var linePlacemark = ge.getElementById(id);
	if(linePlacemark)
	{
		//LinePlacemarkWrap(newPoint1,newPoint2,newOption,newLine,newGeLib)
		return new LinePlacemarkWrap(point1,point2,option,linePlacemark,me);
	}
	linePlacemark = ge.createPlacemark(id);
	if(null != name && undefined != name){
		linePlacemark.setName(name);
	}
	var line = ge.createLineString("");
	var altitude = 0;
	var coordinates = line.getCoordinates();
	coordinates.pushLatLngAlt(point1["lat"],point1["lng"],altitude);
	coordinates.pushLatLngAlt(point2["lat"],point2["lng"],altitude);
	var style = ge.createStyle("");
	//使用默认的颜色
  	if(null == option || undefined == option || null ==option['strokeColor']){
  		style.getLineStyle().getColor().set("FFFFFFFF");//默认是白色
  		style.getLineStyle().setWidth(1);//默认是1px
  	}else{
  		//查找是否缓存option
  		var options = this.getOptions();
  		var key = "l"+option["fillColor"];
  		if(options[key]){
  			//直接使用缓存中的颜色和宽度
  			var catchOption = options[key];
  			style.getLineStyle().getColor().set(catchOption["color"]);
  	  		style.getLineStyle().setWidth(catchOption["width"]);
  		}else{
  			//使用调用者提供的颜色
  	  		var newOption = toGeLineStyle(option);
  	  		//将新的option缓存起来
  	  		options[key] = newOption;
  	  		style.getLineStyle().getColor().set(newOption["color"]);
  	  		style.getLineStyle().setWidth(newOption["width"]);
  		}
  		
  	}
  	linePlacemark.setStyleSelector(style);
  	linePlacemark.setGeometry(line);
  	ge.getFeatures().appendChild(linePlacemark);
  	return new LinePlacemarkWrap(point1,point2,option,linePlacemark,me);
	
	
}
/**
 * @author Liang YJ
 * @date 2014-3-20 19:08
 * @param cell
 * 小区label
 * @description 获取三角形底边的中点
 */
GisCellDisplayLib.prototype.getCellEdgeCenter=function(cell){
	var placemark = this.cellToPolygon[cell];
	if(placemark){
		var nodes = placemark.getGeometry().getGeometries().getChildNodes();
		var polygon = null;
		for(var i=0; i<nodes.getLength(); i++){
			var node = nodes.item(i);
			if("KmlPolygon"==node.getType()){
				polygon = node;
				break;
			}
		}
		if(!polygon){
			return;
		}
		var coordinates = polygon.getOuterBoundary().getCoordinates();
		var point1 = coordinates.get(1);
		var point2 = coordinates.get(2);
		var lng = (point1.getLongitude()+point2.getLongitude())/2;
		var lat = (point1.getLatitude()+point2.getLatitude())/2;
		return {"lng":lng,"lat":lat};
	}else{
		return null;
	}
}
/**
 * @author Liang YJ
 * @date 2014-3-13 14:52
 * @description 利用马甲模式封装了圆形placemark,用于在google地图上显示测量点
 */

function CirclePlacemarkWrap(newCenterPoint,newRadius,newCirclePlacemark,newGeLib){
	var geLib = newGeLib;
	var circlePlacemark = newCirclePlacemark;
	var centerPoint = newCenterPoint;
	var radius = newRadius;
	var mapElement = null;
	var elementData = null;
	
	this.getGeLib = function (){
		return geLib;
	};
	this.setGeLib = function (newGeLib){
		geLib = newGeLib;
	};
	
	this.getCirclePlacemark = function (){
		return circlePlacemark;
	};
	this.setCirclePlacemark = function (newCirclePlacemark){
		circlePlacemark = newCirclePlacemark;
	};
	
	this.getCenterPoint = function (){
		return centerPoint;
	};
	this.setCenterPoint = function (newCenterPoint){
		centerPoint = newCenterPoint;
	};
	
	this.getRadius = function (){
		return radius;
	};
	this.setRadius = function (newRadius){
		radius = newRadius;
	};
	
	this.getMapElement = function (){
		return mapElement;
	};
	this.setMapElement = function (newMapElement){
		mapElement = newMapElement;
	};
	
	this.getData = function (){
		return elementData;
	};
	this.setData = function (newElementData){
		elementData = newElementData;
	};
	
	this.getName = function (){
		return this.getCirclePlacemark().getName();
	};
	this.setName = function (newName){
		this.getCirclePlacemark().setName(newName);
	};
	
	this.getId = function (){
		return this.getCirclePlacemark().getId();
	};
	
	
}
/**
 * @author Liang YJ
 * @date 2014-3-13 10:23
 * @description 获取圆的中心点的坐标
 */
CirclePlacemarkWrap.prototype.getCenter = function (){
	return this.getCenterPoint();
};

/**
 * @author Liang YJ
 * @date 2014-3-13 10:45
 * @description 改变圆形图元的颜色
 */
CirclePlacemarkWrap.prototype.setFillColor = function (option){
	var options = this.getGeLib().getOptions();
	var ge = this.getGeLib().ge;
	if(null != option && undefined != option){
		//查找是否有现成的style
		if(options[option["fillColor"]]){
			//console.log("initOptions转换后的颜色："+opt["fillColor"]);
			this.getCirclePlacemark().getStyleSelector().getPolyStyle().getColor().set(options[option["fillColor"]]);
		}else{
			//如果没有缓存缓存颜色，则使用新颜色，并将颜色缓存
			var newOption = toGePolygonStyle(option);
			options[option["fillColor"]] = newOption;
			this.getCirclePlacemark().getStyleSelector().getPolyStyle().getColor().set(newOption);
		}
		
	}
};

/**
 * @author Liang YJ
 * @date 2014-3-13 11:00
 * @description 设定该图元时候显示
 */
CirclePlacemarkWrap.prototype.setIsShow = function (flag){
	this.getCirclePlacemark().setVisibility(flag);
};

/**
 * 
 */
CirclePlacemarkWrap.prototype.addEventListener = function (eventType,handler){
	var me = this;
	var circlePlacemark = me.getCirclePlacemark()
	if("function" == typeof handler)
	{
		google.earth.addEventListener(circlePlacemark,eventType,function(event){
			handler(me,event);
			//console.log("绑定了");
		});
	}
};

/**
 * @author Liang YJ
 * @date 2014-3-13 11:05
 * @description 获取该图元时候被显示的信息，如果被显示则返回true,否则返回false
 */
CirclePlacemarkWrap.prototype.getIsShow = function (){
	return this.getCirclePlacemark().getVisibility();
};

/**
 * @author Liang YJ
 * @date 2014-3-13 11:25
 * @description 释放图元，即将图元从地图上删除
 */
CirclePlacemarkWrap.prototype.release = function (){
	var circlePlacemark = this.getCirclePlacemark();
	var ge = this.getGeLib().ge;
	if(circlePlacemark){
		ge.getFeatures().removeChild(circlePlacemark);
		circlePlacemark.release();
	}
};





/**
 * @author Liang YJ
 * @param newPoint1
 * 线段的一个端点
 * @param newPoint2
 * 线段的另外一个端点
 * @param newOption
 * 线段的样式包括宽度和颜色等
 * @param newLine
 * 由google earth api封装的在地图上显示的线段
 * @param newGeLib
 * 封装后的google earth类库
 * @date 2014-3-13 17:47
 * @description 利用马甲模式封装了LinePlacemark,用于在google地图上显示线段
 */
function LinePlacemarkWrap(newPoint1,newPoint2,newOption,newLine,newGeLib){
	var point1 = newPoint1;
	var point2 = newPoint2;
	var option = newOption;
	var line = newLine;
	var geLib = newGeLib;
	
	this.getPoint1 = function (){
		return point1;
	};
	this.setPoint1 = function (newPoint1){
		point1 = newPoint1;
	};
	
	this.getPoint2 = function (){
		return point2;
	};
	this.setPoint2 = function (newPoint2)
	{
		point2 = newPoint2;
	};
	
	this.getOption = function (){
		return option;
	};
	this.setOption = function (newOption){
		option = newOption;
	};
	
	this.getLine = function (){
		return line;
	};
	this.setLine = function (newLine){
		line = newLine;
	};
	
	this.getGeLib = function(){
		return geLib;
	};
	this.setGeLib = function(newGeLib){
		geLib = newGeLib;
	};
	
	this.getName = function (){
		return this.getLine().getName();
	};
	this.setName = function (newName){
		this.getLine().setName(newName);
	};
	
	this.getId = function (){
		return this.getLine().getId();
	};

};
/**
 * @author Liang YJ
 * @date 2014-3-14 10:36
 * @description 删除线段
 */
LinePlacemarkWrap.prototype.release = function (){
	var line = this.getLine();
	var ge = this.getGeLib().ge;
	if(line){
		ge.getFeatures().removeChild(line);
		line.release();
	}
};

/**
 * @author Liang YJ
 * @date 2014-3-21 10:50
 * @description 返回线段的两个端点组成的数组
 */
LinePlacemarkWrap.prototype.getPath = function (){
	var pointArr = new Array();
	pointArr.push(this.getPoint1());
	pointArr.push(this.getPoint2());
	return pointArr;
};

/**
 * @author Liang YJ
 * @date 2014-3-16 15:32
 * @param eventType
 * 事件类型，如click,change
 * @param handler
 * 时间处理函数
 */
LinePlacemarkWrap.prototype.addEventListener = function (eventType,handler){
	var me = this;
	var line = me.getLine();
	if("function" == typeof handler){
		google.earth.addEventListener(line,eventType,function(event){
			//console.log("绑定了线");
			handler(me,event);
		});
	}
	
};
/**
 * https://www.google.com/accounts/ManageAccount
 * var map = new GMap2(document.getElementById("map3d"));
 * Google Map通过gMap.setMapType(G_SATELLITE_3D_MAP)直接把地图切换至Google Earth
 * Google Map通过gMap.setMapType(G_NORMAL_MAP)可以隐藏Google Earth，地图切换至其他类型
 */

