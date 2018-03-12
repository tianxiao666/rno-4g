var pagingColumnShowList;

//前台分页栏
/**
参数一：要生成分页控件位置的id名
参数二：要进行分页的集合对象
*/
function pagingColumnByForeground(divId,showList){
	$("#"+divId).html("");
	var pageDiv="";
	pagingColumnShowList=showList;
	pageDiv += "<div>";
	pageDiv += "每页<input type='text' id='pagingColunmSelect' style='width:40px;' value='10' >条记录&nbsp;|&nbsp;";
	pageDiv += "第<input type='text' id='pagingColumnCurrentPage' value='1' style='width:32px;' />/<em id='pagingColumnTotalPage'></em>页<input type='button' id='pagingColumnSkip' value='GO' onclick=\"pagingColumnChangePage();\" />";
	pageDiv += "<input type='button' id='pagingColumnFirstPage' value='首  页' onclick=\"showPagingColumnFirstPage();\" />";
	pageDiv += "<input type='button' id='pagingColumnUpPage' value='上一页' onclick=\"showPagingColumnUpPage();\" />";
	pageDiv += "<input type='button' id='pagingColumnDownPage' value='下一页' onclick='showPagingColumnDownPage();' />";
	pageDiv += "<input type='button' id='pagingColumnLastPage' value='尾  页' onclick=\"showPagingColumnLastPage();\" />";
	pageDiv += "</div>";
	$("#"+divId).html(pageDiv);
	pagingColumnChangePage();
}

//分页功能
function pagingColumnChangePage(){
	var list = pagingColumnShowList;
	var count = list.length;
	var currentPage = $("#pagingColumnCurrentPage").val();
	if(!(/^[1-9]+[0-9]*$/).test(currentPage)) {
		return;
	}
	var pageSize = $("#pagingColunmSelect").val();
	var lastPage = pagingColumnTotalPage(count,pageSize);
	if(currentPage > lastPage){
		currentPage=lastPage;
		$("#pagingColumnCurrentPage").val(lastPage);
	}
	var currentList = doPage(list,currentPage,pageSize);
	$.each(list,function(){
		$(this).hide();
	});
	$.each(currentList,function(){
		$(this).show();
	});
	$("#pagingColumnTotalPage").html(lastPage);
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
function showPagingColumnFirstPage(){
	var list = pagingColumnShowList;
	var currentPage = 1;
	var pageSize = $("#pagingColunmSelect").val();
	var currentList = doPage(list,currentPage,pageSize);
	$.each(list,function(){
		$(this).hide();
	});
	$.each(currentList,function(){
		$(this).show();
	});
	$("#pagingColumnCurrentPage").val(currentPage);
	pagingColumnChangePage();
	/*
	$("#pagingColumnFirstPage").attr("disabled",true);
	$("#pagingColumnUpPage").attr("disabled",true);
	$("#pagingColumnDownPage").attr("disabled",false);
	$("#pagingColumnLastPage").attr("disabled",false);
	*/
}

//显示上一页
function showPagingColumnUpPage(){
	var list = pagingColumnShowList;
	var currentPage = $("#pagingColumnCurrentPage").val();
	if(!(/^[2-9]+[0-9]*$/).test(currentPage)) {
		return;
	}
	currentPage--;
	var pageSize = $("#pagingColunmSelect").val();
	var currentList = doPage(list,currentPage,pageSize);
	$.each(list,function(){
		$(this).hide();
	});
	$.each(currentList,function(){
		$(this).show();
	});
	$("#pagingColumnCurrentPage").val(currentPage);
	pagingColumnChangePage();
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
function showPagingColumnDownPage(){
	var list = pagingColumnShowList;
	var count = list.length;
	var currentPage = $("#pagingColumnCurrentPage").val();
	if(!(/^[1-9]+[0-9]*$/).test(currentPage)) {
		return;
	}
	var pageSize = $("#pagingColunmSelect").val();
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
	$("#pagingColumnCurrentPage").val(currentPage);
	pagingColumnChangePage();
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
function showPagingColumnLastPage(){
	var list = pagingColumnShowList;
	var count = list.length;
	var currentPage = $("#pagingColumnCurrentPage").val();
	if(!(/^[1-9]+[0-9]*$/).test(currentPage)) {
		return;
	}
	var pageSize = $("#pagingColunmSelect").val();
	var lastPage = pagingColumnTotalPage(count,pageSize);
	currentPage = lastPage;
	var currentList = doPage(list,currentPage,pageSize);
	$.each(list,function(){
		$(this).hide();
	});
	$.each(currentList,function(){
		$(this).show();
	});
	$("#pagingColumnCurrentPage").val(currentPage);
	pagingColumnChangePage();
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

//后台分页栏
/**
参数一：要生成分页控件位置的id名
参数二：要进行分页的集合对象
*/
function pagingColumnByBackground(pageDivId,showDivId,actionName,param,aa){
	$("#"+showDivId).html("");
	$("#"+pageDivId).html("");
	var pageDiv="";
	pageDiv += "<div>";
	pageDiv += "每页<input type='text' id='pagingColunmSelect' style='width:40px;' value='40' >条记录&nbsp;|&nbsp;";
	pageDiv += "第<input type='text' id='pagingColumnCurrentPage' value='1' style='width:32px;' />/<em id='pagingColumnTotalPage'></em>页<input type='button' id='pagingColumnSkip' value='GO' onclick=\"pagingColumnChangePage();\" />";
	pageDiv += "<input type='button' id='pagingColumnFirstPage' value='首  页' onclick=\"showPagingColumnFirstPage();\" />";
	pageDiv += "<input type='button' id='pagingColumnUpPage' value='上一页' onclick=\"showPagingColumnUpPage();\" />";
	pageDiv += "<input type='button' id='pagingColumnDownPage' value='下一页' onclick='showPagingColumnDownPage();' />";
	pageDiv += "<input type='button' id='pagingColumnLastPage' value='尾  页' onclick=\"showPagingColumnLastPage();\" />";
	pageDiv += "</div>";
	$("#"+pageDivId).html(pageDiv);
	$.post(actionName,param,function(data){
		$("#"+showDivId).html(data);
	});
}

function pagingColumnByBackground(pageDivId,showDivId,actionName,pageSize){
	var pageDiv="";
	pageDiv += "<div>";
	pageDiv += "每页<input type='text' id='pagingColunmSelect' style='width:40px;' value='"+pageSize+"' >条记录&nbsp;|&nbsp;";
	pageDiv += "第<input type='text' id='pagingColumnCurrentPage' value='1' style='width:32px;' />/<em id='pagingColumnTotalPage'></em>页<input type='button' id='pagingColumnSkip' value='GO' onclick=\"pagingColumnChangePage();\" />";
	pageDiv += "<input type='button' id='pagingColumnFirstPage' value='首  页' onclick=\"showPagingColumnFirstPage();\" />";
	pageDiv += "<input type='button' id='pagingColumnUpPage' value='上一页' onclick=\"showPagingColumnUpPage();\" />";
	pageDiv += "<input type='button' id='pagingColumnDownPage' value='下一页' onclick='showPagingColumnDownPage();' />";
	pageDiv += "<input type='button' id='pagingColumnLastPage' value='尾  页' onclick=\"showPagingColumnLastPage();\" />";
	pageDiv += "</div>";
	$("#"+pageDivId).html(pageDiv);
	$.post(actionName,{currentPage:"1",pageSize:pageSize},function(data){
		$("#"+showDivId).html(data);
		pagingColumnChangePageByBackground();
	});
}

//后台分页功能
function pagingColumnChangePageByBackground(){
	var totalPage = $("#totalPage");
	$("#pagingColumnTotalPage").html(totalPage);
}