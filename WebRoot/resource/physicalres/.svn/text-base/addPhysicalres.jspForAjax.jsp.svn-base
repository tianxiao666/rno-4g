<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">
	$(".main-table1 .hide-show-img").click(function(){
				if($(this).attr("src") == "image/ico_show.gif"){
					$(this).parent().parent().parent().find("tr").not($(".main-table1-tr")).hide();
					$(this).attr("src","image/ico_hide.gif");
				}else{
					$(this).parent().parent().parent().find("tr").not($(".main-table1-tr")).show();
					$(this).attr("src","image/ico_show.gif");
				}
	});
	$(function(){
			$("#editDiv").html("<input id='btnSubmit' type='submit' value='保存' />&nbsp;&nbsp;<input type='button' value='取消' onclick='cancelOpera();' />");
		
	})
	
</script>
					<table class="main-table1 t1" id="physicalresAttribute">
		                   <tr class="main-table1-tr"><td colspan="2" class="main-table1-title"> <img class="hide-show-img" src="image/ico_show.gif" />基本属性</td></tr>
		                   <s:iterator value="addedResMap" id="map" status="st">
			                	<s:if test="!(key == 'id' || key == '_entityId' || key == '_entityType')">
				                	<tr>
				                		<td class="menuTd">
						                	<%-- 获取中文字段名，拿不到拿英文字段名 --%>
						                	<s:if test="currentEntityChineseMap.get(#map.key) == null">
						                		${key}
						                	</s:if>
						                	<s:else>
						                		<s:property value="currentEntityChineseMap.get(#map.key)"/>
						                	</s:else>
					                	</td>
					                    <td class="tl">
					                    	<%-- 验证是整型的情况 --%>
					                    	<s:if test="attrTypeMap.get(#map.key).indexOf('Integer') > -1">
					                    		<%-- 判断属性的非空情况 --%>
						                    	<s:if test="'true'.equals(nullableMap.get(#map.key))">
						                    		<s:if test="dropdownListMap.get(#map.key) != null">
						                    			<select class="input_select" >
						                    				<option value="" selected="selected" onclick="integerOptionOnclick(this);">请选择</option>
							                    			<s:iterator id="dropdownVal" value="dropdownListMap.get(#map.key)">
							                    				<s:if test='#dropdownVal=="是"'>
								                    						<option  value="1" onclick="integerOptionOnclick(this);">${dropdownVal}</option>          
								                    			</s:if>
								                    			<s:elseif test='#dropdownVal=="否"'>
								                    						<option  value="0" onclick="integerOptionOnclick(this);">${dropdownVal}</option>           
								                    			</s:elseif>
								                    			<s:else>
								                    						<option value="${dropdownVal}" onclick="integerOptionOnclick(this);">${dropdownVal}</option>
								                    			</s:else>
							                    			</s:iterator>
						                    			</select>
						                    			<input  type="hidden" name="${param.addedResEntityType}#${key}" value=''/>
								                    	<input title='请填写：0或者1，0表示“否”，1表示“是”' onblur="integerTextOnblur(this);" onfocus="integerTextOnfocus(this);"  type="text" style="margin-left:-136px;position:relative;z-index=2; width:102px;top:1px;border-bottom:none;" value=""/>
						                    		</s:if>
						                    		<s:else>
						                    			<input title='请填写：整数，如“12”' validateDigit="#${param.currentEntityType}_${key}" id="${param.currentEntityType}_${key}" name="${param.addedResEntityType}#${key}" type="text" />
						                    		</s:else>
						                    	</s:if>
						                    	<s:else>
						                    		<s:if test="dropdownListMap.get(#map.key) != null">
						                    			<select class="input_select required">
						                    				<option value="" selected="selected" onclick="integerOptionOnclick(this);">请选择</option>
							                    			<s:iterator id="dropdownVal" value="dropdownListMap.get(#map.key)">
							                    				<s:if test='#dropdownVal=="是"'>
								                    						<option  value="1" onclick="integerOptionOnclick(this);">${dropdownVal}</option>          
								                    			</s:if>
								                    			<s:elseif test='#dropdownVal=="否"'>
								                    						<option  value="0" onclick="integerOptionOnclick(this);">${dropdownVal}</option>           
								                    			</s:elseif>
								                    			<s:else>
								                    						<option value="${dropdownVal}" onclick="integerOptionOnclick(this);">${dropdownVal}</option>
								                    			</s:else>
							                    			</s:iterator>
						                    			</select>
						                    			<input  type="hidden" name="${param.addedResEntityType}#${key}" value=''/>
								                    	<input title='请填写：0或者1，0表示“否”，1表示“是”' onblur="integerTextOnblur(this);" onfocus="integerTextOnfocus(this);"  type="text" style="margin-left:-136px;position:relative;z-index=2; width:102px;top:1px;border-bottom:none;" value=""/>
						                    		</s:if>
						                    		<s:else>
						                    			<input title='请填写：整数，如“12”' class="required" validateDigit="#${param.currentEntityType}_${key}" id="${param.currentEntityType}_${key}" name="${param.addedResEntityType}#${key}" type="text" />
						                    		</s:else>
						                    		<span class="redStar">*</span>
						                    	</s:else>
						                    	<s:if test="dropdownListMap.get(#map.key) != null">
						                    		<em id='manageModel' style="margin-left: 22px;color:#999">(整数)</em>
						                    	</s:if>
						                    	<s:else>
						                    		<em id='manageModel' style="color:#999">(整数)</em>
						                    	</s:else>
					                    	</s:if>
					                    	<%-- 验证浮点型的情况 --%>
					                    	<s:elseif test="attrTypeMap.get(#map.key).indexOf('Double') > -1  || attrTypeMap.get(#map.key).indexOf('Float') > -1">
					                    		<%-- 判断属性的非空情况 --%>
						                    	<s:if test="'true'.equals(nullableMap.get(#map.key))">
						                    		<s:if test="dropdownListMap.get(#map.key) != null">
						                    			<select class="input_select" >
						                    				<option value="" selected="selected" onclick="optionOnclick(this);">请选择</option>
							                    			<s:iterator id="dropdownVal" value="dropdownListMap.get(#map.key)">
							                    				<option value="${dropdownVal}" onclick="optionOnclick(this);">${dropdownVal}</option>
							                    			</s:iterator>
						                    			</select>
								                    	<input title='请填写：数值，如“1232.23”，“12.03”' class="number"  name="${param.addedResEntityType}#${key}" type="text" style="margin-left:-136px;position:relative;z-index=2; width:102px;top:1px;border-bottom:none;" value=""/>							                    										                    				
						                    		</s:if>
						                    		<s:else>
						                    			<input title='请填写：数值，如“1232.23”，“12.03”' class="number" name="${param.addedResEntityType}#${key}" type="text" />
						                    		</s:else>
						                    	</s:if>
						                    	<s:else>
						                    		<s:if test="dropdownListMap.get(#map.key) != null">
						                    			<select class="input_select required">
						                    				<option value="" selected="selected" onclick="optionOnclick(this);">请选择</option>
							                    			<s:iterator id="dropdownVal" value="dropdownListMap.get(#map.key)">
							                    				<option value="${dropdownVal}" onclick="optionOnclick(this);">${dropdownVal}</option>
							                    			</s:iterator>
						                    			</select>
								                    	<input title='请填写：数值，如“1232.23”，“12.03”' class="number"  name="${param.addedResEntityType}#${key}" type="text" style="margin-left:-136px;position:relative;z-index=2; width:102px;top:1px;border-bottom:none;" value=""/>							                    										                    				
						                    		</s:if>
						                    		<s:else>
						                    			<input  title='请填写：数值，如“1232.23”，“12.03”' class="number required" name="${param.addedResEntityType}#${key}" type="text" />
						                    		</s:else>
						                    		<span class="redStar">*</span>
						                    	</s:else>
						                    	<s:if test="dropdownListMap.get(#map.key) != null">
						                    		<em id='manageModel' style="margin-left: 22px;color:#999">(数值)</em>
						                    	</s:if>
						                    	<s:else>
						                    		<em id='manageModel' style="color:#999">(数值)</em>
						                    	</s:else>
					                    	</s:elseif>
					                    	<%-- 验证日期类型的情况 --%>
					                    	<s:elseif test="attrTypeMap.get(#map.key).indexOf('Date') > -1">
					                    		<%-- 判断属性的非空情况 --%>
						                    	<s:if test="'true'.equals(nullableMap.get(#map.key))">
						                    		<input title='请填写：日期时间，如“2012-10-09”，“2012-10-09 11:02:30”' readonly="readonly" name="${param.addedResEntityType}#${key}" type="text" 
					                    			class="input_text" id="time<s:property value='#st.index' />" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
						                    	</s:if>
						                    	<s:else>
						                    		<input title='请填写：日期时间，如“2012-10-09”，“2012-10-09 11:02:30”' readonly="readonly" name="${param.addedResEntityType}#${key}" type="text" 
					                    			class="input_text required" id="time<s:property value='#st.index' />" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
						                    		<span class="redStar">*</span>
						                    	</s:else>
					                    		<%--<input type="button" class="input_button" value="选择时间" 
														onclick=fPopCalendar(event,document.getElementById('time<s:property value="#st.index" />'),document.getElementById('time<s:property value="#st.index" />'),true) />  --%>
					                    	</s:elseif>
					                    	<%-- 验证其他类型的情况 --%>
					                    	<s:else>
					                    		<%-- 判断属性的非空情况 --%>
						                    	<s:if test="'true'.equals(nullableMap.get(#map.key))">
						                    		<s:if test="dropdownListMap.get(#map.key) != null">
						                    			<select class="input_select">
						                    				<option value="" selected="selected" onclick="optionOnclick(this);">请选择</option>
							                    			<s:iterator id="dropdownVal" value="dropdownListMap.get(#map.key)">
							                    				<option value="${dropdownVal}" onclick="optionOnclick(this);">${dropdownVal}</option>
							                    			</s:iterator>
						                    			</select>
								                    	<input  title='请填写：字符串' name="${param.addedResEntityType}#${key}" type="text" style="margin-left:-136px;position:relative;z-index=2; width:102px;top:1px;border-bottom:none;" value=""/>							                    										                    				
						                    		</s:if>
						                    		<s:else>
						                    			<s:if test="attrLengthMap.get(#map.key)>=100">
							                    				<textarea rows="5" title='请填写：字符串' name="${param.addedResEntityType}#${key}"></textarea>
							                    			</s:if>
							                    			<s:else>
							                    				<input title='请填写：字符串' name="${param.addedResEntityType}#${key}" type="text"  />							                    			
							                    			</s:else>
						                    		</s:else>
						                    	</s:if>
						                    	<s:else>
						                    		<s:if test="dropdownListMap.get(#map.key) != null">
						                    			<select class="input_select required">
						                    				<option value="" selected="selected" onclick="optionOnclick(this);">请选择</option>
							                    			<s:iterator id="dropdownVal" value="dropdownListMap.get(#map.key)">
							                    				<option value="${dropdownVal}" onclick="optionOnclick(this);">${dropdownVal}</option>
							                    			</s:iterator>
						                    			</select>
								                    	<input class="required" title='请填写：字符串' name="${param.addedResEntityType}#${key}" type="text" style="margin-left:-136px;position:relative;z-index=2; width:102px;top:1px;border-bottom:none;" value=""/>							                    										                    				
						                    		</s:if>
						                    		<s:else>
						                    			<s:if test="attrLengthMap.get(#map.key)>=100">
							                    				<textarea rows="5" title='请填写：字符串' class="required" name="${param.addedResEntityType}#${key}"></textarea>
							                    		</s:if>
							                    		<s:else>
							                    				<input title='请填写：字符串' class="required" name="${param.addedResEntityType}#${key}" type="text" />							                    			
							                    		</s:else>
						                    		</s:else>
						                    		<span class="redStar">*</span>
						                    	</s:else>
					                    	</s:else>
					                    </td>
					                </tr>
				                </s:if>
		               		</s:iterator>
		             </table>
