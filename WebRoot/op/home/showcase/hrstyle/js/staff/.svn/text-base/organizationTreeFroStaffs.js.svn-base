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
					d.add(object.codeId, object.parentId, object.codeName, "queryStaff.html?orgId="+object.codeString+"&orgStatus=yes");
				});
				$("#tree").append(d + "");
			}
		});

	});

