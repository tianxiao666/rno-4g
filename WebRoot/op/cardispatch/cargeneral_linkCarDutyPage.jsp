<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新人员排班</title>

<%-- 公共 - 页面 - css --%>
<link rel="stylesheet" type="text/css" href="../../css/base.css" />
<link rel="stylesheet" type="text/css" href="../../css/public.css" />
<link rel="stylesheet" type="text/css" href="../../css/public-table.css" />

<%-- 公共 - 插件 - css --%>
<link rel="stylesheet" href="../../jslib/jquery/css/jquery.treeview.css" type="text/css"></link>
<link rel="stylesheet" href="css/ui/jquery.ui.all.css" type="text/css"></link>

<%-- 公共 - 页面 - js --%>
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js" ></script>
<script type="text/javascript" src="../../js/public.js"></script>

<%-- 公共 - 插件 - js --%>
<script type="text/javascript" src="../../jslib/date/date.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.form.js"></script>
<script type="text/javascript" src="js/ui/jquery.bgiframe-2.1.2.js"></script>
<script type="text/javascript" src="js/ui/jquery.ui.core.js"></script>
<script type="text/javascript" src="js/ui/jquery.ui.widget.js"></script>
<script type="text/javascript" src="js/ui/jquery.ui.mouse.js"></script>
<script type="text/javascript" src="js/ui/jquery.ui.draggable.js"></script>
<script type="text/javascript" src="js/ui/jquery.ui.position.js"></script>
<script type="text/javascript" src="js/ui/jquery.ui.dialog.js"></script>
<script type="text/javascript" src="../../jslib/common.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js"></script>
<script type="text/javascript" src="../jslib/generateTree.js"></script>

<%-- 页面 css --%>
<link rel="stylesheet" type="text/css" href="css/cardispatchStaffShiftsManage.css" />



<%-- 自定义插件 - css --%>
<link rel="stylesheet" type="text/css" href="css/calendar_css.css" />

<%-- 自定义工具 - js --%>
<script src="js/util/input.js"></script>
<script src="js/util/urlutil.js"></script>
<script type="text/javascript" src="js/util/dateutil.js"></script>
<script type="text/javascript" src="js/util/objutil.js"></script>

<%-- 自定义插件 - js --%>
<script src="js/tool/calendar.js"></script>
<script src="js/tool/table2Table.js"></script>
<script src="js/tool/tablePage.js"></script>
<script src="js/util/objutil.js"></script>
<script src="js/tool/custom_autocomplete.js"></script>

<%-- 页面 js --%>
<script type="text/javascript" src="js/linkcarduty.js"></script>


<style>
	.dutyPersonInfo{background:#eee; height:550px; width:0px; border:1px solid #ccc; text-align:center; overflow:auto; position:absolute;left:50%;top:250px;margin-left:0px;margin-top:0px;}
	.dutyPersonInfo ul li{ line-height:18px; padding:3px; white-space:nowrap; border-bottom:1px solid #ccc;}
	#left_btn,#right_btn{cursor:pointer;}
	[enableEdit]{display:none; visibility:hidden;}
</style>
<script>
	$(document).ready(function(){
		$(".duty_kind2").hide();
		$(".container-tab2 ul li:eq(0)").css("border-left","1px solid #ccc");
		$(".container-tab2 ul li").each(function(index){
			$(this).click(function(){
				$(".container-tab2 ul li").removeClass("tab2-li-show");
				$(this).addClass("tab2-li-show");
				$(".container-main-table1-tab").hide();
				$(".container-main-table1-tab").eq(index).show();
				calendar.refreshCalendar();
			})
		})
	})
</script>
</head>

<body>


	
    <%-- tab主体部分 --%>
    <div class="container-main-table1-right">
        <div class="container-tab2 clearfix">
            <ul>
                <li class="tab2-li-show"><a href="#" onclick="return false;">日历显示</a></li>
                <li><a href="#" onclick="return false;">列表显示</a></li>
            </ul>
        </div>
        
       
        <div id="cal_text_div" style="width:100%; line-height:32px; text-align:center;">
        	<img src="images/month_left.png" id="left_btn" align="middle"/>
	        <em id="date_span" style="line-height:25px;display:inline-block;"></em>
	        <img src="images/month_right.png" id="right_btn" align="middle"/>
        </div> 
        
        <div class="container-main-table1-tab" id="duty_div" >
        	<%-- 日历div --%>
        </div>
        <div class="container-main-table1-tab" style="display:none;" >
        	<table class="month_work_information tc" id="month_work_info_table">
    		<thead>
         	<tr>
             	<th class="row2" rowspan="2">车牌号</th>
                 <th class="row2" rowspan="2">班次</th>
                 <th class="row1" colspan="31">日期（ <em class="weeked_color2"></em> 绿色区域为周末 ）</th>
             </tr>
             <tr id="numberTr">
             	<c:forEach begin="1" end="31" varStatus="n" >
             		<th title="td${n.count}">${n.count}</th>
             	</c:forEach>
             </tr>
            </thead>
            <tbody>
            
            </tbody>
        </table>
        </div>
    </div>
    
    
    <div id="dutyPersonInfo" title="值班车辆详细信息" >
	<div align="left" id='personInfo'></div>
</body>
</html>
