<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<script type="text/javascript">


$(function() {
	//选中单选按钮
	$(".selectStation-table tr td").each(function(){
		$(this).click(function(){
			$(this).find("input[name='stationRadio']").attr("checked","checked");
		});
	});
	
	//取消按钮
	$("#btnCancels").click( function() {
		$("#staffList").hide();
		$("#black").hide();
	});
	
	//确定按钮
	$("#btnSubmitD").click( function() {
		$(".org_p_staff_list").each(function(){
			if($(this).attr("checked")=="checked"){
				var orgUserId = $(this).val();
				var orgId = $("#orgId").val();
				$.ajax({
		            url:"../organization/createOrgStaffAction",
		            async:false,
		            type:"POST",
		            dataType:"json",
		            data:{"orgId":orgId,"orgUserId":orgUserId},
		            success : function(result) {
						showProviderAccountInfo(orgId);
		            }
		        });
//		        showProviderAccountInfo(orgId);
//		        showCustomerAccountInfo(orgId);
				$("#staffList").hide();
				$("#black").hide();
			}
		});
	});
});
</script>
<div class="selectStation-title">人员列表
   	<span class="selectStation-title-search">
         		<input type="text" id="rex"/>&nbsp;
	<input type="button" id="search" value="快速查询" onclick="staffMultiFuzzy();"/>
	</span>
           </div>
	 <div id="searchStation">
           	<span>首字母索引：</span>
               <ul>
               	<li><a onclick="staffMultiPinyinQuery('a')">A</a></li>
                   <li><a onclick="staffMultiPinyinQuery('b')">B</a></li>
                   <li><a onclick="staffMultiPinyinQuery('c')">C</a></li>
                   <li><a onclick="staffMultiPinyinQuery('d')">D</a></li>
                   <li><a onclick="staffMultiPinyinQuery('e')">E</a></li>
                   <li><a onclick="staffMultiPinyinQuery('f')">F</a></li>
                   <li><a onclick="staffMultiPinyinQuery('g')">G</a></li>
                   <li><a onclick="staffMultiPinyinQuery('h')">H</a></li>
                   <li><a onclick="staffMultiPinyinQuery('i')">I</a></li>
                   <li><a onclick="staffMultiPinyinQuery('j')">J</a></li>
                   <li><a onclick="staffMultiPinyinQuery('k')">K</a></li>
                   <li><a onclick="staffMultiPinyinQuery('l')">L</a></li>
                   <li><a onclick="staffMultiPinyinQuery('m')">M</a></li>
                   <li><a onclick="staffMultiPinyinQuery('n')">N</a></li>
                   <li><a onclick="staffMultiPinyinQuery('o')">O</a></li>
                   <li><a onclick="staffMultiPinyinQuery('p')">P</a></li>
                   <li><a onclick="staffMultiPinyinQuery('q')">Q</a></li>
                   <li><a onclick="staffMultiPinyinQuery('r')">R</a></li>
                   <li><a onclick="staffMultiPinyinQuery('s')">S</a></li>
                   <li><a onclick="staffMultiPinyinQuery('t')">T</a></li>
                   <li><a onclick="staffMultiPinyinQuery('u')">U</a></li>
                   <li><a onclick="staffMultiPinyinQuery('v')">V</a></li>
                   <li><a onclick="staffMultiPinyinQuery('w')">W</a></li>
                   <li><a onclick="staffMultiPinyinQuery('x')">X</a></li>
                   <li><a onclick="staffMultiPinyinQuery('y')">Y</a></li>
                   <li><a onclick="staffMultiPinyinQuery('z')">Z</a></li>
                   <li style="width:96px;"><a onclick="staffMultiPinyinQuery('all')">显示全部</a></li>
                   
               </ul>
           </div>
           <span class="selectStation-main-title">人员列表：
           		<input type="radio" class="org_p_choiceAccount" name="org_p_choiceAccount" value="one" checked="checked" onclick="changeSearchStaffMulti('staffList');" style="top: -2px; vertical-align: middle;" /><em>未选人员 </em>
           		<input type="radio" class="org_p_choiceAccount" name="org_p_choiceAccount" value="all" onclick="changeSearchStaffMulti('staffList');" style="top: -2px; vertical-align: middle;" /><em>所有人员</em>
           	</span>
           
	<div class="selectStation-main">
		<table class="main_table1 tc">	
			<tr>
				<th></th>
				<th>姓名</th>
				<th>IT帐号</th>
				<th>IT用户类型</th>
				<th>性别</th>
				<th>手机电话</th>
				<th>出生年月</th>
			</tr>
			<s:if test="staffList != null && staffList.size() > 0">
				<s:iterator id="map" value="staffList" status="st">
					<tr class="org_p_select_staff_tr">
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
					</tr>
				</s:iterator>
			</s:if>
		</table>
		<div id="org_p_select_staff_page"></div>
	</div>
	
	<div class="selectStation-bottom">
		<input type="button" id="btnSubmitD"  value="确定" />&nbsp;&nbsp;&nbsp;
		<input type="button" id="btnCancels" value="取消" />
	</div>