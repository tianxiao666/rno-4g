<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>人员信息查看</title>
<link rel="stylesheet" type="text/css" href="../../css/base.css" />
<link rel="stylesheet" type="text/css" href="../../css/public.css" />
<link rel="stylesheet" href="css/p_tab.css" type="text/css" />
<link rel="stylesheet" href="css/p_information.css" type="text/css" />
<link rel="stylesheet" type="text/css" href="../../jslib/paging/iscreate-paging.css" />
<%-- 加载地图模块需要引入的文件 --%>
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="../../jslib/paging/paging.js"></script>
<script type="text/javascript" src="../../jslib/date/wdatePicker.js"></script>
<script type="text/javascript" src="js/showStaffInfo.js"></script>
</head>

<body>
<div class="p_container"> 
    <%--主体开始--%>
    <div class="pi_right">
        <div class="pi_menu_title clearfix">
            <p align="center">人员统一信息</p>
            <input type="hidden" id="hiddenStaffId" value="${param.staffId}"/>
        </div>
        
        <%-- content开始 --%>
        <div class="pi_right_content">
            <div class="tab1">
                <div class="tab_menu">
                    <ul>
                        <li class="ontab">基本信息</li>
                        <li id="recordInfoLi" style="display:none;">档案信息</li>
                        <li id="resourceBoxLi" style="display:none;">储物箱</li>
                        <li id="dutyInfoLi">值班信息</li>
                        <li id="taskInfoLi">他的任务</li>
                    </ul>
                </div>
                
                <div class="tab_container">
                    <div class="tab_content">
                        <div class="content_left">
                        	<img style="height: 190px;width: 180px;" src="image/man.jpg" id="staffPicture"/>
                        </div>
                        <div class="content_right">
                        	<table class="pi_table">
                            	<tr>
                                	<td class="menuTd">姓名：</td>
                                	<td id="staffName"></td>
                                	<td class="menuTd">性别：</td>
                                	<td id="staffSex"></td>
                                </tr>
                            	<tr>
                                	<td class="menuTd">IT帐号：</td>
                                	<td id="itAccount"></td>
                                	<td class="menuTd">出生年月：</td>
                                	<td id="birthday"></td>
                                </tr>
                            	<tr>
                                	<td class="menuTd">身份证号码：</td>
                                	<td id="idCardNum"></td>
                                	<td class="menuTd">邮箱：</td>
                                	<td id="email"></td>
                                </tr>
                            	<tr>
                                	<td class="menuTd">联系电话：</td>
                                	<td id="cellPhoneNumber"></td>
                                	<td class="menuTd">集团短号：</td>
                                	<td id="shortNumber"></td>
                                </tr>
                            	<tr>
                                	<td class="menuTd">当前地址：</td>
                                	<td id="address"></td>
                                	<td class="menuTd">当前位置：</td>
                                	<td>
                                		<div id="staffLocation">114.22222 , 23.55555</div>
                                    	<%--<em class="pi_position_ico" title="点击在地图显示" id="staffGisButton"></em>  --%>
                                    </td>
                                </tr>
                            	<tr>
                                	<td class="menuTd">用户群：</td>
                                	<td id="staffType"></td>
                                	<td colspan="2">
                                    	<%--<a class="mr10" href="#">手机终端历史轨迹</a>  --%> &nbsp;
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <div class="content_m">
                        	<h4 class="content_m_tit">
                            	<span class="doc_tit">所属组织/角色/技能</span>
                            </h4>
                            <ul class="pi_ul">
                            	<li>
                                	<em class="menuEm">所属组织：</em>
                                    <p id="belongTo">  </p>
                                    
                                    <em class="menuEm ml50">组织驻地：</em>
                                    <p>
                                    	<p id="orgAddress">  </p>
                                    	<input type="hidden" id="hiddenOrgId"/>
                                    	<%--<em class="pi_position_ico" title="点击在地图显示" id="orgGisButton"></em>  --%>
                                    </p>
                                </li>
                                
                            	<li>
                                	<em class="menuEm">赋予角色：</em>
                                    <div class="s_table_div">
                                        <table class="s_table tc" id="staffRoleTable">
                                            <tr>
                                                <th>角色</th>
                                            </tr>
                                        </table>
                                    </div>
                                </li>
                                
                            	<li>
                                	<em class="menuEm">人员技能：</em>
                                    <p id="staffSkill"></p>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div class="tab_content" style="display:none;">
                        <%-- 档案信息 --%>
                        
                    </div>
                    <div class="tab_content" style="display:none;">
                        <h4 class="content_m_tit">
                            <span class="right_tool_bar">
                            	<select id="meType">
                                	<option value="all">物资目录</option>
                                	<option value="assets">工具资产</option>
                                	<option value="part">设备备件</option>
                                	<option value="consumable">易耗品</option>
                                </select>
                                     入储物箱时间从
                                <input type="text" id="meBeginTime" /><a class="date_button" onclick="fPopCalendar(event,document.getElementById('meBeginTime'),document.getElementById('meBeginTime'),true)"></a>
                                	到
                                <input type="text" id="meEndTime" /><a class="date_button" onclick="fPopCalendar(event,document.getElementById('meEndTime'),document.getElementById('meEndTime'),true)"></a>
                                      物资名称：<input type="text" id="meName"/>
                                <a href="#" class="search_button" id="meSearchButton"></a>
                            </span>
                        </h4>
                        <div class="tab_inside">
                        	<div class="tab_inside_ul">
                            	<ul>
                                    <li class="tab_inside_first_li">
                                         	 工具资产
                                        <em class="blue" id="assetsCount"> </em>
                                    </li>
                                    <li class="tab_inside_ontab">
                                        	通用耗材
                                        <em class="blue" id="consumableCount"> </em>
                                    </li>
                                    <li>
                                        	设备配件
                                        <em class="blue" id="partCount"> </em>
                                    </li>
                                </ul>
                            </div>
                            
                            <div class="tab_inside_content" style="display:none;">
                            	<table class="pi_table2 tc" id="assetsTable">
                                    <%-- 工具资产 Table --%>
                                </table>
                                <div id="assetsPagingDiv"></div>
                            </div>
                        	
                            <div class="tab_inside_content">
                            	<table class="pi_table2 tc" id="consumableTable">
                                    <%-- 易耗品 Table --%>
                                </table>
                                <div id="consumablePagingDiv"></div>
                            </div>
                            
                            <div class="tab_inside_content" style="display:none;">
								<table class="pi_table2 tc" id="partTable">
                                    <%-- 设备备件 Table --%>
                                </table>
                                <div id="partPagingDiv"></div>
							</div>
                        </div>
                        
                    </div>
                    <div class="tab_content" style="display:none;">
                    	<%@ include file="linkstaffPlanDuty.jsp"%>
                    </div>
                    <div class="tab_content" style="display:none;">
                        <h4 class="content_m_tit">
                        	<input type="checkbox" class="taskStatus" value="pending" checked="checked" onclick="getStaffTaskInfo()"/>未完成
                            <input type="checkbox" class="taskStatus"value="hasDone" onclick="getStaffTaskInfo()"/>已完成
                            <span class="right_tool_bar">
                            	<select id="taskType">
                                	<option value="all">任务类型</option>
                                	<option value="urgentrepair">抢修任务</option>
                                	<option value="xunjian">巡检任务</option>
                                	<option value="cardispatch">车辆任务</option>
                                </select>
                                &nbsp;创建时间从&nbsp;
                                <input type="text" id="taskBeginTime" onfocus="var dd=$dp.$('taskEndTime');WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:function(){taskEndTime.focus();},maxDate:'#F{$dp.$D(\'taskEndTime\')}'})" /><a></a>
                                &nbsp;到&nbsp;
                                <input type="text" id="taskEndTime" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'taskBeginTime\')}'})" /><a></a>
                                <input type="text" value="" id="taskName"/>
                                <a href="#" class="search_button" id="taskSearchButton"></a>
                            </span>
                        </h4>
                        <div class="table_div">
                            <table class="pi_table2 tc" id="taskInfoTable">
                                <tr>
                                    <th style="width:120px;">任务类型</th>
                                    <th style="width:120px;">任务编号</th>
                                    <th>任务主题</th>
                                    <th style="width:130px;">创建时间</th>
                                    <th style="width:130px;">截止时间</th>
                                    <th style="width:130px;">完成时间</th>
                                    <th style="width:30px;">历时(小时)</th>
                                    <th style="width:20px;">是否超时</th>
                                </tr>
                            </table>
                            <div class="table_div_bottom pr">
                                <div id="taskPageContent"></div>
                                <div class="task_tips">
                                	任务单：<em class="red" id="taskTotalNumber">0</em> ,
                                         超时任务：<em class="red" id="overTaskNumber">0</em> ,
                                         任务单平均历时：<em class="red" id="taskAvgSpendTime">0</em>小时
                                </div>
                            </div>
        				</div>
                    </div>
                </div>
            </div>
        </div>
        <%-- content结束 --%> 
        
    </div>
    <%--主体结束--%> 
</div>
</body>
</html>
