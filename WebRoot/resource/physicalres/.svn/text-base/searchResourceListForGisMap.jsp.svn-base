<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>网络资源MAP</title>
<link rel="stylesheet" type="text/css" href="css/base.css"/>
<link rel="stylesheet" type="text/css" href="css/public.css"/>
<link rel="stylesheet" type="text/css" href="css/iscreate-paging.css"/>
<link rel="stylesheet" type="text/css" href="css/source.css"/>
<link rel="stylesheet" type="text/css" href="css/city.css" />
<style type="text/css">
.flattening_service_right{overflow: hidden; padding:0 0 5px 10px;}
.flattening_loading{background-color:#FFFFFF;}
.flattening_loading em{color: #777777; display: block; font-weight: bold; margin: auto; position: relative; top: 58px; width: 65px;}
.flattening_loading p{background: url(image/loading.gif) no-repeat; width:100px; height:100px; padding-bottom: 10px; margin: auto;}

</style>
<style>
	.gis_dialog{width:320px; border:1px solid #ccc; margin:5px;}
.gis_dialog h2{height:30px; line-height:30px; padding:0 5px; font-weight:normal;background-color: #F0F0F0; border-bottom: 1px solid #CCCCCC;}
.gis_dialog h2 b{display: inline-block; font-weight:bold;}
.gis_dialog h2 span{display: inline-block; color:blue;}
.gis_dialog h2 a{display: inline-block; float: right; text-decoration: underline;}

.dialog_img_box{height:110px; margin-top:5px; overflow: hidden; position:relative; }
.dialog_img_empty_box{height:10px; margin-top:5px; overflow: hidden; position:relative; }
.dialog_img_arrow{position: absolute; right: 0px; top: 90px;}
.dialog_img_left{background:url(../image/img_left.png) no-repeat;}
.dialog_img_right{background:url(../image/img_right.png) no-repeat;}
.dialog_img_left, .dialog_img_right{width:16px; height:16px; cursor:pointer;float: left;}
.dialog_img_page{float: left; line-height: 16px; padding:0 3px;}
.dialog_img_info{position:absolute; width:1800px; top:0px; left:0px;}
.dialog_img_info ul li {float:left; display:inline; text-align:center; margin: 0 2px;}
.dialog_img_info ul li img {display:block; height: 80px; width: 70px; border: 1px solid #999999; padding: 2px;}

.dialog_info{margin-top:-10px;}
.dialog_info_tab{border: 1px solid #ccc; border-bottom: none; float: left; width:70px;line-height: 30px;padding: 0 5px; background-color:#FFF; border-radius: 5px 5px 0 0; margin-left: 10px;}
.dialog_info_other{margin-left: 100px; padding-top:10px;}
.dialog_info_other a{margin: 0 10px;}
.other_icon1, .other_icon2{width:20px; height:16px; display:inline-block; vertical-align: middle;}
.other_icon1{background:url(image/other_icon1.png) no-repeat;}
.other_icon2{background:url(image/other_icon2.png) no-repeat;}

.dialog_info_detail{border-top:1px solid #ccc; margin-top: -1px;}
.maintain_record, .maintain_task{margin:5px 0;}
.maintain_record_name, .maintain_task_name{float: left; width: 70px; line-height: 20px; font-weight: bold;}
.maintain_record_name .fc{font-weight: normal;color: #999999; font-size: 11px}
.maintain_record_list, .maintain_task_list{float: left; width: 230px;}
.maintain_record_list li, .maintain_task_list li{float: left; margin: 0 3px; padding: 2px 6px; background-color:#E9E9E9; border-radius:10px; } 
.maintain_record_list li:hover, .maintain_task_list li:hover{ cursor:pointer; color:blue;  background-color:#ddd;} 

.info_dialog_box{width:400px; position: absolute;  top:0px; z-index:201; background-color:#666; padding:5px;}
.dialog_box_frame{background-color:white; padding:5px;}
.info_dialog_title{border:1px solid #ccc; border-bottom:none; padding-left:10px; line-height:30px; background-color:#f0f0f0;}
.info_dialog_close{background-color: #FFFFFF; border: 1px solid #666; border-radius: 10px; color: #666; cursor: pointer; display: inline-block; font-size: 16px; height: 18px; line-height: 18px; position: absolute; right: -5px;  text-align: center; top: -5px; width: 18px;}
.info_dialog_close:hover{background-color:#666; color:#fff;}
.info_dialog_content table{width:100%;}
.info_dialog_table td { border: 1px solid #CCCCCC; color: #000000; padding: 5px; vertical-align: middle; white-space: nowrap; background:#FFFFFF;}
.info_dialog_table .info_dialog_td { background-color:#ebf2f8; text-align: right;width:80px;}
.info_dialog_black {background: none repeat scroll 0 0 rgba(0, 0, 0, 0.5); visibility:hidden; height: 999%;left: 0; position: fixed;top: 0;width: 100%;z-index: 200;}


.allinfo_dialog_box{width:850px; position: absolute; top:0px; z-index:201; background-color:#666; padding:5px;}
.allinfo_dialog_table {background: none repeat scroll 0 0 #FFFFFF; margin: 0 auto 10px;width: 100%;}
.allinfo_dialog_table tr td {background-color: #FFFFFF; border: 1px solid #ccc; line-height: 21px;text-align:center; padding: 0 2px; vertical-align: middle; white-space: pre-wrap; color: #444444; }
.allinfo_dialog_table th {background-color:#ebf2f8; border: 1px solid #ccc; line-height: 30px; padding: 0 2px; white-space: nowrap;text-align:center;}

.dialog_big_img{position:absolute; background:#F2F7FF none repeat scroll 0 0; border:1px solid #ccc; padding:5px; width:300px; height:300px;}
.dialog_big_img span{border-bottom:14px dashed transparent; border-top:14px dashed transparent; font-size:0; height:0; line-height:0; position:absolute; width:0; display:block;}
.dialog_big_img span span{top:-14px;}
.dialog_big_img .leftSpan{border-right:14px solid #ccc; left:-14px;}
.dialog_big_img .leftSpan span{border-right:13px solid #F2F7FF; left:2px;}
.dialog_big_img .rightSpan{border-left:14px solid #ccc; left:311px;}
.dialog_big_img .rightSpan span{border-left:13px solid #F2F7FF; left:-15px;}

.dialog_info_detail ul{height: 100px; overflow: auto;padding: 5px;}
.dialog_info_detail ul li{padding: 3px 0;}
</style>
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
<script type="text/javascript"	src="../../jslib/gis/jslib/api/iscreate_map.js"></script>
<script type="text/javascript" src="../../jslib/date/wdatePicker.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.form.js"></script>
<script type="text/javascript" src="../../jslib/paging/paging.js"></script>
<script type="text/javascript" src="../js/dateutil.js"></script>
<script type="text/javascript" src="../js/url.js"></script>
<script type="text/javascript"	src="js/searchResourceListGisMap.js"></script>
<script type="text/javascript" src="../js/objutil.js"></script>
<script type="text/javascript" src="js/gisArea.js"></script>
<script>
$(function(){
	
	
	
// 美工代码
$(function(){
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
})
	
	
	//设置地图样式
	var window_width = $(window).width();
	var window_height = $(window).height();
	var top_width = $(".map_wrapper").width();
	var top_height = $(".map_wrapper").height();
	
	$("#gis_map").css({
		"width" : top_width+"px" , 
		"height" : (window_height-top_height-15)+"px" 
	});
	$("#resource_list_box_main_ul").css("height",(window_height-top_height-101)+"px");
	
	var sType = $("#saveSType").val();
	var currentEntityId = $("#saveCurrentEntityId").val();
	if(sType!=""){
		$("#searchType").val(sType);
		
		$("#sTypeText").val('${chineseName}');
		$("#saveChineseName").val('${chineseName}');
		var params = {selectResType:sType};
		$.post('getSearchAttributesAction',params,function(data){
			$(".up_search").remove();
			$(".search_result ul").append(data);
			if($(".up_search_show").text()=="基本查询 ▲"){
				$(".up_search").show();
			}else{
				$(".up_search").hide();
			}
		})
		
		
		$("#flattening_loading_div").show();
		var pageSize = $("#savePageSize").val();
		if(pageSize==""){
			pageSize=25;
		}else{
			pageSize = parseInt(pageSize);
		}
		$("#pageSize_ul a").removeClass("selected");
	 	$("#pageSize"+pageSize+" a").addClass("selected");
		loadSearchResource(pageSize,null);//加载数据
	}
	
	if ( '${sType}' != "" ) {
		$("#logicNet_div").css({"display" : "none"});
	} else {
		$("#logicNet_div").css({"display" : "block"});
	}
	
		
	
	//查询按钮
	$("#searchForm").submit(function(){
			$("#resource_list_box_ul_div").hide();
			$("#flattening_loading_div").show();
			var pageSize = gettpageSize();
			$("#pageSize").val(pageSize);
			$("#savePageSize").val(pageSize);
			var searchType=$("#searchType").val();
			if(searchType==""){
				alert("请先选择资源类型");
				return false;
			}
			var flag=false;
			/*判断开始时间与结束时间对比*/
			if($("input.date").size()>0){
				$("input.date").each(function(){
					var beginDate=$(this).val();
					var endDate =$(this).next().next().next().val();
					if($.trim(beginDate)!="" && $.trim(endDate)!=""){
						beginDate=Date.parse(beginDate);
						endDate=Date.parse(endDate);
						if(beginDate>endDate){
							flag=true;
						}
					}
				})
				if(flag){
					alert("属性筛选条件的开始时间不能大于结束时间");
					return false;
				}
					
			}
				var s="";
				var i=0;
				$("input.date").each(function(){
					var beginDate = $(this).val();
					var beginDateId = $(this).attr("name");
					var endDate = $(this).next().next().next().val();
					if(i==0){
						s +=beginDateId+":"+beginDate+"/"+endDate;
					}else{
						s +=","+beginDateId+":"+beginDate+"/"+endDate;
					}
					i++;
				})
			$("input[type='hidden'][id='dateString']").val(s);

			//显示数据加载提示层
			$("#loading_div").show();
			$("#black").show();
			$("#saveAreaId").val($("input[id='areaId']").val());
			$("#saveAreaType").val("Sys_Area");
			$("#saveOrderType").val("name");
			$("#saveSType").val($("input[id='searchType']").val());
			$("#savePageIndex").val("1");
			$("input[name='cityAreaId']").val($("#cityAreaId").val());
			$("input[name='districtAreaId']").val($("#districtAreaId").val());
			$("input[name='streetAreaId']").val($("#streetAreaId").val());
			return true;
		
		
	});
	
	//AJAX提交表单
	$("#searchForm").ajaxForm({
		success:function(data){
			if(data!=null){
				 showResourceHTML(data,null);
			}
		},
		dataType:'json'
	});
	
	

loadParentArea();
	$(".up_search_show").click(function(){
		var searchType=$("#searchType").val();
		if(searchType==""){
			alert("请先选择资源类型");
		}else{
			if($(".up_search").css("display") == "none"){
				$(".up_search_show").text("基本查询 ▲");
				$(".up_search").css("display","block");
			}else{
				$(".up_search_show").text("高级查询 ▼");
				$(".up_search").css("display","none");
			}
		}
	})
	$(".switch").click(function(){
		$(this).hide();
		$(".switch_hidden").show();
		$(".resource_list_icon").animate({right:'0px'},'fast');
		$(".resource_list_box").hide("fast");
	})
	$(".switch_hidden").click(function(){
		$(this).hide();
		$(".switch").show();
		$(".resource_list_icon").animate({right:'286px'},'fast');
		$(".resource_list_box").show("fast");
	})
	$(".search_box_close").click(function(){
		$(".search_box_alert").slideToggle("fast");
	})
	$(".zy_show").click(function(){
		$.post("getSearchTypeAction","",function(data){
			var aetgs = data.aetgs;
			var aets = data.aets;
			var content = "";
			$.each(aetgs,function(key,value){
				content+="<h4>"+value+"：</h4><p id='"+key+"'></p>"
			})
			$(".search_box_close").siblings().remove();
			$(".search_box_close").after(content);
			$.each(aets,function(k,v){
				content="";
				$.each(v,function(k1,v1){
					content +="<span ><input type='radio' name='searchType' value='"+k1+"' onclick ='selectType(this);'/><i>"+v1+"</i></span>";
				})
				$("#"+k).html(content);
			})
			$(".search_box_alert").slideToggle("fast");
		},'json')
	})
})

//加载区域数据

function loadAreaResource(areaId,searchType,searchTypeText){
	$("#resource_list_box_ul_div").hide();
	$("#flattening_loading_div").show();
	$("input[name='cityAreaId']").val($("#cityAreaId").val());
	$("input[name='districtAreaId']").val($("#districtAreaId").val());
	$("input[name='streetAreaId']").val($("#streetAreaId").val());
    var areaType = 'Sys_Area';
    var sType = searchType;
    var conditionString = "";
    var orderType = "name";
    var pageIndex = "1";
    var pageSize ="25";
	var params = {areaId:areaId,areaType:areaType,sType:sType,conditionString:conditionString,pageIndex:pageIndex,pageSize:pageSize,orderType:orderType};
	$(".search_result input[type='text']").each(function(){
    	$(this).val("");
    })
    $(".search_result select").each(function(){
    	$(this).val("");
    })
	$.post('searchResourceByPageNewAction',params,function(data){
		$("#saveSType").val(searchType);
		$("#saveAreaId").val(areaId);
   		$("#saveAreaType").val("Sys_Area");
        $("#saveConditionString").val("");
        $("#saveOrderType").val('name');
        $("#savePageIndex").val('1');
        $("#savePageSize").val('25');
        $("#saveChineseName").val(searchTypeText);
		if(data!=null){
			showResourceHTML(data,null);
		}else{
			$("#resource_list_box_ul_div").show();
			$("#flattening_loading_div").hide();
		}
		
	},'json');	

}

//加载数据
function loadSearchResource(pageSize,current){
	$("#resource_list_box_ul_div").hide();
	$("#flattening_loading_div").show();
    var areaId = $("#saveAreaId").val();
    var areaType = $("#saveAreaType").val();
    var sType = $("#saveSType").val();
    var conditionString = $("#saveConditionString").val();
    var orderType = $("#saveOrderType").val();
    var pageIndex = 1;
    if($("#savePageIndex").val()){
    	pageIndex = $("#savePageIndex").val();
    }
	var params = {areaId:areaId,areaType:areaType,sType:sType,conditionString:conditionString,pageIndex:pageIndex,pageSize:pageSize,orderType:orderType};
	$.post('searchResourceByPageNewAction',params,function(data){
		if(data!=null){
			//alert(data)
			//resource_list_box_div
			//resource_list_box_title_div
			//resource_list_box_main_div
			//resource_list_box_paging_div
			//resource_list_box_main_ul
			showResourceHTML(data,current);
		}
	},'json');
}

//跳转列表呈现
function submitEventForList(){
	$("#forwardForm").submit();
}

function selectType(me){
	$("#searchType").val($(me).val());
	$("#sTypeText").val($(me).next().text())
	$("#saveChineseName").val($(me).next().text());
	var params = {selectResType:$(me).val()};
	$.post('getSearchAttributesAction',params,function(data){
		$(".up_search").remove();
		$(".search_result ul").append(data);
		if($(".up_search_show").text()=="基本查询 ▲"){
			$(".up_search").show();
		}else{
			$(".up_search").hide();
		}
	})
	$(".search_box_alert").slideToggle("fast");
	$(".search_result input[type='text']").each(function(){
    	$(this).val("");
    })
	$("#searchForm").submit();
}
	 var ids = "";
	 var userAreaIds = "";
	 var oAreaList = "";
	 var areaByUserId = "";
	 var childAreaList = "";
	 function loadParentArea(){	
		 return;
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
						areaByUserId = data.areaByUserId;
						childAreaList = data.childAreaList;
		 		if(data.userAreaMap){
					$("#areaId").val(data.userAreaMap.id);
			 		$("#areaType").val(data.userAreaMap._entityType);
		 		}
				//alert(data);
				//alert(data.operationalAreaList);
				var counttext = "";
			 	var con = "";
				if(data.parentArea){
					var ind = data.parentArea.length;
					var isGo = false;
					var parentArea = data.operationalChildAreaList;
					oAreaList = data.oAreaList;
					$.each(data.parentArea, function(index, obj){
						//alert(obj.name);
						j = index;
						if(index == 0){
							parentAreatext = obj.name;
							parentAreavalue = obj._entityType+","+obj.id+","+obj.name+","+obj.longitude+","+obj.latitude;
							$.post("getParentAreaAction",function(data){
								selectIds = "province_select";
								var count = "<select onchange='selectChildArea(this);' onclick='clickSelectArea(this);' id='province_select0'>"
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
							 		counttext = counttext + "<select onchange='selectChildArea(this);' onclick='clickSelectArea(this);' id='province_select"+c+"' value=''>"
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
					//counttext = counttext + con;
					
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
					
					},500);
				}
				
				if(data.operationalAreaList){
					$.each(data.operationalAreaList, function(index, obj){
						ids = ids + obj + ",";
					});
				}
				if(data.operationalChildAreaList){
					$.each(data.operationalChildAreaList, function(index, obj){
						userAreaIds = userAreaIds + obj + ",";
					});
				}
				
					}
					});
			 	
	 }

	 function selectChildArea(me){
	 	if($(me).val()){
			if($(me).val() == '请选择'){
	 			$(me).next().html("");
	 			return;
	 		}
	 	var val = $(me).val();
	 	var array = val.split(",");
	 	var parentEntityType = array[0];
	 	var parentEntityId = array[1];
	 	var parentName = array[2];
	 	var longitude = array[3];
	 	var latitude = array[4];
	 	var userAreaIdsarr = userAreaIds.split(",");
	 	if(childAreaList != null && childAreaList != ""){
	 		$.each(childAreaList,function(k,y){
	 			if(y == parentEntityId){
		 			$("#areaId").val(parentEntityId);
			 		$("#areaType").val(parentEntityType);
				 	
	 			}
	 		});
	 	}else{
			 	$("#areaId").val(parentEntityId);
			 	$("#areaType").val(parentEntityType);	 	
	 	}
	 	
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
		 		}else{
		 			$(me).next().html("");
		 		}
		 		
		 		
		 	});
	 	}
	 	
	 }
	 
	 //设置每页行数
	 function setpageSize(pageSize,me){
	 	$("#pageSize_ul a").removeClass("selected");
	 	$(me).addClass("selected");
	 	$("#resource_list_box_ul_div").val();
	 	$("#flattening_loading_div").val();
	 	var current = $("#resource_list_box_title_div .current").next().val();
	 	$("#pageSize").val(pageSize);
	 	$("#savePageSize").val(pageSize);
	 	var  sType = $("#saveSType").val();//资源类型
	 	if(sType!=""){
	 		loadSearchResource(pageSize,current);
	 	}
	 }
	 
	 function setOrderType(saveOrderType,me){
	 	$("#saveOrderType").val(saveOrderType);
		var pageSize = gettpageSize();
	 	loadSearchResource(pageSize,saveOrderType);
	 }
	 
	  function gettpageSize(){
	  	//$("#resource_list_box_title_div a").removeClass("selected");
	  	//$(me).addClass("selected");
	  	var pageSize = 0;
	  	var pageSizeText = $("#pageSize_ul .selected").html();
	  	if(pageSizeText != null){
	  		if(pageSizeText.indexOf("分页显示") > -1){
	  			pageSize = 25;
	  		}else if(pageSizeText.indexOf("显示50个") > -1){
	  			pageSize = 50;
	  		}else if(pageSizeText.indexOf("显示100个") > -1){
	  			pageSize = 100;
	  		}else if(pageSizeText.indexOf("显示200个") > -1){
	  			pageSize = 200;
	  		}else{
	  			pageSize = 0;
	  		}
	  	}
	  	return pageSize;
	  }
	  
	  
	function showResourceHTML(data,current){	
	//保存查询条件 
	  var conditionString = data.conditionString; 
	  $("#saveConditionString").val(conditionString);
	  var context = "";
			context = "<div class='sort_box clearfix'>"
						+"<em class='sort_title'>排序：</em>";
			$.each(data.titleMap,function(key,value){
				if(key == "name" || key == "label" || key == "depositDate"){
					if(current == key){
						context = context + "<a href='javascript:void(0);' class='current' onclick='setOrderType(\""+key+"\",this);'><span class='arrow_down'>"+value+"<em class='down_tip'></em></span></a>"
											+ "<input type='hidden' value='"+key+"'/>";
					}else{
						context = context + "<a href='javascript:void(0);' onclick='setOrderType(\""+key+"\",this);'><span class='arrow_down'>"+value+"<em class='down_tip'></em></span></a>"
											+ "<input type='hidden' value='"+key+"'/>";
					}
				}
			});
			context = context + "</div>";
			//alert(context);
			$("#resource_list_box_title_div").html(context);
			var contextmain = "";
			var index = 0;
			var num = 1;
			$.each(data.contentMapList,function(key,value){
			contextmain = contextmain
				+"<li class='resource_item clearfix' onclick='setMapCenterByResourceId("+value['id']+");'>"
					+"<span class='num'>"+num+"</span>"
					+"<div class='r'>";
				var nameLabel = "";
				var vkey = "";
						if(value['name'] != null && value['name'] != ""){
							nameLabel = value['name'];
							vkey = "name";
						}else{
							nameLabel = value['label'];
							vkey = "label";
						}
						var chineseName = $("#saveChineseName").val();
						contextmain = contextmain + "	<span>"+chineseName+"：<em>"+nameLabel+"</em></span>"
									+"<p class='name mt5'>";
						var assName = data.assNameList[index];
						contextmain = contextmain + "	<span>所属：<em>"+assName+"</em></span>";
				$.each(data.titleMap,function(k,v){
					if(k == "importancegrade"){
						var importancegrade = "";
						if(value['importancegrade'] != null && value['importancegrade'] != ""){
							importancegrade = value['importancegrade'];
						}
						contextmain = contextmain + "	<span>"+v+"：<em>"+importancegrade+"</em></span>";
					}
				});
				contextmain = contextmain + "</p>";
				$.each(data.titleMap,function(k,v){
					if(k == "address"){
						var address = "";
						if(value['address'] != null && value['address'] != ""){
							address = value['address'];
						}
						contextmain = contextmain + "<p class='name mt5'>"+v+"：<em>"+address+"</em></p>";
					}
				});
				contextmain = contextmain + "</div>"
				"</li>";
				num++;
				index++;
			});
			$("#resource_list_box_main_ul").html(contextmain);
			pagingColumnByForeground("resource_list_box_paging_div",$("#resource_list_box_main_ul li"),5);
			$("#resource_list_box_ul_div").show();
			$("#flattening_loading_div").hide();
			//地图展示
			setMapResourceList(data.contentMapList);
			//根据marker地图定位
			var entityId = $("#saveCurrentEntityId").val();
			setMapCenterByResourceId(entityId);
	}
</script>

</head>
<body>
	<%--主体开始--%>
	<div class="map_wrapper">
		<div class="map_hd">
		<form id="forwardForm" method="post" action="forwardSearchResourceGisPageAction" >
			<input type="hidden" id="saveCurrentEntityId" name="currentEntityId" value="${currentEntityId}"/>
	   		<input type="hidden" id="saveCurrentEntityType" name="currentEntityType" value="${currentEntityType}" />
			<input type="hidden" id="saveAreaId"  name="areaId" value="${areaId}"/>
	   		<input type="hidden" id="saveAreaType" name="areaType" value="${areaType}" />
	   		<input type="hidden" id="saveSType" name="sType" value="${sType}"/>
	   		<input type="hidden" id="saveChineseName" name="chineseName" value="${chineseName }"/>
	   		<input type="hidden" id="saveConditionString" name="conditionString" value="${conditionString}"/>
	   		<input type="hidden" id="savePageIndex" name="pageIndex" value="${pageIndex}"/>
	   		<input type="hidden" id="saveOrderType" name="orderType" value="${orderType}"/>
	   		<input type="hidden" id="savePageSize" name="pageSize" value="${pageSize}"/>
	   		<input type="hidden" id="forwardType" name="forwardType" value="listPage"/>
	   		<input type="hidden" id="saveCityAreaId" name="cityAreaId" value="${cityAreaId}"/>
			<input type="hidden" id="saveDistrictAreaId" name="districtAreaId" value="${districtAreaId}"/>
			<input type="hidden" id="saveStreetAreaId" name="streetAreaId" value="${streetAreaId}"/>
   		</form>
   		<input type="hidden" id="cityAreaId"  value=""/>
		<input type="hidden" id="districtAreaId"  value=""/>
		<input type="hidden" id="streetAreaId"  value=""/>
   		
		<form id="searchForm" method="post" action="searchResourceByPageNewAction" >
		<input type="hidden" value="25"  name="pageSize" id="pageSize"/>
		<input type="hidden" value="1"  name="pageIndex"/>
		<input type="hidden" value="" id="dateString" name="dateString"/>
		<input type="hidden" value="" id="areaId" name="areaId"/>
		<input type="hidden" value="" id="areaType" name="areaType"/>
		<input type="hidden" value="" id="searchType" name="sType"/>
		<input type="hidden" value="name"  name="orderType"/>
			<div class="head_box clearfix">
				<div class="search_box">
                   	<div id="top_tool_bar" class="">
                        		<%-- 区域选择 --%>
                            	<input type="hidden" id="select_area_id" />
				            	<div class="toolBar_area" style="white-space:nowrap;">选择区域：
				            		<em id="" class="city_title">
				            			<a title="选择城市" class="city_expand city_show" id="city_select" href="javascript:void(0)">请选择</a>
										<a title="选择城区" class="city_expand district_show" style="display:none;" id="district_select" href="javascript:void(0)">请选择</a>
										<a title="选择街道" class="city_expand street_show" style="display:none;" id="street_select" href="javascript:void(0)">请选择</a>
				            		</em>
				            		<em id="searchEm" style="display:none"> <span id="refreshSecond" style="color:red;">3</span>秒后查询资源<input type="button"  class="search_button" value="取消" onclick="cacelSearchResource();"></em>
				                </div>
				                <div class="map_city" style="z-index: 999;">
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
								<div class="map_district" style="z-index: 999;">
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
								<div class="map_street" style="z-index: 999;">
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
                    <div class="search_box_rightA">
                       	<span>资源类型：</span>
                           <em class="area_content">
                               <input type="text" value="" id="sTypeText"/><a class="areaButton zy_show"></a>
                           </em>
                           <div class="search_box_alert">
                           	<div class="search_box_close"></div>
                           	<h4>点设施资源：</h4>
                               <p id="Search_Facilities">
                               	
                               </p>
                               <h4>无线资源：</h4>
                               <p id="Search_Wireless">
                               	
                               </p>
                               <h4>传输资源：</h4>
                               <p id="Search_Transmission">
                               	
                               </p>
                               <h4>动力资源：</h4>
                               <p id="Search_Power">
                               	
                               </p>
                               <h4>Wlan资源：</h4>
                               <p id="Search_Wlan">
                               	
                               </p>
                               <h4>环境监控资源：</h4>
                               <p id="Search_EvironmentAndMonitoring">
                               	
                               </p>
                           </div>
                       </div>
				</div>
                <div class="search_result">    
               		<ul>
	                 	<li>
	                     	<span class="title_span">资源名称：</span>
	                         <input type="text" name="name"/>
	                     	<span class="title_span">资源编码：</span>
	                         <input type="text" name="label"/>
	                     	<em class="search_box_rightA" style="right:6px; top:6px;">
	                         <input type="submit" class="search_button" value="查询">
	                         <a href="#" class="up_search_show">高级查询 ▼</a>
	                        </em>
	                     </li>  
                	</ul>
                </div>
			</div>
			</form>
			<div id="logicNet_div" style=" display:none; position:absolute;bottom:-35px; left:70px;">
				<input type="button" class=" not_selected2" value="管道网" id="Pipe"/>
				<input type="button" class=" not_selected2" value="光缆网" id="Fiber"/>
				<%-- <input type="button" class=" not_selected2" value="纤芯" id="FiberCore"/> --%>
				<input type="button" class=" not_selected2" value="光路网" id="OpticalRoute"/>
				<input type="button" class=" not_selected2" value="传输拓扑" id="Transmission"/>
			</div>
			<div class="present_box">
				<div class="r">
					<ul class="present clearfix" id="pageSize_ul">
						<li>资源GIS显示：</li>
						<li id="pageSize25">
							<a href="#" class="selected" onclick="setpageSize(25,this);"><em></em>分页显示</a>
						</li>
						<li id="pageSize50">
							<a href="#" onclick="setpageSize(50,this);"><em></em>显示50个</a>
						</li>
						<li id="pageSize100">
							<a href="#" onclick="setpageSize(100,this);"><em></em>显示100个</a>
						</li>
						<li id="pageSize200">
							<a href="#" onclick="setpageSize(200,this);"><em></em>显示200个</a>
						</li>
						<li id="pageSize0">
							<a href="#" onclick="setpageSize(0,this);"><em></em>显示全部</a>
						</li>
					</ul>
					
				</div>
				<ul class="present_tab clearfix">
					<li>
						<a class="gis select" style="cursor:auto" ><em></em>GIS呈现</a>
					</li>
					<li>
						<a  class="list"  onclick="submitEventForList();"  style="border-left:none;"><em></em>列表呈现</a>
					</li>
				</ul>	
			</div>	
		</div>
		<div class="map_bd">
			<div class="resource_list_icon">
				<a href="#" class="switch"></a>
				<a href="#" class="switch_hidden"></a>
				<div class="shad_v"></div>
			</div>
			<div class="resource_list_box" id="resource_list_box_div" style="height:100%; width: 28%;">
				<div  id="resource_list_box_ul_div">
					<div id="resource_list_box_title_div">
					</div>
					<%--
					<div class="no_result hidden" style="height: 597px;" style="display: none;">
						很抱歉，暂时无法找到符合您要求的资源。<br>建议您缩小地图或者改变条件重新搜索。
					</div>
					--%>
					<div id="resource_list_box_main_div" class="resource_list">
						<ul  id="resource_list_box_main_ul" style="overflow-y:auto;height:284px">
						</ul>
					</div>
					<div id="resource_list_box_paging_div" class="resource_page">
					</div>
				</div>
				<%--<div id="flattening_loading_div" class='flattening_loading' style="display: none;">
					<em>数据载入中</em><p></p>
				</div>--%>
				<div  id="flattening_loading_div"  style="text-align:center;display: none;">
		        	<img src="image/loading_img.gif"><br>数据处理中，请稍侯...
		    	</div>
				
			</div>
			<div class="htl_map_move">
				<div id="gis_map" >
					<%-- 地图 --%>
				</div>
			</div>
		</div>
	</div>
</body>
</html>