<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div style="overflow:scroll;overflow-x:hidden;height:201px" id="scrollId">
<table class="main-table1 tc">
        <tr>
            <th width="10%">序号</th>
            <th width="20%">处理时间</th>
            <th width="10%">处理人</th>
            <th width="20%">处理类型</th>
            <th width="40%">处理结果</th>
        </tr>
		<s:if
			test="tasktracerecordList != null && tasktracerecordList.size() > 0">
		<s:iterator id="map" value="tasktracerecordList" status="st">
			<tr>
				<td width="10%">
					<s:property value="#st.getCount()" />
				</td>
				<td width="20%">
					<s:date name="#map.handleTime" format="yyyy-MM-dd HH:mm:ss"/>
				</td >
				<td width="10%">
					<s:property value="#map.handlerName" />
				</td>
				<td width="20%">
					<s:property value="#map.handleWay" />
				</td>
				<td align="left" width="40%">
					<s:property value="#map.handleResultDescription" />
				</td>
			</tr>
		
		</s:iterator>
		
		
		</s:if>
		<s:else>
		<tr>
			<td colspan='5' style='text-align:center'>没有相关的服务跟踪记录。</td>
		</tr>
		</s:else>
</table>

</div>

