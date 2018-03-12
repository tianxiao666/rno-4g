/**
 * 根据json匹配数据到一个form formId
 */
function jsonToForm(objects) {
	if (objects != null) 
	{
		for ( var key in objects) 
		{
			var objValue= objects[key];
			if(objValue==null)
			{
				continue;
			}
			//获取需要绑定数据的标签的类型
			var tagName = "";
			$("."+key).each(function(){
				tagName = $(this)[0].tagName;
				if(tagName == "DIV" || tagName=="span" || tagName=="label" || tagName=="em"){
					$(this).html(objValue);
				}else if(tagName == "INPUT" || tagName == "HIDDEN" || tagName=="textarea"){
					$(this).val(objValue);
				}
			});		
				
		}
	}
 }
var ids = "";
var userAreaIds = "";
function loadUserArea(){	
 	var parentAreatext = "请选择";
 	var parentAreavalue = "";
 	var selectIds = "";
 	var j = 0;
	 var url= 'getUserAreaAction';
	$.ajax({
		url:url,
		data:'',
		async:false,
		dataType:'json',
		type:'post',
		success:function(data){
	 		if(data.userAreaMap){
			 	$("#treeRootName").text(data.userAreaMap.name);
			 	$("#treeRootName").next().val(data.userAreaMap.id);
			 	$("#treeRootName").next().next().val(data.userAreaMap._entityType);
			 	$("#treeRootName").next().next().next().html("");
			 	$("#treeRootName").prev().attr("src","image/plus.gif");
			 	$("#hidTypeFlush").val("");
			 	$("#hidEntityFlush").val("");
			 	$("#logicalresTreeId").val(data.userAreaMap.id);
			 	$("#logicalresTreeType").val(data.userAreaMap._entityType);
			 	$("#logicalresTreeName").val(data.userAreaMap.name);
	 		}
			var counttext = "";
		 	var con = "";
			if(data.parentArea){
				var ind = data.parentArea.length;
				var isGo = false;
				var parentArea = data.operationalChildAreaList;
				$.each(data.parentArea, function(index, obj){
					//alert(obj.name);
					j = index;
					if(index == 0){
						parentAreatext = obj.name;
						parentAreavalue = obj._entityType+","+obj.id+","+obj.name+","+obj.longitude+","+obj.latitude;
						$.post("getParentAreaAction",function(data){
							selectIds = "province_select";
							var count = "<select onchange='selectChildArea(this);' id='province_select0'>"
								+"<option value=''>请选择</option>";
							data = eval(data);
							$.each(data, function(index, obj){
								$.each(parentArea,function(oindex, oobj){
									if(oobj == obj.id){
										count = count + 
										"<option value='"+obj._entityType+","+obj.id+","+obj.name+","+obj.longitude+","+obj.latitude+"'>"+obj.name+"</option>";
									}
								});
							});
							count = count + "</select><div   style='width: 100%; height: 25px; display: inline; margin-left: 3px;'></div>";
							$("#top_tool_bar").html($("#top_tool_bar").html()+count);
							$("#province_select0").next().html(counttext);
							$("#province_select0").val(parentAreavalue);
						});
					}
					if(data.childArea[obj.id]){
						var c = index+1;
				 		counttext = counttext + "<select onchange='selectChildArea(this);'  id='province_select"+c+"' value=''>"
								+"<option>请选择</option>";
						$.each(data.childArea[obj.id], function(index, obj){
							$.each(parentArea,function(oindex, oobj){
							if(oobj == obj.id){
								counttext = counttext + 
								"<option value='"+obj._entityType+","+obj.id+","+obj.name+","+obj.longitude+","+obj.latitude+"'>"+obj.name+"</option>";
								}
							});
						});
						counttext = counttext + "</select><div   style='width: 100%; height: 25px; display: inline; margin-left: 3px;'>";
						con = con + "</div>";
					}
					ind--;
					if(ind <= 0){
						isGo = true;
					}
				});
				
				var timeID=window.setInterval(function(){
				if(isGo == true){
					isGo = null;
					$.each(data.parentArea, function(index, obj){
						if(index != 0){
							$("#province_select"+index).val(obj._entityType+","+obj.id+","+obj.name+","+obj.longitude+","+obj.latitude);
						}
					});
				}else if(isGo == null){
					clearInterval(timeID);
				}
				},1000);
			}
			
			if(data.operationalChildAreaList){
				$.each(data.operationalChildAreaList, function(index, obj){
					userAreaIds = userAreaIds + obj + ",";
				});
			}
			if(!data.operationalAreaList){
				
			}
		}
	});
}
function selectChildArea(me){
	 if($(me).val()){
	 	var val = $(me).val();
	 	var array = val.split(",");
	 	var parentEntityType = array[0];
	 	var parentEntityId = array[1];
	 	var parentName = array[2];
	 	var longitude = array[3];
	 	var latitude = array[4];
	 	var userAreaIdsarr = userAreaIds.split(",");
	 	$("#treeRootName").text(parentName);
	 	$("#treeRootName").next().val(parentEntityId);
	 	$("#treeRootName").next().next().val(parentEntityType);
	 	$("#treeRootName").next().next().next().html("");
	 	$("#treeRootName").prev().attr("src","image/plus.gif");
	 	$("#hidTypeFlush").val("");
	 	$("#hidEntityFlush").val("");
	 	$("#logicalresTreeId").val(parentEntityId);
	 	$("#logicalresTreeType").val(parentEntityType);
	 	$("#logicalresTreeName").val(parentName);
	 	$("#lTreeId").val(parentEntityId);
	 	$("#lTreeType").val(parentEntityType);
	 	var currentEntityType = "Sys_Area";
	 	var params = {parentEntityType:parentEntityType,parentEntityId:parentEntityId,currentEntityType:currentEntityType};
	 	$.post("getChildTypeByEntityAction",params,function(data){
		 	if(data != "[]"){
		 		var countIndex = 0;
		 		var count = "<select onchange='selectChildArea(this);'>"
						+"<option>请选择</option>";
						data = eval(data);
						$.each(data, function(index, obj){
							for(var i = 0;i < userAreaIdsarr.length;i++){
								if(obj.id == userAreaIdsarr[i]){
									countIndex++;
									count = count + 
									"<option value='"+obj._entityType+","+obj.id+","+obj.name+","+obj.longitude+","+obj.latitude+"'>"+obj.name+"</option>";
								}
							}
						});
						if(countIndex == 0){
							$.each(data, function(index, obj){
								count = count + 
									"<option value='"+obj._entityType+","+obj.id+","+obj.name+","+obj.longitude+","+obj.latitude+"'>"+obj.name+"</option>";
							});
						}
					count = count + "</select><div   style='width: 100%; height: 25px; display: inline; margin-left: 3px;'></div>";
					$(me).next().html(count);
		 		}
		 		if ( $(me).attr("id") != "province_select" && longitude != "undefined" && latitude != "undefined" ) {
		 			var lonLat = new ILatLng(latitude, longitude);
		 			map.panTo(lonLat);
		 		}
		 });
	 }
}