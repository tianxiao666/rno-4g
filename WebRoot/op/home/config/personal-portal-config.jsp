<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	<%@include file="/portal/common.jsp"%>
  	
  	<script type="text/javascript" src="portal/config/js/personal-portal-config.js"></script>
  </head>  
  <body>
    <input type="hidden" value=${userId} name="userId" id="userId" />
    <input type="hidden" value=${schemeId} name="schemeId" id="schemeId" />
    <input type="hidden" value=${orgRoleId} name="orgRoleId" id="orgRoleId" />
    <input type="hidden" value='' name="workZoneId" id="workZoneId" />
    <input type="hidden" value='' name="workzonesiteId" id="workzonesiteId" />
    <input type="hidden" value='' name="workzonesiteType" id="workzonesiteType" />
    
    
    <select id="workzoneVisible">
      <option value=1>显示</option>
      <option value=0>隐藏</option>
    </select>
    
      <select id="workzoneSiteVisible">
      <option value=1>显示</option>
      <option value=0>隐藏</option>
    </select>
    
    <select id="itemVisible">
      <option value=1>显示</option>
      <option value=0>隐藏</option>
    </select>
  </body>
</html>
