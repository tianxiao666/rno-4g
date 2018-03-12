<%@ page language="java" import="java.util.*" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ page language="java" import="java.util.Date"%>
<%@ page language="java" import="java.text.DateFormat"%>
<%@ page language="java" import="java.text.SimpleDateFormat"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>创建工单-创建故障抢修工单</title>
<link rel="stylesheet" type="text/css" href="../../css/base.css" />
<link rel="stylesheet" type="text/css" href="../../css/input.css" />
<link rel="stylesheet" type="text/css" href="../../css/public.css" />
<link rel="stylesheet" type="text/css" href="../css/selectStation.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery.treeview.css" />
<link rel="stylesheet" type="text/css" href="../../css/public-table.css" />
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js" ></script>
<script type="text/javascript" src="../../jslib/common.js"></script>
<script type="text/javascript" src="jslib/public.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js" ></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.form.js"></script>
<script type="text/javascript" src="jslib/pageSection.js"></script>
<script type="text/javascript" src="../jslib/baseStationSingleDivPage.js"></script>
<script type="text/javascript" src="../../jslib/propertyDictionary.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.metadata.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.validate.js"></script>
<script type="text/javascript" src="../../jslib/validate.ex.js"></script>
<script type="text/javascript" src="../../jslib/date/wdatePicker.js"></script>
<script type="text/javascript" src="jslib/createWorkOrder.js"></script>
<%DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");%>
<style type="text/css">
#stationList input[type="radio"]{left: 0; position: static; top: 0px;}
.selectStation-table tr td.rows1{white-space: normal;}
.selectStation-title{ width:100%-1px; text-align:left; padding-left:10px; border:1px solid #99BBE8; color:#15428B; font-size:13px; font-weight:bold; background:url(../../images/white-top-bottom.gif) repeat-x; line-height:30px;}
.selectStation-main{ width:100%; margin:10px 0; height:300px; overflow:scroll; overflow-x:hidden;}
.selectStation-bottom{ width:100%; text-align:center; margin:5px 0;}
.selectStation-table{ width:100%;}
.selectStation-table tr{ border:1px solid #fff; border-bottom:1px solid #CCCCCC;}
.selectStation-table tr td{padding:2px 5px; border-left:1px solid #fff; width:25%; white-space:nowrap;}
.selectStation-table tr td:hover{ background:#E8EDFF; color:#0033FF; cursor:pointer; border:1px solid #ccc;}
input[type="radio"]{background:none; border:none;}

.info {
	position: absolute;
	border: 1px solid #000000;
	width: 200px;
	margin:15% auto,auto;
	left: 40%;
	background-color: #FFFFFF;
	color: #000;
	font-size: 20px;
	font-style: bold;
	font-family: 微软雅黑;
	vertical-align: middle;
	z-index:250;
	top:50%;
}
.select_tree{display:none;position:absolute;height:300px; width:200px;background:#fff; border-radius:3px;box-shadow:2px 2px 5px #000;overflow:auto;border:1px solid #ccc; z-index:2;}
</style>
<script>
$(function(){
	$(".show_select_tree").click(function(){
		$(".select_tree").slideDown("fast");
	})
})
</script>
</head>

<body>
	
	<div id="loading_div" class="loading_div" style="display:none">
		<div class="loading_img">
        	<img src="../../images/loading_img.gif" /><br />
        	数据加载中，请稍后...
        </div>
	</div>
	
	<div id="header960">
    	<div class="header-top960"><h2>创建故障抢修工单</h2></div>
        <div class="header-main">
        	<span class="text-l">工单号：</span>
            <span>创建时间：</span>
            <span class="text-r">状态：</span>
       	</div>
    </div>
    <%--头部结束--%>
    <input type="hidden" id="gis" value="${stationOfGIS['gis']}"/>
    <input type="hidden" id="gis_id" value="${stationOfGIS['gis_id']}"/>
	<input type="hidden" id="gis_name" value="${stationOfGIS['gis_name']}"/>
	<input type="hidden" id="gis_type" value="${stationOfGIS['gis_type']}"/>
	<input type="hidden" id="gis_longitude" />
	<input type="hidden" id="gis_latitude" />
	<input type="hidden" id="gis_location" />
	<input type="hidden" id="gis_stationId" />
	<input type="hidden" id="gis_stationName" />
	<input type="hidden" id="gis_stationType" />
	<input type="hidden" id="gis_Area" />
	<input type="hidden" id="gis_importancegrade" />
	
	<input type="hidden" id="moudleId" value="${moudleId}"/>
	<input type="hidden" id="content" value="${content}"/>					
    <div id="container960">
    		<%--tab1开始--%>
    	<form id="form1" action="createUrgentRepairWorkOrderAction" method="post" enctype="multipart/form-data">
    	<input type="hidden" name="commonWorkOrder.wotempId"
			value="${param.wotempId}" />	
    	<div class="container-tab1">
        	<fieldset id="fieldset-1">
            	<legend>
                	<input type="checkbox" checked="checked" /><span>工单信息</span>
                    	
                </legend>
                
                <div class="container-main">

                    
                    <div class="container-main-table1 container-main-table1-tab">
                    
                    	<table class="main-table1 half-width">
                        	<tr class="main-table1-tr"><td colspan="4" class="main-table1-title"> <img class="hide-show-img" src="images/ico_show.gif" />故障工单信息</td></tr>
                            <tr>
                            	<td class="menuTd">工单主题：</td>
                                <td colspan="3">
                                <input id="woTitle" name="workmanageWorkorder.woTitle" type="text"
								style="width: 90%;" class="required input-text  {required:true,maxlength:80}" /><span class="red">*</span></td>
                            </tr>
                            <tr>
                            	<td class="menuTd">告警基站：</td>
                                <td>
                                	<input class="required input-text  {required:true,maxlength:100}" readonly="readonly"  name="urgentrepairWorkorder.faultStationName" type="text" id="selectStationName" />
									<input type="hidden" name="workorderassnetresource.networkResourceId" id="selectStationId" /><span class="red">*</span>
									<input type="button" value="选择基站" onclick="openCommonBaseStationSingleDivByAccount()" id="stationSelectButton" />
									<input type="hidden" name="workorderassnetresource.networkResourceType" id="stationType" />
                                </td>
                                <td class="menuTd">告警网元名称：</td>
                                <td>
                                	<input type="text" name="urgentrepairWorkorder.netElementName" id="netElementName" class="{maxlength:100}"/>
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd">区域：</td>
                                <td >
									<input id="stationOfArea" style="width:280px;" type="text" name="urgentrepairWorkorder.faultArea" class="{required:true,maxlength:60}"/>
									<input type="hidden"  id="areaId" />
									<span class="red">*</span>
									<%-- <div class="select_tree">
										<ul id="tree">
										</ul> --%>
									</div>
                                </td>
                                <td class="menuTd">站址名称：</td>
                                <td>
                                	<input type="text" id="station_Name" name="urgentrepairWorkorder.stationName" class="{required:true,maxlength:100}"/>
                                	<input type="hidden" id="station_id" name="workorderassnetresource.stationId" />
                                </td>
                            </tr>
                            
                            <tr>
                            	<td class="menuTd">站址地址：</td>
                                <td colspan="3">
                                	<input id="stationLocation" style="width:90%" type="text" name="urgentrepairWorkorder.faultStationAddress"  class="{required:true,maxlength:200}"/>
                                	<input type="hidden"  name="commonWorkOrder.longitude" id="lng" />
									<input type="hidden"  name="commonWorkOrder.latitude"  id="lat"/>
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd">基站等级：</td>
                                <td>
                                	<input id="baseStationLevel" name="urgentrepairWorkorder.baseStationLevel" validateIsEmpty="#baseStationLevel" readonly="readonly">
                                    </input><span class="red">*</span>
                                </td>
                                <td class="menuTd">受理专业：</td>
                                <td>
                                	<select name="urgentrepairWorkorder.acceptProfessional" id="acceptProfessional" validateIsEmpty="#acceptProfessional"> 
                                	<option value="">请选择</option>
                                    </select><span class="red">*</span>
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd">故障类型：</td>
                                <td>
                                	<select id="faultType" name="urgentrepairWorkorder.faultType" id="faultType" validateIsEmpty="#faultType">
                                    <option value="">请选择</option>
                                    </select><span class="red">*</span>
                                </td>
                                <td class="menuTd">故障级别：</td>
                                <td>
                                	<select id="faultLevel" name="urgentrepairWorkorder.faultLevel" id="faultLevel" validateIsEmpty="#faultLevel">                     
                                    </select><span class="red">*</span>
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd">故障发生时间：</td>
                                <td>
                                	<input type="hidden" value="<%=dateFormat.format(new Date())%>" id="nowTime"/>
                                	<input type="hidden"  id="oldFaultOccuredTime" value=""/>
									<input type="text" id="faultOccuredTime" name="urgentrepairWorkorder.faultOccuredTime" onFocus="var dd=$dp.$('latestAllowedTime');WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:function(){latestAllowedTime.focus();},maxDate:'#F{$dp.$D(\'latestAllowedTime\')}'})" readonly class="Wdate required input-text" value="<%=dateFormat.format(new Date())%>"/>
									<span class="red">*</span>	
                                </td>
                                <td class="menuTd">截止解决时间：</td>
                                <td>
									<input type="text" id="latestAllowedTime" name="urgentrepairWorkorder.latestAllowedTime" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'faultOccuredTime\')}'})" readonly class="Wdate required input-text"/>
									<span class="red">*</span>	
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd higherLine">故障描述：</td>
                                <td colspan="3">
                                	<textarea rows="4" style="width:90%" name="urgentrepairWorkorder.faultDescription" class="required input-text {required:true,maxlength:1000}" id="faultDescription"></textarea><span class="red">*</span>
                                </td>
                                
                            </tr>
                        </table>
                        
                        <table class="main-table1 half-width">
                        	<tr class="main-table1-tr"><td colspan="4" class="main-table1-title"><img class="hide-show-img" src="images/ico_show.gif" />客户工单信息</td></tr>
                            <tr>
                            	<td class="menuTd">客户工单编号：</td>
                                <td colspan="3">
                                	<input type="text" style="width:90%" name="urgentrepairCustomerworkorder.customerWoId" id="customerWoId" class="{maxlength:200}"/>
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd">客户工单主题：</td>
                                <td colspan="3">
                                	<input type="text" style="width:90%" name="urgentrepairCustomerworkorder.customerWoTitle" id="customerWoTitle" class="{maxlength:200}"/>
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd">派单人部门：</td>
                                <td>
                                	<select name="urgentrepairCustomerworkorder.customerSenderDepartment">
                                		<option value=""> 请选择</option>
                                        <option value="综合室"> 综合室 </option>
                                        <option value="支撑室"> 支撑室 </option>
                                        <option value="网管室"> 网管室 </option>
                                        <option value="其他"> 其他 </option>
                                    </select>
                                </td>
                                <td class="menuTd">派单人：</td>
                                <td>
                                	<input type="text" name="urgentrepairCustomerworkorder.customerWoSender" />
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd">当前处理人部门：</td>
                                <td>
                                	<select name="urgentrepairCustomerworkorder.customerWoCurrentDepartment" class="valid">
                                		<option value="">请选择</option>
                                        <option value="综合室"> 综合室 </option>
                                        <option value="支撑室"> 支撑室 </option>
                                        <option value="网管室"> 网管室 </option>
                                        <option value="其他"> 其他 </option>
                                    </select>
                                </td>
                                <td class="menuTd">当前处理人：</td>
                                <td>
                                	<input type="text" name="urgentrepairCustomerworkorder.customerWoCurrentHandler" />
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd">是否紧急故障：</td>
                                <td>
                                	<select name="urgentrepairCustomerworkorder.isEmergencyFault" id="isEmergencyFault">
                                       <option value="">请选择</option>
                                        <option value="否">
											否
										</option>
										<option value="是">
											是
										</option>
                                    </select>
                                </td>
                                <td class="menuTd">派单方式：</td>
                                <td>
                                	<select name="urgentrepairCustomerworkorder.sendWoWay" id="sendWoWay">
                                		<option value="">请选择</option>
                                        <option value="自动"> 自动 </option>
                                        <option value="半自动"> 半自动 </option>
                                        <option value="手动"> 手动 </option>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd">是否影响业务：</td>
                                <td>
                                	<select id="sideeffectService" name="urgentrepairCustomerworkorder.sideeffectService">
                                		<option value="-1">请选择</option>
                                        <option value="0">
											否
										</option>
										<option value="1">
											是
										</option>
                                    </select>
                                </td>
                                <td class="menuTd">受影响业务：</td>
                                <td>
                                	<input type="text" id="affectedServiceName" name="urgentrepairCustomerworkorder.affectedServiceName" disabled="disabled" />
                                	<em id="tip" style="font-family: Arial; font-size: 18px; font-weight: bold; font-style: normal; text-decoration: none; color: #FF0000; display:none">*</em>
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd">告警类别：</td>
                                <td>
                                	<input type="text" name="urgentrepairCustomerworkorder.alarmClass" id="alarmClass"/>
                                </td>
                                <td class="menuTd">网管告警级别：</td>
                                <td>
                                	<input name="urgentrepairCustomerworkorder.netmanageAlarmLevel" id="netmanageAlarmLevel">
                                     </input>
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd">告警标准名：</td>
                                <td>
                                	<input type="text" name="urgentrepairCustomerworkorder.alarmFormalName" class="{maxlength:100}"/>
                                </td>
                                <td class="menuTd">告警网管来源：</td>
                                <td>
                                	<select name="urgentrepairCustomerworkorder.alarmNetManageSource" id="alarmNetManageSource">
                                     </select>
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd">告警逻辑分类：</td>
                                <td>
                          	    	<select id="alarmLogicalClass"	name="urgentrepairCustomerworkorder.alarmLogicalClass">

									</select>
                                </td>
                                <td class="menuTd">告警逻辑子类：</td>
                                <td>
                                	<select id="alarmLogicalSubClass"	name="urgentrepairCustomerworkorder.alarmLogicalSubClass">
										<option value="">请选择</option>
									</select>
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd">告警关联标识：</td>
                                <td>
                                	<input type="text" name="urgentrepairCustomerworkorder.alarmAssociatedId" id="alarmAssociatedId"/>
                                </td>
                                <td class="menuTd">子告警数量：</td>
                                <td>
                                	<input type="text" name="urgentrepairCustomerworkorder.subAlarmNumber" id="subAlarmNumber" class="{maxlength:1000}"/>
                                </td>
                            </tr>
                            
                            <tr>
                                <td class="menuTd higherLine">子告警信息：</td>
                                <td colspan="3">
                                	<textarea rows="4" style="width:90%" name="urgentrepairCustomerworkorder.subAlarmInfo" id="subAlarmInfo"></textarea>
                                </td>
                            </tr>

                        </table>
                    </div>
                    <%--tab第一部分结束--%>
                    
                </div>
               
            </fieldset>
            <div class="container-bottom">
            	<input type="submit" value="创  建" style="width:60px;" name="save" />
            </div>
        </div>
        	<%--tab1结束，可重写--%>
        </form>
    </div>
    <%--container结束--%>
    <%-- 弹出站址 --%>
    <div id="stationList" style="position:fixed; left:50%; top:50px; z-index:300; width:850px; margin-left:-420px; border:2px solid #999; border-radius:5px;box-shadow:2px 2px 3px #999;background:#fff; display:none;">
	    <input type="hidden" id="common_id" />
		<input type="hidden" id="common_name" />
		<input type="hidden" id="common_type" />
		<input type="hidden" id="common_longitude" />
		<input type="hidden" id="common_latitude" />
		<input type="hidden" id="common_location" />
		<input type="hidden" id="common_stationId" />
		<input type="hidden" id="common_stationName" />
		<input type="hidden" id="common_stationType" />
		<input type="hidden" id="common_Area" />
		<input type="hidden" id="common_importancegrade" />
		<input type="hidden" id="common_orgId" />
		
<div class="selectStation-title">站址列表
</div>
	 <div id="searchStation">	
           	<span>首字母索引：</span>
		   	<em class="selectStation-title-search">
		        <input type="text" id="rex"/>&nbsp;
				<input type="button" id="search" value="快速查询" onclick="baseStationSingleFuzzy();"/>
			</em>
               <ul>
               	   <li><a onclick="baseStationSinglePinyinQuery('a')">A</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('b')">B</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('c')">C</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('d')">D</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('e')">E</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('f')">F</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('g')">G</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('h')">H</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('i')">I</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('j')">J</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('k')">K</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('l')">L</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('m')">M</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('n')">N</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('o')">O</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('p')">P</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('q')">Q</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('r')">R</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('s')">S</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('t')">T</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('u')">U</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('v')">V</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('w')">W</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('x')">X</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('y')">Y</a></li>
                   <li><a onclick="baseStationSinglePinyinQuery('z')">Z</a></li>
                   <li style="width:96px;"><a onclick="baseStationSinglePinyinQuery('all')">显示全部</a></li>
                   
               </ul>
           </div>
           <span class="selectStation-main-title">搜索站址列表：</span>
	<div class="selectStation-main" id="stl">
		
	</div>
	<div class="selectStation-bottom">
		<input type="button" id="btnSubmit"  value="确定" onclick="clickBaseStation();"/>&nbsp;&nbsp;&nbsp;
		<input type="button" id="btnCancels" value="取消" onclick="cancelstl();" />
	</div>
	</div>
	<%-- 弹出站址 结束 --%>
    <div id="black" class="black"></div>
</body>
</html>
