

//设施基础信息(根据资源Id找)
function showBaseFacilityTable(reId,reType,divId){
	$.post("getBaseFacilityAction",{"resourceId":reId,"resourceType":reType},function(data){
		$("#"+divId).html("");
		var locationStr = "../../resource/physicalres/getPhysicalresForOperaAction?currentEntityType="+reType+"&modelType=view&currentEntityId="+reId+"bizModule=urgentrepair&bizProcessCode="+woId+"&bizProcessId=";
		var tableStr = "<table class='main-table1' >";
		tableStr += "<tr class='main-table1-tr'>";
		tableStr += "<td class='main-table1-title' colspan='4'>";
		tableStr += "<img class='hide-show-img' src='../../images/ico_show.gif' />"+data.name;
        tableStr += "<span style='position:absolute;right:5px;'>";
        tableStr += "<a href='"+locationStr+"' target='_black' >详细信息</a>";
        tableStr += "</span>";
        tableStr += "</td>";
        tableStr += "</tr>";
        tableStr += "<tr>";
        tableStr += "<td class='menuTd'>基站编码：</td>";
        tableStr += "<td>"+data.label+"</td>";
        tableStr += "<td class='menuTd'>覆盖类型：</td>";
        tableStr += "<td>"+data.covertype+"</td>";
        tableStr += "</tr>";
        tableStr += "<tr>";
        tableStr += "<td class='menuTd'>GSM频段：</td>";
        tableStr += "<td></td>";
        tableStr += "<td class='menuTd'>基站标识：</td>";
        tableStr += "<td></td>";
        tableStr += "</tr>";
        tableStr += "<tr>";
        tableStr += "<td class='menuTd'>基站名：</td>";
        tableStr += "<td>"+data.name+"</td>";
        tableStr += "<td class='menuTd'>地址：</td>";
        tableStr += "<td>"+data.address+"</td>";
        tableStr += "</tr>";
        tableStr += "<tr>";
        tableStr += "<td class='menuTd'>资源类型：</td>";
        tableStr += "<td></td>";
        tableStr += "<td class='menuTd'>基站类型：</td>";
        tableStr += "<td>"+data.bstype+"</td>";
        tableStr += "</tr>";
        tableStr += "<tr>";
        tableStr += "<td class='menuTd'>交换局号：</td>";
        tableStr += "<td></td>";
        tableStr += "<td class='menuTd'>覆盖范围：</td>";
        tableStr += "<td></td>";
        tableStr += "</tr>";
        tableStr += "<tr>";
        tableStr += "<td class='menuTd'>重要级别：</td>";
        tableStr += "<td colspan='3'>"+data.importancegrade+"</td>";
        tableStr += "</tr>";
        tableStr += "</table>";
		$("#"+divId).html(tableStr);
	},"json");
}

//设施基础信息(根据资源WOID找)
function showBaseFacilityByWoIdTable(woId,divId){
	$("#"+divId).html("");
	var tableStr ="";
	$.post("getBaseFacilityByWoIdAction",{"WOID":woId},function(data){
		if(data){
			$.each(data,function(index,value){
				var reId = value.networkResourceId;
				var reType = value.networkResourceType;
				var tableStr ="";
				tableStr += "<div class='a_tabs' style=' line-height:26px;'>";
				tableStr += "<a class='a_tabs_on' onclick='showOrHideResource(\"baseFacilityTable\",\""+index+"\",\""+reId+"\",\""+reType+"\",\""+woId+"\");'>设施基础信息</a> |";
				tableStr += "<a onclick='showOrHideResource(\"resourceMaintainRecord\",\""+index+"\",\""+reId+"\",\""+reType+"\",\""+woId+"\");'>资源维护记录</a>";
				tableStr += "<span class='fr'>";
				tableStr += "</span>";
				tableStr += "</div>";
				tableStr += "<div id='baseFacilityTable"+index+"'>"
			    tableStr += commonBaseFacility(reId,reType,woId);
			    tableStr += "</div>";
			    tableStr += "<div id='resourceMaintainRecord"+index+"' style='display:none'>"
			    tableStr += commonResourceMaintainRecordTable(woId,reId,reType);
			    tableStr += "</div>";
			    $("#"+divId).append(tableStr);
			    var pageDivId = "resourceMaintainRecordPage";
			    var showList = $(".resourceMaintainRecordList");
			    var pageSize = 10;
			    pagingColumnByForeground(pageDivId,showList,pageSize);
	            /*
				$.post("getBaseFacilityAction",{"resourceId":reId,"resourceType":reType},function(data1){
					var tableStr = commonBaseFacility(data1,reId,reType,index+"",woId);
					$("#"+divId).append(tableStr);
				},"json");
				*/
			});
		}
	},"json");
}

function commonBaseFacility(reId,reType,woId){
	var locationStr =  "../../resource/physicalres/getPhysicalresForOperaAction?currentEntityType="+reType+"&modelType=view&currentEntityId="+reId+"&bizModule=urgentrepair&bizProcessCode="+woId+"&bizProcessId=";
	var tableStr = "";
	$.ajax({
		url:"getBaseFacilityAction",
		async:false,
		type:"POST",
		dataType:"json",
		data:{"resourceId":reId,"resourceType":reType},
		success : function(result) {
			if(result && result.entity){
			    var i = 0;
			    var j = parseInt(result.size.size)-1;
			    tableStr += "<table class='main-table1 a_tabs_table'>";
				tableStr += "<tr class='main-table1-tr'>";
				tableStr += "<td class='main-table1-title' colspan='4'>";
				tableStr += "<img class='hide-show-img' src='images/ico_show.gif' />"+changeUnkownValue(result.entity.name);
			    tableStr += "<span style='position:absolute;right:5px;'>";
			    tableStr += "<a href='"+locationStr+"' target='_black' >详细信息</a>";
			    tableStr += "</span>";
			    tableStr += "</td>";
			    tableStr += "</tr>";
			    $.each(result.dictionary,function(key,value){
			    	if(i%2==0 && i!=j){
			    		tableStr += "<tr>";
			    		tableStr += "<td class='menuTd'>"+result.dictionary[key]+"</td>";
			    		tableStr += "<td style='width:40%;'>"+changeUnkownValue(result.entity[key])+"</td>";
			    	}else if(i==j && i%2==0){
			    		tableStr += "<tr>";
			    		tableStr += "<td class='menuTd'>"+result.dictionary[key]+"</td>";
			    		tableStr += "<td colspan='3'>"+changeUnkownValue(result.entity[key])+"</td>";
			    		tableStr += "</tr>";
			    	}else{
			    		tableStr += "<td class='menuTd'>"+result.dictionary[key]+"</td>";
			    		tableStr += "<td>"+changeUnkownValue(result.entity[key])+"</td>";
			    		tableStr += "</tr>";
			    	}
			    	i++;
			    });
			    tableStr += "</table>";
    		}
		}
	});
	return tableStr;
}

/*
function commonBaseFacility(data,reId,reType,index,woId){
	var name = "";
	if(data && data.entity){
		name = data.entity.name;
	}
	var locationRef = window.location.host;
	locationRef = getNetUrl();
	var locationStr = locationRef + "resource/physicalres/getPhysicalresForOperaAction?currentEntityType="+reType+"&modelType=view&currentEntityId="+reId+"&bizModule=urgentrepair&bizProcessCode="+woId+"&bizProcessId=";
	var tableStr ="";
	tableStr += "<div class='a_tabs' style=' line-height:26px;'>";
	tableStr += "<a class='a_tabs_on' onclick='showOrHideResource(\"baseFacilityTable\",\""+index+"\",\""+reId+"\",\""+reType+"\",\""+woId+"\");'>设施基础信息</a> |";
	tableStr += "<a onclick='showOrHideResource(\"resourceMaintainRecord\",\""+index+"\",\""+reId+"\",\""+reType+"\",\""+woId+"\");'>资源维护记录</a>";
	tableStr += "<span class='fr'>";
	tableStr += "</span>";
	tableStr += "</div>";
	tableStr += "<table id='baseFacilityTable"+index+"' class='main-table1 a_tabs_table'>";
	tableStr += "<tr class='main-table1-tr'>";
	tableStr += "<td class='main-table1-title' colspan='4'>";
	tableStr += "<img class='hide-show-img' src='images/ico_show.gif' />"+name;
    tableStr += "<span style='position:absolute;right:5px;'>";
    tableStr += "<a href='"+locationStr+"' target='_black' >详细信息</a>";
    tableStr += "</span>";
    tableStr += "</td>";
    tableStr += "</tr>";
    if(data && data.entity){
	    var i = 0;
	    var j = parseInt(data.size.size)-1;
	    $.each(data.entity,function(key,value){
	    	if(i%2==0 && i!=j){
	    		tableStr += "<tr>";
	    		tableStr += "<td class='menuTd'>"+data.dictionary[key]+"</td>";
	    		tableStr += "<td style='width:40%;'>"+changeUnkownValue(data.entity[key])+"</td>";
	    	}else if(i==j && i%2==0){
	    		tableStr += "<tr>";
	    		tableStr += "<td class='menuTd'>"+data.dictionary[key]+"</td>";
	    		tableStr += "<td colspan='3'>"+changeUnkownValue(data.entity[key])+"</td>";
	    		tableStr += "</tr>";
	    	}else{
	    		tableStr += "<td class='menuTd'>"+data.dictionary[key]+"</td>";
	    		tableStr += "<td>"+changeUnkownValue(data.entity[key])+"</td>";
	    		tableStr += "</tr>";
	    	}
	    	i++;
	    });
    }
    tableStr += "</table>";
    tableStr += "<div id='resourceMaintainRecord"+index+"' style='display:none'>"
    tableStr += commonResourceMaintainRecordTable(woId,reId,reType);
    tableStr += "</div>"
    return tableStr;
}
*/
function commonResourceMaintainRecordTable(woId,reId,reType){
	var tableStr = "";
	 $.ajax({
            url:"getResourceMaintainRecordAction",
            async:false,
            type:"POST",
            dataType:"json",
            data:{"resourceId":reId,"resourceType":reType,"WOID":woId},
            success : function(result) {
            	tableStr += "<table class='main-table1 a_tabs_table tc'>";
				tableStr += "<tr>";
				tableStr += "<th>资源类型</th>";
			    tableStr += "<th>资源ID</th>";
			    tableStr += "<th>维护操作</th>";
			    tableStr += "<th>维护内容</th>";
			    tableStr += "<th>维护人</th>";
			    tableStr += "<th>维护时间</th>";
			    tableStr += "</tr>";
		    	if(result){
				    $.each(result,function(index,val){
				    	var isBelong = val.isBelong;
				    	if(isBelong=="true"){
				    		tableStr += "<tr class='changeBackColor resourceMaintainRecordList'>";
				    	}else{
				    		tableStr += "<tr class='resourceMaintainRecordList'>";
				    	}
				    	tableStr += "<td>"+val.resourceType+"</td>";
				    	tableStr += "<td>"+val.resourceId+"</td>";
				    	tableStr += "<td>"+val.maintainOperation+"</td>";
				    	var maintainContent = val.maintainContent;
				    	var contentArray = maintainContent.split("$_$");
				    	var content = "";
				    	$.each(contentArray,function(i,v){
				    		content +=v+"</br>";
				    	});
				    	if(content!=""){
				    		content = content.substring(0,content.length-5);
				    	}
				    	tableStr += "<td>"+content+"</td>";
				    	tableStr += "<td>"+val.maintainer+"</td>";
				    	tableStr += "<td>"+val.maintainTime+"</td>";
				    	tableStr += "</tr>";
				    });
			    }
			    tableStr += "</table>";
			    tableStr += "<div id='resourceMaintainRecordPage'>";
			    tableStr += "</div>";
            }
	});
	/*
 	$.post("getResourceMaintainRecordAction",{"resourceId":reId,"resourceType":reType,"WOID":woId},function(data){
		tableStr += "<table id='resourceMaintainRecord"+index+"' class='main-table1 a_tabs_table tc' style='display:none'>";
		tableStr += "<tr>";
		tableStr += "<th>资源类型</th>";
	    tableStr += "<th>资源ID</th>";
	    tableStr += "<th>维护操作</th>";
	    tableStr += "<th>维护内容</th>";
	    tableStr += "<th>维护人</th>";
	    tableStr += "<th>维护时间</th>";
	    tableStr += "</tr>";
    	if(data){
		    $.each(data,function(index,value){
		    	tableStr += "<tr>";
		    	tableStr += "<td>"+value.resourceType+"</td>";
		    	tableStr += "<td>"+value.resourceId+"</td>";
		    	tableStr += "<td>"+value.maintainOperation+"</td>";
		    	tableStr += "<td>"+value.maintainContent+"</td>";
		    	tableStr += "<td>"+value.maintainer+"</td>";
		    	tableStr += "<td>"+value.maintainTime+"</td>";
		    	tableStr += "</tr>";
		    });
	    }
	    tableStr += "</table>";
    },"json");
    */
    return tableStr;
}

//资源维护记录
function showResourceMaintainRecordTable(tableId){
	var tableStr = "<table class='main-table1 tc'>";
	$.post("getResourceMaintainRecordAction",function(data){
		tableStr += "<tr>";
		tableStr += "<th>资源类型</th>";
	    tableStr += "<th>资源ID</th>";
	    tableStr += "<th>维护操作</th>";
	    tableStr += "<th>维护内容</th>";
	    tableStr += "<th>维护人</th>";
	    tableStr += "<th>维护时间</th>";
	    tableStr += "</tr>";
	    if(data){
		    $.each(data,function(index,value){
		    	tableStr += "<tr>";
		    	tableStr += "<td>"+value.resourceType+"</td>";
		    	tableStr += "<td>"+value.resourceId+"</td>";
		    	tableStr += "<td>"+value.maintainOperation+"</td>";
		    	tableStr += "<td>"+value.maintainContent+"</td>";
		    	tableStr += "<td>"+value.maintainer+"</td>";
		    	tableStr += "<td>"+value.maintainTime+"</td>";
		    	tableStr += "</tr>";
		    });
	    }
	    tableStr +="</table>"
	    $("#"+tableId).html(tableStr);
    },"json");
}

function showOrHideResource(id,index,reId,reType,woId){
	var idStr = id+index;
	var baseStr = "baseFacilityTable"+index;
	var resordStr = "resourceMaintainRecord"+index;
	if(idStr==baseStr){
		$("#"+baseStr).show();
		$("#"+resordStr).hide();
		var str = commonBaseFacility(reId,reType,woId);
		$("#baseFacilityTable"+index).html(str);
	}else if(idStr == resordStr){
		$("#"+resordStr).show();
		$("#"+baseStr).hide();
		var str = commonResourceMaintainRecordTable(woId,reId,reType);
		$("#resourceMaintainRecord"+index).html(str);
		var pageDivId = "resourceMaintainRecordPage";
	    var showList = $(".resourceMaintainRecordList");
	    var pageSize = 10;
	    pagingColumnByForeground(pageDivId,showList,pageSize);
	}
}