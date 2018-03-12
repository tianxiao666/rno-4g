var failCnt = 0;// 连续获取进度失败的次数
var maxFailCnt = 5;// 最大允许失败的次数，超过将放弃获取
var interval = 2000;// 周期性获取进度情况的

$(document).ready(function() {
	$("#formImportCell").validate({
		rules:{
			update:"required",
			file:"required",
			areaId:"required"
		},
		messages:{
			file:"请选择邻区关系定义文件",
			areaId:"请选择待导入邻区所属区域",
			update:"请指定对于重复数据的处理方式"
		},
		errorPlacement:function(error,element){
			$(element).parent().append(error);
		},
		errorElement:'span',
		submitHandler : function(form) {
			$(form).ajaxSubmit({
				type : 'post',
				url : "uploadFileAjaxAction",
				dataType : 'text',
				success : function(data) {
					$("#importBtn").attr("disabled", "disabled");// 先禁用
					var obj = eval("(" + data + ")");
					// alert(obj['token']);
					if (obj['token'] && obj['token'] != 'null') {
						$("#importResultDiv").html("正在解析文件...");
						queryProgress(obj['token']);
					} else {
						// token分配失败
						$("#importResultDiv").html("文件解析失败！");
						$("#importBtn").removeAttr("disabled");
					}
				},
				error : function(XmlHttpRequest, textStatus, errorThrown) {
					// alert("error" + textStatus + "------"
					// + errorThrown);
					$("#importResultDiv").html("文件解析失败！");
					$("#importBtn").removeAttr("disabled");
				},
				complete : function() {
					$('#formImportCell .canclear').clearFields();
				}
			});
		}
	});
	
	$("#importBtn").click(function() {
		$("#formImportCell").submit();
		return true;
	});
});

// $("#importBtn").click(function() {
// $("#formImportCell").ajaxSubmit({
// type : 'post',
// url : "uploadFileAjaxAction",
// dataType : 'text',
// success : function(data) {
// $("#importBtn").attr("disabled", "disabled");// 先禁用
// var obj = eval("(" + data + ")");
// // alert(obj['token']);
// if (obj['token'] && obj['token']!='null') {
// $("#importResultDiv").html("正在解析文件...");
// queryProgress(obj['token']);
// }else{
// // token分配失败
// $("#importResultDiv").html("文件解析失败！");
// $("#importBtn").removeAttr("disabled");
// }
// },
// error : function(XmlHttpRequest, textStatus,
// errorThrown) {
// // alert("error" + textStatus + "------"
// // + errorThrown);
// $("#importResultDiv").html("文件解析失败！");
// $("#importBtn").removeAttr("disabled");
// },complete:function(){
// $('#formImportCell .canclear').clearFields();
// }
// });
// return true;
// });

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
				$("#importResultDiv").html("服务器返回失败！");
				return;
			}
			// alert("data=="+data);
			if (obj['flag']) {
				$("#importResultDiv").html(
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
				$("#importResultDiv").html("文件解析失败！原因:" + obj['msg']);
				getImportResult(token);// 获取结果
			}
		},
		error : function(err, status) {
			alert("获取进度失败！");
			// 获取失败
			failCnt++;
			if (failCnt > maxFailCnt) {
				$("#importResultDiv").html("无法获取文件解析进度！");
				$("#importBtn").removeAttr("disabled");// 启用
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
			$("#importResultDiv").html(data);
			// 上传按钮可用
		},
		error : function(err, status) {
			$("#importResultDiv").html("获取结果失败！");
		},
		complete : function() {
			$("#importBtn").removeAttr("disabled");// 启用
		}
	});
}