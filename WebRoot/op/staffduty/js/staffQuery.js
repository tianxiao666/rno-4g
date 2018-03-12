var map = null;
var markers = new Array();
var currentInfoWindow = null;

var MarkerIconUrl ={"checked":"images/staff2.png","unchecked":"images/staff1.png"};//标记的图标

var queryConditions ={};	    //查询条件
var queryResult = null;
var FrameMap = {"ListView":"ListView","MapView":"MapView"};
var currentFrame = "ListView";    //当前员工信息展现形式(ListView:列表展现形式 ; MapView:地图展现形式)
queryConditions.searchType="all";//查询类别
queryConditions.staffName;      //员工姓名
queryConditions.orgId;  //员工所属区域id
queryConditions.sex;            //员工性别
queryConditions.startDutyTime;	//员工值班开始时间
queryConditions.endDutyTime;    //员工值班结束时间
queryConditions.skillId;        //员工技能id
queryConditions.experienceAge;  //员工技能经验年限

$(function(){
	//人员搜索框
	$("#staffName").focus(function(){
		if($("#staffName").val()=="输入姓名查询"){
			$("#staffName").val("");
		}
	});
	$("#staffName").blur(function(){
		if($("#staffName").val()==""){
			$("#staffName").val("输入姓名查询");
		}
	});
	$("#orgSelectButton").click(function(){
		if($("#treeDiv").css("display") == "none"){
			$("#treeDiv").show();
		}else{
			$("#treeDiv").hide();
		}
	});
	$("#treeDiv").hover(function(){},function(){
		$("#treeDiv").hide();
	});
	//默认获取全部人员信息
	shouListViewByConditions(queryConditions);
	//组织架构树
	searchProviderOrgTree();
	//获取技能信息 
	getAllSkillInfo();
	
	//隐藏人员具体信息Div
	$("#people-information .table-head div").click(function(){
		$("#people-information").hide();
	});
	$(".container-tab2 ul li:eq(0)").css("border-left","1px solid #ccc");	//边框样式
	//显示隐藏样式
	$(".container-tab2 ul li").each(function(){
		$(this).click(function(){
			$(".container-tab2 ul li").removeClass("tab2-li-show");
			$(this).addClass("tab2-li-show");
		})
	})
	//地图、列表切换
	$("#listViewButton").click(function(){
		$("#listView").css("display","block");
		$("#mapView").css("display","none");
		shouListViewByConditions(queryConditions);
	});
	$("#mapViewButton").click(function(){
		$("#listView").css("display","none");
		$("#mapView").css("display","block");
		// 显示地图
		if(map==null){
		    initMap();
		}
		shouMapViewByConditions(queryConditions);	
	});
	//普通查询按钮
	$("#simpleQueryButton").click(function(){
		var staffName=$("#staffName").val();
		var bizUnitAreaId=$("#bizunitIdText").val();
		if(staffName == '输入姓名查询') {
			staffName = "";
		}
		queryConditions.searchType = "simple";
		queryConditions.staffName=staffName;
		queryConditions.orgId=bizUnitAreaId;
		if($("#mapView").css("display") == "none") {
			shouListViewByConditions(queryConditions);
		}else {
			//按条件在地图上展现员工信息
			shouMapViewByConditions(queryConditions);
		}
	});
	/******************** 高级查询 ***********************/
	$("#multiSearch").click(function(){
		$("#black").show();
		$("#search-table").show();
	});
	$("#multiSearchButton").click(function(){
		var selectedSkill = $("#skillSelect").val();
		if(selectedSkill=="请选择"){selectedSkill="";}
		//时间验证
		var beginDate=$("#beginDate").val();
		var endDate=$("#endDate").val();
		if(beginDate!="" && endDate==""){
			alert("请选择值班结束时间");
			return false;
		}
		if(beginDate=="" && endDate!=""){
			alert("请选择值班开始时间");
			return false;
		}
		if(beginDate>endDate){
			alert("值班开始时间不能大于结束时间");
			return false;
		}
		var bizUnitAreaId=$("#bizunitIdText").val();
		var staffSex=$("#staffSex").val();
		var experienceAge = $("#experienceAge").val();
		
		queryConditions.staffName="";
		queryConditions.orgId=bizUnitAreaId;
		queryConditions.sex = staffSex;           
		queryConditions.startDutyTime = beginDate;	
		queryConditions.endDutyTime = endDate;    
		queryConditions.skillId = selectedSkill;        
		queryConditions.experienceAge = experienceAge; 
		queryConditions.searchType = "gaoji"; 
		
		if ($("#mapView").css("display") == "none") {
			shouListViewByConditions(queryConditions);
		} else {
			//按条件在地图上展现员工信息
			shouMapViewByConditions(queryConditions);
		};
		$(".advanced-search").toggle("fast");
	});
	
	/* 点出人员信息框 */
	$(".people").each(function(){
		$(this).click(function(e) {
			  $("#people-information").show();
			  $("#people-information").css("left",e.clientX);
			  $("#people-information").css("top",e.clientY);
		})
	})
	
	$("#people-information .table-head div").click(function(){
		$("#people-information").hide();
	})
	
	/* 选择区域点击弹出组织架构 */
	$("#selectWorkPlaceButton").click(function(){
		$("#treeDiv").toggle("fast");
	})
	
	/* 弹出、隐藏高级搜索 框*/
	$("#gaojisousuo").click(function(){
		$(".advanced-search").toggle("fast");
	})
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
		      		op.appendTo($("#skillSelect"));
		      	} 	
	      	}
       } 
   	});
}
//多条件查询，列表显示
function shouListViewByConditions(queryConditions){
	var queryUrl = "getStaffListByConditionsAction";
	$.ajax( {
		url : queryUrl,
		cache : false,
		data : queryConditions,
		async : true,
		type : "POST",
		dataType : 'json',
		success : function(result) {
			var json = result;
			queryResult = json;
			if(queryResult){
				if(currentFrame==FrameMap.MapView){
					//清除地图上的所有标记
					cleanAllMarkers();
					showStaffListOnMap(queryResult);
				}else{
					showStaffListTable(queryResult);
				}
			}
		}
	});

}
//列表显示查询结果 
function showStaffListTable(data){
	var staffList = data;
	var listViewDom = $("#queryResultTab");
	listViewDom.html("");
	
	var thHtml = "<tr><th><input id=\"allChecked\" type=\"checkbox\" />全选</th><th>人员姓名</th><th>联系电话</th><th>性别</th><th>任务数</th><th>专业技能</th></tr>";
	var th = $(thHtml);
	th.appendTo(listViewDom);
    
	//数据为空，则清空表格
	if(!data||data.length==0){
		listViewDom.html("");
		th.appendTo(listViewDom);
		return ; //参数过滤
	}
	for(var i=0;i<staffList.length;i++){
		var staff = staffList[i];
		if(!staff)continue;
		var tr = $("<tr class=\"pageTr\"></tr>");
		var checkBox = $("<input class=\"checkedPerson\" type=\"checkbox\" >");
		var checkBoxTd = $("<td></td>");
		checkBox.attr("value",staff.staffId);
		checkBox.appendTo(checkBoxTd);
		
		var staffName = staff.name;
		var sex = staff.gender;
		if(sex=='male'){
			sex='男';
		}else if(!sex) {
			sex='';
		}else{
			sex='女';
		}
		var contactPhone = staff.contactPhone;
		var taskCount=staff.taskCount;
		var staffId=staff.account;
		var skillStr = staff.skillStr;
		
		if(!skillStr){skillStr=""};
		if(!staffName){staffName=""};
		if(!sex){
			sex = staff.gender;
			if(sex=='male'){
				sex='男';
			}else if(!sex) {
				sex='';
			}else{
				sex='女';
			}
		};
		if(!contactPhone){contactPhone=staff.mobile;if(!contactPhone){contactPhone=""}};
		
		var staffName = $("<td><a href=\"showStaffInfo.jsp?staffId="+staffId+"\" target=\"_blank\" >"+staffName+"</a></td>");
		var contactPhone = $("<td>"+contactPhone+"</td>");
		var sex = $("<td>"+sex+"</td>");
		var skill = $("<td><span class=\"staffSkill\" staffid=\""+staffId+"\" style=\"height:20px\"><a href=\"#\">"+skillStr+"</a></span></td>");
		var exp = $("<td><a href=\"#\" onclick=\"showStaffGantt('"+staffId+"',this)\">"+taskCount+"</a></td>");
		
		checkBoxTd.appendTo(tr);
		staffName.appendTo(tr);
		contactPhone.appendTo(tr);
		sex.appendTo(tr);
		exp.appendTo(tr);
		skill.appendTo(tr);
		tr.appendTo(listViewDom);
	}
	//全部选中
	allChecked();
	//分页功能
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
//显示人员甘特图
function showStaffGantt(staffId,be){
	createGanttContent("ganttContent","sgContent","sgDatepicker",staffId,"people",null);
	var offset = $(be).offset();
	$("#ganttContent").css("top",offset.top + 20 + "px");
	$("#ganttContent").css("left",offset.left - 270 + "px");
	$("#ganttContent").show();
    $("#ganttContent").hover(function(){},function(){
    	$("#ganttContent").hide();
    });
}
function showResourceMonthGantt(){
	if($(".sgDatepicker").prev().val()){
		var staffId = $(".sgDatepicker").prev().val();
		showResourceMonthGanttByConditions(".sgDatepicker",staffId,"people");
	}
}
//按条件获取月份任务
function showResourceMonthGanttByConditions(content,resourceId,resourceType){
	var year = $(content+" .ui-datepicker-year").html().replace("年","-");
	var month = $(content+" .ui-datepicker-month").html().replace("月","-");
	var taskDate = year+month;
	var endDate = "30";
	$(content+" .ui-datepicker-calendar tbody td a").each(function(){
		endDate = $(this).html();
	});
	$.ajax({
		url : "getResourceMonthTaskAction",
		data : {"taskDate":taskDate,"endDate":endDate,"resourceId":resourceId,"resourceType":resourceType},
		dataType : 'json',
		async:false,
		type : 'POST',
		success : function(result){
			if(result){
				$(content+" .ui-datepicker-calendar tbody td a").each(function(){
					var i = $(this).html();
					var ti = result[i];
					var hasTask = ti.hasTask;
					// 有无任务颜色显示不同，有任务为红色，无任务为绿色
					var cs = "bg_green";
					if(hasTask&&hasTask=='true'){
						cs = "bg_red";
						$(this).attr("class",cs);
					}
				});
			}
		}
	});
}
//显示人员信息框
function showStaffInfo(){
	//显示人员技能详细信息
	$(".people").each(function(){
		$(this).click(function(e){
			var staffId = $(this).attr("id");
			showPersonInfo(staffId);
			$("#people-information").show();
			$("#people-information").css("left",e.clientX);
			$("#people-information").css("top",e.clientY);
		});
	});
	$("#people-information").hover(function(){},function(){
		$("#people-information").hide();
	});
}
//显示人员技能信息框
function showStaffSkillInfo(){
	//显示人员技能详细信息
	$(".staffSkill").each(function(){
		$(this).click(function(e){
			var staffId = $(this).attr("id");
			var url="getStaffSkillInfoByStaffIdActionForAjax";
			$.post(url,{staffId:staffId},function($data){
				var res=$data;
				var queryResult=eval("("+res+")");
				showQueryResultByJson(queryResult);
				
				$("#dialog-pop").css("left",e.clientX);
				$("#dialog-pop").css("top",e.clientY);
				$("#dialog-pop").show();
			});
		});
	});
	$("#dialog-pop").hover(function(){},function(){
		$("#dialog-pop").hide();
	});
}
//动态生成HTML
function showQueryResultByJson(data){
	var skillList = data;
	var listViewDom = $("#staffSkillTable");
	var divHtml = "<table id=\"staffSkillTable\" style=\"margin-left:20px;\"></table>";
	var div = $(divHtml);
	//数据为空，则清空表格
	if(!data||data.length==0){
		listViewDom.html("");
		div.appendTo(listViewDom);
		return ; //参数过滤
	}
	
	for(var i=0;i<skillList.length;i++){
		var sk = skillList[i];
		if(!sk)continue;
		var skillType = sk.skillType;
		var skillGrade=sk.skillGrade;
		var experienceYear = sk.experienceYear;
		
		if(!skillType){skillType=""};
		if(!skillGrade){skillGrade=""};
		if(!experienceYear){experienceYear=""};
		
		var trHtml = $("<tr><td style=\"width:100px;height:20px\">"+skillType+"</td><td style=\"width:50px;height:20px\">"+skillGrade+"</td><td style=\"width:50px;height:20px\">"+experienceYear+"年</td></tr>");
		
		trHtml.appendTo(div);
	}
	listViewDom.html("");
	div.appendTo(listViewDom);
}
//显示用户详细信息框
function showPersonInfo(staffId){
	var url="getStaffInfoByStaffIdActionForAjax";
	var param={staffId:staffId};
	var staffResult;
	$.post(url,param,function($data){
		var res = $data ;
		var staff=eval(res);
		//先清空数据
		$("#stTitle").html("");
		$("#stName").html("");
		$("#stSex").html("");
		$("#stContactPhone").html("");
		$("#stQQNum").html("");
		$("#stEmail").html("");
		
		
		//给页面元素赋值
		$("#stTitle").html(staff.staffName);
		$("#stName").html(staff.staffName);
		$("#stSex").html(staff.sex);
		$("#stContactPhone").html(staff.contactPhone);
		$("#stQQNum").html(staff.qqNumber);
		$("#stEmail").html(staff.email);
		
	},"json");
}
//全选事件
function allChecked(){
	//查询结果的全选事件
	$("#allChecked").click(function(){
		if($("#allChecked").attr("checked") == "checked"){
			$(".checkedPerson").each(function(index){
				$(this).attr("checked","checked");
			});
		}else{
			$(".checkedPerson").each(function(index){
				$(this).removeAttr("checked");
			});
		}
	});
}

//初始化地图
function initMap(){
    var center = new ILatLng(23.14651977,113.34106554);
    map = new IMap("mapView",center,18,{});
    currentInfoWindow = new IInfoWindow("",{"position":center});
}
//工作区自适应
function workspaceSelfAdaption(){
//    var mainDiv = $("#mainDiv");
    var allWidth = $(window).width();
    var allHeight = $(window).height();

    //垂直
    var v1 = $("#vertical_div1");
    var v2 = $("#vertical_div2");
    var v3 = $("#vertical_div3");
    var vh1 = v1.height();
    var vh2 = v2.height();
    v3.height(allHeight-vh1-vh2);  
    
    //水平
    var h1 = $("#horizontal_div1");
    var h2 = $("#horizontal_div2");
    var h3 = $("#horizontal_div3");
    var hw1 = h1.width();
    var hw2 = h2.width();
    h3.width(allWidth-hw1-hw2);
    
    
    $(document).resize();
    
}

/**
 * 按搜索条件展现地图
 * @param {Object} conditions
 */
function shouMapViewByConditions(queryConditions){
	var queryUrl = "getStaffListByConditionsAction";
	$.ajax( {
		url : queryUrl,
		cache : false,
		data : queryConditions,
		async : true,
		type : "POST",
		dataType : 'json',
		success : function(result) {
			var json = result;
			queryResult = json;
			//清除地图上的所有标记
			cleanAllMarkers();
            if(queryResult){
                showStaffListOnMap(queryResult);
            }
		}
	});
}

/**
 * 清除地图上的所有标记
 */
function cleanAllMarkers(){
	for(var i=0;i<markers.length;i++){
		var marker = markers[i];
		marker.textOverlay_.setMap(null);
		marker.textOverlay_ = undefined;
		marker.setMap(null);
	}
	markers = new Array();
}

/**
 * 显示员工列表
 * @param {Object} staffList
 * @return {TypeName} 
 */
function showStaffListOnMap(staffList){
	if(!staffList)return ; //参数过滤
	
	var panFlag = false;  //是否已经移动过
	for(var i=0;i<staffList.length;i++){
		var staff = staffList[i];
		if(!staff)continue;
		var marker = createStaffMarker(staff);
		if(!panFlag&&marker!=null){
			map.panTo(marker.getPosition());
			panFlag=true;
		}
		if(marker){
			marker.bizValues_ = staff;	//将业务信息添加到marker对象中
			bindMarkerEvent(marker);
			marker.setMap(map);
			marker.textOverlay_.setMap(map);
			markers.push(marker);
		}
	}
}

/**
 * 生成员工在地图上显示的图标
 * @param {Object} staff 当前员工信息
 */
function createStaffMarker(staff){
	if(!staff)return null;

	var lat = staff.latitude;  //当前纬度
	var lng = staff.longitude;  //当前经度
//	alert(lat+"  "+lng)
	lat = stringToFloat(""+lat);
	lng = stringToFloat(""+lng);
	if(!lat||!lng)return null;
	//员工所在的的GPS坐标
    var position = new ILatLng(lat,lng);
    //坐标校准
    var position = IMapComm.gpsToMapLatLng(position);
    
    //在地图上添加标记
    var marker = new IMarker(position,staff.name,{});
    var markerOptions = {};
    markerOptions.icon = MarkerIconUrl.unchecked;
    
    marker.setOptions(markerOptions);
    
    var name = staff.name;
    var taskText = "";
	if(staff.h2){
		taskText=",二小时内待办"+staff.h2+"个";
	}
//	var age="";
//	if("age" in staff)
//		age=staff.age;
    var statusText = ""+name+taskText;
    var statusLabel = "<lable style='color: #0000FF;font-size: 12px;font-weight: bold;font-style: italic;'>"+statusText+"</lable>";
    marker.textOverlay_ = new ITextOverlay(position,statusLabel,{});
    
    return marker;
}

/**
 * 将字符串转换成浮点数
 */
function stringToFloat(str){
	var num = null;
	try{
		var re = parseFloat(str);
		num = re;
	}catch(err){
		
	}
	return num;
}

/**
 * 给标记绑定点击事件
 * @param {Object} marker
 */
function bindMarkerEvent(marker){
    var clickType = undefined;
	IMapEvent.addListener(marker, "click", function(){
		clickType = "click";
		setTimeout(function(){
			if(clickType=="click")
			selectMarker(marker);
			showMarkerDetail(marker);
			clickType=undefined;
		},300);
		
	}); 
};

/**
 * 选中/取消选中 当前标记
 * 	判断当前是否已经被选中，如果已选中，则不进行操作
 * @param {Object} marker
 */
function selectMarker(marker){
	if(!marker)return;
	if(!("bizValues_" in marker))return;
	var staff = marker.bizValues_;
	if(!staff)return;
	if(isStaffSelected(staff)){ 
		cancelSelected(staff);
		var options = {};
		options.icon = MarkerIconUrl.unchecked;
		marker.setOptions(options);
	}else{
		selectedStaffArray.push(staff);
		var options = {};
		options.icon = MarkerIconUrl.checked;
		marker.setOptions(options);
	}

};

/**
 * 显示当前员工的详细信息
 * @param {Object} marker
 * @return {TypeName} 
 */
function showMarkerDetail(marker){
	if(!currentInfoWindow)return;
	var position = marker.getPosition();
	var content = "";
	
	if("bizValues_" in marker){
	var staff = marker.bizValues_;
	var staffId = staff.account;
	var staffName = staff.name;
	var sex = staff.sex;
	var age = staff.age
	var contactPhone = staff.contactPhone;
	var cellPhoneNumber = staff.cellPhoneNumber;
	var email = staff.email;
	var qqNumber = staff.qqNumber;
	var description = staff.description;
	var skillStr = staff.skillStr;
	var staffSkill = "";
	if(skillStr&&skillStr!=""){
		staffSkill = skillStr.split("/");
	}
	
	if(!staffName){staffName=""};
	if(!sex){
		sex = staff.gender;
		if(sex=='male'){
			sex='男';
		}else if(!sex) {
			sex='';
		}else{
			sex='女';
		}
	};
	if(!age){age=""}else{age=","+age+"岁"};
	if(!contactPhone){contactPhone=staff.mobile;if(!contactPhone){contactPhone=""}};
	if(!cellPhoneNumber){cellPhoneNumber=""};
	if(!email){email=""};
	if(!qqNumber){qqNumber=""};
	if(!description){description=""};
	
	var phone="";
	if("cellPhoneNumber" in staff){
		phone=staff.cellPhoneNumber;
	}else if("contactPhone" in staff){
		phone=staff.contactPhone;
	}
	
	var content = "<div class=\"dialog-title-bar clearfix\"><div class=\"dialog-bar-m clearfix\"><h3><label>人员：</label><a href=\"../staffduty/showStaffInfo.jsp?staffId="+staffId+"\" target=\"_blank\" class=\"blue\">"+staffName+"</a> &nbsp;</h3><h3><label>性别：</label><span class=\"blue\">"+sex+" &nbsp;</span></h3><h3><label>电话：</label><span>"+contactPhone+" &nbsp;<em class=\"dialog-ji selectEntity\">";
	content+="&nbsp;</em></span></h3></div></div>";
	content+="<div class=\"dialog-content clearfix\"><div class=\"dialog-content-box\"><div class=\"dialog-content-tab\"><ul class=\"dialog-tabmenu clearfix\"><li class=\"on\" onclick=\"tableToggle(this);\" >技能</li><li style=\"border-right:1px solid #ccc;\" onclick=\"tableToggle(this);\">闲忙任务数</li></ul><dl class=\"dialog-tabcontent\" class=\"clearfix\"><dd><ul class=\"dialog-skill\">";
	if(staffSkill!=null){
		for(var i=0;i<staffSkill.length;i++){
			var sk = staffSkill[i];
			content+="<li>"+sk+"</li>";
		}
	}
	var totalTask = staff.taskCount;
	content+="</ul></dd><dd id='hdGantt' style=\"display:none\" ><span><h4 style='width:200px; margin:0 auto;'>任务甘特图<span style='font-weight:normal; color:blue;' >("+totalTask+"个任务)</span></h4></span>";
	content+="</dd>";
	content+="</dl>";
    content+="</div></div></div><div class=\"dd_bottom\"></div>";
	/*content +="<div><h4><a style='text-decoration: underline; color:blue; font-size:18px; font-weight:bold;'>当前待办任务总数"+staff.taskCount+"个</a></h4><div style='float:left; margin-left:10px;padding-left:5px;'>";
    content +=staffName+","+sex+age+"<br/>";
    content +=phone+"<br/>";
    for(var i=0;i<staffSkill.length;i++){
    	content +=staffSkill[i]+"<br/>";
    }
    content +="</div>";
    content +="<div style='float:left; border-left:1px dotted #000; margin-left:5px;padding-left:5px;'><ul><li><a style='text-decoration: underline; color:blue; font-size:12px; font-weight:bold;'>2小时内待办任务 0 个</a></li><li><a style='text-decoration: underline; color:blue; font-size:12px; font-weight:bold;'>2-4小时内待办任务 0 个</a></li><li><a style='text-decoration: underline; color:blue; font-size:12px; font-weight:bold;'>4-8小时内待办任务 0 个</a></li><li><a style='text-decoration: underline; color:blue; font-size:12px; font-weight:bold;'>8小时外待办任务"+staff.taskCount+"个</a></li></ul></div></div>";
	*/
	}
	currentInfoWindow.setContent(content);
	currentInfoWindow.setPosition(position);
	marker.openInfoWindow(currentInfoWindow);
	createGanttContent("hdGantt","staffWindowInfoGanttContent","sgDatepicker",staffId,"people",null);
	
};


/************************收藏夹功能变量和方法**************************/
//已经选择的人员列表
var selectedStaffArray = new Array();
/**
 * 判断当前人员是否已选中
 * @param {Object} staff
 */
function isStaffSelected(staff){
	for(var i=0;i<selectedStaffArray.length;i++){
		var st = selectedStaffArray[i];
		if(st.staffId==staff.staffId){
			return true;
		}
	}
	return false;
}
//将人员选中的人员列表中删除
function cancelSelected(staff){
	for(var j=0;j<selectedStaffArray.length;j++){
		var st = selectedStaffArray[j];
		if(st.staffId == staff.staffId){
			selectedStaffArray.splice(j,1);
		};
	}
}
/**
 * 清除当前已选择的员工列表
 */
function cleanSelectedStaffArray(){
	for(var i=0;i<markers.length;i++){
		var marker = markers[i];
		var opts ={};
		opts.icon = MarkerIconUrl.unchecked;
		marker.setOptions(opts);
	}
	selectedStaffArray=new Array();
};

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
			if(orgId==null||orgId==""){
				orgId="16";
			}
			var values = {"orgId":orgId}
			var myUrl = "../organization/getProviderOrgTreeByOrgIdAction";
			$.post(myUrl,values,function(data){
				createOrgTreeOpenFirstNode(data,"treeDiv","tree1","a","searchOrgTreeClick");
			},"json");
		}    
	});
}
//显示服务商的组织信息
function searchOrgTreeClick(dataStr,tableId){
	var data = eval( "(" + dataStr + ")" ) ;
	var orgId = data.orgId;
	$("#bizunitNameText").val(data.name);
	$("#bizunitIdText").val(data.orgId);
	$("#treeDiv").slideUp("fast");
}

/*table切换*/
function tableToggle(me){
	$(".dialog-tabmenu li").removeClass("on");
  	$(me).addClass("on");
  	$(".dialog-tabcontent dd").hide();
  	var curIndex = $(".dialog-tabmenu li").index($(me));
  	$(me).parent().parent().find($(".dialog-tabcontent")).find("dd").eq(curIndex).show();

}


