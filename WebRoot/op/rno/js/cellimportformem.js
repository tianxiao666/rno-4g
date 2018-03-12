var failCnt = 0;// 连续获取进度失败的次数
var maxFailCnt = 5;// 最大允许失败的次数，超过将放弃获取
var interval=2000;//周期性获取进度情况的
var gisCellDisplayLib;
var map;
var minZoom = 15;// 只有大于 该 缩放级别，才真正
var lnglats=new Array();
var zonearea="";
$(document).ready(function() {
	//===========================================================
	loadevent();
	//初始化地图
	initMap();
	$("#areaId").change(function() {
            	gisCellDisplayLib.panTo(getLngLats()[0],getLngLats()[1]);
      	       	});
	
	//===========================================================
		//仅导入
	$("#importBtn").click(function() {
		$("#formImportCell").submit();
		return true;
	});
	//导入的校验
	$("#formImportCell").validate({
		submitHandler : function(form) {
	//$("#importBtn").click(function() {
				//$("#table1").append("<tr><td align='right'>结果：</td><td align='left'><div id='importResultDiv'></div></td></tr>");
				$("#formImportCell").ajaxSubmit({
							type : 'post',
							url : "uploadFileAjaxAction",
							dataType : 'text',
							success : function(data) {
								//console.log("success");
								$("#importBtn").attr("disabled", "disabled");// 先禁用
								var obj ;
								try{
								   obj=eval("(" + data + ")");
								}catch(err){
									$('#formImportCell .canclear').clearFields();	
									$("#importResultDiv").html("上传文件失败！");
									window.location.href=window.location.href;
									return;
								}
								//alert(obj['token']);
								if (obj['token'] && obj['token']!='null') {
									$("#importResultDiv").html("正在解析文件...");
									queryProgress(obj['token']);
								}else{
									//token分配失败
									var msg=obj['msg']?("原因："+obj['msg']):"";
									//console.log("fail msg:"+obj['msg']+"- ---"+msg);
									$("#importResultDiv").html("文件解析失败！"+msg);
									$("#importBtn").removeAttr("disabled");
								}
							},
							error : function(XmlHttpRequest, textStatus,
									errorThrown) {
//								alert("error" + textStatus + "------"
//										+ errorThrown);
								   var msg=obj['msg']?("原因："+obj['msg']):"";
								   //console.log("fail msg:"+obj['msg']+"- ---"+msg);
								   $("#importResultDiv").html("文件解析失败！"+msg);
									$("#importBtn").removeAttr("disabled");
							},complete:function(){
							      $('#formImportCell .canclear').clearFields();	
							}
						});
				//return true;
			//});
				}
			});
		$("#trigger").click(function(){
	 	 //console.log("glomapid:"+glomapid);
	 	 if(glomapid=='null' || glomapid=='baidu'){
//	 	  console.log("切换前是百度");
		  $(this).val("切换百度");
//		  console.log("切换后是谷歌");
		  sessId="google";
	 	 }else{
//	 	 	console.log("切换前是谷歌");
		 	$(this).val("切换谷歌");
//		 	console.log("切换后是百度");
		 	sessId="baidu";
	 	 }
		 storageMapId(sessId);
		 });
			
});
/**
 * 获取区域数据
 */
function getZoneArea(){
	//var b= { "天河": [{ "lng": "1111", "lat":"2222"}],"海珠": [{ "lng": "3333", "lat":"4444"}]};
	//console.log(b.天河[0].lng);
	zonearea="["+$("#outjson").val()+"]";
	//console.log(zonearea);
}
/**
 * 初始化级联信息
 */
function loadevent(){
	getZoneArea();
	getSubAreas("#provinceId","#cityId","#areaId");
	}
/**
 * 获取lnglats数组
 * @return {}
 */
function getLngLats(){
				var areaname=$("#areaId").find("option:selected").text();
            	//alert(zonearea);
            	var mes_obj=eval("("+zonearea+")");
            	//alert(mes_obj);
					//Js循环读取JSON数据，并增加下拉列表选项
					for(var i=0;i<mes_obj.length;i++){ 
			 　　			　for(var key in mes_obj[i]){
			        if(areaname.trim()==key){
						if(mes_obj[i][key][0].lng==0 || mes_obj[i][key][0].lat==0){
							console.log("没有初始化坐标");
							// 存储广州点坐标 
							lnglats[0]=116.404;
							lnglats[1]=39.915;
							break;
							}else{
								lnglats[0]=mes_obj[i][key][0].lng;
								lnglats[1]=mes_obj[i][key][0].lat;
								break;
									}
								}
			         						} 
                   		 }
                   		 return lnglats;
}
/**
 * 通过session存储地图ID
 */
function storageMapId(mapid){
	$(".loading_cover").css("display", "block");
	$.ajax({
		url : 'storageMapIdBySessForAjaxAction',
		data : {
			'mapId' : mapid
		},
		dataType : 'json',
		type : 'post',
		success : function(data) {
			glomapid=data;
			//console.log("data:"+data);
//			var c = data;
			try{
//			initRedirect("initGisCellDisplayAction");
			window.location.href=window.location.href;
			}catch(err){
			
			}
		},
		error : function(xhr, textstatus, e) {

		},
		complete : function() {
			$(".loading_cover").css("display", "none");
		}
	});
}
/**
 * 初始化地图
 */
function initMap() {

	gisCellDisplayLib = new GisCellDisplayLib(map, minZoom,
					null, null, null,null);
//			var lng = $("#hiddenLng").val();
//			var lat = $("#hiddenLat").val();
			var lng = getLngLats()[0];
			var lat = getLngLats()[1];
			map = gisCellDisplayLib.initMap("map_canvas", lng, lat);
}
function queryProgress(token) {
	$.ajax({
				url : 'queryUploadStatusAjaxAction',
				data : {
					'token' : token
				},
				type : 'post',
				dataType : 'text',
				success : function(data) {
					var obj;
					try{
					  obj=eval("(" + data + ")");
					}catch(err){
						$("#importResultDiv").html("服务器返回失败！");
						return;
					}
					//alert("data=="+data);
					if (obj['flag']) {
						$("#importResultDiv").html("文件正在解析，当前进度:"
								+ obj['progress'] + "%");
						var pronum = new Number(obj['progress']);
						//alert("progress:"+pronum);
						if (pronum >= 100) {
							//alert("get final result");
							// 文件解析完成
							getImportResult(token);// 获取结果
						} else {
							//alert("get progress again");
							//setTimeout("queryProgress(" + token + ")", 5000);
							window.setTimeout(function(){ queryProgress(token);},interval); 
						}
					} else {
						$("#importResultDiv").html("文件解析失败！原因:" + obj['msg']);
						getImportResult(token);// 获取结果
					}
				},
				error : function(err, status) {
					alert("获取进度失败！");
					// 获取失败
					failCnt++;
					if (failCnt > maxFailCnt) {
						$("#importResultDiv").html("无法获取文件解析进度！");
						$("#importBtn").removeAttr("disabled");// 启用
					} else {
						window.setTimeout(function(){ queryProgress(token);},interval); 
					}
				}
			});
}

// 获取详细结果
function getImportResult(token) {
	$.ajax({
				url : 'getUploadResultAjaxAction',
				data : {
					'token' : token
				},
				type : 'post',
				dataType : 'text',
				success : function(data) {
					
					//console.log(data);
					mes_obj=eval("(" + data + ")");
					if(mes_obj['msg']){
					$("#importResultDiv").html("文件解析结果："+mes_obj['msg']);
					}
					result();
					// 上传按钮可用
				},
				error : function(err, status) {
					$("#importResultDiv").html("获取结果失败！");
				},
				complete : function() {
					$("#importBtn").removeAttr("disabled");// 启用
				}
			});
}
//增加标注信息[闭包函数]
var result=function addMymarkers(){

			for(var i=0;i<mes_obj.length;i++){ 
				(function(){
					//console.log("addMymarkers来人了");
					gisCellDisplayLib.createMarker(mes_obj[i].btslng,mes_obj[i].btslat,mes_obj[i].description);
					if(i==0){
						gisCellDisplayLib.panTo(mes_obj[i].btslng,mes_obj[i].btslat);
					}
					/*if(map instanceof BMap.Map){
							var marker1 = new BMap.Marker(new BMap.Point(mes_obj[i].btslng,mes_obj[i].btslat));  // 创建标注
							map.addOverlay(marker1);              // 将标注添加到地图中
							try{
								if(i==0){
									map.panTo(marker1.getPosition());
								}
							}catch(err){
								console.log("baidu:"+err);
							}
							var infoWindow1=new BMap.InfoWindow(mes_obj[i].description);
							//console.log(mes_obj[i].description);
							marker1.addEventListener("click", function(){this.openInfoWindow(infoWindow1);});
						}else{
							try{
								if(i==0){
									gisCellDisplayLib.panTo(mes_obj[i].btslng,mes_obj[i].btslat);
								}
							}catch(err){
								console.log("google:"+err);
							}
							// 创建地标。
							var ge=gisCellDisplayLib.ge;
							console.log(ge);
							var placemark = ge.createPlacemark('');
							//placemark.setName(name);
							placemark.setDescription(mes_obj[i].description);
							var point = ge.createPoint('');
							point.setLatitude(Number(mes_obj[i].btslat));
							point.setLongitude(Number(mes_obj[i].btslng));
							placemark.setGeometry(point);
							//向 Google 地球添加地标。
							ge.getFeatures().appendChild(placemark);
							
						}*/
					
					//创建信息窗口
					//var infoWindow1 = new BMap.InfoWindow("基站名："+mes_obj[i].btsname+"</br>站点编号："+mes_obj[i].siteno+"</br>小区ID："+mes_obj[i].cellid+"</br>归属BSC："+mes_obj[i].bsc+"</br>经度："+mes_obj[i].btslng+"　纬度："+mes_obj[i].btslat+"</br>天线方向："+mes_obj[i].antbearing+"</br>颜色类型："+mes_obj[i].colortype+"</br>描述："+mes_obj[i].description);
					/*
					 <table width="100%" border="0" bgcolor="#009999" style="font-size:12px">
						<tr>  <td colspan="2" align="center" style="font-weight:bold">天河南</td></tr>
						<tr>  <td width="58" align="right">基站名：</td>  <td width="135" align="left">bts1</td></tr>
						<tr>  <td align="right">站点编号：</td>  <td align="left">1</td></tr>
						<tr>  <td align="right">小区ID：</td>  <td align="left">1</td></tr>
						<tr>  <td align="right">归属BSC：</td>  <td align="left">cz01b</td></tr>
						<tr>  <td align="right">经度：</td>  <td align="left">113.361397</td></tr>
						<tr>  <td align="right">纬度：</td>  <td align="left">23.12489</td></tr>
						<tr>  <td align="right">天线方向：</td>  <td align="left">180</td></tr>
						<tr>  <td align="right">颜色类型：</td>  <td align="left">red</td></tr>
					  </table>
					 */
					
				})(i);
			}	
}