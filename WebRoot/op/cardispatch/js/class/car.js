function Car ( datas ) {
	this.carId=null;
	this.carModel=null;
	this.carBizId=null;
	this.carNumber=null;
	this.carPic=null;
	this.status=null;
	this.carBrand=null;
	this.carType=null;
	this.loadWeight=null;
	this.passengerNumber=null;
	this.leasePay=null;
	this.custodyFee=null;
	this.carAge=null;
	this.carMarker=null;
	this.seatCount=null;
	this.carBizName=null;
	
	
	var span_list = {};
	
	this.init = function(){
		var instance = this;
		for( var key in instance ) {
			if ( !instance.hasOwnProperty(key) ) {
				continue;
			}
			if ( datas[key] && datas[key] != "null" ) {
				this[key] = datas[key];
				span_list[key] = "[column='car#" + key + "']";
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
				} else {
					$(this).text(ins[key]);
				}
			});
			
		}
	}
}