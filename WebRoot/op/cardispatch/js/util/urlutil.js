	
	/**
	 * url帮助类
	 * @author andy 2012.5.23
	 * @version 1.0
	 * Copy Right Information : iscreate
	 * Project : operationservice
	 * Modification history :2012.5.23
 	 */
	
	/**
	 * 转换url内的动态参数
	 * @param url 地址
	 */
	function convertUrl (url) {
		var index = url.indexOf("?");
		var head = url.substring(0,index);
		var foot = url.substring(index+1);
		var obj = getUrlParamStringToObj(foot);
		var realUrl = getUrlParamObjToString(obj);
		var result = head + "?" +realUrl;
		//
		return result;
	}
	
	function subUrlParamString (url) {
		return url.substring(url.indexOf("?")+1);
	}
	
	/**
	 * 把参数字符串转换成对象
	 * @param url 参数字符串
	 */
	function getUrlParamStringToObj ( url ) {
		var obj = {};
		var paramArr = url.split("&");
		for ( var i = 0 ; i < paramArr.length ; i++ ) {
			var param = paramArr[i].split("=");
			var key = param[0];
			var value = param[1];
			
			try {
				obj[key] = eval(value);
			} catch( ex ) {
				obj[key] = value;
			}
			if( param.length <2 ) {
				value = "";
			}
		}
		return obj;
	}
	
	/**
	 * 转换url内的动态参数
	 * @param obj 参数对象
	 */
	function getUrlParamObjToString ( obj ) {
		var arr = new Array();
		for ( var key in obj ) {
			var value = obj[key];
			var str = key + "=" + value;
			arr.push(str);
		}
		var url = arr.join("&");
		return url;
	}
	
	/**
	 * 组合成url
	 */
	function createUrl ( url , opt ) {
		if ( opt == null) {
			return url;
		}
		url += "?";
		for ( var key in opt ) {
			url += key+"="+opt[key];
			url += "&";
		}
		url = url.substring(0,url.length-1);
		return url;
	}
	