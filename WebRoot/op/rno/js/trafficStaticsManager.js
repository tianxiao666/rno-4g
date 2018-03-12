var areaIds = [];// area ids
var areaidToBscs = []; // area id 对应bsc列表

$(document).ready(function() {

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
		for (var i in celllist) {
			//console.log("i==" + i);
			// CELL 小区中文名 LAC CI ARFCN BSIC TCH 操作
			one = celllist[i];
			tr = "<tr class=\"greystyle-standard-whitetr\">";
			tr += "<td>" + one['label'] + "</td>";
			tr += "<td>" + one['name'] + "</td>";
			tr += "<td>" + one['lac'] + "</td>";
			tr += "<td>" + one['ci'] + "</td>";
			tr += "<td>" + one['bcch'] + "</td>";
			tr += "<td>" + one['bsic'] + "</td>";
			tr += "<td>" + one['tch'] + "</td>";

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
	str += '<th style="width: 8%">ARFCN</th>';
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
function subformupdatecell(){
	var b=confirm("是否确定要修改label="+$("#label").val()+"的小区");
	if(b){return true;}
	if($("#label").val()=="" || $("#name").val()=="" || $("#lac").val()=="" || $("#ci").val()=="" || $("#bcch").val()=="" || $("#bsic").val()=="" || $("#tch").val()=="" || $("#bearing").val()=="" || $("#downtilt").val()=="" || $("#btstype").val()=="" || $("#antHeigh").val()=="" || $("#longitude").val()=="" || $("#latitude").val()=="" || $("#coverarea").val()==""){
	alert("对不起，不能为空!");
	return false;
	}
	return true;
}
//提交导入文件，检测是否为空
	function checkFile(){
	//console.log("haha");	
	}