

$(document).ready(function() {
			//网络制式 单选按钮点击事件 显示不同下载模板
			/*$("input[name='fileCode']").click(function(){
				var curValue = $(this).val();
				if(curValue=="GSMCELLFILE"){
					$("#downloadHref").attr("href","fileDownloadAction?fileName=GSM小区信息导入模板.xlsx").html("GSM小区信息导入模板");
				}else if(curValue=="TDCELLFILE"){
					$("#downloadHref").attr("href","fileDownloadAction?fileName=TD小区信息导入模板.xlsx").html("TD小区信息导入模板");
				}else if(curValue=="WLANCELLFILE"){
					$("#downloadHref").attr("href","fileDownloadAction?fileName=WLAN小区信息导入模板.xlsx").html("WLAN小区信息导入模板");
				}
			})*/
			// 查询条件
			$("#conditionForm").submit(function() {
					 //重新初始化分页参数
					     $("#hiddenPageSize").val("25");
		                 $("#hiddenCurrentPage").val("1");
		                 $("#hiddenTotalPageCnt").val("0");
		                 $("#hiddenTotalCnt").val("-1");
						getResource();
						return false;
					});

			//区域联动1
			$("#provinceId").change(function() {
				getSubAreas("provinceId", "cityId", "市");
			});
			$("#cityId").change(function() {
				getSubAreas("cityId", "queryCellAreaId", "区/县",function(){
					$("#queryCellAreaId").append("<option value='-1' selected='true'>全部</option>")
				});
			});
			
			//区域联动2
			$("#provinceId2").change(function() {
				getSubAreas("provinceId2", "cityId2", "市");
			});
			$("#cityId2").change(function() {
				getSubAreas("cityId2", "areaId2", "区/县");
			});
			
			//区域联动3
			$("#provinceId3").change(function() {
				getSubAreas("provinceId3", "cityId3", "市");
			});
			$("#cityId3").change(function() {
				getSubAreas("cityId3", "areaId3", "区/县");
			});
			
			// 查询面板的区域下拉框的响应
			$("#queryCellAreaId").change(function() {
						//adaptBsc();
					});
					
			//新增单个BSC
			$("#addBscBtn").click(function() {
				addSingleBsc();
			});

});


// 跳转
function showListViewByPage(dir) {
	var pageSize =new Number($("#hiddenPageSize").val());
	var currentPage = new Number($("#hiddenCurrentPage").val());
	var totalPageCnt =new Number($("#hiddenTotalPageCnt").val());
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
	}else{
		return;
	}
	//获取资源
	getResource();
}

/**
 * 获取BSC
 */
function getResource() {
	showOperTips("loadingDataDiv", "loadContentId", "正在加载");
	$("#conditionForm").ajaxSubmit({
				url : 'queryBscByPageForAjaxAction',
				dataType : 'text',
				success : function(data) {
					showBsc(data);
				},
				complete : function() {
					hideOperTips("loadingDataDiv");
				}
	});

}

/**
 * 显示查询回来的BSC
 */
function showBsc(data) {

	data = eval("(" + data + ")");

	// 准备填充小区
	var page = data['page'];
	var bsclist = data['bscList'];

	//console.log("page===" + page);
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
	//清空详情列表
	$("#queryResultTab tr:gt(0)").remove();
	var cityName = $("#cityId").find("option:selected").text();
	var cityId = $("#cityId").find("option:selected").val();
	if (bsclist) {
		var one;
		var tr;
		for (var i=0; i<bsclist.length; i++) {
			//console.log("i==" + i);
			// CELL 小区中文名 LAC CI ARFCN BSIC TCH 操作
			one = bsclist[i];
			if(!one){
				continue;
			}
			tr = "<tr class=\"greystyle-standard-whitetr\">";
			tr += "<td>" + getValidValue(cityName,'') + "</td>";
			tr += "<td>" + getValidValue(one['ENGNAME'],'') + "</td>";
			tr += "<td>" + getValidValue(one['MANUFACTURERS'],'') + "</td>";
			tr += "<td>	<a style='text-decoration:underline;' onclick='deleteBsc(\""+one['ENGNAME']+"\","+cityId+")'>删除</a></td>";
			tr += "</tr>";
			//console.log("tr===" + tr);
			table.append($(tr));// 增加
		}
	}
}

/**
 * 删除BSC
 */
function deleteBsc(bsc,cityId) {

	showOperTips("loadingDataDiv", "loadContentId", "正在执行删除");
	if(!confirm("确实要删除吗?")){
		hideOperTips("loadingDataDiv");
	 	return;
	}
         
	$.ajax({
		url : 'deleteBscByNameForAjaxAction',
		dataType : 'text',
		data : {
			'bscEngName' : bsc,
			'cityId' : cityId
		},
		success : function(raw) {
			var data = eval("("+raw+")");
			var flag = data['flag'];
			var msg = data['msg'];
			if(flag){
				 //刷新
				 //重新初始化分页参数
				 $("#hiddenPageSize").val("25");
	             $("#hiddenCurrentPage").val("1");
	             $("#hiddenTotalPageCnt").val("0");
	             $("#hiddenTotalCnt").val("-1");
				 getResource();
			} else {
				alert("删除网元失败!原因：" + msg);
			}
		},
		complete : function() {
			hideOperTips("loadingDataDiv");
		}
	});
}

/**
 * 新增单个BSC
 */
function addSingleBsc() {
	var cityId = $("#cityId2").val();
	var bscEngName = $("#bscEngName").val().trim();
	var manufacturers = $("#manufacturers").val();
	if(cityId == null || cityId == "") {
		alert("城市不能为空！");
		return ;
	}
	if(bscEngName == null || bscEngName == "") {
		alert("请输入BSC名称！");
		return ;
	}
	showOperTips("loadingDataDiv", "loadContentId", "正在新增BSC");
	$("#addSingleBscForm").ajaxSubmit({
				url : 'addSingleBscAjaxForAction',
				data : {
					'cityId' : cityId,
					'bscEngName' : bscEngName,
					'manufacturers' : manufacturers
				},
				dataType : 'text',
				success : function(raw) {
					var data = eval("("+raw+")");
					var flag = data['flag'];
					if(flag) {
						alert("加入成功!");
						 //刷新
						 //重新初始化分页参数
						 $("#hiddenPageSize").val("25");
			             $("#hiddenCurrentPage").val("1");
			             $("#hiddenTotalPageCnt").val("0");
			             $("#hiddenTotalCnt").val("-1");
						 getResource();
					} else {
						alert("加入失败!" );
					}
				},
				complete : function() {
					$("#addSingleBscDiv").hide();
					hideOperTips("loadingDataDiv");
				}
	});
}
