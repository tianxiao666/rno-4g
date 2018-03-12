<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<input type="hidden" id="totalPage" value="<s:property value='totalPage'/>" />
<table  class="main_table1 tc">
									
	<tr>
		<th></th>
		<th>姓名</th>
		<th>IT帐号</th>
		<th>IT用户类型</th>
		<th>性别</th>
		<th>手机号码</th>
		<th>出生年月</th>
	</tr>
	<s:if 
		test="staffList != null && staffList.size() > 0">
		<s:iterator id="map" value="staffList" status="st">
			<tr>
				<td><input type="radio" class="org_p_staff_list" name="11" value="<s:property value='#map.account'/>" /></td>
				<td><a href="javascript:showUpdateCustomerAccountInfo('<s:property value='#map.account'/>');" class="serviceStaff_showBtn"><s:property value="#map.name"/></a></td>
				<td><s:property value="#map.account"/></td>
				<td><s:property value="#map.type"/></td>
				<td><s:property value="#map.sex"/></td>
				<td><s:property value="#map.cellPhoneNumber"/></td>
				<td><s:property value="#map.birth"/></td>
			</tr>
		</s:iterator>
	</s:if>
</table>