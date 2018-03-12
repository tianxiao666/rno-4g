<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title> 查看车辆申请单信息</title>
<link rel="stylesheet" type="text/css" href="../../css/base.css" />
<link rel="stylesheet" type="text/css" href="../../css/public.css" />
<link rel="stylesheet" type="text/css" href="../../css/public-table.css" />
<link rel="stylesheet" type="text/css" href="css/car_gb.css" />
<link rel="stylesheet" href="../../jslib/paging/iscreate-paging.css" type="text/css"></link>
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js" ></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js"></script>
<script type="text/javascript" src="../../js/public.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.form.js"></script>
<script type="text/javascript" src="js/tool/tablePage.js"></script>
<script>
	var woId = null;
	$(document).ready(function(){
		woId = "${param.WOID}";
	})
</script>
<script type="text/javascript" src="../jslib/pageSection.js"></script>
<script type="text/javascript" src="js/class/cardispatchworkorder.js"></script>
<script type="text/javascript" src="js/obj/cardispatch_lookupworkorder_obj.js"></script>
<script type="text/javascript" src="js/cardispatch_lookupworkorder.js"></script>

</head>

<body>
	<%--头部开始--%>
	<div id="header960">
    	<div class="header-top"><h2>车辆调度申请单</h2></div>
        <div class="header-main">
        	<span class="fl">申请单号：<span column="cardispatchworkorder#woId"></span></span>
            <span class="fr">申请单状态：<span column="cardispatchworkorder#workorderStateCName"></span></span>
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
                    <span class="list_pic_tit"><span id="useSpan" column="cardispatchworkorder#useCarTime"></span></span>
                </li>
                <li>
                    <em class="list_pic_li" id="return">还车</em>
                    <span class="list_pic_tit"><span id="returnSpan" column="cardispatchworkorder#returnCarTime"></span></span>
                </li>
            </ul>
            <div class="list_pic_info">
                <input class="blue_button" id="carGisBtn" style="line-height:12px; float:left;" type="button" value="" />
            	<h4>
            		里程（仪表）： <span column="cardispatchworkorder#totalMileage"></span>公里
            		<br />里程（GPS） ： <span column="cardispatchworkorder#totalgpsMileage"></span>公里
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
                            <li><a href="#" onclick="return false;">行车费用</a></li>
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
                        <table class="car_tb1">
                        	<tr>
                        		<td>派车车辆牌照：
                        			<em class="red">
										<a href="javascript:void(0);" id="carInfo_a" column="cardispatchworkorder#carNumber"></a>
									</em>
                        		</td>
                        		<td>&nbsp;</td>
                        	</tr>
                        	<tr>
                                <td>用车里程读数：
                                	<em class="red">
										<span column="cardispatchworkorder#realUseCarMileage"></span>KM
									</em>
                                </td>
                                <td>实际用车时间：<span column="cardispatchworkorder#realUseCarTime"></span></td>
                            </tr>
                            <tr>
                                <td>还车里程读数：
                                    <em class="red">
										<span column="cardispatchworkorder#realReturnCarMileage"></span>KM
									</em>
                                </td>
                                <td>实际还车时间：<span column="cardispatchworkorder#realReturnCarTime"></span></td>
                            </tr>
                        </table>
                    </div>
                    
                    <div class="container-main-table1 container-main-table1-tab" id="serverTrackRecord_div" style="display:none;">
                    	<%-- 服务跟踪记录 --%>
                    </div>
                    
                    <div class="container-main-table1 container-main-table1-tab" style="display:none;">
                    	<table class="main-table1 tc" id="traveling_cost_table">
                    		<thead>
	                            <tr>
	                                <th>类型</th>
	                                <th>金额（元）</th>
	                                <th>备注</th>
	                            </tr>
                    		</thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
                <%--container-main结束--%>
           	</fieldset>
           
    	</div>
        <%--tab1结束--%>
    </div>
    <%--主体container结束--%>
</body>
</html>

