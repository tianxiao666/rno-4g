/**
 * openstreetmap_leaflet用于在地图上控制giscell的显示类
 * 
 * @param map
 * @param minZoom
 * @param randomShowCnt
 * @param colors
 * @param responseFunction
 * @returns
 */

function GisCellDisplayLib(map, minZoom, randomShowCnt, colors,
		responseFunction, configOption,contextMenu) {

	this.map = map;
	this.minZoom = minZoom ? minZoom : 16;
	this.randomShowCnt = randomShowCnt ? randomShowCnt : 50;
	this.singleColor = colors ? (colors['singleColor'] ? colors['singleColor']
			: "#0B4C5F") : '#0B4C5F';
	this.multiColor = colors ? (colors['multiColor'] ? colors['multiColor']
			: "#FF00FF") : '#FF00FF';

	var defaultClickFunction = function() {
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
	var defaultrightclickFunction = function(){
	};
	this.rightclickFunction= responseFunction ? (responseFunction['rightclickFunction'] ? responseFunction['rightclickFunction']
			: defaultrightclickFunction)
			: defaultrightclickFunction;

	this.allAllowShapes=['polygon','polyline'];
	this.showCellLabel = true;// 是否显示小区名称
	this.shape="polygon";//默认以polygon显示
	if (configOption) {
		if (configOption['showCellLabel'] != null
				&& configOption['showCellLabel'] != undefined) {
			this.showCellLabel = configOption['showCellLabel'];
		}
		if($.inArray(configOption['shape'],this.allAllowShapes)){
			this.shape=configOption['shape'];
		}
	}
	
	var me=this;
	var defaultcontextMenu=[
	{
						text:'搜索邻区',
						callback:function(){
							var polygon;
						if (defaultcontextMenu.length!=0) {
							polygon=defaultcontextMenu[defaultcontextMenu.length-1].polygon;
							defaultcontextMenu.splice(defaultcontextMenu.length-1,1);
//							console.log("callback:defaultcontextMenu回调函数："+defaultcontextMenu);
							}
//							console.log(polygon._data.getCell());
							me.responseRightClickForPolygon(polygon,defaultcontextMenu);
							}
						}
	];
	this.contextMenu=contextMenu?contextMenu: defaultcontextMenu;

	this.preZoom = minZoom;
	this.DiffAzimuth = 1;
	this.DiffDistance = 5;
	this.firstTime = true;
	this.lastOperTime = 0;

	this.composeMarkers = new Array();
	this.allPolygons = new Array();
	this.visiblePolygons = new Array();
	this.specialPolygons = new Array();// 特殊渲染的外观
	this.cellToPolygon = new Object();// 小区到polygon的映射
	this.extraMapOverlays = new Array();// 额外的覆盖物。与specialPolygons的区别是：specialPolygons指的还是小区，而这个extraMapOverlays是额外生成的，如用于标识小区的label覆盖物

	this.sameLnglatPolyArray = new Object;// 相同起点经纬度的poly数组，key为：lng+"_"+lat,value为polygon数组
}

GisCellDisplayLib.prototype.setMap = function(map) {
	this.map = map;
}

GisCellDisplayLib.prototype.getPolygonCnt = function() {
	return this.allPolygons.length;
}

// 设置图元的点击响应函数
GisCellDisplayLib.prototype.setClickFunction = function(fun) {
	if (fun instanceof Function) {
		this.clickFunction = fun;
	}
}
// 设置图元的鼠标移动过响应函数
GisCellDisplayLib.prototype.setMouseoverFunction = function(fun) {
	if (fun instanceof Function) {
		this.mouseoverFunction = fun;
	}
}
// 设置图元的右键响应函数
GisCellDisplayLib.prototype.setRightclickFunction = function(fun) {
	if (fun instanceof Function) {
		this.rightclickFunction = fun;
	}
}
/**
 * 清除成员变量数据
 * @author chao.xj
 */
GisCellDisplayLib.prototype.clearData = function() {
	this.composeMarkers.splice(0, this.composeMarkers.length);
	var map = this.map;

	var allPolygons = this.allPolygons;
	for ( var i = 0; i < allPolygons.length; i++) {
		// map.removeOverlay(allPolygons[i]);
		this.hideOnePolygon(allPolygons[i]);
	}
	allPolygons.splice(0, allPolygons.length);
	// 相同经纬度起点的
	this.sameLnglatPolyArray = null;
	this.sameLnglatPolyArray = new Object();

	// 可见
	this.visiblePolygons.splice(0, this.visiblePolygons.length);
	// 特殊、额外图元
	this.specialPolygons.splice(0, this.specialPolygons.length);

	// 额外
	var extraMapOverlays = this.extraMapOverlays;
	for ( var i = 0; i < extraMapOverlays.length; i++) {
//		map.removeOverlay(extraMapOverlays[i]);
		map.removeLayer(extraMapOverlays[i]);
	}
	extraMapOverlays.splice(0, extraMapOverlays.length);

	// hash表
	this.cellToPolygon = null;
	this.cellToPolygon = new Object();

	// 关闭信息窗
	this.closeInfoWindow();
}

/**
 * 只清除额外覆盖物
 * @author chao.xj
 */
GisCellDisplayLib.prototype.clearOnlyExtraOverlay = function() {
  var map = this.map;
	var extraMapOverlays = this.extraMapOverlays;
	for ( var i = 0; i < extraMapOverlays.length; i++) {
//		map.removeOverlay(extraMapOverlays[i]);
		map.removeLayer(extraMapOverlays[i]);
	}
	extraMapOverlays.splice(0, extraMapOverlays.length);
}
/**
 * 显示gis小区for地图
 * @param {} data
 * @param {} key 当key不存在默认显示gis小区，key有值就显示其他类型小区，例如lte小区可用‘lcid’做key
 * @author chao.xj
 */
GisCellDisplayLib.prototype.showGisCell = function(data,key) {
	// console.log("in GisCellDisplayLib.prototype.showGisCell");
	if (!data) {
		return;
	}
	try {
		var composeMarkers = this.composeMarkers;
		var allPolygons = this.allPolygons;
		var visiblePolygons = this.visiblePolygons;
		var cellToPolygon = this.cellToPolygon;
		var map = this.map;
		var multiColor = this.multiColor;
		var singleColor = this.singleColor;

		var start = composeMarkers.length;// 新获取的小区对象在数组中的起始位置
		var cmk;
		var j = 0;
		var polygon;
		// O(n*n)
		for ( var i = 0; i < data.length; i++) {
			var gisCell = data[i];
			j = 0;
			for (j = 0; j < composeMarkers.length; j++) {
				cmk = composeMarkers[j];
				if (cmk.similiar(gisCell, this.DiffAzimuth, this.DiffDistance)) {
					cmk.addCell(gisCell,key);
					cellToPolygon[cmk.getCell()] = allPolygons[j];// 小区到polygon
					if (cmk.getCount() === 2) {
						// 重新渲染
						polygon = allPolygons[j];
						// console.log("旧的polygon" + polygon);
						if (polygon) {
//							polygon.setFillColor(multiColor);
							polygon.setStyle({fillColor:multiColor});
						}
					}
					break;
				}
			}
			// console.log("j=" + j);
			if (j >= composeMarkers.length) {
				// console.log("准备添加进单独的marker");
				//peng.jm加入 2014年6月11日10:26:22
				var onecmk = new ComposeMarker(gisCell,key);
				
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
			polygon = this.createPolygonFromComposeMark(cmk);
			//this.rightClickMenuItemForPolygon(polygon,typeof txtMenuItem=="undefined"?null:txtMenuItem);//polygon对象创建右键菜单
			// console.log("create polygon = "+polygon);
			allPolygons.push(polygon);
			polygon._isShow = false;
			cellToPolygon[cmk.getCell()] = polygon;// cell to polygon的hash
			// 创建一个marker
			// var tempmk=new BMap.Marker(new

			// 是否要显示？
			var visib = this.shouldDisplay(polygon);
			if (visib === true) {
				// console.log("可见，将在地图显示");
				// polygon._isShow = true;
				// map.addOverlay(polygon);
				this.showOnePolygon(polygon);
				visiblePolygons.push(polygon);
			}
		}
	} catch (err) {
		console.error(err);
	}
}

/**
 * 判断多边形是否要显示
 * @param {} polygon
 * @return {}
 * @author chao.xj
 */
GisCellDisplayLib.prototype.shouldDisplay = function(polygon) {
	// console.log("in GisCellDisplayLib.prototype.shouldDisplay");
	// 如果不在可见区域内，肯定不显示
	var visib = false;
	var map = this.map;
	if (!map.getBounds().contains(
			L.latLng(polygon._data.getLat(), polygon._data.getLng()))) {
		// console.log("不在当前视野内。");
		visib = false;
	} else {
		// 结合缩放级别、可见区域
		if (map.getZoom() < this.minZoom) {
			// 在不需要全部显示的范围内
			// 看一下随机显示的数量达到上限没有
			if (this.visiblePolygons.length < this.randomShowCnt) {
				visib = true;
			} else {
				visib = false;
				// console.log("超过随机显示数量");
			}
		} else {
			// 在可见区域内，且在需要全部显示的缩放级别范围内
			visib = true;
		}
	}
	// console.log("visible == "+visib);
	return visib;

}

/**
 * 从聚合对象创建polygon
 * 
 * @param {}
 *            cmk
 * @return {}
 * @author chao.xj
 */
GisCellDisplayLib.prototype.createPolygonFromComposeMark = function(cmk) {
	try {
		if (!cmk) {
			console.log("空的参数");
			return null;
		}
		var me = this;
		var pa = cmk.getPointArray();
/*		var polygon = new BMap.Polygon(pa, cmk.getPolygonOptions(
				me.singleColor, me.multiColor));*/
		var polygon = L.polygon(pa,cmk.getPolygonOptions(me.singleColor, me.multiColor));//.addTo(me.map);//.bindPopup("I am a polygon.");//.openPopup();
		//{fillColor:,weight:1,fillOpacity:1}
//		var p4=new BMap.Point((pa[1].lng+pa[2].lng)/2,(pa[1].lat+pa[2].lat)/2);
//		polygon=new BMap.Polyline([pa[0],p4],{strokeWeight:1});
		//console.log("polygon:"+polygon);
		
		polygon._data = cmk;// 相互引用
		cmk.setPolygon(polygon);
		
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
			/*	edgePosition = new BMap.Point((p1.lng + p2.lng) / 2,
						(p1.lat + p2.lat) / 2);*/
				edgePosition = L.latLng((p1.lat + p2.lat) / 2,(p1.lng + p2.lng) / 2);
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
			if (edgePosition != null) {

				if (angle > 180) {
					angle = angle - 180;
					labelPosition = edgePosition;
				} else {
					labelPosition = startPosition;
				}
				angle = angle - 90;
				/*var label = new BMap.Label(cellname, {
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
				});*/
				var myIcon = L.icon({
				iconUrl: ' ',
				iconSize: [80, 20]
				});
				var latlng=me.getCellCenterPoint(cmk);
				var label=L.marker(latlng,{alt:cmk._chineseName,icon:myIcon});//.addTo(me.map);//.bindPopup(markername).openPopup();
				//console.log("label:"+label._icon.alt);
				polygon._label = label;
			}
		}
		// /

		polygon.on('click', function(event) {
			me.clickFunction(this, event);
		});
		polygon.on('mouseover', function(event) {
			me.mouseoverFunction(this, event);
		});
		polygon.on('mouseout', function(event) {
			me.mouseoutFunction(this, event);
		});
//		console.log("contextMenu?contextMenu:defaultcontextMenu:"+me.contextMenu);
		/*polygon.on('contextmenu', function(e) {
    	var my = document.getElementById("searchNcell");
				 if (my != null)
        				my.parentNode.removeChild(my);
    					me.rightClickMenuItemForPolygon(e.containerPoint.x, e.containerPoint.y, 100, 18,this,typeof me.contextMenu=="undefined"?null:me.contextMenu);
		});*/
//		me.rightClickMenuItemForPolygon(polygon,typeof txtMenuItem=="undefined"?null:txtMenuItem);
		
		polygon.on('contextmenu', function(event) {
			//me.rightClickMenuItemForPolygon(polygon,typeof txtMenuItem=="undefined"?null:txtMenuItem);//polygon对象创建右键菜单
//			console.log("me.contextMenu:"+me.contextMenu);
			var my = document.getElementById("searchNcell");
				 if (my != null)
        			my.parentNode.removeChild(my);
    		me.rightClickMenuItemForPolygon(event.containerPoint.x, event.containerPoint.y, 100, 18,this,typeof me.contextMenu=="undefined"?null:me.contextMenu);
			me.clearOnlyExtraOverlay();//清除额外覆盖物
			me.resetPolygonToDefaultOutlook();//恢复默认polygon颜色
			var aa=typeof me.contextMenu=="undefined"?null:me.contextMenu;
			//typeof me.contextMenu!='undefined' && me.contextMenu!=null
			
//			console.log("aa:"+aa.length);
			if (aa.length!=0) {
				//var obj=eval("("+txtMenuItem+")");
				var bb={'polygon':polygon};
				aa.push(bb);
				me.contextMenu=aa;
				//console.log("txtMenuItem="+txtMenuItem[txtMenuItem.length-1].polygon);
			}
//		  console.log("me.contextMenu:"+me.contextMenu.length);
		  me.rightclickFunction(polygon,event);
		//me.rightClickMenuItemForPolygon(polygon,aa);//polygon对象创建右键菜单
		//me.rightclickFunction(polygon,aa);
});
		return polygon;
	} catch (err) {
		console.error(err);
		return null;
	}
}

GisCellDisplayLib.prototype.setShowLabel=function(showOrNot){
	this.showCellLabel=showOrNot;
}

/**
 * 显示一个多边形
 * @param {} pl
 * @author chao.xj
 */
GisCellDisplayLib.prototype.showOnePolygon = function(pl) {
	var map = this.map;
	if (pl) {
		try {
			pl._isShow = true;
			if (this.showCellLabel==true && pl._label) {
//				map.addOverlay(pl._label);
				map.addLayer(pl._label);
			}

		} catch (err) {

		}
		try {
//			map.addOverlay(pl);
			map.addLayer(pl);
		} catch (err) {

		}
	}
}
/**
 * 隐藏一个多边形对象:即删除该图层
 * @param {} pl
 * @author chao.xj
 */
GisCellDisplayLib.prototype.hideOnePolygon = function(pl) {
  var map = this.map;
	if (pl) {
		try {
			pl._isShow = false;
			if (pl._label) {
//				map.removeOverlay(pl._label);
				map.removeLayer(pl._label);
			}
		} catch (err) {

		}
		try {
			//map.removeOverlay(pl);
			map.removeLayer(pl);
		} catch (err) {

		}
	}
}

/**
 * 处理缩放事件
 * @param {} e
 * @param {} occurTime
 * @author chao.xj
 */
GisCellDisplayLib.prototype.handleZoomEnd = function(e, occurTime) {
	// console.log("---zoomend happened....");
	var me = this;
	if (occurTime != null && occurTime != undefined) {
		me.lastOperTime = occurTime;
	}
	var ct = new Date().getTime();
	if (ct - me.lastOperTime < me.minResponseInterval) {
		// 开始处理缩放事件
		// 一秒后检查
		// console.log("一秒后检查。。。。");
		window.setTimeout(function() {
			me.handleZoomEnd(e);
		}, 1000);
		return;
	}

	var map = this.map;
	var minZoom = this.minZoom;
	var preZoom = this.preZoom;
	var allPolygons = this.allPolygons;
	var visiblePolygons = this.visiblePolygons;

	var czoom = map.getZoom();
	var bounds = map.getBounds();

	if (czoom >= minZoom && preZoom >= minZoom || czoom < minZoom
			&& preZoom < minZoom) {
		if (czoom < minZoom) {
			return;// 在临界级别以下操作，不管放大，还是缩小，不理
		}
		// 下面处理的是缩放都在临界的上边
		if (czoom > preZoom) {
			// 放大，减小显示
			this.preZoom = czoom;
			var pl;
			var deleIndex = new Array();
			var newvisib = new Array();
			for ( var i = 0; i < visiblePolygons.length; i++) {
				pl = visiblePolygons[i];
				if (!bounds.contains(L.latLng(pl._data.getLat(), pl._data.getLng()))) {
					// console.log("减少显示。");
					// pl._isShow = false;// 不显示
					// map.removeOverlay(pl);
					this.hideOnePolygon(pl);
				} else {
					newvisib.push(pl);
				}
			}
			this.visiblePolygons.splice(0, this.visiblePolygons.length);
			this.visiblePolygons = newvisib;
		} else {
			// 缩小，增加显示
			var pl;
			for ( var i = 0; i < allPolygons.length; i++) {
				pl = allPolygons[i];
				if (bounds.contains(L.latLng(pl._data.getLat(), pl._data.getLng()))
						&& pl._isShow == false) {
					// console.log("增加显示");
					// 在可见区域内，且当前未显示
					visiblePolygons.push(pl);
					// pl._isShow = true;
					// map.addOverlay(pl);
					this.showOnePolygon(pl);
				}
			}
			this.preZoom = czoom;
			return;
		}
	} else {
		if (preZoom >= minZoom && czoom < minZoom) {
			this.preZoom = czoom;
			// 由临界上面缩小到临界下面
			// 看是否增加显示:如果数量已经大于随机显示数量， 则不用显示；否则尽量增加
			var pl;
			for ( var i = 0; i < allPolygons.length
					&& visiblePolygons.length < this.randomShowCnt; i++) {
				pl = allPolygons[i];
				if (bounds.contains(L.latLng(pl._data.getLat(), pl._data.getLng()))
						&& pl._isShow == false) {
					// console.log("增加显示");
					// 在可见区域内，且当前未显示
					visiblePolygons.push(pl);
					// pl._isShow = true;
					// map.addOverlay(pl);
					this.showOnePolygon(pl);
					if (visiblePolygons.length >= this.randomShowCnt) {
						break;
					}
				}
			}

		} else if (preZoom < minZoom && czoom >= minZoom) {
			this.preZoom = czoom;
			// 由临界下面放大到临界上面
			// 增加显示，扫描全部，原来可见的还是保留可见。
			var pl;
			for ( var i = 0; i < allPolygons.length; i++) {
				pl = allPolygons[i];
				if (bounds.contains(L.latLng(pl._data.getLat(), pl._data.getLng()))
						&& pl._isShow == false) {
					// console.log("增加显示");
					// 在可见区域内，且当前未显示
					visiblePolygons.push(pl);
					// pl._isShow = true;
					// map.addOverlay(pl);
					this.showOnePolygon(pl);
				}
			}
		}

	}

}

/**
 * 处理移动事件:为了控制小区在可视地图范围内或某一级别下的显示数据
 * @param {} e
 * @param {} occurTime
 * @author chao.xj
 */
GisCellDisplayLib.prototype.handleMoveEnd = function(e, occurTime) {

	var me = this;
	if (occurTime != null && occurTime != undefined) {
		me.lastOperTime = occurTime;
	}

	var ct = new Date().getTime();
	if (ct - me.lastOperTime < me.minResponseInterval) {
		// console.log("too frequently.");
		window.setTimeout(function() {
			me.handleMoveEnd(e);
		}, 1000);
	}

	var map = this.map;
	var visiblePolygons = this.visiblePolygons;
	var minZoom = this.minZoom;
	var randomShowCnt = this.randomShowCnt;
	var allPolygons = this.allPolygons;

	var czoom = map.getZoom();
	var pl;
	var bounds = map.getBounds();
	visiblePolygons.splice(0, visiblePolygons.length);// 清空
	for ( var i = 0; i < allPolygons.length; i++) {
		// 如果小于minZoom，则最多显示randomShowCnt个;否则全部可见的都要显示
		pl = allPolygons[i];
		
		if (!bounds.contains(L.latLng(pl._data.getLat(), pl._data.getLng()))) {
			// 不包含
			if (pl._isShow == true) {
				// pl._isShow = false;
				// map.removeOverlay(pl);
				this.hideOnePolygon(pl);
			}
		} else {
			if (czoom < minZoom) {
				if (visiblePolygons.length >= randomShowCnt) {
					// 对于可见的，则设置为不可见
					if (pl._isShow == true) {
						// pl._isShow = false;
						// map.removeOverlay(pl);
						this.hideOnePolygon(pl);
					}
					continue;
				}
			}
			// 设置为可见
			if (pl._isShow == false) {
				// pl._isShow = true;
				// map.addOverlay(pl);
				this.showOnePolygon(pl);
			}
			visiblePolygons.push(pl);

		}
	}
}

/**
 * 改变小区对应的polygon的外观
 * 
 * @param cell
 * @param option
 * @param puttospec
 *            是否添加到特殊渲染队列（待reset的时候，就只考虑这个队列里的就行了）
 * @returns {Boolean}
 * @author chao.xj
 */
GisCellDisplayLib.prototype.changeCellPolygonOptions = function(cell, option,
		puttospec) {
	var pl = this.cellToPolygon[cell];
	if (!pl || !option) {
		return false;
	}

	if (option['fillColor']) {
		pl.setStyle({fillColor:option['fillColor']});
	}
	if (option['strokeColor']) {
		pl.setStyle({color:option['strokeColor']});
	}
	if (option['fillOpacity']) {
		pl.setStyle({fillOpacity:option['fillOpacity']});
	}
	if (option['strokeWeight']) {
		pl.setStyle({weight:option['strokeWeight']});
	}

	if (puttospec === true) {
		// 添加到特殊队
		this.specialPolygons.push(pl);
	}

	return true;
}

/**
 * 将全部图元恢复默认外观
 * @author chao.xj
 */
GisCellDisplayLib.prototype.resetPolygonToDefaultOutlook = function() {
	var map = this.map;
	var allPolygons = this.allPolygons;
	var len = allPolygons.length;
	var pl = null;
	var cmk = null;
	var option = null;
	var sc = this.singleColor;
	var mc = this.multiColor;
	for ( var i = 0; i < len; i++) {
		pl = allPolygons[i];
		cmk = pl._data;
		option = cmk.getPolygonOptions(sc, mc);
		
		pl.setStyle({fillColor:option['fillColor']});
		pl.setStyle({color:option['strokeColor']});
		pl.setStyle({fillOpacity:option['fillOpacity']});
		pl.setStyle({weight:option['strokeWeight']});
	}

	// 特殊、额外图元
	this.specialPolygons.splice(0, this.specialPolygons.length);
}

/**
 * 将特殊外观的图元恢复默认外观
 * @author chao.xj
 */
GisCellDisplayLib.prototype.resetSpecPolygonToDefaultOutlook = function() {
	var map = this.map;
	var specialPolygons = this.specialPolygons;
	var len = specialPolygons.length;
	var pl = null;
	var cmk = null;
	var option = null;
	var sc = this.singleColor;
	var mc = this.multiColor;
	for ( var i = 0; i < len; i++) {
		pl = specialPolygons[i];
		cmk = pl._data;
		option = cmk.getPolygonOptions(sc, mc);

		pl.setStyle({fillColor:option['fillColor']});
		pl.setStyle({color:option['strokeColor']});
		pl.setStyle({fillOpacity:option['fillOpacity']});
		pl.setStyle({weight:option['strokeWeight']});

	}

	// 特殊、额外图元
	this.specialPolygons.splice(0, this.specialPolygons.length);
}
/**
 * 移动至新的坐标点
 * @param {} lng
 * @param {} lat
 * @author chao.xj 
 */
GisCellDisplayLib.prototype.panTo = function(lng, lat) {
	var latlng=L.latLng(lat,lng);
	this.map.panTo(latlng);
}
/**
 * 
 * @param {} cell
 * @return {Boolean}
 * @author chao.xj
 */
GisCellDisplayLib.prototype.panToCell = function(cell) {
	// console.log("panToCell cell="+cell);
	if (!cell) {
		return false;
	}

	// 找到cell
	var pl = this.cellToPolygon[cell];
	if (pl) {
		try {
			var point = L.latLng(pl._data.getLat(), pl._data.getLng());
			this.map.panTo(point);
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
 * 判断是否存在某个小区
 * @param {} cell
 * @return {Boolean}
 * @author chao.xj
 */
GisCellDisplayLib.prototype.existCell = function(cell) {

	if (!cell) {
		return false;
	}
	var pl = this.cellToPolygon[cell];
	if (pl) {
		return true;
	}
	return false;

}

/**
 * 获取某个小区的经纬度
 * @param {} cell
 * @return {}
 * @author chao.xj
 */
GisCellDisplayLib.prototype.getCellPoint = function(cell) {
	if (!cell) {
		return null;
	}
	var pl = this.cellToPolygon[cell];
	if (pl) {
		if (pl) {
			try {
				var point = L.latLng(pl._data.getLat(), pl._data.getLng());
				return point;
			} catch (err) {
				return null;
			}
		}
	}
	return null;
}

/**
 * 获取小区的底边的中心点
 * @author chao.xj
 */
GisCellDisplayLib.prototype.getCellEdgeCenterPoint = function(cell) {
	if (!cell) {
		return null;
	}
	var pl = this.cellToPolygon[cell];
	if (pl) {
		if (pl) {
			try {
				var pointArray = pl._data.getPointArray();
				var point = null;
				if (pointArray && pointArray.length > 2) {
					var p1 = pointArray[1];
					var p2 = pointArray[2];
					point = L.latLng((p1.lat + p2.lat) / 2, (p1.lng + p2.lng) / 2);
				}
				return point;
			} catch (err) {
				return null;
			}
		}
	}
	return null;
}
/**
 * 获取小区中心点坐标
 * @param {} cmk
 * @return {}
 * @author chao.xj
 */
GisCellDisplayLib.prototype.getCellCenterPoint = function(cmk) {
	if (!cmk) {
		return null;
	}
	var pointArray = cmk._pointArray;
		if (pointArray) {
			try {
				var point = null;
				if (pointArray && pointArray.length > 2) {
					var p1 = pointArray[1];
					var p2 = pointArray[2];
					point = L.latLng((p1.lat + p2.lat) / 2, (p1.lng + p2.lng) / 2);
				}
				return point;
			} catch (err) {
				return null;
			}
		}
	
	return null;
}

/**
 * 给指定的两个小区连线
 * 
 * @param cell
 * @param anotherCell
 * @author chao.xj
 */
GisCellDisplayLib.prototype.drawLineBetweenCells = function(cell, anotherCell,
		option) {
	if (!cell || !anotherCell) {
		return;
	}

	// 找到cell
	var pl_1 = this.cellToPolygon[cell];
	var pl_2 = this.cellToPolygon[anotherCell];
	if (pl_1 == pl_2) {
		return;
	}
	if (!pl_1 || !pl_2) {
		return;
	}
	try {
//		var point1 = new BMap.Point(pl_1._data.getLng(), pl_1._data.getLat());
//		var point2 = new BMap.Point(pl_2._data.getLng(), pl_2._data.getLat());
   
		var point1=this.getCellEdgeCenterPoint(cell);
		var point2=this.getCellEdgeCenterPoint(anotherCell);
		// 如果该连线已经存在，则不添加
		var extraMapOverlays = this.extraMapOverlays;
		var oldLine, ps;
		var need = true;
		for ( var i = 0; i < extraMapOverlays.length; i++) {
			oldLine = extraMapOverlays[i];
			if (oldLine) {
				ps = oldLine.getLatLngs();
				if (ps) {
					if (point1.equals(ps[0]) && point2.equals(ps[1])
							|| point1.equals(ps[1]) && point2.equals(ps[0])) {
						need = false;// 已经存在
					}
				}
			}
		}

		if (!need) {
			return;
		}

		// 画一条线
		//var pline = new BMap.Polyline([ point1, point2 ], option);
		var pline = L.polyline( [ point1, point2 ], option);
		this.map.addLayer(pline);
		this.extraMapOverlays.push(pline);
	} catch (err) {
		console.log(err);
	}
}

/**
 * 
 * @param contId
 *            map 容器id
 * @param lng
 *            map中心点经度
 * @param lat
 *            map中心点纬度
 * @param mapFuncs
 *            map事件回调函数 这些回调函数是在本框架的默认处理函数处理完后调用。
 * @returns
 * chao.xj  
 */
GisCellDisplayLib.prototype.initMap = function(contId, lng, lat, mapFuncs) {
	var map = this.map;
	var me = this;  
	me.map = L.map(contId);//.setView([22.922940000000000500, 113.381580000000000000], 13);
	// 初始化
	var currentAreaCenter = L.latLng(23.12489, 113.361397);
	if (lng && lng != 0 && lat && lat != 0) {
		// console.log("lng，lat有效。")
		currentAreaCenter = L.latLng(lat, lng);
	}
	me.map.setView(currentAreaCenter,me.preZoom);
	//var tilelayer=L.tileLayer('http://{s}.mapabc.com/mapabc/maptile?&x={x}&y={y}&z={z}', {subdomains: ["emap","emap1", "emap2", "emap3"],attribution: '<!-- a href="http://leafletjs.com/">Leaflet</a --> © OpenStreetMap contributors, 广东怡创'});
	var tilelayer=L.tileLayer('http://emap.mapabc.com/mapabc/maptile?&x={x}&y={y}&z={z}.jpg');
	//var tilelayer=L.tileLayer('http://mt{s}.google.cn/vt/v=w2.114&hl=zh-CN&gl=cn&x={x}&y={y}&z={z}', {subdomains: ['0', '1', '2','3'],attribution: '<!-- a href="http://leafletjs.com/">Leaflet</a --> © OpenStreetMap contributors, 广东怡创'});
   /* var tianditu_normal = L.tileLayer('http://t{s}.tianditu.cn/DataServer?T=vec_w&X={x}&Y={y}&L={z}', {
			maxZoom: 18,
			attribution: '天地图',
			subdomains:['0','1','2','3','4','5','6','7']
		});
	var MapABC_normal_map= L.tileLayer('http://emap{s}.mapabc.com/mapabc/maptile?&x={x}&y={y}&z={z}', {
			maxZoom: 18,
			attribution: 'MapABC',
			subdomains : [ '0', '1', '2', '3' ]
		});
	var google_map = L.tileLayer('http://mt{s}.google.cn/vt/v=w2.114&hl=zh-CN&gl=cn&x={x}&y={y}&z={z}', {
        	maxZoom: 18,
        	attribution: 'GoogleMap',
        	subdomains : [ '0', '1', '2', '3' ]
    	});*/
    tilelayer.addTo(me.map);
    //添加circle 
	/*L.circle([23.12489, 113.361397], 500, { 
	    color: 'red', 
	    fillColor: '#f03', 
	    fillOpacity: 0.5 
	}).addTo(me.map).bindPopup("I am a circle.").openPopup();  */
	//添加polygon                              
	/*L.polygon([                                
	    [23.12489, 113.361397],                       
	    [lat, lng]                      
	]).addTo(me.map).bindPopup("I am a polygon.");//.openPopup();
*/	
/*var polygon=L.polygon([                                
	    [22.07026412667602, 113.2511324225899],                       
	    [22.070837129834356, 113.25201546651502],                       
	    [22.07035120246339, 113.25220631466127]
	],{fillColor:"red",weight:1,fillOpacity:1}).addTo(me.map);//.bindPopup("I am a polygon.").openPopup();
	polygon._cell="S4BJWS1";
	var myIcon = L.icon({
		iconUrl: ' ',
		iconSize: [80, 20]
	});
	L.marker([(22.070837129834356+22.07035120246339)/2, (113.25201546651502+113.25220631466127)/2],{alt:"金湾区大厦1",icon:myIcon}).addTo(this.map);//.bindPopup(markername).openPopup();
*/	
    //给地图点击添加弹窗事件                                                             
	//var popup = L.popup();                                               
	/*function onMapClick(e) {                                             
	    popup                                                            
	        .setLatLng(e.latlng)                                         
	        .setContent("点击位置: " + e.latlng.toString()) 
	        .openOn(me.map);                                                
	}                                                                    
	polygon.on('click', onMapClick);*/ 
	/*polygon.on('click', function(e){
		//console.log("click:"+e.containerPoint.toString()+"---------"+this.getLatLngs().toString());
		//console.log("click小区label:"+this._cell);
		var my = document.getElementById("searchNcell");
				 if (my != null)
        				my.parentNode.removeChild(my);
	});
	polygon.on('contextmenu', function(e) {
    	//alert(e.latlng);
    	//console.log("contextmenu:"+e.layerPoint.toString());
    	var my = document.getElementById("searchNcell");
				 if (my != null)
        				my.parentNode.removeChild(my);
    					me.rightClickMenuItemForPolygon(e.containerPoint.x, e.containerPoint.y, 100, 18,this,typeof me.contextMenu=="undefined"?null:me.contextMenu);
        				
	});*/
	//http://www.lib.utexas.edu/maps/historical/newark_nj_1922.jpg
	/*var imageUrl = 'img/NET_STRUCT_FACTOR_119.png',
    imageBounds = [[22.460216, 113.067677], [22.460216, 114.417799], [21.785928, 114.417799], [21.785928, 113.067677]];
	L.imageOverlay(imageUrl, imageBounds,{opacity:0.5}).addTo(me.map);*/
    //-----------------事件开始-----------------------//
    //缩放
    	me.map.addEventListener("zoomend", function(e) {
		var lastOperTime = new Date().getTime();
		me.lastOperTime = lastOperTime;
		if (mapFuncs && typeof (mapFuncs['zoomend']) === "function") {
			// console.log("自定义zoomend方法。");
			(mapFuncs['zoomend'])(e, lastOperTime);
		} else {
			me.handleZoomEnd(e, lastOperTime);
		}

	});

	// 移动
	me.map.addEventListener("moveend", function(e) {
		var lastOperTime = new Date().getTime();
		me.lastOperTime = lastOperTime;

		if (mapFuncs && typeof (mapFuncs['moveend']) === "function") {
			mapFuncs['moveend'](e, lastOperTime);
		} else {
			me.handleMoveEnd(e, lastOperTime);
		}

	});
	//单击
	me.map.on('click', function(event){
			var my = document.getElementById("searchNcell");
				 if (my != null)
        				my.parentNode.removeChild(my);
		});
	if(typeof(mapFuncs)=="function"){
		mapFuncs();
	}
	return this.map;
	// ----------事件结束-----------------------//
}

/**
 * 获取显示的标题内容
 * @param {} polygon
 * @return {String}
 * @author chao.xj
 */
GisCellDisplayLib.prototype.getTitleContent = function(polygon) {
	if (!polygon) {
		return "";
	}
	var composeMark = polygon._data;
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
 * 创建信息窗口
 * @param {} content
 * @param {} opts
 * @param {} shownow
 * @param {} point
 * @author chao.xj
 */
GisCellDisplayLib.prototype.createInfoWindow = function(content, opts, shownow,
		point) {
	
}
/**
 * 显示信息窗口
 * @param {} content
 * @param {} point
 * @author chao.xj
 */
GisCellDisplayLib.prototype.showInfoWindow = function(content, point) {
	
}
/**
 * 关闭信息窗口
 * @author chao.xj
 */
GisCellDisplayLib.prototype.closeInfoWindow = function() {
	
}

GisCellDisplayLib.prototype.get = function(prop) {
	return this[prop];
}
/**
 * 为地图创建右键菜单
 * @param {} map
 */
/*GisCellDisplayLib.prototype.rightClickMenuItemForMap = function(map){
	
	var contextMenu = new BMap.ContextMenu();//创建右键菜单实例  
	var txtMenuItem = [  
  {  
   text:'放大',  
   callback:function(){map.zoomIn()}  
  },  
  {  
   text:'缩小',  
   callback:function(){map.zoomOut()}  
  },  
  {  
   text:'放置到最大级',  
   callback:function(){map.setZoom(18)}  
  },  
  {  
   text:'查看全国',  
   callback:function(){map.setZoom(4)}  
  },  
  {  
   text:'在此添加标注',  
   callback:function(p){  
    var marker = new BMap.Marker(p), px = map.pointToPixel(p);  
    map.addOverlay(marker);  
   }  
  }  
 ];  
 for(var i=0; i < txtMenuItem.length; i++){  
  
  contextMenu.addItem(new BMap.MenuItem(txtMenuItem[i].text,txtMenuItem[i].callback,100));  
  if(i==1 || i==3) {  
   contextMenu.addSeparator();  
  }  
 }    
 map.addContextMenu(contextMenu);
 var local = new BMap.LocalSearch(map, {  
  renderOptions:{map: map}  
});  
//local.search("广州");
}*/
/**
 * 通过传入不同的txtMenuItem对象值获取不同的右键菜单（某个页面个性化定制菜单）
 * @param {} polygon
 * @param {} txtMenuItem
 * @author chao.xj
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
/*GisCellDisplayLib.prototype.searchNcellByCellName = function(polygon){

	var cellname=polygon._data.getCell();
	
}*/
/**
 * 右键响应
 * @param {} polygon
 * @param {} txtMenuItem
 * @author chao.xj
 */
GisCellDisplayLib.prototype.responseRightClickForPolygon=function(polygon,txtMenuItem){

			var cell=polygon._data.getCell();
			// 主覆盖小区颜色
			var option_serverCell={
					'fillColor':'#FCD208'
			};
			//邻区颜色
			var option_ncell={
						'fillColor':'#4CB848'
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
//				   console.log(mes_obj.length);
				   if(mes_obj.length==0){
//				   alert("对不起,没有邻区数据!");
				   animateInAndOut("operInfo", 500, 500, 1000,
									"operTip", "对不起,没有邻区数据!");
				   }
				   
				  /* for(var key in mes_obj){
//				   	ncellarr.push(mes_obj[key].NCELL);
				   	gisCellDisplayLib.changeCellPolygonOptions(mes_obj[key].NCELL,option_ncell,true);
				   	console.log("cell,mes_obj[key].NCELL:"+cell+","+mes_obj[key].NCELL);
				   	gisCellDisplayLib.drawLineBetweenCells(cell,mes_obj[key].NCELL,{'color':'red',"weight":1});
				   }*/
				   for(var i=0;i<mes_obj.length;i++){
				   	gisCellDisplayLib.changeCellPolygonOptions(mes_obj[i].NCELL,option_ncell,true);
				   	gisCellDisplayLib.drawLineBetweenCells(cell,mes_obj[i].NCELL,{'color':'red',"weight":1});
				   }
				},
				error : function(err, status) {
					console.error(status);
				},
				complete : function() {
//					console.log("完成responseRightClickForPolygon:");
					$(".loading_cover").css("display", "none");
				}
			});
			 /*var contextMenu = new BMap.ContextMenu();//创建右键菜单实例 
			 if(txtMenuItem!=null){
				 for(var i=0; i < txtMenuItem.length; i++){  
				  contextMenu.addItem(new BMap.MenuItem(txtMenuItem[i].text,txtMenuItem[i].callback,100));  
				 } 
				 if (txtMenuItem.length>0) {
				 	polygon.addContextMenu(contextMenu);
				 }
			 }*/
}
/**
 * 清除覆盖物:移除地图所有图层
 * chao.xj
 */
GisCellDisplayLib.prototype.clearOverlays=function(){
	this.map.eachLayer(function (layer) {
                    if (!layer._url)//排除底图
                        map.removeLayer(layer);
                });
     /*if(this.map._layers){
                for (var i in map._layers) {
                    map.removeLayer(map._layers[i]);
                }
            }*/

}
/**
 * 获取缺省光标
 * @return {}
 * @author chao.xj
 */
GisCellDisplayLib.prototype.getDefaultCursor=function(){
	
}
/**
 * 删除图层
 * @param {} obj
 * @author chao.xj
 */
GisCellDisplayLib.prototype.removeOverlay=function(obj){
	this.map.removeLayer(obj);
}
/**
 * 通过图形对象获取cmk数据
 * @param {} shape
 * @return {}
 * @author chao.xj
 */
GisCellDisplayLib.prototype.getComposeMarkerByShape = function(shape) {
	return shape._data;
}
/**
 * 添加覆盖物
 * @param {} overlay
 * @author chao.xj
 */
GisCellDisplayLib.prototype.addOverlay = function(overlay) {
		this.map.addLayer(overlay);
}
/**
 * 通过cmk获取坐标点对象(其实是中心坐标点) 
 * @param {} cmk
 * @return {}
 * @author chao.xj
 */
GisCellDisplayLib.prototype.getLnglatObjByComposeMarker = function(cmk) {
	
	var lnglats=new Object();
	var cell=cmk.getCell();
	if (!cell) {
		return null;
	}
	var pl = this.cellToPolygon[cell];
	if (pl) {
		if (pl) {
			try {
				var pointArray = cmk.getPointArray();
				var point = null;
				if (pointArray && pointArray.length > 2) {
					var p1 = pointArray[1];
					var p2 = pointArray[2];
					/*point = new BMap.Point((p1.lng + p2.lng) / 2,
							(p1.lat + p2.lat) / 2);*/
					lnglats['lng']=(p1.lng + p2.lng) / 2;
					lnglats['lat']=(p1.lat + p2.lat) / 2;
				}
				return lnglats;
			} catch (err) {
				return null;
			}
		}
	}
}
GisCellDisplayLib.prototype.getMap = function() {
	return this.map;
}
/**
 * 通过图形获取原点坐标
 * @param {} pl
 * @return {}
 * @author chao.xj
 */
GisCellDisplayLib.prototype.getOriginPointByShape = function(pl) {
		//var ge=this.ge;
		var cmk = pl._data;
		var point = L.latLng(new Number(polygon._data.getLat()),new Number(
			polygon._data.getLng()));
		return point;
	}
/**
 * 通过CMK获取图形对象
 * @param {} cmk
 * @return {}
 * @author chao.xj
 */
GisCellDisplayLib.prototype.getShapeObjByComposeMarker = function(cmk) {
	
	return cmk.getPolygon();
}
/**
 * 创建标注
 * @param {} lng
 * @param {} lat
 * @param {} markername
 * chao.xj
 */
GisCellDisplayLib.prototype.createMarker = function(lng,lat,markername) {
	//var ge=this.ge;
	L.marker([lat, lng],{title:markername,alt:markername}).addTo(this.map).bindPopup(markername).openPopup();
}
/**
 * 获取偏移量
 * @param {} m
 * @param {} n
 * @return {}
 * chao.xj
 * 
 */
GisCellDisplayLib.prototype.getOffsetSize = function(m,n) {
	
}
/**
 * 设置缺省光标
 * @param {} m
 * @param {} n
 * chao.xj
 */
GisCellDisplayLib.prototype.setDefaultCursor = function(defaultCursor) {
	
}
/**
 * 创建点坐标
 * @param {} lng
 * @param {} lat
 * @return {}
 * chao.xj
 */
GisCellDisplayLib.prototype.createPoint = function(lng,lat) {
	return L.latLng(lat, lng);
}
/**
 * @author chao.xj
 * @date 2014-3-24 11:58
 * @param polygon
 * @description 根据地图图元返回对应的小区label
 * 
 * 
 */
GisCellDisplayLib.prototype.getPolygonCell = function(polygon){
	return polygon._data.getCell();
} 
/**
 * @author chao.xj
 * @date 
 * @param newOptions
 * @description 为保持与gelib的方法一直
 */
GisCellDisplayLib.prototype.initOptions = function (newOptions){

}
/**
 * @author chao.xj
 * @date 
 * @description 为保持与gelib的方法一直
 */
GisCellDisplayLib.prototype.releaseOptions = function (){
	
}
/**
 * 类库适配器
 * @param {} polygon
 * @return {}
 */
function adapterPolygon(polygon){
	//var label = polygon._data.getCell();
	var plobj=new Object();
	plobj=polygon;
	return plobj;
} 
/*AdapterPolygon.prototype.getCell = function() {
	return this._cell;
}*/