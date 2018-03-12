$(document).ready(function() {
	showOrganization();
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
	$("#orgResponsibility").val(organization.orgResponsibility);
	
}
