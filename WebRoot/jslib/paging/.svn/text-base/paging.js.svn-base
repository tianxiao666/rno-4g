//var pageDivId=0;

//前台分页栏
/**
参数一：要生成分页控件位置的id名
参数二：要进行分页的集合对象
*/
function pagingColumnByForeground(divId,showList,pageSize){
	//pageDivId++;
	$("#"+divId).html("");
	var pageDiv="";
	//pagingColumnShowList=showList;
	pageDiv += "<div class='paging_div'>";
	pageDiv += " <a class='paging_link page-first' id='pagingColumnFirstPage"+divId+"' title='首页' ></a>";
	pageDiv += " <a class='paging_link page-prev' id='pagingColumnUpPage"+divId+"' title='上一页' ></a>";
	pageDiv += "<i class='paging_text'>&nbsp;第&nbsp;</i><input type='hidden' id='pagingColumnIndexPage"+divId+"' value='1'/><input type='text' class='paging_input_text' id='pagingColumnCurrentPage"+divId+"' value='1' onkeypress='return noNumbers(event)' />&nbsp;页/共&nbsp;<i class='paging_text' id='pagingColumnTotalPage"+divId+"'></i>页&nbsp;<a class='paging_link page-go' id='pagingColumnSkip"+divId+"' title='GO' >GO</a>";
	//pageDiv += "每页<input type='text' id='pagingColunmSelect"+divId+"' style='width:40px;' value='"+pageSize+"' onkeypress='return noNumbers(event)' />条记录&nbsp;|&nbsp;";
	pageDiv += "<input type='hidden' id='pagingColunmSelect"+divId+"' style='width:40px;' value='"+pageSize+"' onkeypress='return noNumbers(event)' />";
	pageDiv += " <a class='paging_link page-next' id='pagingColumnDownPage"+divId+"' title='下一页' ></a>";
	pageDiv += " <a class='paging_link page-last' id='pagingColumnLastPage"+divId+"' title='末页' ></a>";
	pageDiv += "</div>";
	$("#"+divId).html(pageDiv);
	var pagingColumnCount=divId;
	pagingColumnChangePage(showList,pagingColumnCount);
	$("#pagingColumnSkip"+pagingColumnCount).click(function(){
		pagingColumnChangePage(showList,pagingColumnCount);
	});
	$("#pagingColumnFirstPage"+pagingColumnCount).click(function(){
		showPagingColumnFirstPage(showList,pagingColumnCount);
	});
	$("#pagingColumnUpPage"+pagingColumnCount).click(function(){
		showPagingColumnUpPage(showList,pagingColumnCount);
	});
	$("#pagingColumnDownPage"+pagingColumnCount).click(function(){
		showPagingColumnDownPage(showList,pagingColumnCount);
	});
	$("#pagingColumnLastPage"+pagingColumnCount).click(function(){
		showPagingColumnLastPage(showList,pagingColumnCount);
	});
}

//分页功能
function pagingColumnChangePage(showList,pagingColumnCount){
	var list = showList;
	var count = list.length;
	var currentPage = $("#pagingColumnCurrentPage"+pagingColumnCount).val();
	if(($.trim(currentPage)=="" || parseInt(currentPage)==0) && count!=0){//yuan.yw 2013-08-14 输入框页数为0时处理
		currentPage="1"; 
		$("#pagingColumnCurrentPage"+pagingColumnCount).val("1");
	}else if(($.trim(currentPage)=="" || parseInt(currentPage)==0||!(/^[1-9]+[0-9]*$/).test(currentPage)) && parseInt(param.totalPage)==0){
		$("#pagingColumnCurrentPage"+pagingColumnCount).val("0");
		$("#pagingColumnIndexPage"+pagingColumnCount).val("0");
		return ;
	}
	/*if(!(/^[1-9]+[0-9]*$/).test(currentPage)) {
		return;
	}*/
	var pageSize = $("#pagingColunmSelect"+pagingColumnCount).val();
	var lastPage = pagingColumnTotalPage(count,pageSize);
	if(currentPage > lastPage){
		currentPage=lastPage;
		$("#pagingColumnCurrentPage"+pagingColumnCount).val(lastPage);
	}
	var currentList = doPage(list,currentPage,pageSize);
	$.each(list,function(){
		$(this).hide();
	});
	$.each(currentList,function(){
		$(this).show();
	});
	$("#pagingColumnTotalPage"+pagingColumnCount).html(lastPage);
	$("#pagingColumnIndexPage"+pagingColumnCount).val(currentPage);//当前页 隐藏域值 yuan.yw 2013-08-14
	/*
	if(currentPage==1){
		$("#pagingColumnFirstPage").attr("disabled",true);
		$("#pagingColumnUpPage").attr("disabled",true);
		$("#pagingColumnDownPage").attr("disabled",false);
		$("#pagingColumnLastPage").attr("disabled",false);
	}else if(currentPage == lastPage){
		$("#pagingColumnFirstPage").attr("disabled",false);
		$("#pagingColumnUpPage").attr("disabled",false);
		$("#pagingColumnDownPage").attr("disabled",true);
		$("#pagingColumnLastPage").attr("disabled",true);
	}else{
		$("#pagingColumnFirstPage").attr("disabled",false);
		$("#pagingColumnUpPage").attr("disabled",false);
		$("#pagingColumnDownPage").attr("disabled",false);
		$("#pagingColumnLastPage").attr("disabled",false);
	}*/
}

//显示首页
function showPagingColumnFirstPage(showList,pagingColumnCount){
	var list = showList;
	var currentPage = 1;
	var pageSize = $("#pagingColunmSelect"+pagingColumnCount).val();
	var currentList = doPage(list,currentPage,pageSize);
	$.each(list,function(){
		$(this).hide();
	});
	$.each(currentList,function(){
		$(this).show();
	});
	$("#pagingColumnCurrentPage"+pagingColumnCount).val(currentPage);
	$("#pagingColumnIndexPage"+pagingColumnCount).val(currentPage);//当前页 隐藏域值 yuan.yw 2013-08-14
	pagingColumnChangePage(showList,pagingColumnCount);
	/*
	$("#pagingColumnFirstPage").attr("disabled",true);
	$("#pagingColumnUpPage").attr("disabled",true);
	$("#pagingColumnDownPage").attr("disabled",false);
	$("#pagingColumnLastPage").attr("disabled",false);
	*/
}

//显示上一页
function showPagingColumnUpPage(showList,pagingColumnCount){
	pagingColumnChangePage(showList,pagingColumnCount);
	var list = showList;
	//var currentPage = $("#pagingColumnCurrentPage"+pagingColumnCount).val();
	var currentPage = $("#pagingColumnIndexPage"+pagingColumnCount).val();//yuan.yw 2013-08-14
	if(currentPage==1){
		return;
	}
	if(!(/^[1-9]+[0-9]*$/).test(currentPage)) {
		return;
	}
	currentPage--;
	var pageSize = $("#pagingColunmSelect"+pagingColumnCount).val();
	var currentList = doPage(list,currentPage,pageSize);
	$.each(list,function(){
		$(this).hide();
	});
	$.each(currentList,function(){
		$(this).show();
	});
	$("#pagingColumnIndexPage"+pagingColumnCount).val(currentPage);//当前页 隐藏域值 yuan.yw 2013-08-14
	$("#pagingColumnCurrentPage"+pagingColumnCount).val(currentPage);
	/*
	if(currentPage==1){
		$("#pagingColumnFirstPage").attr("disabled",true);
		$("#pagingColumnUpPage").attr("disabled",true);
		$("#pagingColumnDownPage").attr("disabled",false);
		$("#pagingColumnLastPage").attr("disabled",false);
	}else{
		$("#pagingColumnFirstPage").attr("disabled",false);
		$("#pagingColumnUpPage").attr("disabled",false);
		$("#pagingColumnDownPage").attr("disabled",false);
		$("#pagingColumnLastPage").attr("disabled",false);
	}
	*/
}

//显示下一页
function showPagingColumnDownPage(showList,pagingColumnCount){
	pagingColumnChangePage(showList,pagingColumnCount);
	var list = showList;
	var count = list.length;
	//var currentPage = $("#pagingColumnCurrentPage"+pagingColumnCount).val();
	var currentPage = $("#pagingColumnIndexPage"+pagingColumnCount).val();//yuan.yw 2013-08-14
	if(!(/^[1-9]+[0-9]*$/).test(currentPage)) {
		return;
	}
	var pageSize = $("#pagingColunmSelect"+pagingColumnCount).val();
	var lastPage = pagingColumnTotalPage(count,pageSize);
	if(currentPage >= lastPage){
		currentPage = lastPage;
	}
	if(currentPage < lastPage){
		currentPage++;
	}
	var currentList = doPage(list,currentPage,pageSize);
	$.each(list,function(){
		$(this).hide();
	});
	$.each(currentList,function(){
		$(this).show();
	});
	$("#pagingColumnIndexPage"+pagingColumnCount).val(currentPage);//当前页 隐藏域值 yuan.yw 2013-08-14
	$("#pagingColumnCurrentPage"+pagingColumnCount).val(currentPage);
	/*
	if(currentPage==lastPage){
		$("#pagingColumnFirstPage").attr("disabled",false);
		$("#pagingColumnUpPage").attr("disabled",false);
		$("#pagingColumnDownPage").attr("disabled",true);
		$("#pagingColumnLastPage").attr("disabled",true);
	}else{
		$("#pagingColumnFirstPage").attr("disabled",false);
		$("#pagingColumnUpPage").attr("disabled",false);
		$("#pagingColumnDownPage").attr("disabled",false);
		$("#pagingColumnLastPage").attr("disabled",false);
	}*/
}

//显示尾页
function showPagingColumnLastPage(showList,pagingColumnCount){
	pagingColumnChangePage(showList,pagingColumnCount);
	var list = showList;
	var count = list.length;
	var currentPage = $("#pagingColumnCurrentPage"+pagingColumnCount).val();
	if(!(/^[1-9]+[0-9]*$/).test(currentPage)) {
		return;
	}
	var pageSize = $("#pagingColunmSelect"+pagingColumnCount).val();
	var lastPage = pagingColumnTotalPage(count,pageSize);
	currentPage = lastPage;
	var currentList = doPage(list,currentPage,pageSize);
	$.each(list,function(){
		$(this).hide();
	});
	$.each(currentList,function(){
		$(this).show();
	});
	$("#pagingColumnIndexPage"+pagingColumnCount).val(currentPage);//当前页 隐藏域值 yuan.yw 2013-08-14
	$("#pagingColumnCurrentPage"+pagingColumnCount).val(currentPage);
	/*
	$("#pagingColumnFirstPage").attr("disabled",false);
	$("#pagingColumnUpPage").attr("disabled",false);
	$("#pagingColumnDownPage").attr("disabled",true);
	$("#pagingColumnLastPage").attr("disabled",true);
	*/
}

//分页逻辑
/**
参数一：要分页的集合
参数二：当前页
参数三：每页显示的数量
*/
function doPage(list,currentPage,pageCount){
	var currentIndex=(currentPage-1)*pageCount;
	var pageList=new Array();
	var count=pageCount;
	$.each(list,function(index,obj){
		if(count==0){
			return false;
		}
		if(index >= currentIndex){
			pageList.push(obj);
			count--;
		}
	});
	return pageList;
}

//计算总页数
/**
参数一：总行数
参数二:每页显示的数量
*/
function pagingColumnTotalPage(listCount,pageSize){
	var lastPage = parseInt(listCount / pageSize);
	var remainder = listCount % pageSize;
	if( remainder > 0 ){
		lastPage++;
	}
	return lastPage;
}

//==============================================================================================


//后台分页栏
/**
参数一：要生成分页控件位置的id名
参数二：显示内容的id名
参数三：action名
参数四：每页显示的数量
*/
function pagingColumnByBackgroundJsp(pageDivId,showDivId,actionName,param){
	//pageDivId++;
	var pageSize=param.pageSize;
	var params=ObjectToStr(param);
	var pageDiv="";
	pageDiv += "<div class='paging_div'>";
	pageDiv += " <a class='paging_link page-first' id='pagingColumnFirstPage"+pageDivId+"' title='首页' onclick='showPagingColumnFirstPageByBackgroundJsp(\"" + showDivId + "\",\""+actionName+"\" ,"+params+" ,\""+pageDivId+"\");' ></a>";
	pageDiv += " <a class='paging_link page-prev' id='pagingColumnUpPage"+pageDivId+"' title='上一页' onclick='showPagingColumnUpPageByBackgroundJsp(\"" + showDivId + "\",\""+actionName+"\" ,"+params+" ,\""+pageDivId+"\");' ></a>";
	pageDiv += "<i class='paging_text'>&nbsp;第&nbsp;</i><input type='hidden' id='pagingColumnIndexPage"+pageDivId+"' value='1'/><input type='text' class='paging_input_text' id='pagingColumnCurrentPage"+pageDivId+"' value='1' onkeypress='return noNumbers(event)' />&nbsp;页/共&nbsp;<i class='paging_text' id='pagingColumnTotalPage"+pageDivId+"'></i>页&nbsp;<a class='paging_link page-go' id='pagingColumnSkip"+pageDivId+"' title='GO' onclick='pagingColumnChangePageByBackgroundJsp(\"" + showDivId + "\",\""+actionName+"\" ,"+params+" ,\""+pageDivId+"\");' >GO</a>";
	//pageDiv += "每页<input type='text' id='pagingColunmSelect"+pageDivId+"' style='width:40px;' value='"+pageSize+"' onkeypress='return noNumbers(event)' >条记录&nbsp;|&nbsp;";
	pageDiv += "<input type='hidden' id='pagingColunmSelect' style='width:40px;' value='"+pageSize+"' onkeypress='return noNumbers(event)' >";
	pageDiv += " <a class='paging_link page-next' id='pagingColumnDownPage"+pageDivId+"' title='下一页' onclick='showPagingColumnDownPageByBackgroundJsp(\"" + showDivId + "\",\""+actionName+"\" ,"+params+" ,\""+pageDivId+"\");' ></a>";
	pageDiv += " <a class='paging_link page-last' id='pagingColumnLastPage"+pageDivId+"' title='末页' onclick='showPagingColumnLastPageByBackgroundJsp(\"" + showDivId + "\",\""+actionName+"\" ,"+params+" ,\""+pageDivId+"\");' ></a>";
	pageDiv += "</div>";
	$.ajax({
            url:actionName,
            async:false,
            type:"POST",
            data: param ,
            success : function(result) {
            	$("#"+pageDivId).html(pageDiv);
				$("#"+showDivId).html(result);
				var totalPage = $("#"+showDivId+" #totalPage").val();
				var currentPage=1;
				if(currentPage > totalPage){
					currentPage = totalPage;
				}
				$("#pagingColumnCurrentPage"+pageDivId).val(currentPage);
				$("#pagingColumnIndexPage"+pageDivId).val(currentPage);//当前页 隐藏域值 yuan.yw 2013-08-14
				$("#pagingColumnTotalPage"+pageDivId).html(totalPage);
            }
	});
	/*
	$.post(actionName,param,function(data){
		
	});*/
}

//后台分页-GO按钮
function pagingColumnChangePageByBackgroundJsp(showDivId,actionName,param,pageDivId){
	param.totalPage = $("#"+showDivId+" #totalPage").val();
	var pageSize = $("#pagingColunmSelect"+pageDivId).val();
	var currentPage = $("#pagingColumnCurrentPage"+pageDivId).val();
	if(($.trim(currentPage)=="" || parseInt(currentPage)==0 || !(/^[1-9]+[0-9]*$/).test(currentPage)) && parseInt(param.totalPage)!=0){//yuan.yw 2013-08-14 输入框页数为0时处理
		currentPage="1"; 
		$("#pagingColumnCurrentPage"+pageDivId).val("1");
	}else if(($.trim(currentPage)=="" || parseInt(currentPage)==0||!(/^[1-9]+[0-9]*$/).test(currentPage)) && parseInt(param.totalPage)==0){
		$("#pagingColumnCurrentPage"+pageDivId).val("0");
		$("#pagingColumnIndexPage"+pageDivId).val("0");
		return ;
	}
	param.currentPage=currentPage;
	if(param.currentPage > param.totalPage){
		param.currentPage = param.totalPage;
	}
	//param.pageSize=pageSize;
	$.ajax({
            url:actionName,
            async:false,
            type:"POST",
            data: param ,
            success : function(result) {
				$("#"+showDivId).html(result);
				var totalPage = $("#"+showDivId+" #totalPage").val();
				if(currentPage > totalPage){
					currentPage = totalPage;
				}
				$("#pagingColumnCurrentPage"+pageDivId).val(currentPage);
				$("#pagingColumnIndexPage"+pageDivId).val(currentPage);//当前页 隐藏域值 yuan.yw 2013-08-14
				$("#pagingColumnTotalPage"+pageDivId).html(totalPage);
			}
		});
}

//后台分页-首页
function showPagingColumnFirstPageByBackgroundJsp(showDivId,actionName,param,pageDivId){
	param.totalPage = $("#"+showDivId+" #totalPage").val();
	var pageSize = $("#pagingColunmSelect"+pageDivId).val();
	var currentPage = "1";
	param.currentPage=currentPage;
	//param.pageSize=pageSize;
	$.ajax({
            url:actionName,
            async:false,
            type:"POST",
            data: param ,
            success : function(result) {
				$("#"+showDivId).html(result);
				var totalPage = $("#"+showDivId+" #totalPage").val();
				if(currentPage > totalPage){
					currentPage = totalPage;
				}
				$("#pagingColumnCurrentPage"+pageDivId).val(currentPage);
				$("#pagingColumnIndexPage"+pageDivId).val(currentPage);//当前页 隐藏域值 yuan.yw 2013-08-14
				$("#pagingColumnTotalPage"+pageDivId).html(totalPage);
			}
		});
}

//后台分页-上一页
function showPagingColumnUpPageByBackgroundJsp(showDivId,actionName,param,pageDivId){
	param.totalPage = $("#"+showDivId+" #totalPage").val();
	var pageSize = $("#pagingColunmSelect"+pageDivId).val();
	//var currentPage = $("#pagingColumnCurrentPage"+pageDivId).val();
	var currentPage = $("#pagingColumnIndexPage"+pageDivId).val();// 获取隐藏域中的页数 yuan.yw 2013-08-14
	if(currentPage==1){
		return;
	}
	if(!(/^[1-9]+[0-9]*$/).test(currentPage)) {
		return;
	}
	currentPage--;
	param.currentPage=currentPage;
	//param.pageSize=pageSize;
	$.ajax({
            url:actionName,
            async:false,
            type:"POST",
            data: param ,
            success : function(result) {
				$("#"+showDivId).html(result);
				var totalPage = $("#"+showDivId+" #totalPage").val();
				if(currentPage > totalPage){
					currentPage = totalPage;
				}
				$("#pagingColumnCurrentPage"+pageDivId).val(currentPage);
				$("#pagingColumnIndexPage"+pageDivId).val(currentPage);//当前页 隐藏域值 yuan.yw 2013-08-14
				$("#pagingColumnTotalPage"+pageDivId).html(totalPage);
			}
		});
}

//后台分页-下一页
function showPagingColumnDownPageByBackgroundJsp(showDivId,actionName,param,pageDivId){
	param.totalPage = $("#"+showDivId+" #totalPage").val();
	var pageSize = $("#pagingColunmSelect"+pageDivId).val();
	//var currentPage = $("#pagingColumnCurrentPage"+pageDivId).val();
	var currentPage = $("#pagingColumnIndexPage"+pageDivId).val();// 获取隐藏域中的页数 yuan.yw 2013-08-14
	if(!(/^[1-9]+[0-9]*$/).test(currentPage)) {
		return;
	}
	currentPage++;
	if(currentPage > param.totalPage){
		currentPage = param.totalPage;
	}
	param.currentPage=currentPage;
	//param.pageSize=pageSize;
	$.ajax({
            url:actionName,
            async:false,
            type:"POST",
            data: param ,
            success : function(result) {
				$("#"+showDivId).html(result);
				var totalPage = $("#"+showDivId+" #totalPage").val();
				if(currentPage > totalPage){
					currentPage = totalPage;
				}
				$("#pagingColumnCurrentPage"+pageDivId).val(currentPage);
				$("#pagingColumnIndexPage"+pageDivId).val(currentPage);//当前页 隐藏域值 yuan.yw 2013-08-14
				$("#pagingColumnTotalPage"+pageDivId).html(totalPage);
			}
		});
}

//后台分页-尾页
function showPagingColumnLastPageByBackgroundJsp(showDivId,actionName,param,pageDivId){
	param.totalPage = $("#"+showDivId+" #totalPage").val();
	var pageSize = $("#pagingColunmSelect"+pageDivId).val();
	var currentPage = $("#pagingColumnCurrentPage"+pageDivId).val();
	if(!(/^[1-9]+[0-9]*$/).test(currentPage)) {
		return;
	}
	currentPage=param.totalPage;
	param.currentPage=currentPage+"";
	//param.pageSize=pageSize;
	$.ajax({
            url:actionName,
            async:false,
            type:"POST",
            data: param ,
            success : function(result) {
				$("#"+showDivId).html(result);
				var totalPage = $("#"+showDivId+" #totalPage").val();
				if(currentPage > totalPage){
					currentPage = totalPage;
				}
				$("#pagingColumnCurrentPage"+pageDivId).val(currentPage);
				$("#pagingColumnIndexPage"+pageDivId).val(currentPage);//当前页 隐藏域值 yuan.yw 2013-08-14
				$("#pagingColumnTotalPage"+pageDivId).html(totalPage);
			}
		});
}

//===================================================================================

//后台分页栏
/**
参数一：要生成分页控件位置的id名
参数二：要进行分页的集合对象
*/
function pagingColumnByBackground(pageDivId,showDivId,actionName,param){
	var pageSize=param.pageSize;
	var params=ObjectToStr(param);
	var pageDiv="";
	pageDiv += "<div class='paging_div'>";
	pageDiv += " <a class='paging_link page-first' id='pagingColumnFirstPage' title='首页' onclick='showPagingColumnFirstPageByBackground(\"" + showDivId + "\",\""+actionName+"\" ,"+params+");' ></a>";
	pageDiv += " <a class='paging_link page-prev' id='pagingColumnUpPage' title='上一页' onclick='showPagingColumnUpPageByBackground(\"" + showDivId + "\",\""+actionName+"\" ,"+params+");' ></a>";
	pageDiv += "<i class='paging_text'>&nbsp;第&nbsp;</i><input type='text' class='paging_input_text' id='pagingColumnCurrentPage' value='1' onkeypress='return noNumbers(event)' />&nbsp;页/共&nbsp;<i class='paging_text' id='pagingColumnTotalPage'></i>页&nbsp;<a class='paging_link page-go' id='pagingColumnSkip' title='GO' onclick='pagingColumnChangePageByBackground(\"" + showDivId + "\",\""+actionName+"\" ,"+params+");' >GO</a>";
	//pageDiv += "每页<input type='text' id='pagingColunmSelect' style='width:40px;' value='"+pageSize+"' onkeypress='return noNumbers(event)' >条记录&nbsp;|&nbsp;";
	pageDiv += "<input type='hidden' id='pagingColunmSelect' style='width:40px;' value='"+pageSize+"' onkeypress='return noNumbers(event)' />";
	pageDiv += " <a class='paging_link page-next' id='pagingColumnDownPage' title='下一页' onclick='showPagingColumnDownPageByBackground(\"" + showDivId + "\",\""+actionName+"\" ,"+params+");' ></a>";
	pageDiv += " <a class='paging_link page-last' id='pagingColumnLastPage' title='末页' onclick='showPagingColumnLastPageByBackground(\"" + showDivId + "\",\""+actionName+"\" ,"+params+");' ></a>";
	pageDiv += "</div>";
	$("#"+pageDivId).html(pageDiv);
	
	$.post(actionName,param,function(data){
		$("#"+showDivId).html(data.showDiv);
		var totalPage = data.totalPage;
		var currentPage=1;
		if(currentPage > totalPage){
			currentPage = totalPage;
		}
		$("#pagingColumnCurrentPage").val(currentPage);
		$("#pagingColumnTotalPage").html(totalPage);
	},'json');
}

//后台分页-GO按钮
function pagingColumnChangePageByBackground(showDivId,actionName,param){
	var pageSize = $("#pagingColunmSelect").val();
	var currentPage = $("#pagingColumnCurrentPage").val();
	param.currentPage=currentPage;
	param.pageSize=pageSize;
	$.post(actionName,param,function(data){
		$("#"+showDivId).html(data.showDiv);
		var totalPage = data.totalPage;
		if(currentPage > totalPage){
			currentPage = totalPage;
		}
		$("#pagingColumnCurrentPage").val(currentPage);
		$("#pagingColumnTotalPage").html(totalPage);
	},'json');
}

//后台分页-首页
function showPagingColumnFirstPageByBackground(showDivId,actionName,param){
	var pageSize = $("#pagingColunmSelect").val();
	var currentPage = 1;
	param.currentPage=currentPage;
	param.pageSize=pageSize;
	$.post(actionName,param,function(data){
		$("#"+showDivId).html(data.showDiv);
		var totalPage = data.totalPage;
		if(currentPage > totalPage){
			currentPage = totalPage;
		}
		$("#pagingColumnCurrentPage").val(currentPage);
		$("#pagingColumnTotalPage").html(totalPage);
	},'json');
}

//后台分页-上一页
function showPagingColumnUpPageByBackground(showDivId,actionName,param){
	var pageSize = $("#pagingColunmSelect").val();
	var currentPage = $("#pagingColumnCurrentPage").val();
	if(currentPage==1){
		return;
	}
	if(!(/^[1-9]+[0-9]*$/).test(currentPage)) {
		return;
	}
	currentPage--;
	param.currentPage=currentPage;
	param.pageSize=pageSize;
	$.post(actionName,param,function(data){
		$("#"+showDivId).html(data.showDiv);
		var totalPage = data.totalPage;
		if(currentPage > totalPage){
			currentPage = totalPage;
		}
		$("#pagingColumnCurrentPage").val(currentPage);
		$("#pagingColumnTotalPage").html(totalPage);
	},'json');
}

//后台分页-下一页
function showPagingColumnDownPageByBackground(showDivId,actionName,param){
	var pageSize = $("#pagingColunmSelect").val();
	var currentPage = $("#pagingColumnCurrentPage").val();
	if(!(/^[1-9]+[0-9]*$/).test(currentPage)) {
		return;
	}
	currentPage++;
	param.currentPage=currentPage;
	param.pageSize=pageSize;
	$.post(actionName,param,function(data){
		$("#"+showDivId).html(data.showDiv);
		var totalPage = data.totalPage;
		if(currentPage > totalPage){
			currentPage = totalPage;
		}
		$("#pagingColumnCurrentPage").val(currentPage);
		$("#pagingColumnTotalPage").html(totalPage);
	},'json');
}

//后台分页-尾页
function showPagingColumnLastPageByBackground(showDivId,actionName,param){
	var pageSize = $("#pagingColunmSelect").val();
	var currentPage = $("#pagingColumnCurrentPage").val();
	if(!(/^[1-9]+[0-9]*$/).test(currentPage)) {
		return;
	}
	currentPage=2147483647;
	param.currentPage=currentPage+"";
	param.pageSize=pageSize;
	$.post(actionName,param,function(data){
		$("#"+showDivId).html(data.showDiv);
		var totalPage = data.totalPage;
		if(currentPage > totalPage){
			currentPage = totalPage;
		}
		$("#pagingColumnCurrentPage").val(currentPage);
		$("#pagingColumnTotalPage").html(totalPage);
	},'json');
}

function noNumbers(e)   
{   
	var keynum;
	var keychar;
	var numcheck;
	  
	if(window.event) // IE   
	{   
		keynum = e.keyCode;
	}   
	else if(e.which) // Netscape/Firefox/Opera   
	{   
		keynum = e.which;
	}
	keychar = String.fromCharCode(keynum);
	numcheck = /\d/;
	return numcheck.test(keychar) || e.keyCode == 8;  
} 

function ObjectToStr(o) {
    if (o == undefined) {
        return "";
    }
    var r = [];
    if (typeof o == "string") return "\"" + o.replace(/([\"\\])/g, "\\$1").replace(/(\n)/g, "\\n").replace(/(\r)/g, "\\r").replace(/(\t)/g, "\\t") + "\"";
    if (typeof o == "object") {
        if (!o.sort) {
            for (var i in o)
                r.push("\"" + i + "\":" + ObjectToStr(o[i]));
            if (!!document.all && !/^\n?function\s*toString\(\)\s*\{\n?\s*\[native code\]\n?\s*\}\n?\s*$/.test(o.toString)) {
                r.push("toString:" + o.toString.toString());
            }
            r = "{" + r.join() + "}"
        } else {
            for (var i = 0; i < o.length; i++)
                r.push(ObjectToStr(o[i]))
            r = "[" + r.join() + "]";
        }
        return r;
    }
    return o.toString().replace(/\"\:/g, '":""');
}