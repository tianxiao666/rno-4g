<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<table>
 <s:iterator value="currentEntityMap" id="map" status="st">
			                   		<s:if test="key == 'id'">
					                    <input name="${param.currentEntityType}#${key}" type="hidden" value="${value}" />
					                </s:if>
				                	<s:if test="!(key == 'id' || key == '_entityId' || key == '_entityType')">
					                	<tr>
					                	<td>
						                	<label>
							                	<s:if test="currentEntityChineseMap.get(#map.key) == null">
							                		${key}
							                	</s:if>
							                	<s:else>
							                		<s:property value="currentEntityChineseMap.get(#map.key)"/>
							                	</s:else>
							                	:
						                	</label>
						                    <span>
						                    		<s:if test="attrTypeMap.get(#map.key).indexOf('Integer') > -1">
						                    			<s:if test="dropdownListMap.get(#map.key) != null">
								                    				<s:if test='#map.value=="1"'>是</s:if>
								                    				<s:elseif test='#map.value=="0"'>否</s:elseif>
								                    		
						                    			</s:if>
						                    		</s:if>
						                    		<s:else>
						                    			${value}
						                    		</s:else>
						                    </span>
										</td>
										</tr>
					                </s:if>
			               		</s:iterator>
</table>
