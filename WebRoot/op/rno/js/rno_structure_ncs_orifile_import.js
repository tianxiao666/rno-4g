var rnoFileUpload;
var firstTime=true;
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
											'file':"请选择NCS测试文件"
											
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
						$("#formImportDT").submit();
					});
					initAreaCascade();

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
	});

	$("#cityId1").change(function() {
		getSubAreas("cityId1", "areaId1", "区/县");
		$("#areaId1").append("<option value='-1' selected='true'>不关注</option>");
	});
	
	$("#areaId1").change(function() {
		var areaName = $("#areaId2").find("option:selected").text();
		$("#hiddenAreaName").val(areaName);
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


