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

var Rxlev = [ 'CELL_NAME'];

//新的开始
//var Rxlev=new object();
var Rxlev_xAxis=['-110','-105','-100','-95','-90','-85','-80','-75','-70','-65','-60','-55'];
//var RxQual=new object();
var RxQual_xAxis=['0','1','2','3','4','5','6','7'];
//var POWER=new object();
var POWER_xAxis=['0','1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30','31'];
//var PATHLOSS=new object();
var PATHLOSS_xAxis=['35','40','45','50','55','60','65','70','75','80','85','90','95','100','105','110','115','120','125','130','135','140','145','150','155','160','165','170'];
//var PLDIFF=new object();
var PLDIFF_xAxis=['-25','-20','-15','-10','-5','0','5','10','15','20','25','30','35','40','45'];
//var TA=new object();
var TA_xAxis=['0','1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30'];
//存储缓存小区数据
var cellIndexObj=new Object();
$(document).ready(
		function() {
			//tab选项卡
			tab("div_tab", "li", "onclick");//项目服务范围类别切换
//			console.log($(document).height());
			var docheight=$(document).height();
			 $("#chartDiv").css({
		           height: docheight-175
		        });
			initAreaCascade();

//			getAllBscCell($("#cityId1").val());
			getAllLteCell($("#cityId1").val());

			$(".cellCls").live("click", function() {
				getCellMrList($(this).attr("data"),'chartDiv');
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
		

			// 模糊查询
			$("#inputCell").keyup(function() {
//				matchCell();
				searchCell();
			});

			// set highchart
//			sethighchart();
			//新的开始
//			generateCellIndexChart(cellIndexObj,"chartDiv","line","Rxlev",chartType);
			defaultCellIndexChart(cellIndexObj,"chartDiv",true);
			$("#mrrInfoTab tr:first td").text($("#mrDataType").find("option:selected").text()+"测量信息"); 
			//全屏展现
			$("#fullScreen").click(function(){
				//click全屏
				clickFullScreen();
				});
			$("#fullScreenChart").dblclick(function(){
				//双击全屏关闭
				$("#fullScreenChart").css('display','none');
			});
			$("#chartDiv").dblclick(function(){
				//双击图表全屏
				clickFullScreen();
			});
			//日期选择器
			//timeFormat: "HH:mm:ss",datetimepicker
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
			$("#mrMeaBegDate").datetimepicker(
					{
						dateFormat : "yy-mm-dd",
						timeFormat: "",
						defaultDate : "-2",
						changeMonth : true,
						numberOfMonths : 1,
						showButtonPanel:true,//是否显示按钮面板  
						closeText: '关闭',
						currentText:'加载数据',
						gotoCurrent:true,
						maxDate:new Date() ,
						timeText:'',
						//buttonClose:false,
						onClose : function(selectedDate,e) {
							/*$("#mrMeaEndDate").datetimepicker("option", "minDate",
									selectedDate);*/
							if($("#mrMeaEndDate").datetimepicker("getDate")>addDays(new Date($.trim(selectedDate)),+30)){
								$("#mrMeaEndDate").datetimepicker("option", "minDate",
										addDays(new Date($.trim(selectedDate)),+30));
							}
							if($("#mrMeaEndDate").datetimepicker("getDate")<new Date($.trim(selectedDate))){
								$("#mrMeaEndDate").datetimepicker("option", "minDate",
										addDays(new Date($.trim(selectedDate)),+30));
							}
						},
						onSelect : function(selectedDate) {
							if($("#mrMeaEndDate").datetimepicker("getDate")>addDays(new Date($.trim(selectedDate)),+30)){
								$("#mrMeaEndDate").datetimepicker("setDate",addDays(new Date($.trim(selectedDate)),+30));
							}
							if($("#mrMeaEndDate").datetimepicker("getDate")<new Date($.trim(selectedDate))){
								$("#mrMeaEndDate").datetimepicker("setDate",addDays(new Date($.trim(selectedDate)),+30));
							}
						}
					});
			 
//			$("#mrMeaBegDate").datetimepicker("setDate",-30);// 减去2天
//			console.log($('#mrMeaBegDate').datepicker('option', 'buttonText'));
//			$('#mrMeaBegDate').datepicker('option', 'currentText','加载');
			$("#mrMeaBegDate").datetimepicker("setDate",addDays(new Date(),-30).Format("yyyy-MM-dd"));// 减去30天
			$("#mrMeaEndDate").datetimepicker(
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
							/*$("#mrMeaBegDate").datetimepicker("option", "maxDate",
									selectedDate);*/
							if($("#mrMeaBegDate").datetimepicker("getDate")<addDays(new Date($.trim(selectedDate)),-30)){
								$("#mrMeaBegDate").datetimepicker("option", "maxDate",
										addDays(new Date($.trim(selectedDate)),-30));
								}
							if($("#mrMeaBegDate").datetimepicker("getDate")>new Date($.trim(selectedDate))){
								$("#mrMeaBegDate").datetimepicker("option", "maxDate",
										addDays(new Date($.trim(selectedDate)),-30));
								}
						},
						onSelect : function(selectedDate) {
							if($("#mrMeaBegDate").datetimepicker("getDate")<addDays(new Date($.trim(selectedDate)),-30)){
							$("#mrMeaBegDate").datetimepicker("setDate",addDays(new Date($.trim(selectedDate)),-30));
							}
							if($("#mrMeaBegDate").datetimepicker("getDate")>new Date($.trim(selectedDate))){
								$("#mrMeaBegDate").datetimepicker("setDate",addDays(new Date($.trim(selectedDate)),-30));
								}
						}
					});
			$("#mrMeaEndDate").datetimepicker("setDate",(new Date()));
			//切换展示方式
			$("#mrDisMode").change(function(){
//				$("#chartDiv").empty();//清空
//				$("#chartDiv").attr("style","padding:10px;width:800px;height: 572px");
				var disMode=$(this).find("option:selected").text().trim();
				var dateType=$("#mrDataType").find("option:selected").text().trim();
				var chartType=$("#mrChartType").find("option:selected").val();
				if("折线图"===disMode){
					if("Rxlev"===dateType){
						generateCellIndexChart(cellIndexObj,"chartDiv","line","Rxlev",chartType);
					}else if("RxQual"===dateType){
						generateCellIndexChart(cellIndexObj,"chartDiv","line","RxQual",chartType);
					}else if("POWER"===dateType){
						generateCellIndexChart(cellIndexObj,"chartDiv","line","POWER",chartType);
					}else if("PATHLOSS"===dateType){
						generateCellIndexChart(cellIndexObj,"chartDiv","line","PATHLOSS",chartType);
					}else if("PLDIFF"===dateType){
						generateCellIndexChart(cellIndexObj,"chartDiv","line","PLDIFF",chartType);
					}else if("TA"===dateType){
						generateCellIndexChart(cellIndexObj,"chartDiv","line","TA",chartType);
					}
				}else if("柱状图"===disMode){
					if("Rxlev"===dateType){
						generateCellIndexChart(cellIndexObj,"chartDiv","bar","Rxlev",chartType);
					}else if("RxQual"===dateType){
						generateCellIndexChart(cellIndexObj,"chartDiv","bar","RxQual",chartType);
					}else if("POWER"===dateType){
						generateCellIndexChart(cellIndexObj,"chartDiv","bar","POWER",chartType);
					}else if("PATHLOSS"===dateType){
						generateCellIndexChart(cellIndexObj,"chartDiv","bar","PATHLOSS",chartType);
					}else if("PLDIFF"===dateType){
						generateCellIndexChart(cellIndexObj,"chartDiv","bar","PLDIFF",chartType);
					}else if("TA"===dateType){
						generateCellIndexChart(cellIndexObj,"chartDiv","bar","TA",chartType);
					}
				}
				
			});
			//切换数据类型
			$("#mrDataType").change(function(){
				getCellMrList(selectCell,'chartDiv');				
			});
			//选取不同信道组号
			$("#mrrChgr").change(function(){
				getCellMrList(selectCell,'chartDiv');
			});
			//结束日期改变触发
			/*$("#mrMeaEndDate").change(function(){
				getCellMrList(selectCell);
			});*/
			//选择日期重新加载
			$(document).delegate('.ui-datepicker-current', 'click',
					function() {
						getCellMrList(selectCell,'chartDiv');
					});
			//图表改变触发
			$("#mrChartType").change(function(){
				getCellMrList(selectCell,'chartDiv');
			});
			
		});

/**
 * 输入小区名，模糊查询小区，把符合条件的小区列出来， 其他的隐藏
 */
function matchCell() {
	var cell = $.trim($("#inputCell").val());
	var count=0;
	if (cell == '') {
		$("span.cellCls").each(function(i, ele) {
			if ($(ele).closest("li").css("display") == 'none') {
				$(ele).closest("li").css("display", '');
			}
		});
		$("span.bscCls").each(function(i, ele) {
			if ($(ele).closest("li").css("display") == 'none') {
				$(ele).closest("li").css("display", '');
			}
		});
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
			if(cell != ''){
				$("span.bscCls").each(function(i, ele) {
					var aa=$(ele).text();//HZSMNB8（小区数量:85）
					var bscstr=aa.substring(0,aa.indexOf("("));//HZSMNB8
//					console.log(aa.substring(0,aa.indexOf("（")));
//					console.log(aa);
//					console.log(aa.indexOf("（"));
//					console.log(aa.substring(aa.indexOf("（")));
//					console.log($("span[bsc='"+bscstr+"']").attr("bsc"));
					$("span[bsc='"+bscstr+"']").each(function(i, ele) {
//							console.log("i:"+i);
							if ($(ele).closest("li").css("display") != 'none') {
									count++;
//									console.log("count:"+count);
								}
							
					})
					$(ele).text(bscstr+"(小区数量:"+count+")");
					if(count==0){
						$(ele).closest("li").css("display", 'none');
					}else{
						$(ele).closest("li").css("display", '');
					}
					count=0;
				});
			}
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
//		getAllBscCell($("#cityId1").val());
		getAllLteCell($("#cityId1").val());
	});

}

/**
 * 获取包含有指定小区的ncs的信息
 * 
 * @param cell
 *//*
function getCellNcsList(cell) {
	// cell = 'S3ASDS1';
	selectCell = cell;

	// alert(cell);

	// 情况日期下拉框
	$("#ncsDate").html("");
	$("#ncsTime").html("");

	var cityId = $("#cityId1").val();

	$.ajax({
		url : 'searchNcsContainsCellForAjaxAction',
		data : {
			'cell' : cell,
			'cityId' : cityId
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
			var index, startdate, starthour;
			var times;
			var t;
			for ( var i = 0; i < data.length; i++) {
				one = data[i];
				if (!one) {
					continue;
				}
				ncsId = one['RNO_NCS_DESC_ID'];
				startTime = one['START_TIME'];
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
				var times = ncsDateToTime[startdate];
				if (times) {
					var ts = "";
					for ( var j = 0; j < times.length; j++) {
						ts += "<option value='" + times[j]['ncsId'] + "'>"
								+ times[j]['time'] + "</option>"
					}
					// alert(ts);
					$("#ncsTime").html(ts);
				}

				// 触发一次获取小区在ncs里的测量信息的事件
				$("#ncsTime").trigger("change");
			} else {
				animateInAndOut("operInfo", 500, 500, 1000, "operTip",
						"该小区无测量数据！");
				return;
			}
		}
	})
}*/

/**
 * 获取区域下的所有的bsc列表
 * 
 * @param cityId
 */
/*function getAllBscCell(cityId) {
	//正在加载 <em class="loading_fb" id="tipcontentId"></em>,请稍侯...
	var cityname=$("#cityId1").find("option:selected").text();
	var str="正在加载"+cityname+"小区数据 <em class=\"loading_fb\" id=\"tipcontentId\"></em>,请稍侯...";
	$(".loading").html(str);
	$(".loading_cover").css("display","block");
	//getAllBscCellsInCityForAjaxAction
	$.ajax({
		url : 'getAllBscCellsInCityForAjaxAction',
		data : {
			'cityId' : cityId
		},
		type : 'post',
		dataType : 'text',
		success : function(raw) {
			$("#allBscCell").html(raw);
			$("#allBscCell").treeview({
				collapsed : true
			});
			$("span.cellCls").each(function(i, ele) {
			
			var str=$(ele).text();
			$(ele).attr("all",str) ;
			$(ele).attr("part",str.length>14?str.substring(0,14)+"...":str) ;
			$(ele).text($(ele).attr("part"));
			});
		},
		complete:function(){
		$(".loading_cover").css("display","none");
		}
	})
}*/
/**
 * 获取区域下的所有的bsc列表
 */
/*function getAllBscCell(cityId) {
	//正在加载 <em class="loading_fb" id="tipcontentId"></em>,请稍侯...
	var cityname=$("#cityId1").find("option:selected").text();
	var str="正在加载"+cityname+"小区数据 <em class=\"loading_fb\" id=\"tipcontentId\"></em>,请稍侯...";
	$(".loading").html(str);
	$(".loading_cover").css("display","block");
	showOperTips("loadingDataDiv", "tipcontentId", cityname+"小区数据");
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
			$(".loading_cover").css("display","none");
		}
	})
}*/

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
 * 
 * @title 生成动态小区指标图表
 * @param data
 * @param chartDiv 装载图表容器
 * @param disMode 展现方式
 * @param dataType 数据类型
 * @param chartType 图表类型
 * @author chao.xj
 * @date 2014-11-19下午3:37:55
 * @company 怡创科技
 * @version 1.2
 */
function generateCellIndexChart(data,chartDiv,disMode,dataType,chartType) {
	if (data == null || data == undefined) {
		animateInAndOut("operInfo", 500, 500, 2000, "operTip", "无相关KPI数据！");
		return;
	}
	if(dataType=="覆盖率"){
		dataType = "RSRP_P";
	}
//	console.log(dataType+"---data:"+data['CELL_NAME']);
	var rsrp,rsrq;
	var rsrp_less110_num = 0,rsrp_between110_95_num = 0,rsrp_between95_80_num = 0,rsrp_more80_num = 0;
//	var rsrq_less8_num = 0,rsrq_between8_15_num = 0,rsrq_more15_num = 0;
	//0<         0<=x<9     9<=x<19   19<=x<29 29<=x<33  x>=33
	var rsrq_less0_num = 0,rsrq_between0_9_num = 0,rsrq_between9_19_num = 0,rsrq_between19_29_num = 0,rsrq_between29_33_num = 0,rsrq_more33_num = 0
	var rsrpp_more110_num =0,rsrpp_more95_num =0,rsrpp_more85_num =0;
	var total_sample_num = data.length;//采样点数量
	var cellId,cellName;
	//X=<-110dBm，-110dBm=<X<-95，-95dBm=<X<-80dBm，X>=-80dBm（横轴）
//	console.log("rsrp_less110_num="+rsrp_less110_num+"--rsrp_between110_95_num="+rsrp_between110_95_num+"--rsrp_between95_80_num="+rsrp_between95_80_num+"--rsrp_more80_num="+rsrp_more80_num+"--rsrq_less8_num="+rsrq_less8_num+"--rsrq_between8_15_num="+rsrq_between8_15_num+"--rsrq_more15_num="+rsrq_more15_num);
	var dom=document.getElementById(chartDiv);
//	dom=$("#"+chartDiv )[0];从jquery至dom对象
	var yData=new Array();//上行有序数组
//	if(dataType.substring(0,dataType.lastIndexOf("_"))=='Rxlev'){
	var boundGap=false;
	var cell;
	var title="";//图表子标题
	var serieNameUL="";//上行系统名
	var xAxis_name="";//X轴名称
	var axis =new Array();
			if(data!=null){
				axis.length=0;
				if(dataType=='RSRP'){
					for ( var i = 0; i < data.length; i++) {
						rsrp = data[i]['CELL_RSRP'];
						rsrq = data[i]['CELL_RSRQ'];
						if(i ==0){
							cellId = data[i]['CELL_ID'];
							cellName = data[i]['CELL_NAME'];
						}
						//RSRP
						if(Number(rsrp)<-110){
							rsrp_less110_num++;
						}else if(-110<=Number(rsrp) && Number(rsrp)<-95){
							rsrp_between110_95_num++;		
						}else if(-95<=Number(rsrp) && Number(rsrp)<-80){
							rsrp_between95_80_num++;
						}else if(Number(rsrp)>=-80){
							rsrp_more80_num++;
						}
					}
					cell=cellId+"("+cellName+")";
					
					title=cell+"信号接收功率RSRP分布图";
					boundGap=true;//与y轴有间隔
					serieNameUL="RSRP指标";
					xAxis_name="电平值(dBm)";
					axis.push("X<-110");
					axis.push("-110<=X<-95");
					axis.push("-95<=X<-80");
					axis.push("X>=-80");
					
					yData.push(rsrp_less110_num);
					yData.push(rsrp_between110_95_num);
					yData.push(rsrp_between95_80_num);
					yData.push(rsrp_more80_num);
				}else if(dataType=='RSRQ'){
					//X轴为：x<-19.5，-19.5<=x<-15，-15<=x<-10，-10<=x<-5，-5<=x<-3，x>=-3
							//0         0<=x<9     9<=x<19   19<=x<29 29<=x<33  x>=33
					//rsrq_less0_num = 0,rsrq_between0_9_num = 0,rsrq_between9_19_num = 0,rsrq_between19_29_num = 0,rsrq_between29_33_num = 0,rsrq_more33_num = 0
					for ( var i = 0; i < data.length; i++) {
						rsrp = data[i]['CELL_RSRP'];
						rsrq = data[i]['CELL_RSRQ'];
						if(i ==0){
							cellId = data[i]['CELL_ID'];
							cellName = data[i]['CELL_NAME'];
						}
						//RSRQ
						if(Number(rsrq)<0){
							rsrq_less0_num++;
						}else if(0<=Number(rsrq) && Number(rsrq)<9){
							rsrq_between0_9_num++;		
						}else if(9<=Number(rsrq) && Number(rsrq)<19){
							rsrq_between9_19_num++;
						}else if(19<=Number(rsrq) && Number(rsrq)<29){
							rsrq_between19_29_num++;
						}else if(29<=Number(rsrq) && Number(rsrq)<33){
							rsrq_between29_33_num++;
						}else if(Number(rsrq)>=33){
							rsrq_more33_num++;
						}
					}
					cell=cellId+"("+cellName+")";
					
					title=cell+"信号接收功率RSRQ分布图";
					serieNameUL="RSRQ指标";
					xAxis_name="电平值(dB)";
					boundGap=true;
					axis.push("x<-19.5");
					axis.push("-19.5<=x<-15");
					axis.push("-15<=x<-10");
					axis.push("-10<=x<-5");
					axis.push("-5<=x<-3");
					axis.push("x>=-3");
					
					yData.push(rsrq_less0_num);
					yData.push(rsrq_between0_9_num);
					yData.push(rsrq_between9_19_num);
					yData.push(rsrq_between19_29_num);
					yData.push(rsrq_between29_33_num);
					yData.push(rsrq_more33_num);
				}else if(dataType=='RSRP_P'){
					for ( var i = 0; i < data.length; i++) {
						rsrp = data[i]['CELL_RSRP'];
						rsrq = data[i]['CELL_RSRQ'];
						if(i ==0){
							cellId = data[i]['CELL_ID'];
							cellName = data[i]['CELL_NAME'];
						}
						//RSRP_P
						if(Number(rsrp)>-110){
							rsrpp_more110_num++;
						}
						if(Number(rsrp)>-95){
							rsrpp_more95_num++;
						}
						if(Number(rsrp)>-85){
							rsrpp_more85_num++;
						}
					}
					cell=cellId+"("+cellName+")";
					
					title=cell+"信号接收功率覆盖率分布图";
					serieNameUL="覆盖率";
					xAxis_name="电平值(dBm)";
					boundGap=true;
					axis.push("X>-110");
					axis.push("X>-95");
					axis.push("X>-85");
					//百分比
					yData.push(Number((rsrpp_more110_num*100/total_sample_num).toFixed(2)));
					yData.push(Number((rsrpp_more95_num*100/total_sample_num).toFixed(2)));
					yData.push(Number((rsrpp_more85_num*100/total_sample_num).toFixed(2)));
				}
				//填充测量信息表
				$("#mrrInfoTab tr").eq(0).nextAll().remove();
				$("#mrrInfoTab tr:first td").text((dataType=='RSRP_P'?'覆盖率':dataType)+"测量信息"); 
				$("#mrrInfoTab tr:not(:first)").remove();  
				$("#mrrInfoTab").append("<TR><td class='menuTd'>CELL_NAME</td><td>"+cell+"</td></TR>");
				for ( var i = 0; i < axis.length; i++) {
					if(dataType=='RSRP_P'){
						$("#mrrInfoTab").append("<TR><td class='menuTd'>"+axis[i]+"</td><td>"+yData[i]+"%</td></TR>");
					}else{
						$("#mrrInfoTab").append("<TR><td class='menuTd'>"+axis[i]+"</td><td>"+yData[i]+"[占比:"+(yData[i]*100/total_sample_num).toFixed(2)+"%]</td></TR>");
					}
				}
				
			}
//	}
	require(
	        [
	            'echarts',
	            'echarts/chart/line',   // 按需加载所需图表，如需动态类型切换功能，别忘了同时加载相应图表
	            'echarts/chart/bar'
	        ],
	        function (ec) {
	            var myChart = ec.init(dom);
	            
	            myChart.showLoading({
	                text : "图表数据正在努力加载..."
	            });
	            //信道组号0的接收电平分布图
	            var option = {
	            		title : {
	            	        text: (dataType=='RSRP_P'?'覆盖率':dataType)+'指标',
	            	        subtext: title,
	            			x: "center", //标题水平方向位置
	            			subtextStyle:{color: '#4A4AFF'} 
	            	    },
	            	    tooltip : {
	            	        trigger: 'axis'
	            	    },
	            	   /* legend: {
	            	    	orient:'horizontal',
	            	        data:[serieNameUL] ,
	            	        y	: 'top',
	            	        x 	: 'left',
	            	    },*/
	            	    toolbox: {
	            	        show : true,
	            	        feature : {
	            	            mark : {show: (chartDiv=="fullScreenChart"?false:true)},
	            	            dataView : {show: (chartDiv=="fullScreenChart"?false:true), readOnly: false},
	            	            magicType : {show: (chartDiv=="fullScreenChart"?false:true), type: ['line', 'bar']},
	            	            restore : {show: (chartDiv=="fullScreenChart"?false:true)},
	            	            saveAsImage : {show: (chartDiv=="fullScreenChart"?false:true)},
	            	            myCloseTool : {
	            	                show : (chartDiv=="fullScreenChart"?true:false),
	            	                title : '关闭',
	            	                icon : 'image://../rno/jslib/echarts/close1.png',
	            	                onclick : function (){
	            	                	$("#fullScreenChart").css('display','none');
	            	                }
	            	            },
	            	            myFullTool : {
	            	                show : (chartDiv=="fullScreenChart"?false:true),
	            	                title : '全屏',
	            	                icon : 'image://../rno/jslib/echarts/full1.png',
	            	                onclick : function (){
	            	                	clickFullScreen();
	            	                }
	            	            }
	            	        }
	            	    },
	            	    calculable : true,
	            	    xAxis : [
	            	        {
	            	        	name:xAxis_name,
	            	            type : 'category',
	            	            boundaryGap : boundGap,
	            	            data : axis,
	            	            splitLine:{lineStyle:{type: 'dashed'}},
	            	            axisLine:{lineStyle:{color: '#223434'}},
	            	            axisTick:{show:true,length:5}
	            	        }
	            	    ],
	            	    yAxis : [
	            	        {
	            	        	name:(dataType=='RSRP_P'?'百分比(采样点总数'+total_sample_num+'个)':('采样点个数('+total_sample_num+'个)')),
	            	            type : 'value',
	            	            axisLabel : {
	            	                formatter: (dataType=='RSRP_P'?'{value}%':'{value}')
	            	            },
	            	            splitLine:{lineStyle:{type: 'dashed'}},
	            	            axisLine:{lineStyle:{color: '#223434'}}
	            	        }
	            	    ],
	            	    //112,56, 233, 343, 454, 89, 343, 123, 66, 123
	            	    series : [
	            	        {
	            	            name:serieNameUL,
	            	            type:disMode,
	            	            data:yData,
	            	            markPoint : {
	            	                data : [
	            	                   /* {type : 'max', name: '最大值'},
	            	                    {type : 'min', name: '最小值'}*/
	            	                ]
	            	            },
	            	            markLine : {
	            	                data : [
	            	                    {type : 'average', name: '平均值'}
	            	                ]
	            	            },
	            	            //"#FF0000"
	            	            itemStyle :{normal :{color:function (value){return "#"+("00000"+((Math.random()*16777215+0.5)>>0).toString(16)).slice(-6); },label:{show:true,position:'top',formatter: (dataType=='RSRP_P'?'{c}%':'{c}')}}}
	            	        },
	            	        //45, 23, 49, 33, 43, 44, 38, 23, 46, 13
	            	        
	            	    ]
	            }
	            myChart.hideLoading();
	            myChart.setOption(option);
	            if(chartDiv=="fullScreenChart"){
	            	$("#fullScreenChart").css("background","white");
	            }
	        }
	    );

}

/**
 * 
 * @title 从字符串中提取数字
 * @param text
 * @returns
 * @author chao.xj
 * @date 2014-11-18下午12:06:27
 * @company 怡创科技
 * @version 1.2
 */
function getNum(text){
	var value = text.replace(/[^0-9]/ig,""); 
	return value;
	}
/*function test(arrDemo){
	
	arrDemo.sort(function(a,b){return a>b?1:-1});//从小到大排序
}*/
/**
 * 生成缺省小区指标图表
 */
function defaultCellIndexChart(data,chartDiv,isInit) {
	if(isInit){
		
		if (data == null || data == undefined || data.length==0) {
			animateInAndOut("operInfo", 500, 500, 2000, "operTip", "请选择小区数据！");
			return;
		}
	}else{
		if (data == null || data == undefined || data.length==0) {
			animateInAndOut("operInfo", 500, 500, 2000, "operTip", "无相关KPI数据！");
		}
	}
	$("#mrrInfoTab tr:not(:first)").remove();  
	for ( var i = 0; i < Rxlev.length; i++) {
		$("#mrrInfoTab").append("<TR><td class='menuTd'>"+Rxlev[i]+"</td><td></td></TR>");
	}
	var dom=document.getElementById(chartDiv);
	require(
	        [
	            'echarts',
	            'echarts/chart/line',   // 按需加载所需图表，如需动态类型切换功能，别忘了同时加载相应图表
	            'echarts/chart/bar'
	        ],
	        function (ec) {
	            var myChart = ec.init(dom);
	            
	            myChart.showLoading({
	                text : "图表数据正在努力加载..."
	            });
	            //信道组号0的接收电平分布图
	            var option = {
	            		title : {
	            	        text: '标题',
	            	        subtext: '请在左侧选择小区数据',
	            			x: "center", //标题水平方向位置
	            			subtextStyle:{color: '#FF0000'} 
	            	    },
	            	    tooltip : {
	            	        trigger: 'axis'
	            	    },
	            	    toolbox: {
	            	        show : true,
	            	        feature : {
	            	            mark : {show: true},
	            	            dataView : {show: true, readOnly: false},
	            	            magicType : {show: true, type: ['line', 'bar']},
	            	            restore : {show: true},
	            	            saveAsImage : {show: true}
	            	        }
	            	    },
	            	    calculable : true,
	            	    xAxis : [
	            	        {
	            	        	name:'X轴',
	            	            type : 'category',
	            	            boundaryGap : false,
	            	            data : ['00:00','05:00','12:00','18:00','00:00'],
	            	            splitLine:{lineStyle:{type: 'dashed'}},
	            	            axisLine:{lineStyle:{color: '#223434'}},
	            	            axisTick:{show:true,length:5}
	            	        }
	            	    ],
	            	    yAxis : [
	            	        {
	            	        	name:'Y轴',
	            	            type : 'category',
	            	            data : ['0.0','0.2','0.4','0.6','0.8','1.0','1.2'],
	            	            axisLabel : {
	            	                formatter: '{value}'
	            	            },
	            	            splitLine:{lineStyle:{type: 'dashed'}},
	            	            axisLine:{lineStyle:{color: '#223434'}}
	            	        }
	            	    ],
	            	    //112,56, 233, 343, 454, 89, 343, 123, 66, 123
	            	    series : [
	            	        {
	            	            name:'',
	            	            type:'line',
	            	            data:[],
	            	            markPoint : {
	            	                data : [
	            	                    {type : 'max', name: '最大值'},
	            	                    {type : 'min', name: '最小值'}
	            	                ]
	            	            },
	            	            markLine : {
	            	                data : [
	            	                    {type : 'average', name: '平均值'}
	            	                ]
	            	            },
	            	            itemStyle :{normal :{color:"#FF0000"}}
	            	        },
	            	        //45, 23, 49, 33, 43, 44, 38, 23, 46, 13
	            	        {
	            	            name:'',
	            	            type:'line',
	            	            data:[],
	            	            markPoint : {
	            	                data : [
	            	                    {name : '周最低', value : -2, xAxis: 1, yAxis: -1.5},
	            	                    {type : 'max', name: '最大值'},
	            	                    {type : 'min', name: '最小值'}
	            	                ]
	            	            },
	            	            markLine : {
	            	                data : [
	            	                    {type : 'average', name : '平均值'}
	            	                ]
	            	            },
	            	            itemStyle :{normal :{color:"#007300"}}
	            	        }
	            	    ]
	            }
	            myChart.hideLoading();
	            myChart.setOption(option);
	        }
	    );

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
 * 单击全屏幕展现
 */
function clickFullScreen(){
	if(selectCell==""){
		animateInAndOut("operInfo", 500, 500, 2000, "operTip", "请选择小区数据！");
		return;
	}
	$("#fullScreenChart").css('display','block');
	/*$("#fullScreenChart").attr('style','background-color: #cfeace;height:100%;border: 1px solid #00457b;padding: 8px;font-size: .85em; ');
	$("html,body").css('height','97%').css('margin','0px');*/
       /*属性*/
	    $("#fullScreenChart").css({
		"position" : "absolute",
		"text-align" : "center",
		"top" : "0px",
		"left" : "0px",
		"right" : "0px",
		"bottom" : "0px",
		"background" : "white",
		"visibility" : "visible",
		"z-index":"999"	
			/*,
		"filter" : "Alpha(opacity=90)",
		"-moz-opacity":"0.4"*/
	});
       /*高为屏幕的高*/
       $("#fullScreenChart").css({
           height: function () {
           return $(document).height();
       },
           width:"100%"
        });
     
	getCellMrList(selectCell,'fullScreenChart');	
//	$("#fullScreenChart").css("background","white");
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
 * 获取区域下的所有的LTe小区列表
 */
function getAllLteCell(cityId) {
	//正在加载 <em class="loading_fb" id="tipcontentId"></em>,请稍侯...
	var cityname=$("#cityId1").find("option:selected").text();
	/*var str="正在加载"+cityname+"小区数据 <em class=\"loading_fb\" id=\"tipcontentId\"></em>,请稍侯...";
	$(".loading").html(str);
	$(".loading_cover").css("display","block");*/
	showOperTips("loadingDataDiv", "tipcontentId", cityname+"小区数据");
	$.ajax({
		url : 'getAllLteCellsByCityIdInMapForAjaxAction',
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
			$(".loading_cover").css("display","none");
		}
	})
}
/**
 * 
 * @title 获取包含有指定小区的mr的信息
 * @param cell
 * @param chartDiv
 * @author chao.xj
 * @date 2015-9-15上午10:12:09
 * @company 怡创科技
 * @version 1.2
 */
function getCellMrList(cell,chartDiv) {
	// cell = 'S3ASDS1';
	if(cell==null || cell==""){
		animateInAndOut("operInfo", 500, 500, 2000, "operTip", "请先选择小区数据！");
		return;
	}
	showOperTips("loadingDataDiv", "tipcontentId", "小区指标数据");
	selectCell = cell;
	// alert(cell);

	var cityId = $("#cityId1").find("option:selected").val();
	var mrMeaBegDate=$("#mrMeaBegDate").val();
	var mrMeaEndDate=$("#mrMeaEndDate").val();
	var mrDataType=$("#mrDataType").find("option:selected").text();
//	var mrrChgr=$("#mrrChgr").find("option:selected").text();
	var mrChartType=$("#mrChartType").find("option:selected").val();
	var mrDisMode=$("#mrDisMode").find("option:selected").text();
	$.ajax({
		url : 'searchLteKpiCellIndexForAjaxAction',
		data : {
			'attachParams.cell' : cell,
			'attachParams.cityId' : cityId,
			'attachParams.meaBegTime' : mrMeaBegDate,
			'attachParams.meaEndTime' : mrMeaEndDate,
			'attachParams.dataType' : mrDataType,
			'attachParams.chartType' : mrChartType,
			'attachParams.disMode' : mrDisMode,
//			'attachParams.chgr' : mrrChgr
		},
		dataType : 'text',
		type : 'post',
		success : function(raw) {
//			console.log("raw:"+raw);
			var data = eval("(" + raw + ")");
			data =data['data'];
			var obj;
//			obj=data[0];
//			console.log("数据集的长度==>>>"+data.length);
			obj=data;
			cellIndexObj=obj;
			if (obj == null || obj == undefined || data.length==0) {
				defaultCellIndexChart(obj,"chartDiv",false);
			}else{
				
				if("折线图"===mrDisMode){
					generateCellIndexChart(obj,chartDiv,"line",mrDataType,mrChartType);
				}else if("柱状图"===mrDisMode){
					generateCellIndexChart(obj,chartDiv,"bar",mrDataType,mrChartType);
				}
				showOperTips("loadingDataDiv", "tipcontentId", "数据成功展现");
			}
		},
		complete:function(){
			hideOperTips("loadingDataDiv");
			}
	})
}