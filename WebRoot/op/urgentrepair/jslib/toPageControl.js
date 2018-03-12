$(document).ready(function() {
	//根据登录人身份控制
	if($("#hasPower").val() == "1"){
		//	根据任务单状态控制
		if($("#STATUS").val() == "9"||$("#STATUS").val() == "10") {	
			$(".acceptField").hide();
			$(".acceptDiv").hide();
			$(".finalReplyDiv").show();
			$(".finalReplyField").addClass("tab2-li-show");
			$(".buttonControl").removeAttr("disabled");
		}else if($("#STATUS").val() == "8") {
			$(".workField").hide();
		}else if($("#STATUS").val() == "11"){
			$(".acceptField").hide();
			$(".acceptDiv").hide();
			$(".finalReplyDiv").show();
			$(".finalReplyField").addClass("tab2-li-show");
			$("#handleReply").hide();
			$("#showReply").show();
			$(".buttonControl").attr("disabled","disabled");
		}else if($("#STATUS").val() == "18"){
			$(".acceptField").hide();
			$(".acceptDiv").hide();
			$(".finalReplyDiv").show();
			$(".finalReplyField").addClass("tab2-li-show");
			$("#handleReply").show();
			$("#showReply").hide();
			$(".buttonControl").attr("disabled","disabled");
			$("#stepReplySubmit").removeAttr("disabled");
		}else if($("#STATUS").val() == "12"||$("#STATUS").val() == "13"){
			$(".workField").hide();
			$(".buttonControl").attr("disabled","disabled");
		}
	}else{
		$(".buttonControl").attr("disabled",true);
		
		//	根据任务单状态控制
		if($("#STATUS").val() == "9"||$("#STATUS").val() == "10") {	
			$(".acceptField").hide();
			$(".acceptDiv").hide();
			$(".finalReplyDiv").show();
			$(".finalReplyField").addClass("tab2-li-show");
		}else if($("#STATUS").val() == "8") {
			$(".workField").hide();
		}else if($("#STATUS").val() == "11"){
			$(".acceptField").hide();
			$(".acceptDiv").hide();
			$(".finalReplyDiv").show();
			$(".finalReplyField").addClass("tab2-li-show");
			$("#handleReply").hide();
			$("#showReply").show();
		}else if($("#STATUS").val() == "18"){
			$(".acceptField").hide();
			$(".acceptDiv").hide();
			$(".finalReplyDiv").show();
			$(".finalReplyField").addClass("tab2-li-show");
			$("#handleReply").show();
			$("#showReply").hide();
			$(".buttonControl").attr("disabled","disabled");
		}else if($("#STATUS").val() == "12"||$("#STATUS").val() == "13"){
			$(".workField").hide();
		}
	}
});



