
var driverTablePage = null;


$(document).ready(function(){
	//司机分页
	var driver_opt = {
		"pageSize" : pageSize,
		"dataArray" : new Array(),
		"table" : $("#driver_div_table"),
		"columnMethod" : function(i, key, tdData, tr) {
			if (tdData == null || !tdData["driverName"]) {
				return;
			}
			var driverNameTd = $("<td/>").text(
					tdData["driverName"] == undefined ? ""
							: tdData["driverName"]);
			var driverBizNameTd = $("<td/>").text(
					tdData["driverBizName"] == undefined ? ""
							: tdData["driverBizName"]);
			var addressTd = $("<td/>").text(
					tdData["address"] == undefined ? "" : tdData["address"]);
			var driverLicenseTypeTd = $("<td/>").text(
					tdData["driverLicenseType"] == undefined ? ""
							: tdData["driverLicenseType"]);
			var operaTd = $("<td/>");
			var resource = "";
			if(tdData != null){	
				resource = tdData;
			}
			var operaA = $("<a/>").attr( {
				"title" : "选择司机",
				"href" : "javascript:void(0);"
			});
			$(operaA).text("选择");
			$(operaTd).append($(operaA));
			$(tr).append($(driverNameTd));
			$(tr).append($(driverBizNameTd));
			$(tr).append($(addressTd));
			$(tr).append($(driverLicenseTypeTd));
			$(tr).append($(operaTd));
			$(operaA).click( { "d" : tdData }, function($e) {
				var data = $e.data.d;
				$(".close_alert_div").click();
				var driver = new Driver(data);
				driver.putInfo("body");
			});

		}
	}
	driverTablePage = new TablePage(driver_opt);
	
})