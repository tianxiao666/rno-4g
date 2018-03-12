var fileSize = 0;
var stopQueryProgress=false;///停止查询进度
var interval = 2000;// 周期性获取文件导出进度情况的
$(document).ready(function() {

	// 设置jquery ui
	jqueryUiSet();
	//绑定事件
	bindEvent();
	//获取爱立信小区2G查询列表
	get2GEriCellQueryAllListInfo();
	//初始化uploadCityId
	$("#uploadCityId").val($("#citymenu").val());
	//cell 下的弹出窗事件
	bindSelectEvent("cell_moreBsc", "cell_bscWinDiv", "cell_waitforselBsc", "cell_selectedBsc", "cell_selAllBsc",
					"cell_partOfSelBsc", "cell_delAllBsc", "cell_partOfDelBsc", "cell_confirmBsc",
					"cell_cancelBsc", "cell_targetBsc");
	bindSelectEvent("cell_moreDate", "cell_dateWinDiv", "cell_waitforselDate", "cell_selectedDate", "cell_selAllDate",
			"cell_partOfSelDate", "cell_delAllDate", "cell_partOfDelDate", "cell_confirmDate",
			"cell_cancelDate", "cell_targetDate");
	bindTextToSelectEvent("cell_moreCell","cell_cellWinDiv","cell_confirmCell","cell_cellInput","cell_targetCell");
	bindSelectEvent("cell_moreParam", "cell_paramWinDiv", "cell_waitforselParam", "cell_selectedParam", "cell_selAllParam",
			"cell_partOfSelParam", "cell_delAllParam", "cell_partOfDelParam", "cell_confirmParam",
			"cell_cancelParam", "cell_targetParam");
	//channel 下的弹出窗事件
	bindSelectEvent("channel_moreBsc", "channel_bscWinDiv", "channel_waitforselBsc", "channel_selectedBsc", "channel_selAllBsc",
					"channel_partOfSelBsc", "channel_delAllBsc", "channel_partOfDelBsc", "channel_confirmBsc",
					"channel_cancelBsc", "channel_targetBsc");
	bindSelectEvent("channel_moreDate", "channel_dateWinDiv", "channel_waitforselDate", "channel_selectedDate", "channel_selAllDate",
			"channel_partOfSelDate", "channel_delAllDate", "channel_partOfDelDate", "channel_confirmDate",
			"channel_cancelDate", "channel_targetDate");
	bindTextToSelectEvent("channel_moreCell","channel_cellWinDiv","channel_confirmCell","channel_cellInput","channel_targetCell");
	bindSelectEvent("channel_moreParam", "channel_paramWinDiv", "channel_waitforselParam", "channel_selectedParam", "channel_selAllParam",
			"channel_partOfSelParam", "channel_delAllParam", "channel_partOfDelParam", "channel_confirmParam",
			"channel_cancelParam", "channel_targetParam");
	//ncell 下的弹出窗事件
	bindSelectEvent("ncell_moreBsc", "ncell_bscWinDiv", "ncell_waitforselBsc", "ncell_selectedBsc", "ncell_selAllBsc",
					"ncell_partOfSelBsc", "ncell_delAllBsc", "ncell_partOfDelBsc", "ncell_confirmBsc",
					"ncell_cancelBsc", "ncell_targetBsc");
	bindSelectEvent("ncell_moreDate", "ncell_dateWinDiv", "ncell_waitforselDate", "ncell_selectedDate", "ncell_selAllDate",
			"ncell_partOfSelDate", "ncell_delAllDate", "ncell_partOfDelDate", "ncell_confirmDate",
			"ncell_cancelDate", "ncell_targetDate");
	bindTextToSelectEvent("ncell_moreCell","ncell_cellWinDiv","ncell_confirmCell","ncell_cellInput","ncell_targetCell");
	bindSelectEvent("ncell_moreParam", "ncell_paramWinDiv", "ncell_waitforselParam", "ncell_selectedParam", "ncell_selAllParam",
			"ncell_partOfSelParam", "ncell_delAllParam", "ncell_partOfDelParam", "ncell_confirmParam",
			"ncell_cancelParam", "ncell_targetParam");
	bindTextToSelectEvent("ncell_moreNcell","ncell_ncellWinDiv","ncell_confirmNcell","ncell_ncellInput","ncell_targetNcell");
	
	$("#cell_loadMoreDate").click(function(){
		var cityId=$("#citymenu").find("option:selected").val();
		queryDateInfo(cityId, 6,"cell_waitforselDate","cell_selectedDate");
});
	$("#channel_loadMoreDate").click(function(){
		var cityId=$("#citymenu2").find("option:selected").val();
		queryDateInfo(cityId, 6,"channel_waitforselDate","channel_selectedDate");
		});
	$("#ncell_loadMoreDate").click(function(){
		var cityId=$("#citymenu3").find("option:selected").val();
		queryDateInfo(cityId, 6,"ncell_waitforselDate","ncell_selectedDate");
	});
});

/**
 * 
 * @title 文本输入弹出窗口向列表注入
 * @param moreBtn 更多
 * @param divId 弹出窗口DIV
 * @param confirmBtn 确认按钮
 * @param textInputId 输入文本框
 * @param targetObj 目标填充对象
 * @author chao.xj
 * @date 2014-10-22下午4:54:32
 * @company 怡创科技
 * @version 1.2
 */
function bindTextToSelectEvent(moreBtn,divId,confirmBtn,textInputId,targetObj){
	$("."+moreBtn).click(function() {
		$("#"+divId).toggle();
		//divcenter();
		var input = $(this);            
        var offset = input.offset();
        //先后设置div的内容、位置，最后显示出来（渐进效果）
        $('#'+divId).css('left',offset.left + input.width() + 2 + 'px').css('top',offset.top + 'px');
	});
	$("#"+confirmBtn).click(function(){
		var str="";
		str=$("#"+textInputId).val().trim();
		if(str.length!=0){
			if($("#"+targetObj+" option:first").val()==0){
				//已经选择过一次
				$("#"+targetObj+" option:first").text(str);
				//重置选择项
				$("#"+targetObj+" option:first").attr("selected", 'selected');
				//$("#bsc option:first").prop("selected", 'selected');
				}else{
				//没有选择过
				$("#"+targetObj).prepend("<option value='0'>"+str+"</option>");
				//重置选择项
				$("#"+targetObj+" option:first").attr("selected", 'selected');
				//$("#bsc option:first").prop("selected", 'selected');
				}
				//$("#bsc").append("<option>"+bsc+"</option>");
		}else{
			$("#"+targetObj+" option[value='0']").remove();
			//重置选择项
			$("#"+targetObj+" option:first").attr("selected", 'selected');
		}
		var str=$("#"+targetObj).find("option:selected").text();
		if("cell_confirmCell"==confirmBtn){
			$("#cellForCell").val(str);
		}
		if("channel_confirmCell"==confirmBtn){
			$("#channelForCell").val(str);
		}
		if("ncell_confirmCell"==confirmBtn){
			$("#ncellForCell").val(str);
		}
		if("ncell_confirmNcell"==confirmBtn){
			$("#ncellForNcell").val(str);
		}
		
		//确定后隐藏
		$("#"+divId).toggle();
	});
}

/**
 * 
 * @title 通用绑定列表事件
 * @param moreBtn 更多
 * @param divId 弹出窗口DIV
 * @param waitforsel 待选列表
 * @param selected 被选列表
 * @param selAllBtn 选择全部按钮
 * @param partOfSelBtn 选择部分按钮
 * @param delAllBtn 删除全部按钮
 * @param partOfDelBtn 删除部分按钮
 * @param confirmBtn 确认按钮
 * @param cancelBtn 取消按钮
 * @param targetObj 目标填充对象
 * @author chao.xj
 * @date 2014-10-22下午2:41:47
 * @company 怡创科技
 * @version 1.2
 */
function bindSelectEvent(moreBtn,divId,waitforsel,selected,selAllBtn,partOfSelBtn,delAllBtn,partOfDelBtn,confirmBtn,cancelBtn,targetObj){
	$("."+moreBtn).click(function() {
		$("#"+divId).toggle();
		//divcenter();
		if("cell_moreDate"===moreBtn && $("#"+divId).css("display")!="none"){
			var cityId=$("#citymenu").find("option:selected").val();
			queryDateInfo(cityId, 3,waitforsel,selected);
		}
		if("channel_moreDate"===moreBtn && $("#"+divId).css("display")!="none"){
			var cityId=$("#citymenu2").find("option:selected").val();
			queryDateInfo(cityId, 3,waitforsel,selected);
		}
		if("ncell_moreDate"===moreBtn && $("#"+divId).css("display")!="none"){
			var cityId=$("#citymenu3").find("option:selected").val();
			queryDateInfo(cityId, 3,waitforsel,selected);
		}
		var input = $(this);            
        var offset = input.offset();
        //先后设置div的内容、位置，最后显示出来（渐进效果）
        //$('#'+divId).css('left',offset.left + input.width() + 2 + 'px').css('top',offset.top + 'px');
        //$('#'+divId).attr({style:"position:absolute; left:expression(document.body.clientWidth/2-oDiv.offsetWidth/2); top:expression(((document.body.clientHeight/2-oDiv.offsetHeight/2)<0)?0:(document.body.clientHeight/2-oDiv.offsetHeight/2))"});
        $('#'+divId).css('position','absolute').css('left',document.body.clientWidth/2-document.getElementById(divId).offsetWidth/2).css('top',((document.body.clientHeight/2-document.getElementById(divId).offsetHeight/2)<0)?0:(document.body.clientHeight/2-document.getElementById(divId).offsetHeight/2));
        if($("#"+waitforsel+" option").size()>30){
    		//获取option的大小自动设置高度
    		$("#"+waitforsel).attr("size","30");
    	}else{
    		//获取option的大小自动设置高度
    		$("#"+waitforsel).attr("size",$("#"+waitforsel+" option").size());
    	}
	});
	
	//获取选中部分的内容
	//选择部分
	$("#"+partOfSelBtn).click(function() {
		//先清除旧的再叠加新的
		//增加新的选择
		$("#"+waitforsel).find("option:selected").each(function(){
		//alert($(this).text());
		//增加已选择的
		$("#"+selected).append("<option value='"+$(this).val()+"'>"+$(this).text()+"</option>");
		//删除未选择的
		$(this).remove();
		});
		//获取option的大小自动设置高度
		/*$("#"+selected).attr("size",$("#"+selected+" option").size());
		$("#"+waitforsel).attr("size",$("#"+waitforsel+" option").size());*/
		if($("#"+waitforsel+" option").size()>30){
			//获取option的大小自动设置高度
			$("#"+waitforsel).attr("size","30");
		}else{
			
			//获取option的大小自动设置高度
			$("#"+waitforsel).attr("size",$("#"+waitforsel+" option").size());
		}
		if($("#"+selected+" option").size()>30){
			//获取option的大小自动设置高度
			$("#"+selected).attr("size","30");
		}else{
			
			//获取option的大小自动设置高度
			$("#"+selected).attr("size",$("#"+selected+" option").size());
		}
	});
	//选择全部
	$("#"+selAllBtn).click(function() {
		//先清除旧的再叠加新的
		$("#"+waitforsel+" option").each(function(){
		$("#"+selected).append("<option value='"+$(this).val()+"'>"+$(this).text()+"</option>");
		//删除未选择的
		$(this).remove();
		});
		/*//获取option的大小自动设置高度
		$("#"+waitforsel).attr("size",$("#"+waitforsel+" option").size());
		$("#"+selected).attr("size",$("#"+selected+" option").size());*/
		if($("#"+waitforsel+" option").size()>30){
			//获取option的大小自动设置高度
			$("#"+waitforsel).attr("size","30");
		}else{
			
			//获取option的大小自动设置高度
			$("#"+waitforsel).attr("size",$("#"+waitforsel+" option").size());
		}
		if($("#"+selected+" option").size()>30){
			//获取option的大小自动设置高度
			$("#"+selected).attr("size","30");
		}else{
			
			//获取option的大小自动设置高度
			$("#"+selected).attr("size",$("#"+selected+" option").size());
		}
	});
	//删除部分 
	$("#"+partOfDelBtn).click(function() {
		
		$("#"+selected).find("option:selected").each(function(){
		    //增加未选择的
			$("#"+waitforsel).append("<option value='"+$(this).val()+"'>"+$(this).text()+"</option>");
			$(this).remove();
		});
		//获取option的大小自动设置高度
		/*$("#"+waitforsel).attr("size",$("#"+waitforsel+" option").size());
		$("#"+selected).attr("size",$("#"+selected+" option").size());*/
		if($("#"+waitforsel+" option").size()>30){
			//获取option的大小自动设置高度
			$("#"+waitforsel).attr("size","30");
		}else{
			
			//获取option的大小自动设置高度
			$("#"+waitforsel).attr("size",$("#"+waitforsel+" option").size());
		}
		if($("#"+selected+" option").size()>30){
			//获取option的大小自动设置高度
			$("#"+selected).attr("size","30");
		}else{
			
			//获取option的大小自动设置高度
			$("#"+selected).attr("size",$("#"+selected+" option").size());
		}
	});
	//删除全部
	$("#"+delAllBtn).click(function() {
		
		//增加未选择的
		$("#"+selected+" option").each(function(){
		
		$("#"+waitforsel).append("<option value='"+$(this).val()+"'>"+$(this).text()+"</option>");
		//删除未选择的
		$(this).remove();
		});
		//获取option的大小自动设置高度
		/*$("#"+waitforsel).attr("size",$("#"+waitforsel+" option").size());
		$("#"+selected).attr("size",$("#"+selected+" option").size());*/
		if($("#"+waitforsel+" option").size()>30){
			//获取option的大小自动设置高度
			$("#"+waitforsel).attr("size","30");
		}else{
			
			//获取option的大小自动设置高度
			$("#"+waitforsel).attr("size",$("#"+waitforsel+" option").size());
		}
		if($("#"+selected+" option").size()>30){
			//获取option的大小自动设置高度
			$("#"+selected).attr("size","30");
		}else{
			
			//获取option的大小自动设置高度
			$("#"+selected).attr("size",$("#"+selected+" option").size());
		}
	});
	$("#"+confirmBtn).click(function(){
		var str="";
		$("#"+selected+" option").each(function(){
			str+=$(this).text()+",";
		});
		if(str.length!=0){
			str=str.substr(0,str.length-1);
			
			if($("#"+targetObj+" option:first").val()==0){
				//已经选择过一次
				$("#"+targetObj+" option:first").text(str);
				//重置选择项
				$("#"+targetObj+" option:first").attr("selected", 'selected');
				//$("#bsc option:first").prop("selected", 'selected');
				}else{
				//没有选择过
				$("#"+targetObj).prepend("<option value='0'>"+str+"</option>");
				//重置选择项
				$("#"+targetObj+" option:first").attr("selected", 'selected');
				//$("#bsc option:first").prop("selected", 'selected');
				}
				//$("#bsc").append("<option>"+bsc+"</option>");
		}else{
			$("#"+targetObj+" option[value='0']").remove();
			//重置选择项
			$("#"+targetObj+" option:first").attr("selected", 'selected');
		}
		//获取小区字段归属哪张表标识填充隐藏域
		if("cell_confirmParam"==confirmBtn){
			//BASICTAB,EXTRATAB
			var basicTabVal="";
			var basicTabStr="";
			var extraTabVal="";
			var extraTabStr="";
			var val="";
			
			$("#"+selected+" option").each(function(){
				val=$(this).val();
				if(val=="BASICTAB"){
					basicTabVal=val;
					basicTabStr+=$(this).text()+",";
				}
				if(val=="EXTRATAB"){
					extraTabVal=val;
					extraTabStr+=$(this).text()+",";
				}
			});
			////BASICTAB:ALLOCPREF;:
			if(basicTabVal!=""){
				
				basicTabStr=basicTabStr.substring(0, basicTabStr.length-1);
			}
			if(extraTabVal!=""){
				
				extraTabStr=extraTabStr.substring(0, extraTabStr.length-1);
			}
			if(basicTabVal!=""){
				
				if(extraTabVal!=""){
					
					$("#cellParam").val(basicTabVal+":"+basicTabStr+";"+extraTabVal+":"+extraTabStr);
				}
				if(extraTabVal==""){
					
					$("#cellParam").val(basicTabVal+":"+basicTabStr);
				}
			}else{
				if(extraTabVal!=""){
					
					$("#cellParam").val(extraTabVal+":"+extraTabStr);
				}
				if(extraTabVal==""){
					
					$("#cellParam").val("ALL");
				}
			}
			
			
		}
		//获取BSCID标识填充隐藏域
		if("cell_confirmBsc"==confirmBtn){
			var bscVal="";
			$("#"+selected+" option").each(function(){
				bscVal+=$(this).val()+",";
			});
			bscVal=bscVal.substring(0, bscVal.length-1);
//			console.log(bscVal);
			$("#cellBsc").val(bscVal);
		}
		//获取小区tab日期填充隐藏域
		if("cell_confirmDate"==confirmBtn){
			var dateVal="";
			$("#"+selected+" option").each(function(){
				dateVal+=$(this).val()+",";
			});
			dateVal=dateVal.substring(0, dateVal.length-1);
//			console.log(dateVal);
			$("#cellDate").val(dateVal);
		}
		//获取小区信道tab日期填充隐藏域
		if("channel_confirmDate"==confirmBtn){
			var dateVal="";
			$("#"+selected+" option").each(function(){
				dateVal+=$(this).val()+",";
			});
			dateVal=dateVal.substring(0, dateVal.length-1);
//			console.log(dateVal);
			$("#channelDate").val(dateVal);
		}
		//获取小区邻区tab日期填充隐藏域
		if("ncell_confirmDate"==confirmBtn){
			var dateVal="";
			$("#"+selected+" option").each(function(){
				dateVal+=$(this).val()+",";
			});
			dateVal=dateVal.substring(0, dateVal.length-1);
//			console.log(dateVal);
			$("#ncellDate").val(dateVal);
		}
		if("channel_confirmBsc"==confirmBtn){
			var bscVal="";
			$("#"+selected+" option").each(function(){
				bscVal+=$(this).val()+",";
			});
			bscVal=bscVal.substring(0, bscVal.length-1);
//			console.log(bscVal);
			$("#channelBsc").val(bscVal);
		}
		if("ncell_confirmBsc"==confirmBtn){
			var bscVal="";
			$("#"+selected+" option").each(function(){
				bscVal+=$(this).val()+",";
			});
			bscVal=bscVal.substring(0, bscVal.length-1);
			//console.log(bscVal);
			$("#ncellBsc").val(bscVal);
		}
		//获取信道字段填充目标对象val
		if("channel_confirmParam"==confirmBtn){
			var channelObj=$("#"+targetObj).find("option:selected");
			$("#channelParam").val(channelObj.text());
		}
		//获取邻区字段填充目标对象val
		if("ncell_confirmParam"==confirmBtn){
			var ncellObj=$("#"+targetObj).find("option:selected");
			$("#ncellParam").val(ncellObj.text());
		}
		//确定后隐藏
		$("#"+divId).toggle();
	});
	$("#"+cancelBtn).click(function(){
		//取消后隐藏
		$("#"+divId).toggle();
	});
}
//绑定事件
function bindEvent(){
	var area=$("#citymenu").find("option:selected").text().trim();
	
	$("#area1").val(area);
	$("#area2").val(area);
	
	
	//切换区域时，赋值给uploadCityId
	$("#provincemenu").change(function() {
		getSubAreas("provincemenu", "citymenu", "市");
	});
	$("#citymenu").change(function() {	
		$("#uploadCityId").val($("#citymenu").val());
		var area=$("#citymenu").find("option:selected").text().trim();
		$("#area1").val(area);
		$("#area2").val(area);
		var cityId=$("#citymenu").find("option:selected").val();
		//先清空
		clearCellSelect();
		//再填充
		get2GEriCellQueryDateAndBscListInfo(cityId,"cell");
	});
	
	$("#provincemenu2").change(function() {
		getSubAreas("provincemenu2", "citymenu2", "市");
	});
	$("#citymenu2").change(function() {	
		var cityId=$("#citymenu2").find("option:selected").val();
		//先清空
		clearChannelSelect();
		//再填充
		get2GEriCellQueryDateAndBscListInfo(cityId,"channel");
	});
	
	$("#provincemenu3").change(function() {
		getSubAreas("provincemenu3", "citymenu3", "市");
	});
	$("#citymenu3").change(function() {	
		var cityId=$("#citymenu3").find("option:selected").val();
		//先清空
		clearNcellSelect();
		//再填充
		get2GEriCellQueryDateAndBscListInfo(cityId,"ncell");
	});
	//列表改变事件
	$("#cell_targetBsc").change(function(){
		var bscid=$("#cell_targetBsc").find("option:selected").val();
		$("#cellBsc").val(bscid);
	});
	$("#cell_targetParam").change(function(){
		var obj=$("#cell_targetParam").find("option:selected");
		$("#cellParam").val(obj.val()+":"+obj.text());
	});
	$("#cell_targetDate").change(function(){
		var obj=$("#cell_targetDate").find("option:selected");
		$("#cellDate").val(obj.text());
	});
	$("#cell_targetCell").change(function(){
		var obj=$("#cell_targetCell").find("option:selected");
		$("#cellForCell").val(obj.text());
	});
	
	$("#channel_targetBsc").change(function(){
		var bscid=$("#channel_targetBsc").find("option:selected").val();
		$("#channelBsc").val(bscid);
	});
	$("#channel_targetParam").change(function(){
		var obj=$("#channel_targetParam").find("option:selected");
		$("#channelParam").val(obj.text());
	});
	$("#channel_targetDate").change(function(){
		var obj=$("#channel_targetDate").find("option:selected");
		$("#channelDate").val(obj.text());
	});
	$("#channel_targetCell").change(function(){
		var obj=$("#channel_targetCell").find("option:selected");
		$("#channelForCell").val(obj.text());
	});
	$("#ncell_targetBsc").change(function(){
		var bscid=$("#ncell_targetBsc").find("option:selected").val();
		$("#ncellBsc").val(bscid);
	});
	$("#ncell_targetParam").change(function(){
		var obj=$("#ncell_targetParam").find("option:selected");
		$("#ncellParam").val(obj.text());
	});
	$("#ncell_targetDate").change(function(){
		var obj=$("#ncell_targetDate").find("option:selected");
		$("#ncellDate").val(obj.text());
	});
	$("#ncell_targetCell").change(function(){
		var obj=$("#ncell_targetCell").find("option:selected");
		$("#ncellForCell").val(obj.text());
	});
	$("#ncell_targetNcell").change(function(){
		var obj=$("#ncell_targetNcell").find("option:selected");
		$("#ncellForNcell").val(obj.text());
	});
	
	//查询爱立信小区数据
	$("#searchCellBtn").click(function(){
		initFormPage("searchCellForm");//页面对象设置
		queryEri2GCellData();
	});
	//查询爱立信小区信道数据
	$("#searchCellChannelBtn").click(function(){
		initFormPage("searchCellChannelForm");//页面对象设置
		queryEri2GCellChannelData();
	});
	//查询爱立信小区邻区数据
	$("#searchNcellBtn").click(function(){
		initFormPage("searchNcellForm");//页面对象设置
		queryEri2GNcellData();
	});
	//导出爱立信小区数据
	$("#exportCellBtn").click(function(){
		exportEri2GCellData();
	});
	//导出爱立信小区信道数据
	$("#exportCellChannelBtn").click(function(){
		exportEri2GCellChannelData();
	});
	//导出爱立信邻区数据
	$("#exportNcellBtn").click(function(){
		exportEri2GNcellData();
	});
}


/**
 * 在某个基准日期的基础上，对时间进行加减
 * 正数为加，负数为减去
 * @param baseDate
 * @param day
 * @returns {Date}
 */
function addDays(baseDate,day){
    var tinoneday=24*60*60*1000;
    var nt=baseDate.getTime();
    var cht=day*tinoneday;
    var newD=new Date();
    newD.setTime(nt+cht);
    return newD;
}


//jquery ui 效果
function jqueryUiSet() {
	$("#progressbar").progressbar({
		value : 0
	});
	$("#tabs").tabs();
	
	$( "#accordion" ).accordion();
	
	$("#searchImportDiv").css("height","46px");
	$("#importDiv").css("height","180px");
	
	
	//--ncs上传记录---//
	//$("#provincemenu").selectmenu();
	//$("#citymenu").selectmenu();
	$("#importstatusmenu").selectmenu();
	$("#datepicker").datepicker({
		"dateFormat" : "yy-mm-dd"
	});
	$("#cellQueryDate").datepicker(
			{
				dateFormat : "yy-mm-dd",
				defaultDate : "+1w",
				changeMonth : true,
				numberOfMonths : 1,
				onClose : function(selectedDate) {
					$("#cellQueryDate").datepicker("option", "maxDate",
							selectedDate);
				}
			});
	$("#cellQueryDate").datepicker("setDate",(new Date()));
	
	//---记录查询----//
	/*$("#citymenu2").selectmenu();
	$("#provincemenu2").selectmenu();
	$("#citymenu3").selectmenu();
	$("#provincemenu3").selectmenu();*/
	$("#cellMeaBegDate").datetimepicker(
			{
				dateFormat : "yy-mm-dd",
				timeFormat: "HH:mm:ss",
				defaultDate : "-2",
				changeMonth : true,
				numberOfMonths : 1,
				onClose : function(selectedDate) {
					$("#cellMeaEndDate").datetimepicker("option", "minDate",
							selectedDate);
				}
			});
	$("#cellMeaBegDate").datetimepicker("setDate",addDays(new Date(),-2));// 减去2天
	$("#cellMeaEndDate").datetimepicker(
			{
				dateFormat : "yy-mm-dd",
				timeFormat: "HH:mm:ss",
				defaultDate :"+1w",
				changeMonth : true,
				numberOfMonths : 1,
				onClose : function(selectedDate) {
					$("#cellMeaBegDate").datetimepicker("option", "maxDate",
							selectedDate);
				}
			});
	$("#cellMeaEndDate").datetimepicker("setDate",(new Date()));
	//$("#searchImportBtn").button();
}



/**
 * 查询导入记录
 */
function queryImportDataRec(){
//	var fileCode=$("input[name='fileCode']:checked").val();
	var fileCode=$("#dateType").find("option:selected").val();
	$("#searchImportForm").ajaxSubmit({
		url:'queryUploadRecAjaxAction',
		data:{'fileCode' : fileCode},
		type:'post',
		dataType:'text',
		success:function(raw){
			var data={};
			try{
			data=eval("("+raw+")");
			}catch(err){
				console.log(err);
			}
			displayImportRec(data['data']);
			setFormPageInfo("searchImportForm",data['page']);
			setPageView(data['page'],"ncsImportRecPageDiv");
		}
	});
}


/**
 * 显示上传记录
 * @param data
 */
function displayImportRec(data){
	if(data==null||data==undefined){
		return;
	}
	//
	$("#importListTab tr:not(:first)").each(function(i, ele) {
		$(ele).remove();
	});
	var html="";
	var city=$("#citymenu").find("option:selected").text();
	for ( var i = 0; i < data.length; i++) {
		one = data[i];
		html += "<tr>";
		html+="<td>"+getValidValue(city,'')+"</td>";
		html+="<td>"+getValidValue(one['uploadTime'],'')+"</td>";
		html+="<td>"+getValidValue(one['oriFileName'],'')+"</td>";
		html+="<td>"+getPropValueExpress(one['fileSize'])+"</td>";
		html+="<td>"+getValidValue(one['launchTime'],'')+"</td>";
		html+="<td>"+getValidValue(one['completeTime'],'')+"</td>";
		html+="<td>"+getValidValue(one['account'],'')+"</td>";
		if(one['fileStatus'].indexOf("失败") == -1) {
			html+="<td><a style='text-decoration:underline;' onclick='viewDataUploadReport("+one['jobId']+")'>"+one['fileStatus']+"</a></td>";
		} else {
			html+="<td><a style='text-decoration:underline;color:red;' onclick='viewDataUploadReport("+one['jobId']+")' >"+one['fileStatus']+"</a></td>";
		}
		html+="</tr>";
		
		
	}
	$("#importListTab").append(html);
}





//设置formid下的page信息
//其中，当前页会加一
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

//初始化form下的page信息
function initFormPage(formId) {
	var form = $("#" + formId);
	if (!form) {
		return;
	}
	// form.find("#hiddenPageSize").val(25);
	form.find("#hiddenCurrentPage").val(1);
	form.find("#hiddenTotalPageCnt").val(-1);
	form.find("#hiddenTotalCnt").val(-1);
}




//function toggleSearchImport() {
//	if ($("#importDiv").css("display") != "none") {
//		$("#importDiv").css("display", "none");
//	}
//	$("#searchImportDiv").toggle({
//		"duration" : 500
//	});
//}

function toggleImport() {
//	if ($("#searchImportDiv").css("display") != "none") {
//		$("#searchImportDiv").css("display", "none");
//	}
	$("#importDiv").toggle({
		"duration" : 500
	});
}


/**
 * 
 * @title 查询爱立信2g小区数据
 * @author chao.xj
 * @date 2014-10-24下午1:52:15
 * @company 怡创科技
 * @version 1.2
 */
function queryEri2GCellData(){
	$(".loading_fb").text("正在加载爱立信2G小区数据");
	$(".loading_cover").css("display", "block");
	var param=$("#cell_targetParam").find("option:selected").text();
	if(param=="ALL"){
		$("#cellParam").val("ALL");
	}
	param=$("#cell_targetCell").find("option:selected").text();
	if(param=="ALL"){
		$("#cellForCell").val("ALL");
	}
	//如果日期选择all则输入的是近一个月的全部日期查询
	var cellDate=$("#cell_targetDate").find("option:selected").text();
	if("ALL"===cellDate){
		var str="";
		$("#cell_targetDate option").each(function(){
			if($(this).text()=="ALL" || $(this).val()==0){
				return true;
			}
			str+=$(this).text()+",";
		});
		$("#cellDate").val(str.substring(0, str.length-1));
	}
	$("#searchCellForm").ajaxSubmit({
		url:'queryEri2GCellByPageAjaxAction',
		type:'post',
		dataType:'text',
		success:function(raw){
			var data={};
//			console.log("小区数据："+raw);
			try{
			data=eval("("+raw+")");
			}catch(err){
				console.log(err);
			}
			displayCellData(data['data']);
			setFormPageInfo("searchCellForm",data['page']);
			setPageView(data['page'],"cellListPageDiv");
		},
		complete : function() {
			$(".loading_cover").css("display", "none");
		}
	});
}
/**
 * 
 * @title 查询爱立信2g小区信道数据
 * @author chao.xj
 * @date 2014-10-24下午1:52:15
 * @company 怡创科技
 * @version 1.2
 */
function queryEri2GCellChannelData(){
	$(".loading_fb").text("正在加载爱立信2G小区信道数据");
	$(".loading_cover").css("display", "block");
	var param=$("#channel_targetCell").find("option:selected").text();
	if(param=="ALL"){
		$("#channelForCell").val("ALL");
	}
	//如果日期选择all则输入的是近一个月的全部日期查询
	var channelDate=$("#channel_targetDate").find("option:selected").text();
	if("ALL"===channelDate){
		var str="";
		$("#channel_targetDate option").each(function(){
			if($(this).text()=="ALL" || $(this).val()==0){
				return true;
			}
			str+=$(this).text()+",";
		});
		$("#channelDate").val(str.substring(0, str.length-1));
	}
	$("#searchCellChannelForm").ajaxSubmit({
		url:'queryEri2GCellChannelByPageAjaxAction',
		type:'post',
		dataType:'text',
		success:function(raw){
			var data={};
//			console.log("信道数据："+raw);
			try{
			data=eval("("+raw+")");
			}catch(err){
				console.log(err);
			}
			displayCellChannelData(data['data']);
			setFormPageInfo("searchCellChannelForm",data['page']);
			setPageView(data['page'],"channelListPageDiv");
		},
		complete : function() {
			$(".loading_cover").css("display", "none");
		}
	});
}
/**
 * 
 * @title 查询爱立信2g小区邻区数据
 * @author chao.xj
 * @date 2014-10-24下午1:52:15
 * @company 怡创科技
 * @version 1.2
 */
function queryEri2GNcellData(){
	$(".loading_fb").text("正在加载爱立信2G小区邻区数据");
	$(".loading_cover").css("display", "block");
	var param=$("#ncell_targetCell").find("option:selected").text();
	if(param=="ALL"){
		$("#ncellForCell").val("ALL");
	}
	param=$("#ncell_targetNcell").find("option:selected").text();
	if(param=="ALL"){
		$("#ncellForNcell").val("ALL");
	}
	//如果日期选择all则输入的是近一个月的全部日期查询
	var ncellDate=$("#ncell_targetDate").find("option:selected").text();
	if("ALL"===ncellDate){
		var str="";
		$("#ncell_targetDate option").each(function(){
			if($(this).text()=="ALL" || $(this).val()==0){
				return true;
			}
			str+=$(this).text()+",";
		});
		$("#ncellDate").val(str.substring(0, str.length-1));
	}
	$("#searchNcellForm").ajaxSubmit({
		url:'queryEri2GNcellByPageAjaxAction',
		type:'post',
		dataType:'text',
		success:function(raw){
			var data={};
//			console.log("邻区数据："+raw);
			try{
			data=eval("("+raw+")");
			}catch(err){
				console.log(err);
			}
			displayNcellData(data['data']);
			setFormPageInfo("searchNcellForm",data['page']);
			setPageView(data['page'],"ncellListPageDiv");
		},
		complete : function() {
			$(".loading_cover").css("display", "none");
		}
	});
}
/**
 * 
 * @title 显示返回的cell信息
 * @param data
 * @author chao.xj
 * @date 2014-10-24下午3:01:41
 * @company 怡创科技
 * @version 1.2
 */
function displayCellData(data){
	if(data==null||data==undefined){
		return;
	}
	$("#cellListTab tr").each(function(i, ele) {
		$(ele).remove();
	});
	//MEA_DATE,MSC,BSC,CELL
	var oriParamArr=["MEA_DATE","MSC","BSC","CELL"];
	var params=$("#cell_targetParam").find("option:selected").text();
	var allParams=new Array();
	var newParamArr;
	if(params=="" || params=="ALL"){
		var cellOption="";
		$("#cell_targetParam option").each(function(){
			cellOption=$(this).text();
			if(cellOption!="ALL" && $(this).val()!=0){
				allParams.push(cellOption);
			}
		});
		newParamArr=oriParamArr.concat(allParams);
	}else{
		params=$("#cell_targetParam").find("option:selected").text();
		//console.log("params:"+params);
		var paramArr=params.split(",");
		newParamArr=oriParamArr.concat(paramArr);
	}
	var html="";
	var city=$("#citymenu").find("option:selected").text();
	html += "<tr style='font-weight:bold'>";
	for(var m = 0; m < newParamArr.length; m++){
		//console.log(newParamArr[m]);
		//ACC_16
		//str.lastIndexOf("_") >0
		//!isNaN(str.substr(str.lastIndexOf("-")-1)) 是否数字
		//str.substring(0,str.lastIndexOf("_"))  之前的参数
		if(newParamArr[m].lastIndexOf("_")>0 && !isNaN(newParamArr[m].substr(newParamArr[m].lastIndexOf("_")+1))){
			html+="<td>"+getValidValue(newParamArr[m].substring(0,newParamArr[m].lastIndexOf("_")),'')+"</td>";
		}else{
			html+="<td>"+getValidValue((newParamArr[m]=='MEA_DATE'?'日期':newParamArr[m]),'')+"</td>";
		}
	}
	html+="</tr>";
	for ( var i = 0; i < data.length; i++) {
		one = data[i];
		//console.log(one);
		html += "<tr>";
		for(var j = 0; j < newParamArr.length; j++){
			html+="<td>"+getValidValue(one[$.trim(newParamArr[j])],'')+"</td>";
		}
		html+="</tr>";
	}
	$("#cellListTab").append(html);
}
/**
 * 
 * @title 显示返回的cell信道信息
 * @param data
 * @author chao.xj
 * @date 2014-10-24下午3:01:41
 * @company 怡创科技
 * @version 1.2
 */
function displayCellChannelData(data){
	if(data==null||data==undefined){
		return;
	}
	//
	$("#channelListTab tr").each(function(i, ele) {
		$(ele).remove();
	});
	//MEA_DATE,MSC,BSC,CELL
	var oriParamArr=["MEA_DATE","MSC","BSC","CELL","CH_GROUP","CHGR_STATE","CHGR_TG"];
	/*var params=$("#channel_targetParam").find("option:selected").text();
	//console.log("params:"+params);
	var paramArr=params.split(",");
	var newParamArr=oriParamArr.concat(paramArr);*/
	
	var params=$("#channelParam").val();
	var allParams=new Array();
	var newParamArr;
	if(params=="" || params=="ALL"){
		var cellOption="";
		$("#channel_targetParam option").each(function(){
			cellOption=$(this).text();
			if(cellOption!="ALL" && $(this).val()!=0){
				allParams.push(cellOption);
			}
		});
		newParamArr=oriParamArr.concat(allParams);
	}else{
		
		//console.log("params:"+params);
		var paramArr=params.split(",");
		newParamArr=oriParamArr.concat(paramArr);
	}
	var html="";
	var city=$("#citymenu2").find("option:selected").text();
	html += "<tr style='font-weight:bold'>";
	for(var m = 0; m < newParamArr.length; m++){
//		console.log(newParamArr[m]);
		//html+="<td>"+getValidValue(newParamArr[m],'')+"</td>";
		if(newParamArr[m].lastIndexOf("_")>0 && !isNaN(newParamArr[m].substr(newParamArr[m].lastIndexOf("_")+1))){
			html+="<td>"+getValidValue(newParamArr[m].substring(0,newParamArr[m].lastIndexOf("_")),'')+"</td>";
		}else{
			html+="<td>"+getValidValue(newParamArr[m]=='MEA_DATE'?'日期':newParamArr[m],'')+"</td>";
		}
	}
	html+="</tr>";
	for ( var i = 0; i < data.length; i++) {
		one = data[i];
		//console.log(one);
		html += "<tr>";
		for(var j = 0; j < newParamArr.length; j++){
			html+="<td>"+getValidValue(one[$.trim(newParamArr[j])],'')+"</td>";
		}
		html+="</tr>";
	}
	$("#channelListTab").append(html);
}
/**
 * 
 * @title 显示返回的ncell信息
 * @param data
 * @author chao.xj
 * @date 2014-10-24下午3:01:41
 * @company 怡创科技
 * @version 1.2
 */
function displayNcellData(data){
	if(data==null||data==undefined){
		return;
	}
	//
	$("#ncellListTab tr").each(function(i, ele) {
		$(ele).remove();
	});
	//MEA_DATE,MSC,BSC,CELL
	var oriParamArr=["MEA_DATE","MSC","BSC","CELL","N_BSC","N_CELL"];
	/*var params=$("#ncell_targetParam").find("option:selected").text();
	//console.log("params:"+params);
	var paramArr=params.split(",");
	var newParamArr=oriParamArr.concat(paramArr);*/
	
	var params=$("#ncellParam").val();
	var allParams=new Array();
	var newParamArr;
	if(params=="" || params=="ALL"){
		var cellOption="";
		$("#ncell_targetParam option").each(function(){
			cellOption=$(this).text();
			if(cellOption!="ALL" && $(this).val()!=0){
				allParams.push(cellOption);
			}
		});
		newParamArr=oriParamArr.concat(allParams);
	}else{
		
		//console.log("params:"+params);
		var paramArr=params.split(",");
		newParamArr=oriParamArr.concat(paramArr);
	}
	var html="";
	var city=$("#citymenu3").find("option:selected").text();
	html += "<tr style='font-weight:bold'>";
	for(var m = 0; m < newParamArr.length; m++){
//		console.log(newParamArr[m]);
		//html+="<td>"+getValidValue(newParamArr[m],'')+"</td>";
		if(newParamArr[m].lastIndexOf("_")>0 && !isNaN(newParamArr[m].substr(newParamArr[m].lastIndexOf("_")+1))){
			html+="<td>"+getValidValue(newParamArr[m].substring(0,newParamArr[m].lastIndexOf("_")),'')+"</td>";
		}else{
			
			html+="<td>"+getValidValue((newParamArr[m]=='MEA_DATE'?'日期':newParamArr[m]),'')+"</td>";
		}
	}
	html+="</tr>";
	for ( var i = 0; i < data.length; i++) {
		one = data[i];
		//console.log(one);
		html += "<tr>";
		for(var j = 0; j < newParamArr.length; j++){
			html+="<td>"+getValidValue(one[$.trim(newParamArr[j])],'')+"</td>";
		}
		html+="</tr>";
	}
	$("#ncellListTab").append(html);
}
/**
* 分页跳转的响应
* @param dir
* @param action
* @param formId
* @param divId
*/
function showListViewByPage(dir,action,formId,divId) {
	
	var form=$("#"+formId);
	var div=$("#"+divId);
	//alert(form.find("#hiddenPageSize").val());
	var pageSize =new Number(form.find("#hiddenPageSize").val());
	var currentPage = new Number(form.find("#hiddenCurrentPage").val());
	var totalPageCnt =new Number(form.find("#hiddenTotalPageCnt").val());
	var totalCnt = new Number(form.find("#hiddenTotalCnt").val());

	//console.log("pagesize="+pageSize+",currentPage="+currentPage+",totalPageCnt="+totalPageCnt+",totalCnt="+totalCnt);
	if (dir === "first") {
		if (currentPage <= 1) {
			return;
		} else {
			$(form).find("#hiddenCurrentPage").val("1");
		}
	} else if (dir === "last") {
		if (currentPage >= totalPageCnt) {
			return;
		} else {
			$(form).find("#hiddenCurrentPage").val(totalPageCnt);
		}
	} else if (dir === "back") {
		if (currentPage <= 1) {
			return;
		} else {
			$(form).find("#hiddenCurrentPage").val(currentPage - 1);
		}
	} else if (dir === "next") {
		if (currentPage >= totalPageCnt) {
			return;
		} else {
			$(form).find("#hiddenCurrentPage").val(currentPage + 1);
		}
	} else if (dir === "num") {
		var userinput = $(div).find("#showCurrentPage").val();
		if (isNaN(userinput)) {
			alert("请输入数字！")
			return;
		}
		if (userinput > totalPageCnt || userinput < 1) {
			alert("输入页面范围不在范围内！");
			return;
		}
		$(form).find("#hiddenCurrentPage").val(userinput);
	}else{
		return;
	}
	//获取资源
	if(typeof action =="function"){
		action();
	}
}
/**
 * 
 * @title 获取2G爱立信小区查询所有列表信息填充
 * @author chao.xj
 * @date 2014-10-23下午4:57:58
 * @company 怡创科技
 * @version 1.2
 */
function get2GEriCellQueryAllListInfo(){
	var cityId=$("#citymenu").find("option:selected").val();
	$.ajax({
		url : 'get2GEriCellQueryPageAllListInfoAction',
		type : 'post',
		data:{'cityId':cityId},
		dataType : 'text',
		success : function(data) {
			var listInfo = {};
			try {
				listInfo = eval("(" + data + ")");
				var bscId;
				var bscName;
				var dateStr;
				for ( var key in listInfo) {
					if("bscInfo"==key){
						for ( var bscIndex = 0; bscIndex < listInfo[key].length; bscIndex++) {
							bscId=listInfo[key][bscIndex]['BSC_ID'];
							bscName=listInfo[key][bscIndex]['ENGNAME'];
//							console.log("bscId:"+bscId+"---bscName:"+bscName);
							$("#cell_targetBsc").append("<option value='"+bscId+"'>"+bscName+"</option>");
							$("#channel_targetBsc").append("<option value='"+bscId+"'>"+bscName+"</option>");
							$("#ncell_targetBsc").append("<option value='"+bscId+"'>"+bscName+"</option>");
							$("#cell_waitforselBsc").append("<option value='"+bscId+"'>"+bscName+"</option>");
							$("#channel_waitforselBsc").append("<option value='"+bscId+"'>"+bscName+"</option>");
							$("#ncell_waitforselBsc").append("<option value='"+bscId+"'>"+bscName+"</option>");
						}
					}
					if("dateInfo"==key){
						for ( var dateIndex = 0; dateIndex < listInfo[key].length; dateIndex++) {
							dateStr=listInfo[key][dateIndex]['MEA_DATE'];
//							console.log("dateStr:"+dateStr);
							$("#cell_targetDate").append("<option>"+dateStr+"</option>");
							$("#channel_targetDate").append("<option>"+dateStr+"</option>");
							$("#ncell_targetDate").append("<option>"+dateStr+"</option>");
							$("#cell_waitforselDate").append("<option>"+dateStr+"</option>");
							$("#channel_waitforselDate").append("<option>"+dateStr+"</option>");
							$("#ncell_waitforselDate").append("<option>"+dateStr+"</option>");
						}
					}
					if("paramInfo"==key){
//						console.log(listInfo[key]);
						for (var cellPara in listInfo[key]['CELL']) {
//							console.log(listInfo[key]['CELL'][cellPara]['tabName']);
							if("MSC"==cellPara || "BSC"==cellPara || "CELL"==cellPara || "TO"==cellPara){
								continue;
							}
								
						$("#cell_targetParam").append("<option value='"+(listInfo[key]['CELL'][cellPara]['tabName']=='RNO_2G_ERI_CELL'?'BASICTAB':'EXTRATAB')+"'>"+cellPara+"</option>");
						$("#cell_waitforselParam").append("<option value='"+(listInfo[key]['CELL'][cellPara]['tabName']=='RNO_2G_ERI_CELL'?'BASICTAB':'EXTRATAB')+"'>"+cellPara+"</option>");
							
						}
						for (var channelPara in listInfo[key]['CHANNEL']) {
							if("MSC"==channelPara || "BSC"==channelPara || "CELL"==channelPara || "CH_GROUP"==channelPara || "CHGR_STATE"==channelPara || "CHGR_TG"==channelPara){
								continue;
							}
							$("#channel_targetParam").append("<option>"+channelPara+"</option>");
							$("#channel_waitforselParam").append("<option>"+channelPara+"</option>");
						}
						for (var ncellPara in listInfo[key]['NCELL']) {
							if("MSC"==ncellPara || "BSC"==ncellPara || "CELL"==ncellPara  || "N_CELL"==ncellPara || "N_BSC"==ncellPara){
								continue;
							}
							$("#ncell_targetParam").append("<option>"+ncellPara+"</option>");
							$("#ncell_waitforselParam").append("<option>"+ncellPara+"</option>");
						}
					}
				}
			} catch (err) {

			}
		}
	});
}
/**
 * 
 * @title 获取2G爱立信小区查询日期及BSC列表信息填充
 * @param cityId
 * @param forWhat{cell,channel,ncell}
 * @author chao.xj
 * @date 2014-10-23下午5:55:57
 * @company 怡创科技
 * @version 1.2
 */
function get2GEriCellQueryDateAndBscListInfo(cityId,forWhat){
	$.ajax({
		url : 'get2GEriCellQueryPageDateAndBscListInfoAction',
		type : 'post',
		data:{'cityId':cityId},
		dataType : 'text',
		success : function(data) {
			var listInfo = {};
			try {
				listInfo = eval("(" + data + ")");
				var bscId;
				var bscName;
				var dateStr;
				for ( var key in listInfo) {
					if("bscInfo"==key){
						for ( var bscIndex = 0; bscIndex < listInfo[key].length; bscIndex++) {
							bscId=listInfo[key][bscIndex]['BSC_ID'];
							bscName=listInfo[key][bscIndex]['ENGNAME'];
//							console.log("bscId:"+bscId+"---bscName:"+bscName);
							if(forWhat=="cell"){
								
								$("#cell_targetBsc").append("<option value='"+bscId+"'>"+bscName+"</option>");
								$("#cell_waitforselBsc").append("<option value='"+bscId+"'>"+bscName+"</option>");
							}
							if(forWhat=="channel"){
								
								$("#channel_targetBsc").append("<option value='"+bscId+"'>"+bscName+"</option>");
								$("#channel_waitforselBsc").append("<option value='"+bscId+"'>"+bscName+"</option>");
							}
							if(forWhat=="ncell"){
								
								$("#ncell_targetBsc").append("<option value='"+bscId+"'>"+bscName+"</option>");
								$("#ncell_waitforselBsc").append("<option value='"+bscId+"'>"+bscName+"</option>");
							}
						}
					}
					if("dateInfo"==key){
						for ( var dateIndex = 0; dateIndex < listInfo[key].length; dateIndex++) {
							dateStr=listInfo[key][dateIndex]['MEA_DATE'];
//							console.log("dateStr:"+dateStr);
							if(forWhat=="cell"){
								
								$("#cell_targetDate").append("<option>"+dateStr+"</option>");
								$("#cell_waitforselDate").append("<option>"+dateStr+"</option>");
							}
							if(forWhat=="channel"){
								
								$("#channel_targetDate").append("<option>"+dateStr+"</option>");
								$("#channel_waitforselDate").append("<option>"+dateStr+"</option>");
							}
							if(forWhat=="ncell"){
								
								$("#ncell_targetDate").append("<option>"+dateStr+"</option>");
								$("#ncell_waitforselDate").append("<option>"+dateStr+"</option>");
							}
						}
					}
				}
			} catch (err) {

			}
		}
	});
}
/**
 * 
 * @title 清除除all外的所有cell列表项
 * @author chao.xj
 * @date 2014-10-23下午5:19:27
 * @company 怡创科技
 * @version 1.2
 */
function clearCellSelect(){
	//cell
	$("#cell_targetDate option").each(function(){
		if("ALL"!=$(this).text()){
			$(this).remove();
		}
	});
	$("#cell_waitforselDate option").each(function(){
		if("ALL"!=$(this).text()){
			$(this).remove();
		}
	});
	$("#cell_targetBsc option").each(function(){
		if("ALL"!=$(this).text()){
			$(this).remove();
		}
	});
	$("#cell_waitforselBsc option").each(function(){
		if("ALL"!=$(this).text()){
			$(this).remove();
		}
	});
	$("#cell_selectedDate").html("");
	$("#cell_selectedBsc").html("");
	$("#cell_cellInput").val("");
	$("#cell_selectedParam").html("");
	$("#cellBsc").val("");
	$("#cellForCell").val("");
	$("#cellDate").val("");
	$("#cellParam").val("");
	$("#cell_targetCell").html("<option>ALL</option>");
}
/**
 * 
 * @title 清除除all外的所有Channel列表项
 * @author chao.xj
 * @date 2014-10-23下午5:54:03
 * @company 怡创科技
 * @version 1.2
 */
function clearChannelSelect(){

	//channel
	$("#channel_targetDate option").each(function(){
		if("ALL"!=$(this).text()){
			$(this).remove();
		}
	});
	$("#channel_waitforselDate option").each(function(){
		if("ALL"!=$(this).text()){
			$(this).remove();
		}
	});
	$("#channel_targetBsc option").each(function(){
		if("ALL"!=$(this).text()){
			$(this).remove();
		}
	});
	$("#channel_waitforselBsc option").each(function(){
		if("ALL"!=$(this).text()){
			$(this).remove();
		}
	});
	$("#channel_selectedDate").html("");
	$("#channel_selectedBsc").html("");
	$("#channel_cellInput").val("");
	$("#channel_selectedParam").html("");
	$("#channelBsc").val("");
	$("#channelForCell").val("");
	$("#channelDate").val("");
	$("#channelParam").val("");
	$("#channel_targetCell").html("<option>ALL</option>");
}
/**
 * 
 * @title 清除除all外的所有Ncell列表项
 * @author chao.xj
 * @date 2014-10-23下午5:54:19
 * @company 怡创科技
 * @version 1.2
 */
function clearNcellSelect(){

	//ncell
	$("#ncell_targetDate option").each(function(){
		if("ALL"!=$(this).text()){
			$(this).remove();
		}
	});
	$("#ncell_waitforselDate option").each(function(){
		if("ALL"!=$(this).text()){
			$(this).remove();
		}
	});
	$("#ncell_targetBsc option").each(function(){
		if("ALL"!=$(this).text()){
			$(this).remove();
		}
	});
	$("#ncell_waitforselBsc option").each(function(){
		if("ALL"!=$(this).text()){
			$(this).remove();
		}
	});
	$("#ncell_selectedDate").html("");
	$("#ncell_selectedBsc").html("");
	$("#ncell_cellInput").val("");
	
	$("#ncell_selectedParam").html("");
	$("#ncell_ncellInput").html("");
	$("#ncellBsc").val("");
	$("#ncellForCell").val("");
	$("#ncellDate").val("");
	$("#ncellParam").val("");
	$("#ncellForNcell").val("");
	$("#ncell_targetCell").html("<option>ALL</option>");
	$("#ncell_targetNcell").html("<option>ALL</option>");
	
}
/**
 * 
 * @title 导出爱立信2g小区数据
 * @author chao.xj
 * @date 2014-10-24下午1:52:15
 * @company 怡创科技
 * @version 1.2
 */
function exportEri2GCellData(){
	/*$(".loading_fb").text("爱立信2G小区数据");
	$(".loading_cover").css("display", "block");*/
	$("#progressDesc").html("");
	showOperTips("loadingDataDiv", "tipcontentId", "正在执行导出");
	var param=$("#cell_targetParam").find("option:selected").text();
	if(param=="ALL"){
		$("#cellParam").val("ALL");
	}
	param=$("#cell_targetCell").find("option:selected").text();
	if(param=="ALL"){
		$("#cellForCell").val("ALL");
	}
	$("#searchCellForm").ajaxSubmit({
		url:'exportEri2GCellDataAjaxForAction',
		type:'post',
		dataType:'text',
		success:function(raw){
			var data={};
//			console.log("小区数据："+raw);
			try{
			data=eval("("+raw+")");
			var msg = data["msg"];
			var token = data["token"];
			if(token!="null") {
				//轮询进度
				queryExportProgress(token);
			} else {
				alert(msg);	
				hideOperTips("loadingDataDiv");
			}
			}catch(err){
				console.log(err);
			}
			
		},
		complete : function() {
//			$(".loading_cover").css("display", "none");
			
		}
	});
}
/**
 * 
 * @title 导出爱立信2g小区信道数据
 * @author chao.xj
 * @date 2014-10-24下午1:52:15
 * @company 怡创科技
 * @version 1.2
 */
function exportEri2GCellChannelData(){
	$("#progressDesc").html("");
	showOperTips("loadingDataDiv", "tipcontentId", "正在执行导出");
	var param=$("#channel_targetCell").find("option:selected").text();
	if(param=="ALL"){
		$("#channelForCell").val("ALL");
	}
	$("#searchCellChannelForm").ajaxSubmit({
		url:'exportEri2GCellChannelDataAjaxForAction',
		type:'post',
		dataType:'text',
		success:function(raw){
			var data={};
//			console.log("信道数据："+raw);
			try{
			data=eval("("+raw+")");
			var msg = data["msg"];
			var token = data["token"];
			if(token!="null") {
				//轮询进度
				queryExportProgress(token);
			} else {
				alert(msg);
				hideOperTips("loadingDataDiv");
			}
			}catch(err){
				console.log(err);
			}
		},
		complete : function() {
		}
	});
}
/**
 * 
 * @title 导出爱立信2g邻区数据
 * @author chao.xj
 * @date 2014-10-24下午1:52:15
 * @company 怡创科技
 * @version 1.2
 */
function exportEri2GNcellData(){
	$("#progressDesc").html("");
	showOperTips("loadingDataDiv", "tipcontentId", "正在执行导出");
	var param=$("#ncell_targetCell").find("option:selected").text();
	if(param=="ALL"){
		$("#ncellForCell").val("ALL");
	}
	param=$("#ncell_targetNcell").find("option:selected").text();
	if(param=="ALL"){
		$("#ncellForNcell").val("ALL");
	}
	$("#searchNcellForm").ajaxSubmit({
		url:'exportEri2GCellNcellDataAjaxForAction',
		type:'post',
		dataType:'text',
		success:function(raw){
			var data={};
//			console.log("邻区数据："+raw);
			try{
			data=eval("("+raw+")");
			var msg = data["msg"];
			var token = data["token"];
			if(token!="null") {
				//轮询进度
				queryExportProgress(token);
			} else {
				alert(msg);	
				hideOperTips("loadingDataDiv");
			}
			}catch(err){
				console.log(err);
			}
		},
		complete : function() {
		}
	});
}
/**
 * 
 * @title 查询导出进度
 * @param token
 * @author chao.xj
 * @date 2014-11-10下午3:57:53
 * @company 怡创科技
 * @version 1.2
 */
function queryExportProgress(token) {
	$.ajax({
		url : 'queryExportProgressAjaxForAction',
		data : {
			'token' : token
		},
		type : 'post',
		dataType : 'text',
		success : function(raw) {
			var obj = eval("(" + raw + ")");
			if(obj['fail']) {
				//导出失败
				alert(obj['msg']);
				hideOperTips("loadingDataDiv");
				return;
			}
			if (obj['finished']) {
				//完成
				$("#progressDesc").html("生成文件完成");
				console.log("finished");
				downloadDataFile(token);
				hideOperTips("loadingDataDiv");
			} else {
				//未完成
				$("#progressDesc").html(obj['msg']);
				window.setTimeout(function() {
					queryExportProgress(token);
				}, interval);
			}
		},
		complete:function(){
		}
	});
}
/**
 * 
 * @title 下载爱立信小区结果文件
 * @param token
 * @author chao.xj
 * @date 2014-11-10下午4:17:21
 * @company 怡创科技
 * @version 1.2
 */
function downloadDataFile(token) {
	var form = $("#downloadEriCellDataFileForm");
	form.find("input#token").val(token);
 	form.submit();
//	document.getElementById("downloadEriCellDataFileForm").submit()
}
/**
 * 
 * @title 查询日期信息
 * @author chao.xj
 * @date 2014-10-24下午1:52:15
 * @company 怡创科技
 * @version 1.2
 */
function queryDateInfo(cityId,monthNum,waitforsel,selected){
//	var cityId=$("#citymenu").find("option:selected").val();
	$.ajax({
		url:'get2GEriCellQueryPageMoreDateInfoAction',
		type:'post',
		data:{'monthNum':monthNum,'cityId':cityId},
		dataType:'text',
		success:function(raw){
			var data={};
//			console.log("邻区数据："+raw);
			try{
			data=eval("("+raw+")");
			$("#"+waitforsel).html("");
			for ( var i = 0; i < data['dateInfo'].length; i++) {
				$("#"+waitforsel).append("<option>"+data['dateInfo'][i]['MEA_DATE']+"</option>");
			}
			}catch(err){
				console.log(err);
			}
		},
		complete : function() {
			$("#"+selected).html("");
			if($("#"+selected+" option").size()>30){
	    		//获取option的大小自动设置高度
	    		$("#"+selected).attr("size","30");
	    	}else{
	    		//获取option的大小自动设置高度
	    		$("#"+selected).attr("size",$("#"+selected+" option").size());
	    	}
			if($("#"+waitforsel+" option").size()>30){
	    		//获取option的大小自动设置高度
	    		$("#"+waitforsel).attr("size","30");
	    	}else{
	    		//获取option的大小自动设置高度
	    		$("#"+waitforsel).attr("size",$("#"+waitforsel+" option").size());
	    	}
		}
	});
}