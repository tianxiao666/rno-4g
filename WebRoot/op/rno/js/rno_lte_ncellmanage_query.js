//显示同一站的小区
var currentCellArray = null;
var currentIndex = 0;

var lastCond=new Object();

$(document)
		.ready(
				function() {		
					    
					// 查询条件
					$("#conditionForm").submit(function() {
						// 重新初始化分页参数
						initFormPage('conditionForm');
						$("span#nameErrorText1").html("");
						 $("span#nameErrorText2").html("");
						//记录
						lastCond['queryProvinceId']=$("#queryProvinceId").val();
						lastCond['queryCityId']=$("#queryCityId").val();
						lastCond['queryCellNameId']=$("#queryCellNameId").val();
						lastCond['queryNCellNameId']=$("#queryNCellNameId").val();
						lastCond['queryCellSiteId']=$("#queryCellSiteId").val();
						lastCond['queryNCellSiteId']=$("#queryNCellSiteId").val();
						
						//console.log("条件："+$("#queryProvinceId").val()+" "+$("#queryCityId").val()+" "+
						//				$("#queryCellNameId").val()+""+$("#queryNCellNameId").val()+" "+$("#queryCellSiteId").val()+" "+$("#queryNCellSiteId").val())
						var queryProvinceId = $("#queryProvinceId").val();
						var queryCityId = $("#queryCityId").val();
						var queryCellNameId = $("#queryCellNameId").val();
						 var queryNCellNameId = $("#queryNCellNameId").val();
						 var queryCellSiteId = $("#queryCellSiteId").val();
						 var queryNCellSiteId = $("#queryNCellSiteId").val();
						 var str = queryProvinceId + queryCityId + queryCellNameId + queryNCellNameId + queryCellSiteId + queryNCellSiteId;
						 /*var pattern = new RegExp("[\\\\`~*#!@%&'|{}\":<>.,;+/=?$￥！()（）【】，。：；”‘？》《^]+");*/
						 var strExp=/^[\u4e00-\u9fa5A-Za-z0-9_-]+$/;
						  if(!strExp.test(str)){
							   	$("span#nameErrorText1").html("含有非法字符！");
							   	  return false;   
							 }  else if(!(queryCellNameId.length<40&&queryNCellNameId.length<40&&
									 queryCellSiteId.length<40&&queryNCellSiteId.length<40)){
								 $("span#nameErrorText2").html("输入信息过长！");
							      return false;
						     }   
							 
						queryLteNcell();
						return false;
					});

					// 查询的区域联动
					$("#queryProvinceId")
							.change(
									function() {
										if ($("#queryProvinceId").val() == '-1') {
											$("#queryCityId")
													.append(
															"<option value='-1' selected='true'>全部</option>")
										} else {
											getSubAreas(
													"queryProvinceId",
													"queryCityId",
													"市",
													function() {
														$("#queryCityId")
																.append(
																		"<option value='-1' selected='true'>全部</option>")
													});
										}
									});

			});
// 初始化form下的page信息
function initFormPage(formId) {
	var form = $("#" + formId);
	if (!form) {
		return;
	}
	form.find("#hiddenPageSize").val(25);
	form.find("#hiddenCurrentPage").val(1);
	form.find("#hiddenTotalPageCnt").val(-1);
	form.find("#hiddenTotalCnt").val(-1);
}

/**
 * 获取Lte邻区关系
 */
function queryLteNcell() {
	$(".loading_cover").css("display", "block");
	$("#conditionForm").ajaxSubmit({
		url : 'queryLteNcellByPageForAjaxAction',
		dataType : 'text',
		success : function(data) {
			showLteNCell(data);
		},
		complete : function() {
			$(".loading_cover").css("display", "none");
		},error : function() {
			
		}
	});

}

/**
 * 显示查询回来的Lte邻区
 * 
 * @param {}
 *            data {'page':{},'celllist':[]}
 */
function showLteNCell(data) {

	data = eval("(" + data + ")");
//	 console.log("data===" + data);
	// 准备填充小区
	var page = data['page'];
	var celllist = data['data'];

	var table = $("#queryResultTab");
	// 只保留表头
	$("#queryResultTab tr:not(:first)").each(function(i, ele) {
		$(ele).remove();
	});
	// console.log("celllist====" + celllist);
	if (celllist) {
		var one;
		var tr;
		for ( var i = 0; i < celllist.length; i++) {
			// console.log("i==" + i);
			one = celllist[i];
			if (!one) {
				continue;
			}

			tr = "<tr class=\"greystyle-standard-whitetr\">";
			tr += "<td>" + getValidValue(one['LTE_CELL_NAME']) + "</td>";
			tr += "<td>" + getValidValue(one['LTE_NCELL_NAME']) + "</td>";
			tr += "<td>" + getValidValue(one['LTE_CELL_ID']) + "</td>";
			tr += "<td>" + getValidValue(one['LTE_CELL_ENODEB_ID']) + "</td>";
			tr += "<td>" + getValidValue(one['LTE_NCELL_ID']) + "</td>";
			tr += "<td>" + getValidValue(one['LTE_NCELL_ENODEB_ID']) + "</td>";
			tr += "<td>" + getValidValue(one['LTE_CELL_SITE_ID']) + "</td>";
			tr += "<td>" + getValidValue(one['LTE_NCELL_SITE_ID']) + "</td>";

			tr += "<td><a style='margin-left:20px' onclick='deleteLteNcell(\""+one['LTE_NCELL_RELA_ID']+"\");'>删除</a></td>";

			tr += "</tr>";
			// console.log("tr===" + tr);
			table.append($(tr));// 增加
		}
	}

	// 设置隐藏的page信息
	setFormPageInfo("conditionForm", data['page']);

	// 设置分页面板
	setPageView(data['page'], "lteCellPageDiv");

}


// 设置formid下的page信息
// 其中，当前页会加一
function setFormPageInfo(formId, page) {
	if (formId == null || formId == undefined || page == null
			|| page == undefined) {
		return;
	}

	var form = $("#" + formId);
	if (!form) {
		return;
	}

	// console.log("setFormPageInfo .
	// pageSize="+page.pageSize+",currentPage="+page.currentPage+",totalPageCnt="+page.totalPageCnt+",totalCnt="+page.totalCnt);
	form.find("#hiddenPageSize").val(page.pageSize);
	form.find("#hiddenCurrentPage").val(new Number(page.currentPage));// /
	form.find("#hiddenTotalPageCnt").val(page.totalPageCnt);
	form.find("#hiddenTotalCnt").val(page.totalCnt);

	// alert("after set currentpage in form is
	// :"+form.find("#hiddenCurrentPage").val());
}

/**
 * 设置分页面板
 * 
 * @param page
 *            分页信息
 * @param divId
 *            分页面板id
 */
function setPageView(page, divId) {
	if (page == null || page == undefined) {
		return;
	}

	var div = $("#" + divId);
	if (!div) {
		return;
	}

	//
	var pageSize = page['pageSize'] ? page['pageSize'] : 0;
	// $("#hiddenPageSize").val(pageSize);

	var currentPage = page['currentPage'] ? page['currentPage'] : 1;
	// $("#hiddenCurrentPage").val(currentPage);

	var totalPageCnt = page['totalPageCnt'] ? page['totalPageCnt'] : 0;
	// $("#hiddenTotalPageCnt").val(totalPageCnt);

	var totalCnt = page['totalCnt'] ? page['totalCnt'] : 0;
	// $("#hiddenTotalCnt").val(totalCnt);

	// 设置到面板上
	$(div).find("#emTotalCnt").html(totalCnt);
	$(div).find("#showCurrentPage").val(currentPage);
	$(div).find("#emTotalPageCnt").html(totalPageCnt);
}


//删除lte小区
function deleteLteNcell(lteNcellRelaId){
	$.ajax({
		url:'deleteLteNcellByPageForAjaxAction',
		data:{'ids':lteNcellRelaId},
		dataType : 'text',
		success : function(raw) {
			// console.log(data);
			//重新查询
			var data=null;
			try{
				data=eval("("+raw+")");
				if(!data){
					alert("未知错误！请联系管理员！");
				}else{
					if(data['flag']==true){
						//
						alert("删除成功！");
						
						//重新执行查询
						var table=$("#conditionForm");
						
						table.find("#hiddenTotalCnt").val('-1');//表示在后台要重新计算总数量
						//当前有多少条记录
						var rowcnt=document.getElementById('queryResultTab').rows.length;
						if(rowcnt>2){
							//此时重新查询就可以了。
						}else{
							var curpage=new Number(table.find("#hiddenCurrentPage").val());
							//该页只有一行，如果当前不是第一页的话，就将页数减一，重新查询
							if(curpage>1){
								table.find("#hiddenCurrentPage").val(curpage-1);//减一页
							}
						}
						
						//设置查询条件，不直接获取，防止查询后用户有修改，然后再删除记录的情况
						$("#queryProvinceId").val(lastCond['queryProvinceId']);
						$("#queryCityId").val(lastCond['queryCityId']);
						$("#queryEnodebNameId").val(lastCond['queryEnodebNameId']);
						$("#queryCellNameId").val(lastCond['queryCellNameId']);
						$("#queryCellPciId").val(lastCond['queryCellPciId']);
						
						queryLteNcell();
					}else{
						alert("删除失败！"+data['msg']);
					}
				}
			}catch(err){
			    alert("未知错误！请联系管理员！");
			}
		},
		complete : function() {
			$(".loading_cover").css("display", "none");
		}
	});
}



// 跳转
function showListViewByPage(dir) {
	var pageSize = new Number($("#hiddenPageSize").val());
	var currentPage = new Number($("#hiddenCurrentPage").val());
	var totalPageCnt = new Number($("#hiddenTotalPageCnt").val());
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
	} else {
		return;
	}
	// 获取资源
	queryLteNcell();
}