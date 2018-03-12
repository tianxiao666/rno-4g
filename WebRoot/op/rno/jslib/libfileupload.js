//依赖jquery、selftools.js
/**
* formId:提交的formid
 * btnId:提交的按钮id
 * maxTryCnt:最多尝试次数
 * resultDiv:显示结果的面板
 * callback:回调
 */
function RnoFileUpload(formId,btnId,maxTryCnt,resultDivId,progresscallback,resultcallback){
	this.formId=formId;
	this.btnId=btnId;//允许传入一个数组
	this.maxTryCnt=(typeof maxTryCnt=="number")?maxTryCnt:5;
	this.resultDivId=resultDivId;
	this.progressCallback=progresscallback;//进度查询的callback
	this.resultCallback=resultcallback;//处理最终结果的callback
	
	this.failCnt=0;
	this.interval=2*1000;
}

RnoFileUpload.prototype.upload=function(){
	//console.log("..in upload..");
	var me=this;
	me.disableUploadBtn();
	
	$("#"+me.formId).ajaxSubmit({
		type : 'post',
		url : "uploadFileAjaxAction",
		dataType : 'text',
		success : function(data) {
			var obj;
			try {
				obj = eval("(" + data + ")");
			} catch (err) {
				//$('#formImportCell .canclear').clearFields();
				$("#"+me.resultDivId).html("上传文件失败！");
				//window.location.href = window.location.href;
				return;
			}
			// alert(obj['token']);
			if (obj['token'] && obj['token'] != 'null') {
				$("#"+me.resultDivId).html("正在解析文件...");
				me.queryProgress(obj['token']);
			} else {
				// token分配失败
				var msg=obj['msg']?("原因："+obj['msg']):"";
				//console.log("fail msg:"+obj['msg']+"- ---"+msg);
				$("#"+me.resultDivId).html("文件解析失败！"+msg);
				console.log($("#"+me.resultDivId).html());
				
				me.enableUploadBtn();
				
			}
		},
		error : function(XmlHttpRequest, textStatus, errorThrown) {
			// alert("error" + textStatus + "------"
			// + errorThrown);
			var msg=obj['msg']?("原因："+obj['msg']):"";
			$("#"+me.resultDivId).html("文件解析失败！"+msg);
			me.enableUploadBtn();
		},
		complete : function() {
			//$('#formImportCell .canclear').clearFields();
		}
	});
}

//查询进度
RnoFileUpload.prototype.queryProgress=function(token) {
	var me=this;
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
				$("#"+me.resultDivId).html("服务器返回失败！");
				return;
			}
//			 alert("data=="+data);
			if (obj['flag']) {
				
				var pronum = new Number(obj['progress']);
				if(pronum>=0){
					$("#"+me.resultDivId).html(
							"文件正在解析，当前进度:" + obj['progress'] + "%"+(obj['msg']!=null && obj['msg']!=undefined)?(obj['msg']):'');
				}else{
					//不显示进度，只显示msg
					$("#"+me.resultDivId).html(
							"文件正在解析，当前进度:" + getValidValue(obj['msg'],''));
				}
				// alert("progress:"+pronum);
				if (pronum >= 100) {
//					 alert("get final result");
					// 文件解析完成
					me.getImportResult(token);// 获取结果
				} else {
					// alert("get progress again");
					// setTimeout("queryProgress(" + token + ")", 5000);
					window.setTimeout(function() {
						me.queryProgress(token);
					}, me.interval);
				}
			} else {
				$("#"+me.resultDivID).html("文件解析失败！原因:" + obj['msg']);
				me.getImportResult(token);// 获取结果
			}
		},
		error : function(err, status) {
			alert("获取进度失败！");
			// 获取失败
			me.failCnt++;
			if (me.failCnt > me.maxTryCnt) {
				$("#"+me.resultDivID).html("无法获取文件解析进度！");
				me.enableUploadBtn();
			} else {
				window.setTimeout(function() {
					me.queryProgress(token);
				}, me.interval);
			}
		}
	});
}

// 获取详细结果
RnoFileUpload.prototype.getImportResult=function(token) {
	var me=this;
	$.ajax({
		url : 'getUploadResultAjaxAction',
		data : {
			'token' : token
		},
		type : 'post',
		dataType : 'text',
		success : function(data) {
			// 上传按钮可用
			if(typeof me.resultCallback=="function"){
				me.resultCallback(data);
			}else{
				$("#"+me.resultDivId).html(data);
			}
		},
		error : function(err, status) {
			$("#"+me.resultDivId).html("获取结果失败！");
		},
		complete : function() {
			me.enableUploadBtn();
		}
	});
}

RnoFileUpload.prototype.enableUploadBtn=function(){
	var me=this;
	if(isArray(me.btnId)){
		for(var i=0;i<me.btnId.length;i++){
			$("#"+me.btnId[i]).removeAttr("disabled");
		}
	}else{
		$("#"+me.btnId).removeAttr("disabled");
	}
}

RnoFileUpload.prototype.disableUploadBtn=function(){
	var me=this;
	if(isArray(me.btnId)){
		for(var i=0;i<me.btnId.length;i++){
			$("#"+me.btnId[i]).attr("disabled", "disabled");// 先禁用
		}
	}else{
		$("#"+me.btnId).attr("disabled", "disabled");// 先禁用
	}
}