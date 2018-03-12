$(document).ready(
		function() {
			// 文件上传
			rnoFileUpload = new RnoFileUpload("formImportLteNCell",
					[ "importBtn" ], 3, "importResultDiv", null, function(
							result) {
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
			$("#formImportLteNCell").validate({
				messages : {
					'file' : "请选择LTE邻区关系EXCEL文件"

				},
				errorPlacement : function(error, element) {
					element.parent().append(error);
				},
				// debug : true,
				submitHandler : function(form) {
					try {
						rnoFileUpload.upload();
					} catch (err) {
					}
				}
			});

			$("#importBtn").click(function() {
				var filename = fileid.value; 
				if(!(filename.toUpperCase().endsWith(".CSV")||filename.toUpperCase().endsWith(".XLS")
						||filename.toUpperCase().endsWith(".XLSX")||filename.toUpperCase().endsWith(".XML")
						||filename.toUpperCase().endsWith(".TXT")||filename.toUpperCase().endsWith(".ZIP"))){
					alert("不支持该类型文件！");
					return false;
				}	
				$("#formImportLteNCell").submit();
			});
			

		});
