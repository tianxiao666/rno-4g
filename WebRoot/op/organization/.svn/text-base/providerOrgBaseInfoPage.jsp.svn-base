<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<input type="hidden" id="p_areaIds" value='<s:property value="map.areaId" />' />
<input type="hidden" id="parentOrgName" value='<s:property value="map.parentOrgName" />' />
<input type="hidden" id="show_org_p_longitude" value='<s:property value="map.longitude" />' />
<input type="hidden" id="show_org_p_latitude" value='<s:property value="map.latitude" />' />
<input type="hidden" id="show_org_p_enterpriseId" value='<s:property value="map.enterpriseId" />' />
<input type="hidden" id="show_org_p_parentOrgId" value='<s:property value="map.parentOrgId" />' />
<input type="hidden" id="show_org_p_areaId" value='<s:property value="map.areaId" />' />
<input type="hidden" id="show_org_p_areaName" value='<s:property value="map.areaName" />' />
<table id="orgTable" class="container_info_in_table">
	<tr>
	   <td class="menu_td">组织名称：</td>
	   <td><em id="show_org_p_name"><s:property value="map.name" /></em></td>
	   <td class="menu_td">上级组织：</td>
	   <td><s:property value="map.parentOrgName" /></td>
   </tr>
   <tr>
	   <td class="menu_td">组织属性：</td>
	   <td><em id="show_org_p_orgAttribute"><s:property value="map.orgAttribute" /></em></td>
	   <td class="menu_td">组织类型：</td>
	   <td><em id="show_org_p_type"><s:property value="map.type" /></em></td>
   </tr>
   <tr>
	   <td class="menu_td">组织职责：</td>
	   <td colspan="3"><em id="show_org_p_orgDuty"><s:property value="map.orgDuty" /></em></td>
   </tr>

   <tr>
	   <td class="menu_td">驻点位置：</td>
	   <td colspan="3"><em id="show_org_p_address"><s:property value="map.address" /></em>&nbsp;&nbsp; <%-- <a href="#" target="_blank" class="ml100">查看GIS地图</a> --%></td>
   </tr>

   <tr>
	   <td class="menu_td">驻点电话：</td>
	   <td colspan="3"><em id="show_org_p_contactPhone"><s:property value="map.contactPhone" /></em></td>
   </tr>
	<tr>
		   <td class="menu_td">驻点经度：</td>
		   <td><em id="show_orgs_p_longitude"><s:property value="map.longitude" /></em></td>
 
		   <td class="menu_td">驻点纬度：</td>
		   <td><em id="show_orgs_p_latitude"><s:property value="map.latitude" /></em></td>

	   </tr>
   <tr>
	   <td class="menu_td">负责人：</td>
	   <td><a href="javascript:showUpdateProviderAccountInfo('<s:property value='map.dutyPerson'/>');" class="serviceStaff_showBtn"><em style="display:none;" id="show_org_p_dutyPerson"><s:property value="map.dutyPerson" /></em><em id="show_org_p_dutyPersonName"><s:property value="map.dutyPersonName" /></em></a></td>
	   <td class="menu_td">负责人电话：</td>
	   <td><em id="show_org_p_dutyPersonPhone"><s:property value="map.dutyPersonPhone" /></em></td>
   </tr>
   
   <tr>
	   <td class="menu_td">所属企业：</td>
	   <td colspan="3"><em id="show_org_p_enterpriseName"><s:property value="map.enterpriseName" /></em></td>
   </tr>
    <tr>
	   <td class="menu_td">关联区域：</td>
	   <td colspan="3"><em id="org_areaName"><s:property value="map.areaName" /></em></td>
   </tr>
</table>
<div class="but">
	<input type="button" class="fb serviceInfo_showBtn authorityControlClass" id="" value="修改" onclick="updateProviderOrgBaseInfo();">
	<input type="button" class="fb personnelDelete_showBtn authorityControlClass" id="" value="删除" onclick="deleteProviderOrgBaseInfo();">
	<input type="button" class="fb serviceInfo_showBtn authorityControlClass" id="" value="添加下级组织" onclick="addProviderOrgBaseInfo();">
</div>