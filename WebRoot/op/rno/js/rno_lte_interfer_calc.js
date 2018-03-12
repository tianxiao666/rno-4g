//点击td转换可编辑
var editHTML;
var editText;

// 上传文件
var stopQueryProgress = false;// /停止查询进度

$(document).ready(function() {
					// 切换区域
					initAreaCascade();
					// 默认加载结构分析
					// getStructureTask();
					$("#searchStructureTask").click(function() {
						$("span#nameErrorText").html("");
						var taskName = $.trim($("#taskName").val());
						if (ifHasSpecChar(taskName)) {
							$("span#nameErrorText").html("含有特殊字符");
							return;
						}
						getStructureTask();
					});

					// 重定向至新增任务页面
					$("#addTask").click(function() {
						var cityId = $("#cityId2").val();
						location.href = 'stepByStepOperateLteInterCalcTaskInfoPageForAjaxAction?cityId='	+ cityId;
						});
					/**
					 * 任务消息页面的下一步点击事件
					 */
					$("#taskInfoNextStep").click(function() {
						storageTaskInfoForSession();
					});
					/**
					 * 参数配置页面的上一步点击事件
					 */
					$("#paramInfoPreStep").click(function() {
						fromParamInfoToTaskInfoPage();
					});
					/**
					 * 参数配置页面的下一步点击事件
					 */
					$("#paramInfoNextStep").click(function() {
						fromParamInfoToOverviewInfoPage();
					});
					/**
					 * overview消息页面上一步点击事件
					 */
					$("#overviewInfoPreStep").click(function() {
						// 主要跳转至mrr消息页面
						fromOverviewInfoToParamInfoPage();
					});
					/**
					 * 提交任务分析
					 */
					$("#submitTask").click(	function() {
						if(pciCellValCheck()){
							// eriAndHwStructureAnalysis();
							var calcType = $("input:radio[name='calcType']:checked")	.val();
								// console.log(calcType);
							if (calcType == "dbType") {
									// 触发任务提交action
								pciPlanAnalysis();
								} else {
									// 上传文件action
									uploadMatrixForPciPlanAnalysis();
									}
							}
						});
					/**
					 * 取消任务
					 */
					$("#cancleTask").click(function() {
						cancleNcsAndMrrTask();
					});
					/**
					 * 导入干扰矩阵时自动选中
					 */
					$("#fileid").click(function(){
						$("#pciPlanImport").attr("checked","checked");
					});
					// 绑定td点击事件
					$(".editbox").each(function() { // 取得所有class为editbox的对像
						$(this).bind("click", function() { // 给其绑定单击事件
							var objId = $(this).attr("id");
							$("span#" + objId).html("");
							editText = $(this).html().trim(); // 取得表格单元格的文本
							// console.log(editText);
							setEditHTML(editText); // 初始化控件
							$(this).data("oldtxt", editText) // 将单元格原文本保存在其缓存中，便修改失败或取消时用
							.html(editHTML) // 改变单元格内容为编辑状态
							.unbind("click"); // 删除单元格单击事件，避免多次单击
							$("#editTd").focus();
							/*
							 * $("#editTd").focus(function(){
							 * if(objId=="SAMEFREQCELLCOEFWEIGHT"){ var
							 * samefreq=editText;
							 * console.log("samefreq"+samefreq);
							 * $("#SWITCHRATIOWEIGHT").text(1-Number(samefreq)); }
							 * if(objId=="SWITCHRATIOWEIGHT"){ var
							 * switchrat=editText;
							 * console.log("switchrat"+switchrat);
							 * $("#SAMEFREQCELLCOEFWEIGHT").text(1-Number(switchrat)); }
							 * });
							 */

						});
					});
				});

// 参数配置页面的上一步点击事件
function fromParamInfoToTaskInfoPage() {

	var SAMEFREQCELLCOEFWEIGHT = $("#SAMEFREQCELLCOEFWEIGHT").html().trim();
	var SWITCHRATIOWEIGHT = $("#SWITCHRATIOWEIGHT").html().trim();
	var CELLM3RINTERFERCOEF = $("#CELLM3RINTERFERCOEF").html().trim();
	var CELLM6RINTERFERCOEF = $("#CELLM6RINTERFERCOEF").html().trim();
	var CELLM30RINTERFERCOEF = $("#CELLM30RINTERFERCOEF").html().trim();
	var BEFORENSTRONGCELLTAB = $("#BEFORENSTRONGCELLTAB").html().trim();
	var TOPNCELLLIST = $("#TOPNCELLLIST").html().trim();
	var INCREASETOPNCELLLIST = $("#INCREASETOPNCELLLIST").html().trim();
	var CONVERMETHOD1TARGETVAL = $("#CONVERMETHOD1TARGETVAL").html().trim();
	var CONVERMETHOD2TARGETVAL = $("#CONVERMETHOD2TARGETVAL").html().trim();
	var CONVERMETHOD2SCOREN = $("#CONVERMETHOD2SCOREN").html().trim();
	var MINCORRELATION = $("#MINCORRELATION").html().trim();
	var MINMEASURESUM = $("#MINMEASURESUM").html().trim();

	// 验证数据
	if (!checkThreshold()) {
		return;
	}

	showOperTips("loadingDataDiv", "loadContentId", "正在跳转页面");
	$.ajax({
				url : 'storageLteInterferCalcTaskObjInfoForAjaxAction',
				data : {
					'taskInfoType' : 'paramInfoBack',
					'threshold.SAMEFREQCELLCOEFWEIGHT' : SAMEFREQCELLCOEFWEIGHT,
					'threshold.SWITCHRATIOWEIGHT' : SWITCHRATIOWEIGHT,
					'threshold.CELLM3RINTERFERCOEF' : CELLM3RINTERFERCOEF,
					'threshold.CELLM6RINTERFERCOEF' : CELLM6RINTERFERCOEF,
					'threshold.CELLM30RINTERFERCOEF' : CELLM30RINTERFERCOEF,
					'threshold.BEFORENSTRONGCELLTAB' : BEFORENSTRONGCELLTAB,
					'threshold.TOPNCELLLIST' : TOPNCELLLIST,
					'threshold.INCREASETOPNCELLLIST' : INCREASETOPNCELLLIST,
					'threshold.CONVERMETHOD1TARGETVAL' : CONVERMETHOD1TARGETVAL,
					'threshold.CONVERMETHOD2TARGETVAL' : CONVERMETHOD2TARGETVAL,
					'threshold.CONVERMETHOD2SCOREN' : CONVERMETHOD2SCOREN,
					'threshold.MINCORRELATION' : MINCORRELATION,
					'threshold.MINMEASURESUM' : MINMEASURESUM
				},
				dataType : 'json',
				type : 'post',
				success : function(raw) {
					var data;
					try {
						data = eval("(" + raw + ")");
						// console.log("data.state:"+data.state);
						var state = data['state'];

					} catch (err) {

					}
				},
				complete : function() {
					// hideOperTips("loadingDataDiv");
					// 跳转新的页面
					location.href = "stepByStepOperateLteInterCalcTaskInfoPageForAjaxAction?taskInfoType=paramInfoBack";
				}
			});

}

// 参数配置页面的下一步点击事件
function fromParamInfoToOverviewInfoPage() {

	var SAMEFREQCELLCOEFWEIGHT = $("#SAMEFREQCELLCOEFWEIGHT").html().trim();
	var SWITCHRATIOWEIGHT = $("#SWITCHRATIOWEIGHT").html().trim();
	var CELLM3RINTERFERCOEF = $("#CELLM3RINTERFERCOEF").html().trim();
	var CELLM6RINTERFERCOEF = $("#CELLM6RINTERFERCOEF").html().trim();
	var CELLM30RINTERFERCOEF = $("#CELLM30RINTERFERCOEF").html().trim();
	var BEFORENSTRONGCELLTAB = $("#BEFORENSTRONGCELLTAB").html().trim();
	var TOPNCELLLIST = $("#TOPNCELLLIST").html().trim();
	var INCREASETOPNCELLLIST = $("#INCREASETOPNCELLLIST").html().trim();
	var CONVERMETHOD1TARGETVAL = $("#CONVERMETHOD1TARGETVAL").html().trim();
	var CONVERMETHOD2TARGETVAL = $("#CONVERMETHOD2TARGETVAL").html().trim();
	var CONVERMETHOD2SCOREN = $("#CONVERMETHOD2SCOREN").html().trim();
	var MINCORRELATION = $("#MINCORRELATION").html().trim();
	var MINMEASURESUM = $("#MINMEASURESUM").html().trim();

	// 验证数据
	if (!checkThreshold()) {
		return;
	}
	showOperTips("loadingDataDiv", "loadContentId", "正在统计区域PCI翻频方案任务信息");
	$.ajax({
				url : 'storageLteInterferCalcTaskObjInfoForAjaxAction',
				data : {
					'taskInfoType' : 'paramInfoForward',
					'threshold.SAMEFREQCELLCOEFWEIGHT' : SAMEFREQCELLCOEFWEIGHT,
					'threshold.SWITCHRATIOWEIGHT' : SWITCHRATIOWEIGHT,
					'threshold.CELLM3RINTERFERCOEF' : CELLM3RINTERFERCOEF,
					'threshold.CELLM6RINTERFERCOEF' : CELLM6RINTERFERCOEF,
					'threshold.CELLM30RINTERFERCOEF' : CELLM30RINTERFERCOEF,
					'threshold.BEFORENSTRONGCELLTAB' : BEFORENSTRONGCELLTAB,
					'threshold.TOPNCELLLIST' : TOPNCELLLIST,
					'threshold.INCREASETOPNCELLLIST' : INCREASETOPNCELLLIST,
					'threshold.CONVERMETHOD1TARGETVAL' : CONVERMETHOD1TARGETVAL,
					'threshold.CONVERMETHOD2TARGETVAL' : CONVERMETHOD2TARGETVAL,
					'threshold.CONVERMETHOD2SCOREN' : CONVERMETHOD2SCOREN,
					'threshold.MINCORRELATION' : MINCORRELATION,
					'threshold.MINMEASURESUM' : MINMEASURESUM
				},
				dataType : 'json',
				type : 'post',
				success : function(raw) {
					var data;
					try {
						data = eval("(" + raw + ")");
						// console.log("data.state:"+data.state);
						var state = data['state'];
					} catch (err) {
					}
				},
				complete : function() {
					// hideOperTips("loadingDataDiv");
					// 跳转新的页面
					location.href = "stepByStepOperateLteInterCalcTaskInfoPageForAjaxAction?taskInfoType=paramInfoForward";
				}
			});
}

// overview消息页面上一步点击事件
function fromOverviewInfoToParamInfoPage() {

	var lteCells = $("#pciCell").val().trim();

	// 验证数据
	if (false) {
		return;
	}

	showOperTips("loadingDataDiv", "loadContentId", "返回上一步");
	$
			.ajax({
				url : 'storageLteInterferCalcTaskObjInfoForAjaxAction',
				data : {
					'taskInfoType' : 'overViewBack',
					'lteCells' : lteCells
				},
				dataType : 'json',
				type : 'post',
				success : function(raw) {
					var data;
					try {
						data = eval("(" + raw + ")");
						// console.log("data.state:"+data.state);
						var state = data['state'];
					} catch (err) {
					}
				},
				complete : function() {
					// 跳转新的页面
					location.href = "stepByStepOperateLteInterCalcTaskInfoPageForAjaxAction?taskInfoType=overviewInfoBack";
				}
			});
}

/** 判断变PCI小区表是否符合要求 **/
function pciCellValCheck(){
	var lteCells = $("#pciCell").val().trim();
	if(lteCells==null||lteCells==""){
		animateInAndOut("operInfo", 500, 500, 1000, "operTip","变PCI小区表不能为空！");
		return false;
	}else if (ifHasSpecChar(lteCells)) {
		animateInAndOut("operInfo", 500, 500, 1000, "operTip","包含有以下特殊字符:~'!@#$%^&*()-+_=:");
		//	$('#submitTask').removeAttr("disabled");
		return false;
	}else if(!isNumCutByComma(lteCells)){
		animateInAndOut("operInfo", 500, 500, 1000, "operTip","变PCI小区表应该是小区ID以逗号分隔的形式");
		return false;
	}
	return true;
}

// 提交PCI分析任务
function pciPlanAnalysis() {

	var lteCells = $("#pciCell").val().trim();

	$('#submitTask').attr("disabled","true");
	showOperTips("loadingDataDiv", "loadContentId", "正在提交结构分析计算任务");
	$.ajax({
		url : 'submitPciPlanAnalysisTaskAction',
		data : {
			'lteCells' : lteCells
		},
		type : 'post',
		dataType : 'text',
		success : function(raw) {
			var data = null;
			try {
				data = eval("(" + raw + ")");
				if (data['flag'] == false) {
					alert(data['result']);
				} else {
					alert("任务提交成功，请等待计算完成！");
					// animateInAndOut("operInfo", 500, 500, 1000,
					// "operTip","计算任务提交成功，请稍后查看结果。");
					// location.href="initNcsAnalysisPageAction";
					$("#initNcsAnalysisPageForm").submit();
				}
			} catch (err) {
			}
		},
		complete : function() {
			hideOperTips("loadingDataDiv");
		}
	});
}

// 提交PCI分析任务(导入干扰矩阵)
function uploadMatrixForPciPlanAnalysis() {
	$("#err").remove();
	if ($("#fileid").val() == "") {
		$("#fileid").parent().append(
				"<font id='err' color='red'>请选择干扰矩阵文件</font>");
		return;
	}

	$("#uploadMsgDiv").css("display", "none");
	stopQueryProgress = false;
	doUpload();
}
// 上传
function doUpload() {
	$("#progressNum").text('0%');
	$("#progressbar").progressbar({
		value : 0
	});
	$("#progressInfoDiv").fadeIn();

	fileSize = 1;
	var fileCode = $("#formImportPciPlanFile #fileCode").val();
	/*
	 * var attachParams = {}; attachParams['cityId'] = $("#uploadCityId").val();
	 * attachParams['pciCell'] = $("#pciCell").val();
	 */
	var cityId = $("#uploadCityId").val();
	var lteCells = $("#pciCell").val();
	// 询问是否可以上传
	$.ajax({
				url : 'ifFileAcceptableAjaxAction',
				data : {
					'fileSize' : fileSize,
					'fileCode' : fileCode,
					'attachParams.cityId' : cityId,
					'attachParams.pciCell' : lteCells
				},
				type : 'post',
				dataType : 'json',
				success : function(raw2) {
					var accp = {};
					try {
						// accp = eval("(" + raw2 + ")");
						accp = raw2;
					} catch (err) {

					}
					if (accp['flag'] == true) {
						// 可以上传，获取token
						var token = accp['token'];

						// 启动进度查询方法
						window.setTimeout(function() {
							queryProgress(token);
						}, 1000);

						$('#submitTask').attr("disabled","true");
						showOperTips("loadingDataDiv", "loadContentId", "正在提交结构分析计算任务");

						$("#token").val(token);
						// 执行上传
						$("#formImportPciPlanFile")
								.ajaxSubmit(
										{
											type : 'post',
											url : "batchUploadFileAjaxAction",
											dataType : 'text',
											success : function(raw) {
												if (raw) {
													try {
														var data = eval("("
																+ raw + ")");
														if (data['flag'] == true) {
															$("#progressNum")
																	.text(
																			'100%');
															$("#progressbar")
																	.progressbar(
																			{
																				value : 100
																			});
															$("#uploadMsgDiv")
																	.html(
																			"上传成功");
															$("#uploadMsgDiv")
																	.css(
																			"display",
																			"block");
														} else {
															$("#uploadMsgDiv")
																	.html(
																			"上传失败！"
																					+ getValidValue(data['msg']));
															$("#uploadMsgDiv")
																	.css(
																			"display",
																			"block");
															stopQueryProgress = true;
														}
													} catch (err) {
														$("#uploadMsgDiv")
																.html("上传失败");
														$("#uploadMsgDiv").css(
																"display",
																"block");
														stopQueryProgress = true;
													}
												} else {
													$("#uploadMsgDiv").html(
															"上传失败");
													$("#uploadMsgDiv").css(
															"display", "block");
													stopQueryProgress = true;
												}
												alert("任务提交成功，请等待计算完成！");
												$("#initNcsAnalysisPageForm")
														.submit();
											},
										complete : function() {
											hideOperTips("loadingDataDiv");
										}
										});
					}
				}
			});

}

function queryProgress(token) {

	$.ajax({
		url : "queryUploadFileProgressAction",
		type : 'post',
		data : {
			'token' : token
		},
		success : function(raw) {
			// console.log("progress data:" + raw)
			if (raw == null) {
				clearInterval(i);
				// location.reload(true);
				return;
			}
			var data = {};
			try {
				data = eval('(' + raw + ")");
			} catch (err) {
				console.log(err);
			}
			var percentage = 0;
			if (data.totalBytes > 0) {
				percentage = Math.floor(100 * parseFloat(data.readedBytes)
						/ parseFloat(data.totalBytes));
			}
			$("#progressbar").progressbar({
				value : percentage
			});
			$("#progressNum").text(percentage + '%');
			if (percentage >= 1) {
				return;
			}
			if (stopQueryProgress === false) {
				window.setTimeout(function() {
					queryProgress(token);
				}, 5000);
			}
		}
	});
}

// 取消结构分析任务
function cancleNcsAndMrrTask() {
	location.href = "initLteInterferCalcPageAction";
}

/**
 *
 * @title 存储任务消息向session
 * @author chao.xj
 * @date 2014-7-15下午3:33:14
 * @company 怡创科技
 * @version 1.2
 */
function storageTaskInfoForSession() {

	var isMeet = checkTaskInfoSubmit();
	if (!isMeet) {
		return;
	}
	$("span#dataTypeErrorText").html("");
	// 存储数据
	var taskName = $("#taskName").val();
	var taskDescription = $("#taskDescription").val();
	var provinceId = $("#provinceId2").val();
	var cityId = $("#cityId2").val();
	var provinceName = $("#provinceId2  option:selected").text();
	var cityName = $("#cityId2  option:selected").text();
	// console.log(provinceName+" " +cityName);
	var meaStartTime = $("#beginTime").val();
	var meaEndTime = $("#latestAllowedTime").val();

	var planTypeOne = $("#planType1").is(":checked");
	var planTypeTwo = $("#planType2").is(":checked");

	var planOne = $("#useEriData").is(":checked");
	var planTwo = $("#useHwData").is(":checked");
	var cosi = $("#cosi").val();
	// var calConCluster=$("#calConCluster").is(":checked");
	// var calClusterConstrain=$("#calClusterConstrain").is(":checked");
	// var calClusterWeight=$("#calClusterWeight").is(":checked");
	// var calCellRes=$("#calCellRes").is(":checked");
	// var calIdealDis=$("#calIdealDis").is(":checked");
	var i = 0;
	if (!planOne) {
		i++;
	}
	if (!planTwo) {
		i++;
	}
	if (i == 2) {
		$("span#dataTypeErrorText").html("不能均不选择，至少选择一类!");
		return;
	}
	var converType;
	if (planOne) {
		converType = "ONE";
	} else {
		converType = "TWO";
	}

	i = 0;
	if (!planTypeOne) {
		i++;
	}
	if (!planTypeTwo) {
		i++;
	}
	if (i == 2) {
		$("span#dataTypeErrorText").html("不能均不选择，至少选择一类!");
		return;
	}
	var planType;
	if (planTypeOne) {
		planType = "ONE";
	} else {
		planType = "TWO";
	}

	showOperTips("loadingDataDiv", "loadContentId", "正在跳转页面");

	$
			.ajax({
				url : 'storageLteInterferCalcTaskObjInfoForAjaxAction',
				data : {
					'taskInfoType' : 'taskInfoForward',
					'taskName' : taskName,
					'taskDescription' : taskDescription,
					'provinceId' : provinceId,
					'cityId' : cityId,
					'provinceName' : provinceName,
					'cityName' : cityName,
					'meaStartTime' : meaStartTime,
					'meaEndTime' : meaEndTime,
					'planType' : planType,
					'converType' : converType,
					'cosi' : cosi
				// 'calProcedure.CALCONCLUSTER':calConCluster,
				// 'calProcedure.CALCLUSTERCONSTRAIN':calClusterConstrain,
				// 'calProcedure.CALCLUSTERWEIGHT':calClusterWeight,
				// 'calProcedure.CALCELLRES':calCellRes,
				// 'calProcedure.CALIDEALDIS':calIdealDis
				},
				dataType : 'text',
				type : 'post',
				success : function(raw) {
					var data;
					try {
						data = eval("(" + raw + ")");
						// console.log("data.state:"+data.state);
						var state = data['state'];
					} catch (err) {
					}
				},
				complete : function() {
					// hideOperTips("loadingDataDiv");
					// 跳转新的页面
					location.href = "stepByStepOperateLteInterCalcTaskInfoPageForAjaxAction?taskInfoType=taskInfoForward";
				}
			});
}

/**
 *
 * @title 对提交分析任务的信息作验证:不涉及ncs及mrr
 * @returns
 * @author chao.xj
 * @date 2014-7-15上午11:58:25
 * @company 怡创科技
 * @version 1.2
 */
function checkTaskInfoSubmit() {
	var n = 0;
	var m = 0;
	var flagName = true;
	var flagDesc = true;
	var flagDate = true;

	var taskName = $.trim($("#taskName").val());
	var taskDescription = $.trim($("#taskDescription").val());
	var startTime = $.trim($("#beginTime").val());
	var endTime = $.trim($("#latestAllowedTime").val());

	$("span#nameErrorText").html("");
	$("span#descErrorText").html("");
	$("span#dateErrorText").html("");
	$("span#nameError").html("");
	$("span#descError").html("");
	$("span#dateError").html("");

	for ( var i = 0; i < taskName.length; i++) { // 应用for循环语句,获取表单提交用户名字符串的长度
		var leg = taskName.charCodeAt(i); // 获取字符的ASCII码值
		if (leg > 255) { // 判断如果长度大于255
			n += 2; // 则表示是汉字为2个字节
		} else {
			n += 1; // 否则表示是英文字符,为1个字节
		}
	}
	for ( var i = 0; i < taskDescription.length; i++) {
		var leg = taskDescription.charCodeAt(i);
		if (leg > 255) {
			m += 2;
		} else {
			m += 1;
		}
	}
	// 验证任务名称
	if (ifHasSpecChar(taskName)) {
		$("span#nameErrorText").html("（包含有以下特殊字符:<br>~'!@#$%^&*()-+_=:）");
		$("span#nameError").html("※");
		flagName = false;
	}
	if (n > 25) {
		$("span#nameErrorText").html("（不超过25个字符，<br>汉字占2个字符)");
		$("span#nameError").html("※");
		flagName = false;
	} else if (n == 0) {
		$("span#nameErrorText").html("（请输入任务名称）");
		$("span#nameError").html("※");
		flagName = false;
	}
	// 验证任务描述
	if (ifHasSpecChar(taskDescription)) {
		$("span#descErrorText").html("（包含有以下特殊字符:<br>~'!@#$%^&*()-+_=:）");
		$("span#descError").html("※");
		flagName = false;
	}
	if (m > 255) {
		$("span#descErrorText").html("（不超过255个字符，<br>汉字占2个字符）");
		$("span#descError").html("※");
		flagDesc = false;
	}

	if (endTime == "" || startTime == "") {
		$("span#dateErrorText").html("请填写需要使用的测量数据的时间！");
		$("span#dateError").html("※");
		flagDate = false;
	} else if (exDateRange(endTime, startTime) > 10) {
		// 验证测试日期是否大于十天
		$("span#dateErrorText").html("（时间跨度请不要超过10天！）");
		$("span#dateError").html("※");
		flagDate = false;
	}

	if (flagName && flagDesc && flagDate) {
		result = true;
	} else {
		result = false;
	}
	return result;
}

/**
 * 按条件查询结构分析任务
 */
function getStructureTask() {
	// 重置分页条件
	initFormPage('structureTaskForm');
	// 提交表单
	sumbitStructureForm();
}

/**
 * 提交表单
 */
function sumbitStructureForm() {
	showOperTips("loadingDataDiv", "loadContentId", "正在查询");

	$("#structureTaskForm").ajaxSubmit({
		url : 'queryPciPlanAnalysisTaskByPageForAjaxAction',
		dataType : 'text',
		success : function(raw) {
			showStructureTask(raw);
		},
		complete : function() {
			hideOperTips("loadingDataDiv");
		}
	});
}

/**
 * 列表显示结构分析任务
 */
function showStructureTask(raw) {

	if (raw) {

		var table = $("#structureTaskTable");
		// 清空结构分析任务详情列表
		$("#structureTaskTable tr:gt(0)").remove();

		var data = eval("(" + raw + ")");
		if (data == null || data == undefined) {
			return;
		}
		var list = data['data'];
		var tr = "";
		var one = "";

		for ( var i = 0; i < list.length; i++) {
			one = list[i];
			tr += "<tr>";
			tr += "<td>" + getValidValue(one['JOB_NAME'], '') + "</td>";
			if (one['JOB_RUNNING_STATUS'] == "Initiate") {
				tr += "<td>排队中</td>";
			} else if (one['JOB_RUNNING_STATUS'] == "Launched"
					|| one['JOB_RUNNING_STATUS'] == "Running") {
				tr += "<td>运行中</td>";
			} else if (one['JOB_RUNNING_STATUS'] == "Stopping") {
				tr += "<td>停止中</td>";
			} else if (one['JOB_RUNNING_STATUS'] == "Stopped") {
				tr += "<td>已停止</td>";
			} else if (one['JOB_RUNNING_STATUS'] == "Fail") {
				tr += "<td style='color:red;'>异常终止</td>";
			} else if (one['JOB_RUNNING_STATUS'] == "Succeded") {
				tr += "<td>正常完成</td>";
			} else {
				tr += "<td></td>";
			}
			tr += "<td>" + getValidValue(one['CITY_NAME'], '') + "</td>";
			tr += "<td>--</td>";
			tr += "<td>" + getValidValue(one['BEG_MEA_TIME'].substr(0, 10), '')
					+ "<br/>至<br/>"
					+ getValidValue(one['END_MEA_TIME'].substr(0, 10), '')
					+ "</td>";
			tr += "<td>" + getValidValue(one['LAUNCH_TIME'], '') + "</td>";
			tr += "<td>" + getValidValue(one['COMPLETE_TIME'], '') + "</td>";
			// 排队中
			if (one['JOB_RUNNING_STATUS'] == "Initiate") {
				tr += "<td><input type='button' value='停止' onclick='stopPciTask(\""
						+ one['JOB_ID']
						+ "\",\""
						+ one['MR_JOB_ID']
						+ "\")'/></td>";
			}
			// 运行中
			else if (one['JOB_RUNNING_STATUS'] == "Launched"
					|| one['JOB_RUNNING_STATUS'] == "Running") {
				tr += "<td><input type='button' value='停止' onclick='stopPciTask(\""
						+ one['JOB_ID']
						+ "\",\""
						+ one['MR_JOB_ID']
						+ "\")'/> "
						+ "<input type='button' value='查看运行报告' onclick='checkStructureTaskReport(\""
						+ one['JOB_ID'] + "\")'/></td>";
			}
			// 异常终止
			else if (one['JOB_RUNNING_STATUS'] == "Fail") {
				tr += "<td><input type='button' value='查看运行报告' onclick='checkStructureTaskReport(\""
						+ one['JOB_ID'] + "\")'/></td>";
			}
			// 正常完成
			else if (one['JOB_RUNNING_STATUS'] == "Succeded") {
				tr += "<td><input type='button' value='下载结果文件' onclick='downloadPciFile(\""
						+ one['JOB_ID']
						+ "\",\""
						+ one['MR_JOB_ID']
						+ "\")'/> "
						+ "<input type='button' value='查看运行报告' onclick='checkStructureTaskReport(\""
						+ one['JOB_ID'] + "\")'/></td>";
			}
			// 停止中
			else if (one['JOB_RUNNING_STATUS'] == "Stopping") {
				tr += "<td><input type='button' value='查看运行报告' onclick='checkStructureTaskReport(\""
						+ one['JOB_ID'] + "\")'/></td>";
			}
			// 其他（，已停止）
			else {
				tr += "<td><input type='button' value='下载结果文件' onclick='downloadPciFile(\""
						+ one['JOB_ID']
						+ "\",\""
						+ one['MR_JOB_ID']
						+ "\")'/>"
						+ "<input type='button' value='查看运行报告' onclick='checkStructureTaskReport(\""
						+ one['JOB_ID'] + "\")'/></td>";
			}
			tr += "</tr>";
		}
		table.append(tr);

		// 设置隐藏的page信息
		setFormPageInfo("structureTaskForm", data['page']);

		// 设置分页面板
		setPageView(data['page'], "structureTaskPageDiv");
	}
}

// 查看运行报告
function checkStructureTaskReport(jobId) {
	$("#viewReportForm").find("input#hiddenJobId").val(jobId);
	initFormPage("viewReportForm");
	$("#reportDiv").css("display", "block");
	$("#structureTaskDiv").css("display", "none");
	$("#renderImgDiv").css("display", "none");
	queryReportData();
}
// 停止任务
function stopPciTask(jobId, mrJobId) {
	var flag = confirm("是否停止该任务计算？");
	if (flag == false) {
		return;
	}
	showOperTips("loadingDataDiv", "loadContentId", "正在停止任务");
	$.ajax({
		url : 'stopPciJobByJobIdAndMrJobIdForAjaxAction',
		data : {
			'jobId' : jobId,
			'mrJobId' : mrJobId
		},
		dataType : 'text',
		type : 'post',
		success : function(raw) {
			var data;
			try {
				data = eval("(" + raw + ")");
				var flag = data['flag'];
				if (flag) {
					alert("停止操作已提交，请稍后查看停止结果");
					// 刷新列表
					sumbitStructureForm();
				} else {
					alert("任务停止失败！");
				}
			} catch (err) {
			}
		},
		complete : function() {
			hideOperTips("loadingDataDiv");
		}
	});
}
// 下载结果文件
function downloadPciFile(jobId, mrJobId) {
	var form = $("#downloadPciFileForm");
	form.find("input#jobId").val(jobId);
	form.find("input#mrJobId").val(mrJobId);
	form.submit();
}
// 查看渲染图
function viewRenderImg(jobId) {
	// 保存jobId用于获取对应的渲染图
	$("#reportNcsTaskId").val(jobId);
	// 加载渲染图
	var flag = loadRenderImage();
	if (!flag) {
		return;
	}
	// 加载默认渲染规则
	showRendererRuleColor();

	$("#renderImgDiv").css("display", "block");
	$("#structureTaskDiv").css("display", "none");
	$("#reportDiv").css("display", "none");
}

/**
 * 查询指定job的报告
 */
function queryReportData() {
	$("#viewReportForm").ajaxSubmit({
		url : 'queryJobReportAjaxAction',
		type : 'post',
		dataType : 'text',
		success : function(raw) {
			var data = {};
			try {
				data = eval("(" + raw + ")");
			} catch (err) {
				console.log(err);
			}
			displayReportRec(data['data']);
			setFormPageInfo("viewReportForm", data['page']);
			setPageView(data['page'], "reportListPageDiv");
		}
	});
}
/**
 * 显示报告
 */
function displayReportRec(data) {
	if (data == null || data == undefined) {
		return;
	}
	//
	$("#reportListTab tr:not(:first)").each(function(i, ele) {
		$(ele).remove();
	});
	var html = "";
	for ( var i = 0; i < data.length; i++) {
		one = data[i];
		html += "<tr>";
		html += "<td>" + getValidValue(one['STAGE'], '') + "</td>";
		html += "<td>" + getValidValue(one['BEG_TIME'], '') + "</td>";
		html += "<td>" + getValidValue(one['END_TIME'], '') + "</td>";
		if (one['STATE'].indexOf("Fail") == -1) {
			html += "<td>" + getValidValue(one['STATE'], '') + "</td>";
		} else {
			html += "<td style='color: red;'>"
					+ getValidValue(one['STATE'], '') + "</td>";
		}
		html += "<td>" + getValidValue(one['ATT_MSG'], '') + "</td>";
		html += "</tr>";
	}
	$("#reportListTab").append(html);
}

/**
 * 从报告的详情返回列表页面
 */
function returnToTaskList() {
	$("#reportDiv").css("display", "none");
	$("#structureTaskDiv").css("display", "block");
	$("#renderImgDiv").css("display", "none");
}

// 设置隐藏的page信息
function setFormPageInfo(formId, page) {
	if (formId == null || formId == undefined || page == null
			|| page == undefined) {
		return;
	}

	var form = $("#" + formId);
	if (!form) {
		return;
	}

	form.find("#hiddenPageSize").val(page.pageSize);
	form.find("#hiddenCurrentPage").val(new Number(page.currentPage));
	form.find("#hiddenTotalPageCnt").val(page.totalPageCnt);
	form.find("#hiddenTotalCnt").val(page.totalCnt);
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

	var pageSize = page['pageSize'] ? page['pageSize'] : 0;
	var currentPage = page['currentPage'] ? page['currentPage'] : 1;
	var totalPageCnt = page['totalPageCnt'] ? page['totalPageCnt'] : 0;
	var totalCnt = page['totalCnt'] ? page['totalCnt'] : 0;

	// 设置到面板上
	$(div).find("#emTotalCnt").html(totalCnt);
	$(div).find("#showCurrentPage").val(currentPage);
	$(div).find("#emTotalPageCnt").html(totalPageCnt);
}

/**
 * 分页跳转的响应
 *
 * @param dir
 * @param action（方法名）
 * @param formId
 * @param divId
 */
function showListViewByPage(dir, action, formId, divId) {

	var form = $("#" + formId);
	var div = $("#" + divId);
	// alert(form.find("#hiddenPageSize").val());
	var pageSize = new Number(form.find("#hiddenPageSize").val());
	var currentPage = new Number(form.find("#hiddenCurrentPage").val());
	var totalPageCnt = new Number(form.find("#hiddenTotalPageCnt").val());
	var totalCnt = new Number(form.find("#hiddenTotalCnt").val());

	// console.log("pagesize="+pageSize+",currentPage="+currentPage+",totalPageCnt="+totalPageCnt+",totalCnt="+totalCnt);
	if (dir === "first") {
		if (currentPage <= 1) {
			return;
		} else {
			$(form).find("#hiddenCurrentPage").val("1");
		}
	} else if (dir === "last") {
		if (currentPage >= totalPageCnt) {
			return;
		} else {
			$(form).find("#hiddenCurrentPage").val(totalPageCnt);
		}
	} else if (dir === "back") {
		if (currentPage <= 1) {
			return;
		} else {
			$(form).find("#hiddenCurrentPage").val(currentPage - 1);
		}
	} else if (dir === "next") {
		if (currentPage >= totalPageCnt) {
			return;
		} else {
			$(form).find("#hiddenCurrentPage").val(currentPage + 1);
		}
	} else if (dir === "num") {
		var userinput = $(div).find("#showCurrentPage").val();
		if (isNaN(userinput)) {
			alert("请输入数字！")
			return;
		}
		if (userinput > totalPageCnt || userinput < 1) {
			alert("输入页面范围不在范围内！");
			return;
		}
		$(form).find("#hiddenCurrentPage").val(userinput);
	} else {
		return;
	}
	// 获取资源
	if (typeof action == "function") {
		action();
	}
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

// 初始化区域
function initAreaCascade() {

	$("#provinceId2").change(function() {
		getSubAreas("provinceId2", "cityId2", "市");
	});

	$("#cityId2").change(function() {
		/*
		 * getSubAreas("cityId2", "areaId2", "区/县",function(){
		 * $("#areaId2").append("<option selected='true' value=''>全部</option>");
		 * });
		 */
		// 以城市为单位创建区域网格
		var cityName = $("#cityId2").find("option:selected").text().trim();
		// 获取cityId赋值到表单隐藏域
		var cityId = $("#cityId2").val();
		$("#cityId").val(cityId);
	});
}

// 验证门限值是否符合要求
function checkThreshold() {

	clearTip();

	var SAMEFREQCELLCOEFWEIGHT = $("#SAMEFREQCELLCOEFWEIGHT").html().trim();
	var SWITCHRATIOWEIGHT = $("#SWITCHRATIOWEIGHT").html().trim();
	var CELLM3RINTERFERCOEF = $("#CELLM3RINTERFERCOEF").html().trim();
	var CELLM6RINTERFERCOEF = $("#CELLM6RINTERFERCOEF").html().trim();
	var CELLM30RINTERFERCOEF = $("#CELLM30RINTERFERCOEF").html().trim();
	var BEFORENSTRONGCELLTAB = $("#BEFORENSTRONGCELLTAB").html().trim();
	var TOPNCELLLIST = $("#TOPNCELLLIST").html().trim();
	var INCREASETOPNCELLLIST = $("#INCREASETOPNCELLLIST").html().trim();
	var CONVERMETHOD1TARGETVAL = $("#CONVERMETHOD1TARGETVAL").html().trim();
	var CONVERMETHOD2TARGETVAL = $("#CONVERMETHOD2TARGETVAL").html().trim();
	var CONVERMETHOD2SCOREN = $("#CONVERMETHOD2SCOREN").html().trim();
	var MINCORRELATION = $("#MINCORRELATION").html().trim();
	var MINMEASURESUM = $("#MINMEASURESUM").html().trim();

	var reg = /^[-+]?[0-9]+(\.[0-9]+)?$/; // 验证数字
	var reg1 = /^[0-9]*[1-9][0-9]*$/; // 正整数
	var reg2 = /^(?:[1-9]?\d|100)$/; // 0-100的整数，适用于验证百分数
	var reg3=/^([1-9]\d*|0)$/; //非负整数
	var flag = true;
	// console.log(SAMEFREQINTERTHRESHOLD + " "+ OVERSHOOTINGIDEALDISMULTIPLE);
	if (!reg.test(SAMEFREQCELLCOEFWEIGHT)) {
		$("span#SAMEFREQCELLCOEFWEIGHT").html("※请输入数字※");
		flag = false;
	} else if (SAMEFREQCELLCOEFWEIGHT > 1) {
		$("span#SAMEFREQCELLCOEFWEIGHT").html("※值需要小于等于1※");
		flag = false;
	}else{
		$("span#SAMEFREQCELLCOEFWEIGHT").html("");
	}
	if (!reg.test(SWITCHRATIOWEIGHT)) {
		$("span#SWITCHRATIOWEIGHT").html("※请输入数字※");
		flag = false;
	} else if (SWITCHRATIOWEIGHT > 1) {
		$("span#SWITCHRATIOWEIGHT").html("※值需要小于等于1※");
		flag = false;
	}else{
		$("span#SWITCHRATIOWEIGHT").html("");
	}
	if (!reg.test(CELLM3RINTERFERCOEF)) {
		$("span#CELLM3RINTERFERCOEF").html("※请输入数字※");
		flag = false;
	}else{
		$("span#CELLM3RINTERFERCOEF").html("");
	}
	/*
	 * else if(CELLM3RINTERFERCOEF < 1) {
	 * $("span#CELLM3RINTERFERCOEF").html("※值需要大于等于1※"); flag = false; }
	 */
	if (!reg.test(CELLM6RINTERFERCOEF)) {
		$("span#CELLM6RINTERFERCOEF").html("※请输入数字※");
		flag = false;
	}else{
		$("span#CELLM6RINTERFERCOEF").html("");
	}
	/*
	 * else if(CELLCHECKTIMESIDEALDISMULTIPLE < 1) {
	 * $("span#CELLCHECKTIMESIDEALDISMULTIPLE").html("※值需要大于等于1※"); flag =
	 * false; }
	 */
	if (!reg.test(CELLM30RINTERFERCOEF)) {
		$("span#CELLM30RINTERFERCOEF").html("※请输入数字※");
		flag = false;
	}else{
		$("span#CELLM30RINTERFERCOEF").html("");
	}
	/*
	 * else if(CELLDETECTCITHRESHOLD < 0.02) {
	 * $("span#CELLDETECTCITHRESHOLD").html("※值需要大于等于0.02※"); flag = false; }
	 */
	if (!reg1.test(BEFORENSTRONGCELLTAB)) {
		$("span#BEFORENSTRONGCELLTAB").html("※请输入正整数※");
		flag = false;
	}else{
		$("span#BEFORENSTRONGCELLTAB").html("");
	}
	/*
	 * else if(CELLIDEALDISREFERENCECELLNUM >= 10 ||
	 * CELLIDEALDISREFERENCECELLNUM <= 0) {
	 * $("span#CELLIDEALDISREFERENCECELLNUM").html("※值需要大于0小于10※"); flag =
	 * false; }
	 */
	if (!reg2.test(TOPNCELLLIST)) {
		$("span#TOPNCELLLIST").html("※请输入0-100间的整数※");
		flag = false;
	}else{
		$("span#TOPNCELLLIST").html("");
	}
	/*
	 * else if(GSM900CELLFREQNUM <= 0) {
	 * $("span#GSM900CELLFREQNUM").html("※值需要大于0※"); flag = false; }
	 */
	if (!reg.test(INCREASETOPNCELLLIST)) {
		$("span#INCREASETOPNCELLLIST").html("※请输入数字※");
		flag = false;
	}else{
		$("span#INCREASETOPNCELLLIST").html("");
	}
	/*
	 * else if(GSM1800CELLFREQNUM <= 0) {
	 * $("span#GSM1800CELLFREQNUM").html("※值需要大于0※"); flag = false; }
	 */
	if (!reg2.test(CONVERMETHOD1TARGETVAL)) {
		$("span#CONVERMETHOD1TARGETVAL").html("※请输入0-100间的整数※");
		flag = false;
	}else{
		$("span#CONVERMETHOD1TARGETVAL").html("");
	}
	/*
	 * else if(GSM900CELLIDEALCAPACITY <= 0) {
	 * $("span#GSM900CELLIDEALCAPACITY").html("※值需要大于0※"); flag = false; }
	 */
	if (!reg2.test(CONVERMETHOD2TARGETVAL)) {
		$("span#CONVERMETHOD2TARGETVAL").html("※请输入0-100间的整数※");
		flag = false;
	}else{
		$("span#CONVERMETHOD2TARGETVAL").html("");
	}
	/*
	 * else if(GSM1800CELLIDEALCAPACITY <= 0) {
	 * $("span#GSM1800CELLIDEALCAPACITY").html("※值需要大于0※"); flag = false; }
	 */
	if (!reg.test(CONVERMETHOD2SCOREN)) {
		$("span#CONVERMETHOD2SCOREN").html("※请输入数字※");
		flag = false;
	}else{
		$("span#CONVERMETHOD2SCOREN").html("");
	}
	/*
	 * else if(DLCOVERMINIMUMSIGNALSTRENGTHTHRESHOLD < -94) {
	 * $("span#DLCOVERMINIMUMSIGNALSTRENGTHTHRESHOLD").html("※值需大于等于-94※"); flag =
	 * false; }
	 */
	if (!reg2.test(MINCORRELATION)) {
		$("span#MINCORRELATION").html("※请输入0-100间的整数※");
		flag = false;
	}else{
		$("span#MINCORRELATION").html("");
	}
	if (!reg3.test(MINMEASURESUM)) {
		$("span#MINMEASURESUM").html("※请输入非负整数※");
		flag = false;
	}else{
		$("span#MINMEASURESUM").html("");
	}
	return flag;
}

function clearTip() {

	$("span#SAMEFREQINTERTHRESHOLD").html("");
	$("span#OVERSHOOTINGIDEALDISMULTIPLE").html("");
	$("span#BETWEENCELLIDEALDISMULTIPLE").html("");
	$("span#CELLCHECKTIMESIDEALDISMULTIPLE").html("");
	$("span#CELLDETECTCITHRESHOLD").html("");
	$("span#CELLIDEALDISREFERENCECELLNUM").html("");
	$("span#GSM900CELLFREQNUM").html("");
	$("span#GSM1800CELLFREQNUM").html("");
	$("span#GSM900CELLIDEALCAPACITY").html("");
	$("span#GSM1800CELLIDEALCAPACITY").html("");
	$("span#DLCOVERMINIMUMSIGNALSTRENGTHTHRESHOLD").html("");
	$("span#ULCOVERMINIMUMSIGNALSTRENGTHTHRESHOLD").html("");
	$("span#INTERFACTORMOSTDISTANT").html("");
	$("span#INTERFACTORSAMEANDADJFREQMINIMUMTHRESHOLD").html("");
	$("span#RELATIONNCELLCITHRESHOLD").html("");

	$("span#TOTALSAMPLECNTSMALL").html("");
	// $("span#TOTALSAMPLECNTBIG").html("");
	$("span#TOTALSAMPLECNTTOOSMALL").html("");
	$("span#SAMEFREQINTERCOEFBIG").html("");
	$("span#SAMEFREQINTERCOEFSMALL").html("");
	$("span#OVERSHOOTINGCOEFRFFERDISTANT").html("");
	$("span#NONNCELLSAMEFREQINTERCOEF").html("");
}

// 计算两个日期差值
function exDateRange(sDate1, sDate2) {
	var iDateRange;
	if (sDate1 != "" && sDate2 != "") {
		var startDate = sDate1.replace(/-/g, "/");
		var endDate = sDate2.replace(/-/g, "/");
		var S_Date = new Date(Date.parse(startDate));
		var E_Date = new Date(Date.parse(endDate));
		iDateRange = (S_Date - E_Date) / 86400000;
		// alert(iDateRange);
	}
	return iDateRange;
}

// 点击td转换可编辑
function setEditHTML(value) {
	editHTML = '<input id="editTd" type="text" maxlength="7" onBlur="ok(this)" value="'
			+ value + '" />';
	// editHTML += '<input type="button" onmousemove="ok(this)" value="修改" />';
	// editHTML += '<input type="button" onmousemove="cancel(this)" value="取消"
	// />';
}
/*
 * //取消 function cancel(cbtn){
 *
 * var $obj = $(cbtn).parent(); //'取消'按钮的上一级，即单元格td
 * //console.log($obj.data("oldtxt")); $obj.html($obj.data("oldtxt"));
 * //将单元格内容设为原始数据，取消修改 $obj.bind("click",function(){ //重新绑定单元格双击事件 editText =
 * $(this).html().trim(); setEditHTML(editText);
 * $(this).data("oldtxt",editText).html(editHTML).unbind("click");
 * $("#editTd").focus(); }); }
 */
// 修改
function ok(obtn) {
	var $obj = $(obtn).parent();
	var objId = $obj.attr("id");
	var value = $obj.find("input:text")[0].value; // 取得文本框的值，即新数据
	if (value === "") {
		value = "   ";
	}
	// alert("success");
	$obj.data("oldtxt", value); // 设置此单元格缓存为新数据
	$obj.html($obj.data("oldtxt"));
	$obj.bind("click", function() { // 重新绑定单元格单击事件
		editText = $(this).html().trim();
		setEditHTML(editText);
		$(this).data("oldtxt", editText).html(editHTML).unbind("click");
		$("#editTd").focus();

	});
	if (objId == "SAMEFREQCELLCOEFWEIGHT") {
		var samefreq = $obj.text();
		$("#SWITCHRATIOWEIGHT").text((1 - Number(samefreq)).toFixed(2));
	}
	if (objId == "SWITCHRATIOWEIGHT") {
		var switchrat = $obj.text();
		$("#SAMEFREQCELLCOEFWEIGHT").text((1 - Number(switchrat)).toFixed(2));
	}
}