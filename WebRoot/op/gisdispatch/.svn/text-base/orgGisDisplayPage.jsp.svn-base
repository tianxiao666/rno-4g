<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>展示页面</title>
<link rel="stylesheet" href="css/gis_Concentration.css" type="text/css" />
<link type="text/css" rel="stylesheet" href="css/alert_div.css" />
<%-- 加载地图模块需要引入的文件 --%>
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="../../jslib/gis/jslib/api/iscreate_map.js"></script>
<script type="text/javascript">
var map = null;
$(function(){
	//初始化地图
	var lat = 23.12651273063735;
	var lng = 113.41254847709276;
	var centerLatLng = new ILatLng(lat,lng);
	map = new IMap("gis_map",centerLatLng,15,{});
	//获取组织架构信息
	var queryUrl = "getOrgInfoAction";
	var orgId = $("#orgIdText").val();
	if(!orgId){
		alert("组织架构Id不能为空！");
		return false;
	}
	var param = {orgId:orgId};
	$.ajax({
		type : "post", 
	    url : queryUrl, 
	    data : param, 
	    async : false, 
	    dataType : "json",
	    success : function($data){ 
	    	var queryResult = $data;
			//var queryResult = eval("("+res+")");
			//生成信息弹框
			var orgName = queryResult.orgName;
			var address = queryResult.address;
			var contactPhone = queryResult.contactPhone;
			var dutyPerson = queryResult.dutyPerson;
			var orgId = queryResult.orgId;
			
			if(!orgName||orgName=='null'){orgName="";};
			if(!address||address=='null'){address="";};
			if(!contactPhone||contactPhone=='null'){contactPhone="";};
			if(!dutyPerson||dutyPerson=='null'){dutyPerson="0";};
			
			var orgStaffCount= queryResult.orgStaffCount;
			if(!orgStaffCount||orgStaffCount=='null'){orgStaffCount="0";};
			
			var lat = queryResult.latitude;
			var lng = queryResult.longitude;
			lat = stringToFloat(""+lat);
			lng = stringToFloat(""+lng);
			var position = new ILatLng(lat,lng);
			//移动地图中心
			if(map){
				map.panTo(position);
			}
			position = IMapComm.gpsToMapLatLng(position);
			var marker = new IMarker(position,orgName,{});
			//更改图标
			var options = {};
			options.icon = "image/shiyebu.png";
			marker.setOptions(options);
			
			var statusLabel = "<lable style='text-shadow:1px 1px 10px #000000;color: #0000FF;font-size: 12px;'>"+orgName+"</lable>";
			marker.textOverlay_ = new ITextOverlay(position,statusLabel,{});
			marker.textOverlay_.setMap(map);
			marker.setMap(map);
			//保存数据到Marker对象
			marker.orgName_ = orgName;
			marker.orgId_ = orgId;
			marker.address_ = address;
			marker.contactPhone_ = contactPhone;
			marker.dutyPerson_ = dutyPerson;
			marker.orgStaffCount_ = orgStaffCount;
			//鼠标单击事件
			IMapEvent.addListener(marker, "click", function(){
				showMarkerDetail(marker);
			}); 
	    } 
	});
});	
/*显示Marker对象信息*/
var currentInfoWindow = null;
function showMarkerDetail(marker){
	if(!marker){return false;}
	if(currentInfoWindow!=null && currentInfoWindow.bizValue_){
		currentInfoWindow.open(map);
		return false;
	}
	//拿出Marker保存的数据
	var orgName = marker.orgName_;
	var ogrId = marker.orgId_;
	var address = marker.address_;
	var contactPhone = marker.contactPhone_;
	var dutyPerson = marker.dutyPerson_;
	var orgStaffCount = marker.orgStaffCount_;
	var position = marker.getPosition();
	
	if(!orgName||orgName=='null'){orgName="";};
	if(!address||address=='null'){address="";};
	if(!contactPhone||contactPhone=='null'){contactPhone="";};
	if(!dutyPerson||dutyPerson=='null'){dutyPerson="0";};
	if(!orgStaffCount||orgStaffCount=='null'){orgStaffCount="0";};
	
	var content = "<div class=\"architecture\"><ul><li><label>组织名称：</label><span><a href=\"../humanresource/getOneOrgAction?orgId="+ogrId+"\" target='_blank'>"+orgName+"</a>&nbsp;</span></li><li><label>办公地点：</label><span>"+address+"&nbsp;</span></li><li><label>联系电话：</label><span>"+contactPhone+"&nbsp;</span></li><li><label>现有人数：</label><span>"+orgStaffCount+"人</span></div>";
	currentInfoWindow =  new IInfoWindow(content,{"position":position});
	currentInfoWindow.open(map);
	currentInfoWindow.bizValue_ = orgName;
}
/**
 * 将字符串转换成浮点数
 */
function stringToFloat(str){
	var num = null;
	try{
		var re = parseFloat(str);
		num = re;
	}catch(err){
		
	}
	return num;
}
</script>

</head>

<body>
   <input type="hidden" id="orgIdText" value="${param.orgId}"/>
   <%-- 地图 --%>
   <div id="gis_map" style="position: absolute;width:100%"></div>
</body>
</html>
