

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


function getAetgCountMess(me){
	var aetgName = $(me).attr("id");
	$("div[class='flattening_loading']").show();
	$("."+aetgName).hide();
	var currentEntityType = $("input[name='rootCurrentEntityType']").val();
	var currentEntityId = $("input[name='rootCurrentEntityId']").val();
	var params = {aetgName:aetgName, currentEntityType:currentEntityType,currentEntityId:currentEntityId};
	var url = "getAetgCountMessForFlattenInfoAction";
	$.ajax({
		url:url,
		data:params,
		dataType:'json',
		type:'post',
		async:false,
		success:function(data){
			if (data != "" && data != null) {
				$.each(data,function(index,value){
					var scontent=$("span[id='"+value.type+"']").html()+"";
					if(scontent.indexOf("(")>=0){
						scontent = scontent.substring(0,scontent.lastIndexOf("("));
					}
					$("span[id='"+value.type+"']").html(scontent+"("+value.count+")");
				})	
			}
		}
	})
	$("div[class='flattening_loading']").hide();
	$("."+aetgName).show();
}

/**
切换显示
**/
function tab(me,aetg,firstLoad){
var aetgName = $(me).attr("id");
if(aetg!="childAetg"){
	//$("div[class='flattening_loading']").show();
}else{
	
	if(firstLoad=="firstLoad"){
		//$("div[class='flattening_loading']").show();
		$("span[id='"+aetgName+"']").parent().parent().hide();
	}
}
$(".list_out_div").hide();
var hasLoad = $("#"+aetgName+"Content").attr("name");
if(hasLoad!="hasLoad"){
	var currentEntityType = $("input[name='rootCurrentEntityType']").val();
	var currentEntityId = $("input[name='rootCurrentEntityId']").val();
	params = {aetgName:aetgName, currentEntityType:currentEntityType,currentEntityId:currentEntityId};
	var url = "getResourcesForSrcForGisDispatchAction";
	$.post(url, params, function (data) {
		if (data != "" && data != null) {
			$(".list_out_div").show();
			$("div[class='flattening_loading']").hide();
			if(aetg=="childAetg"){
				$(".list_name").removeClass("list_name_out");
				$("span[id='"+aetgName+"']").addClass("list_name_out");
				$(".list_out_div div").hide();
				$(".list_out_div div[id='"+aetgName+"Content']").show();
				$("span[id='"+aetgName+"']").parent().parent().show();
			}else{
				$(".list_out_div div").show();
			}
			var content = "";
			var typeFlag="";
			$.each(data,function(index,value){
				if(typeFlag!=value._entityType){
					if(index==0){
						content = "<div class='ldt_container'> <div class='ldt_name'>"+value.chineseName+" :</div><div class='ldt_content'>";
					}else{
						content += "</div></div><div class='ldt_container'><div class='ldt_name'>"+value.chineseName+" :</div><div class='ldt_content'>";
					}
					typeFlag=value._entityType;
				}
				if(index==0){
					content += "<a href='#' onclick=\"forward('"+value._entityType+"','"+value.id+"',this)\" title='"+value.chineseName+":"+value.name+"' class='"+value.id+"_"+value._entityType+"' style='background:white' >"+value.name+"</a>";
					var areaId =$("#areaId").val();
					var url = "getPhysicalresForOperaAction";
					var params = {currentEntityId:value.id,currentEntityType:value._entityType,loadBigPage:"loadBigPage",areaId:areaId,showType:"showTask"}
					//AJAX加载修改物理资源页面
					$.post(url, params, function(data){
						$("#gisDivContent").html(data);
					});
				}else{
					content += "<a href='#' onclick=\"forward('"+value._entityType+"','"+value.id+"',this)\" title='"+value.chineseName+":"+value.name+"' class='"+value.id+"_"+value._entityType+"'  >"+value.name+"</a>";
				}		
				if(index==data.length-1){
					content +="</div></div>";
				}
			})
			$("#"+aetgName+"Content").html(content).attr("name","hasLoad");
			$("#noResourceDiv").hide();
		}else{
			$(".list_out_div").show();
			$("div[class='flattening_loading']").hide();
			if(aetg=="childAetg"){
				$(".list_name").removeClass("list_name_out");
				$("span[id='"+aetgName+"']").addClass("list_name_out");
				$(".list_out_div div").hide();
				$(".list_out_div div[id='"+aetgName+"Content']").show();
				$("span[id='"+aetgName+"']").parent().parent().show();
			}else{
				$(".list_out_div div").show();
			}
			$("#"+aetgName+"Content").html("无资源数据").attr("name","hasLoad");
			$("#gisDivContent").html("");
			$("#noResourceDiv").show();
		}
		
	}, "json");
}else{
	$(".list_out_div").show();
	$("div[class='flattening_loading']").hide();
	if(aetg=="childAetg"){
			$(".list_name").removeClass("list_name_out");
			$("span[id='"+aetgName+"']").addClass("list_name_out");
			$(".list_out_div div").hide();
			$(".list_out_div div[id='"+aetgName+"Content']").show();
			$("span[id='"+aetgName+"']").parent().parent().show();
	}else{
		$(".list_out_div div").show();
	}
	$("#"+aetgName+"Content .ldt_container").show();
	$("#"+aetgName+"Content .ldt_container").children().show();
	
	if($("#"+aetgName+"Content").html()!="无资源数据"){
		var aclass="";
		if(aetg=="childAetg"){
			aclass=$("#"+aetgName+"Content .ldt_content").children().eq(0).attr("class")+"";
		}else{
			aclass=$("#"+aetgName+"Content .ldt_content:first").children().eq(0).attr("class")+"";
		}
		var currentEntityId = aclass.substring(0,aclass.indexOf("_"));
		var currentEntityType = aclass.substring(aclass.indexOf("_")+1,aclass.length);
		var areaId =$("#areaId").val();
		var url = "getPhysicalresForOperaAction";
		var params = {currentEntityId:currentEntityId,currentEntityType:currentEntityType,loadBigPage:"loadBigPage",areaId:areaId,showType:"showTask"}
		//AJAX加载修改物理资源页面
		$.post(url, params, function(data){
			$("#gisDivContent").html(data);
		});
		var child = "";
		if(aetg=="childAetg"){
			child=$("#"+aetgName+"Content .ldt_content").children();
		}else{
			child=$("#"+aetgName+"Content .ldt_content:first").children();
		}
		child.each(function(i){
			if(i==0){
				$(this).click();
			}else{
				//$(this).css("backgroud","#D0FBFF");
			}	

		})
		
		$("#noResourceDiv").hide();
	}else{
		$("#gisDivContent").html("");
		$("#noResourceDiv").show();
	}
}

}



function getMaintenance(){
			$("#gisDivContent").html("");
			$("#noResourceDiv").hide();
			$("#Latest_Month").removeClass("selected");
			$("#Latest_Year").removeClass("selected");
			$("#All_Time").addClass("selected");
				var resourceType = $("input[name='rootCurrentEntityType']").val();
				var resourceId = $("input[name='rootCurrentEntityId']").val();
				var params = {resourceId:resourceId,resourceType:resourceType,sTime:"",eTime:""};
				var context = "";
				var indexFlag=0;
				var firstRecordId="";
				$.post("../maintain/loadNetworkResourceMaintenanceAction",params,function(data){
					$.each(data, function(key,value){
						if(indexFlag==0){
							if(value.op_category == '添加资源'){
								context = context 
								+"<li class='ico_5 selected serviceClist'>";
							}else if(value.op_category == '属性修改'){
								context = context 
								+"<li class='ico_4 selected serviceClist'>";
							}else if(value.op_category == "资源关联"){
								context = context 
								+"<li class='ico_1 selected serviceClist'>";
							}else if(value.op_category == "解除关联"){
								context = context 
								+"<li class='ico_2 selected serviceClist'>";
							}else if(value.op_category == "维护服务"){
								context = context 
								+"<li class='ico_3 selected serviceClist'>";
							}else if(value.op_category == "删除资源"){
								context = context 
								+"<li class='ico_6 selected serviceClist'>";
							}else{
								context = context 
								+"<li class='ico_1 selected serviceClist'>";
							}
							firstRecordId=value.id;
						}else{
							if(value.op_category == '添加资源'){
								context = context 
								+"<li class='ico_5 serviceClist'>";
							}else if(value.op_category == '属性修改'){
								context = context 
								+"<li class='ico_4 serviceClist'>";
							}else if(value.op_category == "资源关联"){
								context = context 
								+"<li class='ico_1 serviceClist'>";
							}else if(value.op_category == "解除关联"){
								context = context 
								+"<li class='ico_2 serviceClist'>";
							}else if(value.op_category == "维护服务"){
								context = context 
								+"<li class='ico_3 serviceClist'>";
							}else if(value.op_category == "删除资源"){
								context = context 
								+"<li class='ico_6 serviceClist'>";
							}else{
								context = context 
								+"<li class='ico_1 serviceClist'>";
							}
						}
						
						var op_scene = "";
						if(value.op_scene != null &&  value.op_scene != "" && value.op_scene != "null"){
							op_scene = value.op_scene+",";
						}
						var op_cause = "";
						if(value.op_cause != null &&  value.op_cause != "" && value.op_cause != "null"){
							op_cause = value.op_cause;
						}
						var content;
	               		var contents = "";
						if(value.content && value.content != "null"){
							content = value.content.split("$_$");
						}
						if(content){
							$.each(content,function(k,v){
							contents = contents + v+".";
							});
						}
						context = context
								+"<h4><span class='cz_kind_span'>"
								+"<em>"+value.op_category+"</em>，"
								+"<em class='cz_kind'>"+value.op_cause+"</em>，"
								+"<em class='cz_kind'>"+value.op_scene+"</em>，"
								+"<em class='cz_kind'>"+value.user_name+"</em></span>"
								+"<span class='fr'>"
								+"<em>"+value.op_time+"</em></span></h4><div>"
								+"<em class='fl cz_kind_em'>详情："+contents+"</em>"
								+"<em class='fr'><a href='#'  onclick='showDetailed(\""+value.id+"\",this);'>>></a></em></div>"
                       			+"</li>";
                       	indexFlag++;
					});
					if(context == ""){
						context = "没有此时间范围内的历史记录。";
						$("#flattening_service_right").html("");
						$("#som_info_ul").html(context);
						$("#servicePage").html("");
					}else{
						$("#som_info_ul").html(context);
						pagingColumnByForeground("servicePage",$(".serviceClist"),"10");
						var aHref=$("#som_info_ul").children().eq(0).children().eq(1).children().eq(1).children().eq(0);
						showDetailed(firstRecordId,aHref);
					}
				},"json");
				
			}
			
			function showDetailed(id,me){
				$(".flattening_service_ul li").removeClass("selected");
				$(me).parent().parent().parent().addClass("selected");
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
			$("#gisDivContent").html("");
			$("#noResourceDiv").hide();
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
				var context = "";
				var indexFlag=0;
				var firstRecordId="";
				$.post("../maintain/loadNetworkResourceMaintenanceAction",params,function(data){
					$.each(data, function(key,value){
						if(indexFlag==0){
							if(value.op_category == '添加资源'){
								context = context 
								+"<li class='ico_5 selected serviceClist'>";
							}else if(value.op_category == '属性修改'){
								context = context 
								+"<li class='ico_4 selected serviceClist'>";
							}else if(value.op_category == "资源关联"){
								context = context 
								+"<li class='ico_1 selected serviceClist'>";
							}else if(value.op_category == "解除关联"){
								context = context 
								+"<li class='ico_2 selected serviceClist'>";
							}else if(value.op_category == "维护服务"){
								context = context 
								+"<li class='ico_3 selected serviceClist'>";
							}else if(value.op_category == "删除资源"){
								context = context 
								+"<li class='ico_6 selected serviceClist'>";
							}else{
								context = context 
								+"<li class='ico_1 selected serviceClist'>";
							}
							firstRecordId=value.id;
						}else{
							if(value.op_category == '添加资源'){
								context = context 
								+"<li class='ico_5  serviceClist'>";
							}else if(value.op_category == '属性修改'){
								context = context 
								+"<li class='ico_4  serviceClist'>";
							}else if(value.op_category == "资源关联"){
								context = context 
								+"<li class='ico_1  serviceClist'>";
							}else if(value.op_category == "解除关联"){
								context = context 
								+"<li class='ico_2  serviceClist'>";
							}else if(value.op_category == "维护服务"){
								context = context 
								+"<li class='ico_3  serviceClist'>";
							}else if(value.op_category == "删除资源"){
								context = context 
								+"<li class='ico_6  serviceClist'>";
							}else{
								context = context 
								+"<li class='ico_1  serviceClist'>";
							}
						}
						
						var content;
	               		var contents = "";
						if(value.content && value.content != "null"){
							content = value.content.split("$_$");
						}
						if(content){
							$.each(content,function(k,v){
							contents = contents + v+".";
							});
						}
						context = context
								+"<h4><span class='cz_kind_span'>"
								+"<em>"+value.op_category+"</em>，"
								+"<em class='cz_kind'>"+value.op_cause+"</em>，"
								+"<em class='cz_kind'>"+value.op_scene+"</em>，"
								+"<em class='cz_kind'>"+value.user_name+"</em></span>"
								+"<span class='fr'>"
								+"<em>"+value.op_time+"</em></span></h4><div>"
								+"<em class='fl cz_kind_em'>详情："+contents+"</em>"
								+"<em class='fr'><a href='#'  onclick='showDetailed(\""+value.id+"\",this);'>>></a></em></div>"
                       			+"</li>";
                       	indexFlag++;
					});
					if(context == ""){
						context = "没有此时间范围内的历史记录。";
						$("#flattening_service_right").html("");
						$("#som_info_ul").html(context);
						$("#servicePage").html("");
					}else{
						$("#som_info_ul").html(context);
						pagingColumnByForeground("servicePage",$(".serviceClist"),"10");
						var aHref=$("#som_info_ul").children().eq(0).children().eq(1).children().eq(1).children().eq(0);
						showDetailed(firstRecordId,aHref);
					}
				},"json");
			}
			
			
			function getMaintenanceByYear(){
			$("#gisDivContent").html("");
			$("#noResourceDiv").hide();
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
				var context = "";
				var indexFlag=0;
				var firstRecordId="";
				$.post("../maintain/loadNetworkResourceMaintenanceAction",params,function(data){
					$.each(data, function(key,value){
						if(indexFlag==0){
							if(value.op_category == '添加资源'){
								context = context 
								+"<li class='ico_5 selected serviceClist'>";
							}else if(value.op_category == '属性修改'){
								context = context 
								+"<li class='ico_4 selected serviceClist'>";
							}else if(value.op_category == "资源关联"){
								context = context 
								+"<li class='ico_1 selected serviceClist'>";
							}else if(value.op_category == "解除关联"){
								context = context 
								+"<li class='ico_2 selected serviceClist'>";
							}else if(value.op_category == "维护服务"){
								context = context 
								+"<li class='ico_3 selected serviceClist'>";
							}else if(value.op_category == "删除资源"){
								context = context 
								+"<li class='ico_6 selected serviceClist'>";
							}else{
								context = context 
								+"<li class='ico_1 selected serviceClist'>";
							}
							firstRecordId=value.id;
						}else{
							if(value.op_category == '添加资源'){
								context = context 
								+"<li class='ico_5  serviceClist'>";
							}else if(value.op_category == '属性修改'){
								context = context 
								+"<li class='ico_4  serviceClist'>";
							}else if(value.op_category == "资源关联"){
								context = context 
								+"<li class='ico_1  serviceClist'>";
							}else if(value.op_category == "解除关联"){
								context = context 
								+"<li class='ico_2  serviceClist'>";
							}else if(value.op_category == "维护服务"){
								context = context 
								+"<li class='ico_3  serviceClist'>";
							}else if(value.op_category == "删除资源"){
								context = context 
								+"<li class='ico_6  serviceClist'>";
							}else{
								context = context 
								+"<li class='ico_1  serviceClist'>";
							}
						}
						
						var op_scene = "";
						if(value.op_scene != null &&  value.op_scene != "" && value.op_scene != "null"){
							op_scene = value.op_scene+",";
						}
						var op_cause = "";
						if(value.op_cause != null &&  value.op_cause != "" && value.op_cause != "null"){
							op_cause = value.op_cause;
						}
						var content;
	               		var contents = "";
						if(value.content && value.content != "null"){
							content = value.content.split("$_$");
						}
						if(content){
							$.each(content,function(k,v){
							contents = contents + v +".";
							});
						}
						context = context
							
							+"<h4><span class='cz_kind_span'>"
								+"<em>"+value.op_category+"</em>，"
								+"<em class='cz_kind'>"+value.op_cause+"</em>，"
								+"<em class='cz_kind'>"+value.op_scene+"</em>，"
								+"<em class='cz_kind'>"+value.user_name+"</em></span>"
								+"<span class='fr'>"
								+"<em>"+value.op_time+"</em></span></h4><div>"
								+"<em class='fl cz_kind_em'>详情："+contents+"</em>"
								+"<em class='fr'><a href='#'  onclick='showDetailed(\""+value.id+"\",this);'>>></a></em></div>"
                       			+  "  </li>";
                       	indexFlag++;
					});
					if(context == ""){
						context = "没有此时间范围内的历史记录。";
						$("#flattening_service_right").html("");
						$("#som_info_ul").html(context);
						$("#servicePage").html("");
					}else{
						$("#som_info_ul").html(context);
						pagingColumnByForeground("servicePage",$(".serviceClist"),"10");
						var aHref=$("#som_info_ul").children().eq(0).children().eq(1).children().eq(1).children().eq(0);
						showDetailed(firstRecordId,aHref);
					}
					
				},"json");
			}
//查询维护记录			
function searchMaintenance(me){
				$("#Latest_Month").removeClass("selected");
				$("#Latest_Year").removeClass("selected");
				$("#All_Time").removeClass("selected");
				var resourceType = $("input[name='operatedCurrentEntityType']").val();
				var resourceId = $("input[name='operatedCurrentEntityId']").val();
				var searchCondition = $(me).prev().val();
				if(searchCondition=="请输入查询条件"||$.trim(searchCondition)==""){
					alert("请输入查询条件")
					return false;
				}
				var params = {resourceId:resourceId,resourceType:resourceType,searchCondition:searchCondition};
				var context = "";
				var indexFlag=0;
				var firstRecordId="";
				$.post("../maintain/searchNetworkResourceMaintenanceByCondition",params,function(data){
					$.each(data, function(key,value){
						if(indexFlag==0){
							if(value.op_category == '添加资源'){
								context = context 
								+"<li class='ico_5 selected serviceClist'>";
							}else if(value.op_category == '属性修改'){
								context = context 
								+"<li class='ico_4 selected serviceClist'>";
							}else if(value.op_category == "资源关联"){
								context = context 
								+"<li class='ico_1 selected serviceClist'>";
							}else if(value.op_category == "解除关联"){
								context = context 
								+"<li class='ico_2 selected serviceClist'>";
							}else if(value.op_category == "维护服务"){
								context = context 
								+"<li class='ico_3 selected serviceClist'>";
							}else if(value.op_category == "删除资源"){
								context = context 
								+"<li class='ico_6 selected serviceClist'>";
							}else{
								context = context 
								+"<li class='ico_1 selected serviceClist'>";
							}
							firstRecordId=value.id;
						}else{
							if(value.op_category == '添加资源'){
								context = context 
								+"<li class='ico_5 serviceClist'>";
							}else if(value.op_category == '属性修改'){
								context = context 
								+"<li class='ico_4  serviceClist'>";
							}else if(value.op_category == "资源关联"){
								context = context 
								+"<li class='ico_1  serviceClist'>";
							}else if(value.op_category == "解除关联"){
								context = context 
								+"<li class='ico_2  serviceClist'>";
							}else if(value.op_category == "维护服务"){
								context = context 
								+"<li class='ico_3  serviceClist'>";
							}else if(value.op_category == "删除资源"){
								context = context 
								+"<li class='ico_6 serviceClist'>";
							}else{
								context = context 
								+"<li class='ico_1  serviceClist'>";
							}
						}
						
						var op_scene = "";
						if(value.op_scene != null &&  value.op_scene != "" && value.op_scene != "null"){
							op_scene = value.op_scene+",";
						}
						var op_cause = "";
						if(value.op_cause != null &&  value.op_cause != "" && value.op_cause != "null"){
							op_cause = value.op_cause;
						}
						var content;
	               		var contents = "";
						if(value.content && value.content != "null"){
							content = value.content.split("$_$");
						}
						if(content){
							$.each(content,function(k,v){
							contents = contents + v +".";
							});
						}
						context = context
							
							+"<h4><span class='cz_kind_span'>"
								+"<em>"+value.op_category+"</em>，"
								+"<em class='cz_kind'>"+value.op_cause+"</em>，"
								+"<em class='cz_kind'>"+value.op_scene+"</em>，"
								+"<em class='cz_kind'>"+value.user_name+"</em></span>"
								+"<span class='fr'>"
								+"<em>"+value.op_time+"</em></span></h4><div>"
								+"<em class='fl cz_kind_em'>详情："+contents+"</em>"
								+"<em class='fr'><a href='#'  onclick='showDetailed(\""+value.id+"\",this);'>>></a></em></div>"
                       			+  "  </li>";
                       	indexFlag++;
					});
					if(context == ""){
						context = "没有此查询条件的历史记录。";
						$("#flattening_service_right").html("");
						$("#som_info_ul").html(context);
						$("#servicePage").html("");
					}else{
						$("#som_info_ul").html(context);
						pagingColumnByForeground("servicePage",$(".serviceClist"),"10");
						var aHref=$("#som_info_ul").children().eq(0).children().eq(1).children().eq(1).children().eq(0);
						showDetailed(firstRecordId,aHref);
					}
					
				},"json");
}



function forward(currentEntityType,currentEntityId,me){
			$(".ldt_content a").css("background","#EEEEEE");
			$(me).css("background","#FFFFFF");
            var areaId =$("#areaId").val();
			var url = "getPhysicalresForOperaAction";
			var params = {currentEntityId:currentEntityId,currentEntityType:currentEntityType,loadBigPage:"loadBigPage",areaId:areaId,showType:"showTask"}
			//AJAX加载修改物理资源页面
			$.post(url, params, function(data){
				$("#gisDivContent").html(data);
				$("#noResourceDiv").hide();
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
							content +="<li><span class='aetg_text' onclick='chooseResourceForChildResource(this)'>"+newstr+"</span><input type='hidden' value='"+value.name+"'/><input type='hidden' value='selectRangeResChosenType'/><input type='hidden' value='"+value.id+"'/><input type='hidden' value='"+value._entityType+"'/><span class='aetg_link'><a  href=\"javascript:showBasicResourceInfo('"+value.id+"','"+value._entityType+"')\">基本信息</a><a href='getPhysicalresForOperaAction?currentEntityType="+value._entityType+"&currentEntityId="+value.id+"&areaId="+areaId+"&modelType=view&showType=showTask' target='_blank'>详细</a></span></li>";
						}else{
							var stringObj=value.label;    
		          		    var newstr=stringObj.replace(searchConditionText,"<em style='color:red'>"+searchConditionText+"</em>"); 
							content +="<li><span class='aetg_text' onclick='chooseResourceForChildResource(this)'>"+newstr+"</span><input type='hidden' value='"+value.label+"'/><input type='hidden' value='selectRangeResChosenType'/><input type='hidden' value='"+value.id+"'/><input type='hidden' value='"+value._entityType+"'/><span class='aetg_link'><a  href=\"javascript:showBasicResourceInfo('"+value.id+"','"+value._entityType+"')\">基本信息</a><a href='getPhysicalresForOperaAction?currentEntityType="+value._entityType+"&currentEntityId="+value.id+"&areaId="+areaId+"&modelType=view&showType=showTask' target='_blank'>详细</a></span></li>";
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
								content +="<li><span class='aetg_text' onclick='chooseResourceForChildResource(this)'>"+newstr+"</span><input type='hidden' value='"+value.name+"'/><input type='hidden' value='selectRangeResChosenType'/><input type='hidden' value='"+value.id+"'/><input type='hidden' value='"+value._entityType+"'/><span class='aetg_link'><a  href=\"javascript:showBasicResourceInfo('"+value.id+"','"+value._entityType+"')\">基本信息</a><a href='getPhysicalresForOperaAction?currentEntityType="+value._entityType+"&currentEntityId="+value.id+"&areaId="+areaId+"&modelType=view&showType=showTask' target='_blank'>详细</a></span></li>";
							}else{
								var stringObj=value.label;    
			          		    var newstr=stringObj.replace(searchConditionText,"<em style='color:red'>"+searchConditionText+"</em>"); 
								content +="<li><span class='aetg_text' onclick='chooseResourceForChildResource(this)'>"+newstr+"</span><input type='hidden' value='"+value.label+"'/><input type='hidden' value='selectRangeResChosenType'/><input type='hidden' value='"+value.id+"'/><input type='hidden' value='"+value._entityType+"'/><span class='aetg_link'><a  href=\"javascript:showBasicResourceInfo('"+value.id+"','"+value._entityType+"')\">基本信息</a><a href='getPhysicalresForOperaAction?currentEntityType="+value._entityType+"&currentEntityId="+value.id+"&areaId="+areaId+"&modelType=view&showType=showTask' target='_blank'>详细</a></span></li>";
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
						content +="<li><span class='aetg_text' onclick='chooseResourceForChildResource(this)'>"+newstr+"</span><input type='hidden' value='"+value.name+"'/><input type='hidden' value='selectRangeResChosenType'/><input type='hidden' value='"+value.id+"'/><input type='hidden' value='"+value._entityType+"'/><span class='aetg_link'><a  href=\"javascript:showBasicResourceInfo('"+value.id+"','"+value._entityType+"')\">基本信息</a><a href='getPhysicalresForOperaAction?currentEntityType="+value._entityType+"&currentEntityId="+value.id+"&areaId="+areaId+"&modelType=view&showType=showTask' target='_blank'>详细</a></span></li>";
					}else{
						var stringObj=value.label;    
	          		    var newstr=stringObj.replace(searchConditionText,"<em style='color:red'>"+searchConditionText+"</em>"); 
						content +="<li><span class='aetg_text' onclick='chooseResourceForChildResource(this)'>"+newstr+"</span><input type='hidden' value='"+value.label+"'/><input type='hidden' value='selectRangeResChosenType'/><input type='hidden' value='"+value.id+"'/><input type='hidden' value='"+value._entityType+"'/><span class='aetg_link'><a  href=\"javascript:showBasicResourceInfo('"+value.id+"','"+value._entityType+"')\">基本信息</a><a href='getPhysicalresForOperaAction?currentEntityType="+value._entityType+"&currentEntityId="+value.id+"&areaId="+areaId+"&modelType=view&showType=showTask' target='_blank'>详细</a></span></li>";
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
							content +="<li><span class='aetg_text' onclick='chooseResourceForChildResourceForLogicalres(this)'>"+newstr+"</span><input type='hidden' value='"+value.name+"'/><input type='hidden' value='selectRangeResChosenType'/><input type='hidden' value='"+value.id+"'/><input type='hidden' value='"+value._entityType+"'/><span class='aetg_link'><a  href=\"javascript:showBasicResourceInfo('"+value.id+"','"+value._entityType+"')\">基本信息</a><a href='../physicalres/getPhysicalresForOperaAction?currentEntityType="+value._entityType+"&currentEntityId="+value.id+"&areaId="+areaId+"&modelType=view&showType=showTask' target='_blank'>详细</a></span></li>";
						}else{
							var stringObj=value.label;    
		          		    var newstr=stringObj.replace(searchConditionText,"<em style='color:red'>"+searchConditionText+"</em>"); 
							content +="<li><span class='aetg_text' onclick='chooseResourceForChildResourceForLogicalres(this)'>"+newstr+"</span><input type='hidden' value='"+value.label+"'/><input type='hidden' value='selectRangeResChosenType'/><input type='hidden' value='"+value.id+"'/><input type='hidden' value='"+value._entityType+"'/><span class='aetg_link'><a  href=\"javascript:showBasicResourceInfo('"+value.id+"','"+value._entityType+"')\">基本信息</a><a href='../physicalres/getPhysicalresForOperaAction?currentEntityType="+value._entityType+"&currentEntityId="+value.id+"&areaId="+areaId+"&modelType=view&showType=showTask' target='_blank'>详细</a></span></li>";
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
								content +="<li><span class='aetg_text' onclick='chooseResourceForChildResourceForLogicalres(this)'>"+newstr+"</span><input type='hidden' value='"+value.name+"'/><input type='hidden' value='selectRangeResChosenType'/><input type='hidden' value='"+value.id+"'/><input type='hidden' value='"+value._entityType+"'/><span class='aetg_link'><a  href=\"javascript:showBasicResourceInfo('"+value.id+"','"+value._entityType+"')\">基本信息</a><a href='../physicalres/getPhysicalresForOperaAction?currentEntityType="+value._entityType+"&currentEntityId="+value.id+"&areaId="+areaId+"&modelType=view&showType=showTask' target='_blank'>详细</a></span></li>";
							}else{
								var stringObj=value.label;    
			          		    var newstr=stringObj.replace(searchConditionText,"<em style='color:red'>"+searchConditionText+"</em>"); 
								content +="<li><span class='aetg_text' onclick='chooseResourceForChildResourceForLogicalres(this)'>"+newstr+"</span><input type='hidden' value='"+value.label+"'/><input type='hidden' value='selectRangeResChosenType'/><input type='hidden' value='"+value.id+"'/><input type='hidden' value='"+value._entityType+"'/><span class='aetg_link'><a  href=\"javascript:showBasicResourceInfo('"+value.id+"','"+value._entityType+"')\">基本信息</a><a href='../physicalres/getPhysicalresForOperaAction?currentEntityType="+value._entityType+"&currentEntityId="+value.id+"&areaId="+areaId+"&modelType=view&showType=showTask' target='_blank'>详细</a></span></li>";
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
						content +="<li><span class='aetg_text' onclick='chooseResourceForChildResourceForLogicalres(this)'>"+newstr+"</span><input type='hidden' value='"+value.name+"'/><input type='hidden' value='selectRangeResChosenType'/><input type='hidden' value='"+value.id+"'/><input type='hidden' value='"+value._entityType+"'/><span class='aetg_link'><a  href=\"javascript:showBasicResourceInfo('"+value.id+"','"+value._entityType+"')\">基本信息</a><a href='../physicalres/getPhysicalresForOperaAction?currentEntityType="+value._entityType+"&currentEntityId="+value.id+"&areaId="+areaId+"&modelType=view&showType=showTask' target='_blank'>详细</a></span></li>";
					}else{
						var stringObj=value.label;    
	          		    var newstr=stringObj.replace(searchConditionText,"<em style='color:red'>"+searchConditionText+"</em>"); 
						content +="<li><span class='aetg_text' onclick='chooseResourceForChildResourceForLogicalres(this)'>"+newstr+"</span><input type='hidden' value='"+value.label+"'/><input type='hidden' value='selectRangeResChosenType'/><input type='hidden' value='"+value.id+"'/><input type='hidden' value='"+value._entityType+"'/><span class='aetg_link'><a  href=\"javascript:showBasicResourceInfo('"+value.id+"','"+value._entityType+"')\">基本信息</a><a href='../physicalres/getPhysicalresForOperaAction?currentEntityType="+value._entityType+"&currentEntityId="+value.id+"&areaId="+areaId+"&modelType=view&showType=showTask' target='_blank'>详细</a></span></li>";
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
					return false;
				}
			}else{
				if(index!=$("li[class='photoLi']").size()-1){
					$(this).hide();
					$(this).parent().children().eq(index+1).show();
					$("em[class='photoEm']").hide();
					$("em[class='photoEm']").each(function(i){
						if(i==index+1){
							$(this).show();
						}
					})
					return false;
				}
			}
		}
	});	
}

/**
查询
**/
function getOrderList(orderType,getType){
	$("#gisDivContent").html("");
	$("#noResourceDiv").hide();
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
		var opUrlApp = wo.opUrlApp;
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



function getOdmAndTerminalLayOutView(){
		var currentVal = $("#currentValues").val();
		var currentEntityId = currentVal.substring(currentVal.indexOf("#")+1,currentVal.length);//当前id
		var currentEntityType=currentVal.substring(0,currentVal.indexOf("#"));//当前资源类型
		//currentEntityId = "75";
		//currentEntityType="FiberCrossCabinet";
		var params = {currentEntityType:currentEntityType,currentEntityId:currentEntityId};
	 	$.post("getOdmandterminallayoutAction", params, function(data){	
			if(data.maxCount!=undefined){
	 			$("#panels_view").html("无面板图");
	 			$("#panels_view").parent().parent().css("overflow","hidden");
	 		}else{
	 			$("#panels_view").parent().parent().css("overflow","auto");
	 			$("#messages_view").html("");
	 			$("#paneltable_view").html("");
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