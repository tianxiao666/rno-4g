<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<input type="hidden" id="totalPage" value="<s:property value='total'/>" />
<table id="resultListTable" class="search_result_table">  
	<tbody>
		<tr class="thead">
	      <th>车辆牌照</th>
	      <th>车辆类型</th>
	      <th>所属组织</th>
	      <th>仪表读数(公里)</th>
	      <th>记录时间</th>
	      <th>录入人</th>
	      <th>录入时间</th>
	      <th>删除人</th>
	      <th>删除时间</th>
	      <th>状态</th>
	      <th>操作</th>
	      
	      <s:if test="pageList != null && pageList.size() > 0">
		<s:iterator id="map" value="pageList" status="st">
				<tr class="pageTr">
				<td><s:property value="#map.carNumber"/></td>
				<td><s:property value="#map.carType"/></td>
				<td><s:property value="#map.orgName"/></td>
				<td><a target="_blank" href="getReadingAndBillsAction?id=<s:property value="#map.instrument_reading_id"/>&distinction=Reading"><s:property value="#map.instrument_reading"/></a></td>
				<td><s:date name="#map.recording_time" format="yyyy-MM-dd" /></td>
				<td><s:property value="#map.create_user"/></td>
				<td><s:date name="#map.create_time" format="yyyy-MM-dd  HH:mm:ss" /></td>
				<td><s:property value="#map.delete_user"/></td>
				<td><s:date name="#map.delete_time" format="yyyy-MM-dd  HH:mm:ss" /></td>
				<s:if test="#map.status == 1">
					<td>正常</td>
					<td><a class="delete_people" href="javascript:deleteReadingOrBills(<s:property value="#map.instrument_reading_id"/>);" ></a></td>
				</s:if>
				<s:else>
					<td style="color: red;">无效</td>
					<td></td>
				</s:else>
			</tr>
		</s:iterator>
	</s:if>
	</tbody>
</table>
