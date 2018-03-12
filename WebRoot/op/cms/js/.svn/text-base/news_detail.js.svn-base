
$(function(){
	loadCMS();
});
function loadCMS(){
	var attaNames = $("#attaName").val();
	var attaUrls = $("#attaUrl").val();
	var countext = "";
	if(attaNames != null && attaNames != "" && attaUrls != null && attaUrls != ""){
	attaNames = attaNames.split("#");
	attaUrls = attaUrls.split("#");
	countext = "<h4>附件：</h4><ul>";
	for(var i = 0;attaNames.length > i;i++){
		if(attaNames[i] != null && attaNames[i].trim() != "" && attaUrls[i] != null && attaUrls[i].trim() != ""){
			countext = countext + "   <li><span>"+attaNames[i]+" </span><a href=\"/ops/"+attaUrls[i]+"\" target=\"_blank\">打开</a>"
									+ "<input type=\"hidden\"  value=\""+attaNames[i]+"\"/>"
									+ "	<input type=\"hidden\"  value=\""+attaUrls[i]+"\"/></li>";
		}
									
	}
	countext = countext + "	</ul>";
	$("#attaDiv").html(countext); 
	$("#attaDiv").show();
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
			countextpic = countextpic + "	<img src=\"../../"+picUrls[i]+"\" style=\"width: 800px; margin-bottom:20px;\"/>";
		}
									
	}
	countextpic = countextpic + "	</ul>";
	$("#imgDiv").html(countextpic); 
	}
}