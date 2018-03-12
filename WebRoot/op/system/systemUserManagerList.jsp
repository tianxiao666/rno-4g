<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript" src="../project/js/common.js"></script>
<script type="text/javascript">
</script>
<input type="hidden" id="totalPage" value="<s:property value='total'/>" />

<table id="resultListTable" class="search_result_standardtable_gray">  
	<tbody>
		<tr>
	      <th>姓名</th>
	      <th>账号</th>
	      <th>所属部门与职位</th>
	      <th>角色</th>
	      <th>邮箱</th>
	      <th>账号状态</th>
	      <th>操作</th>
	   </tr>  
	     <s:if test="userList!= null && userList.size() > 0">
			<s:iterator id="map" value="userList" status="st">
				<tr class="pageTr">
					<td class="search_result_standardtable_gray_date_sytle">
						<s:property value="#map.USERNAME"/></a>
					</td>
					<td class="search_result_standardtable_gray_date_sytle">
						<s:property value="#map.ACCOUNT"/>
					</td>
					<td style="padding-left:30px;">
						${map.ORGNAME}
					</td>
					<td style="padding-left:30px;">
						${map.ROLE}
					</td>
					<td class="search_result_standardtable_gray_date_sytle">
						<s:property value="#map.EMAIL"/>
					</td>
					<td class="search_result_standardtable_gray_date_sytle">
						<s:property value="#map.STATUS"/>
					</td>
					<td class="search_result_standardtable_gray_date_sytle">
						<a href="loadUpdateOrgUserViewAction?orgUserId=${map.ORG_USER_ID}" target="_blank">编辑</a>
						<a href="getUserRoleAndPermissionAction?orgUserId=${map.ORG_USER_ID}" target="_blank">设置角色权限</a>
						<a href="viewUserDataRangeAction?orgUserId=${map.ORG_USER_ID}" target="_blank">设置数据范围</a>
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr class="search_result_standardtable_gray_date_sytle"><td colspan="14"><span>暂 无 数 据</span></td></tr>
		</s:else>	
	</tbody>
</table>