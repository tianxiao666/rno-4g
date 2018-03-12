<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查看车辆申请单信息-派车</title>
<link rel="stylesheet" type="text/css" href="../../css/base.css" />
<link rel="stylesheet" type="text/css" href="../../css/public.css" />
<link rel="stylesheet" type="text/css" href="../../css/public-table.css" />
<link rel="stylesheet" type="text/css" href="css/car_gb.css" />
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="../../js/public.js"></script>
<script type="text/javascript" src="../../jslib/date/date.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.form.js"></script>
<script src="js/tool/custom_autocomplete.js"></script>
<script type="text/javascript" src="js/tool/tablePage.js"></script>
<script type="text/javascript" src="js/util/dateutil.js"></script>
<script type="text/javascript" src="js/util/objutil.js"></script>

<script type="text/javascript">

var woId = "";
var isView = "";
var carNumber = "";
var driverCarId = "";

$(document).ready(function(){
	
	woId = "${param.WOID}";
	isView = "${param.type}";
	driverCarId = "${param.driverCarId}";
	
	
	$("#cardriverId").val(driverCarId);
	
	$(".is_pc").click(function(){
		$(".is_pc_td").show();
	});
	$(".not_pc").click(function(){
		$(".is_pc_td").hide();
	});
})
</script>
<script type="text/javascript" src="../jslib/pageSection.js"></script>
<script type="text/javascript" src="js/obj/cardispatch_sendCar_obj.js"></script>
<script type="text/javascript" src="js/class/cardispatchworkorder.js"></script>
<script type="text/javascript" src="js/cardispatch_sendCar.js"></script>
</head>

<body>
	<s:property value="" />
	<%--头部开始--%>
	<div id="header960">
    	<div class="header-top"><h2>车辆调度申请单</h2></div>
        <div class="header-main">
        	<span class="fl">申请单号：<span column="cardispatchworkorder#woId"></span></span>
            <span class="fr">申请单状态：<span id="workorderState">待调度</span></span>
            <span>创建时间：<span column="cardispatchworkorder#createTime"></span></span>
       	</div>
        <%-- 状态 --%>
        <div class="list_pic clearfix">
            <ul>
                <li>
                    <em class="list_pic_li" id="create" >申请</em>
                    <em class="next_ico"></em>
                    <span class="list_pic_tit"><span id="createSpan" column="cardispatchworkorder#createOrderTime"></span></span>
                </li>
                <li>
                    <em class="list_pic_li" id="send">派车</em>
                    <em class="next_ico"></em>
                    <span class="list_pic_tit"><span id="sendSpan" column="cardispatchworkorder#sendCarTime"></span></span>
                </li>
                <li>
                    <em class="list_pic_li" id="use">用车</em>
                    <em class="next_ico"></em>
                    <span class="list_pic_tit"></span>
                </li>
                <li>
                    <em class="list_pic_li" id="return">还车</em>
                    <span class="list_pic_tit"></span>
                </li>
            </ul>
            <div class="list_pic_info">
            	<h4>
            		里程（仪表）： <span column="cardispatchworkorder#totalMileage"></span>公里
            		<br />里程（GPS）  ： <span column="cardispatchworkorder#totalgpsMileage"></span>公里
            	</h4>
            </div>
        </div>
    </div>
    <%--头部结束--%>
    
    <%--主体container开始--%>
    <div id="container960">
    	<%--tab1开始--%>
    	<div class="container-tab1">
        	<fieldset id="fieldset-1">
            	<legend>
                	<input type="checkbox" checked="checked" /><span class="fieldset_span">申请单信息</span>
                </legend>
                <%--container-main开始--%>
                <div class="container-main">
                	<%--tab2开始--%>
                    <div class="container-tab2 clearfix">
                        <ul>
                            <li class="tab2-li-show"><a href="#" onclick="return false;">基本信息</a></li>
                            <li><a href="#" onclick="return false;">服务过程记录</a></li>
                        </ul>
                    </div>
                    <%--tab2结束--%>
                    
                    <%--container-main-table1开始--%>
                    <div class="container-main-table1 container-main-table1-tab">
                    	<table class="main-table1">
                            <tr class="main-table1-tr">
                                <td class="main-table1-title" colspan="4">车辆调度申请单信息</td>
                            </tr>
                            <tr>
                            	<td class="menuTd">车型：</td>
                                <td><span column="cardispatchworkorder#useCarType"></span></td>
                                <td class="menuTd">用车紧急程度：</td>
                                <td><span column="cardispatchworkorder#criticalClass"></span></td>
                            </tr>
                            <tr>
                                <td class="menuTd">用车时间：</td>
                                <td>
                                	<span column="cardispatchworkorder#planUseCarTime"></span>
                                	至
                                	<span column="cardispatchworkorder#planReturnCarTime"></span>
                                </td>
                                <td class="menuTd">用车人：</td>
                                <td><span column="cardispatchworkorder#useCarPersonCName"></span></td>
                            </tr>
                            <tr>
                                <td class="menuTd">申请人：</td>
                                <td><span column="cardispatchworkorder#creatorName"></span></td>
                                <td class="menuTd">申请时间：</td>
                                <td><span column="cardispatchworkorder#createTime"></span></td>
                            </tr>
                            <tr>
                                <td class="menuTd">用车地点：</td>
                                <td colspan="3">
                                	<span column="cardispatchworkorder#planUseCarAddress"></span>
                                </td>
                            </tr>
                            <tr>
                                <td class="menuTd higherLine">申请说明：</td>
                                <td colspan="3">
                                	<span column="cardispatchworkorder#applyDescription"></span>
                                </td>
                            </tr>
                        </table>
                    </div>
                    
                    <div class="container-main-table1 container-main-table1-tab" id="serverTrackRecord_div" style="display:none;">
                    	<table class="main-table1 tc">
                    		<thead>
                    			<tr>
	                                <th>处理时间</th>
	                                <th>处理人</th>
	                                <th>联系电话</th>
	                                <th>处理类型</th>
	                                <th>处理结果</th>
	                            </tr>
                    		</thead>
                    		<tbody>
                    		
                    		</tbody>
                    	</table>
                    </div>
                    <div class="container-main-table1 container-main-table1-tab" style="display:none;">
                    </div>
                </div>
                <%--container-main结束--%>
           	</fieldset>
           
            <fieldset id="fieldset-2">
                <legend>
                    <input type="checkbox" checked="checked" /><span class="fieldset_span">调度单处理：派车</span>
                </legend>
                <div class="container-main">
                	<form action="cardispatchWorkorder_ajax!sendCar.action" id="sendCar_form" method="post"  enctype="multipart/form-data" >
                		<input type="hidden" name="woId" column="cardispatchworkorder#woId" />
                		<input type="hidden" name="planUseCarAddress" column="cardispatchworkorder#planUseCarAddress" />
	                	<table class="main-table1">
	                    	<tr>
	                        	<td class="menuTd">是否派车：</td>
	                        	<td id="isSendCar">
	                            	<input type="radio" name="is_pc" value="true" class="is_pc" checked="checked" />是
	                                <input type="radio" name="is_pc" value="false" class="not_pc" />否
	                            </td>
	                            <td class="menuTd is_pc_td">选派车辆：</td>
	                        	<td class="is_pc_td" id="carNumber_search_td">
	                            	<input type="text" id="carNumber" />
	                            	<input type="hidden" id="carId" />
	                            	<input type="hidden" name="carDriverPairId" id="cardriverId" />
	                                <input type="button" id="carsearch_btn" value="查询" />
	                            </td>
	                        </tr>
	                        <tr>
	                        	<td class="menuTd">备注：</td>
	                        	<td colspan="3" id="dispatchDescription" >
	                            	<textarea name="dispatchDescription" style="width:750px;" id="dispatchDescription"></textarea>
	                            </td>
	                        </tr>
	                    </table>
                   
	                    <div class="container-bottom">
	                        <input type="button" id="sendCarBtn" value="提交" style="width:60px;" />
	                        <input type="reset" value="重置" style="width:60px;" onclick="$('.is_pc').click();" />
	                    </div>
	                </form>    
                </div>
                
            </fieldset>
    	</div>
        <%--tab1结束--%>
    </div>
    <%--主体container结束--%>
</body>
</html>


