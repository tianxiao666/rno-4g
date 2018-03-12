
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
	
	
	
	//获取当前登录人的区域id 和上级区域 id 列表
	$.ajax({
		"url" : "gis_area_resource!getUserParentAreaAndAreaIdsListAction" , 
		"type" : "post" , 
		"async" : false , 
		"success" : function(result){
			var data = eval( "(" + result + ")" );
			user_areaids_string=data[0].split(",");//用户权限区域id String
			user_parentareaids_string=data[1].split(",");//用户权限上级区域id String
			
		}
	});
	
	//获取当前登录人的区域列表
	var stype = $("#saveSType").val();
	if(stype==""){
		$.ajax({
			"url" : "gis_area_resource!gisLoginUserAreaListAction" , 
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
				cacelSearchResource();
				var stype = $("#saveSType").val();
				if(stype==""){
					$("#searchType").val("Station");//加载站址
					$("#sTypeText").val("站址");
					var params = {selectResType:'Station'};
					$.post('getSearchAttributesAction',params,function(data){
						$(".up_search").remove();
						$(".search_result ul").append(data);
						if($(".up_search_show").text()=="基本查询 ▲"){
							$(".up_search").show();
						}else{
							$(".up_search").hide();
						}
					})
					loadAreaResource($("#areaId").val(),$("#searchType").val(),$("#sTypeText").val());
				}
			}
		});
	}else{
		var ids=$("input[name='cityAreaId']").val()+","+$("input[name='districtAreaId']").val()+","+$("input[name='streetAreaId']").val();
		var params={currentEntityId:ids,currentEntityType:"Sys_Area"};
		$.ajax({
			"url" : "getResourceEntitysByIdsAndTypeAction" , 
			"type" : "post" , 
			"async" : false , 
			"data":params,
			"success" : function(result){
				var data = eval( "(" + result + ")" );
				
				user_area_list = data;
				user_area_id_list = new Array();
				for ( var i = 0 ; i < user_area_list.length ; i++ ) {
					var info = user_area_list[i];
					user_area_id_list.push( parseInt(info["id"]) );
				}
				loadUserArea2();
				cacelSearchResource();
				var stype = $("#saveSType").val();
				if(stype==""){
					$("#searchType").val("Station");//加载站址
					$("#sTypeText").val("站址");
					var params = {selectResType:'Station'};
					$.post('getSearchAttributesAction',params,function(data){
						$(".up_search").remove();
						$(".search_result ul").append(data);
						if($(".up_search_show").text()=="基本查询 ▲"){
							$(".up_search").show();
						}else{
							$(".up_search").hide();
						}
					})
					loadAreaResource($("#areaId").val(),$("#searchType").val(),$("#sTypeText").val());
				}
			}
		});
	}
	
	
	
});

//yuan.yw   
var user_areaids_string=new Array();  //用户权限区域 id
var user_parentareaids_string=new Array(); //用户权限上级区域 id
var intervalSet;//setInterval
var secondIndex=3;//当前多少秒

//几秒后查询数据
function refreshSecondInterval(areaId,searchType,searchTypeName){
clearInterval(intervalSet);
$("#searchEm").show();
intervalSet = setInterval(function(){
	if(secondIndex==1){
		clearInterval(intervalSet);
		$("#searchEm").hide();
		loadAreaResource(areaId,searchType,searchTypeName);
		secondIndex=3;
		$("#refreshSecond").html(secondIndex);
	}else{
		secondIndex--;
		$("#refreshSecond").html(secondIndex);
	}
	
},1000);

}
//取消加载数据
function cacelSearchResource(){
	clearInterval(intervalSet);
	$("#searchEm").hide();
	secondIndex=3;
	$("#refreshSecond").html(secondIndex);
}
//区域id是否在权限区域
function isInAreaArray(str){
	var flag = false;
	for(var i=0;i<user_areaids_string.length;i++){
		if(str==user_areaids_string[i]){
			flag=true;
			break;
		}
	}
	if(!flag){
		for(var i=0;i<user_parentareaids_string.length;i++){
			if(str==user_parentareaids_string[i]){
				flag=true;
				break;
			}
		}
	}
	return flag;

}

var province_data = null;
var city_data = null;
var search_name = "";
var user_area_list = null;
var default_city_zoom = 12;
var default_district_zoom = 14;
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
		"url" : "gis_area_resource!gisCityListAction" , 
		"type" : "post" , 
		"async" : false , 
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
				var city_a = $("<a onclick='cityClick(this)' class='city_a' href='javascript:void(0)'>" + city_name + "</a>");
				if(isInAreaArray(city.id+"")){//yuan.yw
					city_a = $("<a onclick='cityClick(this)' class='city_a' href='javascript:void(0)'>" + city_name + "</a>");
				}else{
					city_a = $("<a style='color:grey'  href='javascript:void(0)'>" + city_name + "</a>");
				}
				$(city_a).attr({"data":obj2String(city)});
				$(city_a).appendTo($(city_td));
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

function cityClick (widget) {
	var c = $(widget).attr("data");
	if ( c == null || c == "" ) {
		return;
	}
	var c = eval( "(" + c + ")" );
	$(".city_a").removeClass("sel_city_red");
	$(widget).addClass("sel_city_red");
	$("#now_choice_city").text(c.name);
	$("#city_select").text(c.name);
	$("#select_area_id").val(c.id);
	$(".map_city").hide();
	$("#areaId").val(c.id);
	$("#areaType").val(c._entityType);
	$("#cityAreaId").val(c.id);
	$("#districtAreaId").val("");
	$("#streetAreaId").val("");
	districtList();
	refreshSecondInterval(c.id+"",$("#searchType").val(),$("#sTypeText").val());//yuan.yw
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
			//var city_a = $("<a onclick='cityClick(this)' class='city_a' href='javascript:void(0)'>" + city_name + "</a>").appendTo($(city_td));
			var city_a;
			if(isInAreaArray(city.id+"")){//yuan.yw
				city_a = $("<a onclick='cityClick(this)' class='city_a' href='javascript:void(0)'>" + city_name + "</a>").appendTo($(city_td));
			}else{
				city_a = $("<a style='color:grey' class='city_a' href='javascript:void(0)'>" + city_name + "</a>").appendTo($(city_td));
			}
			$(city_a).attr({"data":obj2String(city)});
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
		"url" : "gis_area_resource!gisSubAreaListAction" , 
		"type" : "post" , 
		"async" : false ,
		"data" : { "areaId" : area_id } , 
		"success" : function(result){
			var data = eval( "(" + result + ")" );
			$(".sel_district_hotcity").empty();
			if ( !data || data == null ) {
				return;
			}
			for ( var i = 0 ; i < data.length ; i++ ) {
				var city = data[i];
				//var city_a = $("<a onclick='districtClick(this)' class='district_a' href='javascript:void(0);'>" + city["name"] + "</a>").appendTo($(".sel_district_hotcity"));
				var city_a="";
				if(isInAreaArray(city.id+"")){//yuan.yw
					city_a = $("<a onclick='districtClick(this)' class='district_a' href='javascript:void(0);'>" + city["name"] + "</a>").appendTo($(".sel_district_hotcity"));
				}else{
					city_a = $("<a style='color:grey' class='district_a' href='javascript:void(0);'>" + city["name"] + "</a>").appendTo($(".sel_district_hotcity"));
				}
				$(city_a).attr({"data":obj2String(city)});
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

function districtClick ( widget ) {
	var c = $(widget).attr("data");
	if ( c == null || c == "" ) {
		return;
	}
	var c = eval( "(" + c + ")" );
	$(".district_a").removeClass("sel_city_red");
	$(widget).addClass("sel_city_red");
	$("#now_choice_district").text(c.name);
	$("#district_select").text(c.name);
	$("#select_area_id").val(c.id);
	$(".map_district").hide();
	$("#areaId").val(c.id);
	$("#areaType").val(c._entityType);
	$("#districtAreaId").val(c.id);
	$("#streetAreaId").val("");
	streetList();
	refreshSecondInterval(c.id+"",$("#searchType").val(),$("#sTypeText").val());//yuan.yw
}


function streetList () {
	$("#street_select").text("请选择");
	$("#now_choice_street").text("");
	$("#street_select").css({"display":""});
	var area_id = $("#select_area_id").val();
	var left = $("#street_select").offset().left;
	$(".map_street").css({"left" : left+"px"} );
	$.ajax({
		"url" : "gis_area_resource!gisSubAreaListAction" , 
		"type" : "post" , 
		"async" : false ,
		"data" : { "areaId" : area_id } , 
		"success" : function(result){
			var data = eval( "(" + result + ")" );
			$(".sel_street_hotcity").empty();
			if ( !data || data == null ) {
				return;
			}
			for ( var i = 0 ; i < data.length ; i++ ) {
				var city = data[i];
				//var city_a = $("<a class='street_a' href='javascript:void(0);'>" + city["name"] + "</a>").appendTo($(".sel_street_hotcity"));
				var city_a;
				if(isInAreaArray(city.id+"")){//yuan.yw
					city_a = $("<a class='street_a' href='javascript:void(0);'>" + city["name"] + "</a>").appendTo($(".sel_street_hotcity"));
					$(city_a).attr({"data":obj2String(city)});
					$(city_a).click({ "d" : city },function(_ev){
						var c = _ev.data.d;
						$(".street_a").removeClass("sel_city_red");
						$(this).addClass("sel_city_red");
						$("#now_choice_street").text(c.name);
						$("#street_select").text(c.name);
						$("#select_area_id").val(c.id);
						$(".map_street").hide();
						$("#areaId").val(c.id);
						$("#areaType").val(c._entityType);
						$("#streetAreaId").val(c.id);
						refreshSecondInterval(c.id+"",$("#searchType").val(),$("#sTypeText").val());//yuan.yw
						
					});
				}else{
					city_a = $("<a style='color:grey' href='javascript:void(0);'>" + city["name"] + "</a>").appendTo($(".sel_street_hotcity"));
					$(city_a).attr({"data":obj2String(city)});
				}
				
				for ( var k = 0 ; user_area_id_list != null && k < user_area_id_list.length ; k++ ) {
					var a_id = user_area_id_list[k];
					if ( city.id == a_id ) {
						$(city_a).click();
						break;
					}
				}
			}
			user_area_id_list = new Array();
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






