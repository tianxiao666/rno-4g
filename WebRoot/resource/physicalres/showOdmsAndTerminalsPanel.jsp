<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<style>
#paneltable_box{width:900px;background:#bbb; padding:10px 0px; border:1px solid #000;margin:8px auto;}

#paneltable{ text-align:center;}
#paneltable_title{text-shadow: 0 0 4px #000000;color:#fff; line-height:24px;text-align:center; font-weight:bold;margin-bottom:5px;}
#messages{background:#fff; border-bottom:1px solid #ccc; padding:3px;}
.main-table1 .menuTd{width:20%;}
.main-table1 .highLighte{ background:#FFF; width:30%;}
.main-table1 span{
cursor:pointer;
}
.main-table1 tr td{padding:2px;}
</style>
<script type="text/javascript">
//浏览模式隐藏按钮
	if($("#view").hasClass("onslected")){
		$("#ldiv input[type='button']").not("#odmterminalButton").hide();
	}else{
		$("#ldiv input[type='button']").show();
	}
$(function(){
	//专项功能框隐藏或显示
			$(".container-tab1 fieldset legend :checkbox").each(function(){
			if($(this).attr("checked")=="checked"){
					$(this).parent().parent().removeClass("fieldset-hide");
					$(this).parent().parent().find(".fieldset_div").show();
					}
					else{
						$(this).parent().parent().addClass("fieldset-hide");
						$(this).parent().parent().find(".fieldset_div").hide();
						}
			$(this).click(function(){
				if($(this).attr("checked")=="checked"){
					$(this).parent().parent().removeClass("fieldset-hide");
					$(this).parent().parent().find(".fieldset_div").show();
					}
					else{
						$(this).parent().parent().addClass("fieldset-hide");
						$(this).parent().parent().find(".fieldset_div").hide();
						}
				})
			})
	//布局设置隐藏或显示
			$(".container-tab2 fieldset legend :checkbox").each(function(){
			if($(this).attr("checked")=="checked"){
					$(this).parent().parent().removeClass("fieldset-hide");
					$(this).parent().parent().find(".fieldset_div").show();
					}
					else{
						$(this).parent().parent().addClass("fieldset-hide");
						$(this).parent().parent().find(".fieldset_div").hide();
						}
			$(this).click(function(){
				if($(this).attr("checked")=="checked"){
					$(this).parent().parent().removeClass("fieldset-hide");
					$(this).parent().parent().find(".fieldset_div").show();
					}
					else{
						$(this).parent().parent().addClass("fieldset-hide");
						$(this).parent().parent().find(".fieldset_div").hide();
						}
				})
			})
			//保存取消按钮清空
			$("#editDiv").empty();
			//模块与端子信息获得
			getOdmAndTerminalLayOut()
		//	getOdmAndTerminalMessage();
			
			
})

function getOdmAndTerminalLayOut(){
		var currentEntityType = $("#currentResourceType").val();//当前类型
		var currentEntityId=$("#currentResourceId").val();//当前资源id
		var params = {currentEntityType:currentEntityType,currentEntityId:currentEntityId};
	 	$.post("getOdmandterminallayoutAction", params, function(data){	
	
			if(data.maxCount!=undefined){
	 			if(confirm("设备尚未设置面板布局，\n是否使用默认布局？")){
	 				$("#terminalrowammount").val(data.maxCount);
	 				$("#layoutCheckbox").attr("checked","checked");
					$("#layoutCheckbox").parent().parent().removeClass("fieldset-hide");
					$("#layoutCheckbox").parent().parent().find(".fieldset_div").show();
	 				var id = $("#layoutId").val();
					var odmcolorder =$("#odmcolorder").val();//模块上下顺序
					var odmroworder =$("#odmroworder").val();//模块左右顺序
					var odmrowammount =$("#odmrowammount").val();//每排模块数
					var terminalorder =$("#terminalorder").val();//端子左右排列顺序
					var terminalrowammount = $("#terminalrowammount").val();//每排端子数
					var firstodmorder=0;//模块先上下还是先左右排序 true为先上下 false为先左右
					if($("#firstodmorder").attr("checked")=="checked"){//checkbox控制
						firstodmorder=1;
					}
					var resourceid = $("#currentResourceId").val();
					var resourcetype = $("#currentResourceType").val();
					var params={
					"Odmterminallayout#id":id,
					"Odmterminallayout#odmupdownorder":odmcolorder,
					"Odmterminallayout#odmupdownorder":odmcolorder,
					"Odmterminallayout#odmleftrightorder":odmroworder,
					"Odmterminallayout#odmorderflag":firstodmorder,
					"Odmterminallayout#odmrowcount":odmrowammount,			
					"Odmterminallayout#terminalorder":terminalorder,
					"Odmterminallayout#terminalrowcount":terminalrowammount,
					"Odmterminallayout#resourceid":resourceid,
					"Odmterminallayout#resourcetype":resourcetype	
					}
					var url = "updateOdmandterminallayoutAction";
					$.post(url, params, function(data){
						if(data!="error"){
							$("#layoutId").val(data);
							showDifferentPanel();
						}else{
							alert("默认布局设置保存失败");
						}
					},'json');
	 			}else{
	 				$("#terminalrowammount").val(data.maxCount);
	 				$("#layoutCheckbox").attr("checked","checked");
					$("#layoutCheckbox").parent().parent().removeClass("fieldset-hide");
					$("#layoutCheckbox").parent().parent().find(".fieldset_div").show();
	 			}
	 		}else{
	 			$("#odmcolorder").val(data.odmupdownorder);//模块上下顺序
				$("#odmroworder").val(data.odmleftrightorder);//模块左右顺序
				$("#odmrowammount").val(data.odmrowcount);//每排模块数
				$("#terminalorder").val(data.terminalorder);//端子左右排列顺序
				$("#terminalrowammount").val(data.terminalrowcount);//每排端子数
				$("#layoutId").val(data.id);
				if(data.odmorderflag==1){
					$("#firstodmorder").attr("checked","checked");
				}else{
					$("#firstodmorder").removeAttr("checked");
				}
				showDifferentPanel();
				$("#layoutCheckbox").removeAttr("checked");
				$("#layoutCheckbox").parent().parent().addClass("fieldset-hide");
				$("#layoutCheckbox").parent().parent().find(".fieldset_div").hide();
		 	}
		},"json");
}

function showDifferentPanel(){	//预览效果
			
			var odmrowammount =$("#odmrowammount").val();//每排模块数
			var terminalrowammount = $("#terminalrowammount").val();//每排端子数
			if(odmrowammount==""||terminalrowammount==""){
				alert("每排模块数或端子数不能为空!");
			}else {
				$("div[id='panels']").empty();//清空之前显示模块端子信息
				var currentResourceName =$("#currentResourceName").val();
				$("div[id='panels']").append("<div id='messages' ></div>");
				$("div[id='panels']").append("<div id='paneltable_box'><div id='paneltable_title'>"+currentResourceName+"</div><div id='paneltable'></div></div>");
			//	$("div[id='panels']").append("<div id='paneltable'></div></div>");
				getOdmAndTerminalMessage();//ajax后台请求模块端子信息
			}
			
			
}
function getOdmAndTerminalMessage(){
			$("#paneltable_title").html($("#currentResourceName").val());
			var currentEntityType = $("#currentResourceType").val();//当前类型
			var currentEntityId=$("#currentResourceId").val();//当前资源id
			var params ={currentEntityType:currentEntityType,currentEntityId:currentEntityId};
			var odmcolorder =$("#odmcolorder").val();//模块上下顺序
			var odmroworder =$("#odmroworder").val();//模块左右顺序
			var odmrowammount =$("#odmrowammount").val();//每排模块数
			var terminalorder =$("#terminalorder").val();//端子左右排列顺序
			var terminalrowammount = $("#terminalrowammount").val();//每排端子数
			var firstodmorder=false;//模块先上下还是先左右排序 true为先上下 false为先左右
			if($("#firstodmorder").attr("checked")=="checked"){//checkbox控制
				firstodmorder=true;
			}
			$("#black").show();
			$("#loading_div").show();
			//alert(firstodmorder);
		   	$.post('getOdmAndTerminalMessageAction',params,function(data){
		   		$("#loading_div").hide();
		   		$("#black").hide();
				var mapCount=data.mapCount;//不同端子状态数目
				var mapName = data.mapName;//端子状态名
				var odmTerminalList=data.odmTerminalList;//模块与端子信息
				$.each(mapCount,function(index,value){//端子状态
					$.each(mapName,function(i,v){
						if(index==i){
							if(i=="terminalNoUseCount"){
								$("#messages").append("<span style='font-size=22px'><img style='position:relative;top:-2px;vertical-align:middle' src='image/terminalNoUseCount.png'/>"+v+"("+value+")</span>&nbsp;&nbsp");
							}
						//	else if(i=="terminalDiscardCount"){
						//		$("#messages").append("<span style='font-size=22px'><img style='position:relative;top:-2px;vertical-align:middle' src='image/terminalDiscardCount.png'/>"+v+"("+value+")</span>&nbsp;&nbsp");
						//	}else if(i=="terminalUsingCount"){
						//		$("#messages").append("<span style='font-size=22px'><img style='position:relative;top:-2px;vertical-align:middle' src='image/terminalUsingCount.png'/>"+v+"("+value+")</span>&nbsp;&nbsp");
						//	}
							else if(i=="terminalUsedCount"){
								$("#messages").append("<span style='font-size=22px'><img style='position:relative;top:-2px;vertical-align:middle' src='image/terminalUsedCount.png'/>"+v+"("+value+")</span>&nbsp;&nbsp");
							}else if(i=="terminalPreUsedCount"){
								$("#messages").append("<span style='font-size=22px'><img style='position:relative;top:-2px;vertical-align:middle' src='image/terminalPreUsedCount.png'/>"+v+"("+value+")</span>&nbsp;&nbsp");
							}
							else if(i=="terminalBreakCount"){
								$("#messages").append("<span style='font-size=22px'><img style='position:relative;top:-2px;vertical-align:middle' src='image/terminalBreakCount.png'/>"+v+"("+value+")</span>&nbsp;&nbsp");
							}
						//	else if(i=="terminalReadyUseCount"){
						//		$("#messages").append("<span style='font-size=22px'><img style='position:relative;top:-2px;vertical-align:middle' src='image/terminalReadyUseCount.png'/>"+v+"("+value+")</span>&nbsp;&nbsp");
						//	}else if(i=="terminalPartUseCount"){
						//		$("#messages").append("<span style='font-size=22px'><img style='position:relative;top:-2px;vertical-align:middle'src='image/terminalPartUseCount.png'/>"+v+"("+value+")</span>&nbsp;&nbsp");
							else if(i=="terminalOtherCount"){
							$("#messages").append("<span style='font-size=22px'><img style='position:relative;top:-2px;vertical-align:middle' src='image/terminalOtherCount.png'/>"+v+"("+value+")</span>&nbsp;&nbsp");
							}
							
						}
					})	
				})
				//checkbox控制端子名称显示与隐藏
				$("#messages").append("<span><input type='checkbox' checked='checked' onclick='showTerminalName(this);'>显示端子名称</span>");
				//odmrowammount=3;
				//根据每排模块数增加水平持平的div
				for(var i=0;i<parseInt(odmrowammount);i++){
					if(odmroworder=="lefttoright"){//模块左右排列
						
							
							if(parseInt(odmrowammount)==1){
								$("#paneltable").append("<div id='panelsdiv"+i+"' style='vertical-align: top;display:inline-block;margin-left:7px; margin-top:3px;border: 1px solid #CCCCCC;box-shadow: 1px 1px 3px #000000; padding: 2px;' ></div>");
							}else{
								$("#paneltable").append("<div id='panelsdiv"+i+"' style='vertical-align: top;display:inline-block;margin-left:7px; margin-top:3px;border: 1px solid #CCCCCC;box-shadow: 1px 1px 3px #000000; padding: 2px;'></div>");
							}
					}else{
							if(parseInt(odmrowammount)==1){
								$("#paneltable").prepend("<div id='panelsdiv"+i+"' style='vertical-align: top;display:inline-block;margin-left:7px; margin-top:3px;border: 1px solid #CCCCCC;box-shadow: 1px 1px 3px #000000; padding: 2px;' ></div>");
							}else{
								$("#paneltable").prepend("<div id='panelsdiv"+i+"' style='vertical-align: top;display:inline-block;margin-left:7px; margin-top:3px;border: 1px solid #CCCCCC;box-shadow: 1px 1px 3px #000000; padding: 2px;'></div>");
							}
							
						
					}
					
					
				}
				//模块数
				var listLength= odmTerminalList.length;
				var odmcolammount= Math.ceil(listLength/parseInt(odmrowammount));//每列模块数
				//alert(odmcolammount);
				$.each(odmTerminalList,function(index,value){//模块端子排列
					var terminalCount = value.terminalCount;//端子数量
					var odmName = value.name;//模块名
					var terminalList = value.terminalList;//端子
					var divIndex=0;//控制append模块的div
					if(firstodmorder==true){//模块先左右还是上下排列
						divIndex=parseInt((index)/odmcolammount);
					}else{
						divIndex=parseInt((index)%parseInt(odmrowammount));
					}
					//var divIndex = parseInt((index)/odmcolammount);
					var rowsCount=Math.ceil(parseInt(terminalCount)/parseInt(terminalrowammount));//一个模块端子的排数
					if(rowsCount==0){
						rowsCount=1;
					}
					//alert(divIndex)
					if(odmcolorder=="uptodown"){//模块上下排列控制
							$("#panelsdiv"+divIndex).append("<table id='"+odmName+"table' class='main-table1 tc' style='width:50%'></table>");
					}else{
							$("#panelsdiv"+divIndex).prepend("<table id='"+odmName+"table' class='main-table1 tc' style='width:50%'></table>");
					}
						
					for(var j=0;j<rowsCount;j++){//根据一个模块端子排数增加table tr
							//alert("ok")
							$("#"+odmName+"table").append("<tr id='"+odmName+j+"'></tr>")
					}
					if(terminalList!=null&&terminalList!=""&&terminalList!="[]"){//端子不为空
						
						$.each(terminalList,function(i,v){
							
							trIndex = parseInt(i/parseInt(terminalrowammount)); //端子增加到的tr index
							//alert(trIndex)
							if(terminalorder=="lefttoright"){//端子左右排列控制
								if(v.status=="空闲"){
									$("#"+odmName+trIndex).append("<td><span onclick='showTerminalMess("+v.id+");' class='effect-style effect-img1'><em id='showTerminal'>"+v.name+"</em></span></td>");
								}
							//	else if(v.status=="废弃"){
							//		$("#"+odmName+trIndex).append("<td><span  onclick='showTerminalMess("+v.id+");' class='effect-style effect-img2'><em id='showTerminal'>"+v.name+"</em></span></td>");
							//	}else if(v.status=="在用"){
							//		$("#"+odmName+trIndex).append("<td><span  onclick='showTerminalMess("+v.id+");' class='effect-style effect-img3'><em id='showTerminal'>"+v.name+"</em></span></td>");
							//	}
								else if(v.status=="占用"){
									$("#"+odmName+trIndex).append("<td><span  onclick='showTerminalMess("+v.id+");' class='effect-style effect-img4'><em id='showTerminal'>"+v.name+"</em></span></td>");
								}else if(v.status=="预占用"){
									$("#"+odmName+trIndex).append("<td><span  onclick='showTerminalMess("+v.id+");' class='effect-style effect-img5'><em id='showTerminal'>"+v.name+"</em></span></td>");
								}else if(v.status=="故障/损坏"){
									$("#"+odmName+trIndex).append("<td><span  onclick='showTerminalMess("+v.id+");' class='effect-style effect-img6'><em id='showTerminal'>"+v.name+"</em></span></td>");
								}
							//	else if(v.status=="备用"){
							//		$("#"+odmName+trIndex).append("<td><span  onclick='showTerminalMess("+v.id+");' class='effect-style effect-img7'><em id='showTerminal'>"+v.name+"</em></span></td>");
							//	}else if(v.status=="部分使用"){
							//		$("#"+odmName+trIndex).append("<td><span  onclick='showTerminalMess("+v.id+");' class='effect-style effect-img8'><em id='showTerminal'>"+v.name+"</em></span></td>");
							    else if(v.status=="其他"){
									$("#"+odmName+trIndex).append("<td><span  onclick='showTerminalMess("+v.id+");' class='effect-style effect-img9'><em id='showTerminal'>"+v.name+"</em></span></td>");
								}
							}else{
								if(v.status=="空闲"){
									$("#"+odmName+trIndex).prepend("<td><span  onclick='showTerminalMess("+v.id+");' class='effect-style effect-img1'><em id='showTerminal'>"+v.name+"</em></span></td>");
								}
							/*	else if(v.status=="废弃"){
									$("#"+odmName+trIndex).prepend("<td><span  onclick='showTerminalMess("+v.id+");' class='effect-style effect-img2'><em id='showTerminal'>"+v.name+"</em></span></td>");
								}else if(v.status=="在用"){
									$("#"+odmName+trIndex).prepend("<td><span  onclick='showTerminalMess("+v.id+");' class='effect-style effect-img3'><em id='showTerminal'>"+v.name+"</em></span></td>");
								}*/
								else if(v.status=="占用"){
									$("#"+odmName+trIndex).prepend("<td><span  onclick='showTerminalMess("+v.id+");' class='effect-style effect-img4'><em id='showTerminal'>"+v.name+"</em></span></td>");
								}else if(v.status=="预占用"){
									$("#"+odmName+trIndex).prepend("<td><span  onclick='showTerminalMess("+v.id+");' class='effect-style effect-img5'><em id='showTerminal'>"+v.name+"</em></span></td>");
								}else if(v.status=="故障/损坏"){
									$("#"+odmName+trIndex).prepend("<td><span  onclick='showTerminalMess("+v.id+");' class='effect-style effect-img6'><em id='showTerminal'>"+v.name+"</em></span></td>");
								}
							/*	else if(v.status=="备用"){
									$("#"+odmName+trIndex).prepend("<td><span  onclick='showTerminalMess("+v.id+");' class='effect-style effect-img7'><em id='showTerminal'>"+v.name+"</em></span></td>");
								}else if(v.status=="部分使用"){
									$("#"+odmName+trIndex).prepend("<td><span  onclick='showTerminalMess("+v.id+");' class='effect-style effect-img8'><em id='showTerminal'>"+v.name+"</em></span></td>");
								}*/
								else if(v.status=="其他"){
									$("#"+odmName+trIndex).prepend("<td><span  onclick='showTerminalMess("+v.id+");' class='effect-style effect-img9'><em id='showTerminal'>"+v.name+"</em></span></td>");
								}
							}	
						})
						if(odmName=="noHasOdm"){//模块名增加
							$("#"+odmName+"0").append("<td rowspan='"+rowsCount+"'></td>");
						}else{
							$("#"+odmName+"0").append("<td rowspan='"+rowsCount+"'>"+odmName+"</td>");
						}
					}	
				})
				
			},'json')
}

function showTerminalName(id){//显示端子名称是否
	if($(id).attr("checked")=="checked"){
		$("em[id='showTerminal']").css("display","");
	}else{
		$("em[id='showTerminal']").css("display","none");
	}
}

function showTerminalMess(terminalId){//端子维护
	var url = "getPhysicalresForOperaAction";
	var params = {currentEntityId:terminalId,currentEntityType:"Terminal",loadBigPage:"loadBigPage"}
	$.post(url, params, function(data){
			$("#terminalmess").html(data);
			$("#editDiv").empty();
			$("#editDiv").html("<input  type='button' value='保存' onclick='updateTerminal();'/>&nbsp;&nbsp;<input type='button' value='取消' onclick='cancelOpera();' />")
	});
				
}

function updateTerminal(){//更新端子
	if($("input[name='Terminal#name']").val()==""){
		alert("端子名不能为空!");
	}else{
		var updatedEntityType ="Terminal";
		var url="updatePhysicalresAction";
		var params={
			"Terminal#id":$("input[name='Terminal#id']").val(),
			"Terminal#name":$("input[name='Terminal#name']").val(),
			"Terminal#label":$("input[name='Terminal#label']").val(),
			"Terminal#status":$("input[name='Terminal#status']").val(),
			updatedEntityType:updatedEntityType
		}
		$.post(url, params, function(data){
				if(data=="success"){
					alert("保存成功");
					showDifferentPanel();
				}else{
					alert("保存失败");
				}
		},'json');
	}
	
}

function saveOdmterminallayout(){//保存布局设置
			var id = $("#layoutId").val();
			var odmcolorder =$("#odmcolorder").val();//模块上下顺序
			var odmroworder =$("#odmroworder").val();//模块左右顺序
			var odmrowammount =$("#odmrowammount").val();//每排模块数
			var terminalorder =$("#terminalorder").val();//端子左右排列顺序
			var terminalrowammount = $("#terminalrowammount").val();//每排端子数
			var firstodmorder=0;//模块先上下还是先左右排序 true为先上下 false为先左右
			if($("#firstodmorder").attr("checked")=="checked"){//checkbox控制
				firstodmorder=1;
			}
			var resourceid = $("#currentResourceId").val();
			var resourcetype = $("#currentResourceType").val();
			var params={
			"Odmterminallayout#id":id,
			"Odmterminallayout#odmupdownorder":odmcolorder,
			"Odmterminallayout#odmleftrightorder":odmroworder,
			"Odmterminallayout#odmorderflag":firstodmorder,
			"Odmterminallayout#odmrowcount":odmrowammount,			
			"Odmterminallayout#terminalorder":terminalorder,
			"Odmterminallayout#terminalrowcount":terminalrowammount,
			"Odmterminallayout#resourceid":resourceid,
			"Odmterminallayout#resourcetype":resourcetype
			}
			var url = "updateOdmandterminallayoutAction";
			$.post(url, params, function(data){
				if(data!="error"){
					alert("保存布局设置成功");
					$("#layoutId").val(data);
					showDifferentPanel();
					$("#layoutCheckbox").removeAttr("checked");
					$("#layoutCheckbox").parent().parent().addClass("fieldset-hide");
					$("#layoutCheckbox").parent().parent().find(".fieldset_div").hide();
				}else{
					alert("保存布局设置失败");
				}
		},'json');
}
</script>
<style>

</style>
<div id="ldiv">
			<div class="container-tab1">
                <fieldset style="width:96%;">
                    <legend> <input type="checkbox" checked="checked"/> 专项功能</legend>
                    
                    <div class="fieldset_div">
                    	<input type="hidden" id="currentResourceType" value="${param.currentEntityType}"/>
			            <input type="hidden" id="currentResourceId" value="${param.currentEntityId}"/>
                    	<input type="button" value="模块/端子批量维护" onclick="addOdmsAndTerminals();" />&nbsp;
                        <input type="button" id="odmterminalButton" value="面板图" onclick="showOdmsAndTerminalsPanel();"/>
                    </div>
                </fieldset>
            </div>
            <table class="main-table1">
                    <tr class="main-table1-tr"><td  class="main-table1-title">面板图</td></tr>
                    <tr><td>
            <div class="container-tab2">
                <fieldset class="fieldset-hide" style="width:96%;">
                    <legend> <input id="layoutCheckbox" type="checkbox" />布局设置</legend>
                    
                    <div class="fieldset_div" style="display:none">
                    	<table>
                    
                    <tr>
                    	<input type="hidden" id="layoutId" value=""/>
                   		
                    	<tr>
                        <td class="menuTd">模块排列：</td>
                        <td>模块排列顺序：<select id="odmcolorder"><option value="uptodown">从上往下</option><option value="downtoup">从下往上</option></select>&nbsp;左右排列顺序&nbsp;<select id="odmroworder"><option value="lefttoright">从左往右</option><option value="righttoleft">从右往左</option></select>&nbsp;&nbsp;
                        <input type="checkbox" id="firstodmorder" />先上下排序后左右排序&nbsp;&nbsp;
                                                                 每排模块数：<input id="odmrowammount" style="width:30px;color:#CC0033" type="text" value="1" onKeyUp="this.value=this.value.replace(/[^0-9]D*$/,'')" onafterpaste="this.value=this.value.replace(/[^0-9]D*$/,'')"/></td>
                    </tr>
                    <tr>
                        <td class="menuTd">端子排列：</td>
                        <td>端子排列顺序：<select id="terminalorder"><option value="lefttoright">从左往右</option><option value="righttoleft">从右往左</option></select>&nbsp;&nbsp;每排的端子数:<input id="terminalrowammount" onKeyUp="this.value=this.value.replace(/[^0-9]D*$/,'')" onafterpaste="this.value=this.value.replace(/[^0-9]D*$/,'')" style="width:30px;color:#CC0033" type="text" value="12"/>&nbsp;
                        </td>
                    </tr>
                    <tr>
                    	<td colspan="2"><input type="button" value="预览效果" onclick="showDifferentPanel();" />&nbsp;&nbsp;<input type="button" value="保存" onclick="saveOdmterminallayout();"/>
                    </tr>
                    
             </table>
                    </div>
                </fieldset>
            </div>
                    </td></tr>
  				</table>
  				<input type="hidden" id="currentResourceName" value="${param.currentEntityName}"/>
             <div id="panels" style="background:none repeat scroll 0 0 WhiteSmoke;">
             	
             	<div id="messages" ></div>
             	<div id="paneltable_box">
	             	<div id="paneltable_title"></div>
	                <div id="paneltable"></div>
                </div>
             </div>
             	<table style="width:100%"><tr id="terminalmess"><td></td></tr></table>
             	
             	<div id="loading_div" style="z-index:6;display:none; position:absolute; width:450px; height:200px; left:50%;top:200px;margin-left:-225px; border-radius:3px;">
			    	<div style="height:100px;padding-top:20px; width:300px; margin:auto;text-align:center;">
			        	<img src="image/ajax-loaderCccc.gif" />
			        </div>
			    	<div style="padding-top:5px;width:300px; margin:auto;text-align:center;">
			        	面板图数据加载中，请稍候
			        </div>
			    </div>
			    </div>