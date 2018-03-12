<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>新车辆排班</title>

<%-- 公共 - 页面 - css --%>
<link rel="stylesheet" type="text/css" href="../../css/base.css" />
<link rel="stylesheet" type="text/css" href="../../css/public.css" />
<link rel="stylesheet" type="text/css" href="../../css/public-table.css" />

<%-- 公共 - 插件 - css --%>
<link rel="stylesheet" href="../../jslib/jquery/css/jquery.treeview.css" type="text/css"/>
<link rel="stylesheet" href="css/ui/jquery.ui.all.css" type="text/css" />
<link rel="stylesheet" href="../../jslib/paging/iscreate-paging.css" type="text/css"/>
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
<script src="js/tool/custom_autocomplete.js"></script>

<%-- 页面 js --%>
<script type="text/javascript" src="js/obj/calendar_obj.js"></script>
<script type="text/javascript" src="js/carduty_calendar.js"></script>

<style>
	.dutyPersonInfo{background:#eee; height:550px; width:0px; border:1px solid #ccc; text-align:center; overflow:auto; position:absolute;left:50%;top:250px;margin-left:0px;margin-top:0px;}
	.dutyPersonInfo ul li{line-height:18px; padding:3px; white-space:nowrap; border-bottom:1px solid #ccc;}
	#left_btn,#right_btn{cursor:pointer;}
	/*车辆排版管理 头部*/
	.search_top_div{position:relative; background:url(cardispatch/images/gradback.png) repeat-x; height:32px; line-height:32px;padding-left:5px;z-index:100; zoom:1; border-bottom:1px solid #ccc;}
	
	#tree2 span{ cursor:pointer;}
	#tree2 span:hover{ background:#eee;}
	
	#tree1 span{ cursor:pointer;}
	#tree1 span:hover{ background:#eee;}
	[enableEdit='false']{display:none; visibility:hidden;}
</style>
<script>
	var enableEdit = "";
	var enable = "";
	$(document).ready(function(){
		enable = "${param.enable}";
		if( enable && enable == "true") {
			enableEdit = "enableEdit='true'";
		} else {
			enableEdit = "enableEdit='false'";
		}
		
		$(".duty_kind2").hide();
		$(".container-tab2 ul li:eq(0)").css("border-left","1px solid #ccc");
		$(".container-tab2 ul li").each(function(index){
			$(this).click(function(){
				$(".container-tab2 ul li").removeClass("tab2-li-show");
				$(this).addClass("tab2-li-show");
				$(".container-main-table1-tab").hide();
				$(".container-main-table1-tab").eq(index).show();
				calendar.refreshCalendar();
				var tab_text = $(this).text();
				if ( tab_text == "日历显示" ) {
					$(".duty_kind2").hide();
					$(".duty_kind").css({"display":"inline"});
				} else if ( tab_text == "列表显示" ) {
					$(".duty_kind").hide();
					$(".duty_kind2").css({"display":"inline"});
				}
			})
		})
	})
</script>
</head>

<body>

	
<%-- 头部搜索部分 begin --%>
	<div class="search_top_div">
        <input type="hidden" id="carHiddenId"/>
        <input type="hidden" id="searchBizunitIdText" />
		<input type="text" id="searchBizunitNameText" readonly="readonly" onfocus="$('#treeDiv').toggle('fast');" value="${ logic_user_biz.name }"/>
		<a href="javascript:void(0);" style="margin-top:5px; margin-left:-29px; *+margin-top:2px;" class="select_button selectWorkPlaceButton" id="chooseAreaButton" title="选择组织"></a>
        <input type="text" enableShow='true'  id="carNumber"/>
        <a href="javascript:void(0)" id="queryButton" enableShow='true'></a>
        <div id="treeDiv">
        	<%-- 放置组织架构树 --%>
        </div>
        <div class="duty_kind">
        	<em class="day"></em>白班
            <em class="night"></em>晚班
        </div>
        <div class="duty_kind2">
        	<em class="day"></em>白班
            <em class="night"></em>晚班
        </div>
        <div class="duty_add" enableEdit='true' >
        	<%-- 批量添加排班 --%>
        </div>
        <div class="advanced-search2">
        	<form action="planDuty.action" id="planDutyForm" method="post">
	        	<ul>
	        		<li>
	                	<span class="advanced-search2-grey">月份：</span>
	                    <span>
							<select onchange="monthChange(this);">
								<option value="0">请选择</option>
								<c:forEach begin="1" end="12" varStatus="n" >
									<option value="${n.count}">${n.count}月</option>
								</c:forEach>
							</select>
						</span>
	                </li>
	            	<li>
	                	<span class="advanced-search2-grey">日期：</span>
	                    <span>从 <input type="text" id="b_time"  name="startTime" /><a href="javascript:void(0);" class="date_button" id="beginTime_btn" onclick="fPopCalendar(event,document.getElementById('b_time'),document.getElementById('b_time'),false)"></a> 
	                    	到 <input type="text" id="e_time"  name="endTime"/><a href="javascript:void(0);" class="date_button" onclick="fPopCalendar(event,document.getElementById('e_time'),document.getElementById('e_time'),false)"></a></span>
	                </li>
	                <li>
	                	<span class="advanced-search2-grey">班次：</span>
	                    <span>
		                    <input id="morning_chk" type="checkbox" value="<s:property value='@com.iscreate.cardispatch.constant.DutyTypeConstant@MORNING'/>" name="depart" /> 
		                    <label for="morning_chk">白班</label>
		                    <input id="night_chk" type="checkbox" value="<s:property value='@com.iscreate.cardispatch.constant.DutyTypeConstant@NIGHT' />"  name="depart" />
		                    <label for="night_chk">晚班</label>
	                    </span>
	                    <div class="advanced-search2-hide"><input type="button" value="添加" id="plan_duty" /></div>
	                </li>
	            </ul>
            </form>
        </div>
    </div>
	<%-- 头部搜索部分 end --%>


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
	</div>
</body>
</html>
