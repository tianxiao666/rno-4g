<%@ page language="java" import="java.util.*" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>

<%@ taglib uri="/struts-tags" prefix="s"%>	
<style type="text/css">
td{word-wrap：break-word}
</style>
<input type="hidden" id="totalPage" value="<s:property value='totalPage'/>" />
        	<table class="search_result_table" id="resultListTable" style="table-layout:inherit;width:100%">  
            	 <tr class='thead'>
                  <th style="width:55px">车辆牌照</th>
                  <th style="width:55px">任务总数</th>
                  <th style="width:152px">车辆归属地</th>
                  <th style="width:55px">车辆类型</th>
                  <th style="width:55px">司机姓名</th>
                  <th style="width:73px">司机电话</th>
                  <th style="width:55px">定位状态</th>
                  <th style="width:55px">是否排班</th>
                  <th style="width:360px">最新位置(时间)</th>
                  <th style="width:71px">当天里程统计</th>
                  <th style="width:55px">操作</th>
                </tr>
                <s:if test="resultList != null && resultList.size() > 0">
				<s:iterator id="map" value="resultList" status="st">
                <tr class='pageTr'>
                    <td><input type='hidden' value='<s:property value="#map.carId"/>'/><a href='cargeneral_index.jsp?carId=<s:property value="#map.carId"/>&type=view' target='_blank' ><s:property value="#map.carNumber"/></a></td>
                   	<td><a href="#" onclick="getCarTaskInfoForGantt('<s:property value="#map.carId"/>',this)" ><s:property value="#map.totalTask"/></a></td>
                    <td><s:property value="#map.carBizName"/></td>
                    <td><s:property value="#map.carType"/></td>
                    <td><s:property value="#map.driverName"/></td>
                    <td><s:property value="#map.driverPhone"/></td>
                    <td>
                    <s:if test="#map.terminalState==0">
                    	离线
                    </s:if>
                    <s:elseif test="#map.terminalState==1">
                    	行驶中
                    </s:elseif>
                    <s:elseif test="#map.terminalState==2">
                    	静止
                    </s:elseif>
                     <s:elseif test="#map.terminalState==3">
                    	待初始化
                    </s:elseif>
                    <s:else>
                    	待初始化
                    </s:else>
                    </td>
                   
                    <s:if test="#map.isArranged=='是'">
                    	 <td style='background-color: green;'>是</td>
                    </s:if>
                    <s:else>
                    	<td >否</td>
                    </s:else>
                    
                    <td class="addressTd" id="<s:property value="#map.carId"/>" val="<s:date name="#map.mod_time" format="(MM月dd日 HH:mm:ss)"/>" name="<s:property value="#map.longitude"/>#<s:property value="#map.latitude"/>" ><img src='images/newLoading.gif'></td>
                    
                    <td class="mileageTd" id="mi<s:property value="#map.carId"/>" val="<s:property value="#map.carId"/>" name="<s:property value=""/>#<s:property value=""/>" ><img src='images/newLoading.gif'></td>
                    
                    <td><a class='map_ico position' href='javascript:void(0)' title='地图定位' onclick="linkToCar('<s:property value="#map.carNumber"/>')"></a><a class='map_ico operate' target="_blank" href="" onclick="javascript:forwardToMap('<s:property value="#map.carNumber"/>',this)" title='轨迹回放' ></a></td>
                </tr>
                </s:iterator>
				</s:if>
             </table> 
<script type="text/javascript">
	//预填当前日期
	var currentTime=new Date();
	currentTime.setDate(currentTime.getDate() - 1);
	var year=currentTime.getFullYear();
	var month=currentTime.getMonth()+1;
	var date=currentTime.getDate();
	if(month<10){month = "0"+month;}
	if(date<10){date = "0"+date;}
	currentTime = year+"-"+month+"-"+date;
	function forwardToMap(carNumber,me){
		$(me).attr("href","loadCarStateMonitoringPageAction?carNumber="+encodeURI(encodeURI(carNumber))+"&beginTime="+currentTime+" 00:00:00&endTime="+currentTime+" 23:59:59&display=none");
		//window.location.href  = "loadCarStateMonitoringPageAction?carNumber="+carNumber+"&beginTime="+currentTime+" 00:00:00&endTime="+currentTime+" 23:59:59&display=none";
		return true;
	}
	$(".addressTd").each(function(){
			var array=$(this).attr("name").split("#");
			var carId=$(this).attr("id");
		    var longitude = array[0];
			var latitude = array[1];		
			var params = {carId:carId,longitude:longitude,latitude:latitude};
			
		    $.post("cardispatchWorkorder_ajax!getAddressByLngLatAction.action",params,function(data){
		    	if(data.address==""){
		    		$("#"+data.carId).html("&nbsp;");
		    	}else{
		    		$("#"+data.carId).html(data.address+$("#"+data.carId).attr("val"));
		    	}
		    	
		    },"json");
	})
	
	var sTime = new Date();
	
	year=sTime.getFullYear();
	month=sTime.getMonth()+1;
	date=sTime.getDate();
	if(month<10){month = "0"+month;}
	if(date<10){date = "0"+date;}
	
	sTime = year+"-"+month+"-"+date+" 00:00:00";
	eTime = year+"-"+month+"-"+date+" 23:59:59";
	
	$(".mileageTd").each(function(){
		
		var carId=$(this).attr("val");
		var params = {carId:carId,sTime:sTime,eTime:eTime};
		
	    $.post("cardispatchWorkorder_ajax!findCurrentDayMileage.action",params,function(data)
	    {
	    	if(data)
	    	{	
	    		$.each(data,function(k,v){
	    			$("#mi"+k).html(v);
	        	});
	    	}
	    	else
	    	{
	    		$("#mi"+carId).html("&nbsp;");
	    	}
	    	
	    },"json");
	})

</script>
           
