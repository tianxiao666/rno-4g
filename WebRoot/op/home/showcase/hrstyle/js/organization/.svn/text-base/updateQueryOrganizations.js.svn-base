$(document).ready(function() {
$("#updateQueryButton").click(function() {
		showStation(1);
	});
});

/**
 * 获取页面的数据通过ajax提交
 */
function showStation(currentPage) {
	
	
		var values
= {
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
				var organizations=result.organizations;
				
                 //删除除表头外的行
				var eventTrs = $("#tableQuery tr");
				var length = eventTrs.length;
				for (var i = length - 1; i > 0; i--) {
					eventTrs.eq(i).remove();
				}
				var tr = "";
					$
							.each(
									organizations,
									function(key, object) {
									    tr =tr+ "<tr>";
										tr = tr
												+ "<td  style='text-align: left'>"
												+ object.orgCode + "</td>";
										tr = tr
												+ "<td style='text-align: left'>"
												+ object.orgName + "</td>";
										tr = tr
												+ "<td style='text-align: left'>"
												+ object.orgChargeMan + "</td>";			
										tr = tr
												+ "<td  style='text-align: left'>"
												+ object.orgStatus
												+ "</td>";
										tr = tr
												+ "<td style='text-align: left' class='tdr'>"
												+ "<a href='updateOrganization.html?id="
												+ object.substanceId
												+ "'>修改</a>"
												+ "</td>";
										tr = tr + "</tr>";
									});
					
					//调用organizationPage.js的foots方法生成分页脚本
					tr = tr+foots("queryOrganizationAction", page.currentPage, page.totalPage,
						page.prePage, page.nextPage, page.pageSize);
					$(tr).appendTo("#tableQuery");
				}
			});
}
