<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>


  <s:iterator  id="cp" value="searchResourceMapList" status="st2">
  		<s:if test="#cp.name != null && #cp.name != ''">
			<li onclick="showChiInfo('${cp.id }','${cp._entityType }',this);" title="${cp.name }">${cp.name }
  		</s:if>
  		<s:else>
  			<li onclick="showChiInfo('${cp.id }','${cp._entityType }',this);" title="${cp.label }">${cp.label }
  		</s:else>
		<input type="hidden" value="${cp.id }"/>
		<input type="hidden" value="${cp._entityType }"/>
		</li>
  </s:iterator>
