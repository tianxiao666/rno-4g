<%@ page language="java" import="java.util.*" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>

<%@ taglib uri="/struts-tags" prefix="s"%>	

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查看巡检计划信息</title>
<link rel="stylesheet" type="text/css" href="../../css/base.css" />
<link rel="stylesheet" type="text/css" href="../../css/public.css" />
<link rel="stylesheet" type="text/css" href="css/xunjian.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/paging/iscreate-paging.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery.treeview.css" />

<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js" ></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js"></script>
<script type="text/javascript" src="../../jslib/jquery/tree_demo.js"></script>
<script type="text/javascript" src="../../jslib/paging/paging.js"></script>
<script type="text/javascript" src="../jslib/generateTree.js"></script>
<script type="text/javascript" src="js/xunjian.js" ></script>
<script type="text/javascript" src="jslib/loadRoutineInspectionPlanWorkOrderInfo.js" ></script>
</head>

<body>
	<input type="hidden" id="WOID" value="<s:property value='planWorkOrderInfoMap.WOID' />" />
    <div class="container960">
        <div class="header">
            <h4><em><s:property value='planWorkOrderInfoMap.planTitle' /></em>
            	<span class="working">
                	<em class="working_ico is_working"><s:property value='planWorkOrderInfoMap.complete' />%</em>
            		<em class="working_ico now_working"><s:property value='planWorkOrderInfoMap.statusName' /></em>
                </span>
            </h4>
        </div>
        <div class="content">
        	<div class="tab">
                <div class="tab_menu">
                    <ul>
                        <li class="ontab">基本信息</li>
                        <li>巡检任务</li>
                    </ul>
                </div>
                <div class="tab_container">
                    <div class="tab_content">
                        <table class="thleft_table">
                            <tr>
                                <th colspan="4"><em class="edit_ico">巡检计划信息</em></th>
                            </tr>
                            <tr>
                            	<td class="label_td">巡检组织：</td>
                                <td style="width:35%;"><s:property value='planWorkOrderInfoMap.orgName' /></td>
                                <td class="label_td">执行时间：</td>
                                <td><s:property value='planWorkOrderInfoMap.executeTime' /></td>
                            </tr>
                            <tr>
                                <td class="label_td">巡检模板：</td>
                                <td colspan="3"><s:property value='planWorkOrderInfoMap.templateName' /></td>
                                <%-- 
                                <td class="label_td">VIP基站巡检次数（次）：</td>
                                <td><s:property value='planWorkOrderInfoMap.vipCount' /></td>
                                 --%>
                            </tr>
                            <tr>
                                <td class="label_td">巡检类型：</td>
                                <td><s:property value='planWorkOrderInfoMap.type' /></td>
                                <td class="label_td">巡检专业：</td>
                                <td><s:property value='planWorkOrderInfoMap.profession' /></td>
                            </tr>
                            <tr>
                                <td class="label_td">备注：</td>
                                <td colspan="3">
                                    <s:property value='planWorkOrderInfoMap.remark' />
                                </td>
                            </tr>
                            <tr>
                                <td class="label_td">创建人：</td>
                                <td><s:property value='planWorkOrderInfoMap.creatorName' /></td>
                                <td class="label_td">创建时间：</td>
                                <td><s:property value='planWorkOrderInfoMap.createTime' /></td>
                            </tr>
                            <tr>
                                <td class="label_td">激活时间：</td>
                                <td><s:property value='planWorkOrderInfoMap.activateTime' /></td>
                                <td class="label_td">关闭时间：</td>
                                <td><s:property value='planWorkOrderInfoMap.finalCompleteTime' /></td>
                            </tr>
                        </table>
                        
                    </div>
                    <div class="tab_content">
                        <table class="thleft_table">
                            <tr>
                                <th colspan="4">
                                	<input type="text" id="searchOrgName" readonly="readonly" /><a href="#" class="orgButton" id="chooseAreaButton"></a>
					            	<div id="treeDiv" class="select_tree_div" style="display: none;">
					   					<%-- 放置组织架构树 --%>
					   				</div>
					   				<input id="searchOrgId" name="selectRoomToPlanOrgId" type="hidden" />
                                    <select id="resourceLevel" style="display:none;">
					                    <option value="" selected="selected">请选择</option>
					                </select>
                                    <input type="text" id="resourceName" placeholder="机房名称" value="" />
                                    <input type="button" value="筛选" onclick="loadTaskList();" />
                                </th>
                            </tr>
                            <tr>
                                <td class="vt">
                                    <h4 class="table_top">巡检任务列表：</h4>
                                    <div id="taskList">
                                    </div>
									 <%-- 默认每页10条或20条记录 --%>
									<div class="paging_div" id="taskListPage">
									</div>
                                    <div style="margin-top:-30px;position:absolute;">
                                    	<p>共有巡检任务<em id="taskCountEm" class="red"></em>个 ，已关闭<em id="taskCloseCountEm" class="red"></em>个，巡检中<em id="startingCountEm" class="red"></em>个，待巡检<em id="noStartCountEm" class="red"></em>个。  平均偏离距离<em class="red" id="averageDeviateEm"></em>米。</p>
                                    </div>
                                    <div class="explain_message">
                                        <ul>
                                            <li>
                                            	终端偏离位置：
                                                <span class="colored bg_yellow"></span>
                                                表示位置偏离距离大于1000米，超出规定范围。
                                            </li>
                                            <li>
                                            	巡检截止时间：
                                                <span class="colored bg_red"></span>
                                                表示巡检任务已经超时。
                                            </li>
                                        </ul>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
