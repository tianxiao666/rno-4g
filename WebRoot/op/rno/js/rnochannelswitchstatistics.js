
var failCnt = 0;// 连续获取进度失败的次数
var maxFailCnt = 5;// 最大允许失败的次数，超过将放弃获取
var interval = 2000;// 周期性获取进度情况的

$(document).ready(function() {
	//指标类型 单选按钮点击事件 显示不同下载模板
	$("input[name='fileCode']").click(function(){
		var curValue = $(this).val();
		if(curValue=="GSMAUDIOTRAFFICSTATICSFILE"){
			$("#downloadHref").attr("href","fileDownloadAction?fileName=GSM网络制式导入模板.xlsx").html("GSM网络制式导入模板");
		}else if(curValue=="GSMDATATRAFFICSTATICSFILE"){
			$("#downloadHref").attr("href","fileDownloadAction?fileName=TD网络制式导入模板.xlsx").html("TD网络制式导入模板");
		}else if(curValue=="CITYNETWORKQULSTATICSFILE"){
			$("#downloadHref").attr("href","fileDownloadAction?fileName=WLAN网络制式导入模板.xlsx").html("WLAN网络制式导入模板");
		}
	})
	
	//导入的校验
	$("#formImportChannelSwitch").validate({
		submitHandler : function(form) {
			$("#formImportChannelSwitch").ajaxSubmit({
				type : 'post',
				url : "uploadFileAjaxAction",
				dataType : 'text',
				success : function(data) {
					$("#importBtn").attr("disabled", "disabled");// 先禁用
					var obj;
					try {
						obj = eval("(" + data + ")");
					} catch (err) {
						$('#formImportChannelSwitch .canclear').clearFields();
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
					$('#formImportChannelSwitch .canclear').clearFields();
				}
			});
		}
	});

	//仅导入
	$("#importBtn").click(function() {
		$("#autoload").val("false");
		var area=$("#areaId").find("option:selected").text();
         $("#chareaname").val(area);
		$("#formImportChannelSwitch").submit();
		return true;
	});

	//导入并加载到分析列表
	$("#importAndLoadBtn").click(function(){
		$("#autoload").val("true");
		var area=$("#areaId").find("option:selected").text();
         $("#chareaname").val(area);
		$("#formImportChannelSwitch").submit();
		return true;
	});
	
	// 联动----区域1
	$("#provinceId").change(function() {
		getSubAreas("provinceId", "cityId", "市");
	});
	$("#cityId").change(function() {
		getSubAreas("cityId", "areaId", "区/县");
	});
	
	// 联动----区域2
	$("#provinceId2").change(function() {
		getSubAreas("provinceId2", "cityId2", "市");
	});
	$("#cityId2").change(function() {
		getSubAreas("cityId2", "areaId2", "区/县");
	});
	
	//查询条件的校验
	$("#conditionForm").validate({
		rules:{
			searchType:'required',
			'queryCondition.areaId':'required'
		},
		submitHandler:function(form){
			queryAndLoadResource();
		}
	});
	
	//查询按钮
	$("#queryAndLoadBtn").click(function(){
		//console.log($("input[name=configId]:checked").val());
		//console.log($("#configId").val());
	    if($("input[name=configId]:checked").val()==""){alert("方案不能为空!");return false;}
		$("#conditionForm").submit();
		return true;
		//queryAndLoadResource();
	});
			//刷新按钮
	$("#refreshLoadedBtn").click(function(){
			$.ajax({
				url : 'getCellHandOverAnalysisListForAjaxAction',
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
	// 从分析列表中删除
		$("#removeFromAnalysis").click(function() {
			removeFromHandOverList();
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
				$("#importResultDiv").html("服务器返回失败！");
				return;
			}
			// alert("data=="+data);
			if (obj['flag']==true) {
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
			//刷新按钮
			$("#refreshLoadedBtn").click();
			//恢复屏蔽
			$("#importBtn").removeAttr("disabled");
			$("#importResultDiv").html(data);
			// 上传按钮可用
			//$("#importResultDiv").html("文件解析结果："+data);
			/*
			var res=data;
			res=eval("("+data+")");
			if(res['msg']){
				$("#importResultDiv").html("文件解析结果："+res['msg']);
			}
			if(res['list']){
				showLoadedList("loadlistTable1",res['list']);//加载数据 chao.xj 2013-10-28
			}
			*/
			try
			{
				var res=eval("("+data+")");
				if(res['msg']){
					$("#importResultDiv").html("文件解析结果："+res['msg']);
				}
				if(res['list']){
					showLoadedList("loadlistTable1",res['list']);//加载数据 chao.xj 2013-10-28
				}
			}catch(err)
			{
				//console.log(err.name + ": " + err.message);
				$("#importResultDiv").html("文件解析结果："+data);
			}
		},
		error : function(err, status) {
			$("#importResultDiv").html("获取结果失败！");
			//console.error(status);
		},
		complete : function() {
			$("#importBtn").removeAttr("disabled");// 启用
		}
	});
}



/**
 * 查询并加载到分析列表
 */
function queryAndLoadResource() {
	$(".loading_cover").css("display", "block"); 
	var area=$("#areaId2").find('option:selected').text().trim();
	
	var chsys=$("#systemconfigure").find('option:selected').text().trim();
	var chtemp=$("#tempanalyse").find('option:selected').text().trim();
	var networkstand=$("input[name=searchType]:checked").val();
	sendDate={chareaname:area,ennetworkstand:networkstand};
	$("#conditionForm").ajaxSubmit({
				url : 'addCellConfigToAnalysisListForAjaxAction',
				dataType : 'text',
				data:sendDate,
				type : 'post',
				success : function(data) {
				   var mes_obj=eval("("+data+")");
					//console.log(mes_obj);
					showLoadedList("loadlistTable2",mes_obj);//加载数据 
				},
				error : function(err, status) {
					//console.error(status);
				},
				complete : function() {
					$(".loading_cover").css("display", "none");
				}
			});

}

/**
 * 
 * 显示加载的分析列表
 * @param tableId table id 值
 * @param data 数据数组
 * @author chao.xj 2013-10-28
 */
function showLoadedList(tableId,data){
	if (!data) {
		return;
	}
	var htmlstr="<tr><td colspan=\"5\" style=\"background: none repeat scroll 0 0  #E8EDFF;\">&nbsp; </td></tr>";//表头
	var item;
	var trClass="";
	//console.log(data.length);
	for(var i=0;i<data.length;i++){
		
		item=data[i];
		if(i%2==0){
			trClass="tb-tr-bg-coldwhite";//tr class 值
		}else{
			trClass="tb-tr-bg-warmwhite";
		}
		if (item) {
			htmlstr +="<tr class=\""+trClass+"\">"//tr html
						+ "  <td width=\"45%\" class=\"bd-right-white\" >    "
						+ "  <span >"
						+item['title']
						+"</span>"
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
						+ "  <td width=\"20%\" class=\"bd-right-white td_nowrap\">"
						+ "  <span>"
						+ item['type']
						+ "</span>"
						+ "  </td>"
						+ "  <td width=\"10%\">"
						+ "  <input type=\"button\" value=\"移除\" onclick=\"removeFromAnalist(this,\'"+data[i]['configId']+"\')\" />"
						+ "  </td >"
						+ "  </tr>"
						+"<tr><td colspan=\"5\" style=\"background-color: #e7e7e7; height:1px; width:100%\"></td> </tr> ";
		}
	}
	$("#"+tableId).html(htmlstr);//table html 内容
}
/**
 * 
 * 显示加载的刷新分析列表
 * @param tableId table id 值
 * @param data 数据数组
 * @author chao.xj 2013-10-28
 */
function showRefreshLoadedList(tableId,data){
	if (!data) {
		return;
	}
	//<tr><td colspan=\"5\" style=\"background: none repeat scroll 0 0  #E8EDFF;\">城城城城 </td></tr>
	//<input type=\"checkbox\" onclick=\"javascript:operAllCheckbox(this,2);\" name=\"selectall\" id=\"2\" /><label for=\"1\"></label>
	var htmlstr="<tr><th>标题</th><th>区域</th><th>指标日期</th><th style=\"width: 10%\">全选<input type=\"checkbox\" onclick=\"javascript:operAllCheckbox(this,3);\" name=\"selectall\" id=\"2\" /></th></tr>";//表头
	var item;
	var trClass="";
	//console.log(data.length);
	for(var i=0;i<data.length;i++){
		
		item=data[i];
		if(i%2==0){
			trClass="tb-tr-bg-coldwhite";//tr class 值
		}else{
			trClass="tb-tr-bg-warmwhite";
		}
		if (item) {
			htmlstr +="<tr class=\""+trClass+"\">"//tr html
						+ "  <td width=\"40%\" class=\"bd-right-white\" >    "
						+ "  <span >"
						+item['title']
						+"</span>"
						+ "  </td>"
						+ "  <td  width=\"25%\"  class=\"bd-right-white td_nowrap\">"
						+ "  <span >"
						+ item['areaName']
						+ "</span>"
						+ "  </td>"
						+ "  <td  width=\"20%\"  class=\"td-standard-date bd-right-white td_nowrap\">"
						+ "  <span >"
						+ item['collectTime']
						+ "</span>"
						+ "  </td>"
						+ "  <td width=\"10%\">"
						//+ "  <input type=\"button\" value=\"移除\" onclick=\"removeFromAnalist(this,\'"+data[i]['configId']+"\')\" />"
						+ "	<input type='checkbox' id='"+ data[i]['configId']+ "' class='forcheck' />";
						+ "  </td >"
						+ "  </tr>"
						+"<tr><td colspan=\"5\" style=\"background-color: #e7e7e7; height:1px; width:100%\"></td> </tr> ";
		}
	}
	$("#"+tableId).html(htmlstr);//table html 内容
}

/**
 * 从分析列表删除指定的项
 * @param obj
 * @param configId
 * @returns
 */
function removeFromAnalist(obj,configId){
	$.ajax({
		url : 'removeCellConfigAnalysisItemFromLoadedListForAjaxAction',
		data : {
			'configId' : configId
		},
		dataType : 'text',
		type : 'post',
		success : function(data) {
			var p=$(obj).parent().parent();
			$(p).remove();
		}
	});
}
// 增加一个名为 trim 的函数作为
		// String 构造函数的原型对象的一个方法。
		String.prototype.trim = function()
		{
		    // 用正则表达式将前后空格
		    // 用空字符串替代。
		    return this.replace(/(^\s*)|(\s*$)/g, "");
		}
//获取小区系统配置及临时分析方案	
function getSysCoufigureAndTempAnalyse(){
	var systemconfigure=$("#systemconfigure");
	var tempanalyse=$("#tempanalyse");
	//console.log("进来了");
	areaId=$("#areaId2").val();
	systemconfigure.empty();
	tempanalyse.empty();
	if(areaId==null){
			//console.log("hahah");
			return;
			}
		
	//systemconfigure.change(function() {
            	//var areaId=systemconfigure.val();
            	var senddata={areaId:areaId};
                $.ajax({
                    type: "POST", 
                    url: "queryCellConfigForAjaxAction",
                    data: senddata, 
                    dataType: "text",
                    async:false,
                    success: function(data, textStatus) {
                    //console.log("success");
                     //是json数据格式,普通文本格式
					//使用eval函数将mes字串,转成对应的对象 
					 var mes_obj=eval("("+data+")");
					//Js循环读取JSON数据，并增加下拉列表选项
					//console.log(mes_obj);
					var tempobj;
					for(var i=0;i<mes_obj.length;i++){ 
			 　　			　for(var key in mes_obj[i]){
			 			 
			 			if("sys"==key){
			 				//console.log("key："+key+",value："+getObjectValues(mes_obj[i][key][0])); 
			 				systemconfigure.append("<option value="+getObjectKeys(mes_obj[i][key][0])+">"+getObjectValues(mes_obj[i][key][0])+"</option>");
			 				
			 				}
			 			if("temp"==key){
			 				var aa=getObjectValues(mes_obj[i][key][i]);
			 				var bb=getObjectKeys(mes_obj[i][key][i]);
			 				for(var j=0;j<aa.length;j++){
			 				tempanalyse.append("<option value="+bb[j]+">"+aa[j]+"</option>");
			 					}
			 				}
			 			 }
                       }
                    }, 
	              error : function(XMLHttpRequest, textStatus){
	                  alert("返回数据错误：" + textStatus);
	              }
                });
                
            //});
}
//得到某个对象的属性值
function getObjectValues(object)
{
    var values = [];
    for (var property in object)
      values.push(object[property]);
    return values;
}
//得到某个对象的属性
function getObjectKeys(object)
{
    var keys = [];
    for (var property in object)
      keys.push(property);
    return keys;
}
function getSysValue(){
		
	    var a=$("#systemconfigure").val();
		
		$("#aa").val(a);
	}
	function getTempValue(){
	    var b=$("#tempanalyse").val();
	    
		$("#bb").val(b);
		
		
	}
//加载分析列表
function loadAnalyseList(){
	var configId=$("#configId");
	
	areaId=$("#areaId2").val();
	if(areaId==null){
			//console.log("hahah");
			return;
			}
		
	//systemconfigure.change(function() {
            	//var areaId=systemconfigure.val();
            	var senddata={configId:configId};
                $.ajax({
                    type: "POST", 
                    url: "addCellConfigToAnalysisListForAjaxAction",
                    data: senddata, 
                    dataType: "text",
                    async:false,
                    success: function(data, textStatus) {
                    //console.log("success");
                     //是json数据格式,普通文本格式
					//使用eval函数将mes字串,转成对应的对象 
					 var mes_obj=eval("("+data+")");
					 //console.log("success");
                    }, 
	              error : function(XMLHttpRequest, textStatus){
	                  alert("返回数据错误：" + textStatus);
	              }
                });
                
            //});
}
/**
 * 从切换统计分析列表删除指定的项
 * @param obj
 * @param configId
 * @returns
 */
function removeFromAnalist(obj,configId){
	$.ajax({
		url : 'removeChannelSwitchAnalysisItemFromLoadedListForAjaxAction',
		data : {
			'configId' : configId
		},
		dataType : 'text',
		type : 'post',
		success : function(data) {
			var p=$(obj).parent().parent();
			$(p).remove();
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
function removeFromHandOverList() {
	var ids = new Array();
	var trs = new Array();
	$("#loadRefreshlistTable3").find("input.forcheck:checked").each(function(i, ele) {
		ids.push($(ele).attr("id"));
		trs.push($(ele).closest("tr"));
	});

	if (ids.length == 0) {
		// layer.msg("请先选择需要删除的邻区关系",2,2);
		alert('请先选择需要从分析列表中删除的 切换统计项');
		return;
	}

	var ncsids = ids.join(",");
	// alert(ncellids);

	$(".loading_cover").css("display", "block");
	$("#form_tab_2").ajaxSubmit({
		url : 'removeHandOverItemFromLoadedListForAjaxAction',
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