

function areaselect ( opt ) {
	var maxLv = null;
	var minLv = null;
	var defaultOpt = null;
	var div = null;
	var divId = null;
	var pro_sel = null;
	var city_sel = null;
	var district_sel = null;
	var street_sel = null;
	var proUrl = null;
	var subUrl = null;
	
	var lv_id_map = null;
	var lv_select_map = null;
	var defaultList = null;
	var lv_area_list = null;
	var defaultList = null;
	
	{
		
		this.lv_id_map =  {
			"0" : "pro" , 
			"1" : "city" , 
			"2" : "district" , 
			"3" : "street" 
		};
		this.lv_select_map = {
			"0" : "pro_sel" , 
			"1" : "city_sel" , 
			"2" : "district_sel" , 
			"3" : "street_sel" 
		};
		this.defaultOpt = $.extend(this.defaultOpt, opt);
		this.maxLv = this.defaultOpt.maxLv;
		this.minLv = this.defaultOpt.minLv;
		this.div = this.defaultOpt.div;
		if ( !this.defaultOpt.divId ) {
			this.divId = $(this.defaultOpt.div).attr("id");
		} else {
			this.divId = this.defaultOpt.divId;
		}
		this.proUrl = this.defaultOpt.proUrl;
		this.subUrl = this.defaultOpt.subUrl;
		this.lv_area_list =  [
			this.divId+"pro" , 
			this.divId+"city" , 
			this.divId+"district" , 
			this.divId+"street" 
		];
		this.defaultList = this.defaultOpt.defaultList;
	}
	//初始化
	this.init = function () {
		this.createSelect();
		this.getAllPro();
		this.defaultList = null;
	}
	
	//创建组件
	this.createSelect = function () {
		//创建省份select
		this.pro_sel = $("<select/>").attr({"id":this.divId + "pro","lv":"0"});
		$(this.div).append($(this.pro_sel));
		//创建市select
		this.city_sel = $("<select/>").attr({"id":this.divId + "city","lv":"1"});
		$(this.div).append($(this.city_sel));
		//创建区select
		this.district_sel = $("<select/>").attr({"id":this.divId + "district","lv":"2"});
		$(this.div).append($(this.district_sel));
		//创建市select
		this.street_sel = $("<select/>").attr({"id":this.divId + "street","lv":"3"});
		$(this.div).append($(this.street_sel));
		
		for( var key in this.lv_id_map ) {
			var num = parseInt(key);
			if ( this.maxLv < num || this.minLv > num ) {
				$("#" + this.divId + this.lv_id_map[key]).hide();
			}
		}
	}
	
	this.getAllPro = function () {
		var ins = this;
		$.ajax({
			"url" : this.proUrl , 
			"type" : "post" , 
			"async" : false , 
			"success" : function( data ){
				data = eval ( "(" + data + ")" );
				$(ins.pro_sel).empty();
				var nextLv = parseInt($(ins.pro_sel).attr("lv"))+1;
				$(ins.pro_sel).change(function(){
					var index = $(this).find(":selected").index();
					var txt = $(this).find(":selected");
					var ifo = data[index];
					ins.getSubArea(nextLv,ifo,txt);
				});
				for( var i = 0 ; i < data.length ; i++ ){
					var info = data[i];
					var option = $("<option/>").text(info.name).val(info.id);
					$(ins.pro_sel).append($(option));
					$(option).click({ "ifo" : info , "nLv" : nextLv },function(_ev){
						var ifo = _ev.data.ifo;
						var nLv = _ev.data.nLv;
						var txt = $(this).text();
						ins.getSubArea(nLv,ifo,txt);
					});
				}
				if ( ins.defaultList && ins.defaultList.length > 0 ) {
					$(ins.pro_sel).find("option[value='"+ ins.defaultList[0] +"']").attr({"selected":"selected"}).click();
				} else {
					$(ins.pro_sel).find("option").eq(0).attr({"selected":"selected"}).click();
				}
			}
		});
	}
	
	this.getSubArea = function ( nextLv , data , optionText ) {
		var ins = this;
		var lv = nextLv;
		$.ajax({
			"url" : ins.subUrl , 
			"type" : "post" , 
			"async" : false , 
			"data" : { "id" : data.id } , 
			"success" : function( data ){
				data = eval ( "(" + data + ")" );
				var proto = ins.lv_select_map[lv];
				var select = ins[proto];
				if ( !select ) {
					return;
				}
				$(select).empty();
				var nextLv = parseInt($(select).attr("lv"))+1;
				$(select).change(function(){
					var index = $(this).find(":selected").index();
					var txt = $(this).find(":selected");
					var ifo = data[index];
					ins.getSubArea(nextLv,ifo,txt);
				});
				for( var i = 0 ; i < data.length ; i++ ){
					var info = data[i];
					var option = $("<option/>").text(info.name).val(info.id);
					$(select).append($(option));
					$(option).click({ "ifo" : info , "nLv" : nextLv },function(_ev){
						var ifo = _ev.data.ifo;
						var nLv = _ev.data.nLv;
						var txt = $(this).text();
						if ( ins.lv_select_map[nLv] ) {
							ins.getSubArea(nLv,ifo , txt);
						}
					});
				}
				if ( ins.defaultList && ins.defaultList.length > 0 ) {
					$(select).find("option[value='"+ ins.defaultList[lv] +"']").attr({"selected":"selected"}).click();
				} else {
					$(select).find("option").eq(0).attr({"selected":"selected"}).click();
				}
			}
		});
	}
	
	//获取地址字符串
	this.getAddress = function () {
		
	}
	
	
	this.init();
	
	return this;
}
