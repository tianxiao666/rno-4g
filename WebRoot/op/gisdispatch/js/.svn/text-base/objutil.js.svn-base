
function getObjectData ( obj , key , def ) {
	if ( !def ) {
		def = "";
	}
	var result = null;
	if ( key instanceof Array ) {
		var ob = obj[key[0]];
		result = ob==undefined?def:ob;
		for( var i = 1 ; i < key.length ; i++ ) {
			var k = key[i];
			if ( !ob ) {
				return "";
			}
			ob = ob[k];
			
			result = ob==undefined||ob==null||ob=="null"?def:ob;
		}
	} else {
		result = obj[key]==undefined||obj[key]==null||obj[key]=="null"?def:obj[key];
	}
	return result;
}


//对象转字符
function obj2String ( obj ) {
		if ( !obj || obj == null ) {
			return;
		}
		if ( obj.constructor == String ) {
			return "'" + obj + "'";
		}
		if ( obj.constructor == Number ) {
			return  obj ;
		}
		var objString = "";
		if ( obj.constructor == Array ) {
			for ( var i = 0 ; i < obj.length ; i++ ) {
				var value = obj[i];
				var s = obj2String(value);
				objString += s + " , ";
			}
			objString = objString.substring(0,objString.lastIndexOf(","));
			objString = "[" + objString + "]" ;
		} else if ( obj.constructor == Object ) {
			for ( var key in obj ) {
				var value = obj[key];
				if ( value.constructor == String ) {
					objString += typeStringChange( key , value );
				} else if ( value.constructor == Number ) {
					objString += typeNumberChange( key , value );
				}  else if ( value.constructor == Array ) {
					objString += typeArrayChange( key , value );
				} else if ( value.constructor == Object ) {
					objString += typeObjChange( key , value );
				} else {
					continue;
				}
				objString += " , ";
			}
			objString = objString.substring(0,objString.lastIndexOf(","));
			objString = "{" + objString + "}" ;
		}
		return objString
	}

//类型字符串转换
function typeStringChange( key , value ) {
	var str = "'" + key + "' : '" + value + "'";
	return str;
}

//类型数字转换
function typeNumberChange( key , value ) {
	var str = "'" + key + "' : " + value + "";
	return str;
}

//类型数组转换
function typeArrayChange( key , value ) {
	var str = "";
	var arr = "";
	for ( var i = 0 ; i < value.length ; i++ ) {
		var val = value[i];
		arr += obj2String(val);
		arr += ",";
	}
	arr = arr.substring(0,arr.lastIndexOf(","));
	arr = "[" + arr + "]";
	str = "'" + key + "' : " + arr + "";
	return str;
}


//类型对象转换
function typeObjChange( key , value ) {
	var str = obj2String(value);
	str = "'" + key + "' : '" + str + "' ";
	return str;
}