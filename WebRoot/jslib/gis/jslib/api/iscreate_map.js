Namespace = {};
Namespace.register = function(ns){  //注册命名空间的函数
    if(typeof(ns)!="string")return;
     ns=ns.split(".");
    var o,ni;
    for(var i=0,len=ns.length;i<len,ni=ns[i];i++){
       try{o=(o?(o[ni]=o[ni]||{}):(eval(ni+"="+ni+"||{}")))}catch(e){o=eval(ni+"={}")}
     }
}
Namespace.register("iscreate.maps.base");
Namespace.register("iscreate.maps.comm");
Namespace.register("iscreate.maps.google");
Namespace.register("iscreate.maps.google.event");
Namespace.register("iscreate.maps.google.tools");
Namespace.register("iscreate.maps.baidu");
Namespace.register("iscreate.maps.baidu.event");
Namespace.register("iscreate.maps.baidu.tools");

// 加载第三方地图(google/mapabc)API
var mapSetting = getMapSetting();
var rUrl = mapSetting.url+mapSetting.key;
getJavascript(rUrl);
iscreate.maps.comm.correctionList_ = null; //校正列表



//根据apiType加载对应的基础模块API
var rootPath_ = getRootPath();
if (mapSetting.apiType) {
	getCorrectionList(mapSetting.apiType);  //ajax请求获取correctionList
	if (mapSetting.apiType == "GoogleMap") {
		getJavascript(rootPath_ + "/jslib/gis/jslib/impl/comm/map_comm.js");
		getJavascript(rootPath_ + "/jslib/gis/jslib/impl/google/lib_for_google.js");
	} else if (mapSetting.apiType == "MapABC") {
		getJavascript(rootPath_ + "/jslib/gis/jslib/impl/comm/map_comm.js");		
		getJavascript(rootPath_ + "/jslib/gis/jslib/impl/mapabc/lib_for_mapabc.js");
	} else if(mapSetting.apiType == "BaiduMap"){
		getJavascript(rootPath_ + "/jslib/gis/jslib/impl/comm/map_comm.js");		
		getJavascript(rootPath_ + "/jslib/gis/jslib/impl/baidu/lib_for_baidu.js");
	}else{
		alert("未能正确识别地图类型！");
	}
}


/**
 * 获取网站的根路径
 * 
 * @return {}
 */
function getRootPath() {
	var strFullPath = window.document.location.href;
	var strPath = window.document.location.pathname;
	var pos = strFullPath.indexOf(strPath);
	var prePath = strFullPath.substring(0, pos);
	var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
	return (prePath + postPath);
} 

/**
 * 加载指定的js文件
 * @param {} src
 */
function getJavascript(src) {
	document.write('<' + 'script src="'+src+'"'+' type="text/javascript"><'+'/script>');
}
/**
 * 以json形式返回地图配置项(同步获取)
 * @return {}
 */
function getMapSetting(){
	var url = "getMapSetting";
	var mapSetting ={};
	var xmlhttp;
	if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp = new XMLHttpRequest();
	} else {// code for IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			var text = xmlhttp.responseText;
			var obj = eval("(" + text + ")");
			mapSetting=obj;
		}
	}
	xmlhttp.open("POST", url, false);
	xmlhttp.send();
	return mapSetting;
}

/**
 * 获取地图校正列表(同步获取)
 * @return {}
 */
function getCorrectionList(apiType){
    var url = 'getCorrectionList?apiType=' + apiType ;
	var xmlhttp;
	if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp = new XMLHttpRequest();
	} else {// code for IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			var text = xmlhttp.responseText;
			var li = eval("(" + text + ")");
			iscreate.maps.comm.correctionList_ = li;
		}
	}
	xmlhttp.open("POST", url, false);
	xmlhttp.send();
}

function selectMapApiType(apiType,callback){
	var url = "selectMap?apiType="+apiType;
	var xmlhttp;
	if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp = new XMLHttpRequest();
	} else {// code for IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			var text = xmlhttp.responseText;
			if(callback){    //回调
				callback(text);
			}
		}
	}
	xmlhttp.open("POST", url, true);
	xmlhttp.send();
}
function selectMap(targetMapType){
	if(!targetMapType){return;}
	var currentMapType = mapSetting.apiType;
	if(currentMapType != targetMapType){
		//切换地图
		mapSetting.apiType = targetMapType;
		$.ajax({ 
		    type : "post", 
		    url : "selectMap", 
		    data : {"apiType":targetMapType}, 
		    async : false, 
		    cache:false, 
		    success : function($data){
		    	if($data!='success'){
			    	alert("切换地图失败！");
			    	return false;
			    }
			    var locationUrl = window.location.href;
				var index = locationUrl.indexOf("?");
				if(index == -1){
					locationUrl+="?lobe=u";		
				}
				locationUrl+="&curFrame=gis";
				locationUrl = locationUrl.replace("#","");
				window.location.href = locationUrl;
				//alert(window.location);
		    }
		});
	}
}
