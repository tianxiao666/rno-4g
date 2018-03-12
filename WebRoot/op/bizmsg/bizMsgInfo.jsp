<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:iterator id="msg" value="bizMsgMap.list">
	<tr class="contentTr" onclick="operaTr(this);">
         <td style="width:20px;"><input class="cbxMsg" type="checkbox" value='<s:property value="#msg.id" />' /></td>
         <td style="width:69px;"><s:property value="#msg.bisMsgType.typeName" /></td>
         <td style="width:200px;" class="first_td">
         	<div class="long_td_div" style="width:198px;">
	         	<s:if test="#msg.link==null">
	         		<a href="#"><s:property value="#msg.title" /></a>
	         	</s:if>
	         	<s:else>
	         		<a target="_blank" href="/ops/<s:property value="#msg.link" />" title="<s:property value="#msg.title" />"><s:property value="#msg.title" /></a>
	         	</s:else>
         	</div>
         </td>
         <td style="width:244px;" class="long_td"><div class="long_td_div" title="<s:property value="#msg.content" />" ><s:property value="#msg.content" /></div></td>
         <td><s:date name="#msg.sendTime" format="yyyy-MM-dd HH:mm" /></td>
     </tr>
</s:iterator>

