<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>网络设施详细信息页面</title>
  </head>
<link rel="stylesheet" href="css/base.css" type="text/css" />
<link rel="stylesheet" href="css/public.css" type="text/css" />
<link rel="stylesheet" href="css/gis_Concentration.css" type="text/css" />
<link rel="stylesheet" href="css/screen.css" />
<style>
.ddd_span{
	display: block;
    margin: 0 auto;
    overflow: hidden;
    text-align: center;
    text-overflow: ellipsis;
    white-space: nowrap;
    width: 250px;
}

</style>
<script type="text/javascript" src="js/jquery-1.6.2.min.js"></script>
<script type="text/javascript">
/**
 * 顶部菜单Table切换
 */
function topTableChange(){
	$(".tab_1 ul li").each(function(index){
		$(this).click(function(){
			$(".tab_1 ul li").removeClass("ontab");
			$(this).addClass("ontab");
			$(".tab_content").hide();
			$(".tab_content").eq(index).show();
		})
	});
}
</script>
<body onload="topTableChange()">
   <div id="top_content" >
    	<div class="tab_1">
        	<ul>
            	<li class="ontab">工单</li>
                <li>任务单</li>
            </ul>
        </div>
            
        <div class="tab_content">
        	<%-- 
        	<div class="radio_tool">
            	<input type="radio" name="woRadio" class="vm" checked="checked" onclick="getWorkOrderList('pendingWorkOrder')"/>待办工单&nbsp;
                <input type="radio" name="woRadio" class="vm" onclick="getWorkOrderList('trackWorkOrder')"/>跟踪工单&nbsp;
                <input type="radio" name="woRadio" class="vm" onclick="getWorkOrderList('superviseWorkOrder')"/>监督工单&nbsp;
            </div>
              --%>
            <div id="dvAll" class="tab_content_main">
            	<div style="overflow:hidden;" ></div>
            	<div  id="tbAll">
                    <table class="tab_content_table tc" id="workOrderTable">
                        <tr id="b2">
                            <th style="width:30px;"></th>
                            <th>工单号</th>
                            <th>工单主题</th>
                            <th>创建人</th>
                            <th>创建时间</th>
                            <th>要求完成时间</th>
                            <th>当前责任人</th>
                            <th>工单状态</th>
                        </tr>
                        <c:forEach var="wk" items="${resultMap.workOrderList}" varStatus="vs">
                        	<tr>
                        		<td>${vs.index+1}</td>
                        		<td><a href="../${wk.WOEDITURL}?WOID=${wk.WOID}" target="_blank">${wk.WOID}</a></td>
                        		<td><span class="ddd_span">${wk.WOTITLE}</span></td>
                        		<td>${wk.CREATEPERSON}</td>
                        		<td>${wk.CREATETIME}</td>
                        		<td>${wk.BIZ_OVERTIME}</td>
                        		<td>${wk.OPERATORID}</td>
                        		<td>${wk.STATUSNAME}</td>
                        	</tr>
                        </c:forEach>
                    </table>
                </div>
            </div>
        </div>
        <div class="tab_content" style="display:none;">
        	<%-- 
        	<div class="radio_tool">
            	<input type="radio" name="toRadio" class="vm" checked="checked" onclick="getTaskOrderList('pendingTaskOrder')"/>待办任务&nbsp;
                <input type="radio" name="toRadio" class="vm" onclick="getTaskOrderList('trackTaskOrder')"/>跟踪任务&nbsp;
                <input type="radio" name="toRadio" class="vm" onclick="getTaskOrderList('superviseTaskOrder')"/>监督任务&nbsp;
            </div>
             --%>
            <div id="dvAll2" class="tab_content_main">
            	<div style="position:absolute; overflow:hidden;" id="dvHead2"></div>
            	<div id="tbAll2">
                    <table class="tab_content_table tc" id="taskOrderTable">
                        <tr id="b2">
                            <th style="width:30px;"></th>
                            <th>任务单号</th>
                            <th >任务单主题</th>
                            <th>创建人</th>
                            <th>创建时间</th>
                            <th>要求完成时间</th>
                            <th>当前责任人</th>
                            <th>任务单状态</th>
                        </tr>
                         <c:forEach var="tk" items="${resultMap.taskOrderList}" varStatus="vs">
                        	<tr>
                        		<td>${vs.index+1}</td>
                        		<td><a href="../${tk.FORMURL}?TOID=${tk.TOID}&WOID=${tk.WOID }" target="_blank">${tk.TOID}</a></td>
                        		<td><span class="ddd_span">${tk.TASKNAME}</span></td>
                        		<td>${tk.ASSIGNEDPERSONID}</td>
                        		<td>${tk.ASSIGNEDTIME}</td>
                        		<td>${tk.OVERTIME}</td>
                        		<td>${tk.OPERATORID}</td>
                        		<td>${tk.STATUSNAME}</td>
                        	</tr>
                        </c:forEach>
                    </table>
                </div>
            </div>
        </div>
      </div>
    </div>
  </body>
</html>
