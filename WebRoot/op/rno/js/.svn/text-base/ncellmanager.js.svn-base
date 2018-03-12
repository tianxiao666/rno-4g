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
	                 
	                 $("#tipcontentId").html("正在查询邻区数据...");
						getResource();
						return false;
					});

			initAreaCascade();
			
			
		   //删除所选择的邻区关系
			$("#deleteNcell").click(function(){
				$("#tipcontentId").html("正在执行删除邻区关系数据...");
			      deleteNcell();
			});

		});

// 随区域选择不同而变化
function adaptBsc() {

	// 获取区域下的bsc，并把结果缓存

	$("#queryCellBscId").empty();

	$("#queryCellBscId").append("<option value='-1'>请选择</option> ");
	// 缓存有无
	var areaId = $("#areaId1").val();
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


function initAreaCascade() {
	// 区域联动事件
	$("#provinceId1").change(function() {
		getSubAreas("provinceId1", "cityId1", "市");
	});

	$("#cityId1").change(function() {
		getSubAreas("cityId1", "areaId1", "区/县",function(){
			$("#areaId1").append("<option value='-1' selected='true'>全部</option>")
		});
	});
	
	$("#areaId1").change(function() {
		// 查询面板的区域下拉框的响应
		adaptBsc();
	});

	$("#provinceId2").change(function() {
		getSubAreas("provinceId2", "cityId2", "市");
	});

	$("#cityId2").change(function() {
		getSubAreas("cityId2", "areaId2", "区/县");
	});
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
function showNCell(data) {
//	console.log("data:"+data);
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
		for (var i=0; i<celllist.length;i++) {
			//console.log("i==" + i);
			// CELL 小区中文名 LAC CI ARFCN BSIC TCH 操作
			one = celllist[i];
			tr = "<tr class=\"greystyle-standard-whitetr\">";
			tr += "<td>" + one['cell'] + "</td>";
			tr += "<td>" + one['ncell'] + "</td>";
			tr += "<td>" + one['cs'] + "</td>";
			tr += "<td>" + one['dir'] + "</td>";

			tr += '<td><input type="hidden" value="'+one['ncellId']+'" /><input type="checkbox" class="ncellid" name="1" id="'+one['ncellId']+'" /><label for="1"></label></td>';
		

			tr += "</tr>";
			//console.log("tr===" + tr);
			table.append($(tr));// 增加
		}
	}

}

// 表头
function getHead() {
	var str = '<th style="width: 20%">主小区</th>';
	str += '<th style="width: 30%">邻小区</th>';
	str += '<th style="width: 30%">是否同site</th>';
	str += '<th style="width: 30%">邻区方向</th>';
	str+='<th style="width: 10%">全选<input type="checkbox" onclick="javascript:operAllCheckbox(this);" name="1" id="1" /><label for="1"></label></th>';
	return str;

}

function operAllCheckbox(obj){
	//alert($(obj).attr("checked"));
	if($(obj).attr("checked")){
		$("input.ncellid").attr("checked","checked");
	}else{
		$("input.ncellid").attr("checked",false);
	}
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
				url : 'queryNCellByPageForAjaxAction',
				dataType : 'text',
				success : function(data) {
					// console.log(data);
					showNCell(data);
				},
				complete : function() {
					$(".loading_cover").css("display", "none");
				}
			});

}

//删除所选择的邻区关系
function deleteNcell(){
	//alert("delete e");
	var ids=new Array();
	$("#queryResultTab").find("input.ncellid:checked").each(function(i,ele){
		 ids.push($(ele).attr("id"));
	});
	
	if(ids.length==0){
		//layer.msg("请先选择需要删除的邻区关系",2,2);
		alert('请先选择需要删除的邻区关系');
		return;
	}
	
	var ncellids=ids.join(",");
	//alert(ncellids);
	
	
	$(".loading_cover").css("display", "block");
	$("#conditionForm").ajaxSubmit({
				url : 'deleteNcellForAjaxAction',
				dataType : 'text',
				data:{'ncellIds':ncellids},
				success : function(data) {
					// console.log(data);
					showNCell(data);
				},
				complete : function() {
					$(".loading_cover").css("display", "none");
				}
			});
	
}

