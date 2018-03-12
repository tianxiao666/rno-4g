var rnoFileUpload;
$(document).ready(
		function() {
			rnoFileUpload = new RnoFileUpload("formImportInterference",
					["importAndLoadBtn","importBtn"], 5, "importResultDiv", null, function(
							result) {
						if (!result) {
							return;
						}
						var data = "";
						try {
							data = eval("(" + result + ")");
							//刷新按钮
						$("#refreshLoadedBtn").click();
						} catch (err) {
							$("#importResultDiv").html(result);
							return;
						}
						$("#importResultDiv").html(
								data['msg'] ? data['msg'] : "");
						$("#loadlistDiv").css("display","block");
						//showLoadedList("loadlistTable1", data['list']);
					});
			$.validator.addMethod("tempnamerule", function(value, element,
					params) {
				if ($("#updatetrueradio").attr("checked")) {
					if ($.trim(value) == "") {
						return false;
					} else {
						return true;
					}
				} else {
					return true;
				}
			}, "必须提供临时分析方案的名称！");
			// 上传表单绑定验证
			$("#formImportInterference").validate({
				rules : {
					"attachParams['name']" : 'tempnamerule'
				},
				errorPlacement : function(error, element) {
					if (element.attr("id") == "tempname") {
						error.insertAfter(element);
					} else {
						element.parent().append(error);
					}
				},
				// debug : true,
				submitHandler : function(form) {
					importInterferenceData();
				}
			});

			// 导入和加载
			$("#importAndLoadBtn").click(function() {
				$("#autoload").val("true");
				$("#importResultDiv").html("");
				$("#formImportInterference").submit();
			});
			// 仅导入
			$("#importBtn").click(function() {
				$("#autoload").val("false");
				$("#importResultDiv").html("");
				$("#formImportInterference").submit();
			});

			$("#conditionForm").validate({
				errorPlacement : function(error, element) {
					$(element).parent().append(error);
				},
				submitHandler : function(form) {
					queryAndloadToAnalysis();
				}
			});
			// 从系统中查询加载
			$("#queryAndLoadBtn").click(function() {
				$("#conditionForm").submit();
				//刷新按钮
				$("#refreshLoadedBtn").click();
			});

			// 区域联动
			initAreaCascade();
			//刷新按钮
	$("#refreshLoadedBtn").click(function(){
			$.ajax({
				url : 'refreshQueryAndLoadInterferenceListForAjaxAction',
				dataType : 'text',
				//data:sendDate,
				type : 'post',
				success : function(data) {
				   var mes_obj=eval("("+data+")");
					//console.log(mes_obj);
					showRefreshLoadedList("loadRefreshlistTable3",mes_obj);//加载数据 
				},
				error : function(err, status) {
					//console.error(status);
				},
				complete : function() {
					$(".loading_cover").css("display", "none");
				}
			});
	});
	//点击选中系统配置方案按钮则填充方案名称输入框并不允许修改
	var a=$("#systemconfigure").val();
	//var a=$("#systemconfigure").val();
	//console.log(a);
	$("#schemeName").val(a);
	$("#schemeName").attr("readonly","readonly");
	$("#systemradio").click(function(){
	//点击选中系统配置方案按钮则填充方案名称输入框并不允许修改
	var a=$("#systemconfigure").val();
	//var a=$("#systemconfigure").val();
	$("#schemeName").val(a);
	$("#schemeName").attr("readonly","readonly");
	});
	$("#tempradio").click(function(){
		$("#schemeName").removeAttr("readonly");
		$("#schemeName").val("");
	});
	// 从分析列表中删除
			$("#removeFromAnalysis").click(function() {
				removeFromInterferenceList();
			});
		});

/**
 * 导入
 */
function importInterferenceData() {
	//console.log("准备提交文件");
	//alert($("input[name=\"attachParams['collectType']\"]").val());
	//var type=$("input[name=\"attachParams['collectType']\"]").val();
	//return;
	$("#loadlistDiv").css("display","none");
	try {
		rnoFileUpload.upload();
	} catch (err) {
		//console.log(err);
	}
}

function initAreaCascade() {
	// 区域联动事件
	$("#provinceId").change(function() {
		getSubAreas("provinceId", "cityId", "市");
	});

	$("#cityId").change(function() {
		getSubAreas("cityId", "areaId", "区/县");
	});

	$("#provinceId2").change(function() {
		getSubAreas("provinceId2", "cityId2", "市");
	});

	$("#cityId2").change(function() {
		getSubAreas("cityId2", "areaId2", "区/县");
	});

	// 区域id2改变以后，需要去获取相应的干扰配置数据
	$("#areaId2").change(
			function() {
				var areaId = $("#areaId2").val();

				$("#systeminterferencedesc").removeAttr("checked");
				$("#systeminterferencedesc").attr("disabled", "disabled");
				$("#systemConfigId").val(-1);// 先清除系统干扰选项
				$("#errForSystem").css("display", "block");

				$("#tempinterferencelist").removeAttr("checked");
				$("#tempinterferencelist").attr("disabled", "disabled");
				$("#templist").html("");// 清除临时干扰选项
				$("#errForTemp").css("display", "block");
				$.ajax({
					url : 'getInterferenceDescriptorInAreaForAjaxAction',
					data : {
						'areaId' : areaId
					},
					dataType : 'json',
					success : function(data) {
						if (!data) {

						} else {
							var item;
							var html = "";
							for ( var i = 0; i < data.length; i++) {
								item = data[i];
								if (item['defaultDescriptor'] == "Y") {
									$("#systemConfigId").val(
											item['interDescId']);
									// 将错误提示层隐藏
									$("#errForSystem").css("display", "none");
									$("#systeminterferencedesc").removeAttr(
											"disabled");
								} else {
									html += "<option value='"
											+ item['interDescId'] + "'>"
											+ item['name'] + "</option>";
									$("#errForTemp").css("display", "none");
									$("#tempinterferencelist").removeAttr(
											"disabled");
								}
							}
							$("#templist").html(html);//
						}
					}
				});
			});
};

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
	var htmlstr = "<tr><td colspan=\"5\" style=\"background: none repeat scroll 0 0  #E8EDFF;\">&nbsp; </td></tr>";// 表头
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
					+ item['title']
					+ "</span>"
					+ "  </td>"
					+ "  <td  width=\"20%\"  class=\"bd-right-white td_nowrap\">"
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
					+ "  <input type=\"button\" value=\"移除\" onclick=\"removeFromAnalist(this,\'"
					+ item['configId']
					+ "\')\" />"
					+ "  </td >"
					+ "  </tr>"
					+ "<tr><td colspan=\"5\" style=\"background-color: #e7e7e7; height:1px; width:100%\"></td> </tr> ";
		}
	}
	//console.log("htmlstr=="+htmlstr)
	$("#" + tableId).html(htmlstr);// table html 内容
}
/**
 * 
 * 刷新按钮显示加载的分析列表
 * 
 * @param tableId
 *            table id 值
 * @param data
 *            数据数组
 * @author chao.xj 2013-10-28
 */
function showRefreshLoadedList(tableId, data) {
	if (!data) {
		return;
	}
	//<tr><td colspan=\"5\" style=\"background: none repeat scroll 0 0  #E8EDFF;\">&nbsp; </td></tr>
	var htmlstr = "<tr><th>标题</th><th>方案名称</th><th>收集日期</th><th style=\"width: 10%\">全选<input type=\"checkbox\" onclick=\"javascript:operAllCheckbox(this,3);\" name=\"selectall\" id=\"2\" /></th></tr>";// 表头
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
					+ item['title']
					+ "</span>"
					+ "  </td>"
					+ "  <td  width=\"20%\"  class=\"bd-right-white td_nowrap\">"
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
					//+ "  <input type=\"button\" value=\"移除\" onclick=\"removeFromAnalist(this,\'"+ item['configId']+ "\')\" />"
					+ "	<input type='checkbox' id='"+ data[i]['configId']+ "' class='forcheck' />";
					+ "  </td >"
					+ "  </tr>"
					+ "<tr><td colspan=\"5\" style=\"background-color: #e7e7e7; height:1px; width:100%\"></td> </tr> ";
		}
	}
	//console.log("htmlstr=="+htmlstr)
	$("#" + tableId).html(htmlstr);// table html 内容
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
		url : 'removeInterferenceItemFromLoadedListForAjaxAction',
		data : {
			'loadedConfigId' : configId
		},
		dataType : 'text',
		type : 'post',
		success : function(data) {
			var p = $(obj).parent().parent();
			$(p).remove();
		}
	});
}

/**
 * 将选择的干扰配置项加载进干扰分析列表
 * 
 * @returns
 */
function queryAndloadToAnalysis() {
	$("#loadlistTable2").html("");
	var areaName = $.trim($("#areaId2").find("option:selected").text());
	// alert("areaName=="+areaName);
	$("#conditionForm").ajaxSubmit({
		url : 'queryAndLoadInterferenceListForAjaxAction',
		data : {
			'areaName' : areaName
		},
		type : 'post',
		dataType : 'text',
		success : function(result) {
			var data=eval("("+result+")");
			if (data['flag'] == true) {
				showLoadedList("loadlistTable2", data['list']);
			} else {
				alert("加载失败！" + getValidValue(data['msg']));
			}
		},
		error : function(xhr, text, e) {
			//console.error(text);
		},
		complete : function() {
					$(".loading_cover").css("display", "none");
				}
	});
}
//显示操作提示信息
function showOperTips(tip){
	$("#operInfo").css("display","");
	$("#operInfo").find("#operTip").html(tip);
}

function animateOperTips(tip){
	$("#operInfo").find("#operTip").html(tip);
	animateInAndOut("operInfo", 500, 500, 2000);
}

function hideOperTips(){
	$("#operInfo").css("display","none");
}
/**
 * 从分析列表中删除
 */
function removeFromInterferenceList() {
	var ids = new Array();
	var trs = new Array();
	$("#loadRefreshlistTable3").find("input.forcheck:checked").each(function(i, ele) {
		ids.push($(ele).attr("id"));
		trs.push($(ele).closest("tr"));
	});

	if (ids.length == 0) {
		// layer.msg("请先选择需要删除的邻区关系",2,2);
		alert('请先选择需要从分析列表中删除的interference');
		return;
	}

	var ncsids = ids.join(",");
	// alert(ncellids);

	$(".loading_cover").css("display", "block");
	$("#form_tab_2").ajaxSubmit({
		url : 'removeInterferenceItemsFromLoadedListForAjaxAction',
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
				animateOperTips("删除成功。");
			} else {
				//alert("删除失败！");
				animateOperTips("删除失败！");
			}
		},
		complete : function() {
			$(".loading_cover").css("display", "none");
		}
	});
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
		$("#loadRefreshlistTable" + seq + " .forcheck").attr("checked", "checked");
	} else {
		$("#loadRefreshlistTable" + seq + " .forcheck").removeAttr("checked");
	}
}