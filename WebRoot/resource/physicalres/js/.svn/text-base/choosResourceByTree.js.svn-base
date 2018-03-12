var user_areaids_string="";
var user_parentareaids_string="";

function isInAreaArray(str){
    var flag = false;
	for(var i=0;i<user_areaids_string.length;i++){
		if(str==user_areaids_string[i]){
			flag=true;
			break;
		}
	}
	return flag;
}

function isInParentAreaArray(str){
	var flag = false;
	for(var i=0;i<user_parentareaids_string.length;i++){
		if(str==user_parentareaids_string[i]){
			flag=true;
			break;
		}
	}
	return flag;

}


$(function(){
	
//获取当前登录人的区域id 和上级区域 id 列表
	$.ajax({
		"url" : "gis_area_resource!getUserParentAreaAndAreaIdsListAction" , 
		"type" : "post" , 
		"async" : false , 
		"success" : function(result){
			var data = eval( "(" + result + ")" );
			user_areaids_string=data[0].split(",");//用户权限区域id String
			user_parentareaids_string=data[1].split(",");//用户权限上级区域id String
			
		}
	});

if(!isInAreaArray($("#areaId").val())){
		$("#rootRadio").remove();
	}


$(".dialog_close").click(function(){
	
	$("#dialog").hide();
	$("#addcontent").empty();
})



//选择按钮，选择隶属资源
			$("#btnChooseParentRes").click(function(){
				var currentEntityName="";
				var rdochoose=$("input[name='rdoChooseResChild']:checked").next();
				if(rdochoose.find("em").size()==0){
					 currentEntityName = rdochoose.html();
				}else{
					 var nameString = rdochoose.html().toString();
					 currentEntityName +=rdochoose.find("em").text();
					 currentEntityName +=nameString.substring(nameString.lastIndexOf(">")+1,nameString.length);
				}
				$("#txtParentRes").val(currentEntityName);
				$("#parentTypeName").text(currentEntityName);
				$("input[name='newParentResEntityType']").val($("input[name='rdoChooseResChild']:checked").next().next().next().val());
				$("input[name='newParentResEntityId']").val($("input[name='rdoChooseResChild']:checked").next().next().val());
				$("#chooseResDiv").hide();
			});
			//取消选择隶属资源
			$("#btnCancelParentRes").click(function(){
				$("#chooseResDiv").hide();
			});
			
			//选择隶属资源提示框显示
			$(".areaButton").click(function(){
				$("#chooseResDiv").show();
			})

})

//加载并显示子级类型信息
	function showChooseResTypeMsg(me) {
		//内容信息已打开，直接隐藏该层，不作任何操作
		if($(me).attr("src") == "image/minus.gif") {
			$(me).attr("src","image/plus.gif");
			if($(me).parent().children().length == 5) {
				$(me).next().next().next().next().slideUp("fast");
			} else {
				$(me).next().next().next().next().next().slideUp("fast");
			}
			return;
		}
	
		var url = "getTypeByEntityAction"; //获取类型
		
		var currentEntityId = null;
		var currentEntityType = null;
		//判断有无出现单选控件
		if($(me).parent().children().length == 5) {
			currentEntityId = $(me).next().next().val();//当前节点的id
			currentEntityType = $(me).next().next().next().val();//当前节点的类型
		} else {
			currentEntityId = $(me).next().next().next().val();//当前节点的id
			currentEntityType = $(me).next().next().next().next().val();//当前节点的类型
		}
		var params = {currentEntityId:currentEntityId,currentEntityType:currentEntityType}
		
		//是否完成加载类型
		var isFinishAddType = false;
		
		if($("#hidTypeFlushForRes").data(currentEntityType + "_" + currentEntityId)) {
			//已通过缓存保存类型信息的情况
			var content = $("#hidTypeFlushForRes").data(currentEntityType + "_" + currentEntityId);
			//加载子级内容
			//判断有无出现单选控件
			if($(me).parent().children().length == 5) {
				$(me).next().next().next().next().get(0).innerHTML = content;
			} else {
				$(me).next().next().next().next().next().get(0).innerHTML = content;
			}
			
			//显示或隐藏子级内容
			if($(me).attr("src") == "image/plus.gif") {
				$(me).attr("src","image/minus.gif");
				//判断有无出现单选控件
				if($(me).parent().children().length == 5) {
					$(me).next().next().next().next().slideDown("fast");
				} else {
					$(me).next().next().next().next().next().slideDown("fast");
				}
			} else {
				$(me).attr("src","image/plus.gif");
				//判断有无出现单选控件
				if($(me).parent().children().length == 5) {
					$(me).next().next().next().next().slideUp("fast");
				} else {
					$(me).next().next().next().next().next().slideUp("fast");
				}
			}
			
			//加载类型完毕，设置为true
			isFinishAddType = true;
		} else {
			//缓存中不存在类型信息的情况，重新获取类型信息，并保存到缓存中
			$.post(url,params,function(data){
				var content = "";
				$.each(data, function(index, obj){
					var imgContent = "";
					if(obj.count == 0) {
						imgContent = "<img src='image/plus.gif' onclick='showChooseResEntityMsg(this);' style='visibility:hidden;' />";
						content += "<li style='display:none'>" 
						+ imgContent 
						+ "<span style='padding-left:5px;cursor:pointer;color:#F3AA36' onclick='chooseTreeType(this)'>" + obj.chineseType + "(...)</span>" 
						+ "<input type='hidden' value=" + obj.chineseType + " />" 
						+ "<input type='hidden' value=" + obj.type + ">" 
						+ "<div style='padding-left:20px;display:none;'></div>" 
						+ "</li>";
					} else {
						imgContent = "<img src='image/plus.gif' onclick='showChooseResEntityMsg(this);' />";
						content += "<li>" 
						+ imgContent 
						+ "<span style='padding-left:5px;cursor:pointer;color:#F3AA36' onclick='chooseTreeType(this)'>" + obj.chineseType + "(...)</span>" 
						+ "<input type='hidden' value=" + obj.chineseType + " />" 
						+ "<input type='hidden' value=" + obj.type + ">" 
						+ "<div style='padding-left:20px;display:none;'></div>" 
						+ "</li>";
					}
					
				});
				content = "<ul>" + content + "</ul>";
				//加载子级内容
				//判断有无出现单选控件
				if($(me).parent().children().length == 5) {
					$(me).next().next().next().next().get(0).innerHTML = content;
				} else {
					$(me).next().next().next().next().next().get(0).innerHTML = content;
				}
				
				//显示或隐藏子级内容
				if($(me).attr("src") == "image/plus.gif") {
					$(me).attr("src","image/minus.gif");
					//判断有无出现单选控件
					if($(me).parent().children().length == 5) {
						$(me).next().next().next().next().slideDown("fast");
					} else {
						$(me).next().next().next().next().next().slideDown("fast");
					}
				} else {
					$(me).attr("src","image/plus.gif");
					//判断有无出现单选控件
					if($(me).parent().children().length == 5) {
						$(me).next().next().next().next().slideUp("fast");
					} else {
						$(me).next().next().next().next().next().slideUp("fast");
					}
				}
				//加载类型完毕，设置为true
				isFinishAddType = true;
				//用缓存保存类型信息
				$("#hidTypeFlushForRes").data(currentEntityType + "_" + currentEntityId, content);
			},'json');
		}
		
		var addCountInterval = setInterval(function(){
			//加载类型已完毕，加载数量
			if(isFinishAddType) {
				var contentDivObj = null;
				if($(me).parent().children().length == 5) {
					contentDivObj = $(me).next().next().next().next();
				} else {
					contentDivObj = $(me).next().next().next().next().next();
				}
				//获取该节点下各类型的节点数量
				contentDivObj.children().children().each(function(index){
					//获取子类型下的li节点
					var liObj = $(this);
				
					//获取父级的id和type
					var parentEntityType = currentEntityType;
					var parentEntityId = currentEntityId;
					//获取需要查找的类型
					var searchType = $(this).children().eq(3).val();
					var params = {parentEntityType:parentEntityType,parentEntityId:parentEntityId,searchType:searchType}
					
					if($("#hidTypeFlushForRes").data(parentEntityType + "_" + parentEntityId + "_addContentCount" + index)) {
						//获取增加数值后的缓存内容
						var operatedObj = liObj.children().eq(1);
						var addCountContent = $("#hidTypeFlushForRes").data(parentEntityType + "_" + parentEntityId + "_addContentCount" + index);
						//提取对应类型的数量
						var count = addCountContent.charAt(addCountContent.lastIndexOf(")") - 1);
						if(count == "0") {
							//数量为0时隐藏小加号
							liObj.children().eq(0).css("visibility","hidden");
							liObj.css("display","none");
						}
						operatedObj.html(addCountContent);
					} else {
						$.post("getChildEntityCountByTypeAction",params,function(data){
							//加载对应类型的数量
							var operatedObj = liObj.children().eq(1);
							var countIndex = (operatedObj.html()).indexOf("(...)");
							var firstContent = operatedObj.html();
							
							if(data == "0") {
								//数量为0时隐藏小加号
								liObj.children().eq(0).css("visibility","hidden");
								liObj.css("display","none");
							}else{
								//数量为0时隐藏小加号
								liObj.children().eq(0).css("visibility","");
								liObj.css("display","block");
							}
							operatedObj.html(firstContent.substring(0, countIndex) + "("+data+")");
							//用缓存保存已经加载了数量的类型，例如，站址(10)
							$("#hidTypeFlushForRes").data(parentEntityType + "_" + parentEntityId + "_addContentCount" + index,firstContent.substring(0, countIndex) + "("+data+")");
						},'json');
					}
				});
				
				isFinishAddType = false;
				//跳出判断是否加载类型完毕的循环
				clearInterval(addCountInterval);
			}
		},10);
	}
	
	//加载并显示子级entity信息
	function showChooseResEntityMsg(me) {
		//若已加载内容，只隐藏该层，不作任何操作
		if($(me).attr("src") == "image/minus.gif") {
			$(me).attr("src", "image/plus.gif");
			//判断有无出现单选控件
			if($(me).parent().children().length == 5) {
				$(me).next().next().next().next().slideUp("fast");
			} else {
				$(me).next().next().next().next().next().slideUp("fast");
			}
			return;
		}
	
		var url = "getChildEntityByTypeAction"; //获取子级entity
		//获取父级节点的类型
		var parentEntityType = $(me).parent().parent().parent().prev().val();
		//获取父级节点的id
		var parentEntityId = $(me).parent().parent().parent().prev().prev().val();
		//被查找的类型(即当前节点的类型)
		var searchType = $(me).next().next().next().val();
		
		var params = {parentEntityType:parentEntityType,parentEntityId:parentEntityId,searchType:searchType}
		
		if($("#hidEntityFlushForRes").data(parentEntityType + "_" + parentEntityId + "_" + searchType, content)) {
			var content = $("#hidEntityFlushForRes").data(parentEntityType + "_" + parentEntityId + "_" + searchType, content);
			//加载子级内容
			//判断有无出现单选控件
			if($(me).parent().children().length == 5) {
				$(me).next().next().next().next().get(0).innerHTML = content;
			} else {
				//除了区域类型，多了单选按钮，所以要增加一个next
				$(me).next().next().next().next().next().get(0).innerHTML = content;
			}
			//显示或隐藏子级内容
			if($(me).attr("src") == "image/plus.gif") {
				$(me).attr("src","image/minus.gif");
				//判断有无出现单选控件
				if($(me).parent().children().length == 5) {
					$(me).next().next().next().next().slideDown("fast");
				} else {
					$(me).next().next().next().next().next().slideDown("fast");
				}
			} else {
				$(me).attr("src","image/plus.gif");
				//判断有无出现单选控件
				if($(me).parent().children().length == 5) {
					$(me).next().next().next().next().slideUp("fast");
				} else {
					$(me).next().next().next().next().next().slideUp("fast");
				}
			}
		} else {
			$.post(url,params,function(data){
				var content = "";
				$.each(data, function(index, obj){
					var entityName = "";
					var entityType = $(me).next().next().next().val();
					if(obj.name != null) {
						entityName = obj.name;
					} else {
						entityName = obj.label;
					}
					
					var imgContent = "";
					var radioContent = "";
					//获取所选择的上级资源类型(因为当时的值为组合值，例如Station_parent，所以要进行substring操作，获取entity类型)
					
					//当前节点拥有子类型，生成小加号
					if(obj.hasType == "has") {
						imgContent = "<img src='image/plus.gif' onclick='showChooseResTypeMsg(this);' />";
					} else {
						imgContent = "<img src='image/plus.gif' onclick='showChooseResTypeMsg(this);' style='visibility:hidden;' />";
					}
					
					if(searchType=="Sys_Area"){
						if(!isInParentAreaArray(obj.id+"")&&!isInAreaArray(obj.id+"")){
							imgContent="";
						}
						if(!isInAreaArray(obj.id+"")){
							radioContent="";
						
						}else{
							//根据关联的父类生成单选按钮
							var aId= $("#chosenEntityId").val();
							var parentTypeGroup = $("#hidParentTypeGroup").val();
							if(parentTypeGroup.indexOf(",") == -1 ) {
								//只存在一个父类型
								if(searchType=='Sys_Area'&&searchType==parentTypeGroup){
									if(obj.id != aId ) {
										radioContent = "<input class='input_radio' name='rdoChooseResChild' type='radio' />";
									}
								}else{
									if(searchType == parentTypeGroup ) {
										radioContent = "<input class='input_radio' name='rdoChooseResChild' type='radio' />";
									}
								}
								
							} else if(parentTypeGroup.indexOf(",") > -1) {
								if(parentTypeGroup.indexOf("BaseStation") >= 0){
									if(searchType.indexOf("BaseStation")>=0){
											radioContent = "<input class='input_radio' name='rdoChooseResChild' type='radio' />";
									}
								}else{
									//存在多个父类型
									var parentTypeArrs = parentTypeGroup.split(",");
									if(parentTypeArrs != null && parentTypeArrs.length > 1) {
										for(var i = 0; i < parentTypeArrs.length; i++) {
											if(searchType=='Sys_Area'&&searchType==parentTypeGroup){
												if(obj.id != aId ) {
													radioContent = "<input class='input_radio' name='rdoChooseResChild' type='radio' />";
													break;
												}
											}else{
												if(searchType == parentTypeArrs[i]) {
													//查找类型为关联的父类型组中的其中一个时，生成单选按钮
													radioContent = "<input class='input_radio' name='rdoChooseResChild' type='radio' />";
													break;
												}
											}
											
										}
									}
								}
								
							}
						}
					}else{
						//根据关联的父类生成单选按钮
						var aId= $("#chosenEntityId").val();
						var parentTypeGroup = $("#hidParentTypeGroup").val();
						if(parentTypeGroup.indexOf(",") == -1 ) {
							//只存在一个父类型
							if(searchType=='Sys_Area'&&searchType==parentTypeGroup){
								if(obj.id != aId ) {
									radioContent = "<input class='input_radio' name='rdoChooseResChild' type='radio' />";
								}
							}else{
								if(searchType == parentTypeGroup ) {
									radioContent = "<input class='input_radio' name='rdoChooseResChild' type='radio' />";
								}
							}
							
						} else if(parentTypeGroup.indexOf(",") > -1) {
							if(parentTypeGroup.indexOf("BaseStation") >= 0){
								if(searchType.indexOf("BaseStation")>=0){
										radioContent = "<input class='input_radio' name='rdoChooseResChild' type='radio' />";
								}
							}else{
								//存在多个父类型
								var parentTypeArrs = parentTypeGroup.split(",");
								if(parentTypeArrs != null && parentTypeArrs.length > 1) {
									for(var i = 0; i < parentTypeArrs.length; i++) {
										if(searchType=='Sys_Area'&&searchType==parentTypeGroup){
											if(obj.id != aId ) {
												radioContent = "<input class='input_radio' name='rdoChooseResChild' type='radio' />";
												break;
											}
										}else{
											if(searchType == parentTypeArrs[i]) {
												//查找类型为关联的父类型组中的其中一个时，生成单选按钮
												radioContent = "<input class='input_radio' name='rdoChooseResChild' type='radio' />";
												break;
											}
										}
										
									}
								}
							}
							
						}
						if(parentEntityType=="Sys_Area"){
							if(!isInAreaArray(parentEntityId+"")){
								imgContent = "";
								radioContent = "";
							}
						}
					}
				
					
					
					
					content += "<li>" 
						+ imgContent 
						+ radioContent
						+ "<span onclick='chooseTreeEntity(this);' style='padding-left:5px;cursor:pointer;'>" + entityName + "</span>" 
						+ "<input type='hidden' value=" + obj.id + " />" 
						+ "<input type='hidden' value=" + entityType + ">" 
						+ "<div style='padding-left:15px;display:none;'></div>" 
						+ "</li>";
				});
				content = "<ul>" + content + "</ul>";
				//加载子级内容
				//判断有无出现单选控件
				if($(me).parent().children().length == 5) {
					$(me).next().next().next().next().get(0).innerHTML = content;
				} else {
					//除了区域类型，多了单选按钮，所以要增加一个next
					$(me).next().next().next().next().next().get(0).innerHTML = content;
				}
				//显示或隐藏子级内容
				if($(me).attr("src") == "image/plus.gif") {
					$(me).attr("src","image/minus.gif");
					//判断有无出现单选控件
					if($(me).parent().children().length == 5) {
						$(me).next().next().next().next().slideDown("fast");
					} else {
						$(me).next().next().next().next().next().slideDown("fast");
					}
				} else {
					$(me).attr("src","image/plus.gif");
					//判断有无出现单选控件
					if($(me).parent().children().length == 5) {
						$(me).next().next().next().next().slideUp("fast");
					} else {
						$(me).next().next().next().next().next().slideUp("fast");
					}
				}
				
				//通过缓存保存节点信息
				$("#hidEntityFlushForRes").data(parentEntityType + "_" + parentEntityId + "_" + searchType, content);
			},'json');
		}
	}


