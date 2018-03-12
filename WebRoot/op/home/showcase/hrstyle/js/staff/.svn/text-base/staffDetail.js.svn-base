$(document).ready(function() {
	showStaff();
});
/**
 * 
 */
function showStaff() {
	var substanceId = request("id");
	var myurl = "getStaffBySubstanceIdAction?substanceId=" + substanceId;
	$.ajax( {
		url : myurl,
		cache : false,
		data : {},
		async : true,
		type : "POST",
		dataType : "json",
		success : function(result) {
			var json = result;
		fillData(json);
	}
	});
}
/**
 * 
 * @param {Object} data
 */
function fillData(data) {

	var staff = data.staff;
    var staffEnrollDay=staff.staffEnrollDay.toString().split(" ",1);
	$("#staffName").html(staff.name);
	$("#staffIdentity").html(staff.staffIdentity);
	$("#staffCode").html(staff.staffCode);
	$("#staffGender").html(staff.staffGender);
	$("#staffPhone").html(staff.staffPhone);
    $("#staffEnrollDay").html(staffEnrollDay.toString());
	$("#staffStatus").html(staff.staffStatus);
	$("#staffPoliticalStatus").html(staff.staffPoliticalStatus);
	$("#organizationName").html(data.organizationName);
	$("#staffBirthPalce").html(staff.staffBirthPalce);
	$("#staffAddress").html(staff.staffAddress);
	$("#staffEmail").html(staff.staffEmail);
	
}
