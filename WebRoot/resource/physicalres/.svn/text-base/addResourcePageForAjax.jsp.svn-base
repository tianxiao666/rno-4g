<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style type="text/css">
textarea{width:116px;}
label.error{color:red}
</style>

<script type="text/javascript">
function submitAction(){
	$("#operaForm").attr("action","addResourceNewAction");
	$("#operaForm").submit();
}
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

if(!isInAreaArray('${areaId}')){
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
				$("input[name='addedResParentEntityType']").val($("input[name='rdoChooseResChild']:checked").next().next().next().val());
				$("input[name='addedResParentEntityId']").val($("input[name='rdoChooseResChild']:checked").next().next().val());
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
	
</script>
<input type="hidden" id="hidEntityFlushForRes"/>
<input type="hidden" id="hidTypeFlushForRes"/>
<input type="hidden" name="currentAreaId" value="${areaId}"/>
<input type="hidden" name="addedResEntityType" value="${sType}">
<input type="hidden" name="addedResParentEntityType">
<input type="hidden" name="addedResParentEntityId">
<div class="dialog" style="margin-left:-350px; margin-top:100px;">
    <div class="dialog_header">
        <div class="dialog_title">添加：${chineseName }</div>
        <div class="dialog_tool">
           <div class="dialog_tool_close dialog_close"></div>
        </div>
    </div>
    <div class="dialog_content" style="padding:4px;">
    	<h4>基本属性</h4>
					<table class="alert_table">
							<s:set id="a" value="1" />	
		                   <s:iterator value="addedResMap" id="map" status="st">
		                   
			                	<s:if test="!(key == 'id' || key == '_entityId' || key == '_entityType')">
			                		<s:if test="#a % 3 != 0 && #a % 3 == 1">
				                	<tr>
				                	</s:if>
				                		<td class="left_td">
						                	<%-- 获取中文字段名，拿不到拿英文字段名 --%>
						                	<s:if test="currentEntityChineseMap.get(#map.key) == null">
						                		${key}
						                	</s:if>
						                	<s:else>
						                		<s:property value="currentEntityChineseMap.get(#map.key)"/>
						                	</s:else>
					                	</td>
					                    <td>
					                    	<%-- 验证是整型的情况 --%>
					                    	<s:if test="attrTypeMap.get(#map.key).indexOf('Integer') > -1">
					                    		<%-- 判断属性的非空情况 --%>
						                    	<s:if test="'true'.equals(nullableMap.get(#map.key))">
						                    		<s:if test="dropdownListMap.get(#map.key) != null">
						                    			<select class="input_select" name="${param.sType}#${key}">
						                    				<option value="" selected="selected" >请选择</option>
							                    			<s:iterator id="dropdownVal" value="dropdownListMap.get(#map.key)">
							                    				<s:if test='#dropdownVal=="是"'>
								                    						<option  value="1" >${dropdownVal}</option>          
								                    			</s:if>
								                    			<s:elseif test='#dropdownVal=="否"'>
								                    						<option  value="0" >${dropdownVal}</option>           
								                    			</s:elseif>
								                    			<s:else>
								                    						<option value="${dropdownVal}" >${dropdownVal}</option>
								                    			</s:else>
							                    			</s:iterator>
						                    			</select>
						                    		</s:if>
						                    		<s:else>
						                    			<input title='请填写：整数，如“12”' validateDigit="#${param.sType}_${key}" id="${param.sType}_${key}" name="${param.sType}#${key}" type="text" />
						                    		</s:else>
						                    	</s:if>
						                    	<s:else>
						                    		<s:if test="dropdownListMap.get(#map.key) != null">
						                    			<select class="input_select required" name="${param.sType}#${key}">
						                    				<option value="" selected="selected" onclick="integerOptionOnclick(this);">请选择</option>
							                    			<s:iterator id="dropdownVal" value="dropdownListMap.get(#map.key)">
							                    				<s:if test='#dropdownVal=="是"'>
								                    						<option  value="1" >${dropdownVal}</option>          
								                    			</s:if>
								                    			<s:elseif test='#dropdownVal=="否"'>
								                    						<option  value="0" >${dropdownVal}</option>           
								                    			</s:elseif>
								                    			<s:else>
								                    						<option value="${dropdownVal}" >${dropdownVal}</option>
								                    			</s:else>
							                    			</s:iterator>
						                    			</select>
						                    		</s:if>
						                    		<s:else>
						                    			<input title='请填写：整数，如“12”' class="required" validateDigit="#${param.sType}_${key}" id="${param.sType}_${key}" name="${param.sType}#${key}" type="text" />
						                    		</s:else>
						                    		<i style="color:red">*</i>
						                    	</s:else>
						                    	
					                    	</s:if>
					                    	<%-- 验证浮点型的情况 --%>
					                    	<s:elseif test="attrTypeMap.get(#map.key).indexOf('Double') > -1  || attrTypeMap.get(#map.key).indexOf('Float') > -1">
					                    		<%-- 判断属性的非空情况 --%>
						                    	<s:if test="'true'.equals(nullableMap.get(#map.key))">
						                    		<s:if test="dropdownListMap.get(#map.key) != null">
						                    			<select class="input_select" name="${param.sType}#${key}">
						                    				<option value="" selected="selected" >请选择</option>
							                    			<s:iterator id="dropdownVal" value="dropdownListMap.get(#map.key)">
							                    				<option value="${dropdownVal}" >${dropdownVal}</option>
							                    			</s:iterator>
						                    			</select>
						                    		</s:if>
						                    		<s:else>
						                    			<input title='请填写：数值，如“1232.23”，“12.03”' class="number" name="${param.sType}#${key}" type="text" />
						                    		</s:else>
						                    	</s:if>
						                    	<s:else>
						                    		<s:if test="dropdownListMap.get(#map.key) != null">
						                    			<select class="input_select required" name="${param.sType}#${key}">
						                    				<option value="" selected="selected" >请选择</option>
							                    			<s:iterator id="dropdownVal" value="dropdownListMap.get(#map.key)">
							                    				<option value="${dropdownVal}" >${dropdownVal}</option>
							                    			</s:iterator>
						                    			</select>
								                    	<input title='请填写：数值，如“1232.23”，“12.03”' class="number"  name="${param.sType}#${key}" type="text" style="margin-left:-139px;position:relative;z-index=2; width:113px;top:1px;border-bottom:none;" value=""/>							                    										                    				
						                    		</s:if>
						                    		<s:else>
						                    			<input  title='请填写：数值，如“1232.23”，“12.03”' class="number required" name="${param.sType}#${key}" type="text" />
						                    		</s:else>
						                    		<i style='color:red'>*</i>
						                    	</s:else>
						                    	
					                    	</s:elseif>
					                    	<%-- 验证日期类型的情况 --%>
					                    	<s:elseif test="attrTypeMap.get(#map.key).indexOf('Date') > -1">
					                    		<%-- 判断属性的非空情况 --%>
						                    	<s:if test="'true'.equals(nullableMap.get(#map.key))">
						                    		<input title='请填写：日期时间，如“2012-10-09”，“2012-10-09 11:02:30”' readonly="readonly" name="${param.sType}#${key}" type="text" 
					                    			class="input_text" id="time<s:property value='#st.index' />" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
						                    	</s:if>
						                    	<s:else>
						                    		<input title='请填写：日期时间，如“2012-10-09”，“2012-10-09 11:02:30”' readonly="readonly" name="${param.sType}#${key}" type="text" 
					                    			class="input_text required" id="time<s:property value='#st.index' />" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
						                    		<i class="redStar">*</i>
						                    	</s:else>
					                    		<%--  <input type="button" class="input_button" value="选择时间" 
														onclick=fPopCalendar(event,document.getElementById('time<s:property value="#st.index" />'),document.getElementById('time<s:property value="#st.index" />'),true) />--%>
					                    	</s:elseif>
					                    	<%-- 验证其他类型的情况 --%>
					                    	<s:else>
					                    		<%-- 判断属性的非空情况 --%>
						                    	<s:if test="'true'.equals(nullableMap.get(#map.key))">
						                    		<s:if test="dropdownListMap.get(#map.key) != null">
						                    			<select class="input_select" name="${param.sType}#${key}">
						                    				<option value="" selected="selected">请选择</option>
							                    			<s:iterator id="dropdownVal" value="dropdownListMap.get(#map.key)">
							                    				<option value="${dropdownVal}">${dropdownVal}</option>
							                    			</s:iterator>
						                    			</select>
						                    		</s:if>
						                    		<s:else>
						                    			<s:if test="attrLengthMap.get(#map.key)>=100">
							                    				<textarea rows="5" title='请填写：字符串' name="${param.sType}#${key}"></textarea>
							                    			</s:if>
							                    			<s:else>
							                    				<input title='请填写：字符串' name="${param.sType}#${key}" type="text"  />							                    			
							                    			</s:else>
						                    		</s:else>
						                    	</s:if>
						                    	<s:else>
						                    		<s:if test="dropdownListMap.get(#map.key) != null">
						                    			<select class="input_select required" name="${param.sType}#${key}">
						                    				<option value="" selected="selected" >请选择</option>
							                    			<s:iterator id="dropdownVal" value="dropdownListMap.get(#map.key)">
							                    				<option value="${dropdownVal}" >${dropdownVal}</option>
							                    			</s:iterator>
						                    			</select>
						                    		</s:if>
						                    		<s:else>
						                    			<s:if test="attrLengthMap.get(#map.key)>=100">
							                    				<textarea rows="5" title='请填写：字符串' class="required" name="${param.sType}#${key}"></textarea>
							                    		</s:if>
							                    		<s:else>
							                    				<input title='请填写：字符串' class="required" name="${param.sType}#${key}" type="text" />							                    			
							                    		</s:else>
						                    		</s:else>
						                    		<i style="color:red">*</i>
						                    	</s:else>
					                    	</s:else>
					                    </td>
				                <s:if test="#a % 3 == 0">
				                	<tr>
				                </s:if>
				                <s:set id="a" value="#a + 1" />
				                </s:if>
		               		</s:iterator>
		             </table>
		 <h4>隶属：
      	<span style="margin-left:34px; *margin-left:40px;">
          	<input type="text"  readonly id="txtParentRes"/><a  class="areaButton"></a>
          </span>
        </h4> 
        <div class="dialog_but">
            <button type="button" class="aui_state_highlight confirm" onclick="submitAction();">保存</button>
            <button type="button" class="aui_state_highlight dialog_close" >取消</button>
        </div>
    </div>
</div>			
 <div id="chooseResDiv" style="display:none;border:#ccc 1px solid; border-radius:5px; position:absolute; left:491px;top:122px; background:#f1f1f1; z-index:100">
    	<input type="hidden" id="hidParentTypeGroup" value="${parentEntityTypeGrp}" />

    	<%-- 隶属内容选择层 --%>
    	<s:if test="#request.areaId!=null&&#request.areaName!=null">
	   		<div id="chooseTree" style="padding:0 8px">
	   			<ul id="chooseResUl" style="width:250px;height:300px;overflow:auto;line-height:16px;">
		        	<li>
			        	<img src="image/plus.gif" onclick="showChooseResTypeMsg(this);" />
			        	<s:if test="parentEntityTypeGrp.indexOf('Sys_Area') > -1">
			        		<input class="input_radio" type="radio" name="rdoChooseResChild" id="rootRadio">
			        	</s:if>
			        	<span onclick='chooseTreeEntity(this);' id='treeRootName' style="cursor:pointer;" class="entityClass">${request.areaName}</span>
			        	<%-- 选进行区域id的hardcode --%>
			        	<input type="hidden" value="${request.areaId}" />
			        	<input type="hidden" value="Sys_Area" />
			        	<div style="padding-left:10px;display:none;"></div>
			        </li>
	             </ul>
	   			<div style="text-align:center">
		   			<input id="btnChooseParentRes" type="button" value="选择"  />&nbsp;
		   			<input id="btnCancelParentRes" type="button" value="取消" />
	   			</div>
	   		</div>
   		 </s:if>
   		 <s:else>
	   		 <div>
	   		 	<ul  style="width:250px;height:300px;overflow:auto;line-height:16px;">
	   		 		<s:iterator value="parentMapList" id="map" status="st">
	   		 			<li>
	   		 				<input class='input_radio' name='rdoChooseResChild' type='radio' />
							<span  style='padding-left:5px;cursor:pointer;'>${map.chineseName}:${map.name}</span> 
						    <input type='hidden' value="${map.id}" />
						    <input type='hidden' value="${map._entityType}">
			        	</li>
	   		 		</s:iterator>
	             </ul>
	             <div style="text-align:center">
		   			<input id="btnChooseParentRes" type="button" value="选择"  />&nbsp;
		   			<input id="btnCancelParentRes" type="button" value="取消" />
	   			</div>
	   		 </div>
   		 </s:else>
   		 
   </div>
   