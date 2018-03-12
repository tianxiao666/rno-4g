<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>资源分类统计</title>
<link rel="stylesheet" href="css/base.css" type="text/css" />
<link rel="stylesheet" href="css/ext-all.css" type="text/css" />
<link rel="stylesheet" href="css/example.css" type="text/css" />
<link rel="stylesheet" href="css/public.css" type="text/css" />
<link rel="stylesheet" href="css/ziyuan.css" type="text/css" />
<link type="text/css" rel="stylesheet" href="css/confirm_div.css" />
<link rel="stylesheet" type="text/css" href="css/city.css" />
<style type="text/css">
/*查询结果列表*/
.aetg{background-color: #CDDFF5; max-height: 200px; overflow: auto; position: absolute; top: 26px; width: 100%;}
.aetg ul li{line-height:24px; padding: 0 5px; cursor:pointer;}
.aetg ul li:hover{background-color: #88B9F5; }
.aetg .aetg_text{display: inline-block; width: 160px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;}
.aetg .aetg_link{position:absolute; right:0px; display:inline-block;}
.aetg a{padding:0 3px;}

/*弹出框*/
.dialog_box{width: 530px; background: url(image/panel_title.png) repeat scroll 0 0 transparent; border: 1px solid #99BBE8; border-radius: 5px 5px 5px 5px; font-size: 12px; top: 100px; padding: 5px; left: 45%; margin-left: -200px; position: absolute;z-index:98;}
.dialog_header{ color: #15428B; font-size: 12px; font-weight: bold; line-height: 15px; padding: 2px 0 4px; position: relative;}
.dialog_tool { position: absolute; right: 1px; top: 0px;}
.dialog_tool div {cursor: pointer; display: block;float: right; height: 16px; margin-left: 2px; opacity: 0.6; width: 16px;}
.dialog_tool_close {background: url(image/panel_tools.gif) no-repeat scroll -16px 0 transparent;}
.dialog_content{border: 1px solid #99BBE8; background-color:#fff;padding:5px;}
.dialog_content ul li{padding:0 5px;}
.dialog_content label{display: block;float: left;text-align: right;vertical-align: middle;width: 85px;}
.dialog_content span{display: block;padding: 0px 5px;text-align: left;vertical-align: middle;margin-left: 85px; color:#444;}
.dialog_black {background: none repeat scroll 0 0 rgba(0, 0, 0, 0.5); height: 100%; left: 0; opacity: 0.5; position: fixed; top: 0; width: 100%;display: none;z-index: 97;}

.logicalressearchtab{ /*搜索切换*/
    background-color: #F1F1F1;
    border-color: -moz-use-text-color #999999 #999999;
    border-image: none;
    border-radius: 0 0 5px 5px;
    border-style: none solid solid;
    border-width: medium 1px 1px;
    margin-left: 3px;
    margin-top: -2px;
    max-height: 327px;
    padding: 1px 0 8px;
    position: absolute;
    top: 27px;
    width: 188px;
}
</style>
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="../js/input.js"></script>
<script type="text/javascript" src="../../jslib/highcharts/highcharts.src.js"></script>
<script type="text/javascript" src="../../jslib/highcharts/exporting.src.js"></script>
<script type="text/javascript" src="js/report.js"></script>
<script type="text/javascript" src="../js/jquery.highlighter-1.0.0.min.js"></script>
<script type="text/javascript" src="js/ext-all-debug.js"></script>
<script type="text/javascript" src="js/table-paging.js"></script>
<script type="text/javascript" src="../js/objutil.js"></script>
<script type="text/javascript" src="js/resourceArea.js"></script>
<script type="text/javascript">
var logicalressearchMethod="all";

$(function(){
		// 美工代码

	$(".city_show").live("click" , function(){
		$(".map_city").show();
		$(".map_district").hide();
		$(".map_street").hide();
	});
	$(".district_show").live("click" , function(){
		$(".map_district").show();
		$(".map_city").hide();
		$(".map_street").hide();
	});
	$(".street_show").live("click" , function(){
		$(".map_street").show();
		$(".map_city").hide();
		$(".map_district").hide();
	});
	$(".close_icon").live("click" , function(){
		$(".map_city").hide();
		$(".map_district").hide();
		$(".map_street").hide();
	});
	
	//loadParentArea();
	onLoadReport();
	$(".dialog_hide").click(function(){//弹出框隐藏
				$("#dialog").hide();
				$(".dialog_black").hide();
	});
	$(".logicalressearchtab").hover(
			  function () {
			    $(this).show();
			  },
			  function () {
			    $(this).hide();
			  }
			); 
			
			$("#logicalresallCheckBox").click(function(){//搜索切换
				if($(this).attr("checked")=='checked'){
					$("#logicalrespartCheckBox").removeAttr('checked');
					//$("li[id='allLi']").hide();
					//$("li[id='partLi']").show();
					logicalressearchMethod='all';
					
					if($.trim($("#logicalresTxtSearch").val())=="资源树搜索"||$.trim($("#logicalresTxtSearch").val())==""){
						$("#logicalresTxtSearch").val("资源分类搜索").css('color','#999');
					}
				}else{
					$("#logicalrespartCheckBox").attr('checked','checked');
					//$("li[id='allLi']").show();
					//$("li[id='partLi']").hide();
					logicalressearchMethod='part';
					if($.trim($("#logicalresTxtSearch").val())=="资源分类搜索"||$.trim($("#logicalresTxtSearch").val())==""){
						$("#logicalresTxtSearch").val("资源树搜索").css('color','#999');
					}
				}
				//$(".searchtab").hide();
			})
			$("#logicalrespartCheckBox").click(function(){//搜索切换
				if($(this).attr("checked")=='checked'){
					$("#logicalresallCheckBox").removeAttr('checked');
					//$("li[id='allLi']").show();
					//$("li[id='partLi']").hide();
					logicalressearchMethod='part';
					var chosenEntityType = $("#logicalresChosenParentEntityType").val();
					var chosenEntityId = $("#logicalresChosenParentEntityType").val();
					var chooseType = $("#logicalresAddedResEntityType").val();
					if($("#logicalresTreeRootName").parent().children().eq(0).attr("src")=='image/plus.gif'){
						chooseEntitys("#logicalresTreeRootName");
					}else{
						if(chosenEntityId==""&&chosenEntityType==""&&chooseType==""){
							chooseEntitys("#logicalresTreeRootName");
						}
					}
					if($.trim($("#logicalresTxtSearch").val())=="资源分类搜索"||$.trim($("#logicalresTxtSearch").val())==""){
						$("#logicalresTxtSearch").val("资源树搜索").css('color','#999');
					}
					
				}else{
					$("#logicalresallCheckBox").attr('checked','checked');
					logicalressearchMethod='all';
					if($.trim($("#logicalresTxtSearch").val())=="资源树搜索"||$.trim($("#logicalresTxtSearch").val())==""){
						$("#logicalresTxtSearch").val("资源分类搜索").css('color','#999');
					}
				}
			})
	
			$("#logicalresTxtSearch").blur(function(){
				if($("#logicalrespartCheckBox").attr("checked")=='checked'){
					if($.trim($("#logicalresTxtSearch").val())==""){
						$("#logicalresTxtSearch").val("资源树搜索").css('color','#999');
					}
				}else{
					if($.trim($("#logicalresTxtSearch").val())==""){
						$("#logicalresTxtSearch").val("资源分类搜索").css('color','#999');
					}
				}
			})
	


	$(".show_top_a").click(function(){
			
		if($("#down2").css("display") == "block"){
			$(this).children().eq(0).text("统计设置 ▼");	
		}else{
			$(this).children().eq(0).text("统计设置 ▲");
		}
		$("#down2").slideToggle("fast");
	})
	$(".min_top_a").click(function(){
			
		if($("#down_de").css("display") == "block"){
			$(this).children().eq(0).text("资源统计报表 ▼");	
			$("#down").css("height","25px");
		}else{
			$(this).children().eq(0).text("资源统计报表 ▲");
			$("#down").css("height","380px");
		}
		$("#down_de").slideToggle("fast");
	})
	$(".btm_top_a").click(function(){
			
		if($("#bottom1").css("display") == "block"){
			$(this).children().eq(0).text("资源详细信息 ▼");	
		}else{
			$(this).children().eq(0).text("资源详细信息 ▲");
		}
		$("#bottom1").slideToggle("fast");
	})
})


	
				//搜索栏"请输入"提示控制
				function txtSearchFocus()
				
				{
				//已更改为黑色字，不作清除内容操作
				if($("#logicalresTxtSearch").css("color") == "#000" || $("#logicalresTxtSearch").css("color") == "rgb(0, 0, 0)") {
					return false;
				}
				$("#logicalresTxtSearch").val("");
				//focus后，字体更改为黑色
				$("#logicalresTxtSearch").css("color","#000");
				}

	//加载资源
	function loadResourceEntity(){
		var areaId = $("#areaId").val();
		var areaType = $("#areaType").val();
		var selectResType = $("#selectType").val();
		$("#spanCheckBox").removeAttr("checked");
		
		if(selectResType){
			if(selectResType.split(",")[0] == "Sys_Area"){
				$("#spanCheckBox").show();
			}else{
				$("#spanCheckBox").hide();
			}
			$("#divDisplay").show();
			$("#tree-ul input[name = 'rdoChooseResChild']").removeAttr("checked");
			//$("input[name='select_ziyuan']").removeAttr("checked");
		}else{
			alert("请选择资源类型");
		}
	}
	
	//获取已添加资源的HTML元素ID
	var optionId= "";
	function optionClick(me){
		optionId = $(me).val();
	}
	
	
	//清除已添加的资源
	function removerOption(){
		if(optionId){
			$("#"+optionId+"").remove();
		}
		selectResourceDisabled();
	}
	
	var allLink = "";
	//读取资源可关联类型 
	function loadType(me){
		$("#checkAll").removeAttr("checked");
		allLink = $(me).val();
		var currentEntitys = $("#selectType").val();//当前节点的类型
			var strs= new Array(); //定义一数组
			strs = currentEntitys.split(",");
			var selectResType = strs[0];
		var url = "loadChildRoLinkTypeBySelectResTypeAction";
		var params = {allLink:allLink,selectResType:selectResType};
			$.post(url, params, function(data){
				data = eval(data);
				var countext = "";
				$.each(data, function(key, value){
					if(value.type){
						countext = countext + "<span name=\"resourceType\" class='inline ml8 mt8 w20 nowrap' title='"+value.chineseType+"'><input type='checkbox' name='selectType' value='"+value.type+","+value.associatedType+"' onclick='selectTypeSpan();'/>"+value.chineseType+"</span>";
					}else{
						countext = countext + "<span name=\"resourceType\" class='inline ml8 mt8 w20 nowrap' title='"+value.type+"'><input type='checkbox' name='selectType' value='"+value.type+","+value.associatedType+"' onclick='selectTypeSpan();'/>"+value.type+"</span>";
					}
				});
				$("#selectDiv").html(countext);
			});
	}
	
	var isSelect = false;
	//根据统计设置查询生成统计报表
	function selectResource(){
		var currentType = $("#selectType").val();//当前节点的类型
			var strs= new Array(); //定义一数组
			strs = currentType.split(",");
			var resourceType = strs[0];
			$("#selectEntityHidden").val(resourceType);
		//if($("#recursiveCheckbox").attr("checked")){
			 //alert("递归");
		//}
		var basicRoStacked = "";
		if($("#effect_settings").attr("checked") == "checked"){
			$("input[name='duibituxiaoguo']").each(function(){
				if($(this).attr("checked")){
					 //alert($(this).val());
					 basicRoStacked = $(this).val();
				}
			});
		}else{
			basicRoStacked = "basic";
		}
		var entityId = "";
		var entityName= new Array(); //定义一数组
		var entityType = "";
		var i = 0;
		var selectEntityCountext = "";
		$("#selectEntity option").each(function(){
				 if($(this).val()){
				 	if(entityId == ""){
				 		entityId = $(this).val();
				 	}else{
				 		entityId = entityId + "," + $(this).val();
				 	}if(entityType == ""){
				 		entityType = $(this).attr("title");
				 	}else{
				 		entityType = entityType + "," + $(this).attr("title");
				 	}
				 }
	 		entityName[i] = $(this).text();
	 		var select = $(this).text();
	 		var selectStrs = "";
	 		if(select.indexOf(")") == 5){
						selectStrs = select.substring(0,select.indexOf("(")-3);
					}
	 		else if(select.indexOf(")")){
						selectStrs = select.substring(select.indexOf(")")+1);
					}else{
						selectStrs = select;
					}
	 		selectEntityCountext = selectEntityCountext+"<div id='"+selectStrs+i+"' title='"+$(this).attr('title')+"'>"+$(this).val()+"</div>";
	 		i++;
		});
		$("#selectEntityDivHidden").html(selectEntityCountext);
		//alert(entityName);
		var selectResType = "";
		var selectDivCountext = "";
		var selectDivCount = 0;
		$("#selectDiv input[name='selectType']").each(function(){
				 if($(this).attr("checked")){
				 if(selectResType == ""){
				 		selectResType = $(this).val();
				 	}else{
				 		selectResType = selectResType + "&_&" + $(this).val();
				 	}
				 	
					var selectStrsId = "";
					//alert($(this).parent().text());
					if($(this).parent().text().indexOf(")") == 5){
						selectStrsId = $(this).parent().text().substring(0,$(this).parent().text().indexOf(")")-3);
					}
					else if($(this).parent().text().indexOf(")")){
						selectStrsId = $(this).parent().text().substring($(this).parent().text().indexOf(")")+1);
					}else{
						selectStrsId = $(this).parent().text();
					}
					selectDivCountext = selectDivCountext+"<div id='"+selectStrsId+"'>"+$(this).val()+"</div>";
					selectDivCount++;
			}
		});
		//alert(selectDivCount);
		if(allLink == 'allLink' && selectDivCount > 5 && selectDivCount < 12 && isSelect == false){
			$("#confirm_div_warning").show();
			$("#selectDivCount").text(selectDivCount);
			return;
		}
		isSelect = false;
		if(allLink == 'allLink' && selectDivCount >= 12){
			$("#confirm_div_wrong").show();
			return;
		}
		$("#down2").hide();
		$("#load_img").show();
		$("#down_down1").html("");
		$("#down_down2").hide();
		$("#down_top").html("");
		$("#bottom_left").html("");
		$("#bottom_right").html("");
		var allLinkvar = "";
		if(allLink == "child"){
			allLinkvar = "allLink";
		}else{
			allLinkvar = allLink;
		}
		$("#selectDivHidden").html(selectDivCountext);
		var url = "statisticsResourceTotalAction";
			var params = {entityId:entityId,selectResType:selectResType,currentType:entityType,allLink:allLinkvar};
			var countext = "<table><tr><th>类型/"+$("#selectType").find("option:selected").text()+"</th>";
			$.each(entityName,function(key,value){
				countext = countext + "<th>"+value+"</th>";
			});
			countext = countext + "</tr>";
			$.post(url,params,function(data){
				//alert(data);
				var evaldata = eval("["+data+"]");
				//alert(evaldata);
				var categories =entityName;
				var series = "";
				$.each(evaldata,function(key,value){
					$.each(value,function(ke,val){
							//alert(ke+",,,,"+val);
							if(val.lastIndexOf(",") == val.length-1){
								val = val.substring(0,val.length-1);
							}
							series = series + "{name:'"+ke+"',data:["+val+"]},";
							countext = countext + "<tr><td>" +ke+ "</td>";
							var vals= new Array(); //定义一数组
							vals = val.split(",");
							var i = 0;
							$.each(vals,function(valskey,valsvalue){
								if(valsvalue){//style='display: none;'
									countext = countext + "<td><a href='#' onclick='getTableResource(this,"+i+")';>" +valsvalue+ "</a><span style='display: none;'>"+entityName[valskey]+"</span><span style='display: none;'>"+ke+"</span></td>";
								}
								//alert(entityName[i]);
								i++;
							});
							countext = countext + "</tr>";
					});
				});
				countext = countext + "</table>";
				$("#down_down1").html(countext);
				$("#down_down2").show();
				if(series != null){
					series = series.substring(0,series.length-1);
				}
				series = "["+series+"]";
				//alert(series);
				//alert(categories);
				series = eval(series);
				var chartsType = "column";
				if($("#effect_settings").attr("checked") == "checked"){
					chartsType = $("#chartsType").val();
				}
				
				if(chartsType == 'column'){
					if(basicRoStacked == 'basic'){
						columnBasic('down_top',categories,series);
					}else if(basicRoStacked == 'stacked'){
						columnStacked('down_top',categories,series);
					}
				}else if(chartsType == 'area'){
					if(basicRoStacked == 'basic'){
						areaBasic('down_top',categories,series);
					}else if(basicRoStacked == 'stacked'){
						areaStacked('down_top',categories,series);
					}
				}else if(chartsType == 'line'){
					if(basicRoStacked == 'basic'){
						lineBasic('down_top',categories,series);
					}else if(basicRoStacked == 'stacked'){
						lineStacked('down_top',categories,series);
					}
				}
				$("#load_img").hide();
			});
	}
	
	function getTableResource(me,xName){
		var entityId = $(me).next().text();
		var select = $(me).next().next().text();
		clickShowMore(xName,entityId,select);
	}
	
	function onLoadReport(){
		var repertType = $("#repertType").val();
		if(repertType != ""){
		$("#down2").hide();
		$("#load_img").show();
			var areaId = $("#areaId").val();
		var areaType = $("#areaType").val();
		var areaName = $("#repertTypeareaName").val();
		var url = "statisticsResourceTotalAction";
			var params = {entityId:areaId,currentType:areaType,repertType:repertType,allLink:"allLink"};
			var countext = "<table><tr><th>&nbsp;</th><th>&nbsp;</th>";
			countext = countext + "</tr>";
			$.post(url,params,function(data){
				//alert(data);
				var evaldata = eval("["+data+"]");
				//alert(evaldata);
				var categories = new Array();
				categories[0] = areaName;
				var series = "";
				$.each(evaldata,function(key,value){
					$.each(value,function(ke,val){
							//alert(ke+",,,,"+val);
							if(val.lastIndexOf(",") == val.length-1){
								val = val.substring(0,val.length-1);
							}
							series = series + "{name:'"+ke+"',data:["+val+"]},";
							countext = countext + "<tr><td>" +ke+ "</td>";
							var vals= new Array(); //定义一数组
							vals = val.split(",");
							$.each(vals,function(valskey,valsvalue){
								if(valsvalue){
									countext = countext + "<td>" +valsvalue+ "</td>";
								}
							});
							countext = countext + "</tr>";
					});
				});
				countext = countext + "</table>";
				$("#down_down1").html(countext);
				$("#down_down2").show();
				if(series != null){
					series = series.substring(0,series.length-1);
				}
				series = "["+series+"]";
				//alert(series);
				//alert(categories);
				series = eval(series);
				var chartsType = "column";
				if($("#effect_settings").attr("checked") == "checked"){
					chartsType = $("#chartsType").val();
				}
				var basicRoStacked = 'basic';
				if(chartsType == 'column'){
					if(basicRoStacked == 'basic'){
						columnBasic('down_top',categories,series);
					}else if(basicRoStacked == 'stacked'){
						columnStacked('down_top',categories,series);
					}
				}else if(chartsType == 'area'){
					if(basicRoStacked == 'basic'){
						areaBasic('down_top',categories,series);
					}else if(basicRoStacked == 'stacked'){
						areaStacked('down_top',categories,series);
					}
				}else if(chartsType == 'line'){
					if(basicRoStacked == 'basic'){
						lineBasic('down_top',categories,series);
					}else if(basicRoStacked == 'stacked'){
						lineStacked('down_top',categories,series);
					}
				}
				$("#load_img").hide();
			});
		}
		
	}
	
	
	
	//加载并显示子级类型信息
		function showChildTypeMasg(me) {
			//内容信息已打开，直接隐藏该层，不作任何操作
			if($(me).attr("src") == "image/minus.gif") {
				$(me).attr("src","image/plus.gif");
				$(me).next().next().next().next().next().slideUp("fast");
				
				return;
			}
		
			//获取父级节点的类型
			var parentEntityType = $(me).next().next().next().next().val();
			var parentEntityId = $(me).next().next().next().val();
			//var currentEntityId = $(me).next().next().next().val();//当前节点的id
			
			var currentEntitys = $("#selectType").val();//当前节点的类型
			var strs= new Array(); //定义一数组
			strs = currentEntitys.split(",");
			var currentEntityType = strs[0];
			var currentEntityName = strs[1];
			var content = "";
			if(currentEntityType == "GeneralBaseStation"){
				$("#TypeDiv input[name = 'GeneralBaseStation']").each(function(){
					//alert($(this).val());
					//$(this).val().split("$_$");
					var trs= $(this).val().split("$_$");
					var currentEntityTypes = trs[0];
					var currentEntityNames = trs[1];
					//是否完成加载类型
					var isFinishAddType = false;
			
					
						var imgContent = "";
							imgContent = "<img src='image/plus.gif' onclick='showChildEntityMasg(this);' />";
						content += "<li>" 
						+ imgContent ;
						content = content + "<span onclick='logicalresChooseType(this);' style='padding-left:5px;cursor:pointer;'>" + currentEntityNames+"(...)" + "</span>";
						 content = content
						+ "<input type='hidden'/>" 
						+ "<input type='hidden' value='"+currentEntityTypes+"'>" 
						+ "<div style='padding-left:15px;display:none;'></div>" 
						+ "</li>";
					});
					content = "<ul>" + content + "</ul>";
					//加载子级内容
				$(me).next().next().next().next().next().get(0).innerHTML =  content;
				//显示或隐藏子级内容
				if($(me).attr("src") == "image/plus.gif") {
					$(me).attr("src","image/minus.gif");
					$(me).next().next().next().next().next().slideDown("fast");
				} else {
					$(me).attr("src","image/plus.gif");
					$(me).next().next().next().next().next().slideUp("fast");
				}
					//加载类型完毕，设置为true
					isFinishAddType = true;
			
			var addCountInterval = setInterval(function(){
				//加载类型已完毕，加载数量
				if(isFinishAddType) {
					//获取该节点下各类型的节点数量
					$(me).next().next().next().next().next().children().children().each(function(index){
						//获取子类型下的li节点
						var liObj = $(this);
					
						//获取需要查找的类型
						var searchType = $(this).children().eq(3).val();
						//alert(parentEntityType);
						var params = {parentEntityType:parentEntityType,parentEntityId:parentEntityId,searchType:searchType,addLink:'addLink'}
						
							$.post("../restree/getChildEntityByTypeUniversalRecursionAction",params,function(data){
								//加载对应类型的数量
								var operatedObj = liObj.children().eq(1);
								var countIndex = (operatedObj.html()).indexOf("(...)");
								var firstContent = operatedObj.html();
								
								if(data == "0") {
									//数量为0时隐藏小加号
									liObj.children().eq(0).css("visibility","hidden");
									
								}
								operatedObj.html(firstContent.substring(0, countIndex) + "("+data+")");
								
							},'json');
					});
					
					isFinishAddType = false;
					//跳出判断是否加载类型完毕的循环
					clearInterval(addCountInterval);
				}
			},10);
			}else if(currentEntityType == "GeneralRoomContainer"){
				$("#TypeDiv input[name = 'GeneralRoomContainer']").each(function(){
					//alert($(this).val());
					//$(this).val().split("$_$");
					var trs= $(this).val().split("$_$");
					var currentEntityTypes = trs[0];
					var currentEntityNames = trs[1];
					//是否完成加载类型
					var isFinishAddType = false;
			
					
						var imgContent = "";
							imgContent = "<img src='image/plus.gif' onclick='showChildEntityMasg(this);' />";
						content += "<li>" 
						+ imgContent ;
						content = content + "<span onclick='logicalresChooseType(this);' style='padding-left:5px;cursor:pointer;'>" + currentEntityNames+"(...)" + "</span>";
						 content = content
						+ "<input type='hidden'/>" 
						+ "<input type='hidden' value='"+currentEntityTypes+"'>" 
						+ "<div style='padding-left:15px;display:none;'></div>" 
						+ "</li>";
					});
					content = "<ul>" + content + "</ul>";
					//加载子级内容
				$(me).next().next().next().next().next().get(0).innerHTML =  content;
				//显示或隐藏子级内容
				if($(me).attr("src") == "image/plus.gif") {
					$(me).attr("src","image/minus.gif");
					$(me).next().next().next().next().next().slideDown("fast");
				} else {
					$(me).attr("src","image/plus.gif");
					$(me).next().next().next().next().next().slideUp("fast");
				}
					//加载类型完毕，设置为true
					isFinishAddType = true;
			
			var addCountInterval = setInterval(function(){
				//加载类型已完毕，加载数量
				if(isFinishAddType) {
					//获取该节点下各类型的节点数量
					$(me).next().next().next().next().next().children().children().each(function(index){
						//获取子类型下的li节点
						var liObj = $(this);
					
						//获取需要查找的类型
						var searchType = $(this).children().eq(3).val();
						//alert(parentEntityType);
						var params = {parentEntityType:parentEntityType,parentEntityId:parentEntityId,searchType:searchType,addLink:'addLink'}
						
							$.post("../restree/getChildEntityByTypeUniversalRecursionAction",params,function(data){
								//加载对应类型的数量
								var operatedObj = liObj.children().eq(1);
								var countIndex = (operatedObj.html()).indexOf("(...)");
								var firstContent = operatedObj.html();
								
								if(data == "0") {
									//数量为0时隐藏小加号
									liObj.children().eq(0).css("visibility","hidden");
									
								}
								operatedObj.html(firstContent.substring(0, countIndex) + "("+data+")");
								
							},'json');
					});
					
					isFinishAddType = false;
					//跳出判断是否加载类型完毕的循环
					clearInterval(addCountInterval);
				}
			},10);
			}else{
			//是否完成加载类型
			var isFinishAddType = false;
			
					var content = "";
						var imgContent = "";
							imgContent = "<img src='image/plus.gif' onclick='showChildEntityMasg(this);' />";
						content += "<li>" 
						+ imgContent ;
						content = content + "<span onclick='logicalresChooseType(this);' style='padding-left:5px;cursor:pointer;'>" + currentEntityName+"(...)" + "</span>";
						 content = content
						+ "<input type='hidden'/>" 
						+ "<input type='hidden' value='"+currentEntityType+"'>" 
						+ "<div style='padding-left:15px;display:none;'></div>" 
						+ "</li>";
					content = "<ul>" + content + "</ul>";
					//加载子级内容
				$(me).next().next().next().next().next().get(0).innerHTML = content;
				//显示或隐藏子级内容
				if($(me).attr("src") == "image/plus.gif") {
					$(me).attr("src","image/minus.gif");
					$(me).next().next().next().next().next().slideDown("fast");
				} else {
					$(me).attr("src","image/plus.gif");
					$(me).next().next().next().next().next().slideUp("fast");
				}
					//加载类型完毕，设置为true
					isFinishAddType = true;
			
			var addCountInterval = setInterval(function(){
				//加载类型已完毕，加载数量
				if(isFinishAddType) {
					//获取该节点下各类型的节点数量
					$(me).next().next().next().next().next().children().children().each(function(index){
						//获取子类型下的li节点
						var liObj = $(this);
					
						//获取需要查找的类型
						var searchType = $(this).children().eq(2).val();
						//alert(parentEntityType);
						var params = {parentEntityType:parentEntityType,parentEntityId:parentEntityId,searchType:currentEntityType,addLink:''}
						
							$.post("../restree/getChildEntityByTypeUniversalRecursionAction",params,function(data){
								//加载对应类型的数量
								var operatedObj = liObj.children().eq(1);
								var countIndex = (operatedObj.html()).indexOf("(...)");
								var firstContent = operatedObj.html();
								
								if(data == "0") {
									//数量为0时隐藏小加号
									liObj.children().eq(0).css("visibility","hidden");
									
								}
								operatedObj.html(firstContent.substring(0, countIndex) + "("+data+")");
								
							},'json');
					});
					
					isFinishAddType = false;
					//跳出判断是否加载类型完毕的循环
					clearInterval(addCountInterval);
				}
			},10);
			}
			}
		
		
		//加载并显示子级entity信息
		function showChildEntityMasg(me) {
			//内容信息已打开，直接隐藏该层，不作任何操作
			if($(me).attr("src") == "image/minus.gif") {
				$(me).attr("src","image/plus.gif");
				$(me).next().next().next().next().slideUp("fast");
				
				return;
			}
			var url = "loadResourceEntityByAreaAction"; //获取子级entity
			//获取父级节点的类型
			var parentEntityType = $(me).parent().parent().parent().prev().val();
			var parentEntityId = $(me).parent().parent().parent().prev().prev().val();
			//被查找的类型(即当前节点的类型)
			var currentEntityType = $(me).next().next().next().val();
			var params = {areaType:parentEntityType,areaId:parentEntityId,selectResType:currentEntityType};
			$.post(url,params,function(data){
				var content = "";
				$.each(data, function(index, obj){
					var entityName = "";
					var entityType = $(me).next().next().next().next().val();
					if(obj.name != null) {
						entityName = obj.name;
					} else {
						entityName = obj.label;
					}
					
					var imgContent = "";
					if(obj._entityType == "Sys_Area") {
						imgContent = "<img src='image/plus.gif' onclick='showChildTypeMasg(this);' />";
					} else {
						imgContent = "<img src='image/plus.gif' onclick='showChildTypeMasg(this);' style='visibility:hidden;' />";
					}
					
					content += "<li>" 
						+ imgContent 
						+ "<input name='rdoChooseResChild' type='checkbox' style=' margin-top: -1px; vertical-align: middle;' value='" + obj.id + "' />" 
						+ "<span onclick='chooseEntitys(this);' style='padding-left:5px;cursor:pointer;'>" + entityName + "</span>" 
						+ "<input type='hidden' value=" + obj.id + " />" 
						+ "<input type='hidden' value=" + obj._entityType + ">" 
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
				//通过缓存保存节点信息
					$("#logicalreshidEntityFlush").data(parentEntityType + "_" + parentEntityId + "_" + currentEntityType, content);
			},'json');
			}
		
		
		
		
		function getChildEntityMasgForSearch(me){
			var url = "loadResourceEntityByAreaAction"; //获取子级entity
			//获取父级节点的类型
			var parentEntityType = $(me).parent().parent().parent().prev().val();
			var parentEntityId = $(me).parent().parent().parent().prev().prev().val();
			//被查找的类型(即当前节点的类型)
			var currentEntityType = $(me).next().next().next().val();
			var params = {areaType:parentEntityType,areaId:parentEntityId,selectResType:currentEntityType};
			$.ajax({
					url:url,
					async:false,
					data:params,
					dataType:'json',
					type:'post',
					success:function(data){
							var content = "";
				$.each(data, function(index, obj){
					var entityName = "";
					var entityType = $(me).next().next().next().next().val();
					if(obj.name != null) {
						entityName = obj.name;
					} else {
						entityName = obj.label;
					}
					
					var imgContent = "";
					if(obj._entityType == "Sys_Area") {
						imgContent = "<img src='image/plus.gif' onclick='showChildTypeMasg(this);' />";
					} else {
						imgContent = "<img src='image/plus.gif' onclick='showChildTypeMasg(this);' style='visibility:hidden;' />";
					}
					
					content += "<li>" 
						+ imgContent 
						+ "<input name='rdoChooseResChild' type='checkbox' style=' margin-top: -1px; vertical-align: middle;' value='" + obj.id + "' />" 
						+ "<span onclick='chooseEntitys(this);' style='padding-left:5px;cursor:pointer;'>" + entityName + "</span>" 
						+ "<input type='hidden' value=" + obj.id + " />" 
						+ "<input type='hidden' value=" + obj._entityType + ">" 
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
				//通过缓存保存节点信息
					$("#logicalreshidEntityFlush").data(parentEntityType + "_" + parentEntityId + "_" + currentEntityType, content);
			
					}
				})
		
		}
		
		//逻辑网搜索(暂定为一级搜索)
	 	function logicalresSearch()
	 	{
			$(".logicalressearchtab").hide();
				var chosenEntityType = $("#logicalresChosenParentEntityType").val();
				var chosenEntityId = $("#logicalresChosenParentEntityId").val();
				var addedResEntityType = $("#logicalresAddedResEntityType").val();
				//获取需要进行搜索的关键字
				var kewWords = $("#logicalresTxtSearch").val();
				//没有任何搜索内容，或者依然为灰色的请输入时，进行搜索内容输入提示
				if($.trim(kewWords) == "" || (($.trim(kewWords)=="资源分类搜索"||$.trim(kewWords)=="资源树搜索") && ($("#logicalresTxtSearch").css("color") == "#999" || $("#logicalresTxtSearch").css("color") == "rgb(153, 153, 153)"))) {
					alert("请输入搜索内容!");
					return false;
					
				}	
				if(logicalressearchMethod=="part"){
					if(addedResEntityType!=""){
						
						var addedResEntityType = $("#logicalresAddedResEntityType").val();
						var addedResParentEntityType = $("#logicalresAddedResParentEntityType").val();
						var addedResParentEntityId = $("#logicalresAddedResParentEntityId").val();
				/*		if(addedResEntityType == "" || addedResParentEntityType == "" || addedResParentEntityId == "") {
							alert("请选择要进行搜索的类型起始节点!");
							return false;
						}*/
						//获取类型前的小加号图片
						var typeImg = $("#divDisplay span.chosenClass").parent().children().eq(0);
						if(typeImg.css("visibility") == "hidden") {
							//不存在小加号图片，即该类型下并无entity内容，则不在搜索处理
							return false;
						}
						//获取搜索的类型起始节点下的内容div
						var entityContentDiv = $("#divDisplay span.chosenClass").parent().children().eq(4);
						
						if(entityContentDiv.children().length == 0) {
							//不存在内容的情况，需要进行加载
							
							showChildEntityMasg(typeImg);
							
							
						}
						
						//令子级entity信息加载完成后，才进行关键字高亮设置
						var searchInterval = setInterval(function(){
							if(entityContentDiv.children().length > 0) {
								//展开子级entity信息后，找到与搜索匹配项的，进行关键字高亮设置
								$("#divDisplay span:[class!='chosenClass']").css("background", "");
								$("#divDisplay em").css("color","");
								var flag = false;
								var searchIndex=0;//标记找到的第一个值
								var topValue=0;//找到的第一个值元素的位置top值
								entityContentDiv.children().children().each(function(index){
									//找到entity的span，进行高亮设置
									var choose = $(this).children().eq(2);
									if(choose.text().toString().indexOf($.trim(kewWords))>=0){
										flag=true;
									}
									if(choose.text().toString()==$.trim(kewWords)){
										//alert("ok");
										searchIndex++;
										if(searchIndex==1){//标记第一个找到的值
											
											//alert($("#treeRoot").scrollTop());
											if($("#tree-ul").scrollTop()!=0){//滚动条回到顶部
												$("#tree-ul").scrollTop(0);
											}
											topValue = $(this).position().top;
											chooseEntitys(choose);
										}else{
											choose.css("background","#FFFF88");
										}
										
										
										
									}else if(choose.text().toString()!=$.trim(kewWords)&&choose.text().toString().indexOf($.trim(kewWords))>=0){
										searchIndex++;
										if(searchIndex==1){
											if($("#tree-ul").scrollTop()!=0){
												$("#tree-ul").scrollTop(0);
											}
											topValue = $(this).position().top;
											chooseEntitys(choose);
										}
										
										choose.highlight($.trim(kewWords), {needUnhighlight: true});
										
									}
									
								});
								if(flag==false){
									alert("没有匹配的数据.\r\n请修改查询条件 或 修改查询的起点后重新查询.");
								}else{
									topValue =topValue-150;//hardcode 使找到的内容相对居中聚焦
									setTimeout(function(){$("#tree-ul").scrollTop(topValue);},100);
		
								}
								//小加号图片变成小减号
								typeImg.attr("src","image/minus.gif");
								//展现entity内容div
								entityContentDiv.show();
								
								clearInterval(searchInterval);
							}
						},10);
				
				}else{
						
						//获取实例前的小加号图片
						var eImg = $("#divDisplay span.chosenClass").parent().children().eq(0);
						if(eImg.css("visibility") == "hidden") {
							//不存在小加号图片，即该类型下并无entity内容，则不在搜索处理
							return false;
						}
						//获取搜索的实例起始节点下的内容div
						var typeContentDiv = "";
						if($("#divDisplay span.chosenClass").parent().children().size()==6){
							typeContentDiv = $("#divDisplay span.chosenClass").parent().children().eq(5);
						}else{
							typeContentDiv = $("#divDisplay span.chosenClass").parent().children().eq(4);
						}
						
						if(typeContentDiv.children().length == 0) {
							showChildTypeMasg(eImg);
						}else{
							typeContentDiv.show();
							if(eImg.attr("src")=="image/plus.gif"){
								eImg.attr("src", "image/minus.gif");
						    }
						}
						var addedResParentEntityType = chosenEntityId;
						var addedResParentEntityId = chosenEntityType;
						var interval = setInterval(function(){
							var lis = typeContentDiv.children().eq(0).children("li");
							if(lis.length>0){
								var searchFlag=false;
								var endFlag= false;
								$("#divDisplay span:[class!='chosenClass']").css("background", "");
								$("#divDisplay em").css("color","");
								var liIndex=0;				
								lis.each(function(){
									liIndex++;
									var addedResEntityType = $(this).children().eq(3).val();
									var typeImg = $(this).children().eq(0);
									if(typeImg.css("visibility") != "hidden") {
									//获取搜索的实例起始节点下的内容div
											var entityContentDiv ="";
											if($(this).children().size()==6){
												entityContentDiv = $(this).children().eq(5);
											}else{
												entityContentDiv = $(this).children().eq(4);
											}
					
											if(entityContentDiv.children().length == 0) {
												//不存在内容的情况，需要进行加载
												getChildEntityMasgForSearch(typeImg);
																							
											}
											
										if(!searchFlag){
											
										
											//令子级entity信息加载完成后，才进行关键字高亮设置
										//	var searchInterval = setInterval(function(){
												if(entityContentDiv.children().length > 0) {
													//展开子级entity信息后，找到与搜索匹配项的，进行关键字高亮设置
													
													var flag = false;
													var searchIndex=0;//标记找到的第一个值
													var topValue=0;//找到的第一个值元素的位置top值
													entityContentDiv.children().children().each(function(index){
														//找到entity的span，进行高亮设置
														var choose = $(this).children().eq(2);
														if(choose.text().toString().indexOf($.trim(kewWords))>=0){
															flag=true;
															
															searchFlag=true;
														}
														if(choose.text().toString()==$.trim(kewWords)){
															//alert("ok");
															searchIndex++;
															if(searchIndex==1){//标记第一个找到的值
																//alert($("#treeRoot").scrollTop());
																if($("#tree-ul").scrollTop()!=0){//滚动条回到顶部
																	$("#tree-ul").scrollTop(0);
																}
																topValue = $(this).position().top;
																chooseEntitys(choose);
															}else{
																choose.css("background","#FFFF88");
															}
															
															//choose.css("background","#FFFF88");
														}else if(choose.text().toString()!=$.trim(kewWords)&&choose.text().toString().indexOf($.trim(kewWords))>=0){
															searchIndex++;
															if(searchIndex==1){
																if($("#tree-ul").scrollTop()!=0){
																	$("#tree-ul").scrollTop(0);
																}
																topValue = $(this).position().top;
																chooseEntitys(choose);
															}
															
															choose.highlight($.trim(kewWords), {needUnhighlight: true});
														}
														
													});
													if(flag==false){
														//alert("没有匹配的数据.\r\n请修改查询条件 或 修改查询的起点后重新查询.");
														entityContentDiv.hide();
														typeImg.attr("src", "image/plus.gif");
														
													}else{
														topValue =topValue-150;//hardcode 使找到的内容相对居中聚焦
														setTimeout(function(){$("#treeRoot").scrollTop(topValue);},100);
														entityContentDiv.show();
														typeImg.attr("src", "image/minus.gif");
													}
											//		clearInterval(searchInterval);
													
												}	
										//	},10);
										}	
									}
									if(liIndex==lis.length){
										
											if(searchFlag==false){
												alert("没有匹配的数据.\r\n请修改查询条件 或 修改查询的起点后重新查询.");
											}
										
										
									}
									
								})
								
								clearInterval(interval);
							}
						},1000)
						
				}
				}else{
					var chosenType = $("#selectType").val()+"";
					chosenType = chosenType.substring(0,chosenType.indexOf(","))
					var areaId = $("#areaId").val(); //区域id
					searchResouceRecursion("selectRangeResChosenType",chosenType,areaId,kewWords);
					
					
				}	
				
				
				}
				
				
				//搜索栏"请输入"提示控制
				function txtSearchFocus()
				
				{
				//已更改为黑色字，不作清除内容操作
				if($("#logicalresTxtSearch").css("color") == "#000" || $("#logicalresTxtSearch").css("color") == "rgb(0, 0, 0)") {
					return false;
				}
				$("#logicalresTxtSearch").val("");
				//focus后，字体更改为黑色
				$("#logicalresTxtSearch").css("color","#000");
				}
				
			 //点击所选择的实体
	function chooseEntitys(me) {
		$("#divDisplay span").css("background", "");
		$("#divDisplay span").attr("class", "");
		$(me).css("background", "#E0EEEE");//选中的实体颜色
		$(me).attr("class","chosenClass");//选中的实体增加一个选中的class属性
		//判断有无出现单选控件
		var currentEntityType="";
		var currentEntityId="";
		
		currentEntityId = $(me).next().val();//当前节点的id
		currentEntityType = $(me).next().next().val();//当前节点的类型
		

		$("#logicalresChosenParentEntityType").val(currentEntityType);
		$("#logicalresChosenParentEntityId").val(currentEntityId);
		//获取被添加物理资源类型的父entity类型
		$("#logicalresAddedResParentEntityType").val("");
		$("#logicalresAddedResParentEntityId").val("");
		//获取被添加物理资源类型
		$("#logicalresAddedResEntityType").val("");
		
	}		
				
				//点击所选择的类型
		function logicalresChooseType(me) {
			$("#divDisplay span").css("background", "");
			$("#divDisplay span").attr("class", "");
			$(me).css("background", "#E0EEEE");//选中的实体颜色
			$(me).attr("class","chosenClass");//选中的实体增加一个选中的class属性
			//点击类型时，清空暂时保存的entity的信息，确保在编辑entity时所获取的是被点击的entity的信息
			$("#logicalresChosenParentEntityType").val("");
			$("#logicalresChosenParentEntityId").val("");
			//获取当前选中的类型信息
			var addedResEntityType = $(me).next().next().val();
			var addedResParentEntityType = $(me).parent().parent().parent().prev().val();
			var addedResParentEntityId = $(me).parent().parent().parent().prev().prev().val();
			//获取被添加物理资源类型的父entity类型
			$("#logicalresAddedResParentEntityType").val(addedResParentEntityType);
			$("#logicalresAddedResParentEntityId").val(addedResParentEntityId);
			//获取被添加物理资源类型
			$("#logicalresAddedResEntityType").val(addedResEntityType);
			/*alert($("#logicalresAddedResParentEntityType").val());
			alert($("#logicalresAddedResParentEntityId").val());
			alert($("#logicalresAddedResEntityType").val());*/
		}		
				
		//点击所选择的类型
	/*	function logicalresChooseType(me) {
			$("#divDisplay span").css("background", "");
			$("#divDisplay span").attr("class", "");
			$(me).css("background", "#0099FF");//选中的实体颜色
			$(me).attr("class","chosenClass");//选中的实体增加一个选中的class属性
			
			//点击类型时，清空暂时保存的entity的信息，确保在编辑entity时所获取的是被点击的entity的信息
			$("#chosenEntityType").val("");
			$("#chosenEntityId").val("");
			//获取当前选中的类型信息
			var addedResEntityType = $(me).next().next().val();
			var addedResParentEntityType = $(me).parent().parent().parent().prev().val();
			var addedResParentEntityId = $(me).parent().parent().parent().prev().prev().val();
			//获取被添加物理资源类型的父entity类型
			$("#logicalresAddedResParentEntityType").val(addedResParentEntityType);
			$("#logicalresAddedResParentEntityId").val(addedResParentEntityId);
			//获取被添加物理资源类型
			$("#logicalresAddedResEntityType").val(addedResEntityType);
			/*alert($("#logicalresAddedResParentEntityType").val());
			alert($("#logicalresAddedResParentEntityId").val());
			alert($("#logicalresAddedResEntityType").val());*/
		//}
		
		//隐藏树形资源选择层
		function divDisplayClose(){
			$("#divDisplay").hide();
			//获取被添加物理资源类型的父entity类型
			$("#logicalresAddedResParentEntityType").val("");
			$("#logicalresAddedResParentEntityId").val("");
			//获取被添加物理资源类型
			$("#logicalresAddedResEntityType").val("");
		}
		
		//通过树形添加资源到需要统计的资源列表
		function divDisplayHide(){
			$("#divDisplay").hide();
			//$("#selectEntity").html("");
			var selectResourceSpan = "";
			$("#tree-ul input[name = 'rdoChooseResChild']").each(function(){
				if($(this).attr("checked")){
				var value =  $(this).next().next().val();
				var title = $(this).next().next().next().val();
				selectResourceSpan = selectResourceSpan + $(this).next().text() + ",";
				var con = 0;
					$("#selectEntity option").each(function(){
						if($(this).val() == value && $(this).attr("title") == title ){
							 con++;
						}
					});
					if(con == 0){
						var countext = " <option onclick='optionClick(this)' value='"+$(this).next().next().val()+"' title='"+$(this).next().next().next().val()+"' id='"+$(this).next().next().val()+"'>"+$(this).next().text()+"</option>";
						$("#selectEntity").html($("#selectEntity").html() + countext);
					}
				}
			});
				if(selectResourceSpan != null && selectResourceSpan != ""){
					selectResourceSpan = selectResourceSpan.substring(0,selectResourceSpan.lastIndexOf(","));
					selectResourceSpan = " "+selectResourceSpan+" ";
				}
			$("#selectResourceSpan").text(selectResourceSpan);
			//获取被添加物理资源类型的父entity类型
			$("#logicalresAddedResParentEntityType").val("");
			$("#logicalresAddedResParentEntityId").val("");
			//获取被添加物理资源类型
			$("#logicalresAddedResEntityType").val("");
			selectResourceDisabled();
		}
		
		//清除DIV内容
		function cleanDiv(){
			$("#checkAll").removeAttr("checked");
			$("#selectResource").attr("disabled");
			$("#condition").show();
			$("#selectEntity").html("");
			$("#selectDiv").html("");
			$("#treediv").html("");
			$("#treeImg").attr("src","image/plus.gif");
			$("input[name='select_ziyuan']").removeAttr("checked");
			$("#radio_child").click();
			$("#selectResourceSpan").val("(什么范围?)");
			$("#selectTypeSpan").val("(什么资源?)");
			loadResourceEntity();
		}
		
		
   		//切换图-表排列方式
		function changeFloat(me){
			var down_top = $("#down_top").html();
		 	var cFloat = $(me).val();
		 	var countext = "<div id='down_top' style='height: 320px;'></div>"
       						+"<div id='down_down'>"
       						+"<div id='down_down1'></div>"
   			    			+"<div id='down_down2' style='display: none'><input type='button' value='导出' id='btnExportRes' onclick='getExportRes();' style='float:right; width:60px; margin-right: 7px;' /></div>"
        					+"</div>";
        	$("#down_de").html(countext);
		 	if(cFloat == 'left-right'){
		 		$("#down_top").attr("class","down_top_left-right");
		 		$("#down_down").attr("class","down_down_left-right");
		 	}else if(cFloat == 'right-left'){
		 		$("#down_top").attr("class","down_top_right-left");
		 		$("#down_down").attr("class","down_down_right-left");
		 	}else if(cFloat == 'up-down'){
		 		$("#down_top").attr("class","down_top_up-down");
		 		$("#down_down").attr("class","down_down_up-down");
		 	}else if(cFloat == 'down-up'){
		 		countext = "<div id='down_down'>"
		 					+"<div id='down_down1'></div>"
   			    			+"<div id='down_down2' style='display: none'><input type='button' value='导出' id='btnExportRes' onclick='getExportRes();' style='float:right; width:60px; margin-right: 7px;' /></div>"
       					  	+"</div>"
       						+"<div id='down_top' style='height: 320px;'></div>";
		 		$("#down_de").html(countext);
		 		$("#down_top").attr("class","down_top_down-up");
		 		$("#down_down").attr("class","down_down_down-up");
		 	}
		 	if(down_top != ""){
				selectResource();
			}
		 }
		 
    //隐藏筛选层
    function divDisplay1hide(){
    	$("#divDisplay1").hide();
    }
    
    //对资源详细信息进行筛选
    function selectExtjsGrid(){
    	var data = $("#extjsGridData").val();
    	/*筛选条件*/
		var conditions = "";
		var column = "";
		var columnValue = "";
    	conditions = $("#conditions").val();
    	column = $("#select_column").val();
    	columnValue = $("#column_value").val();
    	$("#DownLoadForm_conditions").val($("#conditions").val());
    	$("#DownLoadForm_column").val($("#select_column").val());
    	$("#DownLoadForm_columnValue").val($("#column_value").val());
    	createExtjsGrid(true,column,columnValue,conditions,data);
    	$("#divDisplay1").hide();
    }
    
    
    function sttingEffect(me){
    	if($(me).attr("checked")){
    		$("#effect_settings_div").show();
    	}else{
    		$("#effect_settings_div").hide();
    	}
    }
    
    function checkAll(me){
    	if($(me).attr("checked")){
    		$("#selectDiv input[type='checkbox']").attr("checked","checked");
    	}else{
    		$("#selectDiv input[type='checkbox']").removeAttr("checked");
    	}
    	selectTypeSpan();
    	selectResourceDisabled();
    }
    //搜索切换
	function showLogicalresSearchTab(){
		var check1 = $("#logicalresallCheckBox").attr("checked");
		if(check1=='checked'){
			$("li[id='logicalresallLi']").hide();
			$("li[id='logicalrespartLi']").show();
		}else{
			$("li[id='logicalresallLi']").show();
			$("li[id='logicalrespartLi']").hide();
		}
		$(".logicalressearchtab").show();
	}
	function hideTab(event){
		oElement = document.elementFromPoint(event.pageX,event.pageY);
		if($(oElement).attr("class")!="logicalressearchtab" && $(oElement).attr("id")!="searchSpan" && $(oElement).attr("id")!="logicalresTxtSearch"){
			$("div[class='logicalressearchtab']").hide();
		}
	}
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

	var selectRangeResChosenType=chosenType;
	params={selectRangeResChosenType:selectRangeResChosenType,areaId:areaId,searchConditionText:searchConditionText,searchConditionType:searchConditionType}
	
	
	$.post(url,params,function(data){
		if(data!=null&&data!=""){
			var content="";
			$.each(data,function(index,value){
				if(value.name!=undefined){
					var stringObj=value.name;    
          		    var newstr=stringObj.replace(searchConditionText,"<em style='color:red'>"+searchConditionText+"</em>"); 
					content +="<li><span class='aetg_text' onclick='chooseResourceForChildResource(this)'>"+newstr+"</span><input type='hidden' value='"+value.name+"'/><input type='hidden' value='"+chosenTypeName+"'/><input type='hidden' value='"+value.id+"'/><input type='hidden' value='"+value._entityType+"'/><span class='aetg_link'><a  href=\"javascript:showBasicResourceInfo('"+value.id+"','"+value._entityType+"')\">基本信息</a><a href='../physicalres/getPhysicalresForOperaAction?currentEntityType="+value._entityType+"&currentEntityId="+value.id+"&areaId="+areaId+"&modelType=view' target='_blank'>详细</a></span></li>";
				}else{
					var stringObj=value.label;    
          		    var newstr=stringObj.replace(searchConditionText,"<em style='color:red'>"+searchConditionText+"</em>"); 
					content +="<li><span class='aetg_text' onclick='chooseResourceForChildResource(this)'>"+newstr+"</span><input type='hidden' value='"+value.label+"'/><input type='hidden' value='"+chosenTypeName+"'/><input type='hidden' value='"+value.id+"'/><input type='hidden' value='"+value._entityType+"'/><span class='aetg_link'><a  href=\"javascript:showBasicResourceInfo('"+value.id+"','"+value._entityType+"')\">基本信息</a><a href='../physicalres/getPhysicalresForOperaAction?currentEntityType="+value._entityType+"&currentEntityId="+value.id+"&areaId="+areaId+"&modelType=view' target='_blank'>详细</a></span></li>";
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

//选择查询出来的资源
function chooseResourceForChildResource(me){	
		//记录选择的起点资源信息
		var currentEntityName=$(me).next().val();
		var currentEntityId = $(me).next().next().next().val();
		var currentEntityType = $(me).next().next().next().next().val();
		var con = 0;
		$("#selectEntity option").each(function(){
			if($(this).val() == currentEntityId && $(this).attr("title") == currentEntityType ){
				 con++;
			}
		});
		if(con == 0){
			var countext = " <option onclick='optionClick(this)' value='"+currentEntityId+"' title='"+currentEntityType+"' id='"+currentEntityId+"'>"+currentEntityName+"</option>";
			$("#selectEntity").html($("#selectEntity").html() + countext);
		}
		$("#divDisplay").hide();
		$(".aetg").hide();
		$("#logicalresChosenParentEntityType").val("");
		$("#logicalresChosenParentEntityId").val("");
		$("#logicalresAddedResParentEntityType").val();
		$("#logicalresAddedResParentEntityId").val();
		$("#logicalresAddedResEntityType").val();
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
$(document).keydown(function(event){ //按ESC键退出弹出窗口
			if(event.keyCode==27){
				$("#divDisplay").hide();
				$(".aetg").hide();
				$("#logicalresChosenParentEntityType").val("");
				$("#logicalresChosenParentEntityId").val("");
				$("#logicalresAddedResParentEntityType").val();
				$("#logicalresAddedResParentEntityId").val();
				$("#logicalresAddedResEntityType").val();
			}
		}); 
		
		
//导出资源信息(excel)
function getExportRes(){
		/*var sheetHeadMap = bigData.titleMap;
		var searchResourceMapList = conList;
		var params = {sheetHeadMap:sheetHeadMap,searchResourceMapList:searchResourceMapList}
		$.post("exportBizunitRelationAction",params,function(){});*/
		var currentType = $("#selectType").val();//当前节点的类型
			var strs= new Array(); //定义一数组
			strs = currentType.split(",");
			var resourceType = strs[0];
			$("#selectEntityHidden").val(resourceType);
		//if($("#recursiveCheckbox").attr("checked")){
			 //alert("递归");
		//}
		var basicRoStacked = "";
		if($("#effect_settings").attr("checked") == "checked"){
			$("input[name='duibituxiaoguo']").each(function(){
				if($(this).attr("checked")){
					 //alert($(this).val());
					 basicRoStacked = $(this).val();
				}
			});
		}else{
			basicRoStacked = "basic";
		}
		var entityId = "";
		var entityName= new Array(); //定义一数组
		var entityType = "";
		var i = 0;
		var selectEntityCountext = "";
		$("#selectEntity option").each(function(){
				 if($(this).val()){
				 	if(entityId == ""){
				 		entityId = $(this).val();
				 	}else{
				 		entityId = entityId + "," + $(this).val();
				 	}if(entityType == ""){
				 		entityType = $(this).attr("title");
				 	}else{
				 		entityType = entityType + "," + $(this).attr("title");
				 	}
				 }
	 		entityName[i] = $(this).text();
	 		var select = $(this).text();
	 		var selectStrs = "";
	 		if(select.indexOf(")") == 5){
						selectStrs = select.substring(0,select.indexOf("(")-3);
					}
	 		else if(select.indexOf(")")){
						selectStrs = select.substring(select.indexOf(")")+1);
					}else{
						selectStrs = select;
					}
	 		i++;
		});
		//alert(entityName);
		var selectResType = "";
		var selectDivCountext = "";
		$("#selectDiv input[name='selectType']").each(function(){
				 if($(this).attr("checked")){
				 if(selectResType == ""){
				 		selectResType = $(this).val();
				 	}else{
				 		selectResType = selectResType + "&_&" + $(this).val();
				 	}
				 	
					var selectStrsId = "";
					//alert($(this).parent().text());
					if($(this).parent().text().indexOf(")") == 5){
						selectStrsId = $(this).parent().text().substring(0,$(this).parent().text().indexOf(")")-3);
					}
					else if($(this).parent().text().indexOf(")")){
						selectStrsId = $(this).parent().text().substring($(this).parent().text().indexOf(")")+1);
					}else{
						selectStrsId = $(this).parent().text();
					}
			}
		});
		//导出数据的action
    	$("#searchForm_entityId").val(entityId);
    	$("#searchForm_selectResType").val(selectResType);
    	$("#searchForm_currentType").val(entityType);
    	$("#searchForm_allLink").val(allLink);
    	$("#searchForm_tableTitle").val("类型/"+$("#selectType").find("option:selected").text());
    	
		$("#searchForm").attr("action", "downLoadResourceTotalAction");
		document.getElementById("searchForm").submit();
	};
	
	function continueOperate(){
		isSelect = true;
		selectResource();
		$("#confirm_div_warning").hide();
	}
	
	function confirm_div_warning_show(){
		$("#confirm_div_warning").hide();
	}
	
	function confirm_div_wrong_show(){
		$("#confirm_div_wrong").hide();
	}
	
	function selectTypeSpan(){
		var selectTypeSpan = "";
		$("#selectDiv input[name='selectType']").each(function(){
				 if($(this).attr("checked")){
				 	selectTypeSpan = selectTypeSpan + $(this).parent().attr("title") + ",";
				 }
		});
		if(selectTypeSpan != null && selectTypeSpan != null){
			selectTypeSpan = selectTypeSpan.substring(0,selectTypeSpan.lastIndexOf(","));
			selectTypeSpan = " " + selectTypeSpan + " ";
			$("#selectTypeSpan").text(selectTypeSpan);
		}else{
			$("#selectTypeSpan").text("(什么资源?)");
		}
		selectResourceDisabled();
	}
	
	
	function selectResourceDisabled(){
		var count = 0;
		$("#selectDiv input[name='selectType']").each(function(){
				 if($(this).attr("checked")){
				 	count++;
				 }
		});
		if($("#selectEntity option").size() > 0 && count > 0){
			$("#selectResource").removeAttr("disabled");
			$("#condition").hide();
		}else{
			$("#selectResource").attr("disabled","disabled");
			$("#condition").show();
		}
	}
</script>
</head>

<body>
	<%-- 用来逻辑网保存树内容查询信息 --%>
<%-- 用来逻辑网保存树内容查询信息 --%>
<input type="hidden" id="repertType" value="${repertType }"/>
<input type="hidden" id="logicalresChosenParentEntityType" value=""/>
<input type="hidden" id="logicalresChosenParentEntityId" value=""/>
<input type="hidden" id="logicalresAddedResParentEntityType" value=""/>
<input type="hidden" id="logicalresAddedResParentEntityId" value=""/>
<input type="hidden" id="logicalresAddedResEntityType" value=""/>
	<input type="hidden" id="areaId" value="${areaId }"/>
	<input type="hidden" id="areaType" value="${areaType }"/>
    <input type="hidden" id="repertTypeareaName" value="${areaName }"/>
	<input type="hidden" id="selectResTypeHidden" value=""/>
	<input type="hidden" id="selectEntityHidden" value=""/>
	<input type="hidden" id="keyValues" value=""/>
	<input type="hidden" id="extjsGridData" value=""/>
	
	
	<div id="TypeDiv">
                <s:iterator value="allTypeMap.GeneralBaseStation" id="allMap">
                <input type="hidden" name="GeneralBaseStation" value="${allMap }"/>
                </s:iterator>
                <s:iterator value="allTypeMap.GeneralRoomContainer" id="allMap">
                <input type="hidden" name="GeneralRoomContainer" value="${allMap }"/>
                </s:iterator>
                </div>
	<div id="up">
    	<div id="up_top" class="show_top_a" style="cursor:pointer;"  title="统计设置:统计范围、统计目标、结果展示方式">
        	<a href="#" style="color:#000; text-decoration:none;">统计设置 ▼</a>
        </div>
        <div id="divDisplay" style="">
		       <div  style="padding:3px;background:#eee;">
			        <span id="searchSpan" onmouseout="hideTab(event);">  <input style="color:#999;" id="logicalresTxtSearch" type="text" style="width:150px;" value="资源分类搜索" onfocus="txtSearchFocus();" />&nbsp;
			       <input id="logicalresBtnSearch" type="button" value="搜索" onclick="logicalresSearch();" onmouseover="showLogicalresSearchTab();" onmouseout="hideTab(event);" />
		       	 </span></div>
		   <div>
		       <ul id="tree-ul" style="overflow:auto;width:99%;margin:0 auto;padding-left:5px; height:200px; margin: 0px;top:1000px;">
			        <li>
			    		<img onclick="showChildTypeMasg(this);" src="image/plus.gif" id="treeImg">
			    		<input name='rdoChooseResChild' id="spanCheckBox" type='checkbox' style=' margin-top: -1px; vertical-align: middle;' value="${areaId}" />
			    		<span id='logicalresTreeRootName' onclick="chooseEntitys(this);" class="entityClass" style="cursor:pointer;">${areaName}</span>
			    		<input type="hidden" value="${areaId}">
			    		<input type="hidden" value="${areaType}">
			    		<div style="padding-left: 10px;" id="treediv">
			    		</div>
		    		</li>
			    </ul>
		    </div>
		    <div class="container-bottom" style="text-align:center;padding:3px 0;">
			  <input type="button" value="确定" onclick="divDisplayHide();"/>&nbsp;&nbsp;
   			  <input type="button" value="取消" onclick="divDisplayClose();"/>
	     	</div>
	     	 <div style="display:none" class="aetg" >
				<ul id="rd">										
				</ul>
			</div>
		    <div class='logicalressearchtab' style='display:none'  > 
		        	<ul>
		        	<li id='logicalresallLi' style='display:none'>
		        		&nbsp;<input type='checkbox' id='logicalresallCheckBox'  checked/> 资源分类搜索
		        	</li>
		        	<li  id='logicalrespartLi'>
		        		&nbsp;<input type='checkbox' id='logicalrespartCheckBox' /> 资源树搜索
		        	</li>
		        	</ul>
	        </div>
	        <%--信息浮窗--%>
			<div class="dialog">
				<div class="dialog_black"></div>
				<div id="dialog" class="dialog_box" style="display:none;">
					<div class="dialog_header">
						<div class="dialog_title">资源基本信息</div>
						<div class="dialog_tool">
						   <div class="dialog_tool_close dialog_hide"></div>
						</div>
					</div>
					<div class="dialog_content">
						
					</div>
				</div>
			</div>	
       </div>
       </div>
        <div id="down2">
            <div id="top_tool_bar" style="margin-left:17px">
	      		<%-- 区域选择 --%>
                            	<input type="hidden" id="select_area_id" />
				            	<div class="toolBar_area" style="white-space:nowrap;">选择区域：
				            		<em id="" class="city_title">
				            			<a title="选择城市" class="city_expand city_show" id="city_select" href="javascript:void(0)">请选择</a>
										<a title="选择城区" class="city_expand district_show" style="display:none;" id="district_select" href="javascript:void(0)">请选择</a>
										<a title="选择街道" class="city_expand street_show" style="display:none;" id="street_select" href="javascript:void(0)">请选择</a>
				            		</em>
				                </div>
				                <div class="map_city" style="z-index:10001;top: 42px;">
									<div class="city_main">
										<div class="title">
											<em>城市列表</em>
											<a href="javascript:void(0)" title="关闭" class="close_icon city_close"></a>
										</div>
										<div class="sel_city clearfix">
											<div class="sel_city_top">
												<div class="sel_city_default">
													<p>
														<span>当前城市：<em id="now_choice_city"></em></span>
														<%-- 
														<a href="javascript:void(0);" class="def_city">设为默认城市</a>
														<span class="default_info">您默认的城市是：<em>广州市</em>[<a href="javascript:void(0);">删除</a>]</span>
														 --%>
													</p>
												</div>     
												<div class="sel_city_searchbar">
													<div class="btnbar">
														<span id = "" class="sel_city_btnl sel_city_btnl_sel" href="javascript:void(0)">按省份</span>
														<span class="sel_city_btnl" href="javascript:void(0)">按城市</span>
													</div>
													<div class="search_city_form">
														<input type="text" id="search_city_input" name="" class="sel_city_searchinput">
														<span id="search_btn" class="sel_city_btn_submit">搜索</span>
													</div>
												</div>
												<div class="sel_city_letterbar" id="first_letter_choice_div">
													
												</div>
											</div>
											<div class="sel_city_result">
												<table id="province_table" cellspacing="0" cellpadding="0">
													<tbody>
														<%-- 城市列表 --%>
													</tbody>
												</table>
											</div>
										</div>
									</div>
								</div>
								<div class="map_district" style="z-index:10001;top: 42px;">
									<div class="city_main">
										<div class="title">
											<em>城区列表</em>
											<a href="javascript:void(0)" title="关闭" class="close_icon district_close"></a>
										</div>
										<div class="sel_city_default">
											<p>
												<span>当前城区：<em id="now_choice_district"></em></span>
											</p>
										</div> 
										<div class="sel_district_hotcity">
											<%-- 区 --%>
										</div>
									</div>
								</div>
								<div class="map_street" style="z-index:10001;top: 42px;">
									<div class="city_main">
										<div class="title">
											<em>街道列表</em>
											<a href="javascript:void(0)" title="关闭" class="close_icon street_close"></a>
										</div>
										<div class="sel_city_default">
											<p>
												<span>当前街道：<em id="now_choice_street"></em></span>
											</p>
										</div> 
										<div class="sel_street_hotcity">
											<%-- 街 --%>
										</div>
									</div>
								</div>
	        </div>
            <div style="width:98%; margin:0 auto;padding:5px;">
            	<div class="inline vt">
                	<h4>1.统计范围,按
                    	<select id="selectType" style="width:120px;" onchange="cleanDiv();">
                    		<option value="">请选择</option>
                        	<s:iterator value="allTypeMapList" id="typeMap">
	                            <option value="${typeMap.type},${typeMap.chineseType}">${typeMap.chineseType}</option>
	                        </s:iterator>
                        </select>
                        <input type="button" value="选择" onclick="loadResourceEntity();"/>
                    </h4>
                    <div class="mt8">
                        <select id="selectEntity" style="height:135px; width:209px; background:#eee" size="4">
                        </select>
                        <input class="rt5" type="button" value="移除" onclick="removerOption();"/>
                    </div>
                </div>
                <div class="inline vt" style="margin-left:3em;">
                	<h4 class="tc" style="border-bottom: 1px solid #999; ">2.统计目标
                        <span class="f-normal ml10">
                            <input type="radio" onclick="loadType(this);" name="select_ziyuan" id="radio_child" value="child"/>子资源
                            <input type="radio" onclick="loadType(this);" name="select_ziyuan" value="link"/>关联资源
                            <input type="radio" onclick="loadType(this);" name="select_ziyuan" value="allLink"/>自定义
                        </span>
                    </h4>
                    <div style="width: 400px;margin-top:3px;"><input id="checkAll" type="checkbox" onclick="checkAll(this);"/>全选</div>
                    <div id="selectDiv" class="select_box" style="background:#eee;">
                    </div>
                    
                </div>
                <div class="inline vt" style="margin-left:3em;">
                	<h4><input type="checkbox" id="effect_settings"  onclick="sttingEffect(this);"/>展示效果：</h4>
                    <div id="effect_settings_div" class="select_box2 mt12 pl10" style="background:#eee; display: none;">
                    	<div class="mt8"><%-- <input type="checkbox" id="recursiveCheckbox" checked="checked"/>递归统计</div> --%>
                        <div class="mt8">资源分类图形状：<select id="chartsType">
                        	<option value="column">柱状图</option>
                        	<option value="area">区域图</option>
                        	<option value="line">折线图</option>
                        	
                        </select></div>
                        <div class="mt8">范围分组图效果：<input type="radio" name="duibituxiaoguo" checked="checked" value="basic"/>对比  <input type="radio" name="duibituxiaoguo" value="stacked"/>堆积</div>
                        <div class="mt8">图-表排列方式：
	                        <select onchange="changeFloat(this);">
	                        	<option value="left-right">左-右</option>
	                        	<option value="right-left">右-左</option>
	                        	<option value="up-down">上-下</option>
	                        	<option value="down-up">下-上</option>
	                        </select>
						</div>
                    </div>
                </div>
            </div>
        </div>
       	<div class="tip_info">
       		按<span id="selectResourceSpan">(什么范围?)</span>统计：<span id="selectTypeSpan">(什么资源?)</span>
       	</div>
        <div>
        	<input type="button" onclick="selectResource();" id="selectResource" value="查询" disabled="disabled" style=" width:60px; margin-left: 15px;" />
        	<em class="red" id="condition">请设置统计条件</em>
    	</div>
    </div>
    
    <form action="" id="searchForm">
    	<input type="hidden" id="searchForm_entityId" name="entityId"/>
    	<input type="hidden" id="searchForm_selectResType" name="selectResType"/>
    	<input type="hidden" id="searchForm_currentType" name="currentType"/>
    	<input type="hidden" id="searchForm_allLink" name="allLink"/>
    	<input type="hidden" id="searchForm_tableTitle" name="tableTitle"/>
    </form>
    <form action="" id="DownLoadForm" method="post">
    	<input type="hidden" id="DownLoadForm_entityId" name="entityId"/>
    	<input type="hidden" id="DownLoadForm_selectResType" name="selectResType"/>
    	<input type="hidden" id="DownLoadForm_currentType" name="currentType"/>
    	<input type="hidden" id="DownLoadForm_allLink" name="allLink"/>
    	<input type="hidden" id="DownLoadForm_conditions" name="conditions"/>
    	<input type="hidden" id="DownLoadForm_column" name="column"/>
    	<input type="hidden" id="DownLoadForm_columnValue" name="columnValue"/>
    	
    </form>
	
	<div id="down" class="clearfix" style="height: 380px;">
		<div id="min_top" class="clearfix min_top_a" style="cursor:pointer; ">
		<a href="#" style="color:#000; text-decoration:none; float: left; padding-left: 5px;">资源统计报表 ▼</a>
	</div>
		<div id="load_img"  style="display:none">
        	<img src="image/loading_img.gif"><br>数据处理中，请稍侯...
    	</div>
		<div id="down_de">
			<div id="down_top" class="down_top_left-right" style="height: 320px;"></div>
   			     <%-- clear:both; --%>
   			     <div id="down_down" class="down_down_left-right">
   			     <div id="down_down1"></div>
   			     <div id="down_down2" style="display: none"><input type="button" value="导出" id="btnExportRes" onclick="getExportRes();" style="float:right; width:60px; margin-right: 7px;" /></div>
     	   </div>
		</div>
		
    </div>
    <div id="divDisplay1">
    	<div class="alert_top">填写筛选信息</div>
    	<ul>
    		<li>
		    	<span>需要筛选字段: </span>
		    	<select id="select_column">
		    		<option>请选择</option>
		    	</select>
		    </li>
			<li>
				<span>判断符号:</span>
		    	<select id="conditions">
		    		<option>请选择</option>
		    		<option value="equal">等于</option>
		    		<option value="notequal">不等于</option>
		    		<option value="LessThan">小于</option>
		    		<option value="greaterThan">大于</option>
		    		<option value="Contain">包含</option>
		    		<option value="NoContain">不包含</option>
		    		
		    	</select>
		    </li>
		    <li>
		    	<span>字段值:</span>
		    	<input type="text" id="column_value"/>
		    </li>
			<li class="ul_bottom">
		    	<input type="button" value="确定" onclick="selectExtjsGrid();"/>
		    	<input type="button" value="取消" onclick="divDisplay1hide();"/>
    		</li>
    	</ul>
    </div>
    <div id="bottom" class="clearfix">
    <div id="up_top" class="btm_top_a" style="cursor:pointer; ">
        	<a href="#" style="color:#000; text-decoration:none;">资源详细信息 ▼</a>
        	<marquee style="width: 85%;"  onmouseover="this.stop();" onmouseout="this.start();"><font id="marqueefont" ></font></marquee>
    </div>
    
    <div id="bottom1" class="clearfix" style="text-align: center;">
    	<%--<img id="bottom_load_img" style="display: none;" src="image/ajax-loaderCccc.gif"/>  --%>
    	<div class="bottom_load" id="bottom_load_img" style="display: none;">
        	<img src="image/loading_img.gif"><br>数据处理中，请稍侯...
    	</div>
    	<div id="bottom_left" style="width: 100%; float: left;">
    	</div>
    	<div id="bottom_right" style="width: 49%; float: right;"></div>
    </div>
    </div>
    <div id="selectEntityDivHidden" style="display: none;"></div>
    <div id="selectDivHidden" style="display: none;"></div>
    <div id="paging_div" style="display: none"></div>
    <%-- confirm_div 开始  --%>
	<div id="confirm_div_warning" class="confirm_div" style="display: none">
		<div class="confirm_div_cover"></div>
	    <div class="confirm_div_container">
	    	<div class="confirm_div_top ico_warning">警告！</div>
	    	<div class="confirm_div_main">
	        	<h4>
	            	自定义统计，您选择了 <span id="selectDivCount"></span> 类资源，可能需要等待很长时间。<br />
	            	建议您每次统计的资源类型不超过 5 个。<br />
	                您仍然需要继续统计吗？
	            </h4>
	        </div>
	        <div class="confirm_div_bottom">
	        	<input type="button" value="继续" onclick = "continueOperate();" />&nbsp;&nbsp;&nbsp;
	        	<input type="button" value="取消" onclick = "confirm_div_warning_show();"  />
	        </div>
	    </div>
	</div>
	<%-- confirm_div 结束 --%>
    <%-- confirm_div 开始  --%>
	<div id="confirm_div_wrong" class="confirm_div" style="display: none">
		<div class="confirm_div_cover"></div>
	    <div class="confirm_div_container">
	    	<div class="confirm_div_top ico_wrong">错误！</div>
	    	<div class="confirm_div_main">
	        	<h4>
	            	自定义统计，您选择了 12 类资源，类型太多可能无法统计，<br />
	                系统禁止操作。<br />
	                建议您每次统计的资源类型不超过 5 个。<br />
	            </h4>
	        </div>
	        <div class="confirm_div_bottom">
	        	<input type="button" value="确定" onclick = "confirm_div_wrong_show();" />&nbsp;
	        </div>
	    </div>
	</div>
<%-- confirm_div 结束 --%>
</body>
</html>
