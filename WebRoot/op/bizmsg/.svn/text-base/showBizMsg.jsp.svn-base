<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>消息盒子</title>
		<link rel="stylesheet" type="text/css" href="css/messageBox_layout.css" />
		<style type="text/css">
			body{margin:0px;padding:0px;}
			tr{cursor:pointer}
			.trBg{background:#FEF}
		</style>
		<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
		<script type="text/javascript" src="jslib/jquery.jmp3.js"></script>
		<script type="text/javascript">
		//重新响铃，参数为次数
		function repeatRing(times) {
			var index = 0;
			//先马上响一次
			document.getElementById("soundIFrame").src = "sound.jsp";
			index++;
			var intervalFn = setInterval(function(){
				//sound.jsp，用作刷新响铃
				if(times > 1) {
					document.getElementById("soundIFrame").src = "sound.jsp";
					index++;
				}
				//响铃次数达到响铃消息数，或者已达5次(响铃消息数超过5条时，只响5次)
				if(index == times) {
					clearInterval(intervalFn);
				}
			},3000);
			/*
			//更改响铃消息为未读状态
			$.post("updateRingMsgToNoReadAction",function(){
			
			});*/
		}
		
		
		
		$(function(){
			setInterval("manageRefresh()",1000*60*5);
			operaSound();
			//未读消息数量为0时，关闭声音
			/*if('${newTaskMsgCount}' == "0" && '${overTimeMsgCount}' == "0" && '${hastenMsgCount}' == "0") {
				//关闭声音，jmp3插件，提供于火狐使用
				$("#sounddl").hide();
			}*/
			//如果是打开了消息盒子，则关闭声音，否则就根据用户的设置来控制是否打开声音
			if('${soundState}' == "off" || '${openPageState}' == "Yes") {
				//关闭声音，jmp3插件，提供于火狐使用
				$("#sounddl").hide();
				$("#soundImg").attr("src","images/mute.png");
			} else if('${soundState}' == "on" && !('${newTaskMsgRingCount}' == "0" && '${overTimeMsgRingCount}' == "0" && '${hastenMsgRingCount}' == "0")) {
				//$("#sounddl").show();
				/*
				var newTaskMsgRingCount = '${newTaskMsgRingCount}';
				newTaskMsgRingCount = parseInt(newTaskMsgRingCount);
				var overTimeMsgRingCount = '${overTimeMsgRingCount}';
				overTimeMsgRingCount = parseInt(overTimeMsgRingCount);
				var hastenMsgRingCount = '${hastenMsgRingCount}';
				hastenMsgRingCount = parseInt(hastenMsgRingCount);
				
				var totalCount = newTaskMsgRingCount + overTimeMsgRingCount + hastenMsgRingCount;
				alert('${newTaskMsgRingCount}');
				if(totalCount > 0) {
					if(totalCount <= 5) {
						//重新响铃，参数为次数
						repeatRing(totalCount);
					} else {
						//重新响铃，参数为次数
						repeatRing(5);
					}
				}*/
				
				//显示声音，生成bgsound标签，使用于IE
				$("body").prepend("<bgsound id='bgsoundForIE' src='message.mp3' autostart='true' loop='-1' volume='-500' />");
				$("#soundImg").attr("src","images/sound.png");
			}
		
			//音乐播放器
			$("#sounddl").jmp3({
				filepath: "music/",
				flashpath: "swf/",
				showfilename: "false",
				backcolor: "ffffff",
				forecolor: "ffffff",
				width: 1,
				showdownload: "true",
				repeat:"false",
				autoplay:"true"
			});
			
			$(".message_kind li").each(function(){
				$(this).click(function(){
					$(".message_kind li").removeClass("showedLi");
						$(this).addClass("showedLi");
					});
				});
			$(".message_kind li").each(function(index){
				$(this).click(function(){
					var liObj = $(this);
					//根据当前选中的tab，ajax加载消息内容
					getMsgByAjax(liObj, index);
				});
			});
			
			//选择未读或者已读信息时同时加载选中tab对应的信息
			$(".cbxReadState").click(function(){
				manageRefresh();
				/*
				var liObj = null;
				var liIndex = 0;
				
				$(".message_kind li").each(function(index){
					//获取选中tab的li对象及其对应的下标
					if($(this).attr("class") == "showedLi") {
						liObj = $(this);
						liIndex = index;
						return false;
					}
				});
				
				//根据当前选中的tab，ajax加载消息内容
				getMsgByAjax(liObj, liIndex);
				*/
			});
			
				//控制内容过长时的省略显示，点击显示详细信息，再次点击隐藏并显示为省略号
				$(".long_td").live("click", function(){
					if($(this).find(".long_td_div").css("text-overflow") == "ellipsis") {
						$(this).find(".long_td_div").css({"text-overflow":"clip","white-space":"normal"});
					} else {
						$(this).find(".long_td_div").css({"text-overflow":"ellipsis","white-space":"nowrap"});
					}
				});
				
				//全选或反选相应的消息内容
				$(".cbxCheckAll").live("click", function(){
					var cbxCheckAll = $(this);
			
					cbxCheckAll.parent().prev().find(".cbxMsg").each(function(){
						if(cbxCheckAll.attr("checked") == "checked") {
							$(this).attr("checked", true);
						} else {
							$(this).attr("checked", false);
						}
					});
				});
				
				//删除所选择的信息
				$(".btnDeleteMsg").live("click",function(){
					var btnDeleteMsg = $(this);
					
					if(btnDeleteMsg.parent().prev().find(".cbxMsg:checked").length == 0) {
						alert("请选择需要删除的信息!");
						return false;
					}
					
					if(!confirm("确定要删除这些信息吗?")) {
						return false;
					}
					
					//拼接所需要删除的信息的id
					var ids = "";
					btnDeleteMsg.parent().prev().find(".cbxMsg:checked").each(function(){
						ids += $(this).val() + "-";
					});
					ids = ids.substring(0, ids.length - 1);
					
					//批量删除消息
					$.post("deleteBizMessageAction", {ids:ids}, function(data){
						if(data == "success") {
							manageRefresh();
						}
					},'json');
				});
			});
			
			function messageBox_hide(){
				var messageBox = document.getElementById("messageBox_container");
				messageBox.style.display="none";
			}
			
			//喇叭图片展现和声音管理
			function manageSoundControl() {
				var soundImg = document.getElementById("soundImg");
				var soundControl = document.getElementById("bgsoundForIE");
				//小喇叭图片控制和声音控制
				if(soundImg.getAttribute("src") == "images/sound.png"){
					//关掉声音的图片
					soundImg.setAttribute("src","images/mute.png");
					//标记为关闭声音
					$("#hidCtrSoundImgVal").val("closeSound");
					//关闭声音，生成bgsound标签，使用于IE
					$(soundControl).remove();
					//关闭声音，jmp3插件，提供于火狐使用
					$("#sounddl").hide();
				}else if(soundImg.getAttribute("src") == "images/mute.png"){
					//打开声音的图片
					soundImg.setAttribute("src","images/sound.png");
					//标记为打开声音
					$("#hidCtrSoundImgVal").val("openSound");
					//打开声音
					$.post("getNoReadBizMessageCountAndUpdateBizMessageAjaxAction",function(data){
						if(parseInt(data) > 0) {
							//显示声音，生成bgsound标签，使用于IE
							$("body").prepend("<bgsound id='bgsoundForIE' src='message.mp3' autostart='true' loop='-1' volume='-500' />");
							//显示声音，jmp3插件，提供于火狐使用
							$("#sounddl").show();
						}
					},'json');
				}
				
				var soundAjaxState = "";
				if(soundImg.getAttribute("src") == "images/sound.png") {
					soundAjaxState = "on";
				} else {
					soundAjaxState = "off";
				}
				
				//根据图片，更改声音控制的session
				$.post("updateSoundSessionAction",{soundAjaxState:soundAjaxState},function(){
				
				});
			}
			
			//根据当前选中的tab，ajax加载消息内容
			function getMsgByAjax(liObj,index) {
				//通过id获取需要加载的消息的类型
				var msgType = liObj.attr("id");
				var readState = "0";
				
				//未读与已读都选择了的情况，不需要进行加载，清空对应的消息table内容 (*****************暂定公告和信息不做任何操作********************)
				if(msgType == "note" || msgType == "msg") {
					/*
					//对应的消息类型内容层显示
					$(".messageBox_tabMain").hide();
					$(".messageBox_tabMain").eq(index).show();
					$(".messageBox_tabMain").eq(index).find("table").eq(1).empty();
					
					//未读与已读都选择不需要进行加载，所以数量显示为0
					var liContent = liObj.html();
					liContent = liContent.substring(0, liContent.indexOf("(") + 1) + "0)";
					liObj.html(liContent);
					
					//因为暂时没有这两项的内容，先把喇叭隐藏掉
					$("#soundImg").hide();
					
					//控制声音
					operaSound();
					
					return false;
					*/
				}
				
				//显示喇叭
				$("#soundImg").show();
				var checkValue = "";
				$(".cbxReadState").each(function(){
					if($(this).attr("checked")){
						var value = $(this).val();
						checkValue += value+",";
					}
				});
				if(checkValue=="noRead,"){
					readState = "0";
				}else if(checkValue=="hasRead,"){
					readState = "1";
				}else if(checkValue=="noRead,hasRead,"){
					readState = "2";
				}else{
					readState = "3";
				}
				/*
				if($(".cbxReadState:checked").length == 1) {
					//查询一种读取类型的信息
					readState = "1";
				} else if($(".cbxReadState:checked").length == 2) {
					//同时选择未读与已读，未读与已读都查询
					readState = "2";
				}
				*/
				//根据类型和读取情况，AJAX加载消息内容和消息数量
				$.post("loadBizMessageByTypeForAjaxAction",{"typeKey":msgType,"msgState":readState},function(data){
					$("#msgTable").html(data);
					//var count = $("#msgCount").val();
					//加载指定的消息数量
					var liContent = liObj.html();
					liContent = liContent.substring(0, liContent.indexOf("(") + 1) + count + ")";
					liObj.html(liContent);
					
					//对应的消息类型内容层显示
					//$(".messageBox_tabMain").hide();
					//$(".messageBox_tabMain").eq(index).show();
					
					//控制声音
					operaSound();
				});
			}
			
			//控制声音
			function operaSound() {
				$.post("getNoReadBizMessageCountAndUpdateBizMessageAjaxAction",function(data){
					if(parseInt(data) > 0) {
						//存在未读消息，不存在声音控件的情况，生成声音控件
						if($("bgsound").length == 0 && $("#soundImg").attr("src") == "images/sound.png") {
							repeatRing(1);
							//显示声音，生成bgsound标签，使用于IE
							$("body").prepend("<bgsound id='bgsoundForIE' src='message.mp3' autostart='true' loop='-1' volume='-500' />");
							//显示声音，jmp3插件，提供于火狐使用
							$("#sounddl").show();
						}
					} else {
						//不存在未读消息，存在声音控制的情况，移除声音控件
						if($("bgsound").length > 0) {
							//关闭声音，生成bgsound标签，使用于IE
							$("#bgsoundForIE").remove();
							//关闭声音，jmp3插件，提供于火狐使用
							$("#sounddl").hide();
						}
					}
				},'json');
			}
			
			//操作消息内容的tr
			function operaTr(curTr) {
				$(".contentTr").each(function(){
					$(this).attr("class", "contentTr");
				});
				$(curTr).attr("class", "contentTr trBg");
				
				var msgId = $(curTr).children().eq(0).children().val();
				$.post("updateMsgToHasReadAction",{"msgId":msgId},function(){
					//不需要回调，只需要更新信息为已读
				});
			}
			
			//刷新按钮
			function manageRefresh(){
				$("#refreshImg").hide();
				$("#refreshText").show();
				var msgType = "";
				var readState = "";
				//msgType = $(".showedLi").eq(0).attr("id");
				msgType = $("#msgType").val();
				var checkValue = "";
				$(".cbxReadState").each(function(){
					if($(this).attr("checked")){
						var value = $(this).val();
						checkValue += value+",";
					}
				});
				if(checkValue=="noRead,"){
					readState = "0";
				}else if(checkValue=="hasRead,"){
					readState = "1";
				}else if(checkValue=="noRead,hasRead,"){
					readState = "2";
				}else{
					readState = "3";
				}
				
				$(".cbxCheckAll").eq(0).attr("checked",false);
				$.post("loadBizMessageByTypeForAjaxAction",{"typeKey":msgType,"msgState":readState},function(data){
					$("#msgTable").html("");
					var str = "";
					$.each(data.list,function(index,val){
						str +="<tr class='contentTr' onclick='operaTr(this);'>";
						str +="<td style='width:22px;'><input class='cbxMsg' type='checkbox' value='"+val.id+"' /></td>";
						str +="<td style='width:71px;'>"+val.bisMsgType.typeName+"</td>";
						str +="<td style='width:202px;' class='first_td'>";
						str +="<div class='long_td_div' style='width:198px;'>";
						if(val.link==null || val.link==""){
							str +="<a href='#'>"+val.title+"</a>";
						}else{
							str +="<a target='_blank' href='/ops/"+val.link+"' title='"+val.title+"'>"+val.title+"</a>";
						}
						str +="</div>";
						str +="</td>";
						str +="<td style='width:246px;' class='long_td'><div class='long_td_div' title='"+val.content+"' >"+val.content+"</div></td>";
						str +="<td>"+val.sendTime+"</td>";
	                });
					$("#msgTable").html(str);
					
					//var count = $("#msgCount").val();
					//加载指定的消息数量
					//var liContent = $("#"+msgType).html();
					//liContent = liContent.substring(0, liContent.indexOf("(") + 1) + count + ")";
					//$("#"+msgType).html(liContent);
					
					//对应的消息类型内容层显示
					//$(".messageBox_tabMain").hide();
					//$(".messageBox_tabMain").eq(index).show();
					$("#refreshImg").show();
					$("#refreshText").hide();
					//控制声音
					operaSound();
				},"json");
				
			}
			
			//选择消息类型
			function changeMsgTypeSelect(){
				manageRefresh();
			}
		</script>
	</head>
<body style="overflow: hidden;">
<%--
<s:if test="newTaskMsgCount > 0 || overTimeMsgCount > 0 || hastenMsgCount > 0">
	<bgsound id="bgsoundForIE" src="message.mp3" autostart="true" loop="-1" volume="-500" />
</s:if>
--%>

<input id="hidCtrSoundImgVal" type="hidden" value="openSound" />
<div id="messageBox_container">
		<%--
        <div class="messageBox_top">
            <div class="messageBox_top_middle">
            	<div style="margin-top:6px; float:left;">我的消息盒 <img id="soundImg" src="image/sound.png" style="margin-top: -8px;position: relative;top: 5px;" onclick="manageSoundControl(this);"  /></div> 
            	<div class="messageBox_close" onclick="messageBox_hide()"></div>   
            </div>
        </div>
        --%>
        
        <div class="messageBox_main" style="padding:none;">
            <div class="messageBox_tabUl">
            	<select id="msgType" onchange="changeMsgTypeSelect();">
            		<option value="all">消息类型(全部)</option>
            		<s:iterator id="msgType" value="bizMsgMap.msgTypeList">
            			<option value="<s:property value='#msgType.typeKey' />"><s:property value="#msgType.typeName" /></option>
            		</s:iterator>
            	</select>
                <%-- <ul class="message_kind">
                    <li id="newTask" class="showedLi">新工作(<s:property value="bizMsgMap.newTaskCount" />)</li>
                    <li id="overTime">快超时(<s:property value="bizMsgMap.overTimeCount" />)</li>
                    <li id="hasten">催办(<s:property value="bizMsgMap.hastenCount" />)</li>
                    <li id="note">公告(<s:property value="bizMsgMap.noteCount" />)</li>
                    <li id="msg">信息(0)</li>
                </ul> --%>
                <span class="toolBar">
                	<input id="cbxReadStateId" type="checkbox" checked="checked" class="cbxReadState" value="noRead" /><span>未读</span>&nbsp;<input type="checkbox" class="cbxReadState" value="hasRead" /><span>已读</span>
                	<%-- 装载音乐播放器 --%>
                	<span id="sounddl" style="visibility:hidden;position:absolute;display:none;">message.mp3</span>
                </span>
                <span style="left: 5px;margin-left: 117px;margin-top: 204px;position: absolute;z-index: 1;" >
               		<img id="soundImg" src="images/sound.png" onclick="manageSoundControl();" />
               		<img id="refreshImg" src="images/refresh.png" onclick="manageRefresh();"; />
               		<em id="refreshText" style="display:none;top:-5px;position: relative;">下载中...</em>
                </span>
            </div>
            
            <div class="messageBox_tabMain">
            	<table style="width:692px; margin-left:3px;">
                	<tr>	
                		<th style="width:22px;"></th>
                		<th style="width:71px;">消息类型</th>
                        <th style="width:202px;">消息来源</th>
                        <th style="width:246px">消息内容</th>
                        <th>发送时间</th>
                    </tr>
                </table>
                <div style="overflow:auto; overflow-x:hidden; height:144px;" >
               		<table id="msgTable" style=" margin-top:-1px; text-align:center;">
                    	<s:iterator id="msg" value="bizMsgMap.allMsgList">
                    		<tr class="contentTr" onclick="operaTr(this);">
	                             <td style="width:20px;"><input class="cbxMsg" type="checkbox" value='<s:property value="#msg.id" />' /></td>
	                             <td style="width:69px;"><s:property value="#msg.bisMsgType.typeName" /></td>
	                             <td style="width:200px;" class="first_td">
	                             	<div class="long_td_div" style="width:198px;">
	                             	<s:if test="#msg.link == null">
	                             		<a href="#"><s:property value="#msg.title" /></a>
	                             	</s:if>
	                             	<s:else>
	                             		<a target="_blank" href="/ops/<s:property value="#msg.link" />" title="<s:property value="#msg.title" />"><s:property value="#msg.title" /></a>
	                             	</s:else>
	                             	</div>
	                             </td>
	                             <td style="width:244px;" class="long_td"><div class="long_td_div" title="<s:property value="#msg.content" />" ><s:property value="#msg.content" /></div></td>
	                             <td><s:date name="#msg.sendTime" format="yyyy-MM-dd HH:mm" /></td>
	                         </tr>
                    	</s:iterator>
                    </table>
                </div>
               	<div class="messageBox_tabMain_bottom">
                	<input class="cbxCheckAll" type="checkbox" />&nbsp;全选&nbsp;<input class="btnDeleteMsg" type="button" value="删除" />
                	
                	<span style="position:absolute;right:10px;">
                		<%-- <input type="button" value="发送消息" /> --%>
	                </span>
                </div>
            </div>
           
            <div class="messageBox_tabMain" style="display:none;">

            </div>
            <div class="messageBox_tabMain" style="display:none;">

            </div>
            <div class="messageBox_tabMain" style="display:none;">

            </div>
            <div class="messageBox_tabMain" style="display:none;">

            </div>
        </div>
    </div>
    
<iframe style="border:none;visibility:hidden;" id="soundIFrame" width="1" height="1"></iframe>
</body>
</html>
