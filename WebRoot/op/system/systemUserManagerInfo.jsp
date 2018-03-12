<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>用户管理列表	</title>

<link rel="stylesheet" type="text/css" href="../../css/base.css"/>
<link rel="stylesheet" type="text/css" href="../../css/public.css"/>
<link rel="stylesheet" type="text/css" href="css/systemManage.css"/>
<link rel="stylesheet" type="text/css" href="css/architecture.css"/>





<link rel="stylesheet" type="text/css" href="../../css/base.css" />
<link rel="stylesheet" type="text/css" href="../../css/input.css" />
<link rel="stylesheet" type="text/css" href="../../css/public.css" />
<link rel="stylesheet" type="text/css" href="../../css/public-table.css" />
<link rel="stylesheet" type="text/css" href="../../css/public-div-standard.css" />
<link rel="stylesheet" type="text/css" href="../../css/public-table-standard.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery-ui-1.8.23.custom.css" />
<link type="text/css" rel="stylesheet" href="../../jslib/paging/iscreate-paging.css" />
<link rel="stylesheet" type="text/css" href="../project/css/projectInfoManage.css" />
<link type="text/css" rel="stylesheet" href="../project/css/project.css" />
<style>
.orgUserManage_standard_budget_title {
    background-color: #F0F8FF;
    border: 1px solid #DDDDDD;
    height: 30px;
    line-height: 30px;
    margin-top: 5px;
    text-align: center;
}


.orgUserManage_main {
    background: none repeat scroll 0 0 #C3D5ED;
    margin: 5px;
    min-width: 500px;
}


.orgUserManage_content {
    background: none repeat scroll 0 0 #FFFFFF;
    border: 1px solid #99BCE8;
    min-height: 700px;
    overflow: hidden;
}

.orgUserManage_top {
    background: url("../../images/white-top-bottom.gif") repeat-x scroll 0 -1px rgba(0, 0, 0, 0);
    border-bottom: 1px solid #99BBE8;
    color: #15428B;
    font-weight: bold;
    line-height: 26px;
    padding: 0 5px;
}

.orgUserManage_title {
    padding: 0 10px;
}


</style>
</style>
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js" ></script>
<script type="text/javascript" src="../../jslib/common.js"></script>
<script type="text/javascript" src="../../jslib/date/wdatePicker.js "></script>
<script type="text/javascript" src="../../jslib/paging/paging.js"></script>

<script type="text/javascript">
	
	$(function()
	{
		$("#queryBtn").click(function()
		{
			search();
		});
		search();
	})
	
	function search()
	{ 
		
		var userNameOrAccount = $("#userNameOrAccount").val();
		
		var orgId = $("#orgId").val();
		
		var pageDivId = "pageContent";
		var showDivId = "listView1";
		var currentPage = 1;
		var actionName = "findSysUserManagerListAction";
		var pageSize = "10";
		
		
		var param={
			userNameOrAccount:userNameOrAccount,
			orgId:orgId,
			currentPage:currentPage,
			pageSize:pageSize
		};
		
		pagingColumnByBackgroundJsp(pageDivId,showDivId,actionName,param);
	}
	
	function addUser()
	{
		window.open("loadAddOrgUserViewAction");
		
	}
	
</script>
</head>

<input type="hidden" value="PM_TODO" id="menuId" />
<body>
	<%--主体开始--%>
	<div class="orgUserManage_main">
		<div class="orgUserManage_content">
			<div class="orgUserManage_top">
				用户列表
			</div>
			<div class="orgUserManage_title">
	           	<div class="orgUserManage_standard_budget_title">
	           		<h4>用     户    管    理</h4>
	       		 </div>
        	</div>
	        <div class="user_accountInfo" style="width: 100%;">
				<div class="accountInfo" style="padding: 20px;">
					<table style="position:relative;width: 100%">
						<tr height="20px;">
							<td>
								<span>姓名/账号 </span>
								<input type="text" class="pt1"  id="userNameOrAccount" />
								<span style="padding-left:50px;">创建部门：</span>
								<select id="orgId">
									<option value="0" >全部</option>
									<s:iterator id="orgList" value="sysOrgList" status="sysOrg">
										<option value="${orgList.ORG_ID}">${orgList.NAME}</option>
									</s:iterator>
								</select>
								<span style="padding-right:20px;"> </span>
								<span style="padding-left:50px;"><input type="button" id="queryBtn" value="查询" style="width:50px;" class="" /></span>
								
								
	                  		</td>
	                  		<td>
	                  			<input type="button" id="queryBtn" value="新增用户" style="width:100px;float:right" class=""  onclick="addUser();"/>
	                  		</td>
	             		</tr>
	             	</table>
	            </div>
	            
			    <div class="main_tab_container">
			        <%-- 搜索结果 --%>
			        <div class="search_result">
			        	<div id="listView">
				            <div id="listView1" class=""></div>
				        	<div id="pageContent"></div>
			             </div>
			        </div>
			    </div>
	        </div>
	        
		</div>
    </div>
</body>
</html>

