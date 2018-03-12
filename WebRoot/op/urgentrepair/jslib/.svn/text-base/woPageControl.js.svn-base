$(document).ready(function() {
	//根据登录人身份控制
	if($("#hasPower").val() == "1"){
		//	根据工单状态控制
		if($("#STATUS").val() == "3"||$("#STATUS").val() == "6") {	
			$(".acceptField").hide();
			$(".acceptDiv").hide();
			if($("#entryType").val()=="GIS"){
				$("#assignTaskDiv").show();
				$(".assignTaskField").addClass("tab2-li-show");
				$(".paifa_handle_content").show();
				$(".show_handle_content").val("派发任务▲");
			}else{
				$(".finalReplyDiv").show();
				$(".finalReplyField").addClass("tab2-li-show");
			}
			$(".buttonControl").removeAttr("disabled");
		}else if($("#STATUS").val() == "2") {
			$(".finalReplyField").hide();
			$(".stepReplyField").hide();
			$(".assignTaskField").hide();
			$("#buttonSpan").show();
			$(".toSendField").hide();
		}else if($("#STATUS").val() == "7"){
			$(".acceptField").hide();
			$(".acceptDiv").hide();
			$(".finalReplyDiv").show();
			$("#handleReply").hide();
			$("#showReply").show();
			$(".buttonControl").attr("disabled","disabled");
		}
	}else{
		$(".buttonControl").attr("disabled",true);
		
		//	根据工单状态控制
		if($("#STATUS").val() == "3"||$("#STATUS").val() == "6") {	
			$(".acceptField").hide();
			$(".acceptDiv").hide();
			$(".finalReplyDiv").show();
			$(".finalReplyField").addClass("tab2-li-show");
		}else if($("#STATUS").val() == "2") {
			$(".finalReplyField").hide();
			$(".stepReplyField").hide();
			$(".assignTaskField").hide();
			$(".toSendField").hide();
		}else if($("#STATUS").val() == "7"){
			$(".acceptField").hide();
			$("#handleReply").hide();
			$("#showReply").show();
		}
	}
});



