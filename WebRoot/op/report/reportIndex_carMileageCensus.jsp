<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>车辆里程统计报表</title>
		<link rel="stylesheet" type="text/css" href="../../css/base.css" />
		<link rel="stylesheet" type="text/css" href="../../css/public.css" />
		<link rel="stylesheet" type="text/css" href="../css/leftMenu.css">

			
</script>
			<link rel="stylesheet" type="text/css" href="css/statements.css" />
			<link rel="stylesheet" type="text/css"
				href="../../jslib/jquery/css/jquery.treeview.css" />
			<link rel="stylesheet" type="text/css"
				href="../../jslib/dialog/dialog.css" />
			<link rel="stylesheet" href="../../jslib/paging/iscreate-paging.css"
				type="text/css" />

			<script type="text/javascript"
				src="../../jslib/jquery/jquery-1.6.2.min.js">
</script>
			<script type="text/javascript" src="../js/leftMenu.js">
</script>
			<script type="text/javascript"
				src="../../jslib/highcharts/highcharts.src.js">
</script>
			<script type="text/javascript"
				src="../../jslib/highcharts/exporting.src.js">
</script>
			<script type="text/javascript" src="../../jslib/paging/paging.js">
</script>
			<script type="text/javascript">
$(function() {
	$.ajax( {
			"url" : "carCensusReport_ajax!getLoginUserBizId.action",
			"type" : "post",
			"async" : true,
			"success" : function(data) {
				data = eval("(" + data + ")");
				orgId = data.orgId;
				orgName = data.name;
				createReportByBiz(orgId);
			}
		});
})

var orgTitle = "";
var orgName = "";
var orgId = "";
//按组织id,查询车辆信息集合
function createReportByBiz(orgId) {
	if (!orgId || orgId == "") {
		return;
	}
	showCover();
	var date = new Date();
	var time = date.toString("yyyy-MM");
	var time2 = date.getFullYear() + "年" + date.getMonth()+"月";
	orgTitle = "车辆里程统计("  + orgName + ","+time2+")";
	$.ajax( {
		"url" : "carCensusReport_ajax!censusCarCountMileageByBizAction.action",
		"type" : "post",
		"data" : {
			"carBizId" : orgId,
			"time" : time
		},
		"success" : function(result) {
			orgId_info = {};
			var data = eval("(" + result + ")");
			var columnName = [];
			//车辆里程读数
			var carMileage = {
				"type" : "line",
				"name" : "车辆读表里程（公里）",
				"data" : []
			};
			//车辆gps里程
			var carGpsMileage = {
				"type" : "line",
				"name" : "车辆gps里程（公里）",
				"color" : "#89A54E",
				"data" : []
			};
			//车辆数量
			var carCount = {
				"type" : "column",
				"name" : "车辆数量",
				"data" : [],
				"color" : "#FF6600",
				"yAxis" : 1
			};
			var carMileageArr = [];
			var carGpsMileageArr = [];
			var carCountArr = [];
			for ( var key in data) {
				var col = key;
				if ( col.length > 8 ) {
					for ( var ii = 8 ; ii < col.length ; ii += 8 ) {
						col = col.substring(0,ii) + "<br/>" + col.substring(ii);
						ii+=4;
					}
				}
				columnName.push(col);
				var info = data[key];
				for ( var key_info in info) {
					if (key_info == "info") {
						orgId_info[key] = info[key_info];
					} else if (key_info == "totalMileage") {
						carMileageArr.push(info[key_info]);
					} else if (key_info == "totalGpsMileage") {
						carGpsMileageArr.push(info[key_info]);
					} else if (key_info == "carCount") {
						carCountArr.push(parseFloat(info[key_info]));
					} else if (key_info == "data") {
						//mileageData[key] = info[key_info];
					}
				}
			}
			carMileage["data"] = carMileageArr;
			carGpsMileage["data"] = carGpsMileageArr;
			carCount["data"] = carCountArr;
			var arr = [ carCount, carMileage, carGpsMileage ];
			//yAxisTitle - y提示
			//容器id
			//X标题数组
			//数据
			columnAndSplineBasic1(orgTitle, "", "里程（公里）", "车辆数量",
					"report", columnName, arr)
			//显示列表
			hideCover();
		}
	});
}

function clickShowMore  (){}

function showCover() {
	if ($("#cover_div").is(":visible")) {
		return;
	}
	//var width = $("#statements_right_div").width();
	//var height = $("#statements_right_div").height();
	//var left = $("#statements_right_div").offset().left;
	//var top = $("#statements_right_div").offset().top;
	var width = $("#report").width();
	var height = $("#report").height();
	var left = $("#report").offset().left;
	var top = $("#report").offset().top;
	$("#cover_div").css( {
		"width" : width + "px",
		"height" : height + "px",
		"top" : top + "px",
		"left" : left + "px"
	});
	$("#cover_back_div").css( {
		"width" : width + "px",
		"height" : height + "px",
		"top" : 0 + "px",
		"left" : 0 + "px",
		"background-color" : "white",
		"opacity" : 0.8
	});
	var loadingLeft = width / 2 - $("#cover_loading_div").width() / 2;
	var loadingTop = height / 2 - $("#cover_loading_div").height() / 2;
	$("#cover_loading_div").css( {
		"top" : loadingTop + "px",
		"left" : loadingLeft + "px"
	});
	$("#cover_div").fadeIn("fast");
	$("#cover_div").css( {
		"display" : "block"
	});
}

function hideCover() {
	$("#cover_div").fadeOut("fast");
}
</script>

<script>

	//柱状图+折线(对比)
function columnAndSplineBasic1(title, subtitle, yAxisTitle, yAxisTitle2, divId,
		categories, series) {
	columnAndSplineBasicChart = new Highcharts.Chart(
			{
				chart : {
					renderTo : divId,
					zoomType : 'line'
				},
				title : {
					text : title
				},
				subtitle : {
					text : subtitle
				},
				xAxis : {
					categories : categories,
					labels : {
						rotation : -45,
						align : 'right',
						style : {
							fontSize : '12px',
							fontFamily : 'Verdana, sans-serif'
						}
					}
				},
				yAxis : [ { // Primary yAxis
							labels : {
								formatter : function() {
									return this.value;
								},
								style : {
									color : '#666'
								}
							},
							title : {
								text : yAxisTitle,
								style : {
									color : '#666'
								}
							},
							opposite : true

						}, { // Secondary yAxis
							gridLineWidth : 0,
							title : {
								text : yAxisTitle2,
								style : {
									color : "#89A54E"
								}
							},
							labels : {
								formatter : function() {
									return this.value;
								},
								style : {
									color : "#89A54E"
								}
							}

						} ],

				tooltip : {
					formatter : function() {
						var unit = {
							'故障处理平均历时' : '小时',
							'故障处理及时率' : '%'
						}[this.series.name];
						var yvar = this.y + "";
						if (yvar.indexOf(".") >= 0) {
							yvar = yvar.substring(0, yvar.indexOf(".") + 3);
						}
						return '' + this.x + ':' + this.series.name + ':'
								+ yvar;
					}

				},
				plotOptions : {
					column : {
						dataLabels : {
							enabled : true
						},
						pointPadding : 0.2,
						borderWidth : 0,
						point : {
							events : {
								click : function(e) {
									clickShowMore(categories[this.x],
											this.series.name);
								}
							}
						}
					},
					line : {
						dataLabels : {
							enabled : true
						}
					}
				},
				series : series
			});
}
</script>

	</head>
	<body>
		<div id="report" style="width:100%; height:250px;">
			<%-- 报表 --%>
		</div>
		<%-- 遮罩层 --%>
		<div id="cover_div" style=" display:none; z-index: 999; position: absolute;">
			<div id="cover_back_div" style="position: absolute;"></div>
			<div id="cover_loading_div" style="position: absolute;">
				<img id="cover_loading_img" src="image/loading.gif"></img>
				正在读取数据
			</div>
		</div>
	</body>
</html>




