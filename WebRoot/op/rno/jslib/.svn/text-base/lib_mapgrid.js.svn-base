/**
 * 用于在地图上以网格加载小区的功能类
 *
 * @param showCellObj
 *           小区显示对象
 * @param formId
 *           加载小区所需的表单id
 * @param tipSpanId
 *           用于显示加载状态的提示spanId
 */
function MapGridLib(showCellObj,formId,tipSpanId) {

	//加载小区的表单id
	this.formId = formId;
	//加载提示spanId
	this.tipSpanId = tipSpanId;
	//小区显示对象
	this.showCellObj = showCellObj;
	
	//地图是否处于小区加载中
	this.isLoading = false;
	//加载标识
	this.currentloadtoken = getLoadToken();
	// 地图网格数组
	this.cityMapGrids = new Array(); 	
	//加载小区的额外参数
	this.params = new Object();
	
	//当前屏幕的左下角经度
	this.winMinLng = 0;
	//当前屏幕的左下角纬度
	this.winMinLat = 0;
	//当前屏幕的右上角经度
	this.winMaxLng = 0;
	//当前屏幕的右上角纬度
	this.winMaxLat = 0;
	
	//2015-8-10 cc加入 区域大小阈值，直接通过经纬度计算的相对大小，(maxLat-minLat)*(maxLng-minLng)
	this.areaSquareThreshold = 1.5;
	//2015-8-10 cc 加入 网格数量级，me.defGridGrade平方为网格数量
	this.defGridGrade = 10;
	//2015-8-10 cc加入  超过阈值后的网格数量级
	this.extGridGrade = 25;
	//2015-8-10 cc 加入 默认分页大小
	this.defPageSize = 100;
}
/**
 * 可以通过adjMark开启网格大小的设置
 */
function MapGridLib(showCellObj,formId,tipSpanId,configOption,adjMark) {

	//加载小区的表单id
	this.formId = formId;
	//加载提示spanId
	this.tipSpanId = tipSpanId;
	//小区显示对象
	this.showCellObj = showCellObj;
	//用来开启调整项目的选项
	this.adjMark = adjMark;
	
	//地图是否处于小区加载中
	this.isLoading = false;
	//加载标识
	this.currentloadtoken = getLoadToken();
	// 地图网格数组
	this.cityMapGrids = new Array(); 	
	//加载小区的额外参数
	this.params = new Object();
	
	//当前屏幕的左下角经度
	this.winMinLng = 0;
	//当前屏幕的左下角纬度
	this.winMinLat = 0;
	//当前屏幕的右上角经度
	this.winMaxLng = 0;
	//当前屏幕的右上角纬度
	this.winMaxLat = 0;
	
	//2015-8-10 cc加入 区域大小阈值，直接通过经纬度计算的相对大小，(maxLat-minLat)*(maxLng-minLng)
	this.areaSquareThreshold = 1.5;
	//2015-8-10 cc 加入 网格数量级，me.defGridGrade平方为网格数量
	this.defGridGrade = 10;
	//2015-8-10 cc加入  超过阈值后的网格数量级
	this.extGridGrade = 25;
	//2015-8-10 cc 加入 默认分页大小
	this.defPageSize = 100;
}
MapGridLib.prototype.setIsLoading = function(isLoading) {
	this.isLoading = isLoading;
}
MapGridLib.prototype.getIsLoading = function() {
	return this.isLoading;
}
MapGridLib.prototype.setCurrentloadtoken = function(currentloadtoken) {
	this.currentloadtoken = currentloadtoken;
}
MapGridLib.prototype.getCurrentloadtoken = function() {
	return this.currentloadtoken;
}
//设置当前屏幕范围
MapGridLib.prototype.setWinLngLatRange = function(winMinLng,winMinLat,winMaxLng,winMaxLat) {
	this.winMinLng = winMinLng;
	this.winMinLat = winMinLat;
	this.winMaxLng = winMaxLng;
	this.winMaxLat = winMaxLat;
}

//获取地图网格数据
MapGridLib.prototype.getMapGrids = function() {
	return this.cityMapGrids;
}
MapGridLib.prototype.addMapGrid = function(grid) {
	return this.cityMapGrids.push(grid);
}

MapGridLib.prototype.setDefGridGrade = function(defGridGrade) {
	this.defGridGrade = defGridGrade;
};
MapGridLib.prototype.getDefGridGrade = function() {
	return this.defGridGrade;
};

MapGridLib.prototype.setExtGridGrade = function(extGridGrade) {
	this.extGridGrade = extGridGrade;
};
MapGridLib.prototype.getExtGridGrade = function() {
	return this.extGridGrade;
};

MapGridLib.prototype.setAreaSquareThreshold = function(areaSquareThreshold) {
	this.areaSquareThreshold = areaSquareThreshold;
};
MapGridLib.prototype.getAreaSquareThreshold = function() {
	return this.areaSquareThreshold;
};

MapGridLib.prototype.setDefPageSize = function(defPageSize) {
	this.defPageSize = defPageSize;
};
MapGridLib.prototype.getDefPageSize = function() {
	return this.defPageSize;
};

//初始化地图网格
MapGridLib.prototype.initMapGrids = function() {
	var mapGrids = this.cityMapGrids;
	//调整分页大小
	var pageSize = this.defPageSize;
	if(this.defGridGrade!=10){
		pageSize = this.defGridGrade*this.defGridGrade;
	}
	//初始化
	for(var i = 0; i < mapGrids.length; i++ ){
		mapGrids[i].setIsFinished(false);
		mapGrids[i].setIsLoading(false);
		mapGrids[i].setPageSize(pageSize);
		mapGrids[i].setCurrentPage(1);
		mapGrids[i].setTotalPageCnt(-1);
		mapGrids[i].setTotalCnt(-1);
	}
}

//刷加载标识
MapGridLib.prototype.refreshLoadToken = function() {
	this.currentloadtoken = getLoadToken();
}

/**
 * 创建地图网格
 * 
 * @param cityName
 * @returns
 */
MapGridLib.prototype.createMapGrids = function(cityName) {
	var me = this;
	//将加载状态设置为false
	me.isLoading = false;
	
	//清除网格数据
	me.cityMapGrids.splice(0, me.cityMapGrids.length);
	//清除地图小区缓存
	me.showCellObj.clearData();
	
	var baiduSkel = new Array();//百度轮廓线，元素为point

	var bdary = new BMap.Boundary();

	bdary.get(cityName, function(rs) { //获取行政区域
		var count = rs.boundaries.length; //行政区域的点有多少个
		var lngarr = [];
		var latarr = [];
		for ( var i = 0; i < count; i++) {
 			//console.log(rs.boundaries[i]+" " +(rs.boundaries[i] instanceof Array));
			var pps1 = rs.boundaries[i].split(";");
			var pps2;
			for ( var j = 0; j < pps1.length; j++) {
				pps2 = pps1[j].split(",");
				baiduSkel.push(new BMap.Point(pps2[0], pps2[1]));
				lngarr.push(pps2[0]);
				latarr.push(pps2[1]);				
			}
		}
		//console.log(lngarr);
		//console.log(latarr);
		var minlng = Math.min.apply(null, lngarr);//得到最小经度
		var maxlng = Math.max.apply(null, lngarr);//得到最大经度
		var minlat = Math.min.apply(null, latarr);//得到最小纬度
		var maxlat = Math.max.apply(null, latarr);//得到最大纬度
		//console.log(cityName+" 百度区域边界：maxlng="+maxlng+",minlng="+minlng+",maxlat="+maxlat+",minlat="+minlat);
		if(me.adjMark){
			var areaSquare = (maxlng-minlng)*(maxlat-minlat); // 区域面积
			if(areaSquare>me.areaSquareThreshold){
				me.defGridGrade=me.extGridGrade; //如果区域面积过大，调整网格数量。
			}
		}
		//console.log("地图 左下角："+minlng+","+minlat+" 右上角："+maxlng+","+maxlat+" 面积："+(maxlng-minlng)*(maxlat-minlat)+" 网格数量："+(me.defGridGrade*me.defGridGrade)+" 网格大小："+(maxlng-minlng)*(maxlat-minlat)/(me.defGridGrade*me.defGridGrade));
		/*var stepLng = ((maxlng - minlng) / 10);
		var stepLat = ((maxlat - minlat) / 10);*/
		var stepLng = ((maxlng - minlng) / me.defGridGrade);
		var stepLat = ((maxlat - minlat) / me.defGridGrade);
		//console.log(stepLng+" "+stepLat);
		var a = 0;
/*		for(var i = 0; i < 10; i++) { //行
			for(var j = 0; j < 10; j++) { //列
*/				
		for(var i = 0; i < me.defGridGrade; i++) { //行
			for(var j = 0; j < me.defGridGrade; j++) { //列
				var oneGrid = new MapCityGrid();
				a++;
				oneGrid.setGridId(a);
				oneGrid.setCityId(cityId);
				oneGrid.setMinLng(minlng + j * stepLng);
				oneGrid.setMinLat(minlat + i * stepLat);
				oneGrid.setMaxLng(minlng + (j + 1) * stepLng);
				oneGrid.setMaxLat(minlat + (i + 1) * stepLat);
				me.cityMapGrids.push(oneGrid);
			}
		}
	});
}

/**
 * 设置在屏幕显示范围内的网格加载状态为true，其余为false
 *
 */
MapGridLib.prototype.setDisplayGrids = function(){
	
	var me = this;
	
	var winMinLng = me.winMinLng;
	var winMinLat = me.winMinLat;
	var winMaxLng = me.winMaxLng;
	var winMaxLat = me.winMaxLat;
	//console.log("窗口 左下角："+winMinLng+","+winMinLat+" 右上角："+winMaxLng+","+winMaxLat+"面积："+(winMaxLng-winMinLng)*(winMaxLat-winMinLat));
	var winCnt = 0;
	for(var i = 0; i < me.cityMapGrids.length; i++) {
		var one = me.cityMapGrids[i];
		//设置为默认加载状态
		one.setIsLoading(false);
		//console.log(one);
		//视角某个点落在网格里
		if((winMinLng > one.getMinLng() && winMinLng < one.getMaxLng()
				&& winMinLat > one.getMinLat() && winMinLat < one.getMaxLat()) ||
		   (winMinLng > one.getMinLng() && winMinLng < one.getMaxLng()
				&& winMaxLat > one.getMinLat() && winMaxLat < one.getMaxLat()) ||
		   (winMaxLng > one.getMinLng() && winMaxLng < one.getMaxLng()
				&& winMaxLat > one.getMinLat() && winMaxLat < one.getMaxLat()) ||
		   (winMaxLng > one.getMinLng() && winMaxLng < one.getMaxLng()
				&& winMinLat > one.getMinLat() && winMinLat < one.getMaxLat())) {
			one.setIsLoading(true); //进入加载状态
			winCnt++;
			continue;
		}
		//网格的某个点落在视角里
		if((one.getMinLng() > winMinLng && one.getMinLng() < winMaxLng 
				&& one.getMinLat() > winMinLat && one.getMinLat() < winMaxLat) ||
		   (one.getMinLng() > winMinLng && one.getMinLng() < winMaxLng 
				&& one.getMaxLat() > winMinLat && one.getMaxLat() < winMaxLat) ||
		   (one.getMaxLng() > winMinLng && one.getMaxLng() < winMaxLng 
				&& one.getMaxLat() > winMinLat && one.getMaxLat() < winMaxLat) ||
		   (one.getMaxLng() > winMinLng && one.getMaxLng() < winMaxLng
				&& one.getMinLat() > winMinLat && one.getMinLat() < winMaxLat)) {
			one.setIsLoading(true); //进入加载状态
			winCnt++;
			continue;
		}
	}
/*	for(var i=0; i < me.cityMapGrids.length; i++){
		if(me.cityMapGrids[i].getIsLoading()) {
			console.log(me.cityMapGrids[i]);
		}
	}*/
	//console.log("窗口区域网格数量："+winCnt);
}

/**
 * 
 * @param lastOperTime
 *           上一次浏览时间
 * @param loadToken
 *           加载标识
 * @param minResponseInterval
 *			 事件触发的最小间隔
 * @param showCellObj
 *           地图封装类
 * @param tipSpanId
 *           用于显示加载状态的提示spanId
 * @param params
 *           加载小区的自定义条件
 * @param formId
 *           加载小区所需的表单id
 * @returns  
 *			通过地图网格加载小区
 */
MapGridLib.prototype.loadGisCell = function(lastOperTime, loadToken, minResponseInterval,params){

	var me = this;
	
	//console.log("lastOperTime:"+lastOperTime);
	var ct = new Date().getTime();
	//经过2秒再执行
	if (ct - lastOperTime < minResponseInterval 
				&& loadToken == me.currentloadtoken) {
		// console.log("too frequently.");
		window.setTimeout(function() {
			me.loadGisCell(lastOperTime, loadToken, minResponseInterval, params);
		}, 1000);
		//console.log("还不到2秒,不执行");
		return;
	}

	//判断延迟加载后是否还是当前最新的加载
	if(loadToken != me.currentloadtoken) {
		return;
	}
	//console.log("开始加载");
	//获取需要显示的网格，设置其加载状态为true
	me.setDisplayGrids();
	//循环网格列,先加载第一个
	var i = 0;

	if(params != undefined) {
		me.params = params;
	}
	
	me.getGisCellByMapGrid(i, loadToken);
	//显示屏幕内的小区，隐藏屏幕外的小区，在缩放级别一定值时只显示默认数量的小区
	me.showCellObj.handleMoveEnd();
}


MapGridLib.prototype.getGisCellByMapGrid = function(i, loadToken) {
	
	var me = this;
	if(me.cityMapGrids.length <= 0) {
		return;
	}
	if(me.tipSpanId != null && me.tipSpanId != "" && me.tipSpanId != undefined) {
		$("span#"+me.tipSpanId).html("加载小区中...");
	}
	if(loadToken != me.currentloadtoken) {
		//console.log("新的加载开始了，这次加载结束");
		return;
	}
	//判断是否超出一百个网格
	if(i >= me.defGridGrade*me.defGridGrade) {
		if(me.tipSpanId != null && me.tipSpanId != "" && me.tipSpanId != undefined) {
			$("span#"+me.tipSpanId).html("数据加载完成");
		}
		return;
	}
	//判断地图是否处于加载状态
	if(!me.isLoading) {
		//console.log("小区加载状态："+isLoading);
		if(me.tipSpanId != null && me.tipSpanId != "" && me.tipSpanId != undefined) {
			$("span#"+me.tipSpanId).html("");
		}
		return;
	}
	//判断网格是否加载中
	if(!me.cityMapGrids[i].getIsLoading()) {
		//console.log("mapGrids["+i+"]不处于加载中");
		i++;
		me.getGisCellByMapGrid(i, loadToken);
		return;
	} else {
		//判断网格是否加载完成
		if(me.cityMapGrids[i].getIsFinished()) {
			//console.log("mapGrids["+i+"]加载完成");
			i++;
			me.getGisCellByMapGrid(i, loadToken);
			return;
		} 
	}
	var sendData;
	var	gridId = me.cityMapGrids[i].getGridId();
	var	minLng = me.cityMapGrids[i].getMinLng();
	var	minLat = me.cityMapGrids[i].getMinLat();
	var	maxLng = me.cityMapGrids[i].getMaxLng();
	var	maxLat = me.cityMapGrids[i].getMaxLat();
	var pageSize = me.cityMapGrids[i].getPageSize();
	var currentPage = me.cityMapGrids[i].getCurrentPage();
	var totalPageCnt = me.cityMapGrids[i].getTotalPageCnt();
 	var totalCnt = me.cityMapGrids[i].getTotalCnt();

 	if (me.params['freqType'] == null
				|| me.params['freqType'] == undefined) {
		sendData={
			'mapGrid.gridId' : gridId,
			'mapGrid.minLng' : minLng,
			'mapGrid.minLat' : minLat,
			'mapGrid.maxLng' : maxLng,
			'mapGrid.maxLat' : maxLat,
			'mapGrid.pageSize' : pageSize,
			'mapGrid.currentPage' : currentPage,
			'mapGrid.totalPageCnt' : totalPageCnt,
			'mapGrid.totalCnt' : totalCnt
		};
	} else {
		var freqType = me.params['freqType'];
		sendData={
			'freqType' : freqType,
			'mapGrid.gridId' : gridId,
			'mapGrid.minLng' : minLng,
			'mapGrid.minLat' : minLat,
			'mapGrid.maxLng' : maxLng,
			'mapGrid.maxLat' : maxLat,
			'mapGrid.pageSize' : pageSize,
			'mapGrid.currentPage' : currentPage,
			'mapGrid.totalPageCnt' : totalPageCnt,
			'mapGrid.totalCnt' : totalCnt
		};
	}

	$("#"+me.formId).ajaxSubmit(
			{
				url : 'getGisCellByMapGridForAjaxAction',
				data: sendData,
				dataType : 'json',
				success : function(data) {
					// console.log(data);
					var obj = data;
					try {
						if(loadToken == me.currentloadtoken) {//判断是否是最新的加载
							
							me.showCellObj.showGisCell(obj['gisCells']);
							var page = obj['page'];
							if (page) {
								var pageSize = page['pageSize'] ? page['pageSize'] : 0;
								me.cityMapGrids[i].setPageSize(pageSize);
								
								var currentPage = new Number(
										page['currentPage'] ? page['currentPage'] : 1);
								currentPage++;// 向下一页
								me.cityMapGrids[i].setCurrentPage(currentPage);
	
								var totalPageCnt = new Number(
										page['totalPageCnt'] ? page['totalPageCnt'] : 0);
								me.cityMapGrids[i].setTotalPageCnt(totalPageCnt);
	
								var totalCnt = page['totalCnt'] ? page['totalCnt'] : 0;
								me.cityMapGrids[i].setTotalCnt(totalCnt);
								
								//console.log("网格["+i+"]第"+(currentPage-1)+"页数据加入了缓存");
								if (totalPageCnt >= currentPage) {
									//console.log("继续获取grid["+mapGrids[i].getGridId()+"]的下一页小区");
									me.getGisCellByMapGrid(i, loadToken);
								}else{
									//console.log("grid["+grid.getGridId()+"]加载完成");
									me.cityMapGrids[i].setIsFinished(true);
								}
							}
						} else {
							//console.log("新的加载开始了，这次加载的数据舍弃");
						}
					} catch (err) {
						//停止加载
						me.cityMapGrids[i].setIsLoading(false);
					}
				},
				error : function(xmh, textstatus, e) {
					//alert("出错啦！" + textstatus);
					//停止加载
					me.cityMapGrids[i].setIsLoading(false);
				},
				complete : function() {
					//hideOperTips("loadingDataDiv");
					//判断上一个网格是否加载完，完成才加载下一个网格；这里加入是为了防止（totalPageCnt>=currentPage）条件不满足时还能继续循环
					if(me.cityMapGrids[i].getIsFinished()) {
						i++;
						me.getGisCellByMapGrid(i, loadToken);
					}
				}
			});
}

/**
 * 
 * @param labels
 *           需要预加载的小区label, 格式：'xxx','xxx',....
 * @param func
 *			 在本方法执行完成后再调用的函数	
 * @returns  
 *			 通过labels预加载需要显示的小区,然后调用自定义方法展示
 */
MapGridLib.prototype.loadRelaCellByLabelsOnMap = function(cells,cityId,func) {
	var me = this;
	//console.log("预加载的小区："+cells);
	$.ajax({
		url : 'getRelaCellByLabelsAndCityIdForAjaxAction',
		data : {
			'cells' : cells,
			'cityId' : cityId
		},
		dataType : 'json',
		type : 'post',
		success : function(data) {
			//console.log(data);
			var obj = data;
			if(obj){
				me.showCellObj.showGisCell(obj['gisCells']);
			}
		},
		error : function(xhr, textstatus, e) {
		 	console.log(textstatus);
		},
		complete : function() {
			func();	
		}
	});
}

/**
 * 
 * @param val
 *           参数值
 * @param val
 *           参数类型
 * @param func
 *			 在本方法执行完成后再调用的函数	
 * @returns  
 *			 通过小区参数获取预加载需要显示的小区,然后调用自定义方法展示
 */
MapGridLib.prototype.loadRelaCellByCellParamAndCityIdOnMap = function(val,valType,cityId,areaId,func) {
	var me = this;
	$.ajax({
		url : 'getRelaCellByCellParamAndAreaIdForAjaxAction',
		data : {
			'cellParam.value' : val,
			'cellParam.valueType' : valType,
			'cityId' : cityId,
			'areaId' : areaId
		},
		dataType : 'json',
		type : 'post',
		success : function(data) {
			//console.log(data);
			var obj = data;
			if(obj){
				me.showCellObj.showGisCell(obj['gisCells']);
			}
		},
		error : function(xhr, textstatus, e) {
		 	console.log(textstatus);
		},
		complete : function() {
			func();	
		}
	});
}
/**
 * 
 * @param val
 *           参数值
 * @param val
 *           参数类型
 * @param func
 *			 在本方法执行完成后再调用的函数	
 * @returns  
 *			 通过lte小区参数获取预加载需要显示的lte小区,然后调用自定义方法展示
 */
MapGridLib.prototype.loadLteRelaCellByCellParamAndCityIdOnMap= function(val,valType,cityId,areaId,func) {
	var me = this;
	$.ajax({
		url : 'getRelaCellByCellParamAndAreaIdForAjaxAction',
		data : {
			'cellParam.value' : val,
			'cellParam.valueType' : valType,
			'cityId' : cityId,
			'areaId' : areaId,
			'cellType' : 'lte'
		},
		dataType : 'json',
		type : 'post',
		success : function(data) {
			//console.log(data);
			var obj = data;
			if(obj){
				me.showCellObj.showGisCell(obj['lteCells']);
			}
		},
		error : function(xhr, textstatus, e) {
		 	console.log(textstatus);
		},
		complete : function() {
			func();	
		}
	});
}

/**
 * 
 * @param cell
 *           小区label
 * @param func
 *			 在本方法执行完成后再调用的函数	
 * @returns  
 *			 通过小区获取对应的邻区信息进行预加载,然后调用自定义方法展示
 */
MapGridLib.prototype.loadNcellDetailsByCellOnMap = function(cell,cityId,areaId,func) {
	var me = this;
	var ncells = new Array();
	$.ajax({
		url : 'getNcellDetailsByCellAndAreaIdForAjaxAction',
		data : {
			'cellParam.cell' : cell,
			'cityId' : cityId,
			'areaId' : areaId
		},
		dataType : 'json',
		type : 'post',
		success : function(data) {
			//console.log(data);
			var obj = data;
			if(obj){
				for(var i = 0; i < obj['gisCells'].length; i++){
					if(obj['gisCells'][i]['cell'] == cell) {
						continue;
					}
					ncells.push(obj['gisCells'][i]['cell'])
				}
				me.showCellObj.showGisCell(obj['gisCells']);
			}
		},
		error : function(xhr, textstatus, e) {
		 	console.log(textstatus);
		},
		complete : function() {
			func(ncells);	
		}
	});
}





/***************  LTE 小区部分 ************************/


/**
 * 
 * @param lastOperTime
 *           上一次浏览时间
 * @param loadToken
 *           加载标识
 * @param minResponseInterval
 *			 事件触发的最小间隔
 * @param showCellObj
 *           地图封装类
 * @param tipSpanId
 *           用于显示加载状态的提示spanId
 * @param params
 *           加载小区的自定义条件
 * @param formId
 *           加载小区所需的表单id
 * @returns  
 *			通过地图网格加载小区
 */
MapGridLib.prototype.loadLteCell = function(lastOperTime, loadToken, minResponseInterval,params){

	var me = this;
	
	//console.log("lastOperTime:"+lastOperTime);
	var ct = new Date().getTime();
	//经过2秒再执行
	if (ct - lastOperTime < minResponseInterval 
				&& loadToken == me.currentloadtoken) {
		// console.log("too frequently.");
		window.setTimeout(function() {
			me.loadLteCell(lastOperTime, loadToken, minResponseInterval, params);
		}, 1000);
		//console.log("还不到2秒,不执行");
		return;
	}

	//判断延迟加载后是否还是当前最新的加载
	if(loadToken != me.currentloadtoken) {
		return;
	}
	//console.log("开始加载");
	//获取需要显示的网格，设置其加载状态为true
	me.setDisplayGrids();
	//循环网格列,先加载第一个
	var i = 0;

	if(params != undefined) {
		me.params = params;
	}
	
	me.getLteCellByMapGrid(i, loadToken);
	//显示屏幕内的小区，隐藏屏幕外的小区，在缩放级别一定值时只显示默认数量的小区
	me.showCellObj.handleMoveEnd();
}


MapGridLib.prototype.getLteCellByMapGrid = function(i, loadToken) {
	
	var me = this;
	if(me.cityMapGrids.length <= 0) {
		return;
	}
	if(me.tipSpanId != null && me.tipSpanId != "" && me.tipSpanId != undefined) {
		$("span#"+me.tipSpanId).html("加载小区中...");
	}
	if(loadToken != me.currentloadtoken) {
		//console.log("新的加载开始了，这次加载结束");
		return;
	}
	//判断是否超出一百个网格
	if(i >= me.defGridGrade*me.defGridGrade) {
		if(me.tipSpanId != null && me.tipSpanId != "" && me.tipSpanId != undefined) {
			$("span#"+me.tipSpanId).html("数据加载完成");
		}
		return;
	}
	//判断地图是否处于加载状态
	if(!me.isLoading) {
		//console.log("小区加载状态："+isLoading);
		if(me.tipSpanId != null && me.tipSpanId != "" && me.tipSpanId != undefined) {
			$("span#"+me.tipSpanId).html("");
		}
		return;
	}
	//判断网格是否加载中
	if(!me.cityMapGrids[i].getIsLoading()) {
		//console.log("mapGrids["+i+"]不处于加载中");
		i++;
		me.getLteCellByMapGrid(i, loadToken);
		return;
	} else {
		//判断网格是否加载完成
		if(me.cityMapGrids[i].getIsFinished()) {
			//console.log("mapGrids["+i+"]加载完成");
			i++;
			me.getLteCellByMapGrid(i, loadToken);
			return;
		} 
	}
	var sendData;
	var	gridId = me.cityMapGrids[i].getGridId();
	var	minLng = me.cityMapGrids[i].getMinLng();
	var	minLat = me.cityMapGrids[i].getMinLat();
	var	maxLng = me.cityMapGrids[i].getMaxLng();
	var	maxLat = me.cityMapGrids[i].getMaxLat();
	var pageSize = me.cityMapGrids[i].getPageSize();
	var currentPage = me.cityMapGrids[i].getCurrentPage();
	var totalPageCnt = me.cityMapGrids[i].getTotalPageCnt();
 	var totalCnt = me.cityMapGrids[i].getTotalCnt();

 	if (me.params['freqType'] == null
				|| me.params['freqType'] == undefined) {
		sendData={
			'mapGrid.gridId' : gridId,
			'mapGrid.minLng' : minLng,
			'mapGrid.minLat' : minLat,
			'mapGrid.maxLng' : maxLng,
			'mapGrid.maxLat' : maxLat,
			'mapGrid.pageSize' : pageSize,
			'mapGrid.currentPage' : currentPage,
			'mapGrid.totalPageCnt' : totalPageCnt,
			'mapGrid.totalCnt' : totalCnt
		};
	} else {
		var freqType = me.params['freqType'];
		sendData={
			'freqType' : freqType,
			'mapGrid.gridId' : gridId,
			'mapGrid.minLng' : minLng,
			'mapGrid.minLat' : minLat,
			'mapGrid.maxLng' : maxLng,
			'mapGrid.maxLat' : maxLat,
			'mapGrid.pageSize' : pageSize,
			'mapGrid.currentPage' : currentPage,
			'mapGrid.totalPageCnt' : totalPageCnt,
			'mapGrid.totalCnt' : totalCnt
		};
	}

	$("#"+me.formId).ajaxSubmit(
			{
				url : 'getLteCellByMapGridForAjaxAction',
				data: sendData,
				dataType : 'json',
				success : function(data) {
					// console.log(data);
					var obj = data;
					try {
						if(loadToken == me.currentloadtoken) {//判断是否是最新的加载
							
							me.showCellObj.showGisCell(obj['lteCells']);
							var page = obj['page'];
							if (page) {
								var pageSize = page['pageSize'] ? page['pageSize'] : 0;
								me.cityMapGrids[i].setPageSize(pageSize);
								
								var currentPage = new Number(
										page['currentPage'] ? page['currentPage'] : 1);
								currentPage++;// 向下一页
								me.cityMapGrids[i].setCurrentPage(currentPage);
	
								var totalPageCnt = new Number(
										page['totalPageCnt'] ? page['totalPageCnt'] : 0);
								me.cityMapGrids[i].setTotalPageCnt(totalPageCnt);
	
								var totalCnt = page['totalCnt'] ? page['totalCnt'] : 0;
								me.cityMapGrids[i].setTotalCnt(totalCnt);
								
								//console.log("网格["+i+"]第"+(currentPage-1)+"页数据加入了缓存");
								if (totalPageCnt >= currentPage) {
									//console.log("继续获取grid["+mapGrids[i].getGridId()+"]的下一页小区");
									me.getLteCellByMapGrid(i, loadToken);
								}else{
									//console.log("grid["+grid.getGridId()+"]加载完成");
									me.cityMapGrids[i].setIsFinished(true);
								}
							}
						} else {
							//console.log("新的加载开始了，这次加载的数据舍弃");
						}
					} catch (err) {
						//停止加载
						me.cityMapGrids[i].setIsLoading(false);
					}
				},
				error : function(xmh, textstatus, e) {
					//alert("出错啦！" + textstatus);
					//停止加载
					me.cityMapGrids[i].setIsLoading(false);
				},
				complete : function() {
					//hideOperTips("loadingDataDiv");
					//判断上一个网格是否加载完，完成才加载下一个网格；这里加入是为了防止（totalPageCnt>=currentPage）条件不满足时还能继续循环
					if(me.cityMapGrids[i].getIsFinished()) {
						i++;
						me.getLteCellByMapGrid(i, loadToken);
					}
				}
			});
}
MapGridLib.prototype.loadLteCellBySector = function(lastOperTime, loadToken, minResponseInterval,params){

	var me = this;
	
	//console.log("lastOperTime:"+lastOperTime);
	var ct = new Date().getTime();
	//经过2秒再执行
	if (ct - lastOperTime < minResponseInterval 
				&& loadToken == me.currentloadtoken) {
		// console.log("too frequently.");
		window.setTimeout(function() {
			me.loadLteCellBySector(lastOperTime, loadToken, minResponseInterval, params);
		}, 1000);
		//console.log("还不到2秒,不执行");
		return;
	}

	//判断延迟加载后是否还是当前最新的加载
	if(loadToken != me.currentloadtoken) {
		return;
	}
	//console.log("开始加载");
	//获取需要显示的网格，设置其加载状态为true
	me.setDisplayGrids();
	//循环网格列,先加载第一个
	var i = 0;

	if(params != undefined) {
		me.params = params;
	}
	
	me.getLteCellByMapGridAndSector(i, loadToken);
	//显示屏幕内的小区，隐藏屏幕外的小区，在缩放级别一定值时只显示默认数量的小区
	me.showCellObj.handleMoveEnd();
};
/**
 * 2015-7-30 cc 修改GIS小区三角形为扇形
 * @param i
 * @param loadToken
 */
MapGridLib.prototype.getLteCellByMapGridAndSector = function(i, loadToken) {
	
	var me = this;
	if(me.cityMapGrids.length <= 0) {
		return;
	}
	if(me.tipSpanId != null && me.tipSpanId != "" && me.tipSpanId != undefined) {
		$("span#"+me.tipSpanId).html("加载小区中...");
	}
	if(loadToken != me.currentloadtoken) {
		//console.log("新的加载开始了，这次加载结束");
		return;
	}
	//判断是否超出一百个网格
	if(i >= me.defGridGrade*me.defGridGrade) {
		if(me.tipSpanId != null && me.tipSpanId != "" && me.tipSpanId != undefined) {
			$("span#"+me.tipSpanId).html("数据加载完成");
		}
		return;
	}
	//判断地图是否处于加载状态
	if(!me.isLoading) {
		//console.log("小区加载状态："+isLoading);
		if(me.tipSpanId != null && me.tipSpanId != "" && me.tipSpanId != undefined) {
			$("span#"+me.tipSpanId).html("");
		}
		return;
	}
	//判断网格是否加载中
	if(!me.cityMapGrids[i].getIsLoading()) {
		//console.log("mapGrids["+i+"]不处于加载中");
		i++;
		me.getLteCellByMapGridAndSector(i, loadToken);
		return;
	} else {
		//判断网格是否加载完成
		if(me.cityMapGrids[i].getIsFinished()) {
			//console.log("mapGrids["+i+"]加载完成");
			i++;
			me.getLteCellByMapGridAndSector(i, loadToken);
			return;
		} 
	}
	var sendData;
	var	gridId = me.cityMapGrids[i].getGridId();
	var	minLng = me.cityMapGrids[i].getMinLng();
	var	minLat = me.cityMapGrids[i].getMinLat();
	var	maxLng = me.cityMapGrids[i].getMaxLng();
	var	maxLat = me.cityMapGrids[i].getMaxLat();
	var pageSize = me.cityMapGrids[i].getPageSize();
	var currentPage = me.cityMapGrids[i].getCurrentPage();
	var totalPageCnt = me.cityMapGrids[i].getTotalPageCnt();
 	var totalCnt = me.cityMapGrids[i].getTotalCnt();

 	if (me.params['freqType'] == null
				|| me.params['freqType'] == undefined) {
		sendData={
			'mapGrid.gridId' : gridId,
			'mapGrid.minLng' : minLng,
			'mapGrid.minLat' : minLat,
			'mapGrid.maxLng' : maxLng,
			'mapGrid.maxLat' : maxLat,
			'mapGrid.pageSize' : pageSize,
			'mapGrid.currentPage' : currentPage,
			'mapGrid.totalPageCnt' : totalPageCnt,
			'mapGrid.totalCnt' : totalCnt
		};
	} else {
		var freqType = me.params['freqType'];
		sendData={
			'freqType' : freqType,
			'mapGrid.gridId' : gridId,
			'mapGrid.minLng' : minLng,
			'mapGrid.minLat' : minLat,
			'mapGrid.maxLng' : maxLng,
			'mapGrid.maxLat' : maxLat,
			'mapGrid.pageSize' : pageSize,
			'mapGrid.currentPage' : currentPage,
			'mapGrid.totalPageCnt' : totalPageCnt,
			'mapGrid.totalCnt' : totalCnt
		};
	}

	$("#"+me.formId).ajaxSubmit(
			{
				url : 'getLteCellByMapGridForAjaxAction',
				data: sendData,
				dataType : 'json',
				success : function(data) {
					// console.log(data);
					var obj = data;
					try {
						if(loadToken == me.currentloadtoken) {//判断是否是最新的加载
							
							me.showCellObj.showLteCellBySector(obj['lteCells']);
							var page = obj['page'];
							if (page) {
								var pageSize = page['pageSize'] ? page['pageSize'] : 0;
								me.cityMapGrids[i].setPageSize(pageSize);
								
								var currentPage = new Number(
										page['currentPage'] ? page['currentPage'] : 1);
								currentPage++;// 向下一页
								me.cityMapGrids[i].setCurrentPage(currentPage);
	
								var totalPageCnt = new Number(
										page['totalPageCnt'] ? page['totalPageCnt'] : 0);
								me.cityMapGrids[i].setTotalPageCnt(totalPageCnt);
	
								var totalCnt = page['totalCnt'] ? page['totalCnt'] : 0;
								me.cityMapGrids[i].setTotalCnt(totalCnt);
								
								//console.log("网格["+i+"]第"+(currentPage-1)+"页数据加入了缓存");
								if (totalPageCnt >= currentPage) {
									//console.log("继续获取grid["+mapGrids[i].getGridId()+"]的下一页小区");
									me.getLteCellByMapGridAndSector(i, loadToken);
								}else{
									//console.log("grid["+grid.getGridId()+"]加载完成");
									me.cityMapGrids[i].setIsFinished(true);
								}
							}
						} else {
							//console.log("新的加载开始了，这次加载的数据舍弃");
						}
					} catch (err) {
						//停止加载
						me.cityMapGrids[i].setIsLoading(false);
					}
				},
				error : function(xmh, textstatus, e) {
					//alert("出错啦！" + textstatus);
					//停止加载
					me.cityMapGrids[i].setIsLoading(false);
				},
				complete : function() {
					//hideOperTips("loadingDataDiv");
					//判断上一个网格是否加载完，完成才加载下一个网格；这里加入是为了防止（totalPageCnt>=currentPage）条件不满足时还能继续循环
					if(me.cityMapGrids[i].getIsFinished()) {
						i++;
						me.getLteCellByMapGridAndSector(i, loadToken);
					}
				}
			});
}
