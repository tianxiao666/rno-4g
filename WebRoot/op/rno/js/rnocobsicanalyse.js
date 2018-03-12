// 重新选择小区配置数据变量
var reSelCellDescId = new Array();
// var gisCellDisplayLib=new GisCellDisplayLib();
$("document").ready(function() {
	$("#conditionForm").validate({
		submitHandler : function(form) {
			var reSelected = false;
			// 判断重新选择小区配置变量是否为空''
			// console.log("reSelCellDescId.length:"+reSelCellDescId.length);
			if (0 == reSelCellDescId.length) {
				var selareaval = $("#queryCellAreaId").find("option:selected")
						.val();
				// console.log("selareaval:"+selareaval);
				if ("-1" == selareaval) {
					// 查询当前市的所有区域
					var str = '';
					$("#queryCellAreaId").find('option').not(':last').each(
							function(i, e) {
								var area = $(e).val();
								str += area + ",";
							});
					var areastr = str.substring(0, str.length - 1);
					// console.log("区域:"+areastr);
					cobsiccell(reSelected, areastr, null);
				} else {
					// 查询当前的区域
					// console.log("当前区域:"+selareaval);
					cobsiccell(reSelected, selareaval, null);
				}

				// getGisCell(currentloadtoken);
			} else {
				// 查询选择的小区配置数据的信息
				reSelected = true;
				var cellConfigIdStr = getSepCellDescIdByComma();
				// console.log(cellConfigIdStr);
				cobsiccell(reSelected, null, cellConfigIdStr);
			}

		}
	});
	$("#interferquery").click(function() {
		if(!whetherLoadCellToMap){
			animateOperTips("请先加载小区数据到地图然后再进行干扰查询。");
			showDetailDiv(0);
			return;
		}
		// console.log("onLoadingGisCell="+onLoadingGisCell);
		if (onLoadingGisCell == true) {
			alert("正在加载小区数据，请稍后尝试...");
			return;
		}
		$("#conditionForm").submit();
			// return true;
		});
	$("#trigger").click(function() {
				// console.log("glomapid:"+glomapid);
				if (glomapid == 'null' || glomapid == 'baidu') {
					// console.log("切换前是百度");
					$(this).val("切换百度");
					// console.log("切换后是谷歌");
					sessId = "google";
				} else {
					// console.log("切换前是谷歌");
					$(this).val("切换谷歌");
					// console.log("切换后是百度");
					sessId = "baidu";
				}
				storageMapId(sessId);
			});
	/*
	 * $(document).bind("click",function(e){ var targetobj=e.target; var
	 * clickid=$(targetobj).attr("id"); if("div_tab_0_li"==clickid){
	 * $("#div_tab_1_con").hide(); $("#div_tab_0_con").show();
	 * $("#div_tab_1_li").css("background-color","#EEEEEE");
	 * $("#div_tab_0_li").css("background-color","#FFFFFF"); }
	 * if("div_tab_1_li"==clickid){ $("#div_tab_0_con").hide();
	 * $("#div_tab_1_con").show();
	 * $("#div_tab_0_li").css("background-color","#EEEEEE");
	 * $("#div_tab_1_li").css("background-color","#FFFFFF"); } });
	 */
	$("#showCellConfigBtn").click(function() {
		$("#reSelCellConfig_Dialog").toggle();
		var display = $("#reSelCellConfig_Dialog").css("display");
		if ("block" == display) {
			$(".dialog_content").load(
					"initCellLoadConfigureImportPageAction #frame", function() {
						$("#frame").css("width", "600px");
						/*
						 * $("head").append("<script>"); a
						 * =$("head").children(":last"); console.log(a);
						 * a.attr({ type: "text/javascript", src:
						 * "js/cellLoadConfigureImport.js" });
						 */
						// $("head").append("</script>");
						/*
						 * var oHead =
						 * document.getElementsByTagName('HEAD').item(0); var
						 * oScript= document.createElement("script");
						 * oScript.type = "text/javascript";
						 * oScript.src="js/cellLoadConfigureImport.js";
						 * oHead.appendChild( oScript);
						 */
						$.ajaxSetup({
									cache : true
								});

						$.getScript("js/cellLoadConfigureImport.js");
						$.getScript("js/cellLoadConfigureImportAdd.js");
						$("#importAndLoadBtn").remove();
						// console.log($("head").children(":last").attr("src"));
				});
		}
	});
	// 初始化
	// 当刷新页面或重载页面时获取小区分析列表并将configIds值存储入reSelCellDescId数组
	initCellConfigTab();
	$("#restoreDefaultBtn").click(function() {
				// console.log("恢复缺省:"+getSepCellDescIdByComma());
				// 清空session,并将表格清除
				clearCellConfigFromCcsList();
				// 清空小区配置数组
				reSelCellDescId.splice(0, reSelCellDescId.length);
			});
	$("#wholenetinterferquery").click(function() {
		if(!whetherLoadCellToMap){
			animateOperTips("请先加载小区数据到地图然后再进行全网干扰查询。");
			showDetailDiv(0);
			return;
		}
		var reSelected = false;
		// 判断重新选择小区配置变量是否为空''
		// console.log("reSelCellDescId.length:"+reSelCellDescId.length);
		if (0 == reSelCellDescId.length) {
			var selareaval = $("#queryCellAreaId").find("option:selected")
					.val();
			// console.log("selareaval:"+selareaval);
			if ("-1" == selareaval) {
				// 查询当前市的所有区域
				var str = '';
				$("#queryCellAreaId").find('option').not(':last').each(
						function(i, e) {
							var area = $(e).val();
							str += area + ",";
						});
				var areastr = str.substring(0, str.length - 1);
				// console.log("区域:"+areastr);
				getcobsiccellwholenet(reSelected, areastr, null);
			} else {
				// 查询当前的区域
				// console.log("当前区域:"+selareaval);
				getcobsiccellwholenet(reSelected, selareaval, null);
			}

			// getGisCell(currentloadtoken);
		} else {
			// 查询选择的小区配置数据的信息
			reSelected = true;
			var cellConfigIdStr = getSepCellDescIdByComma();
			// console.log(cellConfigIdStr);
			getcobsiccellwholenet(reSelected, null, cellConfigIdStr);
		}
	});
		// getCellConfigureAnalysisListWhenRefreshPage();
});
/**
 * 通过session存储地图ID
 */
function storageMapId(mapid) {
	$(".loading_cover").css("display", "block");
	$.ajax({
				url : 'storageMapIdBySessForAjaxAction',
				data : {
					'mapId' : mapid
				},
				dataType : 'json',
				type : 'post',
				success : function(data) {
					glomapid = data;
					// console.log("data:"+data);
					// var c = data;
					try {
						// initRedirect("initGisCellDisplayAction");
						window.location.href = window.location.href;
					} catch (err) {

					}
				},
				error : function(xhr, textstatus, e) {

				},
				complete : function() {
					$(".loading_cover").css("display", "none");
				}
			});
}
/**
 * bcch,bsic匹配干扰查询功能
 */
function cobsiccell(reSelected, areaIdStr, cellConfigIdStr) {
	var sendData = {
		"reSelected" : reSelected,
		"areaIds" : areaIdStr,
		"configIds" : cellConfigIdStr
	};
	$(".loading_cover").css("display", "block");
	$("#conditionForm").ajaxSubmit({
				type : "POST",
				data : sendData,
				url : "getSpecifyAreaCoBsicCellsForAjaxAction",
				dataType : "text",
				async : true,
				success : function(data, textStatus) {
					// console.log("success");
					// 是json数据格式,普通文本格式
					// 使用eval函数将mes字串,转成对应的对象
					var mes_obj = eval("(" + data + ")");
					// console.log(mes_obj);
					var bcch = $("#bcch").val();
					var bsic = $("#bsic").val();
					var cobsic = bcch + "," + bsic;
					// console.log(mes_obj['interfercell'].length);
					// console.log("mes_obj['fail']="+mes_obj['fail'].length);
					// Js循环读取JSON数据，并增加下拉列表选项
					clearinterfertable();
					gisCellDisplayLib.resetSpecPolygonToDefaultOutlook();
					if (mes_obj['fail'] != null
							&& mes_obj['fail'] != "undefined") {
						// console.log(mes_obj['fail']);
						alert(mes_obj['fail']);
						hideDetailDiv();
					} else {
						createwholenetinterfertable(mes_obj);
						/*for (var i = 0; i < mes_obj['interfercell'].length; i++) {

							// console.log(mes_obj['interfercell'][i]);
							var cellstring = mes_obj['interfercell'][i];
							var cells = new Array();
							createinterfertable(cobsic, cellstring);
							// console.log("cellstring="+cellstring);
							cells = cellstring.split(",");
							// for(var j=0;cells.length;j++){
							gisCellDisplayLib.changeCellPolygonOptions(
									cells[0], {
										'fillColor' : '#660033'
									}, true);
							gisCellDisplayLib.changeCellPolygonOptions(
									cells[1], {
										'fillColor' : '#660033'
									}, true);
							// }
							gisCellDisplayLib.panToCell(cells[0]);
						}*/
						showDetailDiv(2);
					}
				},
				error : function(XMLHttpRequest, textStatus) {
					alert("\u8fd4\u56de\u6570\u636e\u9519\u8bef\uff1a"
							+ textStatus);
				},
				complete:function(){
				$(".loading_cover").css("display", "none");
				}
			});

}
/**
 * cobsic全网干扰查询功能
 */
function getcobsiccellwholenet(reSelected, areaIdStr, cellConfigIdStr) {
	var sendData = {
		"reSelected" : reSelected,
		"areaIds" : areaIdStr,
		"configIds" : cellConfigIdStr
	};
	$(".loading_cover").css("display", "block");
	$("#conditionForm").ajaxSubmit({
				type : "POST",
				data : sendData,
				url : "getSpecifyAreaWholeNetCoBsicCellsForAjaxAction",
				dataType : "text",
				async : true,
				success : function(data, textStatus) {
					// console.log("success");
					// 是json数据格式,普通文本格式
					// 使用eval函数将mes字串,转成对应的对象
					var mes_obj = eval("(" + data + ")");
					// console.log(mes_obj);

					/*
					 * var bcch = $("#bcch").val(); var bsic = $("#bsic").val();
					 * var cobsic = bcch + "," + bsic;
					 */
					// console.log(mes_obj['interfercell'].length);
					// console.log("mes_obj['fail']="+mes_obj['fail'].length);
					// Js循环读取JSON数据，并增加下拉列表选项
					clearinterfertable();
					gisCellDisplayLib.resetSpecPolygonToDefaultOutlook();
					if (mes_obj['fail'] != null
							&& mes_obj['fail'] != "undefined") {
						// console.log(mes_obj['fail']);
						alert(mes_obj['fail']);
						hideDetailDiv();
					} else {
						// console.log("接下来是createwholenetinterfertable");
						createwholenetinterfertable(mes_obj);
						/*
						 * for(var key in mes_obj){ //
						 * console.log(mes_obj[key]); var cobsic=key; var
						 * isncell=mes_obj[key].whetherNcell; var
						 * cellLists=mes_obj[key].cells; for(var i=0;i<cellLists.length;i++){
						 * var cellstring = cellLists[i]; var cells = new
						 * Array(); //createwholenetinterfertable(cobsic,
						 * cellstring); //
						 * console.log("cellstring="+cellstring); cells =
						 * cellstring.split(","); // for(var
						 * j=0;cells.length;j++){
						 * gisCellDisplayLib.changeCellPolygonOptions( cells[0], {
						 * 'fillColor' : '#660033' }, true);
						 * gisCellDisplayLib.changeCellPolygonOptions( cells[1], {
						 * 'fillColor' : '#660033' }, true); // }
						 * gisCellDisplayLib.panToCell(cells[0]); } }
						 */
						showDetailDiv(2);
					}
				},
				error : function(XMLHttpRequest, textStatus) {
					alert("\u8fd4\u56de\u6570\u636e\u9519\u8bef\uff1a"
							+ textStatus);
				},
				complete:function(){
				$(".loading_cover").css("display", "none");
				}
			});

}
/**
 * 创建bcch/bsic匹配小区干扰组表
 * 
 * @param {}
 *            cobsic
 * @param {}
 *            intercell
 */
function createinterfertable(cobsic, intercell) {

	if (cobsic == null || cobsic == undefined || !intercell) {
		return;
	}
	var cs = intercell.split(",");
	var str = "";
	for (var j = 0; j < cs.length; j++) {
		if (!cs[j] || cs[j] == "") {
			continue;
		}
		str += "<a title='点击移动到该小区' href='javascript:moveToCell(\"" + cs[j]
				+ "\")' style='margin-right:2px'>" + cs[j] + "</a>";
	}
	$("#interfertable").append(

			"<tr><td class='menuTd' style='width: 30%'>" + cobsic + "</td><td>"
					+ str + "</td></tr>"

	);
}
/*var aaaaa = {
	"50,66" : {
		"bcch" : 50,
		"bsic" : 66,
		"combinedCells" : [{
					"combinedCell" : "test1,test2",
					"whetherNcell" : true,
					"commonNcell" : "test3",
					"whetherComNcell" : true
				}, {
					"combinedCell" : "test1,test3",
					"whetherNcell" : true,
					"commonNcell" : "test2",
					"whetherComNcell" : true
				}, {
					"combinedCell" : "test2,test3",
					"whetherNcell" : true,
					"commonNcell" : "test1",
					"whetherComNcell" : true
				}]
	}
}*/
/**
 * 创建全网bcch/bsic匹配小区干扰组表
 * @param {} wholenetcellsobj
 * ajax返回data对象
 */
function createwholenetinterfertable(wholenetcellsobj) {
	var mes_obj = wholenetcellsobj;
	// console.log("进入createwholenetinterfertable："+wholenetcellsobj);
	/*
	 * if(cobsic==null || cobsic==undefined || !intercell){ return; } var
	 * cs=intercell.split(","); var str=""; for(var j=0;j<cs.length;j++){
	 * if(!cs[j] || cs[j]==""){ continue; } str+="<a title='点击移动到该小区'
	 * href='javascript:moveToCell(\""+cs[j]+"\")'
	 * style='margin-right:2px'>"+cs[j]+"</a>"; } $("#interfertable").append( "<tr><td class='menuTd' style='width: 30%'>"+cobsic+"</td><td>"+str+"</td></tr>" );
	 */
	for (var key in mes_obj) {
		// console.log(mes_obj[key]);
		var isPanto = false;
		var cobsic = key;// title
		// var isncell = mes_obj[key].whetherNcell;
		var cellLists = mes_obj[key].combinedCells;
		var cellscount = cellLists.length;// aa
		// console.log("cobsic,cellscount:"+cobsic+":"+cellscount);
		// for(var i=0;i<cellLists.length;i++){
		if (cellscount > 0) {
			// console.log("进入cellLists循环");
			var whetherNcell = cellLists[0]['whetherNcell'];
			var whetherComNcell = cellLists[0]['whetherComNcell'];
			var commonNcell = cellLists[0]['commonNcell'];
			var cs = cellLists[0]['combinedCell'].split(",");
			if (whetherNcell && whetherComNcell) {
				gisCellDisplayLib.drawLineBetweenCells(cs[0], cs[1], {
							'strokeColor' : '#FF0000',
							"strokeWeight" : 1
						});
				gisCellDisplayLib.drawLineBetweenCells(cs[0], commonNcell, {
							'strokeColor' : '#FF0000',
							"strokeWeight" : 1
						});
				gisCellDisplayLib.drawLineBetweenCells(cs[1], commonNcell, {
							'strokeColor' : '#FF0000',
							"strokeWeight" : 1
						});
			}
			if (whetherNcell) {
				gisCellDisplayLib.drawLineBetweenCells(cs[0], cs[1], {
							'strokeColor' : '#FF0000',
							"strokeWeight" : 1
						});
			} else {
				// 0000FF
				gisCellDisplayLib.drawLineBetweenCells(cs[0], commonNcell, {
							'strokeColor' : '#FF0000',
							"strokeWeight" : 1
						});
				gisCellDisplayLib.drawLineBetweenCells(cs[1], commonNcell, {
							'strokeColor' : '#FF0000',
							"strokeWeight" : 1
						});
				gisCellDisplayLib.drawLineBetweenCells(cs[0], cs[1], {
							'strokeColor' : '#0000FF',
							"strokeWeight" : 1
						});
			}
			if (!isPanto) {

				gisCellDisplayLib.panToCell(cs[0]);
			}
			isPanto = true;
			var str = "";
			for (var j = 0; j < cs.length; j++) {
				if (!cs[j] || cs[j] == "") {
					continue;
				}
				gisCellDisplayLib.changeCellPolygonOptions(cs[j], {
							'fillColor' : '#660033'
						}, true);
				str += "<a title='点击移动到该小区' href='javascript:moveToCell(\""
						+ cs[j] + "\")' style='margin-right:2px'>" + cs[j]
						+ "</a>";
			}
			var strcomm = "<a title='点击移动到该小区' href='javascript:moveToCell(\""
					+ (typeof(commonNcell) == "undefined" ? "" : commonNcell)
					+ "\")' style='margin-right:2px'>"
					+ (typeof(commonNcell) == "undefined" ? "" : commonNcell)
					+ "</a>";
			var str1 = "<tr>"
					+ "<td class='menuTd' style='width: 30%' rowspan=\""
					+ cellscount + "\" align=\"center\">" + cobsic + "</td>"
					+ "<td align=\"center\">" + str + " "
					+ getNcellDesc(whetherNcell, whetherComNcell, strcomm)
					+ "</td>" + "</tr>";
			// var str2="<tr>"
			// +"<td align=\"center\">t2,t3</td>"
			// "</tr>";
			var str3 = "";
			if (cellscount > 1) {
				for (var j = 1; j < cellscount; j++) {
					var whetherNcell = cellLists[j]['whetherNcell'];
					var whetherComNcell = cellLists[j]['whetherComNcell'];
					var commonNcell = cellLists[j]['commonNcell'];
					var cs = cellLists[j]['combinedCell'].split(",");
					if (whetherNcell && whetherComNcell) {
						gisCellDisplayLib.drawLineBetweenCells(cs[0], cs[1], {
									'strokeColor' : '#FF0000',
									"strokeWeight" : 1
								});
						gisCellDisplayLib.drawLineBetweenCells(cs[0],
								commonNcell, {
									'strokeColor' : '#FF0000',
									"strokeWeight" : 1
								});
						gisCellDisplayLib.drawLineBetweenCells(cs[1],
								commonNcell, {
									'strokeColor' : '#FF0000',
									"strokeWeight" : 1
								});
					}
					if (whetherNcell) {
						gisCellDisplayLib.drawLineBetweenCells(cs[0], cs[1], {
									'strokeColor' : '#FF0000',
									"strokeWeight" : 1
								});
					} else {
						// 0000FF
						gisCellDisplayLib.drawLineBetweenCells(cs[0],
								commonNcell, {
									'strokeColor' : '#FF0000',
									"strokeWeight" : 1
								});
						gisCellDisplayLib.drawLineBetweenCells(cs[1],
								commonNcell, {
									'strokeColor' : '#FF0000',
									"strokeWeight" : 1
								});
						gisCellDisplayLib.drawLineBetweenCells(cs[0], cs[1], {
									'strokeColor' : '#0000FF',
									"strokeWeight" : 1
								});
					}
					var str = "";
					for (var k = 0; k < cs.length; k++) {
						if (!cs[k] || cs[k] == "") {
							continue;
						}
						gisCellDisplayLib.changeCellPolygonOptions(cs[k], {
									'fillColor' : '#660033'
								}, true);
						str += "<a title='点击移动到该小区' href='javascript:moveToCell(\""
								+ cs[k]
								+ "\")' style='margin-right:2px'>"
								+ cs[k] + "</a>";
					}
					var strcomm = "<a title='点击移动到该小区' href='javascript:moveToCell(\""
							+ (typeof(commonNcell) == "undefined"
									? ""
									: commonNcell)
							+ "\")' style='margin-right:2px'>"
							+ (typeof(commonNcell) == "undefined"
									? ""
									: commonNcell) + "</a>";
					var str2 = "<tr>"
							+ "<td align=\"center\">"
							+ str
							+ " "
							+ getNcellDesc(whetherNcell, whetherComNcell,
									strcomm) + "</td>"
					"</tr>";
					str3 += str2;
				}
				$("#interfertable").append(str1 + str3);
			} else {
				$("#interfertable").append(str1);
			}
		}
		// }
	}
}
/**
 * 判断是否相邻或共邻,返回描述信息
 * @param {} whetherNcell
 * @param {} whetherComNcell
 * @param {} commonNcell
 * @return {}
 */
function getNcellDesc(whetherNcell,whetherComNcell,commonNcell){
	if(whetherNcell && whetherComNcell){
		return "相邻且与 "+commonNcell+"共邻";
	}
	if(whetherNcell){
		return "相邻 "+commonNcell;
	}
	if(whetherComNcell){
		return "不相邻但与 "+commonNcell+"共邻";
	}
}
function moveToCell(cell) {
	if (cell == null || cell == undefined) {
		return;
	}
	gisCellDisplayLib.panToCell(cell);
}

function clearinterfertable() {
	// $("#interfertable tr:not(:first)").empty();
	// $("#interfertable tr").eq(0).nextAll().remove();
	// $("#interfertable tr:eq(0)").nextAll().remove();
	// $("#interfertable tr").text("").find(":not(:first)").remove();
	var trslength = $("#interfertable").find("tr").length;
	var trslength1 = $("tr", $("#interfertable"));
	// for(var i=trslength;i>=1;i--) //保留最前面一行！
	// console.log(trslength1.length);
	for (var i = 1; i < trslength1.length; i++) {
		// $("#interfertable").find("tr").eq(i).remove();

		trslength1.eq(i).remove();
	}
	// $("tbody").empty();
}
// 隐藏详情面板
function hideDetailDiv() {
	// console.log("hideDetailDiv进来了");
	$("a.siwtch").hide();
	$(".switch_hidden").show();
	$(".resource_list_icon").animate({
				right : '0px'
			}, 'fast');
	$(".resource_list_box").hide("fast");
}
// 显示详情面板
function showDetailDiv(lival) {
	// console.log("showDetailDiv进来了");
	$(".switch_hidden").hide();
	$(".switch").show();
	$(".resource_list_icon").animate({
				right : '286px'
			}, 'fast');
	$(".resource_list_box").show("fast");
	$(".selected2").click();
//	console.log($("#div_tab ul li").eq(lival));
	$("#div_tab ul li").eq(lival).click();
}
/*function showDetailDivForCell(lival) {
	// console.log("showDetailDiv进来了");
	
	$(".switch_hidden").hide();
	$(".switch").show();
	$(".resource_list_icon").animate({
				right : '286px'
			}, 'fast');
	$(".resource_list_box").show("fast");
	$("div_tab ul li").eq(lival).click();
}*/
/**
 * 初始化小区数据配置信息 同时:当刷新页面或重载页面时获取小区分析列表并将configIds值存储入reSelCellDescId数组
 * 目的:防止reSelCellDescId变量因页面刷新而丢失,同时session数据还存在的现象
 */
function initCellConfigTab() {
	$.ajax({
				url : 'getCellConfigureAnalysisListForAjaxAction',
				dataType : 'text',
				// data:sendDate,
				type : 'post',
				success : function(data) {
					var mes_obj = eval("(" + data + ")");
					// console.log(mes_obj);
					for (var i = 0; i < mes_obj.length; i++) {

						reSelCellDescId.push(mes_obj[i]['configId']);
					}
					showRefreshLoadList("loadRefreshlistTable3", mes_obj);// 加载数据
				},
				error : function(err, status) {
					// console.error(status);
				},
				complete : function() {
					$(".loading_cover").css("display", "none");
				}
			});
}
function showRefreshLoadList(tableId, data) {
	if (!data) {
		return;
	}
	// <tr><td colspan=\"5\" style=\"background: none repeat scroll 0 0
	// #E8EDFF;\">城城城城 </td></tr>
	// <input type=\"checkbox\" onclick=\"javascript:operAllCheckbox(this,2);\"
	// name=\"selectall\" id=\"2\" /><label for=\"1\"></label>
	var htmlstr = "<tr><th>标题</th><th>方案名称</th><th>上传时间</th><th style=\"width: 10%\">操作</th></tr>";// 表头
	var item;
	var trClass = "";
	// console.log(data.length);
	for (var i = 0; i < data.length; i++) {

		item = data[i];
		if (i % 2 == 0) {
			trClass = "tb-tr-bg-coldwhite";// tr class 值
		} else {
			trClass = "tb-tr-bg-warmwhite";
		}
		if (item) {
			htmlstr += "<tr class=\""
					+ trClass
					+ "\">"// tr html
					+ "  <td width=\"40%\" class=\"bd-right-white\" >    "
					+ "  <span >"
					+ item['title']
					+ "</span>"
					+ "  </td>"
					+ "  <td  width=\"25%\"  class=\"bd-right-white td_nowrap\">"
					+ "  <span >"
					+ item['name']
					+ "</span>"
					+ "  </td>"
					+ "  <td  width=\"20%\"  class=\"td-standard-date bd-right-white td_nowrap\">"
					+ "  <span >"
					+ item['collectTime']
					+ "</span>"
					+ "  </td>"
					+ "  <td width=\"10%\">"
					+ "  <input type=\"button\" value=\"移除\" onclick=\"removeCellConfigFromAnalist(this,\'"
					+ data[i]['configId'] + "\')\" id="
					+ data[i]['configId']
					+ " />"
					// + " <input type='checkbox' id='"+ data[i]['configId']+ "'
					// class='forcheck' />";
					+ "  </td >"
					+ "  </tr>"
					+ "<tr><td colspan=\"5\" style=\"background-color: #e7e7e7; height:1px; width:100%\"></td> </tr> ";
		}
	}
	$("#" + tableId).html(htmlstr);// table html 内容
}
/**
 * 从分析列表中删除
 */
function removeCellConfigFromAnalist(obj, configId) {

	var ccsids = configId;
	$(".loading_cover").css("display", "block");
	$.ajax({
				url : 'removeCcsItemFromLoadedListForAjaxAction',
				dataType : 'text',
				data : {
					'configIds' : ccsids
				},
				success : function(data) {
					if (data == "true") {
						// alert("删除成功。");
						$(obj).closest("tr").remove();
						rmvCellDescIdFromReSelCeDescId(configId);
						// console.log("reSelCellDescId.length:"+reSelCellDescId.length);
						animateOperTips("删除成功。");
					} else {
						// alert("删除失败！");
						animateOperTips("删除失败！");
					}
				},
				complete : function() {
					$(".loading_cover").css("display", "none");
				}
			});
}
/**
 * 从分析列表中清空:恢复默认数据=清空session和表格行
 */
function clearCellConfigFromCcsList() {
	var ids = new Array();
	var trs = new Array();
	$("#loadRefreshlistTable3").find("input").each(function(i, ele) {
				// console.log($(ele));
				ids.push($(ele).attr("id"));
				trs.push($(ele).closest("tr"));
			});

	if (ids.length == 0) {
		// layer.msg("请先选择需要删除的邻区关系",2,2);
		// alert('请先选择需要从分析列表中删除的Ccs');
		return;
	}

	var ccsids = ids.join(",");
	// alert(ccsids);

	$(".loading_cover").css("display", "block");
	$.ajax({
				url : 'removeCcsItemFromLoadedListForAjaxAction',
				dataType : 'text',
				data : {
					'configIds' : ccsids
				},
				success : function(data) {
					if (data == "true") {
						for (var i = 0; i < trs.length; i++) {
							trs[i].remove();
						}
						trs = null;
						// alert("删除成功。");
						animateOperTips("删除成功。");
					} else {
						// alert("删除失败！");
						animateOperTips("删除失败！");
					}
				},
				complete : function() {
					$(".loading_cover").css("display", "none");
				}
			});
}
function animateOperTips(tip) {
	$("#operInfo").find("#operTip").html(tip);
	animateInAndOut("operInfo", 500, 500, 2000);
}
/**
 * 获取重新选择小区配置描述ID
 * 
 * @param {}
 *            elem 获取重新小区配置描述ID的reSelCellDescId数组信息
 */
function getReSelCellDescIdVal(elem) {
	// console.log("进入getReSelCellDescIdVal:"+elem.val()+"---"+reSelCellDescId.length);
	elem.each(function(i) {
				var opval = $(this).val();
				// console.log("opval:"+opval);
				var exist = false;
				if (reSelCellDescId.length == 0)
					reSelCellDescId.push(opval);
				for (var j = 0; j < reSelCellDescId.length; j++) {
					if (opval == reSelCellDescId[j])
						exist = true;
				}
				if (!exist)
					reSelCellDescId.push(opval);
			});
	// console.log("结束getReSelCellDescIdVal:"+reSelCellDescId.length);
}
/**
 * 移除小区配置描述ID从全局变量
 * 
 * @param {}
 *            configId
 */
function rmvCellDescIdFromReSelCeDescId(configId) {
	// console.log("进入rmvCellDescIdFromReSelCeDescId
	// length:"+reSelCellDescId.length);
	for (var i = 0; i < reSelCellDescId.length; i++) {
		if (configId == reSelCellDescId[i]) {
			reSelCellDescId.splice(i, 1);
			break;
		}
	}
	// console.log("结束rmvCellDescIdFromReSelCeDescId
	// length:"+reSelCellDescId.length);
}
/**
 * 获取区域下拉列表的区域ID
 * 
 * @param {}
 *            elem
 * @return {} 若被选项是全部则返回市所属的区域ID数据，以逗号分割形式返回，若不是则返回给定区域ID
 */
function getSelectedListAreaIdVal(elem) {
	var str = "";
	var selval = $("#" + elem).find("option:selected").text();
	if (selval == "全部") {
		$("#" + elem + " option[text!=\"全部\"]").each(function(i, e) {
					var opval = $(e).val();
					str += opval + ",";
				});
		return str.substring(0, str.length - 1);
	} else {
		return $("#" + elem).find("option:selected").val();
	}
}
/**
 * 获取[以逗号分割的]小区配置描述ID字符串
 * 
 * @return {} 获取重新小区配置描述ID的reSelCellDescId数组信息
 */
function getSepCellDescIdByComma() {
	var str = "";
	for (var j = 0; j < reSelCellDescId.length; j++) {
		str += reSelCellDescId[j] + ",";
	}
	return str.substring(0, str.length - 1);
}
/**
 * 当刷新页面或重载页面时获取小区分析列表并将configIds值存储入reSelCellDescId数组
 * 目的:防止reSelCellDescId变量因页面刷新而丢失,同时session数据还存在的现象
 */
/*
 * function getCellConfigureAnalysisListWhenRefreshPage() { $.ajax({ url :
 * 'getCellConfigureAnalysisListForAjaxAction', dataType : 'text', //
 * data:sendDate, type : 'post', success : function(data) { var mes_obj =
 * eval("(" + data + ")"); // console.log("mes_obj:"+mes_obj); for (var i = 0; i <
 * mes_obj.length; i++) {
 * 
 * reSelCellDescId.push(mes_obj[i]['configId']); }
 *  }, error : function(err, status) { // console.error(status); }, complete :
 * function() { // console.log(reSelCellDescId); } }); }
 */
