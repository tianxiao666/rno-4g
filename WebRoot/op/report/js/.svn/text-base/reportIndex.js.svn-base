$(function(){
	if(!$("#timehidden").val()){
		getUrgentRepairworkorder('reportUrgentRepairworkorderStatistics');
	}else{
		getUrgentRepairworkorder('reportUrgentRepairworkorderStatistics');
	}
	});
	
function openReport(){
	window.open("reportIndex.jsp");
}	
	
function clickThis(me){
	$(".leftMultilevelMenu_info li").removeClass("menu_selected");
	$(me).addClass("menu_selected");
}

/**
 * 跳转到车辆页面
 * @param {Object} reportType
 */
function forwardTOReportCarPage(reportType){
	var url = "forwardToReportCarAction";
	var params = {reportType:reportType};
	$.post(url, params, function(data){
		$("#statements_right").html(data);
	});
}


/**
 * 跳转到人员页面
 * @param {Object} reportType
 */
function forwardTOReportStaffPage(reportType){
	var url = "forwardToReportStaffAction";
	var params = {reportType:reportType};
	$.post(url, params, function(data){
		$("#statements_right").html(data);
	});
}



//显示报表表格
	function showOrHideStationTableDiv(me){
		if($(me).html() == "<em>[+]</em>展开表格"){
			$(me).html("<em>[-]</em>展开表格");
			$("#stationTableDiv").show();
		}else{
			$(me).html("<em>[+]</em>展开表格");
			$("#stationTableDiv").hide();
		}
	}
	
	//删除业务数据筛选条件
	function delrole(me){
		$(me).parent().remove();
	}
	
	//添加业务数据筛选条件
	function addrole(){
		if($("#rowName").val() != "" && $("#Judge").val() != "" && $("#rowValue").val() != ""){
			var context = $("#roleul").html()
							+	"<li class='clearfix'>"
							+	"<p class='settings_role_info'>"
							+	"<span class='rowNameText'>"+$("#rowName option:selected").text()+"</span>"
							+	"<input type='hidden' class='rowName' value='"+$("#rowName").val()+"'/>"
							+	"<span class='Judge'>"+$("#Judge").val()+"</span>"
							+	"<span class='rowValue'>"+$("#rowValue").val()+"</span>"
							+	"</p>"
							+	"<a class='settings_role_del' onclick='delrole(this);'>×</a>"
							+"</li>";
			$("#roleul").html(context);
		}
	}
	
	function showComment(){
		$("#emType").text($(".ontab").text());
	    	$("#comment_title").text($("#DateTimeSpan").text() + $("#orginnstName").val());
		    $(".comment_dialog").show();
			$(".comment_black").show();
	}
	
	function showRowValue(me){
	var rowName = $(me).val();
	$("#rowValue option").hide();
	if(rowName != "faultGenera"){
		var value = $("#hidden"+rowName).val();
		var va = value.split(",");
		var context = "";
		$.each(va,function(k,v){
			if(v != null && v != ""){
				context = context +"<option class='faultGenera'  value='"+v+"'>"+v+"</option>";
			}
		});
		$("#rowValue").html(context);
		//$("#rowValue").val($("#rowValue .baseStationLevel").eq(0).val());
		//$("#rowValue .baseStationLevel").show();
	}else{
		//$("#rowValue").val($("#rowValue .faultGenera").eq(0).val());
		//$("#rowValue .faultGenera").show();
		var context = "<option class='faultGenera'  value='业务侧原因'>业务侧原因</option>"
								+"<option class='faultGenera'  value='传输原因'>传输原因</option>"
								+"<option class='faultGenera'  value='动力环境'>动力环境</option>"
								+"<option class='faultGenera'  value='工程维护'>工程维护</option>"
								+"<option class='faultGenera'  value='互联互通原因'>互联互通原因</option>"
								+"<option class='faultGenera'  value='认为原因'>认为原因</option>"
								+"<option class='faultGenera'  value='软件原因'>软件原因</option>"
								+"<option class='faultGenera'  value='硬件原因'>硬件原因</option>"
								+"<option class='faultGenera'  value='自然灾害'>自然灾害</option>"
								+"<option class='faultGenera'  value='客户端原因'>客户端原因</option>"
								+"<option class='faultGenera'  value='用户原因'>用户原因</option>"
								+"<option class='faultGenera'  value='其他'>其他</option>	";
		$("#rowValue").html(context);
	}
	}