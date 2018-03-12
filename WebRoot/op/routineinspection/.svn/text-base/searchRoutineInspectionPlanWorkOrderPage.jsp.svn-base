<%@ page language="java" import="java.util.*" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>

<%@ taglib uri="/struts-tags" prefix="s"%>	
        	<table class="thcenter_table tc">
            	<tr>
                	<th>巡检计划名称</th>
                    <th>计划进度</th>
                    <th>实际进度</th>
                    <th>巡检类型</th>
                    <th>巡检组织</th>
                    <th>计划开始时间</th>
                    <th>计划结束时间</th>
                    <th>状态</th>
                    <th>操作</th>
                </tr>
                <s:if test="planWorkOrderList != null && planWorkOrderList.size() > 0">
				<s:iterator id="map" value="planWorkOrderList" status="st">
                <tr>
                    <td><a href="javascript:openPlanWorkOrderInfo('<s:property value='#map.woId'/>');"><s:property value="#map.woTitle"/></a></td>
                    <td><s:property value="#map.designSchedule"/>%</td>
                    <td>
                    	<s:if test="#map.overFlag=='success'">
                			<em class="gantt_show bg_green">
                		</s:if>
                		<s:elseif test="#map.overFlag=='fast'">
                			<em class="gantt_show bg_yellow">
                		</s:elseif>
                		<s:elseif test="#map.overFlag=='over'">
                			<em class="gantt_show bg_red">
                		</s:elseif>
                		<s:else>
                			<em>
                		</s:else>
                    <s:property value="#map.realitySchedule"/>%</em></td>
                    <td><s:property value="#map.type"/></td>
                    <td><s:property value="#map.orgName"/></td>
                    <td><s:property value="#map.planStartTime"/></td>
                    <td><s:property value="#map.planEndTime"/></td>
                    <td><s:property value="#map.statusName"/></td>
                    <td class="tr"><s:if test="#map.status!=21">
                    		<input type="button" value="关闭" onclick="closePlanWorkOrder('<s:property value='#map.woId'/>');" />
                    	</s:if>
                    <input type="button" value="删除" onclick="deletePlanWorkOrder('<s:property value='#map.woId'/>');" /></td>
                </tr>
                </s:iterator>
	</s:if>
            </table>
            <input type="hidden" id="totalPage" value="<s:property value='totalPage'/>" />

