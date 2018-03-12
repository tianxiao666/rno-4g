<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>网络资源查询</title>
<link rel="stylesheet" type="text/css" href="css/base.css"/>
<link rel="stylesheet" type="text/css" href="css/public.css"/>
<link rel="stylesheet" type="text/css" href="css/dialog.css"/>
<link rel="stylesheet" type="text/css" href="css/iscreate-paging.css"/>
<link rel="stylesheet" type="text/css" href="css/source.css"/>
<link rel="stylesheet" type="text/css" href="css/loading_cover.css" />
<link rel="stylesheet" type="text/css" href="css/city.css" />
<style type="text/css">

</style>

<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="../../jslib/date/wdatePicker.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.form.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.validate.js"></script>
<script type="text/javascript" src="../js/objutil.js"></script>
<script type="text/javascript" src="js/gisArea.js"></script>
<script type="text/javascript" src="../js/validate.ex.js"></script>
<script>



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


$(function(){
	var sType = $("#saveSType").val();
	var forwardType = '${forwardType}';
	if(forwardType=="listPage"){
		$("#forwardForm").attr("target","");
	}
	if(sType!=""){
		$("#searchType").val('${sType}');
		$("#sTypeText").val('${chineseName}')
		var params = {selectResType:'${sType}'};
		$.post('getSearchAttributesAction',params,function(data){
			$(".up_search").remove();
			$(".search_result ul").append(data);
			if($(".up_search_show").text()=="基本查询 ▲"){
				$(".up_search").show();
			}else{
				$(".up_search").hide();
			}
		})
		$(".loading_cover").show();
		$(".loading_fb").html('${chineseName}');
		loadResource();//加载数据
	}
	
	//删除查询的资源
	$("#btnDeleteRes").click(function(){
		//先把需要删除的个数保存起来
		var deleteCount = $("input[name='cbxResEntity']:checked").length;
	
		if(deleteCount == 0) {
			alert("请选择要删除的资源!");
			return false;
		}
		
		if(!confirm("确定要删除这些资源及其下子资源吗?")) {
			return false;
		}
		
		//加载层和遮盖层出现
		
		//获取要被删除的查询资源的id和type
		$("input[name='cbxResEntity']:checked").each(function(index){
			var chooseResEntityId = "";
			var chooseResEntityType = "";
			var cbxRes = $(this);
			var name = cbxRes.parent().parent().attr("name");
			if(name!="" && name!=undefined){
				chooseResEntityType =  name.substring(name.indexOf("#")+1);
				chooseResEntityId = name.substring(0,name.indexOf("#"));
			}
			var params = {chooseResEntityId:chooseResEntityId,chooseResEntityType:chooseResEntityType}
			
			//删除选中的资源
			$.post("delPhysicalresByRecursionAction",params,function(data){
				if(data == "success") {
					//后台删除成功后，删除表格中对应的行
					cbxRes.parent().parent().remove();
				}
				
				//已到达最后一个删除元素，删除后进行提示操作
				if(index == deleteCount - 1) {
					//加载层和遮盖层隐藏	
					alert("删除数据完毕!");
				}
			},'json');
		});
	});


	//查询按钮
	$("#searchForm").submit(function(){
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
			$("#savePageSize").val("25");
			$("input[name='cityAreaId']").val($("#cityAreaId").val());
			$("input[name='districtAreaId']").val($("#districtAreaId").val());
			$("input[name='streetAreaId']").val($("#streetAreaId").val());
			$(".loading_cover").show();
			$(".loading_fb").html($("#sTypeText").val());
			return true;
		
		
	});
	
	//AJAX提交表单
	$("#searchForm").ajaxForm({
		success:function(data){
			$(".loading_cover").hide();
			if(data!=null){
				 showResource(data);
			}
		},
		dataType:'json'
	});
	
	//表单jQuery验证
	$("#operaForm").validate({
		submitHandler: function(form) {
			var parentId = $("input[name='addedResParentEntityId']").val();
			if(parentId==""){
				if($("#operaForm").attr("action")=="addResourceNewAction"){
					if(!confirm("未选择隶属资源，是否添加？")){
						return false;
					}
				}
				
			}
			//表单AJAX提交
			$("#operaForm").ajaxSubmit({
				success:function(data){
					if($("#operaForm").attr("action")=="addResourceNewAction"){
						data = eval(data);
						if(data!="error"){
							if(confirm("添加成功，是否进行修改该资源？")){
								var parentId = $("input[name='addedResParentEntityId']").val();
								var parentType = $("input[name='addedResParentEntityType']").val();
								var areaId = $("input[name='currentAreaId']").val();
								var currentEntityType = $("input[name='addedResEntityType']").val();
								var currentEntityId=data;
								forwardToUpdatePage(currentEntityId,currentEntityType,parentId,parentType,areaId);
							}else{
								$(".dialog_close").click();
							}
						}else{
							alert("保存失败.")
						}
					}else{
						data = eval(data);
						if(data=="success"){
							alert("修改成功");
							$(".dialog_close").click();
						}else{
							alert("修改失败.")
						}
					}
					
				}
			})
		}
	})
	

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
	$(".search_box_close").click(function(){
		$(".search_box_alert").slideToggle("fast");
	})
	
	$(".dialog_open").click(function(){
		showAddDialog();
	})
	$(".dialog_close").click(function(){
		$("#dialog").hide();
	})
	
})


//批量操作，全选，全反选操作
	function operaAllCbx(me) {
		$("#showContent input[name='cbxResEntity']").each(function(){
			//操作选中或反选，并显示相应的颜色
			if($(me).attr("checked") == "checked") {
				$(this).attr("checked", true);
			} else {
				$(this).attr("checked", false);
			}
		});
	}


//显示增加资源dialog
function showAddDialog(){
	var sType = $("#searchType").val();
	if(sType==""){
		alert("请先选择资源类型。");
		return false;
	}
	$(".dialog_title").html("添加："+$("#sTypeText").val());	
	var areaId = $("#areaId").val();
	var params ={sType:sType,areaId:areaId};
	$.post("loadAddResourcePageAction",params,function(data){
		$("#addcontent").html(data);
		$("#dialog").show();
	})
	
}

//跳转Gis地图
function submitEventForGis(){
	$("#saveCurrentEntityId").val("");
	$("#saveCurrentEntityType").val("");
	$("#forwardForm").submit();
}
//地图定位
function mapLocation(id,type){
	$("#saveCurrentEntityId").val(id);
	$("#saveCurrentEntityType").val(type);
	$("#forwardForm").submit();
}

//导出
function exportData(){
	var sType = $("#saveSType").val();
	if(sType==""){
		alert("请先查询数据");
		return false;
	}
	$("#forwardForm").attr("action","exportSearchResourceDataAction").removeAttr("target");
	$("#forwardForm").submit();
	$("#forwardForm").attr("action","forwardSearchResourceGisPageAction").attr("target","_blank");
}
//跳转到资源修改页面
function forwardToUpdatePage(currentEntityId,currentEntityType,parentEntityId,parentEntityType,areaId){
    var param="";
	if(parentEntityId!=""){
		param ={currentEntityId:currentEntityId,currentEntityType:currentEntityType,parentEntityId:parentEntityId,parentEntityType:parentEntityType,areaId:areaId};
	}else{
		param ={currentEntityId:currentEntityId,currentEntityType:currentEntityType,areaId:areaId};
	}
	
	$.post("loadUpdateResourcePageAction",param,function(data){
		$("#addcontent").html(data);
		$("#dialog").show();
	})

}

//加载区域数据

function loadAreaResource(areaId,searchType,searchTypeText){
	if(areaId == null || areaId == ""){
		return;
	}
	$(".loading_cover").show();
	$(".loading_fb").html(searchTypeText);
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
		$(".loading_cover").hide();
		if(data!=null){
			showResource(data);
			if(pageSize=="0"){
				$("#totalPage").html("1");
			}
		}
		
	},'json');	
	
}


//加载数据
function loadResource(){
	var areaId = $("#saveAreaId").val();
    var areaType = $("#saveAreaType").val();
    var sType = $("#saveSType").val();
    var conditionString = $("#saveConditionString").val();
    var orderType = $("#saveOrderType").val();
    var pageIndex = $("#savePageIndex").val();
    var pageSize = $("#savePageSize").val();
	var params = {areaId:areaId,areaType:areaType,sType:sType,conditionString:conditionString,pageIndex:pageIndex,pageSize:pageSize,orderType:orderType};
	$.post('searchResourceByPageNewAction',params,function(data){
		if(data!=null){
			showResource(data);
			if(pageSize=="0"){
				$("#totalPage").html("1");
			}
			$(".loading_cover").hide();
		}
	},'json');	

}


//显示查询数据
function showResource(data){
	var totalSize = data.totalSize;
	if(totalSize==0){
		$("#showContent").html("<tr><td>没有查询到目标资源</td></tr>");
		 $("#totalSizeDiv").hide();
		 $(".paging_div").hide();
	}else{
		var totalPage = data.totalPage;
		var titleMap = data.titleMap;
		var contentMapList  = data.contentMapList;
		var attrTypeMap = data.attrTypeMap;
		var conditionString = data.conditionString;
		var pageIndex =parseInt(data.pageIndex);
		var assNameList = data.assNameList;
		var content="";
		$("#totalSizeDiv").html("共"+totalSize+"条记录");
		$("#totalPage").html(totalPage);
		$("#currentPage").val(pageIndex);
		$("#saveConditionString").val(conditionString);
		$("#savePageIndex").val(pageIndex);
		var areaId = $("#saveAreaId").val();
		$("#totalSizeDiv").hide();
		$("#paging_div").hide();
		var flagIndex = 0;
		$.each(titleMap,function(key,value){
			if(key=="name"||key=="label"||key=="belongType"||(attrTypeMap[key]!=undefined&&attrTypeMap[key].indexOf("Date")>=0)){
				flagIndex++;
				content += "<th>"+value+"</th>";
				if(flagIndex==2){
					content += "<th>所属资源名称</th>";
				}
			}
		
		})
		
		content ="<tr><th><input type='checkbox' onclick='operaAllCbx(this)'/>全选</th>"+content+"<th>操作</th></tr>";
		$.each(contentMapList,function(index,con){
			var cStr = "";
			var assName = assNameList[index];
			
			
			$.each(con,function(k,value){
				if(k=="name"||k=="label"||k=="belongType"||(attrTypeMap[k]!=undefined &&attrTypeMap[k].indexOf("Date")>=0)){
					if(k=="name"){
						cStr += "<td><a target='_blank' href='getPhysicalresForOperaAction?currentEntityType="+con['_entityType']+"&currentEntityId="+con['id']+"&areaId="+areaId+"&modelType=view'>"+value+"</a></td>";
					}else if(k=="label"){
						cStr += "<td>"+value+"</td><td>"+assName+"</td>";
					}else{
						cStr += "<td>"+value+"</td>";
					}
				}
			})
			content = content+ "<tr name='"+con['id']+"#"+con['_entityType']+"'><td><input name='cbxResEntity' type='checkbox' /></td>"+cStr+"<td> <a target='_blank' href='getPhysicalresForOperaAction?currentEntityType="+con['_entityType']+"&currentEntityId="+con['id']+"&areaId="+areaId+"&modelType=view' class='modify_a' >修改</a> <a onclick=\"mapLocation('"+con['id']+"','"+con['_entityType']+"')\" class='gis_a'>地图定位</a></td></tr>";
		})
		 $("#showContent").html(content);
		 $("#totalSizeDiv").show();
		 $(".paging_div").show();
	}				
	}
	
//加载数据
function searchResource(str){
    var areaId = $("#saveAreaId").val();
    var areaType = $("#saveAreaType").val();
    var sType = $("#saveSType").val();
    var conditionString = $("#saveConditionString").val();
    var pageIndex = $("#savePageIndex").val();
    var totalPage = $("#totalPage").html();
    var pageSize = $("#savePageSize").val();
    var orderType = $("#saveOrderType").val();
	if(str=="first"){
		if(parseInt(pageIndex)==1){
			return;
		}else{
			pageIndex = 1;
		}
		
	}else if(str=="up"){
		if(parseInt(pageIndex)-1<=0){
			return;
		}else{
			pageIndex = parseInt(pageIndex)-1;
		}
	}else if(str=="next"){
		if(parseInt(pageIndex)+1>parseInt(totalPage)){
			return;
		}else{
			pageIndex = parseInt(pageIndex)+1;
		}
	}else if(str=="last"){
		pageIndex = parseInt(totalPage);
	}else if(str=="condition"){
		if(pageIndex==$("#currentPage").val()){
			return;
		}else{
			if(isNaN(parseInt($("#currentPage").val()))){
				$("#currentPage").val("");
				return;
			}
			pageIndex = parseInt($("#currentPage").val());
			
		}
	}
	var params = {areaId:areaId,areaType:areaType,sType:sType,conditionString:conditionString,pageIndex:pageIndex,pageSize:pageSize,orderType:orderType};
	$.post('searchResourceByPageNewAction',params,function(data){
		if(data!=null){
			showResource(data);
		}
	},'json')
}


function selectType(me){
	$("#searchType").val($(me).val());
	$("#sTypeText").val($(me).next().text())
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
								var count = "<select onchange='selectChildArea(this);'  id='province_select0'>"
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


</script>
</head>
<body>
	<%-- cover层放在body标签下 --%>
	<div class="loading_cover" style="display:none">
    	<div class="cover"></div>
    	<h4 class="loading">正在加载<em class="loading_fb">站址</em>资源,请稍侯...</h4>
    </div>
	<%-- 弹出框 --%>
    <div id="dialog" style="display:none;">
    <form id="operaForm" action = "addResourceNewAction" method="post">
        <div class="black" style="display:block; z-index:11;"></div>
        <div id="addcontent">
        	<%-- <div class="dialog" style="width:700px; margin-left:-350px; margin-top:100px;">
	            <div class="dialog_header">
	                <div class="dialog_title">添加：站址</div>
	                <div class="dialog_tool">
	                   <div class="dialog_tool_close dialog_close"></div>
	                </div>
	            </div>
	            <div class="dialog_content" style="padding:4px;">
	            	
	                <div class="dialog_but">
	                    <button type="submit" class="aui_state_highlight">确定</button>
	                    <button type="button" class="aui_state_highlight dialog_close">取消</button>
	                </div>
	            </div>
        	</div> --%>
        </div>
    </form>
    </div>


	<%--主体开始--%>
	<div class="map_wrapper">
		<div class="map_hd">
		<input type="hidden" value="${forwardType}" id="forwardType">
		<form id="searchForm" method="post" action="searchResourceByPageNewAction" >
		<input type="hidden" value="25"  name="pageSize"/>
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
				                <div class="map_city">
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
								<div class="map_district">
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
								<div class="map_street">
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
                            <input type="text" value="" id="sTypeText" readonly/><a class="areaButton zy_show"></a>
                        </em>
                        <div class="search_box_alert">
                        	<div class="search_box_close"></div>
                        	<h4>点设施资源：</h4>
                            <p id="Search_Facilities">
                            	<input type="radio" />站址
                            	<input type="radio" />基站
                            	<input type="radio" />管井
                            	<input type="radio" />标杆
                            </p>
                            <h4>无线资源：</h4>
                            <p id="Search_Wireless">
                            	<input type="radio" />GSM设备
                            	<input type="radio" />TD设备
                            </p>
                            <h4>传输资源：</h4>
                            <p id="Search_Transmission">
                            	<input type="radio" />AC
                            	<input type="radio" />AP
                            </p>
                            <h4>动力资源：</h4>
                            <p id="Search_Power">
                            	<input type="radio" />DDF
                            	<input type="radio" />光端机
                            </p>
                            <h4>Wlan资源：</h4>
                            <p id="Search_Wlan">
                            	<input type="radio" />DDF
                            	<input type="radio" />光端机
                            </p>
                            <h4>环境监控资源：</h4>
                            <p id="Search_EvironmentAndMonitoring">
                            	<input type="radio" />DDF
                            	<input type="radio" />光端机
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
                     <%--  <li class="up_search" style="display:none;">
                        	
                        </li> --%>  
                    </ul>
				</div>
			</div>
			</form>
			<div class="present_box">
				<div class="r">
					<ul class="present clearfix">
						<li>
							<a  class="caozuo dialog_open">添加</a>
						</li>
						<li>
							<a  class="caozuo" id="btnDeleteRes" >删除</a>
						</li>
						<li>
							<a  class="caozuo" onclick="exportData();" >导出</a>
							
						</li>
					</ul>
				</div>
				<ul class="present_tab clearfix">
					<li>
						<a  class="list select"  style="cursor:auto"><em></em>列表呈现</a>
					</li>
					<li>
						<a class="gis" onclick="submitEventForGis();" style="border-left:none;" ><em></em>GIS呈现</a>
						<form id="forwardForm" method="post" action="forwardSearchResourceGisPageAction" target="_blank">
							<input type="hidden" id="saveAreaId" name="areaId" value="${areaId}"/>
			           		<input type="hidden" id="saveAreaType" name="areaType" value="${areaType}"/>
			           		<input type="hidden" id="saveSType" name="sType" value="${sType}"/>
			           		<input type="hidden" id="saveConditionString" name="conditionString" value="${conditionString}"/>
			           		<input type="hidden" id="savePageIndex" name="pageIndex" value="${pageIndex}"/>
			           		<input type="hidden" id="saveOrderType" name="orderType" value="${orderType}"/>
			           		<input type="hidden" id="saveCurrentEntityId" name="currentEntityId" value="${currentEntityId}"/>
			           		<input type="hidden" id="saveCurrentEntityType" name="currentEntityType" value="${currentEntityType}"/>
			           		<input type="hidden" id="savePageSize" name="pageSize" value="${pageSize}"/>
							<input type="hidden" id="saveCityAreaId" name="cityAreaId" value="${cityAreaId}"/>
							<input type="hidden" id="saveDistrictAreaId" name="districtAreaId" value="${districtAreaId}"/>
							<input type="hidden" id="saveStreetAreaId" name="streetAreaId" value="${streetAreaId}"/>
				   		</form>
				   		<input type="hidden" id="cityAreaId"  value=""/>
						<input type="hidden" id="districtAreaId"  value=""/>
						<input type="hidden" id="streetAreaId"  value=""/>
					</li>
				</ul> 
			</div>	
		</div>
		<div class="map_bd">
            <div class="source_table_div">
                <table class="source_table" id='showContent'>
                  <%--   <tr>
                        <th><input type="checkbox" /></th>
                        <th>资源名称</th>
                        <th>资源编码</th>
                        <th>所属资源名称</th>
                        <th>产权属性</th>
                        <th>入库时间</th>
                        <th>操作</th>
                    </tr>
                   
                    <tr>
                        <td><input type="checkbox" /></td>
                        <td><a href="#">香格里拉大酒店</a></td>
                        <td>111111111</td>
                        <td>海珠区</td>
                        <td>移动</td>
                        <td>2012-3-14 12:00</td>
                        <td>
                            <a href="#" class="modify_a">修改</a>
                            <a href="#" class="gis_a">地图定位</a>
                        </td>
                    </tr> --%>
                </table>
            <div class="ab_left" id='totalSizeDiv' style="display:none">共120条记录</div>
            </div>
            <%-- 默认每页10条或20条记录 --%>
            <div class="paging_div" style="display:none">
                <a class="paging_link page-first"  title="首页" onclick="searchResource('first')"></a> 
                <a class="paging_link page-prev"  title="上一页" onclick="searchResource('up')"></a> 
                <i class="paging_text">第</i>
                <input type="text" class="paging_input_text" value="1" id="currentPage" onKeyUp="this.value=this.value.replace(/[^0-9]D*$/,'')" onafterpaste="this.value=this.value.replace(/[^0-9]D*$/,'')"/>
                <i class="paging_text">页/共<i id='totalPage'></i>页</i> 
                <a class="paging_link page-go"  title="GO" onclick="searchResource('condition')">GO</a>
                <a class="paging_link page-next"  title="下一页" onclick="searchResource('next')"></a> 
                <a class="paging_link page-last"  title="末页" onclick="searchResource('last')"></a> 
           		
            </div>
		</div>
	</div>
</body>
</html>