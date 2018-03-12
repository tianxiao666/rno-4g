$(document).ready(function() {
	showOrganization();
	showSubOrganization();
}); 

/**
 * 获取页面的数据通过ajax提交
 */
function showOrganization(){
	var id = request("id");
	var myurl ="getOrganizationByIdAction?id="+id;
	$.ajax({
           url : myurl,
           type : "POST",
           dataType : 'json',
           success : function (result){
               var json = result ;
               fillData(json);
           }
       });
}

/**
 * 将通过ajax请求获取到的内容填充到页面
 * @param {Object} data
 */
function fillData(data){
    var organization = data.organization;
    
   var organizationUrl = "getAllOrganizationsAction";
	//动态加载组织选择框
		$.ajax( {
			url : organizationUrl,
			type : "POST",
			dataType : 'json',
			success : function(result) {
				$.each(result, function(key, value) {
					if(data.orgParentName==value.organization.orgName){
													$("#orgParentCode").append(
								"<option value=" + value.organization.substanceId + ">"
										+ value.organization.orgName + "</option>");
						}else{
							$("#orgParentCode").append(
								"<option value=" + value.organization.substanceId + ">"
										+ value.organization.orgName + "</option>");
						}
							
				});
			}
		});
    
    
    var orgSetupDay=organization.orgSetupDay.toString().split(" ",1);
	$("#orgName").html(organization.orgName);
	$("#orgChargeMan").html(organization.orgChargeMan);
	$("#orgCode").html(organization.orgCode);
	$("#orgLevel").html(organization.orgLevel);
	$("#orgSetupDay").html(orgSetupDay.toString());
	$("#orgPeopleCount").html(organization.orgPeopleCount);
	$("#orgStatus").html(organization.orgStatus);
	$("#orgProvince").html(organization.orgProvince);
	$("#orgCity").html(organization.orgCity);
	$("#orgPostCode").html(organization.orgPostCode);
	$("#orgHomePage").html(organization.orgHomePage);
	$("#orgParentName").html(data.orgParentName);
	$("#orgPhone").html(organization.orgPhone);
	$("#orgFax").html(organization.orgFax);
	$("#orgAddress").html(organization.orgAddress);
	$("#orgResponsibility").html(organization.orgResponsibility);
	
}




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
												+ "<td style='text-align: left' class='tdr'>"
												+ "<a href='organizationDetail.html?id="
												+ object.substanceId
												+ "'>详细</a>"
												+ "</td>";
										tr = tr + "<tr>";
									
										$("#tableQuery").append(tr);
									});
           }
       });
}




