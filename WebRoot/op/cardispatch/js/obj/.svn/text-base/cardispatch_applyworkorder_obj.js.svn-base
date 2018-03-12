var apply_area_select = null;
var arealist = null;
var selects = [
					"#area_divpro" , 
					"#area_divcity" , 
					"#area_divdistrict" , 
					"#area_divstreet"
			  ]; 

$(document).ready(function(){
	if ( baseStationId && baseStationType ) {
		$.ajax({
			"url" : "cardispatchCommon_ajax!getAreaIdListByBaseStationId.action" , 
			"type" : "post" , 
			"async" : true , 
			"data" : { "baseStationId" : baseStationId , "baseStationType" : baseStationType } , 
			"success" : function( data ){
				arealist = eval("(" + data + ")");
				arealist.reverse();
				apply_area_select = new areaselect({
					"proUrl" : "cardispatchCommon_ajax!getAllProvince.action" , 
					"subUrl" : "cardispatchCommon_ajax!getSubArea.action" , 
					"maxLv" : 4 , 
					"minLv" : 0 , 
					"div" : $("#area_div") , 
					"defaultList" : arealist 
				});
			}
		});
		$.ajax({
			"url" : "cardispatchCommon_ajax!getResourceInfoByType.action" , 
			"type" : "post" , 
			"async" : true , 
			"data" : { "resId" : baseStationId , "resType" : baseStationType } , 
			"success" : function( data ){
				data = eval("(" + data + ")");
				if ( data["address"] ) {
					$("#areaDescription").val(data["address"]);
				}
			}
		});
	} else if ( stationId ) {
		//查询站址地址
		$.ajax({
			"url" : "cardispatchCommon_ajax!getAreaIdListByStationId.action" , 
			"type" : "post" , 
			"async" : true , 
			"data" : { "stationId" : stationId } , 
			"success" : function( data ){
				arealist = eval("(" + data + ")");
				arealist.reverse();
				apply_area_select = new areaselect({
					"proUrl" : "cardispatchCommon_ajax!getAllProvince.action" , 
					"subUrl" : "cardispatchCommon_ajax!getSubArea.action" , 
					"maxLv" : 4 , 
					"minLv" : 0 , 
					"div" : $("#area_div") , 
					"defaultList" : arealist 
				});
			}
		});
		$.ajax({
			"url" : "cardispatchCommon_ajax!getResourceInfoByType.action" , 
			"type" : "post" , 
			"async" : true , 
			"data" : { "resId" : stationId , "resType" : "Station" } , 
			"success" : function( data ){
				data = eval("(" + data + ")");
				if ( data["address"] ) {
					$("#areaDescription").val(data["address"]);
				}
			}
		});
	} else {
		$.ajax({
			"url" : "cardispatchCommon_ajax!getAreaIdByLoginPerson.action" , 
			"type" : "post" , 
			"async" : true , 
			"success" : function( data ){
				arealist = eval("(" + data + ")");
				arealist.reverse();
				apply_area_select = new areaselect({
					"proUrl" : "cardispatchCommon_ajax!getAllProvince.action" , 
					"subUrl" : "cardispatchCommon_ajax!getSubArea.action" , 
					"maxLv" : 4 , 
					"minLv" : 0 , 
					"div" : $("#area_div") , 
					"defaultList" : arealist 
				});
			}
		});
	}
		
	
	
	customAutoComplete({
		divId : "staffAutoComplete_div" , 
		inputSelector : "#useCarPersonName" , 
		url : "cardispatchCommon_ajax!notDriverStaffAutoComplete.action" , 
		showMethodName : "autoCompleteShowInfo" , 
		clickMethodName : "autoCompleteOnClick" , 
		eventName : "keyup" , 
		params : { "name" : "#useCarPersonName" } , 
		width : 143 
	});
	
	//验证
	formcheck({
		"form" : $("#applyCardispatch_form") , 
		"subButton" : $("#applyCardispatchWorkorderBtn") , 
		"isAjax" : true , 
		"showLoading" : true , 
		"formSubmiting" : function () {
			var pro = $("#area_divpro").find(":selected").text();
			var city = $("#area_divcity").find(":selected").text();
			var area = $("#area_divdistrict").find(":selected").text();
			var street = $("#area_divstreet").find(":selected").text();
			var areaDescription = $("#areaDescription").val();
			var address = pro + city + area + street + areaDescription;
			$("#planUseCarAddress").val(address);
			//验证备注长度
			var description_txt = $("#description").val();
			if ( description_txt.length > 60 ) {
				alert("备注长度不能超过60个字符");
				return false;
			}
			return true;
		} , 
		"ajaxSuccess" : function( data ){
			if ( data && data != null && data != "" ) {
				alert("申请成功");
				if ( workType ) {
					window.close();
				} else {
					location.href = "cardispatchWorkorder!enterCardispatchWorkorderAction?WOID="+data;
				}
			} else {
				alert("申请失败");
			}
		}
	});
	
})



	function autoCompleteShowInfo ( data ) {
		return data.name;
	}
	
	
	function autoCompleteOnClick ( data ) {
		$("#useCarPersonName").val(data.name);
		$("#useCarPersonAccountId").val(data.account);
		
	}
	
	
	