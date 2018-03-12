var areaIds = [];// area ids
var areaidToBscs = []; // area id 对应bsc列表

$(document).ready(function() {
			//网络制式 单选按钮点击事件 显示不同下载模板
			$("input[name='fileCode']").click(function(){
				var curValue = $(this).val();
				if(curValue=="GSMCELLFILE"){
					$("#downloadHref").attr("href","fileDownloadAction?fileName=GSM小区信息导入模板.xlsx").html("GSM小区信息导入模板");
				}else if(curValue=="TDCELLFILE"){
					$("#downloadHref").attr("href","fileDownloadAction?fileName=TD小区信息导入模板.xlsx").html("TD小区信息导入模板");
				}else if(curValue=="WLANCELLFILE"){
					$("#downloadHref").attr("href","fileDownloadAction?fileName=WLAN小区信息导入模板.xlsx").html("WLAN小区信息导入模板");
				}
			})
			// 查询条件
			$("#conditionForm").submit(function() {
					 //重新初始化分页参数
					     $("#hiddenPageSize").val("25");
		                 $("#hiddenCurrentPage").val("1");
		                 $("#hiddenTotalPageCnt").val("0");
		                 $("#hiddenTotalCnt").val("0");
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
			
			//进入页面就加载一次
			adaptBsc();
			// 查询面板的区域下拉框的响应
			$("#queryCellAreaId").change(function() {
						adaptBsc();
					});

		});

// 随区域选择不同而变化
function adaptBsc() {

	// 获取区域下的bsc，并把结果缓存

	$("#queryCellBscId").empty();

	$("#queryCellBscId").append("<option value='-1'>请选择</option> ");
	// 缓存有无
	var areaId = $("#queryCellAreaId").val();
	if(areaId == -1) {
		areaId = $("#cityId").val();
	}
	//console.log(areaId);
	if (areaId != -1) {
		var i = 0;
		for (i = 0; i < areaIds.length; i++) {
			if (areaIds[i] == areaId) {
				var bscs = areaidToBscs[i];
				appendBscs(bscs);// 添加
				//console.log("get bscs from page cache");
				break;
			}
		}
		if (i >= areaIds.length) {
			//console.log("try to get bscs from server");
			// 缓存无数据
			// 从服务器加载
			$.ajax({
						url : 'getBscsResideInAreaForAjaxAction',
						data : {
							'areaId' : areaId
						},
						dataType : 'text',
						type : 'post',
						success : function(data) {
							var obj = eval('(' + data + ')');
							if (isArray(obj)) {
								// 加入缓存
								areaIds.push(areaId);
								areaidToBscs.push(obj);
								// 添加
								appendBscs(obj);
							} else {
								//
							}
						}
					});
		}
	}

}
// 判断是array
var isArray = function(obj) {
	return Object.prototype.toString.call(obj) === '[object Array]';
}

// 将bsc填充到下拉框中
function appendBscs(bscs) {
	for (var j = 0; j < bscs.length; j++) {
		$("#queryCellBscId").append("<option value='" + bscs[j]['bscId'] + "'>"
				+ bscs[j]['engname'] + "</option>");
	}
}

/**
 * 显示查询回来的小区
 * 
 * @param {}
 *            data {'page':{},'celllist':[]}
 */
function showCell(data) {

	data = eval("(" + data + ")");
	//console.log("data===" + data);
	// 准备填充小区
	var page = data['page'];
	var celllist = data['celllist'];

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
	// 只保留表头
	table.empty();
	table.append(getHead());

	//console.log("celllist====" + celllist);
	if (celllist) {
		var one;
		var tr;
		for (var i=0;i<celllist.length;i++) {
			//console.log("i==" + i);
			// CELL 小区中文名 LAC CI ARFCN BSIC TCH 操作
			one = celllist[i];
			if(!one){
				continue;
			}
			
			tr = "<tr class=\"greystyle-standard-whitetr\">";
			tr += "<td>" + getValidValue(one['label']) + "</td>";
			tr += "<td>" + getValidValue(one['name']) + "</td>";
			tr += "<td>" + getValidValue(one['lac']) + "</td>";
			tr += "<td>" + getValidValue(one['ci']) + "</td>";
			tr += "<td>" + getValidValue(one['bcch']) + "</td>";
			tr += "<td>" + getValidValue(one['bsic']) + "</td>";
			tr += "<td>" + getValidValue(one['tch']) + "</td>";


			tr += "<td>	<a target='_blank'  href=\""+tomodifyCell(one['id'],one['areaId'])+"\"><p><img src=\"../../images/edit-go.png\" align=\"absmiddle\"	width=\"16\" height=\"16\" alt=\"查看/编辑明细\" />	修改</p> </a></td>";

			tr += "</tr>";
			//console.log("tr===" + tr);
			table.append($(tr));// 增加
		}
	}

}

// 表头
function getHead() {
	var str = '<th style="width: 8%">CELL</th>';
	str += '<th style="width: 10%">小区中文名</th>';
	str += '<th style="width: 8%">LAC</th>';
	str += '<th style="width: 8%">CI</th>';
	str += '<th style="width: 8%">BCCH</th>';
	str += '<th style="width: 10%">BSIC</th>';
	str += '<th style="width: 10%">TCH</th>';
	str += '<th style="width: 10%">操作</th>';
	return str;

}

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
 * 获取小区
 */
function getResource() {
	$(".loading_cover").css("display", "block");
	$("#conditionForm").ajaxSubmit({
				url : 'queryCellByPageForAjaxAction',
				dataType : 'text',
				success : function(data) {
					// console.log(data);
					showCell(data);
				},
				complete : function() {
					$(".loading_cover").css("display", "none");
				}
			});

}

// 修改小区
function tomodifyCell(cellId,areaId) {
     var href='/ops/op/rno/initModifyCellPageAction?cellId='+cellId+"&areaId="+areaId;
     return href;
}
//提交更改小区数据，验证是否为空
// 增加一个名为 trim 的函数作为
// String 构造函数的原型对象的一个方法。
String.prototype.trim = function()
{
    // 用正则表达式将前后空格
    // 用空字符串替代。
    return this.replace(/(^\s*)|(\s*$)/g, "");
}
function subformupdatecell(){
	
	if($("#label").val().trim()=="" || $("#name").val().trim()=="" || $("#lac").val().trim()=="" || $("#ci").val().trim()=="" || $("#bcch").val().trim()=="" || $("#bsic").val().trim()=="" || $("#tch").val().trim()=="" || $("#bearing").val().trim()=="" || $("#downtilt").val().trim()=="" || $("#btstype").val().trim()=="" || $("#antHeigh").val().trim()=="" || $("#longitude").val().trim()=="" || $("#latitude").val().trim()=="" || $("#coverarea").val().trim()==""){
	alert("对不起，不能为空!");
	return false;
	}
	
	if(360<$("#bearing").val().trim() || $("#bearing").val().trim()<0){alert("方向角的范围是0-360");return false;}
	if(65535<$("#ci").val().trim() || $("#ci").val().trim()<0){alert("CI的范围是0-65535");return false;}
	if(65535<$("#lac").val().trim() || $("#lac").val().trim()<1){alert("LAC的范围是1-65535");return false;}
	if(135.04166666667<$("#longitude").val().trim() || $("#longitude").val().trim()<73.666666666667){alert("中国经度LON的范围是东经73.666666666667-135.04166666667");return false;}
	if(53.55<$("#latitude").val().trim() || $("#latitude").val().trim()<3.8666666666667){alert("中国纬度LAT的范围是北纬3.8666666666667-53.55");return false;}
	var b=confirm("是否确定要修改cell="+$("#label").val()+"的小区");
	if(b){return true;}else{return false;}
	
	return true;
}
