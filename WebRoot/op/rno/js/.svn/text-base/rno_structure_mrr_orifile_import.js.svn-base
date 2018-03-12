var rnoFileUpload;
var firstTime=true;

var isAreaLocked=false;

$(document)
		.ready(
				function() {
					// 文件上传
					rnoFileUpload = new RnoFileUpload("formImportDT", [
							 "importBtn" ], 3,
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
							});

					// 导入form验证
					$("#formImportDT")
							.validate(
									{
										messages : {
											'file':"请选择mrr文件"
											
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


					$("#importBtn").click(function() {
						if(!isAreaLocked) {
							$("#formImportDT").submit();
						}
					});
					initAreaCascade();
					checkAreaLock();
//					//ncs记录查询
//					$("#searchNcsDescBtn").click(function(){
//						initFormPage("ncsDescDataForm");
//						doQueryNcsDesc();
//					});
//					
//					
					
//					$(".fileCodeCls").click(function(){
//						if($("input#eric").attr("checked")=="checked"){
//							$("span#fileTip").css("display","");
//						}else{
//							$("span#fileTip").css("display","none");
//						}
//					});
				});


function initAreaCascade() {
	
//	$("#areaId2").append("<option selected='true' value=''>全部</option>");
	
	// 区域联动事件
	$("#provinceId1").change(function() {
		getSubAreas("provinceId1", "cityId1", "市");
		checkAreaLock();
	});

	$("#cityId1").change(function() {
		getSubAreas("cityId1", "areaId1", "区/县");
		$("#areaId1").append("<option value='-1' selected='true'>不关注</option>");
		checkAreaLock();
	});
	
	$("#areaId1").change(function() {
		var areaName = $("#areaId2").find("option:selected").text();
		$("#hiddenAreaName").val(areaName);
		checkAreaLock();
	});

//	$("#provinceId2").change(function() {
//		getSubAreas("provinceId2", "cityId2", "市");
//	});
//
//	$("#cityId2").change(function() {
//		getSubAreas("cityId2", "areaId2", "区/县",function(){
//			$("#areaId2").append("<option selected='true' value=''>全部</option>");
//		});
//		
//	});
}

function checkAreaLock() {
	$("span#areaLockTip").html("");
	var cityId = $("#cityId1").val();
	$.ajax({
		url : "checkAreaLockedForImportMrrAction",
		data : {
			'cityId' : cityId 
		},
		type : 'post',
		dataType : 'text',
		success : function(raw) {
			data = eval("("+raw+")");
			var flag = data['flag'];
			isAreaLocked = flag;
		},
		complete : function() {
			if(isAreaLocked) {
				$("span#areaLockTip").css({
						"font" : "13px/1.5 Tahoma,'Microsoft Yahei','Simsun'",
						"color" : "#FF0000"
					});
				$("span#areaLockTip").html("当前该市正在接收其他用户的MRR导入，请等待");
			} else {
				$("span#areaLockTip").css({
						"font" : "13px/1.5 Tahoma,'Microsoft Yahei','Simsun'",
						"color" : "#0DB026"
					});
				$("span#areaLockTip").html("该市可接收MRR数据导入");
			}
		}
	});
}


