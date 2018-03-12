var rnoFileUpload;
var firstTime=true;
$(document)
		.ready(
				function() {
					// 文件上传
					rnoFileUpload = new RnoFileUpload("formImportMrr", [
							 "importMrrBtn" ], 3,
							"importMrrResultDiv", null, function(result) {
								if (!result) {
									return;
								}
								var data = "";
								try {
									data = eval("(" + result + ")");
								} catch (err) {
									$("#importMrrResultDiv").html(result);
									return;
								}
								$("#importMrrResultDiv").html(
										data['msg'] ? data['msg'] : "");
							});

					// 导入form验证
					$("#formImportMrr")
							.validate(
									{
										messages:{
											areaId:'请选择区域',
											fileCode:'请选择网络制式',
											file:'请选择上传文件',
											update:'请选择重复记录处理方式'
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


					$("#importMrrBtn").click(function() {
						var areaId=$("#formImportMrr #cityId2").find("option:selected").val();
						$("#formImportMrr #areaId").val(areaId);
						$("#formImportMrr").submit();
					});
					initAreaCascade();
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
}



/*var failCnt = 0;// 连续获取进度失败的次数
var maxFailCnt = 5;// 最大允许失败的次数，超过将放弃获取
var interval = 2000;// 周期性获取进度情况的

$(document).ready(function() {
	
		$("#formImportCell").validate({
//			 invalidHandler: function(event, validator) {
//				 var errors = validator.numberOfInvalids();
//				    if (errors) {
//				      var message = errors == 1
//				        ? 'You missed 1 field. It has been highlighted'
//				        : 'You missed ' + errors + ' fields. They have been highlighted';
//				      $("div.error span").html(message);
//				      $("div.error").show();
//				    } else {
//				      $("div.error").hide();
//				    }
//			 },
			errorPlacement:function(error,element){
				$(element).parent().append(error);
			},
			messages:{
				areaId:'请选择区域',
				fileCode:'请选择网络制式',
				file:'请选择上传文件',
				update:'请选择重复记录处理方式'
			},
			submitHandler : function(form) {
				$("#formImportCell").ajaxSubmit({
					type : 'post',
					url : "uploadFileAjaxAction",
					dataType : 'text',
					success : function(data) {
						$("#importCellBtn").attr("disabled", "disabled");// 先禁用
						var obj;
						try {
							obj = eval("(" + data + ")");
						} catch (err) {
							$('#formImportCell .canclear').clearFields();
							$("#importResultDiv").html("上传文件失败！");
							window.location.href = window.location.href;
							return;
						}
						// alert(obj['token']);
						if (obj['token'] && obj['token'] != 'null') {
							$("#importResultDiv").html("正在解析文件...");
							queryProgress(obj['token']);
						} else {
							// token分配失败
							var msg=obj['msg']?("原因："+obj['msg']):"";
							//console.log("fail msg:"+obj['msg']+"- ---"+msg);
							$("#importResultDiv").html("文件解析失败！"+msg);
							$("#importCellBtn").removeAttr("disabled");
						}
					},
					error : function(XmlHttpRequest, textStatus, errorThrown) {
						// alert("error" + textStatus + "------"
						// + errorThrown);
						var msg=obj['msg']?("原因："+obj['msg']):"";
						$("#importResultDiv").html("文件解析失败！"+msg);
						$("#importCellBtn").removeAttr("disabled");
					},
					complete : function() {
						$('#formImportCell .canclear').clearFields();
					}
				});
			}
		});
		$("#formImportMrr").validate({
			
			errorPlacement:function(error,element){
				$(element).parent().append(error);
			},
			messages:{
				areaId:'请选择区域',
				fileCode:'请选择网络制式',
				file:'请选择上传文件',
				update:'请选择重复记录处理方式'
			},
			
			submitHandler : function(form) {
				var areaId=$("#formImportMrr #cityId2").find("option:selected").val();
				console.log("areaId:"+areaId);
				$("#formImportMrr").ajaxSubmit({
					type : 'post',
					data : {
						'areaId':areaId
					},
					url : "uploadFileAjaxAction",
					dataType : 'text',
					success : function(data) {
						$("#importMrrBtn").attr("disabled", "disabled");// 先禁用
						var obj;
						try {
							obj = eval("(" + data + ")");
						} catch (err) {
							$('#formImportMrr .canclear').clearFields();
							$("#formImportMrr #importResultDiv").html("上传文件失败！");
							window.location.href = window.location.href;
							return;
						}
						// alert(obj['token']);
						if (obj['token'] && obj['token'] != 'null') {
							$("#formImportMrr #importResultDiv").html("正在解析文件...");
							queryProgress(obj['token']);
						} else {
							// token分配失败
							var msg=obj['msg']?("原因："+obj['msg']):"";
							//console.log("fail msg:"+obj['msg']+"- ---"+msg);
							$("#formImportMrr #importResultDiv").html("文件解析失败！"+msg);
							$("#importMrrBtn").removeAttr("disabled");
						}
					},
					error : function(XmlHttpRequest, textStatus, errorThrown) {
						// alert("error" + textStatus + "------"
						// + errorThrown);
						var msg=obj['msg']?("原因："+obj['msg']):"";
						$("#formImportMrr #importResultDiv").html("文件解析失败！"+msg);
						$("#importMrrBtn").removeAttr("disabled");
					},
					complete : function() {
						$('#formImportMrr .canclear').clearFields();
					}
				});
			}
		});
	$("#importCellBtn").click(function() {
		$("#formImportCell").submit();
		return true;
	});
	$("#importMrrBtn").click(function() {
		$("#formImportMrr").submit();
		return true;
	});
});

function queryProgress(token) {
	
	$.ajax({
		url : 'queryUploadStatusAjaxAction',
		data : {
			'token' : token
		},
		type : 'post',
		dataType : 'text',
		success : function(data) {
			var obj;
			try {
				obj = eval("(" + data + ")");
			} catch (err) {
				$("#formImportMrr #importResultDiv").html("服务器返回失败！");
				$("#formImportCell #importResultDiv").html("服务器返回失败！");
				return;
			}
			// alert("data=="+data);
			if (obj['flag']) {
				$("#formImportMrr #importResultDiv").html(
						"文件正在解析，当前进度:" + obj['progress'] + "%");
				$("#formImportCell #importResultDiv").html(
						"文件正在解析，当前进度:" + obj['progress'] + "%");
				var pronum = new Number(obj['progress']);
				// alert("progress:"+pronum);
				if (pronum >= 100) {
					// alert("get final result");
					// 文件解析完成
					getImportResult(token);// 获取结果
				} else {
					// alert("get progress again");
					// setTimeout("queryProgress(" + token + ")", 5000);
					window.setTimeout(function() {
						queryProgress(token);
					}, interval);
				}
			} else {
				$("#formImportMrr #importResultDiv").html("文件解析失败！原因:" + obj['msg']);
				$("#formImportCell #importResultDiv").html("文件解析失败！原因:" + obj['msg']);
				getImportResult(token);// 获取结果
			}
		},
		error : function(err, status) {
			alert("获取进度失败！");
			// 获取失败
			failCnt++;
			if (failCnt > maxFailCnt) {
				$("#formImportMrr #importResultDiv").html("无法获取文件解析进度！");
				$("#formImportCell #importResultDiv").html("无法获取文件解析进度！");
				$("#importCellBtn").removeAttr("disabled");// 启用
			} else {
				window.setTimeout(function() {
					queryProgress(token);
				}, interval);
			}
		}
	});
}

// 获取详细结果
function getImportResult(token) {
	$.ajax({
		url : 'getUploadResultAjaxAction',
		data : {
			'token' : token
		},
		type : 'post',
		dataType : 'text',
		success : function(data) {
			$("#formImportMrr #importResultDiv").html(data);
			$("#formImportCell #importResultDiv").html(data);
			// 上传按钮可用
		},
		error : function(err, status) {
			$("formImportMrr #importResultDiv").html("获取结果失败！");
			$("formImportCell #importResultDiv").html("获取结果失败！");
		},
		complete : function() {
			$("#importCellBtn").removeAttr("disabled");// 启用
			$("#importMrrBtn").removeAttr("disabled");// 启用
		}
	});
}*/