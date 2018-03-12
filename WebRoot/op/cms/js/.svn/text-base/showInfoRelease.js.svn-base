

var hasLoadTab="";

$(function(){
	pagingColumnByForeground("span_paging",$(".tab_tr0"),10);
	$(".tab_menu ul li").each(function(index){
		$(this).click(function(){
			var eleId=$(this).attr("id");
			var liObj = $(this);
			$(".tab_menu ul li").removeClass("selected");
			$(this).addClass("selected");
			//if(hasLoadTab!=eleId){
				//根据当前选中的tab，ajax加载消息内容
				getCmsByAjax(liObj,index);
			//}
			hasLoadTab=eleId;
		});
	});
	
	
	//全选/反选
	$("#ckall").live("click", function(){
		var flag=$(this).get(0).checked;
		var allCheck=$(".input_checkbox");
		for (var i = 0; i < allCheck.length; i++) {
			//alert($(allCheck[i]).parent().parent().css("display"));
			if($(allCheck[i]).parent().parent().css("display") != "none"){
			   allCheck[i].checked = flag;
			}
		}
	});
	
	//删除
	$("#btnDelete").click(function(){
		var allCheck=$(".input_checkbox:checked");	//获取选中的记录
		if(allCheck.length == 0) {
			alert("请选择需要删除的记录!");
			return false;
		}
		
		if(!confirm("确定要删除这些记录吗?")) {
			return false;
		}
		
		//拼接所需要删除的信息的id
		var ids = "";
		allCheck.each(function(){
			ids += $(this).val() + "-";
		});
		ids = ids.substring(0, ids.length - 1);
		
		//批量删除消息
		$.post("deleteInfoReleaseAction", {ids:ids}, function(data){
			if(data.flag == "success") {
				//删除成功后，js动态删除选中的消息行
				var deleteCount = 0;
				allCheck.each(function(){
					$(this).parent().parent().remove();
					deleteCount++;
				});
				//加载指定的消息数量
				$("#waitAudit").html("待审核(<em>"+data.tabTotal.waitAudit+"</em>)");
				$("#latestRelease").html("最近发布(<em>"+data.tabTotal.latestRelease+"</em>)");
				$("#draftByMe").html("由我拟稿(<em>"+data.tabTotal.draftByMe+"</em>)");
				$("#allInfo").html("全部信息(<em>"+data.tabTotal.allInfo+"</em>)");
				$("#overReleaseTime").html("最近已过期(<em>"+data.tabTotal.overReleaseTime+"</em>)");
				//$(".tab_menu ul li").each(function(index){
				//	var liContent = $(this).html();
					//先获取删除前的数量
				//	var beforeDeleteCount = parseInt(liContent.substring(liContent.indexOf("(<em>") + 5, liContent.indexOf("</em>)")));
					//获取删除后的数量
				//	var afterDeleteCount = beforeDeleteCount - deleteCount;
					//console.log("afterDeleteCount=="+parseInt(afterDeleteCount));
				//	if(parseInt(afterDeleteCount)<=0){
				//		afterDeleteCount=0;
				//	}
				//	liContent = liContent.substring(0, liContent.indexOf("(") + 1) + "<em>" + afterDeleteCount + "</em>)";
					//alert(liContent);
				//	$(this).html(liContent);
				//	if($(this).attr("class") == "selected"){
				//		$(this).click();
				//	}
				//});
				
			}
		},'json');
	});
	
	
	//新建信息
	$(".cms_layer_title").click(function(){
		location.href="loadAddInfoItemPageAction";
	})
})

function copyInfoItem(){
	var allCheck=$(".input_checkbox:checked");	//获取选中的记录
		if(allCheck.length == 0) {
			alert("请选择需要复制的记录!");
			return false;
		}
		
		//拼接所需要删除的信息的id
		var i = 0;
		allCheck.each(function(){
			i++;
		});
		if(i > 1) {
			alert("请选择需要一条复制的记录!");
			return false;
		}
		//alert(allCheck.val());
		$.post("loadAddInfoItemPageAction",{copyInfoItem:"copyInfoItem",infoItemId:allCheck.next().val()},function(data){
			//alert(data);
			$("#right_content").html(data);
		});
}


//根据当前选中的tab，ajax加载消息内容
function getCmsByAjax(liObj,index) {
	//通过id获取需要加载的消息的类型
	var tabType = liObj.attr("id");
	var ind = index;
	//根据tab类型，AJAX加载cms公告信息
	$.post("loadInfoReleaseByTypeForAjaxAction",{tabType: tabType},function(data){
		var content = "<table class=\"main_table\"><tr>"
		if($(".selected").text().indexOf("待审核") >= 0 || $(".selected").text().indexOf("由我拟稿") >= 0 ){
			content = content
              +"<th style=\"width:45px;\"><input id=\"ckall\" type=\"checkbox\" class=\"\" /></th>"
              +"<th>编号</th><th>类型</th><th>标题</th>";
              if($(".selected").text().indexOf("待审核") >= 0 ){
	              content = content + "<th>拟稿人</th>";
              }
              content = content +"<th>重要级别</th>";
              if($(".selected").text().indexOf("由我拟稿") >= 0 ){
	              content = content + "<th>状态</th>";
              }
             content = content +"<th>紧急程度</th><th>拟稿时间</th></tr>";
		if(data && data.cmsAjaxList){
			//加载指定的cms内容
			$.each(data.cmsAjaxList, function(index, obj){
							if(obj.infoId == null){
								content += "<tr class='tab_tr"+ind+"'><td ><input type='checkbox' class='input_checkbox' value='"+obj.id+"' /><input type='hidden' value='"+obj.id+"' /></td>";
								content += "<td>"+obj.id+"</td>";
							}else{
								content += "<tr class='tab_tr"+ind+"'><td ><input type='checkbox' class='input_checkbox' value='"+obj.infoId+"' /><input type='hidden' value='"+obj.infoId+"' /></td>";
								content += "<td>"+obj.infoId+"</td>";
							}
	                      content +=  "<td>"+handleDataShow(obj.category)+"</td><td  class='wb'><a onclick=\"loadApproveInfoReleasePage('"+obj.id+"','"+obj.infoType+"','"+obj.status+"')\" href='#'>";
	            if(obj.title){
	                 content+= handleDataShow(obj.title) + "</a></td>";
	            }else{
	            	content+= "无标题" + "</a></td>";
	            }
	            		if($(".selected").text().indexOf("由我拟稿") >= 0 ){
				              content = content +"<td  class='tispTd'><span class='tips'>";
			              }else{
		                     content = content +"<td>"+handleDataShow(obj.name)+"</td><td  class='tispTd'><span class='tips'>";
			              }
				if(handleDataShow(obj.importancelevel) == '低'){
					content = content + "<span class='tips_c4'></span>";
				}else if(handleDataShow(obj.importancelevel) == '普通'){
					content = content + "<span class='tips_c3'></span>";
				}if(handleDataShow(obj.importancelevel) == '较高'){
					content = content + "<span class='tips_c2'></span>";
				}if(handleDataShow(obj.importancelevel) == '最高'){
					content = content + "<span class='tips_c1'></span>";
				}else{
					content = content + "<span></span>";
				}
	            content = content +handleDataShow(obj.importancelevel)+"</span></td>";
	                      if($(".selected").text().indexOf("待审核") >= 0 ){
				              content = content +"<td  class='tispTd'><span class='tips'>";
			              }else{
		                     content = content +"<td>"+handleDataShow(obj.statusName)+"</td><td  class='tispTd'><span class='tips'>";
			              }
	                     
						if(handleDataShow(obj.timeSensibility) == '无所谓'){
					content = content + "<span class='tips_c4'></span>";
				}else if(handleDataShow(obj.timeSensibility) == '普通'){
					content = content + "<span class='tips_c3'></span>";
				}if(handleDataShow(obj.timeSensibility) == '较急'){
					content = content + "<span class='tips_c2'></span>";
				}if(handleDataShow(obj.timeSensibility) == '紧急'){
					content = content + "<span class='tips_c1'></span>";
				}else{
					content = content + "<span></span>";
				}
	                      content = content +handleDataShow(obj.timeSensibility)+"</span></td>";
	             content = content + "<td>"+handleDataShow(obj.drafttimeString)+"</td>"; 
	                      content = content 
	                      + "</tr>";
			});
			content +="</table>";
		}
			$(".tab_content").html("");
			$("#infoList_Div").html(content);
			pagingColumnByForeground("span_paging",$(".tab_tr"+ind),10);
			//加载指定的消息数量
			var liContent = liObj.html();
			liContent = liContent.substring(0, liContent.indexOf("(") + 1) + "<em>" + data.count + "</em>)";
			liObj.html(liContent);
			
			//对应的消息类型内容层显示
			$(".tab_content").hide();
			$("#infoList_Div").show();
		}
		if($(".selected").text().indexOf("最近发布") >= 0 || $(".selected").text().indexOf("全部信息") >= 0 || $(".selected").text().indexOf("最近已过期") >= 0){
			content = content
              +"<th style=\"width:45px;\"><input id=\"ckall\" type=\"checkbox\" class=\"\" /></th>"
              +"<th>编号</th><th>类型</th><th>标题</th><th>拟稿人</th>";
              content = content +"<th>重要级别</th>";
              if($(".selected").text().indexOf("全部信息") >= 0 ){
	              content = content + "<th>状态</th>";
              }
             content = content +"<th>紧急程度</th><th>发布时间</th></tr>";
		if(data && data.cmsAjaxList){
			//加载指定的cms内容
			$.each(data.cmsAjaxList, function(index, obj){
				if(obj.infoId == null){
								content += "<tr class='tab_tr"+ind+"'><td ><input type='checkbox' class='input_checkbox' value='"+obj.id+"' /><input type='hidden' value='"+obj.id+"' /></td>";
								content += "<td>"+obj.id+"</td>";
							}else{
								content += "<tr class='tab_tr"+ind+"'><td ><input type='checkbox' class='input_checkbox' value='"+obj.infoId+"' /><input type='hidden' value='"+obj.infoId+"' /></td>";
								content += "<td>"+obj.infoId+"</td>";
							}
	                      content +=  "<td>"+handleDataShow(obj.category)+"</td><td  class='wb'><a onclick=\"loadApproveInfoReleasePage('"+obj.id+"','"+obj.infoType+"','"+obj.status+"')\" href='#'";
	            if(obj.title){
	                 content+= "title='"+handleDataShow(obj.title)+"' >" + handleDataShow(obj.title) + "</a></td>";
	            }else{
	            	content+= "title='无标题' >" + "无标题" + "</a></td>";
	            }
	            			content +="<td>"+handleDataShow(obj.name)+"</td><td  class='tispTd'><span class='tips'>";
	                      
	           
				if(handleDataShow(obj.importancelevel) == '低'){
					content = content + "<span class='tips_c4'></span>";
				}else if(handleDataShow(obj.importancelevel) == '普通'){
					content = content + "<span class='tips_c3'></span>";
				}if(handleDataShow(obj.importancelevel) == '较高'){
					content = content + "<span class='tips_c2'></span>";
				}if(handleDataShow(obj.importancelevel) == '最高'){
					content = content + "<span class='tips_c1'></span>";
				}else{
					content = content + "<span></span>";
				}
	            content = content +handleDataShow(obj.importancelevel)+"</span></td>";
	                      if($(".selected").text().indexOf("全部信息") >= 0 ){
		                     content = content +"<td>"+handleDataShow(obj.statusName)+"</td><td  class='tispTd'><span class='tips'>";
			              }else{
				             content = content +"<td  class='tispTd'><span class='tips'>";
			              }
	                     
						if(handleDataShow(obj.timeSensibility) == '无所谓'){
					content = content + "<span class='tips_c4'></span>";
				}else if(handleDataShow(obj.timeSensibility) == '普通'){
					content = content + "<span class='tips_c3'></span>";
				}if(handleDataShow(obj.timeSensibility) == '较急'){
					content = content + "<span class='tips_c2'></span>";
				}if(handleDataShow(obj.timeSensibility) == '紧急'){
					content = content + "<span class='tips_c1'></span>";
				}else{
					content = content + "<span></span>";
				}
	                      content = content +handleDataShow(obj.timeSensibility)+"</span></td>";
	             content = content + "<td>"+handleDataShow(obj.lastModifiedTimeString)+"</td>"; 
	                      content = content 
	                      + "</tr>";
			});
			content +="</table>";
		}
			$(".tab_content").html("");
			$("#infoList_Div").html(content);
			pagingColumnByForeground("span_paging",$(".tab_tr"+ind),10);
			//加载指定的消息数量
			var liContent = liObj.html();
			liContent = liContent.substring(0, liContent.indexOf("(") + 1) + "<em>" + data.count + "</em>)";
			liObj.html(liContent);
			
			//对应的消息类型内容层显示
			$(".tab_content").hide();
			$("#infoList_Div").show();
		}
		
	},'json');
}

//跳转审核信息页面
function goToApprovePage(infoReleaseId){
	location.href="loadApproveInfoReleasePageAction?infoReleaseId="+infoReleaseId;
}

function handleDataShow(data){
	if(!data){
		data="";
	}
	return data;
}


function loadApproveInfoReleasePage(id,infoType,status){
	var tabType =  $("#infoList_tab .selected").attr("id");
	if(infoType == "sms"){
		$.post("loadInfoItemSMSPageAction",{infoReleaseId:id,tabType:tabType},function(data){
			$("#right_content").html(data);
		});
	}else{
		if(status != 20){
			$.post("loadApproveInfoReleasePageAction",{infoReleaseId:id,infoType:infoType,tabType:tabType},function(data){
				$("#right_content").html(data);
			});
		}else{
			$.post("loadInfoItemPageAction",{infoItemId:id,tabType:tabType},function(data){
				$("#right_content").html(data);
			});
		}
	}
}
