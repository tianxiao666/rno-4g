<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript">
$(function(){
	$("#comment_Dialog").mousedown(function(e){ //e鼠标事件  
        $(this).css("cursor","move");//改变鼠标指针的形状  
		var offset = $(this).offset();//DIV在页面的位置  
        var x = e.pageX - offset.left - 250;//获得鼠标指针离DIV元素左边界的距离  
        var y = e.pageY - offset.top;//获得鼠标指针离DIV元素上边界的距离  
        $(document).bind("mousemove",function(ev){//绑定鼠标的移动事件，因为光标在DIV元素外面也要有效果，所以要用doucment的事件，而不用DIV元素的事件  
            $(".dialog").stop();//加上这个之后  
			var _x = ev.pageX - x;//获得X轴方向移动的值  
			var _y = ev.pageY - y;//获得Y轴方向移动的值  
			$(".dialog").animate({left:_x+"px",top:_y+"px"},10);  
        });       
    });  
    $(document).mouseup(function(){  
        $(".dialog").css("cursor","default");  
        $(this).unbind("mousemove");  
    })  
	//评论弹出框
	    $(".comment_dialog_show").click(function(){
	    	$("#emType").text($(".ontab").text());
	    	if($("#DateTimeSpan").length != 0  && $("#orgNameText").length != 0 ){
	    		$("#comment_title").text($("#DateTimeSpan").text() + $("#orgNameText").val());
	    	}
	    	
		    $(".comment_dialog").show();
	    });

	//时间格式化
Date.prototype.format = function(format){
    /*
     * eg:format="yyyy-MM-dd hh:mm:ss";
     */
    if(!format){
        format = "yyyy-MM-dd hh:mm:ss";
    }

    var o = {
            "M+": this.getMonth() + 1, // month
            "d+": this.getDate(), // day
            "h+": this.getHours(), // hour
            "m+": this.getMinutes(), // minute
            "s+": this.getSeconds(), // second
            "q+": Math.floor((this.getMonth() + 3) / 3), // quarter
            "S": this.getMilliseconds()
            // millisecond
    };

    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }

    for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)) { 
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" +o[k]).length));
        }
    }
    return format;
};
	
	$("#commentTime").val(new Date().format());
	if($("#ul_report_comment li").length > 10){
		pagingColumnByForeground("span_report_paging",$("#ul_report_comment li"),10);
	}
});

	function hideComment_Dialog(){
		$(".comment_dialog").hide();
		$(".black").hide();
	}
	
	
	//保存报表评论
	function saveComment(){
		var userId = $("#userId").val();
		var commentText = $("#commentText").val();
		var commentTime = $("#commentTime").val();	
		var reportId = $("#reportId").val();	
		var userName = $("#userName").val();	
		if(commentText == ""){
			alert("评论内容不能为空");
			return;
		}
		var url = "saveReportCommentAction";
	var params = {userId:userId,commentText:commentText,commentTime:commentTime,reportId:reportId,userName:userName};
	$.post(url, params, function(data){
		alert(data);
		clickGetUrgentRepairworkorderReport();
	});
	}
</script>
	<div class="statements_comment">
		<div class="statements_comment_title clearfix">
			<h3 class="fl">报表用户评论:</h3>
			<a class="comment_dialog_show"><em></em>我要评论</a>
		</div>
	    <div class="statements_comment_info">
			<ul id="ul_report_comment">
				<s:iterator value="reportCommentList" id="vs" status="st2">
					<s:if test="#st2.count%2 == 0">
						<li>
							<p class="comment_content">${vs.content }</p>
							<p class="comment_name">
								--
								<span>${vs.criticsname}</span>
								<span class="comment_time"><s:date name="#vs.criticstime" format="yyyy-MM-dd HH:mm:ss" /></span>
							</p>
						</li>
					</s:if>
					<s:else>
						<li class="c1">
							<p class="comment_content">${vs.content }</p>
							<p class="comment_name">
								--
								<span>${vs.criticsname}</span>
								<span class="comment_time"><s:date name="#vs.criticstime" format="yyyy-MM-dd HH:mm:ss" /></span>
							</p>
						</li>
					</s:else>
				</s:iterator>
			</ul>
			<div class="main_table_foot clearfix">
				<span class="fr" id="span_report_paging">
				</span>
			</div>
		</div>
		
		<div id="comment_Dialog" class="dialog comment_dialog" style="display:none;">
			<div class="dialog_header">
				<div class="dialog_title">报表评论编辑</div>
				<div class="dialog_tool">
				   <div class="dialog_tool_close dialog_closeBtn commentDelete_closeBtn" onclick="hideComment_Dialog();"></div>
				</div>
			</div>
			<div class="dialog_content comment_dialog_content">
				<div>
					<h3><span id="comment_title"></span><em id="emType"></em>分类统计报表</h3>
					<p class="comment_dialog_info"><span>评论意见：</span><textarea id="commentText"></textarea></p>
					<input type="hidden" class="input_text" readonly="readonly" style="width: 40%;" id="userName" value="${userName }"/>
					<input type="hidden" id="userId" class="input_text" readonly="readonly" value="${userId }"/>
					<input type="hidden" id="reportId" class="input_text" readonly="readonly" value="${reportId }"/>
					<input type="hidden" class="input_text" readonly="readonly" style="width: 40%;" value="" id="commentTime"/>
				</div>
				<div class="dialog_but">
					<button class="aui_state_highlight" id="btnSubOrg" onclick="saveComment();">保存</button>
				</div>
			</div>
		</div>
		
		<%--评论信息浮窗
		<div class="comment_dialog" style="display:none;">
			<div class="comment_dialog_header">
				<div class="comment_dialog_title">报表评论编辑</div>
				<div class="comment_dialog_tool">
				   <div class="comment_dialog_tool_close"></div>
				</div>
			</div>
			<div class="comment_dialog_content">
				<div>
					<h3><span id="comment_title"></span><em id="emType"></em>分类统计报表</h3>
					<p class="comment_dialog_info"><span>评论意见：</span><textarea id="commentText"></textarea></p>
					<input type="hidden" class="input_text" readonly="readonly" style="width: 40%;" id="userName" value="${userName }"/>
					<input type="hidden" id="userId" class="input_text" readonly="readonly" value="${userId }"/>
					<input type="hidden" id="reportId" class="input_text" readonly="readonly" value="${reportId }"/>
				<input type="hidden" class="input_text" readonly="readonly" style="width: 40%;" value="" id="commentTime"/>
				</div>
				<div class="but">
					<input type="button" id="btnSubOrg" value="保存"  onclick="saveComment();">
				</div>
			</div>
		</div>
		<div class="comment_black"></div>
		--%>
		<%-- 
		<ul class="statements_comment_fill">
		    <li>
			    <span>评&nbsp;论&nbsp;人：</span>
				<input type="text" class="input_text" readonly="readonly" style="width: 40%;" id="userName" value="${userName }"/>
				<input type="hidden" id="userId" class="input_text" readonly="readonly" value="${userId }"/>
				<input type="hidden" id="reportId" class="input_text" readonly="readonly" value="${reportId }"/>
				<span>评论时间：</span>
				<input type="text" class="input_text" readonly="readonly" style="width: 40%;" value="" id="commentTime"/>
			</li>
			<li>
			    <span class="vt">评论内容：</span>
			    <textarea style="width:90%;" id="commentText"></textarea>
			</li>
			<li class="tc">
			    <input class="input_button" type="button" value="提交" onclick="saveComment();"/>
			</li>
		</ul>
		 --%>
	</div>