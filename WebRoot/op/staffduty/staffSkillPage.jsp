<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>人员技能管理</title>
<link rel="stylesheet" type="text/css" href="../../css/base.css" />
<link rel="stylesheet" type="text/css" href="../../css/public.css" />
<link rel="stylesheet" type="text/css" href="../../css/public-table.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/paging/iscreate-paging.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery.treeview.css" />
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js"></script>
<script type="text/javascript" src="../../jslib/common.js"></script>
<script type="text/javascript" src="../jslib/generateTree.js"></script>
<script type="text/javascript" src="../../jslib/paging/paging.js"></script>
<script type="text/javascript" src="js/staffSkillPage.js"></script>
<style type="text/css">
.treeDiv{z-index: 50;display:none; position:absolute;overflow:auto; background:#fff; border:1px solid #ccc;width: 190px; min-height:186px; text-align:left;margin-top:1px;top:23px;line-height:18px;}
#simpleQueryButton{display:inline-block; height:25px; width:25px; position:relative;top:-3px; vertical-align:middle;margin-right: 3px;background:url("images/search.png") no-repeat; height:25px; width:25px;}
/* 弹出框 */
.advanced-search { display:none;background:#F9F9F9;border:1px solid #999999;position: absolute;right: 0px;margin-top:-10px;width:400px;z-index: 101;}
.advanced-search-title {background: none repeat scroll 0 0 #EEEEEE;border-bottom: 1px solid #999999;line-height: 30px;padding-left: 10px;}
/*选择时间按钮*/
.date_button{height:20px; width:20px; background:url(images/date_choose.png) no-repeat;display: inline-block; position:absolute; margin:1px 0px 0px -22px;}
/*弹出框table样式*/
.alert_div_table{ width:100%; margin:0 auto;}
.alert_div_table th{ background:#eee;border-right:1px solid #fff;border-bottom: 1px solid #CCCCCC;height: 25px;line-height: 25px; border-top:1px solid #ccc; white-space:nowrap;}
.alert_div_table td{ border-bottom:1px solid #ccc;border-right:1px solid #fff;color: #777777; height:30px; vertical-align:middle; padding:0px 8px; white-space:nowrap;}
.alert_div_table .menuTd{ white-space:nowrap; text-align:right; vertical-align:middle;width:80px;padding-left: 30px;color:#000;}
.alert_div_table .lastTd{ border-right:none;}

/*弹出修改技能样式*/
.modify_skill{display:none;position:absolute; width:350px; border:1px solid #ccc; padding:3px; background:#fff;}
.modify_skill_top{width:100%; text-align:center; padding:5px 0;}
.modify_skill_main{min-height:150px; max-height:250px; overflow-y:auto;}
.midify_skill_close{position:absolute;right:0px; height:18px; width:18px; background:url(images/close.png);right: 6px;top: 5px; cursor:pointer;}
.orgSelect{background: url("images/select.png") no-repeat scroll 0 0 transparent; display: inline-block;height: 24px;top:2px;left:126px;position: absolute;width: 24px;}
/*技能鼠标浮窗*/
.dialog-box {position: absolute; width:300px;}
.dialog-title-bar { background: url("image/dialog-top.png") repeat-x;}
.dialog-bar-l{ background: url("image/dialog-bg.png") no-repeat; background-position: 0px 0px; width:20px; height: 40px; float:left;}
.dialog-bar-m{ float:left;}
.dialog-bar-r{ background: url("image/dialog-bg.png") no-repeat; background-position: -79px 0px; width:20px; height: 40px; float:right;}
.dialog-title-bar h2 { font-size: 14px; font-weight: 700;line-height: 40px;}
.dialog-title-bar a.close-dialog {background-color: #FFFFFF;border: 1px solid #999999;display: block; font-size: 14px; font-weight: 700; height: 18px; line-height: 18px; position: absolute;right: 15px;top: 10px;width: 18px;color: #999999;text-align: center;text-decoration: none;}
.dialog-title-bar a.close-dialog:hover {color: #333333;text-decoration: none;}
.dialog-content {background-color: #E6E6E6;}
.dialog-content-box{background-color: #ffffff; margin: 0 5px;border:1px solid #dbdbdb; border-top:none; border-bottom:none;}
.dialog-content-info p{padding:5px 10px;font-size:12px;}
.dialog-content-info{width:100%; text-align:center;}
.dialog-content-info th{border-bottom:1px solid #eee; padding:4px 6px; color:#999; font-weight:bold;}
.dialog-content-info td{border-bottom:1px solid #eee; padding:4px 6px;}

.dialog-foot{background: url("image/dialog-bottom.png") repeat-x; height: 45px; margin:0 20px 0 50px;}
.dialog-foot-l{ background: url("image/dialog-bg.png") no-repeat; background-position: 0px -54px; width:50px; height: 45px; float:left; margin-left: -50px;}
.dialog-foot-r{ background: url("image/dialog-bg.png") no-repeat; background-position: -79px -54px; width:20px; height: 45px; float:right; margin-right: -20px;}
.dialog-foot-i{ background: url("image/dialog-arrow.png") no-repeat; width:50px; height: 45px; left: 100px;position: relative;}
.dialog-right{background: url(image/dialog-right.png) no-repeat; height:140px; float: left; position: absolute; bottom: 0; right: -90px; width:90px;}

</style>
</head>

<body>
<div id="container">
	<div class="container-main">
		<div class="container-main-table1">
			<table class="main-table1"  style="margin-bottom:0px;">
				<tr class="main-table1-tr">
					<td class="main-table1-title" id="tr">
                        <span class="fr" style="position:relative;">
						
	                    	<input type="text" id="bizunitNameText" style="padding:3px; width:140px;" />
	                    	<input type="hidden" id="bizunitIdText" />
	                    	<a class="orgSelect" title="选择组织" href="javascript:void(0);" id="orgSelectButton1"></a>
	                    	
	                   		<input type="text" value="" id="staffName"  style="padding:3px; width:140px;"/>
	                        <a href="#" id="simpleQueryButton"></a>
	                        <input type="button" id="show_gaoji" value="高级查询" />
	                        <div id="treeDiv1" class="treeDiv">
	                            
	                        </div>
	                     </span>
                    </td>
                </tr>
			</table>
            <div class="advanced-search2" style="display:none;">
                <table class="main-table1">
                     <tr>
                        <td class="menuTd">人员组织：</td>
                        <td>
                        	<span style="position:relative;display:inline-block;">
                        		<input type="text" id="queryBizunitNameText"  style="padding:3px; width:140px;"/>
	                    		<input type="hidden" id="queryBizunitIdText" />
	                    		<a class="orgSelect" title="选择组织" href="javascript:void(0);" id="orgSelectButton2"></a>
	                            <div id="treeDiv2" class="treeDiv">
		                       	</div>
                        	</span>
                        </td>
                        <td class="menuTd">人员姓名</td>
                        <td>
                            <input type="text" id="queryStaffName" />
                        </td>
                    </tr>
                     <tr>
                        <td class="menuTd">人员技能：</td>
                        <td colspan="3">
                        	<select id="querySkillType">
                        		
                            </select>
                            <select id="querySkillGrade">
                            	<option>初级</option>
                            	<option>中级</option>
                            	<option>高级</option>
                            </select>
                            <select id="querySkillYear">
                            	<option value="1,2">1-2年</option>
                            	<option value="3,4">3-4年</option>
                            	<option value="5,6">5-6年</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="menuTd">人员性别：</td>
                        <td>
                            <input type="radio" name="staffSex" checked="checked" value="男"/>男
                            <input type="radio" name="staffSex" value="女"/>女
                        </td>
                        <td class="menuTd">联系方式：</td>
                        <td>
                            <input type="text" id="queryContactPhone"/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="4" class="tc">
                            <input type="button" class="advanced-search-hide" value="搜索" id="multiSearchButton" />
                        </td>
                    </tr>
                </table>
            </div>
		</div>
        
		<div class="container-main-table1-right">
            
			<div class="container-main-table1" style=" margin-top:10px;">
                  <table class="main-table1 main-table2" id="staffListTable">
                    <tr>
                    	<th>人员账号</th>
                        <th>人员姓名</th>
                        <th>技能描述</th>
                        <th>操作</th>
                    </tr>
                </table>
			</div>
			<div id="pageContent"></div>
        </div>
    </div>
</div>

<div class="modify_skill">
	<div class="modify_skill_top">
    	<h4>人员技能编辑
        	【<a href="#" onclick="openStaffInfo()"><span id="staffNameSpan"></span></a>】
        	<input type="hidden" id="staffIdHidden"/>
            <span class="midify_skill_close"></span>
        </h4>
        <span>
        	<select id="addSkillType"></select>
            <select id="addSkillGrade">
                <option>初级</option>
               	<option>中级</option>
               	<option>高级</option>
            </select>
            <input type="text" id="addSkillYear" value="1" style="width: 30px" title="请输入大于零的整数技能年限" />年
            <input type="button" value="添加技能" class="mt10" id="addSkillButton" />
        </span>
    </div>
    <div class="modify_skill_main" id="staffSkillDiv">
    	<table class="alert_div_table tc">
        	<tr>
            	<th>技能名称</th>
            	<th>技能级别</th>
            	<th>技能年限</th>
            	<th>操作</th>
            </tr>
        </table>
    </div>
    <div class="modify_skill_bottom">
    </div>
</div>
	<%-- 人员技能鼠标悬浮窗 BEGIN--%>
	<div id="dialog-pop" class="dialog-box" style="display: none">
	    <div class="dialog-popup">
		    <%--标题 --%>
	        <div class="dialog-title-bar clearfix">
			    <div class="dialog-bar-l"></div>
				<div class="dialog-bar-m">
			        <h2>现有技能</h2>
				</div>
				<div class="dialog-bar-r"></div>
			</div>
			<%--标题 end--%>
			<%--tab 切换--%>
			<div class="dialog-content clearfix">
			    <div class="dialog-content-box">
				   <table id="mouseStaffSkillTable" class="dialog-content-info"></table>
				</div>
			</div>
			<%--tab 切换 end --%>
			<%--底部阴影 --%>
			<div class="dialog-foot">
			    <div class="dialog-foot-l"></div>
				<div class="dialog-foot-r"></div>
				<div class="dialog-foot-i"></div>
			</div>
			<%--底部阴影 end --%>
		</div>
		
	</div>
  	<%-- 人员技能鼠标悬浮窗 END --%>
</body>
</html>
