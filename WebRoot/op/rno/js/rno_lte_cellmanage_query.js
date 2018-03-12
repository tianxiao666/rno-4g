var chineseToCode = [ {
	'code' : 'SITE_STYLE',
	'name' : '基站类型'
}, {
	'code' : 'PCI',
	'name' : 'PCI'
}, {
	'code' : 'LONGITUDE',
	'name' : '小区经度'
}, {
	'code' : 'LATITUDE',
	'name' : '小区纬度'
}, {
	'code' : 'GROUND_HEIGHT',
	'name' : '天线挂高'
}, {
	'code' : 'AZIMUTH',
	'name' : '方向角'
}, {
	'code' : 'STATION_CFG',
	'name' : '开站配比'
}, {
	'code' : 'BAND_TYPE',
	'name' : '频带类型'
}, {
	'code' : 'DOWNTILT',
	'name' : '下倾角'
}, {
	'code' : 'FRAME_CFG',
	'name' : '子帧分配'
}, {
	'code' : 'COVER_TYPE',
	'name' : '覆盖类型'
}, {
	'code' : 'M_DOWNTILT',
	'name' : '机械下倾角'
}, {
	'code' : 'SPECIAL_FRAME_CFG',
	'name' : '特殊子帧'
}, {
	'code' : 'RSPOWER',
	'name' : '参考信号功率'
}, {
	'code' : 'E_DOWNTILT',
	'name' : '电子下倾角'
}, {
	'code' : 'IS_VIP',
	'name' : '是否VIP站点'
}, {
	'code' : 'TAC',
	'name' : 'TAC'
}, {
	'code' : 'RRUNUM',
	'name' : 'RRU数量'
}, {
	'code' : 'TAL',
	'name' : 'TAL'
}, {
	'code' : 'RRUVER',
	'name' : 'RRU型号'
}, {
	'code' : 'STATE',
	'name' : '开通情况'
}, {
	'code' : 'CELL_RADIUS',
	'name' : '小区覆盖半径'
}, {
	'code' : 'ANTENNA_TYPE',
	'name' : '天线型号'
}, {
	'code' : 'OPERATION_TIME',
	'name' : '开通日期'
}, {
	'code' : 'BAND',
	'name' : '带宽'
}, {
	'code' : 'INTEGRATED',
	'name' : '天线是否合路'
}, {
	'code' : 'BUILD_TYPE',
	'name' : '建设类型'
}, {
	'code' : 'EARFCN',
	'name' : '下行频点'
}, {
	'code' : 'PDCCH',
	'name' : 'PDCCH'
}, {
	'code' : 'PA',
	'name' : 'PA'
}, {
	'code' : 'PB',
	'name' : 'PB'
} ];
/*{
'code' : 'LOCAL_CELLID',
'name' : '本地小区标识'
}, */
//显示同一站的小区
var currentCellArray = null;
var currentIndex = 0;

var lastCond=new Object();

var submitOK = true;//判断lte小区编辑信息是否通过验证
var editLteCellArray = null;

$(document)
		.ready(
				function() {

					// 查询条件
					$("#conditionForm").submit(function() {
						// 重新初始化分页参数
						initFormPage('conditionForm');
						$("span#nameErrorText1").html("");
						 $("span#nameErrorText2").html("");
						//记录
						lastCond['queryProvinceId']=$("#queryProvinceId").val();
						lastCond['queryCityId']=$("#queryCityId").val();
						lastCond['queryEnodebNameId']=$("#queryEnodebNameId").val();
						lastCond['queryCellNameId']=$("#queryCellNameId").val();
						lastCond['queryCellPciId']=$("#queryCellPciId").val();

						var queryProvinceId = $("#queryProvinceId").val();
						var queryCityId = $("#queryCityId").val();
						 var queryEnodebNameId = $("#queryEnodebNameId").val();
						 var queryCellNameId = $("#queryCellNameId").val();
						 var queryCellPciId = $("#queryCellPciId").val();
						 var str = queryProvinceId + queryCityId + queryEnodebNameId + queryCellNameId + queryCellPciId ;
						 /*var pattern = new RegExp("[\\\\`~*#!@%&'|{}\":<>.,;+/=?$￥！()（）【】，。：；”‘？》《^]+");*/
						 var strExp=/^[\u4e00-\u9fa5A-Za-z0-9_-]+$/;
						  if(!strExp.test(str)){
							   	$("span#nameErrorText1").html("含有非法字符！");
							   	  return false;
							 }  else if(!(queryEnodebNameId.length<40&&queryCellNameId.length<40&&
									 queryCellPciId.length<40)){
								 $("span#nameErrorText2").html("输入信息过长！");
							      return false;
						     }
						queryLteCell();
						return false;
					});

					// 查询的区域联动
					$("#queryProvinceId")
							.change(
									function() {
										if ($("#queryProvinceId").val() == '-1') {
											$("#queryCityId").html("");
											$("#queryCityId").append("<option value='-1' selected='true'>全部</option>");
										} else {
											getSubAreas(
													"queryProvinceId",
													"queryCityId",
													"市",
													function() {
														$("#queryCityId")
																.append(
																		"<option value='-1' selected='true'>全部</option>")
													});
										}
									});


					//下一小区详情
					$("#nextCellDetailBtn").click(function(){
						++currentIndex;
						if(currentIndex>=currentCellArray.length){
							currentIndex=0;
						}
						showOneCellDetail(currentIndex);

					});

					//提交更改的lte小区详情
					$("#btnUpdate").click(function() {
					     //提交前再做一次全局检验
					     var length = chineseToCode.length;
						 for ( var i = 0; i < length; i++) {
						    var oneKey = chineseToCode[i];
						    var code = oneKey['code'];      //元素id值
						    var value = $("#"+code).val(); //元素value值
						    //如果submitOK为true，继续验证下一个元素，反之则不再循环
						 	if(submitOK==true){
								checkDateType(code,value);
						 	} else {
						 		break;
						 	}
						 }
						 updateLteCellAndCoSiteCellDetail(submitOK);
					});

					//重置当前编辑lte小区详情
					$("#btnReset").click(function() {
						 //获取缓存的目标小区，即editLteCellArray的第一个小区
						 var oneLteCellDetail = editLteCellArray[0];
						 //加载到页面重置数据
						 loadOneLteCellDetailForEdit(oneLteCellDetail);
					});
				});

// 跳转
function showListViewByPage(dir) {
	var pageSize = new Number($("#hiddenPageSize").val());
	var currentPage = new Number($("#hiddenCurrentPage").val());
	var totalPageCnt = new Number($("#hiddenTotalPageCnt").val());
	var totalCnt = new Number($("#hiddenTotalCnt").val());

	if (dir === "first") {
		if (currentPage <= 1) {
			return;
		} else {
			$("#hiddenCurrentPage").val("1");
		}
	} else if (dir === "last") {
		if (currentPage >= totalPageCnt) {
			return;
		} else {
			$("#hiddenCurrentPage").val(totalPageCnt);
		}
	} else if (dir === "back") {
		if (currentPage <= 1) {
			return;
		} else {
			$("#hiddenCurrentPage").val(currentPage - 1);
		}
	} else if (dir === "next") {
		if (currentPage >= totalPageCnt) {
			return;
		} else {
			$("#hiddenCurrentPage").val(currentPage + 1);
		}
	} else if (dir === "num") {
		var userinput = $("#showCurrentPage").val();
		if (isNaN(userinput)) {
			alert("请输入数字！")
			return;
		}
		if (userinput > totalPageCnt || userinput < 1) {
			alert("输入页面范围不在范围内！");
			return;
		}
		$("#hiddenCurrentPage").val(userinput);
	} else {
		return;
	}
	// 获取资源
	queryLteCell();
}

/**
 * 获取小区
 */
function queryLteCell() {
	$(".loading_cover").css("display", "block");
	$("#conditionForm").ajaxSubmit({
		url : 'queryLteCellByPageForAjaxAction',
		dataType : 'text',
		success : function(data) {
			// console.log(data);
			showLteCell(data);
		},
		complete : function() {
			$(".loading_cover").css("display", "none");
		}
	});

}

/**
 * 显示查询回来的小区
 *
 * @param {}
 *            data {'page':{},'celllist':[]}
 */
function showLteCell(data) {

	data = eval("(" + data + ")");
	// console.log("data===" + data);
	// 准备填充小区
	var page = data['page'];
	var celllist = data['data'];

	var table = $("#queryResultTab");
	// 只保留表头
	$("#queryResultTab tr:not(:first)").each(function(i, ele) {
		$(ele).remove();
	});
	// console.log("celllist====" + celllist);
	if (celllist) {
		var one;
		var tr;
		for ( var i = 0; i < celllist.length; i++) {
			// console.log("i==" + i);
			one = celllist[i];
			if (!one) {
				continue;
			}

			tr = "<tr class=\"greystyle-standard-whitetr\">";

			var area_name;

			if (queryProvinceId.value == -1) {
				area_name = "全部";
			} else if (queryCityId.value == -1) {
				area_name = queryProvinceId.options[queryProvinceId.selectedIndex].text;
			} else {
				area_name = getValidValue(one['AREA_NAME']);
			}

			tr += "<td>" + area_name + "</td>";
			tr += "<td>" + getValidValue(one['ENODEB_NAME']) + "</td>";
			tr += "<td>" + getValidValue(one['CELL_NAME']) + "</td>";
			tr += "<td>" + getValidValue(one['PCI']) + "</td>";
			tr += "<td>" + getValidValue(one['BAND']) + "</td>";
			tr += "<td>" + getValidValue(one['EARFCN']) + "</td>";
			tr += "<td>" + getValidValue(one['RSPOWER']) + "</td>";

			tr += "<td><a onclick='queryLteCellAndCoSiteCellDetail(\""
					+ one['LTE_CELL_ID']
					+ "\");'>查看详情</a><a style='margin-left:20px' onclick='editLteCellAndCoSiteCellDetail(\""
					+ one['LTE_CELL_ID']
					+ "\");'>编辑</a><a style='margin-left:20px' onclick='deleteLteCell(\""+one['LTE_CELL_ID']+"\");'>删除</a></td>";
			// tr += "<td> <a target='_blank'
			// href=\""+tomodifyCell(one['id'],one['areaId'])+"\"><p><img
			// src=\"../../images/edit-go.png\" align=\"absmiddle\" width=\"16\"
			// height=\"16\" alt=\"查看/编辑明细\" /> 修改</p> </a></td>";

			tr += "</tr>";
			// console.log("tr===" + tr);
			table.append($(tr));// 增加
		}
	}

	// 设置隐藏的page信息
	setFormPageInfo("conditionForm", data['page']);

	// 设置分页面板
	setPageView(data['page'], "lteCellPageDiv");

}

// 初始化form下的page信息
function initFormPage(formId) {
	var form = $("#" + formId);
	if (!form) {
		return;
	}
	form.find("#hiddenPageSize").val(25);
	form.find("#hiddenCurrentPage").val(1);
	form.find("#hiddenTotalPageCnt").val(-1);
	form.find("#hiddenTotalCnt").val(-1);
}

// 设置formid下的page信息
// 其中，当前页会加一
function setFormPageInfo(formId, page) {
	if (formId == null || formId == undefined || page == null
			|| page == undefined) {
		return;
	}

	var form = $("#" + formId);
	if (!form) {
		return;
	}

	// console.log("setFormPageInfo .
	// pageSize="+page.pageSize+",currentPage="+page.currentPage+",totalPageCnt="+page.totalPageCnt+",totalCnt="+page.totalCnt);
	form.find("#hiddenPageSize").val(page.pageSize);
	form.find("#hiddenCurrentPage").val(new Number(page.currentPage));// /
	form.find("#hiddenTotalPageCnt").val(page.totalPageCnt);
	form.find("#hiddenTotalCnt").val(page.totalCnt);

	// alert("after set currentpage in form is
	// :"+form.find("#hiddenCurrentPage").val());
}

/**
 * 设置分页面板
 *
 * @param page
 *            分页信息
 * @param divId
 *            分页面板id
 */
function setPageView(page, divId) {
	if (page == null || page == undefined) {
		return;
	}

	var div = $("#" + divId);
	if (!div) {
		return;
	}

	//
	var pageSize = page['pageSize'] ? page['pageSize'] : 0;
	// $("#hiddenPageSize").val(pageSize);

	var currentPage = page['currentPage'] ? page['currentPage'] : 1;
	// $("#hiddenCurrentPage").val(currentPage);

	var totalPageCnt = page['totalPageCnt'] ? page['totalPageCnt'] : 0;
	// $("#hiddenTotalPageCnt").val(totalPageCnt);

	var totalCnt = page['totalCnt'] ? page['totalCnt'] : 0;
	// $("#hiddenTotalCnt").val(totalCnt);

	// 设置到面板上
	$(div).find("#emTotalCnt").html(totalCnt);
	$(div).find("#showCurrentPage").val(currentPage);
	$(div).find("#emTotalPageCnt").html(totalPageCnt);
}


//删除lte小区
function deleteLteCell(lteCellId){
	$.ajax({
		url:'deleteLteCellByIdsForAjaxAction',
		data:{'ids':lteCellId},
		dataType : 'text',
		success : function(raw) {
			// console.log(data);
			//重新查询
			var data=null;
			try{
				data=eval("("+raw+")");
				if(!data){
					alert("未知错误！请联系管理员！");
				}else{
					if(data['flag']==true){
						//
						alert("删除成功！");

						//重新执行查询
						var table=$("#conditionForm");

						table.find("#hiddenTotalCnt").val('-1');//表示在后台要重新计算总数量
						//当前有多少条记录
						var rowcnt=document.getElementById('queryResultTab').rows.length;
						if(rowcnt>2){
							//此时重新查询就可以了。
						}else{
							var curpage=new Number(table.find("#hiddenCurrentPage").val());
							//该页只有一行，如果当前不是第一页的话，就将页数减一，重新查询
							if(curpage>1){
								table.find("#hiddenCurrentPage").val(curpage-1);//减一页
							}
						}

						//设置查询条件，不直接获取，防止查询后用户有修改，然后再删除记录的情况
						$("#queryProvinceId").val(lastCond['queryProvinceId']);
						$("#queryCityId").val(lastCond['queryCityId']);
						$("#queryEnodebNameId").val(lastCond['queryEnodebNameId']);
						$("#queryCellNameId").val(lastCond['queryCellNameId']);
						$("#queryCellPciId").val(lastCond['queryCellPciId']);

						queryLteCell();
					}else{
						alert("删除失败！"+data['msg']);
					}
				}
			}catch(err){
			    alert("未知错误！请联系管理员！");
			}
		},
		complete : function() {
			$(".loading_cover").css("display", "none");
		}
	});
}

/**
 * 查询小区及同站小区详情
 *
 * @param id
 */
function queryLteCellAndCoSiteCellDetail(id) {
	$(".loading_cover").css("display", "block");
	$.ajax({
		url : 'queryLteCellAndCositeCellsDetailForAjaxAction',
		data : {
			'lteCellId' : id
		},
		dataType : 'text',
		success : function(data) {
			// console.log(data);
			showLteCellAndCoSiteCellDetail(data);
		},
		complete : function() {
			$(".loading_cover").css("display", "none");
		}
	});
}

/**
 * 显示小区和同站小区详情
 *
 * @param data
 */
function showLteCellAndCoSiteCellDetail(data) {
	var arr = eval("(" + data + ")");
	currentCellArray = arr;
	currentIndex = 0;

	showOneCellDetail(0);
}

/**
 * 编辑小区详情
 *
 * @param id
 */
function editLteCellAndCoSiteCellDetail(id) {
	//加载lte小区id保存到隐藏域
	$("#lteCellIdForEdit").val(getValidValue(id));
	//显示编辑框
	$("#editLteCellMessage").css({
		"top" : (32) + "%",
		"left" : (25) + "%",
		"width" : (700) + "px",
		"z-index" : (30)
	});
	$("#editLteCellMessage").show();
	//加载需要编辑的lte小区数据到页面
	$(".loading_cover").css("display", "block");
	$.ajax({
		url :'queryLteCellAndCositeCellsDetailForAjaxAction',
		data : {
			'lteCellId' : id
		},
		dataType :'text',
		success : function(data) {
			var arr = eval("(" + data + ")");
			editLteCellArray = arr;
			//只取目标小区，即第一个小区
			var oneLteCellDetail = editLteCellArray[0];
			if (!oneLteCellDetail) {
				return;
			}
			//console.log(oneLteCellDetail);
			loadOneLteCellDetailForEdit(oneLteCellDetail);
		},
		complete : function() {
			$(".loading_cover").css("display", "none");
		}
	});
}

/**
 * 加载要编辑的小区详情到页面
 *
 * @param oneLteCellDetail
 */
function loadOneLteCellDetailForEdit(oneLteCellDetail) {
	// 循环设置
	var html = '';
	var size = chineseToCode.length;
	var onekey;
	for ( var i = 0; i < size; i++) {
		if (i % 3 == 0) {
			// 换行
			if (i > 0) {
				html += "</tr>";
			}
			html += "<tr>";
		}
		onekey = chineseToCode[i];
		onevalue = typeof oneLteCellDetail[onekey['code']]=='undefined'?"":oneLteCellDetail[onekey['code']];
		//加载select标签
		if(onekey['code']==='IS_VIP' || onekey['code']==='STATE' || onekey['code']==='INTEGRATED') {
			html += "<td class='menuTd'>" + onekey['name']+" : " +
					"<select id='"+onekey['code']+"' value='"+onevalue+"'" +
							" name='lteCellUpdateResult."+onekey['code']+"'> ";
					if(oneLteCellDetail[onekey['code']]==="是") {
						html += "<option value='是' selected='selected'>是</option> " +
								"<option value='否'>否</option> ";
					} else {
						html += "<option value='是'>是</option> " +
								"<option value='否' selected='selected'>否</option> ";
					}
		    html += "</select> "+
					"</td> ";
		} //加载日期标签
		else if(onekey['code']==='OPERATION_TIME') {
			html += "<td class='menuTd'>" + onekey['name']+" : " +
					"<input class='Wdate input-text' type='text' style='width: 132px;' " +
							"readonly='' onfocus='WdatePicker({dateFmt:\"yyyy-MM-dd\"})' " +
							"value='"+onevalue+"' " +
							"name='lteCellUpdateResult."+onekey['code']+"' id='"+onekey['code']+"' > " +
					"</td>";
		} //不加载小区标识
		/*else if(onekey['code']==='LOCAL_CELLID') {
			html +="";
		} */
		//加载input标签
		else {
			html += "<td class='menuTd'>" + onekey['name']+" : " +
						"<input id='"+onekey['code']+"' "+
							 "name='lteCellUpdateResult."+onekey['code']+"' "+
							 "value='"+onevalue+"' " +
							 "onkeyup='checkOnkeyupDateType(this)'/>" +
							 "</br><span id='span"+onekey['code']+"'  style='font-family:华文中宋; color:red;width:100px;display:block;'></span>" +
					"</td>";
		}
	}
	if (size % 3 > 0) {
		for ( var i = 0; i < 3 - size % 3; i++) {
			html += "<td class='menuTd'></td>";
		}
	}
	html+="</tr>";
	//console.log(html);
	$("span#editVIEW_ENODEB_NAME").html(getValidValue(oneLteCellDetail['ENODEB_NAME']));
	$("span#editVIEW_CELL_NAME").html(getValidValue(oneLteCellDetail['CELL_NAME']));
	$("#editCellDetailTable").html(html);
}

/*
 *输入框输入完成后触发验证
 */
function checkOnkeyupDateType(e) {
	var id = e.id;  //获取元素的id
	var value = e.value;  //获取元素的值
	checkDateType(id,value);
}

/*
 * 对单个输入框输入完成后的数据验证
 * @param 元素id值
 * @param 元素value值
 */
function checkDateType(id,value) {

	//验证需要的正则表达式
	var vali1 = /[^A-Za-z0-9]/g;   //输入数字和英文字母
	var vali2 = /[^\d.]/g; 		  //输入数字和小数点
	var vali3 = /[^\d:]/g;		   //输入数字和：
	var vali4 = /[^\d-]/g;    /*/^[-+]?[0-9]*\.?[0-9]+$/;*/	//输入正负浮点数
	var vali5 = /[^\d-.]/g;    //输入数字,负号，小数点

	//----------需要验证的参数 start-------------//
	//经度的验证
	if(id==='LONGITUDE') {
		if(135.04166666667<value || value<73.666666666667){
			$("span#span"+id).html("中国经度范围东经73.666666666667-<br>135.04166666667");
			submitOK = false;
		}else if(vali2.test(value)) {
			//console.log("不合法");
			$("span#span"+id).html("请输入数字或者小数点");
			submitOK = false;
		} else {
			//console.log("合法");
			$("span#span"+id).html("");
			submitOK =true;
		}
	}
	//纬度的验证
	if(id==='LATITUDE') {
		if(53.55<value || value<3.8666666666667){
			$("span#span"+id).html("中国纬度范围北纬3.8666666666667-<br>53.55");
			submitOK = false;
		}else if(vali2.test(value)) {
			//console.log("不合法");
			$("span#span"+id).html("请输入数字或者小数点");
			submitOK = false;
		} else {
			//console.log("合法");
			$("span#span"+id).html("");
			submitOK =true;
		}
	}
	//开站配比，子帧分配，特殊子帧的验证
	if(id==='STATION_CFG' || id==='FRAME_CFG' || id==='SPECIAL_FRAME_CFG') {
		if(vali3.test(value)) {
			//console.log("不合法");
			$("span#span"+id).html("请输入数字或者：符号");
			submitOK = false;
		} else {
			//console.log("合法");
			$("span#span"+id).html("");
			submitOK =true;
		}
	}
	//PA，PB的验证
	if(id==='PA' || id==='PB') {
		if(vali4.test(value)) {
			//console.log("不合法");
			$("span#span"+id).html("请输入正确的格式");
			submitOK = false;
		} else {
			//console.log("合法");
			$("span#span"+id).html("");
			submitOK =true;
		}
	}
	//覆盖类型的验证
	if(id==='COVER_TYPE'){
		if(value=="indoor" || value=="outdoor"){
			//console.log("不合法");
			$("span#span"+id).html("");
			submitOK = true;
		}else if(vali2.test(value)) {
			$("span#span"+id).html("类型是“indoor”或者“outdoor”");
			submitOK = false;
		}
	}
	//数字输入的验证
	if(id==='PCI' || id==='GROUND_HEIGHT' || id==='AZIMUTH'
		|| id==='M_DOWNTILT' || id==='RSPOWER' || id==='E_DOWNTILT'
		|| id==='TAC'|| id==='RRUNUM'|| id==='TAL'|| id==='CELL_RADIUS'
		|| id==='EARFCN' || id==='PDCCH' || id==='DOWNTILT') {
		if(vali5.test(value)) {
			//console.log("不合法");
			$("span#span"+id).html("请输入正确的格式");
			submitOK = false;
		} else {
			//console.log("合法");
			$("span#span"+id).html("");
			submitOK =true;
		}
	}
	//----------需要验证的参数 end-------------//
}

/**
 * 更新修改后的Lte小区详情
 *
 * @param submitOK
 */
function updateLteCellAndCoSiteCellDetail() {
	 if(submitOK) {
	 	 $("#lteCellDetailForm")
			.ajaxSubmit({
					url : "updateLteCellAndCoSiteCellDetailForAjaxAction",
					dataType : 'text',
					success : function(raw) {

							var data=null;
							try{
								data=eval("("+raw+")");
								if(!data){
									alert("未知错误！请联系管理员！");
								}else{
									if(data['flag']==true){
										//
										alert("更新成功！");
										$("#editLteCellMessage").hide();
									}else{
										alert("更新失败！");
									}
								}
							}catch(err){
							    alert("未知错误！请联系管理员！");
							}
					},
					complete : function() {
						//alert("更新完成");
						$(".loading_cover").css("display", "none");
					}
			});
	 }
}

/**
 * 显示队列里的第几个，从0开始
 *
 * @param index
 */
function showOneCellDetail(index) {
	if (!currentCellArray) {
		return;
	}
	if (index < 0) {
		return;
	}
	if (currentCellArray.length <= index) {
		index = 0;
	}
	var cell = currentCellArray[index];

	// 设置值
	$("#lteCellDetailDiv").find("span#VIEW_ENODEB_NAME").html(
			getValidValue(cell['ENODEB_NAME']));
	$("#lteCellDetailDiv").find("span#VIEW_CELL_NAME").html(
			getValidValue(cell['CELL_NAME']));

	// 循环设置
	var html = '';
	var size = chineseToCode.length;
	var onekey;
	for ( var i = 0; i < size; i++) {
		if (i % 3 == 0) {
			// 换行
			if (i > 0) {
				html += "</tr>";
			}
			html += "<tr>";

		}
		onekey = chineseToCode[i];
		html += "<td class='menuTd'>" + onekey['name']+" : "+getValidValue(cell[onekey['code']])+"</td>";
	}
	if (size % 3 > 0) {
		for ( var i = 0; i < 3 - size % 3; i++) {
			html += "<td class='menuTd'></td>";
		}
	}
	html+="</tr>";

	//console.log("html="+html);
	$("#viewCellDetailTable").html(html);


	$("#lteCellDetailDiv").css("display","block");
}