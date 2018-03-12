$(document).ready(function() {
	d = new dTree('d');
	//动态加载组织选择框
		d.add(0, -1, '组织架构');
		$.ajax( {
			url : "getOrganizationTreeAction",
			type : "POST",
			dataType : 'text',
			success : function(result) {
				var resultCollection = jQuery.parseJSON(result);
				$.each(resultCollection, function(key, object) {
					d.add(object.codeId, object.parentId, object.codeName, "organizationDetail.html?id="+object.codeString);
				});
				$("#tree").append(d + "");
			}
		});

	});

function updateTree(){
		d = new dTree('d');
	//动态加载组织选择框
		d.add(0, -1, '组织架构');
		$.ajax( {
			url : "getOrganizationTreeAction",
			type : "POST",
			dataType : 'text',
			success : function(result) {
				var resultCollection = jQuery.parseJSON(result);
				$.each(resultCollection, function(key, object) {
					d.add(object.codeId, object.parentId, object.codeName, "organizationDetailForView.html?id="+object.codeString);
				});
				//移除以前定义的dtree树形结构
					$(".dtree").remove();
			   //重新加载新的树形结构
					$("#tree").append(d + "");
			}
		});
}