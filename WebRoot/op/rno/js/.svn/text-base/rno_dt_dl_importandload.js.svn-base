var rnoFileUpload;
var firstTime=true;
$(document)
		.ready(
				function() {
					// 文件上传
					rnoFileUpload = new RnoFileUpload("formImportDT", [
							"importAndLoadBtn", "importBtn" ], 5,
							"importResultDiv", null, function(result) {
								if (!result) {
									return;
								}
								var data = "";
								try {
									data = eval("(" + result + ")");
								} catch (err) {
									$("#importResultDiv").html(result);
									return;
								}
								$("#importResultDiv").html(
										data['msg'] ? data['msg'] : "");
								if(data['list']){
									$("#refreshLoadedBtn").trigger("click");
								}
								//showLoadedList("loadlistTable1", data['list']);
//								console.log("loaded list = "+data['list']);
//								showLoadedList("loadlistTable1", data['list']);
//								if(data['list']){
//									$("#loadlistDiv").css("display","block");
//								}
							});

					// 导入form验证
					$("#formImportDT")
							.validate(
									{
										messages : {
											'file':"请选择Dt测试文件",
											"attachParams['name']":"请输入测试任务名称"
											
										},
										errorPlacement : function(error,
												element) {
												element.parent().append(error);
										},
										// debug : true,
										submitHandler : function(form) {
											try {
												$("#loadlistDiv").css("display","none");
												rnoFileUpload.upload();
											} catch (err) {
												//console.log(err);
											}
										}
									});

					// 导入按钮
					$("#importAndLoadBtn").click(function() {
						$("#autoload").val("true");
						//$("#loadlistTable1").html("");
						//$("#loadlistTable1").css("display", "none");
						$("#formImportDT").submit();
					});

					$("#importBtn").click(function() {
						$("#autoload").val("false");
						//$("#loadlistTable1").html("");
						//$("#loadlistTable1").css("display", "none");
						$("#formImportDT").submit();
					});
					initAreaCascade();

					// 查询条件
					$("#conditionForm").submit(function() {
						// 重新初始化分页参数
						$("#hiddenPageSize").val("25");
						$("#hiddenCurrentPage").val("1");
						$("#hiddenTotalPageCnt").val("0");
						$("#hiddenTotalCnt").val("0");
						getResource();
						return false;
					});

					// 添加到分析列表
					$("#addToAnalysis").click(function() {
						addItemToAnalysis();
					});

					// 从分析列表中删除
					$("#removeFromAnalysis").click(function() {
						removeFromLoadedList("queryResultTab2");
					});
					// 点击li控件，进行触发
					$("#loadedLi").click(function() {
						if (firstTime) {
							firstTime = false;
							getAllLoadedList();
						}
					});

					$("#refreshLoadedBtn").click(
							function() {
								$("#queryResultTab2").find(
										"input[name=selectall]").removeAttr(
										"checked");
								getAllLoadedList();
							});
					//获取
					$("#refreshLoadedBtn").trigger("click");
				});

function initAreaCascade() {
	// 区域联动事件
	$("#provinceId1").change(function() {
		getSubAreas("provinceId1", "cityId1", "市");
	});

	$("#cityId1").change(function() {
		getSubAreas("cityId1", "areaId1", "区/县");
	});
	
	$("#areaId1").change(function() {
		var areaName = $("#areaId2").find("option:selected").text();
		$("#hiddenAreaName").val(areaName);
	});

	$("#provinceId2").change(function() {
		getSubAreas("provinceId2", "cityId2", "市");
	});

	$("#cityId2").change(function() {
		getSubAreas("cityId2", "areaId2", "区/县");
	});
}

/**
 * 
 * 显示加载的分析列表
 * 
 * @param tableId
 *            table id 值
 * @param data
 *            数据数组
 * @author yuan.yw 2013-10-28
 */
function showLoadedList(tableId, data) {
	if (!data) {
		return;
	}
	var htmlstr = "<tr><td colspan=\"8\" style=\"background: none repeat scroll 0 0  #E8EDFF;\">&nbsp; </td></tr>";// 表头
	var item;
	var trClass = "";
	for ( var i = 0; i < data.length; i++) {
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
					+ "  <td width=\"45%\" class=\"bd-right-white\" >    "
					+ "  <span >"
					+ item['obj']['name']
					+ "</span>"
					+ "  </td>"
					 + " <td width=\"20%\" class=\"bd-right-white td_nowrap\">"
					 + " <span >"
					 + item['obj']['netMode']
					 + "</span>"
					 + " </td>"
					+ "  <td  width=\"20%\"  class=\"td-standard-date bd-right-white td_nowrap\">"
					+ "  <span >"
					+ item['obj']['type']
					+ "</span>"
					+ "  </td>"
					+"<td  width=\"20%\"  class=\"td-standard-date bd-right-white td_nowrap\">"
					+ "  <span >"
					+ item['testDate']
					+ "</span>"
					+ "  </td>"
					+"<td  width=\"20%\"  class=\"td-standard-date bd-right-white td_nowrap\">"
					+ "  <span >"
					+ item['obj']['vendor']
					+ "</span>"
					+ "  </td>"
					+"<td  width=\"20%\"  class=\"td-standard-date bd-right-white td_nowrap\">"
					+ "  <span >"
					+ item['obj']['device']
					+ "</span>"
					+ "  </td>"
					+"<td  width=\"20%\"  class=\"td-standard-date bd-right-white td_nowrap\">"
					+ "  <span >"
					+ item['obj']['version']
					+ "</span>"
					+ "  </td>"
					+ "  <td width=\"10%\">"
					+ "  <input type=\"button\" value=\"移除\" onclick=\"removeFromAnalist(this,\'"
					+ item['configId']
					+ "\')\" />"
					+ "  </td >"
					+ "  </tr>"
					+ "<tr><td colspan=\"5\" style=\"background-color: #e7e7e7; height:1px; width:100%\"></td> </tr> ";
		}
	}
	// console.log("htmlstr=="+htmlstr)
	$("#" + tableId + " tr:not(:first)").each(function(i, ele) {
		$(ele).remove();
	});
	$("#" + tableId).append(htmlstr);// table html 内容
	$("#" + tableId).css("display", "");
}

/**
 * 从分析列表删除指定的项
 * 
 * @param obj
 * @param configId
 * @returns
 */
function removeFromAnalist(obj, configId) {
	$.ajax({
		url : 'removeDtItemFromAnalysisListForAjaxAction',
		data : {
			'configIds' : configId
		},
		dataType : 'text',
		type : 'post',
		success : function(data) {
			var p = $(obj).closest("tr");
			var table = $(obj).closest("table");
			$(p).remove();
			if (table.children().length == 0) {
				table.css("display", "none");
			}
		}
	});
}

/**
 * 获取
 */
function getResource() {
	$(".loading_cover").css("display", "block");
	$("#conditionForm").ajaxSubmit({
		url : 'queryDtDescriptorByPageWithConditionForAjaxAction',
		dataType : 'text',
		success : function(data) {
			// console.log(data);
			showQueryResult(data);
		},
		complete : function() {
			$(".loading_cover").css("display", "none");
		}
	});
}

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
	getResource();
}

function showQueryResult(data) {

	data = eval("(" + data + ")");
	// console.log("data===" + data);
	// 准备填充ncs
	var page = data['page'];
	var ncslist = data['list'];

	// console.log("page===" + page);
	if (page) {
		var pageSize = page['pageSize'] ? page['pageSize'] : 0;
		$("#hiddenPageSize").val(pageSize);

		var currentPage = page['currentPage'] ? page['currentPage'] : 1;
		$("#hiddenCurrentPage").val(currentPage);

		var totalPageCnt = page['totalPageCnt'] ? page['totalPageCnt'] : 0;
		$("#hiddenTotalPageCnt").val(totalPageCnt);

		var totalCnt = page['totalCnt'] ? page['totalCnt'] : 0;
		$("#hiddenTotalCnt").val(totalCnt);

		// 跳转
		$("#emTotalCnt").html(totalCnt);
		$("#showCurrentPage").val(currentPage);
		$("#emTotalPageCnt").html(totalPageCnt);

	}

	var table = $("#queryResultTab");
	// 只保留表头
	// table.append(getHead());
	$("#queryResultTab tr:not(:first)").each(function(i, ele) {
		$(ele).remove();
	});
	// console.log("celllist====" + celllist);
	if (ncslist) {
		var one;
		var tr;
		for ( var i = 0; i < ncslist.length; i++) {
			// console.log("i==" + i);
			// CELL 小区中文名 LAC CI ARFCN BSIC TCH 操作
			one = ncslist[i];
			if (!one) {
				//console.log("one is null!");
				continue;
			}

			tr = "<tr class=\"greystyle-standard-whitetr\">";
			tr += "<td>" + getValidValue(one['NAME']) + "</td>";
			tr += "<td>" + getValidValue(one['NET_MODE']) + "</td>";
			tr += "<td>" + getValidValue(one['TYPE']) + "</td>";
			tr += "<td>" + getValidValue(one['TIMESTR']) + "</td>";
			tr += "<td>" + getValidValue(one['VENDOR']) + "</td>";
			tr += "<td>" + getValidValue(one['DEVICE']) + "</td>";
			tr += "<td>" + getValidValue(one['VERSION']) + "</td>";
			tr += "<td>	<input type='checkbox' id='"
					+ getValidValue(one['DT_DESC_ID'])
					+ "' class='forcheck' /></td>";

			tr += "</tr>";
			// console.log("tr===" + tr);
			table.append($(tr));// 增加
		}
	}

}

function operAllCheckbox(obj, seq) {
	var check = $(obj).attr("checked");
	if (check == true || check == "checked") {
		check = true;
	} else {
		check = false;
	}

	if (!seq) {
		seq = "";
	}
	if (check) {
		$("#queryResultTab" + seq + " .forcheck").attr("checked", "checked");
	} else {
		$("#queryResultTab" + seq + " .forcheck").removeAttr("checked");
	}
}

// 将选择了的ncs项加入到分析列表
function addItemToAnalysis() {
	var ids = new Array();
	var trs = new Array();
	$("#queryResultTab").find("input.forcheck:checked").each(function(i, ele) {
		ids.push($(ele).attr("id"));
		// trs.push($(ele).closest("tr"));
	});

	if (ids.length == 0) {
		// layer.msg("请先选择需要删除的邻区关系",2,2);
//		alert('请先选择需要添加到分析列表的dt数据');
		animateInAndOut("operInfo",500,500,1000,"operTip","请先选择需要添加到分析列表的dt数据");
		return;
	}

	var ncsids = ids.join(",");
	// alert(ncsids);
	var areaName = $("#areaId2").find("option:selected").text();

	$(".loading_cover").css("display", "block");
	$("#conditionForm").ajaxSubmit({
		url : 'addDtItemToListForAjaxAction',
		dataType : 'text',
		data : {
			'configIds' : ncsids,
			"areaName" : areaName
		},
		success : function(data) {
			if (data == "true") {
//				alert("添加成功。");
				animateInAndOut("operInfo",500,500,1000,"operTip","添加成功。");
				$("#refreshLoadedBtn").trigger("click");
			} else {
				alert("添加失败！");
			}
		},
		complete : function() {
			$(".loading_cover").css("display", "none");
		}
	});
}

/**
 * 获取已加载的ncs分析列表
 */
function getAllLoadedList() {
	$(".loading_cover").css("display", "block");
	$.ajax({
		url : 'getAllLoadedDtListForAjaxAction',
		dataType : "json",
		type : 'post',
		success : function(data) {
			bindLoadedListToTable("queryResultTab2",data);
		},
		complete : function() {
			$(".loading_cover").css("display", "none");
		}
	});
}

/**
 * 将已经加载的ncs分析列表到页面
 * 
 * @param data
 */
function bindLoadedListToTable(tableId,data) {
	var table = $("#"+tableId);
	// 只保留表头
	// table.append(getHead());
	$("#"+tableId+" tr:not(:first)").each(function(i, ele) {
		$(ele).remove();
	});
	// console.log("celllist====" + celllist);
	if (data) {
		var one;
		var tr;
		for ( var i = 0; i < data.length; i++) {
			// console.log("i==" + i);
			// CELL 小区中文名 LAC CI ARFCN BSIC TCH 操作
			one = data[i];
			if (!one) {
				//console.log("one is null!");
				continue;
			}

			tr = "<tr class=\"greystyle-standard-whitetr\">";
			tr += "<td>" + getValidValue(one['obj']['name']) + "</td>";
			tr += "<td>" + getValidValue(one['obj']['netMode']) + "</td>";
			tr += "<td>" + getValidValue(one['obj']['type']) + "</td>";
			tr += "<td>" + getValidValue(one['obj']['testDate']) + "</td>";
			tr += "<td>" + getValidValue(one['obj']['vendor']) + "</td>";
			tr += "<td>" + getValidValue(one['obj']['device']) + "</td>";
			tr += "<td>" + getValidValue(one['obj']['version']) + "</td>";
			tr += "<td>	<input type='checkbox' id='"
					+ getValidValue(one['configId'])
					+ "' class='forcheck' /></td>";

			tr += "</tr>";
			// console.log("tr===" + tr);
			table.append($(tr));// 增加
		}
	}
}

/**
 * 从分析列表中删除
 */
function removeFromLoadedList(tableId) {
	var ids = new Array();
	var trs = new Array();
	$("#"+tableId).find("input.forcheck:checked").each(function(i, ele) {
		ids.push($(ele).attr("id"));
		trs.push($(ele).closest("tr"));
	});

	if (ids.length == 0) {
		// layer.msg("请先选择需要删除的邻区关系",2,2);
//		alert('请先选择需要从分析列表中删除的dt数据');
		animateInAndOut("operInfo",500,500,2000,"operTip","请先选择需要从分析列表中删除的dt数据");
		return;
	}

	var ncsids = ids.join(",");
	// alert(ncellids);

	$(".loading_cover").css("display", "block");
	$("#conditionForm").ajaxSubmit({
		url : 'removeDtItemFromAnalysisListForAjaxAction',
		dataType : 'text',
		data : {
			'configIds' : ncsids
		},
		success : function(data) {
			if (data == "true") {
				for ( var i = 0; i < trs.length; i++) {
					trs[i].remove();
				}
				trs = null;
				//alert("删除成功。");
				animateInAndOut("operInfo",500,500,2000,"operTip","删除成功！");
			} else {
				alert("删除失败！");
			}
		},
		complete : function() {
			$(".loading_cover").css("display", "none");
		}
	});
}