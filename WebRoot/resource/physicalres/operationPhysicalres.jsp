<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>资源设施维护-浏览</title>
	<link rel="stylesheet" type="text/css" href="css/base.css" />
	<link rel="stylesheet" type="text/css" href="css/public.css" />
	<link rel="stylesheet" type="text/css" href="css/resources.css" />
	<link rel="stylesheet" type="text/css" href="css/ziyuan.css" />
	<link rel="stylesheet" type="text/css" href="css/info.css" />
	<link rel="stylesheet" type="text/css" href="css/dialog.css"/>
	<link rel="stylesheet" type="text/css" href="css/source.css"/>
	<link href="css/uploadify.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" href="css/jquery.treeview.css" />
	<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
	<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js"></script>
	<script type="text/javascript" src="../../jslib/jquery/jquery.form.js"></script>
	<script type="text/javascript" src="../js/swfobject.js"></script>
	<script type="text/javascript" src="../js/jquery.uploadify.v2.0.3.js"></script>
	<script type="text/javascript" src="../../jslib/date/wdatePicker.js"></script>
	<script type="text/javascript" src="../../jslib/jquery/jquery.validate.js"></script>
	<script type="text/javascript" src="../js/validate.ex.js"></script>
	<script type="text/javascript" src="../js/input.js"></script>
	<script type="text/javascript" src="../../jslib/paging/paging.js"></script>
	<script type="text/javascript" src="js/operationPhysicalres.js"></script>
	
	<script>
	$(function(){
	
		var img =new Image(); 
		$(".photoLi").each(function(){
			var imgsrc= $(this).children().eq(0).attr("src");
			img.src = imgsrc;
			var height = 350
			var width=(height*parseInt(img.width))/parseInt(img.height);
			$(this).children().eq(0).attr("height",height).attr("width",width);
		})
		
		$("#operaForm1").validate({});
		$("#operaForm").validate({});
		
		//表单AJAX提交
		$("#operaForm").ajaxForm({
				success:function(data){
					if(data != "error"){
						alert("添加成功");
						$("#dialog").hide();
						$("#operaFormblack").hide();
						$("#container_left .on").click();
						var rootCurrentEntityId =$("#rootCurrentEntityId").val();
							var rootCurrentEntityType =$("#rootCurrentEntityType").val();
							var obj = $("#container_left .on");
							var searchType = obj.attr("id");
							showCurrentEntity(rootCurrentEntityId,rootCurrentEntityType,searchType,'noAetg',this);
							var parentEntityType = rootCurrentEntityType;
							var parentEntityId = rootCurrentEntityId;
							var params = {parentEntityType:parentEntityType, parentEntityId:parentEntityId, searchType:searchType};
							$.ajax({
								url:'getChildEntityByTypeUniversalRecursionForFlatternInfoAction',
								data:params,
								dataType:'json',
								type:'post',
								async:true,
								success:function(data){
									//$("#liCurrentCount").val(++liCurrentCount);
									//if (data != "0") {
										obj.children("em").html(data);
									//} else {
									//	obj.remove();
									//}
									
								}
							})
					}else{
						alert("添加失败");
						$("#dialog").hide();
						$("#operaFormblack").hide();
					}
				}
		});
				//表单AJAX提交
		$("#operaForm1").ajaxForm({
					success:function(data){
					//alert("保存成功!");
					var index = 0;
					$("input#manageModel_input,textarea#manageModel_input,.timeinput").each(function(){
					if($(this).val() != "选择时间"){
						$(".viewModel").eq(index).text($(this).val());
					}
						index++;
					});
					$("#op_update").show();
					$("#op_submit").hide();
					$("#op_create").hide();
					$(".viewModel").show();
					$(".manageModel").hide();
					$("#modelType").val("view");
					var rootCurrentEntityId = $("input[name='rootCurrentEntityId']").val();
					var rootCurrentEntityType = $("input[name='rootCurrentEntityType']").val();
					var operatedCurrentEntityId = $("input[name='operatedCurrentEntityId']").val();
					var operatedCurrentEntityType = $("input[name='operatedCurrentEntityType']").val();
					if(rootCurrentEntityId==operatedCurrentEntityId && rootCurrentEntityType==operatedCurrentEntityType ){
						$("#parentHref").html($("#parentTypeName").text()).attr("href","href=\'javascript:showparentInfo('"+$("input[name='newParentResEntityId']").val()+"','"+$("input[name='newParentResEntityId']").val()+"');\'");
						
					}
				}
		});
		
		//图片上传(增加图片)
			$("#uploadify").uploadify({
				'uploader':'swf/uploadify.swf',
				'script':'uploadAction?uploadType=image',
				//'auto':true,
				'cancelImg':'image/cancel.png',
				//'folder':'/upload',
				'fileDataName':'file',
				'queueID':'fileQueue',
				'queueSizeLimit':8,
				'fileDesc':'jpg,png,gif,bmp',
				'fileExt':'*.jpg;*.gif;*.png;*.bmp',
				'method':'post',
				'multi':true,
				'buttonText':'浏览文件',
				'onComplete':function(event,queueID,fileObj,serverData,data){
					//$('#bgImage').attr("src",serverData);
					var content = "<dl style='width:200px;height:120px;float:left;text-align:center;'><dt style='width:230px;height:110px;'><img style='width:100px;height:100px;' src='"+serverData+"' /></dt><dd>"+fileObj.name+"</dd></dl>";
					$("#imgPreviewDiv #imgContent").append(content);
					
					var url = "createPhotoAssociatedRelationAction";
					//图片父级entity类型
					var photoParentEntityType = "";
					//图片父级entity ID
					var photoParentEntityId = "";
					if($("#source_list_infoMap_div").css("display") == "none"){
						photoParentEntityType = $("#rootCurrentEntityType").val();
						photoParentEntityId = $("#rootCurrentEntityId").val();
					}else{
						photoParentEntityId = $("#chi_ul .selected").children().eq(0).val();
						photoParentEntityType = $("#chi_ul .selected").children().eq(1).val();
					}
					//图片UUID后的名字
					var photoName = serverData.substring(serverData.lastIndexOf("/")+1);
					//图片原来的名字
					var photoOldName = (fileObj.name).substring(0,(fileObj.name).indexOf("."));
					
					var params = {photoParentEntityType:photoParentEntityType,photoParentEntityId:photoParentEntityId,photoName:photoName,photoOldName:photoOldName}
					$.post(url, params, function(data){
						if($("#source_list_infoMap_div").css("display") == "none"){
								//$("#infoMap_div").html("<div class='flattening_loading'><em>数据载入中</em><p></p></div>");
								$("#infoMap_div").html("<div class='flattening_loading'  style='text-align:center'><img src='image/loading_img.gif'><br>数据处理中，请稍侯...</div>");
								$(".tab_content").hide();
								$(".tab_content").hide();
								$("#source_list_infoMap_div").hide();
								$("#infoMap_tab_content").show();
								$("#info_tab_ul").show();
								$("#infoMap_div").show();
								var rootCurrentEntityType = photoParentEntityType;
								var rootCurrentEntityId = photoParentEntityId;
								$.ajax({
								url : "getPhysicalresForOperaAction",
								//currentEntityType=Area&currentEntityId=1492&modelType=view
								
								data : {loadBigPage:"loadBigPage",modelType:"view",currentEntityType:rootCurrentEntityType,currentEntityId:rootCurrentEntityId},
								type : 'POST',
								async:false,
								success : function(data) {
									$("#infoMap_div").html(data);
									$("#photo_li").click();
									$("#chi_infoMap_div").html("");
									$("#modelType").val("view");
									
								}	
						    });	
						  }else{
						  	//$("#chi_infoMap_div").html("<div class='flattening_loading'><em>数据载入中</em><p></p></div>");
						  	$("#chi_infoMap_div").html("<div class='flattening_loading'  style='text-align:center'><img src='image/loading_img.gif'><br>数据处理中，请稍侯...</div>");
							$(".tab_content").hide();
							$("#infoMap_div").hide();
							$("#chi_infoMap_div").show();
							$("#infoMap_tab_content").show();
							$("#source_list_infoMap_div").show();
							var rootCurrentEntityType = photoParentEntityType;
							var rootCurrentEntityId = photoParentEntityId;
							$.ajax({
							url : "getPhysicalresForOperaAction",
							//currentEntityType=Area&currentEntityId=1492&modelType=view
							
								data : {loadBigPage:"loadBigPage",modelType:"view",currentEntityType:rootCurrentEntityType,currentEntityId:rootCurrentEntityId},
								type : 'POST',
								async:false,
								success : function(data) {
									$("#infoMap_div").html("");
									$("#chi_infoMap_div").html(data);
									$("#photo_li").click();
									$("#modelType").val("view");
									//$(".tab_content").hide();
									//$("#chi_infoMap_div").show();
									//$("#infoMap_tab_content").show();
									//$("#source_list_infoMap_div").show();
									//$("#infoMap_tab_content").show();
								}	
							});	
						  }	
					});//建立图片与父级的关系
				}
		});
	});
	
	function selectInputfocus(me){
		//已更改为黑色字，不作清除内容操作
		if($(me).css("color") == "#000" || $(me).css("color") == "rgb(0, 0, 0)") {
			return false;
		}
		$(me).val("");
		//focus后，字体更改为黑色
		$(me).css("color","#000");
	}
	
	function trunTab(me){
			if($(me).parent().find($(".list_ul2")).css("display") == "block"){
				$(me).removeClass("open");
				$(me).parent().find($(".list_ul2")).slideUp("fast");
			}else{
				$("#container_left .nav_ul span").removeClass("open");
				$("#container_left .nav_ul span").parent().find($(".list_ul2")).slideUp("fast");
				$(me).addClass("open");
				//$(me).parent().find($(".list_ul2")).slideDown("fast");
				$(me).parent().find($(".list_ul2")).show();
				//$(me).parent().find($(".list_ul2")).slideDown().show(function(){$(me).parent().find($(".list_ul2")).html($(me).parent().find($(".list_ul2")).html()).show();}); 
			}
	}

	
			

	function openhideinfoMap(me){
		//if($("#modelType").val() == "view"){
			$(me).hide();
			$("#op_submit").show();
			$("#op_create").show();
			$(".viewModel").hide();
			$(".manageModel").show();
			$("#addcontent").empty();//yuan.yw 修改资源 添加资源div清空 防止日期控件冲突
			$("#modelType").val("modelType");
		//}
	}
	
	/**
	 * 下拉框点击option处理
	 * @param {Object} id
	 */
	function optionOnclick(id){
		//$(id).parent().next().val($(id).text());
		var txt = $(id).find('option:selected').text();
		if($.trim(txt)=='请选择'){
			$(id).next().val("");
		}else{
			$(id).next().val(txt);
		}
		
	}
	/**
	 *  属性为整型时下拉框点击option处理
	 * @param {Object} id
	 */
	function integerOptionOnclick(id){
		//$(id).parent().next().next().val($(id).text());
		//$(id).parent().next().val($(id).parent().val());
		var txt = $(id).find('option:selected').text();
		if($.trim(txt)=='请选择'){
			$(id).next().next().val("");
		}else{
			$(id).next().next().val(txt);
		}
		
		$(id).next().val($(id).val());
	}
	
	
	
	function showPhotoDiv(me){
		$(".tab_nav ul li").removeClass("selected");
		$(me).addClass("selected");
		$(".tab_content").hide();
		$("#photo_tab_content").show();
	}
	
	function showPanel(me){
		$(".tab_nav ul li").removeClass("selected");
		$(me).addClass("selected");
		$(".tab_content").hide();
		$("#panel_tab_content").show();
		//$("#infoMap_tab_content").html("");
		//$("#chi_infoMap_div").html("");
		var currentVal = $("#currentValues").val();
		var currentEntityType = "";
		var currentEntityId = "";
		if($("#source_list_infoMap_div").css("display") == "none"){
			currentEntityType = $("#rootCurrentEntityType").val();
			currentEntityId = $("#rootCurrentEntityId").val();
		}else{
			currentEntityId = $("#chi_ul .selected").children().eq(0).val();
			currentEntityType = $("#chi_ul .selected").children().eq(1).val();
		}
			    if($.trim($(me).text())=="面板图"){
			    	if(currentEntityType=="DDF"){
			    		var params ={currentEntityType:currentEntityType,currentEntityId:currentEntityId};
						$.post("showDDFPanel.jsp", params, function(data){	
							$("#boardPanel").html(data);
						});
			    	}else if(currentEntityType=="PrimaryEquipFrame_GSM"){
						var params={currentEntityType:currentEntityType,currentEntityId:currentEntityId,modelType:"view"};
						$.post("operateResourcePanel.jsp", params, function(data){	
							$("#boardPanel").empty();
							$("#boardPanel").html(data);
							$("#boardPanel label").show();
						});			
				    }else if(currentEntityType=="BaseStation_GSM"){
				    	 var url="getEquipBoardForShowPanelAction";
				         var params ={currentEntityType:currentEntityType,currentEntityId:currentEntityId};
				         $.post(url,params,function(data){
				         	var content="";
				         	var context = "<div id='viewpanelTabDiv'><ul class='tab_2_ul_viewpanel'>";
				         	var pCountext = "";
				         	$.each(data,function(index,value){
				         		if(index==0){
				         			content +="<li onclick='clickViewPanelTab(this)' class='ontab' id='"+value.type+"*"+value.id+"'>"+value.name+"</li>";
				         			params ={currentEntityType:value.type,currentEntityId:value.id,modelType:'view'};
				         			$.ajax({
										url : "operateResourcePanel.jsp",
										//currentEntityType=Area&currentEntityId=1492&modelType=view
										
										data : params,
										type : 'POST',
										async: false,
										success : function(data) {
											pCountext = pCountext +data;
										}
									});
				         		}else{
				         			content +="<li onclick='clickViewPanelTab(this)' id='"+value.type+"*"+value.id+"'>"+value.name+"</li>";
				         		}
				         	})
				         	
				         	context = context + content + "</ul></div>" + pCountext;
				         	$("#boardPanel").html(context);
				         },'json')
				    }else{
				    		getOdmAndTerminalLayOutView();
				    	}
				    }
	}


	//浏览模式RBS2206面板tab
	function clickViewPanelTab(me){
		$(me).siblings().removeClass("ontab");
		$(me).addClass("ontab");
		var cId = $(me).attr("id");
		var array = cId.split("*");
		var currentEntityType = array[0];
		var currentEntityId = array[1]; 
		var params={currentEntityType:currentEntityType,currentEntityId:currentEntityId,modelType:"view"};
		$.post("operateResourcePanel.jsp", params, function(data){	
			$("#boardPanel").children().eq(1).remove();
			$("#boardPanel").children().eq(1).remove();
			$("#boardPanel").children().eq(1).remove();
			$("#boardPanel").append(data);
		});
	}
	
	
	//浏览模式RBS2206面板tab
	function showLayout(me){
		$(".tab_nav ul li").removeClass("selected");
		$(me).addClass("selected");
		$(".tab_content").hide();
		$("#layout_tab_content_tab_2_div").html("功能待开发 ");
		$("#layout_tab_content").show();
	}
	
	
	
	//获取基本信息
	function showInfoMap(me){
		if($("#source_list_infoMap_div").css("display") == "none"){
			//$("#infoMap_div").html("<div class='flattening_loading'><em>数据载入中</em><p></p></div>");
			$("#infoMap_div").html("<div class='flattening_loading'  style='text-align:center'><img src='image/loading_img.gif'><br>数据处理中，请稍侯...</div>");
			$(".tab_content").hide();
					$("#source_list_infoMap_div").hide();
					$("#infoMap_tab_content").show();
			$(".tab_nav ul li").removeClass("selected");
			$(me).addClass("selected");
			var rootCurrentEntityType = $("#rootCurrentEntityType").val();
			var rootCurrentEntityId = $("#rootCurrentEntityId").val();
			$.ajax({
			url : "getPhysicalresForOperaAction",
			//currentEntityType=Area&currentEntityId=1492&modelType=view
			
			data : {loadBigPage:"loadBigPage",modelType:"view",currentEntityType:rootCurrentEntityType,currentEntityId:rootCurrentEntityId},
			type : 'POST',
			async:false,
			success : function(data) {
					$("#infoMap_div").html(data);
					$("#chi_infoMap_div").html("");
					$("#modelType").val("view");
			}	
			});
		}else{
			showChiInfoSelect();	
		}
    
	}
	
	
	//获取基本信息
	function showInfoMapleft(){
		//$("#infoMap_div").html("<div class='flattening_loading'><em>数据载入中</em><p></p></div>");
		$("#infoMap_div").html("<div class='flattening_loading'  style='text-align:center'><img src='image/loading_img.gif'><br>数据处理中，请稍侯...</div>");
		$(".tab_content").hide();
		$(".tab_content").hide();
		$("#source_list_infoMap_div").hide();
		$("#infoMap_tab_content").show();
		$("#info_tab_ul").show();
		$("#infoMap_div").show();
		var rootCurrentEntityType = $("#rootCurrentEntityType").val();
		var rootCurrentEntityId = $("#rootCurrentEntityId").val();
		$.ajax({
		url : "getPhysicalresForOperaAction",
		//currentEntityType=Area&currentEntityId=1492&modelType=view
		
		data : {loadBigPage:"loadBigPage",modelType:"view",currentEntityType:rootCurrentEntityType,currentEntityId:rootCurrentEntityId},
		type : 'POST',
		async:false,
		success : function(data) {
			$("#infoMap_div").html(data);
			$("#chi_infoMap_div").html("");
			$("#modelType").val("view");
			
		}	
    });
	}
	
	function showOrderList(){
		$(".tab_content").hide();
		$("#info_tab_ul").hide();
		$("#order_tab_content").show();
		$("#chi_ul_div").hide();
		//alert($("#order_tab_content").html());
		getOrderList("workOrder","");
		getOrderList("taskOrder","");
	}
	
	function showMaintenanceByMonth(){
		$(".tab_content").hide();
		$("#info_tab_ul").hide();
		$("#flattening_service_tab_content").show();
		getMaintenanceByMonth();
	}
	
	function showChiInfo(id,entityType,me){
		$("#chi_ul li").removeClass("selected");
		$(me).addClass("selected");
		showChiInfoMap(id,entityType);
	}
	
	function showChiInfoMap(id,entityType){
		//$("#chi_infoMap_div").html("<div class='flattening_loading'><em>数据载入中</em><p></p></div>");
		$("#chi_infoMap_div").html("<div class='flattening_loading'  style='text-align:center'><img src='image/loading_img.gif'><br>数据处理中，请稍侯...</div>");
		$(".tab_content").hide();
		$("#infoMap_div").hide();
		$("#chi_infoMap_div").show();
		$("#infoMap_tab_content").show();
		$("#source_list_infoMap_div").show();
		$.ajax({
		url : "getPhysicalresForOperaAction",
		//currentEntityType=Area&currentEntityId=1492&modelType=view
		
			data : {loadBigPage:"loadBigPage",modelType:"view",currentEntityType:entityType,currentEntityId:id},
			type : 'POST',
			async:false,
			success : function(data) {
				$("#infoMap_div").html("");
				$("#chi_infoMap_div").html(data);
				$("#modelType").val("view");
				//$(".tab_content").hide();
				//$("#chi_infoMap_div").show();
				//$("#infoMap_tab_content").show();
				//$("#source_list_infoMap_div").show();
				//$("#infoMap_tab_content").show();
			}	
		});
	}
	
	function showChiInfoSelect(){
		var id = $("#chi_ul .selected").children().eq(0).val();
		var entityType = $("#chi_ul .selected").children().eq(1).val();
		showChiInfoMap(id,entityType);
	}
	
	function deleteChiInfo(){
	if($("#chi_ul .selected").size() < 1){
		alert("请选择一个需要删除的资源");
		return;
	}
				var chooseResEntityType = $("#chi_ul .selected").children().eq(1).val();
				var chooseResEntityId = $("#chi_ul .selected").children().eq(0).val();
				if(chooseResEntityType == "" || chooseResEntityId == "") {
					alert("请选择要删除的资源！");
					return false;
				}
				if(confirm("确定要删除该资源及其下子资源吗？")) {
					var url = "../physicalres/delPhysicalresByRecursionAction";
					
					if(chooseResEntityType == "FiberCore-Terminal" || chooseResEntityType == "FiberCore-FiberCore") {
						//删除纤芯成端或者熔纤接续，跳到特定的删除方法,删除关系
						url = "../physicalres/deleteRelationForExtraForOnlyOneAction";
					} else {
						//一般的物理资源删除
						url = "../physicalres/delPhysicalresByRecursionAction";
					}
					
					var params = {chooseResEntityType:chooseResEntityType,chooseResEntityId:chooseResEntityId}
					$.post(url, params, function(data){
						if(data == "success") {
							alert("删除成功!");
							var rootCurrentEntityId =$("#rootCurrentEntityId").val();
							var rootCurrentEntityType =$("#rootCurrentEntityType").val();
							showCurrentEntity(rootCurrentEntityId,rootCurrentEntityType,chooseResEntityType,'noAetg',this);
							var obj = $("#"+chooseResEntityType);
							var searchType = obj.attr("id");
							var parentEntityType = rootCurrentEntityType;
							var parentEntityId = rootCurrentEntityId;
							var params = {parentEntityType:parentEntityType, parentEntityId:parentEntityId, searchType:searchType};
							$.ajax({
								url:'getChildEntityByTypeUniversalRecursionForFlatternInfoAction',
								data:params,
								dataType:'json',
								type:'post',
								async:true,
								success:function(data){
									//$("#liCurrentCount").val(++liCurrentCount);
									//if (data != "0") {
										obj.children("em").html(data);
									//} else {
									//	obj.remove();
									//}
									
								}
							})
						}else{
							alert("删除失败!");
						}
					},'json');
				}
	}
	
	//资源类别切换
	function clicklist_ul2_li(me){
		
		$(".list_ul2 li").removeClass("on");
		$(me).addClass("on");
	}
	
	
	function addPhoto(){
		$("#messageBox_container").show();//增加图片层显示
		$("#messageBox_container_update").hide();//修改图片层隐藏
		$("#black").show();//遮盖层
	}
	
	//关闭弹出层控制(增加图片层)
		function messageBox_hide(){
			var messageBox = document.getElementById("messageBox_container");
			messageBox.style.display="none";
			//关闭时，清空图片展示列表
			$("#imgContent").html("");
			//关闭时，清空灰色浏览文件
			$("#fileQueue").empty();
			//隐藏遮盖层
			$("#black").hide();
		}
		
		
		//关闭弹出层控制(增加图片层)
		function messageBox_hide(){
			var messageBox = document.getElementById("messageBox_container");
			messageBox.style.display="none";
			//关闭时，清空图片展示列表
			$("#imgContent").html("");
			//关闭时，清空灰色浏览文件
			$("#fileQueue").empty();
			//隐藏遮盖层
			$("#black").hide();
		}
		
		function showparentInfo(id,entityType){
			var host = window.location.host
			//alert(host);
			window.open("/ops/resource/physicalres/getPhysicalresForOperaAction?currentEntityType="+entityType+"&currentEntityId="+id+"&modelType=view");
		}
		
		function showaddResDiv(){
			//alert($("#container_left .on").attr("id"));
			var sType  = $("#container_left .on").attr("id");
			var areaId = $("#areaId").val();
			var currentEntityId = $("#rootCurrentEntityId").val();
			var rootCurrentEntityType = $("#rootCurrentEntityType").val();
			var params ={sType:sType,areaId:areaId,currentEntityId:currentEntityId,currentEntityType:rootCurrentEntityType};
			$.post("loadAddResourcePageAction",params,function(data){
				$("#addcontent").html(data);
				$("#dialog").show();
				$("#operaFormblack").show();
			})
		}
		
		function backToIndex(){
			//alert(window.location.host);
			var pHost = window.location.host;
			window.open("http://"+pHost + "/ops");
		}
		
		function backToNetworkResourceManage(){
			//alert(window.location.host);
			var pHost = window.location.host;
			window.open("http://"+pHost + "/ops/workManagePageIndexAction?type=networkResource");
		}
	</script>
</head>
<body>
<input id="areaId" value="${areaId }" type="hidden"/>
<input  type="hidden" value="${param.currentEntityType}#${value}" id="currentValues"/>
<input id="rootCurrentEntityId" name="rootCurrentEntityId" value="${param.currentEntityId}" type="hidden" />
<input id="rootCurrentEntityType" name="rootCurrentEntityType" value="${param.currentEntityType}" type="hidden" />
<input type='hidden' id='liSize'/>
<input type="hidden" id="modelType" value="${modelType }" />
<%-- 弹出框 --%>
    <div id="dialog" style="display:none;">
    <form id="operaForm" action = "addResourceNewAction" method="post">
        <div class="black" id="operaFormblack" style="display:block; z-index:11;"></div>
        <div id="addcontent">
        </div>
    </form>
    </div>
<div id="header960">
    	<div class="header-top">
    		<h2>
				<a href="javascript:void(0);" onclick="backToIndex();">IOSM首页</a> →
				<a href="javascript:void(0);" onclick="backToNetworkResourceManage();">网络资源</a> →
				资源详细信息
			</h2>
		</div>
        <div class="header-main clearfix">
        	<span class="fl">${cName}:${currentEntityMap.name}</span>
            <span class="fr">归属：<a href="javascript:showparentInfo('${parentEntityMap.id}','${parentEntityMap._entityType}');" id='parentHref' >${parentEntityMap.name}</a></span>
        </div>
    </div>
    <%--头部结束--%>
    
    <div id="container960">
    	<div class="container_left" id="container_left">
        	<ul class="nav_ul">
            	<li>
                	<span class="list_title open" onclick="trunTab(this);">基础信息</span>
                	<ul class="list_ul2" style="display:block;">
                    		<li onclick="showInfoMapleft();clicklist_ul2_li(this);">资源基础信息</li>
                    		<li onclick="showMaintenanceByMonth();clicklist_ul2_li(this);">历史维护记录</li>
                    	<s:if test="#request.currentEntityType=='Station'||#request.currentEntityType=='BaseStation'||#request.currentEntityType=='BaseStation_GSM'||#request.currentEntityType=='BaseStation_repeater'||#request.currentEntityType=='BaseStation_TD'||#request.currentEntityType=='BaseStation_WLAN'">
                    		<li onclick="showOrderList();clicklist_ul2_li(this);">维护工单/任务</li>
                    	</s:if>
                    </ul>
                </li>
            	<s:if test="aetgList != null">
							<s:iterator  id="mp" value="aetgList" status="st">
								<li>
									<span class="list_title open" onclick="tab(this,'${mp.name}');" id="${mp.name}">${mp.chineseName}</span>
									<ul class="list_ul2" style="display:none;" id="${mp.name}"></ul>
								</li>
								
							</s:iterator>
							
				</s:if>
				<s:else>
						<li>
									<span class="list_title open" onclick="tab(this,'noAetg');" id="${mp.name}">下属资源</span>
									<ul class="list_ul2" style="display:none;" id="${mp.name}"></ul>
						</li>
				</s:else>
            </ul>
        </div>
    	<div class="container_right">
    		<form id="operaForm1" action="updatePhysicalresAction"  method="post" target="stay">
            <div class="tab_container" id="infoMap_div">
            	<div class="tab_nav clearfix" id="info_tab_ul">
            		<ul>
                    	<li class="selected" onclick="showInfoMap(this);">基本属性</li>
                    	<s:if test="#request.hasPhoto!='no'">
								<li id="photo_li" onclick="showPhotoDiv(this);">实景照片</li>
								<s:if test="#request.currentEntityType=='DDF'||#request.currentEntityType=='ODF'||#request.currentEntityType=='FiberCrossCabinet'||#request.currentEntityType=='FiberDistributionCabinet'||#request.currentEntityType=='FiberTerminalCase' ||#request.currentEntityType=='BaseStation_GSM'||#request.currentEntityType=='PrimaryEquipFrame_GSM'">
								<li onclick="showPanel(this);">面板图</li>
								</s:if>
								<%--<s:else>
								<li onclick="showLayout(this);">平面布局图</li>
								</s:else>--%>
							</s:if>
							<s:else>
								<s:if test="#request.currentEntityType=='DDF'||#request.currentEntityType=='ODF'||#request.currentEntityType=='FiberCrossCabinet'||#request.currentEntityType=='FiberDistributionCabinet'||#request.currentEntityType=='FiberTerminalCase' ||#request.currentEntityType=='BaseStation_GSM'||#request.currentEntityType=='PrimaryEquipFrame_GSM'">
								<li onclick="showPanel(this);">面板图</li>
								</s:if>
								<%--<s:else>
								<li onclick="showLayout(this);">平面布局图</li>
								</s:else>--%>
							</s:else>
                    </ul>
                </div>
                <div class="tab_content" style="display:block;" id="infoMap_tab_content">
                	<h4 class="table_top">设备属性列表：
                    	<span class="ab_right"><input type="button" id="op_update" value="修改" onclick="openhideinfoMap(this);"/>
                    	<input type="button" id="op_create" value="取消" style="display: none;" onclick="showInfoMap(this);"/>
                    	<input type="submit" id="op_submit" value="保存" style="display: none;"/>
                    	</span>
                    	
                    </h4>
                    
                    <s:set id="a" value="1" />
                    <input type="hidden" name="updatedEntityType" value="${param.currentEntityType}" />
       				   <input name="operatedCurrentEntityId" value="${param.currentEntityId}" type="hidden" />
        			   <input name="operatedCurrentEntityType" value="${param.currentEntityType}" type="hidden" />
                    <input id="chosenEntityType" type="hidden" value="${param.currentEntityType}"/>
					<input id="chosenEntityId" type="hidden"  value="${requestScope.currentEntityId}"/>
					<input id="addedResParentEntityType" name="addedResParentEntityType" type="hidden" />
					<input id="addedResParentEntityId" name="addedResParentEntityId" type="hidden" />
					<input id="addedResEntityType" name="addedResEntityType" type="hidden" />
		        	<input type="hidden" id="bizModule" name="bizModule" value="${bizModule }"/>
					<input type="hidden" id="bizProcessCode" name="bizProcessCode" value="${bizProcessCode }"/>
					<input type="hidden" id="bizProcessId" name="bizProcessId" value="${bizProcessId }"/>
		        	<input id="oldParentResEntityId" name="oldParentResEntityId" type="hidden" value="${parentEntityMap.id}"/>
		        	<input id="oldParentResEntityType" name="oldParentResEntityType" type="hidden" value="${parentEntityMap._entityType}"/>
		        	<input id="newParentResEntityId" name="newParentResEntityId" type="hidden" />
		        	<input id="newParentResEntityType" name="newParentResEntityType" type="hidden" />
                    <table class="source_table2">
                    	<s:iterator value="currentEntityMap" id="map" status="st">
	                   		<s:if test="key == 'id' || key == 'area_id' || key == 'path' || key == 'parent_id'">
			                    <input name="${param.currentEntityType}#${key}" type="hidden" value="${value}" />
			                    
			                </s:if>
		                	<s:if test="!(key == 'id' || key == 'path' || key == 'parent_id' || key == 'area_id' || key == '_entityId' || key == '_entityType')">
							
								<s:if test="#a % 2 != 0">
									<tr>
								</s:if>
			                	<s:else>
			                	</s:else>
				                	<td class="left_td">
					                	<%-- 获取中文字段名，拿不到拿英文字段名 --%>
					                	<s:if test="currentEntityChineseMap.get(#map.key) == null">
					                		${key}
					                	</s:if>
					                	<s:else>
					                		<s:property value="currentEntityChineseMap.get(#map.key)"/>
					                	</s:else>
				                	</td>
				                    <td><span class="manageModel" style="display:none">
				                    	<%-- 验证是整型的情况 --%>
				                    	<s:if test="attrTypeMap.get(#map.key).indexOf('Integer') > -1">
				                    		<%-- 判断属性的非空情况 --%>
					                    	<s:if test="'true'.equals(nullableMap.get(#map.key))">
					                    		<s:if test="dropdownListMap.get(#map.key) != null">
					                    			<select  class="input_select" onchange="integerOptionOnclick(this);">
					                    				
					                    				<option value="" selected="selected">请选择</option>
					                    				
						                    			<s:iterator id="dropdownVal" value="dropdownListMap.get(#map.key)">
						                    				<s:if test='#dropdownVal=="是"'>
						                    					<s:if test='"1"==#map.value'>
						                    						<option selected="selected" value="1">${dropdownVal}</option> 
						                    					</s:if> 
						                    					<s:else>
						                    						<option  value="1" >${dropdownVal}</option> 
						                    					</s:else>          
						                    				</s:if>
						                    				<s:elseif test='#dropdownVal=="否"'>
						                    					<s:if test='"0"==#map.value'>
						                    						<option selected="selected" value="0" >${dropdownVal}</option> 
						                    					</s:if> 
						                    					<s:else>
						                    						<option  value="0" >${dropdownVal}</option> 
						                    					</s:else>          
						                    				</s:elseif>
						                    				<s:else>
						                    					<s:if test="#dropdownVal.equals(#map.value)">
								                    				<option selected="selected" value="${dropdownVal}" >${dropdownVal}</option>
							                    				</s:if>
							                    				<s:else>
							                    					<option value="${dropdownVal}" >${dropdownVal}</option>
							                    				</s:else>
						                    				</s:else>
						                    			</s:iterator>
					                    			</select>
						                    		<input  type="hidden" name="${param.currentEntityType}#${key}" value='${value}'/>
						                    		<s:if test='"1"==#map.value'>
						                    			<input id="manageModel_input" title='请填写：0或者1，0表示“否”，1表示“是”' onblur="integerOptionOnclick(this);" onfocus="integerTextOnfocus(this);"  type="text" style="margin-left:-139px;position:relative;z-index=2;width:105px;top:1px;border-bottom:none;" value="是"/>
						                    		</s:if> 
						                    		<s:elseif test='"0"==#map.value'>
						                    			<input id="manageModel_input" title='请填写：0或者1，0表示“否”，1表示“是”' onblur="integerOptionOnclick(this);" onfocus="integerTextOnfocus(this);"  type="text" style="margin-left:-139px;position:relative;z-index=2;width:105px;top:1px;border-bottom:none;" value="否"/>
						                    		</s:elseif>
						                    		<s:elseif test="''==#map.value" >
						                    			<input id="manageModel_input" title='请填写：0或者1，0表示“否”，1表示“是”' onblur="integerOptionOnclick(this);" onfocus="integerTextOnfocus(this);"  type="text" style="margin-left:-139px;position:relative;z-index=2;width:105px;top:1px;border-bottom:none;" value=""/>
						                    		</s:elseif>
						                    		<s:else>
						                    			<input id="manageModel_input" title='请填写：0或者1，0表示“否”，1表示“是”' onblur="integerOptionOnclick(this);" onfocus="integerTextOnfocus(this);"  type="text" style="margin-left:-139px;position:relative;z-index=2;width:105px;top:1px;border-bottom:none;" value="${value}"/>
						                    		</s:else>			           		
					                    		</s:if>
					                    		<s:else>
					                    			<input id="manageModel_input" title='请填写：整数，如“12”'  validateDigit="#${param.currentEntityType}_${key}" id="${param.currentEntityType}_${key}" name="${param.currentEntityType}#${key}" type="text" value="${value}" />
					                    		</s:else>
					                    		
					                    	</s:if>
					                    	<s:else>
					                    		<s:if test="dropdownListMap.get(#map.key) != null">
					                    			<select  class="input_select required" onchange="integerOptionOnclick(this);">
					                    				<option value="" selected="selected" >请选择</option>
					                    				
						                    			<s:iterator id="dropdownVal" value="dropdownListMap.get(#map.key)">
						                    				<s:if test='#dropdownVal=="是"'>
						                    					<s:if test='"1"==#map.value'>
						                    						<option selected="selected" value="1" >${dropdownVal}</option> 
						                    					</s:if> 
						                    					<s:else>
						                    						<option  value="1" >${dropdownVal}</option> 
						                    					</s:else>          
						                    				</s:if>
						                    				<s:elseif test='#dropdownVal=="否"'>
						                    					<s:if test='"0"==#map.value'>
						                    						<option selected="selected" value="0" >${dropdownVal}</option> 
						                    					</s:if> 
						                    					<s:else>
						                    						<option  value="0" >${dropdownVal}</option> 
						                    					</s:else>          
						                    				</s:elseif>
						                    				<s:else>
						                    					<s:if test="#dropdownVal.equals(#map.value)">
								                    				<option selected="selected" value="${dropdownVal}" >${dropdownVal}</option>
							                    				</s:if>
							                    				<s:else>
							                    					<option value="${dropdownVal}" >${dropdownVal}</option>
							                    				</s:else>
						                    				</s:else>
						                    			</s:iterator>
					                    			</select>
						                    		<input  type="hidden" name="${param.currentEntityType}#${key}" value='${value}'/>
						                    		<s:if test='"1"==#map.value'>
						                    			<input id="manageModel_input" title='请填写：0或者1，0表示“否”，1表示“是”' onblur="integerOptionOnclick(this);" onfocus="integerTextOnfocus(this);"  type="text" style="margin-left:-139px;position:relative;z-index=2;width:105px;top:1px;border-bottom:none;" value="是"/>
						                    		</s:if> 
						                    		<s:elseif test='"0"==#map.value'>
						                    			<input id="manageModel_input" title='请填写：0或者1，0表示“否”，1表示“是”' onblur="integerOptionOnclick(this);" onfocus="integerTextOnfocus(this);"  type="text" style="margin-left:-139px;position:relative;z-index=2;width:105px;top:1px;border-bottom:none;" value="否"/>
						                    		</s:elseif>
						                    		<s:elseif test="''==#map.value" >
						                    			<input id="manageModel_input" title='请填写：0或者1，0表示“否”，1表示“是”' onblur="integerOptionOnclick(this);" onfocus="integerTextOnfocus(this);"  type="text" style="margin-left:-139px;position:relative;z-index=2;width:105px;top:1px;border-bottom:none;" value=""/>
						                    		</s:elseif>
						                    		<s:else>
						                    			<input id="manageModel_input" title='请填写：0或者1，0表示“否”，1表示“是”' onblur="integerOptionOnclick(this);" onfocus="integerTextOnfocus(this);"  type="text" style="margin-left:-139px;position:relative;z-index=2;width:105px;top:1px;border-bottom:none;" value="${value}"/>
						                    		</s:else>				           	
					                    		</s:if>
					                    		<s:else>
					                    			<input id="manageModel_input" title='请填写：整数，如“12”' class="required" validateDigit="#${param.currentEntityType}_${key}" id="${param.currentEntityType}_${key}" name="${param.currentEntityType}#${key}" type="text" value="${value}" />
					                    		</s:else>
					                    		<span class="redStar">*</span>
					                    	</s:else>
					                    	<s:if test="dropdownListMap.get(#map.key) != null">
					                    		
					                    	</s:if>
					                    	<s:else>
					                    		<em id='manageModel' style="color:#999">(整数)</em>
					                    	</s:else>
				                    	</s:if>
				                    	<%-- 验证浮点型的情况 --%>
				                    	<s:elseif test="attrTypeMap.get(#map.key).indexOf('Double') > -1 || attrTypeMap.get(#map.key).indexOf('Float') > -1">
				                    		<%-- 判断属性的非空情况 --%>
					                    	<s:if test="'true'.equals(nullableMap.get(#map.key))">
					                    		<s:if test="dropdownListMap.get(#map.key) != null">
					                    			<select class="input_select" onchange="optionOnclick(this);">
					                    				<option value="" selected="selected">请选择</option>
						                    			<s:iterator id="dropdownVal" value="dropdownListMap.get(#map.key)">
						                    				<s:if test="#dropdownVal.equals(#map.value)">
							                    				<option selected="selected" value="${dropdownVal}">${dropdownVal}</option>
						                    				</s:if>
						                    				<s:else>
						                    					<option value="${dropdownVal}" >${dropdownVal}</option>
						                    				</s:else>
						                    			</s:iterator>
					                    			</select>	
					                    			<s:if test="#map.value==''">
						                    			<input id="manageModel_input" title='请填写：数值，如“1232.23”，“12.03”' class="number"   name="${param.currentEntityType}#${key}" type="text" style="margin-left:-139px;position:relative;z-index=2;width:105px;top:1px;border-bottom:none;" value=""/>							                    										                    				
					                    			</s:if>		
					                    			<s:else>
						                    			<input id="manageModel_input" title='请填写：数值，如“1232.23”，“12.03”' class="number"  name="${param.currentEntityType}#${key}" type="text" style="margin-left:-139px;position:relative;z-index=2;width:105px;top:1px;border-bottom:none;" value="${value}"/>							                    										                    											                    				
					                    			</s:else>				                    				
					                    		</s:if>
					                    		<s:else>
					                    			<input id="manageModel_input" title='请填写：数值，如“1232.23”，“12.03”' class="number" name="${param.currentEntityType}#${key}" type="text" value="${value}" />
					                    		</s:else>
					                    	</s:if>
					                    	<s:else>
					                    		<s:if test="dropdownListMap.get(#map.key) != null">
					                    			<select class="input_select required"  onchange="optionOnclick(this);">
					                    				<option value="" selected="selected">请选择</option>
						                    			<s:iterator id="dropdownVal" value="dropdownListMap.get(#map.key)">
						                    				<s:if test="#dropdownVal.equals(#map.value)">
							                    				<option selected="selected" value="${dropdownVal}" >${dropdownVal}</option>
						                    				</s:if>
						                    				<s:else>
						                    					<option value="${dropdownVal}" >${dropdownVal}</option>
						                    				</s:else>
						                    			</s:iterator>
					                    			</select>
					                    			<s:if test="#map.value==''">
						                    			<input id="manageModel_input" title='请填写：数值，如“1232.23”，“12.03”' class="number required "  name="${param.currentEntityType}#${key}" type="text" style="margin-left:-139px;position:relative;z-index=2;width:105px;top:1px;border-bottom:none;" value=""/>							                    										                    				
					                    			</s:if>		
					                    			<s:else>
						                    			<input id="manageModel_input" title='请填写：数值，如“1232.23”，“12.03”' class="number required "  name="${param.currentEntityType}#${key}" type="text" style="margin-left:-139px;position:relative;z-index=2;width:105px;top:1px;border-bottom:none;" value="${value}"/>							                    										                    											                    				
					                    			</s:else>							                    		</s:if>
					                    		<s:else>
					                    			<input id="manageModel_input" title='请填写：数值，如“1232.23”，“12.03”' class="number required " name="${param.currentEntityType}#${key}" type="text" value="${value}" />
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
					                    		<input title='请填写：日期时间，如“2012-10-09”，“2012-10-09 11:02:30”' readonly="readonly" name="${param.currentEntityType}#${key}" type="text" value="${value}" 
				                    			class="input_text timeinput" id="time<s:property value='#st.index' />" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
					                    	</s:if>
					                    	<s:else>
					                    		<input title='请填写：日期时间，如“2012-10-09”，“2012-10-09 11:02:30”' readonly="readonly" name="${param.currentEntityType}#${key}" type="text" value="${value}" 
				                    			class="input_text required timeinput" id="time<s:property value='#st.index' />" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
					                    		<span class="redStar">*</span>
					                    	</s:else>
				                    		<%-- <input type="button" class="input_button"  value="选择时间" onclick=fPopCalendar(event,document.getElementById('time<s:property value="#st.index" />'),document.getElementById('time<s:property value="#st.index" />'),true) />
													--%>
											
				                    	</s:elseif>
				                    	<%-- 验证其他类型的情况 --%>
				                    	<s:else>
				                    		<%-- 判断属性的非空情况 --%>
					                    	<s:if test="'true'.equals(nullableMap.get(#map.key))">
					                    		<s:if test="dropdownListMap.get(#map.key) != null">
					                    			<select class="input_select" onchange="optionOnclick(this);">
					                    				<option value="" selected="selected">请选择</option>
						                    			<s:iterator id="dropdownVal" value="dropdownListMap.get(#map.key)">
						                    				<s:if test="#dropdownVal.equals(#map.value)">
							                    				<option selected="selected" value="${dropdownVal}">${dropdownVal}</option>
						                    				</s:if>
						                    				<s:else>
						                    					<option value="${dropdownVal}" >${dropdownVal}</option>
						                    				</s:else>
						                    			</s:iterator>
					                    			</select>
					                    			<s:if test="#map.value==''">
						                    			<input id="manageModel_input" title='请填写：字符串' name="${param.currentEntityType}#${key}" type="text" style="margin-left:-139px;position:relative;z-index=2;width:105px;top:1px;border-bottom:none;" value=""/>							                    										                    				
					                    			</s:if>		
					                    			<s:else>
						                    			<input id="manageModel_input" title='请填写：字符串' name="${param.currentEntityType}#${key}" type="text" style="margin-left:-139px;position:relative;z-index=2;width:105px;top:1px;border-bottom:none;" value="${value}"/>							                    										                    											                    				
					                    			</s:else>							                    		</s:if>
					                    		<s:else>
					                    			<s:if test="attrLengthMap.get(#map.key)>=100">
					                    				<textarea id="manageModel_input" rows="5" name="${param.currentEntityType}#${key}" title='请填写：字符串'>${value}</textarea>
					                    			</s:if>
					                    			<s:else>
					                    				<input id="manageModel_input" title='请填写：字符串' name="${param.currentEntityType}#${key}" type="text" value="${value}" />							                    			
					                    			</s:else>
					                    			
					                    		</s:else>
					                    	</s:if>
					                    	<s:else>
					                    		<s:if test="dropdownListMap.get(#map.key) != null">
					                    			<select class="input_select required"  onclick="optionOnclick(this);">
					                    				<option value="" selected="selected">请选择</option>
						                    			<s:iterator id="dropdownVal" value="dropdownListMap.get(#map.key)">
						                    				<s:if test="#dropdownVal.equals(#map.value)">
							                    				<option selected="selected" value="${dropdownVal}" >${dropdownVal}</option>
						                    				</s:if>
						                    				<s:else>
						                    					<option value="${dropdownVal}" >${dropdownVal}</option>
						                    				</s:else>
						                    			</s:iterator>
					                    			</select>
					                    			<s:if test="#map.value==''">
						                    			<input id="manageModel_input" title='请填写：字符串' class="required"  name="${param.currentEntityType}#${key}" type="text" style="margin-left:-139px;position:relative;z-index=2;width:105px;top:1px;border-bottom:none;" value=""/>							                    										                    				
					                    			</s:if>		
					                    			<s:else>
						                    			<input id="manageModel_input" title='请填写：字符串' class="required"  name="${param.currentEntityType}#${key}" type="text" style="margin-left:-139px;position:relative;z-index=2;width:105px;top:1px;border-bottom:none;" value="${value}"/>							                    										                    											                    				
					                    			</s:else>							                    		</s:if>
					                    		<s:else>
					                    			<s:if test="attrLengthMap.get(#map.key)>=100">
					                    				<textarea id="manageModel_input" title='请填写：字符串' rows="5" class="required" name="${param.currentEntityType}#${key}">${value}</textarea>
					                    			</s:if>
					                    			<s:else>
					                    				<input id="manageModel_input" title='请填写：字符串' class="required" name="${param.currentEntityType}#${key}" type="text" value="${value}" />							                    			
					                    			</s:else>
					                    		</s:else>
					                    		<span class="redStar">*</span>
					                    	</s:else>
				                    	</s:else>
				                    	</span>
				                    	<span class="viewModel">
				                    		<s:if test="attrTypeMap.get(#map.key).indexOf('Integer') > -1">
				                    			<s:if test="dropdownListMap.get(#map.key) != null">
						                    				<s:if test='#map.value=="1"'>是</s:if>
						                    				<s:elseif test='#map.value=="0"'>否</s:elseif>
						                    		
				                    			</s:if>
				                    			<s:else>
				                    			${value}
				                    			</s:else>
				                    		</s:if>
				                    		<s:else>
				                    			${value}
				                    		</s:else>
				                    	</span>
				                    </td>
				                <s:if test="#a % 2 == 0">
				                	</tr>
								</s:if>
			                	<s:else>
			                	</s:else>
			                	<s:set id="a" value="#a+1" />
			                </s:if>
	               		</s:iterator>
	               		<tr>
	               		<td  class="left_td">
	               		<h4 style="background: none repeat scroll 0 0 #E8EDFF;line-height: 26px;padding-left: 4px;">隶属： </h4>
			            </td>
	               		<td colspan="3">
	               		<span class="manageModel" style="display:none">
			          		<input type="text"  readonly id="txtParentRes" value="${parentEntityMap.name}"/><a  class="areaButton"></a>
			          	</span>
			          	<span class="viewModel" id="parentTypeName">
			          		${parentEntityMap.name}
			          	</span>
			          	</td>
	               		</tr>
                    </table>
                   <script type="text/javascript" src="js/choosResourceByTree.js"></script>
                   <div id="chooseResDiv" style="display:none;border:#ccc 1px solid; border-radius:5px; position:absolute; left:250px;top:139px; background:#f1f1f1; z-index:100">
				    	<input type="hidden" id="hidParentTypeGroup" value="${parentEntityTypeGrp}" />
				    	
				    	<%-- 隶属内容选择层 --%>
				    	<s:if test="#request.areaId!=null&&#request.areaName!=null">
					   		<div id="chooseTree" style="padding:0 8px">
					   			<ul id="chooseResUl" style="width:250px;height:300px;overflow:auto;line-height:16px;">
						        	<li>
							        	<img src="image/plus.gif" onclick="showChooseResTypeMsg(this);" />
							        	<s:if test="parentEntityTypeGrp.indexOf('Sys_Area') > -1">
							        		<input class="input_radio" type="radio" name="rdoChooseResChild" id="rootRadio" />
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
						        	<li>
							        </li>
					             </ul>
					   		 </div>
				   		 </s:else>
				   </div>
                </div>
                <div class="tab_content" id="photo_tab_content">
                	<s:if test="#request.hasPhoto!='no'">
                			<div style="height: 30px;">
                				<input style="float: right;" type="button" value="添加" onclick="addPhoto();"/>
                			</div>
							<div class="tab_2_div" style="height: 400px;">
								<s:if test="#request.currentEntityType =='Photo' ">
									<ul style="overflow-x:auto;width: auto;">
										<li style='text-align:center' class='photoLi'>
						  				<img height="350" width="700" src="../../upload/${currentEntityMap.uuidname}" style=""><br>
						  				<em><em style="font-weight:bold">图片名:</em>${currentEntityMap.name}</em>
						  				</li>
									</ul>
								</s:if>
								<s:else>
									<s:if test="photoMapList!=null">
									<ul style="overflow-x:auto;width: auto;">
										<s:iterator id="pl" value="photoMapList" status="st2">
											<s:if test="#st2.index ==0">
												<li style='text-align:center' class='photoLi'>
								  				<img height="350" width="700" src="../../upload/${pl.uuidname}" style=""><br>
								  				</li>
											</s:if>
											<s:else>
												<li style='text-align:center;display:none' class='photoLi' >
								  				<img height="350" width="700" src="../../upload/${pl.uuidname}" style=""><br>
								  				</li>
											</s:else>			
									  </s:iterator>
									</ul>
									<div class="mt8 red">
										<input type="button" id="view" disabled="disabled" class="prev" value="< 上一张"  onclick="showPhoto('prev');"/>
										<s:set id="pl_a" value="0" />
										<s:iterator id="pl" value="photoMapList" status="st2">
											<s:if test="#st2.index ==0">
								  				<em class="photoEm"><em style="font-weight:bold">图片名:</em>${pl.name}</em>
											</s:if>
											<s:else>
								  				<em class="photoEm" style='display:none'><em style="font-weight:bold">图片名:</em>${pl.name}</em>
											</s:else>	
										<s:set id="pl_a" value="#pl_a+1" />		
									    </s:iterator>
									    <s:if test="#pl_a =< 1">
											<input type="button" id="view" value="下一张 >"  disabled="disabled" class="next" onclick="showPhoto('next');"/>
									    </s:if>
									    <s:else>
									    	<input type="button" id="view" value="下一张 >" class="next" onclick="showPhoto('next');"/>
									    </s:else>
									</div>
									</s:if>
								<s:else>
								无图片资源
								</s:else>
								</s:else>	
							</div>
						</s:if>	
						
                </div>
                <div class="tab_content" style="height: 880px; overflow: auto;" id="panel_tab_content">
                	<s:if test="#request.hasPhoto!='no'">
							<div class="tab_2_div">
								<s:if test="#request.currentEntityType=='ODF'||#request.currentEntityType=='FiberCrossCabinet'||#request.currentEntityType=='FiberDistributionCabinet'||#request.currentEntityType=='FiberTerminalCase'">
									<div id="panels_view" style="background:none repeat scroll 0 0 WhiteSmoke; hidden; height: 880px;">
						             	<div id="messages_view" ></div>
						             	<div id="paneltable_box_view">
							             	<div id="paneltable_title_view"></div>
							                <div id="paneltable_view"></div>
						                </div>
						                <div id="terminalmess"></div>
						             </div>
								</s:if>
								<s:elseif test="#request.currentEntityType=='DDF'||#request.currentEntityType=='BaseStation_GSM'||#request.currentEntityType=='PrimaryEquipFrame_GSM'">
								<div id='boardPanel' style=" height: 880px;"></div>
								</s:elseif>
								<s:else>
									功能待开发
								</s:else>
							</div>
						</s:if>
						<s:else>
							<div class="tab_2_div">
								<s:if test="#request.currentEntityType=='ODF'||#request.currentEntityType=='FiberCrossCabinet'||#request.currentEntityType=='FiberDistributionCabinet'||#request.currentEntityType=='FiberTerminalCase'">
									<div id="panels_view" style="background:none repeat scroll 0 0 WhiteSmoke; hidden; height: 880px;">
						             	<div id="messages_view" ></div>
						             	<div id="paneltable_box_view">
							             	<div id="paneltable_title_view"></div>
							                <div id="paneltable_view"></div>
						                </div>
						                <div id="terminalmess"></div>
						             </div>
								</s:if>
								<s:elseif test="#request.currentEntityType=='DDF'||#request.currentEntityType=='BaseStation_GSM'||#request.currentEntityType=='PrimaryEquipFrame_GSM'">
								<div id='boardPanel' style=" height: 880px;"></div>
								</s:elseif>
								<s:else>
									功能待开发
								</s:else>
							</div>
						</s:else>
                </div>
                <div class="tab_content" id="layout_tab_content">
                	<div class="tab_2_div"  id="layout_tab_content_tab_2_div">
                	</div>
                </div>
                
                <div class="tab_content" id="order_tab_content">
                	<h4 class="flattening_info_title mb10">生产工单
                        	<input type="checkbox" checked="checked" id="currentWorkOrder"/>当前工单
                            <input type="checkbox"  id="historyWorkOrder"/>历史工单
                            <span class="absolute_right top_0">
                            	<input type="text" id="workOrderSearchCondition" class="search_textinput view" value="请输入查询条件" onfocus="selectInputfocus(this);" style="color:#999;"/>
                                <input type="button" value="查询" id="view" onclick="searchOrderList('workOrder');"/>
                            </span>
                        </h4>
                        <div class="table_container">
                        <%--<div class="flattening_loading" id="workOrderLoading" style='display:none'><em>数据载入中</em><p></p></div>--%>
                        <div class='flattening_loading' id="workOrderLoading"  style='display:none;text-align:center'><img src='image/loading_img.gif'><br>数据处理中，请稍侯...</div>
                            <table class="flattening_table" id="urTable">
                                
                            </table>
                        </div>
                        <div id='workOrder_paging_div'>
                        
                        </div>
                        
                        
                        <h4 class="flattening_info_title mb10">生产任务
                        	<input type="checkbox" checked="checked" id="currentTaskOrder" />当前任务
                            <input type="checkbox" id="historyTaskOrder"/>历史任务
                            <span class="absolute_right top_0">
                            	<input type="text" class="search_textinput view" value="请输入查询条件" onfocus="selectInputfocus(this);" id="taskOrderSearchCondition" style="color:#999;" />
                                <input type="button" value="查询" id="view" onclick="searchOrderList('taskOrder');"/>
                            </span>
                        </h4>
                        <div class="table_container">
                        	<%--<div class="flattening_loading" id="taskOrderLoading" style='display:none'><em>数据载入中</em><p></p></div>--%>
                        	<div class='flattening_loading' id="taskOrderLoading"  style='display:none;text-align:center'><img src='image/loading_img.gif'><br>数据处理中，请稍侯...</div>
                            <table class="flattening_table" id="toTable">
                                
                            </table>
                        </div>
                         <div id='taskOrder_paging_div'>
                        
                        </div>
                </div>
                
                
                <div class="tab_content" id="flattening_service_tab_content">
					<div class="flattening_service_tab">维护与服务记录</div>
					<div class="flattening_service_info flattening_info_box clearfix" style="height: 764px;">
						<div class="flattening_service_left">
							<div class="flattening_service_left_t" >发生日期：<a href="#" onclick="getMaintenance(); " id="All_Time">全部</a><a href="#" class="selected" onclick="getMaintenanceByMonth();" id="Latest_Month">近1月内</a><a href="#" onclick="getMaintenanceByYear();" id="Latest_Year">近1年内</a>
							<span id="view" class=" top_0">
								<input class="search_textinput view" id="search_textinput_net" type="text" style="color: #999;"  onfocus="selectInputfocus(this);" value="请输入查询条件">
								<input id="view" class="" type="button" onclick="searchMaintenance(this);" value="查询">
							</span>
							</div>
							<ul class="flattening_service_ul" id="som_info_ul">
								
							</ul>
							
						</div>
						<div class="flattening_service_right">
							<table class="flattening_service_table"  id="flattening_service_right">
							</table>
						</div>
					</div>
                </div>
            </div>
        	
        	<div id="source_list_infoMap_div" style="display: none;">
        		<div class="source_list clearfix" id="chi_ul_div">
            	<h4>
                	设备列表：
                    <span class="source_list_tool">
                    	<input type="button" value="添加" onclick="showaddResDiv();"/>
                    	<input type="button" value="删除" onclick="deleteChiInfo();"/>
                    </span>
                </h4>
                <ul id="chi_ul">
                	<li class="selected">畔江花园</li>
                	
                </ul>
            </div>
        		<div class="tab_container" id="chi_infoMap_div">
        		</div>
        	</div>
        </div>
    	</form>
    </div>
    
    <%-- 上传图片使用 --%>
	<div id="messageBox_container">
	     <div class="messageBox_top">
	         <div class="messageBox_top_middle">
	         	<div style="margin-top:6px; float:left;">图片上传</div> 
	         	<div class="messageBox_close" onclick="messageBox_hide();"></div>   
	         </div>
	     </div>
	     <div class="messageBox_main">
			<div class="messageBox_content" id="imgPreviewDiv">
				<div id="fileQueue"></div>
				<div id="imgContent"></div>
			</div>
	        <div class="maessageBox_bottom" id="operaFileUploadDiv">
	  			<input type="file" name=file id="uploadify" />
		  		<p>
		             <input type="button" value="开始上传" onclick="javascript:$('#uploadify').uploadifyUpload()" />&nbsp;&nbsp;&nbsp;
		             <input type="button" value="取消上传" onclick="javascript:$('#uploadify').uploadifyClearQueue()" />
		        </p>
	        </div>
	     </div>
	</div>
	
	<%-- 遮盖层 --%>
	<div id="black" style="z-index:5;width:100%;position:absolute; top:0; left:0; height:100%; background:#FFF; display:none; z-index:2;filter:alpha(opacity=42); opacity:0.6;"></div>
</body>
</html>
