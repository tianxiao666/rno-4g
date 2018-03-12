$(document).ready(function(){

$("#currentWorkOrder").change(function(){
		getOrderList("workOrder","");
})	
$("#historyWorkOrder").change(function(){
		getOrderList("workOrder","");
})	
$("#currentTaskOrder").change(function(){
		getOrderList("taskOrder","");
})	
$("#historyTaskOrder").change(function(){
		getOrderList("taskOrder","");
})	


});

function getNetworkResourceMaintenanceUl(params,action){
				//$("#chi_infoMap_div").html("<div class='flattening_loading'><em>数据载入中</em><p></p></div>");
				$("#chi_ul_div").hide();
				$(".tab_content").hide();
				//$("#chi_infoMap_div").show();
				//$("#infoMap_tab_content").show();
				$("#flattening_service_tab_content").show();
				//$("#som_info_ul").html("<div class='flattening_loading'><em>数据载入中</em><p></p></div>");
				$("#som_info_ul").html("<div class='flattening_loading'  style='text-align:center'><img src='image/loading_img.gif'><br>数据处理中，请稍侯...</div>");
				
				var context = "";
				var indexFlag=0;
				var firstRecordId="";
				$.post("../maintain/"+action,params,function(data){
					$.each(data, function(key,value){
						if(indexFlag==0){
							if(value.op_category == '添加资源'){
								context = context 
								+"<li class='ico_5 selected'>";
							}else if(value.op_category == '属性修改'){
								context = context 
								+"<li class='ico_4 selected'>";
							}else if(value.op_category == "资源关联"){
								context = context 
								+"<li class='ico_1 selected'>";
							}else if(value.op_category == "解除关联"){
								context = context 
								+"<li class='ico_2 selected'>";
							}else if(value.op_category == "维护服务"){
								context = context 
								+"<li class='ico_3 selected'>";
							}else if(value.op_category == "删除资源"){
								context = context 
								+"<li class='ico_6 selected'>";
							}else{
								context = context 
								+"<li class='ico_1 selected'>";
							}
							firstRecordId=value.id;
						}else{
							if(value.op_category == '添加资源'){
								context = context 
								+"<li class='ico_5'>";
							}else if(value.op_category == '属性修改'){
								context = context 
								+"<li class='ico_4'>";
							}else if(value.op_category == "资源关联"){
								context = context 
								+"<li class='ico_1'>";
							}else if(value.op_category == "解除关联"){
								context = context 
								+"<li class='ico_2'>";
							}else if(value.op_category == "维护服务"){
								context = context 
								+"<li class='ico_3'>";
							}else if(value.op_category == "删除资源"){
								context = context 
								+"<li class='ico_6'>";
							}else{
								context = context 
								+"<li class='ico_1'>";
							}
						}
						var contents = "";
						if(value.content && value.content != "null"){
							contents = value.content.split("$_$");
						}
						context = context
                            	+"<h4>"+value.op_category+",<span class='grey'>"+value.op_scene+"</span>,<span class='grey'>"+value.op_cause+"</span>"
                             	+" <div class='som_info_div'>"
                                +"<em class='fl'>"+value.op_time+"</em>"
                           		+"<em class='fr fr1'>"+value.user_name+"</em>"
                           		+" </div></h4>"
                           		+"<div class='abd_right'><span class='ellipsis_span'>详细信息:"+contents+"</span><em class='abd_right_go'><a href='#' onclick='showDetailed(\""+value.id+"\",this);'>>></a></em></div>"
                       			+  "  </li>";
                       	indexFlag++;
					});
					if(context == ""){
						context = "没有此时间范围内的历史记录。";
					}
					$("#som_info_ul").html(context);
					if(context != ""){
						var aHref=$("#som_info_ul").children().eq(0).children().eq(1).children().eq(1).children().eq(0);
						if(firstRecordId != "" && firstRecordId != null){
							showDetailed(firstRecordId,aHref);
						}
					}
				},"json");
}

function searchMaintenance(me){
			$("#gisDivContent").hide();
			$("#Latest_Month").removeClass("selected");
			$("#Latest_Year").removeClass("selected");
			$("#All_Time").addClass("selected");
				var resourceType = $("input[name='rootCurrentEntityType']").val();
				var resourceId = $("input[name='rootCurrentEntityId']").val();
				var searchCondition = $("#search_textinput_net").val();
				if(searchCondition == null || searchCondition == "" || searchCondition == "请输入查询条件"){
					alert("请输入查询条件");
					return;
				}
				var params = {resourceId:resourceId,resourceType:resourceType,searchCondition:searchCondition};
				getNetworkResourceMaintenanceUl(params,"searchNetworkResourceMaintenanceByCondition");
}

function getMaintenance(){
			$("#gisDivContent").hide();
			$("#Latest_Month").removeClass("selected");
			$("#Latest_Year").removeClass("selected");
			$("#All_Time").addClass("selected");
				var resourceType = $("input[name='rootCurrentEntityType']").val();
				var resourceId = $("input[name='rootCurrentEntityId']").val();
				var params = {resourceId:resourceId,resourceType:resourceType,sTime:"",eTime:""};
				getNetworkResourceMaintenanceUl(params,"loadNetworkResourceMaintenanceAction");
				
			}
			
			function showDetailed(id,me){
				$(".flattening_service_ul li").removeClass("selected");
				$(me).parent().parent().parent().addClass("selected");
				$("#flattening_service_right").html("");
				var context = "";
				$.post("../maintain/loadNetworkResourceMaintenanceByIdAction",{mId:id},function(data){
					if(data){
					context = context
							+"<tr>"
								+"<td class='table_title' colspan='4'>资源维护与服务记录：详细信息</td>"
							+"</tr>";
					var did = "";
					if(data.id && data.id != "null"){
						did = data.id;
					}
						context = context
					+"<tr>"
                   	+" <td class='table_td'>唯一标识</td>"
                  	+ " <td colspan='3'>"+did+"</td>"
               		+" </tr>";
               		var biz_module = "";
					if(data.biz_module && data.biz_module != "null"){
						biz_module = data.biz_module;
					}
               		context = context
        	      +"<tr>"
                   +" <td class='table_td'><span class='blue'>业务模块</span></td>"
                  + " <td colspan='3'>"+biz_module+"</td>"
               		+" </tr>";
               		var biz_processcode = "";
					if(data.biz_processcode && data.biz_processcode != "null"){
						biz_processcode = data.biz_processcode;
					}
               		context = context
        	      +"<tr>"
                   +" <td class='table_td'><span class='blue'>业务信息唯一标识</span></td>"
                  + " <td colspan='3'>"+biz_processcode+"</td>"
               		+" </tr>";
               		var biz_rocessId = "";
					if(data.biz_rocessId && data.biz_rocessId != "null"){
						biz_rocessId = data.biz_rocessId;
					}
               		context = context
        	      +"<tr>"
                   +" <td class='table_td'><span class='blue'>业务信息id</span></td>"
                  + " <td colspan='3'>"+biz_rocessId+"</td>"
               		+" </tr>";
               		
               		var op_scene = "";
					if(data.op_scene && data.op_scene != "null"){
						op_scene = data.op_scene;
					}
               		context = context
               		+"<tr>"
                   +" <td class='table_td'><span class='blue'>操作场景</span></td>"
                  + " <td colspan='3'>"+op_scene+"</td>"
               		+" </tr>";
               		var op_cause = "";
					if(data.op_cause && data.op_cause != "null"){
						op_cause = data.op_cause;
					}
               		context = context
               		+"<tr>"
                   +" <td class='table_td'><span class='blue'>操作原因</span></td>"
                  + " <td colspan='3'>"+op_cause+"</td>"
               		+" </tr>";
               		var op_category = "";
					if(data.op_category && data.op_category != "null"){
						op_category = data.op_category;
					}
               		context = context
               		+"<tr>"
                   +" <td class='table_td'>操作分类</td>"
                  + " <td colspan='3'>"+op_category+"</td>"
               		+" </tr>";
               		var linkurl = "";
					if(data.linkurl && data.linkurl != "null"){
						linkurl = data.linkurl;
					}
               		context = context
               		+"<tr>"
                   +" <td class='table_td'>链接</td>"
                  + " <td colspan='3'>"+linkurl+"</td>"
               		+" </tr>";
               		var content;
               		var contents = "";
					if(data.content && data.content != "null"){
						content = data.content.split("$_$");
					}
					if(content){
						$.each(content,function(k,v){
						contents = contents + v + "</br>"
						});
					}
               		context = context
               		+"<tr>"
                   +" <td class='table_td'><span class='blue'>详细内容</span></td>"
                  + " <td colspan='3'>"+contents+"</td>"
               		+" </tr>";
               		var user_name = "";
					if(data.user_name && data.user_name != "null"){
						user_name = data.user_name;
					}
               		context = context
               		+"<tr>"
                   +" <td class='table_td'><span class='blue'>操作人</span></td>"
                  + " <td colspan='3'>"+user_name+"</td>"
               		+" </tr>";
               		var src_teminal = "";
					if(data.src_teminal && data.src_teminal != "null"){
						src_teminal = data.src_teminal;
					}
               		context = context
               		+"<tr>"
                   +" <td class='table_td'>终端设备</td>"
                  + " <td colspan='3'>"+src_teminal+"</td>"
               		+" </tr>";
               		var op_time = "";
					if(data.op_time && data.op_time != "null"){
						op_time = data.op_time;
					}
               		context = context
               		+"<tr>"
                   +" <td class='table_td'><span class='blue'>操作时间</span></td>"
                  + " <td colspan='3'>"+op_time+"</td>"
               		+" </tr>";
               		var longitude = "";
					if(data.longitude && data.longitude != "null"){
						longitude = data.longitude;
					}
               		context = context
               		+"<tr>"
                   +" <td class='table_td'>经度</td>"
                  + " <td colspan='3'>"+longitude+"</td>"
               		+" </tr>";
               		var latitude = "";
					if(data.latitude && data.latitude != "null"){
						latitude = data.latitude;
					}
               		context = context
               		+"<tr>"
                   +" <td class='table_td'>纬度</td>"
                  + " <td colspan='3'>"+latitude+"</td>"
               		+" </tr>";
               		var record_type = "";
					if(data.record_type && data.record_type != "null"){
						if(data.record_type == 0){
							record_type = "业务调用";
						}else if(data.record_type == 1){
							record_type = "系统强制";
						}else if(data.record_type == 2){
							record_type = "其他";
						}
					}
               		context = context
               		+"<tr>"
                   +" <td class='table_td'>记录类型</td>"
                  + " <td colspan='3'>"+record_type+"</td>"
               		+" </tr>";
					}
              		$("#flattening_service_right").html(context);
				},"json");
			}
			
			function getDateFommat(date){
				return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate() + " " + date.getHours() + ":" + date.getMinutes() + ":"+date.getSeconds();
			}
			
			function getMaintenanceByMonth(){
			$("#gisDivContent").hide();
			$("#Latest_Month").addClass("selected");
			$("#Latest_Year").removeClass("selected");
			$("#All_Time").removeClass("selected");
				var resourceType = $("input[name='rootCurrentEntityType']").val();
				var resourceId = $("input[name='rootCurrentEntityId']").val();
				var date = new Date();
				var sTime = new Date(date.setMonth(date.getMonth()-1));
				var eTime = new Date();
				sTime = getDateFommat(sTime);
				eTime = getDateFommat(eTime);
				var date1 = new Date(date.setFullYear(date.getFullYear() - 1));
				var params = {resourceId:resourceId,resourceType:resourceType,sTime:sTime,eTime:eTime};
				getNetworkResourceMaintenanceUl(params,"loadNetworkResourceMaintenanceAction");
			}
			
			
			function getMaintenanceByYear(){
			$("#gisDivContent").hide();
			$("#Latest_Month").removeClass("selected");
			$("#Latest_Year").addClass("selected");
			$("#All_Time").removeClass("selected");
				var resourceType = $("input[name='rootCurrentEntityType']").val();
				var resourceId = $("input[name='rootCurrentEntityId']").val();
				var date = new Date();
				var sTime = new Date(date.setFullYear(date.getFullYear() - 1));
				var eTime = new Date();
				sTime = getDateFommat(sTime);
				eTime = getDateFommat(eTime);
				var params = {resourceId:resourceId,resourceType:resourceType,sTime:sTime,eTime:eTime};
				getNetworkResourceMaintenanceUl(params,"loadNetworkResourceMaintenanceAction");
			}

//选项卡 1.切换条id 2.切换条标签类型 3.切换事件
function tab(me,aetgName) {
	if($(me).parent().find($(".list_ul2")).css("display") == "block"){
		$(me).removeClass("open");
		$(me).parent().find($(".list_ul2")).slideUp("fast");
	}else{
		$("#gisDivContent").show();
	//$("div[class='flattening_loading']").show();
	//var aetgName = $(me).attr("id");
	$("div[class='detailContent']").html("");
	$("div[class='detailContent']").hide();
	var params = {aetgName:aetgName};
	var currentEntityType = $("#rootCurrentEntityType").val();
	var currentEntityId = $("#rootCurrentEntityId").val();
	if (aetgName == "noAetg") {
		params = {aetgName:aetgName, currentEntityType:currentEntityType};
	}
	var isFinished = false;
	var url = "getEntityChineseForFlattenInfoAction";
	var dataCount = 1;
	if ($(me).attr("name") != "hasLoad") {
		$.post(url, params, function (data) {
			if (data != "" && data != null) {
				dataCount = data.length;
				var content = "";
				$.each(data, function (index, value) {
					var aetg = value.aetgName;
					var types = value.resultList;
					$.each(types, function (index, value) {
						if (dataCount == 1) {
								content += "<li  id='" + value.type + "'  onclick=\"javascript:showCurrentEntity('" + currentEntityId + "','" + currentEntityType + "','" + value.type + "',this);clicklist_ul2_li(this);\">" + value.chineseType + "(<em class='red'>..</em>)</li>";
						} else {
							content += "<li  id='" + value.type + "'  onclick=\"javascript:showCurrentEntity('" + currentEntityId + "','" + currentEntityType + "','" + value.type + "',this);clicklist_ul2_li(this);\">" + value.chineseType + "(<em class='red'>..</em>)</li>";
						}
					});
					//if (data.length > 1) {
						//$("span[class='list_text'][id='" + aetg + "']").hide();
						//$("span[class='list_text'][id='" + aetg + "']").html(content);
					//	$(me).next().html(content);
					//} else {
						//content = "<li class='clearfix' id='"+aetg+"'  >" + content + "</li>";
						
					//}
				});
				$(me).next().html(content);
				isFinished = true;
			}
		}, "json");
		var addCountInterval = setInterval(function () {
			if (isFinished) {
				if (dataCount == 1) {
					var liSize=$(me).next().children().size();
					var liCurrentCount=0;
					$("#liSize").val(liSize);					
					$(me).next().children().each(function () {
						var obj = $(this);
						var searchType = obj.attr("id");
						var parentEntityType = currentEntityType;
						var parentEntityId = currentEntityId;
						var params = {parentEntityType:parentEntityType, parentEntityId:parentEntityId, searchType:searchType};
						$.ajax({
							url:'getChildEntityByTypeUniversalRecursionForFlatternInfoAction',
							data:params,
							dataType:'json',
							type:'post',
							async:true,
							success:function(data){
								$("#liCurrentCount").val(++liCurrentCount);
								//if (data != "0") {
									obj.children("em").html(data);
								//} else {
								//	obj.remove();
								//}
								
							}
						})
					});
					var showInterval = setInterval(function(){
						var liSize = $("#liSize").val();
						var liCurrentCount = $("#liCurrentCount").val();
						if(liSize==liCurrentCount){
							//$("ul[id='" + aetgName + "']").show();
							//$("div[class='flattening_loading']").hide();
							clearInterval(showInterval);
						}
					},3)
					
				} else {
					var liSize=0;
					var liCurrentCount=0;
					$(me).next().children().each(function () {
						var obj = $(this).children().eq(1).children();
						obj.each(function () {
							liSize++;
						});
					});
					$("#liSize").val(liSize);
					
					$(me).next().children().each(function () {
						var obj = $(this);
						var span = $(this).children().eq(1);
						obj.each(function () {
							var curObj = $(this);
							var searchType = curObj.attr("id");
							var parentEntityType = currentEntityType;
							var parentEntityId = currentEntityId;
							var params = {parentEntityType:parentEntityType, parentEntityId:parentEntityId, searchType:searchType};
							$.ajax({
								url:'getChildEntityByTypeUniversalRecursionForFlatternInfoAction',
								data:params,
								dataType:'json',
								type:'post',
								async:true,
								success:function(data){
									$("#liCurrentCount").val(++liCurrentCount);
									//if (data != "0") {
										curObj.children("em").html(data);
									//} else {
									//	curObj.remove();
									//}
									
								}
							})
						});
					});
					var showInterval = setInterval(function(){
						var liSize = $("#liSize").val();
						var liCurrentCount = $("#liCurrentCount").val();
						if(liSize==liCurrentCount){
							//$("ul[id='" + aetgName + "']").show();
							//$("div[class='flattening_loading']").hide();
							clearInterval(showInterval);
						}
					},3)
					
				}
				isFinished = false;
		//跳出判断是否加载类型完毕的循环
				clearInterval(addCountInterval);
			}
		}, 10);
		//$(me).attr("name", "hasLoad");
		//alert($(me).parent().find($(".list_ul2")).css("display"));
		$("#container_left .nav_ul span").removeClass("open");
		$("#container_left .nav_ul span").parent().find($(".list_ul2")).slideUp("fast");
		$(me).addClass("open");
		$(me).parent().find($(".list_ul2")).slideDown("fast");
	}else{
		$(me).next().show();
		$("div[class='flattening_loading']").hide();
		
	}
		
		
	}
	
	
}
/**
浏览资源
**/
function showCurrentEntity(curId, curType, type, aetgName, me) {
	$(".tab_content").hide();
	$("#chi_infoMap_div").show();
	$("#info_tab_ul").html("");
	$("#chi_infoMap_div").html("");
	//$("#source_list_infoMap_div").show();
	$("#source_list_infoMap_div").show();
	$("#chi_ul_div").show();
	//$("#chi_ul").html("<div class='flattening_loading'><em>数据载入中</em><p></p></div>");
	$("#chi_ul").html("<div class='flattening_loading'  style='text-align:center'><img src='image/loading_img.gif'><br>数据处理中，请稍侯...</div>");
	
	var htmlContent = $(me).html();
	var currentEntityId = curId;
	var currentEntityType = curType;
	var searchType = type;
	var searchMsg = htmlContent;
	var areaId = $("#areaId").val();
	var params = {currentEntityId:currentEntityId, currentEntityType:currentEntityType, searchType:searchType, searchMsg:searchMsg, areaId:areaId};
	$.post("../physicalres/getResourceListForSrcAction", params, function (data) {
		//alert(data);
		$("#chi_ul").html(data);
		
		$("#modelType").val("view");
		
		//$("ul[id='" + aetgName + "']").hide();
	});
}

function isNumberOr_Letter( s ) { 
	//判断是否是数字或字母
	var regu = "^[0-9a-zA-Z\_]+$"; var re = new RegExp(regu); 
	if (re.test(s)) 
	{ 
		return true; 
	} 
	else 
	{ 
		return false; 
	} 
	}; 
//递归搜索	
function  searchResouceRecursion(chosenTypeName,chosenType,areaId,searchConditionText){
	$("#rd").empty();
	var url="searchResourceForChoosenResourceAction";
	var params="";
	var searchConditionType="";
	if(isNumberOr_Letter(searchConditionText)){
		searchConditionType="label";
	}else{
		searchConditionType="name";
	}
	var typeName = chosenTypeName;
	if(chosenType.indexOf("_")>=0){
		if(chosenType.indexOf("BaseStation")>=0){
			chosenType='GeneralBaseStation';
			var selectRangeResChosenType=chosenType;
			params={selectRangeResChosenType:selectRangeResChosenType,areaId:areaId,searchConditionText:searchConditionText,searchConditionType:searchConditionType}
			$.post(url,params,function(data){
				if(data!=null&&data!=""){
					var content="";
					$.each(data,function(index,value){
						if(value.name!=undefined){
							var stringObj=value.name;    
		          		    var newstr=stringObj.replace(searchConditionText,"<em style='color:red'>"+searchConditionText+"</em>"); 
							content +="<li><span class='aetg_text' onclick='chooseResourceForChildResource(this)'>"+newstr+"</span><input type='hidden' value='"+value.name+"'/><input type='hidden' value='selectRangeResChosenType'/><input type='hidden' value='"+value.id+"'/><input type='hidden' value='"+value._entityType+"'/><span class='aetg_link'><a  href=\"javascript:showBasicResourceInfo('"+value.id+"','"+value._entityType+"')\">基本信息</a><a href='getPhysicalresForOperaAction?currentEntityType="+value._entityType+"&currentEntityId="+value.id+"&areaId="+areaId+"&modelType=view' target='_blank'>详细</a></span></li>";
						}else{
							var stringObj=value.label;    
		          		    var newstr=stringObj.replace(searchConditionText,"<em style='color:red'>"+searchConditionText+"</em>"); 
							content +="<li><span class='aetg_text' onclick='chooseResourceForChildResource(this)'>"+newstr+"</span><input type='hidden' value='"+value.label+"'/><input type='hidden' value='selectRangeResChosenType'/><input type='hidden' value='"+value.id+"'/><input type='hidden' value='"+value._entityType+"'/><span class='aetg_link'><a  href=\"javascript:showBasicResourceInfo('"+value.id+"','"+value._entityType+"')\">基本信息</a><a href='getPhysicalresForOperaAction?currentEntityType="+value._entityType+"&currentEntityId="+value.id+"&areaId="+areaId+"&modelType=view' target='_blank'>详细</a></span></li>";
						}
					})
						$("#rds").html(content);
						$("#rds").parent().show();	
				}else{
				
						$("#rds").html("<li>无此查询条件资源</li>");
						$("#rds").parent().show();
				}
			},'json');
		}else{
			chosenType = chosenType.split("_");
			var indexFlag=0;
			for(var type in chosenType){
				
				var selectResChosenType=chosenType[type]+"_parent";
				params={selectResChosenType:selectResChosenType,areaId:areaId,searchConditionText:searchConditionText,searchConditionType:searchConditionType}
				$.post(url,params,function(data){
					if(data!=null&&data!=""){
						var content="";
						$.each(data,function(index,value){
							if(value.name!=undefined){
								var stringObj=value.name;    
			          		    var newstr=stringObj.replace(searchConditionText,"<em style='color:red'>"+searchConditionText+"</em>"); 
								content +="<li><span class='aetg_text' onclick='chooseResourceForChildResource(this)'>"+newstr+"</span><input type='hidden' value='"+value.name+"'/><input type='hidden' value='selectRangeResChosenType'/><input type='hidden' value='"+value.id+"'/><input type='hidden' value='"+value._entityType+"'/><span class='aetg_link'><a  href=\"javascript:showBasicResourceInfo('"+value.id+"','"+value._entityType+"')\">基本信息</a><a href='getPhysicalresForOperaAction?currentEntityType="+value._entityType+"&currentEntityId="+value.id+"&areaId="+areaId+"&modelType=view' target='_blank'>详细</a></span></li>";
							}else{
								var stringObj=value.label;    
			          		    var newstr=stringObj.replace(searchConditionText,"<em style='color:red'>"+searchConditionText+"</em>"); 
								content +="<li><span class='aetg_text' onclick='chooseResourceForChildResource(this)'>"+newstr+"</span><input type='hidden' value='"+value.label+"'/><input type='hidden' value='selectRangeResChosenType'/><input type='hidden' value='"+value.id+"'/><input type='hidden' value='"+value._entityType+"'/><span class='aetg_link'><a  href=\"javascript:showBasicResourceInfo('"+value.id+"','"+value._entityType+"')\">基本信息</a><a href='getPhysicalresForOperaAction?currentEntityType="+value._entityType+"&currentEntityId="+value.id+"&areaId="+areaId+"&modelType=view' target='_blank'>详细</a></span></li>";
							}
						})
							$("#rds").append(content);
							//$("#rd").parent().show();	
					}else{
					
							//$("#rd").html("<li>无此查询条件资源</li>");
							$("#rds").parent().show();
					}
					++indexFlag;
				},'json');
			}
			var showInterval = setInterval(function(){
				if(indexFlag==chosenType.length){
					if($("#rds").children().size()==0){
						$("#rds").html("<li>无此查询条件资源</li>");
						clearInterval(showInterval);
					}else{
						clearInterval(showInterval);
					}
				}
			},2);
			
			$("#rds").parent().show();
		}
	}else{
		var selectResChosenType=chosenType+"_parent";
		params={selectResChosenType:selectResChosenType,areaId:areaId,searchConditionText:searchConditionText,searchConditionType:searchConditionType}
		$.post(url,params,function(data){
			if(data!=null&&data!=""){
				var content="";
				$.each(data,function(index,value){
					if(value.name!=undefined){
						var stringObj=value.name;    
	          		    var newstr=stringObj.replace(searchConditionText,"<em style='color:red'>"+searchConditionText+"</em>"); 
						content +="<li><span class='aetg_text' onclick='chooseResourceForChildResource(this)'>"+newstr+"</span><input type='hidden' value='"+value.name+"'/><input type='hidden' value='selectRangeResChosenType'/><input type='hidden' value='"+value.id+"'/><input type='hidden' value='"+value._entityType+"'/><span class='aetg_link'><a  href=\"javascript:showBasicResourceInfo('"+value.id+"','"+value._entityType+"')\">基本信息</a><a href='getPhysicalresForOperaAction?currentEntityType="+value._entityType+"&currentEntityId="+value.id+"&areaId="+areaId+"&modelType=view' target='_blank'>详细</a></span></li>";
					}else{
						var stringObj=value.label;    
	          		    var newstr=stringObj.replace(searchConditionText,"<em style='color:red'>"+searchConditionText+"</em>"); 
						content +="<li><span class='aetg_text' onclick='chooseResourceForChildResource(this)'>"+newstr+"</span><input type='hidden' value='"+value.label+"'/><input type='hidden' value='selectRangeResChosenType'/><input type='hidden' value='"+value.id+"'/><input type='hidden' value='"+value._entityType+"'/><span class='aetg_link'><a  href=\"javascript:showBasicResourceInfo('"+value.id+"','"+value._entityType+"')\">基本信息</a><a href='getPhysicalresForOperaAction?currentEntityType="+value._entityType+"&currentEntityId="+value.id+"&areaId="+areaId+"&modelType=view' target='_blank'>详细</a></span></li>";
					}
				})
				
					$("#rds").html(content);
					$("#rds").parent().show();
	
				
			}else{
			
					$("#rds").html("<li>无此查询条件资源</li>");
					$("#rds").parent().show();
			}
		},'json');
	}
	
}

//选择查询出来的资源
function chooseResourceForChildResource(me){	
		//记录选择的起点资源信息
		var currentEntityName=$(me).next().val();
		var currentEntityId = $(me).next().next().next().val();
		var currentEntityType = $(me).next().next().next().next().val();
		$("#txtParentRes").val(currentEntityName);
		$(".txtParentRes").html(currentEntityName);
		//保存新选择的隶属资源的id和type
		$("#newParentResEntityId").val(currentEntityId);
		$("#newParentResEntityType").val(currentEntityType);
		$("#rd").parent().slideUp('fast');
		$("#chooseResDiv").slideUp('fast');

		

}
//显示资源基本信息
function showBasicResourceInfo(id,type){
	params={"currentEntityType":type,"currentEntityId":id,"loadBasicPage":"loadBasicPage"};
	$.post("../physicalres/getPhysicalresAction",params,function(data){
		$(".dialog_content").html(data);
		$("#dialog").show();
		$(".dialog_black").show();
	});
}
//递归搜索	
function  searchResouceRecursionForLogicalres(chosenTypeName,chosenType,areaId,searchConditionText){
	$("#rd").empty();
	var url="searchResourceForChoosenResourceAction";
	var params="";
	var searchConditionType="";
	if(isNumberOr_Letter(searchConditionText)){
		searchConditionType="label";
	}else{
		searchConditionType="name";
	}
	var typeName = chosenTypeName;
	if(chosenType.indexOf(",")>=0){
		if(chosenType.indexOf("BaseStation")>=0){
			chosenType='GeneralBaseStation';
			var selectRangeResChosenType=chosenType;
			params={selectRangeResChosenType:selectRangeResChosenType,areaId:areaId,searchConditionText:searchConditionText,searchConditionType:searchConditionType}
			$.post(url,params,function(data){
				if(data!=null&&data!=""){
					var content="";
					$.each(data,function(index,value){
						if(value.name!=undefined){
							var stringObj=value.name;    
		          		    var newstr=stringObj.replace(searchConditionText,"<em style='color:red'>"+searchConditionText+"</em>"); 
							content +="<li><span class='aetg_text' onclick='chooseResourceForChildResourceForLogicalres(this)'>"+newstr+"</span><input type='hidden' value='"+value.name+"'/><input type='hidden' value='selectRangeResChosenType'/><input type='hidden' value='"+value.id+"'/><input type='hidden' value='"+value._entityType+"'/><span class='aetg_link'><a  href=\"javascript:showBasicResourceInfo('"+value.id+"','"+value._entityType+"')\">基本信息</a><a href='../physicalres/getPhysicalresForOperaAction?currentEntityType="+value._entityType+"&currentEntityId="+value.id+"&areaId="+areaId+"&modelType=view' target='_blank'>详细</a></span></li>";
						}else{
							var stringObj=value.label;    
		          		    var newstr=stringObj.replace(searchConditionText,"<em style='color:red'>"+searchConditionText+"</em>"); 
							content +="<li><span class='aetg_text' onclick='chooseResourceForChildResourceForLogicalres(this)'>"+newstr+"</span><input type='hidden' value='"+value.label+"'/><input type='hidden' value='selectRangeResChosenType'/><input type='hidden' value='"+value.id+"'/><input type='hidden' value='"+value._entityType+"'/><span class='aetg_link'><a  href=\"javascript:showBasicResourceInfo('"+value.id+"','"+value._entityType+"')\">基本信息</a><a href='../physicalres/getPhysicalresForOperaAction?currentEntityType="+value._entityType+"&currentEntityId="+value.id+"&areaId="+areaId+"&modelType=view' target='_blank'>详细</a></span></li>";
						}
					})
						$("#rd").html(content);
						$("#rd").parent().show();	
				}else{
				
						$("#rd").html("<li>无此查询条件资源</li>");
						$("#rd").parent().show();
				}
			},'json');
		}else{
			chosenType = chosenType.split(",");
			var indexFlag=0;
			for(var index=0;index<chosenType.length;index++){
				var selectResChosenType=chosenType[index]+"_parent";
				params={selectResChosenType:selectResChosenType,areaId:areaId,searchConditionText:searchConditionText,searchConditionType:searchConditionType}
				$.post(url,params,function(data){
					if(data!=null&&data!=""){
						var content="";
						$.each(data,function(index,value){
							if(value.name!=undefined){
								var stringObj=value.name;    
			          		    var newstr=stringObj.replace(searchConditionText,"<em style='color:red'>"+searchConditionText+"</em>"); 
								content +="<li><span class='aetg_text' onclick='chooseResourceForChildResourceForLogicalres(this)'>"+newstr+"</span><input type='hidden' value='"+value.name+"'/><input type='hidden' value='selectRangeResChosenType'/><input type='hidden' value='"+value.id+"'/><input type='hidden' value='"+value._entityType+"'/><span class='aetg_link'><a  href=\"javascript:showBasicResourceInfo('"+value.id+"','"+value._entityType+"')\">基本信息</a><a href='../physicalres/getPhysicalresForOperaAction?currentEntityType="+value._entityType+"&currentEntityId="+value.id+"&areaId="+areaId+"&modelType=view' target='_blank'>详细</a></span></li>";
							}else{
								var stringObj=value.label;    
			          		    var newstr=stringObj.replace(searchConditionText,"<em style='color:red'>"+searchConditionText+"</em>"); 
								content +="<li><span class='aetg_text' onclick='chooseResourceForChildResourceForLogicalres(this)'>"+newstr+"</span><input type='hidden' value='"+value.label+"'/><input type='hidden' value='selectRangeResChosenType'/><input type='hidden' value='"+value.id+"'/><input type='hidden' value='"+value._entityType+"'/><span class='aetg_link'><a  href=\"javascript:showBasicResourceInfo('"+value.id+"','"+value._entityType+"')\">基本信息</a><a href='../physicalres/getPhysicalresForOperaAction?currentEntityType="+value._entityType+"&currentEntityId="+value.id+"&areaId="+areaId+"&modelType=view' target='_blank'>详细</a></span></li>";
							}
						})
							$("#rd").append(content);
							//$("#rd").parent().show();	
					}else{
					
							//$("#rd").html("<li>无此查询条件资源</li>");
							$("#rd").parent().show();
					}
					++indexFlag;
				},'json');
			}
			var showInterval = setInterval(function(){
				if(indexFlag==chosenType.length){
					if($("#rd").children().size()==0){
						$("#rd").html("<li>无此查询条件资源</li>");
						clearInterval(showInterval);
					}else{
						clearInterval(showInterval);
					}
				}
			},4);
			
			$("#rd").parent().show();
		}
	}else{
		var selectResChosenType=chosenType+"_parent";
		params={selectResChosenType:selectResChosenType,areaId:areaId,searchConditionText:searchConditionText,searchConditionType:searchConditionType}
		$.post(url,params,function(data){
			if(data!=null&&data!=""){
				var content="";
				$.each(data,function(index,value){
					if(value.name!=undefined){
						var stringObj=value.name;    
	          		    var newstr=stringObj.replace(searchConditionText,"<em style='color:red'>"+searchConditionText+"</em>"); 
						content +="<li><span class='aetg_text' onclick='chooseResourceForChildResourceForLogicalres(this)'>"+newstr+"</span><input type='hidden' value='"+value.name+"'/><input type='hidden' value='selectRangeResChosenType'/><input type='hidden' value='"+value.id+"'/><input type='hidden' value='"+value._entityType+"'/><span class='aetg_link'><a  href=\"javascript:showBasicResourceInfo('"+value.id+"','"+value._entityType+"')\">基本信息</a><a href='../physicalres/getPhysicalresForOperaAction?currentEntityType="+value._entityType+"&currentEntityId="+value.id+"&areaId="+areaId+"&modelType=view' target='_blank'>详细</a></span></li>";
					}else{
						var stringObj=value.label;    
	          		    var newstr=stringObj.replace(searchConditionText,"<em style='color:red'>"+searchConditionText+"</em>"); 
						content +="<li><span class='aetg_text' onclick='chooseResourceForChildResourceForLogicalres(this)'>"+newstr+"</span><input type='hidden' value='"+value.label+"'/><input type='hidden' value='selectRangeResChosenType'/><input type='hidden' value='"+value.id+"'/><input type='hidden' value='"+value._entityType+"'/><span class='aetg_link'><a  href=\"javascript:showBasicResourceInfo('"+value.id+"','"+value._entityType+"')\">基本信息</a><a href='../physicalres/getPhysicalresForOperaAction?currentEntityType="+value._entityType+"&currentEntityId="+value.id+"&areaId="+areaId+"&modelType=view' target='_blank'>详细</a></span></li>";
					}
				})
				
					$("#rd").html(content);
					$("#rd").parent().show();
	
				
			}else{
			
					$("#rd").html("<li>无此查询条件资源</li>");
					$("#rd").parent().show();
			}
		},'json');
	}
	
}

//选择查询出来的资源
function chooseResourceForChildResourceForLogicalres(me){	
		//记录选择的起点资源信息
		var entityName=$(me).next().val();
		var entityId = $(me).next().next().next().val();
		var entityType = $(me).next().next().next().next().val();
		
		if(entityName != ""&& entityType != "" && entityId != ""){
		 	var type = $('#hiddenInputId').val();
		 	if($("#"+type).next().next().next().val() != ""){
			 	delType = delType + $("#"+type).next().next().next().val()+",";
		 	}
		 	if($("#"+type).next().next().next().next().val() != ""){
				delId = delId + $("#"+type).next().next().next().next().val()+",";
		 	}
			$("#"+type).val(entityName);
			$("#"+type).next().next().next().val(entityType);
			$("#"+type).next().next().next().next().val(entityId);
			createType = createType + entityType+",";
			createId = createId + entityId+",";
			//alert(createId+createType);
			entityId = "";
			entityType = "";
			entityName = "";
		}
		$("#divDisplay").hide();
		//获取被添加物理资源类型的父entity类型
		$("#logicalresChosenParentEntityType").val("");
		$("#logicalresChosenParentEntityId").val("");
		//获取被添加物理资源类型的父entity类型
		$("#logicalresAddedResParentEntityType").val("");
		$("#logicalresAddedResParentEntityId").val("");
		//获取被添加物理资源类型
		$("#logicalresAddedResEntityType").val("");
		$("div[class='aetg']").hide();
		

}





/**
查询
**/
function getOrderList(orderType,getType){
	$("#gisDivContent").hide();
	var params;
	//var resourceId = "156163547291"
	var resourceId = $("input[name='rootCurrentEntityId']").val();
	var resourceType = $("input[name='rootCurrentEntityType']").val();
	var searchCondition = $("#"+orderType+"SearchCondition").val();
	if(getType=="search"){
		if(orderType=="workOrder"){
			if($("#currentWorkOrder").attr("checked")=="checked"){
				if($("#historyWorkOrder").attr("checked")=="checked"){
					params={resourceType:resourceType,resourceId:resourceId,orderType:orderType,searchCondition:searchCondition};
				}else{
					params={resourceType:resourceType,resourceId:resourceId,orderType:orderType,searchCondition:searchCondition,status:"current"};
				}
			}else{
				if($("#historyWorkOrder").attr("checked")=="checked"){
					params={resourceType:resourceType,resourceId:resourceId,orderType:orderType,searchCondition:searchCondition,status:"history"};
				}
			}
		}else{
			if($("#currentTaskOrder").attr("checked")=="checked"){
				if($("#historyTaskOrder").attr("checked")=="checked"){
					params={resourceType:resourceType,resourceId:resourceId,orderType:orderType,searchCondition:searchCondition};
				}else{
					params={resourceType:resourceType,resourceId:resourceId,orderType:orderType,searchCondition:searchCondition,status:"current"};			
				}
			}else{
				if($("#historyTaskOrder").attr("checked")=="checked"){
					params={resourceType:resourceType,resourceId:resourceId,orderType:orderType,searchCondition:searchCondition,status:"history"};
				}
			}
		}
	}else{
		if(orderType=="workOrder"){
			if($("#currentWorkOrder").attr("checked")=="checked"){
				if($("#historyWorkOrder").attr("checked")=="checked"){
					params={resourceType:resourceType,resourceId:resourceId,orderType:orderType};
				}else{
					params={resourceType:resourceType,resourceId:resourceId,orderType:orderType,status:"current"};
				}
			}else{
				if($("#historyWorkOrder").attr("checked")=="checked"){
					params={resourceType:resourceType,resourceId:resourceId,orderType:orderType,status:"history"};
				}
			}
		}else{
			if($("#currentTaskOrder").attr("checked")=="checked"){
				if($("#historyTaskOrder").attr("checked")=="checked"){
					params={resourceType:resourceType,resourceId:resourceId,orderType:orderType};
				}else{
					params={resourceType:resourceType,resourceId:resourceId,orderType:orderType,status:"current"};			
				}
			}else{
				if($("#historyTaskOrder").attr("checked")=="checked"){
					params={resourceType:resourceType,resourceId:resourceId,orderType:orderType,status:"history"};
				}
			}
		}
	}

	if(orderType=="workOrder"){
		var table = $("#urTable");
		table.html("");
		$("#"+orderType+"_paging_div").html("");
	}else{
		var table = $("#toTable");
		table.html("");
		$("#"+orderType+"_paging_div").html("");
	}
	getOrderListForAjax(params,orderType);
}

/*获取任务单 工单*/
function getOrderListForAjax(params,orderType){

	$("#"+orderType+"Loading").show();
	var opUrlApp=$("#opUrlApp").val()+"";
	//var queryUrl = "http://"+opUrlApp+"/operationservice/outterinterface/getWorkOrdersByResourceIdByStatusAction";
	var queryUrl = "getWorkOrdersByResourceIdByStatusAction";
	$.ajax({ 
	   type : "post", 
	   url : queryUrl, 
	   data :params,
	   async : false, 
	   success : function($data){ 
		    var res = $data;
			res = eval("("+res+")");
			if(res){
				var index = res.length-1;
				var totalPage = 0;
				if(index!=-1){
					totalPage = res[index].totalPage;
				}
				if(orderType=="workOrder"){
					showWorkOrderResultTable(res);
				}else{
					showTaskOrderResultTable(res);
				}
			}
			$("#"+orderType+"Loading").hide();
	     } 
	}); 
}
/**
显示工单
**/
function showWorkOrderResultTable(data){
	var workOrderList = data;
	var table = $("#urTable");
	table.html("");
	var thHtml = "<tr id='b'><th>工单号</th><th style='width:60px;'>工单类型</th><th style='width:32%'>工单主题</th><th style='width:45px;'>创建人</th><th>创建时间</th><th>要求完成时间</th><th>当前责任人</th><th>工单状态</th></tr>";
	var th = $(thHtml);
	th.appendTo(table);
    var opUrlApp=$("#opUrlApp").val()+"";                    
   	//数据为空，则清空表格
	if(!data||data.length==0){
		return ; //参数过滤
	}
	for(var i=0;i<workOrderList.length;i++){
		var wo = workOrderList[i];
		if(!wo)continue;
		var WOID = wo.WOID;
		var WOTITLE = wo.WOTITLE;
		var CREATEPERSON = wo.CREATEPERSON;
		var CREATETIME = wo.CREATETIME;
		var BIZ_OVERTIME = wo.BIZ_OVERTIME;
		var OPERATORID = wo.OPERATORID;
		var STATUSNAME = wo.STATUSNAME;
		var opUrlApp =wo.opUrlApp;
		var netWorkResourceId = "_resource_station_"+wo.netWorkResourceId;
		var locationHref="../../"+wo.locationHref+"?WOID="+WOID;	//che.yd添加的
		var isRead = wo.ISREADED;	//0:未读 1:已读 2：未读，状态改变 3：已读，状态改变
		var isQuick = wo.isQuick;
		var isOver = wo.isOver;
		var WOTYPE = wo.WOTYPE;
		
		if(!WOID)continue;
		if(!WOID){WOID="";}
		if(!WOTITLE || WOTITLE=='null'){WOTITLE="";}
		if(!CREATEPERSON || CREATEPERSON=='null'){CREATEPERSON="";}
		if(!CREATETIME || CREATETIME=='null'){CREATETIME="";}
		if(!BIZ_OVERTIME || BIZ_OVERTIME=='null'){BIZ_OVERTIME="";}
		if(!OPERATORID || OPERATORID=='null'){OPERATORID="";}
		if(!STATUSNAME || STATUSNAME=='null'){STATUSNAME="";}
		if(!netWorkResourceId){netWorkResourceId="";}
		if(!locationHref){locationHref="";}	//che.yd添加的
		if(!WOTYPE){WOTYPE="";}
		
		//Hard code 
		var layerType = "_resource_station";
		var tr = $("<tr class='urTr' ></tr>");
		var index = i+1 ;
		var vs = $("<td>"+ index +"</td>");
		var checkBox = $("<input class=\"WOCheckBox\" type=\"checkbox\" />");
		var checkBoxTd = $("<td></td>");
		checkBox.attr("value",wo.WOID);
		checkBox.appendTo(checkBoxTd);
		var WOID = $("<td><a href=\""+locationHref+"\" target=\"_blank\">"+WOID+"</a></td>");
     	var woTitle = "<td><span class=\"wotitle_span\">"+WOTITLE+"<span class='icon_span'>";
     	if(isRead){
     		if(isRead=='0'){
     			woTitle+="<em class='back_icon icon_new'>新</em>";
     		}else if(isRead=='2' || isRead=='3'){
     			woTitle+="<em class='back_icon icon_change'>变</em>";	
     		}
     	}
     	if(isOver){
     		woTitle+="<em class='back_icon icon_over'>超</em>";
     	}
     	if(isQuick){
     		woTitle+="<em class='back_icon icon_quick'>快</em>";
     	}
     	woTitle+="</span></span></td>";
		var WOTITLE = $(woTitle);	//che.yd添加
		var CREATEPERSON = $("<td>"+CREATEPERSON+"</td>");
		var CREATETIME = $("<td>"+CREATETIME+"</td>");
		var BIZ_OVERTIME = $("<td>"+BIZ_OVERTIME+"</td>");
		var OPERATORID = $("<td>"+OPERATORID+"</td>");
		var STATUSNAME = $("<td>"+STATUSNAME+"</td>");
		var WOTYPE = $("<td>"+WOTYPE+"</td>");
		

		WOID.appendTo(tr);
		WOTYPE.appendTo(tr);
		WOTITLE.appendTo(tr);
		CREATEPERSON.appendTo(tr);
		CREATETIME.appendTo(tr);
		BIZ_OVERTIME.appendTo(tr);
		OPERATORID.appendTo(tr);
		STATUSNAME.appendTo(tr);
		tr.appendTo(table);
	}
	pagingColumnByForeground("workOrder_paging_div",$(".urTr"),"10");
}

/**
显示任务单
**/
function showTaskOrderResultTable(data){
	var taskOrderList = data;
	var table = $("#toTable");
	table.html("");
	var thHtml = "<tr id='b2'><th>任务单号</th><th>任务单类型</th><th style='width:32%'>任务单主题</th><th>创建人</th><th>创建时间</th><th>要求完成时间</th><th>当前责任人</th><th>工单状态</th></tr>";
	var th = $(thHtml);
	th.appendTo(table);
	var opUrlApp=$("#opUrlApp").val()+"";
	//数据为空，则清空表格
	if(!data||data.length==0){
		return ; //参数过滤
	}
	for(var i=0;i<taskOrderList.length;i++){
		var to = taskOrderList[i];
		if(!to)continue;
		var TOID = to.TOID;
		var WOID = to.WOID;
		var TASKNAME = to.TASKNAME;
		var CREATEPERSON = to.ASSIGNEDPERSONID;	//che.yd添加的
		var CREATETIME = to.CREATETIME;
		var LASTEDITTIME = to.LASTEDITTIME;
		var OPERATORID = to.OPERATORID;
		var STATUS = to.STATUSNAME;	//che.yd添加的
		var OVERTIME=to.OVERTIME;	//che.yd添加的
		var opUrlApp = to.opUrlApp;
		var netWorkResourceId = "_resource_station_"+to.netWorkResourceId;
		var locationHref="../../"+to.locationHref+"?WOID="+WOID+"&TOID="+TOID;;	//che.yd添加的
		var isRead = to.ISREADED;	//0:未读 1:已读 2：未读，状态改变 3：已读，状态改变
		var isQuick = to.isQuick;
		var isOver = to.isOver;
		var TOTYPE = to.TOTYPE;

		if(!TOID)continue;
		
		if(!TOID){TOID=""};
		if(!TASKNAME||TASKNAME=='null'){TASKNAME=""};
		if(!CREATEPERSON||CREATEPERSON=='null'){CREATEPERSON=""};
		if(!CREATETIME||CREATETIME=='null'){CREATETIME=""};
		if(!LASTEDITTIME||LASTEDITTIME=='null'){LASTEDITTIME=""};
		if(!OPERATORID || OPERATORID=="null"){OPERATORID=""};
		if(!STATUS||STATUS=='null'){STATUS=""};
		if(!locationHref){locationHref=""};	//che.yd添加的
		if(!TOTYPE){TOTYPE="";}
		//Hard code
		var layerType = "_resource_station";
		var tr = $("<tr class='toTr'></tr>");
		var index = i+1 ;
		var vs = $("<td>"+ index +"</td>");
		var checkBox = $("<input class=\"TOCheckBox\" type=\"checkbox\" />");
		var checkBoxTd = $("<td></td>");
		checkBox.attr("value",to.TOID);
		checkBox.appendTo(checkBoxTd);
		var TOID = $("<td><a href=\""+locationHref+"\" target=\"_blank\">"+TOID+"</a></td>");
		var woTitle = "<td><span class=\"wotitle_span\">"+TASKNAME+"<span class='icon_span'>";
     	if(isRead){
     		if(isRead=='0'){
     			woTitle+="<em class='back_icon icon_new'>新</em>";
     		}else if(isRead=='2' || isRead=='3'){
     			woTitle+="<em class='back_icon icon_change'>变</em>";	
     		}
     	}
     	if(isOver){
     		woTitle+="<em class='back_icon icon_over'>超</em>";
     	}
     	if(isQuick){
     		woTitle+="<em class='back_icon icon_quick'>快</em>";
     	}
     	woTitle+="</span></span></td>";
		var TASKNAME = $(woTitle);	//che.yd添加
		//var TASKNAME = $("<td>"+"<span class=\"wotitle_span\">"+TASKNAME+"</span>"+"</td>");
		var CREATEPERSON = $("<td>"+CREATEPERSON+"</td>");
		var CREATETIME = $("<td>"+CREATETIME+"</td>");
		var OVERTIME = $("<td>"+OVERTIME+"</td>");
		var OPERATORID = $("<td>"+OPERATORID+"</td>");
		var STATUS = $("<td>"+STATUS+"</td>");
		var TOTYPE = $("<td>"+TOTYPE+"</td>");
		

		TOID.appendTo(tr);
		TOTYPE.appendTo(tr);
		TASKNAME.appendTo(tr);
		CREATEPERSON.appendTo(tr);
		CREATETIME.appendTo(tr);
		OVERTIME.appendTo(tr);
		OPERATORID.appendTo(tr);
		STATUS.appendTo(tr);
		tr.appendTo(table);
	}
	pagingColumnByForeground("taskOrder_paging_div",$(".toTr"),"10");
}
//搜索工单 任务单
function searchOrderList(orderType){
	var condition=$("#"+orderType+"SearchCondition").val();
	if(condition=="请输入查询条件"||$.trim(condition)==""){
		alert("请输入查询条件");
		return false;
	}else{
		getOrderList(orderType,"search");
	}
}


function forward(currentEntityType,currentEntityId){
            var areaId =$("#areaId").val();
			var url = "getPhysicalresForOperaAction";
			var params = {currentEntityId:currentEntityId,currentEntityType:currentEntityType,loadBigPage:"loadBigPage",areaId:areaId,showType:"showTask"}
			//AJAX加载修改物理资源页面
			$.post(url, params, function(data){
				$("#gisDivContent").html(data).show();
			});
}


/**
显示图片
*/
function showPhoto(indexFlag){
	$("li[class='photoLi']").each(function(index){
		if($(this).css("display")!="none"){
			if(indexFlag=="prev"){
				if(index!=0){
					$(this).hide();
					$(this).parent().children().eq(index-1).show();
					$("em[class='photoEm']").hide();
					$("em[class='photoEm']").each(function(i){
						if(i==index-1){
							$(this).show();
						}
					})
					$("input[class='prev']").attr("disabled",false);
					$("input[class='next']").attr("disabled",false);
					if(index - 1 <= 0){
						$("input[class='prev']").attr("disabled",true);
					}
					return false;
				}else{
					$("input[class='prev']").attr("disabled",true);
					return false;
				}
			}else{
				var de = $("li[class='photoLi']").size()-1;
				if(index!=de){
					$(this).hide();
					$(this).parent().children().eq(index+1).show();
					$("em[class='photoEm']").hide();
					$("em[class='photoEm']").each(function(i){
						if(i==index+1){
							$(this).show();
						}
					})
					$("input[class='prev']").attr("disabled",false);
					$("input[class='next']").attr("disabled",false);
					if(index + 1 == de){
						$("input[class='next']").attr("disabled",true);
					}
					return false;
				}else if(index >= de){
					$("input[class='next']").attr("disabled",true);
				}
			}
		}
	});	
}


function getOdmAndTerminalLayOutView(){
		//var currentVal = $("#currentValues").val();
		//var currentEntityId = currentVal.substring(currentVal.indexOf("#")+1,currentVal.length);//当前id
		//var currentEntityType=currentVal.substring(0,currentVal.indexOf("#"));//当前资源类型
		var currentEntityType = $("#rootCurrentEntityType").val();
		var currentEntityId = $("#rootCurrentEntityId").val();
		//currentEntityId = "75";
		//currentEntityType="FiberCrossCabinet";
		var params = {currentEntityType:currentEntityType,currentEntityId:currentEntityId};
	 	$.post("getOdmandterminallayoutAction", params, function(data){	
			if(data.maxCount!=undefined){
	 			$("#panels_view").html("无面板图");
	 			$("#panels_view").parent().parent().css("overflow","hidden");
	 		}else{
	 			$("#messages_view").html("");
	 			$("#paneltable_view").html("");
	 			$("#panels_view").parent().parent().css("overflow","auto");
				var odmcolorder =data.odmupdownorder;//模块上下顺序
				var odmroworder =data.odmleftrightorder;//模块左右顺序
				var odmrowammount =data.odmrowcount;//每排模块数
				var terminalorder =data.terminalorder;//端子左右排列顺序
				var terminalrowammount = data.terminalrowcount;//每排端子数
				var firstodmorder=false;//模块先上下还是先左右排序 true为先上下 false为先左右
				if(data.odmorderflag==1){
					firstodmorder=true;
				}
			   	$.post('getOdmAndTerminalMessageAction',params,function(data){
			   		
					var mapCount=data.mapCount;//不同端子状态数目
					var mapName = data.mapName;//端子状态名
					var odmTerminalList=data.odmTerminalList;//模块与端子信息
					$.each(mapCount,function(index,value){//端子状态
						$.each(mapName,function(i,v){
							if(index==i){
								if(i=="terminalNoUseCount"){
									$("#messages_view").append("<span style='font-size=22px'><img style='position:relative;top:-2px;vertical-align:middle' src='image/terminalNoUseCount.png'/>"+v+"("+value+")</span>&nbsp;&nbsp");
								}else if(i=="terminalUsedCount"){
									$("#messages_view").append("<span style='font-size=22px'><img style='position:relative;top:-2px;vertical-align:middle' src='image/terminalUsedCount.png'/>"+v+"("+value+")</span>&nbsp;&nbsp");
								}else if(i=="terminalPreUsedCount"){
									$("#messages_view").append("<span style='font-size=22px'><img style='position:relative;top:-2px;vertical-align:middle' src='image/terminalPreUsedCount.png'/>"+v+"("+value+")</span>&nbsp;&nbsp");
								}
								else if(i=="terminalBreakCount"){
									$("#messages_view").append("<span style='font-size=22px'><img style='position:relative;top:-2px;vertical-align:middle' src='image/terminalBreakCount.png'/>"+v+"("+value+")</span>&nbsp;&nbsp");
								}else if(i=="terminalOtherCount"){
									$("#messages_view").append("<span style='font-size=22px'><img style='position:relative;top:-2px;vertical-align:middle' src='image/terminalOtherCount.png'/>"+v+"("+value+")</span>&nbsp;&nbsp");
								}
								
							}
						})	
					})
					//checkbox控制端子名称显示与隐藏
					$("#messages_view").append("<span><input type='checkbox' checked='checked' onclick='showTerminalNameView(this);'>显示端子名称</span>");
					//根据每排模块数增加水平持平的div
					for(var i=0;i<parseInt(odmrowammount);i++){
						if(odmroworder=="lefttoright"){//模块左右排列
							
								
								if(parseInt(odmrowammount)==1){
									$("#paneltable_view").append("<div id='panelsdiv_view"+i+"' style='vertical-align: top;display:inline-block;margin-left:7px; margin-top:3px;border: 1px solid #CCCCCC;box-shadow: 1px 1px 3px #000000; padding: 2px;' ></div>");
								}else{
									$("#paneltable_view").append("<div id='panelsdiv_view"+i+"' style='vertical-align: top;display:inline-block;margin-left:7px; margin-top:3px;border: 1px solid #CCCCCC;box-shadow: 1px 1px 3px #000000; padding: 2px;'></div>");
								}
						}else{
								if(parseInt(odmrowammount)==1){
									$("#paneltable_view").prepend("<div id='panelsdiv_view"+i+"' style='vertical-align: top;display:inline-block;margin-left:7px; margin-top:3px;border: 1px solid #CCCCCC;box-shadow: 1px 1px 3px #000000; padding: 2px;' ></div>");
								}else{
									$("#paneltable_view").prepend("<div id='panelsdiv_view"+i+"' style='vertical-align: top;display:inline-block;margin-left:7px; margin-top:3px;border: 1px solid #CCCCCC;box-shadow: 1px 1px 3px #000000; padding: 2px;'></div>");
								}
								
							
						}
						
						
					}
					//模块数
					var listLength= odmTerminalList.length;
					var odmcolammount= Math.ceil(listLength/parseInt(odmrowammount));//每列模块数
					$.each(odmTerminalList,function(index,value){//模块端子排列
						var terminalCount = value.terminalCount;//端子数量
						var odmName = value.name;//模块名
						var terminalList = value.terminalList;//端子
						var divIndex=0;//控制append模块的div
						if(firstodmorder==true){//模块先左右还是上下排列
							divIndex=parseInt((index)/odmcolammount);
						}else{
							divIndex=parseInt((index)%parseInt(odmrowammount));
						}
						var rowsCount=Math.ceil(parseInt(terminalCount)/parseInt(terminalrowammount));//一个模块端子的排数
						if(rowsCount==0){
							rowsCount=1;
						}
						if(odmcolorder=="uptodown"){//模块上下排列控制
								$("#panelsdiv_view"+divIndex).append("<table id='"+odmName+"table_view' class='main-table1 panel tc' style='width:50%'></table>");
						}else{
								$("#panelsdiv_view"+divIndex).prepend("<table id='"+odmName+"table_view' class='main-table1 panel tc' style='width:50%'></table>");
						}
							
						for(var j=0;j<rowsCount;j++){//根据一个模块端子排数增加table tr
								$("#"+odmName+"table_view").append("<tr id='"+odmName+j+"_view'></tr>")
						}
						if(terminalList!=null&&terminalList!=""&&terminalList!="[]"){//端子不为空
							
							$.each(terminalList,function(i,v){
								
								trIndex = parseInt(i/parseInt(terminalrowammount)); //端子增加到的tr index
								if(terminalorder=="lefttoright"){//端子左右排列控制
									if(v.status=="空闲"){
										$("#"+odmName+trIndex+"_view").append("<td><span onclick='showTerminalMess("+v.id+");' class='effect-style effect-img1'><em id='showTerminalView'>"+v.name+"</em></span></td>");
									}else if(v.status=="占用"){
										$("#"+odmName+trIndex+"_view").append("<td><span  onclick='showTerminalMess("+v.id+");' class='effect-style effect-img4'><em id='showTerminalView'>"+v.name+"</em></span></td>");
									}else if(v.status=="预占用"){
										$("#"+odmName+trIndex+"_view").append("<td><span  onclick='showTerminalMess("+v.id+");' class='effect-style effect-img5'><em id='showTerminalView'>"+v.name+"</em></span></td>");
									}else if(v.status=="故障/损坏"){
										$("#"+odmName+trIndex+"_view").append("<td><span  onclick='showTerminalMess("+v.id+");' class='effect-style effect-img6'><em id='showTerminalView'>"+v.name+"</em></span></td>");
									} else if(v.status=="其他"){
										$("#"+odmName+trIndex+"_view").append("<td><span  onclick='showTerminalMess("+v.id+");' class='effect-style effect-img9'><em id='showTerminalView'>"+v.name+"</em></span></td>");
									}
								}else{
									if(v.status=="空闲"){
										$("#"+odmName+trIndex+"_view").prepend("<td><span  onclick='showTerminalMess("+v.id+");' class='effect-style effect-img1'><em id='showTerminalView'>"+v.name+"</em></span></td>");
									}else if(v.status=="占用"){
										$("#"+odmName+trIndex+"_view").prepend("<td><span  onclick='showTerminalMess("+v.id+");' class='effect-style effect-img4'><em id='showTerminalView'>"+v.name+"</em></span></td>");
									}else if(v.status=="预占用"){
										$("#"+odmName+trIndex+"_view").prepend("<td><span  onclick='showTerminalMess("+v.id+");' class='effect-style effect-img5'><em id='showTerminalView'>"+v.name+"</em></span></td>");
									}else if(v.status=="故障/损坏"){
										$("#"+odmName+trIndex+"_view").prepend("<td><span  onclick='showTerminalMess("+v.id+");' class='effect-style effect-img6'><em id='showTerminalView'>"+v.name+"</em></span></td>");
									}else if(v.status=="其他"){
										$("#"+odmName+trIndex+"_view").prepend("<td><span  onclick='showTerminalMess("+v.id+");' class='effect-style effect-img9'><em id='showTerminalView'>"+v.name+"</em></span></td>");
									}
								}	
							})
							if(odmName=="noHasOdm"){//模块名增加
								$("#"+odmName+"0_view").append("<td rowspan='"+rowsCount+"'></td>");
							}else{
								$("#"+odmName+"0_view").append("<td rowspan='"+rowsCount+"'>"+odmName+"</td>");
							}
						}	
					})
					
				},'json')
		 	}
		},"json");
}

function showTerminalNameView(id){//显示端子名称是否
	if($(id).attr("checked")=="checked"){
		$("em[id='showTerminalView']").css("display","");
	}else{
		$("em[id='showTerminalView']").css("display","none");
	}
}


function showTerminalMess(terminalId){//端子维护
	var url = "getPhysicalresForOperaAction";
	var params = {currentEntityId:terminalId,currentEntityType:"Terminal",loadBigPage:"loadBigPage"}
	$.post(url, params, function(data){
			$("#terminalmess").html(data);
			$("#editDiv").empty();
			$("#editDiv").html("<input  type='button' value='保存' onclick='updateTerminal();'/>&nbsp;&nbsp;<input type='button' value='取消' onclick='cancelOpera();' />")
	});
				
}


