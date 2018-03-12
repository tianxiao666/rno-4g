function LimitQueue(maxSize) {
	this.maxSize = (maxSize != null && maxSize != undefined
			&& typeof maxSize == 'number' && maxSize > 0) ? maxSize : 10;
	this.values = new Array();
	this.keys = new Array();
}
LimitQueue.prototype.put = function(key, value) {
	if (key == null || key == undefined) {
		return;
	}
	if (this.keys.length > this.maxSize) {
		this.removeFirst();
	}
	this.keys.push(key);
	this.values.push(value);
}

LimitQueue.prototype.removeFirst = function() {
	this.keys = this.keys.shift();
	this.values = this.values.shift();
}

LimitQueue.prototype.remove = function(key) {
	for ( var i = 0; i < this.keys.length; i++) {
		if (key == this.keys[i]) {
			// 删除该key以及value
			this.keys.splice(i, 1);
			this.values.splice(i, 1);
		}
	}
}
LimitQueue.prototype.get = function(key) {
	for ( var i = 0; i < this.keys.length; i++) {
		if (key == this.keys[i]) {
			return this.values[i];
		}
	}
	return null;
}

var ncsDateToTime = new Object();// key为date
// value为一个array，array里的元素有2个key：ncsId,time
var ncsIdToDetail = new Object();// key为ncsId，value为ncs对象
var ncsDates = new Array();// 日期
var selectCell = "";
var selectBsc = "";

var charts = null;
var currentCellNcsData = null;// 当前小区对应的某个ncs的信息，
var cellFreqInterferQueue = new LimitQueue(10);// key为ncsId+"_"+cell，value为该ncs下的该小区的频点的干扰情况

var bscToCells = new Object();
//var ncells = new Array();

$(document).ready(
		function() {

			initAreaCascade();

			getAllBscCell($("#cityId1").val());

			$(".cellCls").live("click", function() {
				getCellNcsList($(this).attr("data"),$(this).attr("bsc"),$(this).attr("manufacturers"));
				selectBsc = $(this).attr('bsc');
			});
			$(".cellCls").live("mouseover", function() {
//				var str=$(this).text();
				$(this).text($(this).attr("all"));
			});
			$(".cellCls").live("mouseout", function() {
//				var str=$(this).text();
				$(this).text($(this).attr("part"));
			});
			// 日期下来框联动
			$("#ncsDate").change(
					function() {
						// 情况时间下拉框，重新绑定时间
						$("#ncsTime").html("");
						var startdate = $("#ncsDate").val();
						var times = ncsDateToTime[startdate];
						if (times) {
							var ts = "";
							for ( var j = 0; j < times.length; j++) {
								ts += "<option value='" + times[j]['ncsId'] +","+ times[j]['manufacturers']
										+ "'>" + times[j]['time'] + "</option>"
							}
							// alert(ts);
							$("#ncsTime").html(ts);
							//加一次触发事件，是因为华为ncs数据没有时间，取00:00:00，当改变日期的时候，
							//时间select不变，change事件不触发
							$("#ncsTime").trigger("change");
						}
					});
			// 时间下来框
			$("#ncsTime").change(function() {
				var valStr = $("#ncsTime").val();
				var val = valStr.split(",");
				var ncsId = val[0];
				var manufacturers = val[1];
				getCellNcsInfo(ncsId, selectCell, manufacturers);
				showNcsInfo(ncsIdToDetail[ncsId]);
			});

			// 数据类型变化
			$("#ncsDataType").change(function() {
				var ncsId = $("#ncsTime").val();
				if (!ncsId || ncsId == '') {
					return;
				}
				// 图表
				displayChart(currentCellNcsData, $("#ncsDataType").val());
			});
			
			 $.datepicker.regional['zh-CN'] = {
			       
			        monthNames: ['一月','二月','三月','四月','五月','六月', '七月','八月','九月','十月','十一月','十二月'],
			        monthNamesShort: ['一月','二月','三月','四月','五月','六月', '七月','八月','九月','十月','十一月','十二月'],
			        dayNamesShort: ['周日','周一','周二','周三','周四','周五','周六'],
			        dayNamesMin: ['日','一','二','三','四','五','六'],
			        closeText: '关闭',
			        constrainInput: true,
			        gotoCurrent:true
			      };
			$.datepicker.setDefaults($.datepicker.regional['zh-CN']);
			$("#mrrMeaBegDate").datetimepicker(
					{
						dateFormat : "yy-mm-dd",
						timeFormat: "",
						defaultDate : "-2",
						changeMonth : true,
						numberOfMonths : 1,
						showButtonPanel:true,//是否显示按钮面板  
						closeText: '关闭',
						currentText:'加载数据',
						maxDate:new Date(),
						gotoCurrent:true,
						timeText:'',
						//buttonClose:false,
						onClose : function(selectedDate,e) {
							/*$("#mrrMeaEndDate").datetimepicker("option", "minDate",
									selectedDate);*/
							if($("#mrrMeaEndDate").datetimepicker("getDate")>addDays(new Date($.trim(selectedDate)),+30)){
								$("#mrrMeaEndDate").datetimepicker("option", "minDate",
										addDays(new Date($.trim(selectedDate)),+30));
							}
							if($("#mrrMeaEndDate").datetimepicker("getDate")<new Date($.trim(selectedDate))){
								$("#mrrMeaEndDate").datetimepicker("option", "minDate",
										addDays(new Date($.trim(selectedDate)),+30));
							}
						},
						onSelect : function(selectedDate) {
							if($("#mrrMeaEndDate").datetimepicker("getDate")>addDays(new Date($.trim(selectedDate)),+30)){
								$("#mrrMeaEndDate").datetimepicker("setDate",addDays(new Date($.trim(selectedDate)),+30));
							}
							if($("#mrrMeaEndDate").datetimepicker("getDate")<new Date($.trim(selectedDate))){
								$("#mrrMeaEndDate").datetimepicker("setDate",addDays(new Date($.trim(selectedDate)),+30));
							}
						}
			});
			 
//			$("#mrrMeaBegDate").datetimepicker("setDate",-30);// 减去2天
//			console.log($('#mrrMeaBegDate').datepicker('option', 'buttonText'));
//			$('#mrrMeaBegDate').datepicker('option', 'currentText','加载');
			$("#mrrMeaBegDate").datetimepicker("setDate",addDays(new Date(),-30).Format("yyyy-MM-dd"));// 减去30天

			$("#mrrMeaEndDate").datetimepicker(
					{
						dateFormat : "yy-mm-dd",
						timeFormat: "",
						defaultDate :"+1w",
						changeMonth : true,
						numberOfMonths : 1,
						showButtonPanel:true,//是否显示按钮面板  
						maxDate:new Date(),
						closeText: '关闭',
						currentText:'加载数据',
						gotoCurrent:true,
						timeText:'',
						onClose : function(selectedDate) {
							/*$("#mrrMeaBegDate").datetimepicker("option", "maxDate",
									selectedDate);*/
							if($("#mrrMeaBegDate").datetimepicker("getDate")<addDays(new Date($.trim(selectedDate)),-30)){
								$("#mrrMeaBegDate").datetimepicker("option", "maxDate",
										addDays(new Date($.trim(selectedDate)),-30));
								}
							if($("#mrrMeaBegDate").datetimepicker("getDate")>new Date($.trim(selectedDate))){
								$("#mrrMeaBegDate").datetimepicker("option", "maxDate",
										addDays(new Date($.trim(selectedDate)),-30));
								}
						},
						onSelect : function(selectedDate) {
							if($("#mrrMeaBegDate").datetimepicker("getDate")<addDays(new Date($.trim(selectedDate)),-30)){
							$("#mrrMeaBegDate").datetimepicker("setDate",addDays(new Date($.trim(selectedDate)),-30));
							}
							if($("#mrrMeaBegDate").datetimepicker("getDate")>new Date($.trim(selectedDate))){
								$("#mrrMeaBegDate").datetimepicker("setDate",addDays(new Date($.trim(selectedDate)),-30)) ;
								}
						}
			});
			$("#mrrMeaEndDate").datetimepicker("setDate",(new Date()));
			
			//Liang YJ 2014-3-18 显示干扰频点
			$("#interfer").click(function(){
				var ncsId = $("#ncsTime").val();
				if (!ncsId || ncsId == '') {
					return;
				}
				displayCellFreqInterferInfo(ncsId,selectCell);
			});

			// 设置chartdiv的高
//			$("#chartDiv").css("height", $("#ncsInfoDiv").css("height"));

			// 展开、收缩小区ncs信息的控制
			$("#toggleCellInfoDiv").click(function() {
				if ($("#cellInfoTab").css("display") != 'none') {
					$("#cellInfoTab").css("display", "none");
					$("#toggleCellInfoDiv").html("原始数据(点击展开)");
				} else {
					$("#toggleCellInfoDiv").html("原始数据(点击收缩)");
					$("#cellInfoTab").css("display", 'block');
				}
			});

			// 模糊查询
			$("#inputCell").keyup(function() {
				searchCell();
			});

			// set highchart
			sethighchart();
			
			
			$("#selectDate").click(function(e){  
				   $("#selectCheckDateDiv").show();
                   $("#selectCheckDateDiv").css({top:e.pageY+10,left:e.pageX+10})  
            });

			//选择日期重新加载
			$(document).delegate('.ui-datepicker-current', 'click',
					function() {
						//日期控件点击加载的方法
						
					});
});

/**
 * 输入小区名，模糊查询小区，把符合条件的小区列出来， 其他的隐藏
 */
function matchCell() {
	var cell = $.trim($("#inputCell").val());
	var count=0;
	if (cell == '') {
//		console.log("全部显示");
		$("span.cellCls").each(function(i, ele) {
			if ($(ele).closest("li").css("display") == 'none') {
				$(ele).closest("li").css("display", '');
				
			}
		});
/*							$("span.bscCls").each(function(i, ele) {
			var aa=$(ele).text();//HZSMNB8（小区数量:85）
			var bscstr=aa.substring(0,aa.indexOf("（"));//HZSMNB8
//			console.log(aa.substring(0,aa.indexOf("（")));
//			console.log(aa);
//			console.log(aa.indexOf("（"));
//			console.log(aa.substring(aa.indexOf("（")));
//			console.log($("span[bsc='"+bscstr+"']").attr("bsc"));
			$("span[bsc='"+bscstr+"']").each(function(i, ele) {
//					console.log("i:"+i);
					if ($(ele).closest("li").css("display") != 'none') {
							count++;
//							console.log("count:"+count);
						}
					
			})
			$(ele).text(bscstr+"（小区数量:"+count+"）");
			count=0;
		})*/
	} else {
		cell = cell.toUpperCase();
//		console.log("筛选【" + cell + "】");
		$("span.cellCls").each(function(i, ele) {
			if ($(ele).attr("data").indexOf(cell) >= 0) {
				if ($(ele).closest("li").css("display") == 'none') {
					$(ele).closest("li").css("display", '');
				}
			} else {
				$(ele).closest("li").css("display", 'none');
			}
		});
		
	}
			$("span.bscCls").each(function(i, ele) {
			var aa=$(ele).text();//HZSMNB8（小区数量:85）
			var bscstr=aa.substring(0,aa.indexOf("（"));//HZSMNB8
//			console.log(aa.substring(0,aa.indexOf("（")));
//			console.log(aa);
//			console.log(aa.indexOf("（"));
//			console.log(aa.substring(aa.indexOf("（")));
//			console.log($("span[bsc='"+bscstr+"']").attr("bsc"));
			$("span[bsc='"+bscstr+"']").each(function(i, ele) {
//					console.log("i:"+i);
					if ($(ele).closest("li").css("display") != 'none') {
							count++;
//							console.log("count:"+count);
						}
					
			})
			$(ele).text(bscstr+"（小区数量:"+count+"）");
			if(count==0){
				$(ele).closest("li").css("display", 'none');
			}else{
				$(ele).closest("li").css("display", '');
			}
			count=0;
		});
}

/**
 * 输入小区名，模糊查询小区，把符合条件的小区列出来， 其他的隐藏
 */
function searchCell() {
	var cell = $.trim($("#inputCell").val());
	//清空
	$("#allBscCell").html("");

	//符合搜索条件的bsc到cell信息的映射对象
	var obj = new Object();
	var list;
	for(var key in bscToCells) {
		list = new Array();
		for(var i=0; i<bscToCells[key].length; i++) {
			if(bscToCells[key][i]["LABEL"].indexOf(cell) >= 0) {
				list.push(bscToCells[key][i]);
			}
		}
		if(list.length > 0) {
			obj[key] = list;
		}
	}
	//console.log(obj);
	
	//排序BSC
	var bscList = new Array();
	for(var key in obj) {
		bscList.push(key);
	}
	bscList.sort();
	//console.log(bscList);
	var bscHtml = "";
	
	for(var i=0; i<bscList.length; i++) {
		bscHtml += "<li><span class='bscCls' id='"+bscList[i]+"'>" + bscList[i] + "(小区数量：" 
			+ obj[bscList[i]].length + ")</span><ul id='"+bscList[i]+"'></ul></li>";
	} 

	$("#allBscCell").html(bscHtml);
	$("#allBscCell").treeview({
		collapsed : true
	});

	$("span.bscCls").on("click", function() {
		var bsc = $(this).attr("id");
		var cells1 = obj[bsc];
		var cellsHtml = "";
		var all;
		var part;
		for(var i=0; i<cells1.length; i++) {
			all = cells1[i]['LABEL'] + "(" + cells1[i]['NAME'] + ")";
			part = all.length>12?all.substring(0,12)+"...":all;
			cellsHtml += "<li><span class='cellCls' data='" + cells1[i]['LABEL'] + "'" 
					+ " bsc='" + bsc + "'" 
					+ " manufacturers='"+cells1[i]['MANUFACTURERS']+"' " 
					+ " all='"+all+"' " 
					+ " part='"+part+"'>" 
					+ part +"</span></li>";
		}
		$("ul#"+bsc).html(cellsHtml);
	});
	//图标事件
	$("#allBscCell").find("div").bind("click", function(event) {
		var bsc = $(this).parent().find("span").attr("id");
		var cells2 = obj[bsc];
		//console.log(cells);
		var cellsHtml = "";
		var all;
		var part;
		for(var i=0; i<cells2.length; i++) {
			all = cells2[i]['LABEL'] + "(" + cells2[i]['NAME'] + ")";
			part = all.length>12?all.substring(0,12)+"...":all;
			cellsHtml += "<li><span class='cellCls' data='" + cells2[i]['LABEL'] + "'" 
					+ " bsc='" + bsc + "'" 
					+ " manufacturers='"+cells2[i]['MANUFACTURERS']+"' " 
					+ " all='"+all+"' " 
					+ " part='"+part+"'>" 
					+ part +"</span></li>";
		}
		$("ul#"+bsc).html(cellsHtml);
    });

}

/**
 * 设置highchart
 */
function sethighchart() {

	Highcharts.setOptions({
		chart : {
			backgroundColor : {
				linearGradient : [ 0, 0, 500, 500 ],
				stops : [ [ 0, 'rgb(255, 255, 255)' ],
						[ 1, 'rgb(240, 240, 255)' ] ]
			},
			borderWidth : 2,
			plotBackgroundColor : 'rgba(255, 255, 255, .9)',
			plotShadow : true,
			plotBorderWidth : 1
		}
	});

	// 空图表
	var options = {
		chart : {
			zoomType : 'xy'
		},
		title : {
			text : '六强比例'
		},
		xAxis : [ {
			id : 'ncellAxis',
			gridLineWidth : 1,
			categories : [ '邻区' ],
			labels : {
				rotation : 90
			}
		} ],
		yAxis : [ { // Primary yAxis
			id : 'ratioAxis',
			labels : {
				style : {
					color : '#89A54E'
				}
			},
			title : {
				text : '六强比例（%）'
			},

		}, { // Tertiary yAxis
			id : 'disAxis',
			gridLineWidth : 1,
			title : {
				text : '距离',
				style : {
					color : '#AA4643'
				}
			},
			labels : {
				style : {
					color : '#AA4643'
				}
			},
			opposite : true
		} ],
		tooltip : {
			shared : false,
			formatter : function() {
				return this.x + "---" + this.y;
			}
		},
		legend : {
			layout : 'horizontal',
			align : 'left',
			x : 20,
			verticalAlign : 'top',
			y : -5,
			floating : true,
			backgroundColor : '#FFFFFF'
		},

	//			
	};
	chart = $('#chartDiv').highcharts(options);

}

/**
 * 创建柱状图的data
 * 
 * @param cellNcsArr
 */
function createRatioData(cellNcsArr, code, multiplier) {

	var data = new Array();
	if (!cellNcsArr) {
		return data;
	}
	var one = null;
	var isnei = 0;
	var field = "";
	if (code == 'six') {
		field = 'TOPSIX'
	} else if (code == 'two') {
		field = 'TOPTWO';
	} else if (code == 'cellrate') {
		field = 'CELLRATE';
	} else if (code == 'abss') {
		field = 'ABSSRATE';
	} else if (code = 'alone') {
		field = 'ALONERATE';
	} else {
		return data;
	}

	if (multiplier == undefined || multiplier == null) {
		multiplier = 1;
	}
	for ( var i = 0; i < cellNcsArr.length; i++) {
		one = new Object();
		isnei = cellNcsArr[i]['DEFINED_NEIGHBOUR1'];
		var aa=cellNcsArr[i];
//		for(var key in aa){
//			console.log(key+":"+aa[key]);
//		}
		if (isnei == '0') {
			one['color'] = 'yellow';
		} else {
			one['color'] = 'red';
		}
		// console.log(cellNcsArr[i][field]*multiplier + '---'+new
		// Number(cellNcsArr[i][field]*multiplier).toFixed(3));
		// one['y'] = new Number(cellNcsArr[i][field]*multiplier).toFixed(3)+'';
		one['y'] = cellNcsArr[i][field];
		one['x']=i;//cellNcsArr记录x下标
		
		//peng.jm 2015-1-26 加入 start
		one['ncell'] = cellNcsArr[i]['NCELL']; //记录ncell
		cellNcsArr[i]['DISTANCE1'] == "10000000000" ? "": cellNcsArr[i]['DISTANCE1'];
		one['dis'] = cellNcsArr[i]['DISTANCE1']; // 记录距离
		//peng.jm 2015-1-26 加入end
		
//		console.log("one:"+one['color']);
		data.push(one);
	}
	
	
	//peng.jm 2015-1-26 加入 start
	//对各项指标值进行排序
	var temp;
	var x;
	for ( var i = 0; i < data.length; i++) {
		for ( var j = i; j < data.length; j++) {
			if(data[j]['y'] >= data[i]['y']) {
				 temp = data[i];
				 data[i] = data[j];
				 data[j] = temp;
				 
				 x = data[i]['x'];
				 data[i]['x'] = data[j]['x'];
				 data[j]['x'] = x;
			}
		}
	}
	//peng.jm 2015-1-26 加入 end
	
	//peng.jm 2015-3-18 加入 start
	var isNeiArray = new Array(); //定义邻区
	var notNeiArray = new Array(); //未定义邻区
	for ( var i = 0; i < data.length; i++) {
		if(data[i]['color'] == 'red') {
			isNeiArray.push(data[i]);
		} else {
			notNeiArray.push(data[i]);
		}
	}
//	console.log(isNeiArray);
//	console.log(notNeiArray);
	
	var result = new Array();
	
	if(isNeiArray.length == 0) {
		for ( var i = 0; i < notNeiArray.length; i++) {
			if(i>=60) break;
			result.push(notNeiArray[i]);
		}
	} 
	else if(isNeiArray.length>0 && isNeiArray.length<60) {
		for ( var i = 0; i < isNeiArray.length; i++) {
			result.push(isNeiArray[i]);
		}
		for ( var i = result.length; i < notNeiArray.length; i++) {
			if(i>=60) break;
			result.push(notNeiArray[i]);
		}
	}
	else if(isNeiArray>60) {
		for ( var i = 0; i < isNeiArray.length; i++) {
			if(i>=60) break;
			result.push(isNeiArray[i]);
		}
	}
	
	//初始化60条数据的x值
	for ( var i = 0; i < result.length; i++) {
		result[i]['x'] = i;
	}
	//将整理好的60条数据再进行排序，保证从大到小
	for ( var i = 0; i < result.length; i++) {
		for ( var j = i; j < result.length; j++) {
			if(result[j]['y'] >= result[i]['y']) {
				 temp = result[i];
				 result[i] = result[j];
				 result[j] = temp;
				 
				 x = result[i]['x'];
				 result[i]['x'] = result[j]['x'];
				 result[j]['x'] = x;
			}
		}
	}
	
	//peng.jm 2015-3-18 加入 end

	
	//peng.jm 2015-1-26 加入 end
	
//	//peng.jm 2015-3-10 加入 start
//	//限制显示60个，而且以存在邻区关系的为优先选取; ncells保存了邻区关系
//	var defArray = new Array();  //在回传数据中存在且在邻区关系中存在的数据 
//	var notdefArray = new Array(); //在回传数据中存在，但在邻区关系中不存在
//	for ( var i = 0; i < data.length; i++) {
//		for ( var j = 0; j < ncells.length; j++) {
//			if(data[i]['ncell'] == ncells[j]) {
//				defArray.push(data[i]);
//			} else {
//				notdefArray.push(data[i]);
//			}	
//		}
//	}
//	//将剩余的ncells也补充到defArray中
//	for ( var i = 0; i < ncells.length; i++) {
//		for ( var j = 0; j < defArray.length; j++) {
//			if(ncells[i]==defArray['ncell']) 
//				continue;
//		}
//		one = new Object();
//		one['color'] = 'red';
//		one['ncell'] = ncells[i];
//		one['x'] = 0;
//		one['y'] = 0;
//		defArray.push(one);
//	}
//	
//	//优先从defArray取， 不足60的，从notdefArray取补充
//	var res = new Array();
//	if(defArray.length == 0) {
//			var n = 1;
//			for ( var i = 0; i < notdefArray.length; i++) {
//				if(n > 60) break;
//				res.push(defArray[i]);
//				n++;
//			}
//	} else if(defArray.length > 0 && defArray.length < 60) {
//		var n = 1;
//		for ( var i = 0; i < defArray.length; i++) {
//			res.push(defArray[i]);
//			n++;
//		}
//		for ( var i = 0; i < notdefArray.length; i++) {
//			if(n > 60) break;
//			res.push(defArray[i]);
//			n++;
//		}
//	} else if(defArray.length == 60) {
//		for ( var i = 0; i < defArray.length; i++) {
//			res.push(defArray[i]);
//		}
//	} else if(defArray.length > 60) {
//		var n = 1;
//		for ( var i = 0; i < defArray.length; i++) {
//			if(n > 60) break;
//			res.push(defArray[i]);
//			n++;
//		}
//	}
//	
//	var result = new Array();
//	//对最终返回结果result的横坐标x初始化
//	for ( var i = 0; i < res.length; i++) {
//		one = new Object();
//		one['color'] = res[i]['color'];
//		one['ncell'] = res[i]['ncell'];
//		one['x'] = i;
//		one['y'] = res[i]['y'];
//		result.push(one);
//	}
//	console.log(result);
//	
//	//对最终返回结果result从大到小排序
//	for ( var i = 0; i < result.length; i++) {
//		for ( var j = i; j < result.length; j++) {
//			if(result[j]['y'] >= result[i]['y']) {
//				 temp = result[i];
//				 result[i] = result[j];
//				 result[j] = temp;
//				 
//				 x = result[i]['x'];
//				 result[i]['x'] = result[j]['x'];
//				 result[j]['x'] = x;
//			}
//		}
//	}
//	//peng.jm 2015-3-10 加入 end
	//console.log(result.length);
	return result;
}

/**
 * 获取指定的ncs下的cell的测量信息
 * 
 * @param ncsId
 * @param cell
 */
function getCellNcsInfo(ncsId, cell, manufacturers) {
	//console.log(ncsId);
	var cityId = $("#cityId1").val();
	showOperTips("loadingDataDiv", "loadContentId", "加载NCS数据中");
	var ncsDate=$("#ncsDate").find("option:selected").val();
	var ncsTime=$("#ncsTime").find("option:selected").text();
	var ncsDateTime=ncsDate+" "+ncsTime;
	$.ajax({
		url : 'getNcsCellInfoForAjaxAction',
		data : {
			'ncsId' : ncsId,
			"cell" : cell,
			'cityId' : cityId,
			'manufacturers' : manufacturers,
			'ncsDateTime':ncsDateTime
		},
		type : 'post',
		dataType : 'text',
		success : function(raw) {
			var res = eval("(" + raw + ")");
			
			//var data = res['data'];
			var data = res;
//			for ( var i = 0; i < res['ncells'].length; i++) {
//				ncells.push(res['ncells'][i]['NCELL']);
//			}

			if(data.length != 0) {
				currentCellNcsData = data;// 更新小区对应的该ncs的信息
				showCellNcsInfo(data);			
			} else {
				$("#ncsDate").html("");
				$("#ncsTime").html("");
				hideOperTips("loadingDataDiv");
				animateInAndOut("operInfo", 500, 500, 1000, "operTip",
						"该小区无测量数据！");
				return;
			}

		},
		complete : function() {
			hideOperTips("loadingDataDiv");
		},complete : function() {
			hideOperTips("loadingDataDiv");
		}
	});

}

/**
 * 显示小区的ncs测量数据 显示在表格，以及图表显示
 * 
 * @param data
 */
function showCellNcsInfo(data) {

	// 情况表格显示
	$("#cellInfoTab tr:not(:first)").each(function(i, ele) {
		$(ele).remove();
	});

	// 清空图表显示

	if (!data || data.length == 0) {
		animateInAndOut("operInfo", 500, 500, 1000, "operTip", "该小区无测量数据！");
		hideOperTips("loadingDataDiv");
		 alert("该小区无测量数据！") ;
		return;
	}

	// 重新显示表格
	var ht = "";
	var one = null;
	var cells = new Array();
//	var colorarr=["#006666","#3366FF"];
	var colorarr=["greystyle-standard greystyle-standard-whitetr","greystyle-standard greystyle-standardcoldwhitetr"];
	for ( var i = 0; i < data.length; i++) {
		one = data[i];
		if (!one) {
			continue;
		}
		cells.push(one['NCELL']);
		ht += "<tr class=\""+colorarr[i%2]+"\"><td>"
				+ (i + 1)
				+ "</td><td>"
				+ one['CELL']
				+ "</td><td>"
				+ one['BSIC']
				+ "</TD><TD>"
				+ one['ARFCN']
				+ "</TD><TD>"
				+ one['REPARFCN1']
				+ "</td>"
				+ "<td>"
				+ getValidValue(one['TOPSIX'], '', 5)
				+ "</td><td>"
				+ getValidValue(one['TOPTWO'], '', 5)
				+ "</td><TD>"
				+ getValidValue(one['TIMESABSS1'], '', 5)
				+ "</td><td>"
				+ getValidValue(one['TIMESALONE1'], '', 5)
				+ "</td><TD>"
				+ one['NCELL']
				+ "</td><td>"
				+ (one['DISTANCE1'] == '10000000000' ? '' : getValidValue(
						one['DISTANCE1'], '', 3)) + "</TD><TD>"
				+ getValidValue(one['NCELLS'], '') + "</TD><TD>"
				+ (one['DEFINED_NEIGHBOUR1'] == '0' ? 'NO' : 'YES')
				+ "</td></tr>";
	}
	$("#cellInfoTab").append(ht);

	// --------------------显示图表---------------------------//
//	displayChart(data, $("#ncsDataType").val());
	$("#ncsDataType").trigger("change");
}

/**
 * 显示数据
 * 
 * @param data
 * @param type
 *            类型：包括；six,two,abss,alone
 */
function displayChart(data, type) {

	if (!data || data.length == 0) {
		return;
	}
	var cells = new Array();
	//peng.jm 2015-1-26 修改 start
//	for ( var i = 0; i < data.length; i++) {
//		one = data[i];
//		if (!one) {
//			continue;
//		}
//		cells.push(one['NCELL']);
//	}
	//peng.jm 2015-1-26 修改 end
	
	// type对应的名称
	var title = "";
	if (type == 'six') {
		title = '六强';
	} else if (type == 'two') {
		title = '两强'
	} else if (type == 'cellrate') {
		title = '强于主小区'
	} else if (type == 'abss') {
		title = 'ABSS';
	} else if (type == 'alone') {
		title = 'ALONE';
	} else {
		return;
	}

	var ratioData = createRatioData(data, type);
	console.log(ratioData);
	
	//peng.jm 2015-1-26 加入 start
	//获取cell列作为图表x轴
	for ( var i = 0; i < ratioData.length; i++) {
		cells.push(ratioData[i]['ncell']);
	}
	//peng.jm 2015-1-26 加入 end
	
//	for(var i=0;i<ratioData.length;i++){
//		var one=ratioData[i];
//	for(var key in one){
//	console.log(key+":"+one[key]);
//	}
//	}
	var ratioRedData=new Array();
	var ratioYelData=new Array();
	for(var i=0;i<ratioData.length;i++){
		var one=ratioData[i];
//		console.log("ratioData[i]['color']:"+ratioData[i]['color']);
		if(ratioData[i]['color']=='red'){
//			ratioRedData[i]=one;
			ratioRedData.push(one);
//			console.log("ratioRedData["+i+"]"+ratioRedData[i]);
		}else{
//			ratioYelData[i]=one;
			ratioYelData.push(one);
//			console.log("ratioRedData["+i+"]"+ratioRedData[i]);
		}
	}
	// 比例
	var chart = $('#chartDiv').highcharts();
	
	//peng.jm 2015-3-18 编辑 start
	// 距离
//	var disData = new Array();
//	for ( var i = 0; i < data.length; i++) {
//		disData.push(data[i]['DISTANCE1'] == "10000000000" ? ""
//				: data[i]['DISTANCE1']);
//	}
	var disData = new Array();
	for ( var i = 0; i < ratioData.length; i++) {
		disData.push(ratioData[i]['dis']);
	}
	//peng.jm 2015-3-18 编辑 end

	if (chart) {
		chart.destroy();
		// console.log("重新生成chart");
	}
	var options = {
		chart : {
			zoomType : 'xy'
		},
		title : {
			text : '[' + selectCell + ']' + title + '比例',
			align:'left',
			x:500,
			y:10
			//style:{'padding-right':'60px','text-align':'right'}
		},
		xAxis : [ {
			id : 'ncellAxis',
			gridLineWidth : 1,
			categories : cells,
			labels : {
				rotation : 90
			}
		} ],
		yAxis : [ { // Primary yAxis
			id : 'ratioAxis',
			labels : {
				style : {
					color : '#89A54E'
				}
			},
			title : {
				text : title + '比例（%）'
			},

		}, { // Tertiary yAxis
			id : 'disAxis',
			gridLineWidth : 1,
			title : {
				text : '距离(km)',
				style : {
					color : '#AA4643'
				}
			},
			labels : {
				style : {
					color : '#AA4643'
				}
			},
			opposite : true
		} ],
		tooltip : {
			shared : false,
			formatter : function() {
				return this.x + "---" + this.y;
			}
		},
		legend : {
			layout : 'horizontal',
			align : 'left',
			x : 10,//20
			verticalAlign : 'top',
			y : -5,
			floating : true,
			backgroundColor : '#FFFFFF'
		},
//		series : [ {
//			id : 'ratioSeries',
//			name : '红色：已定义邻区     黄色：未定义邻区',
//			color : '#4572A7',
//			type : 'column',
//			yAxis : 'ratioAxis',
//			data : ratioData,
//			tooltip : {
//			// // valueSuffix : ' %',
//			// formatter : function() {
//			// return this.y + "bili";
//			// }
//			}
//
//		}
		series : [{
			id : 'ratioSeries',
			name : '红色：已定义邻区',
			color : '#FF0000',
			type : 'column',
			yAxis : 'ratioAxis',
			data : ratioRedData,
			tooltip : {
			// // valueSuffix : ' %',
			// formatter : function() {
			// return this.y + "bili";
			// }
			}
		},
		{
			id : 'ratioSeries',
			name : '黄色：未定义邻区',
			color : '#FFFF00',
			type : 'column',
			yAxis : 'ratioAxis',
			data : ratioYelData,
			tooltip : {
			// // valueSuffix : ' %',
			// formatter : function() {
			// return this.y + "bili";
			// }
			}

		}
		, {
			id : 'disSeries',
			name : '与目标小区的距离',
			color : '#89A54E',
			type : 'scatter',
			yAxis : 'disAxis',
			data : disData,
			tooltip : {
				valueSuffix : ' km'
			}
		} ]
	//			
	};
	chart = $('#chartDiv').highcharts(options);

}

/**
 * 显示ncs信息
 * 
 * @param ncs
 */
function showNcsInfo(ncs) {
	$("#ncsInfoTab").html("");
	var ht = "<tr><td colspan=2>测量信息</td></tr>";
	ht += "<TR><td class='menuTd'>BSC</td><td>" + selectBsc + "</td></TR>";
	ht += "<TR><td class='menuTd'>CREATE_TIME</td><td>" + ncs['MEA_TIME']
			+ "</td>";
	ht += "<TR><td class='menuTd'>RECORDCOUNT</td><td>" + getValidValue(ncs['RECORD_COUNT'])
			+ "</td>";
	ht += "<TR><td class='menuTd'>RID</td><td>" + getValidValue(ncs['RID']) + "</td>";
	ht += "<TR><td class='menuTd'>RELSS</td><td>" +getValidValue(ncs['RELSS']) + "</td>";
	ht += "<TR><td class='menuTd'>RELSS2</td><td>" + getValidValue(ncs['RELSS2']) + "</td>";
	ht += "<TR><td class='menuTd'>RELSS3</td><td>" + getValidValue(ncs['RELSS3'])+ "</td>";
	ht += "<TR><td class='menuTd'>RELSS4</td><td>" + getValidValue(ncs['RELSS4']) + "</td>";
	ht += "<TR><td class='menuTd'>RELSS5</td><td>" + getValidValue(ncs['RELSS5']) + "</td>";
	ht += "<TR><td class='menuTd'>NCELLTYPE</td><td>" + getValidValue(ncs['NCELLTYPE'])
			+ "</td>";
	ht += "<TR><td class='menuTd'>NUMFREQ</td><td>" + getValidValue(ncs['NUMFREQ']) + "</td>";
	ht += "<TR><td class='menuTd'>PERIODLEN</td><td>" + getValidValue(ncs['RECTIME'])
			+ "</td>";
	ht += "<TR><td class='menuTd'>TERMREASON</td><td>" + getValidValue(ncs['TERM_REASON'])
			+ "</td>";
	ht += "<TR><td class='menuTd'>RECTIME</td><td>" + getValidValue(ncs['RECTIME']) + "</td>";
	ht += "<TR><td class='menuTd'>ECNOABSS</td><td>" + getValidValue(ncs['ECNOABSS'])
			+ "</td>";
	ht += "<TR><td class='menuTd'>NUCELLTYPE</td><td>" + getValidValue(ncs['NUCELLTYPE'])
			+ "</td>";
	ht += "<TR><td class='menuTd'>TFDDMRR</td><td>" + getValidValue(ncs['TFDDMRR']) + "</td>";
	ht += "<TR><td class='menuTd'>NUMUMFI</td><td>" + getValidValue(ncs['NUMUMFI']) + "</td>";
	$("#ncsInfoTab").html(ht);
}

function initAreaCascade() {
	// 区域联动事件
	$("#provinceId1").change(function() {
		getSubAreas("provinceId1", "cityId1", "市");
	});
	$("#cityId1").change(function() {
		// 清除bsc列表
		$("#allBscCell").html("");
		// 重新加载bsc列表
		getAllBscCell($("#cityId1").val());
	});

}

/**
 * 获取包含有指定小区的ncs的信息
 * 
 * @param cell
 */
function getCellNcsList(cell,bsc,manufacturers) {
	// cell = 'S3ASDS1';
	selectCell = cell;
	// alert(cell);

	// 情况日期下拉框
	$("#ncsDate").html("");
	$("#ncsTime").html("");

	var cityId = $("#cityId1").val();
	showOperTips("loadingDataDiv", "loadContentId", "加载NCS数据中");
	$.ajax({
		url : 'searchNcsContainsCellForAjaxAction',
		data : {
			'cell' : selectCell,
			'bsc' : bsc,
			'cityId' : cityId,
			'manufacturers' : manufacturers
		},
		dataType : 'text',
		type : 'post',
		success : function(raw) {
			ncsDateToTime = new Object();
			ncsIdToDetail = new Object();
			ncsDates.splice(0, ncsDates.length);

			var data = eval("(" + raw + ")");
			var one;
			var ncsId;
			var manufacturers;
			var index, startdate, starthour;
			var times;
			var t;
			for ( var i = 0; i < data.length; i++) {
				one = data[i];
				if (!one) {
					continue;
				}
				manufacturers = one['MANUFACTURERS'];
				ncsId = one['NCS_ID'];
				startTime = one['MEA_TIME'];
				index = startTime.indexOf(' ');
				startdate = startTime.substring(0, index);
				starthour = startTime.substring(index + 1);

				// 记录
				ncsIdToDetail[ncsId] = one;
				//
				times = ncsDateToTime[startdate];
				if (!times) {
					times = new Array();
					ncsDateToTime[startdate] = times;

					ncsDates.push(startdate);
				}
				t = new Object();
				t['time'] = starthour;
				t['ncsId'] = ncsId;
				t['manufacturers'] = manufacturers;
				times.push(t);
			}

			// 填充日期和时间下来框
			if (ncsDates.length > 0) {
				var ds = "";
				for ( var j = 0; j < ncsDates.length; j++) {
					ds += "<option value='" + ncsDates[j] + "'>" + ncsDates[j]
							+ "</option>";
				}
				// alert(ds);
				$("#ncsDate").html(ds);

				// 取第一个日期的时间填充时间下来框
				var times = ncsDateToTime[ncsDates[0]];
				if (times) {
					var ts = "";
					for ( var j = 0; j < times.length; j++) {
						ts += "<option value='" + times[j]['ncsId'] +","+ times[j]['manufacturers'] + "'>"
								+ times[j]['time'] + "</option>"
					}
					// alert(ts);
					$("#ncsTime").html(ts);
				}

				// 触发一次获取小区在ncs里的测量信息的事件
				$("#ncsTime").trigger("change");
			} else {
				hideOperTips("loadingDataDiv");
				animateInAndOut("operInfo", 500, 500, 3000, "operTip",
						"该小区无测量数据！");
				hideOperTips("loadingDataDiv");
				return;
			}
		},
		complete : function() {
//			hideOperTips("loadingDataDiv");
		}
	})
}

/**
 * 获取区域下的所有的bsc列表
 * 
 * @param cityId
 */
function getAllBscCell(cityId) {
	//正在加载 <em class="loading_fb" id="tipcontentId"></em>,请稍侯...
	var cityname=$("#cityId1").find("option:selected").text();
	showOperTips("loadingDataDiv", "loadContentId", "加载BSC中");
	$.ajax({
		url : 'getAllBscCellsByCityIdInMapForAjaxAction',
		data : {
			'cityId' : cityId
		},
		type : 'post',
		dataType : 'text',
		success : function(raw) {
			var data = eval("("+raw+")");
			bscToCells = data;
			var bscList = new Array();
			for(var key in data) {
				bscList.push(key);
			}
			bscList.sort();
			//console.log(bscList);
			var bscHtml = "";

			for(var i=0; i<bscList.length; i++) {
				bscHtml += "<li><span class='bscCls' id='"+bscList[i]+"'>" + bscList[i] + "(小区数量：" 
					+ bscToCells[bscList[i]].length + ")</span><ul id='"+bscList[i]+"'></ul></li>";
			} 
			
			$("#allBscCell").html(bscHtml);
			$("#allBscCell").treeview({
				collapsed : true
			});

			$("span.bscCls").on("click", function() {
				var bsc = $(this).attr("id");
				var cells = bscToCells[bsc];
				//console.log(cells);
				var cellsHtml = "";
				var all;
				var part;
				for(var i=0; i<cells.length; i++) {
					all = cells[i]['LABEL'] + "(" + cells[i]['NAME'] + ")";
					part = all.length>12?all.substring(0,12)+"...":all;
					cellsHtml += "<li><span class='cellCls' data='" + cells[i]['LABEL'] + "'" 
							+ " bsc='" + bsc + "'" 
							+ " manufacturers='"+cells[i]['MANUFACTURERS']+"' " 
							+ " all='"+all+"' " 
							+ " part='"+part+"'>" 
							+ part +"</span></li>";
				}
				$("ul#"+bsc).html(cellsHtml);
			});
			//图标事件
			$("#allBscCell").find("div").bind("click", function(event) {
				var bsc = $(this).parent().find("span").attr("id");
				var cells = bscToCells[bsc];
				//console.log(cells);
				var cellsHtml = "";
				var all;
				var part;
				for(var i=0; i<cells.length; i++) {
					all = cells[i]['LABEL'] + "(" + cells[i]['NAME'] + ")";
					part = all.length>12?all.substring(0,12)+"...":all;
					cellsHtml += "<li><span class='cellCls' data='" + cells[i]['LABEL'] + "'" 
							+ " bsc='" + bsc + "'" 
							+ " manufacturers='"+cells[i]['MANUFACTURERS']+"' " 
							+ " all='"+all+"' " 
							+ " part='"+part+"'>" 
							+ part +"</span></li>";
				}
				$("ul#"+bsc).html(cellsHtml);
            });
		},
		complete:function(){
			hideOperTips("loadingDataDiv");
		}
	})
}

/**
 * 获取指定cell在指定ncs下的频点干扰情况
 * 
 * @param ncsId
 * @param cell
 */
function displayCellFreqInterferInfo(ncsId, cell) {
	var key = ncsId + "_" + cell;
	var data = cellFreqInterferQueue.get(key);
	if (data != null) {
		generateCellFreqChart(data);
		return;
	}
	$.ajax({
		url : 'getCellFreqInterferInNcsForAjaxAction',
		data : {
			'ncsId' : ncsId,
			'cell' : cell
		},
		type : 'post',
		dataType : 'text',
		success : function(raw) {
			var data = null;
			try {
				data = eval("(" + raw + ")");
				cellFreqInterferQueue.put(key, data);
				generateCellFreqChart(data);
			} catch (err) {
//				console.log(err);
				animateInAndOut("operInfo", 500, 500, 2000, "operTip",
						"该小区无频点干扰相关数据！");
				return;
			}
		}
	});
}

/**
 * 在某个基准日期的基础上，对时间进行加减
 * 正数为加，负数为减去
 * @param baseDate
 * @param day
 * @returns {Date}
 */
function addDays(baseDate,day){
    var tinoneday=24*60*60*1000;
    var nt=baseDate.getTime();
    var cht=day*tinoneday;
    var newD=new Date();
    newD.setTime(nt+cht);
    return newD;
}

/**
 * 对Date的扩展，将 Date 转化为指定格式的String   
 *月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，   
 *年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)   
 *例子：   
 *(new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423   
 *(new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18   
 * @title 
 * @param fmt
 * @returns
 * @author chao.xj
 * @date 2014-11-16下午3:58:43
 * @company 怡创科技
 * @version 1.2
 */
Date.prototype.Format = function(fmt)   
{  
  var o = {   
    "M+" : this.getMonth()+1,                 //月份   
    "d+" : this.getDate(),                    //日   
    "h+" : this.getHours(),                   //小时   
    "m+" : this.getMinutes(),                 //分   
    "s+" : this.getSeconds(),                 //秒   
    "q+" : Math.floor((this.getMonth()+3)/3), //季度   
    "S"  : this.getMilliseconds()             //毫秒   
  };   
  if(/(y+)/.test(fmt))   
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
  for(var k in o)   
    if(new RegExp("("+ k +")").test(fmt))   
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
  return fmt;   
}




/**
 * 生成小区频点干扰堆积图
 * 
 * @param data
 *            {"total":0.6247264944277963,"cells":[{"cell":"S3AAPS1","sum":0.6247264944277963,"inter":[{"ca":0.010941980898127313,"freq":9,"ci":0.008880869286700399},{"ca":0.042896985179619845,"freq":31,"ci":0.0016128838388979067},{"ca":0.011068270801158148,"freq":40,"ci":0.0},{"ca":0.010057491868610375,"freq":53,"ci":0.03822475558055719},{"ca":0.003072257452844344,"freq":58,"ci":0.2684975648742071},{"ca":0.0049726067907947934,"freq":72,"ci":0.007905314967750996},{"ca":0.027520680394367513,"freq":81,"ci":0.012238863850871353},{"ca":5.11520008248224E-4,"freq":85,"ci":0.0036333554304692982},{"ca":8.988315190252671E-5,"freq":1022,"ci":0.0},{"ca":0.01910121533113247,"freq":2,"ci":0.001698641567769288},{"ca":0.10116112291530142,"freq":5,"ci":0.011078622288589312},{"ca":0.024100552190800855,"freq":93,"ci":0.015461055759075616}]}]}
 */
function generateCellFreqChart(data) {
	if (data == null || data == undefined) {
		animateInAndOut("operInfo", 500, 500, 2000, "operTip", "该小区无频点干扰相关数据！");
		return;
	}

	var celllist = data['cells'];
	if (celllist == null || celllist == undefined || celllist.length == 0) {
		animateInAndOut("operInfo", 500, 500, 2000, "operTip", "该小区无频点干扰相关数据！");
		return;
	}
	var mycell = celllist[0];

	//
	var chart = $('#chartDiv').highcharts();
	if (chart) {
		chart.destroy();
	}

	// 频点数组
	var freqs = new Array();
	// ci数据
	var ciarr = new Array();
	// ca数据
	var caarr = new Array();

	var interinfo = mycell['inter'];
	if (interinfo == null || interinfo == undefined || interinfo.length == 0) {
		animateInAndOut("operInfo", 500, 500, 2000, "operTip", "该小区无频点干扰相关数据！");
		return;
	}
	var cione, caone;
	for ( var i = 0; i < interinfo.length; i++) {
//		cione = new Object();
//		cione['x'] = interinfo[i]['freq'];
//		cione['y'] = interinfo[i]['ci'];
		ciarr.push(interinfo[i]['ci']);

//		caone = new Object();
//		caone['x'] = interinfo[i]['freq'];
//		caone['y'] = interinfo[i]['ca'];
		caarr.push(interinfo[i]['ca']);

		freqs.push(interinfo[i]['freq']);
	}
	

	var options = {
		chart : {
			type : 'column',
			zoomType : 'xy'
		},
		title : {
			text : mycell['cell'] + "小区频点干扰情况"
		},
		xAxis : {
			categories : freqs
		},
		yAxis : {
			title : {
				text : '干扰系数'
			}
		},
		plotOptions : {
			column : {
				stacking : 'normal'
//				dataLabels : {
//					enabled : true,
//					color : (Highcharts.theme && Highcharts.theme.dataLabelsColor)
//							|| 'white'
//				}
			}
		},
		legend: {
            borderColor: '#CCC',
            borderWidth: 1,
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'top',
            x: -10,
            y: 100
        },
		series : [ {
			name : 'C/A',
			data : caarr,
			color : '#C0504D'
		},
		{
			name : 'C/I',
			data : ciarr,
			color : '#4F81BD'
		}]

	};

	chart = $('#chartDiv').highcharts(options);

}
