<%@ page language="java" import="java.util.*" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>

<%@ taglib uri="/struts-tags" prefix="s"%>	
			<input type="hidden" id="totalPage" value="<s:property value='totalPage'/>" />
        	<table class="thcenter_table tc">
            	<tr>
                	<th style="width:40px;"><input type="checkbox" /></th>
                	<th>巡检任务名称</th>
                    <th>所属巡检计划</th>
                    <th>巡检组织</th>
                    <th>巡检者</th>
                    <th>计划开始时间</th>
                    <th>计划结束时间</th>
                    <th>偏离位置</th>
                    <th>状态</th>
                </tr>
               <s:if test="taskList != null && taskList.size() > 0">
				<s:iterator id="map" value="taskList" status="st">
                <tr>
                	<td><input type="checkbox" /></td>
                	<td><a href="javascript:openTaskOrderInfo('<s:property value='#map.toId'/>');" ><s:property value="#map.toTitle"/></a>
                		<s:if test="#map.overFlag=='fast'">
                			<em class="small_tip small_tip_almost">快</em>
                		</s:if>
                		<s:elseif test="#map.overFlag=='over'">
                			<em class="small_tip small_tip_over">超</em>
                		</s:elseif>
                	</td>
                	<td><s:property value="#map.planTitle"/></td>
                	<td><s:property value="#map.orgName"/></td>
                	<td><s:property value="#map.currentHandlerName"/></td>
                	<td><s:property value="#map.taskPlanBeginTime" /></td>
                	<td><s:property value="#map.taskPlanEndTime" /></td>
                    <td><s:property value="#map.deviate" /></td>
                	<td class="f-bold"><s:property value="#map.statusName"/></td>
                </tr>
                 </s:iterator>
	</s:if>
            </table>
          

