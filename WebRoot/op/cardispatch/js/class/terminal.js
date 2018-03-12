function Terminal ( datas ) {
	
	this.terminalId=null;
	this.terminalName=null;
	this.clientversion=null;
	this.comment=null;
	this.terminalState=null;
	this.clientimei=null;
	this.telphoneNo=null;
	this.launchedTime=null;
	this.expiredTime=null;
	this.terminalPic=null;
	this.mobileType=null;
	this.stateLastTime=null;
	this.terminalBizId=null;
	this.monthlyRent = null;
	this.terminalBizName=null;
	
	var span_list = {};
	
	this.init = function(){
		var instance = this;
		for( var key in instance ) {
			if ( !instance.hasOwnProperty(key) ) {
				continue;
			}
			if ( datas[key] && datas[key] != "null" ) {
				this[key] = datas[key];
				span_list[key] = "[column='terminal#" + key + "']";
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
		$("[column='terminal#terminalPic']").attr({"src":""});
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