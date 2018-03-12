var newPendingWorkOrderCount = 0;
var newPendingTaskOrderCount = 0;
var willTimeoutPendingWorkOrderCount = 0;
var willTimeoutPendingTaskOrderCount = 0;
//保存被选择的菜单
var firstLevelMenuselected = "";
var secondLevelMenuselected = "";

//消息相关
var messageIframe = null;// 装载消息iframe的缓存
var hasNewCount = false;
var preMessageCount = 0;
var isReading = false;// 用户是否打开了消息盒子在阅读
//是否正在加载未读消息
var isGettingNotReadMessage = false;

//正在加载功能标签
var isGettingWorkZoneSite = false;

//main div的高度
var oriMainDivHeight = "auto";

//workzone 的id
var wzIdArray = new Array();
//无奈变量~~
var noeffect = true;

max = 800;

$(document).ready(function() {
	$(".nav_ul li").not(":last").each(function(index) {
		$(this).hover(function() {
			$(".nav_ul_content").hide();
			$(".nav_ul_content").eq(index).stop(true, true).slideDown("fast");
			$(this).addClass("header_nav_on");
		}, function() {
			$(".nav_ul_content").eq(index).stop(true, true).slideUp("fast");
			$(this).removeClass("header_nav_on");
		});
	});

	$(".nav_ul_content").each(function(index) {
		$(this).hover(function() {
			$(this).stop(true, true).show();
			$(".nav_ul li").removeClass("header_nav_on");
			$(".nav_ul li").eq(index).addClass("header_nav_on");
		}, function() {
			$(this).stop(true, true).slideUp("fast");
			$(".nav_ul li").removeClass("header_nav_on");
		});
	});

	$(".nav_ul_content").eq(0).find($("a")).each(function(index) {
		$(this).click(function() {
			$(".main_nav li").removeClass("main_nav_on");
			$(".main_nav li").eq(index).addClass("main_nav_on");
		})
	})
	$(".nav_ul_content").each(function() {
		$(this).find($("a")).each(function() {
			$(this).click(function() {
				$(".nav_ul_content").slideUp("");
			})
		})
	})
	
	//Liang YJ 2014-4-4 13:30 增加
	//鼠标悬停在一级菜单时，显示二级菜单，当鼠标离开时，收起二级菜单
	$("#navigationMenu > li").hover(function (){
		//console.log($(this).text())
		$(this).children("ul").css("display","block");
		if(firstLevelMenuselected != $(this).attr("id")){
			$(this).addClass("firstLevelMenuOn");
		}
		//console.log("进入一级菜单："+$(this).attr("id"));
	},function(){
		$(this).children("ul").css("display","none");
		if(firstLevelMenuselected != $(this).attr("id")){
			$(this).removeClass("firstLevelMenuOn");
		}
		//console.log("退出一级菜单："+$(this).attr("id"));
		//console.log("退出一级菜单后的样式："+$(this).attr("class"));
	});
	
	//鼠标悬停在二级菜单时改变菜单的样式，提高用户体验
	$("#navigationMenu > li > ul >li").hover(function (){
		if($(this).attr("id") != secondLevelMenuselected){
			$(this).addClass("secondLevelMenuOn");
		}
	},function (){
		if($(this).attr("id") != secondLevelMenuselected){
			$(this).removeClass("secondLevelMenuOn");		
		}
	});
	
	//鼠标点击二级菜单，窗口加载相应的模块
	$("#navigationMenu > li > ul >li").click(function (){
		var href = $(this).children("input").val();
		$("#maininnerdivif").attr("src",href);
		//删除原来选中的菜单的选中样式
		$("#"+secondLevelMenuselected).removeClass("secondLevelMenuselected");
		//console.log("之前被选中的二级菜单的class:"+$("#"+secondLevelMenuselected).attr("class"));
		$("#"+firstLevelMenuselected).removeClass("firstLevelMenuselected");
		//console.log("之前被选中的一级级菜单的class:"+$("#"+firstLevelMenuselected).attr("class"));
		//在选中的菜单添加选中样式
		$(this).removeClass("secondLevelMenuOn");
		$(this).addClass("secondLevelMenuselected");
		//console.log("新选中的二级菜单的class: "+$(this).attr("class"));
		$(this).parent().parent().removeClass("firstLevelMenuOn");
		$(this).parent().parent().addClass("firstLevelMenuselected");
		//console.log("新选中的一级菜单的class: "+$(this).parent().parent().attr("class"));
		//保存最后被选中的菜单
		secondLevelMenuselected = $(this).attr("id");
		//console.log("被选中的二级菜单："+secondLevelMenuselected);
		firstLevelMenuselected = $(this).parent().parent().attr("id");
		//console.log("被选中的一级菜单："+firstLevelMenuselected);
		
	});

	//保存设置信息事件
	$("#saveUserConfigBtn").click(function(){
		saveUserConfig();
	});
	
		//时间显示
		nowTime(document.getElementById('nowTime'));

		var defaultWorkZoneId = $("#defaultWorkZoneId").val();
		var dafaultWorkZoneName = $("#dafaultWorkZoneName").val();
		var defaultWorkZoneSiteId = $("#defaultWorkZoneSiteId").val();
		var defaultWorkZoneSiteName = $("#defaultWorkZoneSiteName").val();
		var userId = $("#userId").val();
		userPortalProjectId = $("#userPortalProjectId").val();

		//初始化第一个iframe
		//workZoneSiteBigFunction(defaultWorkZoneSiteId, defaultWorkZoneSiteName,
		//		defaultWorkZoneId, dafaultWorkZoneName, userPortalProjectId);//初始化第一个工作空间的功能标签

		//alert("高度= "+$("#workZoneFrame"+defaultWorkZoneId).css("height"));
		//alert("隐藏其他wzone iframe");
		//初始化其他的workzone iframe
		var wzIds = $("#hiddenWorkZoneIds").val();
		if (wzIds && wzIds != "") {
			var sss = wzIds.split(",");
			if (sss.length > 0) {
				for (i = 0; i < sss.length; i++) {
					if (sss[i] != "") {
						wzIdArray.push(sss[i]);
						initWorkZoneIframe(sss[i], userPortalProjectId);
					}
				}
			}
		}
		var frameName = "workZonesiteFrame" + defaultWorkZoneSiteId;
		workzoneSiteFrameName = frameName;

		
		$("#provincemenu").change(function() {
			getSubAreas("provincemenu", "citymenu", "市");
		});

	});



// 缓存工作空间功能模块TAB_HTML
var systemTabDivObj = new Object();
// 当前工作空间iframe
var workzoneFrameName = "";
// 当前工作空间功能标签iframe
var workzoneSiteFrameName = "";

// 用户对应门户方案ID
var userPortalProjectId = "";

// 窗口化大小自适应---begin
function windowsResize() {
	// //alert(document.body.clientWidth);
	if (document.getElementById("footermaindiv").style.display == "none") {
		var hight = document.body.clientHeight - 40;
		document.getElementById("maininnerdiv").style.width = "100%";
	}
}

$(window).resize(function() {
	if (window.timerLayout)
		clearTimeout(window.timerLayout);
	window.timerLayout = null;
	if ($.browser.msie) {
		window.timerLayout = setTimeout(windowsResize, 100);
	} else {
		window.timerLayout = setTimeout(windowsResize, 100);
	}
});
// 窗口化大小自适应---end

// 全屏显示hight
function fullsreenDisplay() {
	var topHeight = $("#topNavWrapper").height();
	var height = document.body.clientHeight - 40;

}
// 导航条
function navigationAction() {
	$('#nav').droppy( {
		speed : 100
	});
	$('#nav a').click(function(ev) {
		$('#nav a').removeClass('liSelected');
		$(this).addClass('liSelected');
		// ev.preventDefault();
		});
}

// 动态显示当时时间
// 当前是直接显示客户端时间，希望后续修改成服务器时间
function nowTime(ev, type) {
	/*
	 * ev:显示时间的元素 type:时间显示模式.若传入12则为12小时制,不传入则为24小时制
	 */
	// 年月日时分秒
	var Y, M, D, W, H, I, S;
	// 月日时分秒为单位时前面补零
	function fillZero(v) {
		if (v < 10) {
			v = '0' + v;
		}
		return v;
	}
	(function() {
		var d = new Date();
		var Week = [ '星期天', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六' ]
		Y = d.getFullYear();
		M = fillZero(d.getMonth() + 1);
		D = fillZero(d.getDate());
		W = Week[d.getDay()];
		H = fillZero(d.getHours());
		I = fillZero(d.getMinutes());
		S = fillZero(d.getSeconds());
		// 12小时制显示模式
		if (type && type == 12) {
			// 若要显示更多时间类型诸如中午凌晨可在下面添加判断
			if (H <= 12) {
				H = '上午&nbsp;' + H;
			} else if (H > 12 && H < 24) {
				H -= 12;
				H = '下午&nbsp;' + fillZero(H);
			} else if (H == 24) {
				H = '下午&nbsp;00';
			}
		}
		ev.innerHTML = Y + '年' + M + '月' + D + '日 ' + ' ' + W + '&nbsp;' + H
				+ ':' + I + ':' + S;
		// 每秒更新时间
		setTimeout(arguments.callee, 1000);
	})();
}

//点击工作空间的响应事件
function userWorkZoneAction(userId, workZoneId, workzoneName) {
	// 判断是否处于全屏模式
	if (document.getElementById("footermaindiv").style.display == "none") {
		var hight = document.body.clientHeight - 40;
		document.getElementById("footermaindiv").style.display = "";
		document.getElementById("maininnerdiv").style.width = "1100px";
		document.getElementById("fullscreendisplay_").innerHTML = "工作区全屏";
	}

	// 记录打开的工作空间ID
	$("#defaultWorkZoneId").val(workZoneId);
	$("#workzoneId_").val(workZoneId);
	$("#workzoneName").text(workzoneName);
	var frameName = "workZoneFrame" + workZoneId;
	workzoneFrameName = frameName;
	var queryFlag = "#" + frameName;
	// //alert($(queryFlag).length);
	// iframe存在
	if ($(queryFlag).length != 0) {
		////alert("存在：工作空间-》"+workzoneName);
		$("#maininnerdiv iframe").hide();
		$(queryFlag).show();

		// iframe不存在
	} else {
		var appendHTML = '<IFRAME name="'
				+ frameName
				+ '" id="'
				+ frameName
				+ '" class="portalIframe" style="WIDTH: 100%; marginheight:0px;marginwidth:0px;" src="portletIndexAction?workZoneId='
				+ workZoneId + '&schemeId=' + userPortalProjectId
				+ '" frameBorder=0 scrolling=auto></IFRAME>';

		$("#maininnerdiv").append(appendHTML);
		$("#maininnerdiv iframe").hide();
		$(queryFlag).show();

		var outHTML = "";
		// 组装TAB

	}
}

// 工作空间执行动作
function userWorkZoneAction_old(userId, workZoneId, workzoneName) {
	// 判断是否处于全屏模式
	if (document.getElementById("footermaindiv").style.display == "none") {
		var hight = document.body.clientHeight - 40;
		//document.getElementById("logo_search").style.display = "";
		//document.getElementById("logo_search2").style.display = "";
		document.getElementById("footermaindiv").style.display = "";
		document.getElementById("maininnerdiv").style.width = "1100px";
		//document.getElementById("maininnerdiv").style.height = "800px";
		//document.getElementById(workzoneFrameName).style.height = "800px";
		//document.getElementById(workzoneFrameName).contentWindow.document.getElementById(workzoneSiteFrameName).style.height = "800px";
		document.getElementById("fullscreendisplay_").innerHTML = "工作区全屏";
	}
	//document.getElementById("logo_search").style.display = "";
	//document.getElementById("logo_search2").style.display = "";
	// document.getElementById("displayIndex_").innerHTML = "【隐藏页首】";

	// 默认用户不显示
	if (userId == "default") {
		$('#workZoneSiteConfg').hide();
	} else {
		$('#workZoneSiteConfg').show();
	}

	// 记录打开的工作空间ID
	$("#defaultWorkZoneId").val(workZoneId);
	$("#workzoneId_").val(workZoneId);
	$("#workzoneName").text(workzoneName);
	var frameName = "workZoneFrame" + workZoneId;
	workzoneFrameName = frameName;
	var queryFlag = "#" + frameName;
	// //alert($(queryFlag).length);
	// iframe存在
	if ($(queryFlag).length != 0) {
		////alert("存在：工作空间-》"+workzoneName);
		$("#maininnerdiv iframe").hide();
		$(queryFlag).show();

		// eval("$('#systemTabDiv').html(unescape(systemTabDivObj['"+workZoneId+"']))");
		$("#systemTabDiv").html("");
		var tabpanel_ = unescape(systemTabDivObj[workZoneId]);
		eval(tabpanel_);

		navigationAction();

		// iframe不存在
	} else {
		////alert("不存在：工作空间-》"+workzoneName)
		// 2012-5-27 gmh modify
		// var appendHTML = '<IFRAME name="'+frameName+'" id="'+frameName+'"
		// class="portalIframe" style="WIDTH: 100%; HEIGHT:
		// 800px;marginheight:0px;marginwidth:0px;"
		// src="portletIndex?workZoneId='+workZoneId+'&userPortalProjectId='+userPortalProjectId+'"
		// frameBorder=0 scrolling=auto></IFRAME>';
		var appendHTML = '<IFRAME name="'
				+ frameName
				+ '" id="'
				+ frameName
				+ '" class="portalIframe" style="WIDTH: 100%; marginheight:0px;marginwidth:0px;" src="portletIndexAction?workZoneId='
				+ workZoneId + '&schemeId=' + userPortalProjectId
				+ '" frameBorder=0 scrolling=auto></IFRAME>';

		$("#maininnerdiv").append(appendHTML);
		$("#maininnerdiv iframe").hide();
		$(queryFlag).show();

		var outHTML = "";
		// 组装TAB
		$.ajax( {
			type : "POST",
			url : "getUserworkzonesiteListByWorkzoneIdAction",// "getUserworkzonesiteListByWorkzoneId.action",
			// //2012-5-27
			// gmh
			// modify
			data : "workZoneId=" + workZoneId + "&userPortalProjectId="
					+ userPortalProjectId + "&schemeId=" + userPortalProjectId,
			async : true,
			success : function(data) {
				var obj = eval("(" + data + ")");//

			var outHTML = "";
			// 注释掉jquery的tabpanel，改用下面的ext的tabpanel

			outHTML += "var  tabpanel = new TabPanel({\n";
			outHTML += "renderTo:'systemTabDiv',\n";
			outHTML += "width:800,\n";
			outHTML += "height:30,\n";
			outHTML += "autoResizable:true,\n";
			outHTML += "border:'none',\n";
			outHTML += "active : 0,\n";
			outHTML += "items : [";

			for ( var i = 0; i < obj.result.length; i++) {

				if (i == 0) {
					workzoneSiteFrameName = "workZonesiteFrame"
							+ obj.result[i].id;
				}
				if (i != obj.result.length - 1) {
					outHTML += "{id:'" + obj.result[i].id + "',parentId:'"
							+ workZoneId + "',title:'"
							+ obj.result[i].workItemName
							+ "',closable:false,autoExpand:true},\n";
				} else {
					outHTML += "{id:'" + obj.result[i].id + "',parentId:'"
							+ workZoneId + "',title:'"
							+ obj.result[i].workItemName
							+ "',closable:false,autoExpand:true}\n";
				}
			}

			outHTML += "]});";
			$("#systemTabDiv").html("");
			eval(outHTML);

			// //alert($("#systemTabDiv").html());

			// $("#systemTabDiv").html(outHTML);
			navigationAction();
			// 缓存工作空间功能模块TAB_HTML
			// var outHTML_ = escape($("#systemTabDiv").html());
			var outHTML_ = escape(outHTML);
			eval("systemTabDivObj['" + workZoneId + "']=\"" + outHTML_ + "\"");

		}
		});

	}

}

// 显示页首控制
function displayIndex() {
	var indexObj = document.getElementById("logo_search").style;
	if (indexObj.display == "") {
		document.getElementById("displayIndex_").innerHTML = "【显示页首】";
		indexObj.display = "none";
	} else {
		document.getElementById("displayIndex_").innerHTML = "【隐藏页首】";
		indexObj.display = "";
	}

}


// 工作空间功能详细设置
function workZoneSiteConfigAction() {
	var currentWorkZoneId = $("#defaultWorkZoneId").val();
	var workzoneName = escape(escape($("#workzoneName").text()));
	var result = showModalDialog(
			'workzonesiteConfg.action?userPortalProjectId='
					+ userPortalProjectId + '&workzoneName=' + workzoneName
					+ '&currentWorkZoneId=' + currentWorkZoneId,
			'',
			'dialogWidth:900px;dialogHeight:600px;center:yes;help:no;resizable:no;status:no;scroll:no;');
	if (result == '1') {
		window.location.reload();
	}
}

function workZoneSiteBigFunction(workzonesiteId, workzonesiteName, workZoneId,
		workZoneName, userPortalProjectId) {
	$.each($("#showWorkZoneSiteListDiv").children("ul"), function(i, ele) {
		if ($(ele).attr("id") == "workZoneSiteHiddenDisplay_" + workZoneId) {
			$(ele).css("display", "block");
		} else {
			$(ele).css("display", "none");
		}
	});

	// console.log("功能标签栏放置成功");
	//真正加载功能标签下的内容
	userWorkZoneSiteAction(workzonesiteId, workzonesiteName, workZoneId,
			workZoneName, userPortalProjectId);
}

// 功能模块执行动作
function userWorkZoneSiteAction(workzonesiteId, workzonesiteName, workZoneId,
		workZoneName, userPortalProjectId) {
	////alert("workzonesiteId = "+workzonesiteId+",workzonesiteName = "+workzonesiteName+",workZoneId = "+workZoneId);
	//获取最高页面高度
	var maxHeight;
	$.ajax( {
		url : "getPortalMaxHeight",
		async : false,
		type : "POST",
		dataType : 'json',
		success : function(result) {
			if (result) {
				//alert(result+"--data");
		maxHeight = result;
	}
}
	});
	/*
	if(oriMainDivHeight=="800px"){
	   oriMainDivHeight=$("#maindiv").css("height");
	   var num=new Number(oriMainDivHeight);
	   if(num<800){
	      //最小都要是800px
	      oriMainDivHeight="801px";
	   }
	}
	 * */
	//所在的zone的frame的名称，也是id
	var parent_frameName = "workZoneFrame" + workZoneId;
	workzoneFrameName = parent_frameName;
	var queryFlag = "#" + parent_frameName;
	var workZoneSiteFrameId = "workZonesiteFrame" + workzonesiteId;
	workzoneSiteFrameName = workZoneSiteFrameId;

	//隐藏旧的workzone frame
	if ($("#" + $("#currentWorkZoneFrameId").val())) {
		$("#" + $("#currentWorkZoneFrameId").val()).hide();
	}
	//隐藏旧的workzonesite frame
	if ($("#" + $("#currentWorkZoneSiteFrameId").val())) {
		$("#" + $("#currentWorkZoneSiteFrameId").val()).hide();
	}
	//记录打开的工作空间
	$("#defaultWorkZoneId").val(workZoneId);
	$("#workzoneId_").val(workZoneId);
	$("#dafaultWorkZoneName").val(workZoneName);

	//点击'集中调度' , 不记录当前点击记录
	if (workzonesiteName != "集中调度" && workzonesiteName != "权限管理" ) {
		$("#defaultWorkZoneSiteName").val(workzonesiteName);
		$("#defaultWorkZoneSiteId").val(workzonesiteId);
	}
	$("#currentWorkZoneSiteFrameId").val(workZoneSiteFrameId);
	$("#currentWorkZoneFrameId").val(parent_frameName);

	//alert(maxHeight);

	/////alert("判断是否存在zone 的frame");
	if (!window.frames[parent_frameName]) {
		//不存在
		////alert("不存在 zone frame："+parent_frameName);
		noeffect = true;
		var appendHTML = '<IFRAME name="'
				+ workzoneFrameName
				+ '" id="'
				+ workzoneFrameName
				+ '" class="portalIframe" style="WIDTH: 100%; height:800px; marginheight:0px;marginwidth:0px;" src="portletIndexAction?workZoneId='
				+ workZoneId + '&schemeId=' + userPortalProjectId
				+ '&workzonesiteId=' + workzonesiteId + '&workzonesiteName='
				+ workzonesiteName + '&height=' + oriMainDivHeight
				+ '" frameBorder=0 scrolling=auto></IFRAME>';

		$("#maininnerdiv").append(appendHTML);
		$("#maininnerdiv iframe").hide();
		$("#" + workzoneFrameName).show();

	} else {
		//寻找高度最高的iframe

		var hi = 800, temp = 800;
		var sh = '', finalhi = '';
		for ( var i in wzIdArray) {
			sh = $("#workZoneFrame" + wzIdArray[i]).css("height");
			//console.log("workZoneFrame"+wzIdArray[i]+" height = "+sh);
			temp = parseInt(sh);
			//console.log("temp = "+temp);
			if (temp > hi) {
				// console.log(temp+"大于"+hi);
				hi = temp;
				finalhi = sh;
			}
		}
		finalhi = parseInt(finalhi);
		// alert("maxHeight="+maxHeight+",finalhi="+finalhi);
		if (finalhi > maxHeight) {
			maxHeight = finalhi;

		}

		//console.log("确定最高的workzone 的iframe的高度为:"+hi);
		//$("#"+workzoneFrameName).css("height",maxHeight+"px");
		$("#" + workzoneFrameName).show();
		noeffect = false;
	}

	//'集中调度'   特殊跳转至新页面
	var jumpUrl;
	if (workzonesiteName == "集中调度" || workzonesiteName == "权限管理") {
		$.ajax( {
			url : "getUnderWorkZoneSitePortletUrl?workzonesiteName="
					+ workzonesiteName + "&workzonesiteId=" + workzonesiteId
					+ "&workZoneId=" + workZoneId,
			async : false,
			type : "POST",
			dataType : 'json',
			success : function(result) {
				if (result) {
					//alert(result+"--data");
			jumpUrl = result;
		}
	}
		});
		window.open(jumpUrl);
		var oldClickTabName = $("#defaultWorkZoneSiteName").val();
		var defaultHeight = "800px";
		if (oldClickTabName == "我的首页") {
			defaultHeight = maxHeight + "px";
		}
		setPageIframeHeight(defaultHeight, workZoneId, workZoneSiteFrameId);

	} else {

		var worzoneSiteFrameDiv = $(window.frames[parent_frameName].document)
				.find("#workZonesiteFrame_");
		var worzoneSiteFrame = worzoneSiteFrameDiv.find("#"
				+ workZoneSiteFrameId);

		if (worzoneSiteFrame.length != 0) {//已存在workZoneSite的Iframe
			worzoneSiteFrameDiv.find("iframe").hide();
			worzoneSiteFrameDiv.find("#" + workZoneSiteFrameId).show();

			var defaultHeight = "800px";
			if (workzonesiteName == "我的首页") {
				defaultHeight = maxHeight + "px";
			}
			setPageIframeHeight(defaultHeight, workZoneId, workZoneSiteFrameId);

		} else {//不存在workZoneSite的Iframe
			worzoneSiteFrameDiv.find("iframe").hide();
			var appendHTML = '<IFRAME name="' + workZoneSiteFrameId + '" id="'
					+ workZoneSiteFrameId + '" style="WIDTH: 100%;HEIGHT:'
					+ oriMainDivHeight
					+ '" src="getPorletAction?workzonesiteId=' + workzonesiteId
					+ '&workZoneId=' + workZoneId + '&workzonesiteName='
					+ workzonesiteName
					+ '" frameBorder=0 scrolling=no></IFRAME>';

			worzoneSiteFrameDiv.append(appendHTML);
			worzoneSiteFrameDiv.find("#" + workZoneSiteFrameId).show();

			var defaultHeight = "800px";
			if (workzonesiteName == "我的首页") {
				defaultHeight = maxHeight + "px";
			}
			setPageIframeHeight(defaultHeight, workZoneId, workZoneSiteFrameId);
			////alert("准备显示新建的site frame");

		}
		////alert("显示完 site frame 后");

	}
	var oldClickTabName = $("#defaultWorkZoneSiteName").val();
	var ul = $("#workZoneSiteHiddenDisplay_" + workZoneId);
	$.each($(ul).children("li"), function(i, ele) {
		if ($(ele).text() == oldClickTabName) {
			$(ele).addClass("main_nav_on");
		} else {
			$(ele).removeClass("main_nav_on");
		}
	});

}

function setPageIframeHeight(defaultHeight, workZoneId, workZoneSiteFrameId) {
	var zoneFrame = window.top.document.getElementById("workZoneFrame"
			+ workZoneId);
	var topmaindiv = window.top.document.getElementById("maininnerdiv");
	setInterval(function() {

		var docH = document.documentElement.clientHeight;
		var headH = $(".header").height();
		var navH = $(".main_nav").height();
		var footerH = $("#footermaindiv").height();
		var contentH = docH - headH - navH - footerH - 40;
		$("#maindiv").css("height", contentH);
		$("#maininnerdiv").css("height", contentH - 4 + "px");
		$("#maininnerdivif").css("height", contentH - 4 + "px");
		$(".portalIframe").css("height", contentH - 4 + "px");
		var parent_frameName = "workZoneFrame" + workZoneId;
		var worzoneSiteFrameDiv = $(window.frames[parent_frameName].document)
				.find("#workZonesiteFrame_");
				
//门户各模块高度因为出现滚动条的情况百花齐放,现在的解决方法是硬编码的,属于下策（可以考虑更换页面结构，统一实现方式）

		//(我的首页、组织管理、信息发布、权限管理、门户后台管理、工单管理)   五个功能标签例外（滚动条处理）
			if (workZoneSiteFrameId == "workZonesiteFrame1318581008644"
					|| workZoneSiteFrameId == "workZonesiteFrame1339694459241"
					|| workZoneSiteFrameId == "workZonesiteFrame1338825211456"
					|| workZoneSiteFrameId == "workZonesiteFrame1355986519052"
						|| workZoneSiteFrameId == "workZonesiteFrame1355986519050"
						|| workZoneSiteFrameId == "workZonesiteFrame1319797403396"
						||workZoneSiteFrameId == "workZonesiteFrame1355986519049" //项目信息管理
						) {
				worzoneSiteFrameDiv.find("#" + workZoneSiteFrameId).css(
						"height", defaultHeight);
			} else if(workZoneSiteFrameId == "workZonesiteFrame1335289613104"){//自适应对 '车辆管理' 模块 无效，且默认进入页面高度大于800，为避免出现内部滚动条，默认进去高度需变大 
				worzoneSiteFrameDiv.find("#" + workZoneSiteFrameId).css(
						"height", "810px");
			}else{
				worzoneSiteFrameDiv.find("#" + workZoneSiteFrameId).css(
							"height", contentH - 4 + "px");
			}

		}, 200)

	//$(".content_right").css("height",contentH);
	//$("#view_window").css("height",contentH - 28);
}
function gotoUrl(url) {
	window.open(url);
}

function initWorkZoneIframe(workZoneId, userPortalProjectId) {
	var parent_frameName = "workZoneFrame" + workZoneId;

	if (!window.frames[parent_frameName]) {
		var appendHTML = '<IFRAME name="'
				+ parent_frameName
				+ '" id="'
				+ parent_frameName
				+ '" class="portalIframe" style="display:none;WIDTH: 100%;marginheight:0px;marginwidth:0px;" src="portletIndexAction?workZoneId='
				+ workZoneId + '&schemeId=' + userPortalProjectId
				+ '" frameBorder=0 scrolling=auto></IFRAME>';
	}
	$("#maininnerdiv").append(appendHTML);
}

//保存用户配置
function saveUserConfig(){
	var cityId = $("#citymenu").val();
	$.ajax({
		url : "saveUserConfigAction",
		type : "POST",
		data : {
			'cityId' : cityId
		},
		dataType : 'json',
		success : function(result) {
			var data = eval("("+raw+")");
			var flag = data['flag'];
			if(!flag){
				alert("保存配置失败！");
			}
		}
	});
}

//Liang YJ 2014-4-4 一下方法showChildrenMenu、hideChildrenMenu、showContent、changeMenuStyle、revertMenuStyle不再使用，改由jquery绑定
/**
 * @author Liang YJ
 * @date 2014-4-1 10:25
 * @param obj
 * div
 * @description 当鼠标悬停在一级菜单时弹出对应的二级菜单
 *//*
function showChildrenMenu(obj){
	var id = $(obj).text();
	if(firstLevelMenu != id){
		$(obj).css("background","url('../home/resource/images/blue.gif') repeat-x scroll 0 0 #FFFFFF");		
	}
	$("#"+id).css("display","block"); 
}

*//**
 * @author Liang YJ
 * @date 2014-4-1 11:10
 * @param obj
 * li
 * @description 当鼠标离开一级和二级菜单时，收起二级菜单
 *//*
function hideChildrenMenu(obj){
	var id = $(obj).children("div").text();
	if(firstLevelMenu != id){
		$(obj).children("div").css("background","url('../home/resource/images/nav_back.png') repeat-x scroll 0 0 #FFFFFF");
	}
	$("#"+id).css("display","none"); 		
}

*//**
 * @author Liang YJ
 * @date 2014-4-1 13:34
 * @param href
 * 二级菜单对应的相对地址
 * @param obj
 * 二级菜单<li></li>
 * @description 点击二级菜单，iframe的src值转变成相应的href
 *//*
function showContent(href,obj){
	$("#maininnerdivif").attr("src",href);
	$("#"+selectedMenu).css("background","url('../home/resource/images/secondLevelMenu3.png') repeat-x scroll 0 0 #FFFFFF");
	$("#"+selectedMenu).parent().parent().children("div").css("background","url('../home/resource/images/nav_back.png') repeat-x scroll 0 0 #FFFFFF");
	$(obj).css("background","url('../home/resource/images/blue.gif') repeat-x scroll 0 0 #FFFFFF");
	$(obj).parent().parent().children("div").css("background","url('../home/resource/images/menu2.gif') repeat-x scroll 0 0 #FFFFFF");
	//保存最后被选中的菜单
	selectedMenu = $(obj).text();
	firstLevelMenu = $(obj).parent().parent().children("div").text();
	console.log("被选择的1级菜单："+firstLevelMenu);
}

*//**
 * @author Liang YJ
 * @date 2014-4-4 10:30
 * @param obj
 * 二级菜单
 * @description 当鼠标进入二级菜单时，改变二级菜单的样式
 *//*
function changeMenuStyle(obj){
	if($(obj).attr("id") != selectedMenu){
		$(obj).css("background","url('../home/resource/images/blue.gif') repeat-x scroll 0 0 #FFFFFF");
	}
}

*//**
 * @author Liang YJ
 * @date 2014-4-4 10:37
 * @param obj
 * 二级菜单
 * @description 当鼠标进入二级菜单时，改变二级菜单的样式
 *//*
function revertMenuStyle(obj){
	if($(obj).attr("id") != selectedMenu){
		$(obj).css("background","url('../home/resource/images/secondLevelMenu3.png') repeat-x scroll 0 0 #FFFFFF");		
	}
}*/
