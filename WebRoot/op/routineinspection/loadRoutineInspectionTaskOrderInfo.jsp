<%@ page language="java" import="java.util.*" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>

<%@ taglib uri="/struts-tags" prefix="s"%>	

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查看巡检中任务</title>
<link rel="stylesheet" type="text/css" href="../../css/base.css" />
<link rel="stylesheet" type="text/css" href="../../css/public.css" />
<link rel="stylesheet" type="text/css" href="css/xunjian.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/paging/iscreate-paging.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery.treeview.css" />

<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js" ></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js"></script>
<script type="text/javascript" src="../../jslib/jquery/tree_demo.js"></script>
<script type="text/javascript" src="js/xunjian.js" ></script>
<script type="text/javascript" src="../../jslib/paging/paging.js"></script>
<script type="text/javascript" src="jslib/loadRoutineInspectionTaskOrderInfo.js"></script>
<script>
$(function(){
	$("#signOut").attr("disabled","disabled");
	$(".list_ul2 li").each(function(){
		$(this).click(function(){
			$(".list_ul2 li").removeClass("on");
			$(this).addClass("on");
		})
	})
	
	changeBgcByStatus();
	
	//加载任务管理
	loadQuestionList();
	
	$("#toAddQuestion").click(function(){
		toAddQestion();
	});
	
});

//改变巡检指标的左边树背景颜色
function changeBGColor(me){
	$(".list_ul2 li").removeClass("on");
	$(me).addClass("on");
}

//加载问题列表
function loadQuestionList(){
	var toId = $("#TOID").val();
	var pageDivId = "questionListPage";
	var showDivId = "questionList";
	var actionName = "loadRoutineinspectionQuestionListByToIdAction";
	var pageSize = "10";
	var param={
		TOID : toId,
		currentPage:"1",
		pageSize:pageSize
	};
	pagingColumnByBackgroundJsp(pageDivId,showDivId,actionName,param);
}

//打开问题信息
function openQuestionInfo(id,toId){
	window.open("loadRoutineinspectionQuestionInfoAction?QID="+id+"&TOID="+toId);
}

//跳转添加页面
function toAddQestion(){
	var resourceId = $("#resourceId").val();
	var resourceType = $("#resourceType").val();
	var toId = $("#TOID").val();
	location.href = "jumpAddRoutineinspectionQuestionPageAction?TOID="+toId+"&resourceId="+resourceId+"&resourceType="+resourceType;
}

function changeBgcByStatus(){
	var status = $("#status").val();
	if(status=="22"){
		//淡灰色
		$("#statusColor").css("background","#ccc");
		$("#signOut").attr("disabled","disabled");
		$("#submitResource").attr("disabled","disabled");
		$("#toAddQuestion").attr("disabled","disabled");
	}else if(status=="23"){
		//淡蓝色
		$("#statusColor").css("background","lightblue");
		$("#signIn").hide();
		$("#signOut").show();
		$("#submitResource").removeAttr("disabled","disabled");
		$("#toAddQuestion").removeAttr("disabled","disabled");
	}else if(status=="24"){
		//淡绿色
		$("#statusColor").css("background","lightgreen");
		$("#signIn").hide();
		$("#signOut").show();
		$("#signOut").attr("disabled","disabled");
		$("#submitResource").attr("disabled","disabled");
		$("#toAddQuestion").attr("disabled","disabled");
	}
	
	//判断登陆人是否为维护人员
	var toId = $("#TOID").val();
	$.post("judgeLoginPeopleIsMaintenanceWorkerAction",{"TOID":toId},function(data){
		if(!data){
			$("#signIn").hide();
			$("#signOut").hide();
			$("#submitResource").hide();
		}
	},"json");
}

//签到按钮
function signInButton(){
	var TOID = $("#TOID").val();
	$.post("signInRoutineInspectionAction",{"TOID":TOID},function(data){
		if(data==false){
			alert("签到失败");
		}else{
			$("#statusColor").css("background","lightblue");
			$("#statusColor").html("巡检中");
			$("#signIn").hide();
			$("#signOut").show();
			$(".tab_menu").children().eq(0).children().attr("class","");
			$(".tab_menu").children().eq(0).children().eq(1).attr("class","ontab");
			$(".tab_content").hide();
			$(".tab_content").eq(1).show();
			$("#signOut").attr("disabled","disabled");
			$("#submitResource").show();
			$("#submitResource").removeAttr("disabled","disabled");
			$("#toAddQuestion").removeAttr("disabled","disabled");
		}
	},"json");
}

//签退按钮
function signOutButton(){
	var TOID = $("#TOID").val();
	$.post("signOutRoutineInspectionAction",{"TOID":TOID},function(data){
		if(data==false){
			alert("签退失败");
		}else{
			$("#status").val(24);
			changeBgcByStatus();
			$("#statusColor").html("已关闭");
		}
	},"json");
}

//提交资源按钮事件
function submitResourceEvent(ev){
	if(ev=="show"){
		$("#submitResource").show();
	}else{
		$("#submitResource").hide();
	}
}
</script>
</head>

<body>
	<input type="hidden" id="status" value="<s:property value='taskInfoMap.status' />" />
	<input type="hidden" id="TOID" value="<s:property value='taskInfoMap.TOID' />" />
	<input type="hidden" id="WOID" value="<s:property value='taskInfoMap.WOID' />" />
	<input type="hidden" id="resourceId" value="<s:property value='taskInfoMap.reId' />" />
	<input type="hidden" id="resourceType" value="<s:property value='taskInfoMap.reType' />" />
	<input type="hidden" id="tempIds" value="" />
	<input type="hidden" id="eId" value="" />
	<input type="hidden" id="eType" value="" />
    <div class="container960">
        <div class="header">
            <h4><s:property value='taskInfoMap.taskTitle' />
            	<span class="working">
            		<em id="statusColor" class="working_ico now_working"><s:property value='taskInfoMap.statusName' /></em>
                </span>
            </h4>
        </div>
        <div class="content">
        	<div class="tab">
                <div class="tab_menu">
                    <ul>
                        <li class="ontab" onclick="submitResourceEvent('hide');">任务信息</li>
                        <li onclick="submitResourceEvent('show');">巡检指标</li>
                        <li onclick="submitResourceEvent('hide');">问题管理</li>
                    </ul>
                </div>
                <div class="tab_container">
                    <div class="tab_content">
                        <div class="tab_content_right">
                        	<table class="thleft_table">
                                <tr>
                                    <th colspan="4"><em class="edit_ico">任务详细信息</em></th>
                                </tr>
                                <tr>
                                    <td class="label_td">巡检任务：</td>
                                    <td colspan="3"><s:property value='taskInfoMap.planTitle' /></td>
                                </tr>
                                <tr>
                                    <td class="label_td">巡检机房：</td>
                                    <td class="blue"><s:property value='taskInfoMap.reName' /></td>
                                    <td class="label_td">巡检组织：</td>
                                    <td><s:property value='taskInfoMap.orgName' /></td>
                                </tr>
                                <tr>
                                    <td class="label_td">巡检类型：</td>
                                    <td><s:property value='taskInfoMap.type' /></td>
                                    <td class="label_td">巡检专业：</td>
                                    <td><s:property value='taskInfoMap.profession' /></td>
                                </tr>
                                
                                <tr>
                                    <td class="label_td">计划开始时间：</td>
                                    <td><s:property value='taskInfoMap.taskPlanBeginTime' /></td>
                                    <td class="label_td">计划完成时间：</td>
                                    <td><s:property value='taskInfoMap.taskPlanEndTime' /></td>
                                </tr>
                                <tr>
                                	<td class="label_td">实际开始时间：</td>
                                    <td><s:property value='taskInfoMap.signInTime' /></td>
                                    <td class="label_td">实际完成时间：</td>
                                    <td><s:property value='taskInfoMap.signOutTime' /></td>
                                </tr>
                            </table>
                        </div>
                    </div>
                    
                    <div class="tab_content">
                    	<table class="thleft_table">
                            <tr>
                                <th colspan="4"><em class="edit_ico">定义资源巡检指标</em></th>
                            </tr>
                            <tr>
                                <td id="equipmentList" class="vt" style="width:180px; padding:0px;">
                                    <ul class="nav_ul">
                                        
                                    </ul>
                                </td>
                                <td class="vt">
                                	<div id="rightTable" style="display: none;">
	                                    <h4 class="table_title">
	                                        <span>【 <em id="eName" class="blue"> </em> 】巡检指标</span>
	                                        <span class="tool_bar_right" style="right:1px; top:1px;"></span>
	                                    </h4>
	                                    <div id="eContent">
	                                    	<table class="thleft_table inside_table">
		                                        <tr>
		                                            <th>巡检内容</th>
		                                            <th>巡检结果</th>
		                                            <th>备注</th>
		                                        </tr>
		                                        <tr>
		                                            <td><div class="title_overflow">【资产编码】</div></td>
		                                            <td><input type="text" /></td>
		                                            <td><textarea style="width:180px; height:40px;"></textarea></td>
		                                        </tr>
		                                        <tr>
		                                            <td><div class="title_overflow">【空调温度】空调正常温度范围应该处在【15，25】之间</div></td>
		                                            <td><input type="text" /></td>
		                                            <td><textarea style="width:180px; height:40px;"></textarea></td>
		                                        </tr>
												<tr>
													<td><div>机房及附近是否发生沉降、塌方，或有杂草、易燃物品等安全隐患</div></td>
													<td>
		                                            	<input type="radio" name="radio1" value="正常" checked="checked" />正常
		                                            	<input type="radio" name="radio1" value="异常" />异常
		                                            </td>
		                                            <td><textarea style="width:180px; height:40px;"></textarea></td>
		                                        </tr>
		                                        <tr>
		                                            <td><div class="title_overflow">【交流开关接线接触是否良好】电压：380V±15%,电流：开关容量的70％</div></td>
		                                            <td>
		                                            	<input type="radio" checked="checked" />是
		                                            	<input type="radio" />否
		                                            </td>
		                                            <td><textarea style="width:180px; height:40px;"></textarea></td>
		                                        </tr>
		                                        <tr>
		                                            <td><div class="title_overflow">【交流电电压类型】电压：380V±15%,电流：开关容量的70％</div></td>
		                                            <td>
		                                            	<input type="checkbox" checked="checked" />24V
		                                            	<input type="checkbox" />48V
		                                            </td>
		                                            <td><textarea style="width:180px; height:40px;"></textarea></td>
		                                        </tr>
		                                    </table>
	                                    </div>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                    
                    <div class="tab_content">
                    	<h4 class="tr">
                        	<input type="button" id="toAddQuestion" value="新增问题点" />
                        </h4>
                         <div id="questionList">
				       	</div>
				        <%-- 默认每页10条或20条记录 --%>
				        <div class="paging_div" id="questionListPage">
				        </div>
                        
                        
                    </div>
                    
                        
                    <div class="content_bottom">
                    	<input type="button" style="display:none;" id="submitResource" value="提交资源巡检结果" onclick="submitResourceContent();" />
                    	<input type="button" id="signIn" value="签到" style="width:80px;" onclick="signInButton();" />
                        <input type="button" id="signOut" value="签退" style="display: none;width:80px;" onclick="signOutButton();" />
                    </div>
                </div>
            </div>
        </div>
    </div>
	
</body>
</html>

