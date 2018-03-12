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
	
$(function(){
			//保存取消按钮清空
			$("#editDiv").empty();
			//模块与端子信息获得
			getOdmAndTerminalLayOutView();			
})

function getOdmAndTerminalLayOutView(){
		var currentVal = $("#currentValues").val();
		var currentEntityId = currentVal.substring(currentVal.indexOf("#")+1,currentVal.length);//当前id
		var currentEntityType=currentVal.substring(0,currentVal.indexOf("#"));//当前资源类型
		var params = {currentEntityType:currentEntityType,currentEntityId:currentEntityId};
	 	$.post("getOdmandterminallayoutAction", params, function(data){	
			if(data.maxCount!=undefined){
	 			$("#panels_view").html("无面板图");
	 		}else{
	 			$("#messages_view").html("");
	 			$("#paneltable_view").html("");
				var odmcolorder =data.odmupdownorder;//模块上下顺序
				var odmroworder =data.odmleftrightorder;//模块左右顺序
				var odmrowammount =data.odmrowcount;//每排模块数
				var terminalorder =data.terminalorder;//端子左右排列顺序
				var terminalrowammount = data.terminalrowcount;//每排端子数
				var firstodmorder=false;//模块先上下还是先左右排序 true为先上下 false为先左右
				if(data.odmorderflag==1){
					firstodmorder=true;
				}
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
								$("#messages_view").append("<span style='font-size=22px'><img style='position:relative;top:-2px;vertical-align:middle' src='image/terminalNoUseCount.png'/>"+v+"("+value+")</span>&nbsp;&nbsp");
							}else if(i=="terminalUsedCount"){
								$("#messages_view").append("<span style='font-size=22px'><img style='position:relative;top:-2px;vertical-align:middle' src='image/terminalUsedCount.png'/>"+v+"("+value+")</span>&nbsp;&nbsp");
							}else if(i=="terminalPreUsedCount"){
								$("#messages_view").append("<span style='font-size=22px'><img style='position:relative;top:-2px;vertical-align:middle' src='image/terminalPreUsedCount.png'/>"+v+"("+value+")</span>&nbsp;&nbsp");
							}
							else if(i=="terminalBreakCount"){
								$("#messages_view").append("<span style='font-size=22px'><img style='position:relative;top:-2px;vertical-align:middle' src='image/terminalBreakCount.png'/>"+v+"("+value+")</span>&nbsp;&nbsp");
							}else if(i=="terminalOtherCount"){
							$("#messages_view").append("<span style='font-size=22px'><img style='position:relative;top:-2px;vertical-align:middle' src='image/terminalOtherCount.png'/>"+v+"("+value+")</span>&nbsp;&nbsp");
							}
							
						}
					})	
				})
				//checkbox控制端子名称显示与隐藏
				$("#messages_view").append("<span><input type='checkbox' checked='checked' onclick='showTerminalNameView(this);'>显示端子名称</span><span><input type='checkbox' checked='checked' onclick='show2MTerminalNameView(this);'>显示2M回路名称</span>");
				//odmrowammount=3;
				//根据每排模块数增加水平持平的div
				for(var i=0;i<parseInt(odmrowammount);i++){
					if(odmroworder=="lefttoright"){//模块左右排列
						
							
							if(parseInt(odmrowammount)==1){
								$("#paneltable_view").append("<div id='panelsdiv"+i+"_view' style='vertical-align: top;display:inline-block;margin-left:7px; margin-top:3px;border: 1px solid #CCCCCC;box-shadow: 1px 1px 3px #000000; padding: 2px;' ></div>");
							}else{
								$("#paneltable_view").append("<div  id='panelsdiv"+i+"_view' style='vertical-align: top;display:inline-block;margin-left:7px; margin-top:3px;border: 1px solid #CCCCCC;box-shadow: 1px 1px 3px #000000; padding: 2px;'></div>");
							}
					}else{
							if(parseInt(odmrowammount)==1){
								$("#paneltable_view").prepend("<div  id='panelsdiv"+i+"_view' style='vertical-align: top;display:inline-block;margin-left:7px; margin-top:3px;border: 1px solid #CCCCCC;box-shadow: 1px 1px 3px #000000; padding: 2px;' ></div>");
							}else{
								$("#paneltable_view").prepend("<div  id='panelsdiv"+i+"_view' style='vertical-align: top;display:inline-block;margin-left:7px; margin-top:3px;border: 1px solid #CCCCCC;box-shadow: 1px 1px 3px #000000; padding: 2px;'></div>");
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
							$("#panelsdiv"+divIndex+"_view").append("<table class='equipmentBox_info' id='"+odmName+"table_view'></table><br>");
					}else{
							$("#panelsdiv"+divIndex+"_view").prepend("<table class='equipmentBox_info' id='"+odmName+"table_view'></table><br>");
					}
						
					for(var j=0;j<rowsCount;j++){//根据一个模块端子排数增加table tr
							//alert("ok")
							$("#"+odmName+"table_view").append("<tr id='"+odmName+j+"_view'></tr>")
					}
					if(terminalList!=null&&terminalList!=""&&terminalList!="[]"){//端子不为空
						$.each(terminalList,function(i,v){
							trIndex = parseInt(i/(parseInt(terminalrowammount)*4)); //端子增加到的tr index
							if($("#"+odmName+trIndex+"_view").children("td").size()>=terminalrowammount){
								trIndex = trIndex+1;
							}
							if(i==tFlagIndex){
								if(terminalorder=="lefttoright"){//端子左右排列控制
									var content=terminalContentView(terminalList,i,odmName);
									$("#"+odmName+trIndex+"_view").append(content);
								}else{
									var content=terminalContentView(terminalList,i,odmName);
									$("#"+odmName+trIndex+"_view").prepend(content);
								}
								var tdEl = $("#"+odmName+trIndex+"_view td:last");
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
							var tdSize = $("#"+odmName+rIndex+"_view").children().size();
							if(tdSize<terminalrowammount){
								for(var index=0;index<terminalrowammount-tdSize;index++){
									content += "<td class='ddf'><span ><em></em></span><span ><em></em></span><span ><em></em></span><span ><em></em></span><span class='text'><em></em></span></td>";
								}
								if(terminalorder=="lefttoright"){//端子左右排列控制
									$("#"+odmName+rIndex+"_view").append(content);
								}else{
									$("#"+odmName+rIndex+"_view").prepend(content);
								}
								
							}
						}
						
						if(odmName=="noHasOdm"){//模块名增加
							$("#"+odmName+"0_view").append("<td rowspan='"+rowsCount+"' class='ddfType'></td>");
						}else{
							$("#"+odmName+"0_view").append("<td rowspan='"+rowsCount+"' class='ddfType'>"+odmName+"</td>");
						}
					}	
				})
				
			},'json')
		 	}
		},"json");
}



function showTerminalNameView(id){//显示端子名称是否
	if($(id).attr("checked")=="checked"){
		$("#paneltable_view span").each(function(){
			if(!$(this).hasClass("text")){
				$(this).children().eq(0).children().show();
			}
		})
	}else{
		$("#paneltable_view span").each(function(){
			if(!$(this).hasClass("text")){
				$(this).children().eq(0).children().hide();
			}
		})
	}
}

function show2MTerminalNameView(id){//显示2M回路名称是否
	if($(id).attr("checked")=="checked"){
		$("#paneltable_view span").each(function(){
			if($(this).hasClass("text")){
				$(this).children().show();
			}
		})
	}else{
		$("#paneltable_view span").each(function(){
			if($(this).hasClass("text")){
				$(this).children().hide();
			}
		})
	}
}

/**字符串转数组*/
function stringToArray(value){
	return value.split("");
}
/**端子状态判断*/
function statusClassView(status){
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
function terminalContentView(terminalList,i,odmName){
	var content="";
	var name = terminalList[i].name;
	var mName= get2MtNameView(name,odmName);
	var bName="";
	if(i<=terminalList.length-1){
		content += "<td class='ddf'><span class='"+statusClassView(terminalList[i].status)+"'  onmouseout='hideDDFTerminalInfoView()' onmouseover=\"showDDFTerminalInfoView('"+terminalList[i].id+"',event)\"><em><em>"+getShowTnameView(terminalList[i].name,mName)+"</em></em></span>";
	}else{
		content += "<td class='ddf'><span><em></em></span><span><em></em></span><span><em></em></span><span><em></em></span><span class='text'><em>"+mName+"</em></span></td>";
	}
	if(i+1<=terminalList.length-1){
		bName = get2MtNameView(terminalList[i+1].name,odmName);
		if(bName==mName){
			content +="<span class='"+statusClassView(terminalList[i+1].status)+"'  onmouseout='hideDDFTerminalInfoView()' onmouseover=\"showDDFTerminalInfoView('"+terminalList[i+1].id+"',event)\"><em><em>"+getShowTnameView(terminalList[i+1].name,mName)+"</em></em></span>";
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
			bName = get2MtNameView(terminalList[i+2].name,odmName);
			if(bName==mName){
				content +="<span class='"+statusClassView(terminalList[i+2].status)+"'  onmouseout='hideDDFTerminalInfoView()' onmouseover=\"showDDFTerminalInfoView('"+terminalList[i+2].id+"',event)\"><em><em>"+getShowTnameView(terminalList[i+2].name,mName)+"</em></em></span>";
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
			bName = get2MtNameView(terminalList[i+3].name,odmName);
			if(bName==mName){
				content +="<span class='"+statusClassView(terminalList[i+3].status)+"'  onmouseout='hideDDFTerminalInfoView()' onmouseover=\"showDDFTerminalInfoView('"+terminalList[i+3].id+"',event)\"><em><em>"+getShowTnameView(terminalList[i+3].name,mName)+"</em></em></span><span class='text'><em>"+mName+"</em></span></td>";
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
function get2MtNameView(name,odmName){
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
function getShowTnameView(name,Mname){
	var mName = "";
	var array = name.substring(name.indexOf(Mname)+Mname.length,name.length).split("");
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
function showDDFTerminalInfoView(id,event){
	var params={"currentEntityType":"DDFTerminal","currentEntityId":id,"loadBasicPage":"loadBasicPage"};
	$.post("../physicalres/getPhysicalresAction",params,function(data){
		clearTimeout(st);
		$("#rDDFInfo_view").html(data);
		$("#infoDDFDiv_view").css("left",event.clientX).css("top",event.clientY).show();
		st=setTimeout("$('#infoDDFDiv_view').hide();",4000); 
	});
}
/**隐藏端子信息*/
function hideDDFTerminalInfoView(){
$("#infoDDFDiv_view").hide();
}
</script>
<style>

</style>
<div id="ldiv">
             <div id="panels_view" style="background:none repeat scroll 0 0 WhiteSmoke;">  	
             	<div id="messages_view" ></div>
             	<div id="paneltable_box_view">
	                <div id="paneltable_view" class='equipmentBox clearfix'></div>
                </div>
             </div>
			<div id="loading_div" style="z-index:6;display:none; position:absolute; width:450px; height:200px; left:50%;top:200px;margin-left:-225px; border-radius:3px;">
		    	<div style="height:100px;padding-top:20px; width:300px; margin:auto;text-align:center;">
		        	<img src="image/ajax-loaderCccc.gif" />
		        </div>
		    	<div style="padding-top:5px;width:300px; margin:auto;text-align:center;">
		        	面板图数据加载中，请稍候
		        </div>
		    </div>
		    </div>
		    
		    <div id="infoDDFDiv_view" style='position: fixed;z-index: 1000;display:none;background: none repeat scroll 0 0 #E8EDFF'>
				<div class="flattening_mian" style="border: 2px solid #99BBF0;margin-top:0px;">
					<div id='rDDFInfo_view'></div>
				</div>
			</div>
