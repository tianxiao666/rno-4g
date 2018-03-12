
//企业信息类
function Project( datas ) {
	this.id = null;
	this.projectNumber = null;
	this.cityName = null;
	this.cityId = null;
	this.name = null;
	this.responsibilityDescription = null;
	this.startDate = null;
	this.planEndDate = null;
	this.clientEnterpriseId = null;
	this.clientEnterpriseName = null;
	this.serverEnterpriseId = null;
	this.serverEnterpriseName = null;
	this.clientOrgFullName = null;
	this.serverOrgFullName = null;
	this.clientOrgId = null;
	this.serverOrgId = null;
	this.client = null;
	this.server = null;
	this.agreement = null;
	this.agreementMoney = null;
	this.progress = null;
	this.signingDate = null;
	this.effectDate = null;
	this.projectManager = null;
	this.salesManager = null;
	this.planFirstReviewDate = null;
	this.realFirstReviewDate = null;
	this.planLastReviewDate = null;
	this.realLastReviewDate = null;
	this.payType = null;
	
	var span_list = {};
	
	this.init = function(){
		var instance = this;
		for( var key in instance ) {
			if ( !instance.hasOwnProperty(key) ) {
				continue;
			}
			span_list[key] = "[column='project#" + key + "']";
			if ( datas[key] && datas[key] != "null" ) {
				this[key] = datas[key];
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