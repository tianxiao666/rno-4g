/**
区域联动js
*/

/**
 * 根据父控件的值，获取指定类型的子数据，并绑定到childDomId上
 * 
 * @param parentDomId
 * @param childDomId
 * @param childType
 */
function getSubAreas(parentDomId, childDomId, childType,callback) {
	var parentId = $("#" + parentDomId).val();
	$("#"+childDomId).empty();
	$.ajax({
		url : 'getSubAreaByParentAreaForAjaxAction',
		data : {
			'parentAreaId' : parentId,
			'subAreaLevel' : childType
		},
		dataType : 'json',
		type : 'post',
		async:false,
		success : function(data) {
			if (!data) {
				console.error("返回数据有错误");
				return;
			}
			try {
				var one;
				var htmlStr = "";
				for ( var i = 0; i < data.length; i++) {
					one = data[i];
					//console.log("第"+i+"个数据："+one.toSource());
					if (one['area_id']) {
						htmlStr += "<option value='" + one['area_id'] + "'>"
								+ $.trim(one['name']) + "</option>";
					}
				}
				//console.log("html="+htmlStr);
				$("#"+childDomId).append(htmlStr);
			} catch (err) {
				console.log(err);
			}
			if(typeof callback =="function"){
				callback(data);
			}
		},
		complete:function(){
			$("#"+childDomId).trigger("change");
		}

	});
}