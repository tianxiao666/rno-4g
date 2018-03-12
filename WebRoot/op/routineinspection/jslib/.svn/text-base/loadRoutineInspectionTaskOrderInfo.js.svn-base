$(function(){
	//根据任务单获取设备
	loadResourceByToId();
});

//根据任务单获取设备
function loadResourceByToId(){
	var TOID = $("#TOID").val();
	var isSuccess = "true";
	var totalSize = 0;
	var size = 0;
	$.post("getRoutineInspectionResourceByProfessionAction",{"TOID":TOID},function(data){
		var str = "";
		str += "<ul class='nav_ul'>";
		$.each(data,function(key,value){
			str +="<li>";
			str +="<span class='list_title open'>"+key+"</span>";
			str += "<ul class='list_ul2' style='display:block;'>";
			$.each(value,function(k,v){
				str += "<li class='eList' onclick='changeBGColor(this);showResourceTemplate(\""+v.type+"\",\""+v.name+"\",\""+v.id+"\");'>"+v.name;
				if(v.isComplete=="false"){
					isSuccess = v.isComplete;
				}else{
					str += "<em class='done'></em>";
					size ++;
				}
				str += "</li>";
				totalSize++;
			});
			str += "</ul>";
			str +="</li>";
		});
		str += "</ul>";
		$("#equipmentList").html(str);
		//判断选中的位置
		if(size==0){
			$(".eList").eq(0).click();
		}else if(size==totalSize){
			$(".eList").eq(0).click();
		}else{
			$.each($(".eList"),function(i,val){
				if($(this).find("em").html()!=""){
					$(".eList").eq(i).click();
					return false;
				}
			});
		}
		if(isSuccess=="true"){
			var status = $("#status").val();
			if(status==24){
				$("#signOut").attr("disabled","disabled");
			}else{
				$("#signOut").removeAttr("disabled","disabled");
			}
		}else{
			$("#signOut").attr("disabled","disabled");
		}
	},"json");
}

//获取设备模板
function showResourceTemplate(eType,eName,eId){
	$("#eId").val(eId);
	$("#eType").val(eType);
	$("#rightTable").show();
	$("#eName").html(eName);
	var WOID = $("#WOID").val();
	var tempIdJsonStr = "";
	$.post("getRoutineInspectionRecordTemplateAction",{"WOID":WOID,"eType":eType},function(data){
		var str = "";
		str += "<table class='thleft_table inside_table'>";
		str += "<tr><th style='width:400px;'>巡检内容</th><th>巡检结果</th><th>备注</th></tr>";
		$.each(data,function(index,value){
			tempIdJsonStr += "{\"" + index + "\":\"" + value.id + "\"},";
			str += value.tag;
		});
		str += "</table>";
		$("#eContent").html(str);
		
		if(tempIdJsonStr != ""){
			tempIdJsonStr = tempIdJsonStr.substring(0,tempIdJsonStr.length-1);
		}
		tempIdJsonStr = "[" + tempIdJsonStr + "]";
		$("#tempIds").val(tempIdJsonStr);
		
		//填充设备内容
		getResourceContent();
	},"json");
}

//填充设备内容
function getResourceContent(){
	var eId = $("#eId").val();
	var eType = $("#eType").val();
	var TOID = $("#TOID").val();
	$.post("getRoutineInspectionRecordExampleAction",{"TOID":TOID,"eId":eId,"eType":eType},function(data){
		var trObj = $("#eContent").children().children().children();
		$.each(data,function(index,value){
			var type = trObj.eq(index+1).find("input").eq(0).attr("type");
			if(type=="text"){
				//文本框
				trObj.eq(index+1).find("input").eq(0).val(value.value);
			}else if(type=="radio"){
				//单选
				$.each(trObj.eq(index+1).find("input"),function(){
					if($(this).val()==value.value){
						$(this).attr("checked","checked");
					}
				});
			}else if(type=="checkbox"){
				//多选
			}
			//填充备注
			trObj.eq(index+1).children().eq(2).children().eq(0).val(value.remark);
		});
	},"json");
}

//提交资源巡检结果
function submitResourceContent(){
	var valueJsonStr = "";
	var remarkJsonStr = "";
	var trObj = $("#eContent").children().children().children();
	$.each(trObj,function(index,value){
		if(index!=0){
			var type = $(this).find("input").eq(0).attr("type");
			if(type=="text"){
				//文本框
				valueJsonStr += "{\"" + (index-1) + "\":\"" + $(this).find("input").eq(0).val() + "\"},";
			}else if(type=="radio"){
				//单选
				$.each($(this).find("input"),function(){
					if($(this).attr("checked")=="checked"){
						valueJsonStr += "{\"" + (index-1) + "\":\"" + $(this).val() + "\"},";
					}
				});
			}else if(type=="checkbox"){
				//多选
			}
			var remark = $(this).children().eq(2).children().eq(0).val();
			remarkJsonStr += "{\"" + (index-1) + "\":\"" + remark + "\"},";
		}
	});
	
	if(valueJsonStr!=""){
		valueJsonStr = valueJsonStr.substring(0,valueJsonStr.length-1);
	}
	valueJsonStr = "["+valueJsonStr+"]";
	
	if(remarkJsonStr!=""){
		remarkJsonStr = remarkJsonStr.substring(0,remarkJsonStr.length-1);
	}
	remarkJsonStr = "["+remarkJsonStr+"]";
	var eId = $("#eId").val();
	var eType = $("#eType").val();
	var tempIds = $("#tempIds").val();
	var TOID = $("#TOID").val();
	var WOID = $("#WOID").val();
	$.post("saveRoutineInspectionRecordAction",{"WOID":WOID,"TOID":TOID,"eId":eId,"eType":eType,"tempIdJsonStr":tempIds,"remarkJsonStr":remarkJsonStr,"valueJsonStr":valueJsonStr},function(data){
		if(data=="true"){
			alert("操作成功");
			loadResourceByToId();
		}else{
			alert("操作失败");
		}
	},"json");
}