<%@ page language="java" import="java.util.*" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>

<%@ taglib uri="/struts-tags" prefix="s"%>	

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>巡检计划管理</title>
<link rel="stylesheet" type="text/css" href="../../css/base.css" />
<link rel="stylesheet" type="text/css" href="../../css/public.css" />
<link rel="stylesheet" type="text/css" href="css/xunjian.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/paging/iscreate-paging.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery.treeview.css" />

<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js" ></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js"></script>
<script type="text/javascript" src="../../jslib/jquery/tree_demo.js"></script>
<script type="text/javascript" src="../../jslib/paging/paging.js"></script>
<script type="text/javascript" src="../jslib/generateTree.js"></script>
<script type="text/javascript" src="jslib/searchRoutineInspectionPlanWorkOrder.js" ></script>
<script>
$(function(){
/*
	$(".gantt").hide();
	$(".gantt_show").each(function(){
		$(this).click(function(){
			var tdXY = $(this).offset();
			var tdX = tdXY.left - 123 + "px";
			var tdY = tdXY.top + 27 + "px";
			$(".gantt").css({"left":tdX,"top":tdY});
			$(".gantt").slideDown(200);
		})
	})
	
	$(".gantt_close").click(function(){
		$(".gantt").slideUp(200);
	})
	*/
	$(".explain_title").click(function(){
		$(".explain_message").toggle();
	});
})
</script>
</head>

<body>

	<div class="container">
    	<div class="tool_bar">
        	<span>
            	<input type="text" id="searchOrgName" readonly="readonly" /><a href="#" class="orgButton" id="chooseAreaButton"></a>
            	<div id="treeDiv" class="select_tree_div" style="display: none;">
   					<%-- 放置组织架构树 --%>
   				</div>
   				<input id="searchOrgId" name="selectRoomToPlanOrgId" type="hidden" />
                <select id="type">
                    <option value="" selected="selected">请选择</option>
                    <option value="1">基站巡检</option>
                </select>
                <select id="status">
                	<option value="" selected="selected">请选择</option>
                    <option value="20">执行中</option>
                    <option value="21">已关闭</option>
                </select>
                <input type="text" id="planName" placeholder="巡检计划名称" />
                <input type="button" value="查询" onclick="loadPlanWorkOrderList();" />
            </span>
			<%--
            <span class="fr">
				<a href="#" class="add_button">添 加</a>
				<a href="#" class="delete_button">删 除</a>
            </span>
			--%>
        </div>
        
        <div class="content">
        	<div id="planWorkOrderList">
        	</div>
            <%-- 默认每页10条或20条记录 --%>
            <div class="paging_div" id="planWorkOrderPage">
            </div>
            <div class="explain">
				<div class="explain_title"><em class="explain_icon">▼</em>指标说明：</div>
            	<ul class="explain_message">
                	<li>
                    	<span class="colored bg_green">进展顺利</span>
                        表示进展顺利，算法 = 【实际进度】 > 【计划进度】
                    </li>
                	<li>
                    	<span class="colored bg_yellow">快超时</span>
                        表示进展预警，算法 = 【计划进度】 > 【实际进度】 > 【计划进度 - 5%】
                    </li>
                	<li>
                    	<span class="colored bg_red">已超时</span>
                        表示进展滞后，算法 = 【计划进度】 > 【实际进度】
                    </li>
                	<li>
                   	 	<h4>算法注释：计划进度算法 = （当前时间 - 预计开始时间）/ （预计结束时间 - 预计开始时间）; 当前进度算法 = 已完成巡检任务数量 /  巡检任务总数</h4>
                	</li>
                	<%-- 
                	<li>
                    	<h4>操作备注：点击执行进度，将弹出该巡检计划的甘特图。</h4>
                    </li>
                     --%>
                </ul>
            </div>
        </div>
    </div>
    <div class="insp_black">
        <img src="../../images/loading_img.gif"><br>数据处理中，请稍后...
    </div>
	<div class="gantt" style="display: none;">
    	<em class="gantt_tip"></em>
        <div class="gantt_main">
        	<div class="gantt_title tc">
            	<h4>2012年海珠一体化项目组第三季度基站 巡检计划执行进度甘特图</h4>
            	<span class="gantt_close"></span>
            </div>
        	<div class="gantt_content">
            	<ul id="gantt_tree" class="filetree">
                	<li><span class="folder">海珠一体化项目组</span><em class="schedule_bar"><i class="schedule_true"></i></em>
                        <ul>
                            <li><span>南部片区</span><em class="schedule_bar"><i class="schedule_true"></i></em></li>
                            <li><span>西部片区</span><em class="schedule_bar"><i class="schedule_true"></i></em></li>
                            <li><span>东部片区</span><em class="schedule_bar"><i class="schedule_true"></i></em>
                            	<ul>
                                    <li><span>维护队1</span><em class="schedule_bar"><i class="schedule_true"></i></em></li>
                                    <li><span>维护队2</span><em class="schedule_bar"><i class="schedule_true"></i></em></li>
                                    <li><span>维护队3</span><em class="schedule_bar"><i class="schedule_true"></i></em></li>
                                </ul>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</body>
</html>



