

function cardispatchworkorder ( datas ) {
	this.id = null ;
	this.woId = null ;
	this.useCarType = null ;
	this.applyDescription = null ;
	this.useCarPersonId = null ;
	this.criticalClass = null ;
	this.workType = null ;
	this.planUseCarTime = null ;
	this.planUseCarAddress = null ;
	this.realUseCarMeetAddress = null ;
	this.realUseCarLatitude = null ;
	this.realUseCarLongitude = null ;
	this.realUseCarTime = null ;
	this.realUseCarMileage = null ;
	this.useCarAddressDescription = null ;
	this.carDriverPairId = null ;
	this.planReturnCarTime = null ;
	this.realReturnCarTime = null ;
	this.realReturnCarAddress = null ;
	this.realReturnCarLatitude = null ;
	this.realReturnCarLongitude = null ;
	this.realReturnCarMileage = null ;
	this.dispatchTime = null ;
	this.dispatchDescription = null ;
	this.creator = null;
	this.createTime = null;
	this.totalgpsMileage = null;
	this.createOrderTime = null;
	this.sendCarTime = null;
	this.useCarTime = null;
	this.returnCarTime = null;
	this.closeOrderTime = null;
	this.carNumber = null;
	this.creatorName = null;
	this.useCarPersonCName = null;
	this.workorderStateCName = null;
	this.totalMileage = null;
	this.workorderApplyType = null;
	
	var span_list = {/*
						"id" : "[column='cardispatchworkorder#id']" , 
						"woId" : "[column='cardispatchworkorder#woId']" , 
						"useCarType" : "[column='cardispatchworkorder#useCarType']" , 
						"applyDescription" : "[column='cardispatchworkorder#applyDescription']" , 
						"useCarPersonId" : "[column='cardispatchworkorder#useCarPersonId']" , 
						"criticalClass" : "[column='cardispatchworkorder#criticalClass']" , 
						"workType" : "[column='cardispatchworkorder#workType']" , 
						"planUseCarTime" : "[column='cardispatchworkorder#planUseCarTime']" , 
						"planUseCarAddress" : "[column='cardispatchworkorder#planUseCarAddress']" , 
						"realUseCarMeetAddress" : "[column='cardispatchworkorder#realUseCarMeetAddress']" , 
						"realUseCarLatitude" : "[column='cardispatchworkorder#realUseCarLatitude']" , 
						"realUseCarLongitude" : "[column='cardispatchworkorder#realUseCarLongitude']" , 
						"realUseCarTime" : "[column='cardispatchworkorder#realUseCarTime']" , 
						"realUseCarMileage" : "[column='cardispatchworkorder#realUseCarMileage']" , 
						"useCarAddressDescription" : "[column='cardispatchworkorder#useCarAddressDescription']" , 
						"carDriverPairId" : "[column='cardispatchworkorder#carDriverPairId']" , 
						"planReturnCarTime" : "[column='cardispatchworkorder#planReturnCarTime']" , 
						"realReturnCarTime" : "[column='cardispatchworkorder#realReturnCarTime']" , 
						"realReturnCarAddress" : "[column='cardispatchworkorder#realReturnCarAddress']" , 
						"realReturnCarLatitude" : "[column='cardispatchworkorder#realReturnCarLatitude']" , 
						"realReturnCarLongitude" : "[column='cardispatchworkorder#realReturnCarLongitude']" , 
						"realReturnCarMileage" : "[column='cardispatchworkorder#realReturnCarMileage']" , 
						"dispatchTime" : "[column='cardispatchworkorder#dispatchTime']" , 
						"dispatchDescription" : "[column='cardispatchworkorder#dispatchDescription']" , 
						"creator" : "[column='cardispatchworkorder#creator']" , 
						"createTime" : "[column='cardispatchworkorder#createTime']" 
					*/};
	
	this.init = function(){
		var instance = this;
		for( var key in instance ) {
			if ( !instance.hasOwnProperty(key) ) {
				continue;
			}
			if ( datas[key] && datas[key] != "null" ) {
				this[key] = datas[key];
				span_list[key] = "[column='cardispatchworkorder#" + key + "']";
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
				if ( $(this).is("span") || $(this).is("textarea") || $(this).is("td") ) {
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