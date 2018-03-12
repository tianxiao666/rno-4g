<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%-- 基本css --%>
<link rel="stylesheet" type="text/css" href="../../css/base.css" />
<link rel="stylesheet" type="text/css" href="../../css/public.css" />
<link rel="stylesheet" type="text/css" href="../../css/public-table.css" />
<link rel="stylesheet" type="text/css" href="css/informationManager.css" />
<link rel="stylesheet" href="../../jslib/jquery/css/jquery.treeview.css" type="text/css"></link>
<link rel="stylesheet" href="../../jslib/paging/iscreate-paging.css" type="text/css"></link>
<%-- 基本js --%>
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="../../js/public.js"></script>
<script type="text/javascript"src="../../jslib/jquery/jquery.treeview.js"></script>
<script type="text/javascript" src="../../jslib/common.js"></script>
<script type="text/javascript" src="../jslib/generateTree.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.form.js"></script>
<script type="text/javascript" src="js/util/input.js"></script>
<script type="text/javascript" src="../../jslib/date/date.js"></script>
<script type="text/javascript" src="js/tool/tablePage.js"></script>
<script type="text/javascript" src="js/tool/new_formcheck.js"></script>
<script type="text/javascript" src="js/obj/carmanage_terminallist_obj.js"></script>
<script type="text/javascript" src="js/carmanage_terminallist.js"></script>
<script type="text/javascript">
	$(function(){
		$("#multiQueryButton").click(function(){
			$(".advanced-search").toggle("fast");
		})
		$("#selectWorkPlaceButton").click(function(){
			$("#selectWorkPlace").toggle("fast");
		})
		//按钮显示、隐藏
		$("#gaojisousuo").click(function(){
			$("#confirm_div").show();
		})
		
		$("#searchBtn,#cancelSearch").click(function(){
			$(".advanced-search").toggle("fast");
		})
	})
</script>
<title>车载终端管理</title>
</head>

<body>
	<div class="top_right_tool">
		<input name="mobile_bizName" readonly="readonly" value="" type="text" id="mobile_bizName"/>
		<input name="mobile_bizId" value="" type="hidden" id="mobile_bizId"/>
		<a href="javascript:void(0);" class="select_button selectWorkPlaceButton" id="selectWorkPlaceButton" title="选择组织"></a>
		<div id="selectWorkPlace" class="text_org_tree">
		<%-- 放置资源数 --%>
		</div>
		<input name="clientImei" class="input_text" type="text" id="clientImei"/>
		<input class="input_button" value="搜索" id="simpleQueryButton" type="button" onclick="searchImei();"/>
		<input class="input_button" value="高级搜索" id="multiQueryButton" type="button"/>
	</div>
		<div class="advanced-search">
			<h4 class="advanced-search-title">填写查询信息</h4>
                <table class="main-table1 tl" style="margin-bottom:0px;">
                    <tr>
                        <td class="menuTd">设备类型：</td>
                        <td>
                            <input type="text" class="input_text" id="mobileType" />
                        </td>
                    </tr>
                    <tr>
                        <td class="menuTd">车载终端：</td>
                        <td>
                            <input type="text" class="input_text"  id="clientimei" />
                            
                        </td>
                    </tr>
                    <tr>
                        <td class="menuTd">车牌号码：</td>
                        <td>
                            <input type="text" class="input_text" id="carNo"  />
                        </td>
                    </tr>
                    <tr>
                        <td class="menuTd">开通时间：</td>
                        <td>
                            <input type="text" readonly="readonly" class="input_text"  id="launchedTime"  />
                            <input type="button" class="input_button" value="选择时间" 
		                    		onclick="fPopCalendar(event,document.getElementById('launchedTime'),document.getElementById('launchedTime'),false)"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="menuTd">SIM号：</td>
                        <td colspan="3">
                            <input type="text" class="input_text"  id="telphoneNo"  />
                        </td>
                    </tr>
                    <tr>
                        <td class="tc" colspan="4">
                        	<input type="button" class="input_button" id="searchBtn" value="查询" onclick="highSearchBtn();" />&nbsp;
                 		   	<input type="button" class="input_button" id="cancelSearch" value="取消" />
                        </td>
                    </tr>
                </table>
  			</div>
		</div>
    <div style="width:auto; overflow:hidden;">
    
    
    
    <div class="container-main-table1">
		<h4 class="container-main-title">车载终端信息列表</h4>
        <table class="main-table1 tc" id="mobileList_table">
       		<thead>
	            <tr>
	                <th style="width:35px;"></th>
	                <th>终端号</th>
	                <th>设备类型</th>
	                <th>所属组织</th>
	                <th>开通时间</th>
	            </tr>
            </thead>
            <tbody>
            </tbody>
        </table>
        <div style="padding:0px; padding-top:none; float:left;" id="terminal_foot_div" >
            <input type="checkbox" class="input_checkbox" id="allcheckbox" onclick="allCheck();" />全选&nbsp;
            <input type="button" class="input_button" value="删除" onclick="deleteMobile();" />&nbsp;
            <input type="button" class="input_button" value="添加车载终端" onclick="window.open('carmanage_saveTerminal.jsp');" />
        </div>
    </div>
    </div>
    
    
</body>
</html>

