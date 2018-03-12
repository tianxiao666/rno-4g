<%@ page language="java" import="java.util.*" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>

<%@ taglib uri="/struts-tags" prefix="s"%>	
<input type="hidden" id="totalPage" value="<s:property value='totalPage'/>" />
        	<table class="thcenter_table tc">
            	<tr>
                    <th>问题类型</th>
                    <th>问题描述</th>
                    <th>创建时间</th>
                    <th>解决时间</th>
                    <th>当前处理人</th>
                    <th>状态</th>
                    
                </tr>
                <s:if test="routineinspectionQuestionList != null && routineinspectionQuestionList.size() > 0">
				<s:iterator id="map" value="routineinspectionQuestionList" status="st">
                <tr>
                    <td><s:property value="#map.questionType"/></td>
                   	<td><a href="javascript:openQuestionInfo('<s:property value='#map.id'/>','<s:property value='#map.toId'/>');"><s:property value="#map.description"/></a></td>
                    <td><s:property value="#map.createTime"/></em></td>
                    <td><s:property value="#map.handleTime"/></td>
                    <td><s:property value="#map.handlerName"/></td>
                    <td>
                    	<s:if test="#map.isOver==0">
                			未解决
                		</s:if>
                		<s:elseif test="#map.isOver==1">
                			已解决
                		</s:elseif>
                    </td>
                </tr>
                </s:iterator>
				</s:if>
            </table>
           

