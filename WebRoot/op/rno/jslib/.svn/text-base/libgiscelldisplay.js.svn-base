/**
 * 用于在地图上控制giscell的显示类
 * 
 * @param map
 * @param minZoom
 * @param randomShowCnt
 * @param colors
 * @param responseFunction
 * @returns
 */

function GisCellDisplayLib(map, minZoom, randomShowCnt, colors,
		responseFunction, configOption,contextMenu,preZoom) {

	this.map = map;
	this.minZoom = minZoom ? minZoom : 16;
	this.randomShowCnt = randomShowCnt ? randomShowCnt : 50;
	this.singleColor = colors ? (colors['singleColor'] ? colors['singleColor']
			: "#0B4C5F") : '#0B4C5F';
	this.multiColor = colors ? (colors['multiColor'] ? colors['multiColor']
			: "#FF00FF") : '#FF00FF';

	var defaultClickFunction = function() {
	};
	this.clickFunction = responseFunction ? (responseFunction['clickFunction'] ? 
			responseFunction['clickFunction']
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
				//加入判断是否是重叠小区
				var cmk = polygon._data;
				if (cmk.getCount() > 1) {
						var html = "";
						if (!cmk) {
							return "";
						}
						var cellArray = cmk.getCellArray();
						var cell;
						for ( var i = 0; i < cellArray.length; i++) {
							cell = cellArray[i];
							html += "<a onclick='("+me.responseRightClickForPolygon+")(this,\""+cell.cell+"\")' "
								 + " target='_blank'>"+ (cell.chineseName ? cell.chineseName : cell.cell)
								 + "</a><br/><br/>";
						}
						html = "<h4 style='margin:0 0 5px 0;padding:0.2em 0'>重叠小区（"
								+ cmk.getCount() + "个）</h4>" + html;
						me.showInfoWindow(html, me.getOriginPointByShape(polygon));
				} else {
					me.responseRightClickForPolygon(polygon);
				}
			}
		}
	];
	this.contextMenu=contextMenu?contextMenu: defaultcontextMenu;
	if(preZoom==null || preZoom == undefined) {
		this.preZoom = minZoom;
	} else {
		this.preZoom = preZoom;
	}
	this.DiffAzimuth = 1;
	this.DiffDistance = 5;
	//2015-8-5 cc加入 允许设置相似度。
	if (configOption) {
		if (configOption['DiffAzimuth'] != null
				&& configOption['DiffAzimuth'] != undefined) {
			this.DiffAzimuth = configOption['DiffAzimuth'];
		}
		if(configOption['DiffDistance'] != null
				&& configOption['DiffDistance'] != undefined){
			this.DiffDistance=configOption['DiffDistance'];
		}
	}
	this.firstTime = true;
	this.lastOperTime = 0;

	this.composeMarkers = new Array();
	this.allPolygons = new Array();
	this.visiblePolygons = new Array();
	this.specialPolygons = new Array();// 特殊渲染的外观
	this.cellToPolygon = new Object();// 小区到polygon的映射
	this.extraMapOverlays = new Array();// 额外的覆盖物。与specialPolygons的区别是：specialPolygons指的还是小区，而这个extraMapOverlays是额外生成的，如用于标识小区的label覆盖物

	this.sameLnglatPolyArray = new Object;// 相同起点经纬度的poly数组，key为：lng+"_"+lat,value为polygon数组
	
	this.groundOverlay = new Object();
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
		map.removeOverlay(extraMapOverlays[i]);
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
 */
GisCellDisplayLib.prototype.clearOnlyExtraOverlay = function() {
  var map = this.map;
	var extraMapOverlays = this.extraMapOverlays;
	for ( var i = 0; i < extraMapOverlays.length; i++) {
		map.removeOverlay(extraMapOverlays[i]);
	}
	extraMapOverlays.splice(0, extraMapOverlays.length);
}

GisCellDisplayLib.prototype.showGisCell = function(data) {
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
			//--2014-9-22 pjm add 去重复--//
			if(this.existCell(gisCell['cell'])){
				continue;
			}
			//--2014-5-30 gmh add-- exclude cells without lng,lat//
			if(!gisCell || gisCell['allLngLats']==''){
				continue;
			}
			
			j = 0;
			for (j = 0; j < composeMarkers.length; j++) {
				cmk = composeMarkers[j];
				if (cmk.similiar(gisCell, this.DiffAzimuth, this.DiffDistance)) {
					cmk.addCell(gisCell);
					cellToPolygon[cmk.getCell()] = allPolygons[j];// 小区到polygon
					if (cmk.getCount() === 2) {
						// 重新渲染
						polygon = allPolygons[j];
						// console.log("旧的polygon" + polygon);
						if (polygon) {
							polygon.setFillColor(multiColor);
						}
					}
					break;
				}
			}
			// console.log("j=" + j);
			if (j >= composeMarkers.length) {
				// console.log("准备添加进单独的marker");
				var onecmk = new ComposeMarker(gisCell);
				if (onecmk && onecmk.getCount()>0) {
					composeMarkers.push(onecmk);// 不与任何点重复，加入
				} else {
					// console.log(gisCell.cell+" 未能正确生成ComposeMarker！");
					onecmk=null;
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
			
			//---2014-11-20 pjm 加入 
			//cmk对象包含的所有cell都映射到这个polygon中
			var cellArray = cmk.getCellArray();
			var cell;
			for(var i=0; i<cellArray.length; i++) {
				cell = cellArray[i];
				cellToPolygon[cell.cell] = polygon;
			}
			//cellToPolygon[cmk.getCell()] = polygon;// cell to polygon的hash

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
							polygon.setFillColor(multiColor);
//							polygon.setStyle({fillColor:multiColor});
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
// 判断多边形是否要显示
GisCellDisplayLib.prototype.shouldDisplay = function(polygon) {
	// console.log("in GisCellDisplayLib.prototype.shouldDisplay");
	// 如果不在可见区域内，肯定不显示
	var visib = false;
	var map = this.map;
	if (!map.getBounds().containsPoint(
			new BMap.Point(polygon._data.getLng(), polygon._data.getLat()))) {
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
 */
GisCellDisplayLib.prototype.createPolygonFromComposeMark = function(cmk) {
	try {
		if (!cmk) {
			console.log("空的参数");
			return null;
		}
		var me = this;
		var pa = cmk.getPointArray();
		var polygon = new BMap.Polygon(pa, cmk.getPolygonOptions(
				me.singleColor, me.multiColor));
		
//		var p4=new BMap.Point((pa[1].lng+pa[2].lng)/2,(pa[1].lat+pa[2].lat)/2);
//		polygon=new BMap.Polyline([pa[0],p4],{strokeWeight:1});
		
		
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
				edgePosition = new BMap.Point((p1.lng + p2.lng) / 2,
						(p1.lat + p2.lat) / 2);
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
			}
		}
		// /

		polygon.addEventListener('click', function(event) {
			//加入判断是否是重叠小区
			var cmk = this._data;
			if (cmk.getCount() > 1) {
					var html = "";
					if (!cmk) {
						return "";
					}
					var cellArray = cmk.getCellArray();
					var cell;
					for ( var i = 0; i < cellArray.length; i++) {
						cell = cellArray[i];
						
						if(cell.lcid){
						
						//console.log(cell.lcid+"-----------"+me+"--------------"+me.cellToPolygon);
						//console.log("lib polygon==========>>>>>>"+cell.lcid+"---"+(me.cellToPolygon[cell.lcid]));
						/*html += "<a onclick='("+me.clickFunction+")(this,\""+cell.cell+"\")' " +
									" target='_blank'>"+ (cell.chineseName ? cell.chineseName : cell.cell)
								+ "</a><br/><br/>";*/
						html += "<a onclick='("+me.clickFunction+")("+me.cellToPolygon[cell.lcid]+",\""+cell.lcid+"\")' " +
						" target='_blank'>"+ (cell.chineseName ? cell.chineseName : cell.lcid)
					+ "</a><br/><br/>";
						}else{
						html += "<a onclick='("+me.clickFunction+")(this,\""+cell.cell+"\")' " +
						" href=\"javascript:void(0)\">"+ (cell.chineseName ? cell.chineseName : cell.cell)
					+ "</a><br/><br/>";
						}
						/*html += "<a onclick='("+me.clickFunction+")("+cell.cell!="undefined"?me.cellToPolygon[cell.cell]:me.cellToPolygon[cell.lcid]+",\""+cell.cell!="undefined"?cell.cell:cell.lcid+"\")' " +
						" target='_blank'>"+ (cell.chineseName ? cell.chineseName : cell.cell!="undefined"?cell.cell:cell.lcid)
					+ "</a><br/><br/>";*/
					}
					html = "<h4 style='margin:0 0 5px 0;padding:0.2em 0'>重叠小区（"
							+ cmk.getCount() + "个）</h4>" + html;
					me.showInfoWindow(html, me.getOriginPointByShape(polygon));
			} else {
				me.clickFunction(this);
			}
			
			//me.clickFunction(this, event);
		});
		polygon.addEventListener('mouseover', function(event) {
			me.mouseoverFunction(this, event);
		});
		polygon.addEventListener('mouseout', function(event) {
			me.mouseoutFunction(this, event);
		});
//		console.log("contextMenu?contextMenu:defaultcontextMenu:"+me.contextMenu);
		me.rightClickMenuItemForPolygon(polygon,typeof me.contextMenu=="undefined"?null:me.contextMenu);
//		me.rightClickMenuItemForPolygon(polygon,typeof txtMenuItem=="undefined"?null:txtMenuItem);
		
		polygon.addEventListener('rightclick', function(event) {
			//me.rightClickMenuItemForPolygon(polygon,typeof txtMenuItem=="undefined"?null:txtMenuItem);//polygon对象创建右键菜单
//			console.log("me.contextMenu:"+me.contextMenu);
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

// 显示一个pl
GisCellDisplayLib.prototype.showOnePolygon = function(pl) {
	var map = this.map;
	if (pl) {
		try {
			pl._isShow = true;
			if (this.showCellLabel==true && pl._label) {
				map.addOverlay(pl._label);
			}

		} catch (err) {

		}
		try {
			map.addOverlay(pl);
		} catch (err) {

		}
	}
}

GisCellDisplayLib.prototype.hideOnePolygon = function(pl) {
  var map = this.map;
	if (pl) {
		try {
			pl._isShow = false;
			if (pl._label) {
				map.removeOverlay(pl._label);
			}
		} catch (err) {

		}
		try {
			map.removeOverlay(pl);
		} catch (err) {

		}
	}
}

// 处理缩放事件
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
				if (!bounds.containsPoint(new BMap.Point(pl._data.getLng(),
						pl._data.getLat()))) {
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
				if (bounds.containsPoint(new BMap.Point(pl._data.getLng(),
						pl._data.getLat()))
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
				if (bounds.containsPoint(new BMap.Point(pl._data.getLng(),
						pl._data.getLat()))
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
				if (bounds.containsPoint(new BMap.Point(pl._data.getLng(),
						pl._data.getLat()))
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

// 处理移动事件
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
		if (
				!bounds.containsPoint(new BMap.Point(pl._data.getLng(), pl._data.getLat()))
				//!(bounds['ne']['lng']>=pl._data.getLng()&& bounds['ne']['lat']>=pl._data.getLat()
				//&&bounds['sw']['lng']<=pl._data.getLng() && bounds['sw']['lat']<=pl._data.getLat())
		) {
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
 */
GisCellDisplayLib.prototype.changeCellPolygonOptions = function(cell, option,
		puttospec) {
	var pl = this.cellToPolygon[cell];
	if (!pl || !option) {
		return false;
	}

	if (option['fillColor']) {
		pl.setFillColor(option['fillColor']);
	}
	if (option['strokeColor']) {
		pl.setStrokeColor(option['strokeColor']);
	}
	if (option['fillOpacity']) {
		pl.setFillOpacity(option['fillOpacity']);
	}
	if (option['strokeWeight']) {
		pl.setStrokeWeight(option['strokeWeight']);
	}

	if (puttospec === true) {
		// 添加到特殊队
		this.specialPolygons.push(pl);
	}

	return true;
}

/**
 * 将全部图元恢复默认外观
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

		pl.setFillColor(option['fillColor']);
		pl.setStrokeColor(option['strokeColor']);
		pl.setFillOpacity(option['fillOpacity']);
		pl.setStrokeWeight(option['strokeWeight']);

	}

	// 特殊、额外图元
	this.specialPolygons.splice(0, this.specialPolygons.length);
}

/**
 * 将特殊外观的图元恢复默认外观
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

		pl.setFillColor(option['fillColor']);
		pl.setStrokeColor(option['strokeColor']);
		pl.setFillOpacity(option['fillOpacity']);
		pl.setStrokeWeight(option['strokeWeight']);

	}

	// 特殊、额外图元
	this.specialPolygons.splice(0, this.specialPolygons.length);
}

//2015-7-29 cc 加入  调整地图级别
GisCellDisplayLib.prototype.centerAndZoom = function(point, zoom) {
	this.map.centerAndZoom(point,zoom);
}
/* 使用太繁琐，不如point方便。
GisCellDisplayLib.prototype.centerAndZoom = function(lng,lat, zoom) {
	this.map.centerAndZoom(new BMap.Point(lng, lat),zoom);
}*/

GisCellDisplayLib.prototype.panTo = function(lng, lat) {
	this.map.panTo(new BMap.Point(lng, lat));
}

GisCellDisplayLib.prototype.panToCell = function(cell) {
	// console.log("panToCell cell="+cell);
	if (!cell) {
		return false;
	}

	// 找到cell
	var pl = this.cellToPolygon[cell];
	if (pl) {
		try {
			var point = new BMap.Point(pl._data.getLng(), pl._data.getLat());
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
GisCellDisplayLib.prototype.panToCell = function(cell,zoom) {
	if (!cell) {
		return false;
	}

	// 找到cell
	var pl = this.cellToPolygon[cell];
	if (pl) {
		try {
			var point = new BMap.Point(pl._data.getLng(), pl._data.getLat());
			if(zoom){
				this.centerAndZoom(point,zoom);
				//pl.click();
				//pl.trigger("click");
				//BMapLib.EventWrapper.trigger(pl,'click');

				//加入判断是否是重叠小区
				var cmk = pl._data;
				if (cmk.getCount() > 1) {
						var html = "";
						if (!cmk) {
							return "";
						}
						var cellArray = cmk.getCellArray();
						var cell;
						html+="<div id=\"celltimelist\" >";
						for ( var i = 0; i < cellArray.length; i++) {
							cell = cellArray[i];						
							cellId=cell.lcid?cell.lcid:cell.cell;
							if(cell.beginTimes.length<=1){
							html += "<a onclick='("+this.clickFunction+")(this,\""+cellId+"\")' " +
							//html += "<a onclick=\"javascript:gisCellDisplayLib.clickFunction(gisCellDisplayLib.cellToPolygon[cellId],"+cellId+")\""+
							" href=\"javascript:void(0)\">"+ (cell.chineseName ? cell.chineseName : cellId)	+ "</a><br/><br/>";
							}else{
								html += "<a onclick='("+toggleCellBegtimeList+")()' style=\"color:"+(cell.thFlag?"red":"")+";\" href=\"javascript:void(0)\">"+ (cell.chineseName ? cell.chineseName : cellId)+ "</a><ul>";
								//html += "<a onclick=\""+$(this).find("ul").toggle()+"\" style=\"color:"+(cell.thFlag?"red":"")+";\" href=\"javascript:void(0)\">"+ (cell.chineseName ? cell.chineseName : cellId)+ "</a><ul>";
								//html += "<a onclick=\"javascript:toggleCellBegtimeList()\" style=\"color:"+(cell.thFlag?"red":"")+";\" href=\"javascript:void(0)\">"+ (cell.chineseName ? cell.chineseName : cellId)+ "</a><ul>";
								for(var bt=0;bt<cell.beginTimes.length;bt++){
								html+="<li><a onclick='("+this.clickFunction+")(this,\""+cellId+"\",\""+cell.beginTimes[bt]+"\")' style=\"color:"+(cell.flags[bt]?"red":"")+";\"" +
								" href=\"javascript:void(0)\">"+ (cell.beginTimes[bt])+ "</a></li>";
								}
								html+="</ul><br/><br/>";
							}
						}
						html+="</div>";
						html = "<h4 style='margin:0 0 5px 0;padding:0.2em 0'>重叠小区（"
								+ cmk.getCount() + "个）</h4>" + html;
						this.showInfoWindow(html, this.getOriginPointByShape(pl));
				} else {
					var html = "";
					if (!cmk) {
						return "";
					}
					var cell=cmk.getCellArray()[0];
					if(!cell) {
						return "";
					}
					if(cell.beginTimes.length>1){
						for(var bt=0;bt<cell.beginTimes.length;bt++){
							html+="<a onclick='("+this.clickFunction+")(this,\""+cell.cell+"\",\""+cell.beginTimes[bt]+"\")' style=\"color:"+(cell.flags[bt]?"red":"")+";\"" +
							" href=\"javascript:void(0)\">"+ (cell.beginTimes[bt])+ "</a><br/><br/>";
							}
						html = "<h4 style='margin:0 0 5px 0;padding:0.2em 0'>"+(cell.chineseName ? cell.chineseName : cellId)+"<br/>任务次数（"
							+ cell.beginTimes.length + "次）</h4>" + html;
						this.showInfoWindow(html, this.getOriginPointByShape(pl));
					}else{
						this.clickFunction(pl,cell.cell,cell.beginTimes[0]);
					};
				}
				
				//me.clickFunction(this, event);
			
			}else{
				this.map.panTo(point);
			}
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
 * 
 * @param cell
 * @returns {Boolean}
 * 
 * 2013-11-02 gmh
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
 * 
 * @param cell
 * @returns BMap.Point或null
 * 
 * 2013-11-02 gmh
 */
GisCellDisplayLib.prototype.getCellPoint = function(cell) {
	if (!cell) {
		return null;
	}
	var pl = this.cellToPolygon[cell];
	if (pl) {
		if (pl) {
			try {
				var point = new BMap.Point(pl._data.getLng(), pl._data.getLat());
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
					point = new BMap.Point((p1.lng + p2.lng) / 2,
							(p1.lat + p2.lat) / 2);
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
		var pline = new BMap.Polyline([ point1, point2 ], option);
		this.map.addOverlay(pline);
		this.extraMapOverlays.push(pline);
	} catch (err) {

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
 */
GisCellDisplayLib.prototype.initMap = function(contId, lng, lat, mapFuncs) {
	var me = this;
	me.map = new BMap.Map(contId);
	me.map.enableScrollWheelZoom();//启动鼠标滚轮缩放地图
	me.map.enableKeyboard();//启动键盘操作地图
	me.map.enableContinuousZoom();    // 开启连续缩放效果
	me.map.enableInertialDragging();	  // 开启惯性拖拽效果
	//this.rightClickMenuItemForMap(me.map);//增加右键功能
	// 初始化
	var currentAreaCenter = new BMap.Point(113.361397, 23.12489);
	if (lng && lng != 0 && lat && lat != 0) {
		// console.log("lng，lat有效。")
		currentAreaCenter = new BMap.Point(lng, lat);
	}
	me.map.centerAndZoom(currentAreaCenter, me.preZoom);
	// 控件
	me.map.addControl(new BMap.NavigationControl());
	me.map.addControl(new BMap.MapTypeControl());

	// ----------事件开始-----------------------//
	// 开始移动
	me.map.addEventListener("movestart", function(e) {
		var lastOperTime = new Date().getTime();
		me.lastOperTime = lastOperTime;

		if (mapFuncs && typeof (mapFuncs['movestart']) === "function") {
			mapFuncs['movestart'](e, lastOperTime);
		}

	});
	// 开始缩放
	me.map.addEventListener("zoomstart", function(e) {
		var lastOperTime = new Date().getTime();
		me.lastOperTime = lastOperTime;

		if (mapFuncs && typeof (mapFuncs['zoomstart']) === "function") {
			mapFuncs['zoomstart'](e, lastOperTime);
		}

	});
	// 缩放
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

	// 地图所有图块加载完毕触发
	me.map.addEventListener("tilesloaded", function(e) {
		// console.log("tilesloaded。。。");
		if (me.firstTime) {
			me.firstTime = false;
			if (mapFuncs && typeof (mapFuncs['tilesloaded']) === "function") {
				mapFuncs['tilesloaded'](e);
			}
		}
	});

	return this.map;
	// ----------事件结束-----------------------//

}

// 获取显示的标题内容
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
 */
GisCellDisplayLib.prototype.createInfoWindow = function(content, opts, shownow,
		point) {
	if (!this.infoWindow) {
		if (opts) {
			this.infoWindow = new BMap.InfoWindow(content, opts);
		} else {
			this.infoWindow = new BMap.InfoWindow(content);
		}
		if (shownow === true && point) {
			this.map.openInfoWindow(this.infoWindow, point);
		}
	}
}

GisCellDisplayLib.prototype.showInfoWindow = function(content, point) {
	if (!this.infoWindow) {
		this.createInfoWindow(content, null, true, point);
	} else {
		this.infoWindow.setContent(content);
		this.map.openInfoWindow(this.infoWindow, point);
	}
}

GisCellDisplayLib.prototype.closeInfoWindow = function() {
	if (this.infoWindow && this.infoWindow === true) {
		this.map.closeWindow(this.infoWindow);
	}
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
 */
GisCellDisplayLib.prototype.rightClickMenuItemForPolygon = function(polygon,txtMenuItem){
//	 console.log("进入rightClickMenuItemForPolygon");
	 var polygon=polygon;
	 var contextMenu = new BMap.ContextMenu();//创建右键菜单实例 
//	 console.log(txtMenuItem);
	 if(txtMenuItem!=null){
		 for(var i=0; i < txtMenuItem.length; i++){  
		  contextMenu.addItem(new BMap.MenuItem(txtMenuItem[i].text,txtMenuItem[i].callback,100));
		  contextMenu.addSeparator();  //添加右键菜单的分割线    
		  /*if(i==1 || i==3) {  
		   contextMenu.addSeparator();  
		  }  */
//		  console.log("进入rightClickMenuItemForPolygon for循环");
		 } 
		 if (txtMenuItem.length>0) {
		 	
		 	polygon.addContextMenu(contextMenu);
//		 	console.log("进入rightClickMenuItemForPolygon 菜单添加项");
		 }
	 }
	 
}
/*GisCellDisplayLib.prototype.searchNcellByCellName = function(polygon){

	var cellname=polygon._data.getCell();
	
}*/
GisCellDisplayLib.prototype.responseRightClickForPolygon=function(polygon,cell){
	if(!cell) {
		var cmk = gisCellDisplayLib.getComposeMarkerByShape(polygon);
		cell = cmk.getCell();
	}
	gisCellDisplayLib.getNcellforNcellAnalysisOfBusyCellByCell(cell);
}

GisCellDisplayLib.prototype.getNcellforNcellAnalysisOfBusyCellByCell=function(cell) {
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
				   
				   for(var key in mes_obj){
//				   	ncellarr.push(mes_obj[key].NCELL);
				   	gisCellDisplayLib.changeCellPolygonOptions(mes_obj[key].NCELL,option_ncell,true);
				   	gisCellDisplayLib.drawLineBetweenCells(cell,mes_obj[key].NCELL,{'strokeColor':'red',"strokeWeight":3});
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

GisCellDisplayLib.prototype.clearOverlays=function(){
	this.map.clearOverlays();
}
GisCellDisplayLib.prototype.getDefaultCursor=function(){
	return this.map.getDefaultCursor();
}
GisCellDisplayLib.prototype.removeOverlay=function(obj){
	this.map.removeOverlay(obj);
}
/**
 * 通过图形对象获取cmk数据
 * @param {} shape
 * @return {}
 */
GisCellDisplayLib.prototype.getComposeMarkerByShape = function(shape) {
	return shape._data;
}
/**
 * 添加覆盖物
 * @param {} overlay
 */
GisCellDisplayLib.prototype.addOverlay = function(overlay) {
		this.map.addOverlay(overlay);
}
/**
 * 通过cmk获取坐标点对象(其实是中心坐标点) 
 * @param {} cmk
 * @return {}
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
 */
GisCellDisplayLib.prototype.getOriginPointByShape = function(pl) {
		//var ge=this.ge;
		var cmk = pl._data;
		var point = new BMap.Point(new Number(
			pl._data.getLng()), new Number(pl._data.getLat()));
		return point;
	}
/**
 * 通过CMK获取图形对象
 * @param {} cmk
 * @return {}
 */
GisCellDisplayLib.prototype.getShapeObjByComposeMarker = function(cmk) {
	
	return cmk.getPolygon();
}
/**
 * 创建标注
 * @param {} lng
 * @param {} lat
 * @param {} markername
 */
GisCellDisplayLib.prototype.createMarker = function(lng,lat,markername) {
	//var ge=this.ge;
	var marker1 = new BMap.Marker(new BMap.Point(lng,lat));  // 创建标注
							map.addOverlay(marker1);  
	var infoWindow1=new BMap.InfoWindow(markername);
	//console.log(mes_obj[i].description);
	marker1.addEventListener("click", function(){this.openInfoWindow(infoWindow1);});
}
/**
 * 获取偏移量
 * @param {} m
 * @param {} n
 * @return {}
 */
GisCellDisplayLib.prototype.getOffsetSize = function(m,n) {
	var offsetsize=new BMap.Size(m, n);
	return offsetsize;
}
/**
 * 设置缺省光标
 * @param {} m
 * @param {} n
 */
GisCellDisplayLib.prototype.setDefaultCursor = function(defaultCursor) {
	this.map.setDefaultCursor(defaultCursor);
}
/**
 * 创建点坐标
 * @param {} lng
 * @param {} lat
 * @return {}
 */
GisCellDisplayLib.prototype.createPoint = function(lng,lat) {
	return new BMap.Point(lng,lat);
}
/**
 * @author Liang YJ
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
 * @author Liang YJ
 * @date 2014-3-20 10:45
 * @param newOptions
 * @description 为保持与gelib的方法一直
 */
GisCellDisplayLib.prototype.initOptions = function (newOptions){

}
/**
 * @author Liang YJ
 * @date 2014-3-20 11:11
 * @description 为保持与gelib的方法一直
 */
GisCellDisplayLib.prototype.releaseOptions = function (){
	
}

GisCellDisplayLib.prototype.createGroudOverLay = function(){
	var map = this.map;
	// 启用滚轮放大缩小
	map.enableScrollWheelZoom();
	
	//初始化 西南角和东北角
	var SW = new BMap.Point(116.29579,39.837146);
	var NE = new BMap.Point(116.475451,39.9764);
	var groundOverlayOptions = {
	    opacity: 1,
	    displayOnMinLevel: 10,
	    displayOnMaxLevel: 14
	}

	// 初始化GroundOverlay
	this.groundOverlay = new BMap.GroundOverlay(new BMap.Bounds(SW, NE),groundOverlayOptions);

	map.addOverlay(this.groundOverlay);
}

GisCellDisplayLib.prototype.setGroudOverLayOpt = function (swLng,swLat,neLng,neLat,imgPath){
	var map = this.map;
	var groundOverlay = this.groundOverlay;
	//获取中心点
	var cLng = (Number(swLng)+Number(neLng))/2;
	var cLat = (Number(swLat)+Number(neLat))/2;
	//移动
	map.panTo(new BMap.Point(cLng, cLat));
	//设置图层大小
    var sw = new BMap.Point(swLng,swLat);
  	var ne = new BMap.Point(neLng,neLat);
 	groundOverlay.setBounds(new BMap.Bounds(sw, ne));
 	//设置图层背景图
 	groundOverlay.setImageURL(imgPath);
}


GisCellDisplayLib.prototype.hidePolygonsLabel = function() {
	//console.log("关闭小区名称...");
	var allPolygons = this.allPolygons;
	var map = this.map;
	var pl;
	
	//设置不显示小区名称
	this.showCellLabel = false;
	
	//移除当前加载出来的label,并设置所有polygon的Label显示状态为false
	for ( var i = 0; i < allPolygons.length; i++) {
		pl = allPolygons[i];	
		//console.log(pl._label);
		map.removeOverlay(pl._label);
	}
}

GisCellDisplayLib.prototype.showPolygonsLabel = function() {
	//console.log("开启小区名称...");
	var visiblePolygons = this.visiblePolygons;
	var map = this.map;
	var pl;
	
	//设置显示小区名称
	this.showCellLabel = true;
	
	//显示当前可查看的polygon的label,并设置所有polygon的Label显示状态为true
	for ( var i = 0; i < visiblePolygons.length; i++) {
		pl = visiblePolygons[i];
		//console.log(pl._label);
		if (pl && pl._label) {
			map.addOverlay(pl._label);
		}
	}
}
/**
 * 画出两点坐标的连线
 */
GisCellDisplayLib.prototype.drawLineBetweenPoints = function(cellLon, cellLat,ncellLon,ncellLat,
		option) {
	if (!cellLon || !ncellLon) {
		return;
	}

	try {
		var point1 = new BMap.Point(cellLon, cellLat);
		var point2 = new BMap.Point(ncellLon, ncellLat);
   
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
		var pline = new BMap.Polyline([ point1, point2 ], option);
		this.map.addOverlay(pline);
		this.extraMapOverlays.push(pline);
	} catch (err) {

	}
}
/**
 * 通过多个坐标点集合创建多边形
 */
GisCellDisplayLib.prototype.createPolygonFromLngLats = function(lnglats) {

		var points=new Array();
		var me = this;
		var pa = lnglats.split(";");
		for(var i=0;i<pa.length;i++){
			var lnglat=pa[i].split(",");
			lnglatPoint = new BMap.Point(lnglat[0] , lnglat[1]);
			points.push(lnglatPoint);
		}
		var polygon = new BMap.Polygon(points,{strokeColor:"#00FF00", strokeWeight:6, strokeOpacity:0.5});
		this.map.addOverlay(polygon);
}
/**
 * 创建文本标注
 */
GisCellDisplayLib.prototype.createLabel = function(lng,lat,mark,tip) {
	var point = new BMap.Point(lng,lat);
	var opts = {
	  position : point//,    // 指定文本标注所在的地理位置
//	  offset   : new BMap.Size(30, -30)    //设置文本偏移量
	}
	var label = new BMap.Label(mark, opts);  // 创建文本标注对象
		label.setStyle({
			 color : "#000000",
			 fontSize : "12px",
			 height : "20px",
			 lineHeight : "20px",
			 fontFamily:"微软雅黑",
			 border :"0", 
			 fontWeight :"bold"
		 });
		label.setTitle(tip);
	this.map.addOverlay(label);
}
/**
 * 把三角小区重绘为扇形
 * @param polygon
 * 2015-7-29 cc 
 */
GisCellDisplayLib.prototype.polygonToSector = function(polygon,angle,radius) {
	var pa=polygon.getPath();
	//方向角 
	var  azimuth = this.getComposeMarkerByShape(polygon).getAzimuth();
	var radiusSml=(Math.sqrt((pa[0].lng-pa[1].lng)*(pa[0].lng-pa[1].lng)+(pa[0].lat-pa[1].lat)*(pa[0].lat-pa[1].lat))+Math.sqrt((pa[0].lng-pa[2].lng)*(pa[0].lng-pa[2].lng)+(pa[0].lat-pa[2].lat)*(pa[0].lat-pa[2].lat)))/2;

	//默认圆心角 60
	var defAngle = angle?angle:60;
	//默认半径
	//var defRadius = radius?radius:0.0007;
	var defRadius = radius?radius:radiusSml;

/*	var sDegree=Math.atan2(pa[1].lng-pa[0].lng, pa[1].lat-pa[0].lat)/Math.PI*180;
	var eDegree=Math.atan2(pa[2].lng-pa[0].lng, pa[2].lat-pa[0].lat)/Math.PI*180;
	sDegree=sDegree<0?sDegree+360:sDegree;
	eDegree=eDegree<0?eDegree+360:eDegree;	
	sDegree = (eDegree - sDegree<0)?sDegree-360:sDegree;
	var aDegree = (eDegree + sDegree)/2;
	var nsDegree = aDegree - 30 ;
	var neDegree = aDegree +30 ;*/
	//设置初始方位
	var nsDegree = azimuth - defAngle/2 ;
	var neDegree = azimuth +defAngle/2 ;
	//console.log(nsDegree+"__"+neDegree+"__"+(eDegree-sDegree)+"__"+aDegree+"__"+azimuth);
	//var radius=(this.map.getDistance(pa[0],pa[1])+this.map.getDistance(pa[0],pa[2]))/2;
	var points = [];
	//var cDegree=eDegree - sDegree;
	//设置步进
	var step = defAngle/ 30;
	points.push(pa[0]);
    for ( var i = nsDegree; i <=neDegree; i += step) {
	    //var latConv = map.getDistance(point, new BMap.Point(point.lng + 0.5, point.lat)) * 2;
	    //var lngConv = map.getDistance(point, new BMap.Point(point.lng, point.lat + 0.5)) * 2;
    	//var avgConv=(latConv+lngConv)/2;
	    //var lat = dist * Math.cos(bearing * Math.PI / 180) / avgConv;
	    //var lng = dist * Math.sin(bearing * Math.PI / 180) / avgConv;
	    var lat = defRadius * Math.cos(i * Math.PI / 180);
	    var lng = defRadius * Math.sin(i * Math.PI / 180);
        points.push(new BMap.Point(pa[0].lng + lng, pa[0].lat + lat));
    }
    points.push(pa[0]);
    polygon.setPath(points);
    return polygon;
};

GisCellDisplayLib.prototype.showLteCellBySector = function(data) {
	// console.log("in GisCellDisplayLib.prototype.showGisCell");
	var me = this;
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
			//--2014-9-22 pjm add 去重复--//
			if(this.existCell(gisCell['cell'])){
				continue;
			}
			//--2014-5-30 gmh add-- exclude cells without lng,lat//
			if(!gisCell || gisCell['allLngLats']==''){
				continue;
			}
			
			j = 0;
			for (j = 0; j < composeMarkers.length; j++) {
				cmk = composeMarkers[j];
				if (cmk.similiar(gisCell, this.DiffAzimuth, this.DiffDistance)) {
					cmk.addCell(gisCell);
					cellToPolygon[cmk.getCell()] = allPolygons[j];
					cellToPolygon[gisCell.cell] = allPolygons[j];// 小区到polygon
					if (cmk.getCount() === 2) {
						// 重新渲染
						polygon = allPolygons[j];
						// console.log("旧的polygon" + polygon);
						if (polygon) {
							polygon.setFillColor(multiColor);
						}
					}
					break;
				}
			}
			// console.log("j=" + j);
			if (j >= composeMarkers.length) {
				var onecmk = new ComposeMarker(gisCell);
				if (onecmk && onecmk.getCount()>0) {
					composeMarkers.push(onecmk);// 不与任何点重复，加入
					polygon = me.createPolygonFromComposeMark(onecmk);
					polygon = me.polygonToSector(polygon);
					allPolygons.push(polygon);
					polygon._isShow = false;
					cellToPolygon[onecmk.getCell()] = polygon;
					var visib = me.shouldDisplay(polygon);
					if (visib === true) {
						me.showOnePolygon(polygon);
						visiblePolygons.push(polygon);
					}
				}else {
					onecmk=null;
				}
			}
		}
	} catch (err) {
		console.error(err);
	}
}