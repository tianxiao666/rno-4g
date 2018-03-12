function clickAddInfoItem(me){
		$(".leftLevelMenu_info li").removeClass("menu_selected");
		$(me).addClass("menu_selected");
		var url = "loadAddInfoItemPageAction";
		var params = "";
		$.post(url, params, function(data){
			//alert(data);
			$("#right_content").html(data);
		});
		
	}
	
	function clickApproveInfoRelease(me){
		$(".leftLevelMenu_info li").removeClass("menu_selected");
		$(me).addClass("menu_selected");
		loadInfoReleaseAction("waitAudit");
	}
	
	function loadInfoReleaseAction(tabType){
		var url = "loadInfoReleaseAction";
		var params = {"tabType":tabType};
		$.post(url, params, function(data){
			//alert(data);
			//alert(tabType);
			$("#right_content").html(data);
			$("#infoList_tab li").removeClass("selected");
			$("#"+tabType).addClass("selected");
		});
	}
	
	$(function(){
		loadInfoReleaseAction("waitAudit");
	});