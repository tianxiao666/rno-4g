<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<tbody>
  			<tr>
  		<s:iterator id="map" value="searchMap" status="st">
  			<td style='text-align:right'>
  				<%-- 获取中文字段名，拿不到拿英文字段名 --%>
                <s:if test="currentEntityChineseMap.get(#map.key) == null">
                	${key}：
                </s:if>
                <s:else>
                	<s:property value="currentEntityChineseMap.get(#map.key)"/>：
                </s:else>
            </td>
            <td>
						                    		<s:if test="dropdownListMap.get(#map.key) != null">
						                    			<select class="input_select" style='width:110px'>
						                    				<option value="" selected="selected" >请选择</option>
							                    			<s:iterator id="dropdownVal" value="dropdownListMap.get(#map.key)">
							                    				<s:if test='#dropdownVal=="是"'>
								                    						<option  value="1">${dropdownVal}</option>          
								                    			</s:if>
								                    			<s:elseif test='#dropdownVal=="否"'>
								                    						<option  value="0" >${dropdownVal}</option>           
								                    			</s:elseif>
								                    			<s:else>
								                    						<option value="${dropdownVal}" >${dropdownVal}</option>
								                    			</s:else>
								                    			
							                    			</s:iterator>
						                    			</select>
						                    			<input type="hidden" name="${selectResType}#${key}" />
						                    		</s:if>
						                    		<s:else>
						                    			<s:if test="attrTypeMap.get(#map.key).indexOf('Date') > -1">
						                    				<span ><span>从:</span><input readonly="readonly" class="input-text date" type="text" style="width:90px" id="${key}1" name="${key}" onfocus="var dd=$dp.$('${key}2');WdatePicker({dateFmt:'yyyy-MM-dd',onpicked:function(){${key}2.focus();},maxDate:'#F{$dp.$D(\'${key}2\')}'})" />
						                    				<a> </a><BR>
						                    				<span>到:</span><input readonly="readonly" class="input-text" type="text" style="width:90px" id="${key}2" name="${key}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'${key}1\')}'})" /> 
						                    			   <a> </a></span>
						                    			</s:if>
						                    			<s:else>
						                    				<input class="input-text" type="text"  />
						                    				<input type="hidden" name="${selectResType}#${key}" />
						                    			</s:else>
							                    											                    			
						                    		</s:else>
						                    	
					                    	
  					
  				
  			</td>
 		<s:if test="(#st.index+1)%3 == 0 || #st.index + 1 == currentEntityChineseMap.size()">
  			</tr>
  		</s:if>
  		</s:iterator>
</tbody>
