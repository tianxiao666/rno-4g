/**
 * 展现小区相关的信息的类
 * 
 * @returns
 */

// ------------------复合点 开始------------------------------//
function ComposeMarker() {
	this._oriLng = 0;
	this._oriLat = 0;// 原始gps

	this._lng = 0;// 转换为百度以后的
	this._lat = 0;
	this._freqType = "";
	this._azimuth = 0;
	this._chineseName = "";
	this._cell = "";
	this._site = "";
	this._lac = 0;
	this._ci = 0;
	this._bcch = 0;
	this._tch = "";

	this._type = 'single';
	this._count = 0;
	this._pointArray = new Array();
	this._cellArray = new Array();

	this._polygon = null;
}

function ComposeMarker(gisCell,key) {

	this._oriLng = gisCell.lng ? gisCell.lng : 0;
	this._oriLat = gisCell.lat ? gisCell.lat : 0;

	this._freqType = gisCell.freqType ? gisCell.freqType : "";
	this._azimuth = gisCell.azimuth ? gisCell.azimuth : 0;
	this._chineseName = gisCell.chineseName ? gisCell.chineseName : "";
	//this._cell = gisCell.cell ? gisCell.cell : "";
	//key判断加载的是lte小区还是gis小区,加上“”改为字符串格式
	//console.log(gisCell[key]);
	this._cell = key ? (gisCell[key] ? gisCell[key]+"" : "") : (gisCell.cell ? gisCell.cell : "");

	this._site = gisCell.site ? gisCell.site : "";
	this._lac = gisCell.lac ? gisCell.lac : 0;
	this._ci = gisCell.ci ? gisCell.ci : 0;
	this._bcch = gisCell.bcch ? gisCell.bcch : 0;
	this._tch = gisCell.tch ? gisCell.tch : "";

	this._type = 'single';
	this._count = 1;
//	this._pointArray = new Array();//=>this._lnglatArray = new Array();
	this._lnglatArray = new Array();
	this._cellArray = new Array();
	this._polygon = null;

	// 显示用的经纬度          
	//var alnglats = gisCell.allLngLats;// 113.2,23.33;113.233,23.334;113.222,23.234
	var lnltlists = gisCell.allLngLats;// 113.2,23.33;113.233,23.334;113.222,23.234
	
	//var data=eval("("+lnltlists+")");
	//var alnglats=data.googleearth;
	var data;
	var alnglats;
	if(lnltlists.indexOf("googleearth") > 0) {
		data=eval("("+lnltlists+")");
		alnglats=data.googleearth;
	} else {
		data=lnltlists;
		alnglats=data;
	}

	this._allLngLats=alnglats;
	// console.log("allLnglats=" + alnglats);
	if (!alnglats) {
		// console.log(gisCell.cell+" 具有无效的alnglats！")
		return null;
	}
	var alls = alnglats.split(";");
	for ( var i = 0; i < alls.length; i++) {
		// console.log("all lng lats i ="+i);
		var one = alls[i].split(",");
//		var point = new BMap.Point(one[0], one[1]);
		var point = one[0]+","+one[1];
		this._lnglatArray.push(point);
		if (i == 0) {
			// console.log("准备存储转换后的经纬度。");
			// 转换后的在此处
			this._lng = one[0];
			this._lat = one[1];
			// console.log("存储转换后的经纬度后。");
		}
	}
	// console.log("push gisCell到marker内容的数组。")
	this._cellArray.push(gisCell);

	// if (this._site != "") {
	// console.log("构造函数 ： site ===== [" + this._site + "]");
	// }

}

ComposeMarker.prototype.getLng = function() {
	return this._lng;
}

ComposeMarker.prototype.getLat = function() {
	return this._lat;
}

ComposeMarker.prototype.getOriLng = function() {
	return this._oriLng;
}

ComposeMarker.prototype.getOriLat = function() {
	return this._oriLat;
}

ComposeMarker.prototype.getCell = function() {
	return this._cell;
}
// 获取关联的所有的小区名
ComposeMarker.prototype.getAllCells = function() {
	var content = "";
	for ( var i = 0; i < this._cellArray.length; i++) {
		content += getValidValue(this._cellArray[i]['cell']);
	}
	return content;
}

ComposeMarker.prototype.setPolygon = function(polygon) {
	this._polygon = polygon;
}

ComposeMarker.prototype.getPolygon = function() {
	return this._polygon;
}
ComposeMarker.prototype.getCount = function() {
	return this._count;
}

ComposeMarker.prototype.getPolygonOptions = function(singleColor, multiColor) {
	var options = new Object();
	options.strokeColor = "blue";
	if (this._count === 1) {
		options.fillColor = singleColor;
	} else {
		options.fillColor = multiColor;
	}
	options.strokeWeight = 1;
	options.fillOpacity = 0.5;

	// console.log(options);
	return options;
}

ComposeMarker.prototype.getPointArray = function() {
	return this._lnglatArray;
}

ComposeMarker.prototype.getCellArray = function() {
	return this._cellArray;
}

// 添加一个小区
ComposeMarker.prototype.addCell = function(gisCell,key) {
	if (!gisCell) {
		return;
	}
	var me = this;
	me._count++;
	me._cellArray.push(gisCell);
	if (me._count > 1) {
		me._type = "multi";
	} else {
		// 显示用的经纬度
		var alnglats = gisCell.allLngLats;// 113.2,23.33;113.233,23.334;113.222,23.234
		// console.log("allLnglats=" + alnglats);
		var alls = alnglats.split(";");
		for ( var i = 0; i < alls.length; i++) {
			var one = alls[i].split(",");
			//var point = new BMap.Point(one[0], one[1]);
			var point = one[0]+","+one[1];
			me._lnglatArray.push(point);
			if (i == 0) {
				// 转换后的在此处
				this._lng = new Number(one[0]);
				this._lat = new Number(one[1]);
			}
		}

		// 其他信息
		me._oriLng = gisCell.lng;
		me._oriLat = gisCell.lat;
		me._freqType = gisCell.freqType;
		me._azimuth = gisCell.azimuth;
		me._chineseName = gisCel.chineseName;
		//me._cell = gisCell.cell;
		//key判断加载的是lte小区还是gis小区
		me._cell = key ? (gisCell[key] ? gisCell[key] : "") : (gisCell.cell ? gisCell.cell : "");
		
		me._site = gisCell.site ? gisCell.site : "";
		me._lac = gisCell.lac ? gisCell.lac : 0;
		me._ci = gisCell.ci ? gisCell.ci : 0;
		me._bcch = gisCell.bcch ? gisCell.bcch : 0;
		me._tch = gisCell.tch ? gisCell.tch : "";

		// if (me._site != "") {
		// console.log("add cell site ===== " + me._site);
		// }
	}
}

ComposeMarker.prototype.getAzimuth = function() {
	return this._azimuth;
}

ComposeMarker.prototype.get = function(prop) {
	return this[prop];
}

// 判断是否满足相似度要求：经纬度距离+方向角
ComposeMarker.prototype.similiar = function(gisCell, DiffAzimuth, DiffDistance) {
	if (!gisCell) {
		return false;
	}
	var z = azimuthDiff(this._azimuth, gisCell.azimuth);
	if (this._freqType == gisCell.freqType) {
		if (z <= DiffAzimuth) {
			// /角度在允许范围内
			var d = distance(this._oriLng, this._oriLat, gisCell.lng,
					gisCell.lat);
			if (d < DiffDistance) {
				// 距离也在允许范围内
				return true;
			}
		}
	}
	return false;
}

// prop是不带"_"线的
ComposeMarker.prototype.hasSameCell = function(prop, value) {
	if (!prop || !value) {
		return false;
	}
	prop = $.trim(prop);
	value = $.trim(value);
	if ($.trim(this["_" + prop]) == $.trim(value)) {
		return true;
	}
	if (this._count > 1) {
		for ( var i = 0; i < this._cellArray.length; i++) {
			// console.log("$.trim(this._cellArray[i][prop])) ==
			// "+$.trim(this._cellArray[i][prop]));
			if (value == $.trim(this._cellArray[i][prop])) {
				return true;
			}
		}
	}
	return false;
}

// 模糊匹配
ComposeMarker.prototype.hasLikeCell = function(prop, value) {
	if (!prop || !value) {
		return false;
	}
	prop = $.trim(prop);
	value = $.trim(value);
	tarvalue = $.trim(this["_" + prop]);

	if (tarvalue != null && tarvalue != undefined
			&& (tarvalue + "").indexOf(value) >= 0) {
		return true;
	}
	if (this._count > 1) {
		for ( var i = 0; i < this._cellArray.length; i++) {
			tarvalue = this._cellArray[i][prop];
			try {
				if (tarvalue != null && tarvalue != undefined
						&& (tarvalue + "").indexOf(value) >= 0) {
					return true;
				}
			} catch (err) {
				console.error("err=" + err + ",i=" + i + ",tarvalue="
						+ tarvalue);
			}
		}
	}
	return false;
}

// 获取包含指定freq的giscell
ComposeMarker.prototype.getContainsFreqCell = function(freq) {
	if (!freq || $.trim(freq) == "") {
		return null;
	}
	if (isNaN(freq)) {
		return null;
	}
	var strbcch = "";
	var strtch = "";
	var gisc;
	for ( var i = 0; i < this._count; i++) {
		gisc = this._cellArray[i];
		if (gisc.bcch == freq) {
			strbcch += " " + gisc.cell + ":" + gisc.bcch;
		} else if (gisc.tch) {
			var tcha = gisc.tch.split(",");
			for ( var ti = 0; ti < tcha.length; ti++) {
				if (tcha[ti] == freq) {
					// console.log("find freq["+freq+"] in cell["+gisc.cell+"]
					// tch["+gisc.tch+"]")
					strtch += " " + gisc.cell + ":" + gisc.tch;
					break;
				}
			}

		}
	}

	if (strbcch != "" || strtch != "") {
		return new LabelContent(strbcch, strtch);
	}
	return null;
}

// 获取第一个小区的名称，优先考虑中文名
ComposeMarker.prototype.getFirstCellNameChineseFirst = function() {
	var name = null;
	var ca = this._cellArray;
	var i = 0;
	while (name == null && i < ca.length) {
		if (ca[i].chineseName) {
			return ca[i].chineseName;
		} else if (ca[i].cell) {
			return ca[i].cell;
		}
		i++;
	}

	return "未知名称";
}

// 获取所有小区的名称，优先考虑中文名
// 返回array
ComposeMarker.prototype.getAllCellNameChineseFirst = function() {
	var names = new Array();
	var ca = this._cellArray;
	var i = 0;
	if (ca) {
		while (i < ca.length) {
			if (ca[i].chineseName) {
				names.push(ca[i].chineseName);
			} else if (ca[i].cell) {
				names.push(ca[i].cell);
			}
			i++;
		}
	}
	return names;
}
/**
 * 获取符合频点条件的lable内容
 * @param {} pl
 * @param {} freq
 * @param {} Ge gelib对象
 * @return {}
 */
ComposeMarker.prototype.getFreqCellLabelContent=function(pl,freq,Ge){
	if (!freq || $.trim(freq) == "") {
		return null;
	}
	if (isNaN(freq)) {
		return null;
	}
	var color;
	var labelobj=new Object();
	var labelContent1="";
	var labelContent2="";
	var cmk=Ge.getComposeMarkerByShape(pl);
	var cellArray = cmk.getCellArray();
	var gisc;
	var freq1 = parseInt(freq)+1;
	var freq2 = parseInt(freq)-1;
	for ( var i = 0; i < cmk.getCount(); i++) {
		gisc = cellArray[i];
		if (gisc.bcch == freq) {
			labelContent1 =  gisc.cell + " bcch:" + gisc.bcch+",tch:" + gisc.tch;
			break;
		}
		if(gisc.bcch == freq1||gisc.bcch == freq2){
			labelContent2 = gisc.cell +" bcch:" + gisc.bcch+",tch:" + gisc.tch;
			break;
		}
		if (gisc.tch) {
			var tcha = gisc.tch.split(",");
			for ( var ti = 0; ti < tcha.length; ti++) {
				if (tcha[ti] == freq) {
					labelContent1 = gisc.cell + " bcch:" + gisc.bcch+",tch:" + gisc.tch;
					break;
				}
				if(gisc.bcch == freq1||gisc.bcch == freq2){
					labelContent2 = gisc.cell + " bcch:" + gisc.bcch+",tch:" + gisc.tch;
					break;
				}
			}
		}
	}
	var content="";
	if(labelContent1!=""){
		content = labelContent1;//red
		color=Ge.toGePolygonStyle({"fillColor":"#FF0000","fillOpacity":1});;
	}else if(labelContent2!=""){
		content = labelContent2;//yellow
		color=Ge.toGePolygonStyle({"fillColor":"#FFFF00","fillOpacity":1});;
	}
	labelobj['content']=content;
	labelobj['color']=color;
	return labelobj;
}
// ------------------复合点 结束------------------------------//

/**
 * 
 * @param cell
 *            小区英文名
 * @returns
 */
function CellToNcell(composeMarker) {
	if (!composeMarker) {
		return null;
	}
	this._creatTime = new Date().getTime();
	this._cell = composeMarker.getCell();
	this._polygon = composeMarker.getPolygon();
	this._ncells = new Array();
	this._ncellPolygons = new Array();
}

// function CellToNcell(cell) {
// if (!cell) {
// return null;
// }
// var size = composeMarkers.length;
// var cmk;
// for ( var i = 0; i < size; i++) {
// cmk = composeMarkers[i];
// if (cmk.hasSameCell("cell", cell)) {
// break;
// }
// cmk = null;
// }
// if (cmk) {
// this._creatTime = new Date().getTime();
// this._cell = cell;
// this._polygon = cmk.getPolygon();
// this._ncells = new Array();
// this._ncellPolygons = new Array();
// } else {
// return null;
// }
// }

/**
 * 
 * @param composeMarker
 *            当前小区所属的cmk
 * @param cmks
 *            全部cmk
 * @returns
 */
// function CellToNcell(composeMarker,cmks) {
// if (!composeMarker) {
// return null;
// }
// this._creatTime = new Date().getTime();
// this._cell = composeMarker.getCell();
// this._polygon = composeMarker.getPolygon();
// this._ncells = new Array();
// this._ncellPolygons = new Array();
// this._composeMarkers=cmks;
// }

/**
 * 
 */
function CellToNcell(cell, cmks) {
	if (!cell) {
		return null;
	}
	this._composeMarkers = cmks;
	var size = cmks.length;
	var cmk;
	for ( var i = 0; i < size; i++) {
		cmk = cmks[i];
		if (cmk.hasSameCell("cell", cell)) {
			break;
		}
		cmk = null;
	}
	if (cmk) {
		this._creatTime = new Date().getTime();
		this._cell = cell;
		this._polygon = cmk.getPolygon();
		this._ncells = new Array();
		this._ncellPolygons = new Array();
	} else {
		return null;
	}
}

CellToNcell.prototype.getPolygon = function() {
	return this._polygon;
}

CellToNcell.prototype.getCreateTime = function() {
	return this._creatTime;
}

CellToNcell.prototype.addNcell = function(ncell) {
	if (ncell) {
		// 遍历composemarker数组，找到ncell对应的composemarker
		// 开始遍历
		var composeMarkers = this._composeMarkers;
		var size = composeMarkers.length;
		var cmk;
		for ( var i = 0; i < size; i++) {
			cmk = composeMarkers[i];

			if (cmk.hasSameCell("cell", ncell)) {
				break;
			}
			cmk = null;
		}
		if (cmk) {
			this._ncells.push(ncell);
			this._ncellPolygons.push(cmk.getPolygon());
		}
	}
}

CellToNcell.prototype.getNcells = function() {
	return this._ncells;
}

CellToNcell.prototype.getNcellPolygons = function() {
	return this._ncellPolygons;
}

CellToNcell.prototype.isSame = function(anotherCell) {
	if (!anotherCell) {
		return false;
	}
	if (this._cell === anotherCell) {
		return true;
	}
	return false;
}

CellToNcell.prototype.getCell = function() {
	return this._cell;
}

// ////////////////

// ----标注类[在底边第三点坐标上显示]-----//
function IsTextLabel(lng, lat, data,opts,eventFunction,Ge) {
	var ge=Ge.ge;
	this.lng = lng;
	this.lat = lat;
	var point = ge.createPoint('');
	point.setLatitude(Number(lat));
	point.setLongitude(Number(lng));
	this.center = point;
	this.data = data;
	var textlabel=this.createTextLabel(lng,lat,data,Ge);
	return textlabel;
	//>>>>>>>>>>>>>>扩展
	/*this.content=opts.content;
	this.position=opts.position;
	this.offset=opts.offset;*/
}

IsTextLabel.prototype.createTextLabel=function(lng,lat,data,Ge) {
		var ge=Ge.ge;
		var cont = "";
		var redcolor=Ge.toGePolygonStyle({"fillColor":"#FF0000","fillOpacity":1});
		var yewcolor=Ge.toGePolygonStyle({"fillColor":"#FFFF00","fillOpacity":1});
		// 创建地标。
		var placemark = ge.createPlacemark('');
		placemark.setStyleSelector(ge.createStyle(''));
		placemark.getStyleSelector().getLabelStyle().setScale(0.6);
		if (this.data) {
			if (this.data instanceof LabelContent) {
				cont = this.data.getStyleContent();
				if (data.strbcch && data.strbcch != "") {
				placemark.getStyleSelector().getLabelStyle().getColor().set(redcolor);
				}else{
				placemark.getStyleSelector().getLabelStyle().getColor().set(yewcolor);
				}
			} else {
				cont = data['content'];
				var color=data['color'];
//				console.log(cont+":"+color);
				if(typeof(color)!='undefined'){
				placemark.getStyleSelector().getLabelStyle().getColor().set(color);
				}
			}
		}
		if("undefined"!=typeof(cont)){
			placemark.setName(cont);
		}else{
			placemark.setName(data);
			placemark.getStyleSelector().getLabelStyle().getColor().set(yewcolor);
		}
		// 设置地标的位置。
		//placemark.setDescription(str);
		var point = this.center;
		placemark.setGeometry(point);
		placemark.getStyleSelector().getIconStyle().getIcon().setW(2);
		//向 Google 地球添加地标。
		ge.getFeatures().appendChild(placemark);
		return placemark;
	}
IsTextLabel.prototype.setStyle=function(obj) {
	
}

// 用于标注频点的内容对象
function LabelContent(strbcch, strtch) {
	this.strbcch = strbcch;
	this.strtch = strtch;
}

// 形成展示的文字
LabelContent.prototype.getStyleContent = function() {
	var content = "";
//	console.log("bcch:"+this.strbcch+" tch:"+this.strtch);
	if (this.strbcch && this.strbcch != "") {
		content = "BCCH " + this.strbcch;
	}
	if (this.strtch && this.strtch != "") {
		content += "TCH " + this.strtch;
	}
	return content;
}

function splitIntoSmallBounds(bounds, columns, rows) {
	var sw = bounds.getSouthWest();
	var ne = bounds.getNorthEast();
	// console.log("outside bounds :
	// sw=("+sw.lng+","+sw.lat+"),ne=("+ne.lng+","+ne.lat+")");
	var span = bounds.toSpan();
	var lngstep = span.lng / columns;
	var latstep = span.lat / rows;
	var boundsArray = new Array();
	var subBound;
	var subsw, subne;
	for ( var i = 0; i < rows; i++) {
		for ( var j = 0; j < columns; j++) {
			subsw = new BMap.Point(sw.lng + lngstep * j, ne.lat - latstep
					* (i + 1));
			subne = new BMap.Point(subsw.lng + lngstep, subsw.lat + latstep);
			// console.log("sub bounds["+i+"]["+j+"] :
			// sw=("+subsw.lng+","+subsw.lat+"),ne=("+subne.lng+","+subne.lat+")");
			subBound = new BMap.Bounds(subsw, subne);
			boundsArray.push(subBound);
		}
	}
	return boundsArray;
}

/**
 * 获取bounds的点序列
 * 
 * @param bounds
 */
function getBoundsPointArray(bounds) {
	var sw = bounds.getSouthWest();
	var ne = bounds.getNorthEast();
	var nw = new BMap.Point(sw.lng, ne.lat);
	var se = new BMap.Point(ne.lng, sw.lat);
	return [ sw, nw, ne, se ];
}
