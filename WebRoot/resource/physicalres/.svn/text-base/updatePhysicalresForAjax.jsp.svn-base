<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><div class="tab_container" id="infoMap_div">
<script type="text/javascript">

	$(function(){
		var image =new Image(); 
		$(".photoLi").each(function(){
			var imgsrc= $(this).children().eq(0).attr("src");
			image.src = imgsrc;
			var height = 350
			var width=(height*parseInt(image.width))/parseInt(image.height);
			$(this).children().eq(0).attr("height",height).attr("width",width);
		})
	})
</script>
            	<div class="tab_nav clearfix" id="info_tab_ul">
            		<ul>
                    	<li class="selected" onclick="showInfoMap(this);">基本属性</li>
                    	<s:if test="#request.hasPhoto!='no'">
								<li id="photo_li" onclick="showPhotoDiv(this);">实景照片</li>
								<s:if test="#request.currentEntityType=='DDF'||#request.currentEntityType=='ODF'||#request.currentEntityType=='FiberCrossCabinet'||#request.currentEntityType=='FiberDistributionCabinet'||#request.currentEntityType=='FiberTerminalCase' ||#request.currentEntityType=='BaseStation_GSM'||#request.currentEntityType=='PrimaryEquipFrame_GSM'">
								<li onclick="showPanel(this);">面板图</li>
								</s:if>
								<%--<s:else>
								<li onclick="showLayout(this);">平面布局图</li>
								</s:else>--%>
							</s:if>
							<s:else>
								<s:if test="#request.currentEntityType=='DDF'||#request.currentEntityType=='ODF'||#request.currentEntityType=='FiberCrossCabinet'||#request.currentEntityType=='FiberDistributionCabinet'||#request.currentEntityType=='FiberTerminalCase' ||#request.currentEntityType=='BaseStation_GSM'||#request.currentEntityType=='PrimaryEquipFrame_GSM'">
								<li onclick="showPanel(this);">面板图</li>
								</s:if>
								<%--<s:else>
								<li onclick="showLayout(this);">平面布局图</li>
								</s:else>--%>
							</s:else>
                    </ul>
                </div>
                <div class="tab_content" style="display:block;" id="infoMap_tab_content">
                	<h4 class="table_top">设备属性列表：
                    	<span class="ab_right"><input type="button" id="op_update" value="修改" onclick="openhideinfoMap(this);"/>
                    	<input type="button" id="op_create" value="取消" style="display: none;" onclick="showInfoMap(this);"/>
                    	<input type="submit" id="op_submit" value="保存" style="display: none;"/>
                    	</span>
                    	
                    </h4>
                    
                    <s:set id="a" value="1" />
                    <input type="hidden" name="updatedEntityType" value="${param.currentEntityType}" />
       				   <input name="operatedCurrentEntityId" value="${param.currentEntityId}" type="hidden" />
        			   <input name="operatedCurrentEntityType" value="${param.currentEntityType}" type="hidden" />
                    <input id="chosenEntityType" type="hidden" value="${param.currentEntityType}"/>
					<input id="chosenEntityId" type="hidden"  value="${requestScope.currentEntityId}"/>
					<input id="addedResParentEntityType" name="addedResParentEntityType" type="hidden" />
					<input id="addedResParentEntityId" name="addedResParentEntityId" type="hidden" />
					<input id="addedResEntityType" name="addedResEntityType" type="hidden" />
		        	<input type="hidden" id="bizModule" name="bizModule" value="${bizModule }"/>
					<input type="hidden" id="bizProcessCode" name="bizProcessCode" value="${bizProcessCode }"/>
					<input type="hidden" id="bizProcessId" name="bizProcessId" value="${bizProcessId }"/>
		        	<input id="oldParentResEntityId" name="oldParentResEntityId" type="hidden" value="${parentEntityMap.id}"/>
		        	<input id="oldParentResEntityType" name="oldParentResEntityType" type="hidden" value="${parentEntityMap._entityType}"/>
		        	<input id="newParentResEntityId" name="newParentResEntityId" type="hidden" />
		        	<input id="newParentResEntityType" name="newParentResEntityType" type="hidden" />
                    <table class="source_table2">
                    	<s:iterator value="currentEntityMap" id="map" status="st">
	                   		<s:if test="key == 'id'">
			                    <input name="${param.currentEntityType}#${key}" type="hidden" value="${value}" />
			                    
			                </s:if>
		                	<s:if test="!(key == 'id' || key == '_entityId' || key == '_entityType')">
							
								<s:if test="#a % 2 != 0">
									<tr>
								</s:if>
			                	<s:else>
			                	</s:else>
				                	<td class="left_td">
					                	<%-- 获取中文字段名，拿不到拿英文字段名 --%>
					                	<s:if test="currentEntityChineseMap.get(#map.key) == null">
					                		${key}
					                	</s:if>
					                	<s:else>
					                		<s:property value="currentEntityChineseMap.get(#map.key)"/>
					                	</s:else>
				                	</td>
				                    <td><span class="manageModel" style="display:none">
				                    	<%-- 验证是整型的情况 --%>
				                    	<s:if test="attrTypeMap.get(#map.key).indexOf('Integer') > -1">
				                    		<%-- 判断属性的非空情况 --%>
					                    	<s:if test="'true'.equals(nullableMap.get(#map.key))">
					                    		<s:if test="dropdownListMap.get(#map.key) != null">
					                    			<select  class="input_select" onchange="integerOptionOnclick(this);">
					                    				
					                    				<option value="" selected="selected">请选择</option>
					                    				
						                    			<s:iterator id="dropdownVal" value="dropdownListMap.get(#map.key)">
						                    				<s:if test='#dropdownVal=="是"'>
						                    					<s:if test='"1"==#map.value'>
						                    						<option selected="selected" value="1">${dropdownVal}</option> 
						                    					</s:if> 
						                    					<s:else>
						                    						<option  value="1" >${dropdownVal}</option> 
						                    					</s:else>          
						                    				</s:if>
						                    				<s:elseif test='#dropdownVal=="否"'>
						                    					<s:if test='"0"==#map.value'>
						                    						<option selected="selected" value="0" >${dropdownVal}</option> 
						                    					</s:if> 
						                    					<s:else>
						                    						<option  value="0" >${dropdownVal}</option> 
						                    					</s:else>          
						                    				</s:elseif>
						                    				<s:else>
						                    					<s:if test="#dropdownVal.equals(#map.value)">
								                    				<option selected="selected" value="${dropdownVal}" >${dropdownVal}</option>
							                    				</s:if>
							                    				<s:else>
							                    					<option value="${dropdownVal}" >${dropdownVal}</option>
							                    				</s:else>
						                    				</s:else>
						                    			</s:iterator>
					                    			</select>
						                    		<input  type="hidden" name="${param.currentEntityType}#${key}" value='${value}'/>
						                    		<s:if test='"1"==#map.value'>
						                    			<input id="manageModel_input" title='请填写：0或者1，0表示“否”，1表示“是”' onblur="integerOptionOnclick(this);" onfocus="integerTextOnfocus(this);"  type="text" style="margin-left:-139px;position:relative;z-index=2;width:105px;top:1px;border-bottom:none;" value="是"/>
						                    		</s:if> 
						                    		<s:elseif test='"0"==#map.value'>
						                    			<input id="manageModel_input" title='请填写：0或者1，0表示“否”，1表示“是”' onblur="integerOptionOnclick(this);" onfocus="integerTextOnfocus(this);"  type="text" style="margin-left:-139px;position:relative;z-index=2;width:105px;top:1px;border-bottom:none;" value="否"/>
						                    		</s:elseif>
						                    		<s:elseif test="''==#map.value" >
						                    			<input id="manageModel_input" title='请填写：0或者1，0表示“否”，1表示“是”' onblur="integerOptionOnclick(this);" onfocus="integerTextOnfocus(this);"  type="text" style="margin-left:-139px;position:relative;z-index=2;width:105px;top:1px;border-bottom:none;" value=""/>
						                    		</s:elseif>
						                    		<s:else>
						                    			<input id="manageModel_input" title='请填写：0或者1，0表示“否”，1表示“是”' onblur="integerOptionOnclick(this);" onfocus="integerTextOnfocus(this);"  type="text" style="margin-left:-139px;position:relative;z-index=2;width:105px;top:1px;border-bottom:none;" value="${value}"/>
						                    		</s:else>			           		
					                    		</s:if>
					                    		<s:else>
					                    			<input id="manageModel_input" title='请填写：整数，如“12”'  validateDigit="#${param.currentEntityType}_${key}" id="${param.currentEntityType}_${key}" name="${param.currentEntityType}#${key}" type="text" value="${value}" />
					                    		</s:else>
					                    		
					                    	</s:if>
					                    	<s:else>
					                    		<s:if test="dropdownListMap.get(#map.key) != null">
					                    			<select  class="input_select required" onchange="integerOptionOnclick(this);">
					                    				<option value="" selected="selected" >请选择</option>
					                    				
						                    			<s:iterator id="dropdownVal" value="dropdownListMap.get(#map.key)">
						                    				<s:if test='#dropdownVal=="是"'>
						                    					<s:if test='"1"==#map.value'>
						                    						<option selected="selected" value="1" >${dropdownVal}</option> 
						                    					</s:if> 
						                    					<s:else>
						                    						<option  value="1" >${dropdownVal}</option> 
						                    					</s:else>          
						                    				</s:if>
						                    				<s:elseif test='#dropdownVal=="否"'>
						                    					<s:if test='"0"==#map.value'>
						                    						<option selected="selected" value="0" >${dropdownVal}</option> 
						                    					</s:if> 
						                    					<s:else>
						                    						<option  value="0" >${dropdownVal}</option> 
						                    					</s:else>          
						                    				</s:elseif>
						                    				<s:else>
						                    					<s:if test="#dropdownVal.equals(#map.value)">
								                    				<option selected="selected" value="${dropdownVal}" >${dropdownVal}</option>
							                    				</s:if>
							                    				<s:else>
							                    					<option value="${dropdownVal}" >${dropdownVal}</option>
							                    				</s:else>
						                    				</s:else>
						                    			</s:iterator>
					                    			</select>
						                    		<input  type="hidden" name="${param.currentEntityType}#${key}" value='${value}'/>
						                    		<s:if test='"1"==#map.value'>
						                    			<input id="manageModel_input" title='请填写：0或者1，0表示“否”，1表示“是”' onblur="integerOptionOnclick(this);" onfocus="integerTextOnfocus(this);"  type="text" style="margin-left:-139px;position:relative;z-index=2;width:105px;top:1px;border-bottom:none;" value="是"/>
						                    		</s:if> 
						                    		<s:elseif test='"0"==#map.value'>
						                    			<input id="manageModel_input" title='请填写：0或者1，0表示“否”，1表示“是”' onblur="integerOptionOnclick(this);" onfocus="integerTextOnfocus(this);"  type="text" style="margin-left:-139px;position:relative;z-index=2;width:105px;top:1px;border-bottom:none;" value="否"/>
						                    		</s:elseif>
						                    		<s:elseif test="''==#map.value" >
						                    			<input id="manageModel_input" title='请填写：0或者1，0表示“否”，1表示“是”' onblur="integerOptionOnclick(this);" onfocus="integerTextOnfocus(this);"  type="text" style="margin-left:-139px;position:relative;z-index=2;width:105px;top:1px;border-bottom:none;" value=""/>
						                    		</s:elseif>
						                    		<s:else>
						                    			<input id="manageModel_input" title='请填写：0或者1，0表示“否”，1表示“是”' onblur="integerOptionOnclick(this);" onfocus="integerTextOnfocus(this);"  type="text" style="margin-left:-139px;position:relative;z-index=2;width:105px;top:1px;border-bottom:none;" value="${value}"/>
						                    		</s:else>				           	
					                    		</s:if>
					                    		<s:else>
					                    			<input id="manageModel_input" title='请填写：整数，如“12”' class="required" validateDigit="#${param.currentEntityType}_${key}" id="${param.currentEntityType}_${key}" name="${param.currentEntityType}#${key}" type="text" value="${value}" />
					                    		</s:else>
					                    		<span class="redStar">*</span>
					                    	</s:else>
					                    	<s:if test="dropdownListMap.get(#map.key) != null">
					                    		
					                    	</s:if>
					                    	<s:else>
					                    		<em id='manageModel' style="color:#999">(整数)</em>
					                    	</s:else>
				                    	</s:if>
				                    	<%-- 验证浮点型的情况 --%>
				                    	<s:elseif test="attrTypeMap.get(#map.key).indexOf('Double') > -1 || attrTypeMap.get(#map.key).indexOf('Float') > -1">
				                    		<%-- 判断属性的非空情况 --%>
					                    	<s:if test="'true'.equals(nullableMap.get(#map.key))">
					                    		<s:if test="dropdownListMap.get(#map.key) != null">
					                    			<select class="input_select" onchange="optionOnclick(this);">
					                    				<option value="" selected="selected">请选择</option>
						                    			<s:iterator id="dropdownVal" value="dropdownListMap.get(#map.key)">
						                    				<s:if test="#dropdownVal.equals(#map.value)">
							                    				<option selected="selected" value="${dropdownVal}">${dropdownVal}</option>
						                    				</s:if>
						                    				<s:else>
						                    					<option value="${dropdownVal}" >${dropdownVal}</option>
						                    				</s:else>
						                    			</s:iterator>
					                    			</select>	
					                    			<s:if test="#map.value==''">
						                    			<input id="manageModel_input" title='请填写：数值，如“1232.23”，“12.03”' class="number"   name="${param.currentEntityType}#${key}" type="text" style="margin-left:-139px;position:relative;z-index=2;width:105px;top:1px;border-bottom:none;" value=""/>							                    										                    				
					                    			</s:if>		
					                    			<s:else>
						                    			<input id="manageModel_input" title='请填写：数值，如“1232.23”，“12.03”' class="number"  name="${param.currentEntityType}#${key}" type="text" style="margin-left:-139px;position:relative;z-index=2;width:105px;top:1px;border-bottom:none;" value="${value}"/>							                    										                    											                    				
					                    			</s:else>				                    				
					                    		</s:if>
					                    		<s:else>
					                    			<input id="manageModel_input" title='请填写：数值，如“1232.23”，“12.03”' class="number" name="${param.currentEntityType}#${key}" type="text" value="${value}" />
					                    		</s:else>
					                    	</s:if>
					                    	<s:else>
					                    		<s:if test="dropdownListMap.get(#map.key) != null">
					                    			<select class="input_select required"  onchange="optionOnclick(this);">
					                    				<option value="" selected="selected">请选择</option>
						                    			<s:iterator id="dropdownVal" value="dropdownListMap.get(#map.key)">
						                    				<s:if test="#dropdownVal.equals(#map.value)">
							                    				<option selected="selected" value="${dropdownVal}" >${dropdownVal}</option>
						                    				</s:if>
						                    				<s:else>
						                    					<option value="${dropdownVal}" >${dropdownVal}</option>
						                    				</s:else>
						                    			</s:iterator>
					                    			</select>
					                    			<s:if test="#map.value==''">
						                    			<input id="manageModel_input" title='请填写：数值，如“1232.23”，“12.03”' class="number required "  name="${param.currentEntityType}#${key}" type="text" style="margin-left:-139px;position:relative;z-index=2;width:105px;top:1px;border-bottom:none;" value=""/>							                    										                    				
					                    			</s:if>		
					                    			<s:else>
						                    			<input id="manageModel_input" title='请填写：数值，如“1232.23”，“12.03”' class="number required "  name="${param.currentEntityType}#${key}" type="text" style="margin-left:-139px;position:relative;z-index=2;width:105px;top:1px;border-bottom:none;" value="${value}"/>							                    										                    											                    				
					                    			</s:else>							                    		</s:if>
					                    		<s:else>
					                    			<input id="manageModel_input" title='请填写：数值，如“1232.23”，“12.03”' class="number required " name="${param.currentEntityType}#${key}" type="text" value="${value}" />
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
					                    		<input title='请填写：日期时间，如“2012-10-09”，“2012-10-09 11:02:30”' readonly="readonly" name="${param.currentEntityType}#${key}" type="text" value="${value}" 
				                    			class="input_text timeinput" id="time<s:property value='#st.index' />" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
					                    	</s:if>
					                    	<s:else>
					                    		<input title='请填写：日期时间，如“2012-10-09”，“2012-10-09 11:02:30”' readonly="readonly" name="${param.currentEntityType}#${key}" type="text" value="${value}" 
				                    			class="input_text required timeinput" id="time<s:property value='#st.index'/>"   onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
					                    		<span class="redStar">*</span>
					                    	</s:else>
				                    		<%--  <input type="button" class="input_button"  value="选择时间" onclick=fPopCalendar(event,document.getElementById('time<s:property value="#st.index" />'),document.getElementById('time<s:property value="#st.index" />'),true) />
													--%>
											
				                    	</s:elseif>
				                    	<%-- 验证其他类型的情况 --%>
				                    	<s:else>
				                    		<%-- 判断属性的非空情况 --%>
					                    	<s:if test="'true'.equals(nullableMap.get(#map.key))">
					                    		<s:if test="dropdownListMap.get(#map.key) != null">
					                    			<select class="input_select" onchange="optionOnclick(this);">
					                    				<option value="" selected="selected">请选择</option>
						                    			<s:iterator id="dropdownVal" value="dropdownListMap.get(#map.key)">
						                    				<s:if test="#dropdownVal.equals(#map.value)">
							                    				<option selected="selected" value="${dropdownVal}">${dropdownVal}</option>
						                    				</s:if>
						                    				<s:else>
						                    					<option value="${dropdownVal}" >${dropdownVal}</option>
						                    				</s:else>
						                    			</s:iterator>
					                    			</select>
					                    			<s:if test="#map.value==''">
						                    			<input id="manageModel_input" title='请填写：字符串' name="${param.currentEntityType}#${key}" type="text" style="margin-left:-139px;position:relative;z-index=2;width:105px;top:1px;border-bottom:none;" value=""/>							                    										                    				
					                    			</s:if>		
					                    			<s:else>
						                    			<input id="manageModel_input" title='请填写：”字符串”' name="${param.currentEntityType}#${key}" type="text" style="margin-left:-139px;position:relative;z-index=2;width:105px;top:1px;border-bottom:none;" value="${value}"/>							                    										                    											                    				
					                    			</s:else>							                    		</s:if>
					                    		<s:else>
					                    			<s:if test="attrLengthMap.get(#map.key)>=100">
					                    				<textarea id="manageModel_input" rows="5" name="${param.currentEntityType}#${key}" title='请填写：字符串”'>${value}</textarea>
					                    			</s:if>
					                    			<s:else>
					                    				<input id="manageModel_input" title='请填写：字符串' name="${param.currentEntityType}#${key}" type="text" value="${value}" />							                    			
					                    			</s:else>
					                    			
					                    		</s:else>
					                    	</s:if>
					                    	<s:else>
					                    		<s:if test="dropdownListMap.get(#map.key) != null">
					                    			<select class="input_select required"  onclick="optionOnclick(this);">
					                    				<option value="" selected="selected">请选择</option>
						                    			<s:iterator id="dropdownVal" value="dropdownListMap.get(#map.key)">
						                    				<s:if test="#dropdownVal.equals(#map.value)">
							                    				<option selected="selected" value="${dropdownVal}" >${dropdownVal}</option>
						                    				</s:if>
						                    				<s:else>
						                    					<option value="${dropdownVal}" >${dropdownVal}</option>
						                    				</s:else>
						                    			</s:iterator>
					                    			</select>
					                    			<s:if test="#map.value==''">
						                    			<input id="manageModel_input" title='请填写：”字符串”' class="required"  name="${param.currentEntityType}#${key}" type="text" style="margin-left:-139px;position:relative;z-index=2;width:105px;top:1px;border-bottom:none;" value=""/>							                    										                    				
					                    			</s:if>		
					                    			<s:else>
						                    			<input id="manageModel_input" title='请填写：字符串' class="required"  name="${param.currentEntityType}#${key}" type="text" style="margin-left:-139px;position:relative;z-index=2;width:105px;top:1px;border-bottom:none;" value="${value}"/>							                    										                    											                    				
					                    			</s:else>							                    		</s:if>
					                    		<s:else>
					                    			<s:if test="attrLengthMap.get(#map.key)>=100">
					                    				<textarea id="manageModel_input" title='请填写：”字符串”' rows="5" class="required" name="${param.currentEntityType}#${key}">${value}</textarea>
					                    			</s:if>
					                    			<s:else>
					                    				<input id="manageModel_input" title='请填写：字符串' class="required" name="${param.currentEntityType}#${key}" type="text" value="${value}" />							                    			
					                    			</s:else>
					                    		</s:else>
					                    		<span class="redStar">*</span>
					                    	</s:else>
				                    	</s:else>
				                    	</span>
				                    	<span class="viewModel">
				                    		<s:if test="attrTypeMap.get(#map.key).indexOf('Integer') > -1">
				                    			<s:if test="dropdownListMap.get(#map.key) != null">
						                    				<s:if test='#map.value=="1"'>是</s:if>
						                    				<s:elseif test='#map.value=="0"'>否</s:elseif>
						                    		
				                    			</s:if>
				                    			<s:else>
				                    			${value}
				                    			</s:else>
				                    		</s:if>
				                    		<s:else>
				                    			${value}
				                    		</s:else>
				                    	</span>
				                    </td>
				                <s:if test="#a % 2 == 0">
				                	</tr>
								</s:if>
			                	<s:else>
			                	</s:else>
			                	<s:set id="a" value="#a+1" />
			                </s:if>
	               		</s:iterator>
	               		<tr>
	               		<td  class="left_td">
	               		<h4 style="background: none repeat scroll 0 0 #E8EDFF;line-height: 26px;padding-left: 4px;">隶属： </h4>
			            </td>
	               		<td colspan="3">
	               		<span class="manageModel" style="display:none">
			          		<input type="text"  readonly id="txtParentRes" value="${parentEntityMap.name}"/><a  class="areaButton"></a>
			          	</span>
			          	<span class="viewModel" id="parentTypeName"> 
			          		${parentEntityMap.name}
			          	</span>
			          	</td>
	               		</tr>
                    </table>
                    <script type="text/javascript" src="js/choosResourceByTree.js"></script>
                   <div id="chooseResDiv" style="display:none;border:#ccc 1px solid; border-radius:5px; position:absolute; left:250px;top:139px; background:#f1f1f1; z-index:100">
				    	<input type="hidden" id="hidParentTypeGroup" value="${parentEntityTypeGrp}" />
				    	
				    	<%-- 隶属内容选择层 --%>
				    	<s:if test="#request.areaId!=null&&#request.areaName!=null">
					   		<div id="chooseTree" style="padding:0 8px">
					   			<ul id="chooseResUl" style="width:250px;height:300px;overflow:auto;line-height:16px;">
						        	<li>
							        	<img src="image/plus.gif" onclick="showChooseResTypeMsg(this);" />
							        	<s:if test="parentEntityTypeGrp.indexOf('Sys_Area') > -1">
							        		<input class="input_radio" type="radio" name="rdoChooseResChild" id="rootRadio" />
							        	</s:if>
							        	<span onclick='chooseTreeEntity(this);' id='treeRootName' style="cursor:pointer;" class="entityClass">${request.areaName}</span>
							        	<%-- 选进行区域id的hardcode --%>
							        	<input type="hidden" value="${request.areaId}" />
							        	<input type="hidden" value="Sys_Area" />
							        	<div style="padding-left:10px;display:none;"></div>
							        </li>
					             </ul>
					   			<div style="text-align:center">
						   			<input id="btnChooseParentRes" type="button" value="选择"  />&nbsp;
						   			<input id="btnCancelParentRes" type="button" value="取消" />
					   			</div>
					   		</div>
				   		 </s:if>
				   		 <s:else>
					   		 <div>
					   		 	<ul  style="width:250px;height:300px;overflow:auto;line-height:16px;">
						        	<li>
							        </li>
					             </ul>
					   		 </div>
				   		 </s:else>
				   </div>
                </div>
                <div class="tab_content" id="photo_tab_content">
                	<s:if test="#request.hasPhoto!='no'">
                			<div style="height: 30px;">
                				<input style="float: right;" type="button" value="添加" onclick="addPhoto();"/>
                			</div>
							<div class="tab_2_div" style="height: 400px;">
								<s:if test="#request.currentEntityType =='Photo' ">
									<ul style="overflow-x:auto;width: auto;">
										<li style='text-align:center' class='photoLi'>
						  				<img height="350" width="700" src="../../upload/${currentEntityMap.uuidname}" style=""><br>
						  				<em><em style="font-weight:bold">图片名:</em>${currentEntityMap.name}</em>
						  				</li>
									</ul>
								</s:if>
								<s:else>
									<s:if test="photoMapList!=null">
									<ul style="overflow-x:auto;width: auto;">
										<s:iterator id="pl" value="photoMapList" status="st2">
											<s:if test="#st2.index ==0">
												<li style='text-align:center' class='photoLi'>
								  				<img height="350" width="700" src="../../upload/${pl.uuidname}" style=""><br>
								  				</li>
											</s:if>
											<s:else>
												<li style='text-align:center;display:none' class='photoLi' >
								  				<img height="350" width="700" src="../../upload/${pl.uuidname}" style=""><br>
								  				</li>
											</s:else>			
									  </s:iterator>
									</ul>
									<div class="mt8 red">
										<input type="button" id="view" disabled="disabled" class="prev" value="< 上一张"  onclick="showPhoto('prev');"/>
										<s:set id="pl_a" value="0" />
										<s:iterator id="pl" value="photoMapList" status="st2">
											<s:if test="#st2.index ==0">
								  				<em class="photoEm"><em style="font-weight:bold">图片名:</em>${pl.name}</em>
											</s:if>
											<s:else>
								  				<em class="photoEm" style='display:none'><em style="font-weight:bold">图片名:</em>${pl.name}</em>
											</s:else>	
										<s:set id="pl_a" value="#pl_a+1" />		
									    </s:iterator>
									    <s:if test="#pl_a =< 1">
											<input type="button" id="view" value="下一张 >"  disabled="disabled" class="next" onclick="showPhoto('next');"/>
									    </s:if>
									    <s:else>
									    	<input type="button" id="view" value="下一张 >" class="next" onclick="showPhoto('next');"/>
									    </s:else>
									</div>
									</s:if>
								<s:else>
								无图片资源
								</s:else>
								</s:else>	
							</div>
						</s:if>	
						
                </div>
                <div class="tab_content" style="height: 880px; overflow: auto;" id="panel_tab_content">
                	<s:if test="#request.hasPhoto!='no'">
							<div class="tab_2_div">
								<s:if test="#request.currentEntityType=='ODF'||#request.currentEntityType=='FiberCrossCabinet'||#request.currentEntityType=='FiberDistributionCabinet'||#request.currentEntityType=='FiberTerminalCase'">
									<div id="panels_view" style="background:none repeat scroll 0 0 WhiteSmoke; hidden; height: 880px;">
						             	<div id="messages_view" ></div>
						             	<div id="paneltable_box_view">
							             	<div id="paneltable_title_view"></div>
							                <div id="paneltable_view"></div>
						                </div>
						                <div id="terminalmess"></div>
						             </div>
								</s:if>
								<s:elseif test="#request.currentEntityType=='DDF'||#request.currentEntityType=='BaseStation_GSM'||#request.currentEntityType=='PrimaryEquipFrame_GSM'">
								<div id='boardPanel' style=" height: 880px;"></div>
								</s:elseif>
								<s:else>
									功能待开发
								</s:else>
							</div>
						</s:if>
						<s:else>
							<div class="tab_2_div">
								<s:if test="#request.currentEntityType=='ODF'||#request.currentEntityType=='FiberCrossCabinet'||#request.currentEntityType=='FiberDistributionCabinet'||#request.currentEntityType=='FiberTerminalCase'">
									<div id="panels_view" style="background:none repeat scroll 0 0 WhiteSmoke; hidden; height: 880px;">
						             	<div id="messages_view" ></div>
						             	<div id="paneltable_box_view">
							             	<div id="paneltable_title_view"></div>
							                <div id="paneltable_view"></div>
						                </div>
						                <div id="terminalmess"></div>
						             </div>
								</s:if>
								<s:elseif test="#request.currentEntityType=='DDF'||#request.currentEntityType=='BaseStation_GSM'||#request.currentEntityType=='PrimaryEquipFrame_GSM'">
								<div id='boardPanel' style=" height: 880px;"></div>
								</s:elseif>
								<s:else>
									功能待开发
								</s:else>
							</div>
						</s:else>
                </div>
                <div class="tab_content" id="layout_tab_content">
                	<div class="tab_2_div"  id="layout_tab_content_tab_2_div">
                	</div>
                </div>
                
                <div class="tab_content" id="order_tab_content">
                	<h4 class="flattening_info_title mb10">生产工单
                        	<input type="checkbox" checked="checked" id="currentWorkOrder"/>当前工单
                            <input type="checkbox"  id="historyWorkOrder"/>历史工单
                            <span class="absolute_right top_0">
                            	<input type="text" id="workOrderSearchCondition" class="search_textinput view" value="请输入查询条件" onfocus="selectInputfocus(this);" style="color:#999;"/>
                                <input type="button" value="查询" id="view" onclick="searchOrderList('workOrder');"/>
                            </span>
                        </h4>
                        <div class="table_container">
                       <%--<div class="flattening_loading" id="workOrderLoading" style='display:none'><em>数据载入中</em><p></p></div>  --%> 
                        <div class='flattening_loading' id="workOrderLoading"  style='display:none;text-align:center'><img src='image/loading_img.gif'><br>数据处理中，请稍侯...</div>
                        
                            <table class="flattening_table" id="urTable">
                                
                            </table>
                        </div>
                        <div id='workOrder_paging_div'>
                        
                        </div>
                        
                        
                        <h4 class="flattening_info_title mb10">生产任务
                        	<input type="checkbox" checked="checked" id="currentTaskOrder" />当前任务
                            <input type="checkbox" id="historyTaskOrder"/>历史任务
                            <span class="absolute_right top_0">
                            	<input type="text" class="search_textinput view" value="请输入查询条件" onfocus="selectInputfocus(this);" id="taskOrderSearchCondition" style="color:#999;" />
                                <input type="button" value="查询" id="view" onclick="searchOrderList('taskOrder');"/>
                            </span>
                        </h4>
                        <div class="table_container">
                        	<%--<div class="flattening_loading" id="taskOrderLoading" style='display:none'><em>数据载入中</em><p></p></div>  --%>
                        	 <div class='flattening_loading' id="taskOrderLoading"  style='display:none;text-align:center'><img src='image/loading_img.gif'><br>数据处理中，请稍侯...</div>
                            <table class="flattening_table" id="toTable">
                                
                            </table>
                        </div>
                         <div id='taskOrder_paging_div'>
                        
                        </div>
                </div>
                
                
                <div class="tab_content" id="flattening_service_tab_content">
					<div class="flattening_service_tab">维护与服务记录</div>
					<div class="flattening_service_info flattening_info_box clearfix" style="height: 764px;">
						<div class="flattening_service_left">
							<div class="flattening_service_left_t" >发生日期：<a href="#" onclick="getMaintenance(); " id="All_Time">全部</a><a href="#" class="selected" onclick="getMaintenanceByMonth();" id="Latest_Month">近1月内</a><a href="#" onclick="getMaintenanceByYear();" id="Latest_Year">近1年内</a>
							<span id="view" class=" top_0">
								<input class="search_textinput view" id="search_textinput_net" type="text" style="color: #999;"  onfocus="selectInputfocus(this);" value="请输入查询条件">
								<input id="view" class="" type="button" onclick="searchMaintenance(this);" value="查询">
							</span>
							</div>
							<ul class="flattening_service_ul" id="som_info_ul">
								
							</ul>
							
						</div>
						<div class="flattening_service_right">
							<table class="flattening_service_table"  id="flattening_service_right">
							</table>
						</div>
					</div>
                </div>