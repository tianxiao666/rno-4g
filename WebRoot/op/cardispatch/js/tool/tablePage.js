//全量分页
function FullDosePage ( $pageSize , $msgArray ) {
	var pageSize;			//每页显示数量
	var pageIndex;			//当前显示页码
	var maxPage;			//最大页数
	var maxMsg;				//最大信息数量
	var msgArray;			//信息数组
	
	{
		this.pageSize = $pageSize;
		this.pageIndex = 0;
		this.msgArray = $msgArray;
	}
	
	
	this.setPageSize = function ( $pageSize ) {
		this.pageSize = $pageSize;
		return this.getPageSize();
	}
	this.getPageSize = function () {return this.pageSize;}
	//获取当前显示页码
	this.getPageIndex = function () {
		if ( this.getMaxMsg() == null ) {
			alert("getMaxPage is null ");
			return "0";
		}
		if ( ( this.pageIndex + 1 ) > this.getMaxPage() ) {
			this.pageIndex = this.getMaxPage() - 1;
		}else if(this.pageIndex==-1 && this.getMaxPage()!=0){//yuan.yw 2013-8-14 输入框页数为0
			this.pageIndex=0;
		}
		return this.pageIndex + 1;
	}
	//设置当前显示页码
	this.setPageIndex = function( index ){
		if(!(/^[1-9]+[0-9]*$/).test(index)){//非数字
			index=0;
		}
		this.pageIndex = index - 1 ;
		return this.getPageIndex();
	}
	//获取最大页码
	this.getMaxPage = function () {
		if ( this.getMaxMsg() == null ) {
			alert("getMaxMsg is null ");
			return "-1";
		}
		this.maxPage = Math.ceil( this.getMaxMsg() / this.pageSize );
		if ( this.maxPage <= 0 ) {
			this.maxPage = 0;//yuan.yw 2013-8-14 默认为0
		}
		return this.maxPage;
	}
	//获取最大信息数量
	this.getMaxMsg = function() {
		if ( this.msgArray == null ) {
			this.maxMsg = 0;
		} else {
			this.maxMsg = this.msgArray.length;
		}
		return this.maxMsg;
	}
	//设置信息数组
	this.setMsgArray = function ($msgArray) {
		this.msgArray = $msgArray;
		return this;
	}
	//获取信息数组
	this.getMsgArray = function () {return this.maxArray;}
	
	//添加数据
	this.addData2Array = function ( $data ) {
		this.msgArray.push($data);
	}
	
	//返回要显示的信息数组
	this.getShowArray = function () {
		var from = this.pageIndex * this.pageSize;
		var end = parseInt(from) + (this.pageSize - 1) ;
		var newShowArray = {};
		for ( var i = 0 ; i < this.msgArray.length ;i++ ) {
			if ( i < from ) {
				continue;
			}
			if ( i > end ) {
				break;
			}
			if ( !this.msgArray[i] ) {
				break;
			}
			newShowArray[i] = this.msgArray[i];
		}
		return newShowArray;
	}
	
	this.delData2Array = function ( check ) {
		for ( var i = 0 ; i < this.msgArray.length ; i++ ) {
			var flag = check(this.msgArray[i]);
			if ( flag ) {
				this.msgArray.splice(i,1);
			}
		}
	}
	
	this.addData2Array = function ( result , check ) {
		for ( var i = 0 ; i < this.msgArray.length ; i++ ) {
			var flag = check(this.msgArray[i]);
			if ( !!flag ) {
				break;
			} else if ( !flag && i == this.msgArray.length - 1) {
				this.msgArray.push(result);
				break;
			}
		}
		if ( this.msgArray.length == 0 ) {
			this.msgArray.push(result);
		}
	}
	
	//下一页
	this.nextPage = function () {
		this.pageIndex++;
		if ( this.getPageIndex() > this.getMaxPage() ) {
			this.lastPage();
		}
		return this.getPageIndex();
	}
	//上一页
	this.prevPage = function(){
		this.pageIndex--;
		if ( this.getPageIndex() <= 0 ) {
			this.firstPage();
		}
		
		return this.getPageIndex();
	}
	//首页
	this.firstPage = function () {
		this.pageIndex = 0;
		return this.getPageIndex();
	}
	//尾页
	this.lastPage = function () {
		this.pageIndex = this.getMaxPage() - 1 ;
		return this.getPageIndex();
	}
	//判断是否尾页
	this.isLastPage = function () {
		return this.getPageIndex() == this.getMaxPage();
	}
	//判断是否首页
	this.isFirstPage = function () {
		return this.getPageIndex() == 1;
	}
}

//分页工具
function TablePage ( opt ) {
	var pageSize;					//每页显示数量
	var fdPage;
	var dataArray;
	var div;
	var table;
	var tbody;
	var footDiv;
	var pageSize_select;
	var instance;
	var showSpeed;
	var effect;
	var tableId;
	
	{
		this.showSpeed = 1200;
		var defaultOpt = {
			"pageSize" : 5 , 
			"dataArray" : new Array()
		};
		$.extend(defaultOpt,opt);
		this.pageSize = opt.pageSize;
		this.dataArray = opt["dataArray"];
		this.fdPage = new FullDosePage( this.pageSize , this.dataArray );
		this.table = $(opt.table);
		this.tbody = $(opt.table).find("tbody");
		this.tableId = $(opt.table).attr("id");
		this.div = $(this.table).parent();
		this.columnMethod = function (i,key,rowData,tr){return "";};
		if ( opt.columnMethod ) {
			this.columnMethod = opt.columnMethod;
		}
		if ( opt.pageSize ) {
			this.pageSize_select = opt.pageSize;
		} else {
			this.pageSize_select = 5;
		}
		if ( !opt.effect ) {
			this.effect = 0;
		} else {
			this.effect = opt.effect;
		}
		
		instance = this;
	}
	//初始化操作
	this.init = function () {
		$(this.table).wrap($(div));
		this.createTable();
		//添加组件
		this.createPageWidget();
		$(this.footDiv).find(".pageSize_select").change(function(){
			instance.refreshTable();
			instance.checkButton();
		});
	}
	
	//创建表格
	this.createTable = function () {
		$(this.tbody).empty();
		if ( !this.dataArray ) {
			return;
		}
		var i = 0;
		var arr = this.fdPage.getShowArray();
		for( var key in arr ) {
			var tr = $("<tr/>").appendTo($(this.tbody));
			if ( instance.effect == 1 ) {
				tr.hide();
			}
			var rowData = arr[key];
			var tdEle = this.columnMethod(i,key,rowData,tr);
			if ( instance.effect == 1 ) {
				$(tr).show(this.showSpeed);
			}
			//$(tr).slideRow('down'); 
			i++;
		}
	} 
	
	//创建分页组件
	this.createPageWidget = function () {
	
		
		
		instance.footDiv = $("<div/>").attr({"class":$(instance.div).attr("id")+" paging_div"}).css({"text-align":"right"})
		var foot = this.footDiv;
		$(this.table).after($(foot));
		
		var firstBtn = $("<a class='paging_link page-first'/>").val("首页");
		$(foot).append($(firstBtn));
		$(firstBtn).bind("click",{"data" : instance},function($event){
			var ins = $event.data["data"];
			ins.firstPage();
		});
		var prevBtn = $("<a class='paging_link page-prev'/>").val("上一页");
		$(prevBtn).appendTo($(foot));
		$(prevBtn).bind("click",{"data" : instance},function($event){
			var ins = $event.data["data"];
			ins.prevPage();
		});
		
		var paging_text = $("<i class='paging_text'>&nbsp;第&nbsp;</i>").appendTo($(foot));
		var pagingInput = $("<input style='width:14px; height:12px;' type='text' class='paging_input_text' id='"+this.tableId +"_pagingColumnCurrentPage' value='0' onkeypress='return noNumbers(event)'/>&nbsp;页").appendTo($(foot));
		$(foot).append($("<span>/共&nbsp;</span>"));
		var totalPage = $("<i class='paging_text' id='" + this.tableId + "_pagingColumnTotalPage'></i>页&nbsp;").appendTo($(foot));
		var gotobtn = $("<a class='paging_link page-go' id='pagingColumnSkip"+this.pageSize_select+"' title='GO' >GO</a>").appendTo($(foot));
		
		$(pagingInput).click(function(){
			$(this).select();
		});
		
		$(gotobtn).bind("click",function(){
			var pageIndex = $(instance.footDiv).find("#"+instance.tableId +"_pagingColumnCurrentPage").val();
			instance.goToPage(pageIndex);
		});
		
		var nextBtn = $("<a class='paging_link page-next'/>").val("下一页");
		$(nextBtn).appendTo($(foot));
		$(nextBtn).bind("click",{"data" : instance},function($event){
			var ins = $event.data["data"];
			ins.nextPage();
		});
		var lastBtn = $("<a class='paging_link page-last'/>").val("尾页");
		$(lastBtn).appendTo($(foot));
		$(lastBtn).bind("click",{"data" : instance},function($event){
			var ins = $event.data["data"];
			ins.lastPage();
		});
	}
	
	//首页
	this.firstPage = function () {
		var pageIndex = this.fdPage.firstPage();
		var fd = this.footDiv;
		this.refreshTable();
		this.checkButton();
	}
	
	//尾页
	this.lastPage = function () {
		var pageIndex = this.fdPage.lastPage();
		var fd = this.footDiv;
		this.checkButton();
		this.refreshTable();
	}
	
	//上一页
	this.prevPage = function () {
		var pageIndex = this.fdPage.prevPage();
		var fd = this.footDiv;
		this.checkButton();
		this.refreshTable();
	}
	
	//下一页
	this.nextPage = function () {
		var pageIndex = this.fdPage.nextPage();
		var fd = this.footDiv;
		this.checkButton();
		this.refreshTable();
	}
	
	//TODO
	this.goToPage = function(index){
		this.fdPage.setPageIndex(index);
		this.checkButton();
		this.refreshTable();
	}
	
	
	//检验按钮
	this.checkButton = function () {
		var fd = this.footDiv;
		this.fdPage.getMaxPage();
		if ( this.fdPage.isLastPage() ) {
			$(fd).find(".lastfirstPage_btn").attr({"disabled":"disabled"});
			$(fd).find(".nextPage_btn").attr({"disabled":"disabled"});
		} else {
			$(fd).find(".lastfirstPage_btn").removeAttr("disabled");
			$(fd).find(".nextPage_btn").removeAttr("disabled");
		}
		if ( this.fdPage.isFirstPage() ) {
			$(fd).find(".firstPage_btn").attr({"disabled":"disabled"});
			$(fd).find(".prevPage_btn").attr({"disabled":"disabled"});
		} else {
			$(fd).find(".firstPage_btn").removeAttr("disabled");
			$(fd).find(".prevPage_btn").removeAttr("disabled");
		}
		this.refreshTable();
	}
	
	//设置数据数组
	this.setDataArray = function($dataArray){
		this.dataArray = $dataArray;
		this.fdPage.setMsgArray($dataArray);
		return this.$dataArray;
	}
	
	this.addData2Array = function ( result , check ) {
		this.fdPage.addData2Array(result , check);
	}
	
	this.delData2Array = function ( check ) {
		this.fdPage.delData2Array( check );
	}
	//刷新表格
	this.refreshTable = function(){
		this.fdPage.setPageSize(this.pageSize_select);
		
		var pageTxt = $(this.footDiv).find(".pageIndex");
		var pIndex = this.fdPage.getPageIndex();
		$("#"+this.tableId +"_pagingColumnCurrentPage").val(pIndex);
		
		var pMax = this.fdPage.getMaxPage();
		$("#"+this.tableId + "_pagingColumnTotalPage").text(pMax);
		
		
		$(this.footDiv).find(".maxPage").html( "/" + this.fdPage.getMaxPage() + "&nbsp;");
		this.createTable();
		//this.checkButton();
		var len = $(this.tbody).find("tr").length;
		var cp = $(this.table).find("thead td,th").length;
		if ( len == 0 ) {
			var tr = $("<tr/>");
			$(tr).appendTo($(this.tbody));
			var td = $("<td/>");
			$(td).attr({"colspan":cp}).css({"text-align":"center"});
			$(tr).append($(td));
			$(td).text("暂无数据！");
			$(tr).hide();
			$(tr).show(this.showSpeed);
		}
	}
	
	this.showLoading = function(){
		var cp = $(this.table).find("thead td,th").length;
		$(this.tbody).empty();
		var tr = $("<tr/>");
		$(tr).appendTo($(this.tbody));
		var td = $("<td/>");
		$(td).attr({"colspan":cp});
		$(td).appendTo($(tr));
		var img = $("<img/>").attr({"src":"images/page_loading.gif"});
		$(img).appendTo($(td));
	}
	
	//调用方法
	this.init();	
	this.checkButton();
}

/**
 * @description 限制页数输入数字
 * @author yuan.yw
 * @param {} e
 * @return {}
 */
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

