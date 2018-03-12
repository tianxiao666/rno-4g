<%@ page language="java" import="java.util.*" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>

<%@ taglib uri="/struts-tags" prefix="s"%>	

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>巡检问题点</title>
<link rel="stylesheet" type="text/css" href="../../css/base.css" />
<link rel="stylesheet" type="text/css" href="../../css/public.css" />
<link rel="stylesheet" type="text/css" href="css/xunjian.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/paging/iscreate-paging.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery.treeview.css"/>
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js" ></script>
<script type="text/javascript" src="../../jslib/paging/paging.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js"></script>
<script type="text/javascript" src="../jslib/generateTree.js"></script>
<script type="text/javascript" src="jslib/searchRoutineInspectionQuestion.js" ></script>
</head>

<body>

	<div class="container">
    	<div class="tool_bar">
        	<span>
            	<input type="text" id="searchOrgName" readonly="readonly" /><a href="#" class="orgButton" id="chooseAreaButton"></a>
            	<div id="treeDiv" class="select_tree_div" style="display: none;">
   					<%-- 放置组织架构树 --%>
   				</div>
   				<input id="searchOrgId" type="hidden" />
                <select id="questionType" style="width:120px;">
                   <option value="" selected="selected">请选择</option>
                    <option value="环境问题">环境问题</option>
                    <option value="动力问题">动力问题</option>
                    <option value="物业问题">物业问题</option>
                    <option value="施工隐患">施工隐患</option>
                    <option value="其它">其它</option>
                </select>
                <select id="seriousLevel" style="width:120px;">
                    <option value="" selected="selected">请选择</option>
                    <option value="严重">严重</option>
                    <option value="一般">一般</option>
                    <option value="次要">次要</option>
                </select>
                <select id="isOver" style="width:120px;">
                    <option value="" selected="selected">请选择</option>
                    <option value="0">未解决</option>
                    <option value="1">已解决</option>
                </select>
                <input id="description" type="text" placeholder="问题描述" />
                <input type="button" value="查询" onclick="loadQuestionList();"/>
            </span>
        </div>
        
        <div id="questionList">
        	<table class="thcenter_table tc">
            	<tr>
                	<th>问题描述</th>
                    <th>问题类型</th>
                    <th>重要程度</th>
                    <th>所属组织</th> 
                    <th>创建人</th>
                    <th>创建时间</th>
                    <th>状态</th>
                </tr>
            </table>
       	</div>
        <%-- 默认每页10条或20条记录 --%>
        <div class="paging_div" id="questionListPage">
        	<div class='paging_div'>
			<a class='paging_link page-first' id='pagingColumnFirstPage"+pageDivId+"' title='首页' ></a>
			<a class='paging_link page-prev' id='pagingColumnUpPage"+pageDivId+"' title='上一页' ></a>
			<i class='paging_text'>&nbsp;第&nbsp;</i><input type='text' class='paging_input_text' id='pagingColumnCurrentPage"+pageDivId+"' value='1' onkeypress='return noNumbers(event)' />&nbsp;页/共&nbsp;<i class='paging_text' id='pagingColumnTotalPage"+pageDivId+"'></i>页&nbsp;<a class='paging_link page-go' id='pagingColumnSkip"+pageDivId+"' title='GO' >GO</a>
			每页<input type='text' id='pagingColunmSelect"+pageDivId+"' style='width:40px;'onkeypress='return noNumbers(event)' >条记录&nbsp;|&nbsp;
			<input type='hidden' id='pagingColunmSelect' style='width:40px;' value='"+pageSize+"' onkeypress='return noNumbers(event)' >
			<a class='paging_link page-next' id='pagingColumnDownPage"+pageDivId+"' title='下一页'></a>
			<a class='paging_link page-last' id='pagingColumnLastPage"+pageDivId+"' title='末页' ></a>
			</div>
        </div>
    </div>

</body>
</html>
