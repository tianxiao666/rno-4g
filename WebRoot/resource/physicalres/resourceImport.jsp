<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>资源导入</title>
<link rel="stylesheet" href="css/base.css" type="text/css" />
<link rel="stylesheet" href="css/public.css" type="text/css" />
<link rel="stylesheet" href="css/ziyuan.css" type="text/css" />
<link rel="stylesheet" type="text/css" href="css/city.css" />
<style type="text/css">

.search_box_alert{display:none; position:absolute; width:383px; border:1px solid #99BBE8;background:#fff;right:18px; top:27px;}
.search_box_alert h4{background:#eee; border-bottom:1px solid #ccc; padding:4px 8px; line-height:16px;}
.search_box_alert p{padding:2px 8px; line-height:16px;}
.search_box_close{cursor: pointer;display: block;height: 16px;opacity: 0.8;width: 16px; position:absolute; right:4px; top:4px;background: url("images/panel_tools.gif") no-repeat scroll -16px 0 transparent;}
/*2013-2-26*/

.countMess{
	color:red;
}
#resourceInfo{
	 background: none repeat scroll 0 0 #F9F9F9;
    border-image: none;
    border-radius: 5px 5px 5px 5px;
    border:1px solid #99BCE8;
    display: block;
    left: 63%;
    margin-left: -280px;
    position: absolute;
    top: 260px;
    z-index: 10001;

}
#parResourceInfo{
	background: none repeat scroll 0 0 #F9F9F9;
    border-image: none;
    border-radius: 5px 5px 5px 5px;
    border:1px solid #99BCE8;
    display: block;
    left: 83%;
    margin-left: -280px;
    position: absolute;
    top: 188px;
    z-index: 10001;

}
.searchtab{ /*关联搜索切换*/
	background-color: #F1F1F1;
    margin-top: -2px;
    max-height: 327px;
    padding: 0 5px 5px;
    position: absolute;
    top: 25px;
    width: 143px;
    border:1px solid #999999;
    border-top: none;
    border-radius: 0 0 5px 5px;
}
.logicalressearchtab{ /*搜索切换*/
	background-color: #F1F1F1;
    margin-top: -2px;
    max-height: 327px;
    padding: 0 5px 5px;
    position: absolute;
    top: 57px;
    width: 144px;
    border:1px solid #999999;
    border-top: none;
    border-radius: 0 0 5px 5px;
}
	#confirm_div_container{background: none repeat scroll 0 0 #F9F9F9;
	    border: 1px solid #999999;
	    left: 50%;
	    margin-left: -150px;
	    padding: 5px;
	    position: absolute;
	    z-index: 6;}
	#confirm_div_title{height: 25px; width: 100%; border-bottom: 1px solid rgb(204, 204, 204); padding-bottom: 5px;}
	#logicalresContentDiv{height: 700px;
    overflow-y: scroll;
    width: 380px;}
	#right_container{ width:300px;border:1px solid #ccc; background-color: #fff;margin: 0 auto; position: relative;}
	.right_tab_list{ clear:both; height:30px;}
	.right_tab_list ul{ list-style:none;margin:0; padding:0; }
	.right_tab_list ul li{ float:left; padding:5px; cursor:pointer; border:1px solid #CCC;background:#E2EBF4;}
	.right_tab_list ul li.show_li{ background:#fff;}
	
	.right_tab_table{width:100%; margin-bottom:10px;}
	.right_tab_table th{ font-weight:normal; padding-left:5px; height:30px; background:url(../image/cmp-bg2.gif) repeat-x; border:1px solid #ccc;}
	.right_tab_table td{ border:1px solid #CCC; padding:5px 2px; color:#000; vertical-align:middle;  background:WhiteSmoke;}
	.right_tab_table .menuTd{white-space:nowrap; background:#E2EBF4; text-align:right; width:20%;}
	.right_tab_table .table_bottom{ background:#eee; text-align:right;}
	#down_down{clear:both;overflow:scroll;margin:0 auto;min-height:100px;max-height:480px;}
	#showContentTable tr {cursor:pointer;}
	#showContentTable th,td{white-space:nowrap;padding:1px 2px;height:21px;}
	.chosenClass{background:#FCC;}
	#up_top{cursor:pointer; }
	
/* progress_bar */
.progress_bar_top{color:#333; padding-left:6px; margin:4px 0px; clear:both;}
.progress_bar{ white-space:nowrap;clear:both;height:42px; margin:4px 0px 4px 5px; line-height:40px; background:url(image/epjoin_step_bk.png) 0px -1px no-repeat;}
.progress_bar_color1{ background:url(image/epjoin_step_bk.png) 0px -46px no-repeat;}
.progress_bar_color2{ background:url(image/epjoin_step_bk.png) 0px -91px no-repeat;}
.progress_bar_color3{ background:url(image/epjoin_step_bk.png) 0px -136px no-repeat;}
.progress_bar_child1{display:inline-block;padding-left:15px;}
.progress_bar_child2{display:inline-block;padding-left:60px;}
.progress_isnot{margin-right:5px; color:blue;}
em.blue{color:blue; font-style:normal;}
em.red{color:red; font-style:normal;}

/*自定义导出*/
#down_button{border-left:none; width:15px; margin-left: -4px;}
	#down_em{background: none repeat scroll 0 0 #FFFFFF;
    border-color: -moz-use-text-color #CCCCCC #CCCCCC;
    border-image: none;
    border-right: 1px solid #CCCCCC;
    border-style: none solid solid;
    border-width: medium 1px 1px;
    cursor: pointer;
    display: none;
    font-style: normal;
    height: 25px;
    line-height: 25px;
    margin-left: -71px;
    margin-top: 23px;
    position: absolute;
    text-align: center;
    width: 69px;
    z-index: 5;}
	#down_em:hover{background:#eee;}
</style>
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.form.js"></script>
<script type="text/javascript" src="js/resourceImport.js"></script>
<script type="text/javascript" src="js/searchResource.js"></script>
<script type="text/javascript" src="../js/input.js"></script>
<script type="text/javascript" src="../js/objutil.js"></script>
<script type="text/javascript" src="js/resourceArea.js"></script>
<script type="text/javascript">
//记录数组下标，用来控制，下次循环，从何下标开始
var arrIndex = 0;
//大集合map，用来操作隶属资源和导入资源的关系维护
var bigMap = null;
var importModel=null;
var entityMap="";
var searchMethod="all";
var attrName="";
var parType="";
var showInterval=null;
var areaId="";
var logicalressearchMethod="all";
var escEnable=true;//esc按键禁用判断
var firstLoad=true;//第一次加载选择类型
var importMaps = "";
var importRecord = "";
var assRecord = "";
var rowNums ="";
var importEntityType = "";
var importAmmount=0;

     



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
					content +="<span ><input type='radio' name='searchType' value='"+k1+"' onclick ='chooseResourceType(this);'/><i>"+v1+"</i></span>";
				})
				$("#"+k).html(content);
			})
			$(".search_box_alert").slideToggle("fast");
		},'json')
		
	})
	$(".search_box_close").click(function(){
		$(".search_box_alert").slideToggle("fast");
	})
		
		/*start 自定义导出*/
			$("#down_button").click(function(){ //自定义导出
				$("#down_em").toggle("fast");
			})
			$(".self_export").click(function(){ //自定义导出
				$("#down_em").toggle("fast");
				if($("#showContentTable tr").not("#headTr").not("#addedTr").size() == 0) {
					alert("无资源数据可导出!");
					return false;
				}
				var chosenType = $("tr[id='headTr']").children().eq(1).attr("id")+"";
				chosenType = chosenType.substring(0,chosenType.indexOf("#"));
				$.post("getAttrByChosenTypeAction",{chosenType:chosenType,showType:"selfExport"},function(data){
					var indexFlag=0;
					var content="<tr>";
					$.each(data, function(index, obj){
						var type = obj.substring(0, obj.lastIndexOf("_")); //截取类型
						var chineseType = obj.substring(obj.lastIndexOf("_") + 1); //截取中文类型
						if(indexFlag!=4){
							content +="<td><input type='checkbox' value='"+type+"' name='self_export_attributes'><em>"+chineseType+"</em></td>";
							if(index==data.length-1){
								content+="</tr>";
							}
							indexFlag++;
						}else{
							content+="</tr><tr>";
							content +="<td><input type='checkbox' value='"+type+"' name='self_export_attributes'><em>"+chineseType+"</em></td>";
							if(index==data.length-1){
								content+="</tr>";
							}
							indexFlag=1;
						}
					});
					$("#exportAttrTable").html(content);
					$("#dialog_export").show();
					$("#black").show();
				},'json');
				
			})
			
			$(".dialog-hide_export").click(function(){
			    $("#dialog_export").hide();
				$("#black").hide();
		    })
		    
		    $("#self_export_cancel").click(function(){
		    	$("input[name='self_export_attributes']:checked").removeAttr("checked");
		    })
		    
		    $("#self_export_submit").click(function(){
		   	    if($("input[name='self_export_attributes']:checked").size()==0){
		    		alert("请选择要导出的资源内容字段!")
		    		return false;
		    	}
		    	$("#dialog_export").hide();
				$("#black").hide();
		    	var attrMap="";
		    	$("input[name='self_export_attributes']:checked").each(function(){
		    		attrMap +="\'"+$(this).val()+"\':\'"+$(this).next().text()+"\',"
		    	})
		    	attrMap=attrMap.substring(0,attrMap.lastIndexOf(","))
		    	attrMap="{"+attrMap+"}";
		    	$("input[name='attrMap']").val(attrMap);
		    	$("input[name='exportModel']").val("selfExport");
		    
				//先把需要删除的个数保存起来
				var deleteCount = $("input[name='cbxResEntity']").length;
				var chooseResEntityId = "";
				var chooseResEntityType = "";
				//获取要被删除的查询资源的id和type
				$("input[name='cbxResEntity']").each(function(index){
					var cbxRes = $(this);
					
					/*cbxRes.parent().parent().children().each(function(){
						if(($(this).val()).indexOf("id_") > -1) {
							if(chooseResEntityId==""){
								chooseResEntityId += ($(this).val()).substring(($(this).val()).indexOf("_") + 1);
							}else{
								chooseResEntityId += ","+($(this).val()).substring(($(this).val()).indexOf("_") + 1);
								
							}
						} else if(($(this).val()).indexOf("aeType_") > -1) {
							chooseResEntityType = ($(this).val()).substring(($(this).val()).indexOf("_") + 1);
						}
				
					});*/
					var name = cbxRes.parent().parent().attr("name");
					if(name!=""){
						if(chooseResEntityId==""){
							chooseResEntityId = name.substring(name.indexOf("#")+1);
						}else{
							chooseResEntityId +=","+name.substring(name.indexOf("#")+1);
						}
						chooseResEntityType = name.substring(0,name.indexOf("#"));
					}
					
				});
				
				$("input[type='hidden'][name='chooseResEntityId']").val(chooseResEntityId);
				$("input[type='hidden'][name='chooseResEntityType']").val(chooseResEntityType);
				
				//导出数据的action
				document.getElementById("exportForm").submit();
				$("input[name='exportModel']").val("");
		    })
		    /*end 自定义导出*/
$("select[id='selAttrExactMatch']").change(function(){
	showProgress();
})
$("select[id='selAttrIndistinctMatch']").change(function(){
	showProgress();
})


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

$(".searchtab").hover(
			  function () {
			    $(this).show();
			  },
			  function () {
			    $(this).hide();
			  }
			); 
			$("#allCheckBox").click(function(){//关联搜索切换
				if($(this).attr("checked")=='checked'){
					$("#partCheckBox").removeAttr('checked');
					searchMethod='all';
					if($.trim($("#txtSearch").val())=="资源树搜索"||$.trim($("#txtSearch").val())==""){
						$("#txtSearch").val("资源分类搜索").css('color','#999');
					}
				}else{
					$("#partCheckBox").attr('checked','checked');
					searchMethod='part';
					if($.trim($("#txtSearch").val())=="资源分类搜索"||$.trim($("#txtSearch").val())==""){
						$("#txtSearch").val("资源树搜索").css('color','#999');
					}
				}
			})
			$("#partCheckBox").click(function(){//关联搜索切换
				if($(this).attr("checked")=='checked'){
					$("#allCheckBox").removeAttr('checked');
					searchMethod='part';
					var chosenEntityType = $("#chosenEntityType").val();
					var chosenEntityId = $("#chosenEntityId").val();
					var chooseType = $("#addedResEntityType").val();
					if($("#treeRootName").parent().children().eq(0).attr("src")=='image/plus.gif'){
						chooseEntity("#treeRootName");
					}else{
						if(chosenEntityId==""&&chosenEntityType==""&&chooseType==""){
							chooseEntity("#treeRootName");
						}
					}
					if($.trim($("#txtSearch").val())=="资源分类搜索"||$.trim($("#txtSearch").val())==""){
						$("#txtSearch").val("资源树搜索").css('color','#999');
					}
				}else{
					$("#allCheckBox").attr('checked','checked');
					searchMethod='all';
					if($.trim($("#txtSearch").val())=="资源树搜索"||$.trim($("#txtSearch").val())==""){
						$("#txtSearch").val("资源分类搜索").css('color','#999');
					}
				}
			})
			
			$("#txtSearch").blur(function(){
				if($("#partCheckBox").attr("checked")=='checked'){
					if($.trim($("#txtSearch").val())==""){
						$("#txtSearch").val("资源树搜索").css('color','#999');
					}
				}else{
					if($.trim($("#txtSearch").val())==""){
						$("#txtSearch").val("资源分类搜索").css('color','#999');
					}
				}
			})
			
$(document).keydown(function(event){ //按ESC键退出弹出窗口
			if(event.keyCode==27){
				if(escEnable){
					$(".aetg1").each(function(){
						$(this).hide();
					});
					$("#assResDiv").slideUp('fast');
					$("#chosenEntityType").val("");
					$("#chosenEntityId").val("");
					$("#addedResParentEntityType").val("");
					$("#addedResParentEntityId").val("");
					$("#addedResEntityType").val("");
					$("#resourceInfo").hide();
					$("#parResourceInfo").hide();
					$("#errorMessage").hide();
					$("#errorLink").children().eq(0).html("[+]详细");
					$(".wh_img").hide();
					$("#attributesUl").parent().hide();
					$("#exactMatchAttributesUl").parent().hide();
					$("#indistinctMatchAttributesUl").parent().hide();
				}else{
					return false;
				}	
			}
		}); 
$(".dialog_hide").click(function(){//浮窗隐藏
		$("#dialog").hide();
		$(".dialog_black").hide();
	});
//表单AJAX提交
		$("#sheet_form").ajaxForm({
			success:function(data){
			},
			dataType:'json'
		});
			
//表单AJAX提交
		$("#operaForm").ajaxForm({
			success:function(data){
				alert(data.message);
				showLogicalres(data.id,data.infoName,data.chooseType);
			},
			dataType:'json'
		});

$("#directFill").click(function(){//直接录入
	if($(this).attr("checked")=="checked"){
		$("#hTitle").html("已录入的资源");
		var content="";
		$("tr[id='headTr']").children().each(function(key){
			if($(this).attr("id")!=undefined &&$(this).attr("id")!="" ){
				var attrType = $(this).attr('name');
				var str="";
				var dateString="";
				if(attrType.indexOf("Integer")>=0 || attrType.indexOf("Long")>=0 ){
					var c=$(this).html()+"";
					if(c.indexOf("是否")>=0){
						str='请填写：0或者1，0表示“否”，1表示“是”';
					}else{
						str='请填写：整数，如“12”';
					}
					
				}else if(attrType.indexOf("Float")>=0 || attrType.indexOf("Double")>=0){
					str='请填写：数值，如“1232.23”，“12.03”';
				}else if(attrType.indexOf("Date")>=0){
					str = '请填写：日期时间，如“2012-10-09”，“2012-10-09 11:02:30”';
					dateString = "dateString" ;
				}else{
					str='请填写：字符串';
				}
				if($(this).hasClass("mustFill")){
					if(dateString!=""){
						content +="<td id='"+$(this).attr('id')+"' class='mustFill' name='"+$(this).attr('name')+"' ><input type='text'  name='"+$(this).attr('id')+"' title='"+str+"'  /></td>";			
					}else{
						content +="<td id='"+$(this).attr('id')+"' class='mustFill' name='"+$(this).attr('name')+"' ><input type='text' name='"+$(this).attr('id')+"' title='"+str+"'/></td>";							
					}
				}else{
					if(dateString!=""){
						content +="<td id='"+$(this).attr('id')+"'  name='"+$(this).attr('name')+"' ><input type='text'  name='"+$(this).attr('id')+"' title='"+str+"' /></td>";			
					}else{
						content +="<td id='"+$(this).attr('id')+"'  name='"+$(this).attr('name')+"' ><input type='text' name='"+$(this).attr('id')+"' title='"+str+"'/></td>";							
					}	
				}
			}
		})
		content = "<tr  id='addedTr' onclick='chooseTr(this);' ondblclick='fowardToOperPage(this);'><td style='color:red'>*<br><input type='button' value='保存' style='color:red' onclick='addPhysicalresDirectly(this);'/><input type='button' value='取消' style='color:red' onclick='cancelAddPhysicalresDirectly(this);'/></td>"+content+"</tr>";
		
		$(content).insertAfter($("tr[id='headTr']"));
	}else{
		
		var size = $("tr[id='addedTr']").size();
		if(size>0){
			if(confirm("不录入资源？")){
				$("tr[id='addedTr']").remove();
				$("#hTitle").html("已导入的资源");
			}else{
				$(this).attr("checked","checked")
			}
		}
	}

})
		
$("#highModel").click(function(){//高级模式
if($(this).attr("checked")=="checked"){
	$("tr[id='threeRow']").show();
	$("tr[id='threeRow']").children().eq(1).hide();
	$("#updateSpan").show();
	$("#deleteSpan").show();
	//资源维护
	$("#down_right2").show();
	$("#down_right_main2").hide();
	
}else{
	$("tr[id='threeRow']").hide();
	$("#updateSelect").css("display","none");
	$("#deleteSelect").css("display","none");
	$("#updateSpan").hide();
	$("#deleteSpan").hide();
	$("input[name='importModel']").eq(0).attr("checked",true);
	$("input[name='rdoAssModel']").eq(0).attr("checked",true);
	//资源维护
	$("#down_right2").hide();
	
	$("input[id='chooseCheckBox2']").attr("checked",false);	
}

})

$(".filter_specialty_info,.filter_letter_info,.filter_dictionary_info").mouseenter(function(){
		clearTimeout(stime);
		$(this).show();
	})
	$(".filter_specialty_info,.filter_letter_info,.filter_dictionary_info").mouseleave(function(){
		if($(this).hasClass("filter_specialty_info")){
			curClass = "filter_specialty_info";
		}else if($(this).hasClass("filter_letter_info")){
			curClass = "filter_letter_info";
		}else if($(this).hasClass("filter_dictionary_info")){
			curClass = "filter_dictionary_info";
		}
		stime = setTimeout(function(){
				$("."+curClass).slideUp("fast");
			}
			,2001);
	})
	$(".filter_specialty,.filter_letter,.filter_dictionary").mouseleave(function(e){
		clearTimeout(stime);
		if($(this).hasClass("filter_specialty")){
			curClass = "filter_specialty_info";
		}else if($(this).hasClass("filter_letter")){
			curClass = "filter_letter_info";
		}else if($(this).hasClass("filter_dictionary")){
			curClass = "filter_dictionary_info";
		}
		stime = setTimeout(function(){
				$("."+curClass).slideUp("fast");
			}
			,2001);
	})
	$(".filter_specialty,.filter_letter,.filter_dictionary").click(function(e){
		if($(this).hasClass("filter_specialty")){
			curClass = "filter_specialty_info";
			$(".filter_letter_info,.filter_dictionary_info").slideUp("fast");
		}else if($(this).hasClass("filter_letter")){
			curClass = "filter_letter_info";
			$(".filter_specialty_info,.filter_dictionary_info").slideUp("fast");
		}else if($(this).hasClass("filter_dictionary")){
			curClass = "filter_dictionary_info";
			$(".filter_specialty_info,.filter_letter_info").slideUp("fast");
		}
		if($("."+curClass).css("display")!="none"){
			$("."+curClass).slideUp("fast");
		}else{
			$("."+curClass).slideDown("fast");
		}
	})
	//资源类型弹出
	$(".filter_specialty").mouseover(function(){
		clearTimeout(stime);
		if(firstLoad){
			$(".filter_specialty_info").slideDown("fast");
			$(".filter_letter_info").hide();
			$(".filter_dictionary_info").hide();
			firstLoad = false;
			if($(".filter_specialty_info").css("display")!="none"){
				var ulSize = $("ul[id='ResGroup_4_Geographical']").children().size();
				if(ulSize==0){
					var params ={aetgName:"ResGroup_4_Geographical"}
					var content="";
					$.post('getEntityChineseByAetgAction',params,function(data){
						$.each(data,function(index,value){
							content +="<li><a id='"+value.type+"' onclick='chooseResourceType(this)' >"+value.chineseType+"</a></li>";
						});
						$("ul[id='ResGroup_4_Geographical']").append(content);
					},'json');
					
				}
			}
		}else{
			stime = setTimeout(function(){
				$(".filter_specialty_info").slideDown("fast");
				$(".filter_letter_info").hide();
				$(".filter_dictionary_info").hide();
				if($(".filter_specialty_info").css("display")!="none"){
					var ulSize = $("ul[id='ResGroup_4_Geographical']").children().size();
					if(ulSize==0){
						var params ={aetgName:"ResGroup_4_Geographical"}
						var content="";
						$.post('getEntityChineseByAetgAction',params,function(data){
							$.each(data,function(index,value){
								content +="<li><a id='"+value.type+"' onclick='chooseResourceType(this)' >"+value.chineseType+"</a></li>";
							});
							$("ul[id='ResGroup_4_Geographical']").append(content);
						},'json');
						
					}
				}
			}
			,1000);		
		}
		
	});

	$(".filter_letter").mouseover(function(){
		clearTimeout(stime);
		if(firstLoad){
			$(".filter_letter_info").slideDown("fast");
			$(".filter_specialty_info").hide();
			$(".filter_dictionary_info").hide();
			firstLoad = false;
			if($(".filter_letter_info").css("display")!="none"){
				var ulSize = $("ul[id='A']").children().size();
				if(ulSize==0){
					var params ={firstLetter:"A"}
					var content="";
					$.post('getEntityChineseByFirstLetterAction',params,function(data){
						$.each(data,function(index,value){
							content +="<li><a id='"+value.type+"' onclick='chooseResourceType(this)'>"+value.chineseType+"</a></li>";
						});
						$("ul[id='A']").append(content);
					},'json');
					
				}
			}
		}else{
			stime = setTimeout(function(){
				$(".filter_letter_info").slideDown("fast");
				$(".filter_specialty_info").hide();
				$(".filter_dictionary_info").hide();
				if($(".filter_letter_info").css("display")!="none"){
					var ulSize = $("ul[id='A']").children().size();
					if(ulSize==0){
						var params ={firstLetter:"A"}
						var content="";
						$.post('getEntityChineseByFirstLetterAction',params,function(data){
							$.each(data,function(index,value){
								content +="<li><a id='"+value.type+"' onclick='chooseResourceType(this)'>"+value.chineseType+"</a></li>";
							});
							$("ul[id='A']").append(content);
						},'json');
						
					}
				}
			}
			,1000);	
		}
		
	});

	$(".filter_dictionary").mouseover(function(){
		clearTimeout(stime);
		if(firstLoad){
			$(".filter_dictionary_info").slideDown("fast");
			$(".filter_specialty_info").hide();
			$(".filter_letter_info").hide();
			firstLoad = false;
			if($(".filter_dictionary_info").css("display")!="none"){
				var ulSize = $("ul[id='dictionary']").children().size();
				if(ulSize==0){
					var params="";
					var content="";
					$.post('getAllEntityChineseAction',params,function(data){
						$.each(data,function(index,value){
							content +="<li><a id='"+value.type+"' onclick='chooseResourceType(this)'>"+value.chineseType+"</a></li>";
						});
						$("ul[id='dictionary']").append(content);
					},'json');
					
				}
			}
		}else{
			stime = setTimeout(function(){
				$(".filter_dictionary_info").slideDown("fast");
				$(".filter_specialty_info").hide();
				$(".filter_letter_info").hide();
				if($(".filter_dictionary_info").css("display")!="none"){
					var ulSize = $("ul[id='dictionary']").children().size();
					if(ulSize==0){
						var params="";
						var content="";
						$.post('getAllEntityChineseAction',params,function(data){
							$.each(data,function(index,value){
								content +="<li><a id='"+value.type+"' onclick='chooseResourceType(this)'>"+value.chineseType+"</a></li>";
							});
							$("ul[id='dictionary']").append(content);
						},'json');
						
					}
				}
			}
			,1000);		
		}
		
		
	});
		
	//资源类型左右切换
	$(".filter_dialog_info").click(function(){
		if($(this).hasClass("specialty")){
			var ulSize = $(this).children().eq(1).children().size();
			if(ulSize==0){
				var aetgName = $(this).attr("id");
				var params ={aetgName:aetgName};
				var content="";
				$.post('getEntityChineseByAetgAction',params,function(data){
					$.each(data,function(index,value){
						content +="<li><a id='"+value.type+"' onclick='chooseResourceType(this)'>"+value.chineseType+"</a></li>";
					});
					$("ul[id='"+aetgName+"']").append(content);
				},'json');	
			}
		}else if($(this).hasClass("letter")){
			var ulSize = $(this).children().eq(1).children().size();
			if(ulSize==0){
				var firstLetter = $(this).attr("id");
				var params ={firstLetter:firstLetter};
				var url='getEntityChineseByFirstLetterAction';
				if(firstLetter=="ALL"){
					url='getAllEntityChineseAction';
				}
				var content="";
				$.post(url,params,function(data){
					$.each(data,function(index,value){
						content +="<li><a id='"+value.type+"' onclick='chooseResourceType(this)'>"+value.chineseType+"</a></li>";
					});
					$("ul[id='"+firstLetter+"']").append(content);
				},'json');
			}
			
		}	
		$(this).addClass("filter_active")			//为当前元素增加highlight类
			.children(".filter_main").show().end()			//将子节点的a元素显示出来并重新定位到上次操作的元素
		.siblings().removeClass("filter_active")		//获取元素的兄弟元素，并去掉他们的highlight类
			.children(".filter_main").hide();				//将兄弟元素下的a元素隐藏
	});
	
	
	$("#up_top").click(function(){//导入设置隐藏与显示
				if($("#up_down").css("display") == "block"){
					$("#importset").html("导入设置▲");
				}else{
					$("#importset").html("导入设置▼");
				}
				$("#up_down").slideToggle("fast");
			})
	$("#chooseCheckBox").click(function(){//导入资源关联设置隐藏与显示
		if($(this).attr("checked")=="checked"){
			$("#linkText").text("关联");
			$("input[name='rdoAssModel']").removeAttr("disabled");
			$("input[name='rdoAssModel']").eq(0).attr("checked",true);
			$("#selAttrExactMatch").val("请选择");
			$("#selAttrIndistinctMatch").val("请选择");
			$("input[name='rdoAssModel']").eq(0).parent().parent().children().eq(1).show();
			$("td[id='hideTd']").eq(1).hide();
			$("td[id='hideTd']").eq(2).hide();
			$("#btnShowAssResDiv").removeAttr("disabled");
			$(this).parent().parent().parent().css("height","175px");
		}else{
			$("#linkText").text("不关联");
			$("input[name='rdoAssModel']").attr("disabled",true);
			$("input[name='rdoAssModel']").removeAttr("checked");
			$(this).parent().parent().parent().css("height","0px");
		}
		$("#down_right_main").slideToggle("fast");
		showProgress();
	})
	$("#chooseCheckBox2").click(function(){//资源维护原因
		$("#down_right_main2").slideToggle("fast");	
		if($(this).attr("checked")=="checked"){
			$("#down_right_main2").show();
			$(this).parent().parent().parent().css("height","176px");
		}else{
			$("#down_right_main2").hide();
			$(this).parent().parent().parent().css("height","0px");
		}
		
	})
	$("input[name='importModel']").click(function(){
		if($(this).val()=="newAdd"){
			$("#chooseCheckBox").attr("checked","checked");
			$("select[id='linkAttributes']").attr("disabled",true);
			$("#linkText").text("关联");
			$("input[name='rdoAssModel']").removeAttr("disabled");
			$("input[name='rdoAssModel']").eq(0).attr("checked",true);
			$("#down_right_main").show();
			$("#updateSelect").css("display","none");
			$("#deleteSelect").css("display","none");
			$("#attributesUl").parent().hide();
		}else{
			if($(this).val()=="updateAdd"){
				$("#updateSelect").css("display","inline-block");
				if($("#updateSelect").html()==""){
					$("#updateSelect").html($("#deleteSelect").html());
				}
				$("#deleteSelect").css("display","none");
				$("#deleteSelect").html("");
				
				
			}else{
				$("#deleteSelect").css("display","inline-block");
				if($("#deleteSelect").html()==""){
					$("#deleteSelect").html($("#updateSelect").html());
				}
				$("#updateSelect").css("display","none");
				$("#updateSelect").html("");
					
			}
			$("#chooseCheckBox").removeAttr("checked");
			$("select[id='linkAttributes']").removeAttr("disabled");
			//.css("left",$("#linkAttributes").css("left"));
			$("#linkText").text("不关联");
			$("input[name='rdoAssModel']").attr("disabled",true);
			$("input[name='rdoAssModel']").removeAttr("checked");
			$("#down_right_main").hide();
			var w = parseInt($("#linkAttributes").css("width").replace("px",""))+3;
			$("#attributesUl").parent().css("width",w);
			$("#attributesUl").parent().show();
		}
		showProgress();
	})
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
				$(".searchtab").hide();
				//获取需要进行搜索的关键字
				var kewWords = $("#txtSearch").val();
				var chosenEntityType = $("#chosenEntityType").val();
				var chosenEntityId = $("#chosenEntityId").val();
				var addedResEntityType = $("#addedResEntityType").val();
				//没有任何搜索内容，或者依然为灰色的请输入时，进行搜索内容输入提示
				if($.trim(kewWords) == "" || (($.trim(kewWords)=="资源分类搜索"||$.trim(kewWords)=="资源树搜索") && ($("#txtSearch").css("color") == "#999" || $("#txtSearch").css("color") == "rgb(153, 153, 153)"))) {
					alert("请输入搜索内容!");
					return false;
				}
				if(searchMethod=="part"){
					if(addedResEntityType!=""){
						var addedResParentEntityType = $("#addedResParentEntityType").val();
						var addedResParentEntityId = $("#addedResParentEntityId").val();
				
						//获取类型前的小加号图片
						var typeImg = $("#treeDiv span.spanChosenClass").parent().children().eq(0);
						if(typeImg.css("visibility") == "hidden") {
							//不存在小加号图片，即该类型下并无entity内容，则不在搜索处理
							return false;
						}
						//获取搜索的类型起始节点下的内容div
						var entityContentDiv = $("#treeDiv span.spanChosenClass").parent().children().eq(4);
						
						if(entityContentDiv.children().length == 0) {
							//不存在内容的情况，需要进行加载
							showChildEntityMsg(typeImg);
						}
						
						//令子级entity信息加载完成后，才进行关键字高亮设置
						var searchInterval = setInterval(function(){
							if(entityContentDiv.children().length > 0) {
								//展开子级entity信息后，找到与搜索匹配项的，进行关键字高亮设置
								$("#treeDiv span:[class!='spanChosenClass']").css("background", "");
								$("#treeDiv em").css("color","");
								
								var flag = false;
								var searchIndex=0;//标记找到的第一个值
								var topValue=0;//找到的第一个值元素的位置top值
								entityContentDiv.children().children().each(function(index){
									//找到entity的span，进行高亮设置
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
											
											//alert($("#treeRoot").scrollTop());
											if($("#tree").scrollTop()!=0){//滚动条回到顶部
												$("#tree").scrollTop(0);
											}
											topValue = $(this).position().top;
											chooseEntity(choose);
										}else{
											choose.css("background","#FFFF88");
										}
										
										
										
									}else if(choose.text().toString()!=$.trim(kewWords)&&choose.text().toString().indexOf($.trim(kewWords))>=0){
										searchIndex++;
										if(searchIndex==1){
											if($("#tree").scrollTop()!=0){
												$("#tree").scrollTop(0);
											}
											topValue = $(this).position().top;
											chooseEntity(choose);
										}
										
										choose.highlight($.trim(kewWords), {needUnhighlight: true});
										
									}
									
								});
								if(flag==false){
									alert("没有匹配的数据.\r\n请修改查询条件 或 修改查询的起点后重新查询.");
								}else{
									topValue =topValue-50;//hardcode 使找到的内容相对居中聚焦
									setTimeout(function(){$("#tree").scrollTop(topValue);},100);
		
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
						var eImg = $("#treeDiv span.spanChosenClass").parent().children().eq(0);
						if(eImg.css("visibility") == "hidden") {
							//不存在小加号图片，即该类型下并无entity内容，则不在搜索处理
							return false;
						}
						//获取搜索的实例起始节点下的内容div
						
						
						var typeContentDiv = "";
						//获取搜索的实例起始节点下的内容div
						if($("#treeDiv span.spanChosenClass").parent().children().size()==6){
							typeContentDiv = $("#treeDiv span.spanChosenClass").parent().children().eq(5);
						}else{
							typeContentDiv = $("#treeDiv span.spanChosenClass").parent().children().eq(4);
						}
						if(typeContentDiv.children().length == 0) {
							//不存在内容的情况，需要进行加载
							showChildTypeMsg(eImg);
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
								$("#treeDiv span:[class!='spanChosenClass']").css("background", "");
								$("#treeDiv em").css("color","");
								var liIndex=0;				
								lis.each(function(){
									liIndex++;
									var addedResEntityType = $(this).children().eq(3).val();
									var typeImg = $(this).children().eq(0);
									if(typeImg.css("visibility") != "hidden") {
									//获取搜索的实例起始节点下的内容div
									if($(this).children().size()==6){
										entityContentDiv = $(this).children().eq(5);
									}else{
										entityContentDiv = $(this).children().eq(4);
									}
									if(entityContentDiv.children().length == 0) {
										//不存在内容的情况，需要进行加载
										getChildEntityMsgForSearch(typeImg);
										
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
														if($(this).find("input:[type='radio']").size()>0){
															//alert($(this).children().find("input:[type='radio']").size());
															var choose =$(this).children().eq(2);
															
														}else{
															var choose = $(this).children().eq(1);
															
														}
														if(choose.text().toString().indexOf($.trim(kewWords))>=0){
															flag=true;
															
															searchFlag=true;
														}
														if(choose.text().toString()==$.trim(kewWords)){
															//alert("ok");
															searchIndex++;
															if(searchIndex==1){//标记第一个找到的值
																//alert($("#treeRoot").scrollTop());
																if($("#tree").scrollTop()!=0){//滚动条回到顶部
																	$("#tree").scrollTop(0);
																}
																topValue = $(this).position().top;
																chooseEntity(choose);
															}else{
																choose.css("background","#FFFF88");
															}
															
															//choose.css("background","#FFFF88");
														}else if(choose.text().toString()!=$.trim(kewWords)&&choose.text().toString().indexOf($.trim(kewWords))>=0){
															searchIndex++;
															if(searchIndex==1){
																if($("#tree").scrollTop()!=0){
																	$("#tree").scrollTop(0);
																}
																topValue = $(this).position().top;
																chooseEntity(choose);
															}
															
															choose.highlight($.trim(kewWords), {needUnhighlight: true});
														}
														
													});
													if(flag==false){
														//alert("没有匹配的数据.\r\n请修改查询条件 或 修改查询的起点后重新查询.");
														entityContentDiv.hide();
														typeImg.attr("src", "image/plus.gif");
														
													}else{
														topValue =topValue-50;//hardcode 使找到的内容相对居中聚焦
														setTimeout(function(){$("#tree").scrollTop(topValue);},100);
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
					var chosenType=$("#selResChosenType").val();
					var areaId = $("#areaId").val();
					searchResouceRecursion("selectResChosenType",chosenType,areaId,kewWords)
					
				}	
			});
	//隶属资源弹出层控制按钮
	$("#btnShowAssResDiv").click(function(){
		if($("#selResChosenType").val() == "请选择") {
			alert("请选择上级资源类型!");
			return false;
		}
		//按弹出层选择按钮，展现回归至要节点，防止entity误选择
		$("#tree div").each(function(){
			$(this).hide();
		});
		//根节点的图标变为加号
		$("#tree").children().children().eq(0).attr("src", "image/plus.gif");
		$("#assResDiv").slideDown('fast');
	});
	$("#txtExactContent").focus(function(){
		if($("#selResChosenType").val() == "请选择") {
			alert("请选择上级资源类型!");
			return false;
		}
		//按弹出层选择按钮，展现回归至要节点，防止entity误选择
		$("#treeDiv div").each(function(){
			$(this).hide();
		});
		//根节点的图标变为加号
		$("#tree").children().children().eq(0).attr("src", "image/plus.gif");
		$("#assResDiv").slideDown('fast');
	});
	//隶属资源弹出层选择按钮
	$("#btnDivChoose").click(function(){
		if($("input[name='rdoChoose']:checked").length == 0) {
			alert("请选择一个隶属资源!");
			return false;
		}
		
		//记录选择的隶属资源信息
		var currentEntityName="";
		var rdochoose=$("input[name='rdoChoose']:checked").next();
		if(rdochoose.find("em").size()==0){
			 currentEntityName = rdochoose.html();
		}else{
			var nameString = rdochoose.html().toString();
			 currentEntityName +=rdochoose.find("em").text();
			 currentEntityName +=nameString.substring(nameString.lastIndexOf(">")+1,nameString.length);
		}
		
		var currentEntityId = $("input[name='rdoChoose']:checked").next().next().val();
		var currentEntityType = $("input[name='rdoChoose']:checked").next().next().next().val();
		$("#txtExactContent").val(currentEntityName);
		$("#hidAssEntityId").val(currentEntityId);
		$("#hidAssEntityType").val(currentEntityType);
		$("#assResDiv").slideUp('fast');
		showProgress();
	});
	
	//隶属资源弹出层取消按钮
	$("#btnDivCancel").click(function(){
		$("#assResDiv").slideUp('fast');
		showProgress();
	});
	
	//上级资源指定单选，加载页面时的设置
	$("input[name='rdoAssModel']").each(function(index){
		//因为默认选择了第一个，所以后面两个对应的信息框，默认为禁用状态
		if(index > 0) {
			$(this).parent().next().children().attr("disabled", true);
		}
	});
	
	//选择上级资源指定模式
	$("input[name='rdoAssModel']").each(function(index){
		$(this).click(function(){
			showProgress();
			//释放当前选择模式的禁用状态
			$(this).parent().next().children().attr("disabled", false);
			$("td[id='hideTd']").hide();
			$(this).parent().parent().children().eq(1).show();
			$("input[name='rdoAssModel']").each(function(otherIndex){
				if(index != otherIndex) {
					//禁用没被选择资源的模式
					$(this).parent().next().children().attr("disabled", true);
					if(otherIndex == 0) {
						//上级资源指定第一项，清空第一项中的选择资源，同时隐藏资源选择层
						$("#txtExactContent").val("");
						$("#hidAssEntityId").val("");
						$("#hidAssEntityType").val("");
						$("#assResDiv").slideUp('fast');
					} else if(otherIndex > 0) {
						//上级资源指定第二项或者第三项，让下拉框选择为默认的"请选择"状态
						$(this).parent().next().children().children().eq(0).attr("selected", "selected");
						$("#assResDiv").slideUp('fast');
					}
				}
			});
			var w = parseInt($("#selAttrExactMatch").css("width").replace("px",""))+3;
			$("#exactMatchAttributesUl").parent().css("width",w);
			//w = parseInt($("#selAttrIndistinctMatch").css("width").replace("px",""))+3;
			//$("#indistinctMatchAttributesUl").parent().css("width",w);
			if($(this).val()=="exactMatch"){
				$("#exactMatchAttributesUl").parent().show();
			}else if($(this).val()=="indistinctMatch"){
				$("#indistinctMatchAttributesUl").parent().show();
			}
		});
	});
	
	
	//根据所选择的上级资源类型，获取该类型对应的属性，填充到精确和模糊字段查询下拉框
	$("#selResChosenType").change(function(){
		//把选择需要关联的隶属资源中文类型，先记录在导入资源选择层
		$("#selResChosenType").children().each(function(){
			if($(this).attr("selected") == "selected") {
				$("#txtAssResType").html($(this).html());
				return false;
			}
		});
		
		//选择了区域的情况
		if($("#selResChosenType").val().indexOf("Area_") > -1) {
			if($("#tree").children().children().length == 5) {
				$("#tree").children().children().eq(0).after("<input class='input_radio' name='rdoChoose' type='radio' />");
			}
		} else {
			if($("#tree").children().children().length == 6) {
				$("#tree").children().children().eq(1).remove();
			}
		}
		
		//精确隶属资源选择层隐藏，防止误操作
		$("#assResDiv").slideUp('fast');
		
		//上级资源发生变化，清空对应的entity选择
		$("#txtExactContent").val("");
		$("#hidAssEntityId").val("");
		$("#hidAssEntityType").val("");
	
		//获取所选择的上级资源类型(因为当时的值为组合值，例如Station_parent，所以要进行substring操作，获取entity类型)
		var chosenType = $("#selResChosenType").val();
		chosenType = chosenType.substring(0, chosenType.lastIndexOf("_"));
		$.post("getAttrByChosenTypeAction",{chosenType:chosenType,showType:"import"},function(data){
			$("#selAttrExactMatch").html("<option value='请选择'>请选择</option>");
			$("#selAttrIndistinctMatch").html("<option value='请选择'>请选择</option>");
			$("#exactMatchAttributesUl").html("<li onmouseover='liMouseOver(this)' onclick='liMouseClick(this)' id='请选择'>请选择</li>");
			$("#indistinctMatchAttributesUl").html("<li onmouseover='liMouseOver(this)' onclick='liMouseClick(this)' id='请选择'>请选择</li>");
			$.each(data, function(index, obj){
				var type = obj.substring(0, obj.indexOf("_")); //截取类型
				var chineseType = obj.substring(obj.indexOf("_") + 1); //截取中文类型
				//把获取的类型加载至精确和模糊查找下拉框
				$("#selAttrExactMatch").append("<option value='"+type+"'>"+chineseType+"</option>");
				$("#selAttrIndistinctMatch").append("<option value='"+type+"'>"+chineseType+"</option>");
				$("#exactMatchAttributesUl").append("<li onmouseover='liMouseOver(this)' onclick='liMouseClick(this)' id='"+type+"'>"+chineseType+"</li>");
				$("#indistinctMatchAttributesUl").append("<li onmouseover='liMouseOver(this)' onclick='liMouseClick(this)' id='"+type+"'>"+chineseType+"</li>");
			})
			var w = parseInt($("#selAttrExactMatch").css("width").replace("px",""))+3;
			$("#exactMatchAttributesUl").parent().css("width",w);
			//w = parseInt($("#selAttrIndistinctMatch").css("width").replace("px",""))+3;
			//$("#indistinctMatchAttributesUl").parent().css("width",w);
			if($("input[name='rdoAssModel']").val()=="exactMatch"){
				$("#exactMatchAttributesUl").parent().show();
			}else if($("input[name='rdoAssModel']").val()=="indistinctMatch"){
				$("#indistinctMatchAttributesUl").parent().show();
			}
		},'json');
		showProgress();
	});
	
	//表单提交验证
	$("#importForm").submit(function(){
		if($("#importForm").attr("action") == "resourceImportAction"){
		var rdoCheckedVal = $("input[name='rdoAssModel']:checked").val();
		areaId=$("#areaId").val();
		if(areaId=="" || areaId==undefined){
			alert("当前选择区域不在导入资源权限范围!");
			return false;
		}
		//选择了精确指定上级资源，判断是否已进行选择
		if(rdoCheckedVal == "exactContent") {
			if($("#txtExactContent").val() == "") {
				alert("请选择一个上级资源!");
				return false;
			}
		} else if (rdoCheckedVal == "exactMatch") {
			//选择了文件中精确匹配
			if($("#selAttrExactMatch").val() == "请选择") {
				alert("请选择要精确匹配的属性!");
				return false;
			}
		} else if (rdoCheckedVal == "indistinctMatch") {
			//选择了文件中精确匹配
			if($("#selAttrIndistinctMatch").val() == "请选择") {
				alert("请选择要模糊匹配的属性!");
				return false;
			}
		}
		importEntityType = $("#hidImportEntityType").val();
		importAmmount=0;
		importMaps = "";
		importRecord = "";
		assRecord = "";
		rowNums ="";
		importModel =$("input[name='importModel']:checked").val();
		
		if(importModel=="updateAdd" || importModel=="deleteAdd"){
			if($("#linkAttributes").val()==""){
				if(importModel=="updateAdd"){
					alert("请选择更新资源匹配的属性!");
				}else{
					alert("请选择删除重建匹配的属性!");
				}
				
				return false;
			}
		}
		
		//判断是否导入的是excel文件
		var fileName = $("#importFile").val();
		var fileLastName = fileName.substring(fileName.indexOf(".") + 1);
		//if(!(fileLastName == "xls" || fileLastName == "xlsx")) {
		if(!(fileLastName == "xls" || fileLastName == "xlsx")) {
			alert("请选择excel文件进行导入");
			return false;
		}
		if(!confirm("是否已选择了所需的导入规则及导入文件,进行资源导入?")){
			return false;
		}
		$("#failCount").html("0");
		$("#successCount").html("0");
		$("#errorMessage").hide();
		$("#errorLink").children().eq(0).html("[+]详细");
		$("#errorLink").hide();
		$("#importResultDiv").hide();
		$("#lastResultDiv").html("");
		//显示数据加载提示层
		$("#loading_div").show();
		$("#black").show();
		
		return true;
		}else if ($("#importForm").attr("action") == "getFileSheetAction"){
			return true;
		}
	});
	
	//表单AJAX提交
	$("#importForm").ajaxForm({
		success:function(data){

			if($("#importForm").attr("action") == "resourceImportAction"){
				importMaps = "";
				importRecord = "";
				assRecord = "";
				rowNums ="";
				if(data == null) {
					alert("excel的导入内容为空，请重新选择!");
					//隐藏数据加载提示层
					$("#loading_div").hide();
					$("#black").hide();
					return false;
				}else if(data == "noParentColumn"){
					alert("请正确设置上级资源关联，因为导入文件没有正确的关联定义。");
					//隐藏数据加载提示层
					$("#loading_div").hide();
					$("#black").hide();
					return false;
				}else{
					escEnable=false;
					areaId=$("#areaId").val();
					$("#errorLink").show();
					$("#errorMessage").html("");
					$("#directFill").removeAttr("checked");
					$("#hTitle").html("已导入的资源");
					$("#cbxCheckAll").removeAttr("checked");
					$("#showContentTable tr").not("#headTr").remove();
					importModel =$("input[name='importModel']:checked").val();
					attrName = $("#selAttrExactMatch option:checked").text();
					if(attrName==""||attrName=="请选择"){
						attrName = $("#selAttrIndistinctMatch option:checked").text();
					}
					if(attrName==""||attrName=="请选择"){
						attrName = "名称";
					}
					parType= $("#txtAssResType").html();
					bigMap = data;
					arrIndex = 0;
					if(importModel=="newAdd"){
						//检查
						searchAssEntity(arrIndex, bigMap);
					}else{
						if(importModel=="updateAdd"){
							$("#headTitle").html("资源更新确认");
							$("#titleInfo").html("根据导入规则，您正在更新的以下资源，存在多个可匹配的现有资源，请选择确认.");
						}else{
							$("#headTitle").html("资源删除确认");
							$("#titleInfo").html("根据导入规则，您正在删除导入的以下资源，存在多个可匹配的现有资源，请选择确认.");
						}
						operateExistEntity(arrIndex, bigMap);
					}	
				}	
			}else if ($("#importForm").attr("action") == "getFileSheetAction"){
			
			var count = 0;
			var options = '';
				
			$.each(data,function(key,value){
				options +=  "<option value='"+key+"'>"+value+"</option>";
				count = key;
			});
			$('#Sheet_Select').html(options);
			
			if(count != 0){
				$('#Sheet_Select').show();
			}
				$("#importForm").attr("action","resourceImportAction");
			}
			
		
		},
		dataType:'json'
	});
	
	
	
	//根据单选，选择一个隶属资源
	$("#btnAssAppChoose").click(function(){
		escEnable=false;
		$("#resourceInfo").hide();
		$("#parResourceInfo").hide();
		$("#operaAssDiv").fadeOut("normal");
		//获取选中的隶属资源的单选对象
		var rdoCheckedVal = $("input[name='matchResult']:checked");
		//获取需要操作的导入资源信息,需要绑定的关系类型
		var importEntityId = $("#hidImportEntityId").val();
		var importEntityType = $("#hidImportEntityType").val();
		var assType = $("#hidAssType").val();
		//需要操作的隶属资源信息
		var assEntityId = rdoCheckedVal.next().next().val();
		var assEntityType = rdoCheckedVal.next().next().next().val();
		assRecord +=",'"+assEntityId+"'";
		arrIndex = arrIndex + 1;
		//跳入一下个隶属资源的导入
		if(importModel=="newAdd"){
			searchAssEntity(arrIndex, bigMap);
		}else{
			operateExistEntity(arrIndex,bigMap);
		}
	});
	//有多个资源时选择 取消更新导入或删除导入
	$("#btnNoUpdateAppChoose").click(function(){
		escEnable=false;
		$("#resourceInfo").hide();
		$("#parResourceInfo").hide();
		//$("#updateEntityDiv").fadeOut("fast");
		$("#updateEntityDiv").hide();
		var error = $("#errorMessage").html();
		error = error+"第"+(arrIndex+2)+"行资源用户取消导入 \n";
		$("#errorMessage").html(error);
		assRecord +=",'0'";
		arrIndex = arrIndex + 1;
		operateExistEntity(arrIndex,bigMap);
	})
	//根据单选，选择一个资源 进行更新导入或删除导入
	$("#btnUpdateAppChoose").click(function(){
		escEnable=false;
		$("#resourceInfo").hide();
		$("#parResourceInfo").hide();
		$("#updateEntityDiv").fadeOut("normal");
		//获取选中的资源的单选对象
		var rdoCheckedVal = $("input[name='updateResult']:checked");
		//获取需要操作的导入资源信息,需要绑定的关系类型
		var importEntityType = $("#importEntityType").val();
		var importEntityId = rdoCheckedVal.next().next().val();
		var entityName=rdoCheckedVal.next().text();
		var hasChild = rdoCheckedVal.next().next().next().next().val();
		if(hasChild=="has"){
			if(confirm(entityName+"有子资源实例，是否进行删除替换导入？")){
				importMaps +=","+entityMap;
				importRecord +=",'"+importEntityId+"'";
				rowNums +=",'"+arrIndex+"'";
				importAmmount++;
			}else{
				var error = $("#errorMessage").html();
				error = error+"第"+(arrIndex+2)+"行资源用户取消导入,替换资源有子资源";
				$("#errorMessage").html(error);
				assRecord +=",'0'";
			}
		}else{
			importMaps +=","+entityMap;
			importRecord +=",'"+importEntityId+"'";
			rowNums +=",'"+arrIndex+"'";
			importAmmount++;
		}
		if(bigMap.parNameList!=undefined){
			    var selectResChosenType = $("select[name='selectResChosenType']").val();
			    var rdoAssModel = $("input[name='rdoAssModel']:checked").val();
			    var selectAttrExactMatch = $("select[name='selectAttrExactMatch']").val();
			    var selectAttrIndistinctMatch = $("select[name='selectAttrIndistinctMatch']").val();
			    if(bigMap.parNameList[arrIndex]=="" || bigMap.parNameList[arrIndex]==null || bigMap.parNameList[arrIndex]=="null"){		
					var error = $("#errorMessage").html();
					error = error+"第"+(arrIndex+2)+"行资源关联上级资源【"+bigMap.parMatchList[arrIndex]+"】当前区域范围不存在。 \n";
					$("#errorMessage").html(error);
					assRecord += ",'0'";	
					//直接跳进下一次循环
					arrIndex = arrIndex + 1;
					operateExistEntity(arrIndex, bigMap);
					
				}else if(bigMap.parNameList[arrIndex].length==1){
					
					assRecord += ",'"+bigMap.parNameList[arrIndex][0].id+"'";

					//由于配对只有一个父，直接跳进下一次循环
					arrIndex = arrIndex + 1;
					operateExistEntity(arrIndex, bigMap);
				}else if(bigMap.parNameList[arrIndex].length>1){
					var content = "";
					//查询出来的隶属资源有多个(弹出选择层，让用户进行隶属资源的选择)
					var matchTxt ="";
					$.each(bigMap.parNameList[arrIndex], function(i, obj){
						var entityName = null;
						entityName = obj.name;
						matchTxt=entityName;
						var rdoContent = "";
						//默认让第一单选选中
						if(i == 0) {
							rdoContent = "<input type='radio' name='matchResult' checked='checked' />";
						} else {
							rdoContent = "<input type='radio' name='matchResult' />";
						}
						content +="<li>" + rdoContent
						+ "<span style='color:blue' onmouseout='hideInfo();' onmouseover='showInfo(this,event);'>"+entityName+"</span><input type='hidden' value='"+obj.id+"' />" 
						+ "<input type='hidden' value='"+bigMap.assEntityType+"' />&nbsp;&nbsp;<span style='margin-left:110px'><a href='getPhysicalresForOperaAction?currentEntityId="+obj.id+"&currentEntityType="+bigMap.assEntityType+"&areaId="+areaId+"&modelType=view' style='text-decoration:underline' target='_blank'>详情浏览>></a>&nbsp;&nbsp;<a onclick=\"javascript:showParResource('"+obj.id+"','"+bigMap.assEntityType+"',event)\" style='text-decoration:underline'>资源全路径>></a></li></span>";
					
					});
					
					//生成隶属资源单选按钮，并弹出选择层
					$("#operaAssDiv ul").html(content);
					$("#operaAssDiv").fadeIn("normal");
					$("#black").show();
					
					//避免导入资源的名称获取过快，使用延迟操作
					setTimeout(function(){
						//获取导入资源的名称
						if(bigMap.contentMapList[arrIndex].name != null) {
							$("#txtImportResName").html(bigMap.contentMapList[arrIndex].name);
						} else {
							$("#txtImportResName").html(bigMap.contentMapList[arrIndex].label);
						}
						$("#txtMatch").html(bigMap.parMatchList[arrIndex]);
					},500);
					
				}
			}else if(bigMap.assEntityId!=undefined){
				if(importEntityId!=null && importEntityId!=""){
					if(bigMap.assEntityType==importEntityType&&importEntityId==bigMap.assEntityId){
						assRecord += ",'0'";
						arrIndex = arrIndex + 1;
					}else{
						assRecord += ",'"+bigMap.assEntityId+"'";
						arrIndex = arrIndex + 1;
					}
				}else{
					assRecord += ",'"+bigMap.assEntityId+"'";
					arrIndex = arrIndex + 1;
				}

				operateExistEntity(arrIndex, bigMap);
			
			}else{
				assRecord += ",'0'";	
				arrIndex = arrIndex + 1;
				operateExistEntity(arrIndex, bigMap);
			}		
	});
	
	
	//取消隶属资源选择
	$("#btnNoAssApp").click(function(){
		escEnable=false;
		$("#resourceInfo").hide();
		$("#parResourceInfo").hide();
		$("#operaAssDiv").fadeOut("normal");
		assRecord +=",'0'";
		var error = $("#errorMessage").html();
		error = error+"第"+(arrIndex+2)+"行资源取消与上级资源关联。 \n";
		$("#errorMessage").html(error);
		assRecord +=",'0'";
		arrIndex = arrIndex + 1;
		
		//直接跳入下一个隶属资源的导入
		if(importModel=="newAdd"){
			searchAssEntity(arrIndex, bigMap);
		}else{
			operateExistEntity(arrIndex,bigMap);
		}
	});
	
	
	//删除查询的资源
	$("#btnDeleteRes").click(function(){
		//先把需要删除的个数保存起来
		var deleteCount = $("input[name='cbxResEntity']:checked").length;
		
		if(deleteCount == 0) {
			alert("请选择要删除的资源!");
			return false;
		}
		
		if(!confirm("确定要删除这些资源吗?")) {
			return false;
		}
		$("#tishiText").html("数据删除中......");
		//加载层和遮盖层出现
		$("#loading_div").show();
		$("#black").show();

		//获取要被删除的查询资源的id和type
		$("input[name='cbxResEntity']:checked").each(function(index){
			var chooseResEntityId = "";
			var chooseResEntityType = "";
			var cbxRes = $(this);
		/*	cbxRes.parent().parent().children().each(function(){
				
				if(($(this).val()).indexOf("id_") > -1) {
					chooseResEntityId = ($(this).val()).substring(($(this).val()).indexOf("_") + 1);
				} else if(($(this).val()).indexOf("aeType_") > -1) {
					chooseResEntityType = ($(this).val()).substring(($(this).val()).indexOf("_") + 1);
				}
			});*/
			var name = cbxRes.parent().parent().attr("name");
			if(name!=""){
				chooseResEntityId = name.substring(name.indexOf("#")+1);
				chooseResEntityType = name.substring(0,name.indexOf("#"));
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
					$("#loading_div").hide();
					$("#black").hide();
					$("#tishiText").html("数据导入中......");
					alert("删除数据完毕!");
				}
			},'json');
		});
	});
	
	//编辑按钮，打开浏览页面
	$("#btnOperaPage").click(function(){
		if($(".chosenClass input").length == 0) {
			alert("请选择需要浏览的资源");
			return false;
		}
		var currentEntityId = "";
		var currentEntityType = "";
		$(".chosenClass input").each(function(){
			if(($(this).val()).indexOf("id_") > -1) {
				currentEntityId = ($(this).val()).substring(($(this).val()).indexOf("_") + 1);
			} else if(($(this).val()).indexOf("aeType_") > -1) {
				currentEntityType = ($(this).val()).substring(($(this).val()).indexOf("_") + 1);
			}
		});
		var linkType="";
		var parentType="";
		$("#selResChosenType option").each(function(){
			if($(this).attr("value")!="请选择"){
				type = $(this).attr("value")+"";
				type = type.substring(type.indexOf("_")+1,type.length);
				if(type=="link"){
					linkType="link";
				}else{
					parentType="parent";
				}			
			}
		})
		if(linkType=="link" && parentType==""){
			var params = {id:currentEntityId,infoName:currentEntityType,chooseType:"view"}
			//获取逻辑网属性维护页面信息
			$.post("../logicalres/logicalresViewRoAddAction",params,function(data){
				$("#logicalresContentDiv").html(data);
				$("#confirm_div").show();
				bodyOnLoad(currentEntityId,currentEntityType,"view");
			});
		}else{
			//打开节点编辑页面
			var areaId = $("#areaId").val();
			open("getPhysicalresForOperaAction?currentEntityId="+currentEntityId+"&currentEntityType="+currentEntityType+"&areaId="+areaId+"&modelType=view");	
		}
	});
	
	//资源维护关系
	$("#btnTurnToLogicalres").click(function(){
		if($(".chosenClass input").length == 0) {
			alert("请选择需要编辑的资源");
			return false;
		}
		var currentEntityId = "";
		var currentEntityType = "";
		$(".chosenClass input").each(function(){
			if(($(this).val()).indexOf("id_") > -1) {
				currentEntityId = ($(this).val()).substring(($(this).val()).indexOf("_") + 1);
			} else if(($(this).val()).indexOf("aeType_") > -1) {
				currentEntityType = ($(this).val()).substring(($(this).val()).indexOf("_") + 1);
			}
		});
		var linkType="";
		var parentType="";
		$("#selResChosenType option").each(function(){
			if($(this).attr("value")!="请选择"){
				type = $(this).attr("value")+"";
				type = type.substring(type.indexOf("_")+1,type.length);
				if(type=="link"){
					linkType="link";
				}else{
					parentType="parent";
				}			
			}
		})
		if(linkType=="link" && parentType==""){
			var params = {id:currentEntityId,infoName:currentEntityType,chooseType:"Preserve"}
			//获取逻辑网属性维护页面信息
			$.post("../logicalres/logicalresViewRoAddAction",params,function(data){
				$("#logicalresContentDiv").html(data);
				$("#confirm_div").show();
				bodyOnLoad(currentEntityId,currentEntityType,"Preserve");
			});
		}else{
			//打开节点编辑页面
			var areaId = $("#areaId").val();
			open("getPhysicalresForOperaAction?currentEntityId="+currentEntityId+"&currentEntityType="+currentEntityType+"&areaId="+areaId);	
		}
		
	});
	
	//导出资源信息(excel)
	$("#btnExportRes").click(function(){
		if($("#showContentTable tr").not("#headTr").not("#addedTr").size() == 0) {
			alert("无资源数据可导出!");
			return false;
		}
		
		
		//先把需要删除的个数保存起来
		var deleteCount = $("input[name='cbxResEntity']").length;
		var chooseResEntityId = "";
		var chooseResEntityType = "";
		//获取要被删除的查询资源的id和type
		$("input[name='cbxResEntity']").each(function(index){
			var cbxRes = $(this);
			
			/*cbxRes.parent().parent().children().each(function(){
				if(($(this).val()).indexOf("id_") > -1) {
					if(chooseResEntityId==""){
						chooseResEntityId += ($(this).val()).substring(($(this).val()).indexOf("_") + 1);
					}else{
						chooseResEntityId += ","+($(this).val()).substring(($(this).val()).indexOf("_") + 1);
						
					}
				} else if(($(this).val()).indexOf("aeType_") > -1) {
					chooseResEntityType = ($(this).val()).substring(($(this).val()).indexOf("_") + 1);
				}
		
			});*/
			var name = cbxRes.parent().parent().attr("name");
			if(name!=""){
				if(chooseResEntityId==""){
					chooseResEntityId = name.substring(name.indexOf("#")+1);
				}else{
					chooseResEntityId +=","+name.substring(name.indexOf("#")+1);
				}
				chooseResEntityType = name.substring(0,name.indexOf("#"));
			}
		});
		
		$("input[type='hidden'][name='chooseResEntityId']").val(chooseResEntityId);
		$("input[type='hidden'][name='chooseResEntityType']").val(chooseResEntityType);
		
		//导出数据的action
		document.getElementById("exportForm").submit();
		
	});
	//关闭逻辑网属性编辑层
	$("#btnCloseLogicalresDiv").click(function(){
		$("#confirm_div").hide();
	});
	
});
function hasSpecialChar(s) {      
    var newstr = "";   
    for (var i=0; i<s.length; i++) {   
        c = s.charAt(i);        
        switch (c) {        
            case '\"':        
                newstr+=",\"";        
                break;                
            case '/':        
                newstr+=",/";        
                break;        
            case '\b':        
                newstr+=",\b";        
                break;        
            case '\f':        
                newstr+=",\f";        
                break;        
            case '\n':        
                newstr+=",\n";        
                break;        
            case '\r':        
                newstr+=",\r";        
                break;        
            case '\t':        
                newstr+=",\t";        
                break; 
            case '\\':        
                newstr+=",\\";        
                break;              
        }   
   }   
   return newstr;        
}  

	//更新/删除导入时递归处理
	function operateExistEntity(index,data){
			if(index==0){
				if(data.resultMsg!="" && data.resultMsg!=null && data.resultMsg!="null" && data.resultMsg!=undefined){
					var error = data.resultMsg;
					$("#errorMessage").html(error);
					errorMsg = "导入的资源有数据不规范。 \n禁止导入，详情请查看【-】详细. \n";
					showError($("#errorLink").children().eq(0));
					alert(errorMsg);
					$("#loading_div").hide();
					$("#black").hide();
					return;
				}
			}
			if(index >= data.contentMapList.length) {
				if(importAmmount>1000&&importAmmount<=5000){
					$("#importData").data("importData",data);
					$("div[id='tooltip_dialog'] .dialog_but").html("<button type='button' class='aui_state_highlight dialog_hide' onclick='continueImport();'>继续</button><button type='button' class='aui_state_highlight dialog_hide' onclick='hideTooltipDialog();'>取消</button>");
					$("div[id='tooltip_dialog'] .tooltip_icon").children().eq(0).attr("class","tooltip_warn");
					$("div[id='tooltip_dialog'] .tooltip_icon").children().eq(1).html("警告");
					$("div[id='tooltip_dialog'] .tooltip_info").html("<p>你本次要导入的资源为<em>"+importAmmount+"</em>个，超过<em>1000</em>个。因为数据太多，可能会超时，造成意外错误或脏数据，或部分导入部分未导入的意外情况。</p><p>建议你拆分文件，分多次导入。</p><p>你仍然要继续要导入吗？</p>");
					$("#tooltip_dialog").show();
				}else if( importAmmount>5000){
					$("div[id='tooltip_dialog'] .dialog_but").html("<button type='button' class='aui_state_highlight dialog_hide' onclick='hideTooltipDialog();'>确定</button>");
					$("div[id='tooltip_dialog'] .tooltip_icon").children().eq(0).attr("class","tooltip_error");
					$("div[id='tooltip_dialog'] .tooltip_icon").children().eq(1).html("错误");
					$("div[id='tooltip_dialog'] .tooltip_info").html("<p>你本次要导入的资源为<em>"+importAmmount+"</em>个，超过<em>5000</em>个。因为数据太多，可能会造成意外错误，系统将禁止操作。</p><p>请拆分文件，分多次导入。建议单次导入的数量不超过1000个</p>");
					$("#tooltip_dialog").show();
				}else{
					if(importMaps==""){
						 alert("无资源数据导入！详情请查看【-】详细.")
						 showError($("#errorLink").children().eq(0));
						 $("#loading_div").hide();
						 $("#black").hide();
						 return;
					}
					escEnable=true;
					$("#totalCount").html(data.contentMapList.length);
					$("#realCount").html(importAmmount);
					$("#currentImport").html("已导入成功0个");
					$("#importResultDiv").show();
					var error = $("#errorMessage").html();
					if(error!=""){
						showError($("#errorLink").children().eq(0));
					}
					importMaps = "["+importMaps.substring(1,importMaps.length)+"]";
					importRecord = "["+importRecord.substring(1,importRecord.length)+"]";
					rowNums = "["+rowNums.substring(1,rowNums.length)+"]";
					assRecord = "["+assRecord.substring(1,assRecord.length)+"]";
					var assType = data.assType;
					var assEntityType = data.assEntityType;
					var opScene = $("#op_scene_text").val();
					var opCause = $("#op_cause_text").val();
					var rdoAssModel = $("input[name='rdoAssModel']:checked").val();
					var params={rdoAssModel:rdoAssModel,importModel:importModel,opScene:opScene,opCause:opCause,importMaps:importMaps,importRecord:importRecord,rowNums:rowNums,assRecord:assRecord,assType:assType,assEntityType:assEntityType,importEntityType:importEntityType};
					var url = "importAddResourceAction";
					/*if(importModel=="deleteAdd"){
						url = "importDeleteAddResourceAction";
					}*/
					var iter =  setInterval(function(){
						$.post("getCurrentImportCoutAction",function(data){
							if(data!=null){
								$("#currentImport").html(data);
							}
						},'json');
					},5000)
					$.post(url,params,function(result){
						if(result.resultList!=null && result.resultList!="" && result.resultList!=""){
							var allContent = "";
							$.each(result.resultList,function(k,v){
								//获取一个entity
								var content = "";
							   $.each(data.contentMapList[v.row], function(key, value){
									if(!(key == "id"  || key == "_entityId" || key == "_entityType")) {
										content += "<td>" + value + "</td>";
									} else if(key == "id") {
										content += "<input type='hidden' value='"+key+"_"+v.id+"' />";	
									}
								});
								content += "<input type='hidden' value='aeType_"+importEntityType+"' />";
								if("no"==v.associate){
									content = "<tr style='color:red' onclick='chooseTr(this);' ondblclick='fowardToOperPage(this);' name='"+importEntityType+"#"+v.id+"'><td><input name='cbxResEntity' type='checkbox' /></td>"+content+"</tr>";
								}else{
									content = "<tr  onclick='chooseTr(this);' ondblclick='fowardToOperPage(this);' name='"+importEntityType+"#"+v.id+"'><td><input name='cbxResEntity' type='checkbox' /></td>"+content+"</tr>";
								}
								allContent +=content;
							})
		                    $("#showContentTable").append(allContent);
		                   
						}
						if(result.resultMsg!=null && result.resultMsg!="" && result.resultMsg!=""){
							/*var error = $("#errorMessage").html();
							error = error+result.resultMsg;
							$("#errorMessage").html(error);*/
							$("#currentImport").html(result.resultMsg);
						}
						 $("#loading_div").hide();
						 $("#black").hide();
						 clearInterval(iter);
						 $("#importResultDiv").hide();
						 $("#lastResultDiv").html("导入结果：共"+$("#totalCount").html()+"个资源，实际要导入"+$("#realCount").html()+"个资源，"+$("#currentImport").html());
					},'json')			
				}
				
				return;
			}
			var tempObj=eval(data.contentMapList[index]);
			var i=0;
			entityMap="";
			var str="";
			for(var key in tempObj){
				//alert('key=='+key)
				//alert('value=='+tempObj[key])
				if(tempObj[key]!=""){
					var schar=hasSpecialChar(tempObj[key]);
					if(schar!=""){
						str+=",【"+tempObj[key]+"】值";
					}
					if(key=="aeType"||key=="_entityType"){
						continue;
					}		
					if(i==0){
						entityMap+="\""+key+"\":\""+tempObj[key]+"\"";
					}else{
						entityMap+=",\""+key+"\":\""+tempObj[key]+"\"";
					}
						i++;
				}
				
			}
			if(str!=""){
				 str ="第"+(index+2)+"行导入资源数据中"+str+"存在特殊字符（\",\',\\,/），暂不支持导入. \n";
				 var error = $("#errorMessage").html();
				 error = error+str;
				 $("#errorMessage").html(error);
			}else{
				entityMap="{"+entityMap+"}";
				if(data.curConditionList[index]!=null && data.curConditionList[index]!=""){

							if(data.curConditionList[index].length==1){
								
								if(data.curConditionList[index][0].hasChild=="has"){
									if(confirm(data.curConditionList[index][0].name+"有子资源实例，是否进行删除替换导入？")){
										importMaps +=","+entityMap;
										importRecord +=",'"+data.curConditionList[index][0].id+"'";
										rowNums +=",'"+index+"'";
										importAmmount++;
									}else{
										var error = $("#errorMessage").html();
										error = error+"第"+(index+2)+"行资源用户取消导入,因为替换的资源有子资源";
										$("#errorMessage").html(error);
									}
								}else{
									importMaps +=","+entityMap;
									importRecord +=",'"+data.curConditionList[index][0].id+"'";
									rowNums +=",'"+index+"'";
									importAmmount++;
								}	
							}else if(data.curConditionList[index].length>1){
									var content = "";
									$.each(data.curConditionList[index], function(i, obj){
										var entityName = null;
										entityName = obj.name;
										var rdoContent = "";
										//默认让第一单选选中
										if(i == 0) {
											rdoContent = "<input type='radio' name='updateResult' checked='checked' />";
										} else {
											rdoContent = "<input type='radio' name='updateResult' />";
										}
										content +="<li>" + rdoContent
										+ "<span style='color:blue' onmouseout='hideInfo();' onmouseover='showInfo(this,event);'>"+entityName+"</span><input type='hidden' value='"+obj.id+"' />" 
										+ "<input type='hidden' value='"+importEntityType+"' /><input type='hidden' value='"+obj.hasChild+"' />&nbsp;&nbsp;<span style='margin-left:110px'><a href='getPhysicalresForOperaAction?currentEntityId="+obj.id+"&currentEntityType="+importEntityType+"&areaId="+areaId+"&modelType=view' style='text-decoration:underline' target='_blank'>详情浏览>></a>&nbsp;&nbsp;<a onclick=\"javascript:showParResource('"+obj.id+"','"+importEntityType+"',event)\" style='text-decoration:underline'>资源全路径>></a></li></span>";
									});
									$("#updateImportAttribute").html($("#linkAttributes option:selected").text());
									//生成隶属资源单选按钮，并弹出选择层
									$("#updateEntityDiv ul").html(content);
									$("#updateEntityDiv").fadeIn("normal");
									$("#black").show();
									escEnable=true;
									//避免导入资源的名称获取过快，使用延迟操作
									setTimeout(function(){
										//获取导入资源的名称
										if(data.contentMapList[index].name != null) {
											$("#updateImportResName").html(data.contentMapList[index].name);
										} else {
											$("#updateImportResName").html(data.contentMapList[index].label);
										}
									},500);
									return;
							}		
					}else{
						var error = $("#errorMessage").html();
						if(data.contentMapList[index].name != null) {
							error = error+"第"+(index+2)+"行资源“"+data.contentMapList[index].name+"”当前区域范围数据库不存在记录，不导入数据 \n";
						} else {
							error = error+"第"+(index+2)+"行资源“"+data.contentMapList[index].label+"”当前区域范围数据库不存在记录，不导入数据 \n";
						}
						$("#errorMessage").html(error);
						
					}
			}
			
			
			
			if(data.parNameList!=undefined){
			    var selectResChosenType = $("select[name='selectResChosenType']").val();
			    var rdoAssModel = $("input[name='rdoAssModel']:checked").val();
			    var selectAttrExactMatch = $("select[name='selectAttrExactMatch']").val();
			    var selectAttrIndistinctMatch = $("select[name='selectAttrIndistinctMatch']").val();
			    if(data.parNameList[index]=="" || data.parNameList[index]==null || data.parNameList[index]=="null"){		
					var error = $("#errorMessage").html();
					error = error+"第"+(arrIndex+2)+"行资源关联上级资源【"+data.parMatchList[index]+"】当前区域范围不存在。 \n";
					$("#errorMessage").html(error);
					assRecord += ",'0'";	
					//直接跳进下一次循环
					arrIndex = arrIndex + 1;
					operateExistEntity(arrIndex, bigMap);
					
				}else if(data.parNameList[index].length==1){
					
					assRecord += ",'"+data.parNameList[index][0].id+"'";

					//由于配对只有一个父，直接跳进下一次循环
					arrIndex = arrIndex + 1;
					operateExistEntity(arrIndex, bigMap);
				}else if(data.parNameList[index].length>1){
					var content = "";
					//查询出来的隶属资源有多个(弹出选择层，让用户进行隶属资源的选择)
					var matchTxt ="";
					$.each(data.parNameList[index], function(i, obj){
						var entityName = null;
						entityName = obj.name;
						matchTxt=entityName;
						var rdoContent = "";
						//默认让第一单选选中
						if(index == 0) {
							rdoContent = "<input type='radio' name='matchResult' checked='checked' />";
						} else {
							rdoContent = "<input type='radio' name='matchResult' />";
						}
						content +="<li>" + rdoContent
						+ "<span style='color:blue' onmouseout='hideInfo();' onmouseover='showInfo(this,event);'>"+entityName+"</span><input type='hidden' value='"+obj.id+"' />" 
						+ "<input type='hidden' value='"+data.assEntityType+"' />&nbsp;&nbsp;<span style='margin-left:110px'><a href='getPhysicalresForOperaAction?currentEntityId="+obj.id+"&currentEntityType="+data.assEntityType+"&areaId="+areaId+"&modelType=view' style='text-decoration:underline' target='_blank'>详情浏览>></a>&nbsp;&nbsp;<a onclick=\"javascript:showParResource('"+obj.id+"','"+data.assEntityType+"',event)\" style='text-decoration:underline'>资源全路径>></a></li></span>";
					
					});
					
					//生成隶属资源单选按钮，并弹出选择层
					$("#operaAssDiv ul").html(content);
					$("#operaAssDiv").fadeIn("normal");
					$("#black").show();
					
					//避免导入资源的名称获取过快，使用延迟操作
					setTimeout(function(){
						//获取导入资源的名称
						if(data.contentMapList[index].name != null) {
							$("#txtImportResName").html(data.contentMapList[index].name);
						} else {
							$("#txtImportResName").html(data.contentMapList[index].label);
						}
						$("#txtMatch").html(data.parMatchList[index]);
					},500);
					return;
				}
			}else if(data.assEntityId!=undefined){
				
				if(data.curConditionList[index]!=null && data.curConditionList[index]!=""){
					if(data.assEntityType==importEntityType&&data.curConditionList[index][0].id==data.assEntityId){
						assRecord += ",'0'";
						arrIndex = arrIndex + 1;
					}else{
						assRecord += ",'"+data.assEntityId+"'";
						arrIndex = arrIndex + 1;
					}
				}else{
					assRecord += ",'"+data.assEntityId+"'";
					arrIndex = arrIndex + 1;
				}
				operateExistEntity(arrIndex, bigMap);
			
			}else{
				assRecord += ",'0'";	
				arrIndex = arrIndex + 1;
				operateExistEntity(arrIndex, bigMap);
			}
	}
	
	//查询隶属资源(存在一个或者多个时的处理)
	function searchAssEntity(index, data) {
		//alert(importEntityType)
		//导入资源已结束，隐藏数据加载提示层
		if(index >= data.contentMapList.length) {
			var error = $("#errorMessage").html();
			var errorMsg = ""
			if(error.indexOf("同名")>=0){
				errorMsg = "导入的资源（站址或基站）有同名资源。 \n ";
			}
			if(data.resultMsg!="" && data.resultMsg!=null && data.resultMsg!="null" && data.resultMsg!=undefined){
				error = $("#errorMessage").html();
				error = error+data.resultMsg;
				$("#errorMessage").html(error);
				errorMsg = "导入的资源有数据不规范。 \n"+errorMsg;
			}
			if(errorMsg!=""){
				errorMsg += "禁止导入，详情请查看【-】详细.";
				showError($("#errorLink").children().eq(0));
				alert(errorMsg);
				$("#loading_div").hide();
				$("#black").hide();
			}else{
				if(importAmmount>1000&&importAmmount<=5000){
					$("#importData").data("importData",data);
					$("div[id='tooltip_dialog'] .dialog_but").html("<button type='button' class='aui_state_highlight dialog_hide' onclick='continueImport();'>继续</button><button type='button' class='aui_state_highlight dialog_hide' onclick='hideTooltipDialog();'>取消</button>");
					$("div[id='tooltip_dialog'] .tooltip_icon").children().eq(0).attr("class","tooltip_warn");
					$("div[id='tooltip_dialog'] .tooltip_icon").children().eq(1).html("警告");
					$("div[id='tooltip_dialog'] .tooltip_info").html("<p>你本次要导入的资源为<em>"+importAmmount+"</em>个，超过<em>1000</em>个。因为数据太多，可能会超时，造成意外错误或脏数据，或部分导入部分未导入的意外情况。</p><p>建议你拆分文件，分多次导入。</p><p>你仍然要继续要导入吗？</p>");
					$("#tooltip_dialog").show();
				}else if( importAmmount>5000){
					$("div[id='tooltip_dialog'] .dialog_but").html("<button type='button' class='aui_state_highlight dialog_hide' onclick='hideTooltipDialog();'>确定</button>");
					$("div[id='tooltip_dialog'] .tooltip_icon").children().eq(0).attr("class","tooltip_error");
					$("div[id='tooltip_dialog'] .tooltip_icon").children().eq(1).html("错误");
					$("div[id='tooltip_dialog'] .tooltip_info").html("<p>你本次要导入的资源为<em>"+importAmmount+"</em>个，超过<em>5000</em>个。因为数据太多，可能会造成意外错误，系统将禁止操作。</p><p>请拆分文件，分多次导入。建议单次导入的数量不超过1000个</p>");
					$("#tooltip_dialog").show();
				}else{
					if(importMaps==""){
						 alert("无资源数据导入！详情请查看【-】详细.")
						 showError($("#errorLink").children().eq(0));
						 $("#loading_div").hide();
						 $("#black").hide();
						 return;
					}
					$("#totalCount").html(data.contentMapList.length);
					$("#realCount").html(importAmmount);
					$("#currentImport").html("已导入成功0个");
					$("#importResultDiv").show();
					var error = $("#errorMessage").html();
					if(error!=""){
						showError($("#errorLink").children().eq(0));
					}
					escEnable=true;
					importMaps = "["+importMaps.substring(1,importMaps.length)+"]";
					//importRecord = "["+importRecord.substring(1,importRecord.length)+"]";
					rowNums = "["+rowNums.substring(1,rowNums.length)+"]";
					assRecord = "["+assRecord.substring(1,assRecord.length)+"]";
					var assType = data.assType;
					var assEntityType = data.assEntityType;
					var opScene = $("#op_scene_text").val();
					var opCause = $("#op_cause_text").val();
					var rdoAssModel = $("input[name='rdoAssModel']:checked").val();
					//var importEntityType = $("#hidImportEntityType").val();
					var params={rdoAssModel:rdoAssModel,importModel:importModel,opScene:opScene,opCause:opCause,importMaps:importMaps,rowNums:rowNums,assRecord:assRecord,assType:assType,assEntityType:assEntityType,importEntityType:importEntityType};
					var url = "importAddResourceAction";
					var iter =  setInterval(function(){
						$.post("getCurrentImportCoutAction",function(data){
							if(data!=null){
								$("#currentImport").html(data);
							}
						},'json');
					},5000)
					$.post(url,params,function(result){
						if(result.resultList!=null && result.resultList!="" && result.resultList!=""){
							var allContent = "";
							$.each(result.resultList,function(k,v){
								//获取一个entity
								var content = "";
							   $.each(data.contentMapList[v.row], function(key, value){
									if(!(key == "id"  || key == "_entityId" || key == "_entityType")) {
										content += "<td>" + value + "</td>";
									} else if(key == "id" ) {
										content += "<input type='hidden' value='"+key+"_"+v.id+"' />";
									}
								});
								content += "<input type='hidden' value='aeType_"+importEntityType+"' />";
								if("no"==v.associate){
									content = "<tr style='color:red' onclick='chooseTr(this);' ondblclick='fowardToOperPage(this);' name='"+importEntityType+"#"+v.id+"'><td><input name='cbxResEntity' type='checkbox' /></td>"+content+"</tr>";
								}else{
									content = "<tr  onclick='chooseTr(this);' ondblclick='fowardToOperPage(this);' name='"+importEntityType+"#"+v.id+"'><td><input name='cbxResEntity' type='checkbox' /></td>"+content+"</tr>";
								}
								allContent +=content;
							})
		                    $("#showContentTable").append(allContent);
		                   
						}
						if(result.resultMsg!=null && result.resultMsg!="" && result.resultMsg!=""){
							/*var error = $("#errorMessage").html();
							error = error+result.resultMsg;
							$("#errorMessage").html(error);*/
							$("#currentImport").html(result.resultMsg);
						}
						 $("#loading_div").hide();
						 $("#black").hide();
						 clearInterval(iter);
						 $("#importResultDiv").hide();
						 $("#lastResultDiv").html("导入结果：共"+$("#totalCount").html()+"个资源，实际要导入"+$("#realCount").html()+"个资源，"+$("#currentImport").html());
					},'json')
					
				  
				}
				
			}
			
			return;
		}
		
		for(var i = index; i < data.contentMapList.length; i++) {
			var tempObj=eval(data.contentMapList[index]);
			var iFlag=0;
			entityMap="";
			var str="";
			for(var key in tempObj){
				if(tempObj[key]!=""){
				    //var kValue=string2Json(tempObj[key])
				    var schar=hasSpecialChar(tempObj[key]);
					if(schar!=""){
						str+=",【"+tempObj[key]+"】值";
					}
					if(key=="aeType"||key=="_entityType"){
						continue;
					}		
					if(iFlag==0){
						entityMap+="\""+key+"\":\""+tempObj[key]+"\"";
					}else{
						entityMap+=",\""+key+"\":\""+tempObj[key]+"\"";
					}
						iFlag++;
				}
			}
			if(str!=""){
				 str ="第"+(index+2)+"行导入资源数据中"+str+"存在特殊字符（\",\',\\,/），暂不支持导入. \n";
				 var error = $("#errorMessage").html();
				 error = error+str;
				 $("#errorMessage").html(error);
			}else{
				entityMap="{"+entityMap+"}";
				var opScene = $("#op_scene_text").val();
				var opCause = $("#op_cause_text").val();
				var ajaxResult = "";
				var updateId="";//更新资源id
				if(importEntityType.indexOf("BaseStation")>=0 || importEntityType=="Station"){			
					if(data.curConditionList[i]!=null && data.curConditionList[i]!=""){
						 	var error = $("#errorMessage").html();
							error = error+"第"+(i+2)+"行资源“"+data.curConditionList[i][0].name+"”当前区域范围已经存在.（基站和站址同一区域范围不允许同名） \n";
							$("#errorMessage").html(error);
					}else{
							importMaps +=","+entityMap;
							rowNums +=",'"+i+"'";
							importAmmount++;
					}
				}else{
						importMaps +=","+entityMap;
						rowNums +=",'"+i+"'";
						importAmmount++;
				}
			}
			
			var errorMs = $("#errorMessage").html();
			if((data.resultMsg==null||data.resultMsg=="")){
				if(importEntityType.indexOf("BaseStation")>=0 || importEntityType=="Station" ){
					if(errorMs.indexOf("不允许同名")>=0){
						//直接跳进下一次循环
						arrIndex = arrIndex + 1;
						searchAssEntity(arrIndex, bigMap);
						return;
					}
				}
				if(data.parNameList!=undefined){
				    var selectResChosenType = $("select[name='selectResChosenType']").val();
				    var rdoAssModel = $("input[name='rdoAssModel']:checked").val();
				    var selectAttrExactMatch = $("select[name='selectAttrExactMatch']").val();
				    var selectAttrIndistinctMatch = $("select[name='selectAttrIndistinctMatch']").val();
				    if(data.parNameList[i]=="" || data.parNameList[i]==null || data.parNameList[i]=="null"){		
						assRecord += ",'0'";	
						var error = $("#errorMessage").html();
						error = error+"第"+(arrIndex+2)+"行资源关联上级资源【"+data.parMatchList[i]+"】当前区域范围不存在。 \n";
						$("#errorMessage").html(error);
						//直接跳进下一次循环
						arrIndex = arrIndex + 1;
						searchAssEntity(arrIndex, bigMap);
					}else if(data.parNameList[i].length==1){
						
						assRecord += ",'"+data.parNameList[i][0].id+"'";
	
						//由于配对只有一个父，直接跳进下一次循环
						arrIndex = arrIndex + 1;
						searchAssEntity(arrIndex, bigMap);
					}else if(data.parNameList[i].length>1){
						var content = "";
						//查询出来的隶属资源有多个(弹出选择层，让用户进行隶属资源的选择)
						var matchTxt ="";
						$.each(data.parNameList[i], function(index, obj){
							var entityName = null;
							entityName = obj.name;
							matchTxt=entityName;
							var rdoContent = "";
							//默认让第一单选选中
							if(index == 0) {
								rdoContent = "<input type='radio' name='matchResult' checked='checked' />";
							} else {
								rdoContent = "<input type='radio' name='matchResult' />";
							}
							content +="<li>" + rdoContent
							+ "<span style='color:blue' onmouseout='hideInfo();' onmouseover='showInfo(this,event);'>"+entityName+"</span><input type='hidden' value='"+obj.id+"' />" 
							+ "<input type='hidden' value='"+data.assEntityType+"' />&nbsp;&nbsp;<span style='margin-left:110px'><a href='getPhysicalresForOperaAction?currentEntityId="+obj.id+"&currentEntityType="+data.assEntityType+"&areaId="+areaId+"&modelType=view' style='text-decoration:underline' target='_blank'>详情浏览>></a>&nbsp;&nbsp;<a onclick=\"javascript:showParResource('"+obj.id+"','"+data.assEntityType+"',event)\" style='text-decoration:underline'>资源全路径>></a></li></span>";
						
						});
						
						//生成隶属资源单选按钮，并弹出选择层
						$("#operaAssDiv ul").html(content);
						$("#operaAssDiv").fadeIn("normal");
						$("#black").show();
						
						//避免导入资源的名称获取过快，使用延迟操作
						setTimeout(function(){
							//获取导入资源的名称
							if(data.contentMapList[i].name != null) {
								$("#txtImportResName").html(data.contentMapList[i].name);
							} else {
								$("#txtImportResName").html(data.contentMapList[i].label);
							}
							$("#txtMatch").html(data.parMatchList[i]);
						},500);
						return;
					}
				}else if(data.assEntityId!=undefined){
					
					assRecord += ",'"+data.assEntityId+"'";
					arrIndex = arrIndex + 1;
					searchAssEntity(arrIndex, bigMap);
				
				}else{
					assRecord += ",'0'";	
					arrIndex = arrIndex + 1;
					searchAssEntity(arrIndex, bigMap);
				}
			}else{
				arrIndex = arrIndex + 1;
				searchAssEntity(arrIndex, bigMap);
			}
			
			break;
		}
	}
	
	
	//加载并显示子级类型信息
	function showChildTypeMsg(me) {
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
		//加载link类型的类型
		var addLink = "addLink";
		var params = {currentEntityId:currentEntityId,currentEntityType:currentEntityType,addLink:addLink}
		
		//是否完成加载类型
		var isFinishAddType = false;
		
		if($("#hidTypeFlush").data(currentEntityType + "_" + currentEntityId)) {
			//已通过缓存保存类型信息的情况
			var content = $("#hidTypeFlush").data(currentEntityType + "_" + currentEntityId);
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
						imgContent = "<img src='image/plus.gif' onclick='showChildEntityMsg(this);' style='visibility:hidden;' />";
						content += "<li style='display:none'>" 
						+ imgContent 
						+ "<span onclick='chooseType(this);' style='padding-left:5px;cursor:pointer;color:#F3AA36'>" + obj.chineseType + "(...)</span>" 
						+ "<input type='hidden' value=" + obj.assType + " />" 
						+ "<input type='hidden' value=" + obj.type + ">" 
						+ "<div style='padding-left:20px;display:none;'></div>" 
						+ "</li>";
					} else {
						imgContent = "<img src='image/plus.gif' onclick='showChildEntityMsg(this);' />";
						content += "<li>" 
						+ imgContent 
						+ "<span onclick='chooseType(this);' style='padding-left:5px;cursor:pointer;color:#F3AA36'>" + obj.chineseType + "(...)</span>" 
						+ "<input type='hidden' value=" + obj.assType + " />" 
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
				$("#hidTypeFlush").data(currentEntityType + "_" + currentEntityId, content);
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
					
					//因为一种类型针对另外一种类型查询数量时，只会是child或者link的一种，
					//所以需要求出child和link的总和，实质是根据类型判断是要获取child的数量还是link的数量
					var addLink = "addLink";
					
					var params = {parentEntityType:parentEntityType,parentEntityId:parentEntityId,searchType:searchType,addLink:addLink}
					
					if($("#hidTypeFlush").data(parentEntityType + "_" + parentEntityId + "_addContentCount" + index)) {
						//获取增加数值后的缓存内容
						var operatedObj = liObj.children().eq(1);
						var addCountContent = $("#hidTypeFlush").data(parentEntityType + "_" + parentEntityId + "_addContentCount" + index);
						//提取对应类型的数量
						var count = addCountContent.substring(addCountContent.lastIndexOf("(")+1,addCountContent.lastIndexOf(")"));
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
							$("#hidTypeFlush").data(parentEntityType + "_" + parentEntityId + "_addContentCount" + index,firstContent.substring(0, countIndex) + "("+data+")");
						},'json');
					}
				});
				
				isFinishAddType = false;
				//跳出判断是否加载类型完毕的循环
				clearInterval(addCountInterval);
			}
		},10);
	}
	//为搜索加载子级entity信息
		function getChildEntityMsgForSearch(me){
			var url = "getChildEntityByTypeAction"; //获取子级entity
			//获取父级节点的类型
			var parentEntityType = $(me).parent().parent().parent().prev().val();
			//获取父级节点的id
			var parentEntityId = $(me).parent().parent().parent().prev().prev().val();
			//被查找的类型(即当前节点的类型)
			var searchType = $(me).next().next().next().val();
			//需要关联的类型(即父节点与本节点的关联关系)
			var linkType = $(me).next().next().val();
			var params = {parentEntityType:parentEntityType,parentEntityId:parentEntityId,searchType:searchType,linkType:linkType}
			
			if($("#hidEntityFlush").data(parentEntityType + "_" + parentEntityId + "_" + searchType)) {
				var content = $("#hidEntityFlush").data(parentEntityType + "_" + parentEntityId + "_" + searchType, content);
				//加载子级内容
				//判断有无出现单选控件
				if($(me).parent().children().length == 5) {
					$(me).next().next().next().next().get(0).innerHTML = content;
				} else {
					//除了区域类型，多了单选按钮，所以要增加一个next
					$(me).next().next().next().next().next().get(0).innerHTML = content;
				}
			} else {
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
					var entityType = $(me).next().next().next().val();
					if(obj.name != null) {
						entityName = obj.name;
					} else {
						entityName = obj.label;
					}
					
					var imgContent = "";
					var radioContent = "";
					//获取所选择的上级资源类型(因为当时的值为组合值，例如Station_parent，所以要进行substring操作，获取entity类型)
						var rdoChosenType = $("#selResChosenType").val();
						rdoChosenType = rdoChosenType.substring(0, rdoChosenType.lastIndexOf("_"));
						
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
							imgContent = "<img src='image/plus.gif' onclick='showChildTypeMsg(this);' />";
						} else {
							imgContent = "<img src='image/plus.gif' onclick='showChildTypeMsg(this);' style='visibility:hidden;' />";
						}
						
						//所选择的entity类型为当前类型，生成单选按钮
						if(searchType == rdoChosenType) {
							radioContent = "<input class='input_radio' name='rdoChoose' type='radio' />";
						}
					
					
		
					content += "<li>" 
						+ imgContent 
						+ radioContent
						+ "<span onclick='chooseEntity(this);' style='padding-left:5px;cursor:pointer;'>" + entityName + "</span>" 
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
				
				
				//通过缓存保存节点信息
				//$("#hidEntityFlush").data(parentEntityType + "_" + parentEntityId + "_" + searchType, content);
					}
				})
				
			}
		
		}
	
	//加载并显示子级entity信息
	function showChildEntityMsg(me) {
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
		//需要关联的类型(即父节点与本节点的关联关系)
		var linkType = $(me).next().next().val();
		var params = {parentEntityType:parentEntityType,parentEntityId:parentEntityId,searchType:searchType,linkType:linkType}
		
		if($("#hidEntityFlush").data(parentEntityType + "_" + parentEntityId + "_" + searchType, content)) {
			var content = $("#hidEntityFlush").data(parentEntityType + "_" + parentEntityId + "_" + searchType, content);
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
					var rdoChosenType = $("#selResChosenType").val();
					rdoChosenType = rdoChosenType.substring(0, rdoChosenType.lastIndexOf("_"));
					
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
						imgContent = "<img src='image/plus.gif' onclick='showChildTypeMsg(this);' />";
					} else {
						imgContent = "<img src='image/plus.gif' onclick='showChildTypeMsg(this);' style='visibility:hidden;' />";
					}
					
					//所选择的entity类型为当前类型，生成单选按钮
					if(searchType == rdoChosenType) {
						radioContent = "<input class='input_radio' name='rdoChoose' type='radio' />";
					}
		
					content += "<li>" 
						+ imgContent 
						+ radioContent
						+ "<span onclick='chooseEntity(this);' style='padding-left:5px;cursor:pointer;'>" + entityName + "</span>" 
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
				//$("#hidEntityFlush").data(parentEntityType + "_" + parentEntityId + "_" + searchType, content);
			},'json');
		}
	}
	      
    //点击所选择的类型
	function chooseType(me) {
			$("#treeDiv span").css("background", "");
			$("#treeDiv span").attr("class", "");
			$(me).css("background", "#E0EEEE");//选中的实体颜色
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
			$("#chosenEntityType").val("");
			$("#chosenEntityId").val("");
	}
	      
    //点击所选择的实体
	function chooseEntity(me) {
		$("#treeDiv span").css("background", "");
		$("#treeDiv span").attr("class", "");
		$(me).css("background", "#E0EEEE");//选中的实体颜色
		$(me).attr("class","spanChosenClass");//选中的实体增加一个选中的class属性
		var currentEntityType="";
		var currentEntityId="";
		
		currentEntityId = $(me).next().val();//当前节点的id
		currentEntityType = $(me).next().next().val();//当前节点的类型
		

		$("#chosenEntityType").val(currentEntityType);
		$("#chosenEntityId").val(currentEntityId);
		$("#addedResParentEntityType").val("");
		$("#addedResParentEntityId").val("");
			//获取被添加物理资源类型
		$("#addedResEntityType").val("");
	}
	
	//单击选中table行
	function chooseTr(me) {
		if($(me).attr("name")!=undefined && $(me).attr("name")!=""){
			$("#showContentTable tr").removeClass();
			$(me).addClass("chosenClass");
		}
		
	}
	//下载资源导入模板
	function downResourceImportModule(id){
		var importEntityType=$("#hidImportEntityType").val();
		var selectResChosenTypeName=$("#choosedResourceType").val();;
		var moduleFileName = $(id).html();
		//alert(moduleFileName)
		var params="";
		if(selectResChosenTypeName=="请选择"){
			params = "?importEntityType="+importEntityType+"&moduleFileName="+moduleFileName;	
		}else{
			params =  "?importEntityType="+importEntityType+"&moduleFileName="+moduleFileName+"&selectResChosenTypeName="+selectResChosenTypeName;
		}
		var url="downloadResourceImportModuleAction"+params;
		location.href= url;
		
	}
	
	//选择导入的资源类型
	function chooseResourceType(me){
		$("#txtExactContent").val("");
		$("#hidAssEntityId").val("");
		$("#hidAssEntityType").val("");
		var htmlContent="";
		/*if($(me).parent().parent().parent().hasClass("dictionary")){
			htmlContent = $(me).html()+"";
			htmlContent = htmlContent.substring(htmlContent.indexOf(":")+1,htmlContent.length);

			$("#choosedResourceType").html(htmlContent);
		}else{
			htmlContent = $(me).html();
			$("#choosedResourceType").html($(me).html());
		}*/
		//$("#searchType").val($(me).val());
		htmlContent=$(me).next().text();
	    $("#choosedResourceType").val($(me).next().text())
		var chosenType = $(me).val();
		$("#directFill").removeAttr('disabled');//直接录入取得checkbox disabled
		$("#directFill").removeAttr('checked');
		//把选择需要导入的资源中文类型，先记录在导入资源选择层
		
		$("#txtImportResType").html(htmlContent);
		$("#updateImportResType").html(htmlContent);
			
		
		//精确隶属资源选择层隐藏，防止误操作
		$("#assResDiv").slideUp('fast');

		
		$("#downModule").html(htmlContent+"导入模板.xls");
		
		//此资源类型是否有自定义导入模板
		$.post("hasDownLoadFileAction",{downloadPath:"/importmodulefile/",fileName:htmlContent+"导入模板(自定义).xls"},function(data){
				if(data=="success"){
					var fileName = htmlContent+"导入模板(自定义).xls";
					$("#downSelfModule").html(htmlContent+"导入模板(自定义).xls");	
					$("#downSelfModule").attr("href","downLoadFileAction?fileName="+fileName+"&downloadType=importModule");	
					$("#selfImportSpan").show();
				}else{
					$("#selfImportSpan").hide();
					$("#downSelfModule").html("").removeAttr("href");
				}
		},'json');
		
		
		$("#hidImportEntityType").val(chosenType);
		//加载所选择资源类型关联的类型
		$.post("getAssTypeByChosenTypeAction",{chosenType:chosenType},function(data){
			if(data.length==1){
				$("#selResChosenType").hide();
				$("#selectResChosenTypeText").show();
				$("#selResChosenType").html("");
				$.each(data, function(index, obj){
					if(obj.chineseType == null || obj.chineseType == "") {
						$("#selResChosenType").append("<option value='"+obj.type+"_"+obj.assType+"'>"+obj.type+"</option>");
						$("#selectResChosenTypeText").html(obj.type);
						$("#txtAssResType").html(obj.type);
					} else {
						$("#selResChosenType").append("<option value='"+obj.type+"_"+obj.assType+"'>"+obj.chineseType+"</option>");
						$("#selectResChosenTypeText").html(obj.chineseType);
						$("#txtAssResType").html(obj.chineseType);
					}
					if(obj.type.indexOf("Sys_Area") > -1) {
						if($("#tree").children().children().length == 5) {
							$("#tree").children().children().eq(0).after("<input class='input_radio' name='rdoChoose' type='radio' />");
						}
					} else {
						if($("#tree").children().children().length == 6) {
							$("#tree").children().children().eq(1).remove();
						}
					}
					$.post("getAttrByChosenTypeAction",{chosenType:obj.type,showType:"import"},function(data){
						$("#selAttrExactMatch").html("<option value='请选择'>请选择</option>");
						$("#selAttrIndistinctMatch").html("<option value='请选择'>请选择</option>");
						$.each(data, function(index, obj){
							var type = obj.substring(0, obj.indexOf("_")); //截取类型
							var chineseType = obj.substring(obj.indexOf("_") + 1); //截取中文类型
							//把获取的类型加载至精确和模糊查找下拉框
							$("#selAttrExactMatch").append("<option value='"+type+"'>"+chineseType+"</option>");
							$("#selAttrIndistinctMatch").append("<option value='"+type+"'>"+chineseType+"</option>");
						});
					},'json');
					
				});
				
			}else{
				$("#selAttrExactMatch").html("<option value='请选择'>请选择</option>");
				$("#selAttrIndistinctMatch").html("<option value='请选择'>请选择</option>");
				$("#selResChosenType").show();
				$("#selectResChosenTypeText").hide();
				$("#selResChosenType").html("<option value='请选择'>请选择</option>");
				$.each(data, function(index, obj){
					if(obj.chineseType == null || obj.chineseType == "") {
						$("#selResChosenType").append("<option value='"+obj.type+"_"+obj.assType+"'>"+obj.type+"</option>");
					} else {
						$("#selResChosenType").append("<option value='"+obj.type+"_"+obj.assType+"'>"+obj.chineseType+"</option>");
					}
				});
			}
			
		},'json');
		//更新 删除导入模式 下拉框
		$.post("getAttributesByOrderIdAction",{chosenType:chosenType},function(data){
			$("#linkAttributes").html("<option value=''>请选择</option>");
			$("#attributesUl").html("<li onmouseover='liMouseOver(this)' onclick='liMouseClick(this)' id=''>请选择</li>");
			$.each(data, function(index, obj){
				$("#linkAttributes").append("<option value='"+index+"'>"+obj+"</option>");
				$("#attributesUl").append("<li onmouseover='liMouseOver(this)' onclick='liMouseClick(this)'  id='"+index+"'>"+obj+"</li>");
			});
			var w = parseInt($("#linkAttributes").css("width").replace("px",""))+3;
			$("#attributesUl").parent().css("width",w);
		},'json');
		//表格头
		$.post("getAttrByChosenTypeAction",{chosenType:chosenType,showType:"addResouceDirectly"},function(data){
			$("#showContentTable").html("");	
			var titleContent = "";
			$.each(data.attrList, function(index, obj){
				var type = obj.substring(0, obj.lastIndexOf("_")); //截取类型
				var chineseType = obj.substring(obj.lastIndexOf("_") + 1); //截取中文类型
				$.each(data.nullMap,function(key,value){
					if(type==key){
						if(value=='false'){
							titleContent += "<th id='"+chosenType+"#"+type+"' class='mustFill'><em style='color:red'>*</em><em>"+chineseType+"</em></th>";
						}else{
							titleContent += "<th id='"+chosenType+"#"+type+"'>"+chineseType+"</th>";
						}
						
					}
				})
			});
			
			titleContent = "<tr id='headTr'><th><input id='cbxCheckAll' type='checkbox' onclick='operaAllCbx(this)' />全选</th>"+titleContent+"</tr>";
			
			$("#showContentTable").html(titleContent);
			
			$.each(data.typesMap,function(key,value){
				$("th[id='"+chosenType+"#"+key+"']").attr("name",value);
			})	
		},'json');
		
		if($(me).parent().parent().parent().hasClass("specialty")){
			$(me).parent().parent().parent().parent().slideToggle("fast");
		}else if($(me).parent().parent().parent().hasClass("letter")){
			$(me).parent().parent().parent().parent().parent().slideToggle("fast");
		}else if($(me).parent().parent().parent().hasClass("dictionary")){
			$(me).parent().parent().parent().slideToggle("fast");
		}
		
		showProgress();
		$(".search_box_alert").slideToggle("fast");
	}
	
	//双击转向资源维护页面
	function fowardToOperPage(me){
		if($(me).attr("name")!=undefined && $(me).attr("name")!=""){
			var currentEntityId = "";
			var currentEntityType = "";
			var name=$(me).attr("name");
			if(name!="") {
				currentEntityId = name.substring(name.indexOf("#")+1);
			}  
			if(name!="") {
				currentEntityType = name.substring(0,name.indexOf("#"));
			}
			var linkType="";
			var parentType="";
			$("#selResChosenType option").each(function(){
				if($(this).attr("value")!="请选择"){
					type = $(this).attr("value")+"";
					type = type.substring(type.indexOf("_")+1,type.length);
					if(type=="link"){
						linkType="link";
					}else{
						parentType="parent";
					}			
				}
			})
			if(linkType=="link" && parentType==""){
				var params = {id:currentEntityId,infoName:currentEntityType,chooseType:"view"}
				//获取逻辑网属性维护页面信息
				$.post("../logicalres/logicalresViewRoAddAction",params,function(data){
					$("#logicalresContentDiv").html(data);
					$("#confirm_div").show();
					bodyOnLoad(currentEntityId,currentEntityType,"view");
				});
			}else{
				//打开节点编辑页面
				var areaId = $("#areaId").val();
				open("getPhysicalresForOperaAction?currentEntityId="+currentEntityId+"&currentEntityType="+currentEntityType+"&areaId="+areaId+"&modelType=view");	
			}
		}
	}
	
	//批量操作，全选，全反选操作
	function operaAllCbx(me) {
		$("#showContentTable input[name='cbxResEntity']").each(function(){
			//操作选中或反选，并显示相应的颜色
			if($(me).attr("checked") == "checked") {
				$(this).attr("checked", true);
				//$("#showContentTable tr").attr("class", "chosenClass");
			} else {
				$(this).attr("checked", false);
				//$("#showContentTable tr").attr("class", "");
			}
		});
	}
	/*逻辑网信息*/
	function bodyOnLoad(id,infoName,chooseType){
		if(chooseType == "view"){
			$('#litab1').click();
		}else if(chooseType == "Preserve"){
			$('#litab2').click();
		}else if(chooseType == "add"){
			$('#litab2').click();
		}
	}
	//逻辑网显示
	function showLogicalres(id,infoName,chooseType){ 
			$.post("../logicalres/logicalresViewRoAddAction",{id:id,infoName:infoName,chooseType:chooseType},function(data){
				$("#rightInformation").html(data);
				//$("#imgRight").attr("src","image/hide.png");
				//$("#rightInformation").show();
				if($("#image_plus_1")){
					$("#image_plus_1").click();
				}
				if($("#image_plus_2")){
					$("#image_plus_2").click();
				}
				
				bodyOnLoad(id,infoName,chooseType);
				
			});
		}
	//直接录入增加资源
	function addPhysicalresDirectly(me){
		$("#tishiText").html("数据保存中......");
		$("#loading_div").show();
		$("#black").show();
		var params="";
		var indexFlag = 1;
		var size = $(me).parent().parent().children().size();
		var addedResEntityType = "";
		var  mustFillFlag=true;
		$(me).parent().parent().children().each(function(){
			if($(this).attr("id")!=undefined && $(this).attr("id")!=""  ){
				var name = $(this).children().eq(0).attr("name");
				if(addedResEntityType==""){
					addedResEntityType = name.substring(0,name.indexOf("#"));
				}
				var value = $(this).children().eq(0).val();
				if($(this).hasClass("mustFill")){
					if($.trim(value)==""){
						mustFillFlag=false;
					}
				}
				if(value!=""){
					if(indexFlag==1){
						params += "'"+name+"':'"+value+"'";
					}else{
						params += ",'"+name+"':'"+value+"'";
					}
				}	
				indexFlag++;
			}
			
		})
		if(!mustFillFlag){
			alert("存在带*字段没有录入值");
			$("#loading_div").hide();
			$("#black").hide();
			$("#tishiText").html("数据导入中......");
			return false;
		}
		var areaId = $("#areaId").val();
		
		if(addedResEntityType=="Station"||addedResEntityType.indexOf("BaseStation")>=0){
			var sameflag=false;
			var cName=$("input[name='"+addedResEntityType+"#name']").val();
			var param = {addedResEntityType:addedResEntityType,areaId:areaId,cName:cName};
			$.ajax({
				url:"hasStationResourceRecordAction",
				async:false,
				data:param,
				dataType:'json',
				type:'post',
				success:function(data){
					if(data!="0"){
						alert("已存在同名资源，增加保存失败");
						sameflag=true;
					}
			    }
			})
			if(sameflag){
				$("#loading_div").hide();
				$("#black").hide();
				$("#tishiText").html("数据导入中......");
				return false;
			}
		}
		if(params==""){
			params = params+"'addedResEntityType':'"+addedResEntityType+"'";
		}else{
			params = params+",'addedResEntityType':'"+addedResEntityType+"'";
		}
		
		var linkType="";
		if($("#txtExactContent").val()!=""){
			var type="";
			var chinese="";
			if($("#selResChosenType option:selected").attr("value")!="请选择"){
				type = $("#selResChosenType option:selected").attr("value")+"";
				chinese = $("#selResChosenType option:selected").html();
				type = type.substring(type.indexOf("_")+1,type.length);
				if(type=="link"){
					linkType="link";
				}else{
					linkType="parent";
				}			
			}
			var addedResParentEntityType=$("#hidAssEntityType").val();
			var addedResParentEntityId=$("#hidAssEntityId").val();
			if(params==""){
			params=params+"'addedResParentEntityType':'"+addedResParentEntityType+"','addedResParentEntityId':'"+addedResParentEntityId+"','linkType':'"+linkType+"'";		
			}else{
			params=params+",'addedResParentEntityType':'"+addedResParentEntityType+"','addedResParentEntityId':'"+addedResParentEntityId+"','linkType':'"+linkType+"'";		
			}
			
			if($("#hasWarn").val()==""){
				if(!confirm("是否把本资源添加到　"+chinese+"："+$("#txtExactContent").val()+"　下")){
					$("#loading_div").hide();
					$("#black").hide();
					$("#tishiText").html("数据导入中......");
					return;
				}else{
					$("#hasWarn").val("hasWarn");
				}
				
			}
			
		}else{
			if(!confirm("本资源将不关联到任何资源下，是否继续")){
				$("#loading_div").hide();
				$("#black").hide();
				return;
			}
		}
		var op_scene = $("#op_scene_text").val();
		var op_cause = $("#op_cause_text").val();
		params=params+",'op_scene':'"+op_scene+"','op_cause':'"+op_cause+"'";		
		params ="{"+params+"}"
		params = eval("("+params+")");
		$.post('addPhysicalresDirectlyAction',params,function(data){
			if(data!=null&&data!=""){
				$(me).parent().parent().attr("id","saveTr");
				//$(me).parent().parent().attr("onclick","chooseTr(this);");
				//$(me).parent().parent().attr("ondblclick","fowardToOperPage(this);");
				$(me).parent().parent().attr("name",addedResEntityType+"#"+data);
				$(me).parent().parent().children().each(function(){
					if($(this).attr("id")!=undefined && $(this).attr("id")!="" ){
						var value = $(this).children().eq(0).val();
						$(this).html(value);
					}else{
						$(this).html("<input name='cbxResEntity' type='checkbox' />");
						$("<input type='hidden' value='id_"+data+"' /><input type='hidden' value='aeType_"+addedResEntityType+"' />").insertAfter($(this));
						$(this).attr("name",addedResEntityType+"#"+data);
					}
				})
				var content="";
				$("tr[id='headTr'] th").each(function(){
					if($(this).attr("id")!=undefined && $(this).attr("id")!=""){
						var attrType = $(this).attr('name');
						var str="";
						var dateString="";
						if(attrType.indexOf("Integer")>=0 || attrType.indexOf("Long")>=0 ){
							var c=$(this).html()+"";
							if(c.indexOf("是否")>=0){
								str='请填写：0或者1，0表示“否”，1表示“是”';
							}else{
								str='请填写：整数，如“12”';
							}
							
						}else if(attrType.indexOf("Float")>=0 || attrType.indexOf("Double")>=0){
							str='请填写：数值，如“1232.23”，“12.03”';
						}else if(attrType.indexOf("Date")>=0){
							str = '请填写：日期时间，如“2012-10-09”，“2012-10-09 11:02:30”';
							dateString = "dateString" ;
						}else{
							str='请填写：字符串';
						}
						if($(this).hasClass("mustFill")){
							if(dateString!=""){
								content +="<td id='"+$(this).attr('id')+"' class='mustFill' name='"+$(this).attr('name')+"' ><input type='text'  name='"+$(this).attr('id')+"' title='"+str+"'  /></td>";			
							}else{
								content +="<td id='"+$(this).attr('id')+"' class='mustFill' name='"+$(this).attr('name')+"' ><input type='text' name='"+$(this).attr('id')+"' title='"+str+"'/></td>";							
							}
						}else{
							if(dateString!=""){
								content +="<td id='"+$(this).attr('id')+"'  name='"+$(this).attr('name')+"' ><input type='text'  name='"+$(this).attr('id')+"' title='"+str+"' /></td>";			
							}else{
								content +="<td id='"+$(this).attr('id')+"'  name='"+$(this).attr('name')+"' ><input type='text' name='"+$(this).attr('id')+"' title='"+str+"'/></td>";							
							}	
						}
					}
				})
				content = "<tr  id='addedTr' onclick='chooseTr(this);' ondblclick='fowardToOperPage(this);'><td style='color:red'>*<br><input type='button' value='保存' style='color:red' onclick='addPhysicalresDirectly(this);'/><input type='button' value='取消' style='color:red' onclick='cancelAddPhysicalresDirectly(this);'/></td>"+content+"</tr>";
				$(content).insertAfter($("tr[id='headTr']"));
				$("#loading_div").hide();
				$("#black").hide();
				$("#tishiText").html("数据导入中......");
			}else{
				alert("保存资源失败");
				$("#loading_div").hide();
				$("#black").hide();
				$("#tishiText").html("数据导入中......");
			}
		},'json')
	}
	/**
	取消录入
	**/
	function cancelAddPhysicalresDirectly(me){
		$(me).parent().parent().children().each(function(){
			$(this).children().eq(0).val("");
		});
	}
	//浮点型判断
 function isDecimal(str) {
   return /[0-9]*(\\.?)[0-9]*/.test(str);
 }
 //整型判断
function isInteger(str){
  return /[0-9]+/.test(str);
}
	
	
	function getSheet(){
		$("#importForm").attr("action","getFileSheetAction");
		$("#importForm").submit();
		showProgress();
	}
	
	/**
	 * 下拉框点击option处理
	 * @param {Object} id
	 */
	function optionOnclick(id){
		$(id).parent().prev().val($(id).text());
	}
	/**
	 *  属性为整型时下拉框点击option处理
	 * @param {Object} id
	 */
	function integerOptionOnclick(id){
		$(id).parent().next().val($(id).text());

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
							if($("#logicalresTreeRootName").parent().children().eq(0).attr("onclick")=='showChildTypeMasg(this);'){
								showChildEntityMasg(typeImg);
							}else{
								showChildEntityMasgByLINK(typeImg);
							}
							
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
							if($("#logicalresTreeRootName").parent().children().eq(0).attr("onclick")=='showChildTypeMasg(this);'){
								showChildTypeMasg(eImg);
							}else{
								showChildTypeMasgByLINK(eImg);
							}
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
												if($("#logicalresTreeRootName").parent().children().eq(0).attr("onclick")=='showChildTypeMasg(this);'){
													getChildEntityMasgForSearch(typeImg);
												}else{
													getChildEntityMasgByLINKForSearch(typeImg);
												}												
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
					var chosenType=isRadioType.substring(0,isRadioType.lastIndexOf(","));
					var areaId = $("#areaId").val(); //区域id
					searchResouceRecursionForLogicalres("selectResChosenType",chosenType,areaId,kewWords);
					
					
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
	//关联搜索切换
	function showSearchTab(){
		var check1 = $("#allCheckBox").attr("checked");
		if(check1=='checked'){
			$("li[id='allLi']").hide();
			$("li[id='partLi']").show();
		}else{
			$("li[id='allLi']").show();
			$("li[id='partLi']").hide();
		}
		$(".searchtab").show();
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
	/**隐藏与显示异常textarea
	**/
	function showError(me){
		if($("#errorMessage").css("display")=="none"){
			$("#errorMessage").show();
			$(me).html("[-]详细");
		}else{
			$("#errorMessage").hide();
			$(me).html("[+]详细");
		}
	}
	/**判断是否显示异常textarea*/
	function isShowError(me){
		if($("#errorMessage").css("display")=="none"){
			$("#errorMessage").show();
			$(me).html("[-]详细");
		}
	
	}
	//显示资源基本信息
	function showInfo(me,event){
		var id = $(me).next().val();
		var type = $(me).next().next().val();
		var left=event.clientX+320;
 		var top=event.clientY+document.body.scrollTop;
		
		var params={"currentEntityType":type,"currentEntityId":id,"loadBasicPage":"loadBasicPage"};
		$.post("../physicalres/getPhysicalresAction",params,function(data){
			$("#resourceInfo").html(data);
			$("#resourceInfo").css("left",left).css("top",top);
			$("#resourceInfo").show();
		});
	}
	/**隐藏基本信息*/
	function hideInfo(){
		$("#resourceInfo").hide();
	}
	
	/*资源全路径*/
	function showParResource(id,type,event){
		var params={"currentEntityType":type,"currentEntityId":id};
		var left=event.clientX+320;
 		var top=event.clientY+document.body.scrollTop;
		$.post("../physicalres/getParentResourceListAction",params,function(data){
			if(data!="" && data!=null ){
				var content="";
				data = eval("("+data+")");
				$.each(data,function(index,value){
					if(value.name==undefined){
						if(index==0){
							content = "<tr><td style='text-align:right'><img src='image/arrows.png' style='position:relative;top:-2px;vertical-align:middle'/>"+value.chineseType+":</td><td style='text-align:center'>"+value.label+"</td></tr>"+content;
						}else{
							content = "<tr><td style='text-align:right'>"+value.chineseType+":</td><td style='text-align:center'>"+value.label+"</td></tr><tr><td></td><td style='text-align:center'>↓</td></tr>"+content;
						}
						
					}else{
						if(index==0){
							content = "<tr><td style='text-align:right'><img src='image/arrows.png' style='position:relative;top:-2px;vertical-align:middle'/><span style=''>"+value.chineseType+":</span></td><td style='text-align:center'>"+value.name+"</td></tr>"+content;
						}else{
							content = "<tr><td style='text-align:right'>"+value.chineseType+":</td><td style='text-align:center'>"+value.name+"</td></tr><tr><td></td><td style='text-align:center'>↓</td></tr>"+content;
						}			
					}
				})
				$("#parResourceInfo table").html(content);
			}else{
				$("#parResourceInfo table").html("此资源无上级。");
			}
			$("#parResourceInfo").css("left",left).css("top",top);
			$("#parResourceInfo").show();
		});
	}
	/**隐藏全路径*/
	function hideParInfo(){
		$("#parResourceInfo").hide();
	}
	function hideTab(event){
		oElement = document.elementFromPoint(event.pageX,event.pageY);
		if($(oElement).attr("class")!="searchtab" && $(oElement).attr("id")!="searchSpan" && $(oElement).attr("id")!="txtSearch"){
			$("div[class='searchtab']").hide();
		}
		if($(oElement).attr("class")!="logicalressearchtab" && $(oElement).attr("id")!="searchSpan" && $(oElement).attr("id")!="logicalresTxtSearch"){
			$("div[class='logicalressearchtab']").hide();
		}
	}
	
	function showTip(me){
		$(me).next().css("display","inline-block");
	}
	function hideTip(me){
		$(me).next().css("display","none");
	}
	
	var stime;//settimeout
	function showImportTip(me){//显示导入数量要求提示
		var curId=$(me).attr("id");
		stime=setTimeout("isShowImportTip('#"+curId+"');",2000);	
	}
	function isShowImportTip(me){//settimeout调用
		$(me).hide();
		$(me).next().show();	
	}
	function hideImportTip(){//清除settimeout
		clearTimeout(stime);
	}
	//隐藏导入提示
	function hideTooltipDialog(){
		$("#loading_div").hide();
		$("#black").hide();
		$("#tooltip_dialog").hide();
	}
	//继续导入
	function continueImport(){
		var data = $("#importData").data("importData");
		$("#totalCount").html(data.contentMapList.length);
		$("#realCount").html(importAmmount);
		$("#currentImport").html("已导入成功0个");
		$("#importResultDiv").show();
		var error = $("#errorMessage").html();
		if(error!=""){
			showError($("#errorLink").children().eq(0));
		}
		escEnable=true;
		importMaps = "["+importMaps.substring(1,importMaps.length)+"]";
		importRecord = "["+importRecord.substring(1,importRecord.length)+"]";
		rowNums = "["+rowNums.substring(1,rowNums.length)+"]";
		assRecord = "["+assRecord.substring(1,assRecord.length)+"]";
		var assType = data.assType;
		var assEntityType = data.assEntityType;
		var opScene = $("#op_scene_text").val();
		var opCause = $("#op_cause_text").val();
		var rdoAssModel = $("input[name='rdoAssModel']:checked").val();
		var params={rdoAssModel:rdoAssModel,importModel:importModel,opScene:opScene,opCause:opCause,importMaps:importMaps,importRecord:importRecord,rowNums:rowNums,assRecord:assRecord,assType:assType,assEntityType:assEntityType,importEntityType:importEntityType};
		var url = "importAddResourceAction";
	/*	if("updateAdd"==importModel){
			url = "importUpdateResourceAction";
		}else if("deleteAdd"==importModel){
			url = "importDeleteAddResourceAction";
		}*/
		var iter =  setInterval(function(){
			$.post("getCurrentImportCoutAction",function(data){
				if(data!=null){
					$("#currentImport").html(data);
				}
			},'json');
		},5000)
		$.post(url,params,function(result){
			if(result.resultList!=null && result.resultList!="" && result.resultList!=""){
				var allContent = "";
				$.each(result.resultList,function(k,v){
					//获取一个entity
					var content = "";
				   $.each(data.contentMapList[v.row], function(key, value){
						if(!(key == "id" || key == "aeType" || key == "_entityId" || key == "_entityType")) {
							content += "<td>" + value + "</td>";
						} else if(key == "id" || key == "aeType") {
							if(key=="id"){
								content += "<input type='hidden' value='"+key+"_"+v.id+"' />";
							}else{
								content += "<input type='hidden' value='"+key+"_"+value+"' />";
							}	
						}
					});
					if("no"==v.associate){
						content = "<tr style='color:red' onclick='chooseTr(this);' ondblclick='fowardToOperPage(this);' name='"+importEntityType+"#"+v.id+"'><td><input name='cbxResEntity' type='checkbox' /></td>"+content+"</tr>";
					}else{
						content = "<tr  onclick='chooseTr(this);' ondblclick='fowardToOperPage(this);' name='"+importEntityType+"#"+v.id+"'><td><input name='cbxResEntity' type='checkbox' /></td>"+content+"</tr>";
					}
					allContent +=content;
				})
                   $("#showContentTable").append(allContent);
                  
			}
			if(result.resultMsg!=null && result.resultMsg!="" && result.resultMsg!=""){
				$("#currentImport").html(result.resultMsg);
			}
			$("#loading_div").hide();
			$("#black").hide();
			clearInterval(iter);
			$("#importResultDiv").hide();
			$("#lastResultDiv").html("导入结果：共"+$("#totalCount").html()+"个资源，实际要导入"+$("#realCount").html()+"个资源，"+$("#currentImport").html());

		},'json')			
	}
	//模拟select下拉框
	function liMouseOver(me){
		$(me).siblings().css("background","none");
		$(me).css("background","#4C86D6");
	}
	
	function liMouseClick(me){
		var parId = $(me).parent().attr("id");
		var curId="";
		if(parId=="attributesUl"){
			curId = $(me).attr("id");
			$("select[id='linkAttributes']").val(curId);
		}else if(parId=="exactMatchAttributesUl"){
			curId = $(me).attr("id");
			$("select[id='selAttrExactMatch']").val(curId);
		}else if(parId=="indistinctMatchAttributesUl"){
			curId = $(me).attr("id");
			$("select[id='selAttrIndistinctMatch']").val(curId);
		}
		showProgress();
		$(me).parent().parent().hide();
	}		
	
	function selectClick(me){
		var curId = $(me).attr("id");
		if(curId=="linkAttributes"){
			$("#attributesUl").parent().hide();
		}else if(curId=="selAttrExactMatch"){
			$("#exactMatchAttributesUl").parent().hide();
		}else if(curId=="selAttrIndistinctMatch"){
			$("#indistinctMatchAttributesUl").parent().hide();
		}
	}
	
	function resourceImport(){
		$("#importForm").submit();	
	}
</script>
</head>

<body>
<input type="hidden" value="" id='hasWarn'/><%-- 判断直接录入资源是否已提示过关联资源 --%>
<input type="hidden" id="areaId" value="${request.areaId}"/><%-- 区域id --%>
<%-- 用来逻辑网保存树内容查询信息 --%>
<input type="hidden" id="logicalresChosenParentEntityType" value=""/>
<input type="hidden" id="logicalresChosenParentEntityId" value=""/>
<input type="hidden" id="logicalresAddedResParentEntityType" value=""/>
<input type="hidden" id="logicalresAddedResParentEntityId" value=""/>
<input type="hidden" id="logicalresAddedResEntityType" value=""/>
<%-- 点击类型时隐藏域值 --%>
					<input id="chosenEntityType" name="chosenEntityType" type="hidden" />
					<input id="chosenEntityId" name="chosenEntityId" type="hidden" />
					<input id="addedResParentEntityType" name="addedResParentEntityType" type="hidden" />
					<input id="addedResParentEntityId" name="addedResParentEntityId" type="hidden" />
					<input id="addedResEntityType" name="addedResEntityType" type="hidden" />
	<%-- 信息提示层 --%>
			<div id="confirm_div" style="display:none;">
		    	<div style="z-index:5; position:fixed; left:0px;top:0px; height:100%; width:100%;background:#eee;filter:alpha(opacity=50);-moz-opacity:0.5;-khtml-opacity: 0.5;opacity:0.5;"></div>
		    	<form id="operaForm" action="createORUpateInfoAction" method="post" target="stay">
			    	<div id="confirm_div_container">
			    		<div id="confirm_div_title" onmousemove="Move_obj('confirm_div_title')">
			    			<span style="float:right;"><a id="btnCloseLogicalresDiv" style="cursor:pointer;padding:2px 0px 5px 8px;display:block; height:16px; width:16px; background:#eee; border:1px solid #666;">X</a></span>
			    		</div>
				    	<div id="logicalresContentDiv" >
				        	
				        </div>
				    </div>
		        </form>
		  	</div>
	<%-- 用来保存树内容的缓存控件 --%>
	<input type="hidden" id="hidTypeFlush" />
	<input type="hidden" id="hidEntityFlush" />
	<input type="hidden" id="importData" />
	<div id="up">
		
    	<div id="up_top" style="min-width: 1210px;" title="导入设置：资源类型、资源文件，新增资源或更新资源模式，隶属或关联关系">
        	<span id="importset">导入设置▼</span>
        	
        </div>
        <span style="color:red;margin-left:133px;top:3px;position:absolute;z-index:2"><input type="checkbox" id="highModel">高级模式</span>
    <form id="importForm" action="resourceImportAction" method="post" enctype="multipart/form-data">
       <input type="hidden" name="areaId" value="${request.areaId}"/><%-- 区域id --%>
       
        <div id="up_down" style="height:337px;min-width: 1215px;">
        	<div id="top_tool_bar" style="margin-left:7px">
	      		<%-- 区域选择 --%>
                            	<input type="hidden" id="select_area_id" />
				            	<div class="toolBar_area" style="white-space:nowrap;">选择区域：
				            		<em id="" class="city_title">
				            			<a title="选择城市" class="city_expand city_show" id="city_select" href="javascript:void(0)">请选择</a>
										<a title="选择城区" class="city_expand district_show" style="display:none;" id="district_select" href="javascript:void(0)">请选择</a>
										<a title="选择街道" class="city_expand street_show" style="display:none;" id="street_select" href="javascript:void(0)">请选择</a>
				            		</em>
				                </div>
				                <div class="map_city" style="z-index:10001;top:42px">
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
								<div class="map_district" style="z-index:10001;top:42px">
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
        	<div id="down_left" style="width:540px; float:left;position: relative; z-index: 10000;">
        	<fieldset style="margin-left:5px">
                	<legend>
                    	<span>导入资源选择</span>
                    </legend>
            	<table style="margin:-4px 0px;width:100%;">
                	<tr>
                    	<td ><span>资源类型：</span></td>
                    	<%-- <td>
                    	<em style="color:blue;text-decoration: underline" id="choosedResourceType">未设置</em>&nbsp;<em style="color:red;">*</em></td>
                       		&nbsp;&nbsp; --%>
                   <%-- 	<td>
							<span class="filter_specialty filter_dialog_but"><em class="but_text">按专业</em><em class="but_icon"></em></span>
							<div class="filter_dialog clearfix filter_specialty_info" >
								<div class="filter_dialog_info specialty filter_active" id="ResGroup_4_Geographical">
									<h2 id="ResGroup_4_Geographical">地理位置类</h2>
									<ul class="filter_main" style="display:block;" id="ResGroup_4_Geographical">											
									</ul>
								</div>
								<div class="filter_dialog_info specialty" id="ResGroup_4_Transmission">
									<h2 id="ResGroup_4_Transmission">传输</h2>
									<ul class="filter_main" id="ResGroup_4_Transmission">	
									</ul>
								</div>
								<div class="filter_dialog_info specialty" id="ResGroup_4_Power">
									<h2 id="ResGroup_4_Power">动力</h2>
									<ul class="filter_main" id="ResGroup_4_Power">
									</ul>
								</div>
								<div class="filter_dialog_info specialty" id="ResGroup_4_Wireless">
									<h2 id="ResGroup_4_Wireless">无线</h2>
									<ul class="filter_main" id="ResGroup_4_Wireless">						
									</ul>
								</div>
								<div class="filter_dialog_info specialty" id="ResGroup_4_IndoorCover">
									<h2 id="ResGroup_4_IndoorCover">室分</h2>
									<ul class="filter_main" id="ResGroup_4_IndoorCover">
									</ul>
								</div>
								<div class="filter_dialog_info specialty" id="ResGroup_4_WLAN">
									<h2 id="ResGroup_4_WLAN">WLAN</h2>
									<ul class="filter_main" id="ResGroup_4_WLAN">
									</ul>
								</div>
								<div class="filter_dialog_info specialty" id="ResGroup_4_EvironmentAndMonitoring">
									<h2 id="ResGroup_4_EvironmentAndMonitoring">环境监控</h2>
									<ul class="filter_main" id="ResGroup_4_EvironmentAndMonitoring">
									</ul>
								</div>
								<div class="filter_dialog_info specialty" id="ResGroup_4_PipeAndCable">
									<h2 id="ResGroup_4_PipeAndCable">管线</h2>
									<ul class="filter_main" id="ResGroup_4_PipeAndCable">
									</ul>
								</div>
								<div class="filter_dialog_info specialty" id="ResGroup_4_Equipment">
									<h2 id="ResGroup_4_Equipment">设备级</h2>
									<ul class="filter_main" id="ResGroup_4_Equipment">
									</ul>
								</div>
								<div class="filter_dialog_info specialty" id="ResGroup_4_DeviceBoard">
									<h2 id="ResGroup_4_DeviceBoard">板件级</h2>
									<ul class="filter_main" id="ResGroup_4_DeviceBoard">
									</ul>
								</div>
								<div class="filter_dialog_info specialty" id="ResGroup_4_Cabinet">
									<h2 id="ResGroup_4_Cabinet">机架/箱/柜</h2>
									<ul class="filter_main" id="ResGroup_4_Cabinet">
									</ul>
								</div>
								<div class="filter_dialog_info specialty" id="ResGroup_4_ComponentAndAccessory">
									<h2 id="ResGroup_4_ComponentAndAccessory">零部件/耗材</h2>
									<ul class="filter_main" id="ResGroup_4_ComponentAndAccessory">
									</ul>
								</div>
							</div>
						</td><td>
                        
							<span class="filter_letter filter_dialog_but"><em class="but_text">按字母</em><em class="but_icon"></em></span>
							<div class="filter_dialog clearfix filter_letter_info" >
								<div class="filter_letter_info_left clearfix">
									<div class="filter_dialog_info letter filter_active" id="A">
										<h2 id="A">A</h2>
										<ul class="filter_main" style="display:block;" id="A">	
										</ul>
									</div>
									<div class="filter_dialog_info letter" id="B">
										<h2 id="B">B</h2>
										<ul class="filter_main" id="B">
										</ul>
									</div>
									<div class="filter_dialog_info letter" id="C">
										<h2 id="C">C</h2>
										<ul class="filter_main" id="C">
										</ul>
									</div>
									<div class="filter_dialog_info letter" id="D">
										<h2 id="D">D</h2>
										<ul class="filter_main" id="D">
										</ul>
									</div>
									<div class="filter_dialog_info letter" id="E">
										<h2 id="E"> E</h2>
										<ul class="filter_main" id="E">
										</ul>
									</div>
									<div class="filter_dialog_info letter" id="F">
										<h2 id="F">F</h2>
										<ul class="filter_main" id="F">
										</ul>
									</div>
									<div class="filter_dialog_info letter" id="G">
										<h2 id="G">G</h2>
										<ul class="filter_main" id="G">
										</ul>
									</div>
									<div class="filter_dialog_info letter" id="H">
										<h2 id="H">H</h2>
										<ul class="filter_main" id="H">
										</ul>
									</div>
									<div class="filter_dialog_info letter" id="I">
										<h2 id="I">I</h2>
										<ul class="filter_main" id="I">
										</ul>
									</div>
									<div class="filter_dialog_info letter" id="J">
										<h2 id="J">J</h2>
										<ul class="filter_main" id="J">
										</ul>
									</div>
									<div class="filter_dialog_info letter" id="K">
										<h2 id="K">K</h2>
										<ul class="filter_main" id="K">
										</ul>
									</div>
									<div class="filter_dialog_info letter" id="L">
										<h2 id="L">L</h2>
										<ul class="filter_main" id="L">
										</ul>
									</div>
									<div class="filter_dialog_info letter" id="M">
										<h2 id="M">M</h2>
										<ul class="filter_main letter" id="M">
										</ul>
									</div>
									<div class="filter_dialog_info letter" id="N">
										<h2 id="N">N</h2>
										<ul class="filter_main" id="N">
										</ul>
									</div>
									<div class="filter_dialog_info letter" id="O">
										<h2 id="O">O</h2>
										<ul class="filter_main" id="O">
										</ul>
									</div>
									<div class="filter_dialog_info letter" id="P">
										<h2 id="P">P</h2>
										<ul class="filter_main" id="P">
										</ul>
									</div>
									<div class="filter_dialog_info letter" id="Q">
										<h2 id="Q">Q</h2>
										<ul class="filter_main" id="Q">
										</ul>
									</div>
									<div class="filter_dialog_info letter" id="R">
										<h2 id="R">R</h2>
										<ul class="filter_main" id="R">
										</ul>
									</div>
									<div class="filter_dialog_info letter" id="S">
										<h2 id="S">S</h2>
										<ul class="filter_main" id="S">
										</ul>
									</div>
									<div class="filter_dialog_info letter" id="T">
										<h2 id="T">T</h2>
										<ul class="filter_main" id="T">
										</ul>
									</div>
									<div class="filter_dialog_info letter" id="U">
										<h2 id="U">U</h2>
										<ul class="filter_main letter" id="U">
										</ul>
									</div>
									<div class="filter_dialog_info letter" id="V">
										<h2 id="V">V</h2>
										<ul class="filter_main" id="V">
										</ul>
									</div>
									<div class="filter_dialog_info letter" id="W">
										<h2 id="W">W</h2>
										<ul class="filter_main" id="W">
										</ul>
									</div>
									<div class="filter_dialog_info letter" id="X">
										<h2 id="X">X</h2>
										<ul class="filter_main" id="X">
										</ul>
									</div>
									<div class="filter_dialog_info letter" id="Y">
										<h2 id="Y">Y</h2>
										<ul class="filter_main" id="Y">
										</ul>
									</div>
									<div class="filter_dialog_info letter" id="Z">
										<h2 id="Z">Z</h2>
										<ul class="filter_main" id="Z">	
										</ul>
									</div>
									<div class="filter_dialog_info letter" style="width:64px;" id="ALL" >
										<h2 id="ALL" >全部类型</h2>
										<ul class="filter_main" id="ALL" style="height:376px;overflow: auto;">	
										</ul>
									</div>
								</div>
							</div>
						</td>
						<td>
							<span class="filter_dictionary filter_dialog_but"><em class="but_text">按字典顺序</em><em class="but_icon"></em></span>
							<div class="filter_dictionary_info dictionary" id="dictionary" >
								<ul id="dictionary">
									
								</ul>
							</div>
						
                      <%--    <td>
                        	<select class="input_select" id="selResType">
                            	<option value="请选择">请选择</option>
                            	<s:iterator value="allTypeMapList" id="typeMap">
                            		<option value="${typeMap.type}">${typeMap.chineseType}</option>
                            	</s:iterator>
                            </select><em style="color:red">*</em>
                            --%>
                       <%--  </td> --%>    	
                        <td colspan="5">
                        	 <div class="search_box_rightA">
                            <%--    <span>资源类型：</span> --%> 
                                <em class="area_content">
                                    <input type="text" value="" id="choosedResourceType" readonly/><a class="areaButton zy_show"></a>
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
                        </td>
                    </tr>
                    <tr>
                    	<td>
                           	<span style="color:#999">模板下载：</span>
                        </td>
                        <td colspan="5">
                        	<a id="downModule"  onclick="javascript:downResourceImportModule(this);"></a>
                      		<span id="selfImportSpan" style="display:none">| 自定义模板：<a id="downSelfModule"></a></span>
                        </td>
                    </tr>
                    <tr>
                    	<td>资源文件：</td>
                        <td colspan="5">
                        	<input id="importFile"  name="file" type="file" value="点击浏览" onchange="getSheet();"/> <em style="color:red">*</em>
                        	<select id="Sheet_Select" name="sheetIndex" style="display: none"></select>
                        </td>
                        
                    </tr> 
                     <tr>
                     	<td></td>
                        <td colspan="5" style="white-space:normal;">
                            <span style="color:#999;cursor:pointer" onmouseover="showImportTip(this);" onmouseout="hideImportTip();" id="importTip" >[+] 限制提示:资源数1000个以内</span>
                        	<span style="color:#999;display:none" >(提示:为避免超时，单个文件中的资源数建议控制在1000个以内，超过请拆分为多个文件、分多次导入。如果超过5000个，则系统会禁止导入。)</span>
                        </td>
						
                    </tr>
                    <tr>
                    <td>操作类型：</td>
                        <td colspan="5">
                        	<input class="input_radio" value="newAdd" type="radio" name="importModel" checked/>增加资源
                        	<span style="display:none;position:relative;" id="updateSpan" title="选择一个字段信息，对文件中的待导入资源，在平台资源库中做匹配查找，找到目标资源则进行更新，否则新建。" ><input class="input_radio" value="updateAdd" type="radio" name="importModel"  />更新资源</span>
                        	<span id="updateSelect" style="display:none;position:relative;" title="选择一个字段信息，对文件中的待导入资源，在平台资源库中做匹配查找，找到目标资源则进行更新，否则新建。" >按：<select class="input_select" id="linkAttributes" onclick='selectClick(this);' name="attributeChange" disabled="disabled" onchange="showProgress();"></select>
                        		<em class="wh_tip" onmouseover="showTip(this);" onmouseout="hideTip(this);">?</em>
	                            <div class="wh_img" style="display:none">
	                            	<em class="tip_sj"></em>
	                            	<img src="image/wh_img.png" />
	                            </div>
	                            <div></div>
	                            <div style="display:none;border: 1px solid;left: 24px;position: absolute;width: 76px;z-index:4; background: none repeat scroll 0 0 #F9F9F9;">
									<ul style="font-size:12px;cursor:default" id="attributesUl">
										
									</ul>
							    </div>
							    <div></div>
                        	</span>
                        	
                        	<%--<span style="display:none;position: relative;" id="deleteSpan" title="选择一个字段信息，对文件中的待导入资源，在平台资源库中做匹配查找，找到目标资源则先删除再重建，否则直接新建。"><input class="input_radio" value="deleteAdd" type="radio" name="importModel"  />删除重建&nbsp;</span>--%>
                        	<span id="deleteSelect" style="display:none;position: relative;" title="选择一个字段信息，对文件中的待导入资源，在平台资源库中做匹配查找，找到目标资源则先删除再重建，否则直接新建。"></span>     
                       		
                        </td>
                    </tr>
                </table>
                </fieldset>
            </div>
            
            <div id="down_right" style="width:410px; float:left; margin-left:8px;">
            	<fieldset>
                	<legend>
                    	<span><input id="chooseCheckBox" type="checkbox" checked  />上级资源关联设置：<em id="linkText" style="color:red">关联</em></span>
                    </legend>
                    <div id="down_right_main" >
                    	<%-- 隶属资源的类型和id，导入资源的类型 --%>
                    	<input type="hidden" id="hidAssEntityId" name="assEntityId" />
                        <input type="hidden" id="hidAssEntityType" name="assEntityType" />
                        <input type="hidden" id="hidImportEntityType" name="importEntityType" />
                        <table>
                        <tr>
                        <td>上级资源类型：</td>
                        <td style="text-align:left">
                        	<em id="selectResChosenTypeText" style="color:blue;">未选择导入资源类型</em>
                        	<select class="input_select" name="selectResChosenType" id="selResChosenType" style="display:none">
                            	<option value="请选择">请选择</option>
                            </select><em style="color:red">*</em>
                        </td>
                        
                        </tr>
                            <tr>
                                <td>
                                    <input class="input_radio" type="radio" value="exactContent" name="rdoAssModel" checked=""/>全部关联到本资源下
                                </td>
                                <td id="hideTd">
                                	<input class="input_text" type="text" id="txtExactContent" readonly="readonly" />
                                	&nbsp;<input class="input_button" type="button" id="btnShowAssResDiv" value="选择" />
                                	<div></div>
                                	<div id="assResDiv" style="background:#f9f9f9; border:1px solid #ccc; border-radius:5px; position:absolute; z-index:3; display:none;">	
                                        <div style="margin-bottom:4px;"><span id="searchSpan" onmouseout="hideTab(event);" ><input style="color:#999;" id="txtSearch" type="text"  value="资源分类搜索" />&nbsp;<input id="btnSearch" type="button" value="搜索"  onmouseover='showSearchTab();' onmouseout="hideTab(event);"/></span></div>
                                		
                                		<%-- 隶属内容选择层 --%>
							    		<div id="treeDiv" style="padding:0 8px;">
							    			<ul id="tree" style="width:250px;height:300px;overflow:auto;">
										        <li>
										        	<img src="image/plus.gif" onclick="showChildTypeMsg(this);" />
										        	<span id='treeRootName' style="cursor:pointer;" class="entityClass" onclick="chooseEntity(this);">${request.areaName}</span>
										        	<%-- 选进行区域id的hardcode --%>
										        	<input type="hidden" value="${request.areaId}" />
										        	<input type="hidden" value="Sys_Area" />
										        	<div style="padding-left:10px;display:none;"></div>
										        </li>
										    </ul>
							    		</div>
                                        <div id="text_div_bottom" style="text-align:center;">
                                        	<input id="btnDivChoose" class="input_button" type="button" value="选择" />&nbsp;
							    			<input id="btnDivCancel" class="input_button" type="button" value="取消" />
                                        </div>
                                        <div style="display:none" class="aetg1" >
										    <ul id="rds">
												
											</ul>
										</div>
                                        <div class='searchtab' style='display:none'  > 
								        	<ul>
								        	<li id='allLi' style='display:none'>
								        		&nbsp;<input type='checkbox' id='allCheckBox'  checked/> 资源分类搜索
								        	</li>
								        	<li  id='partLi'>
								        		&nbsp;<input type='checkbox' id='partCheckBox' /> 资源树搜索
								        	</li>
								        	</ul>
							        	</div>
                                    </div>
                                    <div></div>
                                </td>
                            </tr>
                            <tr >
                                <td>
                                    <input class="input_radio" value="exactMatch" type="radio" name="rdoAssModel" title="选择一个字段信息（目标关联资源的属性），对文件中的关联资源列（parent::***），在平台资源库中做匹配查找，找到则建立其与待导入资源的关联，否则不关联" /><span title="选择一个字段信息（目标关联资源的属性），对文件中的关联资源列（parent::***），在平台资源库中做匹配查找，找到则建立其与待导入资源的关联，否则不关联">在导入文件中指定上级资源（精确）</span>
                                </td>
                                <td style="display:none" id="hideTd">
                                    <select class="input_select" onclick='selectClick(this);' name="selectAttrExactMatch" id="selAttrExactMatch" title="选择一个字段信息（目标关联资源的属性），对文件中的关联资源列（parent::***），在平台资源库中做匹配查找，找到则建立其与待导入资源的关联，否则不关联" >
                                        <option value="请选择">请选择</option>
                                    </select>
                                    <em class="wh_tip" onmouseover="showTip(this);" onmouseout="hideTip(this);">?</em>
                                    <div class="wh_img" style="right:-320px;display:none">
                                    	<em class="tip_sj"></em>
                                    	<img src="image/wh_img2.png" />
                                    </div>
                                    <div></div>
                                    <div style="border: 1px solid;position: absolute;width: 76px;z-index:4; background: none repeat scroll 0 0 #F9F9F9;">
										<ul style="font-size:12px;cursor:default" id="exactMatchAttributesUl">
											
										</ul>
								    </div>
								     <div></div>
                                </td>
                            </tr>
                         <%--  <tr id="threeRow" style="display:none">
                                <td>
                                   <input class="input_radio" value="indistinctMatch" type="radio" name="rdoAssModel" title="选择一个字段信息（目标关联资源的属性），对文件中的关联资源列（parent::***），在平台资源库中做匹配查找，找到则建立其与待导入资源的关联，否则不关联" /><span title="选择一个字段信息（目标关联资源的属性），对文件中的关联资源列（parent::***），在平台资源库中做匹配查找，找到则建立其与待导入资源的关联，否则不关联">在导入文件中指定上级资源（模糊匹配）</span>
                                </td>
                                <td id="hideTd">
                                    <select class="input_select" onclick='selectClick(this);' name="selectAttrIndistinctMatch" id="selAttrIndistinctMatch" title="选择一个字段信息（目标关联资源的属性），对文件中的关联资源列（parent::***），在平台资源库中做匹配查找，找到则建立其与待导入资源的关联，否则不关联">
                                        <option value="请选择">请选择</option>
                                    </select>
                                    <em class="wh_tip" onmouseover="showTip(this);" onmouseout="hideTip(this);">?</em>
                                    <div class="wh_img" style="right:-320px;display:none">
                                    	<em class="tip_sj"></em>
                                    	<img src="image/wh_img2.png" />
                                    </div>
                                     <div></div>
                                    <div style="border: 1px solid;position: absolute;width: 76px;z-index:4; background: none repeat scroll 0 0 #F9F9F9;">
										<ul style="font-size:12px;cursor:default" id="indistinctMatchAttributesUl">
											
										</ul>
								    </div>
								     <div></div>
                                </td>
                            </tr> --%>  
                      <%--      <tr>
                                <td>
                                    <input class="input_radio" value="noContent" type="radio" name="rdoAssModel" />暂不设定，导入后再编辑
                                </td>
                                <td></td>
                            </tr>--%>
                        </table>
                    </div>
                </fieldset>
            </div>
            
             
            <div id="down_right_nodisplay" style="display:none;width:200px;float:left; margin-left:8px;">
            	<fieldset>
                	<legend>
                    	<span><input id="chooseCheckBox2" type="checkbox" />资源维护原因：</span>
                    </legend>
                    <div id="down_right_main2" style="display: none; margin-left: 8px; margin-top: 5px;">
                    	<h4>操作场景：</h4>
                    	<div style="position:relative;">
                    		<select style="width:160px;">
	                    		<option onclick="integerOptionOnclick(this);" value="资源管理维护" selected="selected">资源管理维护</option>
	                    		<option onclick="integerOptionOnclick(this);" value="日常服务维护">日常服务维护</option>
	                    		<option onclick="integerOptionOnclick(this);" value="故障维护">故障维护</option>
	                    		<option onclick="integerOptionOnclick(this);" value="工程与专项维护">工程与专项维护</option>
	                    	</select>
	                    	<input type="text" onfocus="optionOnclick(this);" id="op_scene_text" name="opScene" style="position:absolute; left:0px;width:140px;" value="资源管理维护"/>
                    	</div>
                    	
                    	<h4>操作原因：</h4>
                    	<div style="position:relative;">
                    		<select style="width:160px;">
	                    		<option onclick="integerOptionOnclick(this);" value="资源初始化导入" selected="selected">资源初始化导入</option>
	                    		<option onclick="integerOptionOnclick(this);" value="资源编辑维护">资源编辑维护</option>
	                    	</select>
	                    	<input type="text" onfocus="optionOnclick(this);" id="op_cause_text" name="opCause" style="position:absolute; left:0px;width:140px;" value="资源初始化导入"/>
                    	</div>
                    </div>
            	</fieldset>
            	
            </div>
             
            <%-- 导入结果显示的div --%>
            	<h4 class="progress_bar_top">步骤与进度提示：</h4>
            	<div class="progress_bar " id="progressDiv">
			    	<div style="width:773px; border:1px solid #ddd;border-right:none;">
			        	<span class="progress_bar_child1">
			            	1.选择资源类型、浏览资源文件、设置资源保存类型
			                <span class="progress_isnot">
			                	【<em class="red" id="progressEm1">未完成</em>】
			                </span>
			           	</span>
			        	<span class="progress_bar_child2">
			            	2.设置资源的隶属或归属关联，关联到现网中
			                <span class="progress_isnot">
			                	【<em class="red" id="progressEm2">未完成</em>】
			                </span>
			            </span>
			        </div>
			    </div>
                <div class="body_div_bottom" style="clear:both;">
                	<input style="background:none;width:80px;height:30px;border:1px solid wheat;border-radius:5px;" class="input_submit" type="button" id="daoru" value="导入" onclick="resourceImport();" disabled/>&nbsp;
                </div>
                <div style="padding-left:6px;position:absolute;z-index:3">
                 	<span id="lastResultDiv" >
	                	
                	</span> 
                	&nbsp;&nbsp;<span id='errorLink' style='display:none'><a onclick="showError(this);" style="color:blue;text-decoration:underline;cursor:pointer">[+]详细</a></span>
                	<div  ><textarea rows="5" style='height:100px;width:469px;display:none' id='errorMessage'  ></textarea></div>
                </div>
        </div>
    </form>
        
        
         
    </div>
    <form id="exportForm" method="post" action="exportBizunitRelationAction" >
    <input type="hidden" value="" name="chooseResEntityId"/>
    <input type="hidden" value="" name="chooseResEntityType"/>
    <input type="hidden" name="attrMap"/>
    <input type="hidden" name="exportModel"/>
	<div id="down">
    	<div id="down_top">
        	<span class="fl"><h4 id='hTitle'>已导入的资源</h4></span>
        	<span style=" margin-left:100px;color:blue"><input type="checkbox" id="directFill" disabled>直接录入（在下面表格中填写）</span>
            <span class="fr">
	            <input id="btnOperaPage" class="input_button" type="button" value="详情浏览" />
	            <input id="btnTurnToLogicalres" class="input_button" type="button" value="修改维护" />
	            <input id="btnDeleteRes" class="input_button" type="button" value="删除" />
	            <input id="btnExportRes" type="button" value="导出"  />
	            <input id="down_button" type="button"  value="▼">
				<em id="down_em" class="self_export">自定义导出</em>
            </span>
        </div>
        <div id="down_down" style="clear:both;">
            <table id="showContentTable" class="tc">
            	
            </table>
        </div>
    </div>
    </form>
    <div id="operaAssDiv" style="display:none;height:290px; z-index:10000; width:560px; background:#f9f9f9; border:1px solid #99BCE8; border-top:none;border-radius:5px; position:absolute;left:50%; margin-left:-280px; top:117px;">
    	<div class="body_div_top">关联选择确认</div>
        <div class="body_div_main">
        	<div style=" background:#eee;border-bottom:1px solid #ccc; padding:5px;margin-bottom:5px;">
            	根据导入规则，您正在导入的以下资源，存在多个可匹配的隶属/归属/关联资源，请选择确认。
            </div>
            <div style="text-align:-moz-center">
	           <table>
	           	
	               <tr>
	               		<td>导入资源类型：</td>
	                   <td><span id="txtImportResType" style="font-weight:bold"></span></td>
	                   <td>&nbsp;</td>
	                   <td>名称：</td>
	                   <td><span id="txtImportResName" style="color:blue"></span></td>
	               </tr>
	               <tr>
	               		<td>关联资源类型：</td>
	                   <td><span id="txtAssResType" style="font-weight:bold"></span></td>
	                   <td>&nbsp;</td>
	                   <td>匹配内容：</td>
	                   <td><span id="txtMatch" style="color:blue"></span></td>
	               </tr>
	           </table>
            </div>
            <div style=" width:96%; margin:10px; auto;">
            	<fieldset style="padding:10px;">
                	<legend><span></span>&nbsp;匹配结果&nbsp;</legend>
                    <ul>
                    
                    </ul>
                </fieldset>
            </div>
        </div>
        <div class="body_div_bottom" style="margin:6px 200px">
            <input id="btnAssAppChoose" class="input_button" type="button" value="建立关联" />&nbsp;
            <input id="btnNoAssApp" class="input_button" type="button" value="暂不关联" />
            <%-- 记录当前需要操作的导入资源的值 --%>
            <input type="hidden" id="hidImportEntityId" />
            <input type="hidden" id="hidImportEntityType" />
            <input type="hidden" id="hidAssType" />
        </div>
    </div>
    <div id='resourceInfo' style='display:none'></div>
    <div id='parResourceInfo' style='display:none'>
    <div style="text-align:right"><a href="javascript:hideParInfo();">关闭</a></div>
    <table></table>
    </div>
    <div id="updateEntityDiv" style="display:none;height:290px; z-index:10000; width:560px; background:#f9f9f9; border:1px solid #99BCE8; border-top:none;border-radius:5px; position:absolute;left:50%; margin-left:-280px; top:117px;">
    	<div class="body_div_top" id='headTitle'>资源更新（删除）确认</div>
        <div class="body_div_main">
        	<div id='titleInfo' style=" background:#eee;border-bottom:1px solid #ccc; padding:5px;margin-bottom:5px;">
            	根据导入规则，您正在更新的以下资源，存在多个可匹配的现有资源，请选择确认.
            </div>
            <div style="text-align:-moz-center">
	            <table>
	                <tr>
	                	<td >导入资源类型：</td>
	                    <td><span id="updateImportResType" style="font-weight:bold"></span></td>
	                    <td>&nbsp;</td>
	                    <td >名称：</td>
	                    <td><span id="updateImportResName" style="color:blue"></span></td>
	                    <td>&nbsp;</td>
	                    <td >匹配字段：</td>
	                    <td><span id="updateImportAttribute" style="color:blue"></span></td>
	                </tr>
	            </table>
            </div>
            <div style=" width:96%; margin:10px; auto;">
            	<fieldset style="padding:10px;">
                	<legend><span></span>&nbsp;匹配结果&nbsp;</legend>
                    <ul>
                    
                    </ul>
                </fieldset>
            </div>
        </div>
        <div class="body_div_bottom" style="margin:6px 228px">
            <input id="btnUpdateAppChoose" class="input_button" type="button" value="选择" />
             <input id="btnNoUpdateAppChoose" class="input_button" type="button" value="取消" />
            <%-- 记录当前需要操作的导入资源的值 --%>
            <input type="hidden" id="importEntityId" />
            <input type="hidden" id="importEntityType" />
        </div>
    </div>
    
    <%-- 遮盖层 --%>
    <div id="black" style="z-index:5;width:100%;position:absolute; top:0; left:0; height:100%; background:#FFF; display:none; z-index:2;filter:alpha(opacity=42); opacity:0.6;"></div>
    <%-- 数据加载提示层 --%>
   <div id="loading_div" style="z-index:2;display:none; position:absolute; width:450px; height:200px; left:50%;top:200px;margin-left:-225px; border-radius:3px;">
    	<div style="padding-top:20px; width:300px; margin:auto;text-align:center;">
        	<img src="image/loading_img.gif" />
        </div>
    	<div style="padding-top:5px;width:300px; margin:auto;text-align:center;" id='tishiText'>
        	数据导入中，请稍候...
        </div>
    </div>
    <%--<div class="insp_black" id="loading_div" style="display: none;">
        	<img src="image/loading_img.gif">
			<p id='tishiText'>数据处理中，请稍侯...</p>
    </div>--%>
    <div id="importResultDiv" style="z-index:2;display:none; position:absolute; width:450px;left:41%;top:48%;border-radius:3px;">
    	导入结果：共<span id="totalCount" >0</span>个资源，实际要导入<span id="realCount" >0</span>个资源，<span id="currentImport"></span>
   	</div>
    <div id="divDisplay" style="z-index:10; position:absolute; height:260px; width:280px; background-color: #fff; top: 20px;right:25px; border:2px solid #ccc;border-radius:4px; display: none">
       <table class="right_tab_table" style="margin: 0px; padding: 0px;">
       <tr><th class="tl" id="divDisplay_th"  onmouseover='Move_obj("divDisplay_th")' style="cursor: move;" colspan="2">端点设施及归属</th></tr>
       <tr><td style="width: 15px; padding: 0px;"><div style="margin-bottom:4px; float: left;"><span id="searchSpan" onmouseout="hideTab(event);"><input style="color:#999;" id="logicalresTxtSearch" type="text" style="width:150px;" value="资源分类搜索" onfocus="txtSearchFocus();" />&nbsp;<input id="logicalresBtnSearch" type="button" value="搜索" onclick="logicalresSearch();" onmouseover="showLogicalresSearchTab();" onmouseout="hideTab(event);"/></span></td></tr>
       </table>
       
       <ul id="tree-ul" style="margin-bottom:5px;float:left; border:1px solid #99BBE8;overflow:auto;width:99%;margin:0 auto; height:200px; margin: 0px; padding: 0px; top:1000px;">
        
    </ul>
    <div class="container-bottom" style=" border: 2px solid rgb(204, 204, 204); border-radius: 4px 4px 4px 4px; background-color: #FFFFFF;">
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
       </div>
    <%-- 用来逻辑网保存树内容 --%>
<input type="hidden" id="logicalresTreeId" value="<s:property value="areaId"/>"/>
<input type="hidden" id="logicalresTreeName" value="<s:property value="areaName"/>"/>
<input type="hidden" id="logicalresTreeType" value="Sys_Area"/>
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
	<%--自定义导出弹出框--%>
	<div class="dialog-window">
		<div id="black"></div>
		<div id="dialog_export" style="display:none;z-index: 10000;" class="export_div">
			<%--基本信息浮窗--%>
			<div class="dialog-header_export">
				<div class="dialog-title_export">资源导出：站址</div>
				<div class="dialog-tool_export">
				   <div class="dialog-tool-close_export dialog-hide_export" id="export_hide"></div>
				</div>
			</div>
			<div class="dialog_content_export">
            	<h4 class="dialog-content-title_export">请选择需要导出的信息内容:</h4>
                <table class="tb_1" id="exportAttrTable">
                	
                </table>
				<div class="but">
					<input type="button" id="self_export_submit" value="导出" style="background:#99BBE8;width:80px;height:30px;border:1px solid wheat;border-radius:5px;">
                    <input type="button" id="self_export_cancel" value="取消" class="dialog-hide">
				</div>
			</div>
		</div>
	</div>	
	<%-- 导入提示警告 --%>
	<div id="tooltip_dialog" class="dialog_box" style="display:none">
		<div class="dialog_header">
			<div class="dialog_title">导入提示信息</div>
			<div class="dialog_tool">
			   <div class="dialog_tool_close dialog_hide" onclick="hideTooltipDialog();"></div>
			</div>
		</div>
		<div class="dialog_content">
			<div class="tooltip_icon">
				<p class="tooltip_error"></p>
				<p>错误</p>
			</div>
			<div class="tooltip_info">
				<p>你本次要导入的资源为<em>2222</em>个，超过<em>2222</em>个。因为数据太多，可能会造成意外错误，系统将禁止操作。</p>
				<p>请拆分文件，分多次导入。建议单次导入的数量不超过1000个</p>
			</div>
			<div class="dialog_but">
				<button type="button" class="aui_state_highlight dialog_hide">确定</button>
			</div>
		</div>
	</div>
	
</body>
</html>
