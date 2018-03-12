
	var $ = jQuery.noConflict();
	$(function() {
		//tab选项卡
		tab("div_tab1", "li", "onclick");//项目服务范围类别切换

	})
	window.onchange=function(){
	//getSysValue();getTempValue();
	}
	$(function(){
	//刷新按钮
	$("#refreshLoadedBtn").click();
	$("#queryCellConfigureBtn").click(function(){
			// 查询提交
			//alert("1");
	$("#form_tab_2").submit();
		//return true;
		
	});
	//校验
	//$("#form_tab_2").validate({
		//submitHandler : function(form) {
	//ajaxSubmit,submit
	$("#form_tab_2").submit(function() {
	     //alert("2");
	 	 //重新初始化分页参数
	     $("#tab_2_hiddenPageSize").val("25");
         $("#tab_2_hiddenCurrentPage").val("1");
         $("#tab_2_hiddenTotalPageCnt").val("0");
         $("#tab_2_hiddenTotalCnt").val("0");
		getCellConfigureData("form_tab_2","tab_2_");
		return false;
	});
	//}
	//});
	
	/**
 * 获取小区
 */
function getCellConfigureData(formId,tabFlag) {
	$(".loading_cover").css("display", "block");
	/*
	var stsusl="";
	if(formId=="form_tab_2"){
				stsusl="queryRnoCityNetQualityIndexByPageAction";
				}else{
				stsusl="queryRnoStsListByPageAction";
				}
				//console.log(stsusl);*/
	var selval=$("#"+formId +" #areaId2").find("option:selected").val();
	
	var str="";
	var areastr="";
	if("-1"==selval){
		$("#"+formId +" #areaId2").find("option").not(':last').each(function(i,e){
			var eleval=$(e).val();
			str+=eleval+",";
		});
		areastr=str.substring(0,str.length-1);
	}else{
		areastr=selval;
	}
	//console.log("areastr:"+areastr);
	var sendData={"areaIds":areastr};
	$("#"+formId).ajaxSubmit({
				//url : "'"+stsusl+"'",
				url : 'queryCellConfigureDescListByPageAction',
				data:sendData,
				dataType : 'json',
				success : function(data) {
					console.log(data);
					showSts(data,tabFlag,formId);
				},
				complete : function() {
					$(".loading_cover").css("display", "none");
				}
			});
		}
function showSts(data,tabFlag,formId){
	var page = data.newPage;
	var hodescLists = data.celldescLists;
	var colorarr=["#96C4EB","#DBEBF8"];
	//console.log("page===" + page);
	if (page) {
		var pageSize = page.pageSize ? page.pageSize : 0;
		$("#"+tabFlag+"hiddenPageSize").val(pageSize);

		var currentPage = page.currentPage && pageSize!=0 ? page.currentPage : 1;
		$("#"+tabFlag+"hiddenCurrentPage").val(currentPage);

		var totalPageCnt = page.totalPageCnt ? page.totalPageCnt : 0;
		$("#"+tabFlag+"hiddenTotalPageCnt").val(totalPageCnt);

		var totalCnt = page['totalCnt'] ? page['totalCnt'] : 0;
		$("#"+tabFlag+"hiddenTotalCnt").val(totalCnt);

		// 跳转
		$("#"+tabFlag+"emTotalCnt").html(totalCnt);
		$("#"+tabFlag+"showCurrentPage").val(currentPage);
		$("#"+tabFlag+"emTotalPageCnt").html(totalPageCnt);

	}
		var table = $("#"+tabFlag+"queryResultTab");
	// 只保留表头
	//table.empty();
	//table.append(getTableHead());
	var i=0;
	if(formId=="form_tab_2"){
	// 只保留表头
	table.empty();
	table.append(getTableHead());
			if (hodescLists) {
		var tr="";
		//var STS_DATE="<s:date name='1999/02/06' format='yyyy-MM-dd'/>";
		//var helloworld = "<s\:text name=\"<s\:date name=\"1999/02/06\" format=\"yyyy-MM-dd\"/>\"/>";
		$.each(hodescLists,function(key,value){
		//alert(new Date(value.STS_DATE));
		    //console.log(i+":"+colorarr[i%2]);
		    //class=\"greystyle-standard-whitetr\"
			tr += "<tr  style=\"background-color:"+colorarr[i%2]+"\">";
			tr += "<td>" + value.AREANAME + value.CREATE_TIME +"</td>";
			tr += "<td>" + value.NAME + "</td>";
			tr += "<td>" + value.CREATE_TIME + "</td>";
			tr += "<td><input type='checkbox' id='loaditem' name='loaditem' value='"+value.CELL_DESCRIPTOR_ID+"'/></td>";
			tr += "</tr>";
			//table.append(tr);
			//console.log(tr);
			i++;
		})
		table.append(tr);
	 }
	}
	}
function getTableHead() {
	//console.log("来了");
	var str = '<th style="width: 10%">标题</th>';
	str += '<th style="width: 8%">方案名称</th>';
	str += '<th style="width: 8%">上传时间</th>';
	str += '<th style="width: 10%">加载项</th>';
	return str;

}
// 跳转
function showListViewByPage(dir,formId,tabFlag) {
	var pageSize =new Number($("#"+tabFlag+"hiddenPageSize").val());
	var currentPage = new Number($("#"+tabFlag+"hiddenCurrentPage").val());
	var totalPageCnt =new Number($("#"+tabFlag+"hiddenTotalPageCnt").val());
	var totalCnt = new Number($("#"+tabFlag+"hiddenTotalCnt").val());

	if (dir === "first") {
		if (currentPage <= 1) {
			return;
		} else {
			$("#"+tabFlag+"hiddenCurrentPage").val("1");
		}
	} else if (dir === "last") {
		if (currentPage >= totalPageCnt) {
			return;
		} else {
			$("#"+tabFlag+"hiddenCurrentPage").val(totalPageCnt);
		}
	} else if (dir === "back") {
		if (currentPage <= 1) {
			return;
		} else {
			$("#"+tabFlag+"hiddenCurrentPage").val(currentPage - 1);
		}
	} else if (dir === "next") {
		if (currentPage >= totalPageCnt) {
			return;
		} else {
			$("#"+tabFlag+"hiddenCurrentPage").val(currentPage + 1);
		}
	} else if (dir === "num") {
		var userinput = $("#"+tabFlag+"showCurrentPage").val();
		if (isNaN(userinput)) {
			alert("请输入数字！")
			return;
		}
		if (userinput > totalPageCnt || userinput < 1) {
			alert("输入页面范围不在范围内！");
			return;
		}
		$("#"+tabFlag+"hiddenCurrentPage").val(userinput);
	}else{
		return;
	}
	//获取资源
	getCellConfigureData(formId,tabFlag);
}

$("#appendlist").click(function(){
var s='';
var items=$('input[name="loaditem"]:checked');
var len=items.length;
items.each(function(i){
	if(i==len-1){
	s+=$(this).val();
	}else{
    s+=$(this).val()+',';
    }
});
//console.log(s==''?'你还没有选择任何内容！':s);
if(s==''){alert("你还没有选择任何内容！");return;}
var senddata={loaditem:s};
console.log(senddata);
	$.ajax({
				//url : "'"+stsusl+"'",
				url : 'appendRnoHandOverDescListForAjaxAction',
				dataType : 'text',
				type : 'post',
				data:senddata,
				success : function(data) {
					var res=eval("("+data+")");
					alert(res[0]['res']);
					
				},
				complete : function() {
					$(".loading_cover").css("display", "none");
				}
			});
				
		});
		$("#loadtoanalysisBtn").click(function(){
		//console.log("loadtoanalysisBtn入口");
				var s='';
		var items=$('input[name="loaditem"]:checked');
		var len=items.length;
		items.each(function(i){
			if(i==len-1){
			s+=$(this).val();
			}else{
		    s+=$(this).val()+',';
		    }
		});
		//console.log(s==''?'你还没有选择任何内容！':s);
		if(s==''){alert("你还没有选择任何内容！");return;}
		var senddata=s;
		
		//console.log(senddata);
		//return;
	$.ajax({
				//url : "'"+stsusl+"'",
				url : 'addCellConfigDescToAnalysisListForAjaxAction',
				dataType : 'text',
				type : 'post',
				data:{configIds:senddata},
				success : function(data) {
					var res=eval("("+data+")");
					//alert("写入成功!");
					animateOperTips("添加完成!");
					//刷新按钮
					$("#refreshLoadedBtn").click();
					//初始化小区配置信息
					initCellConfigTab();
					getReSelCellDescIdVal(items);
					/*var sarr=s.split(",");
					for(var i=0;i<sarr.length;i++){
					
						reSelCellDescId.push(sarr[i]);
					}*/
				},
				complete : function() {
					$(".loading_cover").css("display", "none");
				}
			});
		});
	});
