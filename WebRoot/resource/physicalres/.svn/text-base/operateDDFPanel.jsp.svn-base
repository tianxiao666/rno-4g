<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<link rel="stylesheet" type="text/css" href="css/base.css"/>
<link rel="stylesheet" type="text/css" href="css/public.css"/>
<link rel="stylesheet" type="text/css" href="css/equipmentBox.css"/>
<style>
#paneltable_box{background:#bbb; padding:10px 0px; border:1px solid #000;margin:8px auto;}

#paneltable{ text-align:center;}
#paneltable_title{text-shadow: 0 0 4px #000000;color:#fff; line-height:24px;text-align:center; font-weight:bold;margin-bottom:5px;}
#messages{background:#fff; border-bottom:1px solid #ccc; padding:3px;}
.main-table1 .menuTd{width:20%;}
.main-table1 .highLighte{ background:#FFF; width:30%;}
.main-table1 span{
cursor:pointer;
}
.main-table1 tr td{padding:2px;}
span{cursor:pointer}
</style>
<script type="text/javascript">
 
//浏览模式隐藏按钮
var tFlagIndex = 0;
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
				$("div[id='panels']").append("<div id='paneltable_box'><div id='paneltable_title' >"+currentResourceName+"</div><div id='paneltable' class='equipmentBox clearfix'></div></div>");
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
		   	$.post('getDDFOdmAndTerminalMessageAction',params,function(data){
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
							}else if(i=="terminalUsedCount"){
								$("#messages").append("<span style='font-size=22px'><img style='position:relative;top:-2px;vertical-align:middle' src='image/terminalUsedCount.png'/>"+v+"("+value+")</span>&nbsp;&nbsp");
							}else if(i=="terminalPreUsedCount"){
								$("#messages").append("<span style='font-size=22px'><img style='position:relative;top:-2px;vertical-align:middle' src='image/terminalPreUsedCount.png'/>"+v+"("+value+")</span>&nbsp;&nbsp");
							}
							else if(i=="terminalBreakCount"){
								$("#messages").append("<span style='font-size=22px'><img style='position:relative;top:-2px;vertical-align:middle' src='image/terminalBreakCount.png'/>"+v+"("+value+")</span>&nbsp;&nbsp");
							}else if(i=="terminalOtherCount"){
							$("#messages").append("<span style='font-size=22px'><img style='position:relative;top:-2px;vertical-align:middle' src='image/terminalOtherCount.png'/>"+v+"("+value+")</span>&nbsp;&nbsp");
							}
							
						}
					})	
				})
				//checkbox控制端子名称显示与隐藏
				$("#messages").append("<span><input type='checkbox' checked='checked' onclick='showTerminalName(this);'>显示端子名称</span><span><input type='checkbox' checked='checked' onclick='show2MTerminalName(this);'>显示2M回路名称</span>");
				//odmrowammount=3;
				//根据每排模块数增加水平持平的div
				for(var i=0;i<parseInt(odmrowammount);i++){
					if(odmroworder=="lefttoright"){//模块左右排列
						
							
							if(parseInt(odmrowammount)==1){
								$("#paneltable").append("<div id='panelsdiv"+i+"' style='vertical-align: top;display:inline-block;margin-left:7px; margin-top:3px;border: 1px solid #CCCCCC;box-shadow: 1px 1px 3px #000000; padding: 2px;' ></div>");
							}else{
								$("#paneltable").append("<div  id='panelsdiv"+i+"' style='vertical-align: top;display:inline-block;margin-left:7px; margin-top:3px;border: 1px solid #CCCCCC;box-shadow: 1px 1px 3px #000000; padding: 2px;'></div>");
							}
					}else{
							if(parseInt(odmrowammount)==1){
								$("#paneltable").prepend("<div  id='panelsdiv"+i+"' style='vertical-align: top;display:inline-block;margin-left:7px; margin-top:3px;border: 1px solid #CCCCCC;box-shadow: 1px 1px 3px #000000; padding: 2px;' ></div>");
							}else{
								$("#paneltable").prepend("<div  id='panelsdiv"+i+"' style='vertical-align: top;display:inline-block;margin-left:7px; margin-top:3px;border: 1px solid #CCCCCC;box-shadow: 1px 1px 3px #000000; padding: 2px;'></div>");
							}
							
						
					}
					
					
				}
				//模块数
				var listLength= odmTerminalList.length;
				var odmcolammount= Math.ceil(listLength/parseInt(odmrowammount));//每列模块数
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
					var rowsCount=Math.ceil(parseInt(terminalCount)/(parseInt(terminalrowammount)*4));//一个模块端子的排数
					if(rowsCount==0){
						rowsCount=1;
					}
					if(odmcolorder=="uptodown"){//模块上下排列控制
							$("#panelsdiv"+divIndex).append("<table class='equipmentBox_info' id='"+odmName+"table'></table><br>");
					}else{
							$("#panelsdiv"+divIndex).prepend("<table class='equipmentBox_info' id='"+odmName+"table'></table><br>");
					}
						
					for(var j=0;j<rowsCount;j++){//根据一个模块端子排数增加table tr
							//alert("ok")
							$("#"+odmName+"table").append("<tr id='"+odmName+j+"'></tr>")
					}
					if(terminalList!=null&&terminalList!=""&&terminalList!="[]"){//端子不为空
						$.each(terminalList,function(i,v){
							trIndex = parseInt(i/(parseInt(terminalrowammount)*4)); //端子增加到的tr index
							if($("#"+odmName+trIndex).children("td").size()>=terminalrowammount){
								trIndex = trIndex+1;
							}
							if(i==tFlagIndex){
								if(terminalorder=="lefttoright"){//端子左右排列控制
									var content=terminalContent(terminalList,i,odmName);
									$("#"+odmName+trIndex).append(content);
								}else{
									var content=terminalContent(terminalList,i,odmName);
									$("#"+odmName+trIndex).prepend(content);
								}
								var tdEl = $("#"+odmName+trIndex+" td:last");
								var span0 = tdEl.children().eq(0).attr("class");
								var span1 = tdEl.children().eq(1).attr("class");
								var span2 = tdEl.children().eq(2).attr("class");
								var span3 = tdEl.children().eq(3).attr("class");
								if(span0=="c8"&&span2=="c8"){
									tdEl.children().eq(0).attr("class","c8-1");
									tdEl.children().eq(2).attr("class","c8-2");
								}
								if(span1=="c8"&&span3=="c8"){
									tdEl.children().eq(1).attr("class","c8-1");
									tdEl.children().eq(3).attr("class","c8-2");
								}
							}															
						})
						for(var rIndex=0;rIndex<rowsCount;rIndex++){
							var content="";
							var tdSize = $("#"+odmName+rIndex).children().size();
							if(tdSize<terminalrowammount){
								for(var index=0;index<terminalrowammount-tdSize;index++){
									content += "<td class='ddf'><span ><em></em></span><span ><em></em></span><span ><em></em></span><span ><em></em></span><span class='text'><em></em></span></td>";
								}
								if(terminalorder=="lefttoright"){//端子左右排列控制
									$("#"+odmName+rIndex).append(content);
								}else{
									$("#"+odmName+rIndex).prepend(content);
								}
								
							}
						}
						
						if(odmName=="noHasOdm"){//模块名增加
							$("#"+odmName+"0").append("<td rowspan='"+rowsCount+"' class='ddfType'></td>");
						}else{
							$("#"+odmName+"0").append("<td rowspan='"+rowsCount+"' class='ddfType'>"+odmName+"</td>");
						}
					}	
				})
				
			},'json')
}

function showTerminalName(id){//显示端子名称是否
	if($(id).attr("checked")=="checked"){
		$("#paneltable span").each(function(){
			if(!$(this).hasClass("text")){
				$(this).children().eq(0).children().show();
			}
		})
	}else{
		$("#paneltable span").each(function(){
			if(!$(this).hasClass("text")){
				$(this).children().eq(0).children().hide();
			}
		})
	}
}
/*显示2M回路名称**/
function show2MTerminalName(id){//显示端子名称是否
	if($(id).attr("checked")=="checked"){
		$("#paneltable span").each(function(){
			if($(this).hasClass("text")){
				$(this).children().show();
			}
		})
	}else{
		$("#paneltable span").each(function(){
			if($(this).hasClass("text")){
				$(this).children().hide();
			}
		})
	}
}
function showTerminalMess(terminalId){//端子维护
	var url = "getPhysicalresForOperaAction";
	var params = {currentEntityId:terminalId,currentEntityType:"DDFTerminal",loadBigPage:"loadBigPage"}
	$.post(url, params, function(data){
			$("#terminalmess").html(data);
			$("#editDiv").empty();
			$("#editDiv").html("<input  type='button' value='保存' onclick='updateTerminal();'/>&nbsp;&nbsp;<input type='button' value='取消' onclick='cancelOpera();' />")
	});
				
}

function updateTerminal(){//更新端子
	if($("input[name='DDFTerminal#name']").val()==""){
		alert("端子名不能为空!");
	}else{
		var updatedEntityType ="DDFTerminal";
		var url="updatePhysicalresAction";
		var params={
			"DDFTerminal#id":$("input[name='DDFTerminal#id']").val(),
			"DDFTerminal#name":$("input[name='DDFTerminal#name']").val(),
			"DDFTerminal#label":$("input[name='DDFTerminal#label']").val(),
			"DDFTerminal#status":$("input[name='DDFTerminal#status']").val(),
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
/**字符串转数组*/
function stringToArray(value){
	return value.split("");
}
/**端子状态判断*/
function statusClass(status){
	if(status=="空闲"){
		return "c3";
	}else if(status=="占用"){
		return "c8";
	}else if(status=="预占用"){
		return "c6";
	}else if(status=="故障/损坏"){
		return "c1";
	}else if(status=="其他"){
		return "c4";
	}
}
/**端子内容*/
function terminalContent(terminalList,i,odmName){
	var content="";
	var name = terminalList[i].name;
	var mName= get2MtName(name,odmName);
	var bName="";
	if(i<=terminalList.length-1){
		content += "<td class='ddf'><span class='"+statusClass(terminalList[i].status)+"' onclick=\"showTerminalMess('"+terminalList[i].id+"')\" onmouseout='hideDDFTerminalInfo()' onmouseover=\"showDDFTerminalInfo('"+terminalList[i].id+"',event)\"><em><em>"+getShowTname(terminalList[i].name,mName)+"</em></em></span>";
	}else{
		content += "<td class='ddf'><span><em></em></span><span><em></em></span><span><em></em></span><span><em></em></span><span class='text'><em>"+mName+"</em></span></td>";
	}
	if(i+1<=terminalList.length-1){
		bName = get2MtName(terminalList[i+1].name,odmName);
		if(bName==mName){
			content +="<span class='"+statusClass(terminalList[i+1].status)+"' onclick=\"showTerminalMess('"+terminalList[i+1].id+"')\" onmouseout='hideDDFTerminalInfo()' onmouseover=\"showDDFTerminalInfo('"+terminalList[i+1].id+"',event)\"><em><em>"+getShowTname(terminalList[i+1].name,mName)+"</em></em></span>";
		}else{
			content += "<span><em></em></span><span><em></em></span><span><em></em></span><span class='text'><em>"+mName+"</em></span></td>";
			if(i+1==terminalList.length-1){
				tFlagIndex=0;
			}else{
				tFlagIndex=i+1;
			}	
		}			
	}else{
		content += "<span><em></em></span><span><em></em></span><span><em></em></span><span class='text'><em>"+mName+"</em></span></td>";
		tFlagIndex=0;
	}
	if(tFlagIndex!=i+1){
		if(i+2<=terminalList.length-1){
			bName = get2MtName(terminalList[i+2].name,odmName);
			if(bName==mName){
				content +="<span class='"+statusClass(terminalList[i+2].status)+"' onclick=\"showTerminalMess('"+terminalList[i+2].id+"')\" onmouseout='hideDDFTerminalInfo()' onmouseover=\"showDDFTerminalInfo('"+terminalList[i+2].id+"',event)\"><em><em>"+getShowTname(terminalList[i+2].name,mName)+"</em></em></span>";
			}else{
				content += "<span><em></em></span><span><em></em></span><span class='text'><em>"+mName+"</em></span></td>";
				if(i+2==terminalList.length-1){
					tFlagIndex=0;
				}else{
					tFlagIndex=i+2;
				}	
			}		
		}else{
			content += "<span><em></em></span><span><em></em></span><span class='text'><em>"+mName+"</em></span></td>";
			tFlagIndex=0;
		}
	}
	if(tFlagIndex!=i+2){
		if(i+3<=terminalList.length-1){
			bName = get2MtName(terminalList[i+3].name,odmName);
			if(bName==mName){
				content +="<span class='"+statusClass(terminalList[i+3].status)+"' onclick=\"showTerminalMess('"+terminalList[i+3].id+"')\" onmouseout='hideDDFTerminalInfo()' onmouseover=\"showDDFTerminalInfo('"+terminalList[i+3].id+"',event)\"><em><em>"+getShowTname(terminalList[i+3].name,mName)+"</em></em></span><span class='text'><em>"+mName+"</em></span></td>";
				if(i+3==terminalList.length-1){
					tFlagIndex=0;
				}else{
					tFlagIndex=i+4;
				}
			}else{
				content += "<span><em></em></span><span class='text'><em>"+mName+"</em></span></td>";
				if(i+3==terminalList.length-1){
					tFlagIndex=0;
				}else{
					tFlagIndex=i+3;
				}
			}		
		}else{
			content += "<span><em></em></span><span class='text'><em>"+mName+"</em></span></td>";
			tFlagIndex=0;
		}
	}
	
	return content;
}
/*2M回路名称*/
function get2MtName(name,odmName){
	var mName = odmName;
	var array = "";
	if(odmName=="noHasOdm"){
		array = name.split("");
	}else{
		name=name.substring(name.indexOf(odmName)+odmName.length,name.length);
		array = name.split("");
	}
	var flag=false;
	var moreLinkLabel=false;
	var firstLinkLabel=0;
	if(!/[A-Za-z0-9]/.test(array[0])){
		moreLinkLabel=true;
	}	
	for(var ins=0;ins<array.length;ins++){
		if(!/[A-Za-z0-9]/.test(array[ins])){
			firstLinkLabel=firstLinkLabel+1;
			if(odmName=="noHasOdm"){
				if(array[ins]!=""){
					break;
				}
			}else{
				if(moreLinkLabel){
					if(firstLinkLabel>1){
						break;
					}else{
						mName +=array[ins];
					}
				}else{
					break;
				}
			}
		}else {
			mName +=array[ins];
		}
	}
	return mName;
}


/*取得显示在面板的端子名称**/
function getShowTname(name,Mname){
	var mName = "";
	var array = name.substring(name.indexOf(Mname)+Mname.length,name.length);
	var flag=false;
	for(var ins=0;ins<=array.length-1;ins++){
		if(/[A-Za-z0-9]/.test(array[ins])){
			mName = mName+array[ins];
		}
	}
	return mName;
}
var st;
/*显示端子信息*/
function showDDFTerminalInfo(id,event){
	var params={"currentEntityType":"DDFTerminal","currentEntityId":id,"loadBasicPage":"loadBasicPage"};
	$.post("../physicalres/getPhysicalresAction",params,function(data){
		clearTimeout(st);
		$("#rDDFInfo").html(data);
		$("#infoDDFDiv").css("left",event.clientX).css("top",event.clientY).show();
		st=setTimeout("$('#infoDDFDiv').hide();",4000); 
	});
}
/**隐藏端子信息*/
function hideDDFTerminalInfo(){

$("#infoDDFDiv").hide();
}
</script>
<style>

</style>
<div id="ldiv">
			<input type="hidden" id="currentResourceType" value="${param.currentEntityType}"/>
			<input type="hidden" id="currentResourceId" value="${param.currentEntityId}"/>
			<input type="hidden" id="currentResourceName" value="${param.currentEntityName}"/>
			<div class="container-tab1">
                <fieldset style="width:96%;">
                    <legend> <input type="checkbox" checked="checked"/> 专项功能</legend>
                    <div class="fieldset_div">
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
                        <td class="menuTd">模块排列：<div style="color:#999;">(数字单元)&nbsp;</div></td>
                        <td>模块排列顺序：<select id="odmcolorder"><option value="uptodown">从上往下</option><option value="downtoup">从下往上</option></select>&nbsp;左右排列顺序&nbsp;<select id="odmroworder"><option value="lefttoright">从左往右</option><option value="righttoleft">从右往左</option></select>&nbsp;&nbsp;
                        <input type="checkbox" id="firstodmorder" />先上下排序后左右排序&nbsp;&nbsp;
                                                                 每排模块数：<input id="odmrowammount" style="width:30px;color:#CC0033" type="text" value="2" onKeyUp="this.value=this.value.replace(/[^0-9]D*$/,'')" onafterpaste="this.value=this.value.replace(/[^0-9]D*$/,'')"/></td>
                    </tr>
                    <tr>
                        <td class="menuTd">端子排列：<div style="color:#999;">(2M回路)&nbsp;&nbsp;</div></td>
                        <td>2M排列顺序：<select id="terminalorder"><option value="lefttoright">从左往右</option><option value="righttoleft">从右往左</option></select>&nbsp;&nbsp;每排的2M数:<input id="terminalrowammount" onKeyUp="this.value=this.value.replace(/[^0-9]D*$/,'')" onafterpaste="this.value=this.value.replace(/[^0-9]D*$/,'')" style="width:30px;color:#CC0033" type="text" value="8"/>&nbsp;
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
		    
		    <div id="infoDDFDiv" style='position: absolute;z-index: 1000;display:none;background: none repeat scroll 0 0 #E8EDFF'>
				<div class="flattening_mian" style="border: 2px solid #99BBF0;margin-top:0px;">
					<div id='rDDFInfo'></div>
				</div>
			</div>
