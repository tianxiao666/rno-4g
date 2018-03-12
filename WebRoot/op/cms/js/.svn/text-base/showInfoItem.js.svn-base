

$(function(){
	loadCMS();
	$("#addAttachment").click(function(){
		var addFlag=true;
		
		var attachmentList=$("input[name=uploadAttachment]");
		for(var i=0;i<attachmentList.length;i++){
			var attachValue=attachmentList[i].value;
			if(!attachValue || attachValue.trim()==""){
				addFlag=false;
				break;
			}
		}
		if(addFlag){
			var divList=$("#uploadAttachment_div");
			var ele_div=$("<div style=\"margin-bottom:5px;\"></div>");
			var ele_file=$("<input type='file' name='uploadAttachment' />");
			ele_file.appendTo(ele_div);
			ele_div.appendTo(divList);
		}
	
	})
	//返回发布信息
	$("#btnNext").click(function(){
		//var infoItemId=$("#infoItemId").val();
		//if($("#Itype").val() == "approve"){
		//	if($("#infoReleaseStatus").val() != 21){
		//		$.post("loadApproveInfoReleasePageAction",{infoItemId:infoItemId},function(data1){
		//						$("#right_content").html(data1);
		//		});
		//	}else{
		//		var infoReleaseId = $("#infoReleaseId").val();
		//		$.post("loadApproveInfoReleasePageAction",{infoReleaseId:infoReleaseId},function(data1){
		//						$("#right_content").html(data1);
		//		});
		//	}
		//}
		var tabType = $("#tabType").val();
		loadInfoReleaseAction(tabType);
	});
	
	
	//表单AJAX提交
		$("#operaForm").ajaxForm({
			success:function(data){
				if(data != 0){
						$.post("loadAddInfoReleasePageAction?infoItemId="+data,{},function(data1){
							$("#right_content").html(data1);
						});
				}
			}
		});
})

function clickShowMore(id){
if(id == 'advanced_mode_but1'){
	$("#advanced_mode_but2").show();
	$("#"+id).hide();
	$("#main_output_table").show();
}else{
	$("#advanced_mode_but1").show();
	$("#"+id).hide();
	$("#main_output_table").hide();
}
}

function loadCMS(){
	var attaNames = $("#attaName").val();
	var attaUrls = $("#attaUrl").val();
	var countext = "";
	if(attaNames != null && attaNames != "" && attaUrls != null && attaUrls != ""){
	attaNames = attaNames.split("#");
	attaUrls = attaUrls.split("#");
	countext = " <ul>";
	for(var i = 0;attaNames.length > i;i++){
		if(attaNames[i] != null && attaNames[i].trim() != "" && attaUrls[i] != null && attaUrls[i].trim() != ""){
			countext = countext + "   <li><span>"+attaNames[i]+" </span><a href=\"/ops/"+attaUrls[i]+"\" target=\"_blank\">打开</a><em class=\"attachment_img_del\" onclick=\"deleteAtta(this);\">×</em>"
									+ "<input type=\"hidden\"  value=\""+attaNames[i]+"\"/>"
									+ "	<input type=\"hidden\"  value=\""+attaUrls[i]+"\"/></li>";
		}
									
	}
	countext = countext + "	</ul>";
	$("#attr").html(countext); 
	clickShowMore('advanced_mode_but1');
	}
	
	var picNames = $("#picName").val();
	var picUrls = $("#picUrl").val();
	var countextpic = "";
	if(picNames != null && picNames != "" && picUrls != null && picUrls != ""){
	picNames = picNames.split("#");
	picUrls = picUrls.split("#");
	countextpic = " <ul>";
	for(var i = 0;picNames.length > i;i++){
		if(picNames[i] != null && picNames[i].trim() != "" && picUrls[i] != null && picUrls[i].trim() != ""){
			countextpic = countextpic + "   <li><span>"+picNames[i]+" </span><a href=\"/ops/"+picUrls[i]+"\" target=\"_blank\">打开</a><em class=\"attachment_img_del\" onclick=\"deletePic(this);\">×</em>"
									+ "<input type=\"hidden\"  value=\""+picNames[i]+"\"/>"
									+ "	<input type=\"hidden\"  value=\""+picUrls[i]+"\"/></li>";
		}
									
	}
	countextpic = countextpic + "	</ul>";
	$("#pica").html(countextpic); 
	clickShowMore('advanced_mode_but1');
	}
}


function deleteAtta(me){
	var attaName = $(me).next().val();
	var attaUrl = $(me).next().next().val();
	var attaNames = $("#attaName").val();
	var attaUrls = $("#attaUrl").val();
	attaNames = attaNames.split("#");
	attaUrls = attaUrls.split("#");
	var attaN = "";
	var attaU = "";
	$.each(attaNames,function(k,y){
		if(attaName != y){
			if(y != null && y.trim() != "" ){
				attaN = attaN +y + "#";
			}
		}
	});
	$.each(attaUrls,function(k,y){
		if(attaUrl != y){
			if(y != null && y.trim() != "" ){
				attaU = attaU +y + "#";
			}
		}
	});
	$("#attaName").val(attaN);
	$("#attaUrl").val(attaU);
	$(me).parent().remove();
}


function deletePic(me){
	var picName = $(me).next().val();
	var picUrl = $(me).next().next().val();
	var picNames = $("#picName").val();
	var picUrls = $("#picUrl").val();
	picNames = picNames.split("#");
	picUrls = picUrls.split("#");
	var picN = "";
	var picU = "";
	$.each(picNames,function(k,y){
		if(picName != y){
			picN = picN +y + "#";
		}
	});
	$.each(picUrls,function(k,y){
		if(picUrl != y){
			picU = picU +y + "#";
		}
	});
	$("#picName").val(picN);
	$("#picUrl").val(picU);
	$(me).parent().remove();
}


function nomvalidateLimit(){
	if($("#nom_titlte").val() == ""){
		//alert("标题/主题不能为空");
		$("#nom_titlte").next().text("*标题/主题不能为空");
		return false;
		
	}else{
		$("#nom_titlte").next().text("");
	}
	if($("#nom_content").val() == ""){
		//alert("正文不能为空");
		$("#nom_content").next().text("*正文不能为空");
		return false;
		
	}else{
		$("#nom_content").next().text("");
	}
	if($("#nom_titlte").val().length > 100){
		$("#nom_titlte").next().text("*标题/主题不能超过100字");
		//alert("标题/主题不能超过100字");
		return false;
	}else{
		$("#nom_titlte").next().text("");
	}
	if($("#nom_label").val().length > 50){
		$("#nom_label").next().text("*文件号不能超过50字");
		//alert("文件号不能超过50字");
		return false;
	}else{
		$("#nom_label").next().text("");
	}
	if($("#nom_titlte").val().length > 100){
		$("#nom_titlte").next().text("*标题/主题不能超过100字");
		//alert("标题/主题不能超过100字");
		return false;
	}else{
		$("#nom_titlte").next().text("");
	}
	if($("#nom_content").val().length > 500){
		$("#nom_content").next().text("*正文不能超过500字");
		//alert("标题/主题不能超过100字");
		return false;
	}else{
		$("#nom_content").next().text("");
	}
}