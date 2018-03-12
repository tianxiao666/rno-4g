
$(document).ready(function(){
	
	
	
	var left = $("#city_select").offset().left;
	$(".map_city").css({"left" : left+"px"} );
	
	
	$("#search_btn").click(function(){
		search_name = $("#search_city_input").val();
		var order_text = $(".sel_city_btnl_sel").text();
		loadUserArea2();
	});
	$(".sel_city_btnl").click(function(){
		$(".sel_city_btnl_sel").removeClass("sel_city_btnl_sel");
		$(this).addClass("sel_city_btnl_sel");
		var city_order_text = $(this).text();
		
		var first_letter_chioce_div = $("#first_letter_choice_div");
		$(first_letter_chioce_div).empty();
		
		var d = null;
		
		if ( city_order_text == "按省份" ) {
			if ( !province_data || province_data == null ) {
				loadUserArea2();
			} else {
				d = province_data;
			}
		} else {
			if ( !city_data || city_data == null ) {
				loadUserArea2();
			} else {
				d = city_data;
			}
		}
		
		$("<a class='first_letter_choice_a' href='javascript:void(0);'>全部</a>").appendTo($(first_letter_chioce_div));
		for ( var first_letter in d ) {
			//添加首字母筛选
			var first_letter_a = $("<a class='first_letter_choice_a' href='javascript:void(0);'></a>").appendTo($("#first_letter_choice_div"));
			$(first_letter_a).html(first_letter);
		}
		if ( city_order_text == "按省份" ) {
			provinceList(d);
		} else {
			cityList(d);
		}
		
		
	});
	$(".first_letter_choice_a").live("click" , function(){
		var first_letter = $(this).text();
		var order_text = $(".sel_city_btnl_sel").text();
		
		if ( order_text == "按省份" ) {
			var p = {};
			if ( first_letter == "全部" ) {
				p = province_data;
			} else {
				p[first_letter] = province_data[first_letter];
			}
			provinceList(p);
		} else {
			var p = {};
			if ( first_letter == "全部" ) {
				p = city_data;
			} else {
				p[first_letter] = city_data[first_letter];
			}
			cityList(p);
		}
		$(".first_letter_choice_a").each(function( index , obj ){
			if ( $(obj).text() == first_letter ) {
				$(obj).addClass("red");
			} else {
				$(obj).removeClass("red");
			}
		});
		
	});
	
	
	
	
	
	//获取当前登录人的区域列表
	$.ajax({
		"url" : "gis_area!gisLoginUserAreaListAction" , 
		"type" : "post" , 
		"async" : false , 
		"success" : function(result){
			var data = eval( "(" + result + ")" );
			
			user_area_list = data;
			user_area_id_list = new Array();
			for ( var i = 0 ; i < user_area_list.length ; i++ ) {
				var info = user_area_list[i];
				user_area_id_list.push( parseInt(info["id"]) );
			}
			loadUserArea2();
			
		}
	});
	
	
	IMapEvent.addListener(map, "zoom_changed", function() {
		console.log(map.getZoom());
	});
	
	
});




var province_data = null;
var city_data = null;
var search_name = "";
var user_area_list = null;
var default_city_zoom = 14;
var default_district_zoom = 16;
var default_street_zoom = 17;


function setUserArea ( ) {
	if ( !user_area_list || user_area_list.length <= 1 ) {
		return;
	}
	var p = user_area_list[1];
	var city_as = $("#province_table tbody .city_a");
	$(city_as).each(function(){
		var d = $(this).attr("data");
		if ( !d || d == "" || d == null ) {
			return true;
		}
		d = eval("(" + d + ")");
		var city_id = d["id"];
		if ( p["id"] == city_id ) {
			$(this).click();
			return false;
		}
	});
	
	if ( user_area_list.length <= 2 ) {
		return;
	}
	var p = user_area_list[2];
	city_as = $(".sel_district_hotcity .city_a");
	$(city_as).each(function(){
		var d = $(this).attr("data");
		if ( !d || d == "" || d == null ) {
			return true;
		}
		d = eval("(" + d + ")");
		var city_id = d["id"];
		if ( p["id"] == city_id ) {
			$(this).click();
			return false;
		}
	});
	
}


function loadUserArea2 () {
	var order_text = $(".sel_city_btnl_sel").text();
	$.ajax({
		"url" : "gis_area!gisCityListAction" , 
		"type" : "post" , 
		"async" : true , 
		"data" : { "name" : search_name , "order" : order_text } , 
		"success" : function(result){
			var data = eval( "(" + result + ")" );
			var first_letter_chioce_div = $("#first_letter_choice_div");
			$(first_letter_chioce_div).empty();
			$("<a class='first_letter_choice_a' href='javascript:void(0);'>全部</a>").appendTo($(first_letter_chioce_div));
			for ( var first_letter in data ) {
				//添加首字母筛选
				var first_letter_a = $("<a class='first_letter_choice_a' href='javascript:void(0);'></a>").appendTo($("#first_letter_choice_div"));
				$(first_letter_a).html(first_letter);
			}
			if ( order_text == "按省份" ) {
				province_data = data;
				provinceList(data);
			} else {
				city_data = data;
				cityList(data);
			}
		}
	});
}


/**
 * 按省份,排列
 * @param {Object} data
 * @memberOf {TypeName} 
 */
function provinceList ( data ) {
	var province_table = $("#province_table tbody");
	$(province_table).empty();
	
	//循环拼音
	for( var first_letter in data ){
		var provinces_list = data[first_letter];
		//不存在省份不作操作
		if ( !provinces_list || provinces_list == null ) {
			continue;
		}
		var province_count = 0;
		var first_tr = null;
		//循环省份
		for ( var province_name in provinces_list ) {
			var tr = $("<tr></tr>").appendTo($(province_table));
			province_count++;
			if ( province_count == 1 ) {
				first_tr = tr;
			}
			var province = provinces_list[province_name];
			var city_list = province["child"];
			var province_info = province["info"];
			//省份td					
			var province_td = $("<td class='sel_city_td_sf'><a href='javascript:void(0)'>" + province_name + ":</a></td>").appendTo($(tr));
			var city_td = $("<td></td>").appendTo($(tr));
			for ( var i = 0 ; i < city_list.length; i++ ) {
				var city = city_list[i];
				var city_name = city["name"];
				var city_a = $("<a class='city_a' href='javascript:void(0)'>" + city_name + "</a>").appendTo($(city_td));
				$(city_a).attr({"data":obj2String(city)});
				$(city_a).click({ "d" : city },function(_ev){
					var c = _ev.data.d;
					$(".city_a").removeClass("sel_city_red");
					$(this).addClass("sel_city_red");
					$("#now_choice_city").text(c.name);
					$("#city_select").text(c.name);
					$("#select_area_id").val(c.id);
					var lat = c.latitude;
					var lng = c.longitude;
					if ( typeof(c.latitude) == "undefind" || c.latitude == null || typeof(c.longitude) == "undefind" || c.longitude == null ) {
						lat = 23.14651977;
						lng = 113.34106554;
					}
					var latlng = new ILatLng(lat , lng);
					map.setCenter(latlng);
					if ( user_area_id_list == null ) {
						map.setZoom(default_city_zoom);
					}
					$(".map_city").hide();
					districtList();
				});
				for ( var k = 0 ; user_area_id_list != null && k < user_area_id_list.length ; k++ ) {
					var a_id = user_area_id_list[k];
					if ( city.id == a_id ) {
						$(city_a).click();
						break;
					}
				}
			}
		}
		$("<tr><td colspan='3'><div class='sel_city_tr_splitline'>&nbsp;</div></td></tr>").appendTo($(province_table));
		//首字母td
		if (first_letter == "其他" ){
			first_letter = "&nbsp;";
		}
		var first_letter_td = $("<td rowspan='" + province_count + "' class='sel_city_td_letter'><div>" + first_letter + "</div></td>").prependTo($(first_tr));
	}
}



/**
 * 按城市,排列
 * @param {Object} data
 * @memberOf {TypeName} 
 */
function cityList ( data ) {
	var province_table = $("#province_table tbody");
	$(province_table).empty();
	
	//循环拼音
	for( var first_letter in data ){
		var first_letter_list = data[first_letter];
		//不存在省份不作操作
		if ( !first_letter_list || first_letter_list == null ) {
			continue;
		}
		var citys_list = first_letter_list["child"];
		if ( !citys_list || citys_list == null ) {
			continue;
		}
		var tr = $("<tr></tr>").appendTo($(province_table));
		var city_td = $("<td></td>").appendTo($(tr));
		for ( var i = 0 ; i < citys_list.length ; i++ ) {
			var city = citys_list[i];
			var city_name = city["name"];
			var city_a = $("<a class='city_a' href='javascript:void(0)'>" + city_name + "</a>").appendTo($(city_td));
			$(city_a).attr({"data":obj2String(city)});
			$(city_a).click({ "d" : city },function(_ev){
				var c = _ev.data.d;
				$(".city_a").removeClass("sel_city_red");
				$(this).addClass("sel_city_red");
				$("#now_choice_city").text(c.name);
				$("#city_select").text(c.name);
				$("#select_area_id").val(c.id);
				var lat = c.latitude;
				var lng = c.longitude;
				if ( typeof(c.latitude) == "undefind" || c.latitude == null || typeof(c.longitude) == "undefind" || c.longitude == null ) {
					lat = 23.14651977;
					lng = 113.34106554;
				}
				var latlng = new ILatLng(lat , lng);
				map.setCenter(latlng);
				if ( user_area_id_list == null ) {
					map.setZoom(default_city_zoom);
				}
				$(".map_city").hide();
				districtList();
			});
			for ( var k = 0 ; user_area_id_list != null && k < user_area_id_list.length ; k++ ) {
				var a_id = user_area_id_list[k];
				if ( city.id == a_id ) {
					$(city_a).click();
					break;
				}
			}
		}
		$("<tr><td colspan='3'><div class='sel_city_tr_splitline'>&nbsp;</div></td></tr>").appendTo($(province_table));
		//首字母td
		var first_letter_td = $("<td class='sel_city_td_letter'><div>" + first_letter + "</div></td>").prependTo($(tr));
	}
	$("<tr><td colspan='2'><div class='sel_city_tr_splitline'>&nbsp;</div></td></tr>").appendTo($(province_table));
	$("<tr><td colspan='2'><div class='sel_city_tr_splitline'>&nbsp;</div></td></tr>").appendTo($(province_table));
}



function districtList () {
	$("#district_select").text("请选择");
	$("#now_choice_district").text("");
	$("#district_select").css({"display":""});
	$("#street_select").css({"display":"none"});
	var area_id = $("#select_area_id").val();
	var left = $("#district_select").offset().left;
	$(".map_district").css({"left" : left+"px"} );
	$.ajax({
		"url" : "gis_area!gisSubAreaListAction" , 
		"type" : "post" , 
		"data" : { "areaId" : area_id } , 
		"success" : function(result){
			var data = eval( "(" + result + ")" );
			$(".sel_district_hotcity").empty();
			if ( !data || data == null ) {
				return;
			}
			for ( var i = 0 ; i < data.length ; i++ ) {
				var city = data[i];
				var city_a = $("<a class='city_a' href='javascript:void(0);'>" + city["name"] + "</a>").appendTo($(".sel_district_hotcity"));
				$(city_a).attr({"data":obj2String(city)});
				$(city_a).click({ "d" : city },function(_ev){
					var c = _ev.data.d;
					$(".city_a").removeClass("sel_city_red");
					$(this).addClass("sel_city_red");
					$("#now_choice_district").text(c.name);
					$("#district_select").text(c.name);
					$("#select_area_id").val(c.id);
					var lat = c.latitude;
					var lng = c.longitude;
					if ( typeof(c.latitude) == "undefind" || c.latitude == null || typeof(c.longitude) == "undefind" || c.longitude == null ) {
						lat = 23.14651977;
						lng = 113.34106554;
					}
					var latlng = new ILatLng(lat , lng);
					map.setCenter(latlng);
					if ( user_area_id_list == null ) {
						map.setZoom(default_district_zoom);
					}
					$(".map_district").hide();
					streetList();
				});
				for ( var k = 0 ; user_area_id_list != null && k < user_area_id_list.length ; k++ ) {
					var a_id = user_area_id_list[k];
					if ( city.id == a_id ) {
						$(city_a).click();
						break;
					}
				}
			}
		}
	});
}


function streetList () {
	$("#street_select").text("请选择");
	$("#now_choice_street").text("");
	$("#street_select").css({"display":""});
	var area_id = $("#select_area_id").val();
	var left = $("#street_select").offset().left;
	$(".map_street").css({"left" : left+"px"} );
	$.ajax({
		"url" : "gis_area!gisSubAreaListAction" , 
		"type" : "post" , 
		"data" : { "areaId" : area_id } , 
		"success" : function(result){
			var data = eval( "(" + result + ")" );
			$(".sel_street_hotcity").empty();
			if ( !data || data == null ) {
				return;
			}
			for ( var i = 0 ; i < data.length ; i++ ) {
				var city = data[i];
				var city_a = $("<a class='city_a' href='javascript:void(0);'>" + city["name"] + "</a>").appendTo($(".sel_street_hotcity"));
				$(city_a).attr({"data":obj2String(city)});
				$(city_a).click({ "d" : city },function(_ev){
					var c = _ev.data.d;
					$(".city_a").removeClass("sel_city_red");
					$(this).addClass("sel_city_red");
					$("#now_choice_street").text(c.name);
					$("#street_select").text(c.name);
					$("#select_area_id").val(c.id);
					var lat = c.latitude;
					var lng = c.longitude;
					if ( typeof(c.latitude) == "undefind" || c.latitude == null || typeof(c.longitude) == "undefind" || c.longitude == null ) {
						lat = 23.14651977;
						lng = 113.34106554;
					}
					var latlng = new ILatLng(lat , lng);
					map.setCenter(latlng);
					if ( user_area_id_list == null ) {
						map.setZoom(default_street_zoom);
					}
					$(".map_street").hide();
				});
				for ( var k = 0 ; user_area_id_list != null && k < user_area_id_list.length ; k++ ) {
					var a_id = user_area_id_list[k];
					if ( city.id == a_id ) {
						$(city_a).click();
						break;
					}
				}
			}
			user_area_id_list = null;
		}
	});
}




/**
 * 美工代码
 */
$(function(){
	$(".city_show").click(function(){
		$(".map_city").show();
		$(".map_district").hide();
		$(".map_street").hide();
	});
	$(".district_show").click(function(){
		$(".map_district").show();
		$(".map_city").hide();
		$(".map_street").hide();
	});
	$(".street_show").click(function(){
		$(".map_street").show();
		$(".map_city").hide();
		$(".map_district").hide();
	});
	$(".close_icon").click(function(){
		$(".map_city").hide();
		$(".map_district").hide();
		$(".map_street").hide();
	});
})






