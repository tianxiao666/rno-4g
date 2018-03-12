<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:set id="a" value="1" />	
<s:iterator id="map" value="searchMap" status="st">
<s:if test="#a % 3 != 0 && #a % 3 == 1">
		       	<li class="up_search">
		       	</s:if>
  			
  				<%-- 获取中文字段名，拿不到拿英文字段名 --%>
                <s:if test="currentEntityChineseMap.get(#map.key) == null">
                	<span class="title_span" title="${key}">
                	${key}：
                </s:if>
                <s:else>
               		 <span class="title_span" title="<s:property value='currentEntityChineseMap.get(#map.key)'/>">
                	<s:property value="currentEntityChineseMap.get(#map.key)"/>：
                </s:else>
            </span>
       		<s:if test="dropdownListMap.get(#map.key) != null">
       			<select class="input_select" style='width:121px' name="${selectResType}#${key}">
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
       		</s:if>
       		<s:else>
       			<s:if test="attrTypeMap.get(#map.key).indexOf('Date') > -1">
       				<input readonly="readonly" class="date" type="text"  id="${key}1" name="${key}" onfocus="var dd=$dp.$('${key}2');WdatePicker({dateFmt:'yyyy-MM-dd',onpicked:function(){${key}2.focus();},maxDate:'#F{$dp.$D(\'${key}2\')}'})" />
       				<a ></a>
       				<i>至</i>
       				<input readonly="readonly"  type="text"  id="${key}2" name="${key}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'${key}1\')}'})"/> 
       			    <a ></a>
       			</s:if>
       			<s:else>
       				<input class="input-text" type="text" name="${selectResType}#${key}" />
       			</s:else>
        											                    			
       		</s:else>	
       	<%--  <s:if test="(#st.index+1)%3 == 0 || #st.index + 1 == currentEntityChineseMap.size()">
	  			<br>
	  		</s:if>	--%>	
	  		<s:if test="#a % 3 == 0">
             	</li>
             </s:if>
             <s:set id="a" value="#a + 1" />
  		</s:iterator>