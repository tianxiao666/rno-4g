<%@ page language="java" import="java.util.*" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>

<%@ taglib uri="/struts-tags" prefix="s"%>	
<input type="hidden" name="orgId" value="${orgId}" />
<input type="hidden" name="orgName" value="${orgName}" />
<input type="hidden" name="dateString" value="${dateString}" />
<input type="hidden" id="totalPage" value="<s:property value='totalPage'/>" />
        	<table class="search_result_table gps_mileage" id="resultListTable">  
        		 <tr class='thead'>
                  <th colspan="7" style="width:100%">${dateString}${orgName}车辆油费统计</th>
                </tr>
            	 <tr class='thead'>
                  <th style="width:20%">组织名称</th>
                  <th style="width:20%">车牌号</th>
                  <th style="width:10%">油费(元)</th>
                  <th style="width:10%">GPS里程数(公里)</th>
                  <th style="width:10%">GPS油耗(元/公里)</th>
                  <th style="width:10%">仪表读数(公里)</th>
                  <th style="width:20%">仪表里程油耗(元/公里)</th>
                </tr>
                <s:if test="resultMap != null && resultMap.size() > 0">
				<s:iterator id="map" value="resultMap" status="st">
					<tr>
						<td rowspan= "<s:property value="resultMap.get(key).size"/>">${key }</td>
					<s:if test="resultMap.get(key) != null && resultMap.get(key).size() > 0">
	                	<s:iterator id="map" value="resultMap.get(key)" status="st">
	                		<s:if test="st.index>1">
	                			<tr>
	                		</s:if>
	                		
	                		<td>
	                		<a href='cargeneral_index.jsp?carId=<s:property value="#map.CARID"/>&type=view' target='_blank' >
		                		<s:if test="#map.CARNUMBER!=null">
		                			<s:property value="#map.CARNUMBER"/>
		                		</s:if>
		                		<s:else>
		                			&nbsp;
		                		</s:else>
	                		</a>
	                		</td>
	                		<td>
	                			<s:if test="#map.FUELBILLS!=null">
		                			<s:property value="#map.FUELBILLS"/>
		                		</s:if>
		                		<s:else>
		                			0
		                		</s:else>
	                		</td>
	                		<td>
	                		<s:if test="#map.GPSMILEAGE!=null">
	                			<s:property value="#map.GPSMILEAGE"/>
	                		</s:if>
	                		<s:else>
	                			0
	                		</s:else>
	                		</td>
	                		<td>
	                		<s:if test="#map.GPSFUEL!=null">
	                			<s:property value="#map.GPSFUEL"/>
	                		</s:if>
	                		<s:else>
	                			0
	                		</s:else>
	                		</td>
	                		<td>
	                		<s:if test="#map.INSREADING!=null">
	                			<s:property value="#map.INSREADING"/>
	                		</s:if>
	                		<s:else>
	                			0
	                		</s:else>
	                		
	                		</td>
	                		<td>
	                		<s:if test="#map.INSFUEL!=null">
	                			<s:property value="#map.INSFUEL"/>
	                		</s:if>
	                		<s:else>
	                			0
	                		</s:else>
	                		</td>
	                		</tr>
	                    </s:iterator>
	                </s:if>
	                <s:else>
	                	<td>&nbsp;</td>
	                	<td>0</td>
	                	<td>0</td>
	                	<td>0</td>
	                	<td>0</td>
	                	<td>0</td>
	                	</tr>
	                </s:else>
                </s:iterator>
				</s:if>
             </table> 