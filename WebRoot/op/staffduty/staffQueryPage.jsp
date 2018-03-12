<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>人员状态综合查询</title>
<link rel="stylesheet" type="text/css" href="../../css/base.css" />
<link rel="stylesheet" type="text/css" href="../../css/public.css" />
<link rel="stylesheet" type="text/css" href="../../css/public-table.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery.treeview.css" />
<link type="text/css" rel="stylesheet" href="../../jslib/gantt/gantt_small.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery-ui-1.8.23.custom.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/paging/iscreate-paging.css" />
<%-- 加载地图模块需要引入的文件 --%>
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="../../jslib/gis/jslib/api/iscreate_map.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.ui.datepicker.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js"></script>
<script type="text/javascript" src="../../jslib/gantt/gantt.js"></script>
<script type="text/javascript" src="../../jslib/paging/paging.js"></script>
<script type="text/javascript" src="../jslib/generateTree.js"></script>
<script type="text/javascript" src="../../jslib/date/wdatePicker.js"></script>
<script type="text/javascript" src="js/staffQuery.js"></script>

<style type="text/css">
.orgSelect{background: url("images/select.png") no-repeat scroll 0 0 transparent; display: inline-block;height: 24px;top:2px;left:126px;position: absolute;width: 24px;}
/* 选择区域组织架构树 */
#simpleQueryButton{display:inline-block; height:25px; width:25px; position:relative;top:-3px; vertical-align:middle;margin-right: 3px;background:url("images/search.png") no-repeat; height:25px; width:25px;}
#treeDiv{ display:none;z-index: 9; position:absolute; right:200px;overflow:auto; background:#fff; border:1px solid #ccc; border-top:none;width: 190px; min-height:186px; text-align:left;margin-top:1px; line-height:18px;}
/* 组织架构树相关样式 */
#orgTree span{ cursor:pointer;}
#orgTree span:hover{ background:#eee;}
/* 弹出框 */
.advanced-search { display:none;background:#F9F9F9;border:1px solid #999999;position: absolute;right: 0px;margin-top:-10px;width:430px;z-index: 5;}
.advanced-search-title {background: none repeat scroll 0 0 #EEEEEE;border-bottom: 1px solid #999999;line-height: 30px;padding-left: 10px;}
/*gis地图右边框样式*/
.gis_right_information{ position:absolute;right:0px;height:100%; width:300px; border-left:1px solid #ccc;}
.gis_right_information_top{ line-height:28px; border:1px solid #ccc; border-left:none; border-right:none; text-align:center; background:#eee;}
.close_gis_right{height:20px; width:20px;position:absolute; right:5px;top:5px; background:url(images/shut_down.png) no-repeat; border:1px solid #bbb; border-radius:3px;}
/*存放甘特图*/
.forGantt{ height:130px; width:300px; border-top:1px solid #ccc;}
/*选择时间按钮*/
.date_button{height:20px; width:20px; background:url(images/date_choose.png) no-repeat;display: inline-block; position:absolute; margin:1px 0px 0px -22px;}


/* tab上方工具条 */
.tab2_right_tool{position:absolute;right:0px;margin-top:-30px;margin-right:5px;}

/* 关闭按钮-人员信息小弹窗 */
.people_information_top{position: relative; left: 167px; height: 18px; width: 18px; background: url(images/2011122704363364_easyicon_cn_24.png) no-repeat; margin-top: -27px; top: 22px; border:1px solid #CCC; cursor:pointer;}
/*技能鼠标浮窗*/
.skilldialog-box {position: absolute; width:300px;}
.skilldialog-title-bar { background: url("image/dialog-top.png") repeat-x;}
.skilldialog-bar-l{ background: url("image/dialog-bg.png") no-repeat; background-position: 0px 0px; width:20px; height: 40px; float:left;}
.skilldialog-bar-m{ float:left;}
.skilldialog-bar-r{ background: url("image/dialog-bg.png") no-repeat; background-position: -79px 0px; width:20px; height: 40px; float:right;}
.skilldialog-title-bar h2 { font-size: 14px; font-weight: 700;line-height: 40px;}
.skilldialog-title-bar a.close-dialog {background-color: #FFFFFF;border: 1px solid #999999;display: block; font-size: 14px; font-weight: 700; height: 18px; line-height: 18px; position: absolute;right: 15px;top: 10px;width: 18px;color: #999999;text-align: center;text-decoration: none;}
.skilldialog-title-bar a.close-dialog:hover {color: #333333;text-decoration: none;}
.skilldialog-content {background-color: #E6E6E6;}
.skilldialog-content-box{background-color: #ffffff; margin: 0 5px;border:1px solid #dbdbdb; border-top:none; border-bottom:none;}
.skilldialog-content-info p{padding:5px 10px;font-size:12px;}
.skilldialog-content-info{width:100%; text-align:center;}
.skilldialog-content-info th{border-bottom:1px solid #eee; padding:4px 6px; color:#999; font-weight:bold;}
.skilldialog-content-info td{border-bottom:1px solid #eee; padding:4px 6px;}

.skilldialog-foot{background: url("image/dialog-bottom.png") repeat-x; height: 45px; margin:0 20px 0 50px;}
.skilldialog-foot-l{ background: url("image/dialog-bg.png") no-repeat; background-position: 0px -54px; width:50px; height: 45px; float:left; margin-left: -50px;}
.skilldialog-foot-r{ background: url("image/dialog-bg.png") no-repeat; background-position: -79px -54px; width:20px; height: 45px; float:right; margin-right: -20px;}
.skilldialog-foot-i{ background: url("image/dialog-arrow.png") no-repeat; width:50px; height: 45px; left: 100px;position: relative;}
.skilldialog-right{background: url(image/dialog-right.png) no-repeat; height:140px; float: left; position: absolute; bottom: 0; right: -90px; width:90px;}

.clearfix {zoom:1;}
.dialog-bar-m {width: 302px;}
.dialog-title-bar h2 {font-size:14px;font-weight:700;line-height:40px;}
.dialog-title-bar a.close-dialog {background-color:#FFF;border:1px solid #999;display:block;font-size:14px;font-weight:700;height:18px;line-height:18px;position:absolute;right:15px;top:10px;width:18px;color:#999;text-align:center;text-decoration:none;}
.dialog-title-bar a.close-dialog:hover {color:#333;text-decoration:none;}
.dialog-bar-m {padding-bottom:5px;}
.dialog-bar-m h3{ padding: 2px 0;}
.dialog-bar-m b{ display: inline-block; font-weight:bold ;text-align: right;vertical-align: middle;min-width:50px; }
.dialog-bar-m label{font-weight:bold ;display: inline-block;text-align: right;vertical-align: middle;min-width:40px;}
.dialog-bar-m span{word-break: break-all; color: #666;text-align: left;vertical-align: middle;}
.dialog-content {width:325px;}
.dialog-content-box {background-color:#fff;border-top:none;}
.dialog-content-info,.dialog-content-main{font-size:12px;padding:3px 8px;}
.dialog-tabmenu li {background:#f0f0f0;cursor: pointer; float: left; height: 24px; line-height: 24px;text-align: center;width: 161px;border-left: 1px solid #CCCCCC; border-top: 1px solid #CCCCCC;}
.dialog-tabmenu li.on {background:#fff;color:#111;border-bottom:1px solid #fff; margin-bottom:-1px;}
.dialog-tabcontent dd { margin:0 auto;padding:2px;height:130px;overflow-y:scroll;}
.dialog-tabcontent .dialog-task{overflow-x: hidden;overflow-y: auto;}
.dialog-tabcontent dd table{width:100%;border-collapse: collapse; border-spacing: 0;}
.dialog-tabcontent dd table td{border: 1px solid #FFFFFF;padding:3px 0; text-align:left;}
.dialog-tabcontent{border: 1px solid #CCCCCC;}

</style>
</head>

<body>
<div id="container">
	<div class="container-main">
		<div class="container-main-table1">
			<table class="main-table1">
				<tr class="main-table1-tr">
					<td class="main-table1-title" id="tr">
						<span class="fr" style="position:relative;">
						
							<input type="hidden" id="bizunitIdText" value="24"/>
	                    	<input type="text" id="bizunitNameText" readonly="readonly"  style="padding:3px; width:140px;"/>
	                        <a class="orgSelect" id="orgSelectButton" title="选择组织" href="javascript:void(0);"></a>
	                        <div id="treeDiv">
	                            <ul id="tree1" class="treeview">
	                                <li><span>惠州区调度中心</span>
	                                    <ul>
	                                        <li><span>江北基站维护队</span></li>
	                                        <li><span>陈江基站维护队</span></li>
	                                        <li><span>陈江传输维护队</span></li>
	                                    </ul>
	                                </li>
	                            </ul> 
	                        </div>
	                   		 <input id="staffName" name="staffName" type="text" value="输入姓名查询" />
	                        <a href="#" id="simpleQueryButton"></a>
	                        <input type="button" id="gaojisousuo" value="高级搜索" />
	                    </span>
                    </td>
                </tr>
			</table>
            <div class="advanced-search">
            	<div class="advanced-search-title">
                	<h4>人员综合查询</h4>
                </div>
                <table class="main-table1" style="margin-bottom:0px;">
                    <tr>
                        <td class="menuTd">专业技能：</td>
                        <td>
                           <%--<select id="skillSelect">
                        		<option value="请选择"> 请选择技能分类 </option>
                        		<s:iterator value="#skillList" id="skill">
                        			<option value="${skill.id}"> ${skill.skillType} </option>
                        		</s:iterator>
                        	</select>
                            &nbsp;&nbsp;
                            --%>
                            <select id="skillSelect">
                        		<option value="请选择">请选择</option>
                        	</select>
                        </td>
                    </tr>
                   <%--<tr>
                        <td class="menuTd">工作年限：</td>
                        <td>
                            <input type="text" id="experienceAge" />
                        </td>
                    </tr>  --%> 
                    <tr>
                        <td class="menuTd">性别：</td>
                        <td>
                            <select id="staffSex">
                                <option value=""> 全部 </option>
                                <option value="男"> 男 </option>
                                <option value="女"> 女 </option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="menuTd">值班时间范围：</td>
                        <td>
                            <input type="text" id="beginDate" readonly="readonly" onfocus="var dd=$dp.$('endDate');WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:function(){endDate.focus();},maxDate:'#F{$dp.$D(\'endDate\')}'})" />
                            <a href="#"  ></a>
                            至
                             <input type="text" id="endDate" readonly="readonly" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'beginDate\')}'})" />
                             <a href="#"  ></a>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" class="tc">
                            <input id="multiSearchButton" type="button" class="advanced-search-hide" value="查询"/>
                            <input type="button" class="search-no" value="取消" onclick="$('.advanced-search').toggle('fast');" />
                        </td>
                    </tr>
                </table>
            </div>
		</div>
        
		<div class="container-main-table1-right">
            <div class="container-tab2 clearfix">
                <ul>
                    <li id="listViewButton" class="tab2-li-show"><a href="#" onclick="return false;">列表呈现</a></li>
                    <li id="mapViewButton"><a href="#" onclick="return false;">GIS呈现</a></li>
                </ul>
            </div>
             <div class="container-tab2-right" style="top: -25px;display:none">
                <input type="button" value="加入候选名单" id="addToCollection" />
                <div id="shoucangjia">
                    <a href="showStaffListToStaffCollectionAction">人员候选名单</a>
                </div>
            </div>
			<div id="listView" class="container-main-table1 container-main-table1-tab" >
                 <table id="queryResultTab" class="main-table1 main-table2">
					<tr>
						<th><input id="allChecked" type="checkbox" />全选</th>
						<th>人员姓名</th>
                        <th>联系电话</th>
                        <th>性别</th>
                        <th>任务数</th>
                        <th>专业技能</th>
					</tr>
				</table>
			</div>    
         	<div id="mapView" class="container-main-table1-tab" style="top: -10px;display:none;width:100%;height:600px"></div>
            <%-- 分页组件  --%>
            <div id="pageContent"></div>
            
            </div>
	</div>
</div>
        <div id="people-information">
        	<table class="main-table1">
            	<tr>
                	<td colspan="2" class="table-head">
                		</div><span id="stTitle"></span><br />
                	</td>
                </tr>
                <tr>
                	<td class="menuTd">员工姓名：</td>
                    <td><span id="stName"></span></td>
                </tr>
                <tr>
                    <td class="menuTd">性别：</td>
                    <td><span id="stSex"></span></td>
                </tr>
                <tr>
                    <td class="menuTd">联系电话：</td>
                    <td><span id="stContactPhone"></span></td>
                </tr>
               	<tr>
                    <td class="menuTd">QQ号码：</td>
                    <td><span id="stQQNum"></span></td>
                </tr>
                <tr>
                    <td class="menuTd">Email：</td>
                    <td><span id="stEmail"></span></td>
                </tr>
            </table>
        </div>
        
    <%-- 人员技能鼠标悬浮窗 BEGIN--%>
	<div id="dialog-pop" class="skilldialog-box" style="display: none">
	    <div class="skilldialog-popup">
		    <%--标题 --%>
	        <div class="skilldialog-title-bar clearfix">
			    <div class="skilldialog-bar-l"></div>
				<div class="skilldialog-bar-m">
			        <h2>现有技能</h2>
				</div>
				<div class="skilldialog-bar-r"></div>
			</div>
			<%--标题 end--%>
			<%--tab 切换--%>
			<div class="skilldialog-content clearfix">
			    <div class="skilldialog-content-box">
				   <table id="mouseStaffSkillTable" class="skilldialog-content-info"></table>
				</div>
			</div>
			<%--tab 切换 end --%>
			<%--底部阴影 --%>
			<div class="skilldialog-foot">
			    <div class="skilldialog-foot-l"></div>
				<div class="skilldialog-foot-r"></div>
				<div class="skilldialog-foot-i"></div>
			</div>
			<%--底部阴影 end --%>
		</div>
		
	</div>
  	<%-- 人员技能鼠标悬浮窗 END --%>
  		
	 <%-- 放置甘特图插件  --%>
	  <div id="ganttContent" style="display: none;  position: absolute; z-index: 250;background:#F9F9F9;border: 1px solid #CCCCCC;">
	  	
	  </div>
	  <%-- 甘特图 END --%>
	  
</body>
</html>

