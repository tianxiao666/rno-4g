<%@ page language="java" import="java.util.*" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>

<%@ taglib uri="/struts-tags" prefix="s"%>	

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>待巡检任务</title>
<link rel="stylesheet" type="text/css" href="../../css/base.css" />
<link rel="stylesheet" type="text/css" href="../../css/public.css" />
<link rel="stylesheet" type="text/css" href="css/xunjian.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/paging/iscreate-paging.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery.treeview.css"/>
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js" ></script>
<script type="text/javascript" src="../../jslib/paging/paging.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js"></script>
<script type="text/javascript" src="../jslib/generateTree.js"></script>
<script type="text/javascript" src="jslib/pendingRoutineInspectionTaskOrder.js" ></script>
</head>

<body>

	<div class="container">
    	<div class="tool_bar">
        	<span>
            	<%--<input type="text" id="searchOrgName" readonly="readonly" /><a href="#" class="orgButton" id="chooseAreaButton"></a>
            	 <div id="treeDiv" class="select_tree_div" style="display: none;">
   					
   				</div> 
   				<input id="searchOrgId" name="selectRoomToPlanOrgId" type="hidden" />--%>
                <select id="type" style="width:120px;">
                    <option value="" selected="selected">请选择</option>
                    <option value="1">基站巡检</option>
                </select>
                
                <input id="planName" type="text" placeholder="巡检计划名称" />
                <input id="taskName" type="text" placeholder="巡检任务名称" />
                <input type="button" value="查询" onclick="loadTaskList();" />
            </span>
            <span class="tool_bar_right">
            <%--  <a href="#" class="delete_button">删 除</a> --%>
            </span>
        </div>
        
        <div class="content">
        	<div id="taskList">
        	</div>
            <%-- 默认每页10条或20条记录 --%>
            <div class="paging_div" id="taskListPage">
                
            </div>
            
            <div class="explain_message" style="background:#fff; border:none; padding:0;">
                <ul>
                    <li>
                        <span class="colored bg_yellow" style="width:24px;">快</span>
                        任务快超时，即【截止时间】  >  【巡检时间】  >  【截止时间  +  3天】
                        <span class="colored bg_red" style="width:24px; margin-left:20px;">超</span>
                        任务已超时，即【巡检时间】  >  【截止时间】
                    </li>
                </ul>
            </div>
        </div>
    </div>

</body>
</html>


