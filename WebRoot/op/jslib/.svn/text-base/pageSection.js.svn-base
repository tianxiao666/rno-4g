

//工单服务跟踪记录
function workOrderTracingRecordSection(woid,divId){
	$.post("getWorkTraceRecordAction",{"WOID":woid},function(data){
		$("#"+divId).html(data);
		/*
		$("#"+divId).html();
		var tableStr = "<table class='main-table1'>";
		tableStr += "<tr>";
		tableStr += "<th>序号</th>";
	    tableStr += "<th>处理时间</th>";
	    tableStr += "<th>处理人</th>";
	    tableStr += "<th>处理类型</th>";
	    tableStr += "<th>处理结果</th>";
	    tableStr += "</tr>";
	    if(data!=null && data.length>0){
		    $.each(data,function(index,value){
		    	tableStr += "<tr>";
		    	tableStr += "<td style='text-align:center'>"+(index+1)+"</td>";
		    	tableStr += "<td style='text-align:center'>"+value.handleTime+"</td>";
		    	tableStr += "<td style='text-align:center'>"+value.handler+"</td>";
		    	tableStr += "<td style='text-align:center'>"+value.handleWay+"</td>";
		    	tableStr += "<td>"+value.handleResultDescription+"</td>";
		    	tableStr += "</tr>";
		    });
	    }else{
	    	tableStr += "<tr>";
	    	tableStr += "<td colspan='5' style='text-align:center'>没有相关的服务跟踪记录。</td>";
	    	tableStr += "</tr>";
	    }
	    tableStr += "</table>";
	    $("#"+divId).html(tableStr);
	    */
    });
}	


//任务单服务跟踪记录
function taskOrderTracingRecordSection(toid,divId){
	$.post("getTaskTraceRecordAction",{"TOID":toid},function(data){
		$("#"+divId).html(data);
		/*
		$("#"+divId).html();
		var tableStr = "<table class='main-table1'>";
		tableStr += "<tr>";
		tableStr += "<th>序号</th>";
	    tableStr += "<th>处理时间</th>";
	    tableStr += "<th>处理人</th>";
	    tableStr += "<th>处理类型</th>";
	    tableStr += "<th>处理结果</th>";
	    tableStr += "</tr>";
	    if(data!=null && data.length>0){
		    $.each(data,function(index,value){
		    	tableStr += "<tr>";
		    	tableStr += "<td style='text-align:center'>"+(index+1)+"</td>";
		    	tableStr += "<td style='text-align:center'>"+value.handleTime+"</td>";
		    	tableStr += "<td style='text-align:center'>"+value.handler+"</td>";
		    	tableStr += "<td style='text-align:center'>"+value.handleWay+"</td>";
		    	tableStr += "<td>"+value.handleResultDescription+"</td>";
		    	tableStr += "</tr>";
		    });
	    }else{
	    	tableStr += "<tr>";
	    	tableStr += "<td colspan='5' style='text-align:center'>没有相关的服务跟踪记录。</td>";
	    	tableStr += "</tr>";
	    }
	    tableStr += "</table>";
	    $("#"+divId).html(tableStr);
	    */
    });
}	




