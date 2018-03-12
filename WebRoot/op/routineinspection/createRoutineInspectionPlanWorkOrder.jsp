<%@ page language="java" import="java.util.*" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ page language="java" import="java.util.Date"%>
<%@ page language="java" import="java.text.DateFormat"%>
<%@ page language="java" import="java.text.SimpleDateFormat"%>

<%@ taglib uri="/struts-tags" prefix="s"%>	
<%DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>制定巡检计划</title>
<link rel="stylesheet" type="text/css" href="../../css/base.css" />
<link rel="stylesheet" type="text/css" href="../../css/public.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/dialog/dialog.css"/>
<link rel="stylesheet" type="text/css" href="css/xunjian.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery.treeview.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/paging/iscreate-paging.css" />

<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js" ></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.validate.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.metadata.js"></script>
<script type="text/javascript" src="../../jslib/validate.ex.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js"></script>
<script type="text/javascript" src="../../op/jslib/generateTree.js"></script>
<script type="text/javascript" src="../../jslib/date/wdatePicker.js"></script>
<script type="text/javascript" src="../../jslib/common.js"></script>
<script type="text/javascript" src="../../jslib/jquery/tree_demo.js"></script>

<script type="text/javascript" src="jslib/createRoutineInspectionPlanWorkOrder.js"></script>

<style type="text/css">
.select_tree_div{display:none;background: none repeat scroll 0 0 #FFFFFF;border: 1px solid #CCCCCC;height:200px;position: absolute;width: 180px; overflow:auto;}
</style>

<script>
$(function(){
	
	
	/*
	$(".content_step").hide().eq(0).show();
	
	$(".next_step").each(function(index){
		$(this).click(function(){
			$(".content_step").eq(index).fadeOut(200);
			setTimeout(function(){
				$(".tip_todo").removeClass("tip_now").eq(index+1).addClass("tip_now");
				$(".tip_todo").slice(0,index+1).addClass("tip_done");
				$(".content_step").eq(index+1).fadeIn(200);
			},200)
		})
	})
	$(".pre_step").each(function(index){
		$(this).click(function(){
			$(".content_step").eq(index+1).fadeOut(200);
			setTimeout(function(){
				$(".tip_todo").removeClass("tip_now").eq(index).removeClass("tip_done").addClass("tip_now");
				$(".tip_todo").slice(0,index).addClass("tip_done");
				$(".content_step").eq(index).fadeIn(200);
			},200)
		})
	});*/
	
	//组织添加/修改基本信息弹出框
	$(".jizhan_showBtn").click(function(){
		$("#jizhan_Dialog").show();
		$(".black").show();
	})
	//关闭弹出框
	$(".dialog_closeBtn").click(function(){
		$("#jizhan_Dialog").hide();
		$(".black").hide();
	})
	$(".modify_button").click(function(){
		$(".modify_alert").slideDown("fast");
	})
	$(".modify_alert_close").click(function(){
		$(".modify_alert").slideUp("fast");
	});
	
})




</script>
</head>

<body>
	<input id="show_orgId" type="hidden" value="<s:property value='orgId' />" />
	<input id="show_orgName" type="hidden" value="<s:property value='orgName' />"  />
	<input type="hidden" value="<%=dateFormat.format(new Date())%>" id="nowTime"/>
	<div class="container960">
    	<%-- <div class="header">
            <h4>制定巡检计划</h4>
        </div> --%>
    	<div class="tool_bar">
        	<span class="tip_todo tip_now">设置巡检计划属性</span>
            <em class="tip_to"></em>
            <%--
            <span class="tip_todo">设置巡检任务共享范围</span>
            <em class="tip_to"></em>
            --%>
            <span class="tip_todo">设置巡检任务</span>
            <%--
            <em class="tip_to"></em>
            <span class="tip_todo">启动巡检计划</span>
            --%>
        </div>
        
        <%------------------ 第一步 ---------------------%>
        <div class="content">
        	<form id="form1" name="form1" action="createRoutineInspectionPlanWorkOrderAction.action" method="post">
        	<div class="content_step">
            	<table class="thleft_table">
                    <tr>
                        <th colspan="4"><em class="edit_ico">定义巡检计划属性</em></th>
                    </tr>
                    <tr>
                        <td class="label_td"><em class="red">*&nbsp;</em>巡检组织：</td>
                        <td style="width:35%;">
                            <input type="text" class="required input_text"  id="searchOrgName" name="orgName" readonly="readonly" style="width:172px;"/>
                            <a href="#" class="orgButton" id="chooseAreaButton"></a>
                            <div id="treeDiv" class="select_tree_div">
		    					<%-- 放置组织架构树 --%>
		    				</div>
		    				<input id="searchOrgId" name="routineinspectionPlanworkorder.orgId" type="hidden" />
                        </td>
                        <td class="label_td"><em class="red">*&nbsp;</em>计划执行时间：</td>
                        <td>
                        	<input type="text" id="date_text1" name="routineinspectionPlanworkorder.planStartTime" onFocus="var date_text2=$dp.$('date_text2');WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:function(){date_text2.focus();},maxDate:'#F{$dp.$D(\'date_text2\')}',minDate:'%y-%M-%d'})" readonly class="Wdate required input-text"/>
                        	至 
                        	<input type="text" id="date_text2" name="routineinspectionPlanworkorder.planEndTime" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'date_text1\')}'})" readonly class="Wdate required input-text"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="label_td"><em class="red">*&nbsp;</em>巡检类型：</td>
                        <td>
                            <select class="required" name="routineinspectionPlanworkorder.type" id="type" validateIsEmpty="#type" style="width:120px;">
                            	<option value="1" <s:if test="routineinspectionPlanworkorder.type==1">selected="selected"</s:if> >基站巡检</option>
                            	<%-- <option>室分巡检</option>
                            	<option>WLAN巡检</option>
                            	<option>管线巡检</option> --%>
                            </select>
                        </td>
                        <td class="label_td"><em class="red">*&nbsp;</em>巡检专业：</td>
                        <td>
                            <input name="routineinspectionPlanworkorder.routineInspectionProfession" type="checkbox" value="ResGroup_4_Wireless_Flattening" <s:if test="routineinspectionPlanworkorder.routineInspectionProfession.contains('Wireless')">checked="checked"</s:if> />无线
                            <input name="routineinspectionPlanworkorder.routineInspectionProfession" type="checkbox" value="ResGroup_4_Power_Flattening" <s:if test="routineinspectionPlanworkorder.routineInspectionProfession.contains('Power')">checked="checked"</s:if> />动力
                            <input name="routineinspectionPlanworkorder.routineInspectionProfession" type="checkbox" value="FlatNavigation_4_Room_4_Transmission" <s:if test="routineinspectionPlanworkorder.routineInspectionProfession.contains('Transmission')">checked="checked"</s:if> />传输
                            <input value="routineinspectionPlanworkorder.routineInspectionProfession" type="hidden" validateCheckbox="routineinspectionPlanworkorder.routineInspectionProfession" />
                        </td>
                    </tr>
                    <tr>
                        <td class="label_td"><em class="red">*&nbsp;</em>巡检模板：</td>
                        <td colspan="3">
                            <select name="routineinspectionPlanworkorder.templateId" id="templateId" validateIsEmpty="#templateId" >
                            	<%-- <option>请选择</option> --%>
                            	<option value="1">2012年基站巡检模板</option>
                            </select>
                        </td>
                        <td style="display: none;" class="label_td"><em class="red">*&nbsp;</em>VIP基站巡检次数：</td>
                        <td style="display: none;">
                            <input id="vipCount" name="routineinspectionPlanworkorder.vipBaseStationRoutineInspectionCount" value="<s:property value='routineinspectionPlanworkorder.vipBaseStationRoutineInspectionCount' />" type="text" /> (次)
                        </td>
                    </tr>
                    <tr>
                        <td class="label_td"><em class="red">*&nbsp;</em>巡检计划：</td>
                        <td colspan="3">
                            <input class="{required:true,maxlength:100}" id="planTitle" style="width:300px;" validateIsEmpty="#planTitle" type="text" name="routineinspectionPlanworkorder.planTitle" value="<s:property value='routineinspectionPlanworkorder.planTitle' />" />
                        </td>
                    </tr>
                    <tr>
                        <td class="label_td">备注：</td>
                        <td colspan="3">
                            <textarea id="remark" name="routineinspectionPlanworkorder.remark" style="width:99%;resize:none;" ><s:property value='routineinspectionPlanworkorder.remark' /></textarea>
                        </td>
                    </tr>
                </table>
                <div class="content_bottom">
                    <input type="submit" id="btn_createNext" class="next_step" value="下一步" />
                    <%-- <input type="button" value="取消" /> --%>
                </div>
            </div>
            </form>
        </div>
    </div>
</body>
</html>
