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
		<th>账号状态</th>
		<th>操作</th>
	</tr>
	<s:if 
		test="staffList != null && staffList.size() > 0">
		<s:iterator id="map" value="staffList" status="st">
			<tr>
				<td><input type="radio" class="org_p_staff_list" name="11" value="<s:property value='#map.org_user_id'/>" /></td>
				<td><a href="javascript:showUpdateProviderAccountInfo('<s:property value='#map.org_user_id'/>');" class="serviceStaff_showBtn"><s:property value="#map.name"/></a></td>
				<td><s:property value="#map.account"/></td>
				<td><s:property value="#map.roleName"/></td>
				<s:if test="#map.gender == 'male'">
					<td>男</td>
				</s:if>
				<s:elseif test="#map.gender == 'female'">
					<td>女</td>
				</s:elseif>
				<s:else>
					<td><s:property value="#map.gender"/></td>
				</s:else>
				<td><s:property value="#map.mobile"/></td>
				<td><s:property value="#map.birthday"/></td>
				<s:if test="#map.account_status == 0">
					<td>未启用</td>
				</s:if>
				<s:elseif test="#map.account_status == 1">
					<td>正常</td>
				</s:elseif>
				<s:elseif test="#map.account_status == -1">
					<td>锁定</td>
				</s:elseif>
				<s:elseif test="#map.account_status == 2">
					<td>不可用</td>
				</s:elseif>
				<s:elseif test="#map.account_status == 3">
					<td>可用</td>
				</s:elseif>
				<s:else>
					<td>异常</td>
				</s:else>
				<s:if test="#map.account_status != 1 && #map.account_status != 3">
					<td><a href="javascript:void(0);" onclick="setNormalAccount('<s:property value="#map.account"/>');">解锁</a></td>
				</s:if>
				<s:else>
					<td></td>
				</s:else>
			</tr>
		</s:iterator>
	</s:if>
</table>