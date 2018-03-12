<%@ page language="java" import="java.util.*" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>

<%@ taglib uri="/struts-tags" prefix="s"%>	

<input type="hidden" id="totalPage" value="<s:property value='totalPage'/>" />
<input type="hidden" id="taskCount" value="<s:property value='taskCount'/>" />
<input type="hidden" id="taskCloseCount" value="<s:property value='taskCloseCount'/>" />
<input type="hidden" id="noStartCount" value="<s:property value='noStartCount'/>" />
<input type="hidden" id="startingCount" value="<s:property value='startingCount'/>" />
<input type="hidden" id="averageDeviate" value="<s:property value='averageDeviate'/>" />
<table class="thcenter_table tc" id="taskList">
    <tr>
        <th style="text-align:center;">巡检任务名称</th>
        <%-- 
        <th style="text-align:center;">执行者</th>
         --%>
        <th style="text-align:center;">执行组织</th>
        <th style="text-align:center;">计划开始时间</th>
        <th style="text-align:center;">计划结束时间</th>
        <th style="text-align:center;">实际开始时间</th>
        <th style="text-align:center;">巡检历时</th>
        <th style="text-align:center;">任务状态</th>
        <th style="text-align:center;">终端偏离位置</th>
    </tr>
    <s:if test="taskList != null && taskList.size() > 0">
	<s:iterator id="map" value="taskList" status="st">
    <tr>
        <td><a href="javascript:openTaskOrderInfo('<s:property value='#map.toId'/>');"><s:property value="#map.title"/></a></td>
        <%-- 
        <td><s:property value="#map.currentHandlerName"/></td>
         --%>
        <td><s:property value="#map.orgName"/></td>
        <td><s:property value="#map.taskPlanBeginTime"/></td>
        <s:if test="isOver=='true' ">
        <td class="bg_red">
        </s:if>
        <s:else>
        <td>
        </s:else>
        <s:property value="#map.taskPlanEndTime"/></td>
        <td><s:property value="#map.signInTime"/></td>
        <td><s:property value="#map.take"/></td>
        <td><s:property value="#map.statusName"/></td>
        <s:if test="isDeviate=='true' ">
        <td class="bg_yellow">
        </s:if>
        <s:else>
        <td>
        </s:else>
        <s:property value="#map.deviate"/></td>
    </tr>
    </s:iterator>
	</s:if>
</table>
									