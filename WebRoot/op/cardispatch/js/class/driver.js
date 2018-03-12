

function Driver ( datas ) {
	this.driverId = null;
	this.accountId = null;
	this.driverName = null;
	this.identificationId = null;
	this.driverPhone = null;
	this.driverLicenseClass = null;
	this.driverLicenseNumber = null;
	this.driverStatus = null;
	this.driverLicenseType = null;
	this.drivingOfAge = null;
	this.driverPic = null;
	this.driverBizId = null;
	this.driverBizName = null;
	this.driverAddress = null;
	this.driverAge = null;
	this.wage = null;
	this.accountSuffix = null;
	this.account = null;
	
	var span_list = {};
	
	this.init = function(){
		var account = datas["accountId"];
		if ( account && account != null ) {
			datas["account"] = account.substring( 0 , account.lastIndexOf("@") );
			account = account.substring( account.lastIndexOf("@") );
			datas["accountSuffix"] = account;
		}
		var instance = this;
		for( var key in instance ) {
			if ( !instance.hasOwnProperty(key) ) {
				continue;
			}
			if ( datas[key] && datas[key] != "null" ) {
				this[key] = datas[key];
				span_list[key] = "[column='driver#" + key + "']";
			} else {
				this[key] = " ";
			}
		}
	}
	
	{	
		this.init();
	} 
	
	
	
	//数据对号入座
	this.putInfo = function ( div ) {
		var ins = this;
		for ( var key in span_list ) {
			var widget = $(div).find(span_list[key]);
			if ( !widget || $(widget).length <= 0 ) {
				continue;
			}
			$(widget).each(function( index ){
				if ( $(this).is("span") || $(this).is("textarea") ) {
					$(this).text(ins[key]);
				} else if ( $(this).is("input") ){
					$(this).val(ins[key]);
				} else if ( $(this).is("img") ){
					$(this).attr({"src":ins[key]});
				} else if ( $(this).is("select") ) {
					$(this).find("option[value='" + ins[key] + "']").attr({"selected":"selected"});
				}
			});
			
		}
	}
	
}