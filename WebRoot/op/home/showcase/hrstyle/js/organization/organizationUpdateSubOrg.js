$(document).ready(function() {
	showSubOrganization();
}); 

function showSubOrganization(){
	var id = request("id");
	var myurl ="getOrganizationAndSubOrgByIdAction?id="+id;
	$.ajax({
           url : myurl,
           type : "POST",
           dataType : 'json',
           success : function (result){
                             //删除除表头外的行
				var eventTrs = $("#tableQuery tr");
				var length = eventTrs.length;
				for (var i = length - 1; i > 0; i--) {
					eventTrs.eq(i).remove();
				}
					$.each(
									result,
									function(key, object) {
									var  tr = "<tr>";
										tr = tr
												+ "<td style='text-align: left'>"
												+ object.orgCode + "</td>";
										tr = tr
												+ "<td style='text-align: left'>"
												+ object.orgName + "</td>";
										tr = tr
												+ "<td style='text-align: left'>"
												+ object.orgChargeMan + "</td>";
										tr = tr
												+ "<td style='text-align: left'>"
												+ object.orgStatus + "</td>";
										tr = tr
												+ "<td width='200'  style='text-align: left' class='tdr'> "
												+ "<a href='updateOrganization.html?id="
												+ object.substanceId
												+ "'>修改</a>"
												+ "</td>";
										tr = tr + "<tr>";
									
										$("#tableQuery").append(tr);
									});
           }
       });
}




