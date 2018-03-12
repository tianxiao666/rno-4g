<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>资源设施维护-浏览</title>
	<link rel="stylesheet" type="text/css" href="css/base.css" />
	<link rel="stylesheet" type="text/css" href="css/public.css" />
	<link rel="stylesheet" href="css/jquery.treeview.css" />
	<link rel="stylesheet" type="text/css" href="css/resources.css" />
	<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
	<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js"></script>
	<script type="text/javascript" src="../../jslib/date/wdatePicker.js"></script>
	<script type="text/javascript" src="../../jslib/jquery/jquery.validate.js"></script>
	<script type="text/javascript" src="../js/validate.ex.js"></script>
	<script type="text/javascript" src="../js/jquery.highlighter-1.0.0.min.js"></script>
<style>
.main-table1 .menuTd{width:20%;}
.main-table1 .highLighte{ background:#FFF; width:30%;}
.leftSide{width:270px; margin:0px 5px; float:left;}
.leftSide-tree{ border:1px solid #B5B8C8;padding: 5px; min-height:722px;}
.rightSide{ width:auto; overflow:hidden; border:1px solid #B5B8C8;}
.rightSide-top{ background:url(image/white-top-bottom.gif) repeat-x; line-height:33px; padding-left:5px;}
#chooseResDiv li{white-space: nowrap;}
/*非空属性提示*/
.redStar{color:red}
</style>
	<script type="text/javascript">
		$(document).ready(function(){
			/* 点击加减按钮收起 */
			$(".main-table1 .hide-show-img").click(function(){
				if($(this).attr("src") == "image/ico_show.gif"){
					$(this).parent().parent().parent().find("tr").not($(".main-table1-tr")).hide();
					$(this).attr("src","image/ico_hide.gif");
				}else{
					$(this).parent().parent().parent().find("tr").not($(".main-table1-tr")).show();
					$(this).attr("src","image/ico_show.gif");
				}
			});
			/*$("#tree").treeview({
				collapsed: true,
				animated: "fast",
				control:"#sidetreecontrol",
			});*/
			//当前资源txt，获取当前entity的name或label值
			/*if("${currentEntityMap.name}" != "") {
				$("#txtCurrentRes").val("${currentEntityMap.name}");
			} else {
				$("#txtCurrentRes").val("${currentEntityMap.label}");
			}*/
			
			//爷节点资源txt，获取爷节点entity的name或label值
			if("${parentEntityMap.name}" != "") {
				$("#txtParentRes").val("${parentEntityMap.name}");
			} else {
				$("#txtParentRes").val("${parentEntityMap.label}");
			}
			//先把原来的隶属资源的id和type保存起来
			$("#oldParentResEntityId").val("${parentEntityMap.id}");
			$("#oldParentResEntityType").val("${parentEntityMap.type}");
			
			//隶属资源按钮(弹出或关闭隶属资源层)
			$("#btnParentRes").click(function(){
				//拿不出爷级资源，说明没有可选择的隶属资源
				/*if("${grandpaEntityMap}" == "") {
					alert("没有可选择的隶属资源");
					return false;
				}*/
				$("#chooseResDiv").toggle();
			});
			
			//取消按钮，隶属资源层关闭
			$("#btnCancelParentRes").click(function(){
				$("#chooseResDiv").hide();
			});
			
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
				//保存新选择的隶属资源的id和type
				$("#newParentResEntityId").val($("input[name='rdoChooseResChild']:checked").next().next().val());
				$("#newParentResEntityType").val($("input[name='rdoChooseResChild']:checked").next().next().next().val());
				$("#chooseResDiv").hide();
			});
			
			//管理模式与浏览模式控制
			$("#modelType").change(function(){
				if($(this).val() == "view") {
					$("input[type='button']").hide();
					$("input[type='submit']").hide();
					$("input[type='text']").attr("readonly",true);
				} else {
					$("input[type='button']").show();
					$("input[type='submit']").show();
					$("input[type='text']").attr("readonly",false);
					//日期类型，继续需要readonly
					$("input[type='button']").each(function(){
						if($(this).val() == "选择时间") {
							$(this).prev().attr("readonly",true);
						}
					})
				}
			});
			
			$(":button,:submit").mousedown(function(){
				$(this).addClass("input_button_down");
			});
			$(":button,:submit").mouseup(function(){
				$(this).removeClass("input_button_down");
			});
			$(":button,:submit").mouseout(function(){
				$(this).removeClass("input_button_down");
			});
			
			//表单jQuery验证
			$("#operaForm").validate({
				submitHandler: function(form) { 
					var addedResEntityType = '${addedResEntityType}';
					var areaId = '${areaId}';
					if(addedResEntityType=="Station"||addedResEntityType.indexOf("BaseStation")>=0){
						var cName=$("input[name='"+addedResEntityType+"#name']").val();
						var params = {addedResEntityType:addedResEntityType,areaId:areaId,cName:cName};
						$.post("hasStationResourceRecordAction",params,function(data){
							if(data!="0"){
								alert("已存在同名资源，保存失败");
								return false;
							}else{
								form.submit();
							}
						},'json');
					}else{
						form.submit();
					}
					
					
				}
			});
			
			//搜索栏"请输入"提示控制
			$("#txtSearch").focus(function(){
				//已更改为黑色字，不作清除内容操作
				if($(this).css("color") == "#000" || $(this).css("color") == "rgb(0, 0, 0)") {
					return false;
				}
				$(this).val("");
				//focus后，字体更改为黑色
				$(this).css("color","#000");
			});
			
			//搜索(暂定为一级搜索)
			$("#btnSearch").click(function(){
				//获取需要进行搜索的关键字
				var kewWords = $("#txtSearch").val();
				//没有任何搜索内容，或者依然为灰色的请输入时，进行搜索内容输入提示
				if($.trim(kewWords) == "" || ($.trim(kewWords)=="请输入" && ($("#txtSearch").css("color") == "#999" || $("#txtSearch").css("color") == "rgb(153, 153, 153)"))) {
					alert("请输入搜索内容!");
					return false;
				}
				var addedResEntityType = $("#addedResEntityType").val();
				var addedResParentEntityType = $("#addedResParentEntityType").val();
				var addedResParentEntityId = $("#addedResParentEntityId").val();
				
				if(addedResEntityType == "" || addedResParentEntityType == "" || addedResParentEntityId == "") {
					alert("请选择要进行搜索的类型起始节点!");
					return false;
				}
				//获取类型前的小加号图片
				var typeImg = $("#chooseTree span.spanChosenClass").parent().children().eq(0);
				if(typeImg.css("visibility") == "hidden") {
					//不存在小加号图片，即该类型下并无entity内容，则不在搜索处理
					return false;
				}
				//获取搜索的类型起始节点下的内容div
				var entityContentDiv = $("#chooseTree span.spanChosenClass").parent().children().eq(4);
				
				if(entityContentDiv.children().length == 0) {
					//不存在内容的情况，需要进行加载
					//showChildEntityMsg(typeImg);
					showChooseResEntityMsg(typeImg);
				}
				
				//令子级entity信息加载完成后，才进行关键字高亮设置
				var searchInterval = setInterval(function(){
					if(entityContentDiv.children().length > 0) {
						//展开子级entity信息后，找到与搜索匹配项的，进行关键字高亮设置
						$("#chooseTree span").css("background", "");
						$("em").css("color","");
						var flag = false;
						var searchIndex=0;//标记找到的第一个值
						var topValue=0;//找到的第一个值元素的位置top值
						entityContentDiv.children().children().each(function(index){
							//找到entity的span，进行高亮设置
							
							//有无radio判断
							
							if($(this).find("input:[type='radio']").size()>0){
								//alert($(this).children().find("input:[type='radio']").size());
								var choose =$(this).children().eq(2);
								
							}else{
								var choose = $(this).children().eq(1);
								
							}
							if(choose.text().toString().indexOf($.trim(kewWords))>=0){
								flag=true;
							}
							if(choose.text().toString()==$.trim(kewWords)){
								//alert("ok");
								searchIndex++;
								if(searchIndex==1){//标记第一个找到的值
									if($("#chooseResUl").scrollTop()!=0){//滚动条回到顶部
										$("#chooseResUl").scrollTop(0);
									}
									topValue = $(this).position().top;
								}
								choose.css("background","#FFFF88");
							}else if(choose.text().toString()!=$.trim(kewWords)&&choose.text().toString().indexOf($.trim(kewWords))>=0){
								searchIndex++;
								if(searchIndex==1){
									if($("#chooseResUl").scrollTop()!=0){
										$("#chooseResUl").scrollTop(0);
									}
									topValue = $(this).position().top;
								}
								
								choose.highlight($.trim(kewWords), {needUnhighlight: true});
							}
							
						});
						if(flag==false){
							alert("没有匹配的数据.\r\n请修改查询条件 或 修改查询的起点后重新查询.");
						}else{
							topValue =topValue-50;//hardcode 使找到的内容相对居中聚焦
							setTimeout(function(){$("#chooseResUl").scrollTop(topValue);},100);

						}
						
						
						//小加号图片变成小减号
						typeImg.attr("src","image/minus.gif");
						//展现entity内容div
						entityContentDiv.show();
						
						clearInterval(searchInterval);
					}
				},10);
			});
		});
			
		//加载并显示子级类型信息
		function showChildTypeMsg(me) {
			var url = null;
			var params = null;
			
			url = "getTypeByEntityAction"; //获取类型
			var currentEntityId = $(me).next().next().val();//当前节点的id
			var currentEntityType = $(me).next().next().next().val();//当前节点的类型
			var params = {currentEntityId:currentEntityId,currentEntityType:currentEntityType}
			
			$.post(url,params,function(data){
				var content = "";
				$.each(data, function(index, obj){
					var imgContent = "";
					if(obj.count == 0) {
						imgContent = "<img src='image/plus.gif' onclick='showChildEntityMsg(this);' style='visibility:hidden;' />";
						content += "<li style='display:none'>" 
						+ imgContent 
						+ "<span onclick='chooseType(this);' style='padding-left:5px;cursor:pointer;'>" + obj.type + "(" + obj.count + ")</span>" 
						+ "<input type='hidden' value=" + obj.count + " />" 
						+ "<input type='hidden' value=" + obj.type + ">" 
						+ "<div style='padding-left:15px;display:none;'></div>" 
						+ "</li>";
					} else {
						imgContent = "<img src='image/plus.gif' onclick='showChildEntityMsg(this);' />";
						content += "<li>" 
						+ imgContent 
						+ "<span onclick='chooseType(this);' style='padding-left:5px;cursor:pointer;'>" + obj.type + "(" + obj.count + ")</span>" 
						+ "<input type='hidden' value=" + obj.count + " />" 
						+ "<input type='hidden' value=" + obj.type + ">" 
						+ "<div style='padding-left:15px;display:none;'></div>" 
						+ "</li>";
					}
					
				});
				content = "<ul>" + content + "</ul>";
				//加载子级内容
				$(me).next().next().next().next().get(0).innerHTML = content;
				//显示或隐藏子级内容
				if($(me).attr("src") == "image/plus.gif") {
					$(me).attr("src","image/minus.gif");
					$(me).next().next().next().next().slideDown("fast");
				} else {
					$(me).attr("src","image/plus.gif");
					$(me).next().next().next().next().slideUp("fast");
				}
			},'json');
		}
		
		//加载并显示子级entity信息
		function showChildEntityMsg(me) {
			var url = null;
			var params = null;
				
			url = "getChildEntityByTypeAction"; //获取子级entity
			//获取父级节点的类型
			var parentEntityType = $(me).parent().parent().parent().prev().val();
			var parentEntityId = $(me).parent().parent().parent().prev().prev().val();
			//被查找的类型(即当前节点的类型)
			var searchType = $(me).next().next().next().val();
			params = {parentEntityType:parentEntityType,parentEntityId:parentEntityId,searchType:searchType}
			
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
					if(obj.hasType == "has") {
						imgContent = "<img src='image/plus.gif' onclick='showChildTypeMsg(this);' />";
					} else {
						imgContent = "<img src='image/plus.gif' onclick='showChildTypeMsg(this);' style='visibility:hidden;' />";
					}
					
					content += "<li>" 
						+ imgContent 
						+ "<span onclick='chooseEntity(this);' style='padding-left:5px;cursor:pointer;'>" + entityName + "</span>" 
						+ "<input type='hidden' value=" + obj.id + " />" 
						+ "<input type='hidden' value=" + entityType + ">" 
						+ "<div style='padding-left:15px;display:none;'></div>" 
						+ "</li>";
				});
				content = "<ul>" + content + "</ul>";
				//加载子级内容
				$(me).next().next().next().next().get(0).innerHTML = content;
				//显示或隐藏子级内容
				if($(me).attr("src") == "image/plus.gif") {
					$(me).attr("src","image/minus.gif");
					$(me).next().next().next().next().slideDown("fast");
				} else {
					$(me).attr("src","image/plus.gif");
					$(me).next().next().next().next().slideUp("fast");
				}
			},'json');
		}
		
		//点击所选择的类型
		function chooseType(me) {
			$("#treeDiv span").css("color", "#000");
			$(me).css("color", "#F00");//选中的实体为红色
		}
		//选择树点击所选择类型
		function chooseTreeType(me){
			
			$("#chooseTree span").css("background", "");
			$("#chooseTree span").attr("class", "");
			$(me).css("background", "#0099FF");//选中的实体颜色
			$(me).attr("class","spanChosenClass");//选中的实体增加一个选中的class属性
			//获取当前选中的类型信息
			var addedResEntityType = $(me).next().next().val();
			var addedResParentEntityType = $(me).parent().parent().parent().prev().val();
			var addedResParentEntityId = $(me).parent().parent().parent().prev().prev().val();
			
			//获取被添加物理资源类型的父entity类型
			$("#addedResParentEntityType").val(addedResParentEntityType);
			$("#addedResParentEntityId").val(addedResParentEntityId);
			//获取被添加物理资源类型
			$("#addedResEntityType").val(addedResEntityType);
		}
		//点击所选择的实体
		function chooseEntity(me) {
			$("#treeDiv span").css("color", "#000");
			$(me).css("color", "#F00");//选中的实体为红色
		}
		
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
					
					//不是所选择的类型的entity，不生成单选按钮
					/*if(searchType != rdoChosenType) {
						if(obj.hasType == "has") {
							imgContent = "<img src='image/plus.gif' onclick='showChildTypeMsg(this);' />";
						} else {
							imgContent = "<img src='image/plus.gif' onclick='showChildTypeMsg(this);' style='visibility:hidden;' />";
						}
					} else {
						//所选择的entity类型为当前类型，生成单选按钮
						radioContent = "<input class='input_radio' name='rdoChoose' type='radio' />";
					}*/
					
					//当前节点拥有子类型，生成小加号
					if(obj.hasType == "has") {
						imgContent = "<img src='image/plus.gif' onclick='showChooseResTypeMsg(this);' />";
					} else {
						imgContent = "<img src='image/plus.gif' onclick='showChooseResTypeMsg(this);' style='visibility:hidden;' />";
					}
					
					//左边树选择节点的父类型
					/*var targetType = $("#treeDiv span.chosenClass").parent().parent().parent().parent().parent().parent().prev().val();
				
					//所选择的entity类型为当前类型，生成单选按钮
					if(searchType == '${addedResParentEntityType}') {
						radioContent = "<input class='input_radio' name='rdoChooseResChild' type='radio' />";
					}*/
					
					//根据关联的父类生成单选按钮
					var parentTypeGroup = '${parentEntityTypeGrp}';
					if(parentTypeGroup.indexOf("_") == -1) {
						//存在一个关联的父类型的情况
						if(searchType == parentTypeGroup) {
							radioContent = "<input class='input_radio' name='rdoChooseResChild' type='radio' />";
						}
					} else {
						//存在多个关联的父类型的情况
						var parentTypeArrs = parentTypeGroup.split("_");
						if(parentTypeArrs != null && parentTypeArrs.length > 1) {
							for(var i = 0; i < parentTypeArrs.length; i++) {
								if(searchType == parentTypeArrs[i]) {
									//查找类型，为关联父类型组的其中之一
									radioContent = "<input class='input_radio' name='rdoChooseResChild' type='radio' />";
									break;
								}
							}
						}
					}
		
					content += "<li>" 
						+ imgContent 
						+ radioContent
						+ "<span style='padding-left:5px;cursor:pointer;'>" + entityName + "</span>" 
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
				//$("#hidEntityFlushForRes").data(parentEntityType + "_" + parentEntityId + "_" + searchType, content);
			},'json');
		}
	}
	/**
	 * input+select组合 属性为整型时input text框onfocus处理
	 * @param {Object} id
	 */
	function integerTextOnfocus(id){
		$(id).next().next().html("");
		$(id).prev().val("");
	}
	/**
	 * input+select组合 属性为整型时input text框onblur处理
	 * @param {Object} id
	 */
	function integerTextOnblur(id){
		var currentVal =$.trim($(id).val());
		if(currentVal!=""){
			if(isNaN(currentVal)&&currentVal!="是"&&currentVal!="否"&&currentVal!="请选择"){
				$(id).parent().append("<label style='margin-left:25px;color:#FF0000'>请输入整数</label>");				
			}else if(currentVal=="是"||currentVal=="否"||currentVal=="请选择"){			
				$(id).prev().prev().children().each(function(){
					if($.trim(currentVal)==$(this).text()){
						$(this).attr("selected",true);
					}
				})
				$(id).prev().val($(id).prev().prev().val());
			}else{
				$(id).prev().val($(id).val());
			}
		}
		
		
	}
	/**
	 * 下拉框点击option处理
	 * @param {Object} id
	 */
	function optionOnclick(id){
		//$(id).parent().next().val($(id).text());
		$(id).next().val($(id).find('option:selected').text());
	}
	/**
	 *  属性为整型时下拉框点击option处理
	 * @param {Object} id
	 */
	function integerOptionOnclick(id){
		//$(id).parent().next().next().val($(id).text());
		//$(id).parent().next().val($(id).parent().val());
		$(id).next().val($(id).find('option:selected').text());
		$(id).next().val($(id).val());
	}
	
	</script>
	<style>
	label.error{
		margin-left:25px;
	}
	textarea{
		width:70%;
		height:100px;
		resize:both;
	}
	#physicalresAttribute input[type='text']{
		width:70%
	}
	</style>
</head>

<body>
<%-- 点击类型时隐藏域值 --%>
					<input id="addedResParentEntityType" name="addedResParentEntityType" type="hidden" />
					<input id="addedResParentEntityId" name="addedResParentEntityId" type="hidden" />
					<input id="addedResEntityType" name="addedResEntityType" type="hidden" />
<div class="leftSide">
		<div class="container-tab1">
			<fieldset>
	        	<legend>
	            	<select id="modelType">
	                	<option value="view">浏览模式</option>
	                    <option selected="selected" value="manage">管理模式</option>
	                </select>
	            </legend>
	            <div class="fr">操作：<input type="button" disabled="disabled" value="增加" />&nbsp;<input disabled="disabled" type="button" value="删除" /></div>
	        </fieldset>
	    </div>
	    <div id="treeDiv" class="leftSide-tree">
           	<ul id="tree">
               	<img src="image/plus.gif" onclick="showChildTypeMsg(this);" />
	       		<span>未命名资源</span>
             </ul>
	    </div>
	            
	</div>
	    <div class="rightSide">
	    	<div class="rightSide-top">
	    	<div id="chooseResDiv" style="display:none; border:#ccc 1px solid; border-radius:5px; position:absolute; left:682px;top:30px; background:#f1f1f1; z-index:4;">
	    			                                			        <div style="margin-bottom:4px;"><input style="color:#999;" id="txtSearch" type="text" style="width:150px;" value="请输入" />&nbsp;<input id="btnSearch" type="button" value="搜索" /></div>
	    		
	    		<%-- 隶属内容选择层 --%>
	    		<div id="chooseTree" style="padding:0 8px;">
	    			<ul id="chooseResUl" style="width:250px;height:300px;overflow:auto;line-height:16px;">
	    				<%--
		               	<li>
		               		<img src="image/plus.gif" onclick="showChooseResMsg(this);" />
				        	<span style="cursor:pointer;" class="entityClass" onclick="chooseEntity(this);">
				        		<c:choose>
				        			<c:when test="${not empty grandpaEntityMap.name}">
				        				${grandpaEntityMap.name}
				        			</c:when>
				        			<c:otherwise>
				        				${grandpaEntityMap.label}
				        			</c:otherwise>
				        		</c:choose>
				        	</span>
				        	
				        	<input type="hidden" value="${grandpaEntityMap.id}" />
				        	<input type="hidden" value="${grandpaEntityMap.type}" />
				        	
				        	<input type="hidden" value="${parentEntityMap.type}" />
				        	<div style="padding-left:10px;display:none;"></div>
		               	</li>
		               	--%>
		               	<s:if test="#request.areaId!=null&&#request.areaName!=null">
		               		<li>
				        	<img src="image/plus.gif" onclick="showChooseResTypeMsg(this);" />
				        	<s:if test="parentEntityTypeGrp.indexOf('Sys_Area') > -1">
				        		<input class="input_radio" type="radio" name="rdoChooseResChild">
				        	</s:if>
				        	<span style="cursor:pointer;" class="entityClass">${request.areaName}</span>
				        	<%-- 选进行区域id的hardcode --%>
				        	<input type="hidden" value="${request.areaId}"/>
				        	<input type="hidden" value="Sys_Area"/>
				        	<div style="padding-left:10px;display:none;"></div>
				        </li>
		               	</s:if>
		               	
		             </ul>
	    			<input id="btnChooseParentRes" type="button" value="选择" />&nbsp;
	    			<input id="btnCancelParentRes" type="button" value="取消" />
	    		</div>
		        		
	        	</div>
	        	当前资源：<input id="txtCurrentRes" type="text" value="未命名资源" />&nbsp;隶属：<input id="txtParentRes" type="text" />&nbsp;<input id="btnParentRes" type="button" value="选择" />
	        </div>
	        <div class="rightSide-main" >
		        <form id="operaForm" action="addPhysicalresAction" method="post">
		        	 <%-- 需要更新的资源的类型 --%>
		        	 <input type="hidden" name="addedResEntityType" value="${param.addedResEntityType}" />
		        	 <input type="hidden" name="addedResParentEntityType" value="${param.addedResParentEntityType}" />
		        	 <input type="hidden" name="addedResParentEntityId" value="${param.addedResParentEntityId}" />
		        	  <input type="hidden" name="areaId" value="${request.areaId}" />
		        	 <input id="oldParentResEntityId" name="oldParentResEntityId" type="hidden" />
		        	 <input id="oldParentResEntityType" name="oldParentResEntityType" type="hidden" />
		        	 <input id="newParentResEntityId" name="newParentResEntityId" type="hidden" />
		        	 <input id="newParentResEntityType" name="newParentResEntityType" type="hidden" />
		             <table class="main-table1 t1" id="physicalresAttribute">
		                   <tr class="main-table1-tr"><td colspan="2" class="main-table1-title"> <img class="hide-show-img" src="image/ico_show.gif" />基本属性</td></tr>
		                   <s:iterator value="addedResMap" id="map" status="st">
			                	<s:if test="!(key == 'id' || key == '_entityId' || key == '_entityType')">
				                	<tr>
				                		<td class="menuTd">
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
						                    			<select class="input_select" >
						                    				<option value="" selected="selected" onclick="integerOptionOnclick(this);">请选择</option>
							                    			<s:iterator id="dropdownVal" value="dropdownListMap.get(#map.key)">
							                    				<s:if test='#dropdownVal=="是"'>
								                    						<option  value="1" onclick="integerOptionOnclick(this);">${dropdownVal}</option>          
								                    			</s:if>
								                    			<s:elseif test='#dropdownVal=="否"'>
								                    						<option  value="0" onclick="integerOptionOnclick(this);">${dropdownVal}</option>           
								                    			</s:elseif>
								                    			<s:else>
								                    						<option value="${dropdownVal}" onclick="integerOptionOnclick(this);">${dropdownVal}</option>
								                    			</s:else>
							                    			</s:iterator>
						                    			</select>
						                    			<input  type="hidden" name="${param.addedResEntityType}#${key}" value=''/>
								                    	<input title='请填写：0或者1，0表示“否”，1表示“是”' onblur="integerTextOnblur(this);" onfocus="integerTextOnfocus(this);"  type="text" style="margin-left:-139px;position:relative;z-index=2; width:113px;top:1px;border-bottom:none;" value=""/>
						                    		</s:if>
						                    		<s:else>
						                    			<input title='请填写：整数，如“12”' validateDigit="#${param.currentEntityType}_${key}" id="${param.currentEntityType}_${key}" name="${param.addedResEntityType}#${key}" type="text" />
						                    		</s:else>
						                    	</s:if>
						                    	<s:else>
						                    		<s:if test="dropdownListMap.get(#map.key) != null">
						                    			<select class="input_select required">
						                    				<option value="" selected="selected" onclick="integerOptionOnclick(this);">请选择</option>
							                    			<s:iterator id="dropdownVal" value="dropdownListMap.get(#map.key)">
							                    				<s:if test='#dropdownVal=="是"'>
								                    						<option  value="1" onclick="integerOptionOnclick(this);">${dropdownVal}</option>          
								                    			</s:if>
								                    			<s:elseif test='#dropdownVal=="否"'>
								                    						<option  value="0" onclick="integerOptionOnclick(this);">${dropdownVal}</option>           
								                    			</s:elseif>
								                    			<s:else>
								                    						<option value="${dropdownVal}" onclick="integerOptionOnclick(this);">${dropdownVal}</option>
								                    			</s:else>
							                    			</s:iterator>
						                    			</select>
						                    			<input  type="hidden" name="${param.addedResEntityType}#${key}" value=''/>
								                    	<input title='请填写：0或者1，0表示“否”，1表示“是”' onblur="integerTextOnblur(this);" onfocus="integerTextOnfocus(this);"  type="text" style="margin-left:-139px;position:relative;z-index=2; width:113px;top:1px;border-bottom:none;" value=""/>
						                    		</s:if>
						                    		<s:else>
						                    			<input title='请填写：整数，如“12”'class="required" validateDigit="#${param.currentEntityType}_${key}" id="${param.currentEntityType}_${key}" name="${param.addedResEntityType}#${key}" type="text" />
						                    		</s:else>
						                    		<span class="redStar">*</span>
						                    	</s:else>
						                    	<s:if test="dropdownListMap.get(#map.key) != null">
					                    		<em id='manageModel' style="margin-left: 22px;color:#999">(整数)</em>
						                    	</s:if>
						                    	<s:else>
						                    		<em id='manageModel' style="color:#999">(整数)</em>
						                    	</s:else>
					                    	</s:if>
					                    	<%-- 验证浮点型的情况 --%>
					                    	<s:elseif test="attrTypeMap.get(#map.key).indexOf('Double') > -1  || attrTypeMap.get(#map.key).indexOf('Float') > -1">
					                    		<%-- 判断属性的非空情况 --%>
						                    	<s:if test="'true'.equals(nullableMap.get(#map.key))">
						                    		<s:if test="dropdownListMap.get(#map.key) != null">
						                    			<select class="input_select" >
						                    				<option value="" selected="selected" onclick="optionOnclick(this);">请选择</option>
							                    			<s:iterator id="dropdownVal" value="dropdownListMap.get(#map.key)">
							                    				<option value="${dropdownVal}" onclick="optionOnclick(this);">${dropdownVal}</option>
							                    			</s:iterator>
						                    			</select>
								                    	<input class="number"  title='请填写：数值，如“1232.23”，“12.03”' name="${param.addedResEntityType}#${key}" type="text" style="margin-left:-139px;position:relative;z-index=2; width:113px;top:1px;border-bottom:none;" value=""/>							                    										                    				
						                    		</s:if>
						                    		<s:else>
						                    			<s:if test="#map.key=='longitude'">
						                    				<input title='请填写：数值，如“1232.23”，“12.03”' class="number" name="${param.addedResEntityType}#${key}" type="text" value="${requestScope.longitude}"/>
						                    			</s:if>
						                    			<s:elseif test="#map.key=='latitude'">
						                    				<input title='请填写：数值，如“1232.23”，“12.03”' class="number" name="${param.addedResEntityType}#${key}" type="text" value="${requestScope.latitude}"/>
						                    			</s:elseif>
						                    			<s:else>
						                    				<input title='请填写：数值，如“1232.23”，“12.03”' class="number" name="${param.addedResEntityType}#${key}" type="text"/>
						                    			</s:else>
													</s:else>
						                    	</s:if>
						                    	<s:else>
						                    		<s:if test="dropdownListMap.get(#map.key) != null">
						                    			<select class="input_select required">
						                    				<option value="" selected="selected" onclick="optionOnclick(this);">请选择</option>
							                    			<s:iterator id="dropdownVal" value="dropdownListMap.get(#map.key)">
							                    				<option value="${dropdownVal}" onclick="optionOnclick(this);">${dropdownVal}</option>
							                    			</s:iterator>
						                    			</select>
								                    	<input class="number" title='请填写：数值，如“1232.23”，“12.03”' name="${param.addedResEntityType}#${key}" type="text" style="margin-left:-139px;position:relative;z-index=2; width:113px;top:1px;border-bottom:none;" value=""/>							                    										                    				
						                    		</s:if>
						                    		<s:else>
						                    			<s:if test="#map.key=='longitude'">
						                    				<input title='请填写：数值，如“1232.23”，“12.03”' class="number required" name="${param.addedResEntityType}#${key}" type="text" value="${requestScope.longitude}"/>
						                    			</s:if>
						                    			<s:elseif test="#map.key=='latitude'">
						                    				<input title='请填写：数值，如“1232.23”，“12.03”' class="number required" name="${param.addedResEntityType}#${key}" type="text" value="${requestScope.latitude}"/>
						                    			</s:elseif>
						                    			<s:else>
						                    				<input title='请填写：数值，如“1232.23”，“12.03”' class="number required" name="${param.addedResEntityType}#${key}" type="text"/>
						                    			</s:else>
						                    		</s:else>
						                    		<span class="redStar">*</span>
						                    	</s:else>
						                    	<s:if test="dropdownListMap.get(#map.key) != null">
						                    		<em id='manageModel' style="margin-left: 22px;color:#999">(数值)</em>
						                    	</s:if>
						                    	<s:else>
						                    		<em id='manageModel' style="color:#999">(数值)</em>
						                    	</s:else>
					                    	</s:elseif>
					                    	<%-- 验证日期类型的情况 --%>
					                    	<s:elseif test="attrTypeMap.get(#map.key).indexOf('Date') > -1">
					                    		<%-- 判断属性的非空情况 --%>
						                    	<s:if test="'true'.equals(nullableMap.get(#map.key))">
						                    		<input title='请填写：日期时间，如“2012-10-09”，“2012-10-09 11:02:30”' readonly="readonly" name="${param.addedResEntityType}#${key}" type="text" 
					                    			class="input_text" id="time<s:property value='#st.index' />"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
						                    	</s:if>
						                    	<s:else>
						                    		<input title='请填写：日期时间，如“2012-10-09”，“2012-10-09 11:02:30”'  readonly="readonly" name="${param.addedResEntityType}#${key}" type="text" 
					                    			class="input_text required" id="time<s:property value='#st.index' />" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
						                    		<span class="redStar">*</span>
						                    	</s:else>
					                    		<%--  <input type="button" class="input_button" value="选择时间" 
														onclick=fPopCalendar(event,document.getElementById('time<s:property value="#st.index" />'),document.getElementById('time<s:property value="#st.index" />'),true) />--%>
					                    	</s:elseif>
					                    	<%-- 验证其他类型的情况 --%>
					                    	<s:else>
					                    		<%-- 判断属性的非空情况 --%>
						                    	<s:if test="'true'.equals(nullableMap.get(#map.key))">
						                    		<s:if test="dropdownListMap.get(#map.key) != null">
						                    			<select class="input_select">
						                    				<option value="" selected="selected" onclick="optionOnclick(this);">请选择</option>
							                    			<s:iterator id="dropdownVal" value="dropdownListMap.get(#map.key)">
							                    				<option value="${dropdownVal}" onclick="optionOnclick(this);">${dropdownVal}</option>
							                    			</s:iterator>
						                    			</select>
								                    	<input title='请填写：字符串'  name="${param.addedResEntityType}#${key}" type="text" style="margin-left:-139px;position:relative;z-index=2; width:113px;top:1px;border-bottom:none;" value=""/>							                    										                    				
						                    		</s:if>
						                    		<s:else>
						                    			<s:if test="attrLengthMap.get(#map.key)>=100">
							                    				<textarea rows="5" title='请填写：字符串' name="${param.addedResEntityType}#${key}"></textarea>
							                    		</s:if>
							                    		<s:else>
							                    				<input title='请填写：字符串' name="${param.addedResEntityType}#${key}" type="text" />							                    			
							                    		</s:else>
						                    		</s:else>
						                    	</s:if>
						                    	<s:else>
						                    		<s:if test="dropdownListMap.get(#map.key) != null">
						                    			<select class="input_select required">
						                    				<option value="" selected="selected" onclick="optionOnclick(this);">请选择</option>
							                    			<s:iterator id="dropdownVal" value="dropdownListMap.get(#map.key)">
							                    				<option value="${dropdownVal}" onclick="optionOnclick(this);">${dropdownVal}</option>
							                    			</s:iterator>
						                    			</select>
								                    	<input class="required" title='请填写：字符串' name="${param.addedResEntityType}#${key}" type="text" style="margin-left:-139px;position:relative;z-index=2; width:113px;top:1px;border-bottom:none;" value=""/>							                    										                    				
						                    		</s:if>
						                    		<s:else>
						                    			<s:if test="attrLengthMap.get(#map.key)>=100">
							                    				<textarea rows="5" title='请填写：字符串'  class="required" name="${param.addedResEntityType}#${key}"></textarea>
							                    		</s:if>
							                    		<s:else>
							                    				<input title='请填写：字符串' class="required" name="${param.addedResEntityType}#${key}" type="text" />							                    			
							                    		</s:else>
						                    		</s:else>
						                    		<span class="redStar">*</span>
						                    	</s:else>
					                    	</s:else>
					                    </td>
					                </tr>
				                </s:if>
		               		</s:iterator>
		             </table>
		             
		             <%--<table class="main-table1">
		                    <tr class="main-table1-tr"><td colspan="3" class="main-table1-title"> <img class="hide-show-img" src="image/ico_show.gif" />管理标签</td></tr>
		                    
		                    <tr>
		                    	<td style="width:30%">BTS</td>
		                        <td class="highLighte">HZUCZF</td>
		                        <td></td>
		                    </tr>
		                    <tr>
		                    	<td style="width:30%">本地传输网局号</td>
		                        <td class="highLighte">0758B1202</td>
		                        <td><input type="button" value="..." /></td>
		                    </tr>
		                    <tr>
		                    	<td style="width:30%">维护责任单位</td>
		                        <td class="highLighte">200803-广东怡创-第一事业部</td>
		                        <td><input type="button" value="..." /></td>
		                    </tr>
		             </table>--%>
		             <div style="width:100%; text-align:center; padding-bottom:10px;">
		             	<input type="submit" value="保存" />&nbsp;&nbsp;<input type="button" value="取消" onclick="cancelOpera();" />
		             </div>
		         </form>
	         </div>
		</div>
	</div>
</body>
</html>
