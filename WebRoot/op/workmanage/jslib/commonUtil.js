
//获取相应的数据字典数据
function getDictionaryDataByTreeId(treeId){
	var requesUrl="getDictionaryData.action";
	var params={"treeId":treeId};
	var resData;
	$.ajax({
		async : false,
		url : requesUrl,
		data : params,
		dataType : 'text',
		type : 'post',
		success : function(result) {
			var data = eval("(" + result + ")");
			resData=data;
		}
	});
	
	return resData;
}


//组装目标的数据展示
function constructSelectData(targetId,data){
	if(targetId && targetId!=""){
		if(data && data!=""){
			var tempElement="";
			$.each(data, function(index, obj){
				tempElement ="<option value='"+obj.treeNodeName+"'>"+obj.treeNodeName+"</option>";
				$("#"+targetId).append(tempElement);
			});
		}
	}
}



//获取表单元素里面的input元素，以json的格式返回
function getInputsByForm(formId){
	var jsonData={};
	var inputs=$("#"+formId+" :input[type!=button]");
	
	for(var i=0;i<inputs.length;i++){
	 	var temp_input=$(inputs[i]);
		var temp_input_name=$(temp_input).attr("name");
		var temp_input_placeholder=$(temp_input).attr("placeholder");
	 	if(temp_input_name && temp_input_name!="" && temp_input_name!="baseStationType"){
		 	var temp_input_value=$(temp_input).val();
			//console.log("temp_input_name："+temp_input_name+"，temp_input_value："+temp_input_value);
			//alert("temp_input_name："+temp_input_name+"，temp_input_value："+temp_input_value);
			//alert("temp_input_name："+temp_input_name+"，temp_input_placeholder："+temp_input_placeholder);
			if(temp_input_placeholder!=temp_input_value){
				jsonData[temp_input_name]=temp_input_value;
			}
	 	}
	}
	
	return jsonData;
}


function jsonToStr(o){
	var arr = []; 
	var fmt = function(s) { 
		if (typeof s == 'object' && s != null){
			return JsonToStr(s); 
		}
		return /^(string|number)$/.test(typeof s) ? "'" + s + "'" : s; 
	}
	for (var i in o){
		 arr.push("'" + i + "':" + fmt(o[i])); 
	}
	return '{' + arr.join(',') + '}'; 
}