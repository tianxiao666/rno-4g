
function openCommonBaseStationSingleDivByAccount(){
	$.post("../organization/getTheTopOrgByAccountAction",function(data){
		if(data!=null && data.length>0){
			var orgId = data[0].orgId;//yuan.yw
			openCommonBaseStationSingleDiv(orgId);
		}
	},"json");
}

//打开该模块窗体
function openCommonBaseStationSingleDiv(orgId){
	$("#rex").val("");
	$("#stl").html("<div class='flattening_loading'  style='text-align:center'><img src='images/loading_img.gif'><br>数据处理中，请稍侯...</div>");
	$("#stationList").show();
	$.post("getListBaseStationAction",{"orgId":orgId},function(data){
		$("#acceptProfessional").html("");
		$("#faultType").html("");
		$("#acceptProfessional").append("<option value=''>请选择</option>");
		$("#faultType").append("<option value=''>请选择</option>");
		var context = getDateToHTML(data);
		$("#stl").html(context);
		$("#black").show();
		$("#common_orgId").val(orgId);
	},"json");
}

//模糊查询
function baseStationSingleFuzzy(){
	$("#stl").html("<div class='flattening_loading'  style='text-align:center'><img src='images/loading_img.gif'><br>数据处理中，请稍侯...</div>");
	$("#stationList").show();
	var orgId = $("#common_orgId").val();
	var rex = $("#rex").val();
	$.post("getListFuzzyBaseStationAction",{"orgId":orgId,"fuzzy":rex},function(data){
		$("#acceptProfessional").html("");
		$("#faultType").html("");
		$("#acceptProfessional").append("<option value=''>请选择</option>");
		$("#faultType").append("<option value=''>请选择</option>");
		var context = getDateToHTML(data);
		$("#stl").html(context);
		$("#black").show();
		$("#common_orgId").val(orgId);
	},"json");
}

//拼音查询
function baseStationSinglePinyinQuery(pinyin){
	$("#stl").html("<div class='flattening_loading'  style='text-align:center'><img src='images/loading_img.gif'><br>数据处理中，请稍侯...</div>");
	$("#stationList").show();
	var orgId = $("#common_orgId").val();
	$.post("getListPinyinBaseStationAction",{"orgId":orgId,"pinyin":pinyin},function(data){
		$("#acceptProfessional").html("");
		$("#faultType").html("");
		$("#acceptProfessional").append("<option value=''>请选择</option>");
		$("#faultType").append("<option value=''>请选择</option>");
		var context = getDateToHTML(data);
		$("#stl").html(context);
		$("#black").show();
		$("#common_orgId").val(orgId);
	},"json");
}

//获取站址
function stationByIdAndType(id,type,hidden_header){
	$.post("getStationByIdAndTypeAction",{"resourceId":id,"resourceType":type},function(data){
		if(data){
			$("#"+hidden_header+"_stationId").val(data.id);
			$("#"+hidden_header+"_stationName").val(data.name);
			$("#"+hidden_header+"_stationType").val(data._entityType);
			$("#"+hidden_header+"_location").val(data.address);
			baseStationByIdAndType(id,type,hidden_header);
			showAreaByIdAndType(data.id,data._entityType,hidden_header);
		}
	},"json");
}

function showAreaByIdAndType(id,type,hidden_header){
	$.post("getAllAreaForUpByIdAction",{"resourceId":id,"resourceType":type},function(data){
		var areaStr = "";
		$.each(data,function(index,value){
			areaStr = "-"+value.name + areaStr;
		});
		areaStr = areaStr.substring(1,areaStr.length);
		$("#"+hidden_header+"_Area").val(areaStr);
		bsSingleSubmit(hidden_header);
	},"json");
}

function baseStationByIdAndType(id,type,hidden_header){
		$.ajax({
		url:"getBaseFacilityAction",
		async:false,
		type:"POST",
		dataType:"json",
		data:{"resourceId":id,"resourceType":type},
		success : function(result) {
			if(result && result.entity){
			    $("#"+hidden_header+"_importancegrade").val(result.entity.importancegrade);
    		}
		}
	});
}

//根据基站名获取基站
function getBaseStationByBaseStationName(baseStationName){
	var array = new Array();
	 $.ajax({
	     url:"getBaseStationByBaseStationNameAjaxAction",
	     async:false,
	     type:"POST",
	     dataType:"json",
	     data:{"baseStationName":baseStationName},
	     success : function(result) {
	     	if(result){
		     	var baseStationId = result.id;
		     	var baseStationType = result._entityType;
		     	if(baseStationId && baseStationType){
		     		array[0] = baseStationId;
		     		array[1] = baseStationType;
		     	}
	     	}
	     }
	});
	return array;
}

//选择基站反填
function bsSingleSubmit(hidden_header){
	$("#selectStationId").val($("#"+hidden_header+"_id").val());
	$("#selectStationName").val($("#"+hidden_header+"_name").val());
	$("#stationLocation").val($("#"+hidden_header+"_location").val());
	$("#lng").val($("#"+hidden_header+"_longitude").val());
	$("#lat").val($("#"+hidden_header+"_latitude").val());
	$("#stationType").val($("#"+hidden_header+"_type").val());
	$("#station_Name").val($("#"+hidden_header+"_stationName").val());
	$("#station_id").val($("#"+hidden_header+"_stationId").val());
	$("#stationOfArea").val($("#"+hidden_header+"_Area").val());
	$("#baseStationLevel").val($("#"+hidden_header+"_importancegrade").val());
	$("#stationList").hide();
	$("#black").hide();
	//抢修专有
	var area = $("#stationOfArea").val().split("-");
	if(area[1]!=null&&area[1]!=""){
		//获取数据字典-受理专业
		getAcceptProfessinalDictionary(area[1]);
		//获取数据字典-故障类型
		getFaultTypeDictionary(area[1]);
	}
}

function checkedStationRadio(me){
	$(me).find("input[name='stationRadio']").attr("checked","checked");
}

function cancelstl(){
	$("#stationList").hide();
	$("#black").hide();
}

function getDateToHTML(data){
	var context = "<table class=\"selectStation-table\">	";
	$.each(data,function(k,v){
		var j = k + 1;
		if(j%4 == 1 || j == 0){
			if(j > 1){
				context =  context + "</tr>";
			}
			context =  context + "<tr>";
		}
		context = context  
		+"<td class=\"rows1\" onclick=\"checkedStationRadio(this);\">"
		+	"	<input id=\"\" type=\"radio\" name=\"stationRadio\"";
		var id = "";
		if(v.id == null || v.id == ''){
			id = "";
		}else{
			id = v.id;
		}
		var name = "";
		if(v.name == null || v.name == ''){
			name = "";
		}else{
			name = v.name;
		}
		var address = "";
		if(v.address == null || v.address == ''){
			address = "";
		}else{
			address = v.address;
		}
		var longitude = "";
		if(v.longitude == null || v.longitude == ''){
			longitude = "";
		}else{
			longitude = v.longitude;
		}
		var latitude = "";
		if(v.latitude == null || v.latitude == ''){
			latitude = "";
		}else{
			latitude = v.latitude;
		}
		var entityType = "";
		if(v._entityType == null || v._entityType == ''){
			entityType = "";
		}else{
			entityType = v._entityType;
		}
		var stationid = "";
		if(v.stationid == null || v.stationid == ''){
			stationid = "";
		}else{
			stationid = v.stationid;
		}
		context = context 
		+	"		value=\""+id+","+name+","+address+","+longitude+","+latitude+","+entityType+","+stationid+"\" pinyin="+v.pinyin+"/>"
		+	"	<span>"+name+"</span>"
		+"	</td>";
		
			
		if(k == (data.length-1)){
			if(k%4 == 1){
					context = context + "<td class=\"rows1\"></td>"
										+ "<td class=\"rows1\"></td>"
										+ "<td class=\"rows1\"></td>";
			}else if(k%4 == 2){
					context = context + "<td class=\"rows1\"></td>"
										+ "<td class=\"rows1\"></td>";
			}else if(k%4 == 3){
					context = context + "<td class=\"rows1\"></td>";
			}
		}
	});
	context = context + "</tr>"
							+"</table>";
	return context;
}

function clickBaseStation(){
	var radio = $("input[name=stationRadio]:checked").val();
		if(radio==undefined){
			alert("请选中基站");
		}else{
			var info = radio.split(",");
			$("#common_id").val(info[0]);
			$("#common_name").val(info[1]);
			$("#common_type").val(info[5]);
			$("#common_longitude").val(info[3]);
			$("#common_latitude").val(info[4]);
			$("#common_location").val(info[2]);
			$("#common_stationId").val(info[6]);
			stationByIdAndType(info[0],info[5],"common");
			//bsSingleSubmit();
		}
}
