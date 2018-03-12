<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
			function clickShowHide(showId,tableId){
				if($('#'+showId).attr("src") == "image/ico_show.gif"){
					$("#"+tableId+" .right_tab_table_tr").hide();
					$('#'+showId).attr("src","image/ico_hide.gif");
				}else{
					$("#"+tableId+" .right_tab_table_tr").show();
					$('#'+showId).attr("src","image/ico_show.gif");
				}
			}
			
			function clickShowChildRight(currentEntityId,currentEntityType){
				var params = {currentEntityId:currentEntityId,currentEntityType:currentEntityType};
				showElements(currentEntityId,currentEntityType);
			$.post("../physicalres/getPhysicalresAction",params,function(data){
				$("#rightInformation").html(data);
				$("#imgRight").attr("src","image/hide.png");
				$("#rightInformation").show();
			});
			}
			
			function clickShowRight(currentEntityId,currentEntityType){
			var params = null;
			showElements(currentEntityId,currentEntityType);
			if(currentEntityType == "Sys_Area"||currentEntityType == "Station"||currentEntityType == "ManWell"
					||currentEntityType == "Pole"||currentEntityType == "HangWall"||currentEntityType == "MarkPost"
					||currentEntityType == "FiberCrossCabinet"||currentEntityType == "FiberDistributionCabinet"||currentEntityType == "FiberTerminalCase"){
				var params = {currentEntityId:currentEntityId,currentEntityType:currentEntityType};
			$.post("../physicalres/getPhysicalresAction",params,function(data){
				$("#rightInformation").html(data);
				$("#imgRight").attr("src","image/hide.png");
				$("#rightInformation").show();
			});
			}else{
				showLogicalres(currentEntityId,currentEntityType,"view");
			}
			
			}
</script>
<s:if test="associatedResourcCount == 0">
	<span style='color: #999;'>没有相关信息</span>
</s:if>
<s:iterator value="childAssociatedResourcMap" id="vs" status="st2">
	<s:if test="#vs.value != null">
<table id="${vs.key }Table" class="right_tab_table">
	<s:if test="infoChildMapChinese.get(#vs.key) == null">
		<tr>
      <th class="tl" colspan="1"><img src="image/ico_hide.gif" id="${vs.key }hide" class="show_hide_img" onclick="clickShowHide('${vs.key }hide','${vs.key }Table');" />包含<s:property value="#vs.key"/>(<s:property value="#vs.value.size"/>)</th>
    </tr>
	</s:if>
	<s:else>
	<tr>
      <th class="tl" colspan="1"><img src="image/ico_hide.gif" id="${vs.key }hide" class="show_hide_img" onclick="clickShowHide('${vs.key }hide','${vs.key }Table');" />包含<s:property value="infoChildMapChinese.get(#vs.key)"/>(<s:property value="#vs.value.size"/>)</th>
    </tr>
	</s:else>
	<s:iterator value="#vs.value" id="vsvalue" status="st3">
	<tr class="right_tab_table_tr" style="display: none;">
		<td><a href="javascript:clickShowChildRight(<s:property value="#vsvalue.get('id')"/>, '<s:property value="#vsvalue.get('_entityType')"/>');" >
		<s:if test="#vsvalue.get('name') != null && #vsvalue.get('name') != '' && #vsvalue.get('name') != 'null'">
			<s:property value="#vsvalue.get('name')"/>
		</s:if>
		<s:else>
			<s:property value="#vsvalue.get('label')"/>
		</s:else>
		</a></td>
		</tr>
	</s:iterator>
</table>
	</s:if>
</s:iterator>
<s:iterator value="linkAssociatedResourcMap" id="vs" status="st2">
	<s:if test="#vs.value != null">
<table id="${vs.key }Table" class="right_tab_table">
	<s:if test="infoLinkMapChinese.get(#vs.key) == null">
		<tr>
      <th class="tl" colspan="1"><img src="image/ico_hide.gif" id="${vs.key }hide" class="show_hide_img" onclick="clickShowHide('${vs.key }hide','${vs.key }Table');" />关联<s:property value="#vs.key"/>(<s:property value="#vs.value.size"/>)</th>
    </tr>
	</s:if>
	<s:else>
	<tr>
      <th class="tl" colspan="1"><img src="image/ico_hide.gif" id="${vs.key }hide" class="show_hide_img" onclick="clickShowHide('${vs.key }hide','${vs.key }Table');" />关联<s:property value="infoLinkMapChinese.get(#vs.key)"/>(<s:property value="#vs.value.size"/>)</th>
    </tr>
	</s:else>
	<s:iterator value="#vs.value" id="vsvalue" status="st3">
	<tr class="right_tab_table_tr" style="display: none;">
		<td><a href="javascript:clickShowRight(<s:property value="#vsvalue.get('id')"/>, '<s:property value="#vsvalue.get('_entityType')"/>','view');" >
		<s:if test="#vsvalue.get('name') != null && #vsvalue.get('name') != '' && #vsvalue.get('name') != 'null'">
			<s:property value="#vsvalue.get('name')"/>
		</s:if>
		<s:else>
			<s:property value="#vsvalue.get('label')"/>
		</s:else>
		</a></td>
		</tr>
	</s:iterator>
</table>
	</s:if>
</s:iterator>

