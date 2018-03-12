// JavaScript Document
var queryConditions ={};	    		//查询条件
queryConditions.staffName="";			//人员姓名
queryConditions.orgId="";  	//所属区域id
queryConditions.skillId="";    			//技能Id
queryConditions.skillGrade="";			//技能级别
queryConditions.skillYear = "";			//技能年限
queryConditions.staffSex = "";			//人员性别
queryConditions.contactPhone = "";		//联系方式
$(function(){
	//组织架构树
	searchProviderOrgTree();
	//默认获取全部人员信息
	queryConditions.orgId="all";
	$.ajax({ 
       type : "post", 
       url : "getStaffListByConditionsActionForStaffSkill",
       data : queryConditions, 
       async : false,
       dataType : "json", 
       success : function($data){ 
       		var res = $data;
	      	showStaffInfoTable(res);			      	
       } 
   	});
   	$("#orgSelectButton1").click(function(){
		if($("#treeDiv1").css("display") == "none"){
			$("#treeDiv1").show();
		}else{
			$("#treeDiv1").hide();
		}
	});
	$("#orgSelectButton2").click(function(){
		if($("#treeDiv2").css("display") == "none"){
			$("#treeDiv2").show();
		}else{
			$("#treeDiv2").hide();
		}
	});
   	//获取所有技能信息
   	getAllSkillInfo();
   	
	//添加技能
	$("#addSkillButton").click(function(){
		var staffId = $("#staffIdHidden").val();
		var skillId = $("#addSkillType").val();
		var skillGrade = $("#addSkillGrade").val();
		var skillYear = $("#addSkillYear").val();
		if(skillYear==""||(isNaN(skillYear)|| !/^[0-9]*[1-9][0-9]*$/.test(skillYear))){//yuan.yw 技能年限只能输入大于零的正整数
			alert("请输入正确的技能年限");
			return false;
		}
		$.ajax({ 
	       type : "post", 
	       url : "addStaffSkillAction",
	       data : {staffId:staffId,skillId:skillId,skillGrade:skillGrade,experienceYear:skillYear}, 
	       async : false,
	       dataType : "json", 
	       success : function($data){ 
	       		var res = $data;
		      	if(res){
					showStaffSkillInfoTable(res,staffId);			      	
	       		}			      	
	       } 
	   	});
	});
	//普通搜索事件
	$("#simpleQueryButton").click(function(){
		var staffName = $("#staffName").val();
		if(staffName=='人员搜索'){
			staffName="";
		}
		var orgId = $("#bizunitIdText").val();
		queryConditions.staffName=staffName;			
		queryConditions.orgId=orgId;  	
		queryConditions.skillId="";    			
		queryConditions.skillGrade="";			
		queryConditions.skillYear = "";			
		queryConditions.staffSex = "";			
		queryConditions.contactPhone = "";		
		$.ajax({ 
	       type : "post", 
	       url : "getStaffListByConditionsActionForStaffSkill",
	       data : queryConditions, 
	       async : false,
	       dataType : "json", 
	       success : function($data){ 
	       		var res = $data;
		      	if(res){
		      		showStaffInfoTable(res);
		      	}       	
	       } 
	   	});
	});
	//高级搜索事件
	$("#multiSearchButton").click(function(){
		var staffName = $("#queryStaffName").val();
		var orgId = $("#queryBizunitIdText").val();
		var skillId = $("#querySkillType").val();
		var skillGrade = $("#querySkillGrade").val();
		var skillYear = $("#querySkillYear").val();
		var staffSex = $(':radio[name="staffSex"]:checked').val(); 
		var contactPhone = $("#queryContactPhone").val();
		
		queryConditions.staffName=staffName;			
		queryConditions.orgId=orgId;  	
		queryConditions.skillId=skillId;    			
		queryConditions.skillGrade=skillGrade;			
		queryConditions.skillYear = skillYear;			
		queryConditions.staffSex = staffSex;			
		queryConditions.contactPhone = contactPhone;		
		$.ajax({ 
	       type : "post", 
	       url : "getStaffListByConditionsActionForStaffSkill",
	       data : queryConditions, 
	       async : false,
	       dataType : "json", 
	       success : function($data){ 
	       		var res = $data;
		      	if(res){
		      		showStaffInfoTable(res);
		      	}     	
	       } 
	   	});
	});
})
/*获取所有技能信息*/
function getAllSkillInfo(){
	$.ajax({ 
       type : "post", 
       url : "getAllSkillInfoAction",
       async : false,
       dataType : "json", 
       success : function($data){ 
       		var res = $data;
	      	if(res){
		      	for(var i=0;i<res.length;i++){
		      		var sk = res[i];
		      		var op = $("<option value='"+sk.id+"'>"+sk.skillType+"</option>");
		      		op.appendTo($("#querySkillType"));
		      		var op2 = $("<option value='"+sk.id+"'>"+sk.skillType+"</option>");
		      		op2.appendTo($("#addSkillType"));
		      	} 	
	      	}
       } 
   	});
}

/*生成人员信息Table*/
function showStaffInfoTable(data){
	var staffList = data;
	var table = $("#staffListTable");
	table.html("");
	var thHtml = "<tr><th>人员帐号</th><th>人员姓名</th><th>技能描述</th><th>操作</th></tr>";
	var th = $(thHtml);
	th.appendTo(table);
	//数据为空，则清空表格
	if(!data){
		return ; //参数过滤
	}
	//循环生成数据
	for(var i=0;i<staffList.length;i++){
		var sk = staffList[i];
		if(!sk)continue;
		var tr = $("<tr class='pageTr'></tr>");
		var staffId = sk.account;
		var staffName = sk.name;	
		var staffSkillList = sk.staffSkillList;
		
		if(!staffName){staffName=""};
		
		var staffIdTd = $("<td><a href='showStaffInfo.jsp?staffId="+staffId+"' target='_blank'>"+staffId+"</a></td>");
		var staffNameTd = $("<td><span class='people'><a href='showStaffInfo.jsp?staffId="+staffId+"' target='_blank'>"+staffName+"</a></span></td>");
		var staffSkillStr = "<td><span style='height:20px;' staffid='"+staffId+"' class='staffSkill'><a href='#'>";
		if(staffSkillList && staffSkillList.length>0){
			for(var k=0;k<staffSkillList.length;k++){
				var ss = staffSkillList[k];
				if(k>2){
					if(k==3){
						staffSkillStr+=".....";
					}
				}else{
					staffSkillStr+=ss.skillType+" "+ss.skillGrade+" | ";
				}
			}
			staffSkillStr = staffSkillStr.substring(0,staffSkillStr.length-2);
		}
		staffSkillStr += "</a></span></td>";
		var staffSkillTd = $(staffSkillStr);
		var operation = $("<td><a href='#' class='show_modify_skill' staffid='"+staffId+"' staffname='"+staffName+"'>编辑人员技能</a></td>");
		
		staffIdTd.appendTo(tr);
		staffNameTd.appendTo(tr);
		staffSkillTd.appendTo(tr);
		operation.appendTo(tr);
		tr.appendTo(table);
	}
	editStaffSkillEvent();
	//分页
	pagingColumnByForeground("pageContent",$(".pageTr"),10);
	$(".staffSkill").each(function(){
		var staffId = $(this).attr("staffid");
		$(this).click(function(event){
			$.ajax({ 
		       type : "post", 
		       url : "getStaffSkillInfoByStaffIdAction",
		       data : {staffId:staffId}, 
		       async : false,
		       dataType : "json", 
		       success : function($data){ 
		       		var staffSkillList = $data;
		       		if(staffSkillList){
						var tableDom = $("#mouseStaffSkillTable");
						tableDom.html("");
						var thHtml = "<tr><th>技能名称</th><th>技能级别</th><th>技能年限</th></tr>";
						var th = $(thHtml);
						th.appendTo(tableDom);
						//循环生成数据
						for(var i=0;i<staffSkillList.length;i++){
							var sk = staffSkillList[i];
							if(!sk)continue;
							var tr = $("<tr></tr>");
							var skillType = sk.skillType;
							var skillGrade = sk.skillGrade;	
							var experienceYear= sk.experienceYear;
							
							
							if(!skillType){skillType=""};
							if(!skillGrade){skillGrade=""};
							if(!experienceYear){experienceYear=""};
							
							skillType = $("<td>"+skillType+"</td>");
							skillGrade = $("<td>"+skillGrade+"</td>");
							experienceYear = $("<td>"+experienceYear+"</td>");
							
							skillType.appendTo(tr);
							skillGrade.appendTo(tr);
							experienceYear.appendTo(tr);
							tr.appendTo(tableDom);
						}
		       		}
		       } 
		   	});
			$("#dialog-pop").show();
			x=event.clientX;
			y=event.clientY;
			$("#dialog-pop").css("left",x + "px");
			$("#dialog-pop").css("top",y + 10 + "px");
			
			$("#dialog-pop").hover(function(){},function(){
				$(this).hide();
			});
		});
	});
}
/*编辑人员技能响应事件*/
function editStaffSkillEvent(){
	$(".show_modify_skill").each(function(){
		$(this).click(function(event){
			var staffId = $(this).attr("staffid");
			var staffName = $(this).attr("staffname");
			$("#staffNameSpan").html(staffName);
			$("#staffIdHidden").val(staffId);
			$.ajax({ 
		       type : "post", 
		       url : "getStaffSkillInfoByStaffIdAction",
		       data : {staffId:staffId}, 
		       async : false,
		       dataType : "json", 
		       success : function($data){ 
		       		var res = $data;
			      	showStaffSkillInfoTable(res,staffId);	      	
		       } 
		   	});
			$(".modify_skill").show();
			x=event.clientX;
			y=event.clientY;
			$(".modify_skill").css("left",x - 400 + "px");
			$(".modify_skill").css("top",y - 10 + "px");
		})	
	})
}
/*生成人员技能Table(信息窗口)*/
function showStaffSkillInfoTable(data,staffId){
	var staffSkillList = data;
	var listViewDom = $("#staffSkillDiv");
	var tableHtml = "<table class='alert_div_table tc' id='staffSkillTable'></table>";
	var table = $(tableHtml);
	var thHtml = "<tr><th>技能名称</th><th>技能级别</th><th>技能年限</th><th>操作</th></tr>";
	var th = $(thHtml);
	th.appendTo(table);
	//数据为空，则清空表格
	if(!data){
		listViewDom.html("");
		table.appendTo(listViewDom);
		return ; //参数过滤
	}
	//循环生成数据
	var skillStr = "";
	for(var i=0;i<staffSkillList.length;i++){
		var sk = staffSkillList[i];
		if(!sk)continue;
		var tr = $("<tr></tr>");
		var skillType = sk.skillType;
		var skillGrade = sk.skillGrade;	
		var experienceYear= sk.experienceYear;
		var skillId = sk.id;
		var staffSkillId = sk.staffSkillId;
		
		if(i>2){
			if(i==3){
				skillStr+=".....";
			}
		}else{
			skillStr+=skillType+" "+skillGrade+" | ";
		}
		
		if(!skillType){skillType=""};
		if(!skillGrade){skillGrade=""};
		if(!experienceYear){experienceYear=""};
		
		skillType = $("<td>"+skillType+"</td>");
		skillGrade = $("<td>"+skillGrade+"</td>");
		experienceYear = $("<td>"+experienceYear+"</td>");
		var operation = $("<td><a href='#' onclick=\"delStaffSkill('"+staffId+"','"+staffSkillId+"')\" >删除</a></td>");
		
		skillType.appendTo(tr);
		skillGrade.appendTo(tr);
		experienceYear.appendTo(tr);
		operation.appendTo(tr);
		tr.appendTo(table);
	}
	listViewDom.html("");
	table.appendTo(listViewDom);
	//联动更新技能信息
	skillStr = skillStr.substring(0,skillStr.length-2);
	$(".staffSkill").each(function(){
		var sid = $(this).attr("staffid");
		if(staffId == sid){
			$(this).html("<a href='#'>"+skillStr+"</a>");
		}
	});
}
/*删除人员技能*/
function delStaffSkill(staffId,staffSkillId){
	$.ajax({ 
       type : "post", 
       url : "delStaffSkillByIdAction",
       data : {staffId:staffId,staffSkillId:staffSkillId}, 
       async : false,
       dataType : "json", 
       success : function($data){ 
	      	var res = $data;
	      	if(res){
				showStaffSkillInfoTable(res,staffId);		
       		}
       } 
   	});
}
//打开人员信息页面
function openStaffInfo(){
	var staffId = $("#staffIdHidden").val();
	window.open("showStaffInfo.jsp?staffId="+staffId);
}
//生成服务商的组织架构树
function searchProviderOrgTree(){
	var orgId = "16";
	$.ajax({
		"url" : "cardispatchForeign_ajax!getLoginUserBiz.action" , 
		"type" : "post" , 
		"async" : true , 
		"success" : function ( data ) {
			data = eval( "(" + data + ")" );
			orgId = data.orgId;
			$("#bizunitNameText").val(data.name);
			$("#bizunitIdText").val(data.orgId);
			$("#queryBizunitNameText").val(data.name);
			$("#queryBizunitIdText").val(data.orgId);
			if(orgId==null||orgId==""){
				orgId="16";
			}
			var values = {"orgId":orgId}
			var myUrl = "../organization/getProviderOrgTreeByOrgIdAction";
			$.post(myUrl,values,function(data){
				createOrgTreeOpenFirstNode(data,"treeDiv1","tree1","a","searchOrgTreeClick");
				createOrgTreeOpenFirstNode(data,"treeDiv2","tree2","b","searchOrgTreeClick2");
			},"json");
		}    
	});
	$("#bizunitNameText").click(function(){
		$("#treeDiv1").css("display","block");
	});
	$("#treeDiv1").hover(function(){},function(){
		$(this).slideUp("fast");
	});
	$("#queryBizunitNameText").click(function(){
		$("#treeDiv2").css("display","block");
	});
	$("#treeDiv2").hover(function(){},function(){
		$(this).slideUp("fast");
	});
}
//显示服务商的组织信息
function searchOrgTreeClick(dataStr,tableId){
	var data = eval( "(" + dataStr + ")" ) ;
	var orgId = data.orgId;
	$("#bizunitNameText").val(data.name);
	$("#bizunitIdText").val(data.orgId);
	$("#treeDiv1").slideUp("fast");
}
function searchOrgTreeClick2(dataStr,tableId){
	var data = eval( "(" + dataStr + ")" ) ;
	var orgId = data.orgId;
	$("#queryBizunitNameText").val(data.name);
	$("#queryBizunitIdText").val(data.orgId);
	$("#treeDiv2").slideUp("fast");
}
/*张声JS*/
$(document).ready(function(){
		/* 弹出、隐藏高级搜索 框*/
		$(".main-table1 #gaojisousuo").click(function(){
			$(".advanced-search").toggle("fast");
		})
		$(".advanced-search-hide").click(function(){
			$(".advanced-search").toggle("fast");
		})
		
		/* 高级搜索 */
		$("#show_gaoji").click(function(){
			$(".advanced-search2").toggle();
		})
		$(".midify_skill_close").click(function(){
			$(".modify_skill").slideToggle("fast");
		})
	})