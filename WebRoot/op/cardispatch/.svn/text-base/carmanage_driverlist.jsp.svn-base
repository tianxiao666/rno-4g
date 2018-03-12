<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>司机管理</title>
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
<script type="text/javascript" src="js/obj/carmanage_driverlist_obj.js"></script>
<script type="text/javascript" src="js/carmanage_driverlist.js"></script>

<script type="text/javascript">
	$(function(){
		$("#multiQueryButton").click(function(){
			$(".advanced-search").toggle("fast");
		})
		$("#selectWorkPlaceButton").click(function(){
			$("#selectWorkPlace").toggle("fast");
		})
		//打开选择区域
		$("#choose_button").click(function(){
			$("#selectWorkPlace2").slideToggle("fast");
		});
	})
</script>
</head>

<body>
	<div class="top_right_tool">
		<input name="choiceBizName" value="" readonly="readonly" type="text" id="choice_bizName"/>
		<input name="choiceBizId" value="" type="hidden" id="choice_bizId"/>
		<a href="javascript:void(0);"class="select_button selectWorkPlaceButton" id="selectWorkPlaceButton"></a>
        <div id="selectWorkPlace" class="text_org_tree">
        	<%-- 放置左边组织架构树 --%>
        </div>
   		<input name="driverName" value="" type="text" id="driverNameSQText">
   		<input class="input_button" value="搜索" id="simpleQueryButton" type="button" onclick="searchDriver();">
        <%-- <input class="input_button" value="高级搜索" id="multiQueryButton" type="button" >  --%>
        <div class="advanced-search" style="display:none;">
	       	<div class="advanced-search-title"><h4>填写查询信息</h4></div>
          		<table class="main-table1 tl" style="margin-bottom:0pxNam;">
                  <tr>
                      <td class="menuTd">司机姓名：</td>
                      <td>
                          <input type="text" class="input_text" id="driverNameHQText"  />
                      </td>
                  </tr>
                  <tr>
                      <td class="menuTd">司机所属区域：</td>
                      <td style="position:relative;z-index:120;">
                          <input type="text" class="input_text" readonly="readonly" id="bizName" />
                          <input type="hidden" class="input_text" id="bizId" />
                          <a href="javascript:void(0);" class="select_button" id="choose_button" title="选择组织"></a>
                          <div id="selectWorkPlace2" style="position:absolute; height:230px; width:200px; z-index:121; *+left:0px; *+top:30px; overflow:auto; border:1px solid #ccc; background:#fff;display:none;">
                        	<%-- 放置左边组织架构树 --%>
                          </div>
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
	
    <div class="container-main-table1">
        <h4 class="container-main-title">司机信息列表</h4>
        <%-- 司机信息Div --%>
        <div id="driverInfoDiv">
	        <table id="resultListTable" class="main-table1 tc">
	          <thead>
	           <tr>
	                <th style="width:35px;"></th>
	                <th>司机姓名</th>
	                <th>司机所属区域</th>
	                <th>司机电话</th>
	                <th>司机身份证号</th>
	            </tr>
	         </thead>
	          <tbody>
	          </tbody>	
	        </table>
        </div>
        <div style="padding:0px; padding-top:none; float:left;" id="driver_foot_div" >
            <input type="checkbox" class="input_checkbox" id="allcheckbox" onclick="allCheck();" />全选&nbsp;
            <input type="button" class="input_button" value="删除" onclick="deleteDriver();" />&nbsp;
            <input type="button" class="input_button" value="添加司机" onclick="window.open('carmanage_saveDriver.jsp')"/>
        </div>
    </div>

</body>
</html>
