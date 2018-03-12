function isNumberOr_Letter( s ) { 
	//判断是否是数字或字母
	var regu = "^[0-9a-zA-Z\_]+$"; var re = new RegExp(regu); 
	if (re.test(s)) 
	{ 
		return true; 
	} 
	else 
	{ 
		return false; 
	} 
	}; 
//递归搜索	
function  searchResouceRecursion(chosenTypeName,chosenType,areaId,searchConditionText){
	var url="searchResourceForChoosenResourceAction";
	var params="";
	var searchConditionType="";
	if(isNumberOr_Letter(searchConditionText)){
		searchConditionType="label";
	}else{
		searchConditionType="name";
	}
	var typeName = chosenTypeName;
	if(chosenTypeName=="selectRangeResChosenType"){
		var selectRangeResChosenType=chosenType;
		params={selectRangeResChosenType:selectRangeResChosenType,areaId:areaId,searchConditionText:searchConditionText,searchConditionType:searchConditionType}
	}else{
		var selectResChosenType=chosenType;
		params={selectResChosenType:selectResChosenType,areaId:areaId,searchConditionText:searchConditionText,searchConditionType:searchConditionType}
	}
	
	$.post(url,params,function(data){
		if(data!=null&&data!=""){
			var content="";
			$.each(data,function(index,value){
				if(value.name!=undefined){
					var stringObj=value.name;    
          		    var newstr=stringObj.replace(searchConditionText,"<em style='color:red'>"+searchConditionText+"</em>"); 
					content +="<li><span class='aetg_text' onclick='chooseResourceForChildResource(this)'>"+newstr+"</span><input type='hidden' value='"+value.name+"'/><input type='hidden' value='"+chosenTypeName+"'/><input type='hidden' value='"+value.id+"'/><input type='hidden' value='"+value._entityType+"'/><span class='aetg_link'><a  href=\"javascript:showBasicResourceInfo('"+value.id+"','"+value._entityType+"')\">基本信息</a><a href='getPhysicalresForOperaAction?currentEntityType="+value._entityType+"&currentEntityId="+value.id+"&areaId="+areaId+"&modelType=view' target='_blank'>详细</a></span></li>";
				}else{
					var stringObj=value.label;    
          		    var newstr=stringObj.replace(searchConditionText,"<em style='color:red'>"+searchConditionText+"</em>"); 
					content +="<li><span class='aetg_text' onclick='chooseResourceForChildResource(this)'>"+newstr+"</span><input type='hidden' value='"+value.label+"'/><input type='hidden' value='"+chosenTypeName+"'/><input type='hidden' value='"+value.id+"'/><input type='hidden' value='"+value._entityType+"'/><span class='aetg_link'><a  href=\"javascript:showBasicResourceInfo('"+value.id+"','"+value._entityType+"')\">基本信息</a><a href='getPhysicalresForOperaAction?currentEntityType="+value._entityType+"&currentEntityId="+value.id+"&areaId="+areaId+"&modelType=view' target='_blank'>详细</a></span></li>";
				}
			})
			if(chosenTypeName=="selectRangeResChosenType"){
				$("#rdRange").html(content);
				$("#rdRange").parent().show();
			}else{
				$("#rds").html(content);
				$("#rds").parent().show();
			}
			
		}else{
			if(typeName=="selectRangeResChosenType"){
				$("#rdRange").html("<li>无此查询条件资源</li>");
				$("#rdRange").parent().show();
			}else{
				$("#rds").html("<li>无此查询条件资源</li>");
				$("#rds").parent().show();
			}
		}
	},'json');
}

//选择查询出来的资源
function chooseResourceForChildResource(me){	
		//记录选择的起点资源信息
		var currentEntityName=$(me).next().val();
		var currentEntityId = $(me).next().next().next().val();
		var currentEntityType = $(me).next().next().next().next().val();
		if($(me).next().next().val()=="selectRangeResChosenType"){
			$("#txtRangeExactContent").val(currentEntityName);
			$("#hidRangeAssEntityId").val(currentEntityId);
			$("#hidRangeAssEntityType").val(currentEntityType);
			$("#rdRange").parent().slideUp('fast');
			$("#assRangeResDiv").slideUp('fast');
			
		}else{
			$("#txtExactContent").val(currentEntityName);
			$("#hidAssEntityId").val(currentEntityId);
			$("#hidAssEntityType").val(currentEntityType);
			$("#rds").parent().slideUp('fast');
			$("#assResDiv").slideUp('fast');
		}
		if($("#hidImportEntityType").val()!=""&& $("#hidImportEntityType").val()!=undefined){
			showProgress();
		}else{
			isCanSearch();
		}
		

}
//显示资源基本信息
function showBasicResourceInfo(id,type){
	params={"currentEntityType":type,"currentEntityId":id,"loadBasicPage":"loadBasicPage"};
	$.post("../physicalres/getPhysicalresAction",params,function(data){
		$(".dialog_content").html(data);
		$("#dialog").show();
		$(".dialog_black").show();
	});
}

//递归搜索	
function  searchResouceRecursionForLogicalres(chosenTypeName,chosenType,areaId,searchConditionText){
	$("#rd").empty();
	var url="searchResourceForChoosenResourceAction";
	var params="";
	var searchConditionType="";
	if(isNumberOr_Letter(searchConditionText)){
		searchConditionType="label";
	}else{
		searchConditionType="name";
	}
	var typeName = chosenTypeName;
	if(chosenType.indexOf(",")>=0){
		if(chosenType.indexOf("BaseStation")>=0){
			chosenType='GeneralBaseStation';
			var selectRangeResChosenType=chosenType;
			params={selectRangeResChosenType:selectRangeResChosenType,areaId:areaId,searchConditionText:searchConditionText,searchConditionType:searchConditionType}
			$.post(url,params,function(data){
				if(data!=null&&data!=""){
					var content="";
					$.each(data,function(index,value){
						if(value.name!=undefined){
							var stringObj=value.name;    
		          		    var newstr=stringObj.replace(searchConditionText,"<em style='color:red'>"+searchConditionText+"</em>"); 
							content +="<li><span class='aetg_text' onclick='chooseResourceForChildResourceForLogicalres(this)'>"+newstr+"</span><input type='hidden' value='"+value.name+"'/><input type='hidden' value='selectRangeResChosenType'/><input type='hidden' value='"+value.id+"'/><input type='hidden' value='"+value._entityType+"'/><span class='aetg_link'><a  href=\"javascript:showBasicResourceInfo('"+value.id+"','"+value._entityType+"')\">基本信息</a><a href='../physicalres/getPhysicalresForOperaAction?currentEntityType="+value._entityType+"&currentEntityId="+value.id+"&areaId="+areaId+"&modelType=view' target='_blank'>详细</a></span></li>";
						}else{
							var stringObj=value.label;    
		          		    var newstr=stringObj.replace(searchConditionText,"<em style='color:red'>"+searchConditionText+"</em>"); 
							content +="<li><span class='aetg_text' onclick='chooseResourceForChildResourceForLogicalres(this)'>"+newstr+"</span><input type='hidden' value='"+value.label+"'/><input type='hidden' value='selectRangeResChosenType'/><input type='hidden' value='"+value.id+"'/><input type='hidden' value='"+value._entityType+"'/><span class='aetg_link'><a  href=\"javascript:showBasicResourceInfo('"+value.id+"','"+value._entityType+"')\">基本信息</a><a href='../physicalres/getPhysicalresForOperaAction?currentEntityType="+value._entityType+"&currentEntityId="+value.id+"&areaId="+areaId+"&modelType=view' target='_blank'>详细</a></span></li>";
						}
					})
						$("#rd").html(content);
						$("#rd").parent().show();	
				}else{
				
						$("#rd").html("<li>无此查询条件资源</li>");
						$("#rd").parent().show();
				}
			},'json');
		}else{
			chosenType = chosenType.split(",");
			var indexFlag=0;
			for(var index=0;index<chosenType.length;index++){
				var selectResChosenType=chosenType[index]+"_parent";
				params={selectResChosenType:selectResChosenType,areaId:areaId,searchConditionText:searchConditionText,searchConditionType:searchConditionType}
				$.post(url,params,function(data){
					if(data!=null&&data!=""){
						var content="";
						$.each(data,function(index,value){
							if(value.name!=undefined){
								var stringObj=value.name;    
			          		    var newstr=stringObj.replace(searchConditionText,"<em style='color:red'>"+searchConditionText+"</em>"); 
								content +="<li><span class='aetg_text' onclick='chooseResourceForChildResourceForLogicalres(this)'>"+newstr+"</span><input type='hidden' value='"+value.name+"'/><input type='hidden' value='selectRangeResChosenType'/><input type='hidden' value='"+value.id+"'/><input type='hidden' value='"+value._entityType+"'/><span class='aetg_link'><a  href=\"javascript:showBasicResourceInfo('"+value.id+"','"+value._entityType+"')\">基本信息</a><a href='../physicalres/getPhysicalresForOperaAction?currentEntityType="+value._entityType+"&currentEntityId="+value.id+"&areaId="+areaId+"&modelType=view' target='_blank'>详细</a></span></li>";
							}else{
								var stringObj=value.label;    
			          		    var newstr=stringObj.replace(searchConditionText,"<em style='color:red'>"+searchConditionText+"</em>"); 
								content +="<li><span class='aetg_text' onclick='chooseResourceForChildResourceForLogicalres(this)'>"+newstr+"</span><input type='hidden' value='"+value.label+"'/><input type='hidden' value='selectRangeResChosenType'/><input type='hidden' value='"+value.id+"'/><input type='hidden' value='"+value._entityType+"'/><span class='aetg_link'><a  href=\"javascript:showBasicResourceInfo('"+value.id+"','"+value._entityType+"')\">基本信息</a><a href='../physicalres/getPhysicalresForOperaAction?currentEntityType="+value._entityType+"&currentEntityId="+value.id+"&areaId="+areaId+"&modelType=view' target='_blank'>详细</a></span></li>";
							}
						})
							$("#rd").append(content);
							//$("#rd").parent().show();	
					}else{
					
							//$("#rd").html("<li>无此查询条件资源</li>");
							$("#rd").parent().show();
					}
					++indexFlag;
				},'json');
			}
			var showInterval = setInterval(function(){
				if(indexFlag==chosenType.length){
					if($("#rd").children().size()==0){
						$("#rd").html("<li>无此查询条件资源</li>");
						clearInterval(showInterval);
					}else{
						clearInterval(showInterval);
					}
				}
			},4);
			
			$("#rd").parent().show();
		}
	}else{
		var selectResChosenType=chosenType+"_parent";
		params={selectResChosenType:selectResChosenType,areaId:areaId,searchConditionText:searchConditionText,searchConditionType:searchConditionType}
		$.post(url,params,function(data){
			if(data!=null&&data!=""){
				var content="";
				$.each(data,function(index,value){
					if(value.name!=undefined){
						var stringObj=value.name;    
	          		    var newstr=stringObj.replace(searchConditionText,"<em style='color:red'>"+searchConditionText+"</em>"); 
						content +="<li><span class='aetg_text' onclick='chooseResourceForChildResourceForLogicalres(this)'>"+newstr+"</span><input type='hidden' value='"+value.name+"'/><input type='hidden' value='selectRangeResChosenType'/><input type='hidden' value='"+value.id+"'/><input type='hidden' value='"+value._entityType+"'/><span class='aetg_link'><a  href=\"javascript:showBasicResourceInfo('"+value.id+"','"+value._entityType+"')\">基本信息</a><a href='../physicalres/getPhysicalresForOperaAction?currentEntityType="+value._entityType+"&currentEntityId="+value.id+"&areaId="+areaId+"&modelType=view' target='_blank'>详细</a></span></li>";
					}else{
						var stringObj=value.label;    
	          		    var newstr=stringObj.replace(searchConditionText,"<em style='color:red'>"+searchConditionText+"</em>"); 
						content +="<li><span class='aetg_text' onclick='chooseResourceForChildResourceForLogicalres(this)'>"+newstr+"</span><input type='hidden' value='"+value.label+"'/><input type='hidden' value='selectRangeResChosenType'/><input type='hidden' value='"+value.id+"'/><input type='hidden' value='"+value._entityType+"'/><span class='aetg_link'><a  href=\"javascript:showBasicResourceInfo('"+value.id+"','"+value._entityType+"')\">基本信息</a><a href='../physicalres/getPhysicalresForOperaAction?currentEntityType="+value._entityType+"&currentEntityId="+value.id+"&areaId="+areaId+"&modelType=view' target='_blank'>详细</a></span></li>";
					}
				})
				
					$("#rd").html(content);
					$("#rd").parent().show();
	
				
			}else{
			
					$("#rd").html("<li>无此查询条件资源</li>");
					$("#rd").parent().show();
			}
		},'json');
	}
	
}

//选择查询出来的资源
function chooseResourceForChildResourceForLogicalres(me){	
		//记录选择的起点资源信息
		var entityName=$(me).next().val();
		var entityId = $(me).next().next().next().val();
		var entityType = $(me).next().next().next().next().val();
		
		if(entityName != ""&& entityType != "" && entityId != ""){
		 	var type = $('#hiddenInputId').val();
		 	if($("#"+type).next().next().next().val() != ""){
			 	delType = delType + $("#"+type).next().next().next().val()+",";
		 	}
		 	if($("#"+type).next().next().next().next().val() != ""){
				delId = delId + $("#"+type).next().next().next().next().val()+",";
		 	}
			$("#"+type).val(entityName);
			$("#"+type).next().next().next().val(entityType);
			$("#"+type).next().next().next().next().val(entityId);
			createType = createType + entityType+",";
			createId = createId + entityId+",";
			//alert(createId+createType);
			entityId = "";
			entityType = "";
			entityName = "";
		}
		$("#divDisplay").hide();
		//获取被添加物理资源类型的父entity类型
		$("#logicalresChosenParentEntityType").val("");
		$("#logicalresChosenParentEntityId").val("");
		//获取被添加物理资源类型的父entity类型
		$("#logicalresAddedResParentEntityType").val("");
		$("#logicalresAddedResParentEntityId").val("");
		//获取被添加物理资源类型
		$("#logicalresAddedResEntityType").val("");
		$("div[class='aetg']").hide();
		

}

