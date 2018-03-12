<%@ page language="java" import="java.util.*" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>

<%@ taglib uri="/struts-tags" prefix="s"%>	

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>制定巡检计划</title>
<link rel="stylesheet" type="text/css" href="css/base.css" />
<link rel="stylesheet" type="text/css" href="css/public.css" />
<link rel="stylesheet" type="text/css" href="jslib/dialog/dialog.css"/>
<link rel="stylesheet" type="text/css" href="op/routineinspection/css/xunjian.css" />
<link rel="stylesheet" type="text/css" href="jslib/jquery/css/jquery.treeview.css" />
<link rel="stylesheet" type="text/css" href="jslib/paging/iscreate-paging.css" />

<script type="text/javascript" src="jslib/jquery/jquery-1.6.2.min.js" ></script>
<script type="text/javascript" src="jslib/jquery/jquery.form.js"></script>
<script type="text/javascript" src="jslib/jquery/jquery.validate.js"></script>
<script type="text/javascript" src="jslib/validate.ex.js"></script>
<script type="text/javascript" src="jslib/jquery/jquery.treeview.js"></script>
<script type="text/javascript" src="jslib/jquery/tree_demo.js"></script>
<script type="text/javascript" src="jslib/date/wdatePicker.js"></script>
<script type="text/javascript" src="jslib/paging/paging.js"></script>
<script type="text/javascript" src="op/jslib/generateTree.js"></script>
<script type="text/javascript" src="op/jslib/common.js"></script>

<script type="text/javascript" src="op/routineinspection/jslib/createRoutineInspectionPlanWorkOrder2.js"></script>

<style type="text/css">
.select_tree_div{display:none;background: none repeat scroll 0 0 #FFFFFF;border: 1px solid #CCCCCC;height:200px;position: absolute;width: 180px; overflow:auto;}
input[type="button"].add_button{display:inline-block;height:24px; width:72px; line-height:24px; text-align:center; color:#fff; text-decoration:none; font-weight:bold;}
input[type="button"].add_button{background:url("op/routineinspection/images/buttons1.png") no-repeat;}
input[type="button"].add_button:hover{background-position:-72px 0;}
input[type="button"].add_button:active{background-position:-144px 0;}
</style>

<script>
$(function(){
	
	/*
	//组织添加/修改基本信息弹出框
	$(".jizhan_showBtn").click(function(){
		$("#jizhan_Dialog").show();
		$(".black").show();
	});
	
	*/
	
	/*
	$(".modify_button").click(function(){
		$(".modify_alert").slideDown("fast");
	})
	$(".modify_alert_close").click(function(){
		$(".modify_alert").slideUp("fast");
	})
	
	*/
})

function deleteRoom(){


	var selectRoomWithOrgList=$("input[name='selectRoomWithOrgList']");
	if(!selectRoomWithOrgList || selectRoomWithOrgList.length==0){
		alert("没有要执行巡检任务的机房");
		return false;
	}else{
		var flag = judgeCheckboxIsSelected("selectRoomWithOrgList");
		if (flag == false) {
			alert("请至少选择一个要执行巡检任务的机房");
			return false;
		};
		$.each($("#thcenter_table_1 .pageTr"),function(){
			if($(this).children().eq(0).children().eq(0).attr("checked")=="checked"){
				$(this).remove();
			}
		});
		pagingColumnByForeground("table-gaging",$("#thcenter_table_1 .pageTr"),10);
	}

	
}

</script>
</head>

<body>

	<div class="container960">
    	<div class="header">
            <h4>制定巡检计划</h4>
        </div>
    
    	<div class="tool_bar">
        	<span class="tip_todo tip_done">设置巡检计划属性</span>
            <em class="tip_to"></em>
            <span class="tip_todo tip_now">设置巡检任务</span>
        </div>
        
        
        <input id="showPlanOrgId" name="showPlanOrgId" type="hidden" value="<s:property value='showPlanOrgId' />" />
        <input id="showPlanStartTime" name="showPlanStartTime" type="hidden" value="<s:property value='showPlanStartTime' />" />
        <input id="showPlanEndTime" name="showPlanEndTime" type="hidden" value="<s:property value='showPlanEndTime' />" />
        
        <div class="content">
            <%------------------ 第三步 ---------------------%>
            <div class="content_step">
            	<div class="tool_bar">
                    <span style="display: inline-block; margin: 5px 0;">
                        巡检任务：
                    </span>
                    <span class="tool_bar_right">
                        <%-- <a href="#" id="btn_addTask" class="add_button jizhan_showBtn">添 加</a>
                        <a href="#" class="delete_button">删 除</a>
                        <a href="#" class="modify_button">修 改</a> --%>
                        
                        <input type="button" id="btn_addTask" value="添加" />
                        <input type="button" value="删 除" onclick="deleteRoom();" />
                       	<input type="button" id="btn_modifyTask" value="修 改" />
                        
                    </span>
                    <form id="modifyRoutineInspectionTaskToPlanOfRoomForm" action="modifyRoutineInspectionTaskToPlanOfRoomAction" method="post">
	                    <div class="modify_alert" style="width:450px">
	                        <span>
	                        	<em class="form_title">巡检维护队：</em>
	                            <input type="text" id="searchOrgName2" name="modifySelectRoomToPlanOrgName" readonly="readonly" />
	                            <a href="#" class="orgButton" id="chooseAreaButton2"></a>
	                            <div id="treeDiv2" class="select_tree_div">
	                            	<%-- 放置组织架构树 --%>
	                            </div>
	                            <input id="searchOrgId2" name="modifySelectRoomToPlanOrgId" type="hidden" />
	                        </span>
	                        <span>
	                        	<em class="form_title">计划时间：</em>
	                        	<%--  <input type="text" /><a href="#" class="dateButton"></a> 至 
	                        	<input type="text" /><a href="#" class="dateButton"></a>--%>
	                        	
                        		<input type="text" id="date_text3" name="routineinspectionPlanworkorder.planStartTime" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" class="Wdate required input-text"/>
                        		至 
                        		<input type="text" id="date_text4" name="routineinspectionPlanworkorder.planEndTime" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'date_text3\')}'})" readonly="readonly"  class="Wdate required input-text"/>
	                        	
	                        </span>
	                        <span class="tc">
	                        	<input type="submit" id="btn_saveModifyRoom" value="保存" />
	                        	<input type="button" class="modify_alert_close" value="取消" />
	                        </span>
	                    </div>
                    </form>
                </div>
                
                <form id="submitRoutineInspectionPlanWorkOrderForm" action="submitRoutineInspectionPlanWorkOrderAction" method="post">
                <div class="content">
                    <table id="thcenter_table_1" class="thcenter_table tc">
                        <tr>
                            <th style="width:40px;"><input type="checkbox" id="ckall" /></th>
                            <%-- <th>巡检任务号</th> --%>
                            <th>巡检机房</th>
                            <th>所属巡检计划</th>
                            <th>巡检维护队</th>
                            <th>计划起始时间</th>
                            <th>计划结束时间</th>
                        </tr>
                        <%-- <tr class="pageTr">
                            <td><input type="checkbox" /></td>
                            <td><a href="#">0221</a></td>
                            <td><a href="#">金山大厦</a></td>
                            <td>2012年天河一体化项目第三季度基站巡检计划</td>
                            <td>天河一队</td>
                            <td>2012-10-01</td>
                            <td>2012-10-31</td>
                        </tr>
                        --%>
                        <s:iterator value="showRoomWithOrgList" var="map">
                        	<tr class="pageTr">
                        		<td><input type="checkbox" name="selectRoomWithOrgList" value="<s:property value="%{#map['resourceId']}" />" />
                        			<input type="hidden" name="saveSelectRoomWithOrgList" value="<s:property value="%{#map['resourceId']}" />" />
                        		</td>
                        		<%-- <td><s:property value="%{#map['routineinspectionToId']}" /></td> --%>
                        		<td><s:property value="%{#map['resourceName']}" /></td>
                        		<td><s:property value="%{#map['routineinspectionPlanName']}" /></td>
                        		<td><s:property value="%{#map['orgName']}" /></td>
                        		<td><s:property value="%{#map['routineinspectionPlanStartTime']}" /></td>
                        		<td><s:property value="%{#map['routineinspectionPlanEndTime']}" /></td>
                        		
                        		
                        		<%-- <input type="checkbox" name="selectTaskList" value="<s:property value='#riTask.routineinspectionToId' />" /></td>
                        		<td><s:property value="#riTask.routineinspectionToId" /></td>
                        		<td><s:property value="#riTask.resourceName" /></td>
                        		<td><s:property value="#riTask.routineinspectionPlanName" /></td>
                        		<td><s:property value="#riTask.orgName" /></td>
                        		<td><s:property value="#riTask.routineinspectionPlanStartTime" /></td>
                        		<td><s:property value="#riTask.routineinspectionPlanEndTime" /> --%>
                        	</tr>
                        </s:iterator>
                        
                    </table>
                    <%-- 默认每页10条或20条记录 --%>
                    <div class="paging_div" id="table-gaging">
                        
                    </div>
                    <div class="content_bottom">
                        <input id="btn_createPre" type="button" class="pre_step" value="上一步" />
                        <input type="submit"  value="保存" />
                    </div>
                </div>
               	</form>
            </div>
           	
           <%-- dialog选择机房所属的组织 --%>
            
            
            <%--巡检计划的基站--%>
            <form id="submitSelectRoutineInspectionToPlanForm" action="submitSelectRoutineInspectionToPlanAction" method="post">
            <div id="jizhan_Dialog" class="dialog jizhan_dialog" style="display:none; top:80px;">
                <div class="dialog_header">
                    <div class="dialog_title">待编入巡检计划的基站</div>
                    <div class="dialog_tool">
                       <div class="dialog_tool_close dialog_closeBtn"></div>
                    </div>
                </div>
                <div class="dialog_content">
                    <div class="planning_top">
                        <input type="text" id="searchOrgName1" name="selectRoomToPlanOrgName1" readonly="readonly" />
                       	<a href="#" class="orgButton" id="chooseAreaButton1"></a>
                       	<div id="treeDiv1" class="select_tree_div">
	    					<%-- 放置组织架构树 --%>
	    				</div>
	    				<input id="searchOrgId1" name="selectRoomToPlanOrgId1" type="hidden" />
                        <input id="searchRoomName" type="text" placeholder="机房名称" />
                        <input id="btnSearchRoom" type="button" value="查询" />
                    </div>
                    <table id="thcenter_table_2" class="thcenter_table tc">
                        <tr>
                            <th style="width:40px;"><input type="checkbox" id="ckall2" /></th>
                            <th>机房名称</th>
                            <th>机房等级</th>
                            <th>维护组织</th>
                            <th style="width:150px;">机房地址</th>
                            <th>上一次巡检时间</th>
                        </tr>
                        <%--  <tr>
                            <td><input type="checkbox" /></td>
                            <td><a href="#">天河中医院</a></td>
                            <td>VIP</td>
                            <td>维护1队</td>
                            <td>天河区棠石路121号天河中医院7楼</td>
                            <td>2012-10-01</td>
                        </tr>--%>
                    </table>
                    <%-- 默认每页10条或20条记录 --%>
                    <div class="paging_div" id="table-gaging2">
                    </div>
                    
                    <div class="planning_footer">
                        <span>
                        	巡检组织：<input type="text" id="searchOrgName" name="selectRoomToPlanOrgName" readonly="readonly" />
                        	<a href="javascript:void(0);" class="orgButton" id="chooseAreaButton"></a>
                        	<div id="treeDiv" class="select_tree_div">
		    					<%-- 放置组织架构树 --%>
		    				</div>
		    				<input id="searchOrgId" name="selectRoomToPlanOrgId" type="hidden" />
                        </span>
                        <span class="planning_time"><em class="red">*&nbsp;</em>巡检时间：
                        	<input type="text" id="date_text1" name="selectRoomToPlanBeginTime" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" class="Wdate required input-text"/>
                        	至 
                        	<input type="text" id="date_text2" name="selectRoomToPlanEndTime" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'date_text1\')}'})" readonly="readonly" class="Wdate required input-text"/>
                        </span>
                        </div>
                        <div class="dialog_but">
                        <button type="submit" id="btnSelectRoom" class="aui_state_highlight">确定</button>
                        <button type="button" class="aui_state_highlight dialog_closeBtn">返回</button>
                    </div>
                </div>
            </div>
            </form>
        </div>
    </div>
</body>
</html>
