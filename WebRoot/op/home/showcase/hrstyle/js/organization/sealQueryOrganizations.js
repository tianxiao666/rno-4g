$(document).ready(function() {
	$("#sealQueryButton").click(function() {
		showStation(1);
	});
});

/**
 * 获取页面的数据通过ajax提交
 */
function showStation(currentPage) {

	var values = {
		"currentPage" : currentPage,
		"orgName" : $("#orgName").val(),
		"orgCode" : $("#orgCode").val(),
		"orgChargeMan" : $("#orgChargeMan").val(),
		"orgLevel" : $("#orgLevel").val(),
		"orgSetupDay" : $("#orgSetupDay").val(),
		"orgProvince" : $("#orgProvince").val(),
		"orgCity" : $("#orgCity").val(),
		"orgStatus" : $("#orgStatus").val()
	}

	$
			.ajax( {
				url : "queryOrganizationAction",
				type : "POST",
				data : values,
				dataType : 'json',
				success : function(result) {
				
				var page = result.page;
				var organizations = result.organizations;
				
					//删除除表头外的行
				var eventTrs = $("#tableQuery tr");
				var length = eventTrs.length;
				for ( var i = length - 1; i > 0; i--) {
					eventTrs.eq(i).remove();
				}
              var tr = "";
				$
						.each(
								organizations,
								function(key, object) {
								    tr = tr +"<tr id='" + object.substanceId
											+ "'>";
									tr = tr + "<td style='text-align: left'>"
											+ object.orgCode + "</td>";
									tr = tr + "<td style='text-align: left'>"
											+ object.orgName + "</td>";
									tr = tr + "<td style='text-align: left'>"
											+ object.orgChargeMan + "</td>";
									tr = tr
											+ "<td style='text-align: left'><p>"
											+ object.orgStatus
											+ "</p></td>";

									if (object.orgStatus == "在用") {

										tr = tr
												+ "<td style='text-align: left' class='tdr'>"
												+ "<a href=javascript:sealOrganization('"
												+ object.substanceId
												+ "');>封存</a>&nbsp; <a href='organizationDetail.html?id="
												+ object.substanceId
												+ "'>详细</a>" + "</td>";
									} else {
                                        tr = tr
												+ "<td style='text-align: left' class='tdr'>"
												+ "<a href=javascript:noSealOrganization('"
												+ object.substanceId
												+ "');>解封存</a>&nbsp; <a href='organizationDetail.html?id="
												+ object.substanceId
												+ "'>详细</a>" + "</td>";
									}

									tr = tr + "</tr>";

								});
				
				//调用organizationPage.js的foots方法生成分页脚本
					tr = tr+foots("queryOrganizationAction", page.currentPage, page.totalPage,
						page.prePage, page.nextPage, page.pageSize);
					$(tr).appendTo("#tableQuery");
			}
			});
}

//封存组织,移除封存的单元行
function sealOrganization(substanceId) {
	var r = confirm("确认封存？");
	var values = {
		"id" : substanceId
	}
	if (r == true) {
		$.ajax( {
			url : "sealOrganizationAction",
			type : "POST",
			data : values,
			dataType : 'text',
			success : function(result) {
				if (result = 'success') {
                     updateTree();
					showStation(1);
				} else {
					alert("封存过程中出错");
				}
			}
		});

	}
}

//解封存组织,移除封存的单元行
function noSealOrganization(substanceId) {
	var r = confirm("确认解封存？");
	var values = {
		"id" : substanceId
	}
	if (r == true) {
		$.ajax( {
			url : "noSealOrganizationAction",
			type : "POST",
			data : values,
			dataType : 'text',
			success : function(result) {
				if (result = 'success') {
	                updateTree();
					showStation(1);
				} else {
					alert("解封存过程中出错");
				}
			}
		});

	}
}
